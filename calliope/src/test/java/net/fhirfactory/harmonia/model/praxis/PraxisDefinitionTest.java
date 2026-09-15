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

package net.fhirfactory.harmonia.model.praxis;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.model.topic.TopicSubscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

public class PraxisDefinitionTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testConstructorsAndDefaults() {
        PraxisDefinition def = new PraxisDefinition();
        assertThat(def.getVersion()).isEqualTo("1.0.0");
        assertThat(def.isEnabled()).isTrue();
        assertThat(def.validate()).isTrue();
        assertThat(def.generatePraxisRouteId()).isNotBlank();
        assertThat(def.generateSequenceRouteId()).isEqualTo(def.generatePraxisRouteId());

        PraxisDefinition def2 = new PraxisDefinition("seq-test-1", "Test Sequence");
        assertThat(def2.getPraxisId()).isEqualTo("seq-test-1");
        assertThat(def2.getPraxisName()).isEqualTo("Test Sequence");
        assertThat(def2.getSequenceId()).isEqualTo("seq-test-1");
        assertThat(def2.getSequenceName()).isEqualTo("Test Sequence");
        assertThat(def2.validate()).isTrue();
        assertThat(def2.generatePraxisRouteId()).isEqualTo("seq-test-1");
        assertThat(def2.toString()).contains("praxisId='seq-test-1'");

        def2.setSequenceId("seq-test-alt");
        def2.setSequenceName("Test Sequence Alt");
        assertThat(def2.getPraxisId()).isEqualTo("seq-test-alt");
        assertThat(def2.getPraxisName()).isEqualTo("Test Sequence Alt");
    }

    @Test
    void testCopyConstructor() {
        PraxisDefinition source = new PraxisDefinition("seq-src", "Source Sequence");
        source.setSequenceDescription("Source Description");
        source.setVersion("2.0.0");
        source.setEnabled(false);
        source.setTopicSubscriptions(List.of(TopicSubscription.forHl7Gateway("gw-1", "ADT", "A01")));
        source.setInputEndpoint("direct:in");
        source.setOutputEndpoint("direct:out");
        source.setErrorEndpoint("direct:err");
        source.setSourceQueueName("task.event.queue.gw-1");

        Map<Integer, String> activityIds = new TreeMap<>();
        activityIds.put(0, "activity-1");
        activityIds.put(1, "activity-2");
        source.setActivityIds(activityIds);

        PraxisDefinition copy = new PraxisDefinition(source);
        assertThat(copy.getPraxisId()).isEqualTo("seq-src");
        assertThat(copy.getPraxisName()).isEqualTo("Source Sequence");
        assertThat(copy.getSequenceDescription()).isEqualTo("Source Description");
        assertThat(copy.getEffectiveDescription()).isEqualTo("Source Description");
        assertThat(copy.getVersion()).isEqualTo("2.0.0");
        assertThat(copy.isEnabled()).isFalse();
        assertThat(copy.getTopicSubscriptions()).hasSize(1);
        assertThat(copy.getInputEndpoint()).isEqualTo("direct:in");
        assertThat(copy.getOutputEndpoint()).isEqualTo("direct:out");
        assertThat(copy.getErrorEndpoint()).isEqualTo("direct:err");
        assertThat(copy.getSourceQueueName()).isEqualTo("task.event.queue.gw-1");
        assertThat(copy.getActivityIds()).containsEntry(0, "activity-1").containsEntry(1, "activity-2");
        assertThat(copy.getActivityIdList()).containsExactly("activity-1", "activity-2");
    }

    @Test
    void testMatchingLogicWithTopicSubscriptions() {
        PraxisDefinition def = new PraxisDefinition("seq-match", "Match Sequence");
        def.addTopicSubscription(TopicSubscription.forHl7Gateway("pas-gw", "ADT", "A01"));
        def.addTopicSubscription(TopicSubscription.forHl7Gateway("pas-gw", "ADT", "A08"));

        Topic topicA01 = Topic.fromHl7("ADT", "A01", "pas-gw");
        Topic topicA08 = Topic.fromHl7("ADT", "A08", "pas-gw");
        Topic topicA03 = Topic.fromHl7("ADT", "A03", "pas-gw");
        Topic limsTopic = Topic.fromHl7("ADT", "A01", "lims-gw");

        assertThat(def.matches(topicA01)).isTrue();
        assertThat(def.matches(topicA08)).isTrue();
        assertThat(def.matches(topicA03)).isFalse();
        assertThat(def.matches(limsTopic)).isFalse();

        // Overload with string params
        assertThat(def.matches("pas-gw", "A01")).isTrue();
        assertThat(def.matches("pas-gw", "ADT^A08")).isTrue();
        assertThat(def.matches("lims-gw", "A01")).isFalse();
    }

    @Test
    void testJsonSerializationRoundtrip() throws Exception {
        PraxisDefinition def = new PraxisDefinition("seq-json", "JSON Sequence");
        def.setSequenceDescription("Test JSON description");
        def.addTopicSubscription(TopicSubscription.forHl7Gateway("gw-1", "ADT", "A01"));

        Map<Integer, String> activityIds = new TreeMap<>();
        activityIds.put(0, "message-queue-to-exchange");
        activityIds.put(1, "patient-identity-update");
        activityIds.put(2, "patient-demographics-update");
        def.setActivityIds(activityIds);

        String json = objectMapper.writeValueAsString(def);
        PraxisDefinition deserialized = objectMapper.readValue(json, PraxisDefinition.class);

        assertThat(deserialized.getPraxisId()).isEqualTo("seq-json");
        assertThat(deserialized.getPraxisName()).isEqualTo("JSON Sequence");
        assertThat(deserialized.getSequenceDescription()).isEqualTo("Test JSON description");
        assertThat(deserialized.getTopicSubscriptions()).hasSize(1);
        assertThat(deserialized.getActivityIds())
                .containsEntry(0, "message-queue-to-exchange")
                .containsEntry(1, "patient-identity-update")
                .containsEntry(2, "patient-demographics-update");
    }

    @Test
    void testJsonDeserializationFromArrayFormat() throws Exception {
        String json = """
        {
          "sequenceId": "seq-array-format",
          "sequenceName": "Array Format Sequence",
          "description": "Legacy array format test",
          "enabled": true,
          "targetGatewayInstances": ["*"],
          "targetTriggerTypes": ["*"],
          "activityIds": ["message-queue-to-exchange", "patient-identity-update"]
        }
        """;

        PraxisDefinition def = objectMapper.readValue(json, PraxisDefinition.class);
        assertThat(def.getPraxisId()).isEqualTo("seq-array-format");
        assertThat(def.getActivityIds())
                .containsEntry(0, "message-queue-to-exchange")
                .containsEntry(1, "patient-identity-update");
        assertThat(def.getEffectiveDescription()).isEqualTo("Legacy array format test");
    }
}
