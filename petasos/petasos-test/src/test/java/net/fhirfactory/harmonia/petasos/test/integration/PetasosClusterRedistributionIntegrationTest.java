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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosClusterRedistributionIntegrationTest {

    private EmbeddedArtemisCluster cluster;
    private Petasos petasosNodeA;
    private Petasos petasosNodeB;
    private final int portA = 61707;
    private final int portB = 61708;

    @BeforeEach
    void setUp() throws Exception {
        cluster = new EmbeddedArtemisCluster();
        cluster.startClusteredPair("cluster-node-a", portA, "cluster-node-b", portB);

        // Client A connects directly to Node A
        PetasosConfig configA = PetasosConfig.builder()
                .addBrokerUrl("tcp://127.0.0.1:" + portA)
                .build();
        petasosNodeA = ArtemisPetasos.create(configA);

        // Client B connects directly to Node B
        PetasosConfig configB = PetasosConfig.builder()
                .addBrokerUrl("tcp://127.0.0.1:" + portB)
                .build();
        petasosNodeB = ArtemisPetasos.create(configB);
    }

    @AfterEach
    void tearDown() {
        if (petasosNodeA != null) {
            petasosNodeA.close();
        }
        if (petasosNodeB != null) {
            petasosNodeB.close();
        }
        if (cluster != null) {
            cluster.close();
        }
    }

    @Test
    void testServerSideClusteringAndMessageRedistribution() throws Exception {
        PetasosDestination destination = PetasosDestination.queue("clustered.tasks.queue");
        int messageCount = 10;
        CountDownLatch latch = new CountDownLatch(messageCount);
        List<String> receivedIds = new ArrayList<>();

        // 1. Subscribe on Node B (where no producer is connected)
        var subscriptionB = petasosNodeB.receive(destination, (message, context) -> {
            synchronized (receivedIds) {
                receivedIds.add(message.getMessageId());
            }
            context.acknowledge();
            latch.countDown();
        });

        // 2. Publish all messages exclusively to Node A
        var producerA = petasosNodeA.createProducer();
        for (int i = 1; i <= messageCount; i++) {
            String msgId = "cluster-msg-" + i;
            PetasosMessage msg = PetasosMessage.builder()
                    .messageId(msgId)
                    .payload("Task Payload #" + i)
                    .destination(destination)
                    .build();
            producerA.send(destination, msg);
        }

        // 3. Verify messages are automatically load balanced / forwarded from Node A to Node B
        boolean received = latch.await(15, TimeUnit.SECONDS);
        assertThat(received).isTrue();
        synchronized (receivedIds) {
            assertThat(receivedIds).hasSize(messageCount);
        }

        producerA.close();
        subscriptionB.close();
    }
}
