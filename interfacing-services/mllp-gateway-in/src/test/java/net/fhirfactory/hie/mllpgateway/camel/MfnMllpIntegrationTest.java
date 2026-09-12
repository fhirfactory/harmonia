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
import net.fhirfactory.hie.mllpgateway.hl7.IncomingMfnMessageProcessor;
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

class MfnMllpIntegrationTest {

    private static final String MLLP_HOST = "127.0.0.1";
    private static final int MLLP_MFN_PORT = 25765;

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
        IncomingMfnMessageProcessor transformer = new IncomingMfnMessageProcessor(taskService, communicationService, provenanceService);
        IncomingMfnMessageProcessorWrapper processor = new IncomingMfnMessageProcessorWrapper(transformer);
        MllpConfig mllpConfig = new MllpConfig("gw-test", MLLP_HOST, 25755, MLLP_MFN_PORT, true);
        IncomingMfnMessageMllpRouteBuilder routeBuilder = new IncomingMfnMessageMllpRouteBuilder(mllpConfig, processor);

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
    @DisplayName("Direct Camel route creates Task and returns HL7 ACK for MFN message")
    void testDirectMfnRouteProcessing() {
        String hl7Mfn = "MSH|^~\\&|STAFF_SYS|FACILITY_1|HIE|HIE_DEST|20260910120000||MFN^M02|MSG-DIRECT-MFN-01|P|2.4\r" +
                "MFI|PRA^Practitioner Master File|STAFF_SYS|UPD|20260910120000|20260910120000|NE\r" +
                "MFE|MUP|ENTRY-01|20260910120000|DOC9911^CLARK^MICHAEL|CE\r" +
                "STF|DOC9911^CLARK^MICHAEL||CLARK^MICHAEL||M|19800510|Y|CARD|||||||||||||||||||||||||||||\r";

        Object response = producerTemplate.requestBody("direct:mfn-events", hl7Mfn);

        assertThat(response).isNotNull();
        String ackString = response.toString();
        assertThat(ackString).contains("MSA|AA|MSG-DIRECT-MFN-01");

        Optional<Task> createdTask = taskService.getById("MSG-DIRECT-MFN-01");
        assertThat(createdTask).isPresent();
        assertThat(createdTask.get().getDescription()).contains("MICHAEL CLARK");
        assertThat(createdTask.get().getFor().getReference()).isEqualTo("Practitioner/DOC9911");

        Optional<Communication> createdComm = communicationService.getById("comm-MSG-DIRECT-MFN-01");
        assertThat(createdComm).isPresent();
        assertThat(createdComm.get().getSubject().getReference()).isEqualTo("Practitioner/DOC9911");

        Optional<Provenance> createdProv = provenanceService.getById("prov-MSG-DIRECT-MFN-01");
        assertThat(createdProv).isPresent();
        assertThat(createdProv.get().getTargetFirstRep().getReference()).isEqualTo("Task/MSG-DIRECT-MFN-01");
    }

    @Test
    @DisplayName("MLLP TCP Socket communication receives HL7 MFN message, saves Task, and sends HL7 ACK")
    void testMllpTcpSocketMfnReceptionAndAck() throws Exception {
        String hl7Mfn = "MSH|^~\\&|STAFF_SYS|FACILITY_2|HIE|HIE_DEST|20260910123000||MFN^M02|MSG-MLLP-MFN-02|P|2.4\r" +
                "MFI|PRA^Practitioner Master File|STAFF_SYS|UPD|20260910123000|20260910123000|NE\r" +
                "MFE|MAD|ENTRY-02|20260910123000|DOC7733^ANDERSON^KAREN|CE\r" +
                "STF|DOC7733^ANDERSON^KAREN||ANDERSON^KAREN||F|19850412|Y|NEURO|||||||||||||||||||||||||||||\r";

        // Connect to Camel MLLP server on MFN port 25765
        try (Socket socket = new Socket(MLLP_HOST, MLLP_MFN_PORT)) {
            socket.setSoTimeout(5000);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            // Send MLLP framed message: <VT> payload <FS><CR>
            byte[] mllpFrame = wrapInMllpFrame(hl7Mfn);
            out.write(mllpFrame);
            out.flush();

            // Read MLLP framed ACK
            String ack = readMllpFrame(in);
            assertThat(ack).isNotEmpty();
            assertThat(ack).contains("MSA|AA|MSG-MLLP-MFN-02");

            // Verify Task was created
            Optional<Task> createdTask = taskService.getById("MSG-MLLP-MFN-02");
            assertThat(createdTask).isPresent();
            assertThat(createdTask.get().getDescription()).contains("KAREN ANDERSON");
            assertThat(createdTask.get().getFor().getReference()).isEqualTo("Practitioner/DOC7733");

            // Verify Communication was created
            Optional<Communication> createdComm = communicationService.getById("comm-MSG-MLLP-MFN-02");
            assertThat(createdComm).isPresent();
            assertThat(createdComm.get().getSubject().getReference()).isEqualTo("Practitioner/DOC7733");

            // Verify Provenance was created
            Optional<Provenance> createdProv = provenanceService.getById("prov-MSG-MLLP-MFN-02");
            assertThat(createdProv).isPresent();
            assertThat(createdProv.get().getTargetFirstRep().getReference()).isEqualTo("Task/MSG-MLLP-MFN-02");
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
                    in.read(); // Read CR
                    break;
                }
                sb.append((char) b);
            }
        }
        return sb.toString();
    }
}
