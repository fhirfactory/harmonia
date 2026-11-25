/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

/**
 * Safe metadata for one synchronous TopicMask evaluation exit.
 *
 * <p>The record deliberately contains no field for candidate text, Topic
 * paths, patterns, versions, exception messages, or causes. It remains
 * package-private and exists only when an invocation-local handler is enabled.</p>
 */
record NamingDiagnostic(
        DiagnosticSeverity severity,
        MatchExitCategory category,
        int depth,
        Integer entryIndex,
        NamingFieldKind fieldKind) {

    /** Validates that emitted metadata is bounded and internally coherent. */
    NamingDiagnostic {
        if (severity == null || category == null || fieldKind == null) {
            throw new IllegalArgumentException("Diagnostic metadata is required");
        }
        if (depth < 0 || depth > NamingLimits.MAX_DEPTH
                || (entryIndex != null && (entryIndex < 0 || entryIndex >= depth))) {
            throw new IllegalArgumentException("Diagnostic metadata is out of range");
        }
    }
}