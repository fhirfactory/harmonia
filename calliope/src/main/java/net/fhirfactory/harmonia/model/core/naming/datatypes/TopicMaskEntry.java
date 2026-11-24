/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.core.naming.datatypes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.re2j.Pattern;
import net.fhirfactory.harmonia.model.core.naming.support.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.support.TopicLimits;
import net.fhirfactory.harmonia.model.core.naming.support.TopicMaskJsonDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * One immutable positional set of independent Topic qualifier, value, and optional version expressions.
 * <p>
 * Expressions use the pinned RE2/J dialect and full-field matching. The original
 * expressions are retained for equality and canonical interchange; immutable
 * compiled patterns are reused while each matcher remains invocation-local.
 * Construction fails closed with {@link IllegalArgumentException} for null,
 * malformed Unicode, oversized, or uncompilable expressions (including
 * constructs RE2/J does not support, such as backreferences and lookaround).
 * Failure messages and diagnostics name only the field category, never the
 * expression text, and the RE2/J parser exception is deliberately not chained.
 * This implementation follows {@code DRP-Topic-and-TopicMask.Final.001.001.md} §§4, 6.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = TopicMaskJsonDecoder.EntryDeserializer.class)
public final class TopicMaskEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicMaskEntry.class);

    private final String qualifierRegex;
    private final String valueRegex;
    private final String versionRegex;
    private final Pattern qualifierPattern;
    private final Pattern valuePattern;
    private final Pattern versionPattern;

    /** Creates an entry after its public factories or strict logical decoder validate the fields. */
    private TopicMaskEntry(String qualifierRegex, String valueRegex, String versionRegex) {
        this.qualifierRegex = requireExpression(qualifierRegex, "qualifier");
        this.valueRegex = requireExpression(valueRegex, "value");
        this.versionRegex = versionRegex == null ? null : requireExpression(versionRegex, "version");
        this.qualifierPattern = compile(this.qualifierRegex, "qualifier");
        this.valuePattern = compile(this.valueRegex, "value");
        this.versionPattern = this.versionRegex == null ? null : compile(this.versionRegex, "version");
    }

    /**
     * Creates an expression entry with no version constraint.
     *
     * @param qualifierRegex RE2/J expression for the qualifier
     * @param valueRegex RE2/J expression for the value
     * @return validated immutable entry
     */
    public static TopicMaskEntry of(String qualifierRegex, String valueRegex) {
        return new TopicMaskEntry(qualifierRegex, valueRegex, null);
    }

    /**
     * Creates an expression entry with an optional version constraint.
     * A non-null empty expression requires a present version but cannot match a valid Topic version.
     *
     * @param qualifierRegex RE2/J qualifier expression
     * @param valueRegex RE2/J value expression
     * @param versionRegex version expression, empty expression, or null for no constraint
     * @return validated immutable entry
     * @throws IllegalArgumentException when any present expression is invalid
     */
    public static TopicMaskEntry of(String qualifierRegex, String valueRegex, String versionRegex) {
        return new TopicMaskEntry(qualifierRegex, valueRegex, versionRegex);
    }

    /**
     * Creates an entry that leaves qualifiers unrestricted and constrains values.
     *
     * @param valueRegex RE2/J value expression
     * @return validated immutable entry
     */
    public static TopicMaskEntry forValue(String valueRegex) {
        return of(".*", valueRegex);
    }

    /**
     * Creates an entry that constrains qualifiers and leaves values unrestricted.
     *
     * @param qualifierRegex RE2/J qualifier expression
     * @return validated immutable entry
     */
    public static TopicMaskEntry forQualifier(String qualifierRegex) {
        return of(qualifierRegex, ".*");
    }

    /**
     * Creates an entry matching any valid qualifier and value at this position.
     *
     * @return validated unrestricted positional entry
     */
    public static TopicMaskEntry any() {
        return of(".*", ".*");
    }

    /**
     * Creates literal qualifier and value criteria, leaving version unconstrained.
     *
     * @param qualifier valid literal qualifier
     * @param value valid literal value
     * @return validated literal entry
     */
    public static TopicMaskEntry ofLiterals(String qualifier, String value) {
        try {
            TopicFieldRules.requireQualifier(qualifier);
            TopicFieldRules.requireValue(value);
        } catch (IllegalArgumentException exception) {
            logConstructionFailure("literal");
            throw exception;
        }
        return of(Pattern.quote(qualifier), Pattern.quote(value));
    }

    /**
     * Creates literal qualifier, value, and version criteria.
     *
     * @param qualifier valid literal qualifier
     * @param value valid literal value
     * @param version valid nonempty literal version
     * @return validated literal entry
     */
    public static TopicMaskEntry ofLiterals(String qualifier, String value, String version) {
        try {
            TopicFieldRules.requireQualifier(qualifier);
            TopicFieldRules.requireValue(value);
            TopicFieldRules.requireVersion(version);
        } catch (IllegalArgumentException exception) {
            logConstructionFailure("literal");
            throw exception;
        }
        return of(Pattern.quote(qualifier), Pattern.quote(value), Pattern.quote(version));
    }

    /**
     * Creates a literal value criterion with an unrestricted qualifier.
     *
     * @param value valid literal Topic value
     * @return validated literal value entry
     */
    public static TopicMaskEntry forLiteralValue(String value) {
        try {
            TopicFieldRules.requireValue(value);
        } catch (IllegalArgumentException exception) {
            logConstructionFailure("literal");
            throw exception;
        }
        return of(".*", Pattern.quote(value));
    }

    /**
     * Creates a literal qualifier criterion with an unrestricted value.
     *
     * @param qualifier valid literal Topic qualifier
     * @return validated literal qualifier entry
     */
    public static TopicMaskEntry forLiteralQualifier(String qualifier) {
        try {
            TopicFieldRules.requireQualifier(qualifier);
        } catch (IllegalArgumentException exception) {
            logConstructionFailure("literal");
            throw exception;
        }
        return of(Pattern.quote(qualifier), ".*");
    }

    /** @return original qualifier expression */
    @JsonProperty("qualifierRegex")
    public String getQualifierRegex() {
        return qualifierRegex;
    }

    /** @return original value expression */
    @JsonProperty("valueRegex")
    public String getValueRegex() {
        return valueRegex;
    }

    /** @return present version expression, including an empty expression, or empty when unconstrained */
    public Optional<String> getVersionRegex() {
        return Optional.ofNullable(versionRegex);
    }

    /** @return nullable version field used only by the logical JSON adapter */
    @JsonProperty("versionRegex")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String versionRegexForJson() {
        return versionRegex;
    }

    /**
     * Evaluates the qualifier expression using full-field RE2/J matching.
     *
     * @param candidate candidate qualifier; null never matches
     * @return true when the whole qualifier matches
     */
    public boolean matchesQualifier(String candidate) {
        return candidate != null && qualifierPattern.matcher(candidate).matches();
    }

    /**
     * Evaluates the value expression using full-field RE2/J matching.
     *
     * @param candidate candidate value; null never matches
     * @return true when the whole value matches
     */
    public boolean matchesValue(String candidate) {
        return candidate != null && valuePattern.matcher(candidate).matches();
    }

    /**
     * Evaluates a present version expression using full-field RE2/J matching.
     * Callers decide separately whether an absent constraint applies.
     *
     * @param candidate candidate Topic version; null never matches
     * @return true only when a version expression exists and fully matches
     */
    public boolean matchesVersion(String candidate) {
        return versionPattern != null && candidate != null && versionPattern.matcher(candidate).matches();
    }

    /** Validates an original decoded RE2/J expression before compilation. */
    private static String requireExpression(String expression, String field) {
        if (expression == null) {
            logConstructionFailure(field);
            throw new IllegalArgumentException(field + " expression must not be null");
        }
        try {
            TopicFieldRules.requireWellFormedUnicode(expression, field + " expression");
            TopicLimits.requireLength(expression, TopicLimits.MAX_REGEX_CODE_POINTS, field + " expression");
        } catch (IllegalArgumentException exception) {
            logConstructionFailure(field);
            throw exception;
        }
        return expression;
    }

    /** Compiles with RE2/J and strips parser exception details that may contain the expression. */
    private static Pattern compile(String expression, String field) {
        try {
            return Pattern.compile(expression);
        } catch (RuntimeException exception) {
            logConstructionFailure(field);
            throw new IllegalArgumentException("Invalid RE2/J " + field + " expression");
        }
    }

    /** Logs only a fixed field category; expressions and exception payloads are never emitted. */
    private static void logConstructionFailure(String field) {
        try {
            LOGGER.error("TopicMask entry construction rejected field={}", field);
        } catch (RuntimeException ignored) {
            // Logging failure cannot change the invalid construction result.
        }
    }

    /** Serializes only original expressions so compiled engine state is reconstructed. */
    private Object writeReplace() {
        return new SerializationProxy(qualifierRegex, valueRegex, versionRegex);
    }

    /** Requires proxy reconstruction so stored expressions are revalidated and recompiled. */
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Serialization proxy required");
    }

    /**
     * Compares original expressions including the distinction between absent
     * and present-empty version constraints.
     *
     * @param other comparison value
     * @return true when all logical expression fields are equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TopicMaskEntry)) {
            return false;
        }
        TopicMaskEntry that = (TopicMaskEntry) other;
        return Objects.equals(qualifierRegex, that.qualifierRegex)
                && Objects.equals(valueRegex, that.valueRegex)
                && Objects.equals(versionRegex, that.versionRegex);
    }

    /** @return hash derived from all original expression strings and version presence */
    @Override
    public int hashCode() {
        return Objects.hash(qualifierRegex, valueRegex, versionRegex);
    }

    /** @return safe structural text indicating only whether a version constraint exists */
    @Override
    public String toString() {
        return "TopicMaskEntry{versionConstraint=" + (versionRegex == null ? "absent" : "present") + '}';
    }

    /** Java serialization form holding only original expressions, revalidated on read. */
    private static final class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String qualifierRegex;
        private final String valueRegex;
        private final String versionRegex;

        private SerializationProxy(String qualifierRegex, String valueRegex, String versionRegex) {
            this.qualifierRegex = qualifierRegex;
            this.valueRegex = valueRegex;
            this.versionRegex = versionRegex;
        }

        private Object readResolve() throws InvalidObjectException {
            try {
                return TopicMaskEntry.of(qualifierRegex, valueRegex, versionRegex);
            } catch (IllegalArgumentException exception) {
                throw new InvalidObjectException("Invalid TopicMask entry proxy");
            }
        }
    }
}