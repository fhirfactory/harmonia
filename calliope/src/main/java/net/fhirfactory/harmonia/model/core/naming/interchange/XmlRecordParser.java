/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.interchange;

import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Hardened XML parsing and bounded field helpers for Naming interchange.
 *
 * <p>All parser/provider details are mapped to payload-free validation metadata.
 * Only XML whitespace is accepted between structural elements, and external
 * resource access is disabled. The type is package-private: only
 * {@link NamingXmlFormat} reads records through it.</p>
 */
final class XmlRecordParser {
    private XmlRecordParser() {
        throw new AssertionError("No instances");
    }

    /** Enforces the record bound and parses strict UTF-8 with secure JDK XML settings. */
    static Document parse(byte[] record, NamingOperation operation) {
        String xml = decodeRecord(record, operation);
        validateXmlDeclaration(xml, operation);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setAttribute("http://www.oracle.com/xml/jaxp/properties/maxElementDepth", "4");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            var builder = factory.newDocumentBuilder();
            builder.setEntityResolver((publicId, systemId) -> {
                throw new SAXException("External entities are disabled");
            });
            builder.setErrorHandler(new StrictErrorHandler());
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            validateDocumentChildren(document, operation);
            return document;
        } catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException exception) {
            throw failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, NamingFieldKind.XML_RECORD, null);
        }
    }

    /** Collects bounded element children and accepts only XML whitespace around them. */
    static List<Element> childElements(
            Element parent,
            NamingOperation operation,
            int maximumChildren,
            boolean depthBound,
            Integer entryIndex) {
        NodeList children = parent.getChildNodes();
        List<Element> elements = new ArrayList<>();
        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (elements.size() >= maximumChildren) {
                    throw failure(depthBound ? NamingFailureCategory.LIMIT_EXCEEDED
                                    : NamingFailureCategory.MALFORMED_RECORD,
                            operation, depthBound ? NamingFieldKind.DEPTH : NamingFieldKind.XML_RECORD,
                            depthBound ? null : entryIndex);
                }
                elements.add((Element) child);
            } else if (child.getNodeType() == Node.TEXT_NODE && isXmlWhitespace(child.getNodeValue())) {
                continue;
            } else {
                throw failure(NamingFailureCategory.MALFORMED_RECORD,
                        operation, NamingFieldKind.XML_RECORD, entryIndex);
            }
        }
        return elements;
    }

    /** Reads text-only XML content without trimming or accepting nested content. */
    static String simpleText(
            Element element,
            NamingOperation operation,
            int index,
            NamingFieldKind fieldKind) {
        NodeList children = element.getChildNodes();
        StringBuilder text = new StringBuilder();
        for (int childIndex = 0; childIndex < children.getLength(); childIndex++) {
            Node child = children.item(childIndex);
            if (child.getNodeType() != Node.TEXT_NODE) {
                throw failure(NamingFailureCategory.MALFORMED_RECORD,
                        operation, fieldKind, index);
            }
            text.append(child.getNodeValue());
        }
        return text.toString();
    }

    /** Decodes canonical padded Base64 and strict UTF-8 under a field-specific byte bound. */
    static String decodeBase64Element(
            Element element,
            NamingOperation operation,
            int index,
            int maximumBytes,
            NamingFieldKind fieldKind) {
        requireAttributes(element, List.of("encoding"), operation, index, fieldKind);
        if (!requiredAttribute(element, "encoding", operation, index, fieldKind).equals("base64")) {
            throw failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, fieldKind, index);
        }
        String encoded = simpleText(element, operation, index, fieldKind);
        int maximumEncodedChars = 4 * ((maximumBytes + 2) / 3);
        if (encoded.length() > maximumEncodedChars) {
            throw failure(NamingFailureCategory.LIMIT_EXCEEDED,
                    operation, fieldKind, index);
        }
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(encoded);
        } catch (IllegalArgumentException exception) {
            throw failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, fieldKind, index);
        }
        if (decoded.length > maximumBytes) {
            throw failure(NamingFailureCategory.LIMIT_EXCEEDED,
                    operation, fieldKind, index);
        }
        if (!Base64.getEncoder().encodeToString(decoded).equals(encoded)) {
            throw failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, fieldKind, index);
        }
        try {
            return StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(decoded)).toString();
        } catch (CharacterCodingException exception) {
            throw failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, fieldKind, index);
        }
    }

    /** Reads a mandatory attribute without including its value in any failure. */
    static String requiredAttribute(Element element, String name, NamingOperation operation) {
        return requiredAttribute(element, name, operation, null);
    }

    /** Reads a mandatory entry attribute while retaining a known valid entry index. */
    static String requiredAttribute(
            Element element,
            String name,
            NamingOperation operation,
            Integer entryIndex) {
        return requiredAttribute(element, name, operation, entryIndex, NamingFieldKind.XML_RECORD);
    }

    /** Reads a mandatory attribute while retaining its containing field kind. */
    static String requiredAttribute(
            Element element,
            String name,
            NamingOperation operation,
            Integer entryIndex,
            NamingFieldKind fieldKind) {
        if (!element.hasAttribute(name)) {
            throw failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, fieldKind, entryIndex);
        }
        return element.getAttribute(name);
    }

    /** Rejects attributes outside the exact interchange contract. */
    static void requireAttributes(Element element, List<String> allowed, NamingOperation operation) {
        requireAttributes(element, allowed, operation, null);
    }

    /** Rejects attributes while retaining a known valid entry index. */
    static void requireAttributes(
            Element element,
            List<String> allowed,
            NamingOperation operation,
            Integer entryIndex) {
        requireAttributes(element, allowed, operation, entryIndex, NamingFieldKind.XML_RECORD);
    }

    /** Rejects unexpected attributes while preserving the field identified by its element name. */
    static void requireAttributes(
            Element element,
            List<String> allowed,
            NamingOperation operation,
            Integer entryIndex,
            NamingFieldKind fieldKind) {
        NamedNodeMap attributes = element.getAttributes();
        for (int index = 0; index < attributes.getLength(); index++) {
            if (!allowed.contains(attributes.item(index).getNodeName())) {
                throw failure(NamingFailureCategory.MALFORMED_RECORD,
                        operation, fieldKind, entryIndex);
            }
        }
    }

    /** Requires one exact unqualified element name. */
    static void requireName(Element element, String expected, NamingOperation operation) {
        requireName(element, expected, operation, null);
    }

    /** Requires one exact entry/field name with its actual operation context. */
    static void requireName(
            Element element,
            String expected,
            NamingOperation operation,
            Integer entryIndex) {
        if (element == null || !element.getTagName().equals(expected)) {
            throw failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, NamingFieldKind.XML_RECORD, entryIndex);
        }
    }

    /** Parses the two canonical lowercase containment literals. */
    static boolean parseBoolean(String value, NamingOperation operation) {
        if (value.equals("true")) {
            return true;
        }
        if (value.equals("false")) {
            return false;
        }
        throw failure(NamingFailureCategory.MALFORMED_RECORD,
                operation, NamingFieldKind.XML_RECORD, null);
    }

    /** Encodes and checks one deterministic UTF-8 XML record. */
    static byte[] boundedBytes(String xml, NamingOperation operation) {
        byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > NamingLimits.MAX_XML_RECORD_UTF8_BYTES) {
            throw failure(NamingFailureCategory.LIMIT_EXCEEDED,
                    operation, NamingFieldKind.XML_RECORD, null);
        }
        return bytes;
    }

    /** Creates a safe record-level validation result. */
    static NamingRuleViolation failure(
            NamingFailureCategory category,
            NamingOperation operation,
            NamingFieldKind field,
            Integer index) {
        return new NamingRuleViolation(category, operation, field, index);
    }

    /** Decodes bytes only after the record bound and rejects malformed UTF-8. */
    private static String decodeRecord(byte[] record, NamingOperation operation) {
        if (record == null) {
            throw failure(NamingFailureCategory.MISSING_INPUT,
                    operation, NamingFieldKind.XML_RECORD, null);
        }
        if (record.length > NamingLimits.MAX_XML_RECORD_UTF8_BYTES) {
            throw failure(NamingFailureCategory.LIMIT_EXCEEDED,
                    operation, NamingFieldKind.XML_RECORD, null);
        }
        try {
            String xml = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(record)).toString();
            return xml.startsWith("\ufeff") ? xml.substring(1) : xml;
        } catch (CharacterCodingException exception) {
            throw failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, NamingFieldKind.XML_RECORD, null);
        }
    }

    /** Validates the declared XML 1.0 version and UTF-8 encoding, when present. */
    private static void validateXmlDeclaration(String xml, NamingOperation operation) {
        if (!xml.startsWith("<?xml") || xml.length() <= 5 || !isXmlWhitespace(xml.charAt(5))) {
            return;
        }
        int end = xml.indexOf("?>", 5);
        if (end < 0 || end > 256) {
            throw failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, NamingFieldKind.XML_RECORD, null);
        }
        validateDeclaredVersion(xml, end, operation);
        for (int offset = 5; offset + 8 <= end; offset++) {
            if ((offset == 5 || isXmlWhitespace(xml.charAt(offset - 1)))
                    && xml.regionMatches(offset, "encoding", 0, 8)) {
                int cursor = skipXmlWhitespace(xml, offset + 8, end);
                if (cursor >= end || xml.charAt(cursor++) != '=') {
                    continue;
                }
                cursor = skipXmlWhitespace(xml, cursor, end);
                if (cursor >= end || (xml.charAt(cursor) != '\'' && xml.charAt(cursor) != '"')) {
                    continue;
                }
                char quote = xml.charAt(cursor++);
                int close = xml.indexOf(quote, cursor);
                if (close < 0 || close > end
                        || !xml.substring(cursor, close).equalsIgnoreCase("UTF-8")) {
                    throw failure(NamingFailureCategory.MALFORMED_RECORD,
                            operation, NamingFieldKind.XML_RECORD, null);
                }
                return;
            }
        }
    }

    /** Rejects a declared XML version other than the supported XML 1.0 version. */
    private static void validateDeclaredVersion(String xml, int end, NamingOperation operation) {
        for (int offset = 5; offset + 7 <= end; offset++) {
            if ((offset == 5 || isXmlWhitespace(xml.charAt(offset - 1)))
                    && xml.regionMatches(offset, "version", 0, 7)) {
                int cursor = offset + 7;
                if (cursor < end && !isXmlWhitespace(xml.charAt(cursor)) && xml.charAt(cursor) != '=') {
                    continue;
                }
                cursor = skipXmlWhitespace(xml, cursor, end);
                if (cursor >= end || xml.charAt(cursor++) != '=') {
                    continue;
                }
                cursor = skipXmlWhitespace(xml, cursor, end);
                if (cursor >= end || (xml.charAt(cursor) != '\'' && xml.charAt(cursor) != '"')) {
                    continue;
                }
                char quote = xml.charAt(cursor++);
                int close = xml.indexOf(quote, cursor);
                if (close < 0 || close > end) {
                    continue;
                }
                if (!xml.substring(cursor, close).equals("1.0")) {
                    throw failure(NamingFailureCategory.MALFORMED_RECORD,
                            operation, NamingFieldKind.XML_RECORD, null);
                }
                return;
            }
        }
    }

    /** Skips only XML's four structural whitespace characters. */
    private static int skipXmlWhitespace(String text, int offset, int end) {
        while (offset < end && isXmlWhitespace(text.charAt(offset))) {
            offset++;
        }
        return offset;
    }

    /** Checks whether one string contains only XML structural whitespace. */
    private static boolean isXmlWhitespace(String text) {
        for (int offset = 0; offset < text.length(); offset++) {
            if (!isXmlWhitespace(text.charAt(offset))) {
                return false;
            }
        }
        return true;
    }

    /** Checks the XML 1.0 whitespace characters, not Java's broader blank set. */
    private static boolean isXmlWhitespace(char value) {
        return value == ' ' || value == '\t' || value == '\n' || value == '\r';
    }

    /** Rejects comments, processing instructions, and other document-level content. */
    private static void validateDocumentChildren(Document document, NamingOperation operation) {
        NodeList children = document.getChildNodes();
        int roots = 0;
        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                roots++;
            } else if (child.getNodeType() == Node.TEXT_NODE && isXmlWhitespace(child.getNodeValue())) {
                continue;
            } else {
                throw failure(NamingFailureCategory.MALFORMED_RECORD,
                        operation, NamingFieldKind.XML_RECORD, null);
            }
        }
        if (roots != 1) {
            throw failure(NamingFailureCategory.MALFORMED_RECORD,
                    operation, NamingFieldKind.XML_RECORD, null);
        }
    }

    /** Prevents parser diagnostics from retaining or echoing input text. */
    private static final class StrictErrorHandler implements ErrorHandler {
        @Override
        public void warning(SAXParseException exception) throws SAXException {
            throw new SAXException("Malformed XML");
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            throw new SAXException("Malformed XML");
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            throw new SAXException("Malformed XML");
        }
    }
}