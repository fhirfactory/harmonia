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

import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Provenance;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OutboundResourceBuildersTest {

    private OutboundCommunicationResourceBuilder commBuilder;
    private OutboundTaskResourceBuilder taskBuilder;
    private OutboundProvenanceResourceBuilder provBuilder;

    @BeforeEach
    void setUp() {
        commBuilder = new OutboundCommunicationResourceBuilder();
        taskBuilder = new OutboundTaskResourceBuilder();
        provBuilder = new OutboundProvenanceResourceBuilder();
    }

    @Test
    void testCommunicationLifecycle() {
        Topic topic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        String hl7 = "MSH|^~\\&|HARMONIA|HIE|HIS_SYS|HOSP_N|20260915083000||ADT^A01|MSG-9001|P|2.4\r";
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-9001", "HIS_NORTH", hl7, topic);

        Communication initialComm = commBuilder.buildInitialCommunication(request, "Hospital North");
        assertThat(initialComm.getStatus()).isEqualTo(Enumerations.EventStatus.INPROGRESS);
        assertThat(initialComm.getId()).contains("comm-out-MSG-9001");
        assertThat(initialComm.getPayload()).hasSize(1);
        assertThat(FhirSecurityTagManager.hasConfidentiality(initialComm, FhirConfidentialityEnum.N)).isTrue();

        OutboundMllpResponse response = OutboundMllpResponse.success("MSG-9001", "AA", "MSA|AA|MSG-9001", 30L);
        Communication updatedComm = commBuilder.updateCommunicationWithResponse(initialComm, response);
        assertThat(updatedComm.getStatus()).isEqualTo(Enumerations.EventStatus.COMPLETED);
        assertThat(updatedComm.getPayload()).hasSize(2);
        assertThat(FhirSecurityTagManager.hasConfidentiality(updatedComm, FhirConfidentialityEnum.N)).isTrue();
    }

    @Test
    void testTaskLifecycle() {
        Topic topic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        String hl7 = "MSH|^~\\&|HARMONIA|HIE|HIS_SYS|HOSP_N|20260915083000||ADT^A01|MSG-9002|P|2.4\r";
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-9002", "HIS_NORTH", hl7, topic);

        Communication comm = commBuilder.buildInitialCommunication(request, "HIS_NORTH");
        Task initialTask = taskBuilder.buildInitialTask(request, comm);

        assertThat(initialTask.getStatus()).isEqualTo(Task.TaskStatus.INPROGRESS);
        assertThat(initialTask.getId()).contains("task-out-MSG-9002");
        assertThat(initialTask.getFocus().getReference()).isEqualTo(comm.getId());
        assertThat(FhirSecurityTagManager.hasConfidentiality(initialTask, FhirConfidentialityEnum.N)).isTrue();

        OutboundMllpResponse response = OutboundMllpResponse.success("MSG-9002", "AA", "MSA|AA|MSG-9002", 20L);
        Task updatedTask = taskBuilder.updateTaskWithResponse(initialTask, response);
        assertThat(updatedTask.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(updatedTask.getOutput()).hasSize(1);
        assertThat(updatedTask.getOutputFirstRep().getExtension()).isNotEmpty();
        assertThat(updatedTask.getOutputFirstRep().getExtensionByUrl("http://example.org/hie/destination-delivery-status")).isNotNull();
        assertThat(FhirSecurityTagManager.hasConfidentiality(updatedTask, FhirConfidentialityEnum.N)).isTrue();
    }

    @Test
    void testProvenanceCreation() {
        Topic topic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-9003", "HIS_NORTH", "MSH|...", topic);
        OutboundMllpResponse response = OutboundMllpResponse.success("MSG-9003", "AA", "MSA|AA|MSG-9003", 25L);

        Provenance provenance = provBuilder.buildProvenance("Communication/comm-out-MSG-9003", request, response, "Harmonia Gateway HIS");

        assertThat(provenance.getId()).contains("prov-out-MSG-9003");
        assertThat(provenance.getTarget()).hasSize(1);
        assertThat(provenance.getTarget().get(0).getReference()).isEqualTo("Communication/comm-out-MSG-9003");
        assertThat(provenance.getAgent()).hasSize(2);
        assertThat(FhirSecurityTagManager.hasConfidentiality(provenance, FhirConfidentialityEnum.N)).isTrue();
    }
}
