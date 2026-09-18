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

package net.fhirfactory.harmonia.model.security;

import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
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

    public static Builder builder() {
        return new Builder();
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

    public boolean permitsAction(ThemisAction action) {
        return permittedActions.isEmpty() || permittedActions.contains(action);
    }

    public boolean permitsResourceType(String resourceType) {
        if (resourceType == null || permittedResourceTypes.isEmpty()) {
            return true;
        }
        return permittedResourceTypes.stream().anyMatch(rt -> rt.equalsIgnoreCase(resourceType));
    }

    public boolean permitsSecurityDomain(String domain) {
        if (domain == null || permittedSecurityDomains.isEmpty()) {
            return true;
        }
        return permittedSecurityDomains.stream().anyMatch(d -> d.equalsIgnoreCase(domain));
    }

    public static class Builder {
        private String ergonId;
        private final Set<ThemisAuthority> requiredExecutionAuthorities = new HashSet<>();
        private final Set<ThemisAction> permittedActions = new HashSet<>();
        private final Set<String> permittedResourceTypes = new HashSet<>();
        private final Set<String> permittedSecurityDomains = new HashSet<>();

        public Builder ergonId(String ergonId) {
            this.ergonId = ergonId;
            return this;
        }

        public Builder requiredExecutionAuthority(ThemisAuthority authority) {
            if (authority != null) {
                this.requiredExecutionAuthorities.add(authority);
            }
            return this;
        }

        public Builder requiredExecutionAuthorities(Set<ThemisAuthority> authorities) {
            if (authorities != null) {
                this.requiredExecutionAuthorities.addAll(authorities);
            }
            return this;
        }

        public Builder permittedAction(ThemisAction action) {
            if (action != null) {
                this.permittedActions.add(action);
            }
            return this;
        }

        public Builder permittedActions(Set<ThemisAction> actions) {
            if (actions != null) {
                this.permittedActions.addAll(actions);
            }
            return this;
        }

        public Builder permittedResourceType(String resourceType) {
            if (resourceType != null && !resourceType.isBlank()) {
                this.permittedResourceTypes.add(resourceType.trim());
            }
            return this;
        }

        public Builder permittedResourceTypes(Set<String> resourceTypes) {
            if (resourceTypes != null) {
                this.permittedResourceTypes.addAll(resourceTypes);
            }
            return this;
        }

        public Builder permittedSecurityDomain(String domain) {
            if (domain != null && !domain.isBlank()) {
                this.permittedSecurityDomains.add(domain.trim());
            }
            return this;
        }

        public Builder permittedSecurityDomains(Set<String> domains) {
            if (domains != null) {
                this.permittedSecurityDomains.addAll(domains);
            }
            return this;
        }

        public ErgonSecurityDefinition build() {
            return new ErgonSecurityDefinition(
                    ergonId,
                    requiredExecutionAuthorities,
                    permittedActions,
                    permittedResourceTypes,
                    permittedSecurityDomains
            );
        }
    }
}
