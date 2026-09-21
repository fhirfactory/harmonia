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

package net.fhirfactory.harmonia.petasos.core.config;

import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosPropertyResolverTest {

    @Test
    void testResolveFromPropertiesWithHaDisabled() {
        Properties props = new Properties();
        props.setProperty(PetasosPropertyResolver.PROP_BROKER_URL, "tcp://petasos:61616");
        props.setProperty(PetasosPropertyResolver.PROP_BROKER_USER, "harmonia");
        props.setProperty(PetasosPropertyResolver.PROP_BROKER_PASSWORD, "harmoniaPassword");
        props.setProperty(PetasosPropertyResolver.PROP_HA_ENABLED, "false");
        props.setProperty(PetasosPropertyResolver.PROP_RECONNECT_ATTEMPTS, "3");

        PetasosConfig config = PetasosPropertyResolver.fromProperties(props);

        assertThat(config.getBrokerUrls()).containsExactly("tcp://petasos:61616");
        assertThat(config.getUsername()).isEqualTo("harmonia");
        assertThat(config.getPassword()).isEqualTo("harmoniaPassword");
        assertThat(config.isHaEnabled()).isFalse();
        assertThat(config.getReconnectAttempts()).isEqualTo(3);
    }

    @Test
    void testResolveFromPropertiesWithHaEnabled() {
        Properties props = new Properties();
        props.setProperty(PetasosPropertyResolver.PROP_BROKER_URLS, "tcp://artemis-primary-a:61616,tcp://artemis-primary-b:61616");
        props.setProperty(PetasosPropertyResolver.PROP_BROKER_USER, "admin");
        props.setProperty(PetasosPropertyResolver.PROP_BROKER_PASSWORD, "adminPassword");
        props.setProperty(PetasosPropertyResolver.PROP_HA_ENABLED, "true");

        PetasosConfig config = PetasosPropertyResolver.fromProperties(props);

        assertThat(config.getBrokerUrls()).containsExactly("tcp://artemis-primary-a:61616", "tcp://artemis-primary-b:61616");
        assertThat(config.getUsername()).isEqualTo("admin");
        assertThat(config.getPassword()).isEqualTo("adminPassword");
        assertThat(config.isHaEnabled()).isTrue();
    }
}
