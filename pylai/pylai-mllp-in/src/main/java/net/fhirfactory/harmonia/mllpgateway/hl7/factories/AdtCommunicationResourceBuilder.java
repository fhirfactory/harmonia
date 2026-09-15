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
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * Builds FHIR {@link Communication} resources containing the raw HL7 message payload and metadata.
 */
@ApplicationScoped
public class AdtCommunicationResourceBuilder {

    private final AdtMessageExtractor extractor;

    public AdtCommunicationResourceBuilder() {
        this(new AdtMessageExtractor());
    }

    @Inject
    public AdtCommunicationResourceBuilder(AdtMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new AdtMessageExtractor();
    }

    public Communication buildCommunication(Terser terser, Topic topic, String rawMessageString, String messageControlId,
                                            String patientId, String patientFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        String triggerEvent = topic != null ? topic.getDataElementQualifier() : "A01";
        return buildCommunication(terser, topic, rawMessageString, messageControlId, triggerEvent, patientId,
                patientFullName, sendingApp, sendingFacility, messageTimestamp);
    }

    public Communication buildCommunication(Terser terser, String rawMessageString, String messageControlId,
                                            String triggerEvent, String patientId, String patientFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        return buildCommunication(terser, null, rawMessageString, messageControlId, triggerEvent, patientId,
                patientFullName, sendingApp, sendingFacility, messageTimestamp);
    }

    public Communication buildCommunication(Terser terser, Topic topic, String rawMessageString, String messageControlId,
                                            String triggerEvent, String patientId, String patientFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        Communication communication = new Communication();
        String commId = "comm-" + (StringUtils.isNotBlank(messageControlId) ? messageControlId : UUID.randomUUID().toString());
        communication.setId("Communication/" + commId);
        communication.setStatus(Enumerations.EventStatus.COMPLETED);
        communication.setPriority(Enumerations.RequestPriority.ROUTINE);

        // Communication Category
        CodeableConcept category = communication.addCategory();
        category.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/communication-category", "notification", "Notification"));
        category.addCoding(new Coding("http://example.org/hl7/trigger-event", triggerEvent, "HL7 ADT " + triggerEvent));
        if (topic != null) {
            category.addCoding(new Coding("http://example.org/hie/topic", topic.toTopicString(), "HIE Topic: " + topic.toTopicString()));
        }
        category.setText("HL7 v2.4 ADT Ingestion Notification");

        // Identifiers
        if (StringUtils.isNotBlank(messageControlId)) {
            Identifier msgId = communication.addIdentifier();
            msgId.setSystem("http://example.org/hl7/message-control-id");
            msgId.setValue(messageControlId);
            msgId.setType(new CodeableConcept().setText("HL7 Message Control ID"));
        }
        if (StringUtils.isNotBlank(triggerEvent)) {
            Identifier eventId = communication.addIdentifier();
            eventId.setSystem("http://example.org/hl7/trigger-event");
            eventId.setValue(triggerEvent);
            eventId.setType(new CodeableConcept().setText("HL7 Trigger Event"));
        }
        if (topic != null) {
            Identifier topicId = communication.addIdentifier();
            topicId.setSystem("http://example.org/hie/topic");
            topicId.setValue(topic.toTopicString());
            topicId.setType(new CodeableConcept().setText("HIE Topic"));
        }
        if (StringUtils.isNotBlank(sendingFacility)) {
            Identifier facilityId = communication.addIdentifier();
            facilityId.setSystem("http://example.org/hl7/sending-facility");
            facilityId.setValue(sendingFacility);
            facilityId.setType(new CodeableConcept().setText("Sending Facility"));
        }

        // Subject (Patient Reference)
        if (StringUtils.isNotBlank(patientId) && !"UNKNOWN".equalsIgnoreCase(patientId)) {
            Reference subjectRef = new Reference("Patient/" + patientId);
            if (StringUtils.isNotBlank(patientFullName)) {
                subjectRef.setDisplay(patientFullName);
            }
            communication.setSubject(subjectRef);
        }

        // Timestamps (Sent from MSH-7, Received now)
        if (StringUtils.isNotBlank(messageTimestamp)) {
            Date sentDate = extractor.parseHl7Date(messageTimestamp);
            if (sentDate != null) {
                communication.setSent(sentDate);
            }
        }
        communication.setReceived(new Date());

        // Sender
        Reference senderRef = new Reference();
        senderRef.setType("Device");
        if (StringUtils.isNotBlank(sendingApp) || StringUtils.isNotBlank(sendingFacility)) {
            StringBuilder senderDisp = new StringBuilder();
            if (StringUtils.isNotBlank(sendingApp)) {
                senderDisp.append(sendingApp);
            }
            if (StringUtils.isNotBlank(sendingFacility)) {
                if (senderDisp.length() > 0) senderDisp.append(" @ ");
                senderDisp.append(sendingFacility);
            }
            senderRef.setDisplay(senderDisp.toString());
        } else {
            senderRef.setDisplay("HL7 MLLP Client");
        }
        communication.setSender(senderRef);

        // Recipient
        Reference recipientRef = new Reference();
        recipientRef.setType("Device");
        recipientRef.setDisplay("HIE MLLP Gateway");
        communication.addRecipient(recipientRef);

        // Note
        Annotation note = communication.addNote();
        note.setTime(new Date());
        note.setText("Raw HL7 v2.4 " + triggerEvent + " ADT message received via MLLP Gateway" + (topic != null ? " [" + topic.toTopicString() + "]" : ""));

        // Payload containing the raw ADT message
        if (rawMessageString != null) {
            Communication.CommunicationPayloadComponent payload = communication.addPayload();
            Attachment attachment = new Attachment();
            attachment.setContentType("application/hl7-v2");
            attachment.setData(rawMessageString.getBytes(StandardCharsets.UTF_8));
            attachment.setTitle("Raw HL7 v2.4 ADT Message");
            attachment.setCreation(new Date());
            payload.setContent(attachment);
        }

        return communication;
    }
}
