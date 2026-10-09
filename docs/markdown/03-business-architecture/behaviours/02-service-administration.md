# Service Administration — Capability-Scoped Behaviour

## 1. Contextual Scope & Architectural Intent

The named owning Capabilities and established Features retain their responsibilities. Affected Capability Tier, complete ancestry, root status and structural Canonical IDs remain unresolved under [approved G1 K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing); document grouping and numbering do not establish architectural identity or hierarchy.

Service Administration governs the clinical administrative lifecycles, coordination workflows, and documentation exchanges that surround patient care episodes. It coordinates referrals, appointments, encounters, orders, diagnostic reports, medication events, care plans, and clinical documents across care boundaries.

---

## 2. Client-Centred Administration
- **Owning Capability**: `Client-Centred Administration`
- **Key Architectural Semantics**:
  - **Administrative Coordination**: Handles operational demographic intake and administrative updates across departmental boundaries.
  - **Ownership Invariant**: Does **not** own authoritative person identity or identifier correlation (which is owned by *Client Administration / Person Identity*).
- **Functions & Exposed Services**:
  - **Feature: Demographic Update Ingestion**:
    - *Function*: `Receive Client Demographic Change` — Ingests administrative demographic updates from PAS/registration feeds.
    - *Exposed Service*: `Demographic Intake Service` — Exposed to departmental registration systems.
  - **Feature: Demographic Change Distribution**:
    - *Function*: `Distribute Governed Demographic Change` — Propagates verified demographic modifications to subscribed clinical subsystems.
    - *Exposed Service*: `Demographic Update Notification` — Emits change notifications across the HIE.
- **Information Responsibility**: Administrative Intake Ledger, Subscribed Distribution Registry.

---

## 3. Referral Administration
- **Owning Capability**: `Referral Administration`
- **Key Architectural Semantics**:
  - **Referral vs. Order**: A **Referral** formally requests another care participant or service provider to assess, manage or assume some degree of clinical care responsibility. Intent, disposition, responsibility actually assumed and Transfer of Care are distinct; Referral itself establishes none of acceptance, delivery, assumption or Transfer of Care, and has no universal Appointment, Encounter or Order consequence. An **Order** concerns *bounded fulfilment of a specific diagnostic or therapeutic task*.
- **Functions & Exposed Services**:
  - **Feature: Referral Document Ingestion**:
    - *Function*: `Receive Referral` — Validates and ingests structured electronic referrals, clinical indications, and priority categories.
    - *Exposed Service*: `Referral Submission Service` — Exposed to external GP practices and referring health facilities.
  - **Feature: Referral Supporting Information**:
    - *Function*: `Assemble Referral Context` — Gathers relevant clinical history, medications, and diagnostic investigations into the referral package.
    - *Exposed Service*: `Referral Context Query` — Provides comprehensive referral dossiers to intake triage clinicians.
  - **Feature: Referral Status & Outcome Tracking**:
    - *Function*: `Manage Referral Progression` — Tracks referral disposition in an **illustrative specialist pathway; dispositions vary**: *Submitted* $\to$ *Triaged* $\to$ *Accepted/Waitlisted* $\to$ *Scheduled* $\to$ *Discharged*.
    - *Exposed Service*: `Referral Status & Outcome Service` — Exposes progression milestones to referrers and patients.
    - *Governed Process*: **Referral Progression Process**.
    - *Scope qualification*: The displayed specialist pathway is illustrative, not mandatory for every Referral; acceptance, waitlisting, decline and redirection retain their contextual meanings.
- **Information Responsibility**: Referral Master Record, Triage Decision Ledger, Referral Disposition Log.

---

## 4. Scheduling Administration
- **Owning Capability**: `Scheduling Administration`
- **Key Architectural Semantics**:
  - **Coordination vs. Authoritative Booking**: Harmonia synchronises and communicates appointment milestones; it does **not** replace the authoritative booking/scheduling engines of host PAS or departmental scheduling platforms.
- **Functions & Exposed Services**:
  - **Feature: Appointment Notification Ingestion**:
    - *Function*: `Receive Appointment Notification` — Ingests appointment creation, rescheduling, cancellation, and check-in events from host PAS engines.
    - *Exposed Service*: `Appointment Notification Ingress` — Ingests booking events from departmental schedulers.
  - **Feature: Appointment Status Synchronization**:
    - *Function*: `Synchronise Appointment Status` — Projects synchronised appointment timelines to clinical dashboards and care coordinators.
    - *Exposed Service*: `Appointment Schedule Query` — Exposes consolidated patient appointment schedules across facilities.
- **Information Responsibility**: Appointment Event History, Schedule Synchronisation State.

---

## 5. Episode & Encounter Administration
- **Owning Capability**: `Episode & Encounter Administration`
- **Key Architectural Semantics**:
  - **Encounter Context**: Tracks the clinical and administrative interaction between a patient and healthcare provider during an emergency, inpatient, or outpatient episode.
- **Functions & Exposed Services**:
  - **Feature: Encounter Context Creation**:
    - *Function*: `Establish Encounter Context` — Binds patient, attending practitioner, clinical class (e.g., Inpatient, Emergency, Ambulatory), and admitting diagnosis to a unique encounter identifier.
    - *Exposed Service*: `Encounter Context Resolution` — Discloses active encounter details to clinical applications.
  - **Feature: Encounter State Progression**:
    - *Function*: `Progress Encounter State` — Coordinates encounter progression (**scoped illustration; ON_LEAVE relationship unresolved**): *Planned* $\to$ *Arrived* $\to$ *In-Progress* $\to$ *Discharged* $\to$ *Completed*.
    - *Exposed Service*: `Encounter Lifecycle Event Notification` — Emits encounter transition events across the HIE.
    - *Governed Process*: **Encounter Lifecycle Process**.
    - *Scope qualification*: Process completion qualifications remain local; their correspondence with Strategy `ON_LEAVE` is unresolved. A shorter summary neither removes established states nor creates universal signing or legal-closure requirements.
  - **Feature: Encounter Bed/Location Association**:
    - *Function*: `Maintain Encounter Care-Place Association` — Binds the encounter to physical wards, rooms, and beds over time.
    - *Exposed Service*: `Encounter Location History Query` — Discloses patient location tracking history.
- **Information Responsibility**: Encounter Master Record, Encounter State Transition Log, Encounter Care-Place Movement Ledger.

---

## 6. Order Administration
- **Owning Capability**: `Order Administration`
- **Key Architectural Semantics**:
  - **Closed-Loop Progression**: Tracks clinical orders through closed-loop progression. Diagnostic, pathology, imaging and procedural examples illustrate scoped behaviour and do not exclude medication orders from Order Administration responsibility.
  - **Order $\neq$ Referral**: Closed-loop execution of requested investigations without transferring longitudinal care responsibility.
- **Functions & Exposed Services**:
  - **Feature: Order Request Ingestion**:
    - *Function*: `Receive Order Request` — Ingests structured orders containing clinical indication, requesting clinician, and test specifications.
    - *Exposed Service*: `Order Requisition Ingress` — Ingests electronic orders from clinical ordering systems.
  - **Feature: Order Destination Resolution & Routing**:
    - *Function*: `Resolve Order Destination` — Evaluates order routing rules to determine target laboratory, imaging centre, or procedural unit.
    - *Exposed Service*: `Order Dispatch Service` — Transmits orders to performing diagnostic systems.
  - **Feature: Order Closed-Loop Progression Tracking**:
    - *Function*: `Manage Order Progression` — Tracks order state (**scoped diagnostic/procedural illustration**): *Placed* $\to$ *Received* $\to$ *Specimen Collected* $\to$ *In-Progress* $\to$ *Preliminary Result* $\to$ *Final Result* $\to$ *Closed*.
    - *Exposed Service*: `Order Status & Tracking Query` — Provides real-time order tracking to ordering clinicians.
    - *Governed Process*: **Closed-Loop Order Progression Process**.
    - *Scope qualification*: Specimen, investigation and result checkpoints describe the illustrated diagnostic/procedural progression. Dispatch, receipt and acceptance remain distinct; Behaviour `Received` is not equated to a Process checkpoint without evidence. Cancellation/modification responsibility remains valid even where absent from that sequence.
  - **Feature: Order Cancellation & Modification Coordination**:
    - *Function*: `Coordinate Order Modification / Cancellation` — Handles requests to amend clinical details or cancel unexecuted orders.
    - *Exposed Service*: `Order Cancellation Service` — Dispatches cancel directives to performing systems.
  - **Capability-scoped behaviour: Order Result Association** (Feature identity not established):
    - *Function*: `Associate Order Outcome` — Correlates returning diagnostic reports and observations back to the originating order requisition.
    - *Exposed Service*: `Order Outcome Notification` — Emits completion notices to ordering practitioners.
- **Information Responsibility**: Clinical Order Master Record, Closed-Loop Tracking Ledger, Order-Result Correlation Matrix.

---

## 7. Diagnostic Administration
- **Owning Capability**: `Diagnostic Administration`
- **Key Architectural Semantics**:
  - **Result Management & Distribution**: Manages the correlation, notification, and delivery of pathology, radiology, and point-of-care results.
  - **Ownership Guardrail**: Does **not** own the clinical diagnostic interpretation or specimen analysis (which is owned by the performing diagnostic service provider/LIS).
- **Functions & Exposed Services**:
  - **Feature: Diagnostic Request Correlation**:
    - *Function*: `Correlate Diagnostic Request` — Matches incoming lab/radiology results to active patient encounters and order requisitions.
    - *Exposed Service*: `Diagnostic Correlation Resolution` — Discloses correlated order/result linkages.
  - **Feature: Diagnostic Report Ingestion & Binding**:
    - *Function*: `Bind Diagnostic Report` — Associates structured observations, reference ranges, and abnormal flags with patient health records.
    - *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems.
  - **Feature: Diagnostic Report Distribution**:
    - *Function*: `Distribute Diagnostic Report` — Delivers preliminary, final, and corrected reports to ordering and copied practitioners.
    - *Feature scope qualification*: Strategy `FEAT-SA-17 — Diagnostic Report Distribution` retains its finalised-report responsibility. Preliminary, corrected and addendum/correction handling belongs to the broader Diagnostic Administration responsibility; it does not redefine that narrower Feature or confer diagnostic interpretation authority.
    - *Exposed Service*: `Diagnostic Result Delivery Service` — Emits secure result notifications.
- **Information Responsibility**: Diagnostic Request-Report Linkage Registry, Result Distribution Ledger.

---

## 8. Medication Administration
- **Owning Capability**: `Medication Administration`
- **Key Architectural Semantics**:
  - **Event Tracking**: Aggregates medication orders, pharmacy dispense records, and administration-event information into a coherent timeline; bedside events are one supported context.
  - **Non-Practice Guardrail**: Harmonia tracks and communicates medication events; it does **not** prescribe drugs, dispense pharmaceuticals, or perform clinical administration.
- **Functions & Exposed Services**:
  - **Feature: Medication Order Ingestion**:
    - *Function*: `Receive Medication Order` — Ingests inpatient and outpatient drug prescriptions.
    - *Exposed Service*: `Medication Order Ingress` — Ingests prescription events.
  - **Feature: Dispense Event Tracking**:
    - *Function*: `Track Dispense Event` — Ingests community and hospital pharmacy dispensing records.
    - *Exposed Service*: `Dispense Event Ingress` — Ingests pharmacy supply notifications.
  - **Feature: Medication Administration Record (MAR) Ingestion**:
    - *Function*: `Receive Medication Administration Event` — Records medication administration-event information, including nurse-administered doses, e-MAR documentation and infusion telemetry as non-exhaustive examples; scope is not limited to nursing, bedside or inpatient settings.
    - *Exposed Service*: `Medication Administration Ingress` — Ingests administration records.
- **Information Responsibility**: Medication Event Timeline, Prescription-Dispense-Administration Correlation Matrix.

---

## 9. Procedure Administration
- **Owning Capability**: `Procedure Administration`
- **Scope qualification**: Procedure information extends beyond surgery; Theatre retains its distinct surgical operational responsibility.
- **Functions & Exposed Services**:
  - **Feature: Procedure Booking Ingestion**:
    - *Function*: `Receive Procedure Booking / Request` — Ingests procedural and surgical requisitions.
    - *Exposed Service*: `Procedure Booking Ingress` — Receives surgical/procedural requests.
  - **Feature: Procedural Documentation Ingestion**:
    - *Function*: `Receive Procedural Documentation` — Ingests operation reports, anaesthetic records, and post-procedure notes.
    - *Exposed Service*: `Procedural Documentation Ingress` — Ingests procedural documentation, including surgical notes, into patient record streams.
- **Information Responsibility**: Procedure Request Register, Procedural Documentation Index.

---

## 10. Clinical Record Administration
- **Owning Capability**: `Clinical Record Administration`
- **Key Architectural Semantics**:
  - **Document Governance**: Governs the ingestion, metadata indexing, versioning, addenda, and legal lifecycle of clinical documents (discharge summaries, specialist letters, advance care directives).
- **Functions & Exposed Services**:
  - **Feature: Clinical Document Ingestion**:
    - *Function*: `Receive Clinical Document` — Receives clinical documents from information suppliers.
    - *Exposed Service*: `Clinical Document Ingress` — Exposes document submission endpoints across the HIE.
  - **Feature: Document Versioning & Supersession**:
    - *Function*: `Govern Clinical Document Lifecycle` — Manages document states (**scoped illustration, not a universal transition model**): *Draft* $\to$ *Preliminary* $\to$ *Final* $\to$ *Amended* $\to$ *Superseded* $\to$ *Entered-in-Error*.
    - *Exposed Service*: `Clinical Document Lifecycle Service` — Allows authoring clinicians to publish addenda or corrections.
    - *Governed Process*: **Clinical Document Lifecycle Process**.
    - *Scope qualification*: `Final ≠ automatically Final Signed`. Authorship, attestation, approval, authentication, verification, signature, finalisation, authority and legal qualification remain distinct. Process signing/countersigning requirements retain their local scope, and amendment/supersession/error states are not mandatory for every document. Ingestion and consumer processing do not define originating validity, authority, authorship or legal status.
  - **Feature: Document Metadata Indexing**:
    - *Function*: `Index Clinical Document Metadata` — Indexes author, specialty, encounter, date, and document type (LOINC/SNOMED).
    - *Exposed Service*: `Document Metadata Registry Query` — Discloses document registry metadata to clinical viewers.
- **Information Responsibility**: Clinical Document Registry, Document Version History, Document Lifecycle State Register.

---

## 11. Care Plan Administration
- **Owning Capability**: `Care Plan Administration`
- **Functions & Exposed Services**:
  - **Feature: Care Plan Ingestion & Distribution**:
    - *Function*: `Receive Care Plan` — Ingests multidisciplinary care plans, clinical goals, and patient action steps.
    - *Exposed Service*: `Care Plan Ingress` — Ingests shared care plans.
  - **Capability-scoped behaviour: Care Plan Context Association** (Feature identity not established):
    - *Function*: `Associate Care Plan Context` — Links care plans to active diagnoses, health problems, and care team members.
    - *Exposed Service*: `Care Plan Context Query` — Discloses active care plans to treating clinicians.
  - **Capability-scoped behaviour: Care Plan Update Distribution** (Feature identity not established):
    - *Function*: `Distribute Care Plan Change` — Broadcasts care plan updates to all enrolled care team participants.
    - *Exposed Service*: `Care Plan Change Notification` — Emits care plan revision notices.
- **Information Responsibility**: Shared Care Plan Registry, Clinical Goal Ledger, Care Plan Revision History.

---

## 12. Clinical Communication Administration
- **Owning Capability**: `Clinical Communication Administration`
- **Key Architectural Semantics**:
  - **Technical vs. Business Acknowledgement**:
    - **Technical ACK**: May establish durable receiving-boundary acceptance into an integration queue; it does not establish business acceptance.
    - **Business Delivery ACK**: Records contextual recipient-reported facts or dispositions, which may include receipt, parsing, acceptance, rejection, filing or another explicit disposition. These are not a universal progression; business-acceptance and filing criteria remain unresolved.
- **Functions & Exposed Services**:
  - **Feature: Clinical Communication Distribution**:
    - *Function*: `Distribute Clinical Communication` — Routes secure messages, referral letters, and critical alerts to practitioner endpoints.
    - *Exposed Service*: `Secure Clinical Message Dispatch` — Exposes message dispatch to clinical authors.
  - **Feature: Delivery Acknowledgement Tracking**:
    - *Function*: `Track Business Delivery Acknowledgement` — Tracks end-to-end delivery state and records business-level delivery/rejection acknowledgements.
    - *Receipt qualification*: Delivery/read evidence does not establish clinical review, agreement, approval or incorporation. Clinical Review itself does not automatically establish incorporation; no universal Clinical Incorporation concept is established.
    - *Exposed Service*: `Communication Delivery Status Query` — Discloses delivery confirmation to message senders.
  - **Feature: Communication Audit Logging**:
    - *Function*: `Record Communication Transaction` — Maintains an immutable audit trail of clinical message dispatch and receipt.
    - *Exposed Service*: `Communication Audit Query` — Discloses transmission history for medico-legal verification.
- **Information Responsibility**: Clinical Message Dispatch Log, Business Delivery Acknowledgement Ledger, Medico-Legal Transmission Audit Register.
- **Authority boundary**: Acknowledgement, receipt, persistence, filing, indexing, assembly and review do not transfer originating authority or establish finality. Communication audit evidence concerns transmission; it does not establish the clinical content’s authority or legal qualification.
