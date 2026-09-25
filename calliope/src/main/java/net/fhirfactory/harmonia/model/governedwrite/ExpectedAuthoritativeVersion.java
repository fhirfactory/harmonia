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
 * Expected authoritative predecessor version for conditional writes.
 * <p>
 * Represents either expected absence (for CREATE) or a specific prior authoritative version (for UPDATE).
 */
public final class ExpectedAuthoritativeVersion implements Serializable {

    private static final ExpectedAuthoritativeVersion NONE = new ExpectedAuthoritativeVersion(null);

    private final AuthoritativeVersion version;

    private ExpectedAuthoritativeVersion(AuthoritativeVersion version) {
        this.version = version;
    }

    public static ExpectedAuthoritativeVersion none() {
        return NONE;
    }

    public static ExpectedAuthoritativeVersion of(AuthoritativeVersion version) {
        Objects.requireNonNull(version, "version must not be null");
        return new ExpectedAuthoritativeVersion(version);
    }

    public static ExpectedAuthoritativeVersion of(String versionString) {
        Objects.requireNonNull(versionString, "versionString must not be null");
        return new ExpectedAuthoritativeVersion(AuthoritativeVersion.of(versionString));
    }

    public static ExpectedAuthoritativeVersion of(long versionNumber) {
        return new ExpectedAuthoritativeVersion(AuthoritativeVersion.of(versionNumber));
    }

    public boolean isNone() {
        return version == null;
    }

    public Optional<AuthoritativeVersion> version() {
        return Optional.ofNullable(version);
    }

    public Optional<String> value() {
        return version().map(AuthoritativeVersion::value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpectedAuthoritativeVersion that = (ExpectedAuthoritativeVersion) o;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    public String toString() {
        return isNone() ? "ExpectedAuthoritativeVersion[none]" : "ExpectedAuthoritativeVersion[" + version.value() + "]";
    }
}
