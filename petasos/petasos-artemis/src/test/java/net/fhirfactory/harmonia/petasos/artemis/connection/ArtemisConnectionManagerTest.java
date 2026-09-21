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

package net.fhirfactory.harmonia.petasos.artemis.connection;

import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;
import net.fhirfactory.harmonia.petasos.api.health.ConnectionState;
import net.fhirfactory.harmonia.petasos.api.health.HealthStatus;
import net.fhirfactory.harmonia.petasos.api.health.PetasosHealth;
import net.fhirfactory.harmonia.petasos.api.topology.PetasosBrokerTopology;
import net.fhirfactory.harmonia.petasos.core.metrics.PetasosMetricsCollector;
import org.apache.activemq.artemis.api.core.client.FailoverEventType;
import org.apache.activemq.artemis.api.core.client.ServerLocator;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArtemisConnectionManagerTest {

    @Test
    void testBuildClusterConnectionUrlSingleBrokerNoHa() {
        PetasosConfig config = PetasosConfig.builder()
                .brokerUrls(List.of("tcp://petasos:61616"))
                .haEnabled(false)
                .build();

        ArtemisConnectionManager manager = new ArtemisConnectionManager(config, new PetasosMetricsCollector());
        String url = manager.buildClusterConnectionUrl(config);

        assertThat(url).startsWith("tcp://petasos:61616?");
        assertThat(url).doesNotContain("ha=true");
        assertThat(config.isHaEnabled()).isFalse();
    }

    @Test
    void testBuildClusterConnectionUrlMultiBrokerWithHa() {
        PetasosConfig config = PetasosConfig.builder()
                .brokerUrls(List.of("tcp://artemis-primary-a:61616", "tcp://artemis-primary-b:61616"))
                .haEnabled(true)
                .build();

        ArtemisConnectionManager manager = new ArtemisConnectionManager(config, new PetasosMetricsCollector());
        String url = manager.buildClusterConnectionUrl(config);

        assertThat(url).startsWith("(tcp://artemis-primary-a:61616,tcp://artemis-primary-b:61616)?ha=true");
        assertThat(config.isHaEnabled()).isTrue();
    }

    @Test
    void testActiveMQConnectionFactoryServerLocatorWithHaDisabled() {
        PetasosConfig config = PetasosConfig.builder()
                .brokerUrls(List.of("tcp://petasos:61616"))
                .haEnabled(false)
                .build();

        ArtemisConnectionManager manager = new ArtemisConnectionManager(config, new PetasosMetricsCollector());
        String url = manager.buildClusterConnectionUrl(config);

        try (ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url)) {
            ServerLocator locator = factory.getServerLocator();
            assertThat(locator).isNotNull();
            assertThat(locator.isHA()).isFalse();
        }
    }

    @Test
    void testActiveMQConnectionFactoryServerLocatorWithHaEnabled() {
        PetasosConfig config = PetasosConfig.builder()
                .brokerUrls(List.of("tcp://artemis-primary-a:61616", "tcp://artemis-primary-b:61616"))
                .haEnabled(true)
                .build();

        ArtemisConnectionManager manager = new ArtemisConnectionManager(config, new PetasosMetricsCollector());
        String url = manager.buildClusterConnectionUrl(config);

        try (ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url)) {
            ServerLocator locator = factory.getServerLocator();
            assertThat(locator).isNotNull();
            assertThat(locator.isHA()).isTrue();
        }
    }

    @Test
    void testBrokerTopologyDefault() {
        PetasosConfig config = PetasosConfig.builder()
                .brokerUrls(List.of("tcp://petasos:61616"))
                .haEnabled(false)
                .build();

        ArtemisConnectionManager manager = new ArtemisConnectionManager(config, new PetasosMetricsCollector());
        PetasosBrokerTopology topology = manager.brokerTopology();

        assertThat(topology).isNotNull();
        assertThat(topology.getNodes()).isEmpty();
        assertThat(topology.getReplicationPairs()).isEmpty();
    }

    @Test
    void testFailoverEventsAndHealth() {
        PetasosConfig config = PetasosConfig.builder()
                .brokerUrls(List.of("tcp://petasos:61616"))
                .haEnabled(false)
                .build();

        ArtemisConnectionManager manager = new ArtemisConnectionManager(config, new PetasosMetricsCollector());

        assertThat(manager.getConnectionState()).isEqualTo(ConnectionState.DISCONNECTED);

        manager.failoverEvent(FailoverEventType.FAILURE_DETECTED);
        assertThat(manager.getConnectionState()).isEqualTo(ConnectionState.RECONNECTING);

        manager.failoverEvent(FailoverEventType.FAILOVER_COMPLETED);
        assertThat(manager.getConnectionState()).isEqualTo(ConnectionState.CONNECTED);
        assertThat(manager.getReconnectCount()).isEqualTo(1);

        manager.failoverEvent(FailoverEventType.FAILOVER_FAILED);
        assertThat(manager.getConnectionState()).isEqualTo(ConnectionState.FAILED);

        PetasosHealth health = manager.health();
        assertThat(health.getStatus()).isEqualTo(HealthStatus.DOWN);
        assertThat(health.getDetails()).containsEntry("haEnabled", false);
    }

    @Test
    void testHealthDetailsWithHaEnabled() {
        PetasosConfig config = PetasosConfig.builder()
                .brokerUrls(List.of("tcp://artemis-primary-a:61616", "tcp://artemis-primary-b:61616"))
                .haEnabled(true)
                .build();

        ArtemisConnectionManager manager = new ArtemisConnectionManager(config, new PetasosMetricsCollector());
        PetasosHealth health = manager.health();

        assertThat(health.getDetails()).containsEntry("haEnabled", true);
        assertThat(health.getDetails()).containsEntry("discoveredClusterNodes", 0);
    }
}
