/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.factory;

import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContainerMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationModelException;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ModelChecks;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.FhirR4ResourceType;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.NamingValidationException;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicBuilder;

/**
 * Release-defined builders for the governed InformationUnit Topic vocabulary.
 *
 * <p>Draft 002.006 §1.6 requires release-defined builders for governed
 * entries and the existing Naming API for individual Topics. Each standard
 * establishes its own subordinate containment structure, while all vocabularies
 * retain the common {@code Domain → Owner → Standard} hierarchy. Containment
 * is encoded in the qualified Topic path; this class does not create a separate
 * relationship model.</p>
 *
 * <p>The predefined {@code RAW} container and content schema paths, and FHIR R4 resource schema paths, are:</p>
 * <pre>
 * RAW container schema   Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW
 * RAW content schema     Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.ContentSchema=RAW
 * FHIR R4 schema         Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4.0.1=R4.Resource=Patient
 * Generic profile        Domain=Health.Owner=ExampleOrganisation.Standard=Clinical.Release:1.0=1.Profile:2.1=Patient
 * </pre>
 *
 * <p>The paths above are shown unescaped for readability; the canonical
 * rendering escapes periods inside fields, for example
 * {@code Release:4\.0\.1=R4}.</p>
 *
 * <p>The FHIR R4 release designation is {@code R4}, carried at the
 * {@code Release} containment level with specific version {@code 4.0.1}.
 * Resource definitions are subordinate to that release and have no independent
 * version. Because Topic identity ignores versions, compare
 * {@link Topic#getCanonicalNameWithVersion()} when version matters.</p>
 *
 * <p>Media types belong to the separate media-type fields in container and
 * content metadata. No method here builds a schema or content concept from a
 * media type or format: a FHIR Patient keeps the same {@code contentSchema}
 * whether it is serialised as JSON or XML.</p>
 *
 * <p>Generic profile Topics are appended to their governing standard Topic.
 * The base must begin with {@code Domain → Owner → Standard}; its own standard
 * may define further containment levels. A profile is not nested under a
 * resource, and its Topic carries no implicit relationship to any resource.</p>
 *
 * <p>The class is stateless and thread-safe.</p>
 */
public final class InformationVocabulary {
    /** Qualified path of the predefined container schema used when no distinct schema exists. */
    public static final String RAW_CONTAINER_SCHEMA =
            "Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW";

    /** Qualified path of the predefined content schema for content without a distinct schema. */
    public static final String RAW_CONTENT_SCHEMA =
            "Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.ContentSchema=RAW";

    private static final Topic RAW_CONTAINER = Topic.fromCanonicalName(RAW_CONTAINER_SCHEMA);
    private static final Topic RAW_CONTENT = Topic.fromCanonicalName(RAW_CONTENT_SCHEMA);

    private InformationVocabulary() {
    }

    /**
     * Returns the predefined qualified {@code RAW} container-schema Topic.
     *
     * @return {@code Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW}
     */
    public static Topic rawContainerSchema() {
        return RAW_CONTAINER;
    }

    /**
     * Returns the predefined qualified {@code RAW} content-schema Topic.
     *
     * <p>This is distinct from {@link #rawContainerSchema()}: it classifies
     * content whose structure is unspecified, while the container path says
     * that Calliope imposes no additional structure on its carrier.</p>
     *
     * @return {@code Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.ContentSchema=RAW}
     */
    public static Topic rawContentSchema() {
        return RAW_CONTENT;
    }

    /**
     * Creates container metadata for a container with no additional structure.
     *
     * @param containerConcept what the container represents; every container has a concept
     * @param containerMediaType IANA media type identifier for its carrier
     * @return container metadata whose schema is the RAW container Topic
     * @throws InformationModelException when an argument is missing or the media type is malformed
     */
    public static ContainerMetadata rawContainer(Topic containerConcept, String containerMediaType) {
        return ContainerMetadata.of(containerConcept, RAW_CONTAINER, containerMediaType);
    }

    /**
     * Builds the content-schema Topic for a FHIR R4 resource type.
     *
     * @param resourceType FHIR R4 resource type
     * @return a schema Topic whose {@code Release} entry is {@code R4}, version {@code 4.0.1}
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public static Topic fhirR4ResourceSchema(FhirR4ResourceType resourceType) {
        ModelChecks.required(resourceType, "vocabulary.resourceType");
        return fhirBase(resourceType).build();
    }

    /**
     * Builds a governing-standard Topic using the common ownership hierarchy.
     *
     * <p>The standard's specific version, when supplied, is attached to the
     * {@code Standard} entry. Standards with additional containment levels can
     * construct their full base Topic with the Naming API before calling
     * {@link #profileSchema(Topic, String, String)}.</p>
     *
     * @param domain governing domain value
     * @param owner organisation responsible for the standard
     * @param standard governing standard name
     * @param standardVersion optional specific version of the standard
     * @return the qualified {@code Domain → Owner → Standard} Topic
     * @throws InformationModelException when a required value is missing or invalid
     */
    public static Topic governingStandard(String domain, String owner, String standard, String standardVersion) {
        ModelChecks.requiredText(domain, "vocabulary.domain");
        ModelChecks.requiredText(owner, "vocabulary.owner");
        ModelChecks.requiredText(standard, "vocabulary.standard");
        try {
            return Topic.builder().addEntry("Domain", domain)
                    .addEntry("Owner", owner)
                    .addEntry("Standard", standard, standardVersion)
                    .build();
        } catch (NamingValidationException rejected) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, "vocabulary.governingStandard");
        }
    }

    /**
     * Adds a profile definition directly beneath its own governing standard Topic.
     *
     * <p>The supplied Topic must begin with {@code Domain → Owner → Standard}.
     * Additional standard-defined levels, such as a release entry, are
     * preserved. A {@code Resource} or existing {@code Profile} entry is
     * rejected so the profile remains independent of an underlying resource
     * and cannot be accidentally nested beneath another profile.</p>
     *
     * @param governingStandard base Topic identifying the profile's governing standard
     * @param profile profile name
     * @param profileVersion optional version of the profile definition
     * @return the profile Topic with all governing-standard entries preserved
     * @throws InformationModelException when the base path or profile input is invalid
     */
    public static Topic profileSchema(Topic governingStandard, String profile, String profileVersion) {
        ModelChecks.required(governingStandard, "vocabulary.governingStandard");
        ModelChecks.requiredText(profile, "vocabulary.profile");
        validateGoverningStandard(governingStandard);

        try {
            TopicBuilder builder = Topic.builder();
            for (var entry : governingStandard.getEntries()) {
                builder.addEntry(entry.getQualifier(), entry.getValue(), entry.getVersion().orElse(null));
            }
            return builder.addEntry("Profile", profile, profileVersion).build();
        } catch (NamingValidationException rejected) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT, "vocabulary.profile");
        }
    }

    /** Enforces the common prefix and prevents resource-bound or nested profiles. */
    private static void validateGoverningStandard(Topic topic) {
        var entries = topic.getEntries();
        if (entries.size() < 3
                || !"Domain".equals(entries.get(0).getQualifier())
                || !"Owner".equals(entries.get(1).getQualifier())
                || !"Standard".equals(entries.get(2).getQualifier())) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT,
                    "vocabulary.governingStandard");
        }
        for (int index = 3; index < entries.size(); index++) {
            String qualifier = entries.get(index).getQualifier();
            if ("Resource".equals(qualifier) || "Profile".equals(qualifier)) {
                throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT,
                        "vocabulary.governingStandard");
            }
        }
    }

    private static TopicBuilder fhirBase(FhirR4ResourceType resourceType) {
        return Topic.builder().addEntry("Domain", "Health")
                .addEntry("Owner", "HealthLevelSevenInternational")
                .addEntry("Standard", "FHIR")
                .addEntry("Release", FhirR4ResourceType.FHIR_R4_RELEASE,
                        FhirR4ResourceType.FHIR_R4_VERSION)
                .addEntry("Resource", resourceType.name());
    }
}
