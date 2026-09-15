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

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testConstructorsAndDefaults() {
        Topic topic = new Topic();
        assertThat(topic.getDomain()).isEqualTo(Topic.DOMAIN_HEALTH);
        assertThat(topic.getModel()).isEqualTo(Topic.MODEL_HL7);
        assertThat(topic.getModelVersion()).isEqualTo(Topic.DEFAULT_HL7_VERSION);
        assertThat(topic.getReceivedDate()).isNotNull();

        Topic topic2 = new Topic("Health", "HL7", "2.4", "ADT", "A01");
        assertThat(topic2.getDataElement()).isEqualTo("ADT");
        assertThat(topic2.getDataElementQualifier()).isEqualTo("A01");
        assertThat(topic2.getCompositeTrigger()).isEqualTo("ADT^A01");
        assertThat(topic2.toTopicString()).isEqualTo("Health.HL7.2.4.ADT.A01");
    }

    @Test
    void testFromHl7Factories() {
        Topic topic = Topic.fromHl7("ADT", "A08", "pas-gw");
        assertThat(topic.getDomain()).isEqualTo("Health");
        assertThat(topic.getModel()).isEqualTo("HL7");
        assertThat(topic.getModelVersion()).isEqualTo("2.4");
        assertThat(topic.getDataElement()).isEqualTo("ADT");
        assertThat(topic.getDataElementQualifier()).isEqualTo("A08");
        assertThat(topic.getSource()).isEqualTo("pas-gw");

        Topic fromComposite = Topic.fromHl7("ORU^R01", "lims-gw");
        assertThat(fromComposite.getDataElement()).isEqualTo("ORU");
        assertThat(fromComposite.getDataElementQualifier()).isEqualTo("R01");
        assertThat(fromComposite.getSource()).isEqualTo("lims-gw");

        Topic egressTopic = Topic.forEgress("ADT", "A01", "harmonia", "mllp-out", "HIS_NORTH");
        assertThat(egressTopic.getDomain()).isEqualTo("Health");
        assertThat(egressTopic.getModel()).isEqualTo("HL7");
        assertThat(egressTopic.getDataElement()).isEqualTo("ADT");
        assertThat(egressTopic.getDataElementQualifier()).isEqualTo("A01");
        assertThat(egressTopic.getSource()).isEqualTo("harmonia");
        assertThat(egressTopic.getTarget()).isEqualTo("mllp-out");
        assertThat(egressTopic.getDestination()).isEqualTo("HIS_NORTH");
    }

    @Test
    void testCopyConstructor() {
        Date now = new Date();
        Topic original = new Topic("Health", "HL7", "2.4", "ADT", "A01", "pas-gw", "hie-core", "APP_A", "HIE", now);
        Topic copy = new Topic(original);

        assertThat(copy).isEqualTo(original);
        assertThat(copy.getSource()).isEqualTo("pas-gw");
        assertThat(copy.getOrigin()).isEqualTo("APP_A");
        assertThat(copy.getDestination()).isEqualTo("HIE");
    }

    @Test
    void testJsonRoundtrip() throws Exception {
        Topic topic = Topic.fromHl7("2.4", "ADT", "A01", "pas-gw", "target-sys", "origin-app", "dest-app");
        String json = objectMapper.writeValueAsString(topic);
        Topic deserialized = objectMapper.readValue(json, Topic.class);

        assertThat(deserialized.getDomain()).isEqualTo("Health");
        assertThat(deserialized.getModel()).isEqualTo("HL7");
        assertThat(deserialized.getModelVersion()).isEqualTo("2.4");
        assertThat(deserialized.getDataElement()).isEqualTo("ADT");
        assertThat(deserialized.getDataElementQualifier()).isEqualTo("A01");
        assertThat(deserialized.getSource()).isEqualTo("pas-gw");
        assertThat(deserialized.getOrigin()).isEqualTo("origin-app");
        assertThat(deserialized.getDestination()).isEqualTo("dest-app");
    }
}
