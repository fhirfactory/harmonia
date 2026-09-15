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
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.health.HealthStatus;
import net.fhirfactory.harmonia.petasos.api.health.PetasosHealth;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.artemis.ArtemisPetasos;
import net.fhirfactory.harmonia.petasos.test.harness.EmbeddedArtemisCluster;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosBrokerFailoverIntegrationTest {

    private EmbeddedArtemisCluster cluster;
    private Petasos petasos;
    private final int primaryPort = 61709;
    private final int backupPort = 61710;
    private final String primaryName = "primary-a";
    private final String backupName = "backup-a";

    @BeforeEach
    void setUp() throws Exception {
        cluster = new EmbeddedArtemisCluster();
        // 1. Start Primary and Backup pair
        cluster.startReplicationPrimary(primaryName, primaryPort, "group-a", backupPort);
        cluster.startReplicationBackup(backupName, backupPort, "group-a", primaryPort);

        // 2. Configure Petasos HA client pointing to both Primary and Backup endpoints
        PetasosConfig config = PetasosConfig.builder()
                .addBrokerUrl("tcp://127.0.0.1:" + primaryPort)
                .addBrokerUrl("tcp://127.0.0.1:" + backupPort)
                .haEnabled(true)
                .reconnectAttempts(50)
                .retryInterval(200)
                .maxRetryInterval(1000)
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
    void testBrokerFailureAndAutomaticFailover() throws Exception {
        PetasosDestination destination = PetasosDestination.queue("failover.resilience.queue");
        var producer = petasos.createProducer();

        // 1. Send messages before failover to Primary A
        for (int i = 1; i <= 5; i++) {
            PetasosMessage msg = PetasosMessage.builder()
                    .messageId("pre-failover-msg-" + i)
                    .payload("Pre-failover payload #" + i)
                    .destination(destination)
                    .durable(true)
                    .build();
            producer.send(destination, msg);
        }

        // Check health
        PetasosHealth initialHealth = petasos.health();
        assertThat(initialHealth.getStatus()).isEqualTo(HealthStatus.UP);

        // 2. Simulate Primary A failure by terminating Primary A broker
        cluster.stopBroker(primaryName);

        // Wait brief moment for Backup A activation
        Thread.sleep(1500);

        // 3. Send messages during / after failover to Backup A
        for (int i = 6; i <= 10; i++) {
            PetasosMessage msg = PetasosMessage.builder()
                    .messageId("post-failover-msg-" + i)
                    .payload("Post-failover payload #" + i)
                    .destination(destination)
                    .durable(true)
                    .build();
            producer.send(destination, msg);
        }

        // 4. Consume all 10 messages from Backup A
        var consumer = petasos.createConsumer();
        List<String> receivedIds = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Optional<PetasosMessage> received = consumer.receive(destination, Duration.ofSeconds(10));
            assertThat(received).isPresent();
            receivedIds.add(received.get().getMessageId());
        }

        assertThat(receivedIds).hasSize(10);
        assertThat(receivedIds).contains("pre-failover-msg-1", "post-failover-msg-10");

        consumer.close();
        producer.close();
    }
}
