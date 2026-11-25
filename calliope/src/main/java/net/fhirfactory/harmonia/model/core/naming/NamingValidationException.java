/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.OptionalInt;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * A safe, categorized rejection raised while validating Naming input.
 *
 * <p>The exception exposes only stable categories and bounded structural
 * context. It intentionally retains neither a cause nor raw input, which could
 * contain protected data or attacker-controlled expression text.</p>
 *
 * <p>The exception is serializable. Its state is the stable metadata above;
 * the entry index is stored as a primitive and range-checked again whenever it
 * is read, so a deserialized instance cannot report an invalid position.</p>
 */
public final class NamingValidationException extends IllegalArgumentException {
    @java.io.Serial
    private static final long serialVersionUID = 1L;
    /** Stored marker for an unknown or invalid entry position. */
    private static final int NO_INDEX = -1;
    /** Safe category for the rejected input or operation. */
    private final NamingFailureCategory category;
    /** Bounded public operation that performed the failed validation. */
    private final NamingOperation operation;
    /** Structural field kind without its corresponding value. */
    private final NamingFieldKind fieldKind;
    /** Valid zero-based entry position when it was known at failure time, otherwise {@link #NO_INDEX}. */
    private final int entryIndex;

    /** Creates a safe validation failure; reachable only from the Naming model package. */
    static NamingValidationException of(
            NamingFailureCategory category,
            NamingOperation operation,
            NamingFieldKind fieldKind,
            Integer entryIndex) {
        return new NamingValidationException(category, operation, fieldKind, entryIndex);
    }

    /**
     * Creates a categorized validation exception for internal Naming use.
     *
     * @param category stable failure category
     * @param operation operation being performed
     * @param fieldKind field category, or {@code NONE}
     * @param entryIndex zero-based entry index when available
     */
    NamingValidationException(
            NamingFailureCategory category,
            NamingOperation operation,
            NamingFieldKind fieldKind,
            Integer entryIndex) {
        super(safeMessage(category, operation, fieldKind, safeIndex(entryIndex)));
        this.category = category;
        this.operation = operation;
        this.fieldKind = fieldKind;
        Integer safeIndex = safeIndex(entryIndex);
        this.entryIndex = safeIndex == null ? NO_INDEX : safeIndex;
    }

    /**
     * Returns the stable category of the rejected operation.
     *
     * @return the payload-free validation category
     */
    public NamingFailureCategory getCategory() {
        return category;
    }

    /**
     * Returns the bounded operation that rejected the input.
     *
     * @return the public operation vocabulary value
     */
    public NamingOperation getOperation() {
        return operation;
    }

    /**
     * Returns the field category without exposing its contents.
     *
     * @return the field kind, or {@link NamingFieldKind#NONE} when no field applies
     */
    public NamingFieldKind getFieldKind() {
        return fieldKind;
    }

    /**
     * Returns the valid zero-based entry index, or an empty result when unavailable.
     *
     * @return an empty OptionalInt for an unknown or invalid position
     */
    public OptionalInt getEntryIndex() {
        Integer safeIndex = safeIndex(entryIndex);
        return safeIndex == null ? OptionalInt.empty() : OptionalInt.of(safeIndex);
    }

    /** Omits indexes outside the completed model's valid zero-based depth range. */
    private static Integer safeIndex(Integer index) {
        return index != null && index >= 0 && index < NamingLimits.MAX_DEPTH ? index : null;
    }

    /** Formats a category-only message and deliberately omits input and engine diagnostics. */
    private static String safeMessage(
            NamingFailureCategory category,
            NamingOperation operation,
            NamingFieldKind fieldKind,
            Integer entryIndex) {
        StringBuilder message = new StringBuilder("Naming input rejected");
        if (category != null) {
            message.append(" [").append(category).append(']');
        }
        if (operation != null) {
            message.append(" during ").append(operation);
        }
        if (fieldKind != null && fieldKind != NamingFieldKind.NONE) {
            message.append(" for ").append(fieldKind);
        }
        if (entryIndex != null) {
            message.append(" at entry ").append(entryIndex);
        }
        return message.toString();
    }
}