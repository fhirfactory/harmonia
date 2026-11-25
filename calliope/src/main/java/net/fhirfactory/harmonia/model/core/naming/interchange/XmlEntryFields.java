/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.interchange;

/**
 * Text fields of one XML entry record, independent of the Naming models.
 *
 * <p>For a Topic record these are the qualifier, value and optional version
 * element texts. For an ordinary TopicMask record they are the decoded raw
 * qualifier pattern, value pattern and optional version constraint. The
 * owning model validates the fields as a completed entry; the XML format only
 * guarantees structure, encoding and bounds.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 *
 * @param qualifierField qualifier or raw qualifier pattern
 * @param valueField value or raw value pattern
 * @param versionField version or raw constraint; {@code null} when the element is absent
 */
public record XmlEntryFields(String qualifierField, String valueField, String versionField) {
}
