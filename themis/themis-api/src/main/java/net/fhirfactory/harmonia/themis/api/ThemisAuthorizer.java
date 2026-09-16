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

import java.util.Set;

/**
 * Core interface for evaluating authorization decisions across Harmonia.
 */
public interface ThemisAuthorizer {

    /**
     * Authorizes a fully constructed authorization request.
     */
    ThemisAuthorizationDecision authorize(ThemisAuthorizationRequest request);

    /**
     * Convenience method to authorize an action for a principal, authorities, target, and security context.
     */
    default ThemisAuthorizationDecision authorize(
            ThemisPrincipal principal,
            Set<ThemisAuthority> authorities,
            ThemisAction action,
            ThemisResource target,
            ThemisSecurityContext context
    ) {
        return authorize(new ThemisAuthorizationRequest(principal, authorities, action, target, context));
    }
}
