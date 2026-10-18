<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Baseline — Functions

[Baseline authority and reading guide](README.md). Currentness does not imply approval. Definitions are reproduced without semantic rewriting. IDs below retain their distinct roles.

## Catalogue

- [Associate Care Plan Context](#hcm-fn-associate-care-plan-context) — Canonical; current; placement Established
- [Distribute Care Plan Change](#hcm-fn-distribute-care-plan-change) — Canonical; current; placement Established
- [Receive Care Plan](#hcm-fn-receive-care-plan) — Canonical; current; placement Established
- [Bind Community Support Network Context](#hcm-fn-bind-community-support-network-context) — Canonical; current; placement Established
- [Synchronize Community Visit Record](#hcm-fn-synchronize-community-visit-record) — Canonical; current; placement Established
- [Provide Outreach Visit Context](#hcm-fn-provide-outreach-visit-context) — Canonical; current; placement Established
- [Reconcile Outreach Clinical Batch](#hcm-fn-reconcile-outreach-clinical-batch) — Canonical; current; placement Established
- [Distribute Governed Demographic Change](#hcm-fn-distribute-governed-demographic-change) — Canonical; current; placement Established
- [Receive Client Demographic Change](#hcm-fn-receive-client-demographic-change) — Canonical; current; placement Established
- [Project Screening Eligibility Timeline](#hcm-fn-project-screening-eligibility-timeline) — Canonical; current; placement Established
- [Syndicate Immunisation Record](#hcm-fn-syndicate-immunisation-record) — Canonical; current; placement Established
- [Distribute Clinical Communication](#hcm-fn-distribute-clinical-communication) — Canonical; current; placement Established
- [Record Communication Transaction](#hcm-fn-record-communication-transaction) — Canonical; current; placement Established
- [Track Business Delivery Acknowledgement](#hcm-fn-track-business-delivery-acknowledgement) — Canonical; current; placement Established
- [Govern Clinical Document Lifecycle](#hcm-fn-govern-clinical-document-lifecycle) — Canonical; current; placement Established
- [Index Clinical Document Metadata](#hcm-fn-index-clinical-document-metadata) — Canonical; current; placement Established
- [Receive Clinical Document](#hcm-fn-receive-clinical-document) — Canonical; current; placement Established
- [Bind Diagnostic Report](#hcm-fn-bind-diagnostic-report) — Canonical; current; placement Established
- [Correlate Diagnostic Request](#hcm-fn-correlate-diagnostic-request) — Canonical; current; placement Established
- [Distribute Diagnostic Report](#hcm-fn-distribute-diagnostic-report) — Canonical; current; placement Established
- [Publish Governed Diagnostic Report](#hcm-fn-publish-governed-diagnostic-report) — Canonical; current; placement Established
- [Receive Diagnostic Requisition Feed](#hcm-fn-receive-diagnostic-requisition-feed) — Canonical; current; placement Established
- [Establish Encounter Context](#hcm-fn-establish-encounter-context) — Canonical; current; placement Established
- [Maintain Encounter Care-Place Association](#hcm-fn-maintain-encounter-care-place-association) — Canonical; current; placement Established
- [Progress Encounter State](#hcm-fn-progress-encounter-state) — Canonical; current; placement Established
- [Assemble Acute Clinical Baseline](#hcm-fn-assemble-acute-clinical-baseline) — Canonical; current; placement Established
- [Route Acute Critical Result Alert](#hcm-fn-route-acute-critical-result-alert) — Canonical; current; placement Established
- [Receive Medication Administration Event](#hcm-fn-receive-medication-administration-event) — Canonical; current; placement Established
- [Receive Medication Order](#hcm-fn-receive-medication-order) — Canonical; current; placement Established
- [Track Dispense Event](#hcm-fn-track-dispense-event) — Canonical; current; placement Established
- [Correlate Adverse Reaction & Allergy Profile](#hcm-fn-correlate-adverse-reaction-allergy-profile) — Canonical; current; placement Established
- [Project Longitudinal Medication History](#hcm-fn-project-longitudinal-medication-history) — Canonical; current; placement Established
- [Associate Order Outcome](#hcm-fn-associate-order-outcome) — Canonical; current; placement Established
- [Coordinate Order Modification / Cancellation](#hcm-fn-coordinate-order-modification-cancellation) — Canonical; current; placement Established
- [Manage Order Progression](#hcm-fn-manage-order-progression) — Canonical; current; placement Established
- [Receive Order Request](#hcm-fn-receive-order-request) — Canonical; current; placement Established
- [Resolve Order Destination](#hcm-fn-resolve-order-destination) — Canonical; current; placement Established
- [Receive Procedural Documentation](#hcm-fn-receive-procedural-documentation) — Canonical; current; placement Established
- [Receive Procedure Booking / Request](#hcm-fn-receive-procedure-booking-request) — Canonical; current; placement Established
- [Assemble Referral Context](#hcm-fn-assemble-referral-context) — Canonical; current; placement Established
- [Manage Referral Progression](#hcm-fn-manage-referral-progression) — Canonical; current; placement Established
- [Receive Referral](#hcm-fn-receive-referral) — Canonical; current; placement Established
- [Receive Appointment Notification](#hcm-fn-receive-appointment-notification) — Canonical; current; placement Established
- [Synchronise Appointment Status](#hcm-fn-synchronise-appointment-status) — Canonical; current; placement Established
- [Evaluate Client Confidentiality Restrictions](#hcm-fn-evaluate-client-confidentiality-restrictions) — Canonical; current; placement Established
- [Evaluate Client Consent](#hcm-fn-evaluate-client-consent) — Canonical; current; placement Established
- [Govern Client Relationship Validity](#hcm-fn-govern-client-relationship-validity) — Canonical; current; placement Established
- [Maintain Client Relationships](#hcm-fn-maintain-client-relationships) — Canonical; current; placement Established
- [Establish Healthcare Subject Context](#hcm-fn-establish-healthcare-subject-context) — Canonical; current; placement Established
- [Govern Subject Demographic Context](#hcm-fn-govern-subject-demographic-context) — Canonical; current; placement Established
- [Apply Governed Person Identity Correction](#hcm-fn-apply-governed-person-identity-correction) — Canonical; current; placement Established
- [Correlate Person Identifiers](#hcm-fn-correlate-person-identifiers) — Canonical; current; placement Established
- [Maintain Person Identity Aliases](#hcm-fn-maintain-person-identity-aliases) — Canonical; current; placement Established
- [Resolve Person Identifier](#hcm-fn-resolve-person-identifier) — Canonical; current; placement Established
- [Maintain Device Association](#hcm-fn-maintain-device-association) — Canonical; current; placement Established
- [Maintain Device Communication Endpoints](#hcm-fn-maintain-device-communication-endpoints) — Canonical; current; placement Established
- [Maintain Device Definition](#hcm-fn-maintain-device-definition) — Canonical; current; placement Established
- [Maintain Device Instance Identity](#hcm-fn-maintain-device-instance-identity) — Canonical; current; placement Established
- [Track Mobile Clinical Asset Operational State](#hcm-fn-track-mobile-clinical-asset-operational-state) — Canonical; current; placement Proposed
- [Maintain Healthcare Service Definition](#hcm-fn-maintain-healthcare-service-definition) — Canonical; current; placement Established
- [Maintain Service / Location / Provider Map](#hcm-fn-maintain-service-location-provider-map) — Canonical; current; placement Established
- [Maintain Service Availability](#hcm-fn-maintain-service-availability) — Canonical; current; placement Established
- [Maintain Service Eligibility Rules](#hcm-fn-maintain-service-eligibility-rules) — Canonical; current; placement Established
- [Maintain Care-Place Definition](#hcm-fn-maintain-care-place-definition) — Canonical; current; placement Established
- [Maintain Healthcare Location](#hcm-fn-maintain-healthcare-location) — Canonical; current; placement Established
- [Maintain Physical Location Hierarchy](#hcm-fn-maintain-physical-location-hierarchy) — Canonical; current; placement Established
- [Govern Organisation Profile](#hcm-fn-govern-organisation-profile) — Canonical; current; placement Established
- [Maintain Organisation Contacts](#hcm-fn-maintain-organisation-contacts) — Canonical; current; placement Established
- [Maintain Organisational Structure](#hcm-fn-maintain-organisational-structure) — Canonical; current; placement Established
- [Verify Healthcare Organisation](#hcm-fn-verify-healthcare-organisation) — Canonical; current; placement Established
- [Bind Practitioner Electronic Endpoints](#hcm-fn-bind-practitioner-electronic-endpoints) — Canonical; current; placement Established
- [Govern Practitioner Profile](#hcm-fn-govern-practitioner-profile) — Canonical; current; placement Established
- [Maintain Clinical Privileges](#hcm-fn-maintain-clinical-privileges) — Canonical; current; placement Established
- [Maintain Practitioner Affiliations](#hcm-fn-maintain-practitioner-affiliations) — Canonical; current; placement Established
- [Maintain Practitioner Roles](#hcm-fn-maintain-practitioner-roles) — Canonical; current; placement Established
- [Verify Practitioner Registration](#hcm-fn-verify-practitioner-registration) — Canonical; current; placement Established
- [Assemble Purpose-Qualified Research Release](#hcm-fn-assemble-purpose-qualified-research-release) — Proposed; current; placement Proposed
- [Maintain Operational Bed State](#hcm-fn-maintain-operational-bed-state) — Canonical; current; placement Established
- [Manage Bed Turnover Progression](#hcm-fn-manage-bed-turnover-progression) — Canonical; current; placement Established
- [Manage Clinic & Practice Capacity](#hcm-fn-manage-clinic-practice-capacity) — Canonical; current; placement Established
- [Manage Clinic Queue Progression](#hcm-fn-manage-clinic-queue-progression) — Canonical; current; placement Established
- [Track Clinic Operating Session](#hcm-fn-track-clinic-operating-session) — Canonical; current; placement Established
- [Verify Clinical Qualification for Work Dispatch](#hcm-fn-verify-clinical-qualification-for-work-dispatch) — Canonical; current; placement Established
- [Manage Discharge Coordination Progression](#hcm-fn-manage-discharge-coordination-progression) — Canonical; current; placement Established
- [Coordinate ED Departmental Capacity](#hcm-fn-coordinate-ed-departmental-capacity) — Canonical; current; placement Established
- [Bind ED Triage Assessment](#hcm-fn-bind-ed-triage-assessment) — Canonical; current; placement Established
- [Provide Emergency LHR Summary](#hcm-fn-provide-emergency-lhr-summary) — Canonical; current; placement Established
- [Integrate Mobile Work Management](#hcm-fn-integrate-mobile-work-management) — Canonical; current; placement Established
- [Manage Mobile Service Capacity](#hcm-fn-manage-mobile-service-capacity) — Canonical; current; placement Established
- [Receive Mobile Visit Progression](#hcm-fn-receive-mobile-visit-progression) — Canonical; current; placement Established
- [Track Operational Staff Presence](#hcm-fn-track-operational-staff-presence) — Canonical; current; placement Established
- [Resolve Active On-Call Coverage](#hcm-fn-resolve-active-on-call-coverage) — Canonical; current; placement Established
- [Capture Post-Clinic Follow-up Requirements](#hcm-fn-capture-post-clinic-follow-up-requirements) — Canonical; current; placement Established
- [Manage Outpatient Capacity](#hcm-fn-manage-outpatient-capacity) — Canonical; current; placement Established
- [Manage Patient Transport Progression](#hcm-fn-manage-patient-transport-progression) — Canonical; current; placement Established
- [Aggregate Service Capacity Telemetry](#hcm-fn-aggregate-service-capacity-telemetry) — Canonical; current; placement Established
- [Manage Theatre Capacity](#hcm-fn-manage-theatre-capacity) — Canonical; current; placement Established
- [Manage Theatre Case Progression](#hcm-fn-manage-theatre-case-progression) — Canonical; current; placement Established
- [Assemble Ward Handover Context](#hcm-fn-assemble-ward-handover-context) — Canonical; current; placement Established
- [Manage Ward Capacity](#hcm-fn-manage-ward-capacity) — Canonical; current; placement Established
- [Assemble Inpatient Discharge Dossier](#hcm-fn-assemble-inpatient-discharge-dossier) — Canonical; current; placement Established
- [Consolidate Inpatient Clinical Round Feed](#hcm-fn-consolidate-inpatient-clinical-round-feed) — Canonical; current; placement Established
- [Dispatch Operational Work Order](#hcm-fn-dispatch-operational-work-order) — Canonical; current; placement Established
- [Manage Operational Work Progression](#hcm-fn-manage-operational-work-progression) — Canonical; current; placement Established
- [Manage Assurance Criteria](#hcm-fn-manage-assurance-criteria) — Canonical; current; placement Established
- [Design Service Assurance](#hcm-fn-design-service-assurance) — Canonical; current; placement Established
- [Correlate Activity with Temporal Context](#hcm-fn-correlate-activity-with-temporal-context) — Canonical; current; placement Established
- [Project Operational Schedule](#hcm-fn-project-operational-schedule) — Canonical; current; placement Established
- [Coordinate Care-Team Collaboration](#hcm-fn-coordinate-care-team-collaboration) — Canonical; current; placement Established
- [Govern Collaboration Metadata](#hcm-fn-govern-collaboration-metadata) — Canonical; current; placement Established
- [Resolve LHR Query within Collaboration](#hcm-fn-resolve-lhr-query-within-collaboration) — Canonical; current; placement Established
- [Map Clinical Concept](#hcm-fn-map-clinical-concept) — Canonical; current; placement Established
- [Resolve Clinical Concept](#hcm-fn-resolve-clinical-concept) — Canonical; current; placement Established
- [Accept Governed Dataset Revision](#hcm-fn-accept-governed-dataset-revision) — Proposed; current; placement Proposed
- [Coordinate Operational Collaboration](#hcm-fn-coordinate-operational-collaboration) — Canonical; current; placement Established
- [Apply Access-Qualified Result Filtering](#hcm-fn-apply-access-qualified-result-filtering) — Canonical; current; placement Established
- [Retrieve Health Information](#hcm-fn-retrieve-health-information) — Canonical; current; placement Established
- [Search Health Information](#hcm-fn-search-health-information) — Canonical; current; placement Established
- [Evaluate Consent Constraint](#hcm-fn-evaluate-consent-constraint) — Canonical; current; placement Established
- [Evaluate Information Access Authority](#hcm-fn-evaluate-information-access-authority) — Canonical; current; placement Established
- [Propagate Security Context](#hcm-fn-propagate-security-context) — Canonical; current; placement Established
- [Record Security-Significant Activity](#hcm-fn-record-security-significant-activity) — Canonical; current; placement Established
- [Distribute Addressed Information](#hcm-fn-distribute-addressed-information) — Canonical; current; placement Established
- [Mediate Information Retrieval](#hcm-fn-mediate-information-retrieval) — Canonical; current; placement Established
- [Receive Information Submission](#hcm-fn-receive-information-submission) — Canonical; current; placement Established
- [Syndicate Information Change](#hcm-fn-syndicate-information-change) — Canonical; current; placement Established
- [Record Significant Information Lineage](#hcm-fn-record-significant-information-lineage) — Proposed; current; placement Proposed
- [Govern Exchange Profile](#hcm-fn-govern-exchange-profile) — Canonical; current; placement Established
- [Govern Information Definition](#hcm-fn-govern-information-definition) — Canonical; current; placement Established
- [Govern Information Semantics](#hcm-fn-govern-information-semantics) — Canonical; current; placement Established
- [Govern Terminology Binding](#hcm-fn-govern-terminology-binding) — Canonical; current; placement Established
- [Verify Semantic Conformance](#hcm-fn-verify-semantic-conformance) — Canonical; current; placement Established
- [Assemble Longitudinal Clinical Record](#hcm-fn-assemble-longitudinal-clinical-record) — Canonical; current; placement Established
- [Maintain Active Clinical Record](#hcm-fn-maintain-active-clinical-record) — Canonical; current; placement Established
- [Preserve Durable Clinical Record](#hcm-fn-preserve-durable-clinical-record) — Canonical; current; placement Established
- [Assemble Associated Binary Content](#hcm-fn-assemble-associated-binary-content) — Proposed; current; placement Proposed
- [Resolve Associated Binary Content](#hcm-fn-resolve-associated-binary-content) — Proposed; current; placement Proposed
- [Coordinate Multi-Agency Chronic Care Plan](#hcm-fn-coordinate-multi-agency-chronic-care-plan) — Canonical; current; placement Established
- [Correlate Primary Care Consultation Context](#hcm-fn-correlate-primary-care-consultation-context) — Canonical; current; placement Established
- [Correlate Virtual Care Consultation Context](#hcm-fn-correlate-virtual-care-consultation-context) — Canonical; current; placement Established
- [Ingest Remote Patient Telemetry](#hcm-fn-ingest-remote-patient-telemetry) — Canonical; current; placement Established
- [Present Contextual Health and Operational Information](#hcm-fn-present-contextual-health-and-operational-information) — Canonical; current; placement Established
- [Assess Service Offering Readiness](#hcm-fn-assess-service-offering-readiness) — Proposed; pending_adjudication; placement Proposed
- [Manage Specimen Transport Progression](#hcm-fn-manage-specimen-transport-progression) — Canonical; pending_adjudication; placement Established
- [Verify Operational Access Credential](#hcm-fn-verify-operational-access-credential) — Canonical; pending_adjudication; placement Established
- [Coordinate Entity-Relative Input](#hcm-fn-coordinate-entity-relative-input) — Proposed; pending_adjudication; placement Proposed
- [Maintain Operational Fulfilment Definition](#hcm-fn-maintain-operational-fulfilment-definition) — Proposed; pending_adjudication; placement Proposed
- [Coordinate Synthetic Task](#hcm-fn-coordinate-synthetic-task) — Canonical; pending_adjudication; placement Established
- [Coordinate To Do](#hcm-fn-coordinate-to-do) — Canonical; pending_adjudication; placement Established
- [Coordinate Work Order](#hcm-fn-coordinate-work-order) — Canonical; pending_adjudication; placement Established
- [Determine Reconciled Activity Continuation](#hcm-fn-determine-reconciled-activity-continuation) — Proposed; pending_adjudication; placement Proposed
- [Establish Activity Effect Observation Basis](#hcm-fn-establish-activity-effect-observation-basis) — Proposed; pending_adjudication; placement Proposed
- [Reconcile Indeterminate Activity Effects](#hcm-fn-reconcile-indeterminate-activity-effects) — Proposed; pending_adjudication; placement Proposed
- [Supervise Activity Timeout / Escalation](#hcm-fn-supervise-activity-timeout-escalation) — Canonical; pending_adjudication; placement Established
- [Adjudicate Assurance Assessment](#hcm-fn-adjudicate-assurance-assessment) — Canonical; pending_adjudication; placement Established
- [Assess Assurance Evidence](#hcm-fn-assess-assurance-evidence) — Canonical; pending_adjudication; placement Established
- [Establish Assurance Context](#hcm-fn-establish-assurance-context) — Canonical; pending_adjudication; placement Established
- [Qualify Information Authority Standing](#hcm-fn-qualify-information-authority-standing) — Proposed; pending_adjudication; placement Proposed
- [Mediate Standards-Based Health Information Communication](#hcm-fn-mediate-standards-based-health-information-communication) — Canonical; pending_adjudication; placement Established
- [Capture Required Significant Audit Evidence](#hcm-fn-capture-required-significant-audit-evidence) — Proposed; pending_adjudication; placement Proposed

<a id="hcm-fn-associate-care-plan-context"></a>

## Associate Care Plan Context

Links care plans to active diagnoses, health problems, and care team members.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:associate-care-plan-context |
| canonicalId | 98cd313fdaa74634a8e1ae56a5bb06dc |
| displayName | Associate Care Plan Context |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.AssociateCarePlanContext |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.FN-AssociateCarePlanContext |
| Snapshot publication reference | HCM-FN-associate-care-plan-context |
| ArchiMate semantic ID | id-22047754df2957f382b6ec9aa06a64c1 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Associate Care Plan Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.FN-ASSOCIATE-CARE-PLAN-CONTEXT |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CARE-PLAN-ADMINISTRATION.FN-ASSOCIATE-CARE-PLAN-CONTEXT"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Care Plan Administration](business-enabling-capabilities.md#hcm-bec-care-plan-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:associate-care-plan-context`) and the element's version-qualified Properties.

<a id="hcm-fn-distribute-care-plan-change"></a>

## Distribute Care Plan Change

Broadcasts care plan updates to all enrolled care team participants.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:distribute-care-plan-change |
| canonicalId | cbfa51308103418a9dffe23a2d8cfd87 |
| displayName | Distribute Care Plan Change |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.DistributeCarePlanChange |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.FN-DistributeCarePlanChange |
| Snapshot publication reference | HCM-FN-distribute-care-plan-change |
| ArchiMate semantic ID | id-495036b56d7c52bf8a6b23c2d6b1df04 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Distribute Care Plan Change |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.FN-DISTRIBUTE-CARE-PLAN-CHANGE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CARE-PLAN-ADMINISTRATION.FN-DISTRIBUTE-CARE-PLAN-CHANGE"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Care Plan Administration](business-enabling-capabilities.md#hcm-bec-care-plan-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:distribute-care-plan-change`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-care-plan"></a>

## Receive Care Plan

Ingests multidisciplinary care plans, clinical goals, and patient action steps.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-care-plan |
| canonicalId | abb5a2ac72424923ae2da671b94b3b48 |
| displayName | Receive Care Plan |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.ReceiveCarePlan |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.FN-ReceiveCarePlan |
| Snapshot publication reference | HCM-FN-receive-care-plan |
| ArchiMate semantic ID | id-4d4de324383b53b2bf465ee762b0d838 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Care Plan |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.FN-RECEIVE-CARE-PLAN |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CARE-PLAN-ADMINISTRATION.FN-RECEIVE-CARE-PLAN"] |

Responsibility/framing context: [Care Plan Administration](business-enabling-capabilities.md#hcm-bec-care-plan-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-care-plan`) and the element's version-qualified Properties.

<a id="hcm-fn-bind-community-support-network-context"></a>

## Bind Community Support Network Context

Links community nursing teams with local non-clinical social care and home care packages.

Feature association not established: Strategy FEAT-SD-16 — Community Care Plan Synchronization remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:bind-community-support-network-context |
| canonicalId | 321fdf544ef044b8be100b656e16d7dc |
| displayName | Bind Community Support Network Context |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CommunityCareCoordination.BindCommunitySupportNetworkContext |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CommunityCareCoordination.FN-BindCommunitySupportNetworkContext |
| Snapshot publication reference | HCM-FN-bind-community-support-network-context |
| ArchiMate semantic ID | id-37d12a5fe08253029161cca25512b355 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bind Community Support Network Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-COMMUNITY-CARE.FN-BIND-COMMUNITY-SUPPORT-NETWORK-CONTEXT |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-COMMUNITY-CARE.FN-BIND-COMMUNITY-SUPPORT-NETWORK-CONTEXT"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Community Care Coordination](business-enabling-capabilities.md#hcm-bec-community-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:bind-community-support-network-context`) and the element's version-qualified Properties.

<a id="hcm-fn-synchronize-community-visit-record"></a>

## Synchronize Community Visit Record

Ingests clinical notes, wound assessments, and vital signs recorded in community and home settings.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:synchronize-community-visit-record |
| canonicalId | cbd4817eb757449a8f0a0e8ce02c5f07 |
| displayName | Synchronize Community Visit Record |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.CommunityCareCoordination.SynchronizeCommunityVisitRecord |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-CommunityCareCoordination.FN-SynchronizeCommunityVisitRecord |
| Snapshot publication reference | HCM-FN-synchronize-community-visit-record |
| ArchiMate semantic ID | id-7e56b0868f755f4faf51cb484b87aa99 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Synchronize Community Visit Record |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-COMMUNITY-CARE.FN-SYNCHRONIZE-COMMUNITY-VISIT-RECORD |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-COMMUNITY-CARE.FN-SYNCHRONIZE-COMMUNITY-VISIT-RECORD"] |

Responsibility/framing context: [Community Care Coordination](business-enabling-capabilities.md#hcm-bec-community-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:synchronize-community-visit-record`) and the element's version-qualified Properties.

<a id="hcm-fn-provide-outreach-visit-context"></a>

## Provide Outreach Visit Context

Packages and delivers portable clinical summaries and offline-capable care summaries for remote outreach clinics.

Derivation: FEAT-SD-17 in Strategy's Outreach Care Continuity (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md#15-outreach-care-harmonia-relevant) establishes this preparation/provision responsibility. Outcome reconciliation below does not supply pre-visit context. This Function remains owned by Outreach Care, uses governed source information without acquiring its originating authority and establishes no copying, storage or offline implementation mechanism.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:provide-outreach-visit-context |
| canonicalId | 4984d2f87f5a46869400c60cd5c5e0ab |
| displayName | Provide Outreach Visit Context |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.OutreachCareContinuity.ProvideOutreachVisitContext |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-OutreachCareContinuity.FN-ProvideOutreachVisitContext |
| Snapshot publication reference | HCM-FN-provide-outreach-visit-context |
| ArchiMate semantic ID | id-4e1a3f1063b359feb2632908e49d9819 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Provide Outreach Visit Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-OUTREACH-CARE.FN-PROVIDE-OUTREACH-VISIT-CONTEXT |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-OUTREACH-CARE.FN-PROVIDE-OUTREACH-VISIT-CONTEXT"] |

Responsibility/framing context: [Outreach Care Continuity](business-enabling-capabilities.md#hcm-bec-outreach-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:provide-outreach-visit-context`) and the element's version-qualified Properties.

<a id="hcm-fn-reconcile-outreach-clinical-batch"></a>

## Reconcile Outreach Clinical Batch

Reconciles and merges clinical documentation recorded during disconnected remote clinic operations.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:reconcile-outreach-clinical-batch |
| canonicalId | 53a7392c0ceb413ba8a00505b29f0f28 |
| displayName | Reconcile Outreach Clinical Batch |
| friendlyName | CareAdministrationEnablement.CarePlanAdministration.OutreachCareContinuity.ReconcileOutreachClinicalBatch |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-CarePlanAdministration.CT3-BEC-OutreachCareContinuity.FN-ReconcileOutreachClinicalBatch |
| Snapshot publication reference | HCM-FN-reconcile-outreach-clinical-batch |
| ArchiMate semantic ID | id-b1eb683fb7c95cc295bbdbfe9246e2a7 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Reconcile Outreach Clinical Batch |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-OUTREACH-CARE.FN-RECONCILE-OUTREACH-CLINICAL-BATCH |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-OUTREACH-CARE.FN-RECONCILE-OUTREACH-CLINICAL-BATCH"] |

Responsibility/framing context: [Outreach Care Continuity](business-enabling-capabilities.md#hcm-bec-outreach-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:reconcile-outreach-clinical-batch`) and the element's version-qualified Properties.

<a id="hcm-fn-distribute-governed-demographic-change"></a>

## Distribute Governed Demographic Change

Propagates verified demographic modifications to subscribed clinical subsystems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:distribute-governed-demographic-change |
| canonicalId | 61a4ae56602c4e2d9428169188e458d8 |
| displayName | Distribute Governed Demographic Change |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.DistributeGovernedDemographicChange |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.FN-DistributeGovernedDemographicChange |
| Snapshot publication reference | HCM-FN-distribute-governed-demographic-change |
| ArchiMate semantic ID | id-5758b5fbf13852b7a7a8cf87b0fa0f05 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Distribute Governed Demographic Change |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.FN-DISTRIBUTE-GOVERNED-DEMOGRAPHIC-CHANGE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-CLIENT-CENTRED-ADMINISTRATION.FN-DISTRIBUTE-GOVERNED-DEMOGRAPHIC-CHANGE"] |

Responsibility/framing context: [Client-Centred Administration](business-enabling-capabilities.md#hcm-bec-client-centred-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:distribute-governed-demographic-change`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-client-demographic-change"></a>

## Receive Client Demographic Change

Ingests administrative demographic updates from PAS/registration feeds.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-client-demographic-change |
| canonicalId | 459db1af34724863a57961ed921763d6 |
| displayName | Receive Client Demographic Change |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ReceiveClientDemographicChange |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.FN-ReceiveClientDemographicChange |
| Snapshot publication reference | HCM-FN-receive-client-demographic-change |
| ArchiMate semantic ID | id-a30906cf0d655e1f921dc845fc372b6c |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Client Demographic Change |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.FN-RECEIVE-CLIENT-DEMOGRAPHIC-CHANGE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-CLIENT-CENTRED-ADMINISTRATION.FN-RECEIVE-CLIENT-DEMOGRAPHIC-CHANGE"] |

Responsibility/framing context: [Client-Centred Administration](business-enabling-capabilities.md#hcm-bec-client-centred-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-client-demographic-change`) and the element's version-qualified Properties.

<a id="hcm-fn-project-screening-eligibility-timeline"></a>

## Project Screening Eligibility Timeline

Tracks age-based and risk-based screening schedules across primary care.

Feature association not established: FEAT-SD-14 — Screening Recall Notification Distribution is adjudicated sufficient through reusable composition/workflow above. Projecting/querying eligibility dates remains distinct from distribution; no dedicated recall Function or replacement association is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:project-screening-eligibility-timeline |
| canonicalId | afb08bdb99cd41d3a86ef7bf7ad5e92d |
| displayName | Project Screening Eligibility Timeline |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ScreeningAndImmunisationEnablement.ProjectScreeningEligibilityTimeline |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ScreeningAndImmunisationEnablement.FN-ProjectScreeningEligibilityTimeline |
| Snapshot publication reference | HCM-FN-project-screening-eligibility-timeline |
| ArchiMate semantic ID | id-d18c8ea89a455fcf8950d819bd98cdb0 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Project Screening Eligibility Timeline |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-PREVENTIVE-CARE.FN-PROJECT-SCREENING-ELIGIBILITY-TIMELINE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PREVENTIVE-CARE.FN-PROJECT-SCREENING-ELIGIBILITY-TIMELINE"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Screening and Immunisation Enablement](business-enabling-capabilities.md#hcm-bec-preventive-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:project-screening-eligibility-timeline`) and the element's version-qualified Properties.

<a id="hcm-fn-syndicate-immunisation-record"></a>

## Syndicate Immunisation Record

Records vaccine encounters and syndicates records to national immunisation registers (e.g., AIR).

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:syndicate-immunisation-record |
| canonicalId | 5a2c53df2e92444ab9b5018b224d98f9 |
| displayName | Syndicate Immunisation Record |
| friendlyName | CareAdministrationEnablement.ClientCentredAdministration.ScreeningAndImmunisationEnablement.SyndicateImmunisationRecord |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClientCentredAdministration.CT3-BEC-ScreeningAndImmunisationEnablement.FN-SyndicateImmunisationRecord |
| Snapshot publication reference | HCM-FN-syndicate-immunisation-record |
| ArchiMate semantic ID | id-4599ccecdc4c52e990a1731a0a149024 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Syndicate Immunisation Record |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-PREVENTIVE-CARE.FN-SYNDICATE-IMMUNISATION-RECORD |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PREVENTIVE-CARE.FN-SYNDICATE-IMMUNISATION-RECORD"] |

Responsibility/framing context: [Screening and Immunisation Enablement](business-enabling-capabilities.md#hcm-bec-preventive-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:syndicate-immunisation-record`) and the element's version-qualified Properties.

<a id="hcm-fn-distribute-clinical-communication"></a>

## Distribute Clinical Communication

Routes secure messages, referral letters, and critical alerts to practitioner endpoints.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:distribute-clinical-communication |
| canonicalId | 5b8d4d4a3f7147fc9ee5f114155c2db8 |
| displayName | Distribute Clinical Communication |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration.DistributeClinicalCommunication |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration.FN-DistributeClinicalCommunication |
| Snapshot publication reference | HCM-FN-distribute-clinical-communication |
| ArchiMate semantic ID | id-bdc68fb503b85bef83f10e758297e4b8 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Distribute Clinical Communication |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.FN-DISTRIBUTE-CLINICAL-COMMUNICATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.FN-DISTRIBUTE-CLINICAL-COMMUNICATION"] |

Responsibility/framing context: [Clinical Communication Administration](business-enabling-capabilities.md#hcm-bec-clinical-communication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:distribute-clinical-communication`) and the element's version-qualified Properties.

<a id="hcm-fn-record-communication-transaction"></a>

## Record Communication Transaction

Maintains an immutable audit trail of clinical message dispatch and receipt.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:record-communication-transaction |
| canonicalId | d64ced698052400fa7892e4d768b3f8f |
| displayName | Record Communication Transaction |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration.RecordCommunicationTransaction |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration.FN-RecordCommunicationTransaction |
| Snapshot publication reference | HCM-FN-record-communication-transaction |
| ArchiMate semantic ID | id-b3bc1d75ea5e53b893932aa4bdb852b8 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Record Communication Transaction |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.FN-RECORD-COMMUNICATION-TRANSACTION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.FN-RECORD-COMMUNICATION-TRANSACTION"] |

Responsibility/framing context: [Clinical Communication Administration](business-enabling-capabilities.md#hcm-bec-clinical-communication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:record-communication-transaction`) and the element's version-qualified Properties.

<a id="hcm-fn-track-business-delivery-acknowledgement"></a>

## Track Business Delivery Acknowledgement

Tracks end-to-end delivery state and records business-level delivery/rejection acknowledgements.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:track-business-delivery-acknowledgement |
| canonicalId | 75fb22db4c2346b99fd7c788264abb5d |
| displayName | Track Business Delivery Acknowledgement |
| friendlyName | CareAdministrationEnablement.ClinicalCommunicationAdministration.TrackBusinessDeliveryAcknowledgement |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalCommunicationAdministration.FN-TrackBusinessDeliveryAcknowledgement |
| Snapshot publication reference | HCM-FN-track-business-delivery-acknowledgement |
| ArchiMate semantic ID | id-71644914e346522b93fefe35d9626272 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Track Business Delivery Acknowledgement |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.FN-TRACK-BUSINESS-DELIVERY-ACKNOWLEDGEMENT |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.FN-TRACK-BUSINESS-DELIVERY-ACKNOWLEDGEMENT"] |

Responsibility/framing context: [Clinical Communication Administration](business-enabling-capabilities.md#hcm-bec-clinical-communication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:track-business-delivery-acknowledgement`) and the element's version-qualified Properties.

<a id="hcm-fn-govern-clinical-document-lifecycle"></a>

## Govern Clinical Document Lifecycle

Manages document states (scoped illustration, not a universal transition model): Draft → Preliminary → Final → Amended → Superseded → Entered-in-Error.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:govern-clinical-document-lifecycle |
| canonicalId | e6d00e00eba64ef4bc41d2af8bc3b944 |
| displayName | Govern Clinical Document Lifecycle |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.GovernClinicalDocumentLifecycle |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.FN-GovernClinicalDocumentLifecycle |
| Snapshot publication reference | HCM-FN-govern-clinical-document-lifecycle |
| ArchiMate semantic ID | id-0e3572d859da5d5a8f5500dd2fbcfa7e |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Govern Clinical Document Lifecycle |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.FN-GOVERN-CLINICAL-DOCUMENT-LIFECYCLE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION.FN-GOVERN-CLINICAL-DOCUMENT-LIFECYCLE"] |

Responsibility/framing context: [Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-clinical-record-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:govern-clinical-document-lifecycle`) and the element's version-qualified Properties.

<a id="hcm-fn-index-clinical-document-metadata"></a>

## Index Clinical Document Metadata

Indexes author, specialty, encounter, date, and document type (LOINC/SNOMED).

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:index-clinical-document-metadata |
| canonicalId | 0ee35bd069924319a7a9fe115d3c52b9 |
| displayName | Index Clinical Document Metadata |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.IndexClinicalDocumentMetadata |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.FN-IndexClinicalDocumentMetadata |
| Snapshot publication reference | HCM-FN-index-clinical-document-metadata |
| ArchiMate semantic ID | id-4051cd3da50d5865a9969f7683091d16 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Index Clinical Document Metadata |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.FN-INDEX-CLINICAL-DOCUMENT-METADATA |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION.FN-INDEX-CLINICAL-DOCUMENT-METADATA"] |

Responsibility/framing context: [Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-clinical-record-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:index-clinical-document-metadata`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-clinical-document"></a>

## Receive Clinical Document

Receives clinical documents from information suppliers.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-clinical-document |
| canonicalId | d8f80ba3a5a9401b84e6476cbec8cace |
| displayName | Receive Clinical Document |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.ReceiveClinicalDocument |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.FN-ReceiveClinicalDocument |
| Snapshot publication reference | HCM-FN-receive-clinical-document |
| ArchiMate semantic ID | id-903e691e2ae65aca97b310f55396f788 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Clinical Document |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.FN-RECEIVE-CLINICAL-DOCUMENT |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION.FN-RECEIVE-CLINICAL-DOCUMENT"] |

Responsibility/framing context: [Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-clinical-record-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-clinical-document`) and the element's version-qualified Properties.

<a id="hcm-fn-bind-diagnostic-report"></a>

## Bind Diagnostic Report

Associates structured observations, reference ranges, and abnormal flags with patient health records.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:bind-diagnostic-report |
| canonicalId | ee4bd6218f3a4029815bfc16912b88af |
| displayName | Bind Diagnostic Report |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.BindDiagnosticReport |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.FN-BindDiagnosticReport |
| Snapshot publication reference | HCM-FN-bind-diagnostic-report |
| ArchiMate semantic ID | id-9da35a8953c9589d9a71be7f2bbcd375 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bind Diagnostic Report |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.FN-BIND-DIAGNOSTIC-REPORT |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.FN-BIND-DIAGNOSTIC-REPORT"] |

Responsibility/framing context: [Diagnostic Administration](business-enabling-capabilities.md#hcm-bec-diagnostic-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:bind-diagnostic-report`) and the element's version-qualified Properties.

<a id="hcm-fn-correlate-diagnostic-request"></a>

## Correlate Diagnostic Request

Matches incoming lab/radiology results to active patient encounters and order requisitions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:correlate-diagnostic-request |
| canonicalId | 6797819b02e0405c8169fa57544d9606 |
| displayName | Correlate Diagnostic Request |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.CorrelateDiagnosticRequest |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.FN-CorrelateDiagnosticRequest |
| Snapshot publication reference | HCM-FN-correlate-diagnostic-request |
| ArchiMate semantic ID | id-15bcefdd16735ad2a992415a19bf730c |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Correlate Diagnostic Request |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.FN-CORRELATE-DIAGNOSTIC-REQUEST |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.FN-CORRELATE-DIAGNOSTIC-REQUEST"] |

Responsibility/framing context: [Diagnostic Administration](business-enabling-capabilities.md#hcm-bec-diagnostic-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:correlate-diagnostic-request`) and the element's version-qualified Properties.

<a id="hcm-fn-distribute-diagnostic-report"></a>

## Distribute Diagnostic Report

Delivers preliminary, final, and corrected reports to ordering and copied practitioners.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:distribute-diagnostic-report |
| canonicalId | ab6d94a9e06742aabe087f11d93f4334 |
| displayName | Distribute Diagnostic Report |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DistributeDiagnosticReport |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.FN-DistributeDiagnosticReport |
| Snapshot publication reference | HCM-FN-distribute-diagnostic-report |
| ArchiMate semantic ID | id-604fb85e248f5b05a72bbcdbb49c7df2 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Distribute Diagnostic Report |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.FN-DISTRIBUTE-DIAGNOSTIC-REPORT |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-DIAGNOSTIC-ADMINISTRATION.FN-DISTRIBUTE-DIAGNOSTIC-REPORT"] |

Responsibility/framing context: [Diagnostic Administration](business-enabling-capabilities.md#hcm-bec-diagnostic-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:distribute-diagnostic-report`) and the element's version-qualified Properties.

<a id="hcm-fn-publish-governed-diagnostic-report"></a>

## Publish Governed Diagnostic Report

Normalises and distributes structured diagnostic observations, reference ranges, and diagnostic image study links into the LHR.

Feature association not established: Strategy FEAT-SD-10 — Diagnostic History Consolidation remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:publish-governed-diagnostic-report |
| canonicalId | f9fcdc3162044865ac6f26015fdd5b17 |
| displayName | Publish Governed Diagnostic Report |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticServiceIntegration.PublishGovernedDiagnosticReport |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticServiceIntegration.FN-PublishGovernedDiagnosticReport |
| Snapshot publication reference | HCM-FN-publish-governed-diagnostic-report |
| ArchiMate semantic ID | id-9b0ffb4bf9a7563bb30384b56c9e60cd |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Publish Governed Diagnostic Report |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-SERVICES.FN-PUBLISH-GOVERNED-DIAGNOSTIC-REPORT |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-DIAGNOSTIC-SERVICES.FN-PUBLISH-GOVERNED-DIAGNOSTIC-REPORT"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Diagnostic Service Integration](business-enabling-capabilities.md#hcm-bec-diagnostic-services). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:publish-governed-diagnostic-report`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-diagnostic-requisition-feed"></a>

## Receive Diagnostic Requisition Feed

Normalises order requisitions from across the health network into modality worklists.

Feature association not established: Strategy FEAT-SD-09 — Laboratory & Imaging Result Distribution remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-diagnostic-requisition-feed |
| canonicalId | af3d47ada486425abc5ab5d054db7004 |
| displayName | Receive Diagnostic Requisition Feed |
| friendlyName | CareAdministrationEnablement.DiagnosticAdministration.DiagnosticServiceIntegration.ReceiveDiagnosticRequisitionFeed |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-DiagnosticAdministration.CT3-BEC-DiagnosticServiceIntegration.FN-ReceiveDiagnosticRequisitionFeed |
| Snapshot publication reference | HCM-FN-receive-diagnostic-requisition-feed |
| ArchiMate semantic ID | id-ea5a08617eec5b09b3cf10309bd80cea |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Diagnostic Requisition Feed |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-SERVICES.FN-RECEIVE-DIAGNOSTIC-REQUISITION-FEED |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-DIAGNOSTIC-SERVICES.FN-RECEIVE-DIAGNOSTIC-REQUISITION-FEED"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Diagnostic Service Integration](business-enabling-capabilities.md#hcm-bec-diagnostic-services). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-diagnostic-requisition-feed`) and the element's version-qualified Properties.

<a id="hcm-fn-establish-encounter-context"></a>

## Establish Encounter Context

Binds patient, attending practitioner, clinical class (e.g., Inpatient, Emergency, Ambulatory), and admitting diagnosis to a unique encounter identifier.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:establish-encounter-context |
| canonicalId | df00c187d509483a8408304a066abec4 |
| displayName | Establish Encounter Context |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.EstablishEncounterContext |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.FN-EstablishEncounterContext |
| Snapshot publication reference | HCM-FN-establish-encounter-context |
| ArchiMate semantic ID | id-2b790ac082dc56aa906dc1f92a3ea9ff |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Establish Encounter Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.FN-ESTABLISH-ENCOUNTER-CONTEXT |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.FN-ESTABLISH-ENCOUNTER-CONTEXT"] |

Responsibility/framing context: [Episode & Encounter Administration](business-enabling-capabilities.md#hcm-bec-episode-encounter-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:establish-encounter-context`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-encounter-care-place-association"></a>

## Maintain Encounter Care-Place Association

Binds the encounter to physical wards, rooms, and beds over time.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-encounter-care-place-association |
| canonicalId | 3ff076243fe141759e7d4401389b3527 |
| displayName | Maintain Encounter Care-Place Association |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.MaintainEncounterCarePlaceAssociation |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.FN-MaintainEncounterCarePlaceAssociation |
| Snapshot publication reference | HCM-FN-maintain-encounter-care-place-association |
| ArchiMate semantic ID | id-f539922ed2c75f219568496904aef7a0 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Encounter Care-Place Association |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.FN-MAINTAIN-ENCOUNTER-CARE-PLACE-ASSOCIATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.FN-MAINTAIN-ENCOUNTER-CARE-PLACE-ASSOCIATION"] |

Responsibility/framing context: [Episode & Encounter Administration](business-enabling-capabilities.md#hcm-bec-episode-encounter-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-encounter-care-place-association`) and the element's version-qualified Properties.

<a id="hcm-fn-progress-encounter-state"></a>

## Progress Encounter State

Coordinates encounter progression (scoped illustration; ON_LEAVE relationship unresolved): Planned → Arrived → In-Progress → Discharged → Completed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:progress-encounter-state |
| canonicalId | 223b1140628b444482bfb0a2c870d751 |
| displayName | Progress Encounter State |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.ProgressEncounterState |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.FN-ProgressEncounterState |
| Snapshot publication reference | HCM-FN-progress-encounter-state |
| ArchiMate semantic ID | id-fcce9c09613351d2963b96dcafa53a65 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Progress Encounter State |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.FN-PROGRESS-ENCOUNTER-STATE |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.FN-PROGRESS-ENCOUNTER-STATE"] |

Responsibility/framing context: [Episode & Encounter Administration](business-enabling-capabilities.md#hcm-bec-episode-encounter-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:progress-encounter-state`) and the element's version-qualified Properties.

<a id="hcm-fn-assemble-acute-clinical-baseline"></a>

## Assemble Acute Clinical Baseline

Rapidly aggregates previous discharge summaries, active medications, allergies, and diagnostic alerts upon acute hospital presentation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:assemble-acute-clinical-baseline |
| canonicalId | be64da1302bf4c4bae357295612e8e6e |
| displayName | Assemble Acute Clinical Baseline |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.AcuteCareMonitoringAndEventCoordination.AssembleAcuteClinicalBaseline |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-AcuteCareMonitoringAndEventCoordination.FN-AssembleAcuteClinicalBaseline |
| Snapshot publication reference | HCM-FN-assemble-acute-clinical-baseline |
| ArchiMate semantic ID | id-e3af93725ce6577e8d4c2a78f76b0c0e |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Assemble Acute Clinical Baseline |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ACUTE-CARE.FN-ASSEMBLE-ACUTE-CLINICAL-BASELINE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-ACUTE-CARE.FN-ASSEMBLE-ACUTE-CLINICAL-BASELINE"] |

Responsibility/framing context: [Acute Care Monitoring & Event Coordination](business-enabling-capabilities.md#hcm-bec-acute-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:assemble-acute-clinical-baseline`) and the element's version-qualified Properties.

<a id="hcm-fn-route-acute-critical-result-alert"></a>

## Route Acute Critical Result Alert

Immediately directs urgent lab/radiology alerts (e.g., critical troponin, intracranial bleed) to active acute attending clinicians.

Feature association not established: FEAT-SD-03 — Acute Clinical State Event Capture is adjudicated sufficient through general clinical-information responsibilities above. Routing a critical-result alert remains distinct from capture; no specialised capture Function or replacement association is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:route-acute-critical-result-alert |
| canonicalId | 66d0295d5a6c4eedba6406d6f672906c |
| displayName | Route Acute Critical Result Alert |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.AcuteCareMonitoringAndEventCoordination.RouteAcuteCriticalResultAlert |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-AcuteCareMonitoringAndEventCoordination.FN-RouteAcuteCriticalResultAlert |
| Snapshot publication reference | HCM-FN-route-acute-critical-result-alert |
| ArchiMate semantic ID | id-5eafdfa570265a29b3b02ee8c4b006bd |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Route Acute Critical Result Alert |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ACUTE-CARE.FN-ROUTE-ACUTE-CRITICAL-RESULT-ALERT |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-ACUTE-CARE.FN-ROUTE-ACUTE-CRITICAL-RESULT-ALERT"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Acute Care Monitoring & Event Coordination](business-enabling-capabilities.md#hcm-bec-acute-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:route-acute-critical-result-alert`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-medication-administration-event"></a>

## Receive Medication Administration Event

Records medication administration-event information, including nurse-administered doses, e-MAR documentation and infusion telemetry as non-exhaustive examples; scope is not limited to nursing, bedside or inpatient settings.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-medication-administration-event |
| canonicalId | 7437a97ccc3947b38f70d7da47bd0714 |
| displayName | Receive Medication Administration Event |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.ReceiveMedicationAdministrationEvent |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.FN-ReceiveMedicationAdministrationEvent |
| Snapshot publication reference | HCM-FN-receive-medication-administration-event |
| ArchiMate semantic ID | id-b16783cbaf425513bec8db37fe711f35 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Medication Administration Event |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.FN-RECEIVE-MEDICATION-ADMINISTRATION-EVENT |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-MEDICATION-ADMINISTRATION.FN-RECEIVE-MEDICATION-ADMINISTRATION-EVENT"] |

Responsibility/framing context: [Medication Administration](business-enabling-capabilities.md#hcm-bec-medication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-medication-administration-event`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-medication-order"></a>

## Receive Medication Order

Ingests inpatient and outpatient drug prescriptions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-medication-order |
| canonicalId | 96e2ae3ccb684635acf78037e75a1322 |
| displayName | Receive Medication Order |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.ReceiveMedicationOrder |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.FN-ReceiveMedicationOrder |
| Snapshot publication reference | HCM-FN-receive-medication-order |
| ArchiMate semantic ID | id-ace13d337e2e5cd5ba057448fbf14cc6 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Medication Order |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.FN-RECEIVE-MEDICATION-ORDER |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-MEDICATION-ADMINISTRATION.FN-RECEIVE-MEDICATION-ORDER"] |

Responsibility/framing context: [Medication Administration](business-enabling-capabilities.md#hcm-bec-medication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-medication-order`) and the element's version-qualified Properties.

<a id="hcm-fn-track-dispense-event"></a>

## Track Dispense Event

Ingests community and hospital pharmacy dispensing records.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:track-dispense-event |
| canonicalId | e4c3b7e257b3448aa79d66992e9e9bbc |
| displayName | Track Dispense Event |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.TrackDispenseEvent |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.FN-TrackDispenseEvent |
| Snapshot publication reference | HCM-FN-track-dispense-event |
| ArchiMate semantic ID | id-c6684c700bdd5988a6c66e38d27363ee |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Track Dispense Event |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.FN-TRACK-DISPENSE-EVENT |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-MEDICATION-ADMINISTRATION.FN-TRACK-DISPENSE-EVENT"] |

Responsibility/framing context: [Medication Administration](business-enabling-capabilities.md#hcm-bec-medication-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:track-dispense-event`) and the element's version-qualified Properties.

<a id="hcm-fn-correlate-adverse-reaction-allergy-profile"></a>

## Correlate Adverse Reaction & Allergy Profile

Binds confirmed substance allergies, adverse drug reactions, and severity grades to the patient identity.

Feature association not established: Strategy FEAT-SD-12 — Medication Administration Tracking remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:correlate-adverse-reaction--allergy-profile |
| canonicalId | 834d18fe21f14af7ad0c9773d2aaad1e |
| displayName | Correlate Adverse Reaction & Allergy Profile |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationTherapyProvisionAndMonitoring.CorrelateAdverseReactionAndAllergyProfile |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationTherapyProvisionAndMonitoring.FN-CorrelateAdverseReactionAndAllergyProfile |
| Snapshot publication reference | HCM-FN-correlate-adverse-reaction-allergy-profile |
| ArchiMate semantic ID | id-22942da4a40d55e0b27c14d51aeb41c5 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Correlate Adverse Reaction & Allergy Profile |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-THERAPY.FN-CORRELATE-ADVERSE-REACTION-ALLERGY-PROFILE |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-MEDICATION-THERAPY.FN-CORRELATE-ADVERSE-REACTION-ALLERGY-PROFILE"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Medication Therapy Provision and Monitoring](business-enabling-capabilities.md#hcm-bec-medication-therapy). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:correlate-adverse-reaction--allergy-profile`) and the element's version-qualified Properties.

<a id="hcm-fn-project-longitudinal-medication-history"></a>

## Project Longitudinal Medication History

Aggregates community dispensing, hospital e-MAR administrations, and specialist prescriptions into a unified chronological medication profile.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:project-longitudinal-medication-history |
| canonicalId | 494f529ada064f77836d40cfe9c7ab48 |
| displayName | Project Longitudinal Medication History |
| friendlyName | CareAdministrationEnablement.MedicationAdministration.MedicationTherapyProvisionAndMonitoring.ProjectLongitudinalMedicationHistory |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-MedicationAdministration.CT3-BEC-MedicationTherapyProvisionAndMonitoring.FN-ProjectLongitudinalMedicationHistory |
| Snapshot publication reference | HCM-FN-project-longitudinal-medication-history |
| ArchiMate semantic ID | id-77d166ce46a75c2e920cbebbc8bc419b |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Project Longitudinal Medication History |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-THERAPY.FN-PROJECT-LONGITUDINAL-MEDICATION-HISTORY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION.CT3-BEC-MEDICATION-THERAPY.FN-PROJECT-LONGITUDINAL-MEDICATION-HISTORY"] |

Responsibility/framing context: [Medication Therapy Provision and Monitoring](business-enabling-capabilities.md#hcm-bec-medication-therapy). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:project-longitudinal-medication-history`) and the element's version-qualified Properties.

<a id="hcm-fn-associate-order-outcome"></a>

## Associate Order Outcome

Correlates returning diagnostic reports and observations back to the originating order requisition.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:associate-order-outcome |
| canonicalId | 8bc2b528ca354c8ba95ddb44facdcd05 |
| displayName | Associate Order Outcome |
| friendlyName | CareAdministrationEnablement.OrderAdministration.AssociateOrderOutcome |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.FN-AssociateOrderOutcome |
| Snapshot publication reference | HCM-FN-associate-order-outcome |
| ArchiMate semantic ID | id-954c6d98636851f4aae3e1839793ab03 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Associate Order Outcome |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.FN-ASSOCIATE-ORDER-OUTCOME |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.FN-ASSOCIATE-ORDER-OUTCOME"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:associate-order-outcome`) and the element's version-qualified Properties.

<a id="hcm-fn-coordinate-order-modification-cancellation"></a>

## Coordinate Order Modification / Cancellation

Handles requests to amend clinical details or cancel unexecuted orders.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:coordinate-order-modification--cancellation |
| canonicalId | 6e445f8bc64e427fbe76b6dda20ed80f |
| displayName | Coordinate Order Modification / Cancellation |
| friendlyName | CareAdministrationEnablement.OrderAdministration.CoordinateOrderModificationCancellation |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.FN-CoordinateOrderModificationCancellation |
| Snapshot publication reference | HCM-FN-coordinate-order-modification-cancellation |
| ArchiMate semantic ID | id-5b999a821b055c9db42b5bfe4a3a5e0b |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Coordinate Order Modification / Cancellation |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.FN-COORDINATE-ORDER-MODIFICATION-CANCELLATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.FN-COORDINATE-ORDER-MODIFICATION-CANCELLATION"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:coordinate-order-modification--cancellation`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-order-progression"></a>

## Manage Order Progression

Tracks order state (scoped diagnostic/procedural illustration): Placed → Received → Specimen Collected → In-Progress → Preliminary Result → Final Result → Closed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-order-progression |
| canonicalId | 52f4b2dfb0c64e5aa4b378a9e63e0867 |
| displayName | Manage Order Progression |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ManageOrderProgression |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.FN-ManageOrderProgression |
| Snapshot publication reference | HCM-FN-manage-order-progression |
| ArchiMate semantic ID | id-cbba6dde3c645928a66b566c2619a0d7 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Order Progression |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.FN-MANAGE-ORDER-PROGRESSION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.FN-MANAGE-ORDER-PROGRESSION"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-order-progression`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-order-request"></a>

## Receive Order Request

Ingests structured orders containing clinical indication, requesting clinician, and test specifications.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-order-request |
| canonicalId | e5387fca59324407b8ed5d6db66ef93b |
| displayName | Receive Order Request |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ReceiveOrderRequest |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.FN-ReceiveOrderRequest |
| Snapshot publication reference | HCM-FN-receive-order-request |
| ArchiMate semantic ID | id-499d0b36b03e5e32b715135df5e21ab5 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Order Request |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.FN-RECEIVE-ORDER-REQUEST |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.FN-RECEIVE-ORDER-REQUEST"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-order-request`) and the element's version-qualified Properties.

<a id="hcm-fn-resolve-order-destination"></a>

## Resolve Order Destination

Evaluates order routing rules to determine target laboratory, imaging centre, or procedural unit.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:resolve-order-destination |
| canonicalId | 5e21ed48205446a9b05bc8017d1eccfc |
| displayName | Resolve Order Destination |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ResolveOrderDestination |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.FN-ResolveOrderDestination |
| Snapshot publication reference | HCM-FN-resolve-order-destination |
| ArchiMate semantic ID | id-ab4c9601aae85ada948cddc0f01034f1 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Resolve Order Destination |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.FN-RESOLVE-ORDER-DESTINATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.FN-RESOLVE-ORDER-DESTINATION"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:resolve-order-destination`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-procedural-documentation"></a>

## Receive Procedural Documentation

Ingests operation reports, anaesthetic records, and post-procedure notes.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-procedural-documentation |
| canonicalId | 62a47d22a07e4fcc8b3f99ccdffff390 |
| displayName | Receive Procedural Documentation |
| friendlyName | CareAdministrationEnablement.ProcedureAdministration.ReceiveProceduralDocumentation |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ProcedureAdministration.FN-ReceiveProceduralDocumentation |
| Snapshot publication reference | HCM-FN-receive-procedural-documentation |
| ArchiMate semantic ID | id-644ab003a5385bd2893c54fd68eb4668 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Procedural Documentation |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-PROCEDURE-ADMINISTRATION.FN-RECEIVE-PROCEDURAL-DOCUMENTATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-PROCEDURE-ADMINISTRATION.FN-RECEIVE-PROCEDURAL-DOCUMENTATION"] |

Responsibility/framing context: [Procedure Administration](business-enabling-capabilities.md#hcm-bec-procedure-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-procedural-documentation`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-procedure-booking-request"></a>

## Receive Procedure Booking / Request

Ingests procedural and surgical requisitions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-procedure-booking--request |
| canonicalId | 6d29d7e7422443b4acf3cbe85c4074e2 |
| displayName | Receive Procedure Booking / Request |
| friendlyName | CareAdministrationEnablement.ProcedureAdministration.ReceiveProcedureBookingRequest |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ProcedureAdministration.FN-ReceiveProcedureBookingRequest |
| Snapshot publication reference | HCM-FN-receive-procedure-booking-request |
| ArchiMate semantic ID | id-700369e28c2a542987af09c0e9e3fe98 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Procedure Booking / Request |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-PROCEDURE-ADMINISTRATION.FN-RECEIVE-PROCEDURE-BOOKING-REQUEST |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-PROCEDURE-ADMINISTRATION.FN-RECEIVE-PROCEDURE-BOOKING-REQUEST"] |

Responsibility/framing context: [Procedure Administration](business-enabling-capabilities.md#hcm-bec-procedure-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-procedure-booking--request`) and the element's version-qualified Properties.

<a id="hcm-fn-assemble-referral-context"></a>

## Assemble Referral Context

Gathers relevant clinical history, medications, and diagnostic investigations into the referral package.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:assemble-referral-context |
| canonicalId | b4a05bf97e844c24b04d27a6b325627a |
| displayName | Assemble Referral Context |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.AssembleReferralContext |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.FN-AssembleReferralContext |
| Snapshot publication reference | HCM-FN-assemble-referral-context |
| ArchiMate semantic ID | id-04d5a8caab2d5a928dbbb41662a50c00 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Assemble Referral Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.FN-ASSEMBLE-REFERRAL-CONTEXT |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.FN-ASSEMBLE-REFERRAL-CONTEXT"] |

Responsibility/framing context: [Referral Administration](business-enabling-capabilities.md#hcm-bec-referral-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:assemble-referral-context`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-referral-progression"></a>

## Manage Referral Progression

Tracks referral disposition in an illustrative specialist pathway; dispositions vary: Submitted → Triaged → Accepted/Waitlisted → Scheduled → Discharged.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-referral-progression |
| canonicalId | 7f9f01be4fd14facb943de5f63f1d8fd |
| displayName | Manage Referral Progression |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ManageReferralProgression |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.FN-ManageReferralProgression |
| Snapshot publication reference | HCM-FN-manage-referral-progression |
| ArchiMate semantic ID | id-770c0b13f2a05e4184064d7056a35ac1 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Referral Progression |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.FN-MANAGE-REFERRAL-PROGRESSION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.FN-MANAGE-REFERRAL-PROGRESSION"] |

Responsibility/framing context: [Referral Administration](business-enabling-capabilities.md#hcm-bec-referral-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-referral-progression`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-referral"></a>

## Receive Referral

Validates and ingests structured electronic referrals, clinical indications, and priority categories.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-referral |
| canonicalId | 28775f5af15646369539a4fc0a7ff8a9 |
| displayName | Receive Referral |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ReceiveReferral |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.FN-ReceiveReferral |
| Snapshot publication reference | HCM-FN-receive-referral |
| ArchiMate semantic ID | id-39045ea0855355d28b37e4f376c51eb0 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Referral |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.FN-RECEIVE-REFERRAL |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.FN-RECEIVE-REFERRAL"] |

Responsibility/framing context: [Referral Administration](business-enabling-capabilities.md#hcm-bec-referral-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-referral`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-appointment-notification"></a>

## Receive Appointment Notification

Ingests appointment creation, rescheduling, cancellation, and check-in events from host PAS engines.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-appointment-notification |
| canonicalId | 8aa2e7b197ae4a0c99b654b9f1a312e8 |
| displayName | Receive Appointment Notification |
| friendlyName | CareAdministrationEnablement.SchedulingAdministration.ReceiveAppointmentNotification |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-SchedulingAdministration.FN-ReceiveAppointmentNotification |
| Snapshot publication reference | HCM-FN-receive-appointment-notification |
| ArchiMate semantic ID | id-209ef4034356542cbe27b4406febd737 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Appointment Notification |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-SCHEDULING-ADMINISTRATION.FN-RECEIVE-APPOINTMENT-NOTIFICATION |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-SCHEDULING-ADMINISTRATION.FN-RECEIVE-APPOINTMENT-NOTIFICATION"] |

Responsibility/framing context: [Scheduling Administration](business-enabling-capabilities.md#hcm-bec-scheduling-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-appointment-notification`) and the element's version-qualified Properties.

<a id="hcm-fn-synchronise-appointment-status"></a>

## Synchronise Appointment Status

Projects synchronised appointment timelines to clinical dashboards and care coordinators.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:synchronise-appointment-status |
| canonicalId | c60fc8ce05104185b5240a910bfd5f67 |
| displayName | Synchronise Appointment Status |
| friendlyName | CareAdministrationEnablement.SchedulingAdministration.SynchroniseAppointmentStatus |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-SchedulingAdministration.FN-SynchroniseAppointmentStatus |
| Snapshot publication reference | HCM-FN-synchronise-appointment-status |
| ArchiMate semantic ID | id-0616e6ebddc05985a67e31401d2ff91f |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Synchronise Appointment Status |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-SCHEDULING-ADMINISTRATION.FN-SYNCHRONISE-APPOINTMENT-STATUS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-SCHEDULING-ADMINISTRATION.FN-SYNCHRONISE-APPOINTMENT-STATUS"] |

Responsibility/framing context: [Scheduling Administration](business-enabling-capabilities.md#hcm-bec-scheduling-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:synchronise-appointment-status`) and the element's version-qualified Properties.

<a id="hcm-fn-evaluate-client-confidentiality-restrictions"></a>

## Evaluate Client Confidentiality Restrictions

Evaluates sensitive record flags (e.g., VIP masking, restricted department exclusions).

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:evaluate-client-confidentiality-restrictions |
| canonicalId | a61324c3ef734910af5886f048cd3bf5 |
| displayName | Evaluate Client Confidentiality Restrictions |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientPrivacyGuardrailManagement.EvaluateClientConfidentialityRestrictions |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientPrivacyGuardrailManagement.FN-EvaluateClientConfidentialityRestrictions |
| Snapshot publication reference | HCM-FN-evaluate-client-confidentiality-restrictions |
| ArchiMate semantic ID | id-bd1e798662925ef29822e2377210cf54 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Evaluate Client Confidentiality Restrictions |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-PRIVACY.FN-EVALUATE-CLIENT-CONFIDENTIALITY-RESTRICTIONS |

Responsibility/framing context: [Client Privacy Guardrail Management](business-enabling-capabilities.md#hcm-bec-client-privacy). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:evaluate-client-confidentiality-restrictions`) and the element's version-qualified Properties.

<a id="hcm-fn-evaluate-client-consent"></a>

## Evaluate Client Consent

Evaluates patient opt-in, opt-out, and general information sharing preferences against proposed exchanges.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:evaluate-client-consent |
| canonicalId | b9e978a651cf4ff3bb690af50b8528cd |
| displayName | Evaluate Client Consent |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientPrivacyGuardrailManagement.EvaluateClientConsent |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientPrivacyGuardrailManagement.FN-EvaluateClientConsent |
| Snapshot publication reference | HCM-FN-evaluate-client-consent |
| ArchiMate semantic ID | id-965ced5ca08457018a292547bfbeca68 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Evaluate Client Consent |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-PRIVACY.FN-EVALUATE-CLIENT-CONSENT |

Responsibility/framing context: [Client Privacy Guardrail Management](business-enabling-capabilities.md#hcm-bec-client-privacy). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:evaluate-client-consent`) and the element's version-qualified Properties.

<a id="hcm-fn-govern-client-relationship-validity"></a>

## Govern Client Relationship Validity

Verifies power-of-attorney documents, guardianship orders, and proxy validity periods.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:govern-client-relationship-validity |
| canonicalId | dc87537df6254fc5bdeeca0ccc011679 |
| displayName | Govern Client Relationship Validity |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientRelationshipAdministration.GovernClientRelationshipValidity |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientRelationshipAdministration.FN-GovernClientRelationshipValidity |
| Snapshot publication reference | HCM-FN-govern-client-relationship-validity |
| ArchiMate semantic ID | id-ab1579b60f365f6bbd9e314962d4f8a0 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Govern Client Relationship Validity |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-RELATIONSHIP.FN-GOVERN-CLIENT-RELATIONSHIP-VALIDITY |

Responsibility/framing context: [Client Relationship Administration](business-enabling-capabilities.md#hcm-bec-client-relationship). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:govern-client-relationship-validity`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-client-relationships"></a>

## Maintain Client Relationships

Records links between patients and carers, next of kin, nominated representatives, and legal guardians.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-client-relationships |
| canonicalId | 1b2ce4a240384672b78976904b83a011 |
| displayName | Maintain Client Relationships |
| friendlyName | EntityInformationEnablement.ClientAdministration.ClientRelationshipAdministration.MaintainClientRelationships |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-ClientRelationshipAdministration.FN-MaintainClientRelationships |
| Snapshot publication reference | HCM-FN-maintain-client-relationships |
| ArchiMate semantic ID | id-c2a5ac5fb5f15aed9b1e6e83f04fbb32 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Client Relationships |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-CLIENT-RELATIONSHIP.FN-MAINTAIN-CLIENT-RELATIONSHIPS |

Responsibility/framing context: [Client Relationship Administration](business-enabling-capabilities.md#hcm-bec-client-relationship). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-client-relationships`) and the element's version-qualified Properties.

<a id="hcm-fn-establish-healthcare-subject-context"></a>

## Establish Healthcare Subject Context

Binds demographic, cultural, and communication requirements to a patient record.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:establish-healthcare-subject-context |
| canonicalId | f445f86f9de24461aabee527e5b41287 |
| displayName | Establish Healthcare Subject Context |
| friendlyName | EntityInformationEnablement.ClientAdministration.HealthcareSubjectProfileAdministration.EstablishHealthcareSubjectContext |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-HealthcareSubjectProfileAdministration.FN-EstablishHealthcareSubjectContext |
| Snapshot publication reference | HCM-FN-establish-healthcare-subject-context |
| ArchiMate semantic ID | id-9da65caf5f135ac78df48ea95bfe5114 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Establish Healthcare Subject Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-HEALTHCARE-SUBJECT.FN-ESTABLISH-HEALTHCARE-SUBJECT-CONTEXT |

Responsibility/framing context: [Healthcare Subject Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-subject). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:establish-healthcare-subject-context`) and the element's version-qualified Properties.

<a id="hcm-fn-govern-subject-demographic-context"></a>

## Govern Subject Demographic Context

Applies validation rules to address changes, date of birth verification, and death notifications.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:govern-subject-demographic-context |
| canonicalId | 76288dacb9034e0bbf75b43d8afe59d2 |
| displayName | Govern Subject Demographic Context |
| friendlyName | EntityInformationEnablement.ClientAdministration.HealthcareSubjectProfileAdministration.GovernSubjectDemographicContext |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-HealthcareSubjectProfileAdministration.FN-GovernSubjectDemographicContext |
| Snapshot publication reference | HCM-FN-govern-subject-demographic-context |
| ArchiMate semantic ID | id-27ebef4bad0357f1aee3f607c73743e1 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Govern Subject Demographic Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-HEALTHCARE-SUBJECT.FN-GOVERN-SUBJECT-DEMOGRAPHIC-CONTEXT |

Responsibility/framing context: [Healthcare Subject Profile Administration](business-enabling-capabilities.md#hcm-bec-healthcare-subject). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:govern-subject-demographic-context`) and the element's version-qualified Properties.

<a id="hcm-fn-apply-governed-person-identity-correction"></a>

## Apply Governed Person Identity Correction

Processes externally authoritative corrections and merge/unlink outcomes, updates governed identity associations and propagates resulting managed-state consequences with audit history. Harmonia does not adjudicate the originating person-merge decision.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:apply-governed-person-identity-correction |
| canonicalId | 844b9abe470d4f4684f35adddf1b880d |
| displayName | Apply Governed Person Identity Correction |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.ApplyGovernedPersonIdentityCorrection |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FN-ApplyGovernedPersonIdentityCorrection |
| Snapshot publication reference | HCM-FN-apply-governed-person-identity-correction |
| ArchiMate semantic ID | id-918518c4d0f75cd893e76e8f6c35914c |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Apply Governed Person Identity Correction |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.FN-APPLY-GOVERNED-PERSON-IDENTITY-CORRECTION |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:apply-governed-person-identity-correction`) and the element's version-qualified Properties.

<a id="hcm-fn-correlate-person-identifiers"></a>

## Correlate Person Identifiers

Correlates local identifiers (e.g., Hospital MRN, National IHI, Pathology ID) using relationships established by authoritative information, retaining their source and namespace.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:correlate-person-identifiers |
| canonicalId | 3050366e2e1d47eeaa4cfd6db92c0a56 |
| displayName | Correlate Person Identifiers |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.CorrelatePersonIdentifiers |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FN-CorrelatePersonIdentifiers |
| Snapshot publication reference | HCM-FN-correlate-person-identifiers |
| ArchiMate semantic ID | id-182ebd29ca745760b524b487507e8523 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Correlate Person Identifiers |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.FN-CORRELATE-PERSON-IDENTIFIERS |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:correlate-person-identifiers`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-person-identity-aliases"></a>

## Maintain Person Identity Aliases

Records and qualifies alias names, titles, and emergency aliases for a governed person identity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-person-identity-aliases |
| canonicalId | 9db2528e180a418eacbad3acf86b3956 |
| displayName | Maintain Person Identity Aliases |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.MaintainPersonIdentityAliases |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FN-MaintainPersonIdentityAliases |
| Snapshot publication reference | HCM-FN-maintain-person-identity-aliases |
| ArchiMate semantic ID | id-3653057cd45b562983af5a2b3fc48af3 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Person Identity Aliases |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.FN-MAINTAIN-PERSON-IDENTITY-ALIASES |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-person-identity-aliases`) and the element's version-qualified Properties.

<a id="hcm-fn-resolve-person-identifier"></a>

## Resolve Person Identifier

Validates and retrieves the authoritative demographic record bound to a specific identifier.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:resolve-person-identifier |
| canonicalId | 2f128e1d8de642478c18b7a0ccb8e083 |
| displayName | Resolve Person Identifier |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.ResolvePersonIdentifier |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FN-ResolvePersonIdentifier |
| Snapshot publication reference | HCM-FN-resolve-person-identifier |
| ArchiMate semantic ID | id-84e64f42373e53cc8d696d0c99fd549d |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Resolve Person Identifier |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.FN-RESOLVE-PERSON-IDENTIFIER |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:resolve-person-identifier`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-device-association"></a>

## Maintain Device Association

Records active associations between clinical devices, patient encounters, and specific care-places (e.g., Bed 4 Vital Signs Monitor).

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-device-association |
| canonicalId | fc0f4d9c781241359d783d26f038b7ff |
| displayName | Maintain Device Association |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.MaintainDeviceAssociation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.FN-MaintainDeviceAssociation |
| Snapshot publication reference | HCM-FN-maintain-device-association |
| ArchiMate semantic ID | id-0507f1b0229854f98fb8a03e435d9ef5 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Device Association |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.FN-MAINTAIN-DEVICE-ASSOCIATION |

Responsibility/framing context: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-device-association`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-device-communication-endpoints"></a>

## Maintain Device Communication Endpoints

Manages network telemetry addresses, gateway ports, and protocol bindings for medical devices.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-device-communication-endpoints |
| canonicalId | ab5a7538fbd449ae8214d584b7d53a43 |
| displayName | Maintain Device Communication Endpoints |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.MaintainDeviceCommunicationEndpoints |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.FN-MaintainDeviceCommunicationEndpoints |
| Snapshot publication reference | HCM-FN-maintain-device-communication-endpoints |
| ArchiMate semantic ID | id-82de8c345117561ba0af47fe509fd0ce |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Device Communication Endpoints |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.FN-MAINTAIN-DEVICE-COMMUNICATION-ENDPOINTS |

Responsibility/framing context: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-device-communication-endpoints`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-device-definition"></a>

## Maintain Device Definition

Models medical device models, manufacturers, regulatory classifications (e.g., TGA/FDA classes), and calibration specifications.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-device-definition |
| canonicalId | d44083e67fb540579737337cabf2c227 |
| displayName | Maintain Device Definition |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.MaintainDeviceDefinition |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.FN-MaintainDeviceDefinition |
| Snapshot publication reference | HCM-FN-maintain-device-definition |
| ArchiMate semantic ID | id-772704a342cb54978a14e80570febd9b |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Device Definition |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.FN-MAINTAIN-DEVICE-DEFINITION |

Responsibility/framing context: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-device-definition`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-device-instance-identity"></a>

## Maintain Device Instance Identity

Tracks physical serial numbers, Unique Device Identifiers (UDI), asset tags, and firmware versions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-device-instance-identity |
| canonicalId | 927796d77f5e4f61835bd2a7bcceb681 |
| displayName | Maintain Device Instance Identity |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.MaintainDeviceInstanceIdentity |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.FN-MaintainDeviceInstanceIdentity |
| Snapshot publication reference | HCM-FN-maintain-device-instance-identity |
| ArchiMate semantic ID | id-bf6ed39e3cd8524084cd01ea09c850c3 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Device Instance Identity |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.FN-MAINTAIN-DEVICE-INSTANCE-IDENTITY |

Responsibility/framing context: [Clinical Device Administration](business-enabling-capabilities.md#hcm-bec-clinical-device-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-device-instance-identity`) and the element's version-qualified Properties.

<a id="hcm-fn-track-mobile-clinical-asset-operational-state"></a>

## Track Mobile Clinical Asset Operational State

Manages the real-time location and operational readiness of mobile ventilators, infusion pumps, and telemetry transmitters.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:track-mobile-clinical-asset-operational-state |
| canonicalId | 0a31e03a6c7847c0b7c04310ca0c29e9 |
| displayName | Track Mobile Clinical Asset Operational State |
| friendlyName | EntityInformationEnablement.ClinicalDeviceAdministration.DeviceUtilisationAdministration.TrackMobileClinicalAssetOperationalState |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClinicalDeviceAdministration.CT3-BEC-DeviceUtilisationAdministration.FN-TrackMobileClinicalAssetOperationalState |
| Snapshot publication reference | HCM-FN-track-mobile-clinical-asset-operational-state |
| ArchiMate semantic ID | id-a619eedf22f3555d95b6292d4d0f3e19 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Track Mobile Clinical Asset Operational State |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-UTILISATION-ADMINISTRATION.FN-TRACK-MOBILE-CLINICAL-ASSET-OPERATIONAL-STATE |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-CLINICAL-RESOURCE-MANAGEMENT.FN-TRACK-MOBILE-CLINICAL-ASSET-OPERATIONAL-STATE"] |

Responsibility/framing context: [Device Utilisation Administration](business-enabling-capabilities.md#hcm-bec-reviewed-device-utilisation-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:track-mobile-clinical-asset-operational-state`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-healthcare-service-definition"></a>

## Maintain Healthcare Service Definition

Defines clinical service offerings (e.g., Cardiology Outpatient Clinic, MRI Imaging, Acute Stroke Care), specialty classifications, and clinical scope.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-healthcare-service-definition |
| canonicalId | 833a98c58dae416da783ef6e77290576 |
| displayName | Maintain Healthcare Service Definition |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.MaintainHealthcareServiceDefinition |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.FN-MaintainHealthcareServiceDefinition |
| Snapshot publication reference | HCM-FN-maintain-healthcare-service-definition |
| ArchiMate semantic ID | id-17407719d48e575da32ea8eeb69e2c78 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Healthcare Service Definition |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.FN-MAINTAIN-HEALTHCARE-SERVICE-DEFINITION |

Responsibility/framing context: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-healthcare-service-definition`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-service-location-provider-map"></a>

## Maintain Service / Location / Provider Map

Binds health service definitions to delivering organisations, physical clinic locations, and participating practitioner roles.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-service--location--provider-map |
| canonicalId | ea06c77ea3c841cc94cb1686295322b4 |
| displayName | Maintain Service / Location / Provider Map |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.MaintainServiceLocationProviderMap |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.FN-MaintainServiceLocationProviderMap |
| Snapshot publication reference | HCM-FN-maintain-service-location-provider-map |
| ArchiMate semantic ID | id-3a28b4fe87f75eed9fa6146a295314d8 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Service / Location / Provider Map |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.FN-MAINTAIN-SERVICE-LOCATION-PROVIDER-MAP |

Responsibility/framing context: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-service--location--provider-map`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-service-availability"></a>

## Maintain Service Availability

Governs regular operating hours, emergency intake availability, and seasonal clinic schedules.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-service-availability |
| canonicalId | 38321dd2581b46a182dcdf52865d44be |
| displayName | Maintain Service Availability |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.MaintainServiceAvailability |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.FN-MaintainServiceAvailability |
| Snapshot publication reference | HCM-FN-maintain-service-availability |
| ArchiMate semantic ID | id-77763efa85a55cacba8cbb88c85b9fdc |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Service Availability |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.FN-MAINTAIN-SERVICE-AVAILABILITY |

Responsibility/framing context: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-service-availability`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-service-eligibility-rules"></a>

## Maintain Service Eligibility Rules

Configures catchment criteria, age thresholds, referral requirements, and clinical indications for service access.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-service-eligibility-rules |
| canonicalId | e1e1cf2aa30840368d107ab9dfd0257f |
| displayName | Maintain Service Eligibility Rules |
| friendlyName | EntityInformationEnablement.HealthServiceAdministration.MaintainServiceEligibilityRules |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-HealthServiceAdministration.FN-MaintainServiceEligibilityRules |
| Snapshot publication reference | HCM-FN-maintain-service-eligibility-rules |
| ArchiMate semantic ID | id-e8a40cea1e0b5af19040ffc81a006e05 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Service Eligibility Rules |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.FN-MAINTAIN-SERVICE-ELIGIBILITY-RULES |

Responsibility/framing context: [Health Service Administration](business-enabling-capabilities.md#hcm-bec-health-service-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-service-eligibility-rules`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-care-place-definition"></a>

## Maintain Care-Place Definition

Defines static bed bays, treatment chairs, surgical tables, and emergency resuscitation cubicles, including equipment capabilities and oxygen/telemetry infrastructure.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-care-place-definition |
| canonicalId | ad9aa4eeecf84eb08460234ed0dc4ecc |
| displayName | Maintain Care-Place Definition |
| friendlyName | EntityInformationEnablement.LocationAdministration.MaintainCarePlaceDefinition |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.FN-MaintainCarePlaceDefinition |
| Snapshot publication reference | HCM-FN-maintain-care-place-definition |
| ArchiMate semantic ID | id-5c75704849c65375aa020ede0e2630fb |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Care-Place Definition |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.FN-MAINTAIN-CARE-PLACE-DEFINITION |

Responsibility/framing context: [Location Administration](business-enabling-capabilities.md#hcm-bec-location-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-care-place-definition`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-healthcare-location"></a>

## Maintain Healthcare Location

Records physical sites, campuses, outpatient clinics, and community health centres.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-healthcare-location |
| canonicalId | 448f1c9317914498ab246f74d4ce2bd6 |
| displayName | Maintain Healthcare Location |
| friendlyName | EntityInformationEnablement.LocationAdministration.MaintainHealthcareLocation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.FN-MaintainHealthcareLocation |
| Snapshot publication reference | HCM-FN-maintain-healthcare-location |
| ArchiMate semantic ID | id-c65df326add75d0e9198503fe6e0a9d6 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Healthcare Location |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.FN-MAINTAIN-HEALTHCARE-LOCATION |

Responsibility/framing context: [Location Administration](business-enabling-capabilities.md#hcm-bec-location-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-healthcare-location`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-physical-location-hierarchy"></a>

## Maintain Physical Location Hierarchy

Models the nested structural containment tree: Campus → Building → Floor → Ward/Suite → Room/Bay → Bed Care-Place.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-physical-location-hierarchy |
| canonicalId | d217a5cd3a3d40e9a89068d1680ec45f |
| displayName | Maintain Physical Location Hierarchy |
| friendlyName | EntityInformationEnablement.LocationAdministration.MaintainPhysicalLocationHierarchy |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-LocationAdministration.FN-MaintainPhysicalLocationHierarchy |
| Snapshot publication reference | HCM-FN-maintain-physical-location-hierarchy |
| ArchiMate semantic ID | id-2151b18d7c015e25b9be93fa56df4828 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Physical Location Hierarchy |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.FN-MAINTAIN-PHYSICAL-LOCATION-HIERARCHY |

Responsibility/framing context: [Location Administration](business-enabling-capabilities.md#hcm-bec-location-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-physical-location-hierarchy`) and the element's version-qualified Properties.

<a id="hcm-fn-govern-organisation-profile"></a>

## Govern Organisation Profile

Maintains official trading names, enterprise registration numbers, and governing bodies.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:govern-organisation-profile |
| canonicalId | 08990a2aa346403f8dd7d716acf188bf |
| displayName | Govern Organisation Profile |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.GovernOrganisationProfile |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.FN-GovernOrganisationProfile |
| Snapshot publication reference | HCM-FN-govern-organisation-profile |
| ArchiMate semantic ID | id-0adb23cbf657505e8c7507c0c0e091b4 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Govern Organisation Profile |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.FN-GOVERN-ORGANISATION-PROFILE |

Responsibility/framing context: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:govern-organisation-profile`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-organisation-contacts"></a>

## Maintain Organisation Contacts

Manages administrative, clinical, and emergency contact details for facilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-organisation-contacts |
| canonicalId | 4584f74d08834bc2bc595463ad51bd37 |
| displayName | Maintain Organisation Contacts |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.MaintainOrganisationContacts |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.FN-MaintainOrganisationContacts |
| Snapshot publication reference | HCM-FN-maintain-organisation-contacts |
| ArchiMate semantic ID | id-6eecef731db45041849f55ebdefe6249 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Organisation Contacts |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.FN-MAINTAIN-ORGANISATION-CONTACTS |

Responsibility/framing context: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-organisation-contacts`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-organisational-structure"></a>

## Maintain Organisational Structure

Models parent-subsidiary relationships, clinical directorates, departments, and operational divisions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-organisational-structure |
| canonicalId | 5ae4edf0813f4a738fdb3160f3e77b0d |
| displayName | Maintain Organisational Structure |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.MaintainOrganisationalStructure |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.FN-MaintainOrganisationalStructure |
| Snapshot publication reference | HCM-FN-maintain-organisational-structure |
| ArchiMate semantic ID | id-2f14a324591554848cd0e4bae1f49b14 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Organisational Structure |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.FN-MAINTAIN-ORGANISATIONAL-STRUCTURE |

Responsibility/framing context: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-organisational-structure`) and the element's version-qualified Properties.

<a id="hcm-fn-verify-healthcare-organisation"></a>

## Verify Healthcare Organisation

Validates enterprise legal identity, national facility identifiers (e.g., HPI-O), and accreditation status.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:verify-healthcare-organisation |
| canonicalId | c39236ca89864f089684242df5647c9d |
| displayName | Verify Healthcare Organisation |
| friendlyName | EntityInformationEnablement.OrganisationAdministration.VerifyHealthcareOrganisation |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-OrganisationAdministration.FN-VerifyHealthcareOrganisation |
| Snapshot publication reference | HCM-FN-verify-healthcare-organisation |
| ArchiMate semantic ID | id-6832728f94f65356949743549350bd84 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Verify Healthcare Organisation |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.FN-VERIFY-HEALTHCARE-ORGANISATION |

Responsibility/framing context: [Organisation Administration](business-enabling-capabilities.md#hcm-bec-organisation-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:verify-healthcare-organisation`) and the element's version-qualified Properties.

<a id="hcm-fn-bind-practitioner-electronic-endpoints"></a>

## Bind Practitioner Electronic Endpoints

Associates secure messaging addresses, direct communication endpoints, and notification preferences to practitioner roles.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:bind-practitioner-electronic-endpoints |
| canonicalId | 11962189d1734cca840842209613c8d1 |
| displayName | Bind Practitioner Electronic Endpoints |
| friendlyName | EntityInformationEnablement.ProviderAdministration.BindPractitionerElectronicEndpoints |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.FN-BindPractitionerElectronicEndpoints |
| Snapshot publication reference | HCM-FN-bind-practitioner-electronic-endpoints |
| ArchiMate semantic ID | id-cb9b2d485588571b96fe872d274c65f1 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bind Practitioner Electronic Endpoints |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.FN-BIND-PRACTITIONER-ELECTRONIC-ENDPOINTS |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:bind-practitioner-electronic-endpoints`) and the element's version-qualified Properties.

<a id="hcm-fn-govern-practitioner-profile"></a>

## Govern Practitioner Profile

Maintains official names, professional identifiers (e.g., HPI-I, Prescriber Number), and clinical qualifications.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:govern-practitioner-profile |
| canonicalId | a9a1a2c5ab544c029ba0c362a305eaa5 |
| displayName | Govern Practitioner Profile |
| friendlyName | EntityInformationEnablement.ProviderAdministration.GovernPractitionerProfile |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.FN-GovernPractitionerProfile |
| Snapshot publication reference | HCM-FN-govern-practitioner-profile |
| ArchiMate semantic ID | id-de0eadc17c6455718271c16ffa31bf49 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Govern Practitioner Profile |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.FN-GOVERN-PRACTITIONER-PROFILE |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:govern-practitioner-profile`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-clinical-privileges"></a>

## Maintain Clinical Privileges

Records approved clinical scopes of practice, procedural authorizations, and prescribing privileges.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-clinical-privileges |
| canonicalId | 4965ae4538cc42cd8af1bd9a1905b108 |
| displayName | Maintain Clinical Privileges |
| friendlyName | EntityInformationEnablement.ProviderAdministration.MaintainClinicalPrivileges |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.FN-MaintainClinicalPrivileges |
| Snapshot publication reference | HCM-FN-maintain-clinical-privileges |
| ArchiMate semantic ID | id-a9675be4707353ed9fc08432d644c6ce |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Clinical Privileges |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.FN-MAINTAIN-CLINICAL-PRIVILEGES |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-clinical-privileges`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-practitioner-affiliations"></a>

## Maintain Practitioner Affiliations

Tracks employment, admitting rights, and consulting appointments at specific healthcare facilities.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-practitioner-affiliations |
| canonicalId | 7dc829546e99424dbbf8f19b4565d3fc |
| displayName | Maintain Practitioner Affiliations |
| friendlyName | EntityInformationEnablement.ProviderAdministration.MaintainPractitionerAffiliations |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.FN-MaintainPractitionerAffiliations |
| Snapshot publication reference | HCM-FN-maintain-practitioner-affiliations |
| ArchiMate semantic ID | id-366324f3604e5c07bad2b45321625ec3 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Practitioner Affiliations |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.FN-MAINTAIN-PRACTITIONER-AFFILIATIONS |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-practitioner-affiliations`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-practitioner-roles"></a>

## Maintain Practitioner Roles

Assigns and maintains clinical roles (e.g., Specialist Physician, Registrar, General Practitioner) fulfilled by a practitioner.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-practitioner-roles |
| canonicalId | 08a269d84dad46a58d6b9181b465ddc4 |
| displayName | Maintain Practitioner Roles |
| friendlyName | EntityInformationEnablement.ProviderAdministration.MaintainPractitionerRoles |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.FN-MaintainPractitionerRoles |
| Snapshot publication reference | HCM-FN-maintain-practitioner-roles |
| ArchiMate semantic ID | id-f7c3a656a4245b8fa867bf405a606439 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Practitioner Roles |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.FN-MAINTAIN-PRACTITIONER-ROLES |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-practitioner-roles`) and the element's version-qualified Properties.

<a id="hcm-fn-verify-practitioner-registration"></a>

## Verify Practitioner Registration

Validates national registration, specialty endorsements, and disciplinary sanctions against regulatory registries (e.g., AHPRA).

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:verify-practitioner-registration |
| canonicalId | a9f4fe6090bf4a4b946ae7064213945c |
| displayName | Verify Practitioner Registration |
| friendlyName | EntityInformationEnablement.ProviderAdministration.VerifyPractitionerRegistration |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.FN-VerifyPractitionerRegistration |
| Snapshot publication reference | HCM-FN-verify-practitioner-registration |
| ArchiMate semantic ID | id-8c54f5ea750a55469a87c79390daf451 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Verify Practitioner Registration |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.FN-VERIFY-PRACTITIONER-REGISTRATION |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:verify-practitioner-registration`) and the element's version-qualified Properties.

<a id="hcm-fn-assemble-purpose-qualified-research-release"></a>

## Assemble Purpose-Qualified Research Release

Construct a release containing only existing information permitted for the explicitly authorised research purpose.

**Overview**

Moves a qualified request to a bounded release with source attribution and applicable use constraints.

**Boundary**

Does not author ethics approvals, promise de-identification or acquire ownership of research conclusions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-assemble-purpose-qualified-research-release |
| canonicalId | bf4354a421174a9ba56c281f4dcd11f6 |
| displayName | Assemble Purpose-Qualified Research Release |
| friendlyName | HealthInformaticsManagement.ResearchInformationCuration.AssemblePurposeQualifiedResearchRelease |
| canonicalName | CT1-BEC-HealthInformaticsManagement.CT2-BEC-ResearchInformationCuration.FN-AssemblePurposeQualifiedResearchRelease |
| Snapshot publication reference | HCM-FN-assemble-purpose-qualified-research-release |
| ArchiMate semantic ID | id-8c21d9dcc7a15e44bad797ce63244403 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT2-BEC-AUTHORISED-RESEARCH-INFORMATION-PROVISION.FN-ASSEMBLE-PURPOSE-QUALIFIED-RESEARCH-RELEASE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT3-BEC-AUTHORISED-RESEARCH-INFORMATION-PROVISION.FN-ASSEMBLE-PURPOSE-QUALIFIED-RESEARCH-RELEASE"] |

Responsibility/framing context: [Research Information Curation](business-enabling-capabilities.md#hcm-bec-authorised-research-information-provision). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-assemble-purpose-qualified-research-release`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-operational-bed-state"></a>

## Maintain Operational Bed State

Tracks real-time bed status and environmental locks.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-operational-bed-state |
| canonicalId | 22f3016539cf4c2db68baa9921acf31b |
| displayName | Maintain Operational Bed State |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement.MaintainOperationalBedState |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement.FN-MaintainOperationalBedState |
| Snapshot publication reference | HCM-FN-maintain-operational-bed-state |
| ArchiMate semantic ID | id-eedfd03346fe5a00b6057c04d5444af3 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Operational Bed State |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.FN-MAINTAIN-OPERATIONAL-BED-STATE |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-BED-CARE-PLACE-MANAGEMENT.FN-MAINTAIN-OPERATIONAL-BED-STATE"] |

Responsibility/framing context: [Bed & Care-Place Management](business-enabling-capabilities.md#hcm-bec-bed-care-place-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-operational-bed-state`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-bed-turnover-progression"></a>

## Manage Bed Turnover Progression

Coordinates cleaning, disinfection, and preparation: Vacated → Cleaning Requested → Cleaning In-Progress → Inspected/Ready → Occupied.

Additional Feature responsibility: FEAT-HSO-13 — Bed Readiness Notification requires the existing turnover behaviour/exposure to notify bed allocation managers and clinical units when a cleaned bed is ready. Readiness notification remains distinct from availability query and from physical placement; no new lifecycle state or Service is introduced.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-bed-turnover-progression |
| canonicalId | 7637b9bcb38d4e3abbe01a59dc23f485 |
| displayName | Manage Bed Turnover Progression |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement.ManageBedTurnoverProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement.FN-ManageBedTurnoverProgression |
| Snapshot publication reference | HCM-FN-manage-bed-turnover-progression |
| ArchiMate semantic ID | id-74201cc998e2512aa13c3f0d391098ac |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Bed Turnover Progression |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.FN-MANAGE-BED-TURNOVER-PROGRESSION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-BED-CARE-PLACE-MANAGEMENT.FN-MANAGE-BED-TURNOVER-PROGRESSION"] |

Responsibility/framing context: [Bed & Care-Place Management](business-enabling-capabilities.md#hcm-bec-bed-care-place-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-bed-turnover-progression`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-clinic-practice-capacity"></a>

## Manage Clinic & Practice Capacity

Understands and manages available, committed and utilised clinic/practice capacity in relation to practitioner availability, sessions, rooms, appointments and patient demand.

Responsibility boundary: Operating-session tracking and individual queue progression contribute to this responsibility; neither alone expresses overall service capacity. Capacity coordination does not own source bookings, clinical decisions or practitioner rostering.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-clinic--practice-capacity |
| canonicalId | 389fb97b2bcd4527959a61f2a288bc39 |
| displayName | Manage Clinic & Practice Capacity |
| friendlyName | HealthServiceOperationsEnablement.ClinicAndPracticeOperations.ManageClinicAndPracticeCapacity |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicAndPracticeOperations.FN-ManageClinicAndPracticeCapacity |
| Snapshot publication reference | HCM-FN-manage-clinic-practice-capacity |
| ArchiMate semantic ID | id-eee6ac056d535d228edc9d54a8afe162 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Clinic & Practice Capacity |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.FN-MANAGE-CLINIC-PRACTICE-CAPACITY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-CLINIC-PRACTICE-OPERATIONS.FN-MANAGE-CLINIC-PRACTICE-CAPACITY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Clinic & Practice Operations](business-enabling-capabilities.md#hcm-bec-clinic-practice-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-clinic--practice-capacity`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-clinic-queue-progression"></a>

## Manage Clinic Queue Progression

Tracks patient arrival, rooming, consultation in-progress, and check-out states.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-clinic-queue-progression |
| canonicalId | cb3f8c48623b4479988c45de86d5a59f |
| displayName | Manage Clinic Queue Progression |
| friendlyName | HealthServiceOperationsEnablement.ClinicAndPracticeOperations.ManageClinicQueueProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicAndPracticeOperations.FN-ManageClinicQueueProgression |
| Snapshot publication reference | HCM-FN-manage-clinic-queue-progression |
| ArchiMate semantic ID | id-b773f620177e5b778afa02b9fbafd744 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Clinic Queue Progression |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.FN-MANAGE-CLINIC-QUEUE-PROGRESSION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-CLINIC-PRACTICE-OPERATIONS.FN-MANAGE-CLINIC-QUEUE-PROGRESSION"] |

Responsibility/framing context: [Clinic & Practice Operations](business-enabling-capabilities.md#hcm-bec-clinic-practice-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-clinic-queue-progression`) and the element's version-qualified Properties.

<a id="hcm-fn-track-clinic-operating-session"></a>

## Track Clinic Operating Session

Ingests and tracks clinic operating-session start, delay and completion, distinct from an individual patient's queue progression.

Derivation: FEAT-HSO-01 directly establishes this session-level behaviour; Manage Clinic Queue Progression and its Process concern individual patients and do not supply it.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:track-clinic-operating-session |
| canonicalId | 83a85a08803c4c00b8de4a38e8c9d77e |
| displayName | Track Clinic Operating Session |
| friendlyName | HealthServiceOperationsEnablement.ClinicAndPracticeOperations.TrackClinicOperatingSession |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicAndPracticeOperations.FN-TrackClinicOperatingSession |
| Snapshot publication reference | HCM-FN-track-clinic-operating-session |
| ArchiMate semantic ID | id-7c8a36e718645113b7b49db09dbe1618 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Track Clinic Operating Session |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.FN-TRACK-CLINIC-OPERATING-SESSION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-CLINIC-PRACTICE-OPERATIONS.FN-TRACK-CLINIC-OPERATING-SESSION"] |

Responsibility/framing context: [Clinic & Practice Operations](business-enabling-capabilities.md#hcm-bec-clinic-practice-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:track-clinic-operating-session`) and the element's version-qualified Properties.

<a id="hcm-fn-verify-clinical-qualification-for-work-dispatch"></a>

## Verify Clinical Qualification for Work Dispatch

Validates that a practitioner possesses active clinical credentials and required competency certificates before dispatch of specialised work.

Derivation: FEAT-HSO-23 belongs to Strategy Clinical Qualification Management. Provider Administration establishes professional qualification/registration and privilege information, but the dispatch-specific qualification/competency checkpoint was not expressed by that information management alone.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:verify-clinical-qualification-for-work-dispatch |
| canonicalId | 1dfebc3aa5f949e5a20faa504eadbee1 |
| displayName | Verify Clinical Qualification for Work Dispatch |
| friendlyName | HealthServiceOperationsEnablement.ClinicalQualificationAdministration.VerifyClinicalQualificationForWorkDispatch |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalQualificationAdministration.FN-VerifyClinicalQualificationForWorkDispatch |
| Snapshot publication reference | HCM-FN-verify-clinical-qualification-for-work-dispatch |
| ArchiMate semantic ID | id-6933f43beb6a5e83ad5a87ba30539e9f |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Verify Clinical Qualification for Work Dispatch |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-QUALIFICATION-MANAGEMENT.FN-VERIFY-CLINICAL-QUALIFICATION-FOR-WORK-DISPATCH |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT.CT3-BEC-CLINICAL-QUALIFICATION-MANAGEMENT.FN-VERIFY-CLINICAL-QUALIFICATION-FOR-WORK-DISPATCH"] |

Responsibility/framing context: [Clinical Qualification Administration](business-enabling-capabilities.md#hcm-bec-clinical-qualification-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:verify-clinical-qualification-for-work-dispatch`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-discharge-coordination-progression"></a>

## Manage Discharge Coordination Progression

Synchronises multi-agency discharge readiness (illustrative preparation; publication, authorisation and confirmed exit are distinct): Discharge Planning Initiated → Medications Reconciled → Transport Booked → Discharge Summary Finalised → Physically Departed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-discharge-coordination-progression |
| canonicalId | ecda6d08166d4df78c15c87387fbe108 |
| displayName | Manage Discharge Coordination Progression |
| friendlyName | HealthServiceOperationsEnablement.DischargeManagement.ManageDischargeCoordinationProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-DischargeManagement.FN-ManageDischargeCoordinationProgression |
| Snapshot publication reference | HCM-FN-manage-discharge-coordination-progression |
| ArchiMate semantic ID | id-6c95803fb2b1558cac669cfeb342e240 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Discharge Coordination Progression |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-DISCHARGE-MANAGEMENT.FN-MANAGE-DISCHARGE-COORDINATION-PROGRESSION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-DISCHARGE-MANAGEMENT.FN-MANAGE-DISCHARGE-COORDINATION-PROGRESSION"] |

Responsibility/framing context: [Discharge Management](business-enabling-capabilities.md#hcm-bec-discharge-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-discharge-coordination-progression`) and the element's version-qualified Properties.

<a id="hcm-fn-coordinate-ed-departmental-capacity"></a>

## Coordinate ED Departmental Capacity

Understands and manages available, committed and utilised ED capacity in relation to care-place availability, demand, patient presence/progression, acuity and operational constraints; retains waiting room occupancy, resuscitation bay availability and time-to-triage metrics.

Capacity boundary: Existing departmental capacity responsibility is clarified, not duplicated. Care-place state contributes to service capacity; capacity coordination neither performs clinical triage nor establishes clinical acuity assertions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:coordinate-ed-departmental-capacity |
| canonicalId | a1559e7ed12c4f249128fba2ff74d536 |
| displayName | Coordinate ED Departmental Capacity |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.CoordinateEdDepartmentalCapacity |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.FN-CoordinateEdDepartmentalCapacity |
| Snapshot publication reference | HCM-FN-coordinate-ed-departmental-capacity |
| ArchiMate semantic ID | id-2eadf51e0bc055b0b8a4dc2ce970299c |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Coordinate ED Departmental Capacity |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.FN-COORDINATE-ED-DEPARTMENTAL-CAPACITY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.FN-COORDINATE-ED-DEPARTMENTAL-CAPACITY"] |

Responsibility/framing context: [Emergency Department Operations](business-enabling-capabilities.md#hcm-bec-emergency-department-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:coordinate-ed-departmental-capacity`) and the element's version-qualified Properties.

<a id="hcm-fn-bind-ed-triage-assessment"></a>

## Bind ED Triage Assessment

Captures ATS (Australasian Triage Scale) category, presenting complaint, and ambulance paramedic handover data.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:bind-ed-triage-assessment |
| canonicalId | fa51fad9b9344546953ff8ec5b5f39ca |
| displayName | Bind ED Triage Assessment |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyCareProgression.BindEdTriageAssessment |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyCareProgression.FN-BindEdTriageAssessment |
| Snapshot publication reference | HCM-FN-bind-ed-triage-assessment |
| ArchiMate semantic ID | id-e50b7c01e26f5ee78df9f4493e435dc0 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bind ED Triage Assessment |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-CARE.FN-BIND-ED-TRIAGE-ASSESSMENT |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-EMERGENCY-CARE.FN-BIND-ED-TRIAGE-ASSESSMENT"] |

Responsibility/framing context: [Emergency Care Progression](business-enabling-capabilities.md#hcm-bec-emergency-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:bind-ed-triage-assessment`) and the element's version-qualified Properties.

<a id="hcm-fn-provide-emergency-lhr-summary"></a>

## Provide Emergency LHR Summary

Delivers rapid, override-capable access to Advance Care Directives, allergy records, and recent hospital admissions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:provide-emergency-lhr-summary |
| canonicalId | a7235e8b249a4914b951e0d795132316 |
| displayName | Provide Emergency LHR Summary |
| friendlyName | HealthServiceOperationsEnablement.EmergencyDepartmentOperations.EmergencyCareProgression.ProvideEmergencyLhrSummary |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-EmergencyDepartmentOperations.CT3-BEC-EmergencyCareProgression.FN-ProvideEmergencyLhrSummary |
| Snapshot publication reference | HCM-FN-provide-emergency-lhr-summary |
| ArchiMate semantic ID | id-efacdaa3bfb552668d112954d36dfcaa |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Provide Emergency LHR Summary |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-CARE.FN-PROVIDE-EMERGENCY-LHR-SUMMARY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-EMERGENCY-CARE.FN-PROVIDE-EMERGENCY-LHR-SUMMARY"] |

Responsibility/framing context: [Emergency Care Progression](business-enabling-capabilities.md#hcm-bec-emergency-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:provide-emergency-lhr-summary`) and the element's version-qualified Properties.

<a id="hcm-fn-integrate-mobile-work-management"></a>

## Integrate Mobile Work Management

Exchanges externally established work/assignment, dispatch and operational progression/status information, with applicable worker/context information, between mobile work-management mechanisms and Harmonia operational coordination.

Derivation and boundary: The human adjudication of FEAT-HSO-16 establishes integration with mobile work/task management. This responsibility is absent from presence observation and visit-specific milestone ingestion. It does not determine, allocate or manage the underlying clinical work, clinical handover or EMR worklists. Work Allocation & Dispatch retains non-clinical operational Work Order responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:integrate-mobile-work-management |
| canonicalId | 81f97ad819da48f09f26029066550e08 |
| displayName | Integrate Mobile Work Management |
| friendlyName | HealthServiceOperationsEnablement.MobileStaffManagement.IntegrateMobileWorkManagement |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-MobileStaffManagement.FN-IntegrateMobileWorkManagement |
| Snapshot publication reference | HCM-FN-integrate-mobile-work-management |
| ArchiMate semantic ID | id-547d48631fb55c009ddbda0680ab5941 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Integrate Mobile Work Management |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.FN-INTEGRATE-MOBILE-WORK-MANAGEMENT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-MOBILE-STAFF-MANAGEMENT.FN-INTEGRATE-MOBILE-WORK-MANAGEMENT"] |

Responsibility/framing context: [Mobile Staff Management](business-enabling-capabilities.md#hcm-bec-mobile-staff-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:integrate-mobile-work-management`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-mobile-service-capacity"></a>

## Manage Mobile Service Capacity

Understands and manages available, committed and utilised mobile-service capacity in relation to workforce availability, geography, time, workload and applicable service demand.

Responsibility boundary: Staff presence and externally established assignments/visit progression contribute to capacity; they do not alone express it. Capacity coordination does not determine clinical work, own the source workforce roster or allocate underlying clinical duties.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-mobile-service-capacity |
| canonicalId | ad5cb5451ef645acb55ceb0c942ea013 |
| displayName | Manage Mobile Service Capacity |
| friendlyName | HealthServiceOperationsEnablement.MobileStaffManagement.ManageMobileServiceCapacity |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-MobileStaffManagement.FN-ManageMobileServiceCapacity |
| Snapshot publication reference | HCM-FN-manage-mobile-service-capacity |
| ArchiMate semantic ID | id-82c4b2c578c85fbd8aac33420efc2b45 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Mobile Service Capacity |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.FN-MANAGE-MOBILE-SERVICE-CAPACITY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-MOBILE-STAFF-MANAGEMENT.FN-MANAGE-MOBILE-SERVICE-CAPACITY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Mobile Staff Management](business-enabling-capabilities.md#hcm-bec-mobile-staff-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-mobile-service-capacity`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-mobile-visit-progression"></a>

## Receive Mobile Visit Progression

Ingests arrival, visit-progression and safety check-in milestones from roving staff.

Derivation: FEAT-HSO-17 directly requires visit-specific milestone ingestion. It remains distinct from presence, capacity assessment and work-management integration. Received progression can inform mobile capacity without determining clinical work or clinical completion.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-mobile-visit-progression |
| canonicalId | 71700f211ef4403f98a2b9b11d8d7848 |
| displayName | Receive Mobile Visit Progression |
| friendlyName | HealthServiceOperationsEnablement.MobileStaffManagement.ReceiveMobileVisitProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-MobileStaffManagement.FN-ReceiveMobileVisitProgression |
| Snapshot publication reference | HCM-FN-receive-mobile-visit-progression |
| ArchiMate semantic ID | id-86865fa1a326580ba9356c52a71c0a21 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Mobile Visit Progression |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.FN-RECEIVE-MOBILE-VISIT-PROGRESSION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-MOBILE-STAFF-MANAGEMENT.FN-RECEIVE-MOBILE-VISIT-PROGRESSION"] |

Responsibility/framing context: [Mobile Staff Management](business-enabling-capabilities.md#hcm-bec-mobile-staff-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-mobile-visit-progression`) and the element's version-qualified Properties.

<a id="hcm-fn-track-operational-staff-presence"></a>

## Track Operational Staff Presence

Tracks active on-duty presence and physical zone assignment for porters, phlebotomists, and roving nurses.

Feature association not established: FEAT-HSO-16 — Mobile Worker Task Dispatch retains its Strategy identity with the integration boundary below. Presence/zone tracking remains distinct from mobile work-management integration; no replacement association to presence is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:track-operational-staff-presence |
| canonicalId | 9ccdb235a69946e0ad5a99001a1d16e0 |
| displayName | Track Operational Staff Presence |
| friendlyName | HealthServiceOperationsEnablement.MobileStaffManagement.TrackOperationalStaffPresence |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-MobileStaffManagement.FN-TrackOperationalStaffPresence |
| Snapshot publication reference | HCM-FN-track-operational-staff-presence |
| ArchiMate semantic ID | id-afcd103a122553998a713eb5cd299ab9 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Track Operational Staff Presence |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.FN-TRACK-OPERATIONAL-STAFF-PRESENCE |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-MOBILE-STAFF-MANAGEMENT.FN-TRACK-OPERATIONAL-STAFF-PRESENCE"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Mobile Staff Management](business-enabling-capabilities.md#hcm-bec-mobile-staff-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:track-operational-staff-presence`) and the element's version-qualified Properties.

<a id="hcm-fn-resolve-active-on-call-coverage"></a>

## Resolve Active On-Call Coverage

Resolves primary, secondary, and tertiary on-call clinical specialists for emergency escalation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:resolve-active-on-call-coverage |
| canonicalId | a88d3779091140c6b90f98d550b5387f |
| displayName | Resolve Active On-Call Coverage |
| friendlyName | HealthServiceOperationsEnablement.OnCallManagement.ResolveActiveOnCallCoverage |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OnCallManagement.FN-ResolveActiveOnCallCoverage |
| Snapshot publication reference | HCM-FN-resolve-active-on-call-coverage |
| ArchiMate semantic ID | id-4654b4feaedd552eb03bfe929f3eda40 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Resolve Active On-Call Coverage |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-ON-CALL-MANAGEMENT.FN-RESOLVE-ACTIVE-ON-CALL-COVERAGE |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-ON-CALL-MANAGEMENT.FN-RESOLVE-ACTIVE-ON-CALL-COVERAGE"] |

Responsibility/framing context: [On-Call Management](business-enabling-capabilities.md#hcm-bec-on-call-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:resolve-active-on-call-coverage`) and the element's version-qualified Properties.

<a id="hcm-fn-capture-post-clinic-follow-up-requirements"></a>

## Capture Post-Clinic Follow-up Requirements

Captures follow-up booking and diagnostic-order requirements generated at clinic completion.

Derivation: FEAT-HSO-10 directly establishes this capture responsibility. The Clinic queue Process's consultation-concluded checkpoint describes orders placed, not Outpatient Operations' capture of follow-up requirements. Capture does not authorise clinical orders, own authoritative bookings or establish a new Service dependency.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:capture-post-clinic-follow-up-requirements |
| canonicalId | 9136f75069bf4770bcc8dc395204d475 |
| displayName | Capture Post-Clinic Follow-up Requirements |
| friendlyName | HealthServiceOperationsEnablement.OutpatientOperations.CapturePostClinicFollowUpRequirements |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OutpatientOperations.FN-CapturePostClinicFollowUpRequirements |
| Snapshot publication reference | HCM-FN-capture-post-clinic-follow-up-requirements |
| ArchiMate semantic ID | id-a5645ead0f4752ff9aad8f40b413743e |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Capture Post-Clinic Follow-up Requirements |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OUTPATIENT-OPERATIONS.FN-CAPTURE-POST-CLINIC-FOLLOW-UP-REQUIREMENTS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-OUTPATIENT-OPERATIONS.FN-CAPTURE-POST-CLINIC-FOLLOW-UP-REQUIREMENTS"] |

Responsibility/framing context: [Outpatient Operations](business-enabling-capabilities.md#hcm-bec-outpatient-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:capture-post-clinic-follow-up-requirements`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-outpatient-capacity"></a>

## Manage Outpatient Capacity

Understands and manages available, committed and utilised outpatient capacity through clinic/session, practitioner and room availability, appointment demand, patient arrival/waiting and operational progression. Retains specialty room allocation and clinician arrival tracking.

Exposure qualification: Query behaviour and Outpatient Operations ownership remain established; a meaningful outside consumer/mandate is not identified by the current description. That exposure relationship remains unestablished rather than populated with an inferred Actor, Role or Capability.

Capacity boundary: Operational coordination does not acquire authoritative booking/rostering or clinical-work management. Room-occupancy query remains narrower than the capacity responsibility; no enlarged exposure or new named Service is inferred.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-outpatient-capacity |
| canonicalId | 45fd6190b7334f838575881725f6a66b |
| displayName | Manage Outpatient Capacity |
| friendlyName | HealthServiceOperationsEnablement.OutpatientOperations.ManageOutpatientCapacity |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-OutpatientOperations.FN-ManageOutpatientCapacity |
| Snapshot publication reference | HCM-FN-manage-outpatient-capacity |
| ArchiMate semantic ID | id-b3c65c2c554f57d39709388e51350155 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Outpatient Capacity |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OUTPATIENT-OPERATIONS.FN-MANAGE-OUTPATIENT-CAPACITY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-OUTPATIENT-OPERATIONS.FN-MANAGE-OUTPATIENT-CAPACITY"] |
| Name aliases | ["Coordinate Outpatient Session Utilisation"] |

Responsibility/framing context: [Outpatient Operations](business-enabling-capabilities.md#hcm-bec-outpatient-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-outpatient-capacity`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-patient-transport-progression"></a>

## Manage Patient Transport Progression

Coordinates intra-facility and inter-facility patient movements: Transport Requested → Porter Dispatched → Patient Collected → In-Transit → Delivered at Destination → Handover Completed.

Additional Feature responsibility: FEAT-HSO-24 — Transport Request Ingestion is directly supported by the request stage of this same Function/Process. It ingests internal portering and external ambulance/inter-hospital requests with clinical mobility requirements; request ingestion is distinct from dispatch, transit and arrival.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-patient-transport-progression |
| canonicalId | 8a2136544646458fb60413afed2b66e1 |
| displayName | Manage Patient Transport Progression |
| friendlyName | HealthServiceOperationsEnablement.PatientTransport.ManagePatientTransportProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-PatientTransport.FN-ManagePatientTransportProgression |
| Snapshot publication reference | HCM-FN-manage-patient-transport-progression |
| ArchiMate semantic ID | id-90cff0c5aaab5ceebeb1279b2c0235b3 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Patient Transport Progression |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-PATIENT-TRANSPORT.FN-MANAGE-PATIENT-TRANSPORT-PROGRESSION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-PATIENT-TRANSPORT.FN-MANAGE-PATIENT-TRANSPORT-PROGRESSION"] |

Responsibility/framing context: [Patient Transport](business-enabling-capabilities.md#hcm-bec-patient-transport). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-patient-transport-progression`) and the element's version-qualified Properties.

<a id="hcm-fn-aggregate-service-capacity-telemetry"></a>

## Aggregate Service Capacity Telemetry

Consumes and coordinates context-specific service-capacity information for broader situational awareness, alongside bed occupancy, ICU surge capacity and ventilator availability across regional hospitals.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:aggregate-service-capacity-telemetry |
| canonicalId | ac08378c955048ef81ed5d9ff9c5cc7e |
| displayName | Aggregate Service Capacity Telemetry |
| friendlyName | HealthServiceOperationsEnablement.ServiceCapacityManagement.AggregateServiceCapacityTelemetry |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ServiceCapacityManagement.FN-AggregateServiceCapacityTelemetry |
| Snapshot publication reference | HCM-FN-aggregate-service-capacity-telemetry |
| ArchiMate semantic ID | id-5c009333040a59219acb5e9e33ccf9d5 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Aggregate Service Capacity Telemetry |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-SERVICE-CAPACITY-MANAGEMENT.FN-AGGREGATE-SERVICE-CAPACITY-TELEMETRY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-CAPACITY-MANAGEMENT.FN-AGGREGATE-SERVICE-CAPACITY-TELEMETRY"] |

Responsibility/framing context: [Service Capacity Management](business-enabling-capabilities.md#hcm-bec-service-capacity-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:aggregate-service-capacity-telemetry`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-theatre-capacity"></a>

## Manage Theatre Capacity

Understands and manages available, committed and utilised theatre capacity in relation to theatre, team and equipment availability, scheduled cases and case progression.

Responsibility boundary: Case milestones and targeted resource notifications contribute; they do not by themselves establish available or committed theatre service capacity. Clinical case determination and originating team/equipment information remain separately governed.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-theatre-capacity |
| canonicalId | 3566d549770f490dba823fe3b0421446 |
| displayName | Manage Theatre Capacity |
| friendlyName | HealthServiceOperationsEnablement.TheatreOperations.ManageTheatreCapacity |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-TheatreOperations.FN-ManageTheatreCapacity |
| Snapshot publication reference | HCM-FN-manage-theatre-capacity |
| ArchiMate semantic ID | id-4ee034a0ff3a51ab8dcc0d6f61609e96 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Theatre Capacity |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS.FN-MANAGE-THEATRE-CAPACITY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-THEATRE-OPERATIONS.FN-MANAGE-THEATRE-CAPACITY"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Theatre Operations](business-enabling-capabilities.md#hcm-bec-theatre-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-theatre-capacity`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-theatre-case-progression"></a>

## Manage Theatre Case Progression

Coordinates surgical case milestones: Case Called → Patient in Anaesthetic Bay → Anaesthesia Commenced → Knife to Skin → Procedure Finished → In PACU/Recovery → Ward Handover Complete.

Additional Feature responsibility: FEAT-HSO-06 — Perioperative Resource Notification is realised by dispatching progression-triggered alerts to portering, sterilisation and recovery teams within this same case-progression behaviour/exposure. Milestone observation alone does not establish notification; no new Function or Service is created.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-theatre-case-progression |
| canonicalId | 88d2d17cccfb4a578b835be588043bab |
| displayName | Manage Theatre Case Progression |
| friendlyName | HealthServiceOperationsEnablement.TheatreOperations.ManageTheatreCaseProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-TheatreOperations.FN-ManageTheatreCaseProgression |
| Snapshot publication reference | HCM-FN-manage-theatre-case-progression |
| ArchiMate semantic ID | id-53ee867aea295460a3318a40bceb3ba2 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Theatre Case Progression |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS.FN-MANAGE-THEATRE-CASE-PROGRESSION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-THEATRE-OPERATIONS.FN-MANAGE-THEATRE-CASE-PROGRESSION"] |

Responsibility/framing context: [Theatre Operations](business-enabling-capabilities.md#hcm-bec-theatre-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-theatre-case-progression`) and the element's version-qualified Properties.

<a id="hcm-fn-assemble-ward-handover-context"></a>

## Assemble Ward Handover Context

Collates current ward patient lists, active medical concerns and pending tasks for nursing shift handover.

Derivation: FEAT-HSO-04 directly requires this assembly. Ward acuity/cohorting telemetry alone does not provide the stated handover content. Source clinical information retains its originating authority; assembly does not perform clinical review, manage clinical handover, determine pending clinical work or acquire clinical judgement.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:assemble-ward-handover-context |
| canonicalId | 6006d70ed3794c57ab2bc57119292e4b |
| displayName | Assemble Ward Handover Context |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.AssembleWardHandoverContext |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.FN-AssembleWardHandoverContext |
| Snapshot publication reference | HCM-FN-assemble-ward-handover-context |
| ArchiMate semantic ID | id-52dfd140ab2f52fbb4d10aee0e015f32 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Assemble Ward Handover Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.FN-ASSEMBLE-WARD-HANDOVER-CONTEXT |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-WARD-OPERATIONS.FN-ASSEMBLE-WARD-HANDOVER-CONTEXT"] |

Responsibility/framing context: [Ward Operations](business-enabling-capabilities.md#hcm-bec-ward-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:assemble-ward-handover-context`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-ward-capacity"></a>

## Manage Ward Capacity

Coordinates available, committed and utilised ward capacity and its patient-flow implications, retaining acuity distribution, nurse-to-patient ratios and isolation cohorting alongside bed/care-place state, patient demand and operational constraints.

Capacity boundary: Bed & Care-Place Management contributes resource state. Ward capacity is assessed in its service context; an available bed alone does not establish capacity to accept a patient. Source staffing and clinical assertions retain their authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-ward-capacity |
| canonicalId | eca5f4fbf92743f987a01930dc4ee763 |
| displayName | Manage Ward Capacity |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.ManageWardCapacity |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.FN-ManageWardCapacity |
| Snapshot publication reference | HCM-FN-manage-ward-capacity |
| ArchiMate semantic ID | id-039fbc0a40a05857a1912f3cdd9f501e |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Ward Capacity |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.FN-MANAGE-WARD-CAPACITY |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-WARD-OPERATIONS.FN-MANAGE-WARD-CAPACITY"] |
| Name aliases | ["Coordinate Ward Operational State"] |

Responsibility/framing context: [Ward Operations](business-enabling-capabilities.md#hcm-bec-ward-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-ward-capacity`) and the element's version-qualified Properties.

<a id="hcm-fn-assemble-inpatient-discharge-dossier"></a>

## Assemble Inpatient Discharge Dossier

Synthesises inpatient diagnoses, procedural summaries, reconciled discharge medications, and follow-up instructions into a canonical discharge summary package.

Feature association not established: FEAT-SD-08 — Inpatient Clinical Progression Tracking is adjudicated sufficient through general clinical-information responsibilities above. Discharge-dossier assembly/publication remains distinct; no separate clinical-tracking Function or replacement association is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:assemble-inpatient-discharge-dossier |
| canonicalId | eeefe77a935a46aface61aca4e2bcfb6 |
| displayName | Assemble Inpatient Discharge Dossier |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.MultidisciplinaryInpatientCareCoordination.AssembleInpatientDischargeDossier |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-MultidisciplinaryInpatientCareCoordination.FN-AssembleInpatientDischargeDossier |
| Snapshot publication reference | HCM-FN-assemble-inpatient-discharge-dossier |
| ArchiMate semantic ID | id-11cb04e40d4c5591989ee2b08c66c3a9 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Assemble Inpatient Discharge Dossier |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-INPATIENT-CARE.FN-ASSEMBLE-INPATIENT-DISCHARGE-DOSSIER |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-INPATIENT-CARE.FN-ASSEMBLE-INPATIENT-DISCHARGE-DOSSIER"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Multidisciplinary Inpatient Care Coordination](business-enabling-capabilities.md#hcm-bec-inpatient-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:assemble-inpatient-discharge-dossier`) and the element's version-qualified Properties.

<a id="hcm-fn-consolidate-inpatient-clinical-round-feed"></a>

## Consolidate Inpatient Clinical Round Feed

Aggregates daily vital signs, overnight pathology, allied health notes, and medication administration records.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:consolidate-inpatient-clinical-round-feed |
| canonicalId | 76eb51b9cc6a4417bbc8491830e43ad1 |
| displayName | Consolidate Inpatient Clinical Round Feed |
| friendlyName | HealthServiceOperationsEnablement.WardOperations.MultidisciplinaryInpatientCareCoordination.ConsolidateInpatientClinicalRoundFeed |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WardOperations.CT3-BEC-MultidisciplinaryInpatientCareCoordination.FN-ConsolidateInpatientClinicalRoundFeed |
| Snapshot publication reference | HCM-FN-consolidate-inpatient-clinical-round-feed |
| ArchiMate semantic ID | id-c0c6dd6651ad548f9f5c40211cf2a1c0 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Consolidate Inpatient Clinical Round Feed |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-INPATIENT-CARE.FN-CONSOLIDATE-INPATIENT-CLINICAL-ROUND-FEED |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION.CT3-BEC-INPATIENT-CARE.FN-CONSOLIDATE-INPATIENT-CLINICAL-ROUND-FEED"] |

Responsibility/framing context: [Multidisciplinary Inpatient Care Coordination](business-enabling-capabilities.md#hcm-bec-inpatient-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:consolidate-inpatient-clinical-round-feed`) and the element's version-qualified Properties.

<a id="hcm-fn-dispatch-operational-work-order"></a>

## Dispatch Operational Work Order

Allocates work requests to available wardspersons or support teams using applicable allocation policies, staff roles, credentials, location and skill, and delivers allocated work to the target worker, device or team queue.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:dispatch-operational-work-order |
| canonicalId | 05a14489d3e943499581620356130007 |
| displayName | Dispatch Operational Work Order |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch.DispatchOperationalWorkOrder |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch.FN-DispatchOperationalWorkOrder |
| Snapshot publication reference | HCM-FN-dispatch-operational-work-order |
| ArchiMate semantic ID | id-a2e96ba6d8a257be811ab0399ef6f1db |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Dispatch Operational Work Order |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.FN-DISPATCH-OPERATIONAL-WORK-ORDER |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-WORK-ALLOCATION-DISPATCH.FN-DISPATCH-OPERATIONAL-WORK-ORDER"] |

Responsibility/framing context: [Work Allocation & Dispatch](business-enabling-capabilities.md#hcm-bec-work-allocation-dispatch). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:dispatch-operational-work-order`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-operational-work-progression"></a>

## Manage Operational Work Progression

Tracks execution states: Queued → Assigned → Accepted → In-Progress → Completed / Aborted.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-operational-work-progression |
| canonicalId | ebc35951617a4f92863922b74dd1c1f4 |
| displayName | Manage Operational Work Progression |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch.ManageOperationalWorkProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch.FN-ManageOperationalWorkProgression |
| Snapshot publication reference | HCM-FN-manage-operational-work-progression |
| ArchiMate semantic ID | id-d90e5f9a69c3522ebcf83c48e8769589 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Operational Work Progression |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.FN-MANAGE-OPERATIONAL-WORK-PROGRESSION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-WORK-ALLOCATION-DISPATCH.FN-MANAGE-OPERATIONAL-WORK-PROGRESSION"] |

Responsibility/framing context: [Work Allocation & Dispatch](business-enabling-capabilities.md#hcm-bec-work-allocation-dispatch). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-operational-work-progression`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-assurance-criteria"></a>

## Manage Assurance Criteria

•
Canonical Name: Manage Assurance Criteria.

•
Definition: Establishes and manages reusable, governed and temporally identifiable assurance criteria through which governed subjects may be evaluated.

•
Scope: Criteria may express governance rule boundaries, quality constraints, performance expectations, compliance criteria, temporal criteria, evidence requirements or completeness expectations. These examples are not an exhaustive taxonomy. Criteria may have lifecycle, version and temporal applicability.

•
Boundary: Managing a criterion does not transfer authority for the governing requirement from which it may derive. Criteria may operationalise a requirement without silently altering, weakening, strengthening or replacing it; not every criterion must derive from formal Governance. Modelling/managing criteria does not inherently confer approval authority. Detailed criterion lifecycle states and approval workflows remain unresolved.

Criteria management maintains the reusable evaluative basis and its temporal standing. It permits an assessment to be understood against the criterion applicable at the relevant time, rather than today's unqualified version. Management of that basis does not inherently grant approval authority or permit alteration of the governing obligation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-assurance-criteria |
| canonicalId | d7e9ba5addd94db2bce164c7c9809e4e |
| displayName | Manage Assurance Criteria |
| friendlyName | IndependentAssuranceEnablement.AssuranceSpecification.AssuranceCriteriaAdministration.ManageAssuranceCriteria |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-AssuranceSpecification.CT3-BEC-AssuranceCriteriaAdministration.FN-ManageAssuranceCriteria |
| Snapshot publication reference | HCM-FN-manage-assurance-criteria |
| ArchiMate semantic ID | id-a80591b425a55e9c92ed5b400f1219d6 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Assurance Criteria |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT.CT3-BEC-ASSURANCE-CRITERIA-MANAGEMENT.FN-MANAGE-ASSURANCE-CRITERIA |
| Unresolved dimensions | ["feature_association","contextual_view"] |

Responsibility/framing context: [Assurance Criteria Administration](business-enabling-capabilities.md#hcm-bec-assurance-criteria-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-assurance-criteria`) and the element's version-qualified Properties.

<a id="hcm-fn-design-service-assurance"></a>

## Design Service Assurance

•
Canonical Name: Design Service Assurance.

•
Definition: Defines how satisfaction of a governed requirement, constraint or expected behaviour is to be independently established, including the applicable assurance disposition, assurance criteria, required evidence and evaluation expectations.

•
Boundary: Concerns assurance design. It does not establish the underlying governance requirement, grant authority to it, perform the resulting assurance, manage the subject activity or prescribe application or implementation mechanisms. Modelling the resulting Assurance Definition does not grant approval authority. The Strategy peer relationship to behaviour design and failure/recovery design creates no additional Capability or Function.

Designing service assurance turns a requirement's assurance question into an explicit disposition and evaluation basis. The modeller determines how satisfaction is to be assessed, not what the authoritative requirement should mean or who may approve it. Design completion is not evidence that the implemented behaviour satisfies that requirement.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:design-service-assurance |
| canonicalId | 6668aa8daf964fec93fce42d48b37ad1 |
| displayName | Design Service Assurance |
| friendlyName | IndependentAssuranceEnablement.AssuranceSpecification.AssuranceDesign.DesignServiceAssurance |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-AssuranceSpecification.CT3-BEC-AssuranceDesign.FN-DesignServiceAssurance |
| Snapshot publication reference | HCM-FN-design-service-assurance |
| ArchiMate semantic ID | id-d0f38c617e795e8090b07c5f563143c5 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Design Service Assurance |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT.CT3-BEC-ASSURANCE-DESIGN.FN-DESIGN-SERVICE-ASSURANCE |
| Unresolved dimensions | ["feature_association","contextual_view"] |

Responsibility/framing context: [Assurance Design](business-enabling-capabilities.md#hcm-bec-assurance-design). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:design-service-assurance`) and the element's version-qualified Properties.

<a id="hcm-fn-correlate-activity-with-temporal-context"></a>

## Correlate Activity with Temporal Context

Associates clinical events with shift boundaries and appointment slots.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:correlate-activity-with-temporal-context |
| canonicalId | b45fa964568843398661cc119b4fc693 |
| displayName | Correlate Activity with Temporal Context |
| friendlyName | SharedInformationAndCoordinationEnablement.CalendarManagement.CorrelateActivityWithTemporalContext |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-CalendarManagement.FN-CorrelateActivityWithTemporalContext |
| Snapshot publication reference | HCM-FN-correlate-activity-with-temporal-context |
| ArchiMate semantic ID | id-1b4d7516a80a5b818e8154de9e21dd8a |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Correlate Activity with Temporal Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CALENDAR-MANAGEMENT.FN-CORRELATE-ACTIVITY-WITH-TEMPORAL-CONTEXT |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-CALENDAR-MANAGEMENT.FN-CORRELATE-ACTIVITY-WITH-TEMPORAL-CONTEXT"] |

Responsibility/framing context: [Calendar Management](business-enabling-capabilities.md#hcm-bec-calendar-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:correlate-activity-with-temporal-context`) and the element's version-qualified Properties.

<a id="hcm-fn-project-operational-schedule"></a>

## Project Operational Schedule

Aggregates and overlays multidisciplinary schedules onto a unified temporal view.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:project-operational-schedule |
| canonicalId | 3ca2e6a5ca614df991a2e1a060f0f1d8 |
| displayName | Project Operational Schedule |
| friendlyName | SharedInformationAndCoordinationEnablement.CalendarManagement.ProjectOperationalSchedule |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-CalendarManagement.FN-ProjectOperationalSchedule |
| Snapshot publication reference | HCM-FN-project-operational-schedule |
| ArchiMate semantic ID | id-7ce9c591e5ae50d4aa57e14968fc42ac |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Project Operational Schedule |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CALENDAR-MANAGEMENT.FN-PROJECT-OPERATIONAL-SCHEDULE |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-CALENDAR-MANAGEMENT.FN-PROJECT-OPERATIONAL-SCHEDULE"] |

Responsibility/framing context: [Calendar Management](business-enabling-capabilities.md#hcm-bec-calendar-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:project-operational-schedule`) and the element's version-qualified Properties.

<a id="hcm-fn-coordinate-care-team-collaboration"></a>

## Coordinate Care-Team Collaboration

Manages care-team communication spaces, participant memberships, and clinical handovers.

This Function maintains clinical collaboration spaces and participant context, enabling team discourse around care. The governed source also says it manages clinical handovers, which conflicts with the metamodel's explicit exclusion of that authority. The conflict is preserved for adjudication; this explanatory catalogue does not resolve it by quietly deleting either statement.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:coordinate-care-team-collaboration |
| canonicalId | 5443b253f0e4425f984702d396e77a3d |
| displayName | Coordinate Care-Team Collaboration |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.CoordinateCareTeamCollaboration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.FN-CoordinateCareTeamCollaboration |
| Snapshot publication reference | HCM-FN-coordinate-care-team-collaboration |
| ArchiMate semantic ID | id-bb2d752d0fb452b3b182b48fa4ce1471 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Coordinate Care-Team Collaboration |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.FN-COORDINATE-CARE-TEAM-COLLABORATION |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION.FN-COORDINATE-CARE-TEAM-COLLABORATION"] |
| Unresolved dimensions | ["semantic_boundary_conflict"] |

Responsibility/framing context: [Clinical Collaboration](business-enabling-capabilities.md#hcm-bec-clinical-collaboration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:coordinate-care-team-collaboration`) and the element's version-qualified Properties.

<a id="hcm-fn-govern-collaboration-metadata"></a>

## Govern Collaboration Metadata

Enforces privacy policies, participant access gates, and zero-PHI space metadata rules.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:govern-collaboration-metadata |
| canonicalId | 94a92326ef2141efb414c4602d878caa |
| displayName | Govern Collaboration Metadata |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.GovernCollaborationMetadata |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.FN-GovernCollaborationMetadata |
| Snapshot publication reference | HCM-FN-govern-collaboration-metadata |
| ArchiMate semantic ID | id-e7562f25b9305c62b7ab167d10f0f23d |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Govern Collaboration Metadata |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.FN-GOVERN-COLLABORATION-METADATA |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION.FN-GOVERN-COLLABORATION-METADATA"] |

Responsibility/framing context: [Clinical Collaboration](business-enabling-capabilities.md#hcm-bec-clinical-collaboration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:govern-collaboration-metadata`) and the element's version-qualified Properties.

<a id="hcm-fn-resolve-lhr-query-within-collaboration"></a>

## Resolve LHR Query within Collaboration

Contextually projects active patient summaries directly into collaborative clinical discussion channels.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:resolve-lhr-query-within-collaboration |
| canonicalId | 1d4ac561d963484480e6b06a09416616 |
| displayName | Resolve LHR Query within Collaboration |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalCollaboration.ResolveLhrQueryWithinCollaboration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalCollaboration.FN-ResolveLhrQueryWithinCollaboration |
| Snapshot publication reference | HCM-FN-resolve-lhr-query-within-collaboration |
| ArchiMate semantic ID | id-0a001a4369305718abbfb3ab2556599f |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Resolve LHR Query within Collaboration |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.FN-RESOLVE-LHR-QUERY-WITHIN-COLLABORATION |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-CLINICAL-COLLABORATION.FN-RESOLVE-LHR-QUERY-WITHIN-COLLABORATION"] |

Responsibility/framing context: [Clinical Collaboration](business-enabling-capabilities.md#hcm-bec-clinical-collaboration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:resolve-lhr-query-within-collaboration`) and the element's version-qualified Properties.

<a id="hcm-fn-map-clinical-concept"></a>

## Map Clinical Concept

Translates local lab codes and legacy terms to canonical SNOMED/LOINC codes using governed translation maps.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:map-clinical-concept |
| canonicalId | 9549208d56cd47d8a9ba4e586323ef3b |
| displayName | Map Clinical Concept |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.MapClinicalConcept |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.FN-MapClinicalConcept |
| Snapshot publication reference | HCM-FN-map-clinical-concept |
| ArchiMate semantic ID | id-1f609a1e39a453f4857be7ec7e28b4ab |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Map Clinical Concept |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.FN-MAP-CLINICAL-CONCEPT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-CLINICAL-KNOWLEDGE-SERVICES.FN-MAP-CLINICAL-CONCEPT"] |

Responsibility/framing context: [Clinical Knowledge Services](business-enabling-capabilities.md#hcm-bec-clinical-knowledge-services). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:map-clinical-concept`) and the element's version-qualified Properties.

<a id="hcm-fn-resolve-clinical-concept"></a>

## Resolve Clinical Concept

Validates concept identifiers and retrieves preferred clinical rubrics and hierarchy paths.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:resolve-clinical-concept |
| canonicalId | 8f89eacbc53b4697b5a747db2e143510 |
| displayName | Resolve Clinical Concept |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.ResolveClinicalConcept |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.FN-ResolveClinicalConcept |
| Snapshot publication reference | HCM-FN-resolve-clinical-concept |
| ArchiMate semantic ID | id-198d724291145ffc8078052f07ed718f |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Resolve Clinical Concept |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.FN-RESOLVE-CLINICAL-CONCEPT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-CLINICAL-KNOWLEDGE-SERVICES.FN-RESOLVE-CLINICAL-CONCEPT"] |

Responsibility/framing context: [Clinical Knowledge Services](business-enabling-capabilities.md#hcm-bec-clinical-knowledge-services). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:resolve-clinical-concept`) and the element's version-qualified Properties.

<a id="hcm-fn-accept-governed-dataset-revision"></a>

## Accept Governed Dataset Revision

Apply an explicitly authorised content revision and its effective standing through governed information-state progression.

**Overview**

Moves a reviewed change to the applicable approved content revision, retaining attribution and uncertainty if durable establishment cannot be confirmed.

**Boundary**

The Function does not invent who approves or become a persistence manager.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-accept-governed-dataset-revision |
| canonicalId | 96c1c089e7f2443dbd3ad5beab6a3011 |
| displayName | Accept Governed Dataset Revision |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalKnowledgeServices.GovernedReferenceDatasetAdministration.AcceptGovernedDatasetRevision |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalKnowledgeServices.CT3-BEC-GovernedReferenceDatasetAdministration.FN-AcceptGovernedDatasetRevision |
| Snapshot publication reference | HCM-FN-accept-governed-dataset-revision |
| ArchiMate semantic ID | id-9b80dfad3498500983d9e0a9c0de1617 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.FN-ACCEPT-GOVERNED-DATASET-REVISION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.FN-ACCEPT-GOVERNED-DATASET-REVISION"] |

Responsibility/framing context: [Governed Reference Dataset Administration](business-enabling-capabilities.md#hcm-bec-governed-reference-dataset-administration). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-accept-governed-dataset-revision`) and the element's version-qualified Properties.

<a id="hcm-fn-coordinate-operational-collaboration"></a>

## Coordinate Operational Collaboration

Provides communication spaces for facility logistics, disaster management teams, and administrative committees.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:coordinate-operational-collaboration |
| canonicalId | 3b5604d966504661af2cb6654ae0de5c |
| displayName | Coordinate Operational Collaboration |
| friendlyName | SharedInformationAndCoordinationEnablement.GeneralCollaboration.CoordinateOperationalCollaboration |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-GeneralCollaboration.FN-CoordinateOperationalCollaboration |
| Snapshot publication reference | HCM-FN-coordinate-operational-collaboration |
| ArchiMate semantic ID | id-d99143445f7656609a7f6eca370d25d0 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Coordinate Operational Collaboration |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-GENERAL-COLLABORATION.FN-COORDINATE-OPERATIONAL-COLLABORATION |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-GENERAL-COLLABORATION.FN-COORDINATE-OPERATIONAL-COLLABORATION"] |

Responsibility/framing context: [General Collaboration](business-enabling-capabilities.md#hcm-bec-general-collaboration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:coordinate-operational-collaboration`) and the element's version-qualified Properties.

<a id="hcm-fn-apply-access-qualified-result-filtering"></a>

## Apply Access-Qualified Result Filtering

Redacts or masks search results and clinical content based on the caller's verified security context and patient consent directives.

Result filtering changes what can be disclosed according to the verified context and applicable consent determinations. It applies established control outcomes without owning their policy. Its capability-scoped standing is retained instead of promoting the behaviour to a Feature simply to obtain traceability symmetry.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:apply-access-qualified-result-filtering |
| canonicalId | 598e46ab52644e70809b3039b7a4c21e |
| displayName | Apply Access-Qualified Result Filtering |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationAccess.ApplyAccessQualifiedResultFiltering |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationAccess.FN-ApplyAccessQualifiedResultFiltering |
| Snapshot publication reference | HCM-FN-apply-access-qualified-result-filtering |
| ArchiMate semantic ID | id-aa04e0e066415f058c57be4f97dab41e |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Apply Access-Qualified Result Filtering |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-ACCESS.FN-APPLY-ACCESS-QUALIFIED-RESULT-FILTERING |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-ACCESS.FN-APPLY-ACCESS-QUALIFIED-RESULT-FILTERING"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Health Information Access](business-enabling-capabilities.md#hcm-bec-health-information-access). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:apply-access-qualified-result-filtering`) and the element's version-qualified Properties.

<a id="hcm-fn-retrieve-health-information"></a>

## Retrieve Health Information

Fetches discrete clinical observations and original attachments.

Feature association not established: FEAT-ISE-09 — Access-Controlled Information Filtering requires access-controlled disclosure/filtering; retrieval of discrete information alone does not materially realise that responsibility. No replacement Feature is assigned.

Retrieval obtains discrete clinical information or attachments under the owning access responsibility. It is distinct from applying the access-qualified filtering required by FEAT-ISE-09. A shared owner and related data do not justify making that unresolved realisation association authoritative.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:retrieve-health-information |
| canonicalId | 0afb265973744f6f9e95ee54e6ea5b77 |
| displayName | Retrieve Health Information |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationAccess.RetrieveHealthInformation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationAccess.FN-RetrieveHealthInformation |
| Snapshot publication reference | HCM-FN-retrieve-health-information |
| ArchiMate semantic ID | id-b3bf36bb09e9570e8d8ccca1f170a022 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Retrieve Health Information |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-ACCESS.FN-RETRIEVE-HEALTH-INFORMATION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-ACCESS.FN-RETRIEVE-HEALTH-INFORMATION"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Health Information Access](business-enabling-capabilities.md#hcm-bec-health-information-access). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:retrieve-health-information`) and the element's version-qualified Properties.

<a id="hcm-fn-search-health-information"></a>

## Search Health Information

Executes structured semantic searches across diagnostic reports, clinical notes, and medication histories.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:search-health-information |
| canonicalId | 2e75f678b9814305b1cb2e810f4f0e9e |
| displayName | Search Health Information |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationAccess.SearchHealthInformation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationAccess.FN-SearchHealthInformation |
| Snapshot publication reference | HCM-FN-search-health-information |
| ArchiMate semantic ID | id-a9f59f7fb36b53828e6f0220d687943b |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Search Health Information |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-ACCESS.FN-SEARCH-HEALTH-INFORMATION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-ACCESS.FN-SEARCH-HEALTH-INFORMATION"] |

Responsibility/framing context: [Health Information Access](business-enabling-capabilities.md#hcm-bec-health-information-access). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:search-health-information`) and the element's version-qualified Properties.

<a id="hcm-fn-evaluate-consent-constraint"></a>

## Evaluate Consent Constraint

Cross-references access requests against client consent directives and sensitive health category restrictions.

Feature association not established: FEAT-ISE-12 — Security Context Propagation propagates attributable security context; evaluating consent constraints does not materially realise that responsibility. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:evaluate-consent-constraint |
| canonicalId | 4a881488296d43d09112cb046fa836d3 |
| displayName | Evaluate Consent Constraint |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.EvaluateConsentConstraint |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.FN-EvaluateConsentConstraint |
| Snapshot publication reference | HCM-FN-evaluate-consent-constraint |
| ArchiMate semantic ID | id-e841aba187555d949bb6615cbabcbec9 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Evaluate Consent Constraint |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.FN-EVALUATE-CONSENT-CONSTRAINT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FN-EVALUATE-CONSENT-CONSTRAINT"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:evaluate-consent-constraint`) and the element's version-qualified Properties.

<a id="hcm-fn-evaluate-information-access-authority"></a>

## Evaluate Information Access Authority

Evaluates user role, organisation, patient relationship, purpose of use, and break-glass overrides against default-deny policies.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:evaluate-information-access-authority |
| canonicalId | 4707b7fc77254ea68c30fc89f5119a77 |
| displayName | Evaluate Information Access Authority |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.EvaluateInformationAccessAuthority |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.FN-EvaluateInformationAccessAuthority |
| Snapshot publication reference | HCM-FN-evaluate-information-access-authority |
| ArchiMate semantic ID | id-78101993be9c5482a2b445ab48523844 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Evaluate Information Access Authority |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.FN-EVALUATE-INFORMATION-ACCESS-AUTHORITY |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FN-EVALUATE-INFORMATION-ACCESS-AUTHORITY"] |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:evaluate-information-access-authority`) and the element's version-qualified Properties.

<a id="hcm-fn-propagate-security-context"></a>

## Propagate Security Context

Generates and binds canonical, immutable security contexts to all intra-platform transactions.

Exposure qualification: Validation is the declared Service behaviour. Its juxtaposition with Propagate Security Context does not establish a one-to-one Function/Service relationship; the precise internal mapping remains unestablished.

Propagation preserves attributable operational security context across managed transactions. It provides continuity of context rather than independently deciding permission for every downstream action. The nearby validation Service has its own declared exposure; juxtaposition does not establish an exact one-to-one implementation relationship.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:propagate-security-context |
| canonicalId | ee79679c6b5041659a2efca15190454f |
| displayName | Propagate Security Context |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.PropagateSecurityContext |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.FN-PropagateSecurityContext |
| Snapshot publication reference | HCM-FN-propagate-security-context |
| ArchiMate semantic ID | id-fad39b654ac4567798905571648a43d9 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Propagate Security Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.FN-PROPAGATE-SECURITY-CONTEXT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FN-PROPAGATE-SECURITY-CONTEXT"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:propagate-security-context`) and the element's version-qualified Properties.

<a id="hcm-fn-record-security-significant-activity"></a>

## Record Security-Significant Activity

Records tamper-evident audit evidence (who, what, when, why, patient MRN, policy outcome) without logging unmasked PHI.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:record-security-significant-activity |
| canonicalId | 6177ed0d53e5407ab1bc4b721619ed44 |
| displayName | Record Security-Significant Activity |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationControl.RecordSecuritySignificantActivity |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationControl.FN-RecordSecuritySignificantActivity |
| Snapshot publication reference | HCM-FN-record-security-significant-activity |
| ArchiMate semantic ID | id-ed8d7e8853d75dd7b9a8791dbe57acf6 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Record Security-Significant Activity |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.FN-RECORD-SECURITY-SIGNIFICANT-ACTIVITY |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FN-RECORD-SECURITY-SIGNIFICANT-ACTIVITY"] |

Responsibility/framing context: [Health Information Control](business-enabling-capabilities.md#hcm-bec-health-information-control). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:record-security-significant-activity`) and the element's version-qualified Properties.

<a id="hcm-fn-distribute-addressed-information"></a>

## Distribute Addressed Information

Directs point-to-point clinical documents to specific recipient practitioner endpoints.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:distribute-addressed-information |
| canonicalId | 6c9090a3d0a74a968475e05eb617e84f |
| displayName | Distribute Addressed Information |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.DistributeAddressedInformation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.FN-DistributeAddressedInformation |
| Snapshot publication reference | HCM-FN-distribute-addressed-information |
| ArchiMate semantic ID | id-ebc65d213968582a80f79dc63f4ed66d |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Distribute Addressed Information |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FN-DISTRIBUTE-ADDRESSED-INFORMATION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FN-DISTRIBUTE-ADDRESSED-INFORMATION"] |

Responsibility/framing context: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:distribute-addressed-information`) and the element's version-qualified Properties.

<a id="hcm-fn-mediate-information-retrieval"></a>

## Mediate Information Retrieval

Federates queries across participating hospital and diagnostic node repositories.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:mediate-information-retrieval |
| canonicalId | 7667abb329724b7d894fca020acb8014 |
| displayName | Mediate Information Retrieval |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.MediateInformationRetrieval |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.FN-MediateInformationRetrieval |
| Snapshot publication reference | HCM-FN-mediate-information-retrieval |
| ArchiMate semantic ID | id-25aa16277b895b2da124a32ea93fb756 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Mediate Information Retrieval |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FN-MEDIATE-INFORMATION-RETRIEVAL |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FN-MEDIATE-INFORMATION-RETRIEVAL"] |

Responsibility/framing context: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:mediate-information-retrieval`) and the element's version-qualified Properties.

<a id="hcm-fn-receive-information-submission"></a>

## Receive Information Submission

Validates and ingests standards-compliant clinical messages from external health providers.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:receive-information-submission |
| canonicalId | 26abb23ed9f54dcf9002eb894f1d02ec |
| displayName | Receive Information Submission |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.ReceiveInformationSubmission |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.FN-ReceiveInformationSubmission |
| Snapshot publication reference | HCM-FN-receive-information-submission |
| ArchiMate semantic ID | id-9725b3758a085a2ca7773450ca65f015 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Receive Information Submission |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FN-RECEIVE-INFORMATION-SUBMISSION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FN-RECEIVE-INFORMATION-SUBMISSION"] |

Responsibility/framing context: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:receive-information-submission`) and the element's version-qualified Properties.

<a id="hcm-fn-syndicate-information-change"></a>

## Syndicate Information Change

Broadcasts event notifications and record updates to subscribed regional care network systems.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:syndicate-information-change |
| canonicalId | 86241540124a40458f7d1dc2f5df84ca |
| displayName | Syndicate Information Change |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationExchangeEnablement.SyndicateInformationChange |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationExchangeEnablement.FN-SyndicateInformationChange |
| Snapshot publication reference | HCM-FN-syndicate-information-change |
| ArchiMate semantic ID | id-5012e7d49fc35c1ab3bef1602e72a4bc |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Syndicate Information Change |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FN-SYNDICATE-INFORMATION-CHANGE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.FN-SYNDICATE-INFORMATION-CHANGE"] |

Responsibility/framing context: [Health Information Exchange Enablement](business-enabling-capabilities.md#hcm-bec-health-information-exchange). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:syndicate-information-change`) and the element's version-qualified Properties.

<a id="hcm-fn-record-significant-information-lineage"></a>

## Record Significant Information Lineage

Associate a managed assertion with its attributed sources and information-significant transformation.

**Overview**

Changes an unexplained derived representation into an attributable relationship to the source information and transformation basis.

**Boundary**

Routine retries, cache mechanics and debug traces are excluded unless information-significant.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-record-significant-information-lineage |
| canonicalId | afdb0358393f4560b926330a14c1aedd |
| displayName | Record Significant Information Lineage |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationContentMaintainerManagement.DataProvenanceCapture.RecordSignificantInformationLineage |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationContentMaintainerManagement.CT3-BEC-DataProvenanceCapture.FN-RecordSignificantInformationLineage |
| Snapshot publication reference | HCM-FN-record-significant-information-lineage |
| ArchiMate semantic ID | id-8b8ca4a2198d57a58bec97c3a0370483 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-LINEAGE-STEWARDSHIP.FN-RECORD-SIGNIFICANT-INFORMATION-LINEAGE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-INFORMATION-LINEAGE-STEWARDSHIP.FN-RECORD-SIGNIFICANT-INFORMATION-LINEAGE"] |

Responsibility/framing context: [Data Provenance Capture](business-enabling-capabilities.md#hcm-bec-information-lineage-stewardship). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-record-significant-information-lineage`) and the element's version-qualified Properties.

<a id="hcm-fn-govern-exchange-profile"></a>

## Govern Exchange Profile

Defines standards-based interchange profiles and message constraints.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:govern-exchange-profile |
| canonicalId | 935f9a62438944599b913f2614c9e595 |
| displayName | Govern Exchange Profile |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.GovernExchangeProfile |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.FN-GovernExchangeProfile |
| Snapshot publication reference | HCM-FN-govern-exchange-profile |
| ArchiMate semantic ID | id-17d9c364bdcf56d3bca3923d6308f312 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Govern Exchange Profile |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.FN-GOVERN-EXCHANGE-PROFILE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.FN-GOVERN-EXCHANGE-PROFILE"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:govern-exchange-profile`) and the element's version-qualified Properties.

<a id="hcm-fn-govern-information-definition"></a>

## Govern Information Definition

Maintains governed data element dictionaries, data types, and structural rules.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:govern-information-definition |
| canonicalId | 820b4d41490a4389bb864f1c4a04e881 |
| displayName | Govern Information Definition |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.GovernInformationDefinition |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.FN-GovernInformationDefinition |
| Snapshot publication reference | HCM-FN-govern-information-definition |
| ArchiMate semantic ID | id-1384dec284575da38d1619b4551a8333 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Govern Information Definition |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.FN-GOVERN-INFORMATION-DEFINITION |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.FN-GOVERN-INFORMATION-DEFINITION"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:govern-information-definition`) and the element's version-qualified Properties.

<a id="hcm-fn-govern-information-semantics"></a>

## Govern Information Semantics

Authors and validates canonical data concepts and entity relationships.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:govern-information-semantics |
| canonicalId | 39a297b6c40c44ecba64857f6c3f3e0f |
| displayName | Govern Information Semantics |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.GovernInformationSemantics |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.FN-GovernInformationSemantics |
| Snapshot publication reference | HCM-FN-govern-information-semantics |
| ArchiMate semantic ID | id-e2fdaca5a5935071ac58f035c56c0104 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Govern Information Semantics |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.FN-GOVERN-INFORMATION-SEMANTICS |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.FN-GOVERN-INFORMATION-SEMANTICS"] |

Responsibility/framing context: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:govern-information-semantics`) and the element's version-qualified Properties.

<a id="hcm-fn-govern-terminology-binding"></a>

## Govern Terminology Binding

Binds canonical data elements to authoritative terminology value sets.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:govern-terminology-binding |
| canonicalId | 9e96ce12b7494594a6129f79f7491223 |
| displayName | Govern Terminology Binding |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.GovernTerminologyBinding |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.FN-GovernTerminologyBinding |
| Snapshot publication reference | HCM-FN-govern-terminology-binding |
| ArchiMate semantic ID | id-7f0794231e2f508e9abcae75e1cb9cf8 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Govern Terminology Binding |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.FN-GOVERN-TERMINOLOGY-BINDING |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.FN-GOVERN-TERMINOLOGY-BINDING"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:govern-terminology-binding`) and the element's version-qualified Properties.

<a id="hcm-fn-verify-semantic-conformance"></a>

## Verify Semantic Conformance

Validates candidate messages against canonical governance schemas.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:verify-semantic-conformance |
| canonicalId | 5445184252c94c05874c52350ff13d2b |
| displayName | Verify Semantic Conformance |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationDesignGovernance.VerifySemanticConformance |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationDesignGovernance.FN-VerifySemanticConformance |
| Snapshot publication reference | HCM-FN-verify-semantic-conformance |
| ArchiMate semantic ID | id-cd9c73b52e985af797037372419aa03b |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Verify Semantic Conformance |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.FN-VERIFY-SEMANTIC-CONFORMANCE |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT.CT3-BEC-INFORMATION-DESIGN-GOVERNANCE.FN-VERIFY-SEMANTIC-CONFORMANCE"] |

Responsibility/framing context: [Information Design Governance](business-enabling-capabilities.md#hcm-bec-information-design-governance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:verify-semantic-conformance`) and the element's version-qualified Properties.

<a id="hcm-fn-assemble-longitudinal-clinical-record"></a>

## Assemble Longitudinal Clinical Record

Synthesises clinical documents, encounters, diagnostic reports, and medication histories across disparate source systems into a unified chronological clinical view.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:assemble-longitudinal-clinical-record |
| canonicalId | 65ea2d2427dd474d84f153671de79d63 |
| displayName | Assemble Longitudinal Clinical Record |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.AssembleLongitudinalClinicalRecord |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.FN-AssembleLongitudinalClinicalRecord |
| Snapshot publication reference | HCM-FN-assemble-longitudinal-clinical-record |
| ArchiMate semantic ID | id-3c16bbde87bc5e2b9b4ccf9fe39bc9dc |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Assemble Longitudinal Clinical Record |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.FN-ASSEMBLE-LONGITUDINAL-CLINICAL-RECORD |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD.FN-ASSEMBLE-LONGITUDINAL-CLINICAL-RECORD"] |

Responsibility/framing context: [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:assemble-longitudinal-clinical-record`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-active-clinical-record"></a>

## Maintain Active Clinical Record

Maintains active problem lists, current medication regimens, allergies, and open clinical alerts for active care coordination.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:maintain-active-clinical-record |
| canonicalId | dbccf500cf4a44e0987f972311bab03a |
| displayName | Maintain Active Clinical Record |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.MaintainActiveClinicalRecord |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.FN-MaintainActiveClinicalRecord |
| Snapshot publication reference | HCM-FN-maintain-active-clinical-record |
| ArchiMate semantic ID | id-568e9e477e6951b59d10392af761ec64 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Maintain Active Clinical Record |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.FN-MAINTAIN-ACTIVE-CLINICAL-RECORD |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD.FN-MAINTAIN-ACTIVE-CLINICAL-RECORD"] |

Responsibility/framing context: [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:maintain-active-clinical-record`) and the element's version-qualified Properties.

<a id="hcm-fn-preserve-durable-clinical-record"></a>

## Preserve Durable Clinical Record

Preserves the governed longitudinal representation and durable, immutable, append-only history of what Harmonia received, knew, asserted or managed, with provenance and versioning. Corrections/supersession establish changed knowledge without retrospectively altering that history or acquiring originating clinical authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:preserve-durable-clinical-record |
| canonicalId | ace0f4afad104482b7970534a45d74d7 |
| displayName | Preserve Durable Clinical Record |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.PreserveDurableClinicalRecord |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.FN-PreserveDurableClinicalRecord |
| Snapshot publication reference | HCM-FN-preserve-durable-clinical-record |
| ArchiMate semantic ID | id-1265240e650f5d3d969f58bba75a7a2f |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Preserve Durable Clinical Record |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.FN-PRESERVE-DURABLE-CLINICAL-RECORD |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-PATIENT-CLINICAL-RECORD.FN-PRESERVE-DURABLE-CLINICAL-RECORD"] |

Responsibility/framing context: [Patient Clinical Record](business-enabling-capabilities.md#hcm-bec-patient-clinical-record). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:preserve-durable-clinical-record`) and the element's version-qualified Properties.

<a id="hcm-fn-assemble-associated-binary-content"></a>

## Assemble Associated Binary Content

Retrieve and assemble the requested logical content from its permitted constituent payloads.

**Overview**

Moves a qualified retrieval request to an assembled result or explicit unavailable/partial standing.

**Boundary**

No storage topology or universal local custody is selected.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-assemble-associated-binary-content |
| canonicalId | 0a0a5f3e40d241e38a5b155ca66eadd5 |
| displayName | Assemble Associated Binary Content |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LinkedInformationCuration.AssembleAssociatedBinaryContent |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LinkedInformationCuration.FN-AssembleAssociatedBinaryContent |
| Snapshot publication reference | HCM-FN-assemble-associated-binary-content |
| ArchiMate semantic ID | id-6e0542071ced53d89091019e51e6a8e7 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.FN-ASSEMBLE-ASSOCIATED-BINARY-CONTENT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.FN-ASSEMBLE-ASSOCIATED-BINARY-CONTENT"] |

Responsibility/framing context: [Linked Information Curation](business-enabling-capabilities.md#hcm-bec-associated-clinical-binary-content-enablement). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-assemble-associated-binary-content`) and the element's version-qualified Properties.

<a id="hcm-fn-resolve-associated-binary-content"></a>

## Resolve Associated Binary Content

Resolve logical content associations to an authorised, custody-qualified retrieval basis.

**Overview**

Changes an unresolved content request into an identified retrieval basis or an explicit inability to resolve it.

**Boundary**

Does not assert payload existence, copy all content or establish clinical meaning.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-resolve-associated-binary-content |
| canonicalId | 118867dc136f4f689265b173abdf626b |
| displayName | Resolve Associated Binary Content |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.LinkedInformationCuration.ResolveAssociatedBinaryContent |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-LinkedInformationCuration.FN-ResolveAssociatedBinaryContent |
| Snapshot publication reference | HCM-FN-resolve-associated-binary-content |
| ArchiMate semantic ID | id-f58d2399c9b85386a82d7c300c228f58 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.FN-RESOLVE-ASSOCIATED-BINARY-CONTENT |
| Structural aliases | ["CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.FN-RESOLVE-ASSOCIATED-BINARY-CONTENT"] |

Responsibility/framing context: [Linked Information Curation](business-enabling-capabilities.md#hcm-bec-associated-clinical-binary-content-enablement). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-resolve-associated-binary-content`) and the element's version-qualified Properties.

<a id="hcm-fn-coordinate-multi-agency-chronic-care-plan"></a>

## Coordinate Multi-Agency Chronic Care Plan

Enables GPs to track multi-provider disease management milestones (e.g., Diabetes or Cardiovascular Care Plans).

Feature association not established: Strategy FEAT-SD-02 — Primary Care Notification Dispatch remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:coordinate-multi-agency-chronic-care-plan |
| canonicalId | fc49b716c2a845408198591e9a4dc515 |
| displayName | Coordinate Multi-Agency Chronic Care Plan |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.PrimaryCareCoordination.CoordinateMultiAgencyChronicCarePlan |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-PrimaryCareCoordination.FN-CoordinateMultiAgencyChronicCarePlan |
| Snapshot publication reference | HCM-FN-coordinate-multi-agency-chronic-care-plan |
| ArchiMate semantic ID | id-8df33f19e25f5f499cdec56502842ca6 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Coordinate Multi-Agency Chronic Care Plan |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-PRIMARY-CARE.FN-COORDINATE-MULTI-AGENCY-CHRONIC-CARE-PLAN |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PRIMARY-CARE.FN-COORDINATE-MULTI-AGENCY-CHRONIC-CARE-PLAN"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Primary Care Coordination](business-enabling-capabilities.md#hcm-bec-primary-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:coordinate-multi-agency-chronic-care-plan`) and the element's version-qualified Properties.

<a id="hcm-fn-correlate-primary-care-consultation-context"></a>

## Correlate Primary Care Consultation Context

Binds GP consultation notes, vital signs, and encounter episodes to the regional Longitudinal Health Record (LHR).

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:correlate-primary-care-consultation-context |
| canonicalId | e6c9088f502742afb538ee654ac0f962 |
| displayName | Correlate Primary Care Consultation Context |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.PrimaryCareCoordination.CorrelatePrimaryCareConsultationContext |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-PrimaryCareCoordination.FN-CorrelatePrimaryCareConsultationContext |
| Snapshot publication reference | HCM-FN-correlate-primary-care-consultation-context |
| ArchiMate semantic ID | id-8ee674db668e5f2bac3d2670cc6dd74a |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Correlate Primary Care Consultation Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-PRIMARY-CARE.FN-CORRELATE-PRIMARY-CARE-CONSULTATION-CONTEXT |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-PRIMARY-CARE.FN-CORRELATE-PRIMARY-CARE-CONSULTATION-CONTEXT"] |

Responsibility/framing context: [Primary Care Coordination](business-enabling-capabilities.md#hcm-bec-primary-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:correlate-primary-care-consultation-context`) and the element's version-qualified Properties.

<a id="hcm-fn-correlate-virtual-care-consultation-context"></a>

## Correlate Virtual Care Consultation Context

Binds video consultation metadata, remote practitioner identity, and shared digital whiteboard artifacts to the encounter.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:correlate-virtual-care-consultation-context |
| canonicalId | d315a7eab4d9453f83ecd44ed8b57b56 |
| displayName | Correlate Virtual Care Consultation Context |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.RemotePatientMonitoring.CorrelateVirtualCareConsultationContext |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-RemotePatientMonitoring.FN-CorrelateVirtualCareConsultationContext |
| Snapshot publication reference | HCM-FN-correlate-virtual-care-consultation-context |
| ArchiMate semantic ID | id-46f0088db2305a348c0f4d38fa847fae |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Correlate Virtual Care Consultation Context |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-REMOTE-VIRTUAL-CARE.FN-CORRELATE-VIRTUAL-CARE-CONSULTATION-CONTEXT |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-REMOTE-VIRTUAL-CARE.FN-CORRELATE-VIRTUAL-CARE-CONSULTATION-CONTEXT"] |

Responsibility/framing context: [Remote Patient Monitoring](business-enabling-capabilities.md#hcm-bec-remote-virtual-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:correlate-virtual-care-consultation-context`) and the element's version-qualified Properties.

<a id="hcm-fn-ingest-remote-patient-telemetry"></a>

## Ingest Remote Patient Telemetry

Ingests continuous or periodic physiological measurements (e.g., blood pressure, pulse oximetry, continuous glucose) from home monitoring devices.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:ingest-remote-patient-telemetry |
| canonicalId | cb03c3040e1c4a4a9fdadb2502113014 |
| displayName | Ingest Remote Patient Telemetry |
| friendlyName | SharedInformationAndCoordinationEnablement.PatientClinicalRecord.RemotePatientMonitoring.IngestRemotePatientTelemetry |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PatientClinicalRecord.CT3-BEC-RemotePatientMonitoring.FN-IngestRemotePatientTelemetry |
| Snapshot publication reference | HCM-FN-ingest-remote-patient-telemetry |
| ArchiMate semantic ID | id-32337b9eb6265ff3bb7aa91fa7b52941 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Ingest Remote Patient Telemetry |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-REMOTE-VIRTUAL-CARE.FN-INGEST-REMOTE-PATIENT-TELEMETRY |
| Structural aliases | ["CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION.CT3-BEC-REMOTE-VIRTUAL-CARE.FN-INGEST-REMOTE-PATIENT-TELEMETRY"] |

Responsibility/framing context: [Remote Patient Monitoring](business-enabling-capabilities.md#hcm-bec-remote-virtual-care). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:ingest-remote-patient-telemetry`) and the element's version-qualified Properties.

<a id="hcm-fn-present-contextual-health-and-operational-information"></a>

## Present Contextual Health and Operational Information

Composes clinical summaries, work queues, and patient records into responsive role-based user interfaces.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:present-contextual-health-and-operational-information |
| canonicalId | 28ad1d4dc0444c7e8f98906a70e9319e |
| displayName | Present Contextual Health and Operational Information |
| friendlyName | SharedInformationAndCoordinationEnablement.PresentationServices.PresentContextualHealthAndOperationalInformation |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-PresentationServices.FN-PresentContextualHealthAndOperationalInformation |
| Snapshot publication reference | HCM-FN-present-contextual-health-and-operational-information |
| ArchiMate semantic ID | id-d7c8e6abff765c41a178e60921c8cdce |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Present Contextual Health and Operational Information |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PRESENTATION-SERVICES.FN-PRESENT-CONTEXTUAL-HEALTH-AND-OPERATIONAL-INFORMATION |
| Structural aliases | ["CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT.CT3-BEC-PRESENTATION-SERVICES.FN-PRESENT-CONTEXTUAL-HEALTH-AND-OPERATIONAL-INFORMATION"] |

Responsibility/framing context: [Presentation Services](business-enabling-capabilities.md#hcm-bec-presentation-services). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:present-contextual-health-and-operational-information`) and the element's version-qualified Properties.

<a id="hcm-fn-assess-service-offering-readiness"></a>

## Assess Service Offering Readiness

Evaluate applicable dependency evidence to establish a context- and time-qualified service readiness view.

**Overview**

Combines qualified dependency standing without converting missing evidence into ready or assuming that an available resource establishes deliverability.

**Boundary**

Resource ownership and clinical clearance remain external to this coordination.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-assess-service-offering-readiness |
| canonicalId | a7eb76c1ff5b450b9835ac19662c8148 |
| displayName | Assess Service Offering Readiness |
| friendlyName | HealthServiceOperationsEnablement.CareResourceAndCapacityCoordination.ServiceOfferingReadinessCoordination.AssessServiceOfferingReadiness |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-CareResourceAndCapacityCoordination.CT3-BEC-ServiceOfferingReadinessCoordination.FN-AssessServiceOfferingReadiness |
| Snapshot publication reference | HCM-FN-assess-service-offering-readiness |
| ArchiMate semantic ID | id-839281bb947054459ba35072f71c6885 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-OFFERING-READINESS-COORDINATION.FN-ASSESS-SERVICE-OFFERING-READINESS |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: source proposal determines collective readiness; reviewed scope establishes dependency tracking only. Evidence may be reused, but determination authority is not silently transferred. |

Responsibility/framing context: [Service Offering Readiness Coordination](business-enabling-capabilities.md#hcm-bec-service-offering-readiness-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-assess-service-offering-readiness`) and the element's version-qualified Properties.

<a id="hcm-fn-manage-specimen-transport-progression"></a>

## Manage Specimen Transport Progression

Tracks the physical transit of pathology bio-specimens, blood products, and surgical trays: Collected → Courier Picked Up → In-Transit → Laboratory Ingress Received.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:manage-specimen-transport-progression |
| canonicalId | 4c9d50efc9e640fcb3abb02b8dd05486 |
| displayName | Manage Specimen Transport Progression |
| friendlyName | HealthServiceOperationsEnablement.ClinicalLogisticsCoordination.ManageSpecimenTransportProgression |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalLogisticsCoordination.FN-ManageSpecimenTransportProgression |
| Snapshot publication reference | HCM-FN-manage-specimen-transport-progression |
| ArchiMate semantic ID | id-bcffa07f03785f4d8d462b3d6294e169 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Manage Specimen Transport Progression |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-LOGISTICS-COORDINATION.FN-MANAGE-SPECIMEN-TRANSPORT-PROGRESSION |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-CLINICAL-LOGISTICS-COORDINATION.FN-MANAGE-SPECIMEN-TRANSPORT-PROGRESSION"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: The Canonical Function includes pathology bio-specimens, blood products and surgical trays. Surgical-tray/materiel movement is excluded from reviewed Clinical Logistics Coordination; a specimen/materiel semantic split requires human judgement. |

Responsibility/framing context: [Clinical Logistics Coordination](business-enabling-capabilities.md#hcm-bec-clinical-logistics-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:manage-specimen-transport-progression`) and the element's version-qualified Properties.

<a id="hcm-fn-verify-operational-access-credential"></a>

## Verify Operational Access Credential

Verifies physical access badges, specialty ward entry authorizations, and restricted area permissions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:verify-operational-access-credential |
| canonicalId | a0eb34a7cb7046fd880248e799632810 |
| displayName | Verify Operational Access Credential |
| friendlyName | HealthServiceOperationsEnablement.WorkforceQualificationAndDevelopmentEnablement.CredentialManagement.VerifyOperationalAccessCredential |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkforceQualificationAndDevelopmentEnablement.CT3-BEC-CredentialManagement.FN-VerifyOperationalAccessCredential |
| Snapshot publication reference | HCM-FN-verify-operational-access-credential |
| ArchiMate semantic ID | id-26037078ca3750fc85190b74dda18899 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Verify Operational Access Credential |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT.CT3-BEC-CREDENTIAL-MANAGEMENT.FN-VERIFY-OPERATIONAL-ACCESS-CREDENTIAL |
| Unresolved dimensions | ["feature_association"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical operational badges/physical-access credentials are distinct from reviewed clinical-service credential definitions. |

Responsibility/framing context: [Credential Management](business-enabling-capabilities.md#hcm-bec-credential-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:verify-operational-access-credential`) and the element's version-qualified Properties.

<a id="hcm-fn-coordinate-entity-relative-input"></a>

## Coordinate Entity-Relative Input

Select interested behaviours and progress one entity-relative input with explicit participation and effect standing.

**Overview**

Moves an accepted input through the applicable stable behaviour sequence using fresh managed contexts.

**Boundary**

No concurrent intra-entity progression or implicit clinical authority is introduced.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-coordinate-entity-relative-input |
| canonicalId | 78417d5c46cc406f95e6a41d22e3f143 |
| displayName | Coordinate Entity-Relative Input |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.EntitySpecificOperationalCoordination.CoordinateEntityRelativeInput |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-EntitySpecificOperationalCoordination.FN-CoordinateEntityRelativeInput |
| Snapshot publication reference | HCM-FN-coordinate-entity-relative-input |
| ArchiMate semantic ID | id-71475841f2d356ed845244bf16548e78 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-ENTITY-SPECIFIC-OPERATIONAL-COORDINATION.FN-COORDINATE-ENTITY-RELATIVE-INPUT |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Internal entity-relative progression is not the reviewed external work bridge; a current BEC destination has not been adjudicated. |

Responsibility/framing context: [Entity-Specific Operational Coordination](business-enabling-capabilities.md#hcm-bec-entity-specific-operational-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-coordinate-entity-relative-input`) and the element's version-qualified Properties.

<a id="hcm-fn-maintain-operational-fulfilment-definition"></a>

## Maintain Operational Fulfilment Definition

Establish and revise the declared context needs, expected outcome basis and housekeeping of an operational undertaking.

**Overview**

Changes governed activity design while preserving the applicability of the definition used by an existing undertaking.

**Boundary**

A definition revision is not a command to run work or a dynamic clinical workflow generator.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-maintain-operational-fulfilment-definition |
| canonicalId | 9f7085926221436bb672e52e59bca512 |
| displayName | Maintain Operational Fulfilment Definition |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.GovernedOperationalActivityDesign.MaintainOperationalFulfilmentDefinition |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-GovernedOperationalActivityDesign.FN-MaintainOperationalFulfilmentDefinition |
| Snapshot publication reference | HCM-FN-maintain-operational-fulfilment-definition |
| ArchiMate semantic ID | id-1731c2477c425e4a8bc04a3247814e25 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-GOVERNED-OPERATIONAL-ACTIVITY-DESIGN.FN-MAINTAIN-OPERATIONAL-FULFILMENT-DEFINITION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Internal execution-envelope design is distinct from governance of work across an external execution boundary. |

Responsibility/framing context: [Governed Operational Activity Design](business-enabling-capabilities.md#hcm-bec-governed-operational-activity-design). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-maintain-operational-fulfilment-definition`) and the element's version-qualified Properties.

<a id="hcm-fn-coordinate-synthetic-task"></a>

## Coordinate Synthetic Task

Orchestrates automated system workflows and operational progression.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:coordinate-synthetic-task |
| canonicalId | 6cb88c7e689648b1bd0a4d1e26b12f9b |
| displayName | Coordinate Synthetic Task |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.CoordinateSyntheticTask |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FN-CoordinateSyntheticTask |
| Snapshot publication reference | HCM-FN-coordinate-synthetic-task |
| ArchiMate semantic ID | id-a5cb54fd2fa85f94acdbd4e60e4b5194 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Coordinate Synthetic Task |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FN-COORDINATE-SYNTHETIC-TASK |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:coordinate-synthetic-task`) and the element's version-qualified Properties.

<a id="hcm-fn-coordinate-to-do"></a>

## Coordinate To Do

Coordinates human review/update/approval items, reminders and sign-off queues within Harmonia's operational responsibility; clinical requirements and decisions remain with accountable clinical authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:coordinate-to-do |
| canonicalId | a4c9a281578d493eb876eda9fb73cac1 |
| displayName | Coordinate To Do |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.CoordinateToDo |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FN-CoordinateToDo |
| Snapshot publication reference | HCM-FN-coordinate-to-do |
| ArchiMate semantic ID | id-f93be8cf5dc050058aa04638dc6a7e02 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Coordinate To Do |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FN-COORDINATE-TO-DO |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:coordinate-to-do`) and the element's version-qualified Properties.

<a id="hcm-fn-coordinate-work-order"></a>

## Coordinate Work Order

Coordinates operational task lifecycles and dispatches.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:coordinate-work-order |
| canonicalId | 0161308bad6945b5bcfdeea5519341cd |
| displayName | Coordinate Work Order |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.CoordinateWorkOrder |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FN-CoordinateWorkOrder |
| Snapshot publication reference | HCM-FN-coordinate-work-order |
| ArchiMate semantic ID | id-cde6aec655685d8da22c6c02b8160f68 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Coordinate Work Order |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FN-COORDINATE-WORK-ORDER |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:coordinate-work-order`) and the element's version-qualified Properties.

<a id="hcm-fn-determine-reconciled-activity-continuation"></a>

## Determine Reconciled Activity Continuation

Establish the permitted operational continuation from reconciled effect standing and applicable authorisation.

**Overview**

Moves a reconciled undertaking to justified continuation, authorised retry or explicitly retained uncertainty.

**Boundary**

Does not confer clinical decision authority or infer absence of effects from silence; policy evaluation remains separately owned.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-determine-reconciled-activity-continuation |
| canonicalId | 2e326eb5a06641048ad8a919723c4c51 |
| displayName | Determine Reconciled Activity Continuation |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.DetermineReconciledActivityContinuation |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FN-DetermineReconciledActivityContinuation |
| Snapshot publication reference | HCM-FN-determine-reconciled-activity-continuation |
| ArchiMate semantic ID | id-74c46d7f30765d1684db081ebfa23a26 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FN-DETERMINE-RECONCILED-ACTIVITY-CONTINUATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-determine-reconciled-activity-continuation`) and the element's version-qualified Properties.

<a id="hcm-fn-establish-activity-effect-observation-basis"></a>

## Establish Activity Effect Observation Basis

Associate the applicable handoff contract and authoritative observations with an indeterminate operational undertaking.

**Overview**

Changes an unqualified uncertainty concern into a time- and attempt-specific observation basis for reconciliation.

**Boundary**

Source observations retain their authority; this is an operational observation basis rather than an independent assurance assessment.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-establish-activity-effect-observation-basis |
| canonicalId | 4038dd6a1baa4856afc76d7adfa6a457 |
| displayName | Establish Activity Effect Observation Basis |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.EstablishActivityEffectObservationBasis |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FN-EstablishActivityEffectObservationBasis |
| Snapshot publication reference | HCM-FN-establish-activity-effect-observation-basis |
| ArchiMate semantic ID | id-7146dde4fa7d5de8bf4a48d3638bd792 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FN-ESTABLISH-ACTIVITY-EFFECT-OBSERVATION-BASIS |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-establish-activity-effect-observation-basis`) and the element's version-qualified Properties.

<a id="hcm-fn-reconcile-indeterminate-activity-effects"></a>

## Reconcile Indeterminate Activity Effects

Inspect authoritative observations to establish, qualify or retain uncertainty about a previously indeterminate activity effect.

**Overview**

Moves an uncertainty concern to a justified disposition while preserving the possibility that the original attempt produced an effect.

**Boundary**

Missing evidence does not establish absence of effect and cannot automatically justify a repeat mutation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-reconcile-indeterminate-activity-effects |
| canonicalId | eace49fe7b304ad786d460ccef622378 |
| displayName | Reconcile Indeterminate Activity Effects |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.ReconcileIndeterminateActivityEffects |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FN-ReconcileIndeterminateActivityEffects |
| Snapshot publication reference | HCM-FN-reconcile-indeterminate-activity-effects |
| ArchiMate semantic ID | id-6243b2abcf395f3e9446777baee272c5 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FN-RECONCILE-INDETERMINATE-ACTIVITY-EFFECTS |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-reconcile-indeterminate-activity-effects`) and the element's version-qualified Properties.

<a id="hcm-fn-supervise-activity-timeout-escalation"></a>

## Supervise Activity Timeout / Escalation

Monitors SLAs and triggers escalations when clinical or operational deadlines elapse.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:supervise-activity-timeout--escalation |
| canonicalId | cbf6bfd71d5d4ea9923f10f288ff24dd |
| displayName | Supervise Activity Timeout / Escalation |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.SuperviseActivityTimeoutEscalation |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.FN-SuperviseActivityTimeoutEscalation |
| Snapshot publication reference | HCM-FN-supervise-activity-timeout-escalation |
| ArchiMate semantic ID | id-bc25cbd6dbef5a559fb7db80a4408500 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Supervise Activity Timeout / Escalation |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FN-SUPERVISE-ACTIVITY-TIMEOUT-ESCALATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:supervise-activity-timeout--escalation`) and the element's version-qualified Properties.

<a id="hcm-fn-adjudicate-assurance-assessment"></a>

## Adjudicate Assurance Assessment

•
Canonical Name: Adjudicate Assurance Assessment.

•
Definition: Determines the assurance finding or conclusion that follows from the assessed evidence and applicable assurance criteria.

•
Boundary: Establishment of the finding/conclusion is part of adjudication. It remains distinct from evidence assessment, operational response, remediation, escalation, workflow progression and clinical judgement. No separate conclusion-establishment or reporting Function is introduced merely because a conclusion is established or may be communicated.

---

## History

### Function Overview — predecessor explanation

Adjudication establishes the supported finding or conclusion from assessed detail. It is distinct from gathering evidence and from reporting an outcome already reached. Insufficiency must remain explicit rather than be forced into satisfaction or non-satisfaction, and the resulting finding does not acquire authority to remediate the subject.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:adjudicate-assurance-assessment |
| canonicalId | 7d7cd216415d4b839ea3edfbf1538110 |
| displayName | Adjudicate Assurance Assessment |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement.GovernedAssurance.AdjudicateAssuranceAssessment |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement.CT3-BEC-GovernedAssurance.FN-AdjudicateAssuranceAssessment |
| Snapshot publication reference | HCM-FN-adjudicate-assurance-assessment |
| ArchiMate semantic ID | id-270886b2adc054ba8834eb50a3c92ba5 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Adjudicate Assurance Assessment |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE.FN-ADJUDICATE-ASSURANCE-ASSESSMENT |
| Unresolved dimensions | ["feature_association","contextual_view"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself. |

Responsibility/framing context: [Governed Assurance](business-enabling-capabilities.md#hcm-bec-governed-assurance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:adjudicate-assurance-assessment`) and the element's version-qualified Properties.

<a id="hcm-fn-assess-assurance-evidence"></a>

## Assess Assurance Evidence

•
Canonical Name: Assess Assurance Evidence.

•
Definition: Evaluates the available trustworthy evidence against the applicable assurance criteria and establishes what that evidence demonstrates for each relevant criterion or assessment concern.

•
Boundary: Assessment remains distinct from adjudication. It may identify satisfaction, exception, insufficiency or other criterion-level results where applicable; no universal result taxonomy is established. Quantitative, qualitative, compound, temporal and confidence-bearing assessment remain possible. Insufficient evidence is not equivalent to either satisfaction or non-satisfaction.

---

## History

### Function Overview — predecessor explanation

Evidence assessment applies the established evaluative basis to the available trustworthy material. It produces assessed detail rather than treating source availability or successful processing as a conclusion. Explicit gaps and qualifications preserve what the evidence cannot establish for subsequent adjudication.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:assess-assurance-evidence |
| canonicalId | e1f6402d47c0455b95b45a1a4d393484 |
| displayName | Assess Assurance Evidence |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement.GovernedAssurance.AssessAssuranceEvidence |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement.CT3-BEC-GovernedAssurance.FN-AssessAssuranceEvidence |
| Snapshot publication reference | HCM-FN-assess-assurance-evidence |
| ArchiMate semantic ID | id-fe132e947ab35881a3f8d3b7c72bb998 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Assess Assurance Evidence |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE.FN-ASSESS-ASSURANCE-EVIDENCE |
| Unresolved dimensions | ["feature_association","contextual_view"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself. |

Responsibility/framing context: [Governed Assurance](business-enabling-capabilities.md#hcm-bec-governed-assurance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:assess-assurance-evidence`) and the element's version-qualified Properties.

<a id="hcm-fn-establish-assurance-context"></a>

## Establish Assurance Context

•
Canonical Name: Establish Assurance Context.

•
Definition: Establishes the governed subject, applicable assurance purpose and criteria, relevant temporal context, and contextual association of sufficient trustworthy source information as evidence required for an assurance activity.

•
Scope: Includes the evidence-assembly responsibility previously described as Assurance Evidence Assembly; no separate evidence-assembly Function is established. Sufficiency is required for conclusion, not assumed merely because context is established; unavailable or insufficient evidence remains explicit.

•
Information Boundary: Source information remains owned by its responsible source Capability. Its use as evidence does not transfer ownership or originating authority. Governed Assurance owns the contextual association that particular information constitutes evidence for a particular assurance purpose. Evidence association and applicable criteria may each be temporal and version-specific; association distinguishes the information state relevant to assurance from its current state.

•
Realisation Boundary: Uses generic EC-02 context-management capability together with EC-14 assurance-specific semantics as documented in §4.1 (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#41-context-management-contribution). Establishment of Assurance Context prescribes no copying, persistence, aggregation, caching or other technical mechanism.

---

## History

### Function Overview — predecessor explanation

Context establishment makes particular source-owned information evidence for a particular assurance purpose, with applicable subject, criteria and time identified. It includes evidence assembly without assuming sufficiency or prescribing copying and storage. This contextual evidentiary association is the assurance responsibility; ownership of the source material remains unchanged.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:establish-assurance-context |
| canonicalId | ffffe352e28c4e878d986b0e15ddff15 |
| displayName | Establish Assurance Context |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement.GovernedAssurance.EstablishAssuranceContext |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement.CT3-BEC-GovernedAssurance.FN-EstablishAssuranceContext |
| Snapshot publication reference | HCM-FN-establish-assurance-context |
| ArchiMate semantic ID | id-0bb5b41f087655cc97757af20493bbfd |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Establish Assurance Context |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE.FN-ESTABLISH-ASSURANCE-CONTEXT |
| Unresolved dimensions | ["feature_association","contextual_view"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself. |

Responsibility/framing context: [Governed Assurance](business-enabling-capabilities.md#hcm-bec-governed-assurance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:establish-assurance-context`) and the element's version-qualified Properties.

<a id="hcm-fn-qualify-information-authority-standing"></a>

## Qualify Information Authority Standing

Associate source and credibility qualifications with an information assertion under applicable governed semantics.

**Overview**

Moves an unqualified received assertion to explicitly attributed authority standing without equating persistence success to credibility.

**Boundary**

Does not adjudicate all competing claims or originate clinical truth.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-qualify-information-authority-standing |
| canonicalId | f5b57bbba47a4e86bd2f79f700d6977a |
| displayName | Qualify Information Authority Standing |
| friendlyName | SharedInformationAndCoordinationEnablement.ClinicalInformationStewardshipEnablement.InformationAuthorityRepresentation.QualifyInformationAuthorityStanding |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-ClinicalInformationStewardshipEnablement.CT3-BEC-InformationAuthorityRepresentation.FN-QualifyInformationAuthorityStanding |
| Snapshot publication reference | HCM-FN-qualify-information-authority-standing |
| ArchiMate semantic ID | id-1feb20e89c115281aed3c0bdc86623bb |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-INFORMATION-AUTHORITY-REPRESENTATION.FN-QUALIFY-INFORMATION-AUTHORITY-STANDING |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Source credibility and authority standing require an explicit home; information-asset tags alone do not establish that responsibility. |

Responsibility/framing context: [Information Authority Representation](business-enabling-capabilities.md#hcm-bec-information-authority-representation). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-qualify-information-authority-standing`) and the element's version-qualified Properties.

<a id="hcm-fn-mediate-standards-based-health-information-communication"></a>

## Mediate Standards-Based Health Information Communication

Handles standards-based communication mediation, transaction coordination, and technical error handling.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | function:mediate-standards-based-health-information-communication |
| canonicalId | 6c14589215f744a2b76605a2023b04fb |
| displayName | Mediate Standards-Based Health Information Communication |
| friendlyName | SharedInformationAndCoordinationEnablement.HealthInformationConnectivityEnablement.HealthInformationCommunication.MediateStandardsBasedHealthInformationCommunication |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-HealthInformationConnectivityEnablement.CT3-BEC-HealthInformationCommunication.FN-MediateStandardsBasedHealthInformationCommunication |
| Snapshot publication reference | HCM-FN-mediate-standards-based-health-information-communication |
| ArchiMate semantic ID | id-eea1d434d5695c2fa0152d2e771843a9 |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Mediate Standards-Based Health Information Communication |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-COMMUNICATION.FN-MEDIATE-STANDARDS-BASED-HEALTH-INFORMATION-COMMUNICATION |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical boundary protocol mediation and technical transmission are distinct from reviewed patient-facing communication. |

Responsibility/framing context: [Health Information Communication](business-enabling-capabilities.md#hcm-bec-health-information-communication). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `function:mediate-standards-based-health-information-communication`) and the element's version-qualified Properties.

<a id="hcm-fn-capture-required-significant-audit-evidence"></a>

## Capture Required Significant Audit Evidence

Establish required significant activity evidence under the governed authoritative audit-capture responsibility.

**Overview**

Moves an auditable fact to confirmed captured standing or an explicit capture failure affecting the protected operation.

**Boundary**

Specific capture authority is not borrowed from the originating identity and is not generalised.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:function-capture-required-significant-audit-evidence |
| canonicalId | c8e614257cf14bd1878df894455f21fb |
| displayName | Capture Required Significant Audit Evidence |
| friendlyName | SharedInformationAndCoordinationEnablement.InformationProtectionAndAccountabilityEnablement.SignificantActivityAuditCapture.CaptureRequiredSignificantAuditEvidence |
| canonicalName | CT1-BEC-SharedInformationAndCoordinationEnablement.CT2-BEC-InformationProtectionAndAccountabilityEnablement.CT3-BEC-SignificantActivityAuditCapture.FN-CaptureRequiredSignificantAuditEvidence |
| Snapshot publication reference | HCM-FN-capture-required-significant-audit-evidence |
| ArchiMate semantic ID | id-b90ff793567e5e2f860f1db1eff4000a |
| Legacy identity evidence | No legacy allocation |
| Construct | function |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-SIGNIFICANT-ACTIVITY-AUDIT-CAPTURE.FN-CAPTURE-REQUIRED-SIGNIFICANT-AUDIT-EVIDENCE |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: The reviewed hierarchy does not allocate general mandatory authoritative audit capture; disclosure or provenance alone is not an equivalent responsibility. |

Responsibility/framing context: [Significant Activity Audit Capture](business-enabling-capabilities.md#hcm-bec-significant-activity-audit-capture). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:function-capture-required-significant-audit-evidence`) and the element's version-qualified Properties.
