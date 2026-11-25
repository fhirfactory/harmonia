/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.canonical;

import java.util.ArrayList;
import java.util.List;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * Strict syntactic reader for the outer canonical expression of an ordinary TopicMask.
 *
 * <p>Only delimiters and the four outer escapes are interpreted here. Empty
 * patterns are syntactically permitted, and a present-empty version
 * constraint is preserved as {@code ""} rather than collapsed into omission.
 * Wildcard and constraint semantics are validated afterwards, when the
 * TopicMask model compiles each decoded entry.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public final class MaskExpressionParser {

    /** Prevents construction of this stateless parser. */
    private MaskExpressionParser() {
        throw new AssertionError("No instances");
    }

    /**
     * Parses a bounded expression path into decoded criterion fields.
     *
     * @param expression canonical mask expression; {@code null} is rejected as missing input
     * @return decoded criterion fields in path order, preserving constraint presence
     * @throws NamingRuleViolation when the expression is absent, oversized, too deep or malformed
     */
    public static List<CanonicalEntryFields> parse(String expression) {
        NamingOperation operation = NamingOperation.MASK_PARSE;
        List<String> segments = CanonicalPathSyntax.splitEntries(expression,
                NamingLimits.MAX_MASK_CANONICAL_UTF8_BYTES, operation);
        List<CanonicalEntryFields> entries = new ArrayList<>(segments.size());
        for (int index = 0; index < segments.size(); index++) {
            CanonicalPathSyntax.RawEntry raw = CanonicalPathSyntax.splitFields(
                    segments.get(index), true, operation, index);
            String qualifier = CanonicalPathSyntax.decode(raw.left(), operation,
                    NamingFieldKind.QUALIFIER_PATTERN, index);
            String value = CanonicalPathSyntax.decode(raw.right(), operation,
                    NamingFieldKind.VALUE_PATTERN, index);
            String version = raw.hasMiddle()
                    ? CanonicalPathSyntax.decode(raw.middle(), operation,
                            NamingFieldKind.VERSION_CONSTRAINT, index)
                    : null;
            entries.add(new CanonicalEntryFields(qualifier, version, value));
        }
        return List.copyOf(entries);
    }
}
