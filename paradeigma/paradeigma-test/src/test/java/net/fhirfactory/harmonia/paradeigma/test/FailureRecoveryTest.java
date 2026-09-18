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

import net.fhirfactory.harmonia.paradeigma.common.failure.FaultInjectionConfig;
import net.fhirfactory.harmonia.paradeigma.common.failure.FailureSimulator;
import net.fhirfactory.harmonia.paradeigma.common.hl7.AckResult;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpClient;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpException;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpServer;
import net.fhirfactory.harmonia.paradeigma.common.mllp.MllpTimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FailureRecoveryTest {

    private MllpServer mockServer;

    @AfterEach
    void tearDown() {
        if (mockServer != null) mockServer.stop();
    }

    @Test
    @DisplayName("Application Error (AE) NACK is parsed and distinguished from transport failures")
    void testApplicationErrorHandling() throws IOException {
        mockServer = new MllpServer(0, rawHl7 -> Hl7AckHandler.generateErrorAck(rawHl7, "Patient MRN Not Found"));
        mockServer.start();

        String hl7 = "MSH|^~\\&|PAS|FAC|HARMONIA|HIE|20260915120000||ADT^A01|MSG-ERR-01|P|2.4\rPID|1||PAT-999^^^MRN||Smith^John\r";

        try (MllpClient client = new MllpClient("127.0.0.1", mockServer.getPort())) {
            String ack = client.sendAndReceive(hl7);
            AckResult result = Hl7AckHandler.parseAck(ack);

            assertThat(result.isError()).isTrue();
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getAckCode()).isEqualTo("AE");
            assertThat(result.getTextMessage()).contains("Patient MRN Not Found");
        }
    }

    @Test
    @DisplayName("Application Reject (AR) NACK is parsed correctly")
    void testApplicationRejectHandling() throws IOException {
        mockServer = new MllpServer(0, rawHl7 -> Hl7AckHandler.generateRejectAck(rawHl7, "Unsupported HL7 Version 2.1"));
        mockServer.start();

        String hl7 = "MSH|^~\\&|PAS|FAC|HARMONIA|HIE|20260915120000||ADT^A01|MSG-REJ-01|P|2.1\rPID|1||PAT-999^^^MRN||Smith^John\r";

        try (MllpClient client = new MllpClient("127.0.0.1", mockServer.getPort())) {
            String ack = client.sendAndReceive(hl7);
            AckResult result = Hl7AckHandler.parseAck(ack);

            assertThat(result.isReject()).isTrue();
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getAckCode()).isEqualTo("AR");
        }
    }

    @Test
    @DisplayName("Client retries on transient connection error and succeeds on subsequent attempt")
    void testTransientConnectionRetry() throws IOException {
        AtomicInteger callCount = new AtomicInteger(0);

        mockServer = new MllpServer(0, rawHl7 -> {
            if (callCount.incrementAndGet() == 1) {
                return null; // First attempt drops connection / no ack
            }
            return Hl7AckHandler.generateAcceptAck(rawHl7);
        });
        mockServer.start();

        String hl7 = "MSH|^~\\&|PAS|FAC|HARMONIA|HIE|20260915120000||ADT^A01|MSG-RETRY-01|P|2.4\rPID|1||PAT-101^^^MRN||Smith^John\r";

        // Client configured with maxRetries=2, retryDelay=50ms
        try (MllpClient client = new MllpClient("127.0.0.1", mockServer.getPort(), 1000, 500, null, 2, 50L, false)) {
            String ack = client.sendAndReceive(hl7);
            AckResult result = Hl7AckHandler.parseAck(ack);
            assertThat(result.isAccept()).isTrue();
        }

        assertThat(callCount.get()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Failure simulator injects configurable faults deterministically")
    void testFailureSimulatorFaults() {
        FaultInjectionConfig config = new FaultInjectionConfig();
        config.setEnabled(true);
        config.setRandomSeed(12345L);
        config.setApplicationErrorProbability(1.0); // Always inject AE error

        FailureSimulator simulator = new FailureSimulator(config);
        String msg = "MSH|^~\\&|PAS|FAC|HARMONIA|HIE|20260915120000||ADT^A01|MSG-SIM-01|P|2.4\rPID|1||PAT-101^^^MRN||Smith^John\r";

        String ack = simulator.evaluateAck(msg);
        AckResult result = Hl7AckHandler.parseAck(ack);
        assertThat(result.isError()).isTrue();
        assertThat(result.getAckCode()).isEqualTo("AE");
    }
}
