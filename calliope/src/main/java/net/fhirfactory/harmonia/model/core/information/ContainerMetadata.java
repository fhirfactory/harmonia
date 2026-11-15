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

package net.fhirfactory.harmonia.model.core.information;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Encapsulates the metadata describing the container, envelope, assembly, or carriage of an Information Unit's representation.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§7):
 * <ul>
 *   <li>{@code ContainerMetadata} describes the container or transport envelope associated with the IU's representation.</li>
 *   <li>Dimensions include container type (e.g. {@code FHIR_BUNDLE}, {@code HL7_MESSAGE}, {@code DOCUMENT}, {@code EVENT}, {@code PRAGMA}),
 *       source, destination, author, recipient, assembly context, interaction mechanism, and envelope structure.</li>
 *   <li>Relevant native envelope semantics are preserved when governed by the information-management contract,
 *       without being conflated with Information Unit Metadata or Content Metadata.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>All attributes collections are defensively copied and unmodifiable.</li>
 *   <li>String properties are trimmed; empty strings are treated as absent.</li>
 *   <li>Immutable value object design.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ContainerMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final ContainerMetadata EMPTY = new ContainerMetadata(null, null, null, null, null, null, null, null, Map.of());

    private final String containerType;
    private final String source;
    private final String destination;
    private final String author;
    private final String recipient;
    private final String assemblyContext;
    private final String interactionMechanism;
    private final String envelopeStructure;
    private final Map<String, String> attributes;

    /**
     * Canonical constructor establishing validated container metadata.
     *
     * @param containerType        type of container (e.g. "FHIR_BUNDLE", "HL7_MESSAGE") (optional)
     * @param source               source gateway or system identifier (optional)
     * @param destination          target destination identifier (optional)
     * @param author               author/sender (optional)
     * @param recipient            intended recipient (optional)
     * @param assemblyContext      assembly context (optional)
     * @param interactionMechanism interaction mechanism (e.g. "MLLP", "REST") (optional)
     * @param envelopeStructure    envelope structure definition (optional)
     * @param attributes           additional container attributes (optional)
     */
    @JsonCreator
    public ContainerMetadata(
            @JsonProperty("containerType") String containerType,
            @JsonProperty("source") String source,
            @JsonProperty("destination") String destination,
            @JsonProperty("author") String author,
            @JsonProperty("recipient") String recipient,
            @JsonProperty("assemblyContext") String assemblyContext,
            @JsonProperty("interactionMechanism") String interactionMechanism,
            @JsonProperty("envelopeStructure") String envelopeStructure,
            @JsonProperty("attributes") Map<String, String> attributes) {
        this.containerType = sanitize(containerType);
        this.source = sanitize(source);
        this.destination = sanitize(destination);
        this.author = sanitize(author);
        this.recipient = sanitize(recipient);
        this.assemblyContext = sanitize(assemblyContext);
        this.interactionMechanism = sanitize(interactionMechanism);
        this.envelopeStructure = sanitize(envelopeStructure);
        this.attributes = attributes == null || attributes.isEmpty()
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
    }

    private static String sanitize(String input) {
        return input != null && !input.isBlank() ? input.trim() : null;
    }

    /**
     * Returns an empty, default {@code ContainerMetadata} instance.
     *
     * @return empty ContainerMetadata
     */
    public static ContainerMetadata empty() {
        return EMPTY;
    }

    /**
     * Creates a new builder for constructing {@code ContainerMetadata} instances.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    @JsonIgnore
    public Optional<String> containerType() {
        return Optional.ofNullable(containerType);
    }

    @JsonProperty("containerType")
    public String getContainerType() {
        return containerType;
    }

    @JsonIgnore
    public Optional<String> source() {
        return Optional.ofNullable(source);
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonIgnore
    public Optional<String> destination() {
        return Optional.ofNullable(destination);
    }

    @JsonProperty("destination")
    public String getDestination() {
        return destination;
    }

    @JsonIgnore
    public Optional<String> author() {
        return Optional.ofNullable(author);
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonIgnore
    public Optional<String> recipient() {
        return Optional.ofNullable(recipient);
    }

    @JsonProperty("recipient")
    public String getRecipient() {
        return recipient;
    }

    @JsonIgnore
    public Optional<String> assemblyContext() {
        return Optional.ofNullable(assemblyContext);
    }

    @JsonProperty("assemblyContext")
    public String getAssemblyContext() {
        return assemblyContext;
    }

    @JsonIgnore
    public Optional<String> interactionMechanism() {
        return Optional.ofNullable(interactionMechanism);
    }

    @JsonProperty("interactionMechanism")
    public String getInteractionMechanism() {
        return interactionMechanism;
    }

    @JsonIgnore
    public Optional<String> envelopeStructure() {
        return Optional.ofNullable(envelopeStructure);
    }

    @JsonProperty("envelopeStructure")
    public String getEnvelopeStructure() {
        return envelopeStructure;
    }

    @JsonProperty("attributes")
    public Map<String, String> attributes() {
        return attributes;
    }

    public Optional<String> getAttribute(String key) {
        if (key == null) return Optional.empty();
        return Optional.ofNullable(attributes.get(key));
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public ContainerMetadata withContainerType(String containerType) {
        return new ContainerMetadata(containerType, this.source, this.destination, this.author, this.recipient, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withSource(String source) {
        return new ContainerMetadata(this.containerType, source, this.destination, this.author, this.recipient, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withDestination(String destination) {
        return new ContainerMetadata(this.containerType, this.source, destination, this.author, this.recipient, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withAuthor(String author) {
        return new ContainerMetadata(this.containerType, this.source, this.destination, author, this.recipient, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withRecipient(String recipient) {
        return new ContainerMetadata(this.containerType, this.source, this.destination, this.author, recipient, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withAssemblyContext(String assemblyContext) {
        return new ContainerMetadata(this.containerType, this.source, this.destination, this.author, this.recipient, assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withInteractionMechanism(String interactionMechanism) {
        return new ContainerMetadata(this.containerType, this.source, this.destination, this.author, this.recipient, this.assemblyContext, interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withEnvelopeStructure(String envelopeStructure) {
        return new ContainerMetadata(this.containerType, this.source, this.destination, this.author, this.recipient, this.assemblyContext, this.interactionMechanism, envelopeStructure, this.attributes);
    }

    public ContainerMetadata withAttributes(Map<String, String> attributes) {
        return new ContainerMetadata(this.containerType, this.source, this.destination, this.author, this.recipient, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, attributes);
    }

    public ContainerMetadata withAttribute(String key, String value) {
        Objects.requireNonNull(key, "attribute key must not be null");
        Map<String, String> updated = new LinkedHashMap<>(this.attributes);
        if (value == null) {
            updated.remove(key);
        } else {
            updated.put(key, value);
        }
        return new ContainerMetadata(this.containerType, this.source, this.destination, this.author, this.recipient, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, updated);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerMetadata that = (ContainerMetadata) o;
        return Objects.equals(containerType, that.containerType)
                && Objects.equals(source, that.source)
                && Objects.equals(destination, that.destination)
                && Objects.equals(author, that.author)
                && Objects.equals(recipient, that.recipient)
                && Objects.equals(assemblyContext, that.assemblyContext)
                && Objects.equals(interactionMechanism, that.interactionMechanism)
                && Objects.equals(envelopeStructure, that.envelopeStructure)
                && Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerType, source, destination, author, recipient, assemblyContext, interactionMechanism, envelopeStructure, attributes);
    }

    @Override
    public String toString() {
        return "ContainerMetadata{" +
                "containerType='" + containerType + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", author='" + author + '\'' +
                ", recipient='" + recipient + '\'' +
                ", interactionMechanism='" + interactionMechanism + '\'' +
                ", attributesCount=" + attributes.size() +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    /**
     * Fluent builder for {@link ContainerMetadata}.
     */
    public static final class Builder {
        private String containerType;
        private String source;
        private String destination;
        private String author;
        private String recipient;
        private String assemblyContext;
        private String interactionMechanism;
        private String envelopeStructure;
        private final Map<String, String> attributes = new LinkedHashMap<>();

        private Builder() {}

        public Builder containerType(String containerType) {
            this.containerType = containerType;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder recipient(String recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder assemblyContext(String assemblyContext) {
            this.assemblyContext = assemblyContext;
            return this;
        }

        public Builder interactionMechanism(String interactionMechanism) {
            this.interactionMechanism = interactionMechanism;
            return this;
        }

        public Builder envelopeStructure(String envelopeStructure) {
            this.envelopeStructure = envelopeStructure;
            return this;
        }

        public Builder attribute(String key, String value) {
            Objects.requireNonNull(key, "key must not be null");
            if (value != null) {
                this.attributes.put(key, value);
            } else {
                this.attributes.remove(key);
            }
            return this;
        }

        public Builder putAttribute(String key, String value) {
            return attribute(key, value);
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes.clear();
            if (attributes != null) {
                this.attributes.putAll(attributes);
            }
            return this;
        }

        public ContainerMetadata build() {
            return new ContainerMetadata(containerType, source, destination, author, recipient, assemblyContext, interactionMechanism, envelopeStructure, attributes);
        }
    }
}
