/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.valuesets;

/**
 * Categorises safe, model-local InformationUnit validation and lifecycle failures.
 *
 * <p>Categories let callers react programmatically (for example, retrying with
 * corrected input versus reporting an attempted lifetime violation) without
 * parsing messages or seeing the rejected value.</p>
 */
public enum InformationFailureCategory {
    /** A required value was not supplied. */
    MISSING_INPUT,
    /** A value is malformed or outside the model's structural constraints. */
    INVALID_INPUT,
    /** Individually valid values form an inconsistent model state. */
    INCONSISTENT_INPUT,
    /** An established value was changed, removed or weakened. */
    IMMUTABILITY_VIOLATION
}
