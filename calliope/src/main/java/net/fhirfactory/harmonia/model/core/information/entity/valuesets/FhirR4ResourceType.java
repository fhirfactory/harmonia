/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.valuesets;

/**
 * The 146 concrete resource types defined by HL7 FHIR R4 (version 4.0.1).
 *
 * <p>This is the release-defined source for FHIR {@code contentSchema} Topics.
 * The standard's recognised release designation is {@link #FHIR_R4_RELEASE},
 * while {@link #FHIR_R4_VERSION} records its specific technical version.
 * Constant names intentionally deviate from Java's UPPER_SNAKE convention: each
 * constant's {@link #name()} is exactly the FHIR resource type name, so there
 * is no hand-maintained mapping table that could drift from the standard. The
 * list was taken from {@code org.hl7.fhir.r4.model.ResourceType} and excludes
 * the abstract {@code Resource} and {@code DomainResource} types.</p>
 *
 * <p>A resource type identifies schema structure. It does not identify a
 * serialisation: the same Patient structure may be carried as JSON or XML.</p>
 */
public enum FhirR4ResourceType {
    /** The FHIR R4 {@code Account} resource type. */
    Account,
    /** The FHIR R4 {@code ActivityDefinition} resource type. */
    ActivityDefinition,
    /** The FHIR R4 {@code AdverseEvent} resource type. */
    AdverseEvent,
    /** The FHIR R4 {@code AllergyIntolerance} resource type. */
    AllergyIntolerance,
    /** The FHIR R4 {@code Appointment} resource type. */
    Appointment,
    /** The FHIR R4 {@code AppointmentResponse} resource type. */
    AppointmentResponse,
    /** The FHIR R4 {@code AuditEvent} resource type. */
    AuditEvent,
    /** The FHIR R4 {@code Basic} resource type. */
    Basic,
    /** The FHIR R4 {@code Binary} resource type. */
    Binary,
    /** The FHIR R4 {@code BiologicallyDerivedProduct} resource type. */
    BiologicallyDerivedProduct,
    /** The FHIR R4 {@code BodyStructure} resource type. */
    BodyStructure,
    /** The FHIR R4 {@code Bundle} resource type. */
    Bundle,
    /** The FHIR R4 {@code CapabilityStatement} resource type. */
    CapabilityStatement,
    /** The FHIR R4 {@code CarePlan} resource type. */
    CarePlan,
    /** The FHIR R4 {@code CareTeam} resource type. */
    CareTeam,
    /** The FHIR R4 {@code CatalogEntry} resource type. */
    CatalogEntry,
    /** The FHIR R4 {@code ChargeItem} resource type. */
    ChargeItem,
    /** The FHIR R4 {@code ChargeItemDefinition} resource type. */
    ChargeItemDefinition,
    /** The FHIR R4 {@code Claim} resource type. */
    Claim,
    /** The FHIR R4 {@code ClaimResponse} resource type. */
    ClaimResponse,
    /** The FHIR R4 {@code ClinicalImpression} resource type. */
    ClinicalImpression,
    /** The FHIR R4 {@code CodeSystem} resource type. */
    CodeSystem,
    /** The FHIR R4 {@code Communication} resource type. */
    Communication,
    /** The FHIR R4 {@code CommunicationRequest} resource type. */
    CommunicationRequest,
    /** The FHIR R4 {@code CompartmentDefinition} resource type. */
    CompartmentDefinition,
    /** The FHIR R4 {@code Composition} resource type. */
    Composition,
    /** The FHIR R4 {@code ConceptMap} resource type. */
    ConceptMap,
    /** The FHIR R4 {@code Condition} resource type. */
    Condition,
    /** The FHIR R4 {@code Consent} resource type. */
    Consent,
    /** The FHIR R4 {@code Contract} resource type. */
    Contract,
    /** The FHIR R4 {@code Coverage} resource type. */
    Coverage,
    /** The FHIR R4 {@code CoverageEligibilityRequest} resource type. */
    CoverageEligibilityRequest,
    /** The FHIR R4 {@code CoverageEligibilityResponse} resource type. */
    CoverageEligibilityResponse,
    /** The FHIR R4 {@code DetectedIssue} resource type. */
    DetectedIssue,
    /** The FHIR R4 {@code Device} resource type. */
    Device,
    /** The FHIR R4 {@code DeviceDefinition} resource type. */
    DeviceDefinition,
    /** The FHIR R4 {@code DeviceMetric} resource type. */
    DeviceMetric,
    /** The FHIR R4 {@code DeviceRequest} resource type. */
    DeviceRequest,
    /** The FHIR R4 {@code DeviceUseStatement} resource type. */
    DeviceUseStatement,
    /** The FHIR R4 {@code DiagnosticReport} resource type. */
    DiagnosticReport,
    /** The FHIR R4 {@code DocumentManifest} resource type. */
    DocumentManifest,
    /** The FHIR R4 {@code DocumentReference} resource type. */
    DocumentReference,
    /** The FHIR R4 {@code EffectEvidenceSynthesis} resource type. */
    EffectEvidenceSynthesis,
    /** The FHIR R4 {@code Encounter} resource type. */
    Encounter,
    /** The FHIR R4 {@code Endpoint} resource type. */
    Endpoint,
    /** The FHIR R4 {@code EnrollmentRequest} resource type. */
    EnrollmentRequest,
    /** The FHIR R4 {@code EnrollmentResponse} resource type. */
    EnrollmentResponse,
    /** The FHIR R4 {@code EpisodeOfCare} resource type. */
    EpisodeOfCare,
    /** The FHIR R4 {@code EventDefinition} resource type. */
    EventDefinition,
    /** The FHIR R4 {@code Evidence} resource type. */
    Evidence,
    /** The FHIR R4 {@code EvidenceVariable} resource type. */
    EvidenceVariable,
    /** The FHIR R4 {@code ExampleScenario} resource type. */
    ExampleScenario,
    /** The FHIR R4 {@code ExplanationOfBenefit} resource type. */
    ExplanationOfBenefit,
    /** The FHIR R4 {@code FamilyMemberHistory} resource type. */
    FamilyMemberHistory,
    /** The FHIR R4 {@code Flag} resource type. */
    Flag,
    /** The FHIR R4 {@code Goal} resource type. */
    Goal,
    /** The FHIR R4 {@code GraphDefinition} resource type. */
    GraphDefinition,
    /** The FHIR R4 {@code Group} resource type. */
    Group,
    /** The FHIR R4 {@code GuidanceResponse} resource type. */
    GuidanceResponse,
    /** The FHIR R4 {@code HealthcareService} resource type. */
    HealthcareService,
    /** The FHIR R4 {@code ImagingStudy} resource type. */
    ImagingStudy,
    /** The FHIR R4 {@code Immunization} resource type. */
    Immunization,
    /** The FHIR R4 {@code ImmunizationEvaluation} resource type. */
    ImmunizationEvaluation,
    /** The FHIR R4 {@code ImmunizationRecommendation} resource type. */
    ImmunizationRecommendation,
    /** The FHIR R4 {@code ImplementationGuide} resource type. */
    ImplementationGuide,
    /** The FHIR R4 {@code InsurancePlan} resource type. */
    InsurancePlan,
    /** The FHIR R4 {@code Invoice} resource type. */
    Invoice,
    /** The FHIR R4 {@code Library} resource type. */
    Library,
    /** The FHIR R4 {@code Linkage} resource type. */
    Linkage,
    /** The FHIR R4 {@code List} resource type. */
    List,
    /** The FHIR R4 {@code Location} resource type. */
    Location,
    /** The FHIR R4 {@code Measure} resource type. */
    Measure,
    /** The FHIR R4 {@code MeasureReport} resource type. */
    MeasureReport,
    /** The FHIR R4 {@code Media} resource type. */
    Media,
    /** The FHIR R4 {@code Medication} resource type. */
    Medication,
    /** The FHIR R4 {@code MedicationAdministration} resource type. */
    MedicationAdministration,
    /** The FHIR R4 {@code MedicationDispense} resource type. */
    MedicationDispense,
    /** The FHIR R4 {@code MedicationKnowledge} resource type. */
    MedicationKnowledge,
    /** The FHIR R4 {@code MedicationRequest} resource type. */
    MedicationRequest,
    /** The FHIR R4 {@code MedicationStatement} resource type. */
    MedicationStatement,
    /** The FHIR R4 {@code MedicinalProduct} resource type. */
    MedicinalProduct,
    /** The FHIR R4 {@code MedicinalProductAuthorization} resource type. */
    MedicinalProductAuthorization,
    /** The FHIR R4 {@code MedicinalProductContraindication} resource type. */
    MedicinalProductContraindication,
    /** The FHIR R4 {@code MedicinalProductIndication} resource type. */
    MedicinalProductIndication,
    /** The FHIR R4 {@code MedicinalProductIngredient} resource type. */
    MedicinalProductIngredient,
    /** The FHIR R4 {@code MedicinalProductInteraction} resource type. */
    MedicinalProductInteraction,
    /** The FHIR R4 {@code MedicinalProductManufactured} resource type. */
    MedicinalProductManufactured,
    /** The FHIR R4 {@code MedicinalProductPackaged} resource type. */
    MedicinalProductPackaged,
    /** The FHIR R4 {@code MedicinalProductPharmaceutical} resource type. */
    MedicinalProductPharmaceutical,
    /** The FHIR R4 {@code MedicinalProductUndesirableEffect} resource type. */
    MedicinalProductUndesirableEffect,
    /** The FHIR R4 {@code MessageDefinition} resource type. */
    MessageDefinition,
    /** The FHIR R4 {@code MessageHeader} resource type. */
    MessageHeader,
    /** The FHIR R4 {@code MolecularSequence} resource type. */
    MolecularSequence,
    /** The FHIR R4 {@code NamingSystem} resource type. */
    NamingSystem,
    /** The FHIR R4 {@code NutritionOrder} resource type. */
    NutritionOrder,
    /** The FHIR R4 {@code Observation} resource type. */
    Observation,
    /** The FHIR R4 {@code ObservationDefinition} resource type. */
    ObservationDefinition,
    /** The FHIR R4 {@code OperationDefinition} resource type. */
    OperationDefinition,
    /** The FHIR R4 {@code OperationOutcome} resource type. */
    OperationOutcome,
    /** The FHIR R4 {@code Organization} resource type. */
    Organization,
    /** The FHIR R4 {@code OrganizationAffiliation} resource type. */
    OrganizationAffiliation,
    /** The FHIR R4 {@code Parameters} resource type. */
    Parameters,
    /** The FHIR R4 {@code Patient} resource type. */
    Patient,
    /** The FHIR R4 {@code PaymentNotice} resource type. */
    PaymentNotice,
    /** The FHIR R4 {@code PaymentReconciliation} resource type. */
    PaymentReconciliation,
    /** The FHIR R4 {@code Person} resource type. */
    Person,
    /** The FHIR R4 {@code PlanDefinition} resource type. */
    PlanDefinition,
    /** The FHIR R4 {@code Practitioner} resource type. */
    Practitioner,
    /** The FHIR R4 {@code PractitionerRole} resource type. */
    PractitionerRole,
    /** The FHIR R4 {@code Procedure} resource type. */
    Procedure,
    /** The FHIR R4 {@code Provenance} resource type. */
    Provenance,
    /** The FHIR R4 {@code Questionnaire} resource type. */
    Questionnaire,
    /** The FHIR R4 {@code QuestionnaireResponse} resource type. */
    QuestionnaireResponse,
    /** The FHIR R4 {@code RelatedPerson} resource type. */
    RelatedPerson,
    /** The FHIR R4 {@code RequestGroup} resource type. */
    RequestGroup,
    /** The FHIR R4 {@code ResearchDefinition} resource type. */
    ResearchDefinition,
    /** The FHIR R4 {@code ResearchElementDefinition} resource type. */
    ResearchElementDefinition,
    /** The FHIR R4 {@code ResearchStudy} resource type. */
    ResearchStudy,
    /** The FHIR R4 {@code ResearchSubject} resource type. */
    ResearchSubject,
    /** The FHIR R4 {@code RiskAssessment} resource type. */
    RiskAssessment,
    /** The FHIR R4 {@code RiskEvidenceSynthesis} resource type. */
    RiskEvidenceSynthesis,
    /** The FHIR R4 {@code Schedule} resource type. */
    Schedule,
    /** The FHIR R4 {@code SearchParameter} resource type. */
    SearchParameter,
    /** The FHIR R4 {@code ServiceRequest} resource type. */
    ServiceRequest,
    /** The FHIR R4 {@code Slot} resource type. */
    Slot,
    /** The FHIR R4 {@code Specimen} resource type. */
    Specimen,
    /** The FHIR R4 {@code SpecimenDefinition} resource type. */
    SpecimenDefinition,
    /** The FHIR R4 {@code StructureDefinition} resource type. */
    StructureDefinition,
    /** The FHIR R4 {@code StructureMap} resource type. */
    StructureMap,
    /** The FHIR R4 {@code Subscription} resource type. */
    Subscription,
    /** The FHIR R4 {@code Substance} resource type. */
    Substance,
    /** The FHIR R4 {@code SubstanceNucleicAcid} resource type. */
    SubstanceNucleicAcid,
    /** The FHIR R4 {@code SubstancePolymer} resource type. */
    SubstancePolymer,
    /** The FHIR R4 {@code SubstanceProtein} resource type. */
    SubstanceProtein,
    /** The FHIR R4 {@code SubstanceReferenceInformation} resource type. */
    SubstanceReferenceInformation,
    /** The FHIR R4 {@code SubstanceSourceMaterial} resource type. */
    SubstanceSourceMaterial,
    /** The FHIR R4 {@code SubstanceSpecification} resource type. */
    SubstanceSpecification,
    /** The FHIR R4 {@code SupplyDelivery} resource type. */
    SupplyDelivery,
    /** The FHIR R4 {@code SupplyRequest} resource type. */
    SupplyRequest,
    /** The FHIR R4 {@code Task} resource type. */
    Task,
    /** The FHIR R4 {@code TerminologyCapabilities} resource type. */
    TerminologyCapabilities,
    /** The FHIR R4 {@code TestReport} resource type. */
    TestReport,
    /** The FHIR R4 {@code TestScript} resource type. */
    TestScript,
    /** The FHIR R4 {@code ValueSet} resource type. */
    ValueSet,
    /** The FHIR R4 {@code VerificationResult} resource type. */
    VerificationResult,
    /** The FHIR R4 {@code VisionPrescription} resource type. */
    VisionPrescription;

    /** Recognised FHIR release designation carried at the Release containment level. */
    public static final String FHIR_R4_RELEASE = "R4";

    /** Specific FHIR R4 technical version carried as the Release entry's version. */
    public static final String FHIR_R4_VERSION = "4.0.1";
}
