/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Covers exact, operator-led, ranged and absent version-constraint semantics. */
class VersionConstraintTest {

    @Test
    void numericOperatorsUseComponentOrderingAndPreserveTopicSpellings() {
        assertTrue(matches("=1.2", "01.002.0"));
        assertTrue(matches("<1.10", "1.9"));
        assertTrue(matches("<=1.2", "1.2.0"));
        assertTrue(matches(">1.9", "1.10"));
        assertTrue(matches(">=1.2", "1.2.0"));
        assertFalse(matches("=1.2", "1.2.1"));
        assertFalse(matches(">1.2", "1.2.0"));
        assertFalse(matches("<1.2", "1.2.0"));
        assertFalse(matches(">=1.2", "release-1.2"));

        Topic original = topic("01.002");
        assertEquals("01.002", original.getEntries().get(0).getVersion().orElseThrow());
        assertEquals("Role:01\\.002=LAB", original.getCanonicalNameWithVersion());
    }

    @Test
    void numericRangesValidateDirectionInclusivityAndClosedSingletons() {
        assertTrue(matches(">=1.2,<2.0", "1.10"));
        assertTrue(matches(">1.2,<=2.0", "1.2.1"));
        assertFalse(matches(">=1.2,<2.0", "2.0"));
        assertTrue(matches(">=1.2,<=1.2.0", "1.2"));
        String adjacentLower = "0.".repeat(31) + "0";
        String adjacentUpper = "0.".repeat(31) + "1";
        assertTrue(TopicMaskEntry.of("Role", "LAB", ">" + adjacentLower + ",<" + adjacentUpper)
                .getVersionConstraint().isPresent());
        assertThrows(NamingValidationException.class, () -> TopicMaskEntry.of("Role", "LAB", ">1.2,<=1.2.0"));
        assertThrows(NamingValidationException.class, () -> TopicMaskEntry.of("Role", "LAB", ">=2,<1"));
        assertThrows(NamingValidationException.class, () -> TopicMaskEntry.of("Role", "LAB", ">=1,>2"));
        assertThrows(NamingValidationException.class, () -> TopicMaskEntry.of("Role", "LAB", ">=1,<=2,<=3"));
        assertThrows(NamingValidationException.class, () -> TopicMaskEntry.of("Role", "LAB", "=1,<2"));
        assertThrows(NamingValidationException.class, () -> TopicMaskEntry.of("Role", "LAB", ">=1.2,>2.0"));
    }

    @Test
    void exactTextEscapesCommaBackslashOperatorsAndAsteriskWithoutNumericCoercion() {
        assertTrue(matches("1.2", "1.2"));
        assertFalse(matches("1.2", "1.2.0"));
        assertTrue(matches("release,hotfix", "release,hotfix"));
        assertTrue(matches("\\=1.2", "=1.2"));
        assertTrue(matches("\\<ci", "<ci"));
        assertTrue(matches("\\>ops", ">ops"));
        assertTrue(matches("\\\\build", "\\build"));
        assertTrue(matches("*", "*"));
        assertFalse(matches("*", "anything"));
        TopicMaskEntry literal = TopicMaskEntry.ofLiterals("Role", "LAB", "=1.2");
        assertEquals("\\=1.2", literal.getVersionConstraint().orElseThrow());
        assertTrue(literal.matchesVersion("=1.2"));
        assertTrue(matches("1.2-beta", "1.2-beta"));
        assertFalse(matches(">=1.2", "1.2-beta"));

        Topic firstSpelling = topic("1.2");
        Topic equivalentSpelling = topic("1.2.0");
        assertEquals(firstSpelling, equivalentSpelling);
        TopicMask numericEqual = TopicMask.builder().addPatterns("Role", "LAB", "=1.2").build();
        assertTrue(numericEqual.matches(firstSpelling));
        assertTrue(numericEqual.matches(equivalentSpelling));
        assertFalse(TopicMask.builder().addPatterns("Role", "LAB", "1.2").build()
                .matches(equivalentSpelling));
    }

    @Test
    void omittedEmptyAndPresentConstraintsTreatAbsentVersionsDifferently() {
        Topic absent = topic(null);
        Topic present = topic("v1");
        TopicMask omitted = TopicMask.builder().addPatterns("Role", "LAB").build();
        TopicMask empty = TopicMask.builder().addPatterns("Role", "LAB", "").build();
        TopicMask exact = TopicMask.builder().addPatterns("Role", "LAB", "v1").build();

        assertTrue(omitted.matches(absent));
        assertTrue(omitted.matches(present));
        assertFalse(empty.matches(absent));
        assertFalse(empty.matches(present));
        assertFalse(exact.matches(absent));
        assertTrue(exact.matches(present));
        assertNotEquals(omitted.getEntries().get(0), empty.getEntries().get(0));
        assertEquals("", empty.getEntries().get(0).getVersionConstraint().orElseThrow());
        assertEquals("v1", exact.getEntries().get(0).getVersionConstraint().orElseThrow());
    }

    @Test
    void invalidConstraintAdditionPreservesPriorBuilderStateAndSafeMetadata() {
        TopicMaskBuilder builder = TopicMask.builder().addPatterns("Role", "LAB");
        NamingValidationException failure = assertThrows(NamingValidationException.class,
                () -> builder.addPatterns("Role", "broken", ">=1.x"));

        assertEquals(NamingOperation.MASK_BUILD, failure.getOperation());
        assertEquals(NamingFieldKind.VERSION_CONSTRAINT, failure.getFieldKind());
        assertEquals(1, failure.getEntryIndex().orElseThrow());
        assertFalse(failure.getMessage().contains("1.x"));
        assertTrue(builder.build().matches(topic(null)));
    }

    @Test
    void exactNumericOperandComponentAndConstraintBoundsAreEnforced() {
        String operand64 = "9".repeat(NamingLimits.MAX_NUMERIC_VERSION_OPERAND_CHARS);
        assertTrue(TopicMaskEntry.of("Role", "LAB", ">=" + operand64)
                .getVersionConstraint().isPresent());
        assertTrue(TopicMaskEntry.of("Role", "LAB", "<" + "1.".repeat(31) + "1")
                .getVersionConstraint().isPresent());
        assertTrue(TopicMaskEntry.of("Role", "LAB", "v".repeat(64))
                .getVersionConstraint().isPresent());
        assertTrue(TopicMaskEntry.of("Role", "LAB", "\\=" + "1".repeat(63))
                .getVersionConstraint().isPresent());

        String maximumRange = ">=" + "1".repeat(64) + ",<=" + "9".repeat(64);
        assertEquals(NamingLimits.MAX_RANGE_CONSTRAINT_RAW_CHARS, maximumRange.length());
        assertTrue(TopicMaskEntry.of("Role", "LAB", maximumRange)
                .getVersionConstraint().isPresent());

        for (String invalid : new String[]{"=" + "9".repeat(65), ">" + "9".repeat(65),
                "<" + "1.".repeat(32) + "1", "v".repeat(65), "\\=" + "1".repeat(64)}) {
            NamingValidationException failure = assertThrows(NamingValidationException.class,
                    () -> TopicMaskEntry.of("Role", "LAB", invalid), invalid);
            assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, failure.getCategory(), invalid);
        }
    }

    private static boolean matches(String constraint, String version) {
        TopicMask mask = TopicMask.builder().addPatterns("Role", "LAB", constraint).build();
        return mask.matches(topic(version));
    }

    private static Topic topic(String version) {
        return Topic.builder().addEntry("Role", "LAB", version).build();
    }
}