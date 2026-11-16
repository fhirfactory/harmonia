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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TopicSubscription Filtering & Matching Tests")
public class TopicSubscriptionTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("forAll subscribes to any Topic")
    void testForAll() {
        TopicSubscription sub = TopicSubscription.forAll();
        assertThat(sub.matches(Topic.fromName("Health.HL7.2.4.ADT.A01"))).isTrue();
        assertThat(sub.matches(Topic.fromFhir("Practitioner", "CREATE"))).isTrue();
    }

    @Test
    @DisplayName("Hierarchical containment matching")
    void testHierarchicalMatching() {
        TopicSubscription hl7Sub = TopicSubscription.forModel("Health", "HL7");

        Topic adt = Topic.fromHl7("ADT", "A01");
        assertThat(hl7Sub.matches(adt)).isTrue();

        Topic fhir = Topic.fromFhir("Patient", "CREATE");
        assertThat(hl7Sub.matches(fhir)).isFalse();

        TopicSubscription adtOnly = TopicSubscription.forHl7("ADT^A01");
        assertThat(adtOnly.matches(Topic.fromHl7("ADT", "A01"))).isTrue();
        assertThat(adtOnly.matches(Topic.fromHl7("ADT", "A08"))).isFalse();
    }

    @Test
    @DisplayName("FHIR resource and interaction matching")
    void testFhirMatching() {
        TopicSubscription practitionerSub = TopicSubscription.forFhir("Practitioner");
        assertThat(practitionerSub.matches(Topic.fromFhir("Practitioner", "CREATE"))).isTrue();
        assertThat(practitionerSub.matches(Topic.fromFhir("Practitioner", "UPDATE"))).isTrue();
        assertThat(practitionerSub.matches(Topic.fromFhir("Patient", "CREATE"))).isFalse();

        TopicSubscription createOnly = TopicSubscription.forFhir("Practitioner", "CREATE");
        assertThat(createOnly.matches(Topic.fromFhir("Practitioner", "CREATE"))).isTrue();
        assertThat(createOnly.matches(Topic.fromFhir("Practitioner", "UPDATE"))).isFalse();
    }

    @Test
    @DisplayName("JSON roundtrip serialization")
    void testJsonRoundtrip() throws Exception {
        TopicSubscription sub = TopicSubscription.forHl7("ADT^A01");

        String json = objectMapper.writeValueAsString(sub);
        TopicSubscription deserialized = objectMapper.readValue(json, TopicSubscription.class);

        assertThat(deserialized.getDomain()).isEqualTo(sub.getDomain());
        assertThat(deserialized.getModel()).isEqualTo(sub.getModel());
        assertThat(deserialized.getDataElement()).isEqualTo(sub.getDataElement());
        assertThat(deserialized.getDataElementQualifier()).isEqualTo(sub.getDataElementQualifier());
        assertThat(deserialized.matches(Topic.fromHl7("ADT", "A01"))).isTrue();
    }
}
