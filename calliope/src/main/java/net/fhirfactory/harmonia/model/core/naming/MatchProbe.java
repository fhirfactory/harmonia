/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

/** Internal counter and failure-injection seam for bounded matching tests. */
interface MatchProbe {
    MatchProbe DIRECT = new MatchProbe() {
        @Override
        public boolean matchesQualifier(TopicMaskEntry entry, String candidate) {
            return entry.matchesQualifier(candidate);
        }

        @Override
        public boolean matchesValue(TopicMaskEntry entry, String candidate) {
            return entry.matchesValue(candidate);
        }

        @Override
        public boolean matchesVersion(TopicMaskEntry entry, String candidate) {
            return entry.matchesVersion(candidate);
        }
    };

    boolean matchesQualifier(TopicMaskEntry entry, String candidate);

    boolean matchesValue(TopicMaskEntry entry, String candidate);

    boolean matchesVersion(TopicMaskEntry entry, String candidate);
}