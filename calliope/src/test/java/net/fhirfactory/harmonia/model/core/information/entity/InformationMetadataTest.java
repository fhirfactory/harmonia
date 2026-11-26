/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.CacheLifecycleMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationModelException;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.OriginMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.PersistenceLifecycleMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.SecurityMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/** AC-2: initialised containers, lifecycle authority separation, origin and security rules. */
class InformationMetadataTest {
    private static final Topic SOURCE = InformationFixtures.topic("SourceSystem", "LabSystem", "3");
    private static final Topic ENTRY = InformationFixtures.topic("Component", "MllpIngress");
    private static final Instant ENTERED = Instant.parse("2026-01-02T03:04:05Z");

    @Test
    void everyContainerIsInitialisedAndTheAuthorityIsExplicit() {
        InformationMetadata metadata = InformationFixtures.unit().getMetadata();

        assertEquals(OptionalLong.empty(), metadata.getPersistenceLifecycle().getManagedRevision());
        assertFalse(metadata.getPersistenceLifecycle().isLegalHold());
        assertTrue(metadata.getPersistenceLifecycle().getDisposition().isEmpty());
        assertTrue(metadata.getCacheLifecycle().getActiveGeneration().isEmpty());
        assertTrue(metadata.getCacheLifecycle().getCacheState().isEmpty());
        assertTrue(metadata.getOrigin().getSourceSystem().isEmpty());
        assertTrue(metadata.getCoordinates().getInformationOwner().isEmpty());
        assertTrue(metadata.getSecurity().getRequirements().isEmpty());
        assertTrue(metadata.getRelationships().getRelationships().isEmpty());
        assertEquals(InformationFixtures.authority(), metadata.getInformationAuthority());
        expectFailure(InformationFailureCategory.MISSING_INPUT, "metadata.informationAuthority",
                () -> InformationMetadata.initial(null));
    }

    @Test
    void managedRevisionAndActiveGenerationAreIndependentAxes() {
        InformationUnit unit = InformationFixtures.unit()
                .withPersistenceLifecycle(PersistenceLifecycleMetadata.notEstablished().withManagedRevision(7))
                .withCacheLifecycle(CacheLifecycleMetadata.notCached().withActiveGeneration("gen-42"));

        assertEquals(OptionalLong.of(7), unit.getMetadata().getPersistenceLifecycle().getManagedRevision());
        assertEquals("gen-42", unit.getMetadata().getCacheLifecycle().getActiveGeneration().orElseThrow());

        InformationUnit recached = unit.withCacheLifecycle(CacheLifecycleMetadata.notCached()
                .withActiveGeneration("gen-43"));
        assertEquals(OptionalLong.of(7), recached.getMetadata().getPersistenceLifecycle().getManagedRevision(),
                "changing the cache manifestation must not change the authoritative revision");
    }

    @Test
    void managedRevisionMustBePositiveAndNeverRegressesOrDisappears() {
        PersistenceLifecycleMetadata seven = PersistenceLifecycleMetadata.notEstablished().withManagedRevision(7);
        InformationUnit unit = InformationFixtures.unit().withPersistenceLifecycle(seven);

        expectFailure(InformationFailureCategory.INVALID_INPUT, "persistence.managedRevision",
                () -> seven.withManagedRevision(0));
        expectFailure(InformationFailureCategory.IMMUTABILITY_VIOLATION, "persistence.managedRevision",
                () -> seven.withManagedRevision(6));
        expectFailure(InformationFailureCategory.IMMUTABILITY_VIOLATION, "persistence.managedRevision",
                () -> unit.withPersistenceLifecycle(PersistenceLifecycleMetadata.notEstablished()));
        assertEquals(OptionalLong.of(7), seven.withManagedRevision(7).getManagedRevision(), "idempotent");
        assertEquals(OptionalLong.of(8), unit.withPersistenceLifecycle(seven.withManagedRevision(8))
                .getMetadata().getPersistenceLifecycle().getManagedRevision());
    }

    @Test
    void dispositionAndLegalHoldAreRecordedWithoutChangingTheRevision() {
        Topic retain = InformationFixtures.topic("Disposition", "Retain7Years");
        PersistenceLifecycleMetadata held = PersistenceLifecycleMetadata.notEstablished().withManagedRevision(2)
                .withDisposition(retain).withLegalHold(true);

        assertEquals(retain, held.getDisposition().orElseThrow());
        assertTrue(held.isLegalHold());
        assertEquals(OptionalLong.of(2), held.getManagedRevision());
        assertTrue(held.isLegalHold());
    }

    @Test
    void activeGenerationIsOpaqueButBounded() {
        expectFailure(InformationFailureCategory.INVALID_INPUT, "cache.activeGeneration",
                () -> CacheLifecycleMetadata.notCached().withActiveGeneration(" "));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "cache.activeGeneration",
                () -> CacheLifecycleMetadata.notCached().withActiveGeneration("g".repeat(257)));
        assertTrue(CacheLifecycleMetadata.notCached().withActiveGeneration("g").withActiveGeneration(null)
                .getActiveGeneration().isEmpty());
    }

    @Test
    void unknownOriginFactsCanBeEstablishedOnceIncludingForInternallyGeneratedInformation() {
        OriginMetadata origin = OriginMetadata.unknown().establishSourceSystem(SOURCE)
                .establishEntryComponent(ENTRY).establishEntryInstant(ENTERED);
        InformationUnit unit = InformationFixtures.unit().withOrigin(origin);

        assertEquals(SOURCE, unit.getMetadata().getOrigin().getSourceSystem().orElseThrow());
        assertEquals(ENTRY, unit.getMetadata().getOrigin().getEntryComponent().orElseThrow());
        assertEquals(ENTERED, unit.getMetadata().getOrigin().getEntryInstant().orElseThrow());
        assertEquals(origin, origin.establishSourceSystem(SOURCE), "same versioned value is idempotent");

        Topic internal = InformationFixtures.topic("Component", "Ergon");
        OriginMetadata generated = OriginMetadata.unknown().establishSourceSystem(internal)
                .establishEntryComponent(internal);
        assertEquals(generated.getSourceSystem(), generated.getEntryComponent());
    }

    @Test
    void establishedOriginFactsCannotBeReplacedRemovedOrReVersioned() {
        OriginMetadata origin = OriginMetadata.unknown().establishSourceSystem(SOURCE)
                .establishEntryInstant(ENTERED);
        InformationUnit unit = InformationFixtures.unit().withOrigin(origin);
        Topic reVersioned = InformationFixtures.topic("SourceSystem", "LabSystem", "4");

        expectFailure(InformationFailureCategory.IMMUTABILITY_VIOLATION, "origin.sourceSystem",
                () -> origin.establishSourceSystem(reVersioned));
        expectFailure(InformationFailureCategory.IMMUTABILITY_VIOLATION, "origin.entryInstant",
                () -> origin.establishEntryInstant(ENTERED.plusSeconds(1)));
        expectFailure(InformationFailureCategory.IMMUTABILITY_VIOLATION, "origin.sourceSystem",
                () -> unit.withOrigin(OriginMetadata.unknown()));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "origin.entryComponent",
                () -> origin.establishEntryComponent(null));
        assertSame(origin, unit.getMetadata().getOrigin(), "rejected changes leave the snapshot intact");
    }

    @Test
    void securityChangesAreProposalsForKritesRatherThanCalliopePolicyDecisions() {
        Topic restricted = InformationFixtures.topic("Sensitivity", "Restricted", "1");
        Topic mentalHealth = InformationFixtures.topic("Handling", "MentalHealth");
        InformationUnit unit = InformationFixtures.unit().withSecurity(SecurityMetadata.of(List.of(restricted)));
        SecurityMetadata stronger = unit.getMetadata().getSecurity().withAdded(List.of(mentalHealth, restricted));
        InformationUnit strengthened = unit.withSecurity(stronger);

        assertEquals(2, strengthened.getMetadata().getSecurity().getRequirements().size());
        InformationUnit weakened = strengthened.withSecurity(
                strengthened.getMetadata().getSecurity().withRemoved(List.of(restricted)));
        assertEquals(List.of(mentalHealth), List.copyOf(weakened.getMetadata().getSecurity().getRequirements()));
        InformationUnit replaced = weakened.withSecurity(SecurityMetadata.of(List.of(
                InformationFixtures.topic("Sensitivity", "Internal", "2"))));
        assertEquals(List.of(InformationFixtures.topic("Sensitivity", "Internal", "2")),
                List.copyOf(replaced.getMetadata().getSecurity().getRequirements()));
        assertEquals(List.of(restricted), List.copyOf(unit.getMetadata().getSecurity().getRequirements()),
                "proposals do not mutate prior snapshots");
        expectFailure(InformationFailureCategory.MISSING_INPUT, "security",
                () -> strengthened.withSecurity(null));
        expectFailure(InformationFailureCategory.INCONSISTENT_INPUT, "security.requirements",
                () -> stronger.withAdded(List.of(InformationFixtures.topic("Sensitivity", "Restricted", "2"))));
        expectFailure(InformationFailureCategory.INCONSISTENT_INPUT, "security.requirements",
                () -> stronger.withRemoved(List.of(InformationFixtures.topic("Sensitivity", "Restricted", "2"))));
        assertThrows(UnsupportedOperationException.class,
                () -> stronger.getRequirements().remove(restricted));
    }

    @Test
    void securityInputCollectionIsCopiedAndNullElementsAreRejected() {
        List<Topic> supplied = new ArrayList<>(List.of(InformationFixtures.topic("Sensitivity", "Normal")));
        SecurityMetadata security = SecurityMetadata.of(supplied);
        supplied.clear();

        assertEquals(1, security.getRequirements().size());
        List<Topic> withNull = new ArrayList<>();
        withNull.add(null);
        expectFailure(InformationFailureCategory.MISSING_INPUT, "security.requirements",
                () -> SecurityMetadata.of(withNull));
        assertEquals("SecurityMetadata{requirements=1}", security.toString());
    }

    @Test
    void securityAndCoordinatesAreIndependent() {
        InformationUnit secured = InformationFixtures.unit()
                .withSecurity(SecurityMetadata.of(List.of(InformationFixtures.topic("Sensitivity", "High"))));
        InformationUnit relocated = secured.reviseCoordinates(InformationCoordinates.of(
                null, null, InformationFixtures.topic("Location", "SiteC"), null));

        assertEquals(secured.getMetadata().getSecurity(), relocated.getMetadata().getSecurity());
        assertNotEquals(secured.getMetadata().getCoordinates(), relocated.getMetadata().getCoordinates());
    }

    static void expectFailure(InformationFailureCategory category, String field, Executable action) {
        InformationModelException failure = assertThrows(InformationModelException.class, action);
        assertEquals(category, failure.getCategory());
        assertEquals(field, failure.getField());
    }
}
