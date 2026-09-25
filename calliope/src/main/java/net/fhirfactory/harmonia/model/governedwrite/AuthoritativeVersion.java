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
 * Authoritative persisted version assigned and committed by the durable database boundary (Mnemosyne).
 */
public record AuthoritativeVersion(
        String value
) implements Serializable {

    public AuthoritativeVersion {
        Objects.requireNonNull(value, "value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
    }

    public static AuthoritativeVersion of(String value) {
        return new AuthoritativeVersion(value);
    }

    public static AuthoritativeVersion of(long versionNumber) {
        return new AuthoritativeVersion(String.valueOf(versionNumber));
    }

    public ExpectedAuthoritativeVersion toExpected() {
        return ExpectedAuthoritativeVersion.of(this);
    }

    @Override
    public String toString() {
        return value;
    }
}
