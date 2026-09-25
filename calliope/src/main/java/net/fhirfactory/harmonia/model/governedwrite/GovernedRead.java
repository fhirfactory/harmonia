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
 * Result of a governed read, packaging the resource along with both active coordination context (Mneme)
 * and authoritative persistence predecessor context (Mnemosyne).
 *
 * @param <T> resource domain payload type
 */
public record GovernedRead<T>(
        ResourceKey key,
        T resource,
        ActiveStateToken activeToken,
        AuthoritativeVersion authoritativeVersion
) implements Serializable {

    public GovernedRead {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(resource, "resource must not be null");
        Objects.requireNonNull(activeToken, "activeToken must not be null");
        Objects.requireNonNull(authoritativeVersion, "authoritativeVersion must not be null");
    }

    public static <T> GovernedRead<T> of(
            ResourceKey key,
            T resource,
            ActiveStateToken activeToken,
            AuthoritativeVersion authoritativeVersion) {
        return new GovernedRead<>(key, resource, activeToken, authoritativeVersion);
    }

    public ExpectedAuthoritativeVersion expectedAuthoritativeVersion() {
        return authoritativeVersion.toExpected();
    }
}
