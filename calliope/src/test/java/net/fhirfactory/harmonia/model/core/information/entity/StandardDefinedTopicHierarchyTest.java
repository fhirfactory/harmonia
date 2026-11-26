/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import static net.fhirfactory.harmonia.model.core.information.entity.InformationMetadataTest.expectFailure;
import static org.junit.jupiter.api.Assertions.assertEquals;

import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentDetail;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContainerMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.factory.InformationVocabulary;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import org.junit.jupiter.api.Test;

/**
 * Protects the boundary between the generic, versioned Topic model and the narrower profile-schema helper.
 *
 * <p>These examples exercise resource-bound, nested-profile and non-{@code Domain}-rooted paths.
 * All are legitimate Naming values that the InformationUnit model accepts; only the helper's documented
 * input contract may reject some of them.</p>
 */
class StandardDefinedTopicHierarchyTest {
    private static final String RESOURCE_PROFILE_PATH =
            "Domain=Health.Owner=ExampleOrganisation.Standard:1\\.0=Clinical.Resource=Patient"
                    + ".Profile:2\\.1=ExampleProfile";
    private static final String NESTED_PROFILE_PATH =
            "Domain=Health.Owner=ExampleOrganisation.Standard=Clinical.Profile=Parent.Profile:3\\.0=Child";
    private static final String OTHER_STANDARD_PATH =
            "System=ExampleRegistry.Standard:2\\.0=Archive.Section:3\\.1=Series.Section=Record";

    /**
     * Shows that helper-excluded Topic shapes retain paths and versions through all IU model boundaries.
     *
     * <p>The Topics are first constructed with the public Naming builder, then reused as the IU concept,
     * authority, coordinates, container and content terms. The final assertions show that the optional
     * restrictions remain local to {@code profileSchema}, rather than leaking into Topic or IU validation.</p>
     */
    @Test
    void validStandardDefinedTopicsRemainUsableAcrossTheInformationUnitModel() {
        Topic resourceBoundProfile = Topic.builder().addEntry("Domain", "Health")
                .addEntry("Owner", "ExampleOrganisation")
                .addEntry("Standard", "Clinical", "1.0")
                .addEntry("Resource", "Patient")
                .addEntry("Profile", "ExampleProfile", "2.1")
                .build();
        Topic nestedProfile = Topic.builder().addEntry("Domain", "Health")
                .addEntry("Owner", "ExampleOrganisation")
                .addEntry("Standard", "Clinical")
                .addEntry("Profile", "Parent")
                .addEntry("Profile", "Child", "3.0")
                .build();
        Topic otherStandardHierarchy = Topic.builder().addEntry("System", "ExampleRegistry")
                .addEntry("Standard", "Archive", "2.0")
                .addEntry("Section", "Series", "3.1")
                .addEntry("Section", "Record")
                .build();

        assertVersionedRoundTrip(resourceBoundProfile, RESOURCE_PROFILE_PATH);
        assertVersionedRoundTrip(nestedProfile, NESTED_PROFILE_PATH);
        assertVersionedRoundTrip(otherStandardHierarchy, OTHER_STANDARD_PATH);

        ContainerMetadata container = ContainerMetadata.of(otherStandardHierarchy, resourceBoundProfile,
                "application/octet-stream");
        ContentMetadata content = ContentMetadata.of(nestedProfile, otherStandardHierarchy,
                "application/json", ContentDetail.empty());
        InformationRepresentation representation = InformationRepresentation.of(container, content,
                Content.inlineText("standard-defined topic hierarchy test"));
        InformationUnit unit = InformationUnit.builder().id(InformationFixtures.ID)
                .informationConcept(resourceBoundProfile).description("qualified topic regression")
                .quality(InformationFixtures.quality()).metadata(InformationMetadata.initial(nestedProfile))
                .representation(representation).build();

        assertEquals(RESOURCE_PROFILE_PATH, unit.getInformationConcept().getCanonicalNameWithVersion());
        assertEquals(resourceBoundProfile.getName() + InformationFixtures.ID, unit.getName());
        assertEquals(NESTED_PROFILE_PATH,
                unit.getMetadata().getInformationAuthority().getCanonicalNameWithVersion());
        assertEquals(OTHER_STANDARD_PATH, unit.getRepresentation().getContainerMetadata()
                .getContainerConcept().getCanonicalNameWithVersion());
        assertEquals(RESOURCE_PROFILE_PATH, unit.getRepresentation().getContainerMetadata()
                .getContainerSchema().getCanonicalNameWithVersion());
        assertEquals(NESTED_PROFILE_PATH, unit.getRepresentation().getContentMetadata()
                .getContentConcept().getCanonicalNameWithVersion());
        assertEquals(OTHER_STANDARD_PATH, unit.getRepresentation().getContentMetadata()
                .getContentSchema().getCanonicalNameWithVersion());

        InformationUnit revisedCoordinates = unit.reviseCoordinates(InformationCoordinates.of(
                resourceBoundProfile, nestedProfile, otherStandardHierarchy, resourceBoundProfile));
        assertEquals(RESOURCE_PROFILE_PATH, revisedCoordinates.getMetadata().getCoordinates()
                .getPersistenceGateway().orElseThrow().getCanonicalNameWithVersion());
        assertEquals(NESTED_PROFILE_PATH, revisedCoordinates.getMetadata().getCoordinates()
                .getGatewayEndpoint().orElseThrow().getCanonicalNameWithVersion());
        assertEquals(OTHER_STANDARD_PATH, revisedCoordinates.getMetadata().getCoordinates()
                .getInformationLocation().orElseThrow().getCanonicalNameWithVersion());
        assertEquals(RESOURCE_PROFILE_PATH, revisedCoordinates.getMetadata().getCoordinates()
                .getInformationOwner().orElseThrow().getCanonicalNameWithVersion());

        InformationUnit revisedAuthority = unit.reviseInformationAuthority(resourceBoundProfile);
        assertEquals(NESTED_PROFILE_PATH, unit.getMetadata().getInformationAuthority().getCanonicalNameWithVersion(),
                "the original immutable snapshot retains its authority");
        assertEquals(RESOURCE_PROFILE_PATH,
                revisedAuthority.getMetadata().getInformationAuthority().getCanonicalNameWithVersion());

        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.governingStandard",
                () -> InformationVocabulary.profileSchema(resourceBoundProfile, "AnotherProfile", null));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "vocabulary.governingStandard",
                () -> InformationVocabulary.profileSchema(nestedProfile, "AnotherProfile", null));
    }

    private static void assertVersionedRoundTrip(Topic topic, String expectedCanonicalPath) {
        assertEquals(expectedCanonicalPath, topic.getCanonicalNameWithVersion());
        Topic reparsed = Topic.fromCanonicalNameWithVersion(expectedCanonicalPath);
        assertEquals(expectedCanonicalPath, reparsed.getCanonicalNameWithVersion(),
                "canonical parsing must preserve every qualified entry and version");
        assertEquals(topic, reparsed, "Topic identity is its qualified path");
        assertEquals(topic.getEntries(), reparsed.getEntries());
    }
}