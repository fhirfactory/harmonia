/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.validation;

/**
 * Fixed resource limits bounding Naming construction, parsing, matching and interchange.
 *
 * <p>These limits keep transient work proportional to the represented path and
 * avoid a configurable expression engine or unbounded parser state. They are
 * the single source of every Naming bound, so builders, canonical parsers,
 * matching descriptions and XML records cannot drift apart.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API; the values are contractual, the constants'
 * location is not.</p>
 */
public final class NamingLimits {
    /** Maximum Topic and ordinary TopicMask depth. */
    public static final int MAX_DEPTH = 16;
    /** Maximum qualifier length in Unicode code points. */
    public static final int MAX_QUALIFIER_CODE_POINTS = 64;
    /** Maximum value length in Unicode code points. */
    public static final int MAX_VALUE_CODE_POINTS = 256;
    /** Maximum present Topic version length in code points. */
    public static final int MAX_VERSION_CODE_POINTS = 64;
    /** Maximum qualifier or value pattern length in code points. */
    public static final int MAX_PATTERN_CODE_POINTS = 512;
    /** Maximum Topic canonical representation size in UTF-8 bytes. */
    public static final int MAX_TOPIC_CANONICAL_UTF8_BYTES = 32 * 1024;
    /** Maximum ordinary mask canonical representation size in UTF-8 bytes. */
    public static final int MAX_MASK_CANONICAL_UTF8_BYTES = 128 * 1024;
    /** Maximum XML interchange record size in UTF-8 bytes. */
    public static final int MAX_XML_RECORD_UTF8_BYTES = 256 * 1024;
    /** Maximum numeric version operand length in ASCII characters. */
    public static final int MAX_NUMERIC_VERSION_OPERAND_CHARS = 64;
    /** Maximum number of numeric version components. */
    public static final int MAX_NUMERIC_VERSION_COMPONENTS = 32;
    /** Maximum raw escaped exact-version constraint length. */
    public static final int MAX_EXACT_CONSTRAINT_RAW_CHARS = 65;
    /** Maximum raw single-comparison constraint length. */
    public static final int MAX_SINGLE_CONSTRAINT_RAW_CHARS = 66;
    /** Maximum raw numeric range constraint length. */
    public static final int MAX_RANGE_CONSTRAINT_RAW_CHARS = 133;
    /** Maximum decoded UTF-8 bytes of one XML pattern field. */
    public static final int MAX_PATTERN_UTF8_BYTES = MAX_PATTERN_CODE_POINTS * 4;
    /** Maximum decoded UTF-8 bytes of one XML version-constraint field. */
    public static final int MAX_VERSION_CONSTRAINT_UTF8_BYTES = MAX_RANGE_CONSTRAINT_RAW_CHARS;

    /** Prevents construction of this constants holder. */
    private NamingLimits() {
        throw new AssertionError("No instances");
    }
}
