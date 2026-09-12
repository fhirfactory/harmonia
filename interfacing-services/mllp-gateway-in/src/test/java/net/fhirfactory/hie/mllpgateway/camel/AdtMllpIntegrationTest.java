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

package net.fhirfactory.hie.mllpgateway.camel;

import net.fhirfactory.hie.mllpgateway.config.MllpConfig;
import net.fhirfactory.hie.mllpgateway.hl7.IncomingAdtMessageProcessor;
import net.fhirfactory.hie.mllpgateway.service.CommunicationService;
import net.fhirfactory.hie.mllpgateway.service.DefaultCommunicationService;
import net.fhirfactory.hie.mllpgateway.service.DefaultProvenanceService;
import net.fhirfactory.hie.mllpgateway.service.DefaultTaskService;
import net.fhirfactory.hie.mllpgateway.service.ProvenanceService;
import net.fhirfactory.hie.mllpgateway.service.TaskService;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.Provenance;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AdtMllpIntegrationTest {

    private static final String MLLP_HOST = "127.0.0.1";
    private static final int MLLP_PORT = 25755;

    private static TaskService taskService;
    private static CommunicationService communicationService;
    private static ProvenanceService provenanceService;
    private static CamelContext camelContext;
    private static ProducerTemplate producerTemplate;

    @BeforeAll
    static void initCamel() throws Exception {
        taskService = new DefaultTaskService();
        communicationService = new DefaultCommunicationService();
        provenanceService = new DefaultProvenanceService();
        IncomingAdtMessageProcessor transformer = new IncomingAdtMessageProcessor(taskService, communicationService, provenanceService);
        IncomingAdtMessageProcessorWrapper processor = new IncomingAdtMessageProcessorWrapper(transformer);
        MllpConfig mllpConfig = new MllpConfig(MLLP_HOST, MLLP_PORT, true);
        IncomingAdtMessageMllpRouteBuilder routeBuilder = new IncomingAdtMessageMllpRouteBuilder(mllpConfig, processor);

        camelContext = new DefaultCamelContext();
        camelContext.addRoutes(routeBuilder);
        camelContext.start();

        producerTemplate = camelContext.createProducerTemplate();
    }

    @AfterAll
    static void stopCamel() throws Exception {
        if (producerTemplate != null) {
            producerTemplate.stop();
        }
        if (camelContext != null) {
            camelContext.stop();
            camelContext.close();
        }
    }

    @Test
    @DisplayName("Direct Camel route creates Task and returns HL7 ACK")
    void testDirectAdtRouteProcessing() {
        String hl7A01 = "MSH|^~\\&|HOSPITAL_A|FACILITY_1|HIE|HIE_IM|20260907150000||ADT^A01|MSG-DIRECT-001|P|2.4\r" +
                "EVN|A01|20260907150000\r" +
                "PID|1||PAT99911^^^HOSPITAL^MR||CLARK^MICHAEL||19881010|M\r" +
                "PV1|1|I|ICU^RM05^BED1^HOSPITAL||||DOC09^MILLER^DAVID^^DR|||||||||||V888777\r";

        Object response = producerTemplate.requestBody("direct:adt-events", hl7A01);

        assertThat(response).isNotNull();
        String ackString = response.toString();
        assertThat(ackString).contains("MSA|AA|MSG-DIRECT-001");

        Optional<Task> createdTask = taskService.getById("MSG-DIRECT-001");
        assertThat(createdTask).isPresent();
        assertThat(createdTask.get().getDescription()).contains("MICHAEL CLARK");
        assertThat(createdTask.get().getFor().getReference()).isEqualTo("Patient/PAT99911");
        assertThat(createdTask.get().getStatus()).isEqualTo(Task.TaskStatus.REQUESTED);

        Optional<Communication> createdComm = communicationService.getById("comm-MSG-DIRECT-001");
        assertThat(createdComm).isPresent();
        assertThat(createdComm.get().getSubject().getReference()).isEqualTo("Patient/PAT99911");
        assertThat(createdComm.get().getPayloadFirstRep().getContent()).isInstanceOf(org.hl7.fhir.r5.model.Attachment.class);

        Optional<Provenance> createdProv = provenanceService.getById("prov-MSG-DIRECT-001");
        assertThat(createdProv).isPresent();
        assertThat(createdProv.get().getTargetFirstRep().getReference()).isEqualTo("Task/MSG-DIRECT-001");
        assertThat(createdProv.get().getEntityFirstRep().getWhat().getReference()).isEqualTo("Communication/comm-MSG-DIRECT-001");
    }

    @Test
    @DisplayName("MLLP TCP Socket communication receives HL7 ADT message, saves Task, and sends HL7 ACK")
    void testMllpTcpSocketAdtReceptionAndAck() throws Exception {
        String hl7A08 = "MSH|^~\\&|HOSPITAL_B|FACILITY_2|HIE|HIE_IM|20260907153000||ADT^A08|MSG-MLLP-SOCKET-002|P|2.4\r" +
                "EVN|A08|20260907153000\r" +
                "PID|1||PAT77733^^^HOSPITAL^MR||ANDERSON^KAREN||19950412|F\r" +
                "PV1|1|E|ED^BAY02^^HOSPITAL||||DOC10^WHITE^SARAH^^DR|||||||||||V666555\r";

        // Connect to Camel MLLP server on port 25755
        try (Socket socket = new Socket(MLLP_HOST, MLLP_PORT)) {
            socket.setSoTimeout(5000);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            // Send MLLP framed message: <VT> payload <FS><CR>
            byte[] mllpFrame = wrapInMllpFrame(hl7A08);
            out.write(mllpFrame);
            out.flush();

            // Read MLLP framed ACK
            String ack = readMllpFrame(in);
            assertThat(ack).isNotEmpty();
            assertThat(ack).contains("MSA|AA|MSG-MLLP-SOCKET-002");

            // Verify Task was created
            Optional<Task> createdTask = taskService.getById("MSG-MLLP-SOCKET-002");
            assertThat(createdTask).isPresent();
            assertThat(createdTask.get().getDescription()).contains("KAREN ANDERSON");
            assertThat(createdTask.get().getFor().getReference()).isEqualTo("Patient/PAT77733");
            assertThat(createdTask.get().getStatus()).isEqualTo(Task.TaskStatus.INPROGRESS);

            // Verify Communication was created
            Optional<Communication> createdComm = communicationService.getById("comm-MSG-MLLP-SOCKET-002");
            assertThat(createdComm).isPresent();
            assertThat(createdComm.get().getSubject().getReference()).isEqualTo("Patient/PAT77733");

            // Verify Provenance was created
            Optional<Provenance> createdProv = provenanceService.getById("prov-MSG-MLLP-SOCKET-002");
            assertThat(createdProv).isPresent();
            assertThat(createdProv.get().getTargetFirstRep().getReference()).isEqualTo("Task/MSG-MLLP-SOCKET-002");
            assertThat(createdProv.get().getEntityFirstRep().getWhat().getReference()).isEqualTo("Communication/comm-MSG-MLLP-SOCKET-002");
        }
    }

    private byte[] wrapInMllpFrame(String message) {
        byte[] payload = message.getBytes(StandardCharsets.UTF_8);
        byte[] frame = new byte[payload.length + 3];
        frame[0] = 0x0B; // <VT>
        System.arraycopy(payload, 0, frame, 1, payload.length);
        frame[frame.length - 2] = 0x1C; // <FS>
        frame[frame.length - 1] = 0x0D; // <CR>
        return frame;
    }

    private String readMllpFrame(InputStream in) throws Exception {
        StringBuilder sb = new StringBuilder();
        int b;
        boolean started = false;
        while ((b = in.read()) != -1) {
            if (b == 0x0B) {
                started = true;
                continue;
            }
            if (started) {
                if (b == 0x1C) {
                    // Check next byte for CR
                    in.read();
                    break;
                }
                sb.append((char) b);
            }
        }
        return sb.toString();
    }
}
