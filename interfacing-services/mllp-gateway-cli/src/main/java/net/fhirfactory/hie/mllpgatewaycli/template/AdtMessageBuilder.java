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

package net.fhirfactory.hie.mllpgatewaycli.template;

import java.util.Objects;

/**
 * Builds valid HL7 v2.4 ADT messages from templates and parameters.
 */
public class AdtMessageBuilder {

    /**
     * Builds an HL7 v2.4 ADT message for the specified template type and parameters.
     */
    public static String buildMessage(AdtTemplateType templateType, AdtMessageParameters params) {
        Objects.requireNonNull(templateType, "Template type must not be null");
        String rawTemplate = AdtTemplateRegistry.getRawTemplate(templateType)
                .orElseThrow(() -> new IllegalArgumentException("Unknown template type: " + templateType));
        return populateTemplate(rawTemplate, params);
    }

    /**
     * Replaces template placeholder variables with actual values from parameters.
     */
    public static String populateTemplate(String rawTemplate, AdtMessageParameters params) {
        if (rawTemplate == null) {
            return "";
        }
        AdtMessageParameters p = params != null ? params : new AdtMessageParameters();

        String message = rawTemplate
                .replace("${sendingApp}", p.getSendingApp())
                .replace("${sendingFacility}", p.getSendingFacility())
                .replace("${receivingApp}", p.getReceivingApp())
                .replace("${receivingFacility}", p.getReceivingFacility())
                .replace("${timestamp}", p.getEventTimestamp())
                .replace("${messageControlId}", p.getMessageControlId())
                .replace("${mrn}", p.getMrn())
                .replace("${assigningAuth}", p.getAssigningAuth())
                .replace("${identifierType}", p.getIdentifierType())
                .replace("${firstName}", p.getFirstName())
                .replace("${lastName}", p.getLastName())
                .replace("${dob}", p.getDateOfBirth())
                .replace("${gender}", p.getGender())
                .replace("${streetAddress}", p.getStreetAddress() != null ? p.getStreetAddress() : "123 Main Street")
                .replace("${city}", p.getCity() != null ? p.getCity() : "Metropolis")
                .replace("${state}", p.getState() != null ? p.getState() : "NY")
                .replace("${postalCode}", p.getPostalCode() != null ? p.getPostalCode() : "10001")
                .replace("${phoneNumber}", p.getPhoneNumber() != null ? p.getPhoneNumber() : "555-0100")
                .replace("${currentLocation}", p.getCurrentLocation())
                .replace("${priorLocation}", p.getPriorLocation())
                .replace("${attendingDoctor}", p.getAttendingDoctor())
                .replace("${visitNumber}", p.getVisitNumber());

        // Ensure segments end with standard carriage return \r
        return normalizeCarriageReturns(message);
    }

    /**
     * Customizes an existing HL7 message string with given parameter overrides.
     */
    public static String customizeRawHl7Message(String rawHl7, AdtMessageParameters params) {
        if (rawHl7 == null || rawHl7.isBlank() || params == null) {
            return rawHl7;
        }

        String normalized = rawHl7.replace("\r\n", "\r").replace("\n", "\r");
        String[] lines = normalized.split("\r");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            if (line.isBlank()) continue;
            String updatedLine = line;

            if (line.startsWith("MSH|")) {
                String[] fields = line.split("\\|", -1);
                if (fields.length > 2 && params.getSendingApp() != null) fields[2] = params.getSendingApp();
                if (fields.length > 3 && params.getSendingFacility() != null) fields[3] = params.getSendingFacility();
                if (fields.length > 9 && params.getMessageControlId() != null) fields[9] = params.getMessageControlId();
                updatedLine = String.join("|", fields);
            } else if (line.startsWith("PID|")) {
                String[] fields = line.split("\\|", -1);
                if (fields.length > 3 && params.getMrn() != null) {
                    fields[3] = params.getMrn() + "^^^" + params.getAssigningAuth() + "^" + params.getIdentifierType();
                }
                if (fields.length > 5 && (params.getFirstName() != null || params.getLastName() != null)) {
                    String lastName = params.getLastName();
                    String firstName = params.getFirstName();
                    fields[5] = lastName + "^" + firstName + "^^^^";
                }
                if (fields.length > 7 && params.getDateOfBirth() != null) {
                    fields[7] = params.getDateOfBirth();
                }
                if (fields.length > 8 && params.getGender() != null) {
                    fields[8] = params.getGender();
                }
                updatedLine = String.join("|", fields);
            } else if (line.startsWith("PV1|")) {
                String[] fields = line.split("\\|", -1);
                if (fields.length > 3 && params.getCurrentLocation() != null) {
                    fields[3] = params.getCurrentLocation();
                }
                if (fields.length > 7 && params.getAttendingDoctor() != null) {
                    fields[7] = params.getAttendingDoctor();
                }
                if (fields.length > 19 && params.getVisitNumber() != null) {
                    fields[19] = params.getVisitNumber();
                }
                updatedLine = String.join("|", fields);
            }

            result.append(updatedLine).append("\r");
        }

        return result.toString();
    }

    private static String normalizeCarriageReturns(String message) {
        String cleaned = message.replace("\r\n", "\r").replace("\n", "\r");
        if (!cleaned.endsWith("\r")) {
            cleaned = cleaned + "\r";
        }
        return cleaned;
    }
}
