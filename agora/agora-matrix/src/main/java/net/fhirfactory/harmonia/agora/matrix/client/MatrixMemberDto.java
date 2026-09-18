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
 * Encapsulates Matrix room membership information.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatrixMemberDto {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("membership")
    private String membership; // "join", "invite", "leave", "ban"

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    public MatrixMemberDto() {
    }

    public MatrixMemberDto(String userId, String membership, String displayName) {
        this.userId = userId;
        this.membership = membership;
        this.displayName = displayName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatrixMemberDto that = (MatrixMemberDto) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "MatrixMemberDto{" +
                "userId='" + userId + '\'' +
                ", membership='" + membership + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    public static class Builder {
        private String userId;
        private String membership;
        private String displayName;
        private String avatarUrl;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder membership(String membership) {
            this.membership = membership;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public MatrixMemberDto build() {
            MatrixMemberDto dto = new MatrixMemberDto();
            dto.setUserId(userId);
            dto.setMembership(membership);
            dto.setDisplayName(displayName);
            dto.setAvatarUrl(avatarUrl);
            return dto;
        }
    }
}
