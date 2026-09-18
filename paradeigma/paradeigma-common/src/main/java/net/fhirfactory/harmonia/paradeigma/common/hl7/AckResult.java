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

import java.util.Objects;

/**
 * Parsed HL7 Acknowledgement (ACK) result model.
 */
public class AckResult {

    private final String ackCode;             // AA (Accept), AE (Error), AR (Reject), CA, CE, CR
    private final String messageControlId;    // MSH-10 of the ACK
    private final String correlatedMessageId; // MSA-2 (reference to original message MSH-10)
    private final String textMessage;         // MSA-3 text explanation
    private final String rawAck;              // full raw ACK string

    public AckResult(String ackCode, String messageControlId, String correlatedMessageId, String textMessage, String rawAck) {
        this.ackCode = ackCode != null ? ackCode.trim().toUpperCase() : "AA";
        this.messageControlId = messageControlId;
        this.correlatedMessageId = correlatedMessageId;
        this.textMessage = textMessage;
        this.rawAck = rawAck;
    }

    public boolean isAccept() {
        return "AA".equals(ackCode) || "CA".equals(ackCode);
    }

    public boolean isError() {
        return "AE".equals(ackCode) || "CE".equals(ackCode);
    }

    public boolean isReject() {
        return "AR".equals(ackCode) || "CR".equals(ackCode);
    }

    public boolean isSuccess() {
        return isAccept();
    }

    public boolean matchesControlId(String expectedControlId) {
        if (expectedControlId == null || correlatedMessageId == null) {
            return false;
        }
        return expectedControlId.trim().equals(correlatedMessageId.trim());
    }

    public String getAckCode() {
        return ackCode;
    }

    public String getMessageControlId() {
        return messageControlId;
    }

    public String getCorrelatedMessageId() {
        return correlatedMessageId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public String getRawAck() {
        return rawAck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AckResult ackResult = (AckResult) o;
        return Objects.equals(ackCode, ackResult.ackCode) &&
                Objects.equals(correlatedMessageId, ackResult.correlatedMessageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ackCode, correlatedMessageId);
    }

    @Override
    public String toString() {
        return "AckResult{" +
                "code='" + ackCode + '\'' +
                ", correlatedId='" + correlatedMessageId + '\'' +
                ", text='" + textMessage + '\'' +
                '}';
    }
}
