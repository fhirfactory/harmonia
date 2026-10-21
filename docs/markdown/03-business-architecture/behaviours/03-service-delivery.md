<!-- continuum-source-succession -->
> **RAD-1.1.5 source succession:** Partial section succession; unwrapped scope/governance and unresolved material retain their recorded roles. See the [canonical continuum](../../../../enterprise-architecture/continuum/documentation/published/1.1.5/README.md) and [section correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json). Original source wording and standing below are preserved.

# Service Delivery — Capability-Scoped Behaviour

## 1. Contextual Scope & Architectural Intent

The named owning Capabilities and established Features retain their responsibilities. Affected Capability Tier, complete ancestry, root status and structural Canonical IDs remain unresolved under [approved G1 K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing); document grouping and numbering do not establish architectural identity or hierarchy.

Service Delivery represents the clinical operating contexts in which direct patient care is delivered across primary, acute, emergency, inpatient, diagnostic, and virtual settings.

### The Fundamental Care Enablement Principle
> **Harmonia enables, informs, and coordinates healthcare delivery across care boundaries; it does not practice medicine, prescribe pharmaceuticals, make autonomous clinical decisions, or replace clinician judgment.**

Service Delivery capabilities within Harmonia define **integration enablement and clinical context correlation behaviour**. They reuse and consume core capabilities (*Patient Clinical Record, Health Information Exchange, Health Information Control, Episode & Encounter Administration, Clinical Record Administration*) rather than duplicating their responsibilities.

<a id="residual-feature-sufficiency"></a>

### Residual Feature Sufficiency

The 2026-10-09 human adjudications apply the [Business composition and clinical-work boundaries](../metamodel/business-architecture-metamodel.md#36-business-responsibility-and-reusable-composition). General clinical-information capture, processing, preservation and distribution are expressed by [Patient Clinical Record and Health Information Exchange](05-intrinsic-enablement.md#21-patient-clinical-record), with applicable [Service Administration](02-service-administration.md) responsibility. A clinical setting does not establish specialised capture or clinical-work ownership.

| Strategy Feature | Adjudicated Business expression and boundary |
| :--- | :--- |
| `FEAT-SD-03 — Acute Clinical State Event Capture` | Acute deterioration/status information is managed through general clinical-information responsibilities. No Acute-specific capture Function is required. The Strategy Feature's acute-specific framing is over-specialised if interpreted as a separate Harmonia responsibility; its existing name/definition are retained for bounded Strategy review, not silently redefined. Alert routing below remains distinct from capture. |
| `FEAT-SD-08 — Inpatient Clinical Progression Tracking` | Relevant milestone information uses the same general responsibilities. Operational consequences may inform patient flow, capacity or discharge coordination; those consumers do not establish the originating clinical fact. An allied-health clearance may affect discharge readiness without becoming an operationally authored clearance. No Inpatient-specific clinical-tracking Function or EMR workflow is required. Dossier assembly below remains distinct. |
| `FEAT-SD-14 — Screening Recall Notification Distribution` | Recall is a domain-specific composition of clinical-information management, Calendar Management, Workflow & Activity Coordination and information communication/distribution, with applicable entity coordination. Configured workflow/Praxis may realise it without a screening-specific recall Function. Eligibility projection alone remains distinct from distribution. Exact workflow and provider contracts are downstream, not established by this sufficiency decision. |

These are composition-level sufficiency decisions, not replacement Feature associations to the Functions below. The same reusable responsibilities can support follow-up, vaccination, diagnostic or review reminders without a new Business Function for each clinical purpose. Population/cohort identification may precede individual activity. Once activity concerns a specific managed patient, the [entity-specific Digital Twin coordination rule](../../02-strategy/strategic-views/logical-component-responsibilities.md#entity-specific-operational-coordination) applies; it establishes no Domain03 Twin Actor, Process or execution mechanism.

---

## 2. Clinical Care Enablement Contexts

```text
Service Delivery Contexts
├── Primary Care
├── Acute Care
├── Emergency Care
├── Inpatient Care
├── Diagnostic Services
├── Medication Therapy
├── Preventive Care
├── Community Care
├── Outreach Care
└── Remote & Virtual Care
```

---

<a id="21-primary-care-enablement"></a>

### 2.1 Primary Care
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Primary Care Coordination](../../../../enterprise-architecture/continuum/strategy/capability/f80e73ee91624ce5baa11ae15480fd35.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Primary Care`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Clinical Operating Context**: General practice clinics, community medical centres, and family medicine practices providing longitudinal whole-person care.
- **Enabling Functions & Exposed Services**:
  - **Feature: General Practice Shared Record Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Correlate Primary Care Consultation Context](../../../../enterprise-architecture/continuum/business/business-function/e6c9088f502742afb538ee654ac0f962.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Correlate Primary Care Consultation Context` — Binds GP consultation notes, vital signs, and encounter episodes to the regional Longitudinal Health Record (LHR).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Primary Care Summary Ingress](../../../../enterprise-architecture/continuum/business/business-service/7f4ca66b911e48bc8bf859e239f5ba00.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Primary Care Summary Ingress` — Ingests GP event summaries into the HIE.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Chronic Care Timeline Query](../../../../enterprise-architecture/continuum/business/business-service/88243738a3c941efb369f5dd6da6252b.md)
- [Coordinate Multi-Agency Chronic Care Plan](../../../../enterprise-architecture/continuum/business/business-function/fc49b716c2a845408198591e9a4dc515.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: Strategy `FEAT-SD-02 — Primary Care Notification Dispatch` remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordinate Multi-Agency Chronic Care Plan](../../../../enterprise-architecture/continuum/business/business-function/fc49b716c2a845408198591e9a4dc515.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Coordinate Multi-Agency Chronic Care Plan` — Enables GPs to track multi-provider disease management milestones (e.g., Diabetes or Cardiovascular Care Plans).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Chronic Care Timeline Query](../../../../enterprise-architecture/continuum/business/business-service/88243738a3c941efb369f5dd6da6252b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Chronic Care Timeline Query` — Exposes aggregated multidisciplinary care timelines.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Typed architectural dependencies**:
  - **Service consumption**: `Person Identifier Resolution`; `Longitudinal Clinical Record Query` from Patient Clinical Record, already established in the [consumption matrix](../dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix).
  - **Capability dependency**: `Care Plan Administration`.
  - **Unresolved reference**: “Referral Submission” — an exact Service identity/consuming relationship is not established here; the abbreviated wording is not an alias.

---

<a id="22-acute-care-enablement"></a>

### 2.2 Acute Care
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Acute Care Monitoring & Event Coordination](../../../../enterprise-architecture/continuum/strategy/capability/ee17b26b8c874cc3af50d07a787ce488.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Acute Care`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Clinical Operating Context**: Hospital specialty wards, surgical suites, and intensive care units managing severe, urgent, or post-operative conditions.
- **Enabling Functions & Exposed Services**:
  - **Feature: Acute Care Clinical Context Provision**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Assemble Acute Clinical Baseline](../../../../enterprise-architecture/continuum/business/business-function/be64da1302bf4c4bae357295612e8e6e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Assemble Acute Clinical Baseline` — Rapidly aggregates previous discharge summaries, active medications, allergies, and diagnostic alerts upon acute hospital presentation.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Acute Admission Dossier Query](../../../../enterprise-architecture/continuum/business/business-service/7276d5e4d484440f9466ec24c183bdab.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Acute Admission Dossier Query` — Delivers a high-priority clinical briefing to admitting teams.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Acute Clinical Alert Dispatch](../../../../enterprise-architecture/continuum/business/business-service/34d7f0bfff8f470bb97c00582f5e8a1d.md)
- [Route Acute Critical Result Alert](../../../../enterprise-architecture/continuum/business/business-function/66d0295d5a6c4eedba6406d6f672906c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: `FEAT-SD-03 — Acute Clinical State Event Capture` is adjudicated sufficient through general clinical-information responsibilities above. Routing a critical-result alert remains distinct from capture; no specialised capture Function or replacement association is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Route Acute Critical Result Alert](../../../../enterprise-architecture/continuum/business/business-function/66d0295d5a6c4eedba6406d6f672906c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Route Acute Critical Result Alert` — Immediately directs urgent lab/radiology alerts (e.g., critical troponin, intracranial bleed) to active acute attending clinicians.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Acute Clinical Alert Dispatch](../../../../enterprise-architecture/continuum/business/business-service/34d7f0bfff8f470bb97c00582f5e8a1d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Acute Clinical Alert Dispatch` — Dispatches urgent interruptive alerts to on-duty ward teams.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Typed architectural dependencies**:
  - **Service consumption**: `Encounter Context Resolution`.
  - **Capability dependency**: `Health Information Control`; break-glass access describes the required governed context, not an independently allocated Service.
  - **Unresolved reference**: “Diagnostic Result Delivery” — intended exact Service/relationship remains unestablished; no alias or replacement edge is inferred.

---

<a id="23-emergency-care-enablement"></a>

### 2.3 Emergency Care
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Emergency Care Progression](../../../../enterprise-architecture/continuum/strategy/capability/7125e459a48946d0ba075b5ecc3571b4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Emergency Care`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Clinical Operating Context**: Emergency Departments (ED), trauma centres, and urgent care clinics operating under time-critical, high-acuity constraints.
- **Enabling Functions & Exposed Services**:
  - **Feature: Emergency Department Arrival & Triage Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Bind ED Triage Assessment](../../../../enterprise-architecture/continuum/business/business-function/fa51fad9b9344546953ff8ec5b5f39ca.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Bind ED Triage Assessment` — Captures ATS (Australasian Triage Scale) category, presenting complaint, and ambulance paramedic handover data.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [ED Triage Stream Ingress](../../../../enterprise-architecture/continuum/business/business-service/677947e357b541f58f36e9b4e36157e8.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `ED Triage Stream Ingress` — Ingests emergency triage presentations.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Emergency Care Cross-Facility Correlation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Provide Emergency LHR Summary](../../../../enterprise-architecture/continuum/business/business-function/a7235e8b249a4914b951e0d795132316.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Provide Emergency LHR Summary` — Delivers rapid, override-capable access to Advance Care Directives, allergy records, and recent hospital admissions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Emergency Fast-Path LHR Query](../../../../enterprise-architecture/continuum/business/business-service/0588ef78691143cdb2d884960811db1e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Emergency Fast-Path LHR Query` — Provides low-latency emergency record summaries.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Typed architectural dependencies**:
  - **Service consumption**: `Person Identifier Resolution`. “Fast-Match” does not establish a separate Service or probabilistic/master-person matching behaviour.
  - **Capability dependency**: `Client Privacy` for its established consent/privacy responsibility; “Break-Glass Override” does not allocate override authority or another element.
  - **Unresolved relationship to an established Feature**: `FEAT-SA-09 — Encounter State Progression`. A precise Service-consumption relationship is not established by naming that Feature.

---

<a id="24-inpatient-care-enablement"></a>

### 2.4 Inpatient Care
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Multidisciplinary Inpatient Care Coordination](../../../../enterprise-architecture/continuum/strategy/capability/c798e38e12e5428dbb8b3cabc977d430.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Inpatient Care`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Clinical Operating Context**: Inpatient hospital wards, rehabilitation units, and sub-acute facilities managing multi-day hospital stays.
- **Enabling Functions & Exposed Services**:
  - **Feature: Inpatient Rounding Context Provision**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Consolidate Inpatient Clinical Round Feed](../../../../enterprise-architecture/continuum/business/business-function/76eb51b9cc6a4417bbc8491830e43ad1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Consolidate Inpatient Clinical Round Feed` — Aggregates daily vital signs, overnight pathology, allied health notes, and medication administration records.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ward Round Clinical Briefing Query](../../../../enterprise-architecture/continuum/business/business-service/2b0f17a8a2da4c0f92a5ad4a17ee526b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Ward Round Clinical Briefing Query` — Discloses consolidated 24-hour clinical briefing views.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Discharge Dossier Packaging Service](../../../../enterprise-architecture/continuum/business/business-service/02a17f4f9b0a425299141261130a1768.md)
- [Assemble Inpatient Discharge Dossier](../../../../enterprise-architecture/continuum/business/business-function/eeefe77a935a46aface61aca4e2bcfb6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: `FEAT-SD-08 — Inpatient Clinical Progression Tracking` is adjudicated sufficient through general clinical-information responsibilities above. Discharge-dossier assembly/publication remains distinct; no separate clinical-tracking Function or replacement association is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Assemble Inpatient Discharge Dossier](../../../../enterprise-architecture/continuum/business/business-function/eeefe77a935a46aface61aca4e2bcfb6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Assemble Inpatient Discharge Dossier` — Synthesises inpatient diagnoses, procedural summaries, reconciled discharge medications, and follow-up instructions into a canonical discharge summary package.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Discharge Dossier Packaging Service](../../../../enterprise-architecture/continuum/business/business-service/02a17f4f9b0a425299141261130a1768.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Discharge Dossier Packaging Service` — Emits canonical discharge summaries to GPs and community providers.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Typed architectural dependencies**:
  - **Capability dependency**: `Discharge Management`.
  - **Unresolved references / relationships**: “Encounter Care-Place Movement,” “Clinical Document Lifecycle,” “Medication Administration Event Ingestion.” Their intended element types and exact consumption/participation relationships are not established by these descriptions; no Function, Process or Service equivalence is inferred.

---

<a id="25-diagnostic-services-enablement"></a>

### 2.5 Diagnostic Services
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Diagnostic Service Integration](../../../../enterprise-architecture/continuum/strategy/capability/9ded2f3a28d24017bb921a0cdcf65a2b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Diagnostic Services`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Clinical Operating Context**: Clinical pathology laboratories, diagnostic imaging networks (radiology/MRI/CT), nuclear medicine, and clinical neurophysiology departments.
- **Enabling Functions & Exposed Services**:
  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Diagnostic Requisition Feed](../../../../enterprise-architecture/continuum/business/business-function/af3d47ada486425abc5ab5d054db7004.md)
- [Modality Worklist Distribution](../../../../enterprise-architecture/continuum/business/business-service/e254b6a140c94b33a206e74db1aecfbb.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: Strategy `FEAT-SD-09 — Laboratory & Imaging Result Distribution` remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Receive Diagnostic Requisition Feed](../../../../enterprise-architecture/continuum/business/business-function/af3d47ada486425abc5ab5d054db7004.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Receive Diagnostic Requisition Feed` — Normalises order requisitions from across the health network into modality worklists.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Modality Worklist Distribution](../../../../enterprise-architecture/continuum/business/business-service/e254b6a140c94b33a206e74db1aecfbb.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Modality Worklist Distribution` — Supplies order worklists to diagnostic laboratory/imaging systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Diagnostic Publication Service](../../../../enterprise-architecture/continuum/business/business-service/600ca5384b4643b0b35579a02c307dcf.md)
- [Publish Governed Diagnostic Report](../../../../enterprise-architecture/continuum/business/business-function/f9fcdc3162044865ac6f26015fdd5b17.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: Strategy `FEAT-SD-10 — Diagnostic History Consolidation` remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Publish Governed Diagnostic Report](../../../../enterprise-architecture/continuum/business/business-function/f9fcdc3162044865ac6f26015fdd5b17.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Publish Governed Diagnostic Report` — Normalises and distributes structured diagnostic observations, reference ranges, and diagnostic image study links into the LHR.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Diagnostic Publication Service](../../../../enterprise-architecture/continuum/business/business-service/600ca5384b4643b0b35579a02c307dcf.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Diagnostic Publication Service` — Publishes verified diagnostic reports across the enterprise.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Capability dependencies**: `Order Administration` for closed-loop order context; `Information Design Governance` for governed terminology bindings. These contextual purposes establish neither an additional Feature nor a precise Service-consumption edge.

---

<a id="26-medication-therapy-enablement"></a>

### 2.6 Medication Therapy
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Medication Therapy Provision and Monitoring](../../../../enterprise-architecture/continuum/strategy/capability/d1821b4f6d5746f9a697dfe3b792a186.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Medication Therapy`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Clinical Operating Context**: Hospital pharmacies, clinical pharmacotherapy review panels, community dispensing networks, and outpatient infusion suites.
- **Enabling Functions & Exposed Services**:
  - **Feature: Medication History Reconciliation Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Project Longitudinal Medication History](../../../../enterprise-architecture/continuum/business/business-function/494f529ada064f77836d40cfe9c7ab48.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Project Longitudinal Medication History` — Aggregates community dispensing, hospital e-MAR administrations, and specialist prescriptions into a unified chronological medication profile.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Longitudinal Medication History Query](../../../../enterprise-architecture/continuum/business/business-service/6a42771bbe5449ab87e8ece94519d03d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Longitudinal Medication History Query` — Discloses full medication timelines for medication reconciliation.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Allergy & Adverse Reaction Query](../../../../enterprise-architecture/continuum/business/business-service/7820fc4fb98c452aba29f35d8c7e74fb.md)
- [Correlate Adverse Reaction & Allergy Profile](../../../../enterprise-architecture/continuum/business/business-function/834d18fe21f14af7ad0c9773d2aaad1e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: Strategy `FEAT-SD-12 — Medication Administration Tracking` remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Correlate Adverse Reaction & Allergy Profile](../../../../enterprise-architecture/continuum/business/business-function/834d18fe21f14af7ad0c9773d2aaad1e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Correlate Adverse Reaction & Allergy Profile` — Binds confirmed substance allergies, adverse drug reactions, and severity grades to the patient identity.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Allergy & Adverse Reaction Query](../../../../enterprise-architecture/continuum/business/business-service/7820fc4fb98c452aba29f35d8c7e74fb.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Allergy & Adverse Reaction Query` — Exposes active allergy profiles to clinical decision support systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Capability dependencies**: `Medication Administration`, `Patient Clinical Record`, `Health Information Control`; no specific Service is allocated by this contextual list.

---

<a id="27-preventive-care-enablement"></a>

### 2.7 Preventive Care
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Screening and Immunisation Enablement](../../../../enterprise-architecture/continuum/strategy/capability/3e9016eaebae4ed0b67a5cacba2652f5.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Preventive Care`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Clinical Operating Context**: Public health screening programmes (breast/bowel/cervical), national immunisation registries, and health promotion initiatives.
- **Enabling Functions & Exposed Services**:
  - **Feature: Immunisation Event Submission**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Syndicate Immunisation Record](../../../../enterprise-architecture/continuum/business/business-function/5a2c53df2e92444ab9b5018b224d98f9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Syndicate Immunisation Record` — Records vaccine encounters and syndicates records to national immunisation registers (e.g., AIR).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Immunisation History Query](../../../../enterprise-architecture/continuum/business/business-service/f2a0107d73664aa0be9d6695fc455d74.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Immunisation History Query` — Discloses complete vaccination history.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Project Screening Eligibility Timeline](../../../../enterprise-architecture/continuum/business/business-function/afb08bdb99cd41d3a86ef7bf7ad5e92d.md)
- [Preventive Screening Status Query](../../../../enterprise-architecture/continuum/business/business-service/bc679a6f23de44b79a452fb5ef10c5ee.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: `FEAT-SD-14 — Screening Recall Notification Distribution` is adjudicated sufficient through reusable composition/workflow above. Projecting/querying eligibility dates remains distinct from distribution; no dedicated recall Function or replacement association is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Project Screening Eligibility Timeline](../../../../enterprise-architecture/continuum/business/business-function/afb08bdb99cd41d3a86ef7bf7ad5e92d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Project Screening Eligibility Timeline` — Tracks age-based and risk-based screening schedules across primary care.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Preventive Screening Status Query](../../../../enterprise-architecture/continuum/business/business-service/bc679a6f23de44b79a452fb5ef10c5ee.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Preventive Screening Status Query` — Discloses screening due dates to primary care providers.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Typed architectural dependencies**:
  - **Capability dependency**: `Health Information Exchange` for syndication.
  - **Unresolved reference**: “Healthcare Subject Demographic Context” — exact element/relationship remains unestablished; descriptive context does not create a Capability alias or Service.

---

<a id="28-community-care-enablement"></a>

### 2.8 Community Care
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Community Care Coordination](../../../../enterprise-architecture/continuum/strategy/capability/8c8c6266d9ed43e09dbe44fcdd669132.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Community Care`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Clinical Operating Context**: District nursing, home-based palliative care, community mental health teams, and allied health community outreach.
- **Enabling Functions & Exposed Services**:
  - **Feature: Community Encounter Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Synchronize Community Visit Record](../../../../enterprise-architecture/continuum/business/business-function/cbd4817eb757449a8f0a0e8ce02c5f07.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Synchronize Community Visit Record` — Ingests clinical notes, wound assessments, and vital signs recorded in community and home settings.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Community Care Encounter Ingress](../../../../enterprise-architecture/continuum/business/business-service/5806fe40300044b280a25fb86667e789.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Community Care Encounter Ingress` — Ingests community care documentation.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Capability-scoped Functions / Services**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Bind Community Support Network Context](../../../../enterprise-architecture/continuum/business/business-function/321fdf544ef044b8be100b656e16d7dc.md)
- [Community Care Team Query](../../../../enterprise-architecture/continuum/business/business-service/5292120098cc4b7e8280b22f46b60b95.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Feature association not established*: Strategy `FEAT-SD-16 — Community Care Plan Synchronization` remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Bind Community Support Network Context](../../../../enterprise-architecture/continuum/business/business-function/321fdf544ef044b8be100b656e16d7dc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Bind Community Support Network Context` — Links community nursing teams with local non-clinical social care and home care packages.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Community Care Team Query](../../../../enterprise-architecture/continuum/business/business-service/5292120098cc4b7e8280b22f46b60b95.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Community Care Team Query` — Exposes community provider contact rosters.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Typed architectural dependencies**:
  - **Capability dependencies**: `Client Relationship`, `Episode & Encounter Administration`.
  - **Collaboration reference with unresolved involvement**: `Care-Team Collaboration` is an established Collaboration; precise participation/involvement is not allocated by the former “Consumes” wording.

---

<a id="29-outreach-care-enablement"></a>

### 2.9 Outreach Care
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Outreach Care Continuity](../../../../enterprise-architecture/continuum/strategy/capability/8526d971b07f40de84ef05b0d192089c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Outreach Care`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Clinical Operating Context**: Mobile specialist clinics, rural and remote fly-in fly-out (FIFO) clinical teams, and school health screening programmes.
- **Enabling Functions & Exposed Services**:
  - **Feature: Outreach Visit Context Provision**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Provide Outreach Visit Context](../../../../enterprise-architecture/continuum/business/business-function/4984d2f87f5a46869400c60cd5c5e0ab.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Provide Outreach Visit Context` — Packages and delivers portable clinical summaries and offline-capable care summaries for remote outreach clinics.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Derivation*: `FEAT-SD-17` in [Strategy's Outreach Care Continuity](../../02-strategy/capabilities/business-enabling-capabilities.md#15-outreach-care-harmonia-relevant) establishes this preparation/provision responsibility. Outcome reconciliation below does not supply pre-visit context. This Function remains owned by Outreach Care, uses governed source information without acquiring its originating authority and establishes no copying, storage or offline implementation mechanism.
    - *Exposure boundary*: Remote outreach clinics are the established recipients. No separate named Service, Process, Role allocation or structural ID is introduced merely to mirror the Function.
  - **Feature: Outreach Encounter Outcome Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Reconcile Outreach Clinical Batch](../../../../enterprise-architecture/continuum/business/business-function/53a7392c0ceb413ba8a00505b29f0f28.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Reconcile Outreach Clinical Batch` — Reconciles and merges clinical documentation recorded during disconnected remote clinic operations.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Outreach Batch Reconciliation Service](../../../../enterprise-architecture/continuum/business/business-service/9488822da1764a1898303a9c656a6062.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Outreach Batch Reconciliation Service` — Reconciles batch clinical encounters upon network reconnection.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Typed architectural dependencies**:
  - **Capability dependencies**: `Health Information Exchange`, `Clinical Record Administration`.
  - **Established Service with unresolved consumption applicability**: `Person Identity Correction` retains Person Identity ownership; how Outreach Care requires that Service is not established here. It cannot authorise originating person-merge decisions.

---

<a id="210-remote--virtual-care-enablement"></a>

### 2.10 Remote & Virtual Care
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Remote Patient Monitoring](../../../../enterprise-architecture/continuum/strategy/capability/2d1addc7dd1d4821b71a11c692d35b27.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Remote & Virtual Care`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Clinical Operating Context**: Telehealth consultations, virtual hospital-in-the-home (vHIT), remote patient monitoring (RPM), and asynchronous specialist e-consults.
- **Enabling Functions & Exposed Services**:
  - **Feature: Virtual Care Session Context Binding**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Correlate Virtual Care Consultation Context](../../../../enterprise-architecture/continuum/business/business-function/d315a7eab4d9453f83ecd44ed8b57b56.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Correlate Virtual Care Consultation Context` — Binds video consultation metadata, remote practitioner identity, and shared digital whiteboard artifacts to the encounter.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Virtual Encounter Context Resolution](../../../../enterprise-architecture/continuum/business/business-service/db1dfa6db11342128e82df186877c477.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Virtual Encounter Context Resolution` — Binds virtual session identifiers to patient encounters.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Remote Telemetry Ingestion**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ingest Remote Patient Telemetry](../../../../enterprise-architecture/continuum/business/business-function/cb03c3040e1c4a4a9fdadb2502113014.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Ingest Remote Patient Telemetry` — Ingests continuous or periodic physiological measurements (e.g., blood pressure, pulse oximetry, continuous glucose) from home monitoring devices.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Remote Telemetry Ingress](../../../../enterprise-architecture/continuum/business/business-service/89014429b6454ced82c26cbe777fa33a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Remote Telemetry Ingress` — Ingests remote patient data streams into the LHR.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Capability dependencies**: `Clinical Device Administration`, `Patient Clinical Record`, `Clinical Communication Administration`; no specific Service is allocated by this contextual list.
