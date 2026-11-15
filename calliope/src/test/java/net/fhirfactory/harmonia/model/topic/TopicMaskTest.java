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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TopicMask Immutable Model and Matching Tests")
public class TopicMaskTest {

    @Test
    @DisplayName("TopicNameBased inclusive and exact matching semantics")
    void testTopicNameBasedExactAndInclusiveSemantics() {
        Topic twoLevel = Topic.fromName("Australia.ACT");
        Topic threeLevel = Topic.fromName("Australia.ACT.Canberra");
        Topic otherState = Topic.fromName("Australia.NSW.Sydney");

        // Inclusive Australia.ACT mask
        TopicMask inclusiveMask = TopicMask.builder()
                .topicNameBased()
                .inclusive()
                .addNamePattern("Australia")
                .addNamePattern("ACT")
                .build();

        assertThat(inclusiveMask.matches(twoLevel)).isTrue();
        assertThat(inclusiveMask.matches(threeLevel)).isTrue();
        assertThat(inclusiveMask.matches(otherState)).isFalse();

        // Exact Australia.ACT mask
        TopicMask exactMask = TopicMask.builder()
                .topicNameBased()
                .exact()
                .addNamePattern("Australia")
                .addNamePattern("ACT")
                .build();

        assertThat(exactMask.matches(twoLevel)).isTrue();
        assertThat(exactMask.matches(threeLevel)).isFalse(); // Exact rejects deeper depth
        assertThat(exactMask.matches(otherState)).isFalse();
    }

    @Test
    @DisplayName("TopicCanonicalNameBased matches qualifiers and names independently")
    void testTopicCanonicalNameBasedMatching() {
        Topic canberra = Topic.builder()
                .addEntry("Country", "Australia")
                .addEntry("StateTerritory", "ACT")
                .addEntry("City", "Canberra")
                .build();

        Topic sydney = Topic.builder()
                .addEntry("Country", "Australia")
                .addEntry("StateTerritory", "NSW")
                .addEntry("City", "Sydney")
                .build();

        // Match qualifier 'StateTerritory' with regex 'A.*' and name 'ACT'
        TopicMask mask = TopicMask.builder()
                .topicCanonicalNameBased()
                .inclusive()
                .addCanonicalPattern("Country", "Australia")
                .addCanonicalPattern("State.*", "A.*")
                .build();

        assertThat(mask.matches(canberra)).isTrue();
        assertThat(mask.matches(sydney)).isFalse();

        // Mismatched qualifier regex
        TopicMask wrongQualifierMask = TopicMask.builder()
                .topicCanonicalNameBased()
                .inclusive()
                .addCanonicalPattern("Province", "Australia")
                .build();

        assertThat(wrongQualifierMask.matches(canberra)).isFalse();
    }

    @Test
    @DisplayName("Element-wise regex matching and literal dot boundaries")
    void testElementWiseRegexIsolation() {
        TopicMask mask = TopicMask.builder()
                .topicNameBased()
                .exact()
                .addNamePattern("Health")
                .addNamePattern("HL7|FHIR")
                .addNamePattern("2\\..*|R5")
                .addNamePattern("ADT.*")
                .addNamePattern("A0[18]")
                .build();

        Topic adtA01 = Topic.fromHl7("ADT", "A01", null);
        Topic adtA08 = Topic.fromHl7("ADT", "A08", null);
        Topic adtA03 = Topic.fromHl7("ADT", "A03", null);
        Topic fhirPatient = Topic.fromFhir("Patient", "create", null);

        assertThat(mask.matches(adtA01)).isTrue();
        assertThat(mask.matches(adtA08)).isTrue();
        assertThat(mask.matches(adtA03)).isFalse();
        assertThat(mask.matches(fhirPatient)).isFalse();
    }

    @Test
    @DisplayName("Default unrestricted mask matches any Topic")
    void testUnrestrictedMask() {
        TopicMask unrestricted = TopicMask.unrestricted();

        assertThat(unrestricted.matches(Topic.fromName("Health.HL7.2.4.ADT.A01"))).isTrue();
        assertThat(unrestricted.matches(Topic.fromName("harmonia.mneme.iu.0123456789abcdef0123456789abcdef"))).isTrue();
        assertThat(unrestricted.matches(Topic.fromName("Single"))).isTrue();
        assertThat(unrestricted.matches((Topic) null)).isFalse();
    }

    @Test
    @DisplayName("fromPattern parses wildcard shorthand and exact mask syntax")
    void testFromPatternFactory() {
        TopicMask mask = TopicMask.fromPattern("Health.HL7.*");

        assertThat(mask.getType()).isEqualTo(TopicMaskType.TopicNameBased);
        assertThat(mask.getMaskType()).isEqualTo(MaskType.Inclusive);
        assertThat(mask.getDepth()).isEqualTo(2);

        assertThat(mask.matches(Topic.fromName("Health.HL7.2.4.ADT.A01"))).isTrue();
        assertThat(mask.matches(Topic.fromName("Health.FHIR.R5"))).isFalse();
    }

    @Test
    @DisplayName("TopicMask for InformationUnits matches IU topics and specific IU UUID")
    void testInformationUnitMasks() {
        UUID iu1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID iu2 = UUID.fromString("987fcdeb-51a2-43d7-b890-123456789abc");

        Topic topic1 = Topic.forInformationUnit(iu1);
        Topic topic2 = Topic.forInformationUnit(iu2);
        Topic other = Topic.fromName("Health.HL7.2.4.ADT.A01");

        TopicMask allIUs = TopicMask.forInformationUnits();
        assertThat(allIUs.matches(topic1)).isTrue();
        assertThat(allIUs.matches(topic2)).isTrue();
        assertThat(allIUs.matches(other)).isFalse();

        TopicMask specificIU = TopicMask.forInformationUnit(iu1);
        assertThat(specificIU.matches(topic1)).isTrue();
        assertThat(specificIU.matches(topic2)).isFalse();
    }

    @Test
    @DisplayName("Invalid regex syntax is rejected at construction time")
    void testInvalidRegexRejection() {
        assertThatThrownBy(() -> TopicMask.builder().addNamePattern("[invalid(").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid regular expression syntax");
    }

    @Test
    @DisplayName("TopicMask is immutable and provides diagnostic formatting")
    void testTopicMaskDiagnostics() {
        UUID id = UUID.randomUUID();
        TopicMask mask = TopicMask.builder()
                .id(id)
                .topicNameBased()
                .exact()
                .addNamePattern("Health")
                .addNamePattern("HL7")
                .build();

        assertThat(mask.getId()).isEqualTo(id);
        assertThat(mask.getShortId()).isEqualTo(id.toString().substring(0, 8));
        assertThat(mask.getMaskPattern()).isEqualTo("Health.HL7");
        assertThat(mask.toString()).contains("TopicMask{");
    }
}
