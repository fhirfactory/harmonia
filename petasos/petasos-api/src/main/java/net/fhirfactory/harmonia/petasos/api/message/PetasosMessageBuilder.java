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

import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Fluent builder for {@link PetasosMessage}.
 */
public final class PetasosMessageBuilder {

    private String messageId;
    private String correlationId;
    private String causationId;
    private String messageType;
    private String source;
    private PetasosDestination destination;
    private Instant timestamp;
    private String contentType = PetasosMessage.DEFAULT_CONTENT_TYPE;
    private String schemaIdentifier;
    private String schemaVersion;
    private byte[] payload = new byte[0];
    private final Map<String, Object> metadata = new LinkedHashMap<>();
    private boolean durable = true;
    private int priority = PetasosMessage.DEFAULT_PRIORITY;
    private Instant expiration;
    private String duplicateDetectionId;
    private ThemisSecurityContext securityContext;
    private ThemisPrincipal originatingPrincipal;

    public PetasosMessageBuilder() {
        this.messageId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
    }

    public PetasosMessageBuilder(PetasosMessage copy) {
        Objects.requireNonNull(copy, "Source message must not be null");
        this.messageId = copy.getMessageId();
        this.correlationId = copy.getCorrelationId();
        this.causationId = copy.getCausationId();
        this.messageType = copy.getMessageType();
        this.source = copy.getSource();
        this.destination = copy.getDestination();
        this.timestamp = copy.getTimestamp();
        this.contentType = copy.getContentType();
        this.schemaIdentifier = copy.getSchemaIdentifier();
        this.schemaVersion = copy.getSchemaVersion();
        this.payload = copy.getPayload();
        this.metadata.putAll(copy.getMetadata());
        this.durable = copy.isDurable();
        this.priority = copy.getPriority();
        this.expiration = copy.getExpiration();
        this.duplicateDetectionId = copy.getDuplicateDetectionId();
        this.securityContext = copy.getSecurityContext();
        this.originatingPrincipal = copy.getOriginatingPrincipal();
    }

    public PetasosMessageBuilder messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public PetasosMessageBuilder correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public PetasosMessageBuilder causationId(String causationId) {
        this.causationId = causationId;
        return this;
    }

    public PetasosMessageBuilder messageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public PetasosMessageBuilder source(String source) {
        this.source = source;
        return this;
    }

    public PetasosMessageBuilder destination(PetasosDestination destination) {
        this.destination = destination;
        return this;
    }

    public PetasosMessageBuilder destinationQueue(String queueName) {
        this.destination = PetasosDestination.queue(queueName);
        return this;
    }

    public PetasosMessageBuilder destinationTopic(String topicName) {
        this.destination = PetasosDestination.topic(topicName);
        return this;
    }

    public PetasosMessageBuilder timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public PetasosMessageBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public PetasosMessageBuilder schemaIdentifier(String schemaIdentifier) {
        this.schemaIdentifier = schemaIdentifier;
        return this;
    }

    public PetasosMessageBuilder schemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
        return this;
    }

    public PetasosMessageBuilder schema(String identifier, String version) {
        this.schemaIdentifier = identifier;
        this.schemaVersion = version;
        return this;
    }

    public PetasosMessageBuilder payload(byte[] payload) {
        this.payload = payload != null ? payload.clone() : new byte[0];
        return this;
    }

    public PetasosMessageBuilder payload(String textPayload) {
        return payload(textPayload, StandardCharsets.UTF_8);
    }

    public PetasosMessageBuilder payload(String textPayload, Charset charset) {
        Objects.requireNonNull(charset, "Charset must not be null");
        this.payload = textPayload != null ? textPayload.getBytes(charset) : new byte[0];
        return this;
    }

    public PetasosMessageBuilder metadata(Map<String, Object> metadata) {
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
        return this;
    }

    public PetasosMessageBuilder header(String key, Object value) {
        if (key != null) {
            if (value != null) {
                this.metadata.put(key, value);
            } else {
                this.metadata.remove(key);
            }
        }
        return this;
    }

    public PetasosMessageBuilder durable(boolean durable) {
        this.durable = durable;
        return this;
    }

    public PetasosMessageBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }

    public PetasosMessageBuilder expiration(Instant expiration) {
        this.expiration = expiration;
        return this;
    }

    public PetasosMessageBuilder ttl(Duration ttl) {
        if (ttl != null && !ttl.isZero() && !ttl.isNegative()) {
            this.expiration = Instant.now().plus(ttl);
        } else {
            this.expiration = null;
        }
        return this;
    }

    public PetasosMessageBuilder duplicateDetectionId(String duplicateDetectionId) {
        this.duplicateDetectionId = duplicateDetectionId;
        return this;
    }

    public PetasosMessageBuilder securityContext(ThemisSecurityContext securityContext) {
        this.securityContext = securityContext;
        return this;
    }

    public PetasosMessageBuilder originatingPrincipal(ThemisPrincipal originatingPrincipal) {
        this.originatingPrincipal = originatingPrincipal;
        return this;
    }

    public PetasosMessage build() {
        if (messageId == null || messageId.isBlank()) {
            messageId = UUID.randomUUID().toString();
        }
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = messageId;
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        if (duplicateDetectionId == null || duplicateDetectionId.isBlank()) {
            duplicateDetectionId = messageId;
        }

        return new PetasosMessage(
                messageId,
                correlationId,
                causationId,
                messageType,
                source,
                destination,
                timestamp,
                contentType,
                schemaIdentifier,
                schemaVersion,
                payload,
                metadata,
                durable,
                priority,
                expiration,
                duplicateDetectionId,
                securityContext,
                originatingPrincipal
        );
    }
}
