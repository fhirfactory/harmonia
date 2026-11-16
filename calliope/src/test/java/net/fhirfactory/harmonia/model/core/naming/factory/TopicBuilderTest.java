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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.core.naming.factory;

import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TopicBuilder Invariant & Validation Tests")
public class TopicBuilderTest {

    @Test
    @DisplayName("Should build valid Topic with sequential entries")
    void testSuccessfulBuild() {
        UUID id = UUID.randomUUID();
        Topic topic = new TopicBuilder(id)
                .addEntry("Country", "Australia")
                .addEntry("StateTerritory", "NSW")
                .addEntry("City", "Sydney")
                .build();

        assertThat(topic.getId()).isEqualTo(id);
        assertThat(topic.getDepth()).isEqualTo(3);
        assertThat(topic.getName()).isEqualTo("Australia.NSW.Sydney");
    }

    @Test
    @DisplayName("Should reject duplicate sequence positions without corrupting builder state")
    void testRejectDuplicateSequencePositions() {
        TopicBuilder builder = new TopicBuilder()
                .addEntry(0, "Country", "Australia")
                .addEntry(1, "StateTerritory", "ACT");

        // Attempt to overwrite position 0 with duplicate sequence number
        assertThatThrownBy(() -> builder.addEntry(0, "Country", "NewZealand"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicate Topic sequence position rejected: 0");

        // Attempt to overwrite position 1 with TopicEntry
        assertThatThrownBy(() -> builder.addEntry(1, new TopicEntry("State", "NSW")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicate Topic sequence position rejected: 1");

        // Verify that builder state was NOT corrupted and can be completed
        builder.addEntry(2, "City", "Canberra");
        Topic topic = builder.build();

        assertThat(topic.getDepth()).isEqualTo(3);
        assertThat(topic.getEntry(0).getName()).isEqualTo("Australia");
        assertThat(topic.getEntry(1).getName()).isEqualTo("ACT");
        assertThat(topic.getEntry(2).getName()).isEqualTo("Canberra");
        assertThat(topic.getName()).isEqualTo("Australia.ACT.Canberra");
    }

    @Test
    @DisplayName("Should reject negative sequence numbers")
    void testRejectNegativeSequenceNumber() {
        TopicBuilder builder = new TopicBuilder();
        assertThatThrownBy(() -> builder.addEntry(-1, "Country", "Australia"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Sequence number must be non-negative: -1");
    }

    @Test
    @DisplayName("Should reject building with gaps in sequence numbers")
    void testRejectGapsInSequence() {
        TopicBuilder builder = new TopicBuilder()
                .addEntry(0, "Country", "Australia")
                .addEntry(2, "City", "Sydney"); // Missing index 1

        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("sequence has gap at position: 1");
    }

    @Test
    @DisplayName("Should reject building without entries")
    void testRejectEmptyBuild() {
        TopicBuilder builder = new TopicBuilder();
        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot build Topic with no canonical name entries");
    }

    @Test
    @DisplayName("Should reject blank qualifier or name")
    void testRejectBlankQualifierOrName() {
        TopicBuilder builder = new TopicBuilder();
        assertThatThrownBy(() -> builder.addEntry("", "Australia"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("qualifier must not be null or blank");

        assertThatThrownBy(() -> builder.addEntry("Country", "  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name must not be null or blank");
    }

    @Test
    @DisplayName("Topic entry supports version strings with dots")
    void testTopicEntrySupportsVersionStringsWithDots() {
        TopicEntry entry = new TopicEntry("ModelVersion", "2.4");
        assertThat(entry.getName()).isEqualTo("2.4");
        assertThat(entry.getQualifier()).isEqualTo("ModelVersion");
    }
}
