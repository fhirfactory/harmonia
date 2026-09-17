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

package net.fhirfactory.harmonia.agora.matrix.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;

/**
 * Encapsulates a Matrix room event.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatrixEventDto {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("room_id")
    private String roomId;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("origin_server_ts")
    private Long originServerTs;

    @JsonProperty("content")
    private Map<String, Object> content;

    @JsonProperty("state_key")
    private String stateKey;

    @JsonProperty("unsigned")
    private Map<String, Object> unsigned;

    public MatrixEventDto() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getOriginServerTs() {
        return originServerTs;
    }

    public void setOriginServerTs(Long originServerTs) {
        this.originServerTs = originServerTs;
    }

    public Map<String, Object> getContent() {
        return content != null ? content : Collections.emptyMap();
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public String getStateKey() {
        return stateKey;
    }

    public void setStateKey(String stateKey) {
        this.stateKey = stateKey;
    }

    public Map<String, Object> getUnsigned() {
        return unsigned != null ? unsigned : Collections.emptyMap();
    }

    public void setUnsigned(Map<String, Object> unsigned) {
        this.unsigned = unsigned;
    }

    @Override
    public String toString() {
        return "MatrixEventDto{" +
                "eventId='" + eventId + '\'' +
                ", type='" + type + '\'' +
                ", roomId='" + roomId + '\'' +
                ", sender='" + sender + '\'' +
                ", stateKey='" + stateKey + '\'' +
                '}';
    }

    public static class Builder {
        private String eventId;
        private String type;
        private String roomId;
        private String sender;
        private Long originServerTs;
        private Map<String, Object> content;
        private String stateKey;
        private Map<String, Object> unsigned;

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder roomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        public Builder originServerTs(Long originServerTs) {
            this.originServerTs = originServerTs;
            return this;
        }

        public Builder content(Map<String, Object> content) {
            this.content = content;
            return this;
        }

        public Builder stateKey(String stateKey) {
            this.stateKey = stateKey;
            return this;
        }

        public Builder unsigned(Map<String, Object> unsigned) {
            this.unsigned = unsigned;
            return this;
        }

        public MatrixEventDto build() {
            MatrixEventDto dto = new MatrixEventDto();
            dto.setEventId(eventId);
            dto.setType(type);
            dto.setRoomId(roomId);
            dto.setSender(sender);
            dto.setOriginServerTs(originServerTs);
            dto.setContent(content);
            dto.setStateKey(stateKey);
            dto.setUnsigned(unsigned);
            return dto;
        }
    }
}
