/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import java.util.UUID;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationModelException;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationQuality;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitRelationship;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ModelChecks;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * Caller-confined builder for a derived IU that inherits minimum protected metadata.
 *
 * <p>The successor UUID and source-provenance link are fixed when this builder
 * is created. Its source {@link InformationMetadata} supplies immutable origin
 * and security values only; persistence and cache lifecycle values are
 * initialised afresh, and other relationships are not inherited. The caller
 * must explicitly provide the successor's concept, description, quality,
 * authority and representation. Coordinates default to unplaced and may be
 * supplied when the successor has known logical coordinates.</p>
 *
 * <p>{@link #build()} validates the complete set and does not consume or alter
 * the builder. When a required input is missing, callers can supply it and
 * retry. This builder is not thread-safe; confine it to one thread.</p>
 */
public final class MinimumMetadataSuccessorBuilder {
    private final UUID successorId;
    private final InformationMetadata sourceMetadata;
    private final InformationUnitRelationship provenance;
    private Topic informationConcept;
    private String description;
    private InformationQuality quality;
    private Topic informationAuthority;
    private InformationCoordinates coordinates = InformationCoordinates.unplaced();
    private InformationRepresentation representation;

    /** Creates the controlled builder with inherited facts fixed by its source. */
    MinimumMetadataSuccessorBuilder(UUID successorId, InformationMetadata sourceMetadata,
                                    InformationUnitRelationship provenance) {
        this.successorId = successorId;
        this.sourceMetadata = sourceMetadata;
        this.provenance = provenance;
    }

    /**
     * Supplies the successor's classification Topic.
     *
     * @param informationConcept non-null qualified classification
     * @return this builder
     */
    public MinimumMetadataSuccessorBuilder informationConcept(Topic informationConcept) {
        this.informationConcept = informationConcept;
        return this;
    }

    /**
     * Supplies the successor's description; the empty string is permitted.
     *
     * @param description non-null description
     * @return this builder
     */
    public MinimumMetadataSuccessorBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Supplies the lifetime-immutable quality assessment for this new IU.
     *
     * @param quality non-null quality value
     * @return this builder
     */
    public MinimumMetadataSuccessorBuilder quality(InformationQuality quality) {
        this.quality = quality;
        return this;
    }

    /**
     * Supplies the explicit governed information authority for this new IU.
     *
     * @param informationAuthority required authority Topic
     * @return this builder
     */
    public MinimumMetadataSuccessorBuilder informationAuthority(Topic informationAuthority) {
        this.informationAuthority = informationAuthority;
        return this;
    }

    /**
     * Supplies logical coordinates; omit this call to retain unplaced coordinates.
     *
     * @param coordinates non-null coordinate value
     * @return this builder
     */
    public MinimumMetadataSuccessorBuilder coordinates(InformationCoordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    /**
     * Supplies the complete governed representation for the new IU.
     *
     * @param representation non-null representation
     * @return this builder
     */
    public MinimumMetadataSuccessorBuilder representation(InformationRepresentation representation) {
        this.representation = representation;
        return this;
    }

    /**
     * Creates a complete immutable successor snapshot after validating all caller inputs.
     *
     * @return a valid new IU with inherited origin, security and source provenance
     * @throws InformationModelException {@code MISSING_INPUT} naming the first missing required field;
     *         this builder remains available for correction and retry
     */
    public InformationUnit build() {
        Topic concept = ModelChecks.required(informationConcept, "informationConcept");
        String validatedDescription = ModelChecks.required(description, "description");
        InformationQuality validatedQuality = ModelChecks.required(quality, "quality");
        Topic authority = ModelChecks.required(informationAuthority, "informationAuthority");
        InformationCoordinates validatedCoordinates = ModelChecks.required(coordinates, "coordinates");
        InformationRepresentation validatedRepresentation = ModelChecks.required(representation, "representation");
        InformationMetadata successorMetadata = sourceMetadata.forMinimumMetadataSuccessor(
                provenance, authority, validatedCoordinates);
        return new InformationUnit(successorId, concept, validatedDescription, validatedQuality,
                successorMetadata, validatedRepresentation);
    }
}