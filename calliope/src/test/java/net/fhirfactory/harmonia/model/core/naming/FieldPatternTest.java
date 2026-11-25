/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Verifies wildcard classification, escape decoding, and bounded literal matching. */
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
        NamingValidationException absent = assertThrows(NamingValidationException.class,
                () -> FieldPattern.parse(null, NamingFieldKind.VALUE_PATTERN,
                        NamingOperation.MASK_ENTRY_BUILD, null));
        assertEquals(NamingFailureCategory.MISSING_INPUT, absent.getCategory());
        for (String pattern : new String[]{"**", "Pa*ti*ent", "*Patient*Record", "Patient*Record*"}) {
            NamingValidationException failure = assertThrows(NamingValidationException.class,
                    () -> FieldPattern.parse(pattern, NamingFieldKind.VALUE_PATTERN,
                            NamingOperation.MASK_ENTRY_BUILD, 0));
            assertEquals(NamingFailureCategory.PATTERN_REJECTED, failure.getCategory());
            assertFalse(failure.getMessage().contains(pattern));
        }
        NamingValidationException malformed = assertThrows(NamingValidationException.class,
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
        NamingValidationException oversized = assertThrows(NamingValidationException.class,
                () -> FieldPattern.parse(exact + "😀", NamingFieldKind.VALUE_PATTERN,
                        NamingOperation.MASK_ENTRY_BUILD, null));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, oversized.getCategory());
    }

    @Test
    void literalHelpersMayExpandToTheAcceptedRawPatternLimit() {
        String literal = "*".repeat(NamingLimits.MAX_VALUE_CODE_POINTS);
        TopicMaskEntry entry = TopicMaskEntry.ofLiterals("Role", literal);

        assertEquals(NamingLimits.MAX_PATTERN_CODE_POINTS,
                entry.getValuePattern().codePointCount(0, entry.getValuePattern().length()));
        assertTrue(entry.matchesValue(literal));
        assertFalse(entry.matchesValue(literal + "*"));
    }

    private static void assertPattern(String pattern, String candidate, boolean expected) {
        assertEquals(expected, FieldPattern.parse(pattern, NamingFieldKind.VALUE_PATTERN,
                NamingOperation.MASK_ENTRY_BUILD, null).matches(candidate), pattern);
    }
}