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

import java.util.Objects;

/**
 * Encapsulates Matrix room and Space metadata.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatrixRoomDto {

    public static final String ROOM_TYPE_SPACE = "m.space";

    @JsonProperty("room_id")
    private String roomId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("room_type")
    private String roomType;

    @JsonProperty("canonical_alias")
    private String canonicalAlias;

    @JsonProperty("visibility")
    private String visibility;

    @JsonProperty("joined_members_count")
    private Integer joinedMembersCount;

    public MatrixRoomDto() {
    }

    public MatrixRoomDto(String roomId, String name, String topic, String roomType) {
        this.roomId = roomId;
        this.name = name;
        this.topic = topic;
        this.roomType = roomType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isSpace() {
        return ROOM_TYPE_SPACE.equalsIgnoreCase(roomType);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getCanonicalAlias() {
        return canonicalAlias;
    }

    public void setCanonicalAlias(String canonicalAlias) {
        this.canonicalAlias = canonicalAlias;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Integer getJoinedMembersCount() {
        return joinedMembersCount;
    }

    public void setJoinedMembersCount(Integer joinedMembersCount) {
        this.joinedMembersCount = joinedMembersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatrixRoomDto that = (MatrixRoomDto) o;
        return Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }

    @Override
    public String toString() {
        return "MatrixRoomDto{" +
                "roomId='" + roomId + '\'' +
                ", name='" + name + '\'' +
                ", roomType='" + roomType + '\'' +
                ", visibility='" + visibility + '\'' +
                '}';
    }

    public static class Builder {
        private String roomId;
        private String name;
        private String topic;
        private String roomType;
        private String canonicalAlias;
        private String visibility;
        private Integer joinedMembersCount;

        public Builder roomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder roomType(String roomType) {
            this.roomType = roomType;
            return this;
        }

        public Builder space(boolean isSpace) {
            this.roomType = isSpace ? ROOM_TYPE_SPACE : null;
            return this;
        }

        public Builder canonicalAlias(String canonicalAlias) {
            this.canonicalAlias = canonicalAlias;
            return this;
        }

        public Builder visibility(String visibility) {
            this.visibility = visibility;
            return this;
        }

        public Builder joinedMembersCount(Integer joinedMembersCount) {
            this.joinedMembersCount = joinedMembersCount;
            return this;
        }

        public MatrixRoomDto build() {
            MatrixRoomDto dto = new MatrixRoomDto();
            dto.setRoomId(roomId);
            dto.setName(name);
            dto.setTopic(topic);
            dto.setRoomType(roomType);
            dto.setCanonicalAlias(canonicalAlias);
            dto.setVisibility(visibility);
            dto.setJoinedMembersCount(joinedMembersCount);
            return dto;
        }
    }
}
