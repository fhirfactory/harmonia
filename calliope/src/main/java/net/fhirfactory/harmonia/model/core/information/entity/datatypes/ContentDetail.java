/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * Optional provider-supplied characteristics of the represented Content.
 *
 * <p>Every value is optional because not every value is meaningful for every
 * Content (a PNG has no duration; an HL7 message has no pages). An empty
 * instance is the initialised default required by Draft 002.005 §1.</p>
 *
 * <p>The content provider supplies {@code size} (in bytes), {@code checksum}
 * and {@code checksumAlgorithm} for the actual Content bytes - for a
 * reference, the referenced bytes. Calliope does not compute or verify any
 * digest and does not parse Content to check dimensions; the PersistenceService
 * decides how and when verification occurs (§1.7). A checksum and its
 * algorithm must be supplied together so a digest can never be read under the
 * wrong algorithm. {@link #DEFAULT_CHECKSUM_ALGORITHM} is the default a
 * provider uses when it generates a digest; Calliope never silently assigns it
 * to a caller-supplied checksum.</p>
 */
public final class ContentDetail {
    /** Default digest algorithm for provider-generated checksums. */
    public static final String DEFAULT_CHECKSUM_ALGORITHM = "SHA-256";

    private static final ContentDetail EMPTY = new ContentDetail(new ContentDetailBuilder());

    private final Long size;
    private final String language;
    private final Long height;
    private final Long width;
    private final Long frames;
    private final Duration duration;
    private final Long pages;
    private final String checksum;
    private final String checksumAlgorithm;

    /** Captures an already validated builder state. */
    ContentDetail(ContentDetailBuilder builder) {
        this.size = builder.size;
        this.language = builder.language;
        this.height = builder.height;
        this.width = builder.width;
        this.frames = builder.frames;
        this.duration = builder.duration;
        this.pages = builder.pages;
        this.checksum = builder.checksum;
        this.checksumAlgorithm = builder.checksumAlgorithm;
    }

    /**
     * Returns a new, reusable, caller-confined builder.
     *
     * @return an empty builder
     */
    public static ContentDetailBuilder builder() {
        return new ContentDetailBuilder();
    }

    /**
     * Returns the empty detail value for Content without known details.
     *
     * @return a shared immutable empty instance
     */
    public static ContentDetail empty() {
        return EMPTY;
    }

    /**
     * Returns the Content size in bytes, when supplied.
     *
     * @return the Content size in bytes, when supplied
     */
    public Optional<Long> getSize() {
        return Optional.ofNullable(size);
    }

    /**
     * Returns the BCP-47 language tag, when supplied.
     *
     * @return the BCP-47 language tag, when supplied
     */
    public Optional<String> getLanguage() {
        return Optional.ofNullable(language);
    }

    /**
     * Returns the pixel height for image or video Content, when supplied.
     *
     * @return the pixel height for image or video Content, when supplied
     */
    public Optional<Long> getHeight() {
        return Optional.ofNullable(height);
    }

    /**
     * Returns the pixel width for image or video Content, when supplied.
     *
     * @return the pixel width for image or video Content, when supplied
     */
    public Optional<Long> getWidth() {
        return Optional.ofNullable(width);
    }

    /**
     * Returns the frame count for multi-frame Content, when supplied.
     *
     * @return the frame count for multi-frame Content, when supplied
     */
    public Optional<Long> getFrames() {
        return Optional.ofNullable(frames);
    }

    /**
     * Returns the playing duration for time-based Content, when supplied.
     *
     * @return the playing duration for time-based Content, when supplied
     */
    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }

    /**
     * Returns the page count for paged Content, when supplied.
     *
     * @return the page count for paged Content, when supplied
     */
    public Optional<Long> getPages() {
        return Optional.ofNullable(pages);
    }

    /**
     * Returns the provider-supplied digest text, when supplied.
     *
     * @return the provider-supplied digest text, when supplied
     */
    public Optional<String> getChecksum() {
        return Optional.ofNullable(checksum);
    }

    /**
     * Returns the digest algorithm; present exactly when a checksum is present.
     *
     * @return the digest algorithm; present exactly when a checksum is present
     */
    public Optional<String> getChecksumAlgorithm() {
        return Optional.ofNullable(checksumAlgorithm);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ContentDetail that)) {
            return false;
        }
        return Objects.equals(size, that.size) && Objects.equals(language, that.language)
                && Objects.equals(height, that.height) && Objects.equals(width, that.width)
                && Objects.equals(frames, that.frames) && Objects.equals(duration, that.duration)
                && Objects.equals(pages, that.pages) && Objects.equals(checksum, that.checksum)
                && Objects.equals(checksumAlgorithm, that.checksumAlgorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, language, height, width, frames, duration, pages, checksum, checksumAlgorithm);
    }

    /** Reports only presence, because payload-derived values can be identifying. */
    @Override
    public String toString() {
        return "ContentDetail{sizePresent=" + (size != null) + ", checksumPresent=" + (checksum != null) + "}";
    }
}
