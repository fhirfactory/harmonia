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

import net.fhirfactory.harmonia.befe.model.operations.OperationalInstance;
import net.fhirfactory.harmonia.befe.provider.KubernetesInstanceProvider;
import net.fhirfactory.harmonia.befe.service.ModuleStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KubernetesInstanceProviderTest {

    private ModuleStatusService moduleStatusService;
    private KubernetesInstanceProvider instanceProvider;

    @BeforeEach
    void setUp() {
        moduleStatusService = new ModuleStatusService();
        instanceProvider = new KubernetesInstanceProvider(moduleStatusService);
        instanceProvider.init();
    }

    @Test
    @DisplayName("1. Outside Kubernetes: provider falls back to local discovery")
    void testOutsideKubernetesFallback() {
        assertThat(instanceProvider.isKubernetesAvailable()).isFalse();
    }

    @Test
    @DisplayName("2. Discovers instances registered in Infinispan modulestatus-cache")
    void testDiscoversModuleStatusInstances() {
        moduleStatusService.registerModule("petasos-broker-1", "Petasos Artemis Broker", "MESSAGING", true);

        List<OperationalInstance> instances = instanceProvider.getInstances("petasos");
        assertThat(instances).isNotEmpty();

        OperationalInstance instance = instances.stream()
                .filter(i -> "petasos-broker-1".equals(i.getInstanceId()))
                .findFirst()
                .orElse(null);

        assertThat(instance).isNotNull();
        assertThat(instance.getSubsystemId()).isEqualTo("petasos");
        assertThat(instance.isReady()).isTrue();
        assertThat(instance.getState()).isEqualTo("Running");
        // MONITOR-GAP-001: Outside Kubernetes, cpuPercent is -1.0 (N/A) rather than fake 0
        assertThat(instance.getCpuPercent()).isEqualTo(-1.0);
        assertThat(instance.getMemoryMb()).isGreaterThan(0);
    }

    @Test
    @DisplayName("3. Synthesizes default local instance when no cached module found")
    void testSynthesizesLocalInstance() {
        List<OperationalInstance> instances = instanceProvider.getInstances("themis");
        assertThat(instances).hasSize(1);

        OperationalInstance inst = instances.get(0);
        assertThat(inst.getInstanceId()).isEqualTo("themis-0");
        assertThat(inst.getSubsystemId()).isEqualTo("themis");
        assertThat(inst.getRole()).isEqualTo("Primary");
        assertThat(inst.getState()).isEqualTo("Running");
        assertThat(inst.getCpuPercent()).isEqualTo(-1.0);
        assertThat(inst.getMemoryMb()).isGreaterThan(0);
        assertThat(inst.getUptime()).isNotNull();
    }

    @Test
    @DisplayName("4. Subsystem alias matching resolves related component modules")
    void testSubsystemAliasMatching() {
        moduleStatusService.registerModule("ponos-workengine", "Ponos Work Engine", "WORKFLOW", true);
        moduleStatusService.registerModule("iris-befe", "Iris BEFE Gateway", "PRESENTATION", true);

        List<OperationalInstance> energeiaInstances = instanceProvider.getInstances("energeia");
        assertThat(energeiaInstances.stream().anyMatch(i -> "ponos-workengine".equals(i.getInstanceId()))).isTrue();

        List<OperationalInstance> irisInstances = instanceProvider.getInstances("iris");
        assertThat(irisInstances.stream().anyMatch(i -> "iris-befe".equals(i.getInstanceId()))).isTrue();
    }
}
