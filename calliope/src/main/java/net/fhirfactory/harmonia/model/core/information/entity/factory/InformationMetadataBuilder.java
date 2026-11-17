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

package net.fhirfactory.harmonia.model.core.information.entity.factory;

import net.fhirfactory.harmonia.model.core.information.entity.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationItinerary;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationSecurityContext;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitRelationship;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.LifecycleContext;
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Assembles the governed information metadata concerns without introducing operational working state.
 */
public final class InformationMetadataBuilder {

    private InformationCoordinates coordinates;
    private InformationItinerary itinerary;
    private Topic origin;
    private InformationSecurityContext securityContext;
    private LifecycleContext lifecycleContext;
    private final List<InformationUnitRelationship> relationships = new ArrayList<>();

    /** Sets the named persistence and ownership coordinates. */
    public InformationMetadataBuilder coordinates(InformationCoordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    /** Sets the conceptual information itinerary. */
    public InformationMetadataBuilder itinerary(InformationItinerary itinerary) {
        this.itinerary = itinerary;
        return this;
    }

    /** Sets the governed origin Topic. */
    public InformationMetadataBuilder origin(Topic origin) {
        this.origin = origin;
        return this;
    }

    /** Sets the IU-side security requirements. */
    public InformationMetadataBuilder securityContext(InformationSecurityContext securityContext) {
        this.securityContext = securityContext;
        return this;
    }

    /** Sets the governed lifecycle and retention context. */
    public InformationMetadataBuilder lifecycleContext(LifecycleContext lifecycleContext) {
        this.lifecycleContext = lifecycleContext;
        return this;
    }

    /** Adds one non-null relationship to this metadata value. */
    public InformationMetadataBuilder relationship(InformationUnitRelationship relationship) {
        if (relationship != null) {
            relationships.add(relationship);
        }
        return this;
    }

    /** Replaces the relationship set; null clears it. */
    public InformationMetadataBuilder relationships(Collection<InformationUnitRelationship> relationships) {
        this.relationships.clear();
        if (relationships != null) {
            this.relationships.addAll(relationships);
        }
        return this;
    }

    /** Builds an immutable information metadata value. */
    public InformationMetadata build() {
        return new InformationMetadata(coordinates, itinerary, origin, securityContext, lifecycleContext, relationships);
    }
}