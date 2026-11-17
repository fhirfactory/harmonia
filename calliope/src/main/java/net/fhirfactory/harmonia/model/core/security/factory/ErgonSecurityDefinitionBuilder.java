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

package net.fhirfactory.harmonia.model.core.security.factory;

import net.fhirfactory.harmonia.model.core.security.ErgonSecurityDefinition;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;

import java.util.HashSet;
import java.util.Set;

/** Assembles a default-deny Ergon security envelope from explicit allow-list entries. */
public final class ErgonSecurityDefinitionBuilder {

    private String ergonId;
    private final Set<ThemisAuthority> requiredExecutionAuthorities = new HashSet<>();
    private final Set<ThemisAction> permittedActions = new HashSet<>();
    private final Set<String> permittedResourceTypes = new HashSet<>();
    private final Set<String> permittedSecurityDomains = new HashSet<>();

    /** Sets the Ergon activity identifier. */
    public ErgonSecurityDefinitionBuilder ergonId(String ergonId) {
        this.ergonId = ergonId;
        return this;
    }

    /** Adds one required execution authority. */
    public ErgonSecurityDefinitionBuilder requiredExecutionAuthority(ThemisAuthority authority) {
        if (authority != null) {
            requiredExecutionAuthorities.add(authority);
        }
        return this;
    }

    /** Adds required execution authorities. */
    public ErgonSecurityDefinitionBuilder requiredExecutionAuthorities(Set<ThemisAuthority> authorities) {
        if (authorities != null) {
            requiredExecutionAuthorities.addAll(authorities);
        }
        return this;
    }

    /** Adds one explicitly permitted action. */
    public ErgonSecurityDefinitionBuilder permittedAction(ThemisAction action) {
        if (action != null) {
            permittedActions.add(action);
        }
        return this;
    }

    /** Adds explicitly permitted actions. */
    public ErgonSecurityDefinitionBuilder permittedActions(Set<ThemisAction> actions) {
        if (actions != null) {
            permittedActions.addAll(actions);
        }
        return this;
    }

    /** Adds one explicitly permitted FHIR resource type. */
    public ErgonSecurityDefinitionBuilder permittedResourceType(String resourceType) {
        if (resourceType != null && !resourceType.isBlank()) {
            permittedResourceTypes.add(resourceType.trim());
        }
        return this;
    }

    /** Adds explicitly permitted FHIR resource types. */
    public ErgonSecurityDefinitionBuilder permittedResourceTypes(Set<String> resourceTypes) {
        if (resourceTypes != null) {
            resourceTypes.stream().filter(value -> value != null && !value.isBlank())
                    .map(String::trim).forEach(permittedResourceTypes::add);
        }
        return this;
    }

    /** Adds one explicitly permitted security domain. */
    public ErgonSecurityDefinitionBuilder permittedSecurityDomain(String domain) {
        if (domain != null && !domain.isBlank()) {
            permittedSecurityDomains.add(domain.trim());
        }
        return this;
    }

    /** Adds explicitly permitted security domains. */
    public ErgonSecurityDefinitionBuilder permittedSecurityDomains(Set<String> domains) {
        if (domains != null) {
            domains.stream().filter(value -> value != null && !value.isBlank())
                    .map(String::trim).forEach(permittedSecurityDomains::add);
        }
        return this;
    }

    /** Builds an immutable security definition; empty allow-lists remain deny-all. */
    public ErgonSecurityDefinition build() {
        return new ErgonSecurityDefinition(ergonId, requiredExecutionAuthorities, permittedActions,
                permittedResourceTypes, permittedSecurityDomains);
    }
}