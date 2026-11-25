/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.List;
import java.util.Objects;

/**
 * Immutable structural selector over ordered Topic entries.
 *
 * <p>Ordinary masks check each qualifier, value, and optional version at the
 * corresponding position. An explicit universal mask is a separate logical
 * kind and is not inferred from a broad expression.</p>
 *
 * <p>Canonical expressions and raw patterns may contain sensitive input and
 * are not sanitized for operational diagnostics or logging.</p>
 */
public final class TopicMask {
    private static final TopicMask UNIVERSAL = new TopicMask(true, List.of(), false);

    private final boolean universal;
    private final List<TopicMaskEntry> entries;
    private final boolean includeContainedElements;

    /** Copies immutable criteria and records the explicit universal/containment kind. */
    private TopicMask(boolean universal, List<TopicMaskEntry> entries, boolean includeContainedElements) {
        this.universal = universal;
        this.entries = List.copyOf(entries);
        this.includeContainedElements = includeContainedElements;
    }

    /**
     * Returns a new builder for an ordinary structural mask.
     *
     * @return an empty caller-confined TopicMaskBuilder
     */
    public static TopicMaskBuilder builder() {
        return new TopicMaskBuilder();
    }

    /**
     * Returns the explicit immutable universal mask.
     *
     * @return the shared universal mask value
     */
    public static TopicMask all() {
        return UNIVERSAL;
    }

    /**
     * Reconstructs an ordinary mask from its outer canonical expression.
     *
     * @param expression bounded escaped path of qualifier, value, and optional version expressions
     * @param includeContainedElements whether extra trailing Topic entries may match
     * @return an immutable ordinary mask
     * @throws NamingValidationException when syntax, patterns, constraints or limits are rejected
     */
    public static TopicMask fromCanonicalExpression(String expression, boolean includeContainedElements) {
        return MaskExpressionParser.parse(expression, includeContainedElements);
    }

    /** Creates an ordinary immutable mask from a nonempty validated criterion snapshot. */
    static TopicMask fromEntries(List<TopicMaskEntry> entries, boolean includeContainedElements) {
        if (entries.isEmpty()) {
            throw TopicFieldRules.failure(NamingFailureCategory.MISSING_INPUT,
                    NamingOperation.MASK_BUILD, NamingFieldKind.DEPTH, null);
        }
        return new TopicMask(false, entries, includeContainedElements);
    }

    /**
     * Returns whether this is the explicit universal logical kind.
     *
     * @return true only for the explicit universal kind
     */
    public boolean isUniversal() {
        return universal;
    }

    /**
     * Returns the immutable ordered criteria; empty for the universal kind.
     *
     * @return immutable criteria in matching order
     */
    public List<TopicMaskEntry> getEntries() {
        return entries;
    }

    /**
     * Returns whether ordinary matching includes extra trailing Topic entries.
     *
     * @return the ordinary mask's explicit containment setting
     * @throws IllegalStateException when this is the explicit universal kind
     */
    public boolean isIncludeContainedElements() {
        requireOrdinary();
        return includeContainedElements;
    }

    /**
     * Returns the canonical escaped expression path for an ordinary mask.
     *
     * <p>The rendering may contain sensitive patterns or constraints and is not
     * sanitized for operational diagnostics or logging.</p>
     *
     * @return escaped expression path with version presence preserved
     * @throws IllegalStateException when this is the explicit universal kind
     */
    public String getCanonicalExpression() {
        requireOrdinary();
        StringBuilder canonical = new StringBuilder();
        for (int index = 0; index < entries.size(); index++) {
            if (index > 0) {
                canonical.append('.');
            }
            TopicMaskEntry entry = entries.get(index);
            canonical.append(CanonicalPathSyntax.encode(entry.getQualifierPattern()));
            entry.getVersionConstraint().ifPresent(version -> canonical.append(':')
                    .append(CanonicalPathSyntax.encode(version)));
            canonical.append('=').append(CanonicalPathSyntax.encode(entry.getValuePattern()));
        }
        return canonical.toString();
    }

    /**
     * Returns whether this mask matches the complete candidate Topic.
     *
     * <p>This method performs neutral Boolean selection only; a positive result
     * does not authorize or perform any operation.</p>
     *
     * @param candidate candidate Topic; null is always rejected with false
     * @return true only when the explicit kind and all positional constraints match
     */
    public boolean matches(Topic candidate) {
        return MaskEvaluator.evaluate(this, candidate, null, null);
    }

    /** Runs the matcher with an optional invocation-local safe-metadata sink. */
    boolean matches(Topic candidate, MatchDiagnosticSink sink) {
        return MaskEvaluator.evaluate(this, candidate, sink, null);
    }

    /** Provides a package-private counter and fault-injection seam for bounded evaluation tests. */
    boolean matches(Topic candidate, MatchDiagnosticSink sink, MatchProbe probe) {
        return MaskEvaluator.evaluate(this, candidate, sink, probe);
    }

    /** Indicates the criterion-bearing kind without exposing its internal enum representation. */
    boolean isOrdinary() {
        return !universal;
    }

    /** Returns the construction flag to the package-scoped evaluator. */
    boolean getContainmentFlag() {
        return includeContainedElements;
    }

    /** Prevents expression-only accessors from pretending a universal mask has criteria. */
    private void requireOrdinary() {
        if (universal) {
            throw new IllegalStateException("This accessor applies only to ordinary TopicMasks");
        }
    }

    /** Compares universal kind or, for ordinary masks, ordered criteria and containment. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TopicMask mask) || universal != mask.universal) {
            return false;
        }
        return universal || (includeContainedElements == mask.includeContainedElements
                && entries.equals(mask.entries));
    }

    /** Hashes the same logical fields used by {@link #equals(Object)}. */
    @Override
    public int hashCode() {
        return universal ? 1 : Objects.hash(entries, includeContainedElements);
    }

    /**
     * Returns a descriptive universal label or the ordinary canonical expression.
     *
     * <p>An ordinary rendering may contain sensitive patterns or constraints
     * and is not sanitized diagnostic output.</p>
     *
     * @return a descriptive label for universal masks or the ordinary canonical expression
     */
    @Override
    public String toString() {
        return universal ? "TopicMask.all()" : getCanonicalExpression();
    }
}