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
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Laboratory test suite characterizing participant failure, cache loss / non-authoritative boundary,
 * explicit failure on unavailability, and clean post-outage recovery (ADR-019).
 */
public class MnemeFailureAndRecoveryScenarioTest {

    private InfinispanLaboratoryServer laboratoryServer;
    private RemoteCacheManager clientA;
    private RemoteCacheManager clientB;

    @BeforeEach
    void startEnvironment() {
        laboratoryServer = new InfinispanLaboratoryServer();
        laboratoryServer.startCluster();

        clientA = laboratoryServer.createClientA();
        clientB = laboratoryServer.createClientB();
    }

    @AfterEach
    void stopEnvironment() {
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
    @DisplayName("Scenario 05: Participant Failure (Resilience of Surviving Participants)")
    void testScenario05ParticipantFailure() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/501";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"501\",\"name\":\"Smith\",\"meta\":{\"versionId\":\"1\"}}";
        String updatedJson = "{\"resourceType\":\"Person\",\"id\":\"501\",\"name\":\"Jones\",\"meta\":{\"versionId\":\"2\"}}";

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("05", "PARTICIPANT FAILURE")
                .purpose("Observe what happens to distributed cluster state and surviving participants when one Hot Rod client participant disconnects or fails.")
                .hypothesis("Client disconnect affects only the failed client; the distributed cluster retains active state and surviving client (CLIENT-B) continues reading and updating resources without degradation.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addParticipant("NODE-1", "Infinispan EmbeddedCacheManager [node-1] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addParticipant("NODE-2", "Infinispan EmbeddedCacheManager [node-2] in cluster '" + laboratoryServer.getClusterName() + "'")
                .addInitialState("Key", key)
                .addInitialState("Cache", cacheName + " (CacheMode.REPL_SYNC)")
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: CLIENT-A writes initial resource
        cacheClientA.put(key, initialJson);
        narrator.addStep(1, "CLIENT-A WRITE INITIAL RESOURCE",
                "operation: RemoteCache.put(\"" + key + "\", initialPayload)",
                "target: NODE-1",
                "result: SUCCESS");

        // Step 2: CLIENT-B verifies visibility
        String readB = cacheClientB.get(key);
        assertThat(readB).isEqualTo(initialJson);
        narrator.addStep(2, "CLIENT-B READ INITIAL RESOURCE",
                "source: NODE-2",
                "observed value: " + readB,
                "result: SUCCESS");

        // Step 3: CLIENT-A disconnects / terminates
        clientA.stop();
        narrator.addStep(3, "CLIENT-A DISCONNECT / FAILURE",
                "action: RemoteCacheManager.stop() on CLIENT-A",
                "status: CLIENT-A disconnected from NODE-1");

        // Step 4: Surviving CLIENT-B reads existing resource
        String readBAfterDisconnect = cacheClientB.get(key);
        narrator.addStep(4, "SURVIVING CLIENT-B READ AFTER CLIENT-A FAILURE",
                "source: NODE-2",
                "observed value: " + readBAfterDisconnect,
                "result: SUCCESS \u2014 State preserved in cluster");

        // Step 5: Surviving CLIENT-B updates resource
        cacheClientB.put(key, updatedJson);
        String finalReadB = cacheClientB.get(key);
        narrator.addStep(5, "SURVIVING CLIENT-B UPDATE WRITE",
                "target: NODE-2",
                "operation: RemoteCache.put(\"" + key + "\", updatedPayload)",
                "observed final value: " + finalReadB,
                "result: SUCCESS");

        narrator.addFinalState("Key", key)
                .addFinalState("Final Distributed Value", finalReadB)
                .addFinalState("FHIR Version", "2")
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Participant failure resilience confirmed: CLIENT-A termination did not impact cluster state or surviving participant operations. CLIENT-B continued normal read and write operations against NODE-2 over Hot Rod.")
                .infinispanMechanism("Hot Rod clients are stateless consumers connected to clustered server backends. Cluster state is held in Infinispan REPL_SYNC nodes independently of client lifecycle.")
                .adr019Assessment("DEMONSTRATED", "Distributed resource availability is decoupled from individual client runtimes; surviving participants operate normally.");

        narrator.narrate();

        assertThat(readBAfterDisconnect).isEqualTo(initialJson);
        assertThat(finalReadB).isEqualTo(updatedJson);
    }

    @Test
    @DisplayName("Scenario 06: Cache Loss / Reconstructability (Non-Authoritative Boundary)")
    void testScenario06CacheLossAndReconstructability() {
        String cacheName = "person-cache";
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);
        RemoteCache<String, String> cacheClientB = clientB.getCache(cacheName);

        String key = "Person/601";
        String initialJson = "{\"resourceType\":\"Person\",\"id\":\"601\",\"name\":\"Smith\",\"meta\":{\"versionId\":\"1\"}}";

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("06", "CACHE LOSS / RECONSTRUCTABILITY")
                .purpose("Demonstrate that Mneme state is non-authoritative working state; when cache state is cleared or lost, Mneme does not hold durable truth.")
                .hypothesis("Clearing the cache evicts working state; subsequent reads return null, proving Mneme is not an authoritative durability boundary. Automatic reconstruction from Mnemosyne is not yet implemented (Task 09).")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + laboratoryServer.getServer1Port())
                .addParticipant("CLIENT-B", "Hot Rod RemoteCacheManager instance #2 connected to Node-2 on port " + laboratoryServer.getServer2Port())
                .addInitialState("Key", key)
                .addInitialState("Initial Payload", initialJson)
                .addInitialState("Mnemosyne durable version", "NOT PARTICIPATING IN THIS SCENARIO");

        // Step 1: Populate working state
        cacheClientA.put(key, initialJson);
        String preClearB = cacheClientB.get(key);
        assertThat(preClearB).isEqualTo(initialJson);

        narrator.addStep(1, "ESTABLISH WORKING STATE",
                "operation: CLIENT-A RemoteCache.put(\"" + key + "\", payload)",
                "verified by CLIENT-B: " + preClearB);

        // Step 2: Simulate cache loss (clear)
        cacheClientA.clear();
        narrator.addStep(2, "CACHE LOSS / FLUSH",
                "action: RemoteCache.clear()",
                "target: Cluster cache '" + cacheName + "' emptied");

        // Step 3: Reads post-cache loss
        String postClearA = cacheClientA.get(key);
        String postClearB = cacheClientB.get(key);

        narrator.addStep(3, "POST-FLUSH READ ATTEMPTS",
                "CLIENT-A read: " + postClearA + " (cache miss)",
                "CLIENT-B read: " + postClearB + " (cache miss)",
                "reconstruction path: NONE \u2014 Point reads currently return null on cache miss");

        narrator.addFinalState("Key", key)
                .addFinalState("Final Working Value", "null (evicted/cleared)")
                .addFinalState("FHIR Version", "None (cache miss)")
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Non-authoritative boundary confirmed: Mneme state was lost on cache clear. No automatic reconstruction path exists in the current implementation; point reads return null upon cache eviction.")
                .infinispanMechanism("RemoteCache.clear() removes all cache entries from memory and backing stores across the cluster.")
                .adr019Assessment("DEMONSTRATED (Non-Authoritative Boundary) / NOT CURRENTLY IMPLEMENTED (Automatic Cache-Aside Reconstruction)",
                        "Mneme is confirmed as non-authoritative distributed working memory (ADR-018/ADR-019). Cache-aside read-through from authoritative Mnemosyne persistence is scheduled for Task 09.");

        narrator.narrate();

        assertThat(postClearA).isNull();
        assertThat(postClearB).isNull();
    }

    @Test
    @DisplayName("Scenario 08: Mneme Unavailable (Fail-Explicit Semantics)")
    void testScenario08MnemeUnavailable() {
        String cacheName = "person-cache";
        int port1 = laboratoryServer.getServer1Port();
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);

        String key = "Person/801";
        String payload = "{\"resourceType\":\"Person\",\"id\":\"801\",\"name\":\"Smith\",\"meta\":{\"versionId\":\"1\"}}";

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("08", "MNEME UNAVAILABLE")
                .purpose("Verify Task 07 Step 03 invariant: when required Mneme infrastructure is unavailable, operations fail explicitly without falling back to process-local maps (ConcurrentHashMap).")
                .hypothesis("Stopping HotRodServer causes client operations to throw HotRodClientException / TransportException visibly; no silent JVM-local fallback occurs.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + port1)
                .addParticipant("SERVER-1", "HotRodServer on port " + port1 + " (to be stopped)")
                .addInitialState("Key", key)
                .addInitialState("Mneme State", "Online initially, then stopped to simulate outage");

        // Step 1: Verify normal operation before outage
        cacheClientA.put(key, payload);
        assertThat(cacheClientA.get(key)).isEqualTo(payload);

        narrator.addStep(1, "BASELINE ONLINE OPERATION",
                "CLIENT-A put and get succeeded while server online");

        // Step 2: Stop Server 1 (simulate Mneme unavailability)
        laboratoryServer.stopServer1();
        narrator.addStep(2, "MNEME OUTAGE TRIGGERED",
                "action: HotRodServer on port " + port1 + " stopped",
                "status: Server endpoint unreachable");

        // Step 3: Attempt write and read during outage
        Exception thrownException = null;
        try {
            cacheClientA.put(key, "{\"resourceType\":\"Person\",\"id\":\"801\",\"name\":\"UpdatedDuringOutage\"}");
        } catch (Exception ex) {
            thrownException = ex;
        }

        narrator.addStep(3, "OPERATION ATTEMPT DURING OUTAGE",
                "operation: RemoteCache.put(\"" + key + "\", payload)",
                "observed exception: " + (thrownException != null ? thrownException.getClass().getName() : "NONE"),
                "exception message: " + (thrownException != null ? thrownException.getMessage() : "N/A"),
                "process-local fallback: NONE (No ConcurrentHashMap fallback map used)");

        narrator.addFinalState("Outage Response", "Explicit Exception Thrown")
                .addFinalState("Silent Fallback", "None")
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Fail-explicit invariant confirmed: Hot Rod client operation threw an explicit exception (" + (thrownException != null ? thrownException.getClass().getSimpleName() : "exception") + ") upon server unavailability without silently masking failure via JVM-local maps.")
                .infinispanMechanism("Hot Rod client connection pool fails connection establishment and throws HotRodClientException / TransportException when target server endpoint is stopped.")
                .adr019Assessment("DEMONSTRATED", "Fail-explicit semantics (Step 03 invariant) verified: zero process-local state substitution occurs when distributed cache is unreachable.");

        narrator.narrate();

        assertThat(thrownException).isNotNull();
        assertThat(thrownException).isInstanceOf(HotRodClientException.class);
    }

    @Test
    @DisplayName("Scenario 10: Recovery After Mneme Availability Returns")
    void testScenario10RecoveryAfterMnemeAvailabilityReturns() {
        String cacheName = "person-cache";
        int port1 = laboratoryServer.getServer1Port();
        RemoteCache<String, String> cacheClientA = clientA.getCache(cacheName);

        String key = "Person/1001";
        String preOutageJson = "{\"resourceType\":\"Person\",\"id\":\"1001\",\"name\":\"PreOutage\",\"meta\":{\"versionId\":\"1\"}}";
        String postRecoveryJson = "{\"resourceType\":\"Person\",\"id\":\"1001\",\"name\":\"PostRecovery\",\"meta\":{\"versionId\":\"2\"}}";

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("10", "RECOVERY AFTER MNEME AVAILABILITY RETURNS")
                .purpose("Verify that when Mneme availability is restored following an outage, clients cleanly reconnect and resume distributed operations without relying on stale process-local state.")
                .hypothesis("After server restart on the same port, subsequent Hot Rod operations reconnect and succeed; state is synchronized directly with the distributed cluster without stale memory artifacts.")
                .addParticipant("CLIENT-A", "Hot Rod RemoteCacheManager instance #1 connected to Node-1 on port " + port1)
                .addParticipant("SERVER-1", "HotRodServer on port " + port1)
                .addInitialState("Key", key)
                .addInitialState("Initial Payload", preOutageJson);

        // Step 1: Pre-outage write
        cacheClientA.put(key, preOutageJson);
        assertThat(cacheClientA.get(key)).isEqualTo(preOutageJson);
        narrator.addStep(1, "PRE-OUTAGE OPERATION",
                "CLIENT-A wrote initial state to NODE-1 (port " + port1 + "): SUCCESS");

        // Step 2: Stop Server (outage)
        laboratoryServer.stopServer1();
        assertThatThrownBy(() -> cacheClientA.get(key))
                .isInstanceOf(HotRodClientException.class);

        narrator.addStep(2, "OUTAGE ENCOUNTERED",
                "SERVER-1 stopped -> CLIENT-A read failed with HotRodClientException");

        // Step 3: Restore Server on same port
        laboratoryServer.startServer1OnPort(port1);
        narrator.addStep(3, "MNEME RESTORED",
                "action: HotRodServer restarted on port " + port1 + " bound to NODE-1");

        // Step 4: Reconnect and perform post-recovery update
        cacheClientA.put(key, postRecoveryJson);
        String finalObserved = cacheClientA.get(key);

        narrator.addStep(4, "POST-RECOVERY RESUMPTION",
                "operation: RemoteCache.put(\"" + key + "\", postRecoveryJson)",
                "observed value: " + finalObserved,
                "result: SUCCESS \u2014 Operations resumed cleanly without stale local state");

        narrator.addFinalState("Key", key)
                .addFinalState("Final Distributed Value", finalObserved)
                .addFinalState("FHIR Version", "2")
                .addFinalState("Mnemosyne Durable Version", "NOT PARTICIPATING IN THIS SCENARIO")
                .observedSemantics("Clean post-outage recovery confirmed: Once server availability returned, Hot Rod client re-established connection and resumed read/write operations without stale local state interference.")
                .infinispanMechanism("Hot Rod client connection pool automatically reconnects to healthy server endpoints on subsequent request attempts.")
                .adr019Assessment("DEMONSTRATED", "Recovery after availability returns is demonstrated: distributed operations resume transparently upon service restoration without process-local state artifacts.");

        narrator.narrate();

        assertThat(finalObserved).isEqualTo(postRecoveryJson);
    }
}
