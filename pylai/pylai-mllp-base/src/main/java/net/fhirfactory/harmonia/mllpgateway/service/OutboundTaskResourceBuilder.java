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

package net.fhirfactory.harmonia.mllpgateway.service;

import jakarta.enterprise.context.ApplicationScoped;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.Date;
import java.util.UUID;

/**
 * Builds and updates FHIR {@link Task} (Pragma) resources representing outbound MLLP dispatch tasks.
 */
@ApplicationScoped
public class OutboundTaskResourceBuilder {

    public Task buildInitialTask(OutboundMllpRequest request, Communication communication) {
        Task task = new Task();
        String controlId = request != null ? request.getMessageControlId() : null;
        String cleanId = StringUtils.isNotBlank(controlId) ? cleanId(controlId) : UUID.randomUUID().toString();
        task.setId("Task/task-out-" + cleanId);
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setPriority(Enumerations.RequestPriority.ROUTINE);
        task.setAuthoredOn(request != null && request.getTimestamp() != null ? request.getTimestamp() : new Date());
        task.setLastModified(new Date());

        Topic topic = request != null ? request.getTopic() : null;
        String messageType = topic != null ? topic.getDataElement() : "ADT";
        String triggerEvent = topic != null ? topic.getDataElementQualifier() : "A01";

        // Identifiers
        if (StringUtils.isNotBlank(controlId)) {
            Identifier msgId = task.addIdentifier();
            msgId.setSystem("urn:ietf:rfc:3986");
            msgId.setValue("urn:hl7:message-control-id:" + controlId);
        }

        Identifier eventId = task.addIdentifier();
        eventId.setSystem("http://terminology.hl7.org/CodeSystem/v2-0003");
        eventId.setValue(messageType + "^" + triggerEvent);

        if (topic != null) {
            Identifier topicId = task.addIdentifier();
            topicId.setSystem("http://example.org/hie/topic");
            topicId.setValue(topic.toTopicString());
            topicId.setType(new CodeableConcept().setText("HIE Topic"));
        }

        if (request != null && StringUtils.isNotBlank(request.getDestinationId())) {
            Identifier destId = task.addIdentifier();
            destId.setSystem("http://example.org/hie/destination-id");
            destId.setValue(request.getDestinationId());
        }

        // Link to input Communication resource
        if (communication != null && StringUtils.isNotBlank(communication.getId())) {
            task.addBasedOn(new Reference(communication.getId()));
            task.setFocus(new Reference(communication.getId()));

            Task.TaskInputComponent inputParam = task.addInput();
            inputParam.setType(new CodeableConcept().setText("Outbound Message"));
            inputParam.setValue(new Reference(communication.getId()));
        }

        task.setDescription("Outbound MLLP dispatch task for message " + controlId + " to destination " +
                (request != null ? request.getDestinationId() : "external"));

        Annotation note = task.addNote();
        note.setTime(new Date());
        note.setText("Outbound task created and in-progress");

        FhirSecurityTagManager.applyDefaultSecurityTag(task);
        return task;
    }

    public Task updateTaskWithResponse(Task task, OutboundMllpResponse response) {
        if (task == null || response == null) {
            return task;
        }

        task.setLastModified(new Date());

        if (response.isSuccessful()) {
            task.setStatus(Task.TaskStatus.COMPLETED);
        } else {
            task.setStatus(Task.TaskStatus.FAILED);
            if (StringUtils.isNotBlank(response.getErrorMessage())) {
                task.setStatusReason(new CodeableReference(new CodeableConcept().setText(response.getErrorMessage())));
            }
        }

        Annotation note = task.addNote();
        note.setTime(new Date());
        if (response.isSuccessful()) {
            note.setText("Outbound task completed successfully. ACK: " + response.getAckCode() + " in " + response.getDurationMs() + "ms");
        } else {
            note.setText("Outbound task execution failed: " + response.getErrorMessage());
        }

        Task.TaskOutputComponent output = task.addOutput();
        output.setType(new CodeableConcept().setText("MLLP Transmission Result"));
        output.setValue(new StringType("ACK=" + response.getAckCode() + ", Success=" + response.isSuccessful()));

        FhirSecurityTagManager.applyDefaultSecurityTag(task);
        return task;
    }

    private String cleanId(String rawId) {
        return rawId.replaceAll("[^a-zA-Z0-9._-]", "-").replaceAll("-+", "-");
    }
}
