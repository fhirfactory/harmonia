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

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;

/**
 * High-priority policy authorizing system administrators carrying system.admin authorities.
 */
public class SystemAdminPolicy implements ThemisPolicy {

    public static final String POLICY_ID = "system-admin-policy";
    public static final String AUTH_SYSTEM_ADMIN = "system.admin";
    public static final String AUTH_SYSTEM_WILDCARD = "system/*.*";
    public static final String AUTH_GLOBAL_WILDCARD = "*";

    @Override
    public String getPolicyId() {
        return POLICY_ID;
    }

    @Override
    public String getDescription() {
        return "Authorizes system administrator interactions across Harmonia subsystems.";
    }

    @Override
    public int getOrder() {
        return 10; // High priority evaluation
    }

    @Override
    public boolean appliesTo(ThemisAuthorizationRequest request) {
        if (request == null) {
            return false;
        }
        return request.hasAuthority(AUTH_SYSTEM_ADMIN)
                || request.hasAuthority(AUTH_SYSTEM_WILDCARD)
                || request.hasAuthority(AUTH_GLOBAL_WILDCARD);
    }

    @Override
    public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request) {
        String corrId = request.context() != null ? request.context().correlationId() : null;
        return ThemisAuthorizationDecision.allow(POLICY_ID, corrId, "Authorized by system administrative authority");
    }
}
