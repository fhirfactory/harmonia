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

package net.fhirfactory.harmonia.petasos.test.integration;

import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.artemis.ArtemisPetasos;
import net.fhirfactory.harmonia.petasos.test.harness.EmbeddedArtemisCluster;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosDeduplicationIntegrationTest {

    private EmbeddedArtemisCluster cluster;
    private Petasos petasos;
    private final int brokerPort = 61704;

    @BeforeEach
    void setUp() throws Exception {
        cluster = new EmbeddedArtemisCluster();
        cluster.startStandaloneBroker("dedup-broker", brokerPort, false);

        PetasosConfig config = PetasosConfig.builder()
                .addBrokerUrl("tcp://127.0.0.1:" + brokerPort)
                .duplicateDetectionEnabled(true)
                .build();
        petasos = ArtemisPetasos.create(config);
    }

    @AfterEach
    void tearDown() {
        if (petasos != null) {
            petasos.close();
        }
        if (cluster != null) {
            cluster.close();
        }
    }

    @Test
    void testArtemisDuplicateDetectionDropsDuplicates() throws Exception {
        PetasosDestination destination = PetasosDestination.queue("dedup.clinical.events");
        List<String> receivedMessageIds = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        PetasosSubscription subscription = petasos.receive(destination, (message, context) -> {
            synchronized (receivedMessageIds) {
                receivedMessageIds.add(message.getMessageId());
            }
            context.acknowledge();
            latch.countDown();
        });

        String duplicateKey = "UNIQUE-DEDUP-KEY-999";

        // Send first message
        PetasosMessage msg1 = PetasosMessage.builder()
                .messageId("msg-attempt-1")
                .duplicateDetectionId(duplicateKey)
                .payload("Clinical Update #1")
                .destination(destination)
                .build();
        petasos.send(destination, msg1);

        // Send second message with identical duplicateDetectionId
        PetasosMessage msg2 = PetasosMessage.builder()
                .messageId("msg-attempt-2")
                .duplicateDetectionId(duplicateKey)
                .payload("Clinical Update #1 (Duplicate transmission)")
                .destination(destination)
                .build();
        petasos.send(destination, msg2);

        boolean firstReceived = latch.await(5, TimeUnit.SECONDS);
        assertThat(firstReceived).isTrue();

        // Brief wait to ensure duplicate is not delivered
        Thread.sleep(1000);

        synchronized (receivedMessageIds) {
            assertThat(receivedMessageIds).hasSize(1);
            assertThat(receivedMessageIds.get(0)).isEqualTo("msg-attempt-1");
        }

        subscription.close();
    }

    @Test
    void testBrokerRedeliveryAfterHandlerFailureIsProcessed() throws Exception {
        PetasosDestination destination = PetasosDestination.queue("dedup.clinical.retry");
        List<String> attempts = new ArrayList<>();
        CountDownLatch redeliverySuccessLatch = new CountDownLatch(1);

        PetasosSubscription subscription = petasos.receive(destination, (message, context) -> {
            synchronized (attempts) {
                attempts.add("delivery-" + context.getRedeliveryCount());
                if (context.getRedeliveryCount() == 1) {
                    // First attempt: simulate processing failure to trigger session recovery / broker redelivery
                    throw new RuntimeException("Simulated transient failure on attempt 1");
                }
            }
            context.acknowledge();
            redeliverySuccessLatch.countDown();
        });

        String duplicateKey = "RETRY-DEDUP-KEY-777";
        PetasosMessage msg = PetasosMessage.builder()
                .messageId("msg-transient-1")
                .duplicateDetectionId(duplicateKey)
                .payload("Clinical Update with Transient Failure")
                .destination(destination)
                .build();
        petasos.send(destination, msg);

        boolean redelivered = redeliverySuccessLatch.await(5, TimeUnit.SECONDS);
        assertThat(redelivered).isTrue();

        synchronized (attempts) {
            assertThat(attempts).contains("delivery-1", "delivery-2");
        }

        // Now that redelivery was acknowledged, a subsequent duplicate transmission must be skipped
        CountDownLatch duplicateLatch = new CountDownLatch(1);
        List<String> duplicateAttempts = new ArrayList<>();

        PetasosMessage duplicateMsg = PetasosMessage.builder()
                .messageId("msg-transient-duplicate")
                .duplicateDetectionId(duplicateKey)
                .payload("Duplicate transmission")
                .destination(destination)
                .build();
        petasos.send(destination, duplicateMsg);

        Thread.sleep(1000);

        synchronized (attempts) {
            // No new deliveries to the handler should have occurred
            assertThat(attempts).hasSize(2);
        }

        subscription.close();
    }
}
