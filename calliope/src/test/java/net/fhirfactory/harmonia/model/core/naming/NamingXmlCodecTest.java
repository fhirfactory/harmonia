/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;
import org.junit.jupiter.api.Test;

/** Checks independently authored XML fixtures, deterministic bytes, and strict reconstruction. */
class NamingXmlCodecTest {

    @Test
    void topicFixtureRestoresOrderedCompletedFieldsAndWritesExactBytes() throws IOException {
        byte[] fixture = resource("topic.xml");
        Topic topic = NamingXmlCodec.readTopic(fixture);

        assertEquals(2, topic.getDepth());
        assertEquals("System", topic.getEntries().get(0).getQualifier());
        assertEquals("Harmonia", topic.getEntries().get(0).getValue());
        assertFalse(topic.getEntries().get(0).getVersion().isPresent());
        assertEquals("Component", topic.getEntries().get(1).getQualifier());
        assertEquals("Petasos.Receiver", topic.getEntries().get(1).getValue());
        assertEquals("2.4", topic.getEntries().get(1).getVersion().orElseThrow());
        assertEquals("System=Harmonia.Component=Petasos\\.Receiver", topic.getCanonicalName());
        assertArrayEquals(fixture, NamingXmlCodec.writeTopic(topic));
    }

    @Test
    void ordinaryMaskFixturePreservesKindContainmentOrderAndRawRange() throws IOException {
        byte[] fixture = resource("ordinary-mask.xml");
        TopicMask mask = NamingXmlCodec.readTopicMask(fixture);
        TopicMask expected = TopicMask.builder().addPatterns("Role", "LA*", ">=1.2,<2.0")
                .includeContainedElements(true).build();

        assertEquals(expected, mask);
        assertFalse(mask.isUniversal());
        assertTrue(mask.isIncludeContainedElements());
        assertEquals("Role", mask.getEntries().get(0).getQualifierPattern());
        assertEquals("LA*", mask.getEntries().get(0).getValuePattern());
        assertEquals(">=1.2,<2.0", mask.getEntries().get(0).getVersionConstraint().orElseThrow());
        assertTrue(mask.matches(Topic.builder().addEntry("Role", "LAB", "1.10").build()));
        assertFalse(mask.matches(Topic.builder().addEntry("Role", "LAB", "2.0").build()));
        assertArrayEquals(fixture, NamingXmlCodec.writeTopicMask(mask));
    }

    @Test
    void emptyConstraintAndUniversalFixtureRemainDifferentLogicalRecords() throws IOException {
        byte[] emptyFixture = resource("ordinary-mask-empty-constraint.xml");
        TopicMask empty = NamingXmlCodec.readTopicMask(emptyFixture);
        assertFalse(empty.isUniversal());
        assertEquals("", empty.getEntries().get(0).getVersionConstraint().orElseThrow());
        assertFalse(empty.matches(Topic.builder().addEntry("Role", "LAB", "1").build()));
        assertArrayEquals(emptyFixture, NamingXmlCodec.writeTopicMask(empty));

        byte[] universalFixture = resource("universal-mask.xml");
        TopicMask universal = NamingXmlCodec.readTopicMask(universalFixture);
        assertTrue(universal.isUniversal());
        assertTrue(universal.getEntries().isEmpty());
        assertArrayEquals(universalFixture, NamingXmlCodec.writeTopicMask(universal));
    }

    @Test
    void strictXmlRejectsOrderingDuplicatesUnexpectedContentAndInvalidDeclarations() throws IOException {
        NamingValidationException ordering = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopicMask(resource("malformed-mask-order.xml")));
        assertEquals(NamingOperation.MASK_XML_READ, ordering.getOperation());
        assertEquals(0, ordering.getEntryIndex().orElseThrow());
        rejectMask("<topicMask kind=\"ordinary\" includeContainedElements=\"false\"><entry>"
                + "<qualifierPattern encoding=\"base64\">Um9sZQ==</qualifierPattern>"
                + "<qualifierPattern encoding=\"base64\">Um9sZQ==</qualifierPattern>"
                + "</entry></topicMask>");
        rejectMask("<topicMask kind=\"ordinary\" includeContainedElements=\"yes\"></topicMask>");
        rejectMask("<topicMask kind=\"universal\" includeContainedElements=\"false\"></topicMask>");
        rejectMask("<topicMask kind=\"ordinary\" includeContainedElements=\"false\"><entry><!--x--></entry></topicMask>");
        rejectMask("<topicMask kind=\"ordinary\" includeContainedElements=\"false\"><entry>"
                + "<qualifierPattern encoding=\"base64\">Um9sZQ</qualifierPattern>"
                + "<valuePattern encoding=\"base64\">TEFC</valuePattern>"
                + "</entry></topicMask>");
        rejectMask("<?xml version=\"1.0\" encoding=\"UTF-16\"?><topicMask kind=\"universal\"></topicMask>");
    }

    @Test
    void topicXmlRequiresStrictCompletedFieldsInAcceptedOrder() {
        for (String xml : new String[]{
                "<topic><entry><value>LAB</value></entry></topic>",
                "<topic><entry><qualifier></qualifier><value>LAB</value></entry></topic>",
                "<topic><entry><qualifier> </qualifier><value>LAB</value></entry></topic>",
                "<topic><entry><qualifier>Role</qualifier><value>LAB</value><version></version></entry></topic>",
                "<topic><entry><value>LAB</value><qualifier>Role</qualifier></entry></topic>",
                "<topic><entry><qualifier>Role</qualifier><qualifier>Other</qualifier>"
                        + "<value>LAB</value></entry></topic>",
                "<topic><entry id=\"unexpected\"><qualifier>Role</qualifier>"
                        + "<value>LAB</value></entry></topic>"}) {
            NamingValidationException failure = assertThrows(NamingValidationException.class,
                    () -> NamingXmlCodec.readTopic(xml.getBytes(StandardCharsets.UTF_8)), xml);
            assertEquals(NamingOperation.TOPIC_XML_READ, failure.getOperation());
            assertEquals(0, failure.getEntryIndex().orElseThrow(), xml);
            assertFalse(failure.getMessage().contains("Role"));
        }
    }

    @Test
    void malformedUtf8DtdAndOversizedXmlFailClosed() {
        byte[] malformedUtf8 = {(byte) 0xc3, (byte) 0x28};
        byte[] dtd = ("<!DOCTYPE topic [<!ENTITY x SYSTEM \"file:///etc/passwd\">]>"
                + "<topic><entry><qualifier>Role</qualifier><value>&x;</value></entry></topic>")
                .getBytes(StandardCharsets.UTF_8);
        byte[] oversized = new byte[NamingLimits.MAX_XML_RECORD_UTF8_BYTES + 1];

        NamingValidationException utf8Failure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopic(malformedUtf8));
        assertEquals(NamingFailureCategory.MALFORMED_RECORD, utf8Failure.getCategory());
        NamingValidationException dtdFailure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopic(dtd));
        assertEquals(NamingFailureCategory.MALFORMED_RECORD, dtdFailure.getCategory());
        NamingValidationException limit = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopic(oversized));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, limit.getCategory());
    }

    @Test
    void xmlTextPreservesUnicodeAndUsesOnlyStructuralXmlWhitespace() {
        String topicXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<topic><entry><qualifier>Role</qualifier><value>e\u0301😀</value></entry></topic>";
        Topic parsed = NamingXmlCodec.readTopic(topicXml.getBytes(StandardCharsets.UTF_8));
        assertEquals("e\u0301😀", parsed.getEntries().get(0).getValue());
        assertTrue(new String(NamingXmlCodec.writeTopic(parsed), StandardCharsets.UTF_8)
                .contains("<value>e\u0301😀</value>"));

        String nonXmlWhitespace = "<topic><entry><qualifier>Role</qualifier>\u00a0<value>LAB</value>"
                + "</entry></topic>";
        assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopic(nonXmlWhitespace.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void xmlInterchangeAcceptsOnlyXmlVersion10() {
        String topicBody = "<topic><entry><qualifier>Role</qualifier><value>LAB</value></entry></topic>";
        String maskBody = "<topicMask kind=\"universal\"></topicMask>";

        assertEquals(1, NamingXmlCodec.readTopic(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + topicBody).getBytes(StandardCharsets.UTF_8)).getDepth());
        assertEquals(1, NamingXmlCodec.readTopic(topicBody.getBytes(StandardCharsets.UTF_8)).getDepth());
        assertTrue(NamingXmlCodec.readTopicMask(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + maskBody).getBytes(StandardCharsets.UTF_8)).isUniversal());
        assertTrue(NamingXmlCodec.readTopicMask(maskBody.getBytes(StandardCharsets.UTF_8)).isUniversal());

        String topic11 = "<?xml version=\"1.1\"?><topic>"
                + "<entry><qualifier>Role</qualifier><value>LAB</value></entry></topic>";
        NamingValidationException topicFailure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopic(topic11.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NamingFailureCategory.MALFORMED_RECORD, topicFailure.getCategory());
        assertEquals(NamingFieldKind.XML_RECORD, topicFailure.getFieldKind());

        String topic11RestrictedCharacter = "<?xml version=\"1.1\"?><topic>"
                + "<entry><qualifier>Role</qualifier><value>&#x1;</value></entry></topic>";
        NamingValidationException restrictedCharacterFailure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopic(topic11RestrictedCharacter.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NamingFailureCategory.MALFORMED_RECORD, restrictedCharacterFailure.getCategory());
        assertEquals(NamingFieldKind.XML_RECORD, restrictedCharacterFailure.getFieldKind());

        String mask11 = "<?xml version='1.1'?><topicMask kind='universal'></topicMask>";
        NamingValidationException maskFailure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopicMask(mask11.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NamingFailureCategory.MALFORMED_RECORD, maskFailure.getCategory());
        assertEquals(NamingFieldKind.XML_RECORD, maskFailure.getFieldKind());
    }

    @Test
    void xmlFieldFailuresReportTheUnambiguouslyIdentifiedField() {
        assertXmlField("<topic><entry><qualifier><nested/></qualifier><value>LAB</value></entry></topic>",
                NamingFieldKind.QUALIFIER);
        assertXmlField("<topic><entry><qualifier>Role</qualifier><value><nested/></value></entry></topic>",
                NamingFieldKind.VALUE);
        assertXmlField("<topic><entry><qualifier>Role</qualifier><value>LAB</value>"
                + "<version><nested/></version></entry></topic>", NamingFieldKind.VERSION);

        String badQualifierEncoding = "<topicMask kind=\"ordinary\" includeContainedElements=\"false\"><entry>"
                + "<qualifierPattern encoding=\"base64\">bad!</qualifierPattern>"
                + "<valuePattern encoding=\"base64\">TEFC</valuePattern></entry></topicMask>";
        NamingValidationException qualifierFailure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopicMask(badQualifierEncoding.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NamingFieldKind.QUALIFIER_PATTERN, qualifierFailure.getFieldKind());

        String badValueUtf8 = "<topicMask kind=\"ordinary\" includeContainedElements=\"false\"><entry>"
                + "<qualifierPattern encoding=\"base64\">Um9sZQ==</qualifierPattern>"
                + "<valuePattern encoding=\"base64\">wyg=</valuePattern></entry></topicMask>";
        NamingValidationException valueFailure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopicMask(badValueUtf8.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NamingFieldKind.VALUE_PATTERN, valueFailure.getFieldKind());

        String badVersionEncoding = "<topicMask kind=\"ordinary\" includeContainedElements=\"false\"><entry>"
                + "<qualifierPattern encoding=\"base64\">Um9sZQ==</qualifierPattern>"
                + "<valuePattern encoding=\"base64\">TEFC</valuePattern>"
                + "<versionConstraint encoding=\"hex\">XD0x</versionConstraint></entry></topicMask>";
        NamingValidationException versionFailure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopicMask(badVersionEncoding.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NamingFieldKind.VERSION_CONSTRAINT, versionFailure.getFieldKind());
        assertEquals(0, versionFailure.getEntryIndex().orElseThrow());
    }

    @Test
    void base64MaskFieldsPreserveLineBreaksAndRejectInvalidUtf8() {
        String xml = "<topicMask kind=\"ordinary\" includeContainedElements=\"false\"><entry>"
                + "<qualifierPattern encoding=\"base64\">Um9sZQ0K</qualifierPattern>"
                + "<valuePattern encoding=\"base64\">TEEq</valuePattern>"
                + "<versionConstraint encoding=\"base64\">XD0xLjI=</versionConstraint>"
                + "</entry></topicMask>";
        TopicMask mask = NamingXmlCodec.readTopicMask(xml.getBytes(StandardCharsets.UTF_8));
        assertEquals("Role\r\n", mask.getEntries().get(0).getQualifierPattern());
        assertEquals("LA*", mask.getEntries().get(0).getValuePattern());
        assertEquals("\\=1.2", mask.getEntries().get(0).getVersionConstraint().orElseThrow());
        assertTrue(new String(NamingXmlCodec.writeTopicMask(mask), StandardCharsets.UTF_8)
                .contains("Um9sZQ0K"));

        String invalidUtf8 = "<topicMask kind=\"ordinary\" includeContainedElements=\"false\"><entry>"
                + "<qualifierPattern encoding=\"base64\">wyg=</qualifierPattern>"
                + "<valuePattern encoding=\"base64\">TEFC</valuePattern>"
                + "</entry></topicMask>";
        NamingValidationException failure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopicMask(invalidUtf8.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NamingFailureCategory.MALFORMED_RECORD, failure.getCategory());
    }

    private byte[] resource(String name) throws IOException {
        String path = "/net/fhirfactory/harmonia/model/core/naming/interchange/" + name;
        try (InputStream input = getClass().getResourceAsStream(path)) {
            if (input == null) {
                throw new IOException("Missing Naming fixture");
            }
            return input.readAllBytes();
        }
    }

    private static void rejectMask(String xml) {
        assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopicMask(xml.getBytes(StandardCharsets.UTF_8)), xml);
    }

    private static void assertXmlField(String xml, NamingFieldKind expectedField) {
        NamingValidationException failure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopic(xml.getBytes(StandardCharsets.UTF_8)), xml);
        assertEquals(expectedField, failure.getFieldKind(), xml);
        assertEquals(0, failure.getEntryIndex().orElseThrow(), xml);
    }
}