/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.kleio.persistence.service;

import ca.uhn.fhir.context.FhirContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import net.fhirfactory.harmonia.kleio.audit.model.AuditQuery;
import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import net.fhirfactory.harmonia.kleio.audit.service.AuditIntegrityException;
import net.fhirfactory.harmonia.kleio.audit.service.AuditService;
import net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper;
import net.fhirfactory.harmonia.kleio.persistence.exception.AuditPersistenceException;
import net.fhirfactory.harmonia.kleio.persistence.model.PersistedAuditEventRow;
import net.fhirfactory.harmonia.kleio.persistence.repository.AppendOnlyAuditEventRepository;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import org.hl7.fhir.r5.model.AuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Production implementation of {@link AuditService} providing durable, append-only persistence
 * backed by PostgreSQL via {@link AppendOnlyAuditEventRepository}.
 * <p>
 * Guarantees that acknowledgement indicates durable commitment to the database, prevents tampering
 * on replay, rejects encounters with soft-deleted rows, and provides deterministic chronological keyset paging.
 */
@ApplicationScoped
public class DurableAuditService implements AuditService {

    private static final Logger log = LoggerFactory.getLogger(DurableAuditService.class);

    private static final int DEFAULT_PAGE_SIZE = 200;
    private static final int MAX_SCAN_ROWS = 10000;

    private static final Comparator<HarmoniaAuditEvent> CANONICAL_ORDER = Comparator
            .comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder())
            .thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder());

    private final AppendOnlyAuditEventRepository repository;
    private final HarmoniaAuditEventMapper mapper;
    private final FhirContext fhirContext;

    /**
     * Protected no-arg constructor required for CDI proxy generation.
     */
    protected DurableAuditService() {
        this.repository = null;
        this.mapper = null;
        this.fhirContext = null;
    }

    /**
     * CDI-injected constructor with complete component dependencies.
     *
     * @param repository  append-only repository adapter
     * @param mapper      canonical to FHIR R5 mapper
     * @param fhirContext HAPI FHIR R5 context
     */
    @Inject
    public DurableAuditService(
            AppendOnlyAuditEventRepository repository,
            HarmoniaAuditEventMapper mapper,
            FhirContext fhirContext
    ) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper must not be null");
        this.fhirContext = Objects.requireNonNull(fhirContext, "fhirContext must not be null");
    }

    /**
     * Test convenience constructor initializing mapper and FHIR context automatically.
     *
     * @param repository append-only repository adapter
     */
    public DurableAuditService(AppendOnlyAuditEventRepository repository) {
        this(repository, new HarmoniaAuditEventMapper(), FhirContext.forR5());
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public HarmoniaAuditEvent append(HarmoniaAuditEvent event) {
        Objects.requireNonNull(event, "event must not be null");

        String eventId = event.eventId();

        // 1. Serialize to FHIR R5 JSON
        String resourceJson;
        try {
            AuditEvent fhirEvent = mapper.toFhir(event);
            resourceJson = fhirContext.newJsonParser().encodeResourceToString(fhirEvent);
        } catch (Exception e) {
            log.error("Failed to serialize audit event to FHIR R5: eventId={}", eventId, e);
            throw new AuditPersistenceException("Failed to serialize audit event with ID " + eventId, e);
        }

        // 2. Insert attempt
        boolean inserted = repository.insertIfAbsent(eventId, resourceJson, Instant.now());

        if (inserted) {
            log.info("[AUDIT] Appended durable audit event: id={}, classification={}, action={}, outcome={}",
                    event.eventId(), event.classification(), event.action(), event.outcome());
            return event;
        }

        // 3. Conflict resolution branch
        log.debug("Conflict encountered during audit append; resolving replay for eventId={}", eventId);

        Optional<PersistedAuditEventRow> existingRowOpt = repository.findByEventId(eventId);
        if (existingRowOpt.isEmpty()) {
            throw new AuditPersistenceException("Audit event with ID " + eventId + " encountered conflict but could not be loaded");
        }

        PersistedAuditEventRow row = existingRowOpt.get();

        if (row.isDeleted()) {
            log.warn("[AUDIT] Integrity violation: soft-deleted record encountered on append replay: id={}", eventId);
            throw new AuditIntegrityException(eventId,
                    "Audit integrity violation: event with ID " + eventId + " exists but is marked deleted");
        }

        HarmoniaAuditEvent existingEvent;
        try {
            AuditEvent existingFhir = fhirContext.newJsonParser().parseResource(AuditEvent.class, row.resourceJson());
            existingEvent = mapper.fromFhir(existingFhir);
        } catch (Exception e) {
            log.error("Persisted audit event contains unparseable JSON: id={}", eventId, e);
            throw new AuditIntegrityException(eventId,
                    "Audit integrity violation: persisted audit event with ID " + eventId + " contains malformed JSON", e);
        }

        if (existingEvent.equals(event)) {
            log.debug("Idempotent replay of existing audit event: id={}", eventId);
            return existingEvent;
        }

        log.warn("[AUDIT] Integrity violation: divergent content collision on append: id={}", eventId);
        throw new AuditIntegrityException(eventId,
                "Conflicting audit event with ID " + eventId + " already exists with divergent content");
    }

    @Override
    public Optional<HarmoniaAuditEvent> findById(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            return Optional.empty();
        }

        Optional<PersistedAuditEventRow> rowOpt = repository.findByEventId(eventId);
        if (rowOpt.isEmpty()) {
            return Optional.empty();
        }

        PersistedAuditEventRow row = rowOpt.get();

        if (row.isDeleted()) {
            log.warn("[AUDIT] Integrity violation: soft-deleted record encountered on point read: id={}", eventId);
            throw new AuditIntegrityException(eventId,
                    "Audit integrity violation: event with ID " + eventId + " exists but is marked deleted");
        }

        try {
            AuditEvent fhir = fhirContext.newJsonParser().parseResource(AuditEvent.class, row.resourceJson());
            return Optional.of(mapper.fromFhir(fhir));
        } catch (Exception e) {
            log.error("Persisted audit event contains unparseable JSON: id={}", eventId, e);
            throw new AuditIntegrityException(eventId,
                    "Audit integrity violation: persisted audit event with ID " + eventId + " contains malformed JSON", e);
        }
    }

    @Override
    public Optional<HarmoniaAuditEvent> get(String eventId) {
        return findById(eventId);
    }

    @Override
    public List<HarmoniaAuditEvent> find(AuditQuery query) {
        Objects.requireNonNull(query, "query must not be null");

        List<HarmoniaAuditEvent> matching = new ArrayList<>();
        long cursorId = Long.MAX_VALUE;
        int totalScanned = 0;
        final int pageSize = DEFAULT_PAGE_SIZE;

        while (matching.size() < query.limit() && totalScanned < MAX_SCAN_ROWS) {
            int toFetch = Math.min(pageSize, MAX_SCAN_ROWS - totalScanned);
            List<PersistedAuditEventRow> page = repository.findCandidatePage(cursorId, toFetch);
            if (page.isEmpty()) {
                break;
            }

            for (PersistedAuditEventRow row : page) {
                cursorId = row.id();
                totalScanned++;

                if (row.isDeleted()) {
                    log.warn("[AUDIT] Integrity violation: soft-deleted record in candidate query scan: id={}", row.fhirId());
                    throw new AuditIntegrityException(row.fhirId(),
                            "Audit integrity violation: audit event with ID " + row.fhirId() + " in query stream is marked deleted");
                }

                HarmoniaAuditEvent candidate;
                try {
                    AuditEvent fhir = fhirContext.newJsonParser().parseResource(AuditEvent.class, row.resourceJson());
                    candidate = mapper.fromFhir(fhir);
                } catch (Exception e) {
                    log.error("Persisted candidate audit event contains unparseable JSON: id={}", row.fhirId(), e);
                    throw new AuditIntegrityException(row.fhirId(),
                            "Audit integrity violation: persisted audit event with ID " + row.fhirId() + " contains malformed JSON", e);
                }

                if (query.matches(candidate)) {
                    matching.add(candidate);
                    if (matching.size() == query.limit()) {
                        break;
                    }
                }
            }

            if (page.size() < toFetch) {
                break;
            }
        }

        if (totalScanned >= MAX_SCAN_ROWS && matching.size() < query.limit()) {
            if (repository.hasMoreRows(cursorId)) {
                log.error("Audit query scan ceiling of {} rows exceeded before query criteria completed", MAX_SCAN_ROWS);
                throw new AuditPersistenceException(
                        "Audit query scan ceiling of " + MAX_SCAN_ROWS + " rows exceeded before query criteria could be completed");
            }
        }

        matching.sort(CANONICAL_ORDER);
        return Collections.unmodifiableList(matching);
    }

    @Override
    public List<HarmoniaAuditEvent> findByCorrelationId(String correlationId) {
        if (correlationId == null || correlationId.isBlank()) {
            return List.of();
        }
        return find(AuditQuery.builder().correlationId(correlationId).limit(AuditQuery.MAX_LIMIT).build());
    }

    @Override
    public List<HarmoniaAuditEvent> findByPrincipal(String principalId) {
        if (principalId == null || principalId.isBlank()) {
            return List.of();
        }
        return find(AuditQuery.builder().principalId(principalId).limit(AuditQuery.MAX_LIMIT).build());
    }

    @Override
    public List<HarmoniaAuditEvent> getRecentEvents(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        return find(AuditQuery.builder().limit(limit).build());
    }

    @Override
    public HarmoniaAuditEvent recordDecision(ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision) {
        if (request == null || decision == null) {
            return null;
        }
        HarmoniaAuditEvent event = HarmoniaAuditEvent.fromDecision(request, decision);
        return append(event);
    }
}
