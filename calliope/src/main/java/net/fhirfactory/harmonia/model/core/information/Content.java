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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Encapsulates the governed portable information payload within an {@link InformationRepresentation}.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit})
 * and {@code DRP-Information-Unit-Framework.Final.002.md} (§§1, 5):
 * <ul>
 *   <li><b>Portable Contract:</b> The governed contract for Content is portable <b>textual</b>, <b>binary</b>,
 *       or <b>by reference</b> (the large-binary pass-by-reference exception).</li>
 *   <li><b>Working Manifestation:</b> Native Java domain objects may serve as working manifestations for in-memory
 *       processing, but an uncontrolled Java object graph cannot be the sole canonical interchange or persistence
 *       contract for non-Java peers and archival use.</li>
 *   <li><b>Native Version Preservation:</b> Intrinsic instance versions (such as FHIR {@code meta.versionId}) remain
 *       inside the native Content; none is fabricated for an unversioned payload.</li>
 *   <li><b>Invariant 7 (Zero-PHI Diagnostic Logging):</b> {@link #toString()} produces a diagnostic summary
 *       (mode and size) and <b>never emits raw payload data or Protected Health Information (PHI)</b> into logs.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Immutable value object design.</li>
 *   <li>Byte arrays are defensively copied upon construction and access.</li>
 *   <li>Null-safe inspection and conversion helpers.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Mode describing the representation format of this Content payload.
     */
    public enum Mode {
        EMPTY,
        TEXT,
        BINARY,
        REFERENCE,
        WORKING
    }

    private static final Content EMPTY = new Content(Mode.EMPTY, null, null, null, null);

    private final Mode mode;
    private final String textContent;
    private final byte[] binaryContent;
    private final String reference;
    private final transient Object workingManifestation;

    @JsonCreator
    public Content(
            @JsonProperty("mode") Mode mode,
            @JsonProperty("textContent") String textContent,
            @JsonProperty("binaryContent") byte[] binaryContent,
            @JsonProperty("reference") String reference) {
        this(mode, textContent, binaryContent, reference, null);
    }

    private Content(
            Mode mode,
            String textContent,
            byte[] binaryContent,
            String reference,
            Object workingManifestation) {
        this.mode = mode != null ? mode : Mode.EMPTY;
        this.textContent = textContent;
        this.binaryContent = binaryContent != null ? Arrays.copyOf(binaryContent, binaryContent.length) : null;
        this.reference = reference != null && !reference.isBlank() ? reference.trim() : null;
        this.workingManifestation = workingManifestation;
    }

    // =========================================================================
    // Factory Methods
    // =========================================================================

    /**
     * Returns an empty Content instance representing the absence of an inline payload
     * (e.g. in Summary projection or unpopulated representations).
     *
     * @return empty Content instance
     */
    public static Content empty() {
        return EMPTY;
    }

    /**
     * Creates a portable textual Content instance (e.g. JSON, XML, or plain text).
     *
     * @param text the textual payload, must not be null
     * @return textual Content
     */
    public static Content text(String text) {
        Objects.requireNonNull(text, "text must not be null");
        return new Content(Mode.TEXT, text, null, null, null);
    }

    /**
     * Creates a portable binary Content instance (e.g. PDF, image, FHIR binary, or wire bytes).
     * The input array is defensively copied.
     *
     * @param bytes the binary payload, must not be null
     * @return binary Content
     */
    public static Content binary(byte[] bytes) {
        Objects.requireNonNull(bytes, "bytes must not be null");
        return new Content(Mode.BINARY, null, bytes, null, null);
    }

    /**
     * Creates a pass-by-reference Content instance for large, external, or separately managed payloads.
     *
     * @param reference the opaque resolvable reference (URI or identifier), must not be null or blank
     * @return by-reference Content
     */
    public static Content byReference(String reference) {
        Objects.requireNonNull(reference, "reference must not be null");
        if (reference.isBlank()) {
            throw new IllegalArgumentException("reference must not be blank");
        }
        return new Content(Mode.REFERENCE, null, null, reference.trim(), null);
    }

    /**
     * Creates a Content instance encapsulating an in-memory Java working manifestation.
     *
     * @param workingObject the working object, must not be null
     * @return working Content
     */
    public static Content working(Object workingObject) {
        Objects.requireNonNull(workingObject, "workingObject must not be null");
        if (workingObject instanceof String s) {
            return text(s);
        }
        if (workingObject instanceof byte[] b) {
            return binary(b);
        }
        if (workingObject instanceof Content c) {
            return c;
        }
        return new Content(Mode.WORKING, null, null, null, workingObject);
    }

    /**
     * Intelligently creates a Content instance from any given object value.
     *
     * @param value payload object, string, byte array, or reference
     * @return populated Content or empty Content if null
     */
    public static Content of(Object value) {
        if (value == null) {
            return empty();
        }
        if (value instanceof Content c) {
            return c;
        }
        if (value instanceof String s) {
            return text(s);
        }
        if (value instanceof byte[] b) {
            return binary(b);
        }
        return working(value);
    }

    // =========================================================================
    // Accessors and Inspection
    // =========================================================================

    @JsonProperty("mode")
    public Mode mode() {
        return mode;
    }

    @JsonIgnore
    public Mode getMode() {
        return mode;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return mode == Mode.EMPTY;
    }

    @JsonIgnore
    public boolean isText() {
        return mode == Mode.TEXT;
    }

    @JsonIgnore
    public boolean isBinary() {
        return mode == Mode.BINARY;
    }

    @JsonIgnore
    public boolean isReference() {
        return mode == Mode.REFERENCE;
    }

    @JsonIgnore
    public boolean isWorking() {
        return mode == Mode.WORKING;
    }

    /**
     * Returns the textual representation, or empty Optional if not textual.
     * If the content is binary, it is decoded as UTF-8.
     *
     * @return optional text content
     */
    @JsonIgnore
    public Optional<String> textContent() {
        if (textContent != null) {
            return Optional.of(textContent);
        }
        if (binaryContent != null) {
            return Optional.of(new String(binaryContent, StandardCharsets.UTF_8));
        }
        return Optional.empty();
    }

    @JsonProperty("textContent")
    public String getTextContent() {
        return textContent;
    }

    @JsonIgnore
    public String asString() {
        return textContent().orElse(null);
    }

    /**
     * Returns a defensive copy of the binary content, or empty Optional if not present.
     * If the content is text, it is encoded as UTF-8 bytes.
     *
     * @return optional binary byte array copy
     */
    @JsonIgnore
    public Optional<byte[]> binaryContent() {
        if (binaryContent != null) {
            return Optional.of(Arrays.copyOf(binaryContent, binaryContent.length));
        }
        if (textContent != null) {
            return Optional.of(textContent.getBytes(StandardCharsets.UTF_8));
        }
        return Optional.empty();
    }

    @JsonProperty("binaryContent")
    public byte[] getBinaryContent() {
        return binaryContent != null ? Arrays.copyOf(binaryContent, binaryContent.length) : null;
    }

    @JsonIgnore
    public byte[] asBytes() {
        return binaryContent().orElse(null);
    }

    /**
     * Returns the external/pass-by-reference identifier, or empty Optional if not reference mode.
     *
     * @return optional reference string
     */
    @JsonIgnore
    public Optional<String> reference() {
        return Optional.ofNullable(reference);
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonIgnore
    public String asReference() {
        return reference;
    }

    /**
     * Returns the in-memory Java working manifestation, if present.
     *
     * @return optional working manifestation object
     */
    @JsonIgnore
    public Optional<Object> workingManifestation() {
        if (workingManifestation != null) {
            return Optional.of(workingManifestation);
        }
        if (textContent != null) {
            return Optional.of(textContent);
        }
        if (binaryContent != null) {
            return Optional.of(Arrays.copyOf(binaryContent, binaryContent.length));
        }
        return Optional.empty();
    }

    /**
     * Type-safe extraction of the payload manifestation.
     *
     * @param targetType target class
     * @param <T>        target type
     * @return optional cast manifestation
     */
    @SuppressWarnings("unchecked")
    @JsonIgnore
    public <T> Optional<T> as(Class<T> targetType) {
        Objects.requireNonNull(targetType, "targetType must not be null");
        if (targetType == String.class) {
            return (Optional<T>) textContent();
        }
        if (targetType == byte[].class) {
            return (Optional<T>) binaryContent();
        }
        if (targetType.isInstance(this)) {
            return Optional.of((T) this);
        }
        if (workingManifestation != null && targetType.isInstance(workingManifestation)) {
            return Optional.of((T) workingManifestation);
        }
        return Optional.empty();
    }

    /**
     * Returns the estimated size of the content payload in bytes/characters.
     *
     * @return size in bytes/characters, or 0 if empty/reference
     */
    @JsonIgnore
    public long size() {
        if (binaryContent != null) {
            return binaryContent.length;
        }
        if (textContent != null) {
            return textContent.length();
        }
        return 0L;
    }

    // =========================================================================
    // Identity, Equality, and Safe Diagnostic Logging
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content that = (Content) o;
        if (mode != that.mode) return false;
        if (!Objects.equals(textContent, that.textContent)) return false;
        if (!Arrays.equals(binaryContent, that.binaryContent)) return false;
        if (!Objects.equals(reference, that.reference)) return false;
        return Objects.equals(workingManifestation, that.workingManifestation);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mode, textContent, reference, workingManifestation);
        result = 31 * result + Arrays.hashCode(binaryContent);
        return result;
    }

    /**
     * Zero-PHI diagnostic representation. Emits only mode and size/reference descriptors;
     * <b>never outputs payload text, bytes, or clinical observation content.</b>
     */
    @Override
    public String toString() {
        return switch (mode) {
            case EMPTY -> "Content[EMPTY]";
            case TEXT -> "Content[TEXT, length=" + (textContent != null ? textContent.length() : 0) + "]";
            case BINARY -> "Content[BINARY, bytes=" + (binaryContent != null ? binaryContent.length : 0) + "]";
            case REFERENCE -> "Content[REFERENCE, uri=" + reference + "]";
            case WORKING -> "Content[WORKING, type=" + (workingManifestation != null ? workingManifestation.getClass().getSimpleName() : "null") + "]";
        };
    }
}
