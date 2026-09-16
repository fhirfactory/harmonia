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

package net.fhirfactory.harmonia.paradeigma.test;

import net.fhirfactory.harmonia.erga.order.routing.OrmRoutingErgon;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticOrderGenerator;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticPatientGenerator;
import net.fhirfactory.harmonia.paradeigma.common.hl7.AckResult;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7MessageBuilders;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7Parsers;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class OrmDeterministicRoutingTest {

    private MllpServer lmsOrmServer;     // PD-08 LMS-ORM-OUT (:2204)
    private MllpServer rispacOrmServer;  // PD-09 RISPAC-ORM-OUT (:2205)

    private SyntheticPatientGenerator patientGen;
    private SyntheticOrderGenerator orderGen;
    private OrmRoutingErgon routingErgon;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() throws IOException {
        patientGen = new SyntheticPatientGenerator(12345L);
        orderGen = new SyntheticOrderGenerator(12345L);
        camelContext = new DefaultCamelContext();
        routingErgon = new OrmRoutingErgon(camelContext);

        lmsOrmServer = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        lmsOrmServer.start();

        rispacOrmServer = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        rispacOrmServer.start();
    }

    @AfterEach
    void tearDown() {
        if (lmsOrmServer != null) lmsOrmServer.stop();
        if (rispacOrmServer != null) rispacOrmServer.stop();
    }

    @Test
    @DisplayName("Laboratory orders (CBC, ELEC, LFT) are routed strictly to LMS")
    void testLaboratoryRouting() throws Exception {
        PatientProfile patient = patientGen.generatePatient("PAT-ROUTING-LAB");
        VisitProfile visit = patientGen.generateVisit(patient);

        String[] labTests = {"CBC", "ELEC", "LFT", "GLU", "CRP"};
        for (String test : labTests) {
            OrderProfile order = orderGen.generateLabOrder(patient, visit, test);
            String ormHl7 = Hl7MessageBuilders.buildOrmO01(order, patient, visit);
            String controlId = Hl7Parsers.extractMessageControlId(ormHl7);

            Pragma pragma = Pragma.builder().pragmaId("pragma-lab-" + test).correlationId(controlId).build();
            pragma.addInput(ErgonPayload.fromJson(0, null, null, ormHl7));

            Exchange exchange = new DefaultExchange(camelContext);
            exchange.getIn().setBody(pragma);
            routingErgon.processActivity(exchange);

            assertThat(exchange.getMessage().getHeader("HIE_ROUTED_DESTINATION")).isEqualTo("LMS");
            assertThat(exchange.getMessage().getHeader("HIE_TARGET_QUEUE")).isEqualTo(OrmRoutingErgon.QUEUE_LMS_ORM);

            // Transmit to LMS endpoint and verify
            assertSendAndAck(lmsOrmServer.getPort(), ormHl7, controlId);
        }

        assertThat(lmsOrmServer.getReceivedCount()).isEqualTo(5);
        assertThat(rispacOrmServer.getReceivedCount()).isEqualTo(0); // Zero leakage
    }

    @Test
    @DisplayName("Diagnostic imaging orders (XR_CHEST, CT_HEAD, MRI_BRAIN) are routed strictly to RIS-PAC")
    void testImagingRouting() throws Exception {
        PatientProfile patient = patientGen.generatePatient("PAT-ROUTING-RAD");
        VisitProfile visit = patientGen.generateVisit(patient);

        String[] imagingStudies = {"XR_CHEST", "CT_HEAD", "MRI_BRAIN", "US_ABDOMEN"};
        for (String study : imagingStudies) {
            OrderProfile order = orderGen.generateImagingOrder(patient, visit, study);
            String ormHl7 = Hl7MessageBuilders.buildOrmO01(order, patient, visit);
            String controlId = Hl7Parsers.extractMessageControlId(ormHl7);

            Pragma pragma = Pragma.builder().pragmaId("pragma-rad-" + study).correlationId(controlId).build();
            pragma.addInput(ErgonPayload.fromJson(0, null, null, ormHl7));

            Exchange exchange = new DefaultExchange(camelContext);
            exchange.getIn().setBody(pragma);
            routingErgon.processActivity(exchange);

            assertThat(exchange.getMessage().getHeader("HIE_ROUTED_DESTINATION")).isEqualTo("RISPAC");
            assertThat(exchange.getMessage().getHeader("HIE_TARGET_QUEUE")).isEqualTo(OrmRoutingErgon.QUEUE_RIS_ORM);

            // Transmit to RIS-PAC endpoint and verify
            assertSendAndAck(rispacOrmServer.getPort(), ormHl7, controlId);
        }

        assertThat(rispacOrmServer.getReceivedCount()).isEqualTo(4);
        assertThat(lmsOrmServer.getReceivedCount()).isEqualTo(0); // Zero leakage
    }

    private void assertSendAndAck(int port, String message, String controlId) {
        try (MllpClient client = new MllpClient("127.0.0.1", port)) {
            String ack = client.sendAndReceive(message);
            AckResult result = Hl7AckHandler.parseAck(ack);
            assertThat(result.isAccept()).isTrue();
            assertThat(result.matchesControlId(controlId)).isTrue();
        }
    }
}
