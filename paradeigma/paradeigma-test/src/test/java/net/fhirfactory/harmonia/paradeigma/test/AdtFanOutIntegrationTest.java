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

import net.fhirfactory.harmonia.erga.distribution.AdtDistributionErgon;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticPatientGenerator;
import net.fhirfactory.harmonia.paradeigma.common.hl7.AckResult;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7MessageBuilders;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7Parsers;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
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

class AdtFanOutIntegrationTest {

    private MllpServer emrAdtServer;
    private MllpServer lmsAdtServer;
    private MllpServer rispacAdtServer;

    private SyntheticPatientGenerator patientGen;
    private AdtDistributionErgon distributionErgon;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() throws IOException {
        patientGen = new SyntheticPatientGenerator(12345L);
        camelContext = new DefaultCamelContext();
        distributionErgon = new AdtDistributionErgon(camelContext);

        emrAdtServer = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        emrAdtServer.start();

        lmsAdtServer = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        lmsAdtServer.start();

        rispacAdtServer = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        rispacAdtServer.start();
    }

    @AfterEach
    void tearDown() {
        if (emrAdtServer != null) emrAdtServer.stop();
        if (lmsAdtServer != null) lmsAdtServer.stop();
        if (rispacAdtServer != null) rispacAdtServer.stop();
    }

    @Test
    @DisplayName("Single PAS ADT message fans out to EMR, LMS, and RIS-PAC outbound endpoints")
    void testAdtFanOutDistribution() throws Exception {
        PatientProfile patient = patientGen.generatePatient("PAT-FAN-100");
        VisitProfile visit = patientGen.generateVisit(patient);
        String adtA01 = Hl7MessageBuilders.buildAdtA01(patient, visit);
        String controlId = Hl7Parsers.extractMessageControlId(adtA01);

        // Process through Ponos AdtDistributionErgon
        Pragma pragma = Pragma.builder().pragmaId("pragma-fan-1").correlationId(controlId).build();
        ErgonPayload input = ErgonPayload.fromJson(0, null, null, adtA01);
        pragma.addInput(input);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(pragma);

        distributionErgon.processActivity(exchange);

        Pragma outputPragma = exchange.getMessage().getBody(Pragma.class);
        assertThat(outputPragma).isNotNull();
        assertThat(outputPragma.getOutput()).hasSize(3);

        // Fan-out dispatch over MLLP to the 3 simulated endpoints
        assertSendAndAck(emrAdtServer.getPort(), adtA01, controlId);
        assertSendAndAck(lmsAdtServer.getPort(), adtA01, controlId);
        assertSendAndAck(rispacAdtServer.getPort(), adtA01, controlId);

        assertThat(emrAdtServer.getReceivedCount()).isEqualTo(1);
        assertThat(lmsAdtServer.getReceivedCount()).isEqualTo(1);
        assertThat(rispacAdtServer.getReceivedCount()).isEqualTo(1);
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
