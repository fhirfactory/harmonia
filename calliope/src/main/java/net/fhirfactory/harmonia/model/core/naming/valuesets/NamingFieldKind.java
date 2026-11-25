/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.valuesets;

/** Identifies a Naming field without retaining or exposing its value. */
public enum NamingFieldKind {
    /** A completed qualifier value. */
    QUALIFIER,
    /** A completed Topic value. */
    VALUE,
    /** An opaque completed Topic version. */
    VERSION,
    /** A raw qualifier pattern. */
    QUALIFIER_PATTERN,
    /** A raw value pattern. */
    VALUE_PATTERN,
    /** A raw optional version constraint. */
    VERSION_CONSTRAINT,
    /** A strict Topic or TopicMask canonical representation. */
    CANONICAL_TEXT,
    /** An XML record, attribute, element, or encoded field. */
    XML_RECORD,
    /** A completed path or mask depth. */
    DEPTH,
    /** A caller-supplied zero-based position or index field. */
    INDEX,
    /** No individual field applies to the rejected operation. */
    NONE
}