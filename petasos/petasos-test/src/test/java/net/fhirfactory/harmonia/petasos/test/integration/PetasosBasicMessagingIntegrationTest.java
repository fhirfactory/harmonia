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

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosBasicMessagingIntegrationTest {

    private EmbeddedArtemisCluster cluster;
    private Petasos petasos;
    private final int brokerPort = 61701;

    @BeforeEach
    void setUp() throws Exception {
        cluster = new EmbeddedArtemisCluster();
        cluster.startStandaloneBroker("primary-test-broker", brokerPort, false);

        PetasosConfig config = PetasosConfig.builder()
                .addBrokerUrl("tcp://127.0.0.1:" + brokerPort)
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
    void testQueueSendAndReceiveAsync() throws Exception {
        PetasosDestination destination = PetasosDestination.queue("test.clinical.inbound");
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<PetasosMessage> receivedMsg = new AtomicReference<>();

        PetasosSubscription subscription = petasos.receive(destination, (message, context) -> {
            receivedMsg.set(message);
            context.acknowledge();
            latch.countDown();
        });

        PetasosMessage message = PetasosMessage.builder()
                .messageId("msg-1001")
                .correlationId("corr-2002")
                .causationId("cause-3003")
                .messageType("PatientAdmitEvent")
                .source("pylai-mllp-in")
                .destination(destination)
                .contentType("application/fhir+json")
                .schema("http://hl7.org/fhir/StructureDefinition/Patient", "5.0.0")
                .payload("{\"resourceType\":\"Patient\",\"id\":\"pat-100\"}")
                .header("HospitalCode", "METRO-CENTRAL")
                .durable(true)
                .build();

        petasos.send(destination, message);

        boolean received = latch.await(10, TimeUnit.SECONDS);
        assertThat(received).isTrue();

        PetasosMessage result = receivedMsg.get();
        assertThat(result).isNotNull();
        assertThat(result.getMessageId()).isEqualTo("msg-1001");
        assertThat(result.getCorrelationId()).isEqualTo("corr-2002");
        assertThat(result.getCausationId()).isEqualTo("cause-3003");
        assertThat(result.getMessageType()).isEqualTo("PatientAdmitEvent");
        assertThat(result.getSource()).isEqualTo("pylai-mllp-in");
        assertThat(result.getPayloadAsString()).isEqualTo("{\"resourceType\":\"Patient\",\"id\":\"pat-100\"}");
        assertThat(result.getMetadata()).containsEntry("HospitalCode", "METRO-CENTRAL");

        subscription.close();
    }

    @Test
    void testQueueSendAndReceiveSync() throws Exception {
        PetasosDestination destination = PetasosDestination.queue("test.sync.queue");
        var consumer = petasos.createConsumer();
        var producer = petasos.createProducer();

        PetasosMessage message = PetasosMessage.builder()
                .messageId("sync-msg-1")
                .payload("Synchronous test message")
                .destination(destination)
                .build();

        producer.send(destination, message);

        Optional<PetasosMessage> received = consumer.receive(destination, Duration.ofSeconds(5));
        assertThat(received).isPresent();
        assertThat(received.get().getMessageId()).isEqualTo("sync-msg-1");
        assertThat(received.get().getPayloadAsString()).isEqualTo("Synchronous test message");

        consumer.close();
        producer.close();
    }
}
