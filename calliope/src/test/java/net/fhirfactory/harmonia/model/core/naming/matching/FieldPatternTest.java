/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.matching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;
import org.junit.jupiter.api.Test;

/** Verifies wildcard classification, escape decoding, and bounded pattern matching. */
class FieldPatternTest {

    @Test
    void acceptsOnlyTheSixWholeFieldWildcardForms() {
        assertPattern("Patient", "Patient", true);
        assertPattern("Patient", "patient", false);
        assertPattern("Patient*", "Patient Record", true);
        assertPattern("Patient*", "XPatient", false);
        assertPattern("*Patient", "New Patient", true);
        assertPattern("*Patient", "Patient Record", false);
        assertPattern("*Patient*", "New Patient Record", true);
        assertPattern("Patient*Record", "PatientRecord", true);
        assertPattern("Patient*Record", "Patient.Middle.Record", true);
        assertPattern("Patient*Record", "PatientRecordX", false);
        assertPattern("AB*BC", "ABC", false);
        assertPattern("*", "anything", true);
        assertPattern("", "anything", false);
        assertPattern("患者*", "患者 records", true);
        assertPattern("*😀*", "Patient 😀 Record", true);
    }

    @Test
    void escapedAsterisksAndBackslashesAreLiteralWhileOtherCharactersStayLiteral() {
        assertPattern("Patient\\*", "Patient*", true);
        assertPattern("Patient\\*", "PatientX", false);
        assertPattern("C:\\\\temp", "C:\\temp", true);
        assertPattern("\\*\\\\", "*\\", true);
        assertPattern("[a-z].+$", "[a-z].+$", true);
        assertPattern("[a-z].+$", "abc", false);
        assertEquals("Patient\\*\\\\", FieldPattern.escapeLiteral("Patient*\\"));
    }

    @Test
    void unsupportedArrangementsAndMalformedUnicodeAreRejectedWithoutPayloads() {
        NamingRuleViolation absent = assertThrows(NamingRuleViolation.class,
                () -> FieldPattern.parse(null, NamingFieldKind.VALUE_PATTERN,
                        NamingOperation.MASK_ENTRY_BUILD, null));
        assertEquals(NamingFailureCategory.MISSING_INPUT, absent.getCategory());
        for (String pattern : new String[]{"**", "Pa*ti*ent", "*Patient*Record", "Patient*Record*"}) {
            NamingRuleViolation failure = assertThrows(NamingRuleViolation.class,
                    () -> FieldPattern.parse(pattern, NamingFieldKind.VALUE_PATTERN,
                            NamingOperation.MASK_ENTRY_BUILD, 0));
            assertEquals(NamingFailureCategory.PATTERN_REJECTED, failure.getCategory());
            assertEquals(0, failure.getEntryIndex());
            assertFalse(failure.getMessage().contains(pattern));
            assertEquals(0, failure.getStackTrace().length);
        }
        NamingRuleViolation malformed = assertThrows(NamingRuleViolation.class,
                () -> FieldPattern.parse("bad\uD800", NamingFieldKind.VALUE_PATTERN,
                        NamingOperation.MASK_ENTRY_BUILD, 0));
        assertEquals(NamingFailureCategory.INVALID_FIELD, malformed.getCategory());
        assertEquals(NamingFieldKind.VALUE_PATTERN, malformed.getFieldKind());
    }

    @Test
    void patternBoundCountsCodePointsAndRejectsValuesAboveTheMaximum() {
        String exact = "😀".repeat(NamingLimits.MAX_PATTERN_CODE_POINTS);
        assertTrue(FieldPattern.parse(exact, NamingFieldKind.VALUE_PATTERN,
                NamingOperation.MASK_ENTRY_BUILD, null).matches(exact));
        NamingRuleViolation oversized = assertThrows(NamingRuleViolation.class,
                () -> FieldPattern.parse(exact + "😀", NamingFieldKind.VALUE_PATTERN,
                        NamingOperation.MASK_ENTRY_BUILD, null));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, oversized.getCategory());
    }

    @Test
    void compiledCriterionValidatesFieldsInOrderAndDistinguishesConstraintPresence() {
        NamingRuleViolation qualifierFirst = assertThrows(NamingRuleViolation.class,
                () -> EntryCriterion.compile("**", "**", ">=x", NamingOperation.MASK_BUILD, 3));
        assertEquals(NamingFieldKind.QUALIFIER_PATTERN, qualifierFirst.getFieldKind());
        assertEquals(3, qualifierFirst.getEntryIndex());
        NamingRuleViolation constraintLast = assertThrows(NamingRuleViolation.class,
                () -> EntryCriterion.compile("Role", "LAB", ">=x", NamingOperation.MASK_BUILD, 3));
        assertEquals(NamingFieldKind.VERSION_CONSTRAINT, constraintLast.getFieldKind());

        EntryCriterion omitted = EntryCriterion.compile("Role", "LAB", null, NamingOperation.MASK_BUILD, 0);
        EntryCriterion presentEmpty = EntryCriterion.compile("Role", "LAB", "", NamingOperation.MASK_BUILD, 0);
        assertFalse(omitted.hasVersionConstraint());
        assertTrue(omitted.matchesVersion(null));
        assertTrue(presentEmpty.hasVersionConstraint());
        assertFalse(presentEmpty.matchesVersion("1"));
        assertFalse(presentEmpty.matchesVersion(null));
    }

    private static void assertPattern(String pattern, String candidate, boolean expected) {
        assertEquals(expected, FieldPattern.parse(pattern, NamingFieldKind.VALUE_PATTERN,
                NamingOperation.MASK_ENTRY_BUILD, null).matches(candidate), pattern);
    }
}
