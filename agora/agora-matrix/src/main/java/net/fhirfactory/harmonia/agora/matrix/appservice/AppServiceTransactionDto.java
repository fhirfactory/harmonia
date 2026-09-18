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

package net.fhirfactory.harmonia.agora.matrix.appservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the Matrix Application Service transaction payload
 * delivered via PUT /_matrix/app/v1/transactions/{txnId}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppServiceTransactionDto {

    @JsonProperty("events")
    private List<AppServiceEventDto> events = new ArrayList<>();

    public AppServiceTransactionDto() {
    }

    public AppServiceTransactionDto(List<AppServiceEventDto> events) {
        this.events = events != null ? events : new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<AppServiceEventDto> getEvents() {
        return events != null ? events : Collections.emptyList();
    }

    public void setEvents(List<AppServiceEventDto> events) {
        this.events = events != null ? events : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "AppServiceTransactionDto{" +
                "eventCount=" + (events != null ? events.size() : 0) +
                '}';
    }

    public static class Builder {
        private final List<AppServiceEventDto> events = new ArrayList<>();

        public Builder event(AppServiceEventDto event) {
            if (event != null) {
                this.events.add(event);
            }
            return this;
        }

        public Builder events(List<AppServiceEventDto> events) {
            if (events != null) {
                this.events.addAll(events);
            }
            return this;
        }

        public AppServiceTransactionDto build() {
            return new AppServiceTransactionDto(new ArrayList<>(events));
        }
    }
}
