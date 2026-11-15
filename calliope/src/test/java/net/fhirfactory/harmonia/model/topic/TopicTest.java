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

package net.fhirfactory.harmonia.model.topic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Topic Canonical Model and Invariant Tests")
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
        SortedMap<Integer, Topic.Entry> entries = new TreeMap<>();
        entries.put(0, new Topic.Entry("Country", "Australia"));
        entries.put(1, new Topic.Entry("StateTerritory", "ACT"));
        entries.put(2, new Topic.Entry("City", "Canberra"));

        Topic topic = new Topic(id, entries);

        assertThat(topic.getId()).isEqualTo(id);
        assertThat(topic.getDepth()).isEqualTo(3);
        assertThat(topic.getName()).isEqualTo("Australia.ACT.Canberra");
        assertThat(topic.getFriendlyName()).isEqualTo("Canberra");
        assertThat(topic.getRelativeName()).isEqualTo("City=Canberra");
        assertThat(topic.toCanonicalString()).isEqualTo("[0: Country=Australia, 1: StateTerritory=ACT, 2: City=Canberra]");
        assertThat(topic.getShortId()).isEqualTo(id.toString().substring(0, 8));
        assertThat(topic.toFormattedString()).contains("id: " + id);
    }

    @Test
    @DisplayName("Topic builder creates immutable, correctly sequenced Topic")
    void testTopicBuilder() {
        UUID id = UUID.randomUUID();
        Topic topic = Topic.builder()
                .id(id)
                .addEntry("Country", "Australia")
                .addEntry("StateTerritory", "NSW")
                .addEntry("City", "Sydney")
                .build();

        assertThat(topic.getId()).isEqualTo(id);
        assertThat(topic.getName()).isEqualTo("Australia.NSW.Sydney");
        assertThat(topic.getFriendlyName()).isEqualTo("Sydney");
        assertThat(topic.getRelativeName()).isEqualTo("City=Sydney");
        assertThat(topic.getDepth()).isEqualTo(3);
        assertThat(topic.getEntry(0).getQualifier()).isEqualTo("Country");
        assertThat(topic.getEntry(1).getQualifier()).isEqualTo("StateTerritory");
        assertThat(topic.getEntry(2).getQualifier()).isEqualTo("City");
    }

    @Test
    @DisplayName("Topic normalises absent qualifiers to N0, N1, ... Nn by absolute position")
    void testDefaultQualifiersNormalization() {
        UUID id = UUID.randomUUID();
        SortedMap<Integer, Topic.Entry> entries = new TreeMap<>();
        entries.put(0, new Topic.Entry(null, "Health"));
        entries.put(1, new Topic.Entry("", "HL7"));
        entries.put(2, new Topic.Entry("   ", "2.4"));

        Topic topic = new Topic(id, entries);

        assertThat(topic.getEntry(0).getQualifier()).isEqualTo("N0");
        assertThat(topic.getEntry(1).getQualifier()).isEqualTo("N1");
        assertThat(topic.getEntry(2).getQualifier()).isEqualTo("N2");
        assertThat(topic.toCanonicalString()).isEqualTo("[0: N0=Health, 1: N1=HL7, 2: N2=2.4]");
        assertThat(topic.getName()).isEqualTo("Health.HL7.2.4");
        assertThat(topic.getFriendlyName()).isEqualTo("2.4");
        assertThat(topic.getRelativeName()).isEqualTo("N2=2.4");
    }

    @Test
    @DisplayName("Topic construction strictly rejects gaps in sequence numbers")
    void testRejectionOfSequenceGaps() {
        UUID id = UUID.randomUUID();
        SortedMap<Integer, Topic.Entry> gapEntries = new TreeMap<>();
        gapEntries.put(0, new Topic.Entry("Country", "Australia"));
        gapEntries.put(2, new Topic.Entry("City", "Canberra")); // Gap at 1

        assertThatThrownBy(() -> new Topic(id, gapEntries))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("gaps or invalid order");
    }

    @Test
    @DisplayName("Topic construction strictly rejects null ID or empty canonicalName")
    void testRejectionOfNullIdOrEmptyCanonicalName() {
        assertThatThrownBy(() -> new Topic(null, Map.of(0, new Topic.Entry("A", "B"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id (UUID) must not be null");

        assertThatThrownBy(() -> new Topic(UUID.randomUUID(), Map.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("canonicalName must not be null or empty");
    }

    @Test
    @DisplayName("Topic entry supports version strings with dots")
    void testTopicEntrySupportsVersionStringsWithDots() {
        Topic.Entry entry = new Topic.Entry("ModelVersion", "2.4");
        assertThat(entry.getName()).isEqualTo("2.4");
        assertThat(entry.getQualifier()).isEqualTo("ModelVersion");
    }

    @Test
    @DisplayName("Topic.fromName parses dot-delimited string and builds sequenced Topic")
    void testFromNameFactory() {
        UUID id = UUID.randomUUID();
        Topic topic = Topic.fromName(id, "Australia.ACT.Canberra");

        assertThat(topic.getId()).isEqualTo(id);
        assertThat(topic.getDepth()).isEqualTo(3);
        assertThat(topic.getName()).isEqualTo("Australia.ACT.Canberra");
        assertThat(topic.getEntry(0).getQualifier()).isEqualTo("N0");
        assertThat(topic.getEntry(0).getName()).isEqualTo("Australia");
        assertThat(topic.getEntry(1).getQualifier()).isEqualTo("N1");
        assertThat(topic.getEntry(1).getName()).isEqualTo("ACT");
        assertThat(topic.getEntry(2).getQualifier()).isEqualTo("N2");
        assertThat(topic.getEntry(2).getName()).isEqualTo("Canberra");
    }

    @Test
    @DisplayName("Topic.forInformationUnit constructs canonical IU Topic with 32-hex identifier")
    void testForInformationUnit() {
        UUID iuUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Topic iuTopic = Topic.forInformationUnit(iuUuid);

        assertThat(iuTopic.getName()).isEqualTo("harmonia.mneme.iu.123e4567e89b12d3a456426614174000");
        assertThat(iuTopic.getDepth()).isEqualTo(4);
        assertThat(iuTopic.getEntry(0).getName()).isEqualTo("harmonia");
        assertThat(iuTopic.getEntry(1).getName()).isEqualTo("mneme");
        assertThat(iuTopic.getEntry(2).getName()).isEqualTo("iu");
        assertThat(iuTopic.getEntry(3).getName()).isEqualTo("123e4567e89b12d3a456426614174000");
        assertThat(iuTopic.getFriendlyName()).isEqualTo("123e4567e89b12d3a456426614174000");
        assertThat(iuTopic.getRelativeName()).isEqualTo("Identifier=123e4567e89b12d3a456426614174000");
    }

    @Test
    @DisplayName("Topic is immutable; canonicalName map cannot be modified")
    void testTopicImmutability() {
        Topic topic = Topic.fromName("Health.HL7.2.4.ADT.A01");
        assertThatThrownBy(() -> topic.getCanonicalName().put(5, new Topic.Entry("Ext", "Extra")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Legacy factory helpers and routing backwards compatibility")
    void testLegacyFactoryHelpersAndBackwardsCompatibility() {
        Topic topic = Topic.fromHl7("ADT", "A08", "pas-gw");
        assertThat(topic.getDomain()).isEqualTo("Health");
        assertThat(topic.getModel()).isEqualTo("HL7");
        assertThat(topic.getModelVersion()).isEqualTo("2.4");
        assertThat(topic.getDataElement()).isEqualTo("ADT");
        assertThat(topic.getDataElementQualifier()).isEqualTo("A08");
        assertThat(topic.getSource()).isEqualTo("pas-gw");
        assertThat(topic.getCompositeTrigger()).isEqualTo("ADT^A08");
        assertThat(topic.toTopicString()).isEqualTo("Health.HL7.2.4.ADT.A08");

        Topic egressTopic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        assertThat(egressTopic.getSource()).isEqualTo("harmonia");
        assertThat(egressTopic.getTarget()).isEqualTo("mllp-out");
        assertThat(egressTopic.getDestination()).isEqualTo("HIS_NORTH");
    }

    @Test
    @DisplayName("JSON serialization and deserialization roundtrip")
    void testJsonRoundtrip() throws Exception {
        UUID id = UUID.randomUUID();
        Topic topic = Topic.builder()
                .id(id)
                .addEntry("Domain", "Health")
                .addEntry("Model", "HL7")
                .addEntry("ModelVersion", "2.4")
                .addEntry("DataElement", "ADT")
                .addEntry("DataElementQualifier", "A01")
                .source("pas-gw")
                .destination("hie-core")
                .build();

        String json = objectMapper.writeValueAsString(topic);
        Topic deserialized = objectMapper.readValue(json, Topic.class);

        assertThat(deserialized.getId()).isEqualTo(id);
        assertThat(deserialized.getName()).isEqualTo("Health.HL7.2.4.ADT.A01");
        assertThat(deserialized.getDomain()).isEqualTo("Health");
        assertThat(deserialized.getModel()).isEqualTo("HL7");
        assertThat(deserialized.getSource()).isEqualTo("pas-gw");
        assertThat(deserialized.getDestination()).isEqualTo("hie-core");
    }
}
