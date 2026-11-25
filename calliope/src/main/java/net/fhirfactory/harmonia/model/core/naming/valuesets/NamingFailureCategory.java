/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.valuesets;

/** Stable, payload-free categories for rejected Naming operations. */
public enum NamingFailureCategory {
    /** A required caller-supplied field was absent. */
    MISSING_INPUT,
    /** A caller argument is outside its permitted structural domain. */
    INVALID_ARGUMENT,
    /** A supplied completed field violates its field contract. */
    INVALID_FIELD,
    /** Canonical text is malformed under the strict grammar. */
    MALFORMED_TEXT,
    /** An XML interchange record is malformed or unsupported. */
    MALFORMED_RECORD,
    /** A pattern uses a wildcard arrangement outside the accepted forms. */
    PATTERN_REJECTED,
    /** A fixed depth, length, byte, operand, or scan bound was exceeded. */
    LIMIT_EXCEEDED,
    /** A builder operation conflicts with its selected construction mode. */
    INVALID_STATE
}