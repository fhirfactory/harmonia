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

package net.fhirfactory.harmonia.erga.hl7v2x.factories;

import ca.uhn.hl7v2.util.Terser;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static net.fhirfactory.harmonia.erga.hl7v2x.common.Hl7v2ParsingSupport.cleanId;
import static net.fhirfactory.harmonia.erga.hl7v2x.common.Hl7v2ParsingSupport.parseHl7Date;

/**
 * Builds metadata and audit FHIR resources ({@link Communication}, {@link Provenance}) for ADT transformations.
 */
public class AdtMetadataResourceBuilder {

    /**
     * Builds Communication resource containing raw ADT message.
     */
    public Communication buildCommunication(Terser terser, String rawMessageString, String messageControlId,
                                            String triggerEvent, String patientId, String patientFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        Communication communication = new Communication();
        communication.setId("Communication/comm-" + cleanId(messageControlId));
        communication.setStatus(Enumerations.EventStatus.COMPLETED);
        communication.setPriority(Enumerations.RequestPriority.ROUTINE);

        if (StringUtils.isNotBlank(patientId) && !"unknown".equalsIgnoreCase(patientId)) {
            communication.setSubject(new Reference("Patient/" + cleanId(patientId)).setDisplay(patientFullName));
        }

        if (StringUtils.isNotBlank(messageTimestamp)) {
            Date sentDate = parseHl7Date(messageTimestamp);
            if (sentDate != null) communication.setSent(sentDate);
        }
        communication.setReceived(new Date());

        Communication.CommunicationPayloadComponent payload = communication.addPayload();
        Attachment attachment = new Attachment();
        attachment.setContentType("application/hl7-v2");
        attachment.setData(rawMessageString != null ? rawMessageString.getBytes(StandardCharsets.UTF_8) : new byte[0]);
        attachment.setTitle("Raw HL7 v2.x ADT Message (" + triggerEvent + ")");
        attachment.setCreation(new Date());
        payload.setContent(attachment);

        return communication;
    }

    /**
     * Builds Provenance resource tracing the transformation.
     */
    public Provenance buildProvenance(Bundle bundle, Patient patient, String messageControlId, String triggerEvent,
                                      String sendingApp, String sendingFacility, String activityId, String activityName) {
        Provenance provenance = new Provenance();
        provenance.setId("Provenance/prov-" + cleanId(messageControlId));
        provenance.setRecorded(new Date());

        if (bundle != null) {
            provenance.addTarget(new Reference("Bundle/" + bundle.getIdPart()));
        }
        if (patient != null) {
            provenance.setPatient(new Reference("Patient/" + patient.getIdPart()));
        }

        CodeableConcept activity = new CodeableConcept();
        activity.setText(activityName != null ? activityName : "ADT to FHIR Mapper Activity");
        activity.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-DataOperation", "TRANSFORM", "Transform"));
        provenance.setActivity(activity);

        Provenance.ProvenanceAgentComponent agent = provenance.addAgent();
        agent.setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/provenance-participant-type", "assembler", "Assembler")));
        String deviceId = activityId != null ? activityId : "adt2fhir-mapper";
        String deviceDisplay = activityName != null ? activityName : "ADT to FHIR Mapper Activity";
        agent.setWho(new Reference("Device/" + deviceId).setDisplay(deviceDisplay));

        return provenance;
    }
}
