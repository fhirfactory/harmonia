/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.ArrayList;
import java.util.List;
import net.fhirfactory.harmonia.model.core.naming.interchange.NamingXmlFormat;
import net.fhirfactory.harmonia.model.core.naming.interchange.XmlEntryFields;
import net.fhirfactory.harmonia.model.core.naming.interchange.XmlMaskRecord;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * Stateless, deterministic UTF-8 XML interchange for complete Topics and TopicMasks.
 *
 * <p>Topic entries retain order and optional versions. Mask records explicitly
 * distinguish universal from ordinary masks, preserve containment and
 * criterion order, and use padded UTF-8 Base64 for raw pattern and constraint
 * text. Input parsing is hardened and bounded before model construction.</p>
 *
 * <p>The codec is the supported facade over the interchange package. The
 * record format, hardened parsing, encodings and bounds belong to
 * {@code NamingXmlFormat}; this class maps between the models and that
 * format's neutral records and applies the models' strict completed-entry
 * validation with the actual XML operation and entry index. Reconstruction
 * never applies builder defaults such as generated qualifiers.</p>
 */
public final class NamingXmlCodec {

    private NamingXmlCodec() {
        throw new AssertionError("No instances");
    }

    /**
     * Writes a bounded deterministic UTF-8 Topic record.
     *
     * @param topic complete immutable Topic to encode
     * @return canonical UTF-8 XML bytes, without a byte-order mark
     * @throws NamingValidationException when the Topic is absent or output exceeds the XML record limit
     */
    public static byte[] writeTopic(Topic topic) {
        if (topic == null) {
            throw NamingValidationException.of(NamingFailureCategory.MISSING_INPUT,
                    NamingOperation.TOPIC_XML_WRITE, NamingFieldKind.NONE, null);
        }
        List<XmlEntryFields> fields = new ArrayList<>(topic.getDepth());
        for (TopicEntry entry : topic.getEntries()) {
            fields.add(new XmlEntryFields(entry.getQualifier(), entry.getValue(), entry.versionOrNull()));
        }
        return NamingBoundary.guard(() -> NamingXmlFormat.writeTopic(fields));
    }

    /**
     * Reads a strict complete Topic record without applying builder defaults.
     *
     * @param record UTF-8 XML bytes within the 256 KiB record limit
     * @return an immutable Topic preserving the ordered completed entries
     * @throws NamingValidationException when bytes, XML structure, encoding, or completed fields are invalid
     */
    public static Topic readTopic(byte[] record) {
        List<XmlEntryFields> fields = NamingBoundary.guard(() -> NamingXmlFormat.readTopic(record));
        List<TopicEntry> entries = new ArrayList<>(fields.size());
        for (int index = 0; index < fields.size(); index++) {
            XmlEntryFields field = fields.get(index);
            entries.add(TopicEntry.create(field.qualifierField(), field.valueField(), field.versionField(),
                    NamingOperation.TOPIC_XML_READ, index));
        }
        return Topic.fromEntries(entries);
    }

    /**
     * Writes an ordinary or explicit universal TopicMask as bounded deterministic XML.
     *
     * <p>Ordinary masks retain kind, containment, criterion order, raw fields,
     * and constraint presence. Universal masks use their separate explicit kind.</p>
     *
     * @param mask immutable mask to encode
     * @return canonical UTF-8 XML bytes with padded Base64 for raw ordinary fields
     * @throws NamingValidationException when the mask is absent or output exceeds the XML record limit
     */
    public static byte[] writeTopicMask(TopicMask mask) {
        if (mask == null) {
            throw NamingValidationException.of(NamingFailureCategory.MISSING_INPUT,
                    NamingOperation.MASK_XML_WRITE, NamingFieldKind.NONE, null);
        }
        XmlMaskRecord record;
        if (mask.isUniversal()) {
            record = XmlMaskRecord.universalRecord();
        } else {
            List<XmlEntryFields> fields = new ArrayList<>(mask.getEntries().size());
            for (TopicMaskEntry entry : mask.getEntries()) {
                fields.add(new XmlEntryFields(entry.getQualifierPattern(), entry.getValuePattern(),
                        entry.getVersionConstraint().orElse(null)));
            }
            record = XmlMaskRecord.ordinaryRecord(mask.isIncludeContainedElements(), fields);
        }
        return NamingBoundary.guard(() -> NamingXmlFormat.writeTopicMask(record));
    }

    /**
     * Reads strict ordinary or explicitly universal mask interchange.
     *
     * <p>The reader validates XML structure and strict UTF-8 Base64 before it
     * classifies patterns and version constraints. It rejects DTDs, external
     * resolution, unsupported ordering, duplicate fields and unexpected content.</p>
     *
     * @param record UTF-8 XML bytes within the 256 KiB record limit
     * @return an immutable ordinary or explicit universal TopicMask
     * @throws NamingValidationException when bytes, XML structure, encoding, or decoded criteria are invalid
     */
    public static TopicMask readTopicMask(byte[] record) {
        XmlMaskRecord parsed = NamingBoundary.guard(() -> NamingXmlFormat.readTopicMask(record));
        if (parsed.universal()) {
            return TopicMask.all();
        }
        List<TopicMaskEntry> entries = new ArrayList<>(parsed.entries().size());
        for (int index = 0; index < parsed.entries().size(); index++) {
            XmlEntryFields field = parsed.entries().get(index);
            entries.add(TopicMaskEntry.create(field.qualifierField(), field.valueField(), field.versionField(),
                    NamingOperation.MASK_XML_READ, index));
        }
        return TopicMask.fromEntries(entries, parsed.includeContainedElements());
    }
}
