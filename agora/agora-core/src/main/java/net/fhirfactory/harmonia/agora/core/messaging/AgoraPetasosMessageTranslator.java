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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.agora.api.model.AgoraCollaborationEvent;
import net.fhirfactory.harmonia.agora.api.topic.AgoraTopics;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosMessagingException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Translates between Agora domain events and Petasos messaging envelopes.
 * Strictly adheres to Petasos zero-inspection and zero-PHI transport guidelines.
 */
public final class AgoraPetasosMessageTranslator {

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private AgoraPetasosMessageTranslator() {
        // Utility class
    }

    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    /**
     * Translates an {@link AgoraCollaborationEvent} into a canonical {@link PetasosMessage} envelope.
     *
     * @param event       the collaboration event
     * @param destination the target Petasos queue or topic
     * @return the serialized Petasos envelope
     * @throws PetasosMessagingException if serialization fails
     */
    public static PetasosMessage toPetasosMessage(AgoraCollaborationEvent event, PetasosDestination destination) throws PetasosMessagingException {
        if (event == null) {
            throw new IllegalArgumentException("AgoraCollaborationEvent must not be null");
        }
        PetasosDestination target = (destination != null) ? destination : PetasosDestination.queue(AgoraTopics.QUEUE_AGORA_INBOUND);

        byte[] payloadBytes;
        try {
            payloadBytes = OBJECT_MAPPER.writeValueAsBytes(event);
        } catch (JsonProcessingException e) {
            throw new PetasosMessagingException("Failed to serialize AgoraCollaborationEvent to Petasos payload", e);
        }

        Map<String, Object> metadata = new LinkedHashMap<>();
        if (event.getCorrelationId() != null) {
            metadata.put("correlationId", event.getCorrelationId());
        }
        if (event.getCausationId() != null) {
            metadata.put("causationId", event.getCausationId());
        }
        if (event.getRoomId() != null) {
            metadata.put("roomId", event.getRoomId());
        }
        if (event.getHarmoniaResourceType() != null) {
            metadata.put("harmoniaResourceType", event.getHarmoniaResourceType());
        }
        if (event.getHarmoniaResourceId() != null) {
            metadata.put("harmoniaResourceId", event.getHarmoniaResourceId());
        }
        if (event.getEventType() != null) {
            metadata.put("agoraEventType", event.getEventType());
        }

        return PetasosMessage.builder()
                .messageId(event.getEventId())
                .correlationId(event.getCorrelationId())
                .causationId(event.getCausationId())
                .messageType(event.getEventType())
                .source(AgoraTopics.DEFAULT_SOURCE)
                .destination(target)
                .timestamp(event.getTimestamp())
                .contentType("application/json")
                .schemaIdentifier(AgoraTopics.DATA_ELEMENT_COMMUNICATION)
                .schemaVersion(AgoraTopics.TOPIC_MODEL_VERSION)
                .payload(payloadBytes)
                .metadata(metadata)
                .durable(true)
                .build();
    }

    /**
     * Translates a {@link PetasosMessage} envelope back into an {@link AgoraCollaborationEvent}.
     *
     * @param message the Petasos message envelope
     * @return the deserialized domain event
     * @throws PetasosMessagingException if deserialization fails
     */
    public static AgoraCollaborationEvent fromPetasosMessage(PetasosMessage message) throws PetasosMessagingException {
        if (message == null || message.getPayload() == null || message.getPayload().length == 0) {
            throw new IllegalArgumentException("PetasosMessage payload must not be null or empty");
        }

        try {
            return OBJECT_MAPPER.readValue(message.getPayload(), AgoraCollaborationEvent.class);
        } catch (IOException e) {
            throw new PetasosMessagingException("Failed to deserialize AgoraCollaborationEvent from Petasos payload", e);
        }
    }
}
