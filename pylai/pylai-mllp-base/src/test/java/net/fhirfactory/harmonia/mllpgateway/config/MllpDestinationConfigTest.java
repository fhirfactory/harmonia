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

package net.fhirfactory.harmonia.mllpgateway.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MllpDestinationConfigTest {

    @Test
    void testDefaultsAndCustomProperties() {
        MllpDestinationConfig config = new MllpDestinationConfig("HIS_NORTH", "192.168.1.100", 2575);

        assertThat(config.getDestinationId()).isEqualTo("HIS_NORTH");
        assertThat(config.getHost()).isEqualTo("192.168.1.100");
        assertThat(config.getPort()).isEqualTo(2575);
        assertThat(config.getConnectTimeoutMs()).isEqualTo(5000);
        assertThat(config.getReadTimeoutMs()).isEqualTo(10000);
        assertThat(config.getCharset()).isEqualTo("UTF-8");
        assertThat(config.isKeepAlive()).isTrue();
        assertThat(config.isAutoAck()).isTrue();
        assertThat(config.isEnabled()).isTrue();
        assertThat(config.isSslEnabled()).isFalse();
        assertThat(config.getEffectiveQueueName()).isEqualTo("petasos.queue.mllp.outbound.his_north");
    }

    @Test
    void testCustomQueueName() {
        MllpDestinationConfig config = new MllpDestinationConfig();
        config.setDestinationId("LIS_MAIN");
        config.setQueueName("custom.queue.lis");

        assertThat(config.getEffectiveQueueName()).isEqualTo("custom.queue.lis");
    }

    @Test
    void testCustomQueuePrefix() {
        MllpDestinationConfig config = new MllpDestinationConfig();
        config.setDestinationId("PACS_CORE");
        config.setQueuePrefix("task.outbound.queue");

        assertThat(config.getEffectiveQueueName()).isEqualTo("task.outbound.queue.pacs_core");
    }
}
