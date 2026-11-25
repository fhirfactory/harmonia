/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.matching;

import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;
import net.fhirfactory.harmonia.model.core.naming.validation.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * Immutable description of one accepted, bounded whole-field wildcard pattern.
 *
 * <p>The parser decodes only escaped asterisks and backslashes. It classifies
 * the supported shape once, then matching uses equality, prefix/suffix tests,
 * or a bounded substring search without interpreting other syntax.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public final class FieldPattern {
    private enum Shape { EXACT, PREFIX, SUFFIX, CONTAINS, PREFIX_SUFFIX, ANY }

    private final Shape shape;
    private final String first;
    private final String second;

    private FieldPattern(Shape shape, String first, String second) {
        this.shape = shape;
        this.first = first;
        this.second = second;
    }

    /**
     * Validates and classifies a raw field pattern without changing its spelling.
     *
     * @param pattern raw pattern in the accepted six-form wildcard grammar
     * @param field pattern field kind used for failure metadata
     * @param operation operation used for failure metadata
     * @param index entry position used for failure metadata, or {@code null}
     * @return an immutable classified description
     * @throws NamingRuleViolation when the pattern is missing, malformed, unsupported or oversized
     */
    public static FieldPattern parse(
            String pattern,
            NamingFieldKind field,
            NamingOperation operation,
            Integer index) {
        if (pattern == null) {
            throw new NamingRuleViolation(NamingFailureCategory.MISSING_INPUT,
                    operation, field, index);
        }
        int length = TopicFieldRules.codePointCount(pattern, operation, field, index,
                NamingFailureCategory.MISSING_INPUT);
        TopicFieldRules.checkLimit(length, NamingLimits.MAX_PATTERN_CODE_POINTS,
                operation, field, index);

        StringBuilder literal = new StringBuilder(pattern.length());
        int[] stars = new int[2];
        int starCount = 0;
        for (int offset = 0; offset < pattern.length(); offset++) {
            char current = pattern.charAt(offset);
            if (current == '\\' && offset + 1 < pattern.length()
                    && (pattern.charAt(offset + 1) == '*' || pattern.charAt(offset + 1) == '\\')) {
                literal.append(pattern.charAt(++offset));
            } else if (current == '*') {
                if (starCount == stars.length) {
                    throw rejected(operation, field, index);
                }
                stars[starCount++] = literal.length();
            } else {
                literal.append(current);
            }
        }

        String decoded = literal.toString();
        if (starCount == 0) {
            return new FieldPattern(Shape.EXACT, decoded, null);
        }
        if (starCount == 1) {
            int position = stars[0];
            if (decoded.isEmpty()) {
                return new FieldPattern(Shape.ANY, "", null);
            }
            if (position == 0) {
                return new FieldPattern(Shape.SUFFIX, decoded, null);
            }
            if (position == decoded.length()) {
                return new FieldPattern(Shape.PREFIX, decoded, null);
            }
            return new FieldPattern(Shape.PREFIX_SUFFIX,
                    decoded.substring(0, position), decoded.substring(position));
        }
        if (stars[0] == 0 && stars[1] == decoded.length() && !decoded.isEmpty()) {
            return new FieldPattern(Shape.CONTAINS, decoded, null);
        }
        throw rejected(operation, field, index);
    }

    /**
     * Matches one complete decoded field using only the classified bounded operation.
     *
     * @param candidate complete, non-null candidate field
     * @return true when the field satisfies the classified shape
     */
    public boolean matches(String candidate) {
        return switch (shape) {
            case EXACT -> first.equals(candidate);
            case PREFIX -> candidate.startsWith(first);
            case SUFFIX -> candidate.endsWith(first);
            case CONTAINS -> candidate.indexOf(first) >= 0;
            case PREFIX_SUFFIX -> candidate.length() >= first.length() + second.length()
                    && candidate.startsWith(first) && candidate.endsWith(second);
            case ANY -> true;
        };
    }

    /**
     * Escapes only the pattern metacharacters required by a literal-field helper.
     *
     * @param literal validated literal field text
     * @return pattern text that matches exactly the literal
     */
    public static String escapeLiteral(String literal) {
        StringBuilder escaped = new StringBuilder(literal.length());
        for (int offset = 0; offset < literal.length(); offset++) {
            char current = literal.charAt(offset);
            if (current == '\\' || current == '*') {
                escaped.append('\\');
            }
            escaped.append(current);
        }
        return escaped.toString();
    }

    /** Returns a payload-free rejection for unsupported wildcard placement. */
    private static NamingRuleViolation rejected(
            NamingOperation operation,
            NamingFieldKind field,
            Integer index) {
        return new NamingRuleViolation(NamingFailureCategory.PATTERN_REJECTED,
                operation, field, index);
    }
}