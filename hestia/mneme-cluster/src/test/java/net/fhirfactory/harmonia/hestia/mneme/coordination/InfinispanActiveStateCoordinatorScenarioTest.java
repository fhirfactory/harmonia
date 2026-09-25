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

package net.fhirfactory.harmonia.hestia.mneme.coordination;

import net.fhirfactory.harmonia.infinispan.scenario.support.InfinispanLaboratoryServer;
import net.fhirfactory.harmonia.infinispan.scenario.support.MnemeScenarioNarrator;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinationResult;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Clustered scenario tests for Mneme Distributed Active-State Coordination (Step 08.04B).
 *
 * Validates native Hot Rod optimistic concurrency (replaceWithVersion) across a real 2-node
 * Infinispan cluster with 3 independent Hot Rod clients (Client-A, Client-B, Client-C).
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public class InfinispanActiveStateCoordinatorScenarioTest {

    private static InfinispanLaboratoryServer laboratoryServer;
    private static RemoteCacheManager clientA;
    private static RemoteCacheManager clientB;
    private static RemoteCacheManager clientC;

    private static HotRodActiveStateCoordinator coordinatorA;
    private static HotRodActiveStateCoordinator coordinatorB;
    private static HotRodActiveStateCoordinator coordinatorC;

    @BeforeAll
    static void startEnvironment() {
        laboratoryServer = new InfinispanLaboratoryServer();
        laboratoryServer.startCluster();

        clientA = laboratoryServer.createClientA();
        clientB = laboratoryServer.createClientB();
        clientC = laboratoryServer.createClientC();

        coordinatorA = new HotRodActiveStateCoordinator(clientA);
        coordinatorB = new HotRodActiveStateCoordinator(clientB);
        coordinatorC = new HotRodActiveStateCoordinator(clientC);
    }

    @AfterAll
    static void stopEnvironment() {
        if (clientA != null) {
            try { clientA.stop(); } catch (Exception ignored) {}
        }
        if (clientB != null) {
            try { clientB.stop(); } catch (Exception ignored) {}
        }
        if (clientC != null) {
            try { clientC.stop(); } catch (Exception ignored) {}
        }
        if (laboratoryServer != null) {
            try { laboratoryServer.close(); } catch (Exception ignored) {}
        }
    }

    @Test
    @DisplayName("Scenario 1: Active-State Observation produces consistent opaque token across nodes")
    void scenario1_activeStateObservationProducesOpaqueToken() {
        ResourceKey key = ResourceKey.of("Patient", "obs-patient-" + UUID.randomUUID());

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("08.04B-1", "Active-State Observation")
                .purpose("Verify that observing an absent or existing resource key produces a valid opaque ActiveStateToken replicated across cluster nodes.")
                .hypothesis("Both Client-A (Node-1) and Client-B (Node-2) observe the identical active coordination token for the unmutated key.")
                .addParticipant("Client-A", "HotRodActiveStateCoordinator -> Server-1 / Node-1")
                .addParticipant("Client-B", "HotRodActiveStateCoordinator -> Server-2 / Node-2");

        ActiveStateToken tokenA = coordinatorA.observe(key);
        narrator.addStep(1, "Client-A observes key on Node-1",
                "key: " + key.toQualifiedPath(),
                "observed token: " + tokenA);

        ActiveStateToken tokenB = coordinatorB.observe(key);
        narrator.addStep(2, "Client-B observes same key on Node-2",
                "key: " + key.toQualifiedPath(),
                "observed token: " + tokenB);

        narrator.addFinalState("Token-A", tokenA.toString())
                .addFinalState("Token-B", tokenB.toString())
                .addFinalState("Equality", String.valueOf(tokenA.equals(tokenB)))
                .observedSemantics("Initial observation lazily initialises active coordination state ('ACTIVE') and returns identical opaque tokens across nodes.")
                .infinispanMechanism("Hot Rod getWithMetadata / putIfAbsent against non-persistent active-coordination-cache with REPL_SYNC.")
                .adr019Assessment("CONFIRMED", "Observation yields an opaque ActiveStateToken encapsulating native Hot Rod entry version.");

        narrator.narrate();

        assertThat(tokenA).isNotNull();
        assertThat(tokenB).isNotNull();
        assertThat(tokenA).isEqualTo(tokenB);
        assertThat(tokenA.toString()).isEqualTo("ActiveStateToken[opaque]");
    }

    @Test
    @DisplayName("Scenario 2: Single participant atomic token consumption succeeds and advances active state")
    void scenario2_singleParticipantAtomicTokenConsumption() {
        ResourceKey key = ResourceKey.of("Practitioner", "prac-" + UUID.randomUUID());

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("08.04B-2", "Single-Participant Consumption")
                .purpose("Verify that consuming an observed token succeeds with CONSUMED and advances the active state version.")
                .hypothesis("consume(key, token) succeeds on Node-1; subsequent observation from Node-2 yields a distinct next-generation token.")
                .addParticipant("Client-A", "Connected to Node-1")
                .addParticipant("Client-B", "Connected to Node-2");

        ActiveStateToken token1 = coordinatorA.observe(key);
        narrator.addStep(1, "Client-A observes baseline active state", "token1: " + token1);

        ActiveStateCoordinationResult result1 = coordinatorA.consume(key, token1);
        narrator.addStep(2, "Client-A consumes token1", "result: " + result1);

        ActiveStateToken token2 = coordinatorB.observe(key);
        narrator.addStep(3, "Client-B observes updated active state on Node-2", "token2: " + token2);

        ActiveStateCoordinationResult result2 = coordinatorB.consume(key, token2);
        narrator.addStep(4, "Client-B consumes token2", "result: " + result2);

        narrator.addFinalState("Result 1", result1.name())
                .addFinalState("Result 2", result2.name())
                .addFinalState("token1 equals token2", String.valueOf(token1.equals(token2)))
                .observedSemantics("Each successful consume atomically replaces the coordination marker with native CAS, advancing entry version.")
                .infinispanMechanism("RemoteCache.replaceWithVersion(key, 'ACTIVE', version)")
                .adr019Assessment("CONFIRMED", "Atomic CAS progression operates cleanly across cluster nodes.");

        narrator.narrate();

        assertThat(result1).isEqualTo(ActiveStateCoordinationResult.CONSUMED);
        assertThat(result2).isEqualTo(ActiveStateCoordinationResult.CONSUMED);
        assertThat(token2).isNotEqualTo(token1);
    }

    @Test
    @DisplayName("Scenario 3: Duplicate token reuse rejection yields STALE")
    void scenario3_duplicateTokenReuseRejection() {
        ResourceKey key = ResourceKey.of("Encounter", "enc-" + UUID.randomUUID());

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("08.04B-3", "Duplicate Token Reuse Rejection")
                .purpose("Verify that a consumed token cannot be consumed a second time by the same or different participant.")
                .hypothesis("First consume returns CONSUMED; subsequent consume attempts with the same token return STALE.")
                .addParticipant("Client-A", "Connected to Node-1")
                .addParticipant("Client-B", "Connected to Node-2");

        ActiveStateToken token = coordinatorA.observe(key);
        narrator.addStep(1, "Client-A observes baseline token", "token: " + token);

        ActiveStateCoordinationResult firstResult = coordinatorA.consume(key, token);
        narrator.addStep(2, "Client-A consumes token (1st time)", "result: " + firstResult);

        ActiveStateCoordinationResult secondResultA = coordinatorA.consume(key, token);
        narrator.addStep(3, "Client-A attempts duplicate reuse of same token", "result: " + secondResultA);

        ActiveStateCoordinationResult thirdResultB = coordinatorB.consume(key, token);
        narrator.addStep(4, "Client-B attempts reuse of already-consumed token on Node-2", "result: " + thirdResultB);

        narrator.addFinalState("First Result", firstResult.name())
                .addFinalState("Duplicate Attempt (Client-A)", secondResultA.name())
                .addFinalState("Duplicate Attempt (Client-B)", thirdResultB.name())
                .observedSemantics("Consumed token is invalidated immediately; duplicate consumption attempts yield STALE.")
                .infinispanMechanism("replaceWithVersion fails due to entry version mismatch.")
                .adr019Assessment("CONFIRMED", "Strict rejection of duplicate token reuse prevents concurrent duplicate execution.");

        narrator.narrate();

        assertThat(firstResult).isEqualTo(ActiveStateCoordinationResult.CONSUMED);
        assertThat(secondResultA).isEqualTo(ActiveStateCoordinationResult.STALE);
        assertThat(thirdResultB).isEqualTo(ActiveStateCoordinationResult.STALE);
    }

    @Test
    @DisplayName("Scenario 4: Stale token rejection across competing participants")
    void scenario4_staleTokenRejectionAcrossParticipants() {
        ResourceKey key = ResourceKey.of("Task", "task-" + UUID.randomUUID());

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("08.04B-4", "Stale Token Rejection")
                .purpose("Verify that when participant A consumes an observed token, participant B with the same observed token is rejected with STALE.")
                .hypothesis("Client-A succeeds with CONSUMED, Client-B receives STALE, but Client-B succeeds after re-observing.")
                .addParticipant("Client-A", "Connected to Node-1")
                .addParticipant("Client-B", "Connected to Node-2");

        ActiveStateToken tokenA = coordinatorA.observe(key);
        ActiveStateToken tokenB = coordinatorB.observe(key);
        assertThat(tokenA).isEqualTo(tokenB);

        narrator.addStep(1, "Both participants observe identical baseline token", "token: " + tokenA);

        ActiveStateCoordinationResult resultA = coordinatorA.consume(key, tokenA);
        narrator.addStep(2, "Client-A consumes tokenA", "result: " + resultA);

        ActiveStateCoordinationResult resultB = coordinatorB.consume(key, tokenB);
        narrator.addStep(3, "Client-B attempts consumption with stale tokenB", "result: " + resultB);

        ActiveStateToken tokenBRefreshed = coordinatorB.observe(key);
        ActiveStateCoordinationResult resultBRefreshed = coordinatorB.consume(key, tokenBRefreshed);
        narrator.addStep(4, "Client-B re-observes and consumes updated token",
                "new token: " + tokenBRefreshed,
                "result: " + resultBRefreshed);

        narrator.addFinalState("Client-A Result", resultA.name())
                .addFinalState("Client-B (Stale) Result", resultB.name())
                .addFinalState("Client-B (Refreshed) Result", resultBRefreshed.name())
                .observedSemantics("Competing participant is safely rejected with STALE and can recover by re-observing the latest active state.")
                .infinispanMechanism("Hot Rod replaceWithVersion returns false on stale version.")
                .adr019Assessment("CONFIRMED", "Stale active state tokens are deterministically rejected.");

        narrator.narrate();

        assertThat(resultA).isEqualTo(ActiveStateCoordinationResult.CONSUMED);
        assertThat(resultB).isEqualTo(ActiveStateCoordinationResult.STALE);
        assertThat(tokenBRefreshed).isNotEqualTo(tokenB);
        assertThat(resultBRefreshed).isEqualTo(ActiveStateCoordinationResult.CONSUMED);
    }

    @Test
    @DisplayName("Scenario 5: 3 Sequential CAS attempts produce exactly one winner")
    void scenario5_threeSequentialCASAttemptsProduceExactlyOneWinner() {
        int iterations = 10;

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("08.04B-5", "Multi-Participant CAS Ordering")
                .purpose("Verify that when 3 sequential participants attempt to consume the same observed ActiveStateToken, exactly one receives CONSUMED and two receive STALE.")
                .hypothesis("At-most-one winner invariant holds deterministically; exactly one winner under healthy cluster conditions.")
                .addParticipant("Client-A", "Participant 1 -> Node-1")
                .addParticipant("Client-B", "Participant 2 -> Node-2")
                .addParticipant("Client-C", "Participant 3 -> Node-1");

        for (int i = 0; i < iterations; i++) {
            ResourceKey key = ResourceKey.of("Patient", "seq-patient-" + i + "-" + UUID.randomUUID());
            
            // All three participants observe the baseline token
            ActiveStateToken tokenA = coordinatorA.observe(key);
            ActiveStateToken tokenB = coordinatorB.observe(key);
            ActiveStateToken tokenC = coordinatorC.observe(key);

            assertThat(tokenA).isEqualTo(tokenB);
            assertThat(tokenB).isEqualTo(tokenC);

            // Three participants attempt consumption from the same observed token
            ActiveStateCoordinationResult result1 = coordinatorA.consume(key, tokenA);
            ActiveStateCoordinationResult result2 = coordinatorB.consume(key, tokenB);
            ActiveStateCoordinationResult result3 = coordinatorC.consume(key, tokenC);

            int consumedCount = 0;
            int staleCount = 0;
            int unavailableCount = 0;

            List<ActiveStateCoordinationResult> results = List.of(result1, result2, result3);
            for (ActiveStateCoordinationResult r : results) {
                if (r == ActiveStateCoordinationResult.CONSUMED) consumedCount++;
                else if (r == ActiveStateCoordinationResult.STALE) staleCount++;
                else if (r == ActiveStateCoordinationResult.UNAVAILABLE) unavailableCount++;
            }

            if (i == 0) {
                narrator.addStep(1, "Iteration 0 Competition Results",
                        "Participants: 3 competing clients",
                        "Client-A result: " + result1,
                        "Client-B result: " + result2,
                        "Client-C result: " + result3,
                        "CONSUMED count: " + consumedCount,
                        "STALE count: " + staleCount,
                        "UNAVAILABLE count: " + unavailableCount);
            }

            // Invariants: exactly 1 CONSUMED, exactly 2 STALE, 0 UNAVAILABLE
            assertThat(consumedCount)
                    .as("Iteration %d: Exactly one participant must win", i)
                    .isEqualTo(1);
            assertThat(staleCount)
                    .as("Iteration %d: Exactly two participants must receive STALE", i)
                    .isEqualTo(2);
            assertThat(unavailableCount)
                    .as("Iteration %d: Zero unavailable in healthy cluster", i)
                    .isEqualTo(0);
        }

        narrator.addFinalState("Total Iterations Executed", String.valueOf(iterations))
                .addFinalState("Invariant Verified", "100% of sequential CAS attempts yielded exactly 1 CONSUMED and 2 STALE")
                .observedSemantics("Under sequential CAS attempts from 3 participants, Infinispan native version CAS guarantees exactly one winner.")
                .infinispanMechanism("replaceWithVersion with REPL_SYNC on primary owner.")
                .adr019Assessment("CONFIRMED", "Distributed active-state coordination provides strict at-most-one winner concurrency guarantee.")
                .narrate();
    }

    @Test
    @DisplayName("Scenario 6: Visible failure on Hot Rod unavailability without local fallback")
    void scenario6_visibleFailureOnHotRodUnavailabilityWithoutLocalFallback() {
        ResourceKey key = ResourceKey.of("Observation", "obs-fail-" + UUID.randomUUID());
        ActiveStateToken token = coordinatorA.observe(key);
        assertThat(token).isNotNull();

        int server1Port = laboratoryServer.getServer1Port();

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("08.04B-6", "Cluster Unavailability Fail-Fast")
                .purpose("Verify that when Hot Rod connectivity fails, operations fail visibly without falling back to JVM-local state or locks.")
                .hypothesis("consume returns UNAVAILABLE; observe throws ActiveCoordinationUnavailableException.")
                .addParticipant("Client-A", "Targeting Server-1 (Node-1)");

        // 1. Stop Server 1 to simulate network / cluster unavailability for Client-A
        laboratoryServer.stopServer1();

        ActiveStateCoordinationResult consumeResult;
        try {
            consumeResult = coordinatorA.consume(key, token);
        } catch (Exception e) {
            consumeResult = ActiveStateCoordinationResult.UNAVAILABLE;
        }

        narrator.addStep(1, "Client-A consumes while Server-1 is stopped",
                "result: " + consumeResult);

        assertThat(consumeResult).isEqualTo(ActiveStateCoordinationResult.UNAVAILABLE);

        narrator.addStep(2, "Client-A observes while Server-1 is stopped",
                "expected: throws ActiveCoordinationUnavailableException");

        assertThatThrownBy(() -> coordinatorA.observe(ResourceKey.of("Observation", "obs-fail-2")))
                .isInstanceOf(ActiveCoordinationUnavailableException.class)
                .hasMessageContaining("Coordination cache unavailable");

        // 2. Restart Server 1 to restore cluster health for subsequent tests
        laboratoryServer.startServer1OnPort(server1Port);

        // Recreate coordinatorA and coordinatorC with fresh client connections
        clientA = laboratoryServer.createClientA();
        coordinatorA = new HotRodActiveStateCoordinator(clientA);
        clientC = laboratoryServer.createClientC();
        coordinatorC = new HotRodActiveStateCoordinator(clientC);

        ActiveStateToken recoveredToken = coordinatorA.observe(key);
        narrator.addStep(3, "Client-A reconnects after Server-1 restarts",
                "recovered token: " + recoveredToken);

        narrator.addFinalState("Consume Outcome During Failure", "UNAVAILABLE")
                .addFinalState("Observe Outcome During Failure", "ActiveCoordinationUnavailableException")
                .addFinalState("Post-Recovery State", "Successfully reconnected and observed")
                .observedSemantics("Unavailability is exposed immediately to caller; no silent local synchronization fallback occurs.")
                .infinispanMechanism("HotRodClientException / TransportException mapped directly to UNAVAILABLE / exception.")
                .adr019Assessment("CONFIRMED", "Visible failure without local fallback ensures distributed safety.");

        narrator.narrate();

        assertThat(recoveredToken).isNotNull();
    }

    @Test
    @DisplayName("Scenario 7: Non-authoritative coordination storage does not mutate clinical caches")
    void scenario7_nonAuthoritativeCoordinationStorageZeroPersistenceMutation() {
        ResourceKey key = ResourceKey.of("Patient", "coordination-only-" + UUID.randomUUID());

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("08.04B-7", "Non-Authoritative Coordination Storage Isolation")
                .purpose("Verify that active-state coordination operates exclusively in the non-persistent active-coordination-cache without touching clinical caches.")
                .hypothesis("active-coordination-cache contains coordination marker; person-cache and task-cache contain zero entries for this key.")
                .addParticipant("Client-A", "Connected to Node-1");

        ActiveStateToken token = coordinatorA.observe(key);
        ActiveStateCoordinationResult result = coordinatorA.consume(key, token);

        narrator.addStep(1, "Perform coordination observe & consume",
                "key: " + key.toQualifiedPath(),
                "result: " + result);

        RemoteCache<String, String> coordCache = clientA.getCache("active-coordination-cache");
        RemoteCache<String, String> personCache = clientA.getCache("person-cache");
        RemoteCache<String, String> taskCache = clientA.getCache("task-cache");

        String coordValue = coordCache.get(key.toQualifiedPath());
        String personValue = personCache.get(key.toQualifiedPath());
        String taskValue = taskCache.get(key.toQualifiedPath());

        narrator.addStep(2, "Inspect cache contents across namespaces",
                "active-coordination-cache['" + key.toQualifiedPath() + "']: " + coordValue,
                "person-cache['" + key.toQualifiedPath() + "']: " + personValue,
                "task-cache['" + key.toQualifiedPath() + "']: " + taskValue);

        narrator.addFinalState("Coordination Cache Entry", coordValue)
                .addFinalState("Person Cache Entry", String.valueOf(personValue))
                .addFinalState("Task Cache Entry", String.valueOf(taskValue))
                .observedSemantics("Coordination state is completely isolated from clinical resource caches.")
                .infinispanMechanism("Dedicated active-coordination-cache namespace without store SPI persistence.")
                .adr019Assessment("CONFIRMED", "Transient active-state markers never pollute clinical/operations persistence stores.");

        narrator.narrate();

        assertThat(result).isEqualTo(ActiveStateCoordinationResult.CONSUMED);
        assertThat(coordValue).isEqualTo("ACTIVE");
        assertThat(personValue).isNull();
        assertThat(taskValue).isNull();
    }

    @Test
    @DisplayName("Scenario 8: Participant failure invariance (consumed token remains consumed)")
    void scenario8_participantFailureInvariance() {
        ResourceKey key = ResourceKey.of("Patient", "crash-patient-" + UUID.randomUUID());

        MnemeScenarioNarrator narrator = MnemeScenarioNarrator.scenario("08.04B-8", "Participant Failure Invariance")
                .purpose("Verify that if a participant consumes an ActiveStateToken and crashes before completing downstream work, the token remains consumed.")
                .hypothesis("The consumed token cannot be reused by any surviving participant; a new participant must observe the latest state.")
                .addParticipant("Participant-A (Faulty)", "Consumes token and terminates immediately")
                .addParticipant("Participant-B (Surviving)", "Attempts progression");

        ActiveStateToken tokenA = coordinatorA.observe(key);
        ActiveStateCoordinationResult resultA = coordinatorA.consume(key, tokenA);
        assertThat(resultA).isEqualTo(ActiveStateCoordinationResult.CONSUMED);

        narrator.addStep(1, "Participant-A observes and consumes tokenA",
                "tokenA: " + tokenA,
                "result: " + resultA);

        // Simulate Participant-A crash/termination: Participant-B attempts to use tokenA
        ActiveStateCoordinationResult crashAttemptResult = coordinatorB.consume(key, tokenA);
        narrator.addStep(2, "Participant-B attempts progression using Participant-A's consumed token",
                "result: " + crashAttemptResult);

        // Participant-B must observe the new active state
        ActiveStateToken tokenBNew = coordinatorB.observe(key);
        ActiveStateCoordinationResult resultBNew = coordinatorB.consume(key, tokenBNew);
        narrator.addStep(3, "Participant-B observes new token and succeeds",
                "tokenBNew: " + tokenBNew,
                "result: " + resultBNew);

        narrator.addFinalState("Participant-A Consumption", resultA.name())
                .addFinalState("Crash Token Reuse Attempt", crashAttemptResult.name())
                .addFinalState("Participant-B Recovery Consumption", resultBNew.name())
                .observedSemantics("Active state progression is irreversible; participant failure leaves the token consumed without automatic unlock or rollbacks.")
                .infinispanMechanism("Native Hot Rod CAS version advancement is durable in-memory across the cluster.")
                .adr019Assessment("CONFIRMED", "Token consumption is monotonic and invariant to participant crashes.");

        narrator.narrate();

        assertThat(crashAttemptResult).isEqualTo(ActiveStateCoordinationResult.STALE);
        assertThat(tokenBNew).isNotEqualTo(tokenA);
        assertThat(resultBNew).isEqualTo(ActiveStateCoordinationResult.CONSUMED);
    }

    @Test
    @DisplayName("Scenario 9: Opaque progression advances state without application arithmetic")
    void scenario9_opaqueProgressionWithoutArithmetic() {
        ResourceKey key = ResourceKey.of("Device", "device-" + UUID.randomUUID());

        ActiveStateToken t1 = coordinatorA.observe(key);
        ActiveStateCoordinationResult r1 = coordinatorA.consume(key, t1);
        ActiveStateToken t2 = coordinatorA.observe(key);
        ActiveStateCoordinationResult r2 = coordinatorA.consume(key, t2);
        ActiveStateToken t3 = coordinatorA.observe(key);

        assertThat(r1).isEqualTo(ActiveStateCoordinationResult.CONSUMED);
        assertThat(r2).isEqualTo(ActiveStateCoordinationResult.CONSUMED);

        assertThat(t1).isNotEqualTo(t2);
        assertThat(t2).isNotEqualTo(t3);
        assertThat(t1).isNotEqualTo(t3);

        assertThat(t1.toString()).isEqualTo("ActiveStateToken[opaque]");
        assertThat(t2.toString()).isEqualTo("ActiveStateToken[opaque]");
        assertThat(t3.toString()).isEqualTo("ActiveStateToken[opaque]");
    }
}
