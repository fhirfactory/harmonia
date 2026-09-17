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
import net.fhirfactory.harmonia.mllpgateway.service.CommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultCommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultProvenanceService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultTaskService;
import net.fhirfactory.harmonia.mllpgateway.service.ProvenanceService;
import net.fhirfactory.harmonia.mllpgateway.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IncomingOrmMessageProcessorTest {

    private TaskService taskService;
    private CommunicationService communicationService;
    private ProvenanceService provenanceService;
    private TaskEventProducerService taskEventProducerService;
    private IncomingOrmMessageProcessor processor;

    @BeforeEach
    void setUp() {
        taskService = new DefaultTaskService();
        communicationService = new DefaultCommunicationService();
        provenanceService = new DefaultProvenanceService();
        taskEventProducerService = mock(TaskEventProducerService.class);
        MllpConfig config = new MllpConfig("0.0.0.0", 2104, true);

        processor = new IncomingOrmMessageProcessor(taskService, communicationService, provenanceService, taskEventProducerService, config);
    }

    @Test
    @DisplayName("Process valid ORM^O01 Lab Order")
    void testProcessOrmLabOrder() throws Exception {
        String ormHl7 = "MSH|^~\\&|PARADEIGMA_EMR|FACILITY|HARMONIA|HIE|20260915120000||ORM^O01|MSG-ORM-101|P|2.4\r" +
                "PID|1||PAT-101^^^MRN||Smith^John\r" +
                "PV1|1|I|WARD-3A^301^A\r" +
                "ORC|NW|ORD-1001|||IP||^^^R||20260915120000\r" +
                "OBR|1|ORD-1001||CBC^Complete Blood Count^LN\r";

        OrmProcessingResult result = processor.processOrmMessage(ormHl7);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessageControlId()).isEqualTo("MSG-ORM-101");
        assertThat(result.getPlacerOrderNumber()).isEqualTo("ORD-1001");
        assertThat(result.getUniversalServiceId()).isEqualTo("CBC");
        assertThat(result.getPatientId()).isEqualTo("PAT-101");
        assertThat(result.getAckMessage()).contains("MSA|AA|MSG-ORM-101");

        verify(taskEventProducerService, times(1)).sendTaskEvent(any());
    }

    @Test
    @DisplayName("REC-001: Petasos publish failure returns AE NACK to prevent dual-write data loss")
    void testPetasosPublishFailureReturnsAeNack() throws Exception {
        String ormHl7 = "MSH|^~\\&|PARADEIGMA_EMR|FACILITY|HARMONIA|HIE|20260915120000||ORM^O01|MSG-ORM-101|P|2.4\r" +
                "PID|1||PAT-101^^^MRN||Smith^John\r" +
                "PV1|1|I|WARD-3A^301^A\r" +
                "ORC|NW|ORD-1001|||IP||^^^R||20260915120000\r" +
                "OBR|1|ORD-1001||CBC^Complete Blood Count^LN\r";

        TaskEventProducerService failingProducer = mock(TaskEventProducerService.class);
        doThrow(new RuntimeException("Petasos Artemis connection error"))
                .when(failingProducer).sendTaskEvent(any());

        IncomingOrmMessageProcessor failingProcessor = new IncomingOrmMessageProcessor(taskService, communicationService, provenanceService, failingProducer, new MllpConfig("0.0.0.0", 2104, true));

        OrmProcessingResult result = failingProcessor.processOrmMessage(ormHl7);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getAckMessage()).contains("MSA|AE|MSG-ORM-101");
        assertThat(result.getErrorMessage()).contains("Petasos Artemis connection error");
    }

    @Test
    @DisplayName("Process blank payload returns AE error")
    void testProcessBlankPayload() {
        OrmProcessingResult result = processor.processOrmMessage("");
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getAckMessage()).contains("MSA|AE");
    }
}
