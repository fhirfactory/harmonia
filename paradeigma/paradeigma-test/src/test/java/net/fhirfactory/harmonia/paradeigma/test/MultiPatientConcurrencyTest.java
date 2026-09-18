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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class MultiPatientConcurrencyTest {

    private MllpServer mockGateway;
    private int gatewayPort;

    @BeforeEach
    void setUp() throws IOException {
        mockGateway = new MllpServer(0, Hl7AckHandler::generateAcceptAck);
        mockGateway.start();
        gatewayPort = mockGateway.getPort();
    }

    @AfterEach
    void tearDown() {
        if (mockGateway != null) mockGateway.stop();
    }

    @Test
    @DisplayName("Run 10 concurrent multi-patient journeys with zero correlation crosstalk")
    void testConcurrentPatientJourneys() throws InterruptedException, ExecutionException {
        int concurrency = 10;
        ExecutorService pool = Executors.newFixedThreadPool(concurrency);
        List<Future<Boolean>> futures = new ArrayList<>();

        Set<String> patientIds = Collections.newSetFromMap(new ConcurrentHashMap<>());
        Set<String> controlIds = Collections.newSetFromMap(new ConcurrentHashMap<>());
        AtomicInteger totalMessages = new AtomicInteger(0);

        for (int i = 0; i < concurrency; i++) {
            final int index = i;
            futures.add(pool.submit(() -> {
                SyntheticPatientGenerator pGen = new SyntheticPatientGenerator(1000L + index * 37L);
                SyntheticOrderGenerator oGen = new SyntheticOrderGenerator(2000L + index * 37L);
                SyntheticResultGenerator rGen = new SyntheticResultGenerator(3000L + index * 37L);

                PatientProfile patient = pGen.generatePatient("PAT-CONCUR-" + index);
                VisitProfile visit = pGen.generateVisit(patient);
                patientIds.add(patient.getPatientId());

                try (MllpClient client = new MllpClient("127.0.0.1", gatewayPort, 2000, 2000)) {
                    // 1. A04
                    String a04 = Hl7MessageBuilders.buildAdtA04(patient, visit);
                    sendAndVerify(client, a04, controlIds, totalMessages);

                    // 2. A01
                    String a01 = Hl7MessageBuilders.buildAdtA01(patient, visit);
                    sendAndVerify(client, a01, controlIds, totalMessages);

                    // 3. Lab Order
                    OrderProfile labOrder = oGen.generateLabOrder(patient, visit, "CBC");
                    String ormLab = Hl7MessageBuilders.buildOrmO01(labOrder, patient, visit);
                    sendAndVerify(client, ormLab, controlIds, totalMessages);

                    // 4. Lab Result
                    ResultProfile labRes = rGen.generateResult(labOrder);
                    String oruLab = Hl7MessageBuilders.buildOruR01(labRes, patient, visit);
                    sendAndVerify(client, oruLab, controlIds, totalMessages);

                    // 5. Rad Order
                    OrderProfile radOrder = oGen.generateImagingOrder(patient, visit, "XR_CHEST");
                    String ormRad = Hl7MessageBuilders.buildOrmO01(radOrder, patient, visit);
                    sendAndVerify(client, ormRad, controlIds, totalMessages);

                    // 6. Rad Result
                    ResultProfile radRes = rGen.generateResult(radOrder);
                    String oruRad = Hl7MessageBuilders.buildOruR01(radRes, patient, visit);
                    sendAndVerify(client, oruRad, controlIds, totalMessages);

                    // 7. A03 Discharge
                    String a03 = Hl7MessageBuilders.buildAdtA03(patient, visit);
                    sendAndVerify(client, a03, controlIds, totalMessages);

                    return true;
                } catch (Exception e) {
                    return false;
                }
            }));
        }

        for (Future<Boolean> f : futures) {
            assertThat(f.get()).isTrue();
        }
        pool.shutdown();

        assertThat(patientIds).hasSize(concurrency);
        assertThat(totalMessages.get()).isEqualTo(concurrency * 7);
        assertThat(controlIds).hasSize(concurrency * 7); // 100% unique MSH-10s
        assertThat(mockGateway.getReceivedCount()).isEqualTo(concurrency * 7);
    }

    private void sendAndVerify(MllpClient client, String hl7, Set<String> controlIds, AtomicInteger count) {
        String ctrlId = Hl7Parsers.extractMessageControlId(hl7);
        controlIds.add(ctrlId);
        String ack = client.sendAndReceive(hl7);
        AckResult result = Hl7AckHandler.parseAck(ack);
        assertThat(result.isAccept()).isTrue();
        assertThat(result.matchesControlId(ctrlId)).isTrue();
        count.incrementAndGet();
    }
}
