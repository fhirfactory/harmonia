/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.matching;

import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;

/**
 * Safe metadata for one synchronous TopicMask evaluation exit.
 *
 * <p>The record deliberately contains no field for candidate text, Topic
 * paths, patterns, versions, exception messages, or causes. It is constructed
 * only when an invocation-local sink is enabled.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 *
 * @param severity handling intent for the exit
 * @param category bounded exit category
 * @param depth candidate depth, zero for a null candidate
 * @param entryIndex last evaluated position, or {@code null} when no position applies
 * @param fieldKind field kind associated with the exit
 */
public record NamingDiagnostic(
        DiagnosticSeverity severity,
        MatchExitCategory category,
        int depth,
        Integer entryIndex,
        NamingFieldKind fieldKind) {

    /** Validates that emitted metadata is bounded and internally coherent. */
    public NamingDiagnostic {
        if (severity == null || category == null || fieldKind == null) {
            throw new IllegalArgumentException("Diagnostic metadata is required");
        }
        if (depth < 0 || depth > NamingLimits.MAX_DEPTH
                || (entryIndex != null && (entryIndex < 0 || entryIndex >= depth))) {
            throw new IllegalArgumentException("Diagnostic metadata is out of range");
        }
    }
}
