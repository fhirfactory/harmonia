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

package net.fhirfactory.harmonia.befe.security;

import jakarta.enterprise.context.ApplicationScoped;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;

import java.util.Locale;
import java.util.Set;

/**
 * Default ThemisAuthorizer implementation for Iris BEFE.
 * Strictly adheres to Invariant 6 (Default-Deny Security Governance).
 * Evaluates operational RBAC roles and permissions, rejecting any unauthenticated or unauthorized requests.
 */
@ApplicationScoped
public class DefaultThemisAuthorizer implements ThemisAuthorizer {

    public static final String POLICY_OPERATIONS_RBAC = "themis.policy.operations.rbac";

    public static final String ROLE_SYS_ADM = "SYS_ADM";
    public static final String ROLE_SYS_INT = "SYS_INT";
    public static final String ROLE_OPS_ADM = "OPS_ADM";
    public static final String ROLE_OPS_VIEWER = "OPS_VIEWER";

    public static final String AUTH_SYSTEM_ADMIN = "system.admin";
    public static final String AUTH_SYSTEM_INTEGRATION = "system.integration";
    public static final String AUTH_OPERATIONS_READ = "operations.read";
    public static final String AUTH_OPERATIONS_ADMIN = "operations.admin";

    private static final Set<String> ALLOWED_ROLES = Set.of(
            ROLE_SYS_ADM,
            ROLE_SYS_INT,
            ROLE_OPS_ADM,
            ROLE_OPS_VIEWER
    );

    private static final Set<String> ALLOWED_AUTHORITIES = Set.of(
            AUTH_SYSTEM_ADMIN.toLowerCase(Locale.ROOT),
            AUTH_SYSTEM_INTEGRATION.toLowerCase(Locale.ROOT),
            AUTH_OPERATIONS_READ.toLowerCase(Locale.ROOT),
            AUTH_OPERATIONS_ADMIN.toLowerCase(Locale.ROOT),
            "role_" + ROLE_SYS_ADM.toLowerCase(Locale.ROOT),
            "role_" + ROLE_SYS_INT.toLowerCase(Locale.ROOT),
            "role_" + ROLE_OPS_ADM.toLowerCase(Locale.ROOT),
            "role_" + ROLE_OPS_VIEWER.toLowerCase(Locale.ROOT)
    );

    @Override
    public ThemisAuthorizationDecision authorize(ThemisAuthorizationRequest request) {
        if (request == null) {
            return ThemisAuthorizationDecision.defaultDeny(null, "ThemisAuthorizationRequest is null");
        }

        String correlationId = request.context() != null ? request.context().correlationId() : null;

        // 1. Verify Principal
        ThemisPrincipal principal = request.principal();
        if (principal == null || principal.principalId() == null || principal.principalId().isBlank()
                || "anonymous".equalsIgnoreCase(principal.principalId())
                || "system:anonymous".equalsIgnoreCase(principal.principalId())) {
            return ThemisAuthorizationDecision.deny(
                    ThemisDecisionReason.PRINCIPAL_MISSING,
                    POLICY_OPERATIONS_RBAC,
                    correlationId,
                    "Authentication required: principal is missing or anonymous"
            );
        }

        // 2. Verify Authorities / Roles
        Set<ThemisAuthority> authorities = request.authorities();
        boolean authorized = false;

        if (authorities != null) {
            for (ThemisAuthority auth : authorities) {
                if (auth != null && auth.authorityCode() != null) {
                    String authCode = auth.authorityCode().toLowerCase(Locale.ROOT);
                    if (ALLOWED_AUTHORITIES.contains(authCode)) {
                        authorized = true;
                        break;
                    }
                }
            }
        }

        // Check principal attributes for roles if authorities were empty
        if (!authorized && principal.attributes() != null) {
            String roleAttr = principal.attributes().get("role");
            if (roleAttr != null && ALLOWED_ROLES.contains(roleAttr.toUpperCase(Locale.ROOT))) {
                authorized = true;
            }
        }

        if (authorized) {
            return ThemisAuthorizationDecision.allow(
                    POLICY_OPERATIONS_RBAC,
                    correlationId,
                    "Authorized by Operations RBAC policy for principal: " + principal.principalId()
            );
        }

        // Default Deny
        return ThemisAuthorizationDecision.deny(
                ThemisDecisionReason.ACTION_NOT_PERMITTED,
                POLICY_OPERATIONS_RBAC,
                correlationId,
                "Principal " + principal.principalId() + " lacks required operations authorization"
        );
    }
}
