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

package net.fhirfactory.harmonia.paradeigma.common.security;

import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaRoleEnum;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisRole;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.Collections;
import java.util.Set;

/**
 * Scenario context wrapper containing Themis security attributes, assigned roles, and granted authorities
 * for scenario execution and security assertions.
 */
public record SecurityScenarioContext(
        String actorName,
        ThemisSecurityContext securityContext,
        Set<HarmoniaRoleEnum> assignedRoles,
        Set<ThemisAuthority> grantedAuthorities
) {

    public SecurityScenarioContext {
        assignedRoles = assignedRoles != null ? Set.copyOf(assignedRoles) : Collections.emptySet();
        grantedAuthorities = grantedAuthorities != null ? Set.copyOf(grantedAuthorities) : Collections.emptySet();
    }

    public ThemisPrincipal principal() {
        return securityContext != null ? securityContext.requestingPrincipal() : null;
    }

    public String correlationId() {
        return securityContext != null ? securityContext.correlationId() : null;
    }

    public boolean hasAuthority(HarmoniaAuthorityEnum authority) {
        if (authority == null || grantedAuthorities.isEmpty()) return false;
        return grantedAuthorities.stream()
                .anyMatch(a -> a.authorityCode().equalsIgnoreCase(authority.getCode()));
    }

    public boolean hasRole(HarmoniaRoleEnum role) {
        if (role == null) return false;
        return assignedRoles.contains(role);
    }

    public Set<ThemisRole> toThemisRoles() {
        return ParadeigmaSecurityActors.getThemisRoles(assignedRoles);
    }

    public static SecurityScenarioContext of(
            String actorName,
            ThemisSecurityContext securityContext,
            Set<HarmoniaRoleEnum> roles) {
        Set<ThemisAuthority> auths = ParadeigmaSecurityActors.getThemisAuthoritiesForRoles(roles);
        return new SecurityScenarioContext(actorName, securityContext, roles, auths);
    }

    public static SecurityScenarioContext providerSteward() {
        return of("Provider Steward", ParadeigmaSecurityActors.providerStewardContext(), ParadeigmaSecurityActors.PROVIDER_STEWARD_ROLES);
    }

    public static SecurityScenarioContext clinician() {
        return of("Clinician", ParadeigmaSecurityActors.clinicianContext(), ParadeigmaSecurityActors.CLINICIAN_ROLES);
    }

    public static SecurityScenarioContext systemAdmin() {
        return of("System Administrator", ParadeigmaSecurityActors.systemAdminContext(), ParadeigmaSecurityActors.SYSTEM_ADMIN_ROLES);
    }

    public static SecurityScenarioContext integrationService() {
        return of("Integration Service", ParadeigmaSecurityActors.integrationServiceContext(), ParadeigmaSecurityActors.INTEGRATION_SERVICE_ROLES);
    }

    public static SecurityScenarioContext readOnly() {
        return of("Read Only User", ParadeigmaSecurityActors.readOnlyContext(), ParadeigmaSecurityActors.READ_ONLY_ROLES);
    }

    public static SecurityScenarioContext unauthorized() {
        return new SecurityScenarioContext("Unauthorized User", ParadeigmaSecurityActors.unauthorizedContext(), Set.of(), Set.of());
    }

    public static SecurityScenarioContext unauthenticated() {
        return new SecurityScenarioContext("Unauthenticated User", ParadeigmaSecurityActors.unauthenticatedContext(), Set.of(), Set.of());
    }
}
