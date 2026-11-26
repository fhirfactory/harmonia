/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Objects;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * Describes what the Content is and how it is structured.
 *
 * <p>Draft 002.005 §1.6 keeps content concept, schema and media type apart:</p>
 * <ul>
 *   <li>{@code contentConcept} - what the Content represents;</li>
 *   <li>{@code contentSchema} - its structure, schema or profile, with version
 *       semantics carried by the Topic's entry versions;</li>
 *   <li>format and encoding (for example JSON versus XML) - deliberately
 *       <em>not</em> substituted for the schema, so a FHIR Patient keeps the
 *       same schema whichever serialisation carries it; {@code contentMediaType}
 *       separately records its IANA media type identifier.</li>
 * </ul>
 * <p>The native payload's own instance version stays inside the native Content
 * and is not copied here. {@link ContentDetail} is always initialised; use
 * {@link ContentDetail#empty()} when nothing is known.</p>
 *
 * <p>Immutable and safe for concurrent use.</p>
 */
public final class ContentMetadata {
    private final Topic contentConcept;
    private final Topic contentSchema;
    private final String contentMediaType;
    private final ContentDetail contentDetail;

    private ContentMetadata(Topic contentConcept, Topic contentSchema, String contentMediaType,
                            ContentDetail contentDetail) {
        this.contentConcept = contentConcept;
        this.contentSchema = contentSchema;
        this.contentMediaType = contentMediaType;
        this.contentDetail = contentDetail;
    }

    /**
     * Creates content metadata.
     *
     * @param contentConcept what the Content represents
     * @param contentSchema the Content's structure, schema or profile
     * @param contentMediaType IANA media type identifier, for example {@code application/fhir+json}
     * @param contentDetail provider-supplied details; use {@link ContentDetail#empty()} when unknown
     * @return an immutable value
     * @throws InformationModelException {@code MISSING_INPUT} when an argument is null;
     *         {@code INVALID_INPUT} when the media type is malformed
     */
    public static ContentMetadata of(Topic contentConcept, Topic contentSchema, String contentMediaType,
                                     ContentDetail contentDetail) {
        return new ContentMetadata(ModelChecks.required(contentConcept, "contentMetadata.contentConcept"),
                ModelChecks.required(contentSchema, "contentMetadata.contentSchema"),
                ModelChecks.mediaType(contentMediaType, "contentMetadata.contentMediaType"),
                ModelChecks.required(contentDetail, "contentMetadata.contentDetail"));
    }

    /**
     * Returns what the Content represents.
     *
     * @return what the Content represents
     */
    public Topic getContentConcept() {
        return contentConcept;
    }

    /**
     * Returns the schema Topic; its entry versions carry schema version semantics.
     *
     * @return the schema Topic; its entry versions carry schema version semantics
     */
    public Topic getContentSchema() {
        return contentSchema;
    }

    /**
     * Returns the normalised IANA media type identifier for the Content.
     *
     * @return the normalised IANA media type identifier for the Content
     */
    public String getContentMediaType() {
        return contentMediaType;
    }

    /**
     * Returns the always-initialised content detail.
     *
     * @return the always-initialised content detail
     */
    public ContentDetail getContentDetail() {
        return contentDetail;
    }

    /** Compares Topics including entry versions, because schema versions are meaningful. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ContentMetadata that)) {
            return false;
        }
        return ModelChecks.sameVersionedTopic(contentConcept, that.contentConcept)
                && ModelChecks.sameVersionedTopic(contentSchema, that.contentSchema)
                && contentMediaType.equals(that.contentMediaType)
                && contentDetail.equals(that.contentDetail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentConcept, contentSchema, contentMediaType, contentDetail);
    }

    /** Classification Topics and detail presence only; never payload values. */
    @Override
    public String toString() {
        return "ContentMetadata{concept=" + contentConcept.getCanonicalNameWithVersion()
                + ", schema=" + contentSchema.getCanonicalNameWithVersion()
                + ", mediaType=" + contentMediaType + ", " + contentDetail + "}";
    }
}
