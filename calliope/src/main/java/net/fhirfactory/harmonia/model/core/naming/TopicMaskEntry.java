/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import net.fhirfactory.harmonia.model.core.naming.matching.EntryCriterion;
import net.fhirfactory.harmonia.model.core.naming.matching.FieldPattern;
import net.fhirfactory.harmonia.model.core.naming.matching.VersionConstraint;
import net.fhirfactory.harmonia.model.core.naming.validation.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * One immutable positional criterion over a Topic qualifier, value and optional version.
 *
 * <p>Pattern text is retained exactly as supplied and classified once into
 * bounded whole-field operations. A version constraint preserves its raw
 * spelling and distinguishes omission from a present-empty nonmatching
 * constraint. Entries are reusable concurrently after construction.</p>
 *
 * <p>The entry is a dependent value of its TopicMask. It keeps the original
 * raw text as its identity and owns one compiled {@link EntryCriterion} from
 * the matching package, which the mask evaluates; the compiled form is never
 * exposed through the supported API.</p>
 *
 * <p>Entries are serializable through a private proxy carrying only the raw
 * patterns and optional raw constraint. Deserialization recompiles the
 * criterion through the same validation and rejects streams that bypass the
 * proxy; the compiled form is never written.</p>
 */
public final class TopicMaskEntry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /** Raw qualifier pattern; serialized only through {@link SerialForm}. */
    private final transient String qualifierPattern;
    /** Raw value pattern; serialized only through {@link SerialForm}. */
    private final transient String valuePattern;
    /** Raw constraint, {@code null} when omitted; serialized only through {@link SerialForm}. */
    private final transient String versionConstraint;
    /** Compiled description; derived from the raw fields and never serialized. */
    private final transient EntryCriterion criterion;

    private TopicMaskEntry(
            String qualifierPattern,
            String valuePattern,
            String versionConstraint,
            EntryCriterion criterion) {
        this.qualifierPattern = qualifierPattern;
        this.valuePattern = valuePattern;
        this.versionConstraint = versionConstraint;
        this.criterion = criterion;
    }

    /** Validates and compiles original fields, reporting the caller's operation and index. */
    static TopicMaskEntry create(
            String qualifierPattern,
            String valuePattern,
            String versionConstraint,
            NamingOperation operation,
            Integer index) {
        EntryCriterion criterion = NamingBoundary.guard(() -> EntryCriterion.compile(
                qualifierPattern, valuePattern, versionConstraint, operation, index));
        return new TopicMaskEntry(qualifierPattern, valuePattern, versionConstraint, criterion);
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

    /** Returns the compiled matching description owned by this entry. */
    EntryCriterion criterion() {
        return criterion;
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
        String exactVersion = version == null ? null : VersionConstraint.escapeExactText(version);
        return create(FieldPattern.escapeLiteral(qualifier), FieldPattern.escapeLiteral(value),
                exactVersion, operation, index);
    }

    /** Creates a validated literal value criterion for a factory or builder. */
    static TopicMaskEntry literalValue(String value, NamingOperation operation, Integer index) {
        NamingBoundary.check(() -> TopicFieldRules.validateValue(value, operation, index));
        return create("*", FieldPattern.escapeLiteral(value), null, operation, index);
    }

    /** Creates a validated literal qualifier criterion for a factory or builder. */
    static TopicMaskEntry literalQualifier(String qualifier, NamingOperation operation, Integer index) {
        NamingBoundary.check(() -> TopicFieldRules.validateQualifierInput(qualifier, false, operation, index));
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
        NamingBoundary.check(() -> TopicFieldRules.validateTopicCompletedFields(
                qualifier, value, version, operation, index));
        if (requireVersion && (version == null || version.isEmpty())) {
            throw NamingValidationException.of(version == null ? NamingFailureCategory.MISSING_INPUT
                            : NamingFailureCategory.INVALID_FIELD,
                    operation, NamingFieldKind.VERSION, index);
        }
    }

    /**
     * Serializes through {@link SerialForm} so deserialization recompiles and re-validates.
     *
     * @return the validating serialization proxy
     */
    @Serial
    private Object writeReplace() {
        return new SerialForm(qualifierPattern, valuePattern, versionConstraint);
    }

    /**
     * Rejects any stream that tries to restore an entry without its validating proxy.
     *
     * @param stream the object stream, which is never read
     * @throws InvalidObjectException always, because only the proxy may be deserialized
     */
    @Serial
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw NamingSerialization.invalid(TopicMaskEntry.class);
    }

    /** Serialized form of one criterion; a {@code null} constraint records omission. */
    static final class SerialForm implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private final String qualifierPattern;
        private final String valuePattern;
        private final String versionConstraint;

        /** Captures raw fields; also used by tests to exercise rejection of crafted streams. */
        SerialForm(String qualifierPattern, String valuePattern, String versionConstraint) {
            this.qualifierPattern = qualifierPattern;
            this.valuePattern = valuePattern;
            this.versionConstraint = versionConstraint;
        }

        /** Recompiles the criterion through strict pattern and constraint validation. */
        @Serial
        private Object readResolve() throws ObjectStreamException {
            try {
                return create(qualifierPattern, valuePattern, versionConstraint,
                        NamingOperation.MASK_ENTRY_BUILD, null);
            } catch (NamingValidationException rejected) {
                throw NamingSerialization.invalid(TopicMaskEntry.class);
            }
        }
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