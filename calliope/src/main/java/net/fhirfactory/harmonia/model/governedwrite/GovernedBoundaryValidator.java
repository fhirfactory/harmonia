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

package net.fhirfactory.harmonia.model.governedwrite;

import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;

import java.util.Objects;

/**
 * Fail-closed boundary validator for the existing Provider Registry domain classification.
 * <p>
 * Enforces classification verification at the managed-access boundary before delegating
 * to coordination or persistence tiers. For this validator,
 * {@link ProviderRegistryConstants} is authoritative for the Provider Registry resource
 * types it defines; unclassified or unsupported types are rejected fail-closed.
 * {@link ProviderRegistryConstants} is not a universal registry of all
 * Harmonia-managed information. Broader managed-information classification remains a
 * future Calliope concern as additional information domains are introduced.
 */
public final class GovernedBoundaryValidator {

    private GovernedBoundaryValidator() {
        // Utility validator class
    }

    /**
     * Asserts that the given resource key represents a supported managed type.
     *
     * @param key target resource key
     * @throws IllegalArgumentException if the resource type is unclassified or unsupported
     * @throws NullPointerException     if the key is null
     */
    public static void requireManagedType(ResourceKey key) {
        Objects.requireNonNull(key, "key must not be null");
        requireManagedType(key.resourceType());
    }

    /**
     * Asserts that the given resource type string represents a supported managed type.
     *
     * @param resourceType target resource type name
     * @throws IllegalArgumentException if the resource type is null, blank, unclassified, or unsupported
     */
    public static void requireManagedType(String resourceType) {
        if (resourceType == null || resourceType.isBlank()) {
            throw new IllegalArgumentException("resourceType must not be null or blank");
        }
        if (!isManagedType(resourceType)) {
            throw new IllegalArgumentException(
                    "Resource type [" + resourceType + "] is not a supported managed type"
            );
        }
    }

    /**
     * Checks if the given resource type is a supported managed type.
     *
     * @param resourceType resource type name
     * @return true if classified as a supported managed type, false otherwise
     */
    public static boolean isManagedType(String resourceType) {
        return ProviderRegistryConstants.isSupportedResourceType(resourceType);
    }

    /**
     * Checks if the given resource key represents a supported managed type.
     *
     * @param key target resource key
     * @return true if key is non-null and its resource type is a supported managed type, false otherwise
     */
    public static boolean isManagedType(ResourceKey key) {
        return key != null && isManagedType(key.resourceType());
    }
}
