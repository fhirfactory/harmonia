<!-- continuum-source-succession -->
------- Legacy Content - Superseded ------- Start ------
Superseded as an active architectural carrier by the [canonical element continuum](../../../../enterprise-architecture/documentation/continuum-manifest/published/1.1.5/README.md).
[Exact source/identity correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json) records this historical generated carrier. Its original bytes and then-current claims are retained below; it is no longer a current authoring source.
---------------------------------------------------------

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Baseline — Features

[Baseline authority and reading guide](README.md). Currentness does not imply approval. Definitions are reproduced without semantic rewriting. IDs below retain their distinct roles.

## Catalogue

- [Care Goal & Plan Progression Representation](#hcm-ft-care-goal-plan-progression-representation) — Proposed; current; placement Proposed
- [Care Plan Ingestion & Distribution](#feat-sa-26) — Canonical; current; placement Proposed
- [Community Care Plan Synchronization](#feat-sd-16) — Canonical; current; placement Proposed
- [Community Encounter Ingestion](#feat-sd-15) — Canonical; current; placement Proposed
- [Outreach Encounter Outcome Ingestion](#feat-sd-18) — Canonical; current; placement Proposed
- [Outreach Visit Context Provision](#feat-sd-17) — Canonical; current; placement Proposed
- [Demographic Change Distribution](#feat-sa-02) — Canonical; current; placement Proposed
- [Demographic Update Ingestion](#feat-sa-01) — Canonical; current; placement Proposed
- [Immunisation Event Submission](#feat-sd-13) — Canonical; current; placement Proposed
- [Screening Recall Notification Distribution](#feat-sd-14) — Canonical; current; placement Proposed
- [Clinical Communication Distribution](#feat-sa-27) — Canonical; current; placement Proposed
- [Communication Audit Logging](#feat-sa-29) — Canonical; current; placement Proposed
- [Delivery Acknowledgement Tracking](#feat-sa-28) — Canonical; current; placement Proposed
- [Clinical Document Ingestion](#feat-sa-23) — Canonical; current; placement Proposed
- [Document Metadata Indexing](#feat-sa-25) — Canonical; current; placement Proposed
- [Document Versioning & Supersession](#feat-sa-24) — Canonical; current; placement Proposed
- [Diagnostic History Consolidation](#feat-sd-10) — Canonical; current; placement Proposed
- [Laboratory & Imaging Result Distribution](#feat-sd-09) — Canonical; current; placement Proposed
- [Diagnostic Preliminary & Corrected Report Handling](#hcm-ft-diagnostic-preliminary-corrected-report-handling) — Proposed; current; placement Proposed
- [Diagnostic Report Distribution](#feat-sa-17) — Canonical; current; placement Proposed
- [Diagnostic Report Ingestion & Binding](#feat-sa-16) — Canonical; current; placement Proposed
- [Diagnostic Request Correlation](#feat-sa-15) — Canonical; current; placement Proposed
- [Acute Care Clinical Context Provision](#feat-sd-04) — Canonical; current; placement Proposed
- [Acute Clinical State Event Capture](#feat-sd-03) — Canonical; current; placement Proposed
- [Encounter Bed/Location Association](#feat-sa-10) — Canonical; current; placement Proposed
- [Encounter Context Creation](#feat-sa-08) — Canonical; current; placement Proposed
- [Encounter History Retrieval](#hcm-ft-encounter-history-retrieval) — Proposed; current; placement Proposed
- [Encounter State Progression](#feat-sa-09) — Canonical; current; placement Proposed
- [Dispense Event Tracking](#feat-sa-19) — Canonical; current; placement Proposed
- [Medication Administration Record (MAR) Ingestion](#feat-sa-20) — Canonical; current; placement Proposed
- [Medication Order Ingestion](#feat-sa-18) — Canonical; current; placement Proposed
- [Medication Administration Tracking](#feat-sd-12) — Canonical; current; placement Proposed
- [Medication History Reconciliation Ingestion](#feat-sd-11) — Canonical; current; placement Proposed
- [Order Cancellation & Modification Coordination](#feat-sa-14) — Canonical; current; placement Proposed
- [Order Closed-Loop Progression Tracking](#feat-sa-13) — Canonical; current; placement Proposed
- [Order Destination Resolution & Routing](#feat-sa-12) — Canonical; current; placement Proposed
- [Order Outcome Association](#hcm-ft-order-outcome-association) — Proposed; current; placement Proposed
- [Order Request Ingestion](#feat-sa-11) — Canonical; current; placement Proposed
- [Procedural Documentation Ingestion](#feat-sa-22) — Canonical; current; placement Proposed
- [Procedure Booking Ingestion](#feat-sa-21) — Canonical; current; placement Proposed
- [Referral Destination Coordination](#hcm-ft-referral-destination-coordination) — Proposed; current; placement Proposed
- [Referral Document Ingestion](#feat-sa-03) — Canonical; current; placement Proposed
- [Referral Status & Outcome Tracking](#feat-sa-05) — Canonical; current; placement Proposed
- [Referral Supporting Information](#feat-sa-04) — Canonical; current; placement Proposed
- [Appointment Notification Ingestion](#feat-sa-06) — Canonical; current; placement Proposed
- [Appointment Status Synchronization](#feat-sa-07) — Canonical; current; placement Proposed
- [Confidentiality Flag Enforcement](#feat-em-10) — Canonical; current; placement Proposed
- [Consent Directive Evaluation](#feat-em-09) — Canonical; current; placement Proposed
- [Next-of-Kin & Guardian Association](#feat-em-07) — Canonical; current; placement Proposed
- [Relationship Validity Tracking](#feat-em-08) — Canonical; current; placement Proposed
- [Demographic Context Governance](#feat-em-06) — Canonical; current; placement Proposed
- [Subject Context Binding](#feat-em-05) — Canonical; current; placement Proposed
- [Cross-Authority Identifier Correlation](#feat-em-02) — Canonical; current; placement Proposed
- [Governed Identity Correction](#feat-em-04) — Canonical; current; placement Proposed
- [Identifier Resolution](#feat-em-01) — Canonical; current; placement Proposed
- [Identity Alias Association](#feat-em-03) — Canonical; current; placement Proposed
- [Identity Source & Correction Lineage](#hcm-ft-identity-source-correction-lineage) — Proposed; current; placement Proposed
- [Device Serial Tracking](#feat-em-29) — Canonical; current; placement Proposed
- [Device Type Specification](#feat-em-28) — Canonical; current; placement Proposed
- [Device Endpoint Registration](#feat-em-31) — Canonical; current; placement Proposed
- [Device Association Lineage](#hcm-ft-device-association-lineage) — Proposed; current; placement Proposed
- [Device-Patient/Location Assignment](#feat-em-30) — Canonical; current; placement Proposed
- [Mobile Clinical Equipment Tracking](#feat-hso-14) — Canonical; current; placement Proposed
- [Eligibility & Catchment Specification](#feat-em-27) — Canonical; current; placement Proposed
- [Operating Schedule Specification](#feat-em-26) — Canonical; current; placement Proposed
- [Service Catalogue Publishing](#feat-em-24) — Canonical; current; placement Proposed
- [Service-Location-Provider Binding](#feat-em-25) — Canonical; current; placement Proposed
- [Atomic Care-Place Modeling](#feat-em-23) — Canonical; current; placement Proposed
- [Location Geospatial & Address Resolution](#feat-em-21) — Canonical; current; placement Proposed
- [Physical Hierarchy Modeling](#feat-em-22) — Canonical; current; placement Proposed
- [Directory Contact Management](#feat-em-20) — Canonical; current; placement Proposed
- [Hierarchical Structure Navigation](#feat-em-19) — Canonical; current; placement Proposed
- [National Organisation Verification](#feat-em-17) — Canonical; current; placement Proposed
- [Organisation Profile Management](#feat-em-18) — Canonical; current; placement Proposed
- [National Practitioner Verification](#feat-em-11) — Canonical; current; placement Proposed
- [Practitioner Profile Governance](#feat-em-12) — Canonical; current; placement Proposed
- [Provider Directory Discovery & Resolution](#hcm-ft-provider-directory-discovery-resolution) — Proposed; current; placement Proposed
- [Electronic Endpoint Binding](#feat-em-14) — Canonical; current; placement Proposed
- [Practitioner-Organisation Affiliation](#feat-em-15) — Canonical; current; placement Proposed
- [Role & Specialty Modeling](#feat-em-13) — Canonical; current; placement Proposed
- [Bed Availability Tracking](#feat-hso-11) — Canonical; current; placement Proposed
- [Bed Readiness Notification](#feat-hso-13) — Canonical; current; placement Proposed
- [Bed State Progression](#feat-hso-12) — Canonical; current; placement Proposed
- [Clinic Session Capacity Management](#hcm-ft-clinic-session-capacity-management) — Proposed; current; placement Proposed
- [Clinic Session Tracking](#feat-hso-01) — Canonical; current; placement Proposed
- [Operational Queue Progression](#feat-hso-02) — Canonical; current; placement Proposed
- [Pathology Specimen Transport Tracking](#feat-hso-26) — Canonical; current; placement Proposed
- [Clinical Privileging Record](#feat-em-16) — Canonical; current; placement Proposed
- [Operational Credential Check](#feat-hso-23) — Canonical; current; placement Proposed
- [Discharge Milestone Tracking](#feat-hso-27) — Canonical; current; placement Proposed
- [Discharge Notification Dispatch](#feat-hso-29) — Canonical; current; placement Proposed
- [Discharge Readiness & Coordination Oversight](#feat-hso-28) — Canonical; current; placement Proposed
- [Emergency Care Cross-Facility Correlation](#feat-sd-06) — Canonical; current; placement Proposed
- [Emergency Department Arrival & Triage Ingestion](#feat-sd-05) — Canonical; current; placement Proposed
- [ED Bed & Bay Tracking](#feat-hso-07) — Canonical; current; placement Proposed
- [ED Length of Stay Tracking](#feat-hso-08) — Canonical; current; placement Proposed
- [Emergency Treatment Capacity Management](#hcm-ft-emergency-treatment-capacity-management) — Proposed; current; placement Proposed
- [Mobile Service Capacity Management](#hcm-ft-mobile-service-capacity-management) — Proposed; current; placement Proposed
- [Mobile Visit Status Tracking](#feat-hso-17) — Canonical; current; placement Proposed
- [Mobile Worker Task Dispatch](#feat-hso-16) — Canonical; current; placement Proposed
- [Active On-Call Provider Resolution](#feat-hso-18) — Canonical; current; placement Proposed
- [Outpatient Appointment Capacity Management](#hcm-ft-outpatient-appointment-capacity-management) — Proposed; current; placement Proposed
- [Outpatient Arrival Registration](#feat-hso-09) — Canonical; current; placement Proposed
- [Post-Clinic Order Coordination](#feat-hso-10) — Canonical; current; placement Proposed
- [Transport Dispatch & Progress Tracking](#feat-hso-25) — Canonical; current; placement Proposed
- [Transport Request Ingestion](#feat-hso-24) — Canonical; current; placement Proposed
- [Operational Capacity Metric Aggregation](#feat-hso-15) — Canonical; current; placement Proposed
- [Operating Theatre Case Progression](#feat-hso-05) — Canonical; current; placement Proposed
- [Perioperative Resource Notification](#feat-hso-06) — Canonical; current; placement Proposed
- [Theatre Session Capacity Management](#hcm-ft-theatre-session-capacity-management) — Proposed; current; placement Proposed
- [Inpatient Clinical Progression Tracking](#feat-sd-08) — Canonical; current; placement Proposed
- [Inpatient Rounding Context Provision](#feat-sd-07) — Canonical; current; placement Proposed
- [Clinical Handover Context Assembly](#feat-hso-04) — Canonical; current; placement Proposed
- [Ward Care Capacity Management](#hcm-ft-ward-care-capacity-management) — Proposed; current; placement Proposed
- [Ward Patient Flow Coordination](#feat-hso-03) — Canonical; current; placement Proposed
- [Work Dispatch Delivery](#feat-hso-21) — Canonical; current; placement Proposed
- [Work Item Instantiation](#feat-hso-19) — Canonical; current; placement Proposed
- [Work Progress Oversight & Escalation](#feat-hso-22) — Canonical; current; placement Proposed
- [Worker Matching & Allocation](#feat-hso-20) — Canonical; current; placement Proposed
- [Temporal Assurance Criteria Stewardship](#hcm-ft-temporal-assurance-criteria-stewardship) — Proposed; current; placement Proposed
- [Assurance Disposition & Design Modelling](#hcm-ft-assurance-disposition-design-modelling) — Proposed; current; placement Proposed
- [Schedule Projection Integration](#feat-ise-24) — Canonical; current; placement Proposed
- [Temporal Event Correlation](#feat-ise-25) — Canonical; current; placement Proposed
- [Care-Team Clinical Collaboration Coordination](#feat-ise-16) — Canonical; current; placement Proposed
- [Collaboration Interaction Capture Qualification](#hcm-ft-collaboration-interaction-capture-qualification) — Proposed; current; placement Proposed
- [In-Conversation LHR Query Resolution](#feat-ise-17) — Canonical; current; placement Proposed
- [Zero-PHI Collaboration Metadata Governance](#feat-ise-18) — Canonical; current; placement Proposed
- [Bidirectional Terminology Mapping](#feat-ise-15) — Canonical; current; placement Proposed
- [Clinical Concept Resolution](#feat-ise-14) — Canonical; current; placement Proposed
- [Governed Dataset Content Revision](#hcm-ft-governed-dataset-content-revision) — Proposed; current; placement Proposed
- [Operational Team Chat Enablement](#feat-ise-19) — Canonical; current; placement Proposed
- [Access-Controlled Information Filtering](#feat-ise-09) — Canonical; current; placement Proposed
- [Federated Clinical Query](#feat-ise-08) — Canonical; current; placement Proposed
- [Health Information Access & Consent Control](#feat-ise-11) — Canonical; current; placement Proposed
- [Addressed Clinical Distribution](#feat-ise-06) — Canonical; current; placement Proposed
- [Clinical Record Retrieval Mediation](#feat-ise-05) — Canonical; current; placement Proposed
- [Direct Submission Ingestion & Delivery](#feat-ise-04) — Canonical; current; placement Proposed
- [Event-Driven Syndication](#feat-ise-07) — Canonical; current; placement Proposed
- [Information Transformation Lineage](#hcm-ft-information-transformation-lineage) — Proposed; current; placement Proposed
- [Authorised Research Release Assembly](#hcm-ft-authorised-research-release-assembly) — Proposed; current; placement Proposed
- [Demand & Capacity Planning Projection](#hcm-ft-demand-capacity-planning-projection) — Proposed; current; placement Proposed
- [Population Notification Obligation Tracking](#hcm-ft-population-notification-obligation-tracking) — Proposed; current; placement Proposed
- [Exchange Profile Governance](#hcm-ft-exchange-profile-governance) — Proposed; current; placement Proposed
- [Information Definition & Schema Governance](#hcm-ft-information-definition-schema-governance) — Proposed; current; placement Proposed
- [Information Standards & Semantic Governance](#feat-ise-27) — Canonical; current; placement Proposed
- [Terminology Binding Governance](#hcm-ft-terminology-binding-governance) — Proposed; current; placement Proposed
- [Semantic Conformance Verification](#feat-ise-28) — Canonical; current; placement Proposed
- [Binary Content Resolution & Assembly](#hcm-ft-binary-content-resolution-assembly) — Proposed; current; placement Proposed
- [Active Clinical Record Access](#feat-ise-02) — Canonical; current; placement Proposed
- [Clinical Information Historical Reconstruction](#hcm-ft-clinical-information-historical-reconstruction) — Proposed; current; placement Proposed
- [Durable Clinical Record Preservation](#feat-ise-03) — Canonical; current; placement Proposed
- [Longitudinal Health Record (LHR) Assembly](#feat-ise-01) — Canonical; current; placement Proposed
- [General Practice Shared Record Ingestion](#feat-sd-01) — Canonical; current; placement Proposed
- [Primary Care Notification Dispatch](#feat-sd-02) — Canonical; current; placement Proposed
- [Remote Telemetry Ingestion](#feat-sd-19) — Canonical; current; placement Proposed
- [Virtual Care Session Context Binding](#feat-sd-20) — Canonical; current; placement Proposed
- [Longitudinal Clinical & Operational Presentation](#feat-ise-26) — Canonical; current; placement Proposed
- [Service Readiness Dependency Assessment](#hcm-ft-service-readiness-dependency-assessment) — Proposed; pending_adjudication; placement Proposed
- [Entity Behaviour Failure & Recovery Governance](#hcm-ft-entity-behaviour-failure-recovery-governance) — Proposed; pending_adjudication; placement Proposed
- [Entity-Relative Activity Coordination](#hcm-ft-entity-relative-activity-coordination) — Proposed; pending_adjudication; placement Proposed
- [Activity Outcome & Continuation Rules](#hcm-ft-activity-outcome-continuation-rules) — Proposed; pending_adjudication; placement Proposed
- [Operational Activity Context Definition](#hcm-ft-operational-activity-context-definition) — Proposed; pending_adjudication; placement Proposed
- [Controlled Administrative Recovery](#hcm-ft-controlled-administrative-recovery) — Proposed; pending_adjudication; placement Proposed
- [Synthetic Task Orchestration](#feat-ise-22) — Canonical; pending_adjudication; placement Proposed
- [To Do Decision & Approval Coordination](#feat-ise-21) — Canonical; pending_adjudication; placement Proposed
- [Uncertain Activity Outcome Reconciliation](#hcm-ft-uncertain-activity-outcome-reconciliation) — Proposed; pending_adjudication; placement Proposed
- [Work Order Progression](#feat-ise-20) — Canonical; pending_adjudication; placement Proposed
- [Workflow Timeout & Escalation Oversight](#feat-ise-23) — Canonical; pending_adjudication; placement Proposed
- [Independent Evidence Assessment & Adjudication](#hcm-ft-independent-evidence-assessment-adjudication) — Proposed; pending_adjudication; placement Proposed
- [Information Authority & Source Standing Representation](#hcm-ft-information-authority-source-standing-representation) — Proposed; pending_adjudication; placement Proposed
- [Standards-Based Boundary Exchange](#feat-ise-10) — Canonical; pending_adjudication; placement Proposed
- [Governed Policy Assignment & Revocation](#hcm-ft-governed-policy-assignment-revocation) — Proposed; pending_adjudication; placement Proposed
- [Non-PHI Compliance Auditing](#feat-ise-13) — Canonical; pending_adjudication; placement Proposed
- [Operation-Bound Authorisation Enforcement](#hcm-ft-operation-bound-authorisation-enforcement) — Proposed; pending_adjudication; placement Proposed
- [Security Context Propagation](#feat-ise-12) — Canonical; pending_adjudication; placement Proposed
- [Trusted Identity Context Intake](#hcm-ft-trusted-identity-context-intake) — Proposed; pending_adjudication; placement Proposed
- [Governance Requirement Version Publication](#hcm-ft-governance-requirement-version-publication) — Proposed; pending_adjudication; placement Proposed
- [Mandatory Significant Activity Audit Capture](#hcm-ft-mandatory-significant-activity-audit-capture) — Proposed; pending_adjudication; placement Proposed

<a id="hcm-ft-care-goal-plan-progression-representation"></a>

## Care Goal & Plan Progression Representation

Represent externally agreed care goals, participants and plan changes with their effective standing.

**Overview**

Plan ingestion/distribution is complemented by the ability to distinguish the applicable goal and plan context as a shared plan progresses.

**Boundary**

Representing a goal does not choose treatment, determine clinical completion or grant plan approval authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-care-goal-plan-progression-representation |
| canonicalId | 7332de92e6c14bc8b071cf18f78f9525 |
| displayName | [Feature] Care Goal & Plan Progression Representation |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CarePlanLifecycleAndCoordination.CareGoalAndPlanProgressionRepresentation |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CarePlanLifecycleAndCoordination.FEAT-CareGoalAndPlanProgressionRepresentation |
| Snapshot publication reference | HCM-FT-care-goal-plan-progression-representation |
| ArchiMate semantic ID | id-3a7f53d246f6577c904c5980bdc3cb81 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-CARE-PLAN-LIFECYCLE-COORDINATION.FEAT-STEP2-CARE-GOAL-PLAN-PROGRESSION-REPRESENTATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CARE-PLAN-ADMINISTRATION.FEAT-STEP2-CARE-GOAL-PLAN-PROGRESSION-REPRESENTATION"] |

Responsibility/framing context: [Care Plan Lifecycle & Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-care-plan-lifecycle-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-care-goal-plan-progression-representation`) and the element's version-qualified Properties.

<a id="feat-sa-26"></a>

## Care Plan Ingestion & Distribution

Ingest multidisciplinary care plans and syndicate updates to authorized care team members.

Care-plan exchange keeps authorised team members aware of established multidisciplinary intent and its updates. It provides information continuity while preserving clinical planning authority and the distinction between a distributed plan and care actually performed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:care-plan-ingestion--distribution |
| canonicalId | 6d4b374a6bca4b58a86c912a94bbd6b1 |
| displayName | [Feature] Care Plan Ingestion & Distribution |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CarePlanLifecycleAndCoordination.CarePlanIngestionAndDistribution |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CarePlanLifecycleAndCoordination.FEAT-CarePlanIngestionAndDistribution |
| Snapshot publication reference | FEAT-SA-26 |
| ArchiMate semantic ID | id-651b1c005e2352d390d8a34cb2b787d7 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Care Plan Ingestion & Distribution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-CARE-PLAN-LIFECYCLE-COORDINATION.FEAT-SA-26 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CARE-PLAN-ADMINISTRATION.FEAT-SA-26"] |

Responsibility/framing context: [Care Plan Lifecycle & Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-care-plan-lifecycle-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:care-plan-ingestion--distribution`) and the element's version-qualified Properties.

<a id="feat-sd-16"></a>

## Community Care Plan Synchronization

Synchronize community care plan updates between hospital outreach teams and community health organizations.

Community plan synchronisation keeps hospital outreach and community participants informed of established plan changes. It supports shared clinical intent without allowing synchronisation to become authority to choose that intent or allocate clinical work.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:community-care-plan-synchronization |
| canonicalId | 30a0a2a6f1044d9ca9337b655f303e24 |
| displayName | [Feature] Community Care Plan Synchronization |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CommunityCareCoordination.CommunityCarePlanSynchronization |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CommunityCareCoordination.FEAT-CommunityCarePlanSynchronization |
| Snapshot publication reference | FEAT-SD-16 |
| ArchiMate semantic ID | id-ab77a6c8e9d9547cb46e0d7fdcf06500 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Community Care Plan Synchronization |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-COMMUNITY-CARE.FEAT-SD-16 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-COMMUNITY-CARE.FEAT-SD-16"] |

Responsibility/framing context: [Community Care Coordination](business-enabling-capabilities.md#hcm-bec-community-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:community-care-plan-synchronization`) and the element's version-qualified Properties.

<a id="feat-sd-15"></a>

## Community Encounter Ingestion

Ingest home visit notes, community nursing observations, and social support assessments.

Community encounter ingestion brings home-visit and community assessments into the longitudinal information context. It supports continuity across organisational boundaries while preserving source attribution and the difference between the documentation received and care performed by the platform.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:community-encounter-ingestion |
| canonicalId | 7afe8ae69ed7491192d884576ca0dc15 |
| displayName | [Feature] Community Encounter Ingestion |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CommunityCareCoordination.CommunityEncounterIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CommunityCareCoordination.FEAT-CommunityEncounterIngestion |
| Snapshot publication reference | FEAT-SD-15 |
| ArchiMate semantic ID | id-66c9c702be3c599e9910cf3435db8353 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Community Encounter Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-COMMUNITY-CARE.FEAT-SD-15 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-COMMUNITY-CARE.FEAT-SD-15"] |

Responsibility/framing context: [Community Care Coordination](business-enabling-capabilities.md#hcm-bec-community-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:community-encounter-ingestion`) and the element's version-qualified Properties.

<a id="feat-sd-18"></a>

## Outreach Encounter Outcome Ingestion

Ingest and reconcile clinical documentation gathered during rural and remote outreach visits.

Outreach outcome ingestion brings documentation collected during remote visits back into the governed record. It reconnects previously separated activity with the regional context without erasing when, where and by whom the source information was established.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:outreach-encounter-outcome-ingestion |
| canonicalId | 464ee3ad8d3648d992c6e51e3632d593 |
| displayName | [Feature] Outreach Encounter Outcome Ingestion |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.OutreachCareContinuity.OutreachEncounterOutcomeIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-OutreachCareContinuity.FEAT-OutreachEncounterOutcomeIngestion |
| Snapshot publication reference | FEAT-SD-18 |
| ArchiMate semantic ID | id-b08a20a5f0025228927e240825fe0372 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Outreach Encounter Outcome Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-OUTREACH-CARE.FEAT-SD-18 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-OUTREACH-CARE.FEAT-SD-18"] |

Responsibility/framing context: [Outreach Care Continuity](business-enabling-capabilities.md#hcm-bec-outreach-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:outreach-encounter-outcome-ingestion`) and the element's version-qualified Properties.

<a id="feat-sd-17"></a>

## Outreach Visit Context Provision

Package and deliver portable clinical summaries and offline-capable care summaries for remote outreach clinics.

Outreach context provision prepares portable summaries for care beyond the usual connected environment. Their usefulness depends on retaining clinical and temporal context; an offline-capable copy is not automatically current authoritative state or a new origin of clinical facts.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:outreach-visit-context-provision |
| canonicalId | 44049d029a41437685272dfe94db4a2a |
| displayName | [Feature] Outreach Visit Context Provision |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.OutreachCareContinuity.OutreachVisitContextProvision |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-OutreachCareContinuity.FEAT-OutreachVisitContextProvision |
| Snapshot publication reference | FEAT-SD-17 |
| ArchiMate semantic ID | id-c1beacb024da5f76afcbc6579e875d04 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Outreach Visit Context Provision |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-OUTREACH-CARE.FEAT-SD-17 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-OUTREACH-CARE.FEAT-SD-17"] |

Responsibility/framing context: [Outreach Care Continuity](business-enabling-capabilities.md#hcm-bec-outreach-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:outreach-visit-context-provision`) and the element's version-qualified Properties.

<a id="feat-sa-02"></a>

## Demographic Change Distribution

Fan out verified client demographic updates to downstream clinical subsystems.

Change distribution propagates verified demographic updates to participating clinical systems. It supports consistent subject context across the network while keeping delivery to each destination distinguishable from that destination's eventual incorporation or business response.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:demographic-change-distribution |
| canonicalId | bfd2ce654a79404dbef2694d30fb39fc |
| displayName | [Feature] Demographic Change Distribution |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ClientIntakeAndDemographicUpdate.DemographicChangeDistribution |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ClientIntakeAndDemographicUpdate.FEAT-DemographicChangeDistribution |
| Snapshot publication reference | FEAT-SA-02 |
| ArchiMate semantic ID | id-35f2e62266245c35ae0957b13f0899d7 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Demographic Change Distribution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-CLIENT-INTAKE-DEMOGRAPHIC-UPDATE.FEAT-SA-02 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-CLIENT-CENTRED-ADMINISTRATION.FEAT-SA-02"] |

Responsibility/framing context: [Client Intake & Demographic Update](business-enabling-capabilities.md#hcm-bec-reviewed-client-intake-demographic-update). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:demographic-change-distribution`) and the element's version-qualified Properties.

<a id="feat-sa-01"></a>

## Demographic Update Ingestion

Ingest and validate patient admission and registration updates from external PAS systems.

Demographic ingestion receives administrative changes from upstream admission and registration systems. It makes externally established updates available for governed processing without treating each incoming assertion as an independent master-person adjudication by Harmonia.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:demographic-update-ingestion |
| canonicalId | 594ccbd95f5841e4b874fd1bade34a5d |
| displayName | [Feature] Demographic Update Ingestion |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ClientIntakeAndDemographicUpdate.DemographicUpdateIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ClientIntakeAndDemographicUpdate.FEAT-DemographicUpdateIngestion |
| Snapshot publication reference | FEAT-SA-01 |
| ArchiMate semantic ID | id-288b859447af59cb9a00b36056bba57e |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Demographic Update Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-CLIENT-INTAKE-DEMOGRAPHIC-UPDATE.FEAT-SA-01 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-CLIENT-CENTRED-ADMINISTRATION.FEAT-SA-01"] |

Responsibility/framing context: [Client Intake & Demographic Update](business-enabling-capabilities.md#hcm-bec-reviewed-client-intake-demographic-update). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:demographic-update-ingestion`) and the element's version-qualified Properties.

<a id="feat-sd-13"></a>

## Immunisation Event Submission

Ingest point-of-care immunisation administration records and syndicate to national immunization registers.

Immunisation submission connects a source-recorded administration event with the national register context. It supports continuity and reporting without performing the immunisation or making successful transmission equivalent to every downstream registry outcome.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:immunisation-event-submission |
| canonicalId | 47bab2681bf943899e83fab8c9941676 |
| displayName | [Feature] Immunisation Event Submission |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ScreeningAndImmunisationEnablement.ImmunisationEventSubmission |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ScreeningAndImmunisationEnablement.FEAT-ImmunisationEventSubmission |
| Snapshot publication reference | FEAT-SD-13 |
| ArchiMate semantic ID | id-9bff89837605529e9289be57afb905d6 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Immunisation Event Submission |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-PREVENTIVE-CARE.FEAT-SD-13 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PREVENTIVE-CARE.FEAT-SD-13"] |

Responsibility/framing context: [Screening and Immunisation Enablement](business-enabling-capabilities.md#hcm-bec-preventive-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:immunisation-event-submission`) and the element's version-qualified Properties.

<a id="feat-sd-14"></a>

## Screening Recall Notification Distribution

Distribute clinical screening reminders and recall notices to designated healthcare providers.

Screening recall distribution conveys established reminders to the designated care providers. Its notification responsibility is expressible through approved event and workflow composition; the catalogue does not invent a specialised Function or transfer the clinical decision to recall the patient.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:screening-recall-notification-distribution |
| canonicalId | 7ec85487625349a7b3292585fadb20c7 |
| displayName | [Feature] Screening Recall Notification Distribution |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ScreeningAndImmunisationEnablement.ScreeningRecallNotificationDistribution |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ScreeningAndImmunisationEnablement.FEAT-ScreeningRecallNotificationDistribution |
| Snapshot publication reference | FEAT-SD-14 |
| ArchiMate semantic ID | id-54c51ccb433e50bc874d2953adcfb82c |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Screening Recall Notification Distribution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-PREVENTIVE-CARE.FEAT-SD-14 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PREVENTIVE-CARE.FEAT-SD-14"] |

Responsibility/framing context: [Screening and Immunisation Enablement](business-enabling-capabilities.md#hcm-bec-preventive-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:screening-recall-notification-distribution`) and the element's version-qualified Properties.

<a id="feat-sa-27"></a>

## Clinical Communication Distribution

Distribute secure clinical communications between verified healthcare providers and designated care team endpoints.

Clinical communication distribution connects verified providers and designated team endpoints through governed addressed delivery. The responsibility concerns reaching the intended participant, rather than assuming that delivered information has been read, incorporated or acted upon clinically.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:clinical-communication-distribution |
| canonicalId | 8527f36d5d5a460b92403f9d84f36490 |
| displayName | [Feature] Clinical Communication Distribution |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration.SecureClinicalCommunication.ClinicalCommunicationDistribution |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration.CT3-BEC-SecureClinicalCommunication.FEAT-ClinicalCommunicationDistribution |
| Snapshot publication reference | FEAT-SA-27 |
| ArchiMate semantic ID | id-5a0cb2b52e4b5dc0864b86c6baff897a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Communication Distribution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.CT3-BEC-SECURE-CLINICAL-COMMUNICATION.FEAT-SA-27 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.FEAT-SA-27"] |

Responsibility/framing context: [Secure Clinical Communication](business-enabling-capabilities.md#hcm-bec-reviewed-secure-clinical-communication). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:clinical-communication-distribution`) and the element's version-qualified Properties.

<a id="feat-sa-29"></a>

## Communication Audit Logging

Maintain an enduring, non-PHI, tamper-evident log of all clinical communication transactions.

Communication audit evidence preserves the attributable transaction history required for scrutiny of clinical communication. It records meaningful communication facts under the zero-PHI constraints rather than turning the audit trail into a copy of message content or a permanent diagnostic trace.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:communication-audit-logging |
| canonicalId | b9df49b2f221486392f5cc393369d888 |
| displayName | [Feature] Communication Audit Logging |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration.SecureClinicalCommunication.CommunicationAuditLogging |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration.CT3-BEC-SecureClinicalCommunication.FEAT-CommunicationAuditLogging |
| Snapshot publication reference | FEAT-SA-29 |
| ArchiMate semantic ID | id-47bd63bf8323578c86caf2ae6a18f3d8 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Communication Audit Logging |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.CT3-BEC-SECURE-CLINICAL-COMMUNICATION.FEAT-SA-29 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.FEAT-SA-29"] |

Responsibility/framing context: [Secure Clinical Communication](business-enabling-capabilities.md#hcm-bec-reviewed-secure-clinical-communication). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:communication-audit-logging`) and the element's version-qualified Properties.

<a id="feat-sa-28"></a>

## Delivery Acknowledgement Tracking

Correlate recipient delivery and read receipts with outbound clinical communications, distinguishing transport delivery from recipient acknowledgement. Receipt/read evidence does not by itself establish business acceptance, clinical review, incorporation, approval, finality or originating authority.

Acknowledgement tracking associates delivery and read evidence with the communication transaction. Each receipt retains its contractual meaning; neither delivery nor reading alone proves business acceptance, clinical review, approval, finality or originating authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:delivery-acknowledgement-tracking |
| canonicalId | 6884eb06bb794c5b8513b9b63d69af7e |
| displayName | [Feature] Delivery Acknowledgement Tracking |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration.SecureClinicalCommunication.DeliveryAcknowledgementTracking |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration.CT3-BEC-SecureClinicalCommunication.FEAT-DeliveryAcknowledgementTracking |
| Snapshot publication reference | FEAT-SA-28 |
| ArchiMate semantic ID | id-801fe8f13b7f525cacd29bd5ad21f1aa |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Delivery Acknowledgement Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.CT3-BEC-SECURE-CLINICAL-COMMUNICATION.FEAT-SA-28 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.FEAT-SA-28"] |

Responsibility/framing context: [Secure Clinical Communication](business-enabling-capabilities.md#hcm-bec-reviewed-secure-clinical-communication). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:delivery-acknowledgement-tracking`) and the element's version-qualified Properties.

<a id="feat-sa-23"></a>

## Clinical Document Ingestion

Ingest structured and unstructured clinical documents (discharge summaries, consult notes, letters).

Clinical document ingestion receives authored documents into governed information management. It supports both structured and unstructured contributions while preserving that receipt establishes a managed contribution, not independent clinical verification of every assertion in the document.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:clinical-document-ingestion |
| canonicalId | 54c79edadc7a478fb594ff4eb47f8368 |
| displayName | [Feature] Clinical Document Ingestion |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.ClinicalDocumentationLifecycleAdministration.ClinicalDocumentIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.CT3-BEC-ClinicalDocumentationLifecycleAdministration.FEAT-ClinicalDocumentIngestion |
| Snapshot publication reference | FEAT-SA-23 |
| ArchiMate semantic ID | id-1148b4e52cbc5303a14f52125a5ae281 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Document Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.CT3-BEC-CLINICAL-DOCUMENTATION-LIFECYCLE-ADMINISTRATION.FEAT-SA-23 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION.FEAT-SA-23"] |

Responsibility/framing context: [Clinical Documentation Lifecycle Administration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-documentation-lifecycle-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:clinical-document-ingestion`) and the element's version-qualified Properties.

<a id="feat-sa-25"></a>

## Document Metadata Indexing

Index clinical document metadata (author, specialty, encounter, date, confidentiality) for rapid discovery.

Metadata indexing makes documents discoverable by clinically and operationally relevant attributes. It describes content for search without making the index the document itself or granting access merely because a matching record can be located.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:document-metadata-indexing |
| canonicalId | 0c85a269e90a4329b83d934f9061cd49 |
| displayName | [Feature] Document Metadata Indexing |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.ClinicalDocumentationLifecycleAdministration.DocumentMetadataIndexing |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.CT3-BEC-ClinicalDocumentationLifecycleAdministration.FEAT-DocumentMetadataIndexing |
| Snapshot publication reference | FEAT-SA-25 |
| ArchiMate semantic ID | id-907bd80b5a7a5b8c81468b8b4bc80958 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Document Metadata Indexing |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.CT3-BEC-CLINICAL-DOCUMENTATION-LIFECYCLE-ADMINISTRATION.FEAT-SA-25 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION.FEAT-SA-25"] |

Responsibility/framing context: [Clinical Documentation Lifecycle Administration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-documentation-lifecycle-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:document-metadata-indexing`) and the element's version-qualified Properties.

<a id="feat-sa-24"></a>

## Document Versioning & Supersession

Manage document classification, author attribution, amendment attribution, versioning, and supersession or withdrawal without destructive data loss.

Versioning and supersession make document change understandable without destructive loss of the previous account. Author and amendment attribution distinguish who established each contribution; withdrawal or correction changes current standing without retrospectively erasing what Harmonia previously received or managed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:document-versioning--supersession |
| canonicalId | c8e2e623dd4e4c3b8de6c3bfb8c2a3ee |
| displayName | [Feature] Document Versioning & Supersession |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.ClinicalDocumentationLifecycleAdministration.DocumentVersioningAndSupersession |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.CT3-BEC-ClinicalDocumentationLifecycleAdministration.FEAT-DocumentVersioningAndSupersession |
| Snapshot publication reference | FEAT-SA-24 |
| ArchiMate semantic ID | id-c2d307b43e135f2aa2d68dbb8a58661e |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Document Versioning & Supersession |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.CT3-BEC-CLINICAL-DOCUMENTATION-LIFECYCLE-ADMINISTRATION.FEAT-SA-24 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION.FEAT-SA-24"] |

Responsibility/framing context: [Clinical Documentation Lifecycle Administration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-documentation-lifecycle-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:document-versioning--supersession`) and the element's version-qualified Properties.

<a id="feat-sd-10"></a>

## Diagnostic History Consolidation

Consolidate historical diagnostic trends (e.g., cumulative laboratory results) across multiple regional providers into an integrated view.

Diagnostic history consolidation makes prior evidence visible across regional providers. A combined historical view can support comparison over time without itself establishing a diagnostic interpretation, reconciling conflicting clinical assertions or replacing each report's source standing.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:diagnostic-history-consolidation |
| canonicalId | 0b3fa0b7374842ff8ce199191a3479bd |
| displayName | [Feature] Diagnostic History Consolidation |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticServiceIntegration.DiagnosticHistoryConsolidation |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticServiceIntegration.FEAT-DiagnosticHistoryConsolidation |
| Snapshot publication reference | FEAT-SD-10 |
| ArchiMate semantic ID | id-6641d905b9765c6380aa6b335dc4200a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Diagnostic History Consolidation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-SERVICES.FEAT-SD-10 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-DIAGNOSTIC-SERVICES.FEAT-SD-10"] |

Responsibility/framing context: [Diagnostic Service Integration](business-enabling-capabilities.md#hcm-bec-diagnostic-services). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:diagnostic-history-consolidation`) and the element's version-qualified Properties.

<a id="feat-sd-09"></a>

## Laboratory & Imaging Result Distribution

Reliably distribute structured diagnostic reports (pathology, radiology) to ordering clinicians and designated secondary recipients.

Result distribution connects established diagnostic reports with ordering and designated secondary recipients. It supports continuity of diagnostic information across care settings while retaining the distinction between delivery, recipient review and the clinical decision based on the result.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:laboratory--imaging-result-distribution |
| canonicalId | 93d42becda8f4850b73ff2c93bb7e8f3 |
| displayName | [Feature] Laboratory & Imaging Result Distribution |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticServiceIntegration.LaboratoryAndImagingResultDistribution |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticServiceIntegration.FEAT-LaboratoryAndImagingResultDistribution |
| Snapshot publication reference | FEAT-SD-09 |
| ArchiMate semantic ID | id-8cc1576660195c098afae0eb858958bd |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Laboratory & Imaging Result Distribution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-SERVICES.FEAT-SD-09 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-DIAGNOSTIC-SERVICES.FEAT-SD-09"] |

Responsibility/framing context: [Diagnostic Service Integration](business-enabling-capabilities.md#hcm-bec-diagnostic-services). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:laboratory--imaging-result-distribution`) and the element's version-qualified Properties.

<a id="hcm-ft-diagnostic-preliminary-corrected-report-handling"></a>

## Diagnostic Preliminary & Corrected Report Handling

Receive, qualify and distribute preliminary, corrected and addendum diagnostic information with its relationship to prior reports.

**Overview**

The finalised-report Feature remains unchanged. The broader documented diagnostic behaviour needs a separate frame for information whose preliminary or correction standing is material.

**Boundary**

Does not originate diagnostic interpretation or silently overwrite a previously received report.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-diagnostic-preliminary-corrected-report-handling |
| canonicalId | 7449ee4dada7424ea6d39785258b4981 |
| displayName | [Feature] Diagnostic Preliminary & Corrected Report Handling |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement.DiagnosticPreliminaryAndCorrectedReportHandling |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement.FEAT-DiagnosticPreliminaryAndCorrectedReportHandling |
| Snapshot publication reference | HCM-FT-diagnostic-preliminary-corrected-report-handling |
| ArchiMate semantic ID | id-790a8b7d12b65899b81a59248847861d |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-WORKFLOW-MANAGEMENT.FEAT-STEP2-DIAGNOSTIC-PRELIMINARY-CORRECTED-REPORT-HANDLING |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.FEAT-STEP2-DIAGNOSTIC-PRELIMINARY-CORRECTED-REPORT-HANDLING"] |

Responsibility/framing context: [Diagnostic Workflow Management](business-enabling-capabilities.md#hcm-bec-reviewed-diagnostic-workflow-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-diagnostic-preliminary-corrected-report-handling`) and the element's version-qualified Properties.

<a id="feat-sa-17"></a>

## Diagnostic Report Distribution

Distribute finalized laboratory, pathology, and imaging reports to ordering clinicians and designated care teams.

This Feature distributes finalised diagnostic reports to the ordering and designated care participants. Its narrower finalised-report scope is preserved even though broader Diagnostic Administration behaviour also handles preliminary, corrected and addendum information; those behaviours do not silently redefine the Feature.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:diagnostic-report-distribution |
| canonicalId | 3813b3b5175e461ba74805d8fa2aa648 |
| displayName | [Feature] Diagnostic Report Distribution |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement.DiagnosticReportDistribution |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement.FEAT-DiagnosticReportDistribution |
| Snapshot publication reference | FEAT-SA-17 |
| ArchiMate semantic ID | id-6ffaf46cf2715eb28cd85d28c24c7d0a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Diagnostic Report Distribution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-WORKFLOW-MANAGEMENT.FEAT-SA-17 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.FEAT-SA-17"] |

Responsibility/framing context: [Diagnostic Workflow Management](business-enabling-capabilities.md#hcm-bec-reviewed-diagnostic-workflow-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:diagnostic-report-distribution`) and the element's version-qualified Properties.

<a id="feat-sa-16"></a>

## Diagnostic Report Ingestion & Binding

Ingest finalized laboratory and radiology reports, verifying content integrity and binding them to the parent order.

Report ingestion binds a finalised diagnostic contribution to the order it answers and verifies content integrity. The report's professional standing comes from its diagnostic source; successful ingestion does not independently authorise its findings or establish a patient-level clinical conclusion.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:diagnostic-report-ingestion--binding |
| canonicalId | a00823445a8a44c1a00f3ec93a7e0e6f |
| displayName | [Feature] Diagnostic Report Ingestion & Binding |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement.DiagnosticReportIngestionAndBinding |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement.FEAT-DiagnosticReportIngestionAndBinding |
| Snapshot publication reference | FEAT-SA-16 |
| ArchiMate semantic ID | id-edd2707ca7e458b190c0e857d51bc695 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Diagnostic Report Ingestion & Binding |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-WORKFLOW-MANAGEMENT.FEAT-SA-16 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.FEAT-SA-16"] |

Responsibility/framing context: [Diagnostic Workflow Management](business-enabling-capabilities.md#hcm-bec-reviewed-diagnostic-workflow-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:diagnostic-report-ingestion--binding`) and the element's version-qualified Properties.

<a id="feat-sa-15"></a>

## Diagnostic Request Correlation

Correlate diagnostic test orders with collected clinical specimens and patient encounters.

Diagnostic request correlation connects the order with its specimen and encounter context. It helps distinguish evidence belonging to the intended fulfilment from superficially similar results, without treating association as proof of specimen suitability or diagnostic interpretation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:diagnostic-request-correlation |
| canonicalId | 30cd73b8d7d941648affbb379196f532 |
| displayName | [Feature] Diagnostic Request Correlation |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement.DiagnosticRequestCorrelation |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement.FEAT-DiagnosticRequestCorrelation |
| Snapshot publication reference | FEAT-SA-15 |
| ArchiMate semantic ID | id-6d5eae5946d45e069850870062bc6b74 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Diagnostic Request Correlation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-WORKFLOW-MANAGEMENT.FEAT-SA-15 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.FEAT-SA-15"] |

Responsibility/framing context: [Diagnostic Workflow Management](business-enabling-capabilities.md#hcm-bec-reviewed-diagnostic-workflow-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:diagnostic-request-correlation`) and the element's version-qualified Properties.

<a id="feat-sd-04"></a>

## Acute Care Clinical Context Provision

Provide consolidated longitudinal history and active medication lists to acute care clinicians at point of triage.

Acute context provision brings relevant history and active medications together for clinicians facing a current presentation. It supports rapid understanding without substituting the consolidated view for clinical assessment, triage or a decision about treatment.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:acute-care-clinical-context-provision |
| canonicalId | 90de621f7ac1410eb6fc770cb07a9e03 |
| displayName | [Feature] Acute Care Clinical Context Provision |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.AcuteCareMonitoringAndEventCoordination.AcuteCareClinicalContextProvision |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-AcuteCareMonitoringAndEventCoordination.FEAT-AcuteCareClinicalContextProvision |
| Snapshot publication reference | FEAT-SD-04 |
| ArchiMate semantic ID | id-2b78a0d9086258329d7c35d562d218a8 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Acute Care Clinical Context Provision |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ACUTE-CARE.FEAT-SD-04 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-ACUTE-CARE.FEAT-SD-04"] |

Responsibility/framing context: [Acute Care Monitoring & Event Coordination](business-enabling-capabilities.md#hcm-bec-acute-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:acute-care-clinical-context-provision`) and the element's version-qualified Properties.

<a id="feat-sd-03"></a>

## Acute Clinical State Event Capture

Ingest clinical deterioration notifications and acute clinical status updates.

Acute-state capture records deterioration notifications and current clinical status as externally established information. Approved reusable clinical-information composition is sufficient for this responsibility; routing a critical-result alert does not become a specialised capture Function merely because both concern urgent care.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:acute-clinical-state-event-capture |
| canonicalId | 566f6934c9b04627b0876f91bedd8f30 |
| displayName | [Feature] Acute Clinical State Event Capture |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.AcuteCareMonitoringAndEventCoordination.AcuteClinicalStateEventCapture |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-AcuteCareMonitoringAndEventCoordination.FEAT-AcuteClinicalStateEventCapture |
| Snapshot publication reference | FEAT-SD-03 |
| ArchiMate semantic ID | id-ac6154860e0d561e83acf8b1bbb85fda |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Acute Clinical State Event Capture |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ACUTE-CARE.FEAT-SD-03 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-ACUTE-CARE.FEAT-SD-03"] |

Responsibility/framing context: [Acute Care Monitoring & Event Coordination](business-enabling-capabilities.md#hcm-bec-acute-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:acute-clinical-state-event-capture`) and the element's version-qualified Properties.

<a id="feat-sa-10"></a>

## Encounter Bed/Location Association

Dynamically maintain the association between an encounter, care team, and physical bed or ward location.

Encounter association keeps the relevant place and care-team context aligned with the episode as the patient moves. Location changes are meaningful operational facts, but they do not themselves establish a transfer of clinical responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:encounter-bedlocation-association |
| canonicalId | 47f0fca36aa4454eae95f4b151c7076f |
| displayName | [Feature] Encounter Bed/Location Association |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.EncounterLifecycleManagement.EncounterBedLocationAssociation |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-EncounterLifecycleManagement.FEAT-EncounterBedLocationAssociation |
| Snapshot publication reference | FEAT-SA-10 |
| ArchiMate semantic ID | id-21972725cd225097acead1b4571b1a95 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Encounter Bed/Location Association |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ENCOUNTER-LIFECYCLE-MANAGEMENT.FEAT-SA-10 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.FEAT-SA-10"] |

Responsibility/framing context: [Encounter Lifecycle Management](business-enabling-capabilities.md#hcm-bec-reviewed-encounter-lifecycle-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:encounter-bedlocation-association`) and the element's version-qualified Properties.

<a id="feat-sa-08"></a>

## Encounter Context Creation

Create and maintain authoritative encounter tracking contexts for hospital admissions, outpatient visits, and community contacts.

Encounter context creation supplies an identifiable frame for information concerning a particular admission, visit or contact. Its governed tracking authority concerns the encounter representation within Harmonia; it does not make Harmonia the originator of every clinical fact associated with that encounter.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:encounter-context-creation |
| canonicalId | 817f1046d7e84699a2e2050b84285f95 |
| displayName | [Feature] Encounter Context Creation |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.EncounterLifecycleManagement.EncounterContextCreation |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-EncounterLifecycleManagement.FEAT-EncounterContextCreation |
| Snapshot publication reference | FEAT-SA-08 |
| ArchiMate semantic ID | id-4390e66b343d556f8649e5367a3502c7 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Encounter Context Creation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ENCOUNTER-LIFECYCLE-MANAGEMENT.FEAT-SA-08 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.FEAT-SA-08"] |

Responsibility/framing context: [Encounter Lifecycle Management](business-enabling-capabilities.md#hcm-bec-reviewed-encounter-lifecycle-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:encounter-context-creation`) and the element's version-qualified Properties.

<a id="hcm-ft-encounter-history-retrieval"></a>

## Encounter History Retrieval

Expose qualified episode, movement and encounter-history context.

**Overview**

Consumers need to interpret prior care-place and episode relationships, not just the latest encounter state; historical retrieval remains bounded by source attribution and access policy.

**Boundary**

A historical view does not retrospectively alter clinical events or acquire longitudinal-record ownership.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-encounter-history-retrieval |
| canonicalId | 4088e1408f554143a0f1469efd1b43a7 |
| displayName | [Feature] Encounter History Retrieval |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.EncounterLifecycleManagement.EncounterHistoryRetrieval |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-EncounterLifecycleManagement.FEAT-EncounterHistoryRetrieval |
| Snapshot publication reference | HCM-FT-encounter-history-retrieval |
| ArchiMate semantic ID | id-9240e1a4cc6f53c4b6f1b8d0b36a96db |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ENCOUNTER-LIFECYCLE-MANAGEMENT.FEAT-STEP2-ENCOUNTER-HISTORY-RETRIEVAL |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.FEAT-STEP2-ENCOUNTER-HISTORY-RETRIEVAL"] |

Responsibility/framing context: [Encounter Lifecycle Management](business-enabling-capabilities.md#hcm-bec-reviewed-encounter-lifecycle-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-encounter-history-retrieval`) and the element's version-qualified Properties.

<a id="feat-sa-09"></a>

## Encounter State Progression

Track encounter status transitions (PLANNED → ARRIVED → IN_PROGRESS → ON_LEAVE → DISCHARGED).

Encounter progression preserves the difference between planned, arrived, active, leave and discharged circumstances. These states make administrative activity interpretable without implying a clinical outcome. The source's ON_LEAVE transition detail remains incomplete in the corresponding Process and is not repaired here.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:encounter-state-progression |
| canonicalId | c049039bf57e4e4aa97aab5967196383 |
| displayName | [Feature] Encounter State Progression |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.EncounterLifecycleManagement.EncounterStateProgression |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-EncounterLifecycleManagement.FEAT-EncounterStateProgression |
| Snapshot publication reference | FEAT-SA-09 |
| ArchiMate semantic ID | id-26d1c6dd72ab5d6198424ef2ad44a966 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Encounter State Progression |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ENCOUNTER-LIFECYCLE-MANAGEMENT.FEAT-SA-09 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.FEAT-SA-09"] |

Responsibility/framing context: [Encounter Lifecycle Management](business-enabling-capabilities.md#hcm-bec-reviewed-encounter-lifecycle-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:encounter-state-progression`) and the element's version-qualified Properties.

<a id="feat-sa-19"></a>

## Dispense Event Tracking

Ingest medication dispense verification records from pharmacy management systems.

Dispense tracking receives the pharmacy's evidence that medication was supplied under the applicable context. It connects supply with the medication account without inferring that the patient received a dose or that Harmonia performed the professional dispensing activity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:dispense-event-tracking |
| canonicalId | 430bf990d4194255b7ae09c2fe27367e |
| displayName | [Feature] Dispense Event Tracking |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationOrderAndDispenseManagement.DispenseEventTracking |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationOrderAndDispenseManagement.FEAT-DispenseEventTracking |
| Snapshot publication reference | FEAT-SA-19 |
| ArchiMate semantic ID | id-27b82b85e02654628f22891f870d6379 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Dispense Event Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-ORDER-DISPENSE-MANAGEMENT.FEAT-SA-19 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-MEDICATION-ADMINISTRATION.FEAT-SA-19"] |

Responsibility/framing context: [Medication Order & Dispense Management](business-enabling-capabilities.md#hcm-bec-reviewed-medication-order-dispense-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:dispense-event-tracking`) and the element's version-qualified Properties.

<a id="feat-sa-20"></a>

## Medication Administration Record (MAR) Ingestion

Ingest point-of-care medication administration events.

MAR ingestion captures source-reported point-of-care administration events. It preserves the crucial distinction between intended or supplied medication and an administration recorded as performed, without originating that bedside event or its clinical authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:medication-administration-record-mar-ingestion |
| canonicalId | 779d9cb66bc84585a8dd1f456b0ee37c |
| displayName | [Feature] Medication Administration Record (MAR) Ingestion |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationOrderAndDispenseManagement.MedicationAdministrationRecordMarIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationOrderAndDispenseManagement.FEAT-MedicationAdministrationRecordMarIngestion |
| Snapshot publication reference | FEAT-SA-20 |
| ArchiMate semantic ID | id-9c97bc1a60fa5266a83e994d2dc0f169 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Medication Administration Record (MAR) Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-ORDER-DISPENSE-MANAGEMENT.FEAT-SA-20 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-MEDICATION-ADMINISTRATION.FEAT-SA-20"] |

Responsibility/framing context: [Medication Order & Dispense Management](business-enabling-capabilities.md#hcm-bec-reviewed-medication-order-dispense-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:medication-administration-record-mar-ingestion`) and the element's version-qualified Properties.

<a id="feat-sa-18"></a>

## Medication Order Ingestion

Ingest inpatient and discharge prescription orders.

Medication order ingestion preserves the intended therapy expressed by inpatient or discharge prescriptions. The received order is evidence of clinical intent, distinct from pharmacy dispensing and the patient's subsequent administration record.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:medication-order-ingestion |
| canonicalId | 5247b47f181243bb975ae2cd70db65ba |
| displayName | [Feature] Medication Order Ingestion |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationOrderAndDispenseManagement.MedicationOrderIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationOrderAndDispenseManagement.FEAT-MedicationOrderIngestion |
| Snapshot publication reference | FEAT-SA-18 |
| ArchiMate semantic ID | id-41f5134accc85a82904baca65799badc |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Medication Order Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-ORDER-DISPENSE-MANAGEMENT.FEAT-SA-18 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-MEDICATION-ADMINISTRATION.FEAT-SA-18"] |

Responsibility/framing context: [Medication Order & Dispense Management](business-enabling-capabilities.md#hcm-bec-reviewed-medication-order-dispense-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:medication-order-ingestion`) and the element's version-qualified Properties.

<a id="feat-sd-12"></a>

## Medication Administration Tracking

Distribute medication therapy orders, dispensing updates, and administration records across clinical care boundaries.

Medication tracking conveys orders, dispensing changes and administration information across care boundaries while keeping their meanings separate. This supports a coherent therapy account without treating a supplied medicine as an administered dose or a distributed order as treatment performed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:medication-administration-tracking |
| canonicalId | 6e01c72ef9c149e2ac231463810c2265 |
| displayName | [Feature] Medication Administration Tracking |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationTherapyProvisionAndMonitoring.MedicationAdministrationTracking |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationTherapyProvisionAndMonitoring.FEAT-MedicationAdministrationTracking |
| Snapshot publication reference | FEAT-SD-12 |
| ArchiMate semantic ID | id-fdf1c3f9f36351b5b37d22467ed821b4 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Medication Administration Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-THERAPY.FEAT-SD-12 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-MEDICATION-THERAPY.FEAT-SD-12"] |

Responsibility/framing context: [Medication Therapy Provision and Monitoring](business-enabling-capabilities.md#hcm-bec-medication-therapy). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:medication-administration-tracking`) and the element's version-qualified Properties.

<a id="feat-sd-11"></a>

## Medication History Reconciliation Ingestion

Ingest reconciled medication histories compiled during clinical transitions of care.

This Feature receives medication histories already reconciled by clinical participants during care transitions. The platform preserves and connects that account; ingestion is distinct from performing the clinical reconciliation or independently deciding which regimen is correct.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:medication-history-reconciliation-ingestion |
| canonicalId | 561e1ef130e04f38b854665bb8fb9c77 |
| displayName | [Feature] Medication History Reconciliation Ingestion |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationTherapyProvisionAndMonitoring.MedicationHistoryReconciliationIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationTherapyProvisionAndMonitoring.FEAT-MedicationHistoryReconciliationIngestion |
| Snapshot publication reference | FEAT-SD-11 |
| ArchiMate semantic ID | id-3a3587f7a51653948fd1be46dabe8ad4 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Medication History Reconciliation Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-THERAPY.FEAT-SD-11 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-MEDICATION-THERAPY.FEAT-SD-11"] |

Responsibility/framing context: [Medication Therapy Provision and Monitoring](business-enabling-capabilities.md#hcm-bec-medication-therapy). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:medication-history-reconciliation-ingestion`) and the element's version-qualified Properties.

<a id="feat-sa-14"></a>

## Order Cancellation & Modification Coordination

Coordinate order cancellation and amendment requests, ensuring synchronization with performing systems.

Cancellation and amendment coordination carries a requested change into the performing-system context. It preserves the difference between asking to change an order and evidence that the change took effect, rather than silently rewriting the original request as though no prior intent existed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:order-cancellation--modification-coordination |
| canonicalId | 6d12d9ddf4f8425ca60b289f3356f683 |
| displayName | [Feature] Order Cancellation & Modification Coordination |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression.OrderCancellationAndModificationCoordination |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression.FEAT-OrderCancellationAndModificationCoordination |
| Snapshot publication reference | FEAT-SA-14 |
| ArchiMate semantic ID | id-9dc2181af703551bbff54b8af0a2c8cb |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Order Cancellation & Modification Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.CT3-BEC-CLOSED-LOOP-ORDER-PROGRESSION.FEAT-SA-14 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.FEAT-SA-14"] |

Responsibility/framing context: [Closed-Loop Order Progression](business-enabling-capabilities.md#hcm-bec-reviewed-closed-loop-order-progression). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:order-cancellation--modification-coordination`) and the element's version-qualified Properties.

<a id="feat-sa-13"></a>

## Order Closed-Loop Progression Tracking

Track order progression across destination determination, routing, delivery, receipt acknowledgement (distinguishing technical delivery acknowledgement from business acknowledgement), execution progress, completion, and outcome association.

Closed-loop tracking keeps delivery, technical acknowledgement, business disposition, execution and returned outcome distinct. Its purpose is to expose what has actually been established at each stage, preventing a successful handoff from being reported as eventual fulfilment.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:order-closed-loop-progression-tracking |
| canonicalId | 9e63463942b94a6ba3bc045a04d3580b |
| displayName | [Feature] Order Closed-Loop Progression Tracking |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression.OrderClosedLoopProgressionTracking |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression.FEAT-OrderClosedLoopProgressionTracking |
| Snapshot publication reference | FEAT-SA-13 |
| ArchiMate semantic ID | id-157ff4007a5450beb816543f882416fa |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Order Closed-Loop Progression Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.CT3-BEC-CLOSED-LOOP-ORDER-PROGRESSION.FEAT-SA-13 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.FEAT-SA-13"] |

Responsibility/framing context: [Closed-Loop Order Progression](business-enabling-capabilities.md#hcm-bec-reviewed-closed-loop-order-progression). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:order-closed-loop-progression-tracking`) and the element's version-qualified Properties.

<a id="feat-sa-12"></a>

## Order Destination Resolution & Routing

Resolve fulfillment endpoints and reliably route orders to performing service systems.

Destination resolution connects an order with the service endpoint intended to fulfil it. Routing advances the request towards that destination without proving execution or changing the original clinical intent simply because a transport route was chosen.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:order-destination-resolution--routing |
| canonicalId | 9f8f1542efc64e89ad226689291f2635 |
| displayName | [Feature] Order Destination Resolution & Routing |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression.OrderDestinationResolutionAndRouting |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression.FEAT-OrderDestinationResolutionAndRouting |
| Snapshot publication reference | FEAT-SA-12 |
| ArchiMate semantic ID | id-287eedd7878756b69cfff8e1b3783f6b |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Order Destination Resolution & Routing |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.CT3-BEC-CLOSED-LOOP-ORDER-PROGRESSION.FEAT-SA-12 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.FEAT-SA-12"] |

Responsibility/framing context: [Closed-Loop Order Progression](business-enabling-capabilities.md#hcm-bec-reviewed-closed-loop-order-progression). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:order-destination-resolution--routing`) and the element's version-qualified Properties.

<a id="hcm-ft-order-outcome-association"></a>

## Order Outcome Association

Associate an externally established outcome with the applicable bounded order and its progression.

**Overview**

Execution completion and returned outcome are different checkpoints. This Feature makes the result link explicit without equating an order acknowledgement with an outcome.

**Boundary**

Does not interpret diagnostic findings or declare fulfilment from transport delivery alone.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-order-outcome-association |
| canonicalId | 4ad2e11162f34775adfea01b949d2803 |
| displayName | [Feature] Order Outcome Association |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression.OrderOutcomeAssociation |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression.FEAT-OrderOutcomeAssociation |
| Snapshot publication reference | HCM-FT-order-outcome-association |
| ArchiMate semantic ID | id-45230eebe97658aab88ff8abcbc9d37b |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.CT3-BEC-CLOSED-LOOP-ORDER-PROGRESSION.FEAT-STEP2-ORDER-OUTCOME-ASSOCIATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.FEAT-STEP2-ORDER-OUTCOME-ASSOCIATION"] |

Responsibility/framing context: [Closed-Loop Order Progression](business-enabling-capabilities.md#hcm-bec-reviewed-closed-loop-order-progression). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-order-outcome-association`) and the element's version-qualified Properties.

<a id="feat-sa-11"></a>

## Order Request Ingestion

Ingest clinical diagnostic, medication, and procedure order requests with validated ordering clinician context.

Order ingestion establishes the received bounded fulfilment request with its ordering-clinician context. It begins the information account needed for a closed loop while leaving the performing service's business acceptance and execution distinguishable from technical ingress acceptance.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:order-request-ingestion |
| canonicalId | fc1f8168fd6949beb810e4ff57a2368e |
| displayName | [Feature] Order Request Ingestion |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression.OrderRequestIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression.FEAT-OrderRequestIngestion |
| Snapshot publication reference | FEAT-SA-11 |
| ArchiMate semantic ID | id-54db6777e733549186d5ff73117ae94e |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Order Request Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.CT3-BEC-CLOSED-LOOP-ORDER-PROGRESSION.FEAT-SA-11 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.FEAT-SA-11"] |

Responsibility/framing context: [Closed-Loop Order Progression](business-enabling-capabilities.md#hcm-bec-reviewed-closed-loop-order-progression). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:order-request-ingestion`) and the element's version-qualified Properties.

<a id="feat-sa-22"></a>

## Procedural Documentation Ingestion

Ingest operative reports, anesthesia records, and post-procedure summaries.

Procedural documentation ingestion preserves the operative, anaesthetic and post-procedure account supplied by the clinical service. It connects performed-care documentation with the wider record without giving the integration process authority to originate or judge the clinical findings.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:procedural-documentation-ingestion |
| canonicalId | 324aefc4578f4cae934101ae5ad74ff8 |
| displayName | [Feature] Procedural Documentation Ingestion |
| friendlyName | CareAdministrationEnablement.ProcedureAdministration.ProcedureAndSurgicalAdministration.ProceduralDocumentationIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ProcedureAdministration.CT3-BEC-ProcedureAndSurgicalAdministration.FEAT-ProceduralDocumentationIngestion |
| Snapshot publication reference | FEAT-SA-22 |
| ArchiMate semantic ID | id-b2a05065d92758159716890b3d1a785c |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Procedural Documentation Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-PROCEDURE-ADMINISTRATION.CT3-BEC-PROCEDURE-SURGICAL-ADMINISTRATION.FEAT-SA-22 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-PROCEDURE-ADMINISTRATION.FEAT-SA-22"] |

Responsibility/framing context: [Procedure & Surgical Administration](business-enabling-capabilities.md#hcm-bec-reviewed-procedure-surgical-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:procedural-documentation-ingestion`) and the element's version-qualified Properties.

<a id="feat-sa-21"></a>

## Procedure Booking Ingestion

Ingest scheduled surgical case bookings and procedural requests.

Procedure booking ingestion makes scheduled cases and procedural requests available in the wider care context. It conveys intended or arranged activity while retaining the distinction between a booked procedure and one actually performed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:procedure-booking-ingestion |
| canonicalId | 12e0f64cff2043339be17fb38708247a |
| displayName | [Feature] Procedure Booking Ingestion |
| friendlyName | CareAdministrationEnablement.ProcedureAdministration.ProcedureAndSurgicalAdministration.ProcedureBookingIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ProcedureAdministration.CT3-BEC-ProcedureAndSurgicalAdministration.FEAT-ProcedureBookingIngestion |
| Snapshot publication reference | FEAT-SA-21 |
| ArchiMate semantic ID | id-c22c7a1a106758c297225365f528dddb |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Procedure Booking Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-PROCEDURE-ADMINISTRATION.CT3-BEC-PROCEDURE-SURGICAL-ADMINISTRATION.FEAT-SA-21 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-PROCEDURE-ADMINISTRATION.FEAT-SA-21"] |

Responsibility/framing context: [Procedure & Surgical Administration](business-enabling-capabilities.md#hcm-bec-reviewed-procedure-surgical-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:procedure-booking-ingestion`) and the element's version-qualified Properties.

<a id="hcm-ft-referral-destination-coordination"></a>

## Referral Destination Coordination

Coordinate qualified referral destination resolution and convey the request under its applicable disposition contract.

**Overview**

Referral intake and status capture leave a distinct routing responsibility: finding the intended service and preserving redirection without turning technical delivery into assumed care responsibility.

**Boundary**

Does not make clinical triage decisions or infer Transfer of Care from acceptance.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-referral-destination-coordination |
| canonicalId | 7d7ddffcda9d4854a02be5c3265ad775 |
| displayName | [Feature] Referral Destination Coordination |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ReferralIntakeAndProgression.ReferralDestinationCoordination |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.CT3-BEC-ReferralIntakeAndProgression.FEAT-ReferralDestinationCoordination |
| Snapshot publication reference | HCM-FT-referral-destination-coordination |
| ArchiMate semantic ID | id-d977b0b1376453b9ba66d44eb556c0e9 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.CT3-BEC-REFERRAL-INTAKE-PROGRESSION.FEAT-STEP2-REFERRAL-DESTINATION-COORDINATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.FEAT-STEP2-REFERRAL-DESTINATION-COORDINATION"] |

Responsibility/framing context: [Referral Intake & Progression](business-enabling-capabilities.md#hcm-bec-reviewed-referral-intake-progression). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-referral-destination-coordination`) and the element's version-qualified Properties.

<a id="feat-sa-03"></a>

## Referral Document Ingestion

Ingest structured clinical referral messages across integration boundaries.

Referral ingestion establishes the received referral material within the integration boundary. It preserves the request for another participant's involvement without treating successful receipt as clinical acceptance, responsibility assumption or a promise that care will be delivered.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:referral-document-ingestion |
| canonicalId | 1d1c5f8e8ca1498fb68dba601a7079dc |
| displayName | [Feature] Referral Document Ingestion |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ReferralIntakeAndProgression.ReferralDocumentIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.CT3-BEC-ReferralIntakeAndProgression.FEAT-ReferralDocumentIngestion |
| Snapshot publication reference | FEAT-SA-03 |
| ArchiMate semantic ID | id-e028948e45e9556e9ae39992ba4ae05f |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Referral Document Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.CT3-BEC-REFERRAL-INTAKE-PROGRESSION.FEAT-SA-03 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.FEAT-SA-03"] |

Responsibility/framing context: [Referral Intake & Progression](business-enabling-capabilities.md#hcm-bec-reviewed-referral-intake-progression). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:referral-document-ingestion`) and the element's version-qualified Properties.

<a id="feat-sa-05"></a>

## Referral Status & Outcome Tracking

Track referral operational progression and communicate referral outcomes (acceptance, decline, or redirection) to referring providers.

Status and outcome tracking makes explicit referral dispositions visible to the referring participant. Acceptance, decline and redirection remain meaningful alternatives; a transport acknowledgement does not fill in a missing business decision or establish a universal referral progression.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:referral-status--outcome-tracking |
| canonicalId | 836fa704f7b24bfc8f7eeb72a36bb897 |
| displayName | [Feature] Referral Status & Outcome Tracking |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ReferralIntakeAndProgression.ReferralStatusAndOutcomeTracking |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.CT3-BEC-ReferralIntakeAndProgression.FEAT-ReferralStatusAndOutcomeTracking |
| Snapshot publication reference | FEAT-SA-05 |
| ArchiMate semantic ID | id-a81241907aa15de294c53e8dc44d9367 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Referral Status & Outcome Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.CT3-BEC-REFERRAL-INTAKE-PROGRESSION.FEAT-SA-05 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.FEAT-SA-05"] |

Responsibility/framing context: [Referral Intake & Progression](business-enabling-capabilities.md#hcm-bec-reviewed-referral-intake-progression). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:referral-status--outcome-tracking`) and the element's version-qualified Properties.

<a id="feat-sa-04"></a>

## Referral Supporting Information

Collate and associate supporting clinical documentation, diagnostic history, and patient context with referral requests.

Supporting information gives the referral its relevant history and clinical context. Association makes the material available for the recipient's consideration while preserving source provenance and the distinction between evidence supplied and the recipient's own clinical decision.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:referral-supporting-information |
| canonicalId | c8a50c0e932847d7b7d0289101aacd98 |
| displayName | [Feature] Referral Supporting Information |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ReferralIntakeAndProgression.ReferralSupportingInformation |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.CT3-BEC-ReferralIntakeAndProgression.FEAT-ReferralSupportingInformation |
| Snapshot publication reference | FEAT-SA-04 |
| ArchiMate semantic ID | id-41682f56954051209563bf1a3b4af863 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Referral Supporting Information |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.CT3-BEC-REFERRAL-INTAKE-PROGRESSION.FEAT-SA-04 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.FEAT-SA-04"] |

Responsibility/framing context: [Referral Intake & Progression](business-enabling-capabilities.md#hcm-bec-reviewed-referral-intake-progression). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:referral-supporting-information`) and the element's version-qualified Properties.

<a id="feat-sa-06"></a>

## Appointment Notification Ingestion

Ingest scheduled appointment events from departmental scheduling systems.

Appointment ingestion receives scheduling facts established by departmental systems. It supports shared awareness of planned care opportunities without claiming authority over the underlying booking decision or proving that attendance and clinical fulfilment will occur.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:appointment-notification-ingestion |
| canonicalId | 1d5bc2b1742544728357fbea42a55ec4 |
| displayName | [Feature] Appointment Notification Ingestion |
| friendlyName | CareAdministrationEnablement.SchedulingAdministration.AppointmentAndBookingAdministration.AppointmentNotificationIngestion |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-SchedulingAdministration.CT3-BEC-AppointmentAndBookingAdministration.FEAT-AppointmentNotificationIngestion |
| Snapshot publication reference | FEAT-SA-06 |
| ArchiMate semantic ID | id-5c8267d2b067511591a88922b37ab219 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Appointment Notification Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-SCHEDULING-ADMINISTRATION.CT3-BEC-APPOINTMENT-BOOKING-ADMINISTRATION.FEAT-SA-06 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-SCHEDULING-ADMINISTRATION.FEAT-SA-06"] |

Responsibility/framing context: [Appointment & Booking Administration](business-enabling-capabilities.md#hcm-bec-reviewed-appointment-booking-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:appointment-notification-ingestion`) and the element's version-qualified Properties.

<a id="feat-sa-07"></a>

## Appointment Status Synchronization

Propagate booking cancellations, reschedules, and attendances across care coordination systems.

Status synchronisation keeps cancellation, rescheduling and attendance information coherent across coordination contexts. The purpose is propagation of the externally established booking change rather than independent scheduling or inference of a clinical outcome from appointment state.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:appointment-status-synchronization |
| canonicalId | a7a9932600d84434a1a37005f726747d |
| displayName | [Feature] Appointment Status Synchronization |
| friendlyName | CareAdministrationEnablement.SchedulingAdministration.AppointmentAndBookingAdministration.AppointmentStatusSynchronization |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-SchedulingAdministration.CT3-BEC-AppointmentAndBookingAdministration.FEAT-AppointmentStatusSynchronization |
| Snapshot publication reference | FEAT-SA-07 |
| ArchiMate semantic ID | id-a8db07a831405f489c73236896b52c25 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Appointment Status Synchronization |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-SCHEDULING-ADMINISTRATION.CT3-BEC-APPOINTMENT-BOOKING-ADMINISTRATION.FEAT-SA-07 |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-SCHEDULING-ADMINISTRATION.FEAT-SA-07"] |

Responsibility/framing context: [Appointment & Booking Administration](business-enabling-capabilities.md#hcm-bec-reviewed-appointment-booking-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:appointment-status-synchronization`) and the element's version-qualified Properties.

<a id="feat-em-10"></a>

## Confidentiality Flag Enforcement

Enforce restricted access flags (e.g., VIP, sensitive diagnosis, restricted record) across query and presentation interfaces.

Confidentiality enforcement makes a restricted-record condition effective across discovery and presentation paths. Its purpose is consistent governed treatment of sensitive information, rather than a flag displayed in one interface while other paths disclose the same content without restriction.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:confidentiality-flag-enforcement |
| canonicalId | 1c18e7c2ccf640c9afc2525503b505a6 |
| displayName | [Feature] Confidentiality Flag Enforcement |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientPrivacyGuardrailManagement.ConfidentialityFlagEnforcement |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientPrivacyGuardrailManagement.FEAT-ConfidentialityFlagEnforcement |
| Snapshot publication reference | FEAT-EM-10 |
| ArchiMate semantic ID | id-94fde1c6f231504a8363a1b0e0a957da |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Confidentiality Flag Enforcement |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-PRIVACY.FEAT-EM-10 |

Responsibility/framing context: [Client Privacy Guardrail Management](business-enabling-capabilities.md#hcm-bec-client-privacy). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:confidentiality-flag-enforcement`) and the element's version-qualified Properties.

<a id="feat-em-09"></a>

## Consent Directive Evaluation

Evaluate client consent directives against requested information disclosure and exchange actions.

Consent evaluation brings the client's applicable directives into consideration of a requested disclosure or exchange. It distinguishes the recorded directive from the particular action being evaluated, preserving the need for established policy and authority rather than treating consent data as a universal permission token.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:consent-directive-evaluation |
| canonicalId | baf6dc6ef38a4256a9a67a65fd3f31ba |
| displayName | [Feature] Consent Directive Evaluation |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientPrivacyGuardrailManagement.ConsentDirectiveEvaluation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientPrivacyGuardrailManagement.FEAT-ConsentDirectiveEvaluation |
| Snapshot publication reference | FEAT-EM-09 |
| ArchiMate semantic ID | id-59c5db76c2d25e2d894cbaf05f986efb |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Consent Directive Evaluation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-PRIVACY.FEAT-EM-09 |

Responsibility/framing context: [Client Privacy Guardrail Management](business-enabling-capabilities.md#hcm-bec-client-privacy). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:consent-directive-evaluation`) and the element's version-qualified Properties.

<a id="feat-em-07"></a>

## Next-of-Kin & Guardian Association

Associate nominated emergency contacts, legal guardians, and care agents with the client record.

This Feature records the nominated people and capacities through which a client can be contacted or represented. Association preserves who the contact or guardian is in relation to the client; it does not make every nominated person an unrestricted recipient of clinical information.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:next-of-kin--guardian-association |
| canonicalId | 05de74a8a7dc4334ba60543a228e73cb |
| displayName | [Feature] Next-of-Kin & Guardian Association |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientRelationshipAdministration.NextOfKinAndGuardianAssociation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientRelationshipAdministration.FEAT-NextOfKinAndGuardianAssociation |
| Snapshot publication reference | FEAT-EM-07 |
| ArchiMate semantic ID | id-66c6ae94ed8a5c2bbc3fdeac8cb70152 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Next-of-Kin & Guardian Association |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-RELATIONSHIP.FEAT-EM-07 |

Responsibility/framing context: [Client Relationship Administration](business-enabling-capabilities.md#hcm-bec-client-relationship). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:next-of-kin--guardian-association`) and the element's version-qualified Properties.

<a id="feat-em-08"></a>

## Relationship Validity Tracking

Maintain effective date intervals and authority scopes for nominated client relationships.

Relationship validity makes effective periods and authority scope part of the relationship's meaning. A contact association that was legitimate in one period or capacity need not be treated as currently valid for every purpose.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:relationship-validity-tracking |
| canonicalId | 309603a8330545abb5db5f9889aa60d9 |
| displayName | [Feature] Relationship Validity Tracking |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientRelationshipAdministration.RelationshipValidityTracking |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientRelationshipAdministration.FEAT-RelationshipValidityTracking |
| Snapshot publication reference | FEAT-EM-08 |
| ArchiMate semantic ID | id-f18e114c0b4b52f99c8e64d4a657d4b5 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Relationship Validity Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-RELATIONSHIP.FEAT-EM-08 |

Responsibility/framing context: [Client Relationship Administration](business-enabling-capabilities.md#hcm-bec-client-relationship). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:relationship-validity-tracking`) and the element's version-qualified Properties.

<a id="feat-em-06"></a>

## Demographic Context Governance

Manage essential demographic attributes (date of birth, sex, vital status) with full provenance and timestamping.

Demographic governance retains the provenance and temporal context of attributes used to interpret the subject of care. A changed date, status or demographic assertion can therefore be understood as sourced information rather than an unexplained overwrite of previously known context.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:demographic-context-governance |
| canonicalId | 3d0377451a1b44d0bb41404207cdd867 |
| displayName | [Feature] Demographic Context Governance |
| friendlyName | EntityInformationEnablement.ClientAdministration.HealthcareSubjectProfileAdministration.DemographicContextGovernance |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-HealthcareSubjectProfileAdministration.FEAT-DemographicContextGovernance |
| Snapshot publication reference | FEAT-EM-06 |
| ArchiMate semantic ID | id-c2745c3617f756d0b58bb1c5d0a8cb71 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Demographic Context Governance |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-HEALTHCARE-SUBJECT.FEAT-EM-06 |

Responsibility/framing context: [Healthcare Subject Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-subject). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:demographic-context-governance`) and the element's version-qualified Properties.

<a id="feat-em-05"></a>

## Subject Context Binding

Bind authenticated person identity to clinical encounters, orders, and observations as the enduring subject of care with verifiable association.

Subject binding connects resolved identity with the records and activities concerning that person. It makes the association verifiable across encounters, orders and observations rather than assuming that proximity in a message or screen establishes the enduring subject of care.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:subject-context-binding |
| canonicalId | 5d956689bc714e2d8be4fff7766c0b2d |
| displayName | [Feature] Subject Context Binding |
| friendlyName | EntityInformationEnablement.ClientAdministration.HealthcareSubjectProfileAdministration.SubjectContextBinding |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-HealthcareSubjectProfileAdministration.FEAT-SubjectContextBinding |
| Snapshot publication reference | FEAT-EM-05 |
| ArchiMate semantic ID | id-3fec896b22ed53b99aa8437ae3245012 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Subject Context Binding |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-HEALTHCARE-SUBJECT.FEAT-EM-05 |

Responsibility/framing context: [Healthcare Subject Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-subject). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:subject-context-binding`) and the element's version-qualified Properties.

<a id="feat-em-02"></a>

## Cross-Authority Identifier Correlation

Correlate disparate local identifiers (e.g., hospital MRN, clinic client ID, national IHI) belonging to the same individual.

Cross-authority correlation preserves the association between identifiers used for one person by different participants. It enables information to be related across regional systems without erasing the separate identifier namespaces or claiming that Harmonia originated the identity decision.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:cross-authority-identifier-correlation |
| canonicalId | 6d74d143c7454fa0bf30743ddf7ba094 |
| displayName | [Feature] Cross-Authority Identifier Correlation |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.CrossAuthorityIdentifierCorrelation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-CrossAuthorityIdentifierCorrelation |
| Snapshot publication reference | FEAT-EM-02 |
| ArchiMate semantic ID | id-d9272acdc675539cb441d2db9c5ad6e6 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Cross-Authority Identifier Correlation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.FEAT-EM-02 |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:cross-authority-identifier-correlation`) and the element's version-qualified Properties.

<a id="feat-em-04"></a>

## Governed Identity Correction

Apply administrative identity updates, linked-record corrections, and split/merge notifications propagated from authoritative sources.

Governed correction applies externally authoritative identity updates and their consequences to Harmonia-managed associations. Administrative review concerns the legitimate application of the source outcome; it does not grant Harmonia an EMPI merge engine or authority to select a golden person record.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:governed-identity-correction |
| canonicalId | f7ab7a24e8234129b2ab02e9abd4fba2 |
| displayName | [Feature] Governed Identity Correction |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.GovernedIdentityCorrection |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-GovernedIdentityCorrection |
| Snapshot publication reference | FEAT-EM-04 |
| ArchiMate semantic ID | id-6f0bfe5e814c5734a7c530f2b7515666 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Governed Identity Correction |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.FEAT-EM-04 |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:governed-identity-correction`) and the element's version-qualified Properties.

<a id="feat-em-01"></a>

## Identifier Resolution

Ingest and resolve external identifiers against registered issuing authorities and namespace systems.

Identifier Resolution interprets an identifier in its issuing-authority and namespace context, so that the same local value is not assumed to identify the same person everywhere. It provides an identifiable subject reference without deciding an authoritative master-person match.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:identifier-resolution |
| canonicalId | ae034169e1394ccdb3a2ce9651eaf305 |
| displayName | [Feature] Identifier Resolution |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.IdentifierResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-IdentifierResolution |
| Snapshot publication reference | FEAT-EM-01 |
| ArchiMate semantic ID | id-f1e1928516765880955f638a7fec8c35 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Identifier Resolution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.FEAT-EM-01 |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:identifier-resolution`) and the element's version-qualified Properties.

<a id="feat-em-03"></a>

## Identity Alias Association

Maintain explicit associations between active identifiers and historical or demographic aliases without destructive overwriting.

Alias association keeps historical and demographic identity references navigable alongside active identifiers. The non-destructive relationship preserves how earlier information identified the person rather than rewriting that history whenever the current identity representation changes.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:identity-alias-association |
| canonicalId | 97721dbd017e4c2daa73e1e87a8499b9 |
| displayName | [Feature] Identity Alias Association |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.IdentityAliasAssociation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-IdentityAliasAssociation |
| Snapshot publication reference | FEAT-EM-03 |
| ArchiMate semantic ID | id-28d7815137a05a2aab6d1684011dbbde |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Identity Alias Association |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.FEAT-EM-03 |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:identity-alias-association`) and the element's version-qualified Properties.

<a id="hcm-ft-identity-source-correction-lineage"></a>

## Identity Source & Correction Lineage

Retain source-attributed identity assertions and externally decided correction history.

**Overview**

Identifier correlations can change when an upstream authority corrects a relationship. Reviewers must be able to distinguish the source decision, its effective standing and the managed links affected.

**Boundary**

No probabilistic matching, golden-record selection or autonomous merge authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-identity-source-correction-lineage |
| canonicalId | d72134e75001449a9e7ffae53f61458f |
| displayName | [Feature] Identity Source & Correction Lineage |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.IdentitySourceAndCorrectionLineage |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-IdentitySourceAndCorrectionLineage |
| Snapshot publication reference | HCM-FT-identity-source-correction-lineage |
| ArchiMate semantic ID | id-bdf18b5e580b5b85a34357a3b21f36db |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.FEAT-STEP2-IDENTITY-SOURCE-CORRECTION-LINEAGE |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-identity-source-correction-lineage`) and the element's version-qualified Properties.

<a id="feat-em-29"></a>

## Device Serial Tracking

Ingest and maintain unique device identifier (UDI) records and serial numbers.

Serial tracking makes individual hardware distinguishable within the device estate. A unique device reference supports subsequent assignment and source interpretation, while remaining separate from evidence that the device is maintained, connected or fit for a particular clinical use.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:device-serial-tracking |
| canonicalId | df3dae0cf3044059875dbb888ea6a9ba |
| displayName | [Feature] Device Serial Tracking |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.ClinicalDeviceProfileAdministration.DeviceSerialTracking |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-ClinicalDeviceProfileAdministration.FEAT-DeviceSerialTracking |
| Snapshot publication reference | FEAT-EM-29 |
| ArchiMate semantic ID | id-cf011e118aad5e158d838fa1c7f02e4b |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Device Serial Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-DEFINITION.FEAT-EM-29 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-INSTANCE.FEAT-EM-29"] |

Responsibility/framing context: [Clinical Device Profile Administration](business-enabling-capabilities.md#hcm-bec-device-definition). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:device-serial-tracking`) and the element's version-qualified Properties.

<a id="feat-em-28"></a>

## Device Type Specification

Catalog medical device models, regulatory approvals, and capabilities.

Device type specification provides model-level capabilities and regulatory context against which individual devices can be understood. It describes the kind of equipment without substituting for the release, condition or deployment of a particular instance.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:device-type-specification |
| canonicalId | ca6d91b03b56415790810846649a3dca |
| displayName | [Feature] Device Type Specification |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.ClinicalDeviceProfileAdministration.DeviceTypeSpecification |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-ClinicalDeviceProfileAdministration.FEAT-DeviceTypeSpecification |
| Snapshot publication reference | FEAT-EM-28 |
| ArchiMate semantic ID | id-181e8903dd805d74a4f2cb3c0cfea108 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Device Type Specification |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-DEFINITION.FEAT-EM-28 |

Responsibility/framing context: [Clinical Device Profile Administration](business-enabling-capabilities.md#hcm-bec-device-definition). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:device-type-specification`) and the element's version-qualified Properties.

<a id="feat-em-31"></a>

## Device Endpoint Registration

Register communication interfaces and security credentials for diagnostic telemetry feeds.

Endpoint registration supplies the interfaces and credential context through which a diagnostic or telemetry feed can participate. It describes provisioned connectivity rather than proving that data has arrived, been clinically interpreted or acquired authoritative standing.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:device-endpoint-registration |
| canonicalId | d32c856559c54b41941fc51fbd019195 |
| displayName | [Feature] Device Endpoint Registration |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceOaAndMMetadataManagement.DeviceEndpointRegistration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceOaAndMMetadataManagement.FEAT-DeviceEndpointRegistration |
| Snapshot publication reference | FEAT-EM-31 |
| ArchiMate semantic ID | id-b42db284cbb651eb93b4011c2db855fc |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Device Endpoint Registration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-OA-M-METADATA-MANAGEMENT.FEAT-EM-31 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-CONNECTIVITY.FEAT-EM-31"] |

Responsibility/framing context: [Device OA&M Metadata Management](business-enabling-capabilities.md#hcm-bec-reviewed-device-oa-m-metadata-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:device-endpoint-registration`) and the element's version-qualified Properties.

<a id="hcm-ft-device-association-lineage"></a>

## Device Association Lineage

Preserve the attributed history and effective basis of device-to-subject/place associations.

**Overview**

A telemetry record needs to be interpreted against the association applicable when it arose, rather than the device current location alone.

**Boundary**

Association history does not interpret physiological observations or prove clinical correctness.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-device-association-lineage |
| canonicalId | 849889c1a9e4495c86a6b32e49c449e3 |
| displayName | [Feature] Device Association Lineage |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceUtilisationAdministration.DeviceAssociationLineage |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceUtilisationAdministration.FEAT-DeviceAssociationLineage |
| Snapshot publication reference | HCM-FT-device-association-lineage |
| ArchiMate semantic ID | id-d754effb7cca5cbea74d760acd8501cc |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-UTILISATION-ADMINISTRATION.FEAT-STEP2-DEVICE-ASSOCIATION-LINEAGE |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-ASSOCIATION.FEAT-STEP2-DEVICE-ASSOCIATION-LINEAGE"] |

Responsibility/framing context: [Device Utilisation Administration](business-enabling-capabilities.md#hcm-bec-reviewed-device-utilisation-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-device-association-lineage`) and the element's version-qualified Properties.

<a id="feat-em-30"></a>

## Device-Patient/Location Assignment

Track dynamic association between a telemetry monitor, patient, and physical bed.

Dynamic device association preserves which patient or physical place a device is linked to as assignments change. This is essential context for interpreting telemetry: a stable device identity does not imply that every observation concerns the same patient over time.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:device-patientlocation-assignment |
| canonicalId | 891ed75bdd3c490296e5f0b7cacf6c87 |
| displayName | [Feature] Device-Patient/Location Assignment |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceUtilisationAdministration.DevicePatientLocationAssignment |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceUtilisationAdministration.FEAT-DevicePatientLocationAssignment |
| Snapshot publication reference | FEAT-EM-30 |
| ArchiMate semantic ID | id-773a1d80395252f492cecaced4054bf3 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Device-Patient/Location Assignment |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-UTILISATION-ADMINISTRATION.FEAT-EM-30 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-ASSOCIATION.FEAT-EM-30"] |

Responsibility/framing context: [Device Utilisation Administration](business-enabling-capabilities.md#hcm-bec-reviewed-device-utilisation-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:device-patientlocation-assignment`) and the element's version-qualified Properties.

<a id="feat-hso-14"></a>

## Mobile Clinical Equipment Tracking

Track physical location and allocation status of shared clinical equipment (infusion pumps, ventilators, mobile telemetry).

Equipment tracking provides the dynamic location and allocation context of shared devices. It distinguishes an identifiable asset from one available for the present operational need, while leaving maintenance, ownership and clinical authorisation with their respective responsibilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:mobile-clinical-equipment-tracking |
| canonicalId | a013a8f41ce245f48aed8d9fe33615c5 |
| displayName | [Feature] Mobile Clinical Equipment Tracking |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceUtilisationAdministration.MobileClinicalEquipmentTracking |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceUtilisationAdministration.FEAT-MobileClinicalEquipmentTracking |
| Snapshot publication reference | FEAT-HSO-14 |
| ArchiMate semantic ID | id-a563f93c6b91563eb9fb0a16e8052144 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Mobile Clinical Equipment Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-UTILISATION-ADMINISTRATION.FEAT-HSO-14 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-CLINICAL-RESOURCE-MANAGEMENT.FEAT-HSO-14"] |

Responsibility/framing context: [Device Utilisation Administration](business-enabling-capabilities.md#hcm-bec-reviewed-device-utilisation-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:mobile-clinical-equipment-tracking`) and the element's version-qualified Properties.

<a id="feat-em-27"></a>

## Eligibility & Catchment Specification

Define clinical referral thresholds and geographic catchment boundaries.

Eligibility specification captures the clinical thresholds and geographical bounds applicable to service access. It provides the criteria context needed to consider a request without making publication of those criteria an individual eligibility decision or a completed allocation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:eligibility--catchment-specification |
| canonicalId | c65feabade3d47c39e7d7af9bc16ebdc |
| displayName | [Feature] Eligibility & Catchment Specification |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.HealthcareServiceProfileAdministration.EligibilityAndCatchmentSpecification |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-HealthcareServiceProfileAdministration.FEAT-EligibilityAndCatchmentSpecification |
| Snapshot publication reference | FEAT-EM-27 |
| ArchiMate semantic ID | id-c59136a64f975632934f7b398588f222 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Eligibility & Catchment Specification |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-HEALTHCARE-SERVICE-DEFINITION.FEAT-EM-27 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-SERVICE-ELIGIBILITY-DEFINITION.FEAT-EM-27"] |

Responsibility/framing context: [Healthcare Service Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-service-definition). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:eligibility--catchment-specification`) and the element's version-qualified Properties.

<a id="feat-em-26"></a>

## Operating Schedule Specification

Define temporal availability, clinic operating hours, and on-call service windows.

Operating schedules make temporal availability and exceptions explicit for a service. They distinguish an existing offering from one available at the relevant time, while leaving actual resource readiness and operational capacity as separate concerns.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:operating-schedule-specification |
| canonicalId | 4753953665d343c5a97bc4965be3f615 |
| displayName | [Feature] Operating Schedule Specification |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.HealthcareServiceProfileAdministration.OperatingScheduleSpecification |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-HealthcareServiceProfileAdministration.FEAT-OperatingScheduleSpecification |
| Snapshot publication reference | FEAT-EM-26 |
| ArchiMate semantic ID | id-2eea70aa8e3558b8a4cd7e61821abd11 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operating Schedule Specification |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-HEALTHCARE-SERVICE-DEFINITION.FEAT-EM-26 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-SERVICE-AVAILABILITY-DEFINITION.FEAT-EM-26"] |

Responsibility/framing context: [Healthcare Service Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-service-definition). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:operating-schedule-specification`) and the element's version-qualified Properties.

<a id="feat-em-24"></a>

## Service Catalogue Publishing

Publish structured healthcare service definitions, clinical specialties, and intake criteria to regional directories.

Service catalogue publishing makes identifiable service definitions and intake context available to regional participants. A published service can be discovered as an offering rather than inferred from an organisation's name; publication alone does not establish present availability or collective readiness.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:service-catalogue-publishing |
| canonicalId | d397e670ae524f399e91176c64390fa7 |
| displayName | [Feature] Service Catalogue Publishing |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.HealthcareServiceProfileAdministration.ServiceCataloguePublishing |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-HealthcareServiceProfileAdministration.FEAT-ServiceCataloguePublishing |
| Snapshot publication reference | FEAT-EM-24 |
| ArchiMate semantic ID | id-084705bec7aa5d6aa377c16fcdc8d05c |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Service Catalogue Publishing |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-HEALTHCARE-SERVICE-DEFINITION.FEAT-EM-24 |

Responsibility/framing context: [Healthcare Service Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-service-definition). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:service-catalogue-publishing`) and the element's version-qualified Properties.

<a id="feat-em-25"></a>

## Service-Location-Provider Binding

Correlate which organization provides a named health service at which physical location with which practitioner roles.

Service-location-provider binding explains which participant provides an identifiable service in which place and acting capacities. The mapping associations belong to Health Service Administration, while the organisations, locations and practitioner entities retain their own information responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:service-location-provider-binding |
| canonicalId | 37188bb916244f41b1b7d988ad52a2eb |
| displayName | [Feature] Service-Location-Provider Binding |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.HealthcareServiceProfileAdministration.ServiceLocationProviderBinding |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-HealthcareServiceProfileAdministration.FEAT-ServiceLocationProviderBinding |
| Snapshot publication reference | FEAT-EM-25 |
| ArchiMate semantic ID | id-e4106c09a78d572987a147ae86fef3ca |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Service-Location-Provider Binding |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-HEALTHCARE-SERVICE-DEFINITION.FEAT-EM-25 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-SERVICE-PROVISION-RELATIONSHIP.FEAT-EM-25"] |

Responsibility/framing context: [Healthcare Service Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-service-definition). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:service-location-provider-binding`) and the element's version-qualified Properties.

<a id="feat-em-23"></a>

## Atomic Care-Place Modeling

Define specific beds, care-bays, and examination rooms with their physical capabilities (e.g., telemetry-capable, negative-pressure).

Care-place modelling identifies the particular physical place and characteristics relevant to care, such as isolation or telemetry capability. It supplies the descriptive frame against which changing occupancy and readiness can be considered without making those states part of the place's identity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:atomic-care-place-modeling |
| canonicalId | ddf34f0844114798a4bd84ee72bbe497 |
| displayName | [Feature] Atomic Care-Place Modeling |
| friendlyName | EntityInformationEnablement.LocationAdministration.CarePlaceDefinitionAdministration.AtomicCarePlaceModeling |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.CT3-BEC-CarePlaceDefinitionAdministration.FEAT-AtomicCarePlaceModeling |
| Snapshot publication reference | FEAT-EM-23 |
| ArchiMate semantic ID | id-d1125ae070df590ba4705abb33cf77ea |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Atomic Care-Place Modeling |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.CT3-BEC-CARE-PLACE-DEFINITION.FEAT-EM-23 |

Responsibility/framing context: [Care-Place Definition Administration](business-enabling-capabilities.md#hcm-bec-care-place-definition). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:atomic-care-place-modeling`) and the element's version-qualified Properties.

<a id="feat-em-21"></a>

## Location Geospatial & Address Resolution

Validate physical addresses, geospatial coordinates, and location operational status.

Address and geospatial resolution gives a recognised healthcare location an intelligible physical reference. It supports place interpretation without treating a valid address or recorded operating status as proof that a particular service or care place is currently ready.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:location-geospatial--address-resolution |
| canonicalId | a27d1f97313f4ba5907c7cf7e2ec8858 |
| displayName | [Feature] Location Geospatial & Address Resolution |
| friendlyName | EntityInformationEnablement.LocationAdministration.HealthcareLocationProfileAdministration.LocationGeospatialAndAddressResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.CT3-BEC-HealthcareLocationProfileAdministration.FEAT-LocationGeospatialAndAddressResolution |
| Snapshot publication reference | FEAT-EM-21 |
| ArchiMate semantic ID | id-6df12096d1d752db9cd6211c5f51ea3a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Location Geospatial & Address Resolution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.CT3-BEC-HEALTHCARE-LOCATION.FEAT-EM-21 |

Responsibility/framing context: [Healthcare Location Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-location). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:location-geospatial--address-resolution`) and the element's version-qualified Properties.

<a id="feat-em-22"></a>

## Physical Hierarchy Modeling

Represent campus, building, wing, floor, and ward architectural relationships.

Physical hierarchy modelling relates a care environment to its campus, building, floor or ward context. The relationships describe where places sit physically; they do not automatically reproduce organisational hierarchy, service responsibility or operational capacity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:physical-hierarchy-modeling |
| canonicalId | 6166149dbd7b431985063d92e78ecdb7 |
| displayName | [Feature] Physical Hierarchy Modeling |
| friendlyName | EntityInformationEnablement.LocationAdministration.HealthcareLocationProfileAdministration.PhysicalHierarchyModeling |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.CT3-BEC-HealthcareLocationProfileAdministration.FEAT-PhysicalHierarchyModeling |
| Snapshot publication reference | FEAT-EM-22 |
| ArchiMate semantic ID | id-f80fcd4209065e388707071708a9fdf7 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Physical Hierarchy Modeling |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.CT3-BEC-HEALTHCARE-LOCATION.FEAT-EM-22 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.CT3-BEC-LOCATION-STRUCTURE.FEAT-EM-22"] |

Responsibility/framing context: [Healthcare Location Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-location). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:physical-hierarchy-modeling`) and the element's version-qualified Properties.

<a id="feat-em-20"></a>

## Directory Contact Management

Manage verified phone, email, and administrative contact channels for healthcare facilities.

Contact management provides verified administrative and clinical communication channels for organisations. It supports reaching the appropriate participant while distinguishing a directory contact from the role-specific destination and authority relevant to a particular clinical exchange.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:directory-contact-management |
| canonicalId | 1cdac17b7547448daa20b74ed45ec366 |
| displayName | [Feature] Directory Contact Management |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.HealthcareOrganisationProfileAdministration.DirectoryContactManagement |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-HealthcareOrganisationProfileAdministration.FEAT-DirectoryContactManagement |
| Snapshot publication reference | FEAT-EM-20 |
| ArchiMate semantic ID | id-15e478240fa95510833a12b9c6b0424a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Directory Contact Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-HEALTHCARE-ORGANISATION.FEAT-EM-20 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-ORGANISATION-CONTACT.FEAT-EM-20"] |

Responsibility/framing context: [Healthcare Organisation Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-organisation). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:directory-contact-management`) and the element's version-qualified Properties.

<a id="feat-em-19"></a>

## Hierarchical Structure Navigation

Model, traverse, and resolve nested departmental, divisional, and regional healthcare networks.

Hierarchy navigation makes departmental, divisional and network relationships usable without flattening them into unrelated organisations. Traversal reflects established structure; it does not create corporate authority or infer responsibility from the way a directory happens to display a tree.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:hierarchical-structure-navigation |
| canonicalId | 7ded6f7865294fb2a696a58160d577f7 |
| displayName | [Feature] Hierarchical Structure Navigation |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.HealthcareOrganisationProfileAdministration.HierarchicalStructureNavigation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-HealthcareOrganisationProfileAdministration.FEAT-HierarchicalStructureNavigation |
| Snapshot publication reference | FEAT-EM-19 |
| ArchiMate semantic ID | id-d7bdfa04e6145465a17f0e9a96432259 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Hierarchical Structure Navigation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-HEALTHCARE-ORGANISATION.FEAT-EM-19 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-ORGANISATIONAL-STRUCTURE.FEAT-EM-19"] |

Responsibility/framing context: [Healthcare Organisation Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-organisation). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:hierarchical-structure-navigation`) and the element's version-qualified Properties.

<a id="feat-em-17"></a>

## National Organisation Verification

Validate national organization identifiers (e.g., HPI-O) and legal entity classifications.

National organisation verification grounds an organisation identifier and entity classification in their recognised context. This supports reliable interpretation of the healthcare participant without assuming that its identity establishes every service, location or authority associated with it.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:national-organisation-verification |
| canonicalId | 0f17fe5439fe45dc8f4edbff3cca0776 |
| displayName | [Feature] National Organisation Verification |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.HealthcareOrganisationProfileAdministration.NationalOrganisationVerification |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-HealthcareOrganisationProfileAdministration.FEAT-NationalOrganisationVerification |
| Snapshot publication reference | FEAT-EM-17 |
| ArchiMate semantic ID | id-cf983a3e0da35a50a06f2e8c3d7e5f2d |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | National Organisation Verification |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-HEALTHCARE-ORGANISATION.FEAT-EM-17 |

Responsibility/framing context: [Healthcare Organisation Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-organisation). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:national-organisation-verification`) and the element's version-qualified Properties.

<a id="feat-em-18"></a>

## Organisation Profile Management

Govern directory profiles, operational status, and published service descriptions.

Organisation profiles provide a governed account of the participant's status and published service descriptions. They support consistent directory use across systems while leaving the organisation's real-world governance and the definition of individual offerings distinguishable.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:organisation-profile-management |
| canonicalId | 6a4c44cfb1414f3bae5ba0fce1e58970 |
| displayName | [Feature] Organisation Profile Management |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.HealthcareOrganisationProfileAdministration.OrganisationProfileManagement |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-HealthcareOrganisationProfileAdministration.FEAT-OrganisationProfileManagement |
| Snapshot publication reference | FEAT-EM-18 |
| ArchiMate semantic ID | id-f15b1ca5af8f5d329e4351c717508eff |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Organisation Profile Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-HEALTHCARE-ORGANISATION.FEAT-EM-18 |

Responsibility/framing context: [Healthcare Organisation Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-organisation). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:organisation-profile-management`) and the element's version-qualified Properties.

<a id="feat-em-11"></a>

## National Practitioner Verification

Ingest, validate, and verify national practitioner identifiers (e.g., HPI-I) and professional registration status.

National verification connects practitioner identifiers and registration information to their recognised source context. It helps distinguish an identifiable professional from an unverified directory assertion without implying that verification alone grants local clinical privileges.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:national-practitioner-verification |
| canonicalId | 12c5a17dcc564619a6a9222dd2e15528 |
| displayName | [Feature] National Practitioner Verification |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerProfileAdministration.NationalPractitionerVerification |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerProfileAdministration.FEAT-NationalPractitionerVerification |
| Snapshot publication reference | FEAT-EM-11 |
| ArchiMate semantic ID | id-b753aaf91c785bd998ff94d0ee413dac |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | National Practitioner Verification |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER.FEAT-EM-11 |

Responsibility/framing context: [Practitioner Profile Administration](business-enabling-capabilities.md#hcm-bec-practitioner). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:national-practitioner-verification`) and the element's version-qualified Properties.

<a id="feat-em-12"></a>

## Practitioner Profile Governance

Maintain core professional demographic details, qualifications, and active practice status.

Practitioner profile governance maintains the professional context used across care information and communication. Qualifications and practice status remain attributable facts, distinct from the organisation-specific role or privilege under which the practitioner may act.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:practitioner-profile-governance |
| canonicalId | d23be319c6114c28bcb9e24e1258a0fc |
| displayName | [Feature] Practitioner Profile Governance |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerProfileAdministration.PractitionerProfileGovernance |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerProfileAdministration.FEAT-PractitionerProfileGovernance |
| Snapshot publication reference | FEAT-EM-12 |
| ArchiMate semantic ID | id-99fd4d1c8c21544cadf1ae4735acd6ca |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Practitioner Profile Governance |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER.FEAT-EM-12 |

Responsibility/framing context: [Practitioner Profile Administration](business-enabling-capabilities.md#hcm-bec-practitioner). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:practitioner-profile-governance`) and the element's version-qualified Properties.

<a id="hcm-ft-provider-directory-discovery-resolution"></a>

## Provider Directory Discovery & Resolution

Expose qualified practitioner discovery using identifiers, profile criteria and effective standing.

**Overview**

Routing and care participation need to find a described provider matching stated criteria, beyond merely ingesting a national identifier or updating a profile.

**Boundary**

Discovery supplies information; it does not determine privilege or availability.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-provider-directory-discovery-resolution |
| canonicalId | cd3bd35f7f9e4852bbbce299bb795d8a |
| displayName | [Feature] Provider Directory Discovery & Resolution |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerProfileAdministration.ProviderDirectoryDiscoveryAndResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerProfileAdministration.FEAT-ProviderDirectoryDiscoveryAndResolution |
| Snapshot publication reference | HCM-FT-provider-directory-discovery-resolution |
| ArchiMate semantic ID | id-523ec46acc27554ab7706bf0c6ed45b6 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER.FEAT-STEP2-PROVIDER-DIRECTORY-DISCOVERY-RESOLUTION |

Responsibility/framing context: [Practitioner Profile Administration](business-enabling-capabilities.md#hcm-bec-practitioner). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-provider-directory-discovery-resolution`) and the element's version-qualified Properties.

<a id="feat-em-14"></a>

## Electronic Endpoint Binding

Associate validated communication endpoints (secure messaging addresses, direct routing queues) with specific practitioner roles.

Endpoint binding makes a verified communication destination specific to the practitioner's acting role. It supports routing to the intended professional capacity rather than assuming that every address attached to the person is interchangeable across organisations and services.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:electronic-endpoint-binding |
| canonicalId | 5dc3a424e68d47aea64ec573dc4b1225 |
| displayName | [Feature] Electronic Endpoint Binding |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerRoleAdministration.ElectronicEndpointBinding |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerRoleAdministration.FEAT-ElectronicEndpointBinding |
| Snapshot publication reference | FEAT-EM-14 |
| ArchiMate semantic ID | id-fecc304d80685084bf190ebdd1a0dc6c |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Electronic Endpoint Binding |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER-ROLE.FEAT-EM-14 |

Responsibility/framing context: [Practitioner Role Administration](business-enabling-capabilities.md#hcm-bec-practitioner-role). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:electronic-endpoint-binding`) and the element's version-qualified Properties.

<a id="feat-em-15"></a>

## Practitioner-Organisation Affiliation

Maintain temporal affiliations between practitioners and healthcare organizations.

Affiliation records keep practitioner participation in an organisation temporally intelligible. They support interpretation of an actor's organisational context while preserving that affiliation, employment, qualification and permission to undertake a particular activity answer different questions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:practitioner-organisation-affiliation |
| canonicalId | 9501e88ea4b7474a8aa63191687db51e |
| displayName | [Feature] Practitioner-Organisation Affiliation |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerRoleAdministration.PractitionerOrganisationAffiliation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerRoleAdministration.FEAT-PractitionerOrganisationAffiliation |
| Snapshot publication reference | FEAT-EM-15 |
| ArchiMate semantic ID | id-5199761c85d15e35bd4e6d9f00bf6e9d |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Practitioner-Organisation Affiliation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER-ROLE.FEAT-EM-15 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PROVIDER-RELATIONSHIP.FEAT-EM-15"] |

Responsibility/framing context: [Practitioner Role Administration](business-enabling-capabilities.md#hcm-bec-practitioner-role). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:practitioner-organisation-affiliation`) and the element's version-qualified Properties.

<a id="feat-em-13"></a>

## Role & Specialty Modeling

Define and associate clinical specialties, practice roles, and service capacities for a practitioner.

Role and specialty modelling describes the capacity in which a practitioner participates at an organisation or location. This keeps the person separate from each acting role and its service context; role resolution itself establishes neither Clinical Privilege nor Operational Privilege.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:role--specialty-modeling |
| canonicalId | ef859d4934514f4eb601c6abf9708760 |
| displayName | [Feature] Role & Specialty Modeling |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerRoleAdministration.RoleAndSpecialtyModeling |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerRoleAdministration.FEAT-RoleAndSpecialtyModeling |
| Snapshot publication reference | FEAT-EM-13 |
| ArchiMate semantic ID | id-eab466ee0e1d5547b9e8a7461b4e1911 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Role & Specialty Modeling |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER-ROLE.FEAT-EM-13 |

Responsibility/framing context: [Practitioner Role Administration](business-enabling-capabilities.md#hcm-bec-practitioner-role). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:role--specialty-modeling`) and the element's version-qualified Properties.

<a id="feat-hso-11"></a>

## Bed Availability Tracking

Maintain operational status of all registered beds and care-places (VACANT, OCCUPIED, BLOCKED, RESERVED, DIRTY, CLEANING, CLEAN).

Availability tracking preserves the operational distinctions among vacancy, occupancy, reservation, blockage and cleaning conditions. These states support meaningful care-place coordination without equating an unoccupied bed with a ready place or usable service capacity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:bed-availability-tracking |
| canonicalId | 7c61f21e558c44ab92c267a781116d1d |
| displayName | [Feature] Bed Availability Tracking |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement.BedAndCarePlaceStateAdministration.BedAvailabilityTracking |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement.CT3-BEC-BedAndCarePlaceStateAdministration.FEAT-BedAvailabilityTracking |
| Snapshot publication reference | FEAT-HSO-11 |
| ArchiMate semantic ID | id-37a284c316fd590991cc54279e766af4 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bed Availability Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.CT3-BEC-BED-CARE-PLACE-STATE-ADMINISTRATION.FEAT-HSO-11 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-BED-CARE-PLACE-MANAGEMENT.FEAT-HSO-11"] |

Responsibility/framing context: [Bed & Care-Place State Administration](business-enabling-capabilities.md#hcm-bec-reviewed-bed-care-place-state-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:bed-availability-tracking`) and the element's version-qualified Properties.

<a id="feat-hso-13"></a>

## Bed Readiness Notification

Notify bed allocation managers and clinical units when a cleaned bed achieves READY status.

Readiness notification exposes the established readiness of a care place to the responsible allocation participants. It makes a relevant transition actionable without deciding the clinical suitability of the next allocation or establishing whole-service readiness.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:bed-readiness-notification |
| canonicalId | 6a9cf80d6f82425cb2aaae27f4c5c349 |
| displayName | [Feature] Bed Readiness Notification |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement.BedAndCarePlaceTurnoverCoordination.BedReadinessNotification |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement.CT3-BEC-BedAndCarePlaceTurnoverCoordination.FEAT-BedReadinessNotification |
| Snapshot publication reference | FEAT-HSO-13 |
| ArchiMate semantic ID | id-62b06ad8a1485803984649065d4b2b5b |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bed Readiness Notification |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.CT3-BEC-BED-CARE-PLACE-TURNOVER-COORDINATION.FEAT-HSO-13 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-BED-CARE-PLACE-MANAGEMENT.FEAT-HSO-13"] |

Responsibility/framing context: [Bed & Care-Place Turnover Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-bed-care-place-turnover-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:bed-readiness-notification`) and the element's version-qualified Properties.

<a id="feat-hso-12"></a>

## Bed State Progression

Progress bed states in response to clinical and environmental events (e.g., patient discharge automatically transitions bed to DIRTY).

Bed-state progression applies the consequences of relevant clinical and environmental events to the managed place state. The resulting change is an operational fact, such as a need for cleaning after departure, rather than a transfer of clinical responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:bed-state-progression |
| canonicalId | c903b83181bb456a89dc9d450063eff6 |
| displayName | [Feature] Bed State Progression |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement.BedAndCarePlaceTurnoverCoordination.BedStateProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement.CT3-BEC-BedAndCarePlaceTurnoverCoordination.FEAT-BedStateProgression |
| Snapshot publication reference | FEAT-HSO-12 |
| ArchiMate semantic ID | id-39843c9f6da2547c9f5d7eabf3525ad6 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bed State Progression |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.CT3-BEC-BED-CARE-PLACE-TURNOVER-COORDINATION.FEAT-HSO-12 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-BED-CARE-PLACE-MANAGEMENT.FEAT-HSO-12"] |

Responsibility/framing context: [Bed & Care-Place Turnover Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-bed-care-place-turnover-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:bed-state-progression`) and the element's version-qualified Properties.

<a id="hcm-ft-clinic-session-capacity-management"></a>

## Clinic Session Capacity Management

Represent available, committed and utilised session capacity under clinic-specific constraints.

**Overview**

Clinic capacity depends on session staffing and service mix as well as places; queue progression alone is insufficient.

**Boundary**

Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-clinic-session-capacity-management |
| canonicalId | 02bdd6a6f22e47f5a29d722ab57f9915 |
| displayName | [Feature] Clinic Session Capacity Management |
| friendlyName | HealthServiceOperationsEnablement.ClinicAndPracticeOperations.SessionAndQueueCoordination.ClinicSessionCapacityManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicAndPracticeOperations.CT3-BEC-SessionAndQueueCoordination.FEAT-ClinicSessionCapacityManagement |
| Snapshot publication reference | HCM-FT-clinic-session-capacity-management |
| ArchiMate semantic ID | id-a766049417a05c75815f7dc20c247bb3 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.CT3-BEC-SESSION-QUEUE-COORDINATION.FEAT-STEP2-CLINIC-SESSION-CAPACITY-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-CLINIC-PRACTICE-OPERATIONS.FEAT-STEP2-CLINIC-SESSION-CAPACITY-MANAGEMENT"] |

Responsibility/framing context: [Session & Queue Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-session-queue-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-clinic-session-capacity-management`) and the element's version-qualified Properties.

<a id="feat-hso-01"></a>

## Clinic Session Tracking

Ingest and track clinic operating session start, delay, and completion states.

Clinic-session tracking records the operating session's start, delay and completion as service-level context. It is distinct from an individual patient's queue state: knowing where one patient is waiting does not establish whether the clinic session is running as planned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:clinic-session-tracking |
| canonicalId | 03e185a6e0d145c3975b3f4715c94207 |
| displayName | [Feature] Clinic Session Tracking |
| friendlyName | HealthServiceOperationsEnablement.ClinicAndPracticeOperations.SessionAndQueueCoordination.ClinicSessionTracking |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicAndPracticeOperations.CT3-BEC-SessionAndQueueCoordination.FEAT-ClinicSessionTracking |
| Snapshot publication reference | FEAT-HSO-01 |
| ArchiMate semantic ID | id-8255e836d9cf51158e21e8a35b81848e |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinic Session Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.CT3-BEC-SESSION-QUEUE-COORDINATION.FEAT-HSO-01 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-CLINIC-PRACTICE-OPERATIONS.FEAT-HSO-01"] |

Responsibility/framing context: [Session & Queue Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-session-queue-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:clinic-session-tracking`) and the element's version-qualified Properties.

<a id="feat-hso-02"></a>

## Operational Queue Progression

Track client queue progression through reception, waiting area, consultation, and billing.

Operational queue progression makes the patient's passage through clinic stages visible. The queue concerns real-world arrival, waiting and consultation context, rather than a broker queue, and does not give Harmonia authority to determine clinical priority or own the clinician's worklist.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:operational-queue-progression |
| canonicalId | 5852c7d4c9ac4bbf99e7df008d0ed770 |
| displayName | [Feature] Operational Queue Progression |
| friendlyName | HealthServiceOperationsEnablement.ClinicAndPracticeOperations.SessionAndQueueCoordination.OperationalQueueProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicAndPracticeOperations.CT3-BEC-SessionAndQueueCoordination.FEAT-OperationalQueueProgression |
| Snapshot publication reference | FEAT-HSO-02 |
| ArchiMate semantic ID | id-b86cb7ff593057368b07a7f7c77cddc7 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operational Queue Progression |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.CT3-BEC-SESSION-QUEUE-COORDINATION.FEAT-HSO-02 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-CLINIC-PRACTICE-OPERATIONS.FEAT-HSO-02"] |

Responsibility/framing context: [Session & Queue Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-session-queue-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:operational-queue-progression`) and the element's version-qualified Properties.

<a id="feat-hso-26"></a>

## Pathology Specimen Transport Tracking

Track pickup, custody chain, transport conditions, and laboratory delivery of clinical specimens.

Specimen transport tracking retains pickup, custody and condition context through laboratory delivery. It supports interpretation of the material's journey without treating a delivered package as proof of specimen suitability, completed analysis or an authorised diagnostic report.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:pathology-specimen-transport-tracking |
| canonicalId | 34f182cfc6b945d090a880943bbb4959 |
| displayName | [Feature] Pathology Specimen Transport Tracking |
| friendlyName | HealthServiceOperationsEnablement.ClinicalLogisticsCoordination.SpecimenMovementTraceability.PathologySpecimenTransportTracking |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalLogisticsCoordination.CT3-BEC-SpecimenMovementTraceability.FEAT-PathologySpecimenTransportTracking |
| Snapshot publication reference | FEAT-HSO-26 |
| ArchiMate semantic ID | id-6df39c0df1365e7390f6d062c1cdb576 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Pathology Specimen Transport Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-LOGISTICS-COORDINATION.CT3-BEC-SPECIMEN-MOVEMENT-TRACEABILITY.FEAT-HSO-26 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-CLINICAL-LOGISTICS-COORDINATION.FEAT-HSO-26"] |

Responsibility/framing context: [Specimen Movement Traceability](business-enabling-capabilities.md#hcm-bec-reviewed-specimen-movement-traceability). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:pathology-specimen-transport-tracking`) and the element's version-qualified Properties.

<a id="feat-em-16"></a>

## Clinical Privileging Record

Track active clinical admitting and practice privileges within healthcare facilities.

The privileging record makes applicable admitting and practice permissions available as contextual evidence. It represents the health service's established privilege standing rather than granting new authority merely because a record exists in the directory.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:clinical-privileging-record |
| canonicalId | 5073df3c6bb94c218ca19b74550b19fa |
| displayName | [Feature] Clinical Privileging Record |
| friendlyName | HealthServiceOperationsEnablement.ClinicalQualificationAdministration.ProviderClinicalQualificationTracking.ClinicalPrivilegingRecord |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalQualificationAdministration.CT3-BEC-ProviderClinicalQualificationTracking.FEAT-ClinicalPrivilegingRecord |
| Snapshot publication reference | FEAT-EM-16 |
| ArchiMate semantic ID | id-9806bb0b0d5b56b1b8ce353774a0e579 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Privileging Record |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-QUALIFICATION-MANAGEMENT.CT3-BEC-PROVIDER-CLINICAL-QUALIFICATION-TRACKING.FEAT-EM-16 |
| Structural aliases | ["CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PROVIDER-ENGAGEMENT.FEAT-EM-16"] |

Responsibility/framing context: [Provider Clinical Qualification Tracking](business-enabling-capabilities.md#hcm-bec-reviewed-provider-clinical-qualification-tracking). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:clinical-privileging-record`) and the element's version-qualified Properties.

<a id="feat-hso-23"></a>

## Operational Credential Check

Validate that a practitioner possesses active credentials and required competency certs prior to dispatching specialized work.

The qualification checkpoint considers active professional credentials and competency evidence before specialised dispatch. Its historical credential terminology does not make it the operational badge or physical-access responsibility of Credential Management, nor does a qualification check alone establish all required privilege.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:operational-credential-check |
| canonicalId | 1a76831985fa461c891a3428e5dea86d |
| displayName | [Feature] Operational Credential Check |
| friendlyName | HealthServiceOperationsEnablement.ClinicalQualificationAdministration.ProviderClinicalQualificationTracking.OperationalCredentialCheck |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalQualificationAdministration.CT3-BEC-ProviderClinicalQualificationTracking.FEAT-OperationalCredentialCheck |
| Snapshot publication reference | FEAT-HSO-23 |
| ArchiMate semantic ID | id-5ac299185f3957a4a8370bcbda042fca |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operational Credential Check |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-QUALIFICATION-MANAGEMENT.CT3-BEC-PROVIDER-CLINICAL-QUALIFICATION-TRACKING.FEAT-HSO-23 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT.CT3-BEC-CLINICAL-QUALIFICATION-MANAGEMENT.FEAT-HSO-23"] |

Responsibility/framing context: [Provider Clinical Qualification Tracking](business-enabling-capabilities.md#hcm-bec-reviewed-provider-clinical-qualification-tracking). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:operational-credential-check`) and the element's version-qualified Properties.

<a id="feat-hso-27"></a>

## Discharge Milestone Tracking

Track clinical criteria-led discharge milestones (medications dispensed, transportation arranged, home care confirmed).

Discharge milestone tracking records confirmations contributed by the relevant clinical and operational participants. It makes outstanding requirements visible without allowing the tracking mechanism to declare clinical readiness or assume that every local milestone defines a universal discharge pathway.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:discharge-milestone-tracking |
| canonicalId | 887eec1336934b13a3f21a8e3eb5f131 |
| displayName | [Feature] Discharge Milestone Tracking |
| friendlyName | HealthServiceOperationsEnablement.DischargeManagement.DischargeProgressionAndCoordination.DischargeMilestoneTracking |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-DischargeManagement.CT3-BEC-DischargeProgressionAndCoordination.FEAT-DischargeMilestoneTracking |
| Snapshot publication reference | FEAT-HSO-27 |
| ArchiMate semantic ID | id-37f70cf9ceb050f7b67a2f4218516acd |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Discharge Milestone Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-DISCHARGE-MANAGEMENT.CT3-BEC-DISCHARGE-PROGRESSION-COORDINATION.FEAT-HSO-27 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-DISCHARGE-MANAGEMENT.FEAT-HSO-27"] |

Responsibility/framing context: [Discharge Progression & Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-discharge-progression-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:discharge-milestone-tracking`) and the element's version-qualified Properties.

<a id="feat-hso-29"></a>

## Discharge Notification Dispatch

Dispatch departure notifications, updated bed states, and finalized discharge summaries to external care providers upon physical patient exit.

Departure notification communicates the physical exit and associated record and place-state context to the applicable participants. It distinguishes departure information from earlier readiness and from the clinical decision, while retaining the separate question of responsibility actually assumed elsewhere.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:discharge-notification-dispatch |
| canonicalId | 5476307ca5a34590aa7025ab59ca0c10 |
| displayName | [Feature] Discharge Notification Dispatch |
| friendlyName | HealthServiceOperationsEnablement.DischargeManagement.DischargeProgressionAndCoordination.DischargeNotificationDispatch |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-DischargeManagement.CT3-BEC-DischargeProgressionAndCoordination.FEAT-DischargeNotificationDispatch |
| Snapshot publication reference | FEAT-HSO-29 |
| ArchiMate semantic ID | id-5dcf4908977755da9d9d24634c06a74e |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Discharge Notification Dispatch |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-DISCHARGE-MANAGEMENT.CT3-BEC-DISCHARGE-PROGRESSION-COORDINATION.FEAT-HSO-29 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-DISCHARGE-MANAGEMENT.FEAT-HSO-29"] |

Responsibility/framing context: [Discharge Progression & Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-discharge-progression-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:discharge-notification-dispatch`) and the element's version-qualified Properties.

<a id="feat-hso-28"></a>

## Discharge Readiness & Coordination Oversight

Track and aggregate multidisciplinary discharge milestones, home care confirmations, and transit readiness across care teams.

Readiness oversight brings multidisciplinary, home-care and transport confirmations into one coordination view. Aggregation supports the responsible participants' decisions but does not itself confer authority to discharge or establish acceptance of continuing care by another provider.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:discharge-readiness--coordination-oversight |
| canonicalId | 5499422ed3644e0bbf7645f9ae3f20cb |
| displayName | [Feature] Discharge Readiness & Coordination Oversight |
| friendlyName | HealthServiceOperationsEnablement.DischargeManagement.DischargeProgressionAndCoordination.DischargeReadinessAndCoordinationOversight |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-DischargeManagement.CT3-BEC-DischargeProgressionAndCoordination.FEAT-DischargeReadinessAndCoordinationOversight |
| Snapshot publication reference | FEAT-HSO-28 |
| ArchiMate semantic ID | id-ad709cdc9a7859a6ae8f274f7f04a76c |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Discharge Readiness & Coordination Oversight |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-DISCHARGE-MANAGEMENT.CT3-BEC-DISCHARGE-PROGRESSION-COORDINATION.FEAT-HSO-28 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-DISCHARGE-MANAGEMENT.FEAT-HSO-28"] |

Responsibility/framing context: [Discharge Progression & Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-discharge-progression-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:discharge-readiness--coordination-oversight`) and the element's version-qualified Properties.

<a id="feat-sd-06"></a>

## Emergency Care Cross-Facility Correlation

Correlate incoming emergency presentations with recent regional admissions, prior discharge summaries, and known allergy profiles.

Cross-facility correlation connects an emergency presentation with relevant recent history and allergy information. It supports situational understanding across institutional boundaries without deciding the clinical significance of the combined evidence or assigning emergency treatment.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:emergency-care-cross-facility-correlation |
| canonicalId | 6140586e2b18424d845a1f07018e6b49 |
| displayName | [Feature] Emergency Care Cross-Facility Correlation |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyCareProgression.EmergencyCareCrossFacilityCorrelation |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyCareProgression.FEAT-EmergencyCareCrossFacilityCorrelation |
| Snapshot publication reference | FEAT-SD-06 |
| ArchiMate semantic ID | id-026b3d0ebfee56b5b32f4cbba982a4a8 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Emergency Care Cross-Facility Correlation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-CARE.FEAT-SD-06 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-EMERGENCY-CARE.FEAT-SD-06"] |

Responsibility/framing context: [Emergency Care Progression](business-enabling-capabilities.md#hcm-bec-emergency-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:emergency-care-cross-facility-correlation`) and the element's version-qualified Properties.

<a id="feat-sd-05"></a>

## Emergency Department Arrival & Triage Ingestion

Ingest emergency presentation, triage category, and chief complaint records.

Emergency presentation ingestion retains the arrival, triage category and presenting complaint reported by the emergency context. It makes that event available for regional correlation while preserving that the clinical triage judgement was established by the responsible source.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:emergency-department-arrival--triage-ingestion |
| canonicalId | 1eb4053512164f6f976a9767da9ba7a7 |
| displayName | [Feature] Emergency Department Arrival & Triage Ingestion |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyCareProgression.EmergencyDepartmentArrivalAndTriageIngestion |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyCareProgression.FEAT-EmergencyDepartmentArrivalAndTriageIngestion |
| Snapshot publication reference | FEAT-SD-05 |
| ArchiMate semantic ID | id-79aecd702d2b5dcfb35db553f01bf0d1 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Emergency Department Arrival & Triage Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-CARE.FEAT-SD-05 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-EMERGENCY-CARE.FEAT-SD-05"] |

Responsibility/framing context: [Emergency Care Progression](business-enabling-capabilities.md#hcm-bec-emergency-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:emergency-department-arrival--triage-ingestion`) and the element's version-qualified Properties.

<a id="feat-hso-07"></a>

## ED Bed & Bay Tracking

Track occupancy and status of emergency cubicles, resuscitation bays, and short-stay beds.

Emergency-place tracking distinguishes occupancy and status across cubicles, resuscitation bays and short-stay beds. It provides place context for departmental operations while leaving the service's usable capacity and clinical triage decisions separate from an inventory of spaces.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:ed-bed--bay-tracking |
| canonicalId | 7c0251128fc942d88bf25141421fd909 |
| displayName | [Feature] ED Bed & Bay Tracking |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyDepartmentOperationalCoordination.EdBedAndBayTracking |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyDepartmentOperationalCoordination.FEAT-EdBedAndBayTracking |
| Snapshot publication reference | FEAT-HSO-07 |
| ArchiMate semantic ID | id-5814dabf7ccf5b6d94b9dafc0e60bce1 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | ED Bed & Bay Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONAL-COORDINATION.FEAT-HSO-07 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.FEAT-HSO-07"] |

Responsibility/framing context: [Emergency Department Operational Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-emergency-department-operational-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:ed-bed--bay-tracking`) and the element's version-qualified Properties.

<a id="feat-hso-08"></a>

## ED Length of Stay Tracking

Monitor operational wait times and length-of-stay milestones, generating operational escalation notifications.

Length-of-stay tracking makes waits and operational timing thresholds visible for escalation. The timing information supports management of impediments without determining clinical urgency or making an elapsed-time alert an independent assurance finding.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:ed-length-of-stay-tracking |
| canonicalId | e544f3d76db84dfaad57880d13a873c4 |
| displayName | [Feature] ED Length of Stay Tracking |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyDepartmentOperationalCoordination.EdLengthOfStayTracking |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyDepartmentOperationalCoordination.FEAT-EdLengthOfStayTracking |
| Snapshot publication reference | FEAT-HSO-08 |
| ArchiMate semantic ID | id-39515750d91154fd8d37227f3bae2ae5 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | ED Length of Stay Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONAL-COORDINATION.FEAT-HSO-08 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.FEAT-HSO-08"] |

Responsibility/framing context: [Emergency Department Operational Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-emergency-department-operational-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:ed-length-of-stay-tracking`) and the element's version-qualified Properties.

<a id="hcm-ft-emergency-treatment-capacity-management"></a>

## Emergency Treatment Capacity Management

Represent current emergency treatment capacity under space, staffing and acuity constraints.

**Overview**

Bay state and length of stay contribute evidence but do not alone establish the ability to accept another care demand.

**Boundary**

Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-emergency-treatment-capacity-management |
| canonicalId | 502876430cf747f4b3bb102fb69557a5 |
| displayName | [Feature] Emergency Treatment Capacity Management |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyDepartmentOperationalCoordination.EmergencyTreatmentCapacityManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyDepartmentOperationalCoordination.FEAT-EmergencyTreatmentCapacityManagement |
| Snapshot publication reference | HCM-FT-emergency-treatment-capacity-management |
| ArchiMate semantic ID | id-6f6d3ad191315cfe8532bafc91d8b022 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONAL-COORDINATION.FEAT-STEP2-EMERGENCY-TREATMENT-CAPACITY-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.FEAT-STEP2-EMERGENCY-TREATMENT-CAPACITY-MANAGEMENT"] |

Responsibility/framing context: [Emergency Department Operational Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-emergency-department-operational-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-emergency-treatment-capacity-management`) and the element's version-qualified Properties.

<a id="hcm-ft-mobile-service-capacity-management"></a>

## Mobile Service Capacity Management

Represent mobile service capacity with travel, coverage, work commitment and availability context.

**Overview**

Mobile availability cannot be interpreted as a simple on-duty count because distance, assignments and suitable skills affect deliverability.

**Boundary**

Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-mobile-service-capacity-management |
| canonicalId | abdcb95b3725405f878f199b07779d19 |
| displayName | [Feature] Mobile Service Capacity Management |
| friendlyName | HealthServiceOperationsEnablement.MobileStaffManagement.MobileWorkerDispatchAndSafetyCoordination.MobileServiceCapacityManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-MobileStaffManagement.CT3-BEC-MobileWorkerDispatchAndSafetyCoordination.FEAT-MobileServiceCapacityManagement |
| Snapshot publication reference | HCM-FT-mobile-service-capacity-management |
| ArchiMate semantic ID | id-56e5d0861bcd5ac2a6bf3e2206be9623 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.CT3-BEC-MOBILE-WORKER-DISPATCH-SAFETY-COORDINATION.FEAT-STEP2-MOBILE-SERVICE-CAPACITY-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-MOBILE-STAFF-MANAGEMENT.FEAT-STEP2-MOBILE-SERVICE-CAPACITY-MANAGEMENT"] |

Responsibility/framing context: [Mobile Worker Dispatch & Safety Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-mobile-worker-dispatch-safety-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-mobile-service-capacity-management`) and the element's version-qualified Properties.

<a id="feat-hso-17"></a>

## Mobile Visit Status Tracking

Ingest arrival, visit progression, and safety check-in milestones from roving staff.

Mobile visit tracking receives arrival, progression and safety checkpoints from roving workers. It makes reported activity visible in the wider operational context without originating the clinical visit, deciding its clinical content or assuming that a checkpoint proves the intended clinical outcome.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:mobile-visit-status-tracking |
| canonicalId | 3cd49cb00159437ea25048c1158172c1 |
| displayName | [Feature] Mobile Visit Status Tracking |
| friendlyName | HealthServiceOperationsEnablement.MobileStaffManagement.MobileWorkerDispatchAndSafetyCoordination.MobileVisitStatusTracking |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-MobileStaffManagement.CT3-BEC-MobileWorkerDispatchAndSafetyCoordination.FEAT-MobileVisitStatusTracking |
| Snapshot publication reference | FEAT-HSO-17 |
| ArchiMate semantic ID | id-cc03161ecba65ae09e77504b684c45e4 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Mobile Visit Status Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.CT3-BEC-MOBILE-WORKER-DISPATCH-SAFETY-COORDINATION.FEAT-HSO-17 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-MOBILE-STAFF-MANAGEMENT.FEAT-HSO-17"] |

Responsibility/framing context: [Mobile Worker Dispatch & Safety Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-mobile-worker-dispatch-safety-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:mobile-visit-status-tracking`) and the element's version-qualified Properties.

<a id="feat-hso-16"></a>

## Mobile Worker Task Dispatch

Dispatch community care tasks and urgent visits to roving clinicians' mobile devices.

Mobile dispatch exchanges externally established assignment, dispatch and progression information with business mobile-work mechanisms. The retained name does not grant Harmonia authority to allocate clinical work, manage handover or own EMR worklists, and external task terminology does not redefine Synthetic Task.

**Boundary**

The proposed replacement name remains unconfirmed. This entry preserves the existing identity and the human-adjudicated integration boundary.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:mobile-worker-task-dispatch |
| canonicalId | 426363f2f02a4625a4feab89a2adc755 |
| displayName | [Feature] Mobile Worker Task Dispatch |
| friendlyName | HealthServiceOperationsEnablement.MobileStaffManagement.MobileWorkerDispatchAndSafetyCoordination.MobileWorkerTaskDispatch |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-MobileStaffManagement.CT3-BEC-MobileWorkerDispatchAndSafetyCoordination.FEAT-MobileWorkerTaskDispatch |
| Snapshot publication reference | FEAT-HSO-16 |
| ArchiMate semantic ID | id-2bc2b73b45f85084a790267351edc65b |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Mobile Worker Task Dispatch |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.CT3-BEC-MOBILE-WORKER-DISPATCH-SAFETY-COORDINATION.FEAT-HSO-16 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-MOBILE-STAFF-MANAGEMENT.FEAT-HSO-16"] |

Responsibility/framing context: [Mobile Worker Dispatch & Safety Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-mobile-worker-dispatch-safety-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:mobile-worker-task-dispatch`) and the element's version-qualified Properties.

<a id="feat-hso-18"></a>

## Active On-Call Provider Resolution

Resolve currently active on-call specialists, registrars, and operational managers for a given specialty and hospital at any point in time.

Active on-call resolution identifies the current provider in the required specialty and hospital context. It supports correct routing at the relevant time without authoring the roster or treating current coverage as proof of qualification, privilege or responsibility acceptance.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:active-on-call-provider-resolution |
| canonicalId | b588f3cc869d460a9561657352fb189a |
| displayName | [Feature] Active On-Call Provider Resolution |
| friendlyName | HealthServiceOperationsEnablement.OnCallManagement.OnCallAvailabilityAndContactResolution.ActiveOnCallProviderResolution |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OnCallManagement.CT3-BEC-OnCallAvailabilityAndContactResolution.FEAT-ActiveOnCallProviderResolution |
| Snapshot publication reference | FEAT-HSO-18 |
| ArchiMate semantic ID | id-dd333265394a581797c0d16c50ed3f96 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Active On-Call Provider Resolution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-ON-CALL-MANAGEMENT.CT3-BEC-ON-CALL-AVAILABILITY-CONTACT-RESOLUTION.FEAT-HSO-18 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-ON-CALL-MANAGEMENT.FEAT-HSO-18"] |

Responsibility/framing context: [On-Call Availability & Contact Resolution](business-enabling-capabilities.md#hcm-bec-reviewed-on-call-availability-contact-resolution). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:active-on-call-provider-resolution`) and the element's version-qualified Properties.

<a id="hcm-ft-outpatient-appointment-capacity-management"></a>

## Outpatient Appointment Capacity Management

Represent usable outpatient session capacity from booking, staffing and local flow information.

**Overview**

The projection distinguishes scheduled slots from currently usable care capacity without taking source booking authority.

**Boundary**

Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-outpatient-appointment-capacity-management |
| canonicalId | 3c4f8b0fc23044cbbac32602e1272680 |
| displayName | [Feature] Outpatient Appointment Capacity Management |
| friendlyName | HealthServiceOperationsEnablement.OutpatientOperations.OutpatientFlowCoordination.OutpatientAppointmentCapacityManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OutpatientOperations.CT3-BEC-OutpatientFlowCoordination.FEAT-OutpatientAppointmentCapacityManagement |
| Snapshot publication reference | HCM-FT-outpatient-appointment-capacity-management |
| ArchiMate semantic ID | id-e7edec60d944543494e6f3b42c7bf82e |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OUTPATIENT-OPERATIONS.CT3-BEC-OUTPATIENT-FLOW-COORDINATION.FEAT-STEP2-OUTPATIENT-APPOINTMENT-CAPACITY-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-OUTPATIENT-OPERATIONS.FEAT-STEP2-OUTPATIENT-APPOINTMENT-CAPACITY-MANAGEMENT"] |

Responsibility/framing context: [Outpatient Flow Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-outpatient-flow-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-outpatient-appointment-capacity-management`) and the element's version-qualified Properties.

<a id="feat-hso-09"></a>

## Outpatient Arrival Registration

Ingest self-service check-in and clinic arrival events, notifying treating clinicians.

Outpatient arrival registration connects a check-in event with the treating context and required notification. Approved reusable composition supplies this integration responsibility; it does not require a specialised arrival Function or prove that a consultation has begun or finished.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:outpatient-arrival-registration |
| canonicalId | 4ea71fb4950848cfb4f6d916190edad0 |
| displayName | [Feature] Outpatient Arrival Registration |
| friendlyName | HealthServiceOperationsEnablement.OutpatientOperations.OutpatientFlowCoordination.OutpatientArrivalRegistration |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OutpatientOperations.CT3-BEC-OutpatientFlowCoordination.FEAT-OutpatientArrivalRegistration |
| Snapshot publication reference | FEAT-HSO-09 |
| ArchiMate semantic ID | id-b041f0650c57509fa5628fb8fb3bbc7e |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Outpatient Arrival Registration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OUTPATIENT-OPERATIONS.CT3-BEC-OUTPATIENT-FLOW-COORDINATION.FEAT-HSO-09 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-OUTPATIENT-OPERATIONS.FEAT-HSO-09"] |

Responsibility/framing context: [Outpatient Flow Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-outpatient-flow-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:outpatient-arrival-registration`) and the element's version-qualified Properties.

<a id="feat-hso-10"></a>

## Post-Clinic Order Coordination

Capture follow-up booking and diagnostic order requirements generated at clinic completion.

Post-clinic coordination captures the follow-up bookings and diagnostic requirements established at clinic completion. It connects those requirements with subsequent operational activity while preserving that capture is not the clinical decision, a completed booking or diagnostic fulfilment.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:post-clinic-order-coordination |
| canonicalId | 439b3530b7c9443ab3e48d2cc50a3b0c |
| displayName | [Feature] Post-Clinic Order Coordination |
| friendlyName | HealthServiceOperationsEnablement.OutpatientOperations.OutpatientFlowCoordination.PostClinicOrderCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OutpatientOperations.CT3-BEC-OutpatientFlowCoordination.FEAT-PostClinicOrderCoordination |
| Snapshot publication reference | FEAT-HSO-10 |
| ArchiMate semantic ID | id-6d8246a51c6a534cbf9d036ad6b88a08 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Post-Clinic Order Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OUTPATIENT-OPERATIONS.CT3-BEC-OUTPATIENT-FLOW-COORDINATION.FEAT-HSO-10 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-OUTPATIENT-OPERATIONS.FEAT-HSO-10"] |

Responsibility/framing context: [Outpatient Flow Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-outpatient-flow-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:post-clinic-order-coordination`) and the element's version-qualified Properties.

<a id="feat-hso-25"></a>

## Transport Dispatch & Progress Tracking

Dispatch transport orders, tracking departure, transit, and destination arrival checkpoints.

Transport progression connects dispatch, departure, transit and arrival evidence for a physical patient journey. The checkpoints make movement intelligible while preserving that reaching a destination does not establish clinical handover or assumption of responsibility there.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:transport-dispatch--progress-tracking |
| canonicalId | d8ef9994298f466bafce919c5e6cb701 |
| displayName | [Feature] Transport Dispatch & Progress Tracking |
| friendlyName | HealthServiceOperationsEnablement.PatientTransport.PatientTransferCoordination.TransportDispatchAndProgressTracking |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-PatientTransport.CT3-BEC-PatientTransferCoordination.FEAT-TransportDispatchAndProgressTracking |
| Snapshot publication reference | FEAT-HSO-25 |
| ArchiMate semantic ID | id-e4b9833e869b5bb0b75a7d3a05960b80 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Transport Dispatch & Progress Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-PATIENT-TRANSPORT.CT3-BEC-PATIENT-TRANSFER-COORDINATION.FEAT-HSO-25 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-PATIENT-TRANSPORT.FEAT-HSO-25"] |

Responsibility/framing context: [Patient Transfer Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-patient-transfer-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:transport-dispatch--progress-tracking`) and the element's version-qualified Properties.

<a id="feat-hso-24"></a>

## Transport Request Ingestion

Ingest internal (portering) and external (ambulance, inter-hospital) patient transfer requests with clinical mobility requirements.

Transport ingestion establishes the requested movement and applicable clinical mobility constraints. It gives operational coordination the context needed to arrange transit without making the request itself a departure, arrival or accepted care transition.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:transport-request-ingestion |
| canonicalId | 7104472d065840eb91ae4cb7a3a0cb43 |
| displayName | [Feature] Transport Request Ingestion |
| friendlyName | HealthServiceOperationsEnablement.PatientTransport.PatientTransferCoordination.TransportRequestIngestion |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-PatientTransport.CT3-BEC-PatientTransferCoordination.FEAT-TransportRequestIngestion |
| Snapshot publication reference | FEAT-HSO-24 |
| ArchiMate semantic ID | id-8b229766078a5e849660abdac36a765b |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Transport Request Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-PATIENT-TRANSPORT.CT3-BEC-PATIENT-TRANSFER-COORDINATION.FEAT-HSO-24 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-PATIENT-TRANSPORT.FEAT-HSO-24"] |

Responsibility/framing context: [Patient Transfer Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-patient-transfer-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:transport-request-ingestion`) and the element's version-qualified Properties.

<a id="feat-hso-15"></a>

## Operational Capacity Metric Aggregation

Aggregate operational capacity telemetry (occupancy rates, wait times, diversion status) across facilities.

Capacity aggregation combines occupancy, waiting and diversion context across facilities. The higher-order view must retain the originating service-specific capacity meaning; a shared metric does not transfer clinic, ward or theatre capacity responsibility to the aggregator.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:operational-capacity-metric-aggregation |
| canonicalId | 1fe9199a49744eeaa5d063d7a84544ea |
| displayName | [Feature] Operational Capacity Metric Aggregation |
| friendlyName | HealthServiceOperationsEnablement.ServiceCapacityManagement.ServiceDemandAndCapacityCoordination.OperationalCapacityMetricAggregation |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ServiceCapacityManagement.CT3-BEC-ServiceDemandAndCapacityCoordination.FEAT-OperationalCapacityMetricAggregation |
| Snapshot publication reference | FEAT-HSO-15 |
| ArchiMate semantic ID | id-4a05f5487a135f89a097d6c3664a4506 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operational Capacity Metric Aggregation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-SERVICE-CAPACITY-MANAGEMENT.CT3-BEC-SERVICE-DEMAND-CAPACITY-COORDINATION.FEAT-HSO-15 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-CAPACITY-MANAGEMENT.FEAT-HSO-15"] |

Responsibility/framing context: [Service Demand & Capacity Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-service-demand-capacity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:operational-capacity-metric-aggregation`) and the element's version-qualified Properties.

<a id="feat-hso-05"></a>

## Operating Theatre Case Progression

Track surgical case state transitions (CALLED → IN_THEATRE → ANAESTHETISED → INCISION → CLOSED → RECOVERY).

Case progression records externally established surgical milestones in an operationally meaningful sequence. It makes the theatre situation visible without authorising or performing the clinical acts denoted by anaesthesia, incision, closure or recovery.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:operating-theatre-case-progression |
| canonicalId | 2fd65f09f7384f0486280e56ec5a9e84 |
| displayName | [Feature] Operating Theatre Case Progression |
| friendlyName | HealthServiceOperationsEnablement.TheatreOperations.PerioperativeLogisticsCoordination.OperatingTheatreCaseProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-TheatreOperations.CT3-BEC-PerioperativeLogisticsCoordination.FEAT-OperatingTheatreCaseProgression |
| Snapshot publication reference | FEAT-HSO-05 |
| ArchiMate semantic ID | id-b0e1921663045bf5a2297d507ff219fc |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operating Theatre Case Progression |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS.CT3-BEC-PERIOPERATIVE-LOGISTICS-COORDINATION.FEAT-HSO-05 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-THEATRE-OPERATIONS.FEAT-HSO-05"] |

Responsibility/framing context: [Perioperative Logistics Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-perioperative-logistics-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:operating-theatre-case-progression`) and the element's version-qualified Properties.

<a id="feat-hso-06"></a>

## Perioperative Resource Notification

Dispatch alerts to portering, sterilization, and recovery teams based on surgical case progression milestones.

Perioperative notification connects relevant case milestones with portering, sterilisation and recovery participants. Observation alone does not realise the Feature's dispatch responsibility; the established case-progression behaviour includes the required alerts without adding another Function or Service.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:perioperative-resource-notification |
| canonicalId | 1f2446ccc59348a6bc54d8268ef4e6e1 |
| displayName | [Feature] Perioperative Resource Notification |
| friendlyName | HealthServiceOperationsEnablement.TheatreOperations.PerioperativeLogisticsCoordination.PerioperativeResourceNotification |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-TheatreOperations.CT3-BEC-PerioperativeLogisticsCoordination.FEAT-PerioperativeResourceNotification |
| Snapshot publication reference | FEAT-HSO-06 |
| ArchiMate semantic ID | id-48c0126f15ab5a2cbfceae72f135688a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Perioperative Resource Notification |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS.CT3-BEC-PERIOPERATIVE-LOGISTICS-COORDINATION.FEAT-HSO-06 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-THEATRE-OPERATIONS.FEAT-HSO-06"] |

Responsibility/framing context: [Perioperative Logistics Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-perioperative-logistics-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:perioperative-resource-notification`) and the element's version-qualified Properties.

<a id="hcm-ft-theatre-session-capacity-management"></a>

## Theatre Session Capacity Management

Represent available and committed procedural-session capacity with its resource dependencies.

**Overview**

Theatre capacity involves session time, team capability, equipment and recovery provision rather than a count of theatres.

**Boundary**

Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-theatre-session-capacity-management |
| canonicalId | 258c27077caf4bf88e562e8dd607bdfd |
| displayName | [Feature] Theatre Session Capacity Management |
| friendlyName | HealthServiceOperationsEnablement.TheatreOperations.PerioperativeLogisticsCoordination.TheatreSessionCapacityManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-TheatreOperations.CT3-BEC-PerioperativeLogisticsCoordination.FEAT-TheatreSessionCapacityManagement |
| Snapshot publication reference | HCM-FT-theatre-session-capacity-management |
| ArchiMate semantic ID | id-2047568070c354a381514a23e44cd763 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS.CT3-BEC-PERIOPERATIVE-LOGISTICS-COORDINATION.FEAT-STEP2-THEATRE-SESSION-CAPACITY-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-THEATRE-OPERATIONS.FEAT-STEP2-THEATRE-SESSION-CAPACITY-MANAGEMENT"] |

Responsibility/framing context: [Perioperative Logistics Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-perioperative-logistics-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-theatre-session-capacity-management`) and the element's version-qualified Properties.

<a id="feat-sd-08"></a>

## Inpatient Clinical Progression Tracking

Capture inpatient clinical milestone completions (e.g., medical stability achieved, allied health clearance).

Inpatient progression tracking captures clinical milestones reported by the responsible participants. General clinical-information capabilities and configured workflow composition suffice for the established Feature; capture does not require inventing a specialised Function or make Harmonia the authority declaring clinical stability.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:inpatient-clinical-progression-tracking |
| canonicalId | 863265027ecb432595b9c6d45adf1190 |
| displayName | [Feature] Inpatient Clinical Progression Tracking |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.MultidisciplinaryInpatientCareCoordination.InpatientClinicalProgressionTracking |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-MultidisciplinaryInpatientCareCoordination.FEAT-InpatientClinicalProgressionTracking |
| Snapshot publication reference | FEAT-SD-08 |
| ArchiMate semantic ID | id-de2b013d1c5b559297a47c8e839bf849 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Inpatient Clinical Progression Tracking |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-INPATIENT-CARE.FEAT-SD-08 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-INPATIENT-CARE.FEAT-SD-08"] |

Responsibility/framing context: [Multidisciplinary Inpatient Care Coordination](business-enabling-capabilities.md#hcm-bec-inpatient-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:inpatient-clinical-progression-tracking`) and the element's version-qualified Properties.

<a id="feat-sd-07"></a>

## Inpatient Rounding Context Provision

Aggregate diagnostic results, current medication administration records, and nursing notes for multidisciplinary inpatient rounds.

Rounding context brings results, medication records and nursing notes into an accessible multidisciplinary view. Assembly serves the clinical discussion but does not perform rounds, determine pending clinical work or establish the team's professional conclusions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:inpatient-rounding-context-provision |
| canonicalId | c0dfa2ef22354340a4f1488790359bbd |
| displayName | [Feature] Inpatient Rounding Context Provision |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.MultidisciplinaryInpatientCareCoordination.InpatientRoundingContextProvision |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-MultidisciplinaryInpatientCareCoordination.FEAT-InpatientRoundingContextProvision |
| Snapshot publication reference | FEAT-SD-07 |
| ArchiMate semantic ID | id-317d264de57158c7abd59b04b424f8c2 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Inpatient Rounding Context Provision |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-INPATIENT-CARE.FEAT-SD-07 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-INPATIENT-CARE.FEAT-SD-07"] |

Responsibility/framing context: [Multidisciplinary Inpatient Care Coordination](business-enabling-capabilities.md#hcm-bec-inpatient-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:inpatient-rounding-context-provision`) and the element's version-qualified Properties.

<a id="feat-hso-04"></a>

## Clinical Handover Context Assembly

Collate current ward patient lists, active medical concerns, and pending tasks for nursing shift handovers.

Handover context assembly brings patient lists, current concerns and pending tasks together for the participants conducting a shift handover. Assembly is the established responsibility: it does not perform clinical review, decide pending clinical work or manage the handover itself.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:clinical-handover-context-assembly |
| canonicalId | f8c748aa08444723b784887174831d51 |
| displayName | [Feature] Clinical Handover Context Assembly |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.WardLogisticsCoordination.ClinicalHandoverContextAssembly |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-WardLogisticsCoordination.FEAT-ClinicalHandoverContextAssembly |
| Snapshot publication reference | FEAT-HSO-04 |
| ArchiMate semantic ID | id-7323852ae9465ec8ab0f39be5f2fbf0a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Handover Context Assembly |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-WARD-LOGISTICS-COORDINATION.FEAT-HSO-04 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-WARD-OPERATIONS.FEAT-HSO-04"] |

Responsibility/framing context: [Ward Logistics Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-ward-logistics-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:clinical-handover-context-assembly`) and the element's version-qualified Properties.

<a id="hcm-ft-ward-care-capacity-management"></a>

## Ward Care Capacity Management

Represent ward care capacity in relation to staffing, care-place suitability and patient dependency.

**Overview**

A clean bed can remain unusable when staffing or patient needs constrain the ward; source clinical facts remain attributed.

**Boundary**

Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-ward-care-capacity-management |
| canonicalId | 0d45b0afc0584afba4bd0414f5f10e37 |
| displayName | [Feature] Ward Care Capacity Management |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.WardLogisticsCoordination.WardCareCapacityManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-WardLogisticsCoordination.FEAT-WardCareCapacityManagement |
| Snapshot publication reference | HCM-FT-ward-care-capacity-management |
| ArchiMate semantic ID | id-93de013fe0b75f5cbaf7a4275539eb93 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-WARD-LOGISTICS-COORDINATION.FEAT-STEP2-WARD-CARE-CAPACITY-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-WARD-OPERATIONS.FEAT-STEP2-WARD-CARE-CAPACITY-MANAGEMENT"] |

Responsibility/framing context: [Ward Logistics Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-ward-logistics-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-ward-care-capacity-management`) and the element's version-qualified Properties.

<a id="feat-hso-03"></a>

## Ward Patient Flow Coordination

Coordinate inpatient ward occupancy, care-place allocation, and patient bed status across care facilities.

Ward flow coordination connects occupancy, place allocation and bed status within the ward's operational context. Those facts contribute to ward capacity but do not exhaust its acuity, staffing and cohorting responsibilities or prove that another patient can be accepted.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:ward-patient-flow-coordination |
| canonicalId | 011d8c6481264ff58b7866a2c74a235b |
| displayName | [Feature] Ward Patient Flow Coordination |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.WardLogisticsCoordination.WardPatientFlowCoordination |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-WardLogisticsCoordination.FEAT-WardPatientFlowCoordination |
| Snapshot publication reference | FEAT-HSO-03 |
| ArchiMate semantic ID | id-6ff20df32f225115adaf5651c8bc20ce |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Ward Patient Flow Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-WARD-LOGISTICS-COORDINATION.FEAT-HSO-03 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-WARD-OPERATIONS.FEAT-HSO-03"] |

Responsibility/framing context: [Ward Logistics Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-ward-logistics-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:ward-patient-flow-coordination`) and the element's version-qualified Properties.

<a id="feat-hso-21"></a>

## Work Dispatch Delivery

Deliver work orders to target workers, devices, or team queues with technical and operational acknowledgement capture.

Dispatch delivery brings assigned operational work to the intended worker or team and retains acknowledgement evidence. Technical receipt and operational acknowledgement must remain distinguishable; neither alone proves that the work was performed or its intended outcome achieved.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:work-dispatch-delivery |
| canonicalId | 095826f788b64784af38af31bbe77105 |
| displayName | [Feature] Work Dispatch Delivery |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch.OperationalWorkAssignmentAndDispatchCoordination.WorkDispatchDelivery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch.CT3-BEC-OperationalWorkAssignmentAndDispatchCoordination.FEAT-WorkDispatchDelivery |
| Snapshot publication reference | FEAT-HSO-21 |
| ArchiMate semantic ID | id-f3c15ee52d2b5309a14fbf7087a99688 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Work Dispatch Delivery |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.CT3-BEC-OPERATIONAL-WORK-ASSIGNMENT-DISPATCH-COORDINATION.FEAT-HSO-21 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-WORK-ALLOCATION-DISPATCH.FEAT-HSO-21"] |

Responsibility/framing context: [Operational Work Assignment & Dispatch Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-operational-work-assignment-dispatch-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:work-dispatch-delivery`) and the element's version-qualified Properties.

<a id="feat-hso-19"></a>

## Work Item Instantiation

Instantiate operational units of work from clinical triggers (e.g., bed cleaning required, specimen transport required).

Work-item instantiation gives an authorised operational requirement an identifiable unit of work. It distinguishes recognising that support activity is needed from assigning it, dispatching it and recording its completion; a clinical trigger does not make the resulting support item clinical work authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:work-item-instantiation |
| canonicalId | 321fa656c71f4019842b5b991dbc9ff4 |
| displayName | [Feature] Work Item Instantiation |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch.OperationalWorkAssignmentAndDispatchCoordination.WorkItemInstantiation |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch.CT3-BEC-OperationalWorkAssignmentAndDispatchCoordination.FEAT-WorkItemInstantiation |
| Snapshot publication reference | FEAT-HSO-19 |
| ArchiMate semantic ID | id-f63d1e9808aa5ac78f8aab1e05fbb7d0 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Work Item Instantiation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.CT3-BEC-OPERATIONAL-WORK-ASSIGNMENT-DISPATCH-COORDINATION.FEAT-HSO-19 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-WORK-ALLOCATION-DISPATCH.FEAT-HSO-19"] |

Responsibility/framing context: [Operational Work Assignment & Dispatch Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-operational-work-assignment-dispatch-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:work-item-instantiation`) and the element's version-qualified Properties.

<a id="feat-hso-22"></a>

## Work Progress Oversight & Escalation

Monitor work progression against time-to-completion thresholds, escalating stalled or unacknowledged tasks.

Progress oversight identifies stalled or unacknowledged operational work against applicable timing expectations. Escalation serves completion management and does not itself constitute independent assurance or transfer authority over clinical decisions related to the support activity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:work-progress-oversight--escalation |
| canonicalId | abc12faeffd24917aad6748b0f0ca8d7 |
| displayName | [Feature] Work Progress Oversight & Escalation |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch.OperationalWorkAssignmentAndDispatchCoordination.WorkProgressOversightAndEscalation |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch.CT3-BEC-OperationalWorkAssignmentAndDispatchCoordination.FEAT-WorkProgressOversightAndEscalation |
| Snapshot publication reference | FEAT-HSO-22 |
| ArchiMate semantic ID | id-8b77c87dc8ba58d8b154ef1927270e04 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Work Progress Oversight & Escalation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.CT3-BEC-OPERATIONAL-WORK-ASSIGNMENT-DISPATCH-COORDINATION.FEAT-HSO-22 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-WORK-ALLOCATION-DISPATCH.FEAT-HSO-22"] |

Responsibility/framing context: [Operational Work Assignment & Dispatch Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-operational-work-assignment-dispatch-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:work-progress-oversight--escalation`) and the element's version-qualified Properties.

<a id="feat-hso-20"></a>

## Worker Matching & Allocation

Match work items against available staff roles, credentials, and locations based on allocation policies.

Worker matching relates an operational requirement to eligible roles, credentials, locations and availability under policy. It consumes those constraints rather than changing their authoritative records, and does not turn operational allocation into permission to decide clinical work.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:worker-matching--allocation |
| canonicalId | caac8a83dc974cdb9c9bc1b778b9cee7 |
| displayName | [Feature] Worker Matching & Allocation |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch.OperationalWorkAssignmentAndDispatchCoordination.WorkerMatchingAndAllocation |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch.CT3-BEC-OperationalWorkAssignmentAndDispatchCoordination.FEAT-WorkerMatchingAndAllocation |
| Snapshot publication reference | FEAT-HSO-20 |
| ArchiMate semantic ID | id-109077f750945afeb4ab67ed67f204f0 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Worker Matching & Allocation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.CT3-BEC-OPERATIONAL-WORK-ASSIGNMENT-DISPATCH-COORDINATION.FEAT-HSO-20 |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-WORK-ALLOCATION-DISPATCH.FEAT-HSO-20"] |

Responsibility/framing context: [Operational Work Assignment & Dispatch Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-operational-work-assignment-dispatch-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:worker-matching--allocation`) and the element's version-qualified Properties.

<a id="hcm-ft-temporal-assurance-criteria-stewardship"></a>

## Temporal Assurance Criteria Stewardship

Manage identifiable assurance criteria and their relevant temporal and version applicability.

**Overview**

Evidence must be evaluated against the criteria applicable to its purpose and time rather than an unqualified current rule.

**Boundary**

Criteria stewardship does not approve governing requirements or select a universal result taxonomy.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-temporal-assurance-criteria-stewardship |
| canonicalId | e345a4b1eef846a7b77568f519e2c75c |
| displayName | [Feature] Temporal Assurance Criteria Stewardship |
| friendlyName | IndependentAssuranceEnablement.AssuranceSpecification.AssuranceCriteriaAdministration.TemporalAssuranceCriteriaStewardship |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-AssuranceSpecification.CT3-BEC-AssuranceCriteriaAdministration.FEAT-TemporalAssuranceCriteriaStewardship |
| Snapshot publication reference | HCM-FT-temporal-assurance-criteria-stewardship |
| ArchiMate semantic ID | id-e2307fc080bf598bb4a5d37d18533361 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT.CT3-BEC-ASSURANCE-CRITERIA-MANAGEMENT.FEAT-STEP2-TEMPORAL-ASSURANCE-CRITERIA-STEWARDSHIP |

Responsibility/framing context: [Assurance Criteria Administration](business-enabling-capabilities.md#hcm-bec-assurance-criteria-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-temporal-assurance-criteria-stewardship`) and the element's version-qualified Properties.

<a id="hcm-ft-assurance-disposition-design-modelling"></a>

## Assurance Disposition & Design Modelling

Model how satisfaction of an applicable governed concern will be independently established.

**Overview**

This proposal provides a Feature frame for the established design responsibility, including disposition, criteria and evidence expectations, without creating an additional assurance Function.

**Boundary**

Modelling is distinct from requirement approval, subject management and evaluation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-assurance-disposition-design-modelling |
| canonicalId | c36180ebffe44b8db587a00e78b4b358 |
| displayName | [Feature] Assurance Disposition & Design Modelling |
| friendlyName | IndependentAssuranceEnablement.AssuranceSpecification.AssuranceDesign.AssuranceDispositionAndDesignModelling |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-AssuranceSpecification.CT3-BEC-AssuranceDesign.FEAT-AssuranceDispositionAndDesignModelling |
| Snapshot publication reference | HCM-FT-assurance-disposition-design-modelling |
| ArchiMate semantic ID | id-fd8f8171bd2b50e5a5b3b916cbb1d89a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT.CT3-BEC-ASSURANCE-DESIGN.FEAT-STEP2-ASSURANCE-DISPOSITION-DESIGN-MODELLING |

Responsibility/framing context: [Assurance Design](business-enabling-capabilities.md#hcm-bec-assurance-design). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-assurance-disposition-design-modelling`) and the element's version-qualified Properties.

<a id="feat-ise-24"></a>

## Schedule Projection Integration

Project clinician rosters, on-call schedules, and facility operating calendars into operational views.

Schedule projection makes external roster, on-call and facility calendars available in operational views. It preserves their temporal usefulness without making the projection the authoritative schedule or granting authority to alter staffing and coverage decisions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:schedule-projection-integration |
| canonicalId | 883a68ab282b462988d5ae7a6ea2e980 |
| displayName | [Feature] Schedule Projection Integration |
| friendlyName | SharedInformationAndCoordinationEnablement.CalendarManagement.OperationalAndClinicalCalendarCoordination.ScheduleProjectionIntegration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-CalendarManagement.CT3-BEC-OperationalAndClinicalCalendarCoordination.FEAT-ScheduleProjectionIntegration |
| Snapshot publication reference | FEAT-ISE-24 |
| ArchiMate semantic ID | id-aea0b75e918a5827b541770aa620a6f8 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Schedule Projection Integration |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CALENDAR-MANAGEMENT.CT3-BEC-OPERATIONAL-CLINICAL-CALENDAR-COORDINATION.FEAT-ISE-24 |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-CALENDAR-MANAGEMENT.FEAT-ISE-24"] |

Responsibility/framing context: [Operational & Clinical Calendar Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-operational-clinical-calendar-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:schedule-projection-integration`) and the element's version-qualified Properties.

<a id="feat-ise-25"></a>

## Temporal Event Correlation

Correlate incoming clinical events with scheduled appointments and operational calendar milestones.

Temporal correlation relates incoming events to the relevant appointment or calendar milestone. Association provides context for interpretation and coordination without assuming that temporal proximity proves attendance, fulfilment or the clinical outcome expected at that time.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:temporal-event-correlation |
| canonicalId | 05e1c23baf1c47fbac8457f991efa2be |
| displayName | [Feature] Temporal Event Correlation |
| friendlyName | SharedInformationAndCoordinationEnablement.CalendarManagement.OperationalAndClinicalCalendarCoordination.TemporalEventCorrelation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-CalendarManagement.CT3-BEC-OperationalAndClinicalCalendarCoordination.FEAT-TemporalEventCorrelation |
| Snapshot publication reference | FEAT-ISE-25 |
| ArchiMate semantic ID | id-4aeda5a5e90752788a66aa0fd3628ed3 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Temporal Event Correlation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CALENDAR-MANAGEMENT.CT3-BEC-OPERATIONAL-CLINICAL-CALENDAR-COORDINATION.FEAT-ISE-25 |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-CALENDAR-MANAGEMENT.FEAT-ISE-25"] |

Responsibility/framing context: [Operational & Clinical Calendar Coordination](business-enabling-capabilities.md#hcm-bec-reviewed-operational-clinical-calendar-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:temporal-event-correlation`) and the element's version-qualified Properties.

<a id="feat-ise-16"></a>

## Care-Team Clinical Collaboration Coordination

Coordinate secure multidisciplinary clinical communication and consultation exchange bound to patient encounters or clinical topics.

Clinical collaboration coordination binds secure multidisciplinary exchange to the relevant encounter or clinical topic. It supports conversation among authorised participants without making discourse an authoritative observation or acquiring the clinical handover authority excluded by the metamodel.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:care-team-clinical-collaboration-coordination |
| canonicalId | b0860d6b68f34b8cb2f93b25cdf2aa97 |
| displayName | [Feature] Care-Team Clinical Collaboration Coordination |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.ClinicalContextCollaboration.CareTeamClinicalCollaborationCoordination |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.CT3-BEC-ClinicalContextCollaboration.FEAT-CareTeamClinicalCollaborationCoordination |
| Snapshot publication reference | FEAT-ISE-16 |
| ArchiMate semantic ID | id-bdd04a66c8eb54d28f5861d4a750bedf |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Care-Team Clinical Collaboration Coordination |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.CT3-BEC-CLINICAL-CONTEXT-COLLABORATION.FEAT-ISE-16 |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION.FEAT-ISE-16"] |

Responsibility/framing context: [Clinical Context Collaboration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-context-collaboration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:care-team-clinical-collaboration-coordination`) and the element's version-qualified Properties.

<a id="hcm-ft-collaboration-interaction-capture-qualification"></a>

## Collaboration Interaction Capture Qualification

Qualify when a defined collaboration interaction creates a new governed information or activity consequence.

**Overview**

A message may convey existing information or an explicit interaction may derive a new managed consequence. The distinction preserves collaboration-native records while routing governed consequences to their accountable responsibility.

**Boundary**

Conversation presence, display or read receipt does not itself make a clinical decision, approval or final record.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-collaboration-interaction-capture-qualification |
| canonicalId | 8e2a760f15bd454c8aba1526ee69740c |
| displayName | [Feature] Collaboration Interaction Capture Qualification |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.ClinicalContextCollaboration.CollaborationInteractionCaptureQualification |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.CT3-BEC-ClinicalContextCollaboration.FEAT-CollaborationInteractionCaptureQualification |
| Snapshot publication reference | HCM-FT-collaboration-interaction-capture-qualification |
| ArchiMate semantic ID | id-75a499a861ff5b1ba5293910bda53633 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.CT3-BEC-CLINICAL-CONTEXT-COLLABORATION.FEAT-STEP2-COLLABORATION-INTERACTION-CAPTURE-QUALIFICATION |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION.FEAT-STEP2-COLLABORATION-INTERACTION-CAPTURE-QUALIFICATION"] |

Responsibility/framing context: [Clinical Context Collaboration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-context-collaboration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-collaboration-interaction-capture-qualification`) and the element's version-qualified Properties.

<a id="feat-ise-17"></a>

## In-Conversation LHR Query Resolution

Resolve clinical data queries within an authorized collaboration context, presenting verified clinical summaries directly to care team members.

In-conversation query supplies governed clinical summaries within an authorised collaboration context. The convenience of retrieving information during discussion does not weaken consent or access controls, and displaying a summary does not promote the surrounding conversation into the clinical record.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:in-conversation-lhr-query-resolution |
| canonicalId | d4437b477aab42c79b5f45150c5d7f3f |
| displayName | [Feature] In-Conversation LHR Query Resolution |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.ClinicalContextCollaboration.InConversationLhrQueryResolution |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.CT3-BEC-ClinicalContextCollaboration.FEAT-InConversationLhrQueryResolution |
| Snapshot publication reference | FEAT-ISE-17 |
| ArchiMate semantic ID | id-07ba91e1f3105439b697ef7dbce26f82 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | In-Conversation LHR Query Resolution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.CT3-BEC-CLINICAL-CONTEXT-COLLABORATION.FEAT-ISE-17 |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION.FEAT-ISE-17"] |

Responsibility/framing context: [Clinical Context Collaboration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-context-collaboration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:in-conversation-lhr-query-resolution`) and the element's version-qualified Properties.

<a id="feat-ise-18"></a>

## Zero-PHI Collaboration Metadata Governance

Ensure collaboration channel titles, topics, and aliases use opaque identifiers free of patient names, MRNs, or sensitive details.

Metadata governance keeps patient names, identifiers and sensitive detail out of collaboration titles, topics and aliases. Opaque references preserve useful contextual association without placing PHI in broadly visible channel metadata or diagnostic surfaces.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:zero-phi-collaboration-metadata-governance |
| canonicalId | 87e9246c944a40d9af5c4d0a3a7d4e9a |
| displayName | [Feature] Zero-PHI Collaboration Metadata Governance |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.ClinicalContextCollaboration.ZeroPhiCollaborationMetadataGovernance |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.CT3-BEC-ClinicalContextCollaboration.FEAT-ZeroPhiCollaborationMetadataGovernance |
| Snapshot publication reference | FEAT-ISE-18 |
| ArchiMate semantic ID | id-1f6e2bc8338f5658869a5957b235f40f |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Zero-PHI Collaboration Metadata Governance |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.CT3-BEC-CLINICAL-CONTEXT-COLLABORATION.FEAT-ISE-18 |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION.FEAT-ISE-18"] |

Responsibility/framing context: [Clinical Context Collaboration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-context-collaboration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:zero-phi-collaboration-metadata-governance`) and the element's version-qualified Properties.

<a id="feat-ise-15"></a>

## Bidirectional Terminology Mapping

Translate between legacy local code systems and canonical national healthcare profiles.

Terminology mapping applies governed correspondence between local and canonical semantic systems. Translation preserves the limitations and authority of the map; it does not justify silently treating related source concepts as identical or clinically adjudicating the underlying information.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:bidirectional-terminology-mapping |
| canonicalId | 6134dc91af8d45e8b81cc28d18a5251f |
| displayName | [Feature] Bidirectional Terminology Mapping |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.ClinicalKnowledgeRequestAdministration.BidirectionalTerminologyMapping |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.CT3-BEC-ClinicalKnowledgeRequestAdministration.FEAT-BidirectionalTerminologyMapping |
| Snapshot publication reference | FEAT-ISE-15 |
| ArchiMate semantic ID | id-1116384ed5cc5bd3a0d28aa12a00d5cc |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bidirectional Terminology Mapping |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-CLINICAL-KNOWLEDGE-REQUEST-ADMINISTRATION.FEAT-ISE-15 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.FEAT-ISE-15","CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-CLINICAL-KNOWLEDGE-SERVICES.FEAT-ISE-15"] |

Responsibility/framing context: [Clinical Knowledge Request Administration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-knowledge-request-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:bidirectional-terminology-mapping`) and the element's version-qualified Properties.

<a id="feat-ise-14"></a>

## Clinical Concept Resolution

Resolve local clinical codes against standard terminologies (SNOMED CT, LOINC, ICD-10).

Concept resolution makes a coded clinical reference interpretable against its governed terminology. It validates the reference context without making a recognised code proof of clinical truth or giving the consuming system authority to redefine the concept.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:clinical-concept-resolution |
| canonicalId | 30cdb9b2a810467c9b56e9870de4367e |
| displayName | [Feature] Clinical Concept Resolution |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.ClinicalKnowledgeRequestAdministration.ClinicalConceptResolution |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.CT3-BEC-ClinicalKnowledgeRequestAdministration.FEAT-ClinicalConceptResolution |
| Snapshot publication reference | FEAT-ISE-14 |
| ArchiMate semantic ID | id-e97a4d5858585c9a89e93fb9321aba7c |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Concept Resolution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-CLINICAL-KNOWLEDGE-REQUEST-ADMINISTRATION.FEAT-ISE-14 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.FEAT-ISE-14","CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-CLINICAL-KNOWLEDGE-SERVICES.FEAT-ISE-14"] |

Responsibility/framing context: [Clinical Knowledge Request Administration](business-enabling-capabilities.md#hcm-bec-reviewed-clinical-knowledge-request-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:clinical-concept-resolution`) and the element's version-qualified Properties.

<a id="hcm-ft-governed-dataset-content-revision"></a>

## Governed Dataset Content Revision

Review, accept and expose authorised changes to mutable reference content with effective revision standing.

**Overview**

A model definition can remain stable while approved terminology membership or mapping content changes. This Feature represents that operational governance without making every definition runtime mutable.

**Boundary**

Does not invent an approval authority or bypass normal durable establishment.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-governed-dataset-content-revision |
| canonicalId | 89123c868b8a46c38800916110cd918d |
| displayName | [Feature] Governed Dataset Content Revision |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.GovernedReferenceDatasetAdministration.GovernedDatasetContentRevision |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.CT3-BEC-GovernedReferenceDatasetAdministration.FEAT-GovernedDatasetContentRevision |
| Snapshot publication reference | HCM-FT-governed-dataset-content-revision |
| ArchiMate semantic ID | id-a41954fb90ab5744a24d14428d45c29a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.FEAT-STEP2-GOVERNED-DATASET-CONTENT-REVISION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.FEAT-STEP2-GOVERNED-DATASET-CONTENT-REVISION"] |

Responsibility/framing context: [Governed Reference Dataset Administration](business-enabling-capabilities.md#hcm-bec-governed-reference-dataset-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-governed-dataset-content-revision`) and the element's version-qualified Properties.

<a id="feat-ise-19"></a>

## Operational Team Chat Enablement

Provide secure instant messaging channels for departmental, administrative, and facility support staff.

Operational chat provides secure communication for administrative and support teams. It enables participants to discuss work while leaving assignment, completion and the authority of operational decisions with the capabilities responsible for that activity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:operational-team-chat-enablement |
| canonicalId | 367a390e7a464003beba189428c5faca |
| displayName | [Feature] Operational Team Chat Enablement |
| friendlyName | SharedInformationAndCoordinationEnablement.GeneralCollaboration.OperationalCollaboration.OperationalTeamChatEnablement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-GeneralCollaboration.CT3-BEC-OperationalCollaboration.FEAT-OperationalTeamChatEnablement |
| Snapshot publication reference | FEAT-ISE-19 |
| ArchiMate semantic ID | id-47a6974fa53d5efdb0ec6a3f36f31d1c |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operational Team Chat Enablement |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-GENERAL-COLLABORATION.CT3-BEC-OPERATIONAL-COLLABORATION.FEAT-ISE-19 |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-GENERAL-COLLABORATION.FEAT-ISE-19"] |

Responsibility/framing context: [Operational Collaboration](business-enabling-capabilities.md#hcm-bec-reviewed-operational-collaboration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:operational-team-chat-enablement`) and the element's version-qualified Properties.

<a id="feat-ise-09"></a>

## Access-Controlled Information Filtering

Filter search results dynamically according to requesting user authorizations and patient consent directives.

Access-qualified filtering restricts the disclosed result to the verified caller's authority and applicable consent. The responsibility is more specific than retrieving a resource; the latter's shared owner and subject matter do not establish a Feature realisation relationship.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:access-controlled-information-filtering |
| canonicalId | a9f1d2100faa44beb69add62bb4f4b3f |
| displayName | [Feature] Access-Controlled Information Filtering |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationAccess.GovernedHealthInformationRetrieval.AccessControlledInformationFiltering |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationAccess.CT3-BEC-GovernedHealthInformationRetrieval.FEAT-AccessControlledInformationFiltering |
| Snapshot publication reference | FEAT-ISE-09 |
| ArchiMate semantic ID | id-05263c5deb1d5ddfb4cbca4fad307bbd |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Access-Controlled Information Filtering |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-ACCESS.CT3-BEC-GOVERNED-HEALTH-INFORMATION-RETRIEVAL.FEAT-ISE-09 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-ACCESS.FEAT-ISE-09"] |

Responsibility/framing context: [Governed Health Information Retrieval](business-enabling-capabilities.md#hcm-bec-reviewed-governed-health-information-retrieval). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:access-controlled-information-filtering`) and the element's version-qualified Properties.

<a id="feat-ise-08"></a>

## Federated Clinical Query

Execute bounded, indexed queries across distributed clinical resources and directory graphs.

Federated query makes relevant clinical and directory information discoverable across distributed contexts using bounded criteria. Discovery is a governed information-access responsibility rather than authority to disclose every match or reinterpret the clinical meaning of the results.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:federated-clinical-query |
| canonicalId | cfcc4d64af434418a567ccf46d8052dd |
| displayName | [Feature] Federated Clinical Query |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationAccess.GovernedHealthInformationRetrieval.FederatedClinicalQuery |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationAccess.CT3-BEC-GovernedHealthInformationRetrieval.FEAT-FederatedClinicalQuery |
| Snapshot publication reference | FEAT-ISE-08 |
| ArchiMate semantic ID | id-7750de806a73576bba5905eebbc9d550 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Federated Clinical Query |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-ACCESS.CT3-BEC-GOVERNED-HEALTH-INFORMATION-RETRIEVAL.FEAT-ISE-08 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-ACCESS.FEAT-ISE-08"] |

Responsibility/framing context: [Governed Health Information Retrieval](business-enabling-capabilities.md#hcm-bec-reviewed-governed-health-information-retrieval). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:federated-clinical-query`) and the element's version-qualified Properties.

<a id="feat-ise-11"></a>

## Health Information Access & Consent Control

Evaluate patient consent preferences, practitioner authority boundaries, and access control policies for managed clinical records.

Access and consent control evaluates whether an information operation is legitimate in its practitioner, patient and purpose context. It supplies policy determinations consumed by access and presentation paths rather than allowing those consumers to establish authority from caller-supplied content.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:health-information-access--consent-control |
| canonicalId | e2643fa59303401abb1b778f3295e536 |
| displayName | [Feature] Health Information Access & Consent Control |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.PatientHealthInformationRestrictionManagement.HealthInformationAccessAndConsentControl |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.CT3-BEC-PatientHealthInformationRestrictionManagement.FEAT-HealthInformationAccessAndConsentControl |
| Snapshot publication reference | FEAT-ISE-11 |
| ArchiMate semantic ID | id-75e742fb555b5d699b57e2ffe2237290 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Health Information Access & Consent Control |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.CT3-BEC-PATIENT-HEALTH-INFORMATION-RESTRICTION-MANAGEMENT.FEAT-ISE-11 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FEAT-ISE-11"] |

Responsibility/framing context: [Patient Health Information Restriction Management](business-enabling-capabilities.md#hcm-bec-reviewed-patient-health-information-restriction-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:health-information-access--consent-control`) and the element's version-qualified Properties.

<a id="feat-ise-06"></a>

## Addressed Clinical Distribution

Fan out clinical events and diagnostic reports to designated external and internal recipients with per-destination delivery tracking.

Addressed distribution sends information to explicitly designated recipients and tracks each destination's delivery separately. This preserves partial outcomes in a fan-out rather than collapsing several deliveries into a single success that hides a missing recipient.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:addressed-clinical-distribution |
| canonicalId | b476c86edab644c394da33d3de7bd0bd |
| displayName | [Feature] Addressed Clinical Distribution |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieImplementationManagement.AddressedClinicalDistribution |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieImplementationManagement.FEAT-AddressedClinicalDistribution |
| Snapshot publication reference | FEAT-ISE-06 |
| ArchiMate semantic ID | id-58f6fa48f0e35e7dab2554c31ac16497 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Addressed Clinical Distribution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.CT3-BEC-HIE-IMPLEMENTATION-MANAGEMENT.FEAT-ISE-06 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FEAT-ISE-06"] |

Responsibility/framing context: [HIE Implementation Management](business-enabling-capabilities.md#hcm-bec-reviewed-hie-implementation-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:addressed-clinical-distribution`) and the element's version-qualified Properties.

<a id="feat-ise-05"></a>

## Clinical Record Retrieval Mediation

Mediate query-based retrieval requests across external and internal repositories.

Retrieval mediation connects a request with information held in participating repositories. It makes distributed retrieval possible without transferring repository authority, acquiring the clinical payload or treating a resolvable request as automatic permission to disclose.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:clinical-record-retrieval-mediation |
| canonicalId | 6f4a9c8f7916488bb0dd838ccccb1df4 |
| displayName | [Feature] Clinical Record Retrieval Mediation |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieImplementationManagement.ClinicalRecordRetrievalMediation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieImplementationManagement.FEAT-ClinicalRecordRetrievalMediation |
| Snapshot publication reference | FEAT-ISE-05 |
| ArchiMate semantic ID | id-013cb98a363e5db5a88c0f3425a5921c |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Record Retrieval Mediation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.CT3-BEC-HIE-IMPLEMENTATION-MANAGEMENT.FEAT-ISE-05 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FEAT-ISE-05"] |

Responsibility/framing context: [HIE Implementation Management](business-enabling-capabilities.md#hcm-bec-reviewed-hie-implementation-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:clinical-record-retrieval-mediation`) and the element's version-qualified Properties.

<a id="feat-ise-04"></a>

## Direct Submission Ingestion & Delivery

Support push submissions of clinical documents and messages across boundaries.

Direct submission supports a sender's deliberate push of information across an exchange boundary. It preserves the distinction between ingress or delivery acceptance and the recipient's business handling, under the actual contract rather than an assumed meaning of transport success.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:direct-submission-ingestion--delivery |
| canonicalId | e8bfad88315c4833b6772ef580ca55a5 |
| displayName | [Feature] Direct Submission Ingestion & Delivery |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieImplementationManagement.DirectSubmissionIngestionAndDelivery |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieImplementationManagement.FEAT-DirectSubmissionIngestionAndDelivery |
| Snapshot publication reference | FEAT-ISE-04 |
| ArchiMate semantic ID | id-faf9309586445b52ad791069de388e1a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Direct Submission Ingestion & Delivery |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.CT3-BEC-HIE-IMPLEMENTATION-MANAGEMENT.FEAT-ISE-04 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FEAT-ISE-04"] |

Responsibility/framing context: [HIE Implementation Management](business-enabling-capabilities.md#hcm-bec-reviewed-hie-implementation-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:direct-submission-ingestion--delivery`) and the element's version-qualified Properties.

<a id="feat-ise-07"></a>

## Event-Driven Syndication

Publish clinical and directory change events to subscribed systems matching interest filters.

Syndication connects published changes with subscribers whose registered interests match them. It differs from named-recipient distribution in how consumers are selected, while retaining governed delivery and the distinction between notification and the subscriber's eventual business action.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:event-driven-syndication |
| canonicalId | 30bb720ed4e64a47a1fcf3be811c1931 |
| displayName | [Feature] Event-Driven Syndication |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieImplementationManagement.EventDrivenSyndication |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieImplementationManagement.FEAT-EventDrivenSyndication |
| Snapshot publication reference | FEAT-ISE-07 |
| ArchiMate semantic ID | id-800daf9a3a695e5ab10e449abc4b96bc |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Event-Driven Syndication |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.CT3-BEC-HIE-IMPLEMENTATION-MANAGEMENT.FEAT-ISE-07 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FEAT-ISE-07"] |

Responsibility/framing context: [HIE Implementation Management](business-enabling-capabilities.md#hcm-bec-reviewed-hie-implementation-management). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:event-driven-syndication`) and the element's version-qualified Properties.

<a id="hcm-ft-information-transformation-lineage"></a>

## Information Transformation Lineage

Record meaningful source-to-derived information relationships and transformation attribution.

**Overview**

A consumer should be able to explain which source assertions contributed to a managed representation and what significant transformation occurred.

**Boundary**

Routine cache reconstruction, retries and diagnostics do not automatically become permanent lineage.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-information-transformation-lineage |
| canonicalId | cc47f49b85a34e87bafc49de7f0426e2 |
| displayName | [Feature] Information Transformation Lineage |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationContentMaintainerManagement.DataProvenanceCapture.InformationTransformationLineage |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationContentMaintainerManagement.CT3-BEC-DataProvenanceCapture.FEAT-InformationTransformationLineage |
| Snapshot publication reference | HCM-FT-information-transformation-lineage |
| ArchiMate semantic ID | id-e689c5ca77d256e2b55e60bffbaec3db |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-LINEAGE-STEWARDSHIP.FEAT-STEP2-INFORMATION-TRANSFORMATION-LINEAGE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-INFORMATION-LINEAGE-STEWARDSHIP.FEAT-STEP2-INFORMATION-TRANSFORMATION-LINEAGE"] |

Responsibility/framing context: [Data Provenance Capture](business-enabling-capabilities.md#hcm-bec-information-lineage-stewardship). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-information-transformation-lineage`) and the element's version-qualified Properties.

<a id="hcm-ft-authorised-research-release-assembly"></a>

## Authorised Research Release Assembly

Assemble and qualify the existing information allowed by a particular authorised research purpose.

**Overview**

The release carries purpose and applicable privacy/consent/use constraints; the requester does not gain unrestricted clinical-record access by having a research role.

**Boundary**

No ethics approval, de-identification guarantee or clinical-trial management is inferred.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-authorised-research-release-assembly |
| canonicalId | dcebc2f2b5b7494a8e80a02402664078 |
| displayName | [Feature] Authorised Research Release Assembly |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationContentMaintainerManagement.InformationProductAdministration.AuthorisedResearchReleaseAssembly |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationContentMaintainerManagement.CT3-BEC-InformationProductAdministration.FEAT-AuthorisedResearchReleaseAssembly |
| Snapshot publication reference | HCM-FT-authorised-research-release-assembly |
| ArchiMate semantic ID | id-91835e68ae02530786223c71a4acaa39 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-PRODUCT-ADMINISTRATION.FEAT-STEP2-AUTHORISED-RESEARCH-RELEASE-ASSEMBLY |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT3-BEC-AUTHORISED-RESEARCH-INFORMATION-PROVISION.FEAT-STEP2-AUTHORISED-RESEARCH-RELEASE-ASSEMBLY"] |

Responsibility/framing context: [Information Product Administration](business-enabling-capabilities.md#hcm-bec-reviewed-information-product-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-authorised-research-release-assembly`) and the element's version-qualified Properties.

<a id="hcm-ft-demand-capacity-planning-projection"></a>

## Demand & Capacity Planning Projection

Project qualified observed demand, capacity and utilisation for authorised planning consumers.

**Overview**

Comparisons need service, temporal and context qualifiers so that planned future capacity is not confused with present operational capacity.

**Boundary**

Projection does not establish a planning conclusion or allocate services and staff.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-demand-capacity-planning-projection |
| canonicalId | 4f97baa3aed24b8385bf3ecda4eab622 |
| displayName | [Feature] Demand & Capacity Planning Projection |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationContentMaintainerManagement.InformationProductAdministration.DemandAndCapacityPlanningProjection |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationContentMaintainerManagement.CT3-BEC-InformationProductAdministration.FEAT-DemandAndCapacityPlanningProjection |
| Snapshot publication reference | HCM-FT-demand-capacity-planning-projection |
| ArchiMate semantic ID | id-1bd07e714962537590dc80ad2f2c7acd |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-PRODUCT-ADMINISTRATION.FEAT-STEP2-DEMAND-CAPACITY-PLANNING-PROJECTION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT3-BEC-SERVICE-PLANNING-INFORMATION-PROVISION.FEAT-STEP2-DEMAND-CAPACITY-PLANNING-PROJECTION"] |

Responsibility/framing context: [Information Product Administration](business-enabling-capabilities.md#hcm-bec-reviewed-information-product-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-demand-capacity-planning-projection`) and the element's version-qualified Properties.

<a id="hcm-ft-population-notification-obligation-tracking"></a>

## Population Notification Obligation Tracking

Track the relation between an applicable notification obligation, the supplied source evidence and observed notification disposition.

**Overview**

Sending a required notification and discharging the governing obligation are different assertions. This Feature keeps the explicit contract and indeterminate responses visible.

**Boundary**

Does not define notifiability, author statutory rules or own public-health response.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-population-notification-obligation-tracking |
| canonicalId | 62f6a91a10684876b7d08b536700f12f |
| displayName | [Feature] Population Notification Obligation Tracking |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationContentMaintainerManagement.InformationProductAdministration.PopulationNotificationObligationTracking |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationContentMaintainerManagement.CT3-BEC-InformationProductAdministration.FEAT-PopulationNotificationObligationTracking |
| Snapshot publication reference | HCM-FT-population-notification-obligation-tracking |
| ArchiMate semantic ID | id-b4ddb18a42815f51909df742fbb3f753 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-PRODUCT-ADMINISTRATION.FEAT-STEP2-POPULATION-NOTIFICATION-OBLIGATION-TRACKING |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT3-BEC-POPULATION-HEALTH-NOTIFICATION-ENABLEMENT.FEAT-STEP2-POPULATION-NOTIFICATION-OBLIGATION-TRACKING"] |

Responsibility/framing context: [Information Product Administration](business-enabling-capabilities.md#hcm-bec-reviewed-information-product-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-population-notification-obligation-tracking`) and the element's version-qualified Properties.

<a id="hcm-ft-exchange-profile-governance"></a>

## Exchange Profile Governance

Govern the semantic and conformance constraints of an interoperability profile.

**Overview**

An external contract must state which representations and extensions are permitted; validation applies that contract but does not define it.

**Boundary**

Profile governance does not transmit messages or publish internal operational metadata.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-exchange-profile-governance |
| canonicalId | 5e59d8f2e23e4fb5b59a5bd1dd3f31fa |
| displayName | [Feature] Exchange Profile Governance |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseCanonicalReusableModelDesign.ExchangeProfileGovernance |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseCanonicalReusableModelDesign.FEAT-ExchangeProfileGovernance |
| Snapshot publication reference | HCM-FT-exchange-profile-governance |
| ArchiMate semantic ID | id-2541fb40968a5bf4b307b0c83bd8202b |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-DESIGN.FEAT-STEP2-EXCHANGE-PROFILE-GOVERNANCE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.FEAT-STEP2-EXCHANGE-PROFILE-GOVERNANCE"] |

Responsibility/framing context: [Enterprise Canonical/Reusable Model Design](business-enabling-capabilities.md#hcm-bec-reviewed-enterprise-canonical-reusable-model-design). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-exchange-profile-governance`) and the element's version-qualified Properties.

<a id="hcm-ft-information-definition-schema-governance"></a>

## Information Definition & Schema Governance

Govern structural definitions, data dictionaries and constraint meaning.

**Overview**

Existing definition-governance behaviour needs a distinct Feature frame from general semantic-model authorship and runtime validation.

**Boundary**

Governed definitions do not own runtime instances or their clinical facts.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-information-definition-schema-governance |
| canonicalId | 3317bbb7e2db4384abf44a50bf67422c |
| displayName | [Feature] Information Definition & Schema Governance |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseCanonicalReusableModelDesign.InformationDefinitionAndSchemaGovernance |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseCanonicalReusableModelDesign.FEAT-InformationDefinitionAndSchemaGovernance |
| Snapshot publication reference | HCM-FT-information-definition-schema-governance |
| ArchiMate semantic ID | id-087c246587f15ff0b9554654eb508fd2 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-DESIGN.FEAT-STEP2-INFORMATION-DEFINITION-SCHEMA-GOVERNANCE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.FEAT-STEP2-INFORMATION-DEFINITION-SCHEMA-GOVERNANCE"] |

Responsibility/framing context: [Enterprise Canonical/Reusable Model Design](business-enabling-capabilities.md#hcm-bec-reviewed-enterprise-canonical-reusable-model-design). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-information-definition-schema-governance`) and the element's version-qualified Properties.

<a id="feat-ise-27"></a>

## Information Standards & Semantic Governance

Govern canonical clinical data definitions, terminology bindings, and exchange profiles across regional systems.

Semantic governance provides canonical definitions, bindings and profiles through which regional representations can be understood consistently. Its purpose concerns meaning and constraints rather than a particular schema technology, and consuming a definition does not transfer its governing authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:information-standards--semantic-governance |
| canonicalId | af5d5249302f47ebb393c91afcc60392 |
| displayName | [Feature] Information Standards & Semantic Governance |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseCanonicalReusableModelDesign.InformationStandardsAndSemanticGovernance |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseCanonicalReusableModelDesign.FEAT-InformationStandardsAndSemanticGovernance |
| Snapshot publication reference | FEAT-ISE-27 |
| ArchiMate semantic ID | id-40a0a237e02e58769e19d6c4f13cdf8d |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Information Standards & Semantic Governance |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-DESIGN.FEAT-ISE-27 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.FEAT-ISE-27"] |

Responsibility/framing context: [Enterprise Canonical/Reusable Model Design](business-enabling-capabilities.md#hcm-bec-reviewed-enterprise-canonical-reusable-model-design). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:information-standards--semantic-governance`) and the element's version-qualified Properties.

<a id="hcm-ft-terminology-binding-governance"></a>

## Terminology Binding Governance

Govern how information definitions are bound to applicable terminology and value sets.

**Overview**

Binding a data element to a vocabulary is distinct from resolving a patient code or changing the runtime membership of a dataset.

**Boundary**

Definition of a binding does not take external ontology authority or execute policy.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-terminology-binding-governance |
| canonicalId | b999df692ca64010b4adeedbcb0d3a5a |
| displayName | [Feature] Terminology Binding Governance |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseCanonicalReusableModelDesign.TerminologyBindingGovernance |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseCanonicalReusableModelDesign.FEAT-TerminologyBindingGovernance |
| Snapshot publication reference | HCM-FT-terminology-binding-governance |
| ArchiMate semantic ID | id-ebc8336c54825ec881877a648f360f9e |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-DESIGN.FEAT-STEP2-TERMINOLOGY-BINDING-GOVERNANCE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.FEAT-STEP2-TERMINOLOGY-BINDING-GOVERNANCE"] |

Responsibility/framing context: [Enterprise Canonical/Reusable Model Design](business-enabling-capabilities.md#hcm-bec-reviewed-enterprise-canonical-reusable-model-design). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-terminology-binding-governance`) and the element's version-qualified Properties.

<a id="feat-ise-28"></a>

## Semantic Conformance Verification

Verify inbound and outbound representations against published semantic rules and conformance constraints.

Conformance verification evaluates a representation against published semantic rules and constraints. It distinguishes meaningful conformity from simple parseability while retaining that a conformant message is not necessarily clinically true, authoritative or permitted for disclosure.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:semantic-conformance-verification |
| canonicalId | d170a68efcae4aa59350f565ca69443a |
| displayName | [Feature] Semantic Conformance Verification |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseCanonicalReusableModelUtilisationSupport.SemanticConformanceVerification |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseCanonicalReusableModelUtilisationSupport.FEAT-SemanticConformanceVerification |
| Snapshot publication reference | FEAT-ISE-28 |
| ArchiMate semantic ID | id-4d0ca08bf6b4510c8171c5bd2e4e62f7 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Semantic Conformance Verification |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-UTILISATION-SUPPORT.FEAT-ISE-28 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.FEAT-ISE-28"] |

Responsibility/framing context: [Enterprise Canonical/Reusable Model Utilisation Support](business-enabling-capabilities.md#hcm-bec-reviewed-enterprise-canonical-reusable-model-utilisation-support). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:semantic-conformance-verification`) and the element's version-qualified Properties.

<a id="hcm-ft-binary-content-resolution-assembly"></a>

## Binary Content Resolution & Assembly

Resolve governed payload associations and retrieve or assemble the content required by a requesting information context.

**Overview**

A logical clinical item may reference externally held or multipart payloads. The Feature supplies a usable content result while retaining custody and missing-content standing.

**Boundary**

No assumption of local copying, shared storage, universal content retention or originating clinical authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-binary-content-resolution-assembly |
| canonicalId | 8fb4036f7a9c47c3883b2507528fab5e |
| displayName | [Feature] Binary Content Resolution & Assembly |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LinkedInformationCuration.BinaryContentResolutionAndAssembly |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LinkedInformationCuration.FEAT-BinaryContentResolutionAndAssembly |
| Snapshot publication reference | HCM-FT-binary-content-resolution-assembly |
| ArchiMate semantic ID | id-e3c67fe6821e5511965aa45e07d9d354 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.FEAT-STEP2-BINARY-CONTENT-RESOLUTION-ASSEMBLY |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.FEAT-STEP2-BINARY-CONTENT-RESOLUTION-ASSEMBLY"] |

Responsibility/framing context: [Linked Information Curation](business-enabling-capabilities.md#hcm-bec-associated-clinical-binary-content-enablement). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-binary-content-resolution-assembly`) and the element's version-qualified Properties.

<a id="feat-ise-02"></a>

## Active Clinical Record Access

Provide low-latency, active distributed access to consolidated patient clinical summaries.

Active access supports responsive use of consolidated clinical summaries in ongoing care. The working representation remains reconstructable active state; speed and availability do not promote it to authoritative durable truth when persistence or convergence is unavailable.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:active-clinical-record-access |
| canonicalId | 76b35ecac42b4781bfd7911c9931f5f1 |
| displayName | [Feature] Active Clinical Record Access |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LongitudinalClinicalRecordAdministration.ActiveClinicalRecordAccess |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LongitudinalClinicalRecordAdministration.FEAT-ActiveClinicalRecordAccess |
| Snapshot publication reference | FEAT-ISE-02 |
| ArchiMate semantic ID | id-bfcb6625f31c552abf47156256316200 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Active Clinical Record Access |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-LONGITUDINAL-CLINICAL-RECORD-ADMINISTRATION.FEAT-ISE-02 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD.FEAT-ISE-02"] |

Responsibility/framing context: [Longitudinal Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-reviewed-longitudinal-clinical-record-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:active-clinical-record-access`) and the element's version-qualified Properties.

<a id="hcm-ft-clinical-information-historical-reconstruction"></a>

## Clinical Information Historical Reconstruction

Reconstruct what information Harmonia received, knew or managed at a relevant historical basis.

**Overview**

Current state and historical operational truth answer different questions. Reconstruction retains superseded and corrected knowledge without rewriting the earlier record.

**Boundary**

Does not infer that historical information was clinically true or that a present source state applied in the past.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-clinical-information-historical-reconstruction |
| canonicalId | 838728ef4fd04b4da0c28dc4fce0cfe9 |
| displayName | [Feature] Clinical Information Historical Reconstruction |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LongitudinalClinicalRecordAdministration.ClinicalInformationHistoricalReconstruction |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LongitudinalClinicalRecordAdministration.FEAT-ClinicalInformationHistoricalReconstruction |
| Snapshot publication reference | HCM-FT-clinical-information-historical-reconstruction |
| ArchiMate semantic ID | id-aac71d2b16d550e4b694338cee216d5a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-LONGITUDINAL-CLINICAL-RECORD-ADMINISTRATION.FEAT-STEP2-CLINICAL-INFORMATION-HISTORICAL-RECONSTRUCTION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD.FEAT-STEP2-CLINICAL-INFORMATION-HISTORICAL-RECONSTRUCTION"] |

Responsibility/framing context: [Longitudinal Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-reviewed-longitudinal-clinical-record-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-clinical-information-historical-reconstruction`) and the element's version-qualified Properties.

<a id="feat-ise-03"></a>

## Durable Clinical Record Preservation

Durably maintain governed, vendor-neutral longitudinal clinical representation and preservation with verifiable integrity controls.

Durable preservation retains the governed longitudinal representation and the history of what Harmonia received or managed. Corrections establish changed knowledge without retrospectively rewriting that history, and preservation neither originates clinical facts nor automatically establishes legal qualification of the record.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:durable-clinical-record-preservation |
| canonicalId | aba48dbf729d477aa87e112725dc1511 |
| displayName | [Feature] Durable Clinical Record Preservation |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LongitudinalClinicalRecordAdministration.DurableClinicalRecordPreservation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LongitudinalClinicalRecordAdministration.FEAT-DurableClinicalRecordPreservation |
| Snapshot publication reference | FEAT-ISE-03 |
| ArchiMate semantic ID | id-6116f5faabba5d65b7a1e94486ea164c |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Durable Clinical Record Preservation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-LONGITUDINAL-CLINICAL-RECORD-ADMINISTRATION.FEAT-ISE-03 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD.FEAT-ISE-03"] |

Responsibility/framing context: [Longitudinal Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-reviewed-longitudinal-clinical-record-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:durable-clinical-record-preservation`) and the element's version-qualified Properties.

<a id="feat-ise-01"></a>

## Longitudinal Health Record (LHR) Assembly

Assemble disparate encounters, diagnostic reports, medications, and clinical documents into a unified, vendor-neutral longitudinal timeline.

Longitudinal assembly relates contributions from disparate systems into a usable chronology. The unified view preserves source and subject context rather than turning consolidation into originating clinical authority or erasing differences in the standing of individual assertions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:longitudinal-health-record-lhr-assembly |
| canonicalId | c9c1c7a58cb5470db0141329ecc349f4 |
| displayName | [Feature] Longitudinal Health Record (LHR) Assembly |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LongitudinalClinicalRecordAdministration.LongitudinalHealthRecordLhrAssembly |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LongitudinalClinicalRecordAdministration.FEAT-LongitudinalHealthRecordLhrAssembly |
| Snapshot publication reference | FEAT-ISE-01 |
| ArchiMate semantic ID | id-1b6407fd4129511ab1134a5156efa49a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Longitudinal Health Record (LHR) Assembly |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-LONGITUDINAL-CLINICAL-RECORD-ADMINISTRATION.FEAT-ISE-01 |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD.FEAT-ISE-01"] |

Responsibility/framing context: [Longitudinal Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-reviewed-longitudinal-clinical-record-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:longitudinal-health-record-lhr-assembly`) and the element's version-qualified Properties.

<a id="feat-sd-01"></a>

## General Practice Shared Record Ingestion

Ingest shared health summaries, encounter notes, and immunization records from primary care practice management systems.

Shared-record ingestion brings general-practice contributions into cross-provider continuity. The summary or encounter note retains its clinical source while becoming available alongside regional information; consolidation does not transfer the GP's professional authority to the platform.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:general-practice-shared-record-ingestion |
| canonicalId | 869b77462ece488788c5a20598e7d587 |
| displayName | [Feature] General Practice Shared Record Ingestion |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.PrimaryCareCoordination.GeneralPracticeSharedRecordIngestion |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-PrimaryCareCoordination.FEAT-GeneralPracticeSharedRecordIngestion |
| Snapshot publication reference | FEAT-SD-01 |
| ArchiMate semantic ID | id-27699af98b8b57449168f0731d027a57 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | General Practice Shared Record Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-PRIMARY-CARE.FEAT-SD-01 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PRIMARY-CARE.FEAT-SD-01"] |

Responsibility/framing context: [Primary Care Coordination](business-enabling-capabilities.md#hcm-bec-primary-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:general-practice-shared-record-ingestion`) and the element's version-qualified Properties.

<a id="feat-sd-02"></a>

## Primary Care Notification Dispatch

Dispatch emergency department arrival, acute admission, and discharge notifications to the patient's registered general practitioner.

Primary-care notifications inform the registered GP of significant acute-care events. This outward notification responsibility is distinct from querying a chronic-care timeline: the latter shares context but has no established equivalence to dispatching the required arrival, admission or discharge notice.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:primary-care-notification-dispatch |
| canonicalId | 56739765fcfd4084b38d4550593ee74d |
| displayName | [Feature] Primary Care Notification Dispatch |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.PrimaryCareCoordination.PrimaryCareNotificationDispatch |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-PrimaryCareCoordination.FEAT-PrimaryCareNotificationDispatch |
| Snapshot publication reference | FEAT-SD-02 |
| ArchiMate semantic ID | id-3c947d3531ae559bae7c358d9662ee7b |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Primary Care Notification Dispatch |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-PRIMARY-CARE.FEAT-SD-02 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PRIMARY-CARE.FEAT-SD-02"] |

Responsibility/framing context: [Primary Care Coordination](business-enabling-capabilities.md#hcm-bec-primary-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:primary-care-notification-dispatch`) and the element's version-qualified Properties.

<a id="feat-sd-19"></a>

## Remote Telemetry Ingestion

Ingest home patient monitoring data, biometric telemetry, and patient-reported outcome measures (PROMs).

Remote telemetry ingestion accepts measurements and patient-reported contributions from outside the usual care site. The information can inform subsequent monitoring and interpretation, but acquisition alone does not establish clinical significance or authorise an intervention.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:remote-telemetry-ingestion |
| canonicalId | 969dde193fab4c5d82d7dc210c7f5a71 |
| displayName | [Feature] Remote Telemetry Ingestion |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.RemotePatientMonitoring.RemoteTelemetryIngestion |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-RemotePatientMonitoring.FEAT-RemoteTelemetryIngestion |
| Snapshot publication reference | FEAT-SD-19 |
| ArchiMate semantic ID | id-67a6a7c637b55190abfa3b66d9cbdd28 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Remote Telemetry Ingestion |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-REMOTE-VIRTUAL-CARE.FEAT-SD-19 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-REMOTE-VIRTUAL-CARE.FEAT-SD-19"] |

Responsibility/framing context: [Remote Patient Monitoring](business-enabling-capabilities.md#hcm-bec-remote-virtual-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:remote-telemetry-ingestion`) and the element's version-qualified Properties.

<a id="feat-sd-20"></a>

## Virtual Care Session Context Binding

Associate synchronous virtual/telehealth consultations with the longitudinal health record and active care episode.

Virtual-session binding associates a consultation with the enduring record and the relevant episode. It preserves the clinical context across delivery modes without turning the communication session itself into proof of clinical conclusions or adequacy of care.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:virtual-care-session-context-binding |
| canonicalId | c25d66fd6aff4299857eadc7c0243544 |
| displayName | [Feature] Virtual Care Session Context Binding |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.RemotePatientMonitoring.VirtualCareSessionContextBinding |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-RemotePatientMonitoring.FEAT-VirtualCareSessionContextBinding |
| Snapshot publication reference | FEAT-SD-20 |
| ArchiMate semantic ID | id-3135686eb16c5fabacc1675448e54251 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Virtual Care Session Context Binding |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-REMOTE-VIRTUAL-CARE.FEAT-SD-20 |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-REMOTE-VIRTUAL-CARE.FEAT-SD-20"] |

Responsibility/framing context: [Remote Patient Monitoring](business-enabling-capabilities.md#hcm-bec-remote-virtual-care). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:virtual-care-session-context-binding`) and the element's version-qualified Properties.

<a id="feat-ise-26"></a>

## Longitudinal Clinical & Operational Presentation

Deliver contextual clinical timelines, directory administration views, and operational management dashboards.

Contextual presentation makes clinical timelines, directories and operational information usable for people. It consumes established identity and security context while preserving the originating authority of what is shown; a user interface does not become an alternative persistence or policy boundary.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:longitudinal-clinical--operational-presentation |
| canonicalId | e82eaafa2e4046f48d4c86e15f282a0b |
| displayName | [Feature] Longitudinal Clinical & Operational Presentation |
| friendlyName | SharedInformationAndCoordinationEnablement.PresentationServices.ContextualInformationPresentation.LongitudinalClinicalAndOperationalPresentation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PresentationServices.CT3-BEC-ContextualInformationPresentation.FEAT-LongitudinalClinicalAndOperationalPresentation |
| Snapshot publication reference | FEAT-ISE-26 |
| ArchiMate semantic ID | id-13fa0d57229b5a61be2d49730b4872ad |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Longitudinal Clinical & Operational Presentation |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PRESENTATION-SERVICES.CT3-BEC-CONTEXTUAL-INFORMATION-PRESENTATION.FEAT-ISE-26 |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-PRESENTATION-SERVICES.FEAT-ISE-26"] |

Responsibility/framing context: [Contextual Information Presentation](business-enabling-capabilities.md#hcm-bec-reviewed-contextual-information-presentation). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:longitudinal-clinical--operational-presentation`) and the element's version-qualified Properties.

<a id="hcm-ft-service-readiness-dependency-assessment"></a>

## Service Readiness Dependency Assessment

Associate time- and context-qualified dependency evidence to establish a bounded readiness view for an offering.

**Overview**

An offering may be in operating hours but not deliverable because staffing, equipment, materials or supporting services are missing. A readiness view retains insufficiency and the owners of those dependency assertions.

**Boundary**

Readiness evidence does not originate clinical clearance or transfer resource management.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-service-readiness-dependency-assessment |
| canonicalId | 4d917929d5cf43218049a84cf46662b0 |
| displayName | [Feature] Service Readiness Dependency Assessment |
| friendlyName | HealthServiceOperationsEnablement.CareResourceAndCapacityCoordination.ServiceOfferingReadinessCoordination.ServiceReadinessDependencyAssessment |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-CareResourceAndCapacityCoordination.CT3-BEC-ServiceOfferingReadinessCoordination.FEAT-ServiceReadinessDependencyAssessment |
| Snapshot publication reference | HCM-FT-service-readiness-dependency-assessment |
| ArchiMate semantic ID | id-eea28ef3bab358869d143da0c838865a |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-OFFERING-READINESS-COORDINATION.FEAT-STEP2-SERVICE-READINESS-DEPENDENCY-ASSESSMENT |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: source proposal determines collective readiness; reviewed scope establishes dependency tracking only. Evidence may be reused, but determination authority is not silently transferred. |

Responsibility/framing context: [Service Offering Readiness Coordination](business-enabling-capabilities.md#hcm-bec-service-offering-readiness-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-service-readiness-dependency-assessment`) and the element's version-qualified Properties.

<a id="hcm-ft-entity-behaviour-failure-recovery-governance"></a>

## Entity Behaviour Failure & Recovery Governance

Apply explicit behaviour-failure dispositions and controlled, authorised resumption for entity-relative processing.

**Overview**

Failure may stop a behaviour, an input or the entity progression and may leave effects uncertain. Resumption must preserve that distinction rather than imply rollback or automatic safe retry.

**Boundary**

Recovery cannot silently declare no effects, bypass governance or resume a halted entity automatically.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-entity-behaviour-failure-recovery-governance |
| canonicalId | bdfaeebab74e4d4d96a5e3799a3aa07b |
| displayName | [Feature] Entity Behaviour Failure & Recovery Governance |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.EntitySpecificOperationalCoordination.EntityBehaviourFailureAndRecoveryGovernance |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-EntitySpecificOperationalCoordination.FEAT-EntityBehaviourFailureAndRecoveryGovernance |
| Snapshot publication reference | HCM-FT-entity-behaviour-failure-recovery-governance |
| ArchiMate semantic ID | id-337f8c9d1cb75bd19fc27f6b3cacb703 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-ENTITY-SPECIFIC-OPERATIONAL-COORDINATION.FEAT-STEP2-ENTITY-BEHAVIOUR-FAILURE-RECOVERY-GOVERNANCE |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Internal entity-relative progression is not the reviewed external work bridge; a current BEC destination has not been adjudicated. |

Responsibility/framing context: [Entity-Specific Operational Coordination](business-enabling-capabilities.md#hcm-bec-entity-specific-operational-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-entity-behaviour-failure-recovery-governance`) and the element's version-qualified Properties.

<a id="hcm-ft-entity-relative-activity-coordination"></a>

## Entity-Relative Activity Coordination

Sequence applicable behaviours for an identifiable entity input while preserving its operational reference frame.

**Overview**

Interest matching can select multiple behaviours while the entity progresses one input at a time. Each behaviour uses a fresh managed information context.

**Boundary**

Fan-out is not concurrent execution; clinical decisions and originating information remain with their owners.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-entity-relative-activity-coordination |
| canonicalId | b9960d7de097464ea6de94929ca48c89 |
| displayName | [Feature] Entity-Relative Activity Coordination |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.EntitySpecificOperationalCoordination.EntityRelativeActivityCoordination |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-EntitySpecificOperationalCoordination.FEAT-EntityRelativeActivityCoordination |
| Snapshot publication reference | HCM-FT-entity-relative-activity-coordination |
| ArchiMate semantic ID | id-690bcf58be875ffc83126a9d288b2df5 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-ENTITY-SPECIFIC-OPERATIONAL-COORDINATION.FEAT-STEP2-ENTITY-RELATIVE-ACTIVITY-COORDINATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Internal entity-relative progression is not the reviewed external work bridge; a current BEC destination has not been adjudicated. |

Responsibility/framing context: [Entity-Specific Operational Coordination](business-enabling-capabilities.md#hcm-bec-entity-specific-operational-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-entity-relative-activity-coordination`) and the element's version-qualified Properties.

<a id="hcm-ft-activity-outcome-continuation-rules"></a>

## Activity Outcome & Continuation Rules

Define how output roles, expected outcomes and continuation conditions are interpreted for an undertaking.

**Overview**

An output may contribute to completion, subsequent activity or retained evidence under different rules. Those meanings need to be specified rather than inferred from a returned payload.

**Boundary**

Completion policy is not business outcome truth; uncertain effects remain explicit.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-activity-outcome-continuation-rules |
| canonicalId | 34c8309244c142ec8772dcbb11ecc4ef |
| displayName | [Feature] Activity Outcome & Continuation Rules |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.GovernedOperationalActivityDesign.ActivityOutcomeAndContinuationRules |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-GovernedOperationalActivityDesign.FEAT-ActivityOutcomeAndContinuationRules |
| Snapshot publication reference | HCM-FT-activity-outcome-continuation-rules |
| ArchiMate semantic ID | id-339280e5161858089a433918445c4836 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-GOVERNED-OPERATIONAL-ACTIVITY-DESIGN.FEAT-STEP2-ACTIVITY-OUTCOME-CONTINUATION-RULES |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Internal execution-envelope design is distinct from governance of work across an external execution boundary. |

Responsibility/framing context: [Governed Operational Activity Design](business-enabling-capabilities.md#hcm-bec-governed-operational-activity-design). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-activity-outcome-continuation-rules`) and the element's version-qualified Properties.

<a id="hcm-ft-operational-activity-context-definition"></a>

## Operational Activity Context Definition

Define the anticipated information context and expected fulfilment basis of an operational undertaking.

**Overview**

Even a one-unit activity needs a declared information and outcome frame. The definition is separate from loading values or running the business logic.

**Boundary**

No dynamically invented resource set or orchestration engine is implied.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-operational-activity-context-definition |
| canonicalId | 58dcd59389664b7d83d14b536d7d03d0 |
| displayName | [Feature] Operational Activity Context Definition |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.GovernedOperationalActivityDesign.OperationalActivityContextDefinition |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-GovernedOperationalActivityDesign.FEAT-OperationalActivityContextDefinition |
| Snapshot publication reference | HCM-FT-operational-activity-context-definition |
| ArchiMate semantic ID | id-25d8c4b3871058aa99f090a7ee1e2231 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-GOVERNED-OPERATIONAL-ACTIVITY-DESIGN.FEAT-STEP2-OPERATIONAL-ACTIVITY-CONTEXT-DEFINITION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Internal execution-envelope design is distinct from governance of work across an external execution boundary. |

Responsibility/framing context: [Governed Operational Activity Design](business-enabling-capabilities.md#hcm-bec-governed-operational-activity-design). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-operational-activity-context-definition`) and the element's version-qualified Properties.

<a id="hcm-ft-controlled-administrative-recovery"></a>

## Controlled Administrative Recovery

Perform authorised recovery as identifiable governed activity through normal execution and evidence boundaries.

**Overview**

Operators may need to resume, reconcile or repair an operational concern. That is first-class work with permission and outcome standing, rather than direct mutation through a console.

**Boundary**

Administrative interaction grants no raw cache/database or subsystem-internal bypass.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-controlled-administrative-recovery |
| canonicalId | 21d0601a852043a4b0b1d4fe841108d2 |
| displayName | [Feature] Controlled Administrative Recovery |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.ControlledAdministrativeRecovery |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FEAT-ControlledAdministrativeRecovery |
| Snapshot publication reference | HCM-FT-controlled-administrative-recovery |
| ArchiMate semantic ID | id-14f2b94d4e9551c6b3d19b26aa56aa16 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FEAT-STEP2-CONTROLLED-ADMINISTRATIVE-RECOVERY |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-controlled-administrative-recovery`) and the element's version-qualified Properties.

<a id="feat-ise-22"></a>

## Synthetic Task Orchestration

Execute non-human, automated platform activity units and sequence orchestration.

---

## History

### Feature Overview — predecessor explanation

Synthetic Task orchestration governs non-human executable work and its sequencing. This technical work meaning remains distinct from human Work Orders, clinical worklists and external systems' generic task terminology; incomplete stalled/failed Process paths are not supplied by convention.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:synthetic-task-orchestration |
| canonicalId | 7cf9dd3aeeb84a83b2445f897db9bd4e |
| displayName | [Feature] Synthetic Task Orchestration |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.SyntheticTaskOrchestration |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FEAT-SyntheticTaskOrchestration |
| Snapshot publication reference | FEAT-ISE-22 |
| ArchiMate semantic ID | id-a230f00f87e65345adb868f557cee471 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Synthetic Task Orchestration |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FEAT-ISE-22 |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:synthetic-task-orchestration`) and the element's version-qualified Properties.

<a id="feat-ise-21"></a>

## To Do Decision & Approval Coordination

Coordinate human clinical review items, sign-offs, and approval workflows.

---

## History

### Feature Overview — predecessor explanation

To Do coordination supports human review, sign-off and decision activity. Its progression can expose whether the review item has been handled while leaving the meaning and authority of the professional or governance decision with the responsible participant.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:to-do-decision--approval-coordination |
| canonicalId | 377a40e8f2d64840a10dd55f87a9d005 |
| displayName | [Feature] To Do Decision & Approval Coordination |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.ToDoDecisionAndApprovalCoordination |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FEAT-ToDoDecisionAndApprovalCoordination |
| Snapshot publication reference | FEAT-ISE-21 |
| ArchiMate semantic ID | id-522c663fad0151a597da85ce9ef313b7 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | To Do Decision & Approval Coordination |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FEAT-ISE-21 |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:to-do-decision--approval-coordination`) and the element's version-qualified Properties.

<a id="hcm-ft-uncertain-activity-outcome-reconciliation"></a>

## Uncertain Activity Outcome Reconciliation

Reconcile indeterminate activity effects through authoritative observation before establishing a safe next disposition.

**Overview**

A timeout or absent response leaves a materially significant outcome unknown. Reconciliation can confirm an effect, establish absence or retain uncertainty without automatically retrying the original work.

**Boundary**

Execution uncertainty differs from insufficient assurance evidence; a retry does not prove the earlier attempt had no effect.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-uncertain-activity-outcome-reconciliation |
| canonicalId | eb06ecc956404d0397701ceafb5fbd46 |
| displayName | [Feature] Uncertain Activity Outcome Reconciliation |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.UncertainActivityOutcomeReconciliation |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FEAT-UncertainActivityOutcomeReconciliation |
| Snapshot publication reference | HCM-FT-uncertain-activity-outcome-reconciliation |
| ArchiMate semantic ID | id-00706dca11295d44b01a89f0e66c83d2 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FEAT-STEP2-UNCERTAIN-ACTIVITY-OUTCOME-RECONCILIATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-uncertain-activity-outcome-reconciliation`) and the element's version-qualified Properties.

<a id="feat-ise-20"></a>

## Work Order Progression

Manage execution states and dispatch of human operational work orders.

---

## History

### Feature Overview — predecessor explanation

Work Order progression coordinates human operational doing as a distinct work meaning. Dispatch, acceptance and completion describe the undertaking without making the workflow engine the authority over clinical work or treating a receipt as performance.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:work-order-progression |
| canonicalId | 5ca494e85869419c867618fd9648d03a |
| displayName | [Feature] Work Order Progression |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.WorkOrderProgression |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FEAT-WorkOrderProgression |
| Snapshot publication reference | FEAT-ISE-20 |
| ArchiMate semantic ID | id-c8c5184814d85707844e56c072d9459c |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Work Order Progression |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FEAT-ISE-20 |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:work-order-progression`) and the element's version-qualified Properties.

<a id="feat-ise-23"></a>

## Workflow Timeout & Escalation Oversight

Supervise operational deadlines, executing automated timeout notifications and supervisory escalations.

---

## History

### Feature Overview — predecessor explanation

Timeout oversight observes deadlines and initiates the established coordination response to stalled activity. It is management of progression rather than an assurance conclusion, and escalation does not grant the coordinator authority over the clinical meaning of the underlying work.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:workflow-timeout--escalation-oversight |
| canonicalId | ec55d06e393e4988a84c6157b719bd40 |
| displayName | [Feature] Workflow Timeout & Escalation Oversight |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.WorkflowTimeoutAndEscalationOversight |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FEAT-WorkflowTimeoutAndEscalationOversight |
| Snapshot publication reference | FEAT-ISE-23 |
| ArchiMate semantic ID | id-bef177231eed51078d4244176d3aced4 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Workflow Timeout & Escalation Oversight |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FEAT-ISE-23 |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:workflow-timeout--escalation-oversight`) and the element's version-qualified Properties.

<a id="hcm-ft-independent-evidence-assessment-adjudication"></a>

## Independent Evidence Assessment & Adjudication

Independently establish assurance context, assess evidence and adjudicate findings under applicable criteria.

**Overview**

The Feature unifies the review frame while retaining the three distinct canonical Functions and the boundary between criterion-level assessment and reportable conclusion.

**Boundary**

No new assurance Function or clinical-adequacy mandate is introduced; insufficient evidence establishes neither satisfaction nor non-satisfaction.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-independent-evidence-assessment-adjudication |
| canonicalId | 3aedee8f78484b81955866bcf16b8a6d |
| displayName | [Feature] Independent Evidence Assessment & Adjudication |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement.GovernedAssurance.IndependentEvidenceAssessmentAndAdjudication |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement.CT3-BEC-GovernedAssurance.FEAT-IndependentEvidenceAssessmentAndAdjudication |
| Snapshot publication reference | HCM-FT-independent-evidence-assessment-adjudication |
| ArchiMate semantic ID | id-74297fcf6597537fb193578f0eefca8d |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE.FEAT-STEP2-INDEPENDENT-EVIDENCE-ASSESSMENT-ADJUDICATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself. |

Responsibility/framing context: [Governed Assurance](business-enabling-capabilities.md#hcm-bec-governed-assurance). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-independent-evidence-assessment-adjudication`) and the element's version-qualified Properties.

<a id="hcm-ft-information-authority-source-standing-representation"></a>

## Information Authority & Source Standing Representation

Attach and preserve qualified source-authority and credibility standing for managed assertions.

**Overview**

The user of an assertion needs to distinguish authoritative, informational and anecdotal standing even when each was technically valid and durably accepted.

**Boundary**

Authority classification is not a clinical truth judgement or a universal rule for choosing competing assertions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-information-authority-source-standing-representation |
| canonicalId | 70a444156cd246cb97608a782e4bdea8 |
| displayName | [Feature] Information Authority & Source Standing Representation |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalInformationStewardshipEnablement.InformationAuthorityRepresentation.InformationAuthorityAndSourceStandingRepresentation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalInformationStewardshipEnablement.CT3-BEC-InformationAuthorityRepresentation.FEAT-InformationAuthorityAndSourceStandingRepresentation |
| Snapshot publication reference | HCM-FT-information-authority-source-standing-representation |
| ArchiMate semantic ID | id-15b2dbb248135673b3bfa3dbe53a03fc |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-INFORMATION-AUTHORITY-REPRESENTATION.FEAT-STEP2-INFORMATION-AUTHORITY-SOURCE-STANDING-REPRESENTATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Source credibility and authority standing require an explicit home; information-asset tags alone do not establish that responsibility. |

Responsibility/framing context: [Information Authority Representation](business-enabling-capabilities.md#hcm-bec-information-authority-representation). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-information-authority-source-standing-representation`) and the element's version-qualified Properties.

<a id="feat-ise-10"></a>

## Standards-Based Boundary Exchange

Provide standards-conformant boundary exchange for healthcare messaging and resource representations.

---

## History

### Feature Overview — predecessor explanation

Boundary exchange preserves the standards contract understood by the external participant. Harmonia's internal management metadata must not leak into that contract, and publication must construct an allowed external representation without destructively changing the internally managed information.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:standards-based-boundary-exchange |
| canonicalId | 3192ba1541ff41458d176184119c30bc |
| displayName | [Feature] Standards-Based Boundary Exchange |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationConnectivityEnablement.HealthInformationCommunication.StandardsBasedBoundaryExchange |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationConnectivityEnablement.CT3-BEC-HealthInformationCommunication.FEAT-StandardsBasedBoundaryExchange |
| Snapshot publication reference | FEAT-ISE-10 |
| ArchiMate semantic ID | id-0a5c88a49f2a518ebcf9b6f0315c092d |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Standards-Based Boundary Exchange |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-COMMUNICATION.FEAT-ISE-10 |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical boundary protocol mediation and technical transmission are distinct from reviewed patient-facing communication. |

Responsibility/framing context: [Health Information Communication](business-enabling-capabilities.md#hcm-bec-health-information-communication). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:standards-based-boundary-exchange`) and the element's version-qualified Properties.

<a id="hcm-ft-governed-policy-assignment-revocation"></a>

## Governed Policy Assignment & Revocation

Manage approved permission criteria and identity/role assignments, including temporal applicability and revocation.

**Overview**

An assignment becoming inapplicable by expiry differs from an explicit revocation; both must affect the effective authority used for operations.

**Boundary**

Management does not grant unconstrained administrative authority or derive permission from resource tags.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-governed-policy-assignment-revocation |
| canonicalId | d6c271ae95ea44449c5e22665d1d9bc2 |
| displayName | [Feature] Governed Policy Assignment & Revocation |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.GovernedPolicyAssignmentAndRevocation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.FEAT-GovernedPolicyAssignmentAndRevocation |
| Snapshot publication reference | HCM-FT-governed-policy-assignment-revocation |
| ArchiMate semantic ID | id-c0ee6896b3d759a9980c2a79ad7c3e53 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FEAT-STEP2-GOVERNED-POLICY-ASSIGNMENT-REVOCATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved. |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-governed-policy-assignment-revocation`) and the element's version-qualified Properties.

<a id="feat-ise-13"></a>

## Non-PHI Compliance Auditing

Generate tamper-evident, verifiable audit records of security-significant events without emitting unmasked PHI.

---

## History

### Feature Overview — predecessor explanation

Security-significant audit evidence makes governed actions attributable and verifiable while respecting PHI restrictions. The record concerns meaningful security facts under policy rather than routine implementation events, and recording it does not independently establish an assurance conclusion.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:non-phi-compliance-auditing |
| canonicalId | 08e37326908a49988dfb787f68df840a |
| displayName | [Feature] Non-PHI Compliance Auditing |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.NonPhiComplianceAuditing |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.FEAT-NonPhiComplianceAuditing |
| Snapshot publication reference | FEAT-ISE-13 |
| ArchiMate semantic ID | id-134a61003723533683cb931b9231dd3f |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Non-PHI Compliance Auditing |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FEAT-ISE-13 |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved. |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:non-phi-compliance-auditing`) and the element's version-qualified Properties.

<a id="hcm-ft-operation-bound-authorisation-enforcement"></a>

## Operation-Bound Authorisation Enforcement

Apply effective policy at the governed action boundary and reject actions lacking sufficient authority.

**Overview**

Consistent enforcement is required across mutation, access, execution and administration rather than relying on each caller to voluntarily invoke a check.

**Boundary**

Consumes trusted identity and criteria; it does not authenticate or establish clinical privilege from directory roles.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-operation-bound-authorisation-enforcement |
| canonicalId | 3b7dcee7d58d4d1c960dc92abd58b4c3 |
| displayName | [Feature] Operation-Bound Authorisation Enforcement |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.OperationBoundAuthorisationEnforcement |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.FEAT-OperationBoundAuthorisationEnforcement |
| Snapshot publication reference | HCM-FT-operation-bound-authorisation-enforcement |
| ArchiMate semantic ID | id-edcdc7b12cc25092aee8b7c197674755 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FEAT-STEP2-OPERATION-BOUND-AUTHORISATION-ENFORCEMENT |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved. |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-operation-bound-authorisation-enforcement`) and the element's version-qualified Properties.

<a id="feat-ise-12"></a>

## Security Context Propagation

Propagate tamper-evident, attributable security context across asynchronous processing boundaries.

---

## History

### Feature Overview — predecessor explanation

Security-context propagation carries attributable operational authority across asynchronous boundaries. Its responsibility is preservation of established context, distinct from evaluating a consent constraint; shared security subject matter is insufficient to assign that Function to this Feature.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | feature:security-context-propagation |
| canonicalId | 2b071c4fb10c4f61afecd18d09387214 |
| displayName | [Feature] Security Context Propagation |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.SecurityContextPropagation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.FEAT-SecurityContextPropagation |
| Snapshot publication reference | FEAT-ISE-12 |
| ArchiMate semantic ID | id-7e3672f150a85990ba602078b3393205 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Security Context Propagation |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FEAT-ISE-12 |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved. |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `feature:security-context-propagation`) and the element's version-qualified Properties.

<a id="hcm-ft-trusted-identity-context-intake"></a>

## Trusted Identity Context Intake

Accept and qualify externally authenticated identity context for Harmonia-governed operations.

**Overview**

The boundary between WHO and WHAT remains explicit: an external identity assertion is verified as trusted input before internal authorisation is resolved.

**Boundary**

Harmonia does not become an authentication or credential-issuance provider.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-trusted-identity-context-intake |
| canonicalId | 2ef8f67907c34cb5a6a37fe7181434e2 |
| displayName | [Feature] Trusted Identity Context Intake |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.TrustedIdentityContextIntake |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.FEAT-TrustedIdentityContextIntake |
| Snapshot publication reference | HCM-FT-trusted-identity-context-intake |
| ArchiMate semantic ID | id-9caab2bbf6365d30999ddb7592dfc543 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FEAT-STEP2-TRUSTED-IDENTITY-CONTEXT-INTAKE |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved. |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-trusted-identity-context-intake`) and the element's version-qualified Properties.

<a id="hcm-ft-governance-requirement-version-publication"></a>

## Governance Requirement Version Publication

Publish attributable versions and applicability of externally approved governing requirements.

**Overview**

Operational and assurance consumers need to identify the governing basis they used rather than silently substitute a current rule for a historical one.

**Boundary**

Publication does not confer requirement approval, authorisation policy or assurance-criteria authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-governance-requirement-version-publication |
| canonicalId | 001a067a4b3b46bebfe945f1bef541ce |
| displayName | [Feature] Governance Requirement Version Publication |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationProtectionAndAccountabilityEnablement.GovernanceRequirementPublicationSupport.GovernanceRequirementVersionPublication |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationProtectionAndAccountabilityEnablement.CT3-BEC-GovernanceRequirementPublicationSupport.FEAT-GovernanceRequirementVersionPublication |
| Snapshot publication reference | HCM-FT-governance-requirement-version-publication |
| ArchiMate semantic ID | id-012a49f6e1ce5f4094fd4870dfd281d4 |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-GOVERNANCE-REQUIREMENT-PUBLICATION-SUPPORT.FEAT-STEP2-GOVERNANCE-REQUIREMENT-VERSION-PUBLICATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Requirement publication/authority is not assurance-service review or information-model design; the reviewed hierarchy does not allocate a home. |

Responsibility/framing context: [Governance Requirement Publication Support](business-enabling-capabilities.md#hcm-bec-governance-requirement-publication-support). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-governance-requirement-version-publication`) and the element's version-qualified Properties.

<a id="hcm-ft-mandatory-significant-activity-audit-capture"></a>

## Mandatory Significant Activity Audit Capture

Capture required significant activity evidence using the specifically governed authoritative audit-capture boundary.

**Overview**

The actor whose action is recorded need not be entitled to persist the audit. Required capture uses its own established authority and failure can block the governed operation.

**Boundary**

Specific audit authority is not borrowed from the audited identity or generalised to other operations.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:feature-mandatory-significant-activity-audit-capture |
| canonicalId | b69936757c4446a7a1b57a455aae7a5f |
| displayName | [Feature] Mandatory Significant Activity Audit Capture |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationProtectionAndAccountabilityEnablement.SignificantActivityAuditCapture.MandatorySignificantActivityAuditCapture |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationProtectionAndAccountabilityEnablement.CT3-BEC-SignificantActivityAuditCapture.FEAT-MandatorySignificantActivityAuditCapture |
| Snapshot publication reference | HCM-FT-mandatory-significant-activity-audit-capture |
| ArchiMate semantic ID | id-a07c25a127ca5917984623d75092c14d |
| Legacy identity evidence | No legacy allocation |
| Construct | feature |
| Tier/classification | FT |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-SIGNIFICANT-ACTIVITY-AUDIT-CAPTURE.FEAT-STEP2-MANDATORY-SIGNIFICANT-ACTIVITY-AUDIT-CAPTURE |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: The reviewed hierarchy does not allocate general mandatory authoritative audit capture; disclosure or provenance alone is not an equivalent responsibility. |

Responsibility/framing context: [Significant Activity Audit Capture](business-enabling-capabilities.md#hcm-bec-significant-activity-audit-capture). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:feature-mandatory-significant-activity-audit-capture`) and the element's version-qualified Properties.

## [Feature] Identifier Capture

Accepted as submitted in CR-001 of [run-001](../../../archimate/processing/ingestion/1.0.8/run-001/change-review.md). No additional definition, responsibility or standing is inferred. Machine-readable source: [accepted ingestion capture](accepted-ingestion-1.0.8.json).

| Field | Value |
| --- | --- |
| representationId | id-727a264026584f80bc3304ff8383dc92 |
| archimateType | Capability |
| canonicalId | 3cae3c4810974c5f8ee5a4c942f38b07 |
| displayName | [Feature] Identifier Capture |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.IdentifierCapture |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-IdentifierCapture |

------- Legacy Content - Superseded ------- Finish ----
