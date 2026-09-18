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

package net.fhirfactory.harmonia.paradeigma.pas;

import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.pas.config.PasConfig;
import net.fhirfactory.harmonia.paradeigma.pas.service.PasPatientLifecycleManager;
import net.fhirfactory.harmonia.paradeigma.pas.service.PasService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class PasServiceTest {

    private MllpServer mockHarmonia;
    private int mockPort;
    private PasService pasService;
    private PasPatientLifecycleManager lifecycleManager;

    @BeforeEach
    void setUp() throws IOException {
        mockHarmonia = new MllpServer(0, rawHl7 -> Hl7AckHandler.generateAcceptAck(rawHl7));
        mockHarmonia.start();
        mockPort = mockHarmonia.getPort();

        PasConfig config = new PasConfig();
        config.setHarmoniaHost("127.0.0.1");
        config.setHarmoniaPort(mockPort);

        lifecycleManager = new PasPatientLifecycleManager(12345L);
        pasService = new PasService(config, lifecycleManager);
    }

    @AfterEach
    void tearDown() {
        if (pasService != null) pasService.close();
        if (mockHarmonia != null) mockHarmonia.stop();
    }

    @Test
    @DisplayName("PAS sends A04, A01, A02, A08, A03 to mock Harmonia and receives AA ACKs")
    void testSequentialAdtLifecycle() {
        // 1. Register A04
        String a04 = lifecycleManager.registerPatient(null);
        ManualTriggerResponse r1 = pasService.sendAdtMessage(a04);
        assertThat(r1.isSuccess()).isTrue();
        assertThat(r1.getAckCode()).isEqualTo("AA");

        // 2. Admit A01
        String patientId = lifecycleManager.getRegisteredPatients().keySet().iterator().next();
        String a01 = lifecycleManager.admitPatient(patientId);
        ManualTriggerResponse r2 = pasService.sendAdtMessage(a01);
        assertThat(r2.isSuccess()).isTrue();

        // 3. Transfer A02
        String a02 = lifecycleManager.transferPatient(patientId, "WARD-4B", "401", "A");
        ManualTriggerResponse r3 = pasService.sendAdtMessage(a02);
        assertThat(r3.isSuccess()).isTrue();

        // 4. Update A08
        String a08 = lifecycleManager.updatePatient(patientId);
        ManualTriggerResponse r4 = pasService.sendAdtMessage(a08);
        assertThat(r4.isSuccess()).isTrue();

        // 5. Discharge A03
        String a03 = lifecycleManager.dischargePatient(patientId);
        ManualTriggerResponse r5 = pasService.sendAdtMessage(a03);
        assertThat(r5.isSuccess()).isTrue();

        SimulatorStatusDto status = pasService.getStatus();
        assertThat(status.getMessagesSent()).isEqualTo(5);
        assertThat(status.getAckAcceptCount()).isEqualTo(5);
        assertThat(status.getFailureCount()).isEqualTo(0);
        assertThat(mockHarmonia.getReceivedCount()).isEqualTo(5);
    }
}
