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

package net.fhirfactory.harmonia.mllpgateway.hl7.factories;

import ca.uhn.hl7v2.util.Terser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.mllpgateway.hl7.MfnMessageExtractor;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.Date;

/**
 * Builds FHIR {@link Task} resources representing the ingested MFN^M02 event.
 * The generated Task has the {@link Communication} resource encapsulating the incoming
 * HL7 v2 message as its {@code Task.input}.
 */
@ApplicationScoped
public class MfnTaskResourceBuilder {

    private final MfnMessageExtractor extractor;

    public MfnTaskResourceBuilder() {
        this(new MfnMessageExtractor());
    }

    @Inject
    public MfnTaskResourceBuilder(MfnMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new MfnMessageExtractor();
    }

    public Task buildTask(Terser terser, Topic topic, String messageControlId,
                          String practitionerId, String practitionerFullName,
                          String sendingApp, String sendingFacility,
                          String messageTimestamp, Communication communication) {
        String messageType = topic != null ? topic.getDataElement() : "MFN";
        String triggerEvent = topic != null ? topic.getDataElementQualifier() : "M02";
        return buildTask(terser, topic, messageType, triggerEvent, messageControlId, practitionerId, practitionerFullName,
                sendingApp, sendingFacility, messageTimestamp, communication);
    }

    public Task buildTask(Terser terser, String messageType, String triggerEvent, String messageControlId,
                          String practitionerId, String practitionerFullName,
                          String sendingApp, String sendingFacility,
                          String messageTimestamp, Communication communication) {
        return buildTask(terser, null, messageType, triggerEvent, messageControlId, practitionerId, practitionerFullName,
                sendingApp, sendingFacility, messageTimestamp, communication);
    }

    public Task buildTask(Terser terser, Topic topic, String messageType, String triggerEvent, String messageControlId,
                          String practitionerId, String practitionerFullName,
                          String sendingApp, String sendingFacility,
                          String messageTimestamp, Communication communication) {
        Task task = new Task();
        String cleanCtrlId = extractor.cleanId(messageControlId);
        task.setId("Task/" + cleanCtrlId);

        // Task Identifiers
        Identifier msgId = task.addIdentifier();
        msgId.setSystem("urn:ietf:rfc:3986");
        msgId.setValue("urn:hl7:message-control-id:" + messageControlId);

        Identifier mfnId = task.addIdentifier();
        mfnId.setSystem("http://terminology.hl7.org/CodeSystem/v2-0003");
        mfnId.setValue((messageType != null ? messageType : "MFN") + "^" + (triggerEvent != null ? triggerEvent : "M02"));

        if (topic != null) {
            Identifier topicId = task.addIdentifier();
            topicId.setSystem("http://example.org/hie/topic");
            topicId.setValue(topic.toTopicString());
            topicId.setType(new CodeableConcept().setText("HIE Topic"));
        }

        if (StringUtils.isNotBlank(practitionerId) && !"UNKNOWN".equalsIgnoreCase(practitionerId)) {
            Identifier practIdentifier = task.addIdentifier();
            practIdentifier.setSystem("http://example.org/practitioner-id");
            practIdentifier.setValue(practitionerId);
        }

        // Link Task to source Communication resource if available
        if (communication != null) {
            String commId = communication.getId();
            if (StringUtils.isNotBlank(commId)) {
                task.addBasedOn(new Reference(commId));
            }
        }

        // Task Status & Priority
        String rawMessage = null;
        if (communication != null && communication.hasPayload()) {
            for (Communication.CommunicationPayloadComponent payload : communication.getPayload()) {
                if (payload.hasContentAttachment() && payload.getContentAttachment().hasData()) {
                    rawMessage = new String(payload.getContentAttachment().getData(), java.nio.charset.StandardCharsets.UTF_8);
                    break;
                }
            }
        }
        String actionCode = extractor.extractMfeActionCode(rawMessage, terser);
        Task.TaskStatus status = extractor.determineTaskStatus(actionCode);
        task.setStatus(status);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setPriority(Enumerations.RequestPriority.ROUTINE);

        // Task Reason (HIE-Synthetic-Task)
        ErgonReasonEnum.HIE_SYNTHETIC_TASK.applyTo(task);

        // Task Code / Category
        CodeableConcept taskCode = task.getCode();
        String eventDesc = extractor.getTriggerEventDescription(triggerEvent);
        taskCode.setText(eventDesc);
        Coding coding = taskCode.addCoding();
        coding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0003");
        coding.setCode(triggerEvent != null ? triggerEvent : "M02");
        coding.setDisplay(eventDesc);

        if (topic != null) {
            Coding topicCoding = taskCode.addCoding();
            topicCoding.setSystem("http://example.org/hie/topic");
            topicCoding.setCode(topic.toTopicString());
            topicCoding.setDisplay("Topic: " + topic.toTopicString());
        }

        // Description
        StringBuilder desc = new StringBuilder();
        desc.append(messageType != null ? messageType : "MFN")
                .append("^")
                .append(triggerEvent != null ? triggerEvent : "M02")
                .append(" (").append(eventDesc).append(")")
                .append(" for Practitioner ").append(practitionerFullName)
                .append(" (ID: ").append(practitionerId).append(")");
        task.setDescription(desc.toString());

        // Practitioner Reference (for & focus)
        if (StringUtils.isNotBlank(practitionerId) && !"UNKNOWN".equalsIgnoreCase(practitionerId)) {
            Reference practRef = new Reference("Practitioner/" + extractor.cleanId(practitionerId));
            practRef.setDisplay(practitionerFullName);
            task.setFor(practRef);
            task.setFocus(practRef);
        }

        // Timestamps
        Date eventDate = extractor.parseHl7Date(messageTimestamp);
        if (eventDate != null) {
            task.setAuthoredOn(eventDate);
            task.setLastModified(new Date());
        } else {
            task.setAuthoredOn(new Date());
            task.setLastModified(new Date());
        }

        // Requester (Sending App / Facility)
        if (StringUtils.isNotBlank(sendingApp) || StringUtils.isNotBlank(sendingFacility)) {
            Reference requester = new Reference();
            requester.setType("Device");
            StringBuilder reqDisp = new StringBuilder();
            if (StringUtils.isNotBlank(sendingApp)) reqDisp.append(sendingApp);
            if (StringUtils.isNotBlank(sendingFacility)) {
                if (reqDisp.length() > 0) reqDisp.append(" @ ");
                reqDisp.append(sendingFacility);
            }
            requester.setDisplay(reqDisp.toString());
            task.setRequester(requester);
        }

        // Owner (Practitioner or Gateway)
        if (StringUtils.isNotBlank(practitionerId) && !"UNKNOWN".equalsIgnoreCase(practitionerId)) {
            Reference owner = new Reference("Practitioner/" + extractor.cleanId(practitionerId));
            owner.setDisplay(practitionerFullName);
            task.setOwner(owner);
        }

        // Clinical/Operational Notes
        StringBuilder noteBuilder = new StringBuilder();
        noteBuilder.append("HL7 v2.4 Trigger Event: ")
                .append(messageType != null ? messageType : "MFN")
                .append("^")
                .append(triggerEvent != null ? triggerEvent : "M02");
        if (StringUtils.isNotBlank(sendingApp) || StringUtils.isNotBlank(sendingFacility)) {
            noteBuilder.append(" from ").append(extractor.defaultIfBlank(sendingApp, "")).append("@").append(extractor.defaultIfBlank(sendingFacility, ""));
        }
        if (StringUtils.isNotBlank(practitionerFullName)) {
            noteBuilder.append(" | Practitioner: ").append(practitionerFullName);
            if (StringUtils.isNotBlank(practitionerId)) noteBuilder.append(" (").append(practitionerId).append(")");
        }
        Annotation note = task.addNote();
        note.setText(noteBuilder.toString());
        note.setTime(new Date());

        // Communication resource as Task.input
        if (communication != null) {
            task.addContained(communication);

            Task.TaskInputComponent input = task.addInput();
            input.getType().setText("HL7 Communication Resource").addCoding()
                    .setSystem("http://terminology.hl7.org/CodeSystem/task-input-type")
                    .setCode("input-communication")
                    .setDisplay("HL7 Communication Resource");
            String commRef = communication.getIdPart() != null ? "#" + communication.getIdPart() : "#" + communication.getId();
            input.setValue(new Reference(commRef));
        }

        return task;
    }
}
