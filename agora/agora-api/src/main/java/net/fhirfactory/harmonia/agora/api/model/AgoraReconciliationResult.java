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

package net.fhirfactory.harmonia.agora.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulates the outcome of a membership reconciliation cycle for a Matrix room.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgoraReconciliationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("roomId")
    private final String roomId;

    @JsonProperty("invitedUsers")
    private final List<String> invitedUsers;

    @JsonProperty("kickedUsers")
    private final List<String> kickedUsers;

    @JsonProperty("retainedUsers")
    private final List<String> retainedUsers;

    @JsonProperty("deniedUsers")
    private final List<String> deniedUsers;

    @JsonCreator
    public AgoraReconciliationResult(
            @JsonProperty("roomId") String roomId,
            @JsonProperty("invitedUsers") List<String> invitedUsers,
            @JsonProperty("kickedUsers") List<String> kickedUsers,
            @JsonProperty("retainedUsers") List<String> retainedUsers,
            @JsonProperty("deniedUsers") List<String> deniedUsers
    ) {
        this.roomId = Objects.requireNonNull(roomId, "roomId must not be null");
        this.invitedUsers = (invitedUsers != null) ? List.copyOf(invitedUsers) : List.of();
        this.kickedUsers = (kickedUsers != null) ? List.copyOf(kickedUsers) : List.of();
        this.retainedUsers = (retainedUsers != null) ? List.copyOf(retainedUsers) : List.of();
        this.deniedUsers = (deniedUsers != null) ? List.copyOf(deniedUsers) : List.of();
    }

    public static Builder builder(String roomId) {
        return new Builder(roomId);
    }

    public String getRoomId() {
        return roomId;
    }

    public List<String> getInvitedUsers() {
        return invitedUsers;
    }

    public List<String> getKickedUsers() {
        return kickedUsers;
    }

    public List<String> getRetainedUsers() {
        return retainedUsers;
    }

    public List<String> getDeniedUsers() {
        return deniedUsers;
    }

    public boolean hasDrift() {
        return !invitedUsers.isEmpty() || !kickedUsers.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgoraReconciliationResult that = (AgoraReconciliationResult) o;
        return Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }

    @Override
    public String toString() {
        return "AgoraReconciliationResult{" +
                "roomId='" + roomId + '\'' +
                ", invitedUsers=" + invitedUsers +
                ", kickedUsers=" + kickedUsers +
                ", retainedUsers=" + retainedUsers +
                ", deniedUsers=" + deniedUsers +
                ", hasDrift=" + hasDrift() +
                '}';
    }

    public static class Builder {
        private final String roomId;
        private List<String> invitedUsers = List.of();
        private List<String> kickedUsers = List.of();
        private List<String> retainedUsers = List.of();
        private List<String> deniedUsers = List.of();

        public Builder(String roomId) {
            this.roomId = roomId;
        }

        public Builder invitedUsers(List<String> invitedUsers) {
            this.invitedUsers = (invitedUsers != null) ? List.copyOf(invitedUsers) : List.of();
            return this;
        }

        public Builder kickedUsers(List<String> kickedUsers) {
            this.kickedUsers = (kickedUsers != null) ? List.copyOf(kickedUsers) : List.of();
            return this;
        }

        public Builder retainedUsers(List<String> retainedUsers) {
            this.retainedUsers = (retainedUsers != null) ? List.copyOf(retainedUsers) : List.of();
            return this;
        }

        public Builder deniedUsers(List<String> deniedUsers) {
            this.deniedUsers = (deniedUsers != null) ? List.copyOf(deniedUsers) : List.of();
            return this;
        }

        public AgoraReconciliationResult build() {
            return new AgoraReconciliationResult(roomId, invitedUsers, kickedUsers, retainedUsers, deniedUsers);
        }
    }
}
