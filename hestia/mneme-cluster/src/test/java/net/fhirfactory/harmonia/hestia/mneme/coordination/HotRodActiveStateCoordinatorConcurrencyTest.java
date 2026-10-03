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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.hestia.mneme.coordination;

import net.fhirfactory.harmonia.infinispan.scenario.support.InfinispanLaboratoryServer;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Concurrency proof for Scenario G:
 * <p>
 * Proves that when multiple contenders race to transition a resource from NO_HEGEMON to HEGEMON
 * using the same observed ActiveStateToken, exactly ONE contender wins the atomic transition
 * (Acquired), and all competing contenders lose (Stale / NotHegemon).
 * Mnemosyne is not involved; Mneme's native Hot Rod CAS resolves the race with zero split-brain.
 */
class HotRodActiveStateCoordinatorConcurrencyTest {

    private static InfinispanLaboratoryServer laboratoryServer;
    private static RemoteCacheManager clientA;
    private static RemoteCacheManager clientB;
    private static HotRodActiveStateCoordinator coordinatorA;
    private static HotRodActiveStateCoordinator coordinatorB;

    @BeforeAll
    static void startEnvironment() {
        laboratoryServer = new InfinispanLaboratoryServer();
        laboratoryServer.startCluster();

        clientA = laboratoryServer.createClientA();
        clientB = laboratoryServer.createClientB();

        coordinatorA = new HotRodActiveStateCoordinator(clientA);
        coordinatorB = new HotRodActiveStateCoordinator(clientB);
    }

    @AfterAll
    static void stopEnvironment() {
        if (clientA != null) {
            try { clientA.stop(); } catch (Exception ignored) {}
        }
        if (clientB != null) {
            try { clientB.stop(); } catch (Exception ignored) {}
        }
        if (laboratoryServer != null) {
            try { laboratoryServer.close(); } catch (Exception ignored) {}
        }
    }

    @Test
    @DisplayName("Scenario G: Two concurrent contenders racing on NO_HEGEMON yield exactly one Acquired and one Stale")
    void testConcurrentTwoContendersRaceOnNoHegemon() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            // Repeat across 5 distinct resource keys to prove consistency under concurrency
            for (int i = 0; i < 5; i++) {
                ResourceKey key = ResourceKey.of("Patient", "patient-race-" + i + "-" + UUID.randomUUID());
                ActiveStateToken observedToken = coordinatorA.observe(key);

                InstanceId instanceX = InstanceId.random();
                InstanceId instanceY = InstanceId.random();

                CyclicBarrier barrier = new CyclicBarrier(2);

                Callable<HegemonyCoordinationResult> taskX = () -> {
                    barrier.await(5, TimeUnit.SECONDS);
                    return coordinatorA.acquireOrConfirmHegemony(key, instanceX, observedToken);
                };

                Callable<HegemonyCoordinationResult> taskY = () -> {
                    barrier.await(5, TimeUnit.SECONDS);
                    return coordinatorB.acquireOrConfirmHegemony(key, instanceY, observedToken);
                };

                Future<HegemonyCoordinationResult> futureX = executor.submit(taskX);
                Future<HegemonyCoordinationResult> futureY = executor.submit(taskY);

                HegemonyCoordinationResult resultX = futureX.get(5, TimeUnit.SECONDS);
                HegemonyCoordinationResult resultY = futureY.get(5, TimeUnit.SECONDS);

                // Assert exactly one winner
                boolean xWon = resultX instanceof HegemonyCoordinationResult.Acquired;
                boolean yWon = resultY instanceof HegemonyCoordinationResult.Acquired;

                assertThat(xWon ^ yWon)
                        .as("Exactly one contender must win Acquired (X won: %s, Y won: %s)", xWon, yWon)
                        .isTrue();

                InstanceId winner = xWon ? instanceX : instanceY;
                InstanceId loser = xWon ? instanceY : instanceX;
                HegemonyCoordinationResult loserResult = xWon ? resultY : resultX;

                assertThat(loserResult)
                        .as("Loser must receive Stale or NotHegemon")
                        .satisfies(res -> assertThat(res instanceof HegemonyCoordinationResult.Stale
                                || res instanceof HegemonyCoordinationResult.NotHegemon).isTrue());

                // Both nodes observe the winner as RESOURCE_IS_HEGEMON
                assertThat(coordinatorA.checkHegemonStatus(key, winner)).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_HEGEMON);
                assertThat(coordinatorB.checkHegemonStatus(key, winner)).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_HEGEMON);

                // Both nodes observe the loser as RESOURCE_IS_NOT_HEGEMON
                assertThat(coordinatorA.checkHegemonStatus(key, loser)).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_NOT_HEGEMON);
                assertThat(coordinatorB.checkHegemonStatus(key, loser)).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_NOT_HEGEMON);
            }
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    @DisplayName("Scenario G (Multi-contender): Four concurrent contenders on NO_HEGEMON yield exactly one Acquired and three Stale or NotHegemon")
    void testConcurrentFourContendersRaceOnNoHegemon() throws Exception {
        int threads = 4;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            ResourceKey key = ResourceKey.of("Observation", "obs-race-" + UUID.randomUUID());
            ActiveStateToken observedToken = coordinatorA.observe(key);

            List<InstanceId> instances = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                instances.add(InstanceId.random());
            }

            CyclicBarrier barrier = new CyclicBarrier(threads);
            List<Future<HegemonyCoordinationResult>> futures = new ArrayList<>();

            for (int i = 0; i < threads; i++) {
                final int idx = i;
                final HotRodActiveStateCoordinator coord = (idx % 2 == 0) ? coordinatorA : coordinatorB;
                futures.add(executor.submit(() -> {
                    barrier.await(5, TimeUnit.SECONDS);
                    return coord.acquireOrConfirmHegemony(key, instances.get(idx), observedToken);
                }));
            }

            int acquiredCount = 0;
            int nonWinnerCount = 0;
            InstanceId winner = null;

            for (int i = 0; i < threads; i++) {
                HegemonyCoordinationResult res = futures.get(i).get(5, TimeUnit.SECONDS);
                if (res instanceof HegemonyCoordinationResult.Acquired) {
                    acquiredCount++;
                    winner = instances.get(i);
                } else if (res instanceof HegemonyCoordinationResult.Stale || res instanceof HegemonyCoordinationResult.NotHegemon) {
                    nonWinnerCount++;
                }
            }

            assertThat(acquiredCount).as("Exactly one contender must acquire Hegemony").isEqualTo(1);
            assertThat(nonWinnerCount).as("All other contenders must be rejected with Stale or NotHegemon").isEqualTo(3);
            assertThat(winner).isNotNull();

            // Verify status on the cluster
            assertThat(coordinatorA.checkHegemonStatus(key, winner)).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_HEGEMON);
            for (InstanceId instance : instances) {
                if (!instance.equals(winner)) {
                    assertThat(coordinatorA.checkHegemonStatus(key, instance)).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_NOT_HEGEMON);
                }
            }
        } finally {
            executor.shutdownNow();
        }
    }
}
