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

package net.fhirfactory.hie.mllpgateway.hl7.factories;

import ca.uhn.hl7v2.util.Terser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.hie.mllpgateway.hl7.MfnMessageExtractor;
import net.fhirfactory.hie.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.Date;
import java.util.UUID;

/**
 * Builds FHIR {@link Provenance} resources linking the generated Task and source Communication for MFN events.
 */
@ApplicationScoped
public class MfnProvenanceResourceBuilder {

    private final MfnMessageExtractor extractor;

    public MfnProvenanceResourceBuilder() {
        this(new MfnMessageExtractor());
    }

    @Inject
    public MfnProvenanceResourceBuilder(MfnMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new MfnMessageExtractor();
    }

    public Provenance buildProvenance(Terser terser, Topic topic, String messageControlId,
                                      String practitionerId, String practitionerFullName,
                                      String sendingApp, String sendingFacility, String messageTimestamp,
                                      Task savedTask, Communication savedCommunication) {
        String triggerEvent = topic != null ? topic.getDataElementQualifier() : "M02";
        return buildProvenance(terser, topic, messageControlId, triggerEvent, practitionerId, practitionerFullName,
                sendingApp, sendingFacility, messageTimestamp, savedTask, savedCommunication);
    }

    public Provenance buildProvenance(Terser terser, String messageControlId, String triggerEvent,
                                      String practitionerId, String practitionerFullName,
                                      String sendingApp, String sendingFacility, String messageTimestamp,
                                      Task savedTask, Communication savedCommunication) {
        return buildProvenance(terser, null, messageControlId, triggerEvent, practitionerId, practitionerFullName,
                sendingApp, sendingFacility, messageTimestamp, savedTask, savedCommunication);
    }

    public Provenance buildProvenance(Terser terser, Topic topic, String messageControlId, String triggerEvent,
                                      String practitionerId, String practitionerFullName,
                                      String sendingApp, String sendingFacility, String messageTimestamp,
                                      Task savedTask, Communication savedCommunication) {
        Provenance provenance = new Provenance();
        String provId = "prov-" + (StringUtils.isNotBlank(messageControlId) ? messageControlId : UUID.randomUUID().toString());
        provenance.setId("Provenance/" + provId);

        // 1. Target: The Task resource created from the HL7 message
        if (savedTask != null) {
            String taskId = savedTask.getId();
            if (StringUtils.isNotBlank(taskId)) {
                Reference targetRef = new Reference(taskId.startsWith("Task/") ? taskId : "Task/" + taskId);
                targetRef.setType("Task");
                targetRef.setDisplay("Task for Practitioner " + StringUtils.defaultIfBlank(practitionerFullName, "Unknown") + " (HL7 MFN " + (triggerEvent != null ? triggerEvent : "M02") + ")");
                provenance.addTarget(targetRef);
            }
        } else {
            Reference targetRef = new Reference("Task/" + messageControlId);
            targetRef.setType("Task");
            targetRef.setDisplay("Task for Practitioner " + StringUtils.defaultIfBlank(practitionerFullName, "Unknown") + " (HL7 MFN " + (triggerEvent != null ? triggerEvent : "M02") + ")");
            provenance.addTarget(targetRef);
        }

        // 2. Activity: HL7 MFN Message Transformation
        CodeableConcept activity = new CodeableConcept();
        activity.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-DataOperation", "CREATE", "Create"));
        activity.addCoding(new Coding("http://example.org/hl7/trigger-event", triggerEvent != null ? triggerEvent : "M02", "HL7 MFN " + (triggerEvent != null ? triggerEvent : "M02")));
        activity.setText("Ingestion and Transformation of HL7 MFN " + (triggerEvent != null ? triggerEvent : "M02") + " Event");
        provenance.setActivity(activity);

        // 3. Occurred / Recorded Timestamps
        if (StringUtils.isNotBlank(messageTimestamp)) {
            Date eventDate = extractor.parseHl7Date(messageTimestamp);
            if (eventDate != null) {
                provenance.setOccurred(new DateTimeType(eventDate));
            }
        }
        provenance.setRecorded(new Date());

        // 4. Source Entity: Link the source Communication containing the raw HL7 payload
        if (savedCommunication != null) {
            Provenance.ProvenanceEntityComponent sourceEntity = provenance.addEntity();
            sourceEntity.setRole(Provenance.ProvenanceEntityRole.SOURCE);
            String commId = savedCommunication.getId();
            sourceEntity.setWhat(new Reference(commId).setDisplay("Raw HL7 MFN Message Communication"));
        }

        // 5. Patient / Subject Context (Practitioner)
        if (StringUtils.isNotBlank(practitionerId) && !"UNKNOWN".equalsIgnoreCase(practitionerId)) {
            Reference practRef = new Reference("Practitioner/" + extractor.cleanId(practitionerId));
            if (StringUtils.isNotBlank(practitionerFullName)) {
                practRef.setDisplay(practitionerFullName);
            }
            provenance.setPatient(practRef);
        }

        // 6. Agents
        // Agent A: Sending Device / Application (Transmitter)
        Provenance.ProvenanceAgentComponent transmitterAgent = provenance.addAgent();
        transmitterAgent.setType(new CodeableConcept().addCoding(
                new Coding("http://terminology.hl7.org/CodeSystem/provenance-participant-type", "transmitter", "Transmitter")));
        Reference transmitterRef = new Reference();
        transmitterRef.setType("Device");
        StringBuilder transDisp = new StringBuilder();
        if (StringUtils.isNotBlank(sendingApp)) transDisp.append(sendingApp);
        if (StringUtils.isNotBlank(sendingFacility)) {
            if (transDisp.length() > 0) transDisp.append(" @ ");
            transDisp.append(sendingFacility);
        }
        transmitterRef.setDisplay(transDisp.length() > 0 ? transDisp.toString() : "HL7 Ingest Client");
        transmitterAgent.setWho(transmitterRef);

        // Agent B: HIE MLLP Gateway (Assembler / Ingest Device)
        Provenance.ProvenanceAgentComponent gatewayAgent = provenance.addAgent();
        gatewayAgent.setType(new CodeableConcept().addCoding(
                new Coding("http://terminology.hl7.org/CodeSystem/provenance-participant-type", "assembler", "Assembler")));
        Reference gatewayRef = new Reference("Device/hie-mllp-gateway").setDisplay("HIE MLLP Gateway");
        gatewayAgent.setWho(gatewayRef);

        // Agent C: Practitioner (Author / Subject, if present)
        if (StringUtils.isNotBlank(practitionerId) && !"UNKNOWN".equalsIgnoreCase(practitionerId)) {
            Provenance.ProvenanceAgentComponent practAgent = provenance.addAgent();
            practAgent.setType(new CodeableConcept().addCoding(
                    new Coding("http://terminology.hl7.org/CodeSystem/provenance-participant-type", "author", "Author")));
            Reference docRef = new Reference("Practitioner/" + extractor.cleanId(practitionerId));
            if (StringUtils.isNotBlank(practitionerFullName)) {
                docRef.setDisplay(practitionerFullName);
            }
            practAgent.setWho(docRef);
        }

        if (topic != null && provenance.getActivity() != null) {
            provenance.getActivity().addCoding(new Coding("http://example.org/hie/topic", topic.toTopicString(), "HIE Topic: " + topic.toTopicString()));
        }

        return provenance;
    }
}
