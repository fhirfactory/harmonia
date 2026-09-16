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

package net.fhirfactory.harmonia.paradeigma.emr;

import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.emr.config.EmrConfig;
import net.fhirfactory.harmonia.paradeigma.emr.mllp.EmrMllpListener;
import net.fhirfactory.harmonia.paradeigma.emr.service.EmrPatientManager;
import net.fhirfactory.harmonia.paradeigma.emr.service.EmrService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class EmrServiceTest {

    private MllpServer mockHarmonia;
    private int mockHarmoniaPort;
    private EmrService emrService;
    private EmrPatientManager patientManager;
    private EmrMllpListener emrListener;
    private EmrConfig config;

    @BeforeEach
    void setUp() throws IOException {
        mockHarmonia = new MllpServer(0, rawHl7 -> Hl7AckHandler.generateAcceptAck(rawHl7));
        mockHarmonia.start();
        mockHarmoniaPort = mockHarmonia.getPort();

        config = new EmrConfig();
        config.setHarmoniaHost("127.0.0.1");
        config.setHarmoniaOrmPort(mockHarmoniaPort);
        config.setInboundAdtPort(0); // Ephemeral port for test

        patientManager = new EmrPatientManager(12345L);
        emrService = new EmrService(config, patientManager);
        emrListener = new EmrMllpListener(config, patientManager, emrService);
        emrListener.start();
    }

    @AfterEach
    void tearDown() {
        if (emrListener != null) emrListener.stop();
        if (emrService != null) emrService.close();
        if (mockHarmonia != null) mockHarmonia.stop();
    }

    @Test
    @DisplayName("EMR receives ADT fan-out over MLLP and stores patient in registry")
    void testReceiveFanoutAdt() {
        int listenerPort = emrListener.getServer().getPort();
        String fanoutAdt = "MSH|^~\\&|HARMONIA|HIE|PARADEIGMA_EMR|EMR|20260915120000||ADT^A01|MSG-FAN-01|P|2.4\r" +
                "PID|1||PAT-999^^^MRN||Johnson^Robert\r" +
                "PV1|1|I|WARD-3A^301^A\r";

        try (MllpClient client = new MllpClient("127.0.0.1", listenerPort)) {
            String ack = client.sendAndReceive(fanoutAdt);
            assertThat(ack).contains("MSA|AA|MSG-FAN-01");
        }

        assertThat(patientManager.getAllPatients()).hasSize(1);
        assertThat(patientManager.getAllPatients().get(0).getPatientId()).isEqualTo("PAT-999");
        assertThat(patientManager.getAllPatients().get(0).getFamilyName()).isEqualTo("Johnson");
    }

    @Test
    @DisplayName("EMR places Lab and Imaging ORM^O01 orders to Harmonia")
    void testPlaceOrders() {
        ManualTriggerResponse labRes = emrService.placeLabOrder("CBC");
        assertThat(labRes.isSuccess()).isTrue();
        assertThat(labRes.getAckCode()).isEqualTo("AA");

        ManualTriggerResponse imgRes = emrService.placeImagingOrder("XR_CHEST");
        assertThat(imgRes.isSuccess()).isTrue();
        assertThat(imgRes.getAckCode()).isEqualTo("AA");

        SimulatorStatusDto status = emrService.getStatus();
        assertThat(status.getMessagesSent()).isEqualTo(2);
        assertThat(status.getAckAcceptCount()).isEqualTo(2);
        assertThat(mockHarmonia.getReceivedCount()).isEqualTo(2);
    }
}
