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

package net.fhirfactory.harmonia.paradeigma.common.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Response payload returned after manually triggering a clinical event on a simulator.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManualTriggerResponse {

    private boolean success;
    private String messageControlId;
    private String triggerEvent;
    private String patientId;
    private String visitNumber;
    private String orderNumber;
    private String ackCode;
    private String ackMessage;
    private long durationMs;
    private String error;

    public ManualTriggerResponse() {
    }

    public static ManualTriggerResponse success(String messageControlId, String triggerEvent, String patientId,
                                                String ackCode, String ackMessage, long durationMs) {
        ManualTriggerResponse r = new ManualTriggerResponse();
        r.setSuccess(true);
        r.setMessageControlId(messageControlId);
        r.setTriggerEvent(triggerEvent);
        r.setPatientId(patientId);
        r.setAckCode(ackCode);
        r.setAckMessage(ackMessage);
        r.setDurationMs(durationMs);
        return r;
    }

    public static ManualTriggerResponse failure(String triggerEvent, String error) {
        ManualTriggerResponse r = new ManualTriggerResponse();
        r.setSuccess(false);
        r.setTriggerEvent(triggerEvent);
        r.setError(error);
        return r;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessageControlId() {
        return messageControlId;
    }

    public void setMessageControlId(String messageControlId) {
        this.messageControlId = messageControlId;
    }

    public String getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(String triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getAckCode() {
        return ackCode;
    }

    public void setAckCode(String ackCode) {
        this.ackCode = ackCode;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
