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
- **Owning Capability**: `Primary Care`
- **Clinical Operating Context**: General practice clinics, community medical centres, and family medicine practices providing longitudinal whole-person care.
- **Enabling Functions & Exposed Services**:
  - **Feature: General Practice Shared Record Ingestion**:
    - *Function*: `Correlate Primary Care Consultation Context` — Binds GP consultation notes, vital signs, and encounter episodes to the regional Longitudinal Health Record (LHR).
    - *Exposed Service*: `Primary Care Summary Ingress` — Ingests GP event summaries into the HIE.
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: Strategy `FEAT-SD-02 — Primary Care Notification Dispatch` remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.
    - *Function*: `Coordinate Multi-Agency Chronic Care Plan` — Enables GPs to track multi-provider disease management milestones (e.g., Diabetes or Cardiovascular Care Plans).
    - *Exposed Service*: `Chronic Care Timeline Query` — Exposes aggregated multidisciplinary care timelines.
- **Typed architectural dependencies**:
  - **Service consumption**: `Person Identifier Resolution`; `Longitudinal Clinical Record Query` from Patient Clinical Record, already established in the [consumption matrix](../dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix).
  - **Capability dependency**: `Care Plan Administration`.
  - **Unresolved reference**: “Referral Submission” — an exact Service identity/consuming relationship is not established here; the abbreviated wording is not an alias.

---

<a id="22-acute-care-enablement"></a>

### 2.2 Acute Care
- **Owning Capability**: `Acute Care`
- **Clinical Operating Context**: Hospital specialty wards, surgical suites, and intensive care units managing severe, urgent, or post-operative conditions.
- **Enabling Functions & Exposed Services**:
  - **Feature: Acute Care Clinical Context Provision**:
    - *Function*: `Assemble Acute Clinical Baseline` — Rapidly aggregates previous discharge summaries, active medications, allergies, and diagnostic alerts upon acute hospital presentation.
    - *Exposed Service*: `Acute Admission Dossier Query` — Delivers a high-priority clinical briefing to admitting teams.
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: `FEAT-SD-03 — Acute Clinical State Event Capture` is adjudicated sufficient through general clinical-information responsibilities above. Routing a critical-result alert remains distinct from capture; no specialised capture Function or replacement association is assigned.
    - *Function*: `Route Acute Critical Result Alert` — Immediately directs urgent lab/radiology alerts (e.g., critical troponin, intracranial bleed) to active acute attending clinicians.
    - *Exposed Service*: `Acute Clinical Alert Dispatch` — Dispatches urgent interruptive alerts to on-duty ward teams.
- **Typed architectural dependencies**:
  - **Service consumption**: `Encounter Context Resolution`.
  - **Capability dependency**: `Health Information Control`; break-glass access describes the required governed context, not an independently allocated Service.
  - **Unresolved reference**: “Diagnostic Result Delivery” — intended exact Service/relationship remains unestablished; no alias or replacement edge is inferred.

---

<a id="23-emergency-care-enablement"></a>

### 2.3 Emergency Care
- **Owning Capability**: `Emergency Care`
- **Clinical Operating Context**: Emergency Departments (ED), trauma centres, and urgent care clinics operating under time-critical, high-acuity constraints.
- **Enabling Functions & Exposed Services**:
  - **Feature: Emergency Department Arrival & Triage Ingestion**:
    - *Function*: `Bind ED Triage Assessment` — Captures ATS (Australasian Triage Scale) category, presenting complaint, and ambulance paramedic handover data.
    - *Exposed Service*: `ED Triage Stream Ingress` — Ingests emergency triage presentations.
  - **Feature: Emergency Care Cross-Facility Correlation**:
    - *Function*: `Provide Emergency LHR Summary` — Delivers rapid, override-capable access to Advance Care Directives, allergy records, and recent hospital admissions.
    - *Exposed Service*: `Emergency Fast-Path LHR Query` — Provides low-latency emergency record summaries.
- **Typed architectural dependencies**:
  - **Service consumption**: `Person Identifier Resolution`. “Fast-Match” does not establish a separate Service or probabilistic/master-person matching behaviour.
  - **Capability dependency**: `Client Privacy` for its established consent/privacy responsibility; “Break-Glass Override” does not allocate override authority or another element.
  - **Unresolved relationship to an established Feature**: `FEAT-SA-09 — Encounter State Progression`. A precise Service-consumption relationship is not established by naming that Feature.

---

<a id="24-inpatient-care-enablement"></a>

### 2.4 Inpatient Care
- **Owning Capability**: `Inpatient Care`
- **Clinical Operating Context**: Inpatient hospital wards, rehabilitation units, and sub-acute facilities managing multi-day hospital stays.
- **Enabling Functions & Exposed Services**:
  - **Feature: Inpatient Rounding Context Provision**:
    - *Function*: `Consolidate Inpatient Clinical Round Feed` — Aggregates daily vital signs, overnight pathology, allied health notes, and medication administration records.
    - *Exposed Service*: `Ward Round Clinical Briefing Query` — Discloses consolidated 24-hour clinical briefing views.
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: `FEAT-SD-08 — Inpatient Clinical Progression Tracking` is adjudicated sufficient through general clinical-information responsibilities above. Discharge-dossier assembly/publication remains distinct; no separate clinical-tracking Function or replacement association is assigned.
    - *Function*: `Assemble Inpatient Discharge Dossier` — Synthesises inpatient diagnoses, procedural summaries, reconciled discharge medications, and follow-up instructions into a canonical discharge summary package.
    - *Exposed Service*: `Discharge Dossier Packaging Service` — Emits canonical discharge summaries to GPs and community providers.
- **Typed architectural dependencies**:
  - **Capability dependency**: `Discharge Management`.
  - **Unresolved references / relationships**: “Encounter Care-Place Movement,” “Clinical Document Lifecycle,” “Medication Administration Event Ingestion.” Their intended element types and exact consumption/participation relationships are not established by these descriptions; no Function, Process or Service equivalence is inferred.

---

<a id="25-diagnostic-services-enablement"></a>

### 2.5 Diagnostic Services
- **Owning Capability**: `Diagnostic Services`
- **Clinical Operating Context**: Clinical pathology laboratories, diagnostic imaging networks (radiology/MRI/CT), nuclear medicine, and clinical neurophysiology departments.
- **Enabling Functions & Exposed Services**:
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: Strategy `FEAT-SD-09 — Laboratory & Imaging Result Distribution` remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.
    - *Function*: `Receive Diagnostic Requisition Feed` — Normalises order requisitions from across the health network into modality worklists.
    - *Exposed Service*: `Modality Worklist Distribution` — Supplies order worklists to diagnostic laboratory/imaging systems.
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: Strategy `FEAT-SD-10 — Diagnostic History Consolidation` remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.
    - *Function*: `Publish Governed Diagnostic Report` — Normalises and distributes structured diagnostic observations, reference ranges, and diagnostic image study links into the LHR.
    - *Exposed Service*: `Diagnostic Publication Service` — Publishes verified diagnostic reports across the enterprise.
- **Capability dependencies**: `Order Administration` for closed-loop order context; `Information Design Governance` for governed terminology bindings. These contextual purposes establish neither an additional Feature nor a precise Service-consumption edge.

---

<a id="26-medication-therapy-enablement"></a>

### 2.6 Medication Therapy
- **Owning Capability**: `Medication Therapy`
- **Clinical Operating Context**: Hospital pharmacies, clinical pharmacotherapy review panels, community dispensing networks, and outpatient infusion suites.
- **Enabling Functions & Exposed Services**:
  - **Feature: Medication History Reconciliation Ingestion**:
    - *Function*: `Project Longitudinal Medication History` — Aggregates community dispensing, hospital e-MAR administrations, and specialist prescriptions into a unified chronological medication profile.
    - *Exposed Service*: `Longitudinal Medication History Query` — Discloses full medication timelines for medication reconciliation.
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: Strategy `FEAT-SD-12 — Medication Administration Tracking` remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.
    - *Function*: `Correlate Adverse Reaction & Allergy Profile` — Binds confirmed substance allergies, adverse drug reactions, and severity grades to the patient identity.
    - *Exposed Service*: `Allergy & Adverse Reaction Query` — Exposes active allergy profiles to clinical decision support systems.
- **Capability dependencies**: `Medication Administration`, `Patient Clinical Record`, `Health Information Control`; no specific Service is allocated by this contextual list.

---

<a id="27-preventive-care-enablement"></a>

### 2.7 Preventive Care
- **Owning Capability**: `Preventive Care`
- **Clinical Operating Context**: Public health screening programmes (breast/bowel/cervical), national immunisation registries, and health promotion initiatives.
- **Enabling Functions & Exposed Services**:
  - **Feature: Immunisation Event Submission**:
    - *Function*: `Syndicate Immunisation Record` — Records vaccine encounters and syndicates records to national immunisation registers (e.g., AIR).
    - *Exposed Service*: `Immunisation History Query` — Discloses complete vaccination history.
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: `FEAT-SD-14 — Screening Recall Notification Distribution` is adjudicated sufficient through reusable composition/workflow above. Projecting/querying eligibility dates remains distinct from distribution; no dedicated recall Function or replacement association is assigned.
    - *Function*: `Project Screening Eligibility Timeline` — Tracks age-based and risk-based screening schedules across primary care.
    - *Exposed Service*: `Preventive Screening Status Query` — Discloses screening due dates to primary care providers.
- **Typed architectural dependencies**:
  - **Capability dependency**: `Health Information Exchange` for syndication.
  - **Unresolved reference**: “Healthcare Subject Demographic Context” — exact element/relationship remains unestablished; descriptive context does not create a Capability alias or Service.

---

<a id="28-community-care-enablement"></a>

### 2.8 Community Care
- **Owning Capability**: `Community Care`
- **Clinical Operating Context**: District nursing, home-based palliative care, community mental health teams, and allied health community outreach.
- **Enabling Functions & Exposed Services**:
  - **Feature: Community Encounter Ingestion**:
    - *Function*: `Synchronize Community Visit Record` — Ingests clinical notes, wound assessments, and vital signs recorded in community and home settings.
    - *Exposed Service*: `Community Care Encounter Ingress` — Ingests community care documentation.
  - **Capability-scoped Functions / Services**:
    - *Feature association not established*: Strategy `FEAT-SD-16 — Community Care Plan Synchronization` remains valid, but semantic evidence does not establish that the following Function or Service belongs to, realises, specialises or is equivalent to it. No replacement Feature is assigned.
    - *Function*: `Bind Community Support Network Context` — Links community nursing teams with local non-clinical social care and home care packages.
    - *Exposed Service*: `Community Care Team Query` — Exposes community provider contact rosters.
- **Typed architectural dependencies**:
  - **Capability dependencies**: `Client Relationship`, `Episode & Encounter Administration`.
  - **Collaboration reference with unresolved involvement**: `Care-Team Collaboration` is an established Collaboration; precise participation/involvement is not allocated by the former “Consumes” wording.

---

<a id="29-outreach-care-enablement"></a>

### 2.9 Outreach Care
- **Owning Capability**: `Outreach Care`
- **Clinical Operating Context**: Mobile specialist clinics, rural and remote fly-in fly-out (FIFO) clinical teams, and school health screening programmes.
- **Enabling Functions & Exposed Services**:
  - **Feature: Outreach Visit Context Provision**:
    - *Function*: `Provide Outreach Visit Context` — Packages and delivers portable clinical summaries and offline-capable care summaries for remote outreach clinics.
    - *Derivation*: `FEAT-SD-17` in [Strategy's Outreach Care Continuity](../../02-strategy/capabilities/business-enabling-capabilities.md#15-outreach-care-harmonia-relevant) establishes this preparation/provision responsibility. Outcome reconciliation below does not supply pre-visit context. This Function remains owned by Outreach Care, uses governed source information without acquiring its originating authority and establishes no copying, storage or offline implementation mechanism.
    - *Exposure boundary*: Remote outreach clinics are the established recipients. No separate named Service, Process, Role allocation or structural ID is introduced merely to mirror the Function.
  - **Feature: Outreach Encounter Outcome Ingestion**:
    - *Function*: `Reconcile Outreach Clinical Batch` — Reconciles and merges clinical documentation recorded during disconnected remote clinic operations.
    - *Exposed Service*: `Outreach Batch Reconciliation Service` — Reconciles batch clinical encounters upon network reconnection.
- **Typed architectural dependencies**:
  - **Capability dependencies**: `Health Information Exchange`, `Clinical Record Administration`.
  - **Established Service with unresolved consumption applicability**: `Person Identity Correction` retains Person Identity ownership; how Outreach Care requires that Service is not established here. It cannot authorise originating person-merge decisions.

---

<a id="210-remote--virtual-care-enablement"></a>

### 2.10 Remote & Virtual Care
- **Owning Capability**: `Remote & Virtual Care`
- **Clinical Operating Context**: Telehealth consultations, virtual hospital-in-the-home (vHIT), remote patient monitoring (RPM), and asynchronous specialist e-consults.
- **Enabling Functions & Exposed Services**:
  - **Feature: Virtual Care Session Context Binding**:
    - *Function*: `Correlate Virtual Care Consultation Context` — Binds video consultation metadata, remote practitioner identity, and shared digital whiteboard artifacts to the encounter.
    - *Exposed Service*: `Virtual Encounter Context Resolution` — Binds virtual session identifiers to patient encounters.
  - **Feature: Remote Telemetry Ingestion**:
    - *Function*: `Ingest Remote Patient Telemetry` — Ingests continuous or periodic physiological measurements (e.g., blood pressure, pulse oximetry, continuous glucose) from home monitoring devices.
    - *Exposed Service*: `Remote Telemetry Ingress` — Ingests remote patient data streams into the LHR.
- **Capability dependencies**: `Clinical Device Administration`, `Patient Clinical Record`, `Clinical Communication Administration`; no specific Service is allocated by this contextual list.
