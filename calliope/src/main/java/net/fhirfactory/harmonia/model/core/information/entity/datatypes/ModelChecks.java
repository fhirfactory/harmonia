/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Locale;
import java.util.Objects;
import java.util.function.BiPredicate;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.MediaTopLevelType;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * Shared, value-free argument checks for the InformationUnit model.
 *
 * <p>Centralising these checks keeps every rejection consistent: the same
 * category for the same fault, a structural field label, and no echo of the
 * rejected value. The class is public only so the root and factory packages
 * can reuse it; it carries no state and is not part of the model contract.</p>
 */
public final class ModelChecks {
    private ModelChecks() {
    }

    /**
     * Requires a non-null value.
     *
     * @param value value to check
     * @param field structural label used in the failure
     * @param <T> value type
     * @return the same value
     * @throws InformationModelException with {@code MISSING_INPUT} when null
     */
    public static <T> T required(T value, String field) {
        if (value == null) {
            throw InformationModelException.of(InformationFailureCategory.MISSING_INPUT, field);
        }
        return value;
    }

    /**
     * Requires text that is present and not blank.
     *
     * @param value text to check
     * @param field structural label used in the failure
     * @return the same text, unmodified
     * @throws InformationModelException {@code MISSING_INPUT} for null, {@code INVALID_INPUT} for blank
     */
    public static String requiredText(String value, String field) {
        required(value, field);
        if (value.isBlank()) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, field);
        }
        return value;
    }

    /**
     * Validates and normalises an IANA media type identifier.
     *
     * <p>The accepted form is one registered top-level token, a slash, and a
     * syntactically valid restricted subtype token. Parameters and whitespace
     * are rejected because this field identifies the media type only, not a
     * media type plus transport parameters. RFC 6838 limits each name to 127
     * ASCII characters. This structural check does not claim that a subtype
     * is currently registered by IANA.</p>
     *
     * @param value media type identifier, for example {@code application/fhir+json}
     * @param field structural label used in failures
     * @return the identifier normalised to lowercase using {@link Locale#ROOT}
     * @throws InformationModelException {@code MISSING_INPUT} for null;
     *         {@code INVALID_INPUT} for malformed, unsupported-top-level or parameterised values
     */
    public static String mediaType(String value, String field) {
        required(value, field);
        if (value.isBlank()) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, field);
        }
        int separator = value.indexOf('/');
        if (separator <= 0 || separator != value.lastIndexOf('/') || separator == value.length() - 1
                || separator > 127 || value.length() - separator - 1 > 127) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, field);
        }
        String topLevel = value.substring(0, separator);
        String subtype = value.substring(separator + 1);
        String normalizedTopLevel = topLevel.toLowerCase(Locale.ROOT);
        boolean registeredTopLevel = false;
        for (MediaTopLevelType candidate : MediaTopLevelType.values()) {
            if (candidate.getToken().equals(normalizedTopLevel)) {
                registeredTopLevel = true;
                break;
            }
        }
        if (!registeredTopLevel || !isRestrictedName(topLevel) || !isRestrictedName(subtype)) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, field);
        }
        return value.toLowerCase(Locale.ROOT);
    }

    /** Checks the restricted ASCII name syntax from RFC 6838. */
    private static boolean isRestrictedName(String name) {
        if (name.isEmpty() || name.length() > 127 || !isAsciiAlphaNumeric(name.charAt(0))) {
            return false;
        }
        for (int index = 1; index < name.length(); index++) {
            char character = name.charAt(index);
            if (!isAsciiAlphaNumeric(character) && character != '!' && character != '#'
                    && character != '$' && character != '&' && character != '-'
                    && character != '^' && character != '_' && character != '.' && character != '+') {
                return false;
            }
        }
        return true;
    }

    private static boolean isAsciiAlphaNumeric(char character) {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')
                || (character >= '0' && character <= '9');
    }

    /**
     * Compares two optional Topics including entry versions.
     *
     * <p>{@link Topic#equals(Object)} deliberately ignores entry versions. Lifetime
     * rules ("an established value may not change") must not, because a changed
     * version is a changed fact; they therefore compare the version-capable
     * canonical form.</p>
     *
     * @param left first Topic, may be null
     * @param right second Topic, may be null
     * @return true when both are absent or have the same versioned canonical form
     */
    public static boolean sameVersionedTopic(Topic left, Topic right) {
        if (left == null || right == null) {
            return left == right;
        }
        return left.getCanonicalNameWithVersion().equals(right.getCanonicalNameWithVersion());
    }

    /**
     * Applies the establish-once rule for a single optional value.
     *
     * @param current currently established value, or null when still unknown
     * @param proposed proposed value, or null to leave unknown
     * @param same equality used to recognise an idempotent re-statement
     * @param field structural label used in the failure
     * @param <T> value type
     * @return the value to hold after the change
     * @throws InformationModelException {@code IMMUTABILITY_VIOLATION} when an established value
     *         would be removed or replaced
     */
    public static <T> T establishOnce(T current, T proposed, BiPredicate<T, T> same, String field) {
        if (current == null) {
            return proposed;
        }
        if (proposed == null || !same.test(current, proposed)) {
            throw InformationModelException.of(InformationFailureCategory.IMMUTABILITY_VIOLATION, field);
        }
        return current;
    }

    /**
     * Requires two values to be equal under ordinary value equality.
     *
     * @param expected the value that must be preserved
     * @param actual the proposed value
     * @param field structural label used in the failure
     * @throws InformationModelException {@code IMMUTABILITY_VIOLATION} when they differ
     */
    public static void unchanged(Object expected, Object actual, String field) {
        if (!Objects.equals(expected, actual)) {
            throw InformationModelException.of(InformationFailureCategory.IMMUTABILITY_VIOLATION, field);
        }
    }
}
