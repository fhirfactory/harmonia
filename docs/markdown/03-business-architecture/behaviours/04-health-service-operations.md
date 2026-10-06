# Health Service Operations — Capability-Scoped Behaviour

## 1. Contextual Scope & Architectural Intent

Health Service Operations governs the operational logistics, physical resource management, facility capacity, staff dispatch, and operational movement required to support healthcare delivery across clinical facilities.

### Operational Logistics vs. Software Plumbing
> **In Domain 03, "Operations" refers strictly to healthcare facility logistics (e.g., bed turnover, patient portering, theatre case scheduling, clinic queues, dispatch). It does not mean IT middleware monitoring, application deployment, or database administration.**

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
├── 14. Clinical Logistics
└── 15. Discharge Management
```

---

### 2.1 Clinic & Practice Operations
- **Owning Capability**: `L1: Clinic & Practice Operations`
- **Functions & Exposed Services**:
  - **Feature: Operational Queue Progression**:
    - *Function*: `Manage Clinic Queue Progression` — Tracks patient arrival, rooming, consultation in-progress, and check-out states.
    - *Exposed Service*: `Clinic Queue Telemetry Service` — Discloses real-time clinic waiting times and queue states.
    - *Governed Process*: **Clinic / Operational Queue Progression Process**.
- **Information Responsibility**: Clinic Queue State, Patient Waiting Time Metrics.

---

### 2.2 Ward Operations
- **Owning Capability**: `L1: Ward Operations`
- **Functions & Exposed Services**:
  - **Feature: Ward Patient Flow Coordination**:
    - *Function*: `Coordinate Ward Operational State` — Manages ward acuity distribution, nurse-to-patient ratios, and isolation cohorting.
    - *Exposed Service*: `Ward Operational Summary Query` — Discloses ward-level operational telemetry.
- **Information Responsibility**: Ward Shift Status Ledger, Acuity Balance Register.

---

### 2.3 Theatre Operations
- **Owning Capability**: `L1: Theatre Operations`
- **Functions & Exposed Services**:
  - **Feature: Operating Theatre Case Progression**:
    - *Function*: `Manage Theatre Case Progression` — Coordinates surgical case milestones: *Case Called* $\to$ *Patient in Anaesthetic Bay* $\to$ *Anaesthesia Commenced* $\to$ *Knife to Skin* $\to$ *Procedure Finished* $\to$ *In PACU/Recovery* $\to$ *Ward Handover Complete*.
    - *Exposed Service*: `Theatre Case Progression Telemetry` — Emits real-time surgical milestone updates.
    - *Governed Process*: **Theatre Case Progression Process**.
- **Information Responsibility**: Theatre Slate Register, Perioperative Milestone Log.

---

### 2.4 Emergency Department Operations
- **Owning Capability**: `L1: Emergency Department Operations`
- **Functions & Exposed Services**:
  - **Feature: ED Length of Stay Tracking**:
    - *Function*: `Coordinate ED Departmental Capacity` — Tracks waiting room occupancy, resuscitation bay availability, and time-to-triage metrics.
    - *Exposed Service*: `ED Operational Status Query` — Discloses facility ED occupancy and surge level.
- **Information Responsibility**: ED Operational Dashboard Ledger, Resuscitation Bay Occupancy Matrix.

---

### 2.5 Outpatient Operations
- **Owning Capability**: `L1: Outpatient Operations`
- **Functions & Exposed Services**:
  - **Feature: Outpatient Arrival Registration**:
    - *Function*: `Coordinate Outpatient Session Utilisation` — Tracks specialty clinic room allocation and clinician arrival.
    - *Exposed Service*: `Ambulatory Session Status Query` — Exposes clinic room occupancy.
- **Information Responsibility**: Outpatient Session Schedule, Room Allocation Register.

---

### 2.6 Bed & Care-Place Management
- **Owning Capability**: `L1: Bed & Care-Place Management`
- **Key Architectural Semantics**:
  - **Operational State vs. Definition**: While *Location Administration* defines the physical care-place, *Bed & Care-Place Management* owns the real-time operational status (*Available*, *Occupied*, *Reserved*, *Blocked*, *Dirty*, *Cleaning In-Progress*, *Maintenance Lock*).
- **Functions & Exposed Services**:
  - **Feature: Bed Availability Tracking**:
    - *Function*: `Maintain Operational Bed State` — Tracks real-time bed status and environmental locks.
    - *Exposed Service*: `Bed Availability Query` — Discloses real-time bed availability across hospital directorates.
  - **Feature: Bed State Progression**:
    - *Function*: `Manage Bed Turnover Progression` — Coordinates cleaning, disinfection, and preparation: *Vacated* $\to$ *Cleaning Requested* $\to$ *Cleaning In-Progress* $\to$ *Inspected/Ready* $\to$ *Occupied*.
    - *Exposed Service*: `Bed Turnover Lifecycle Service` — Coordinates turnover requests with environmental services.
    - *Governed Process*: **Bed Turnover Process**.
- **Information Responsibility**: Operational Bed Status Registry, Care-Place Lock Register, Bed Turnover Milestone Log.

---

### 2.7 Clinical Resource Management
- **Owning Capability**: `L1: Clinical Resource Management`
- **Functions & Exposed Services**:
  - **Feature: Mobile Clinical Equipment Tracking**:
    - *Function*: `Track Mobile Clinical Asset Operational State` — Manages the real-time location and operational readiness of mobile ventilators, infusion pumps, and telemetry transmitters.
    - *Exposed Service*: `Clinical Resource Availability Query` — Exposes mobile equipment availability.
- **Information Responsibility**: Mobile Clinical Asset Operational Ledger, Maintenance Readiness Matrix.

---

### 2.8 Service Capacity Management
- **Owning Capability**: `L1: Service Capacity Management`
- **Functions & Exposed Services**:
  - **Feature: Operational Capacity Metric Aggregation**:
    - *Function*: `Aggregate Service Capacity Telemetry` — Gathers bed occupancy, ICU surge capacity, and ventilator availability across regional hospitals.
    - *Exposed Service*: `Regional Health System Capacity Query` — Provides executive operational situational awareness.
- **Information Responsibility**: Regional System Capacity Snapshot, Surge Level State.

---

### 2.9 Mobile Staff Management
- **Owning Capability**: `L1: Mobile Staff Management`
- **Functions & Exposed Services**:
  - **Feature: Mobile Worker Task Dispatch**:
    - *Function*: `Track Operational Staff Presence` — Tracks active on-duty presence and physical zone assignment for porters, phlebotomists, and roving nurses.
    - *Exposed Service*: `Staff Presence Telemetry Query` — Discloses on-duty staff availability by hospital zone.
- **Information Responsibility**: Staff Operational Presence Register, Zone Allocation Matrix.

---

### 2.10 On-Call Management
- **Owning Capability**: `L1: On-Call Management`
- **Functions & Exposed Services**:
  - **Feature: Active On-Call Provider Resolution**:
    - *Function*: `Resolve Active On-Call Coverage` — Resolves primary, secondary, and tertiary on-call clinical specialists for emergency escalation.
    - *Exposed Service*: `On-Call Specialist Lookup` — Discloses active on-call contacts for switchboard and emergency teams.
- **Information Responsibility**: On-Call Roster Projection, Escalation Contact Matrix.

---

### 2.11 Work Allocation & Dispatch
- **Owning Capability**: `L1: Work Allocation & Dispatch`
- **Key Architectural Semantics**:
  - **Ownership Guardrail**: Owns the dispatching, queuing, assignment, and operational progression of non-clinical work orders (e.g., patient transfers, cleaning, specimen pickups). It does **not** acquire ownership of Practitioners, Locations, Services, or Credentials.
- **Functions & Exposed Services**:
  - **Feature: Work Item Instantiation**:
    - *Function*: `Dispatch Operational Work Order` — Allocates work requests to available wardspersons or support teams based on proximity and skill.
    - *Exposed Service*: `Work Dispatch Service` — Emits job dispatches to mobile staff devices.
  - **Feature: Work Progress Oversight & Escalation**:
    - *Function*: `Manage Operational Work Progression` — Tracks execution states: *Queued* $\to$ *Assigned* $\to$ *Accepted* $\to$ *In-Progress* $\to$ *Completed / Aborted*.
    - *Exposed Service*: `Operational Work Status Query` — Discloses task progression to requesting wards.
    - *Governed Process*: **Operational Work Progression Process**.
- **Information Responsibility**: Operational Work Dispatch Ledger, Job Progression Milestone Ledger.

---

### 2.12 Credential Management
- **Owning Capability**: `L1: Credential Management`
- **Functions & Exposed Services**:
  - **Feature: Operational Credential Check**:
    - *Function*: `Verify Operational Access Credential` — Verifies physical access badges, specialty ward entry authorizations, and restricted area permissions.
    - *Exposed Service*: `Operational Credential Checkpoint Service` — Evaluates physical/operational access rights.
- **Information Responsibility**: Operational Access Authorization Ledger, Badge Credential Binding Register.

---

### 2.13 Patient Transport
- **Owning Capability**: `L1: Patient Transport`
- **Functions & Exposed Services**:
  - **Feature: Transport Dispatch & Progress Tracking**:
    - *Function*: `Manage Patient Transport Progression` — Coordinates intra-facility and inter-facility patient movements: *Transport Requested* $\to$ *Porter Dispatched* $\to$ *Patient Collected* $\to$ *In-Transit* $\to$ *Delivered at Destination* $\to$ *Handover Completed*.
    - *Exposed Service*: `Patient Transport Tracking Service` — Provides real-time transit telemetry.
    - *Governed Process*: **Patient Transport Process**.
- **Information Responsibility**: Patient Transport Request Master, Transit Milestone Log.

---

### 2.14 Clinical Logistics
- **Owning Capability**: `L1: Clinical Logistics`
- **Functions & Exposed Services**:
  - **Feature: Pathology Specimen Transport Tracking**:
    - *Function*: `Manage Specimen Transport Progression` — Tracks the physical transit of pathology bio-specimens, blood products, and surgical trays: *Collected* $\to$ *Courier Picked Up* $\to$ *In-Transit* $\to$ *Laboratory Ingress Received*.
    - *Exposed Service*: `Specimen Transit Tracking Service` — Exposes chain-of-custody tracking.
    - *Governed Process*: **Specimen Transport Process**.
- **Information Responsibility**: Specimen Courier Manifest, Chain-of-Custody Tracking Register.

---

### 2.15 Discharge Management
- **Owning Capability**: `L1: Discharge Management`
- **Functions & Exposed Services**:
  - **Feature: Discharge Readiness & Coordination Oversight**:
    - *Function*: `Manage Discharge Coordination Progression` — Synchronises multi-agency discharge readiness: *Discharge Planning Initiated* $\to$ *Medications Reconciled* $\to$ *Transport Booked* $\to$ *Discharge Summary Finalised* $\to$ *Physically Departed*.
    - *Exposed Service*: `Discharge Readiness Telemetry` — Discloses discharge barrier checklists and planned departure times.
    - *Governed Process*: **Discharge Progression Process**.
- **Information Responsibility**: Discharge Readiness Checklist Ledger, Estimated Date of Discharge (EDD) Register.
