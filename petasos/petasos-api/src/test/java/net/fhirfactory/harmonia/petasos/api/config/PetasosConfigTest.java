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

package net.fhirfactory.harmonia.petasos.api.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PetasosConfigTest {

    @Test
    void testDefaultLocalConfiguration() {
        PetasosConfig config = PetasosConfig.defaultLocal();

        assertThat(config.getBrokerUrls()).containsExactly(PetasosConfig.DEFAULT_BROKER_URL);
        assertThat(config.getPrimaryBrokerUrl()).isEqualTo(PetasosConfig.DEFAULT_BROKER_URL);
        assertThat(config.getUsername()).isEqualTo(PetasosConfig.DEFAULT_USERNAME);
        assertThat(config.getPassword()).isEqualTo(PetasosConfig.DEFAULT_PASSWORD);
        assertThat(config.isHaEnabled()).isTrue();
        assertThat(config.getReconnectAttempts()).isEqualTo(-1);
        assertThat(config.getRetryInterval()).isEqualTo(500L);
        assertThat(config.getMaxRetryInterval()).isEqualTo(2000L);
        assertThat(config.getRetryIntervalMultiplier()).isEqualTo(1.5);
        assertThat(config.getConnectionTtl()).isEqualTo(60000L);
        assertThat(config.getClientFailureCheckPeriod()).isEqualTo(10000L);
        assertThat(config.getCallTimeout()).isEqualTo(30000L);
        assertThat(config.isDuplicateDetectionEnabled()).isTrue();
        assertThat(config.getDeadLetterAddress()).isEqualTo(PetasosConfig.DEFAULT_DLQ_ADDRESS);
        assertThat(config.getExpiryAddress()).isEqualTo(PetasosConfig.DEFAULT_EXPIRY_ADDRESS);
        assertThat(config.isSslEnabled()).isFalse();
    }

    @Test
    void testBuilderCustomProperties() {
        PetasosConfig config = PetasosConfig.builder()
                .brokerUrls(List.of("tcp://artemis-1:61616", "tcp://artemis-2:61616"))
                .username("harmonia")
                .password("harmoniaPassword")
                .haEnabled(false)
                .reconnectAttempts(5)
                .retryInterval(1000L)
                .maxRetryInterval(5000L)
                .retryIntervalMultiplier(2.0)
                .connectionTtl(30000L)
                .clientFailureCheckPeriod(5000L)
                .callTimeout(15000L)
                .duplicateDetectionEnabled(false)
                .deadLetterAddress("CustomDLQ")
                .expiryAddress("CustomExpiry")
                .sslEnabled(true)
                .trustStorePath("/path/to/truststore.jks")
                .trustStorePassword("trustpass")
                .keyStorePath("/path/to/keystore.jks")
                .keyStorePassword("keypass")
                .build();

        assertThat(config.getBrokerUrls()).containsExactly("tcp://artemis-1:61616", "tcp://artemis-2:61616");
        assertThat(config.getPrimaryBrokerUrl()).isEqualTo("tcp://artemis-1:61616");
        assertThat(config.getUsername()).isEqualTo("harmonia");
        assertThat(config.getPassword()).isEqualTo("harmoniaPassword");
        assertThat(config.isHaEnabled()).isFalse();
        assertThat(config.getReconnectAttempts()).isEqualTo(5);
        assertThat(config.getRetryInterval()).isEqualTo(1000L);
        assertThat(config.getMaxRetryInterval()).isEqualTo(5000L);
        assertThat(config.getRetryIntervalMultiplier()).isEqualTo(2.0);
        assertThat(config.getConnectionTtl()).isEqualTo(30000L);
        assertThat(config.getClientFailureCheckPeriod()).isEqualTo(5000L);
        assertThat(config.getCallTimeout()).isEqualTo(15000L);
        assertThat(config.isDuplicateDetectionEnabled()).isFalse();
        assertThat(config.getDeadLetterAddress()).isEqualTo("CustomDLQ");
        assertThat(config.getExpiryAddress()).isEqualTo("CustomExpiry");
        assertThat(config.isSslEnabled()).isTrue();
        assertThat(config.getTrustStorePath()).isEqualTo("/path/to/truststore.jks");
        assertThat(config.getTrustStorePassword()).isEqualTo("trustpass");
        assertThat(config.getKeyStorePath()).isEqualTo("/path/to/keystore.jks");
        assertThat(config.getKeyStorePassword()).isEqualTo("keypass");
    }

    @Test
    void testFromEnvironmentWithSystemProperties() {
        String prevUrls = System.getProperty(PetasosConfig.PROP_PETASOS_BROKER_URLS);
        String prevUser = System.getProperty(PetasosConfig.PROP_PETASOS_BROKER_USER);
        String prevPass = System.getProperty(PetasosConfig.PROP_PETASOS_BROKER_PASSWORD);
        String prevHa = System.getProperty(PetasosConfig.PROP_PETASOS_HA_ENABLED);

        try {
            System.setProperty(PetasosConfig.PROP_PETASOS_BROKER_URLS, "tcp://host1:61616,tcp://host2:61616");
            System.setProperty(PetasosConfig.PROP_PETASOS_BROKER_USER, "test-user");
            System.setProperty(PetasosConfig.PROP_PETASOS_BROKER_PASSWORD, "test-pass");
            System.setProperty(PetasosConfig.PROP_PETASOS_HA_ENABLED, "false");

            PetasosConfig config = PetasosConfig.fromEnvironment();

            assertThat(config.getBrokerUrls()).containsExactly("tcp://host1:61616", "tcp://host2:61616");
            assertThat(config.getUsername()).isEqualTo("test-user");
            assertThat(config.getPassword()).isEqualTo("test-pass");
            assertThat(config.isHaEnabled()).isFalse();
        } finally {
            restoreProperty(PetasosConfig.PROP_PETASOS_BROKER_URLS, prevUrls);
            restoreProperty(PetasosConfig.PROP_PETASOS_BROKER_USER, prevUser);
            restoreProperty(PetasosConfig.PROP_PETASOS_BROKER_PASSWORD, prevPass);
            restoreProperty(PetasosConfig.PROP_PETASOS_HA_ENABLED, prevHa);
        }
    }

    private void restoreProperty(String key, String value) {
        if (value != null) {
            System.setProperty(key, value);
        } else {
            System.clearProperty(key);
        }
    }
}
