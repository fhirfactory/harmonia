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

package net.fhirfactory.hie.mllpgatewaycli.client;

/**
 * Encapsulates the response and metadata from sending an MLLP message.
 */
public class MllpResult {

    private final boolean success;
    private final String ackCode;
    private final String messageControlId;
    private final String ackText;
    private final String rawAck;
    private final String errorMessage;
    private final long durationMs;

    public MllpResult(boolean success, String ackCode, String messageControlId, String ackText,
                      String rawAck, String errorMessage, long durationMs) {
        this.success = success;
        this.ackCode = ackCode;
        this.messageControlId = messageControlId;
        this.ackText = ackText;
        this.rawAck = rawAck;
        this.errorMessage = errorMessage;
        this.durationMs = durationMs;
    }

    public static MllpResult success(String ackCode, String messageControlId, String ackText, String rawAck, long durationMs) {
        return new MllpResult(true, ackCode, messageControlId, ackText, rawAck, null, durationMs);
    }

    public static MllpResult nack(String ackCode, String messageControlId, String ackText, String rawAck, long durationMs) {
        return new MllpResult(false, ackCode, messageControlId, ackText, rawAck, "NACK received: " + ackCode + (ackText != null ? " - " + ackText : ""), durationMs);
    }

    public static MllpResult failure(String errorMessage, long durationMs) {
        return new MllpResult(false, null, null, null, null, errorMessage, durationMs);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAckCode() {
        return ackCode;
    }

    public String getMessageControlId() {
        return messageControlId;
    }

    public String getAckText() {
        return ackText;
    }

    public String getRawAck() {
        return rawAck;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public long getDurationMs() {
        return durationMs;
    }

    @Override
    public String toString() {
        return "MllpResult{" +
                "success=" + success +
                ", ackCode='" + ackCode + '\'' +
                ", messageControlId='" + messageControlId + '\'' +
                ", ackText='" + ackText + '\'' +
                ", durationMs=" + durationMs +
                (errorMessage != null ? ", errorMessage='" + errorMessage + '\'' : "") +
                '}';
    }
}
