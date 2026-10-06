# Business Actor Model

## 1. Definition & Architectural Purpose

In the Harmonia Business Architecture, a **Business Actor** represents an independently identifiable entity that participates in healthcare business interactions, collaborative structures, and operational workflows.

An Actor answers the fundamental architectural question:
> **What kind of independently identifiable participant is this?**

An Actor represents the *entity type* of the participant, completely independent of the functional capacities (*Roles*) it may fulfil in any specific operational context.

---

## 2. The 7 Business Actor Categories

Harmonia recognises exactly seven canonical Business Actor categories:

```text
Business Actor
├── Person
├── Group
├── Organisation
├── Organisational Unit
├── Government / Regulatory Body
├── System
└── Device
```

### 2.1 Person
- **Definition**: An individual human being participating in the healthcare ecosystem.
- **Architectural Scope**: Encompasses patients, healthcare practitioners, informal carers, legal guardians, system administrators, and operational coordinators.
- **Examples**:
  - A patient receiving care in an emergency department.
  - A medical practitioner authoring a clinical discharge summary.
  - A nominated carer exercising authorised substitute decision-making.

### 2.2 Group
- **Definition**: An informal or formal collective of individuals acting with a shared purpose or within a common social/clinical structure, without constituting an incorporated legal entity or formal organisational unit.
- **Architectural Scope**: Encompasses multidisciplinary care teams, clinical review panels, patient families, and peer support collectives.
- **Examples**:
  - The Acute Stroke Rapid Response Team.
  - A patient's family support group.
  - A multidisciplinary tumour board.

### 2.3 Organisation
- **Definition**: A formally constituted legal, commercial, public, or healthcare entity recognised under national or regional law.
- **Architectural Scope**: Encompasses healthcare networks, private hospital operators, pathology providers, diagnostic imaging networks, health insurance providers, and community health trusts.
- **Examples**:
  - Canberra Health Services (CHS).
  - ACT Pathology.
  - Capital Health Network (Primary Health Network).

### 2.4 Organisational Unit
- **Definition**: A defined administrative, functional, or operational subdivision within an Organisation.
- **Architectural Scope**: Encompasses clinical directorates, specialty departments, hospital wards, outpatient clinics, and administrative divisions.
- **Examples**:
  - Department of Respiratory Medicine.
  - Ward 11A (Inpatient Cardiology).
  - Health Information Management & Medical Records Directorate.

### 2.5 Government / Regulatory Body
- **Definition**: A statutory authority, government department, national agency, or regulatory body exercising legislative, jurisdictional, accreditation, or policy authority over healthcare delivery.
- **Architectural Scope**: Encompasses national digital health authorities, practitioner registration boards, health ombudsmen, and statutory data custodians.
- **Examples**:
  - Australian Health Practitioner Regulation Agency (AHPRA).
  - Australian Digital Health Agency (ADHA).
  - ACT Health Directorate (Policy & Governance Branch).

### 2.6 System
- **Definition**: An autonomous external software platform, electronic medical record, enterprise registry, or departmental clinical system that participates directly in business interactions by authoring, receiving, or transforming healthcare information.
- **Architectural Scope**: Encompasses external PAS, EMR, LIS, RIS-PACS, and national registries that act as independent business participants.
- **Examples**:
  - ACT Pathology Laboratory Information System (LIS).
  - Epic EMR instance at a tertiary hospital.
  - National Cancer Registry.

### 2.7 Device
- **Definition**: A physical medical instrument, diagnostic appliance, point-of-care monitor, or clinical apparatus that directly participates in care delivery or automated clinical observation.
- **Architectural Scope**: Encompasses patient bedside monitors, automated infusion pumps, smart point-of-care blood gas analysers, and diagnostic imaging scanners.
- **Examples**:
  - Bedside Vital Signs Monitor in an ICU bay.
  - Volumetric Infusion Pump transmitting rate telemetry.
  - Point-of-care glucometer associated with a clinical encounter.

---

## 3. Actor Guardrails and Invariants

### 3.1 Actor vs. Role Demarcation
- An **Actor** defines *who or what* exists (e.g., `Person`, `Organisation`, `System`).
- A **Role** defines *in what capacity* the actor acts (e.g., `Patient`, `Clinician`, `InformationSupplier`).
- Actors fulfil Roles; an Actor must **never** be conflated with the Role it fulfils.

### 3.2 External Systems vs. Internal Application Components
- **System and Device as Business Actors**: External software applications (e.g., `ACT Pathology LIS`) and clinical appliances (e.g., `Bedside Monitor`) are legitimate Business Actors when they autonomously initiate or receive business interactions.
- **Internal Harmonia Components are NOT Business Actors**: Internal software modules, pipelines, queues, or databases (such as ingress gateways, persistence engines, or workflow coordinators) are implementation mechanisms that realise capabilities. They **must never be modelled as Business Actors**.
