/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import java.util.Map;
import java.util.UUID;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentDetail;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationQuality;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.factory.InformationVocabulary;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.FhirR4ResourceType;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.QualityGrade;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * Synthetic, PHI-free fixtures shared by the InformationUnit tests.
 *
 * <p>Every value is invented. The fixed UUIDs make name and relationship
 * assertions exact and repeatable.</p>
 */
final class InformationFixtures {
    static final UUID ID = UUID.fromString("0f8fad5b-d9cb-469f-a165-70867728950e");
    static final UUID OTHER_ID = UUID.fromString("7c9e6679-7425-40de-944b-e07fc1f90ae7");

    private InformationFixtures() {
    }

    /** A qualified concept whose entries carry versions, to prove versions never reach the name. */
    static Topic concept() {
        return Topic.builder().addEntry("System", "Harmonia", "1").addEntry("Concept", "Observation", "2.0").build();
    }

    static Topic topic(String qualifier, String value) {
        return Topic.builder().addEntry("System", "Harmonia").addEntry(qualifier, value).build();
    }

    static Topic topic(String qualifier, String value, String version) {
        return Topic.builder().addEntry("System", "Harmonia").addEntry(qualifier, value, version).build();
    }

    static Topic authority() {
        return topic("Authority", "ClinicalRecords");
    }

    static InformationQuality quality() {
        return InformationQuality.of("quality-team", QualityGrade.INFORMATIONAL, Map.of("lab-a", "iso-15189"));
    }

    static InformationRepresentation representation() {
        return InformationRepresentation.of(InformationVocabulary.rawContainer(topic("Container", "Message"),
                        "application/fhir+json"),
                ContentMetadata.of(topic("ContentConcept", "Observation"),
                        InformationVocabulary.fhirR4ResourceSchema(FhirR4ResourceType.Observation),
                        "application/fhir+json", ContentDetail.empty()),
                Content.inlineText("{\"resourceType\":\"Observation\"}"));
    }

    static InformationUnitBuilder builder() {
        return InformationUnit.builder().id(ID).informationConcept(concept()).description("synthetic observation")
                .quality(quality()).metadata(InformationMetadata.initial(authority()))
                .representation(representation());
    }

    static InformationUnit unit() {
        return builder().build();
    }
}
