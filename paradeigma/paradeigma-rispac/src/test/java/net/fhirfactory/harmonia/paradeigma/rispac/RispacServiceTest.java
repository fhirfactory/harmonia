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

package net.fhirfactory.harmonia.paradeigma.rispac;

import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.rispac.config.RispacConfig;
import net.fhirfactory.harmonia.paradeigma.rispac.mllp.RispacMllpListener;
import net.fhirfactory.harmonia.paradeigma.rispac.service.RispacResultWorker;
import net.fhirfactory.harmonia.paradeigma.rispac.service.RispacService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class RispacServiceTest {

    private MllpServer mockHarmonia;
    private int mockHarmoniaPort;
    private RispacService rispacService;
    private RispacResultWorker resultWorker;
    private RispacMllpListener rispacListener;
    private RispacConfig config;

    @BeforeEach
    void setUp() throws IOException {
        mockHarmonia = new MllpServer(0, rawHl7 -> Hl7AckHandler.generateAcceptAck(rawHl7));
        mockHarmonia.start();
        mockHarmoniaPort = mockHarmonia.getPort();

        config = new RispacConfig();
        config.setHarmoniaHost("127.0.0.1");
        config.setHarmoniaOruPort(mockHarmoniaPort);
        config.setInboundAdtPort(0); // Ephemeral port
        config.setInboundOrmPort(0); // Ephemeral port
        config.setReportingDelayMs(50L);

        rispacService = new RispacService(config);
        resultWorker = new RispacResultWorker(config, rispacService);
        rispacListener = new RispacMllpListener(config, rispacService, resultWorker);
        rispacListener.start();
    }

    @AfterEach
    void tearDown() {
        if (resultWorker != null) resultWorker.stop();
        if (rispacListener != null) rispacListener.stop();
        if (rispacService != null) rispacService.close();
        if (mockHarmonia != null) mockHarmonia.stop();
    }

    @Test
    @DisplayName("RIS-PAC receives routed Imaging ORM on :2205 and responds with ACK AA")
    void testReceiveRoutedOrm() {
        int ormPort = rispacListener.getOrmServer().getPort();
        String ormHl7 = "MSH|^~\\&|HARMONIA|HIE|PARADEIGMA_RISPAC|RISPAC|20260915120000||ORM^O01|MSG-ROUTED-02|P|2.4\r" +
                "PID|1||PAT-102^^^MRN||Smith^Jane\r" +
                "ORC|NW|ORD-9002|||IP||^^^R||20260915120000\r" +
                "OBR|1|ORD-9002||XR_CHEST^Chest X-Ray PA and Lateral^RAD\r";

        try (MllpClient client = new MllpClient("127.0.0.1", ormPort)) {
            String ack = client.sendAndReceive(ormHl7);
            assertThat(ack).contains("MSA|AA|MSG-ROUTED-02");
        }

        assertThat(rispacService.getReceivedOrders()).hasSize(1);
        assertThat(rispacService.getReceivedOrders().get(0).getPlacerOrderNumber()).isEqualTo("ORD-9002");
    }

    @Test
    @DisplayName("RIS-PAC produces and sends synthetic ORU^R01 diagnostic imaging reports to Harmonia")
    void testSendImagingReport() {
        ManualTriggerResponse res = rispacService.produceImagingReport("ORD-9003", "XR_CHEST");
        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getAckCode()).isEqualTo("AA");

        SimulatorStatusDto status = rispacService.getStatus();
        assertThat(status.getMessagesSent()).isEqualTo(1);
        assertThat(status.getAckAcceptCount()).isEqualTo(1);
        assertThat(mockHarmonia.getReceivedCount()).isEqualTo(1);
    }
}
