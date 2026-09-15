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

package net.fhirfactory.harmonia.model.topic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicSubscriptionTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testWildcardSubscriptionMatchesAnyTopic() {
        TopicSubscription sub = TopicSubscription.forAll();
        Topic topic = Topic.fromHl7("ADT", "A01", "pas-gw");

        assertThat(sub.matches(topic)).isTrue();
    }

    @Test
    void testHierarchicalContainmentMatching() {
        Topic topicA01 = Topic.fromHl7("ADT", "A01", "pas-gw");
        Topic topicA08 = Topic.fromHl7("ADT", "A08", "pas-gw");
        Topic topicR01 = Topic.fromHl7("ORU", "R01", "lims-gw");

        // 1. Subscription matching all ADT under Health -> HL7 -> 2.4
        TopicSubscription adtSub = new TopicSubscription("Health", "HL7", "2.4", "ADT", "*");
        assertThat(adtSub.matches(topicA01)).isTrue();
        assertThat(adtSub.matches(topicA08)).isTrue();
        assertThat(adtSub.matches(topicR01)).isFalse();

        // 2. Subscription matching specific qualifier A01
        TopicSubscription a01Sub = new TopicSubscription("Health", "HL7", "2.4", "ADT", "A01");
        assertThat(a01Sub.matches(topicA01)).isTrue();
        assertThat(a01Sub.matches(topicA08)).isFalse();

        // 3. Subscription matching all HL7 (Model level containment)
        TopicSubscription allHl7 = new TopicSubscription("Health", "HL7", "*", "*", "*");
        assertThat(allHl7.matches(topicA01)).isTrue();
        assertThat(allHl7.matches(topicA08)).isTrue();
        assertThat(allHl7.matches(topicR01)).isTrue();

        // 4. Subscription with different domain
        TopicSubscription financeSub = new TopicSubscription("Finance", "*", "*", "*", "*");
        assertThat(financeSub.matches(topicA01)).isFalse();
    }

    @Test
    void testSourceAndRoutingMatching() {
        Topic pasTopic = Topic.fromHl7("ADT", "A01", "pas-gw");
        Topic limsTopic = Topic.fromHl7("ORU", "R01", "lims-gw");

        TopicSubscription pasSub = TopicSubscription.forHl7Gateway("pas-gw", "ADT", "*");
        assertThat(pasSub.matches(pasTopic)).isTrue();
        assertThat(pasSub.matches(limsTopic)).isFalse();

        // Multi-source comma-separated matching
        TopicSubscription multiGwSub = TopicSubscription.forHl7Gateway("pas-gw, lims-gw", "*", "*");
        assertThat(multiGwSub.matches(pasTopic)).isTrue();
        assertThat(multiGwSub.matches(limsTopic)).isTrue();
    }

    @Test
    void testJsonRoundtrip() throws Exception {
        TopicSubscription sub = TopicSubscription.forHl7Gateway("pas-gw", "ADT", "A01,A08");
        String json = objectMapper.writeValueAsString(sub);
        TopicSubscription deserialized = objectMapper.readValue(json, TopicSubscription.class);

        assertThat(deserialized.getDomain()).isEqualTo("Health");
        assertThat(deserialized.getModel()).isEqualTo("HL7");
        assertThat(deserialized.getDataElement()).isEqualTo("ADT");
        assertThat(deserialized.getDataElementQualifier()).isEqualTo("A01,A08");
        assertThat(deserialized.getSource()).isEqualTo("pas-gw");

        Topic matchingTopic = Topic.fromHl7("ADT", "A08", "pas-gw");
        assertThat(deserialized.matches(matchingTopic)).isTrue();
    }
}
