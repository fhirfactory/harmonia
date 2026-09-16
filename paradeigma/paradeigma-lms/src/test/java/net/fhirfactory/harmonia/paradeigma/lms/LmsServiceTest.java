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

package net.fhirfactory.harmonia.paradeigma.lms;

import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.lms.config.LmsConfig;
import net.fhirfactory.harmonia.paradeigma.lms.mllp.LmsMllpListener;
import net.fhirfactory.harmonia.paradeigma.lms.service.LmsResultWorker;
import net.fhirfactory.harmonia.paradeigma.lms.service.LmsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class LmsServiceTest {

    private MllpServer mockHarmonia;
    private int mockHarmoniaPort;
    private LmsService lmsService;
    private LmsResultWorker resultWorker;
    private LmsMllpListener lmsListener;
    private LmsConfig config;

    @BeforeEach
    void setUp() throws IOException {
        mockHarmonia = new MllpServer(0, rawHl7 -> Hl7AckHandler.generateAcceptAck(rawHl7));
        mockHarmonia.start();
        mockHarmoniaPort = mockHarmonia.getPort();

        config = new LmsConfig();
        config.setHarmoniaHost("127.0.0.1");
        config.setHarmoniaOruPort(mockHarmoniaPort);
        config.setInboundAdtPort(0); // Ephemeral port
        config.setInboundOrmPort(0); // Ephemeral port
        config.setResultDelayMs(50L);

        lmsService = new LmsService(config);
        resultWorker = new LmsResultWorker(config, lmsService);
        lmsListener = new LmsMllpListener(config, lmsService, resultWorker);
        lmsListener.start();
    }

    @AfterEach
    void tearDown() {
        if (resultWorker != null) resultWorker.stop();
        if (lmsListener != null) lmsListener.stop();
        if (lmsService != null) lmsService.close();
        if (mockHarmonia != null) mockHarmonia.stop();
    }

    @Test
    @DisplayName("LMS receives routed Lab ORM on :2204 and responds with ACK AA")
    void testReceiveRoutedOrm() {
        int ormPort = lmsListener.getOrmServer().getPort();
        String ormHl7 = "MSH|^~\\&|HARMONIA|HIE|PARADEIGMA_LMS|LMS|20260915120000||ORM^O01|MSG-ROUTED-01|P|2.4\r" +
                "PID|1||PAT-101^^^MRN||Smith^John\r" +
                "ORC|NW|ORD-9001|||IP||^^^R||20260915120000\r" +
                "OBR|1|ORD-9001||CBC^Complete Blood Count^LN\r";

        try (MllpClient client = new MllpClient("127.0.0.1", ormPort)) {
            String ack = client.sendAndReceive(ormHl7);
            assertThat(ack).contains("MSA|AA|MSG-ROUTED-01");
        }

        assertThat(lmsService.getReceivedOrders()).hasSize(1);
        assertThat(lmsService.getReceivedOrders().get(0).getPlacerOrderNumber()).isEqualTo("ORD-9001");
    }

    @Test
    @DisplayName("LMS produces and sends synthetic ORU^R01 lab results to Harmonia")
    void testSendLabResult() {
        ManualTriggerResponse res = lmsService.produceLabResult("ORD-9002", "CBC");
        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getAckCode()).isEqualTo("AA");

        SimulatorStatusDto status = lmsService.getStatus();
        assertThat(status.getMessagesSent()).isEqualTo(1);
        assertThat(status.getAckAcceptCount()).isEqualTo(1);
        assertThat(mockHarmonia.getReceivedCount()).isEqualTo(1);
    }
}
