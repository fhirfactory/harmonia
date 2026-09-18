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
import net.fhirfactory.harmonia.petasos.api.config.PetasosConsumerConfig;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.artemis.ArtemisPetasos;
import net.fhirfactory.harmonia.petasos.test.harness.EmbeddedArtemisCluster;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosDeadLetterAndExpiryIntegrationTest {

    private EmbeddedArtemisCluster cluster;
    private Petasos petasos;
    private final int brokerPort = 61705;

    @BeforeEach
    void setUp() throws Exception {
        cluster = new EmbeddedArtemisCluster();
        cluster.startStandaloneBroker("dlq-expiry-broker", brokerPort, false);

        PetasosConfig config = PetasosConfig.builder()
                .addBrokerUrl("tcp://127.0.0.1:" + brokerPort)
                .deadLetterAddress("DLQ")
                .expiryAddress("ExpiryQueue")
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
    void testMessageExplicitRejectionRoutesToDLQ() throws Exception {
        PetasosDestination destination = PetasosDestination.queue("flaky.processing.queue");
        PetasosDestination dlqDestination = PetasosDestination.queue("DLQ");

        CountDownLatch latch = new CountDownLatch(1);

        PetasosConsumerConfig consumerConfig = PetasosConsumerConfig.builder()
                .autoAcknowledgeOnSuccess(false)
                .autoRejectOnError(false)
                .build();

        var subscription = petasos.receive(destination, consumerConfig, (message, context) -> {
            context.reject(false); // Reject without requeue -> routes directly to DLQ
            latch.countDown();
        });

        PetasosMessage message = PetasosMessage.builder()
                .messageId("poison-pill-1")
                .payload("Corrupt clinical payload")
                .destination(destination)
                .build();

        petasos.send(destination, message);

        boolean rejected = latch.await(5, TimeUnit.SECONDS);
        assertThat(rejected).isTrue();

        subscription.close();

        // Verify message arrived in DLQ
        var dlqConsumer = petasos.createConsumer();
        Optional<PetasosMessage> dlqMessage = dlqConsumer.receive(dlqDestination, Duration.ofSeconds(5));
        assertThat(dlqMessage).isPresent();
        assertThat(dlqMessage.get().getMessageId()).isEqualTo("poison-pill-1");
        dlqConsumer.close();
    }

    @Test
    void testExpiredMessageRoutedToExpiryQueue() throws Exception {
        PetasosDestination destination = PetasosDestination.queue("expiring.queue");
        PetasosDestination expiryDestination = PetasosDestination.queue("ExpiryQueue");

        // Send message with 300ms TTL
        PetasosMessage msg = PetasosMessage.builder()
                .messageId("expiring-msg-1")
                .payload("Time-sensitive token")
                .ttl(Duration.ofMillis(300))
                .destination(destination)
                .build();

        petasos.send(destination, msg);

        // Sleep to let message expire and expiry scanner execute
        Thread.sleep(1200);

        // Normal consumer on queue should find nothing because message has expired
        var consumer = petasos.createConsumer();
        Optional<PetasosMessage> normalReceived = consumer.receive(destination, Duration.ofMillis(300));
        assertThat(normalReceived).isEmpty();

        // ExpiryQueue contains the expired message
        Optional<PetasosMessage> expiredReceived = consumer.receive(expiryDestination, Duration.ofSeconds(5));
        assertThat(expiredReceived).isPresent();
        assertThat(expiredReceived.get().getMessageId()).isEqualTo("expiring-msg-1");

        consumer.close();
    }
}
