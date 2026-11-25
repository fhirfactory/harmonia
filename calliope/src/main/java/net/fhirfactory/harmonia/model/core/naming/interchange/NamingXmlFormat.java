/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.interchange;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Deterministic UTF-8 XML record format for Topics and TopicMasks.
 *
 * <p>This class owns every XML concern of Naming interchange: the element
 * vocabulary and order, deterministic output, padded UTF-8 Base64 for raw
 * mask fields, hardened parsing, structural validation and the 256 KiB record
 * bound. It works only on neutral {@link XmlEntryFields} and
 * {@link XmlMaskRecord} values. The supported {@code NamingXmlCodec} facade
 * maps those values to and from the models, which perform strict
 * completed-entry validation with the actual operation and entry index.</p>
 *
 * <p>Readers reject missing, duplicate, misordered and unexpected content,
 * DTDs, external resolution, conflicting encodings and malformed UTF-8 or
 * Base64 with payload-free {@link NamingRuleViolation} metadata. Writers emit
 * the same canonical layout for equal input.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public final class NamingXmlFormat {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    /** Prevents construction of this stateless format. */
    private NamingXmlFormat() {
        throw new AssertionError("No instances");
    }

    /**
     * Writes a bounded deterministic Topic record.
     *
     * @param entries ordered, already completed Topic entry fields
     * @return UTF-8 XML bytes without a byte-order mark
     * @throws NamingRuleViolation when the output exceeds the XML record limit
     */
    public static byte[] writeTopic(List<XmlEntryFields> entries) {
        StringBuilder xml = new StringBuilder(XML_HEADER).append("<topic>\n");
        for (XmlEntryFields entry : entries) {
            xml.append("  <entry>\n")
                    .append("    <qualifier>").append(escapeText(entry.qualifierField()))
                    .append("</qualifier>\n")
                    .append("    <value>").append(escapeText(entry.valueField())).append("</value>\n");
            if (entry.versionField() != null) {
                xml.append("    <version>").append(escapeText(entry.versionField())).append("</version>\n");
            }
            xml.append("  </entry>\n");
        }
        xml.append("</topic>");
        return XmlRecordParser.boundedBytes(xml.toString(), NamingOperation.TOPIC_XML_WRITE);
    }

    /**
     * Reads the structure of a strict Topic record.
     *
     * @param record UTF-8 XML bytes within the 256 KiB record limit
     * @return one to sixteen entry fields in record order; versions are {@code null} when absent
     * @throws NamingRuleViolation when bytes, XML structure or encoding are invalid
     */
    public static List<XmlEntryFields> readTopic(byte[] record) {
        NamingOperation operation = NamingOperation.TOPIC_XML_READ;
        Document document = XmlRecordParser.parse(record, operation);
        Element root = document.getDocumentElement();
        XmlRecordParser.requireName(root, "topic", operation);
        XmlRecordParser.requireAttributes(root, List.of(), operation);
        List<Element> entryElements = XmlRecordParser.childElements(root, operation,
                NamingLimits.MAX_DEPTH, true, null);
        if (entryElements.isEmpty()) {
            throw XmlRecordParser.failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, NamingFieldKind.DEPTH, null);
        }
        List<XmlEntryFields> entries = new ArrayList<>(entryElements.size());
        for (int index = 0; index < entryElements.size(); index++) {
            List<Element> fields = entryFields(entryElements.get(index), operation, index);
            XmlRecordParser.requireName(fields.get(0), "qualifier", operation, index);
            XmlRecordParser.requireName(fields.get(1), "value", operation, index);
            XmlRecordParser.requireAttributes(fields.get(0), List.of(), operation, index);
            XmlRecordParser.requireAttributes(fields.get(1), List.of(), operation, index);
            String qualifier = XmlRecordParser.simpleText(fields.get(0), operation, index);
            String value = XmlRecordParser.simpleText(fields.get(1), operation, index);
            String version = null;
            if (fields.size() == 3) {
                XmlRecordParser.requireName(fields.get(2), "version", operation, index);
                XmlRecordParser.requireAttributes(fields.get(2), List.of(), operation, index);
                version = XmlRecordParser.simpleText(fields.get(2), operation, index);
                if (version.isEmpty()) {
                    throw XmlRecordParser.failure(NamingFailureCategory.MALFORMED_RECORD,
                            operation, NamingFieldKind.VERSION, index);
                }
            }
            entries.add(new XmlEntryFields(qualifier, value, version));
        }
        return List.copyOf(entries);
    }

    /**
     * Writes an ordinary or explicit universal mask record.
     *
     * @param mask record structure to encode
     * @return UTF-8 XML bytes with padded Base64 for raw ordinary fields
     * @throws NamingRuleViolation when the output exceeds the XML record limit
     */
    public static byte[] writeTopicMask(XmlMaskRecord mask) {
        StringBuilder xml = new StringBuilder(XML_HEADER);
        if (mask.universal()) {
            xml.append("<topicMask kind=\"universal\"></topicMask>");
            return XmlRecordParser.boundedBytes(xml.toString(), NamingOperation.MASK_XML_WRITE);
        }
        xml.append("<topicMask kind=\"ordinary\" includeContainedElements=\"")
                .append(mask.includeContainedElements()).append("\">\n");
        for (XmlEntryFields entry : mask.entries()) {
            xml.append("  <entry>\n")
                    .append("    <qualifierPattern encoding=\"base64\">")
                    .append(encodeBase64(entry.qualifierField()))
                    .append("</qualifierPattern>\n")
                    .append("    <valuePattern encoding=\"base64\">")
                    .append(encodeBase64(entry.valueField())).append("</valuePattern>\n");
            if (entry.versionField() != null) {
                xml.append("    <versionConstraint encoding=\"base64\">")
                        .append(encodeBase64(entry.versionField())).append("</versionConstraint>\n");
            }
            xml.append("  </entry>\n");
        }
        xml.append("</topicMask>");
        return XmlRecordParser.boundedBytes(xml.toString(), NamingOperation.MASK_XML_WRITE);
    }

    /**
     * Reads the structure of a strict ordinary or explicitly universal mask record.
     *
     * @param record UTF-8 XML bytes within the 256 KiB record limit
     * @return the explicit record kind, containment flag and decoded raw criterion fields
     * @throws NamingRuleViolation when bytes, XML structure, Base64 or UTF-8 are invalid
     */
    public static XmlMaskRecord readTopicMask(byte[] record) {
        NamingOperation operation = NamingOperation.MASK_XML_READ;
        Document document = XmlRecordParser.parse(record, operation);
        Element root = document.getDocumentElement();
        XmlRecordParser.requireName(root, "topicMask", operation);
        String kind = XmlRecordParser.requiredAttribute(root, "kind", operation);
        if (kind.equals("universal")) {
            XmlRecordParser.requireAttributes(root, List.of("kind"), operation);
            if (!XmlRecordParser.childElements(root, operation, 0, false, null).isEmpty()) {
                throw XmlRecordParser.failure(NamingFailureCategory.MALFORMED_RECORD,
                        operation, NamingFieldKind.XML_RECORD, null);
            }
            return XmlMaskRecord.universalRecord();
        }
        if (!kind.equals("ordinary")) {
            throw XmlRecordParser.failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, NamingFieldKind.XML_RECORD, null);
        }
        XmlRecordParser.requireAttributes(root, List.of("kind", "includeContainedElements"), operation);
        boolean include = XmlRecordParser.parseBoolean(
                XmlRecordParser.requiredAttribute(root, "includeContainedElements", operation), operation);
        List<Element> entryElements = XmlRecordParser.childElements(root, operation,
                NamingLimits.MAX_DEPTH, true, null);
        if (entryElements.isEmpty()) {
            throw XmlRecordParser.failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, NamingFieldKind.DEPTH, null);
        }
        List<XmlEntryFields> entries = new ArrayList<>(entryElements.size());
        for (int index = 0; index < entryElements.size(); index++) {
            List<Element> fields = entryFields(entryElements.get(index), operation, index);
            XmlRecordParser.requireName(fields.get(0), "qualifierPattern", operation, index);
            XmlRecordParser.requireName(fields.get(1), "valuePattern", operation, index);
            String qualifier = XmlRecordParser.decodeBase64Element(fields.get(0), operation, index,
                    NamingLimits.MAX_PATTERN_UTF8_BYTES);
            String value = XmlRecordParser.decodeBase64Element(fields.get(1), operation, index,
                    NamingLimits.MAX_PATTERN_UTF8_BYTES);
            String version = null;
            if (fields.size() == 3) {
                XmlRecordParser.requireName(fields.get(2), "versionConstraint", operation, index);
                version = XmlRecordParser.decodeBase64Element(fields.get(2), operation, index,
                        NamingLimits.MAX_VERSION_CONSTRAINT_UTF8_BYTES);
            }
            entries.add(new XmlEntryFields(qualifier, value, version));
        }
        return XmlMaskRecord.ordinaryRecord(include, entries);
    }

    /** Validates one attribute-free entry element and returns its two or three field elements. */
    private static List<Element> entryFields(Element entry, NamingOperation operation, int index) {
        XmlRecordParser.requireName(entry, "entry", operation, index);
        XmlRecordParser.requireAttributes(entry, List.of(), operation, index);
        List<Element> fields = XmlRecordParser.childElements(entry, operation, 3, false, index);
        if (fields.size() < 2) {
            throw XmlRecordParser.failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, NamingFieldKind.XML_RECORD, index);
        }
        return fields;
    }

    /** Encodes original pattern or constraint text as padded UTF-8 Base64. */
    private static String encodeBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /** Escapes valid Topic text for deterministic XML element content. */
    private static String escapeText(String text) {
        StringBuilder escaped = new StringBuilder(text.length());
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            switch (current) {
                case '&' -> escaped.append("&amp;");
                case '<' -> escaped.append("&lt;");
                case '>' -> escaped.append("&gt;");
                default -> escaped.append(current);
            }
        }
        return escaped.toString();
    }
}
