/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Stateless, deterministic UTF-8 XML interchange for complete Topics and TopicMasks.
 *
 * <p>Topic entries retain order and optional versions. Mask records explicitly
 * distinguish universal from ordinary masks, preserve containment and
 * criterion order, and use padded UTF-8 Base64 for raw pattern and constraint
 * text. Input parsing is hardened and bounded before model construction.</p>
 */
public final class NamingXmlCodec {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

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
            throw TopicFieldRules.failure(NamingFailureCategory.MISSING_INPUT,
                    NamingOperation.TOPIC_XML_WRITE, NamingFieldKind.NONE, null);
        }
        StringBuilder xml = new StringBuilder(XML_HEADER).append("<topic>\n");
        for (TopicEntry entry : topic.getEntries()) {
            xml.append("  <entry>\n")
                    .append("    <qualifier>").append(escapeText(entry.getQualifier()))
                    .append("</qualifier>\n")
                    .append("    <value>").append(escapeText(entry.getValue())).append("</value>\n");
            entry.getVersion().ifPresent(version -> xml.append("    <version>")
                    .append(escapeText(version)).append("</version>\n"));
            xml.append("  </entry>\n");
        }
        xml.append("</topic>");
        return XmlRecordParser.boundedBytes(xml.toString(), NamingOperation.TOPIC_XML_WRITE);
    }

    /**
     * Reads a strict complete Topic record without applying builder defaults.
     *
     * @param record UTF-8 XML bytes within the 256 KiB record limit
     * @return an immutable Topic preserving the ordered completed entries
     * @throws NamingValidationException when bytes, XML structure, encoding, or completed fields are invalid
     */
    public static Topic readTopic(byte[] record) {
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
        List<TopicEntry> entries = new ArrayList<>(entryElements.size());
        for (int index = 0; index < entryElements.size(); index++) {
            Element entry = entryElements.get(index);
            XmlRecordParser.requireName(entry, "entry", operation, index);
            XmlRecordParser.requireAttributes(entry, List.of(), operation, index);
            List<Element> fields = XmlRecordParser.childElements(entry, operation, 3, false, index);
            if (fields.size() < 2 || fields.size() > 3) {
                throw XmlRecordParser.failure(NamingFailureCategory.MALFORMED_RECORD,
                        operation, NamingFieldKind.XML_RECORD, index);
            }
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
            entries.add(TopicEntry.create(qualifier, value, version, operation, index));
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
            throw TopicFieldRules.failure(NamingFailureCategory.MISSING_INPUT,
                    NamingOperation.MASK_XML_WRITE, NamingFieldKind.NONE, null);
        }
        StringBuilder xml = new StringBuilder(XML_HEADER);
        if (mask.isUniversal()) {
            xml.append("<topicMask kind=\"universal\"></topicMask>");
            return XmlRecordParser.boundedBytes(xml.toString(), NamingOperation.MASK_XML_WRITE);
        }
        xml.append("<topicMask kind=\"ordinary\" includeContainedElements=\"")
                .append(mask.isIncludeContainedElements()).append("\">\n");
        for (TopicMaskEntry entry : mask.getEntries()) {
            xml.append("  <entry>\n")
                    .append("    <qualifierPattern encoding=\"base64\">")
                    .append(encodeBase64(entry.getQualifierPattern()))
                    .append("</qualifierPattern>\n")
                    .append("    <valuePattern encoding=\"base64\">")
                    .append(encodeBase64(entry.getValuePattern())).append("</valuePattern>\n");
            entry.getVersionConstraint().ifPresent(version -> xml.append(
                    "    <versionConstraint encoding=\"base64\">")
                    .append(encodeBase64(version)).append("</versionConstraint>\n"));
            xml.append("  </entry>\n");
        }
        xml.append("</topicMask>");
        return XmlRecordParser.boundedBytes(xml.toString(), NamingOperation.MASK_XML_WRITE);
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
            return TopicMask.all();
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
        List<TopicMaskEntry> entries = new ArrayList<>(entryElements.size());
        for (int index = 0; index < entryElements.size(); index++) {
            Element entry = entryElements.get(index);
            XmlRecordParser.requireName(entry, "entry", operation, index);
            XmlRecordParser.requireAttributes(entry, List.of(), operation, index);
            List<Element> fields = XmlRecordParser.childElements(entry, operation, 3, false, index);
            if (fields.size() < 2 || fields.size() > 3) {
                throw XmlRecordParser.failure(NamingFailureCategory.MALFORMED_RECORD,
                        operation, NamingFieldKind.XML_RECORD, index);
            }
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
            entries.add(TopicMaskEntry.create(qualifier, value, version, operation, index));
        }
        return TopicMask.fromEntries(entries, include);
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