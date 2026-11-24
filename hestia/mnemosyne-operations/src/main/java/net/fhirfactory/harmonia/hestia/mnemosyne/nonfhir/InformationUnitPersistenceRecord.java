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

package net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir;

import java.util.Objects;
import java.util.UUID;

/**
 * Engine-neutral persistence representation for one IU revision. Version zero marks an
 * uncommitted proposal; Mnemosyne assigns a positive authoritative version on commit.
 */
public record InformationUnitPersistenceRecord(
        UUID identifier,
        String topicName,
        long version,
        String canonicalJson,
        String sha256
) {

    /** Validates stable identity, serialised content and version before persistence. */
    public InformationUnitPersistenceRecord {
        Objects.requireNonNull(identifier, "identifier must not be null");
        requireText(topicName, "topicName");
        requireText(canonicalJson, "canonicalJson");
        requireText(sha256, "sha256");
        if (version < 0) {
            throw new IllegalArgumentException("version must not be negative");
        }
        if (!sha256.matches("[0-9a-f]{64}")) {
            throw new IllegalArgumentException("sha256 must be a lowercase 64-character hexadecimal digest");
        }
    }

    /**
     * Returns the same immutable IU revision with its database-assigned version.
     *
     * @param authoritativeVersion positive persisted version
     * @return versioned record
     */
    public InformationUnitPersistenceRecord withVersion(long authoritativeVersion) {
        if (authoritativeVersion < 1) {
            throw new IllegalArgumentException("authoritativeVersion must be positive");
        }
        return new InformationUnitPersistenceRecord(identifier, topicName, authoritativeVersion, canonicalJson, sha256);
    }

    /** Validates a required nonblank string without exposing content in exception messages. */
    private static void requireText(String value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}