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

package net.fhirfactory.harmonia.paradeigma.common.hl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.util.idgenerator.NanoTimeGenerator;

import java.util.UUID;

/**
 * Utility wrapper around HAPI HL7 v2 parser and Terser navigation.
 */
public final class Hl7Parsers {

    private static final HapiContext CONTEXT = new DefaultHapiContext();

    static {
        CONTEXT.getParserConfiguration().setIdGenerator(new NanoTimeGenerator());
        CONTEXT.setValidationContext(new ca.uhn.hl7v2.validation.impl.NoValidation());
    }

    private Hl7Parsers() {
        // Utility class
    }

    public static PipeParser getPipeParser() {
        return CONTEXT.getPipeParser();
    }

    public static Message parse(String rawHl7) throws HL7Exception {
        if (rawHl7 == null || rawHl7.isBlank()) {
            throw new HL7Exception("Cannot parse empty or null HL7 message");
        }
        return getPipeParser().parse(rawHl7.trim());
    }

    public static String encode(Message message) throws HL7Exception {
        if (message == null) {
            return null;
        }
        return getPipeParser().encode(message);
    }

    public static Terser getTerser(Message message) {
        return new Terser(message);
    }

    public static Terser getTerser(String rawHl7) throws HL7Exception {
        return new Terser(parse(rawHl7));
    }

    /**
     * Extracts MSH-10 Message Control ID using Terser with regex fallback.
     */
    public static String extractMessageControlId(String rawHl7) {
        if (rawHl7 == null) {
            return UUID.randomUUID().toString();
        }
        try {
            Message msg = parse(rawHl7);
            Terser terser = new Terser(msg);
            String id = terser.get("/.MSH-10");
            if (id != null && !id.isBlank()) {
                return id.trim();
            }
        } catch (Exception ignored) {
        }

        // Regex fallback
        return extractFieldByRegex(rawHl7, "MSH", 10);
    }

    /**
     * Extracts MSH-9-1 Message Type (e.g. ADT, ORM, ORU).
     */
    public static String extractMessageType(String rawHl7) {
        if (rawHl7 == null) return null;
        try {
            Terser terser = getTerser(rawHl7);
            return terser.get("/.MSH-9-1");
        } catch (Exception e) {
            return extractSubFieldByRegex(rawHl7, "MSH", 9, 1);
        }
    }

    /**
     * Extracts MSH-9-2 Trigger Event (e.g. A01, A04, O01, R01).
     */
    public static String extractTriggerEvent(String rawHl7) {
        if (rawHl7 == null) return null;
        try {
            Terser terser = getTerser(rawHl7);
            return terser.get("/.MSH-9-2");
        } catch (Exception e) {
            return extractSubFieldByRegex(rawHl7, "MSH", 9, 2);
        }
    }

    /**
     * Extracts OBR-4.1 Universal Service Identifier (e.g. CBC, XR_CHEST).
     */
    public static String extractUniversalServiceId(String rawHl7) {
        if (rawHl7 == null) return null;
        try {
            Terser terser = getTerser(rawHl7);
            String obr4_1 = terser.get("/.OBR-4-1");
            if (obr4_1 != null && !obr4_1.isBlank()) {
                return obr4_1.trim();
            }
        } catch (Exception ignored) {
        }
        return extractSubFieldByRegex(rawHl7, "OBR", 4, 1);
    }

    private static String extractFieldByRegex(String rawHl7, String segmentName, int fieldIndex) {
        String[] lines = rawHl7.split("\r\n|\r|\n");
        for (String line : lines) {
            if (line.startsWith(segmentName + "|")) {
                String[] fields = line.split("\\|", -1);
                // In MSH, fields[1] is the field separator '|', so fields[2] is MSH-3
                int idx = "MSH".equals(segmentName) ? fieldIndex - 1 : fieldIndex;
                if (idx < fields.length) {
                    return fields[idx].trim();
                }
            }
        }
        return UUID.randomUUID().toString();
    }

    private static String extractSubFieldByRegex(String rawHl7, String segmentName, int fieldIndex, int componentIndex) {
        String field = extractFieldByRegex(rawHl7, segmentName, fieldIndex);
        if (field == null || field.isBlank()) {
            return null;
        }
        String[] components = field.split("\\^", -1);
        int cIdx = componentIndex - 1;
        if (cIdx >= 0 && cIdx < components.length) {
            return components[cIdx].trim();
        }
        return field;
    }
}
