/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Exercises TopicBuilder modes, recoverability, snapshots, and field limits. */
class TopicBuilderTest {

    @Test
    void failedFirstAddDoesNotSelectModeAndIndexedGapsCanBeRepaired() {
        TopicBuilder builder = Topic.builder();
        assertThrows(NamingValidationException.class, () -> builder.addEntry("System", ""));

        builder.addEntry(2, "Layer", "leaf");
        assertThrows(NamingValidationException.class, builder::build);
        builder.addEntry(0, "System", "Harmonia");
        builder.addEntry(1, "", "Component");

        Topic built = builder.build();
        assertEquals("System=Harmonia.N1=Component.Layer=leaf", built.getCanonicalName());
    }

    @Test
    void invalidReplacementAndMixedModeLeaveBuilderAndPublishedTopicUnchanged() {
        TopicBuilder builder = Topic.builder();
        builder.addEntry(0, "System", "Harmonia");
        Topic snapshot = builder.build();
        assertThrows(NamingValidationException.class, () -> builder.addEntry(0, "System", ""));
        assertThrows(NamingValidationException.class, () -> builder.addEntry("Component", "Calliope"));

        assertEquals("System=Harmonia", snapshot.getCanonicalName());
        assertEquals(snapshot, builder.build());
        assertThrows(UnsupportedOperationException.class,
                () -> snapshot.getEntries().add(snapshot.getEntries().get(0)));
    }

    @Test
    void indexedReplacementPreservesDepthAndGeneratedQualifierMatchesExplicitLiteral() {
        TopicBuilder indexed = Topic.builder();
        indexed.addEntry(2, "Layer", "leaf");
        indexed.addEntry(0, "System", "Harmonia");
        indexed.addEntry(1, null, "Mneme");
        Topic generated = indexed.build();
        Topic explicit = Topic.builder().addEntry("System", "Harmonia")
                .addEntry("N1", "Mneme").addEntry("Layer", "leaf").build();

        assertEquals(explicit, generated);
        assertEquals(0, generated.compareTo(explicit));
        assertEquals("System=Harmonia.N1=Mneme.Layer=leaf", generated.getCanonicalName());
        assertTrue(TopicMask.builder().addPatterns("System", "Harmonia")
                .addPatterns("N1", "Mneme").includeContainedElements(true).build().matches(generated));
        assertEquals(generated, NamingXmlCodec.readTopic(NamingXmlCodec.writeTopic(generated)));
        indexed.addEntry(1, "Component", "Calliope", "2.0");
        Topic replaced = indexed.build();
        assertEquals(3, replaced.getDepth());
        assertEquals("2.0", replaced.getEntries().get(1).getVersion().orElseThrow());
        assertEquals("N1", generated.getEntries().get(1).getQualifier());
    }

    @Test
    void sequentialModeCannotBeChangedByIndexedInput() {
        TopicBuilder sequential = Topic.builder().addEntry("System", "Harmonia");
        Topic snapshot = sequential.build();

        assertThrows(NamingValidationException.class,
                () -> sequential.addEntry(0, "System", "Replacement"));
        assertEquals(snapshot, sequential.build());
        sequential.addEntry("Component", "Calliope");
        assertEquals(2, sequential.build().getDepth());
    }

    @Test
    void indexedModeCannotBeChangedBySequentialInput() {
        TopicBuilder indexed = Topic.builder().addEntry(0, "System", "Harmonia");
        Topic snapshot = indexed.build();

        assertThrows(NamingValidationException.class, () -> indexed.addEntry("Component", "Calliope"));
        assertEquals(snapshot, indexed.build());
        indexed.addEntry(1, "Component", "Calliope");
        assertEquals(2, indexed.build().getDepth());
    }

    @Test
    void fieldsAreValidatedWithoutTrimmingAndCodePointLimitsAreApplied() {
        NamingValidationException blank = assertThrows(NamingValidationException.class,
                () -> Topic.builder().addEntry("System", "   "));
        assertEquals(NamingFailureCategory.INVALID_FIELD, blank.getCategory());
        assertTrue(blank.getMessage().contains("VALUE"));
        assertThrows(NamingValidationException.class, () -> Topic.builder().addEntry(" System", "x"));
        assertThrows(NamingValidationException.class, () -> Topic.builder().addEntry("System", "x\u00a0y"));

        String exactValue = "😀".repeat(NamingLimits.MAX_VALUE_CODE_POINTS);
        assertEquals(exactValue, Topic.builder().addEntry("Symbol", exactValue).build()
                .getEntries().get(0).getValue());
        NamingValidationException tooLong = assertThrows(NamingValidationException.class,
                () -> Topic.builder().addEntry("Symbol", exactValue + "😀"));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, tooLong.getCategory());
        assertThrows(NamingValidationException.class,
                () -> Topic.builder().addEntry("System", "bad\uD800"));
        for (String rejected : new String[]{"a\u0000b", "a\u200Db", "a\u2028b", "a\uE000b", "a\u0378b"}) {
            assertThrows(NamingValidationException.class,
                    () -> Topic.builder().addEntry("System", rejected), rejected);
        }
    }

    @Test
    void qualifierAndVersionLimitsAndPrintableAsciiRulesAreEnforced() {
        String qualifier = "Q".repeat(NamingLimits.MAX_QUALIFIER_CODE_POINTS);
        String version = "V".repeat(NamingLimits.MAX_VERSION_CODE_POINTS);
        Topic accepted = Topic.builder().addEntry(qualifier, "Value", version).build();
        assertEquals(qualifier, accepted.getEntries().get(0).getQualifier());
        assertEquals(version, accepted.getEntries().get(0).getVersion().orElseThrow());

        assertThrows(NamingValidationException.class,
                () -> Topic.builder().addEntry(qualifier + "Q", "Value"));
        assertThrows(NamingValidationException.class,
                () -> Topic.builder().addEntry("Role", "Value", version + "V"));
        assertThrows(NamingValidationException.class, () -> Topic.builder().addEntry("Role\u007f", "Value"));
        assertThrows(NamingValidationException.class, () -> Topic.builder().addEntry("Role", "Value", "V\u00e9"));
    }

    @Test
    void indexedAndSequentialDepthAreBoundedBeforeStorageGrowth() {
        TopicBuilder indexed = Topic.builder();
        NamingValidationException sparse = assertThrows(NamingValidationException.class,
                () -> indexed.addEntry(NamingLimits.MAX_DEPTH, "System", "Harmonia"));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, sparse.getCategory());
        for (int index = 0; index < NamingLimits.MAX_DEPTH; index++) {
            indexed.addEntry(index, "N" + index, "value" + index);
        }
        assertEquals(NamingLimits.MAX_DEPTH, indexed.build().getDepth());
        assertThrows(NamingValidationException.class,
                () -> indexed.addEntry(NamingLimits.MAX_DEPTH, "N", "overflow"));

        TopicBuilder sequential = Topic.builder();
        for (int index = 0; index < NamingLimits.MAX_DEPTH; index++) {
            sequential.addEntry("N" + index, "value" + index);
        }
        assertThrows(NamingValidationException.class,
                () -> sequential.addEntry("overflow", "too many"));
    }

    @Test
    void successfulBuildsAreIndependentReusableSnapshots() {
        TopicBuilder builder = Topic.builder().addEntry("System", "Harmonia");
        Topic first = builder.build();
        builder.addEntry("Component", "Calliope");
        Topic second = builder.build();
        List<String> firstValues = new ArrayList<>();
        first.getEntries().forEach(entry -> firstValues.add(entry.getValue()));

        assertEquals(List.of("Harmonia"), firstValues);
        assertEquals(1, first.getDepth());
        assertEquals(2, second.getDepth());
    }
}