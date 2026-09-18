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

package net.fhirfactory.harmonia.agora.matrix.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Encapsulates Synapse local user details for administration.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatrixUserDto {

    @JsonProperty("name")
    private String userId;

    @JsonProperty("displayname")
    private String displayName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("admin")
    private Boolean admin = false;

    @JsonProperty("deactivated")
    private Boolean deactivated = false;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("user_type")
    private String userType;

    public MatrixUserDto() {
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(Boolean deactivated) {
        this.deactivated = deactivated;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatrixUserDto that = (MatrixUserDto) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "MatrixUserDto{" +
                "userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", admin=" + admin +
                ", deactivated=" + deactivated +
                '}';
    }

    public static class Builder {
        private String userId;
        private String displayName;
        private String password;
        private Boolean admin = false;
        private Boolean deactivated = false;
        private String avatarUrl;
        private String userType;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder admin(Boolean admin) {
            this.admin = admin;
            return this;
        }

        public Builder deactivated(Boolean deactivated) {
            this.deactivated = deactivated;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder userType(String userType) {
            this.userType = userType;
            return this;
        }

        public MatrixUserDto build() {
            MatrixUserDto dto = new MatrixUserDto();
            dto.setUserId(userId);
            dto.setDisplayName(displayName);
            dto.setPassword(password);
            dto.setAdmin(admin);
            dto.setDeactivated(deactivated);
            dto.setAvatarUrl(avatarUrl);
            dto.setUserType(userType);
            return dto;
        }
    }
}
