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

package net.fhirfactory.harmonia.praxis.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QueueConfigTest {

    private QueueConfig queueConfig;

    @BeforeEach
    void setUp() {
        queueConfig = new QueueConfig();
    }

    @AfterEach
    void tearDown() {
        System.clearProperty("petasos.ha.enabled");
        System.clearProperty("task.broker.ha.enabled");
        System.clearProperty("petasos.broker.url");
        System.clearProperty("petasos.broker.user");
        System.clearProperty("petasos.broker.password");
    }

    @Test
    void testDefaultHaEnabled() {
        assertThat(queueConfig.isHaEnabled()).isTrue();
    }

    @Test
    void testCustomHaEnabled() {
        queueConfig.setHaEnabled(false);
        assertThat(queueConfig.isHaEnabled()).isFalse();

        queueConfig.setHaEnabled(true);
        assertThat(queueConfig.isHaEnabled()).isTrue();
    }

    @Test
    void testHaEnabledFromSystemProperty() {
        System.setProperty("petasos.ha.enabled", "false");
        assertThat(queueConfig.isHaEnabled()).isFalse();

        System.setProperty("petasos.ha.enabled", "true");
        assertThat(queueConfig.isHaEnabled()).isTrue();
    }

    @Test
    void testTaskBrokerHaEnabledFromSystemProperty() {
        System.setProperty("task.broker.ha.enabled", "false");
        assertThat(queueConfig.isHaEnabled()).isFalse();
    }

    @Test
    void testBrokerCredentialsAndUrl() {
        System.setProperty("petasos.broker.url", "tcp://custom-broker:61616");
        System.setProperty("petasos.broker.user", "harmonia");
        System.setProperty("petasos.broker.password", "harmoniaPassword");

        assertThat(queueConfig.getBrokerUrl()).isEqualTo("tcp://custom-broker:61616");
        assertThat(queueConfig.getBrokerUsername()).isEqualTo("harmonia");
        assertThat(queueConfig.getBrokerPassword()).isEqualTo("harmoniaPassword");
    }
}
