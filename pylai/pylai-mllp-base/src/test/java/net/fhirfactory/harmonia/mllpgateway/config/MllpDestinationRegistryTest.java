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

import net.fhirfactory.harmonia.model.topic.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MllpDestinationRegistryTest {

    private MllpDestinationRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new MllpDestinationRegistry();
    }

    @Test
    void testRegisterAndRetrieve() {
        MllpDestinationConfig his = new MllpDestinationConfig("HIS_NORTH", "Hospital North", "10.0.1.10", 2575);
        registry.registerDestination(his);

        assertThat(registry.hasDestination("HIS_NORTH")).isTrue();
        assertThat(registry.hasDestination("his_north")).isTrue();
        assertThat(registry.size()).isEqualTo(1);

        Optional<MllpDestinationConfig> found = registry.getDestination("HIS_NORTH");
        assertThat(found).isPresent();
        assertThat(found.get().getHost()).isEqualTo("10.0.1.10");
        assertThat(found.get().getPort()).isEqualTo(2575);
    }

    @Test
    void testResolveByTopic() {
        MllpDestinationConfig his = new MllpDestinationConfig("HIS_NORTH", "Hospital North", "10.0.1.10", 2575, "HOSP_N", null);
        MllpDestinationConfig lis = new MllpDestinationConfig("LIS_MAIN", "Central Lab", "10.0.2.20", 2576, "LAB_MAIN", null);

        registry.registerDestination(his);
        registry.registerDestination(lis);

        // Resolve by topic destination
        Topic topic1 = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        Optional<MllpDestinationConfig> resolved1 = registry.resolveDestination(topic1);
        assertThat(resolved1).isPresent();
        assertThat(resolved1.get().getDestinationId()).isEqualTo("HIS_NORTH");

        // Resolve by topic target
        Topic topic2 = Topic.forEgress("ORU", "R01", "harmonia", "LIS_MAIN", null);
        Optional<MllpDestinationConfig> resolved2 = registry.resolveDestination(topic2);
        assertThat(resolved2).isPresent();
        assertThat(resolved2.get().getDestinationId()).isEqualTo("LIS_MAIN");

        // Resolve by topic origin (facility code)
        Topic topic3 = new Topic();
        topic3.setOrigin("LAB_MAIN");
        Optional<MllpDestinationConfig> resolved3 = registry.resolveDestination(topic3);
        assertThat(resolved3).isPresent();
        assertThat(resolved3.get().getDestinationId()).isEqualTo("LIS_MAIN");
    }

    @Test
    void testResolveByIdentifierOrFacility() {
        MllpDestinationConfig pacs = new MllpDestinationConfig("PACS_CORE", "Imaging PACS", "10.0.3.30", 2577, "IMAGING_DEPT", null);
        registry.registerDestination(pacs);

        assertThat(registry.resolveDestination("PACS_CORE")).isPresent();
        assertThat(registry.resolveDestination("pacs_core")).isPresent();
        assertThat(registry.resolveDestination("IMAGING_DEPT")).isPresent();
        assertThat(registry.resolveDestination("Imaging PACS")).isPresent();
        assertThat(registry.resolveDestination("UNKNOWN")).isEmpty();
    }

    @Test
    void testUnregisterAndClear() {
        MllpDestinationConfig dest1 = new MllpDestinationConfig("DEST1", "host1", 2575);
        MllpDestinationConfig dest2 = new MllpDestinationConfig("DEST2", "host2", 2576);

        registry.registerDestination(dest1);
        registry.registerDestination(dest2);
        assertThat(registry.size()).isEqualTo(2);

        registry.unregisterDestination("DEST1");
        assertThat(registry.hasDestination("DEST1")).isFalse();
        assertThat(registry.size()).isEqualTo(1);

        registry.clear();
        assertThat(registry.size()).isEqualTo(0);
        assertThat(registry.getDefaultDestination()).isEmpty();
    }

    @Test
    void testInvalidRegistration() {
        assertThatThrownBy(() -> registry.registerDestination(null))
                .isInstanceOf(IllegalArgumentException.class);

        MllpDestinationConfig emptyId = new MllpDestinationConfig();
        assertThatThrownBy(() -> registry.registerDestination(emptyId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
