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

package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Encapsulates the single governed representation of an Information Unit, containing its container envelope metadata,
 * content structure metadata, and portable content payload.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit})
 * and {@code DRP-Information-Unit-Framework.Final.002.md} (§§1, 5; IU-02):
 * <ul>
 *   <li><b>EXACTLY-ONE-REPRESENTATION INVARIANT (IU-02):</b> An Information Unit contains <b>exactly one</b> governed
 *       {@code InformationRepresentation}. It SHALL NOT maintain alternative governed representations, collections of
 *       alternate encodings, or reverse transformation rules. A consumer requiring a differently structured or formatted
 *       governed representation transforms the information and creates a <b>new Information Unit</b>.</li>
 *   <li><b>THREE-FOLD CONSTITUENTS:</b>
 *     <ol>
 *       <li><b>{@link ContainerMetadata}:</b> Dimensions describing the container or transport envelope.</li>
 *       <li><b>{@link ContentMetadata}:</b> Dimensions describing the schema, model, version, format, and content reference.</li>
 *       <li><b>{@link Content}:</b> The portable represented content (textual, binary, or by reference), or working manifestation.</li>
 *     </ol>
 *   </li>
 *   <li><b>SUMMARY PROJECTION SEMANTICS:</b> The Information Unit Summary projection is an operation projection of the
 *       same Information Unit that <b>precisely excludes {@code Content}</b> while preserving {@link ContainerMetadata}
 *       and {@link ContentMetadata} (including the {@code contentReference}).
 *       The Summary is <b>not</b> a second entity, second representation kind, or independently governed record.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>{@link ContainerMetadata} and {@link ContentMetadata} are guaranteed non-null.</li>
 *   <li>Byte arrays passed as content are defensively cloned upon construction and access to preserve immutability.</li>
 *   <li>Invariant 7 (Zero-PHI diagnostic logging) is enforced through {@link Content#toString()}.</li>
 *   <li>Immutable value object design.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationRepresentation implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ContainerMetadata containerMetadata;
    private final ContentMetadata contentMetadata;
    private final Content content;

    /**
     * Creates the single governed representation from its typed constituents.
     *
     * @param containerMetadata envelope and carriage metadata, defaulted when absent
     * @param contentMetadata structure and content reference metadata, defaulted when absent
     * @param content mode-tagged portable content, defaulted to empty for a summary
     */
    @JsonCreator
    public InformationRepresentation(
            @JsonProperty("containerMetadata") ContainerMetadata containerMetadata,
            @JsonProperty("contentMetadata") ContentMetadata contentMetadata,
            @JsonProperty("content") Content content) {
        this.containerMetadata = containerMetadata != null ? containerMetadata : ContainerMetadata.empty();
        this.contentMetadata = contentMetadata != null ? contentMetadata : ContentMetadata.empty();
        this.content = content != null ? content : Content.empty();
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    /**
     * Returns the container metadata.
     *
     * @return container metadata, never null
     */
    @JsonProperty("containerMetadata")
    public ContainerMetadata containerMetadata() {
        return containerMetadata;
    }

    public ContainerMetadata getContainerMetadata() {
        return containerMetadata;
    }

    /**
     * Returns the content metadata (including structure topic, version, and content reference).
     *
     * @return content metadata, never null
     */
    @JsonProperty("contentMetadata")
    public ContentMetadata contentMetadata() {
        return contentMetadata;
    }

    public ContentMetadata getContentMetadata() {
        return contentMetadata;
    }

    /**
     * Returns the typed payload without converting its representation mode.
     *
     * @return the content value, never null
     */
    public Content content() {
        return content;
    }

    /**
     * Returns the portable JSON payload, omitting Content entirely for a Summary projection.
     *
     * @return the represented content, or null when the representation is summarized
     */
    @JsonProperty("content")
    public Content getPortableContent() {
        return content.isEmpty() ? null : content;
    }

    /**
     * Indicates whether this representation is a Summary projection (i.e. content is excluded).
     *
     * @return true if content is empty (Summary projection), false if content is present
     */
    @JsonIgnore
    public boolean isSummary() {
        return content.isEmpty();
    }

    /**
     * Indicates whether actual content is present in this representation.
     *
     * @return true if content is present, false if excluded (Summary projection)
     */
    @JsonIgnore
    public boolean hasContent() {
        return !content.isEmpty();
    }


    // =========================================================================
    // Immutable Derivations ("With" Mutators & Projections)
    // =========================================================================

    /**
     * Derives an Information Unit Summary projection by excluding precisely the {@code content} payload.
     *
     * @return new InformationRepresentation without content
     */
    public InformationRepresentation toSummary() {
        if (this.content.isEmpty()) {
            return this;
        }
        return new InformationRepresentation(this.containerMetadata, this.contentMetadata, Content.empty());
    }

    /**
     * Alias for {@link #toSummary()} emphasizing content omission.
     *
     * @return new InformationRepresentation without content
     */
    public InformationRepresentation withoutContent() {
        return toSummary();
    }

    /**
     * Derives a hydrated representation with the supplied typed content payload.
     *
     * @param newContent the new mode-tagged content
     * @return new InformationRepresentation with content
     */
    public InformationRepresentation withContent(Content newContent) {
        return new InformationRepresentation(this.containerMetadata, this.contentMetadata, newContent);
    }

    /**
     * Derives an immutable copy with updated container metadata.
     *
     * @param containerMetadata new container metadata
     * @return new InformationRepresentation
     */
    public InformationRepresentation withContainerMetadata(ContainerMetadata containerMetadata) {
        return new InformationRepresentation(containerMetadata, this.contentMetadata, this.content);
    }

    /**
     * Derives an immutable copy with updated content metadata.
     *
     * @param contentMetadata new content metadata
     * @return new InformationRepresentation
     */
    public InformationRepresentation withContentMetadata(ContentMetadata contentMetadata) {
        return new InformationRepresentation(this.containerMetadata, contentMetadata, this.content);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationRepresentation that = (InformationRepresentation) o;
        return Objects.equals(containerMetadata, that.containerMetadata) &&
                Objects.equals(contentMetadata, that.contentMetadata) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerMetadata, contentMetadata, content);
    }

    /**
     * Zero-PHI safe diagnostic logging.
     */
    @Override
    public String toString() {
        return "InformationRepresentation{" +
                "containerMetadata=" + containerMetadata +
                ", contentMetadata=" + contentMetadata +
                ", isSummary=" + isSummary() +
                ", content=" + content +
                '}';
    }

}
