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

package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("InformationItinerary Strongly Typed Canonical Topic Reference Tests")
public class InformationItineraryTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should construct InformationItinerary with strongly typed Topic references and preserve canonical identity")
    void testItineraryConstructionAndIdentityPreservation() {
        Topic srcTopic = TopicFactory.fromName("Health.Gateway.MllpIn");
        Topic createTopic = TopicFactory.fromName("Health.Ergon.Ingress");
        Topic prevTopic = TopicFactory.fromName("Health.Ergon.Validation");
        Topic nextTopic = TopicFactory.fromName("Health.Ergon.Transform");
        Topic exitTopic = TopicFactory.fromName("Health.Gateway.MllpOut");
        Topic part1 = TopicFactory.fromName("Health.Ergon.Audit");
        Topic part2 = TopicFactory.fromName("Health.Ergon.Provenance");

        InformationItinerary itinerary = InformationItinerary.builder()
                .sourceEntryComponent(srcTopic)
                .creationComponent(createTopic)
                .previousComponent(prevTopic)
                .nextComponent(nextTopic)
                .targetExitComponent(exitTopic)
                .addParticipatingComponent(part1)
                .addParticipatingComponent(part2)
                .build();

        // Strong Topic typing
        assertThat(itinerary.sourceEntryComponent()).contains(srcTopic);
        assertThat(itinerary.creationComponent()).contains(createTopic);
        assertThat(itinerary.previousComponent()).contains(prevTopic);
        assertThat(itinerary.nextComponent()).contains(nextTopic);
        assertThat(itinerary.targetExitComponent()).contains(exitTopic);
        assertThat(itinerary.participatingComponents()).containsExactly(part1, part2);

        // Complete canonical path identity is retained independently of the unqualified name projection.
        assertThat(itinerary.sourceEntryComponentCanonicalName()).contains(srcTopic.getCanonicalName());
        assertThat(itinerary.creationComponentCanonicalName()).contains(createTopic.getCanonicalName());
        assertThat(itinerary.previousComponentCanonicalName()).contains(prevTopic.getCanonicalName());
        assertThat(itinerary.nextComponentCanonicalName()).contains(nextTopic.getCanonicalName());
        assertThat(itinerary.targetExitComponentCanonicalName()).contains(exitTopic.getCanonicalName());

        // Derived Name Helpers
        assertThat(itinerary.sourceEntryComponentName()).contains("Health.Gateway.MllpIn");
        assertThat(itinerary.creationComponentName()).contains("Health.Ergon.Ingress");
        assertThat(itinerary.previousComponentName()).contains("Health.Ergon.Validation");
        assertThat(itinerary.nextComponentName()).contains("Health.Ergon.Transform");
        assertThat(itinerary.targetExitComponentName()).contains("Health.Gateway.MllpOut");
    }

    @Test
    @DisplayName("Should reject null elements in participatingComponents list rather than silently discarding")
    void testRejectionOfNullParticipatingComponents() {
        List<Topic> listWithNull = new ArrayList<>();
        listWithNull.add(TopicFactory.fromName("Health.Component.A"));
        listWithNull.add(null);

        assertThatThrownBy(() -> InformationItinerary.builder().participatingComponents(listWithNull).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("participatingComponents must not contain null elements");

        assertThatThrownBy(() -> new InformationItinerary(null, null, null, null, null, listWithNull))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("participatingComponents must not contain null elements");
    }

    @Test
    @DisplayName("Should serialize and deserialize InformationItinerary preserving full Topic references")
    void testItineraryJsonRoundtrip() throws Exception {
        Topic srcTopic = TopicFactory.fromName("Health.Gateway.MllpIn");
        Topic exitTopic = TopicFactory.fromName("Health.Gateway.MllpOut");

        InformationItinerary itinerary = InformationItinerary.builder()
                .sourceEntryComponent(srcTopic)
                .targetExitComponent(exitTopic)
                .build();

        String json = objectMapper.writeValueAsString(itinerary);
        InformationItinerary deserialized = objectMapper.readValue(json, InformationItinerary.class);

        assertThat(deserialized.sourceEntryComponent()).isEqualTo(itinerary.sourceEntryComponent());
        assertThat(deserialized.targetExitComponent()).isEqualTo(itinerary.targetExitComponent());
        assertThat(deserialized.sourceEntryComponentCanonicalName()).contains(srcTopic.getCanonicalName());
        assertThat(deserialized.targetExitComponentCanonicalName()).contains(exitTopic.getCanonicalName());
        assertThat(deserialized.creationComponent()).isEmpty();
    }
}
