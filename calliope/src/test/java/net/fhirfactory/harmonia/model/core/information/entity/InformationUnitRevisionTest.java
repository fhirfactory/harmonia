/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import static net.fhirfactory.harmonia.model.core.information.entity.InformationMetadataTest.expectFailure;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.CacheLifecycleMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationQuality;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitRelationship;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.OriginMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.PersistenceLifecycleMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Relationships;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.SecurityMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.QualityGrade;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.RelationshipRole;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.RelationshipType;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import org.junit.jupiter.api.Test;

/** AC-3: governed revisions under one UUID and new IUs for transformation. */
class InformationUnitRevisionTest {
    private static final Topic SITE_A = InformationFixtures.topic("Location", "SiteA");
    private static final Topic SITE_B = InformationFixtures.topic("Location", "SiteB");
    private static final Topic OWNER_A = InformationFixtures.topic("Owner", "WardA");
    private static final Topic OWNER_B = InformationFixtures.topic("Owner", "WardB");

    @Test
    void coordinateChangeCreatesANewRevisionAndPreservesTheEarlierCoordinates() {
        InformationUnit first = InformationFixtures.unit()
                .reviseCoordinates(InformationCoordinates.of(null, null, SITE_A, OWNER_A))
                .withPersistenceLifecycle(PersistenceLifecycleMetadata.notEstablished().withManagedRevision(1)
                        .withLegalHold(true));
        InformationUnit relocated = first.reviseCoordinates(InformationCoordinates.of(null, null, SITE_B, OWNER_A));
        InformationUnit persisted = relocated.withPersistenceLifecycle(
                PersistenceLifecycleMetadata.notEstablished().withManagedRevision(2).withLegalHold(true));
        InformationUnit reowned = persisted.reviseCoordinates(InformationCoordinates.of(null, null, SITE_B, OWNER_B));

        assertEquals(SITE_A, first.getMetadata().getCoordinates().getInformationLocation().orElseThrow());
        assertEquals(SITE_B, relocated.getMetadata().getCoordinates().getInformationLocation().orElseThrow());
        assertEquals(OWNER_A, relocated.getMetadata().getCoordinates().getInformationOwner().orElseThrow());
        assertEquals(OWNER_B, reowned.getMetadata().getCoordinates().getInformationOwner().orElseThrow());
        assertEquals(first.getId(), reowned.getId());
        assertEquals(OptionalLong.of(1), first.getMetadata().getPersistenceLifecycle().getManagedRevision());
        assertEquals(OptionalLong.of(1), relocated.getMetadata().getPersistenceLifecycle().getManagedRevision(),
                "the origin revision is retained as the next write's expected revision");
        assertEquals(OptionalLong.of(2), persisted.getMetadata().getPersistenceLifecycle().getManagedRevision(),
                "Mnemosyne establishes the successor after the governed write succeeds");
        assertEquals(OptionalLong.of(2), reowned.getMetadata().getPersistenceLifecycle().getManagedRevision());
        assertTrue(relocated.getMetadata().getPersistenceLifecycle().isLegalHold());
    }

    @Test
    void authorityChangeCreatesANewSnapshotUnderTheSameUuidAndRetainsExpectedRevision() {
        Topic originalAuthority = InformationFixtures.authority();
        Topic revisedAuthority = InformationFixtures.topic("Authority", "RegionalRecords");
        InformationUnit source = InformationFixtures.unit().withPersistenceLifecycle(
                PersistenceLifecycleMetadata.notEstablished().withManagedRevision(8));

        InformationUnit revised = source.reviseInformationAuthority(revisedAuthority);
        InformationUnit reVersioned = source.reviseInformationAuthority(
                InformationFixtures.topic("Authority", "ClinicalRecords", "2"));

        assertEquals(source.getId(), revised.getId());
        assertEquals(originalAuthority, source.getMetadata().getInformationAuthority());
        assertEquals(revisedAuthority, revised.getMetadata().getInformationAuthority());
        assertEquals(OptionalLong.of(8), revised.getMetadata().getPersistenceLifecycle().getManagedRevision());
        assertEquals(OptionalLong.of(8), source.getMetadata().getPersistenceLifecycle().getManagedRevision());
        assertNotEquals(source, revised);
        assertNotEquals(source, reVersioned, "a Topic entry-version change is a governed authority change");
        assertSame(source, source.reviseInformationAuthority(originalAuthority),
                "re-stating an identical versioned authority is idempotent");
        expectFailure(InformationFailureCategory.MISSING_INPUT, "metadata.informationAuthority",
                () -> source.reviseInformationAuthority(null));
    }

    @Test
    void revisionPreservesRepresentationQualityOriginSecurityAndRelationships() {
        InformationUnit source = InformationFixtures.unit()
                .withOrigin(OriginMetadata.unknown().establishSourceSystem(InformationFixtures.topic("Src", "Lab")))
                .withSecurity(SecurityMetadata.of(List.of(InformationFixtures.topic("Sensitivity", "High"))));
        InformationUnit revised = source.reviseCoordinates(InformationCoordinates.of(null, null, SITE_B, OWNER_B));

        assertSame(source.getRepresentation(), revised.getRepresentation());
        assertSame(source.getQuality(), revised.getQuality());
        assertEquals(source.getMetadata().getOrigin(), revised.getMetadata().getOrigin());
        assertEquals(source.getMetadata().getSecurity(), revised.getMetadata().getSecurity());
        assertEquals(source.getMetadata().getInformationAuthority(),
                revised.getMetadata().getInformationAuthority());
        expectFailure(InformationFailureCategory.MISSING_INPUT, "coordinates", () -> source.reviseCoordinates(null));
    }

    @Test
    void cloneSuccessorCopiesDataAndMetadataPreservesLinksAndAddsProvenance() {
        InformationUnitRelationship existing = InformationUnitRelationship.toInformationUnit(
                RelationshipRole.PART_OF, UUID.fromString("f18a5c51-3b9e-4be4-a14e-5fa8fe58a123"));
        OriginMetadata origin = OriginMetadata.unknown()
                .establishSourceSystem(InformationFixtures.topic("Source", "Lab", "1.0"))
                .establishEntryComponent(InformationFixtures.topic("Component", "Ingress"))
                .establishEntryInstant(Instant.parse("2026-01-02T03:04:05Z"));
        SecurityMetadata security = SecurityMetadata.of(List.of(InformationFixtures.topic("Sensitivity", "High")));
        CacheLifecycleMetadata cache = CacheLifecycleMetadata.notCached()
                .withCacheState(InformationFixtures.topic("Cache", "Active"))
                .withActiveGeneration("cache-generation-7");
        InformationUnit source = InformationFixtures.unit()
                .withOrigin(origin)
                .withRelationships(Relationships.of(List.of(existing)))
                .withSecurity(security)
                .withPersistenceLifecycle(PersistenceLifecycleMetadata.notEstablished()
                        .withManagedRevision(3)
                        .withDisposition(InformationFixtures.topic("Disposition", "Archive"))
                        .withLegalHold(true))
                .withCacheLifecycle(cache);

        InformationUnit clone = source.deriveSuccessorAsClone(InformationFixtures.OTHER_ID,
                RelationshipRole.DERIVED_FROM);

        assertNotEquals(source.getId(), clone.getId());
        assertEquals(source.getInformationConcept(), clone.getInformationConcept());
        assertEquals(source.getDescription(), clone.getDescription());
        assertEquals(source.getQuality(), clone.getQuality());
        assertEquals(source.getRepresentation(), clone.getRepresentation());
        assertEquals(origin, clone.getMetadata().getOrigin());
        assertEquals(source.getMetadata().getCoordinates(), clone.getMetadata().getCoordinates());
        assertEquals(source.getMetadata().getInformationAuthority(), clone.getMetadata().getInformationAuthority());
        assertEquals(security, clone.getMetadata().getSecurity());

        List<InformationUnitRelationship> cloneRelationships =
                clone.getMetadata().getRelationships().getRelationships();
        assertEquals(2, cloneRelationships.size());
        assertEquals(existing, cloneRelationships.get(0));
        InformationUnitRelationship provenance = cloneRelationships.get(1);
        assertEquals(RelationshipType.INTERNAL_IU, provenance.getType());
        assertEquals(RelationshipRole.DERIVED_FROM, provenance.getRole());
        assertEquals(source.getId(), provenance.getTargetInformationUnitId().orElseThrow());

        assertEquals(OptionalLong.empty(), clone.getMetadata().getPersistenceLifecycle().getManagedRevision());
        assertTrue(clone.getMetadata().getPersistenceLifecycle().getDisposition().isEmpty());
        assertFalse(clone.getMetadata().getPersistenceLifecycle().isLegalHold());
        assertTrue(clone.getMetadata().getCacheLifecycle().getCacheState().isEmpty());
        assertTrue(clone.getMetadata().getCacheLifecycle().getActiveGeneration().isEmpty());
        assertEquals(OptionalLong.of(3), source.getMetadata().getPersistenceLifecycle().getManagedRevision());
        assertEquals("cache-generation-7", source.getMetadata().getCacheLifecycle()
                .getActiveGeneration().orElseThrow(), "the source snapshot is unchanged");

        InformationUnit rebuilt = InformationUnit.builder().id(clone.getId())
                .informationConcept(clone.getInformationConcept()).description(clone.getDescription())
                .quality(clone.getQuality()).metadata(clone.getMetadata()).representation(clone.getRepresentation())
                .build();
        assertEquals(clone, rebuilt);
    }

    @Test
    void minimumMetadataSuccessorInheritsOnlyOriginAndSecurityAndUsesCallerValues() {
        InformationUnitRelationship existing = InformationUnitRelationship.toInformationUnit(
                RelationshipRole.PART_OF, UUID.fromString("f18a5c51-3b9e-4be4-a14e-5fa8fe58a123"));
        OriginMetadata origin = OriginMetadata.unknown()
                .establishSourceSystem(InformationFixtures.topic("Source", "Registry", "2"));
        SecurityMetadata security = SecurityMetadata.of(List.of(InformationFixtures.topic("Sensitivity", "High")));
        InformationUnit source = InformationFixtures.unit()
                .withOrigin(origin)
                .withSecurity(security)
                .withRelationships(Relationships.of(List.of(existing)))
                .withPersistenceLifecycle(PersistenceLifecycleMetadata.notEstablished().withManagedRevision(5)
                        .withDisposition(InformationFixtures.topic("Disposition", "Archive")).withLegalHold(true))
                .withCacheLifecycle(CacheLifecycleMetadata.notCached().withActiveGeneration("active-source"));
        InformationCoordinates coordinates = InformationCoordinates.of(null, null, SITE_B, OWNER_B);
        InformationQuality quality = InformationQuality.of("validation-service", QualityGrade.CANONICAL,
                Map.of("validator", "profile-check"));
        InformationUnit expected = source.deriveSuccessorWithMinimumMetadata(InformationFixtures.OTHER_ID,
                        RelationshipRole.DERIVED_FROM)
                .informationConcept(InformationFixtures.topic("Concept", "Validated"))
                .description("validated synthetic information")
                .quality(quality)
                .informationAuthority(InformationFixtures.topic("Authority", "RegionalRecords"))
                .coordinates(coordinates)
                .representation(source.getRepresentation())
                .build();

        assertEquals(InformationFixtures.OTHER_ID, expected.getId());
        assertEquals("validated synthetic information", expected.getDescription());
        assertEquals(quality, expected.getQuality());
        assertEquals(InformationFixtures.topic("Concept", "Validated"), expected.getInformationConcept());
        assertEquals(InformationFixtures.topic("Authority", "RegionalRecords"),
                expected.getMetadata().getInformationAuthority());
        assertEquals(coordinates, expected.getMetadata().getCoordinates());
        assertEquals(source.getRepresentation(), expected.getRepresentation());
        assertEquals(origin, expected.getMetadata().getOrigin());
        assertEquals(security, expected.getMetadata().getSecurity());
        assertEquals(List.of(InformationUnitRelationship.toInformationUnit(RelationshipRole.DERIVED_FROM,
                        source.getId())), expected.getMetadata().getRelationships().getRelationships(),
                "the successor receives provenance only, not the source's other relationships");
        assertEquals(OptionalLong.empty(), expected.getMetadata().getPersistenceLifecycle().getManagedRevision());
        assertTrue(expected.getMetadata().getPersistenceLifecycle().getDisposition().isEmpty());
        assertFalse(expected.getMetadata().getPersistenceLifecycle().isLegalHold());
        assertTrue(expected.getMetadata().getCacheLifecycle().getActiveGeneration().isEmpty());
        assertEquals(List.of(existing), source.getMetadata().getRelationships().getRelationships());
        assertEquals(OptionalLong.of(5), source.getMetadata().getPersistenceLifecycle().getManagedRevision());
        assertEquals("active-source", source.getMetadata().getCacheLifecycle()
                .getActiveGeneration().orElseThrow());
    }

    @Test
    void derivationRejectsReusedIdsMissingArgumentsAndCloneSelfReferences() {
        InformationUnit source = InformationFixtures.unit();

        expectFailure(InformationFailureCategory.INCONSISTENT_INPUT, "derivation.id",
                () -> source.deriveSuccessorAsClone(source.getId(), RelationshipRole.DERIVED_FROM));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "derivation.id",
                () -> source.deriveSuccessorAsClone(null, RelationshipRole.DERIVED_FROM));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "derivation.role",
                () -> source.deriveSuccessorAsClone(InformationFixtures.OTHER_ID, null));
        InformationUnit selfReferencingSource = source.withRelationships(Relationships.of(List.of(
                InformationUnitRelationship.toInformationUnit(RelationshipRole.PART_OF,
                        InformationFixtures.OTHER_ID))));
        expectFailure(InformationFailureCategory.INCONSISTENT_INPUT, "derivation.id",
                () -> selfReferencingSource.deriveSuccessorAsClone(InformationFixtures.OTHER_ID,
                        RelationshipRole.DERIVED_FROM));

        expectFailure(InformationFailureCategory.INCONSISTENT_INPUT, "derivation.id",
                () -> source.deriveSuccessorWithMinimumMetadata(source.getId(), RelationshipRole.DERIVED_FROM));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "derivation.id",
                () -> source.deriveSuccessorWithMinimumMetadata(null, RelationshipRole.DERIVED_FROM));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "derivation.role",
                () -> source.deriveSuccessorWithMinimumMetadata(InformationFixtures.OTHER_ID, null));
    }

    @Test
    void minimumMetadataBuilderReportsEachMissingFieldAndRetainsInputsAfterFailure() {
        InformationUnit source = InformationFixtures.unit();
        MinimumMetadataSuccessorBuilder builder = source.deriveSuccessorWithMinimumMetadata(
                InformationFixtures.OTHER_ID, RelationshipRole.DERIVED_FROM);

        expectFailure(InformationFailureCategory.MISSING_INPUT, "informationConcept", builder::build);
        builder.informationConcept(source.getInformationConcept());
        expectFailure(InformationFailureCategory.MISSING_INPUT, "description", builder::build);
        builder.description("derived description");
        expectFailure(InformationFailureCategory.MISSING_INPUT, "quality", builder::build);
        builder.quality(source.getQuality());
        expectFailure(InformationFailureCategory.MISSING_INPUT, "informationAuthority", builder::build);
        builder.informationAuthority(source.getMetadata().getInformationAuthority());
        expectFailure(InformationFailureCategory.MISSING_INPUT, "representation", builder::build);
        builder.representation(source.getRepresentation());
        assertEquals(source.getId(), builder.build().getMetadata().getRelationships().getRelationships().get(0)
                .getTargetInformationUnitId().orElseThrow(), "the failed builds did not discard prior inputs");

        Set<String> forbiddenSetters = Set.of("metadata", "origin", "relationships", "security");
        Set<String> publicForbiddenMethods = Arrays.stream(MinimumMetadataSuccessorBuilder.class.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(Method::getName).filter(forbiddenSetters::contains).collect(Collectors.toSet());
        assertEquals(Set.of(), publicForbiddenMethods,
                "inherited origin, security and provenance cannot be replaced through the builder");
    }

    @Test
    void noPublicOperationReplacesRepresentationQualityConceptOrIdentityUnderTheSameUuid() {
        Set<Class<?>> protectedTypes = Set.of(InformationRepresentation.class, InformationQuality.class,
                Topic.class, java.util.UUID.class, ContentMetadata.class, Content.class);
        List<String> offenders = Arrays.stream(InformationUnit.class.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getReturnType() == InformationUnit.class)
                .filter(method -> !Set.of("reviseInformationAuthority", "deriveSuccessorAsClone")
                        .contains(method.getName()))
                .filter(method -> Arrays.stream(method.getParameterTypes()).anyMatch(protectedTypes::contains))
                .map(Method::getName)
                .toList();

        assertEquals(List.of(), offenders);
        Set<String> snapshotOperations = Arrays.stream(InformationUnit.class.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getReturnType() == InformationUnit.class)
                .map(Method::getName).collect(Collectors.toSet());
        assertEquals(Set.of("reviseCoordinates", "reviseInformationAuthority", "deriveSuccessorAsClone",
                "withPersistenceLifecycle", "withCacheLifecycle", "withOrigin", "withSecurity",
                "withRelationships", "withDescription"),
                snapshotOperations);
        assertFalse(Arrays.stream(InformationUnit.class.getDeclaredMethods())
                .anyMatch(method -> method.getName().equals("deriveSuccessor")));
        assertFalse(Arrays.stream(InformationRepresentation.class.getMethods())
                .anyMatch(method -> method.getName().startsWith("with")));
        assertFalse(Arrays.stream(InformationQuality.class.getMethods())
                .anyMatch(method -> method.getName().startsWith("with")));
    }

    @Test
    void rejectedChangesLeaveTheOriginalSnapshotIntact() {
        InformationUnit source = InformationFixtures.unit()
                .withPersistenceLifecycle(PersistenceLifecycleMetadata.notEstablished().withManagedRevision(5));
        InformationUnit copy = source.withDescription(source.getDescription());

        expectFailure(InformationFailureCategory.IMMUTABILITY_VIOLATION, "persistence.managedRevision",
                () -> source.withPersistenceLifecycle(PersistenceLifecycleMetadata.notEstablished()
                        .withManagedRevision(4)));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "description", () -> source.withDescription(null));
        assertEquals(copy, source);
    }
}
