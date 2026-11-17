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

package net.fhirfactory.harmonia.model.core.security;

import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.model.core.security.valuesets.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.core.security.valuesets.HarmoniaSecurityLabelEnum;
import net.fhirfactory.harmonia.model.core.security.factory.ErgonSecurityDefinitionBuilder;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Declares the execution security envelope for an Ergon activity or Ponos processing route.
 */
public record ErgonSecurityDefinition(
        String ergonId,
        Set<ThemisAuthority> requiredExecutionAuthorities,
        Set<ThemisAction> permittedActions,
        Set<String> permittedResourceTypes,
        Set<String> permittedSecurityDomains
) implements Serializable {

    public ErgonSecurityDefinition {
        Objects.requireNonNull(ergonId, "ergonId must not be null");
        requiredExecutionAuthorities = requiredExecutionAuthorities == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(requiredExecutionAuthorities));
        permittedActions = permittedActions == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(permittedActions));
        permittedResourceTypes = permittedResourceTypes == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(permittedResourceTypes));
        permittedSecurityDomains = permittedSecurityDomains == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(permittedSecurityDomains));
    }

    public static ErgonSecurityDefinitionBuilder builder() {
        return new ErgonSecurityDefinitionBuilder();
    }

    public static ErgonSecurityDefinition forProviderRegistryChange(String ergonId, String resourceType) {
        return builder()
                .ergonId(ergonId)
                .requiredExecutionAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_PROCESS.toThemisAuthority())
                .requiredExecutionAuthority(HarmoniaAuthorityEnum.PROVIDER_RESOURCE_CREATE.toThemisAuthority())
                .requiredExecutionAuthority(HarmoniaAuthorityEnum.PROVIDER_RESOURCE_UPDATE.toThemisAuthority())
                .permittedAction(ThemisAction.PROCESS)
                .permittedAction(ThemisAction.EXECUTE)
                .permittedAction(ThemisAction.UPDATE)
                .permittedAction(ThemisAction.CREATE)
                .permittedResourceType(resourceType)
                .permittedSecurityDomain(HarmoniaSecurityLabelEnum.PROVIDER_REGISTRY.getCode())
                .build();
    }

    public static ErgonSecurityDefinition forDemographicsUpdate(String ergonId) {
        return builder()
                .ergonId(ergonId)
                .requiredExecutionAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_PROCESS.toThemisAuthority())
                .permittedAction(ThemisAction.PROCESS)
                .permittedAction(ThemisAction.EXECUTE)
                .permittedResourceType("Patient")
                .permittedSecurityDomain(HarmoniaSecurityLabelEnum.CLINICAL.getCode())
                .build();
    }

    /**
     * Checks an action only when it is explicitly present in a non-empty allow-list.
     *
     * @param action action being evaluated
     * @return true only when the non-null action is explicitly permitted
     */
    public boolean permitsAction(ThemisAction action) {
        return action != null && !permittedActions.isEmpty() && permittedActions.contains(action);
    }

    /**
     * Checks a resource type only when it is explicitly present in a non-empty allow-list.
     *
     * @param resourceType resource type being evaluated
     * @return true only when the non-blank resource type is explicitly permitted
     */
    public boolean permitsResourceType(String resourceType) {
        if (resourceType == null || resourceType.isBlank() || permittedResourceTypes.isEmpty()) {
            return false;
        }
        return permittedResourceTypes.stream().anyMatch(rt -> rt.equalsIgnoreCase(resourceType.trim()));
    }

    /**
     * Checks a security domain only when it is explicitly present in a non-empty allow-list.
     *
     * @param domain security domain being evaluated
     * @return true only when the non-blank domain is explicitly permitted
     */
    public boolean permitsSecurityDomain(String domain) {
        if (domain == null || domain.isBlank() || permittedSecurityDomains.isEmpty()) {
            return false;
        }
        return permittedSecurityDomains.stream().anyMatch(d -> d.equalsIgnoreCase(domain.trim()));
    }

}
