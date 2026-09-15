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
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * Builds FHIR {@link Communication} resources containing the raw HL7 MFN message payload and metadata.
 */
@ApplicationScoped
public class MfnCommunicationResourceBuilder {

    private final MfnMessageExtractor extractor;

    public MfnCommunicationResourceBuilder() {
        this(new MfnMessageExtractor());
    }

    @Inject
    public MfnCommunicationResourceBuilder(MfnMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new MfnMessageExtractor();
    }

    public Communication buildCommunication(Terser terser, Topic topic, String rawMessageString, String messageControlId,
                                            String practitionerId, String practitionerFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        String triggerEvent = topic != null ? topic.getDataElementQualifier() : "M02";
        return buildCommunication(terser, topic, rawMessageString, messageControlId, triggerEvent, practitionerId,
                practitionerFullName, sendingApp, sendingFacility, messageTimestamp);
    }

    public Communication buildCommunication(Terser terser, String rawMessageString, String messageControlId,
                                            String triggerEvent, String practitionerId, String practitionerFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        return buildCommunication(terser, null, rawMessageString, messageControlId, triggerEvent, practitionerId,
                practitionerFullName, sendingApp, sendingFacility, messageTimestamp);
    }

    public Communication buildCommunication(Terser terser, Topic topic, String rawMessageString, String messageControlId,
                                            String triggerEvent, String practitionerId, String practitionerFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        Communication communication = new Communication();
        String commId = "comm-" + (StringUtils.isNotBlank(messageControlId) ? messageControlId : UUID.randomUUID().toString());
        communication.setId("Communication/" + commId);
        communication.setStatus(Enumerations.EventStatus.COMPLETED);
        communication.setPriority(Enumerations.RequestPriority.ROUTINE);

        // Communication Category
        CodeableConcept category = communication.addCategory();
        category.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/communication-category", "notification", "Notification"));
        category.addCoding(new Coding("http://example.org/hl7/trigger-event", triggerEvent != null ? triggerEvent : "M02", "HL7 MFN " + (triggerEvent != null ? triggerEvent : "M02")));
        if (topic != null) {
            category.addCoding(new Coding("http://example.org/hie/topic", topic.toTopicString(), "HIE Topic: " + topic.toTopicString()));
        }
        category.setText("HL7 v2.4 MFN Ingestion Notification");

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

        // Subject (Practitioner Reference)
        if (StringUtils.isNotBlank(practitionerId) && !"UNKNOWN".equalsIgnoreCase(practitionerId)) {
            Reference subjectRef = new Reference("Practitioner/" + extractor.cleanId(practitionerId));
            if (StringUtils.isNotBlank(practitionerFullName)) {
                subjectRef.setDisplay(practitionerFullName);
            }
            communication.setSubject(subjectRef);
        }

        // Timestamps
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
        note.setText("Raw HL7 v2.4 " + (triggerEvent != null ? triggerEvent : "M02") + " MFN message received via MLLP Gateway" + (topic != null ? " [" + topic.toTopicString() + "]" : ""));

        // Payload containing raw MFN message
        if (rawMessageString != null) {
            Communication.CommunicationPayloadComponent payload = communication.addPayload();
            Attachment attachment = new Attachment();
            attachment.setContentType("application/hl7-v2");
            attachment.setData(rawMessageString.getBytes(StandardCharsets.UTF_8));
            attachment.setTitle("Raw HL7 v2.4 MFN Message");
            attachment.setCreation(new Date());
            payload.setContent(attachment);
        }

        FhirSecurityTagManager.applyDefaultSecurityTag(communication);
        return communication;
    }
}
