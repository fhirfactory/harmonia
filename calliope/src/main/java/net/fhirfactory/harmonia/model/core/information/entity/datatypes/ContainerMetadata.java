/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Objects;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * Describes the container in which an InformationUnit's Content is carried.
 *
 * <p>{@code containerConcept} says what the container represents (for
 * example, a FHIR Bundle or an HL7 v2 batch); {@code containerSchema}
 * identifies its schema, and {@code containerMediaType} records the IANA media
 * type identifier used to carry it. All three are required: every container
 * has a concept, and a container without additional structure uses the
 * predefined qualified Calliope {@code RAW} Topic, which the factory package supplies
 * (Draft 002.005 §1.6). These Topics are independent of the IU's own
 * {@code informationConcept} classification.</p>
 *
 * <p>Immutable and safe for concurrent use.</p>
 */
public final class ContainerMetadata {
    private final Topic containerConcept;
    private final Topic containerSchema;
    private final String containerMediaType;

    private ContainerMetadata(Topic containerConcept, Topic containerSchema, String containerMediaType) {
        this.containerConcept = containerConcept;
        this.containerSchema = containerSchema;
        this.containerMediaType = containerMediaType;
    }

    /**
     * Creates container metadata from an explicit concept, schema and media type.
     *
     * @param containerConcept what the container represents
     * @param containerSchema the container's schema, or the {@code RAW} Topic
     * @param containerMediaType IANA media type identifier, for example {@code application/fhir+json}
     * @return an immutable value
     * @throws InformationModelException {@code MISSING_INPUT} when an argument is null;
     *         {@code INVALID_INPUT} when the media type is malformed
     */
    public static ContainerMetadata of(Topic containerConcept, Topic containerSchema, String containerMediaType) {
        return new ContainerMetadata(ModelChecks.required(containerConcept, "container.containerConcept"),
                ModelChecks.required(containerSchema, "container.containerSchema"),
                ModelChecks.mediaType(containerMediaType, "container.containerMediaType"));
    }

    /**
     * Returns what the container represents.
     *
     * @return what the container represents
     */
    public Topic getContainerConcept() {
        return containerConcept;
    }

    /**
     * Returns the container schema Topic, including its entry versions.
     *
     * @return the container schema Topic, including its entry versions
     */
    public Topic getContainerSchema() {
        return containerSchema;
    }

    /**
     * Returns the normalised IANA media type identifier for the container.
     *
     * @return the normalised IANA media type identifier for the container
     */
    public String getContainerMediaType() {
        return containerMediaType;
    }

    /** Compares Topics including entry versions, because schema versions are meaningful. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ContainerMetadata that)) {
            return false;
        }
        return ModelChecks.sameVersionedTopic(containerConcept, that.containerConcept)
                && ModelChecks.sameVersionedTopic(containerSchema, that.containerSchema)
                && containerMediaType.equals(that.containerMediaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerConcept, containerSchema, containerMediaType);
    }

    /** Classification Topics only; no Content is reachable from this value. */
    @Override
    public String toString() {
        return "ContainerMetadata{concept=" + containerConcept.getCanonicalNameWithVersion()
                + ", schema=" + containerSchema.getCanonicalNameWithVersion()
                + ", mediaType=" + containerMediaType + "}";
    }
}
