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

package net.fhirfactory.harmonia.petasos.api.destination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a logical Petasos messaging destination.
 */
public final class PetasosDestination implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum DestinationType {
        QUEUE,
        TOPIC
    }

    private final String name;
    private final DestinationType type;

    @JsonCreator
    public PetasosDestination(
            @JsonProperty("name") String name,
            @JsonProperty("type") DestinationType type) {
        this.name = Objects.requireNonNull(name, "Destination name must not be null").trim();
        if (this.name.isEmpty()) {
            throw new IllegalArgumentException("Destination name must not be blank");
        }
        this.type = type != null ? type : DestinationType.QUEUE;
    }

    public static PetasosDestination queue(String name) {
        return new PetasosDestination(name, DestinationType.QUEUE);
    }

    public static PetasosDestination topic(String name) {
        return new PetasosDestination(name, DestinationType.TOPIC);
    }

    public static PetasosDestination of(String name, DestinationType type) {
        return new PetasosDestination(name, type);
    }

    public String getName() {
        return name;
    }

    public DestinationType getType() {
        return type;
    }

    public boolean isQueue() {
        return type == DestinationType.QUEUE;
    }

    public boolean isTopic() {
        return type == DestinationType.TOPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetasosDestination that)) return false;
        return Objects.equals(name, that.name) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return (type == DestinationType.QUEUE ? "queue://" : "topic://") + name;
    }
}
