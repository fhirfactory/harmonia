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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.hestia.mnemosyne.governance;

import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.Objects;
import java.util.Set;

/**
 * Mnemosyne policy-enforcement point (PEP). It delegates every decision to the injected Themis
 * authorizer and fails closed on missing context, denial, null decisions, or evaluator failure.
 */
public final class MnemosyneAuthorizationGate {

    private final ThemisAuthorizer authorizer;

    /** Creates the PEP around the existing Themis policy decision point. */
    public MnemosyneAuthorizationGate(ThemisAuthorizer authorizer) {
        this.authorizer = Objects.requireNonNull(authorizer, "authorizer must not be null");
    }

    /**
     * Requires a positive Themis decision before the caller can reach persistence.
     *
     * @param action governed operation
     * @param key target resource identity
     * @param context established immutable Themis context
     * @throws SecurityException when the context or Themis decision is absent, denied, or fails
     */
    public void requireAuthorized(ThemisAction action, ResourceKey key, ThemisSecurityContext context) {
        Objects.requireNonNull(action, "action must not be null");
        Objects.requireNonNull(key, "key must not be null");
        if (context == null) {
            throw new SecurityException("Themis security context is required for " + action + " " + key);
        }

        ThemisPrincipal principal = context.requestingPrincipal() != null
                ? context.requestingPrincipal()
                : ThemisPrincipal.system("anonymous");
        ThemisResource target = ThemisResource.of(key.resourceType(), key.id());
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.of(
                principal,
                context.authorities() == null ? Set.of() : context.authorities(),
                action,
                target,
                context);

        ThemisAuthorizationDecision decision;
        try {
            decision = authorizer.authorize(request);
        } catch (RuntimeException exception) {
            throw new SecurityException("Themis authorization evaluation failed for " + action + " " + key, exception);
        }
        if (decision == null || !decision.isAllowed()) {
            throw new SecurityException("Themis denied " + action + " for " + key);
        }
    }
}