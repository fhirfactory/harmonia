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
import net.fhirfactory.harmonia.model.topic.Topic;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Encapsulates the metadata describing the Content of an Information Unit independently of the Content's actual data values.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§§8, 9, 16; IU-09) and
 * {@code DRP-Interaction-Mneme-to-Mnemosyne.Final.001.md} (§§4.3.1, 4.4, 4.5; FC-01–FC-05):
 * <ul>
 *   <li><b>CRITICAL ARCHITECTURAL DISTINCTION:</b>
 *       <pre>{@code Unit Structure ≠ Content Structure ≠ Content Format}</pre>
 *       <ul>
 *         <li><b>Unit Structure:</b> Characterises the governed Information Unit form.</li>
 *         <li><b>Content Structure:</b> Identified through {@code contentStructureTopic} (strongly typed {@link Topic})
 *             and {@code contentStructureVersion} (e.g. Topic {@code Health.FHIR.R5.Patient} with structure version {@code "R5"}).</li>
 *         <li><b>Content Format:</b> Describes the concrete encoding/serialisation MIME type (e.g. {@code "application/json"}).</li>
 *       </ul>
 *   </li>
 *   <li><b>Content Structure Version:</b> Records the schema/model version. It does <b>not</b> represent Mneme's operational
 *       cache version (which resides in {@link ManagementContext#operationalVersion()}) and does not duplicate a payload-native
 *       instance version (e.g. FHIR {@code meta.versionId}, which resides inside the native Content itself).</li>
 *   <li><b>Content Reference ({@code contentReference}):</b> An opaque resolvable reference pointing to separately managed
 *       or external Content.
 *       <ul>
 *         <li><b>Crucially preserved in the Information Unit Summary projection</b> (unlike actual Content, which is excluded).</li>
 *         <li>Opaque to Mneme; interpreted by Mnemosyne and the applicable PersistenceService together with
 *             {@link InformationCoordinates}.</li>
 *       </ul>
 *   </li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>All attributes collections are defensively copied and unmodifiable.</li>
 *   <li>String properties are trimmed; empty strings are treated as absent.</li>
 *   <li>Negative content sizes are rejected at construction.</li>
 *   <li>Immutable value object design.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ContentMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final ContentMetadata EMPTY = new ContentMetadata(null, null, null, null, null, null, Map.of());

    private final Topic contentStructureTopic;
    private final String contentStructureVersion;
    private final String contentFormat;
    private final String contentReference;
    private final String contentDigest;
    private final Long contentSize;
    private final Map<String, String> attributes;

    /**
     * Canonical constructor establishing validated content metadata.
     *
     * @param contentStructureTopic   strongly typed Topic describing the Content Structure (optional)
     * @param contentStructureVersion Version of the Content Structure schema/model (optional)
     * @param contentFormat           concrete format or MIME type (optional)
     * @param contentReference        opaque resolvable reference for separately managed/external content (optional)
     * @param contentDigest           cryptographic hash/digest of the content (optional)
     * @param contentSize             content size in bytes, must not be negative (optional)
     * @param attributes              additional content attributes (optional)
     * @throws IllegalArgumentException if {@code contentSize} is negative
     */
    @JsonCreator
    public ContentMetadata(
            @JsonProperty("contentStructureTopic") Topic contentStructureTopic,
            @JsonProperty("contentStructureVersion") String contentStructureVersion,
            @JsonProperty("contentFormat") String contentFormat,
            @JsonProperty("contentReference") String contentReference,
            @JsonProperty("contentDigest") String contentDigest,
            @JsonProperty("contentSize") Long contentSize,
            @JsonProperty("attributes") Map<String, String> attributes) {
        if (contentSize != null && contentSize < 0) {
            throw new IllegalArgumentException("contentSize must not be negative: " + contentSize);
        }
        this.contentStructureTopic = contentStructureTopic;
        this.contentStructureVersion = sanitize(contentStructureVersion);
        this.contentFormat = sanitize(contentFormat);
        this.contentReference = sanitize(contentReference);
        this.contentDigest = sanitize(contentDigest);
        this.contentSize = contentSize;
        this.attributes = attributes == null || attributes.isEmpty()
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
    }

    private static String sanitize(String input) {
        return input != null && !input.isBlank() ? input.trim() : null;
    }

    /**
     * Returns an empty, default {@code ContentMetadata} instance.
     *
     * @return an empty ContentMetadata
     */
    public static ContentMetadata empty() {
        return EMPTY;
    }

    /**
     * Creates a new builder for constructing {@code ContentMetadata} instances.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    // =========================================================================
    // Core Accessors (Strongly Typed Topic References & Values)
    // =========================================================================

    /**
     * Returns the strongly typed {@link Topic} identifying the Content Structure schema/model.
     *
     * @return the content structure type Topic, or null if unspecified
     */
    @JsonProperty("type")
    public Topic type() {
        return contentStructureTopic;
    }

    @JsonIgnore
    public Topic getType() {
        return contentStructureTopic;
    }

    @JsonIgnore
    public Optional<Topic> contentStructureTopic() {
        return Optional.ofNullable(contentStructureTopic);
    }

    @JsonProperty("contentStructureTopic")
    public Topic getContentStructureTopic() {
        return contentStructureTopic;
    }

    @JsonIgnore
    public Optional<UUID> contentStructureTopicId() {
        return contentStructureTopic().map(Topic::getId);
    }

    @JsonIgnore
    public Optional<String> contentStructureTopicName() {
        return contentStructureTopic().map(Topic::getName);
    }

    @JsonIgnore
    public Optional<String> contentStructureVersion() {
        return Optional.ofNullable(contentStructureVersion);
    }

    @JsonProperty("contentStructureVersion")
    public String getContentStructureVersion() {
        return contentStructureVersion;
    }

    @JsonIgnore
    public Optional<String> structureVersion() {
        return Optional.ofNullable(contentStructureVersion);
    }

    @JsonIgnore
    public String getStructureVersion() {
        return contentStructureVersion;
    }

    @JsonIgnore
    public Optional<String> contentFormat() {
        return Optional.ofNullable(contentFormat);
    }

    @JsonProperty("contentFormat")
    public String getContentFormat() {
        return contentFormat;
    }

    @JsonIgnore
    public Optional<String> format() {
        return Optional.ofNullable(contentFormat);
    }

    @JsonIgnore
    public String getFormat() {
        return contentFormat;
    }

    @JsonIgnore
    public Optional<String> contentReference() {
        return Optional.ofNullable(contentReference);
    }

    @JsonProperty("contentReference")
    public String getContentReference() {
        return contentReference;
    }

    @JsonIgnore
    public Optional<String> reference() {
        return Optional.ofNullable(contentReference);
    }

    @JsonIgnore
    public String getReference() {
        return contentReference;
    }

    @JsonIgnore
    public Optional<String> contentDigest() {
        return Optional.ofNullable(contentDigest);
    }

    @JsonProperty("contentDigest")
    public String getContentDigest() {
        return contentDigest;
    }

    @JsonIgnore
    public Optional<String> digest() {
        return Optional.ofNullable(contentDigest);
    }

    @JsonIgnore
    public String getDigest() {
        return contentDigest;
    }

    @JsonIgnore
    public Optional<Long> contentSize() {
        return Optional.ofNullable(contentSize);
    }

    @JsonProperty("contentSize")
    public Long getContentSize() {
        return contentSize;
    }

    @JsonIgnore
    public Optional<Long> size() {
        return Optional.ofNullable(contentSize);
    }

    @JsonIgnore
    public Long getSize() {
        return contentSize;
    }

    public Map<String, String> attributes() {
        return attributes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Optional<String> attribute(String key) {
        return Optional.ofNullable(attributes.get(key));
    }

    public Optional<String> getAttribute(String key) {
        return attribute(key);
    }

    public boolean hasContentReference() {
        return contentReference != null && !contentReference.isBlank();
    }

    public boolean isEmpty() {
        return contentStructureTopic == null
                && contentStructureVersion == null
                && contentFormat == null
                && contentReference == null
                && contentDigest == null
                && contentSize == null
                && attributes.isEmpty();
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public ContentMetadata withContentStructureTopic(Topic contentStructureTopic) {
        return new ContentMetadata(contentStructureTopic, this.contentStructureVersion, this.contentFormat, this.contentReference, this.contentDigest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentStructureVersion(String contentStructureVersion) {
        return new ContentMetadata(this.contentStructureTopic, contentStructureVersion, this.contentFormat, this.contentReference, this.contentDigest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentFormat(String contentFormat) {
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, contentFormat, this.contentReference, this.contentDigest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentReference(String contentReference) {
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, this.contentFormat, contentReference, this.contentDigest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentDigest(String contentDigest) {
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, this.contentFormat, this.contentReference, contentDigest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentSize(Long contentSize) {
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, this.contentFormat, this.contentReference, this.contentDigest, contentSize, this.attributes);
    }

    public ContentMetadata withAttributes(Map<String, String> attributes) {
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, this.contentFormat, this.contentReference, this.contentDigest, this.contentSize, attributes);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentMetadata that = (ContentMetadata) o;
        return Objects.equals(contentStructureTopic, that.contentStructureTopic) &&
                Objects.equals(contentStructureVersion, that.contentStructureVersion) &&
                Objects.equals(contentFormat, that.contentFormat) &&
                Objects.equals(contentReference, that.contentReference) &&
                Objects.equals(contentDigest, that.contentDigest) &&
                Objects.equals(contentSize, that.contentSize) &&
                Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentStructureTopic, contentStructureVersion, contentFormat, contentReference, contentDigest, contentSize, attributes);
    }

    @Override
    public String toString() {
        return "ContentMetadata{" +
                "structure=" + (contentStructureTopic != null ? contentStructureTopic.getShortId() : "null") +
                (contentStructureVersion != null ? ", version='" + contentStructureVersion + '\'' : "") +
                (contentFormat != null ? ", format='" + contentFormat + '\'' : "") +
                (contentReference != null ? ", ref='" + contentReference + '\'' : "") +
                (contentSize != null ? ", size=" + contentSize : "") +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static final class Builder {

        private Topic contentStructureTopic;
        private String contentStructureVersion;
        private String contentFormat;
        private String contentReference;
        private String contentDigest;
        private Long contentSize;
        private final Map<String, String> attributes = new LinkedHashMap<>();

        public Builder contentStructureTopic(Topic contentStructureTopic) {
            this.contentStructureTopic = contentStructureTopic;
            return this;
        }

        public Builder contentStructureVersion(String contentStructureVersion) {
            this.contentStructureVersion = contentStructureVersion;
            return this;
        }

        public Builder contentFormat(String contentFormat) {
            this.contentFormat = contentFormat;
            return this;
        }

        public Builder contentReference(String contentReference) {
            this.contentReference = contentReference;
            return this;
        }

        public Builder contentDigest(String contentDigest) {
            this.contentDigest = contentDigest;
            return this;
        }

        public Builder contentSize(Long contentSize) {
            this.contentSize = contentSize;
            return this;
        }

        public Builder putAttribute(String key, String value) {
            if (key != null && value != null) {
                this.attributes.put(key.trim(), value.trim());
            }
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes.clear();
            if (attributes != null) {
                this.attributes.putAll(attributes);
            }
            return this;
        }

        public ContentMetadata build() {
            return new ContentMetadata(
                    contentStructureTopic,
                    contentStructureVersion,
                    contentFormat,
                    contentReference,
                    contentDigest,
                    contentSize,
                    attributes);
        }
    }
}
