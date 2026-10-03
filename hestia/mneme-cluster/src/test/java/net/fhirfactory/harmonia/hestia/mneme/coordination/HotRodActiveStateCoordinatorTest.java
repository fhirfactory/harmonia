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
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinationResult;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateTokenBridge;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HotRodActiveStateCoordinatorTest {

    private InfinispanLaboratoryServer laboratoryServer;
    private RemoteCacheManager clientA;
    private HotRodActiveStateCoordinator coordinator;
    private ResourceKey sampleKey;

    @BeforeEach
    void setUp() {
        laboratoryServer = new InfinispanLaboratoryServer();
        laboratoryServer.startCluster();
        clientA = laboratoryServer.createClientA();
        coordinator = new HotRodActiveStateCoordinator(clientA);
        sampleKey = ResourceKey.of("Patient", "patient-" + UUID.randomUUID());
    }

    @AfterEach
    void tearDown() {
        if (clientA != null) {
            try {
                clientA.stop();
            } catch (Exception ignored) {
            }
        }
        if (laboratoryServer != null) {
            try {
                laboratoryServer.close();
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    @DisplayName("Constructor parameter validation")
    void testConstructorValidation() {
        assertThatThrownBy(() -> new HotRodActiveStateCoordinator((RemoteCache<String, String>) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("coordinationCache must not be null");

        assertThatThrownBy(() -> new HotRodActiveStateCoordinator((RemoteCacheManager) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cacheManager must not be null");
    }

    @Test
    @DisplayName("Parameter validation on observe and consume")
    void testMethodParameterValidation() {
        assertThatThrownBy(() -> coordinator.observe(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ResourceKey must not be null");

        ActiveStateToken token = ActiveStateTokenBridge.create(100L);
        assertThatThrownBy(() -> coordinator.consume(null, token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ResourceKey must not be null");

        assertThatThrownBy(() -> coordinator.consume(sampleKey, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("observedToken must not be null");

        InstanceId instanceId = InstanceId.random();
        assertThatThrownBy(() -> coordinator.checkHegemonStatus(null, instanceId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ResourceKey must not be null");

        assertThatThrownBy(() -> coordinator.checkHegemonStatus(sampleKey, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("instanceId must not be null");

        assertThatThrownBy(() -> coordinator.acquireOrConfirmHegemony(null, instanceId, token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ResourceKey must not be null");

        assertThatThrownBy(() -> coordinator.acquireOrConfirmHegemony(sampleKey, null, token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("instanceId must not be null");

        assertThatThrownBy(() -> coordinator.acquireOrConfirmHegemony(sampleKey, instanceId, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("observedToken must not be null");
    }

    @Test
    @DisplayName("Scenario A: No-Hegemon status observation reports NO_RESOURCE_IS_HEGEMON without mutating cache")
    void testScenarioA_NoHegemonStatusObservation() {
        InstanceId instanceA = InstanceId.random();
        RemoteCache<String, String> cache = clientA.getCache(HotRodActiveStateCoordinator.COORDINATION_CACHE_NAME);

        // Case 1: Cold key (absent entry)
        ResourceHegemonStatus status1 = coordinator.checkHegemonStatus(sampleKey, instanceA);
        assertThat(status1).isEqualTo(ResourceHegemonStatus.NO_RESOURCE_IS_HEGEMON);
        // Cache must remain completely unmodified (absent)
        assertThat(cache.getWithMetadata(sampleKey.toQualifiedPath())).isNull();

        // Case 2: Explicitly seeded NO_HEGEMON state
        ActiveStateToken token = coordinator.observe(sampleKey);
        assertThat(token).isNotNull();
        long versionBefore = cache.getWithMetadata(sampleKey.toQualifiedPath()).getVersion();

        ResourceHegemonStatus status2 = coordinator.checkHegemonStatus(sampleKey, instanceA);
        assertThat(status2).isEqualTo(ResourceHegemonStatus.NO_RESOURCE_IS_HEGEMON);
        long versionAfter = cache.getWithMetadata(sampleKey.toQualifiedPath()).getVersion();
        assertThat(versionAfter).isEqualTo(versionBefore);
    }

    @Test
    @DisplayName("Scenario B: First Hegemon acquisition from NO_HEGEMON atomically advances generation and returns Acquired")
    void testScenarioB_FirstHegemonAcquisitionFromNoHegemon() {
        InstanceId instanceA = InstanceId.random();
        ActiveStateToken observedToken = coordinator.observe(sampleKey);

        HegemonyCoordinationResult result = coordinator.acquireOrConfirmHegemony(sampleKey, instanceA, observedToken);
        assertThat(result).isInstanceOf(HegemonyCoordinationResult.Acquired.class);

        HegemonyCoordinationResult.Acquired acquired = (HegemonyCoordinationResult.Acquired) result;
        assertThat(acquired.newToken()).isNotNull();
        assertThat(acquired.newToken()).isNotEqualTo(observedToken);

        // Verification: instanceA is now observed as Hegemon
        ResourceHegemonStatus status = coordinator.checkHegemonStatus(sampleKey, instanceA);
        assertThat(status).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_HEGEMON);
    }

    @Test
    @DisplayName("Scenario C: Stale token rejection returns Stale when observed token is outdated")
    void testScenarioC_StaleTokenRejection() {
        InstanceId instanceA = InstanceId.random();
        coordinator.observe(sampleKey);

        // Synthetic outdated token version
        ActiveStateToken outdatedToken = ActiveStateTokenBridge.create(999999L);
        HegemonyCoordinationResult result = coordinator.acquireOrConfirmHegemony(sampleKey, instanceA, outdatedToken);
        assertThat(result).isInstanceOf(HegemonyCoordinationResult.Stale.class);

        // Hegemon status must remain NO_RESOURCE_IS_HEGEMON
        ResourceHegemonStatus status = coordinator.checkHegemonStatus(sampleKey, instanceA);
        assertThat(status).isEqualTo(ResourceHegemonStatus.NO_RESOURCE_IS_HEGEMON);
    }

    @Test
    @DisplayName("Scenario D: Existing-Hegemon happy path validates token without cache write or generation increment")
    void testScenarioD_ExistingHegemonHappyPathConfirmation() {
        InstanceId instanceA = InstanceId.random();
        ActiveStateToken initialToken = coordinator.observe(sampleKey);

        HegemonyCoordinationResult acqResult = coordinator.acquireOrConfirmHegemony(sampleKey, instanceA, initialToken);
        assertThat(acqResult).isInstanceOf(HegemonyCoordinationResult.Acquired.class);
        ActiveStateToken hegemonToken = ((HegemonyCoordinationResult.Acquired) acqResult).newToken();

        // Check status
        assertThat(coordinator.checkHegemonStatus(sampleKey, instanceA)).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_HEGEMON);

        // Capture metadata version before confirmation
        RemoteCache<String, String> cache = clientA.getCache(HotRodActiveStateCoordinator.COORDINATION_CACHE_NAME);
        long versionBeforeConfirm = cache.getWithMetadata(sampleKey.toQualifiedPath()).getVersion();

        // Existing Hegemon executes acquireOrConfirmHegemony
        HegemonyCoordinationResult confirmResult = coordinator.acquireOrConfirmHegemony(sampleKey, instanceA, hegemonToken);
        assertThat(confirmResult).isInstanceOf(HegemonyCoordinationResult.Confirmed.class);
        HegemonyCoordinationResult.Confirmed confirmed = (HegemonyCoordinationResult.Confirmed) confirmResult;
        assertThat(confirmed.confirmedToken()).isEqualTo(hegemonToken);

        // Verify: ZERO cache write occurred, version is identical
        long versionAfterConfirm = cache.getWithMetadata(sampleKey.toQualifiedPath()).getVersion();
        assertThat(versionAfterConfirm).isEqualTo(versionBeforeConfirm);
    }

    @Test
    @DisplayName("Scenario E: Non-Hegemon observation reports RESOURCE_IS_NOT_HEGEMON without cache mutation")
    void testScenarioE_NonHegemonObservation() {
        InstanceId instanceA = InstanceId.random();
        InstanceId instanceB = InstanceId.random();
        ActiveStateToken token = coordinator.observe(sampleKey);

        coordinator.acquireOrConfirmHegemony(sampleKey, instanceA, token);

        RemoteCache<String, String> cache = clientA.getCache(HotRodActiveStateCoordinator.COORDINATION_CACHE_NAME);
        long versionBefore = cache.getWithMetadata(sampleKey.toQualifiedPath()).getVersion();

        ResourceHegemonStatus statusB = coordinator.checkHegemonStatus(sampleKey, instanceB);
        assertThat(statusB).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_NOT_HEGEMON);

        long versionAfter = cache.getWithMetadata(sampleKey.toQualifiedPath()).getVersion();
        assertThat(versionAfter).isEqualTo(versionBefore);
    }

    @Test
    @DisplayName("Scenario F: Non-Hegemon acquisition rejected with NotHegemon, preserving existing Hegemon")
    void testScenarioF_NonHegemonAcquisitionRejected() {
        InstanceId instanceA = InstanceId.random();
        InstanceId instanceB = InstanceId.random();
        ActiveStateToken token = coordinator.observe(sampleKey);

        HegemonyCoordinationResult acqResult = coordinator.acquireOrConfirmHegemony(sampleKey, instanceA, token);
        ActiveStateToken currentToken = ((HegemonyCoordinationResult.Acquired) acqResult).newToken();

        RemoteCache<String, String> cache = clientA.getCache(HotRodActiveStateCoordinator.COORDINATION_CACHE_NAME);
        long versionBefore = cache.getWithMetadata(sampleKey.toQualifiedPath()).getVersion();

        // Instance B attempts acquisition with the current valid token
        HegemonyCoordinationResult resultB = coordinator.acquireOrConfirmHegemony(sampleKey, instanceB, currentToken);
        assertThat(resultB).isInstanceOf(HegemonyCoordinationResult.NotHegemon.class);

        // Existing Hegemon remains instanceA and token generation does not advance
        long versionAfter = cache.getWithMetadata(sampleKey.toQualifiedPath()).getVersion();
        assertThat(versionAfter).isEqualTo(versionBefore);
        assertThat(coordinator.checkHegemonStatus(sampleKey, instanceA)).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_HEGEMON);
        assertThat(coordinator.checkHegemonStatus(sampleKey, instanceB)).isEqualTo(ResourceHegemonStatus.RESOURCE_IS_NOT_HEGEMON);
    }

    @Test
    @DisplayName("Scenario H: Corrupt/unknown coordination state fails closed with ActiveCoordinationCorruptException (AX-14/AX-15)")
    void testScenarioH_CorruptCoordinationStateFailsClosed() {
        InstanceId instanceA = InstanceId.random();
        RemoteCache<String, String> cache = clientA.getCache(HotRodActiveStateCoordinator.COORDINATION_CACHE_NAME);
        ActiveStateToken token = ActiveStateTokenBridge.create(1L);

        // Subcase 1: Blank/whitespace coordination state
        cache.put(sampleKey.toQualifiedPath(), "   \t \n");
        assertThatThrownBy(() -> coordinator.checkHegemonStatus(sampleKey, instanceA))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("blank or null");
        assertThatThrownBy(() -> coordinator.acquireOrConfirmHegemony(sampleKey, instanceA, token))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("blank or null");

        // Subcase 2: Legacy "ACTIVE" marker fails closed
        cache.put(sampleKey.toQualifiedPath(), "ACTIVE");
        assertThatThrownBy(() -> coordinator.checkHegemonStatus(sampleKey, instanceA))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("Unrecognised active coordination record encoding: ACTIVE");
        assertThatThrownBy(() -> coordinator.acquireOrConfirmHegemony(sampleKey, instanceA, token))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("Unrecognised active coordination record encoding: ACTIVE");

        // Subcase 3: Malformed InstanceId fails closed
        cache.put(sampleKey.toQualifiedPath(), "HEGEMON:not-a-uuid");
        assertThatThrownBy(() -> coordinator.checkHegemonStatus(sampleKey, instanceA))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("Malformed InstanceId");
        assertThatThrownBy(() -> coordinator.acquireOrConfirmHegemony(sampleKey, instanceA, token))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("Malformed InstanceId");
    }

    @Test
    @DisplayName("observe returns valid token for absent and subsequent existing entry")
    void testObserveAbsentAndExistingEntry() {
        ActiveStateToken token1 = coordinator.observe(sampleKey);
        assertThat(token1).isNotNull();

        ActiveStateToken token2 = coordinator.observe(sampleKey);
        assertThat(token2).isNotNull();
        assertThat(token2).isEqualTo(token1);
    }

    @Test
    @DisplayName("consume succeeds with CONSUMED, rejects stale token with STALE")
    void testConsumeProgressionAndStaleRejection() {
        ActiveStateToken token = coordinator.observe(sampleKey);
        assertThat(token).isNotNull();

        ActiveStateCoordinationResult result1 = coordinator.consume(sampleKey, token);
        assertThat(result1).isEqualTo(ActiveStateCoordinationResult.CONSUMED);

        // Attempting to reuse the now consumed token returns STALE
        ActiveStateCoordinationResult result2 = coordinator.consume(sampleKey, token);
        assertThat(result2).isEqualTo(ActiveStateCoordinationResult.STALE);

        // Observing again yields a newer token that can be consumed
        ActiveStateToken nextToken = coordinator.observe(sampleKey);
        assertThat(nextToken).isNotEqualTo(token);

        ActiveStateCoordinationResult result3 = coordinator.consume(sampleKey, nextToken);
        assertThat(result3).isEqualTo(ActiveStateCoordinationResult.CONSUMED);
    }

    @Test
    @DisplayName("Unavailability fail-fast: consume returns UNAVAILABLE and observe throws when server stops")
    void testUnavailabilityHandling() {
        ActiveStateToken token = coordinator.observe(sampleKey);
        assertThat(token).isNotNull();

        // Stop the Hot Rod server backing Client-A
        laboratoryServer.stopServer1();

        // consume must fail visibly with UNAVAILABLE without falling back to JVM-local state
        ActiveStateCoordinationResult consumeResult = coordinator.consume(sampleKey, token);
        assertThat(consumeResult).isEqualTo(ActiveStateCoordinationResult.UNAVAILABLE);

        // observe must fail visibly with ActiveCoordinationUnavailableException
        assertThatThrownBy(() -> coordinator.observe(ResourceKey.of("Patient", "fail-fast-test")))
                .isInstanceOf(ActiveCoordinationUnavailableException.class)
                .hasMessageContaining("Coordination cache unavailable");

        // checkHegemonStatus must fail visibly with ActiveCoordinationUnavailableException
        InstanceId instanceId = InstanceId.random();
        assertThatThrownBy(() -> coordinator.checkHegemonStatus(ResourceKey.of("Patient", "fail-fast-test"), instanceId))
                .isInstanceOf(ActiveCoordinationUnavailableException.class)
                .hasMessageContaining("Coordination cache unavailable");

        // acquireOrConfirmHegemony must return Unavailable
        HegemonyCoordinationResult acqResult = coordinator.acquireOrConfirmHegemony(sampleKey, instanceId, token);
        assertThat(acqResult).isInstanceOf(HegemonyCoordinationResult.Unavailable.class);
    }
}
