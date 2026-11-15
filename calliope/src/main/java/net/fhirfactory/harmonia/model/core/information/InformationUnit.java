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
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.topic.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * The foundational, indivisible unit of governed information within Harmonia.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit}):
 * <ul>
 *   <li>The <b>Information Unit (IU)</b> is Harmonia's indivisible unit of governed information.
 *       Its independence is logical and governed: it does not prescribe a single physical database row or network packet.</li>
 *   <li><b>IU Identity:</b> Composed of a persistent unique {@link UUID} and a deterministically derived
 *       canonical {@link Topic} with the pattern:
 *       <pre>{@code harmonia.mneme.iu.<hex-string-of-compressed-uuid>}</pre></li>
 *   <li><b>Authoritative Persisted Revision ({@code version}):</b> The authoritative persisted version established and
 *       returned by Mnemosyne. Strictly distinct from Mneme operational revision, cache Active Generation, and Content Structure Version.</li>
 *   <li><b>Singular Governed Representation:</b> An Information Unit contains <b>exactly one</b> governed
 *       {@link InformationRepresentation}. It SHALL NOT maintain alternative representations or conversion collections.
 *       A consumer requiring a differently structured representation creates a <b>new Information Unit</b>.</li>
 *   <li><b>Metadata Concerns ({@link InformationUnitMetadata}):</b>
 *     <ol>
 *       <li>{@link InformationCoordinates}: Four named Topic references (PersistenceGateway, GatewayEndpoint, InformationLocation, InformationOwner).
 *           <b>100% independent of SecurityContext</b>.</li>
 *       <li>{@link InformationItinerary}: Distinct conceptual information path and component participation concern with strongly typed Topics.</li>
 *       <li>{@link InformationSecurityContext}: IU-side required criteria, compartments, and action masks.</li>
 *       <li>{@link LifecycleContext}: Governed information lifecycle and persistence coordination.</li>
 *       <li>{@link ManagementContext}: Mneme operational working context (operational version, active generation).</li>
 *     </ol>
 *   </li>
 *   <li><b>IU Summary Projection Semantics:</b> The IU Summary is an operational projection of that <b>same
 *       Information Unit</b>, excluding <b>precisely {@code InformationRepresentation.Content}</b> while preserving all
 *       metadata contexts and the {@code contentReference} in {@link ContentMetadata}. The Summary is <b>not</b> a second
 *       entity or independently versioned record.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming &amp; Invariants:</b>
 * <ul>
 *   <li>{@code identifier}, {@code metadata}, and {@code representation} are guaranteed non-null.</li>
 *   <li>Immutable value object design ensures complete thread-safety across distributed components.</li>
 *   <li>Comprehensive validation enforces model invariants at construction time.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    private final InformationUnitIdentifier identifier;
    private final String version;
    private final InformationUnitMetadata metadata;
    private final InformationRepresentation representation;

    /**
     * Canonical constructor establishing validated Information Unit components.
     *
     * @param identifier     the persistent identity and canonical Topic, must not be null
     * @param version        authoritative persisted version established by Mnemosyne (optional)
     * @param metadata       the metadata contexts and relationships (optional, defaults to empty)
     * @param representation the single governed representation (optional, defaults to empty Summary)
     * @throws NullPointerException if {@code identifier} is null
     */
    @JsonCreator
    public InformationUnit(
            @JsonProperty("identifier") InformationUnitIdentifier identifier,
            @JsonProperty("version") String version,
            @JsonProperty("metadata") InformationUnitMetadata metadata,
            @JsonProperty("representation") InformationRepresentation representation) {
        this.identifier = Objects.requireNonNull(identifier, "identifier must not be null");
        this.version = version != null && !version.isBlank() ? version.trim() : null;
        this.metadata = metadata != null ? metadata : InformationUnitMetadata.empty();
        this.representation = representation != null ? representation : InformationRepresentation.builder().buildSummary();
    }

    /**
     * Compatibility constructor without explicit authoritative version.
     *
     * @param identifier     the persistent identity and canonical Topic, must not be null
     * @param metadata       metadata contexts and relationships (optional, defaults to empty)
     * @param representation single governed representation (optional, defaults to empty Summary)
     */
    public InformationUnit(
            InformationUnitIdentifier identifier,
            InformationUnitMetadata metadata,
            InformationRepresentation representation) {
        this(identifier, null, metadata, representation);
    }

    /**
     * Factory method creating an {@code InformationUnit} with specified UUID, metadata, and representation.
     *
     * @param uuid           the persistent UUID, must not be null
     * @param metadata       metadata contexts (optional, defaults to empty)
     * @param representation single governed representation (optional, defaults to empty Summary)
     * @return populated InformationUnit
     */
    public static InformationUnit create(
            UUID uuid,
            InformationUnitMetadata metadata,
            InformationRepresentation representation) {
        return new InformationUnit(InformationUnitIdentifier.fromUuid(uuid), null, metadata, representation);
    }

    /**
     * Factory method creating an {@code InformationUnit} with specified UUID, authoritative version, metadata, and representation.
     *
     * @param uuid           the persistent UUID, must not be null
     * @param version        authoritative persisted version
     * @param metadata       metadata contexts (optional, defaults to empty)
     * @param representation single governed representation (optional, defaults to empty Summary)
     * @return populated InformationUnit
     */
    public static InformationUnit create(
            UUID uuid,
            String version,
            InformationUnitMetadata metadata,
            InformationRepresentation representation) {
        return new InformationUnit(InformationUnitIdentifier.fromUuid(uuid), version, metadata, representation);
    }

    /**
     * Factory method creating an {@code InformationUnit} with a freshly generated random UUID.
     *
     * @param metadata       metadata contexts (optional, defaults to empty)
     * @param representation single governed representation (optional, defaults to empty Summary)
     * @return populated InformationUnit
     */
    public static InformationUnit random(
            InformationUnitMetadata metadata,
            InformationRepresentation representation) {
        return new InformationUnit(InformationUnitIdentifier.random(), null, metadata, representation);
    }

    /**
     * Creates a new builder for constructing {@code InformationUnit} instances.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    // =========================================================================
    // Core Accessors
    // =========================================================================

    /**
     * Returns the persistent identity and canonical Topic of the Information Unit.
     *
     * @return the identifier, never null
     */
    @JsonProperty("identifier")
    public InformationUnitIdentifier identifier() {
        return identifier;
    }

    public InformationUnitIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * Returns the persistent UUID of this Information Unit.
     *
     * @return the UUID, never null
     */
    @JsonIgnore
    public UUID uuid() {
        return identifier.uuid();
    }

    @JsonIgnore
    public UUID getUuid() {
        return identifier.uuid();
    }

    /**
     * Returns the canonical hierarchical Topic name for this Information Unit.
     *
     * @return canonical topic string, never null
     */
    @JsonIgnore
    public String topicName() {
        return identifier.topicName();
    }

    @JsonIgnore
    public String getTopicName() {
        return identifier.topicName();
    }

    /**
     * Returns the canonical {@link Topic} representation of this Information Unit identifier.
     *
     * @return canonical Topic instance
     */
    @JsonIgnore
    public Topic toTopic() {
        return identifier.toTopic();
    }

    /**
     * Returns the authoritative persisted version established by Mnemosyne.
     *
     * @return authoritative persisted version, or null if not yet persisted
     */
    @JsonProperty("version")
    public String version() {
        return version;
    }

    public String getVersion() {
        return version;
    }

    /**
     * Returns the metadata contexts and creator-supplied relationships.
     *
     * @return the metadata, never null
     */
    @JsonProperty("metadata")
    public InformationUnitMetadata metadata() {
        return metadata;
    }

    public InformationUnitMetadata getMetadata() {
        return metadata;
    }

    /**
     * Returns the single governed representation of this Information Unit.
     *
     * @return the representation, never null
     */
    @JsonProperty("representation")
    public InformationRepresentation representation() {
        return representation;
    }

    public InformationRepresentation getRepresentation() {
        return representation;
    }

    // =========================================================================
    // Operational Management Context Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public ManagementContext managementContext() {
        return metadata.managementContext();
    }

    @JsonIgnore
    public ManagementContext getManagementContext() {
        return metadata.managementContext();
    }

    @JsonIgnore
    public Optional<AuthoritativeVersion> operationalVersion() {
        return metadata.managementContext().operationalVersion();
    }

    @JsonIgnore
    public Optional<String> activeGeneration() {
        return metadata.managementContext().activeGeneration();
    }

    // =========================================================================
    // Information Coordinates Convenience Accessors (Strongly Typed Topics)
    // =========================================================================

    @JsonIgnore
    public InformationCoordinates informationCoordinates() {
        return metadata.informationCoordinates();
    }

    @JsonIgnore
    public InformationCoordinates getInformationCoordinates() {
        return metadata.informationCoordinates();
    }

    @JsonIgnore
    public Optional<Topic> persistenceGateway() {
        return metadata.informationCoordinates().persistenceGateway();
    }

    @JsonIgnore
    public Optional<Topic> gatewayEndpoint() {
        return metadata.informationCoordinates().gatewayEndpoint();
    }

    @JsonIgnore
    public Optional<Topic> informationLocation() {
        return metadata.informationCoordinates().informationLocation();
    }

    @JsonIgnore
    public Optional<Topic> informationOwner() {
        return metadata.informationCoordinates().informationOwner();
    }

    @JsonIgnore
    public Optional<String> persistenceGatewayName() {
        return metadata.informationCoordinates().persistenceGatewayName();
    }

    @JsonIgnore
    public Optional<String> gatewayEndpointName() {
        return metadata.informationCoordinates().gatewayEndpointName();
    }

    @JsonIgnore
    public Optional<String> informationLocationName() {
        return metadata.informationCoordinates().informationLocationName();
    }

    @JsonIgnore
    public Optional<String> informationOwnerName() {
        return metadata.informationCoordinates().informationOwnerName();
    }

    // =========================================================================
    // Information Itinerary Convenience Accessors (Strongly Typed Topics)
    // =========================================================================

    @JsonIgnore
    public InformationItinerary informationItinerary() {
        return metadata.informationItinerary();
    }

    @JsonIgnore
    public InformationItinerary getInformationItinerary() {
        return metadata.informationItinerary();
    }

    @JsonIgnore
    public Optional<Topic> sourceEntryComponent() {
        return metadata.informationItinerary().sourceEntryComponent();
    }

    @JsonIgnore
    public Optional<Topic> creationComponent() {
        return metadata.informationItinerary().creationComponent();
    }

    @JsonIgnore
    public Optional<Topic> previousComponent() {
        return metadata.informationItinerary().previousComponent();
    }

    @JsonIgnore
    public Optional<Topic> nextComponent() {
        return metadata.informationItinerary().nextComponent();
    }

    @JsonIgnore
    public Optional<Topic> targetExitComponent() {
        return metadata.informationItinerary().targetExitComponent();
    }

    @JsonIgnore
    public List<Topic> participatingComponents() {
        return metadata.informationItinerary().participatingComponents();
    }

    // =========================================================================
    // Security Context Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public InformationSecurityContext securityContext() {
        return metadata.securityContext();
    }

    @JsonIgnore
    public InformationSecurityContext getSecurityContext() {
        return metadata.securityContext();
    }

    // =========================================================================
    // Lifecycle Context Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public LifecycleContext lifecycleContext() {
        return metadata.lifecycleContext();
    }

    @JsonIgnore
    public LifecycleContext getLifecycleContext() {
        return metadata.lifecycleContext();
    }

    // =========================================================================
    // Relationships Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public List<InformationUnitRelationship> relationships() {
        return metadata.relationships();
    }

    @JsonIgnore
    public List<InformationUnitRelationship> getRelationships() {
        return metadata.relationships();
    }

    // =========================================================================
    // Representation Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public ContainerMetadata containerMetadata() {
        return representation.containerMetadata();
    }

    @JsonIgnore
    public ContainerMetadata getContainerMetadata() {
        return representation.containerMetadata();
    }

    @JsonIgnore
    public ContentMetadata contentMetadata() {
        return representation.contentMetadata();
    }

    @JsonIgnore
    public ContentMetadata getContentMetadata() {
        return representation.contentMetadata();
    }

    @JsonIgnore
    public Optional<Topic> contentStructureTopic() {
        return representation.contentMetadata().contentStructureTopic();
    }

    @JsonIgnore
    public Optional<String> contentStructureTopicName() {
        return representation.contentMetadata().contentStructureTopicName();
    }

    @JsonIgnore
    public Optional<String> contentStructureVersion() {
        return representation.contentMetadata().contentStructureVersion();
    }

    @JsonIgnore
    public Optional<Object> content() {
        return representation.content();
    }

    @JsonIgnore
    public Object getContent() {
        return representation.getContent();
    }

    @JsonIgnore
    public Optional<String> contentAsString() {
        return representation.contentAsString();
    }

    @JsonIgnore
    public Optional<byte[]> contentAsBytes() {
        return representation.contentAsBytes();
    }

    @JsonIgnore
    public <T> Optional<T> contentAs(Class<T> type) {
        return representation.contentAs(type);
    }

    @JsonIgnore
    public boolean hasContent() {
        return representation.hasContent();
    }

    @JsonIgnore
    public boolean isSummary() {
        return representation.isSummary();
    }

    // =========================================================================
    // Summary Projection Semantics
    // =========================================================================

    /**
     * Projects this Information Unit to its <b>Information Unit Summary</b>.
     * <p>
     * In accordance with {@code DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md} (FC-01–FC-05):
     * <ul>
     *   <li>The Summary is an operational projection of the <b>same Information Unit</b>.</li>
     *   <li>Excludes <b>precisely {@code InformationRepresentation.Content}</b>.</li>
     *   <li>Preserves all metadata contexts, relationships, {@link ContainerMetadata},
     *       and {@link ContentMetadata} (crucially including {@code contentReference}).</li>
     *   <li>The Summary is not an independently versioned or separate entity.</li>
     * </ul>
     *
     * @return Summary projection of this Information Unit
     */
    public InformationUnit toSummary() {
        if (isSummary()) {
            return this;
        }
        return new InformationUnit(this.identifier, this.version, this.metadata, this.representation.toSummary());
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public InformationUnit withVersion(String version) {
        return new InformationUnit(this.identifier, version, this.metadata, this.representation);
    }

    public InformationUnit withVersion(AuthoritativeVersion version) {
        return new InformationUnit(this.identifier, version != null ? version.value() : null, this.metadata, this.representation);
    }

    public InformationUnit withMetadata(InformationUnitMetadata metadata) {
        return new InformationUnit(this.identifier, this.version, metadata, this.representation);
    }

    public InformationUnit withRepresentation(InformationRepresentation representation) {
        return new InformationUnit(this.identifier, this.version, this.metadata, representation);
    }

    public InformationUnit withManagementContext(ManagementContext managementContext) {
        return new InformationUnit(this.identifier, this.version, this.metadata.withManagementContext(managementContext), this.representation);
    }

    public InformationUnit withInformationCoordinates(InformationCoordinates informationCoordinates) {
        return new InformationUnit(this.identifier, this.version, this.metadata.withInformationCoordinates(informationCoordinates), this.representation);
    }

    public InformationUnit withInformationItinerary(InformationItinerary informationItinerary) {
        return new InformationUnit(this.identifier, this.version, this.metadata.withInformationItinerary(informationItinerary), this.representation);
    }

    public InformationUnit withSecurityContext(InformationSecurityContext securityContext) {
        return new InformationUnit(this.identifier, this.version, this.metadata.withSecurityContext(securityContext), this.representation);
    }

    public InformationUnit withLifecycleContext(LifecycleContext lifecycleContext) {
        return new InformationUnit(this.identifier, this.version, this.metadata.withLifecycleContext(lifecycleContext), this.representation);
    }

    public InformationUnit withRelationships(Collection<InformationUnitRelationship> relationships) {
        return new InformationUnit(this.identifier, this.version, this.metadata.withRelationships(relationships), this.representation);
    }

    public InformationUnit withRelationship(InformationUnitRelationship relationship) {
        return new InformationUnit(this.identifier, this.version, this.metadata.withRelationship(relationship), this.representation);
    }

    public InformationUnit withContent(Object content) {
        return new InformationUnit(this.identifier, this.version, this.metadata, this.representation.withContent(content));
    }

    public InformationUnit withContentMetadata(ContentMetadata contentMetadata) {
        return new InformationUnit(this.identifier, this.version, this.metadata, this.representation.withContentMetadata(contentMetadata));
    }

    public InformationUnit withContainerMetadata(ContainerMetadata containerMetadata) {
        return new InformationUnit(this.identifier, this.version, this.metadata, this.representation.withContainerMetadata(containerMetadata));
    }

    // =========================================================================
    // Validation Helper
    // =========================================================================

    /**
     * Validates that this Information Unit satisfies foundational model invariants.
     *
     * @return true if valid
     * @throws IllegalStateException if any architectural invariant is violated
     */
    public boolean validate() {
        if (identifier == null) {
            throw new IllegalStateException("InformationUnit must have a non-null identifier");
        }
        if (identifier.uuid() == null) {
            throw new IllegalStateException("InformationUnit identifier must have a non-null UUID");
        }
        if (identifier.topicName() == null || identifier.topicName().isBlank()) {
            throw new IllegalStateException("InformationUnit identifier must have a non-blank topic name");
        }
        if (metadata == null) {
            throw new IllegalStateException("InformationUnit must have non-null metadata");
        }
        if (representation == null) {
            throw new IllegalStateException("InformationUnit must have exactly one non-null representation");
        }
        return true;
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationUnit that = (InformationUnit) o;
        return identifier.equals(that.identifier) &&
                Objects.equals(version, that.version) &&
                metadata.equals(that.metadata) &&
                representation.equals(that.representation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, version, metadata, representation);
    }

    @Override
    public String toString() {
        return "InformationUnit{" +
                "id=" + identifier +
                (version != null ? ", version='" + version + '\'' : "") +
                ", isSummary=" + isSummary() +
                ", metadata=" + metadata +
                ", representation=" + representation +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static final class Builder {

        private InformationUnitIdentifier identifier;
        private String version;
        private InformationUnitMetadata metadata;
        private InformationRepresentation representation;

        private InformationCoordinates coordinates;
        private InformationItinerary itinerary;
        private InformationSecurityContext securityContext;
        private LifecycleContext lifecycleContext;
        private ManagementContext managementContext;
        private final List<InformationUnitRelationship> relationships = new ArrayList<>();

        private ContainerMetadata containerMetadata;
        private ContentMetadata contentMetadata;
        private Object content;

        public Builder identifier(InformationUnitIdentifier identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder identifier(UUID uuid) {
            this.identifier = InformationUnitIdentifier.fromUuid(uuid);
            return this;
        }

        public Builder uuid(UUID uuid) {
            this.identifier = InformationUnitIdentifier.fromUuid(uuid);
            return this;
        }

        public Builder randomIdentifier() {
            this.identifier = InformationUnitIdentifier.random();
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder version(AuthoritativeVersion version) {
            this.version = version != null ? version.value() : null;
            return this;
        }

        public Builder metadata(InformationUnitMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder representation(InformationRepresentation representation) {
            this.representation = representation;
            return this;
        }

        public Builder coordinates(InformationCoordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder informationCoordinates(InformationCoordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder itinerary(InformationItinerary itinerary) {
            this.itinerary = itinerary;
            return this;
        }

        public Builder informationItinerary(InformationItinerary itinerary) {
            this.itinerary = itinerary;
            return this;
        }

        public Builder securityContext(InformationSecurityContext securityContext) {
            this.securityContext = securityContext;
            return this;
        }

        public Builder lifecycleContext(LifecycleContext lifecycleContext) {
            this.lifecycleContext = lifecycleContext;
            return this;
        }

        public Builder managementContext(ManagementContext managementContext) {
            this.managementContext = managementContext;
            return this;
        }

        public Builder addRelationship(InformationUnitRelationship relationship) {
            if (relationship != null) {
                this.relationships.add(relationship);
            }
            return this;
        }

        public Builder relationships(Collection<InformationUnitRelationship> relationships) {
            this.relationships.clear();
            if (relationships != null) {
                this.relationships.addAll(relationships);
            }
            return this;
        }

        public Builder containerMetadata(ContainerMetadata containerMetadata) {
            this.containerMetadata = containerMetadata;
            return this;
        }

        public Builder contentMetadata(ContentMetadata contentMetadata) {
            this.contentMetadata = contentMetadata;
            return this;
        }

        public Builder content(Object content) {
            this.content = content;
            return this;
        }

        public InformationUnit buildSummary() {
            this.content = null;
            return build();
        }

        public InformationUnit build() {
            InformationUnitIdentifier id = identifier != null ? identifier : InformationUnitIdentifier.random();
            InformationUnitMetadata meta = metadata;
            if (meta == null) {
                meta = new InformationUnitMetadata(
                        managementContext != null ? managementContext : ManagementContext.empty(),
                        coordinates != null ? coordinates : InformationCoordinates.empty(),
                        itinerary != null ? itinerary : InformationItinerary.empty(),
                        securityContext != null ? securityContext : InformationSecurityContext.empty(),
                        lifecycleContext != null ? lifecycleContext : LifecycleContext.empty(),
                        relationships
                );
            }
            InformationRepresentation rep = representation;
            if (rep == null) {
                rep = new InformationRepresentation(
                        containerMetadata != null ? containerMetadata : ContainerMetadata.empty(),
                        contentMetadata != null ? contentMetadata : ContentMetadata.empty(),
                        content
                );
            }
            return new InformationUnit(id, version, meta, rep);
        }
    }
}
