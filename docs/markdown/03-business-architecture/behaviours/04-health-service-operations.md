<!-- continuum-source-succession -->
> **RAD-1.1.5 source succession:** Partial section succession; unwrapped scope/governance and unresolved material retain their recorded roles. See the [canonical continuum](../../../../enterprise-architecture/continuum/documentation/published/1.1.5/README.md) and [section correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json). Original source wording and standing below are preserved.

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
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Clinic & Practice Operations](../../../../enterprise-architecture/continuum/strategy/capability/3dffe63e03124efab255204ee4e9a5d9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Clinic & Practice Operations`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Capability-scoped capacity behaviour**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Clinic & Practice Capacity](../../../../enterprise-architecture/continuum/business/business-function/389fb97b2bcd4527959a61f2a288bc39.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Clinic & Practice Capacity` — Understands and manages available, committed and utilised clinic/practice capacity in relation to practitioner availability, sessions, rooms, appointments and patient demand.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Clinic & Practice Capacity](../../../../enterprise-architecture/continuum/business/business-function/389fb97b2bcd4527959a61f2a288bc39.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Responsibility boundary*: Operating-session tracking and individual queue progression contribute to this responsibility; neither alone expresses overall service capacity. Capacity coordination does not own source bookings, clinical decisions or practitioner rostering.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Clinic Session Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Track Clinic Operating Session](../../../../enterprise-architecture/continuum/business/business-function/83a85a08803c4c00b8de4a38e8c9d77e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Track Clinic Operating Session` — Ingests and tracks clinic operating-session start, delay and completion, distinct from an individual patient's queue progression.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Derivation*: `FEAT-HSO-01` directly establishes this session-level behaviour; Manage Clinic Queue Progression and its Process concern individual patients and do not supply it.
    - *Capacity contribution*: Session start/delay/completion remains distinct observable Business behaviour contributing to clinic/practice capacity; it is not merged into capacity merely for structural neatness.
  - **Feature: Operational Queue Progression**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Clinic Queue Progression](../../../../enterprise-architecture/continuum/business/business-function/cb3f8c48623b4479988c45de86d5a59f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Clinic Queue Progression` — Tracks patient arrival, rooming, consultation in-progress, and check-out states.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinic Queue Telemetry Service](../../../../enterprise-architecture/continuum/business/business-service/c720457349e644179c98d13be59bd581.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Clinic Queue Telemetry Service` — Discloses real-time clinic waiting times and queue states.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Clinic Queue Progression](../../../../enterprise-architecture/continuum/business/business-function/cb3f8c48623b4479988c45de86d5a59f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Clinic / Operational Queue Progression Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Clinic & Practice Capacity, Clinic Session Progression, Clinic Queue State, Patient Waiting Time Metrics.

---

### 2.2 Ward Operations
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Ward Operations](../../../../enterprise-architecture/continuum/strategy/capability/f996ecca728f4942a88b034aca818a99.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Ward Operations`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Capability-scoped capacity behaviour**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Ward Capacity](../../../../enterprise-architecture/continuum/business/business-function/eca5f4fbf92743f987a01930dc4ee763.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Ward Capacity` — Coordinates available, committed and utilised ward capacity and its patient-flow implications, retaining acuity distribution, nurse-to-patient ratios and isolation cohorting alongside bed/care-place state, patient demand and operational constraints.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Ward Capacity](../../../../enterprise-architecture/continuum/business/business-function/eca5f4fbf92743f987a01930dc4ee763.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Name Alias*: `Coordinate Ward Operational State` — the same established ward operational responsibility, clarified as Capacity Management; no new Function identity or identifier is allocated.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ward Operational Summary Query](../../../../enterprise-architecture/continuum/business/business-service/55a7f84dfb4142e2aa96c5253b4b6394.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Ward Operational Summary Query` — Discloses ward-level operational telemetry.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Ward Capacity](../../../../enterprise-architecture/continuum/business/business-function/eca5f4fbf92743f987a01930dc4ee763.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Existing Feature contribution*: Ward Patient Flow Coordination concerns ward occupancy, care-place allocation and patient bed status. Those patient-flow responsibilities remain within ward operational coordination; they do not define the whole capacity responsibility.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ward Operational Summary Query](../../../../enterprise-architecture/continuum/business/business-service/55a7f84dfb4142e2aa96c5253b4b6394.md)
- [Manage Ward Capacity](../../../../enterprise-architecture/continuum/business/business-function/eca5f4fbf92743f987a01930dc4ee763.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Capacity boundary*: Bed & Care-Place Management contributes resource state. Ward capacity is assessed in its service context; an available bed alone does not establish capacity to accept a patient. Source staffing and clinical assertions retain their authority.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Clinical Handover Context Assembly**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Assemble Ward Handover Context](../../../../enterprise-architecture/continuum/business/business-function/6006d70ed3794c57ab2bc57119292e4b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Assemble Ward Handover Context` — Collates current ward patient lists, active medical concerns and pending tasks for nursing shift handover.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Derivation*: `FEAT-HSO-04` directly requires this assembly. Ward acuity/cohorting telemetry alone does not provide the stated handover content. Source clinical information retains its originating authority; assembly does not perform clinical review, manage clinical handover, determine pending clinical work or acquire clinical judgement.
- **Information Responsibility**: Ward Capacity, Ward Shift Status Ledger, Acuity Balance Register, Ward Handover Context.

---

### 2.3 Theatre Operations
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Theatre Operations](../../../../enterprise-architecture/continuum/strategy/capability/4bf7a8260e1444fdac44e97c3557b533.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Theatre Operations`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Capability-scoped capacity behaviour**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Theatre Capacity](../../../../enterprise-architecture/continuum/business/business-function/3566d549770f490dba823fe3b0421446.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Theatre Capacity` — Understands and manages available, committed and utilised theatre capacity in relation to theatre, team and equipment availability, scheduled cases and case progression.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Theatre Capacity](../../../../enterprise-architecture/continuum/business/business-function/3566d549770f490dba823fe3b0421446.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Responsibility boundary*: Case milestones and targeted resource notifications contribute; they do not by themselves establish available or committed theatre service capacity. Clinical case determination and originating team/equipment information remain separately governed.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Operating Theatre Case Progression**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Theatre Case Progression](../../../../enterprise-architecture/continuum/business/business-function/88d2d17cccfb4a578b835be588043bab.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Theatre Case Progression` — Coordinates surgical case milestones: *Case Called* $\to$ *Patient in Anaesthetic Bay* $\to$ *Anaesthesia Commenced* $\to$ *Knife to Skin* $\to$ *Procedure Finished* $\to$ *In PACU/Recovery* $\to$ *Ward Handover Complete*.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Theatre Case Progression Telemetry](../../../../enterprise-architecture/continuum/business/business-service/15fca4d52810448785ca3c70b899a294.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Theatre Case Progression Telemetry` — Emits real-time surgical milestone updates.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Theatre Case Progression](../../../../enterprise-architecture/continuum/business/business-function/88d2d17cccfb4a578b835be588043bab.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Additional Feature responsibility*: `FEAT-HSO-06 — Perioperative Resource Notification` is realised by dispatching progression-triggered alerts to portering, sterilisation and recovery teams within this same case-progression behaviour/exposure. Milestone observation alone does not establish notification; no new Function or Service is created.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Theatre Case Progression](../../../../enterprise-architecture/continuum/business/business-function/88d2d17cccfb4a578b835be588043bab.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Theatre Case Progression Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Theatre Capacity, Theatre Slate Register, Perioperative Milestone Log.

---

### 2.4 Emergency Department Operations
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Emergency Department Operations](../../../../enterprise-architecture/continuum/strategy/capability/24a4b5815faa4d369bc756a6e97d3e9e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Emergency Department Operations`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Capability-scoped capacity behaviour**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate ED Departmental Capacity](../../../../enterprise-architecture/continuum/business/business-function/a1559e7ed12c4f249128fba2ff74d536.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Coordinate ED Departmental Capacity` — Understands and manages available, committed and utilised ED capacity in relation to care-place availability, demand, patient presence/progression, acuity and operational constraints; retains waiting room occupancy, resuscitation bay availability and time-to-triage metrics.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [ED Operational Status Query](../../../../enterprise-architecture/continuum/business/business-service/f07828653c06425f9effd92b1d9886c9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `ED Operational Status Query` — Discloses facility ED occupancy and surge level.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Existing Feature scope*: ED Length of Stay Tracking retains the existing wait/progression metrics context; capacity responsibility is broader than that Feature. This clarification does not assert a new whole-Feature realisation relationship.
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate ED Departmental Capacity](../../../../enterprise-architecture/continuum/business/business-function/a1559e7ed12c4f249128fba2ff74d536.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Additional Feature relationship*: `FEAT-HSO-07 — ED Bed & Bay Tracking` is materially realised by this capacity-tracking behaviour for occupancy/status of emergency cubicles, resuscitation bays and short-stay beds. Those care-place states are explicit within the existing Function and its operational status exposure.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate ED Departmental Capacity](../../../../enterprise-architecture/continuum/business/business-function/a1559e7ed12c4f249128fba2ff74d536.md)
- [ED Operational Status Query](../../../../enterprise-architecture/continuum/business/business-service/f07828653c06425f9effd92b1d9886c9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Capacity boundary*: Existing departmental capacity responsibility is clarified, not duplicated. Care-place state contributes to service capacity; capacity coordination neither performs clinical triage nor establishes clinical acuity assertions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: ED Capacity, ED Operational Dashboard Ledger, Resuscitation Bay Occupancy Matrix.

---

### 2.5 Outpatient Operations
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Outpatient Operations](../../../../enterprise-architecture/continuum/strategy/capability/21399d52b9624ddfbeb786f1d72e40c3.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Outpatient Operations`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Outpatient Capacity](../../../../enterprise-architecture/continuum/business/business-function/45fd6190b7334f838575881725f6a66b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Outpatient Capacity` — Understands and manages available, committed and utilised outpatient capacity through clinic/session, practitioner and room availability, appointment demand, patient arrival/waiting and operational progression. Retains specialty room allocation and clinician arrival tracking.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Outpatient Capacity](../../../../enterprise-architecture/continuum/business/business-function/45fd6190b7334f838575881725f6a66b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Name Alias*: `Coordinate Outpatient Session Utilisation` — the existing responsibility reframed as outpatient Capacity Management; no new Function identity or identifier is allocated.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Outpatient Capacity](../../../../enterprise-architecture/continuum/business/business-function/45fd6190b7334f838575881725f6a66b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature responsibility*: `FEAT-HSO-09 — Outpatient Arrival Registration` is realised within this capacity behaviour by ingesting patient check-in/arrival events and notifying treating clinicians. Patient arrival contributes demand/presence; it does not itself establish capacity. This bounded responsibility does not equate the whole capacity Function with the Feature or create a separate Arrival Registration Function.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ambulatory Session Status Query](../../../../enterprise-architecture/continuum/business/business-service/a790a785f2e148d7a1c99655d53db4e9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Ambulatory Session Status Query` — Exposes clinic room occupancy.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Outpatient Capacity](../../../../enterprise-architecture/continuum/business/business-function/45fd6190b7334f838575881725f6a66b.md)
- [Ambulatory Session Status Query](../../../../enterprise-architecture/continuum/business/business-service/a790a785f2e148d7a1c99655d53db4e9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposure qualification*: Query behaviour and Outpatient Operations ownership remain established; a meaningful outside consumer/mandate is not identified by the current description. That exposure relationship remains unestablished rather than populated with an inferred Actor, Role or Capability.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Outpatient Capacity](../../../../enterprise-architecture/continuum/business/business-function/45fd6190b7334f838575881725f6a66b.md)
- [Ambulatory Session Status Query](../../../../enterprise-architecture/continuum/business/business-service/a790a785f2e148d7a1c99655d53db4e9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Capacity boundary*: Operational coordination does not acquire authoritative booking/rostering or clinical-work management. Room-occupancy query remains narrower than the capacity responsibility; no enlarged exposure or new named Service is inferred.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Post-Clinic Order Coordination**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Capture Post-Clinic Follow-up Requirements](../../../../enterprise-architecture/continuum/business/business-function/9136f75069bf4770bcc8dc395204d475.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Capture Post-Clinic Follow-up Requirements` — Captures follow-up booking and diagnostic-order requirements generated at clinic completion.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Derivation*: `FEAT-HSO-10` directly establishes this capture responsibility. The Clinic queue Process's consultation-concluded checkpoint describes orders placed, not Outpatient Operations' capture of follow-up requirements. Capture does not authorise clinical orders, own authoritative bookings or establish a new Service dependency.
- **Information Responsibility**: Outpatient Capacity, Outpatient Arrival/Waiting Demand, Outpatient Session Schedule, Room Allocation Register, Post-Clinic Follow-up Requirements.

---

### 2.6 Bed & Care-Place Management
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Bed & Care-Place Management](../../../../enterprise-architecture/continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Bed & Care-Place Management`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Operational State vs. Definition**: While *Location Administration* defines the physical care-place, *Bed & Care-Place Management* owns the real-time operational status (*Available*, *Occupied*, *Reserved*, *Blocked*, *Dirty*, *Cleaning In-Progress*, *Maintenance Lock*).
  - **Resource State vs. Capacity**: This state contributes to Ward Operations' capacity responsibility. Bed availability does not establish ward service capacity or authority to accept another patient.
- **Functions & Exposed Services**:
  - **Feature: Bed Availability Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Operational Bed State](../../../../enterprise-architecture/continuum/business/business-function/22f3016539cf4c2db68baa9921acf31b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Operational Bed State` — Tracks real-time bed status and environmental locks.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Bed Availability Query](../../../../enterprise-architecture/continuum/business/business-service/c51894eccd7b45019c2f084993f4cfc6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Bed Availability Query` — Discloses real-time bed availability across hospital directorates.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Bed State Progression**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Bed Turnover Progression](../../../../enterprise-architecture/continuum/business/business-function/7637b9bcb38d4e3abbe01a59dc23f485.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Bed Turnover Progression` — Coordinates cleaning, disinfection, and preparation: *Vacated* $\to$ *Cleaning Requested* $\to$ *Cleaning In-Progress* $\to$ *Inspected/Ready* $\to$ *Occupied*.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Bed Turnover Lifecycle Service](../../../../enterprise-architecture/continuum/business/business-service/f0ab3847b1bf41869a85b1a5ef217429.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Bed Turnover Lifecycle Service` — Coordinates turnover requests with environmental services.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Bed Turnover Progression](../../../../enterprise-architecture/continuum/business/business-function/7637b9bcb38d4e3abbe01a59dc23f485.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Additional Feature responsibility*: `FEAT-HSO-13 — Bed Readiness Notification` requires the existing turnover behaviour/exposure to notify bed allocation managers and clinical units when a cleaned bed is ready. Readiness notification remains distinct from availability query and from physical placement; no new lifecycle state or Service is introduced.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Bed Turnover Progression](../../../../enterprise-architecture/continuum/business/business-function/7637b9bcb38d4e3abbe01a59dc23f485.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Bed Turnover Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Operational Bed Status Registry, Care-Place Lock Register, Bed Turnover Milestone Log.

---

### 2.7 Clinical Resource Management
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Clinical Resource Management](../../../../enterprise-architecture/continuum/strategy/capability/a5faedcd97064c28b9e7e551e9106ea1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Clinical Resource Management`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Feature: Mobile Clinical Equipment Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Track Mobile Clinical Asset Operational State](../../../../enterprise-architecture/continuum/business/business-function/0a31e03a6c7847c0b7c04310ca0c29e9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Track Mobile Clinical Asset Operational State` — Manages the real-time location and operational readiness of mobile ventilators, infusion pumps, and telemetry transmitters.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Resource Availability Query](../../../../enterprise-architecture/continuum/business/business-service/0e5ed162a288425aad2833ed5e07ed64.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Clinical Resource Availability Query` — Exposes mobile equipment availability.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Mobile Clinical Asset Operational Ledger, Maintenance Readiness Matrix.

---

### 2.8 Service Capacity Management
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Service Capacity Management](../../../../enterprise-architecture/continuum/strategy/capability/d86105bf9c86445394088f65c895d37f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Service Capacity Management`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Feature: Operational Capacity Metric Aggregation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Aggregate Service Capacity Telemetry](../../../../enterprise-architecture/continuum/business/business-function/ac08378c955048ef81ed5d9ff9c5cc7e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Aggregate Service Capacity Telemetry` — Consumes and coordinates context-specific service-capacity information for broader situational awareness, alongside bed occupancy, ICU surge capacity and ventilator availability across regional hospitals.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Regional Health System Capacity Query](../../../../enterprise-architecture/continuum/business/business-service/ecbb1944572d44dbba55aaf565559d13.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Regional Health System Capacity Query` — Provides executive operational situational awareness.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Aggregate Service Capacity Telemetry](../../../../enterprise-architecture/continuum/business/business-function/ac08378c955048ef81ed5d9ff9c5cc7e.md)
- [Regional Health System Capacity Query](../../../../enterprise-architecture/continuum/business/business-service/ecbb1944572d44dbba55aaf565559d13.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Consumer boundary*: Aggregation does not establish the underlying context-specific capacity. Resource occupancy/readiness and service capacity remain distinguishable; exact additional provider contracts are unestablished.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Regional System Capacity Snapshot, Surge Level State.

---

### 2.9 Mobile Staff Management
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Mobile Staff Management](../../../../enterprise-architecture/continuum/strategy/capability/79a4a48fddf74c69acf7e16404d06ea5.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Mobile Staff Management`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Capability-scoped capacity behaviour**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Mobile Service Capacity](../../../../enterprise-architecture/continuum/business/business-function/ad5cb5451ef645acb55ceb0c942ea013.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Mobile Service Capacity` — Understands and manages available, committed and utilised mobile-service capacity in relation to workforce availability, geography, time, workload and applicable service demand.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Mobile Service Capacity](../../../../enterprise-architecture/continuum/business/business-function/ad5cb5451ef645acb55ceb0c942ea013.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Responsibility boundary*: Staff presence and externally established assignments/visit progression contribute to capacity; they do not alone express it. Capacity coordination does not determine clinical work, own the source workforce roster or allocate underlying clinical duties.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Track Operational Staff Presence](../../../../enterprise-architecture/continuum/business/business-function/9ccdb235a69946e0ad5a99001a1d16e0.md)
- [Staff Presence Telemetry Query](../../../../enterprise-architecture/continuum/business/business-service/b3cd0d8e40f44b9fa2b64367237345fa.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: `FEAT-HSO-16 — Mobile Worker Task Dispatch` retains its Strategy identity with the integration boundary below. Presence/zone tracking remains distinct from mobile work-management integration; no replacement association to presence is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Track Operational Staff Presence](../../../../enterprise-architecture/continuum/business/business-function/9ccdb235a69946e0ad5a99001a1d16e0.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Track Operational Staff Presence` — Tracks active on-duty presence and physical zone assignment for porters, phlebotomists, and roving nurses.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Staff Presence Telemetry Query](../../../../enterprise-architecture/continuum/business/business-service/b3cd0d8e40f44b9fa2b64367237345fa.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Staff Presence Telemetry Query` — Discloses on-duty staff availability by hospital zone.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Mobile Visit Status Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Mobile Visit Progression](../../../../enterprise-architecture/continuum/business/business-function/71700f211ef4403f98a2b9b11d8d7848.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Mobile Visit Progression` — Ingests arrival, visit-progression and safety check-in milestones from roving staff.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Derivation*: `FEAT-HSO-17` directly requires visit-specific milestone ingestion. It remains distinct from presence, capacity assessment and work-management integration. Received progression can inform mobile capacity without determining clinical work or clinical completion.
  - **Feature: Mobile Worker Task Dispatch** (integration scope qualified in Strategy):
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Integrate Mobile Work Management](../../../../enterprise-architecture/continuum/business/business-function/81f97ad819da48f09f26029066550e08.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Integrate Mobile Work Management` — Exchanges externally established work/assignment, dispatch and operational progression/status information, with applicable worker/context information, between mobile work-management mechanisms and Harmonia operational coordination.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Integrate Mobile Work Management](../../../../enterprise-architecture/continuum/business/business-function/81f97ad819da48f09f26029066550e08.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Derivation and boundary*: The human adjudication of `FEAT-HSO-16` establishes integration with mobile work/task management. This responsibility is absent from presence observation and visit-specific milestone ingestion. It does not determine, allocate or manage the underlying clinical work, clinical handover or EMR worklists. Work Allocation & Dispatch retains non-clinical operational Work Order responsibility.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Integrate Mobile Work Management](../../../../enterprise-architecture/continuum/business/business-function/81f97ad819da48f09f26029066550e08.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Terminology*: External “work/task” vocabulary does not make human work a Harmonia Task. Work Order means human doing; To Do means human review/update/approval; Task means synthetic task. The preferred Strategy name `Mobile Work Task Management Integration` remains a proposed naming correction pending human confirmation, not a new Feature or alias.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Mobile Service Capacity, Staff Operational Presence Register, Zone Allocation Matrix, Mobile Visit Progression, Exchanged Mobile Work-Management Information. Originating work/assignment authority remains with its external/business work-management source.

---

### 2.10 On-Call Management
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] On-Call Management](../../../../enterprise-architecture/continuum/strategy/capability/b16cef8f2bfd4b92948ec75cbcfa7b9a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `On-Call Management`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Feature: Active On-Call Provider Resolution**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Resolve Active On-Call Coverage](../../../../enterprise-architecture/continuum/business/business-function/a88d3779091140c6b90f98d550b5387f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Resolve Active On-Call Coverage` — Resolves primary, secondary, and tertiary on-call clinical specialists for emergency escalation.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [On-Call Specialist Lookup](../../../../enterprise-architecture/continuum/business/business-service/e0ba307dc51e47a395c1eac9fe728a98.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `On-Call Specialist Lookup` — Discloses active on-call contacts for switchboard and emergency teams.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: On-Call Roster Projection, Escalation Contact Matrix.

---

### 2.11 Work Allocation & Dispatch
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Work Allocation & Dispatch](../../../../enterprise-architecture/continuum/strategy/capability/152a5252952c405994e9b171234efcb0.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Work Allocation & Dispatch`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Ownership Guardrail**: Owns the dispatching, queuing, assignment, and operational progression of non-clinical work orders (e.g., patient transfers, cleaning, specimen pickups). It does **not** acquire ownership of Practitioners, Locations, Services, or Credentials.
  - **Capacity Consumer**: May use context-specific capacity/availability in operational allocation; it neither establishes that capacity nor acquires clinical-work allocation authority through mobile integration.
- **Functions & Exposed Services**:
  - **Feature: Work Item Instantiation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Dispatch Operational Work Order](../../../../enterprise-architecture/continuum/business/business-function/05a14489d3e943499581620356130007.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Dispatch Operational Work Order` — Allocates work requests to available wardspersons or support teams using applicable allocation policies, staff roles, credentials, location and skill, and delivers allocated work to the target worker, device or team queue.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Work Dispatch Service](../../../../enterprise-architecture/continuum/business/business-service/7410926f15d3480eaa856d998d67d83f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Work Dispatch Service` — Delivers allocated work and captures distinct technical and operational acknowledgements; acknowledgement alone does not establish completion.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Dispatch Operational Work Order](../../../../enterprise-architecture/continuum/business/business-function/05a14489d3e943499581620356130007.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Additional Feature relationships*: `FEAT-HSO-20 — Worker Matching & Allocation` is realised by the existing allocation behaviour; `FEAT-HSO-21 — Work Dispatch Delivery` by its dispatch exposure and acknowledgement capture. Applicable credential/privilege determinations remain separately governed; Role information is not itself privilege.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Work Progress Oversight & Escalation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Operational Work Progression](../../../../enterprise-architecture/continuum/business/business-function/ebc35951617a4f92863922b74dd1c1f4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Operational Work Progression` — Tracks execution states: *Queued* $\to$ *Assigned* $\to$ *Accepted* $\to$ *In-Progress* $\to$ *Completed / Aborted*.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Operational Work Status Query](../../../../enterprise-architecture/continuum/business/business-service/8659408923e641b99727ef218e2b8dfc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Operational Work Status Query` — Discloses task progression to requesting wards.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Operational Work Progression](../../../../enterprise-architecture/continuum/business/business-function/ebc35951617a4f92863922b74dd1c1f4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Operational Work Progression Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Operational Work Dispatch Ledger, Job Progression Milestone Ledger.

---

### 2.12 Credential Management
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Credential Management](../../../../enterprise-architecture/continuum/strategy/capability/3a7a4d2d0ce8446b8a6293284fb16b83.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Credential Management`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Operational Credential Checkpoint Service](../../../../enterprise-architecture/continuum/business/business-service/7ecc60e34e07491588b106b9075ab09a.md)
- [Verify Operational Access Credential](../../../../enterprise-architecture/continuum/business/business-function/a0eb34a7cb7046fd880248e799632810.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

  - **Capability-scoped behaviour: Operational Credential Check** (Feature association not established):


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Distinction*: Operational badge/access `Credential Management` is distinct from Strategy `Clinical Qualification Management`. Lexical similarity does not bind this behaviour to qualification/competency Feature `FEAT-HSO-23`.
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Verify Operational Access Credential](../../../../enterprise-architecture/continuum/business/business-function/a0eb34a7cb7046fd880248e799632810.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Verify Operational Access Credential` — Verifies physical access badges, specialty ward entry authorizations, and restricted area permissions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Operational Credential Checkpoint Service](../../../../enterprise-architecture/continuum/business/business-service/7ecc60e34e07491588b106b9075ab09a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Operational Credential Checkpoint Service` — Evaluates physical/operational access rights.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Operational Access Authorization Ledger, Badge Credential Binding Register.

---

### 2.13 Patient Transport
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Patient Transport](../../../../enterprise-architecture/continuum/strategy/capability/6041d7c0cc5d4519aaea584bb4e6d365.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Patient Transport`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Feature: Transport Dispatch & Progress Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Patient Transport Progression](../../../../enterprise-architecture/continuum/business/business-function/8a2136544646458fb60413afed2b66e1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Patient Transport Progression` — Coordinates intra-facility and inter-facility patient movements: *Transport Requested* $\to$ *Porter Dispatched* $\to$ *Patient Collected* $\to$ *In-Transit* $\to$ *Delivered at Destination* $\to$ *Handover Completed*.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Patient Transport Tracking Service](../../../../enterprise-architecture/continuum/business/business-service/8d71168ac7da48e990757b2a0ae3670f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Patient Transport Tracking Service` — Provides real-time transit telemetry.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Patient Transport Progression](../../../../enterprise-architecture/continuum/business/business-function/8a2136544646458fb60413afed2b66e1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Patient Transport Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Patient Transport Progression](../../../../enterprise-architecture/continuum/business/business-function/8a2136544646458fb60413afed2b66e1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Additional Feature responsibility*: `FEAT-HSO-24 — Transport Request Ingestion` is directly supported by the request stage of this same Function/Process. It ingests internal portering and external ambulance/inter-hospital requests with clinical mobility requirements; request ingestion is distinct from dispatch, transit and arrival.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Patient Transport Request Master, Transit Milestone Log.

---

<a id="214-clinical-logistics"></a>

### 2.14 Clinical Logistics Coordination
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Clinical Logistics Coordination](../../../../enterprise-architecture/continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Clinical Logistics Coordination`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Feature: Pathology Specimen Transport Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Specimen Transport Progression](../../../../enterprise-architecture/continuum/business/business-function/4c9d50efc9e640fcb3abb02b8dd05486.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Specimen Transport Progression` — Tracks the physical transit of pathology bio-specimens, blood products, and surgical trays: *Collected* $\to$ *Courier Picked Up* $\to$ *In-Transit* $\to$ *Laboratory Ingress Received*.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Specimen Transit Tracking Service](../../../../enterprise-architecture/continuum/business/business-service/100e740770bc4ab28cfe3c9af4b541cd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Specimen Transit Tracking Service` — Exposes chain-of-custody tracking.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Specimen Transport Progression](../../../../enterprise-architecture/continuum/business/business-function/4c9d50efc9e640fcb3abb02b8dd05486.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Specimen Transport Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Specimen Courier Manifest, Chain-of-Custody Tracking Register.

---

### 2.15 Discharge Management
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Discharge Management](../../../../enterprise-architecture/continuum/strategy/capability/ec5e8d6732764aa489c2dd7630fc8fef.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Discharge Management`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Capacity/Progression Consumer**: May use applicable service capacity and patient progression in discharge coordination. It does not establish the consumed capacity or originating clinical clearances; clinical progression, operational readiness, discharge authorisation and physical departure remain distinct.
- **Functions & Exposed Services**:
  - **Feature: Discharge Readiness & Coordination Oversight**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Discharge Coordination Progression](../../../../enterprise-architecture/continuum/business/business-function/ecda6d08166d4df78c15c87387fbe108.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Discharge Coordination Progression` — Synchronises multi-agency discharge readiness (**illustrative preparation; publication, authorisation and confirmed exit are distinct**): *Discharge Planning Initiated* $\to$ *Medications Reconciled* $\to$ *Transport Booked* $\to$ *Discharge Summary Finalised* $\to$ *Physically Departed*.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Discharge Coordination Progression](../../../../enterprise-architecture/continuum/business/business-function/ecda6d08166d4df78c15c87387fbe108.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Additional Feature relationship*: `FEAT-HSO-27 — Discharge Milestone Tracking` is realised by this existing Function/Process's clinical readiness, medications, transport/services and home-care confirmation milestones.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Discharge Coordination Progression](../../../../enterprise-architecture/continuum/business/business-function/ecda6d08166d4df78c15c87387fbe108.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Departure-triggered responsibility*: `FEAT-HSO-29 — Discharge Notification Dispatch` requires this behaviour to dispatch departure notifications, updated bed states and finalised discharge summaries to external care providers upon confirmed physical exit. Preparation/publication of applicable information may occur earlier; it does not satisfy this departure-triggered responsibility.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Discharge Readiness Telemetry](../../../../enterprise-architecture/continuum/business/business-service/03d01a9b024c4497badf3fe011a8dd20.md)
- [Manage Discharge Coordination Progression](../../../../enterprise-architecture/continuum/business/business-function/ecda6d08166d4df78c15c87387fbe108.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Scope qualification*: Preparation, summary finalisation/signing, availability/publication, applicable discharge authorisation and confirmed physical departure remain distinct. The displayed preparation pathway is illustrative, not a universal state machine. Discharge readiness or available information does not establish discharge authorisation or departure.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Discharge Readiness Telemetry](../../../../enterprise-architecture/continuum/business/business-service/03d01a9b024c4497badf3fe011a8dd20.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Discharge Readiness Telemetry` — Discloses discharge barrier checklists and planned departure times.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Discharge Coordination Progression](../../../../enterprise-architecture/continuum/business/business-function/ecda6d08166d4df78c15c87387fbe108.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Discharge Progression Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Discharge Readiness Checklist Ledger, Estimated Date of Discharge (EDD) Register.

---

<a id="clinical-qualification-management"></a>

### 2.16 Clinical Qualification Management
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Clinical Qualification Administration](../../../../enterprise-architecture/continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Clinical Qualification Management`.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions**:
  - **Feature: Operational Credential Check**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Verify Clinical Qualification for Work Dispatch](../../../../enterprise-architecture/continuum/business/business-function/1dfebc3aa5f949e5a20faa504eadbee1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Verify Clinical Qualification for Work Dispatch` — Validates that a practitioner possesses active clinical credentials and required competency certificates before dispatch of specialised work.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Derivation*: `FEAT-HSO-23` belongs to Strategy Clinical Qualification Management. Provider Administration establishes professional qualification/registration and privilege information, but the dispatch-specific qualification/competency checkpoint was not expressed by that information management alone.
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Verify Clinical Qualification for Work Dispatch](../../../../enterprise-architecture/continuum/business/business-function/1dfebc3aa5f949e5a20faa504eadbee1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Boundary*: This checkpoint does not originate qualifications, confer clinical privileges, manage clinical competency training or establish a provider Service dependency. Clinical Privilege and Operational Privilege remain distinct, and an activity may require both. Physical badge/access Credential Management in §2.12 remains independently owned and is not associated with this Feature.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Qualification/Competency Checkpoint Outcome and Applicable Work Context; source qualification and privilege information retains its established authority and ownership.

The section number is navigation only. No Capability Tier, ancestry, structural ID, new Role, Service or Process is allocated by this directly derived Function.
