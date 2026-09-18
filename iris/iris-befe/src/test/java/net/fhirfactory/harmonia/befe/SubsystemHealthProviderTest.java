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

package net.fhirfactory.harmonia.befe;

import net.fhirfactory.harmonia.befe.model.operations.OperationalHealth;
import net.fhirfactory.harmonia.befe.model.operations.OperationalInstance;
import net.fhirfactory.harmonia.befe.model.operations.OperationalSubsystem;
import net.fhirfactory.harmonia.befe.model.operations.TimeSeries;
import net.fhirfactory.harmonia.befe.provider.*;
import net.fhirfactory.harmonia.befe.service.ModuleStatusService;
import net.fhirfactory.harmonia.model.status.ModuleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SubsystemHealthProviderTest {

    private ModuleStatusService moduleStatusService;
    private KubernetesInstanceProvider instanceProvider;

    private PylaiHealthProvider pylaiProvider;
    private PetasosHealthProvider petasosProvider;
    private EnergeiaHealthProvider energeiaProvider;
    private MnemeHealthProvider mnemeProvider;
    private MnemosyneHealthProvider mnemosyneProvider;
    private CalliopeHealthProvider calliopeProvider;
    private ThemisHealthProvider themisProvider;
    private AgoraHealthProvider agoraProvider;
    private IrisHealthProvider irisProvider;

    @BeforeEach
    void setUp() {
        moduleStatusService = new ModuleStatusService();
        instanceProvider = new KubernetesInstanceProvider(moduleStatusService);

        pylaiProvider = new PylaiHealthProvider();
        configure(pylaiProvider);

        petasosProvider = new PetasosHealthProvider();
        configure(petasosProvider);

        energeiaProvider = new EnergeiaHealthProvider();
        configure(energeiaProvider);

        mnemeProvider = new MnemeHealthProvider();
        configure(mnemeProvider);

        mnemosyneProvider = new MnemosyneHealthProvider();
        configure(mnemosyneProvider);

        calliopeProvider = new CalliopeHealthProvider();
        configure(calliopeProvider);

        themisProvider = new ThemisHealthProvider();
        configure(themisProvider);

        agoraProvider = new AgoraHealthProvider();
        configure(agoraProvider);

        irisProvider = new IrisHealthProvider();
        configure(irisProvider);
    }

    private void configure(AbstractSubsystemHealthProvider provider) {
        provider.setModuleStatusService(moduleStatusService);
        provider.setInstanceProvider(instanceProvider);
    }

    @Test
    @DisplayName("1. Complete inventory: all 9 Harmonia subsystems have dedicated providers")
    void testSubsystemInventory() {
        List<SubsystemHealthProvider> providers = List.of(
                pylaiProvider, petasosProvider, energeiaProvider,
                mnemeProvider, mnemosyneProvider, calliopeProvider,
                themisProvider, agoraProvider, irisProvider
        );

        assertThat(providers).hasSize(9);
        List<String> ids = providers.stream().map(SubsystemHealthProvider::getSubsystemId).toList();
        assertThat(ids).containsExactlyInAnyOrder(
                "pylai", "petasos", "energeia", "mneme",
                "mnemosyne", "calliope", "themis", "agora", "iris"
        );
    }

    @Test
    @DisplayName("2. Provider telemetry normalization: returns valid overview, health, and instances")
    void testProviderTelemetryNormalization() {
        // Register mock healthy module status for Pylai and its dependencies
        moduleStatusService.registerModule("pylai", "Pylai Gateway", "GATEWAY", true);
        moduleStatusService.registerModule("petasos", "Petasos Messaging", "MESSAGING", true);
        moduleStatusService.registerModule("mnemosyne", "Mnemosyne Storage", "PERSISTENCE", true);
        moduleStatusService.registerModule("themis", "Themis Security", "SECURITY", true);

        OperationalSubsystem overview = pylaiProvider.getSubsystemOverview();
        assertThat(overview.getId()).isEqualTo("pylai");
        assertThat(overview.getName()).isEqualTo("Pylai");
        assertThat(overview.getState()).isEqualTo("HEALTHY");
        assertThat(overview.getInstanceCount()).isGreaterThanOrEqualTo(1);

        OperationalHealth health = pylaiProvider.getOperationalHealth();
        assertThat(health.getSubsystemId()).isEqualTo("pylai");
        assertThat(health.getStatus()).isEqualTo("HEALTHY");
        assertThat(health.getDependencies()).isNotEmpty();
        assertThat(health.getDependenciesSummary()).contains("Healthy");

        // Anti-fabrication assertions: unmeasured metrics are null rather than fabricated numbers
        assertThat(health.getAvailabilityPercent()).isNull();
        assertThat(health.getP95LatencyMs()).isNull();
        assertThat(health.getDependencies().stream().allMatch(d -> d.getLatencyMs() == null)).isTrue();
        assertThat(health.getDependencies().stream().allMatch(d -> "HEALTHY".equals(d.getStatus()))).isTrue();

        List<OperationalInstance> instances = pylaiProvider.getInstances();
        assertThat(instances).isNotEmpty();
        assertThat(instances.get(0).getSubsystemId()).isEqualTo("pylai");
    }

    @Test
    @DisplayName("3. Energeia hierarchy: returns child subsystems Ponos and Praxis")
    void testEnergeiaChildHierarchy() {
        OperationalSubsystem overview = energeiaProvider.getSubsystemOverview();
        assertThat(overview.getId()).isEqualTo("energeia");
        assertThat(overview.getChildren()).hasSize(2);

        List<String> childIds = overview.getChildren().stream().map(OperationalSubsystem::getId).toList();
        assertThat(childIds).containsExactly("ponos", "praxis");
    }

    @Test
    @DisplayName("4. Time-series statistics: empty series without fabricated data across windows")
    void testTimeSeriesStatistics() {
        String[] windows = {"15m", "1h", "6h", "24h"};
        for (String win : windows) {
            Map<String, TimeSeries> stats = petasosProvider.getStatistics(win);
            assertThat(stats).isNotEmpty();
            assertThat(stats).containsKey("enqueue_rate");
            TimeSeries ts = stats.get("enqueue_rate");
            assertThat(ts.getWindow()).isEqualTo(win);
            assertThat(ts.getPoints()).isEmpty();
            assertThat(ts.getSummary()).isEmpty();
        }
    }

    @Test
    @DisplayName("5. Graceful degradation: handles UNKNOWN, DEGRADED, and UNAVAILABLE states consistently")
    void testGracefulDegradation() {
        // When no module status is registered, state is UNKNOWN
        assertThat(petasosProvider.getSubsystemOverview().getState()).isEqualTo("UNKNOWN");
        OperationalHealth unknownHealth = petasosProvider.getOperationalHealth();
        assertThat(unknownHealth.getStatus()).isEqualTo("UNKNOWN");
        assertThat(unknownHealth.getAvailabilityPercent()).isNull();
        assertThat(unknownHealth.getP95LatencyMs()).isNull();
        assertThat(unknownHealth.getDependencies().stream().noneMatch(d -> "HEALTHY".equals(d.getStatus()))).isTrue();

        // Stale module status (> 5 minutes old) -> DEGRADED
        ModuleStatus staleModule = new ModuleStatus("petasos", "Petasos", "MESSAGING", "READY", true);
        staleModule.setLastUpdated(Instant.now().minusSeconds(600).toString());
        moduleStatusService.updateStatus(staleModule, false);

        assertThat(petasosProvider.getSubsystemOverview().getState()).isEqualTo("DEGRADED");
        OperationalHealth degradedHealth = petasosProvider.getOperationalHealth();
        assertThat(degradedHealth.getStatus()).isEqualTo("DEGRADED");

        // Stopped module status -> UNAVAILABLE
        moduleStatusService.unregisterModule("petasos");
        assertThat(petasosProvider.getSubsystemOverview().getState()).isEqualTo("UNAVAILABLE");
        assertThat(petasosProvider.isAvailable()).isFalse();

        // Ensure getOperationalHealth() is consistent with UNAVAILABLE state
        OperationalHealth health = petasosProvider.getOperationalHealth();
        assertThat(health.getStatus()).isEqualTo("UNAVAILABLE");
        assertThat(health.getAvailabilityPercent()).isEqualTo(0.0);
        assertThat(health.getP95LatencyMs()).isNull();
        assertThat(health.getDependencies().stream().allMatch(d -> "UNAVAILABLE".equals(d.getStatus()))).isTrue();
        assertThat(health.getDependenciesSummary()).contains("Unavailable");
    }

    @Test
    @DisplayName("6. Calliope foundation provider is self-healthy and embedded")
    void testCalliopeProvider() {
        OperationalSubsystem overview = calliopeProvider.getSubsystemOverview();
        assertThat(overview.getState()).isEqualTo("HEALTHY");

        OperationalHealth health = calliopeProvider.getOperationalHealth();
        assertThat(health.getStatus()).isEqualTo("HEALTHY");
        assertThat(health.getDependencies()).isEmpty();
        assertThat(health.getDetails()).containsEntry("embedded", true);
    }
}
