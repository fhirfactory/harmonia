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

import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosConcurrencyIntegrationTest {

    private EmbeddedArtemisCluster cluster;
    private Petasos petasos;
    private final int brokerPort = 61703;

    @BeforeEach
    void setUp() throws Exception {
        cluster = new EmbeddedArtemisCluster();
        cluster.startStandaloneBroker("concurrent-broker", brokerPort, false);

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
    void testConcurrentProducersAndConsumers() throws Exception {
        int totalMessages = 50;
        int producerCount = 5;
        int messagesPerProducer = totalMessages / producerCount;

        PetasosDestination destination = PetasosDestination.queue("concurrent.work.queue");
        CountDownLatch consumeLatch = new CountDownLatch(totalMessages);
        Set<String> receivedIds = ConcurrentHashMap.newKeySet();
        AtomicInteger duplicates = new AtomicInteger(0);

        // Start consumer
        PetasosSubscription subscription = petasos.receive(destination, (message, context) -> {
            boolean added = receivedIds.add(message.getMessageId());
            if (!added) {
                duplicates.incrementAndGet();
            }
            context.acknowledge();
            consumeLatch.countDown();
        });

        // Launch concurrent producers
        ExecutorService executor = Executors.newFixedThreadPool(producerCount);
        CountDownLatch producerLatch = new CountDownLatch(producerCount);

        for (int p = 0; p < producerCount; p++) {
            final int producerId = p;
            executor.submit(() -> {
                try {
                    var producer = petasos.createProducer();
                    for (int i = 0; i < messagesPerProducer; i++) {
                        String msgId = "p" + producerId + "-msg-" + i;
                        PetasosMessage msg = PetasosMessage.builder()
                                .messageId(msgId)
                                .payload("Payload from producer " + producerId + " item " + i)
                                .destination(destination)
                                .build();
                        producer.send(destination, msg);
                    }
                    producer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    producerLatch.countDown();
                }
            });
        }

        boolean produced = producerLatch.await(10, TimeUnit.SECONDS);
        assertThat(produced).isTrue();

        boolean consumed = consumeLatch.await(15, TimeUnit.SECONDS);
        assertThat(consumed).isTrue();

        assertThat(receivedIds).hasSize(totalMessages);
        assertThat(duplicates.get()).isEqualTo(0);

        subscription.close();
        executor.shutdown();
    }
}
