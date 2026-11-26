/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.valuesets;

import java.util.Locale;

/**
 * IANA registered top-level media type tokens accepted by model validation.
 *
 * <p>This set validates the top-level token in required IANA media type
 * identifiers. Media types belong to separate container/content fields; they
 * are never content concepts or schemas. A media type is also distinct from a
 * schema: {@code image/png} versus {@code image/jpeg} is a media type and not
 * a structure Topic (Draft 002.005 §1.6).</p>
 */
public enum MediaTopLevelType {
    /** Application-specific data. */
    APPLICATION,
    /** Audio data. */
    AUDIO,
    /** Reserved for examples in documentation. */
    EXAMPLE,
    /** Font data. */
    FONT,
    /** Haptic data. */
    HAPTIC,
    /** Image data. */
    IMAGE,
    /** An encapsulated message. */
    MESSAGE,
    /** A model, such as a three-dimensional model. */
    MODEL,
    /** A composite of several parts. */
    MULTIPART,
    /** Human-readable text. */
    TEXT,
    /** Video data. */
    VIDEO;

    /**
     * Returns the registered lowercase token, for example {@code image}.
     *
     * @return the IANA top-level type token
     */
    public String getToken() {
        return name().toLowerCase(Locale.ROOT);
    }
}
