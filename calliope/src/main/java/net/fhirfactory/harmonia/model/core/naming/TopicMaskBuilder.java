/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.ArrayList;
import java.util.List;

/**
 * Reusable, caller-confined builder for immutable ordinary TopicMasks.
 *
 * <p>Every addition validates and classifies its criterion before mutating this
 * builder. A rejected addition leaves the accumulated criteria and containment
 * flag available for correction and retry.</p>
 */
public final class TopicMaskBuilder {
    private final List<TopicMaskEntry> entries = new ArrayList<>();
    private boolean includeContainedElements;

    /** Creates an empty builder with exact-depth matching as the default. */
    TopicMaskBuilder() {
    }

    /**
     * Adds a previously validated immutable criterion.
     *
     * @param entry non-null immutable mask entry
     * @return this builder for fluent construction
     * @throws NamingValidationException when entry is absent or maximum depth is reached
     */
    public TopicMaskBuilder add(TopicMaskEntry entry) {
        ensureCapacity();
        if (entry == null) {
            throw TopicFieldRules.failure(NamingFailureCategory.MISSING_INPUT,
                    NamingOperation.MASK_BUILD, NamingFieldKind.NONE, entries.size());
        }
        entries.add(entry);
        return this;
    }

    /**
     * Adds qualifier and value patterns with no version constraint.
     *
     * @param qualifierPattern pattern applied to the complete qualifier
     * @param valuePattern pattern applied to the complete value
     * @return this builder for fluent construction
     * @throws NamingValidationException when a pattern or depth is rejected
     */
    public TopicMaskBuilder addPatterns(String qualifierPattern, String valuePattern) {
        ensureCapacity();
        TopicMaskEntry entry = TopicMaskEntry.create(qualifierPattern, valuePattern, null,
                NamingOperation.MASK_BUILD, entries.size());
        entries.add(entry);
        return this;
    }

    /**
     * Adds qualifier and value patterns with a present raw version constraint.
     *
     * @param qualifierPattern pattern applied to the complete qualifier
     * @param valuePattern pattern applied to the complete value
     * @param versionConstraint optional raw version constraint; {@code null} omits it and {@code ""} is present-empty
     * @return this builder for fluent construction
     * @throws NamingValidationException when a pattern, constraint or depth is rejected
     */
    public TopicMaskBuilder addPatterns(
            String qualifierPattern,
            String valuePattern,
            String versionConstraint) {
        ensureCapacity();
        TopicMaskEntry entry = TopicMaskEntry.create(qualifierPattern, valuePattern, versionConstraint,
                NamingOperation.MASK_BUILD, entries.size());
        entries.add(entry);
        return this;
    }

    /**
     * Adds a value-pattern criterion with an unrestricted qualifier.
     *
     * @param valuePattern pattern applied to the complete value
     * @return this builder for fluent construction
     * @throws NamingValidationException when the pattern or capacity is rejected
     */
    public TopicMaskBuilder addForValue(String valuePattern) {
        ensureCapacity();
        TopicMaskEntry entry = TopicMaskEntry.create("*", valuePattern, null,
                NamingOperation.MASK_BUILD, entries.size());
        entries.add(entry);
        return this;
    }

    /**
     * Adds a qualifier-pattern criterion with an unrestricted value.
     *
     * @param qualifierPattern pattern applied to the complete qualifier
     * @return this builder for fluent construction
     * @throws NamingValidationException when the pattern or capacity is rejected
     */
    public TopicMaskBuilder addForQualifier(String qualifierPattern) {
        ensureCapacity();
        TopicMaskEntry entry = TopicMaskEntry.create(qualifierPattern, "*", null,
                NamingOperation.MASK_BUILD, entries.size());
        entries.add(entry);
        return this;
    }

    /**
     * Adds a criterion matching any qualifier and value; validates capacity first.
     *
     * @return this builder for fluent construction
     * @throws NamingValidationException when maximum mask depth is reached
     */
    public TopicMaskBuilder addAny() {
        ensureCapacity();
        entries.add(TopicMaskEntry.create("*", "*", null,
                NamingOperation.MASK_BUILD, entries.size()));
        return this;
    }

    /**
     * Adds a criterion for validated literal qualifier and value fields.
     *
     * @param qualifier nonempty valid Topic qualifier literal
     * @param value nonempty valid Topic value literal
     * @return this builder for fluent construction
     * @throws NamingValidationException when a literal or depth is rejected
     */
    public TopicMaskBuilder addLiterals(String qualifier, String value) {
        ensureCapacity();
        TopicMaskEntry entry = TopicMaskEntry.literals(qualifier, value, null, false,
                NamingOperation.MASK_BUILD, entries.size());
        entries.add(entry);
        return this;
    }

    /**
     * Adds a criterion for validated literal fields including a required version.
     *
     * @param qualifier nonempty valid Topic qualifier literal
     * @param value nonempty valid Topic value literal
     * @param version nonempty valid Topic version literal
     * @return this builder for fluent construction
     * @throws NamingValidationException when a literal or depth is rejected
     */
    public TopicMaskBuilder addLiterals(String qualifier, String value, String version) {
        ensureCapacity();
        TopicMaskEntry entry = TopicMaskEntry.literals(qualifier, value, version, true,
                NamingOperation.MASK_BUILD, entries.size());
        entries.add(entry);
        return this;
    }

    /**
     * Adds a literal value criterion with unrestricted qualifier.
     *
     * @param value valid nonempty Topic value literal
     * @return this builder for fluent construction
     * @throws NamingValidationException when the literal or capacity is rejected
     */
    public TopicMaskBuilder addForLiteralValue(String value) {
        ensureCapacity();
        TopicMaskEntry entry = TopicMaskEntry.literalValue(value, NamingOperation.MASK_BUILD, entries.size());
        entries.add(entry);
        return this;
    }

    /**
     * Adds a literal qualifier criterion with unrestricted value.
     *
     * @param qualifier valid nonempty printable-ASCII Topic qualifier literal
     * @return this builder for fluent construction
     * @throws NamingValidationException when the literal or capacity is rejected
     */
    public TopicMaskBuilder addForLiteralQualifier(String qualifier) {
        ensureCapacity();
        TopicMaskEntry entry = TopicMaskEntry.literalQualifier(
                qualifier, NamingOperation.MASK_BUILD, entries.size());
        entries.add(entry);
        return this;
    }

    /**
     * Sets whether ordinary matching accepts trailing entries beyond this mask's depth.
     *
     * @param include true to allow trailing candidate entries; false requires exact depth
     * @return this builder for fluent construction
     */
    public TopicMaskBuilder includeContainedElements(boolean include) {
        includeContainedElements = include;
        return this;
    }

    /**
     * Builds a defensive immutable ordinary-mask snapshot without consuming the builder.
     *
     * @return an immutable ordinary mask
     * @throws NamingValidationException when the builder contains no criteria
     */
    public TopicMask build() {
        if (entries.isEmpty()) {
            throw TopicFieldRules.failure(NamingFailureCategory.MISSING_INPUT,
                    NamingOperation.MASK_BUILD, NamingFieldKind.DEPTH, null);
        }
        return TopicMask.fromEntries(List.copyOf(entries), includeContainedElements);
    }

    /** Rejects a seventeenth criterion before construction or compilation work begins. */
    private void ensureCapacity() {
        if (entries.size() >= NamingLimits.MAX_DEPTH) {
            throw TopicFieldRules.failure(NamingFailureCategory.LIMIT_EXCEEDED,
                    NamingOperation.MASK_BUILD, NamingFieldKind.DEPTH, null);
        }
    }
}