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
import net.fhirfactory.harmonia.model.topic.Topic;

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
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit})
 * and {@code DRP-Information-Unit-Framework.Final.002.md} (§§1, 5):
 * <ul>
 *   <li>{@code ContainerMetadata} describes the container or transport envelope associated with the IU's representation.</li>
 *   <li><b>Topic-Valued Classification:</b> In accordance with the fundamental Harmonia architectural principle,
 *       {@link #type()} is a strongly typed {@link Topic} (e.g. {@code Harmonia.Container.FHIR.Bundle},
 *       {@code Harmonia.Container.HL7.Message}, {@code Harmonia.Container.Document}, {@code Harmonia.Container.Pragma}).</li>
 *   <li><b>Component and Endpoint References:</b> Where source, destination, author, or recipient represent governed
 *       Harmonia components, systems, or endpoints, they are represented as strongly typed {@link Topic} references.</li>
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

    public static final Topic TYPE_FHIR_BUNDLE = Topic.fromName("Harmonia.Container.FHIR.Bundle");
    public static final Topic TYPE_HL7_MESSAGE = Topic.fromName("Harmonia.Container.HL7.Message");
    public static final Topic TYPE_DOCUMENT = Topic.fromName("Harmonia.Container.Document");
    public static final Topic TYPE_PRAGMA = Topic.fromName("Harmonia.Container.Pragma");
    public static final Topic TYPE_EVENT = Topic.fromName("Harmonia.Container.Event");

    private static final ContainerMetadata EMPTY = new ContainerMetadata((Topic) null, (Topic) null, (Topic) null, (Topic) null, (Topic) null, null, null, null, Map.of());

    private final Topic type;
    private final Topic sourceTopic;
    private final Topic destinationTopic;
    private final Topic authorTopic;
    private final Topic recipientTopic;
    private final String assemblyContext;
    private final String interactionMechanism;
    private final String envelopeStructure;
    private final Map<String, String> attributes;

    /**
     * Canonical constructor establishing validated container metadata with strongly typed Topic entities.
     *
     * @param type                 Topic classifying the container envelope (optional)
     * @param sourceTopic          Topic describing the source gateway or system (optional)
     * @param destinationTopic     Topic describing the target destination (optional)
     * @param authorTopic          Topic describing the author/sender (optional)
     * @param recipientTopic       Topic describing the recipient (optional)
     * @param assemblyContext      assembly context (optional)
     * @param interactionMechanism interaction mechanism (e.g. "MLLP", "REST") (optional)
     * @param envelopeStructure    envelope structure definition (optional)
     * @param attributes           additional container attributes (optional)
     */
    public ContainerMetadata(
            Topic type,
            Topic sourceTopic,
            Topic destinationTopic,
            Topic authorTopic,
            Topic recipientTopic,
            String assemblyContext,
            String interactionMechanism,
            String envelopeStructure,
            Map<String, String> attributes) {
        this.type = type;
        this.sourceTopic = sourceTopic;
        this.destinationTopic = destinationTopic;
        this.authorTopic = authorTopic;
        this.recipientTopic = recipientTopic;
        this.assemblyContext = sanitize(assemblyContext);
        this.interactionMechanism = sanitize(interactionMechanism);
        this.envelopeStructure = sanitize(envelopeStructure);
        this.attributes = attributes == null || attributes.isEmpty()
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
    }

    /**
     * Flexible creator handling both strongly-typed Topics and legacy String properties.
     */
    @JsonCreator
    public ContainerMetadata(
            @JsonProperty("type") Topic type,
            @JsonProperty("containerType") String containerType,
            @JsonProperty("sourceTopic") Topic sourceTopic,
            @JsonProperty("source") String source,
            @JsonProperty("destinationTopic") Topic destinationTopic,
            @JsonProperty("destination") String destination,
            @JsonProperty("authorTopic") Topic authorTopic,
            @JsonProperty("author") String author,
            @JsonProperty("recipientTopic") Topic recipientTopic,
            @JsonProperty("recipient") String recipient,
            @JsonProperty("assemblyContext") String assemblyContext,
            @JsonProperty("interactionMechanism") String interactionMechanism,
            @JsonProperty("envelopeStructure") String envelopeStructure,
            @JsonProperty("attributes") Map<String, String> attributes) {
        this(
                type != null ? type : (containerType != null && !containerType.isBlank() ? Topic.fromName(containerType.trim()) : null),
                sourceTopic != null ? sourceTopic : (source != null && !source.isBlank() ? Topic.fromName(source.trim()) : null),
                destinationTopic != null ? destinationTopic : (destination != null && !destination.isBlank() ? Topic.fromName(destination.trim()) : null),
                authorTopic != null ? authorTopic : (author != null && !author.isBlank() ? Topic.fromName(author.trim()) : null),
                recipientTopic != null ? recipientTopic : (recipient != null && !recipient.isBlank() ? Topic.fromName(recipient.trim()) : null),
                assemblyContext,
                interactionMechanism,
                envelopeStructure,
                attributes
        );
    }

    /**
     * Backward-compatible constructor accepting String identifiers.
     */
    public ContainerMetadata(
            String containerType,
            String source,
            String destination,
            String author,
            String recipient,
            String assemblyContext,
            String interactionMechanism,
            String envelopeStructure,
            Map<String, String> attributes) {
        this(
                containerType != null && !containerType.isBlank() ? Topic.fromName(containerType.trim()) : null,
                null,
                source != null && !source.isBlank() ? Topic.fromName(source.trim()) : null,
                source,
                destination != null && !destination.isBlank() ? Topic.fromName(destination.trim()) : null,
                destination,
                author != null && !author.isBlank() ? Topic.fromName(author.trim()) : null,
                author,
                recipient != null && !recipient.isBlank() ? Topic.fromName(recipient.trim()) : null,
                recipient,
                assemblyContext,
                interactionMechanism,
                envelopeStructure,
                attributes
        );
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

    /**
     * Returns the strongly typed {@link Topic} classifying the container envelope.
     *
     * @return the container type Topic, or null if unspecified
     */
    @JsonProperty("type")
    public Topic type() {
        return type;
    }

    public Topic getType() {
        return type;
    }

    @JsonIgnore
    public Optional<String> containerType() {
        return type != null ? Optional.of(type.getName()) : Optional.empty();
    }

    @JsonProperty("containerType")
    public String getContainerType() {
        return type != null ? type.getName() : null;
    }

    /**
     * Returns the strongly typed {@link Topic} identifying the source system or gateway.
     *
     * @return optional source Topic
     */
    @JsonIgnore
    public Optional<Topic> sourceTopic() {
        return Optional.ofNullable(sourceTopic);
    }

    @JsonProperty("sourceTopic")
    public Topic getSourceTopic() {
        return sourceTopic;
    }

    @JsonIgnore
    public Optional<String> source() {
        return sourceTopic != null ? Optional.of(sourceTopic.getName()) : Optional.empty();
    }

    @JsonProperty("source")
    public String getSource() {
        return sourceTopic != null ? sourceTopic.getName() : null;
    }

    /**
     * Returns the strongly typed {@link Topic} identifying the target destination.
     *
     * @return optional destination Topic
     */
    @JsonIgnore
    public Optional<Topic> destinationTopic() {
        return Optional.ofNullable(destinationTopic);
    }

    @JsonProperty("destinationTopic")
    public Topic getDestinationTopic() {
        return destinationTopic;
    }

    @JsonIgnore
    public Optional<String> destination() {
        return destinationTopic != null ? Optional.of(destinationTopic.getName()) : Optional.empty();
    }

    @JsonProperty("destination")
    public String getDestination() {
        return destinationTopic != null ? destinationTopic.getName() : null;
    }

    /**
     * Returns the strongly typed {@link Topic} identifying the author or sender.
     *
     * @return optional author Topic
     */
    @JsonIgnore
    public Optional<Topic> authorTopic() {
        return Optional.ofNullable(authorTopic);
    }

    @JsonProperty("authorTopic")
    public Topic getAuthorTopic() {
        return authorTopic;
    }

    @JsonIgnore
    public Optional<String> author() {
        return authorTopic != null ? Optional.of(authorTopic.getName()) : Optional.empty();
    }

    @JsonProperty("author")
    public String getAuthor() {
        return authorTopic != null ? authorTopic.getName() : null;
    }

    /**
     * Returns the strongly typed {@link Topic} identifying the intended recipient.
     *
     * @return optional recipient Topic
     */
    @JsonIgnore
    public Optional<Topic> recipientTopic() {
        return Optional.ofNullable(recipientTopic);
    }

    @JsonProperty("recipientTopic")
    public Topic getRecipientTopic() {
        return recipientTopic;
    }

    @JsonIgnore
    public Optional<String> recipient() {
        return recipientTopic != null ? Optional.of(recipientTopic.getName()) : Optional.empty();
    }

    @JsonProperty("recipient")
    public String getRecipient() {
        return recipientTopic != null ? recipientTopic.getName() : null;
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

    public ContainerMetadata withType(Topic type) {
        return new ContainerMetadata(type, this.sourceTopic, this.destinationTopic, this.authorTopic, this.recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withContainerType(String containerType) {
        Topic newType = containerType != null && !containerType.isBlank() ? Topic.fromName(containerType.trim()) : null;
        return withType(newType);
    }

    public ContainerMetadata withSourceTopic(Topic sourceTopic) {
        return new ContainerMetadata(this.type, sourceTopic, this.destinationTopic, this.authorTopic, this.recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withSource(String source) {
        Topic s = source != null && !source.isBlank() ? Topic.fromName(source.trim()) : null;
        return withSourceTopic(s);
    }

    public ContainerMetadata withDestinationTopic(Topic destinationTopic) {
        return new ContainerMetadata(this.type, this.sourceTopic, destinationTopic, this.authorTopic, this.recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withDestination(String destination) {
        Topic d = destination != null && !destination.isBlank() ? Topic.fromName(destination.trim()) : null;
        return withDestinationTopic(d);
    }

    public ContainerMetadata withAuthorTopic(Topic authorTopic) {
        return new ContainerMetadata(this.type, this.sourceTopic, this.destinationTopic, authorTopic, this.recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withAuthor(String author) {
        Topic a = author != null && !author.isBlank() ? Topic.fromName(author.trim()) : null;
        return withAuthorTopic(a);
    }

    public ContainerMetadata withRecipientTopic(Topic recipientTopic) {
        return new ContainerMetadata(this.type, this.sourceTopic, this.destinationTopic, this.authorTopic, recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withRecipient(String recipient) {
        Topic r = recipient != null && !recipient.isBlank() ? Topic.fromName(recipient.trim()) : null;
        return withRecipientTopic(r);
    }

    public ContainerMetadata withAssemblyContext(String assemblyContext) {
        return new ContainerMetadata(this.type, this.sourceTopic, this.destinationTopic, this.authorTopic, this.recipientTopic, assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withInteractionMechanism(String interactionMechanism) {
        return new ContainerMetadata(this.type, this.sourceTopic, this.destinationTopic, this.authorTopic, this.recipientTopic, this.assemblyContext, interactionMechanism, this.envelopeStructure, this.attributes);
    }

    public ContainerMetadata withEnvelopeStructure(String envelopeStructure) {
        return new ContainerMetadata(this.type, this.sourceTopic, this.destinationTopic, this.authorTopic, this.recipientTopic, this.assemblyContext, this.interactionMechanism, envelopeStructure, this.attributes);
    }

    public ContainerMetadata withAttributes(Map<String, String> attributes) {
        return new ContainerMetadata(this.type, this.sourceTopic, this.destinationTopic, this.authorTopic, this.recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, attributes);
    }

    public ContainerMetadata withAttribute(String key, String value) {
        Objects.requireNonNull(key, "attribute key must not be null");
        Map<String, String> updated = new LinkedHashMap<>(this.attributes);
        if (value == null) {
            updated.remove(key);
        } else {
            updated.put(key, value);
        }
        return new ContainerMetadata(this.type, this.sourceTopic, this.destinationTopic, this.authorTopic, this.recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, updated);
    }

    // =========================================================================
    // Identity and Equality
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerMetadata that = (ContainerMetadata) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(sourceTopic, that.sourceTopic) &&
                Objects.equals(destinationTopic, that.destinationTopic) &&
                Objects.equals(authorTopic, that.authorTopic) &&
                Objects.equals(recipientTopic, that.recipientTopic) &&
                Objects.equals(assemblyContext, that.assemblyContext) &&
                Objects.equals(interactionMechanism, that.interactionMechanism) &&
                Objects.equals(envelopeStructure, that.envelopeStructure) &&
                Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, sourceTopic, destinationTopic, authorTopic, recipientTopic,
                assemblyContext, interactionMechanism, envelopeStructure, attributes);
    }

    @Override
    public String toString() {
        return "ContainerMetadata{" +
                "type=" + type +
                ", source=" + sourceTopic +
                ", destination=" + destinationTopic +
                ", author=" + authorTopic +
                ", recipient=" + recipientTopic +
                ", assemblyContext='" + assemblyContext + '\'' +
                ", interactionMechanism='" + interactionMechanism + '\'' +
                ", envelopeStructure='" + envelopeStructure + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static final class Builder {
        private Topic type;
        private Topic sourceTopic;
        private Topic destinationTopic;
        private Topic authorTopic;
        private Topic recipientTopic;
        private String assemblyContext;
        private String interactionMechanism;
        private String envelopeStructure;
        private final Map<String, String> attributes = new LinkedHashMap<>();

        private Builder() {}

        public Builder type(Topic type) {
            this.type = type;
            return this;
        }

        public Builder containerType(String containerType) {
            this.type = containerType != null && !containerType.isBlank() ? Topic.fromName(containerType.trim()) : null;
            return this;
        }

        public Builder sourceTopic(Topic sourceTopic) {
            this.sourceTopic = sourceTopic;
            return this;
        }

        public Builder source(String source) {
            this.sourceTopic = source != null && !source.isBlank() ? Topic.fromName(source.trim()) : null;
            return this;
        }

        public Builder destinationTopic(Topic destinationTopic) {
            this.destinationTopic = destinationTopic;
            return this;
        }

        public Builder destination(String destination) {
            this.destinationTopic = destination != null && !destination.isBlank() ? Topic.fromName(destination.trim()) : null;
            return this;
        }

        public Builder authorTopic(Topic authorTopic) {
            this.authorTopic = authorTopic;
            return this;
        }

        public Builder author(String author) {
            this.authorTopic = author != null && !author.isBlank() ? Topic.fromName(author.trim()) : null;
            return this;
        }

        public Builder recipientTopic(Topic recipientTopic) {
            this.recipientTopic = recipientTopic;
            return this;
        }

        public Builder recipient(String recipient) {
            this.recipientTopic = recipient != null && !recipient.isBlank() ? Topic.fromName(recipient.trim()) : null;
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

        public Builder attributes(Map<String, String> attributes) {
            this.attributes.clear();
            if (attributes != null) {
                this.attributes.putAll(attributes);
            }
            return this;
        }

        public Builder putAttribute(String key, String value) {
            return attribute(key, value);
        }

        public Builder attribute(String key, String value) {
            if (key != null) {
                if (value == null) {
                    this.attributes.remove(key);
                } else {
                    this.attributes.put(key, value);
                }
            }
            return this;
        }

        public ContainerMetadata build() {
            return new ContainerMetadata(
                    type,
                    sourceTopic,
                    destinationTopic,
                    authorTopic,
                    recipientTopic,
                    assemblyContext,
                    interactionMechanism,
                    envelopeStructure,
                    attributes);
        }
    }
}
