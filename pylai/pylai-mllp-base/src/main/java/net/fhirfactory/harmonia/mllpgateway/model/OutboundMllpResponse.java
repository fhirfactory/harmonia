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

package net.fhirfactory.harmonia.mllpgateway.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Data transfer model encapsulating an outbound MLLP transmission result and acknowledgement.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutboundMllpResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("messageControlId")
    private String messageControlId;

    @JsonProperty("destinationId")
    private String destinationId;

    @JsonProperty("ackCode")
    private String ackCode;

    @JsonProperty("ackText")
    private String ackText;

    @JsonProperty("rawAckMessage")
    private String rawAckMessage;

    @JsonProperty("successful")
    private boolean successful;

    @JsonProperty("durationMs")
    private long durationMs;

    @JsonProperty("dispatchedAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dispatchedAt;

    @JsonProperty("acknowledgedAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date acknowledgedAt;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("taskId")
    private String taskId;

    @JsonProperty("communicationId")
    private String communicationId;

    @JsonProperty("provenanceId")
    private String provenanceId;

    public OutboundMllpResponse() {
        this.acknowledgedAt = new Date();
    }

    public static OutboundMllpResponse success(String messageControlId, String ackCode, String rawAckMessage, long durationMs) {
        OutboundMllpResponse response = new OutboundMllpResponse();
        response.setMessageControlId(messageControlId);
        response.setAckCode(ackCode != null ? ackCode : "AA");
        response.setRawAckMessage(rawAckMessage);
        response.setSuccessful(true);
        response.setDurationMs(durationMs);
        response.setAcknowledgedAt(new Date());
        return response;
    }

    public static OutboundMllpResponse success(String requestId, String messageControlId, String destinationId,
                                              String ackCode, String rawAckMessage, long durationMs) {
        OutboundMllpResponse response = success(messageControlId, ackCode, rawAckMessage, durationMs);
        response.setRequestId(requestId);
        response.setDestinationId(destinationId);
        return response;
    }

    public static OutboundMllpResponse nack(String messageControlId, String ackCode, String ackText,
                                           String rawAckMessage, long durationMs) {
        OutboundMllpResponse response = new OutboundMllpResponse();
        response.setMessageControlId(messageControlId);
        response.setAckCode(ackCode != null ? ackCode : "AE");
        response.setAckText(ackText);
        response.setRawAckMessage(rawAckMessage);
        response.setSuccessful(false);
        response.setDurationMs(durationMs);
        response.setErrorMessage(ackText != null ? ackText : "HL7 Application NACK/Reject: " + ackCode);
        response.setAcknowledgedAt(new Date());
        return response;
    }

    public static OutboundMllpResponse failure(String messageControlId, String errorMessage, long durationMs) {
        OutboundMllpResponse response = new OutboundMllpResponse();
        response.setMessageControlId(messageControlId);
        response.setSuccessful(false);
        response.setErrorMessage(errorMessage);
        response.setDurationMs(durationMs);
        response.setAcknowledgedAt(new Date());
        return response;
    }

    public static OutboundMllpResponse failure(String requestId, String messageControlId, String destinationId,
                                              String errorMessage, long durationMs) {
        OutboundMllpResponse response = failure(messageControlId, errorMessage, durationMs);
        response.setRequestId(requestId);
        response.setDestinationId(destinationId);
        return response;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessageControlId() {
        return messageControlId;
    }

    public void setMessageControlId(String messageControlId) {
        this.messageControlId = messageControlId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getAckCode() {
        return ackCode;
    }

    public void setAckCode(String ackCode) {
        this.ackCode = ackCode;
    }

    public String getAckText() {
        return ackText;
    }

    public void setAckText(String ackText) {
        this.ackText = ackText;
    }

    public String getRawAckMessage() {
        return rawAckMessage;
    }

    public void setRawAckMessage(String rawAckMessage) {
        this.rawAckMessage = rawAckMessage;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public Date getDispatchedAt() {
        return dispatchedAt;
    }

    public void setDispatchedAt(Date dispatchedAt) {
        this.dispatchedAt = dispatchedAt;
    }

    public Date getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void setAcknowledgedAt(Date acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(String communicationId) {
        this.communicationId = communicationId;
    }

    public String getProvenanceId() {
        return provenanceId;
    }

    public void setProvenanceId(String provenanceId) {
        this.provenanceId = provenanceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutboundMllpResponse that = (OutboundMllpResponse) o;
        return successful == that.successful &&
                durationMs == that.durationMs &&
                Objects.equals(requestId, that.requestId) &&
                Objects.equals(messageControlId, that.messageControlId) &&
                Objects.equals(destinationId, that.destinationId) &&
                Objects.equals(ackCode, that.ackCode) &&
                Objects.equals(ackText, that.ackText) &&
                Objects.equals(rawAckMessage, that.rawAckMessage) &&
                Objects.equals(errorMessage, that.errorMessage) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(communicationId, that.communicationId) &&
                Objects.equals(provenanceId, that.provenanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, messageControlId, destinationId, ackCode, ackText,
                rawAckMessage, successful, durationMs, errorMessage, taskId, communicationId, provenanceId);
    }

    @Override
    public String toString() {
        return "OutboundMllpResponse{" +
                "requestId='" + requestId + '\'' +
                ", messageControlId='" + messageControlId + '\'' +
                ", destinationId='" + destinationId + '\'' +
                ", ackCode='" + ackCode + '\'' +
                ", successful=" + successful +
                ", durationMs=" + durationMs +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
