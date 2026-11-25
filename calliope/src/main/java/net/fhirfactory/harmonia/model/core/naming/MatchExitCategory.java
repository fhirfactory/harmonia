/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

/** Internal bounded outcome categories for one TopicMask evaluation. */
enum MatchExitCategory {
    NULL_CANDIDATE,
    DEPTH_MISMATCH,
    QUALIFIER_MISMATCH,
    VALUE_MISMATCH,
    REQUIRED_VERSION_ABSENT,
    VERSION_MISMATCH,
    MATCH,
    EVALUATION_ERROR,
    UNIVERSAL_MATCH
}