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
import net.fhirfactory.harmonia.model.core.information.factory.InformationUnitBuilder;
import net.fhirfactory.harmonia.model.core.information.factory.InformationUnitFactory;
import net.fhirfactory.harmonia.model.core.information.factory.InformationUnitValidator;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Encapsulates the canonical, immutable representation of a Harmonia Information Unit (IU).
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
 *   <li><b>Metadata Concerns ({@link InformationMetadata}):</b>
 *     <ol>
 *       <li>{@link InformationCoordinates}: Four named Topic references (PersistenceGateway, GatewayEndpoint, InformationLocation, InformationOwner).
 *           <b>100% independent of SecurityContext</b>.</li>
 *       <li>{@link InformationItinerary}: Distinct conceptual information path and component participation concern with strongly typed Topics.</li>
 *       <li><b>Origin:</b> Governed Topic describing where the information originated.</li>
 *       <li>{@link InformationSecurityContext}: IU-side required criteria, compartments, and action masks.</li>
 *       <li>{@link LifecycleContext}: Governed information lifecycle (retention, disposition, archive).</li>
 *       <li><b>Relationships:</b> Creator-supplied relationships ({@link InformationUnitRelationship}).</li>
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
    private final InformationMetadata metadata;
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
    public InformationUnit(
            InformationUnitIdentifier identifier,
            String version,
            InformationMetadata metadata,
            InformationRepresentation representation) {
        this.identifier = Objects.requireNonNull(identifier, "identifier must not be null");
        this.version = version != null && !version.isBlank() ? version.trim() : null;
        this.metadata = metadata != null ? metadata : InformationMetadata.empty();
        this.representation = representation != null ? representation : InformationRepresentation.builder().buildSummary();
    }

    @JsonCreator
    public InformationUnit(
            @JsonProperty("identifier") InformationUnitIdentifier identifier,
            @JsonProperty("version") String version,
            @JsonProperty("metadata") InformationUnitMetadata metadata,
            @JsonProperty("representation") InformationRepresentation representation) {
        this(identifier, version, (InformationMetadata) metadata, representation);
    }

    public InformationUnit(
            InformationUnitIdentifier identifier,
            InformationMetadata metadata,
            InformationRepresentation representation) {
        this(identifier, null, metadata, representation);
    }

    public InformationUnit(
            InformationUnitIdentifier identifier,
            InformationUnitMetadata metadata,
            InformationRepresentation representation) {
        this(identifier, null, (InformationMetadata) metadata, representation);
    }

    public static InformationUnit create(UUID uuid, InformationMetadata metadata, InformationRepresentation representation) {
        return InformationUnitFactory.create(uuid, metadata, representation);
    }

    public static InformationUnit create(UUID uuid, InformationUnitMetadata metadata, InformationRepresentation representation) {
        return InformationUnitFactory.create(uuid, metadata, representation);
    }

    public static InformationUnit create(UUID uuid, String version, InformationMetadata metadata, InformationRepresentation representation) {
        return InformationUnitFactory.create(uuid, version, metadata, representation);
    }

    public static InformationUnit create(UUID uuid, String version, InformationUnitMetadata metadata, InformationRepresentation representation) {
        return InformationUnitFactory.create(uuid, version, metadata, representation);
    }

    public static InformationUnit random(InformationMetadata metadata, InformationRepresentation representation) {
        return InformationUnitFactory.random(metadata, representation);
    }

    public static InformationUnit random(InformationUnitMetadata metadata, InformationRepresentation representation) {
        return InformationUnitFactory.random(metadata, representation);
    }

    public static InformationUnitBuilder builder() {
        return new InformationUnitBuilder();
    }

    // =========================================================================
    // Core Accessors
    // =========================================================================

    @JsonProperty("identifier")
    public InformationUnitIdentifier identifier() { return identifier; }
    public InformationUnitIdentifier getIdentifier() { return identifier; }

    @JsonIgnore
    public UUID uuid() { return identifier.uuid(); }
    @JsonIgnore
    public UUID getUuid() { return identifier.uuid(); }

    @JsonIgnore
    public String topicName() { return identifier.topicName(); }
    @JsonIgnore
    public String getTopicName() { return identifier.topicName(); }

    @JsonIgnore
    public Topic toTopic() { return identifier.toTopic(); }

    @JsonProperty("version")
    public String version() { return version; }
    public String getVersion() { return version; }

    @JsonProperty("metadata")
    public InformationMetadata metadata() { return metadata; }

    @JsonIgnore
    public InformationUnitMetadata getMetadata() {
        if (metadata instanceof InformationUnitMetadata ium) {
            return ium;
        }
        return new InformationUnitMetadata(
                ManagementContext.empty(),
                metadata.coordinates(),
                metadata.itinerary(),
                metadata.origin().orElse(null),
                metadata.securityContext(),
                metadata.lifecycleContext(),
                metadata.relationships()
        );
    }

    @JsonProperty("representation")
    public InformationRepresentation representation() { return representation; }
    public InformationRepresentation getRepresentation() { return representation; }

    // =========================================================================
    // Operational Management Context Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public ManagementContext managementContext() {
        return metadata instanceof InformationUnitMetadata ium ? ium.managementContext() : ManagementContext.empty();
    }
    @JsonIgnore
    public ManagementContext getManagementContext() { return managementContext(); }

    @JsonIgnore
    public Optional<OperationalRevision> operationalRevision() { return managementContext().operationalRevision(); }
    @Deprecated
    @JsonIgnore
    public Optional<AuthoritativeVersion> operationalVersion() { return managementContext().operationalVersion(); }
    @JsonIgnore
    public Optional<String> activeGeneration() { return managementContext().activeGeneration(); }

    // =========================================================================
    // Information Coordinates Convenience Accessors (Strongly Typed Topics)
    // =========================================================================

    @JsonIgnore
    public InformationCoordinates informationCoordinates() { return metadata.coordinates(); }
    @JsonIgnore
    public InformationCoordinates getInformationCoordinates() { return metadata.coordinates(); }

    @JsonIgnore
    public Optional<Topic> persistenceGateway() { return metadata.coordinates().persistenceGateway(); }
    @JsonIgnore
    public Optional<Topic> gatewayEndpoint() { return metadata.coordinates().gatewayEndpoint(); }
    @JsonIgnore
    public Optional<Topic> informationLocation() { return metadata.coordinates().informationLocation(); }
    @JsonIgnore
    public Optional<Topic> informationOwner() { return metadata.coordinates().informationOwner(); }

    @JsonIgnore
    public Optional<String> persistenceGatewayName() { return metadata.coordinates().persistenceGatewayName(); }
    @JsonIgnore
    public Optional<String> gatewayEndpointName() { return metadata.coordinates().gatewayEndpointName(); }
    @JsonIgnore
    public Optional<String> informationLocationName() { return metadata.coordinates().informationLocationName(); }
    @JsonIgnore
    public Optional<String> informationOwnerName() { return metadata.coordinates().informationOwnerName(); }

    // =========================================================================
    // Information Itinerary Convenience Accessors (Strongly Typed Topics)
    // =========================================================================

    @JsonIgnore
    public InformationItinerary informationItinerary() { return metadata.itinerary(); }
    @JsonIgnore
    public InformationItinerary getInformationItinerary() { return metadata.itinerary(); }

    @JsonIgnore
    public Optional<Topic> sourceEntryComponent() { return metadata.itinerary().sourceEntryComponent(); }
    @JsonIgnore
    public Optional<Topic> creationComponent() { return metadata.itinerary().creationComponent(); }
    @JsonIgnore
    public Optional<Topic> previousComponent() { return metadata.itinerary().previousComponent(); }
    @JsonIgnore
    public Optional<Topic> nextComponent() { return metadata.itinerary().nextComponent(); }
    @JsonIgnore
    public Optional<Topic> targetExitComponent() { return metadata.itinerary().targetExitComponent(); }
    @JsonIgnore
    public List<Topic> participatingComponents() { return metadata.itinerary().participatingComponents(); }

    // =========================================================================
    // Origin Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public Optional<Topic> origin() { return metadata.origin(); }
    @JsonIgnore
    public Topic getOrigin() { return metadata.origin().orElse(null); }

    // =========================================================================
    // Security Context Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public InformationSecurityContext securityContext() { return metadata.securityContext(); }
    @JsonIgnore
    public InformationSecurityContext getSecurityContext() { return metadata.securityContext(); }

    // =========================================================================
    // Lifecycle Context Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public LifecycleContext lifecycleContext() { return metadata.lifecycleContext(); }
    @JsonIgnore
    public LifecycleContext getLifecycleContext() { return metadata.lifecycleContext(); }

    // =========================================================================
    // Relationships Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public List<InformationUnitRelationship> relationships() { return metadata.relationships(); }
    @JsonIgnore
    public List<InformationUnitRelationship> getRelationships() { return metadata.relationships(); }

    // =========================================================================
    // Representation Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public ContainerMetadata containerMetadata() { return representation.containerMetadata(); }
    @JsonIgnore
    public ContainerMetadata getContainerMetadata() { return representation.containerMetadata(); }

    @JsonIgnore
    public ContentMetadata contentMetadata() { return representation.contentMetadata(); }
    @JsonIgnore
    public ContentMetadata getContentMetadata() { return representation.contentMetadata(); }

    @JsonIgnore
    public Optional<Topic> contentStructureTopic() { return representation.contentMetadata().contentStructureTopic(); }
    @JsonIgnore
    public Optional<String> contentStructureTopicName() { return representation.contentMetadata().contentStructureTopicName(); }
    @JsonIgnore
    public Optional<String> contentStructureVersion() { return representation.contentMetadata().contentStructureVersion(); }

    @JsonIgnore
    public Optional<Object> content() { return representation.content(); }
    @JsonIgnore
    public Object getContent() { return representation.getContent(); }

    @JsonIgnore
    public Optional<String> contentAsString() { return representation.contentAsString(); }
    @JsonIgnore
    public Optional<byte[]> contentAsBytes() { return representation.contentAsBytes(); }
    @JsonIgnore
    public <T> Optional<T> contentAs(Class<T> type) { return representation.contentAs(type); }
    @JsonIgnore
    public boolean hasContent() { return representation.hasContent(); }
    @JsonIgnore
    public boolean isSummary() { return representation.isSummary(); }

    // =========================================================================
    // Summary Projection Semantics
    // =========================================================================

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

    public InformationUnit withMetadata(InformationMetadata metadata) {
        return new InformationUnit(this.identifier, this.version, metadata, this.representation);
    }

    public InformationUnit withMetadata(InformationUnitMetadata metadata) {
        return new InformationUnit(this.identifier, this.version, metadata, this.representation);
    }

    public InformationUnit withOrigin(Topic origin) {
        return withMetadata(this.metadata.withOrigin(origin));
    }

    public InformationUnit withRepresentation(InformationRepresentation representation) {
        return new InformationUnit(this.identifier, this.version, this.metadata, representation);
    }

    public InformationUnit withManagementContext(ManagementContext managementContext) {
        if (this.metadata instanceof InformationUnitMetadata ium) {
            return new InformationUnit(this.identifier, this.version, ium.withManagementContext(managementContext), this.representation);
        }
        InformationUnitMetadata newMeta = new InformationUnitMetadata(managementContext, metadata.coordinates(), metadata.itinerary(), metadata.origin().orElse(null), metadata.securityContext(), metadata.lifecycleContext(), metadata.relationships());
        return new InformationUnit(this.identifier, this.version, newMeta, this.representation);
    }

    public InformationUnit withInformationCoordinates(InformationCoordinates informationCoordinates) {
        return new InformationUnit(this.identifier, this.version, this.metadata.withCoordinates(informationCoordinates), this.representation);
    }

    public InformationUnit withInformationItinerary(InformationItinerary informationItinerary) {
        return new InformationUnit(this.identifier, this.version, this.metadata.withItinerary(informationItinerary), this.representation);
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

    public InformationUnit withContent(Content content) {
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

    public boolean validate() {
        return InformationUnitValidator.validate(this);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationUnit that = (InformationUnit) o;
        return Objects.equals(identifier, that.identifier) &&
                Objects.equals(version, that.version) &&
                Objects.equals(metadata, that.metadata) &&
                Objects.equals(representation, that.representation);
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

    public static final class Builder extends InformationUnitBuilder {
    }
}
