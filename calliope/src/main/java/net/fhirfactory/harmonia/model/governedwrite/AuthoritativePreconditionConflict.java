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
import java.util.Optional;

/**
 * Represents an authoritative persistence precondition conflict at the durable database boundary (Mnemosyne).
 */
public record AuthoritativePreconditionConflict(
        ResourceKey key,
        PreconditionFailureReason reason,
        ExpectedAuthoritativeVersion expectedVersion,
        AuthoritativeVersion currentVersion,
        String message
) implements Serializable {

    public AuthoritativePreconditionConflict {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(reason, "reason must not be null");
        Objects.requireNonNull(expectedVersion, "expectedVersion must not be null");
        Objects.requireNonNull(message, "message must not be null");
    }

    public static AuthoritativePreconditionConflict resourceAlreadyExists(ResourceKey key) {
        return new AuthoritativePreconditionConflict(
                key,
                PreconditionFailureReason.RESOURCE_ALREADY_EXISTS,
                ExpectedAuthoritativeVersion.none(),
                null,
                "Resource already exists: " + key
        );
    }

    public static AuthoritativePreconditionConflict resourceAlreadyExists(ResourceKey key, AuthoritativeVersion currentVersion) {
        return new AuthoritativePreconditionConflict(
                key,
                PreconditionFailureReason.RESOURCE_ALREADY_EXISTS,
                ExpectedAuthoritativeVersion.none(),
                currentVersion,
                "Resource already exists with version " + (currentVersion != null ? currentVersion.value() : "unknown") + ": " + key
        );
    }

    public static AuthoritativePreconditionConflict expectedVersionMismatch(
            ResourceKey key,
            ExpectedAuthoritativeVersion expectedVersion,
            AuthoritativeVersion currentVersion) {
        return new AuthoritativePreconditionConflict(
                key,
                PreconditionFailureReason.EXPECTED_VERSION_MISMATCH,
                expectedVersion,
                currentVersion,
                "Expected version " + expectedVersion + " did not match current version " + (currentVersion != null ? currentVersion.value() : "unknown") + " for " + key
        );
    }

    public Optional<AuthoritativeVersion> currentVersionOptional() {
        return Optional.ofNullable(currentVersion);
    }
}
