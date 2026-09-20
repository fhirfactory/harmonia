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

package net.fhirfactory.harmonia.praxis.service;

import net.fhirfactory.harmonia.model.status.ModuleStatus;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.health.ConnectionState;
import net.fhirfactory.harmonia.petasos.api.health.HealthStatus;
import net.fhirfactory.harmonia.petasos.api.health.PetasosHealth;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PetasosModuleStatusPublisherTest {

    private ModuleStatusService moduleStatusService;
    private Petasos petasos;
    private PetasosModuleStatusPublisher publisher;

    @BeforeEach
    void setUp() {
        moduleStatusService = new ModuleStatusService();
        petasos = mock(Petasos.class);
        publisher = new PetasosModuleStatusPublisher();
        publisher.setModuleStatusService(moduleStatusService);
        publisher.setPetasos(petasos);
        publisher.setHeartbeatSeconds(3600);
    }

    @AfterEach
    void tearDown() {
        publisher.stop();
    }

    @Test
    @DisplayName("Publishes READY petasos ModuleStatus with broker telemetry details from petasos.health()")
    void publishOnce_healthyBroker_registersExpectedDetails() {
        Map<String, Object> healthDetails = Map.of(
                "primaryUrl", "tcp://petasos:61616",
                "haEnabled", false,
                "configuredUrls", java.util.List.of("tcp://petasos:61616"),
                "discoveredClusterNodes", 1
        );
        PetasosHealth health = new PetasosHealth(
                HealthStatus.UP,
                ConnectionState.CONNECTED,
                "tcp://petasos:61616",
                "artemis-standalone",
                0L,
                Instant.now(),
                healthDetails
        );
        when(petasos.health()).thenReturn(health);

        publisher.publishOnce();

        Optional<ModuleStatus> statusOpt = moduleStatusService.getModuleStatus(PetasosModuleStatusPublisher.MODULE_ID);
        assertThat(statusOpt).isPresent();
        ModuleStatus status = statusOpt.get();
        assertThat(status.getModuleId()).isEqualTo("petasos");
        assertThat(status.getStatus()).isEqualTo("READY");
        assertThat(status.isReady()).isTrue();
        assertThat(status.getDetails())
                .containsEntry("brokerStatus", "HEALTHY")
                .containsEntry("connectionState", "CONNECTED")
                .containsEntry("connectedBroker", "tcp://petasos:61616")
                .containsEntry("brokerTopology", "Standalone Single-Broker")
                .containsEntry("primaryUrl", "tcp://petasos:61616");
        assertThat(String.valueOf(status.getDetails().get("brokerMessage")))
                .contains("tcp://petasos:61616");
    }

    @Test
    @DisplayName("Publishes ERROR/UNAVAILABLE details when petasos.health() reports DOWN")
    void publishOnce_downBroker_registersUnavailable() {
        PetasosHealth health = PetasosHealth.down(
                ConnectionState.FAILED,
                "tcp://petasos:61616",
                3L,
                Map.of("primaryUrl", "tcp://petasos:61616", "haEnabled", false)
        );
        when(petasos.health()).thenReturn(health);

        publisher.publishOnce();

        ModuleStatus status = moduleStatusService.getModuleStatus("petasos").orElseThrow();
        assertThat(status.getStatus()).isEqualTo("ERROR");
        assertThat(status.isReady()).isFalse();
        assertThat(status.getDetails())
                .containsEntry("brokerStatus", "UNAVAILABLE")
                .containsEntry("connectionState", "FAILED")
                .containsEntry("reconnectCount", 3L);
    }

    @Test
    @DisplayName("toModuleDetails maps null health safely")
    void toModuleDetails_nullHealth() {
        Map<String, Object> details = PetasosModuleStatusPublisher.toModuleDetails(null);
        assertThat(details)
                .containsEntry("brokerStatus", "UNKNOWN")
                .containsEntry("connectionState", "DISCONNECTED")
                .containsEntry("brokerTopology", "Standalone Single-Broker");
    }
}
