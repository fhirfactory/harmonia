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

/**
 * Immutable, opaque active-state token representing distributed cache entry state (Mneme).
 * <p>
 * This token encapsulates coordination state without exposing underlying cache, version numbers,
 * or storage implementation details. It strictly forbids arithmetic, numeric incrementing,
 * ordering comparisons, and any Infinispan or Hot Rod types.
 * <p>
 * Construction and version extraction are restricted to infrastructure via {@link ActiveStateTokenBridge}.
 */
public final class ActiveStateToken implements Serializable {

    private final long version;

    // Package-private constructor for internal bridge only
    ActiveStateToken(long version) {
        if (version < 0) {
            throw new IllegalArgumentException("Version must be non-negative");
        }
        this.version = version;
    }

    // Package-private accessor for internal bridge only
    long internalVersion() {
        return this.version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiveStateToken that = (ActiveStateToken) o;
        return this.version == that.version;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(version);
    }

    @Override
    public String toString() {
        return "ActiveStateToken[opaque]";
    }
}
