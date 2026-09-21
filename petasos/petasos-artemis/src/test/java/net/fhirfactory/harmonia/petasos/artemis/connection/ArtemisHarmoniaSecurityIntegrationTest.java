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

package net.fhirfactory.harmonia.petasos.artemis.connection;

import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosConsumer;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.health.ConnectionState;
import net.fhirfactory.harmonia.petasos.api.health.HealthStatus;
import net.fhirfactory.harmonia.petasos.api.health.PetasosHealth;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.api.producer.PetasosProducer;
import net.fhirfactory.harmonia.petasos.artemis.ArtemisPetasos;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ArtemisHarmoniaSecurityIntegrationTest {

    private boolean isBrokerRunning() {
        try (Socket socket = new Socket("127.0.0.1", 61616)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    void testHarmoniaUserMessagingAndHealthWithoutManagePermission() throws Exception {
        assumeTrue(isBrokerRunning(), "ActiveMQ Artemis broker is not running on 127.0.0.1:61616");

        PetasosConfig config = PetasosConfig.builder()
                .addBrokerUrl("tcp://127.0.0.1:61616")
                .username("harmonia")
                .password("harmoniaPassword")
                .haEnabled(false)
                .build();

        try (Petasos petasos = ArtemisPetasos.create(config)) {
            // Check health
            PetasosHealth health = petasos.health();
            assertThat(health.getStatus()).isEqualTo(HealthStatus.UP);
            assertThat(health.getConnectionState()).isEqualTo(ConnectionState.CONNECTED);

            String testRunId = UUID.randomUUID().toString();
            String asyncMsgId = "sec-val-async-" + testRunId;
            String syncMsgId = "sec-val-sync-" + testRunId;

            // Test asynchronous messaging with harmonia role
            PetasosDestination destination = PetasosDestination.queue("task.event.queue.security-validation-" + testRunId);
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<PetasosMessage> receivedMsg = new AtomicReference<>();

            PetasosSubscription subscription = petasos.receive(destination, (message, context) -> {
                receivedMsg.set(message);
                context.acknowledge();
                latch.countDown();
            });

            PetasosMessage message = PetasosMessage.builder()
                    .messageId(asyncMsgId)
                    .correlationId("corr-" + testRunId)
                    .payload("{\"test\":\"harmonia-security-validation\"}")
                    .destination(destination)
                    .durable(true)
                    .build();

            petasos.send(destination, message);

            boolean received = latch.await(10, TimeUnit.SECONDS);
            assertThat(received).isTrue();
            assertThat(receivedMsg.get()).isNotNull();
            assertThat(receivedMsg.get().getMessageId()).isEqualTo(asyncMsgId);
            assertThat(receivedMsg.get().getPayloadAsString()).isEqualTo("{\"test\":\"harmonia-security-validation\"}");

            subscription.close();

            // Test synchronous producer and consumer
            try (PetasosProducer producer = petasos.createProducer();
                 PetasosConsumer consumer = petasos.createConsumer()) {
                PetasosDestination syncDest = PetasosDestination.queue("petasos.queue.sync-security-val-" + testRunId);
                PetasosMessage syncMsg = PetasosMessage.builder()
                        .messageId(syncMsgId)
                        .payload("Sync payload with harmonia user")
                        .destination(syncDest)
                        .build();

                producer.send(syncDest, syncMsg);

                Optional<PetasosMessage> syncReceived = consumer.receive(syncDest, Duration.ofSeconds(5));
                assertThat(syncReceived).isPresent();
                assertThat(syncReceived.get().getMessageId()).isEqualTo(syncMsgId);
                assertThat(syncReceived.get().getPayloadAsString()).isEqualTo("Sync payload with harmonia user");
            }
        }
    }
}
