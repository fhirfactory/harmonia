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

import java.io.Serializable;
import java.util.Objects;

/**
 * Immutable identifier for a governed resource, consisting of resource type and logical id.
 */
public record ResourceKey(
        String resourceType,
        String id
) implements Serializable {

    public ResourceKey {
        Objects.requireNonNull(resourceType, "resourceType must not be null");
        Objects.requireNonNull(id, "id must not be null");
        if (resourceType.isBlank()) {
            throw new IllegalArgumentException("resourceType must not be blank");
        }
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
    }

    public static ResourceKey of(String resourceType, String id) {
        return new ResourceKey(resourceType, id);
    }

    public String toQualifiedPath() {
        return resourceType + "/" + id;
    }

    @Override
    public String toString() {
        return resourceType + "/" + id;
    }
}
