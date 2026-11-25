/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

/**
 * Internal limits bounding Naming construction, parsing, matching, and interchange.
 *
 * <p>These fixed limits keep transient work proportional to the represented
 * path and avoid a configurable expression engine or unbounded parser state.</p>
 */
final class NamingLimits {
    static final int MAX_DEPTH = 16;
    static final int MAX_QUALIFIER_CODE_POINTS = 64;
    static final int MAX_VALUE_CODE_POINTS = 256;
    static final int MAX_VERSION_CODE_POINTS = 64;
    static final int MAX_PATTERN_CODE_POINTS = 512;
    static final int MAX_TOPIC_CANONICAL_UTF8_BYTES = 32 * 1024;
    static final int MAX_MASK_CANONICAL_UTF8_BYTES = 128 * 1024;
    static final int MAX_XML_RECORD_UTF8_BYTES = 256 * 1024;
    static final int MAX_NUMERIC_VERSION_OPERAND_CHARS = 64;
    static final int MAX_NUMERIC_VERSION_COMPONENTS = 32;
    static final int MAX_EXACT_CONSTRAINT_RAW_CHARS = 65;
    static final int MAX_SINGLE_CONSTRAINT_RAW_CHARS = 66;
    static final int MAX_RANGE_CONSTRAINT_RAW_CHARS = 133;
    static final int MAX_PATTERN_UTF8_BYTES = MAX_PATTERN_CODE_POINTS * 4;
    static final int MAX_VERSION_CONSTRAINT_UTF8_BYTES = MAX_RANGE_CONSTRAINT_RAW_CHARS;

    private NamingLimits() {
        throw new AssertionError("No instances");
    }
}