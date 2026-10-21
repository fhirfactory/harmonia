<!-- continuum-source-succession -->
------- Legacy Content - Superseded ------- Start ------
Superseded as an active architectural carrier by the [canonical element continuum](../../../../enterprise-architecture/continuum/documentation/published/1.1.5/README.md).
[Exact source/identity correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json) records this historical generated carrier. Its original bytes and then-current claims are retained below; it is no longer a current authoring source.
---------------------------------------------------------

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Baseline — Services

[Baseline authority and reading guide](README.md). Currentness does not imply approval. Definitions are reproduced without semantic rewriting. IDs below retain their distinct roles.

## Catalogue

- [Care Plan Change Notification](#hcm-sv-care-plan-change-notification) — Canonical; current; placement Established
- [Care Plan Context Query](#hcm-sv-care-plan-context-query) — Canonical; current; placement Established
- [Care Plan Ingress](#hcm-sv-care-plan-ingress) — Canonical; current; placement Established
- [Community Care Encounter Ingress](#hcm-sv-community-care-encounter-ingress) — Canonical; current; placement Established
- [Community Care Team Query](#hcm-sv-community-care-team-query) — Canonical; current; placement Established
- [Outreach Batch Reconciliation Service](#hcm-sv-outreach-batch-reconciliation-service) — Canonical; current; placement Established
- [Demographic Intake Service](#hcm-sv-demographic-intake-service) — Canonical; current; placement Established
- [Demographic Update Notification](#hcm-sv-demographic-update-notification) — Canonical; current; placement Established
- [Immunisation History Query](#hcm-sv-immunisation-history-query) — Canonical; current; placement Established
- [Preventive Screening Status Query](#hcm-sv-preventive-screening-status-query) — Canonical; current; placement Established
- [Communication Audit Query](#hcm-sv-communication-audit-query) — Canonical; current; placement Established
- [Communication Delivery Status Query](#hcm-sv-communication-delivery-status-query) — Canonical; current; placement Established
- [Secure Clinical Message Dispatch](#hcm-sv-secure-clinical-message-dispatch) — Canonical; current; placement Established
- [Clinical Document Ingress](#hcm-sv-clinical-document-ingress) — Canonical; current; placement Established
- [Clinical Document Lifecycle Service](#hcm-sv-clinical-document-lifecycle-service) — Canonical; current; placement Established
- [Document Metadata Registry Query](#hcm-sv-document-metadata-registry-query) — Canonical; current; placement Established
- [Diagnostic Correlation Resolution](#hcm-sv-diagnostic-correlation-resolution) — Canonical; current; placement Established
- [Diagnostic Report Ingress](#hcm-sv-diagnostic-report-ingress) — Canonical; current; placement Established
- [Diagnostic Result Delivery Service](#hcm-sv-diagnostic-result-delivery-service) — Canonical; current; placement Established
- [Diagnostic Publication Service](#hcm-sv-diagnostic-publication-service) — Canonical; current; placement Established
- [Modality Worklist Distribution](#hcm-sv-modality-worklist-distribution) — Canonical; current; placement Established
- [Encounter Context Resolution](#hcm-sv-encounter-context-resolution) — Canonical; current; placement Established
- [Encounter Lifecycle Event Notification](#hcm-sv-encounter-lifecycle-event-notification) — Canonical; current; placement Established
- [Encounter Location History Query](#hcm-sv-encounter-location-history-query) — Canonical; current; placement Established
- [Acute Admission Dossier Query](#hcm-sv-acute-admission-dossier-query) — Canonical; current; placement Established
- [Acute Clinical Alert Dispatch](#hcm-sv-acute-clinical-alert-dispatch) — Canonical; current; placement Established
- [Dispense Event Ingress](#hcm-sv-dispense-event-ingress) — Canonical; current; placement Established
- [Medication Administration Ingress](#hcm-sv-medication-administration-ingress) — Canonical; current; placement Established
- [Medication Order Ingress](#hcm-sv-medication-order-ingress) — Canonical; current; placement Established
- [Allergy & Adverse Reaction Query](#hcm-sv-allergy-adverse-reaction-query) — Canonical; current; placement Established
- [Longitudinal Medication History Query](#hcm-sv-longitudinal-medication-history-query) — Canonical; current; placement Established
- [Order Cancellation Service](#hcm-sv-order-cancellation-service) — Canonical; current; placement Established
- [Order Dispatch Service](#hcm-sv-order-dispatch-service) — Canonical; current; placement Established
- [Order Outcome Notification](#hcm-sv-order-outcome-notification) — Canonical; current; placement Established
- [Order Requisition Ingress](#hcm-sv-order-requisition-ingress) — Canonical; current; placement Established
- [Order Status & Tracking Query](#hcm-sv-order-status-tracking-query) — Canonical; current; placement Established
- [Procedural Documentation Ingress](#hcm-sv-procedural-documentation-ingress) — Canonical; current; placement Established
- [Procedure Booking Ingress](#hcm-sv-procedure-booking-ingress) — Canonical; current; placement Established
- [Referral Context Query](#hcm-sv-referral-context-query) — Canonical; current; placement Established
- [Referral Status & Outcome Service](#hcm-sv-referral-status-outcome-service) — Canonical; current; placement Established
- [Referral Submission Service](#hcm-sv-referral-submission-service) — Canonical; current; placement Established
- [Appointment Notification Ingress](#hcm-sv-appointment-notification-ingress) — Canonical; current; placement Established
- [Appointment Schedule Query](#hcm-sv-appointment-schedule-query) — Canonical; current; placement Established
- [Confidentiality Restriction Evaluation](#hcm-sv-confidentiality-restriction-evaluation) — Canonical; current; placement Established
- [Consent Decision Service](#hcm-sv-consent-decision-service) — Canonical; current; placement Established
- [Client Relationship Query](#hcm-sv-client-relationship-query) — Canonical; current; placement Established
- [Representative Authority Verification](#hcm-sv-representative-authority-verification) — Canonical; current; placement Established
- [Demographic Change Distribution](#hcm-sv-demographic-change-distribution) — Canonical; current; placement Established
- [Healthcare Subject Context Resolution](#hcm-sv-healthcare-subject-context-resolution) — Canonical; current; placement Established
- [Person Identifier Correlation](#hcm-sv-person-identifier-correlation) — Canonical; current; placement Established
- [Person Identifier Resolution](#hcm-sv-person-identifier-resolution) — Canonical; current; placement Established
- [Person Identity Correction](#hcm-sv-person-identity-correction) — Canonical; current; placement Established
- [Device Association Query](#hcm-sv-device-association-query) — Canonical; current; placement Established
- [Device Definition Query](#hcm-sv-device-definition-query) — Canonical; current; placement Established
- [Device Endpoint Resolution](#hcm-sv-device-endpoint-resolution) — Canonical; current; placement Established
- [Device Instance Resolution](#hcm-sv-device-instance-resolution) — Canonical; current; placement Established
- [Clinical Resource Availability Query](#hcm-sv-clinical-resource-availability-query) — Canonical; current; placement Proposed
- [Healthcare Service Directory Query](#hcm-sv-healthcare-service-directory-query) — Canonical; current; placement Established
- [Service Availability Query](#hcm-sv-service-availability-query) — Canonical; current; placement Established
- [Service Eligibility Evaluation](#hcm-sv-service-eligibility-evaluation) — Canonical; current; placement Established
- [Service Provision Resolution](#hcm-sv-service-provision-resolution) — Canonical; current; placement Established
- [Care-Place Specification Lookup](#hcm-sv-care-place-specification-lookup) — Canonical; current; placement Established
- [Location Directory Query](#hcm-sv-location-directory-query) — Canonical; current; placement Established
- [Location Hierarchy Resolution](#hcm-sv-location-hierarchy-resolution) — Canonical; current; placement Established
- [Organisation Contact Lookup](#hcm-sv-organisation-contact-lookup) — Canonical; current; placement Established
- [Organisation Directory Query](#hcm-sv-organisation-directory-query) — Canonical; current; placement Established
- [Organisation Verification Service](#hcm-sv-organisation-verification-service) — Canonical; current; placement Established
- [Organisational Hierarchy Resolution](#hcm-sv-organisational-hierarchy-resolution) — Canonical; current; placement Established
- [Clinical Privilege Verification](#hcm-sv-clinical-privilege-verification) — Canonical; current; placement Established
- [Practitioner Affiliation Resolution](#hcm-sv-practitioner-affiliation-resolution) — Canonical; current; placement Established
- [Practitioner Directory Query](#hcm-sv-practitioner-directory-query) — Canonical; current; placement Established
- [Practitioner Endpoint Lookup](#hcm-sv-practitioner-endpoint-lookup) — Canonical; current; placement Established
- [Practitioner Role Resolution](#hcm-sv-practitioner-role-resolution) — Canonical; current; placement Established
- [Practitioner Verification Service](#hcm-sv-practitioner-verification-service) — Canonical; current; placement Established
- [Authorised Research Information Export](#hcm-sv-authorised-research-information-export) — Proposed; current; placement Proposed
- [Bed Availability Query](#hcm-sv-bed-availability-query) — Canonical; current; placement Established
- [Bed Turnover Lifecycle Service](#hcm-sv-bed-turnover-lifecycle-service) — Canonical; current; placement Established
- [Clinic Queue Telemetry Service](#hcm-sv-clinic-queue-telemetry-service) — Canonical; current; placement Established
- [Specimen Transit Tracking Service](#hcm-sv-specimen-transit-tracking-service) — Canonical; current; placement Established
- [Discharge Readiness Telemetry](#hcm-sv-discharge-readiness-telemetry) — Canonical; current; placement Established
- [ED Operational Status Query](#hcm-sv-ed-operational-status-query) — Canonical; current; placement Established
- [ED Triage Stream Ingress](#hcm-sv-ed-triage-stream-ingress) — Canonical; current; placement Established
- [Emergency Fast-Path LHR Query](#hcm-sv-emergency-fast-path-lhr-query) — Canonical; current; placement Established
- [Staff Presence Telemetry Query](#hcm-sv-staff-presence-telemetry-query) — Canonical; current; placement Established
- [On-Call Specialist Lookup](#hcm-sv-on-call-specialist-lookup) — Canonical; current; placement Established
- [Ambulatory Session Status Query](#hcm-sv-ambulatory-session-status-query) — Canonical; current; placement Established
- [Patient Transport Tracking Service](#hcm-sv-patient-transport-tracking-service) — Canonical; current; placement Established
- [Regional Health System Capacity Query](#hcm-sv-regional-health-system-capacity-query) — Canonical; current; placement Established
- [Theatre Case Progression Telemetry](#hcm-sv-theatre-case-progression-telemetry) — Canonical; current; placement Established
- [Ward Operational Summary Query](#hcm-sv-ward-operational-summary-query) — Canonical; current; placement Established
- [Discharge Dossier Packaging Service](#hcm-sv-discharge-dossier-packaging-service) — Canonical; current; placement Established
- [Ward Round Clinical Briefing Query](#hcm-sv-ward-round-clinical-briefing-query) — Canonical; current; placement Established
- [Operational Work Status Query](#hcm-sv-operational-work-status-query) — Canonical; current; placement Established
- [Work Dispatch Service](#hcm-sv-work-dispatch-service) — Canonical; current; placement Established
- [Temporal Context Resolution](#hcm-sv-temporal-context-resolution) — Canonical; current; placement Established
- [Unified Calendar Projection Query](#hcm-sv-unified-calendar-projection-query) — Canonical; current; placement Established
- [Clinical Collaboration Space Service](#hcm-sv-clinical-collaboration-space-service) — Canonical; current; placement Established
- [Collaboration Clinical Summary Resolution](#hcm-sv-collaboration-clinical-summary-resolution) — Canonical; current; placement Established
- [Collaboration Space Governance Service](#hcm-sv-collaboration-space-governance-service) — Canonical; current; placement Established
- [Terminology Concept Resolution Service](#hcm-sv-terminology-concept-resolution-service) — Canonical; current; placement Established
- [Terminology Mapping Service](#hcm-sv-terminology-mapping-service) — Canonical; current; placement Established
- [Governed Reference Dataset Content Query](#hcm-sv-governed-reference-dataset-content-query) — Proposed; current; placement Proposed
- [Operational Collaboration Service](#hcm-sv-operational-collaboration-service) — Canonical; current; placement Established
- [Clinical Information Search Service](#hcm-sv-clinical-information-search-service) — Canonical; current; placement Established
- [Clinical Resource Retrieval Service](#hcm-sv-clinical-resource-retrieval-service) — Canonical; current; placement Established
- [Consent Enforcement Service](#hcm-sv-consent-enforcement-service) — Canonical; current; placement Established
- [Policy Evaluation & Authorisation Service](#hcm-sv-policy-evaluation-authorisation-service) — Canonical; current; placement Established
- [Security Audit Ingress](#hcm-sv-security-audit-ingress) — Canonical; current; placement Established
- [Security Context Validation Service](#hcm-sv-security-context-validation-service) — Canonical; current; placement Established
- [Addressed Document Delivery Service](#hcm-sv-addressed-document-delivery-service) — Canonical; current; placement Established
- [Clinical Event Syndication Stream](#hcm-sv-clinical-event-syndication-stream) — Canonical; current; placement Established
- [Federated Health Information Query](#hcm-sv-federated-health-information-query) — Canonical; current; placement Established
- [HIE Submission Gateway Service](#hcm-sv-hie-submission-gateway-service) — Canonical; current; placement Established
- [Exchange Profile Specification Query](#hcm-sv-exchange-profile-specification-query) — Canonical; current; placement Established
- [Information Dictionary Query](#hcm-sv-information-dictionary-query) — Canonical; current; placement Established
- [Semantic Conformance Verification Service](#hcm-sv-semantic-conformance-verification-service) — Canonical; current; placement Established
- [Semantic Model Definition Service](#hcm-sv-semantic-model-definition-service) — Canonical; current; placement Established
- [Terminology Binding Specification Lookup](#hcm-sv-terminology-binding-specification-lookup) — Canonical; current; placement Established
- [Active Problem & Allergy Summary Query](#hcm-sv-active-problem-allergy-summary-query) — Canonical; current; placement Established
- [Historical Clinical Record Access](#hcm-sv-historical-clinical-record-access) — Canonical; current; placement Established
- [Longitudinal Clinical Record Query](#hcm-sv-longitudinal-clinical-record-query) — Canonical; current; placement Established
- [Associated Binary Content Retrieval](#hcm-sv-associated-binary-content-retrieval) — Proposed; current; placement Proposed
- [Chronic Care Timeline Query](#hcm-sv-chronic-care-timeline-query) — Canonical; current; placement Established
- [Primary Care Summary Ingress](#hcm-sv-primary-care-summary-ingress) — Canonical; current; placement Established
- [Remote Telemetry Ingress](#hcm-sv-remote-telemetry-ingress) — Canonical; current; placement Established
- [Virtual Encounter Context Resolution](#hcm-sv-virtual-encounter-context-resolution) — Canonical; current; placement Established
- [Contextual Presentation Service](#hcm-sv-contextual-presentation-service) — Canonical; current; placement Established
- [Service Offering Readiness Query](#hcm-sv-service-offering-readiness-query) — Proposed; pending_adjudication; placement Proposed
- [Operational Credential Checkpoint Service](#hcm-sv-operational-credential-checkpoint-service) — Canonical; pending_adjudication; placement Established
- [Entity Operational Input Submission](#hcm-sv-entity-operational-input-submission) — Proposed; pending_adjudication; placement Proposed
- [Activity Escalation Telemetry](#hcm-sv-activity-escalation-telemetry) — Canonical; pending_adjudication; placement Established
- [Synthetic Task Execution Service](#hcm-sv-synthetic-task-execution-service) — Canonical; pending_adjudication; placement Established
- [To Do Management Service](#hcm-sv-to-do-management-service) — Canonical; pending_adjudication; placement Established
- [Work Order Coordination Service](#hcm-sv-work-order-coordination-service) — Canonical; pending_adjudication; placement Established
- [Communicate Service Assurance Outcome](#hcm-sv-communicate-service-assurance-outcome) — Canonical; pending_adjudication; placement Established
- [Request Service Assurance](#hcm-sv-request-service-assurance) — Canonical; pending_adjudication; placement Established
- [Request Service Assurance Status](#hcm-sv-request-service-assurance-status) — Canonical; pending_adjudication; placement Established
- [Standards Communication Gateway Service](#hcm-sv-standards-communication-gateway-service) — Canonical; pending_adjudication; placement Established
- [Mandatory Audit Evidence Capture](#hcm-sv-mandatory-audit-evidence-capture) — Proposed; pending_adjudication; placement Proposed

<a id="hcm-sv-care-plan-change-notification"></a>

## Care Plan Change Notification

Emits care plan revision notices.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:care-plan-change-notification |
| canonicalId | 08a8309372734287b2c22a06b95080a4 |
| displayName | Care Plan Change Notification |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CarePlanChangeNotification |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.SV-CarePlanChangeNotification |
| Snapshot publication reference | HCM-SV-care-plan-change-notification |
| ArchiMate semantic ID | id-6a7c9e0a85145a73bbb7190f033214ae |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Care Plan Change Notification |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.SV-CARE-PLAN-CHANGE-NOTIFICATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CARE-PLAN-ADMINISTRATION.SV-CARE-PLAN-CHANGE-NOTIFICATION"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Care Plan Administration](business-enabling-capabilities.md#hcm-bec-care-plan-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:care-plan-change-notification`) and the element's version-qualified Properties.

<a id="hcm-sv-care-plan-context-query"></a>

## Care Plan Context Query

Discloses active care plans to treating clinicians.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:care-plan-context-query |
| canonicalId | 0bb4cceb0f1b4311930635d8b4c320e0 |
| displayName | Care Plan Context Query |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CarePlanContextQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.SV-CarePlanContextQuery |
| Snapshot publication reference | HCM-SV-care-plan-context-query |
| ArchiMate semantic ID | id-24102c5e1b0459a69be0318db28a6dbd |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Care Plan Context Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.SV-CARE-PLAN-CONTEXT-QUERY |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CARE-PLAN-ADMINISTRATION.SV-CARE-PLAN-CONTEXT-QUERY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Care Plan Administration](business-enabling-capabilities.md#hcm-bec-care-plan-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:care-plan-context-query`) and the element's version-qualified Properties.

<a id="hcm-sv-care-plan-ingress"></a>

## Care Plan Ingress

Ingests shared care plans.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:care-plan-ingress |
| canonicalId | 180d3f29c2324905ab19337a4dcbce7b |
| displayName | Care Plan Ingress |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CarePlanLifecycleAndCoordination.CarePlanIngestionAndDistribution.CarePlanIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CarePlanLifecycleAndCoordination.FEAT-CarePlanIngestionAndDistribution.SV-CarePlanIngress |
| Snapshot publication reference | HCM-SV-care-plan-ingress |
| ArchiMate semantic ID | id-7204348a711e5529a7c4c794c4d66259 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Care Plan Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.SV-CARE-PLAN-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CARE-PLAN-ADMINISTRATION.SV-CARE-PLAN-INGRESS"] |

Responsibility/framing context: [Care Plan Administration](business-enabling-capabilities.md#hcm-bec-care-plan-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:care-plan-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-community-care-encounter-ingress"></a>

## Community Care Encounter Ingress

Ingests community care documentation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:community-care-encounter-ingress |
| canonicalId | 5806fe40300044b280a25fb86667e789 |
| displayName | Community Care Encounter Ingress |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CommunityCareCoordination.CommunityEncounterIngestion.CommunityCareEncounterIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CommunityCareCoordination.FEAT-CommunityEncounterIngestion.SV-CommunityCareEncounterIngress |
| Snapshot publication reference | HCM-SV-community-care-encounter-ingress |
| ArchiMate semantic ID | id-710fde4f099052a8845d80bae71b368c |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Community Care Encounter Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-COMMUNITY-CARE.SV-COMMUNITY-CARE-ENCOUNTER-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-COMMUNITY-CARE.SV-COMMUNITY-CARE-ENCOUNTER-INGRESS"] |

Responsibility/framing context: [Community Care Coordination](business-enabling-capabilities.md#hcm-bec-community-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:community-care-encounter-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-community-care-team-query"></a>

## Community Care Team Query

Exposes community provider contact rosters.

Feature association not established: Strategy FEAT-SD-16 — Community Care Plan Synchronization remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:community-care-team-query |
| canonicalId | 5292120098cc4b7e8280b22f46b60b95 |
| displayName | Community Care Team Query |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CommunityCareCoordination.CommunityCareTeamQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CommunityCareCoordination.SV-CommunityCareTeamQuery |
| Snapshot publication reference | HCM-SV-community-care-team-query |
| ArchiMate semantic ID | id-0dd88fd817285933bfaade2bf83a66a7 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Community Care Team Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-COMMUNITY-CARE.SV-COMMUNITY-CARE-TEAM-QUERY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-COMMUNITY-CARE.SV-COMMUNITY-CARE-TEAM-QUERY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Community Care Coordination](business-enabling-capabilities.md#hcm-bec-community-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:community-care-team-query`) and the element's version-qualified Properties.

<a id="hcm-sv-outreach-batch-reconciliation-service"></a>

## Outreach Batch Reconciliation Service

Reconciles batch clinical encounters upon network reconnection.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:outreach-batch-reconciliation-service |
| canonicalId | 9488822da1764a1898303a9c656a6062 |
| displayName | Outreach Batch Reconciliation Service |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.OutreachCareContinuity.OutreachEncounterOutcomeIngestion.OutreachBatchReconciliationService |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-OutreachCareContinuity.FEAT-OutreachEncounterOutcomeIngestion.SV-OutreachBatchReconciliationService |
| Snapshot publication reference | HCM-SV-outreach-batch-reconciliation-service |
| ArchiMate semantic ID | id-a960f9960e11585cb76cfbaa0a5c17ef |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Outreach Batch Reconciliation Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-OUTREACH-CARE.SV-OUTREACH-BATCH-RECONCILIATION-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-OUTREACH-CARE.SV-OUTREACH-BATCH-RECONCILIATION-SERVICE"] |

Responsibility/framing context: [Outreach Care Continuity](business-enabling-capabilities.md#hcm-bec-outreach-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:outreach-batch-reconciliation-service`) and the element's version-qualified Properties.

<a id="hcm-sv-demographic-intake-service"></a>

## Demographic Intake Service

Exposed to departmental registration systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:demographic-intake-service |
| canonicalId | b6d8aab087944a6e8cc246b85d32843e |
| displayName | Demographic Intake Service |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ClientIntakeAndDemographicUpdate.DemographicUpdateIngestion.DemographicIntakeService |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ClientIntakeAndDemographicUpdate.FEAT-DemographicUpdateIngestion.SV-DemographicIntakeService |
| Snapshot publication reference | HCM-SV-demographic-intake-service |
| ArchiMate semantic ID | id-1155b1c2b46158a48866cbc349ab9bbd |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Demographic Intake Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.SV-DEMOGRAPHIC-INTAKE-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-CLIENT-CENTRED-ADMINISTRATION.SV-DEMOGRAPHIC-INTAKE-SERVICE"] |

Responsibility/framing context: [Client-Centred Administration](business-enabling-capabilities.md#hcm-bec-client-centred-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:demographic-intake-service`) and the element's version-qualified Properties.

<a id="hcm-sv-demographic-update-notification"></a>

## Demographic Update Notification

Emits change notifications across the HIE.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:demographic-update-notification |
| canonicalId | 10b54e9ef0ed47e491d18731c58247f5 |
| displayName | Demographic Update Notification |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ClientIntakeAndDemographicUpdate.DemographicChangeDistribution.DemographicUpdateNotification |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ClientIntakeAndDemographicUpdate.FEAT-DemographicChangeDistribution.SV-DemographicUpdateNotification |
| Snapshot publication reference | HCM-SV-demographic-update-notification |
| ArchiMate semantic ID | id-8ce353192d015105bfb22afd2a7b5ee8 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Demographic Update Notification |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.SV-DEMOGRAPHIC-UPDATE-NOTIFICATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-CLIENT-CENTRED-ADMINISTRATION.SV-DEMOGRAPHIC-UPDATE-NOTIFICATION"] |

Responsibility/framing context: [Client-Centred Administration](business-enabling-capabilities.md#hcm-bec-client-centred-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:demographic-update-notification`) and the element's version-qualified Properties.

<a id="hcm-sv-immunisation-history-query"></a>

## Immunisation History Query

Discloses complete vaccination history.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:immunisation-history-query |
| canonicalId | f2a0107d73664aa0be9d6695fc455d74 |
| displayName | Immunisation History Query |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ScreeningAndImmunisationEnablement.ImmunisationEventSubmission.ImmunisationHistoryQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ScreeningAndImmunisationEnablement.FEAT-ImmunisationEventSubmission.SV-ImmunisationHistoryQuery |
| Snapshot publication reference | HCM-SV-immunisation-history-query |
| ArchiMate semantic ID | id-46418e01821352bc941375e34a88abee |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Immunisation History Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-PREVENTIVE-CARE.SV-IMMUNISATION-HISTORY-QUERY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PREVENTIVE-CARE.SV-IMMUNISATION-HISTORY-QUERY"] |

Responsibility/framing context: [Screening and Immunisation Enablement](business-enabling-capabilities.md#hcm-bec-preventive-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:immunisation-history-query`) and the element's version-qualified Properties.

<a id="hcm-sv-preventive-screening-status-query"></a>

## Preventive Screening Status Query

Discloses screening due dates to primary care providers.

Feature association not established: FEAT-SD-14 — Screening Recall Notification Distribution is adjudicated sufficient through reusable composition/workflow above. Projecting/querying eligibility dates remains distinct from distribution; no dedicated recall Function or replacement association is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:preventive-screening-status-query |
| canonicalId | bc679a6f23de44b79a452fb5ef10c5ee |
| displayName | Preventive Screening Status Query |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ScreeningAndImmunisationEnablement.PreventiveScreeningStatusQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ScreeningAndImmunisationEnablement.SV-PreventiveScreeningStatusQuery |
| Snapshot publication reference | HCM-SV-preventive-screening-status-query |
| ArchiMate semantic ID | id-40cd7493d023511f90294cbfec8209e1 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Preventive Screening Status Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-PREVENTIVE-CARE.SV-PREVENTIVE-SCREENING-STATUS-QUERY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PREVENTIVE-CARE.SV-PREVENTIVE-SCREENING-STATUS-QUERY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Screening and Immunisation Enablement](business-enabling-capabilities.md#hcm-bec-preventive-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:preventive-screening-status-query`) and the element's version-qualified Properties.

<a id="hcm-sv-communication-audit-query"></a>

## Communication Audit Query

Discloses transmission history for medico-legal verification.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:communication-audit-query |
| canonicalId | 7c871898fd634a16902048da75586fd9 |
| displayName | Communication Audit Query |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration.SecureClinicalCommunication.CommunicationAuditLogging.CommunicationAuditQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration.CT3-BEC-SecureClinicalCommunication.FEAT-CommunicationAuditLogging.SV-CommunicationAuditQuery |
| Snapshot publication reference | HCM-SV-communication-audit-query |
| ArchiMate semantic ID | id-d5503c6d10b5545eb1c5d1d5ae381954 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Communication Audit Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.SV-COMMUNICATION-AUDIT-QUERY |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.SV-COMMUNICATION-AUDIT-QUERY"] |

Responsibility/framing context: [Clinical Communication Administration](business-enabling-capabilities.md#hcm-bec-clinical-communication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:communication-audit-query`) and the element's version-qualified Properties.

<a id="hcm-sv-communication-delivery-status-query"></a>

## Communication Delivery Status Query

Discloses delivery confirmation to message senders.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:communication-delivery-status-query |
| canonicalId | 1b3447db9891406b951f6a6ccf20763e |
| displayName | Communication Delivery Status Query |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration.SecureClinicalCommunication.DeliveryAcknowledgementTracking.CommunicationDeliveryStatusQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration.CT3-BEC-SecureClinicalCommunication.FEAT-DeliveryAcknowledgementTracking.SV-CommunicationDeliveryStatusQuery |
| Snapshot publication reference | HCM-SV-communication-delivery-status-query |
| ArchiMate semantic ID | id-621a40630d9256eda0126f58d382dace |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Communication Delivery Status Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.SV-COMMUNICATION-DELIVERY-STATUS-QUERY |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.SV-COMMUNICATION-DELIVERY-STATUS-QUERY"] |

Responsibility/framing context: [Clinical Communication Administration](business-enabling-capabilities.md#hcm-bec-clinical-communication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:communication-delivery-status-query`) and the element's version-qualified Properties.

<a id="hcm-sv-secure-clinical-message-dispatch"></a>

## Secure Clinical Message Dispatch

Exposes message dispatch to clinical authors.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:secure-clinical-message-dispatch |
| canonicalId | d28fb3b4123b4e569a95a823fd83f837 |
| displayName | Secure Clinical Message Dispatch |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration.SecureClinicalCommunication.ClinicalCommunicationDistribution.SecureClinicalMessageDispatch |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration.CT3-BEC-SecureClinicalCommunication.FEAT-ClinicalCommunicationDistribution.SV-SecureClinicalMessageDispatch |
| Snapshot publication reference | HCM-SV-secure-clinical-message-dispatch |
| ArchiMate semantic ID | id-bc33442399e1567093c059606f9a2888 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Secure Clinical Message Dispatch |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.SV-SECURE-CLINICAL-MESSAGE-DISPATCH |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.SV-SECURE-CLINICAL-MESSAGE-DISPATCH"] |

Responsibility/framing context: [Clinical Communication Administration](business-enabling-capabilities.md#hcm-bec-clinical-communication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:secure-clinical-message-dispatch`) and the element's version-qualified Properties.

<a id="hcm-sv-clinical-document-ingress"></a>

## Clinical Document Ingress

Exposes document submission endpoints across the HIE.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:clinical-document-ingress |
| canonicalId | 2a69a33c93954a868249270d919dabfd |
| displayName | Clinical Document Ingress |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.ClinicalDocumentationLifecycleAdministration.ClinicalDocumentIngestion.ClinicalDocumentIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.CT3-BEC-ClinicalDocumentationLifecycleAdministration.FEAT-ClinicalDocumentIngestion.SV-ClinicalDocumentIngress |
| Snapshot publication reference | HCM-SV-clinical-document-ingress |
| ArchiMate semantic ID | id-8371aafb47eb591f8182552dceb459ad |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Document Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.SV-CLINICAL-DOCUMENT-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION.SV-CLINICAL-DOCUMENT-INGRESS"] |

Responsibility/framing context: [Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-clinical-record-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:clinical-document-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-clinical-document-lifecycle-service"></a>

## Clinical Document Lifecycle Service

Allows authoring clinicians to publish addenda or corrections.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:clinical-document-lifecycle-service |
| canonicalId | fbeffe86f67347e3b39ad4544b13cbe7 |
| displayName | Clinical Document Lifecycle Service |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.ClinicalDocumentationLifecycleAdministration.DocumentVersioningAndSupersession.ClinicalDocumentLifecycleService |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.CT3-BEC-ClinicalDocumentationLifecycleAdministration.FEAT-DocumentVersioningAndSupersession.SV-ClinicalDocumentLifecycleService |
| Snapshot publication reference | HCM-SV-clinical-document-lifecycle-service |
| ArchiMate semantic ID | id-184be279c5e85927a1ff5e81aa7939e6 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Document Lifecycle Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.SV-CLINICAL-DOCUMENT-LIFECYCLE-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION.SV-CLINICAL-DOCUMENT-LIFECYCLE-SERVICE"] |

Responsibility/framing context: [Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-clinical-record-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:clinical-document-lifecycle-service`) and the element's version-qualified Properties.

<a id="hcm-sv-document-metadata-registry-query"></a>

## Document Metadata Registry Query

Discloses document registry metadata to clinical viewers.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:document-metadata-registry-query |
| canonicalId | 63254ecfe5554eeeb63d0c9f1720388b |
| displayName | Document Metadata Registry Query |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.ClinicalDocumentationLifecycleAdministration.DocumentMetadataIndexing.DocumentMetadataRegistryQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.CT3-BEC-ClinicalDocumentationLifecycleAdministration.FEAT-DocumentMetadataIndexing.SV-DocumentMetadataRegistryQuery |
| Snapshot publication reference | HCM-SV-document-metadata-registry-query |
| ArchiMate semantic ID | id-340b2f052af95bf097a047aa2e43fe15 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Document Metadata Registry Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.SV-DOCUMENT-METADATA-REGISTRY-QUERY |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION.SV-DOCUMENT-METADATA-REGISTRY-QUERY"] |

Responsibility/framing context: [Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-clinical-record-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:document-metadata-registry-query`) and the element's version-qualified Properties.

<a id="hcm-sv-diagnostic-correlation-resolution"></a>

## Diagnostic Correlation Resolution

Discloses correlated order/result linkages.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:diagnostic-correlation-resolution |
| canonicalId | 15f3582e02ae47e1b7f9117573a8ddf7 |
| displayName | Diagnostic Correlation Resolution |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement.DiagnosticRequestCorrelation.DiagnosticCorrelationResolution |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement.FEAT-DiagnosticRequestCorrelation.SV-DiagnosticCorrelationResolution |
| Snapshot publication reference | HCM-SV-diagnostic-correlation-resolution |
| ArchiMate semantic ID | id-db40af8a466c5bb58974b3392b9fb7a0 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Diagnostic Correlation Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.SV-DIAGNOSTIC-CORRELATION-RESOLUTION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.SV-DIAGNOSTIC-CORRELATION-RESOLUTION"] |

Responsibility/framing context: [Diagnostic Administration](business-enabling-capabilities.md#hcm-bec-diagnostic-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:diagnostic-correlation-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-diagnostic-report-ingress"></a>

## Diagnostic Report Ingress

Ingests results from external LIS/RIS systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:diagnostic-report-ingress |
| canonicalId | b151ad42b9194d9e84b1dd9beecdf6dd |
| displayName | Diagnostic Report Ingress |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement.DiagnosticReportIngestionAndBinding.DiagnosticReportIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement.FEAT-DiagnosticReportIngestionAndBinding.SV-DiagnosticReportIngress |
| Snapshot publication reference | HCM-SV-diagnostic-report-ingress |
| ArchiMate semantic ID | id-77ff0fef85f05d44b38dda24fc719a2b |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Diagnostic Report Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.SV-DIAGNOSTIC-REPORT-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.SV-DIAGNOSTIC-REPORT-INGRESS"] |

Responsibility/framing context: [Diagnostic Administration](business-enabling-capabilities.md#hcm-bec-diagnostic-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:diagnostic-report-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-diagnostic-result-delivery-service"></a>

## Diagnostic Result Delivery Service

Emits secure result notifications.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:diagnostic-result-delivery-service |
| canonicalId | 86a9d1868c8c45788791a9436794531c |
| displayName | Diagnostic Result Delivery Service |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticWorkflowManagement.DiagnosticReportDistribution.DiagnosticResultDeliveryService |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticWorkflowManagement.FEAT-DiagnosticReportDistribution.SV-DiagnosticResultDeliveryService |
| Snapshot publication reference | HCM-SV-diagnostic-result-delivery-service |
| ArchiMate semantic ID | id-b1e74bef5b305c99902a16b70734e083 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Diagnostic Result Delivery Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.SV-DIAGNOSTIC-RESULT-DELIVERY-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.SV-DIAGNOSTIC-RESULT-DELIVERY-SERVICE"] |

Responsibility/framing context: [Diagnostic Administration](business-enabling-capabilities.md#hcm-bec-diagnostic-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:diagnostic-result-delivery-service`) and the element's version-qualified Properties.

<a id="hcm-sv-diagnostic-publication-service"></a>

## Diagnostic Publication Service

Publishes verified diagnostic reports across the enterprise.

Feature association not established: Strategy FEAT-SD-10 — Diagnostic History Consolidation remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:diagnostic-publication-service |
| canonicalId | 600ca5384b4643b0b35579a02c307dcf |
| displayName | Diagnostic Publication Service |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticServiceIntegration.DiagnosticPublicationService |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticServiceIntegration.SV-DiagnosticPublicationService |
| Snapshot publication reference | HCM-SV-diagnostic-publication-service |
| ArchiMate semantic ID | id-e7acb465794c55129d0681b967598e3f |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Diagnostic Publication Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-SERVICES.SV-DIAGNOSTIC-PUBLICATION-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-DIAGNOSTIC-SERVICES.SV-DIAGNOSTIC-PUBLICATION-SERVICE"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Diagnostic Service Integration](business-enabling-capabilities.md#hcm-bec-diagnostic-services). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:diagnostic-publication-service`) and the element's version-qualified Properties.

<a id="hcm-sv-modality-worklist-distribution"></a>

## Modality Worklist Distribution

Supplies order worklists to diagnostic laboratory/imaging systems.

Feature association not established: Strategy FEAT-SD-09 — Laboratory & Imaging Result Distribution remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:modality-worklist-distribution |
| canonicalId | e254b6a140c94b33a206e74db1aecfbb |
| displayName | Modality Worklist Distribution |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticServiceIntegration.ModalityWorklistDistribution |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticServiceIntegration.SV-ModalityWorklistDistribution |
| Snapshot publication reference | HCM-SV-modality-worklist-distribution |
| ArchiMate semantic ID | id-b72a63ea9c385f0d89ff7adf103df933 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Modality Worklist Distribution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-SERVICES.SV-MODALITY-WORKLIST-DISTRIBUTION |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-DIAGNOSTIC-SERVICES.SV-MODALITY-WORKLIST-DISTRIBUTION"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Diagnostic Service Integration](business-enabling-capabilities.md#hcm-bec-diagnostic-services). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:modality-worklist-distribution`) and the element's version-qualified Properties.

<a id="hcm-sv-encounter-context-resolution"></a>

## Encounter Context Resolution

Discloses active encounter details to clinical applications.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:encounter-context-resolution |
| canonicalId | ca22b0a4e51b4f5eb6b528bc36fa5d9c |
| displayName | Encounter Context Resolution |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.EncounterLifecycleManagement.EncounterContextCreation.EncounterContextResolution |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-EncounterLifecycleManagement.FEAT-EncounterContextCreation.SV-EncounterContextResolution |
| Snapshot publication reference | HCM-SV-encounter-context-resolution |
| ArchiMate semantic ID | id-af5949583dca5647aa621461dafff79a |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Encounter Context Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.SV-ENCOUNTER-CONTEXT-RESOLUTION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.SV-ENCOUNTER-CONTEXT-RESOLUTION"] |

Responsibility/framing context: [Episode & Encounter Administration](business-enabling-capabilities.md#hcm-bec-episode-encounter-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:encounter-context-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-encounter-lifecycle-event-notification"></a>

## Encounter Lifecycle Event Notification

Emits encounter transition events across the HIE.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:encounter-lifecycle-event-notification |
| canonicalId | e9c6b81172984785a47d222ac0c28d45 |
| displayName | Encounter Lifecycle Event Notification |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.EncounterLifecycleManagement.EncounterStateProgression.EncounterLifecycleEventNotification |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-EncounterLifecycleManagement.FEAT-EncounterStateProgression.SV-EncounterLifecycleEventNotification |
| Snapshot publication reference | HCM-SV-encounter-lifecycle-event-notification |
| ArchiMate semantic ID | id-a67e6110df1a5446a71a73624e836ef5 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Encounter Lifecycle Event Notification |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.SV-ENCOUNTER-LIFECYCLE-EVENT-NOTIFICATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.SV-ENCOUNTER-LIFECYCLE-EVENT-NOTIFICATION"] |

Responsibility/framing context: [Episode & Encounter Administration](business-enabling-capabilities.md#hcm-bec-episode-encounter-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:encounter-lifecycle-event-notification`) and the element's version-qualified Properties.

<a id="hcm-sv-encounter-location-history-query"></a>

## Encounter Location History Query

Discloses patient location tracking history.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:encounter-location-history-query |
| canonicalId | 5f0f9fec1d16458aaf914b0957793953 |
| displayName | Encounter Location History Query |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.EncounterLifecycleManagement.EncounterBedLocationAssociation.EncounterLocationHistoryQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-EncounterLifecycleManagement.FEAT-EncounterBedLocationAssociation.SV-EncounterLocationHistoryQuery |
| Snapshot publication reference | HCM-SV-encounter-location-history-query |
| ArchiMate semantic ID | id-055b7f7c7d2d53b8a927bf82eea4c6fe |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Encounter Location History Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.SV-ENCOUNTER-LOCATION-HISTORY-QUERY |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.SV-ENCOUNTER-LOCATION-HISTORY-QUERY"] |

Responsibility/framing context: [Episode & Encounter Administration](business-enabling-capabilities.md#hcm-bec-episode-encounter-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:encounter-location-history-query`) and the element's version-qualified Properties.

<a id="hcm-sv-acute-admission-dossier-query"></a>

## Acute Admission Dossier Query

Delivers a high-priority clinical briefing to admitting teams.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:acute-admission-dossier-query |
| canonicalId | 7276d5e4d484440f9466ec24c183bdab |
| displayName | Acute Admission Dossier Query |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.AcuteCareMonitoringAndEventCoordination.AcuteCareClinicalContextProvision.AcuteAdmissionDossierQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-AcuteCareMonitoringAndEventCoordination.FEAT-AcuteCareClinicalContextProvision.SV-AcuteAdmissionDossierQuery |
| Snapshot publication reference | HCM-SV-acute-admission-dossier-query |
| ArchiMate semantic ID | id-fe1a8980c6655f9dae0c01cee4275643 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Acute Admission Dossier Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ACUTE-CARE.SV-ACUTE-ADMISSION-DOSSIER-QUERY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-ACUTE-CARE.SV-ACUTE-ADMISSION-DOSSIER-QUERY"] |

Responsibility/framing context: [Acute Care Monitoring & Event Coordination](business-enabling-capabilities.md#hcm-bec-acute-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:acute-admission-dossier-query`) and the element's version-qualified Properties.

<a id="hcm-sv-acute-clinical-alert-dispatch"></a>

## Acute Clinical Alert Dispatch

Dispatches urgent interruptive alerts to on-duty ward teams.

Feature association not established: FEAT-SD-03 — Acute Clinical State Event Capture is adjudicated sufficient through general clinical-information responsibilities above. Routing a critical-result alert remains distinct from capture; no specialised capture Function or replacement association is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:acute-clinical-alert-dispatch |
| canonicalId | 34d7f0bfff8f470bb97c00582f5e8a1d |
| displayName | Acute Clinical Alert Dispatch |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.AcuteCareMonitoringAndEventCoordination.AcuteClinicalAlertDispatch |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-AcuteCareMonitoringAndEventCoordination.SV-AcuteClinicalAlertDispatch |
| Snapshot publication reference | HCM-SV-acute-clinical-alert-dispatch |
| ArchiMate semantic ID | id-565f762c339959fd95d94107b088d70b |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Acute Clinical Alert Dispatch |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ACUTE-CARE.SV-ACUTE-CLINICAL-ALERT-DISPATCH |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-ACUTE-CARE.SV-ACUTE-CLINICAL-ALERT-DISPATCH"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Acute Care Monitoring & Event Coordination](business-enabling-capabilities.md#hcm-bec-acute-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:acute-clinical-alert-dispatch`) and the element's version-qualified Properties.

<a id="hcm-sv-dispense-event-ingress"></a>

## Dispense Event Ingress

Ingests pharmacy supply notifications.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:dispense-event-ingress |
| canonicalId | b81512ca8e36434e9fa29493e645d31d |
| displayName | Dispense Event Ingress |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationOrderAndDispenseManagement.DispenseEventTracking.DispenseEventIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationOrderAndDispenseManagement.FEAT-DispenseEventTracking.SV-DispenseEventIngress |
| Snapshot publication reference | HCM-SV-dispense-event-ingress |
| ArchiMate semantic ID | id-ec47b3ad39be598bb63579c4668b8791 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Dispense Event Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.SV-DISPENSE-EVENT-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-MEDICATION-ADMINISTRATION.SV-DISPENSE-EVENT-INGRESS"] |

Responsibility/framing context: [Medication Administration](business-enabling-capabilities.md#hcm-bec-medication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:dispense-event-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-medication-administration-ingress"></a>

## Medication Administration Ingress

Ingests administration records.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:medication-administration-ingress |
| canonicalId | 8d224a68f01648d39096d39ebac893df |
| displayName | Medication Administration Ingress |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationOrderAndDispenseManagement.MedicationAdministrationRecordMarIngestion.MedicationAdministrationIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationOrderAndDispenseManagement.FEAT-MedicationAdministrationRecordMarIngestion.SV-MedicationAdministrationIngress |
| Snapshot publication reference | HCM-SV-medication-administration-ingress |
| ArchiMate semantic ID | id-9704a57532cf529d9ce347836ad499d6 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Medication Administration Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.SV-MEDICATION-ADMINISTRATION-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-MEDICATION-ADMINISTRATION.SV-MEDICATION-ADMINISTRATION-INGRESS"] |

Responsibility/framing context: [Medication Administration](business-enabling-capabilities.md#hcm-bec-medication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:medication-administration-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-medication-order-ingress"></a>

## Medication Order Ingress

Ingests prescription events.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:medication-order-ingress |
| canonicalId | 3bcfd2c922aa429ba77618ac94982a56 |
| displayName | Medication Order Ingress |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationOrderAndDispenseManagement.MedicationOrderIngestion.MedicationOrderIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationOrderAndDispenseManagement.FEAT-MedicationOrderIngestion.SV-MedicationOrderIngress |
| Snapshot publication reference | HCM-SV-medication-order-ingress |
| ArchiMate semantic ID | id-1247fe9185585dbb9d73ff442fa680ca |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Medication Order Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.SV-MEDICATION-ORDER-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-MEDICATION-ADMINISTRATION.SV-MEDICATION-ORDER-INGRESS"] |

Responsibility/framing context: [Medication Administration](business-enabling-capabilities.md#hcm-bec-medication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:medication-order-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-allergy-adverse-reaction-query"></a>

## Allergy & Adverse Reaction Query

Exposes active allergy profiles to clinical decision support systems.

Feature association not established: Strategy FEAT-SD-12 — Medication Administration Tracking remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:allergy--adverse-reaction-query |
| canonicalId | 7820fc4fb98c452aba29f35d8c7e74fb |
| displayName | Allergy & Adverse Reaction Query |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationTherapyProvisionAndMonitoring.AllergyAndAdverseReactionQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationTherapyProvisionAndMonitoring.SV-AllergyAndAdverseReactionQuery |
| Snapshot publication reference | HCM-SV-allergy-adverse-reaction-query |
| ArchiMate semantic ID | id-6a7bff1805c5591b9425454e0f7e72c4 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Allergy & Adverse Reaction Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-THERAPY.SV-ALLERGY-ADVERSE-REACTION-QUERY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-MEDICATION-THERAPY.SV-ALLERGY-ADVERSE-REACTION-QUERY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Medication Therapy Provision and Monitoring](business-enabling-capabilities.md#hcm-bec-medication-therapy). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:allergy--adverse-reaction-query`) and the element's version-qualified Properties.

<a id="hcm-sv-longitudinal-medication-history-query"></a>

## Longitudinal Medication History Query

Discloses full medication timelines for medication reconciliation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:longitudinal-medication-history-query |
| canonicalId | 6a42771bbe5449ab87e8ece94519d03d |
| displayName | Longitudinal Medication History Query |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationTherapyProvisionAndMonitoring.MedicationHistoryReconciliationIngestion.LongitudinalMedicationHistoryQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationTherapyProvisionAndMonitoring.FEAT-MedicationHistoryReconciliationIngestion.SV-LongitudinalMedicationHistoryQuery |
| Snapshot publication reference | HCM-SV-longitudinal-medication-history-query |
| ArchiMate semantic ID | id-96924697c45b59728088f510012581e0 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Longitudinal Medication History Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-THERAPY.SV-LONGITUDINAL-MEDICATION-HISTORY-QUERY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-MEDICATION-THERAPY.SV-LONGITUDINAL-MEDICATION-HISTORY-QUERY"] |

Responsibility/framing context: [Medication Therapy Provision and Monitoring](business-enabling-capabilities.md#hcm-bec-medication-therapy). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:longitudinal-medication-history-query`) and the element's version-qualified Properties.

<a id="hcm-sv-order-cancellation-service"></a>

## Order Cancellation Service

Dispatches cancel directives to performing systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:order-cancellation-service |
| canonicalId | 1c8467ef0b6047329bdb899a52c23efc |
| displayName | Order Cancellation Service |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression.OrderCancellationAndModificationCoordination.OrderCancellationService |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression.FEAT-OrderCancellationAndModificationCoordination.SV-OrderCancellationService |
| Snapshot publication reference | HCM-SV-order-cancellation-service |
| ArchiMate semantic ID | id-c37ea153f6ef51c19bf8773019d2a007 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Order Cancellation Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.SV-ORDER-CANCELLATION-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.SV-ORDER-CANCELLATION-SERVICE"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:order-cancellation-service`) and the element's version-qualified Properties.

<a id="hcm-sv-order-dispatch-service"></a>

## Order Dispatch Service

Transmits orders to performing diagnostic systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:order-dispatch-service |
| canonicalId | a8b0f7284a854fdfb43a9af446aa1270 |
| displayName | Order Dispatch Service |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression.OrderDestinationResolutionAndRouting.OrderDispatchService |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression.FEAT-OrderDestinationResolutionAndRouting.SV-OrderDispatchService |
| Snapshot publication reference | HCM-SV-order-dispatch-service |
| ArchiMate semantic ID | id-f87718cc106c516dbbcc270bc5de9c86 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Order Dispatch Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.SV-ORDER-DISPATCH-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.SV-ORDER-DISPATCH-SERVICE"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:order-dispatch-service`) and the element's version-qualified Properties.

<a id="hcm-sv-order-outcome-notification"></a>

## Order Outcome Notification

Emits completion notices to ordering practitioners.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:order-outcome-notification |
| canonicalId | 1d961373ab484e5f887ecf4c7b166130 |
| displayName | Order Outcome Notification |
| friendlyName | CareAdministrationEnablement.OrderAdministration.OrderOutcomeNotification |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.SV-OrderOutcomeNotification |
| Snapshot publication reference | HCM-SV-order-outcome-notification |
| ArchiMate semantic ID | id-127c0da9a72653bdb7c1b818d609a01a |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Order Outcome Notification |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.SV-ORDER-OUTCOME-NOTIFICATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.SV-ORDER-OUTCOME-NOTIFICATION"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:order-outcome-notification`) and the element's version-qualified Properties.

<a id="hcm-sv-order-requisition-ingress"></a>

## Order Requisition Ingress

Ingests electronic orders from clinical ordering systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:order-requisition-ingress |
| canonicalId | ba824a1ec2c64d9598c409803bcf9d86 |
| displayName | Order Requisition Ingress |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression.OrderRequestIngestion.OrderRequisitionIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression.FEAT-OrderRequestIngestion.SV-OrderRequisitionIngress |
| Snapshot publication reference | HCM-SV-order-requisition-ingress |
| ArchiMate semantic ID | id-f95f6dbb613c576f912e1d482728781d |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Order Requisition Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.SV-ORDER-REQUISITION-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.SV-ORDER-REQUISITION-INGRESS"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:order-requisition-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-order-status-tracking-query"></a>

## Order Status & Tracking Query

Provides real-time order tracking to ordering clinicians.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:order-status--tracking-query |
| canonicalId | a4c049391dd64cd7af83bb5b4c7af0c5 |
| displayName | Order Status & Tracking Query |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression.OrderClosedLoopProgressionTracking.OrderStatusAndTrackingQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression.FEAT-OrderClosedLoopProgressionTracking.SV-OrderStatusAndTrackingQuery |
| Snapshot publication reference | HCM-SV-order-status-tracking-query |
| ArchiMate semantic ID | id-a58b3a12fe93507ca00859816aa57897 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Order Status & Tracking Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.SV-ORDER-STATUS-TRACKING-QUERY |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.SV-ORDER-STATUS-TRACKING-QUERY"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:order-status--tracking-query`) and the element's version-qualified Properties.

<a id="hcm-sv-procedural-documentation-ingress"></a>

## Procedural Documentation Ingress

Ingests procedural documentation, including surgical notes, into patient record streams.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:procedural-documentation-ingress |
| canonicalId | b58405366b7a43dc909ca3074503e11a |
| displayName | Procedural Documentation Ingress |
| friendlyName | CareAdministrationEnablement.ProcedureAdministration.ProcedureAndSurgicalAdministration.ProceduralDocumentationIngestion.ProceduralDocumentationIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ProcedureAdministration.CT3-BEC-ProcedureAndSurgicalAdministration.FEAT-ProceduralDocumentationIngestion.SV-ProceduralDocumentationIngress |
| Snapshot publication reference | HCM-SV-procedural-documentation-ingress |
| ArchiMate semantic ID | id-68a97fc4710358369321fe60512cdefe |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Procedural Documentation Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-PROCEDURE-ADMINISTRATION.SV-PROCEDURAL-DOCUMENTATION-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-PROCEDURE-ADMINISTRATION.SV-PROCEDURAL-DOCUMENTATION-INGRESS"] |

Responsibility/framing context: [Procedure Administration](business-enabling-capabilities.md#hcm-bec-procedure-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:procedural-documentation-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-procedure-booking-ingress"></a>

## Procedure Booking Ingress

Receives surgical/procedural requests.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:procedure-booking-ingress |
| canonicalId | 64c52e8e6f304dee8d86a5da96f927e3 |
| displayName | Procedure Booking Ingress |
| friendlyName | CareAdministrationEnablement.ProcedureAdministration.ProcedureAndSurgicalAdministration.ProcedureBookingIngestion.ProcedureBookingIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ProcedureAdministration.CT3-BEC-ProcedureAndSurgicalAdministration.FEAT-ProcedureBookingIngestion.SV-ProcedureBookingIngress |
| Snapshot publication reference | HCM-SV-procedure-booking-ingress |
| ArchiMate semantic ID | id-9c617080399e55658c15e36d94570e92 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Procedure Booking Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-PROCEDURE-ADMINISTRATION.SV-PROCEDURE-BOOKING-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-PROCEDURE-ADMINISTRATION.SV-PROCEDURE-BOOKING-INGRESS"] |

Responsibility/framing context: [Procedure Administration](business-enabling-capabilities.md#hcm-bec-procedure-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:procedure-booking-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-referral-context-query"></a>

## Referral Context Query

Provides comprehensive referral dossiers to intake triage clinicians.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:referral-context-query |
| canonicalId | d688ee73f68a4dda9380404e22b3539b |
| displayName | Referral Context Query |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ReferralIntakeAndProgression.ReferralSupportingInformation.ReferralContextQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.CT3-BEC-ReferralIntakeAndProgression.FEAT-ReferralSupportingInformation.SV-ReferralContextQuery |
| Snapshot publication reference | HCM-SV-referral-context-query |
| ArchiMate semantic ID | id-b702aed53e625fd6865f1fc539fc9ce4 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Referral Context Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.SV-REFERRAL-CONTEXT-QUERY |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.SV-REFERRAL-CONTEXT-QUERY"] |

Responsibility/framing context: [Referral Administration](business-enabling-capabilities.md#hcm-bec-referral-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:referral-context-query`) and the element's version-qualified Properties.

<a id="hcm-sv-referral-status-outcome-service"></a>

## Referral Status & Outcome Service

Exposes progression milestones to referrers and patients.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:referral-status--outcome-service |
| canonicalId | dfd0481be6124da0a791db41a5e9c238 |
| displayName | Referral Status & Outcome Service |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ReferralIntakeAndProgression.ReferralStatusAndOutcomeTracking.ReferralStatusAndOutcomeService |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.CT3-BEC-ReferralIntakeAndProgression.FEAT-ReferralStatusAndOutcomeTracking.SV-ReferralStatusAndOutcomeService |
| Snapshot publication reference | HCM-SV-referral-status-outcome-service |
| ArchiMate semantic ID | id-1feb90e9446455a1821e4bd111f85068 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Referral Status & Outcome Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.SV-REFERRAL-STATUS-OUTCOME-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.SV-REFERRAL-STATUS-OUTCOME-SERVICE"] |

Responsibility/framing context: [Referral Administration](business-enabling-capabilities.md#hcm-bec-referral-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:referral-status--outcome-service`) and the element's version-qualified Properties.

<a id="hcm-sv-referral-submission-service"></a>

## Referral Submission Service

Exposed to external GP practices and referring health facilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:referral-submission-service |
| canonicalId | 42b4e9da22764480a0bea7019927860c |
| displayName | Referral Submission Service |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ReferralIntakeAndProgression.ReferralDocumentIngestion.ReferralSubmissionService |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.CT3-BEC-ReferralIntakeAndProgression.FEAT-ReferralDocumentIngestion.SV-ReferralSubmissionService |
| Snapshot publication reference | HCM-SV-referral-submission-service |
| ArchiMate semantic ID | id-e34fc08fc40058048e901636f647f1ae |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Referral Submission Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.SV-REFERRAL-SUBMISSION-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.SV-REFERRAL-SUBMISSION-SERVICE"] |

Responsibility/framing context: [Referral Administration](business-enabling-capabilities.md#hcm-bec-referral-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:referral-submission-service`) and the element's version-qualified Properties.

<a id="hcm-sv-appointment-notification-ingress"></a>

## Appointment Notification Ingress

Ingests booking events from departmental schedulers.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:appointment-notification-ingress |
| canonicalId | 67045c71e2194f6ba19002edcea94ee0 |
| displayName | Appointment Notification Ingress |
| friendlyName | CareAdministrationEnablement.SchedulingAdministration.AppointmentAndBookingAdministration.AppointmentNotificationIngestion.AppointmentNotificationIngress |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-SchedulingAdministration.CT3-BEC-AppointmentAndBookingAdministration.FEAT-AppointmentNotificationIngestion.SV-AppointmentNotificationIngress |
| Snapshot publication reference | HCM-SV-appointment-notification-ingress |
| ArchiMate semantic ID | id-291d3634ca38569baa9acd6e7152c4da |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Appointment Notification Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-SCHEDULING-ADMINISTRATION.SV-APPOINTMENT-NOTIFICATION-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-SCHEDULING-ADMINISTRATION.SV-APPOINTMENT-NOTIFICATION-INGRESS"] |

Responsibility/framing context: [Scheduling Administration](business-enabling-capabilities.md#hcm-bec-scheduling-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:appointment-notification-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-appointment-schedule-query"></a>

## Appointment Schedule Query

Exposes consolidated patient appointment schedules across facilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:appointment-schedule-query |
| canonicalId | 2a7eb5b22dc04256aa049c2fcf84e62c |
| displayName | Appointment Schedule Query |
| friendlyName | CareAdministrationEnablement.SchedulingAdministration.AppointmentAndBookingAdministration.AppointmentStatusSynchronization.AppointmentScheduleQuery |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-SchedulingAdministration.CT3-BEC-AppointmentAndBookingAdministration.FEAT-AppointmentStatusSynchronization.SV-AppointmentScheduleQuery |
| Snapshot publication reference | HCM-SV-appointment-schedule-query |
| ArchiMate semantic ID | id-880cefbf9aa35e37858e5fcdc1e63937 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Appointment Schedule Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-SCHEDULING-ADMINISTRATION.SV-APPOINTMENT-SCHEDULE-QUERY |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-SCHEDULING-ADMINISTRATION.SV-APPOINTMENT-SCHEDULE-QUERY"] |

Responsibility/framing context: [Scheduling Administration](business-enabling-capabilities.md#hcm-bec-scheduling-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:appointment-schedule-query`) and the element's version-qualified Properties.

<a id="hcm-sv-confidentiality-restriction-evaluation"></a>

## Confidentiality Restriction Evaluation

Enforces record-level masking directives across the HIE.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:confidentiality-restriction-evaluation |
| canonicalId | 87cfcfb20b7a4570b502b4a9a53803ad |
| displayName | Confidentiality Restriction Evaluation |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientPrivacyGuardrailManagement.ConfidentialityFlagEnforcement.ConfidentialityRestrictionEvaluation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientPrivacyGuardrailManagement.FEAT-ConfidentialityFlagEnforcement.SV-ConfidentialityRestrictionEvaluation |
| Snapshot publication reference | HCM-SV-confidentiality-restriction-evaluation |
| ArchiMate semantic ID | id-3e985d75039e5955bcd36d42b4d100e3 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Confidentiality Restriction Evaluation |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-PRIVACY.SV-CONFIDENTIALITY-RESTRICTION-EVALUATION |

Responsibility/framing context: [Client Privacy Guardrail Management](business-enabling-capabilities.md#hcm-bec-client-privacy). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:confidentiality-restriction-evaluation`) and the element's version-qualified Properties.

<a id="hcm-sv-consent-decision-service"></a>

## Consent Decision Service

Provides real-time consent evaluation decisions to Health Information Control.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:consent-decision-service |
| canonicalId | e1bebefbdbef4a1981ec31341eb71dfd |
| displayName | Consent Decision Service |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientPrivacyGuardrailManagement.ConsentDirectiveEvaluation.ConsentDecisionService |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientPrivacyGuardrailManagement.FEAT-ConsentDirectiveEvaluation.SV-ConsentDecisionService |
| Snapshot publication reference | HCM-SV-consent-decision-service |
| ArchiMate semantic ID | id-f004e0762488533d95b1b0a829c9c89e |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Consent Decision Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-PRIVACY.SV-CONSENT-DECISION-SERVICE |

Responsibility/framing context: [Client Privacy Guardrail Management](business-enabling-capabilities.md#hcm-bec-client-privacy). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:consent-decision-service`) and the element's version-qualified Properties.

<a id="hcm-sv-client-relationship-query"></a>

## Client Relationship Query

Discloses verified support network members and contact details to clinical teams.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:client-relationship-query |
| canonicalId | 7b3eb2e91baa4e9c887927433979a8f6 |
| displayName | Client Relationship Query |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientRelationshipAdministration.NextOfKinAndGuardianAssociation.ClientRelationshipQuery |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientRelationshipAdministration.FEAT-NextOfKinAndGuardianAssociation.SV-ClientRelationshipQuery |
| Snapshot publication reference | HCM-SV-client-relationship-query |
| ArchiMate semantic ID | id-056f074d8cbd53b4a23bca5e4c0eb70f |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Client Relationship Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-RELATIONSHIP.SV-CLIENT-RELATIONSHIP-QUERY |

Responsibility/framing context: [Client Relationship Administration](business-enabling-capabilities.md#hcm-bec-client-relationship). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:client-relationship-query`) and the element's version-qualified Properties.

<a id="hcm-sv-representative-authority-verification"></a>

## Representative Authority Verification

Confirms legal decision-making authority for substitute consent.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:representative-authority-verification |
| canonicalId | 2d3205c785ba4db5af5868ebb6ab1495 |
| displayName | Representative Authority Verification |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientRelationshipAdministration.RelationshipValidityTracking.RepresentativeAuthorityVerification |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientRelationshipAdministration.FEAT-RelationshipValidityTracking.SV-RepresentativeAuthorityVerification |
| Snapshot publication reference | HCM-SV-representative-authority-verification |
| ArchiMate semantic ID | id-2d4621568a275be6a00cab409179d4b2 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Representative Authority Verification |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-RELATIONSHIP.SV-REPRESENTATIVE-AUTHORITY-VERIFICATION |

Responsibility/framing context: [Client Relationship Administration](business-enabling-capabilities.md#hcm-bec-client-relationship). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:representative-authority-verification`) and the element's version-qualified Properties.

<a id="hcm-sv-demographic-change-distribution"></a>

## Demographic Change Distribution

Emits governed subject demographic updates to subscribed clinical systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:demographic-change-distribution |
| canonicalId | 2c4a8c1251294885922f8ccdc3d7b069 |
| displayName | Demographic Change Distribution |
| friendlyName | EntityInformationEnablement.ClientAdministration.HealthcareSubjectProfileAdministration.DemographicContextGovernance.DemographicChangeDistribution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-HealthcareSubjectProfileAdministration.FEAT-DemographicContextGovernance.SV-DemographicChangeDistribution |
| Snapshot publication reference | HCM-SV-demographic-change-distribution |
| ArchiMate semantic ID | id-0672fb8a0d7a5f029d80931b0d3d2c60 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Demographic Change Distribution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-HEALTHCARE-SUBJECT.SV-DEMOGRAPHIC-CHANGE-DISTRIBUTION |

Responsibility/framing context: [Healthcare Subject Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-subject). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:demographic-change-distribution`) and the element's version-qualified Properties.

<a id="hcm-sv-healthcare-subject-context-resolution"></a>

## Healthcare Subject Context Resolution

Provides comprehensive demographic, language, and indigenous status context to clinical applications.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:healthcare-subject-context-resolution |
| canonicalId | 99a739efc7be4d7b92d90181ecd69d15 |
| displayName | Healthcare Subject Context Resolution |
| friendlyName | EntityInformationEnablement.ClientAdministration.HealthcareSubjectProfileAdministration.SubjectContextBinding.HealthcareSubjectContextResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-HealthcareSubjectProfileAdministration.FEAT-SubjectContextBinding.SV-HealthcareSubjectContextResolution |
| Snapshot publication reference | HCM-SV-healthcare-subject-context-resolution |
| ArchiMate semantic ID | id-ad4ecd236c2254a6823f0666a21c023d |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Healthcare Subject Context Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-HEALTHCARE-SUBJECT.SV-HEALTHCARE-SUBJECT-CONTEXT-RESOLUTION |

Responsibility/framing context: [Healthcare Subject Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-subject). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:healthcare-subject-context-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-person-identifier-correlation"></a>

## Person Identifier Correlation

Exposed across the HIE to establish patient cross-reference mappings.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:person-identifier-correlation |
| canonicalId | 71220901361e42f686faf7d852f57e45 |
| displayName | Person Identifier Correlation |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.CrossAuthorityIdentifierCorrelation.PersonIdentifierCorrelation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-CrossAuthorityIdentifierCorrelation.SV-PersonIdentifierCorrelation |
| Snapshot publication reference | HCM-SV-person-identifier-correlation |
| ArchiMate semantic ID | id-9d1f5cf6bf6f5caaa534416611b5462b |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Person Identifier Correlation |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.SV-PERSON-IDENTIFIER-CORRELATION |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:person-identifier-correlation`) and the element's version-qualified Properties.

<a id="hcm-sv-person-identifier-resolution"></a>

## Person Identifier Resolution

Exposed to Service Administration and Service Delivery to disambiguate client identities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:person-identifier-resolution |
| canonicalId | a533cf0d23364f398f2a71843cce98c4 |
| displayName | Person Identifier Resolution |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.IdentifierResolution.PersonIdentifierResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-IdentifierResolution.SV-PersonIdentifierResolution |
| Snapshot publication reference | HCM-SV-person-identifier-resolution |
| ArchiMate semantic ID | id-16d54501047750f6b5ff556db2bffdfa |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Person Identifier Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.SV-PERSON-IDENTIFIER-RESOLUTION |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:person-identifier-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-person-identity-correction"></a>

## Person Identity Correction

Exposed to authorized HIM/Medical Records administrators.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:person-identity-correction |
| canonicalId | dd5ed5ba58f94ecbbdac913c88d2b7b6 |
| displayName | Person Identity Correction |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.GovernedIdentityCorrection.PersonIdentityCorrection |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-GovernedIdentityCorrection.SV-PersonIdentityCorrection |
| Snapshot publication reference | HCM-SV-person-identity-correction |
| ArchiMate semantic ID | id-a236d718153153808c072f1d8cae8c1f |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Person Identity Correction |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.SV-PERSON-IDENTITY-CORRECTION |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:person-identity-correction`) and the element's version-qualified Properties.

<a id="hcm-sv-device-association-query"></a>

## Device Association Query

Discloses active device-to-patient and device-to-location bindings.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:device-association-query |
| canonicalId | 01637c40914945d68d261c2094b86904 |
| displayName | Device Association Query |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceUtilisationAdministration.DevicePatientLocationAssignment.DeviceAssociationQuery |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceUtilisationAdministration.FEAT-DevicePatientLocationAssignment.SV-DeviceAssociationQuery |
| Snapshot publication reference | HCM-SV-device-association-query |
| ArchiMate semantic ID | id-19f45f21d7555051ac3dfbb1ef38b693 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Device Association Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.SV-DEVICE-ASSOCIATION-QUERY |

Responsibility/framing context: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:device-association-query`) and the element's version-qualified Properties.

<a id="hcm-sv-device-definition-query"></a>

## Device Definition Query

Discloses approved medical device models and technical parameters.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:device-definition-query |
| canonicalId | 49325c789401465aa565fd7fa7109070 |
| displayName | Device Definition Query |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.ClinicalDeviceProfileAdministration.DeviceTypeSpecification.DeviceDefinitionQuery |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-ClinicalDeviceProfileAdministration.FEAT-DeviceTypeSpecification.SV-DeviceDefinitionQuery |
| Snapshot publication reference | HCM-SV-device-definition-query |
| ArchiMate semantic ID | id-2db79541f9215cdea6b465c592f650f9 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Device Definition Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.SV-DEVICE-DEFINITION-QUERY |

Responsibility/framing context: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:device-definition-query`) and the element's version-qualified Properties.

<a id="hcm-sv-device-endpoint-resolution"></a>

## Device Endpoint Resolution

Exposes network addresses for telemetry routing.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:device-endpoint-resolution |
| canonicalId | 5806d6e030b543d0867a862d5b6d8fad |
| displayName | Device Endpoint Resolution |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceOaAndMMetadataManagement.DeviceEndpointRegistration.DeviceEndpointResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceOaAndMMetadataManagement.FEAT-DeviceEndpointRegistration.SV-DeviceEndpointResolution |
| Snapshot publication reference | HCM-SV-device-endpoint-resolution |
| ArchiMate semantic ID | id-7f6b142b6d885045aa3ee74a806aca39 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Device Endpoint Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.SV-DEVICE-ENDPOINT-RESOLUTION |

Responsibility/framing context: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:device-endpoint-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-device-instance-resolution"></a>

## Device Instance Resolution

Resolves physical device instances from scanned barcodes or UDIs.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:device-instance-resolution |
| canonicalId | 14fb1e3f17dd40d88087a00c9707578d |
| displayName | Device Instance Resolution |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.ClinicalDeviceProfileAdministration.DeviceSerialTracking.DeviceInstanceResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-ClinicalDeviceProfileAdministration.FEAT-DeviceSerialTracking.SV-DeviceInstanceResolution |
| Snapshot publication reference | HCM-SV-device-instance-resolution |
| ArchiMate semantic ID | id-31fc8f96cdca5b1f9393b3f80b0b044e |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Device Instance Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.SV-DEVICE-INSTANCE-RESOLUTION |

Responsibility/framing context: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:device-instance-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-clinical-resource-availability-query"></a>

## Clinical Resource Availability Query

Exposes mobile equipment availability.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:clinical-resource-availability-query |
| canonicalId | 0e5ed162a288425aad2833ed5e07ed64 |
| displayName | Clinical Resource Availability Query |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceUtilisationAdministration.MobileClinicalEquipmentTracking.ClinicalResourceAvailabilityQuery |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceUtilisationAdministration.FEAT-MobileClinicalEquipmentTracking.SV-ClinicalResourceAvailabilityQuery |
| Snapshot publication reference | HCM-SV-clinical-resource-availability-query |
| ArchiMate semantic ID | id-b4b97dbcfd22595095666a0bf8b52c99 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Resource Availability Query |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-UTILISATION-ADMINISTRATION.SV-CLINICAL-RESOURCE-AVAILABILITY-QUERY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-CLINICAL-RESOURCE-MANAGEMENT.SV-CLINICAL-RESOURCE-AVAILABILITY-QUERY"] |

Responsibility/framing context: [Device Utilisation Administration](business-enabling-capabilities.md#hcm-bec-reviewed-device-utilisation-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:clinical-resource-availability-query`) and the element's version-qualified Properties.

<a id="hcm-sv-healthcare-service-directory-query"></a>

## Healthcare Service Directory Query

Exposes published clinical service offerings.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:healthcare-service-directory-query |
| canonicalId | c7bf105e1217441a9423706b1481d153 |
| displayName | Healthcare Service Directory Query |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.HealthcareServiceProfileAdministration.ServiceCataloguePublishing.HealthcareServiceDirectoryQuery |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-HealthcareServiceProfileAdministration.FEAT-ServiceCataloguePublishing.SV-HealthcareServiceDirectoryQuery |
| Snapshot publication reference | HCM-SV-healthcare-service-directory-query |
| ArchiMate semantic ID | id-3f0037ab838758889d1bf105ee42f651 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Healthcare Service Directory Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.SV-HEALTHCARE-SERVICE-DIRECTORY-QUERY |

Responsibility/framing context: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:healthcare-service-directory-query`) and the element's version-qualified Properties.

<a id="hcm-sv-service-availability-query"></a>

## Service Availability Query

Discloses operational intake hours and open status.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:service-availability-query |
| canonicalId | a3846cb2960a4000a5db9ef18942ee20 |
| displayName | Service Availability Query |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.HealthcareServiceProfileAdministration.OperatingScheduleSpecification.ServiceAvailabilityQuery |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-HealthcareServiceProfileAdministration.FEAT-OperatingScheduleSpecification.SV-ServiceAvailabilityQuery |
| Snapshot publication reference | HCM-SV-service-availability-query |
| ArchiMate semantic ID | id-fc36267c36615fb7b9d3b65db4231071 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Service Availability Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.SV-SERVICE-AVAILABILITY-QUERY |

Responsibility/framing context: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:service-availability-query`) and the element's version-qualified Properties.

<a id="hcm-sv-service-eligibility-evaluation"></a>

## Service Eligibility Evaluation

Evaluates referral eligibility for intake coordinators.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:service-eligibility-evaluation |
| canonicalId | 7e005ce374f347088ae854442fc3edef |
| displayName | Service Eligibility Evaluation |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.HealthcareServiceProfileAdministration.EligibilityAndCatchmentSpecification.ServiceEligibilityEvaluation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-HealthcareServiceProfileAdministration.FEAT-EligibilityAndCatchmentSpecification.SV-ServiceEligibilityEvaluation |
| Snapshot publication reference | HCM-SV-service-eligibility-evaluation |
| ArchiMate semantic ID | id-c2f03bf3f778546b85c249ec02339058 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Service Eligibility Evaluation |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.SV-SERVICE-ELIGIBILITY-EVALUATION |

Responsibility/framing context: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:service-eligibility-evaluation`) and the element's version-qualified Properties.

<a id="hcm-sv-service-provision-resolution"></a>

## Service Provision Resolution

Exposes multi-dimensional resolution queries answering who, where, and when healthcare services are delivered.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:service-provision-resolution |
| canonicalId | e54866d88b1e442f8ea170f06f98028b |
| displayName | Service Provision Resolution |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.HealthcareServiceProfileAdministration.ServiceLocationProviderBinding.ServiceProvisionResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.CT3-BEC-HealthcareServiceProfileAdministration.FEAT-ServiceLocationProviderBinding.SV-ServiceProvisionResolution |
| Snapshot publication reference | HCM-SV-service-provision-resolution |
| ArchiMate semantic ID | id-19fda295a5e351489a17e16f65373ed0 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Service Provision Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.SV-SERVICE-PROVISION-RESOLUTION |

Responsibility/framing context: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:service-provision-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-care-place-specification-lookup"></a>

## Care-Place Specification Lookup

Discloses care-place physical capabilities to Bed & Care-Place Management.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:care-place-specification-lookup |
| canonicalId | 43b011b0c233408eaf6e555ecbf05d9e |
| displayName | Care-Place Specification Lookup |
| friendlyName | EntityInformationEnablement.LocationAdministration.CarePlaceDefinitionAdministration.AtomicCarePlaceModeling.CarePlaceSpecificationLookup |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.CT3-BEC-CarePlaceDefinitionAdministration.FEAT-AtomicCarePlaceModeling.SV-CarePlaceSpecificationLookup |
| Snapshot publication reference | HCM-SV-care-place-specification-lookup |
| ArchiMate semantic ID | id-97752cf214e65111be413129b58575ff |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Care-Place Specification Lookup |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.SV-CARE-PLACE-SPECIFICATION-LOOKUP |

Responsibility/framing context: [Location Administration](business-enabling-capabilities.md#hcm-bec-location-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:care-place-specification-lookup`) and the element's version-qualified Properties.

<a id="hcm-sv-location-directory-query"></a>

## Location Directory Query

Exposes location profiles and geospatial addresses.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:location-directory-query |
| canonicalId | 5ef66362eb674794b335c435227b0c95 |
| displayName | Location Directory Query |
| friendlyName | EntityInformationEnablement.LocationAdministration.HealthcareLocationProfileAdministration.LocationGeospatialAndAddressResolution.LocationDirectoryQuery |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.CT3-BEC-HealthcareLocationProfileAdministration.FEAT-LocationGeospatialAndAddressResolution.SV-LocationDirectoryQuery |
| Snapshot publication reference | HCM-SV-location-directory-query |
| ArchiMate semantic ID | id-a8afe8cb79b95984a1effcfe7840e3a0 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Location Directory Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.SV-LOCATION-DIRECTORY-QUERY |

Responsibility/framing context: [Location Administration](business-enabling-capabilities.md#hcm-bec-location-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:location-directory-query`) and the element's version-qualified Properties.

<a id="hcm-sv-location-hierarchy-resolution"></a>

## Location Hierarchy Resolution

Discloses physical containment and navigation paths for clinical facilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:location-hierarchy-resolution |
| canonicalId | 389af7aaa9d74ab8b42e55f0fa72ef99 |
| displayName | Location Hierarchy Resolution |
| friendlyName | EntityInformationEnablement.LocationAdministration.HealthcareLocationProfileAdministration.PhysicalHierarchyModeling.LocationHierarchyResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.CT3-BEC-HealthcareLocationProfileAdministration.FEAT-PhysicalHierarchyModeling.SV-LocationHierarchyResolution |
| Snapshot publication reference | HCM-SV-location-hierarchy-resolution |
| ArchiMate semantic ID | id-d711eba24c665c33ad39db9023c6cb83 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Location Hierarchy Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.SV-LOCATION-HIERARCHY-RESOLUTION |

Responsibility/framing context: [Location Administration](business-enabling-capabilities.md#hcm-bec-location-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:location-hierarchy-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-organisation-contact-lookup"></a>

## Organisation Contact Lookup

Provides official operational contact details.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:organisation-contact-lookup |
| canonicalId | 551f1e73fb73449a83f3155cb8158f30 |
| displayName | Organisation Contact Lookup |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.HealthcareOrganisationProfileAdministration.DirectoryContactManagement.OrganisationContactLookup |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-HealthcareOrganisationProfileAdministration.FEAT-DirectoryContactManagement.SV-OrganisationContactLookup |
| Snapshot publication reference | HCM-SV-organisation-contact-lookup |
| ArchiMate semantic ID | id-aed999ba3cfb55309a710d4752bb144b |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Organisation Contact Lookup |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.SV-ORGANISATION-CONTACT-LOOKUP |

Responsibility/framing context: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:organisation-contact-lookup`) and the element's version-qualified Properties.

<a id="hcm-sv-organisation-directory-query"></a>

## Organisation Directory Query

Discloses healthcare organisation profiles across the region.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:organisation-directory-query |
| canonicalId | c5acf1dc97874d0a8264f9a5c1b735fa |
| displayName | Organisation Directory Query |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.HealthcareOrganisationProfileAdministration.OrganisationProfileManagement.OrganisationDirectoryQuery |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-HealthcareOrganisationProfileAdministration.FEAT-OrganisationProfileManagement.SV-OrganisationDirectoryQuery |
| Snapshot publication reference | HCM-SV-organisation-directory-query |
| ArchiMate semantic ID | id-cc22b9345d5d5d55bc0bf2bf1127230f |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Organisation Directory Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.SV-ORGANISATION-DIRECTORY-QUERY |

Responsibility/framing context: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:organisation-directory-query`) and the element's version-qualified Properties.

<a id="hcm-sv-organisation-verification-service"></a>

## Organisation Verification Service

Provides verified organisation credentials to external and internal partners.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:organisation-verification-service |
| canonicalId | 223b2cbe762b40efb9f05bb5cdd66bcb |
| displayName | Organisation Verification Service |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.HealthcareOrganisationProfileAdministration.NationalOrganisationVerification.OrganisationVerificationService |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-HealthcareOrganisationProfileAdministration.FEAT-NationalOrganisationVerification.SV-OrganisationVerificationService |
| Snapshot publication reference | HCM-SV-organisation-verification-service |
| ArchiMate semantic ID | id-975b6adcaec55a84b41eeff62e424af2 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Organisation Verification Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.SV-ORGANISATION-VERIFICATION-SERVICE |

Responsibility/framing context: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:organisation-verification-service`) and the element's version-qualified Properties.

<a id="hcm-sv-organisational-hierarchy-resolution"></a>

## Organisational Hierarchy Resolution

Resolves department nesting and reporting hierarchies.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:organisational-hierarchy-resolution |
| canonicalId | c9187704dd7f49f69f9e674c16e9181b |
| displayName | Organisational Hierarchy Resolution |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.HealthcareOrganisationProfileAdministration.HierarchicalStructureNavigation.OrganisationalHierarchyResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.CT3-BEC-HealthcareOrganisationProfileAdministration.FEAT-HierarchicalStructureNavigation.SV-OrganisationalHierarchyResolution |
| Snapshot publication reference | HCM-SV-organisational-hierarchy-resolution |
| ArchiMate semantic ID | id-6162511d477f5f7396daafea25feb438 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Organisational Hierarchy Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.SV-ORGANISATIONAL-HIERARCHY-RESOLUTION |

Responsibility/framing context: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:organisational-hierarchy-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-clinical-privilege-verification"></a>

## Clinical Privilege Verification

Verifies procedural and prescribing authority for clinical ordering systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:clinical-privilege-verification |
| canonicalId | f6599b4283194577a977bfc86ea854c2 |
| displayName | Clinical Privilege Verification |
| friendlyName | HealthServiceOperationsEnablement.ClinicalQualificationAdministration.ProviderClinicalQualificationTracking.ClinicalPrivilegingRecord.ClinicalPrivilegeVerification |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalQualificationAdministration.CT3-BEC-ProviderClinicalQualificationTracking.FEAT-ClinicalPrivilegingRecord.SV-ClinicalPrivilegeVerification |
| Snapshot publication reference | HCM-SV-clinical-privilege-verification |
| ArchiMate semantic ID | id-2d89fceee6cf5de3aedffbc5efccd66c |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Privilege Verification |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.SV-CLINICAL-PRIVILEGE-VERIFICATION |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:clinical-privilege-verification`) and the element's version-qualified Properties.

<a id="hcm-sv-practitioner-affiliation-resolution"></a>

## Practitioner Affiliation Resolution

Discloses organizational ties and practice locations.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:practitioner-affiliation-resolution |
| canonicalId | 65baca2a21f040d49be74fab1727f96d |
| displayName | Practitioner Affiliation Resolution |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerRoleAdministration.PractitionerOrganisationAffiliation.PractitionerAffiliationResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerRoleAdministration.FEAT-PractitionerOrganisationAffiliation.SV-PractitionerAffiliationResolution |
| Snapshot publication reference | HCM-SV-practitioner-affiliation-resolution |
| ArchiMate semantic ID | id-daad446c0f7e5e3b87d846afb6c2671f |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Practitioner Affiliation Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.SV-PRACTITIONER-AFFILIATION-RESOLUTION |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:practitioner-affiliation-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-practitioner-directory-query"></a>

## Practitioner Directory Query

Provides provider directory lookups across the healthcare network.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:practitioner-directory-query |
| canonicalId | ed8e6a3d891340fe92fc3201abd15c46 |
| displayName | Practitioner Directory Query |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerDirectoryQuery |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.SV-PractitionerDirectoryQuery |
| Snapshot publication reference | HCM-SV-practitioner-directory-query |
| ArchiMate semantic ID | id-dd5327a07b345531bbf1436a950d151b |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Practitioner Directory Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.SV-PRACTITIONER-DIRECTORY-QUERY |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:practitioner-directory-query`) and the element's version-qualified Properties.

<a id="hcm-sv-practitioner-endpoint-lookup"></a>

## Practitioner Endpoint Lookup

Provides secure message routing destinations for clinical communication.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:practitioner-endpoint-lookup |
| canonicalId | 2efb21629acf41e9b4f5c59db00721d0 |
| displayName | Practitioner Endpoint Lookup |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerRoleAdministration.ElectronicEndpointBinding.PractitionerEndpointLookup |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerRoleAdministration.FEAT-ElectronicEndpointBinding.SV-PractitionerEndpointLookup |
| Snapshot publication reference | HCM-SV-practitioner-endpoint-lookup |
| ArchiMate semantic ID | id-54e83e2d7c2d58f880e9171415f2d743 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Practitioner Endpoint Lookup |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.SV-PRACTITIONER-ENDPOINT-LOOKUP |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:practitioner-endpoint-lookup`) and the element's version-qualified Properties.

<a id="hcm-sv-practitioner-role-resolution"></a>

## Practitioner Role Resolution

Resolves the specific clinical capacities of a practitioner.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:practitioner-role-resolution |
| canonicalId | c3e5c04e2a194b4a8d49b8776fce2c51 |
| displayName | Practitioner Role Resolution |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerRoleAdministration.RoleAndSpecialtyModeling.PractitionerRoleResolution |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerRoleAdministration.FEAT-RoleAndSpecialtyModeling.SV-PractitionerRoleResolution |
| Snapshot publication reference | HCM-SV-practitioner-role-resolution |
| ArchiMate semantic ID | id-8a0aa9ba949a5b8b8ad5be5dc83be377 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Practitioner Role Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.SV-PRACTITIONER-ROLE-RESOLUTION |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:practitioner-role-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-practitioner-verification-service"></a>

## Practitioner Verification Service

Discloses verified registration and status to credentialing and clinical systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:practitioner-verification-service |
| canonicalId | adf039a87e9243a1b82612a4919f6f14 |
| displayName | Practitioner Verification Service |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerProfileAdministration.NationalPractitionerVerification.PractitionerVerificationService |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerProfileAdministration.FEAT-NationalPractitionerVerification.SV-PractitionerVerificationService |
| Snapshot publication reference | HCM-SV-practitioner-verification-service |
| ArchiMate semantic ID | id-0a4f74f6f31f5e37b8837f3a50cc7277 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Practitioner Verification Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.SV-PRACTITIONER-VERIFICATION-SERVICE |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:practitioner-verification-service`) and the element's version-qualified Properties.

<a id="hcm-sv-authorised-research-information-export"></a>

## Authorised Research Information Export

Provide an approved research consumer with purpose-qualified existing health information under its release contract.

**Overview**

The service exposes the permitted release and its applicable use basis rather than unrestricted clinical repository access.

**Boundary**

No new research-governance mandate or universal release format is allocated.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:service-authorised-research-information-export |
| canonicalId | 134e1ff0331040b3a8069c9dc5e331d4 |
| displayName | Authorised Research Information Export |
| friendlyName | HealthInformaticsManagement.ResearchInformationCuration.AuthorisedResearchInformationExport |
| canonicalName | CT1-BEC-HealthInformaticsManagement.CT2-BEC-ResearchInformationCuration.SV-AuthorisedResearchInformationExport |
| Snapshot publication reference | HCM-SV-authorised-research-information-export |
| ArchiMate semantic ID | id-c4599a96d4515815b0f9e3f0bea1cb3a |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT2-BEC-AUTHORISED-RESEARCH-INFORMATION-PROVISION.SV-AUTHORISED-RESEARCH-INFORMATION-EXPORT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT3-BEC-AUTHORISED-RESEARCH-INFORMATION-PROVISION.SV-AUTHORISED-RESEARCH-INFORMATION-EXPORT"] |

Responsibility/framing context: [Research Information Curation](business-enabling-capabilities.md#hcm-bec-authorised-research-information-provision). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:service-authorised-research-information-export`) and the element's version-qualified Properties.

<a id="hcm-sv-bed-availability-query"></a>

## Bed Availability Query

Discloses real-time bed availability across hospital directorates.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:bed-availability-query |
| canonicalId | c51894eccd7b45019c2f084993f4cfc6 |
| displayName | Bed Availability Query |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement.BedAndCarePlaceStateAdministration.BedAvailabilityTracking.BedAvailabilityQuery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement.CT3-BEC-BedAndCarePlaceStateAdministration.FEAT-BedAvailabilityTracking.SV-BedAvailabilityQuery |
| Snapshot publication reference | HCM-SV-bed-availability-query |
| ArchiMate semantic ID | id-f7ff8ad70b355db3876f450936497684 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bed Availability Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.SV-BED-AVAILABILITY-QUERY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-BED-CARE-PLACE-MANAGEMENT.SV-BED-AVAILABILITY-QUERY"] |

Responsibility/framing context: [Bed & Care-Place Management](business-enabling-capabilities.md#hcm-bec-bed-care-place-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:bed-availability-query`) and the element's version-qualified Properties.

<a id="hcm-sv-bed-turnover-lifecycle-service"></a>

## Bed Turnover Lifecycle Service

Coordinates turnover requests with environmental services.

Additional Feature responsibility: FEAT-HSO-13 — Bed Readiness Notification requires the existing turnover behaviour/exposure to notify bed allocation managers and clinical units when a cleaned bed is ready. Readiness notification remains distinct from availability query and from physical placement; no new lifecycle state or Service is introduced.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:bed-turnover-lifecycle-service |
| canonicalId | f0ab3847b1bf41869a85b1a5ef217429 |
| displayName | Bed Turnover Lifecycle Service |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement.BedTurnoverLifecycleService |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement.SV-BedTurnoverLifecycleService |
| Snapshot publication reference | HCM-SV-bed-turnover-lifecycle-service |
| ArchiMate semantic ID | id-99d84888a1b35e738a2822b931fdb7cb |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bed Turnover Lifecycle Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.SV-BED-TURNOVER-LIFECYCLE-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-BED-CARE-PLACE-MANAGEMENT.SV-BED-TURNOVER-LIFECYCLE-SERVICE"] |

Responsibility/framing context: [Bed & Care-Place Management](business-enabling-capabilities.md#hcm-bec-bed-care-place-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:bed-turnover-lifecycle-service`) and the element's version-qualified Properties.

<a id="hcm-sv-clinic-queue-telemetry-service"></a>

## Clinic Queue Telemetry Service

Discloses real-time clinic waiting times and queue states.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:clinic-queue-telemetry-service |
| canonicalId | c720457349e644179c98d13be59bd581 |
| displayName | Clinic Queue Telemetry Service |
| friendlyName | HealthServiceOperationsEnablement.ClinicAndPracticeOperations.SessionAndQueueCoordination.OperationalQueueProgression.ClinicQueueTelemetryService |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicAndPracticeOperations.CT3-BEC-SessionAndQueueCoordination.FEAT-OperationalQueueProgression.SV-ClinicQueueTelemetryService |
| Snapshot publication reference | HCM-SV-clinic-queue-telemetry-service |
| ArchiMate semantic ID | id-af013a86ebb250d79556d64fbb65ce11 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinic Queue Telemetry Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.SV-CLINIC-QUEUE-TELEMETRY-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-CLINIC-PRACTICE-OPERATIONS.SV-CLINIC-QUEUE-TELEMETRY-SERVICE"] |

Responsibility/framing context: [Clinic & Practice Operations](business-enabling-capabilities.md#hcm-bec-clinic-practice-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:clinic-queue-telemetry-service`) and the element's version-qualified Properties.

<a id="hcm-sv-specimen-transit-tracking-service"></a>

## Specimen Transit Tracking Service

Exposes chain-of-custody tracking.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:specimen-transit-tracking-service |
| canonicalId | 100e740770bc4ab28cfe3c9af4b541cd |
| displayName | Specimen Transit Tracking Service |
| friendlyName | HealthServiceOperationsEnablement.ClinicalLogisticsCoordination.SpecimenMovementTraceability.PathologySpecimenTransportTracking.SpecimenTransitTrackingService |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalLogisticsCoordination.CT3-BEC-SpecimenMovementTraceability.FEAT-PathologySpecimenTransportTracking.SV-SpecimenTransitTrackingService |
| Snapshot publication reference | HCM-SV-specimen-transit-tracking-service |
| ArchiMate semantic ID | id-658d398af45950afa8c57e1f8ebc9f91 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Specimen Transit Tracking Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-LOGISTICS-COORDINATION.SV-SPECIMEN-TRANSIT-TRACKING-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-CLINICAL-LOGISTICS-COORDINATION.SV-SPECIMEN-TRANSIT-TRACKING-SERVICE"] |

Responsibility/framing context: [Clinical Logistics Coordination](business-enabling-capabilities.md#hcm-bec-clinical-logistics-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:specimen-transit-tracking-service`) and the element's version-qualified Properties.

<a id="hcm-sv-discharge-readiness-telemetry"></a>

## Discharge Readiness Telemetry

Discloses discharge barrier checklists and planned departure times.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:discharge-readiness-telemetry |
| canonicalId | 03d01a9b024c4497badf3fe011a8dd20 |
| displayName | Discharge Readiness Telemetry |
| friendlyName | HealthServiceOperationsEnablement.DischargeManagement.DischargeReadinessTelemetry |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-DischargeManagement.SV-DischargeReadinessTelemetry |
| Snapshot publication reference | HCM-SV-discharge-readiness-telemetry |
| ArchiMate semantic ID | id-d42da85c238f5fcfabda0f3107f7e1e9 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Discharge Readiness Telemetry |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-DISCHARGE-MANAGEMENT.SV-DISCHARGE-READINESS-TELEMETRY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-DISCHARGE-MANAGEMENT.SV-DISCHARGE-READINESS-TELEMETRY"] |

Responsibility/framing context: [Discharge Management](business-enabling-capabilities.md#hcm-bec-discharge-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:discharge-readiness-telemetry`) and the element's version-qualified Properties.

<a id="hcm-sv-ed-operational-status-query"></a>

## ED Operational Status Query

Discloses facility ED occupancy and surge level.

Capacity boundary: Existing departmental capacity responsibility is clarified, not duplicated. Care-place state contributes to service capacity; capacity coordination neither performs clinical triage nor establishes clinical acuity assertions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:ed-operational-status-query |
| canonicalId | f07828653c06425f9effd92b1d9886c9 |
| displayName | ED Operational Status Query |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EdOperationalStatusQuery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.SV-EdOperationalStatusQuery |
| Snapshot publication reference | HCM-SV-ed-operational-status-query |
| ArchiMate semantic ID | id-d688256eae67521fa1e6b33e4f1f2724 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | ED Operational Status Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.SV-ED-OPERATIONAL-STATUS-QUERY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.SV-ED-OPERATIONAL-STATUS-QUERY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Emergency Department Operations](business-enabling-capabilities.md#hcm-bec-emergency-department-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:ed-operational-status-query`) and the element's version-qualified Properties.

<a id="hcm-sv-ed-triage-stream-ingress"></a>

## ED Triage Stream Ingress

Ingests emergency triage presentations.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:ed-triage-stream-ingress |
| canonicalId | 677947e357b541f58f36e9b4e36157e8 |
| displayName | ED Triage Stream Ingress |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyCareProgression.EmergencyDepartmentArrivalAndTriageIngestion.EdTriageStreamIngress |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyCareProgression.FEAT-EmergencyDepartmentArrivalAndTriageIngestion.SV-EdTriageStreamIngress |
| Snapshot publication reference | HCM-SV-ed-triage-stream-ingress |
| ArchiMate semantic ID | id-0b4f72b08c0c5b5bb3f6cfdae114aa83 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | ED Triage Stream Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-CARE.SV-ED-TRIAGE-STREAM-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-EMERGENCY-CARE.SV-ED-TRIAGE-STREAM-INGRESS"] |

Responsibility/framing context: [Emergency Care Progression](business-enabling-capabilities.md#hcm-bec-emergency-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:ed-triage-stream-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-emergency-fast-path-lhr-query"></a>

## Emergency Fast-Path LHR Query

Provides low-latency emergency record summaries.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:emergency-fast-path-lhr-query |
| canonicalId | 0588ef78691143cdb2d884960811db1e |
| displayName | Emergency Fast-Path LHR Query |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyCareProgression.EmergencyCareCrossFacilityCorrelation.EmergencyFastPathLhrQuery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyCareProgression.FEAT-EmergencyCareCrossFacilityCorrelation.SV-EmergencyFastPathLhrQuery |
| Snapshot publication reference | HCM-SV-emergency-fast-path-lhr-query |
| ArchiMate semantic ID | id-5a59cea7c21854d995df5329d002535a |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Emergency Fast-Path LHR Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-CARE.SV-EMERGENCY-FAST-PATH-LHR-QUERY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-EMERGENCY-CARE.SV-EMERGENCY-FAST-PATH-LHR-QUERY"] |

Responsibility/framing context: [Emergency Care Progression](business-enabling-capabilities.md#hcm-bec-emergency-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:emergency-fast-path-lhr-query`) and the element's version-qualified Properties.

<a id="hcm-sv-staff-presence-telemetry-query"></a>

## Staff Presence Telemetry Query

Discloses on-duty staff availability by hospital zone.

Feature association not established: FEAT-HSO-16 — Mobile Worker Task Dispatch retains its Strategy identity with the integration boundary below. Presence/zone tracking remains distinct from mobile work-management integration; no replacement association to presence is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:staff-presence-telemetry-query |
| canonicalId | b3cd0d8e40f44b9fa2b64367237345fa |
| displayName | Staff Presence Telemetry Query |
| friendlyName | HealthServiceOperationsEnablement.MobileStaffManagement.StaffPresenceTelemetryQuery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-MobileStaffManagement.SV-StaffPresenceTelemetryQuery |
| Snapshot publication reference | HCM-SV-staff-presence-telemetry-query |
| ArchiMate semantic ID | id-a7e6a3c418a857bb9a66fed528ebdc85 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Staff Presence Telemetry Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.SV-STAFF-PRESENCE-TELEMETRY-QUERY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-MOBILE-STAFF-MANAGEMENT.SV-STAFF-PRESENCE-TELEMETRY-QUERY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Mobile Staff Management](business-enabling-capabilities.md#hcm-bec-mobile-staff-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:staff-presence-telemetry-query`) and the element's version-qualified Properties.

<a id="hcm-sv-on-call-specialist-lookup"></a>

## On-Call Specialist Lookup

Discloses active on-call contacts for switchboard and emergency teams.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:on-call-specialist-lookup |
| canonicalId | e0ba307dc51e47a395c1eac9fe728a98 |
| displayName | On-Call Specialist Lookup |
| friendlyName | HealthServiceOperationsEnablement.OnCallManagement.OnCallAvailabilityAndContactResolution.ActiveOnCallProviderResolution.OnCallSpecialistLookup |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OnCallManagement.CT3-BEC-OnCallAvailabilityAndContactResolution.FEAT-ActiveOnCallProviderResolution.SV-OnCallSpecialistLookup |
| Snapshot publication reference | HCM-SV-on-call-specialist-lookup |
| ArchiMate semantic ID | id-2fea1124c0455826b976bde2dfe87067 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | On-Call Specialist Lookup |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-ON-CALL-MANAGEMENT.SV-ON-CALL-SPECIALIST-LOOKUP |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-ON-CALL-MANAGEMENT.SV-ON-CALL-SPECIALIST-LOOKUP"] |

Responsibility/framing context: [On-Call Management](business-enabling-capabilities.md#hcm-bec-on-call-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:on-call-specialist-lookup`) and the element's version-qualified Properties.

<a id="hcm-sv-ambulatory-session-status-query"></a>

## Ambulatory Session Status Query

Exposes clinic room occupancy.

Exposure qualification: Query behaviour and Outpatient Operations ownership remain established; a meaningful outside consumer/mandate is not identified by the current description. That exposure relationship remains unestablished rather than populated with an inferred Actor, Role or Capability.

Capacity boundary: Operational coordination does not acquire authoritative booking/rostering or clinical-work management. Room-occupancy query remains narrower than the capacity responsibility; no enlarged exposure or new named Service is inferred.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:ambulatory-session-status-query |
| canonicalId | a790a785f2e148d7a1c99655d53db4e9 |
| displayName | Ambulatory Session Status Query |
| friendlyName | HealthServiceOperationsEnablement.OutpatientOperations.AmbulatorySessionStatusQuery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OutpatientOperations.SV-AmbulatorySessionStatusQuery |
| Snapshot publication reference | HCM-SV-ambulatory-session-status-query |
| ArchiMate semantic ID | id-95b5cf1da0db5b56b0b5649c34a0a923 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Ambulatory Session Status Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OUTPATIENT-OPERATIONS.SV-AMBULATORY-SESSION-STATUS-QUERY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-OUTPATIENT-OPERATIONS.SV-AMBULATORY-SESSION-STATUS-QUERY"] |
| Unresolved dimensions | ["feature_association","exposure_consumer"] |

Responsibility/framing context: [Outpatient Operations](business-enabling-capabilities.md#hcm-bec-outpatient-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:ambulatory-session-status-query`) and the element's version-qualified Properties.

<a id="hcm-sv-patient-transport-tracking-service"></a>

## Patient Transport Tracking Service

Provides real-time transit telemetry.

Additional Feature responsibility: FEAT-HSO-24 — Transport Request Ingestion is directly supported by the request stage of this same Function/Process. It ingests internal portering and external ambulance/inter-hospital requests with clinical mobility requirements; request ingestion is distinct from dispatch, transit and arrival.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:patient-transport-tracking-service |
| canonicalId | 8d71168ac7da48e990757b2a0ae3670f |
| displayName | Patient Transport Tracking Service |
| friendlyName | HealthServiceOperationsEnablement.PatientTransport.PatientTransportTrackingService |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-PatientTransport.SV-PatientTransportTrackingService |
| Snapshot publication reference | HCM-SV-patient-transport-tracking-service |
| ArchiMate semantic ID | id-386fe234615b58488262767a5f2f337e |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Patient Transport Tracking Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-PATIENT-TRANSPORT.SV-PATIENT-TRANSPORT-TRACKING-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-PATIENT-TRANSPORT.SV-PATIENT-TRANSPORT-TRACKING-SERVICE"] |

Responsibility/framing context: [Patient Transport](business-enabling-capabilities.md#hcm-bec-patient-transport). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:patient-transport-tracking-service`) and the element's version-qualified Properties.

<a id="hcm-sv-regional-health-system-capacity-query"></a>

## Regional Health System Capacity Query

Provides executive operational situational awareness.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:regional-health-system-capacity-query |
| canonicalId | ecbb1944572d44dbba55aaf565559d13 |
| displayName | Regional Health System Capacity Query |
| friendlyName | HealthServiceOperationsEnablement.ServiceCapacityManagement.ServiceDemandAndCapacityCoordination.OperationalCapacityMetricAggregation.RegionalHealthSystemCapacityQuery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ServiceCapacityManagement.CT3-BEC-ServiceDemandAndCapacityCoordination.FEAT-OperationalCapacityMetricAggregation.SV-RegionalHealthSystemCapacityQuery |
| Snapshot publication reference | HCM-SV-regional-health-system-capacity-query |
| ArchiMate semantic ID | id-f80ca2f4577658349ca52a2fb996c3d8 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Regional Health System Capacity Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-SERVICE-CAPACITY-MANAGEMENT.SV-REGIONAL-HEALTH-SYSTEM-CAPACITY-QUERY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-CAPACITY-MANAGEMENT.SV-REGIONAL-HEALTH-SYSTEM-CAPACITY-QUERY"] |

Responsibility/framing context: [Service Capacity Management](business-enabling-capabilities.md#hcm-bec-service-capacity-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:regional-health-system-capacity-query`) and the element's version-qualified Properties.

<a id="hcm-sv-theatre-case-progression-telemetry"></a>

## Theatre Case Progression Telemetry

Emits real-time surgical milestone updates.

Additional Feature responsibility: FEAT-HSO-06 — Perioperative Resource Notification is realised by dispatching progression-triggered alerts to portering, sterilisation and recovery teams within this same case-progression behaviour/exposure. Milestone observation alone does not establish notification; no new Function or Service is created.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:theatre-case-progression-telemetry |
| canonicalId | 15fca4d52810448785ca3c70b899a294 |
| displayName | Theatre Case Progression Telemetry |
| friendlyName | HealthServiceOperationsEnablement.TheatreOperations.TheatreCaseProgressionTelemetry |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-TheatreOperations.SV-TheatreCaseProgressionTelemetry |
| Snapshot publication reference | HCM-SV-theatre-case-progression-telemetry |
| ArchiMate semantic ID | id-9f71162dc5935336b0dca5621625b94e |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Theatre Case Progression Telemetry |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS.SV-THEATRE-CASE-PROGRESSION-TELEMETRY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-THEATRE-OPERATIONS.SV-THEATRE-CASE-PROGRESSION-TELEMETRY"] |

Responsibility/framing context: [Theatre Operations](business-enabling-capabilities.md#hcm-bec-theatre-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:theatre-case-progression-telemetry`) and the element's version-qualified Properties.

<a id="hcm-sv-ward-operational-summary-query"></a>

## Ward Operational Summary Query

Discloses ward-level operational telemetry.

Capacity boundary: Bed & Care-Place Management contributes resource state. Ward capacity is assessed in its service context; an available bed alone does not establish capacity to accept a patient. Source staffing and clinical assertions retain their authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:ward-operational-summary-query |
| canonicalId | 55a7f84dfb4142e2aa96c5253b4b6394 |
| displayName | Ward Operational Summary Query |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.WardOperationalSummaryQuery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.SV-WardOperationalSummaryQuery |
| Snapshot publication reference | HCM-SV-ward-operational-summary-query |
| ArchiMate semantic ID | id-55aff8b8a30455a99f9eb585ab782f10 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Ward Operational Summary Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.SV-WARD-OPERATIONAL-SUMMARY-QUERY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-WARD-OPERATIONS.SV-WARD-OPERATIONAL-SUMMARY-QUERY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Ward Operations](business-enabling-capabilities.md#hcm-bec-ward-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:ward-operational-summary-query`) and the element's version-qualified Properties.

<a id="hcm-sv-discharge-dossier-packaging-service"></a>

## Discharge Dossier Packaging Service

Emits canonical discharge summaries to GPs and community providers.

Feature association not established: FEAT-SD-08 — Inpatient Clinical Progression Tracking is adjudicated sufficient through general clinical-information responsibilities above. Discharge-dossier assembly/publication remains distinct; no separate clinical-tracking Function or replacement association is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:discharge-dossier-packaging-service |
| canonicalId | 02a17f4f9b0a425299141261130a1768 |
| displayName | Discharge Dossier Packaging Service |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.MultidisciplinaryInpatientCareCoordination.DischargeDossierPackagingService |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-MultidisciplinaryInpatientCareCoordination.SV-DischargeDossierPackagingService |
| Snapshot publication reference | HCM-SV-discharge-dossier-packaging-service |
| ArchiMate semantic ID | id-fe0a0983a3f657c686030a6cfb383e42 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Discharge Dossier Packaging Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-INPATIENT-CARE.SV-DISCHARGE-DOSSIER-PACKAGING-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-INPATIENT-CARE.SV-DISCHARGE-DOSSIER-PACKAGING-SERVICE"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Multidisciplinary Inpatient Care Coordination](business-enabling-capabilities.md#hcm-bec-inpatient-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:discharge-dossier-packaging-service`) and the element's version-qualified Properties.

<a id="hcm-sv-ward-round-clinical-briefing-query"></a>

## Ward Round Clinical Briefing Query

Discloses consolidated 24-hour clinical briefing views.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:ward-round-clinical-briefing-query |
| canonicalId | 2b0f17a8a2da4c0f92a5ad4a17ee526b |
| displayName | Ward Round Clinical Briefing Query |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.MultidisciplinaryInpatientCareCoordination.InpatientRoundingContextProvision.WardRoundClinicalBriefingQuery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-MultidisciplinaryInpatientCareCoordination.FEAT-InpatientRoundingContextProvision.SV-WardRoundClinicalBriefingQuery |
| Snapshot publication reference | HCM-SV-ward-round-clinical-briefing-query |
| ArchiMate semantic ID | id-ea64cd1258995a3ebae6b21e311943fe |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Ward Round Clinical Briefing Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-INPATIENT-CARE.SV-WARD-ROUND-CLINICAL-BRIEFING-QUERY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-INPATIENT-CARE.SV-WARD-ROUND-CLINICAL-BRIEFING-QUERY"] |

Responsibility/framing context: [Multidisciplinary Inpatient Care Coordination](business-enabling-capabilities.md#hcm-bec-inpatient-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:ward-round-clinical-briefing-query`) and the element's version-qualified Properties.

<a id="hcm-sv-operational-work-status-query"></a>

## Operational Work Status Query

Discloses task progression to requesting wards.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:operational-work-status-query |
| canonicalId | 8659408923e641b99727ef218e2b8dfc |
| displayName | Operational Work Status Query |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch.OperationalWorkAssignmentAndDispatchCoordination.WorkProgressOversightAndEscalation.OperationalWorkStatusQuery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch.CT3-BEC-OperationalWorkAssignmentAndDispatchCoordination.FEAT-WorkProgressOversightAndEscalation.SV-OperationalWorkStatusQuery |
| Snapshot publication reference | HCM-SV-operational-work-status-query |
| ArchiMate semantic ID | id-7f937c0dfc8f5705bbd78f302e5284bf |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operational Work Status Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.SV-OPERATIONAL-WORK-STATUS-QUERY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-WORK-ALLOCATION-DISPATCH.SV-OPERATIONAL-WORK-STATUS-QUERY"] |

Responsibility/framing context: [Work Allocation & Dispatch](business-enabling-capabilities.md#hcm-bec-work-allocation-dispatch). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:operational-work-status-query`) and the element's version-qualified Properties.

<a id="hcm-sv-work-dispatch-service"></a>

## Work Dispatch Service

Delivers allocated work and captures distinct technical and operational acknowledgements; acknowledgement alone does not establish completion.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:work-dispatch-service |
| canonicalId | 7410926f15d3480eaa856d998d67d83f |
| displayName | Work Dispatch Service |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch.WorkDispatchService |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch.SV-WorkDispatchService |
| Snapshot publication reference | HCM-SV-work-dispatch-service |
| ArchiMate semantic ID | id-6bf9921ea3a15f76864aef3c2e8ce5df |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Work Dispatch Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.SV-WORK-DISPATCH-SERVICE |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-WORK-ALLOCATION-DISPATCH.SV-WORK-DISPATCH-SERVICE"] |

Responsibility/framing context: [Work Allocation & Dispatch](business-enabling-capabilities.md#hcm-bec-work-allocation-dispatch). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:work-dispatch-service`) and the element's version-qualified Properties.

<a id="hcm-sv-temporal-context-resolution"></a>

## Temporal Context Resolution

Resolves operational shifts.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:temporal-context-resolution |
| canonicalId | 47f6491361794c2393cd3b64caec2d1a |
| displayName | Temporal Context Resolution |
| friendlyName | SharedInformationAndCoordinationEnablement.CalendarManagement.OperationalAndClinicalCalendarCoordination.TemporalEventCorrelation.TemporalContextResolution |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-CalendarManagement.CT3-BEC-OperationalAndClinicalCalendarCoordination.FEAT-TemporalEventCorrelation.SV-TemporalContextResolution |
| Snapshot publication reference | HCM-SV-temporal-context-resolution |
| ArchiMate semantic ID | id-af526ad0c091540c8ee3b7afe9bf95d8 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Temporal Context Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CALENDAR-MANAGEMENT.SV-TEMPORAL-CONTEXT-RESOLUTION |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-CALENDAR-MANAGEMENT.SV-TEMPORAL-CONTEXT-RESOLUTION"] |

Responsibility/framing context: [Calendar Management](business-enabling-capabilities.md#hcm-bec-calendar-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:temporal-context-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-unified-calendar-projection-query"></a>

## Unified Calendar Projection Query

Discloses consolidated temporal schedules.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:unified-calendar-projection-query |
| canonicalId | c78bdb7ffc7d42fda450c024ca252322 |
| displayName | Unified Calendar Projection Query |
| friendlyName | SharedInformationAndCoordinationEnablement.CalendarManagement.OperationalAndClinicalCalendarCoordination.ScheduleProjectionIntegration.UnifiedCalendarProjectionQuery |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-CalendarManagement.CT3-BEC-OperationalAndClinicalCalendarCoordination.FEAT-ScheduleProjectionIntegration.SV-UnifiedCalendarProjectionQuery |
| Snapshot publication reference | HCM-SV-unified-calendar-projection-query |
| ArchiMate semantic ID | id-54794fdfa3ba5b09aae8eeffe08110d5 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Unified Calendar Projection Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CALENDAR-MANAGEMENT.SV-UNIFIED-CALENDAR-PROJECTION-QUERY |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-CALENDAR-MANAGEMENT.SV-UNIFIED-CALENDAR-PROJECTION-QUERY"] |

Responsibility/framing context: [Calendar Management](business-enabling-capabilities.md#hcm-bec-calendar-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:unified-calendar-projection-query`) and the element's version-qualified Properties.

<a id="hcm-sv-clinical-collaboration-space-service"></a>

## Clinical Collaboration Space Service

Manages team collaboration spaces and discussions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:clinical-collaboration-space-service |
| canonicalId | 17579c34d3014825a878a9ff121e30a2 |
| displayName | Clinical Collaboration Space Service |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.ClinicalContextCollaboration.CareTeamClinicalCollaborationCoordination.ClinicalCollaborationSpaceService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.CT3-BEC-ClinicalContextCollaboration.FEAT-CareTeamClinicalCollaborationCoordination.SV-ClinicalCollaborationSpaceService |
| Snapshot publication reference | HCM-SV-clinical-collaboration-space-service |
| ArchiMate semantic ID | id-18a2039b1b8f5d578679c60daaa0ee71 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Collaboration Space Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.SV-CLINICAL-COLLABORATION-SPACE-SERVICE |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION.SV-CLINICAL-COLLABORATION-SPACE-SERVICE"] |

Responsibility/framing context: [Clinical Collaboration](business-enabling-capabilities.md#hcm-bec-clinical-collaboration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:clinical-collaboration-space-service`) and the element's version-qualified Properties.

<a id="hcm-sv-collaboration-clinical-summary-resolution"></a>

## Collaboration Clinical Summary Resolution

Embeds clinical summaries in collaboration feeds.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:collaboration-clinical-summary-resolution |
| canonicalId | 6a25de08f1b5461b86bd410ea21b80a6 |
| displayName | Collaboration Clinical Summary Resolution |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.ClinicalContextCollaboration.InConversationLhrQueryResolution.CollaborationClinicalSummaryResolution |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.CT3-BEC-ClinicalContextCollaboration.FEAT-InConversationLhrQueryResolution.SV-CollaborationClinicalSummaryResolution |
| Snapshot publication reference | HCM-SV-collaboration-clinical-summary-resolution |
| ArchiMate semantic ID | id-8a77a391a9b95912b0a494a96d90f67a |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Collaboration Clinical Summary Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.SV-COLLABORATION-CLINICAL-SUMMARY-RESOLUTION |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION.SV-COLLABORATION-CLINICAL-SUMMARY-RESOLUTION"] |

Responsibility/framing context: [Clinical Collaboration](business-enabling-capabilities.md#hcm-bec-clinical-collaboration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:collaboration-clinical-summary-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-collaboration-space-governance-service"></a>

## Collaboration Space Governance Service

Governs space lifecycles and membership.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:collaboration-space-governance-service |
| canonicalId | f94eef008b214ed88ff92bc64640ccb0 |
| displayName | Collaboration Space Governance Service |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.ClinicalContextCollaboration.ZeroPhiCollaborationMetadataGovernance.CollaborationSpaceGovernanceService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.CT3-BEC-ClinicalContextCollaboration.FEAT-ZeroPhiCollaborationMetadataGovernance.SV-CollaborationSpaceGovernanceService |
| Snapshot publication reference | HCM-SV-collaboration-space-governance-service |
| ArchiMate semantic ID | id-66a6f7298e5e5a7eb602b505517bca41 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Collaboration Space Governance Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.SV-COLLABORATION-SPACE-GOVERNANCE-SERVICE |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION.SV-COLLABORATION-SPACE-GOVERNANCE-SERVICE"] |

Responsibility/framing context: [Clinical Collaboration](business-enabling-capabilities.md#hcm-bec-clinical-collaboration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:collaboration-space-governance-service`) and the element's version-qualified Properties.

<a id="hcm-sv-terminology-concept-resolution-service"></a>

## Terminology Concept Resolution Service

Exposes concept validation lookups.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:terminology-concept-resolution-service |
| canonicalId | 3be22abcc49045c7a609bdf6428fe15a |
| displayName | Terminology Concept Resolution Service |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.ClinicalKnowledgeRequestAdministration.ClinicalConceptResolution.TerminologyConceptResolutionService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.CT3-BEC-ClinicalKnowledgeRequestAdministration.FEAT-ClinicalConceptResolution.SV-TerminologyConceptResolutionService |
| Snapshot publication reference | HCM-SV-terminology-concept-resolution-service |
| ArchiMate semantic ID | id-2391b2f33a8456ffb135b331e7e333e4 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Terminology Concept Resolution Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.SV-TERMINOLOGY-CONCEPT-RESOLUTION-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-CLINICAL-KNOWLEDGE-SERVICES.SV-TERMINOLOGY-CONCEPT-RESOLUTION-SERVICE"] |

Responsibility/framing context: [Clinical Knowledge Services](business-enabling-capabilities.md#hcm-bec-clinical-knowledge-services). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:terminology-concept-resolution-service`) and the element's version-qualified Properties.

<a id="hcm-sv-terminology-mapping-service"></a>

## Terminology Mapping Service

Translates codes across terminology systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:terminology-mapping-service |
| canonicalId | 27dfb51817344e17ab78aa6f33f959ba |
| displayName | Terminology Mapping Service |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.ClinicalKnowledgeRequestAdministration.BidirectionalTerminologyMapping.TerminologyMappingService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.CT3-BEC-ClinicalKnowledgeRequestAdministration.FEAT-BidirectionalTerminologyMapping.SV-TerminologyMappingService |
| Snapshot publication reference | HCM-SV-terminology-mapping-service |
| ArchiMate semantic ID | id-eda9bcdfca33599cbfb01af28065bf7d |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Terminology Mapping Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.SV-TERMINOLOGY-MAPPING-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-CLINICAL-KNOWLEDGE-SERVICES.SV-TERMINOLOGY-MAPPING-SERVICE"] |

Responsibility/framing context: [Clinical Knowledge Services](business-enabling-capabilities.md#hcm-bec-clinical-knowledge-services). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:terminology-mapping-service`) and the element's version-qualified Properties.

<a id="hcm-sv-governed-reference-dataset-content-query"></a>

## Governed Reference Dataset Content Query

Provide authorised consumers with approved content at an identified revision and applicability basis.

**Overview**

Consumers can resolve the membership or mapping content they actually used without treating build-time model definitions as runtime data.

**Boundary**

Does not grant unrestricted dataset mutation or external terminology authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:service-governed-reference-dataset-content-query |
| canonicalId | 625ef1036a7442b2b8aed06f72b8afff |
| displayName | Governed Reference Dataset Content Query |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.GovernedReferenceDatasetAdministration.GovernedReferenceDatasetContentQuery |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.CT3-BEC-GovernedReferenceDatasetAdministration.SV-GovernedReferenceDatasetContentQuery |
| Snapshot publication reference | HCM-SV-governed-reference-dataset-content-query |
| ArchiMate semantic ID | id-8b2a95285ad65f61bae6d379425bf9e4 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.SV-GOVERNED-REFERENCE-DATASET-CONTENT-QUERY |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.SV-GOVERNED-REFERENCE-DATASET-CONTENT-QUERY"] |

Responsibility/framing context: [Governed Reference Dataset Administration](business-enabling-capabilities.md#hcm-bec-governed-reference-dataset-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:service-governed-reference-dataset-content-query`) and the element's version-qualified Properties.

<a id="hcm-sv-operational-collaboration-service"></a>

## Operational Collaboration Service

Provides non-clinical operational messaging channels.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:operational-collaboration-service |
| canonicalId | 4096602ad3084bc2a9ad9a06276cbcdc |
| displayName | Operational Collaboration Service |
| friendlyName | SharedInformationAndCoordinationEnablement.GeneralCollaboration.OperationalCollaboration.OperationalTeamChatEnablement.OperationalCollaborationService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-GeneralCollaboration.CT3-BEC-OperationalCollaboration.FEAT-OperationalTeamChatEnablement.SV-OperationalCollaborationService |
| Snapshot publication reference | HCM-SV-operational-collaboration-service |
| ArchiMate semantic ID | id-ad83c410bf525d789704618d1ac8524b |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operational Collaboration Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-GENERAL-COLLABORATION.SV-OPERATIONAL-COLLABORATION-SERVICE |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-GENERAL-COLLABORATION.SV-OPERATIONAL-COLLABORATION-SERVICE"] |

Responsibility/framing context: [General Collaboration](business-enabling-capabilities.md#hcm-bec-general-collaboration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:operational-collaboration-service`) and the element's version-qualified Properties.

<a id="hcm-sv-clinical-information-search-service"></a>

## Clinical Information Search Service

Discloses search capabilities to clinical user interfaces.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:clinical-information-search-service |
| canonicalId | ef87ad2b419443a9989849bf18dfb385 |
| displayName | Clinical Information Search Service |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationAccess.GovernedHealthInformationRetrieval.FederatedClinicalQuery.ClinicalInformationSearchService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationAccess.CT3-BEC-GovernedHealthInformationRetrieval.FEAT-FederatedClinicalQuery.SV-ClinicalInformationSearchService |
| Snapshot publication reference | HCM-SV-clinical-information-search-service |
| ArchiMate semantic ID | id-0a0509d6bd365c709c58419485df29b4 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Information Search Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-ACCESS.SV-CLINICAL-INFORMATION-SEARCH-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-ACCESS.SV-CLINICAL-INFORMATION-SEARCH-SERVICE"] |

Responsibility/framing context: [Health Information Access](business-enabling-capabilities.md#hcm-bec-health-information-access). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:clinical-information-search-service`) and the element's version-qualified Properties.

<a id="hcm-sv-clinical-resource-retrieval-service"></a>

## Clinical Resource Retrieval Service

Delivers structured clinical information to requesting applications.

Feature association not established: FEAT-ISE-09 — Access-Controlled Information Filtering requires access-controlled disclosure/filtering; retrieval of discrete information alone does not materially realise that responsibility. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:clinical-resource-retrieval-service |
| canonicalId | 3cdde3bb6a204c46b08468e080f2545a |
| displayName | Clinical Resource Retrieval Service |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationAccess.ClinicalResourceRetrievalService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationAccess.SV-ClinicalResourceRetrievalService |
| Snapshot publication reference | HCM-SV-clinical-resource-retrieval-service |
| ArchiMate semantic ID | id-86038993ea475d81b675875e1a52ba56 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Resource Retrieval Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-ACCESS.SV-CLINICAL-RESOURCE-RETRIEVAL-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-ACCESS.SV-CLINICAL-RESOURCE-RETRIEVAL-SERVICE"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Health Information Access](business-enabling-capabilities.md#hcm-bec-health-information-access). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:clinical-resource-retrieval-service`) and the element's version-qualified Properties.

<a id="hcm-sv-consent-enforcement-service"></a>

## Consent Enforcement Service

Returns masking and redaction instructions.

Feature association not established: FEAT-ISE-12 — Security Context Propagation propagates attributable security context; evaluating consent constraints does not materially realise that responsibility. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:consent-enforcement-service |
| canonicalId | 67bf392d2dd6464292619e259cc37f8e |
| displayName | Consent Enforcement Service |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.ConsentEnforcementService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.SV-ConsentEnforcementService |
| Snapshot publication reference | HCM-SV-consent-enforcement-service |
| ArchiMate semantic ID | id-c4d53a607101520e93a971e5eb5e64ad |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Consent Enforcement Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.SV-CONSENT-ENFORCEMENT-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.SV-CONSENT-ENFORCEMENT-SERVICE"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:consent-enforcement-service`) and the element's version-qualified Properties.

<a id="hcm-sv-policy-evaluation-authorisation-service"></a>

## Policy Evaluation & Authorisation Service

Evaluates access requests across the platform.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:policy-evaluation--authorisation-service |
| canonicalId | a025ac002b6a461abaac07fcb87f411f |
| displayName | Policy Evaluation & Authorisation Service |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.PatientHealthInformationRestrictionManagement.HealthInformationAccessAndConsentControl.PolicyEvaluationAndAuthorisationService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.CT3-BEC-PatientHealthInformationRestrictionManagement.FEAT-HealthInformationAccessAndConsentControl.SV-PolicyEvaluationAndAuthorisationService |
| Snapshot publication reference | HCM-SV-policy-evaluation-authorisation-service |
| ArchiMate semantic ID | id-0b6d1d05c1b5502b96828b0500ee415e |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Policy Evaluation & Authorisation Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.SV-POLICY-EVALUATION-AUTHORISATION-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.SV-POLICY-EVALUATION-AUTHORISATION-SERVICE"] |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:policy-evaluation--authorisation-service`) and the element's version-qualified Properties.

<a id="hcm-sv-security-audit-ingress"></a>

## Security Audit Ingress

Ingests audit events across all capabilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:security-audit-ingress |
| canonicalId | 43c4a2aa7b4548ec8fdc2844e3d666b6 |
| displayName | Security Audit Ingress |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.SecurityAuditIngress |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.SV-SecurityAuditIngress |
| Snapshot publication reference | HCM-SV-security-audit-ingress |
| ArchiMate semantic ID | id-7d4e1b089b2b5d8781a1456f9dcca279 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Security Audit Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.SV-SECURITY-AUDIT-INGRESS |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.SV-SECURITY-AUDIT-INGRESS"] |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:security-audit-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-security-context-validation-service"></a>

## Security Context Validation Service

Verifies transaction security credentials.

Exposure qualification: Validation is the declared Service behaviour. Its juxtaposition with Propagate Security Context does not establish a one-to-one Function/Service relationship; the precise internal mapping remains unestablished.

This Service lets a client obtain validation of transaction security credentials. The exposure is established even though its precise internal Function mapping is not. Validation must not be rewritten as context propagation merely to manufacture a symmetrical Function-to-Service pair.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:security-context-validation-service |
| canonicalId | f1ff9e6542004a7584a385062cb0ed21 |
| displayName | Security Context Validation Service |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.SecurityContextValidationService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.SV-SecurityContextValidationService |
| Snapshot publication reference | HCM-SV-security-context-validation-service |
| ArchiMate semantic ID | id-7ec30e1d7cf4575283579d0a1ccf0523 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Security Context Validation Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.SV-SECURITY-CONTEXT-VALIDATION-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.SV-SECURITY-CONTEXT-VALIDATION-SERVICE"] |
| Unresolved dimensions | ["feature_association","function_mapping"] |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:security-context-validation-service`) and the element's version-qualified Properties.

<a id="hcm-sv-addressed-document-delivery-service"></a>

## Addressed Document Delivery Service

Securely delivers documents to designated recipient mailboxes.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:addressed-document-delivery-service |
| canonicalId | 97080460bacf40ef8b6db2091cc3bb6e |
| displayName | Addressed Document Delivery Service |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieImplementationManagement.AddressedClinicalDistribution.AddressedDocumentDeliveryService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieImplementationManagement.FEAT-AddressedClinicalDistribution.SV-AddressedDocumentDeliveryService |
| Snapshot publication reference | HCM-SV-addressed-document-delivery-service |
| ArchiMate semantic ID | id-05a3a9a9feb85494a9cc9834bb3e2008 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Addressed Document Delivery Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.SV-ADDRESSED-DOCUMENT-DELIVERY-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.SV-ADDRESSED-DOCUMENT-DELIVERY-SERVICE"] |

Responsibility/framing context: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:addressed-document-delivery-service`) and the element's version-qualified Properties.

<a id="hcm-sv-clinical-event-syndication-stream"></a>

## Clinical Event Syndication Stream

Emits publish-subscribe clinical event feeds.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:clinical-event-syndication-stream |
| canonicalId | 337f39bb62d9491bb6e41c4af32b220c |
| displayName | Clinical Event Syndication Stream |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieImplementationManagement.EventDrivenSyndication.ClinicalEventSyndicationStream |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieImplementationManagement.FEAT-EventDrivenSyndication.SV-ClinicalEventSyndicationStream |
| Snapshot publication reference | HCM-SV-clinical-event-syndication-stream |
| ArchiMate semantic ID | id-95e2411e0cd55f9388d1d3b6be0883d6 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Event Syndication Stream |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.SV-CLINICAL-EVENT-SYNDICATION-STREAM |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.SV-CLINICAL-EVENT-SYNDICATION-STREAM"] |

Responsibility/framing context: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:clinical-event-syndication-stream`) and the element's version-qualified Properties.

<a id="hcm-sv-federated-health-information-query"></a>

## Federated Health Information Query

Resolves distributed record queries.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:federated-health-information-query |
| canonicalId | b4db5850f7114c968901966b4f6e36f5 |
| displayName | Federated Health Information Query |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieImplementationManagement.ClinicalRecordRetrievalMediation.FederatedHealthInformationQuery |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieImplementationManagement.FEAT-ClinicalRecordRetrievalMediation.SV-FederatedHealthInformationQuery |
| Snapshot publication reference | HCM-SV-federated-health-information-query |
| ArchiMate semantic ID | id-c285d5712fe255ccaaaeba2cdae324ae |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Federated Health Information Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.SV-FEDERATED-HEALTH-INFORMATION-QUERY |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.SV-FEDERATED-HEALTH-INFORMATION-QUERY"] |

Responsibility/framing context: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:federated-health-information-query`) and the element's version-qualified Properties.

<a id="hcm-sv-hie-submission-gateway-service"></a>

## HIE Submission Gateway Service

Ingress endpoint for regional data contributions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:hie-submission-gateway-service |
| canonicalId | 816c1013b609440fb772060396fd2dac |
| displayName | HIE Submission Gateway Service |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.HieImplementationManagement.DirectSubmissionIngestionAndDelivery.HieSubmissionGatewayService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.CT3-BEC-HieImplementationManagement.FEAT-DirectSubmissionIngestionAndDelivery.SV-HieSubmissionGatewayService |
| Snapshot publication reference | HCM-SV-hie-submission-gateway-service |
| ArchiMate semantic ID | id-5d57cd297e41543b8a7af2c4c6489e12 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | HIE Submission Gateway Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.SV-HIE-SUBMISSION-GATEWAY-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.SV-HIE-SUBMISSION-GATEWAY-SERVICE"] |

Responsibility/framing context: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:hie-submission-gateway-service`) and the element's version-qualified Properties.

<a id="hcm-sv-exchange-profile-specification-query"></a>

## Exchange Profile Specification Query

Discloses interoperability schemas.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:exchange-profile-specification-query |
| canonicalId | 98bc4e8f57f5493ebeb88f8bfc8104a8 |
| displayName | Exchange Profile Specification Query |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.ExchangeProfileSpecificationQuery |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.SV-ExchangeProfileSpecificationQuery |
| Snapshot publication reference | HCM-SV-exchange-profile-specification-query |
| ArchiMate semantic ID | id-3f95bae1b24b59deb585bef821ecb342 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Exchange Profile Specification Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.SV-EXCHANGE-PROFILE-SPECIFICATION-QUERY |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.SV-EXCHANGE-PROFILE-SPECIFICATION-QUERY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:exchange-profile-specification-query`) and the element's version-qualified Properties.

<a id="hcm-sv-information-dictionary-query"></a>

## Information Dictionary Query

Provides data dictionary specifications.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:information-dictionary-query |
| canonicalId | ea491740e6cd40c69f65ad7d9d4f81fe |
| displayName | Information Dictionary Query |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.InformationDictionaryQuery |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.SV-InformationDictionaryQuery |
| Snapshot publication reference | HCM-SV-information-dictionary-query |
| ArchiMate semantic ID | id-0bbe77717dab5aa1a7f19f191c7dfd89 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Information Dictionary Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.SV-INFORMATION-DICTIONARY-QUERY |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.SV-INFORMATION-DICTIONARY-QUERY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:information-dictionary-query`) and the element's version-qualified Properties.

<a id="hcm-sv-semantic-conformance-verification-service"></a>

## Semantic Conformance Verification Service

Validates representations for compliance.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:semantic-conformance-verification-service |
| canonicalId | 3d492f97eb3f4d50a1c448477c61bf2b |
| displayName | Semantic Conformance Verification Service |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseCanonicalReusableModelUtilisationSupport.SemanticConformanceVerification.SemanticConformanceVerificationService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseCanonicalReusableModelUtilisationSupport.FEAT-SemanticConformanceVerification.SV-SemanticConformanceVerificationService |
| Snapshot publication reference | HCM-SV-semantic-conformance-verification-service |
| ArchiMate semantic ID | id-fe6d5a803b2c5bae86d1deaffa2e6bde |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Semantic Conformance Verification Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.SV-SEMANTIC-CONFORMANCE-VERIFICATION-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.SV-SEMANTIC-CONFORMANCE-VERIFICATION-SERVICE"] |

Responsibility/framing context: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:semantic-conformance-verification-service`) and the element's version-qualified Properties.

<a id="hcm-sv-semantic-model-definition-service"></a>

## Semantic Model Definition Service

Discloses canonical model definitions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:semantic-model-definition-service |
| canonicalId | 0f6b41152f294a87b84970c74ab5a014 |
| displayName | Semantic Model Definition Service |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.EnterpriseCanonicalReusableModelDesign.InformationStandardsAndSemanticGovernance.SemanticModelDefinitionService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.CT3-BEC-EnterpriseCanonicalReusableModelDesign.FEAT-InformationStandardsAndSemanticGovernance.SV-SemanticModelDefinitionService |
| Snapshot publication reference | HCM-SV-semantic-model-definition-service |
| ArchiMate semantic ID | id-77725f994de4501ca9714c56538bb33d |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Semantic Model Definition Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.SV-SEMANTIC-MODEL-DEFINITION-SERVICE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.SV-SEMANTIC-MODEL-DEFINITION-SERVICE"] |

Responsibility/framing context: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:semantic-model-definition-service`) and the element's version-qualified Properties.

<a id="hcm-sv-terminology-binding-specification-lookup"></a>

## Terminology Binding Specification Lookup

Discloses value set bindings.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:terminology-binding-specification-lookup |
| canonicalId | 1b9e7c961a054a6bbdb7876d08a82ef3 |
| displayName | Terminology Binding Specification Lookup |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.TerminologyBindingSpecificationLookup |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.SV-TerminologyBindingSpecificationLookup |
| Snapshot publication reference | HCM-SV-terminology-binding-specification-lookup |
| ArchiMate semantic ID | id-a93be27ecf9853ce80f153411351c669 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Terminology Binding Specification Lookup |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.SV-TERMINOLOGY-BINDING-SPECIFICATION-LOOKUP |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.SV-TERMINOLOGY-BINDING-SPECIFICATION-LOOKUP"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:terminology-binding-specification-lookup`) and the element's version-qualified Properties.

<a id="hcm-sv-active-problem-allergy-summary-query"></a>

## Active Problem & Allergy Summary Query

Exposes immediate active clinical summaries.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:active-problem--allergy-summary-query |
| canonicalId | a382bc84c1d043849b8cd8ebe8a482c1 |
| displayName | Active Problem & Allergy Summary Query |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LongitudinalClinicalRecordAdministration.ActiveClinicalRecordAccess.ActiveProblemAndAllergySummaryQuery |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LongitudinalClinicalRecordAdministration.FEAT-ActiveClinicalRecordAccess.SV-ActiveProblemAndAllergySummaryQuery |
| Snapshot publication reference | HCM-SV-active-problem-allergy-summary-query |
| ArchiMate semantic ID | id-b439a9589d195390b7f114ff47156a8b |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Active Problem & Allergy Summary Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.SV-ACTIVE-PROBLEM-ALLERGY-SUMMARY-QUERY |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD.SV-ACTIVE-PROBLEM-ALLERGY-SUMMARY-QUERY"] |

Responsibility/framing context: [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:active-problem--allergy-summary-query`) and the element's version-qualified Properties.

<a id="hcm-sv-historical-clinical-record-access"></a>

## Historical Clinical Record Access

Provides governed historical record retrieval for authorised clinical and legal review; preservation does not itself establish legal qualification or originating authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:historical-clinical-record-access |
| canonicalId | be4938981b43403282694631b40f0bb2 |
| displayName | Historical Clinical Record Access |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LongitudinalClinicalRecordAdministration.DurableClinicalRecordPreservation.HistoricalClinicalRecordAccess |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LongitudinalClinicalRecordAdministration.FEAT-DurableClinicalRecordPreservation.SV-HistoricalClinicalRecordAccess |
| Snapshot publication reference | HCM-SV-historical-clinical-record-access |
| ArchiMate semantic ID | id-8a3a595aebf057fcb4ef2bfd02d0b50d |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Historical Clinical Record Access |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.SV-HISTORICAL-CLINICAL-RECORD-ACCESS |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD.SV-HISTORICAL-CLINICAL-RECORD-ACCESS"] |

Responsibility/framing context: [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:historical-clinical-record-access`) and the element's version-qualified Properties.

<a id="hcm-sv-longitudinal-clinical-record-query"></a>

## Longitudinal Clinical Record Query

Discloses the comprehensive clinical timeline to authorised clinicians.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:longitudinal-clinical-record-query |
| canonicalId | df10431b93bf47dfae2b8ec7b3e6552b |
| displayName | Longitudinal Clinical Record Query |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LongitudinalClinicalRecordAdministration.LongitudinalHealthRecordLhrAssembly.LongitudinalClinicalRecordQuery |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LongitudinalClinicalRecordAdministration.FEAT-LongitudinalHealthRecordLhrAssembly.SV-LongitudinalClinicalRecordQuery |
| Snapshot publication reference | HCM-SV-longitudinal-clinical-record-query |
| ArchiMate semantic ID | id-3bbfba99153650739023e87ba668f90c |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Longitudinal Clinical Record Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.SV-LONGITUDINAL-CLINICAL-RECORD-QUERY |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD.SV-LONGITUDINAL-CLINICAL-RECORD-QUERY"] |

Responsibility/framing context: [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:longitudinal-clinical-record-query`) and the element's version-qualified Properties.

<a id="hcm-sv-associated-binary-content-retrieval"></a>

## Associated Binary Content Retrieval

Allow information-consuming capabilities to request resolution and retrieval of associated content.

**Overview**

The client contract returns authorised content or explicit incomplete/unavailable standing while retaining custody attribution.

**Boundary**

The client acquires neither physical repository access nor originating clinical authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:service-associated-binary-content-retrieval |
| canonicalId | f0e0c03e407e4534b9e422df103fc35f |
| displayName | Associated Binary Content Retrieval |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LinkedInformationCuration.AssociatedBinaryContentRetrieval |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LinkedInformationCuration.SV-AssociatedBinaryContentRetrieval |
| Snapshot publication reference | HCM-SV-associated-binary-content-retrieval |
| ArchiMate semantic ID | id-4a95ba1792425d52b3efcface99d5162 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.SV-ASSOCIATED-BINARY-CONTENT-RETRIEVAL |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.SV-ASSOCIATED-BINARY-CONTENT-RETRIEVAL"] |

Responsibility/framing context: [Linked Information Curation](business-enabling-capabilities.md#hcm-bec-associated-clinical-binary-content-enablement). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:service-associated-binary-content-retrieval`) and the element's version-qualified Properties.

<a id="hcm-sv-chronic-care-timeline-query"></a>

## Chronic Care Timeline Query

Exposes aggregated multidisciplinary care timelines.

Feature association not established: Strategy FEAT-SD-02 — Primary Care Notification Dispatch remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:chronic-care-timeline-query |
| canonicalId | 88243738a3c941efb369f5dd6da6252b |
| displayName | Chronic Care Timeline Query |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.PrimaryCareCoordination.ChronicCareTimelineQuery |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-PrimaryCareCoordination.SV-ChronicCareTimelineQuery |
| Snapshot publication reference | HCM-SV-chronic-care-timeline-query |
| ArchiMate semantic ID | id-6516283f9adc5425b17742bfd7edb4ef |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Chronic Care Timeline Query |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-PRIMARY-CARE.SV-CHRONIC-CARE-TIMELINE-QUERY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PRIMARY-CARE.SV-CHRONIC-CARE-TIMELINE-QUERY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Primary Care Coordination](business-enabling-capabilities.md#hcm-bec-primary-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:chronic-care-timeline-query`) and the element's version-qualified Properties.

<a id="hcm-sv-primary-care-summary-ingress"></a>

## Primary Care Summary Ingress

Ingests GP event summaries into the HIE.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:primary-care-summary-ingress |
| canonicalId | 7f4ca66b911e48bc8bf859e239f5ba00 |
| displayName | Primary Care Summary Ingress |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.PrimaryCareCoordination.GeneralPracticeSharedRecordIngestion.PrimaryCareSummaryIngress |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-PrimaryCareCoordination.FEAT-GeneralPracticeSharedRecordIngestion.SV-PrimaryCareSummaryIngress |
| Snapshot publication reference | HCM-SV-primary-care-summary-ingress |
| ArchiMate semantic ID | id-c744982f402e5146b024bbae4114f07b |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Primary Care Summary Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-PRIMARY-CARE.SV-PRIMARY-CARE-SUMMARY-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PRIMARY-CARE.SV-PRIMARY-CARE-SUMMARY-INGRESS"] |

Responsibility/framing context: [Primary Care Coordination](business-enabling-capabilities.md#hcm-bec-primary-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:primary-care-summary-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-remote-telemetry-ingress"></a>

## Remote Telemetry Ingress

Ingests remote patient data streams into the LHR.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:remote-telemetry-ingress |
| canonicalId | 89014429b6454ced82c26cbe777fa33a |
| displayName | Remote Telemetry Ingress |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.RemotePatientMonitoring.RemoteTelemetryIngestion.RemoteTelemetryIngress |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-RemotePatientMonitoring.FEAT-RemoteTelemetryIngestion.SV-RemoteTelemetryIngress |
| Snapshot publication reference | HCM-SV-remote-telemetry-ingress |
| ArchiMate semantic ID | id-d3aec82635285358868a1812552a97e1 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Remote Telemetry Ingress |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-REMOTE-VIRTUAL-CARE.SV-REMOTE-TELEMETRY-INGRESS |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-REMOTE-VIRTUAL-CARE.SV-REMOTE-TELEMETRY-INGRESS"] |

Responsibility/framing context: [Remote Patient Monitoring](business-enabling-capabilities.md#hcm-bec-remote-virtual-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:remote-telemetry-ingress`) and the element's version-qualified Properties.

<a id="hcm-sv-virtual-encounter-context-resolution"></a>

## Virtual Encounter Context Resolution

Binds virtual session identifiers to patient encounters.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:virtual-encounter-context-resolution |
| canonicalId | db1dfa6db11342128e82df186877c477 |
| displayName | Virtual Encounter Context Resolution |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.RemotePatientMonitoring.VirtualCareSessionContextBinding.VirtualEncounterContextResolution |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-RemotePatientMonitoring.FEAT-VirtualCareSessionContextBinding.SV-VirtualEncounterContextResolution |
| Snapshot publication reference | HCM-SV-virtual-encounter-context-resolution |
| ArchiMate semantic ID | id-374bb4912c525ac2956131577d7d14d2 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Virtual Encounter Context Resolution |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-REMOTE-VIRTUAL-CARE.SV-VIRTUAL-ENCOUNTER-CONTEXT-RESOLUTION |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-REMOTE-VIRTUAL-CARE.SV-VIRTUAL-ENCOUNTER-CONTEXT-RESOLUTION"] |

Responsibility/framing context: [Remote Patient Monitoring](business-enabling-capabilities.md#hcm-bec-remote-virtual-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:virtual-encounter-context-resolution`) and the element's version-qualified Properties.

<a id="hcm-sv-contextual-presentation-service"></a>

## Contextual Presentation Service

Delivers contextual health and operational information for presentation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:contextual-presentation-service |
| canonicalId | 905d44b44f434fa4974e09dcafac016a |
| displayName | Contextual Presentation Service |
| friendlyName | SharedInformationAndCoordinationEnablement.PresentationServices.ContextualInformationPresentation.LongitudinalClinicalAndOperationalPresentation.ContextualPresentationService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PresentationServices.CT3-BEC-ContextualInformationPresentation.FEAT-LongitudinalClinicalAndOperationalPresentation.SV-ContextualPresentationService |
| Snapshot publication reference | HCM-SV-contextual-presentation-service |
| ArchiMate semantic ID | id-e021def7dcd95db0bda2eb7d9b6e369d |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Contextual Presentation Service |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PRESENTATION-SERVICES.SV-CONTEXTUAL-PRESENTATION-SERVICE |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-PRESENTATION-SERVICES.SV-CONTEXTUAL-PRESENTATION-SERVICE"] |

Responsibility/framing context: [Presentation Services](business-enabling-capabilities.md#hcm-bec-presentation-services). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:contextual-presentation-service`) and the element's version-qualified Properties.

<a id="hcm-sv-service-offering-readiness-query"></a>

## Service Offering Readiness Query

Expose the qualified readiness view of a defined offering and operating context to operational planning consumers.

**Overview**

The response identifies relevant evidence basis and insufficiency rather than an unconditional ready flag.

**Boundary**

A readiness answer grants no resource allocation or clinical go/no-go authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:service-service-offering-readiness-query |
| canonicalId | 3b0bdc7bc18940208511f39e43d5357a |
| displayName | Service Offering Readiness Query |
| friendlyName | HealthServiceOperationsEnablement.CareResourceAndCapacityCoordination.ServiceOfferingReadinessCoordination.ServiceOfferingReadinessQuery |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-CareResourceAndCapacityCoordination.CT3-BEC-ServiceOfferingReadinessCoordination.SV-ServiceOfferingReadinessQuery |
| Snapshot publication reference | HCM-SV-service-offering-readiness-query |
| ArchiMate semantic ID | id-6faff100c7ba555aa7b35ea22d43829b |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-OFFERING-READINESS-COORDINATION.SV-SERVICE-OFFERING-READINESS-QUERY |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: source proposal determines collective readiness; reviewed scope establishes dependency tracking only. Evidence may be reused, but determination authority is not silently transferred. |

Responsibility/framing context: [Service Offering Readiness Coordination](business-enabling-capabilities.md#hcm-bec-service-offering-readiness-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:service-service-offering-readiness-query`) and the element's version-qualified Properties.

<a id="hcm-sv-operational-credential-checkpoint-service"></a>

## Operational Credential Checkpoint Service

Evaluates physical/operational access rights.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:operational-credential-checkpoint-service |
| canonicalId | 7ecc60e34e07491588b106b9075ab09a |
| displayName | Operational Credential Checkpoint Service |
| friendlyName | HealthServiceOperationsEnablement.WorkforceQualificationAndDevelopmentEnablement.CredentialManagement.OperationalCredentialCheckpointService |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkforceQualificationAndDevelopmentEnablement.CT3-BEC-CredentialManagement.SV-OperationalCredentialCheckpointService |
| Snapshot publication reference | HCM-SV-operational-credential-checkpoint-service |
| ArchiMate semantic ID | id-5c94f522c90c54df8c5951669cf51373 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operational Credential Checkpoint Service |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT.CT3-BEC-CREDENTIAL-MANAGEMENT.SV-OPERATIONAL-CREDENTIAL-CHECKPOINT-SERVICE |
| Unresolved dimensions | ["feature_association"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical operational badges/physical-access credentials are distinct from reviewed clinical-service credential definitions. |

Responsibility/framing context: [Credential Management](business-enabling-capabilities.md#hcm-bec-credential-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:operational-credential-checkpoint-service`) and the element's version-qualified Properties.

<a id="hcm-sv-entity-operational-input-submission"></a>

## Entity Operational Input Submission

Allow authorised domain coordinators to submit an entity-relative stimulus or requested operational effect.

**Overview**

Acceptance concerns the stated handoff contract; the caller observes completion/effect through separately qualified progression information.

**Boundary**

Input acceptance does not establish successful completion or authority over source clinical facts.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:service-entity-operational-input-submission |
| canonicalId | 6a27b36b624444d4b09e68f286045d46 |
| displayName | Entity Operational Input Submission |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.EntitySpecificOperationalCoordination.EntityOperationalInputSubmission |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-EntitySpecificOperationalCoordination.SV-EntityOperationalInputSubmission |
| Snapshot publication reference | HCM-SV-entity-operational-input-submission |
| ArchiMate semantic ID | id-cc4944fb648457dcb1b5288da72cda01 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-ENTITY-SPECIFIC-OPERATIONAL-COORDINATION.SV-ENTITY-OPERATIONAL-INPUT-SUBMISSION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Internal entity-relative progression is not the reviewed external work bridge; a current BEC destination has not been adjudicated. |

Responsibility/framing context: [Entity-Specific Operational Coordination](business-enabling-capabilities.md#hcm-bec-entity-specific-operational-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:service-entity-operational-input-submission`) and the element's version-qualified Properties.

<a id="hcm-sv-activity-escalation-telemetry"></a>

## Activity Escalation Telemetry

Discloses SLA breaches.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:activity-escalation-telemetry |
| canonicalId | f6c353c9349d47daadd59e57821ad03b |
| displayName | Activity Escalation Telemetry |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.ActivityEscalationTelemetry |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.SV-ActivityEscalationTelemetry |
| Snapshot publication reference | HCM-SV-activity-escalation-telemetry |
| ArchiMate semantic ID | id-ea70d9701c4151c09d62a0cf40fc5edf |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Activity Escalation Telemetry |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.SV-ACTIVITY-ESCALATION-TELEMETRY |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:activity-escalation-telemetry`) and the element's version-qualified Properties.

<a id="hcm-sv-synthetic-task-execution-service"></a>

## Synthetic Task Execution Service

Coordinates automated tasks.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:synthetic-task-execution-service |
| canonicalId | 57317da133ba4e29bde3997921f92e2e |
| displayName | Synthetic Task Execution Service |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.SyntheticTaskExecutionService |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.SV-SyntheticTaskExecutionService |
| Snapshot publication reference | HCM-SV-synthetic-task-execution-service |
| ArchiMate semantic ID | id-6fa328ef53b75a4bbfc9020cd912901e |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Synthetic Task Execution Service |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.SV-SYNTHETIC-TASK-EXECUTION-SERVICE |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:synthetic-task-execution-service`) and the element's version-qualified Properties.

<a id="hcm-sv-to-do-management-service"></a>

## To Do Management Service

Provides To Do inbox and sign-off coordination without acquiring underlying clinical-work management.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:to-do-management-service |
| canonicalId | 6bc69c5517024c43b1ab701aac5e39b7 |
| displayName | To Do Management Service |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.ToDoManagementService |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.SV-ToDoManagementService |
| Snapshot publication reference | HCM-SV-to-do-management-service |
| ArchiMate semantic ID | id-2aaa8d3224cb58719606976d9dba95f7 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | To Do Management Service |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.SV-TO-DO-MANAGEMENT-SERVICE |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:to-do-management-service`) and the element's version-qualified Properties.

<a id="hcm-sv-work-order-coordination-service"></a>

## Work Order Coordination Service

Manages operational work progression.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:work-order-coordination-service |
| canonicalId | 8a1987ed11d44598b325359287e503f0 |
| displayName | Work Order Coordination Service |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.WorkOrderCoordinationService |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.SV-WorkOrderCoordinationService |
| Snapshot publication reference | HCM-SV-work-order-coordination-service |
| ArchiMate semantic ID | id-e60fd5809f09594f9c91f7ff5faa9fb3 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Work Order Coordination Service |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.SV-WORK-ORDER-COORDINATION-SERVICE |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:work-order-coordination-service`) and the element's version-qualified Properties.

<a id="hcm-sv-communicate-service-assurance-outcome"></a>

## Communicate Service Assurance Outcome

•
Canonical Name: Communicate Service Assurance Outcome.

•
Definition: Communicates applicable established Assurance Findings and Conclusions to authorised recipients according to their responsibilities, authority and information requirements.

•
Approved Potential Recipients: Care Coordinator, Service Coordinator, Regulator, Policy Authority, System Steward.

•
Associated Interaction: Service Assurance Outcome Communication (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/collaborations-interactions/interactions.md#service-assurance-outcome-communication), with Service Guardian and one or more authorised recipient Roles.

•
Process Relationship: Exposes the communication behaviour of Assurance Process Reporting / Communication (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/processes/business-processes.md#assurance-process-reporting-communication), beginning from an already established finding/conclusion. It does not perform assessment or adjudication or alter/reinterpret the established outcome. A documentary report is not required.

•
Recipient Boundary: No mandatory broadcast is established. A particular outcome may be communicated to one, some or all legitimate recipients according to applicability, authority, responsibility and information requirements. Requester, status consumer and outcome recipient need not be the same Role. Communication does not transfer responsibility for recipients' interpretation within their authority, operational/clinical management, remediation, escalation, policy/compliance response, process improvement or other subsequent action to Service Guardian.

---

## History

### Service Overview — predecessor explanation

Outcome communication exposes already established findings to applicable authorised recipients from the approved roles. It preserves the adjudicated meaning and does not require a broadcast to every possible recipient. Receipt does not transfer the recipient's subsequent clinical, governance or operational action to Service Guardian.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:communicate-service-assurance-outcome |
| canonicalId | 371b04d6bbf84b53a3c5667ae6df2809 |
| displayName | Communicate Service Assurance Outcome |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement.GovernedAssurance.CommunicateServiceAssuranceOutcome |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement.CT3-BEC-GovernedAssurance.SV-CommunicateServiceAssuranceOutcome |
| Snapshot publication reference | HCM-SV-communicate-service-assurance-outcome |
| ArchiMate semantic ID | id-05ac1cbd590259caa07b664ca8bbe93d |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Communicate Service Assurance Outcome |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE.SV-COMMUNICATE-SERVICE-ASSURANCE-OUTCOME |
| Unresolved dimensions | ["feature_association","contextual_view","function_mapping"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself. |

Responsibility/framing context: [Governed Assurance](business-enabling-capabilities.md#hcm-bec-governed-assurance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:communicate-service-assurance-outcome`) and the element's version-qualified Properties.

<a id="hcm-sv-request-service-assurance"></a>

## Request Service Assurance

•
Canonical Name: Request Service Assurance.

•
Definition: Enables an authorised consumer to request that Service Assurance be performed for an applicable governed subject in accordance with an established Assurance Definition.

•
Approved Consumers: Care Coordinator, Service Coordinator, Regulator, Policy Authority, System Steward.

•
Associated Interaction: Service Assurance Request (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/collaborations-interactions/interactions.md#service-assurance-request), with Service Guardian and one authorised requesting Role.

•
Process Relationship: Requests Assurance Process Execution (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/processes/business-processes.md#assurance-process-execution) under an applicable established Assurance Definition. It does not require a new Assurance Process Design instance for each request.

•
Independence Boundary: The requesting Role identifies the assurance need within its responsibility and authority. Requesting does not grant authority to define or alter the Assurance Definition, alter Assurance Criteria, direct evidence assessment, determine adjudication or control the resulting Assurance Finding / Conclusion. REQ-FND-005 control independence remains applicable, including where a requester performs or manages the subject. Detailed payloads, contracts, initiation mechanics and states remain unresolved.

---

## History

### Service Overview — predecessor explanation

This Service gives approved requesting roles a governed way to seek assurance within their authority. A request expresses the legitimate assurance need; it does not confer control over criteria, evidence assessment or adjudication. The requester can be involved in the subject while the required independence of the conclusion remains intact.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:request-service-assurance |
| canonicalId | a76c0be369744fcfb5eb91b90c9b59e7 |
| displayName | Request Service Assurance |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement.GovernedAssurance.RequestServiceAssurance |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement.CT3-BEC-GovernedAssurance.SV-RequestServiceAssurance |
| Snapshot publication reference | HCM-SV-request-service-assurance |
| ArchiMate semantic ID | id-db26e0ecb3e959eebf8fb824616215e1 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Request Service Assurance |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE.SV-REQUEST-SERVICE-ASSURANCE |
| Unresolved dimensions | ["feature_association","contextual_view","function_mapping"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself. |

Responsibility/framing context: [Governed Assurance](business-enabling-capabilities.md#hcm-bec-governed-assurance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:request-service-assurance`) and the element's version-qualified Properties.

<a id="hcm-sv-request-service-assurance-status"></a>

## Request Service Assurance Status

•
Canonical Name: Request Service Assurance Status.

•
Definition: Enables the System Steward to obtain the current business progression state of an identifiable Service Assurance activity for stewardship purposes.

•
Only Approved Consumer: System Steward.

•
Associated Interaction: Service Assurance Status (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/collaborations-interactions/interactions.md#service-assurance-status), between System Steward and Service Guardian.

•
Status Boundary: Concerns activity progression state. It does not communicate provisional findings, incomplete assessment results as conclusions, predicted outcomes or unadjudicated conclusions. Other requesting Roles do not gain status access merely because they requested assurance. No detailed activity-status taxonomy is established.

Service Assurance Status answers "where is the assurance activity in its progression?" It does not answer "what has the assurance established?" The latter requires an adjudicated Assurance Finding / Conclusion and subsequent outcome communication; neither status nor execution completion establishes that conclusion.

Management Monitoring evaluates activity state in order to progress or manage activity. Assurance evaluates activity against governing criteria in order to establish an assurance conclusion. System Steward's use of this Service is stewardship / management observation of assurance progression, not itself assurance or recursive assurance. The non-recursion boundary in §7.2 applies.

---

## History

### Service Overview — predecessor explanation

The status Service exposes progression of an identifiable assurance activity exclusively to System Steward. Its client question is where the activity has reached, not what the evidence will establish. Requesting assurance does not automatically grant status access, and progression observation is stewardship rather than recursive assurance.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:request-service-assurance-status |
| canonicalId | 60f8cbc814614f669f31d4e78734c6d6 |
| displayName | Request Service Assurance Status |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement.GovernedAssurance.RequestServiceAssuranceStatus |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement.CT3-BEC-GovernedAssurance.SV-RequestServiceAssuranceStatus |
| Snapshot publication reference | HCM-SV-request-service-assurance-status |
| ArchiMate semantic ID | id-e69514b725475221959dce4506cd68d7 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Request Service Assurance Status |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE.SV-REQUEST-SERVICE-ASSURANCE-STATUS |
| Unresolved dimensions | ["feature_association","contextual_view","function_mapping"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself. |

Responsibility/framing context: [Governed Assurance](business-enabling-capabilities.md#hcm-bec-governed-assurance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:request-service-assurance-status`) and the element's version-qualified Properties.

<a id="hcm-sv-standards-communication-gateway-service"></a>

## Standards Communication Gateway Service

Protocol interface for external client integrations.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | service:standards-communication-gateway-service |
| canonicalId | 80e239faf1bd476e973eb602b3730380 |
| displayName | Standards Communication Gateway Service |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationConnectivityEnablement.HealthInformationCommunication.StandardsCommunicationGatewayService |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationConnectivityEnablement.CT3-BEC-HealthInformationCommunication.SV-StandardsCommunicationGatewayService |
| Snapshot publication reference | HCM-SV-standards-communication-gateway-service |
| ArchiMate semantic ID | id-d550582636e05bfe86193036e9287801 |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Standards Communication Gateway Service |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-COMMUNICATION.SV-STANDARDS-COMMUNICATION-GATEWAY-SERVICE |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical boundary protocol mediation and technical transmission are distinct from reviewed patient-facing communication. |

Responsibility/framing context: [Health Information Communication](business-enabling-capabilities.md#hcm-bec-health-information-communication). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `service:standards-communication-gateway-service`) and the element's version-qualified Properties.

<a id="hcm-sv-mandatory-audit-evidence-capture"></a>

## Mandatory Audit Evidence Capture

Allow governed-operation boundaries to require authoritative capture of significant activity evidence.

**Overview**

The client contract distinguishes confirmed capture from inability to capture, allowing required operations to fail closed.

**Boundary**

The exposed boundary does not permit callers to assign themselves audit platform authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:service-mandatory-audit-evidence-capture |
| canonicalId | 6ab3f8628ba14f17bddbf6092cf56ba7 |
| displayName | Mandatory Audit Evidence Capture |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationProtectionAndAccountabilityEnablement.SignificantActivityAuditCapture.MandatoryAuditEvidenceCapture |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationProtectionAndAccountabilityEnablement.CT3-BEC-SignificantActivityAuditCapture.SV-MandatoryAuditEvidenceCapture |
| Snapshot publication reference | HCM-SV-mandatory-audit-evidence-capture |
| ArchiMate semantic ID | id-94caf7d1a6c85f7a86c64db72c8cc84e |
| Legacy identity evidence | No legacy allocation |
| Construct | service |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-SIGNIFICANT-ACTIVITY-AUDIT-CAPTURE.SV-MANDATORY-AUDIT-EVIDENCE-CAPTURE |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: The reviewed hierarchy does not allocate general mandatory authoritative audit capture; disclosure or provenance alone is not an equivalent responsibility. |

Responsibility/framing context: [Significant Activity Audit Capture](business-enabling-capabilities.md#hcm-bec-significant-activity-audit-capture). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:service-mandatory-audit-evidence-capture`) and the element's version-qualified Properties.

## [Business Service] Identifier Create

Accepted as submitted in CR-006 of [run-001](../../../archimate/processing/ingestion/1.0.8/run-001/change-review.md). No additional definition, responsibility or standing is inferred. Machine-readable source: [accepted ingestion capture](accepted-ingestion-1.0.8.json).

| Field | Value |
| --- | --- |
| representationId | id-fddda503b8f0438e995465cd93204326 |
| archimateType | BusinessService |
| canonicalId | 063b7a6b4ad549428a7566a4ce2c5de5 |
| displayName | [Business Service] Identifier Create |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.IdentifierCapture.IdentifierCreate |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-IdentifierCapture.SV-IdentifierCreate |

## [Business Service] Identifier Update

Accepted as submitted in CR-007 of [run-001](../../../archimate/processing/ingestion/1.0.8/run-001/change-review.md). No additional definition, responsibility or standing is inferred. Machine-readable source: [accepted ingestion capture](accepted-ingestion-1.0.8.json).

| Field | Value |
| --- | --- |
| representationId | id-0088d61ccfcf4962b3aab8bc3b96021d |
| archimateType | BusinessService |
| canonicalId | 281155a0820942b884a13fa08d2e0b04 |
| displayName | [Business Service] Identifier Update |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.IdentifierCapture.IdentifierUpdate |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-IdentifierCapture.SV-IdentifierUpdate |

## [Business Service] Verify Identity Source

Accepted as submitted in CR-009 of [run-001](../../../archimate/processing/ingestion/1.0.8/run-001/change-review.md). No additional definition, responsibility or standing is inferred. Machine-readable source: [accepted ingestion capture](accepted-ingestion-1.0.8.json).

| Field | Value |
| --- | --- |
| representationId | id-9bfedbfd5b404694a2224c039fa5d4c4 |
| archimateType | BusinessService |
| canonicalId | b95f652107f4420b86cc10fb893c9b01 |
| displayName | [Business Service] Verify Identity Source |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.IdentitySourceAndCorrectionLineage.VerifyIdentitySource |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-IdentitySourceAndCorrectionLineage.SV-VerifyIdentitySource |

------- Legacy Content - Superseded ------- Finish ----
