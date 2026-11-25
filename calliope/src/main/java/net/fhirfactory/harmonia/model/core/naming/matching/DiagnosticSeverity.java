/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.matching;

/**
 * Safe handling intent for one optional match diagnostic.
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public enum DiagnosticSeverity {
    /** Routine exit, including every match and ordinary mismatch. */
    TRACE,
    /** Unexpected evaluation failure that was converted to a non-match. */
    ERROR
}