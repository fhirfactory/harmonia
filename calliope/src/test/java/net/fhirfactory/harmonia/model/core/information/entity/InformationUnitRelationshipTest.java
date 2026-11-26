/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import static net.fhirfactory.harmonia.model.core.information.entity.InformationMetadataTest.expectFailure;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitRelationship;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Relationships;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.RelationshipRole;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.RelationshipType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** AC-2: typed relationships, URI targets, UUID-only IU targets and list immutability. */
class InformationUnitRelationshipTest {
    private static final String IU_TARGET = "urn:uuid:7c9e6679-7425-40de-944b-e07fc1f90ae7";

    @Test
    void adjudicatedTypeAndRoleValuesAreExactlyThoseOfDraftSection14() {
        assertEquals(List.of("EXTERNAL_SYSTEM", "EXTERNAL_ORGANISATION", "INTERNAL_IU", "INTERNAL_OTHER"),
                Arrays.stream(RelationshipType.values()).map(Enum::name).toList());
        assertEquals(List.of("BASED_ON", "ALTERNATE_REPRESENTATION", "PART_OF", "DERIVED_FROM"),
                Arrays.stream(RelationshipRole.values()).map(Enum::name).toList());
    }

    @ParameterizedTest
    @EnumSource(RelationshipRole.class)
    void everyRoleCanBeUsedWithEveryType(RelationshipRole role) {
        List<InformationUnitRelationship> links = List.of(
                InformationUnitRelationship.parse(RelationshipType.EXTERNAL_SYSTEM, role,
                        "https://lab.example.org/fhir/Observation/123"),
                InformationUnitRelationship.parse(RelationshipType.EXTERNAL_ORGANISATION, role,
                        "urn:oid:1.2.36.1.2001.1003.0.8003621566684455"),
                InformationUnitRelationship.parse(RelationshipType.INTERNAL_IU, role, IU_TARGET),
                InformationUnitRelationship.parse(RelationshipType.INTERNAL_OTHER, role,
                        "urn:harmonia:pathway:abc"));
        InformationUnit unit = InformationFixtures.unit().withRelationships(Relationships.of(links));

        assertEquals(links, unit.getMetadata().getRelationships().getRelationships());
        links.forEach(link -> assertEquals(role, link.getRole()));
    }

    @Test
    void urnTargetsRoundTripThroughTheirStringRepresentation() {
        InformationUnitRelationship parsed = InformationUnitRelationship.parse(RelationshipType.EXTERNAL_SYSTEM,
                RelationshipRole.BASED_ON, "urn:oid:2.16.840.1.113883.19.5");
        InformationUnitRelationship again = InformationUnitRelationship.parse(parsed.getType(), parsed.getRole(),
                parsed.getTargetText());

        assertEquals("urn:oid:2.16.840.1.113883.19.5", parsed.getTargetText());
        assertEquals(URI.create("urn:oid:2.16.840.1.113883.19.5"), parsed.getTarget());
        assertEquals(parsed, again);
        assertEquals(Optional.empty(), parsed.getTargetInformationUnitId());
    }

    @Test
    void internalIuTargetIdentifiesTheUnitByUuid() {
        InformationUnitRelationship link = InformationUnitRelationship.toInformationUnit(
                RelationshipRole.PART_OF, InformationFixtures.OTHER_ID);

        assertEquals(IU_TARGET, link.getTargetText());
        assertEquals(InformationFixtures.OTHER_ID, link.getTargetInformationUnitId().orElseThrow());
        InformationUnitRelationship upperCase = InformationUnitRelationship.parse(RelationshipType.INTERNAL_IU,
                RelationshipRole.PART_OF, "urn:uuid:" + InformationFixtures.OTHER_ID.toString().toUpperCase());
        assertEquals(InformationFixtures.OTHER_ID, upperCase.getTargetInformationUnitId().orElseThrow());
        assertEquals("urn:uuid:7C9E6679-7425-40DE-944B-E07FC1F90AE7", upperCase.getTargetText(),
                "the supplied spelling is preserved");
    }

    @Test
    void classificationDerivedNameIsInsufficientAsAnInternalIuTarget() {
        InformationUnit target = InformationFixtures.unit();

        expectFailure(InformationFailureCategory.INVALID_INPUT, "relationship.target",
                () -> InformationUnitRelationship.parse(RelationshipType.INTERNAL_IU, RelationshipRole.BASED_ON,
                        "urn:harmonia:" + target.getName()));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "relationship.target",
                () -> InformationUnitRelationship.parse(RelationshipType.INTERNAL_IU, RelationshipRole.BASED_ON,
                        "urn:uuid:not-a-uuid"));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "relationship.target",
                () -> InformationUnitRelationship.parse(RelationshipType.INTERNAL_IU, RelationshipRole.BASED_ON,
                        "urn:uuid:7c9e6679-7425-40de-944b-e07fc1f90ae7-extra"));
    }

    @Test
    void relativeMalformedAndMissingTargetsAreRejected() {
        expectFailure(InformationFailureCategory.INVALID_INPUT, "relationship.target",
                () -> InformationUnitRelationship.parse(RelationshipType.EXTERNAL_SYSTEM, RelationshipRole.BASED_ON,
                        "Observation/123"));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "relationship.target",
                () -> InformationUnitRelationship.parse(RelationshipType.EXTERNAL_SYSTEM, RelationshipRole.BASED_ON,
                        "http://exa mple.org"));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "relationship.target",
                () -> InformationUnitRelationship.of(RelationshipType.EXTERNAL_SYSTEM, RelationshipRole.BASED_ON,
                        null));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "relationship.type",
                () -> InformationUnitRelationship.parse(null, RelationshipRole.BASED_ON, IU_TARGET));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "relationship.role",
                () -> InformationUnitRelationship.parse(RelationshipType.INTERNAL_IU, null, IU_TARGET));
    }

    @Test
    void remoteTargetsArePreservedWithoutResolutionOrReciprocalLinks() {
        InformationUnitRelationship remote = InformationUnitRelationship.parse(RelationshipType.EXTERNAL_SYSTEM,
                RelationshipRole.BASED_ON, "https://unreachable.invalid/never/resolved?x=1#frag");
        InformationUnit source = InformationFixtures.unit()
                .withRelationships(Relationships.none().with(remote));

        assertEquals("https://unreachable.invalid/never/resolved?x=1#frag", remote.getTargetText());
        assertEquals(1, source.getMetadata().getRelationships().getRelationships().size());
        assertTrue(InformationFixtures.unit().getMetadata().getRelationships().getRelationships().isEmpty(),
                "no reciprocal or implicit links are created");
    }

    @Test
    void semanticLinksAreSeparateFromTheContentLocator() {
        URI locator = URI.create("s3://bucket/object-1");
        InformationUnitRelationship link = InformationUnitRelationship.of(RelationshipType.EXTERNAL_SYSTEM,
                RelationshipRole.ALTERNATE_REPRESENTATION, locator);
        Content content = Content.reference(locator);

        assertNotEquals(link.getTarget().toString(), content.toString(), "toString never exposes the locator");
        assertFalse(link.toString().contains("bucket"));
        assertEquals(locator, content.getReference().orElseThrow());
    }

    @Test
    void relationshipListIsCopiedOnInputAndUnmodifiableOnOutput() {
        List<InformationUnitRelationship> supplied = new ArrayList<>();
        supplied.add(InformationUnitRelationship.toInformationUnit(RelationshipRole.DERIVED_FROM,
                InformationFixtures.OTHER_ID));
        Relationships relationships = Relationships.of(supplied);
        supplied.clear();

        assertEquals(1, relationships.getRelationships().size());
        assertThrows(UnsupportedOperationException.class, () -> relationships.getRelationships().clear());
        Relationships extended = relationships.with(relationships.getRelationships().get(0));
        assertEquals(1, relationships.getRelationships().size(), "with() leaves the original unchanged");
        assertEquals(2, extended.getRelationships().size(), "duplicates are preserved as supplied");

        List<InformationUnitRelationship> withNull = new ArrayList<>();
        withNull.add(null);
        expectFailure(InformationFailureCategory.MISSING_INPUT, "relationships", () -> Relationships.of(withNull));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "relationships",
                () -> InformationFixtures.unit().withRelationships(null));
    }
}
