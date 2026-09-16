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
import java.util.Objects;

/**
 * Represents a granular permission or authority token evaluated by Themis policies.
 */
public record ThemisAuthority(String authorityCode) implements Serializable {

    public ThemisAuthority {
        Objects.requireNonNull(authorityCode, "authorityCode must not be null");
        authorityCode = authorityCode.trim();
        if (authorityCode.isEmpty()) {
            throw new IllegalArgumentException("authorityCode must not be blank");
        }
    }

    public static ThemisAuthority of(String authorityCode) {
        return new ThemisAuthority(authorityCode);
    }

    @Override
    public String toString() {
        return authorityCode;
    }
}
