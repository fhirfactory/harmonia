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

package net.fhirfactory.harmonia.model.core.naming.support;

import java.util.Objects;

/**
 * Validates decoded Topic fields without trimming, normalization, or delimiter assumptions.
 * <p>
 * Qualifiers and versions use printable ASCII. Values use Unicode letters,
 * numbers, punctuation, symbols, and combining marks, with U+0020 as the only
 * accepted space. All validation also rejects malformed UTF-16.
 */
public final class TopicFieldRules {

    private TopicFieldRules() {
    }

    /**
     * Validates one completed qualifier.
     *
     * @param qualifier qualifier text
     * @throws IllegalArgumentException when the qualifier violates its character or length rules
     */
    public static void requireQualifier(String qualifier) {
        requirePrintableAscii(qualifier, TopicLimits.MAX_QUALIFIER_CODE_POINTS, "qualifier");
    }

    /**
     * Validates a qualifier input before TopicBuilder applies a positional default.
     * Null and empty inputs are intentionally accepted here and become `N<position>`
     * when the final path is assembled.
     *
     * @param qualifier nullable qualifier input
     * @throws IllegalArgumentException when a nonempty supplied qualifier is invalid
     */
    public static void requireQualifierInput(String qualifier) {
        if (qualifier != null && !qualifier.isEmpty()) {
            requireQualifier(qualifier);
        }
    }

    /**
     * Validates a Topic value by Unicode code point.
     *
     * @param value required value
     * @throws IllegalArgumentException when the value is null, empty, malformed, or disallowed
     */
    public static void requireValue(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value must not be null or empty");
        }
        requireWellFormedUnicode(value, "value");
        TopicLimits.requireLength(value, TopicLimits.MAX_VALUE_CODE_POINTS, "value");
        if (value.charAt(0) == ' ' || value.charAt(value.length() - 1) == ' ') {
            throw new IllegalArgumentException("value must not have leading or trailing spaces");
        }
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            offset += Character.charCount(codePoint);
            if (codePoint == ' ') {
                continue;
            }
            int type = Character.getType(codePoint);
            if (type != Character.UPPERCASE_LETTER
                    && type != Character.LOWERCASE_LETTER
                    && type != Character.TITLECASE_LETTER
                    && type != Character.MODIFIER_LETTER
                    && type != Character.OTHER_LETTER
                    && type != Character.DECIMAL_DIGIT_NUMBER
                    && type != Character.LETTER_NUMBER
                    && type != Character.OTHER_NUMBER
                    && type != Character.CONNECTOR_PUNCTUATION
                    && type != Character.DASH_PUNCTUATION
                    && type != Character.START_PUNCTUATION
                    && type != Character.END_PUNCTUATION
                    && type != Character.INITIAL_QUOTE_PUNCTUATION
                    && type != Character.FINAL_QUOTE_PUNCTUATION
                    && type != Character.OTHER_PUNCTUATION
                    && type != Character.MATH_SYMBOL
                    && type != Character.CURRENCY_SYMBOL
                    && type != Character.MODIFIER_SYMBOL
                    && type != Character.OTHER_SYMBOL
                    && type != Character.NON_SPACING_MARK
                    && type != Character.COMBINING_SPACING_MARK
                    && type != Character.ENCLOSING_MARK) {
                throw new IllegalArgumentException("value contains a disallowed Unicode code point");
            }
        }
    }

    /**
     * Converts a nullable/empty API version to absence and validates present values.
     *
     * @param version optional version text
     * @return the supplied nonempty version, or null for absence
     */
    public static String normalizedVersion(String version) {
        if (version == null || version.isEmpty()) {
            return null;
        }
        requirePrintableAscii(version, TopicLimits.MAX_VERSION_CODE_POINTS, "version");
        return version;
    }

    /**
     * Validates a present version as a completed field.
     *
     * @param version nonempty version text
     */
    public static void requireVersion(String version) {
        requirePrintableAscii(version, TopicLimits.MAX_VERSION_CODE_POINTS, "version");
    }

    /**
     * Rejects malformed UTF-16, including unpaired high and low surrogates.
     *
     * @param text text to inspect
     * @param field safe field label for diagnostics
     */
    public static void requireWellFormedUnicode(String text, String field) {
        Objects.requireNonNull(field, "field");
        if (text == null) {
            throw new IllegalArgumentException(field + " must not be null");
        }
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            if (Character.isHighSurrogate(current)) {
                if (index + 1 >= text.length() || !Character.isLowSurrogate(text.charAt(index + 1))) {
                    throw new IllegalArgumentException(field + " contains malformed Unicode");
                }
                index++;
            } else if (Character.isLowSurrogate(current)) {
                throw new IllegalArgumentException(field + " contains malformed Unicode");
            }
        }
    }

    private static void requirePrintableAscii(String text, int maximum, String field) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException(field + " must not be null or empty");
        }
        requireWellFormedUnicode(text, field);
        TopicLimits.requireLength(text, maximum, field);
        if (text.charAt(0) == ' ' || text.charAt(text.length() - 1) == ' ') {
            throw new IllegalArgumentException(field + " must not have leading or trailing spaces");
        }
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            if (current < 0x20 || current > 0x7e) {
                throw new IllegalArgumentException(field + " must contain printable ASCII only");
            }
        }
    }
}