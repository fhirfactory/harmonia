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

package net.fhirfactory.harmonia.model.core.naming;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicEntry;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicBuilder;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Topic Canonical Naming Model Tests")
public class TopicTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Topic explicitly sequenced construction and derived names")
    void testExplicitCanonicalSequencingAndDerivedNames() {
        UUID id = UUID.randomUUID();
        SortedMap<Integer, TopicEntry> entries = new TreeMap<>();
        entries.put(0, new TopicEntry("Country", "Australia"));
        entries.put(1, new TopicEntry("StateTerritory", "ACT"));
        entries.put(2, new TopicEntry("City", "Canberra"));

        Topic topic = new Topic(id, entries);

        assertThat(topic.getId()).isEqualTo(id);
        assertThat(topic.getDepth()).isEqualTo(3);
        assertThat(topic.getName()).isEqualTo("Australia.ACT.Canberra");
        assertThat(topic.getFriendlyName()).isEqualTo("Canberra");
        assertThat(topic.getRelativeName()).isEqualTo("City=Canberra");
        assertThat(topic.toCanonicalString()).isEqualTo("Country=Australia.StateTerritory=ACT.City=Canberra");
        assertThat(topic.getShortId()).isEqualTo(id.toString().substring(0, 8));
        assertThat(topic.toFormattedString()).contains(topic.getName());
    }

    @Test
    @DisplayName("Topic construction strictly rejects gaps in sequence numbers")
    void testRejectionOfSequenceGaps() {
        UUID id = UUID.randomUUID();
        SortedMap<Integer, TopicEntry> gapEntries = new TreeMap<>();
        gapEntries.put(0, new TopicEntry("Country", "Australia"));
        gapEntries.put(2, new TopicEntry("City", "Canberra")); // Gap at index 1

        assertThatThrownBy(() -> new Topic(id, gapEntries))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("gap at position: 1");
    }

    @Test
    @DisplayName("Topic construction strictly rejects null ID or empty canonicalName")
    void testRejectionOfNullIdOrEmptyCanonicalName() {
        SortedMap<Integer, TopicEntry> validEntries = new TreeMap<>();
        validEntries.put(0, new TopicEntry("A", "B"));

        assertThatThrownBy(() -> new Topic(null, validEntries))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Topic id must not be null");

        assertThatThrownBy(() -> new Topic(UUID.randomUUID(), new TreeMap<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Topic canonicalName must not be null or empty");
    }

    @Test
    @DisplayName("Topic is immutable; canonicalName map cannot be modified")
    void testTopicImmutability() {
        Topic topic = TopicFactory.fromName("Health.HL7.2.4.ADT.A01");
        assertThatThrownBy(() -> topic.getCanonicalName().put(5, new TopicEntry("Ext", "Extra")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Standard 5-tuple Topic convenience accessors")
    void test5TupleTopicAccessors() {
        Topic topic = new Topic("Health", "HL7", "2.4", "ADT", "A01");
        assertThat(topic.getDomain()).isEqualTo("Health");
        assertThat(topic.getModel()).isEqualTo("HL7");
        assertThat(topic.getModelVersion()).isEqualTo("2.4");
        assertThat(topic.getDataElement()).isEqualTo("ADT");
        assertThat(topic.getDataElementQualifier()).isEqualTo("A01");
        assertThat(topic.getCompositeTrigger()).isEqualTo("ADT^A01");
        assertThat(topic.getName()).isEqualTo("Health.HL7.2.4.ADT.A01");
    }

    @Test
    @DisplayName("JSON serialization and deserialization roundtrip")
    void testJsonRoundtrip() throws Exception {
        UUID id = UUID.randomUUID();
        Topic topic = new TopicBuilder(id)
                .addEntry("Domain", "Health")
                .addEntry("Model", "HL7")
                .addEntry("ModelVersion", "2.4")
                .addEntry("DataElement", "ADT")
                .addEntry("DataElementQualifier", "A01")
                .build();

        String json = objectMapper.writeValueAsString(topic);
        Topic deserialized = objectMapper.readValue(json, Topic.class);

        assertThat(deserialized.getId()).isEqualTo(id);
        assertThat(deserialized.getName()).isEqualTo("Health.HL7.2.4.ADT.A01");
        assertThat(deserialized.getDomain()).isEqualTo("Health");
        assertThat(deserialized.getModel()).isEqualTo("HL7");
        assertThat(deserialized).isEqualTo(topic);
    }
}
