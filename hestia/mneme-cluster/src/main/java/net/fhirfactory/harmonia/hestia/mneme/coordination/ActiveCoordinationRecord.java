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
 * Cache-resident coordination record encoding Active Hegemon state for a managed resource.
 * <p>
 * Strictly fail-closed under AX-14 and AX-15: blank, whitespace, malformed, or unrecognised
 * encodings throw {@link ActiveCoordinationCorruptException}.
 */
public record ActiveCoordinationRecord(ActiveHegemon activeHegemon) implements Serializable {

    public static final String NO_HEGEMON_MARKER = "NO_HEGEMON";
    public static final String HEGEMON_PREFIX = "HEGEMON:";

    public ActiveCoordinationRecord {
        Objects.requireNonNull(activeHegemon, "activeHegemon must not be null");
    }

    public static ActiveCoordinationRecord none() {
        return new ActiveCoordinationRecord(ActiveHegemon.none());
    }

    public static ActiveCoordinationRecord of(InstanceId id) {
        return new ActiveCoordinationRecord(ActiveHegemon.of(id));
    }

    public String encode() {
        if (!activeHegemon.isPresent()) {
            return NO_HEGEMON_MARKER;
        }
        return HEGEMON_PREFIX + activeHegemon.instanceId().value().toString();
    }

    public static ActiveCoordinationRecord decode(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new ActiveCoordinationCorruptException("Active coordination record is blank or null");
        }
        String trimmed = raw.trim();
        if (NO_HEGEMON_MARKER.equals(trimmed)) {
            return none();
        }
        if (trimmed.startsWith(HEGEMON_PREFIX)) {
            String uuidStr = trimmed.substring(HEGEMON_PREFIX.length()).trim();
            try {
                UUID uuid = UUID.fromString(uuidStr);
                return of(InstanceId.of(uuid));
            } catch (IllegalArgumentException e) {
                throw new ActiveCoordinationCorruptException("Malformed InstanceId in coordination record: " + trimmed, e);
            }
        }
        throw new ActiveCoordinationCorruptException("Unrecognised active coordination record encoding: " + trimmed);
    }
}
