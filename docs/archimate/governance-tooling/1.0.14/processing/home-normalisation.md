<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Explicit Home and Business baseline normalisation — 1.0.14

Accepted Solution Architect modelling/governance change: **1.0.13 → 1.0.14**.
[Architecture Modelling Strategy](../../../../markdown/governance/architecture-modelling-strategy.md)
establishes native directed Association specialised as Home, independently of
Realization, structural containment, ownership, naming and navigation. Semantic
compatibility constrains all Harmonia ArchiMate specialisations. AX-04/14/17 apply.
No Information Architecture or Application Architecture ingestion occurred.

## Fresh Solution Architect-authored input — four relationships

The current 1.0.8 XML was freshly read: SHA-256 `366bbcf305575b831ea7e8e72e1c2b19f9631518fda8f297a4f7c51a959a5556`,
21,858,672 bytes. [Compressed exact input](../../ingestion/1.0.8/run-002/detection/submission.xml.gz)
and [complete endpoint/context evidence](../../ingestion/1.0.8/run-002/detection/explicit-homes.json)
preserve the authored assertions; the landing XML and native Archi file remain
byte-identical to task start. A distinct updated input has its own run-002 under
the existing run convention; run-001's original observations are not replaced.

| Authored source | Native source type | Authored target | Target type | Authored relationship ID |
| --- | --- | --- | --- | --- |
| [Business Process] Identifier Verification | BusinessProcess | [Feature] Identifier Capture | Capability / Feature | `id-ba8d543c3f6a4f15bd41e5c2d7bcf6cf` |
| [Business Service] Identifier Update | BusinessService | [Feature] Identifier Capture | Capability / Feature | `id-a21144ee285f467982d13c39b49228dd` |
| [Business Service] Identifier Create | BusinessService | [Feature] Identifier Capture | Capability / Feature | `id-3f9d4356f4b84ef7b45fe3435d27fde8` |
| [Business Service] Verify Identity Source | BusinessService | Identity Source & Correction Lineage | Capability / Feature | `id-d559bbf53430465e90f47c40593403a5` |

Every row is an unnamed native **Association**, directed **source → target**, with
Archi specialisation **Home**. All targets are Features. The submitted Capability
context is Entity Information Enablement → Client Administration → Person Identity
Administration → Feature, preserving its independently governed standing.
The update adds exactly these four edges and four visual connections in two
existing Views; every previous submitted element and semantic edge is unchanged.
The architecture definition does not depend on XML. The current export's
`isDirected="true"` and relationship property `specialization = Home` demonstrate
interchange fidelity; the displayed property-definition name is `Specialization`.

## Existing-baseline normalisation — 208 additional assertions

All **319 existing Business behaviours** were assessed. The fixed
[migration register](../../../../markdown/governance/baseline/business-homes.json) records exact canonical endpoint
identities, literal relationship IDs, semantic allocation statements, corroborating
source-key bindings and preserved standing. **104 Functions,
96 Services and 8 Processes** now have
explicit Home. The full materialised list appears in
[baseline relationship assertions](../../../../markdown/governance/baseline/relationships.md#explicit-business-home-assertions).

Evidence is an explicit governed Feature responsibility allocation in the Business
catalogue plus its independently reconciled binding. A Process requires its own
explicit Governed Process statement. A sole Realization, heading, folder, View,
name or graph traversal supplies no Home. Multi-Feature behaviour and proposed
bindings are withheld where a singular Home needs architectural judgement.
The register is the one-time accepted migration result. Publication replays its
literal rows and validates their fingerprints; it discovers no additional Home.

## Uncertainty retained for the Solution Architect

**93** behaviours have insufficient current deterministic Home
evidence: 56
current records and 37
pending-adjudication records. **18** additional current behaviours
have competing plausible Feature contexts. Candidate contexts below are review
evidence, not asserted Homes. Direct Capability-scoped responsibility remains
valid; absence of a Home is not a newly introduced conformance failure.

### More than one plausible Home — 18

| Business behaviour | Exact source key | Currentness | Candidate Feature contexts for adjudication |
| --- | --- | --- | --- |
| [Practitioner Directory Query](../../../../markdown/governance/baseline/services.md#hcm-sv-practitioner-directory-query) | `service:practitioner-directory-query` | current | Practitioner Profile Governance; Provider Directory Discovery & Resolution |
| [Manage Ward Capacity](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-ward-capacity) | `function:manage-ward-capacity` | current | Ward Patient Flow Coordination; Ward Care Capacity Management |
| [Manage Theatre Case Progression](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-theatre-case-progression) | `function:manage-theatre-case-progression` | current | Operating Theatre Case Progression; Perioperative Resource Notification |
| [Theatre Case Progression Telemetry](../../../../markdown/governance/baseline/services.md#hcm-sv-theatre-case-progression-telemetry) | `service:theatre-case-progression-telemetry` | current | Operating Theatre Case Progression; Perioperative Resource Notification |
| [Coordinate ED Departmental Capacity](../../../../markdown/governance/baseline/functions.md#hcm-fn-coordinate-ed-departmental-capacity) | `function:coordinate-ed-departmental-capacity` | current | ED Bed & Bay Tracking; Emergency Treatment Capacity Management |
| [Manage Outpatient Capacity](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-outpatient-capacity) | `function:manage-outpatient-capacity` | current | Outpatient Arrival Registration; Outpatient Appointment Capacity Management |
| [Manage Bed Turnover Progression](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-bed-turnover-progression) | `function:manage-bed-turnover-progression` | current | Bed Readiness Notification; Bed State Progression |
| [Bed Turnover Lifecycle Service](../../../../markdown/governance/baseline/services.md#hcm-sv-bed-turnover-lifecycle-service) | `service:bed-turnover-lifecycle-service` | current | Bed Readiness Notification; Bed State Progression |
| [Dispatch Operational Work Order](../../../../markdown/governance/baseline/functions.md#hcm-fn-dispatch-operational-work-order) | `function:dispatch-operational-work-order` | current | Work Dispatch Delivery; Work Item Instantiation; Worker Matching & Allocation |
| [Work Dispatch Service](../../../../markdown/governance/baseline/services.md#hcm-sv-work-dispatch-service) | `service:work-dispatch-service` | current | Work Dispatch Delivery; Work Item Instantiation; Worker Matching & Allocation |
| [Manage Patient Transport Progression](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-patient-transport-progression) | `function:manage-patient-transport-progression` | current | Transport Dispatch & Progress Tracking; Transport Request Ingestion |
| [Patient Transport Tracking Service](../../../../markdown/governance/baseline/services.md#hcm-sv-patient-transport-tracking-service) | `service:patient-transport-tracking-service` | current | Transport Dispatch & Progress Tracking; Transport Request Ingestion |
| [Manage Discharge Coordination Progression](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-discharge-coordination-progression) | `function:manage-discharge-coordination-progression` | current | Discharge Milestone Tracking; Discharge Notification Dispatch; Discharge Readiness & Coordination Oversight |
| [Discharge Readiness Telemetry](../../../../markdown/governance/baseline/services.md#hcm-sv-discharge-readiness-telemetry) | `service:discharge-readiness-telemetry` | current | Discharge Milestone Tracking; Discharge Notification Dispatch; Discharge Readiness & Coordination Oversight |
| [Theatre Case Progression Process](../../../../markdown/governance/baseline/processes.md#hcm-pr-theatre-case-progression-process) | `process:theatre-case-progression-process` | current | Operating Theatre Case Progression; Perioperative Resource Notification |
| [Bed Turnover Process](../../../../markdown/governance/baseline/processes.md#hcm-pr-bed-turnover-process) | `process:bed-turnover-process` | current | Bed Readiness Notification; Bed State Progression |
| [Patient Transport Process](../../../../markdown/governance/baseline/processes.md#hcm-pr-patient-transport-process) | `process:patient-transport-process` | current | Transport Dispatch & Progress Tracking; Transport Request Ingestion |
| [Discharge Progression Process](../../../../markdown/governance/baseline/processes.md#hcm-pr-discharge-progression-process) | `process:discharge-progression-process` | current | Discharge Milestone Tracking; Discharge Notification Dispatch; Discharge Readiness & Coordination Oversight |

### Home not deterministically established — 93

| Business behaviour | Exact source key | Currentness | Retained Feature evidence, if any |
| --- | --- | --- | --- |
| [Associate Order Outcome](../../../../markdown/governance/baseline/functions.md#hcm-fn-associate-order-outcome) | `function:associate-order-outcome` | current | Order Outcome Association |
| [Order Outcome Notification](../../../../markdown/governance/baseline/services.md#hcm-sv-order-outcome-notification) | `service:order-outcome-notification` | current | Not established |
| [Associate Care Plan Context](../../../../markdown/governance/baseline/functions.md#hcm-fn-associate-care-plan-context) | `function:associate-care-plan-context` | current | Not established |
| [Care Plan Context Query](../../../../markdown/governance/baseline/services.md#hcm-sv-care-plan-context-query) | `service:care-plan-context-query` | current | Not established |
| [Distribute Care Plan Change](../../../../markdown/governance/baseline/functions.md#hcm-fn-distribute-care-plan-change) | `function:distribute-care-plan-change` | current | Not established |
| [Care Plan Change Notification](../../../../markdown/governance/baseline/services.md#hcm-sv-care-plan-change-notification) | `service:care-plan-change-notification` | current | Not established |
| [Coordinate Multi-Agency Chronic Care Plan](../../../../markdown/governance/baseline/functions.md#hcm-fn-coordinate-multi-agency-chronic-care-plan) | `function:coordinate-multi-agency-chronic-care-plan` | current | Not established |
| [Chronic Care Timeline Query](../../../../markdown/governance/baseline/services.md#hcm-sv-chronic-care-timeline-query) | `service:chronic-care-timeline-query` | current | Not established |
| [Route Acute Critical Result Alert](../../../../markdown/governance/baseline/functions.md#hcm-fn-route-acute-critical-result-alert) | `function:route-acute-critical-result-alert` | current | Not established |
| [Acute Clinical Alert Dispatch](../../../../markdown/governance/baseline/services.md#hcm-sv-acute-clinical-alert-dispatch) | `service:acute-clinical-alert-dispatch` | current | Not established |
| [Assemble Inpatient Discharge Dossier](../../../../markdown/governance/baseline/functions.md#hcm-fn-assemble-inpatient-discharge-dossier) | `function:assemble-inpatient-discharge-dossier` | current | Not established |
| [Discharge Dossier Packaging Service](../../../../markdown/governance/baseline/services.md#hcm-sv-discharge-dossier-packaging-service) | `service:discharge-dossier-packaging-service` | current | Not established |
| [Receive Diagnostic Requisition Feed](../../../../markdown/governance/baseline/functions.md#hcm-fn-receive-diagnostic-requisition-feed) | `function:receive-diagnostic-requisition-feed` | current | Not established |
| [Modality Worklist Distribution](../../../../markdown/governance/baseline/services.md#hcm-sv-modality-worklist-distribution) | `service:modality-worklist-distribution` | current | Not established |
| [Publish Governed Diagnostic Report](../../../../markdown/governance/baseline/functions.md#hcm-fn-publish-governed-diagnostic-report) | `function:publish-governed-diagnostic-report` | current | Not established |
| [Diagnostic Publication Service](../../../../markdown/governance/baseline/services.md#hcm-sv-diagnostic-publication-service) | `service:diagnostic-publication-service` | current | Not established |
| [Correlate Adverse Reaction & Allergy Profile](../../../../markdown/governance/baseline/functions.md#hcm-fn-correlate-adverse-reaction-allergy-profile) | `function:correlate-adverse-reaction--allergy-profile` | current | Not established |
| [Allergy & Adverse Reaction Query](../../../../markdown/governance/baseline/services.md#hcm-sv-allergy-adverse-reaction-query) | `service:allergy--adverse-reaction-query` | current | Not established |
| [Project Screening Eligibility Timeline](../../../../markdown/governance/baseline/functions.md#hcm-fn-project-screening-eligibility-timeline) | `function:project-screening-eligibility-timeline` | current | Not established |
| [Preventive Screening Status Query](../../../../markdown/governance/baseline/services.md#hcm-sv-preventive-screening-status-query) | `service:preventive-screening-status-query` | current | Not established |
| [Bind Community Support Network Context](../../../../markdown/governance/baseline/functions.md#hcm-fn-bind-community-support-network-context) | `function:bind-community-support-network-context` | current | Not established |
| [Community Care Team Query](../../../../markdown/governance/baseline/services.md#hcm-sv-community-care-team-query) | `service:community-care-team-query` | current | Not established |
| [Manage Clinic & Practice Capacity](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-clinic-practice-capacity) | `function:manage-clinic--practice-capacity` | current | Clinic Session Capacity Management |
| [Ward Operational Summary Query](../../../../markdown/governance/baseline/services.md#hcm-sv-ward-operational-summary-query) | `service:ward-operational-summary-query` | current | Not established |
| [Manage Theatre Capacity](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-theatre-capacity) | `function:manage-theatre-capacity` | current | Theatre Session Capacity Management |
| [ED Operational Status Query](../../../../markdown/governance/baseline/services.md#hcm-sv-ed-operational-status-query) | `service:ed-operational-status-query` | current | Not established |
| [Ambulatory Session Status Query](../../../../markdown/governance/baseline/services.md#hcm-sv-ambulatory-session-status-query) | `service:ambulatory-session-status-query` | current | Not established |
| [Manage Mobile Service Capacity](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-mobile-service-capacity) | `function:manage-mobile-service-capacity` | current | Mobile Service Capacity Management |
| [Track Operational Staff Presence](../../../../markdown/governance/baseline/functions.md#hcm-fn-track-operational-staff-presence) | `function:track-operational-staff-presence` | current | Not established |
| [Staff Presence Telemetry Query](../../../../markdown/governance/baseline/services.md#hcm-sv-staff-presence-telemetry-query) | `service:staff-presence-telemetry-query` | current | Not established |
| [Verify Operational Access Credential](../../../../markdown/governance/baseline/functions.md#hcm-fn-verify-operational-access-credential) | `function:verify-operational-access-credential` | pending_adjudication | Not established |
| [Operational Credential Checkpoint Service](../../../../markdown/governance/baseline/services.md#hcm-sv-operational-credential-checkpoint-service) | `service:operational-credential-checkpoint-service` | pending_adjudication | Not established |
| [Manage Specimen Transport Progression](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-specimen-transport-progression) | `function:manage-specimen-transport-progression` | pending_adjudication | Not established |
| [Retrieve Health Information](../../../../markdown/governance/baseline/functions.md#hcm-fn-retrieve-health-information) | `function:retrieve-health-information` | current | Not established |
| [Clinical Resource Retrieval Service](../../../../markdown/governance/baseline/services.md#hcm-sv-clinical-resource-retrieval-service) | `service:clinical-resource-retrieval-service` | current | Not established |
| [Apply Access-Qualified Result Filtering](../../../../markdown/governance/baseline/functions.md#hcm-fn-apply-access-qualified-result-filtering) | `function:apply-access-qualified-result-filtering` | current | Not established |
| [Mediate Standards-Based Health Information Communication](../../../../markdown/governance/baseline/functions.md#hcm-fn-mediate-standards-based-health-information-communication) | `function:mediate-standards-based-health-information-communication` | pending_adjudication | Not established |
| [Standards Communication Gateway Service](../../../../markdown/governance/baseline/services.md#hcm-sv-standards-communication-gateway-service) | `service:standards-communication-gateway-service` | pending_adjudication | Not established |
| [Evaluate Consent Constraint](../../../../markdown/governance/baseline/functions.md#hcm-fn-evaluate-consent-constraint) | `function:evaluate-consent-constraint` | current | Not established |
| [Consent Enforcement Service](../../../../markdown/governance/baseline/services.md#hcm-sv-consent-enforcement-service) | `service:consent-enforcement-service` | current | Not established |
| [Propagate Security Context](../../../../markdown/governance/baseline/functions.md#hcm-fn-propagate-security-context) | `function:propagate-security-context` | current | Not established |
| [Security Context Validation Service](../../../../markdown/governance/baseline/services.md#hcm-sv-security-context-validation-service) | `service:security-context-validation-service` | current | Not established |
| [Record Security-Significant Activity](../../../../markdown/governance/baseline/functions.md#hcm-fn-record-security-significant-activity) | `function:record-security-significant-activity` | current | Not established |
| [Security Audit Ingress](../../../../markdown/governance/baseline/services.md#hcm-sv-security-audit-ingress) | `service:security-audit-ingress` | current | Not established |
| [Coordinate Work Order](../../../../markdown/governance/baseline/functions.md#hcm-fn-coordinate-work-order) | `function:coordinate-work-order` | pending_adjudication | Not established |
| [Work Order Coordination Service](../../../../markdown/governance/baseline/services.md#hcm-sv-work-order-coordination-service) | `service:work-order-coordination-service` | pending_adjudication | Not established |
| [Coordinate To Do](../../../../markdown/governance/baseline/functions.md#hcm-fn-coordinate-to-do) | `function:coordinate-to-do` | pending_adjudication | Not established |
| [To Do Management Service](../../../../markdown/governance/baseline/services.md#hcm-sv-to-do-management-service) | `service:to-do-management-service` | pending_adjudication | Not established |
| [Coordinate Synthetic Task](../../../../markdown/governance/baseline/functions.md#hcm-fn-coordinate-synthetic-task) | `function:coordinate-synthetic-task` | pending_adjudication | Not established |
| [Synthetic Task Execution Service](../../../../markdown/governance/baseline/services.md#hcm-sv-synthetic-task-execution-service) | `service:synthetic-task-execution-service` | pending_adjudication | Not established |
| [Supervise Activity Timeout / Escalation](../../../../markdown/governance/baseline/functions.md#hcm-fn-supervise-activity-timeout-escalation) | `function:supervise-activity-timeout--escalation` | pending_adjudication | Not established |
| [Activity Escalation Telemetry](../../../../markdown/governance/baseline/services.md#hcm-sv-activity-escalation-telemetry) | `service:activity-escalation-telemetry` | pending_adjudication | Not established |
| [Govern Information Definition](../../../../markdown/governance/baseline/functions.md#hcm-fn-govern-information-definition) | `function:govern-information-definition` | current | Information Definition & Schema Governance |
| [Information Dictionary Query](../../../../markdown/governance/baseline/services.md#hcm-sv-information-dictionary-query) | `service:information-dictionary-query` | current | Not established |
| [Govern Terminology Binding](../../../../markdown/governance/baseline/functions.md#hcm-fn-govern-terminology-binding) | `function:govern-terminology-binding` | current | Terminology Binding Governance |
| [Terminology Binding Specification Lookup](../../../../markdown/governance/baseline/services.md#hcm-sv-terminology-binding-specification-lookup) | `service:terminology-binding-specification-lookup` | current | Not established |
| [Govern Exchange Profile](../../../../markdown/governance/baseline/functions.md#hcm-fn-govern-exchange-profile) | `function:govern-exchange-profile` | current | Exchange Profile Governance |
| [Exchange Profile Specification Query](../../../../markdown/governance/baseline/services.md#hcm-sv-exchange-profile-specification-query) | `service:exchange-profile-specification-query` | current | Not established |
| [Design Service Assurance](../../../../markdown/governance/baseline/functions.md#hcm-fn-design-service-assurance) | `function:design-service-assurance` | current | Assurance Disposition & Design Modelling |
| [Manage Assurance Criteria](../../../../markdown/governance/baseline/functions.md#hcm-fn-manage-assurance-criteria) | `function:manage-assurance-criteria` | current | Temporal Assurance Criteria Stewardship |
| [Establish Assurance Context](../../../../markdown/governance/baseline/functions.md#hcm-fn-establish-assurance-context) | `function:establish-assurance-context` | pending_adjudication | Not established |
| [Assess Assurance Evidence](../../../../markdown/governance/baseline/functions.md#hcm-fn-assess-assurance-evidence) | `function:assess-assurance-evidence` | pending_adjudication | Not established |
| [Adjudicate Assurance Assessment](../../../../markdown/governance/baseline/functions.md#hcm-fn-adjudicate-assurance-assessment) | `function:adjudicate-assurance-assessment` | pending_adjudication | Not established |
| [Request Service Assurance](../../../../markdown/governance/baseline/services.md#hcm-sv-request-service-assurance) | `service:request-service-assurance` | pending_adjudication | Not established |
| [Request Service Assurance Status](../../../../markdown/governance/baseline/services.md#hcm-sv-request-service-assurance-status) | `service:request-service-assurance-status` | pending_adjudication | Not established |
| [Communicate Service Assurance Outcome](../../../../markdown/governance/baseline/services.md#hcm-sv-communicate-service-assurance-outcome) | `service:communicate-service-assurance-outcome` | pending_adjudication | Not established |
| [Specimen Transport Process](../../../../markdown/governance/baseline/processes.md#hcm-pr-specimen-transport-process) | `process:specimen-transport-process` | pending_adjudication | Not established |
| [Work Order Progression Process](../../../../markdown/governance/baseline/processes.md#hcm-pr-work-order-progression-process) | `process:work-order-progression-process` | pending_adjudication | Not established |
| [To Do Progression Process](../../../../markdown/governance/baseline/processes.md#hcm-pr-to-do-progression-process) | `process:to-do-progression-process` | pending_adjudication | Not established |
| [Synthetic Task Progression Process](../../../../markdown/governance/baseline/processes.md#hcm-pr-synthetic-task-progression-process) | `process:synthetic-task-progression-process` | pending_adjudication | Not established |
| [Assurance Process Design](../../../../markdown/governance/baseline/processes.md#hcm-pr-assurance-process-design) | `process:assurance-process-design` | current | Not established |
| [Assurance Process Execution](../../../../markdown/governance/baseline/processes.md#hcm-pr-assurance-process-execution) | `process:assurance-process-execution` | pending_adjudication | Not established |
| [Assurance Process Reporting / Communication](../../../../markdown/governance/baseline/processes.md#hcm-pr-assurance-process-reporting-communication) | `process:assurance-process-reporting--communication` | pending_adjudication | Not established |
| [Resolve Associated Binary Content](../../../../markdown/governance/baseline/functions.md#hcm-fn-resolve-associated-binary-content) | `proposal:function-resolve-associated-binary-content` | current | Binary Content Resolution & Assembly |
| [Assemble Associated Binary Content](../../../../markdown/governance/baseline/functions.md#hcm-fn-assemble-associated-binary-content) | `proposal:function-assemble-associated-binary-content` | current | Binary Content Resolution & Assembly |
| [Associated Binary Content Retrieval](../../../../markdown/governance/baseline/services.md#hcm-sv-associated-binary-content-retrieval) | `proposal:service-associated-binary-content-retrieval` | current | Not established |
| [Qualify Information Authority Standing](../../../../markdown/governance/baseline/functions.md#hcm-fn-qualify-information-authority-standing) | `proposal:function-qualify-information-authority-standing` | pending_adjudication | Not established |
| [Accept Governed Dataset Revision](../../../../markdown/governance/baseline/functions.md#hcm-fn-accept-governed-dataset-revision) | `proposal:function-accept-governed-dataset-revision` | current | Governed Dataset Content Revision |
| [Governed Reference Dataset Content Query](../../../../markdown/governance/baseline/services.md#hcm-sv-governed-reference-dataset-content-query) | `proposal:service-governed-reference-dataset-content-query` | current | Not established |
| [Capture Required Significant Audit Evidence](../../../../markdown/governance/baseline/functions.md#hcm-fn-capture-required-significant-audit-evidence) | `proposal:function-capture-required-significant-audit-evidence` | pending_adjudication | Not established |
| [Mandatory Audit Evidence Capture](../../../../markdown/governance/baseline/services.md#hcm-sv-mandatory-audit-evidence-capture) | `proposal:service-mandatory-audit-evidence-capture` | pending_adjudication | Not established |
| [Record Significant Information Lineage](../../../../markdown/governance/baseline/functions.md#hcm-fn-record-significant-information-lineage) | `proposal:function-record-significant-information-lineage` | current | Information Transformation Lineage |
| [Coordinate Entity-Relative Input](../../../../markdown/governance/baseline/functions.md#hcm-fn-coordinate-entity-relative-input) | `proposal:function-coordinate-entity-relative-input` | pending_adjudication | Not established |
| [Entity Operational Input Submission](../../../../markdown/governance/baseline/services.md#hcm-sv-entity-operational-input-submission) | `proposal:service-entity-operational-input-submission` | pending_adjudication | Not established |
| [Maintain Operational Fulfilment Definition](../../../../markdown/governance/baseline/functions.md#hcm-fn-maintain-operational-fulfilment-definition) | `proposal:function-maintain-operational-fulfilment-definition` | pending_adjudication | Not established |
| [Reconcile Indeterminate Activity Effects](../../../../markdown/governance/baseline/functions.md#hcm-fn-reconcile-indeterminate-activity-effects) | `proposal:function-reconcile-indeterminate-activity-effects` | pending_adjudication | Not established |
| [Establish Activity Effect Observation Basis](../../../../markdown/governance/baseline/functions.md#hcm-fn-establish-activity-effect-observation-basis) | `proposal:function-establish-activity-effect-observation-basis` | pending_adjudication | Not established |
| [Determine Reconciled Activity Continuation](../../../../markdown/governance/baseline/functions.md#hcm-fn-determine-reconciled-activity-continuation) | `proposal:function-determine-reconciled-activity-continuation` | pending_adjudication | Not established |
| [Indeterminate Activity Outcome Reconciliation Process](../../../../markdown/governance/baseline/processes.md#hcm-pr-indeterminate-activity-outcome-reconciliation-process) | `proposal:process-indeterminate-activity-outcome-reconciliation-process` | pending_adjudication | Not established |
| [Assess Service Offering Readiness](../../../../markdown/governance/baseline/functions.md#hcm-fn-assess-service-offering-readiness) | `proposal:function-assess-service-offering-readiness` | pending_adjudication | Not established |
| [Service Offering Readiness Query](../../../../markdown/governance/baseline/services.md#hcm-sv-service-offering-readiness-query) | `proposal:service-service-offering-readiness-query` | pending_adjudication | Not established |
| [Assemble Purpose-Qualified Research Release](../../../../markdown/governance/baseline/functions.md#hcm-fn-assemble-purpose-qualified-research-release) | `proposal:function-assemble-purpose-qualified-research-release` | current | Authorised Research Release Assembly |
| [Authorised Research Information Export](../../../../markdown/governance/baseline/services.md#hcm-sv-authorised-research-information-export) | `proposal:service-authorised-research-information-export` | current | Not established |

## Change Review consequences

CR-023, CR-025, CR-027 and CR-029 have **Outcome Resolved** and Outcome Reason
referencing the governing Home decision: **Realization does not establish Home
or logical context**. Each historical **Disposition remains On-Hold**. Revision 3
and all original detected evidence remain preserved. Original run-001 now has
10 Resolved and 26 Unresolved Outcomes; all 36 Dispositions remain On-Hold.

CR-022/024/026/028 remain independently Unresolved native Realization proposals;
none is rejected or replaced. CR-035/036 retain their original authored View
evidence and Unresolved Outcomes. Run-002 separately records four new Home edge
proposals and two updated View appearance proposals, all with initial/default
On-Hold Dispositions and Unresolved reconciliation Outcomes. The authored Home
intent is established; endpoint acceptance/identity and execution remain
separately governed. This task supplies no new endpoint identities or additional
Home for the new elements. [Original review outcome revision](../../ingestion/1.0.8/run-001/change-review.md#explicit-home-decision);
[updated-input review](../../ingestion/1.0.8/run-002/change-review.md).

## Preservation, interchange and validation

The normalised [Open Exchange model](../../../exchange/publish/1.0.14/Harmonia-Architecture-1.0.14.xml) retains
**all 1,299 existing elements, 852 native relationships and 1,196 Views**, with
resolved property names, definitions, identities and presentation unchanged.
Its only additional semantic edges are the 208 fixed Home assertions: **1,060
relationships** in total. Realization, Serving, Flow, Composition and all other
valid native edges remain intact. Source/target/direction/Association/Home survive
independent parse/serialization checks. Canonical IDs allocated: **zero**.
Original publications, source capture and authored 1.0.8 inputs remain unchanged.
No automatic naming, folder placement, View placement or Home inheritance is
introduced. Existing navigation is preserved independently of logical Home.

Model SHA-256: `8be6fd16e617d96f7054572dbf896b2384647f16cc0c11b3039137610b9712be`.
[Independent semantic/audit validation](../../ingestion/1.0.8/run-002/validation/validation-results.json)
records 226 passing checks, including original Dispositions,
detected evidence, all native relationships, no guessed Home and source hashes.

| Automated check | Result | Tests/checks | Evidence |
| --- | --- | ---: | --- |
| archimate_tests | PASS | 167 | [log](../../ingestion/1.0.8/run-002/validation/framework-tests-final.log.gz) |
| architecture_tests | PASS | 90 | [log](../../ingestion/1.0.8/run-002/validation/architecture-tests.log.gz) |
| governance_tests | PASS | 72 | [log](../../ingestion/1.0.8/run-002/validation/governance-tests.log.gz) |
| home_validation | PASS | 226 | [log](../../ingestion/1.0.8/run-002/validation/validation-final.log.gz) |
| rads_validation | PASS |  | [log](../../ingestion/1.0.8/run-002/validation/rads-validation.log.gz) |
| release | PASS |  | [log](../../ingestion/1.0.8/run-002/validation/release-check.log.gz) |
| baseline_check | PASS |  | [log](../../ingestion/1.0.8/run-002/validation/baseline-check.log.gz) |

The initial framework run exposed an obsolete test fixture pinned to the current
publication path 1.0.8. The fixture now reads governed release authority while
preserving historical 1.0.8 proofs. Its initial failure log remains audit evidence;
the final complete suite passes. Full source validation retains **2,467 existing
unresolved architectural dimensions** without promoting or erasing them.

The standard publication gate reproduces all 14 generated outputs independently;
the saved comparison is evidence separate from this renderer. Generated reports
read recorded verification and refuse missing, failed or stale evidence.

## Release and stop boundary

The root release authority is **1.0.14**, previous **1.0.13**.
This is an accepted modelling/governance and existing-baseline representation
change, not sign-off of all 1.0.8 proposals. Solution Architect final Archi import,
visual inspection and Business baseline sign-off remain outstanding. Home
cardinality/obligation, exhaustive types, inheritance, automatic naming/navigation
and Information/Application-specific rules remain deliberately undecided.
No downstream ingestion was begun. See [inspection guide](archi-inspection-guide.md).
