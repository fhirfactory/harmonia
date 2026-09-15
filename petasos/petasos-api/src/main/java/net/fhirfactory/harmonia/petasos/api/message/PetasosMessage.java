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

package net.fhirfactory.harmonia.petasos.api.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

/**
 * Standard Petasos message envelope for the Harmonia integration platform.
 * <p>
 * Transports opaque payloads between modules while tracking message identity,
 * correlation, causation lineage, schemas, timestamps, and routing metadata.
 * Payload contents are strictly opaque to the Petasos transport subsystem.
 */
public final class PetasosMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_CONTENT_TYPE = "application/json";
    public static final int DEFAULT_PRIORITY = 4;

    private final String messageId;
    private final String correlationId;
    private final String causationId;
    private final String messageType;
    private final String source;
    private final PetasosDestination destination;
    private final Instant timestamp;
    private final String contentType;
    private final String schemaIdentifier;
    private final String schemaVersion;
    private final byte[] payload;
    private final Map<String, Object> metadata;
    private final boolean durable;
    private final int priority;
    private final Instant expiration;
    private final String duplicateDetectionId;

    @JsonCreator
    public PetasosMessage(
            @JsonProperty("messageId") String messageId,
            @JsonProperty("correlationId") String correlationId,
            @JsonProperty("causationId") String causationId,
            @JsonProperty("messageType") String messageType,
            @JsonProperty("source") String source,
            @JsonProperty("destination") PetasosDestination destination,
            @JsonProperty("timestamp") Instant timestamp,
            @JsonProperty("contentType") String contentType,
            @JsonProperty("schemaIdentifier") String schemaIdentifier,
            @JsonProperty("schemaVersion") String schemaVersion,
            @JsonProperty("payload") byte[] payload,
            @JsonProperty("metadata") Map<String, Object> metadata,
            @JsonProperty("durable") boolean durable,
            @JsonProperty("priority") int priority,
            @JsonProperty("expiration") Instant expiration,
            @JsonProperty("duplicateDetectionId") String duplicateDetectionId) {

        this.messageId = messageId != null && !messageId.isBlank() ? messageId : UUID.randomUUID().toString();
        this.correlationId = correlationId != null && !correlationId.isBlank() ? correlationId : this.messageId;
        this.causationId = causationId;
        this.messageType = messageType;
        this.source = source;
        this.destination = destination;
        this.timestamp = timestamp != null ? timestamp : Instant.now();
        this.contentType = contentType != null && !contentType.isBlank() ? contentType : DEFAULT_CONTENT_TYPE;
        this.schemaIdentifier = schemaIdentifier;
        this.schemaVersion = schemaVersion;
        this.payload = payload != null ? payload.clone() : new byte[0];
        this.metadata = metadata != null ? Collections.unmodifiableMap(new LinkedHashMap<>(metadata)) : Collections.emptyMap();
        this.durable = durable;
        this.priority = priority >= 0 && priority <= 9 ? priority : DEFAULT_PRIORITY;
        this.expiration = expiration;
        this.duplicateDetectionId = duplicateDetectionId != null && !duplicateDetectionId.isBlank()
                ? duplicateDetectionId
                : this.messageId;
    }

    public static PetasosMessageBuilder builder() {
        return new PetasosMessageBuilder();
    }

    public static PetasosMessage of(String textPayload) {
        return builder().payload(textPayload).build();
    }

    public static PetasosMessage of(byte[] bytesPayload) {
        return builder().payload(bytesPayload).build();
    }

    public PetasosMessageBuilder toBuilder() {
        return new PetasosMessageBuilder(this);
    }

    public String getMessageId() {
        return messageId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getCausationId() {
        return causationId;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getSource() {
        return source;
    }

    public PetasosDestination getDestination() {
        return destination;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getContentType() {
        return contentType;
    }

    public String getSchemaIdentifier() {
        return schemaIdentifier;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public byte[] getPayload() {
        return payload.clone();
    }

    @JsonIgnore
    public String getPayloadAsString() {
        return getPayloadAsString(StandardCharsets.UTF_8);
    }

    @JsonIgnore
    public String getPayloadAsString(Charset charset) {
        Objects.requireNonNull(charset, "Charset must not be null");
        return new String(payload, charset);
    }

    @JsonIgnore
    public int getPayloadSize() {
        return payload.length;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    public <T> T getMetadata(String key) {
        return (T) metadata.get(key);
    }

    @JsonIgnore
    public String getMetadataString(String key) {
        Object val = metadata.get(key);
        return val != null ? val.toString() : null;
    }

    public boolean isDurable() {
        return durable;
    }

    public int getPriority() {
        return priority;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public String getDuplicateDetectionId() {
        return duplicateDetectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetasosMessage that)) return false;
        return durable == that.durable &&
                priority == that.priority &&
                Objects.equals(messageId, that.messageId) &&
                Objects.equals(correlationId, that.correlationId) &&
                Objects.equals(causationId, that.causationId) &&
                Objects.equals(messageType, that.messageType) &&
                Objects.equals(source, that.source) &&
                Objects.equals(destination, that.destination) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(schemaIdentifier, that.schemaIdentifier) &&
                Objects.equals(schemaVersion, that.schemaVersion) &&
                Arrays.equals(payload, that.payload) &&
                Objects.equals(metadata, that.metadata) &&
                Objects.equals(expiration, that.expiration) &&
                Objects.equals(duplicateDetectionId, that.duplicateDetectionId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(messageId, correlationId, causationId, messageType, source,
                destination, timestamp, contentType, schemaIdentifier, schemaVersion,
                metadata, durable, priority, expiration, duplicateDetectionId);
        result = 31 * result + Arrays.hashCode(payload);
        return result;
    }

    @Override
    public String toString() {
        // Safe logging: Payload is deliberately NOT logged to protect sensitive healthcare data
        return "PetasosMessage{" +
                "messageId='" + messageId + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", causationId='" + causationId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", source='" + source + '\'' +
                ", destination=" + destination +
                ", timestamp=" + timestamp +
                ", contentType='" + contentType + '\'' +
                ", schemaId='" + schemaIdentifier + '\'' +
                ", schemaVer='" + schemaVersion + '\'' +
                ", payloadSizeBytes=" + payload.length +
                ", durable=" + durable +
                ", priority=" + priority +
                ", duplicateDetectionId='" + duplicateDetectionId + '\'' +
                '}';
    }
}
