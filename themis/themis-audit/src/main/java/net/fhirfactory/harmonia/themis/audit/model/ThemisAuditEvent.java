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

package net.fhirfactory.harmonia.themis.audit.model;

import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Structured, immutable security audit event representing an evaluated authorization decision.
 * Contains non-sensitive metadata for traceability without exposing PHI or authentication secrets.
 */
public record ThemisAuditEvent(
        String eventId,
        String decisionId,
        ThemisDecision decision,
        ThemisDecisionReason reason,
        String policyId,
        String principalId,
        PrincipalType principalType,
        String sourceDomain,
        ThemisAction action,
        String resourceType,
        String resourceId,
        String securityDomain,
        Set<ThemisSecurityLabel> securityLabels,
        String correlationId,
        String causationId,
        Instant timestamp,
        String classification
) implements Serializable {

    public static final String CLASSIFICATION_AUDIT = "AUDIT";

    public ThemisAuditEvent {
        eventId = eventId != null ? eventId : UUID.randomUUID().toString();
        timestamp = timestamp != null ? timestamp : Instant.now();
        classification = classification != null ? classification : CLASSIFICATION_AUDIT;
        securityLabels = securityLabels == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(securityLabels));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String eventId;
        private String decisionId;
        private ThemisDecision decision;
        private ThemisDecisionReason reason;
        private String policyId;
        private String principalId;
        private PrincipalType principalType;
        private String sourceDomain;
        private ThemisAction action;
        private String resourceType;
        private String resourceId;
        private String securityDomain;
        private Set<ThemisSecurityLabel> securityLabels = Set.of();
        private String correlationId;
        private String causationId;
        private Instant timestamp;
        private String classification = CLASSIFICATION_AUDIT;

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder decisionId(String decisionId) {
            this.decisionId = decisionId;
            return this;
        }

        public Builder decision(ThemisDecision decision) {
            this.decision = decision;
            return this;
        }

        public Builder reason(ThemisDecisionReason reason) {
            this.reason = reason;
            return this;
        }

        public Builder policyId(String policyId) {
            this.policyId = policyId;
            return this;
        }

        public Builder principalId(String principalId) {
            this.principalId = principalId;
            return this;
        }

        public Builder principalType(PrincipalType principalType) {
            this.principalType = principalType;
            return this;
        }

        public Builder sourceDomain(String sourceDomain) {
            this.sourceDomain = sourceDomain;
            return this;
        }

        public Builder action(ThemisAction action) {
            this.action = action;
            return this;
        }

        public Builder resourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public Builder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder securityDomain(String securityDomain) {
            this.securityDomain = securityDomain;
            return this;
        }

        public Builder securityLabels(Set<ThemisSecurityLabel> securityLabels) {
            this.securityLabels = securityLabels != null ? securityLabels : Set.of();
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder causationId(String causationId) {
            this.causationId = causationId;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder classification(String classification) {
            this.classification = classification;
            return this;
        }

        public ThemisAuditEvent build() {
            return new ThemisAuditEvent(
                    eventId, decisionId, decision, reason, policyId,
                    principalId, principalType, sourceDomain, action,
                    resourceType, resourceId, securityDomain, securityLabels,
                    correlationId, causationId, timestamp, classification
            );
        }
    }
}
