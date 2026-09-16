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
 * High-level clinical order category for Paradeigma workflow routing.
 */
public enum OrderType {
    LABORATORY("LAB", "Laboratory Order", "LMS"),
    DIAGNOSTIC_IMAGING("RAD", "Diagnostic Imaging Order", "RISPAC");

    private final String codePrefix;
    private final String description;
    private final String targetSystem;

    OrderType(String codePrefix, String description, String targetSystem) {
        this.codePrefix = codePrefix;
        this.description = description;
        this.targetSystem = targetSystem;
    }

    public String getCodePrefix() {
        return codePrefix;
    }

    public String getDescription() {
        return description;
    }

    public String getTargetSystem() {
        return targetSystem;
    }

    /**
     * Determines whether an OBR-4 Universal Service Identifier represents a Lab or Imaging order.
     *
     * @param universalServiceIdentifier e.g. "CBC", "LAB_ELEC", "XR_CHEST", "RAD_CT_HEAD"
     * @return OrderType (defaults to LABORATORY if unrecognized)
     */
    public static OrderType fromUniversalServiceIdentifier(String universalServiceIdentifier) {
        if (universalServiceIdentifier == null) {
            return LABORATORY;
        }
        String id = universalServiceIdentifier.trim().toUpperCase();
        if (id.startsWith("RAD") || id.startsWith("IMG") || id.startsWith("XR") ||
                id.startsWith("CT") || id.startsWith("MRI") || id.startsWith("US") ||
                id.contains("CHEST") || id.contains("HEAD") || id.contains("ABDOMEN") || id.contains("BRAIN")) {
            return DIAGNOSTIC_IMAGING;
        }
        return LABORATORY;
    }
}
