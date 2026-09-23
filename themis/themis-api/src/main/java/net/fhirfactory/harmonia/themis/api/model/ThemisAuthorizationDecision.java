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

package net.fhirfactory.harmonia.themis.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable outcome of a Themis authorization evaluation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ThemisAuthorizationDecision(
        String decisionId,
        ThemisDecision decision,
        ThemisDecisionReason reason,
        String policyId,
        Instant evaluatedAt,
        String correlationId,
        String message
) implements Serializable {

    public ThemisAuthorizationDecision {
        decisionId = (decisionId == null || decisionId.isBlank()) ? UUID.randomUUID().toString() : decisionId;
        Objects.requireNonNull(decision, "decision must not be null");
        reason = reason == null ? (decision == ThemisDecision.ALLOW ? ThemisDecisionReason.ALLOWED_BY_POLICY : ThemisDecisionReason.DEFAULT_DENY) : reason;
        evaluatedAt = evaluatedAt == null ? Instant.now() : evaluatedAt;
    }

    @JsonIgnore
    public boolean isAllowed() {
        return decision.isAllowed();
    }

    @JsonIgnore
    public boolean isDenied() {
        return decision.isDenied();
    }

    public static ThemisAuthorizationDecision allow(String policyId, String correlationId) {
        return allow(policyId, correlationId, "Authorized by policy");
    }

    public static ThemisAuthorizationDecision allow(String policyId, String correlationId, String message) {
        return new ThemisAuthorizationDecision(
                UUID.randomUUID().toString(),
                ThemisDecision.ALLOW,
                ThemisDecisionReason.ALLOWED_BY_POLICY,
                policyId,
                Instant.now(),
                correlationId,
                message
        );
    }

    public static ThemisAuthorizationDecision deny(ThemisDecisionReason reason, String policyId, String correlationId, String message) {
        return new ThemisAuthorizationDecision(
                UUID.randomUUID().toString(),
                ThemisDecision.DENY,
                reason,
                policyId,
                Instant.now(),
                correlationId,
                message
        );
    }

    public static ThemisAuthorizationDecision defaultDeny(String correlationId, String message) {
        return deny(ThemisDecisionReason.DEFAULT_DENY, "default-deny", correlationId, message);
    }
}
