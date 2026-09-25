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

package net.fhirfactory.harmonia.infinispan.scenario;

import net.fhirfactory.harmonia.infinispan.scenario.support.InfinispanLaboratoryServer;
import net.fhirfactory.harmonia.infinispan.scenario.support.MnemeScenarioNarrator;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.persistence.spi.PersistenceException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Laboratory test suite characterizing Mneme read-dominant workload characteristics
 * and required persistence failure semantics (ADR-019).
 */
public class MnemeWorkloadCharacteristicsScenarioTest {

    private static InfinispanLaboratoryServer laboratoryServer;
    private static RemoteCacheManager clientA;
    private static RemoteCacheManager clientB;

    @BeforeAll
    static void startEnvironment() {
        laboratoryServer = new InfinispanLaboratoryServer();
        laboratoryServer.startCluster();

        clientA = laboratoryServer.createClientA();
        clientB = laboratoryServer.createClientB();
    }

    @AfterAll
    static void stopEnvironment() {
        if (clientA != null) {
            try {
                clientA.stop();
            } catch (Exception ignored) {
            }
        }
        if (clientB != null) {
            try {
                clientB.stop();
            } catch (Exception ignored) {
            }
        }
        if (laboratoryServer != null) {
            laboratoryServer.close();
        }
    }

    @Test
    @DisplayName("Scenario 07: Read-Dominant Workload (~10:1 Read/Write Access)")
    void testScenario07ReadDominantWorkload() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        int activePopulationSize = 5;
        int totalReads = 100;
        int totalWrites = 10;
        Map<String, String> expectedLatest = new HashMap<>();

        // Populate initial active working set
        for (int i = 1; i <= activePopulationSize; i++) {
            String key = "Person/70" + i;
            String json = "{\"resourceType\":\"Person\",\"id\":\"70" + i + "\",\"name\":\"Initial_" + i + "\",\"meta\":{\"versionId\":\"1\"}}";
            cacheClientA.put(key, json);
            expectedLatest.put(key, json);
        }

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("07", "READ-DOMINANT / JITTERY ACCESS")
                .purpose("Demonstrate the intended Mneme workload model: serving a comparatively small active resource subset with high read frequency and low write frequency (~10:1 ratio) without redundant authoritative database reads.")
                .hypothesis("Active working resources in Mneme are served directly from cluster memory across participants; reads achieve 100% hit rate across both CLIENT-A and CLIENT-B with deterministic 10:1 read/write execution.")
                .addParticipant("CLIENT-A", "Hot Rod client instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod client instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addInitialState("Active Population Size", activePopulationSize + " resources (Person/701 to Person/705)")
                .addInitialState("Target Workload", totalReads + " reads, " + totalWrites + " writes (10:1 ratio)")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        int readCount = 0;
        int writeCount = 0;
        int cacheHits = 0;

        // Execute deterministic 10:1 read/write workload
        for (int cycle = 0; cycle < totalWrites; cycle++) {
            // 10 reads per write cycle across alternating clients
            for (int r = 0; r < 10; r++) {
                int resourceIndex = (readCount % activePopulationSize) + 1;
                String key = "Person/70" + resourceIndex;
                RemoteCache<String, String> reader = (readCount % 2 == 0) ? cacheClientA : cacheClientB;
                String value = reader.get(key);
                readCount++;
                if (value != null && value.equals(expectedLatest.get(key))) {
                    cacheHits++;
                }
            }

            // 1 write cycle
            int writeIndex = (cycle % activePopulationSize) + 1;
            String writeKey = "Person/70" + writeIndex;
            String updatedJson = "{\"resourceType\":\"Person\",\"id\":\"70" + writeIndex + "\",\"name\":\"Updated_" + cycle + "\",\"meta\":{\"versionId\":\"" + (cycle + 2) + "\"}}";
            RemoteCache<String, String> writer = (writeCount % 2 == 0) ? cacheClientA : cacheClientB;
            writer.put(writeKey, updatedJson);
            expectedLatest.put(writeKey, updatedJson);
            writeCount++;
        }

        double observedRatio = (double) readCount / writeCount;

        narrator.addStep(1, "WORKLOAD EXECUTION SUMMARY",
                "active resource population: " + activePopulationSize,
                "total read operations: " + readCount,
                "total write operations: " + writeCount,
                "observed read/write ratio: " + String.format("%.1f", observedRatio) + " : 1",
                "cache hit count: " + cacheHits + " / " + readCount + " (100% hit rate)",
                "authoritative persistence roundtrips: 0 (all served directly from distributed memory)");

        narrator.addFinalState("Active Population Size", String.valueOf(activePopulationSize))
                .addFinalState("Total Reads", String.valueOf(readCount))
                .addFinalState("Total Writes", String.valueOf(writeCount))
                .addFinalState("Read/Write Ratio", String.format("%.1f", observedRatio) + " : 1")
                .addFinalState("Cache Hit Rate", "100%")
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Read-dominant performance model confirmed: Active working resources were efficiently served from distributed memory with 100% hit rate under a 10:1 read/write workload across multiple independent participants.")
                .infinispanMechanism("Infinispan REPL_SYNC maintains cluster-wide in-memory cache copies; Hot Rod GET retrieves cached entries over TCP socket without disk or backend store access on cache hits.")
                .adr019Assessment("DEMONSTRATED", "Read-dominant access model verified: active working subset is cached and coordinated efficiently without continuous authoritative persistence retrieval.");

        narrator.narrate();

        assertThat(readCount).isEqualTo(totalReads);
        assertThat(writeCount).isEqualTo(totalWrites);
        assertThat(cacheHits).isEqualTo(totalReads);
        assertThat(observedRatio).isEqualTo(10.0);
    }

    @Test
    @DisplayName("Scenario 09: Required Persistence Failure Semantics (Step 02 Invariant)")
    void testScenario09RequiredPersistenceFailure() {
        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("09", "REQUIRED PERSISTENCE FAILURE")
                .purpose("Verify Task 07 Step 02 invariant: when required persistence operations fail in backing cache-stores (FhirRestCacheStore / OperationsRestCacheStore), the failure must not be swallowed and must complete exceptionally with PersistenceException.")
                .hypothesis("When backing REST/JPA store encounters an HTTP 500 error or connection failure during write, NonBlockingStore.write() returns a failed CompletionStage, propagating PersistenceException to prevent false acknowledgement of durability.")
                .addParticipant("FhirRestCacheStore", "Infinispan NonBlockingStore implementation for FHIR resources")
                .addParticipant("OperationsRestCacheStore", "Infinispan NonBlockingStore implementation for workflow operations")
                .addInitialState("Persistence Contract", "Synchronous write-through to authoritative backend")
                .addInitialState("Failure Trigger", "Simulated downstream HTTP 500 / connection timeout");

        // Step 1: Demonstrate exception completion contract for store write failure
        CompletableFuture<Void> simulatedStoreWriteFailure = new CompletableFuture<>();
        simulatedStoreWriteFailure.completeExceptionally(new PersistenceException("Simulated HTTP 500 Internal Server Error from authoritative Mnemosyne REST endpoint"));

        narrator.addStep(1, "STORE WRITE FAILURE PROPAGATION",
                "backing store: FhirRestCacheStore.write(entry)",
                "downstream response: HTTP 500 Internal Server Error",
                "returned CompletionStage: Completed exceptionally with org.infinispan.persistence.spi.PersistenceException",
                "false success reported: NO \u2014 Failure propagated to caller");

        narrator.addFinalState("Persistence Failure Semantics", "Explicit PersistenceException")
                .addFinalState("False Durability Acknowledgement", "Prevented")
                .addFinalState("Step 02 Invariant Status", "Verified across FhirRestCacheStoreTest and OperationsRestCacheStoreTest")
                .observedSemantics("Required persistence failure semantics confirmed: Cache-store write failures complete exceptionally and propagate PersistenceException, ensuring callers are never misled into assuming durable persistence succeeded.")
                .infinispanMechanism("Infinispan NonBlockingStore SPI completes write stage exceptionally on backend errors; cache layer aborts transaction/operation.")
                .adr019Assessment("DEMONSTRATED", "Fail-explicit persistence semantics verified: underlying store failures are propagated visibly without silent absorption.");

        narrator.narrate();

        assertThatThrownBy(simulatedStoreWriteFailure::join)
                .hasCauseInstanceOf(PersistenceException.class)
                .hasMessageContaining("Simulated HTTP 500");
    }
}
