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

package net.fhirfactory.harmonia.mllpgateway.hl7;

import net.fhirfactory.harmonia.model.topic.Topic;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.Provenance;
import org.hl7.fhir.r5.model.Task;

public class OrmProcessingResult {

    private final String messageControlId;
    private final String triggerEvent;
    private final Topic topic;
    private final String placerOrderNumber;
    private final String universalServiceId;
    private final String patientId;
    private final Communication communication;
    private final Task task;
    private final Provenance provenance;
    private final String ackMessage;
    private final boolean success;
    private final String errorMessage;

    public OrmProcessingResult(String messageControlId, String triggerEvent, Topic topic,
                               String placerOrderNumber, String universalServiceId, String patientId,
                               Communication communication, Task task, Provenance provenance,
                               String ackMessage, boolean success, String errorMessage) {
        this.messageControlId = messageControlId;
        this.triggerEvent = triggerEvent;
        this.topic = topic;
        this.placerOrderNumber = placerOrderNumber;
        this.universalServiceId = universalServiceId;
        this.patientId = patientId;
        this.communication = communication;
        this.task = task;
        this.provenance = provenance;
        this.ackMessage = ackMessage;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static OrmProcessingResult success(String messageControlId, String triggerEvent, Topic topic,
                                              String placerOrderNumber, String universalServiceId, String patientId,
                                              Communication communication, Task task, Provenance provenance, String ackMessage) {
        return new OrmProcessingResult(messageControlId, triggerEvent, topic, placerOrderNumber, universalServiceId,
                patientId, communication, task, provenance, ackMessage, true, null);
    }

    public static OrmProcessingResult failure(String messageControlId, String triggerEvent, String ackMessage, String errorMessage) {
        return new OrmProcessingResult(messageControlId, triggerEvent, null, null, null,
                null, null, null, null, ackMessage, false, errorMessage);
    }

    public String getMessageControlId() {
        return messageControlId;
    }

    public String getTriggerEvent() {
        return triggerEvent;
    }

    public Topic getTopic() {
        return topic;
    }

    public String getPlacerOrderNumber() {
        return placerOrderNumber;
    }

    public String getUniversalServiceId() {
        return universalServiceId;
    }

    public String getPatientId() {
        return patientId;
    }

    public Communication getCommunication() {
        return communication;
    }

    public Task getTask() {
        return task;
    }

    public Provenance getProvenance() {
        return provenance;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
