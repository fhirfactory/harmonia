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

package net.fhirfactory.harmonia.kleio.audit.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Immutable canonical query criteria for searching structured audit events.
 * Supports bounded results, chronological filtering, and predicate evaluation.
 */
public record AuditQuery(
        String eventId,
        Instant startTime,
        Instant endTime,
        AuditClassification classification,
        String principalId,
        String targetId,
        AuditAction action,
        AuditOutcome outcome,
        String correlationId,
        String operationId,
        int limit
) implements Serializable, Predicate<HarmoniaAuditEvent> {

    public static final int DEFAULT_LIMIT = 50;
    public static final int MAX_LIMIT = 1000;

    public AuditQuery {
        eventId = eventId != null && !eventId.isBlank() ? eventId.trim() : null;
        principalId = principalId != null && !principalId.isBlank() ? principalId.trim() : null;
        targetId = targetId != null && !targetId.isBlank() ? targetId.trim() : null;
        correlationId = correlationId != null && !correlationId.isBlank() ? correlationId.trim() : null;
        operationId = operationId != null && !operationId.isBlank() ? operationId.trim() : null;
        limit = (limit <= 0) ? DEFAULT_LIMIT : Math.min(limit, MAX_LIMIT);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .eventId(eventId)
                .startTime(startTime)
                .endTime(endTime)
                .classification(classification)
                .principalId(principalId)
                .targetId(targetId)
                .action(action)
                .outcome(outcome)
                .correlationId(correlationId)
                .operationId(operationId)
                .limit(limit);
    }

    /**
     * Evaluates whether a canonical audit event matches this query's criteria.
     *
     * @param event audit event to test
     * @return true if the event satisfies all specified criteria, false otherwise
     */
    public boolean matches(HarmoniaAuditEvent event) {
        if (event == null) {
            return false;
        }
        if (eventId != null && !eventId.equals(event.eventId())) {
            return false;
        }
        if (startTime != null && event.recordedAt().isBefore(startTime)) {
            return false;
        }
        if (endTime != null && event.recordedAt().isAfter(endTime)) {
            return false;
        }
        if (classification != null && classification != event.classification()) {
            return false;
        }
        if (principalId != null) {
            boolean matchInitiating = event.initiatingPrincipal() != null && principalId.equals(event.initiatingPrincipal().principalId());
            boolean matchExecuting = event.executingPrincipal() != null && principalId.equals(event.executingPrincipal().principalId());
            boolean matchFallback = principalId.equals(event.principalId());
            if (!matchInitiating && !matchExecuting && !matchFallback) {
                return false;
            }
        }
        if (targetId != null) {
            boolean matchTarget = event.target() != null && targetId.equals(event.target().resourceId());
            boolean matchFallback = targetId.equals(event.resourceId());
            if (!matchTarget && !matchFallback) {
                return false;
            }
        }
        if (action != null && action != event.action()) {
            return false;
        }
        if (outcome != null && outcome != event.outcome()) {
            return false;
        }
        if (correlationId != null && !correlationId.equals(event.correlationId())) {
            return false;
        }
        if (operationId != null && !operationId.equals(event.operationId())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean test(HarmoniaAuditEvent event) {
        return matches(event);
    }

    public static class Builder {
        private String eventId;
        private Instant startTime;
        private Instant endTime;
        private AuditClassification classification;
        private String principalId;
        private String targetId;
        private AuditAction action;
        private AuditOutcome outcome;
        private String correlationId;
        private String operationId;
        private int limit = DEFAULT_LIMIT;

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder startTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(Instant endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder classification(AuditClassification classification) {
            this.classification = classification;
            return this;
        }

        public Builder principalId(String principalId) {
            this.principalId = principalId;
            return this;
        }

        public Builder targetId(String targetId) {
            this.targetId = targetId;
            return this;
        }

        public Builder action(AuditAction action) {
            this.action = action;
            return this;
        }

        public Builder outcome(AuditOutcome outcome) {
            this.outcome = outcome;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder operationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public AuditQuery build() {
            return new AuditQuery(
                    eventId,
                    startTime,
                    endTime,
                    classification,
                    principalId,
                    targetId,
                    action,
                    outcome,
                    correlationId,
                    operationId,
                    limit
            );
        }
    }
}
