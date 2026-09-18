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

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Utility for parsing and generating HL7 v2.4 Acknowledgement (ACK) messages.
 */
public final class Hl7AckHandler {

    private static final DateTimeFormatter HL7_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private Hl7AckHandler() {
        // Utility class
    }

    /**
     * Parses a raw HL7 ACK string and returns an {@link AckResult}.
     *
     * @param rawAck raw ACK string
     * @return parsed AckResult
     */
    public static AckResult parseAck(String rawAck) {
        if (rawAck == null || rawAck.isBlank()) {
            return new AckResult("AE", UUID.randomUUID().toString(), null, "Empty ACK payload", rawAck);
        }

        try {
            Message msg = Hl7Parsers.parse(rawAck);
            Terser terser = new Terser(msg);

            String ackCode = terser.get("/.MSA-1");
            String correlatedId = terser.get("/.MSA-2");
            String textMessage = terser.get("/.MSA-3");
            String messageControlId = terser.get("/.MSH-10");

            return new AckResult(
                    ackCode != null ? ackCode : "AA",
                    messageControlId,
                    correlatedId,
                    textMessage,
                    rawAck
            );
        } catch (Exception e) {
            // Regex fallback for non-standard or malformed ACKs
            String ackCode = extractField(rawAck, "MSA", 1);
            String correlatedId = extractField(rawAck, "MSA", 2);
            String textMessage = extractField(rawAck, "MSA", 3);
            String messageControlId = extractField(rawAck, "MSH", 10);

            return new AckResult(
                    ackCode != null ? ackCode : "AE",
                    messageControlId,
                    correlatedId,
                    textMessage != null ? textMessage : e.getMessage(),
                    rawAck
            );
        }
    }

    /**
     * Generates a successful AA ACK for an inbound HL7 message.
     */
    public static String generateAcceptAck(String rawMessage) {
        return generateAck(rawMessage, "AA", "Message accepted successfully");
    }

    /**
     * Generates an application error AE ACK for an inbound HL7 message.
     */
    public static String generateErrorAck(String rawMessage, String errorMessage) {
        return generateAck(rawMessage, "AE", errorMessage != null ? errorMessage : "Application error");
    }

    /**
     * Generates an application reject AR ACK for an inbound HL7 message.
     */
    public static String generateRejectAck(String rawMessage, String rejectMessage) {
        return generateAck(rawMessage, "AR", rejectMessage != null ? rejectMessage : "Application reject");
    }

    /**
     * Generates a standard HL7 v2.4 ACK message for an incoming HL7 message.
     *
     * @param rawMessage the incoming message to acknowledge
     * @param ackCode the ACK status code (AA, AE, AR)
     * @param textMessage human readable message in MSA-3
     * @return encoded ACK string with \r delimiters
     */
    public static String generateAck(String rawMessage, String ackCode, String textMessage) {
        String originalControlId = Hl7Parsers.extractMessageControlId(rawMessage);
        String sendingApp = extractField(rawMessage, "MSH", 3);
        String sendingFac = extractField(rawMessage, "MSH", 4);
        String receivingApp = extractField(rawMessage, "MSH", 5);
        String receivingFac = extractField(rawMessage, "MSH", 6);

        String ackSendingApp = (receivingApp != null && !receivingApp.isBlank()) ? receivingApp : "HARMONIA";
        String ackSendingFac = (receivingFac != null && !receivingFac.isBlank()) ? receivingFac : "FACILITY";
        String ackReceivingApp = (sendingApp != null && !sendingApp.isBlank()) ? sendingApp : "SIMULATOR";
        String ackReceivingFac = (sendingFac != null && !sendingFac.isBlank()) ? sendingFac : "FACILITY";

        String timestamp = LocalDateTime.now().format(HL7_DATE_FORMAT);
        String ackControlId = "ACK-" + UUID.randomUUID().toString().substring(0, 8);
        String code = ackCode != null ? ackCode : "AA";
        String text = textMessage != null ? textMessage.replace('|', ' ').replace('\r', ' ').replace('\n', ' ') : "Success";

        return "MSH|^~\\&|" + ackSendingApp + "|" + ackSendingFac + "|" + ackReceivingApp + "|" + ackReceivingFac + "|"
                + timestamp + "||ACK|" + ackControlId + "|P|2.4\r"
                + "MSA|" + code + "|" + originalControlId + "|" + text + "\r";
    }

    private static String extractField(String rawHl7, String segmentName, int fieldIndex) {
        if (rawHl7 == null) return null;
        String[] lines = rawHl7.split("\r\n|\r|\n");
        for (String line : lines) {
            if (line.startsWith(segmentName + "|")) {
                String[] fields = line.split("\\|", -1);
                int idx = "MSH".equals(segmentName) ? fieldIndex - 1 : fieldIndex;
                if (idx < fields.length) {
                    return fields[idx].trim();
                }
            }
        }
        return null;
    }
}
