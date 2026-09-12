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

package net.fhirfactory.hie.model.sequence;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.hie.model.topic.Topic;
import net.fhirfactory.hie.model.topic.TopicSubscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskSequenceDefinitionTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testConstructorsAndDefaults() {
        TaskSequenceDefinition def = new TaskSequenceDefinition();
        assertThat(def.getVersion()).isEqualTo("1.0.0");
        assertThat(def.isEnabled()).isTrue();
        assertThat(def.validate()).isTrue();

        TaskSequenceDefinition def2 = new TaskSequenceDefinition("seq-test-1", "Test Sequence");
        assertThat(def2.getSequenceId()).isEqualTo("seq-test-1");
        assertThat(def2.getSequenceName()).isEqualTo("Test Sequence");
        assertThat(def2.validate()).isTrue();
    }

    @Test
    void testCopyConstructor() {
        TaskSequenceDefinition source = new TaskSequenceDefinition("seq-src", "Source Sequence");
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

        TaskSequenceDefinition copy = new TaskSequenceDefinition(source);
        assertThat(copy.getSequenceId()).isEqualTo("seq-src");
        assertThat(copy.getSequenceName()).isEqualTo("Source Sequence");
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
        TaskSequenceDefinition def = new TaskSequenceDefinition("seq-match", "Match Sequence");
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
        TaskSequenceDefinition def = new TaskSequenceDefinition("seq-json", "JSON Sequence");
        def.setSequenceDescription("Test JSON description");
        def.addTopicSubscription(TopicSubscription.forHl7Gateway("gw-1", "ADT", "A01"));

        Map<Integer, String> activityIds = new TreeMap<>();
        activityIds.put(0, "message-queue-to-exchange");
        activityIds.put(1, "patient-identity-update");
        activityIds.put(2, "patient-demographics-update");
        def.setActivityIds(activityIds);

        String json = objectMapper.writeValueAsString(def);
        TaskSequenceDefinition deserialized = objectMapper.readValue(json, TaskSequenceDefinition.class);

        assertThat(deserialized.getSequenceId()).isEqualTo("seq-json");
        assertThat(deserialized.getSequenceName()).isEqualTo("JSON Sequence");
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

        TaskSequenceDefinition def = objectMapper.readValue(json, TaskSequenceDefinition.class);
        assertThat(def.getSequenceId()).isEqualTo("seq-array-format");
        assertThat(def.getActivityIds())
                .containsEntry(0, "message-queue-to-exchange")
                .containsEntry(1, "patient-identity-update");
        assertThat(def.getEffectiveDescription()).isEqualTo("Legacy array format test");
    }
}
