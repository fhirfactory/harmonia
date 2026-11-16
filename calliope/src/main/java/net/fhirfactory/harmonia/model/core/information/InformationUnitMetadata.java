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

/**
 * Concrete {@link InformationMetadata} carrier providing metadata contexts, origin, and relationships
 * for an Information Unit, with working-cache bridge accessors.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit})
 * and {@code DRP-Information-Unit-Framework.Final.002.md} (§§1, 3, 4):
 * <ul>
 *   <li>{@code InformationUnitMetadata} extends {@link InformationMetadata} to encapsulate the governed characteristics
 *       of an Information Unit.</li>
 *   <li><b>Operational vs Governed Information Separation:</b> {@link ManagementContext} operational working-cache data
 *       and optimistic-concurrency revisions belong conceptually to {@link OperationalMetadata} (OM-R04 / OM-06).
 *       A working-cache manifestation bridge is preserved here for backward compatibility.</li>
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
public class InformationUnitMetadata extends InformationMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final InformationUnitMetadata EMPTY = new InformationUnitMetadata(
            ManagementContext.empty(),
            InformationCoordinates.empty(),
            InformationItinerary.empty(),
            null,
            InformationSecurityContext.empty(),
            LifecycleContext.empty(),
            List.of());

    private final ManagementContext managementContext;

    /**
     * Canonical constructor establishing validated metadata contexts.
     *
     * @param managementContext      operational management context (optional, defaults to empty)
     * @param informationCoordinates operational coordinates (optional, defaults to empty)
     * @param informationItinerary   information itinerary context (optional, defaults to empty)
     * @param origin                 origin Topic (optional)
     * @param securityContext        security requirements context (optional, defaults to empty)
     * @param lifecycleContext       lifecycle coordination context (optional, defaults to empty)
     * @param relationships          creator-supplied relationships (optional, defaults to empty list)
     */
    @JsonCreator
    public InformationUnitMetadata(
            @JsonProperty("managementContext") ManagementContext managementContext,
            @JsonProperty("coordinates") InformationCoordinates informationCoordinates,
            @JsonProperty("itinerary") InformationItinerary informationItinerary,
            @JsonProperty("origin") Topic origin,
            @JsonProperty("securityContext") InformationSecurityContext securityContext,
            @JsonProperty("lifecycleContext") LifecycleContext lifecycleContext,
            @JsonProperty("relationships") Collection<InformationUnitRelationship> relationships) {
        super(informationCoordinates, informationItinerary, origin, securityContext, lifecycleContext, relationships);
        this.managementContext = managementContext != null ? managementContext : ManagementContext.empty();
    }

    public InformationUnitMetadata(
            ManagementContext managementContext,
            InformationCoordinates informationCoordinates,
            InformationItinerary informationItinerary,
            InformationSecurityContext securityContext,
            LifecycleContext lifecycleContext,
            Collection<InformationUnitRelationship> relationships) {
        this(managementContext, informationCoordinates, informationItinerary, null, securityContext, lifecycleContext, relationships);
    }

    public InformationUnitMetadata(
            ManagementContext managementContext,
            InformationCoordinates informationCoordinates,
            InformationSecurityContext securityContext,
            LifecycleContext lifecycleContext,
            Collection<InformationUnitRelationship> relationships) {
        this(managementContext, informationCoordinates, InformationItinerary.empty(), null, securityContext, lifecycleContext, relationships);
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

    @JsonIgnore
    public InformationCoordinates informationCoordinates() {
        return coordinates();
    }

    @JsonIgnore
    public InformationCoordinates getInformationCoordinates() {
        return coordinates();
    }

    @JsonIgnore
    public InformationItinerary informationItinerary() {
        return itinerary();
    }

    @JsonIgnore
    public InformationItinerary getInformationItinerary() {
        return itinerary();
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public InformationUnitMetadata withManagementContext(ManagementContext managementContext) {
        return new InformationUnitMetadata(managementContext, coordinates(), itinerary(), origin().orElse(null), securityContext(), lifecycleContext(), relationships());
    }

    public InformationUnitMetadata withInformationCoordinates(InformationCoordinates informationCoordinates) {
        return new InformationUnitMetadata(this.managementContext, informationCoordinates, itinerary(), origin().orElse(null), securityContext(), lifecycleContext(), relationships());
    }

    public InformationUnitMetadata withInformationItinerary(InformationItinerary informationItinerary) {
        return new InformationUnitMetadata(this.managementContext, coordinates(), informationItinerary, origin().orElse(null), securityContext(), lifecycleContext(), relationships());
    }

    public InformationUnitMetadata withOriginTopic(Topic origin) {
        return new InformationUnitMetadata(this.managementContext, coordinates(), itinerary(), origin, securityContext(), lifecycleContext(), relationships());
    }

    public InformationUnitMetadata withSecurityContext(InformationSecurityContext securityContext) {
        return new InformationUnitMetadata(this.managementContext, coordinates(), itinerary(), origin().orElse(null), securityContext, lifecycleContext(), relationships());
    }

    public InformationUnitMetadata withLifecycleContext(LifecycleContext lifecycleContext) {
        return new InformationUnitMetadata(this.managementContext, coordinates(), itinerary(), origin().orElse(null), securityContext(), lifecycleContext, relationships());
    }

    public InformationUnitMetadata withRelationships(Collection<InformationUnitRelationship> relationships) {
        return new InformationUnitMetadata(this.managementContext, coordinates(), itinerary(), origin().orElse(null), securityContext(), lifecycleContext(), relationships);
    }

    public InformationUnitMetadata withRelationship(InformationUnitRelationship relationship) {
        Objects.requireNonNull(relationship, "relationship must not be null");
        List<InformationUnitRelationship> updated = new ArrayList<>(relationships());
        updated.add(relationship);
        return new InformationUnitMetadata(this.managementContext, coordinates(), itinerary(), origin().orElse(null), securityContext(), lifecycleContext(), updated);
    }

    // =========================================================================
    // Identity and Equality
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InformationUnitMetadata that = (InformationUnitMetadata) o;
        return Objects.equals(managementContext, that.managementContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), managementContext);
    }

    @Override
    public String toString() {
        return "InformationUnitMetadata{" +
                "managementContext=" + managementContext +
                ", coordinates=" + coordinates() +
                ", itinerary=" + itinerary() +
                ", origin=" + origin().orElse(null) +
                ", securityContext=" + securityContext() +
                ", lifecycleContext=" + lifecycleContext() +
                ", relationships=" + relationships().size() +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static final class Builder extends InformationMetadata.Builder {

        private ManagementContext managementContext;

        public Builder managementContext(ManagementContext managementContext) {
            this.managementContext = managementContext;
            return this;
        }

        @Override
        public Builder coordinates(InformationCoordinates coordinates) {
            super.coordinates(coordinates);
            return this;
        }

        public Builder informationCoordinates(InformationCoordinates coordinates) {
            return coordinates(coordinates);
        }

        @Override
        public Builder itinerary(InformationItinerary itinerary) {
            super.itinerary(itinerary);
            return this;
        }

        public Builder informationItinerary(InformationItinerary itinerary) {
            return itinerary(itinerary);
        }

        @Override
        public Builder origin(Topic origin) {
            super.origin(origin);
            return this;
        }

        @Override
        public Builder securityContext(InformationSecurityContext securityContext) {
            super.securityContext(securityContext);
            return this;
        }

        @Override
        public Builder lifecycleContext(LifecycleContext lifecycleContext) {
            super.lifecycleContext(lifecycleContext);
            return this;
        }

        @Override
        public Builder relationship(InformationUnitRelationship relationship) {
            super.relationship(relationship);
            return this;
        }

        @Override
        public Builder relationships(Collection<InformationUnitRelationship> relationships) {
            super.relationships(relationships);
            return this;
        }

        @Override
        public InformationUnitMetadata build() {
            return new InformationUnitMetadata(
                    managementContext,
                    coordinates,
                    itinerary,
                    origin,
                    securityContext,
                    lifecycleContext,
                    relationships);
        }
    }
}
