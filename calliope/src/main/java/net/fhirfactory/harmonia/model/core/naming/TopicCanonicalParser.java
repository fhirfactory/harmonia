/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements strict reconstruction from the two accepted Topic canonical forms.
 *
 * <p>Literal completed-field validation remains in {@link TopicEntry}; this
 * parser never applies builder-generated qualifier defaults.</p>
 */
final class TopicCanonicalParser {

    private TopicCanonicalParser() {
        throw new AssertionError("No instances");
    }

    /** Parses one bounded path using the selected strict version grammar. */
    static Topic parse(String text, boolean withVersion) {
        NamingOperation operation = NamingOperation.TOPIC_PARSE;
        List<String> segments = CanonicalPathSyntax.splitEntries(text,
                NamingLimits.MAX_TOPIC_CANONICAL_UTF8_BYTES, operation);
        if (segments.size() > NamingLimits.MAX_DEPTH) {
            throw TopicFieldRules.failure(NamingFailureCategory.LIMIT_EXCEEDED,
                    operation, NamingFieldKind.DEPTH, null);
        }
        List<TopicEntry> entries = new ArrayList<>(segments.size());
        for (int index = 0; index < segments.size(); index++) {
            CanonicalPathSyntax.RawEntry raw = CanonicalPathSyntax.splitFields(
                    segments.get(index), withVersion, false, operation, index);
            String qualifier = CanonicalPathSyntax.decode(raw.left(), operation,
                    NamingFieldKind.QUALIFIER, index);
            String value = CanonicalPathSyntax.decode(raw.right(), operation,
                    NamingFieldKind.VALUE, index);
            String version = null;
            if (raw.hasMiddle()) {
                version = CanonicalPathSyntax.decode(raw.middle(), operation,
                        NamingFieldKind.VERSION, index);
                if (version.isEmpty()) {
                    throw TopicFieldRules.failure(NamingFailureCategory.MALFORMED_TEXT,
                            operation, NamingFieldKind.VERSION, index);
                }
            }
            if (qualifier.isEmpty() || value.isEmpty()) {
                throw TopicFieldRules.failure(NamingFailureCategory.MALFORMED_TEXT,
                        operation, qualifier.isEmpty() ? NamingFieldKind.QUALIFIER : NamingFieldKind.VALUE,
                        index);
            }
            entries.add(TopicEntry.create(qualifier, value, version, operation, index));
        }
        return Topic.fromEntries(entries);
    }
}