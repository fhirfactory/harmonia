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

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates Matrix room power level state (`m.room.power_levels`).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatrixPowerLevelsDto {

    @JsonProperty("users")
    private Map<String, Integer> users = new HashMap<>();

    @JsonProperty("users_default")
    private Integer usersDefault = 0;

    @JsonProperty("events")
    private Map<String, Integer> events = new HashMap<>();

    @JsonProperty("events_default")
    private Integer eventsDefault = 0;

    @JsonProperty("state_default")
    private Integer stateDefault = 50;

    @JsonProperty("ban")
    private Integer ban = 50;

    @JsonProperty("kick")
    private Integer kick = 50;

    @JsonProperty("redact")
    private Integer redact = 50;

    @JsonProperty("invite")
    private Integer invite = 50;

    public MatrixPowerLevelsDto() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Integer> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Integer> users) {
        this.users = users;
    }

    public Integer getUsersDefault() {
        return usersDefault;
    }

    public void setUsersDefault(Integer usersDefault) {
        this.usersDefault = usersDefault;
    }

    public Map<String, Integer> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Integer> events) {
        this.events = events;
    }

    public Integer getEventsDefault() {
        return eventsDefault;
    }

    public void setEventsDefault(Integer eventsDefault) {
        this.eventsDefault = eventsDefault;
    }

    public Integer getStateDefault() {
        return stateDefault;
    }

    public void setStateDefault(Integer stateDefault) {
        this.stateDefault = stateDefault;
    }

    public Integer getBan() {
        return ban;
    }

    public void setBan(Integer ban) {
        this.ban = ban;
    }

    public Integer getKick() {
        return kick;
    }

    public void setKick(Integer kick) {
        this.kick = kick;
    }

    public Integer getRedact() {
        return redact;
    }

    public void setRedact(Integer redact) {
        this.redact = redact;
    }

    public Integer getInvite() {
        return invite;
    }

    public void setInvite(Integer invite) {
        this.invite = invite;
    }

    public static class Builder {
        private final MatrixPowerLevelsDto dto = new MatrixPowerLevelsDto();

        public Builder user(String userId, int powerLevel) {
            dto.users.put(userId, powerLevel);
            return this;
        }

        public Builder users(Map<String, Integer> users) {
            if (users != null) {
                dto.users.putAll(users);
            }
            return this;
        }

        public Builder usersDefault(Integer usersDefault) {
            dto.usersDefault = usersDefault;
            return this;
        }

        public Builder event(String eventType, int powerLevel) {
            dto.events.put(eventType, powerLevel);
            return this;
        }

        public Builder events(Map<String, Integer> events) {
            if (events != null) {
                dto.events.putAll(events);
            }
            return this;
        }

        public Builder eventsDefault(Integer eventsDefault) {
            dto.eventsDefault = eventsDefault;
            return this;
        }

        public Builder stateDefault(Integer stateDefault) {
            dto.stateDefault = stateDefault;
            return this;
        }

        public Builder ban(Integer ban) {
            dto.ban = ban;
            return this;
        }

        public Builder kick(Integer kick) {
            dto.kick = kick;
            return this;
        }

        public Builder redact(Integer redact) {
            dto.redact = redact;
            return this;
        }

        public Builder invite(Integer invite) {
            dto.invite = invite;
            return this;
        }

        public MatrixPowerLevelsDto build() {
            return dto;
        }
    }
}
