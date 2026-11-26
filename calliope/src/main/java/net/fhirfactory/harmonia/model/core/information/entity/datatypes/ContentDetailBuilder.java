/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.time.Duration;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;

/**
 * Reusable, caller-confined builder for {@link ContentDetail}.
 *
 * <p>Each setter validates its own value and, when it rejects input, leaves
 * the previously accepted value untouched, so the caller can correct the
 * input and retry. The one cross-field rule (a checksum always travels with
 * its algorithm) is enforced by the single {@link #checksum(String, String)}
 * setter, so every accumulated state is buildable and {@link #build()} can be
 * called repeatedly. Passing {@code null} to a setter clears that value.</p>
 *
 * <p>The builder is not thread-safe; it is intended to be confined to the
 * thread that assembles one value.</p>
 */
public final class ContentDetailBuilder {
    private static final int MAX_LANGUAGE_LENGTH = 35;
    private static final int MAX_CHECKSUM_LENGTH = 1024;
    private static final int MAX_ALGORITHM_LENGTH = 64;

    Long size;
    String language;
    Long height;
    Long width;
    Long frames;
    Duration duration;
    Long pages;
    String checksum;
    String checksumAlgorithm;

    /** Creates an empty builder; obtain one through {@link ContentDetail#builder()}. */
    ContentDetailBuilder() {
    }

    /**
     * Sets the Content size in bytes; zero is valid for empty Content.
     *
     * @param size byte count, or null to clear
     * @return this builder
     * @throws InformationModelException {@code INVALID_INPUT} when negative
     */
    public ContentDetailBuilder size(Long size) {
        this.size = nonNegative(size, "contentDetail.size");
        return this;
    }

    /**
     * Sets a BCP-47 language tag, for example {@code en-AU}.
     *
     * <p>Only the tag's shape is checked (ASCII letters, digits and hyphens);
     * registry validation is outside a format-neutral model.</p>
     *
     * @param language language tag, or null to clear
     * @return this builder
     * @throws InformationModelException {@code INVALID_INPUT} when malformed
     */
    public ContentDetailBuilder language(String language) {
        if (language != null && !isLanguageTag(language)) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, "contentDetail.language");
        }
        this.language = language;
        return this;
    }

    /**
     * Sets the pixel height.
     *
     * @param height positive height, or null to clear
     * @return this builder
     * @throws InformationModelException {@code INVALID_INPUT} when not positive
     */
    public ContentDetailBuilder height(Long height) {
        this.height = positive(height, "contentDetail.height");
        return this;
    }

    /**
     * Sets the pixel width.
     *
     * @param width positive width, or null to clear
     * @return this builder
     * @throws InformationModelException {@code INVALID_INPUT} when not positive
     */
    public ContentDetailBuilder width(Long width) {
        this.width = positive(width, "contentDetail.width");
        return this;
    }

    /**
     * Sets the frame count.
     *
     * @param frames positive frame count, or null to clear
     * @return this builder
     * @throws InformationModelException {@code INVALID_INPUT} when not positive
     */
    public ContentDetailBuilder frames(Long frames) {
        this.frames = positive(frames, "contentDetail.frames");
        return this;
    }

    /**
     * Sets the playing duration.
     *
     * @param duration non-negative duration, or null to clear
     * @return this builder
     * @throws InformationModelException {@code INVALID_INPUT} when negative
     */
    public ContentDetailBuilder duration(Duration duration) {
        if (duration != null && duration.isNegative()) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, "contentDetail.duration");
        }
        this.duration = duration;
        return this;
    }

    /**
     * Sets the page count.
     *
     * @param pages positive page count, or null to clear
     * @return this builder
     * @throws InformationModelException {@code INVALID_INPUT} when not positive
     */
    public ContentDetailBuilder pages(Long pages) {
        this.pages = positive(pages, "contentDetail.pages");
        return this;
    }

    /**
     * Sets the provider-supplied digest together with its algorithm.
     *
     * <p>Both are accepted or both rejected; passing two nulls clears them.
     * The digest text (hexadecimal, Base64 or other provider encoding) is
     * preserved exactly.</p>
     *
     * @param checksum digest text, or null
     * @param checksumAlgorithm algorithm name such as {@code SHA-256}, or null
     * @return this builder
     * @throws InformationModelException {@code INCONSISTENT_INPUT} when only one is supplied,
     *         {@code INVALID_INPUT} when either contains whitespace, control characters or is too long
     */
    public ContentDetailBuilder checksum(String checksum, String checksumAlgorithm) {
        if ((checksum == null) != (checksumAlgorithm == null)) {
            throw InformationModelException.of(InformationFailureCategory.INCONSISTENT_INPUT,
                    "contentDetail.checksum");
        }
        if (checksum != null) {
            requireToken(checksum, MAX_CHECKSUM_LENGTH, "contentDetail.checksum");
            requireToken(checksumAlgorithm, MAX_ALGORITHM_LENGTH, "contentDetail.checksumAlgorithm");
        }
        this.checksum = checksum;
        this.checksumAlgorithm = checksumAlgorithm;
        return this;
    }

    /**
     * Builds an immutable snapshot; the builder remains usable.
     *
     * @return the detail value
     */
    public ContentDetail build() {
        return new ContentDetail(this);
    }

    private static Long nonNegative(Long value, String field) {
        if (value != null && value < 0) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, field);
        }
        return value;
    }

    private static Long positive(Long value, String field) {
        if (value != null && value <= 0) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, field);
        }
        return value;
    }

    /** Accepts printable ASCII without spaces, so a digest cannot hide separators or controls. */
    private static void requireToken(String value, int maxLength, String field) {
        if (value.isEmpty() || value.length() > maxLength) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, field);
        }
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (character <= ' ' || character > '~') {
                throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, field);
            }
        }
    }

    /** Checks the BCP-47 shape: alphanumeric subtags of 1-8 characters joined by single hyphens. */
    private static boolean isLanguageTag(String value) {
        if (value.isEmpty() || value.length() > MAX_LANGUAGE_LENGTH) {
            return false;
        }
        int subtagLength = 0;
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (character == '-') {
                if (subtagLength == 0) {
                    return false;
                }
                subtagLength = 0;
            } else if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')
                    || (character >= '0' && character <= '9')) {
                subtagLength++;
                if (subtagLength > 8) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return subtagLength > 0;
    }
}
