/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Consumer;
import java.util.stream.Stream;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationModelException;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** AC-1: UUID identity, exact name projection and MI-05 concept matching. */
class InformationUnitIdentityTest {

    @Test
    void nameConcatenatesRawConceptValuesAndUuidWithoutSeparatorQualifiersOrVersions() {
        InformationUnit unit = InformationFixtures.unit();

        assertEquals("Harmonia.Observation0f8fad5b-d9cb-469f-a165-70867728950e", unit.getName());
        assertEquals(unit.getInformationConcept().getName() + unit.getId(), unit.getName());
    }

    @Test
    void conceptRetainsItsQualifiedPathAndEntryVersions() {
        Topic concept = InformationFixtures.unit().getInformationConcept();

        assertEquals("System=Harmonia.Concept=Observation", concept.getCanonicalName());
        assertEquals("System:1=Harmonia.Concept:2\\.0=Observation", concept.getCanonicalNameWithVersion());
    }

    @Test
    void uuidIsStableAcrossRevisionsAndIsTheOnlyIdentity() {
        InformationUnit original = InformationFixtures.unit();
        InformationUnit revised = original.reviseCoordinates(InformationCoordinates.of(null, null,
                InformationFixtures.topic("Location", "SiteB"), null)).withDescription("renamed");

        assertEquals(original.getId(), revised.getId());
        assertEquals(original.getName(), revised.getName());
        assertNotEquals(original, revised, "snapshots differ even though identity is the same");
    }

    @Test
    void qualifiedConceptIsTheMi05MatchValueForSupportedTopicMasks() {
        Topic concept = InformationFixtures.unit().getInformationConcept();
        TopicMask exact = TopicMask.builder().addLiterals("System", "Harmonia")
                .addLiterals("Concept", "Observation").build();
        TopicMask family = TopicMask.builder().addLiterals("System", "Harmonia").includeContainedElements(true)
                .build();
        TopicMask wrongQualifier = TopicMask.builder().addLiterals("System", "Harmonia")
                .addLiterals("Kind", "Observation").build();

        assertTrue(exact.matches(concept));
        assertTrue(family.matches(concept));
        assertFalse(wrongQualifier.matches(concept), "qualifiers matter, so the raw name is insufficient");
    }

    @Test
    void builderReturnsEqualIndependentSnapshotsAndToStringRevealsOnlyTheUuid() {
        InformationUnitBuilder builder = InformationFixtures.builder();
        InformationUnit first = builder.build();
        InformationUnit second = builder.build();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals("InformationUnit{id=0f8fad5b-d9cb-469f-a165-70867728950e}", first.toString());
    }

    @Test
    void conceptEntryVersionIsPartOfSnapshotEqualityButNotOfTheName() {
        InformationUnit versioned = InformationFixtures.unit();
        InformationUnit unversioned = InformationFixtures.builder()
                .informationConcept(Topic.fromCanonicalName("System=Harmonia.Concept=Observation")).build();

        assertEquals(versioned.getName(), unversioned.getName());
        assertEquals(versioned.getInformationConcept(), unversioned.getInformationConcept());
        assertNotEquals(versioned, unversioned);
    }

    @Test
    void emptyDescriptionIsPermitted() {
        assertEquals("", InformationFixtures.builder().description("").build().getDescription());
    }

    static Stream<Arguments> missingFields() {
        return Stream.of(
                Arguments.of("id", (Consumer<InformationUnitBuilder>) b -> b.id(null)),
                Arguments.of("informationConcept", (Consumer<InformationUnitBuilder>) b -> b.informationConcept(null)),
                Arguments.of("description", (Consumer<InformationUnitBuilder>) b -> b.description(null)),
                Arguments.of("quality", (Consumer<InformationUnitBuilder>) b -> b.quality(null)),
                Arguments.of("metadata", (Consumer<InformationUnitBuilder>) b -> b.metadata(null)),
                Arguments.of("representation", (Consumer<InformationUnitBuilder>) b -> b.representation(null)));
    }

    @ParameterizedTest
    @MethodSource("missingFields")
    void failedBuildNamesTheMissingFieldAndPreservesOtherInputsForRetry(String field,
                                                                          Consumer<InformationUnitBuilder> clear) {
        InformationUnitBuilder builder = InformationFixtures.builder();
        clear.accept(builder);

        InformationModelException failure = assertThrows(InformationModelException.class, builder::build);
        assertEquals(InformationFailureCategory.MISSING_INPUT, failure.getCategory());
        assertEquals(field, failure.getField());

        InformationUnit reference = InformationFixtures.unit();
        InformationUnitBuilder corrected = switch (field) {
            case "id" -> builder.id(reference.getId());
            case "informationConcept" -> builder.informationConcept(reference.getInformationConcept());
            case "description" -> builder.description(reference.getDescription());
            case "quality" -> builder.quality(reference.getQuality());
            case "metadata" -> builder.metadata(reference.getMetadata());
            default -> builder.representation(reference.getRepresentation());
        };
        assertEquals(reference, corrected.build());
    }

    @Test
    void exceptionMessagesCarryOnlyCategoryAndStructuralField() {
        InformationModelException failure = assertThrows(InformationModelException.class,
                () -> InformationFixtures.builder().description(null).build());

        assertEquals("Information model MISSING_INPUT at field description", failure.getMessage());
        assertThrows(IllegalArgumentException.class,
                () -> InformationModelException.of(InformationFailureCategory.INVALID_INPUT, "Jane Citizen"));
    }

    @Test
    void initialMetadataContainersAreSharedImmutableDefaults() {
        InformationUnit first = InformationFixtures.unit();
        InformationUnit second = InformationFixtures.builder().id(InformationFixtures.OTHER_ID).build();

        assertSame(InformationCoordinates.unplaced(), first.getMetadata().getCoordinates());
        assertSame(first.getMetadata().getCoordinates(), second.getMetadata().getCoordinates());
    }
}
