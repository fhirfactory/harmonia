/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.canonical;

import java.util.ArrayList;
import java.util.List;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * Strict syntactic reader for the two accepted Topic canonical forms.
 *
 * <p>The parser enforces the encoded-size and depth bounds before allocating
 * entries, splits on unescaped delimiters, decodes exactly one escape layer
 * and rejects empty completed fields. It never generates qualifier defaults
 * and never constructs a Topic: completed-field validation and assembly stay
 * with the Topic model, which receives the decoded fields in path order.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public final class TopicCanonicalParser {

    /** Prevents construction of this stateless parser. */
    private TopicCanonicalParser() {
        throw new AssertionError("No instances");
    }

    /**
     * Parses one bounded Topic path using the selected strict version grammar.
     *
     * @param text canonical text; {@code null} is rejected as missing input
     * @param withVersion whether the version-capable form is being read
     * @return decoded, nonempty entry fields in path order
     * @throws NamingRuleViolation when text is absent, oversized, too deep or malformed
     */
    public static List<CanonicalEntryFields> parse(String text, boolean withVersion) {
        NamingOperation operation = NamingOperation.TOPIC_PARSE;
        List<String> segments = CanonicalPathSyntax.splitEntries(text,
                NamingLimits.MAX_TOPIC_CANONICAL_UTF8_BYTES, operation);
        List<CanonicalEntryFields> entries = new ArrayList<>(segments.size());
        for (int index = 0; index < segments.size(); index++) {
            CanonicalPathSyntax.RawEntry raw = CanonicalPathSyntax.splitFields(
                    segments.get(index), withVersion, operation, index);
            String qualifier = CanonicalPathSyntax.decode(raw.left(), operation,
                    NamingFieldKind.QUALIFIER, index);
            String value = CanonicalPathSyntax.decode(raw.right(), operation,
                    NamingFieldKind.VALUE, index);
            String version = null;
            if (raw.hasMiddle()) {
                version = CanonicalPathSyntax.decode(raw.middle(), operation,
                        NamingFieldKind.VERSION, index);
                if (version.isEmpty()) {
                    throw new NamingRuleViolation(NamingFailureCategory.MALFORMED_TEXT,
                            operation, NamingFieldKind.VERSION, index);
                }
            }
            if (qualifier.isEmpty() || value.isEmpty()) {
                throw new NamingRuleViolation(NamingFailureCategory.MALFORMED_TEXT,
                        operation, qualifier.isEmpty() ? NamingFieldKind.QUALIFIER : NamingFieldKind.VALUE,
                        index);
            }
            entries.add(new CanonicalEntryFields(qualifier, version, value));
        }
        return List.copyOf(entries);
    }
}
