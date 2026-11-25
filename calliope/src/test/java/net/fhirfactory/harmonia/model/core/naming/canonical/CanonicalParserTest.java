/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.canonical;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;
import org.junit.jupiter.api.Test;

/** Verifies that the canonical parsers return neutral, decoded, ordered fields. */
class CanonicalParserTest {

    @Test
    void topicParserDecodesOneEscapeLayerAndPreservesOrderAndVersions() {
        assertEquals(List.of(
                        new CanonicalEntryFields("System", null, "Harmonia"),
                        new CanonicalEntryFields("Path", "1.0", "a.b=c\\d")),
                TopicCanonicalParser.parse("System=Harmonia.Path:1\\.0=a\\.b\\=c\\\\d", true));
        assertEquals(List.of(new CanonicalEntryFields("Role", null, "LAB")),
                TopicCanonicalParser.parse("Role=LAB", false));
    }

    @Test
    void maskParserKeepsEmptyPatternsAndDistinguishesOmittedFromPresentEmptyConstraints() {
        assertEquals(List.of(
                        new CanonicalEntryFields("Role", null, "LAB*"),
                        new CanonicalEntryFields("", "", ""),
                        new CanonicalEntryFields("*", ">=1.2,<2", "\\*")),
                MaskExpressionParser.parse("Role=LAB*.:=.*:>\\=1\\.2,<2=\\\\*"));
        assertEquals(List.of(new CanonicalEntryFields("", null, "")),
                MaskExpressionParser.parse("="));
        assertEquals(List.of(new CanonicalEntryFields("Role", null, "")),
                MaskExpressionParser.parse("Role="));
    }

    @Test
    void syntaxFailuresAreRuleViolationsWithOperationAndEntryIndex() {
        NamingRuleViolation topic = assertThrows(NamingRuleViolation.class,
                () -> TopicCanonicalParser.parse("Role=LAB.Bad:1=X", false));
        assertEquals(NamingFailureCategory.MALFORMED_TEXT, topic.getCategory());
        assertEquals(NamingOperation.TOPIC_PARSE, topic.getOperation());
        assertEquals(NamingFieldKind.CANONICAL_TEXT, topic.getFieldKind());
        assertEquals(1, topic.getEntryIndex());

        NamingRuleViolation mask = assertThrows(NamingRuleViolation.class,
                () -> MaskExpressionParser.parse(null));
        assertEquals(NamingFailureCategory.MISSING_INPUT, mask.getCategory());
        assertEquals(NamingOperation.MASK_PARSE, mask.getOperation());

        NamingRuleViolation depth = assertThrows(NamingRuleViolation.class,
                () -> MaskExpressionParser.parse("*=*.".repeat(16) + "*=*"));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, depth.getCategory());
        assertEquals(NamingFieldKind.DEPTH, depth.getFieldKind());
    }

    @Test
    void emptyTopicFieldsRetainTheirFieldSpecificClassification() {
        assertTopicFieldFailure("=LAB", NamingFieldKind.QUALIFIER, 0);
        assertTopicFieldFailure("Role=", NamingFieldKind.VALUE, 0);
        assertTopicFieldFailure("Role:1=", NamingFieldKind.VALUE, 0);
        assertTopicFieldFailure("Role=LAB.Child=", NamingFieldKind.VALUE, 1);
        assertTopicFieldFailure(":1=LAB", NamingFieldKind.QUALIFIER, 0);
        assertTopicFieldFailure("Role:=LAB", NamingFieldKind.VERSION, 0);
    }

    private static void assertTopicFieldFailure(
            String text, NamingFieldKind fieldKind, int index) {
        NamingRuleViolation failure = assertThrows(NamingRuleViolation.class,
                () -> TopicCanonicalParser.parse(text, true), text);
        assertEquals(NamingFailureCategory.MALFORMED_TEXT, failure.getCategory());
        assertEquals(NamingOperation.TOPIC_PARSE, failure.getOperation());
        assertEquals(fieldKind, failure.getFieldKind());
        assertEquals(index, failure.getEntryIndex());
    }
}
