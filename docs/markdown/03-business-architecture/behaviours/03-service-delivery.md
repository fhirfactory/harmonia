# Service Delivery — Capability-Scoped Behaviour

## 1. Contextual Scope & Architectural Intent

The named owning Capabilities and established Features retain their responsibilities. Affected Capability Tier, complete ancestry, root status and structural Canonical IDs remain unresolved under [approved G1 K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing); document grouping and numbering do not establish architectural identity or hierarchy.

Service Delivery represents the clinical operating contexts in which direct patient care is delivered across primary, acute, emergency, inpatient, diagnostic, and virtual settings.

### The Fundamental Care Enablement Principle
> **Harmonia enables, informs, and coordinates healthcare delivery across care boundaries; it does not practice medicine, prescribe pharmaceuticals, make autonomous clinical decisions, or replace clinician judgment.**

Service Delivery capabilities within Harmonia define **integration enablement and clinical context correlation behaviour**. They reuse and consume core capabilities (*Patient Clinical Record, Health Information Exchange, Health Information Control, Episode & Encounter Administration, Clinical Record Administration*) rather than duplicating their responsibilities.

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
- **Architectural Dependencies**: Consumes *Person Identifier Resolution*, *Patient Clinical Record Query*, *Referral Submission*, *Care Plan Administration*.

---

<a id="22-acute-care-enablement"></a>

### 2.2 Acute Care
- **Owning Capability**: `Acute Care`
- **Clinical Operating Context**: Hospital specialty wards, surgical suites, and intensive care units managing severe, urgent, or post-operative conditions.
- **Enabling Functions & Exposed Services**:
  - **Feature: Acute Care Clinical Context Provision**:
    - *Function*: `Assemble Acute Clinical Baseline` — Rapidly aggregates previous discharge summaries, active medications, allergies, and diagnostic alerts upon acute hospital presentation.
    - *Exposed Service*: `Acute Admission Dossier Query` — Delivers a high-priority clinical briefing to admitting teams.
  - **Feature: Acute Clinical State Event Capture**:
    - *Function*: `Route Acute Critical Result Alert` — Immediately directs urgent lab/radiology alerts (e.g., critical troponin, intracranial bleed) to active acute attending clinicians.
    - *Exposed Service*: `Acute Clinical Alert Dispatch` — Dispatches urgent interruptive alerts to on-duty ward teams.
- **Architectural Dependencies**: Consumes *Encounter Context Resolution*, *Diagnostic Result Delivery*, *Health Information Control (Break-Glass Access)*.

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
- **Architectural Dependencies**: Consumes *Person Identifier Resolution (Fast-Match)*, *Client Privacy (Break-Glass Override)*, *Encounter State Progression*.

---

<a id="24-inpatient-care-enablement"></a>

### 2.4 Inpatient Care
- **Owning Capability**: `Inpatient Care`
- **Clinical Operating Context**: Inpatient hospital wards, rehabilitation units, and sub-acute facilities managing multi-day hospital stays.
- **Enabling Functions & Exposed Services**:
  - **Feature: Inpatient Rounding Context Provision**:
    - *Function*: `Consolidate Inpatient Clinical Round Feed` — Aggregates daily vital signs, overnight pathology, allied health notes, and medication administration records.
    - *Exposed Service*: `Ward Round Clinical Briefing Query` — Discloses consolidated 24-hour clinical briefing views.
  - **Feature: Inpatient Clinical Progression Tracking**:
    - *Function*: `Assemble Inpatient Discharge Dossier` — Synthesises inpatient diagnoses, procedural summaries, reconciled discharge medications, and follow-up instructions into a canonical discharge summary package.
    - *Exposed Service*: `Discharge Dossier Packaging Service` — Emits canonical discharge summaries to GPs and community providers.
- **Architectural Dependencies**: Consumes *Encounter Care-Place Movement*, *Clinical Document Lifecycle*, *Medication Administration Event Ingestion*, *Discharge Management*.

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
- **Architectural Dependencies**: Consumes *Order Administration (Closed-Loop Progression)*, *Information Design Governance (LOINC/SNOMED Bindings)*.

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
- **Architectural Dependencies**: Consumes *Medication Administration*, *Patient Clinical Record*, *Health Information Control*.

---

<a id="27-preventive-care-enablement"></a>

### 2.7 Preventive Care
- **Owning Capability**: `Preventive Care`
- **Clinical Operating Context**: Public health screening programmes (breast/bowel/cervical), national immunisation registries, and health promotion initiatives.
- **Enabling Functions & Exposed Services**:
  - **Feature: Immunisation Event Submission**:
    - *Function*: `Syndicate Immunisation Record` — Records vaccine encounters and syndicates records to national immunisation registers (e.g., AIR).
    - *Exposed Service*: `Immunisation History Query` — Discloses complete vaccination history.
  - **Feature: Screening Recall Notification Distribution**:
    - *Function*: `Project Screening Eligibility Timeline` — Tracks age-based and risk-based screening schedules across primary care.
    - *Exposed Service*: `Preventive Screening Status Query` — Discloses screening due dates to primary care providers.
- **Architectural Dependencies**: Consumes *Health Information Exchange (Syndication)*, *Healthcare Subject Demographic Context*.

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
- **Architectural Dependencies**: Consumes *Client Relationship*, *Care-Team Collaboration*, *Episode & Encounter Administration*.

---

<a id="29-outreach-care-enablement"></a>

### 2.9 Outreach Care
- **Owning Capability**: `Outreach Care`
- **Clinical Operating Context**: Mobile specialist clinics, rural and remote fly-in fly-out (FIFO) clinical teams, and school health screening programmes.
- **Enabling Functions & Exposed Services**:
  - **Feature: Outreach Encounter Outcome Ingestion**:
    - *Function*: `Reconcile Outreach Clinical Batch` — Reconciles and merges clinical documentation recorded during disconnected remote clinic operations.
    - *Exposed Service*: `Outreach Batch Reconciliation Service` — Reconciles batch clinical encounters upon network reconnection.
- **Architectural Dependencies**: Consumes *Health Information Exchange*, *Person Identity Correction*, *Clinical Record Administration*.

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
- **Architectural Dependencies**: Consumes *Clinical Device Administration*, *Patient Clinical Record*, *Clinical Communication Administration*.
