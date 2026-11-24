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

import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

/**
 * Stable owner/subject/type identity for owner-defined operational Status. The external Themis
 * resource identifier is a deterministic opaque value so a status subject is not exposed as a log key.
 */
public record OperationalStatusKey(String owner, String subject, String statusType) {

    /** Validates and trims the identity components at the contract boundary. */
    public OperationalStatusKey {
        owner = requireValue(owner, "owner");
        subject = requireValue(subject, "subject");
        statusType = requireValue(statusType, "statusType");
    }

    /**
     * Returns an opaque stable resource key for Themis decisions and conflict reporting.
     *
     * @return key whose identifier does not disclose the owner/subject tuple
     */
    public ResourceKey resourceKey() {
        String identity = encode(owner) + encode(subject) + encode(statusType);
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(identity.getBytes(StandardCharsets.UTF_8));
            return ResourceKey.of("OperationalStatus", HexFormat.of().formatHex(digest));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("The Java runtime does not provide SHA-256", exception);
        }
    }

    /** Validates one PostgreSQL-compatible identity component without normalizing its value. */
    private static String requireValue(String value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        if (value.indexOf('\u0000') >= 0) {
            throw new IllegalArgumentException(name + " must not contain a NUL character");
        }
        return value;
    }

    /** Length-prefixes a key part so distinct owner tuples cannot share an encoded identity. */
    private static String encode(String value) {
        return value.length() + ":" + value;
    }
}