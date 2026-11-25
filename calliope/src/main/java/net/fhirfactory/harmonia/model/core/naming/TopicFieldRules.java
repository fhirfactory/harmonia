/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;


/**
 * Shared Unicode and completed-field validation for the Naming boundary.
 *
 * <p>These checks are intentionally centralized so builders, canonical
 * parsers, and interchange adapters enforce identical field semantics while
 * exposing only safe failure categories.</p>
 */
final class TopicFieldRules {

    /** Prevents construction of this package-scoped validation utility. */
    private TopicFieldRules() {
        throw new AssertionError("No instances");
    }

    /** Counts well-formed Unicode code points and rejects malformed surrogate sequences. */
    static int codePointCount(
            String value,
            NamingOperation operation,
            NamingFieldKind field,
            Integer index,
            NamingFailureCategory nullCategory) {
        if (value == null) {
            throw failure(nullCategory, operation, field, index);
        }
        int count = 0;
        for (int offset = 0; offset < value.length();) {
            char current = value.charAt(offset);
            if (Character.isHighSurrogate(current)) {
                if (offset + 1 >= value.length() || !Character.isLowSurrogate(value.charAt(offset + 1))) {
                    throw failure(NamingFailureCategory.INVALID_FIELD, operation, field, index);
                }
                offset += 2;
            } else if (Character.isLowSurrogate(current)) {
                throw failure(NamingFailureCategory.INVALID_FIELD, operation, field, index);
            } else {
                offset++;
            }
            count++;
        }
        return count;
    }

    /** Validates a supplied printable-ASCII qualifier or permits assembly-time generation. */
    static void validateQualifierInput(
            String qualifier,
            boolean allowGenerated,
            NamingOperation operation,
            Integer index) {
        if (qualifier == null || qualifier.isEmpty()) {
            if (allowGenerated) {
                return;
            }
            throw failure(qualifier == null ? NamingFailureCategory.MISSING_INPUT
                    : NamingFailureCategory.INVALID_FIELD, operation, NamingFieldKind.QUALIFIER, index);
        }
        validateAsciiField(qualifier, NamingFieldKind.QUALIFIER,
                NamingLimits.MAX_QUALIFIER_CODE_POINTS, operation, index);
    }

    /** Enforces the accepted Unicode categories, boundary-space rule, and value length. */
    static void validateValue(String value, NamingOperation operation, Integer index) {
        int count = codePointCount(value, operation, NamingFieldKind.VALUE, index,
                NamingFailureCategory.MISSING_INPUT);
        if (count == 0) {
            throw failure(NamingFailureCategory.INVALID_FIELD, operation, NamingFieldKind.VALUE, index);
        }
        checkLimit(count, NamingLimits.MAX_VALUE_CODE_POINTS, operation, NamingFieldKind.VALUE, index);
        if (Character.isWhitespace(value.codePointAt(0))
                || Character.isWhitespace(value.codePointBefore(value.length()))) {
            throw failure(NamingFailureCategory.INVALID_FIELD, operation, NamingFieldKind.VALUE, index);
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
                throw failure(NamingFailureCategory.INVALID_FIELD, operation, NamingFieldKind.VALUE, index);
            }
        }
    }

    /** Normalizes API null/empty version input to absence and validates present values. */
    static String normalizeVersion(String version, NamingOperation operation, Integer index) {
        if (version == null || version.isEmpty()) {
            return null;
        }
        validateAsciiField(version, NamingFieldKind.VERSION,
                NamingLimits.MAX_VERSION_CODE_POINTS, operation, index);
        return version;
    }


    /** Validates a fully assembled TopicEntry without generating or normalizing fields. */
    static void validateTopicCompletedFields(
            String qualifier,
            String value,
            String version,
            NamingOperation operation,
            Integer index) {
        validateQualifierInput(qualifier, false, operation, index);
        validateValue(value, operation, index);
        if (version != null) {
            validateAsciiField(version, NamingFieldKind.VERSION,
                    NamingLimits.MAX_VERSION_CODE_POINTS, operation, index);
        }
    }

    /** Validates exact-text version data while attributing errors to a mask constraint. */
    static void validateConstraintVersion(
            String version,
            NamingOperation operation,
            Integer index) {
        validateAsciiField(version, NamingFieldKind.VERSION_CONSTRAINT,
                NamingLimits.MAX_VERSION_CODE_POINTS, operation, index);
    }

    /** Raises a safe category when a measured quantity exceeds its accepted bound. */
    static void checkLimit(
            int count,
            int maximum,
            NamingOperation operation,
            NamingFieldKind field,
            Integer index) {
        if (count > maximum) {
            throw failure(NamingFailureCategory.LIMIT_EXCEEDED, operation, field, index);
        }
    }

    /** Constructs a validation exception containing only stable structural metadata. */
    static NamingValidationException failure(
            NamingFailureCategory category,
            NamingOperation operation,
            NamingFieldKind field,
            Integer index) {
        return new NamingValidationException(category, operation, field, index);
    }

    /** Validates nonempty printable ASCII and rejects leading or trailing spaces. */
    static void validateAsciiField(
            String value,
            NamingFieldKind field,
            int maximum,
            NamingOperation operation,
            Integer index) {
        int count = codePointCount(value, operation, field, index,
                NamingFailureCategory.MISSING_INPUT);
        if (count == 0) {
            throw failure(NamingFailureCategory.INVALID_FIELD, operation, field, index);
        }
        checkLimit(count, maximum, operation, field, index);
        if (value.charAt(0) == ' ' || value.charAt(value.length() - 1) == ' ') {
            throw failure(NamingFailureCategory.INVALID_FIELD, operation, field, index);
        }
        for (int offset = 0; offset < value.length(); offset++) {
            char current = value.charAt(offset);
            if (current < 0x20 || current > 0x7e) {
                throw failure(NamingFailureCategory.INVALID_FIELD, operation, field, index);
            }
        }
    }
}