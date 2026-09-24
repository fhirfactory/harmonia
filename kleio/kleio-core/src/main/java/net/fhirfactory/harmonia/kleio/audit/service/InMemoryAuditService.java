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

package net.fhirfactory.harmonia.kleio.audit.service;

import net.fhirfactory.harmonia.kleio.audit.model.AuditOutcome;
import net.fhirfactory.harmonia.kleio.audit.model.AuditQuery;
import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * In-memory thread-safe implementation of {@link AuditService} enforcing append-only semantics,
 * idempotent replay on identical events, and integrity violation detection on conflicting event IDs.
 */
public class InMemoryAuditService implements AuditService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryAuditService.class);
    private static final int DEFAULT_MAX_CAPACITY = 5000;
    private static final Comparator<HarmoniaAuditEvent> CANONICAL_ORDER = Comparator
            .comparing(HarmoniaAuditEvent::recordedAt, Comparator.reverseOrder())
            .thenComparing(HarmoniaAuditEvent::eventId, Comparator.reverseOrder());

    private final int maxCapacity;
    private final List<HarmoniaAuditEvent> eventLog = new ArrayList<>();
    private final Map<String, HarmoniaAuditEvent> eventsById = new HashMap<>();

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public InMemoryAuditService() {
        this(DEFAULT_MAX_CAPACITY);
    }

    public InMemoryAuditService(int maxCapacity) {
        this.maxCapacity = maxCapacity > 0 ? maxCapacity : DEFAULT_MAX_CAPACITY;
    }

    @Override
    public HarmoniaAuditEvent append(HarmoniaAuditEvent event) {
        Objects.requireNonNull(event, "event must not be null");

        boolean isNewAppend = false;

        writeLock.lock();
        try {
            HarmoniaAuditEvent existing = eventsById.get(event.eventId());
            if (existing != null) {
                if (existing.equals(event)) {
                    // Idempotent replay: return existing event without log growth or eviction
                    return existing;
                }
                throw new AuditIntegrityException(
                        event.eventId(),
                        "Conflicting audit event with ID " + event.eventId() + " already exists with divergent content"
                );
            }

            // Genuinely new append: trim ring buffer if at capacity
            while (eventLog.size() >= maxCapacity && !eventLog.isEmpty()) {
                HarmoniaAuditEvent evicted = eventLog.remove(0);
                if (evicted != null) {
                    eventsById.remove(evicted.eventId());
                }
            }

            eventLog.add(event);
            eventsById.put(event.eventId(), event);
            isNewAppend = true;
        } finally {
            writeLock.unlock();
        }

        if (isNewAppend) {
            logAuditEvent(event);
        }

        return event;
    }

    @Override
    public Optional<HarmoniaAuditEvent> findById(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            return Optional.empty();
        }
        readLock.lock();
        try {
            return Optional.ofNullable(eventsById.get(eventId));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<HarmoniaAuditEvent> find(AuditQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        readLock.lock();
        try {
            return eventLog.stream()
                    .filter(query::matches)
                    .sorted(CANONICAL_ORDER)
                    .limit(query.limit())
                    .toList();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<HarmoniaAuditEvent> findByCorrelationId(String correlationId) {
        if (correlationId == null || correlationId.isBlank()) {
            return List.of();
        }
        readLock.lock();
        try {
            return eventLog.stream()
                    .filter(e -> correlationId.equals(e.correlationId()))
                    .toList();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<HarmoniaAuditEvent> findByPrincipal(String principalId) {
        if (principalId == null || principalId.isBlank()) {
            return List.of();
        }
        readLock.lock();
        try {
            return eventLog.stream()
                    .filter(e -> (e.initiatingPrincipal() != null && principalId.equals(e.initiatingPrincipal().principalId()))
                            || (e.executingPrincipal() != null && principalId.equals(e.executingPrincipal().principalId()))
                            || principalId.equals(e.principalId()))
                    .toList();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<HarmoniaAuditEvent> getRecentEvents(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        readLock.lock();
        try {
            int size = eventLog.size();
            int count = Math.min(limit, size);
            List<HarmoniaAuditEvent> result = new ArrayList<>(count);
            for (int i = size - 1; i >= size - count; i--) {
                result.add(eventLog.get(i));
            }
            return Collections.unmodifiableList(result);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public HarmoniaAuditEvent recordDecision(ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision) {
        if (request == null || decision == null) {
            return null;
        }
        HarmoniaAuditEvent event = HarmoniaAuditEvent.fromDecision(request, decision);
        return append(event);
    }

    /**
     * Clears all recorded audit events from in-memory storage (retained strictly for test harnesses and resets).
     */
    public void clear() {
        writeLock.lock();
        try {
            eventLog.clear();
            eventsById.clear();
        } finally {
            writeLock.unlock();
        }
    }

    private void logAuditEvent(HarmoniaAuditEvent event) {
        if (event.outcome() == AuditOutcome.DENIED || (event.decision() != null && event.decision() == ThemisDecision.DENY)) {
            log.warn("[AUDIT] Security Decision: DENIED | id={} | principal={} | action={} | resource={}/{} | domain={} | policy={} | reason={} | correlationId={}",
                    event.eventId(), event.principalId(), event.action(), event.resourceType(), event.resourceId(),
                    event.securityDomain(), event.policyId(), event.reason(), event.correlationId());
        } else {
            log.info("[AUDIT] Security Decision: ALLOWED | id={} | principal={} | action={} | resource={}/{} | domain={} | policy={} | correlationId={}",
                    event.eventId(), event.principalId(), event.action(), event.resourceType(), event.resourceId(),
                    event.securityDomain(), event.policyId(), event.correlationId());
        }
    }
}
