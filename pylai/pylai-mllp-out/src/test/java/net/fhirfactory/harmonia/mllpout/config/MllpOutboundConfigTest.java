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

package net.fhirfactory.harmonia.mllpout.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MllpOutboundConfigTest {

    @Test
    void testDedicatedEventQueueResolution() {
        MllpOutboundConfig config = new MllpOutboundConfig("mllp-sender-his", "HIS_NORTH");
        assertThat(config.getDedicatedEventQueueName()).isEqualTo("petasos.queue.mllp.outbound.his_north");
        assertThat(config.getDedicatedEventQueueName("LIS_MAIN")).isEqualTo("petasos.queue.mllp.outbound.lis_main");
    }

    @Test
    void testFallbackToInstanceId() {
        MllpOutboundConfig config = new MllpOutboundConfig("instance-01", null);
        assertThat(config.getDedicatedEventQueueName()).isEqualTo("petasos.queue.mllp.outbound.instance-01");
    }
}
