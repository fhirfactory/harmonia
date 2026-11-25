/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.canonical;

import java.util.ArrayList;
import java.util.List;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;
import net.fhirfactory.harmonia.model.core.naming.validation.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * Scans and emits the shared outer escaping used by Topic and TopicMask text.
 *
 * <p>This scanner recognizes only the four accepted structural escapes. It
 * does not interpret wildcard or version-constraint semantics, which are
 * validated by their respective matching descriptions. Only {@link #encode}
 * is used outside this package, by the models when rendering canonical text;
 * scanning and decoding remain package-private to the canonical parsers.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public final class CanonicalPathSyntax {

    /** Prevents construction of the stateless syntax scanner. */
    private CanonicalPathSyntax() {
        throw new AssertionError("No instances");
    }

    /** Checks bounded UTF-8 input and splits only on unescaped entry periods. */
    static List<String> splitEntries(
            String text,
            int maximumBytes,
            NamingOperation operation) {
        if (text == null) {
            throw new NamingRuleViolation(NamingFailureCategory.MISSING_INPUT,
                    operation, NamingFieldKind.CANONICAL_TEXT, null);
        }
        if (utf8Length(text, operation) > maximumBytes) {
            throw new NamingRuleViolation(NamingFailureCategory.LIMIT_EXCEEDED,
                    operation, NamingFieldKind.CANONICAL_TEXT, null);
        }
        if (text.isEmpty()) {
            throw malformed(operation, null);
        }
        List<String> entries = new ArrayList<>();
        int start = 0;
        for (int offset = 0; offset < text.length(); offset++) {
            char current = text.charAt(offset);
            if (current == '\\') {
                if (offset + 1 < text.length() && isEscapable(text.charAt(offset + 1))) {
                    offset++;
                }
            } else if (current == '.') {
                if (offset == start) {
                    throw malformed(operation, entries.size());
                }
                if (entries.size() >= NamingLimits.MAX_DEPTH) {
                    throw new NamingRuleViolation(NamingFailureCategory.LIMIT_EXCEEDED,
                            operation, NamingFieldKind.DEPTH, null);
                }
                entries.add(text.substring(start, offset));
                start = offset + 1;
            }
        }
        if (start == text.length()) {
            throw malformed(operation, entries.size());
        }
        if (entries.size() >= NamingLimits.MAX_DEPTH) {
            throw new NamingRuleViolation(NamingFailureCategory.LIMIT_EXCEEDED,
                    operation, NamingFieldKind.DEPTH, null);
        }
        entries.add(text.substring(start));
        return List.copyOf(entries);
    }

    /** Finds the single outer equals and optional preceding version separator. */
    static RawEntry splitFields(
            String text,
            boolean allowVersion,
            boolean allowEmptyFields,
            NamingOperation operation,
            int index) {
        int equals = -1;
        int colon = -1;
        for (int offset = 0; offset < text.length(); offset++) {
            char current = text.charAt(offset);
            if (current == '\\') {
                if (offset + 1 < text.length() && isEscapable(text.charAt(offset + 1))) {
                    offset++;
                }
            } else if (current == '=') {
                if (equals >= 0) {
                    throw malformed(operation, index);
                }
                equals = offset;
            } else if (current == ':') {
                if (equals >= 0 || colon >= 0 || !allowVersion) {
                    throw malformed(operation, index);
                }
                colon = offset;
            }
        }
        if (equals < 0 || (!allowEmptyFields && (equals == 0 || equals == text.length() - 1))
                || (!allowEmptyFields && colon == 0)) {
            throw malformed(operation, index);
        }
        if (colon >= 0 && colon > equals) {
            throw malformed(operation, index);
        }
        if (colon >= 0) {
            return new RawEntry(text.substring(0, colon), text.substring(colon + 1, equals),
                    text.substring(equals + 1), true);
        }
        return new RawEntry(text.substring(0, equals), null, text.substring(equals + 1), false);
    }

    /** Removes exactly one outer escape layer from a single field. */
    static String decode(String raw, NamingOperation operation, NamingFieldKind field, int index) {
        StringBuilder decoded = new StringBuilder(raw.length());
        for (int offset = 0; offset < raw.length(); offset++) {
            char current = raw.charAt(offset);
            if (current != '\\') {
                decoded.append(current);
                continue;
            }
            if (offset + 1 >= raw.length() || !isEscapable(raw.charAt(offset + 1))) {
                throw new NamingRuleViolation(NamingFailureCategory.MALFORMED_TEXT,
                        operation, field, index);
            }
            decoded.append(raw.charAt(++offset));
        }
        TopicFieldRules.codePointCount(decoded.toString(), operation, field, index,
                NamingFailureCategory.MALFORMED_TEXT);
        return decoded.toString();
    }

    /**
     * Adds one outer escape layer without interpreting the field's semantics.
     *
     * @param raw unescaped field text
     * @return the text with each structural delimiter and backslash escaped
     */
    public static String encode(String raw) {
        StringBuilder encoded = new StringBuilder(raw.length());
        for (int offset = 0; offset < raw.length(); offset++) {
            char current = raw.charAt(offset);
            if (isEscapable(current)) {
                encoded.append('\\');
            }
            encoded.append(current);
        }
        return encoded.toString();
    }

    /** Returns whether one code unit is among the four structural escape targets. */
    private static boolean isEscapable(char value) {
        return value == '.' || value == '=' || value == ':' || value == '\\';
    }

    /** Measures UTF-8 byte length while strictly validating every surrogate pair. */
    private static long utf8Length(String text, NamingOperation operation) {
        long bytes = 0;
        for (int offset = 0; offset < text.length();) {
            char current = text.charAt(offset);
            int addition;
            if (Character.isHighSurrogate(current)) {
                if (offset + 1 >= text.length() || !Character.isLowSurrogate(text.charAt(offset + 1))) {
                    throw new NamingRuleViolation(NamingFailureCategory.MALFORMED_TEXT,
                            operation, NamingFieldKind.CANONICAL_TEXT, null);
                }
                offset += 2;
                addition = 4;
            } else if (Character.isLowSurrogate(current)) {
                throw new NamingRuleViolation(NamingFailureCategory.MALFORMED_TEXT,
                        operation, NamingFieldKind.CANONICAL_TEXT, null);
            } else {
                offset++;
                addition = current <= 0x7f ? 1 : current <= 0x7ff ? 2 : 3;
            }
            bytes += addition;
        }
        return bytes;
    }

    /** Builds a payload-free canonical syntax failure for the current entry. */
    private static NamingRuleViolation malformed(NamingOperation operation, Integer index) {
        return new NamingRuleViolation(NamingFailureCategory.MALFORMED_TEXT,
                operation, NamingFieldKind.CANONICAL_TEXT, index);
    }

    /** Raw outer-field slices; {@code hasMiddle} preserves present-empty versions. */
    record RawEntry(String left, String middle, String right, boolean hasMiddle) {
    }
}