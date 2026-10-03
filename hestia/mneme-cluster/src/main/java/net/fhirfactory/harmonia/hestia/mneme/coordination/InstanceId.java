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

package net.fhirfactory.harmonia.hestia.mneme.coordination;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Opaque, UUID-based value record identifying an active resource instance managed by Mneme.
 * <p>
 * Represents active-state management metadata within Mneme's cache coordination layer.
 * It is not FHIR content, not information identity, not durable identity or authoritative version,
 * and is not required to survive reconstruction of lost active state.
 */
public record InstanceId(UUID value) implements Serializable {

    public InstanceId {
        Objects.requireNonNull(value, "value must not be null");
    }

    public static InstanceId random() {
        return new InstanceId(UUID.randomUUID());
    }

    public static InstanceId of(UUID value) {
        return new InstanceId(value);
    }
}
