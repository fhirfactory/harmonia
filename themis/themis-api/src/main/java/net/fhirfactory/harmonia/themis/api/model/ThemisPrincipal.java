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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.themis.api.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an authenticated identity requesting or performing work across Harmonia.
 */
public record ThemisPrincipal(
        String principalId,
        PrincipalType principalType,
        String sourceDomain,
        Map<String, String> attributes
) implements Serializable {

    public ThemisPrincipal {
        attributes = attributes == null ? Map.of() : Collections.unmodifiableMap(Map.copyOf(attributes));
    }

    public static ThemisPrincipal of(String principalId, PrincipalType principalType) {
        return new ThemisPrincipal(principalId, principalType, null, Map.of());
    }

    public static ThemisPrincipal of(String principalId, PrincipalType principalType, String sourceDomain) {
        return new ThemisPrincipal(principalId, principalType, sourceDomain, Map.of());
    }

    public static ThemisPrincipal human(String principalId) {
        return of(principalId, PrincipalType.HUMAN);
    }

    public static ThemisPrincipal system(String principalId) {
        return of(principalId, PrincipalType.SYSTEM);
    }

    public static ThemisPrincipal service(String principalId) {
        return of(principalId, PrincipalType.SERVICE);
    }

    public static ThemisPrincipal process(String principalId) {
        return of(principalId, PrincipalType.PROCESS);
    }
}
