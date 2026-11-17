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

package net.fhirfactory.harmonia.model.core.information.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContainerMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitIdentifier;
import net.fhirfactory.harmonia.model.core.information.operations.ManagementContext;

import java.io.Serializable;
import java.util.Objects;
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
 *   <li><b>Peer operational view:</b> An optional {@link ManagementContext} remains available on the IU facade for
 *       working-cache and handshake contracts; it is not governed InformationMetadata or represented Content.</li>
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
    private final ManagementContext managementContext;
    private final InformationRepresentation representation;

    /**
     * Canonical constructor establishing validated Information Unit components.
     *
     * @param identifier        the persistent identity, must not be null
     * @param version           authoritative persisted version established by Mnemosyne (optional)
     * @param metadata          governed information metadata (optional, defaults to empty)
     * @param managementContext operational working view (optional)
     * @param representation    the single governed representation (optional, defaults to empty Summary)
     * @throws NullPointerException if {@code identifier} is null
     */
    @JsonCreator
    public InformationUnit(
            @JsonProperty("identifier") InformationUnitIdentifier identifier,
            @JsonProperty("version") String version,
            @JsonProperty("metadata") InformationMetadata metadata,
            @JsonProperty("managementContext") ManagementContext managementContext,
            @JsonProperty("representation") InformationRepresentation representation) {
        this.identifier = Objects.requireNonNull(identifier, "identifier must not be null");
        this.version = version != null && !version.isBlank() ? version.trim() : null;
        this.metadata = metadata != null ? metadata : InformationMetadata.empty();
        this.managementContext = managementContext;
        this.representation = representation != null
                ? representation
                : new InformationRepresentation(ContainerMetadata.empty(), ContentMetadata.empty(), Content.empty());
    }

    // =========================================================================
    // Core Accessors
    // =========================================================================

    @JsonProperty("identifier")
    public InformationUnitIdentifier identifier() {
        return identifier;
    }

    @JsonProperty("version")
    public String version() {
        return version;
    }

    @JsonProperty("metadata")
    public InformationMetadata metadata() {
        return metadata;
    }

    @JsonIgnore
    public ManagementContext managementContext() {
        return managementContext != null ? managementContext : ManagementContext.empty();
    }

    @JsonProperty("managementContext")
    public ManagementContext getManagementContext() {
        return managementContext;
    }

    @JsonProperty("representation")
    public InformationRepresentation representation() {
        return representation;
    }

    /**
     * Indicates whether this representation is the content-excluding Summary projection.
     *
     * @return true when the represented Content is empty
     */
    @JsonIgnore
    public boolean isSummary() {
        return representation.isSummary();
    }

    /**
     * Returns this IU's Summary projection, preserving identity, metadata and the peer operational view.
     *
     * @return this IU when already summarized, otherwise a new IU without Content
     */
    public InformationUnit toSummary() {
        if (isSummary()) {
            return this;
        }
        return new InformationUnit(identifier, version, metadata, managementContext, representation.toSummary());
    }

    /**
     * Returns a copy with a different authoritative persisted revision.
     *
     * @param version persisted revision, or null when not yet assigned
     * @return copied InformationUnit
     */
    public InformationUnit withVersion(String version) {
        return new InformationUnit(identifier, version, metadata, managementContext, representation);
    }

    /**
     * Returns a copy with replacement governed information metadata.
     *
     * @param metadata replacement metadata
     * @return copied InformationUnit
     */
    public InformationUnit withMetadata(InformationMetadata metadata) {
        return new InformationUnit(identifier, version, metadata, managementContext, representation);
    }

    /**
     * Returns a copy with an embedded peer operational view.
     *
     * @param managementContext optional working and handshake context
     * @return copied InformationUnit
     */
    public InformationUnit withManagementContext(ManagementContext managementContext) {
        return new InformationUnit(identifier, version, metadata, managementContext, representation);
    }

    /**
     * Returns a copy with a replacement singular representation.
     *
     * @param representation replacement representation
     * @return copied InformationUnit
     */
    public InformationUnit withRepresentation(InformationRepresentation representation) {
        return new InformationUnit(identifier, version, metadata, managementContext, representation);
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
                Objects.equals(managementContext, that.managementContext) &&
                Objects.equals(representation, that.representation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, version, metadata, managementContext, representation);
    }

    @Override
    public String toString() {
        return "InformationUnit{" +
                "id=" + identifier.uuid().toString().substring(0, 8) +
                ", summary=" + isSummary() +
                ", hasManagementContext=" + (managementContext != null && !managementContext.isEmpty()) +
                '}';
    }
}
