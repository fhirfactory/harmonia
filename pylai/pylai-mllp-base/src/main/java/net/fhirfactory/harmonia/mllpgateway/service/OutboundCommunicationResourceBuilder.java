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

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * Builds and updates FHIR {@link Communication} resources representing outbound MLLP transmissions.
 */
@ApplicationScoped
public class OutboundCommunicationResourceBuilder {

    public Communication buildInitialCommunication(OutboundMllpRequest request, String destinationFacility) {
        Communication communication = new Communication();
        String controlId = request != null ? request.getMessageControlId() : null;
        String cleanId = StringUtils.isNotBlank(controlId) ? cleanId(controlId) : UUID.randomUUID().toString();
        communication.setId("Communication/comm-out-" + cleanId);
        communication.setStatus(Enumerations.EventStatus.INPROGRESS);
        communication.setPriority(Enumerations.RequestPriority.ROUTINE);

        Topic topic = request != null ? request.getTopic() : null;
        String triggerEvent = topic != null ? topic.getDataElementQualifier() : "A01";

        // Category
        CodeableConcept category = communication.addCategory();
        category.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/communication-category", "notification", "Notification"));
        category.addCoding(new Coding("http://example.org/hl7/transmission-direction", "outbound", "Outbound Transmission"));
        if (topic != null) {
            category.addCoding(new Coding("http://example.org/hie/topic", topic.toTopicString(), "HIE Topic: " + topic.toTopicString()));
        }
        category.setText("HL7 Outbound Egress Transmission");

        // Identifiers
        if (StringUtils.isNotBlank(controlId)) {
            Identifier msgId = communication.addIdentifier();
            msgId.setSystem("http://example.org/hl7/message-control-id");
            msgId.setValue(controlId);
            msgId.setType(new CodeableConcept().setText("HL7 Message Control ID"));
        }
        if (request != null && StringUtils.isNotBlank(request.getDestinationId())) {
            Identifier destId = communication.addIdentifier();
            destId.setSystem("http://example.org/hie/destination-id");
            destId.setValue(request.getDestinationId());
            destId.setType(new CodeableConcept().setText("Target Destination ID"));
        }
        if (topic != null) {
            Identifier topicId = communication.addIdentifier();
            topicId.setSystem("http://example.org/hie/topic");
            topicId.setValue(topic.toTopicString());
            topicId.setType(new CodeableConcept().setText("HIE Topic"));
        }

        communication.setSent(request != null && request.getTimestamp() != null ? request.getTimestamp() : new Date());

        // Sender
        Reference senderRef = new Reference();
        senderRef.setType("Device");
        senderRef.setDisplay("Harmonia MLLP Outbound Gateway");
        communication.setSender(senderRef);

        // Recipient
        Reference recipientRef = new Reference();
        recipientRef.setType("Device");
        recipientRef.setDisplay(StringUtils.isNotBlank(destinationFacility) ? destinationFacility :
                (request != null && request.getDestinationId() != null ? request.getDestinationId() : "External HL7 Receiver"));
        communication.addRecipient(recipientRef);

        // Note
        Annotation note = communication.addNote();
        note.setTime(new Date());
        note.setText("Initiated outbound HL7 message dispatch via MLLP" + (topic != null ? " [" + topic.toTopicString() + "]" : ""));

        // Payload containing outgoing raw HL7 message
        if (request != null && request.getRawMessage() != null) {
            Communication.CommunicationPayloadComponent payload = communication.addPayload();
            Attachment attachment = new Attachment();
            attachment.setContentType("application/hl7-v2");
            attachment.setData(request.getRawMessage().getBytes(StandardCharsets.UTF_8));
            attachment.setTitle("Outbound HL7 Message Payload");
            attachment.setCreation(new Date());
            payload.setContent(attachment);
        }

        FhirSecurityTagManager.applyDefaultSecurityTag(communication);
        return communication;
    }

    public Communication updateCommunicationWithResponse(Communication communication, OutboundMllpResponse response) {
        if (communication == null || response == null) {
            return communication;
        }

        if (response.isSuccessful()) {
            communication.setStatus(Enumerations.EventStatus.COMPLETED);
        } else {
            communication.setStatus(Enumerations.EventStatus.NOTDONE);
        }

        communication.setReceived(response.getAcknowledgedAt() != null ? response.getAcknowledgedAt() : new Date());

        // Add ACK summary note
        Annotation note = communication.addNote();
        note.setTime(new Date());
        if (response.isSuccessful()) {
            note.setText("Outbound MLLP dispatch acknowledged successfully (" + response.getAckCode() + ") in " + response.getDurationMs() + "ms");
        } else {
            note.setText("Outbound MLLP dispatch failed or rejected: " + response.getErrorMessage() +
                    (response.getAckCode() != null ? " (" + response.getAckCode() + ")" : ""));
        }

        // Attach raw ACK payload if present
        if (StringUtils.isNotBlank(response.getRawAckMessage())) {
            Communication.CommunicationPayloadComponent ackPayload = communication.addPayload();
            Attachment ackAttachment = new Attachment();
            ackAttachment.setContentType("application/hl7-v2");
            ackAttachment.setData(response.getRawAckMessage().getBytes(StandardCharsets.UTF_8));
            ackAttachment.setTitle("Returned HL7 ACK Message");
            ackAttachment.setCreation(new Date());
            ackPayload.setContent(ackAttachment);
        }

        FhirSecurityTagManager.applyDefaultSecurityTag(communication);
        return communication;
    }

    private String cleanId(String rawId) {
        return rawId.replaceAll("[^a-zA-Z0-9._-]", "-").replaceAll("-+", "-");
    }
}
