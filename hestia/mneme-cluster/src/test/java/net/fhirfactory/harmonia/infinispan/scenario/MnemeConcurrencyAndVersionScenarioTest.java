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
    private static RemoteCacheManager clientC;

    @BeforeAll
    static void startEnvironment() {
        laboratoryServer = new InfinispanLaboratoryServer();
        laboratoryServer.startCluster();

        clientA = laboratoryServer.createClientA();
        clientB = laboratoryServer.createClientB();
        clientC = laboratoryServer.createClientC();
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
        if (clientC != null) {
            try {
                clientC.stop();
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
    @DisplayName("Scenario 03-A (Part A): Multi-Field Uncoordinated Lost Update (Unconditional PUT)")
    void testPartAMultiFieldLostUpdate() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/mf-101";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"mf-101\",\"name\":\"Smith\",\"telecom\":\"555-0100\",\"address\":\"100 Main St\",\"meta\":{\"versionId\":\"1\"}}";
        // Candidate A modifies telecom only, preparing payload from initial state
        String candidateAJson = "{\"resourceType\":\"Person\",\"id\":\"mf-101\",\"name\":\"Smith\",\"telecom\":\"555-9999\",\"address\":\"100 Main St\",\"meta\":{\"versionId\":\"2\"}}";
        // Candidate B modifies address only, preparing payload from initial state
        String candidateBJson = "{\"resourceType\":\"Person\",\"id\":\"mf-101\",\"name\":\"Smith\",\"telecom\":\"555-0100\",\"address\":\"200 Elm St\",\"meta\":{\"versionId\":\"2\"}}";

        // Setup initial distributed state
        cacheClientA.put(key, initialJson);

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("03-A", "MULTI-FIELD UNCOORDINATED LOST UPDATE (UNCONDITIONAL PUT)")
                .purpose("Demonstrate the multi-field lost update problem where two participants modify distinct fields of the same resource concurrently from a common initial snapshot using unconditional RemoteCache.put operations.")
                .hypothesis("Both participants read the initial multi-field resource. Participant A updates telecom and writes; Participant B updates address and writes. Under unconditional PUT, Participant B's write overwrites the whole resource, causing Participant A's telecom change to be lost.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Initial Value", "name=Smith, telecom=555-0100, address=100 Main St")
                .addInitialState("Initial FHIR meta.versionId", "1")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: CLIENT-A and CLIENT-B read initial state
        MetadataValue<String> readA = cacheClientA.getWithMetadata(key);
        MetadataValue<String> readB = cacheClientB.getWithMetadata(key);
        assertThat(readA).isNotNull();
        assertThat(readB).isNotNull();

        narrator.addStep(1, "CONCURRENT INITIAL READS",
                "CLIENT-A read value: " + readA.getValue(),
                "CLIENT-A observed entry version: " + readA.getVersion(),
                "CLIENT-B read value: " + readB.getValue(),
                "CLIENT-B observed entry version: " + readB.getVersion());

        // Step 2: CLIENT-A writes candidate A (telecom change)
        cacheClientA.put(key, candidateAJson);
        narrator.addStep(2, "CLIENT-A UNCONDITIONAL PUT (telecom -> 555-9999)",
                "operation: RemoteCache.put(\"" + key + "\", candidateA)",
                "payload change: telecom updated to 555-9999, address kept as 100 Main St",
                "result: SUCCESS");

        // Step 3: CLIENT-B writes candidate B (address change, based on stale initial read)
        cacheClientB.put(key, candidateBJson);
        narrator.addStep(3, "CLIENT-B UNCONDITIONAL PUT (address -> 200 Elm St)",
                "operation: RemoteCache.put(\"" + key + "\", candidateB)",
                "payload change: address updated to 200 Elm St, telecom kept as 555-0100 (stale)",
                "result: SUCCESS (silent overwrite of entire resource)");

        // Step 4: Final observation
        MetadataValue<String> finalReadA = cacheClientA.getWithMetadata(key);
        MetadataValue<String> finalReadB = cacheClientB.getWithMetadata(key);
        assertThat(finalReadA).isNotNull();
        assertThat(finalReadB).isNotNull();

        narrator.addStep(4, "FINAL OBSERVATION ACROSS PARTICIPANTS",
                "CLIENT-A observed value: " + finalReadA.getValue(),
                "CLIENT-B observed value: " + finalReadB.getValue(),
                "Observed telecom field: 555-0100 (CLIENT-A's update to 555-9999 was LOST)",
                "Observed address field: 200 Elm St (CLIENT-B's update was APPLIED)");

        narrator.addFinalState("Key", key)
                .addFinalState("Final Value", finalReadB.getValue())
                .addFinalState("Telecom Field", "555-0100 (LOST UPDATE: 555-9999 obliterated)")
                .addFinalState("Address Field", "200 Elm St")
                .addFinalState("Infinispan Entry Version", String.valueOf(finalReadB.getVersion()))
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Multi-Field Lost Update: Unconditional whole-object PUT operations lack granular field-level merging. When CLIENT-B overwrote the resource, CLIENT-A's independent telecom modification was completely erased without conflict notification.")
                .infinispanMechanism("Hot Rod PUT replaces the entire string/JSON payload at the specified key unconditionally without version checking or field-level diffing.")
                .adr019Assessment("DEMONSTRATED (Lost Update Risk) / NOT CURRENTLY PREVENTED (Production Put Semantics)",
                        "Demonstrates the fundamental risk of uncoordinated writes on multi-field clinical resources. Coordinating writes with optimistic version checks or patch/merge semantics is required to prevent lost updates under ADR-019.");

        narrator.narrate();

        // Invariant Assertions
        assertThat(finalReadA.getValue()).isEqualTo(candidateBJson);
        assertThat(finalReadB.getValue()).isEqualTo(candidateBJson);
        assertThat(finalReadA.getValue()).contains("200 Elm St");
        assertThat(finalReadA.getValue()).contains("555-0100");
        assertThat(finalReadA.getValue()).doesNotContain("555-9999");
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

    @Test
    @DisplayName("Scenario 04-A (Part B): Multi-Field Version-Aware Conditional Update (replaceWithVersion)")
    void testPartBVersionAwareConditionalUpdate() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/mf-102";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"mf-102\",\"name\":\"Smith\",\"telecom\":\"555-0100\",\"address\":\"100 Main St\",\"meta\":{\"versionId\":\"1\"}}";
        String candidateAJson = "{\"resourceType\":\"Person\",\"id\":\"mf-102\",\"name\":\"Smith\",\"telecom\":\"555-9999\",\"address\":\"100 Main St\",\"meta\":{\"versionId\":\"2\"}}";
        String candidateBJson = "{\"resourceType\":\"Person\",\"id\":\"mf-102\",\"name\":\"Smith\",\"telecom\":\"555-0100\",\"address\":\"200 Elm St\",\"meta\":{\"versionId\":\"2\"}}";

        // Setup initial distributed state
        cacheClientA.put(key, initialJson);

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("04-A", "MULTI-FIELD VERSION-AWARE CONDITIONAL UPDATE (replaceWithVersion)")
                .purpose("Demonstrate how native Hot Rod replaceWithVersion prevents multi-field lost updates by rejecting writes that present a stale entry version token.")
                .hypothesis("When both clients read the same initial multi-field version token, Client-A's replaceWithVersion will succeed (advancing the cluster entry version token) and Client-B's attempt using the stale token will be rejected, protecting Client-A's update from being overwritten.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Initial Value", "name=Smith, telecom=555-0100, address=100 Main St")
                .addInitialState("Initial FHIR meta.versionId", "1")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: Initial reads with metadata
        MetadataValue<String> readA = cacheClientA.getWithMetadata(key);
        MetadataValue<String> readB = cacheClientB.getWithMetadata(key);
        assertThat(readA).isNotNull();
        assertThat(readB).isNotNull();
        long vInitialA = readA.getVersion();
        long vInitialB = readB.getVersion();
        assertThat(vInitialA).isEqualTo(vInitialB);

        narrator.addStep(1, "CONCURRENT READS WITH VERSION METADATA",
                "CLIENT-A initial version token: " + vInitialA,
                "CLIENT-B initial version token: " + vInitialB);

        // Step 2: CLIENT-A executes conditional update
        boolean replaceSuccessA = cacheClientA.replaceWithVersion(key, candidateAJson, vInitialA);
        MetadataValue<String> afterA = cacheClientA.getWithMetadata(key);
        assertThat(afterA).isNotNull();
        long vAfterA = afterA.getVersion();

        narrator.addStep(2, "CLIENT-A CONDITIONAL UPDATE (telecom -> 555-9999)",
                "operation: RemoteCache.replaceWithVersion(\"" + key + "\", candidateA, " + vInitialA + ")",
                "result: " + (replaceSuccessA ? "SUCCESS (true)" : "FAILED (false)"),
                "new cluster entry version: " + vAfterA);

        // Step 3: CLIENT-B attempts conditional update with stale version
        boolean replaceSuccessB = cacheClientB.replaceWithVersion(key, candidateBJson, vInitialB);
        MetadataValue<String> finalRead = cacheClientB.getWithMetadata(key);
        assertThat(finalRead).isNotNull();

        narrator.addStep(3, "CLIENT-B CONDITIONAL UPDATE WITH STALE TOKEN (address -> 200 Elm St)",
                "operation: RemoteCache.replaceWithVersion(\"" + key + "\", candidateB, " + vInitialB + " [stale])",
                "current cluster entry version: " + vAfterA,
                "result: " + (replaceSuccessB ? "UNEXPECTED SUCCESS" : "REJECTED (false) \u2014 Conflict detected"));

        narrator.addFinalState("Key", key)
                .addFinalState("Final Value", finalRead.getValue())
                .addFinalState("Telecom Field", "555-9999 (CLIENT-A update PRESERVED)")
                .addFinalState("Address Field", "100 Main St (CLIENT-B stale update REJECTED)")
                .addFinalState("Infinispan Entry Version", String.valueOf(finalRead.getVersion()))
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Optimistic Concurrency Conflict Detection: CLIENT-A's write advanced the cluster version token. CLIENT-B's subsequent update presenting the stale initial token was rejected with return value false, preventing silent erasure of CLIENT-A's telecom change.")
                .infinispanMechanism("Hot Rod REPLACE_IF_UNMODIFIED verifies the client-supplied version against the entry's version metadata on the owning cluster node before committing the modification.")
                .adr019Assessment("DEMONSTRATED (Available Infinispan Capability) / TARGET ARCHITECTURE (Task 08)",
                        "Hot Rod replaceWithVersion provides atomic distributed compare-and-set semantics that prevent lost updates across independent client nodes.");

        narrator.narrate();

        // Invariant Assertions
        assertThat(replaceSuccessA).isTrue();
        assertThat(replaceSuccessB).isFalse();
        assertThat(finalRead.getValue()).isEqualTo(candidateAJson);
        assertThat(finalRead.getValue()).contains("555-9999");
        assertThat(finalRead.getValue()).contains("100 Main St");
        assertThat(finalRead.getVersion()).isEqualTo(vAfterA);
        assertThat(vAfterA).isNotEqualTo(vInitialA);
    }

    @Test
    @DisplayName("Scenario 04-B (Part C): Conflict Detection Followed by Reread, Reapply, and Retry")
    void testPartCConflictRereadReapplyRetry() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/mf-103";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"mf-103\",\"name\":\"Smith\",\"telecom\":\"555-0100\",\"address\":\"100 Main St\",\"meta\":{\"versionId\":\"1\"}}";
        String candidateAJson = "{\"resourceType\":\"Person\",\"id\":\"mf-103\",\"name\":\"Smith\",\"telecom\":\"555-9999\",\"address\":\"100 Main St\",\"meta\":{\"versionId\":\"2\"}}";
        // Client-B initially intends to update address only based on initial state
        String candidateBInitialJson = "{\"resourceType\":\"Person\",\"id\":\"mf-103\",\"name\":\"Smith\",\"telecom\":\"555-0100\",\"address\":\"200 Elm St\",\"meta\":{\"versionId\":\"2\"}}";

        // Setup initial distributed state
        cacheClientA.put(key, initialJson);

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("04-B", "CONFLICT RESOLUTION WITH REREAD, REAPPLY, AND RETRY")
                .purpose("Demonstrate the complete optimistic concurrency retry pattern: when a conditional update is rejected due to concurrent modification, the client rereads the current state, merges its intended field modification with the latest server state, and retries replaceWithVersion with the new version token, successfully preserving both logical updates.")
                .hypothesis("Following a rejected conditional replace, Client-B can reread current state, reapply its address modification on top of Client-A's telecom modification, and retry replaceWithVersion to achieve convergent distributed update without data loss.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Initial Value", "name=Smith, telecom=555-0100, address=100 Main St")
                .addInitialState("Initial FHIR meta.versionId", "1")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: Initial read by both clients
        MetadataValue<String> readA = cacheClientA.getWithMetadata(key);
        MetadataValue<String> readB = cacheClientB.getWithMetadata(key);
        assertThat(readA).isNotNull();
        assertThat(readB).isNotNull();
        long vInitial = readA.getVersion();

        narrator.addStep(1, "INITIAL CONCURRENT READS",
                "CLIENT-A initial version token: " + vInitial,
                "CLIENT-B initial version token: " + vInitial);

        // Step 2: CLIENT-A executes replaceWithVersion (telecom -> 555-9999)
        boolean replaceSuccessA = cacheClientA.replaceWithVersion(key, candidateAJson, vInitial);
        MetadataValue<String> afterA = cacheClientA.getWithMetadata(key);
        assertThat(afterA).isNotNull();
        long vAfterA = afterA.getVersion();

        narrator.addStep(2, "CLIENT-A APPLIES UPDATE (telecom -> 555-9999)",
                "operation: RemoteCache.replaceWithVersion(\"" + key + "\", candidateA, " + vInitial + ")",
                "result: SUCCESS (true)",
                "new cluster entry version: " + vAfterA);

        // Step 3: CLIENT-B attempts conditional update with initial token (conflict!)
        boolean replaceSuccessBInitial = cacheClientB.replaceWithVersion(key, candidateBInitialJson, vInitial);
        narrator.addStep(3, "CLIENT-B ATTEMPTS UPDATE WITH STALE TOKEN",
                "operation: RemoteCache.replaceWithVersion(\"" + key + "\", candidateBInitial, " + vInitial + ")",
                "result: REJECTED (false) \u2014 Stale token detected");

        // Step 4: CLIENT-B handles conflict: rereads latest state and reapplies its address change
        MetadataValue<String> rereadB = cacheClientB.getWithMetadata(key);
        assertThat(rereadB).isNotNull();
        long vRereadB = rereadB.getVersion();
        assertThat(vRereadB).isEqualTo(vAfterA);

        // Merge: take reread state (which has telecom: 555-9999) and apply address: 200 Elm St
        String candidateBRetriedJson = "{\"resourceType\":\"Person\",\"id\":\"mf-103\",\"name\":\"Smith\",\"telecom\":\"555-9999\",\"address\":\"200 Elm St\",\"meta\":{\"versionId\":\"3\"}}";

        narrator.addStep(4, "CLIENT-B OCC RESOLUTION (Reread + Merge + Retry)",
                "reread observed value: " + rereadB.getValue(),
                "reread observed version: " + vRereadB,
                "merged payload: telecom=555-9999 (from Client-A), address=200 Elm St (from Client-B), meta.versionId=3",
                "retry operation: RemoteCache.replaceWithVersion(\"" + key + "\", candidateBRetried, " + vRereadB + ")");

        boolean replaceSuccessBRetry = cacheClientB.replaceWithVersion(key, candidateBRetriedJson, vRereadB);
        MetadataValue<String> finalRead = cacheClientB.getWithMetadata(key);
        assertThat(finalRead).isNotNull();
        long vFinal = finalRead.getVersion();

        narrator.addStep(5, "CLIENT-B RETRY OUTCOME",
                "result: " + (replaceSuccessBRetry ? "SUCCESS (true)" : "FAILED (false)"),
                "final cluster entry version: " + vFinal);

        narrator.addFinalState("Key", key)
                .addFinalState("Final Value", finalRead.getValue())
                .addFinalState("Telecom Field", "555-9999 (CLIENT-A change PRESERVED)")
                .addFinalState("Address Field", "200 Elm St (CLIENT-B change PRESERVED)")
                .addFinalState("FHIR meta.versionId", "3")
                .addFinalState("Infinispan Entry Version", String.valueOf(vFinal))
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Optimistic Concurrency Resolution with Convergence: Stale write rejection triggered an application-level reread and merge. Both Client-A's telecom change and Client-B's address change are successfully merged and preserved in the final state without data loss.")
                .infinispanMechanism("Hot Rod replaceWithVersion combined with client-side reread and reapply pattern.")
                .adr019Assessment("DEMONSTRATED (Optimistic Concurrency Resolution Pattern) / RECOMMENDED (Harmonia Service Workflows)",
                        "Proves that combining Infinispan Hot Rod version metadata with optimistic retry loops satisfies ADR-019 distributed coordination requirements.");

        narrator.narrate();

        // Invariant Assertions
        assertThat(replaceSuccessA).isTrue();
        assertThat(replaceSuccessBInitial).isFalse();
        assertThat(replaceSuccessBRetry).isTrue();
        assertThat(finalRead.getValue()).isEqualTo(candidateBRetriedJson);
        assertThat(finalRead.getValue()).contains("555-9999");
        assertThat(finalRead.getValue()).contains("200 Elm St");
        assertThat(vFinal).isNotEqualTo(vAfterA);
        assertThat(vAfterA).isNotEqualTo(vInitial);
    }

    @Test
    @DisplayName("Scenario 04-C (Part D): Three-Way Distributed Concurrency (CAS Coordination)")
    void testPartDThreeWayConcurrency() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);
        RemoteCache<String, String> cacheClientC = clientC.getCache(cacheName);

        String key = "Person/mf-104";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"mf-104\",\"name\":\"Smith\",\"telecom\":\"555-0100\",\"meta\":{\"versionId\":\"1\"}}";
        String candidateAJson = "{\"resourceType\":\"Person\",\"id\":\"mf-104\",\"name\":\"Smith\",\"telecom\":\"555-AAAA\",\"meta\":{\"versionId\":\"2\"}}";
        String candidateBJson = "{\"resourceType\":\"Person\",\"id\":\"mf-104\",\"name\":\"Smith\",\"telecom\":\"555-BBBB\",\"meta\":{\"versionId\":\"2\"}}";
        String candidateCJson = "{\"resourceType\":\"Person\",\"id\":\"mf-104\",\"name\":\"Smith\",\"telecom\":\"555-CCCC\",\"meta\":{\"versionId\":\"2\"}}";

        // Setup initial distributed state
        cacheClientA.put(key, initialJson);

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("04-C", "THREE-WAY DISTRIBUTED CONCURRENCY (CAS COORDINATION)")
                .purpose("Demonstrate three-way concurrent conditional updates against the same starting version token across three independent Hot Rod client participants.")
                .hypothesis("When three independent clients (Client-A, Client-B, Client-C) read the same initial entry version token, exactly one client's replaceWithVersion will succeed, advancing the cluster entry version token and causing the other two attempts to be rejected.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("CLIENT-C", "Hot Rod RemoteCacheManager instance #3 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Initial Value", "name=Smith, telecom=555-0100")
                .addInitialState("Initial FHIR meta.versionId", "1")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: All three clients read the initial state and version token
        MetadataValue<String> readA = cacheClientA.getWithMetadata(key);
        MetadataValue<String> readB = cacheClientB.getWithMetadata(key);
        MetadataValue<String> readC = cacheClientC.getWithMetadata(key);
        assertThat(readA).isNotNull();
        assertThat(readB).isNotNull();
        assertThat(readC).isNotNull();
        long vInitial = readA.getVersion();
        assertThat(readB.getVersion()).isEqualTo(vInitial);
        assertThat(readC.getVersion()).isEqualTo(vInitial);

        narrator.addStep(1, "THREE-WAY INITIAL READS",
                "CLIENT-A observed version token: " + vInitial,
                "CLIENT-B observed version token: " + vInitial,
                "CLIENT-C observed version token: " + vInitial);

        // Step 2: CLIENT-A executes replaceWithVersion with vInitial (first to arrive)
        boolean replaceSuccessA = cacheClientA.replaceWithVersion(key, candidateAJson, vInitial);
        MetadataValue<String> afterA = cacheClientA.getWithMetadata(key);
        assertThat(afterA).isNotNull();
        long vAfterA = afterA.getVersion();

        narrator.addStep(2, "CLIENT-A CONDITIONAL UPDATE (First Arrival)",
                "operation: RemoteCache.replaceWithVersion(\"" + key + "\", candidateA, " + vInitial + ")",
                "result: " + (replaceSuccessA ? "SUCCESS (true)" : "FAILED (false)"),
                "new cluster entry version: " + vAfterA);

        // Step 3: CLIENT-B attempts replaceWithVersion with vInitial (stale)
        boolean replaceSuccessB = cacheClientB.replaceWithVersion(key, candidateBJson, vInitial);
        narrator.addStep(3, "CLIENT-B CONDITIONAL UPDATE (Second Arrival)",
                "operation: RemoteCache.replaceWithVersion(\"" + key + "\", candidateB, " + vInitial + " [stale])",
                "result: " + (replaceSuccessB ? "UNEXPECTED SUCCESS" : "REJECTED (false)"));

        // Step 4: CLIENT-C attempts replaceWithVersion with vInitial (stale)
        boolean replaceSuccessC = cacheClientC.replaceWithVersion(key, candidateCJson, vInitial);
        narrator.addStep(4, "CLIENT-C CONDITIONAL UPDATE (Third Arrival)",
                "operation: RemoteCache.replaceWithVersion(\"" + key + "\", candidateC, " + vInitial + " [stale])",
                "result: " + (replaceSuccessC ? "UNEXPECTED SUCCESS" : "REJECTED (false)"));

        // Step 5: Final observation across all three clients
        MetadataValue<String> finalReadA = cacheClientA.getWithMetadata(key);
        MetadataValue<String> finalReadB = cacheClientB.getWithMetadata(key);
        MetadataValue<String> finalReadC = cacheClientC.getWithMetadata(key);
        assertThat(finalReadA).isNotNull();
        assertThat(finalReadB).isNotNull();
        assertThat(finalReadC).isNotNull();

        narrator.addFinalState("Key", key)
                .addFinalState("Final Value", finalReadA.getValue())
                .addFinalState("Winning Participant", "CLIENT-A (telecom=555-AAAA)")
                .addFinalState("Rejected Participants", "CLIENT-B, CLIENT-C")
                .addFinalState("Infinispan Entry Version", String.valueOf(finalReadA.getVersion()))
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Exactly-One-Winner Atomic CAS: Across three independent concurrent participants starting from the same state, exactly one write succeeded and all subsequent attempts with the original version token were rejected.")
                .infinispanMechanism("Server-side atomic Compare-And-Set serialization over JGroups REPL_SYNC cluster state.")
                .adr019Assessment("DEMONSTRATED (Multi-Participant Coordination Invariant)",
                        "Verifies that Infinispan Hot Rod CAS coordination scales deterministically across N concurrent client participants.");

        narrator.narrate();

        // Invariant Assertions
        assertThat(replaceSuccessA).isTrue();
        assertThat(replaceSuccessB).isFalse();
        assertThat(replaceSuccessC).isFalse();
        assertThat(finalReadA.getValue()).isEqualTo(candidateAJson);
        assertThat(finalReadB.getValue()).isEqualTo(candidateAJson);
        assertThat(finalReadC.getValue()).isEqualTo(candidateAJson);
        assertThat(finalReadA.getVersion()).isEqualTo(vAfterA);
    }

    @Test
    @DisplayName("Scenario 04-D (Part E): Same-Client vs Different-Client Stale Representations")
    void testPartESameClientVsDifferentClient() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/mf-105";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"mf-105\",\"name\":\"Smith\",\"meta\":{\"versionId\":\"1\"}}";
        String candidate1Json = "{\"resourceType\":\"Person\",\"id\":\"mf-105\",\"name\":\"Smith-V1-Updated\",\"meta\":{\"versionId\":\"2\"}}";
        String candidate2Json = "{\"resourceType\":\"Person\",\"id\":\"mf-105\",\"name\":\"Smith-V2-StaleSameClient\",\"meta\":{\"versionId\":\"3\"}}";
        String candidate3Json = "{\"resourceType\":\"Person\",\"id\":\"mf-105\",\"name\":\"Smith-V2-StaleDiffClient\",\"meta\":{\"versionId\":\"3\"}}";

        // Setup initial distributed state
        cacheClientA.put(key, initialJson);

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("04-D", "SAME-CLIENT VS DIFFERENT-CLIENT STALE REPRESENTATIONS")
                .purpose("Demonstrate that entry version tokens represent cluster-wide entry metadata and enforce conflict rejection identically regardless of whether the stale token is presented by the same client connection or a different client connection.")
                .hypothesis("replaceWithVersion evaluates cluster entry version metadata on the server; stale version tokens are rejected equally whether submitted by the client that previously updated the entry or a distinct client participant.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Initial Value", "name=Smith")
                .addInitialState("Initial FHIR meta.versionId", "1")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: Capture initial version token v1 via Client-A and Client-B
        MetadataValue<String> snapA = cacheClientA.getWithMetadata(key);
        MetadataValue<String> snapB = cacheClientB.getWithMetadata(key);
        assertThat(snapA).isNotNull();
        assertThat(snapB).isNotNull();
        long v1 = snapA.getVersion();

        narrator.addStep(1, "INITIAL VERSION CAPTURE",
                "CLIENT-A initial version snapshot: " + v1,
                "CLIENT-B initial version snapshot: " + v1);

        // Step 2: CLIENT-A updates the entry successfully with v1 -> advances version to v2
        boolean replaceSuccess1 = cacheClientA.replaceWithVersion(key, candidate1Json, v1);
        MetadataValue<String> after1 = cacheClientA.getWithMetadata(key);
        assertThat(after1).isNotNull();
        long v2 = after1.getVersion();

        narrator.addStep(2, "CLIENT-A ADVANCES CLUSTER VERSION TO V2",
                "operation: CLIENT-A replaceWithVersion(\"" + key + "\", candidate1, " + v1 + ")",
                "result: SUCCESS (true)",
                "new cluster entry version: " + v2);

        // Step 3: Same-Client Stale Attempt: CLIENT-A tries to update again using its OWN stale v1 token
        boolean replaceSuccessSameClient = cacheClientA.replaceWithVersion(key, candidate2Json, v1);
        narrator.addStep(3, "SAME-CLIENT STALE UPDATE ATTEMPT",
                "operation: CLIENT-A replaceWithVersion(\"" + key + "\", candidate2, " + v1 + " [stale from earlier step])",
                "result: " + (replaceSuccessSameClient ? "UNEXPECTED SUCCESS" : "REJECTED (false)"));

        // Step 4: Different-Client Stale Attempt: CLIENT-B tries to update using its stale v1 token
        boolean replaceSuccessDiffClient = cacheClientB.replaceWithVersion(key, candidate3Json, v1);
        narrator.addStep(4, "DIFFERENT-CLIENT STALE UPDATE ATTEMPT",
                "operation: CLIENT-B replaceWithVersion(\"" + key + "\", candidate3, " + v1 + " [stale from earlier step])",
                "result: " + (replaceSuccessDiffClient ? "UNEXPECTED SUCCESS" : "REJECTED (false)"));

        // Step 5: Final observation
        MetadataValue<String> finalRead = cacheClientA.getWithMetadata(key);
        assertThat(finalRead).isNotNull();

        narrator.addFinalState("Key", key)
                .addFinalState("Final Value", finalRead.getValue())
                .addFinalState("Same-Client Stale Write Rejected", String.valueOf(!replaceSuccessSameClient))
                .addFinalState("Different-Client Stale Write Rejected", String.valueOf(!replaceSuccessDiffClient))
                .addFinalState("Infinispan Entry Version", String.valueOf(finalRead.getVersion()))
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Cluster-Scoped Entry Versioning: Version tokens are strictly server-side entry metadata headers, not client-scoped or connection-bound tokens. Stale tokens are rejected identically regardless of client origin.")
                .infinispanMechanism("Server-side version comparison against internal cache entry metadata header.")
                .adr019Assessment("DEMONSTRATED (Cluster-Wide State Invariant)",
                        "Confirms that optimistic concurrency is strictly tied to entry state rather than client identity.");

        narrator.narrate();

        // Invariant Assertions
        assertThat(replaceSuccess1).isTrue();
        assertThat(replaceSuccessSameClient).isFalse();
        assertThat(replaceSuccessDiffClient).isFalse();
        assertThat(finalRead.getValue()).isEqualTo(candidate1Json);
        assertThat(finalRead.getVersion()).isEqualTo(v2);
    }

    @Test
    @DisplayName("Scenario 04-E (Part F): Conditional Removal (removeWithVersion)")
    void testPartFConditionalRemove() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/mf-106";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"mf-106\",\"name\":\"Smith\",\"meta\":{\"versionId\":\"1\"}}";
        String candidateAJson = "{\"resourceType\":\"Person\",\"id\":\"mf-106\",\"name\":\"Smith-Updated\",\"meta\":{\"versionId\":\"2\"}}";

        // Setup initial distributed state
        cacheClientA.put(key, initialJson);

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("04-E", "CONDITIONAL REMOVAL (removeWithVersion)")
                .purpose("Demonstrate conditional entry removal using Hot Rod removeWithVersion, verifying that delete operations are rejected if the entry was modified concurrently by another participant.")
                .hypothesis("removeWithVersion(key, staleVersion) will return false and preserve the modified entry; removeWithVersion(key, currentVersion) will succeed and delete the entry.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Initial Value", "name=Smith")
                .addInitialState("Initial FHIR meta.versionId", "1")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: CLIENT-B reads initial version token vInitial
        MetadataValue<String> readB = cacheClientB.getWithMetadata(key);
        assertThat(readB).isNotNull();
        long vInitial = readB.getVersion();

        narrator.addStep(1, "CLIENT-B CAPTURES INITIAL VERSION",
                "CLIENT-B initial version token: " + vInitial);

        // Step 2: CLIENT-A modifies the entry, advancing the cluster version
        boolean replaceSuccessA = cacheClientA.replaceWithVersion(key, candidateAJson, vInitial);
        MetadataValue<String> afterA = cacheClientA.getWithMetadata(key);
        assertThat(afterA).isNotNull();
        long vAfterA = afterA.getVersion();

        narrator.addStep(2, "CLIENT-A UPDATES ENTRY CONCURRENTLY",
                "operation: CLIENT-A replaceWithVersion(\"" + key + "\", candidateA, " + vInitial + ")",
                "result: SUCCESS (true)",
                "new cluster entry version: " + vAfterA);

        // Step 3: CLIENT-B attempts conditional remove using stale vInitial
        boolean removeSuccessStale = cacheClientB.removeWithVersion(key, vInitial);
        MetadataValue<String> readAfterStaleRemove = cacheClientA.getWithMetadata(key);
        assertThat(readAfterStaleRemove).isNotNull();

        narrator.addStep(3, "CLIENT-B ATTEMPTS CONDITIONAL REMOVE WITH STALE VERSION",
                "operation: CLIENT-B removeWithVersion(\"" + key + "\", " + vInitial + " [stale])",
                "result: " + (removeSuccessStale ? "UNEXPECTED SUCCESS" : "REJECTED (false) \u2014 Deletion prevented"),
                "entry still present in cluster: " + readAfterStaleRemove.getValue());

        // Step 4: CLIENT-B rereads the updated version and executes conditional remove with current version
        MetadataValue<String> rereadB = cacheClientB.getWithMetadata(key);
        assertThat(rereadB).isNotNull();
        long vCurrent = rereadB.getVersion();
        assertThat(vCurrent).isEqualTo(vAfterA);

        boolean removeSuccessCurrent = cacheClientB.removeWithVersion(key, vCurrent);
        String finalGetA = cacheClientA.get(key);
        String finalGetB = cacheClientB.get(key);

        narrator.addStep(4, "CLIENT-B EXECUTES CONDITIONAL REMOVE WITH CURRENT VERSION",
                "operation: CLIENT-B removeWithVersion(\"" + key + "\", " + vCurrent + ")",
                "result: " + (removeSuccessCurrent ? "SUCCESS (true)" : "FAILED (false)"),
                "CLIENT-A post-deletion get: " + finalGetA,
                "CLIENT-B post-deletion get: " + finalGetB);

        narrator.addFinalState("Key", key)
                .addFinalState("Final Cache State", "NULL (entry deleted)")
                .addFinalState("Stale Remove Protected Concurrently Modified Entry", String.valueOf(!removeSuccessStale))
                .addFinalState("Current Remove Succeeded", String.valueOf(removeSuccessCurrent))
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Optimistic Conditional Deletion: Stale removal attempts are rejected when an entry has been modified concurrently, preventing unintended deletion of updated resources. Removal succeeds only when presenting the matching cluster version token.")
                .infinispanMechanism("Hot Rod REMOVE_IF_UNMODIFIED protocol operation.")
                .adr019Assessment("DEMONSTRATED (Conditional Deletion Capability)",
                        "Hot Rod removeWithVersion enables safe lifecycle management (e.g. task completion/eviction) without risking deletion of concurrently updated records.");

        narrator.narrate();

        // Invariant Assertions
        assertThat(replaceSuccessA).isTrue();
        assertThat(removeSuccessStale).isFalse();
        assertThat(readAfterStaleRemove.getValue()).isEqualTo(candidateAJson);
        assertThat(removeSuccessCurrent).isTrue();
        assertThat(finalGetA).isNull();
        assertThat(finalGetB).isNull();
    }

    @Test
    @DisplayName("Scenario 04-F (Part G): Value-Conditional Operations (putIfAbsent, replace, remove)")
    void testPartGValueConditionalOperations() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/mf-107";
        String initialValue = "{\"resourceType\":\"Person\",\"id\":\"mf-107\",\"status\":\"initial\"}";
        String updatedValue = "{\"resourceType\":\"Person\",\"id\":\"mf-107\",\"status\":\"updated\"}";

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("04-F", "VALUE-CONDITIONAL OPERATIONS (putIfAbsent, replace, remove)")
                .purpose("Demonstrate value-conditional Hot Rod operations (putIfAbsent, replace, remove) and contrast value-equality comparison semantics against entry-version token comparison semantics.")
                .hypothesis("Value-conditional operations succeed only when key absence or exact string equality matches; they provide conditional semantics but require full payload matching rather than opaque version tokens.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Initial State", "KEY ABSENT")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // 1. putIfAbsent
        cacheClientA.putIfAbsent(key, initialValue);
        String afterInsertValue = cacheClientB.get(key);
        narrator.addStep(1, "putIfAbsent ON ABSENT KEY",
                "operation: CLIENT-A putIfAbsent(\"" + key + "\", initialValue)",
                "result: SUCCESS (key was absent, inserted into cluster)",
                "observed cache value from CLIENT-B: " + afterInsertValue);

        cacheClientB.putIfAbsent(key, "{\"status\":\"clobber-attempt\"}");
        String afterClobberAttemptValue = cacheClientA.get(key);
        narrator.addStep(2, "putIfAbsent ON EXISTING KEY",
                "operation: CLIENT-B putIfAbsent(\"" + key + "\", clobberValue)",
                "result: KEY ALREADY EXISTS (clobber attempt rejected, original value preserved)",
                "cache value remains: " + afterClobberAttemptValue);

        // 2. Value-conditional replace(key, oldValue, newValue)
        boolean replaceWrong = cacheClientA.replace(key, "{\"status\":\"wrong-expected-value\"}", updatedValue);
        narrator.addStep(3, "replace(key, oldValue, newValue) WITH MISMATCHED OLD VALUE",
                "operation: CLIENT-A replace(\"" + key + "\", wrongOldValue, updatedValue)",
                "result: " + (replaceWrong ? "UNEXPECTED TRUE" : "FALSE (value mismatch, update rejected)"),
                "cache value remains: " + cacheClientB.get(key));

        boolean replaceRight = cacheClientA.replace(key, initialValue, updatedValue);
        narrator.addStep(4, "replace(key, oldValue, newValue) WITH MATCHING OLD VALUE",
                "operation: CLIENT-A replace(\"" + key + "\", initialValue, updatedValue)",
                "result: " + (replaceRight ? "TRUE (value matched, update applied)" : "FALSE"),
                "cache value: " + cacheClientB.get(key));

        // 3. Value-conditional remove(key, expectedValue)
        boolean removeWrong = cacheClientB.remove(key, "{\"status\":\"wrong-expected-value\"}");
        narrator.addStep(5, "remove(key, expectedValue) WITH MISMATCHED VALUE",
                "operation: CLIENT-B remove(\"" + key + "\", wrongValue)",
                "result: " + (removeWrong ? "UNEXPECTED TRUE" : "FALSE (value mismatch, delete rejected)"),
                "cache value remains: " + cacheClientA.get(key));

        boolean removeRight = cacheClientB.remove(key, updatedValue);
        String finalGet = cacheClientA.get(key);
        narrator.addStep(6, "remove(key, expectedValue) WITH MATCHING VALUE",
                "operation: CLIENT-B remove(\"" + key + "\", updatedValue)",
                "result: " + (removeRight ? "TRUE (value matched, delete applied)" : "FALSE"),
                "cache value post-remove: " + finalGet);

        narrator.addFinalState("Key", key)
                .addFinalState("Final Cache State", "NULL (deleted via value-conditional remove)")
                .addFinalState("Value Comparison vs Version Comparison",
                        "Value comparison requires transmitting and matching the entire payload (sensitive to formatting, whitespace, field ordering in JSON strings). Version comparison (replaceWithVersion) operates on an 8-byte opaque token, which is O(1) in bandwidth and format-agnostic.")
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Value-Conditional Semantics Verified: putIfAbsent, replace(k, oldVal, newVal), and remove(k, expectedVal) provide exact string/byte value equality conditional operations over Hot Rod.")
                .infinispanMechanism("Hot Rod PUT_IF_ABSENT, REPLACE, and REMOVE operations evaluate server-side value equality.")
                .adr019Assessment("DEMONSTRATED (Value-Conditional Operations) / CONTRASTED with Version-Aware Concurrency",
                        "Value-conditional operations are supported over Hot Rod but are suboptimal for complex JSON resources due to payload size and serialization formatting sensitivity; version-aware operations (replaceWithVersion) remain the preferred mechanism for ADR-019.");

        narrator.narrate();

        // Invariant Assertions
        assertThat(afterInsertValue).isEqualTo(initialValue);
        assertThat(afterClobberAttemptValue).isEqualTo(initialValue);
        assertThat(replaceWrong).isFalse();
        assertThat(replaceRight).isTrue();
        assertThat(removeWrong).isFalse();
        assertThat(removeRight).isTrue();
        assertThat(finalGet).isNull();
    }
}
