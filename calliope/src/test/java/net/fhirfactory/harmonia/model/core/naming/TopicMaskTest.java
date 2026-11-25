/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies ordinary and universal mask semantics, ordering, helpers, and immutability. */
class TopicMaskTest {

    @Test
    void allSixPatternsApplyToQualifierAndValueAsCompleteCaseSensitiveFields() {
        String[][] cases = {
                {"Patient", "Patient", "Patient", "Patient", "true"},
                {"Patient*", "Patient Record", "Patient*", "Patient Record", "true"},
                {"*Patient", "New Patient", "*Patient", "New Patient", "true"},
                {"*Patient*", "New Patient Record", "*Patient*", "New Patient Record", "true"},
                {"Patient*Record", "Patient.Middle.Record", "Patient*Record", "Patient.Middle.Record", "true"},
                {"*", "AnyValue", "*", "AnyQualifier", "true"}
        };
        for (String[] row : cases) {
            TopicMask mask = TopicMask.builder().addPatterns(row[2], row[0]).build();
            assertEquals(Boolean.parseBoolean(row[4]), mask.matches(topic(row[3], row[1], null)), row[0]);
        }

        assertFalse(TopicMask.builder().addPatterns("Patient", "Patient")
                .build().matches(topic("patient", "Patient", null)));
        assertFalse(TopicMask.builder().addPatterns("Patient", "patient")
                .build().matches(topic("Patient", "Patient", null)));
        assertFalse(TopicMask.builder().addPatterns("Patient*Record", "AB*BC")
                .build().matches(topic("PatientRecord", "ABC", null)));
    }

    @Test
    void escapedWildcardsAndPunctuationAreLiteralAndHelpersAreEquivalent() {
        Topic candidate = topic("Role*", "A.B\\C", null);
        TopicMask escaped = TopicMask.builder().addPatterns("Role\\*", "A.B\\\\C").build();
        TopicMask literals = TopicMask.builder().addLiterals("Role*", "A.B\\C").build();

        assertTrue(escaped.matches(candidate));
        assertTrue(literals.matches(candidate));
        assertEquals(escaped, literals);
        assertTrue(TopicMaskEntry.forLiteralValue("A.B\\C").matchesValue("A.B\\C"));
        assertTrue(TopicMaskEntry.forLiteralQualifier("Role*").matchesQualifier("Role*"));
        assertTrue(TopicMaskEntry.forValue("A.B*").matchesValue("A.BC"));
        assertTrue(TopicMaskEntry.forQualifier("Role*").matchesQualifier("RoleX"));
        assertTrue(TopicMaskEntry.any().matchesValue("anything"));
    }

    @Test
    void orderedCriteriaRequireCorrespondingPositionsAndContainmentIsExplicit() {
        Topic candidate = Topic.builder().addEntry("System", "Harmonia")
                .addEntry("Component", "Calliope").addEntry("Action", "Read").build();
        TopicMask exact = TopicMask.builder().addPatterns("System", "Harmonia")
                .addPatterns("Component", "Calliope").build();
        TopicMask contained = TopicMask.builder().addPatterns("System", "Harmonia")
                .addPatterns("Component", "Calliope").includeContainedElements(true).build();
        TopicMask reversed = TopicMask.builder().addPatterns("Component", "Calliope")
                .addPatterns("System", "Harmonia").includeContainedElements(true).build();

        assertFalse(exact.matches(candidate));
        assertTrue(contained.matches(candidate));
        assertFalse(reversed.matches(candidate));
        assertFalse(contained.matches(topic("System", "Harmonia", null)));
        assertFalse(exact.isIncludeContainedElements());
        assertTrue(contained.isIncludeContainedElements());
    }

    @Test
    void universalMaskIsExplicitAndOrdinaryBroadMaskRemainsDistinct() {
        TopicMask universal = TopicMask.all();
        TopicMask exactDepthAny = TopicMask.builder().addAny().build();
        TopicMask ordinary = TopicMask.builder().addAny().includeContainedElements(true).build();

        assertTrue(universal.isUniversal());
        assertEquals(List.of(), universal.getEntries());
        assertTrue(universal.matches(topic("Role", "LAB", "opaque")));
        assertFalse(universal.matches(null));
        assertFalse(ordinary.isUniversal());
        assertTrue(ordinary.matches(topic("Role", "LAB", "opaque")));
        assertNotEquals(exactDepthAny, ordinary);
        assertNotEquals(universal, ordinary);
        assertThrows(IllegalStateException.class, universal::getCanonicalExpression);
        assertThrows(IllegalStateException.class, universal::isIncludeContainedElements);
        assertEquals(universal, TopicMask.all());
        assertEquals(universal.hashCode(), TopicMask.all().hashCode());
    }

    @Test
    void buildersValidateBeforeMutationAndPublishIndependentImmutableSnapshots() {
        TopicMaskBuilder emptyBuilder = TopicMask.builder();
        assertThrows(NamingValidationException.class, emptyBuilder::build);
        assertEquals(1, emptyBuilder.addAny().build().getEntries().size());

        TopicMaskBuilder builder = TopicMask.builder().addPatterns("Role", "LAB");
        TopicMask first = builder.build();
        NamingValidationException absentEntry = assertThrows(NamingValidationException.class,
                () -> builder.add(null));
        assertEquals(NamingOperation.MASK_BUILD, absentEntry.getOperation());
        assertEquals(1, absentEntry.getEntryIndex().orElseThrow());
        assertThrows(NamingValidationException.class, () -> builder.addPatterns("Role", "bad", "<"));
        assertThrows(NamingValidationException.class, () -> builder.addPatterns(null, "bad"));
        assertEquals(1, builder.build().getEntries().size());

        builder.addPatterns("Component", "Calliope");
        TopicMask second = builder.includeContainedElements(true).build();
        assertEquals(1, first.getEntries().size());
        assertEquals(2, second.getEntries().size());
        assertFalse(first.isIncludeContainedElements());
        assertTrue(second.isIncludeContainedElements());
        assertThrows(UnsupportedOperationException.class,
                () -> second.getEntries().add(TopicMaskEntry.any()));
        assertNotEquals(first, second);
    }

    @Test
    void maskEntryEqualityUsesRawPatternsAndConstraintPresence() {
        TopicMaskEntry numericA = TopicMaskEntry.of("Role", "LAB", "=01.002");
        TopicMaskEntry numericB = TopicMaskEntry.of("Role", "LAB", "=1.2");
        assertNotEquals(numericA, numericB);
        assertNotEquals(TopicMaskEntry.of("Role", "LAB"), TopicMaskEntry.of("Role", "LAB", ""));
        assertEquals(TopicMaskEntry.of("Role", "LAB"), TopicMaskEntry.of("Role", "LAB"));
        assertEquals("=01.002", numericA.getVersionConstraint().orElseThrow());
        assertNotEquals(TopicMask.builder().add(numericA).build(),
                TopicMask.builder().add(numericB).build());
    }

    private static Topic topic(String qualifier, String value, String version) {
        return Topic.builder().addEntry(qualifier, value, version).build();
    }
}