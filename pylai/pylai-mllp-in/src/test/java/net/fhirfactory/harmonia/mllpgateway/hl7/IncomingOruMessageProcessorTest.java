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

class IncomingOruMessageProcessorTest {

    private TaskService taskService;
    private CommunicationService communicationService;
    private ProvenanceService provenanceService;
    private TaskEventProducerService taskEventProducerService;
    private IncomingOruMessageProcessor processor;

    @BeforeEach
    void setUp() {
        taskService = new DefaultTaskService();
        communicationService = new DefaultCommunicationService();
        provenanceService = new DefaultProvenanceService();
        taskEventProducerService = mock(TaskEventProducerService.class);
        MllpConfig config = new MllpConfig("0.0.0.0", 2102, true);

        processor = new IncomingOruMessageProcessor(taskService, communicationService, provenanceService, taskEventProducerService, config);
    }

    @Test
    @DisplayName("Process valid ORU^R01 Lab Result")
    void testProcessOruLabResult() throws Exception {
        String oruHl7 = "MSH|^~\\&|PARADEIGMA_LMS|FACILITY|HARMONIA|HIE|20260915120000||ORU^R01|MSG-ORU-201|P|2.4\r" +
                "PID|1||PAT-102^^^MRN||Smith^John\r" +
                "PV1|1|I|WARD-3A^301^A\r" +
                "ORC|RE|ORD-1002|LMS-2002||CM||||20260915120000\r" +
                "OBR|1|ORD-1002|LMS-2002|CBC^Complete Blood Count^LN|||20260915115000|||||||||||||||20260915120000|||F\r" +
                "OBX|1|NM|718-7^Haemoglobin^LN||145.0|g/L|130-180|N|||F\r";

        OruProcessingResult result = processor.processOruMessage(oruHl7);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessageControlId()).isEqualTo("MSG-ORU-201");
        assertThat(result.getPlacerOrderNumber()).isEqualTo("ORD-1002");
        assertThat(result.getFillerOrderNumber()).isEqualTo("LMS-2002");
        assertThat(result.getUniversalServiceId()).isEqualTo("CBC");
        assertThat(result.getPatientId()).isEqualTo("PAT-102");
        assertThat(result.getAckMessage()).contains("MSA|AA|MSG-ORU-201");

        verify(taskEventProducerService, times(1)).sendTaskEvent(any());
    }

    @Test
    @DisplayName("REC-001: Petasos publish failure returns AE NACK to prevent dual-write data loss")
    void testPetasosPublishFailureReturnsAeNack() throws Exception {
        String oruHl7 = "MSH|^~\\&|PARADEIGMA_LMS|FACILITY|HARMONIA|HIE|20260915120000||ORU^R01|MSG-ORU-201|P|2.4\r" +
                "PID|1||PAT-102^^^MRN||Smith^John\r" +
                "PV1|1|I|WARD-3A^301^A\r" +
                "ORC|RE|ORD-1002|LMS-2002||CM||||20260915120000\r" +
                "OBR|1|ORD-1002|LMS-2002|CBC^Complete Blood Count^LN|||20260915115000|||||||||||||||20260915120000|||F\r" +
                "OBX|1|NM|718-7^Haemoglobin^LN||145.0|g/L|130-180|N|||F\r";

        TaskEventProducerService failingProducer = mock(TaskEventProducerService.class);
        doThrow(new RuntimeException("Petasos Artemis connection error"))
                .when(failingProducer).sendTaskEvent(any());

        IncomingOruMessageProcessor failingProcessor = new IncomingOruMessageProcessor(taskService, communicationService, provenanceService, failingProducer, new MllpConfig("0.0.0.0", 2102, true));

        OruProcessingResult result = failingProcessor.processOruMessage(oruHl7);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getAckMessage()).contains("MSA|AE|MSG-ORU-201");
        assertThat(result.getErrorMessage()).contains("Petasos Artemis connection error");
    }

    @Test
    @DisplayName("Process blank payload returns AE error")
    void testProcessBlankPayload() {
        OruProcessingResult result = processor.processOruMessage("");
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getAckMessage()).contains("MSA|AE");
    }
}
