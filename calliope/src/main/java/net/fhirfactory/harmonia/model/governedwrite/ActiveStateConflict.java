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
 * Represents a transient active-coordination conflict in distributed cache memory (Mneme CAS contention).
 */
public record ActiveStateConflict(
        ResourceKey key,
        String message
) implements Serializable {

    public ActiveStateConflict {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(message, "message must not be null");
    }

    public static ActiveStateConflict of(ResourceKey key, String message) {
        return new ActiveStateConflict(key, message);
    }
}
