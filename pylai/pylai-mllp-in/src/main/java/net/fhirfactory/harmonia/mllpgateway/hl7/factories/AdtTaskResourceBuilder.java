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
import net.fhirfactory.harmonia.mllpgateway.hl7.AdtMessageExtractor;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.Date;

/**
 * Builds FHIR {@link Task} resources representing the ingested ADT event.
 * The generated Task has the {@link Communication} resource encapsulating the incoming
 * HL7 v2 message as its {@code Task.input}.
 */
@ApplicationScoped
public class AdtTaskResourceBuilder {

    private final AdtMessageExtractor extractor;

    public AdtTaskResourceBuilder() {
        this(new AdtMessageExtractor());
    }

    @Inject
    public AdtTaskResourceBuilder(AdtMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new AdtMessageExtractor();
    }

    public Task buildTask(Terser terser, Topic topic, String messageControlId,
                          String patientId, String patientFullName, String visitNumber, String patientClass,
                          String assignedLocationPointOfCare, String assignedLocationRoom, String assignedLocationBed,
                          String attendingDocId, String attendingDocName, String sendingApp, String sendingFacility,
                          String messageTimestamp, Communication communication) {
        String messageType = topic != null ? topic.getDataElement() : "ADT";
        String triggerEvent = topic != null ? topic.getDataElementQualifier() : "A01";
        return buildTask(terser, topic, messageType, triggerEvent, messageControlId, patientId, patientFullName,
                visitNumber, patientClass, assignedLocationPointOfCare, assignedLocationRoom, assignedLocationBed,
                attendingDocId, attendingDocName, sendingApp, sendingFacility, messageTimestamp, communication);
    }

    public Task buildTask(Terser terser, String messageType, String triggerEvent, String messageControlId,
                          String patientId, String patientFullName, String visitNumber, String patientClass,
                          String assignedLocationPointOfCare, String assignedLocationRoom, String assignedLocationBed,
                          String attendingDocId, String attendingDocName, String sendingApp, String sendingFacility,
                          String messageTimestamp, Communication communication) {
        return buildTask(terser, null, messageType, triggerEvent, messageControlId, patientId, patientFullName,
                visitNumber, patientClass, assignedLocationPointOfCare, assignedLocationRoom, assignedLocationBed,
                attendingDocId, attendingDocName, sendingApp, sendingFacility, messageTimestamp, communication);
    }

    public Task buildTask(Terser terser, Topic topic, String messageType, String triggerEvent, String messageControlId,
                          String patientId, String patientFullName, String visitNumber, String patientClass,
                          String assignedLocationPointOfCare, String assignedLocationRoom, String assignedLocationBed,
                          String attendingDocId, String attendingDocName, String sendingApp, String sendingFacility,
                          String messageTimestamp, Communication communication) {
        Task task = new Task();
        String cleanCtrlId = extractor.cleanId(messageControlId);
        task.setId("Task/" + cleanCtrlId);

        // Task Identifier
        Identifier msgId = task.addIdentifier();
        msgId.setSystem("urn:ietf:rfc:3986");
        msgId.setValue("urn:hl7:message-control-id:" + messageControlId);

        Identifier adtId = task.addIdentifier();
        adtId.setSystem("http://terminology.hl7.org/CodeSystem/v2-0003");
        adtId.setValue((messageType != null ? messageType : "ADT") + "^" + (triggerEvent != null ? triggerEvent : "A01"));

        if (topic != null) {
            Identifier topicId = task.addIdentifier();
            topicId.setSystem("http://example.org/hie/topic");
            topicId.setValue(topic.toTopicString());
            topicId.setType(new CodeableConcept().setText("HIE Topic"));
        }

        if (StringUtils.isNotBlank(visitNumber)) {
            Identifier visitId = task.addIdentifier();
            visitId.setSystem("http://example.org/visit-number");
            visitId.setValue(visitNumber);
        }

        // Link Task to source Communication resource if available
        if (communication != null) {
            String commId = communication.getId();
            if (StringUtils.isNotBlank(commId)) {
                task.addBasedOn(new Reference(commId));
            }
        }

        // Task Status and Priority based on ADT event
        Task.TaskStatus status = extractor.determineTaskStatus(triggerEvent);
        task.setStatus(status);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setPriority(extractor.determinePriority(patientClass));

        // Task Reason (HIE-Synthetic-Task)
        ErgonReasonEnum.HIE_SYNTHETIC_TASK.applyTo(task);

        // Task Code / Category
        CodeableConcept taskCode = task.getCode();
        String eventDesc = extractor.getTriggerEventDescription(triggerEvent);
        taskCode.setText(eventDesc);
        Coding coding = taskCode.addCoding();
        coding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0003");
        coding.setCode(triggerEvent != null ? triggerEvent : "A01");
        coding.setDisplay(eventDesc);

        if (topic != null) {
            Coding topicCoding = taskCode.addCoding();
            topicCoding.setSystem("http://example.org/hie/topic");
            topicCoding.setCode(topic.toTopicString());
            topicCoding.setDisplay("Topic: " + topic.toTopicString());
        }

        // Description
        StringBuilder desc = new StringBuilder();
        desc.append(messageType != null ? messageType : "ADT")
                .append("^")
                .append(triggerEvent != null ? triggerEvent : "A01")
                .append(" (").append(eventDesc).append(")");
        if (StringUtils.isNotBlank(patientFullName)) {
            desc.append(" for Patient ").append(patientFullName);
            if (StringUtils.isNotBlank(patientId)) {
                desc.append(" (ID: ").append(patientId).append(")");
            }
        }
        if (StringUtils.isNotBlank(visitNumber)) {
            desc.append(", Visit: ").append(visitNumber);
        }
        task.setDescription(desc.toString());

        // Patient Reference (for)
        if (StringUtils.isNotBlank(patientId) && !"UNKNOWN".equalsIgnoreCase(patientId)) {
            Reference patientRef = new Reference("Patient/" + extractor.cleanId(patientId));
            if (StringUtils.isNotBlank(patientFullName)) {
                patientRef.setDisplay(patientFullName);
            }
            task.setFor(patientRef);
        }

        // Encounter Reference (focus)
        if (StringUtils.isNotBlank(visitNumber)) {
            Reference encounterRef = new Reference("Encounter/" + extractor.cleanId(visitNumber));
            encounterRef.setDisplay("Encounter " + visitNumber + (patientClass != null ? " (" + patientClass + ")" : ""));
            task.setFocus(encounterRef);
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

        // Owner (Attending Physician or Gateway)
        if (StringUtils.isNotBlank(attendingDocId)) {
            Reference owner = new Reference("Practitioner/" + extractor.cleanId(attendingDocId));
            if (StringUtils.isNotBlank(attendingDocName)) {
                owner.setDisplay(attendingDocName);
            }
            task.setOwner(owner);
        }

        // Clinical/Operational Notes
        StringBuilder noteBuilder = new StringBuilder();
        noteBuilder.append("HL7 v2.4 Trigger Event: ").append(messageType != null ? messageType : "ADT").append("^").append(triggerEvent != null ? triggerEvent : "A01");
        if (StringUtils.isNotBlank(sendingApp) || StringUtils.isNotBlank(sendingFacility)) {
            noteBuilder.append(" from ").append(extractor.defaultIfBlank(sendingApp, "")).append("@").append(extractor.defaultIfBlank(sendingFacility, ""));
        }
        if (StringUtils.isNotBlank(assignedLocationPointOfCare)) {
            noteBuilder.append(" | Location: ").append(assignedLocationPointOfCare);
            if (StringUtils.isNotBlank(assignedLocationRoom)) noteBuilder.append(" Rm:").append(assignedLocationRoom);
            if (StringUtils.isNotBlank(assignedLocationBed)) noteBuilder.append(" Bed:").append(assignedLocationBed);
        }
        if (StringUtils.isNotBlank(attendingDocName)) {
            noteBuilder.append(" | Attending: ").append(attendingDocName);
            if (StringUtils.isNotBlank(attendingDocId)) noteBuilder.append(" (").append(attendingDocId).append(")");
        }
        Annotation note = task.addNote();
        note.setText(noteBuilder.toString());
        note.setTime(new Date());

        // Location / Location Reference in input
        if (StringUtils.isNotBlank(assignedLocationPointOfCare)) {
            Task.TaskInputComponent locInput = task.addInput();
            locInput.getType().setText("Assigned Location");
            StringBuilder locDisp = new StringBuilder(assignedLocationPointOfCare);
            if (StringUtils.isNotBlank(assignedLocationRoom)) locDisp.append(" Room ").append(assignedLocationRoom);
            if (StringUtils.isNotBlank(assignedLocationBed)) locDisp.append(" Bed ").append(assignedLocationBed);
            locInput.setValue(new StringType(locDisp.toString()));
        }

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
