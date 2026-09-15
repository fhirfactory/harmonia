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

import static org.assertj.core.api.Assertions.assertThat;

class PetasosDurablePersistenceRecoveryIntegrationTest {

    private EmbeddedArtemisCluster cluster;
    private final int brokerPort = 61702;
    private final String brokerName = "durable-broker";

    @BeforeEach
    void setUp() {
        cluster = new EmbeddedArtemisCluster();
    }

    @AfterEach
    void tearDown() {
        if (cluster != null) {
            cluster.close();
        }
    }

    @Test
    void testMessagesPersistAcrossBrokerRestart() throws Exception {
        // 1. Start persistent broker with Artemis NIO journal
        cluster.startStandaloneBroker(brokerName, brokerPort, true);

        PetasosConfig config = PetasosConfig.builder()
                .addBrokerUrl("tcp://127.0.0.1:" + brokerPort)
                .reconnectAttempts(10)
                .retryInterval(200)
                .build();

        Petasos client1 = ArtemisPetasos.create(config);
        PetasosDestination destination = PetasosDestination.queue("persistent.clinical.orders");

        // 2. Publish 5 durable messages
        List<String> sentMessageIds = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String msgId = "persist-msg-" + i;
            sentMessageIds.add(msgId);
            PetasosMessage msg = PetasosMessage.builder()
                    .messageId(msgId)
                    .correlationId("order-batch-1")
                    .messageType("MedicationOrder")
                    .payload("Order Details #" + i)
                    .durable(true)
                    .destination(destination)
                    .build();
            client1.send(destination, msg);
        }

        // Close first client session
        client1.close();

        // 3. Stop broker while messages remain unconsumed on disk
        cluster.stopBroker(brokerName);

        // 4. Restart broker from persistent journal files
        cluster.startStandaloneBroker(brokerName, brokerPort, true);

        // 5. Connect new Petasos client and consume queued messages
        Petasos client2 = ArtemisPetasos.create(config);
        var consumer = client2.createConsumer();

        List<String> recoveredMessageIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Optional<PetasosMessage> msg = consumer.receive(destination, Duration.ofSeconds(5));
            assertThat(msg).isPresent();
            recoveredMessageIds.add(msg.get().getMessageId());
            assertThat(msg.get().isDurable()).isTrue();
        }

        assertThat(recoveredMessageIds).containsExactlyElementsOf(sentMessageIds);

        consumer.close();
        client2.close();
    }
}
