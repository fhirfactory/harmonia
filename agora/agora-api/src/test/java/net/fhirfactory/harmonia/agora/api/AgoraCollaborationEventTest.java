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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.agora.api.model.AgoraCollaborationEvent;
import net.fhirfactory.harmonia.agora.api.model.AgoraRoomType;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AgoraCollaborationEvent API Unit Tests")
@Timeout(10)
class AgoraCollaborationEventTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    @DisplayName("Should build event with complete fields and defaults")
    void testEventBuilder() {
        Instant now = Instant.now();
        ThemisSecurityContext secContext = ThemisSecurityContext.fromPrincipal(
                ThemisPrincipal.human("user:dr-smith"), "corr-12345");

        AgoraCollaborationEvent event = AgoraCollaborationEvent.builder()
                .eventId("event-001")
                .eventType("TASK_NOTE")
                .roomId("!room123:synapse")
                .roomType(AgoraRoomType.TASKS)
                .sender("@_harmonia_p_user:synapse")
                .harmoniaResourceType("Task")
                .harmoniaResourceId("task-999")
                .content("Review urgent lab result")
                .timestamp(now)
                .correlationId("corr-12345")
                .causationId("cause-6789")
                .securityContext(secContext)
                .addMetadata("priority", "URGENT")
                .build();

        assertThat(event.getEventId()).isEqualTo("event-001");
        assertThat(event.getEventType()).isEqualTo("TASK_NOTE");
        assertThat(event.getRoomId()).isEqualTo("!room123:synapse");
        assertThat(event.getRoomType()).isEqualTo(AgoraRoomType.TASKS);
        assertThat(event.getSender()).isEqualTo("@_harmonia_p_user:synapse");
        assertThat(event.getHarmoniaResourceType()).isEqualTo("Task");
        assertThat(event.getHarmoniaResourceId()).isEqualTo("task-999");
        assertThat(event.getContent()).isEqualTo("Review urgent lab result");
        assertThat(event.getTimestamp()).isEqualTo(now);
        assertThat(event.getCorrelationId()).isEqualTo("corr-12345");
        assertThat(event.getCausationId()).isEqualTo("cause-6789");
        assertThat(event.getSecurityContext()).isEqualTo(secContext);
        assertThat(event.getMetadata()).containsEntry("priority", "URGENT");
    }

    @Test
    @DisplayName("Should serialize and deserialize JSON roundtrip losslessly")
    void testJsonRoundtrip() throws Exception {
        ThemisSecurityContext secContext = ThemisSecurityContext.fromPrincipal(
                ThemisPrincipal.service("service:pylai"), "corr-9988");

        AgoraCollaborationEvent original = AgoraCollaborationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("CLINICAL_DISCUSSION")
                .roomId("!discussion:synapse")
                .roomType(AgoraRoomType.DISCUSSION)
                .sender("@_harmonia_p_doctor:synapse")
                .harmoniaResourceType("Patient")
                .harmoniaResourceId("pat-4455")
                .content("Patient vitals stable post-op")
                .correlationId("corr-9988")
                .securityContext(secContext)
                .metadata(Map.of("department", "ICU"))
                .build();

        String json = objectMapper.writeValueAsString(original);
        assertThat(json).isNotBlank();

        AgoraCollaborationEvent deserialized = objectMapper.readValue(json, AgoraCollaborationEvent.class);
        assertThat(deserialized.getEventId()).isEqualTo(original.getEventId());
        assertThat(deserialized.getEventType()).isEqualTo(original.getEventType());
        assertThat(deserialized.getRoomId()).isEqualTo(original.getRoomId());
        assertThat(deserialized.getRoomType()).isEqualTo(original.getRoomType());
        assertThat(deserialized.getSender()).isEqualTo(original.getSender());
        assertThat(deserialized.getHarmoniaResourceType()).isEqualTo(original.getHarmoniaResourceType());
        assertThat(deserialized.getHarmoniaResourceId()).isEqualTo(original.getHarmoniaResourceId());
        assertThat(deserialized.getContent()).isEqualTo(original.getContent());
        assertThat(deserialized.getCorrelationId()).isEqualTo(original.getCorrelationId());
        assertThat(deserialized.getMetadata()).containsEntry("department", "ICU");
    }
}
