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

package net.fhirfactory.harmonia.themis.audit.service;

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.audit.model.ThemisAuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * In-memory thread-safe implementation of {@link ThemisAuditService}.
 * Logs non-sensitive audit records tagged with the AUDIT security classification.
 */
public class InMemoryThemisAuditService implements ThemisAuditService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryThemisAuditService.class);
    private static final int DEFAULT_MAX_CAPACITY = 5000;

    private final int maxCapacity;
    private final List<ThemisAuditEvent> eventLog = new CopyOnWriteArrayList<>();

    public InMemoryThemisAuditService() {
        this(DEFAULT_MAX_CAPACITY);
    }

    public InMemoryThemisAuditService(int maxCapacity) {
        this.maxCapacity = maxCapacity > 0 ? maxCapacity : DEFAULT_MAX_CAPACITY;
    }

    @Override
    public ThemisAuditEvent recordDecision(ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision) {
        if (request == null || decision == null) {
            return null;
        }

        String principalId = request.principal() != null ? request.principal().principalId() : "unknown";
        var principalType = request.principal() != null ? request.principal().principalType() : null;
        String sourceDomain = request.principal() != null ? request.principal().sourceDomain() : null;
        String resourceType = request.target() != null ? request.target().resourceType() : null;
        String resourceId = request.target() != null ? request.target().resourceId() : null;
        String securityDomain = request.target() != null ? request.target().securityDomain() : null;
        var labels = request.target() != null ? request.target().securityLabels() : java.util.Set.<net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel>of();
        String correlationId = request.context() != null ? request.context().correlationId() : decision.correlationId();
        String causationId = request.context() != null ? request.context().causationId() : null;

        ThemisAuditEvent event = ThemisAuditEvent.builder()
                .decisionId(decision.decisionId())
                .decision(decision.decision())
                .reason(decision.reason())
                .policyId(decision.policyId())
                .principalId(principalId)
                .principalType(principalType)
                .sourceDomain(sourceDomain)
                .action(request.action())
                .resourceType(resourceType)
                .resourceId(resourceId)
                .securityDomain(securityDomain)
                .securityLabels(labels)
                .correlationId(correlationId)
                .causationId(causationId)
                .timestamp(decision.evaluatedAt())
                .classification(ThemisAuditEvent.CLASSIFICATION_AUDIT)
                .build();

        recordEvent(event);
        return event;
    }

    @Override
    public void recordEvent(ThemisAuditEvent event) {
        if (event == null) {
            return;
        }

        if (eventLog.size() >= maxCapacity) {
            // Trim oldest events
            int removeCount = eventLog.size() - maxCapacity + 1;
            for (int i = 0; i < removeCount && !eventLog.isEmpty(); i++) {
                eventLog.remove(0);
            }
        }

        eventLog.add(event);

        if (event.decision() == ThemisDecision.DENY) {
            log.warn("[AUDIT] Security Decision: DENIED | id={} | principal={} | action={} | resource={}/{} | domain={} | policy={} | reason={} | correlationId={}",
                    event.decisionId(), event.principalId(), event.action(), event.resourceType(), event.resourceId(),
                    event.securityDomain(), event.policyId(), event.reason(), event.correlationId());
        } else {
            log.info("[AUDIT] Security Decision: ALLOWED | id={} | principal={} | action={} | resource={}/{} | domain={} | policy={} | correlationId={}",
                    event.decisionId(), event.principalId(), event.action(), event.resourceType(), event.resourceId(),
                    event.securityDomain(), event.policyId(), event.correlationId());
        }
    }

    @Override
    public List<ThemisAuditEvent> getRecentEvents(int limit) {
        int max = limit > 0 ? limit : 50;
        List<ThemisAuditEvent> copy = new ArrayList<>(eventLog);
        Collections.reverse(copy);
        return copy.stream().limit(max).collect(Collectors.toList());
    }

    @Override
    public List<ThemisAuditEvent> getEventsByCorrelationId(String correlationId) {
        if (correlationId == null) {
            return List.of();
        }
        return eventLog.stream()
                .filter(e -> correlationId.equals(e.correlationId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ThemisAuditEvent> getEventsByPrincipal(String principalId) {
        if (principalId == null) {
            return List.of();
        }
        return eventLog.stream()
                .filter(e -> principalId.equals(e.principalId()))
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        eventLog.clear();
    }
}
