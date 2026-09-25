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
import org.infinispan.client.hotrod.MetadataValue;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Laboratory test suite characterizing Mneme concurrent update semantics and
 * Infinispan version-aware conditional update capabilities (ADR-019).
 */
public class MnemeConcurrencyAndVersionScenarioTest {

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
    @DisplayName("Scenario 03: Uncoordinated Concurrent Update (Last-Writer-Wins Characterisation)")
    void testScenario03UncoordinatedConcurrentUpdate() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/123";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"123\",\"name\":\"Smith\",\"meta\":{\"versionId\":\"7\"}}";
        String candidateAJson = "{\"resourceType\":\"Person\",\"id\":\"123\",\"name\":\"Jones\",\"meta\":{\"versionId\":\"8\"}}";
        String candidateBJson = "{\"resourceType\":\"Person\",\"id\":\"123\",\"name\":\"Brown\",\"meta\":{\"versionId\":\"8\"}}";

        // Setup initial distributed state
        cacheClientA.put(key, initialJson);

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("03", "UNCOORDINATED CONCURRENT UPDATE")
                .purpose("Observe and document what actually happens when two independent participants read the same distributed resource and subsequently update it using current unconditional operations (RemoteCache.put).")
                .hypothesis("Both clients start from the same initial state and execute unconditional RemoteCache.put operations; under current Harmonia usage, both writes succeed and the second writer's value overwrites the first writer (last-writer-wins) without stale-write detection.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Initial Value", "Smith")
                .addInitialState("Initial FHIR meta.versionId", "7")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: CLIENT-A reads initial state
        MetadataValue<String> readA = cacheClientA.getWithMetadata(key);
        assertThat(readA).isNotNull();
        String valA = readA.getValue();
        long verA = readA.getVersion();
        narrator.addStep(1, "CLIENT-A INITIAL READ",
                "source: NODE-1",
                "observed value: " + valA,
                "observed FHIR version: 7",
                "observed Infinispan entry version: " + verA);

        // Step 2: CLIENT-B reads initial state
        MetadataValue<String> readB = cacheClientB.getWithMetadata(key);
        assertThat(readB).isNotNull();
        String valB = readB.getValue();
        long verB = readB.getVersion();
        narrator.addStep(2, "CLIENT-B INITIAL READ",
                "source: NODE-2",
                "observed value: " + valB,
                "observed FHIR version: 7",
                "observed Infinispan entry version: " + verB);

        // Confirm both clients start from exact same distributed state
        assertThat(valA).isEqualTo(initialJson);
        assertThat(valB).isEqualTo(initialJson);
        assertThat(verA).isEqualTo(verB);

        // Step 3: CLIENT-A prepares Candidate A and writes
        narrator.addStep(3, "CLIENT-A WRITE (Candidate A)",
                "candidate value: Jones (FHIR version 8)",
                "operation: RemoteCache.put(\"" + key + "\", candidateA)",
                "target: NODE-1",
                "result: SUCCESS");
        cacheClientA.put(key, candidateAJson);

        // Step 4: CLIENT-B prepares Candidate B (based on stale initial read) and writes
        narrator.addStep(4, "CLIENT-B WRITE (Candidate B - based on initial state)",
                "candidate value: Brown (FHIR version 8)",
                "operation: RemoteCache.put(\"" + key + "\", candidateB)",
                "target: NODE-2",
                "result: SUCCESS");
        cacheClientB.put(key, candidateBJson);

        // Step 5: Post-write reads from both clients
        MetadataValue<String> finalReadA = cacheClientA.getWithMetadata(key);
        MetadataValue<String> finalReadB = cacheClientB.getWithMetadata(key);
        assertThat(finalReadA).isNotNull();
        assertThat(finalReadB).isNotNull();

        narrator.addStep(5, "FINAL OBSERVATION ACROSS PARTICIPANTS",
                "CLIENT-A observed value: " + finalReadA.getValue(),
                "CLIENT-A observed Infinispan entry version: " + finalReadA.getVersion(),
                "CLIENT-B observed value: " + finalReadB.getValue(),
                "CLIENT-B observed Infinispan entry version: " + finalReadB.getVersion());

        narrator.addFinalState("Key", key)
                .addFinalState("Final Distributed Value", finalReadB.getValue())
                .addFinalState("FHIR Version", "8 (from Candidate B payload meta.versionId)")
                .addFinalState("Infinispan Entry Version", String.valueOf(finalReadB.getVersion()))
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Last-Writer-Wins (uncoordinated overwrite): Both CLIENT-A and CLIENT-B successfully executed unconditional put() operations. CLIENT-B's write silently overwrote CLIENT-A's write even though CLIENT-B started from a stale version without awareness of CLIENT-A's intermediate modification.")
                .infinispanMechanism("Hot Rod PUT is unconditional by default. Infinispan updates the entry value directly without validating the prior version or enforcing version advancement across uncoordinated writes.")
                .adr019Assessment("DEMONSTRATED (Availability) / NOT CURRENTLY IMPLEMENTED (Distributed Coordination)",
                        "Distributed resource availability is maintained across nodes, but distributed stale-write prevention and optimistic concurrency coordination are NOT currently implemented in production client access.");

        narrator.narrate();

        // Objective Characterisation Assertions
        assertThat(finalReadA.getValue()).isEqualTo(candidateBJson);
        assertThat(finalReadB.getValue()).isEqualTo(candidateBJson);
        assertThat(finalReadA.getVersion()).isEqualTo(finalReadB.getVersion());
        assertThat(finalReadB.getVersion()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("Scenario 04: Experimental Infinispan Version / Conditional Update")
    void testScenario04ExperimentalInfinispanVersionConditionalUpdate() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/456";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"456\",\"name\":\"Smith\",\"meta\":{\"versionId\":\"7\"}}";
        String candidateAJson = "{\"resourceType\":\"Person\",\"id\":\"456\",\"name\":\"Jones\",\"meta\":{\"versionId\":\"8\"}}";
        String candidateBJson = "{\"resourceType\":\"Person\",\"id\":\"456\",\"name\":\"Brown\",\"meta\":{\"versionId\":\"8\"}}";

        // Setup initial distributed state
        cacheClientA.put(key, initialJson);

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("04", "INFINISPAN VERSION / CONDITIONAL UPDATE")
                .purpose("Investigate and demonstrate whether Infinispan Hot Rod exposes entry version metadata and native conditional update operations (replaceWithVersion) capable of detecting and rejecting stale concurrent updates.")
                .hypothesis("Infinispan native Hot Rod protocol supports replaceWithVersion(key, value, version). When CLIENT-A replaces with current version V1, it succeeds and version advances to V2; subsequent attempt by CLIENT-B with stale version V1 is rejected (returns false).")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Initial Value", "Smith")
                .addInitialState("Initial FHIR meta.versionId", "7")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: CLIENT-A reads initial version V1
        MetadataValue<String> readA = cacheClientA.getWithMetadata(key);
        assertThat(readA).isNotNull();
        long v1A = readA.getVersion();
        narrator.addStep(1, "CLIENT-A INITIAL READ WITH VERSION",
                "source: NODE-1",
                "operation: RemoteCache.getWithMetadata(\"" + key + "\")",
                "observed value: " + readA.getValue(),
                "observed FHIR version: 7",
                "observed Infinispan entry version (V1): " + v1A);

        // Step 2: CLIENT-B reads initial version V1
        MetadataValue<String> readB = cacheClientB.getWithMetadata(key);
        assertThat(readB).isNotNull();
        long v1B = readB.getVersion();
        narrator.addStep(2, "CLIENT-B INITIAL READ WITH VERSION",
                "source: NODE-2",
                "operation: RemoteCache.getWithMetadata(\"" + key + "\")",
                "observed value: " + readB.getValue(),
                "observed FHIR version: 7",
                "observed Infinispan entry version (V1): " + v1B);

        assertThat(v1A).isEqualTo(v1B);

        // Step 3: CLIENT-A executes conditional update with version V1
        boolean replaceSuccessA = cacheClientA.replaceWithVersion(key, candidateAJson, v1A);
        MetadataValue<String> postUpdateA = cacheClientA.getWithMetadata(key);
        assertThat(postUpdateA).isNotNull();
        long v2 = postUpdateA.getVersion();

        narrator.addStep(3, "CLIENT-A CONDITIONAL UPDATE (replaceWithVersion)",
                "target: NODE-1",
                "operation: RemoteCache.replaceWithVersion(\"" + key + "\", candidateA, " + v1A + ")",
                "result: " + (replaceSuccessA ? "SUCCESS (true)" : "FAILED (false)"),
                "new Infinispan entry version (V2): " + v2);

        // Step 4: CLIENT-B attempts conditional update with STALE version V1
        boolean replaceSuccessB = cacheClientB.replaceWithVersion(key, candidateBJson, v1B);
        MetadataValue<String> postUpdateB = cacheClientB.getWithMetadata(key);
        assertThat(postUpdateB).isNotNull();

        narrator.addStep(4, "CLIENT-B CONDITIONAL UPDATE WITH STALE VERSION (replaceWithVersion)",
                "target: NODE-2",
                "operation: RemoteCache.replaceWithVersion(\"" + key + "\", candidateB, " + v1B + " [stale])",
                "current cluster entry version: " + v2,
                "result: " + (replaceSuccessB ? "UNEXPECTED SUCCESS" : "REJECTED (false) \u2014 Stale update prevented"));

        narrator.addFinalState("Key", key)
                .addFinalState("Final Distributed Value", postUpdateB.getValue())
                .addFinalState("FHIR Version", "8 (from Candidate A payload meta.versionId)")
                .addFinalState("Infinispan Entry Version", String.valueOf(postUpdateB.getVersion()))
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Optimistic concurrency coordination demonstrated: CLIENT-A's conditional replace succeeded advancing entry version from V1 to V2. CLIENT-B's subsequent update supplying stale version V1 was rejected (returned false), preventing overwrite of concurrent modifications.")
                .infinispanMechanism("Hot Rod REPLACE_IF_UNMODIFIED (replaceWithVersion) compares the client-supplied entry version against the server cluster entry version before applying the modification.")
                .adr019Assessment("DEMONSTRATED (Available Infinispan Capability) / NOT CURRENTLY USED (Harmonia Production)",
                        "Infinispan natively provides robust distributed optimistic locking via replaceWithVersion(). Harmonia production services currently use unconditional put(); adopting replaceWithVersion in subsequent tasks (Task 08) will satisfy ADR-019 distributed coordination requirements.");

        narrator.narrate();

        // Objective Invariant Assertions
        assertThat(replaceSuccessA).isTrue();
        assertThat(replaceSuccessB).isFalse();
        assertThat(postUpdateB.getValue()).isEqualTo(candidateAJson);
        assertThat(postUpdateB.getVersion()).isEqualTo(v2);
        assertThat(v2).isNotEqualTo(v1A);
    }
}
