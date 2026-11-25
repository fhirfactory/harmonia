/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.validation;

import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * Payload-free rule violation raised by the Naming implementation packages.
 *
 * <p>Validation, canonical parsing, matching description and XML record code
 * live in function packages that cannot see the hidden factory of the public
 * {@code NamingValidationException}. They raise this violation instead; the
 * Naming model boundary translates it, field for field, into the supported
 * public exception before it reaches a caller. A violation therefore never
 * escapes a supported operation.</p>
 *
 * <p>The violation records only the stable category, operation, field kind
 * and an optional entry position. It retains no cause, raw input, pattern,
 * version or parser message, and it suppresses stack-trace capture because it
 * is a routine, expected rejection rather than a programming error.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public final class NamingRuleViolation extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /** Safe category for the rejected input or operation. */
    private final NamingFailureCategory category;
    /** Supported operation that was being performed. */
    private final NamingOperation operation;
    /** Structural field kind, without its value. */
    private final NamingFieldKind fieldKind;
    /** Zero-based entry position when known; range-checked by the public exception. */
    private final Integer entryIndex;

    /**
     * Records one safe rule violation.
     *
     * @param category stable failure category
     * @param operation supported operation being performed
     * @param fieldKind structural field kind, or {@code NONE}
     * @param entryIndex zero-based entry position when known, otherwise {@code null}
     */
    public NamingRuleViolation(
            NamingFailureCategory category,
            NamingOperation operation,
            NamingFieldKind fieldKind,
            Integer entryIndex) {
        super("Naming rule violated", null, false, false);
        this.category = category;
        this.operation = operation;
        this.fieldKind = fieldKind;
        this.entryIndex = entryIndex;
    }

    /**
     * Returns the stable failure category.
     *
     * @return the payload-free category
     */
    public NamingFailureCategory getCategory() {
        return category;
    }

    /**
     * Returns the operation during which the rule was violated.
     *
     * @return the supported operation vocabulary value
     */
    public NamingOperation getOperation() {
        return operation;
    }

    /**
     * Returns the structural field kind without its contents.
     *
     * @return the field kind
     */
    public NamingFieldKind getFieldKind() {
        return fieldKind;
    }

    /**
     * Returns the entry position recorded at failure time.
     *
     * @return the zero-based position, or {@code null} when unknown
     */
    public Integer getEntryIndex() {
        return entryIndex;
    }
}
