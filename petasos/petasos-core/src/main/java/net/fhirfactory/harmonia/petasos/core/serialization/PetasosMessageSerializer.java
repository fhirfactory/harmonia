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

package net.fhirfactory.harmonia.petasos.core.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosException;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Standard JSON serializer and deserializer for {@link PetasosMessage} envelopes.
 */
public final class PetasosMessageSerializer {

    private static final ObjectMapper MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    public static byte[] serializeToBytes(PetasosMessage message) {
        if (message == null) {
            return new byte[0];
        }
        try {
            return MAPPER.writeValueAsBytes(message);
        } catch (Exception e) {
            throw new PetasosException("Failed to serialize PetasosMessage: " + e.getMessage(), e);
        }
    }

    public static String serializeToString(PetasosMessage message) {
        if (message == null) {
            return "";
        }
        try {
            return MAPPER.writeValueAsString(message);
        } catch (Exception e) {
            throw new PetasosException("Failed to serialize PetasosMessage: " + e.getMessage(), e);
        }
    }

    public static PetasosMessage deserialize(byte[] jsonBytes) {
        if (jsonBytes == null || jsonBytes.length == 0) {
            throw new IllegalArgumentException("JSON bytes must not be null or empty");
        }
        try {
            return MAPPER.readValue(jsonBytes, PetasosMessage.class);
        } catch (IOException e) {
            throw new PetasosException("Failed to deserialize PetasosMessage from bytes: " + e.getMessage(), e);
        }
    }

    public static PetasosMessage deserialize(String jsonString) {
        if (jsonString == null || jsonString.isBlank()) {
            throw new IllegalArgumentException("JSON string must not be null or blank");
        }
        try {
            return MAPPER.readValue(jsonString, PetasosMessage.class);
        } catch (IOException e) {
            throw new PetasosException("Failed to deserialize PetasosMessage from JSON string: " + e.getMessage(), e);
        }
    }
}
