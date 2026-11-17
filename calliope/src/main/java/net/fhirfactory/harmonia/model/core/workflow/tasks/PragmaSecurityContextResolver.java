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

package net.fhirfactory.harmonia.model.core.workflow.tasks;

import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

/**
 * Resolves the canonical {@link ThemisSecurityContext} from a {@link Pragma}'s security and correlation fields.
 */
public final class PragmaSecurityContextResolver {

    private PragmaSecurityContextResolver() {
        // Utility class
    }

    public static ThemisSecurityContext resolveSecurityContext(Pragma pragma) {
        if (pragma == null) {
            return null;
        }

        ThemisSecurityContext originating = pragma.getOriginatingSecurityContext();
        if (originating != null) {
            ThemisSecurityContext.Builder b = originating.toBuilder();
            if (pragma.getOriginatingPrincipal() != null && originating.requestingPrincipal() == null) {
                b.originatingPrincipal(pragma.getOriginatingPrincipal());
            }
            if (pragma.getExecutingPrincipal() != null && originating.executingPrincipal() == null) {
                b.executingPrincipal(pragma.getExecutingPrincipal());
            }
            if (!pragma.getOriginatingAuthorities().isEmpty() && originating.authorities().isEmpty()) {
                b.authorities(pragma.getOriginatingAuthorities());
            }
            if (pragma.getCorrelationId() != null && originating.correlationId() == null) {
                b.correlationId(pragma.getCorrelationId());
            }
            if (pragma.getCausationId() != null && originating.causationId() == null) {
                b.causationId(pragma.getCausationId());
            }
            return b.build();
        }

        if (pragma.getOriginatingPrincipal() != null || pragma.getExecutingPrincipal() != null || !pragma.getOriginatingAuthorities().isEmpty()) {
            return ThemisSecurityContext.builder()
                    .originatingPrincipal(pragma.getOriginatingPrincipal())
                    .executingPrincipal(pragma.getExecutingPrincipal())
                    .authorities(pragma.getOriginatingAuthorities())
                    .correlationId(pragma.getCorrelationId())
                    .causationId(pragma.getCausationId())
                    .build();
        }

        return null;
    }
}
