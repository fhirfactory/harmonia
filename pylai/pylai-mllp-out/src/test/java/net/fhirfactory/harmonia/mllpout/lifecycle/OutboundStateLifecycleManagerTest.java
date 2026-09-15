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

package net.fhirfactory.harmonia.mllpout.lifecycle;

import net.fhirfactory.harmonia.mllpgateway.messaging.TaskEventProducerService;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import net.fhirfactory.harmonia.mllpgateway.service.CommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.ProvenanceService;
import net.fhirfactory.harmonia.mllpgateway.service.TaskService;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.Provenance;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboundStateLifecycleManagerTest {

    @Mock
    private CommunicationService communicationService;

    @Mock
    private TaskService taskService;

    @Mock
    private ProvenanceService provenanceService;

    @Mock
    private TaskEventProducerService taskEventProducerService;

    private OutboundStateLifecycleManager lifecycleManager;

    @BeforeEach
    void setUp() {
        lifecycleManager = new OutboundStateLifecycleManager(communicationService, taskService, provenanceService, taskEventProducerService);
    }

    @Test
    void testOnDispatchInitiated() {
        Topic topic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-8001", "HIS_NORTH", "MSH|...", topic);

        when(communicationService.create(any(Communication.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(taskService.create(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Communication comm = lifecycleManager.onDispatchInitiated(request);

        assertThat(comm).isNotNull();
        assertThat(comm.getId()).contains("comm-out-MSG-8001");
        verify(communicationService).create(any(Communication.class));
        verify(taskService).create(any(Task.class));
    }

    @Test
    void testOnDispatchCompletedSuccess() throws Exception {
        Topic topic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        OutboundMllpRequest request = new OutboundMllpRequest("MSG-8002", "HIS_NORTH", "MSH|...", topic);
        OutboundMllpResponse response = OutboundMllpResponse.success("MSG-8002", "AA", "MSA|AA|MSG-8002", 30L);

        when(communicationService.getById(anyString())).thenReturn(Optional.empty());
        when(taskService.getById(anyString())).thenReturn(Optional.empty());

        OutboundMllpResponse result = lifecycleManager.onDispatchCompleted(request, response);

        assertThat(result.getCommunicationId()).contains("comm-out-MSG-8002");
        assertThat(result.getTaskId()).contains("task-out-MSG-8002");
        assertThat(result.getProvenanceId()).contains("prov-out-MSG-8002");

        verify(communicationService).create(any(Communication.class));
        verify(taskService).create(any(Task.class));
        verify(provenanceService).create(any(Provenance.class));
        verify(taskEventProducerService).sendTaskEvent(anyString(), eq("MLLP_OUTBOUND_DISPATCH"), eq("COMPLETED"), eq(topic), eq("MSG-8002"), anyString());
    }
}
