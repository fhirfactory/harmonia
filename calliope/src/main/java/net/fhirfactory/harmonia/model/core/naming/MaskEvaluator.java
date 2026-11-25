/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

/**
 * Evaluates immutable TopicMask criteria in positional, fail-closed order.
 *
 * <p>Ordinary masks check qualifier, value, and then an optional version at
 * each position, stopping at the first mismatch. A completed path is bounded
 * to sixteen entries, so at most 48 field checks occur. Diagnostic callbacks
 * happen once, after the Boolean result is established.</p>
 */
final class MaskEvaluator {
    private MaskEvaluator() {
        throw new AssertionError("No instances");
    }

    /** Evaluates one candidate with an optional invocation-local sink and test probe. */
    static boolean evaluate(
            TopicMask mask,
            Topic candidate,
            MatchDiagnosticSink sink,
            MatchProbe suppliedProbe) {
        int depth = candidate == null ? 0 : candidate.getDepth();
        boolean matches;
        MatchExitCategory category;
        NamingFieldKind field;
        Integer entryIndex = null;
        DiagnosticSeverity severity = DiagnosticSeverity.TRACE;
        if (candidate == null) {
            matches = false;
            category = MatchExitCategory.NULL_CANDIDATE;
            field = NamingFieldKind.NONE;
        } else if (mask == null) {
            matches = false;
            category = MatchExitCategory.EVALUATION_ERROR;
            field = NamingFieldKind.NONE;
            severity = DiagnosticSeverity.ERROR;
        } else if (!mask.isOrdinary()) {
            matches = true;
            category = MatchExitCategory.UNIVERSAL_MATCH;
            field = NamingFieldKind.NONE;
        } else {
            return evaluateOrdinary(mask, candidate, sink,
                    suppliedProbe == null ? MatchProbe.DIRECT : suppliedProbe);
        }
        emit(sink, severity, category, depth, entryIndex, field);
        return matches;
    }

    /** Determines one ordinary-mask result, preserving ordered checks and first mismatch. */
    private static boolean evaluateOrdinary(
            TopicMask mask,
            Topic candidate,
            MatchDiagnosticSink sink,
            MatchProbe probe) {
        int depth = candidate.getDepth();
        int requiredDepth = mask.getEntries().size();
        boolean matches = true;
        MatchExitCategory category = MatchExitCategory.MATCH;
        NamingFieldKind field = NamingFieldKind.VALUE_PATTERN;
        Integer entryIndex = null;
        DiagnosticSeverity severity = DiagnosticSeverity.TRACE;
        if (depth < requiredDepth || (!mask.getContainmentFlag() && depth != requiredDepth)) {
            matches = false;
            category = MatchExitCategory.DEPTH_MISMATCH;
            field = NamingFieldKind.DEPTH;
        } else {
            for (int index = 0; index < requiredDepth; index++) {
                TopicEntry candidateEntry = candidate.getEntries().get(index);
                TopicMaskEntry criterion = mask.getEntries().get(index);
                NamingFieldKind evaluatingField = NamingFieldKind.QUALIFIER_PATTERN;
                try {
                    if (!probe.matchesQualifier(criterion, candidateEntry.getQualifier())) {
                        matches = false;
                        category = MatchExitCategory.QUALIFIER_MISMATCH;
                        field = NamingFieldKind.QUALIFIER_PATTERN;
                        entryIndex = index;
                        break;
                    }
                    evaluatingField = NamingFieldKind.VALUE_PATTERN;
                    if (!probe.matchesValue(criterion, candidateEntry.getValue())) {
                        matches = false;
                        category = MatchExitCategory.VALUE_MISMATCH;
                        field = NamingFieldKind.VALUE_PATTERN;
                        entryIndex = index;
                        break;
                    }
                    if (criterion.hasVersionConstraint()) {
                        evaluatingField = NamingFieldKind.VERSION_CONSTRAINT;
                        String version = candidateEntry.getVersion().orElse(null);
                        if (version == null) {
                            matches = false;
                            category = MatchExitCategory.REQUIRED_VERSION_ABSENT;
                            field = NamingFieldKind.VERSION_CONSTRAINT;
                            entryIndex = index;
                            break;
                        }
                        if (!probe.matchesVersion(criterion, version)) {
                            matches = false;
                            category = MatchExitCategory.VERSION_MISMATCH;
                            field = NamingFieldKind.VERSION_CONSTRAINT;
                            entryIndex = index;
                            break;
                        }
                    }
                } catch (RuntimeException ignored) {
                    matches = false;
                    category = MatchExitCategory.EVALUATION_ERROR;
                    field = evaluatingField;
                    entryIndex = index;
                    severity = DiagnosticSeverity.ERROR;
                    break;
                }
                entryIndex = index;
                field = criterion.hasVersionConstraint()
                        ? NamingFieldKind.VERSION_CONSTRAINT : NamingFieldKind.VALUE_PATTERN;
            }
        }
        emit(sink, severity, category, depth, entryIndex, field);
        return matches;
    }

    /** Constructs and dispatches safe metadata only when diagnostics are enabled. */
    private static void emit(
            MatchDiagnosticSink sink,
            DiagnosticSeverity severity,
            MatchExitCategory category,
            int depth,
            Integer index,
            NamingFieldKind field) {
        if (sink != null) {
            try {
                sink.accept(new NamingDiagnostic(severity, category, depth, index, field));
            } catch (RuntimeException ignored) {
                // A diagnostic handler cannot change or retry an established result.
            }
        }
    }
}