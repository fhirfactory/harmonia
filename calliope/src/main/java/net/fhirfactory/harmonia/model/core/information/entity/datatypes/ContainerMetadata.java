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

package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.information.entity.factory.ContainerMetadataBuilder;
import net.fhirfactory.harmonia.model.core.naming.Topic;

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
    @JsonCreator
    public ContainerMetadata(
            @JsonProperty("type") Topic type,
            @JsonProperty("sourceTopic") Topic sourceTopic,
            @JsonProperty("destinationTopic") Topic destinationTopic,
            @JsonProperty("authorTopic") Topic authorTopic,
            @JsonProperty("recipientTopic") Topic recipientTopic,
            @JsonProperty("assemblyContext") String assemblyContext,
            @JsonProperty("interactionMechanism") String interactionMechanism,
            @JsonProperty("envelopeStructure") String envelopeStructure,
            @JsonProperty("attributes") Map<String, String> attributes) {
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
    public static ContainerMetadataBuilder builder() {
        return new ContainerMetadataBuilder();
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


    public ContainerMetadata withSourceTopic(Topic sourceTopic) {
        return new ContainerMetadata(this.type, sourceTopic, this.destinationTopic, this.authorTopic, this.recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }


    public ContainerMetadata withDestinationTopic(Topic destinationTopic) {
        return new ContainerMetadata(this.type, this.sourceTopic, destinationTopic, this.authorTopic, this.recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }


    public ContainerMetadata withAuthorTopic(Topic authorTopic) {
        return new ContainerMetadata(this.type, this.sourceTopic, this.destinationTopic, authorTopic, this.recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
    }


    public ContainerMetadata withRecipientTopic(Topic recipientTopic) {
        return new ContainerMetadata(this.type, this.sourceTopic, this.destinationTopic, this.authorTopic, recipientTopic, this.assemblyContext, this.interactionMechanism, this.envelopeStructure, this.attributes);
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
                "typeId=" + shortId(type) +
                ", sourceId=" + shortId(sourceTopic) +
                ", destinationId=" + shortId(destinationTopic) +
                ", authorId=" + shortId(authorTopic) +
                ", recipientId=" + shortId(recipientTopic) +
                ", attributeCount=" + attributes.size() +
                '}';
    }

    private static String shortId(Topic topic) {
        return topic != null ? topic.getShortId() : "none";
    }

}
