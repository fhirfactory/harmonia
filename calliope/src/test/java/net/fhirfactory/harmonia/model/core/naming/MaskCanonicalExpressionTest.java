/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests independent canonical mask grammar, delimiter escaping, and reconstruction. */
class MaskCanonicalExpressionTest {

    @Test
    void canonicalTextEscapesEachRawFieldOnceAndPreservesOriginalConstraint() {
        TopicMask original = TopicMask.builder()
                .addPatterns("Patient\\*", "A.B:C=D\\E", ">=1.2,<2.0")
                .includeContainedElements(true)
                .build();
        String expected = "Patient\\\\*:>\\=1\\.2,<2\\.0=A\\.B\\:C\\=D\\\\E";

        assertEquals(expected, original.getCanonicalExpression());
        TopicMask reconstructed = TopicMask.fromCanonicalExpression(expected, true);
        assertEquals(original, reconstructed);
        assertEquals(expected, reconstructed.getCanonicalExpression());
        assertEquals("Patient\\*", reconstructed.getEntries().get(0).getQualifierPattern());
        assertEquals(">=1.2,<2.0",
                reconstructed.getEntries().get(0).getVersionConstraint().orElseThrow());
        TopicMask exactDepth = TopicMask.fromCanonicalExpression(expected, false);
        assertFalse(exactDepth.isIncludeContainedElements());
        assertFalse(exactDepth.equals(reconstructed));
    }

    @Test
    void omissionPresentEmptyAndEmptyPatternsRemainDistinct() {
        TopicMask omitted = TopicMask.fromCanonicalExpression("Role=LAB", false);
        TopicMask presentEmpty = TopicMask.fromCanonicalExpression("Role:=LAB", false);
        TopicMask emptyPatterns = TopicMask.fromCanonicalExpression("=", false);

        assertEquals("Role=LAB", omitted.getCanonicalExpression());
        assertEquals("Role:=LAB", presentEmpty.getCanonicalExpression());
        assertEquals("", presentEmpty.getEntries().get(0).getVersionConstraint().orElseThrow());
        assertEquals("=", emptyPatterns.getCanonicalExpression());
        assertFalse(emptyPatterns.matches(Topic.builder().addEntry("Role", "LAB").build()));
        assertTrue(TopicMask.fromCanonicalExpression("*=*", true).isIncludeContainedElements());
    }

    @Test
    void canonicalParserRejectsMalformedEscapesAndDelimiterArrangements() {
        for (String input : new String[]{"", ".Role=LAB", "Role=LAB.", "Role==LAB", "Role:1:2=LAB",
                "Role=LAB\\q", "Role:=LAB:extra", "Role=LAB\\", "@all"}) {
            assertThrows(NamingValidationException.class,
                    () -> TopicMask.fromCanonicalExpression(input, false), input);
        }
        assertTrue(TopicMask.fromCanonicalExpression("Role=LAB", true)
                .matches(Topic.builder().addEntry("Role", "LAB").addEntry("N1", "Extra").build()));
    }

    @Test
    void ordinaryUniversalDistinctionIsNeverInferredFromText() {
        TopicMask ordinaryAny = TopicMask.fromCanonicalExpression("*=*", true);
        assertFalse(ordinaryAny.isUniversal());
        assertEquals("*=*", ordinaryAny.getCanonicalExpression());
        assertTrue(TopicMask.all().isUniversal());
        assertThrows(NamingValidationException.class, () -> TopicMask.fromCanonicalExpression("@all", false));
    }

    @Test
    void exactCommaConstraintRemainsLiteralAcrossCanonicalReconstruction() {
        TopicMask original = TopicMask.builder().addPatterns("Role", "LAB", "release,hotfix").build();
        String expected = "Role:release,hotfix=LAB";

        assertEquals(expected, original.getCanonicalExpression());
        TopicMask reconstructed = TopicMask.fromCanonicalExpression(expected, false);
        assertEquals(original, reconstructed);
        assertTrue(reconstructed.matches(Topic.builder()
                .addEntry("Role", "LAB", "release,hotfix").build()));
    }
}