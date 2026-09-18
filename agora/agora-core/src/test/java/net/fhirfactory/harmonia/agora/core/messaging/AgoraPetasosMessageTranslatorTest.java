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

package net.fhirfactory.harmonia.agora.core.messaging;

import net.fhirfactory.harmonia.agora.api.model.AgoraCollaborationEvent;
import net.fhirfactory.harmonia.agora.api.model.AgoraRoomType;
import net.fhirfactory.harmonia.agora.api.topic.AgoraTopics;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AgoraPetasosMessageTranslator Tests")
@Timeout(10)
class AgoraPetasosMessageTranslatorTest {

    @Test
    @DisplayName("Should translate AgoraCollaborationEvent to PetasosMessage envelope and back")
    void testRoundTripTranslation() {
        ThemisSecurityContext secContext = ThemisSecurityContext.fromPrincipal(
                ThemisPrincipal.human("user:dr-alice"), "corr-transl-1");

        AgoraCollaborationEvent event = AgoraCollaborationEvent.builder()
                .eventId("evt-transl-001")
                .eventType("TASK_NOTE")
                .roomId("!room789:synapse")
                .roomType(AgoraRoomType.TASKS)
                .sender("@_harmonia_p_user:synapse")
                .harmoniaResourceType("Task")
                .harmoniaResourceId("task-456")
                .content("Lab review complete")
                .timestamp(Instant.now())
                .correlationId("corr-transl-1")
                .causationId("cause-transl-1")
                .securityContext(secContext)
                .metadata(Map.of("facility", "Hospital-A"))
                .build();

        PetasosDestination destination = PetasosDestination.queue(AgoraTopics.QUEUE_AGORA_INBOUND);
        PetasosMessage petasosMessage = AgoraPetasosMessageTranslator.toPetasosMessage(event, destination);

        assertThat(petasosMessage).isNotNull();
        assertThat(petasosMessage.getMessageId()).isEqualTo("evt-transl-001");
        assertThat(petasosMessage.getCorrelationId()).isEqualTo("corr-transl-1");
        assertThat(petasosMessage.getCausationId()).isEqualTo("cause-transl-1");
        assertThat(petasosMessage.getMessageType()).isEqualTo("TASK_NOTE");
        assertThat(petasosMessage.getSource()).isEqualTo("harmonia-agora");
        assertThat(petasosMessage.getDestination()).isEqualTo(destination);
        assertThat(petasosMessage.isDurable()).isTrue();
        assertThat(petasosMessage.getMetadata()).containsEntry("roomId", "!room789:synapse");
        assertThat(petasosMessage.getMetadata()).containsEntry("harmoniaResourceType", "Task");
        assertThat(petasosMessage.getMetadata()).containsEntry("harmoniaResourceId", "task-456");

        AgoraCollaborationEvent reconstructed = AgoraPetasosMessageTranslator.fromPetasosMessage(petasosMessage);
        assertThat(reconstructed).isNotNull();
        assertThat(reconstructed.getEventId()).isEqualTo(event.getEventId());
        assertThat(reconstructed.getEventType()).isEqualTo(event.getEventType());
        assertThat(reconstructed.getRoomId()).isEqualTo(event.getRoomId());
        assertThat(reconstructed.getRoomType()).isEqualTo(event.getRoomType());
        assertThat(reconstructed.getSender()).isEqualTo(event.getSender());
        assertThat(reconstructed.getHarmoniaResourceId()).isEqualTo(event.getHarmoniaResourceId());
        assertThat(reconstructed.getHarmoniaResourceType()).isEqualTo(event.getHarmoniaResourceType());
        assertThat(reconstructed.getContent()).isEqualTo(event.getContent());
        assertThat(reconstructed.getCorrelationId()).isEqualTo(event.getCorrelationId());
        assertThat(reconstructed.getCausationId()).isEqualTo(event.getCausationId());
    }

    @Test
    @DisplayName("Should reject null event or empty payload")
    void testInvalidPayloadHandling() {
        assertThatThrownBy(() -> AgoraPetasosMessageTranslator.toPetasosMessage(null, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> AgoraPetasosMessageTranslator.fromPetasosMessage(null))
                .isInstanceOf(IllegalArgumentException.class);

        PetasosMessage emptyMsg = PetasosMessage.builder().build();
        assertThatThrownBy(() -> AgoraPetasosMessageTranslator.fromPetasosMessage(emptyMsg))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
