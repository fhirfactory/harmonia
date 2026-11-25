/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.Objects;
import java.util.Optional;

/**
 * One immutable positional criterion over a Topic qualifier, value and optional version.
 *
 * <p>Pattern text is retained exactly as supplied and classified once into
 * bounded whole-field operations. A version constraint preserves its raw
 * spelling and distinguishes omission from a present-empty nonmatching
 * constraint. Entries are reusable concurrently after construction.</p>
 */
public final class TopicMaskEntry {
    private final String qualifierPattern;
    private final String valuePattern;
    private final String versionConstraint;
    private final FieldPattern parsedQualifier;
    private final FieldPattern parsedValue;
    private final VersionConstraint parsedVersion;

    private TopicMaskEntry(
            String qualifierPattern,
            String valuePattern,
            String versionConstraint,
            FieldPattern parsedQualifier,
            FieldPattern parsedValue,
            VersionConstraint parsedVersion) {
        this.qualifierPattern = qualifierPattern;
        this.valuePattern = valuePattern;
        this.versionConstraint = versionConstraint;
        this.parsedQualifier = parsedQualifier;
        this.parsedValue = parsedValue;
        this.parsedVersion = parsedVersion;
    }

    /** Validates original fields and creates immutable parsed descriptions. */
    static TopicMaskEntry create(
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
        return new TopicMaskEntry(qualifierPattern, valuePattern, versionConstraint,
                qualifier, value, version);
    }

    /**
     * Creates a criterion with no version constraint.
     *
     * @param qualifierPattern bounded qualifier pattern in the accepted wildcard grammar
     * @param valuePattern bounded value pattern in the accepted wildcard grammar
     * @return an immutable criterion preserving both raw patterns
     * @throws NamingValidationException when either pattern is missing, malformed, unsupported or oversized
     */
    public static TopicMaskEntry of(String qualifierPattern, String valuePattern) {
        return create(qualifierPattern, valuePattern, null, NamingOperation.MASK_ENTRY_BUILD, null);
    }

    /**
     * Creates a criterion with an optional raw version constraint.
     *
     * @param qualifierPattern bounded qualifier pattern in the accepted wildcard grammar
     * @param valuePattern bounded value pattern in the accepted wildcard grammar
     * @param versionConstraint raw version constraint, {@code null} when omitted and {@code ""} when present-empty
     * @return an immutable criterion retaining the original constraint spelling
     * @throws NamingValidationException when any supplied pattern or constraint is invalid or oversized
     */
    public static TopicMaskEntry of(
            String qualifierPattern,
            String valuePattern,
            String versionConstraint) {
        return create(qualifierPattern, valuePattern, versionConstraint,
                NamingOperation.MASK_ENTRY_BUILD, null);
    }

    /**
     * Creates a criterion with an unrestricted qualifier and the supplied value pattern.
     *
     * @param valuePattern accepted value pattern
     * @return an immutable criterion
     * @throws NamingValidationException when the pattern is invalid or oversized
     */
    public static TopicMaskEntry forValue(String valuePattern) {
        return of("*", valuePattern);
    }

    /**
     * Creates a criterion with the supplied qualifier pattern and an unrestricted value.
     *
     * @param qualifierPattern accepted qualifier pattern
     * @return an immutable criterion
     * @throws NamingValidationException when the pattern is invalid or oversized
     */
    public static TopicMaskEntry forQualifier(String qualifierPattern) {
        return of(qualifierPattern, "*");
    }

    /**
     * Creates an ordinary criterion unrestricted over qualifier and value.
     *
     * @return an immutable criterion equivalent to two unrestricted patterns
     */
    public static TopicMaskEntry any() {
        return of("*", "*");
    }

    /**
     * Creates a criterion matching validated literal qualifier and value fields.
     *
     * @param qualifier completed literal qualifier
     * @param value completed literal value
     * @return an immutable criterion with pattern metacharacters escaped
     * @throws NamingValidationException when either literal violates Topic field rules
     */
    public static TopicMaskEntry ofLiterals(String qualifier, String value) {
        return literals(qualifier, value, null, false, NamingOperation.MASK_ENTRY_BUILD, null);
    }

    /**
     * Creates a criterion matching literal fields and a required literal version.
     *
     * @param qualifier completed literal qualifier
     * @param value completed literal value
     * @param version nonempty completed literal version; a reserved leading character is escaped
     * @return an immutable criterion with exact version matching
     * @throws NamingValidationException when any literal is absent, invalid or oversized
     */
    public static TopicMaskEntry ofLiterals(String qualifier, String value, String version) {
        return literals(qualifier, value, version, true, NamingOperation.MASK_ENTRY_BUILD, null);
    }

    /**
     * Creates a criterion matching one validated literal value with any qualifier.
     *
     * @param value nonempty completed literal Topic value
     * @return an immutable criterion with the literal pattern safely escaped
     * @throws NamingValidationException when the value violates Topic field rules
     */
    public static TopicMaskEntry forLiteralValue(String value) {
        return literalValue(value, NamingOperation.MASK_ENTRY_BUILD, null);
    }

    /**
     * Creates a criterion matching one validated literal qualifier with any value.
     *
     * @param qualifier nonempty completed printable-ASCII qualifier
     * @return an immutable criterion with the literal pattern safely escaped
     * @throws NamingValidationException when the qualifier violates Topic field rules
     */
    public static TopicMaskEntry forLiteralQualifier(String qualifier) {
        return literalQualifier(qualifier, NamingOperation.MASK_ENTRY_BUILD, null);
    }

    /**
     * Returns the original qualifier pattern, preserving its wildcard escapes.
     *
     * @return the raw qualifier pattern
     */
    public String getQualifierPattern() {
        return qualifierPattern;
    }

    /**
     * Returns the original value pattern, preserving its wildcard escapes.
     *
     * @return the raw value pattern
     */
    public String getValuePattern() {
        return valuePattern;
    }

    /**
     * Returns the optional original constraint, including present-empty and leading escapes.
     *
     * @return the raw constraint when supplied, or empty when omitted
     */
    public Optional<String> getVersionConstraint() {
        return Optional.ofNullable(versionConstraint);
    }

    /** Applies the prevalidated qualifier pattern to one complete candidate field. */
    boolean matchesQualifier(String candidate) {
        return parsedQualifier.matches(candidate);
    }

    /** Applies the prevalidated value pattern to one complete candidate field. */
    boolean matchesValue(String candidate) {
        return parsedValue.matches(candidate);
    }

    /** Applies the optional prevalidated version constraint to an opaque candidate version. */
    boolean matchesVersion(String candidate) {
        return parsedVersion == null || parsedVersion.matches(candidate);
    }

    /** Indicates whether a version constraint was present, including present-empty. */
    boolean hasVersionConstraint() {
        return versionConstraint != null;
    }

    /** Creates literal criteria while preserving the caller's construction operation and index. */
    static TopicMaskEntry literals(
            String qualifier,
            String value,
            String version,
            boolean requireVersion,
            NamingOperation operation,
            Integer index) {
        validateLiteralFields(qualifier, value, version, requireVersion, operation, index);
        String exactVersion = version != null && startsWithReservedVersionCharacter(version)
                ? "\\" + version : version;
        return create(FieldPattern.escapeLiteral(qualifier), FieldPattern.escapeLiteral(value),
                exactVersion, operation, index);
    }

    /** Creates a validated literal value criterion for a factory or builder. */
    static TopicMaskEntry literalValue(String value, NamingOperation operation, Integer index) {
        TopicFieldRules.validateValue(value, operation, index);
        return create("*", FieldPattern.escapeLiteral(value), null, operation, index);
    }

    /** Creates a validated literal qualifier criterion for a factory or builder. */
    static TopicMaskEntry literalQualifier(String qualifier, NamingOperation operation, Integer index) {
        TopicFieldRules.validateQualifierInput(qualifier, false, operation, index);
        return create(FieldPattern.escapeLiteral(qualifier), "*", null, operation, index);
    }

    /** Validates literal helper inputs before escaping them into pattern syntax. */
    private static void validateLiteralFields(
            String qualifier,
            String value,
            String version,
            boolean requireVersion,
            NamingOperation operation,
            Integer index) {
        TopicFieldRules.validateTopicCompletedFields(qualifier, value, version, operation, index);
        if (requireVersion && (version == null || version.isEmpty())) {
            throw TopicFieldRules.failure(version == null ? NamingFailureCategory.MISSING_INPUT
                            : NamingFailureCategory.INVALID_FIELD,
                    operation, NamingFieldKind.VERSION, index);
        }
    }

    /** Identifies an exact version that requires the reserved leading escape. */
    private static boolean startsWithReservedVersionCharacter(String version) {
        char first = version.charAt(0);
        return first == '=' || first == '<' || first == '>' || first == '\\';
    }

    /** Compares the complete original logical record, not its derived matching descriptions. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TopicMaskEntry entry)) {
            return false;
        }
        return qualifierPattern.equals(entry.qualifierPattern)
                && valuePattern.equals(entry.valuePattern)
                && Objects.equals(versionConstraint, entry.versionConstraint);
    }

    /** Hashes the same original pattern and constraint data used by {@link #equals(Object)}. */
    @Override
    public int hashCode() {
        return Objects.hash(qualifierPattern, valuePattern, versionConstraint);
    }
}