/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.matching;

import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * Compiled, immutable matching description of one positional mask criterion.
 *
 * <p>A criterion is compiled once, when its owning TopicMaskEntry is created:
 * the qualifier and value patterns are classified into bounded whole-field
 * shapes and the optional raw version constraint is parsed. Matching then
 * performs only those prevalidated checks. Compilation validates the
 * qualifier pattern, then the value pattern, then the constraint, so the
 * first invalid field determines the reported failure.</p>
 *
 * <p>The criterion holds no raw text: the TopicMaskEntry keeps the original
 * spelling for equality, rendering and interchange. Instances hold no mutable
 * state and are safe for concurrent reuse.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public final class EntryCriterion {
    private final FieldPattern qualifier;
    private final FieldPattern value;
    /** Parsed constraint, or {@code null} when the raw constraint was omitted. */
    private final VersionConstraint version;

    private EntryCriterion(FieldPattern qualifier, FieldPattern value, VersionConstraint version) {
        this.qualifier = qualifier;
        this.value = value;
        this.version = version;
    }

    /**
     * Validates and compiles one criterion.
     *
     * @param qualifierPattern raw qualifier pattern
     * @param valuePattern raw value pattern
     * @param versionConstraint raw constraint, {@code null} when omitted and {@code ""} when present-empty
     * @param operation operation used for failure metadata
     * @param index entry position used for failure metadata, or {@code null}
     * @return the compiled immutable criterion
     * @throws NamingRuleViolation when a pattern or the constraint is missing, malformed or oversized
     */
    public static EntryCriterion compile(
            String qualifierPattern,
            String valuePattern,
            String versionConstraint,
            NamingOperation operation,
            Integer index) {
        FieldPattern qualifier = FieldPattern.parse(qualifierPattern,
                NamingFieldKind.QUALIFIER_PATTERN, operation, index);
        FieldPattern value = FieldPattern.parse(valuePattern,
                NamingFieldKind.VALUE_PATTERN, operation, index);
        VersionConstraint version = VersionConstraint.parse(versionConstraint, operation, index);
        return new EntryCriterion(qualifier, value, version);
    }

    /**
     * Applies the qualifier pattern to one complete candidate qualifier.
     *
     * @param candidate complete candidate qualifier
     * @return true when the qualifier satisfies the pattern
     */
    public boolean matchesQualifier(String candidate) {
        return qualifier.matches(candidate);
    }

    /**
     * Applies the value pattern to one complete candidate value.
     *
     * @param candidate complete candidate value
     * @return true when the value satisfies the pattern
     */
    public boolean matchesValue(String candidate) {
        return value.matches(candidate);
    }

    /**
     * Applies the optional constraint to one opaque candidate version.
     *
     * @param candidate candidate version, or {@code null} when absent
     * @return true when no constraint was supplied or the candidate satisfies it
     */
    public boolean matchesVersion(String candidate) {
        return version == null || version.matches(candidate);
    }

    /**
     * Indicates whether a constraint was supplied, including a present-empty constraint.
     *
     * @return true when the version must be evaluated
     */
    public boolean hasVersionConstraint() {
        return version != null;
    }
}
