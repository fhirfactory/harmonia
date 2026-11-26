/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;

/**
 * One portable Content carrier: either inline bytes or an external locator.
 *
 * <p>Draft 002.005 §1.7 requires exactly one of {@code reference} or
 * {@code data}. The two factories make the "both" and "neither" states
 * unrepresentable. Calliope preserves supplied bytes exactly and treats a
 * reference as an opaque locator for externally persisted Content: it never
 * retrieves, verifies, decodes or manages referenced Content. Generating and
 * associating provider references belongs to Mneme, Mnemosyne and the
 * PersistenceService under the governed interaction contract.</p>
 *
 * <p>Byte arrays are copied on the way in and on the way out, so a caller can
 * never change a governed representation by mutating a shared buffer.</p>
 */
public final class Content {
    private final URI reference;
    private final byte[] data;

    private Content(URI reference, byte[] data) {
        this.reference = reference;
        this.data = data;
    }

    /**
     * Creates Content carried inline as an exact byte sequence.
     *
     * <p>An IU may be constructed with inline Content before any persistence
     * reference exists.</p>
     *
     * @param data bytes to preserve; an empty array is valid Content
     * @return an immutable defensive snapshot
     * @throws InformationModelException {@code MISSING_INPUT} when data is null
     */
    public static Content inline(byte[] data) {
        return new Content(null, ModelChecks.required(data, "content.data").clone());
    }

    /**
     * Creates inline Content from text, encoded exactly once as UTF-8.
     *
     * <p>This is a convenience for text Content; the encoding is a carriage
     * decision and is not recorded as a schema.</p>
     *
     * @param text text to carry
     * @return inline Content holding the UTF-8 bytes of the text
     * @throws InformationModelException {@code MISSING_INPUT} when text is null
     */
    public static Content inlineText(String text) {
        return new Content(null, ModelChecks.required(text, "content.data").getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Creates Content represented only by a provider-supplied absolute locator.
     *
     * @param reference opaque absolute URI for externally persisted Content
     * @return a reference-only Content value
     * @throws InformationModelException {@code MISSING_INPUT} for null, {@code INVALID_INPUT}
     *         for a relative URI
     */
    public static Content reference(URI reference) {
        ModelChecks.required(reference, "content.reference");
        if (!reference.isAbsolute()) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, "content.reference");
        }
        return new Content(reference, null);
    }

    /**
     * Returns whether the Content is carried inline.
     *
     * @return true for inline data, false for a reference
     */
    public boolean isInline() {
        return data != null;
    }

    /**
     * Returns the opaque locator; no remote lookup is performed.
     *
     * @return the reference, or empty for inline Content
     */
    public Optional<URI> getReference() {
        return Optional.ofNullable(reference);
    }

    /**
     * Returns an independent copy of the inline bytes.
     *
     * @return a fresh copy of the data, or empty for reference-only Content
     */
    public Optional<byte[]> getData() {
        return data == null ? Optional.empty() : Optional.of(data.clone());
    }

    /** Compares the carrier kind and exact bytes or locator. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Content content)) {
            return false;
        }
        return Objects.equals(reference, content.reference) && Arrays.equals(data, content.data);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(reference) + Arrays.hashCode(data);
    }

    /** Omits both payload bytes and locator because either may be sensitive. */
    @Override
    public String toString() {
        return "Content{carrier=" + (data == null ? "reference" : "inline") + "}";
    }
}
