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

import java.util.Objects;

/**
 * Internal infrastructure bridge enabling Mneme coordination layers to construct and inspect
 * {@link ActiveStateToken} instances without exposing numeric or internal accessors to general callers.
 * <p>
 * Usage of this bridge is strictly restricted to {@code net.fhirfactory.harmonia.hestia.mneme..}
 * and contract unit tests by architectural guardrails.
 */
public final class ActiveStateTokenBridge {

    private ActiveStateTokenBridge() {
        // Utility / bridge class
    }

    public static ActiveStateToken create(long version) {
        return new ActiveStateToken(version);
    }

    public static long extractVersion(ActiveStateToken token) {
        Objects.requireNonNull(token, "token must not be null");
        return token.internalVersion();
    }
}
