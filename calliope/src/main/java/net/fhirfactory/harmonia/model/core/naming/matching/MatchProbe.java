/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.matching;

/**
 * Check seam through which the evaluator applies each compiled field check.
 *
 * <p>Production evaluation always uses {@link #DIRECT}. Tests may supply a
 * counting or deliberately failing probe to verify ordering, short-circuit
 * behaviour, the 48-check work bound and fail-closed handling of unexpected
 * failures. The seam is reachable only through the TopicMask model's
 * package-private evaluation overload; no supported operation accepts it.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public interface MatchProbe {

    /** Applies each check directly to the compiled criterion. */
    MatchProbe DIRECT = new MatchProbe() {
        @Override
        public boolean matchesQualifier(EntryCriterion criterion, String candidate) {
            return criterion.matchesQualifier(candidate);
        }

        @Override
        public boolean matchesValue(EntryCriterion criterion, String candidate) {
            return criterion.matchesValue(candidate);
        }

        @Override
        public boolean matchesVersion(EntryCriterion criterion, String candidate) {
            return criterion.matchesVersion(candidate);
        }
    };

    /**
     * Applies the qualifier check.
     *
     * @param criterion compiled criterion
     * @param candidate candidate qualifier
     * @return the check result
     */
    boolean matchesQualifier(EntryCriterion criterion, String candidate);

    /**
     * Applies the value check.
     *
     * @param criterion compiled criterion
     * @param candidate candidate value
     * @return the check result
     */
    boolean matchesValue(EntryCriterion criterion, String candidate);

    /**
     * Applies the version check to a present candidate version.
     *
     * @param criterion compiled criterion with a supplied constraint
     * @param candidate present candidate version
     * @return the check result
     */
    boolean matchesVersion(EntryCriterion criterion, String candidate);
}
