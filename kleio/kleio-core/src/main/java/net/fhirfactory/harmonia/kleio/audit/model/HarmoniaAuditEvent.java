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

import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Structured, immutable canonical security audit event representing an audited action, decision,
 * or activity across Harmonia subsystems.
 * <p>
 * Enforces mandatory identity, timestamp, classification, action, and outcome, with defensive copying
 * for collections and attributes. Provides bounded, safe {@link #toString()} representation suppressing
 * sensitive credentials, authority sets, attributes, and PHI markers.
 */
public record HarmoniaAuditEvent(
        String eventId,
        Instant recordedAt,
        AuditClassification classification,
        AuditAction action,
        AuditOutcome outcome,
        AuditPrincipal initiatingPrincipal,
        AuditPrincipal executingPrincipal,
        String securityDomain,
        AuditTarget target,
        AuditAuthorizationEvidence authorizationEvidence,
        Set<ThemisAuthority> authorities,
        Set<ThemisSecurityLabel> securityLabels,
        String correlationId,
        String causationId,
        String operationId,
        AuditSource source,
        Map<String, String> attributes
) implements Serializable {

    private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.]{1,64}$");

    public HarmoniaAuditEvent {
        Objects.requireNonNull(eventId, "eventId must not be null");
        if (!EVENT_ID_PATTERN.matcher(eventId).matches()) {
            throw new IllegalArgumentException("eventId must match pattern " + EVENT_ID_PATTERN.pattern() + " (1-64 alphanumeric, hyphen, or dot characters): " + eventId);
        }
        Objects.requireNonNull(recordedAt, "recordedAt must not be null");
        Objects.requireNonNull(classification, "classification must not be null");
        Objects.requireNonNull(action, "action must not be null");
        Objects.requireNonNull(outcome, "outcome must not be null");

        authorities = authorities == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(authorities));
        securityLabels = securityLabels == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(securityLabels));
        attributes = attributes == null ? Map.of() : Collections.unmodifiableMap(Map.copyOf(attributes));
    }

    /**
     * Canonical alias for {@link #initiatingPrincipal()} providing originating principal semantics.
     *
     * @return the initiating principal
     */
    public AuditPrincipal originatingPrincipal() {
        return initiatingPrincipal;
    }

    /**
     * Helper projection for {@link #initiatingPrincipal()} as {@link ThemisPrincipal}.
     *
     * @return the initiating principal projected as ThemisPrincipal, or null
     */
    public ThemisPrincipal initiatingThemisPrincipal() {
        return initiatingPrincipal != null ? initiatingPrincipal.toThemisPrincipal() : null;
    }

    /**
     * Backward-compatible alias for {@link #recordedAt()}.
     *
     * @return timestamp when the event was recorded
     */
    public Instant timestamp() {
        return recordedAt;
    }

    /**
     * Backward-compatible accessor for authorization decision ID.
     *
     * @return decision ID, or null if no authorization evidence is present
     */
    public String decisionId() {
        return authorizationEvidence != null ? authorizationEvidence.decisionId() : null;
    }

    /**
     * Backward-compatible accessor for authorization decision enum.
     *
     * @return evaluated decision, or null if no authorization evidence is present
     */
    public ThemisDecision decision() {
        return authorizationEvidence != null ? authorizationEvidence.decision() : null;
    }

    /**
     * Backward-compatible accessor for authorization decision reason.
     *
     * @return decision reason, or null if no authorization evidence is present
     */
    public ThemisDecisionReason reason() {
        return authorizationEvidence != null ? authorizationEvidence.reason() : null;
    }

    /**
     * Backward-compatible accessor for evaluated policy ID.
     *
     * @return policy ID, or null if no authorization evidence is present
     */
    public String policyId() {
        return authorizationEvidence != null ? authorizationEvidence.policyId() : null;
    }

    /**
     * Backward-compatible accessor for initiating principal ID.
     *
     * @return principal ID, or null if no initiating principal is present
     */
    public String principalId() {
        return initiatingPrincipal != null ? initiatingPrincipal.principalId() : null;
    }

    /**
     * Backward-compatible accessor for initiating principal type.
     *
     * @return principal type, or null if no initiating principal is present
     */
    public PrincipalType principalType() {
        return initiatingPrincipal != null ? initiatingPrincipal.principalType() : null;
    }

    /**
     * Backward-compatible accessor for initiating principal source domain.
     *
     * @return source domain, or null if no initiating principal is present
     */
    public String sourceDomain() {
        return initiatingPrincipal != null ? initiatingPrincipal.sourceDomain() : null;
    }

    /**
     * Backward-compatible accessor for target resource type.
     *
     * @return resource type, or null if no target is present
     */
    public String resourceType() {
        return target != null ? target.resourceType() : null;
    }

    /**
     * Backward-compatible accessor for target resource ID.
     *
     * @return resource ID, or null if no target is present
     */
    public String resourceId() {
        return target != null ? target.resourceId() : null;
    }

    /**
     * Creates a {@link HarmoniaAuditEvent} from an authorization request and decision pair,
     * preserving trusted context and provenance without fabricating synthetic identities or missing IDs.
     *
     * @param request  the evaluated authorization request
     * @param decision the resulting authorization decision
     * @return structured, immutable security audit event
     */
    public static HarmoniaAuditEvent fromDecision(ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision) {
        return fromDecision(UUID.randomUUID().toString(), request, decision);
    }

    /**
     * Creates a {@link HarmoniaAuditEvent} with an explicit event ID from an authorization request and decision pair,
     * preserving trusted context and provenance without fabricating synthetic identities or missing IDs.
     *
     * @param eventId  the unique event ID
     * @param request  the evaluated authorization request
     * @param decision the resulting authorization decision
     * @return structured, immutable security audit event
     */
    public static HarmoniaAuditEvent fromDecision(String eventId, ThemisAuthorizationRequest request, ThemisAuthorizationDecision decision) {
        Objects.requireNonNull(eventId, "eventId must not be null");
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(decision, "decision must not be null");

        Instant recordedAt = decision.evaluatedAt() != null ? decision.evaluatedAt() : Instant.now();
        AuditClassification classification = AuditClassification.SECURITY;
        AuditAction action = request.action() != null
                ? AuditAction.fromThemisAction(request.action())
                : AuditAction.AUTHORIZE;
        AuditOutcome outcome = decision.isAllowed() ? AuditOutcome.SUCCESS : AuditOutcome.DENIED;

        AuditPrincipal initiatingPrincipal = AuditPrincipal.from(
                request.principal() != null
                        ? request.principal()
                        : (request.context() != null ? request.context().originatingPrincipal() : null)
        );

        AuditPrincipal executingPrincipal = AuditPrincipal.from(
                request.context() != null
                        ? request.context().executingPrincipal()
                        : null
        );

        String securityDomain = null;
        if (request.target() != null && request.target().securityDomain() != null) {
            securityDomain = request.target().securityDomain();
        } else if (request.context() != null && request.context().securityDomain() != null) {
            securityDomain = request.context().securityDomain();
        } else if (request.principal() != null && request.principal().sourceDomain() != null) {
            securityDomain = request.principal().sourceDomain();
        }

        AuditTarget target = request.target() != null
                ? AuditTarget.fromResource(request.target())
                : null;

        AuditAuthorizationEvidence authorizationEvidence = AuditAuthorizationEvidence.fromDecision(decision);

        Set<ThemisAuthority> authSet = new LinkedHashSet<>();
        if (request.authorities() != null) {
            authSet.addAll(request.authorities());
        }
        if (request.context() != null && request.context().authorities() != null) {
            authSet.addAll(request.context().authorities());
        }

        Set<ThemisSecurityLabel> securityLabels = request.target() != null && request.target().securityLabels() != null
                ? request.target().securityLabels()
                : Set.of();

        String correlationId = (request.context() != null && request.context().correlationId() != null)
                ? request.context().correlationId()
                : decision.correlationId();

        String causationId = request.context() != null ? request.context().causationId() : null;
        String operationId = null;

        AuditSource source = AuditSource.of("THEMIS", "AUTHORIZATION_ENGINE");

        return new HarmoniaAuditEvent(
                eventId,
                recordedAt,
                classification,
                action,
                outcome,
                initiatingPrincipal,
                executingPrincipal,
                securityDomain,
                target,
                authorizationEvidence,
                Collections.unmodifiableSet(authSet),
                securityLabels,
                correlationId,
                causationId,
                operationId,
                source,
                Map.of()
        );
    }

    @Override
    public String toString() {
        return "HarmoniaAuditEvent[" +
                "eventId=" + eventId +
                ", recordedAt=" + recordedAt +
                ", classification=" + classification +
                ", action=" + action +
                ", outcome=" + outcome +
                ", initiatingPrincipal=" + initiatingPrincipal +
                ", executingPrincipal=" + executingPrincipal +
                ", securityDomain=" + securityDomain +
                ", target=" + target +
                ", authorizationEvidence=" + authorizationEvidence +
                ", correlationId=" + correlationId +
                ", causationId=" + causationId +
                ", operationId=" + operationId +
                ", source=" + source +
                ", authoritiesCount=" + (authorities != null ? authorities.size() : 0) +
                ", securityLabelsCount=" + (securityLabels != null ? securityLabels.size() : 0) +
                ", attributeCount=" + (attributes != null ? attributes.size() : 0) +
                "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .eventId(eventId)
                .recordedAt(recordedAt)
                .classification(classification)
                .action(action)
                .outcome(outcome)
                .initiatingPrincipal(initiatingPrincipal)
                .executingPrincipal(executingPrincipal)
                .securityDomain(securityDomain)
                .target(target)
                .authorizationEvidence(authorizationEvidence)
                .authorities(authorities)
                .securityLabels(securityLabels)
                .correlationId(correlationId)
                .causationId(causationId)
                .operationId(operationId)
                .source(source)
                .attributes(attributes);
    }

    public static class Builder {
        private String eventId;
        private Instant recordedAt;
        private AuditClassification classification;
        private AuditAction action;
        private AuditOutcome outcome;
        private AuditPrincipal initiatingPrincipal;
        private AuditPrincipal executingPrincipal;
        private String securityDomain;
        private AuditTarget target;
        private AuditAuthorizationEvidence authorizationEvidence;
        private Set<ThemisAuthority> authorities = Set.of();
        private Set<ThemisSecurityLabel> securityLabels = Set.of();
        private String correlationId;
        private String causationId;
        private String operationId;
        private AuditSource source;
        private Map<String, String> attributes = Map.of();

        // Compatibility fields for granular legacy builder calls
        private String decisionId;
        private ThemisDecision decision;
        private ThemisDecisionReason reason;
        private String policyId;
        private String message;
        private String principalId;
        private PrincipalType principalType;
        private String sourceDomain;
        private String resourceType;
        private String resourceId;

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder recordedAt(Instant recordedAt) {
            this.recordedAt = recordedAt;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.recordedAt = timestamp;
            return this;
        }

        public Builder classification(AuditClassification classification) {
            this.classification = classification;
            return this;
        }

        public Builder classification(String classification) {
            if (classification == null) {
                this.classification = null;
                return this;
            }
            this.classification = AuditClassification.valueOf(classification.trim().toUpperCase());
            return this;
        }

        public Builder action(AuditAction action) {
            this.action = action;
            return this;
        }

        public Builder action(ThemisAction action) {
            this.action = AuditAction.fromThemisAction(action);
            return this;
        }

        public Builder outcome(AuditOutcome outcome) {
            this.outcome = outcome;
            return this;
        }

        public Builder initiatingPrincipal(AuditPrincipal initiatingPrincipal) {
            this.initiatingPrincipal = initiatingPrincipal;
            return this;
        }

        public Builder initiatingPrincipal(ThemisPrincipal initiatingPrincipal) {
            this.initiatingPrincipal = AuditPrincipal.from(initiatingPrincipal);
            return this;
        }

        public Builder principal(AuditPrincipal principal) {
            this.initiatingPrincipal = principal;
            return this;
        }

        public Builder principal(ThemisPrincipal principal) {
            this.initiatingPrincipal = AuditPrincipal.from(principal);
            return this;
        }

        public Builder originatingPrincipal(AuditPrincipal principal) {
            this.initiatingPrincipal = principal;
            return this;
        }

        public Builder originatingPrincipal(ThemisPrincipal principal) {
            this.initiatingPrincipal = AuditPrincipal.from(principal);
            return this;
        }

        public Builder executingPrincipal(AuditPrincipal executingPrincipal) {
            this.executingPrincipal = executingPrincipal;
            return this;
        }

        public Builder executingPrincipal(ThemisPrincipal executingPrincipal) {
            this.executingPrincipal = AuditPrincipal.from(executingPrincipal);
            return this;
        }

        public Builder securityDomain(String securityDomain) {
            this.securityDomain = securityDomain;
            return this;
        }

        public Builder target(AuditTarget target) {
            this.target = target;
            return this;
        }

        public Builder authorizationEvidence(AuditAuthorizationEvidence authorizationEvidence) {
            this.authorizationEvidence = authorizationEvidence;
            return this;
        }

        public Builder authorities(Set<ThemisAuthority> authorities) {
            this.authorities = authorities != null ? Set.copyOf(authorities) : Set.of();
            return this;
        }

        public Builder authorities(Collection<ThemisAuthority> authorities) {
            this.authorities = authorities != null ? Set.copyOf(authorities) : Set.of();
            return this;
        }

        public Builder addAuthority(ThemisAuthority authority) {
            if (authority != null) {
                Set<ThemisAuthority> updated = new HashSet<>(this.authorities);
                updated.add(authority);
                this.authorities = Collections.unmodifiableSet(updated);
            }
            return this;
        }

        public Builder addAuthority(String authorityCode) {
            if (authorityCode != null) {
                return addAuthority(ThemisAuthority.of(authorityCode));
            }
            return this;
        }

        public Builder securityLabels(Set<ThemisSecurityLabel> securityLabels) {
            this.securityLabels = securityLabels != null ? Set.copyOf(securityLabels) : Set.of();
            return this;
        }

        public Builder securityLabels(Collection<ThemisSecurityLabel> securityLabels) {
            this.securityLabels = securityLabels != null ? Set.copyOf(securityLabels) : Set.of();
            return this;
        }

        public Builder addSecurityLabel(ThemisSecurityLabel label) {
            if (label != null) {
                Set<ThemisSecurityLabel> updated = new HashSet<>(this.securityLabels);
                updated.add(label);
                this.securityLabels = Collections.unmodifiableSet(updated);
            }
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

        public Builder operationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder source(AuditSource source) {
            this.source = source;
            return this;
        }

        public Builder source(String subsystem, String component) {
            this.source = AuditSource.of(subsystem, component);
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes = attributes != null ? Map.copyOf(attributes) : Map.of();
            return this;
        }

        public Builder addAttribute(String key, String value) {
            if (key != null && value != null) {
                Map<String, String> updated = new LinkedHashMap<>(this.attributes);
                updated.put(key, value);
                this.attributes = Collections.unmodifiableMap(updated);
            }
            return this;
        }

        // Backward compatibility setters
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

        public Builder message(String message) {
            this.message = message;
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

        public Builder resourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public Builder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public HarmoniaAuditEvent build() {
            String resolvedEventId = eventId != null ? eventId : UUID.randomUUID().toString();
            Instant resolvedRecordedAt = recordedAt != null ? recordedAt : Instant.now();
            AuditClassification resolvedClassification = classification != null
                    ? classification
                    : AuditClassification.SECURITY;
            AuditAction resolvedAction = action != null ? action : AuditAction.AUTHORIZE;

            AuditOutcome resolvedOutcome = outcome;
            if (resolvedOutcome == null && decision != null) {
                resolvedOutcome = AuditOutcome.fromDecision(decision);
            }
            if (resolvedOutcome == null) {
                resolvedOutcome = AuditOutcome.SUCCESS;
            }

            AuditPrincipal resolvedInitiating = initiatingPrincipal;
            if (resolvedInitiating == null && principalId != null) {
                resolvedInitiating = AuditPrincipal.of(principalId, principalType, sourceDomain);
            }

            AuditTarget resolvedTarget = target;
            if (resolvedTarget == null && (resourceType != null || resourceId != null || securityDomain != null)) {
                resolvedTarget = AuditTarget.of(resourceType, resourceId, securityDomain);
            }

            AuditAuthorizationEvidence resolvedEvidence = authorizationEvidence;
            if (resolvedEvidence == null && (decisionId != null || decision != null || reason != null || policyId != null || message != null)) {
                resolvedEvidence = AuditAuthorizationEvidence.of(decisionId, decision, reason, policyId, message);
            }

            return new HarmoniaAuditEvent(
                    resolvedEventId,
                    resolvedRecordedAt,
                    resolvedClassification,
                    resolvedAction,
                    resolvedOutcome,
                    resolvedInitiating,
                    executingPrincipal,
                    securityDomain,
                    resolvedTarget,
                    resolvedEvidence,
                    authorities,
                    securityLabels,
                    correlationId,
                    causationId,
                    operationId,
                    source,
                    attributes
            );
        }
    }
}
