/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Objects;

/**
 * The single governed representation of an InformationUnit.
 *
 * <p>Each IU has exactly one representation (Draft 002.005 §1.6). Governed
 * revisions preserve it, and this class deliberately offers no "with" methods:
 * a transformation that produces a new representation - even a semantically
 * equivalent one - must create a new IU with a new UUID and provenance
 * linking it to its source. Establishing a persistence locator later does not
 * replace this value either; Mnemosyne records that locator in the single
 * authoritative association for the IU revision (§2).</p>
 *
 * <p>Immutable and safe for concurrent use.</p>
 */
public final class InformationRepresentation {
    private final ContainerMetadata containerMetadata;
    private final ContentMetadata contentMetadata;
    private final Content content;

    private InformationRepresentation(ContainerMetadata containerMetadata, ContentMetadata contentMetadata,
                                      Content content) {
        this.containerMetadata = containerMetadata;
        this.contentMetadata = contentMetadata;
        this.content = content;
    }

    /**
     * Creates the governed representation.
     *
     * @param containerMetadata container concept and schema
     * @param contentMetadata content concept, schema and detail
     * @param content exactly one inline or reference carrier
     * @return an immutable representation
     * @throws InformationModelException {@code MISSING_INPUT} when any argument is null
     */
    public static InformationRepresentation of(ContainerMetadata containerMetadata,
                                               ContentMetadata contentMetadata, Content content) {
        return new InformationRepresentation(
                ModelChecks.required(containerMetadata, "representation.containerMetadata"),
                ModelChecks.required(contentMetadata, "representation.contentMetadata"),
                ModelChecks.required(content, "representation.content"));
    }

    /**
     * Returns the container description.
     *
     * @return the container description
     */
    public ContainerMetadata getContainerMetadata() {
        return containerMetadata;
    }

    /**
     * Returns the Content description.
     *
     * @return the Content description
     */
    public ContentMetadata getContentMetadata() {
        return contentMetadata;
    }

    /**
     * Returns the Content carrier, which defensively copies inline bytes.
     *
     * @return the Content carrier, which defensively copies inline bytes
     */
    public Content getContent() {
        return content;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof InformationRepresentation that)) {
            return false;
        }
        return containerMetadata.equals(that.containerMetadata) && contentMetadata.equals(that.contentMetadata)
                && content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerMetadata, contentMetadata, content);
    }

    /** Delegates to constituents that already omit payload and locator values. */
    @Override
    public String toString() {
        return "InformationRepresentation{" + containerMetadata + ", " + contentMetadata + ", " + content + "}";
    }
}
