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

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
 *         <li><b>Content Structure:</b> Identified through {@code contentStructureTopic} and {@code contentStructureVersion}
 *             (e.g. {@code "Health.FHIR.R5.Patient"} with structure version {@code "R5"}).</li>
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
 *   <li>Immutable value object design.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ContentMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final ContentMetadata EMPTY = new ContentMetadata(null, null, null, null, null, null, Map.of());

    private final String contentStructureTopic;
    private final String contentStructureVersion;
    private final String contentFormat;
    private final String contentReference;
    private final String contentDigest;
    private final Long contentSize;
    private final Map<String, String> attributes;

    /**
     * Canonical constructor establishing validated content metadata.
     *
     * @param contentStructureTopic   Topic describing the Content Structure (optional)
     * @param contentStructureVersion Version of the Content Structure schema/model (optional)
     * @param contentFormat           concrete format or MIME type (optional)
     * @param contentReference        opaque resolvable reference for separately managed/external content (optional)
     * @param contentDigest           cryptographic hash/digest of the content (optional)
     * @param contentSize             content size in bytes (optional)
     * @param attributes              additional content attributes (optional)
     */
    @JsonCreator
    public ContentMetadata(
            @JsonProperty("contentStructureTopic") String contentStructureTopic,
            @JsonProperty("contentStructureVersion") String contentStructureVersion,
            @JsonProperty("contentFormat") String contentFormat,
            @JsonProperty("contentReference") String contentReference,
            @JsonProperty("contentDigest") String contentDigest,
            @JsonProperty("contentSize") Long contentSize,
            @JsonProperty("attributes") Map<String, String> attributes) {
        this.contentStructureTopic = sanitize(contentStructureTopic);
        this.contentStructureVersion = sanitize(contentStructureVersion);
        this.contentFormat = sanitize(contentFormat);
        this.contentReference = sanitize(contentReference);
        this.contentDigest = sanitize(contentDigest);
        this.contentSize = contentSize != null && contentSize >= 0 ? contentSize : null;
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
     * @return empty ContentMetadata
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
    // Accessors
    // =========================================================================

    /**
     * Returns the Topic describing the Content Structure (e.g. "Health.FHIR.R5.Patient").
     *
     * @return optional Content Structure Topic
     */
    @JsonIgnore
    public Optional<String> contentStructureTopic() {
        return Optional.ofNullable(contentStructureTopic);
    }

    @JsonProperty("contentStructureTopic")
    public String getContentStructureTopic() {
        return contentStructureTopic;
    }

    /**
     * Returns the Content Structure Version (e.g. "R5", "2.4").
     *
     * @return optional Content Structure Version
     */
    @JsonIgnore
    public Optional<String> contentStructureVersion() {
        return Optional.ofNullable(contentStructureVersion);
    }

    @JsonProperty("contentStructureVersion")
    public String getContentStructureVersion() {
        return contentStructureVersion;
    }

    /**
     * Returns the concrete format or MIME type (e.g. "application/json", "application/fhir+json").
     *
     * @return optional format string
     */
    @JsonIgnore
    public Optional<String> contentFormat() {
        return Optional.ofNullable(contentFormat);
    }

    @JsonProperty("contentFormat")
    public String getContentFormat() {
        return contentFormat;
    }

    /**
     * Returns the resolvable opaque reference for separately managed or external content.
     *
     * @return optional Content reference
     */
    @JsonIgnore
    public Optional<String> contentReference() {
        return Optional.ofNullable(contentReference);
    }

    @JsonProperty("contentReference")
    public String getContentReference() {
        return contentReference;
    }

    /**
     * Indicates whether this ContentMetadata contains a resolvable content reference.
     *
     * @return true if content reference is present
     */
    @JsonIgnore
    public boolean hasContentReference() {
        return contentReference != null && !contentReference.isBlank();
    }

    /**
     * Returns the cryptographic digest/hash of the content (e.g. SHA-256).
     *
     * @return optional content digest
     */
    @JsonIgnore
    public Optional<String> contentDigest() {
        return Optional.ofNullable(contentDigest);
    }

    @JsonProperty("contentDigest")
    public String getContentDigest() {
        return contentDigest;
    }

    /**
     * Returns the content size in bytes.
     *
     * @return optional content size
     */
    @JsonIgnore
    public Optional<Long> contentSize() {
        return Optional.ofNullable(contentSize);
    }

    @JsonProperty("contentSize")
    public Long getContentSize() {
        return contentSize;
    }

    /**
     * Returns the unmodifiable map of additional content metadata attributes.
     *
     * @return unmodifiable map of attributes
     */
    @JsonProperty("attributes")
    public Map<String, String> attributes() {
        return attributes;
    }

    public Optional<String> getAttribute(String key) {
        if (key == null) return Optional.empty();
        return Optional.ofNullable(attributes.get(key));
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public ContentMetadata withContentStructure(String topic, String version) {
        return new ContentMetadata(topic, version, this.contentFormat, this.contentReference, this.contentDigest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentStructureTopic(String topic) {
        return new ContentMetadata(topic, this.contentStructureVersion, this.contentFormat, this.contentReference, this.contentDigest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentStructureVersion(String version) {
        return new ContentMetadata(this.contentStructureTopic, version, this.contentFormat, this.contentReference, this.contentDigest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentFormat(String format) {
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, format, this.contentReference, this.contentDigest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentReference(String reference) {
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, this.contentFormat, reference, this.contentDigest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentDigest(String digest) {
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, this.contentFormat, this.contentReference, digest, this.contentSize, this.attributes);
    }

    public ContentMetadata withContentSize(Long size) {
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, this.contentFormat, this.contentReference, this.contentDigest, size, this.attributes);
    }

    public ContentMetadata withAttributes(Map<String, String> attributes) {
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, this.contentFormat, this.contentReference, this.contentDigest, this.contentSize, attributes);
    }

    public ContentMetadata withAttribute(String key, String value) {
        Objects.requireNonNull(key, "attribute key must not be null");
        Map<String, String> updated = new LinkedHashMap<>(this.attributes);
        if (value == null) {
            updated.remove(key);
        } else {
            updated.put(key, value);
        }
        return new ContentMetadata(this.contentStructureTopic, this.contentStructureVersion, this.contentFormat, this.contentReference, this.contentDigest, this.contentSize, updated);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentMetadata that = (ContentMetadata) o;
        return Objects.equals(contentStructureTopic, that.contentStructureTopic)
                && Objects.equals(contentStructureVersion, that.contentStructureVersion)
                && Objects.equals(contentFormat, that.contentFormat)
                && Objects.equals(contentReference, that.contentReference)
                && Objects.equals(contentDigest, that.contentDigest)
                && Objects.equals(contentSize, that.contentSize)
                && Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentStructureTopic, contentStructureVersion, contentFormat, contentReference, contentDigest, contentSize, attributes);
    }

    @Override
    public String toString() {
        return "ContentMetadata{" +
                "structureTopic='" + contentStructureTopic + '\'' +
                ", structureVersion='" + contentStructureVersion + '\'' +
                ", format='" + contentFormat + '\'' +
                (contentReference != null ? ", reference='" + contentReference + '\'' : "") +
                (contentDigest != null ? ", digest='" + contentDigest + '\'' : "") +
                (contentSize != null ? ", size=" + contentSize : "") +
                ", attributesCount=" + attributes.size() +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    /**
     * Fluent builder for {@link ContentMetadata}.
     */
    public static final class Builder {
        private String contentStructureTopic;
        private String contentStructureVersion;
        private String contentFormat;
        private String contentReference;
        private String contentDigest;
        private Long contentSize;
        private final Map<String, String> attributes = new LinkedHashMap<>();

        private Builder() {}

        public Builder contentStructureTopic(String contentStructureTopic) {
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

        public Builder attribute(String key, String value) {
            Objects.requireNonNull(key, "key must not be null");
            if (value != null) {
                this.attributes.put(key, value);
            } else {
                this.attributes.remove(key);
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
            return new ContentMetadata(contentStructureTopic, contentStructureVersion, contentFormat,
                    contentReference, contentDigest, contentSize, attributes);
        }
    }
}
