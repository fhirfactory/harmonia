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

package net.fhirfactory.harmonia.mllpgateway.hl7;

import net.fhirfactory.harmonia.mllpgateway.config.MllpConfig;
import net.fhirfactory.harmonia.mllpgateway.messaging.TaskEventProducerService;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.mllpgateway.service.CommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultCommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultTaskService;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IncomingAdtMessageProcessorTest {

    private DefaultTaskService taskService;
    private DefaultCommunicationService communicationService;
    private TaskEventProducerService taskEventProducerService;
    private IncomingAdtMessageProcessor transformer;

    @BeforeEach
    void setUp() {
        taskService = new DefaultTaskService();
        taskService.clear();
        communicationService = new DefaultCommunicationService();
        communicationService.clear();
        taskEventProducerService = mock(TaskEventProducerService.class);
        transformer = new IncomingAdtMessageProcessor(taskService, communicationService, taskEventProducerService);
    }

    @Test
    void testProcessAdtA01Message() {
        String hl7A01 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907101500||ADT^A01|MSG-A01-001|P|2.4\r" +
                "EVN|A01|20260907101500\r" +
                "PID|1||PAT10099^^^HOSPITAL^MR||SMITH^JOHN^A||19800512|M|||123 MAIN ST^^SPRINGFIELD^IL^62701\r" +
                "NK1|1|SMITH^MARY|WIFE^Wife|123 MAIN ST^^SPRINGFIELD^IL^62701|555-1234\r" +
                "PV1|1|I|WARDA^RM101^BED1^HOSPITAL||||DOC01^JONES^ROBERT^^DR||||||||||||V20260907-01|||||||||||||||||||||||||20260907100000\r";

        AdtProcessingResult result = transformer.processAdtMessage(hl7A01);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessageControlId()).isEqualTo("MSG-A01-001");
        assertThat(result.getTriggerEvent()).isEqualTo("A01");
        assertThat(result.getPatientId()).isEqualTo("PAT10099");
        assertThat(result.getPatientName()).isEqualTo("JOHN SMITH");
        assertThat(result.getAckMessage()).contains("MSA|AA|MSG-A01-001");

        // Verify Topic is populated on result
        Topic topic = result.getTopic();
        assertThat(topic).isNotNull();
        assertThat(topic.getDomain()).isEqualTo("Health");
        assertThat(topic.getModel()).isEqualTo("HL7");
        assertThat(topic.getModelVersion()).isEqualTo("2.4");
        assertThat(topic.getDataElement()).isEqualTo("ADT");
        assertThat(topic.getDataElementQualifier()).isEqualTo("A01");

        // Verify Communication resource created and persisted in CommunicationService
        Communication comm = result.getCommunication();
        assertThat(comm).isNotNull();
        assertThat(comm.getIdPart()).isEqualTo("comm-MSG-A01-001");
        assertThat(comm.getStatus()).isEqualTo(Enumerations.EventStatus.COMPLETED);
        assertThat(comm.getPriority()).isEqualTo(Enumerations.RequestPriority.ROUTINE);
        assertThat(comm.getSubject().getReference()).isEqualTo("Patient/PAT10099");
        assertThat(comm.getSubject().getDisplay()).isEqualTo("JOHN SMITH");
        assertThat(comm.getSender().getDisplay()).contains("EPIC @ HOSPITAL");
        assertThat(comm.getRecipientFirstRep().getDisplay()).isEqualTo("Harmonia MLLP Gateway");
        assertThat(comm.getNoteFirstRep().getText()).contains("Raw HL7 v2.4 A01 ADT message received");

        // Verify Communication contains raw ADT message payload
        assertThat(comm.getPayload()).hasSize(1);
        Communication.CommunicationPayloadComponent commPayload = comm.getPayloadFirstRep();
        assertThat(commPayload.getContent()).isInstanceOf(Attachment.class);
        Attachment attachment = (Attachment) commPayload.getContent();
        assertThat(attachment.getContentType()).isEqualTo("application/hl7-v2");
        assertThat(new String(attachment.getData(), StandardCharsets.UTF_8)).isEqualTo(hl7A01);

        // Verify Communication persisted in CommunicationService cache
        assertThat(communicationService.count()).isEqualTo(1);
        assertThat(communicationService.getById("comm-MSG-A01-001")).isPresent();

        Task task = result.getTask();
        assertThat(task).isNotNull();
        assertThat(task.getStatus()).isEqualTo(Task.TaskStatus.REQUESTED);
        assertThat(task.getPriority()).isEqualTo(Enumerations.RequestPriority.ROUTINE);
        assertThat(ErgonReasonEnum.hasReason(task, ErgonReasonEnum.HARMONIA_SYNTHETIC_TASK)).isTrue();
        assertThat(task.getReasonFirstRep().getConcept().getText()).isEqualTo("Harmonia-Synthetic-Task");
        assertThat(task.getDescription()).contains("ADT^A01 (Admit/Visit Notification) for Patient JOHN SMITH (ID: PAT10099)");
        assertThat(task.getFor().getReference()).isEqualTo("Patient/PAT10099");
        assertThat(task.getFor().getDisplay()).isEqualTo("JOHN SMITH");
        assertThat(task.getFocus().getReference()).isEqualTo("Encounter/V20260907-01");
        assertThat(task.getCode().getCodingFirstRep().getCode()).isEqualTo("A01");

        // Verify Task links to source Communication via basedOn and Task.input
        assertThat(task.getBasedOn()).hasSize(1);
        assertThat(task.getBasedOnFirstRep().getReference()).isEqualTo("Communication/comm-MSG-A01-001");
        assertThat(task.getContained()).hasSize(1);
        assertThat(task.getContained().get(0)).isInstanceOf(Communication.class);
        assertThat(task.getInput().stream().anyMatch(i -> i.getType().hasCoding() && "input-communication".equals(i.getType().getCodingFirstRep().getCode()))).isTrue();

        // Verify Task was persisted in TaskService
        assertThat(taskService.count()).isEqualTo(1);
        assertThat(taskService.getById("MSG-A01-001")).isPresent();

        // Verify TaskEvent was sent to task-processor after saving to cache
        ArgumentCaptor<ErgonEvent> eventCaptor = ArgumentCaptor.forClass(ErgonEvent.class);
        try {
            verify(taskEventProducerService, atLeastOnce()).sendTaskEvent(eventCaptor.capture());
            ErgonEvent sentEvent = eventCaptor.getValue();
            assertThat(sentEvent.getTaskId()).isEqualTo("MSG-A01-001");
            assertThat(sentEvent.getAction()).isEqualTo("PROCESS");
            assertThat(sentEvent.getStatus()).isEqualTo("requested");
            assertThat(sentEvent.getGatewayInstanceId()).isEqualTo("mllp-gateway-default");
            assertThat(sentEvent.getTopic()).isNotNull();
            assertThat(sentEvent.getTopic().getDomain()).isEqualTo("Health");
            assertThat(sentEvent.getTopic().getModel()).isEqualTo("HL7");
            assertThat(sentEvent.getTopic().getDataElement()).isEqualTo("ADT");
            assertThat(sentEvent.getTopic().getDataElementQualifier()).isEqualTo("A01");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testProcessAdtA03DischargeMessage() {
        String hl7A03 = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907120000||ADT^A03|MSG-A03-002|P|2.4\r" +
                "EVN|A03|20260907120000\r" +
                "PID|1||PAT20088^^^HOSPITAL^MR||DOE^JANE||19920824|F\r" +
                "PV1|1|I|WARDB^RM202^BED2^HOSPITAL||||DOC02^BROWN^SARAH^^DR|||||||||||V20260907-02||||||||||||||||||||||||||20260907120000\r";

        AdtProcessingResult result = transformer.processAdtMessage(hl7A03);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getTriggerEvent()).isEqualTo("A03");
        assertThat(result.getTask().getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(result.getAckMessage()).contains("MSA|AA|MSG-A03-002");
        assertThat(result.getCommunication()).isNotNull();
    }

    @Test
    void testProcessAdtA08UpdateMessageWithEmergency() {
        String hl7A08 = "MSH|^~\\&|CERNER|HOSPITAL|HIE|HIE_IM|20260907130000||ADT^A08|MSG-A08-003|P|2.4\r" +
                "EVN|A08|20260907130000\r" +
                "PID|1||PAT30077^^^HOSPITAL^MR||TAYLOR^ALICE||19750315|F\r" +
                "PV1|1|E|ER^RM01^BAY1^HOSPITAL||||DOC03^WILSON^EMILY^^DR|||||||||||V20260907-03\r";

        AdtProcessingResult result = transformer.processAdtMessage(hl7A08);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getTriggerEvent()).isEqualTo("A08");
        assertThat(result.getTask().getStatus()).isEqualTo(Task.TaskStatus.INPROGRESS);
        assertThat(result.getTask().getPriority()).isEqualTo(Enumerations.RequestPriority.STAT);
        assertThat(result.getAckMessage()).contains("MSA|AA|MSG-A08-003");
        assertThat(result.getCommunication()).isNotNull();
    }

    @Test
    void testProcessAdtWithMultipleNextOfKinAndPractitioners() {
        String hl7Multi = "MSH|^~\\&|EPIC|HOSPITAL|HIE|HIE_IM|20260907140000||ADT^A01|MSG-MULTI-01|P|2.4\r" +
                "PID|1||PAT500||JOHNSON^ROBERT^M||19650420|M|||456 ELM ST^^BOSTON^MA^02101\r" +
                "NK1|1|JOHNSON^PATRICIA|SPOUSE^Spouse|456 ELM ST^^BOSTON^MA^02101|555-1111\r" +
                "NK1|2|JOHNSON^DAVID|CHILD^Son|789 OAK ST^^BOSTON^MA^02102|555-2222\r" +
                "PV1|1|I|ICU^RM1^BED1||||DOC10^CLARK^JAMES^^DR|DOC20^LEE^SUSAN^^DR|||||||||DOC30^DAVIS^PAUL^^DR||V5001\r";

        AdtProcessingResult result = transformer.processAdtMessage(hl7Multi);
        assertThat(result.isSuccess()).isTrue();

        Task task = result.getTask();
        assertThat(task).isNotNull();
        assertThat(task.getContained()).hasSize(1);
        assertThat(task.getContained().get(0)).isInstanceOf(Communication.class);
    }

    @Test
    void testCommunicationPersistedBeforeTask() {
        String hl7 = "MSH|^~\\&|APP|FAC|HIE|HIE_IM|20260907100000||ADT^A01|MSG-ORDER-001|P|2.4\r" +
                "PID|1||PAT111||TEST^USER||19900101|M\r" +
                "PV1|1|I|WARD1\r";

        CommunicationService mockCommService = mock(CommunicationService.class);
        DefaultTaskService mockTaskService = mock(DefaultTaskService.class);

        when(mockCommService.create(any(Communication.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mockTaskService.create(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncomingAdtMessageProcessor testTransformer = new IncomingAdtMessageProcessor(mockTaskService, mockCommService, taskEventProducerService);
        AdtProcessingResult res = testTransformer.processAdtMessage(hl7);

        assertThat(res.isSuccess()).isTrue();

        org.mockito.InOrder inOrder = inOrder(mockCommService, mockTaskService);
        inOrder.verify(mockCommService).create(any(Communication.class));
        inOrder.verify(mockTaskService).create(any(Task.class));
    }

    @Test
    void testTaskCreatedAfterCommunication() {
        String hl7 = "MSH|^~\\&|APP|FAC|HIE|HIE_IM|20260907100000||ADT^A01|MSG-NOPROV-001|P|2.4\r" +
                "PID|1||PAT111||TEST^USER||19900101|M\r" +
                "PV1|1|I|WARD1\r";

        CommunicationService mockCommService = mock(CommunicationService.class);
        DefaultTaskService mockTaskService = mock(DefaultTaskService.class);

        when(mockCommService.create(any(Communication.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mockTaskService.create(any(Task.class))).thenReturn(null);

        IncomingAdtMessageProcessor testTransformer = new IncomingAdtMessageProcessor(mockTaskService, mockCommService, taskEventProducerService);
        AdtProcessingResult res = testTransformer.processAdtMessage(hl7);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getCommunication()).isNotNull();
        assertThat(res.getTask()).isNull();
    }

    @Test
    void testProcessNonAdtTriggerTypes() throws Exception {
        MllpConfig mllpConfig = new MllpConfig("custom-lab-gw", "0.0.0.0", 2576, true);
        TaskEventProducerService mockProducer = mock(TaskEventProducerService.class);
        transformer.setMllpConfig(mllpConfig);
        transformer.setTaskEventProducerService(mockProducer);

        // ORU R01 Lab Result
        String oruR01 = "MSH|^~\\&|LIS|LAB|HIE|HIE_IM|20260907150000||ORU^R01|MSG-ORU-001|P|2.4\r" +
                "PID|1||PAT9999||SMITH^JOHN||19800101|M\r" +
                "PV1|1|O\r";
        AdtProcessingResult resultOru = transformer.processAdtMessage(oruR01);
        assertThat(resultOru.isSuccess()).isTrue();
        assertThat(resultOru.getTriggerEvent()).isEqualTo("R01");
        assertThat(resultOru.getMessageControlId()).isEqualTo("MSG-ORU-001");

        ArgumentCaptor<ErgonEvent> eventCaptor = ArgumentCaptor.forClass(ErgonEvent.class);
        verify(mockProducer).sendTaskEvent(eventCaptor.capture());
        ErgonEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getGatewayInstanceId()).isEqualTo("custom-lab-gw");
        assertThat(capturedEvent.getMessageType()).isEqualTo("ORU");
        assertThat(capturedEvent.getTriggerType()).isEqualTo("R01");
        assertThat(capturedEvent.getControlId()).isEqualTo("MSG-ORU-001");

        // ORM O01 Order
        reset(mockProducer);
        String ormO01 = "MSH|^~\\&|CIS|CLINIC|HIE|HIE_IM|20260907151000||ORM^O01|MSG-ORM-002|P|2.4\r" +
                "PID|1||PAT8888||DOE^JANE||19850202|F\r";
        AdtProcessingResult resultOrm = transformer.processAdtMessage(ormO01);
        assertThat(resultOrm.isSuccess()).isTrue();
        assertThat(resultOrm.getTriggerEvent()).isEqualTo("O01");

        verify(mockProducer).sendTaskEvent(eventCaptor.capture());
        ErgonEvent capturedOrmEvent = eventCaptor.getValue();
        assertThat(capturedOrmEvent.getMessageType()).isEqualTo("ORM");
        assertThat(capturedOrmEvent.getTriggerType()).isEqualTo("O01");
    }

    @Test
    void testProcessAdtA40MergeMessage() {
        String hl7A40 = "MSH|^~\\&|PAS|HOSPITAL|HIE|HIE_IM|20260907160000||ADT^A40|MSG-A40-001|P|2.4\r" +
                "PID|1||PAT-NEW||SURVIVING^PATIENT||19700101|M\r" +
                "MRG|PAT-OLD^^^HOSPITAL^MR||||||MERGED^PATIENT\r";

        AdtProcessingResult result = transformer.processAdtMessage(hl7A40);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getTriggerEvent()).isEqualTo("A40");
        assertThat(result.getTask()).isNotNull();
    }

    @Test
    @DisplayName("REC-001: Petasos publish failure returns AE NACK to prevent dual-write data loss")
    void testPetasosPublishFailureReturnsAeNack() throws Exception {
        String hl7 = "MSH|^~\\&|APP|FAC|HIE|HIE_IM|20260907100000||ADT^A01|MSG-FAIL-001|P|2.4\r" +
                "PID|1||PAT111||TEST^USER||19900101|M\r" +
                "PV1|1|I|WARD1\r";

        TaskEventProducerService failingProducer = mock(TaskEventProducerService.class);
        doThrow(new RuntimeException("Petasos Artemis broker connection failure"))
                .when(failingProducer).sendTaskEvent(any(ErgonEvent.class));

        transformer.setTaskEventProducerService(failingProducer);

        AdtProcessingResult result = transformer.processAdtMessage(hl7);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getAckMessage()).contains("MSA|AE|MSG-FAIL-001");
        assertThat(result.getErrorMessage()).contains("Petasos Artemis broker connection failure");
    }

    @Test
    void testProcessInvalidMessageReturnsFailure() {
        AdtProcessingResult blankResult = transformer.processAdtMessage("");
        assertThat(blankResult.isSuccess()).isFalse();
        assertThat(blankResult.getAckMessage()).contains("MSA|AE");

        AdtProcessingResult malformedResult = transformer.processAdtMessage("GARBAGE DATA NOT HL7");
        assertThat(malformedResult.isSuccess()).isFalse();
        assertThat(malformedResult.getAckMessage()).contains("MSA|AE");
    }
}
