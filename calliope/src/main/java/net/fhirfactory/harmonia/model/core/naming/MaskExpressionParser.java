/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.ArrayList;
import java.util.List;

/**
 * Strictly reconstructs the outer canonical syntax of an ordinary TopicMask.
 *
 * <p>Only delimiters and the four outer escapes are interpreted here. The
 * decoded expressions are then independently validated and compiled by
 * {@link TopicMaskEntry}.</p>
 */
final class MaskExpressionParser {

    private MaskExpressionParser() {
        throw new AssertionError("No instances");
    }

    /** Parses a bounded expression path and preserves the separate containment flag. */
    static TopicMask parse(String expression, boolean includeContainedElements) {
        NamingOperation operation = NamingOperation.MASK_PARSE;
        List<String> segments = CanonicalPathSyntax.splitEntries(expression,
                NamingLimits.MAX_MASK_CANONICAL_UTF8_BYTES, operation);
        if (segments.size() > NamingLimits.MAX_DEPTH) {
            throw TopicFieldRules.failure(NamingFailureCategory.LIMIT_EXCEEDED,
                    operation, NamingFieldKind.DEPTH, null);
        }
        List<TopicMaskEntry> entries = new ArrayList<>(segments.size());
        for (int index = 0; index < segments.size(); index++) {
            CanonicalPathSyntax.RawEntry raw = CanonicalPathSyntax.splitFields(
                    segments.get(index), true, true, operation, index);
            String qualifier = CanonicalPathSyntax.decode(raw.left(), operation,
                    NamingFieldKind.QUALIFIER_PATTERN, index);
            String value = CanonicalPathSyntax.decode(raw.right(), operation,
                    NamingFieldKind.VALUE_PATTERN, index);
            String version = raw.hasMiddle()
                    ? CanonicalPathSyntax.decode(raw.middle(), operation,
                            NamingFieldKind.VERSION_CONSTRAINT, index)
                    : null;
            entries.add(TopicMaskEntry.create(qualifier, value, version, operation, index));
        }
        return TopicMask.fromEntries(entries, includeContainedElements);
    }
}