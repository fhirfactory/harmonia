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
    }
}
