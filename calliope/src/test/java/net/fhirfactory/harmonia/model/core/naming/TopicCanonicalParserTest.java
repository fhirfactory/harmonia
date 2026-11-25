/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;
import org.junit.jupiter.api.Test;

/** Validates the strict Topic canonical grammar and escaped field reconstruction. */
class TopicCanonicalParserTest {

    @Test
    void canonicalParserRejectsMissingFieldsMalformedSeparatorsAndUnsupportedEscapes() {
        for (String text : new String[]{"", "=value", "N1=", "System=value.", ".System=value",
                "System=value=extra", "System:value=Harmonia", "Role:2=LAB", "System=bad\\q"}) {
            assertThrows(NamingValidationException.class, () -> Topic.fromCanonicalName(text), text);
        }
        NamingValidationException emptyVersion = assertThrows(NamingValidationException.class,
                () -> Topic.fromCanonicalNameWithVersion("Role:=LAB"));
        assertEquals(NamingFailureCategory.MALFORMED_TEXT, emptyVersion.getCategory());
        assertThrows(NamingValidationException.class,
                () -> Topic.fromCanonicalNameWithVersion("Role:2=LAB:extra"));
    }

    @Test
    void canonicalFailuresIdentifyTheActualOperationFieldAndKnownEntry() {
        NamingValidationException topicFailure = assertThrows(NamingValidationException.class,
                () -> Topic.fromCanonicalName("Role=LAB.Component=bad\\q"));
        assertEquals(NamingOperation.TOPIC_PARSE, topicFailure.getOperation());
        assertEquals(NamingFieldKind.CANONICAL_TEXT, topicFailure.getFieldKind());
        assertEquals(1, topicFailure.getEntryIndex().orElseThrow());
        assertEquals(NamingFailureCategory.MALFORMED_TEXT, topicFailure.getCategory());
        assertFalse(topicFailure.getMessage().contains("bad"));

        NamingValidationException maskFailure = assertThrows(NamingValidationException.class,
                () -> TopicMask.fromCanonicalExpression("Role=LAB.Component=bad\\q", false));
        assertEquals(NamingOperation.MASK_PARSE, maskFailure.getOperation());
        assertEquals(NamingFieldKind.CANONICAL_TEXT, maskFailure.getFieldKind());
        assertEquals(1, maskFailure.getEntryIndex().orElseThrow());
        assertEquals(NamingFailureCategory.MALFORMED_TEXT, maskFailure.getCategory());
        assertFalse(maskFailure.getMessage().contains("bad"));
    }

    @Test
    void allStructuralCharactersRoundTripAcrossCanonicalAndDisplayProjections() {
        String originalValue = "A.B=C:D\\E";
        Topic topic = Topic.builder().addEntry("Role:Name", originalValue, "2.4\\x").build();

        Topic parsed = Topic.fromCanonicalNameWithVersion(topic.getCanonicalNameWithVersion());

        assertEquals(originalValue, parsed.getEntries().get(0).getValue());
        assertEquals("Role:Name", parsed.getEntries().get(0).getQualifier());
        assertEquals("2.4\\x", parsed.getEntries().get(0).getVersion().orElseThrow());
        assertEquals(topic.getCanonicalNameWithVersion(), parsed.getCanonicalNameWithVersion());
        assertEquals("Role\\:Name=A\\.B\\=C\\:D\\\\E", topic.getCanonicalName());
        assertEquals(originalValue, topic.getDisplayName());
    }

    @Test
    void canonicalByteLimitsAreCheckedBeforeStructuralParsing() {
        NamingValidationException topicLimit = assertThrows(NamingValidationException.class,
                () -> Topic.fromCanonicalName("x".repeat(NamingLimits.MAX_TOPIC_CANONICAL_UTF8_BYTES + 1)));
        NamingValidationException maskLimit = assertThrows(NamingValidationException.class,
                () -> TopicMask.fromCanonicalExpression("x".repeat(
                        NamingLimits.MAX_MASK_CANONICAL_UTF8_BYTES + 1), false));

        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, topicLimit.getCategory());
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, maskLimit.getCategory());
        NamingValidationException topicAtLimit = assertThrows(NamingValidationException.class,
                () -> Topic.fromCanonicalName("x".repeat(NamingLimits.MAX_TOPIC_CANONICAL_UTF8_BYTES)));
        NamingValidationException maskAtLimit = assertThrows(NamingValidationException.class,
                () -> TopicMask.fromCanonicalExpression(
                        "x".repeat(NamingLimits.MAX_MASK_CANONICAL_UTF8_BYTES), false));
        assertEquals(NamingFailureCategory.MALFORMED_TEXT, topicAtLimit.getCategory());
        assertEquals(NamingFailureCategory.MALFORMED_TEXT, maskAtLimit.getCategory());
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED,
                assertThrows(NamingValidationException.class,
                        () -> Topic.fromCanonicalName("Q=x.".repeat(NamingLimits.MAX_DEPTH) + "Q=x"))
                        .getCategory());
        assertThrows(NamingValidationException.class, () -> TopicMask.fromCanonicalExpression("@all", false));
    }
}