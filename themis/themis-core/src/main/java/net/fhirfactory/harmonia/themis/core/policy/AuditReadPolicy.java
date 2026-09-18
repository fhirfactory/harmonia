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

/**
 * Policy governing access to AUDIT records and logs.
 */
public class AuditReadPolicy implements ThemisPolicy {

    public static final String POLICY_ID = "audit-read-policy";
    public static final String DOMAIN = "AUDIT";
    public static final String AUTH_AUDIT_READ = "audit.read";
    public static final String AUTH_SYSTEM_ADMIN = "system.admin";

    @Override
    public String getPolicyId() {
        return POLICY_ID;
    }

    @Override
    public String getDescription() {
        return "Authorizes READ and SEARCH access to security audit records.";
    }

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request) {
        if (request == null || request.target() == null || request.action() == null) {
            return false;
        }
        boolean matchesDomain = DOMAIN.equalsIgnoreCase(request.target().securityDomain())
                || request.target().hasSecurityLabel(DOMAIN);
        boolean matchesAction = request.action() == ThemisAction.READ || request.action() == ThemisAction.SEARCH;
        return matchesDomain && matchesAction;
    }

    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request) {
        String corrId = request.context() != null ? request.context().correlationId() : null;
        if (request.hasAuthority(AUTH_AUDIT_READ) || request.hasAuthority(AUTH_SYSTEM_ADMIN)) {
            return ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by audit.read");
        }
        return ThemisAuthorizationDecision.deny(ThemisDecisionReason.AUTHORITY_MISSING, POLICY_ID, corrId, "Missing required authority: " + AUTH_AUDIT_READ);
    }
}
