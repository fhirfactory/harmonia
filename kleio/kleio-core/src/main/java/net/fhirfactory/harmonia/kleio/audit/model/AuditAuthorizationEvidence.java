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

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;

import java.io.Serializable;

/**
 * Immutable authorization decision evidence captured in an audit event.
 * <p>
 * Diagnostic message content is preserved for auditing but suppressed in
 * {@link #toString()} to prevent accidental leakage of sensitive tokens or PHI.
 */
public record AuditAuthorizationEvidence(
        String decisionId,
        ThemisDecision decision,
        ThemisDecisionReason reason,
        String policyId,
        String message
) implements Serializable {

    public static AuditAuthorizationEvidence of(
            String decisionId,
            ThemisDecision decision,
            ThemisDecisionReason reason,
            String policyId,
            String message
    ) {
        return new AuditAuthorizationEvidence(decisionId, decision, reason, policyId, message);
    }

    public static AuditAuthorizationEvidence fromDecision(ThemisAuthorizationDecision decision) {
        if (decision == null) {
            return null;
        }
        return new AuditAuthorizationEvidence(
                decision.decisionId(),
                decision.decision(),
                decision.reason(),
                decision.policyId(),
                decision.message()
        );
    }

    @Override
    public String toString() {
        return "AuditAuthorizationEvidence[" +
                "decisionId=" + decisionId +
                ", decision=" + decision +
                ", reason=" + reason +
                ", policyId=" + policyId +
                ", hasMessage=" + (message != null && !message.isBlank()) +
                "]";
    }
}
