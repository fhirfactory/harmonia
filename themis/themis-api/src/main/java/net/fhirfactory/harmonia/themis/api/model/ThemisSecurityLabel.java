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
 * Generic security classification label applied to resources, tasks, or data elements.
 */
public record ThemisSecurityLabel(String system, String code) implements Serializable {

    public static final String DEFAULT_SYSTEM = "http://harmonia.fhirfactory.net/security/labels";

    public ThemisSecurityLabel {
        if (system == null || system.isBlank()) {
            system = DEFAULT_SYSTEM;
        }
        Objects.requireNonNull(code, "code must not be null");
        code = code.trim();
        if (code.isEmpty()) {
            throw new IllegalArgumentException("code must not be blank");
        }
    }

    public static ThemisSecurityLabel of(String code) {
        return new ThemisSecurityLabel(DEFAULT_SYSTEM, code);
    }

    public static ThemisSecurityLabel of(String system, String code) {
        return new ThemisSecurityLabel(system, code);
    }

    public boolean matchesCode(String targetCode) {
        return code.equalsIgnoreCase(targetCode);
    }
}
