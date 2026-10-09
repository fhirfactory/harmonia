# Health Service Operations — Capability-Scoped Behaviour

## 1. Contextual Scope & Architectural Intent

The named owning Capabilities and established Features retain their responsibilities. Affected Capability Tier, complete ancestry, root status and structural Canonical IDs remain unresolved under [approved G1 K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing); document grouping and numbering do not establish architectural identity or hierarchy.

Health Service Operations governs the operational logistics, physical resource management, facility capacity, staff dispatch, and operational movement required to support healthcare delivery across clinical facilities.

### Operational Logistics vs. Software Plumbing
> **In Domain 03, "Operations" refers strictly to healthcare facility logistics (e.g., bed turnover, patient portering, theatre case scheduling, clinic queues, dispatch). It does not mean IT middleware monitoring, application deployment, or database administration.**

Where Healthcare Service context materially affects operational meaning, authority, coordination, progression or accountability, keep it identifiable with the activity and relevant information responsibility. Location, Organisation, Practitioner or Role does not imply that service context. This contextual obligation establishes no information representation, identifier or implementation binding.

The bounded coverage additions below derive from the [established Strategy HSO Features](../../02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations). They reuse existing compound behaviour where its meaning supports the relationship, or add the minimum directly required behaviour. Additional Functions do not mechanically create Services, Processes, Roles or structural IDs.

<a id="capacity-management-responsibility-boundary"></a>

### Capacity Management Responsibility Boundary

The 2026-10-09 human adjudication establishes **Capacity Management** as Business responsibility for understanding and managing available, committed and utilised capacity in six service-delivery operational contexts. Clinic/practice, ward, theatre, ED, outpatient and mobile service capacity retain their distinct Business meanings. This does not establish a common capacity model, information schema or lifecycle. Generalised service/location/patient/operational-state capture belongs to downstream derivation and is not a Domain03 Function.

Context-specific responsibility establishes/manages service capacity. Resource state contributes to it: an available bed alone does not mean a ward can accept another patient; staffing, demand, acuity and operational constraints may also matter. Service Capacity Management consumes/coordinates context-specific capacity; Work Allocation & Dispatch and Discharge Management may consume capacity/availability or capacity/progression information without becoming its owners. These are Business responsibility relationships, not newly allocated Services or consumption contracts.

| HSO context | Context-specific capacity responsibility |
| :--- | :--- |
| Clinic & Practice Operations | Yes — session/queue behaviour contributes; overall clinic/practice capacity is expressed separately in §2.1. |
| Ward Operations | Yes — existing operational coordination is reframed as ward capacity in §2.2. |
| Theatre Operations | Yes — case progression contributes; theatre capacity is expressed separately in §2.3. |
| Emergency Department Operations | Yes — existing departmental capacity behaviour is clarified in §2.4. |
| Outpatient Operations | Yes — existing session utilisation is reframed as outpatient capacity in §2.5. |
| Bed & Care-Place Management | No — establishes resource state contributing to ward capacity. |
| Clinical Resource Management | No — establishes equipment state/availability; resource readiness is distinct from service capacity. |
| Service Capacity Management | No — consumes/coordinates context-specific capacity. |
| Mobile Staff Management | Yes — presence and visit progression contribute; mobile service capacity is expressed separately in §2.9. |
| On-Call Management | No — resolves coverage rather than establishing context-specific capacity. |
| Work Allocation & Dispatch | No — may consume capacity/availability for operational work. |
| Credential Management | No — verifies operational access. |
| Patient Transport | No — coordinates transport requests/progression. |
| Clinical Logistics Coordination | No — coordinates logistics/custody. |
| Discharge Management | No — may consume capacity/progression for discharge coordination. |
| Clinical Qualification Management | No — verifies qualifications/competency for applicable dispatch. |

[Clinical work integration](../metamodel/business-architecture-metamodel.md#37-clinical-work-integration-boundary) does not confer clinical-work management, clinical handover, clinical task determination, clinical allocation authority or EMR worklist ownership. Capacity and progression consumers retain the originating authority of clinical facts.

---

## 2. Facility Operational Units & Queue Coordination

```text
Health Service Operations Contexts
├── 1. Clinic & Practice Operations
├── 2. Ward Operations
├── 3. Theatre Operations
├── 4. Emergency Department Operations
├── 5. Outpatient Operations
├── 6. Bed & Care-Place Management
├── 7. Clinical Resource Management
├── 8. Service Capacity Management
├── 9. Mobile Staff Management
├── 10. On-Call Management
├── 11. Work Allocation & Dispatch
├── 12. Credential Management
├── 13. Patient Transport
├── 14. Clinical Logistics Coordination
├── 15. Discharge Management
└── 16. Clinical Qualification Management
```

---

### 2.1 Clinic & Practice Operations
- **Owning Capability**: `Clinic & Practice Operations`
- **Functions & Exposed Services**:
  - **Capability-scoped capacity behaviour**:
    - *Function*: `Manage Clinic & Practice Capacity` — Understands and manages available, committed and utilised clinic/practice capacity in relation to practitioner availability, sessions, rooms, appointments and patient demand.
    - *Responsibility boundary*: Operating-session tracking and individual queue progression contribute to this responsibility; neither alone expresses overall service capacity. Capacity coordination does not own source bookings, clinical decisions or practitioner rostering.
  - **Feature: Clinic Session Tracking**:
    - *Function*: `Track Clinic Operating Session` — Ingests and tracks clinic operating-session start, delay and completion, distinct from an individual patient's queue progression.
    - *Derivation*: `FEAT-HSO-01` directly establishes this session-level behaviour; Manage Clinic Queue Progression and its Process concern individual patients and do not supply it.
    - *Capacity contribution*: Session start/delay/completion remains distinct observable Business behaviour contributing to clinic/practice capacity; it is not merged into capacity merely for structural neatness.
  - **Feature: Operational Queue Progression**:
    - *Function*: `Manage Clinic Queue Progression` — Tracks patient arrival, rooming, consultation in-progress, and check-out states.
    - *Exposed Service*: `Clinic Queue Telemetry Service` — Discloses real-time clinic waiting times and queue states.
    - *Governed Process*: **Clinic / Operational Queue Progression Process**.
- **Information Responsibility**: Clinic & Practice Capacity, Clinic Session Progression, Clinic Queue State, Patient Waiting Time Metrics.

---

### 2.2 Ward Operations
- **Owning Capability**: `Ward Operations`
- **Functions & Exposed Services**:
  - **Capability-scoped capacity behaviour**:
    - *Function*: `Manage Ward Capacity` — Coordinates available, committed and utilised ward capacity and its patient-flow implications, retaining acuity distribution, nurse-to-patient ratios and isolation cohorting alongside bed/care-place state, patient demand and operational constraints.
    - *Name Alias*: `Coordinate Ward Operational State` — the same established ward operational responsibility, clarified as Capacity Management; no new Function identity or identifier is allocated.
    - *Exposed Service*: `Ward Operational Summary Query` — Discloses ward-level operational telemetry.
    - *Existing Feature contribution*: Ward Patient Flow Coordination concerns ward occupancy, care-place allocation and patient bed status. Those patient-flow responsibilities remain within ward operational coordination; they do not define the whole capacity responsibility.
    - *Capacity boundary*: Bed & Care-Place Management contributes resource state. Ward capacity is assessed in its service context; an available bed alone does not establish capacity to accept a patient. Source staffing and clinical assertions retain their authority.
  - **Feature: Clinical Handover Context Assembly**:
    - *Function*: `Assemble Ward Handover Context` — Collates current ward patient lists, active medical concerns and pending tasks for nursing shift handover.
    - *Derivation*: `FEAT-HSO-04` directly requires this assembly. Ward acuity/cohorting telemetry alone does not provide the stated handover content. Source clinical information retains its originating authority; assembly does not perform clinical review, manage clinical handover, determine pending clinical work or acquire clinical judgement.
- **Information Responsibility**: Ward Capacity, Ward Shift Status Ledger, Acuity Balance Register, Ward Handover Context.

---

### 2.3 Theatre Operations
- **Owning Capability**: `Theatre Operations`
- **Functions & Exposed Services**:
  - **Capability-scoped capacity behaviour**:
    - *Function*: `Manage Theatre Capacity` — Understands and manages available, committed and utilised theatre capacity in relation to theatre, team and equipment availability, scheduled cases and case progression.
    - *Responsibility boundary*: Case milestones and targeted resource notifications contribute; they do not by themselves establish available or committed theatre service capacity. Clinical case determination and originating team/equipment information remain separately governed.
  - **Feature: Operating Theatre Case Progression**:
    - *Function*: `Manage Theatre Case Progression` — Coordinates surgical case milestones: *Case Called* $\to$ *Patient in Anaesthetic Bay* $\to$ *Anaesthesia Commenced* $\to$ *Knife to Skin* $\to$ *Procedure Finished* $\to$ *In PACU/Recovery* $\to$ *Ward Handover Complete*.
    - *Exposed Service*: `Theatre Case Progression Telemetry` — Emits real-time surgical milestone updates.
    - *Additional Feature responsibility*: `FEAT-HSO-06 — Perioperative Resource Notification` is realised by dispatching progression-triggered alerts to portering, sterilisation and recovery teams within this same case-progression behaviour/exposure. Milestone observation alone does not establish notification; no new Function or Service is created.
    - *Governed Process*: **Theatre Case Progression Process**.
- **Information Responsibility**: Theatre Capacity, Theatre Slate Register, Perioperative Milestone Log.

---

### 2.4 Emergency Department Operations
- **Owning Capability**: `Emergency Department Operations`
- **Functions & Exposed Services**:
  - **Capability-scoped capacity behaviour**:
    - *Function*: `Coordinate ED Departmental Capacity` — Understands and manages available, committed and utilised ED capacity in relation to care-place availability, demand, patient presence/progression, acuity and operational constraints; retains waiting room occupancy, resuscitation bay availability and time-to-triage metrics.
    - *Exposed Service*: `ED Operational Status Query` — Discloses facility ED occupancy and surge level.
    - *Existing Feature scope*: ED Length of Stay Tracking retains the existing wait/progression metrics context; capacity responsibility is broader than that Feature. This clarification does not assert a new whole-Feature realisation relationship.
    - *Additional Feature relationship*: `FEAT-HSO-07 — ED Bed & Bay Tracking` is materially realised by this capacity-tracking behaviour for occupancy/status of emergency cubicles, resuscitation bays and short-stay beds. Those care-place states are explicit within the existing Function and its operational status exposure.
    - *Capacity boundary*: Existing departmental capacity responsibility is clarified, not duplicated. Care-place state contributes to service capacity; capacity coordination neither performs clinical triage nor establishes clinical acuity assertions.
- **Information Responsibility**: ED Capacity, ED Operational Dashboard Ledger, Resuscitation Bay Occupancy Matrix.

---

### 2.5 Outpatient Operations
- **Owning Capability**: `Outpatient Operations`
- **Functions & Exposed Services**:
  - **Capability-scoped Functions / Services**:
    - *Function*: `Manage Outpatient Capacity` — Understands and manages available, committed and utilised outpatient capacity through clinic/session, practitioner and room availability, appointment demand, patient arrival/waiting and operational progression. Retains specialty room allocation and clinician arrival tracking.
    - *Name Alias*: `Coordinate Outpatient Session Utilisation` — the existing responsibility reframed as outpatient Capacity Management; no new Function identity or identifier is allocated.
    - *Feature responsibility*: `FEAT-HSO-09 — Outpatient Arrival Registration` is realised within this capacity behaviour by ingesting patient check-in/arrival events and notifying treating clinicians. Patient arrival contributes demand/presence; it does not itself establish capacity. This bounded responsibility does not equate the whole capacity Function with the Feature or create a separate Arrival Registration Function.
    - *Exposed Service*: `Ambulatory Session Status Query` — Exposes clinic room occupancy.
    - *Exposure qualification*: Query behaviour and Outpatient Operations ownership remain established; a meaningful outside consumer/mandate is not identified by the current description. That exposure relationship remains unestablished rather than populated with an inferred Actor, Role or Capability.
    - *Capacity boundary*: Operational coordination does not acquire authoritative booking/rostering or clinical-work management. Room-occupancy query remains narrower than the capacity responsibility; no enlarged exposure or new named Service is inferred.
  - **Feature: Post-Clinic Order Coordination**:
    - *Function*: `Capture Post-Clinic Follow-up Requirements` — Captures follow-up booking and diagnostic-order requirements generated at clinic completion.
    - *Derivation*: `FEAT-HSO-10` directly establishes this capture responsibility. The Clinic queue Process's consultation-concluded checkpoint describes orders placed, not Outpatient Operations' capture of follow-up requirements. Capture does not authorise clinical orders, own authoritative bookings or establish a new Service dependency.
- **Information Responsibility**: Outpatient Capacity, Outpatient Arrival/Waiting Demand, Outpatient Session Schedule, Room Allocation Register, Post-Clinic Follow-up Requirements.

---

### 2.6 Bed & Care-Place Management
- **Owning Capability**: `Bed & Care-Place Management`
- **Key Architectural Semantics**:
  - **Operational State vs. Definition**: While *Location Administration* defines the physical care-place, *Bed & Care-Place Management* owns the real-time operational status (*Available*, *Occupied*, *Reserved*, *Blocked*, *Dirty*, *Cleaning In-Progress*, *Maintenance Lock*).
  - **Resource State vs. Capacity**: This state contributes to Ward Operations' capacity responsibility. Bed availability does not establish ward service capacity or authority to accept another patient.
- **Functions & Exposed Services**:
  - **Feature: Bed Availability Tracking**:
    - *Function*: `Maintain Operational Bed State` — Tracks real-time bed status and environmental locks.
    - *Exposed Service*: `Bed Availability Query` — Discloses real-time bed availability across hospital directorates.
  - **Feature: Bed State Progression**:
    - *Function*: `Manage Bed Turnover Progression` — Coordinates cleaning, disinfection, and preparation: *Vacated* $\to$ *Cleaning Requested* $\to$ *Cleaning In-Progress* $\to$ *Inspected/Ready* $\to$ *Occupied*.
    - *Exposed Service*: `Bed Turnover Lifecycle Service` — Coordinates turnover requests with environmental services.
    - *Additional Feature responsibility*: `FEAT-HSO-13 — Bed Readiness Notification` requires the existing turnover behaviour/exposure to notify bed allocation managers and clinical units when a cleaned bed is ready. Readiness notification remains distinct from availability query and from physical placement; no new lifecycle state or Service is introduced.
    - *Governed Process*: **Bed Turnover Process**.
- **Information Responsibility**: Operational Bed Status Registry, Care-Place Lock Register, Bed Turnover Milestone Log.

---

### 2.7 Clinical Resource Management
- **Owning Capability**: `Clinical Resource Management`
- **Functions & Exposed Services**:
  - **Feature: Mobile Clinical Equipment Tracking**:
    - *Function*: `Track Mobile Clinical Asset Operational State` — Manages the real-time location and operational readiness of mobile ventilators, infusion pumps, and telemetry transmitters.
    - *Exposed Service*: `Clinical Resource Availability Query` — Exposes mobile equipment availability.
- **Information Responsibility**: Mobile Clinical Asset Operational Ledger, Maintenance Readiness Matrix.

---

### 2.8 Service Capacity Management
- **Owning Capability**: `Service Capacity Management`
- **Functions & Exposed Services**:
  - **Feature: Operational Capacity Metric Aggregation**:
    - *Function*: `Aggregate Service Capacity Telemetry` — Consumes and coordinates context-specific service-capacity information for broader situational awareness, alongside bed occupancy, ICU surge capacity and ventilator availability across regional hospitals.
    - *Exposed Service*: `Regional Health System Capacity Query` — Provides executive operational situational awareness.
    - *Consumer boundary*: Aggregation does not establish the underlying context-specific capacity. Resource occupancy/readiness and service capacity remain distinguishable; exact additional provider contracts are unestablished.
- **Information Responsibility**: Regional System Capacity Snapshot, Surge Level State.

---

### 2.9 Mobile Staff Management
- **Owning Capability**: `Mobile Staff Management`
- **Functions & Exposed Services**:
  - **Capability-scoped capacity behaviour**:
    - *Function*: `Manage Mobile Service Capacity` — Understands and manages available, committed and utilised mobile-service capacity in relation to workforce availability, geography, time, workload and applicable service demand.
    - *Responsibility boundary*: Staff presence and externally established assignments/visit progression contribute to capacity; they do not alone express it. Capacity coordination does not determine clinical work, own the source workforce roster or allocate underlying clinical duties.
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: `FEAT-HSO-16 — Mobile Worker Task Dispatch` retains its Strategy identity with the integration boundary below. Presence/zone tracking remains distinct from mobile work-management integration; no replacement association to presence is assigned.
    - *Function*: `Track Operational Staff Presence` — Tracks active on-duty presence and physical zone assignment for porters, phlebotomists, and roving nurses.
    - *Exposed Service*: `Staff Presence Telemetry Query` — Discloses on-duty staff availability by hospital zone.
  - **Feature: Mobile Visit Status Tracking**:
    - *Function*: `Receive Mobile Visit Progression` — Ingests arrival, visit-progression and safety check-in milestones from roving staff.
    - *Derivation*: `FEAT-HSO-17` directly requires visit-specific milestone ingestion. It remains distinct from presence, capacity assessment and work-management integration. Received progression can inform mobile capacity without determining clinical work or clinical completion.
  - **Feature: Mobile Worker Task Dispatch** (integration scope qualified in Strategy):
    - *Function*: `Integrate Mobile Work Management` — Exchanges externally established work/assignment, dispatch and operational progression/status information, with applicable worker/context information, between mobile work-management mechanisms and Harmonia operational coordination.
    - *Derivation and boundary*: The human adjudication of `FEAT-HSO-16` establishes integration with mobile work/task management. This responsibility is absent from presence observation and visit-specific milestone ingestion. It does not determine, allocate or manage the underlying clinical work, clinical handover or EMR worklists. Work Allocation & Dispatch retains non-clinical operational Work Order responsibility.
    - *Terminology*: External “work/task” vocabulary does not make human work a Harmonia Task. Work Order means human doing; To Do means human review/update/approval; Task means synthetic task. The preferred Strategy name `Mobile Work Task Management Integration` remains a proposed naming correction pending human confirmation, not a new Feature or alias.
- **Information Responsibility**: Mobile Service Capacity, Staff Operational Presence Register, Zone Allocation Matrix, Mobile Visit Progression, Exchanged Mobile Work-Management Information. Originating work/assignment authority remains with its external/business work-management source.

---

### 2.10 On-Call Management
- **Owning Capability**: `On-Call Management`
- **Functions & Exposed Services**:
  - **Feature: Active On-Call Provider Resolution**:
    - *Function*: `Resolve Active On-Call Coverage` — Resolves primary, secondary, and tertiary on-call clinical specialists for emergency escalation.
    - *Exposed Service*: `On-Call Specialist Lookup` — Discloses active on-call contacts for switchboard and emergency teams.
- **Information Responsibility**: On-Call Roster Projection, Escalation Contact Matrix.

---

### 2.11 Work Allocation & Dispatch
- **Owning Capability**: `Work Allocation & Dispatch`
- **Key Architectural Semantics**:
  - **Ownership Guardrail**: Owns the dispatching, queuing, assignment, and operational progression of non-clinical work orders (e.g., patient transfers, cleaning, specimen pickups). It does **not** acquire ownership of Practitioners, Locations, Services, or Credentials.
  - **Capacity Consumer**: May use context-specific capacity/availability in operational allocation; it neither establishes that capacity nor acquires clinical-work allocation authority through mobile integration.
- **Functions & Exposed Services**:
  - **Feature: Work Item Instantiation**:
    - *Function*: `Dispatch Operational Work Order` — Allocates work requests to available wardspersons or support teams using applicable allocation policies, staff roles, credentials, location and skill, and delivers allocated work to the target worker, device or team queue.
    - *Exposed Service*: `Work Dispatch Service` — Delivers allocated work and captures distinct technical and operational acknowledgements; acknowledgement alone does not establish completion.
    - *Additional Feature relationships*: `FEAT-HSO-20 — Worker Matching & Allocation` is realised by the existing allocation behaviour; `FEAT-HSO-21 — Work Dispatch Delivery` by its dispatch exposure and acknowledgement capture. Applicable credential/privilege determinations remain separately governed; Role information is not itself privilege.
  - **Feature: Work Progress Oversight & Escalation**:
    - *Function*: `Manage Operational Work Progression` — Tracks execution states: *Queued* $\to$ *Assigned* $\to$ *Accepted* $\to$ *In-Progress* $\to$ *Completed / Aborted*.
    - *Exposed Service*: `Operational Work Status Query` — Discloses task progression to requesting wards.
    - *Governed Process*: **Operational Work Progression Process**.
- **Information Responsibility**: Operational Work Dispatch Ledger, Job Progression Milestone Ledger.

---

### 2.12 Credential Management
- **Owning Capability**: `Credential Management`
- **Functions & Exposed Services**:
  - **Capability-scoped behaviour: Operational Credential Check** (Feature association not established):
    - *Distinction*: Operational badge/access `Credential Management` is distinct from Strategy `Clinical Qualification Management`. Lexical similarity does not bind this behaviour to qualification/competency Feature `FEAT-HSO-23`.
    - *Function*: `Verify Operational Access Credential` — Verifies physical access badges, specialty ward entry authorizations, and restricted area permissions.
    - *Exposed Service*: `Operational Credential Checkpoint Service` — Evaluates physical/operational access rights.
- **Information Responsibility**: Operational Access Authorization Ledger, Badge Credential Binding Register.

---

### 2.13 Patient Transport
- **Owning Capability**: `Patient Transport`
- **Functions & Exposed Services**:
  - **Feature: Transport Dispatch & Progress Tracking**:
    - *Function*: `Manage Patient Transport Progression` — Coordinates intra-facility and inter-facility patient movements: *Transport Requested* $\to$ *Porter Dispatched* $\to$ *Patient Collected* $\to$ *In-Transit* $\to$ *Delivered at Destination* $\to$ *Handover Completed*.
    - *Exposed Service*: `Patient Transport Tracking Service` — Provides real-time transit telemetry.
    - *Governed Process*: **Patient Transport Process**.
    - *Additional Feature responsibility*: `FEAT-HSO-24 — Transport Request Ingestion` is directly supported by the request stage of this same Function/Process. It ingests internal portering and external ambulance/inter-hospital requests with clinical mobility requirements; request ingestion is distinct from dispatch, transit and arrival.
- **Information Responsibility**: Patient Transport Request Master, Transit Milestone Log.

---

<a id="214-clinical-logistics"></a>

### 2.14 Clinical Logistics Coordination
- **Owning Capability**: `Clinical Logistics Coordination`
- **Functions & Exposed Services**:
  - **Feature: Pathology Specimen Transport Tracking**:
    - *Function*: `Manage Specimen Transport Progression` — Tracks the physical transit of pathology bio-specimens, blood products, and surgical trays: *Collected* $\to$ *Courier Picked Up* $\to$ *In-Transit* $\to$ *Laboratory Ingress Received*.
    - *Exposed Service*: `Specimen Transit Tracking Service` — Exposes chain-of-custody tracking.
    - *Governed Process*: **Specimen Transport Process**.
- **Information Responsibility**: Specimen Courier Manifest, Chain-of-Custody Tracking Register.

---

### 2.15 Discharge Management
- **Owning Capability**: `Discharge Management`
- **Capacity/Progression Consumer**: May use applicable service capacity and patient progression in discharge coordination. It does not establish the consumed capacity or originating clinical clearances; clinical progression, operational readiness, discharge authorisation and physical departure remain distinct.
- **Functions & Exposed Services**:
  - **Feature: Discharge Readiness & Coordination Oversight**:
    - *Function*: `Manage Discharge Coordination Progression` — Synchronises multi-agency discharge readiness (**illustrative preparation; publication, authorisation and confirmed exit are distinct**): *Discharge Planning Initiated* $\to$ *Medications Reconciled* $\to$ *Transport Booked* $\to$ *Discharge Summary Finalised* $\to$ *Physically Departed*.
    - *Additional Feature relationship*: `FEAT-HSO-27 — Discharge Milestone Tracking` is realised by this existing Function/Process's clinical readiness, medications, transport/services and home-care confirmation milestones.
    - *Departure-triggered responsibility*: `FEAT-HSO-29 — Discharge Notification Dispatch` requires this behaviour to dispatch departure notifications, updated bed states and finalised discharge summaries to external care providers upon confirmed physical exit. Preparation/publication of applicable information may occur earlier; it does not satisfy this departure-triggered responsibility.
    - *Scope qualification*: Preparation, summary finalisation/signing, availability/publication, applicable discharge authorisation and confirmed physical departure remain distinct. The displayed preparation pathway is illustrative, not a universal state machine. Discharge readiness or available information does not establish discharge authorisation or departure.
    - *Exposed Service*: `Discharge Readiness Telemetry` — Discloses discharge barrier checklists and planned departure times.
    - *Governed Process*: **Discharge Progression Process**.
- **Information Responsibility**: Discharge Readiness Checklist Ledger, Estimated Date of Discharge (EDD) Register.

---

<a id="clinical-qualification-management"></a>

### 2.16 Clinical Qualification Management
- **Owning Capability**: `Clinical Qualification Management`.
- **Functions**:
  - **Feature: Operational Credential Check**:
    - *Function*: `Verify Clinical Qualification for Work Dispatch` — Validates that a practitioner possesses active clinical credentials and required competency certificates before dispatch of specialised work.
    - *Derivation*: `FEAT-HSO-23` belongs to Strategy Clinical Qualification Management. Provider Administration establishes professional qualification/registration and privilege information, but the dispatch-specific qualification/competency checkpoint was not expressed by that information management alone.
    - *Boundary*: This checkpoint does not originate qualifications, confer clinical privileges, manage clinical competency training or establish a provider Service dependency. Clinical Privilege and Operational Privilege remain distinct, and an activity may require both. Physical badge/access Credential Management in §2.12 remains independently owned and is not associated with this Feature.
- **Information Responsibility**: Qualification/Competency Checkpoint Outcome and Applicable Work Context; source qualification and privilege information retains its established authority and ownership.

The section number is navigation only. No Capability Tier, ancestry, structural ID, new Role, Service or Process is allocated by this directly derived Function.
