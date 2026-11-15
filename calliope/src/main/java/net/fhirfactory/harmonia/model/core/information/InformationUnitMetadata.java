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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulates the distinct metadata contexts and optional relationships of an Information Unit.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit}):
 * <ul>
 *   <li>{@code InformationUnitMetadata} groups the distinct, non-overlapping metadata concerns:
 *     <ol>
 *       <li><b>{@link InformationCoordinates}:</b> Four named Topic references identifying PersistenceGateway,
 *           GatewayEndpoint, InformationLocation, and InformationOwner.</li>
 *       <li><b>{@link InformationItinerary}:</b> Distinct conceptual information path and participation concern.</li>
 *       <li><b>{@link InformationSecurityContext}:</b> IU-side required security criteria, compartments, and action masks.</li>
 *       <li><b>{@link LifecycleContext}:</b> Governed information lifecycle and persistence coordination.</li>
 *       <li><b>{@link ManagementContext}:</b> Operational working context maintained by Mneme.</li>
 *     </ol>
 *   </li>
 *   <li><b>Relationships:</b> Optional creator- or updater-supplied local and remote references ({@link InformationUnitRelationship}).</li>
 *   <li><b>CRITICAL INDEPENDENCE:</b> {@code InformationCoordinates} is completely independent from {@code SecurityContext}.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>All context fields are guaranteed non-null (defaulting to their respective {@code empty()} instances).</li>
 *   <li>Relationships list is defensively copied and returned as an unmodifiable list.</li>
 *   <li>Immutable value object design.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationUnitMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final InformationUnitMetadata EMPTY = new InformationUnitMetadata(
            ManagementContext.empty(),
            InformationCoordinates.empty(),
            InformationItinerary.empty(),
            InformationSecurityContext.empty(),
            LifecycleContext.empty(),
            List.of());

    private final ManagementContext managementContext;
    private final InformationCoordinates informationCoordinates;
    private final InformationItinerary informationItinerary;
    private final InformationSecurityContext securityContext;
    private final LifecycleContext lifecycleContext;
    private final List<InformationUnitRelationship> relationships;

    /**
     * Canonical constructor establishing validated metadata contexts.
     *
     * @param managementContext      operational management context (optional, defaults to empty)
     * @param informationCoordinates operational coordinates (optional, defaults to empty)
     * @param informationItinerary   information itinerary context (optional, defaults to empty)
     * @param securityContext        security requirements context (optional, defaults to empty)
     * @param lifecycleContext       lifecycle coordination context (optional, defaults to empty)
     * @param relationships          creator-supplied relationships (optional, defaults to empty list)
     */
    @JsonCreator
    public InformationUnitMetadata(
            @JsonProperty("managementContext") ManagementContext managementContext,
            @JsonProperty("informationCoordinates") InformationCoordinates informationCoordinates,
            @JsonProperty("informationItinerary") InformationItinerary informationItinerary,
            @JsonProperty("securityContext") InformationSecurityContext securityContext,
            @JsonProperty("lifecycleContext") LifecycleContext lifecycleContext,
            @JsonProperty("relationships") Collection<InformationUnitRelationship> relationships) {
        this.managementContext = managementContext != null ? managementContext : ManagementContext.empty();
        this.informationCoordinates = informationCoordinates != null ? informationCoordinates : InformationCoordinates.empty();
        this.informationItinerary = informationItinerary != null ? informationItinerary : InformationItinerary.empty();
        this.securityContext = securityContext != null ? securityContext : InformationSecurityContext.empty();
        this.lifecycleContext = lifecycleContext != null ? lifecycleContext : LifecycleContext.empty();
        if (relationships == null || relationships.isEmpty()) {
            this.relationships = List.of();
        } else {
            List<InformationUnitRelationship> list = new ArrayList<>();
            for (InformationUnitRelationship rel : relationships) {
                if (rel != null) {
                    list.add(rel);
                }
            }
            this.relationships = Collections.unmodifiableList(list);
        }
    }

    /**
     * Compatibility constructor without explicit {@code InformationItinerary}.
     */
    public InformationUnitMetadata(
            ManagementContext managementContext,
            InformationCoordinates informationCoordinates,
            InformationSecurityContext securityContext,
            LifecycleContext lifecycleContext,
            Collection<InformationUnitRelationship> relationships) {
        this(managementContext, informationCoordinates, InformationItinerary.empty(), securityContext, lifecycleContext, relationships);
    }

    /**
     * Returns an empty, default {@code InformationUnitMetadata} instance.
     *
     * @return empty InformationUnitMetadata
     */
    public static InformationUnitMetadata empty() {
        return EMPTY;
    }

    /**
     * Creates a new builder for constructing {@code InformationUnitMetadata} instances.
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
     * Returns the operational management context.
     *
     * @return the management context, never null
     */
    @JsonProperty("managementContext")
    public ManagementContext managementContext() {
        return managementContext;
    }

    public ManagementContext getManagementContext() {
        return managementContext;
    }

    /**
     * Returns the operational information coordinates.
     *
     * @return the information coordinates, never null
     */
    @JsonProperty("informationCoordinates")
    public InformationCoordinates informationCoordinates() {
        return informationCoordinates;
    }

    public InformationCoordinates getInformationCoordinates() {
        return informationCoordinates;
    }

    /**
     * Returns the information itinerary context.
     *
     * @return the information itinerary, never null
     */
    @JsonProperty("informationItinerary")
    public InformationItinerary informationItinerary() {
        return informationItinerary;
    }

    public InformationItinerary getInformationItinerary() {
        return informationItinerary;
    }

    /**
     * Returns the IU-side security requirements context.
     *
     * @return the security context, never null
     */
    @JsonProperty("securityContext")
    public InformationSecurityContext securityContext() {
        return securityContext;
    }

    public InformationSecurityContext getSecurityContext() {
        return securityContext;
    }

    /**
     * Returns the lifecycle and persistence coordination context.
     *
     * @return the lifecycle context, never null
     */
    @JsonProperty("lifecycleContext")
    public LifecycleContext lifecycleContext() {
        return lifecycleContext;
    }

    public LifecycleContext getLifecycleContext() {
        return lifecycleContext;
    }

    /**
     * Returns the unmodifiable list of creator-supplied relationships.
     *
     * @return unmodifiable list of relationships, never null
     */
    @JsonProperty("relationships")
    public List<InformationUnitRelationship> relationships() {
        return relationships;
    }

    public List<InformationUnitRelationship> getRelationships() {
        return relationships;
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public InformationUnitMetadata withManagementContext(ManagementContext managementContext) {
        return new InformationUnitMetadata(managementContext, this.informationCoordinates, this.informationItinerary, this.securityContext, this.lifecycleContext, this.relationships);
    }

    public InformationUnitMetadata withInformationCoordinates(InformationCoordinates informationCoordinates) {
        return new InformationUnitMetadata(this.managementContext, informationCoordinates, this.informationItinerary, this.securityContext, this.lifecycleContext, this.relationships);
    }

    public InformationUnitMetadata withInformationItinerary(InformationItinerary informationItinerary) {
        return new InformationUnitMetadata(this.managementContext, this.informationCoordinates, informationItinerary, this.securityContext, this.lifecycleContext, this.relationships);
    }

    public InformationUnitMetadata withSecurityContext(InformationSecurityContext securityContext) {
        return new InformationUnitMetadata(this.managementContext, this.informationCoordinates, this.informationItinerary, securityContext, this.lifecycleContext, this.relationships);
    }

    public InformationUnitMetadata withLifecycleContext(LifecycleContext lifecycleContext) {
        return new InformationUnitMetadata(this.managementContext, this.informationCoordinates, this.informationItinerary, this.securityContext, lifecycleContext, this.relationships);
    }

    public InformationUnitMetadata withRelationships(Collection<InformationUnitRelationship> relationships) {
        return new InformationUnitMetadata(this.managementContext, this.informationCoordinates, this.informationItinerary, this.securityContext, this.lifecycleContext, relationships);
    }

    public InformationUnitMetadata withRelationship(InformationUnitRelationship relationship) {
        Objects.requireNonNull(relationship, "relationship must not be null");
        List<InformationUnitRelationship> updated = new ArrayList<>(this.relationships);
        updated.add(relationship);
        return new InformationUnitMetadata(this.managementContext, this.informationCoordinates, this.informationItinerary, this.securityContext, this.lifecycleContext, updated);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationUnitMetadata that = (InformationUnitMetadata) o;
        return Objects.equals(managementContext, that.managementContext) &&
                Objects.equals(informationCoordinates, that.informationCoordinates) &&
                Objects.equals(informationItinerary, that.informationItinerary) &&
                Objects.equals(securityContext, that.securityContext) &&
                Objects.equals(lifecycleContext, that.lifecycleContext) &&
                Objects.equals(relationships, that.relationships);
    }

    @Override
    public int hashCode() {
        return Objects.hash(managementContext, informationCoordinates, informationItinerary, securityContext, lifecycleContext, relationships);
    }

    @Override
    public String toString() {
        return "InformationUnitMetadata{" +
                "managementContext=" + managementContext +
                ", coordinates=" + informationCoordinates +
                ", itinerary=" + informationItinerary +
                ", securityContext=" + securityContext +
                ", lifecycleContext=" + lifecycleContext +
                ", relationships=" + relationships.size() +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static final class Builder {

        private ManagementContext managementContext;
        private InformationCoordinates informationCoordinates;
        private InformationItinerary informationItinerary;
        private InformationSecurityContext securityContext;
        private LifecycleContext lifecycleContext;
        private final List<InformationUnitRelationship> relationships = new ArrayList<>();

        public Builder managementContext(ManagementContext managementContext) {
            this.managementContext = managementContext;
            return this;
        }

        public Builder informationCoordinates(InformationCoordinates informationCoordinates) {
            this.informationCoordinates = informationCoordinates;
            return this;
        }

        public Builder informationItinerary(InformationItinerary informationItinerary) {
            this.informationItinerary = informationItinerary;
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

        public InformationUnitMetadata build() {
            return new InformationUnitMetadata(
                    managementContext,
                    informationCoordinates,
                    informationItinerary,
                    securityContext,
                    lifecycleContext,
                    relationships);
        }
    }
}
