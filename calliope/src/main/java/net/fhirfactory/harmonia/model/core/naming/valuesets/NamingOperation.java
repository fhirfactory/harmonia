/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.valuesets;

/** Identifies the bounded public operation associated with safe validation metadata. */
public enum NamingOperation {
    /** Sequential or indexed Topic construction and builder validation. */
    TOPIC_BUILD,
    /** Strict reconstruction from a Topic canonical form. */
    TOPIC_PARSE,
    /** Parent, ancestor, or indexed Topic navigation. */
    TOPIC_NAVIGATE,
    /** Ordinary mask builder operations. */
    MASK_BUILD,
    /** Direct construction of one standalone mask criterion. */
    MASK_ENTRY_BUILD,
    /** Strict reconstruction from an ordinary mask canonical expression. */
    MASK_PARSE,
    /** Read a Topic XML record. */
    TOPIC_XML_READ,
    /** Write a Topic XML record. */
    TOPIC_XML_WRITE,
    /** Read a TopicMask XML record. */
    MASK_XML_READ,
    /** Write a TopicMask XML record. */
    MASK_XML_WRITE
}