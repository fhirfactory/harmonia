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

package net.fhirfactory.harmonia.model.core.information.factory;

import net.fhirfactory.harmonia.model.core.information.*;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Fluent builder for creating immutable {@link InformationUnit} instances.
 */
public class InformationUnitBuilder {

    protected InformationUnitIdentifier identifier;
    protected String version;
    protected InformationMetadata metadata;
    protected InformationRepresentation representation;

    protected InformationCoordinates coordinates;
    protected InformationItinerary itinerary;
    protected Topic origin;
    protected InformationSecurityContext securityContext;
    protected LifecycleContext lifecycleContext;
    protected ManagementContext managementContext;
    protected final List<InformationUnitRelationship> relationships = new ArrayList<>();

    protected ContainerMetadata containerMetadata;
    protected ContentMetadata contentMetadata;
    protected Content content;

    public InformationUnitBuilder() {
    }

    public InformationUnitBuilder identifier(InformationUnitIdentifier identifier) {
        this.identifier = identifier;
        return this;
    }

    public InformationUnitBuilder identifier(UUID uuid) {
        this.identifier = InformationUnitIdentifier.fromUuid(uuid);
        return this;
    }

    public InformationUnitBuilder uuid(UUID uuid) {
        this.identifier = InformationUnitIdentifier.fromUuid(uuid);
        return this;
    }

    public InformationUnitBuilder randomIdentifier() {
        this.identifier = InformationUnitIdentifier.random();
        return this;
    }

    public InformationUnitBuilder version(String version) {
        this.version = version;
        return this;
    }

    public InformationUnitBuilder version(AuthoritativeVersion version) {
        this.version = version != null ? version.value() : null;
        return this;
    }

    public InformationUnitBuilder metadata(InformationMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public InformationUnitBuilder metadata(InformationUnitMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public InformationUnitBuilder representation(InformationRepresentation representation) {
        this.representation = representation;
        return this;
    }

    public InformationUnitBuilder coordinates(InformationCoordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public InformationUnitBuilder informationCoordinates(InformationCoordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public InformationUnitBuilder itinerary(InformationItinerary itinerary) {
        this.itinerary = itinerary;
        return this;
    }

    public InformationUnitBuilder informationItinerary(InformationItinerary itinerary) {
        this.itinerary = itinerary;
        return this;
    }

    public InformationUnitBuilder origin(Topic origin) {
        this.origin = origin;
        return this;
    }

    public InformationUnitBuilder securityContext(InformationSecurityContext securityContext) {
        this.securityContext = securityContext;
        return this;
    }

    public InformationUnitBuilder lifecycleContext(LifecycleContext lifecycleContext) {
        this.lifecycleContext = lifecycleContext;
        return this;
    }

    public InformationUnitBuilder managementContext(ManagementContext managementContext) {
        this.managementContext = managementContext;
        return this;
    }

    public InformationUnitBuilder relationships(Collection<InformationUnitRelationship> relationships) {
        this.relationships.clear();
        if (relationships != null) {
            this.relationships.addAll(relationships);
        }
        return this;
    }

    public InformationUnitBuilder relationship(InformationUnitRelationship relationship) {
        if (relationship != null) {
            this.relationships.add(relationship);
        }
        return this;
    }

    public InformationUnitBuilder addRelationship(InformationUnitRelationship relationship) {
        return relationship(relationship);
    }

    public InformationUnitBuilder containerMetadata(ContainerMetadata containerMetadata) {
        this.containerMetadata = containerMetadata;
        return this;
    }

    public InformationUnitBuilder contentMetadata(ContentMetadata contentMetadata) {
        this.contentMetadata = contentMetadata;
        return this;
    }

    public InformationUnitBuilder content(Content content) {
        this.content = content;
        return this;
    }

    public InformationUnitBuilder content(Object contentObject) {
        if (contentObject instanceof Content c) {
            this.content = c;
        } else if (contentObject instanceof String s) {
            this.content = Content.of(s);
        } else if (contentObject instanceof byte[] b) {
            this.content = Content.of(b);
        } else if (contentObject != null) {
            this.content = Content.of(contentObject.toString());
        }
        return this;
    }

    public InformationUnit build() {
        if (this.identifier == null) {
            this.identifier = InformationUnitIdentifier.random();
        }

        InformationMetadata effectiveMeta = this.metadata;
        if (effectiveMeta == null) {
            effectiveMeta = new InformationUnitMetadata(
                    this.managementContext != null ? this.managementContext : ManagementContext.empty(),
                    this.coordinates != null ? this.coordinates : InformationCoordinates.empty(),
                    this.itinerary != null ? this.itinerary : InformationItinerary.empty(),
                    this.origin,
                    this.securityContext != null ? this.securityContext : InformationSecurityContext.empty(),
                    this.lifecycleContext != null ? this.lifecycleContext : LifecycleContext.empty(),
                    this.relationships
            );
        }

        InformationRepresentation effectiveRep = this.representation;
        if (effectiveRep == null) {
            InformationRepresentation.Builder repBuilder = InformationRepresentation.builder();
            if (this.containerMetadata != null) {
                repBuilder.containerMetadata(this.containerMetadata);
            }
            if (this.contentMetadata != null) {
                repBuilder.contentMetadata(this.contentMetadata);
            }
            if (this.content != null) {
                repBuilder.content(this.content);
                effectiveRep = repBuilder.build();
            } else {
                effectiveRep = repBuilder.buildSummary();
            }
        }

        return new InformationUnit(this.identifier, this.version, effectiveMeta, effectiveRep);
    }

    public InformationUnit buildSummary() {
        InformationUnit full = build();
        return full.toSummary();
    }
}
