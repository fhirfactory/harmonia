/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import static net.fhirfactory.harmonia.model.core.information.entity.InformationMetadataTest.expectFailure;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.stream.Collectors;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentDetail;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.factory.InformationVocabulary;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.FhirR4ResourceType;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.MediaTopLevelType;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Release-defined vocabulary: qualified RAW and FHIR R4 schema Topics. */
class InformationVocabularyTest {
    /**
     * SHA-256 of the newline-terminated FHIR R4 resource names listed by
     * the R4 resource type enumeration from the locally cached HL7 model artifact.
     */
    private static final String FHIR_R4_RESOURCE_LIST_SHA256 =
            "07e475727c0d89a50e24ce31500d27fab96134c77a2c164ecacbb35d9d1532ea";

    @Test
    void rawContainerSchemaIsTheSolutionArchitectsQualifiedPath() {
        Topic raw = InformationVocabulary.rawContainerSchema();

        assertEquals("Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW",
                raw.getCanonicalNameWithVersion());
        assertEquals("RAW", raw.getFriendlyName());
        assertSame(raw, InformationVocabulary.rawContainerSchema());
        assertSame(raw, InformationVocabulary.rawContainer(InformationFixtures.topic("C", "File"),
                "application/octet-stream")
                .getContainerSchema());
    }

    @Test
    void rawContentSchemaHasItsOwnGovernedPathAndRemainsMandatory() {
        Topic rawContainer = InformationVocabulary.rawContainerSchema();
        Topic rawContent = InformationVocabulary.rawContentSchema();
        ContentMetadata content = ContentMetadata.of(InformationFixtures.topic("ContentConcept", "Opaque"),
                rawContent, "application/octet-stream", ContentDetail.empty());

        assertEquals("Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.ContentSchema=RAW",
                rawContent.getCanonicalNameWithVersion());
        assertNotEquals(rawContainer, rawContent, "container structure and content schema are different axes");
        assertEquals(rawContent, content.getContentSchema());
        expectFailure(InformationFailureCategory.MISSING_INPUT, "contentMetadata.contentSchema",
                () -> ContentMetadata.of(InformationFixtures.topic("ContentConcept", "Opaque"), null,
                        "application/octet-stream", ContentDetail.empty()));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "contentMetadata.contentConcept",
                () -> ContentMetadata.of(null, rawContent, "application/octet-stream", ContentDetail.empty()));
    }

    @Test
    void fhirR4ResourceTypesMatchTheOfficialR4ListExactly() throws Exception {
        String names = Arrays.stream(FhirR4ResourceType.values()).map(Enum::name)
                .collect(Collectors.joining("\n", "", "\n"));
        String digest = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                .digest(names.getBytes(StandardCharsets.UTF_8)));

        assertEquals(146, FhirR4ResourceType.values().length);
        assertEquals(FHIR_R4_RESOURCE_LIST_SHA256, digest);
        assertEquals("R4", FhirR4ResourceType.FHIR_R4_RELEASE);
        assertEquals("4.0.1", FhirR4ResourceType.FHIR_R4_VERSION);
    }

    @ParameterizedTest
    @EnumSource(FhirR4ResourceType.class)
    void everyFhirR4ResourceTypeBuildsAVersionedSchemaTopic(FhirR4ResourceType type) {
        Topic schema = InformationVocabulary.fhirR4ResourceSchema(type);

        assertEquals("Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4\\.0\\.1=R4.Resource="
                        + type.name(),
                schema.getCanonicalNameWithVersion());
        assertEquals(type.name(), schema.getFriendlyName());
    }

    @Test
    void genericProfileSchemasUseTheirGoverningOwnerAndStandard() {
        Topic standard = InformationVocabulary.governingStandard(
                "Health", "ExampleOrganisation", "Clinical", "1.0");
        Topic profile = InformationVocabulary.profileSchema(standard, "Patient", "2.1");

        assertEquals("Domain=Health.Owner=ExampleOrganisation.Standard:1\\.0=Clinical.Profile:2\\.1=Patient",
                profile.getCanonicalNameWithVersion());
        assertEquals(standard, profile.getParent());
        assertFalse(profile.getEntries().stream().anyMatch(entry -> "Resource".equals(entry.getQualifier())));
        assertNotEquals(InformationVocabulary.fhirR4ResourceSchema(FhirR4ResourceType.Patient), profile);
    }

    @Test
    void profileSchemaPreservesStandardDefinedIntermediateContainment() {
        Topic standard = Topic.builder().addEntry("Domain", "Health")
                .addEntry("Owner", "ExampleOrganisation")
                .addEntry("Standard", "Clinical", "1.0")
                .addEntry("Release", "2026", "4")
                .build();
        Topic profile = InformationVocabulary.profileSchema(standard, "Observation", null);

        assertEquals("Domain=Health.Owner=ExampleOrganisation.Standard:1\\.0=Clinical.Release:4=2026"
                + ".Profile=Observation", profile.getCanonicalNameWithVersion());
        assertEquals(standard, profile.getParent());
    }

    @Test
    void profileAndStandardVersionsRemainDataRatherThanTopicIdentity() {
        Topic standard = InformationVocabulary.governingStandard(
                "Health", "ExampleOrganisation", "Clinical", "1.0");
        Topic versioned = InformationVocabulary.profileSchema(standard, "Patient", "2.1");
        Topic unversionedProfile = InformationVocabulary.profileSchema(standard, "Patient", null);
        Topic otherStandardVersion = InformationVocabulary.profileSchema(
                InformationVocabulary.governingStandard("Health", "ExampleOrganisation", "Clinical", "1.1"),
                "Patient", null);

        assertEquals(versioned, unversionedProfile);
        assertEquals(unversionedProfile, otherStandardVersion);
        assertNotEquals(versioned.getCanonicalNameWithVersion(), unversionedProfile.getCanonicalNameWithVersion());
        assertNotEquals(unversionedProfile.getCanonicalNameWithVersion(),
                otherStandardVersion.getCanonicalNameWithVersion());
    }

    @Test
    void invalidGenericProfileAndGoverningStandardInputsAreRejectedSafely() {
        expectFailure(InformationFailureCategory.MISSING_INPUT, "vocabulary.domain",
                () -> InformationVocabulary.governingStandard(null, "ExampleOrganisation", "Clinical", null));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.domain",
                () -> InformationVocabulary.governingStandard(" ", "ExampleOrganisation", "Clinical", null));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "vocabulary.owner",
                () -> InformationVocabulary.governingStandard("Health", null, "Clinical", null));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.owner",
                () -> InformationVocabulary.governingStandard("Health", " ", "Clinical", null));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "vocabulary.standard",
                () -> InformationVocabulary.governingStandard("Health", "ExampleOrganisation", null, null));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.standard",
                () -> InformationVocabulary.governingStandard("Health", "ExampleOrganisation", " ", null));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.governingStandard",
                () -> InformationVocabulary.governingStandard("Health", "ExampleOrganisation", "Clinical", "v\u00e9"));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "vocabulary.governingStandard",
                () -> InformationVocabulary.profileSchema(null, "Patient", null));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "vocabulary.profile",
                () -> InformationVocabulary.profileSchema(InformationVocabulary.governingStandard(
                        "Health", "ExampleOrganisation", "Clinical", null), null, null));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.profile",
                () -> InformationVocabulary.profileSchema(InformationVocabulary.governingStandard(
                        "Health", "ExampleOrganisation", "Clinical", null), "", null));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.profile",
                () -> InformationVocabulary.profileSchema(InformationVocabulary.governingStandard(
                        "Health", "ExampleOrganisation", "Clinical", null), "Patient", "v\u00e9"));
    }

    @Test
    void profileBaseMustBeAGoverningStandardAndCannotBeResourceBoundOrNested() {
        Topic wrongOrder = Topic.builder().addEntry("Owner", "ExampleOrganisation")
                .addEntry("Domain", "Health").addEntry("Standard", "Clinical").build();
        Topic tooShort = Topic.builder().addEntry("Domain", "Health").addEntry("Owner", "ExampleOrganisation")
                .build();
        Topic resource = InformationVocabulary.fhirR4ResourceSchema(FhirR4ResourceType.Patient);
        Topic parentProfile = InformationVocabulary.profileSchema(InformationVocabulary.governingStandard(
                "Health", "ExampleOrganisation", "Clinical", null), "Parent", null);

        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.governingStandard",
                () -> InformationVocabulary.profileSchema(wrongOrder, "Patient", null));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.governingStandard",
                () -> InformationVocabulary.profileSchema(tooShort, "Patient", null));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.governingStandard",
                () -> InformationVocabulary.profileSchema(resource, "Patient", null));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.governingStandard",
                () -> InformationVocabulary.profileSchema(parentProfile, "Child", null));
    }

    @Test
    void invalidVocabularyInputIsRejectedSafely() {
        expectFailure(InformationFailureCategory.MISSING_INPUT, "vocabulary.resourceType",
                () -> InformationVocabulary.fhirR4ResourceSchema(null));
    }

    @Test
    void mediaTypesDoNotBecomeContentConceptsOrSchemas() {
        for (Method method : InformationVocabulary.class.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                assertFalse(method.getName().toLowerCase().contains("media"), method.getName());
                assertFalse(Arrays.asList(method.getParameterTypes()).contains(MediaTopLevelType.class),
                        method.getName());
            }
        }
        assertEquals(11, MediaTopLevelType.values().length);
        assertEquals("image", MediaTopLevelType.IMAGE.getToken());
    }
}
