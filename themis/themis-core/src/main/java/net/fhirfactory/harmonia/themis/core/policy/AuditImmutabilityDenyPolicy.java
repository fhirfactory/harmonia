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

package net.fhirfactory.harmonia.themis.core.policy;

import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;

/**
 * Explicit-deny policy governing the AUDIT security domain.
 * Enforces strict immutability by rejecting any external mutating actions
 * (CREATE, UPDATE, DELETE) on audit resources, taking precedence over all allow rules.
 */
public class AuditImmutabilityDenyPolicy implements ThemisPolicy {

    public static final String POLICY_ID = "audit-immutability-deny-policy";
    public static final String DOMAIN = HarmoniaSecurityConstants.LABEL_AUDIT;

    @Override
    public String getPolicyId() {
        return POLICY_ID;
    }

    @Override
    public String getDescription() {
        return "Explicitly denies external mutation actions (CREATE, UPDATE, DELETE) on AUDIT resources to guarantee immutability.";
    }

    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public boolean isExplicitDeny() {
        return true;
    }

    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request) {
        if (request == null || request.target() == null || request.action() == null) {
            return false;
        }
        boolean matchesDomain = DOMAIN.equalsIgnoreCase(request.target().securityDomain())
                || request.target().hasSecurityLabel(DOMAIN);
        boolean matchesAction = request.action() == ThemisAction.CREATE
                || request.action() == ThemisAction.UPDATE
                || request.action() == ThemisAction.DELETE;
        return matchesDomain && matchesAction;
    }

    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request) {
        String corrId = (request != null && request.context() != null) ? request.context().correlationId() : null;
        return ThemisAuthorizationDecision.deny(
                ThemisDecisionReason.ACTION_NOT_PERMITTED,
                POLICY_ID,
                corrId,
                "External mutation of AUDIT records is strictly prohibited"
        );
    }
}
