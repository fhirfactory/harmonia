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

class PetasosMessageOrderingIntegrationTest {

    private EmbeddedArtemisCluster cluster;
    private Petasos petasos;
    private final int brokerPort = 61706;

    @BeforeEach
    void setUp() throws Exception {
        cluster = new EmbeddedArtemisCluster();
        cluster.startStandaloneBroker("ordering-broker", brokerPort, false);

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
    void testFifoQueueMessageOrdering() throws Exception {
        int messageCount = 20;
        PetasosDestination destination = PetasosDestination.queue("ordered.clinical.sequence");

        List<Integer> sentSequence = new ArrayList<>();
        for (int i = 1; i <= messageCount; i++) {
            sentSequence.add(i);
            PetasosMessage message = PetasosMessage.builder()
                    .messageId("seq-msg-" + i)
                    .correlationId("stream-1")
                    .header("sequenceNumber", i)
                    .payload("Item #" + i)
                    .destination(destination)
                    .build();
            petasos.send(destination, message);
        }

        List<Integer> receivedSequence = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(messageCount);

        var subscription = petasos.receive(destination, (message, context) -> {
            Integer seq = message.getMetadata("sequenceNumber");
            if (seq != null) {
                receivedSequence.add(seq);
            }
            context.acknowledge();
            latch.countDown();
        });

        boolean completed = latch.await(10, TimeUnit.SECONDS);
        assertThat(completed).isTrue();
        assertThat(receivedSequence).containsExactlyElementsOf(sentSequence);

        subscription.close();
    }
}
