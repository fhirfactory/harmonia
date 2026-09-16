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

package net.fhirfactory.harmonia.themis.api;

import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;

import java.util.List;
import java.util.Set;

/**
 * Main service contract for Themis policy governance and evaluation.
 */
public interface ThemisService extends ThemisAuthorizer {

    /**
     * Registers a security policy dynamically.
     */
    void registerPolicy(ThemisPolicy policy);

    /**
     * Unregisters a security policy by its policy ID.
     */
    void unregisterPolicy(String policyId);

    /**
     * Retrieves all currently registered policies.
     */
    List<ThemisPolicy> getRegisteredPolicies();

    /**
     * Evaluates dual-authority asynchronous task execution.
     * Both the originating requester authority AND the Ergon execution authority must be satisfied.
     */
    ThemisAuthorizationDecision authorizeAsyncExecution(
            ThemisPrincipal originatingPrincipal,
            Set<ThemisAuthority> originatingAuthorities,
            ThemisPrincipal executionPrincipal,
            Set<ThemisAuthority> executionAuthorities,
            ThemisAction action,
            ThemisResource target,
            ThemisSecurityContext context
    );
}
