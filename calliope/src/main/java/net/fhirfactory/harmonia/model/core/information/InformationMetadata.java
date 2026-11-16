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
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Encapsulates the governed information-related metadata of an Information Unit.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit})
 * and {@code DRP-Information-Unit-Framework.Final.002.md} (§§1, 3, 4):
 * <ul>
 *   <li><b>Peer Metadata Concern:</b> {@code InformationMetadata} is the conceptual peer of {@code PetasosParcelMetadata}
 *       and {@link OperationalMetadata}. It groups the characteristics of governed information:
 *     <ol>
 *       <li><b>{@link InformationCoordinates}:</b> Four named Topic references (PersistenceGateway, GatewayEndpoint,
 *           InformationLocation, InformationOwner).</li>
 *       <li><b>{@link InformationItinerary}:</b> Distinct conceptual information path and participation concern.</li>
 *       <li><b>Origin ({@link #origin()}):</b> Governed Topic describing where the information originated.</li>
 *       <li><b>{@link InformationSecurityContext}:</b> IU-side required security criteria, compartments, and action masks.</li>
 *       <li><b>{@link LifecycleContext}:</b> Governed information lifecycle (retention, disposition, archive).</li>
 *       <li><b>Relationships:</b> Creator- or updater-supplied local and remote references ({@link InformationUnitRelationship}).</li>
 *     </ol>
 *   </li>
 *   <li><b>Strict Separation from Operational Metadata:</b> Active working-cache state, Mneme operational revisions,
 *       cache active generations, and persistence handshaking belong to {@link OperationalMetadata}, <b>not</b>
 *       as an intrinsic persistent constituent of {@code InformationMetadata}.</li>
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
public class InformationMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final InformationMetadata EMPTY = new InformationMetadata(
            InformationCoordinates.empty(),
            InformationItinerary.empty(),
            null,
            InformationSecurityContext.empty(),
            LifecycleContext.empty(),
            List.of());

    private final InformationCoordinates coordinates;
    private final InformationItinerary itinerary;
    private final Topic origin;
    private final InformationSecurityContext securityContext;
    private final LifecycleContext lifecycleContext;
    private final List<InformationUnitRelationship> relationships;

    /**
     * Canonical constructor establishing validated information metadata contexts.
     *
     * @param coordinates     operational coordinates (optional, defaults to empty)
     * @param itinerary       information itinerary context (optional, defaults to empty)
     * @param origin          governed origin Topic (optional)
     * @param securityContext security requirements context (optional, defaults to empty)
     * @param lifecycleContext lifecycle and retention context (optional, defaults to empty)
     * @param relationships   creator-supplied relationships (optional, defaults to empty list)
     */
    @JsonCreator
    public InformationMetadata(
            @JsonProperty("coordinates") InformationCoordinates coordinates,
            @JsonProperty("itinerary") InformationItinerary itinerary,
            @JsonProperty("origin") Topic origin,
            @JsonProperty("securityContext") InformationSecurityContext securityContext,
            @JsonProperty("lifecycleContext") LifecycleContext lifecycleContext,
            @JsonProperty("relationships") Collection<InformationUnitRelationship> relationships) {
        this.coordinates = coordinates != null ? coordinates : InformationCoordinates.empty();
        this.itinerary = itinerary != null ? itinerary : InformationItinerary.empty();
        this.origin = origin;
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
     * Returns an empty, default {@code InformationMetadata} instance.
     *
     * @return empty InformationMetadata
     */
    public static InformationMetadata empty() {
        return EMPTY;
    }

    /**
     * Creates a new builder for constructing {@code InformationMetadata} instances.
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
     * Returns the operational information coordinates.
     *
     * @return the coordinates, never null
     */
    @JsonProperty("coordinates")
    public InformationCoordinates coordinates() {
        return coordinates;
    }

    public InformationCoordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Returns the information itinerary context.
     *
     * @return the information itinerary, never null
     */
    @JsonProperty("itinerary")
    public InformationItinerary itinerary() {
        return itinerary;
    }

    public InformationItinerary getItinerary() {
        return itinerary;
    }

    /**
     * Returns the governed Topic describing where this information originated.
     *
     * @return optional origin Topic
     */
    @JsonIgnore
    public Optional<Topic> origin() {
        return Optional.ofNullable(origin);
    }

    @JsonProperty("origin")
    public Topic getOrigin() {
        return origin;
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
     * Returns the lifecycle and retention coordination context.
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

    public InformationMetadata withCoordinates(InformationCoordinates coordinates) {
        return new InformationMetadata(coordinates, this.itinerary, this.origin, this.securityContext, this.lifecycleContext, this.relationships);
    }

    public InformationMetadata withItinerary(InformationItinerary itinerary) {
        return new InformationMetadata(this.coordinates, itinerary, this.origin, this.securityContext, this.lifecycleContext, this.relationships);
    }

    public InformationMetadata withOrigin(Topic origin) {
        return new InformationMetadata(this.coordinates, this.itinerary, origin, this.securityContext, this.lifecycleContext, this.relationships);
    }

    public InformationMetadata withSecurityContext(InformationSecurityContext securityContext) {
        return new InformationMetadata(this.coordinates, this.itinerary, this.origin, securityContext, this.lifecycleContext, this.relationships);
    }

    public InformationMetadata withLifecycleContext(LifecycleContext lifecycleContext) {
        return new InformationMetadata(this.coordinates, this.itinerary, this.origin, this.securityContext, lifecycleContext, this.relationships);
    }

    public InformationMetadata withRelationships(Collection<InformationUnitRelationship> relationships) {
        return new InformationMetadata(this.coordinates, this.itinerary, this.origin, this.securityContext, this.lifecycleContext, relationships);
    }

    public InformationMetadata withRelationship(InformationUnitRelationship relationship) {
        Objects.requireNonNull(relationship, "relationship must not be null");
        List<InformationUnitRelationship> updated = new ArrayList<>(this.relationships);
        updated.add(relationship);
        return new InformationMetadata(this.coordinates, this.itinerary, this.origin, this.securityContext, this.lifecycleContext, updated);
    }

    // =========================================================================
    // Identity and Equality
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationMetadata that = (InformationMetadata) o;
        return Objects.equals(coordinates, that.coordinates) &&
                Objects.equals(itinerary, that.itinerary) &&
                Objects.equals(origin, that.origin) &&
                Objects.equals(securityContext, that.securityContext) &&
                Objects.equals(lifecycleContext, that.lifecycleContext) &&
                Objects.equals(relationships, that.relationships);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, itinerary, origin, securityContext, lifecycleContext, relationships);
    }

    @Override
    public String toString() {
        return "InformationMetadata{" +
                "coordinates=" + coordinates +
                ", itinerary=" + itinerary +
                ", origin=" + origin +
                ", securityContext=" + securityContext +
                ", lifecycleContext=" + lifecycleContext +
                ", relationships=" + relationships.size() +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static class Builder {

        protected InformationCoordinates coordinates;
        protected InformationItinerary itinerary;
        protected Topic origin;
        protected InformationSecurityContext securityContext;
        protected LifecycleContext lifecycleContext;
        protected final List<InformationUnitRelationship> relationships = new ArrayList<>();

        public Builder coordinates(InformationCoordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder itinerary(InformationItinerary itinerary) {
            this.itinerary = itinerary;
            return this;
        }

        public Builder origin(Topic origin) {
            this.origin = origin;
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

        public Builder relationship(InformationUnitRelationship relationship) {
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

        public InformationMetadata build() {
            return new InformationMetadata(
                    coordinates,
                    itinerary,
                    origin,
                    securityContext,
                    lifecycleContext,
                    relationships);
        }
    }
}
