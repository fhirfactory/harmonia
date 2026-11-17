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
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.ContentMode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
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

    private static final Content EMPTY = new Content(ContentMode.EMPTY, null, null, null, null);

    private final ContentMode mode;
    private final String textContent;
    private final byte[] binaryContent;
    private final String reference;
    private final transient Object workingManifestation;

    @JsonCreator
    public Content(
            @JsonProperty("mode") ContentMode mode,
            @JsonProperty("textContent") String textContent,
            @JsonProperty("binaryContent") byte[] binaryContent,
            @JsonProperty("reference") String reference) {
        this(mode, textContent, binaryContent, reference, null);
    }

    private Content(
            ContentMode mode,
            String textContent,
            byte[] binaryContent,
            String reference,
            Object workingManifestation) {
        this.mode = mode != null ? mode : ContentMode.EMPTY;
        this.textContent = textContent;
        this.binaryContent = binaryContent != null ? Arrays.copyOf(binaryContent, binaryContent.length) : null;
        this.reference = reference != null && !reference.isBlank() ? reference.trim() : null;
        this.workingManifestation = workingManifestation;
        validatePayload(this.mode, this.textContent, this.binaryContent, this.reference, this.workingManifestation);
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
        return new Content(ContentMode.TEXT, text, null, null, null);
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
        return new Content(ContentMode.BINARY, null, bytes, null, null);
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
        return new Content(ContentMode.REFERENCE, null, null, reference.trim(), null);
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
        return new Content(ContentMode.WORKING, null, null, null, workingObject);
    }


    // =========================================================================
    // Accessors and Inspection
    // =========================================================================

    /** Returns the in-memory mode without converting the payload. */
    public ContentMode mode() {
        return mode;
    }

    /**
     * Returns the portable mode for JSON serialization and rejects working-only content.
     *
     * @return the portable content mode
     * @throws IllegalStateException when this content contains only a Java working manifestation
     */
    @JsonProperty("mode")
    public ContentMode portableMode() {
        if (mode == ContentMode.WORKING) {
            throw new IllegalStateException("Working content has no portable JSON representation");
        }
        return mode;
    }


    /** Indicates whether this Content has no represented payload. */
    @JsonIgnore
    public boolean isEmpty() {
        return mode == ContentMode.EMPTY;
    }

    /** Indicates whether the canonical payload is textual. */
    @JsonIgnore
    public boolean isText() {
        return mode == ContentMode.TEXT;
    }

    /** Indicates whether the canonical payload is binary. */
    @JsonIgnore
    public boolean isBinary() {
        return mode == ContentMode.BINARY;
    }

    /** Indicates whether the canonical payload is represented by reference. */
    @JsonIgnore
    public boolean isReference() {
        return mode == ContentMode.REFERENCE;
    }

    /** Indicates whether the payload is available only as an in-memory Java manifestation. */
    @JsonIgnore
    public boolean isWorking() {
        return mode == ContentMode.WORKING;
    }

    /**
     * Returns the textual view of the payload, decoding binary content as UTF-8 when needed.
     *
     * @return optional text content
     */
    public Optional<String> textContent() {
        if (textContent != null) {
            return Optional.of(textContent);
        }
        if (binaryContent != null) {
            return Optional.of(new String(binaryContent, StandardCharsets.UTF_8));
        }
        return Optional.empty();
    }

    /** Returns the canonical text field for JSON serialization. */
    @JsonProperty("textContent")
    public String getTextContent() {
        return textContent;
    }

    /**
     * Returns a defensive byte view, encoding text as UTF-8 when needed.
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

    /** Returns a defensive copy of the canonical binary field for JSON serialization. */
    @JsonProperty("binaryContent")
    public byte[] getBinaryContent() {
        return binaryContent != null ? Arrays.copyOf(binaryContent, binaryContent.length) : null;
    }


    /**
     * Returns the external/pass-by-reference identifier, or empty Optional if not reference mode.
     *
     * @return optional reference string
     */
    public Optional<String> reference() {
        return Optional.ofNullable(reference);
    }

    /** Returns the opaque reference for JSON serialization. */
    @JsonProperty("reference")
    public String getReference() {
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

    /** Compares mode and payload values, including binary-array contents. */
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

    /** Hashes the mode and payload values consistently with {@link #equals(Object)}. */
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
            case REFERENCE -> "Content[REFERENCE]";
            case WORKING -> "Content[WORKING, type=" + (workingManifestation != null ? workingManifestation.getClass().getSimpleName() : "null") + "]";
        };
    }

    /**
     * Rejects mode/value combinations that cannot represent a valid Content instance.
     *
     * @param mode the declared content mode
     * @param textContent textual payload, when applicable
     * @param binaryContent binary payload, when applicable
     * @param reference opaque reference, when applicable
     * @param workingManifestation in-memory Java manifestation, when applicable
     * @throws IllegalArgumentException when the mode and payload values are inconsistent
     */
    private static void validatePayload(
            ContentMode mode,
            String textContent,
            byte[] binaryContent,
            String reference,
            Object workingManifestation) {
        boolean hasText = textContent != null;
        boolean hasBinary = binaryContent != null;
        boolean hasReference = reference != null;
        boolean hasWorkingManifestation = workingManifestation != null;

        boolean valid = switch (mode) {
            case EMPTY -> !hasText && !hasBinary && !hasReference && !hasWorkingManifestation;
            case TEXT -> hasText && !hasBinary && !hasReference && !hasWorkingManifestation;
            case BINARY -> !hasText && hasBinary && !hasReference && !hasWorkingManifestation;
            case REFERENCE -> !hasText && !hasBinary && hasReference && !hasWorkingManifestation;
            case WORKING -> !hasText && !hasBinary && !hasReference && hasWorkingManifestation;
        };
        if (!valid) {
            throw new IllegalArgumentException("Content payload does not match mode " + mode);
        }
    }

    /**
     * Replaces this value with a validated portable form for Java serialization.
     *
     * @return the serialization proxy for portable modes
     * @throws NotSerializableException when the value contains only an in-memory working manifestation
     */
    private Object writeReplace() throws ObjectStreamException {
        if (mode == ContentMode.WORKING) {
            throw new NotSerializableException("Working content has no portable Java serialization form");
        }
        return new ContentSerializationProxy(this);
    }

    /**
     * Prevents Java from bypassing the validated serialization proxy.
     *
     * @param input serialization stream supplying this object
     * @throws IOException because direct field-based deserialization is unsupported
     */
    private void readObject(ObjectInputStream input) throws IOException {
        throw new InvalidObjectException("Content must be reconstructed through its serialization proxy");
    }
}
