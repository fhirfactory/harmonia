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
import java.util.UUID;

import static net.fhirfactory.harmonia.erga.hl7v2x.common.Hl7v2ParsingSupport.cleanId;
import static net.fhirfactory.harmonia.erga.hl7v2x.common.Hl7v2ParsingSupport.parseHl7Date;

/**
 * Builds metadata FHIR resources ({@link Communication}, {@link Provenance}) from HL7 v2 MFN^M02 messages.
 */
public class MfnMetadataResourceBuilder {

    /**
     * Builds a FHIR Communication resource storing the raw HL7 v2 MFN message payload.
     *
     * @param terser               optional HAPI Terser instance
     * @param rawMessageString     raw HL7 message string
     * @param messageControlId     message control ID
     * @param triggerEvent         MFN trigger event (e.g. M02)
     * @param practitionerId       practitioner ID
     * @param practitionerFullName practitioner full name
     * @param sendingApp           MSH sending application
     * @param sendingFacility      MSH sending facility
     * @param messageTimestamp     MSH message timestamp string
     * @return populated Communication resource
     */
    public Communication buildCommunication(Terser terser, String rawMessageString, String messageControlId,
                                            String triggerEvent, String practitionerId, String practitionerFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        if (StringUtils.isBlank(rawMessageString)) return null;

        Communication comm = new Communication();
        String idSeed = StringUtils.isNotBlank(messageControlId) ? cleanId(messageControlId) : UUID.randomUUID().toString().substring(0, 8);
        comm.setId("Communication/comm-" + idSeed);
        comm.setStatus(Enumerations.EventStatus.COMPLETED);

        if (StringUtils.isNotBlank(messageTimestamp)) {
            Date sent = parseHl7Date(messageTimestamp);
            if (sent != null) comm.setSent(sent);
        } else {
            comm.setSent(new Date());
        }

        if (StringUtils.isNotBlank(practitionerId)) {
            comm.setSubject(new Reference("Practitioner/" + cleanId(practitionerId)).setDisplay(practitionerFullName));
        }

        if (StringUtils.isNotBlank(sendingFacility)) {
            comm.setSender(new Reference("Organization/" + cleanId(sendingFacility)).setDisplay(sendingFacility));
        }

        // Payload attachment with raw HL7 v2 message
        Communication.CommunicationPayloadComponent payload = comm.addPayload();
        Attachment att = new Attachment();
        att.setContentType("x-application/hl7-v2+er7");
        att.setData(rawMessageString.getBytes(StandardCharsets.UTF_8));
        att.setTitle("HL7 v2.x MFN^" + (StringUtils.isNotBlank(triggerEvent) ? triggerEvent : "M02") + " Message");
        payload.setContent(att);

        return comm;
    }

    /**
     * Builds a FHIR Provenance resource for the generated Bundle.
     *
     * @param bundle           the target FHIR Bundle
     * @param practitioner     primary Practitioner resource
     * @param messageControlId message control ID
     * @param triggerEvent     trigger event
     * @param sendingApp       sending application
     * @param sendingFacility  sending facility
     * @param activityId       ID of the task processing activity
     * @param activityName     Name of the task processing activity
     * @return populated Provenance resource
     */
    public Provenance buildProvenance(Bundle bundle, Practitioner practitioner, String messageControlId,
                                      String triggerEvent, String sendingApp, String sendingFacility,
                                      String activityId, String activityName) {
        Provenance provenance = new Provenance();
        String idSeed = StringUtils.isNotBlank(messageControlId) ? cleanId(messageControlId) : UUID.randomUUID().toString().substring(0, 8);
        provenance.setId("Provenance/prov-" + idSeed);
        provenance.setRecorded(new Date());

        // Target references
        if (bundle != null && bundle.hasEntry()) {
            for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                if (entry.hasResource() && !(entry.getResource() instanceof Provenance)) {
                    provenance.addTarget(new Reference(entry.getResource().getId()));
                }
            }
        } else if (practitioner != null) {
            provenance.addTarget(new Reference("Practitioner/" + practitioner.getIdPart()));
        }

        // Activity code
        CodeableConcept act = new CodeableConcept();
        act.setText("HL7 v2.x MFN to FHIR R5 Bundle Transformation");
        act.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-DataOperation", "CREATE", "create"));
        provenance.setActivity(act);

        // Agent: The transforming activity device
        Provenance.ProvenanceAgentComponent agent = provenance.addAgent();
        agent.setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/provenance-participant-type", "assembler", "Assembler")));
        String deviceId = activityId != null ? activityId : "mfn2fhir-bundle";
        String deviceDisplay = activityName != null ? activityName : "MFN to FHIR Bundle Activity";
        agent.setWho(new Reference("Device/" + deviceId).setDisplay(deviceDisplay));

        // Provenance entity (source)
        if (StringUtils.isNotBlank(messageControlId)) {
            Provenance.ProvenanceEntityComponent entity = provenance.addEntity();
            entity.setRole(Provenance.ProvenanceEntityRole.SOURCE);
            entity.setWhat(new Reference("Communication/comm-" + idSeed).setDisplay("Raw MFN Message"));
        }

        return provenance;
    }
}
