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

package net.fhirfactory.harmonia.befe.model.operations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Cross-subsystem operational interaction event.
 * Used for diagnostic correlation tracing across gateways, message transport, workflows, and persistence.
 * Zero-PHI: Clinical resource contents and patient identifiable attributes are strictly omitted.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalEvent implements Serializable {

    private String eventId;
    private long timestamp;
    private String subsystem;
    private String eventType;
    private String operation;
    private String status;               // SUCCESS, WARNING, FAILURE
    private long durationMs;
    private String messageId;
    private String correlationId;
    private String causationId;
    private String pragmaId;
    private String praxisId;
    private String ergonId;
    private String interfaceId;
    private String reasonCode;

    public OperationalEvent() {
    }

    public OperationalEvent(String eventId, long timestamp, String subsystem, String eventType,
                            String operation, String status, long durationMs, String messageId,
                            String correlationId, String causationId, String pragmaId,
                            String praxisId, String ergonId, String interfaceId, String reasonCode) {
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.subsystem = subsystem;
        this.eventType = eventType;
        this.operation = operation;
        this.status = status;
        this.durationMs = durationMs;
        this.messageId = messageId;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.pragmaId = pragmaId;
        this.praxisId = praxisId;
        this.ergonId = ergonId;
        this.interfaceId = interfaceId;
        this.reasonCode = reasonCode;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCausationId() {
        return causationId;
    }

    public void setCausationId(String causationId) {
        this.causationId = causationId;
    }

    public String getPragmaId() {
        return pragmaId;
    }

    public void setPragmaId(String pragmaId) {
        this.pragmaId = pragmaId;
    }

    public String getPraxisId() {
        return praxisId;
    }

    public void setPraxisId(String praxisId) {
        this.praxisId = praxisId;
    }

    public String getErgonId() {
        return ergonId;
    }

    public void setErgonId(String ergonId) {
        this.ergonId = ergonId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
}
