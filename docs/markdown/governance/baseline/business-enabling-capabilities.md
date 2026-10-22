<!-- continuum-source-succession -->
------- Legacy Content - Superseded ------- Start ------
Superseded as an active architectural carrier by the [canonical element continuum](../../../../enterprise-architecture/documentation/continuum-manifest/published/1.1.5/README.md).
[Exact source/identity correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json) records this historical generated carrier. Its original bytes and then-current claims are retained below; it is no longer a current authoring source.
---------------------------------------------------------

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Baseline — Business Enabling Capabilities

[Baseline authority and reading guide](README.md). Currentness does not imply approval. Definitions are reproduced without semantic rewriting. IDs below retain their distinct roles.

## Catalogue

- [Care Administration Enablement](#hcm-bec-care-administration-information-enablement) — Proposed; current; placement Proposed
- [Care Plan Administration](#hcm-bec-care-plan-administration) — Canonical; current; placement Proposed
- [Care Plan Lifecycle & Coordination](#hcm-bec-reviewed-care-plan-lifecycle-coordination) — Proposed; current; placement Proposed
- [Community Care Coordination](#hcm-bec-community-care) — Canonical; current; placement Proposed
- [Outreach Care Continuity](#hcm-bec-outreach-care) — Canonical; current; placement Proposed
- [Client-Centred Administration](#hcm-bec-client-centred-administration) — Canonical; current; placement Proposed
- [Client Intake & Demographic Update](#hcm-bec-reviewed-client-intake-demographic-update) — Proposed; current; placement Proposed
- [Screening and Immunisation Enablement](#hcm-bec-preventive-care) — Canonical; current; placement Proposed
- [Clinical Charging & Claims Administration](#hcm-bec-clinical-charging-claims-administration) — Canonical; current; placement Proposed
- [Clinical Communication Administration](#hcm-bec-clinical-communication-administration) — Canonical; current; placement Proposed
- [Secure Clinical Communication](#hcm-bec-reviewed-secure-clinical-communication) — Proposed; current; placement Proposed
- [Clinical Record Administration](#hcm-bec-clinical-record-administration) — Canonical; current; placement Proposed
- [Clinical Documentation Lifecycle Administration](#hcm-bec-reviewed-clinical-documentation-lifecycle-administration) — Proposed; current; placement Proposed
- [Diagnostic Administration](#hcm-bec-diagnostic-administration) — Canonical; current; placement Proposed
- [Diagnostic Service Integration](#hcm-bec-diagnostic-services) — Canonical; current; placement Proposed
- [Diagnostic Workflow Management](#hcm-bec-reviewed-diagnostic-workflow-management) — Proposed; current; placement Proposed
- [Episode & Encounter Administration](#hcm-bec-episode-encounter-administration) — Canonical; current; placement Proposed
- [Acute Care Monitoring & Event Coordination](#hcm-bec-acute-care) — Canonical; current; placement Proposed
- [Encounter Lifecycle Management](#hcm-bec-reviewed-encounter-lifecycle-management) — Proposed; current; placement Proposed
- [Funding & Coverage Administration](#hcm-bec-funding-coverage-administration) — Canonical; current; placement Proposed
- [Medication Administration](#hcm-bec-medication-administration) — Canonical; current; placement Proposed
- [Medication Order & Dispense Management](#hcm-bec-reviewed-medication-order-dispense-management) — Proposed; current; placement Proposed
- [Medication Therapy Provision and Monitoring](#hcm-bec-medication-therapy) — Canonical; current; placement Proposed
- [Order Administration](#hcm-bec-order-administration) — Canonical; current; placement Proposed
- [Closed-Loop Order Progression](#hcm-bec-reviewed-closed-loop-order-progression) — Proposed; current; placement Proposed
- [Procedure Administration](#hcm-bec-procedure-administration) — Canonical; current; placement Proposed
- [Procedure & Surgical Administration](#hcm-bec-reviewed-procedure-surgical-administration) — Proposed; current; placement Proposed
- [Referral Administration](#hcm-bec-referral-administration) — Canonical; current; placement Proposed
- [Referral Intake & Progression](#hcm-bec-reviewed-referral-intake-progression) — Proposed; current; placement Proposed
- [Scheduling Administration](#hcm-bec-scheduling-administration) — Canonical; current; placement Proposed
- [Appointment & Booking Administration](#hcm-bec-reviewed-appointment-booking-administration) — Proposed; current; placement Proposed
- [Clinical Service Dependency Management](#hcm-bec-reviewed-clinical-service-dependency-management) — Proposed; current; placement Proposed
- [Acute Care](#hcm-bec-reviewed-acute-care) — Proposed; current; placement Proposed
- [Allied Health](#hcm-bec-allied-health) — Canonical; current; placement Proposed
- [Ambulatory Care](#hcm-bec-ambulatory-care) — Canonical; current; placement Proposed
- [Clinical Assessment](#hcm-bec-clinical-assessment) — Canonical; current; placement Proposed
- [Community Care](#hcm-bec-reviewed-community-care) — Proposed; current; placement Proposed
- [Diagnostic Services](#hcm-bec-reviewed-diagnostic-services) — Proposed; current; placement Proposed
- [Emergency Care](#hcm-bec-reviewed-emergency-care) — Proposed; current; placement Proposed
- [Inpatient Care](#hcm-bec-reviewed-inpatient-care) — Proposed; current; placement Proposed
- [Medication Therapy](#hcm-bec-reviewed-medication-therapy) — Proposed; current; placement Proposed
- [Outreach Care](#hcm-bec-reviewed-outreach-care) — Proposed; current; placement Proposed
- [Preventive Care](#hcm-bec-reviewed-preventive-care) — Proposed; current; placement Proposed
- [Primary Care](#hcm-bec-reviewed-primary-care) — Proposed; current; placement Proposed
- [Rehabilitation](#hcm-bec-rehabilitation) — Canonical; current; placement Proposed
- [Remote & Virtual Care](#hcm-bec-reviewed-remote-virtual-care) — Proposed; current; placement Proposed
- [Surgical & Procedural Care](#hcm-bec-surgical-procedural-care) — Canonical; current; placement Proposed
- [Therapeutic Services](#hcm-bec-therapeutic-services) — Canonical; current; placement Proposed
- [Entity Information Enablement](#hcm-bec-healthcare-entity-information-enablement) — Proposed; current; placement Proposed
- [Client Administration](#hcm-bec-client-administration) — Canonical; current; placement Proposed
- [Client Privacy Guardrail Management](#hcm-bec-client-privacy) — Canonical; current; placement Proposed
- [Client Relationship Administration](#hcm-bec-client-relationship) — Canonical; current; placement Proposed
- [Healthcare Subject Profile Administration](#hcm-bec-healthcare-subject) — Canonical; current; placement Proposed
- [Person Identity Administration](#hcm-bec-person-identity) — Canonical; current; placement Proposed
- [Clinical Device Administration](#hcm-bec-clinical-device-administration) — Canonical; current; placement Proposed
- [Clinical Device Profile Administration](#hcm-bec-device-definition) — Proposed; current; placement Proposed
- [Device Connectivity Compliance Management](#hcm-bec-reviewed-device-connectivity-compliance-management) — Proposed; current; placement Proposed
- [Device OA&M Metadata Management](#hcm-bec-reviewed-device-oa-m-metadata-management) — Proposed; current; placement Proposed
- [Device Utilisation Administration](#hcm-bec-reviewed-device-utilisation-administration) — Proposed; current; placement Proposed
- [Health Materiel Administration](#hcm-bec-health-product-administration) — Canonical; current; placement Proposed
- [Health Materiel Catalogue & Formulary Administration](#hcm-bec-product-formulary-catalogue) — Proposed; current; placement Proposed
- [Health Materiel Profile Administration](#hcm-bec-health-product-definition) — Proposed; current; placement Proposed
- [Health Materiel Traceability Administration](#hcm-bec-product-batch-serial-tracking) — Proposed; current; placement Proposed
- [Health Service Administration](#hcm-bec-health-service-administration) — Canonical; current; placement Proposed
- [Healthcare Service Profile Administration](#hcm-bec-healthcare-service-definition) — Proposed; current; placement Proposed
- [Location Administration](#hcm-bec-location-administration) — Canonical; current; placement Proposed
- [Care-Place Definition Administration](#hcm-bec-care-place-definition) — Proposed; current; placement Proposed
- [Healthcare Location Profile Administration](#hcm-bec-healthcare-location) — Proposed; current; placement Proposed
- [Organisation Administration](#hcm-bec-organisation-administration) — Canonical; current; placement Proposed
- [Healthcare Organisation Profile Administration](#hcm-bec-healthcare-organisation) — Proposed; current; placement Proposed
- [Provider Administration](#hcm-bec-provider-administration) — Canonical; current; placement Proposed
- [Practitioner Profile Administration](#hcm-bec-practitioner) — Proposed; current; placement Proposed
- [Practitioner Role Administration](#hcm-bec-practitioner-role) — Proposed; current; placement Proposed
- [Vendor Liaison](#hcm-bec-reviewed-vendor-liaison) — Proposed; current; placement Proposed
- [Health Informatics Management](#hcm-bec-secondary-health-information-use-enablement) — Proposed; current; placement Proposed
- [Population Health Information Reporting](#hcm-bec-population-health-notification-enablement) — Proposed; current; placement Proposed
- [Research Information Curation](#hcm-bec-authorised-research-information-provision) — Proposed; current; placement Proposed
- [Service Planning Data Management](#hcm-bec-service-planning-information-provision) — Proposed; current; placement Proposed
- [Health Service Operations Enablement](#hcm-bec-care-operations-coordination-enablement) — Proposed; current; placement Proposed
- [Bed & Care-Place Management](#hcm-bec-bed-care-place-management) — Canonical; current; placement Proposed
- [Bed & Care-Place State Administration](#hcm-bec-reviewed-bed-care-place-state-administration) — Proposed; current; placement Proposed
- [Bed & Care-Place Turnover Coordination](#hcm-bec-reviewed-bed-care-place-turnover-coordination) — Proposed; current; placement Proposed
- [Clinic & Practice Operations](#hcm-bec-clinic-practice-operations) — Canonical; current; placement Proposed
- [Session & Queue Coordination](#hcm-bec-reviewed-session-queue-coordination) — Proposed; current; placement Proposed
- [Clinical Competency Administration](#hcm-bec-clinical-competency-management) — Canonical; current; placement Proposed
- [Clinical Logistics Coordination](#hcm-bec-clinical-logistics-coordination) — Canonical; current; placement Proposed
- [Specimen Metadata Management](#hcm-bec-reviewed-specimen-metadata-management) — Proposed; current; placement Proposed
- [Specimen Movement Traceability](#hcm-bec-reviewed-specimen-movement-traceability) — Proposed; current; placement Proposed
- [Clinical Qualification Administration](#hcm-bec-clinical-qualification-management) — Canonical; current; placement Proposed
- [Organisation Clinical Certification & Compliance Support](#hcm-bec-reviewed-organisation-clinical-certification-compliance-support) — Proposed; current; placement Proposed
- [Provider Clinical Qualification Tracking](#hcm-bec-reviewed-provider-clinical-qualification-tracking) — Proposed; current; placement Proposed
- [Clinical Rostering](#hcm-bec-clinical-rostering) — Canonical; current; placement Proposed
- [Clinical Roster Capture & Communication](#hcm-bec-reviewed-clinical-roster-capture-communication) — Proposed; current; placement Proposed
- [Clinical Supervision](#hcm-bec-clinical-supervision) — Canonical; current; placement Proposed
- [Clinical Escalation Path Management](#hcm-bec-reviewed-clinical-escalation-path-management) — Proposed; current; placement Proposed
- [Clinical Training & Education](#hcm-bec-clinical-training-education) — Canonical; current; placement Proposed
- [Clinical Workforce Capability Monitoring](#hcm-bec-clinical-workforce-management) — Canonical; current; placement Proposed
- [Credential Management](#hcm-bec-reviewed-credential-management) — Proposed; current; placement Proposed
- [Clinical Service Credential Definition Management](#hcm-bec-reviewed-clinical-service-credential-definition-management) — Proposed; current; placement Proposed
- [Discharge Management](#hcm-bec-discharge-management) — Canonical; current; placement Proposed
- [Discharge Progression & Coordination](#hcm-bec-reviewed-discharge-progression-coordination) — Proposed; current; placement Proposed
- [Emergency Department Operations](#hcm-bec-emergency-department-operations) — Canonical; current; placement Proposed
- [Emergency Care Progression](#hcm-bec-emergency-care) — Canonical; current; placement Proposed
- [Emergency Department Operational Coordination](#hcm-bec-reviewed-emergency-department-operational-coordination) — Proposed; current; placement Proposed
- [Mobile Staff Management](#hcm-bec-mobile-staff-management) — Canonical; current; placement Proposed
- [Mobile Worker Dispatch & Safety Coordination](#hcm-bec-reviewed-mobile-worker-dispatch-safety-coordination) — Proposed; current; placement Proposed
- [On-Call Management](#hcm-bec-on-call-management) — Canonical; current; placement Proposed
- [On-Call Availability & Contact Resolution](#hcm-bec-reviewed-on-call-availability-contact-resolution) — Proposed; current; placement Proposed
- [Outpatient Operations](#hcm-bec-outpatient-operations) — Canonical; current; placement Proposed
- [Outpatient Flow Coordination](#hcm-bec-reviewed-outpatient-flow-coordination) — Proposed; current; placement Proposed
- [Patient Transport](#hcm-bec-patient-transport) — Canonical; current; placement Proposed
- [Patient Transfer Coordination](#hcm-bec-reviewed-patient-transfer-coordination) — Proposed; current; placement Proposed
- [Service Capacity Management](#hcm-bec-service-capacity-management) — Canonical; current; placement Proposed
- [Service Demand & Capacity Coordination](#hcm-bec-reviewed-service-demand-capacity-coordination) — Proposed; current; placement Proposed
- [Service Establishment Support](#hcm-bec-reviewed-service-establishment-support) — Proposed; current; placement Proposed
- [Theatre Operations](#hcm-bec-theatre-operations) — Canonical; current; placement Proposed
- [Perioperative Logistics Coordination](#hcm-bec-reviewed-perioperative-logistics-coordination) — Proposed; current; placement Proposed
- [Ward Operations](#hcm-bec-ward-operations) — Canonical; current; placement Proposed
- [Multidisciplinary Inpatient Care Coordination](#hcm-bec-inpatient-care) — Canonical; current; placement Proposed
- [Ward Logistics Coordination](#hcm-bec-reviewed-ward-logistics-coordination) — Proposed; current; placement Proposed
- [Work Allocation & Dispatch](#hcm-bec-work-allocation-dispatch) — Canonical; current; placement Proposed
- [Operational Work Assignment & Dispatch Coordination](#hcm-bec-reviewed-operational-work-assignment-dispatch-coordination) — Proposed; current; placement Proposed
- [Independent Assurance Enablement](#hcm-bec-independent-service-assurance-enablement) — Proposed; current; placement Proposed
- [Assurance Service Review](#hcm-bec-reviewed-assurance-service-review) — Proposed; current; placement Proposed
- [Assurance Service Evaluation](#hcm-bec-reviewed-assurance-service-evaluation) — Proposed; current; placement Proposed
- [Assurance Specification](#hcm-bec-assurance-specification-enablement) — Proposed; current; placement Proposed
- [Assurance Criteria Administration](#hcm-bec-assurance-criteria-management) — Canonical; current; placement Proposed
- [Assurance Design](#hcm-bec-assurance-design) — Canonical; current; placement Proposed
- [Shared Information & Coordination Enablement](#hcm-bec-governed-health-information-enablement) — Proposed; current; placement Proposed
- [Calendar Management](#hcm-bec-calendar-management) — Canonical; current; placement Proposed
- [Operational & Clinical Calendar Coordination](#hcm-bec-reviewed-operational-clinical-calendar-coordination) — Proposed; current; placement Proposed
- [Clinical Collaboration](#hcm-bec-clinical-collaboration) — Canonical; current; placement Proposed
- [Clinical Context Collaboration](#hcm-bec-reviewed-clinical-context-collaboration) — Proposed; current; placement Proposed
- [Clinical Knowledge Services](#hcm-bec-clinical-knowledge-services) — Canonical; current; placement Proposed
- [Clinical Knowledge Artefact Distribution Management](#hcm-bec-reviewed-clinical-knowledge-artefact-distribution-management) — Proposed; current; placement Proposed
- [Clinical Knowledge Request Administration](#hcm-bec-reviewed-clinical-knowledge-request-administration) — Proposed; current; placement Proposed
- [Governed Reference Dataset Administration](#hcm-bec-governed-reference-dataset-administration) — Proposed; current; placement Proposed
- [General Collaboration](#hcm-bec-general-collaboration) — Canonical; current; placement Proposed
- [Operational Collaboration](#hcm-bec-reviewed-operational-collaboration) — Proposed; current; placement Proposed
- [Health Information Access](#hcm-bec-health-information-access) — Canonical; current; placement Proposed
- [Governed Health Information Retrieval](#hcm-bec-reviewed-governed-health-information-retrieval) — Proposed; current; placement Proposed
- [Health Information Communication](#hcm-bec-reviewed-health-information-communication) — Proposed; current; placement Proposed
- [Patient Health Library Access](#hcm-bec-reviewed-patient-health-library-access) — Proposed; current; placement Proposed
- [Patient Notification Services](#hcm-bec-reviewed-patient-notification-services) — Proposed; current; placement Proposed
- [Patient Self-Service](#hcm-bec-reviewed-patient-self-service) — Proposed; current; placement Proposed
- [Patient Task Communication Services](#hcm-bec-reviewed-patient-task-communication-services) — Proposed; current; placement Proposed
- [Health Information Control](#hcm-bec-health-information-control) — Canonical; current; placement Proposed
- [Patient Health Information Disclosure Management](#hcm-bec-reviewed-patient-health-information-disclosure-management) — Proposed; current; placement Proposed
- [Patient Health Information Filter Management](#hcm-bec-reviewed-patient-health-information-filter-management) — Proposed; current; placement Proposed
- [Patient Health Information Restriction Management](#hcm-bec-reviewed-patient-health-information-restriction-management) — Proposed; current; placement Proposed
- [Health Information Exchange Enablement](#hcm-bec-health-information-exchange) — Canonical; current; placement Proposed
- [HIE Endpoint State Management](#hcm-bec-reviewed-hie-endpoint-state-management) — Proposed; current; placement Proposed
- [HIE Implementation Management](#hcm-bec-reviewed-hie-implementation-management) — Proposed; current; placement Proposed
- [HIE Requirement Management](#hcm-bec-reviewed-hie-requirement-management) — Proposed; current; placement Proposed
- [Information Asset Stewardship Management](#hcm-bec-reviewed-information-asset-stewardship-management) — Proposed; current; placement Proposed
- [Information Asset Definition](#hcm-bec-reviewed-information-asset-definition) — Proposed; current; placement Proposed
- [Information Asset Tag Management](#hcm-bec-reviewed-information-asset-tag-management) — Proposed; current; placement Proposed
- [Information Content Maintainer Management](#hcm-bec-reviewed-information-content-maintainer-management) — Proposed; current; placement Proposed
- [Data Provenance Capture](#hcm-bec-information-lineage-stewardship) — Proposed; current; placement Proposed
- [Data Scientist Administration](#hcm-bec-reviewed-data-scientist-administration) — Proposed; current; placement Proposed
- [Information Product Administration](#hcm-bec-reviewed-information-product-administration) — Proposed; current; placement Proposed
- [Information Design Governance](#hcm-bec-information-design-governance) — Canonical; current; placement Proposed
- [Enterprise Canonical/Reusable Model Design](#hcm-bec-reviewed-enterprise-canonical-reusable-model-design) — Proposed; current; placement Proposed
- [Enterprise Canonical/Reusable Model Library Management](#hcm-bec-reviewed-enterprise-canonical-reusable-model-library-management) — Proposed; current; placement Proposed
- [Enterprise Canonical/Reusable Model Utilisation Support](#hcm-bec-reviewed-enterprise-canonical-reusable-model-utilisation-support) — Proposed; current; placement Proposed
- [Enterprise Reference Data Stewardship](#hcm-bec-reviewed-enterprise-reference-data-stewardship) — Proposed; current; placement Proposed
- [Patient Clinical Record](#hcm-bec-patient-clinical-record) — Canonical; current; placement Proposed
- [Linked Information Curation](#hcm-bec-associated-clinical-binary-content-enablement) — Proposed; current; placement Proposed
- [Longitudinal Clinical Record Administration](#hcm-bec-reviewed-longitudinal-clinical-record-administration) — Proposed; current; placement Proposed
- [Primary Care Coordination](#hcm-bec-primary-care) — Canonical; current; placement Proposed
- [Remote Patient Monitoring](#hcm-bec-remote-virtual-care) — Canonical; current; placement Proposed
- [Presentation Services](#hcm-bec-presentation-services) — Canonical; current; placement Proposed
- [Contextual Information Presentation](#hcm-bec-reviewed-contextual-information-presentation) — Proposed; current; placement Proposed
- [Workflow & Activity Coordination](#hcm-bec-reviewed-workflow-activity-coordination) — Proposed; current; placement Proposed
- [Activity Routing Management](#hcm-bec-reviewed-activity-routing-management) — Proposed; current; placement Proposed
- [External Task Coordination](#hcm-bec-reviewed-external-task-coordination) — Proposed; current; placement Proposed
- [External Task Status & Traceability Management](#hcm-bec-reviewed-external-task-status-traceability-management) — Proposed; current; placement Proposed
- [Work Boundary Governance](#hcm-bec-reviewed-work-boundary-governance) — Proposed; current; placement Proposed
- [Service Offering Readiness Coordination](#hcm-bec-service-offering-readiness-coordination) — Proposed; pending_adjudication; placement Proposed
- [Credential Management](#hcm-bec-credential-management) — Canonical; pending_adjudication; placement Proposed
- [Entity-Specific Operational Coordination](#hcm-bec-entity-specific-operational-coordination) — Proposed; pending_adjudication; placement Proposed
- [Governed Operational Activity Design](#hcm-bec-governed-operational-activity-design) — Proposed; pending_adjudication; placement Proposed
- [Workflow & Activity Coordination](#hcm-bec-workflow-activity-coordination) — Canonical; pending_adjudication; placement Proposed
- [Independent Assurance Evaluation Enablement](#hcm-bec-independent-assurance-evaluation-enablement) — Proposed; pending_adjudication; placement Proposed
- [Governed Assurance](#hcm-bec-governed-assurance) — Canonical; pending_adjudication; placement Proposed
- [Information Authority Representation](#hcm-bec-information-authority-representation) — Proposed; pending_adjudication; placement Proposed
- [Health Information Communication](#hcm-bec-health-information-communication) — Canonical; pending_adjudication; placement Proposed
- [Governance Requirement Publication Support](#hcm-bec-governance-requirement-publication-support) — Proposed; pending_adjudication; placement Proposed
- [Significant Activity Audit Capture](#hcm-bec-significant-activity-audit-capture) — Proposed; pending_adjudication; placement Proposed
- [Care Access Transaction Enablement](#hcm-bec-care-access-transaction-enablement) — Proposed; superseded; placement Proposed
- [Care Documentation & Communication Enablement](#hcm-bec-care-documentation-communication-enablement) — Proposed; superseded; placement Proposed
- [Care Financial Information Enablement](#hcm-bec-care-financial-information-enablement) — Proposed; superseded; placement Proposed
- [Care Fulfilment Administration Enablement](#hcm-bec-care-fulfilment-administration-enablement) — Proposed; superseded; placement Proposed
- [Care Context Integration Enablement](#hcm-bec-care-context-integration-enablement) — Proposed; superseded; placement Proposed
- [Facility & Episodic Care Integration](#hcm-bec-facility-episodic-care-integration) — Proposed; superseded; placement Proposed
- [Longitudinal & Community Care Integration](#hcm-bec-longitudinal-community-care-integration) — Proposed; superseded; placement Proposed
- [Specialist Clinical Context Integration](#hcm-bec-specialist-clinical-context-integration) — Proposed; superseded; placement Proposed
- [Device Association](#hcm-bec-device-association) — Proposed; superseded; placement Proposed
- [Device Connectivity](#hcm-bec-device-connectivity) — Proposed; superseded; placement Proposed
- [Device Instance](#hcm-bec-device-instance) — Proposed; superseded; placement Proposed
- [Service Availability Definition](#hcm-bec-service-availability-definition) — Proposed; superseded; placement Proposed
- [Service Eligibility Definition](#hcm-bec-service-eligibility-definition) — Proposed; superseded; placement Proposed
- [Service Provision Relationship](#hcm-bec-service-provision-relationship) — Proposed; superseded; placement Proposed
- [Location Structure](#hcm-bec-location-structure) — Proposed; superseded; placement Proposed
- [Organisation Contact](#hcm-bec-organisation-contact) — Proposed; superseded; placement Proposed
- [Organisational Structure](#hcm-bec-organisational-structure) — Proposed; superseded; placement Proposed
- [Provider Engagement](#hcm-bec-provider-engagement) — Proposed; superseded; placement Proposed
- [Provider Relationship](#hcm-bec-provider-relationship) — Proposed; superseded; placement Proposed
- [Care Movement & Transition Coordination](#hcm-bec-care-movement-transition-coordination) — Proposed; superseded; placement Proposed
- [Care Resource & Capacity Coordination](#hcm-bec-care-resource-capacity-coordination) — Proposed; superseded; placement Proposed
- [Clinical Resource Management](#hcm-bec-clinical-resource-management) — Canonical; superseded; placement Proposed
- [Care Setting Operations Enablement](#hcm-bec-care-setting-operations-enablement) — Proposed; superseded; placement Proposed
- [Operational Workforce Coordination](#hcm-bec-operational-workforce-coordination) — Proposed; superseded; placement Proposed
- [Workforce Qualification & Development Enablement](#hcm-bec-workforce-qualification-development-enablement) — Proposed; superseded; placement Proposed
- [Human Collaboration & Work Enablement](#hcm-bec-human-collaboration-work-enablement) — Proposed; superseded; placement Proposed
- [Collaborative Interaction Enablement](#hcm-bec-collaborative-interaction-enablement) — Proposed; superseded; placement Proposed
- [Operational Activity & Temporal Enablement](#hcm-bec-operational-activity-temporal-enablement) — Proposed; superseded; placement Proposed
- [Clinical Information Stewardship Enablement](#hcm-bec-clinical-information-stewardship-enablement) — Proposed; superseded; placement Proposed
- [Health Information Connectivity Enablement](#hcm-bec-health-information-connectivity-enablement) — Proposed; superseded; placement Proposed
- [Information Protection & Accountability Enablement](#hcm-bec-information-protection-accountability-enablement) — Proposed; superseded; placement Proposed
- [Semantic & Reference Knowledge Enablement](#hcm-bec-semantic-reference-knowledge-enablement) — Proposed; superseded; placement Proposed

<a id="hcm-bec-care-administration-information-enablement"></a>

## Care Administration Enablement

Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems.

**Overview**

Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems.

**Intent**

Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems.

**Boundary**

Information administration does not perform the clinical act, own host booking engines or infer Transfer of Care from referral acceptance.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-administration-information-enablement |
| canonicalId | 28df27d55c514a37995145ab46945f78 |
| displayName | [CT1] Care Administration Enablement |
| friendlyName | CareAdministrationEnablement |
| canonicalName | CT1-BEC-CareAdministrationEnablement |
| Snapshot publication reference | HCM-BEC-care-administration-information-enablement |
| ArchiMate semantic ID | id-fe7b5e3e02bb53f59ab89c27a81e9e18 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Care Administration Enablement |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT |
| Name aliases | ["Care Administration Information Enablement"] |

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Care Plan Administration](business-enabling-capabilities.md#hcm-bec-care-plan-administration) — Canonical; placement Proposed
- [Client-Centred Administration](business-enabling-capabilities.md#hcm-bec-client-centred-administration) — Canonical; placement Proposed
- [Clinical Charging & Claims Administration](business-enabling-capabilities.md#hcm-bec-clinical-charging-claims-administration) — Canonical; placement Proposed
- [Clinical Communication Administration](business-enabling-capabilities.md#hcm-bec-clinical-communication-administration) — Canonical; placement Proposed
- [Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-clinical-record-administration) — Canonical; placement Proposed
- [Diagnostic Administration](business-enabling-capabilities.md#hcm-bec-diagnostic-administration) — Canonical; placement Proposed
- [Episode & Encounter Administration](business-enabling-capabilities.md#hcm-bec-episode-encounter-administration) — Canonical; placement Proposed
- [Funding & Coverage Administration](business-enabling-capabilities.md#hcm-bec-funding-coverage-administration) — Canonical; placement Proposed
- [Medication Administration](business-enabling-capabilities.md#hcm-bec-medication-administration) — Canonical; placement Proposed
- [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration) — Canonical; placement Proposed
- [Procedure Administration](business-enabling-capabilities.md#hcm-bec-procedure-administration) — Canonical; placement Proposed
- [Referral Administration](business-enabling-capabilities.md#hcm-bec-referral-administration) — Canonical; placement Proposed
- [Scheduling Administration](business-enabling-capabilities.md#hcm-bec-scheduling-administration) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-administration-information-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-care-plan-administration"></a>

## Care Plan Administration

Care Plan Administration receives care plans and coordinates the distribution of updates to authorised team members. It supports shared understanding of established clinical intent across participating organisations. The responsibility concerns the plan as managed information, distinct from deciding the intended course of care or performing the actions expressed by it.

**Overview**

Care Plan Administration receives care plans and coordinates the distribution of updates to authorised team members. It supports shared understanding of established clinical intent across participating organisations. The responsibility concerns the plan as managed information, distinct from deciding the intended course of care or performing the actions expressed by it.

**Intent**

Care Plan Administration receives care plans and coordinates the distribution of updates to authorised team members. It supports shared understanding of established clinical intent across participating organisations. The responsibility concerns the plan as managed information, distinct from deciding the intended course of care or performing the actions expressed by it.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Synchronising a plan does not author its clinical intent, approve treatment or prove planned care occurred.

### Harmonia Architectural Relevance — predecessor explanation

Timely propagation of multidisciplinary plan information materially supports continuity without transferring clinical planning authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:care-plan-administration |
| canonicalId | cf06eec5699f406fa30a00d2f76e3098 |
| displayName | [CT2] Care Plan Administration |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration |
| Snapshot publication reference | HCM-BEC-care-plan-administration |
| ArchiMate semantic ID | id-8c082e2d6c605903834291c424722f75 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Care Plan Administration |
| Reviewed name | Care Plan Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CARE-PLAN-ADMINISTRATION"] |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Care Plan Lifecycle & Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-care-plan-lifecycle-coordination) — Proposed; placement Proposed
- [Community Care Coordination](business-enabling-capabilities.md#hcm-bec-community-care) — Canonical; placement Proposed
- [Outreach Care Continuity](business-enabling-capabilities.md#hcm-bec-outreach-care) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:care-plan-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-care-plan-lifecycle-coordination"></a>

## Care Plan Lifecycle & Coordination

Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source.

**Overview**

Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-care-plan-lifecycle-coordination |
| canonicalId | 4cb7db65392f4fab801d7794af4d4b29 |
| displayName | [CT3] Care Plan Lifecycle & Coordination |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CarePlanLifecycleAndCoordination |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CarePlanLifecycleAndCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-care-plan-lifecycle-coordination |
| ArchiMate semantic ID | id-ab0baa5289245368afb3d9943f0dee5c |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Care Plan Lifecycle & Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-CARE-PLAN-LIFECYCLE-COORDINATION |

Parent: [Care Plan Administration](business-enabling-capabilities.md#hcm-bec-care-plan-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-care-plan-lifecycle-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-community-care"></a>

## Community Care Coordination

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Community Care integrates encounters, observations and plans across home, community and hospital outreach participants. It supports continuity where clinical activity occurs outside the hospital's immediate information environment. Receiving visit records and synchronising plan changes helps make community activity visible without turning the platform into the provider of that activity.

### Boundary — predecessor explanation

Plan synchronisation does not originate clinical intent, assign community work or establish that a visit occurred beyond its source evidence.

### Harmonia Architectural Relevance — predecessor explanation

Cross-organisational encounter and care-plan information materially supports community care continuity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:community-care |
| canonicalId | 8c8c6266d9ed43e09dbe44fcdd669132 |
| displayName | [CT3] Community Care Coordination |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CommunityCareCoordination |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CommunityCareCoordination |
| Snapshot publication reference | HCM-BEC-community-care |
| ArchiMate semantic ID | id-d835f657a75459268088115e97986dd1 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Community Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-COMMUNITY-CARE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-COMMUNITY-CARE"] |
| Name aliases | ["Community Care"] |

Parent: [Care Plan Administration](business-enabling-capabilities.md#hcm-bec-care-plan-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:community-care`) and the element's version-qualified Properties.

<a id="hcm-bec-outreach-care"></a>

## Outreach Care Continuity

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Outreach Care supplies portable clinical context and receives documentation from rural or remote outreach activity. It addresses continuity where the relevant care information may not be available through the usual on-site environment. The responsibility connects the prior record with the outcomes reported after the visit while keeping source and temporal context intelligible.

### Boundary — predecessor explanation

An offline-capable summary does not acquire clinical authority or guarantee that the remote copy represents current authoritative state.

### Harmonia Architectural Relevance — predecessor explanation

Context provision and outcome ingestion materially connect outreach activity to the longitudinal record.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:outreach-care |
| canonicalId | 8526d971b07f40de84ef05b0d192089c |
| displayName | [CT3] Outreach Care Continuity |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.OutreachCareContinuity |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-OutreachCareContinuity |
| Snapshot publication reference | HCM-BEC-outreach-care |
| ArchiMate semantic ID | id-c5461090ddce50daa204e0c031c184aa |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Outreach Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-OUTREACH-CARE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-OUTREACH-CARE"] |
| Name aliases | ["Outreach Care"] |

Parent: [Care Plan Administration](business-enabling-capabilities.md#hcm-bec-care-plan-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:outreach-care`) and the element's version-qualified Properties.

<a id="hcm-bec-client-centred-administration"></a>

## Client-Centred Administration

Client-Centred Administration receives demographic and registration changes and makes verified updates available to participating clinical systems. It connects administrative events from external PAS environments with the client context used across care. The enabling responsibility concerns integration of changes, rather than independently deciding the authoritative demographic truth whenever a source sends an update.

**Overview**

Client-Centred Administration receives demographic and registration changes and makes verified updates available to participating clinical systems. It connects administrative events from external PAS environments with the client context used across care. The enabling responsibility concerns integration of changes, rather than independently deciding the authoritative demographic truth whenever a source sends an update.

**Intent**

Client-Centred Administration receives demographic and registration changes and makes verified updates available to participating clinical systems. It connects administrative events from external PAS environments with the client context used across care. The enabling responsibility concerns integration of changes, rather than independently deciding the authoritative demographic truth whenever a source sends an update.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Ingestion and distribution do not establish master clinical identity reconciliation or authority over the originating registration process.

### Harmonia Architectural Relevance — predecessor explanation

Demographic changes materially affect downstream association and continuity, so their governed propagation is active enablement rather than passive directory context.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:client-centred-administration |
| canonicalId | 8752b7a937754b2faa296300c6a48911 |
| displayName | [CT2] Client-Centred Administration |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration |
| Snapshot publication reference | HCM-BEC-client-centred-administration |
| ArchiMate semantic ID | id-4759e7e551b45a3ab1b24088b8421b9f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Client-Centred Administration |
| Reviewed name | Client-Centred Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-CLIENT-CENTRED-ADMINISTRATION"] |
| Release scope | Not Addressed in R1.x / R2.x |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Client Intake & Demographic Update](business-enabling-capabilities.md#hcm-bec-reviewed-client-intake-demographic-update) — Proposed; placement Proposed
- [Screening and Immunisation Enablement](business-enabling-capabilities.md#hcm-bec-preventive-care) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:client-centred-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-client-intake-demographic-update"></a>

## Client Intake & Demographic Update

Administer the established client intake & demographic update information and coordination responsibility while preserving its source authority.

**Overview**

Administer the established client intake & demographic update information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the established client intake & demographic update information and coordination responsibility while preserving its source authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-client-intake-demographic-update |
| canonicalId | f51f8d6b7556424987c76d9a7465c67c |
| displayName | [CT3] Client Intake & Demographic Update |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ClientIntakeAndDemographicUpdate |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ClientIntakeAndDemographicUpdate |
| Snapshot publication reference | HCM-BEC-reviewed-client-intake-demographic-update |
| ArchiMate semantic ID | id-714b37e59d095316bb94389ebbc14e33 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-CLIENT-INTAKE-DEMOGRAPHIC-UPDATE |

Parent: [Client-Centred Administration](business-enabling-capabilities.md#hcm-bec-client-centred-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-client-intake-demographic-update`) and the element's version-qualified Properties.

<a id="hcm-bec-preventive-care"></a>

## Screening and Immunisation Enablement

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Preventive Care enables information flows around immunisation and screening recall. Administration records can be conveyed to national registers, while established reminders can reach designated providers. This Service Delivery context supplies bounded information enablement; it does not replace the broader Business Capability Preventative Health or its programme and cohort responsibilities.

### Boundary — predecessor explanation

Submission does not perform immunisation, and a recall notice does not determine individual clinical eligibility or complete screening.

### Harmonia Architectural Relevance — predecessor explanation

Register integration and recall distribution materially connect preventative activity across participants.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:preventive-care |
| canonicalId | 3e9016eaebae4ed0b67a5cacba2652f5 |
| displayName | [CT3] Screening and Immunisation Enablement |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ScreeningAndImmunisationEnablement |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ScreeningAndImmunisationEnablement |
| Snapshot publication reference | HCM-BEC-preventive-care |
| ArchiMate semantic ID | id-2a555773d20b5eaaac720432d7e6f4ec |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Preventive Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-PREVENTIVE-CARE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PREVENTIVE-CARE"] |
| Name aliases | ["Preventive Care"] |

Parent: [Client-Centred Administration](business-enabling-capabilities.md#hcm-bec-client-centred-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:preventive-care`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-charging-claims-administration"></a>

## Clinical Charging & Claims Administration

This capability supplies clinical activity summaries and charging records to billing contexts. It preserves a route from activity information to the systems responsible for financial processing without treating the export as a completed bill or claim. No Feature decomposition or comprehensive revenue-cycle responsibility is established.

**Overview**

This capability supplies clinical activity summaries and charging records to billing contexts. It preserves a route from activity information to the systems responsible for financial processing without treating the export as a completed bill or claim. No Feature decomposition or comprehensive revenue-cycle responsibility is established.

**Intent**

This capability supplies clinical activity summaries and charging records to billing contexts. It preserves a route from activity information to the systems responsible for financial processing without treating the export as a completed bill or claim. No Feature decomposition or comprehensive revenue-cycle responsibility is established.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Export of activity evidence does not confer pricing, billing, claim or accounting authority.

### Harmonia Architectural Relevance — predecessor explanation

Its Adjacent standing reflects information supplied to financial systems while the financial activity remains externally governed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-charging--claims-administration |
| canonicalId | af78ab7c89454a608cd4d34ec7703331 |
| displayName | [CT2] Clinical Charging & Claims Administration |
| friendlyName | CareAdministrationEnablement.ClinicalChargingAndClaimsAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalChargingAndClaimsAdministration |
| Snapshot publication reference | HCM-BEC-clinical-charging-claims-administration |
| ArchiMate semantic ID | id-5463239c9ef65cef98c5274ab6e8f6db |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Charging & Claims Administration |
| Reviewed name | Clinical Charging & Claims Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-CHARGING-CLAIMS-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FINANCIAL-INFORMATION-ENABLEMENT.CT3-BEC-CLINICAL-CHARGING-CLAIMS-ADMINISTRATION"] |
| Release scope | Not Addressed in R1.x / R2.x |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-charging--claims-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-communication-administration"></a>

## Clinical Communication Administration

This capability manages secure clinical communication transactions, delivery and receipt evidence, and the required enduring audit trail. Verified provider and team endpoints support appropriate addressing while transaction correlation makes the communication history intelligible. The responsibility preserves separate meanings for transport delivery, recipient acknowledgement and clinical or business disposition.

**Overview**

This capability manages secure clinical communication transactions, delivery and receipt evidence, and the required enduring audit trail. Verified provider and team endpoints support appropriate addressing while transaction correlation makes the communication history intelligible. The responsibility preserves separate meanings for transport delivery, recipient acknowledgement and clinical or business disposition.

**Intent**

This capability manages secure clinical communication transactions, delivery and receipt evidence, and the required enduring audit trail. Verified provider and team endpoints support appropriate addressing while transaction correlation makes the communication history intelligible. The responsibility preserves separate meanings for transport delivery, recipient acknowledgement and clinical or business disposition.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Delivery or read receipts do not prove clinical review, incorporation, approval or acceptance. Audit evidence must remain governed without unmasked PHI in diagnostic streams.

### Harmonia Architectural Relevance — predecessor explanation

Secure addressed communication and attributable transaction evidence are substantive platform responsibilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-communication-administration |
| canonicalId | b38f2c2811724734b5887b6dbaaabbfc |
| displayName | [CT2] Clinical Communication Administration |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration |
| Snapshot publication reference | HCM-BEC-clinical-communication-administration |
| ArchiMate semantic ID | id-69fe602fea3e5afc95441b03dd7c198a |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Communication Administration |
| Reviewed name | Clinical Communication Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION"] |
| Release scope | Not Addressed in R1.x / R2.x |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Secure Clinical Communication](business-enabling-capabilities.md#hcm-bec-reviewed-secure-clinical-communication) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-communication-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-secure-clinical-communication"></a>

## Secure Clinical Communication

Administer the established secure clinical communication information and coordination responsibility while preserving its source authority.

**Overview**

Administer the established secure clinical communication information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the established secure clinical communication information and coordination responsibility while preserving its source authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-secure-clinical-communication |
| canonicalId | eddb86c7922145439b9dcf0259f4aed5 |
| displayName | [CT3] Secure Clinical Communication |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration.SecureClinicalCommunication |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration.CT3-BEC-SecureClinicalCommunication |
| Snapshot publication reference | HCM-BEC-reviewed-secure-clinical-communication |
| ArchiMate semantic ID | id-b87005ff372252e1893fbd216a77d525 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.CT3-BEC-SECURE-CLINICAL-COMMUNICATION |

Parent: [Clinical Communication Administration](business-enabling-capabilities.md#hcm-bec-clinical-communication-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-secure-clinical-communication`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-record-administration"></a>

## Clinical Record Administration

Clinical Record Administration manages clinical documents as governed information with authorship, classification, versions and supersession history. It distinguishes a new document from an amendment or withdrawal and maintains metadata for discovery. Its purpose is to preserve an intelligible documentary record across change, rather than merely accumulate files.

**Overview**

Clinical Record Administration manages clinical documents as governed information with authorship, classification, versions and supersession history. It distinguishes a new document from an amendment or withdrawal and maintains metadata for discovery. Its purpose is to preserve an intelligible documentary record across change, rather than merely accumulate files.

**Intent**

Clinical Record Administration manages clinical documents as governed information with authorship, classification, versions and supersession history. It distinguishes a new document from an amendment or withdrawal and maintains metadata for discovery. Its purpose is to preserve an intelligible documentary record across change, rather than merely accumulate files.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

This responsibility owns document governance; exchange of documents does not transfer that ownership to the transporting capability. Preservation does not originate the clinical facts.

### Harmonia Architectural Relevance — predecessor explanation

Non-destructive document lifecycle and governed discovery are direct platform information-management responsibilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-record-administration |
| canonicalId | ce9e7e7b22054cefb707a650d5be7219 |
| displayName | [CT2] Clinical Record Administration |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration |
| Snapshot publication reference | HCM-BEC-clinical-record-administration |
| ArchiMate semantic ID | id-839d37ad5fcd510d924a18389bada633 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Record Administration |
| Reviewed name | Clinical Record Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION"] |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Clinical Documentation Lifecycle Administration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-documentation-lifecycle-administration) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-record-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-clinical-documentation-lifecycle-administration"></a>

## Clinical Documentation Lifecycle Administration

Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation.

**Overview**

Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-clinical-documentation-lifecycle-administration |
| canonicalId | df683100067b47e785037c27cfd17a3c |
| displayName | [CT3] Clinical Documentation Lifecycle Administration |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.ClinicalDocumentationLifecycleAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.CT3-BEC-ClinicalDocumentationLifecycleAdministration |
| Snapshot publication reference | HCM-BEC-reviewed-clinical-documentation-lifecycle-administration |
| ArchiMate semantic ID | id-31701b65e27a544297e8b3807896433e |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Clinical Documentation Lifecycle Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.CT3-BEC-CLINICAL-DOCUMENTATION-LIFECYCLE-ADMINISTRATION |

Parent: [Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-clinical-record-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-clinical-documentation-lifecycle-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-diagnostic-administration"></a>

## Diagnostic Administration

Diagnostic Administration correlates requests with specimens and encounters and integrates diagnostic reports into their request context. It connects the laboratory or imaging contribution with the participants expecting an outcome, preserving report identity and standing as updates occur. Its enabling role differs from performing the diagnostic procedure or professionally interpreting evidence.

**Overview**

Diagnostic Administration correlates requests with specimens and encounters and integrates diagnostic reports into their request context. It connects the laboratory or imaging contribution with the participants expecting an outcome, preserving report identity and standing as updates occur. Its enabling role differs from performing the diagnostic procedure or professionally interpreting evidence.

**Intent**

Diagnostic Administration correlates requests with specimens and encounters and integrates diagnostic reports into their request context. It connects the laboratory or imaging contribution with the participants expecting an outcome, preserving report identity and standing as updates occur. Its enabling role differs from performing the diagnostic procedure or professionally interpreting evidence.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

The finalised-report distribution Feature retains its narrower scope. Broader Domain03 behaviour covering preliminary, corrected or addendum information does not silently expand that Feature.

### Harmonia Architectural Relevance — predecessor explanation

Request, specimen and report correlation and governed distribution are substantive information-integration responsibilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:diagnostic-administration |
| canonicalId | 3cc720ba2e8b4d7a94bb349391d61895 |
| displayName | [CT2] Diagnostic Administration |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration |
| Snapshot publication reference | HCM-BEC-diagnostic-administration |
| ArchiMate semantic ID | id-77908c3422545c70ba9ce082db020c0f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Diagnostic Administration |
| Reviewed name | Diagnostic Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION"] |
| Release scope | Not Addressed in R1.x / R2.x |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Diagnostic Service Integration](business-enabling-capabilities.md#hcm-bec-diagnostic-services) — Canonical; placement Proposed
- [Diagnostic Workflow Management](business-enabling-capabilities.md#hcm-bec-reviewed-diagnostic-workflow-management) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:diagnostic-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-diagnostic-services"></a>

## Diagnostic Service Integration

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Diagnostic Services supports cross-provider availability of reports and diagnostic history. Result distribution brings established findings to the ordering and designated participants; history consolidation makes relevant evidence visible across institutional boundaries. This delivery context complements Diagnostic Administration's request and report associations without making information integration the professional diagnostic service itself.

### Boundary — predecessor explanation

Consolidating trends does not interpret diagnostic evidence or establish a patient-level clinical conclusion.

### Harmonia Architectural Relevance — predecessor explanation

Governed report distribution and integrated history materially enable diagnostic information use across care boundaries.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:diagnostic-services |
| canonicalId | 9ded2f3a28d24017bb921a0cdcf65a2b |
| displayName | [CT3] Diagnostic Service Integration |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticServiceIntegration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticServiceIntegration |
| Snapshot publication reference | HCM-BEC-diagnostic-services |
| ArchiMate semantic ID | id-8ad249d9cc7f5e85a84f1dbf5b7ccaaf |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Diagnostic Services |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-SERVICES |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-DIAGNOSTIC-SERVICES"] |
| Name aliases | ["Diagnostic Services"] |

Parent: [Diagnostic Administration](business-enabling-capabilities.md#hcm-bec-diagnostic-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:diagnostic-services`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-diagnostic-workflow-management"></a>

## Diagnostic Workflow Management

Administer the established diagnostic workflow management information and coordination responsibility while preserving its source authority.

**Overview**

Administer the established diagnostic workflow management information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the established diagnostic workflow management information and coordination responsibility while preserving its source authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-diagnostic-workflow-management |
| canonicalId | 270ed0e32b294458aa52cf57df7e55db |
| displayName | [CT3] Diagnostic Workflow Management |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement |
| Snapshot publication reference | HCM-BEC-reviewed-diagnostic-workflow-management |
| ArchiMate semantic ID | id-a0b4e1a965765006a8dc7b598f517bb3 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-WORKFLOW-MANAGEMENT |

Parent: [Diagnostic Administration](business-enabling-capabilities.md#hcm-bec-diagnostic-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-diagnostic-workflow-management`) and the element's version-qualified Properties.

<a id="hcm-bec-episode-encounter-administration"></a>

## Episode & Encounter Administration

This capability maintains identifiable encounter contexts and their governed progression, including association with care teams and locations. It enables information from an admission, visit or community contact to be understood within the relevant episode of activity. Encounter progression is an administrative and operational account, separate from the clinical acts that occur during it.

**Overview**

This capability maintains identifiable encounter contexts and their governed progression, including association with care teams and locations. It enables information from an admission, visit or community contact to be understood within the relevant episode of activity. Encounter progression is an administrative and operational account, separate from the clinical acts that occur during it.

**Intent**

This capability maintains identifiable encounter contexts and their governed progression, including association with care teams and locations. It enables information from an admission, visit or community contact to be understood within the relevant episode of activity. Encounter progression is an administrative and operational account, separate from the clinical acts that occur during it.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

The source lists ON_LEAVE in the Feature description but does not fully connect it in the Process illustration. That transition gap remains unresolved rather than completed by convention.

### Harmonia Architectural Relevance — predecessor explanation

Encounter context and tracked state are substantive platform responsibilities supporting correlation across records, orders and operational events.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:episode--encounter-administration |
| canonicalId | 61d7067aee0540259b3daca4287d66d2 |
| displayName | [CT2] Episode & Encounter Administration |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration |
| Snapshot publication reference | HCM-BEC-episode-encounter-administration |
| ArchiMate semantic ID | id-198c918e6b225485b2065dc763be8ce1 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Episode & Encounter Administration |
| Reviewed name | Episode & Encounter Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION"] |
| Release scope | Not Addressed in R1.x / R2.x |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Acute Care Monitoring & Event Coordination](business-enabling-capabilities.md#hcm-bec-acute-care) — Canonical; placement Proposed
- [Encounter Lifecycle Management](business-enabling-capabilities.md#hcm-bec-reviewed-encounter-lifecycle-management) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:episode--encounter-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-acute-care"></a>

## Acute Care Monitoring & Event Coordination

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Acute Care provides the information context needed around acute clinical events, including externally reported deterioration and timely access to history and active medications. It helps current events be interpreted alongside prior information rather than as isolated messages. Its Service Delivery context does not become a child of Individual Care Delivery's setting-independent Business Capability hierarchy.

### Boundary — predecessor explanation

Capturing a deterioration event does not determine clinical significance or prescribe an acute response.

### Harmonia Architectural Relevance — predecessor explanation

Harmonia materially supplies and correlates acute-care information while clinical decisions and intervention remain with the care service.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:acute-care |
| canonicalId | ee17b26b8c874cc3af50d07a787ce488 |
| displayName | [CT3] Acute Care Monitoring & Event Coordination |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.AcuteCareMonitoringAndEventCoordination |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-AcuteCareMonitoringAndEventCoordination |
| Snapshot publication reference | HCM-BEC-acute-care |
| ArchiMate semantic ID | id-8c9e5a5c2384558db1e86af56d2c942b |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Acute Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ACUTE-CARE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-ACUTE-CARE"] |
| Name aliases | ["Acute Care"] |

Parent: [Episode & Encounter Administration](business-enabling-capabilities.md#hcm-bec-episode-encounter-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:acute-care`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-encounter-lifecycle-management"></a>

## Encounter Lifecycle Management

Administer the established encounter lifecycle management information and coordination responsibility while preserving its source authority.

**Overview**

Administer the established encounter lifecycle management information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the established encounter lifecycle management information and coordination responsibility while preserving its source authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-encounter-lifecycle-management |
| canonicalId | 2ebf1bb20ab34755b41567c156cc5928 |
| displayName | [CT3] Encounter Lifecycle Management |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.EncounterLifecycleManagement |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-EncounterLifecycleManagement |
| Snapshot publication reference | HCM-BEC-reviewed-encounter-lifecycle-management |
| ArchiMate semantic ID | id-db254db117db5f77926aa5add9ddb6bc |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ENCOUNTER-LIFECYCLE-MANAGEMENT |

Parent: [Episode & Encounter Administration](business-enabling-capabilities.md#hcm-bec-episode-encounter-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-encounter-lifecycle-management`) and the element's version-qualified Properties.

<a id="hcm-bec-funding-coverage-administration"></a>

## Funding & Coverage Administration

Funding & Coverage Administration represents externally supplied insurance coverage and eligibility flags needed as care context. It allows those administrative conditions to be available without implying that Harmonia determines entitlements. The established Adjacent standing and absence of Features bound the current responsibility to this information contribution.

**Overview**

Funding & Coverage Administration represents externally supplied insurance coverage and eligibility flags needed as care context. It allows those administrative conditions to be available without implying that Harmonia determines entitlements. The established Adjacent standing and absence of Features bound the current responsibility to this information contribution.

**Intent**

Funding & Coverage Administration represents externally supplied insurance coverage and eligibility flags needed as care context. It allows those administrative conditions to be available without implying that Harmonia determines entitlements. The established Adjacent standing and absence of Features bound the current responsibility to this information contribution.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Coverage information is not claims adjudication, funding governance or a universal determination of care eligibility.

### Harmonia Architectural Relevance — predecessor explanation

Harmonia consumes the administrative context; it does not acquire the payer's authority or manage the underlying coverage arrangement.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:funding--coverage-administration |
| canonicalId | 640432d413cf47928ea2b3d221f92313 |
| displayName | [CT2] Funding & Coverage Administration |
| friendlyName | CareAdministrationEnablement.FundingAndCoverageAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-FundingAndCoverageAdministration |
| Snapshot publication reference | HCM-BEC-funding-coverage-administration |
| ArchiMate semantic ID | id-c2df74422094583c85e83601696f971f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Funding & Coverage Administration |
| Reviewed name | Funding & Coverage Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-FUNDING-COVERAGE-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FINANCIAL-INFORMATION-ENABLEMENT.CT3-BEC-FUNDING-COVERAGE-ADMINISTRATION"] |
| Release scope | Not Addressed in R1.x / R2.x |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:funding--coverage-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-medication-administration"></a>

## Medication Administration

In this enabling catalogue, Medication Administration integrates medication orders, dispensing evidence and point-of-care administration records. It preserves the distinct events through which intended therapy becomes supplied and, where reported, administered. This administrative integration supports a coherent medication account across systems without itself dispensing or administering a medicine.

**Overview**

In this enabling catalogue, Medication Administration integrates medication orders, dispensing evidence and point-of-care administration records. It preserves the distinct events through which intended therapy becomes supplied and, where reported, administered. This administrative integration supports a coherent medication account across systems without itself dispensing or administering a medicine.

**Intent**

In this enabling catalogue, Medication Administration integrates medication orders, dispensing evidence and point-of-care administration records. It preserves the distinct events through which intended therapy becomes supplied and, where reported, administered. This administrative integration supports a coherent medication account across systems without itself dispensing or administering a medicine.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Prescribed, dispensed and administered are different states. The capability name does not assign bedside medication delivery to Harmonia.

### Harmonia Architectural Relevance — predecessor explanation

Connecting medication events materially supports continuity and interpretation while clinical and pharmacy actors retain originating responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:medication-administration |
| canonicalId | c907e2a95629435b9951941ad4e2ebe9 |
| displayName | [CT2] Medication Administration |
| friendlyName | CareAdministrationEnablement.MedicationAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration |
| Snapshot publication reference | HCM-BEC-medication-administration |
| ArchiMate semantic ID | id-b977d4fbbfbc5943bf2b03b300408b3a |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Medication Administration |
| Reviewed name | Medication Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-MEDICATION-ADMINISTRATION"] |
| Release scope | Not Addressed in R1.x / R2.x |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Medication Order & Dispense Management](business-enabling-capabilities.md#hcm-bec-reviewed-medication-order-dispense-management) — Proposed; placement Proposed
- [Medication Therapy Provision and Monitoring](business-enabling-capabilities.md#hcm-bec-medication-therapy) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:medication-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-medication-order-dispense-management"></a>

## Medication Order & Dispense Management

Administer the established medication order & dispense management information and coordination responsibility while preserving its source authority.

**Overview**

Administer the established medication order & dispense management information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the established medication order & dispense management information and coordination responsibility while preserving its source authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-medication-order-dispense-management |
| canonicalId | 9874098228764b62b1f640ea749b742d |
| displayName | [CT3] Medication Order & Dispense Management |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationOrderAndDispenseManagement |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationOrderAndDispenseManagement |
| Snapshot publication reference | HCM-BEC-reviewed-medication-order-dispense-management |
| ArchiMate semantic ID | id-ea9da64cb51253f1aabacc59253b8db8 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-ORDER-DISPENSE-MANAGEMENT |

Parent: [Medication Administration](business-enabling-capabilities.md#hcm-bec-medication-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-medication-order-dispense-management`) and the element's version-qualified Properties.

<a id="hcm-bec-medication-therapy"></a>

## Medication Therapy Provision and Monitoring

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Medication Therapy connects reconciled histories and medication events across care transitions. The clinically established medication account is received and made available with order, dispensing and administration information still distinguishable. This supports continuity of therapy understanding without turning the platform into the clinician performing medication reconciliation or choosing treatment.

### Boundary — predecessor explanation

Ingesting a reconciled history is not clinical reconciliation, and an order or dispense event does not prove administration.

### Harmonia Architectural Relevance — predecessor explanation

Cross-boundary medication information materially supports care continuity and interpretation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:medication-therapy |
| canonicalId | d1821b4f6d5746f9a697dfe3b792a186 |
| displayName | [CT3] Medication Therapy Provision and Monitoring |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationTherapyProvisionAndMonitoring |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationTherapyProvisionAndMonitoring |
| Snapshot publication reference | HCM-BEC-medication-therapy |
| ArchiMate semantic ID | id-f6b618c033b55400a9e26a6c1c5d9c16 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Medication Therapy |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-THERAPY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-MEDICATION-THERAPY"] |
| Name aliases | ["Medication Therapy"] |

Parent: [Medication Administration](business-enabling-capabilities.md#hcm-bec-medication-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:medication-therapy`) and the element's version-qualified Properties.

<a id="hcm-bec-order-administration"></a>

## Order Administration

Order Administration manages the closed loop around a request for defined fulfilment. It receives the request, resolves and routes to a destination, distinguishes delivery from business disposition, tracks progress and associates the eventual outcome. This makes the difference between an order sent and an order fulfilled visible throughout the requester's interaction.

**Overview**

Order Administration manages the closed loop around a request for defined fulfilment. It receives the request, resolves and routes to a destination, distinguishes delivery from business disposition, tracks progress and associates the eventual outcome. This makes the difference between an order sent and an order fulfilled visible throughout the requester's interaction.

**Intent**

Order Administration manages the closed loop around a request for defined fulfilment. It receives the request, resolves and routes to a destination, distinguishes delivery from business disposition, tracks progress and associates the eventual outcome. This makes the difference between an order sent and an order fulfilled visible throughout the requester's interaction.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Intent — predecessor explanation

Preserve an intelligible connection between the original request, its destination, progression and returned outcome.

### Boundary — predecessor explanation

Technical acknowledgements have only their contractual meaning. Receipt does not universally establish durable acceptance, business acceptance or completion; outcome association has no invented Feature equivalence.

### Harmonia Architectural Relevance — predecessor explanation

Reliable routing and closed-loop state are direct platform responsibilities. The performing service retains authority over clinical execution and its result.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:order-administration |
| canonicalId | c3c5abd7987f4433a46cbfed8167914c |
| displayName | [CT2] Order Administration |
| friendlyName | CareAdministrationEnablement.OrderAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration |
| Snapshot publication reference | HCM-BEC-order-administration |
| ArchiMate semantic ID | id-35b56e00f8885f5f97147eca279b8ef6 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Order Administration |
| Reviewed name | Order Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION"] |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Closed-Loop Order Progression](business-enabling-capabilities.md#hcm-bec-reviewed-closed-loop-order-progression) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:order-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-closed-loop-order-progression"></a>

## Closed-Loop Order Progression

Administer the traceable progression of an order from request through destination, delivery, acknowledgement, fulfilment and outcome association.

**Overview**

Administer the traceable progression of an order from request through destination, delivery, acknowledgement, fulfilment and outcome association. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the traceable progression of an order from request through destination, delivery, acknowledgement, fulfilment and outcome association.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-closed-loop-order-progression |
| canonicalId | 2f79ebd7392c46ef92def6f25bc9c7ce |
| displayName | [CT3] Closed-Loop Order Progression |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression |
| Snapshot publication reference | HCM-BEC-reviewed-closed-loop-order-progression |
| ArchiMate semantic ID | id-5f410811dfd950f0a195ba1f600b15f0 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Closed-Loop Order Progression |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.CT3-BEC-CLOSED-LOOP-ORDER-PROGRESSION |

Parent: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-closed-loop-order-progression`) and the element's version-qualified Properties.

<a id="hcm-bec-procedure-administration"></a>

## Procedure Administration

Procedure Administration integrates procedural bookings and the documentation of procedures performed. It keeps the scheduled case distinct from operative, anaesthetic and post-procedure information received later. That separation supports continuity across scheduling and clinical documentation without inferring that a booking establishes performance.

**Overview**

Procedure Administration integrates procedural bookings and the documentation of procedures performed. It keeps the scheduled case distinct from operative, anaesthetic and post-procedure information received later. That separation supports continuity across scheduling and clinical documentation without inferring that a booking establishes performance.

**Intent**

Procedure Administration integrates procedural bookings and the documentation of procedures performed. It keeps the scheduled case distinct from operative, anaesthetic and post-procedure information received later. That separation supports continuity across scheduling and clinical documentation without inferring that a booking establishes performance.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Administrative integration neither performs the procedure nor originates its professional findings.

### Harmonia Architectural Relevance — predecessor explanation

Correlated bookings and procedural documents support cross-system care context while the surgical or procedural service retains clinical authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:procedure-administration |
| canonicalId | 649dd7cc26094e539386a050086dfb91 |
| displayName | [CT2] Procedure Administration |
| friendlyName | CareAdministrationEnablement.ProcedureAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ProcedureAdministration |
| Snapshot publication reference | HCM-BEC-procedure-administration |
| ArchiMate semantic ID | id-4aa8850ccaab516b9d3854b9e82971eb |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Procedure Administration |
| Reviewed name | Procedure Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-PROCEDURE-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-PROCEDURE-ADMINISTRATION"] |
| Release scope | Not Addressed in R1.x / R2.x |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Procedure & Surgical Administration](business-enabling-capabilities.md#hcm-bec-reviewed-procedure-surgical-administration) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:procedure-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-procedure-surgical-administration"></a>

## Procedure & Surgical Administration

Administer the established procedure & surgical administration information and coordination responsibility while preserving its source authority.

**Overview**

Administer the established procedure & surgical administration information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the established procedure & surgical administration information and coordination responsibility while preserving its source authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-procedure-surgical-administration |
| canonicalId | 3c4a53dd8c8f468f83ca5d92397dd7e8 |
| displayName | [CT3] Procedure & Surgical Administration |
| friendlyName | CareAdministrationEnablement.ProcedureAdministration.ProcedureAndSurgicalAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ProcedureAdministration.CT3-BEC-ProcedureAndSurgicalAdministration |
| Snapshot publication reference | HCM-BEC-reviewed-procedure-surgical-administration |
| ArchiMate semantic ID | id-afd1a07848325b678cdee592358efbb4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-PROCEDURE-ADMINISTRATION.CT3-BEC-PROCEDURE-SURGICAL-ADMINISTRATION |

Parent: [Procedure Administration](business-enabling-capabilities.md#hcm-bec-procedure-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-procedure-surgical-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-referral-administration"></a>

## Referral Administration

Referral Administration receives referral material, associates the supporting clinical context and tracks the recipient's explicit disposition and outcome. Its purpose is to keep the request and its progression intelligible across care boundaries. It distinguishes a request for another participant's involvement from an order for bounded fulfilment and from actual acceptance of care responsibility.

**Overview**

Referral Administration receives referral material, associates the supporting clinical context and tracks the recipient's explicit disposition and outcome. Its purpose is to keep the request and its progression intelligible across care boundaries. It distinguishes a request for another participant's involvement from an order for bounded fulfilment and from actual acceptance of care responsibility.

**Intent**

Referral Administration receives referral material, associates the supporting clinical context and tracks the recipient's explicit disposition and outcome. Its purpose is to keep the request and its progression intelligible across care boundaries. It distinguishes a request for another participant's involvement from an order for bounded fulfilment and from actual acceptance of care responsibility.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Transport acceptance does not establish clinical acceptance. A referral need not universally produce an appointment, encounter, order or transfer of care.

### Harmonia Architectural Relevance — predecessor explanation

Harmonia materially connects the referral, supporting information and reported disposition while the receiving service retains clinical and responsibility decisions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:referral-administration |
| canonicalId | ae0de25600ee416aa24d2a167b64f230 |
| displayName | [CT2] Referral Administration |
| friendlyName | CareAdministrationEnablement.ReferralAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration |
| Snapshot publication reference | HCM-BEC-referral-administration |
| ArchiMate semantic ID | id-5beae734749a57468bf09956473d2f40 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Referral Administration |
| Reviewed name | Referral Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION"] |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Referral Intake & Progression](business-enabling-capabilities.md#hcm-bec-reviewed-referral-intake-progression) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:referral-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-referral-intake-progression"></a>

## Referral Intake & Progression

Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems.

**Overview**

Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-referral-intake-progression |
| canonicalId | d11bdb9464b2476894aa372ab9b929a7 |
| displayName | [CT3] Referral Intake & Progression |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ReferralIntakeAndProgression |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.CT3-BEC-ReferralIntakeAndProgression |
| Snapshot publication reference | HCM-BEC-reviewed-referral-intake-progression |
| ArchiMate semantic ID | id-f7fc7616aa89520a84e2235872dc93ed |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Referral Intake & Progression |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.CT3-BEC-REFERRAL-INTAKE-PROGRESSION |

Parent: [Referral Administration](business-enabling-capabilities.md#hcm-bec-referral-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-referral-intake-progression`) and the element's version-qualified Properties.

<a id="hcm-bec-scheduling-administration"></a>

## Scheduling Administration

Scheduling Administration integrates appointment information established by departmental scheduling systems. It receives scheduled events and propagates cancellation, rescheduling and attendance changes so that care-coordination contexts do not diverge. Its concern is the coherence of scheduling information across participants rather than originating every booking decision.

**Overview**

Scheduling Administration integrates appointment information established by departmental scheduling systems. It receives scheduled events and propagates cancellation, rescheduling and attendance changes so that care-coordination contexts do not diverge. Its concern is the coherence of scheduling information across participants rather than originating every booking decision.

**Intent**

Scheduling Administration integrates appointment information established by departmental scheduling systems. It receives scheduled events and propagates cancellation, rescheduling and attendance changes so that care-coordination contexts do not diverge. Its concern is the coherence of scheduling information across participants rather than originating every booking decision.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

A synchronised booking does not prove attendance, service capacity or delivered clinical care.

### Harmonia Architectural Relevance — predecessor explanation

Timely appointment changes materially support coordination across systems without giving Harmonia the scheduler's clinical or departmental authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:scheduling-administration |
| canonicalId | 35fb3744c900470f9895056246a13fb1 |
| displayName | [CT2] Scheduling Administration |
| friendlyName | CareAdministrationEnablement.SchedulingAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-SchedulingAdministration |
| Snapshot publication reference | HCM-BEC-scheduling-administration |
| ArchiMate semantic ID | id-fa4797b3d84052ecbea381b52dfa8b3f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Scheduling Administration |
| Reviewed name | Scheduling Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-SCHEDULING-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-SCHEDULING-ADMINISTRATION"] |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Appointment & Booking Administration](business-enabling-capabilities.md#hcm-bec-reviewed-appointment-booking-administration) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:scheduling-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-appointment-booking-administration"></a>

## Appointment & Booking Administration

Administer externally supplied appointment and booking information and communicate changes in their scheduling status.

**Overview**

Administer externally supplied appointment and booking information and communicate changes in their scheduling status. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer externally supplied appointment and booking information and communicate changes in their scheduling status.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-appointment-booking-administration |
| canonicalId | 7b3ba9bcac564dd4a7a08631581db2ca |
| displayName | [CT3] Appointment & Booking Administration |
| friendlyName | CareAdministrationEnablement.SchedulingAdministration.AppointmentAndBookingAdministration |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-SchedulingAdministration.CT3-BEC-AppointmentAndBookingAdministration |
| Snapshot publication reference | HCM-BEC-reviewed-appointment-booking-administration |
| ArchiMate semantic ID | id-75679799ab975e8088f69be6f0f414f0 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Appointment & Booking Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-SCHEDULING-ADMINISTRATION.CT3-BEC-APPOINTMENT-BOOKING-ADMINISTRATION |

Parent: [Scheduling Administration](business-enabling-capabilities.md#hcm-bec-scheduling-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-appointment-booking-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-clinical-service-dependency-management"></a>

## Clinical Service Dependency Management

Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.

**Overview**

Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-clinical-service-dependency-management |
| canonicalId | 232db9b8b47c450cb0aa9acaf539c09d |
| displayName | [CT1] Clinical Service Dependency Management |
| friendlyName | ClinicalServiceDependencyManagement |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement |
| Snapshot publication reference | HCM-BEC-reviewed-clinical-service-dependency-management |
| ArchiMate semantic ID | id-7afbcec0122951079c2bb647251be323 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Clinical Service Dependency Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT |
| Release scope | Not Addressed in R1.x / R2.x |

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Acute Care](business-enabling-capabilities.md#hcm-bec-reviewed-acute-care) — Proposed; placement Proposed
- [Allied Health](business-enabling-capabilities.md#hcm-bec-allied-health) — Canonical; placement Proposed
- [Ambulatory Care](business-enabling-capabilities.md#hcm-bec-ambulatory-care) — Canonical; placement Proposed
- [Clinical Assessment](business-enabling-capabilities.md#hcm-bec-clinical-assessment) — Canonical; placement Proposed
- [Community Care](business-enabling-capabilities.md#hcm-bec-reviewed-community-care) — Proposed; placement Proposed
- [Diagnostic Services](business-enabling-capabilities.md#hcm-bec-reviewed-diagnostic-services) — Proposed; placement Proposed
- [Emergency Care](business-enabling-capabilities.md#hcm-bec-reviewed-emergency-care) — Proposed; placement Proposed
- [Inpatient Care](business-enabling-capabilities.md#hcm-bec-reviewed-inpatient-care) — Proposed; placement Proposed
- [Medication Therapy](business-enabling-capabilities.md#hcm-bec-reviewed-medication-therapy) — Proposed; placement Proposed
- [Outreach Care](business-enabling-capabilities.md#hcm-bec-reviewed-outreach-care) — Proposed; placement Proposed
- [Preventive Care](business-enabling-capabilities.md#hcm-bec-reviewed-preventive-care) — Proposed; placement Proposed
- [Primary Care](business-enabling-capabilities.md#hcm-bec-reviewed-primary-care) — Proposed; placement Proposed
- [Rehabilitation](business-enabling-capabilities.md#hcm-bec-rehabilitation) — Canonical; placement Proposed
- [Remote & Virtual Care](business-enabling-capabilities.md#hcm-bec-reviewed-remote-virtual-care) — Proposed; placement Proposed
- [Surgical & Procedural Care](business-enabling-capabilities.md#hcm-bec-surgical-procedural-care) — Canonical; placement Proposed
- [Therapeutic Services](business-enabling-capabilities.md#hcm-bec-therapeutic-services) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-clinical-service-dependency-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-acute-care"></a>

## Acute Care

Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-acute-care |
| canonicalId | 9bb988c526404522b83aeed5069e0eb5 |
| displayName | [CT2] Acute Care |
| friendlyName | ClinicalServiceDependencyManagement.AcuteCare |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-AcuteCare |
| Snapshot publication reference | HCM-BEC-reviewed-acute-care |
| ArchiMate semantic ID | id-36aa4edb4b1557c7bb8fde88fa158486 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Acute Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-ACUTE-CARE |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-acute-care`) and the element's version-qualified Properties.

<a id="hcm-bec-allied-health"></a>

## Allied Health

Identify and track the operational and infrastructure dependencies necessary for allied health services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for allied health services to function, retaining each dependency provider's responsibility.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for allied health services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Allied Health is preserved as an Adjacent Service Delivery context without an established atomic Feature set. It acknowledges the professional care context in which information can be needed or produced, while leaving the specific therapeutic and assessment responsibilities with their clinical participants. The model does not create a generic allied-health workflow from the contextual name.

### Boundary — predecessor explanation

Care coordination and record access do not transfer professional judgement or therapy delivery to Harmonia.

### Harmonia Architectural Relevance — predecessor explanation

The formal Adjacent position is retained rather than inherited from neighbouring Relevant delivery contexts.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:allied-health |
| canonicalId | 63cd714a3e2a4354aa5383b189fea566 |
| displayName | [CT2] Allied Health |
| friendlyName | ClinicalServiceDependencyManagement.AlliedHealth |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-AlliedHealth |
| Snapshot publication reference | HCM-BEC-allied-health |
| ArchiMate semantic ID | id-38ea53c83c9a55cf920b5ce358b95c74 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Allied Health |
| Reviewed name | Allied Health |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-ALLIED-HEALTH |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-ALLIED-HEALTH"] |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:allied-health`) and the element's version-qualified Properties.

<a id="hcm-bec-ambulatory-care"></a>

## Ambulatory Care

Identify and track the operational and infrastructure dependencies necessary for ambulatory care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for ambulatory care services to function, retaining each dependency provider's responsibility.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for ambulatory care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Ambulatory Care is an established Adjacent delivery context retained for understanding information used around care without inpatient admission. The RAD establishes no atomic enabling Feature decomposition for it. The entry therefore locates the context without claiming a separate scheduling, treatment or clinical progression mandate for Harmonia.

### Boundary — predecessor explanation

The contextual entry does not allocate BC tiers or require a distinct platform function for every ambulatory activity.

### Harmonia Architectural Relevance — predecessor explanation

Adjacent standing preserves contextual participation while clinical service delivery remains external.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:ambulatory-care |
| canonicalId | f74d7c3a39c540c4b462576e8f09fc58 |
| displayName | [CT2] Ambulatory Care |
| friendlyName | ClinicalServiceDependencyManagement.AmbulatoryCare |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-AmbulatoryCare |
| Snapshot publication reference | HCM-BEC-ambulatory-care |
| ArchiMate semantic ID | id-b2fc16d589fe5693abaf68993d5e5701 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Ambulatory Care |
| Reviewed name | Ambulatory Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-AMBULATORY-CARE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-AMBULATORY-CARE"] |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:ambulatory-care`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-assessment"></a>

## Clinical Assessment

Identify and track the operational and infrastructure dependencies necessary for clinical assessment services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for clinical assessment services to function, retaining each dependency provider's responsibility.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for clinical assessment services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Clinical Assessment is retained as an Adjacent Service Delivery context. It identifies the setting of assessment-related information needs without establishing a specialised Harmonia Feature set or replacing the Business Capability Individual Health Assessment. The distinction allows the catalogue to recognise clinical activity while keeping actual examination, interpretation and professional judgement outside the platform's assigned practice.

### Boundary — predecessor explanation

No atomic Features are established. The contextual name does not make Harmonia a clinical assessor or establish BC ancestry.

### Harmonia Architectural Relevance — predecessor explanation

The Adjacent classification denotes contextual information participation rather than material execution of clinical assessment.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-assessment |
| canonicalId | a7d84c95e48c453797419813ca80a66c |
| displayName | [CT2] Clinical Assessment |
| friendlyName | ClinicalServiceDependencyManagement.ClinicalAssessment |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-ClinicalAssessment |
| Snapshot publication reference | HCM-BEC-clinical-assessment |
| ArchiMate semantic ID | id-95b79770d57e537bbfb65b05c9c21829 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Assessment |
| Reviewed name | Clinical Assessment |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-CLINICAL-ASSESSMENT |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-CLINICAL-ASSESSMENT"] |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-assessment`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-community-care"></a>

## Community Care

Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-community-care |
| canonicalId | 942aa78cad004ef99de6b3aae8ddd014 |
| displayName | [CT2] Community Care |
| friendlyName | ClinicalServiceDependencyManagement.CommunityCare |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-CommunityCare |
| Snapshot publication reference | HCM-BEC-reviewed-community-care |
| ArchiMate semantic ID | id-a12191bde58c547b954e263c5e49ba3e |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Community Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-COMMUNITY-CARE |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-community-care`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-diagnostic-services"></a>

## Diagnostic Services

Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-diagnostic-services |
| canonicalId | b825ebab42f444d8b738b4c1f489f785 |
| displayName | [CT2] Diagnostic Services |
| friendlyName | ClinicalServiceDependencyManagement.DiagnosticServices |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-DiagnosticServices |
| Snapshot publication reference | HCM-BEC-reviewed-diagnostic-services |
| ArchiMate semantic ID | id-52c91baf111d50f7bd4b24fcc80ab1be |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Diagnostic Services |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-DIAGNOSTIC-SERVICES |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-diagnostic-services`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-emergency-care"></a>

## Emergency Care

Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-emergency-care |
| canonicalId | aa088f0647ad4b78966fe28fa80af674 |
| displayName | [CT2] Emergency Care |
| friendlyName | ClinicalServiceDependencyManagement.EmergencyCare |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-EmergencyCare |
| Snapshot publication reference | HCM-BEC-reviewed-emergency-care |
| ArchiMate semantic ID | id-0bdbebb9b36f5b2b8d5816dbc27aeeb3 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Emergency Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-EMERGENCY-CARE |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-emergency-care`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-inpatient-care"></a>

## Inpatient Care

Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-inpatient-care |
| canonicalId | b39fb49c01b545a5b51d3313531efb1b |
| displayName | [CT2] Inpatient Care |
| friendlyName | ClinicalServiceDependencyManagement.InpatientCare |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-InpatientCare |
| Snapshot publication reference | HCM-BEC-reviewed-inpatient-care |
| ArchiMate semantic ID | id-052abff4e30c5d5eaa4071331c4af500 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Inpatient Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-INPATIENT-CARE |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-inpatient-care`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-medication-therapy"></a>

## Medication Therapy

Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-medication-therapy |
| canonicalId | bb04996164c94e0c827a020432f4e498 |
| displayName | [CT2] Medication Therapy |
| friendlyName | ClinicalServiceDependencyManagement.MedicationTherapy |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-MedicationTherapy |
| Snapshot publication reference | HCM-BEC-reviewed-medication-therapy |
| ArchiMate semantic ID | id-6c80b51346315f2fa3d9111d871deba1 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Medication Therapy |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-MEDICATION-THERAPY |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-medication-therapy`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-outreach-care"></a>

## Outreach Care

Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-outreach-care |
| canonicalId | b9ddbe2dd6a7485ba9cd3482215ea09e |
| displayName | [CT2] Outreach Care |
| friendlyName | ClinicalServiceDependencyManagement.OutreachCare |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-OutreachCare |
| Snapshot publication reference | HCM-BEC-reviewed-outreach-care |
| ArchiMate semantic ID | id-00c20496abf655fa974ae887a695102a |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Outreach Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-OUTREACH-CARE |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-outreach-care`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-preventive-care"></a>

## Preventive Care

Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-preventive-care |
| canonicalId | dfdc4f0273ba4910848b3b65c9ef6ab2 |
| displayName | [CT2] Preventive Care |
| friendlyName | ClinicalServiceDependencyManagement.PreventiveCare |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-PreventiveCare |
| Snapshot publication reference | HCM-BEC-reviewed-preventive-care |
| ArchiMate semantic ID | id-2bc56e5d43a356dda381e7a34377af89 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Preventive Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-PREVENTIVE-CARE |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-preventive-care`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-primary-care"></a>

## Primary Care

Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-primary-care |
| canonicalId | d118673d77d74c8cb8cec24f03adddd8 |
| displayName | [CT2] Primary Care |
| friendlyName | ClinicalServiceDependencyManagement.PrimaryCare |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-PrimaryCare |
| Snapshot publication reference | HCM-BEC-reviewed-primary-care |
| ArchiMate semantic ID | id-351cfc95a8c85684a3ff908f6df282eb |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Primary Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-PRIMARY-CARE |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-primary-care`) and the element's version-qualified Properties.

<a id="hcm-bec-rehabilitation"></a>

## Rehabilitation

Identify and track the operational and infrastructure dependencies necessary for rehabilitation services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for rehabilitation services to function, retaining each dependency provider's responsibility.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for rehabilitation services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Rehabilitation is an Adjacent clinical delivery context whose information can be represented without assigning the rehabilitation programme to Harmonia. No atomic Features are established in the current catalogue. Its presence records a healthcare context rather than requiring a new specialised coordination engine or a clinical outcome authority.

### Boundary — predecessor explanation

Information integration does not perform therapy, determine rehabilitation goals or judge clinical effectiveness.

### Harmonia Architectural Relevance — predecessor explanation

Contextual participation is retained within the formal Adjacent classification.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:rehabilitation |
| canonicalId | 99ed51abcc5e4e8ba3b3d0161b17544a |
| displayName | [CT2] Rehabilitation |
| friendlyName | ClinicalServiceDependencyManagement.Rehabilitation |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-Rehabilitation |
| Snapshot publication reference | HCM-BEC-rehabilitation |
| ArchiMate semantic ID | id-c9ed027109de56548cba237529d5f6d8 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Rehabilitation |
| Reviewed name | Rehabilitation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-REHABILITATION |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-REHABILITATION"] |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:rehabilitation`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-remote-virtual-care"></a>

## Remote & Virtual Care

Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-remote-virtual-care |
| canonicalId | 30dff22d40b64da29ede7c41673db085 |
| displayName | [CT2] Remote & Virtual Care |
| friendlyName | ClinicalServiceDependencyManagement.RemoteAndVirtualCare |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-RemoteAndVirtualCare |
| Snapshot publication reference | HCM-BEC-reviewed-remote-virtual-care |
| ArchiMate semantic ID | id-3bc2433424425db093822bd2db4dd8c7 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Remote & Virtual Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-REMOTE-VIRTUAL-CARE |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-remote-virtual-care`) and the element's version-qualified Properties.

<a id="hcm-bec-surgical-procedural-care"></a>

## Surgical & Procedural Care

Identify and track the operational and infrastructure dependencies necessary for surgical & procedural care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for surgical & procedural care services to function, retaining each dependency provider's responsibility.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for surgical & procedural care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Surgical & Procedural Care retains the clinical delivery context around procedures as Adjacent. It is distinct from the established integration of bookings and documents in Procedure Administration and from theatre operational coordination. Recognising those neighbouring responsibilities prevents information about a procedure from being read as an assignment to perform or clinically manage it.

### Boundary — predecessor explanation

No atomic Features are allocated to this context, and Harmonia does not acquire surgical decision or procedural authority.

### Harmonia Architectural Relevance — predecessor explanation

The context is represented without assigning direct clinical delivery responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:surgical--procedural-care |
| canonicalId | c9a26d1aa1014545b9ae759b663c807b |
| displayName | [CT2] Surgical & Procedural Care |
| friendlyName | ClinicalServiceDependencyManagement.SurgicalAndProceduralCare |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-SurgicalAndProceduralCare |
| Snapshot publication reference | HCM-BEC-surgical-procedural-care |
| ArchiMate semantic ID | id-1c66c2d5d4925e4cb8872aa25ea0071b |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Surgical & Procedural Care |
| Reviewed name | Surgical & Procedural Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-SURGICAL-PROCEDURAL-CARE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-SURGICAL-PROCEDURAL-CARE"] |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:surgical--procedural-care`) and the element's version-qualified Properties.

<a id="hcm-bec-therapeutic-services"></a>

## Therapeutic Services

Identify and track the operational and infrastructure dependencies necessary for therapeutic services services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for therapeutic services services to function, retaining each dependency provider's responsibility.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for therapeutic services services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Therapeutic Services is retained as an Adjacent clinical delivery context, recognising care activity whose information may concern Harmonia without assigning treatment performance to it. The current model establishes no atomic Features for this context. The broad label is not used to invent therapy-specific Functions or reclassify clinical interventions as platform execution.

### Boundary — predecessor explanation

Information participation does not confer authority to select, perform or assess the clinical adequacy of therapy.

### Harmonia Architectural Relevance — predecessor explanation

Adjacent standing preserves the contextual role while treatment remains the responsible service's activity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:therapeutic-services |
| canonicalId | b512b6d326004cb7b91a627fe6dbd8be |
| displayName | [CT2] Therapeutic Services |
| friendlyName | ClinicalServiceDependencyManagement.TherapeuticServices |
| canonicalName | CT1-BEC-ClinicalServiceDependencyManagement.CT2-BEC-TherapeuticServices |
| Snapshot publication reference | HCM-BEC-therapeutic-services |
| ArchiMate semantic ID | id-12eca4e940755bed847e500b41770a5c |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Therapeutic Services |
| Reviewed name | Therapeutic Services |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-THERAPEUTIC-SERVICES |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-THERAPEUTIC-SERVICES"] |

Parent: [Clinical Service Dependency Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-dependency-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:therapeutic-services`) and the element's version-qualified Properties.

<a id="hcm-bec-healthcare-entity-information-enablement"></a>

## Entity Information Enablement

Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices.

**Overview**

Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices.

**Intent**

Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices.

**Boundary**

Registry information does not establish clinical privilege, service readiness, occupancy or originating national identity authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-healthcare-entity-information-enablement |
| canonicalId | b5a92874f8ac4509a30af5253590edc1 |
| displayName | [CT1] Entity Information Enablement |
| friendlyName | EntityInformationEnablement |
| canonicalName | CT1-BEC-EntityInformationEnablement |
| Snapshot publication reference | HCM-BEC-healthcare-entity-information-enablement |
| ArchiMate semantic ID | id-4726f9b8f6015e019238b5a8cc596c7c |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Entity Information Enablement |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT |
| Name aliases | ["Healthcare Entity Information Enablement"] |

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Client Administration](business-enabling-capabilities.md#hcm-bec-client-administration) — Canonical; placement Proposed
- [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration) — Canonical; placement Proposed
- [Health Materiel Administration](business-enabling-capabilities.md#hcm-bec-health-product-administration) — Canonical; placement Proposed
- [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration) — Canonical; placement Proposed
- [Location Administration](business-enabling-capabilities.md#hcm-bec-location-administration) — Canonical; placement Proposed
- [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration) — Canonical; placement Proposed
- [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-healthcare-entity-information-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-client-administration"></a>

## Client Administration

Client Administration provides the representation and demographic context needed to recognise individuals receiving care. It brings identity, healthcare-subject context, relationships and privacy into the same administrative frame while preserving their different responsibilities. The established partial chain to Person Identity is retained; the catalogue's other contextual descriptions do not supply a complete capability hierarchy.

**Overview**

Client Administration provides the representation and demographic context needed to recognise individuals receiving care. It brings identity, healthcare-subject context, relationships and privacy into the same administrative frame while preserving their different responsibilities. The established partial chain to Person Identity is retained; the catalogue's other contextual descriptions do not supply a complete capability hierarchy.

**Intent**

Client Administration provides the representation and demographic context needed to recognise individuals receiving care. It brings identity, healthcare-subject context, relationships and privacy into the same administrative frame while preserving their different responsibilities. The established partial chain to Person Identity is retained; the catalogue's other contextual descriptions do not supply a complete capability hierarchy.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Intent — predecessor explanation

Keep the individual and the circumstances of their representation intelligible across care information and operational activity.

### Boundary — predecessor explanation

R1/R2 identifier correlation and administrative correction do not make Harmonia the authoritative clinical EMPI. Probabilistic matching and master identity merge/unmerge remain an uncommitted roadmap candidate.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:client-administration |
| canonicalId | f0885472cced4bccbcce1568bf0360dc |
| displayName | [CT2] Client Administration |
| friendlyName | EntityInformationEnablement.ClientAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration |
| Snapshot publication reference | HCM-BEC-client-administration |
| ArchiMate semantic ID | id-c67b67dba5cc512d94eac01eb1524775 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Client Administration |
| Reviewed name | Client Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION |
| Unresolved dimensions | ["relevance"] |

Parent: [Entity Information Enablement](business-enabling-capabilities.md#hcm-bec-healthcare-entity-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Client Privacy Guardrail Management](business-enabling-capabilities.md#hcm-bec-client-privacy) — Canonical; placement Proposed
- [Client Relationship Administration](business-enabling-capabilities.md#hcm-bec-client-relationship) — Canonical; placement Proposed
- [Healthcare Subject Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-subject) — Canonical; placement Proposed
- [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:client-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-client-privacy"></a>

## Client Privacy Guardrail Management

Enforcing client-level privacy preferences, consent directives, and confidentiality markers.

Definition — reviewed candidate

Manage client-associated privacy constraints, preferences, permissions, restrictions and consent directives, and evaluate their applicability to information use.

**Overview**

Manage client-associated privacy constraints, preferences, permissions, restrictions and consent directives, and evaluate their applicability to information use.

**Intent**

Manage client-associated privacy constraints, preferences, permissions, restrictions and consent directives, and evaluate their applicability to information use.

**Boundary**

Owns client-associated constraints and consent directives; general enterprise privacy policy remains outside this capability.

---

## History

### Capability Overview — predecessor explanation

Client Privacy applies client-level consent directives, confidentiality markers and privacy preferences to governed information use. It makes the person's restrictions operationally relevant at disclosure, query and presentation boundaries instead of leaving them as passive record annotations. Its responsibility complements the broader policy boundary while retaining the distinction between the directive and an action evaluated against it.

### Boundary — predecessor explanation

A privacy flag does not become a caller-controlled grant of authority. Consent evaluation and information access must respect established policy ownership.

### Harmonia Architectural Relevance — predecessor explanation

Consistent application of client restrictions across information pathways is a direct platform responsibility, supporting Core classification.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:client-privacy |
| canonicalId | 7577a6e1815243648dd5356c21cca151 |
| displayName | [CT3] Client Privacy Guardrail Management |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientPrivacyGuardrailManagement |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientPrivacyGuardrailManagement |
| Snapshot publication reference | HCM-BEC-client-privacy |
| ArchiMate semantic ID | id-12123907605b5a30a078af04daf3c730 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Client Privacy |
| Reviewed name | Client Privacy Guardrail Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-PRIVACY |
| Name aliases | ["Client Privacy"] |

Parent: [Client Administration](business-enabling-capabilities.md#hcm-bec-client-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:client-privacy`) and the element's version-qualified Properties.

<a id="hcm-bec-client-relationship"></a>

## Client Relationship Administration

Representing relationships between the client and carers, guardians, or contacts.

Definition — reviewed candidate

Administer qualified client relationships with carers, guardians, contacts and other nominated care participants.

**Overview**

Administer qualified client relationships with carers, guardians, contacts and other nominated care participants.

**Intent**

Administer qualified client relationships with carers, guardians, contacts and other nominated care participants.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Client Relationship represents nominated carers, guardians, contacts and care agents together with the effective period and authority scope of those relationships. It supports correct interpretation of who is related to a client and in what capacity, rather than treating a contact name as unrestricted authority. The relationship context remains distinct from the client's own identity and privacy directives.

### Boundary — predecessor explanation

Representation of a relationship does not by itself authorise every disclosure or establish a new legal mandate.

### Harmonia Architectural Relevance — predecessor explanation

Temporal relationship information materially supports appropriate care communication and context without assigning guardianship decisions to Harmonia.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:client-relationship |
| canonicalId | d682c11d3eb04770b1f20be478fd475e |
| displayName | [CT3] Client Relationship Administration |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientRelationshipAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientRelationshipAdministration |
| Snapshot publication reference | HCM-BEC-client-relationship |
| ArchiMate semantic ID | id-db94873a0aac5928aed7bf23ed0bcbea |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Client Relationship |
| Reviewed name | Client Relationship Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-RELATIONSHIP |
| Name aliases | ["Client Relationship"] |

Parent: [Client Administration](business-enabling-capabilities.md#hcm-bec-client-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:client-relationship`) and the element's version-qualified Properties.

<a id="hcm-bec-healthcare-subject"></a>

## Healthcare Subject Profile Administration

Managing the core healthcare subject context bound to clinical records and operational activities.

Definition — reviewed candidate

Administer demographic and subject-of-care profile information bound to clinical records and operational activities.

**Overview**

Administer demographic and subject-of-care profile information bound to clinical records and operational activities.

**Intent**

Administer demographic and subject-of-care profile information bound to clinical records and operational activities.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Healthcare Subject maintains the subject-of-care context attached to clinical records and operational activity. It connects a resolved person identity with the encounters, orders and observations concerning that person, while preserving the provenance and temporal standing of demographic attributes. Its responsibility differs from resolving identifiers: it establishes how the person is represented as the continuing subject of care.

### Boundary — predecessor explanation

Subject association does not originate clinical findings or confer authority to change the source person's identity.

### Harmonia Architectural Relevance — predecessor explanation

Verifiable subject binding prevents otherwise valid information from being interpreted in the wrong person's care context, making it a substantive information-management responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:healthcare-subject |
| canonicalId | df214b5499694b768eb4a7a175a9d471 |
| displayName | [CT3] Healthcare Subject Profile Administration |
| friendlyName | EntityInformationEnablement.ClientAdministration.HealthcareSubjectProfileAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-HealthcareSubjectProfileAdministration |
| Snapshot publication reference | HCM-BEC-healthcare-subject |
| ArchiMate semantic ID | id-e4ada895b3705f13829792ac8ab2e274 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Healthcare Subject |
| Reviewed name | Healthcare Subject Profile Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-HEALTHCARE-SUBJECT |
| Name aliases | ["Healthcare Subject"] |

Parent: [Client Administration](business-enabling-capabilities.md#hcm-bec-client-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:healthcare-subject`) and the element's version-qualified Properties.

<a id="hcm-bec-person-identity"></a>

## Person Identity Administration

Resolving, maintaining, and correlating identities and identifiers assigned to an individual across different authorities.

Definition — reviewed candidate

Resolve, maintain and correlate identities and identifiers assigned to a person across issuing authorities, retaining source-qualified correction history.

**Overview**

Resolve, maintain and correlate identities and identifiers assigned to a person across issuing authorities, retaining source-qualified correction history.

**Intent**

Resolve, maintain and correlate identities and identifiers assigned to a person across issuing authorities, retaining source-qualified correction history.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Person Identity resolves and correlates the identities and identifiers assigned to one individual by different authorities. It enables an incoming local identifier to be understood in its issuing namespace and related to other known identifiers without destructively replacing their history. Resolution, cross-authority correlation, alias association and governed correction preserve different aspects of that responsibility.

### Intent — predecessor explanation

Support reliable association of care information with the intended individual across participating systems.

### Boundary — predecessor explanation

Harmonia relies on authoritative upstream PAS/MPI information for R1/R2. Correlation and correction notifications do not establish an autonomous master-patient reconciliation engine.

### Harmonia Architectural Relevance — predecessor explanation

Identity association is a direct platform concern because clinical records, orders and operational activity need an enduring subject context. Its Core standing does not imply originating master identity authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:person-identity |
| canonicalId | 6c3de7750ef1436cad6a9533731782b6 |
| displayName | [CT3] Person Identity Administration |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration |
| Snapshot publication reference | HCM-BEC-person-identity |
| ArchiMate semantic ID | id-29e0bf94882559a7b6ed9d092303a560 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Person Identity |
| Reviewed name | Person Identity Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY |
| Name aliases | ["Person Identity"] |

Parent: [Client Administration](business-enabling-capabilities.md#hcm-bec-client-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:person-identity`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-device-administration"></a>

## Clinical Device Administration

Clinical Device Administration represents device types, identifiable instances, patient or location associations and communication endpoints. This lets device-originated information be interpreted in relation to the actual device and care context, rather than merely a network address. Type, instance, assignment and connectivity each answer a different question about the source of device data.

**Overview**

Clinical Device Administration represents device types, identifiable instances, patient or location associations and communication endpoints. This lets device-originated information be interpreted in relation to the actual device and care context, rather than merely a network address. Type, instance, assignment and connectivity each answer a different question about the source of device data.

**Intent**

Clinical Device Administration represents device types, identifiable instances, patient or location associations and communication endpoints. This lets device-originated information be interpreted in relation to the actual device and care context, rather than merely a network address. Type, instance, assignment and connectivity each answer a different question about the source of device data.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Device registration is not maintenance, release management or clinical interpretation. An endpoint credential does not by itself authorise clinical use of the device.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-device-administration |
| canonicalId | 31bcd6fda0d14cc8b992cdca39a3cbed |
| displayName | [CT2] Clinical Device Administration |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration |
| Snapshot publication reference | HCM-BEC-clinical-device-administration |
| ArchiMate semantic ID | id-a6aaf19f1f225b67b1e3a19740d5f457 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Device Administration |
| Reviewed name | Clinical Device Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION |
| Unresolved dimensions | ["relevance"] |

Parent: [Entity Information Enablement](business-enabling-capabilities.md#hcm-bec-healthcare-entity-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Clinical Device Profile Administration](business-enabling-capabilities.md#hcm-bec-device-definition) — Proposed; placement Proposed
- [Device Connectivity Compliance Management](business-enabling-capabilities.md#hcm-bec-reviewed-device-connectivity-compliance-management) — Proposed; placement Proposed
- [Device OA&M Metadata Management](business-enabling-capabilities.md#hcm-bec-reviewed-device-oa-m-metadata-management) — Proposed; placement Proposed
- [Device Utilisation Administration](business-enabling-capabilities.md#hcm-bec-reviewed-device-utilisation-administration) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-device-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-device-definition"></a>

## Clinical Device Profile Administration

Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities.

**Overview**

Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities.

**Intent**

Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities.

**Boundary**

Describes device types and capabilities without taking regulatory approval or clinical-use authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-device-definition |
| canonicalId | d0a1ba80c790476ea6042290eb7ea09e |
| displayName | [CT3] Clinical Device Profile Administration |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.ClinicalDeviceProfileAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-ClinicalDeviceProfileAdministration |
| Snapshot publication reference | HCM-BEC-device-definition |
| ArchiMate semantic ID | id-66b395be368b5464932a1d028bac18a4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Clinical Device Profile Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-DEFINITION |
| Name aliases | ["Device Definition"] |

Parent: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-device-definition`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-device-connectivity-compliance-management"></a>

## Device Connectivity Compliance Management

Manage information about whether device connectivity satisfies applicable standards, policies and integration constraints.

**Overview**

Manage information about whether device connectivity satisfies applicable standards, policies and integration constraints. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage information about whether device connectivity satisfies applicable standards, policies and integration constraints.

**Boundary**

Endpoint registration alone does not establish compliance; conformity concerns applicable standards, policies and integration constraints.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-device-connectivity-compliance-management |
| canonicalId | b16ab359030a4583868f3aac2221074e |
| displayName | [CT3] Device Connectivity Compliance Management |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceConnectivityComplianceManagement |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceConnectivityComplianceManagement |
| Snapshot publication reference | HCM-BEC-reviewed-device-connectivity-compliance-management |
| ArchiMate semantic ID | id-ec5e54de56ea59dbb41c9ab6477ffea5 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Device Connectivity Compliance Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-CONNECTIVITY-COMPLIANCE-MANAGEMENT |

Parent: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-device-connectivity-compliance-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-device-oa-m-metadata-management"></a>

## Device OA&M Metadata Management

Manage operational and administrative metadata needed to understand how a clinical device is connected, monitored and administered.

**Overview**

Manage operational and administrative metadata needed to understand how a clinical device is connected, monitored and administered. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage operational and administrative metadata needed to understand how a clinical device is connected, monitored and administered.

**Boundary**

Connectivity, monitoring and administration metadata do not perform device maintenance, interpret clinical telemetry or determine conformance.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-device-oa-m-metadata-management |
| canonicalId | 659bb194a7ef42ab9ba8a938adb5c179 |
| displayName | [CT3] Device OA&M Metadata Management |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceOaAndMMetadataManagement |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceOaAndMMetadataManagement |
| Snapshot publication reference | HCM-BEC-reviewed-device-oa-m-metadata-management |
| ArchiMate semantic ID | id-c7a85b3afdcf5eca8beba6058ebd91e7 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Device OA&M Metadata Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-OA-M-METADATA-MANAGEMENT |

Parent: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-device-oa-m-metadata-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-device-utilisation-administration"></a>

## Device Utilisation Administration

Administer qualified device-to-subject/place allocation, location, utilisation and operational readiness information, including the time at which an association applies.

**Overview**

Administer qualified device-to-subject/place allocation, location, utilisation and operational readiness information, including the time at which an association applies. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer qualified device-to-subject/place allocation, location, utilisation and operational readiness information, including the time at which an association applies.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-device-utilisation-administration |
| canonicalId | 1a3732b5911c45b3aece38b2db2d0f15 |
| displayName | [CT3] Device Utilisation Administration |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceUtilisationAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceUtilisationAdministration |
| Snapshot publication reference | HCM-BEC-reviewed-device-utilisation-administration |
| ArchiMate semantic ID | id-09b56d7aca615938baee8103ee9ffb15 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Device Utilisation Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-UTILISATION-ADMINISTRATION |

Parent: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-device-utilisation-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-health-product-administration"></a>

## Health Materiel Administration

Health Product Administration retains the reference context for medications, implants, blood products and consumables. Product definitions, formularies and batch or serial information help explain what is being referenced without assigning a supply-chain implementation to Harmonia. No Feature decomposition is established for this responsibility in the present baseline.

**Overview**

Health Product Administration retains the reference context for medications, implants, blood products and consumables. Product definitions, formularies and batch or serial information help explain what is being referenced without assigning a supply-chain implementation to Harmonia. No Feature decomposition is established for this responsibility in the present baseline.

**Intent**

Health Product Administration retains the reference context for medications, implants, blood products and consumables. Product definitions, formularies and batch or serial information help explain what is being referenced without assigning a supply-chain implementation to Harmonia. No Feature decomposition is established for this responsibility in the present baseline.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Product representation does not perform procurement, stock control, dispensing or clinical administration. The contextual child descriptions do not establish additional canonical capabilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:health-product-administration |
| canonicalId | e7081070a9f8444f81c1d8fce37ef664 |
| displayName | [CT2] Health Materiel Administration |
| friendlyName | EntityInformationEnablement.HealthMaterielAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthMaterielAdministration |
| Snapshot publication reference | HCM-BEC-health-product-administration |
| ArchiMate semantic ID | id-c480beb83dfe55e599ff5dc56b4249bb |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Health Product Administration |
| Reviewed name | Health Materiel Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-PRODUCT-ADMINISTRATION |
| Name aliases | ["Health Product Administration"] |
| Unresolved dimensions | ["relevance"] |

Parent: [Entity Information Enablement](business-enabling-capabilities.md#hcm-bec-healthcare-entity-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Health Materiel Catalogue & Formulary Administration](business-enabling-capabilities.md#hcm-bec-product-formulary-catalogue) — Proposed; placement Proposed
- [Health Materiel Profile Administration](business-enabling-capabilities.md#hcm-bec-health-product-definition) — Proposed; placement Proposed
- [Health Materiel Traceability Administration](business-enabling-capabilities.md#hcm-bec-product-batch-serial-tracking) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:health-product-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-product-formulary-catalogue"></a>

## Health Materiel Catalogue & Formulary Administration

Administer health-materiel catalogue and externally approved formulary information, preserving approval and version standing.

**Overview**

Administer health-materiel catalogue and externally approved formulary information, preserving approval and version standing.

**Intent**

Administer health-materiel catalogue and externally approved formulary information, preserving approval and version standing.

**Boundary**

Represents externally approved formularies; it does not acquire formulary approval or dispensing authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-product-formulary-catalogue |
| canonicalId | 28b327760af94b8b986357469524d793 |
| displayName | [CT3] Health Materiel Catalogue & Formulary Administration |
| friendlyName | EntityInformationEnablement.HealthMaterielAdministration.HealthMaterielCatalogueAndFormularyAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthMaterielAdministration.CT3-BEC-HealthMaterielCatalogueAndFormularyAdministration |
| Snapshot publication reference | HCM-BEC-product-formulary-catalogue |
| ArchiMate semantic ID | id-ba55c7918aea5b8f9385ed78d5c72528 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Health Materiel Catalogue & Formulary Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-PRODUCT-ADMINISTRATION.CT3-BEC-PRODUCT-FORMULARY-CATALOGUE |
| Name aliases | ["Product Formulary & Catalogue"] |

Parent: [Health Materiel Administration](business-enabling-capabilities.md#hcm-bec-health-product-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-product-formulary-catalogue`) and the element's version-qualified Properties.

<a id="hcm-bec-health-product-definition"></a>

## Health Materiel Profile Administration

Administer identifying and descriptive information about health materiel referenced in healthcare information and activity.

**Overview**

Administer identifying and descriptive information about health materiel referenced in healthcare information and activity.

**Intent**

Administer identifying and descriptive information about health materiel referenced in healthcare information and activity.

**Boundary**

Reference representation is distinct from procurement, manufacturing and prescribing.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-health-product-definition |
| canonicalId | 9626edae517d40d2bfae619860f18e20 |
| displayName | [CT3] Health Materiel Profile Administration |
| friendlyName | EntityInformationEnablement.HealthMaterielAdministration.HealthMaterielProfileAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthMaterielAdministration.CT3-BEC-HealthMaterielProfileAdministration |
| Snapshot publication reference | HCM-BEC-health-product-definition |
| ArchiMate semantic ID | id-f7048c29ae50565bad9ad68e7f030af4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Health Materiel Profile Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-PRODUCT-ADMINISTRATION.CT3-BEC-HEALTH-PRODUCT-DEFINITION |
| Name aliases | ["Health Product Definition"] |

Parent: [Health Materiel Administration](business-enabling-capabilities.md#hcm-bec-health-product-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-health-product-definition`) and the element's version-qualified Properties.

<a id="hcm-bec-product-batch-serial-tracking"></a>

## Health Materiel Traceability Administration

Administer batch/lot, serial, supplier, manufacturer, receipt/delivery and relevant movement/provenance information concerning health materiel.

**Overview**

Administer batch/lot, serial, supplier, manufacturer, receipt/delivery and relevant movement/provenance information concerning health materiel.

**Intent**

Administer batch/lot, serial, supplier, manufacturer, receipt/delivery and relevant movement/provenance information concerning health materiel.

**Boundary**

Materiel traceability does not make Harmonia a procurement, stock-control or supply-chain manager; specimen movement remains Clinical Logistics Coordination.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-product-batch-serial-tracking |
| canonicalId | 2d41ed0f00a94a4b90ffe87d6fd4e9fe |
| displayName | [CT3] Health Materiel Traceability Administration |
| friendlyName | EntityInformationEnablement.HealthMaterielAdministration.HealthMaterielTraceabilityAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthMaterielAdministration.CT3-BEC-HealthMaterielTraceabilityAdministration |
| Snapshot publication reference | HCM-BEC-product-batch-serial-tracking |
| ArchiMate semantic ID | id-c433d8bde03c55eb85e0864109861ff2 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Health Materiel Traceability Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-PRODUCT-ADMINISTRATION.CT3-BEC-PRODUCT-BATCH-SERIAL-TRACKING |
| Name aliases | ["Product Batch & Serial Tracking"] |

Parent: [Health Materiel Administration](business-enabling-capabilities.md#hcm-bec-health-product-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-product-batch-serial-tracking`) and the element's version-qualified Properties.

<a id="hcm-bec-health-service-administration"></a>

## Health Service Administration

Health Service Administration represents identifiable services and the conditions under which organisations, locations and practitioner roles provide them. Catalogue publishing, service-location-provider association, operating schedules and eligibility definitions allow a service to be understood as more than a location or specialty label. The service context remains material wherever activity meaning or accountability depends on it.

**Overview**

Health Service Administration represents identifiable services and the conditions under which organisations, locations and practitioner roles provide them. Catalogue publishing, service-location-provider association, operating schedules and eligibility definitions allow a service to be understood as more than a location or specialty label. The service context remains material wherever activity meaning or accountability depends on it.

**Intent**

Health Service Administration represents identifiable services and the conditions under which organisations, locations and practitioner roles provide them. Catalogue publishing, service-location-provider association, operating schedules and eligibility definitions allow a service to be understood as more than a location or specialty label. The service context remains material wherever activity meaning or accountability depends on it.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

This capability owns the service mapping associations, not the mapped organisations, locations or practitioners. The supplied Domain03-to-Domain04 trace preserves that distinction; it does not transfer entity ownership.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:health-service-administration |
| canonicalId | ba68a52913a0499d8377553bddfa74a4 |
| displayName | [CT2] Health Service Administration |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration |
| Snapshot publication reference | HCM-BEC-health-service-administration |
| ArchiMate semantic ID | id-19d0a7d9080e5758915878db238ec2d8 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Health Service Administration |
| Reviewed name | Health Service Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION |
| Unresolved dimensions | ["relevance"] |

Parent: [Entity Information Enablement](business-enabling-capabilities.md#hcm-bec-healthcare-entity-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Healthcare Service Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-service-definition) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:health-service-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-healthcare-service-definition"></a>

## Healthcare Service Profile Administration

Administer service definitions, provision relationships, defined availability and eligibility information describing offered healthcare services.

**Overview**

Administer service definitions, provision relationships, defined availability and eligibility information describing offered healthcare services.

**Intent**

Administer service definitions, provision relationships, defined availability and eligibility information describing offered healthcare services.

**Boundary**

Defined availability is not dynamic operational capacity, collective readiness or authority to accept clinical care.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-healthcare-service-definition |
| canonicalId | e75d5eb30936476ea7a9cbadfa43d5ef |
| displayName | [CT3] Healthcare Service Profile Administration |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.HealthcareServiceProfileAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-HealthcareServiceProfileAdministration |
| Snapshot publication reference | HCM-BEC-healthcare-service-definition |
| ArchiMate semantic ID | id-9c7e5f6635095618a9fb1650dc184c64 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Healthcare Service Profile Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-HEALTHCARE-SERVICE-DEFINITION |
| Name aliases | ["Healthcare Service Definition"] |

Parent: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-healthcare-service-definition`) and the element's version-qualified Properties.

<a id="hcm-bec-location-administration"></a>

## Location Administration

Location Administration represents the places and physical relationships relevant to healthcare activity, from facilities to particular care places. Address and geospatial context answer where a place is; hierarchy explains its surroundings; care-place definition records the local characteristics relevant to care. These are stable descriptive responsibilities, distinct from the changing operational state of a bed or ward.

**Overview**

Location Administration represents the places and physical relationships relevant to healthcare activity, from facilities to particular care places. Address and geospatial context answer where a place is; hierarchy explains its surroundings; care-place definition records the local characteristics relevant to care. These are stable descriptive responsibilities, distinct from the changing operational state of a bed or ward.

**Intent**

Location Administration represents the places and physical relationships relevant to healthcare activity, from facilities to particular care places. Address and geospatial context answer where a place is; hierarchy explains its surroundings; care-place definition records the local characteristics relevant to care. These are stable descriptive responsibilities, distinct from the changing operational state of a bed or ward.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Defining a place does not establish occupancy, readiness, facilities maintenance or service capacity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:location-administration |
| canonicalId | c410005667444f99a221483bf577fad2 |
| displayName | [CT2] Location Administration |
| friendlyName | EntityInformationEnablement.LocationAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration |
| Snapshot publication reference | HCM-BEC-location-administration |
| ArchiMate semantic ID | id-243b002011da54d5ac721e2a99d456ea |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Location Administration |
| Reviewed name | Location Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION |
| Unresolved dimensions | ["relevance"] |

Parent: [Entity Information Enablement](business-enabling-capabilities.md#hcm-bec-healthcare-entity-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Care-Place Definition Administration](business-enabling-capabilities.md#hcm-bec-care-place-definition) — Proposed; placement Proposed
- [Healthcare Location Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-location) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:location-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-care-place-definition"></a>

## Care-Place Definition Administration

Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place.

**Overview**

Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place.

**Intent**

Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place.

**Boundary**

Describes a place and its capabilities; operational bed state and readiness remain separately managed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-place-definition |
| canonicalId | 629b493c7d9844baa25c029a87ed04f6 |
| displayName | [CT3] Care-Place Definition Administration |
| friendlyName | EntityInformationEnablement.LocationAdministration.CarePlaceDefinitionAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.CT3-BEC-CarePlaceDefinitionAdministration |
| Snapshot publication reference | HCM-BEC-care-place-definition |
| ArchiMate semantic ID | id-6406fe2dfdf65a79980a8fbd6881f251 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Care-Place Definition Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.CT3-BEC-CARE-PLACE-DEFINITION |
| Name aliases | ["Care-Place Definition"] |

Parent: [Location Administration](business-enabling-capabilities.md#hcm-bec-location-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-place-definition`) and the element's version-qualified Properties.

<a id="hcm-bec-healthcare-location"></a>

## Healthcare Location Profile Administration

Administer the identity, address, geospatial description and physical containment relationships of healthcare locations.

**Overview**

Administer the identity, address, geospatial description and physical containment relationships of healthcare locations.

**Intent**

Administer the identity, address, geospatial description and physical containment relationships of healthcare locations.

**Boundary**

Location description does not establish occupancy, cleanliness, service capacity or service readiness.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-healthcare-location |
| canonicalId | e579e54a08bb4f899a504aa998ba246b |
| displayName | [CT3] Healthcare Location Profile Administration |
| friendlyName | EntityInformationEnablement.LocationAdministration.HealthcareLocationProfileAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.CT3-BEC-HealthcareLocationProfileAdministration |
| Snapshot publication reference | HCM-BEC-healthcare-location |
| ArchiMate semantic ID | id-bc2bc98a7a495b47bf11db08e9c29bae |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Healthcare Location Profile Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.CT3-BEC-HEALTHCARE-LOCATION |
| Name aliases | ["Healthcare Location"] |

Parent: [Location Administration](business-enabling-capabilities.md#hcm-bec-location-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-healthcare-location`) and the element's version-qualified Properties.

<a id="hcm-bec-organisation-administration"></a>

## Organisation Administration

Organisation Administration maintains the identity, profiles, structural relationships and contact context of participating healthcare organisations. It enables a legal or operational entity to be distinguished from a department, network relationship or communication channel. This supports consistent interpretation of providers and destinations across information flows rather than allowing each interface to create an incompatible organisational account.

**Overview**

Organisation Administration maintains the identity, profiles, structural relationships and contact context of participating healthcare organisations. It enables a legal or operational entity to be distinguished from a department, network relationship or communication channel. This supports consistent interpretation of providers and destinations across information flows rather than allowing each interface to create an incompatible organisational account.

**Intent**

Organisation Administration maintains the identity, profiles, structural relationships and contact context of participating healthcare organisations. It enables a legal or operational entity to be distinguished from a department, network relationship or communication channel. This supports consistent interpretation of providers and destinations across information flows rather than allowing each interface to create an incompatible organisational account.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Representing organisational structure does not acquire corporate authority or ownership of every service offered by the organisation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:organisation-administration |
| canonicalId | 14fbdc5ce3dd4c2092c5397dab3ed743 |
| displayName | [CT2] Organisation Administration |
| friendlyName | EntityInformationEnablement.OrganisationAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration |
| Snapshot publication reference | HCM-BEC-organisation-administration |
| ArchiMate semantic ID | id-97a226da4bae53e9bb0cb071c6dbf5c2 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Organisation Administration |
| Reviewed name | Organisation Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION |
| Unresolved dimensions | ["relevance"] |

Parent: [Entity Information Enablement](business-enabling-capabilities.md#hcm-bec-healthcare-entity-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Healthcare Organisation Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-organisation) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:organisation-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-healthcare-organisation"></a>

## Healthcare Organisation Profile Administration

Administer identifying, descriptive, structural, location, contact, registered/legal and recognised endpoint information needed to represent and interact with a healthcare organisation.

**Overview**

Administer identifying, descriptive, structural, location, contact, registered/legal and recognised endpoint information needed to represent and interact with a healthcare organisation.

**Intent**

Administer identifying, descriptive, structural, location, contact, registered/legal and recognised endpoint information needed to represent and interact with a healthcare organisation.

**Boundary**

Maintains verified legal and operational identity information without corporate management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-healthcare-organisation |
| canonicalId | f2de9e9394b849f2943355c7a2882a15 |
| displayName | [CT3] Healthcare Organisation Profile Administration |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.HealthcareOrganisationProfileAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-HealthcareOrganisationProfileAdministration |
| Snapshot publication reference | HCM-BEC-healthcare-organisation |
| ArchiMate semantic ID | id-4bd6510c5dc5510291b60560f6e68b71 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Healthcare Organisation Profile Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-HEALTHCARE-ORGANISATION |
| Name aliases | ["Healthcare Organisation"] |

Parent: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-healthcare-organisation`) and the element's version-qualified Properties.

<a id="hcm-bec-provider-administration"></a>

## Provider Administration

Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing.

**Overview**

Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing.

**Intent**

Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Intent — predecessor explanation

Provide a trustworthy provider context for routing, participation and the application of relevant qualification and privilege constraints.

### Boundary — predecessor explanation

Practitioner Role Resolution establishes neither Clinical Privilege nor Operational Privilege. The descriptive practitioner and role groupings do not establish reconciled CT ancestry.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:provider-administration |
| canonicalId | d6312f85968a4209bafff5ed009297a6 |
| displayName | [CT2] Provider Administration |
| friendlyName | EntityInformationEnablement.ProviderAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration |
| Snapshot publication reference | HCM-BEC-provider-administration |
| ArchiMate semantic ID | id-7b438cff4f2d5b6d928cd7ea538f67a4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Provider Administration |
| Reviewed name | Provider Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION |
| Unresolved dimensions | ["relevance"] |

Parent: [Entity Information Enablement](business-enabling-capabilities.md#hcm-bec-healthcare-entity-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Practitioner Profile Administration](business-enabling-capabilities.md#hcm-bec-practitioner) — Proposed; placement Proposed
- [Practitioner Role Administration](business-enabling-capabilities.md#hcm-bec-practitioner-role) — Proposed; placement Proposed
- [Vendor Liaison](business-enabling-capabilities.md#hcm-bec-reviewed-vendor-liaison) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:provider-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-practitioner"></a>

## Practitioner Profile Administration

Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution.

**Overview**

Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution.

**Intent**

Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution.

**Boundary**

Professional registration and profile representation do not grant clinical or operational privilege.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-practitioner |
| canonicalId | 40759a835f7b456e9257c38b67b0a380 |
| displayName | [CT3] Practitioner Profile Administration |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerProfileAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerProfileAdministration |
| Snapshot publication reference | HCM-BEC-practitioner |
| ArchiMate semantic ID | id-4c6e206c1684595b84981d627a110e53 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Practitioner Profile Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER |
| Name aliases | ["Practitioner"] |

Parent: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-practitioner`) and the element's version-qualified Properties.

<a id="hcm-bec-practitioner-role"></a>

## Practitioner Role Administration

Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints.

**Overview**

Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints.

**Intent**

Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints.

**Boundary**

Role, specialty, capacity and endpoint information supply context; they do not constitute a privilege determination.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-practitioner-role |
| canonicalId | 6651471e67ae46fb8d1c1241c8590be9 |
| displayName | [CT3] Practitioner Role Administration |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerRoleAdministration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerRoleAdministration |
| Snapshot publication reference | HCM-BEC-practitioner-role |
| ArchiMate semantic ID | id-394620b518475a49b9810a7f556b3313 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Practitioner Role Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER-ROLE |
| Name aliases | ["Practitioner Role"] |

Parent: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-practitioner-role`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-vendor-liaison"></a>

## Vendor Liaison

Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision.

**Overview**

Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-vendor-liaison |
| canonicalId | 66358bd6698e4050b12342522fbbb923 |
| displayName | [CT3] Vendor Liaison |
| friendlyName | EntityInformationEnablement.ProviderAdministration.VendorLiaison |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-VendorLiaison |
| Snapshot publication reference | HCM-BEC-reviewed-vendor-liaison |
| ArchiMate semantic ID | id-f65c91b69f5a54cfae47b3572ae8bb4e |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Vendor Liaison |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-VENDOR-LIAISON |

Parent: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-vendor-liaison`) and the element's version-qualified Properties.

<a id="hcm-bec-secondary-health-information-use-enablement"></a>

## Health Informatics Management

Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.

**Overview**

Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.

**Intent**

Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.

**Boundary**

Harmonia is adjacent to a Big Data/analytical environment. It establishes governed assets and reporting inputs; it does not become that analytical platform.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-secondary-health-information-use-enablement |
| canonicalId | a790b5f2c5a1497fa6ec09d7824da32e |
| displayName | [CT1] Health Informatics Management |
| friendlyName | HealthInformaticsManagement |
| canonicalName | CT1-BEC-HealthInformaticsManagement |
| Snapshot publication reference | HCM-BEC-secondary-health-information-use-enablement |
| ArchiMate semantic ID | id-dc7f84d80543529cab8e2ccc86a0428c |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Health Informatics Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT"] |
| Name aliases | ["Secondary Health Information Use Enablement"] |

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Population Health Information Reporting](business-enabling-capabilities.md#hcm-bec-population-health-notification-enablement) — Proposed; placement Proposed
- [Research Information Curation](business-enabling-capabilities.md#hcm-bec-authorised-research-information-provision) — Proposed; placement Proposed
- [Service Planning Data Management](business-enabling-capabilities.md#hcm-bec-service-planning-information-provision) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-secondary-health-information-use-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-population-health-notification-enablement"></a>

## Population Health Information Reporting

Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.

**Overview**

Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.

**Intent**

Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.

**Boundary**

Does not determine population-health findings, create statutory obligations or equate transport delivery with discharge of notification duty.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-population-health-notification-enablement |
| canonicalId | 2135b45eab034370b0063b20e821985d |
| displayName | [CT2] Population Health Information Reporting |
| friendlyName | HealthInformaticsManagement.PopulationHealthInformationReporting |
| canonicalName | CT1-BEC-HealthInformaticsManagement.CT2-BEC-PopulationHealthInformationReporting |
| Snapshot publication reference | HCM-BEC-population-health-notification-enablement |
| ArchiMate semantic ID | id-1ba1dd139e615a7da6f08eef1caa56be |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Population Health Information Reporting |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT2-BEC-POPULATION-HEALTH-NOTIFICATION-ENABLEMENT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT3-BEC-POPULATION-HEALTH-NOTIFICATION-ENABLEMENT"] |
| Name aliases | ["Population Health Notification Enablement"] |

Parent: [Health Informatics Management](business-enabling-capabilities.md#hcm-bec-secondary-health-information-use-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-population-health-notification-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-authorised-research-information-provision"></a>

## Research Information Curation

Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.

**Overview**

Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.

**Intent**

Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.

**Boundary**

Does not grant ethics approval, recruit trial participants, manage trials or become the authority for derived research conclusions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-authorised-research-information-provision |
| canonicalId | 4f5b8cec90324c2989cdc30e80bc8cb9 |
| displayName | [CT2] Research Information Curation |
| friendlyName | HealthInformaticsManagement.ResearchInformationCuration |
| canonicalName | CT1-BEC-HealthInformaticsManagement.CT2-BEC-ResearchInformationCuration |
| Snapshot publication reference | HCM-BEC-authorised-research-information-provision |
| ArchiMate semantic ID | id-2bb6a6fd2f5f55db940b8d0793706993 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Research Information Curation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT2-BEC-AUTHORISED-RESEARCH-INFORMATION-PROVISION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT3-BEC-AUTHORISED-RESEARCH-INFORMATION-PROVISION"] |
| Name aliases | ["Authorised Research Information Provision"] |

Parent: [Health Informatics Management](business-enabling-capabilities.md#hcm-bec-secondary-health-information-use-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-authorised-research-information-provision`) and the element's version-qualified Properties.

<a id="hcm-bec-service-planning-information-provision"></a>

## Service Planning Data Management

Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.

**Overview**

Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.

**Intent**

Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.

**Boundary**

Observed utilisation, operational capacity and planned capacity are distinct; information provision does not commission services or allocate resources.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-service-planning-information-provision |
| canonicalId | 4873cd4b960d45d8b93560d5c8b98500 |
| displayName | [CT2] Service Planning Data Management |
| friendlyName | HealthInformaticsManagement.ServicePlanningDataManagement |
| canonicalName | CT1-BEC-HealthInformaticsManagement.CT2-BEC-ServicePlanningDataManagement |
| Snapshot publication reference | HCM-BEC-service-planning-information-provision |
| ArchiMate semantic ID | id-95802b6c6b505351b4d3d57f8aed2753 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Service Planning Data Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT2-BEC-SERVICE-PLANNING-INFORMATION-PROVISION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT3-BEC-SERVICE-PLANNING-INFORMATION-PROVISION"] |
| Name aliases | ["Service Planning Information Provision"] |

Parent: [Health Informatics Management](business-enabling-capabilities.md#hcm-bec-secondary-health-information-use-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-service-planning-information-provision`) and the element's version-qualified Properties.

<a id="hcm-bec-care-operations-coordination-enablement"></a>

## Health Service Operations Enablement

Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.

**Overview**

Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.

**Intent**

Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.

**Boundary**

Coordination owns only explicitly assigned operational activity. It does not originate clinical clearance, determine clinical work or take workforce and resource governance from their sources.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-operations-coordination-enablement |
| canonicalId | b13ee2c5a4ef4fd6be8a44795696dfc2 |
| displayName | [CT1] Health Service Operations Enablement |
| friendlyName | HealthServiceOperationsEnablement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement |
| Snapshot publication reference | HCM-BEC-care-operations-coordination-enablement |
| ArchiMate semantic ID | id-7d2f4319495b59248bd13f7b256a1c32 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Health Service Operations Enablement |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT |
| Name aliases | ["Care Operations Coordination Enablement"] |

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Bed & Care-Place Management](business-enabling-capabilities.md#hcm-bec-bed-care-place-management) — Canonical; placement Proposed
- [Clinic & Practice Operations](business-enabling-capabilities.md#hcm-bec-clinic-practice-operations) — Canonical; placement Proposed
- [Clinical Competency Administration](business-enabling-capabilities.md#hcm-bec-clinical-competency-management) — Canonical; placement Proposed
- [Clinical Logistics Coordination](business-enabling-capabilities.md#hcm-bec-clinical-logistics-coordination) — Canonical; placement Proposed
- [Clinical Qualification Administration](business-enabling-capabilities.md#hcm-bec-clinical-qualification-management) — Canonical; placement Proposed
- [Clinical Rostering](business-enabling-capabilities.md#hcm-bec-clinical-rostering) — Canonical; placement Proposed
- [Clinical Supervision](business-enabling-capabilities.md#hcm-bec-clinical-supervision) — Canonical; placement Proposed
- [Clinical Training & Education](business-enabling-capabilities.md#hcm-bec-clinical-training-education) — Canonical; placement Proposed
- [Clinical Workforce Capability Monitoring](business-enabling-capabilities.md#hcm-bec-clinical-workforce-management) — Canonical; placement Proposed
- [Credential Management](business-enabling-capabilities.md#hcm-bec-reviewed-credential-management) — Proposed; placement Proposed
- [Discharge Management](business-enabling-capabilities.md#hcm-bec-discharge-management) — Canonical; placement Proposed
- [Emergency Department Operations](business-enabling-capabilities.md#hcm-bec-emergency-department-operations) — Canonical; placement Proposed
- [Mobile Staff Management](business-enabling-capabilities.md#hcm-bec-mobile-staff-management) — Canonical; placement Proposed
- [On-Call Management](business-enabling-capabilities.md#hcm-bec-on-call-management) — Canonical; placement Proposed
- [Outpatient Operations](business-enabling-capabilities.md#hcm-bec-outpatient-operations) — Canonical; placement Proposed
- [Patient Transport](business-enabling-capabilities.md#hcm-bec-patient-transport) — Canonical; placement Proposed
- [Service Capacity Management](business-enabling-capabilities.md#hcm-bec-service-capacity-management) — Canonical; placement Proposed
- [Service Establishment Support](business-enabling-capabilities.md#hcm-bec-reviewed-service-establishment-support) — Proposed; placement Proposed
- [Theatre Operations](business-enabling-capabilities.md#hcm-bec-theatre-operations) — Canonical; placement Proposed
- [Ward Operations](business-enabling-capabilities.md#hcm-bec-ward-operations) — Canonical; placement Proposed
- [Work Allocation & Dispatch](business-enabling-capabilities.md#hcm-bec-work-allocation-dispatch) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-operations-coordination-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-bed-care-place-management"></a>

## Bed & Care-Place Management

Bed & Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants.

**Overview**

Bed & Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants.

**Intent**

Bed & Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Intent — predecessor explanation

Support credible care-place decisions using governed state rather than physical presence alone.

### Boundary — predecessor explanation

A ready bed does not establish service-level capacity or collective readiness, and place state does not determine clinical suitability or care responsibility.

### Harmonia Architectural Relevance — predecessor explanation

Governed place-state progression and operational notification are substantive assigned platform responsibilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:bed--care-place-management |
| canonicalId | f117ccec0bfa4f738e6ddb646b3534a2 |
| displayName | [CT2] Bed & Care-Place Management |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement |
| Snapshot publication reference | HCM-BEC-bed-care-place-management |
| ArchiMate semantic ID | id-99e98c1485575f90883330359d72b921 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bed & Care-Place Management |
| Reviewed name | Bed & Care-Place Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-BED-CARE-PLACE-MANAGEMENT"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Bed & Care-Place State Administration](business-enabling-capabilities.md#hcm-bec-reviewed-bed-care-place-state-administration) — Proposed; placement Proposed
- [Bed & Care-Place Turnover Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-bed-care-place-turnover-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:bed--care-place-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-bed-care-place-state-administration"></a>

## Bed & Care-Place State Administration

Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places.

**Overview**

Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-bed-care-place-state-administration |
| canonicalId | 26a9383d7bb146a19141558d232843de |
| displayName | [CT3] Bed & Care-Place State Administration |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement.BedAndCarePlaceStateAdministration |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement.CT3-BEC-BedAndCarePlaceStateAdministration |
| Snapshot publication reference | HCM-BEC-reviewed-bed-care-place-state-administration |
| ArchiMate semantic ID | id-315b46c620d151868c9f84a0ed45ccd2 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Bed & Care-Place State Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.CT3-BEC-BED-CARE-PLACE-STATE-ADMINISTRATION |

Parent: [Bed & Care-Place Management](business-enabling-capabilities.md#hcm-bec-bed-care-place-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-bed-care-place-state-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-bed-care-place-turnover-coordination"></a>

## Bed & Care-Place Turnover Coordination

Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications.

**Overview**

Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-bed-care-place-turnover-coordination |
| canonicalId | 3c9464f9eae1415d8fa041bd1f3da4f2 |
| displayName | [CT3] Bed & Care-Place Turnover Coordination |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement.BedAndCarePlaceTurnoverCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement.CT3-BEC-BedAndCarePlaceTurnoverCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-bed-care-place-turnover-coordination |
| ArchiMate semantic ID | id-f498770b9a45551990afc0c14336212f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Bed & Care-Place Turnover Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.CT3-BEC-BED-CARE-PLACE-TURNOVER-COORDINATION |

Parent: [Bed & Care-Place Management](business-enabling-capabilities.md#hcm-bec-bed-care-place-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-bed-care-place-turnover-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-clinic-practice-operations"></a>

## Clinic & Practice Operations

Clinic & Practice Operations tracks operating sessions and the operational progression of people through the clinic environment. It makes delays, waiting and completion context visible alongside service-specific capacity responsibilities. This is real-world clinic operation, distinct from the technical queueing machinery that might realise the information flow.

**Overview**

Clinic & Practice Operations tracks operating sessions and the operational progression of people through the clinic environment. It makes delays, waiting and completion context visible alongside service-specific capacity responsibilities. This is real-world clinic operation, distinct from the technical queueing machinery that might realise the information flow.

**Intent**

Clinic & Practice Operations tracks operating sessions and the operational progression of people through the clinic environment. It makes delays, waiting and completion context visible alongside service-specific capacity responsibilities. This is real-world clinic operation, distinct from the technical queueing machinery that might realise the information flow.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Operational queue progression does not determine clinical priority or own the clinician's worklist.

### Harmonia Architectural Relevance — predecessor explanation

Session and flow information materially supports clinic coordination while the clinic retains its contextual capacity responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinic--practice-operations |
| canonicalId | 3dffe63e03124efab255204ee4e9a5d9 |
| displayName | [CT2] Clinic & Practice Operations |
| friendlyName | HealthServiceOperationsEnablement.ClinicAndPracticeOperations |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicAndPracticeOperations |
| Snapshot publication reference | HCM-BEC-clinic-practice-operations |
| ArchiMate semantic ID | id-c907d86a7e995d0f99f48365418f41f8 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinic & Practice Operations |
| Reviewed name | Clinic & Practice Operations |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-CLINIC-PRACTICE-OPERATIONS"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Session & Queue Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-session-queue-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinic--practice-operations`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-session-queue-coordination"></a>

## Session & Queue Coordination

Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information.

**Overview**

Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-session-queue-coordination |
| canonicalId | 22981a95ca6e466582363706fde8fceb |
| displayName | [CT3] Session & Queue Coordination |
| friendlyName | HealthServiceOperationsEnablement.ClinicAndPracticeOperations.SessionAndQueueCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicAndPracticeOperations.CT3-BEC-SessionAndQueueCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-session-queue-coordination |
| ArchiMate semantic ID | id-5c2d8bf1b2ec5762b2be0bc402aa7ca1 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Session & Queue Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.CT3-BEC-SESSION-QUEUE-COORDINATION |

Parent: [Clinic & Practice Operations](business-enabling-capabilities.md#hcm-bec-clinic-practice-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-session-queue-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-competency-management"></a>

## Clinical Competency Administration

Administer information about what clinical capability an organisation can provide and what a practitioner is competent to perform.

**Overview**

Administer information about what clinical capability an organisation can provide and what a practitioner is competent to perform.

**Intent**

Administer information about what clinical capability an organisation can provide and what a practitioner is competent to perform.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Clinical Competency Management is retained as a Reference context concerning practitioner competence. It is distinguishable from checking supplied qualification or competency evidence before specialised work. The model does not establish that representing evidence makes Harmonia the authority that assesses or grants competence.

### Boundary — predecessor explanation

No atomic Features or competency-assessment authority are allocated to this context.

### Harmonia Architectural Relevance — predecessor explanation

The formal Reference classification remains separate from the relevance of operational qualification checking elsewhere.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-competency-management |
| canonicalId | 8a99f4a6e5054d6c997bacec04805c01 |
| displayName | [CT2] Clinical Competency Administration |
| friendlyName | HealthServiceOperationsEnablement.ClinicalCompetencyAdministration |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalCompetencyAdministration |
| Snapshot publication reference | HCM-BEC-clinical-competency-management |
| ArchiMate semantic ID | id-e9ea67f4568d5b698a547301aa89e4bd |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Clinical Competency Management |
| Reviewed name | Clinical Competency Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-COMPETENCY-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT.CT3-BEC-CLINICAL-COMPETENCY-MANAGEMENT"] |
| Name aliases | ["Clinical Competency Management"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-competency-management`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-logistics-coordination"></a>

## Clinical Logistics Coordination

Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services.

**Overview**

Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services.

**Intent**

Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services.

**Boundary**

Includes specimen metadata and specimen movement traceability. Supply/materiel movement belongs to Health Materiel Traceability Administration.

---

## History

### Capability Overview — predecessor explanation

Clinical Logistics Coordination integrates the movement and custody context of clinical material, notably specimen pickup, transport conditions and laboratory delivery. It keeps a material's journey connected to the fulfilment context requiring it. This enables clinical logistics to be followed without reducing specimen integrity to a delivery timestamp alone.

### Boundary — predecessor explanation

Tracking material movement does not perform diagnostic interpretation or establish general enterprise supply-chain ownership.

### Harmonia Architectural Relevance — predecessor explanation

Custody and transport information materially support diagnostic continuity and appropriate interpretation of specimen handling.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-logistics-coordination |
| canonicalId | 0c27be11b4a44cb19e992309b8146a17 |
| displayName | [CT2] Clinical Logistics Coordination |
| friendlyName | HealthServiceOperationsEnablement.ClinicalLogisticsCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalLogisticsCoordination |
| Snapshot publication reference | HCM-BEC-clinical-logistics-coordination |
| ArchiMate semantic ID | id-143b994de5cd593897012a96963e43ca |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Logistics Coordination |
| Reviewed name | Clinical Logistics Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-LOGISTICS-COORDINATION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-CLINICAL-LOGISTICS-COORDINATION"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Specimen Metadata Management](business-enabling-capabilities.md#hcm-bec-reviewed-specimen-metadata-management) — Proposed; placement Proposed
- [Specimen Movement Traceability](business-enabling-capabilities.md#hcm-bec-reviewed-specimen-movement-traceability) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-logistics-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-specimen-metadata-management"></a>

## Specimen Metadata Management

Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen.

**Overview**

Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-specimen-metadata-management |
| canonicalId | 738736425b7446fdae47b06c91c4f69c |
| displayName | [CT3] Specimen Metadata Management |
| friendlyName | HealthServiceOperationsEnablement.ClinicalLogisticsCoordination.SpecimenMetadataManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalLogisticsCoordination.CT3-BEC-SpecimenMetadataManagement |
| Snapshot publication reference | HCM-BEC-reviewed-specimen-metadata-management |
| ArchiMate semantic ID | id-e58d7bdb27f95fb396623167a677a5ba |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Specimen Metadata Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-LOGISTICS-COORDINATION.CT3-BEC-SPECIMEN-METADATA-MANAGEMENT |

Parent: [Clinical Logistics Coordination](business-enabling-capabilities.md#hcm-bec-clinical-logistics-coordination). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-specimen-metadata-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-specimen-movement-traceability"></a>

## Specimen Movement Traceability

Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens.

**Overview**

Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-specimen-movement-traceability |
| canonicalId | bfa2e9a84ddd4287897573dc0d917a00 |
| displayName | [CT3] Specimen Movement Traceability |
| friendlyName | HealthServiceOperationsEnablement.ClinicalLogisticsCoordination.SpecimenMovementTraceability |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalLogisticsCoordination.CT3-BEC-SpecimenMovementTraceability |
| Snapshot publication reference | HCM-BEC-reviewed-specimen-movement-traceability |
| ArchiMate semantic ID | id-8c4d5090fb7150469d8e4b10a7551dab |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Specimen Movement Traceability |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-LOGISTICS-COORDINATION.CT3-BEC-SPECIMEN-MOVEMENT-TRACEABILITY |

Parent: [Clinical Logistics Coordination](business-enabling-capabilities.md#hcm-bec-clinical-logistics-coordination). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-specimen-movement-traceability`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-qualification-management"></a>

## Clinical Qualification Administration

Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing.

**Overview**

Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing.

**Intent**

Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Clinical Qualification Management checks professional and clinical qualification or competency context before specialised operational work is dispatched. It makes relevant standing available at the point where an operational action must respect it. The approved rename preserves the professional responsibility while distinguishing it from operational badges and physical access credentials.

### Boundary — predecessor explanation

The historical name Clinical Credential Management is an alias only for this element. It is not equivalent to Credential Management, and role resolution alone establishes neither clinical nor operational privilege.

### Harmonia Architectural Relevance — predecessor explanation

Qualification context materially constrains legitimate dispatch without making Harmonia the originating professional regulator or training authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-qualification-management |
| canonicalId | 0511a705d74a4f48a0ce92590ae764fa |
| displayName | [CT2] Clinical Qualification Administration |
| friendlyName | HealthServiceOperationsEnablement.ClinicalQualificationAdministration |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalQualificationAdministration |
| Snapshot publication reference | HCM-BEC-clinical-qualification-management |
| ArchiMate semantic ID | id-c1ec3269b7655d079fee5f5b92fbd5e6 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Clinical Qualification Management |
| Reviewed name | Clinical Qualification Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-QUALIFICATION-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT.CT3-BEC-CLINICAL-QUALIFICATION-MANAGEMENT"] |
| Name aliases | ["Clinical Credential Management","Clinical Qualification Management"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Organisation Clinical Certification & Compliance Support](business-enabling-capabilities.md#hcm-bec-reviewed-organisation-clinical-certification-compliance-support) — Proposed; placement Proposed
- [Provider Clinical Qualification Tracking](business-enabling-capabilities.md#hcm-bec-reviewed-provider-clinical-qualification-tracking) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-qualification-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-organisation-clinical-certification-compliance-support"></a>

## Organisation Clinical Certification & Compliance Support

Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation.

**Overview**

Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-organisation-clinical-certification-compliance-support |
| canonicalId | 4de624e7bf49472f91e3e5c734ee8725 |
| displayName | [CT3] Organisation Clinical Certification & Compliance Support |
| friendlyName | HealthServiceOperationsEnablement.ClinicalQualificationAdministration.OrganisationClinicalCertificationAndComplianceSupport |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalQualificationAdministration.CT3-BEC-OrganisationClinicalCertificationAndComplianceSupport |
| Snapshot publication reference | HCM-BEC-reviewed-organisation-clinical-certification-compliance-support |
| ArchiMate semantic ID | id-5ae6f84e6e865ab78d46b5a33357cb9d |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Organisation Clinical Certification & Compliance Support |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-QUALIFICATION-MANAGEMENT.CT3-BEC-ORGANISATION-CLINICAL-CERTIFICATION-COMPLIANCE-SUPPORT |

Parent: [Clinical Qualification Administration](business-enabling-capabilities.md#hcm-bec-clinical-qualification-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-organisation-clinical-certification-compliance-support`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-provider-clinical-qualification-tracking"></a>

## Provider Clinical Qualification Tracking

Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing.

**Overview**

Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-provider-clinical-qualification-tracking |
| canonicalId | 8fd5c78eba3046fa9153fb4efb44e9c8 |
| displayName | [CT3] Provider Clinical Qualification Tracking |
| friendlyName | HealthServiceOperationsEnablement.ClinicalQualificationAdministration.ProviderClinicalQualificationTracking |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalQualificationAdministration.CT3-BEC-ProviderClinicalQualificationTracking |
| Snapshot publication reference | HCM-BEC-reviewed-provider-clinical-qualification-tracking |
| ArchiMate semantic ID | id-7b77530f0c8a569498954dc0a5fcf798 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Provider Clinical Qualification Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-QUALIFICATION-MANAGEMENT.CT3-BEC-PROVIDER-CLINICAL-QUALIFICATION-TRACKING |

Parent: [Clinical Qualification Administration](business-enabling-capabilities.md#hcm-bec-clinical-qualification-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-provider-clinical-qualification-tracking`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-rostering"></a>

## Clinical Rostering

Clinical Rostering provides the Adjacent context of planned workforce availability. It is distinct from consuming roster projections for operational views or resolving who is currently on call. The present architecture retains the context without establishing a Harmonia Feature decomposition for authoring or managing the authoritative roster.

**Overview**

Clinical Rostering provides the Adjacent context of planned workforce availability. It is distinct from consuming roster projections for operational views or resolving who is currently on call. The present architecture retains the context without establishing a Harmonia Feature decomposition for authoring or managing the authoritative roster.

**Intent**

Clinical Rostering provides the Adjacent context of planned workforce availability. It is distinct from consuming roster projections for operational views or resolving who is currently on call. The present architecture retains the context without establishing a Harmonia Feature decomposition for authoring or managing the authoritative roster.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

A projected roster does not confer qualification, privilege, deployment or ownership of the originating schedule.

### Harmonia Architectural Relevance — predecessor explanation

Roster information can inform operations while the formal Adjacent classification preserves its external management.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-rostering |
| canonicalId | 192ca88a289544dcbf6adaf643f39969 |
| displayName | [CT2] Clinical Rostering |
| friendlyName | HealthServiceOperationsEnablement.ClinicalRostering |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalRostering |
| Snapshot publication reference | HCM-BEC-clinical-rostering |
| ArchiMate semantic ID | id-cbe0d80e3f8c501781eb087d3f932b11 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Rostering |
| Reviewed name | Clinical Rostering |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-ROSTERING |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-CLINICAL-ROSTERING"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Clinical Roster Capture & Communication](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-roster-capture-communication) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-rostering`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-clinical-roster-capture-communication"></a>

## Clinical Roster Capture & Communication

Capture and communicate supplied clinical roster information, its effective periods and subsequent coverage changes.

**Overview**

Capture and communicate supplied clinical roster information, its effective periods and subsequent coverage changes. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Capture and communicate supplied clinical roster information, its effective periods and subsequent coverage changes.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-clinical-roster-capture-communication |
| canonicalId | 8243cba81fb6449781d10823ae194237 |
| displayName | [CT3] Clinical Roster Capture & Communication |
| friendlyName | HealthServiceOperationsEnablement.ClinicalRostering.ClinicalRosterCaptureAndCommunication |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalRostering.CT3-BEC-ClinicalRosterCaptureAndCommunication |
| Snapshot publication reference | HCM-BEC-reviewed-clinical-roster-capture-communication |
| ArchiMate semantic ID | id-50bd81c2c0425bbba29347b0aeb4025a |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Clinical Roster Capture & Communication |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-ROSTERING.CT3-BEC-CLINICAL-ROSTER-CAPTURE-COMMUNICATION |

Parent: [Clinical Rostering](business-enabling-capabilities.md#hcm-bec-clinical-rostering). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-clinical-roster-capture-communication`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-supervision"></a>

## Clinical Supervision

Enable responsible clinical participants to identify and use the applicable clinical escalation path.

**Overview**

Enable responsible clinical participants to identify and use the applicable clinical escalation path.

**Intent**

Enable responsible clinical participants to identify and use the applicable clinical escalation path.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Clinical Supervision is a Reference context for the professional supervision of care activity. Its inclusion locates that enterprise responsibility without assigning a Harmonia supervision Feature or clinical-work mandate. Operational visibility into activity and qualifications can be useful context without becoming the professional act of supervising practice.

### Boundary — predecessor explanation

Workflow oversight or platform escalation does not confer clinical supervision or responsibility for clinical adequacy.

### Harmonia Architectural Relevance — predecessor explanation

Reference standing is preserved; no direct platform supervision responsibility is established.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-supervision |
| canonicalId | f697b1ac79b544749315f31819b17969 |
| displayName | [CT2] Clinical Supervision |
| friendlyName | HealthServiceOperationsEnablement.ClinicalSupervision |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalSupervision |
| Snapshot publication reference | HCM-BEC-clinical-supervision |
| ArchiMate semantic ID | id-57136cee07f65bf98b7bfe63fba60fa4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Supervision |
| Reviewed name | Clinical Supervision |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-SUPERVISION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT.CT3-BEC-CLINICAL-SUPERVISION"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Clinical Escalation Path Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-escalation-path-management) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-supervision`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-clinical-escalation-path-management"></a>

## Clinical Escalation Path Management

Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants.

**Overview**

Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants.

**Boundary**

Harmonia manages the escalation path; responsible clinical participants perform supervision and retain clinical judgement.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-clinical-escalation-path-management |
| canonicalId | cca86a32ba8c4dda84caeaa248e9f444 |
| displayName | [CT3] Clinical Escalation Path Management |
| friendlyName | HealthServiceOperationsEnablement.ClinicalSupervision.ClinicalEscalationPathManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalSupervision.CT3-BEC-ClinicalEscalationPathManagement |
| Snapshot publication reference | HCM-BEC-reviewed-clinical-escalation-path-management |
| ArchiMate semantic ID | id-e77accf68d2a5f63b25c3c257d97feec |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Clinical Escalation Path Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-SUPERVISION.CT3-BEC-CLINICAL-ESCALATION-PATH-MANAGEMENT |

Parent: [Clinical Supervision](business-enabling-capabilities.md#hcm-bec-clinical-supervision). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-clinical-escalation-path-management`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-training-education"></a>

## Clinical Training & Education

Clinical Training & Education remains a Reference context for developing practitioners' knowledge and skills. The catalogue recognises its enterprise significance without assigning training delivery or a Feature decomposition to Harmonia. Its separation from qualification checks prevents a represented certificate or training record from being mistaken for the education activity itself.

**Overview**

Clinical Training & Education remains a Reference context for developing practitioners' knowledge and skills. The catalogue recognises its enterprise significance without assigning training delivery or a Feature decomposition to Harmonia. Its separation from qualification checks prevents a represented certificate or training record from being mistaken for the education activity itself.

**Intent**

Clinical Training & Education remains a Reference context for developing practitioners' knowledge and skills. The catalogue recognises its enterprise significance without assigning training delivery or a Feature decomposition to Harmonia. Its separation from qualification checks prevents a represented certificate or training record from being mistaken for the education activity itself.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

No platform training programme, assessment process or clinical teaching authority is established.

### Harmonia Architectural Relevance — predecessor explanation

Reference standing retains enterprise context without a material assigned processing responsibility in this entry.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-training--education |
| canonicalId | d016f35ffbc345d99d7281e968c14ddb |
| displayName | [CT2] Clinical Training & Education |
| friendlyName | HealthServiceOperationsEnablement.ClinicalTrainingAndEducation |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalTrainingAndEducation |
| Snapshot publication reference | HCM-BEC-clinical-training-education |
| ArchiMate semantic ID | id-e4265b53f55553c2acc4f7322e9f1cf2 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Training & Education |
| Reviewed name | Clinical Training & Education |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-TRAINING-EDUCATION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT.CT3-BEC-CLINICAL-TRAINING-EDUCATION"] |
| Release scope | Not Addressed in R1.x / R2.x |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-training--education`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-workforce-management"></a>

## Clinical Workforce Capability Monitoring

Monitor supplied information about the clinical capability available from the workforce without acquiring employment or clinical allocation authority.

**Overview**

Monitor supplied information about the clinical capability available from the workforce without acquiring employment or clinical allocation authority.

**Intent**

Monitor supplied information about the clinical capability available from the workforce without acquiring employment or clinical allocation authority.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Clinical Workforce Management is retained as an Adjacent operational context for the workforce on which services depend. The current enabling catalogue does not assign an atomic Feature set. Its presence allows workforce constraints to be recognised without treating integration of workforce facts as authority to govern employment, coverage or clinical allocation.

### Boundary — predecessor explanation

No new workforce system responsibility or complete CT ancestry is inferred from this contextual entry.

### Harmonia Architectural Relevance — predecessor explanation

Adjacent standing reflects contextual participation rather than management of the workforce itself.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-workforce-management |
| canonicalId | 367b3e5643f44fa785f093db8bb9a6e8 |
| displayName | [CT2] Clinical Workforce Capability Monitoring |
| friendlyName | HealthServiceOperationsEnablement.ClinicalWorkforceCapabilityMonitoring |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalWorkforceCapabilityMonitoring |
| Snapshot publication reference | HCM-BEC-clinical-workforce-management |
| ArchiMate semantic ID | id-54b2e4d160c052c18e147e5980c3a02d |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Clinical Workforce Management |
| Reviewed name | Clinical Workforce Capability Monitoring |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-WORKFORCE-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-CLINICAL-WORKFORCE-MANAGEMENT"] |
| Name aliases | ["Clinical Workforce Management"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-workforce-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-credential-management"></a>

## Credential Management

Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges.

**Overview**

Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-credential-management |
| canonicalId | 21eed4fc8c924496b16c6ddaa32f3d01 |
| displayName | [CT2] Credential Management |
| friendlyName | HealthServiceOperationsEnablement.CredentialManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-CredentialManagement |
| Snapshot publication reference | HCM-BEC-reviewed-credential-management |
| ArchiMate semantic ID | id-ecc5bfe7e27b54dfb59023a05879f1b0 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Credential Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CREDENTIAL-MANAGEMENT |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Clinical Service Credential Definition Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-service-credential-definition-management) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-credential-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-clinical-service-credential-definition-management"></a>

## Clinical Service Credential Definition Management

Manage the definitions of credentials required for participation in a clinical service, distinct from physical badges and authentication credentials.

**Overview**

Manage the definitions of credentials required for participation in a clinical service, distinct from physical badges and authentication credentials. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage the definitions of credentials required for participation in a clinical service, distinct from physical badges and authentication credentials.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-clinical-service-credential-definition-management |
| canonicalId | 2e5c97f51e7e48d1b865ac416dd8099d |
| displayName | [CT3] Clinical Service Credential Definition Management |
| friendlyName | HealthServiceOperationsEnablement.CredentialManagement.ClinicalServiceCredentialDefinitionManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-CredentialManagement.CT3-BEC-ClinicalServiceCredentialDefinitionManagement |
| Snapshot publication reference | HCM-BEC-reviewed-clinical-service-credential-definition-management |
| ArchiMate semantic ID | id-69e67b8a2bb25dd99ec9fb966b9d5481 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Clinical Service Credential Definition Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CREDENTIAL-MANAGEMENT.CT3-BEC-CLINICAL-SERVICE-CREDENTIAL-DEFINITION-MANAGEMENT |

Parent: [Credential Management](business-enabling-capabilities.md#hcm-bec-reviewed-credential-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-clinical-service-credential-definition-management`) and the element's version-qualified Properties.

<a id="hcm-bec-discharge-management"></a>

## Discharge Management

Discharge Management coordinates the reported milestones needed around discharge, including medication supply, transport and home-care confirmations. It makes readiness and impediments visible across disciplines and communicates departure information when physical exit occurs. The responsibility preserves a distinction between gathering confirmations and making the clinical decision that discharge is appropriate.

**Overview**

Discharge Management coordinates the reported milestones needed around discharge, including medication supply, transport and home-care confirmations. It makes readiness and impediments visible across disciplines and communicates departure information when physical exit occurs. The responsibility preserves a distinction between gathering confirmations and making the clinical decision that discharge is appropriate.

**Intent**

Discharge Management coordinates the reported milestones needed around discharge, including medication supply, transport and home-care confirmations. It makes readiness and impediments visible across disciplines and communicates departure information when physical exit occurs. The responsibility preserves a distinction between gathering confirmations and making the clinical decision that discharge is appropriate.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Milestone completion does not transfer clinical responsibility or prove the receiving provider accepted it. Readiness, exit and clinical decision authority remain distinct.

### Harmonia Architectural Relevance — predecessor explanation

Cross-team milestone coordination and departure notification are substantive assigned platform responsibilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:discharge-management |
| canonicalId | ec5e8d6732764aa489c2dd7630fc8fef |
| displayName | [CT2] Discharge Management |
| friendlyName | HealthServiceOperationsEnablement.DischargeManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-DischargeManagement |
| Snapshot publication reference | HCM-BEC-discharge-management |
| ArchiMate semantic ID | id-481836991b9257c69af3d3dceb781a72 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Discharge Management |
| Reviewed name | Discharge Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-DISCHARGE-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-DISCHARGE-MANAGEMENT"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Discharge Progression & Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-discharge-progression-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:discharge-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-discharge-progression-coordination"></a>

## Discharge Progression & Coordination

Coordinate externally established discharge milestones, multidisciplinary readiness information and departure notifications.

**Overview**

Coordinate externally established discharge milestones, multidisciplinary readiness information and departure notifications. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate externally established discharge milestones, multidisciplinary readiness information and departure notifications.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-discharge-progression-coordination |
| canonicalId | d6048993146b45639bab27860f94f34f |
| displayName | [CT3] Discharge Progression & Coordination |
| friendlyName | HealthServiceOperationsEnablement.DischargeManagement.DischargeProgressionAndCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-DischargeManagement.CT3-BEC-DischargeProgressionAndCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-discharge-progression-coordination |
| ArchiMate semantic ID | id-a99640e4d2c05ac3be8b58e113e550dc |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Discharge Progression & Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-DISCHARGE-MANAGEMENT.CT3-BEC-DISCHARGE-PROGRESSION-COORDINATION |

Parent: [Discharge Management](business-enabling-capabilities.md#hcm-bec-discharge-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-discharge-progression-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-emergency-department-operations"></a>

## Emergency Department Operations

This capability maintains the operational context of emergency places, waits and length-of-stay milestones. It supports understanding of occupancy and impediments in the department while keeping capacity's emergency-service meaning intact. Its concern differs from Emergency Care's clinical presentation context, even though both can refer to the same patient encounter.

**Overview**

This capability maintains the operational context of emergency places, waits and length-of-stay milestones. It supports understanding of occupancy and impediments in the department while keeping capacity's emergency-service meaning intact. Its concern differs from Emergency Care's clinical presentation context, even though both can refer to the same patient encounter.

**Intent**

This capability maintains the operational context of emergency places, waits and length-of-stay milestones. It supports understanding of occupancy and impediments in the department while keeping capacity's emergency-service meaning intact. Its concern differs from Emergency Care's clinical presentation context, even though both can refer to the same patient encounter.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Operational timing and escalation do not determine triage or acquire clinical decision authority.

### Harmonia Architectural Relevance — predecessor explanation

Care-place state and operational waiting information materially support coordination of the emergency service.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:emergency-department-operations |
| canonicalId | 24a4b5815faa4d369bc756a6e97d3e9e |
| displayName | [CT2] Emergency Department Operations |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations |
| Snapshot publication reference | HCM-BEC-emergency-department-operations |
| ArchiMate semantic ID | id-60e2aa8fae4d51b993b4022f9ed688ff |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Emergency Department Operations |
| Reviewed name | Emergency Department Operations |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONS"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Emergency Care Progression](business-enabling-capabilities.md#hcm-bec-emergency-care) — Canonical; placement Proposed
- [Emergency Department Operational Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-emergency-department-operational-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:emergency-department-operations`) and the element's version-qualified Properties.

<a id="hcm-bec-emergency-care"></a>

## Emergency Care Progression

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Emergency Care integrates presentation and triage information and correlates it with recent admissions, summaries and known allergies. It supports a rapidly intelligible account of the patient arriving for emergency care without replacing the clinical triage decision. The context is valuable because the regional history may be distributed across participants outside the receiving department.

### Boundary — predecessor explanation

Ingested triage categories remain externally established clinical information; correlation does not confer emergency decision authority.

### Harmonia Architectural Relevance — predecessor explanation

Cross-facility context and current presentation information materially support emergency care continuity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:emergency-care |
| canonicalId | 7125e459a48946d0ba075b5ecc3571b4 |
| displayName | [CT3] Emergency Care Progression |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyCareProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyCareProgression |
| Snapshot publication reference | HCM-BEC-emergency-care |
| ArchiMate semantic ID | id-036bd5a9fa1b5fca9c3245abf3016f44 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Emergency Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-CARE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-EMERGENCY-CARE"] |
| Name aliases | ["Emergency Care"] |

Parent: [Emergency Department Operations](business-enabling-capabilities.md#hcm-bec-emergency-department-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:emergency-care`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-emergency-department-operational-coordination"></a>

## Emergency Department Operational Coordination

Coordinate emergency-department bed/bay occupancy, operational waits, length-of-stay milestones and local capacity information.

**Overview**

Coordinate emergency-department bed/bay occupancy, operational waits, length-of-stay milestones and local capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate emergency-department bed/bay occupancy, operational waits, length-of-stay milestones and local capacity information.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-emergency-department-operational-coordination |
| canonicalId | 8f0b0f3d9e8d44a7827b0835870b4a4f |
| displayName | [CT3] Emergency Department Operational Coordination |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyDepartmentOperationalCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyDepartmentOperationalCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-emergency-department-operational-coordination |
| ArchiMate semantic ID | id-1b8e6ac4079f5e8dad95cae8bbb52aaf |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Emergency Department Operational Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONAL-COORDINATION |

Parent: [Emergency Department Operations](business-enabling-capabilities.md#hcm-bec-emergency-department-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-emergency-department-operational-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-mobile-staff-management"></a>

## Mobile Staff Management

Mobile Staff Management integrates with external business mobile-work mechanisms and receives visit progression and safety milestones. Its established dispatch Feature exchanges assignment, dispatch and status information with the relevant worker context. This is an information integration boundary around mobile activity, rather than a platform claim to decide the clinician's work.

**Overview**

Mobile Staff Management integrates with external business mobile-work mechanisms and receives visit progression and safety milestones. Its established dispatch Feature exchanges assignment, dispatch and status information with the relevant worker context. This is an information integration boundary around mobile activity, rather than a platform claim to decide the clinician's work.

**Intent**

Mobile Staff Management integrates with external business mobile-work mechanisms and receives visit progression and safety milestones. Its established dispatch Feature exchanges assignment, dispatch and status information with the relevant worker context. This is an information integration boundary around mobile activity, rather than a platform claim to decide the clinician's work.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Harmonia does not determine clinical allocation, handover or EMR worklists. External work/task language does not redefine the internal Synthetic Task.

### Harmonia Architectural Relevance — predecessor explanation

Mobile assignment and progression exchanges materially connect roving activity to the wider care environment within that bounded integration role.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:mobile-staff-management |
| canonicalId | 79a4a48fddf74c69acf7e16404d06ea5 |
| displayName | [CT2] Mobile Staff Management |
| friendlyName | HealthServiceOperationsEnablement.MobileStaffManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-MobileStaffManagement |
| Snapshot publication reference | HCM-BEC-mobile-staff-management |
| ArchiMate semantic ID | id-bfbdb4730780550e884dabf485f6cc6f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Mobile Staff Management |
| Reviewed name | Mobile Staff Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-MOBILE-STAFF-MANAGEMENT"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Mobile Worker Dispatch & Safety Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-mobile-worker-dispatch-safety-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:mobile-staff-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-mobile-worker-dispatch-safety-coordination"></a>

## Mobile Worker Dispatch & Safety Coordination

Exchange externally established mobile work assignments, visit progression and safety check-in information with qualified worker context.

**Overview**

Exchange externally established mobile work assignments, visit progression and safety check-in information with qualified worker context. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Exchange externally established mobile work assignments, visit progression and safety check-in information with qualified worker context.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-mobile-worker-dispatch-safety-coordination |
| canonicalId | d72164668dfe448bbebe74e9e1a75233 |
| displayName | [CT3] Mobile Worker Dispatch & Safety Coordination |
| friendlyName | HealthServiceOperationsEnablement.MobileStaffManagement.MobileWorkerDispatchAndSafetyCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-MobileStaffManagement.CT3-BEC-MobileWorkerDispatchAndSafetyCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-mobile-worker-dispatch-safety-coordination |
| ArchiMate semantic ID | id-3fd840ab23015c128060f2b3dd4359b4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Mobile Worker Dispatch & Safety Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.CT3-BEC-MOBILE-WORKER-DISPATCH-SAFETY-COORDINATION |

Parent: [Mobile Staff Management](business-enabling-capabilities.md#hcm-bec-mobile-staff-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-mobile-worker-dispatch-safety-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-on-call-management"></a>

## On-Call Management

On-Call Management resolves the active provider for a specialty, service or hospital at the relevant time. It turns changing coverage information into usable routing context so that operational communication can reach the intended capacity. This differs from authoring the roster or deciding who is professionally qualified for the requested activity.

**Overview**

On-Call Management resolves the active provider for a specialty, service or hospital at the relevant time. It turns changing coverage information into usable routing context so that operational communication can reach the intended capacity. This differs from authoring the roster or deciding who is professionally qualified for the requested activity.

**Intent**

On-Call Management resolves the active provider for a specialty, service or hospital at the relevant time. It turns changing coverage information into usable routing context so that operational communication can reach the intended capacity. This differs from authoring the roster or deciding who is professionally qualified for the requested activity.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Being on call does not by itself establish clinical privilege or assignment of a particular care responsibility.

### Harmonia Architectural Relevance — predecessor explanation

Current provider resolution materially enables appropriate contact and routing across operational boundaries.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:on-call-management |
| canonicalId | b16cef8f2bfd4b92948ec75cbcfa7b9a |
| displayName | [CT2] On-Call Management |
| friendlyName | HealthServiceOperationsEnablement.OnCallManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OnCallManagement |
| Snapshot publication reference | HCM-BEC-on-call-management |
| ArchiMate semantic ID | id-d398ed8aeb1c5dc49a55367f1421d42c |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | On-Call Management |
| Reviewed name | On-Call Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-ON-CALL-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-ON-CALL-MANAGEMENT"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [On-Call Availability & Contact Resolution](business-enabling-capabilities.md#hcm-bec-reviewed-on-call-availability-contact-resolution) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:on-call-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-on-call-availability-contact-resolution"></a>

## On-Call Availability & Contact Resolution

Resolve currently available on-call providers and their contact paths for the applicable specialty, service and time.

**Overview**

Resolve currently available on-call providers and their contact paths for the applicable specialty, service and time. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Resolve currently available on-call providers and their contact paths for the applicable specialty, service and time.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-on-call-availability-contact-resolution |
| canonicalId | 677a816c42414ea694791b4ee5a14285 |
| displayName | [CT3] On-Call Availability & Contact Resolution |
| friendlyName | HealthServiceOperationsEnablement.OnCallManagement.OnCallAvailabilityAndContactResolution |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OnCallManagement.CT3-BEC-OnCallAvailabilityAndContactResolution |
| Snapshot publication reference | HCM-BEC-reviewed-on-call-availability-contact-resolution |
| ArchiMate semantic ID | id-c3f38627d9395f77bf64711330c8f290 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | On-Call Availability & Contact Resolution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-ON-CALL-MANAGEMENT.CT3-BEC-ON-CALL-AVAILABILITY-CONTACT-RESOLUTION |

Parent: [On-Call Management](business-enabling-capabilities.md#hcm-bec-on-call-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-on-call-availability-contact-resolution`) and the element's version-qualified Properties.

<a id="hcm-bec-outpatient-operations"></a>

## Outpatient Operations

Outpatient Operations integrates arrival events and follow-up requirements generated as clinics progress. It connects a person's operational presence with the relevant service context and makes post-clinic booking or diagnostic needs available for subsequent activity. The responsibility retains outpatient-specific capacity semantics without requiring a separate arrival Function where approved reusable composition suffices.

**Overview**

Outpatient Operations integrates arrival events and follow-up requirements generated as clinics progress. It connects a person's operational presence with the relevant service context and makes post-clinic booking or diagnostic needs available for subsequent activity. The responsibility retains outpatient-specific capacity semantics without requiring a separate arrival Function where approved reusable composition suffices.

**Intent**

Outpatient Operations integrates arrival events and follow-up requirements generated as clinics progress. It connects a person's operational presence with the relevant service context and makes post-clinic booking or diagnostic needs available for subsequent activity. The responsibility retains outpatient-specific capacity semantics without requiring a separate arrival Function where approved reusable composition suffices.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

An arrival does not establish consultation completion, and a captured follow-up requirement does not perform the clinical activity.

### Harmonia Architectural Relevance — predecessor explanation

Arrival and clinic-outcome information materially connect outpatient progression to care coordination.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:outpatient-operations |
| canonicalId | 21399d52b9624ddfbeb786f1d72e40c3 |
| displayName | [CT2] Outpatient Operations |
| friendlyName | HealthServiceOperationsEnablement.OutpatientOperations |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OutpatientOperations |
| Snapshot publication reference | HCM-BEC-outpatient-operations |
| ArchiMate semantic ID | id-f8932fe58ea154028a1a5dfa4887f491 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Outpatient Operations |
| Reviewed name | Outpatient Operations |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OUTPATIENT-OPERATIONS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-OUTPATIENT-OPERATIONS"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Outpatient Flow Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-outpatient-flow-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:outpatient-operations`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-outpatient-flow-coordination"></a>

## Outpatient Flow Coordination

Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information.

**Overview**

Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-outpatient-flow-coordination |
| canonicalId | 799ea278af014168b9b534e5fdadc6d1 |
| displayName | [CT3] Outpatient Flow Coordination |
| friendlyName | HealthServiceOperationsEnablement.OutpatientOperations.OutpatientFlowCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OutpatientOperations.CT3-BEC-OutpatientFlowCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-outpatient-flow-coordination |
| ArchiMate semantic ID | id-4595d9da485357d4b4d5ab830b5ad0a1 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Outpatient Flow Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OUTPATIENT-OPERATIONS.CT3-BEC-OUTPATIENT-FLOW-COORDINATION |

Parent: [Outpatient Operations](business-enabling-capabilities.md#hcm-bec-outpatient-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-outpatient-flow-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-patient-transport"></a>

## Patient Transport

Patient Transport coordinates the operational progression of movement requests, including the patient's applicable mobility constraints. Departure, transit and arrival checkpoints make the physical journey visible to participants. The responsibility supports internal and inter-facility movement without treating arrival as acceptance of clinical responsibility at the destination.

**Overview**

Patient Transport coordinates the operational progression of movement requests, including the patient's applicable mobility constraints. Departure, transit and arrival checkpoints make the physical journey visible to participants. The responsibility supports internal and inter-facility movement without treating arrival as acceptance of clinical responsibility at the destination.

**Intent**

Patient Transport coordinates the operational progression of movement requests, including the patient's applicable mobility constraints. Departure, transit and arrival checkpoints make the physical journey visible to participants. The responsibility supports internal and inter-facility movement without treating arrival as acceptance of clinical responsibility at the destination.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Transport completion does not perform clinical handover or establish a care responsibility transition.

### Harmonia Architectural Relevance — predecessor explanation

Transport request and dispatch progression are substantive assigned operational coordination responsibilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:patient-transport |
| canonicalId | 6041d7c0cc5d4519aaea584bb4e6d365 |
| displayName | [CT2] Patient Transport |
| friendlyName | HealthServiceOperationsEnablement.PatientTransport |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-PatientTransport |
| Snapshot publication reference | HCM-BEC-patient-transport |
| ArchiMate semantic ID | id-97b7e74f9a4d5e95a1f89de6b3cfc393 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Patient Transport |
| Reviewed name | Patient Transport |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-PATIENT-TRANSPORT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-PATIENT-TRANSPORT"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Patient Transfer Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-patient-transfer-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:patient-transport`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-patient-transfer-coordination"></a>

## Patient Transfer Coordination

Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones.

**Overview**

Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-patient-transfer-coordination |
| canonicalId | c7c7aa8e34df4568ae4b91cdde380a11 |
| displayName | [CT3] Patient Transfer Coordination |
| friendlyName | HealthServiceOperationsEnablement.PatientTransport.PatientTransferCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-PatientTransport.CT3-BEC-PatientTransferCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-patient-transfer-coordination |
| ArchiMate semantic ID | id-c67d028a12d457b1bff85ea82df63718 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Patient Transfer Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-PATIENT-TRANSPORT.CT3-BEC-PATIENT-TRANSFER-COORDINATION |

Parent: [Patient Transport](business-enabling-capabilities.md#hcm-bec-patient-transport). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-patient-transfer-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-service-capacity-management"></a>

## Service Capacity Management

Service Capacity Management provides a higher-order view by aggregating capacity information across services and facilities. It can consider occupancy, waits and diversion while preserving the service-specific meaning of the underlying capacity. Its purpose is not to erase differences between clinic, ward, theatre, emergency, outpatient and mobile-service operation.

**Overview**

Service Capacity Management provides a higher-order view by aggregating capacity information across services and facilities. It can consider occupancy, waits and diversion while preserving the service-specific meaning of the underlying capacity. Its purpose is not to erase differences between clinic, ward, theatre, emergency, outpatient and mobile-service operation.

**Intent**

Service Capacity Management provides a higher-order view by aggregating capacity information across services and facilities. It can consider occupancy, waits and diversion while preserving the service-specific meaning of the underlying capacity. Its purpose is not to erase differences between clinic, ward, theatre, emergency, outpatient and mobile-service operation.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Consuming contextual capacity does not acquire the originating capacity-management responsibility or infer capacity solely from resource counts.

### Harmonia Architectural Relevance — predecessor explanation

Cross-service operational visibility materially supports coordination while source service contexts retain their authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:service-capacity-management |
| canonicalId | d86105bf9c86445394088f65c895d37f |
| displayName | [CT2] Service Capacity Management |
| friendlyName | HealthServiceOperationsEnablement.ServiceCapacityManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ServiceCapacityManagement |
| Snapshot publication reference | HCM-BEC-service-capacity-management |
| ArchiMate semantic ID | id-2de1ef69d9675c59baa5c66e541e1df5 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Service Capacity Management |
| Reviewed name | Service Capacity Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-SERVICE-CAPACITY-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-CAPACITY-MANAGEMENT"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Service Demand & Capacity Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-service-demand-capacity-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:service-capacity-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-service-demand-capacity-coordination"></a>

## Service Demand & Capacity Coordination

Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers.

**Overview**

Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-service-demand-capacity-coordination |
| canonicalId | 93b5548ce8d04fcd999b97536c1eebf9 |
| displayName | [CT3] Service Demand & Capacity Coordination |
| friendlyName | HealthServiceOperationsEnablement.ServiceCapacityManagement.ServiceDemandAndCapacityCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ServiceCapacityManagement.CT3-BEC-ServiceDemandAndCapacityCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-service-demand-capacity-coordination |
| ArchiMate semantic ID | id-4125661f05bc5551a63787e0afe15491 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Service Demand & Capacity Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-SERVICE-CAPACITY-MANAGEMENT.CT3-BEC-SERVICE-DEMAND-CAPACITY-COORDINATION |

Parent: [Service Capacity Management](business-enabling-capabilities.md#hcm-bec-service-capacity-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-service-demand-capacity-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-service-establishment-support"></a>

## Service Establishment Support

Support the information needed to establish a health service in its intended operating context.

**Overview**

Support the information needed to establish a health service in its intended operating context. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Support the information needed to establish a health service in its intended operating context.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-service-establishment-support |
| canonicalId | 20e67a41e7f44398b0f0cf3d379d8b9c |
| displayName | [CT2] Service Establishment Support |
| friendlyName | HealthServiceOperationsEnablement.ServiceEstablishmentSupport |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ServiceEstablishmentSupport |
| Snapshot publication reference | HCM-BEC-reviewed-service-establishment-support |
| ArchiMate semantic ID | id-1ae0ff5d6b3b54deb3f9a0832045bcf5 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Service Establishment Support |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-SERVICE-ESTABLISHMENT-SUPPORT |
| Release scope | Not Addressed in R1.x / R2.x |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-service-establishment-support`) and the element's version-qualified Properties.

<a id="hcm-bec-theatre-operations"></a>

## Theatre Operations

Theatre Operations integrates surgical case progression and the operational consequences relevant to portering, sterilisation and recovery participants. It gives reported milestones an operational coordination context without originating the surgical acts they describe. Theatre-specific capacity remains a business responsibility even when shared information machinery supports several service contexts.

**Overview**

Theatre Operations integrates surgical case progression and the operational consequences relevant to portering, sterilisation and recovery participants. It gives reported milestones an operational coordination context without originating the surgical acts they describe. Theatre-specific capacity remains a business responsibility even when shared information machinery supports several service contexts.

**Intent**

Theatre Operations integrates surgical case progression and the operational consequences relevant to portering, sterilisation and recovery participants. It gives reported milestones an operational coordination context without originating the surgical acts they describe. Theatre-specific capacity remains a business responsibility even when shared information machinery supports several service contexts.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Tracking incision or recovery information does not authorise surgery or transfer control of the clinical procedure.

### Harmonia Architectural Relevance — predecessor explanation

Milestone-driven operational notifications materially connect supporting activity around the theatre service.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:theatre-operations |
| canonicalId | 4bf7a8260e1444fdac44e97c3557b533 |
| displayName | [CT2] Theatre Operations |
| friendlyName | HealthServiceOperationsEnablement.TheatreOperations |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-TheatreOperations |
| Snapshot publication reference | HCM-BEC-theatre-operations |
| ArchiMate semantic ID | id-ee472a7091b85442911ccf12aac63a4d |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Theatre Operations |
| Reviewed name | Theatre Operations |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-THEATRE-OPERATIONS"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Perioperative Logistics Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-perioperative-logistics-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:theatre-operations`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-perioperative-logistics-coordination"></a>

## Perioperative Logistics Coordination

Coordinate theatre case progression information and notifications to supporting perioperative operational teams.

**Overview**

Coordinate theatre case progression information and notifications to supporting perioperative operational teams. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate theatre case progression information and notifications to supporting perioperative operational teams.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-perioperative-logistics-coordination |
| canonicalId | 6b7c08b60b5b48519bfe4b6f993dd6fb |
| displayName | [CT3] Perioperative Logistics Coordination |
| friendlyName | HealthServiceOperationsEnablement.TheatreOperations.PerioperativeLogisticsCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-TheatreOperations.CT3-BEC-PerioperativeLogisticsCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-perioperative-logistics-coordination |
| ArchiMate semantic ID | id-0df9d488a0865c958bf5b6c3557b8976 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Perioperative Logistics Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS.CT3-BEC-PERIOPERATIVE-LOGISTICS-COORDINATION |

Parent: [Theatre Operations](business-enabling-capabilities.md#hcm-bec-theatre-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-perioperative-logistics-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-ward-operations"></a>

## Ward Operations

Ward Operations coordinates ward occupancy and care-place context and assembles the information needed around shift changes. Its responsibility includes the ward-specific meaning of capacity and patient flow rather than a generic resource counter. It can make pending concerns and tasks visible without becoming the authority managing clinical handover.

**Overview**

Ward Operations coordinates ward occupancy and care-place context and assembles the information needed around shift changes. Its responsibility includes the ward-specific meaning of capacity and patient flow rather than a generic resource counter. It can make pending concerns and tasks visible without becoming the authority managing clinical handover.

**Intent**

Ward Operations coordinates ward occupancy and care-place context and assembles the information needed around shift changes. Its responsibility includes the ward-specific meaning of capacity and patient flow rather than a generic resource counter. It can make pending concerns and tasks visible without becoming the authority managing clinical handover.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Handover information assembly is distinct from performing or managing handover. Context-specific capacity remains with Ward Operations.

### Harmonia Architectural Relevance — predecessor explanation

Correlated ward state and contextual information materially support operational continuity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:ward-operations |
| canonicalId | f996ecca728f4942a88b034aca818a99 |
| displayName | [CT2] Ward Operations |
| friendlyName | HealthServiceOperationsEnablement.WardOperations |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations |
| Snapshot publication reference | HCM-BEC-ward-operations |
| ArchiMate semantic ID | id-014da71d575756afa0db8502a0d1c904 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Ward Operations |
| Reviewed name | Ward Operations |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-WARD-OPERATIONS"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Multidisciplinary Inpatient Care Coordination](business-enabling-capabilities.md#hcm-bec-inpatient-care) — Canonical; placement Proposed
- [Ward Logistics Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-ward-logistics-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:ward-operations`) and the element's version-qualified Properties.

<a id="hcm-bec-inpatient-care"></a>

## Multidisciplinary Inpatient Care Coordination

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Inpatient Care brings together information for multidisciplinary review and captures externally established clinical milestones during an admission. Results, medication records and nursing notes support a shared contextual view, while progression events make reported care status visible. The responsibility concerns information availability and correlation rather than control of the inpatient clinical worklist.

### Boundary — predecessor explanation

Clinical milestone capture does not establish the milestone's clinical authority or manage rounds and handover.

### Harmonia Architectural Relevance — predecessor explanation

The platform materially supplies consolidated inpatient context; milestone tracking may be realised through approved reusable composition without an invented specialised Function.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:inpatient-care |
| canonicalId | c798e38e12e5428dbb8b3cabc977d430 |
| displayName | [CT3] Multidisciplinary Inpatient Care Coordination |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.MultidisciplinaryInpatientCareCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-MultidisciplinaryInpatientCareCoordination |
| Snapshot publication reference | HCM-BEC-inpatient-care |
| ArchiMate semantic ID | id-2fb39d490c105c5d985595cc71f06003 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Inpatient Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-INPATIENT-CARE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-INPATIENT-CARE"] |
| Name aliases | ["Inpatient Care"] |

Parent: [Ward Operations](business-enabling-capabilities.md#hcm-bec-ward-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:inpatient-care`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-ward-logistics-coordination"></a>

## Ward Logistics Coordination

Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information.

**Overview**

Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-ward-logistics-coordination |
| canonicalId | 3249647f7b9846c899f982ab339c81f9 |
| displayName | [CT3] Ward Logistics Coordination |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.WardLogisticsCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-WardLogisticsCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-ward-logistics-coordination |
| ArchiMate semantic ID | id-5d528e4fcb5e5092a26ac5fba62d8fe9 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Ward Logistics Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-WARD-LOGISTICS-COORDINATION |

Parent: [Ward Operations](business-enabling-capabilities.md#hcm-bec-ward-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-ward-logistics-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-work-allocation-dispatch"></a>

## Work Allocation & Dispatch

Work Allocation & Dispatch progresses authorised operational work from a recognised requirement through matching, dispatch, acknowledgement and oversight. It distinguishes an instantiated work item from its assignment and from evidence of performance, allowing stalled activity to be identified and escalated. Examples such as cleaning or transport concern real-world support work, not technical message queues.

**Overview**

Work Allocation & Dispatch progresses authorised operational work from a recognised requirement through matching, dispatch, acknowledgement and oversight. It distinguishes an instantiated work item from its assignment and from evidence of performance, allowing stalled activity to be identified and escalated. Examples such as cleaning or transport concern real-world support work, not technical message queues.

**Intent**

Work Allocation & Dispatch progresses authorised operational work from a recognised requirement through matching, dispatch, acknowledgement and oversight. It distinguishes an instantiated work item from its assignment and from evidence of performance, allowing stalled activity to be identified and escalated. Examples such as cleaning or transport concern real-world support work, not technical message queues.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Intent — predecessor explanation

Make operational work progression accountable from requirement to reported outcome within applicable policy and role constraints.

### Boundary — predecessor explanation

This assignment responsibility does not confer clinical work allocation, qualification or privilege. A technical receipt has only its interaction contract's meaning.

### Harmonia Architectural Relevance — predecessor explanation

Governed operational work progression is a substantive platform responsibility supporting the Core classification.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:work-allocation--dispatch |
| canonicalId | 152a5252952c405994e9b171234efcb0 |
| displayName | [CT2] Work Allocation & Dispatch |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch |
| Snapshot publication reference | HCM-BEC-work-allocation-dispatch |
| ArchiMate semantic ID | id-802e169720d253d1979cf0de2eb2004b |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Work Allocation & Dispatch |
| Reviewed name | Work Allocation & Dispatch |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-WORK-ALLOCATION-DISPATCH"] |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Operational Work Assignment & Dispatch Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-operational-work-assignment-dispatch-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:work-allocation--dispatch`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-operational-work-assignment-dispatch-coordination"></a>

## Operational Work Assignment & Dispatch Coordination

Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority.

**Overview**

Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-operational-work-assignment-dispatch-coordination |
| canonicalId | 3b58935cfa974176989c3c75d76a00fe |
| displayName | [CT3] Operational Work Assignment & Dispatch Coordination |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch.OperationalWorkAssignmentAndDispatchCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch.CT3-BEC-OperationalWorkAssignmentAndDispatchCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-operational-work-assignment-dispatch-coordination |
| ArchiMate semantic ID | id-b4d502174176517d89e42e4cd48e1fff |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Operational Work Assignment & Dispatch Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.CT3-BEC-OPERATIONAL-WORK-ASSIGNMENT-DISPATCH-COORDINATION |

Parent: [Work Allocation & Dispatch](business-enabling-capabilities.md#hcm-bec-work-allocation-dispatch). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-operational-work-assignment-dispatch-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-independent-service-assurance-enablement"></a>

## Independent Assurance Enablement

Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.

**Overview**

Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.

**Intent**

Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.

**Boundary**

Assurance cannot manage, remediate or approve its subject merely because it observes it. Clinical adequacy, financial governance and source-information authority remain outside this mandate.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-independent-service-assurance-enablement |
| canonicalId | 616682b612d244429e656b040e002d56 |
| displayName | [CT1] Independent Assurance Enablement |
| friendlyName | IndependentAssuranceEnablement |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement |
| Snapshot publication reference | HCM-BEC-independent-service-assurance-enablement |
| ArchiMate semantic ID | id-7045e58c19235175b54f5d054232b883 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Independent Assurance Enablement |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT |
| Name aliases | ["Independent Service Assurance Enablement"] |

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Assurance Service Review](business-enabling-capabilities.md#hcm-bec-reviewed-assurance-service-review) — Proposed; placement Proposed
- [Assurance Specification](business-enabling-capabilities.md#hcm-bec-assurance-specification-enablement) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-independent-service-assurance-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-assurance-service-review"></a>

## Assurance Service Review

Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself.

**Overview**

Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself.

**Boundary**

Reviews the assurance service itself. Canonical evidence association, subject assessment, adjudication, findings and conclusions are retained separately pending adjudication.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-assurance-service-review |
| canonicalId | b1a791458d644d4794895e4fa42ec83c |
| displayName | [CT2] Assurance Service Review |
| friendlyName | IndependentAssuranceEnablement.AssuranceServiceReview |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-AssuranceServiceReview |
| Snapshot publication reference | HCM-BEC-reviewed-assurance-service-review |
| ArchiMate semantic ID | id-8a7d47abbe015a309139a0ee2c1250d4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Assurance Service Review |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SERVICE-REVIEW |

Parent: [Independent Assurance Enablement](business-enabling-capabilities.md#hcm-bec-independent-service-assurance-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Assurance Service Evaluation](business-enabling-capabilities.md#hcm-bec-reviewed-assurance-service-evaluation) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-assurance-service-review`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-assurance-service-evaluation"></a>

## Assurance Service Evaluation

Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review.

**Overview**

Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review.

**Boundary**

Evaluation concerns operation, effectiveness, appropriateness and conformance of the assurance service; it is not execution of assurance against a subject.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-assurance-service-evaluation |
| canonicalId | 039343518db542d48cde6afab84a3e1e |
| displayName | [CT3] Assurance Service Evaluation |
| friendlyName | IndependentAssuranceEnablement.AssuranceServiceReview.AssuranceServiceEvaluation |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-AssuranceServiceReview.CT3-BEC-AssuranceServiceEvaluation |
| Snapshot publication reference | HCM-BEC-reviewed-assurance-service-evaluation |
| ArchiMate semantic ID | id-0bcc748a066653819c90f63952059b9a |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Assurance Service Evaluation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SERVICE-REVIEW.CT3-BEC-ASSURANCE-SERVICE-EVALUATION |

Parent: [Assurance Service Review](business-enabling-capabilities.md#hcm-bec-reviewed-assurance-service-review). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-assurance-service-evaluation`) and the element's version-qualified Properties.

<a id="hcm-bec-assurance-specification-enablement"></a>

## Assurance Specification

Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.

**Overview**

Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.

**Intent**

Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.

**Boundary**

Modelling and criteria stewardship do not approve governing requirements or perform independent conclusion-making.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-assurance-specification-enablement |
| canonicalId | b81775d89d774cf8ba61731d13c67a54 |
| displayName | [CT2] Assurance Specification |
| friendlyName | IndependentAssuranceEnablement.AssuranceSpecification |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-AssuranceSpecification |
| Snapshot publication reference | HCM-BEC-assurance-specification-enablement |
| ArchiMate semantic ID | id-e18e1674a30c5c97a40ef75faea25663 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Assurance Specification |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT |
| Name aliases | ["Assurance Specification Enablement"] |

Parent: [Independent Assurance Enablement](business-enabling-capabilities.md#hcm-bec-independent-service-assurance-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Assurance Criteria Administration](business-enabling-capabilities.md#hcm-bec-assurance-criteria-management) — Canonical; placement Proposed
- [Assurance Design](business-enabling-capabilities.md#hcm-bec-assurance-design) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-assurance-specification-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-assurance-criteria-management"></a>

## Assurance Criteria Administration

The capability to establish and manage reusable, governed and temporally identifiable assurance criteria through which subjects may be evaluated.

Definition — reviewed candidate

Administer reusable governed assurance criteria and their version-specific and temporal applicability without changing governing requirement authority.

**Overview**

Administer reusable governed assurance criteria and their version-specific and temporal applicability without changing governing requirement authority.

**Intent**

Administer reusable governed assurance criteria and their version-specific and temporal applicability without changing governing requirement authority.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Assurance Criteria Management maintains reusable evaluative criteria with governed standing and temporal applicability. Version and effective context matter because a conclusion must be understood against the criteria applicable to the subject and time being assessed. Criteria may operationalise governing requirements, but their management is not authority to change those requirements.

### Boundary — predecessor explanation

Modelling criteria does not inherently approve them. Detailed criterion states and approval workflows remain unresolved, and criteria need not all originate in formal Governance.

### Harmonia Architectural Relevance — predecessor explanation

Formal relevance remains Unresolved. Established assurance support explains the enabling purpose without classifying it by inference or allocating software responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:assurance-criteria-management |
| canonicalId | 1bfd71f24321428c8871b587d413f47e |
| displayName | [CT3] Assurance Criteria Administration |
| friendlyName | IndependentAssuranceEnablement.AssuranceSpecification.AssuranceCriteriaAdministration |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-AssuranceSpecification.CT3-BEC-AssuranceCriteriaAdministration |
| Snapshot publication reference | HCM-BEC-assurance-criteria-management |
| ArchiMate semantic ID | id-5b9c494f5afb5f348c6e67afc7e022d5 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Assurance Criteria Management |
| Reviewed name | Assurance Criteria Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT.CT3-BEC-ASSURANCE-CRITERIA-MANAGEMENT |
| Name aliases | ["Assurance Criteria Management"] |
| Unresolved dimensions | ["feature_decomposition","relevance"] |

Parent: [Assurance Specification](business-enabling-capabilities.md#hcm-bec-assurance-specification-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:assurance-criteria-management`) and the element's version-qualified Properties.

<a id="hcm-bec-assurance-design"></a>

## Assurance Design

The capability to determine and define how satisfaction of a governed requirement, constraint or expected behaviour will be assured, including its assurance disposition, applicable criteria, required evidence and evaluation expectations.

Definition — reviewed candidate

Model how satisfaction of a governed subject or requirement will be assured, preserving applicable assurance dispositions and boundaries.

**Overview**

Model how satisfaction of a governed subject or requirement will be assured, preserving applicable assurance dispositions and boundaries.

**Intent**

Model how satisfaction of a governed subject or requirement will be assured, preserving applicable assurance dispositions and boundaries.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Assurance Design determines how satisfaction of a governed requirement or expected behaviour will be assured. It makes the disposition, criteria, evidence and evaluation expectations explicit alongside behaviour and failure/recovery design. Its output is a basis for assurance rather than proof that the implemented behaviour has satisfied the requirement.

### Boundary — predecessor explanation

Designing assurance does not confer approval authority, define the underlying obligation or allocate a runtime component. No Feature decomposition or CT ancestry is established.

### Harmonia Architectural Relevance — predecessor explanation

Formal classification remains Unresolved. Its approved support to Health Service Assurance establishes purpose and Enterprise Capability contributions without adjudicating this separate relevance dimension.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:assurance-design |
| canonicalId | 71c8a612b88f4ecba049e94d506bd0c2 |
| displayName | [CT3] Assurance Design |
| friendlyName | IndependentAssuranceEnablement.AssuranceSpecification.AssuranceDesign |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-AssuranceSpecification.CT3-BEC-AssuranceDesign |
| Snapshot publication reference | HCM-BEC-assurance-design |
| ArchiMate semantic ID | id-e5aa0bd0a82b5ee484397627845f3f1c |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Assurance Design |
| Reviewed name | Assurance Design |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT.CT3-BEC-ASSURANCE-DESIGN |
| Unresolved dimensions | ["feature_decomposition","relevance"] |

Parent: [Assurance Specification](business-enabling-capabilities.md#hcm-bec-assurance-specification-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:assurance-design`) and the element's version-qualified Properties.

<a id="hcm-bec-governed-health-information-enablement"></a>

## Shared Information & Coordination Enablement

Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.

**Overview**

Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.

**Intent**

Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.

**Boundary**

Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-governed-health-information-enablement |
| canonicalId | 0062fcacae1740988b6be2bda2aa1674 |
| displayName | [CT1] Shared Information & Coordination Enablement |
| friendlyName | SharedInformationAndCoordinationEnablement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement |
| Snapshot publication reference | HCM-BEC-governed-health-information-enablement |
| ArchiMate semantic ID | id-a5127b36f8a25c748c68426668173fcd |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Shared Information & Coordination Enablement |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT |
| Name aliases | ["Governed Health Information Enablement"] |

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Calendar Management](business-enabling-capabilities.md#hcm-bec-calendar-management) — Canonical; placement Proposed
- [Clinical Collaboration](business-enabling-capabilities.md#hcm-bec-clinical-collaboration) — Canonical; placement Proposed
- [Clinical Knowledge Services](business-enabling-capabilities.md#hcm-bec-clinical-knowledge-services) — Canonical; placement Proposed
- [General Collaboration](business-enabling-capabilities.md#hcm-bec-general-collaboration) — Canonical; placement Proposed
- [Health Information Access](business-enabling-capabilities.md#hcm-bec-health-information-access) — Canonical; placement Proposed
- [Health Information Communication](business-enabling-capabilities.md#hcm-bec-reviewed-health-information-communication) — Proposed; placement Proposed
- [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control) — Canonical; placement Proposed
- [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange) — Canonical; placement Proposed
- [Information Asset Stewardship Management](business-enabling-capabilities.md#hcm-bec-reviewed-information-asset-stewardship-management) — Proposed; placement Proposed
- [Information Content Maintainer Management](business-enabling-capabilities.md#hcm-bec-reviewed-information-content-maintainer-management) — Proposed; placement Proposed
- [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance) — Canonical; placement Proposed
- [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record) — Canonical; placement Proposed
- [Presentation Services](business-enabling-capabilities.md#hcm-bec-presentation-services) — Canonical; placement Proposed
- [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-workflow-activity-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-governed-health-information-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-calendar-management"></a>

## Calendar Management

Calendar Management projects schedules and correlates events with temporal commitments. It brings rosters, on-call windows, facility calendars and appointment milestones into operational views without replacing their authoritative sources. This distinguishes the temporal context used for coordination from the business decisions that establish the schedule.

**Overview**

Calendar Management projects schedules and correlates events with temporal commitments. It brings rosters, on-call windows, facility calendars and appointment milestones into operational views without replacing their authoritative sources. This distinguishes the temporal context used for coordination from the business decisions that establish the schedule.

**Intent**

Calendar Management projects schedules and correlates events with temporal commitments. It brings rosters, on-call windows, facility calendars and appointment milestones into operational views without replacing their authoritative sources. This distinguishes the temporal context used for coordination from the business decisions that establish the schedule.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Projection does not author the roster or confer qualification, privilege or clinical assignment authority.

### Harmonia Architectural Relevance — predecessor explanation

Consistent temporal projections and event correlation are direct platform coordination responsibilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:calendar-management |
| canonicalId | 7669036f7e6749219412fc64df5f7064 |
| displayName | [CT2] Calendar Management |
| friendlyName | SharedInformationAndCoordinationEnablement.CalendarManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-CalendarManagement |
| Snapshot publication reference | HCM-BEC-calendar-management |
| ArchiMate semantic ID | id-6b7034b5239c505d858d651b8ee78832 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Calendar Management |
| Reviewed name | Calendar Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CALENDAR-MANAGEMENT |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-CALENDAR-MANAGEMENT"] |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Operational & Clinical Calendar Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-operational-clinical-calendar-coordination) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:calendar-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-operational-clinical-calendar-coordination"></a>

## Operational & Clinical Calendar Coordination

Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters.

**Overview**

Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-operational-clinical-calendar-coordination |
| canonicalId | f6d8f1310ba543f69e8b57bf05603db3 |
| displayName | [CT3] Operational & Clinical Calendar Coordination |
| friendlyName | SharedInformationAndCoordinationEnablement.CalendarManagement.OperationalAndClinicalCalendarCoordination |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-CalendarManagement.CT3-BEC-OperationalAndClinicalCalendarCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-operational-clinical-calendar-coordination |
| ArchiMate semantic ID | id-d975937d894d521eaf2465cf00b08aaf |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Operational & Clinical Calendar Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CALENDAR-MANAGEMENT.CT3-BEC-OPERATIONAL-CLINICAL-CALENDAR-COORDINATION |

Parent: [Calendar Management](business-enabling-capabilities.md#hcm-bec-calendar-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-operational-clinical-calendar-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-collaboration"></a>

## Clinical Collaboration

Clinical Collaboration provides secure multidisciplinary spaces bound to clinical context, including governed access to longitudinal information during discussion. Its purpose is contextual conversation between care participants rather than a separate source of authoritative clinical facts. A statement in a conversation remains discourse until explicitly authored through the governed record boundary.

**Overview**

Clinical Collaboration provides secure multidisciplinary spaces bound to clinical context, including governed access to longitudinal information during discussion. Its purpose is contextual conversation between care participants rather than a separate source of authoritative clinical facts. A statement in a conversation remains discourse until explicitly authored through the governed record boundary.

**Intent**

Clinical Collaboration provides secure multidisciplinary spaces bound to clinical context, including governed access to longitudinal information during discussion. Its purpose is contextual conversation between care participants rather than a separate source of authoritative clinical facts. A statement in a conversation remains discourse until explicitly authored through the governed record boundary.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Channel metadata must remain free of PHI. The source Function's mention of managing clinical handovers conflicts with the metamodel's clinical-work boundary; both are preserved for review without assigning that authority here.

### Harmonia Architectural Relevance — predecessor explanation

LHR-aware collaboration and controlled contextual queries are substantive Core responsibilities, distinct from clinical practice or record authorship.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-collaboration |
| canonicalId | e6429225463a44d39f82e490e254e5d4 |
| displayName | [CT2] Clinical Collaboration |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration |
| Snapshot publication reference | HCM-BEC-clinical-collaboration |
| ArchiMate semantic ID | id-0770a7338b0a53adb14971ace57fea7f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Collaboration |
| Reviewed name | Clinical Collaboration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION"] |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Clinical Context Collaboration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-context-collaboration) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-collaboration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-clinical-context-collaboration"></a>

## Clinical Context Collaboration

Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records.

**Overview**

Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-clinical-context-collaboration |
| canonicalId | f11617eed6ff4d6fa237644ec95b28b2 |
| displayName | [CT3] Clinical Context Collaboration |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.ClinicalContextCollaboration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.CT3-BEC-ClinicalContextCollaboration |
| Snapshot publication reference | HCM-BEC-reviewed-clinical-context-collaboration |
| ArchiMate semantic ID | id-97b0afca9c9e513d8205eb624be06169 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Clinical Context Collaboration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.CT3-BEC-CLINICAL-CONTEXT-COLLABORATION |

Parent: [Clinical Collaboration](business-enabling-capabilities.md#hcm-bec-clinical-collaboration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-clinical-context-collaboration`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-knowledge-services"></a>

## Clinical Knowledge Services

Administer clinical knowledge requests, distribute governed knowledge artefacts and administer approved reference datasets.

**Overview**

Administer clinical knowledge requests, distribute governed knowledge artefacts and administer approved reference datasets.

**Intent**

Administer clinical knowledge requests, distribute governed knowledge artefacts and administer approved reference datasets.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Capability Overview — predecessor explanation

Clinical Knowledge Services resolves governed clinical concepts and applies governed terminology mappings. It makes codes usable across local and standard semantic systems while retaining their source meaning. The responsibility is semantic reference and mapping enablement, distinct from formulating reusable clinical knowledge or applying that knowledge to an individual.

### Boundary — predecessor explanation

Code translation does not establish equivalence beyond the governed map or confer clinical decision authority.

### Harmonia Architectural Relevance — predecessor explanation

Concept resolution and mapping materially support meaningful information exchange and interpretation across regional systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-knowledge-services |
| canonicalId | bbd2bd21859f40e39963bdbe7a851686 |
| displayName | [CT2] Clinical Knowledge Services |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices |
| Snapshot publication reference | HCM-BEC-clinical-knowledge-services |
| ArchiMate semantic ID | id-c7f56c1dcc525ca8805682bc1d92907d |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Knowledge Services |
| Reviewed name | Clinical Knowledge Services |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-CLINICAL-KNOWLEDGE-SERVICES"] |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Clinical Knowledge Artefact Distribution Management](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-knowledge-artefact-distribution-management) — Proposed; placement Proposed
- [Clinical Knowledge Request Administration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-knowledge-request-administration) — Proposed; placement Proposed
- [Governed Reference Dataset Administration](business-enabling-capabilities.md#hcm-bec-governed-reference-dataset-administration) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-knowledge-services`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-clinical-knowledge-artefact-distribution-management"></a>

## Clinical Knowledge Artefact Distribution Management

Manage distribution of governed clinical knowledge artefacts to intended consumers under their applicable use contracts.

**Overview**

Manage distribution of governed clinical knowledge artefacts to intended consumers under their applicable use contracts. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage distribution of governed clinical knowledge artefacts to intended consumers under their applicable use contracts.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-clinical-knowledge-artefact-distribution-management |
| canonicalId | cd29a9eb798949bc8bc81bb5d434cabf |
| displayName | [CT3] Clinical Knowledge Artefact Distribution Management |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.ClinicalKnowledgeArtefactDistributionManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.CT3-BEC-ClinicalKnowledgeArtefactDistributionManagement |
| Snapshot publication reference | HCM-BEC-reviewed-clinical-knowledge-artefact-distribution-management |
| ArchiMate semantic ID | id-048fd66ba8095ee3bb759b0c89bdf32f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Clinical Knowledge Artefact Distribution Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-CLINICAL-KNOWLEDGE-ARTEFACT-DISTRIBUTION-MANAGEMENT |

Parent: [Clinical Knowledge Services](business-enabling-capabilities.md#hcm-bec-clinical-knowledge-services). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-clinical-knowledge-artefact-distribution-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-clinical-knowledge-request-administration"></a>

## Clinical Knowledge Request Administration

Administer clinical knowledge requests and their qualified resolution to applicable governed knowledge or reference content.

**Overview**

Administer clinical knowledge requests and their qualified resolution to applicable governed knowledge or reference content. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer clinical knowledge requests and their qualified resolution to applicable governed knowledge or reference content.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-clinical-knowledge-request-administration |
| canonicalId | 543c53483a2249d78044c6635b7b7c48 |
| displayName | [CT3] Clinical Knowledge Request Administration |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.ClinicalKnowledgeRequestAdministration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.CT3-BEC-ClinicalKnowledgeRequestAdministration |
| Snapshot publication reference | HCM-BEC-reviewed-clinical-knowledge-request-administration |
| ArchiMate semantic ID | id-59010f4f41df5e509a40226be95a123b |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Clinical Knowledge Request Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-CLINICAL-KNOWLEDGE-REQUEST-ADMINISTRATION |

Parent: [Clinical Knowledge Services](business-enabling-capabilities.md#hcm-bec-clinical-knowledge-services). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-clinical-knowledge-request-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-governed-reference-dataset-administration"></a>

## Governed Reference Dataset Administration

Administer approved runtime reference-dataset content and revisions separately from the reusable models defining its meaning.

**Overview**

Administer approved runtime reference-dataset content and revisions separately from the reusable models defining its meaning.

**Intent**

Administer approved runtime reference-dataset content and revisions separately from the reusable models defining its meaning.

**Boundary**

Does not own external ontology authority, execute authorisation policy or establish durable truth independently of the normal persistence boundary.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-governed-reference-dataset-administration |
| canonicalId | 682b8684ad9e49d5a5c348a148081607 |
| displayName | [CT3] Governed Reference Dataset Administration |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.GovernedReferenceDatasetAdministration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.CT3-BEC-GovernedReferenceDatasetAdministration |
| Snapshot publication reference | HCM-BEC-governed-reference-dataset-administration |
| ArchiMate semantic ID | id-cc4edaeee1d3519185ee94a683e4a5e0 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Governed Reference Dataset Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION"] |

Parent: [Clinical Knowledge Services](business-enabling-capabilities.md#hcm-bec-clinical-knowledge-services). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-governed-reference-dataset-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-general-collaboration"></a>

## General Collaboration

General Collaboration provides secure operational messaging for departmental, administrative and facility-support participants. It gives non-clinical coordination an interaction context without assuming every discussion requires a patient-linked clinical workspace. Its role is communication enablement rather than ownership of the support activity being discussed.

**Overview**

General Collaboration provides secure operational messaging for departmental, administrative and facility-support participants. It gives non-clinical coordination an interaction context without assuming every discussion requires a patient-linked clinical workspace. Its role is communication enablement rather than ownership of the support activity being discussed.

**Intent**

General Collaboration provides secure operational messaging for departmental, administrative and facility-support participants. It gives non-clinical coordination an interaction context without assuming every discussion requires a patient-linked clinical workspace. Its role is communication enablement rather than ownership of the support activity being discussed.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Conversation does not assign operational authority, complete work or become an authoritative record merely through retention.

### Harmonia Architectural Relevance — predecessor explanation

Operational messaging materially enables coordination while the participants' business responsibilities remain external to the collaboration mechanism.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:general-collaboration |
| canonicalId | 3bb740e6db284112b054905c0c6c80be |
| displayName | [CT2] General Collaboration |
| friendlyName | SharedInformationAndCoordinationEnablement.GeneralCollaboration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-GeneralCollaboration |
| Snapshot publication reference | HCM-BEC-general-collaboration |
| ArchiMate semantic ID | id-220d1f41d46b57fba6377fcd533fb94a |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | General Collaboration |
| Reviewed name | General Collaboration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-GENERAL-COLLABORATION |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-GENERAL-COLLABORATION"] |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Operational Collaboration](business-enabling-capabilities.md#hcm-bec-reviewed-operational-collaboration) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:general-collaboration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-operational-collaboration"></a>

## Operational Collaboration

Enable operational team discussion and collaboration without promoting conversational content to clinical record authority.

**Overview**

Enable operational team discussion and collaboration without promoting conversational content to clinical record authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable operational team discussion and collaboration without promoting conversational content to clinical record authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-operational-collaboration |
| canonicalId | 631fdb8b47084008ac9b5de28b26972b |
| displayName | [CT3] Operational Collaboration |
| friendlyName | SharedInformationAndCoordinationEnablement.GeneralCollaboration.OperationalCollaboration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-GeneralCollaboration.CT3-BEC-OperationalCollaboration |
| Snapshot publication reference | HCM-BEC-reviewed-operational-collaboration |
| ArchiMate semantic ID | id-7a89497d659f50629cd7dd7105c0e411 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Operational Collaboration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-GENERAL-COLLABORATION.CT3-BEC-OPERATIONAL-COLLABORATION |

Parent: [General Collaboration](business-enabling-capabilities.md#hcm-bec-general-collaboration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-operational-collaboration`) and the element's version-qualified Properties.

<a id="hcm-bec-health-information-access"></a>

## Health Information Access

Health Information Access enables authorised discovery and retrieval across clinical information. It combines semantic search and discrete retrieval with access-qualified filtering so that a discoverable item is not automatically disclosable. It consumes established policy and consent determinations rather than making the search interface an alternative security authority.

**Overview**

Health Information Access enables authorised discovery and retrieval across clinical information. It combines semantic search and discrete retrieval with access-qualified filtering so that a discoverable item is not automatically disclosable. It consumes established policy and consent determinations rather than making the search interface an alternative security authority.

**Intent**

Health Information Access enables authorised discovery and retrieval across clinical information. It combines semantic search and discrete retrieval with access-qualified filtering so that a discoverable item is not automatically disclosable. It consumes established policy and consent determinations rather than making the search interface an alternative security authority.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Retrieval alone does not realise the filtering Feature. Unestablished Feature associations remain unestablished even where the behaviour shares the same owner.

### Harmonia Architectural Relevance — predecessor explanation

Responsive governed access is a direct platform responsibility. The approved support to Individual Care Delivery remains bounded and does not establish runtime dependencies for every clinical ability.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:health-information-access |
| canonicalId | cc253aea3c5f489da0502df74dde1669 |
| displayName | [CT2] Health Information Access |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationAccess |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationAccess |
| Snapshot publication reference | HCM-BEC-health-information-access |
| ArchiMate semantic ID | id-20ff65f6cd1d5932968574bc72bd7aa3 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Health Information Access |
| Reviewed name | Health Information Access |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-ACCESS |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-ACCESS"] |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Governed Health Information Retrieval](business-enabling-capabilities.md#hcm-bec-reviewed-governed-health-information-retrieval) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:health-information-access`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-governed-health-information-retrieval"></a>

## Governed Health Information Retrieval

Retrieve and search health information and apply access-qualified result filtering under established access and consent determinations.

**Overview**

Retrieve and search health information and apply access-qualified result filtering under established access and consent determinations. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Retrieve and search health information and apply access-qualified result filtering under established access and consent determinations.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-governed-health-information-retrieval |
| canonicalId | 76e74e46ca4d44a1a7bb116b358f231c |
| displayName | [CT3] Governed Health Information Retrieval |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationAccess.GovernedHealthInformationRetrieval |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationAccess.CT3-BEC-GovernedHealthInformationRetrieval |
| Snapshot publication reference | HCM-BEC-reviewed-governed-health-information-retrieval |
| ArchiMate semantic ID | id-2b06d69a6fe75ab79dc8d11abe5c780a |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Governed Health Information Retrieval |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-ACCESS.CT3-BEC-GOVERNED-HEALTH-INFORMATION-RETRIEVAL |

Parent: [Health Information Access](business-enabling-capabilities.md#hcm-bec-health-information-access). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-governed-health-information-retrieval`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-health-information-communication"></a>

## Health Information Communication

Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.

**Overview**

Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-health-information-communication |
| canonicalId | 7925605bf60f4b288e826020409a09d9 |
| displayName | [CT2] Health Information Communication |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationCommunication |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationCommunication |
| Snapshot publication reference | HCM-BEC-reviewed-health-information-communication |
| ArchiMate semantic ID | id-5004f4019d1059108356b6ee7cfcaa8e |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Health Information Communication |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-COMMUNICATION |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Patient Health Library Access](business-enabling-capabilities.md#hcm-bec-reviewed-patient-health-library-access) — Proposed; placement Proposed
- [Patient Notification Services](business-enabling-capabilities.md#hcm-bec-reviewed-patient-notification-services) — Proposed; placement Proposed
- [Patient Self-Service](business-enabling-capabilities.md#hcm-bec-reviewed-patient-self-service) — Proposed; placement Proposed
- [Patient Task Communication Services](business-enabling-capabilities.md#hcm-bec-reviewed-patient-task-communication-services) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-health-information-communication`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-patient-health-library-access"></a>

## Patient Health Library Access

Enable authorised patient access to appropriately governed health knowledge and library information.

**Overview**

Enable authorised patient access to appropriately governed health knowledge and library information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable authorised patient access to appropriately governed health knowledge and library information.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-patient-health-library-access |
| canonicalId | 808e5a3c3f6b496fa329962088f0bee0 |
| displayName | [CT3] Patient Health Library Access |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationCommunication.PatientHealthLibraryAccess |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationCommunication.CT3-BEC-PatientHealthLibraryAccess |
| Snapshot publication reference | HCM-BEC-reviewed-patient-health-library-access |
| ArchiMate semantic ID | id-cb1d28c1e8135785a123e050fa77210a |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Patient Health Library Access |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-COMMUNICATION.CT3-BEC-PATIENT-HEALTH-LIBRARY-ACCESS |

Parent: [Health Information Communication](business-enabling-capabilities.md#hcm-bec-reviewed-health-information-communication). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-patient-health-library-access`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-patient-notification-services"></a>

## Patient Notification Services

Enable governed patient notifications concerning health information and applicable care or operational events.

**Overview**

Enable governed patient notifications concerning health information and applicable care or operational events. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable governed patient notifications concerning health information and applicable care or operational events.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-patient-notification-services |
| canonicalId | 9d172cd511b742499c4e7c23f6630a35 |
| displayName | [CT3] Patient Notification Services |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationCommunication.PatientNotificationServices |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationCommunication.CT3-BEC-PatientNotificationServices |
| Snapshot publication reference | HCM-BEC-reviewed-patient-notification-services |
| ArchiMate semantic ID | id-ac3af58b121552608850d847992c8afa |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Patient Notification Services |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-COMMUNICATION.CT3-BEC-PATIENT-NOTIFICATION-SERVICES |

Parent: [Health Information Communication](business-enabling-capabilities.md#hcm-bec-reviewed-health-information-communication). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-patient-notification-services`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-patient-self-service"></a>

## Patient Self-Service

Enable patients to undertake permitted information-access and administrative self-service interactions.

**Overview**

Enable patients to undertake permitted information-access and administrative self-service interactions. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable patients to undertake permitted information-access and administrative self-service interactions.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-patient-self-service |
| canonicalId | d4956cab8b2b46c396899949c9486a1c |
| displayName | [CT3] Patient Self-Service |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationCommunication.PatientSelfService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationCommunication.CT3-BEC-PatientSelfService |
| Snapshot publication reference | HCM-BEC-reviewed-patient-self-service |
| ArchiMate semantic ID | id-446513b2b23f594f80789cda9185d637 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Patient Self-Service |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-COMMUNICATION.CT3-BEC-PATIENT-SELF-SERVICE |

Parent: [Health Information Communication](business-enabling-capabilities.md#hcm-bec-reviewed-health-information-communication). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-patient-self-service`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-patient-task-communication-services"></a>

## Patient Task Communication Services

Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts.

**Overview**

Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-patient-task-communication-services |
| canonicalId | ba8ce1d54ace4bc9aa3521dd954f36cb |
| displayName | [CT3] Patient Task Communication Services |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationCommunication.PatientTaskCommunicationServices |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationCommunication.CT3-BEC-PatientTaskCommunicationServices |
| Snapshot publication reference | HCM-BEC-reviewed-patient-task-communication-services |
| ArchiMate semantic ID | id-6f0e40c416e15dfa8c70a216c1a2bb6f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Patient Task Communication Services |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-COMMUNICATION.CT3-BEC-PATIENT-TASK-COMMUNICATION-SERVICES |

Parent: [Health Information Communication](business-enabling-capabilities.md#hcm-bec-reviewed-health-information-communication). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-patient-task-communication-services`) and the element's version-qualified Properties.

<a id="hcm-bec-health-information-control"></a>

## Health Information Control

Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.

**Overview**

Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.

**Intent**

Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

---

## History

### Capability Overview — predecessor explanation

Health Information Control establishes the policy and security basis for governed information operations. Default-deny authority evaluation, consent constraints, security-context propagation and security-significant evidence have distinct purposes: deciding permission, retaining restrictions, carrying attributable operational context and preserving required evidence. The capability makes security intrinsic to the operation rather than optional presentation behaviour.

### Boundary — predecessor explanation

Security context is operational context and does not automatically become resource content. Security-context validation has no invented one-to-one mapping to propagation, and audit recording is not an assurance conclusion.

### Harmonia Architectural Relevance — predecessor explanation

Assigned policy enforcement and attributable context are substantive Core responsibilities across managed operations.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:health-information-control |
| canonicalId | 44cc0e17ad534675bf26dce6e46a68ff |
| displayName | [CT2] Health Information Control |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl |
| Snapshot publication reference | HCM-BEC-health-information-control |
| ArchiMate semantic ID | id-b9c636efc8a35c18bd0dce7db89caa88 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Health Information Control |
| Reviewed name | Health Information Control |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL"] |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Patient Health Information Disclosure Management](business-enabling-capabilities.md#hcm-bec-reviewed-patient-health-information-disclosure-management) — Proposed; placement Proposed
- [Patient Health Information Filter Management](business-enabling-capabilities.md#hcm-bec-reviewed-patient-health-information-filter-management) — Proposed; placement Proposed
- [Patient Health Information Restriction Management](business-enabling-capabilities.md#hcm-bec-reviewed-patient-health-information-restriction-management) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:health-information-control`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-patient-health-information-disclosure-management"></a>

## Patient Health Information Disclosure Management

Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions.

**Overview**

Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-patient-health-information-disclosure-management |
| canonicalId | 7f918639e23842e78fa92f1bf47c5a7b |
| displayName | [CT3] Patient Health Information Disclosure Management |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.PatientHealthInformationDisclosureManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.CT3-BEC-PatientHealthInformationDisclosureManagement |
| Snapshot publication reference | HCM-BEC-reviewed-patient-health-information-disclosure-management |
| ArchiMate semantic ID | id-38a9a0f14b5659f4b88fab8c9ae53274 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Patient Health Information Disclosure Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.CT3-BEC-PATIENT-HEALTH-INFORMATION-DISCLOSURE-MANAGEMENT |

Parent: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-patient-health-information-disclosure-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-patient-health-information-filter-management"></a>

## Patient Health Information Filter Management

Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations.

**Overview**

Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-patient-health-information-filter-management |
| canonicalId | 62f673613c76494eba972cad3e4e8d0b |
| displayName | [CT3] Patient Health Information Filter Management |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.PatientHealthInformationFilterManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.CT3-BEC-PatientHealthInformationFilterManagement |
| Snapshot publication reference | HCM-BEC-reviewed-patient-health-information-filter-management |
| ArchiMate semantic ID | id-1174f336b2b158229d94060f3b0f43ed |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Patient Health Information Filter Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.CT3-BEC-PATIENT-HEALTH-INFORMATION-FILTER-MANAGEMENT |

Parent: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-patient-health-information-filter-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-patient-health-information-restriction-management"></a>

## Patient Health Information Restriction Management

Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management.

**Overview**

Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-patient-health-information-restriction-management |
| canonicalId | 262366990bda4ac4ace5429269cf6a98 |
| displayName | [CT3] Patient Health Information Restriction Management |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.PatientHealthInformationRestrictionManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.CT3-BEC-PatientHealthInformationRestrictionManagement |
| Snapshot publication reference | HCM-BEC-reviewed-patient-health-information-restriction-management |
| ArchiMate semantic ID | id-0c99a409fcad5e30b1c384f25976197f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Patient Health Information Restriction Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.CT3-BEC-PATIENT-HEALTH-INFORMATION-RESTRICTION-MANAGEMENT |

Parent: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-patient-health-information-restriction-management`) and the element's version-qualified Properties.

<a id="hcm-bec-health-information-exchange"></a>

## Health Information Exchange Enablement

As a BEC, Health Information Exchange provides reusable information-movement enablement through submission, retrieval mediation, addressed distribution and syndication. The models distinguish sending to a participant, requesting information, deliberately distributing to named recipients and publishing changes to matching subscribers. Their common responsibility is the exchange context, including routing and delivery state, rather than the business meaning of every payload.

**Overview**

As a BEC, Health Information Exchange provides reusable information-movement enablement through submission, retrieval mediation, addressed distribution and syndication. The models distinguish sending to a participant, requesting information, deliberately distributing to named recipients and publishing changes to matching subscribers. Their common responsibility is the exchange context, including routing and delivery state, rather than the business meaning of every payload.

**Intent**

As a BEC, Health Information Exchange provides reusable information-movement enablement through submission, retrieval mediation, addressed distribution and syndication. The models distinguish sending to a participant, requesting information, deliberately distributing to named recipients and publishing changes to matching subscribers. Their common responsibility is the exchange context, including routing and delivery state, rather than the business meaning of every payload.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

The capability owns exchange transaction state without acquiring clinical-document ownership. Its name collision with the BC remains unresolved; exchange egress ends management of the emitted representation.

### Harmonia Architectural Relevance — predecessor explanation

Multi-party exchange and per-destination delivery tracking are substantive Core responsibilities, bounded by the external contract and originating information authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:health-information-exchange |
| canonicalId | a61a15908ed544e6acf3e9cab63a0267 |
| displayName | [CT2] Health Information Exchange Enablement |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement |
| Snapshot publication reference | HCM-BEC-health-information-exchange |
| ArchiMate semantic ID | id-0712aaf441dc5100a5ac79c4b7848477 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Health Information Exchange |
| Reviewed name | Health Information Exchange |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT"] |
| Name aliases | ["Health Information Exchange","Health Information Exchange Enablement"] |
| Unresolved dimensions | ["name_uniqueness"] |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [HIE Endpoint State Management](business-enabling-capabilities.md#hcm-bec-reviewed-hie-endpoint-state-management) — Proposed; placement Proposed
- [HIE Implementation Management](business-enabling-capabilities.md#hcm-bec-reviewed-hie-implementation-management) — Proposed; placement Proposed
- [HIE Requirement Management](business-enabling-capabilities.md#hcm-bec-reviewed-hie-requirement-management) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:health-information-exchange`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-hie-endpoint-state-management"></a>

## HIE Endpoint State Management

Manage recognised exchange endpoint registrations, operational standing and contract-qualified availability information.

**Overview**

Manage recognised exchange endpoint registrations, operational standing and contract-qualified availability information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage recognised exchange endpoint registrations, operational standing and contract-qualified availability information.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-hie-endpoint-state-management |
| canonicalId | 1bec951a1c98450b8d4672c5c424f9bd |
| displayName | [CT3] HIE Endpoint State Management |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieEndpointStateManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieEndpointStateManagement |
| Snapshot publication reference | HCM-BEC-reviewed-hie-endpoint-state-management |
| ArchiMate semantic ID | id-6a1c86bc579e59e5854aafb3ace5c01f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | HIE Endpoint State Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.CT3-BEC-HIE-ENDPOINT-STATE-MANAGEMENT |

Parent: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-hie-endpoint-state-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-hie-implementation-management"></a>

## HIE Implementation Management

Manage implementation information and progression for established exchange requirements, including submission, retrieval, distribution and syndication interactions.

**Overview**

Manage implementation information and progression for established exchange requirements, including submission, retrieval, distribution and syndication interactions. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage implementation information and progression for established exchange requirements, including submission, retrieval, distribution and syndication interactions.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-hie-implementation-management |
| canonicalId | 8ce29be373e04252b7aa881b6a72f375 |
| displayName | [CT3] HIE Implementation Management |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieImplementationManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieImplementationManagement |
| Snapshot publication reference | HCM-BEC-reviewed-hie-implementation-management |
| ArchiMate semantic ID | id-390fb11c89e7528e96684afcec04539a |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | HIE Implementation Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.CT3-BEC-HIE-IMPLEMENTATION-MANAGEMENT |

Parent: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-hie-implementation-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-hie-requirement-management"></a>

## HIE Requirement Management

Manage the identified information-exchange requirements, interaction contracts and participant obligations applicable to a health-information exchange.

**Overview**

Manage the identified information-exchange requirements, interaction contracts and participant obligations applicable to a health-information exchange. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage the identified information-exchange requirements, interaction contracts and participant obligations applicable to a health-information exchange.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-hie-requirement-management |
| canonicalId | 1c852a1799984feba50011172983c865 |
| displayName | [CT3] HIE Requirement Management |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieRequirementManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieRequirementManagement |
| Snapshot publication reference | HCM-BEC-reviewed-hie-requirement-management |
| ArchiMate semantic ID | id-7c8885a8c55552888e8b3cda35c06693 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | HIE Requirement Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.CT3-BEC-HIE-REQUIREMENT-MANAGEMENT |

Parent: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-hie-requirement-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-information-asset-stewardship-management"></a>

## Information Asset Stewardship Management

Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses.

**Overview**

Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-information-asset-stewardship-management |
| canonicalId | 5bac15316d8d4f4db6e7a253f1acfbe3 |
| displayName | [CT2] Information Asset Stewardship Management |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationAssetStewardshipManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationAssetStewardshipManagement |
| Snapshot publication reference | HCM-BEC-reviewed-information-asset-stewardship-management |
| ArchiMate semantic ID | id-0e93a43b398a503d804b87204f939a09 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Information Asset Stewardship Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-ASSET-STEWARDSHIP-MANAGEMENT |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Information Asset Definition](business-enabling-capabilities.md#hcm-bec-reviewed-information-asset-definition) — Proposed; placement Proposed
- [Information Asset Tag Management](business-enabling-capabilities.md#hcm-bec-reviewed-information-asset-tag-management) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-information-asset-stewardship-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-information-asset-definition"></a>

## Information Asset Definition

Define the identifying, semantic and governed-use characteristics of an information asset.

**Overview**

Define the identifying, semantic and governed-use characteristics of an information asset. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Define the identifying, semantic and governed-use characteristics of an information asset.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-information-asset-definition |
| canonicalId | 05c58b2ccfcc45e9a6c70d9a569c4f61 |
| displayName | [CT3] Information Asset Definition |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationAssetStewardshipManagement.InformationAssetDefinition |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationAssetStewardshipManagement.CT3-BEC-InformationAssetDefinition |
| Snapshot publication reference | HCM-BEC-reviewed-information-asset-definition |
| ArchiMate semantic ID | id-d7356584bc845225b4e2870b336411ee |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Information Asset Definition |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-ASSET-STEWARDSHIP-MANAGEMENT.CT3-BEC-INFORMATION-ASSET-DEFINITION |

Parent: [Information Asset Stewardship Management](business-enabling-capabilities.md#hcm-bec-reviewed-information-asset-stewardship-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-information-asset-definition`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-information-asset-tag-management"></a>

## Information Asset Tag Management

Manage attributable tags describing information assets without treating caller-controlled tags as security authority.

**Overview**

Manage attributable tags describing information assets without treating caller-controlled tags as security authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage attributable tags describing information assets without treating caller-controlled tags as security authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-information-asset-tag-management |
| canonicalId | df27c3ba6e164e56af7bff4e1eb1954a |
| displayName | [CT3] Information Asset Tag Management |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationAssetStewardshipManagement.InformationAssetTagManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationAssetStewardshipManagement.CT3-BEC-InformationAssetTagManagement |
| Snapshot publication reference | HCM-BEC-reviewed-information-asset-tag-management |
| ArchiMate semantic ID | id-6630abffd8ac5e50b947a7179e1d976c |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Information Asset Tag Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-ASSET-STEWARDSHIP-MANAGEMENT.CT3-BEC-INFORMATION-ASSET-TAG-MANAGEMENT |

Parent: [Information Asset Stewardship Management](business-enabling-capabilities.md#hcm-bec-reviewed-information-asset-stewardship-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-information-asset-tag-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-information-content-maintainer-management"></a>

## Information Content Maintainer Management

Administer information-content maintainer participation, source provenance and governed information products.

**Overview**

Administer information-content maintainer participation, source provenance and governed information products. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer information-content maintainer participation, source provenance and governed information products.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-information-content-maintainer-management |
| canonicalId | b81a1ab42af24c2e9896b8f3f78d291a |
| displayName | [CT2] Information Content Maintainer Management |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationContentMaintainerManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationContentMaintainerManagement |
| Snapshot publication reference | HCM-BEC-reviewed-information-content-maintainer-management |
| ArchiMate semantic ID | id-f4e98809bf18585aac90aa1597b89bb0 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Information Content Maintainer Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Data Provenance Capture](business-enabling-capabilities.md#hcm-bec-information-lineage-stewardship) — Proposed; placement Proposed
- [Data Scientist Administration](business-enabling-capabilities.md#hcm-bec-reviewed-data-scientist-administration) — Proposed; placement Proposed
- [Information Product Administration](business-enabling-capabilities.md#hcm-bec-reviewed-information-product-administration) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-information-content-maintainer-management`) and the element's version-qualified Properties.

<a id="hcm-bec-information-lineage-stewardship"></a>

## Data Provenance Capture

Capture meaningful source attribution and transformation lineage for governed information content without transferring originating authority.

**Overview**

Capture meaningful source attribution and transformation lineage for governed information content without transferring originating authority.

**Intent**

Capture meaningful source attribution and transformation lineage for governed information content without transferring originating authority.

**Boundary**

Lineage does not grant originating authority, decide clinical correctness or replace activity audit and independent assurance.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-information-lineage-stewardship |
| canonicalId | 44a7937520a54945b06b5fd4a2bbd870 |
| displayName | [CT3] Data Provenance Capture |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationContentMaintainerManagement.DataProvenanceCapture |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationContentMaintainerManagement.CT3-BEC-DataProvenanceCapture |
| Snapshot publication reference | HCM-BEC-information-lineage-stewardship |
| ArchiMate semantic ID | id-72018038e84c518c8bb24cb76c3b418e |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Data Provenance Capture |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-LINEAGE-STEWARDSHIP |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-INFORMATION-LINEAGE-STEWARDSHIP"] |
| Name aliases | ["Information Lineage Stewardship"] |

Parent: [Information Content Maintainer Management](business-enabling-capabilities.md#hcm-bec-reviewed-information-content-maintainer-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-information-lineage-stewardship`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-data-scientist-administration"></a>

## Data Scientist Administration

Administer profiles and qualified participation information for data scientists maintaining governed information content.

**Overview**

Administer profiles and qualified participation information for data scientists maintaining governed information content. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer profiles and qualified participation information for data scientists maintaining governed information content.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-data-scientist-administration |
| canonicalId | 3bb1a11fba234717bf40c9e6452f401a |
| displayName | [CT3] Data Scientist Administration |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationContentMaintainerManagement.DataScientistAdministration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationContentMaintainerManagement.CT3-BEC-DataScientistAdministration |
| Snapshot publication reference | HCM-BEC-reviewed-data-scientist-administration |
| ArchiMate semantic ID | id-fe08dab2f85453af9f1ebec308bf64f6 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Data Scientist Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-DATA-SCIENTIST-ADMINISTRATION |

Parent: [Information Content Maintainer Management](business-enabling-capabilities.md#hcm-bec-reviewed-information-content-maintainer-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-data-scientist-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-information-product-administration"></a>

## Information Product Administration

Administer identifiable governed information products, their purpose, content standing and qualified release/use context.

**Overview**

Administer identifiable governed information products, their purpose, content standing and qualified release/use context. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer identifiable governed information products, their purpose, content standing and qualified release/use context.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-information-product-administration |
| canonicalId | b5a6a31855834ea58870ebb6c51ed4b0 |
| displayName | [CT3] Information Product Administration |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationContentMaintainerManagement.InformationProductAdministration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationContentMaintainerManagement.CT3-BEC-InformationProductAdministration |
| Snapshot publication reference | HCM-BEC-reviewed-information-product-administration |
| ArchiMate semantic ID | id-ed9226f42cd853c08bb48e6ce86cfd4b |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Information Product Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-PRODUCT-ADMINISTRATION |

Parent: [Information Content Maintainer Management](business-enabling-capabilities.md#hcm-bec-reviewed-information-content-maintainer-management). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-information-product-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-information-design-governance"></a>

## Information Design Governance

Information Design Governance maintains canonical definitions, semantic bindings, exchange profiles and the conformance rules applied to representations. It makes meaning and constraints available across regional information flows so that an accepted technical format is not mistaken for semantic correctness. Governance and verification remain distinguishable from the clinical facts represented.

**Overview**

Information Design Governance maintains canonical definitions, semantic bindings, exchange profiles and the conformance rules applied to representations. It makes meaning and constraints available across regional information flows so that an accepted technical format is not mistaken for semantic correctness. Governance and verification remain distinguishable from the clinical facts represented.

**Intent**

Information Design Governance maintains canonical definitions, semantic bindings, exchange profiles and the conformance rules applied to representations. It makes meaning and constraints available across regional information flows so that an accepted technical format is not mistaken for semantic correctness. Governance and verification remain distinguishable from the clinical facts represented.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Consumers of definitions do not acquire their semantic authority, and conformance checking does not establish clinical truth or permission to disclose.

### Harmonia Architectural Relevance — predecessor explanation

Governed semantics and representation conformance are direct platform responsibilities that constrain both internal use and boundary exchange.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:information-design-governance |
| canonicalId | f39ed9ebcc724857a4bf7a2b8e045c7a |
| displayName | [CT2] Information Design Governance |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance |
| Snapshot publication reference | HCM-BEC-information-design-governance |
| ArchiMate semantic ID | id-7f29effdff4f526fb023f7c3f8d2d82e |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Information Design Governance |
| Reviewed name | Information Design Governance |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE"] |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Enterprise Canonical/Reusable Model Design](business-enabling-capabilities.md#hcm-bec-reviewed-enterprise-canonical-reusable-model-design) — Proposed; placement Proposed
- [Enterprise Canonical/Reusable Model Library Management](business-enabling-capabilities.md#hcm-bec-reviewed-enterprise-canonical-reusable-model-library-management) — Proposed; placement Proposed
- [Enterprise Canonical/Reusable Model Utilisation Support](business-enabling-capabilities.md#hcm-bec-reviewed-enterprise-canonical-reusable-model-utilisation-support) — Proposed; placement Proposed
- [Enterprise Reference Data Stewardship](business-enabling-capabilities.md#hcm-bec-reviewed-enterprise-reference-data-stewardship) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:information-design-governance`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-enterprise-canonical-reusable-model-design"></a>

## Enterprise Canonical/Reusable Model Design

Design canonical and reusable enterprise information models and semantic bindings under applicable information-design governance.

**Overview**

Design canonical and reusable enterprise information models and semantic bindings under applicable information-design governance. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Design canonical and reusable enterprise information models and semantic bindings under applicable information-design governance.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-enterprise-canonical-reusable-model-design |
| canonicalId | 90e9cd401ddf4c7690c978d8dd131413 |
| displayName | [CT3] Enterprise Canonical/Reusable Model Design |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseCanonicalReusableModelDesign |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseCanonicalReusableModelDesign |
| Snapshot publication reference | HCM-BEC-reviewed-enterprise-canonical-reusable-model-design |
| ArchiMate semantic ID | id-cd6d12693e255af7876c44caddfdbdb4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Enterprise Canonical/Reusable Model Design |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-DESIGN |

Parent: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-enterprise-canonical-reusable-model-design`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-enterprise-canonical-reusable-model-library-management"></a>

## Enterprise Canonical/Reusable Model Library Management

Manage the identifiable library of canonical and reusable enterprise model definitions and their governed revisions.

**Overview**

Manage the identifiable library of canonical and reusable enterprise model definitions and their governed revisions. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage the identifiable library of canonical and reusable enterprise model definitions and their governed revisions.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-enterprise-canonical-reusable-model-library-management |
| canonicalId | 76943b4b68644c4dac1368fce8bb20d9 |
| displayName | [CT3] Enterprise Canonical/Reusable Model Library Management |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseCanonicalReusableModelLibraryManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseCanonicalReusableModelLibraryManagement |
| Snapshot publication reference | HCM-BEC-reviewed-enterprise-canonical-reusable-model-library-management |
| ArchiMate semantic ID | id-a3005df7ce87583ea31e0c4a7c3be749 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Enterprise Canonical/Reusable Model Library Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-LIBRARY-MANAGEMENT |

Parent: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-enterprise-canonical-reusable-model-library-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-enterprise-canonical-reusable-model-utilisation-support"></a>

## Enterprise Canonical/Reusable Model Utilisation Support

Support correct utilisation and conformance of canonical and reusable models across their intended consumers.

**Overview**

Support correct utilisation and conformance of canonical and reusable models across their intended consumers. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Support correct utilisation and conformance of canonical and reusable models across their intended consumers.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-enterprise-canonical-reusable-model-utilisation-support |
| canonicalId | 5744cb9d86494064af87afb3cbf2cf97 |
| displayName | [CT3] Enterprise Canonical/Reusable Model Utilisation Support |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseCanonicalReusableModelUtilisationSupport |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseCanonicalReusableModelUtilisationSupport |
| Snapshot publication reference | HCM-BEC-reviewed-enterprise-canonical-reusable-model-utilisation-support |
| ArchiMate semantic ID | id-bb43c048f11e5d069f844bdb8c3a1ed9 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Enterprise Canonical/Reusable Model Utilisation Support |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-UTILISATION-SUPPORT |

Parent: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-enterprise-canonical-reusable-model-utilisation-support`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-enterprise-reference-data-stewardship"></a>

## Enterprise Reference Data Stewardship

Steward enterprise reference-data definitions, bindings and applicable governance independently of mutable runtime dataset administration.

**Overview**

Steward enterprise reference-data definitions, bindings and applicable governance independently of mutable runtime dataset administration. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Steward enterprise reference-data definitions, bindings and applicable governance independently of mutable runtime dataset administration.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-enterprise-reference-data-stewardship |
| canonicalId | 971d4b079ad24e89b70a8ed8dec0c8bf |
| displayName | [CT3] Enterprise Reference Data Stewardship |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseReferenceDataStewardship |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseReferenceDataStewardship |
| Snapshot publication reference | HCM-BEC-reviewed-enterprise-reference-data-stewardship |
| ArchiMate semantic ID | id-8cbd1321a0c15306b7355745d2e22818 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Enterprise Reference Data Stewardship |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-REFERENCE-DATA-STEWARDSHIP |

Parent: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-enterprise-reference-data-stewardship`) and the element's version-qualified Properties.

<a id="hcm-bec-patient-clinical-record"></a>

## Patient Clinical Record

Patient Clinical Record provides a governed longitudinal representation across encounters, providers and source systems. Assembly supplies the timeline, active access supports current care context, and durable preservation retains the history of what Harmonia received, knew or managed. These contributions are complementary: a responsive working view must not become the authority for durable history.

**Overview**

Patient Clinical Record provides a governed longitudinal representation across encounters, providers and source systems. Assembly supplies the timeline, active access supports current care context, and durable preservation retains the history of what Harmonia received, knew or managed. These contributions are complementary: a responsive working view must not become the authority for durable history.

**Intent**

Patient Clinical Record provides a governed longitudinal representation across encounters, providers and source systems. Assembly supplies the timeline, active access supports current care context, and durable preservation retains the history of what Harmonia received, knew or managed. These contributions are complementary: a responsive working view must not become the authority for durable history.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Intent — predecessor explanation

Make cross-institutional information usable over time without erasing source provenance or prior knowledge through correction.

### Boundary — predecessor explanation

Preservation does not originate clinical facts. Active distributed access and authoritative durable preservation remain separate under AX-05.

### Harmonia Architectural Relevance — predecessor explanation

Longitudinal assembly, active access and governed durable history are direct information-management responsibilities within the Core boundary.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:patient-clinical-record |
| canonicalId | 5478878e0e6e4d34a8d5023ac6ac2d54 |
| displayName | [CT2] Patient Clinical Record |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord |
| Snapshot publication reference | HCM-BEC-patient-clinical-record |
| ArchiMate semantic ID | id-974025114007505b922d37dc6ba0a04f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Patient Clinical Record |
| Reviewed name | Patient Clinical Record |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD"] |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Linked Information Curation](business-enabling-capabilities.md#hcm-bec-associated-clinical-binary-content-enablement) — Proposed; placement Proposed
- [Longitudinal Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-reviewed-longitudinal-clinical-record-administration) — Proposed; placement Proposed
- [Primary Care Coordination](business-enabling-capabilities.md#hcm-bec-primary-care) — Canonical; placement Proposed
- [Remote Patient Monitoring](business-enabling-capabilities.md#hcm-bec-remote-virtual-care) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:patient-clinical-record`) and the element's version-qualified Properties.

<a id="hcm-bec-associated-clinical-binary-content-enablement"></a>

## Linked Information Curation

Curate associations to information and binary content needed to interpret and use a clinical record, retaining custody and source-authority distinctions.

**Overview**

Curate associations to information and binary content needed to interpret and use a clinical record, retaining custody and source-authority distinctions.

**Intent**

Curate associations to information and binary content needed to interpret and use a clinical record, retaining custody and source-authority distinctions.

**Boundary**

Association is not physical custody. Persistence authority, source clinical meaning and external repository authority remain separate.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-associated-clinical-binary-content-enablement |
| canonicalId | 5edb2a8b5ee54ddda8104d1fab323a94 |
| displayName | [CT3] Linked Information Curation |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LinkedInformationCuration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LinkedInformationCuration |
| Snapshot publication reference | HCM-BEC-associated-clinical-binary-content-enablement |
| ArchiMate semantic ID | id-baf9aba95d5c539bab7fc75f6861f176 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Linked Information Curation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT"] |
| Name aliases | ["Associated Clinical Binary Content Enablement"] |

Parent: [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-associated-clinical-binary-content-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-longitudinal-clinical-record-administration"></a>

## Longitudinal Clinical Record Administration

Administer longitudinal record assembly, active clinical summaries and durable received/managed history with source attribution and supersession.

**Overview**

Administer longitudinal record assembly, active clinical summaries and durable received/managed history with source attribution and supersession. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer longitudinal record assembly, active clinical summaries and durable received/managed history with source attribution and supersession.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-longitudinal-clinical-record-administration |
| canonicalId | 14748dc6117f4b6ba7c0fc554b76bd89 |
| displayName | [CT3] Longitudinal Clinical Record Administration |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LongitudinalClinicalRecordAdministration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LongitudinalClinicalRecordAdministration |
| Snapshot publication reference | HCM-BEC-reviewed-longitudinal-clinical-record-administration |
| ArchiMate semantic ID | id-dbb8a6f722f756808d5022cbd06aec85 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Longitudinal Clinical Record Administration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-LONGITUDINAL-CLINICAL-RECORD-ADMINISTRATION |

Parent: [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-longitudinal-clinical-record-administration`) and the element's version-qualified Properties.

<a id="hcm-bec-primary-care"></a>

## Primary Care Coordination

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Primary Care enables information continuity between general practice and other care settings through shared-record ingestion and notifications of acute events. A GP's notes, summaries and immunisation records can contribute to the longitudinal view, while admission and discharge information can return to the registered practitioner. The enabling responsibility is the connection between contexts, rather than practice management or primary clinical care.

### Boundary — predecessor explanation

Receiving a summary or dispatching a notification does not perform consultation, clinical review or follow-up.

### Harmonia Architectural Relevance — predecessor explanation

These flows materially support continuity across primary and acute care without assigning general-practice authority to Harmonia.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:primary-care |
| canonicalId | f80e73ee91624ce5baa11ae15480fd35 |
| displayName | [CT3] Primary Care Coordination |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.PrimaryCareCoordination |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-PrimaryCareCoordination |
| Snapshot publication reference | HCM-BEC-primary-care |
| ArchiMate semantic ID | id-b9ab7bb6c4865419928c0c1f13bf45b7 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Primary Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-PRIMARY-CARE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PRIMARY-CARE"] |
| Name aliases | ["Primary Care"] |

Parent: [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:primary-care`) and the element's version-qualified Properties.

<a id="hcm-bec-remote-virtual-care"></a>

## Remote Patient Monitoring

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Remote & Virtual Care integrates remotely obtained observations and associates virtual consultations with their patient and episode context. It distinguishes a telemetry contribution from the encounter in which a practitioner may interpret it. The enabling responsibility concerns information continuity across delivery modes, preserving the setting-independent meaning of the Business care capabilities.

### Boundary — predecessor explanation

Telemetry acquisition is not clinical monitoring judgement or treatment; session association does not establish clinical adequacy.

### Harmonia Architectural Relevance — predecessor explanation

Remote data and virtual-session context materially extend information continuity across care locations and modes.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:remote--virtual-care |
| canonicalId | 2d1addc7dd1d4821b71a11c692d35b27 |
| displayName | [CT3] Remote Patient Monitoring |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.RemotePatientMonitoring |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-RemotePatientMonitoring |
| Snapshot publication reference | HCM-BEC-remote-virtual-care |
| ArchiMate semantic ID | id-cb13e2de68e55d8c9204e770611aef37 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Proposed |
| Canonical name | Remote & Virtual Care |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-REMOTE-VIRTUAL-CARE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-REMOTE-VIRTUAL-CARE"] |
| Name aliases | ["Remote & Virtual Care"] |

Parent: [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:remote--virtual-care`) and the element's version-qualified Properties.

<a id="hcm-bec-presentation-services"></a>

## Presentation Services

Presentation Services supplies contextual human interaction through clinical timelines, directory views and operational dashboards. It makes governed information usable by people while consuming the identity, security and clinical context established elsewhere. The responsibility is the interaction and presentation experience rather than a new authority for the information shown.

**Overview**

Presentation Services supplies contextual human interaction through clinical timelines, directory views and operational dashboards. It makes governed information usable by people while consuming the identity, security and clinical context established elsewhere. The responsibility is the interaction and presentation experience rather than a new authority for the information shown.

**Intent**

Presentation Services supplies contextual human interaction through clinical timelines, directory views and operational dashboards. It makes governed information usable by people while consuming the identity, security and clinical context established elsewhere. The responsibility is the interaction and presentation experience rather than a new authority for the information shown.

**Boundary**

Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

---

## History

### Boundary — predecessor explanation

Presentation does not own authentication, credential storage, consent decisions, EMPI reconciliation or authoritative clinical state. A displayed value is not made authoritative by its appearance in a dashboard.

### Harmonia Architectural Relevance — predecessor explanation

Decoupled contextual interaction is a substantive Core responsibility that preserves the established information-access and control boundaries.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:presentation-services |
| canonicalId | e9b6ba1917ba44cca7466444d0836c98 |
| displayName | [CT2] Presentation Services |
| friendlyName | SharedInformationAndCoordinationEnablement.PresentationServices |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PresentationServices |
| Snapshot publication reference | HCM-BEC-presentation-services |
| ArchiMate semantic ID | id-306525d358285c6cb525859aba8c7f7f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Presentation Services |
| Reviewed name | Presentation Services |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PRESENTATION-SERVICES |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-PRESENTATION-SERVICES"] |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Contextual Information Presentation](business-enabling-capabilities.md#hcm-bec-reviewed-contextual-information-presentation) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:presentation-services`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-contextual-information-presentation"></a>

## Contextual Information Presentation

Present governed longitudinal clinical and operational information in its applicable human interaction context.

**Overview**

Present governed longitudinal clinical and operational information in its applicable human interaction context. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Present governed longitudinal clinical and operational information in its applicable human interaction context.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-contextual-information-presentation |
| canonicalId | 9f900c53ae56488cad69e53c2205e898 |
| displayName | [CT3] Contextual Information Presentation |
| friendlyName | SharedInformationAndCoordinationEnablement.PresentationServices.ContextualInformationPresentation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PresentationServices.CT3-BEC-ContextualInformationPresentation |
| Snapshot publication reference | HCM-BEC-reviewed-contextual-information-presentation |
| ArchiMate semantic ID | id-dc26649d879b5c23813b59e7ada49e3d |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Contextual Information Presentation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PRESENTATION-SERVICES.CT3-BEC-CONTEXTUAL-INFORMATION-PRESENTATION |

Parent: [Presentation Services](business-enabling-capabilities.md#hcm-bec-presentation-services). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-contextual-information-presentation`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-workflow-activity-coordination"></a>

## Workflow & Activity Coordination

Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.

**Overview**

Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.

**Boundary**

External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-workflow-activity-coordination |
| canonicalId | c724f3d38bfa4567983a11ad22095d61 |
| displayName | [CT2] Workflow & Activity Coordination |
| friendlyName | SharedInformationAndCoordinationEnablement.WorkflowAndActivityCoordination |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-WorkflowAndActivityCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-workflow-activity-coordination |
| ArchiMate semantic ID | id-36d21ea7e98b5e1bbbddad25ba53cd4e |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Workflow & Activity Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-WORKFLOW-ACTIVITY-COORDINATION |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Activity Routing Management](business-enabling-capabilities.md#hcm-bec-reviewed-activity-routing-management) — Proposed; placement Proposed
- [External Task Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-external-task-coordination) — Proposed; placement Proposed
- [External Task Status & Traceability Management](business-enabling-capabilities.md#hcm-bec-reviewed-external-task-status-traceability-management) — Proposed; placement Proposed
- [Work Boundary Governance](business-enabling-capabilities.md#hcm-bec-reviewed-work-boundary-governance) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-workflow-activity-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-activity-routing-management"></a>

## Activity Routing Management

Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility.

**Overview**

Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-activity-routing-management |
| canonicalId | 27411c143e6e41ffba1af139fbbcd7e2 |
| displayName | [CT3] Activity Routing Management |
| friendlyName | SharedInformationAndCoordinationEnablement.WorkflowAndActivityCoordination.ActivityRoutingManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-WorkflowAndActivityCoordination.CT3-BEC-ActivityRoutingManagement |
| Snapshot publication reference | HCM-BEC-reviewed-activity-routing-management |
| ArchiMate semantic ID | id-96e9160ac5bc532faed556f17a714f99 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Activity Routing Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-WORKFLOW-ACTIVITY-COORDINATION.CT3-BEC-ACTIVITY-ROUTING-MANAGEMENT |

Parent: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-workflow-activity-coordination). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-activity-routing-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-external-task-coordination"></a>

## External Task Coordination

Coordinate work entering or leaving the Harmonia execution boundary with the system or actor responsible for undertaking and completing that work.

**Overview**

Coordinate work entering or leaving the Harmonia execution boundary with the system or actor responsible for undertaking and completing that work. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate work entering or leaving the Harmonia execution boundary with the system or actor responsible for undertaking and completing that work.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-external-task-coordination |
| canonicalId | ff3447c929724245a13ee864874b5561 |
| displayName | [CT3] External Task Coordination |
| friendlyName | SharedInformationAndCoordinationEnablement.WorkflowAndActivityCoordination.ExternalTaskCoordination |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-WorkflowAndActivityCoordination.CT3-BEC-ExternalTaskCoordination |
| Snapshot publication reference | HCM-BEC-reviewed-external-task-coordination |
| ArchiMate semantic ID | id-b42f6b68fe5a51959a491c7439d1b6c0 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | External Task Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-WORKFLOW-ACTIVITY-COORDINATION.CT3-BEC-EXTERNAL-TASK-COORDINATION |

Parent: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-workflow-activity-coordination). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-external-task-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-external-task-status-traceability-management"></a>

## External Task Status & Traceability Management

Manage the observable status and attributable traceability of work as it progresses through systems outside the Harmonia execution boundary.

**Overview**

Manage the observable status and attributable traceability of work as it progresses through systems outside the Harmonia execution boundary. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage the observable status and attributable traceability of work as it progresses through systems outside the Harmonia execution boundary.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-external-task-status-traceability-management |
| canonicalId | b25bb04a8aa94dcfac105a646878ccfc |
| displayName | [CT3] External Task Status & Traceability Management |
| friendlyName | SharedInformationAndCoordinationEnablement.WorkflowAndActivityCoordination.ExternalTaskStatusAndTraceabilityManagement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-WorkflowAndActivityCoordination.CT3-BEC-ExternalTaskStatusAndTraceabilityManagement |
| Snapshot publication reference | HCM-BEC-reviewed-external-task-status-traceability-management |
| ArchiMate semantic ID | id-44334b55ed7857dc9bea3f43ffb1d874 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | External Task Status & Traceability Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-WORKFLOW-ACTIVITY-COORDINATION.CT3-BEC-EXTERNAL-TASK-STATUS-TRACEABILITY-MANAGEMENT |

Parent: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-workflow-activity-coordination). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-external-task-status-traceability-management`) and the element's version-qualified Properties.

<a id="hcm-bec-reviewed-work-boundary-governance"></a>

## Work Boundary Governance

Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.

**Overview**

Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | reviewed:bec-work-boundary-governance |
| canonicalId | 35b66a0b38db4568849ecb842ffdcbf3 |
| displayName | [CT3] Work Boundary Governance |
| friendlyName | SharedInformationAndCoordinationEnablement.WorkflowAndActivityCoordination.WorkBoundaryGovernance |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-WorkflowAndActivityCoordination.CT3-BEC-WorkBoundaryGovernance |
| Snapshot publication reference | HCM-BEC-reviewed-work-boundary-governance |
| ArchiMate semantic ID | id-eed78c1571b356ff9a9222054e7feeb4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Reviewed name | Work Boundary Governance |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-WORKFLOW-ACTIVITY-COORDINATION.CT3-BEC-WORK-BOUNDARY-GOVERNANCE |

Parent: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-workflow-activity-coordination). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `reviewed:bec-work-boundary-governance`) and the element's version-qualified Properties.

<a id="hcm-bec-service-offering-readiness-coordination"></a>

## Service Offering Readiness Coordination

Assemble the dependency evidence needed to determine and communicate whether a defined service offering is operationally ready in a particular context.

**Overview**

Collective readiness depends on suitable workforce, spaces, equipment, materials and supporting services. This proposal evaluates their joint availability for an offering without treating one available bed, an operating-hours declaration or aggregate capacity as service readiness.

**Intent**

Distinguish collective service deliverability from nominal availability or readiness of any single dependency.

**Boundary**

Contributing capabilities retain source readiness and resource management. Clinical go/no-go decisions and service governance are not granted by this information-based coordination.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-service-offering-readiness-coordination |
| canonicalId | 0caecaba5d3e4781b8671b12c13ee283 |
| displayName | [CT3] Service Offering Readiness Coordination |
| friendlyName | HealthServiceOperationsEnablement.CareResourceAndCapacityCoordination.ServiceOfferingReadinessCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-CareResourceAndCapacityCoordination.CT3-BEC-ServiceOfferingReadinessCoordination |
| Snapshot publication reference | HCM-BEC-service-offering-readiness-coordination |
| ArchiMate semantic ID | id-f4b94cd1b3af514eb9e99ddfd82245ba |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-OFFERING-READINESS-COORDINATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: source proposal determines collective readiness; reviewed scope establishes dependency tracking only. Evidence may be reused, but determination authority is not silently transferred. |

Parent: [Care Resource & Capacity Coordination](business-enabling-capabilities.md#hcm-bec-care-resource-capacity-coordination). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-service-offering-readiness-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-credential-management"></a>

## Credential Management

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Credential Management concerns operational badges and physical-access credentials used in authorised operational participation. It retains a distinct Domain03 responsibility even though Strategy has no matching catalogue entry. This prevents the word credential from merging physical-access context with the professional qualifications and competency evidence checked by Clinical Qualification Management.

### Boundary — predecessor explanation

No Strategy correspondence or Feature association is inferred from name similarity. Operational credentials do not themselves establish professional clinical qualification or privilege.

### Harmonia Architectural Relevance — predecessor explanation

Relevance remains Unresolved. Established operational behaviour provides context for possible enablement but does not authorise a formal classification.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:credential-management |
| canonicalId | 3a7a4d2d0ce8446b8a6293284fb16b83 |
| displayName | [CT3] Credential Management |
| friendlyName | HealthServiceOperationsEnablement.WorkforceQualificationAndDevelopmentEnablement.CredentialManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkforceQualificationAndDevelopmentEnablement.CT3-BEC-CredentialManagement |
| Snapshot publication reference | HCM-BEC-credential-management |
| ArchiMate semantic ID | id-7a05f7ea632d5ed6b2f2ff63e590988f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Credential Management |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT.CT3-BEC-CREDENTIAL-MANAGEMENT |
| Unresolved dimensions | ["strategy_catalogue_correspondence","relevance"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical operational badges/physical-access credentials are distinct from reviewed clinical-service credential definitions. |

Parent: [Workforce Qualification & Development Enablement](business-enabling-capabilities.md#hcm-bec-workforce-qualification-development-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:credential-management`) and the element's version-qualified Properties.

<a id="hcm-bec-entity-specific-operational-coordination"></a>

## Entity-Specific Operational Coordination

Coordinate governed operational progression relative to one represented entity, including interested behaviour participation and controlled recovery.

**Overview**

Activity concerning the same patient, practitioner or bed needs a coherent entity-relative frame. Interest fan-out, serial progression, fresh information context and uncertain effects remain distinguishable from generic multi-work-unit progression.

**Intent**

Keep the progression of one represented entity coherent across interested operational behaviours.

**Boundary**

Entity coordination does not own all entity information, determine clinical work or become the source of clinical decisions. No Twin component allocation follows from this proposal.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-entity-specific-operational-coordination |
| canonicalId | 9f0c1429b64f4f88982fbbda6eaec233 |
| displayName | [CT3] Entity-Specific Operational Coordination |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.EntitySpecificOperationalCoordination |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-EntitySpecificOperationalCoordination |
| Snapshot publication reference | HCM-BEC-entity-specific-operational-coordination |
| ArchiMate semantic ID | id-90f67ae80f4e599eb3a922a46aa46139 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-ENTITY-SPECIFIC-OPERATIONAL-COORDINATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Internal entity-relative progression is not the reviewed external work bridge; a current BEC destination has not been adjudicated. |

Parent: [Operational Activity & Temporal Enablement](business-enabling-capabilities.md#hcm-bec-operational-activity-temporal-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-entity-specific-operational-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-governed-operational-activity-design"></a>

## Governed Operational Activity Design

Define the information context, expected outcomes, continuation conditions and housekeeping of governed operational activity.

**Overview**

An activity needs a fulfilment definition even when only one bounded execution unit is involved. This capability specifies its semantic envelope while execution and event delivery remain separate responsibilities.

**Intent**

Make the expected information and fulfilment basis of an undertaking explicit before it executes.

**Boundary**

An activity definition is neither an orchestrator nor a choreographer. It does not choose clinical care or dynamically invent execution logic.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-governed-operational-activity-design |
| canonicalId | 3a96040821c14fe38830411dab4f8453 |
| displayName | [CT3] Governed Operational Activity Design |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.GovernedOperationalActivityDesign |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-GovernedOperationalActivityDesign |
| Snapshot publication reference | HCM-BEC-governed-operational-activity-design |
| ArchiMate semantic ID | id-e285e2523d1f5fe488af1b4735d4a49b |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-GOVERNED-OPERATIONAL-ACTIVITY-DESIGN |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Internal execution-envelope design is distinct from governance of work across an external execution boundary. |

Parent: [Operational Activity & Temporal Enablement](business-enabling-capabilities.md#hcm-bec-operational-activity-temporal-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-governed-operational-activity-design`) and the element's version-qualified Properties.

<a id="hcm-bec-workflow-activity-coordination"></a>

## Workflow & Activity Coordination

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Workflow & Activity Coordination provides governed progression for three distinct work meanings: human operational Work Orders, human review or decision To Dos, and non-human Synthetic Tasks. It supports dispatch, state and deadline oversight without collapsing those meanings into a generic technical task. The common coordination responsibility makes progression visible while preserving who has authority to perform or decide the underlying activity.

### Intent — predecessor explanation

Keep assigned activity, acknowledgement, progress and outcome distinguishable across human and automated work.

### Boundary — predecessor explanation

Coordination does not confer clinical work ownership. Synthetic Tasks are not external clinical worklists, and the incomplete stalled/failed Process paths remain unresolved.

### Harmonia Architectural Relevance — predecessor explanation

Governed activity progression is a substantive Core responsibility; information traceability preserves archetype, work and undertaking distinctions without transferring outcome authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:workflow--activity-coordination |
| canonicalId | 94c683c7588f475e973b58cb226345db |
| displayName | [CT3] Workflow & Activity Coordination |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination |
| Snapshot publication reference | HCM-BEC-workflow-activity-coordination |
| ArchiMate semantic ID | id-fe75271f0234500982b272bc2ac9a9b9 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Workflow & Activity Coordination |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Parent: [Operational Activity & Temporal Enablement](business-enabling-capabilities.md#hcm-bec-operational-activity-temporal-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:workflow--activity-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-independent-assurance-evaluation-enablement"></a>

## Independent Assurance Evaluation Enablement

Associate trustworthy evidence, assess it and adjudicate independently reportable findings about governed service behaviour.

**Overview**

Associate trustworthy evidence, assess it and adjudicate independently reportable findings about governed service behaviour. Its constituent abilities distinguish Governed Assurance.

**Intent**

Produce findings supported by applicable evidence and criteria while retaining inability to conclude.

**Boundary**

The subject can supply evidence but cannot control its conclusion. Evidence insufficiency remains distinct from execution uncertainty and clinical judgement.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-independent-assurance-evaluation-enablement |
| canonicalId | 7fa5112efb724b90a17a03c7c19c516a |
| displayName | [CT2] Independent Assurance Evaluation Enablement |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement |
| Snapshot publication reference | HCM-BEC-independent-assurance-evaluation-enablement |
| ArchiMate semantic ID | id-fe7ae69ddcf154af96952f3194f71c33 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: The previous grouping frames Canonical subject-assurance execution; it cannot be equated with review of the assurance service itself. |

Parent: [Independent Assurance Enablement](business-enabling-capabilities.md#hcm-bec-independent-service-assurance-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-independent-assurance-evaluation-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-governed-assurance"></a>

## Governed Assurance

The capability to independently evaluate a governed subject against applicable assurance criteria through the assembly and evaluation of sufficient trustworthy evidence, establishing explicit findings, exceptions and assurance conclusions.

---

## History

### Capability Overview — predecessor explanation

Governed Assurance independently evaluates a subject through applicable criteria and trustworthy evidence, establishing explicit findings and conclusions. Context establishment associates source-owned information with a particular assurance purpose; assessment evaluates it; adjudication establishes the supported outcome. Insufficient evidence remains a meaningful limit rather than a forced positive or negative result.

The capability can manage its own assurance activity without managing the subject. Request, progression status and established outcome are different client concerns; the requester does not thereby control the evaluation or gain unrestricted status access.

### Boundary — predecessor explanation

No remediation, assignment or operational escalation of the subject is acquired. Financial governance is deliberately excluded; assurance does not recursively assure its own execution, and no component allocation is established.

### Harmonia Architectural Relevance — predecessor explanation

Formal classification remains Unresolved. The approved independent-assurance responsibility and contribution model establish material purpose without resolving that metadata dimension.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:governed-assurance |
| canonicalId | da1a983d4dcf4d5bb772b8f69bc08426 |
| displayName | [CT3] Governed Assurance |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement.GovernedAssurance |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement.CT3-BEC-GovernedAssurance |
| Snapshot publication reference | HCM-BEC-governed-assurance |
| ArchiMate semantic ID | id-14dccd7d85915b23b805d452c4bb35e2 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Governed Assurance |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE |
| Unresolved dimensions | ["feature_decomposition","relevance"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself. |

Parent: [Independent Assurance Evaluation Enablement](business-enabling-capabilities.md#hcm-bec-independent-assurance-evaluation-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:governed-assurance`) and the element's version-qualified Properties.

<a id="hcm-bec-information-authority-representation"></a>

## Information Authority Representation

Represent the source, credibility and qualified authority standing of competing health-information assertions.

**Overview**

A persisted assertion may be authoritative, informational or anecdotal. This ability carries that standing into interpretation without treating transport reliability, freshness or successful commit as proof of credibility.

**Intent**

Keep an assertion technical acceptance separate from the standing of the source that asserted it.

**Boundary**

Classification does not originate clinical facts or universally adjudicate conflicts. Policy determines how qualified assertions are used.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-information-authority-representation |
| canonicalId | 8c850faafb334f97a81a0b4cd8fdb65f |
| displayName | [CT3] Information Authority Representation |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalInformationStewardshipEnablement.InformationAuthorityRepresentation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalInformationStewardshipEnablement.CT3-BEC-InformationAuthorityRepresentation |
| Snapshot publication reference | HCM-BEC-information-authority-representation |
| ArchiMate semantic ID | id-b78f67bb04765785bb46be9e9c5427d3 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-INFORMATION-AUTHORITY-REPRESENTATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Source credibility and authority standing require an explicit home; information-asset tags alone do not establish that responsibility. |

Parent: [Clinical Information Stewardship Enablement](business-enabling-capabilities.md#hcm-bec-clinical-information-stewardship-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-information-authority-representation`) and the element's version-qualified Properties.

<a id="hcm-bec-health-information-communication"></a>

## Health Information Communication

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Health Information Communication supplies standards-based transmission mediation between participating parties. It addresses protocol-facing transaction coordination and technical error handling while the business interaction retains its own meaning and authority. This differentiates the communication mechanism from the exchange model and from the clinical or operational purpose of the message.

### Boundary — predecessor explanation

Transport mediation does not own every communicated interaction or payload. External contracts must preserve standards semantics and exclude private operational metadata.

### Harmonia Architectural Relevance — predecessor explanation

Standards-conformant communication boundaries are a direct interoperability responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:health-information-communication |
| canonicalId | 9794b2aff43443b495a19aefe0efa67b |
| displayName | [CT3] Health Information Communication |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationConnectivityEnablement.HealthInformationCommunication |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationConnectivityEnablement.CT3-BEC-HealthInformationCommunication |
| Snapshot publication reference | HCM-BEC-health-information-communication |
| ArchiMate semantic ID | id-09019f6c980e57d1a11691eb4bf9f8eb |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Health Information Communication |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-COMMUNICATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical boundary protocol mediation and technical transmission are distinct from reviewed patient-facing communication. |

Parent: [Health Information Connectivity Enablement](business-enabling-capabilities.md#hcm-bec-health-information-connectivity-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:health-information-communication`) and the element's version-qualified Properties.

<a id="hcm-bec-governance-requirement-publication-support"></a>

## Governance Requirement Publication Support

Represent and publish versioned governing requirements for use by authorised operational and assurance consumers.

**Overview**

Consumers need an identifiable statement of which rule applies and who established it. This proposal fills BC-17 information support without giving the platform authority to make legislation, approve governance requirements or reinterpret their force.

**Intent**

Let operational and assurance consumers identify the governing statement and applicability basis they actually used.

**Boundary**

Governance approval remains externally mandated; operational policy, assurance criteria and requirement authority are not interchangeable.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-governance-requirement-publication-support |
| canonicalId | 7e780751d50e4ffe942f8443b7cb6567 |
| displayName | [CT3] Governance Requirement Publication Support |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationProtectionAndAccountabilityEnablement.GovernanceRequirementPublicationSupport |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationProtectionAndAccountabilityEnablement.CT3-BEC-GovernanceRequirementPublicationSupport |
| Snapshot publication reference | HCM-BEC-governance-requirement-publication-support |
| ArchiMate semantic ID | id-43399be21121568aa6b26f5b0c8414c4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-GOVERNANCE-REQUIREMENT-PUBLICATION-SUPPORT |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Requirement publication/authority is not assurance-service review or information-model design; the reviewed hierarchy does not allocate a home. |

Parent: [Information Protection & Accountability Enablement](business-enabling-capabilities.md#hcm-bec-information-protection-accountability-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-governance-requirement-publication-support`) and the element's version-qualified Properties.

<a id="hcm-bec-significant-activity-audit-capture"></a>

## Significant Activity Audit Capture

Capture required security- and information-significant activity as governed, attributable audit evidence.

**Overview**

Mandatory evidence capture has its own policy-governed platform authority and must remain possible when the audited identity is denied an action. The captured fact is distinguished from debug output and the identity whose activity is recorded.

**Intent**

Ensure required significant activity evidence can be captured under the authority specifically assigned to that responsibility.

**Boundary**

The specific intrinsic authority for mandatory audit capture is not generalised to other activities. Capture is not an assurance finding and cannot expose unmasked PHI in diagnostic logs.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-significant-activity-audit-capture |
| canonicalId | d903948169eb4785b0f03d916c842a8a |
| displayName | [CT3] Significant Activity Audit Capture |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationProtectionAndAccountabilityEnablement.SignificantActivityAuditCapture |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationProtectionAndAccountabilityEnablement.CT3-BEC-SignificantActivityAuditCapture |
| Snapshot publication reference | HCM-BEC-significant-activity-audit-capture |
| ArchiMate semantic ID | id-24a125b569e45888bf313cd39d435fba |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-SIGNIFICANT-ACTIVITY-AUDIT-CAPTURE |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: The reviewed hierarchy does not allocate general mandatory authoritative audit capture; disclosure or provenance alone is not an equivalent responsibility. |

Parent: [Information Protection & Accountability Enablement](business-enabling-capabilities.md#hcm-bec-information-protection-accountability-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-significant-activity-audit-capture`) and the element's version-qualified Properties.

<a id="hcm-bec-care-access-transaction-enablement"></a>

## Care Access Transaction Enablement

Coordinate the intake, care-request disposition and appointment information through which a person accesses care.

**Overview**

Coordinate the intake, care-request disposition and appointment information through which a person accesses care. Its constituent abilities distinguish Client-Centred Administration; Referral Administration; Scheduling Administration.

**Intent**

Make an access request and its disposition traceable without confusing a booking with care responsibility or delivery.

**Boundary**

A referral requests care responsibility; an appointment concerns a booking. Neither establishes care delivery or host booking authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-access-transaction-enablement |
| canonicalId | b65973b576154d3a96cf458bf33df53a |
| displayName | [CT2] Care Access Transaction Enablement |
| friendlyName | CareAdministrationEnablement.CareAccessTransactionEnablement |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CareAccessTransactionEnablement |
| Snapshot publication reference | HCM-BEC-care-access-transaction-enablement |
| ArchiMate semantic ID | id-957fa549536c5a02b16c2b8d7fed3ad4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-access-transaction-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-care-documentation-communication-enablement"></a>

## Care Documentation & Communication Enablement

Administer the lifecycle, shared-plan context and directed communication of care documentation.

**Overview**

Administer the lifecycle, shared-plan context and directed communication of care documentation. Its constituent abilities distinguish Clinical Record Administration; Care Plan Administration; Clinical Communication Administration.

**Intent**

Preserve which document or plan applies and what a directed recipient has actually acknowledged.

**Boundary**

This frame does not absorb longitudinal synthesis, collaboration discourse, originating clinical decisions or generic transport.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-documentation-communication-enablement |
| canonicalId | 5ad2e381ec484d1d9062a65eee842fa7 |
| displayName | [CT2] Care Documentation & Communication Enablement |
| friendlyName | CareAdministrationEnablement.CareDocumentationAndCommunicationEnablement |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CareDocumentationAndCommunicationEnablement |
| Snapshot publication reference | HCM-BEC-care-documentation-communication-enablement |
| ArchiMate semantic ID | id-ddefa589dee05b94ab470e11f3b0540c |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-documentation-communication-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-care-financial-information-enablement"></a>

## Care Financial Information Enablement

Represent coverage context and convey care-activity evidence to responsible funding and charging systems.

**Overview**

Represent coverage context and convey care-activity evidence to responsible funding and charging systems. Its constituent abilities distinguish Funding & Coverage Administration; Clinical Charging & Claims Administration.

**Intent**

Give responsible financial systems the care-activity and coverage context they need without creating another financial authority.

**Boundary**

Adjacent information participation does not provide financial governance, coverage adjudication or a billing platform.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-financial-information-enablement |
| canonicalId | 59a756b89c604e3e97ee5dede2d6ed89 |
| displayName | [CT2] Care Financial Information Enablement |
| friendlyName | CareAdministrationEnablement.CareFinancialInformationEnablement |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CareFinancialInformationEnablement |
| Snapshot publication reference | HCM-BEC-care-financial-information-enablement |
| ArchiMate semantic ID | id-4ddd19075297571c9aeee1382d935a72 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FINANCIAL-INFORMATION-ENABLEMENT |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-financial-information-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-care-fulfilment-administration-enablement"></a>

## Care Fulfilment Administration Enablement

Maintain distinguishable care-episode, bounded fulfilment and delivered-activity information across performing systems.

**Overview**

Maintain distinguishable care-episode, bounded fulfilment and delivered-activity information across performing systems. Its constituent abilities distinguish Episode & Encounter Administration; Order Administration; Diagnostic Administration; Medication Administration; Procedure Administration.

**Intent**

Maintain the link between a bounded care request, the relevant episode and the externally observed fulfilment outcome.

**Boundary**

Encounter progression, order outcome, diagnostic interpretation, medication events and procedure documentation retain different source authorities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-fulfilment-administration-enablement |
| canonicalId | 9f0f4379c1954524ba5e691d0c7d95ab |
| displayName | [CT2] Care Fulfilment Administration Enablement |
| friendlyName | CareAdministrationEnablement.CareFulfilmentAdministrationEnablement |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CareFulfilmentAdministrationEnablement |
| Snapshot publication reference | HCM-BEC-care-fulfilment-administration-enablement |
| ArchiMate semantic ID | id-0ac4ff89dcdc5e7c951d67f06c504dd8 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT |

Parent: [Care Administration Enablement](business-enabling-capabilities.md#hcm-bec-care-administration-information-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-fulfilment-administration-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-care-context-integration-enablement"></a>

## Care Context Integration Enablement

Compose governed information and coordination behaviour for materially different care settings, continuity arrangements and specialist clinical purposes.

**Overview**

Emergency presentation, inpatient rounds and outreach visits need different contextual assemblies of the same underlying information. This frame retains those established setting-specific responsibilities as compositions rather than duplicate records or clinical execution authorities.

**Intent**

Give a care participant the appropriate information context for the particular integration purpose without duplicating general information responsibilities.

**Boundary**

Setting, care mode and specialty are overlapping contextual dimensions. These children are integration responsibilities, not a mutually exclusive taxonomy of patients or clinical care.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-context-integration-enablement |
| canonicalId | a84a7e96e7874a4b87d18c53b1b5cf29 |
| displayName | [CT1] Care Context Integration Enablement |
| friendlyName | CareContextIntegrationEnablement |
| canonicalName | CT1-BEC-CareContextIntegrationEnablement |
| Snapshot publication reference | HCM-BEC-care-context-integration-enablement |
| ArchiMate semantic ID | id-994a0770b7fd5726a603f696f3dc4d1c |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT |

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-context-integration-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-facility-episodic-care-integration"></a>

## Facility & Episodic Care Integration

Provide the context assemblies and boundary integration needed for facility presentations and bounded care episodes.

**Overview**

Provide the context assemblies and boundary integration needed for facility presentations and bounded care episodes. Its constituent abilities distinguish Acute Care; Emergency Care; Inpatient Care; Ambulatory Care; Surgical & Procedural Care.

**Intent**

Make presentation- and episode-specific information available to the responsible care team in its applicable context.

**Boundary**

Acute, emergency and inpatient perspectives overlap; each capability owns its named integration purpose rather than a population or clinical workflow.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-facility-episodic-care-integration |
| canonicalId | c1eb24ed7e36417cb426288ff57512c0 |
| displayName | [CT2] Facility & Episodic Care Integration |
| friendlyName | CareContextIntegrationEnablement.FacilityAndEpisodicCareIntegration |
| canonicalName | CT1-BEC-CareContextIntegrationEnablement.CT2-BEC-FacilityAndEpisodicCareIntegration |
| Snapshot publication reference | HCM-BEC-facility-episodic-care-integration |
| ArchiMate semantic ID | id-10e11a4f3fb85b80be5c8970415e8125 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION |

Parent: [Care Context Integration Enablement](business-enabling-capabilities.md#hcm-bec-care-context-integration-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-facility-episodic-care-integration`) and the element's version-qualified Properties.

<a id="hcm-bec-longitudinal-community-care-integration"></a>

## Longitudinal & Community Care Integration

Support continuity, remote participation and preventive follow-up through appropriately assembled and exchanged care context.

**Overview**

Support continuity, remote participation and preventive follow-up through appropriately assembled and exchanged care context. Its constituent abilities distinguish Primary Care; Community Care; Outreach Care; Remote & Virtual Care; Preventive Care.

**Intent**

Support continuity when care information must follow the person across providers, visits and distance.

**Boundary**

Longitudinal purpose does not make these care contexts exclusive. Outreach location and remote mode remain distinct from source clinical responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-longitudinal-community-care-integration |
| canonicalId | 8430d50adc504ecc959f15b7cbbcd095 |
| displayName | [CT2] Longitudinal & Community Care Integration |
| friendlyName | CareContextIntegrationEnablement.LongitudinalAndCommunityCareIntegration |
| canonicalName | CT1-BEC-CareContextIntegrationEnablement.CT2-BEC-LongitudinalAndCommunityCareIntegration |
| Snapshot publication reference | HCM-BEC-longitudinal-community-care-integration |
| ArchiMate semantic ID | id-6a2eb8694bb1540b9d1f64051ee43dc3 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION |

Parent: [Care Context Integration Enablement](business-enabling-capabilities.md#hcm-bec-care-context-integration-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-longitudinal-community-care-integration`) and the element's version-qualified Properties.

<a id="hcm-bec-specialist-clinical-context-integration"></a>

## Specialist Clinical Context Integration

Integrate specialist assessment, investigation and therapeutic information with longitudinal and episode context.

**Overview**

Integrate specialist assessment, investigation and therapeutic information with longitudinal and episode context. Its constituent abilities distinguish Clinical Assessment; Diagnostic Services; Therapeutic Services; Medication Therapy; Allied Health; Rehabilitation.

**Intent**

Keep specialised clinical context connected to the wider record while retaining the specialist source of clinical judgement.

**Boundary**

A clinical specialty is evidence for integration purpose, not authority to invent a specialist engine, clinical decision or duplicated general capture function.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-specialist-clinical-context-integration |
| canonicalId | 062802a425d14530b0ce9434c5980d85 |
| displayName | [CT2] Specialist Clinical Context Integration |
| friendlyName | CareContextIntegrationEnablement.SpecialistClinicalContextIntegration |
| canonicalName | CT1-BEC-CareContextIntegrationEnablement.CT2-BEC-SpecialistClinicalContextIntegration |
| Snapshot publication reference | HCM-BEC-specialist-clinical-context-integration |
| ArchiMate semantic ID | id-54d9ddfc96fc54b2bf9e1617c856f538 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION |

Parent: [Care Context Integration Enablement](business-enabling-capabilities.md#hcm-bec-care-context-integration-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-specialist-clinical-context-integration`) and the element's version-qualified Properties.

<a id="hcm-bec-device-association"></a>

## Device Association

Associating clinical devices with patients, beds, or rooms.

**Overview**

A device can be associated with different patients and care places at different times. Qualified association and its lineage allow observations to be interpreted against the applicable binding rather than the current assignment alone.

**Intent**

Allow a device-originated item to be associated with the correct subject and place at the relevant time.

**Boundary**

Represents qualified device-to-subject/place association without owning those subjects or observations.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-device-association |
| canonicalId | 902daf2ce9714876bbfee52eef1a7ab0 |
| displayName | [CT3] Device Association |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceAssociation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceAssociation |
| Snapshot publication reference | HCM-BEC-device-association |
| ArchiMate semantic ID | id-1cfea61cbb7a5750b32bdf93debae923 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-ASSOCIATION |

Parent: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-device-association`) and the element's version-qualified Properties.

<a id="hcm-bec-device-connectivity"></a>

## Device Connectivity

Registering communication protocols and network endpoints for medical devices.

**Overview**

A device communication binding identifies the protocol and endpoint through which its feed can participate. Connectivity metadata supports routing without owning authentication issuance, payload semantics or physiological interpretation.

**Intent**

Make a device feed reachable through its registered communication binding.

**Boundary**

Represents communication bindings; it does not acquire authentication credential issuance or telemetry interpretation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-device-connectivity |
| canonicalId | 658d4b6770a943bcb5801fb7ca9b47be |
| displayName | [CT3] Device Connectivity |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceConnectivity |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceConnectivity |
| Snapshot publication reference | HCM-BEC-device-connectivity |
| ArchiMate semantic ID | id-e0d0c691e440506bbcfd8980ffdbc1ea |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-CONNECTIVITY |

Parent: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-device-connectivity`) and the element's version-qualified Properties.

<a id="hcm-bec-device-instance"></a>

## Device Instance

Tracking physical device hardware instances and serial numbers.

**Overview**

An identifiable physical device carries UDI and serial context through its use. The instance record supports association and resolution without originating device observations or making maintenance decisions.

**Intent**

Keep the identity of the actual physical equipment distinguishable from its model and endpoint.

**Boundary**

Identifies physical hardware; serial tracking is distinct from maintenance and live observation content.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-device-instance |
| canonicalId | dd860926038f41e082c56e38cbb5ab95 |
| displayName | [CT3] Device Instance |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceInstance |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceInstance |
| Snapshot publication reference | HCM-BEC-device-instance |
| ArchiMate semantic ID | id-73a3caf14181524195989816b0e12246 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-INSTANCE |

Parent: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-device-instance`) and the element's version-qualified Properties.

<a id="hcm-bec-service-availability-definition"></a>

## Service Availability Definition

Modeling regular operating hours, exceptional closures, and emergency availability.

**Overview**

Regular hours, on-call windows and exceptional closures describe when an offering is nominally available. They provide an input to readiness and scheduling without proving that all required dependencies are ready.

**Intent**

Express the operating windows against which a service participation request can be considered.

**Boundary**

Represents operating windows and exceptions; it does not establish staffing, capacity or readiness.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-service-availability-definition |
| canonicalId | 9e5329a42f964199b4b0a87ab1cacc2e |
| displayName | [CT3] Service Availability Definition |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.ServiceAvailabilityDefinition |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-ServiceAvailabilityDefinition |
| Snapshot publication reference | HCM-BEC-service-availability-definition |
| ArchiMate semantic ID | id-fe6ea1fbce775529bcc7e3f9a41c2305 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-SERVICE-AVAILABILITY-DEFINITION |

Parent: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-service-availability-definition`) and the element's version-qualified Properties.

<a id="hcm-bec-service-eligibility-definition"></a>

## Service Eligibility Definition

Capturing referral criteria, catchment boundaries, and clinical eligibility.

**Overview**

Published catchment, referral and clinical eligibility conditions explain the intended audience and access constraints of an offering. Their representation supports responsible assessment without deciding acceptance or clinical priority.

**Intent**

Make the governed intake and catchment conditions available to the party responsible for access decisions.

**Boundary**

Represents governed eligibility criteria; it does not make clinical acceptance or triage decisions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-service-eligibility-definition |
| canonicalId | 8a3f29e5948141e2a3d0c72539759daf |
| displayName | [CT3] Service Eligibility Definition |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.ServiceEligibilityDefinition |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-ServiceEligibilityDefinition |
| Snapshot publication reference | HCM-BEC-service-eligibility-definition |
| ArchiMate semantic ID | id-36a039fd097b5360a1ab9dcad34f9a97 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-SERVICE-ELIGIBILITY-DEFINITION |

Parent: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-service-eligibility-definition`) and the element's version-qualified Properties.

<a id="hcm-bec-service-provision-relationship"></a>

## Service Provision Relationship

Binding health services to specific organizations, locations, and practitioner roles.

**Overview**

Service provision joins a named offering to the organisations, places and practitioner roles through which it is provided. The mapping is the owned concern; the identity and meaning of each mapped participant remain with its established capability.

**Intent**

Make it clear which provider delivers which service at which place and through which participating roles.

**Boundary**

Owns mapping associations, retaining the owners of each participating entity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-service-provision-relationship |
| canonicalId | 718ebe1fa8d44ad9a6632a5932277a8e |
| displayName | [CT3] Service Provision Relationship |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.ServiceProvisionRelationship |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-ServiceProvisionRelationship |
| Snapshot publication reference | HCM-BEC-service-provision-relationship |
| ArchiMate semantic ID | id-3006d0eeedfd5899af44611e8fbda5b4 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-SERVICE-PROVISION-RELATIONSHIP |

Parent: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-service-provision-relationship`) and the element's version-qualified Properties.

<a id="hcm-bec-location-structure"></a>

## Location Structure

Modeling the physical architecture of healthcare campuses.

**Overview**

Campus, building, floor, ward and room relationships explain physical containment. The structure provides navigation and context without determining organisational reporting lines or current operational capacity.

**Intent**

Allow a particular place to be interpreted within its physical surroundings.

**Boundary**

Physical containment differs from organisational structure and operational responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-location-structure |
| canonicalId | 2436c87195334daf85ed4e2de4ec4bd5 |
| displayName | [CT3] Location Structure |
| friendlyName | EntityInformationEnablement.LocationAdministration.LocationStructure |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.CT3-BEC-LocationStructure |
| Snapshot publication reference | HCM-BEC-location-structure |
| ArchiMate semantic ID | id-8a82a7c44c7d573786cf4d00508d9941 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.CT3-BEC-LOCATION-STRUCTURE |

Parent: [Location Administration](business-enabling-capabilities.md#hcm-bec-location-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-location-structure`) and the element's version-qualified Properties.

<a id="hcm-bec-organisation-contact"></a>

## Organisation Contact

Maintaining verified administrative and clinical communication channels.

**Overview**

Verified facility contact channels make an organisation reachable for appropriate administrative and clinical communication. The contact record says how it may be reached; recipient qualification and disclosure policy govern a particular use.

**Intent**

Provide verified channels for reaching the intended healthcare organisation.

**Boundary**

Contact validity is distinct from clinical recipient eligibility and policy permission.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-organisation-contact |
| canonicalId | b7f5108880eb429bb407c87a37b0e23f |
| displayName | [CT3] Organisation Contact |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.OrganisationContact |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-OrganisationContact |
| Snapshot publication reference | HCM-BEC-organisation-contact |
| ArchiMate semantic ID | id-7d47194b90fa59628cd4b1a830f1b111 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-ORGANISATION-CONTACT |

Parent: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-organisation-contact`) and the element's version-qualified Properties.

<a id="hcm-bec-organisational-structure"></a>

## Organisational Structure

Managing parent-child hierarchies, departments, and corporate relationships.

**Overview**

Departments, divisions and regional networks need qualified structural relationships that can be traversed and understood over time. Structural membership is distinct from physical location and from authority to manage another organisation.

**Intent**

Make organisational relationships navigable without confusing structure with ownership or physical containment.

**Boundary**

Structural relationships do not acquire ownership of every related organisation or service.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-organisational-structure |
| canonicalId | 19d211cf601c49e5a65665330d0c5c3d |
| displayName | [CT3] Organisational Structure |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.OrganisationalStructure |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-OrganisationalStructure |
| Snapshot publication reference | HCM-BEC-organisational-structure |
| ArchiMate semantic ID | id-975bd55fc43e538faf4920f7bace43f6 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-ORGANISATIONAL-STRUCTURE |

Parent: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-organisational-structure`) and the element's version-qualified Properties.

<a id="hcm-bec-provider-engagement"></a>

## Provider Engagement

Capturing terms of service and clinical privilege agreements.

**Overview**

Engagement information represents the terms and externally granted practice privileges under which a provider participates. This differs from who the practitioner is and from whether a particular operation is authorised now.

**Intent**

Make the externally established terms and privilege standing of provider participation available without granting it.

**Boundary**

Records externally granted service terms and privileges; it does not independently grant those privileges.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-provider-engagement |
| canonicalId | a0777dbd9b0c4528817e99a7f34aa444 |
| displayName | [CT3] Provider Engagement |
| friendlyName | EntityInformationEnablement.ProviderAdministration.ProviderEngagement |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-ProviderEngagement |
| Snapshot publication reference | HCM-BEC-provider-engagement |
| ArchiMate semantic ID | id-30b102fdb0fd5f788ddf1812fe82689d |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PROVIDER-ENGAGEMENT |

Parent: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-provider-engagement`) and the element's version-qualified Properties.

<a id="hcm-bec-provider-relationship"></a>

## Provider Relationship

Managing organizational affiliations and multidisciplinary team relationships.

**Overview**

Effective affiliations explain which practitioner or team participates with which organisation. These relationships support collaboration and routing while each participating identity keeps its own responsibility owner.

**Intent**

Preserve which affiliations apply when interpreting provider participation.

**Boundary**

Affiliation records do not transfer the participating organisations or practitioner identities to this capability.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-provider-relationship |
| canonicalId | 3bb8a3d5b62a4b779442290fe8d5106c |
| displayName | [CT3] Provider Relationship |
| friendlyName | EntityInformationEnablement.ProviderAdministration.ProviderRelationship |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-ProviderRelationship |
| Snapshot publication reference | HCM-BEC-provider-relationship |
| ArchiMate semantic ID | id-706567fdfba05959be6ac7247ad0480e |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PROVIDER-RELATIONSHIP |

Parent: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-provider-relationship`) and the element's version-qualified Properties.

<a id="hcm-bec-care-movement-transition-coordination"></a>

## Care Movement & Transition Coordination

Coordinate movement of people/materials and operational departure readiness across care boundaries.

**Overview**

Coordinate movement of people/materials and operational departure readiness across care boundaries. Its constituent abilities distinguish Patient Transport; Clinical Logistics Coordination; Discharge Management.

**Intent**

Coordinate the operational prerequisites and observed milestones of movement and departure without assuming clinical clearance.

**Boundary**

Physical movement, clinical handover, discharge clinical authorisation and care-responsibility transition remain distinct.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-movement-transition-coordination |
| canonicalId | 59b9331fa3c04ef09bf963ad8a5d0d71 |
| displayName | [CT2] Care Movement & Transition Coordination |
| friendlyName | HealthServiceOperationsEnablement.CareMovementAndTransitionCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-CareMovementAndTransitionCoordination |
| Snapshot publication reference | HCM-BEC-care-movement-transition-coordination |
| ArchiMate semantic ID | id-f460240046915af4a9f9a081c5593b1c |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-movement-transition-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-care-resource-capacity-coordination"></a>

## Care Resource & Capacity Coordination

Relate operational care-place and equipment state to the coordinated view of service demand and capacity.

**Overview**

Relate operational care-place and equipment state to the coordinated view of service demand and capacity. Its constituent abilities distinguish Bed & Care-Place Management; Clinical Resource Management; Service Capacity Management.

**Intent**

Make resource-state contributions and coordinated capacity views usable without collapsing their different responsibilities.

**Boundary**

Bed/equipment availability is a contribution to capacity, not its definition. Higher-order capacity consumes local capacity without taking its ownership.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-resource-capacity-coordination |
| canonicalId | 96f71cb8fa6844d7aa7e9f16759a3a62 |
| displayName | [CT2] Care Resource & Capacity Coordination |
| friendlyName | HealthServiceOperationsEnablement.CareResourceAndCapacityCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-CareResourceAndCapacityCoordination |
| Snapshot publication reference | HCM-BEC-care-resource-capacity-coordination |
| ArchiMate semantic ID | id-2f2df4a3cea854849a3a985fa2b47996 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-resource-capacity-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-resource-management"></a>

## Clinical Resource Management

Definition unresolved in the baseline. No definition has been synthesised from the name.

---

## History

### Capability Overview — predecessor explanation

Clinical Resource Management integrates location and allocation information for shared clinical equipment. It makes the current operational resource context available rather than treating device inventory as proof that equipment can be used for a particular need. The responsibility supports coordination while retaining the distinction between resource state and a service's capacity.

### Boundary — predecessor explanation

Equipment tracking does not acquire enterprise asset ownership, maintenance responsibility or clinical authority to use it.

### Harmonia Architectural Relevance — predecessor explanation

Dynamic equipment context materially supports operational coordination across care environments.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | capability:clinical-resource-management |
| canonicalId | a5faedcd97064c28b9e7e551e9106ea1 |
| displayName | [CT3] Clinical Resource Management |
| friendlyName | HealthServiceOperationsEnablement.CareResourceAndCapacityCoordination.ClinicalResourceManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-CareResourceAndCapacityCoordination.CT3-BEC-ClinicalResourceManagement |
| Snapshot publication reference | HCM-BEC-clinical-resource-management |
| ArchiMate semantic ID | id-b44a1b08f3ee550381f617d0f5bed38d |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT3 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Resource Management |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-CLINICAL-RESOURCE-MANAGEMENT |

Parent: [Care Resource & Capacity Coordination](business-enabling-capabilities.md#hcm-bec-care-resource-capacity-coordination). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `capability:clinical-resource-management`) and the element's version-qualified Properties.

<a id="hcm-bec-care-setting-operations-enablement"></a>

## Care Setting Operations Enablement

Coordinate the local flow, session state and capacity semantics of a care-delivery setting.

**Overview**

Coordinate the local flow, session state and capacity semantics of a care-delivery setting. Its constituent abilities distinguish Clinic & Practice Operations; Ward Operations; Theatre Operations; Emergency Department Operations; Outpatient Operations.

**Intent**

Express the actual operating constraints of each setting rather than substituting a generic resource count for capacity.

**Boundary**

Local capacity is established here; aggregate telemetry, bed state and equipment state cannot substitute for it.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-care-setting-operations-enablement |
| canonicalId | a03741e6e98642bea44680d23fdcb434 |
| displayName | [CT2] Care Setting Operations Enablement |
| friendlyName | HealthServiceOperationsEnablement.CareSettingOperationsEnablement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-CareSettingOperationsEnablement |
| Snapshot publication reference | HCM-BEC-care-setting-operations-enablement |
| ArchiMate semantic ID | id-0aca1a5cceb9581a85904af01a3591e3 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-care-setting-operations-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-operational-workforce-coordination"></a>

## Operational Workforce Coordination

Make workforce availability, coverage and eligible operational work participation usable in coordination.

**Overview**

Make workforce availability, coverage and eligible operational work participation usable in coordination. Its constituent abilities distinguish Clinical Workforce Management; Clinical Rostering; Mobile Staff Management; On-Call Management; Work Allocation & Dispatch.

**Intent**

Support eligible operational participation using qualified availability and coverage information.

**Boundary**

Source rosters remain separately authoritative; operational dispatch does not acquire clinical-work allocation authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-operational-workforce-coordination |
| canonicalId | 66e72222f2064a23adbdb0311092918b |
| displayName | [CT2] Operational Workforce Coordination |
| friendlyName | HealthServiceOperationsEnablement.OperationalWorkforceCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OperationalWorkforceCoordination |
| Snapshot publication reference | HCM-BEC-operational-workforce-coordination |
| ArchiMate semantic ID | id-67d1729bf7565773bc62694f3cdc1957 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-operational-workforce-coordination`) and the element's version-qualified Properties.

<a id="hcm-bec-workforce-qualification-development-enablement"></a>

## Workforce Qualification & Development Enablement

Represent learning, competence, professional-standing checks and separately governed operational access qualifications.

**Overview**

Represent learning, competence, professional-standing checks and separately governed operational access qualifications. Its constituent abilities distinguish Clinical Training & Education; Clinical Competency Management; Clinical Qualification Management; Clinical Supervision; Credential Management.

**Intent**

Keep professional standing, competence, supervision and operational-access evidence distinguishable when work is considered.

**Boundary**

Clinical qualifications and physical badges remain distinct CT3 responsibilities. Representation or checking does not grant privilege or become an authentication provider.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-workforce-qualification-development-enablement |
| canonicalId | d24a1f4c4f674c45a95c317998ff4186 |
| displayName | [CT2] Workforce Qualification & Development Enablement |
| friendlyName | HealthServiceOperationsEnablement.WorkforceQualificationAndDevelopmentEnablement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkforceQualificationAndDevelopmentEnablement |
| Snapshot publication reference | HCM-BEC-workforce-qualification-development-enablement |
| ArchiMate semantic ID | id-0be98fb67d425ecab44e20c991d19a01 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT |

Parent: [Health Service Operations Enablement](business-enabling-capabilities.md#hcm-bec-care-operations-coordination-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-workforce-qualification-development-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-human-collaboration-work-enablement"></a>

## Human Collaboration & Work Enablement

Enable contextual human interaction, collaboration and the definition and progression of governed operational activity.

**Overview**

People discuss information, inspect it, make authorised decisions and undertake work. Those interactions need reusable coordination and temporal context, while entity-relative progression and activity design retain distinct semantics from message exchange and screen rendering.

**Intent**

Allow people and automated participants to interact and progress authorised work with intelligible context and accountability.

**Boundary**

Conversation is not a clinical record, presentation is not authority, and generic work coordination does not determine clinical work or own its outcome.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-human-collaboration-work-enablement |
| canonicalId | c9347f73b21242c5b8cce41252b74da0 |
| displayName | [CT1] Human Collaboration & Work Enablement |
| friendlyName | HumanCollaborationAndWorkEnablement |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement |
| Snapshot publication reference | HCM-BEC-human-collaboration-work-enablement |
| ArchiMate semantic ID | id-0bac088d5c4358b99269265e43c4d89e |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT |

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-human-collaboration-work-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-collaborative-interaction-enablement"></a>

## Collaborative Interaction Enablement

Support contextual discussion and human interaction with governed information and activity.

**Overview**

Support contextual discussion and human interaction with governed information and activity. Its constituent abilities distinguish Clinical Collaboration; General Collaboration; Presentation Services.

**Intent**

Give participants a useful discussion and interaction context without turning the communication medium into a clinical authority.

**Boundary**

Collaboration control, collaboration operations and general presentation remain distinct; a discussion or screen is not a new clinical fact.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-collaborative-interaction-enablement |
| canonicalId | 6140b21c874241d092708e2411caceed |
| displayName | [CT2] Collaborative Interaction Enablement |
| friendlyName | HumanCollaborationAndWorkEnablement.CollaborativeInteractionEnablement |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-CollaborativeInteractionEnablement |
| Snapshot publication reference | HCM-BEC-collaborative-interaction-enablement |
| ArchiMate semantic ID | id-cbc054c958a05feda3487a42caa57375 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT |

Parent: [Human Collaboration & Work Enablement](business-enabling-capabilities.md#hcm-bec-human-collaboration-work-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-collaborative-interaction-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-operational-activity-temporal-enablement"></a>

## Operational Activity & Temporal Enablement

Progress governed operational work in identifiable activity and temporal contexts.

**Overview**

Progress governed operational work in identifiable activity and temporal contexts. Its constituent abilities distinguish Workflow & Activity Coordination; Calendar Management.

**Intent**

Keep work progression and its timing intelligible across human and synthetic undertakings.

**Boundary**

Work Order is human doing, To Do is human review/update/approval and Task is synthetic work. Projection does not own authoritative bookings or rosters.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-operational-activity-temporal-enablement |
| canonicalId | ca96b2b2ff434854995711e403e95f6f |
| displayName | [CT2] Operational Activity & Temporal Enablement |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement |
| Snapshot publication reference | HCM-BEC-operational-activity-temporal-enablement |
| ArchiMate semantic ID | id-4e4a405d0abf537089c1bcabb41669d2 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT |

Parent: [Human Collaboration & Work Enablement](business-enabling-capabilities.md#hcm-bec-human-collaboration-work-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-operational-activity-temporal-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-clinical-information-stewardship-enablement"></a>

## Clinical Information Stewardship Enablement

Maintain governed longitudinal synthesis and the associations needed to use its constituent clinical information safely.

**Overview**

Maintain governed longitudinal synthesis and the associations needed to use its constituent clinical information safely. Its constituent abilities distinguish Patient Clinical Record.

**Intent**

Make the integrated clinical account usable while keeping its source assertions and associated content attributable.

**Boundary**

The synthesis preserves what Harmonia received or managed without becoming the originator of the clinical facts.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-clinical-information-stewardship-enablement |
| canonicalId | 3811c758ff2140c39a801bf89f75098a |
| displayName | [CT2] Clinical Information Stewardship Enablement |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalInformationStewardshipEnablement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalInformationStewardshipEnablement |
| Snapshot publication reference | HCM-BEC-clinical-information-stewardship-enablement |
| ArchiMate semantic ID | id-1b407e2fabba5fd2a45c73692a95d79b |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-clinical-information-stewardship-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-health-information-connectivity-enablement"></a>

## Health Information Connectivity Enablement

Make governed health information discoverable and exchangeable under explicit internal and external contracts.

**Overview**

Make governed health information discoverable and exchangeable under explicit internal and external contracts. Its constituent abilities distinguish Health Information Exchange; Health Information Access; Health Information Communication.

**Intent**

Let authorised participants find and exchange the information their contract permits.

**Boundary**

Query execution, transaction coordination and boundary protocol mediation are separate responsibilities; none transfers content ownership.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-health-information-connectivity-enablement |
| canonicalId | 6a1030483dfc4ae6af6998b587129d1c |
| displayName | [CT2] Health Information Connectivity Enablement |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationConnectivityEnablement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationConnectivityEnablement |
| Snapshot publication reference | HCM-BEC-health-information-connectivity-enablement |
| ArchiMate semantic ID | id-10a8917a66475db1b276741d0efd840f |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-health-information-connectivity-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-information-protection-accountability-enablement"></a>

## Information Protection & Accountability Enablement

Apply information-use policy and preserve the accountability context needed for governed health-information activity.

**Overview**

Apply information-use policy and preserve the accountability context needed for governed health-information activity. Its constituent abilities distinguish Health Information Control.

**Intent**

Make information use both governed and explainable through policy, attributable rules and significant evidence.

**Boundary**

Patient directives remain with Client Privacy; runtime authorisation does not become external authentication or clinical authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-information-protection-accountability-enablement |
| canonicalId | e922cba7cf1d4ebeb9abf6472a3803ce |
| displayName | [CT2] Information Protection & Accountability Enablement |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationProtectionAndAccountabilityEnablement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationProtectionAndAccountabilityEnablement |
| Snapshot publication reference | HCM-BEC-information-protection-accountability-enablement |
| ArchiMate semantic ID | id-c72a309296ec5da68e55dc8ba1f8fcb7 |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-information-protection-accountability-enablement`) and the element's version-qualified Properties.

<a id="hcm-bec-semantic-reference-knowledge-enablement"></a>

## Semantic & Reference Knowledge Enablement

Govern the definitions, terminology and reference content that make healthcare information consistently interpretable.

**Overview**

Govern the definitions, terminology and reference content that make healthcare information consistently interpretable. Its constituent abilities distinguish Clinical Knowledge Services; Information Design Governance.

**Intent**

Keep structural meaning, terminology bindings and approved reference content consistent enough for shared interpretation.

**Boundary**

Definition semantics, mutable approved content and runtime policy decisions retain distinct ownership.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:bec-semantic-reference-knowledge-enablement |
| canonicalId | 92a71292710c41b29775d19b98c197d6 |
| displayName | [CT2] Semantic & Reference Knowledge Enablement |
| friendlyName | SharedInformationAndCoordinationEnablement.SemanticAndReferenceKnowledgeEnablement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-SemanticAndReferenceKnowledgeEnablement |
| Snapshot publication reference | HCM-BEC-semantic-reference-knowledge-enablement |
| ArchiMate semantic ID | id-bfc2580cd5a15519b21c0118c610d8ca |
| Legacy identity evidence | No legacy allocation |
| Construct | capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | superseded |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT |

Parent: [Shared Information & Coordination Enablement](business-enabling-capabilities.md#hcm-bec-governed-health-information-enablement). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:bec-semantic-reference-knowledge-enablement`) and the element's version-qualified Properties.

------- Legacy Content - Superseded ------- Finish ----
