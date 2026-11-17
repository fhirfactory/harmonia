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

import net.fhirfactory.harmonia.model.core.naming.factory.TopicMaskBuilder;

import net.fhirfactory.harmonia.model.core.naming.factory.TopicMaskFactory;

import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.core.naming.valuesets.MaskType;
import net.fhirfactory.harmonia.model.core.naming.valuesets.TopicMaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TopicMask Canonical Filtering Tests")
public class TopicMaskTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Name-based mask with inclusive depth matching")
    void testNameBasedInclusiveMask() {
        TopicMask mask = new TopicMaskBuilder()
                .inclusive()
                .addNamePattern("Health")
                .addNamePattern("HL7")
                .addNamePattern("2\\..*")
                .build();

        assertThat(mask.getType()).isEqualTo(TopicMaskType.TopicNameBased);
        assertThat(mask.getMaskType()).isEqualTo(MaskType.Inclusive);

        Topic candidate = TopicFactory.fromName("Health.HL7.2.4.ADT.A01");
        assertThat(mask.matches(candidate)).isTrue();

        Topic nonMatching = TopicFactory.fromName("Finance.HL7.2.4.ADT.A01");
        assertThat(mask.matches(nonMatching)).isFalse();
    }

    @Test
    @DisplayName("Name-based mask with exact depth matching")
    void testNameBasedExactMask() {
        TopicMask mask = new TopicMaskBuilder()
                .exact()
                .addNamePattern("Health")
                .addNamePattern("HL7")
                .addNamePattern("2\\.4")
                .build();

        Topic depth3 = TopicFactory.fromName("Health.HL7.2.4");
        assertThat(mask.matches(depth3)).isTrue();

        Topic depth5 = TopicFactory.fromName("Health.HL7.2.4.ADT.A01");
        assertThat(mask.matches(depth5)).isFalse();
    }

    @Test
    @DisplayName("Canonical-based mask matches qualifier and name independently")
    void testCanonicalBasedMask() {
        TopicMask mask = new TopicMaskBuilder()
                .inclusive()
                .addCanonicalPattern("Domain", "Health")
                .addCanonicalPattern("Model", "FHIR")
                .addCanonicalPattern(".*", ".*")
                .addCanonicalPattern("DataElement", "Practitioner")
                .build();

        assertThat(mask.getType()).isEqualTo(TopicMaskType.TopicCanonicalNameBased);

        Topic candidate = TopicFactory.fromFhir("Practitioner", "CREATE");
        assertThat(mask.matches(candidate)).isTrue();

        Topic nonMatching = TopicFactory.fromFhir("Patient", "CREATE");
        assertThat(mask.matches(nonMatching)).isFalse();
    }

    @Test
    @DisplayName("Should reject mixing name and canonical pattern bases")
    void testRejectMixingBases() {
        assertThatThrownBy(() -> new TopicMaskBuilder()
                .addNamePattern("Health")
                .addCanonicalPattern("Model", "HL7"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Unrestricted mask matches any Topic")
    void testUnrestrictedMask() {
        TopicMask unrestricted = TopicMaskFactory.unrestricted();
        assertThat(unrestricted.matches(TopicFactory.fromName("Health.HL7.2.4.ADT.A01"))).isTrue();
        assertThat(unrestricted.matches(TopicFactory.fromFhir("Practitioner", "READ"))).isTrue();
    }

    @Test
    @DisplayName("JSON roundtrip serialization")
    void testJsonRoundtrip() throws Exception {
        TopicMask mask = TopicMaskFactory.fromNamePattern("Health.HL7.2.4.*", MaskType.Inclusive);

        String json = objectMapper.writeValueAsString(mask);
        TopicMask deserialized = objectMapper.readValue(json, TopicMask.class);

        assertThat(deserialized.getId()).isEqualTo(mask.getId());
        assertThat(deserialized.getType()).isEqualTo(mask.getType());
        assertThat(deserialized.getMaskType()).isEqualTo(mask.getMaskType());
        assertThat(deserialized.matches(TopicFactory.fromName("Health.HL7.2.4.ADT.A01"))).isTrue();
    }
}
