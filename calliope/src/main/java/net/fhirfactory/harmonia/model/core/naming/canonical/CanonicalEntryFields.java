/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.canonical;

/**
 * Decoded fields of one canonical path entry, before model-specific validation.
 *
 * <p>The canonical parsers remove exactly one outer escape layer and return
 * these neutral field texts in path order. For a Topic they are a qualifier,
 * optional version and value; for a TopicMask they are a qualifier pattern,
 * optional raw version constraint and value pattern. The owning model decides
 * what the text means and validates it as a completed entry.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 *
 * @param qualifierField decoded qualifier or qualifier pattern
 * @param versionField decoded version or constraint; {@code null} when the
 *     separator was absent and {@code ""} when present-empty
 * @param valueField decoded value or value pattern
 */
public record CanonicalEntryFields(String qualifierField, String versionField, String valueField) {
}
