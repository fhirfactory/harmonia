/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import static net.fhirfactory.harmonia.model.core.information.entity.InformationMetadataTest.expectFailure;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationQuality;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.QualityGrade;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.RelationshipRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** AC-6: lifetime-immutable quality, separate assessment owner and authority, validation successors. */
class InformationQualityTest {

    @Test
    void gradesAreExactlyTheThreeAdjudicatedValues() {
        assertEquals(List.of(QualityGrade.ANECDOTAL, QualityGrade.INFORMATIONAL, QualityGrade.CANONICAL),
                List.of(QualityGrade.values()));
    }

    @ParameterizedTest
    @EnumSource(QualityGrade.class)
    void everyGradeIsCarriedIndependentlyOfTheAuthorityReference(QualityGrade grade) {
        InformationUnit unit = InformationFixtures.builder()
                .quality(InformationQuality.of("assessor", grade, Map.of())).build();

        assertEquals(grade, unit.getQuality().getGrade());
        assertEquals(InformationFixtures.authority(), unit.getMetadata().getInformationAuthority(),
                "CANONICAL or not, the authority reference is a separate explicit fact");
    }

    @Test
    void certificationMapIsCopiedOnInputAndUnmodifiableOnOutput() {
        Map<String, String> supplied = new HashMap<>(Map.of("lab-b", "nata", "lab-a", "iso-15189"));
        InformationQuality quality = InformationQuality.of("assessor", QualityGrade.CANONICAL, supplied);
        supplied.put("lab-c", "forged");
        supplied.remove("lab-a");

        assertEquals(Map.of("lab-a", "iso-15189", "lab-b", "nata"), quality.getCertifications());
        assertEquals(List.of("lab-a", "lab-b"), List.copyOf(quality.getCertifications().keySet()),
                "certifiers are exposed in a stable order");
        assertThrows(UnsupportedOperationException.class, () -> quality.getCertifications().put("x", "y"));
        assertThrows(UnsupportedOperationException.class, () -> quality.getCertifications().clear());
        assertThrows(UnsupportedOperationException.class,
                () -> quality.getCertifications().entrySet().iterator().next().setValue("changed"));
    }

    @Test
    void invalidQualityInputIsRejectedWithoutEchoingValues() {
        Map<String, String> nullValue = new HashMap<>();
        nullValue.put("lab-a", null);
        Map<String, String> nullKey = new HashMap<>();
        nullKey.put(null, "cert");

        expectFailure(InformationFailureCategory.MISSING_INPUT, "quality.owner",
                () -> InformationQuality.of(null, QualityGrade.ANECDOTAL, Map.of()));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "quality.owner",
                () -> InformationQuality.of("  ", QualityGrade.ANECDOTAL, Map.of()));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "quality.grade",
                () -> InformationQuality.of("assessor", null, Map.of()));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "quality.certifications",
                () -> InformationQuality.of("assessor", QualityGrade.ANECDOTAL, null));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "quality.certifications",
                () -> InformationQuality.of("assessor", QualityGrade.ANECDOTAL, nullValue));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "quality.certifications",
                () -> InformationQuality.of("assessor", QualityGrade.ANECDOTAL, nullKey));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "quality.certifications",
                () -> InformationQuality.of("assessor", QualityGrade.ANECDOTAL, Map.of("lab", " ")));
        assertEquals("InformationQuality{grade=CANONICAL, certifications=1}",
                InformationQuality.of("Dr Secret", QualityGrade.CANONICAL, Map.of("Secret Lab", "x")).toString());
    }

    @Test
    void qualityIsStableAcrossACoordinateOwnerRevisionAndItsOwnerIsDistinct() {
        InformationUnit unit = InformationFixtures.unit().reviseCoordinates(InformationCoordinates.of(null, null,
                null, InformationFixtures.topic("Owner", "WardA")));
        InformationUnit reowned = unit.reviseCoordinates(InformationCoordinates.of(null, null, null,
                InformationFixtures.topic("Owner", "WardB")));

        assertSame(unit.getQuality(), reowned.getQuality());
        assertEquals("quality-team", reowned.getQuality().getOwner());
        assertNotEquals(unit.getMetadata().getCoordinates().getInformationOwner(),
                reowned.getMetadata().getCoordinates().getInformationOwner());
    }

    @Test
    void subsequentValidationProducesANewIuWithDistinctUuidAndSourceProvenance() {
        InformationUnit source = InformationFixtures.unit();
        InformationQuality assured = InformationQuality.of("validation-service", QualityGrade.CANONICAL,
                Map.of("terminology-server", "ontoserver-validated"));
        InformationUnit validated = source.deriveSuccessorWithMinimumMetadata(InformationFixtures.OTHER_ID,
                        RelationshipRole.DERIVED_FROM)
                .informationConcept(source.getInformationConcept())
                .description(source.getDescription())
                .quality(assured)
                .informationAuthority(source.getMetadata().getInformationAuthority())
                .representation(source.getRepresentation())
                .build();

        assertNotEquals(source.getId(), validated.getId());
        assertEquals(QualityGrade.CANONICAL, validated.getQuality().getGrade());
        assertEquals(QualityGrade.INFORMATIONAL, source.getQuality().getGrade(), "source quality is unchanged");
        assertEquals(InformationCoordinates.unplaced(), validated.getMetadata().getCoordinates(),
                "minimum-metadata derivation defaults omitted coordinates to unplaced");
        assertEquals(source.getId(), validated.getMetadata().getRelationships().getRelationships().get(0)
                .getTargetInformationUnitId().orElseThrow());
        assertEquals(source.getRepresentation(), validated.getRepresentation());
    }
}
