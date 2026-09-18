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

package net.fhirfactory.harmonia.agora.api;

import net.fhirfactory.harmonia.agora.api.topic.AgoraTopics;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AgoraTopics API Unit Tests")
@Timeout(10)
class AgoraTopicConstantsTest {

    @Test
    @DisplayName("Should verify canonical queue and topic names")
    void testQueueNames() {
        assertThat(AgoraTopics.QUEUE_AGORA_INBOUND).isEqualTo("petasos.queue.agora.inbound");
        assertThat(AgoraTopics.QUEUE_AGORA_OUTBOUND).isEqualTo("petasos.queue.agora.outbound");
        assertThat(AgoraTopics.TOPIC_AGORA_EVENTS).isEqualTo("petasos.topic.agora.events");
        assertThat(AgoraTopics.QUEUE_AGORA_AUDIT).isEqualTo("petasos.queue.agora.audit");
    }

    @Test
    @DisplayName("Should create Calliope Topic descriptor conforming to platform taxonomy")
    void testCreateTopic() {
        Topic topic = AgoraTopics.createTopic("Communication", "Inbound", "harmonia-core");
        assertThat(topic).isNotNull();
        assertThat(topic.getDomain()).isEqualTo("Health");
        assertThat(topic.getModel()).isEqualTo("FHIR");
        assertThat(topic.getModelVersion()).isEqualTo("R5");
        assertThat(topic.getDataElement()).isEqualTo("Communication");
        assertThat(topic.getDataElementQualifier()).isEqualTo("Inbound");
        assertThat(topic.getSource()).isEqualTo("harmonia-agora");
        assertThat(topic.getDestination()).isEqualTo("harmonia-core");
        assertThat(topic.getReceivedDate()).isNotNull();

        Topic inbound = AgoraTopics.inboundTopic();
        assertThat(inbound.getDataElementQualifier()).isEqualTo("Inbound");

        Topic outbound = AgoraTopics.outboundTopic();
        assertThat(outbound.getDataElementQualifier()).isEqualTo("Outbound");
    }
}
