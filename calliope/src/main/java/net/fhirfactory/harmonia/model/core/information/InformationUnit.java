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
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * The foundational, indivisible unit of governed information within Harmonia.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the Harmonia Architecture Continuum (1.1.22/1.1.24) and
 * {@code DRP-Information-Unit-Framework.Final.001.md} (§§2–9; IU-01 through IU-13):
 * <ul>
 *   <li>The <b>Information Unit (IU)</b> is Harmonia's indivisible unit of governed information.
 *       Its independence is logical and governed: it does not prescribe a single physical database row or network packet.</li>
 *   <li><b>IU Identity (IU-01, IU-06):</b> Composed of a persistent unique {@link UUID} and a deterministically derived
 *       canonical {@link Topic} with the pattern:
 *       <pre>{@code harmonia.mneme.iu.<hex-string-of-compressed-uuid>}</pre></li>
 *   <li><b>Singular Governed Representation (IU-02):</b> An Information Unit contains <b>exactly one</b> governed
 *       {@link InformationRepresentation}. It SHALL NOT maintain alternative representations or conversion collections.
 *       A consumer requiring a differently structured representation creates a <b>new Information Unit</b>.</li>
 *   <li><b>Four Metadata Contexts (IU-03, IU-04, IU-05):</b>
 *     <ol>
 *       <li>{@link ManagementContext}: Mneme operational working information (operational version, active generation).</li>
 *       <li>{@link InformationCoordinates}: Four named Topic references (PersistenceGateway, GatewayEndpoint, InformationLocation, InformationOwner).
 *           <b>100% independent of SecurityContext</b>.</li>
 *       <li>{@link InformationSecurityContext}: IU-side required criteria, compartments, and action masks.</li>
 *       <li>{@link LifecycleContext}: Mneme–Mnemosyne lifecycle and persistence coordination.</li>
 *     </ol>
 *   </li>
 *   <li><b>IU Summary Projection Semantics (FC-01–FC-05):</b> The IU Summary is an operation projection of that <b>same
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
    private final InformationUnitMetadata metadata;
    private final InformationRepresentation representation;

    /**
     * Canonical constructor establishing validated Information Unit components.
     *
     * @param identifier     the persistent identity and canonical Topic, must not be null
     * @param metadata       the four metadata contexts and relationships (optional, defaults to empty)
     * @param representation the single governed representation (optional, defaults to empty Summary)
     * @throws NullPointerException if {@code identifier} is null
     */
    @JsonCreator
    public InformationUnit(
            @JsonProperty("identifier") InformationUnitIdentifier identifier,
            @JsonProperty("metadata") InformationUnitMetadata metadata,
            @JsonProperty("representation") InformationRepresentation representation) {
        this.identifier = Objects.requireNonNull(identifier, "identifier must not be null");
        this.metadata = metadata != null ? metadata : InformationUnitMetadata.empty();
        this.representation = representation != null ? representation : InformationRepresentation.builder().buildSummary();
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
        return new InformationUnit(InformationUnitIdentifier.fromUuid(uuid), metadata, representation);
    }

    /**
     * Factory method creating an {@code InformationUnit} with specified identifier, metadata, and representation.
     *
     * @param identifier     identifier, must not be null
     * @param metadata       metadata contexts (optional, defaults to empty)
     * @param representation single governed representation (optional, defaults to empty Summary)
     * @return populated InformationUnit
     */
    public static InformationUnit create(
            InformationUnitIdentifier identifier,
            InformationUnitMetadata metadata,
            InformationRepresentation representation) {
        return new InformationUnit(identifier, metadata, representation);
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
     * Returns the persistent identifier and canonical Topic.
     *
     * @return the identifier, never null
     */
    @JsonProperty("identifier")
    public InformationUnitIdentifier identifier() {
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

    /**
     * Returns the canonical hierarchical Topic name (e.g. {@code "harmonia.mneme.iu.<hex>"}).
     *
     * @return canonical topic name, never null
     */
    @JsonIgnore
    public String topicName() {
        return identifier.topicName();
    }

    /**
     * Returns the Calliope {@link Topic} representation of this Information Unit.
     *
     * @return Calliope Topic, never null
     */
    @JsonIgnore
    public Topic toTopic() {
        return identifier.toTopic();
    }

    /**
     * Returns the four metadata contexts and relationships.
     *
     * @return metadata contexts, never null
     */
    @JsonProperty("metadata")
    public InformationUnitMetadata metadata() {
        return metadata;
    }

    /**
     * Returns the single governed representation.
     *
     * @return the single governed representation, never null
     */
    @JsonProperty("representation")
    public InformationRepresentation representation() {
        return representation;
    }

    // =========================================================================
    // Delegated Metadata Context Accessors
    // =========================================================================

    @JsonIgnore
    public ManagementContext managementContext() {
        return metadata.managementContext();
    }

    @JsonIgnore
    public InformationCoordinates informationCoordinates() {
        return metadata.informationCoordinates();
    }

    @JsonIgnore
    public InformationSecurityContext securityContext() {
        return metadata.securityContext();
    }

    @JsonIgnore
    public LifecycleContext lifecycleContext() {
        return metadata.lifecycleContext();
    }

    @JsonIgnore
    public List<InformationUnitRelationship> relationships() {
        return metadata.relationships();
    }

    // =========================================================================
    // Delegated Representation & Content Accessors
    // =========================================================================

    @JsonIgnore
    public ContainerMetadata containerMetadata() {
        return representation.containerMetadata();
    }

    @JsonIgnore
    public ContentMetadata contentMetadata() {
        return representation.contentMetadata();
    }

    @JsonIgnore
    public Optional<Object> content() {
        return representation.content();
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

    // =========================================================================
    // Summary Projection Semantics
    // =========================================================================

    /**
     * Derives an Information Unit Summary projection of this same Information Unit,
     * <b>excluding precisely {@code InformationRepresentation.Content}</b> while retaining
     * all metadata contexts and {@link ContentMetadata} (including any {@code contentReference}).
     *
     * @return InformationUnit in Summary projection
     */
    public InformationUnit toSummary() {
        if (this.isSummary()) {
            return this;
        }
        return new InformationUnit(this.identifier, this.metadata, this.representation.toSummary());
    }

    /**
     * Indicates whether this Information Unit is currently in Summary projection (content excluded).
     *
     * @return true if content is excluded (Summary projection), false if content is present
     */
    @JsonIgnore
    public boolean isSummary() {
        return representation.isSummary();
    }

    /**
     * Indicates whether actual represented content is present in this Information Unit.
     *
     * @return true if content is present, false if excluded (Summary projection)
     */
    @JsonIgnore
    public boolean hasContent() {
        return representation.hasContent();
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    /**
     * Derives an immutable copy with an updated or hydrated content payload.
     *
     * @param content the new payload content
     * @return new InformationUnit with content
     */
    public InformationUnit withContent(Object content) {
        return new InformationUnit(this.identifier, this.metadata, this.representation.withContent(content));
    }

    public InformationUnit withMetadata(InformationUnitMetadata metadata) {
        return new InformationUnit(this.identifier, metadata, this.representation);
    }

    public InformationUnit withManagementContext(ManagementContext managementContext) {
        return new InformationUnit(this.identifier, this.metadata.withManagementContext(managementContext), this.representation);
    }

    public InformationUnit withInformationCoordinates(InformationCoordinates coordinates) {
        return new InformationUnit(this.identifier, this.metadata.withInformationCoordinates(coordinates), this.representation);
    }

    public InformationUnit withSecurityContext(InformationSecurityContext securityContext) {
        return new InformationUnit(this.identifier, this.metadata.withSecurityContext(securityContext), this.representation);
    }

    public InformationUnit withLifecycleContext(LifecycleContext lifecycleContext) {
        return new InformationUnit(this.identifier, this.metadata.withLifecycleContext(lifecycleContext), this.representation);
    }

    public InformationUnit withRelationships(Collection<InformationUnitRelationship> relationships) {
        return new InformationUnit(this.identifier, this.metadata.withRelationships(relationships), this.representation);
    }

    public InformationUnit withRelationship(InformationUnitRelationship relationship) {
        return new InformationUnit(this.identifier, this.metadata.withRelationship(relationship), this.representation);
    }

    public InformationUnit withRepresentation(InformationRepresentation representation) {
        return new InformationUnit(this.identifier, this.metadata, representation);
    }

    public InformationUnit withContainerMetadata(ContainerMetadata containerMetadata) {
        return new InformationUnit(this.identifier, this.metadata, this.representation.withContainerMetadata(containerMetadata));
    }

    public InformationUnit withContentMetadata(ContentMetadata contentMetadata) {
        return new InformationUnit(this.identifier, this.metadata, this.representation.withContentMetadata(contentMetadata));
    }

    // =========================================================================
    // Validation
    // =========================================================================

    /**
     * Validates all core invariants of this Information Unit.
     *
     * @throws IllegalStateException if any core invariant is violated
     */
    public void validate() {
        if (identifier == null) {
            throw new IllegalStateException("InformationUnit identifier must not be null");
        }
        if (metadata == null) {
            throw new IllegalStateException("InformationUnit metadata must not be null");
        }
        if (representation == null) {
            throw new IllegalStateException("InformationUnit representation must not be null (exactly one representation invariant)");
        }
        if (representation.containerMetadata() == null) {
            throw new IllegalStateException("InformationRepresentation containerMetadata must not be null");
        }
        if (representation.contentMetadata() == null) {
            throw new IllegalStateException("InformationRepresentation contentMetadata must not be null");
        }
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationUnit that = (InformationUnit) o;
        return identifier.equals(that.identifier)
                && metadata.equals(that.metadata)
                && representation.equals(that.representation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, metadata, representation);
    }

    @Override
    public String toString() {
        return "InformationUnit{" +
                "topic='" + identifier.topicName() + '\'' +
                ", isSummary=" + isSummary() +
                ", metadata=" + metadata +
                ", representation=" + representation +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    /**
     * Fluent builder for {@link InformationUnit}.
     */
    public static final class Builder {
        private InformationUnitIdentifier identifier;
        private InformationUnitMetadata metadata = InformationUnitMetadata.empty();
        private InformationRepresentation representation = InformationRepresentation.builder().buildSummary();

        private Builder() {}

        public Builder identifier(InformationUnitIdentifier identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder identifier(UUID uuid) {
            this.identifier = uuid != null ? InformationUnitIdentifier.fromUuid(uuid) : null;
            return this;
        }

        public Builder randomIdentifier() {
            this.identifier = InformationUnitIdentifier.random();
            return this;
        }

        public Builder metadata(InformationUnitMetadata metadata) {
            this.metadata = metadata != null ? metadata : InformationUnitMetadata.empty();
            return this;
        }

        public Builder managementContext(ManagementContext managementContext) {
            this.metadata = this.metadata.withManagementContext(managementContext);
            return this;
        }

        public Builder informationCoordinates(InformationCoordinates coordinates) {
            this.metadata = this.metadata.withInformationCoordinates(coordinates);
            return this;
        }

        public Builder securityContext(InformationSecurityContext securityContext) {
            this.metadata = this.metadata.withSecurityContext(securityContext);
            return this;
        }

        public Builder lifecycleContext(LifecycleContext lifecycleContext) {
            this.metadata = this.metadata.withLifecycleContext(lifecycleContext);
            return this;
        }

        public Builder addRelationship(InformationUnitRelationship relationship) {
            this.metadata = this.metadata.withRelationship(relationship);
            return this;
        }

        public Builder relationships(Collection<InformationUnitRelationship> relationships) {
            this.metadata = this.metadata.withRelationships(relationships);
            return this;
        }

        public Builder representation(InformationRepresentation representation) {
            this.representation = representation != null ? representation : InformationRepresentation.builder().buildSummary();
            return this;
        }

        public Builder containerMetadata(ContainerMetadata containerMetadata) {
            this.representation = this.representation.withContainerMetadata(containerMetadata);
            return this;
        }

        public Builder contentMetadata(ContentMetadata contentMetadata) {
            this.representation = this.representation.withContentMetadata(contentMetadata);
            return this;
        }

        public Builder content(Object content) {
            this.representation = this.representation.withContent(content);
            return this;
        }

        public Builder content(String content) {
            this.representation = this.representation.withContent(content);
            return this;
        }

        public Builder content(byte[] content) {
            this.representation = this.representation.withContent(content);
            return this;
        }

        public InformationUnit build() {
            if (identifier == null) {
                identifier = InformationUnitIdentifier.random();
            }
            return new InformationUnit(identifier, metadata, representation);
        }

        public InformationUnit buildSummary() {
            if (identifier == null) {
                identifier = InformationUnitIdentifier.random();
            }
            return new InformationUnit(identifier, metadata, representation.toSummary());
        }
    }
}
