/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.interchange;

import java.util.List;

/**
 * Structure of one TopicMask XML record, independent of the TopicMask model.
 *
 * <p>The explicit universal kind carries no containment flag and no entries.
 * An ordinary record carries its containment flag and ordered criterion
 * fields. Keeping the kind explicit ensures a universal mask is never
 * inferred from ordinary content.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 *
 * @param universal whether the record is of the explicit universal kind
 * @param includeContainedElements ordinary containment flag; {@code false} for universal records
 * @param entries ordered ordinary criterion fields; empty for universal records
 */
public record XmlMaskRecord(boolean universal, boolean includeContainedElements, List<XmlEntryFields> entries) {

    /** Copies the entry list so a record never shares mutable state with its creator. */
    public XmlMaskRecord {
        entries = List.copyOf(entries);
    }

    /**
     * Returns the explicit universal record.
     *
     * @return a universal record without entries
     */
    public static XmlMaskRecord universalRecord() {
        return new XmlMaskRecord(true, false, List.of());
    }

    /**
     * Returns an ordinary record.
     *
     * @param includeContainedElements containment flag
     * @param entries ordered criterion fields
     * @return an ordinary record
     */
    public static XmlMaskRecord ordinaryRecord(boolean includeContainedElements, List<XmlEntryFields> entries) {
        return new XmlMaskRecord(false, includeContainedElements, entries);
    }
}
