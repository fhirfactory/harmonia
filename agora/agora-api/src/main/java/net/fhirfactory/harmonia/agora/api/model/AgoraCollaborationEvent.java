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

package net.fhirfactory.harmonia.agora.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Canonical event model for collaboration interactions between Matrix Synapse and Harmonia.
 * Transports safe collaboration context and clinical task notifications across the Petasos messaging backbone.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgoraCollaborationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("eventId")
    private final String eventId;

    @JsonProperty("eventType")
    private final String eventType;

    @JsonProperty("roomId")
    private final String roomId;

    @JsonProperty("roomType")
    private final AgoraRoomType roomType;

    @JsonProperty("sender")
    private final String sender;

    @JsonProperty("harmoniaResourceId")
    private final String harmoniaResourceId;

    @JsonProperty("harmoniaResourceType")
    private final String harmoniaResourceType;

    @JsonProperty("content")
    private final String content;

    @JsonProperty("timestamp")
    private final Instant timestamp;

    @JsonProperty("correlationId")
    private final String correlationId;

    @JsonProperty("causationId")
    private final String causationId;

    @JsonProperty("securityContext")
    private final ThemisSecurityContext securityContext;

    @JsonProperty("metadata")
    private final Map<String, Object> metadata;

    @JsonCreator
    public AgoraCollaborationEvent(
            @JsonProperty("eventId") String eventId,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("roomId") String roomId,
            @JsonProperty("roomType") AgoraRoomType roomType,
            @JsonProperty("sender") String sender,
            @JsonProperty("harmoniaResourceId") String harmoniaResourceId,
            @JsonProperty("harmoniaResourceType") String harmoniaResourceType,
            @JsonProperty("content") String content,
            @JsonProperty("timestamp") Instant timestamp,
            @JsonProperty("correlationId") String correlationId,
            @JsonProperty("causationId") String causationId,
            @JsonProperty("securityContext") ThemisSecurityContext securityContext,
            @JsonProperty("metadata") Map<String, Object> metadata) {

        this.eventId = (eventId != null && !eventId.isBlank()) ? eventId : UUID.randomUUID().toString();
        this.eventType = (eventType != null && !eventType.isBlank()) ? eventType : "COLLABORATION_MESSAGE";
        this.roomId = roomId;
        this.roomType = roomType;
        this.sender = sender;
        this.harmoniaResourceId = harmoniaResourceId;
        this.harmoniaResourceType = harmoniaResourceType;
        this.content = content;
        this.timestamp = (timestamp != null) ? timestamp : Instant.now();
        this.correlationId = (correlationId != null && !correlationId.isBlank()) ? correlationId : this.eventId;
        this.causationId = causationId;
        this.securityContext = securityContext;
        this.metadata = (metadata != null)
                ? Collections.unmodifiableMap(new LinkedHashMap<>(metadata))
                : Collections.emptyMap();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getRoomId() {
        return roomId;
    }

    public AgoraRoomType getRoomType() {
        return roomType;
    }

    public String getSender() {
        return sender;
    }

    public String getHarmoniaResourceId() {
        return harmoniaResourceId;
    }

    public String getHarmoniaResourceType() {
        return harmoniaResourceType;
    }

    public String getContent() {
        return content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getCausationId() {
        return causationId;
    }

    public ThemisSecurityContext getSecurityContext() {
        return securityContext;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public static class Builder {
        private String eventId;
        private String eventType = "COLLABORATION_MESSAGE";
        private String roomId;
        private AgoraRoomType roomType;
        private String sender;
        private String harmoniaResourceId;
        private String harmoniaResourceType;
        private String content;
        private Instant timestamp;
        private String correlationId;
        private String causationId;
        private ThemisSecurityContext securityContext;
        private Map<String, Object> metadata = new LinkedHashMap<>();

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder roomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder roomType(AgoraRoomType roomType) {
            this.roomType = roomType;
            return this;
        }

        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        public Builder harmoniaResourceId(String harmoniaResourceId) {
            this.harmoniaResourceId = harmoniaResourceId;
            return this;
        }

        public Builder harmoniaResourceType(String harmoniaResourceType) {
            this.harmoniaResourceType = harmoniaResourceType;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder causationId(String causationId) {
            this.causationId = causationId;
            return this;
        }

        public Builder securityContext(ThemisSecurityContext securityContext) {
            this.securityContext = securityContext;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            if (metadata != null) {
                this.metadata = new LinkedHashMap<>(metadata);
            }
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        public AgoraCollaborationEvent build() {
            return new AgoraCollaborationEvent(
                    eventId, eventType, roomId, roomType, sender,
                    harmoniaResourceId, harmoniaResourceType, content,
                    timestamp, correlationId, causationId, securityContext, metadata
            );
        }
    }
}
