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

package net.fhirfactory.harmonia.mllpgatewaycli.template;

import java.util.Arrays;
import java.util.Optional;

/**
 * Standard HL7 v2.4 ADT trigger event template definitions.
 */
public enum AdtTemplateType {

    A01("A01", "ADT^A01", "Admit / Visit Notification", "I"),
    A02("A02", "ADT^A02", "Transfer a Patient", "I"),
    A03("A03", "ADT^A03", "Discharge / End Visit", "I"),
    A04("A04", "ADT^A04", "Register a Patient", "O"),
    A05("A05", "ADT^A05", "Pre-Admit a Patient", "P"),
    A08("A08", "ADT^A08", "Update Patient Information", "I"),
    A11("A11", "ADT^A11", "Cancel Admit / Visit Notification", "I"),
    A12("A12", "ADT^A12", "Cancel Transfer", "I"),
    A13("A13", "ADT^A13", "Cancel Discharge / End Visit", "I"),
    A31("A31", "ADT^A31", "Update Person Information", "O"),
    A40("A40", "ADT^A40", "Merge Patient - Patient Identifier List", "I");

    private final String code;
    private final String messageType;
    private final String description;
    private final String defaultPatientClass;

    AdtTemplateType(String code, String messageType, String description, String defaultPatientClass) {
        this.code = code;
        this.messageType = messageType;
        this.description = description;
        this.defaultPatientClass = defaultPatientClass;
    }

    public String getCode() {
        return code;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getDescription() {
        return description;
    }

    public String getDefaultPatientClass() {
        return defaultPatientClass;
    }

    public static Optional<AdtTemplateType> fromString(String input) {
        if (input == null || input.isBlank()) {
            return Optional.empty();
        }
        String normalized = input.trim().toUpperCase()
                .replace("ADT^", "")
                .replace("ADT_", "")
                .replace("ADT-", "");
        return Arrays.stream(values())
                .filter(t -> t.code.equalsIgnoreCase(normalized) || t.messageType.equalsIgnoreCase(input.trim()))
                .findFirst();
    }
}
