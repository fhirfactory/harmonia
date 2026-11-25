/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

/**
 * Immutable, construction-validated interpretation of one raw version constraint.
 *
 * <p>Exact text and numeric comparisons remain distinct. Numeric operands are
 * component spans, so even long decimal components never pass through a
 * machine numeric type.</p>
 */
final class VersionConstraint {
    private enum Kind { EMPTY, EXACT, COMPARISON, RANGE }
    private enum Operator { EQUAL, LESS, LESS_OR_EQUAL, GREATER, GREATER_OR_EQUAL }

    private final Kind kind;
    private final String exact;
    private final Operator firstOperator;
    private final NumericVersion firstOperand;
    private final Operator secondOperator;
    private final NumericVersion secondOperand;

    private VersionConstraint(
            Kind kind,
            String exact,
            Operator firstOperator,
            NumericVersion firstOperand,
            Operator secondOperator,
            NumericVersion secondOperand) {
        this.kind = kind;
        this.exact = exact;
        this.firstOperator = firstOperator;
        this.firstOperand = firstOperand;
        this.secondOperator = secondOperator;
        this.secondOperand = secondOperand;
    }

    /** Parses a present raw constraint; null is represented by the absence of this description. */
    static VersionConstraint parse(String raw, NamingOperation operation, Integer index) {
        if (raw == null) {
            return null;
        }
        int rawLength = TopicFieldRules.codePointCount(raw, operation,
                NamingFieldKind.VERSION_CONSTRAINT, index, NamingFailureCategory.MISSING_INPUT);
        if (rawLength == 0) {
            return new VersionConstraint(Kind.EMPTY, null, null, null, null, null);
        }
        if (startsWithOperator(raw.charAt(0))) {
            return parseNumeric(raw, operation, index);
        }
        if (raw.charAt(0) == '\\') {
            if (raw.length() < 2 || !startsWithOperatorOrBackslash(raw.charAt(1))) {
                throw rejected(operation, index);
            }
            if (rawLength > NamingLimits.MAX_EXACT_CONSTRAINT_RAW_CHARS) {
                throw limit(operation, index);
            }
            String exactText = raw.substring(1);
            TopicFieldRules.validateConstraintVersion(exactText, operation, index);
            return new VersionConstraint(Kind.EXACT, exactText, null, null, null, null);
        }
        if (rawLength > NamingLimits.MAX_VERSION_CODE_POINTS) {
            throw limit(operation, index);
        }
        TopicFieldRules.validateConstraintVersion(raw, operation, index);
        return new VersionConstraint(Kind.EXACT, raw, null, null, null, null);
    }

    /** Checks whether one candidate version satisfies this already classified constraint. */
    boolean matches(String candidate) {
        if (candidate == null || kind == Kind.EMPTY) {
            return false;
        }
        if (kind == Kind.EXACT) {
            return exact.equals(candidate);
        }
        NumericVersion candidateVersion = NumericVersion.parse(candidate);
        if (candidateVersion == null) {
            return false;
        }
        if (!matches(candidateVersion, firstOperator, firstOperand)) {
            return false;
        }
        return kind != Kind.RANGE || matches(candidateVersion, secondOperator, secondOperand);
    }

    /** Parses one or two numeric comparisons and validates lower/upper endpoint ordering. */
    private static VersionConstraint parseNumeric(
            String raw,
            NamingOperation operation,
            Integer index) {
        int comma = raw.indexOf(',');
        if (raw.length() > (comma < 0 ? NamingLimits.MAX_SINGLE_CONSTRAINT_RAW_CHARS
                : NamingLimits.MAX_RANGE_CONSTRAINT_RAW_CHARS)) {
            throw limit(operation, index);
        }
        if (comma < 0) {
            Comparison comparison = parseComparison(raw, false, operation, index);
            return new VersionConstraint(Kind.COMPARISON, null, comparison.operator,
                    comparison.operand, null, null);
        }
        if (comma == 0 || comma == raw.length() - 1 || raw.indexOf(',', comma + 1) >= 0) {
            throw rejected(operation, index);
        }
        Comparison lower = parseComparison(raw.substring(0, comma), true, operation, index);
        Comparison upper = parseComparison(raw.substring(comma + 1), true, operation, index);
        if ((lower.operator != Operator.GREATER && lower.operator != Operator.GREATER_OR_EQUAL)
                || (upper.operator != Operator.LESS && upper.operator != Operator.LESS_OR_EQUAL)) {
            throw rejected(operation, index);
        }
        int order = lower.operand.compareTo(upper.operand);
        if (order > 0 || (order == 0
                && (lower.operator != Operator.GREATER_OR_EQUAL
                || upper.operator != Operator.LESS_OR_EQUAL))) {
            throw rejected(operation, index);
        }
        return new VersionConstraint(Kind.RANGE, null, lower.operator, lower.operand,
                upper.operator, upper.operand);
    }

    /** Parses one operator followed by one bounded numeric operand. */
    private static Comparison parseComparison(
            String text,
            boolean range,
            NamingOperation operation,
            Integer index) {
        Operator operator;
        int operandStart;
        if (text.startsWith("<=")) {
            operator = Operator.LESS_OR_EQUAL;
            operandStart = 2;
        } else if (text.startsWith(">=")) {
            operator = Operator.GREATER_OR_EQUAL;
            operandStart = 2;
        } else if (text.startsWith("=")) {
            operator = Operator.EQUAL;
            operandStart = 1;
        } else if (text.startsWith("<")) {
            operator = Operator.LESS;
            operandStart = 1;
        } else if (text.startsWith(">")) {
            operator = Operator.GREATER;
            operandStart = 1;
        } else {
            throw rejected(operation, index);
        }
        if (range && operator == Operator.EQUAL) {
            throw rejected(operation, index);
        }
        String operandText = text.substring(operandStart);
        validateNumericOperandBounds(operandText, operation, index);
        NumericVersion operand = NumericVersion.parse(operandText);
        if (operand == null) {
            throw rejected(operation, index);
        }
        return new Comparison(operator, operand);
    }

    /** Enforces numeric operand and component bounds before allocating comparison spans. */
    private static void validateNumericOperandBounds(
            String operand,
            NamingOperation operation,
            Integer index) {
        if (operand.length() > NamingLimits.MAX_NUMERIC_VERSION_OPERAND_CHARS) {
            throw limit(operation, index);
        }
        int components = 1;
        for (int offset = 0; offset < operand.length(); offset++) {
            if (operand.charAt(offset) == '.') {
                components++;
            }
        }
        if (components > NamingLimits.MAX_NUMERIC_VERSION_COMPONENTS) {
            throw limit(operation, index);
        }
    }

    /** Applies a comparison operator to one numeric candidate and operand. */
    private static boolean matches(
            NumericVersion candidate,
            Operator operator,
            NumericVersion operand) {
        int comparison = candidate.compareTo(operand);
        return switch (operator) {
            case EQUAL -> comparison == 0;
            case LESS -> comparison < 0;
            case LESS_OR_EQUAL -> comparison <= 0;
            case GREATER -> comparison > 0;
            case GREATER_OR_EQUAL -> comparison >= 0;
        };
    }

    /** Identifies an operator-led numeric constraint. */
    private static boolean startsWithOperator(char value) {
        return value == '=' || value == '<' || value == '>';
    }

    /** Identifies a reserved character that may be escaped for exact text. */
    private static boolean startsWithOperatorOrBackslash(char value) {
        return startsWithOperator(value) || value == '\\';
    }

    /** Creates payload-free malformed constraint metadata. */
    private static NamingValidationException rejected(NamingOperation operation, Integer index) {
        return TopicFieldRules.failure(NamingFailureCategory.INVALID_FIELD,
                operation, NamingFieldKind.VERSION_CONSTRAINT, index);
    }

    /** Creates payload-free constraint-limit metadata. */
    private static NamingValidationException limit(NamingOperation operation, Integer index) {
        return TopicFieldRules.failure(NamingFailureCategory.LIMIT_EXCEEDED,
                operation, NamingFieldKind.VERSION_CONSTRAINT, index);
    }

    private record Comparison(Operator operator, NumericVersion operand) {
    }
}