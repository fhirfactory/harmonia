<!-- continuum-source-succession -->
> **RAD-1.1.5 source succession:** Partial section succession; unwrapped scope/governance and unresolved material retain their recorded roles. See the [canonical continuum](../../../../enterprise-architecture/continuum/documentation/published/1.1.5/README.md) and [section correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json). Original source wording and standing below are preserved.

# Service Administration — Capability-Scoped Behaviour

## 1. Contextual Scope & Architectural Intent

The named owning Capabilities and established Features retain their responsibilities. Affected Capability Tier, complete ancestry, root status and structural Canonical IDs remain unresolved under [approved G1 K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing); document grouping and numbering do not establish architectural identity or hierarchy.

Service Administration governs the clinical administrative lifecycles, coordination workflows, and documentation exchanges that surround patient care episodes. It coordinates referrals, appointments, encounters, orders, diagnostic reports, medication events, care plans, and clinical documents across care boundaries.

---

## 2. Client-Centred Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Client-Centred Administration](../../../../enterprise-architecture/continuum/strategy/capability/8752b7a937754b2faa296300c6a48911.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Client-Centred Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Administrative Coordination**: Handles operational demographic intake and administrative updates across departmental boundaries.
  - **Ownership Invariant**: Does **not** own authoritative person identity or identifier correlation (which is owned by *Client Administration / Person Identity*).
- **Functions & Exposed Services**:
  - **Feature: Demographic Update Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Client Demographic Change](../../../../enterprise-architecture/continuum/business/business-function/459db1af34724863a57961ed921763d6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Client Demographic Change` — Ingests administrative demographic updates from PAS/registration feeds.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Demographic Intake Service](../../../../enterprise-architecture/continuum/business/business-service/b6d8aab087944a6e8cc246b85d32843e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Demographic Intake Service` — Exposed to departmental registration systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Demographic Change Distribution**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Distribute Governed Demographic Change](../../../../enterprise-architecture/continuum/business/business-function/61a4ae56602c4e2d9428169188e458d8.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Distribute Governed Demographic Change` — Propagates verified demographic modifications to subscribed clinical subsystems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Demographic Update Notification](../../../../enterprise-architecture/continuum/business/business-service/10b54e9ef0ed47e491d18731c58247f5.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Demographic Update Notification` — Emits change notifications across the HIE.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Administrative Intake Ledger, Subscribed Distribution Registry.

---

## 3. Referral Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Referral Administration](../../../../enterprise-architecture/continuum/strategy/capability/ae0de25600ee416aa24d2a167b64f230.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Referral Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Referral vs. Order**: A **Referral** formally requests another care participant or service provider to assess, manage or assume some degree of clinical care responsibility. Intent, disposition, responsibility actually assumed and Transfer of Care are distinct; Referral itself establishes none of acceptance, delivery, assumption or Transfer of Care, and has no universal Appointment, Encounter or Order consequence. An **Order** concerns *bounded fulfilment of a specific diagnostic or therapeutic task*.
- **Functions & Exposed Services**:
  - **Feature: Referral Document Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Referral](../../../../enterprise-architecture/continuum/business/business-function/28775f5af15646369539a4fc0a7ff8a9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Referral` — Validates and ingests structured electronic referrals, clinical indications, and priority categories.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Referral Submission Service](../../../../enterprise-architecture/continuum/business/business-service/42b4e9da22764480a0bea7019927860c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Referral Submission Service` — Exposed to external GP practices and referring health facilities.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Referral Supporting Information**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Assemble Referral Context](../../../../enterprise-architecture/continuum/business/business-function/b4a05bf97e844c24b04d27a6b325627a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Assemble Referral Context` — Gathers relevant clinical history, medications, and diagnostic investigations into the referral package.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Referral Context Query](../../../../enterprise-architecture/continuum/business/business-service/d688ee73f68a4dda9380404e22b3539b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Referral Context Query` — Provides comprehensive referral dossiers to intake triage clinicians.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Referral Status & Outcome Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Referral Progression](../../../../enterprise-architecture/continuum/business/business-function/7f9f01be4fd14facb943de5f63f1d8fd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Referral Progression` — Tracks referral disposition in an **illustrative specialist pathway; dispositions vary**: *Submitted* $\to$ *Triaged* $\to$ *Accepted/Waitlisted* $\to$ *Scheduled* $\to$ *Discharged*.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Referral Status & Outcome Service](../../../../enterprise-architecture/continuum/business/business-service/dfd0481be6124da0a791db41a5e9c238.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Referral Status & Outcome Service` — Exposes progression milestones to referrers and patients.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Referral Progression](../../../../enterprise-architecture/continuum/business/business-function/7f9f01be4fd14facb943de5f63f1d8fd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Referral Progression Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Referral Progression](../../../../enterprise-architecture/continuum/business/business-function/7f9f01be4fd14facb943de5f63f1d8fd.md)
- [Referral Status & Outcome Service](../../../../enterprise-architecture/continuum/business/business-service/dfd0481be6124da0a791db41a5e9c238.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Scope qualification*: The displayed specialist pathway is illustrative, not mandatory for every Referral; acceptance, waitlisting, decline and redirection retain their contextual meanings.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Referral Master Record, Triage Decision Ledger, Referral Disposition Log.

---

## 4. Scheduling Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Scheduling Administration](../../../../enterprise-architecture/continuum/strategy/capability/35fb3744c900470f9895056246a13fb1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Scheduling Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Coordination vs. Authoritative Booking**: Harmonia synchronises and communicates appointment milestones; it does **not** replace the authoritative booking/scheduling engines of host PAS or departmental scheduling platforms.
- **Functions & Exposed Services**:
  - **Feature: Appointment Notification Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Appointment Notification](../../../../enterprise-architecture/continuum/business/business-function/8aa2e7b197ae4a0c99b654b9f1a312e8.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Appointment Notification` — Ingests appointment creation, rescheduling, cancellation, and check-in events from host PAS engines.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Appointment Notification Ingress](../../../../enterprise-architecture/continuum/business/business-service/67045c71e2194f6ba19002edcea94ee0.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Appointment Notification Ingress` — Ingests booking events from departmental schedulers.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Appointment Status Synchronization**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Synchronise Appointment Status](../../../../enterprise-architecture/continuum/business/business-function/c60fc8ce05104185b5240a910bfd5f67.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Synchronise Appointment Status` — Projects synchronised appointment timelines to clinical dashboards and care coordinators.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Appointment Schedule Query](../../../../enterprise-architecture/continuum/business/business-service/2a7eb5b22dc04256aa049c2fcf84e62c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Appointment Schedule Query` — Exposes consolidated patient appointment schedules across facilities.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Appointment Event History, Schedule Synchronisation State.

---

## 5. Episode & Encounter Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Episode & Encounter Administration](../../../../enterprise-architecture/continuum/strategy/capability/61d7067aee0540259b3daca4287d66d2.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Episode & Encounter Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Encounter Context**: Tracks the clinical and administrative interaction between a patient and healthcare provider during an emergency, inpatient, or outpatient episode.
- **Functions & Exposed Services**:
  - **Feature: Encounter Context Creation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Establish Encounter Context](../../../../enterprise-architecture/continuum/business/business-function/df00c187d509483a8408304a066abec4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Establish Encounter Context` — Binds patient, attending practitioner, clinical class (e.g., Inpatient, Emergency, Ambulatory), and admitting diagnosis to a unique encounter identifier.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Encounter Context Resolution](../../../../enterprise-architecture/continuum/business/business-service/ca22b0a4e51b4f5eb6b528bc36fa5d9c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Encounter Context Resolution` — Discloses active encounter details to clinical applications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Encounter State Progression**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Progress Encounter State](../../../../enterprise-architecture/continuum/business/business-function/223b1140628b444482bfb0a2c870d751.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Progress Encounter State` — Coordinates encounter progression (**scoped illustration; ON_LEAVE relationship unresolved**): *Planned* $\to$ *Arrived* $\to$ *In-Progress* $\to$ *Discharged* $\to$ *Completed*.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Encounter Lifecycle Event Notification](../../../../enterprise-architecture/continuum/business/business-service/e9c6b81172984785a47d222ac0c28d45.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Encounter Lifecycle Event Notification` — Emits encounter transition events across the HIE.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Progress Encounter State](../../../../enterprise-architecture/continuum/business/business-function/223b1140628b444482bfb0a2c870d751.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Encounter Lifecycle Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Progress Encounter State](../../../../enterprise-architecture/continuum/business/business-function/223b1140628b444482bfb0a2c870d751.md)
- [Encounter Lifecycle Event Notification](../../../../enterprise-architecture/continuum/business/business-service/e9c6b81172984785a47d222ac0c28d45.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Scope qualification*: Process completion qualifications remain local; their correspondence with Strategy `ON_LEAVE` is unresolved. A shorter summary neither removes established states nor creates universal signing or legal-closure requirements.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Encounter Bed/Location Association**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Encounter Care-Place Association](../../../../enterprise-architecture/continuum/business/business-function/3ff076243fe141759e7d4401389b3527.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Encounter Care-Place Association` — Binds the encounter to physical wards, rooms, and beds over time.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Encounter Location History Query](../../../../enterprise-architecture/continuum/business/business-service/5f0f9fec1d16458aaf914b0957793953.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Encounter Location History Query` — Discloses patient location tracking history.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Encounter Master Record, Encounter State Transition Log, Encounter Care-Place Movement Ledger.

---

## 6. Order Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Order Administration](../../../../enterprise-architecture/continuum/strategy/capability/c3c5abd7987f4433a46cbfed8167914c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Order Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Closed-Loop Progression**: Tracks clinical orders through closed-loop progression. Diagnostic, pathology, imaging and procedural examples illustrate scoped behaviour and do not exclude medication orders from Order Administration responsibility.
  - **Order $\neq$ Referral**: Closed-loop execution of requested investigations without transferring longitudinal care responsibility.
- **Functions & Exposed Services**:
  - **Feature: Order Request Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Order Request](../../../../enterprise-architecture/continuum/business/business-function/e5387fca59324407b8ed5d6db66ef93b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Order Request` — Ingests structured orders containing clinical indication, requesting clinician, and test specifications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Order Requisition Ingress](../../../../enterprise-architecture/continuum/business/business-service/ba824a1ec2c64d9598c409803bcf9d86.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Order Requisition Ingress` — Ingests electronic orders from clinical ordering systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Order Destination Resolution & Routing**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Resolve Order Destination](../../../../enterprise-architecture/continuum/business/business-function/5e21ed48205446a9b05bc8017d1eccfc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Resolve Order Destination` — Evaluates order routing rules to determine target laboratory, imaging centre, or procedural unit.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Order Dispatch Service](../../../../enterprise-architecture/continuum/business/business-service/a8b0f7284a854fdfb43a9af446aa1270.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Order Dispatch Service` — Transmits orders to performing diagnostic systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Order Closed-Loop Progression Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Order Progression](../../../../enterprise-architecture/continuum/business/business-function/52f4b2dfb0c64e5aa4b378a9e63e0867.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Manage Order Progression` — Tracks order state (**scoped diagnostic/procedural illustration**): *Placed* $\to$ *Received* $\to$ *Specimen Collected* $\to$ *In-Progress* $\to$ *Preliminary Result* $\to$ *Final Result* $\to$ *Closed*.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Order Status & Tracking Query](../../../../enterprise-architecture/continuum/business/business-service/a4c049391dd64cd7af83bb5b4c7af0c5.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Order Status & Tracking Query` — Provides real-time order tracking to ordering clinicians.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Order Progression](../../../../enterprise-architecture/continuum/business/business-function/52f4b2dfb0c64e5aa4b378a9e63e0867.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Closed-Loop Order Progression Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Manage Order Progression](../../../../enterprise-architecture/continuum/business/business-function/52f4b2dfb0c64e5aa4b378a9e63e0867.md)
- [Order Status & Tracking Query](../../../../enterprise-architecture/continuum/business/business-service/a4c049391dd64cd7af83bb5b4c7af0c5.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Scope qualification*: Specimen, investigation and result checkpoints describe the illustrated diagnostic/procedural progression. Dispatch, receipt and acceptance remain distinct; Behaviour `Received` is not equated to a Process checkpoint without evidence. Cancellation/modification responsibility remains valid even where absent from that sequence.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Order Cancellation & Modification Coordination**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate Order Modification / Cancellation](../../../../enterprise-architecture/continuum/business/business-function/6e445f8bc64e427fbe76b6dda20ed80f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Coordinate Order Modification / Cancellation` — Handles requests to amend clinical details or cancel unexecuted orders.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Order Cancellation Service](../../../../enterprise-architecture/continuum/business/business-service/1c8467ef0b6047329bdb899a52c23efc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Order Cancellation Service` — Dispatches cancel directives to performing systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Order Outcome Notification](../../../../enterprise-architecture/continuum/business/business-service/1d961373ab484e5f887ecf4c7b166130.md)
- [Associate Order Outcome](../../../../enterprise-architecture/continuum/business/business-function/8bc2b528ca354c8ba95ddb44facdcd05.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

  - **Capability-scoped behaviour: Order Result Association** (Feature identity not established):


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Associate Order Outcome](../../../../enterprise-architecture/continuum/business/business-function/8bc2b528ca354c8ba95ddb44facdcd05.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Associate Order Outcome` — Correlates returning diagnostic reports and observations back to the originating order requisition.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Order Outcome Notification](../../../../enterprise-architecture/continuum/business/business-service/1d961373ab484e5f887ecf4c7b166130.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Order Outcome Notification` — Emits completion notices to ordering practitioners.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Clinical Order Master Record, Closed-Loop Tracking Ledger, Order-Result Correlation Matrix.

---

## 7. Diagnostic Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Diagnostic Administration](../../../../enterprise-architecture/continuum/strategy/capability/3cc720ba2e8b4d7a94bb349391d61895.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Diagnostic Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Result Management & Distribution**: Manages the correlation, notification, and delivery of pathology, radiology, and point-of-care results.
  - **Ownership Guardrail**: Does **not** own the clinical diagnostic interpretation or specimen analysis (which is owned by the performing diagnostic service provider/LIS).
- **Functions & Exposed Services**:
  - **Feature: Diagnostic Request Correlation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Correlate Diagnostic Request](../../../../enterprise-architecture/continuum/business/business-function/6797819b02e0405c8169fa57544d9606.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Correlate Diagnostic Request` — Matches incoming lab/radiology results to active patient encounters and order requisitions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Diagnostic Correlation Resolution](../../../../enterprise-architecture/continuum/business/business-service/15f3582e02ae47e1b7f9117573a8ddf7.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Diagnostic Correlation Resolution` — Discloses correlated order/result linkages.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Diagnostic Report Ingestion & Binding**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Bind Diagnostic Report](../../../../enterprise-architecture/continuum/business/business-function/ee4bd6218f3a4029815bfc16912b88af.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Bind Diagnostic Report` — Associates structured observations, reference ranges, and abnormal flags with patient health records.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Diagnostic Report Ingress](../../../../enterprise-architecture/continuum/business/business-service/b151ad42b9194d9e84b1dd9beecdf6dd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Diagnostic Report Ingress` — Ingests results from external LIS/RIS systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Diagnostic Report Distribution**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Distribute Diagnostic Report](../../../../enterprise-architecture/continuum/business/business-function/ab6d94a9e06742aabe087f11d93f4334.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Distribute Diagnostic Report` — Delivers preliminary, final, and corrected reports to ordering and copied practitioners.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Diagnostic Result Delivery Service](../../../../enterprise-architecture/continuum/business/business-service/86a9d1868c8c45788791a9436794531c.md)
- [Distribute Diagnostic Report](../../../../enterprise-architecture/continuum/business/business-function/ab6d94a9e06742aabe087f11d93f4334.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature scope qualification*: Strategy `FEAT-SA-17 — Diagnostic Report Distribution` retains its finalised-report responsibility. Preliminary, corrected and addendum/correction handling belongs to the broader Diagnostic Administration responsibility; it does not redefine that narrower Feature or confer diagnostic interpretation authority.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Diagnostic Result Delivery Service](../../../../enterprise-architecture/continuum/business/business-service/86a9d1868c8c45788791a9436794531c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Diagnostic Result Delivery Service` — Emits secure result notifications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Diagnostic Request-Report Linkage Registry, Result Distribution Ledger.

---

## 8. Medication Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Medication Administration](../../../../enterprise-architecture/continuum/strategy/capability/c907e2a95629435b9951941ad4e2ebe9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Medication Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Event Tracking**: Aggregates medication orders, pharmacy dispense records, and administration-event information into a coherent timeline; bedside events are one supported context.
  - **Non-Practice Guardrail**: Harmonia tracks and communicates medication events; it does **not** prescribe drugs, dispense pharmaceuticals, or perform clinical administration.
- **Functions & Exposed Services**:
  - **Feature: Medication Order Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Medication Order](../../../../enterprise-architecture/continuum/business/business-function/96e2ae3ccb684635acf78037e75a1322.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Medication Order` — Ingests inpatient and outpatient drug prescriptions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Medication Order Ingress](../../../../enterprise-architecture/continuum/business/business-service/3bcfd2c922aa429ba77618ac94982a56.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Medication Order Ingress` — Ingests prescription events.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Dispense Event Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Track Dispense Event](../../../../enterprise-architecture/continuum/business/business-function/e4c3b7e257b3448aa79d66992e9e9bbc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Track Dispense Event` — Ingests community and hospital pharmacy dispensing records.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Dispense Event Ingress](../../../../enterprise-architecture/continuum/business/business-service/b81512ca8e36434e9fa29493e645d31d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Dispense Event Ingress` — Ingests pharmacy supply notifications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Medication Administration Record (MAR) Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Medication Administration Event](../../../../enterprise-architecture/continuum/business/business-function/7437a97ccc3947b38f70d7da47bd0714.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Medication Administration Event` — Records medication administration-event information, including nurse-administered doses, e-MAR documentation and infusion telemetry as non-exhaustive examples; scope is not limited to nursing, bedside or inpatient settings.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Medication Administration Ingress](../../../../enterprise-architecture/continuum/business/business-service/8d224a68f01648d39096d39ebac893df.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Medication Administration Ingress` — Ingests administration records.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Medication Event Timeline, Prescription-Dispense-Administration Correlation Matrix.

---

## 9. Procedure Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Procedure Administration](../../../../enterprise-architecture/continuum/strategy/capability/649dd7cc26094e539386a050086dfb91.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Procedure Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Scope qualification**: Procedure information extends beyond surgery; Theatre retains its distinct surgical operational responsibility.
- **Functions & Exposed Services**:
  - **Feature: Procedure Booking Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Procedure Booking / Request](../../../../enterprise-architecture/continuum/business/business-function/6d29d7e7422443b4acf3cbe85c4074e2.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Procedure Booking / Request` — Ingests procedural and surgical requisitions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Procedure Booking Ingress](../../../../enterprise-architecture/continuum/business/business-service/64c52e8e6f304dee8d86a5da96f927e3.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Procedure Booking Ingress` — Receives surgical/procedural requests.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Procedural Documentation Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Procedural Documentation](../../../../enterprise-architecture/continuum/business/business-function/62a47d22a07e4fcc8b3f99ccdffff390.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Procedural Documentation` — Ingests operation reports, anaesthetic records, and post-procedure notes.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Procedural Documentation Ingress](../../../../enterprise-architecture/continuum/business/business-service/b58405366b7a43dc909ca3074503e11a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Procedural Documentation Ingress` — Ingests procedural documentation, including surgical notes, into patient record streams.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Procedure Request Register, Procedural Documentation Index.

---

## 10. Clinical Record Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Clinical Record Administration](../../../../enterprise-architecture/continuum/strategy/capability/ce9e7e7b22054cefb707a650d5be7219.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Clinical Record Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Document Governance**: Governs the ingestion, metadata indexing, versioning, addenda, and legal lifecycle of clinical documents (discharge summaries, specialist letters, advance care directives).
- **Functions & Exposed Services**:
  - **Feature: Clinical Document Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Clinical Document](../../../../enterprise-architecture/continuum/business/business-function/d8f80ba3a5a9401b84e6476cbec8cace.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Clinical Document` — Receives clinical documents from information suppliers.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Document Ingress](../../../../enterprise-architecture/continuum/business/business-service/2a69a33c93954a868249270d919dabfd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Clinical Document Ingress` — Exposes document submission endpoints across the HIE.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Document Versioning & Supersession**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Clinical Document Lifecycle](../../../../enterprise-architecture/continuum/business/business-function/e6d00e00eba64ef4bc41d2af8bc3b944.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Govern Clinical Document Lifecycle` — Manages document states (**scoped illustration, not a universal transition model**): *Draft* $\to$ *Preliminary* $\to$ *Final* $\to$ *Amended* $\to$ *Superseded* $\to$ *Entered-in-Error*.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Document Lifecycle Service](../../../../enterprise-architecture/continuum/business/business-service/fbeffe86f67347e3b39ad4544b13cbe7.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Clinical Document Lifecycle Service` — Allows authoring clinicians to publish addenda or corrections.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Clinical Document Lifecycle](../../../../enterprise-architecture/continuum/business/business-function/e6d00e00eba64ef4bc41d2af8bc3b944.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Clinical Document Lifecycle Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Clinical Document Lifecycle](../../../../enterprise-architecture/continuum/business/business-function/e6d00e00eba64ef4bc41d2af8bc3b944.md)
- [Clinical Document Lifecycle Service](../../../../enterprise-architecture/continuum/business/business-service/fbeffe86f67347e3b39ad4544b13cbe7.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Scope qualification*: `Final ≠ automatically Final Signed`. Authorship, attestation, approval, authentication, verification, signature, finalisation, authority and legal qualification remain distinct. Process signing/countersigning requirements retain their local scope, and amendment/supersession/error states are not mandatory for every document. Ingestion and consumer processing do not define originating validity, authority, authorship or legal status.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Document Metadata Indexing**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Index Clinical Document Metadata](../../../../enterprise-architecture/continuum/business/business-function/0ee35bd069924319a7a9fe115d3c52b9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Index Clinical Document Metadata` — Indexes author, specialty, encounter, date, and document type (LOINC/SNOMED).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Document Metadata Registry Query](../../../../enterprise-architecture/continuum/business/business-service/63254ecfe5554eeeb63d0c9f1720388b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Document Metadata Registry Query` — Discloses document registry metadata to clinical viewers.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Clinical Document Registry, Document Version History, Document Lifecycle State Register.

---

## 11. Care Plan Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Care Plan Administration](../../../../enterprise-architecture/continuum/strategy/capability/cf06eec5699f406fa30a00d2f76e3098.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Care Plan Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Feature: Care Plan Ingestion & Distribution**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Care Plan](../../../../enterprise-architecture/continuum/business/business-function/abb5a2ac72424923ae2da671b94b3b48.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Care Plan` — Ingests multidisciplinary care plans, clinical goals, and patient action steps.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Care Plan Ingress](../../../../enterprise-architecture/continuum/business/business-service/180d3f29c2324905ab19337a4dcbce7b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Care Plan Ingress` — Ingests shared care plans.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Care Plan Context Query](../../../../enterprise-architecture/continuum/business/business-service/0bb4cceb0f1b4311930635d8b4c320e0.md)
- [Associate Care Plan Context](../../../../enterprise-architecture/continuum/business/business-function/98cd313fdaa74634a8e1ae56a5bb06dc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

  - **Capability-scoped behaviour: Care Plan Context Association** (Feature identity not established):


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Associate Care Plan Context](../../../../enterprise-architecture/continuum/business/business-function/98cd313fdaa74634a8e1ae56a5bb06dc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Associate Care Plan Context` — Links care plans to active diagnoses, health problems, and care team members.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Care Plan Context Query](../../../../enterprise-architecture/continuum/business/business-service/0bb4cceb0f1b4311930635d8b4c320e0.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Care Plan Context Query` — Discloses active care plans to treating clinicians.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Care Plan Change Notification](../../../../enterprise-architecture/continuum/business/business-service/08a8309372734287b2c22a06b95080a4.md)
- [Distribute Care Plan Change](../../../../enterprise-architecture/continuum/business/business-function/cbfa51308103418a9dffe23a2d8cfd87.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

  - **Capability-scoped behaviour: Care Plan Update Distribution** (Feature identity not established):


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Distribute Care Plan Change](../../../../enterprise-architecture/continuum/business/business-function/cbfa51308103418a9dffe23a2d8cfd87.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Distribute Care Plan Change` — Broadcasts care plan updates to all enrolled care team participants.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Care Plan Change Notification](../../../../enterprise-architecture/continuum/business/business-service/08a8309372734287b2c22a06b95080a4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Care Plan Change Notification` — Emits care plan revision notices.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Shared Care Plan Registry, Clinical Goal Ledger, Care Plan Revision History.

---

## 12. Clinical Communication Administration
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Clinical Communication Administration](../../../../enterprise-architecture/continuum/strategy/capability/b38f2c2811724734b5887b6dbaaabbfc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Clinical Communication Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Technical vs. Business Acknowledgement**:
    - **Technical ACK**: May establish durable receiving-boundary acceptance into an integration queue; it does not establish business acceptance.
    - **Business Delivery ACK**: Records contextual recipient-reported facts or dispositions, which may include receipt, parsing, acceptance, rejection, filing or another explicit disposition. These are not a universal progression; business-acceptance and filing criteria remain unresolved.
- **Functions & Exposed Services**:
  - **Feature: Clinical Communication Distribution**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Distribute Clinical Communication](../../../../enterprise-architecture/continuum/business/business-function/5b8d4d4a3f7147fc9ee5f114155c2db8.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Distribute Clinical Communication` — Routes secure messages, referral letters, and critical alerts to practitioner endpoints.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Secure Clinical Message Dispatch](../../../../enterprise-architecture/continuum/business/business-service/d28fb3b4123b4e569a95a823fd83f837.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Secure Clinical Message Dispatch` — Exposes message dispatch to clinical authors.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Delivery Acknowledgement Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Track Business Delivery Acknowledgement](../../../../enterprise-architecture/continuum/business/business-function/75fb22db4c2346b99fd7c788264abb5d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Track Business Delivery Acknowledgement` — Tracks end-to-end delivery state and records business-level delivery/rejection acknowledgements.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Communication Delivery Status Query](../../../../enterprise-architecture/continuum/business/business-service/1b3447db9891406b951f6a6ccf20763e.md)
- [Track Business Delivery Acknowledgement](../../../../enterprise-architecture/continuum/business/business-function/75fb22db4c2346b99fd7c788264abb5d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Receipt qualification*: Delivery/read evidence does not establish clinical review, agreement, approval or incorporation. Clinical Review itself does not automatically establish incorporation; no universal Clinical Incorporation concept is established.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Communication Delivery Status Query](../../../../enterprise-architecture/continuum/business/business-service/1b3447db9891406b951f6a6ccf20763e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Communication Delivery Status Query` — Discloses delivery confirmation to message senders.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Communication Audit Logging**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Record Communication Transaction](../../../../enterprise-architecture/continuum/business/business-function/d64ced698052400fa7892e4d768b3f8f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Record Communication Transaction` — Maintains an immutable audit trail of clinical message dispatch and receipt.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Communication Audit Query](../../../../enterprise-architecture/continuum/business/business-service/7c871898fd634a16902048da75586fd9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Communication Audit Query` — Discloses transmission history for medico-legal verification.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Clinical Message Dispatch Log, Business Delivery Acknowledgement Ledger, Medico-Legal Transmission Audit Register.
- **Authority boundary**: Acknowledgement, receipt, persistence, filing, indexing, assembly and review do not transfer originating authority or establish finality. Communication audit evidence concerns transmission; it does not establish the clinical content’s authority or legal qualification.
