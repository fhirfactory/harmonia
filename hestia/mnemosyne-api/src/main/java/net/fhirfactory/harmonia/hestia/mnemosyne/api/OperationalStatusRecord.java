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

package net.fhirfactory.harmonia.hestia.mnemosyne.api;

import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;

import java.util.Objects;

/**
 * Opaque owner-defined Status payload and its persistence version. Mneme/Mnemosyne manage
 * availability and durable storage; the owner retains the payload's semantics.
 */
public record OperationalStatusRecord(
        OperationalStatusKey key,
        String payload,
        AuthoritativeVersion version
) {

    /** Ensures the owner key and payload are present while allowing an unversioned write proposal. */
    public OperationalStatusRecord {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(payload, "payload must not be null");
    }

    /**
     * Creates an unversioned proposal for the next governed record operation.
     *
     * @param key owner/subject/type identity
     * @param payload opaque owner-defined status representation
     * @return unversioned status proposal
     */
    public static OperationalStatusRecord proposed(OperationalStatusKey key, String payload) {
        return new OperationalStatusRecord(key, payload, null);
    }
}