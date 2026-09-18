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

package net.fhirfactory.harmonia.paradeigma.common.model;

/**
 * Administrative sex / gender enumeration corresponding to HL7 v2 Table 0001.
 */
public enum Gender {
    M("M", "Male"),
    F("F", "Female"),
    O("O", "Other"),
    U("U", "Unknown");

    private final String hl7Code;
    private final String description;

    Gender(String hl7Code, String description) {
        this.hl7Code = hl7Code;
        this.description = description;
    }

    public String getHl7Code() {
        return hl7Code;
    }

    public String getDescription() {
        return description;
    }

    public static Gender fromHl7Code(String code) {
        if (code == null) {
            return U;
        }
        for (Gender g : values()) {
            if (g.hl7Code.equalsIgnoreCase(code.trim())) {
                return g;
            }
        }
        return U;
    }
}
