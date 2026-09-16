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

package net.fhirfactory.harmonia.paradeigma.common.mllp;

import net.fhirfactory.harmonia.paradeigma.common.hl7.AckResult;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7AckHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MllpClientServerIntegrationTest {

    private MllpServer server;
    private int serverPort;

    @BeforeEach
    void setUp() throws IOException {
        // Start server on ephemeral port 0
        server = new MllpServer(0, rawMsg -> Hl7AckHandler.generateAcceptAck(rawMsg));
        server.start();
        serverPort = server.getPort();
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    @DisplayName("Client sends message over TCP/MLLP and receives correlated AA ACK")
    void testClientSendAndReceive() {
        String hl7 = "MSH|^~\\&|PAS|FACILITY|HARMONIA|HIE|20260915120000||ADT^A01|MSG-TEST-001|P|2.4\r"
                + "PID|1||PAT-101^^^MRN||Smith^John\r";

        try (MllpClient client = new MllpClient("127.0.0.1", serverPort)) {
            String ack = client.sendAndReceive(hl7);
            assertThat(ack).isNotNull();

            AckResult ackResult = Hl7AckHandler.parseAck(ack);
            assertThat(ackResult.isAccept()).isTrue();
            assertThat(ackResult.getCorrelatedMessageId()).isEqualTo("MSG-TEST-001");
        }

        assertThat(server.getReceivedCount()).isEqualTo(1);
        assertThat(server.getReceivedMessages()).hasSize(1);
        assertThat(server.getReceivedMessages().get(0)).isEqualTo(hl7);
    }

    @Test
    @DisplayName("Client connects to unavailable server and throws MllpConnectionException")
    void testConnectionFailure() {
        try (MllpClient client = new MllpClient("127.0.0.1", 64999, 1000, 1000)) {
            assertThatThrownBy(() -> client.sendAndReceive("MSH|..."))
                    .isInstanceOf(MllpConnectionException.class);
        }
    }

    @Test
    @DisplayName("Concurrent clients transmit messages reliably without interference")
    void testConcurrentClients() throws InterruptedException {
        int threadCount = 10;
        int messagesPerThread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try (MllpClient client = new MllpClient("127.0.0.1", serverPort)) {
                    for (int m = 0; m < messagesPerThread; m++) {
                        String msgId = "TH-" + threadId + "-MSG-" + m;
                        String hl7 = "MSH|^~\\&|PAS|FACILITY|HARMONIA|HIE|20260915120000||ADT^A04|" + msgId + "|P|2.4\r"
                                + "PID|1||PAT-" + threadId + "^^^MRN||Smith^Test\r";
                        String ack = client.sendAndReceive(hl7);
                        AckResult res = Hl7AckHandler.parseAck(ack);
                        if (res.isAccept() && res.matchesControlId(msgId)) {
                            successCount.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    // ignore
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean finished = latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(finished).isTrue();
        assertThat(successCount.get()).isEqualTo(threadCount * messagesPerThread);
        assertThat(server.getReceivedCount()).isEqualTo(threadCount * messagesPerThread);
    }
}
