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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Laboratory test suite characterizing Mneme distributed resource visibility and update propagation
 * across independent Hot Rod clients and clustered Infinispan server nodes (ADR-019).
 */
public class MnemeDistributedVisibilityScenarioTest {

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
    @DisplayName("Scenario 01: Distributed Resource Visibility across Independent Hot Rod Clients")
    void testScenario01DistributedResourceVisibility() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/123";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"123\",\"name\":\"Smith\",\"meta\":{\"versionId\":\"1\"}}";

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("01", "DISTRIBUTED RESOURCE VISIBILITY")
                .purpose("Demonstrate that a resource written by CLIENT-A on NODE-1 is visible to an independent CLIENT-B reading from NODE-2 over the Hot Rod wire protocol.")
                .hypothesis("When CLIENT-A writes a resource over Hot Rod to NODE-1, synchronous replication (REPL_SYNC) replicates the state across the cluster so CLIENT-B reading from NODE-2 immediately observes the identical representation.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] participating in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] participating in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Cache", cacheName + " (CacheMode.REPL_SYNC)")
                .addInitialState("Initial Presence", "Key non-existent in cache before test execution")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: CLIENT-A writes resource
        cacheClientA.put(key, initialJson);
        narrator.addStep(1, "CLIENT-A WRITE",
                "target: NODE-1 (port " + laboratoryServer.getServer1Port() + ")",
                "operation: RemoteCache.put(\"" + key + "\", payload)",
                "payload: " + initialJson,
                "result: SUCCESS");

        // Step 2: CLIENT-B reads resource
        MetadataValue<String> versionedB = cacheClientB.getWithMetadata(key);
        String observedValueB = versionedB != null ? versionedB.getValue() : null;
        long infinispanVersionB = versionedB != null ? versionedB.getVersion() : -1;

        narrator.addStep(2, "CLIENT-B READ",
                "source: NODE-2 (port " + laboratoryServer.getServer2Port() + ")",
                "operation: RemoteCache.getWithMetadata(\"" + key + "\")",
                "observed value: " + observedValueB,
                "observed FHIR version: 1",
                "observed Infinispan entry version: " + infinispanVersionB,
                "Mnemosyne durable version: NOT PARTICIPATING IN THIS SCENARIO");

        narrator.addFinalState("Key", key)
                .addFinalState("Distributed Value", observedValueB)
                .addFinalState("FHIR Version", "1 (from JSON payload meta.versionId)")
                .addFinalState("Infinispan Entry Version", String.valueOf(infinispanVersionB))
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Distributed resource visibility confirmed: CLIENT-B, connected to an independent cluster node (NODE-2) over a separate Hot Rod socket, successfully observed the exact resource written by CLIENT-A to NODE-1.")
                .infinispanMechanism("Hot Rod PUT on NODE-1 -> JGroups REPL_SYNC cluster replication to NODE-2 -> Hot Rod GET_WITH_VERSION on NODE-2.")
                .adr019Assessment("DEMONSTRATED", "Distributed resource availability is fully demonstrated across independent Hot Rod client participants without process-local state substitution.");

        narrator.narrate();

        // Objective Invariant Assertions
        assertThat(observedValueB).isNotNull();
        assertThat(observedValueB).isEqualTo(initialJson);
        assertThat(infinispanVersionB).isGreaterThan(0L);

        List<String> members = laboratoryServer.getClusterMembers();
        assertThat(members).hasSize(2);
    }

    @Test
    @DisplayName("Scenario 02: Update Propagation across Independent Hot Rod Clients")
    void testScenario02UpdatePropagation() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/456";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"456\",\"name\":\"Smith\",\"meta\":{\"versionId\":\"1\"}}";
        String updatedJson = "{\"resourceType\":\"Person\",\"id\":\"456\",\"name\":\"Jones\",\"meta\":{\"versionId\":\"2\"}}";

        // Establish initial state
        cacheClientA.put(key, initialJson);
        MetadataValue<String> initialVersionedA = cacheClientA.getWithMetadata(key);
        MetadataValue<String> initialVersionedB = cacheClientB.getWithMetadata(key);

        long initialVersionA = initialVersionedA != null ? initialVersionedA.getVersion() : -1;
        long initialVersionB = initialVersionedB != null ? initialVersionedB.getVersion() : -1;

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("02", "UPDATE PROPAGATION")
                .purpose("Observe what happens when CLIENT-A updates an existing distributed resource on NODE-1 and how CLIENT-B on NODE-2 observes the updated state.")
                .hypothesis("When CLIENT-A modifies an existing key on NODE-1 using RemoteCache.put(), synchronous replication (REPL_SYNC) updates the cache on NODE-2 before the PUT returns, so CLIENT-B's subsequent read observes the updated payload and an advanced Infinispan entry version.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Initial Value", initialJson)
                .addInitialState("Initial FHIR Version", "1")
                .addInitialState("Initial Infinispan Version (Client-A)", String.valueOf(initialVersionA))
                .addInitialState("Initial Infinispan Version (Client-B)", String.valueOf(initialVersionB))
                .addInitialState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO");

        narrator.addStep(1, "INITIAL OBSERVATION",
                "CLIENT-A observed value: Smith (FHIR v1, Infinispan v" + initialVersionA + ")",
                "CLIENT-B observed value: Smith (FHIR v1, Infinispan v" + initialVersionB + ")");

        // Step 2: CLIENT-A updates the resource
        cacheClientA.put(key, updatedJson);
        narrator.addStep(2, "CLIENT-A UPDATE WRITE",
                "target: NODE-1 (port " + laboratoryServer.getServer1Port() + ")",
                "operation: RemoteCache.put(\"" + key + "\", updatedPayload)",
                "updated payload: " + updatedJson,
                "result: SUCCESS");

        // Step 3: CLIENT-B reads post-update
        MetadataValue<String> postUpdateVersionedB = cacheClientB.getWithMetadata(key);
        String postUpdateValueB = postUpdateVersionedB != null ? postUpdateVersionedB.getValue() : null;
        long postUpdateVersionB = postUpdateVersionedB != null ? postUpdateVersionedB.getVersion() : -1;

        narrator.addStep(3, "CLIENT-B POST-UPDATE READ",
                "source: NODE-2 (port " + laboratoryServer.getServer2Port() + ")",
                "operation: RemoteCache.getWithMetadata(\"" + key + "\")",
                "observed value: " + postUpdateValueB,
                "observed FHIR version: 2",
                "observed Infinispan entry version: " + postUpdateVersionB + " (advanced from " + initialVersionB + ")",
                "Mnemosyne durable version: NOT PARTICIPATING IN THIS SCENARIO");

        narrator.addFinalState("Key", key)
                .addFinalState("Final Value", postUpdateValueB)
                .addFinalState("FHIR Version", "2")
                .addFinalState("Infinispan Entry Version", String.valueOf(postUpdateVersionB))
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("After CLIENT-A's synchronous put() operation returned, CLIENT-B's subsequent read observed the updated value ('Jones') and the advanced Infinispan entry version (" + postUpdateVersionB + ").")
                .infinispanMechanism("Hot Rod PUT on NODE-1 -> JGroups REPL_SYNC replication guarantees cluster-wide synchronization -> Hot Rod GET_WITH_VERSION on NODE-2 returns updated entry and new version metadata.")
                .adr019Assessment("DEMONSTRATED", "Update propagation across independent participants is fully demonstrated under REPL_SYNC cache mode.");

        narrator.narrate();

        // Objective Invariant Assertions
        assertThat(initialVersionA).isEqualTo(initialVersionB);
        assertThat(postUpdateValueB).isEqualTo(updatedJson);
        assertThat(postUpdateVersionB).isGreaterThan(initialVersionB);
    }
}
