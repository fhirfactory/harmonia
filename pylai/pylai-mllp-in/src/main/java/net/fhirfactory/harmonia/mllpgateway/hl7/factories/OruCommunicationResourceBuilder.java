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
import net.fhirfactory.harmonia.mllpgateway.hl7.OruMessageExtractor;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Attachment;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Reference;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@ApplicationScoped
public class OruCommunicationResourceBuilder {

    private final OruMessageExtractor extractor;

    @Inject
    public OruCommunicationResourceBuilder(OruMessageExtractor extractor) {
        this.extractor = extractor;
    }

    public OruCommunicationResourceBuilder() {
        this(new OruMessageExtractor());
    }

    public Communication buildCommunication(String rawHl7Message, String messageControlId, String triggerEvent,
                                            Topic topic, Terser terser, String gatewayInstanceId,
                                            String sendingApp, String sendingFacility, Date messageTimestamp) {
        Communication comm = new Communication();
        comm.setId(extractor.cleanId(messageControlId != null ? messageControlId : java.util.UUID.randomUUID().toString()));
        comm.setStatus(Enumerations.EventStatus.COMPLETED);

        // Identifiers
        Identifier id = comm.addIdentifier();
        id.setSystem("http://harmonia.net/identifier/hl7-message-control-id");
        id.setValue(messageControlId);

        String fillerOrder = extractor.extractFillerOrderNumber(rawHl7Message, terser);
        if (StringUtils.isNotBlank(fillerOrder)) {
            Identifier filId = comm.addIdentifier();
            filId.setSystem("http://harmonia.net/identifier/filler-order-number");
            filId.setValue(fillerOrder);
        }

        String placerOrder = extractor.extractPlacerOrderNumber(rawHl7Message, terser);
        if (StringUtils.isNotBlank(placerOrder)) {
            Identifier ordId = comm.addIdentifier();
            ordId.setSystem("http://harmonia.net/identifier/placer-order-number");
            ordId.setValue(placerOrder);
        }

        // Category
        CodeableConcept category = comm.addCategory();
        category.addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/communication-category")
                .setCode("result")
                .setDisplay("HL7 v2 Result Message");

        // Payload
        if (rawHl7Message != null) {
            Communication.CommunicationPayloadComponent payload = comm.addPayload();
            Attachment attachment = new Attachment();
            attachment.setContentType("application/hl7-v2");
            attachment.setData(rawHl7Message.getBytes(StandardCharsets.UTF_8));
            attachment.setTitle("Raw HL7 v2.4 ORU Message");
            attachment.setCreation(new Date());
            payload.setContent(attachment);
        }

        // Subject (Patient Reference)
        String patientId = extractor.extractPatientId(rawHl7Message, terser);
        String patientName = extractor.extractPatientFullName(rawHl7Message, terser);
        if (StringUtils.isNotBlank(patientId)) {
            comm.setSubject(new Reference("Patient/" + extractor.cleanId(patientId)).setDisplay(patientName));
        }

        comm.setSent(messageTimestamp != null ? messageTimestamp : new Date());

        FhirSecurityTagManager.applyDefaultSecurityTag(comm);
        return comm;
    }
}
