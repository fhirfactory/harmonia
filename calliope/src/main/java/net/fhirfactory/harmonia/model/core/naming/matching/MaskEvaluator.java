/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.matching;

import java.util.List;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;

/**
 * Evaluates compiled TopicMask criteria in positional, fail-closed order.
 *
 * <p>Ordinary evaluation first checks depth (exact by default, at least the
 * mask depth when containment is requested), then at each position the
 * qualifier, the value and, only when a constraint was supplied, the version,
 * stopping at the first mismatch. A completed path is bounded to sixteen
 * entries, so at most 48 field checks occur. An unexpected runtime failure in
 * a check yields {@code false}; it never propagates and never yields a match.</p>
 *
 * <p>Diagnostics are optional and invocation-local. When no sink is supplied
 * no record is constructed. When one is supplied, exactly one bounded record
 * is emitted after the Boolean result is established; a throwing sink cannot
 * change that result and is not retried. The evaluator holds no state, so
 * concurrent evaluations never share mutable data.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public final class MaskEvaluator {

    /** Prevents construction of this stateless evaluator. */
    private MaskEvaluator() {
        throw new AssertionError("No instances");
    }

    /**
     * Evaluates the explicit universal selector, which matches every non-null candidate.
     *
     * @param candidate candidate view, or {@code null}
     * @param sink optional invocation-local diagnostic sink, or {@code null}
     * @return true for every non-null candidate
     */
    public static boolean evaluateUniversal(MatchCandidate candidate, MatchDiagnosticSink sink) {
        if (candidate == null) {
            emit(sink, DiagnosticSeverity.TRACE, MatchExitCategory.NULL_CANDIDATE, 0, null,
                    NamingFieldKind.NONE);
            return false;
        }
        emit(sink, DiagnosticSeverity.TRACE, MatchExitCategory.UNIVERSAL_MATCH, candidate.depth(), null,
                NamingFieldKind.NONE);
        return true;
    }

    /**
     * Evaluates ordered ordinary criteria against one candidate.
     *
     * @param criteria nonempty compiled criteria in matching order
     * @param includeContained whether trailing candidate entries beyond the mask depth are accepted
     * @param candidate candidate view, or {@code null}
     * @param sink optional invocation-local diagnostic sink, or {@code null}
     * @param probe optional check seam used by tests for counting and fault injection, or {@code null}
     * @return true only when depth and every positional check succeed
     */
    public static boolean evaluateOrdinary(
            List<EntryCriterion> criteria,
            boolean includeContained,
            MatchCandidate candidate,
            MatchDiagnosticSink sink,
            MatchProbe probe) {
        if (candidate == null) {
            emit(sink, DiagnosticSeverity.TRACE, MatchExitCategory.NULL_CANDIDATE, 0, null,
                    NamingFieldKind.NONE);
            return false;
        }
        if (criteria == null || criteria.isEmpty()) {
            emit(sink, DiagnosticSeverity.ERROR, MatchExitCategory.EVALUATION_ERROR, candidate.depth(),
                    null, NamingFieldKind.NONE);
            return false;
        }
        return evaluatePositions(criteria, includeContained, candidate, sink,
                probe == null ? MatchProbe.DIRECT : probe);
    }

    /** Determines one ordinary result, preserving ordered checks and the first mismatch. */
    private static boolean evaluatePositions(
            List<EntryCriterion> criteria,
            boolean includeContained,
            MatchCandidate candidate,
            MatchDiagnosticSink sink,
            MatchProbe probe) {
        int depth = candidate.depth();
        int requiredDepth = criteria.size();
        boolean matches = true;
        MatchExitCategory category = MatchExitCategory.MATCH;
        NamingFieldKind field = NamingFieldKind.VALUE_PATTERN;
        Integer entryIndex = null;
        DiagnosticSeverity severity = DiagnosticSeverity.TRACE;
        if (depth < requiredDepth || (!includeContained && depth != requiredDepth)) {
            matches = false;
            category = MatchExitCategory.DEPTH_MISMATCH;
            field = NamingFieldKind.DEPTH;
        } else {
            for (int index = 0; index < requiredDepth; index++) {
                EntryCriterion criterion = criteria.get(index);
                NamingFieldKind evaluatingField = NamingFieldKind.QUALIFIER_PATTERN;
                try {
                    if (!probe.matchesQualifier(criterion, candidate.qualifierAt(index))) {
                        matches = false;
                        category = MatchExitCategory.QUALIFIER_MISMATCH;
                        field = NamingFieldKind.QUALIFIER_PATTERN;
                        entryIndex = index;
                        break;
                    }
                    evaluatingField = NamingFieldKind.VALUE_PATTERN;
                    if (!probe.matchesValue(criterion, candidate.valueAt(index))) {
                        matches = false;
                        category = MatchExitCategory.VALUE_MISMATCH;
                        field = NamingFieldKind.VALUE_PATTERN;
                        entryIndex = index;
                        break;
                    }
                    if (criterion.hasVersionConstraint()) {
                        evaluatingField = NamingFieldKind.VERSION_CONSTRAINT;
                        String version = candidate.versionAt(index);
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
