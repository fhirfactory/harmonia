/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Objects;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;

/**
 * Safe validation and lifecycle exception for Calliope InformationUnit values.
 *
 * <p>The exception retains only a stable failure category and a structural
 * field label such as {@code origin.sourceSystem}. It deliberately omits
 * supplied values, URIs, descriptions, certifications and Content so that
 * callers can log or return it without disclosing protected information
 * (Invariant 7). Every rejection happens before any state is changed, so the
 * object or builder on which the call was made remains valid and usable.</p>
 */
public final class InformationModelException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private final InformationFailureCategory category;
    private final String field;

    private InformationModelException(InformationFailureCategory category, String field) {
        super("Information model " + category.name() + " at field " + field);
        this.category = category;
        this.field = field;
    }

    /**
     * Creates a model exception using a safe, fixed structural field label.
     *
     * @param category category of the rejected operation
     * @param field field path containing only letters, digits, '.', '_', '-', '[' and ']'
     * @return an exception that contains no rejected value
     * @throws IllegalArgumentException when the label itself could carry caller data
     */
    public static InformationModelException of(InformationFailureCategory category, String field) {
        Objects.requireNonNull(category, "category");
        if (field == null || field.isBlank() || !isSafeField(field)) {
            throw new IllegalArgumentException("Information model field label is invalid");
        }
        return new InformationModelException(category, field);
    }

    /**
     * Returns the stable failure category.
     *
     * @return the category describing why the operation was rejected
     */
    public InformationFailureCategory getCategory() {
        return category;
    }

    /**
     * Returns the structural field label, never a rejected value.
     *
     * @return the dotted field path of the rejected input
     */
    public String getField() {
        return field;
    }

    /** Restricts diagnostics to labels that cannot contain caller data. */
    private static boolean isSafeField(String field) {
        for (int index = 0; index < field.length(); index++) {
            char character = field.charAt(index);
            boolean asciiLetterOrDigit = (character >= 'a' && character <= 'z')
                    || (character >= 'A' && character <= 'Z') || (character >= '0' && character <= '9');
            if (!(asciiLetterOrDigit || character == '.' || character == '_'
                    || character == '[' || character == ']' || character == '-')) {
                return false;
            }
        }
        return true;
    }
}
