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
import java.util.Optional;

/**
 * Value record identifying the optional current active representation instance (Active Hegemon)
 * for a managed resource within Mneme.
 * <p>
 * When absent ({@code instanceId == null}), participating instances are considered aligned and current.
 * When present, identifies the instance containing Mneme's current active representation.
 * Conveys neither Mnemosyne durable authority nor information standing.
 */
public record ActiveHegemon(InstanceId instanceId) implements Serializable {

    public static ActiveHegemon of(InstanceId id) {
        return new ActiveHegemon(Objects.requireNonNull(id, "instanceId must not be null"));
    }

    public static ActiveHegemon none() {
        return new ActiveHegemon(null);
    }

    public boolean isPresent() {
        return instanceId != null;
    }

    public Optional<InstanceId> instanceIdOptional() {
        return Optional.ofNullable(instanceId);
    }
}
