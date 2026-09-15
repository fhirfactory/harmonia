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
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultCommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultTaskService;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IncomingMfnMessageProcessorTest {

    private DefaultTaskService taskService;
    private DefaultCommunicationService communicationService;
    private TaskEventProducerService taskEventProducerService;
    private IncomingMfnMessageProcessor transformer;

    private static final String FULL_MFN_M02 =
            "MSH|^~\\&|STAFF_APP|HOSPITAL_A|HIE_APP|HIE_DEST|20260910120000||MFN^M02|MSG-MFN-001|P|2.4\r" +
            "MFI|PRA^Practitioner Master File|STAFF_APP|UPD|20260910120000|20260910120000|NE\r" +
            "MFE|MUP|MFN-ENTRY-001|20260910120000|DOC-12345^SMITH^JOHN|CE\r" +
            "STF|DOC-12345^SMITH^JOHN|NPI-9876543210^^^NPI^NPI~DOC-12345^^^HOSP^MD~TAX-123^^^TAX^TAX|SMITH^JOHN^ROBERT^JR^DR^MD|PHY^Physician^HL70182|M|19750820|Y|CARD^Cardiology Department|MED^Internal Medicine|(555)555-1234^PRN^PH^^^555^5551234~(555)555-4321^WPN^FX~drsmith@hospital.org^NET^Internet^drsmith@hospital.org|100 MEDICAL PKWY^SUITE 300^METROPOLIS^NY^10001^USA^O|20200101000000|20301231235959||dr.smith@metropolishospital.org|||M^Married|Chief of Cardiology|CARDIO-CHIEF^Attending Physician|FT^Full Time|||||||||||207RC0000X^Cardiovascular Disease|USA^United States|||en^English^ISO639||2186-5^Not Hispanic or Latino\r" +
            "PRA|DOC-12345|CARD-GRP^Metro Cardiology Associates|MD^Medical Doctor|Y|CARD^Cardiology^HL70265~EP^Electrophysiology^HL70265|LIC-998877^MD^NY^20281231~DEA-123456^DEA^US^20270630|ADM^Admitting Privileges^HOSP|20050701\r" +
            "ORG|1|DEPT|ORG-CARD-01|DEPT|Metro Cardiology Clinic|200 HEALTH BLVD^^METROPOLIS^NY^10001^USA|(555)555-9000\r" +
            "AFF|1|American College of Cardiology|2400 N Street NW^Washington^DC^20037^USA|20100101^20301231\r" +
            "LAN|1|en^English^ISO639|1^Read^HL70403|1^Fluent^HL70404\r" +
            "LAN|2|es^Spanish^ISO639|3^Speak^HL70403|2^Good^HL70404\r" +
            "EDU|1|Cardiology|MD^Doctor of Medicine|20000520|Johns Hopkins University School of Medicine|733 N Broadway^Baltimore^MD^21205^USA\r" +
            "CER|1|BC-CARD^Board Certified in Cardiovascular Disease|CERT-887766|American Board of Internal Medicine\r" +
            "NTE|1|P|Specializes in interventional cardiac procedures and electrophysiology.";

    @BeforeEach
    void setUp() {
        taskService = new DefaultTaskService();
        taskService.clear();
        communicationService = new DefaultCommunicationService();
        communicationService.clear();
        taskEventProducerService = mock(TaskEventProducerService.class);
        transformer = new IncomingMfnMessageProcessor(taskService, communicationService, taskEventProducerService);
    }

    @Test
    @DisplayName("Process full MFN^M02 message successfully with Communication in Task.input and task event dispatch")
    void testProcessFullMfnMessage() throws Exception {
        MfnProcessingResult result = transformer.processMfnMessage(FULL_MFN_M02);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessageControlId()).isEqualTo("MSG-MFN-001");
        assertThat(result.getTriggerEvent()).isEqualTo("M02");
        assertThat(result.getPractitionerId()).isEqualTo("DOC-12345");
        assertThat(result.getPractitionerName()).contains("JOHN").contains("SMITH");
        assertThat(result.getAckMessage()).contains("MSA|AA|MSG-MFN-001");

        // Verify Topic
        Topic topic = result.getTopic();
        assertThat(topic).isNotNull();
        assertThat(topic.getDomain()).isEqualTo("Health");
        assertThat(topic.getModel()).isEqualTo("HL7");
        assertThat(topic.getModelVersion()).isEqualTo("2.4");
        assertThat(topic.getDataElement()).isEqualTo("MFN");
        assertThat(topic.getDataElementQualifier()).isEqualTo("M02");

        // Verify Communication
        Communication comm = result.getCommunication();
        assertThat(comm).isNotNull();
        assertThat(comm.getIdPart()).isEqualTo("comm-MSG-MFN-001");
        assertThat(comm.getStatus()).isEqualTo(Enumerations.EventStatus.COMPLETED);
        assertThat(comm.getSubject().getReference()).isEqualTo("Practitioner/DOC-12345");
        assertThat(comm.getSender().getDisplay()).contains("STAFF_APP @ HOSPITAL_A");
        assertThat(comm.getRecipientFirstRep().getDisplay()).isEqualTo("HIE MLLP Gateway");
        assertThat(communicationService.getById("comm-MSG-MFN-001")).isPresent();

        // Verify Task
        Task task = result.getTask();
        assertThat(task).isNotNull();
        assertThat(task.getIdPart()).isEqualTo("MSG-MFN-001");
        assertThat(task.getStatus()).isEqualTo(Task.TaskStatus.INPROGRESS); // MUP
        assertThat(task.getIntent()).isEqualTo(Task.TaskIntent.ORDER);
        assertThat(task.getPriority()).isEqualTo(Enumerations.RequestPriority.ROUTINE);
        assertThat(ErgonReasonEnum.hasReason(task, ErgonReasonEnum.HIE_SYNTHETIC_TASK)).isTrue();
        assertThat(task.getFor().getReference()).isEqualTo("Practitioner/DOC-12345");
        assertThat(task.getBasedOnFirstRep().getReference()).isEqualTo("Communication/comm-MSG-MFN-001");
        assertThat(taskService.getById("MSG-MFN-001")).isPresent();

        // Verify Communication as Task.input and contained
        assertThat(task.getContained()).hasSize(1);
        Resource contained = task.getContained().get(0);
        assertThat(contained).isInstanceOf(Communication.class);
        assertThat(task.getInput()).hasSize(1);
        Task.TaskInputComponent commInput = task.getInputFirstRep();
        assertThat(commInput.getType().getCodingFirstRep().getCode()).isEqualTo("input-communication");
        assertThat(((Reference) commInput.getValue()).getReference()).isEqualTo("#comm-MSG-MFN-001");

        // Verify TaskEvent dispatched
        ArgumentCaptor<ErgonEvent> eventCaptor = ArgumentCaptor.forClass(ErgonEvent.class);
        verify(taskEventProducerService, times(1)).sendTaskEvent(eventCaptor.capture());
        ErgonEvent event = eventCaptor.getValue();
        assertThat(event.getTaskId()).isEqualTo("MSG-MFN-001");
        assertThat(event.getAction()).isEqualTo("PROCESS");
        assertThat(event.getControlId()).isEqualTo("MSG-MFN-001");
        assertThat(event.getTopic()).isEqualTo(topic);
    }

    @Test
    @DisplayName("Empty or blank message returns error result and fallback NACK")
    void testProcessBlankMessage() {
        MfnProcessingResult result = transformer.processMfnMessage("   ");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).contains("Payload is blank");
        assertThat(result.getAckMessage()).contains("MSA|AE|UNKNOWN");
    }

    @Test
    @DisplayName("Constructors, getters, setters and delegation methods work as expected")
    void testConstructorsAndSetters() {
        IncomingMfnMessageProcessor p = new IncomingMfnMessageProcessor();
        p.setTaskService(taskService);
        p.setCommunicationService(communicationService);
        p.setTaskEventProducerService(taskEventProducerService);
        MllpConfig cfg = new MllpConfig("gw-1", "127.0.0.1", 2575, 2576, true);
        p.setMllpConfig(cfg);

        assertThat(p.getTaskService()).isEqualTo(taskService);
        assertThat(p.getCommunicationService()).isEqualTo(communicationService);
        assertThat(p.getTaskEventProducerService()).isEqualTo(taskEventProducerService);
        assertThat(p.getMllpConfig()).isEqualTo(cfg);
        assertThat(p.getExtractor()).isNotNull();
        assertThat(p.getCommunicationBuilder()).isNotNull();
        assertThat(p.getTaskBuilder()).isNotNull();
    }
}
