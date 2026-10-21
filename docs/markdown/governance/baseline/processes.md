<!-- continuum-source-succession -->
------- Legacy Content - Superseded ------- Start ------
Superseded as an active architectural carrier by the [canonical element continuum](../../../../enterprise-architecture/continuum/documentation/published/1.1.5/README.md).
[Exact source/identity correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json) records this historical generated carrier. Its original bytes and then-current claims are retained below; it is no longer a current authoring source.
---------------------------------------------------------

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Baseline — Processes

[Baseline authority and reading guide](README.md). Currentness does not imply approval. Definitions are reproduced without semantic rewriting. IDs below retain their distinct roles.

## Catalogue

- [Clinical Document Lifecycle Process](#hcm-pr-clinical-document-lifecycle-process) — Canonical; current; placement Established
- [Encounter Lifecycle Process](#hcm-pr-encounter-lifecycle-process) — Canonical; current; placement Established
- [Closed-Loop Order Progression Process](#hcm-pr-closed-loop-order-progression-process) — Canonical; current; placement Established
- [Referral Progression Process](#hcm-pr-referral-progression-process) — Canonical; current; placement Established
- [Governed Person Identity Correction Process](#hcm-pr-governed-person-identity-correction-process) — Canonical; current; placement Established
- [Practitioner Verification Process](#hcm-pr-practitioner-verification-process) — Canonical; current; placement Established
- [Bed Turnover Process](#hcm-pr-bed-turnover-process) — Canonical; current; placement Established
- [Clinic / Operational Queue Progression Process](#hcm-pr-clinic-operational-queue-progression-process) — Canonical; current; placement Established
- [Discharge Progression Process](#hcm-pr-discharge-progression-process) — Canonical; current; placement Established
- [Patient Transport Process](#hcm-pr-patient-transport-process) — Canonical; current; placement Established
- [Theatre Case Progression Process](#hcm-pr-theatre-case-progression-process) — Canonical; current; placement Established
- [Operational Work Progression Process](#hcm-pr-operational-work-progression-process) — Canonical; current; placement Established
- [Assurance Process Design](#hcm-pr-assurance-process-design) — Canonical; current; placement Established
- [Specimen Transport Process](#hcm-pr-specimen-transport-process) — Canonical; pending_adjudication; placement Established
- [Indeterminate Activity Outcome Reconciliation Process](#hcm-pr-indeterminate-activity-outcome-reconciliation-process) — Proposed; pending_adjudication; placement Proposed
- [Synthetic Task Progression Process](#hcm-pr-synthetic-task-progression-process) — Canonical; pending_adjudication; placement Established
- [To Do Progression Process](#hcm-pr-to-do-progression-process) — Canonical; pending_adjudication; placement Established
- [Work Order Progression Process](#hcm-pr-work-order-progression-process) — Canonical; pending_adjudication; placement Established
- [Assurance Process Execution](#hcm-pr-assurance-process-execution) — Canonical; pending_adjudication; placement Established
- [Assurance Process Reporting / Communication](#hcm-pr-assurance-process-reporting-communication) — Canonical; pending_adjudication; placement Established

<a id="hcm-pr-clinical-document-lifecycle-process"></a>

## Clinical Document Lifecycle Process

•
Owning Capability: Clinical Record Administration

•
Process Purpose: Governs the versioning, clinical sign-off, addenda, superseding, and legal status of clinical documents (discharge summaries, specialist letters, advance care directives).

•
State Progression Lifecycle: Draft → Preliminary → Final Signed → Amended / Addended → Superseded → Entered-in-Error   (Scoped illustration; no universal document lifecycle)

•
Key State Dispositions:

•
Draft: Incomplete clinical document saved during consultation.

•
Preliminary: Document authored pending senior registrar or consultant countersignature.

•
Final Signed: Authoritative clinical document legally signed and published.

•
Amended / Addended: Governed supplementary clinical addendum appended to signed document.

•
Superseded: Entire document replaced by a newer revision; prior version retained for audit.

•
Entered-in-Error: Document formally retracted due to wrong patient or invalid content; content struck through with retraction notice.

•
Scope qualification: Final ≠ automatically Final Signed. The described signing/countersigning and legal qualifications remain local requirements, not a universal document lifecycle. Authorship, attestation, approval, authentication, verification, signature, finalisation, authority and legal qualification remain distinct. Amendment, supersession and entered-in-error are not mandatory stages for every document, and consumer processing does not establish originating authority or legal status.

This Process makes authorship and changing document standing intelligible through the locally described review, amendment and withdrawal circumstances. It preserves prior versions rather than treating correction as erasure. The illustration does not require every document to pass through every stage or make finalisation, signing, legal qualification and originating authority equivalent.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:clinical-document-lifecycle-process |
| canonicalId | eacdec16b96844b4963b8a960d1a8666 |
| displayName | Clinical Document Lifecycle Process |
| friendlyName | CareAdministrationEnablement.ClinicalRecordAdministration.ClinicalDocumentationLifecycleAdministration.DocumentVersioningAndSupersession.ClinicalDocumentLifecycleProcess |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ClinicalRecordAdministration.CT3-BEC-ClinicalDocumentationLifecycleAdministration.FEAT-DocumentVersioningAndSupersession.PR-ClinicalDocumentLifecycleProcess |
| Snapshot publication reference | HCM-PR-clinical-document-lifecycle-process |
| ArchiMate semantic ID | id-f6f3ea4454445eb59d7272e9451f4df9 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinical Document Lifecycle Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.PR-CLINICAL-DOCUMENT-LIFECYCLE-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT.CT3-BEC-CLINICAL-RECORD-ADMINISTRATION.PR-CLINICAL-DOCUMENT-LIFECYCLE-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Clinical Record Administration](business-enabling-capabilities.md#hcm-bec-clinical-record-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:clinical-document-lifecycle-process`) and the element's version-qualified Properties.

<a id="hcm-pr-encounter-lifecycle-process"></a>

## Encounter Lifecycle Process

•
Owning Capability: Episode & Encounter Administration

•
Process Purpose: Governs the clinical and administrative lifecycle of an acute, emergency, inpatient, or outpatient encounter between a patient and healthcare services.

•
State Progression Lifecycle: Planned / Booked → Arrived → Triaged / Ingested → Active In-Progress → Discharged → Completed / Encoded   (Scoped illustration; ON_LEAVE relationship unresolved)

•
Key State Dispositions:

•
Planned / Booked: Elective admission or clinic appointment scheduled.

•
Arrived: Patient presents at facility or emergency desk.

•
Triaged / Ingested: Clinical triage category assigned; encounter record activated.

•
Active In-Progress: Inpatient care, bedside monitoring, and clinical orders actively underway.

•
Discharged: Patient clinically discharged; care-place vacated.

•
Completed / Encoded: Clinical documentation finalized, ICD/DRG coding completed, and encounter legally closed.

•
Scope qualification: These completion criteria retain their locally described applicability; they do not establish universal signing, legal closure or originating authority over clinical information. Finalisation, signing and legal qualification remain distinct. The relationship with Strategy ON_LEAVE, including applicability and transitions, remains unresolved.

The encounter lifecycle distinguishes the planned opportunity, presence, active encounter and locally applicable completion context. It supplies an administrative progression frame for care information rather than evidence that every clinical objective has been met. The relation to Strategy's ON_LEAVE state remains unresolved, and local signing or legal-closure descriptions do not become universal completion rules.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:encounter-lifecycle-process |
| canonicalId | df49e2e8bb3748f3abbdd1c28ae1be8f |
| displayName | Encounter Lifecycle Process |
| friendlyName | CareAdministrationEnablement.EpisodeAndEncounterAdministration.EncounterLifecycleManagement.EncounterStateProgression.EncounterLifecycleProcess |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-EpisodeAndEncounterAdministration.CT3-BEC-EncounterLifecycleManagement.FEAT-EncounterStateProgression.PR-EncounterLifecycleProcess |
| Snapshot publication reference | HCM-PR-encounter-lifecycle-process |
| ArchiMate semantic ID | id-0e94558f9afd57259e6017e0fea3ec07 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Encounter Lifecycle Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.PR-ENCOUNTER-LIFECYCLE-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.PR-ENCOUNTER-LIFECYCLE-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Episode & Encounter Administration](business-enabling-capabilities.md#hcm-bec-episode-encounter-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:encounter-lifecycle-process`) and the element's version-qualified Properties.

<a id="hcm-pr-closed-loop-order-progression-process"></a>

## Closed-Loop Order Progression Process

•
Owning Capability: Order Administration

•
Process Purpose: Governs the rigorous, closed-loop tracking of diagnostic pathology, radiology, and procedural orders from requisition to result correlation, preventing dropped or unfulfilled investigations.

•
Scope qualification: These examples describe a diagnostic/procedural progression and do not exclude medication from the broader Order Administration responsibility. Specimen and result checkpoints are scoped. The signed requisition/report requirements remain within this illustrated scope; signing is not universally equivalent to finalisation or authority. Result consumption/binding does not define originating validity, authority, authorship or legal status.

•
State Progression Lifecycle: Requisition Placed → Order Dispatched → Specimen Collected / Scheduled → In-Execution → Preliminary Result Bound → Final Result Bound → Closed / Verified   (Scoped diagnostic/procedural illustration)

•
Key State Dispositions:

•
Requisition Placed: Electronic order created and signed by requesting clinician.

•
Order Dispatched: Order routed and accepted by performing diagnostic service. This local compound checkpoint retains routing and recipient acceptance as distinct facts; dispatch alone establishes neither receipt nor acceptance. Behaviour Received is not equated with this checkpoint without evidence.

•
Specimen Collected / Scheduled: Bio-specimen collected or imaging appointment booked.

•
In-Execution: Laboratory analysis or diagnostic scanning underway.

•
Preliminary Result Bound: Critical or interim observations published and linked to order.

•
Final Result Bound: Authoritative diagnostic report signed and bound to order.

•
Closed / Verified: Requesting clinician acknowledges result; order closed. This Order-closure checkpoint does not establish diagnostic-content verification, approval or clinical incorporation.

Closed-loop progression preserves the connection between the requisition, performing context and returned result so that a request does not disappear after dispatch. The diagnostic/procedural illustration includes specimen and report checkpoints without excluding medication from the broader owning capability. Dispatch, recipient acceptance, result binding and requester closure remain distinct facts even where a source checkpoint combines them.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:closed-loop-order-progression-process |
| canonicalId | e19a7c9594a4401e92450e4c31598982 |
| displayName | Closed-Loop Order Progression Process |
| friendlyName | CareAdministrationEnablement.OrderAdministration.ClosedLoopOrderProgression.OrderClosedLoopProgressionTracking.ClosedLoopOrderProgressionProcess |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-OrderAdministration.CT3-BEC-ClosedLoopOrderProgression.FEAT-OrderClosedLoopProgressionTracking.PR-ClosedLoopOrderProgressionProcess |
| Snapshot publication reference | HCM-PR-closed-loop-order-progression-process |
| ArchiMate semantic ID | id-a5695708ce5d5c22a34782d8381612bb |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Closed-Loop Order Progression Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.PR-CLOSED-LOOP-ORDER-PROGRESSION-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT.CT3-BEC-ORDER-ADMINISTRATION.PR-CLOSED-LOOP-ORDER-PROGRESSION-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Order Administration](business-enabling-capabilities.md#hcm-bec-order-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:closed-loop-order-progression-process`) and the element's version-qualified Properties.

<a id="hcm-pr-referral-progression-process"></a>

## Referral Progression Process

•
Owning Capability: Referral Administration

•
Process Purpose: Governs the end-to-end operational progression of an incoming clinical referral from receipt through specialist clinical triage, booking, and final service acceptance.

•
Scope qualification: This illustrates a specialist booking/attendance pathway; Referral does not universally require acceptance, scheduling, attendance or discharge, or establish delivery, responsibility actually assumed or Transfer of Care. Decline, rejection and redirection remain valid dispositions without invented transition paths. Accepted / Waitlisted is a local compound checkpoint, not universal equivalence between acceptance and waitlisting.

•
State Progression Lifecycle: Submitted → Intake Validated → Clinically Triaged → Accepted / Waitlisted → Scheduled → Consultation Attended → Discharged / Rejected   (Illustrative specialist pathway; dispositions vary)

•
Key State Dispositions:

•
Submitted: Electronic referral received from GP or external facility.

•
Intake Validated: Administrative validation of patient details, mandatory fields, and tests.

•
Clinically Triaged: Senior clinician assigns urgency category (e.g., Cat 1 urgent within 30 days).

•
Accepted / Waitlisted: Referral accepted and placed onto the specialty waitlist.

•
Scheduled: Outpatient appointment allocated and patient notified.

•
Consultation Attended: Patient seen; initial specialist assessment completed.

•
Discharged / Rejected: Referral completed and discharged back to primary care, or formally rejected with clinical rationale.

This Process illustrates one specialist pathway through validation, triage and booking. It makes that pathway's progression visible without imposing it on every referral: rejection, decline or redirection can be legitimate dispositions, and a request need not universally lead to attendance. Neither a transmitted referral nor the local compound acceptance/waitlist checkpoint establishes actual responsibility transfer.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:referral-progression-process |
| canonicalId | cfb714e9320647ffa1dffc80dddd01d9 |
| displayName | Referral Progression Process |
| friendlyName | CareAdministrationEnablement.ReferralAdministration.ReferralIntakeAndProgression.ReferralStatusAndOutcomeTracking.ReferralProgressionProcess |
| canonicalName | CT1-BEC-CareAdministrationEnablement.CT2-BEC-ReferralAdministration.CT3-BEC-ReferralIntakeAndProgression.FEAT-ReferralStatusAndOutcomeTracking.PR-ReferralProgressionProcess |
| Snapshot publication reference | HCM-PR-referral-progression-process |
| ArchiMate semantic ID | id-c1a4b8f0f5275f89bec405d882a45d07 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Referral Progression Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.PR-REFERRAL-PROGRESSION-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT.CT3-BEC-REFERRAL-ADMINISTRATION.PR-REFERRAL-PROGRESSION-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Referral Administration](business-enabling-capabilities.md#hcm-bec-referral-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:referral-progression-process`) and the element's version-qualified Properties.

<a id="hcm-pr-governed-person-identity-correction-process"></a>

## Governed Person Identity Correction Process

•
Owning Capability: Person Identity (under Client Administration)

•
Process Purpose: Processes externally authoritative identity corrections and merge/unlink outcomes, applies their consequences to Harmonia-managed identity information and identifier associations, and communicates resulting changes with an audit history.

•
Authority boundary: Person correction/merge decisions originate externally. Internal triage, evidence verification and approval concern applicability and governed processing of that authoritative outcome, not person matching, golden-record determination, master-person selection or originating merge adjudication.

•
State Progression Lifecycle: Correction Requested → HIM Triage → Evidence Verified → Correction Approved → Merge/Unlink Executed → Change Broadcasted → Closed

•
Key State Dispositions:

•
Correction Requested: Externally authoritative correction or merge/unlink outcome received for governed application.

•
HIM Triage: Health Information Manager reviews the received decision/outcome and its applicability.

•
Evidence Verified: Authoritative source decision and supporting provenance/documentation verified.

•
Correction Approved: Governed application of the externally authoritative outcome approved; this does not originate a person-merge decision.

•
Merge/Unlink Executed: External merge/unlink consequences applied to Harmonia's managed identifier associations and audit history.

•
Change Broadcasted: Downstream clinical systems notified of merged or rectified identity.

This Process governs the application of an externally authoritative correction rather than originating a person-merge judgement. Triage, evidence verification and approval establish that the received outcome should be applied to Harmonia's associations. The progression and change communication preserve why the managed identity account changed without making internal approval a master-person decision.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:governed-person-identity-correction-process |
| canonicalId | 72f3c63da45d44868579a3bb15d1568c |
| displayName | Governed Person Identity Correction Process |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.GovernedIdentityCorrection.GovernedPersonIdentityCorrectionProcess |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-GovernedIdentityCorrection.PR-GovernedPersonIdentityCorrectionProcess |
| Snapshot publication reference | HCM-PR-governed-person-identity-correction-process |
| ArchiMate semantic ID | id-46e16f772bab5982ae8fae3daa900739 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Governed Person Identity Correction Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.PR-GOVERNED-PERSON-IDENTITY-CORRECTION-PROCESS |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Person Identity Administration](business-enabling-capabilities.md#hcm-bec-person-identity). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:governed-person-identity-correction-process`) and the element's version-qualified Properties.

<a id="hcm-pr-practitioner-verification-process"></a>

## Practitioner Verification Process

•
Owning Capability: Provider Administration

•
Process Purpose: Governs the lifecycle of validating, certifying, and periodically re-verifying a healthcare practitioner's professional credentials, registration status, and clinical scope of practice.

•
State Progression Lifecycle: Verification Initiated → Regulatory Registry Queried → Credentials Validated → Privileges Endorsed → Active Verified → Expired / Suspended

•
Key State Dispositions:

•
Verification Initiated: New practitioner registration or periodic re-credentialing triggered.

•
Regulatory Registry Queried: Real-time query executed against national registry (e.g., AHPRA).

•
Credentials Validated: Primary qualifications, specialist endorsements, and sanctions checked.

•
Privileges Endorsed: Health service medical administration endorses admitting/procedural scope.

•
Active Verified: Practitioner verified for clinical ordering and electronic communication.

•
Expired / Suspended: Verification lapses or regulatory sanction triggers privilege suspension.

Verification progresses from checking the recognised professional source to the locally applicable endorsement and continuing verification status. The distinction matters because a nationally identifiable practitioner is not automatically privileged for every service activity. The illustrated progression retains the health service's endorsement responsibility rather than letting directory ingestion grant clinical authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:practitioner-verification-process |
| canonicalId | 32a3e1f948474d3aa77ca71bfa515f58 |
| displayName | Practitioner Verification Process |
| friendlyName | EntityInformationEnablement.ProviderAdministration.PractitionerProfileAdministration.NationalPractitionerVerification.PractitionerVerificationProcess |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ProviderAdministration.CT3-BEC-PractitionerProfileAdministration.FEAT-NationalPractitionerVerification.PR-PractitionerVerificationProcess |
| Snapshot publication reference | HCM-PR-practitioner-verification-process |
| ArchiMate semantic ID | id-666fffcc85fc5efea1d96fc35483a865 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Practitioner Verification Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.PR-PRACTITIONER-VERIFICATION-PROCESS |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Provider Administration](business-enabling-capabilities.md#hcm-bec-provider-administration). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:practitioner-verification-process`) and the element's version-qualified Properties.

<a id="hcm-pr-bed-turnover-process"></a>

## Bed Turnover Process

•
Owning Capability: Bed & Care-Place Management

•
Process Purpose: Governs the rapid physical turnover, cleaning, sanitisation, and re-allocation of inpatient and emergency care-places.

•
State Progression Lifecycle: Bed Vacated → Cleaning Dispatched → Sanitisation In-Progress → Terminal Cleaning Verified → Bed Ready / Available → Bed Allocated / Occupied

•
Key State Dispositions:

•
Bed Vacated: Prior patient discharged or transferred.

•
Cleaning Dispatched: Cleaning job allocated to environmental services team.

•
Sanitisation In-Progress: Ward cleaning or isolation decontamination underway.

•
Terminal Cleaning Verified: Ward coordinator or supervisor verifies care-place readiness.

•
Bed Ready / Available: Bed marked available for incoming patient allocation.

•
Bed Allocated / Occupied: Incoming patient allocated and physically placed in bed.

Bed turnover connects the release of a care place with the operational work needed to return it to use. Its progression preserves the difference between vacancy, cleaning and readiness. A place can be empty while work remains outstanding, and a completed turnover does not by itself prove that the service has capacity for the next patient.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:bed-turnover-process |
| canonicalId | 69f74289926942f4baed4acf4c654a3b |
| displayName | Bed Turnover Process |
| friendlyName | HealthServiceOperationsEnablement.BedAndCarePlaceManagement.BedTurnoverProcess |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-BedAndCarePlaceManagement.PR-BedTurnoverProcess |
| Snapshot publication reference | HCM-PR-bed-turnover-process |
| ArchiMate semantic ID | id-3e34095e9c8b5a16969eb265501e228b |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Bed Turnover Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.PR-BED-TURNOVER-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-BED-CARE-PLACE-MANAGEMENT.PR-BED-TURNOVER-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Bed & Care-Place Management](business-enabling-capabilities.md#hcm-bec-bed-care-place-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:bed-turnover-process`) and the element's version-qualified Properties.

<a id="hcm-pr-clinic-operational-queue-progression-process"></a>

## Clinic / Operational Queue Progression Process

•
Owning Capability: Clinic & Practice Operations

•
Process Purpose: Governs the movement and state tracking of outpatients through an ambulatory specialty clinic session.

•
State Progression Lifecycle: Scheduled → Patient Arrived → Checked-In → Roomed / Pre-Consult → Consultation In-Progress → Consultation Concluded → Departed

•
Key State Dispositions:

•
Scheduled: Appointment booked on session list.

•
Patient Arrived: Patient enters waiting area.

•
Checked-In: Reception verifies demographics and marks patient ready.

•
Roomed / Pre-Consult: Nursing staff record baseline vitals and room patient.

•
Consultation In-Progress: Clinician actively conducting consultation.

•
Consultation Concluded: Clinical discussion finished; follow-up orders placed.

•
Departed: Patient checks out and departs clinic facility.

This Process follows an individual's operational passage through the clinic. Waiting, rooming, consultation and departure have different implications for flow and communication, while the clinic's overall session has its own state. The progression is therefore patient-specific operational context rather than a complete account of clinic capacity or clinical priority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:clinic--operational-queue-progression-process |
| canonicalId | 319d6956276a4cdeb43e350e1b01d078 |
| displayName | Clinic / Operational Queue Progression Process |
| friendlyName | HealthServiceOperationsEnablement.ClinicAndPracticeOperations.SessionAndQueueCoordination.OperationalQueueProgression.ClinicOperationalQueueProgressionProcess |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicAndPracticeOperations.CT3-BEC-SessionAndQueueCoordination.FEAT-OperationalQueueProgression.PR-ClinicOperationalQueueProgressionProcess |
| Snapshot publication reference | HCM-PR-clinic-operational-queue-progression-process |
| ArchiMate semantic ID | id-6d1d76ff79ee5bc0856e025561f5dc85 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Clinic / Operational Queue Progression Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.PR-CLINIC-OPERATIONAL-QUEUE-PROGRESSION-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-CLINIC-PRACTICE-OPERATIONS.PR-CLINIC-OPERATIONAL-QUEUE-PROGRESSION-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Clinic & Practice Operations](business-enabling-capabilities.md#hcm-bec-clinic-practice-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:clinic--operational-queue-progression-process`) and the element's version-qualified Properties.

<a id="hcm-pr-discharge-progression-process"></a>

## Discharge Progression Process

•
Owning Capability: Discharge Management

•
Process Purpose: Governs the multidisciplinary coordination of patient discharge planning, pharmacy reconciliation, transport, and community handover.

•
State Progression Lifecycle: Discharge Planning Initiated → Clinical Readiness Confirmed → Medications Reconciled → Transport & Services Booked → Discharge Summary Signed → Physically Departed   (Illustrative preparation; publication, authorisation and exit distinct)

•
Key State Dispositions:

•
Discharge Planning Initiated: Estimated Date of Discharge (EDD) set on admission.

•
Clinical Readiness Confirmed: Attending medical team declares patient fit for discharge.

•
Medications Reconciled: Hospital pharmacy reconciles and dispenses discharge medications.

•
Transport & Services Booked: Community nursing, home equipment, and patient transport booked.

•
Discharge Summary Signed: Applicable signed summary is available/published and may be communicated before departure where the Business behaviour requires it; signing, finalisation, publication and discharge authorisation remain distinct.

•
Physically Departed: Confirmed physical exit triggers departure notifications, updated bed-state communication and finalised-summary dispatch to external care providers under FEAT-HSO-29; care-place turnover follows its own responsibility.

•
Timing reconciliation: Preparation, information availability/publication, applicable discharge authorisation and physical exit are distinct. Earlier preparation/communication is permitted; it does not realise a Feature triggered by confirmed exit. This illustrated local pathway establishes neither universal publication timing nor a universal discharge state machine, and adds no inferred publication event or authorisation state.

•
Preserved uncertainty: Summary signing and finalisation are not universal equivalents. Detailed applicability, discharge-authority workflows and timing beyond the established preparation/confirmed-exit distinction remain unestablished.

Discharge progression coordinates the confirmations and practical requirements around leaving care. Clinical readiness, medications, destination arrangements and transport are related but distinct contributions, and the source progression retains its stated local applicability. Operational coordination does not originate the clinical discharge decision or prove that another provider assumed responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:discharge-progression-process |
| canonicalId | 7165955d9c454175a04b9ad00293f011 |
| displayName | Discharge Progression Process |
| friendlyName | HealthServiceOperationsEnablement.DischargeManagement.DischargeProgressionProcess |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-DischargeManagement.PR-DischargeProgressionProcess |
| Snapshot publication reference | HCM-PR-discharge-progression-process |
| ArchiMate semantic ID | id-a3b050eb480a522ba73d4237437255e6 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Discharge Progression Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-DISCHARGE-MANAGEMENT.PR-DISCHARGE-PROGRESSION-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-DISCHARGE-MANAGEMENT.PR-DISCHARGE-PROGRESSION-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Discharge Management](business-enabling-capabilities.md#hcm-bec-discharge-management). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:discharge-progression-process`) and the element's version-qualified Properties.

<a id="hcm-pr-patient-transport-process"></a>

## Patient Transport Process

•
Owning Capability: Patient Transport

•
Process Purpose: Governs the physical portering and transport of patients between hospital departments, wards, diagnostic suites, and external facilities.

•
State Progression Lifecycle: Transport Requested → Porter Dispatched → Patient Collected → In-Transit → Delivered at Destination → Clinical Handover Complete

•
Key State Dispositions:

•
Transport Requested: Clinical unit books wheelchair, bed, or ambulance transport.

•
Porter Dispatched: Wardsperson assigned and en route to pickup location.

•
Patient Collected: Patient identity verified and transfer initiated.

•
In-Transit: Patient actively moving through facility corridors or ambulance transit.

•
Delivered at Destination: Patient arrives at target diagnostic suite or ward.

•
Clinical Handover Complete: Safe custody and clinical briefing transferred to receiving staff.

Patient transport progression records the physical journey from a movement request through departure, transit and destination arrival. Its checkpoints allow operational participants to follow the journey with relevant mobility context intact. Arrival closes a movement concern only under its applicable scope; it does not establish clinical handover or destination acceptance of care responsibility.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:patient-transport-process |
| canonicalId | f2e58fd18cd94b0faaa364d3890a7c9e |
| displayName | Patient Transport Process |
| friendlyName | HealthServiceOperationsEnablement.PatientTransport.PatientTransportProcess |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-PatientTransport.PR-PatientTransportProcess |
| Snapshot publication reference | HCM-PR-patient-transport-process |
| ArchiMate semantic ID | id-f072345cdf64534ea66938792b6141bc |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Patient Transport Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-PATIENT-TRANSPORT.PR-PATIENT-TRANSPORT-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-PATIENT-TRANSPORT.PR-PATIENT-TRANSPORT-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Patient Transport](business-enabling-capabilities.md#hcm-bec-patient-transport). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:patient-transport-process`) and the element's version-qualified Properties.

<a id="hcm-pr-theatre-case-progression-process"></a>

## Theatre Case Progression Process

•
Owning Capability: Theatre Operations

•
Process Purpose: Governs the perioperative operational milestones of a surgical procedure within the operating theatre suite.

•
State Progression Lifecycle: Case Called → In Anaesthetic Bay → Anaesthesia Commenced → Patient in Theatre → Knife-to-Skin → Procedure Finished → In PACU Recovery → Ward Handover Complete

•
Key State Dispositions:

•
Case Called: Theatre team requests ward to send patient for surgery.

•
In Anaesthetic Bay: Patient arrives in theatre holding and identity/consent verified.

•
Anaesthesia Commenced: Anaesthetic induction underway.

•
Patient in Theatre: Patient transferred onto operating table.

•
Knife-to-Skin: Surgical incision initiated (operative start).

•
Procedure Finished: Wound closure completed and dressings applied.

•
In PACU Recovery: Patient monitored in post-anaesthesia care unit.

•
Ward Handover Complete: Post-operative clinical handover to inpatient ward completed.

Theatre case progression follows reported milestones of the surgical encounter so that supporting participants can coordinate around the actual situation. The state account concerns what the clinical service reports has occurred. It does not make the integration mechanism the actor administering anaesthesia, performing surgery or managing clinical handover.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:theatre-case-progression-process |
| canonicalId | 8ad97120b5b54a31a64a4522e6bc9fd1 |
| displayName | Theatre Case Progression Process |
| friendlyName | HealthServiceOperationsEnablement.TheatreOperations.TheatreCaseProgressionProcess |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-TheatreOperations.PR-TheatreCaseProgressionProcess |
| Snapshot publication reference | HCM-PR-theatre-case-progression-process |
| ArchiMate semantic ID | id-938712df45315334bb92ff456431556b |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Theatre Case Progression Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS.PR-THEATRE-CASE-PROGRESSION-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT.CT3-BEC-THEATRE-OPERATIONS.PR-THEATRE-CASE-PROGRESSION-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Theatre Operations](business-enabling-capabilities.md#hcm-bec-theatre-operations). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:theatre-case-progression-process`) and the element's version-qualified Properties.

<a id="hcm-pr-operational-work-progression-process"></a>

## Operational Work Progression Process

•
Owning Capability: Work Allocation & Dispatch

•
Process Purpose: Governs the dispatch, assignment, execution, and completion of facility operational jobs (e.g., equipment moves, waste removal, linen supply).

•
State Progression Lifecycle: Work Requested → Queued in Dispatch → Assigned to Worker → Job Accepted → Work In-Progress → Work Completed / Aborted

•
Key State Dispositions:

•
Work Requested: Facility support task submitted by clinical unit.

•
Queued in Dispatch: Job prioritized in operational queue.

•
Assigned to Worker: Job allocated to specific staff member or team.

•
Job Accepted: Worker acknowledges dispatch on mobile device.

•
Work In-Progress: Task actively being executed.

•
Work Completed / Aborted: Job marked finished or formally cancelled with reason.

Operational work progression makes the path from a recognised support requirement to assigned and reported work explicit. Acknowledgement and activity progress help distinguish a dispatch from actual performance and expose impediments requiring operational attention. This responsibility remains bounded to authorised support work rather than ownership of the clinician's worklist.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:operational-work-progression-process |
| canonicalId | 5ebba522194b4de5a5b3963e855d712d |
| displayName | Operational Work Progression Process |
| friendlyName | HealthServiceOperationsEnablement.WorkAllocationAndDispatch.OperationalWorkAssignmentAndDispatchCoordination.WorkProgressOversightAndEscalation.OperationalWorkProgressionProcess |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-WorkAllocationAndDispatch.CT3-BEC-OperationalWorkAssignmentAndDispatchCoordination.FEAT-WorkProgressOversightAndEscalation.PR-OperationalWorkProgressionProcess |
| Snapshot publication reference | HCM-PR-operational-work-progression-process |
| ArchiMate semantic ID | id-64bf723fe0985e75967cf435d0f49fe1 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operational Work Progression Process |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.PR-OPERATIONAL-WORK-PROGRESSION-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION.CT3-BEC-WORK-ALLOCATION-DISPATCH.PR-OPERATIONAL-WORK-PROGRESSION-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |

Responsibility/framing context: [Work Allocation & Dispatch](business-enabling-capabilities.md#hcm-bec-work-allocation-dispatch). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:operational-work-progression-process`) and the element's version-qualified Properties.

<a id="hcm-pr-assurance-process-design"></a>

## Assurance Process Design

•
Canonical Name: Assurance Process Design.

•
Owning Capability: Assurance Design (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md#assurance-design).

•
Process Purpose: Establishes and maintains how satisfaction of a governed requirement, constraint or expected behaviour is to be assured.

•
Principal Performing Role: Service Assurance Modeller (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/actors-roles/roles.md#service-assurance-modeller).

•
Participating Functions: Design Service Assurance (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#design-service-assurance) and Manage Assurance Criteria (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#manage-assurance-criteria). Manage Assurance Criteria retains ownership within Assurance Criteria Management; its participation does not subsume that Capability's criteria lifecycle or transfer its information responsibility to Assurance Design.

•
Conceptual Output: Assurance Definition (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#42-assurance-definition--conceptual-modelling-output), retaining its conceptual Business Architecture output/boundary treatment.

•
Boundary: Governance Authority establishes what is authoritative. Design does not grant Service Assurance Modeller approval authority. Detailed Assurance Definition lifecycle, approval/publication workflows, criteria lifecycle and information modelling remain unresolved. Participation of the two Functions does not establish a cross-capability lifecycle or a new exchange/Service between their owners.

Assurance design establishes a reusable basis for how satisfaction will be evaluated, rather than running the evaluation itself. Criteria-management participation retains its separate owner and does not create an exchange Service or a shared criteria lifecycle. An Assurance Definition can be reused, so each execution need not imply a freshly performed Design Process.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:assurance-process-design |
| canonicalId | aec3177dcdd84961814f2ba4f688ad33 |
| displayName | Assurance Process Design |
| friendlyName | IndependentAssuranceEnablement.AssuranceSpecification.AssuranceDesign.AssuranceProcessDesign |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-AssuranceSpecification.CT3-BEC-AssuranceDesign.PR-AssuranceProcessDesign |
| Snapshot publication reference | HCM-PR-assurance-process-design |
| ArchiMate semantic ID | id-68c2c17a966b5a3587ae2e315c9dd7e3 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Assurance Process Design |
| Record currentness | current |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT.CT3-BEC-ASSURANCE-DESIGN.PR-ASSURANCE-PROCESS-DESIGN |
| Unresolved dimensions | ["feature_association","contextual_view"] |

Responsibility/framing context: [Assurance Design](business-enabling-capabilities.md#hcm-bec-assurance-design). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:assurance-process-design`) and the element's version-qualified Properties.

<a id="hcm-pr-specimen-transport-process"></a>

## Specimen Transport Process

•
Owning Capability: Clinical Logistics Coordination

•
Process Purpose: Governs the physical courier chain-of-custody, transport, and delivery of pathology bio-specimens, blood products, and surgical biopsies.

•
State Progression Lifecycle: Specimen Packaged → Courier Dispatched → Chain-of-Custody Signed → In-Transit (Monitored) → Lab Ingress Received → Specimen Accepted

•
Key State Dispositions:

•
Specimen Packaged: Clinical unit packages bio-sample with barcode manifest.

•
Courier Dispatched: Courier route assigned for pickup.

•
Chain-of-Custody Signed: Courier scans barcode and accepts legal custody.

•
In-Transit (Monitored): Specimen in transit under temperature/time constraints.

•
Lab Ingress Received: Central pathology specimen reception logs physical receipt.

•
Specimen Accepted: Specimen integrity verified and passed to analytical track.

---

## History

### Process Overview — predecessor explanation

Specimen transport preserves the movement and custody account connecting collection context with laboratory receipt. It makes the journey of the material explainable rather than reducing it to a sent/received pair. Delivery remains distinct from specimen acceptance, diagnostic performance and the eventual professional interpretation of its evidence.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:specimen-transport-process |
| canonicalId | 11bb000294fc44a8af295f4682e2fe6c |
| displayName | Specimen Transport Process |
| friendlyName | HealthServiceOperationsEnablement.ClinicalLogisticsCoordination.SpecimenTransportProcess |
| canonicalName | CT1-BEC-HealthServiceOperationsEnablement.CT2-BEC-ClinicalLogisticsCoordination.PR-SpecimenTransportProcess |
| Snapshot publication reference | HCM-PR-specimen-transport-process |
| ArchiMate semantic ID | id-3e74a227467d5546b1222b8953f47396 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Specimen Transport Process |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-LOGISTICS-COORDINATION.PR-SPECIMEN-TRANSPORT-PROCESS |
| Structural aliases | ["CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION.CT3-BEC-CLINICAL-LOGISTICS-COORDINATION.PR-SPECIMEN-TRANSPORT-PROCESS"] |
| Unresolved dimensions | ["feature_association"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: The Canonical Process includes blood products as well as bio-specimens and surgical biopsies. Whether the blood-product wording denotes diagnostic specimens or transported health materiel is unadjudicated; the original Process is preserved without narrowing it. |

Responsibility/framing context: [Clinical Logistics Coordination](business-enabling-capabilities.md#hcm-bec-clinical-logistics-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:specimen-transport-process`) and the element's version-qualified Properties.

<a id="hcm-pr-indeterminate-activity-outcome-reconciliation-process"></a>

## Indeterminate Activity Outcome Reconciliation Process

Order the observation, effect qualification and authorised disposition of a materially indeterminate operational outcome.

**Overview**

First inspect the governing handoff and available authoritative state; then reconcile observed effects; finally authorise a safe continuation, retry or retained uncertainty. Each participating Function keeps its responsibility owner.

**Boundary**

Not a universal lifecycle or automatic retry loop. A safe disposition may remain unresolved.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:process-indeterminate-activity-outcome-reconciliation-process |
| canonicalId | 96baf3721ce94397a11486184d3df7cf |
| displayName | Indeterminate Activity Outcome Reconciliation Process |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.IndeterminateActivityOutcomeReconciliationProcess |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.PR-IndeterminateActivityOutcomeReconciliationProcess |
| Snapshot publication reference | HCM-PR-indeterminate-activity-outcome-reconciliation-process |
| ArchiMate semantic ID | id-4efafd6ac18d55da8bafd3658c00e18c |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | pending_adjudication |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.PR-INDETERMINATE-ACTIVITY-OUTCOME-RECONCILIATION-PROCESS |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Proposed placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:process-indeterminate-activity-outcome-reconciliation-process`) and the element's version-qualified Properties.

<a id="hcm-pr-synthetic-task-progression-process"></a>

## Synthetic Task Progression Process

•
Owning Capability: Workflow & Activity Coordination

•
Activity Archetype: Non-Human Executable Work — automated system workflows, batch syndications, or policy evaluation tasks.

•
State Progression Lifecycle:

RAD diagram: textual source notation.
\begin{Verbatim}[breaklines,fontsize=
]
graph LR
    D["Draft / Scheduled"] --> E["Executing"]
    E --> W["Awaiting-Dependency"]
    W --> C["Completed"]
    F["Progression Stalled / Failed<br/>Outcome/position unresolved"]
    NOTE["Illustrative progression, not a normative transition model.<br/>No Completed-to-Stalled/Failed transition is established."]
    classDef note fill:#fff8dc,stroke:#8a6d3b,stroke-dasharray:5 5;
    class NOTE,F note;
\end{Verbatim}

•
Key State Dispositions:

•
Draft / Scheduled: Automated task instantiated and scheduled for processing.

•
Executing: System actively processing task.

•
Awaiting-Dependency: Paused awaiting asynchronous external reply or event.

•
Completed: Automated execution succeeded and output artifact produced.

•
Progression Stalled / Failed: Transient fault triggers scheduled recovery attempt; permanent failure records failure evidence.

•
Preserved uncertainty: Whether stalled/failed is an alternative outcome, reopening/post-completion behaviour or a presentation defect remains unresolved. Its disconnected diagram node does not make success and failure equivalent or establish post-completion transitions.

---

## History

### Process Overview — predecessor explanation

Synthetic Task progression concerns automated non-human execution, including waiting for a dependency and producing an output. It must remain distinct from human work orders and external clinical task lists. The disconnected stalled/failed outcome in the illustration leaves its relation to the success path unresolved; no reopening or post-completion failure transition is inferred.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:synthetic-task-progression-process |
| canonicalId | 618acdb38fc74952ab360fdde4f7d865 |
| displayName | Synthetic Task Progression Process |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.SyntheticTaskProgressionProcess |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.PR-SyntheticTaskProgressionProcess |
| Snapshot publication reference | HCM-PR-synthetic-task-progression-process |
| ArchiMate semantic ID | id-f9fa8fe0918f535893a879de3285ea40 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Synthetic Task Progression Process |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.PR-SYNTHETIC-TASK-PROGRESSION-PROCESS |
| Unresolved dimensions | ["feature_association"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:synthetic-task-progression-process`) and the element's version-qualified Properties.

<a id="hcm-pr-to-do-progression-process"></a>

## To Do Progression Process

•
Owning Capability: Workflow & Activity Coordination

•
Activity Archetype: Human Reviewing / Updating / Deciding — clinical review, document countersignature, or administrative authorization task.

•
State Progression Lifecycle:

RAD diagram: textual source notation.
\begin{Verbatim}[breaklines,fontsize=
]
graph LR
    I["Issued"] --> B["In-Inbox"]
    B --> R["Opened / Under-Review"]
    R --> A["Actioned / Decided"]
    D["Dismissed / Delegated<br/>Disposition/position unresolved"]
    NOTE["Illustrative progression, not a normative transition model.<br/>Relationship of dismissal/delegation to action/decision is unestablished."]
    classDef note fill:#fff8dc,stroke:#8a6d3b,stroke-dasharray:5 5;
    class NOTE,D note;
\end{Verbatim}

•
Key State Dispositions:

•
Issued: To Do generated (e.g., review abnormal potassium result).

•
In-Inbox: Displayed in practitioner's actionable task list.

•
Opened / Under-Review: Clinician inspecting relevant clinical context.

•
Actioned / Decided: Decision executed (e.g., signed off, order placed, acknowledged).

•
Dismissed / Delegated: Task reassigned to registrar or dismissed with comment.

•
Preserved uncertainty: Whether dismissal/delegation is an alternative disposition, a subsequent action or a scoped variant remains unresolved; its disconnected diagram node establishes no transition or position in the illustrative progression.

---

## History

### Process Overview — predecessor explanation

To Do progression coordinates a human review or decision item. Completion records progression of the item, while the significance and authority of the resulting judgement depend on the governed activity. A review being marked complete therefore does not independently establish clinical adequacy, signing authority or an assurance conclusion.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:to-do-progression-process |
| canonicalId | c5e4c3076b094c98a628179b91ae03d0 |
| displayName | To Do Progression Process |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.ToDoProgressionProcess |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.PR-ToDoProgressionProcess |
| Snapshot publication reference | HCM-PR-to-do-progression-process |
| ArchiMate semantic ID | id-731a4a1d8d4c56c4a355c892d0c58311 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | To Do Progression Process |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.PR-TO-DO-PROGRESSION-PROCESS |
| Unresolved dimensions | ["feature_association"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:to-do-progression-process`) and the element's version-qualified Properties.

<a id="hcm-pr-work-order-progression-process"></a>

## Work Order Progression Process

•
Owning Capability: Workflow & Activity Coordination

•
Activity Archetype: Human Doing — physical or operational task executed by healthcare staff.

•
State Progression Lifecycle: Created → Ready → Dispatched → Accepted → In-Progress → Completed / Failed / Cancelled

•
Key State Dispositions:

•
Created: Work order instantiated with scope, target, and required skills.

•
Ready: Preconditions satisfied; available for assignment.

•
Dispatched: Sent to target individual or worker group queue.

•
Accepted: Worker claims task.

•
In-Progress: Execution underway.

•
Completed: Task finished with recorded completion telemetry.

---

## History

### Process Overview — predecessor explanation

Work Order progression describes a human undertaking physical or operational activity. Assignment, acknowledgement and outcome make the undertaking accountable without collapsing dispatch into completion. Its generic work meaning can support different operational contexts while leaving their domain authority and the clinical-work boundary intact.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:work-order-progression-process |
| canonicalId | 73fbbe4e027b4805809fad430e791eba |
| displayName | Work Order Progression Process |
| friendlyName | HumanCollaborationAndWorkEnablement.OperationalActivityAndTemporalEnablement.WorkflowAndActivityCoordination.WorkOrderProgressionProcess |
| canonicalName | CT1-BEC-HumanCollaborationAndWorkEnablement.CT2-BEC-OperationalActivityAndTemporalEnablement.CT3-BEC-WorkflowAndActivityCoordination.PR-WorkOrderProgressionProcess |
| Snapshot publication reference | HCM-PR-work-order-progression-process |
| ArchiMate semantic ID | id-dfebbd3f1e345317a8ca625929b68676 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Work Order Progression Process |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.PR-WORK-ORDER-PROGRESSION-PROCESS |
| Unresolved dimensions | ["feature_association"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge. |

Responsibility/framing context: [Workflow & Activity Coordination](business-enabling-capabilities.md#hcm-bec-workflow-activity-coordination). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:work-order-progression-process`) and the element's version-qualified Properties.

<a id="hcm-pr-assurance-process-execution"></a>

## Assurance Process Execution

•
Canonical Name: Assurance Process Execution.

•
Owning Capability: Governed Assurance (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md#governed-assurance).

•
Process Purpose: Performs defined assurance for an identifiable governed subject in accordance with an applicable Assurance Definition and establishes an Assurance Finding / Conclusion from applicable trustworthy evidence.

•
Principal Performing Role: Service Guardian (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/actors-roles/roles.md#service-guardian).

•
Participating Functions: Establish Assurance Context (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#establish-assurance-context), Assess Assurance Evidence (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#assess-assurance-evidence) and Adjudicate Assurance Assessment (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#adjudicate-assurance-assessment), all retaining Governed Assurance ownership.

•
Conceptual Basis and Output: Applicable Assurance Definition plus identifiable governed subject; Assurance Finding / Conclusion established through adjudication where sufficient trustworthy evidence permits. Correct execution with insufficient evidence does not imply satisfaction or non-satisfaction or force a conclusion.

•
Boundary: Function completion does not define Process lifecycle states. No Context Established, Evidence Assessed, Adjudicated or Concluded states are inferred. Detailed lifecycle states, transitions and initiation mechanics remain unresolved. Requesting this Process does not grant control of evidence assessment, adjudication or the resulting finding/conclusion.

RAD diagram: textual source notation.
\begin{Verbatim}[breaklines,fontsize=
]
graph TD
    DEF["Applicable Assurance Definition<br/>Conceptual basis"] -->|Assurance basis| EXEC["PR: Assurance Process Execution<br/>Owner: Governed Assurance"]
    SUBJECT["Identifiable Governed Subject"] -->|Subject of assurance| EXEC
    EXEC -->|Participating Function| CONTEXT["FN: Establish Assurance Context"]
    EXEC -->|Participating Function| ASSESS["FN: Assess Assurance Evidence"]
    EXEC -->|Participating Function| ADJ["FN: Adjudicate Assurance Assessment"]
    EXEC -->|Establishes through adjudication| FINDING["Assurance Finding / Conclusion"]
\end{Verbatim}

---

## History

### Process Overview — predecessor explanation

Assurance execution evaluates an identifiable subject under an applicable definition and establishes a finding through adjudication where sufficient evidence permits. Correct execution with insufficient evidence remains different from execution failure and from a positive or negative conclusion about the subject. Participating Functions describe responsibilities without supplying unapproved lifecycle states.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:assurance-process-execution |
| canonicalId | 3c67e57403b041bb8ccdaf3980b6ff94 |
| displayName | Assurance Process Execution |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement.GovernedAssurance.AssuranceProcessExecution |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement.CT3-BEC-GovernedAssurance.PR-AssuranceProcessExecution |
| Snapshot publication reference | HCM-PR-assurance-process-execution |
| ArchiMate semantic ID | id-63448f2658975592a4c956d874474037 |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Assurance Process Execution |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE.PR-ASSURANCE-PROCESS-EXECUTION |
| Unresolved dimensions | ["feature_association","contextual_view"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself. |

Responsibility/framing context: [Governed Assurance](business-enabling-capabilities.md#hcm-bec-governed-assurance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:assurance-process-execution`) and the element's version-qualified Properties.

<a id="hcm-pr-assurance-process-reporting-communication"></a>

## Assurance Process Reporting / Communication

•
Canonical Name: Assurance Process Reporting / Communication.

•
Owning Capability: Governed Assurance (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md#governed-assurance).

•
Process Purpose: Communicates applicable established Assurance Findings and Conclusions to legitimate recipients according to their responsibilities, authority and information requirements, without altering the established assurance outcome or assuming responsibility for the recipient's subsequent action.

•
Starting Basis: An already established Assurance Finding / Conclusion. This Process does not perform assessment or adjudication, alter or reinterpret the established outcome.

•
Communication Participation: Service Guardian participates through Service Assurance Outcome Communication (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/collaborations-interactions/interactions.md#service-assurance-outcome-communication), associated with Communicate Service Assurance Outcome (https://github.com/fhirfactory/harmonia/blob/98410db60155198a75f74c79d98060070209faa1/docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#communicate-service-assurance-outcome). No additional Function is introduced to populate this Process.

•
Boundary: Reporting / Communication is not limited to documentary report production; an appropriate Business Interaction may communicate the outcome without a report document. It assumes no operational response, remediation, escalation, policy/compliance response, process improvement, clinical management, clinical judgement or other recipient action. Detailed communication progression and completion semantics remain unresolved.

---

## History

### Process Overview — predecessor explanation

This Process starts from an established finding and communicates it to legitimate recipients without altering the outcome. Communication need not create a report document, and it does not repeat assessment or adjudication. Recipients retain authority and responsibility for any clinical, operational, policy or improvement action they take in response.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | process:assurance-process-reporting--communication |
| canonicalId | 2fc0b626577b43feb050bcd91ac2b0a9 |
| displayName | Assurance Process Reporting / Communication |
| friendlyName | IndependentAssuranceEnablement.IndependentAssuranceEvaluationEnablement.GovernedAssurance.AssuranceProcessReportingCommunication |
| canonicalName | CT1-BEC-IndependentAssuranceEnablement.CT2-BEC-IndependentAssuranceEvaluationEnablement.CT3-BEC-GovernedAssurance.PR-AssuranceProcessReportingCommunication |
| Snapshot publication reference | HCM-PR-assurance-process-reporting-communication |
| ArchiMate semantic ID | id-ddca479bd2b15e12819ea0037affb9fa |
| Legacy identity evidence | No legacy allocation |
| Construct | process |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Assurance Process Reporting / Communication |
| Record currentness | pending_adjudication |
| Placement standing | Established |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE.PR-ASSURANCE-PROCESS-REPORTING-COMMUNICATION |
| Unresolved dimensions | ["feature_association","contextual_view"] |
| Human adjudication | HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself. |

Responsibility/framing context: [Governed Assurance](business-enabling-capabilities.md#hcm-bec-governed-assurance). This assertion retains Established placement standing.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `process:assurance-process-reporting--communication`) and the element's version-qualified Properties.

## [Business Process] Identifier Verification

Accepted as submitted in CR-008 of [run-001](../../../archimate/processing/ingestion/1.0.8/run-001/change-review.md). No additional definition, responsibility or standing is inferred. Machine-readable source: [accepted ingestion capture](accepted-ingestion-1.0.8.json).

| Field | Value |
| --- | --- |
| representationId | id-c645a859ac2c4447ad5ae850cf384bf3 |
| archimateType | BusinessProcess |
| canonicalId | 6ea7f8afe17d4319ab6fa9691db4328d |
| displayName | [Business Process] Identifier Verification |
| friendlyName | EntityInformationEnablement.ClientAdministration.PersonIdentityAdministration.IdentifierCapture.IdentifierVerification |
| canonicalName | CT1-BEC-EntityInformationEnablement.CT2-BEC-ClientAdministration.CT3-BEC-PersonIdentityAdministration.FEAT-IdentifierCapture.PR-IdentifierVerification |

------- Legacy Content - Superseded ------- Finish ----
