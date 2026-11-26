/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import java.util.UUID;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationModelException;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationQuality;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ModelChecks;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * Reusable, caller-confined builder for {@link InformationUnit}.
 *
 * <p>Setters only record values; {@link #build()} validates the complete set
 * and either returns a new immutable IU or throws without changing anything.
 * A failed build therefore preserves every input so the caller can correct
 * the missing value and retry, and a successful build can be repeated to
 * produce equal snapshots.</p>
 *
 * <p>Required: {@code id}, {@code informationConcept}, {@code description}
 * (may be empty), {@code quality}, {@code metadata} and
 * {@code representation}. Use {@link InformationMetadata#initial(Topic)} to
 * obtain metadata with every container initialised.</p>
 *
 * <p>Not thread-safe; confine each builder to one thread.</p>
 */
public final class InformationUnitBuilder {
    private UUID id;
    private Topic informationConcept;
    private String description;
    private InformationQuality quality;
    private InformationMetadata metadata;
    private InformationRepresentation representation;

    /** Creates an empty builder; obtain one through {@link InformationUnit#builder()}. */
    InformationUnitBuilder() {
    }

    /**
     * Sets the {@code id} input; validation happens in {@link #build()}.
     *
     * @param id the IU's sole instance identifier
     * @return this builder
     */
    public InformationUnitBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the {@code informationConcept} input; validation happens in {@link #build()}.
     *
     * @param informationConcept the qualified classification Topic
     * @return this builder
     */
    public InformationUnitBuilder informationConcept(Topic informationConcept) {
        this.informationConcept = informationConcept;
        return this;
    }

    /**
     * Sets the {@code description} input; validation happens in {@link #build()}.
     *
     * @param description free-text description; may be empty
     * @return this builder
     */
    public InformationUnitBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the {@code quality} input; validation happens in {@link #build()}.
     *
     * @param quality lifetime-immutable quality assessment
     * @return this builder
     */
    public InformationUnitBuilder quality(InformationQuality quality) {
        this.quality = quality;
        return this;
    }

    /**
     * Sets the {@code metadata} input; validation happens in {@link #build()}.
     *
     * @param metadata initialised governed metadata
     * @return this builder
     */
    public InformationUnitBuilder metadata(InformationMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Sets the {@code representation} input; validation happens in {@link #build()}.
     *
     * @param representation the single governed representation
     * @return this builder
     */
    public InformationUnitBuilder representation(InformationRepresentation representation) {
        this.representation = representation;
        return this;
    }

    /**
     * Validates all inputs and creates an immutable IU snapshot.
     *
     * @return a new InformationUnit
     * @throws InformationModelException {@code MISSING_INPUT} naming the first absent field;
     *         the builder is left unchanged
     */
    public InformationUnit build() {
        return new InformationUnit(ModelChecks.required(id, "id"),
                ModelChecks.required(informationConcept, "informationConcept"),
                ModelChecks.required(description, "description"),
                ModelChecks.required(quality, "quality"),
                ModelChecks.required(metadata, "metadata"),
                ModelChecks.required(representation, "representation"));
    }
}
