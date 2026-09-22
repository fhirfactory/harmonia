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
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;

/**
 * Default ThemisAuthorizer implementation for Iris BEFE.
 * Strictly adheres to Invariant 6 (Default-Deny Security Governance).
 * Delegates domain-neutral authorization decisions to the deterministic Themis policy evaluator.
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

    private final ThemisAuthorizer evaluator;

    public DefaultThemisAuthorizer() {
        this(DeterministicPolicyEvaluator.withDefaultPolicies());
    }

    public DefaultThemisAuthorizer(ThemisAuthorizer evaluator) {
        this.evaluator = evaluator != null ? evaluator : DeterministicPolicyEvaluator.withDefaultPolicies();
    }

    @Override
    public ThemisAuthorizationDecision authorize(ThemisAuthorizationRequest request) {
        if (request == null) {
            return ThemisAuthorizationDecision.defaultDeny(null, "ThemisAuthorizationRequest is null");
        }
        return evaluator.authorize(request);
    }
}
