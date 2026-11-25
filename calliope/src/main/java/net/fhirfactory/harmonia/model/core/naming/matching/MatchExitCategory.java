/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.matching;

/**
 * Bounded outcome categories for one TopicMask evaluation.
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public enum MatchExitCategory {
    /** The candidate was {@code null}. */
    NULL_CANDIDATE,
    /** The candidate depth did not satisfy the exact or containment depth rule. */
    DEPTH_MISMATCH,
    /** A qualifier did not satisfy its pattern. */
    QUALIFIER_MISMATCH,
    /** A value did not satisfy its pattern. */
    VALUE_MISMATCH,
    /** A version constraint was supplied but the candidate entry had no version. */
    REQUIRED_VERSION_ABSENT,
    /** A present version did not satisfy its constraint. */
    VERSION_MISMATCH,
    /** Every ordinary positional check succeeded. */
    MATCH,
    /** An unexpected failure occurred and the result was fixed as a non-match. */
    EVALUATION_ERROR,
    /** The explicit universal mask matched a non-null candidate. */
    UNIVERSAL_MATCH
}