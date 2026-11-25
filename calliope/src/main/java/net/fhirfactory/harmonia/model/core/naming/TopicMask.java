/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fhirfactory.harmonia.model.core.naming.canonical.CanonicalEntryFields;
import net.fhirfactory.harmonia.model.core.naming.canonical.CanonicalPathSyntax;
import net.fhirfactory.harmonia.model.core.naming.canonical.MaskExpressionParser;
import net.fhirfactory.harmonia.model.core.naming.matching.EntryCriterion;
import net.fhirfactory.harmonia.model.core.naming.matching.MaskEvaluator;
import net.fhirfactory.harmonia.model.core.naming.matching.MatchCandidate;
import net.fhirfactory.harmonia.model.core.naming.matching.MatchDiagnosticSink;
import net.fhirfactory.harmonia.model.core.naming.matching.MatchProbe;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * Immutable structural selector over ordered Topic entries.
 *
 * <p>Ordinary masks check each qualifier, value, and optional version at the
 * corresponding position. An explicit universal mask is a separate logical
 * kind and is not inferred from a broad expression.</p>
 *
 * <p>TopicMask is the principal selector value. It owns its ordered entries
 * and delegates evaluation to the matching package, passing the entries'
 * compiled criteria and a read-only view of the candidate. Matching supplies
 * a Boolean selection only; it never grants authorization.</p>
 *
 * <p>Instances are immutable and safe for concurrent reuse; evaluation keeps
 * no shared mutable state and no result cache. Canonical expressions and raw
 * patterns may contain sensitive input and are not sanitized for operational
 * diagnostics or logging.</p>
 *
 * <p>Masks are serializable through a private proxy carrying the explicit
 * kind, containment flag and raw criterion text. Deserialization recompiles
 * every criterion through strict validation, restores the universal kind as
 * the shared {@link #all()} instance and rejects streams that bypass the
 * proxy.</p>
 */
public final class TopicMask implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final TopicMask UNIVERSAL = new TopicMask(true, List.of(), false);

    /** Explicit universal kind; serialized only through {@link SerialForm}. */
    private final transient boolean universal;
    /** Ordered criteria; serialized only through {@link SerialForm}. */
    private final transient List<TopicMaskEntry> entries;
    /** Compiled criteria in entry order, derived once from {@link #entries}; never serialized. */
    private final transient List<EntryCriterion> criteria;
    /** Ordinary containment flag; serialized only through {@link SerialForm}. */
    private final transient boolean includeContainedElements;

    /** Copies immutable criteria and records the explicit universal/containment kind. */
    private TopicMask(boolean universal, List<TopicMaskEntry> entries, boolean includeContainedElements) {
        if (!universal && entries.size() > NamingLimits.MAX_DEPTH) {
            throw NamingValidationException.of(NamingFailureCategory.LIMIT_EXCEEDED,
                    NamingOperation.MASK_BUILD, NamingFieldKind.DEPTH, null);
        }
        this.universal = universal;
        this.entries = List.copyOf(entries);
        this.criteria = this.entries.stream().map(TopicMaskEntry::criterion).toList();
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
        List<CanonicalEntryFields> fields = NamingBoundary.guard(() -> MaskExpressionParser.parse(expression));
        List<TopicMaskEntry> parsed = new ArrayList<>(fields.size());
        for (int index = 0; index < fields.size(); index++) {
            CanonicalEntryFields field = fields.get(index);
            parsed.add(TopicMaskEntry.create(field.qualifierField(), field.valueField(), field.versionField(),
                    NamingOperation.MASK_PARSE, index));
        }
        return fromEntries(parsed, includeContainedElements);
    }

    /** Creates an ordinary immutable mask from a nonempty validated criterion snapshot. */
    static TopicMask fromEntries(List<TopicMaskEntry> entries, boolean includeContainedElements) {
        if (entries.isEmpty()) {
            throw NamingValidationException.of(NamingFailureCategory.MISSING_INPUT,
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
        return matches(candidate, null, null);
    }

    /** Runs the matcher with an optional invocation-local safe-metadata sink. */
    boolean matches(Topic candidate, MatchDiagnosticSink sink) {
        return matches(candidate, sink, null);
    }

    /** Provides a package-private counter and fault-injection seam for bounded evaluation tests. */
    boolean matches(Topic candidate, MatchDiagnosticSink sink, MatchProbe probe) {
        MatchCandidate view = candidate == null ? null : new TopicView(candidate);
        return universal
                ? MaskEvaluator.evaluateUniversal(view, sink)
                : MaskEvaluator.evaluateOrdinary(criteria, includeContainedElements, view, sink, probe);
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

    /**
     * Serializes through {@link SerialForm} so deserialization recompiles and re-validates.
     *
     * @return the validating serialization proxy
     */
    @Serial
    private Object writeReplace() {
        return new SerialForm(this);
    }

    /**
     * Rejects any stream that tries to restore a mask without its validating proxy.
     *
     * @param stream the object stream, which is never read
     * @throws InvalidObjectException always, because only the proxy may be deserialized
     */
    @Serial
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw NamingSerialization.invalid(TopicMask.class);
    }

    /**
     * Serialized form of a mask: explicit kind, containment and parallel raw criterion arrays.
     *
     * <p>A universal form must carry empty arrays and resolves to {@link #all()}.
     * An ordinary form must carry one to sixteen criteria; a {@code null}
     * constraint element records omission and {@code ""} a present-empty
     * constraint.</p>
     */
    static final class SerialForm implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private final boolean universal;
        private final boolean includeContainedElements;
        private final String[] qualifierPatterns;
        private final String[] valuePatterns;
        private final String[] versionConstraints;

        /** Captures the logical fields of a completed mask. */
        private SerialForm(TopicMask mask) {
            int depth = mask.entries.size();
            this.universal = mask.universal;
            this.includeContainedElements = mask.includeContainedElements;
            this.qualifierPatterns = new String[depth];
            this.valuePatterns = new String[depth];
            this.versionConstraints = new String[depth];
            for (int index = 0; index < depth; index++) {
                TopicMaskEntry entry = mask.entries.get(index);
                qualifierPatterns[index] = entry.getQualifierPattern();
                valuePatterns[index] = entry.getValuePattern();
                versionConstraints[index] = entry.getVersionConstraint().orElse(null);
            }
        }

        /** Creates an arbitrary form; used by tests to exercise rejection of crafted streams. */
        SerialForm(boolean universal, boolean includeContainedElements, String[] qualifierPatterns,
                String[] valuePatterns, String[] versionConstraints) {
            this.universal = universal;
            this.includeContainedElements = includeContainedElements;
            this.qualifierPatterns = qualifierPatterns;
            this.valuePatterns = valuePatterns;
            this.versionConstraints = versionConstraints;
        }

        /** Rebuilds the mask through strict validation, preserving the universal singleton. */
        @Serial
        private Object readResolve() throws ObjectStreamException {
            if (universal) {
                if (includeContainedElements || !isEmpty(qualifierPatterns) || !isEmpty(valuePatterns)
                        || !isEmpty(versionConstraints)) {
                    throw NamingSerialization.invalid(TopicMask.class);
                }
                return UNIVERSAL;
            }
            int depth = NamingSerialization.requireParallel(TopicMask.class,
                    qualifierPatterns, valuePatterns, versionConstraints);
            try {
                List<TopicMaskEntry> rebuilt = new ArrayList<>(depth);
                for (int index = 0; index < depth; index++) {
                    rebuilt.add(TopicMaskEntry.create(qualifierPatterns[index], valuePatterns[index],
                            versionConstraints[index], NamingOperation.MASK_BUILD, index));
                }
                return fromEntries(rebuilt, includeContainedElements);
            } catch (NamingValidationException rejected) {
                throw NamingSerialization.invalid(TopicMask.class);
            }
        }

        /** Treats an absent array as empty for the universal-kind check. */
        private static boolean isEmpty(String[] values) {
            return values == null || values.length == 0;
        }
    }

    /** Invocation-local read-only view that lets the evaluator read a Topic without depending on it. */
    private record TopicView(Topic topic) implements MatchCandidate {
        @Override
        public int depth() {
            return topic.getDepth();
        }

        @Override
        public String qualifierAt(int index) {
            return topic.getEntries().get(index).getQualifier();
        }

        @Override
        public String valueAt(int index) {
            return topic.getEntries().get(index).getValue();
        }

        @Override
        public String versionAt(int index) {
            return topic.getEntries().get(index).versionOrNull();
        }
    }
}