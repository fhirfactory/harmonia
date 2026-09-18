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
import net.fhirfactory.harmonia.mllpgateway.hl7.OrmMessageExtractor;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.Date;

@ApplicationScoped
public class OrmTaskResourceBuilder {

    private final OrmMessageExtractor extractor;

    @Inject
    public OrmTaskResourceBuilder(OrmMessageExtractor extractor) {
        this.extractor = extractor;
    }

    public OrmTaskResourceBuilder() {
        this(new OrmMessageExtractor());
    }

    public Task buildTask(String rawHl7Message, String messageControlId, String triggerEvent,
                          Topic topic, Terser terser, String gatewayInstanceId,
                          String sendingApp, String sendingFacility, Date messageTimestamp,
                          Communication communication) {
        Task task = new Task();
        String placerOrderNumber = extractor.extractPlacerOrderNumber(rawHl7Message, terser);
        String universalServiceId = extractor.extractUniversalServiceId(rawHl7Message, terser);
        String universalServiceText = extractor.extractUniversalServiceText(rawHl7Message, terser);
        String patientId = extractor.extractPatientId(rawHl7Message, terser);
        String patientName = extractor.extractPatientFullName(rawHl7Message, terser);
        String orderControl = extractor.extractOrderControl(rawHl7Message, terser);

        task.setId(extractor.cleanId(messageControlId != null ? messageControlId : java.util.UUID.randomUUID().toString()));
        task.setStatus(extractor.determineTaskStatus(orderControl));
        task.setIntent(Task.TaskIntent.ORDER);
        task.setPriority(Enumerations.RequestPriority.ROUTINE);

        // Identifiers
        Identifier id = task.addIdentifier();
        id.setSystem("http://harmonia.net/identifier/hl7-message-control-id");
        id.setValue(messageControlId);

        if (StringUtils.isNotBlank(placerOrderNumber)) {
            Identifier ordId = task.addIdentifier();
            ordId.setSystem("http://harmonia.net/identifier/placer-order-number");
            ordId.setValue(placerOrderNumber);
        }

        // Code
        CodeableConcept code = task.getCode();
        code.setText("ORM^" + (triggerEvent != null ? triggerEvent : "O01") + " - " + universalServiceText);
        code.addCoding()
                .setSystem("http://harmonia.net/codesystem/hl7-trigger-event")
                .setCode(triggerEvent != null ? triggerEvent : "O01")
                .setDisplay("Order Message");

        task.setDescription("Clinical Order: " + universalServiceText + " (" + universalServiceId + ")");

        // Focus & For
        if (StringUtils.isNotBlank(patientId)) {
            task.setFor(new Reference("Patient/" + extractor.cleanId(patientId)).setDisplay(patientName));
        }

        task.setAuthoredOn(messageTimestamp != null ? messageTimestamp : new Date());
        task.setLastModified(new Date());

        // Business Status
        CodeableConcept bizStatus = task.getBusinessStatus();
        bizStatus.setText(orderControl != null ? orderControl : "NW");

        // Note
        Annotation note = task.addNote();
        note.setText("Order Placer ID: " + placerOrderNumber + " | Universal Service: " + universalServiceId + " (" + universalServiceText + ")");
        note.setTime(new Date());

        // Universal Service Identifier as Task.input
        Task.TaskInputComponent svcInput = task.addInput();
        svcInput.getType().setText("Universal Service Identifier");
        svcInput.setValue(new StringType(universalServiceId));

        // Communication as Task.input
        if (communication != null) {
            task.addContained(communication);
            Task.TaskInputComponent commInput = task.addInput();
            commInput.getType().setText("HL7 Communication Resource");
            String commRef = communication.getIdPart() != null ? "#" + communication.getIdPart() : "#" + communication.getId();
            commInput.setValue(new Reference(commRef));
        }

        FhirSecurityTagManager.applyDefaultSecurityTag(task);
        return task;
    }
}
