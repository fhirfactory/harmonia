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

import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticOrderGenerator;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticPatientGenerator;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticResultGenerator;
import net.fhirfactory.harmonia.paradeigma.common.hl7.AckResult;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7MessageBuilders;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7Parsers;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.ResultProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-End System Test verifying representative clinical workflows across all 9 MLLP interfaces
 * (PD-01 through PD-09) with strict MLLP framing, ACK handling, and correlation verification.
 */
class EndToEndPatientJourneyTest {

    // Simulated Harmonia Inbound Gateways (PD-01, PD-02, PD-03, PD-04)
    private MllpServer harmoniaPasAdtIn;   // PD-01
    private MllpServer harmoniaLmsOruIn;   // PD-02
    private MllpServer harmoniaRispacOruIn;// PD-03
    private MllpServer harmoniaEmrOrmIn;   // PD-04

    // Simulated Healthcare Applications Inbound Interfaces (PD-05, PD-06, PD-07, PD-08, PD-09)
    private MllpServer emrAdtOut;    // PD-05 (EMR receiving ADT)
    private MllpServer lmsAdtOut;    // PD-06 (LMS receiving ADT)
    private MllpServer rispacAdtOut; // PD-07 (RIS-PAC receiving ADT)
    private MllpServer lmsOrmOut;    // PD-08 (LMS receiving Lab ORM)
    private MllpServer rispacOrmOut; // PD-09 (RIS-PAC receiving Rad ORM)

    private SyntheticPatientGenerator patientGen;
    private SyntheticOrderGenerator orderGen;
    private SyntheticResultGenerator resultGen;

    @BeforeEach
    void setUp() throws IOException {
        patientGen = new SyntheticPatientGenerator(12345L);
        orderGen = new SyntheticOrderGenerator(12345L);
        resultGen = new SyntheticResultGenerator(12345L);

        // Harmonia Inbound Gateways (return AA ACKs)
        harmoniaPasAdtIn = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        harmoniaPasAdtIn.start();

        harmoniaLmsOruIn = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        harmoniaLmsOruIn.start();

        harmoniaRispacOruIn = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        harmoniaRispacOruIn.start();

        harmoniaEmrOrmIn = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        harmoniaEmrOrmIn.start();

        // Simulator Inbound Interfaces (return AA ACKs)
        emrAdtOut = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        emrAdtOut.start();

        lmsAdtOut = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        lmsAdtOut.start();

        rispacAdtOut = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        rispacAdtOut.start();

        lmsOrmOut = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        lmsOrmOut.start();

        rispacOrmOut = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        rispacOrmOut.start();
    }

    @AfterEach
    void tearDown() {
        stopQuietly(harmoniaPasAdtIn);
        stopQuietly(harmoniaLmsOruIn);
        stopQuietly(harmoniaRispacOruIn);
        stopQuietly(harmoniaEmrOrmIn);
        stopQuietly(emrAdtOut);
        stopQuietly(lmsAdtOut);
        stopQuietly(rispacAdtOut);
        stopQuietly(lmsOrmOut);
        stopQuietly(rispacOrmOut);
    }

    @Test
    @DisplayName("Complete End-to-End Patient Journey across all 9 Interfaces")
    void testCompletePatientJourney() {
        PatientProfile patient = patientGen.generatePatient("PAT-E2E-001");
        VisitProfile visit = patientGen.generateVisit(patient);

        // 1. PAS -> Harmonia (PD-01 ADT^A04 Register)
        String a04 = Hl7MessageBuilders.buildAdtA04(patient, visit);
        String a04ControlId = Hl7Parsers.extractMessageControlId(a04);
        assertTransmitAndAck(harmoniaPasAdtIn.getPort(), a04, a04ControlId);

        // 2. Harmonia Fan-out -> EMR, LMS, RIS-PAC (PD-05, PD-06, PD-07)
        assertTransmitAndAck(emrAdtOut.getPort(), a04, a04ControlId);
        assertTransmitAndAck(lmsAdtOut.getPort(), a04, a04ControlId);
        assertTransmitAndAck(rispacAdtOut.getPort(), a04, a04ControlId);

        // 3. PAS -> Harmonia (PD-01 ADT^A01 Admit)
        String a01 = Hl7MessageBuilders.buildAdtA01(patient, visit);
        String a01ControlId = Hl7Parsers.extractMessageControlId(a01);
        assertTransmitAndAck(harmoniaPasAdtIn.getPort(), a01, a01ControlId);

        // 4. Harmonia Fan-out -> EMR, LMS, RIS-PAC (PD-05, PD-06, PD-07)
        assertTransmitAndAck(emrAdtOut.getPort(), a01, a01ControlId);
        assertTransmitAndAck(lmsAdtOut.getPort(), a01, a01ControlId);
        assertTransmitAndAck(rispacAdtOut.getPort(), a01, a01ControlId);

        // 5. EMR -> Harmonia (PD-04 ORM^O01 Lab Order CBC)
        OrderProfile labOrder = orderGen.generateLabOrder(patient, visit, "CBC");
        String labOrm = Hl7MessageBuilders.buildOrmO01(labOrder, patient, visit);
        String labOrmControlId = Hl7Parsers.extractMessageControlId(labOrm);
        assertTransmitAndAck(harmoniaEmrOrmIn.getPort(), labOrm, labOrmControlId);

        // 6. Harmonia Routes ORM -> LMS (PD-08)
        assertTransmitAndAck(lmsOrmOut.getPort(), labOrm, labOrmControlId);

        // 7. LMS -> Harmonia (PD-02 ORU^R01 Lab Result)
        ResultProfile labResult = resultGen.generateResult(labOrder);
        String labOru = Hl7MessageBuilders.buildOruR01(labResult, patient, visit);
        String labOruControlId = Hl7Parsers.extractMessageControlId(labOru);
        assertTransmitAndAck(harmoniaLmsOruIn.getPort(), labOru, labOruControlId);

        // 8. EMR -> Harmonia (PD-04 ORM^O01 Imaging Order XR_CHEST)
        OrderProfile radOrder = orderGen.generateImagingOrder(patient, visit, "XR_CHEST");
        String radOrm = Hl7MessageBuilders.buildOrmO01(radOrder, patient, visit);
        String radOrmControlId = Hl7Parsers.extractMessageControlId(radOrm);
        assertTransmitAndAck(harmoniaEmrOrmIn.getPort(), radOrm, radOrmControlId);

        // 9. Harmonia Routes ORM -> RIS-PAC (PD-09)
        assertTransmitAndAck(rispacOrmOut.getPort(), radOrm, radOrmControlId);

        // 10. RIS-PAC -> Harmonia (PD-03 ORU^R01 Imaging Report)
        ResultProfile radResult = resultGen.generateResult(radOrder);
        String radOru = Hl7MessageBuilders.buildOruR01(radResult, patient, visit);
        String radOruControlId = Hl7Parsers.extractMessageControlId(radOru);
        assertTransmitAndAck(harmoniaRispacOruIn.getPort(), radOru, radOruControlId);

        // 11. PAS -> Harmonia (PD-01 ADT^A02 Transfer) & Fan-out (PD-05, PD-06, PD-07)
        String a02 = Hl7MessageBuilders.buildAdtA02(patient, visit, "WARD-4B", "401", "B");
        String a02ControlId = Hl7Parsers.extractMessageControlId(a02);
        assertTransmitAndAck(harmoniaPasAdtIn.getPort(), a02, a02ControlId);
        assertTransmitAndAck(emrAdtOut.getPort(), a02, a02ControlId);
        assertTransmitAndAck(lmsAdtOut.getPort(), a02, a02ControlId);
        assertTransmitAndAck(rispacAdtOut.getPort(), a02, a02ControlId);

        // 12. PAS -> Harmonia (PD-01 ADT^A03 Discharge) & Fan-out (PD-05, PD-06, PD-07)
        String a03 = Hl7MessageBuilders.buildAdtA03(patient, visit);
        String a03ControlId = Hl7Parsers.extractMessageControlId(a03);
        assertTransmitAndAck(harmoniaPasAdtIn.getPort(), a03, a03ControlId);
        assertTransmitAndAck(emrAdtOut.getPort(), a03, a03ControlId);
        assertTransmitAndAck(lmsAdtOut.getPort(), a03, a03ControlId);
        assertTransmitAndAck(rispacAdtOut.getPort(), a03, a03ControlId);

        // Verify total message flow counts
        assertThat(harmoniaPasAdtIn.getReceivedCount()).isEqualTo(4); // A04, A01, A02, A03
        assertThat(harmoniaEmrOrmIn.getReceivedCount()).isEqualTo(2); // Lab ORM, Rad ORM
        assertThat(harmoniaLmsOruIn.getReceivedCount()).isEqualTo(1); // Lab ORU
        assertThat(harmoniaRispacOruIn.getReceivedCount()).isEqualTo(1); // Rad ORU

        assertThat(emrAdtOut.getReceivedCount()).isEqualTo(4);
        assertThat(lmsAdtOut.getReceivedCount()).isEqualTo(4);
        assertThat(rispacAdtOut.getReceivedCount()).isEqualTo(4);
        assertThat(lmsOrmOut.getReceivedCount()).isEqualTo(1);
        assertThat(rispacOrmOut.getReceivedCount()).isEqualTo(1);
    }

    private void assertTransmitAndAck(int port, String hl7Message, String expectedControlId) {
        try (MllpClient client = new MllpClient("127.0.0.1", port)) {
            String ack = client.sendAndReceive(hl7Message);
            assertThat(ack).isNotNull();
            AckResult result = Hl7AckHandler.parseAck(ack);
            assertThat(result.isAccept()).isTrue();
            assertThat(result.matchesControlId(expectedControlId)).isTrue();
        }
    }

    private void stopQuietly(MllpServer server) {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception ignored) {
            }
        }
    }
}
