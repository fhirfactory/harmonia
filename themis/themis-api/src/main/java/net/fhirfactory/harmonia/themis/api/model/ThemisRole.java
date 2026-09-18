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
import java.util.Objects;
import java.util.Set;

/**
 * Mnemonic role grouping one or more granular Themis authorities.
 */
public record ThemisRole(
        String roleCode,
        Set<ThemisAuthority> authorities,
        String description
) implements Serializable {

    public ThemisRole {
        Objects.requireNonNull(roleCode, "roleCode must not be null");
        roleCode = roleCode.trim();
        if (roleCode.isEmpty()) {
            throw new IllegalArgumentException("roleCode must not be blank");
        }
        authorities = authorities == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(authorities));
    }

    public static ThemisRole of(String roleCode, Set<ThemisAuthority> authorities) {
        return new ThemisRole(roleCode, authorities, null);
    }

    public static ThemisRole of(String roleCode, ThemisAuthority... authorities) {
        return new ThemisRole(roleCode, authorities == null ? Set.of() : Set.of(authorities), null);
    }

    public static ThemisRole of(String roleCode, String description, Set<ThemisAuthority> authorities) {
        return new ThemisRole(roleCode, authorities, description);
    }
}
