# Principal Business Processes

## 1. Process Demarcation & Justification Rules

In the Harmonia Business Architecture, a **Business Process** represents the governed progression of a healthcare activity instance through a defined lifecycle of meaningful states, dispositions, and operational outcomes within an owning Capability or Feature boundary.

### 1.1 Process Inclusion Rule
> **Model a Business Process only where the business materially cares about the progression of an activity instance through meaningful states, dispositions, or outcomes.**

Generic, stateless, or purely retrieval-oriented operations do **not** constitute Business Processes:
- Search and query execution;
- Data resolution and translation;
- Format transformation and mapping;
- Cryptographic verification and hash validation;
- Terminology and code lookup;
- User interface presentation and rendering.

### 1.2 The 16 Canonical R1 Business Processes

Harmonia recognises exactly sixteen justified principal Business Processes in R1:

```text
Principal Business Processes (R1)
├── Entity & Administrative Processes
│   ├── 1. Governed Person Identity Correction
│   └── 2. Practitioner Verification
├── Clinical Lifecycle & Service Administration Processes
│   ├── 3. Referral Progression
│   ├── 4. Encounter Lifecycle
│   ├── 5. Closed-Loop Order Progression
│   └── 6. Clinical Document Lifecycle
├── Healthcare Facility & Logistics Operations Processes
│   ├── 7. Theatre Case Progression
│   ├── 8. Clinic / Operational Queue Progression
│   ├── 9. Bed Turnover
│   ├── 10. Operational Work Progression
│   ├── 11. Patient Transport
│   ├── 12. Specimen Transport
│   └── 13. Discharge Progression
└── Horizontal Workflow Coordination Processes
    ├── 14. Work Order Progression (Human Doing)
    ├── 15. To Do Progression (Human Reviewing / Deciding)
    └── 16. Synthetic Task Progression (Automated System Work)
```

---

## 2. Entity & Administrative Processes

### 2.1 Governed Person Identity Correction Process
- **Owning Capability**: `L2: Person Identity` (under `Client Administration`)
- **Process Purpose**: Governs the formal, audited remediation, merging, unlinking, or correction of person demographic records and identifier linkages following identity fraud, misidentification, or duplicate registration.
- **State Progression Lifecycle**:
  $$\text{Correction Requested} \longrightarrow \text{HIM Triage} \longrightarrow \text{Evidence Verified} \longrightarrow \text{Correction Approved} \longrightarrow \text{Merge/Unlink Executed} \longrightarrow \text{Change Broadcasted} \longrightarrow \text{Closed}$$
- **Key State Dispositions**:
  - `Correction Requested`: Demographic discrepancy or duplicate identity flag submitted.
  - `HIM Triage`: Health Information Manager reviews candidate identity records.
  - `Evidence Verified`: Primary identity documentation or statutory declarations verified.
  - `Correction Approved`: Authorised correction decision signed off.
  - `Merge/Unlink Executed`: Canonical identifier correlation graph updated and audit record sealed.
  - `Change Broadcasted`: Downstream clinical systems notified of merged or rectified identity.

---

### 2.2 Practitioner Verification Process
- **Owning Capability**: `L1: Provider Administration`
- **Process Purpose**: Governs the lifecycle of validating, certifying, and periodically re-verifying a healthcare practitioner's professional credentials, registration status, and clinical scope of practice.
- **State Progression Lifecycle**:
  $$\text{Verification Initiated} \longrightarrow \text{Regulatory Registry Queried} \longrightarrow \text{Credentials Validated} \longrightarrow \text{Privileges Endorsed} \longrightarrow \text{Active Verified} \longrightarrow \text{Expired / Suspended}$$
- **Key State Dispositions**:
  - `Verification Initiated`: New practitioner registration or periodic re-credentialing triggered.
  - `Regulatory Registry Queried`: Real-time query executed against national registry (e.g., AHPRA).
  - `Credentials Validated`: Primary qualifications, specialist endorsements, and sanctions checked.
  - `Privileges Endorsed`: Health service medical administration endorses admitting/procedural scope.
  - `Active Verified`: Practitioner verified for clinical ordering and electronic communication.
  - `Expired / Suspended`: Verification lapses or regulatory sanction triggers privilege suspension.

---

## 3. Clinical Lifecycle & Service Administration Processes

### 3.3 Referral Progression Process
- **Owning Capability**: `L1: Referral Administration`
- **Process Purpose**: Governs the end-to-end operational progression of an incoming clinical referral from receipt through specialist clinical triage, booking, and final service acceptance.
- **State Progression Lifecycle**:
  $$\text{Submitted} \longrightarrow \text{Intake Validated} \longrightarrow \text{Clinically Triaged} \longrightarrow \text{Accepted / Waitlisted} \longrightarrow \text{Scheduled} \longrightarrow \text{Consultation Attended} \longrightarrow \text{Discharged / Rejected}$$
- **Key State Dispositions**:
  - `Submitted`: Electronic referral received from GP or external facility.
  - `Intake Validated`: Administrative validation of patient details, mandatory fields, and tests.
  - `Clinically Triaged`: Senior clinician assigns urgency category (e.g., Cat 1 urgent within 30 days).
  - `Accepted / Waitlisted`: Referral accepted and placed onto the specialty waitlist.
  - `Scheduled`: Outpatient appointment allocated and patient notified.
  - `Consultation Attended`: Patient seen; initial specialist assessment completed.
  - `Discharged / Rejected`: Referral completed and discharged back to primary care, or formally rejected with clinical rationale.

---

### 3.4 Encounter Lifecycle Process
- **Owning Capability**: `L1: Episode & Encounter Administration`
- **Process Purpose**: Governs the clinical and administrative lifecycle of an acute, emergency, inpatient, or outpatient encounter between a patient and healthcare services.
- **State Progression Lifecycle**:
  $$\text{Planned / Booked} \longrightarrow \text{Arrived} \longrightarrow \text{Triaged / Ingested} \longrightarrow \text{Active In-Progress} \longrightarrow \text{Discharged} \longrightarrow \text{Completed / Encoded}$$
- **Key State Dispositions**:
  - `Planned / Booked`: Elective admission or clinic appointment scheduled.
  - `Arrived`: Patient presents at facility or emergency desk.
  - `Triaged / Ingested`: Clinical triage category assigned; encounter record activated.
  - `Active In-Progress`: Inpatient care, bedside monitoring, and clinical orders actively underway.
  - `Discharged`: Patient clinically discharged; care-place vacated.
  - `Completed / Encoded`: Clinical documentation finalized, ICD/DRG coding completed, and encounter legally closed.

---

### 3.5 Closed-Loop Order Progression Process
- **Owning Capability**: `L1: Order Administration`
- **Process Purpose**: Governs the rigorous, closed-loop tracking of diagnostic pathology, radiology, and procedural orders from requisition to result correlation, preventing dropped or unfulfilled investigations.
- **State Progression Lifecycle**:
  $$\text{Requisition Placed} \longrightarrow \text{Order Dispatched} \longrightarrow \text{Specimen Collected / Scheduled} \longrightarrow \text{In-Execution} \longrightarrow \text{Preliminary Result Bound} \longrightarrow \text{Final Result Bound} \longrightarrow \text{Closed / Verified}$$
- **Key State Dispositions**:
  - `Requisition Placed`: Electronic order created and signed by requesting clinician.
  - `Order Dispatched`: Order routed and accepted by performing diagnostic service.
  - `Specimen Collected / Scheduled`: Bio-specimen collected or imaging appointment booked.
  - `In-Execution`: Laboratory analysis or diagnostic scanning underway.
  - `Preliminary Result Bound`: Critical or interim observations published and linked to order.
  - `Final Result Bound`: Authoritative diagnostic report signed and bound to order.
  - `Closed / Verified`: Requesting clinician acknowledges result; order closed.

---

### 3.6 Clinical Document Lifecycle Process
- **Owning Capability**: `L1: Clinical Record Administration`
- **Process Purpose**: Governs the versioning, clinical sign-off, addenda, superseding, and legal status of clinical documents (discharge summaries, specialist letters, advance care directives).
- **State Progression Lifecycle**:
  $$\text{Draft} \longrightarrow \text{Preliminary} \longrightarrow \text{Final Signed} \longrightarrow \text{Amended / Addended} \longrightarrow \text{Superseded} \longrightarrow \text{Entered-in-Error}$$
- **Key State Dispositions**:
  - `Draft`: Incomplete clinical document saved during consultation.
  - `Preliminary`: Document authored pending senior registrar or consultant countersignature.
  - `Final Signed`: Authoritative clinical document legally signed and published.
  - `Amended / Addended`: Governed supplementary clinical addendum appended to signed document.
  - `Superseded`: Entire document replaced by a newer revision; prior version retained for audit.
  - `Entered-in-Error`: Document formally retracted due to wrong patient or invalid content; content struck through with retraction notice.

---

## 4. Healthcare Facility & Logistics Operations Processes

### 4.7 Theatre Case Progression Process
- **Owning Capability**: `L1: Theatre Operations`
- **Process Purpose**: Governs the perioperative operational milestones of a surgical procedure within the operating theatre suite.
- **State Progression Lifecycle**:
  $$\text{Case Called} \longrightarrow \text{In Anaesthetic Bay} \longrightarrow \text{Anaesthesia Commenced} \longrightarrow \text{Patient in Theatre} \longrightarrow \text{Knife-to-Skin} \longrightarrow \text{Procedure Finished} \longrightarrow \text{In PACU Recovery} \longrightarrow \text{Ward Handover Complete}$$
- **Key State Dispositions**:
  - `Case Called`: Theatre team requests ward to send patient for surgery.
  - `In Anaesthetic Bay`: Patient arrives in theatre holding and identity/consent verified.
  - `Anaesthesia Commenced`: Anaesthetic induction underway.
  - `Patient in Theatre`: Patient transferred onto operating table.
  - `Knife-to-Skin`: Surgical incision initiated (operative start).
  - `Procedure Finished`: Wound closure completed and dressings applied.
  - `In PACU Recovery`: Patient monitored in post-anaesthesia care unit.
  - `Ward Handover Complete`: Post-operative clinical handover to inpatient ward completed.

---

### 4.8 Clinic / Operational Queue Progression Process
- **Owning Capability**: `L1: Clinic & Practice Operations`
- **Process Purpose**: Governs the movement and state tracking of outpatients through an ambulatory specialty clinic session.
- **State Progression Lifecycle**:
  $$\text{Scheduled} \longrightarrow \text{Patient Arrived} \longrightarrow \text{Checked-In} \longrightarrow \text{Roomed / Pre-Consult} \longrightarrow \text{Consultation In-Progress} \longrightarrow \text{Consultation Concluded} \longrightarrow \text{Departed}$$
- **Key State Dispositions**:
  - `Scheduled`: Appointment booked on session list.
  - `Patient Arrived`: Patient enters waiting area.
  - `Checked-In`: Reception verifies demographics and marks patient ready.
  - `Roomed / Pre-Consult`: Nursing staff record baseline vitals and room patient.
  - `Consultation In-Progress`: Clinician actively conducting consultation.
  - `Consultation Concluded`: Clinical discussion finished; follow-up orders placed.
  - `Departed`: Patient checks out and departs clinic facility.

---

### 4.9 Bed Turnover Process
- **Owning Capability**: `L1: Bed & Care-Place Management`
- **Process Purpose**: Governs the rapid physical turnover, cleaning, sanitisation, and re-allocation of inpatient and emergency care-places.
- **State Progression Lifecycle**:
  $$\text{Bed Vacated} \longrightarrow \text{Cleaning Dispatched} \longrightarrow \text{Sanitisation In-Progress} \longrightarrow \text{Terminal Cleaning Verified} \longrightarrow \text{Bed Ready / Available} \longrightarrow \text{Bed Allocated / Occupied}$$
- **Key State Dispositions**:
  - `Bed Vacated`: Prior patient discharged or transferred.
  - `Cleaning Dispatched`: Cleaning job allocated to environmental services team.
  - `Sanitisation In-Progress`: Ward cleaning or isolation decontamination underway.
  - `Terminal Cleaning Verified`: Ward coordinator or supervisor verifies care-place readiness.
  - `Bed Ready / Available`: Bed marked available for incoming patient allocation.
  - `Bed Allocated / Occupied`: Incoming patient allocated and physically placed in bed.

---

### 4.10 Operational Work Progression Process
- **Owning Capability**: `L1: Work Allocation & Dispatch`
- **Process Purpose**: Governs the dispatch, assignment, execution, and completion of facility operational jobs (e.g., equipment moves, waste removal, linen supply).
- **State Progression Lifecycle**:
  $$\text{Work Requested} \longrightarrow \text{Queued in Dispatch} \longrightarrow \text{Assigned to Worker} \longrightarrow \text{Job Accepted} \longrightarrow \text{Work In-Progress} \longrightarrow \text{Work Completed / Aborted}$$
- **Key State Dispositions**:
  - `Work Requested`: Facility support task submitted by clinical unit.
  - `Queued in Dispatch`: Job prioritized in operational queue.
  - `Assigned to Worker`: Job allocated to specific staff member or team.
  - `Job Accepted`: Worker acknowledges dispatch on mobile device.
  - `Work In-Progress`: Task actively being executed.
  - `Work Completed / Aborted`: Job marked finished or formally cancelled with reason.

---

### 4.11 Patient Transport Process
- **Owning Capability**: `L1: Patient Transport`
- **Process Purpose**: Governs the physical portering and transport of patients between hospital departments, wards, diagnostic suites, and external facilities.
- **State Progression Lifecycle**:
  $$\text{Transport Requested} \longrightarrow \text{Porter Dispatched} \longrightarrow \text{Patient Collected} \longrightarrow \text{In-Transit} \longrightarrow \text{Delivered at Destination} \longrightarrow \text{Clinical Handover Complete}$$
- **Key State Dispositions**:
  - `Transport Requested`: Clinical unit books wheelchair, bed, or ambulance transport.
  - `Porter Dispatched`: Wardsperson assigned and en route to pickup location.
  - `Patient Collected`: Patient identity verified and transfer initiated.
  - `In-Transit`: Patient actively moving through facility corridors or ambulance transit.
  - `Delivered at Destination`: Patient arrives at target diagnostic suite or ward.
  - `Clinical Handover Complete`: Safe custody and clinical briefing transferred to receiving staff.

---

### 4.12 Specimen Transport Process
- **Owning Capability**: `L1: Clinical Logistics`
- **Process Purpose**: Governs the physical courier chain-of-custody, transport, and delivery of pathology bio-specimens, blood products, and surgical biopsies.
- **State Progression Lifecycle**:
  $$\text{Specimen Packaged} \longrightarrow \text{Courier Dispatched} \longrightarrow \text{Chain-of-Custody Signed} \longrightarrow \text{In-Transit (Monitored)} \longrightarrow \text{Lab Ingress Received} \longrightarrow \text{Specimen Accepted}$$
- **Key State Dispositions**:
  - `Specimen Packaged`: Clinical unit packages bio-sample with barcode manifest.
  - `Courier Dispatched`: Courier route assigned for pickup.
  - `Chain-of-Custody Signed`: Courier scans barcode and accepts legal custody.
  - `In-Transit (Monitored)`: Specimen in transit under temperature/time constraints.
  - `Lab Ingress Received`: Central pathology specimen reception logs physical receipt.
  - `Specimen Accepted`: Specimen integrity verified and passed to analytical track.

---

### 4.13 Discharge Progression Process
- **Owning Capability**: `L1: Discharge Management`
- **Process Purpose**: Governs the multidisciplinary coordination of patient discharge planning, pharmacy reconciliation, transport, and community handover.
- **State Progression Lifecycle**:
  $$\text{Discharge Planning Initiated} \longrightarrow \text{Clinical Readiness Confirmed} \longrightarrow \text{Medications Reconciled} \longrightarrow \text{Transport & Services Booked} \longrightarrow \text{Discharge Summary Signed} \longrightarrow \text{Physically Departed}$$
- **Key State Dispositions**:
  - `Discharge Planning Initiated`: Estimated Date of Discharge (EDD) set on admission.
  - `Clinical Readiness Confirmed`: Attending medical team declares patient fit for discharge.
  - `Medications Reconciled`: Hospital pharmacy reconciles and dispenses discharge medications.
  - `Transport & Services Booked`: Community nursing, home equipment, and patient transport booked.
  - `Discharge Summary Signed`: Final discharge summary published and sent to GP.
  - `Physically Departed`: Patient departs ward; care-place turnover triggered.

---

## 5. Horizontal Workflow Coordination Processes

### 5.14 Work Order Progression Process
- **Owning Capability**: `L1: Workflow & Activity Coordination`
- **Activity Archetype**: **Human Doing** — physical or operational task executed by healthcare staff.
- **State Progression Lifecycle**:
  $$\text{Created} \longrightarrow \text{Ready} \longrightarrow \text{Dispatched} \longrightarrow \text{Accepted} \longrightarrow \text{In-Progress} \longrightarrow \text{Completed / Failed / Cancelled}$$
- **Key State Dispositions**:
  - `Created`: Work order instantiated with scope, target, and required skills.
  - `Ready`: Preconditions satisfied; available for assignment.
  - `Dispatched`: Sent to target individual or worker group queue.
  - `Accepted`: Worker claims task.
  - `In-Progress`: Execution underway.
  - `Completed`: Task finished with recorded completion telemetry.

---

### 5.15 To Do Progression Process
- **Owning Capability**: `L1: Workflow & Activity Coordination`
- **Activity Archetype**: **Human Reviewing / Updating / Deciding** — clinical review, document countersignature, or administrative authorization task.
- **State Progression Lifecycle**:
  $$\text{Issued} \longrightarrow \text{In-Inbox} \longrightarrow \text{Opened / Under-Review} \longrightarrow \text{Actioned / Decided} \longrightarrow \text{Dismissed / Delegated}$$
- **Key State Dispositions**:
  - `Issued`: To Do generated (e.g., review abnormal potassium result).
  - `In-Inbox`: Displayed in practitioner's actionable task list.
  - `Opened / Under-Review`: Clinician inspecting relevant clinical context.
  - `Actioned / Decided`: Decision executed (e.g., signed off, order placed, acknowledged).
  - `Dismissed / Delegated`: Task reassigned to registrar or dismissed with comment.

---

### 5.16 Synthetic Task Progression Process
- **Owning Capability**: `L1: Workflow & Activity Coordination`
- **Activity Archetype**: **Non-Human Executable Work** — automated system workflows, batch syndications, or policy evaluation tasks.
- **State Progression Lifecycle**:
  $$\text{Draft / Scheduled} \longrightarrow \text{Executing} \longrightarrow \text{Awaiting-Dependency} \longrightarrow \text{Completed} \longrightarrow \text{Progression Stalled / Failed}$$
- **Key State Dispositions**:
  - `Draft / Scheduled`: Automated task instantiated and scheduled for processing.
  - `Executing`: System actively processing task.
  - `Awaiting-Dependency`: Paused awaiting asynchronous external reply or event.
  - `Completed`: Automated execution succeeded and output artifact produced.
  - `Progression Stalled / Failed`: Transient fault triggers scheduled recovery attempt; permanent failure records failure evidence.
