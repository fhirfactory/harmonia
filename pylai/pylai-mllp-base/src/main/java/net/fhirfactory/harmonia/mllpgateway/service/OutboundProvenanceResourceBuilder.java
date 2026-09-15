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
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.Date;
import java.util.UUID;

/**
 * Builds FHIR {@link Provenance} resources recording outbound MLLP transmissions.
 */
@ApplicationScoped
public class OutboundProvenanceResourceBuilder {

    public Provenance buildProvenance(String targetResourceRef, OutboundMllpRequest request,
                                      OutboundMllpResponse response, String agentDisplayName) {
        Provenance provenance = new Provenance();
        String controlId = request != null ? request.getMessageControlId() : null;
        String cleanId = StringUtils.isNotBlank(controlId) ? cleanId(controlId) : UUID.randomUUID().toString();
        provenance.setId("Provenance/prov-out-" + cleanId);

        if (StringUtils.isNotBlank(targetResourceRef)) {
            provenance.addTarget(new Reference(targetResourceRef));
        }

        provenance.setRecorded(response != null && response.getAcknowledgedAt() != null ? response.getAcknowledgedAt() : new Date());

        // Activity
        CodeableConcept activity = new CodeableConcept();
        activity.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-DataOperation", "TRANSMIT", "Transmit"));
        activity.setText("Outbound HL7 MLLP Dispatch");
        provenance.setActivity(activity);

        // Transmitting Agent
        Provenance.ProvenanceAgentComponent agent = provenance.addAgent();
        CodeableConcept type = new CodeableConcept();
        type.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/provenance-participant-type", "transmitter", "Transmitter"));
        agent.setType(type);

        Reference who = new Reference();
        who.setType("Device");
        who.setDisplay(StringUtils.isNotBlank(agentDisplayName) ? agentDisplayName : "Harmonia MLLP Outbound Gateway");
        agent.setWho(who);

        // Target Receiver Agent
        if (request != null && StringUtils.isNotBlank(request.getDestinationId())) {
            Provenance.ProvenanceAgentComponent receiverAgent = provenance.addAgent();
            CodeableConcept receiverType = new CodeableConcept();
            receiverType.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/provenance-participant-type", "receiver", "Receiver"));
            receiverAgent.setType(receiverType);

            Reference receiverWho = new Reference();
            receiverWho.setType("Device");
            receiverWho.setDisplay(request.getDestinationId());
            receiverAgent.setWho(receiverWho);
        }

        FhirSecurityTagManager.applyDefaultSecurityTag(provenance);
        return provenance;
    }

    private String cleanId(String rawId) {
        return rawId.replaceAll("[^a-zA-Z0-9._-]", "-").replaceAll("-+", "-");
    }
}
