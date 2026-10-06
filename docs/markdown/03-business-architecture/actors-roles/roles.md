# Business Role Model

## 1. Definition & Architectural Purpose

In the Harmonia Business Architecture, a **Business Role** represents a named, stable functional capacity, responsibility, or entitlement fulfilled by a Business Actor in healthcare business interactions, collaborative processes, or governance contexts.

A Role answers the fundamental architectural question:
> **In what functional capacity, authority, or responsibility is the participant acting?**

While an Actor defines *what kind of entity participates*, a Role defines *the operational or legal capacity in which it participates*. A single Actor may fulfil multiple distinct Roles across different interactions or simultaneously.

---

## 2. The 6 Business Role Families

Harmonia classifies all canonical Business Roles into six distinct families:

```text
Business Role
├── Care Participant Roles
│   ├── Client
│   │   └── Patient
│   └── Subject of Care
├── Care Delivery Roles
│   ├── Practitioner
│   │   └── Clinician
│   ├── Care Coordinator
│   ├── Referrer
│   ├── Requester
│   ├── Prescriber
│   └── Performer
├── Care Support Roles
│   ├── Carer
│   ├── Support Person
│   ├── Advocate
│   └── Representative
│       ├── Guardian
│       ├── Substitute Decision-Maker
│       └── Delegate / Proxy
├── Operational Roles
│   ├── Service Coordinator
│   ├── Ward Coordinator
│   ├── Patient Flow Coordinator
│   ├── Bed Manager
│   ├── Work Coordinator
│   └── Wardsperson
├── Governance / Authority Roles
│   ├── Regulator
│   ├── Policy Authority
│   ├── System Steward
│   ├── Information Steward
│   ├── Information Custodian
│   └── Terminology Steward
└── Information / Service Participation Roles
    ├── Service Provider
    ├── Information Supplier
    └── Information Client
```

---

### 2.1 Care Participant Roles

Care Participant Roles represent individuals or subjects receiving healthcare, health monitoring, or preventive services.

- **Client**: A person or party who enters into a service relationship with a healthcare organisation or practitioner.
  - **Patient**: A Client actively receiving clinical assessment, diagnosis, medical treatment, nursing care, or direct clinical management.
- **Subject of Care**: The identifiable individual whose health, clinical observations, biological samples, or clinical documents form the subject matter of an encounter or health record (often synonymous with Patient, but distinct when specimens, genomic data, or population screening cohorts are processed).

---

### 2.2 Care Delivery Roles

Care Delivery Roles represent healthcare professionals, clinical teams, and service agents directly involved in clinical decision-making, order placement, or service execution.

- **Practitioner**: An individual registered, certified, or qualified to provide healthcare services.
  - **Clinician**: A Practitioner actively engaged in direct clinical assessment, diagnosis, treatment, or medical decision-making.
- **Care Coordinator**: A practitioner or case manager responsible for planning, synchronising, and monitoring cross-specialty or cross-setting care delivery for a patient.
- **Referrer**: A practitioner or service agent who formally requests another service provider to assess, manage, or assume partial/total clinical responsibility for a patient.
- **Requester**: A practitioner or clinical agent who formally places an order for a diagnostic investigation, medication supply, or procedural intervention.
- **Prescriber**: A practitioner legally authorised and clinically responsible for authorising the dispensing and administration of scheduled medications.
- **Performer**: A clinician, proceduralist, technician, or care agent who executes a diagnostic test, surgical procedure, clinical observation, or therapeutic intervention.

---

### 2.3 Care Support Roles

Care Support Roles represent informal carers, personal supporters, patient advocates, and legally constituted representatives who support the patient or exercise surrogate decision-making authority.

- **Carer**: An individual who provides unpaid, ongoing personal care, support, or assistance to a patient.
- **Support Person**: An individual nominated by the patient to accompany them, provide moral/emotional support, or assist in communication during care episodes.
- **Advocate**: An individual or agency acting on behalf of the patient to promote their rights, preferences, and interests within the healthcare system.
- **Representative**: An individual or legal entity empowered to act or make decisions on behalf of the patient.
  - **Guardian**: A legally appointed guardian exercising general legal custody and personal welfare decision-making.
  - **Substitute Decision-Maker**: An individual legally authorised to make specific medical treatment decisions when the patient lacks decision-making capacity.
  - **Delegate / Proxy**: An individual nominated by the patient or authorised by law to access health information, receive communications, or perform specific administrative acts.

---

### 2.4 Operational Roles

Operational Roles represent personnel and logistics agents responsible for managing healthcare facilities, inpatient wards, theatre suites, bed capacity, work allocation, and physical patient movement.

- **Service Coordinator**: An operational administrator who manages service schedules, clinic queues, and service capacity.
- **Ward Coordinator**: A senior nurse or ward administrator responsible for shift-level operational oversight, staffing balance, and bed usage within a clinical unit.
- **Patient Flow Coordinator**: A facility-wide coordinator managing admissions, emergency department egress, transfers, and bed allocations across hospital directorates.
- **Bed Manager**: An operational agent who tracks physical bed availability, assigns care-places, and coordinates bed turnover logistics.
- **Work Coordinator**: An operational manager or dispatch supervisor who creates, assigns, reallocates, and tracks operational work tasks (e.g., portering, cleaning, specimen transport).
- **Wardsperson**: An operational staff member (orderly/porter) responsible for the physical transport of patients, equipment, specimens, and clinical supplies.

---

### 2.5 Governance / Authority Roles

Governance and Authority Roles represent statutory regulators, policy bodies, and enterprise data stewards responsible for compliance, privacy governance, terminology standards, and system security.

- **Regulator**: A statutory authority responsible for professional registration, healthcare standards enforcement, and accreditation (e.g., AHPRA).
- **Policy Authority**: An entity responsible for defining enterprise or jurisdictional clinical, security, and operational policies.
- **System Steward**: An organisational authority responsible for the governance, availability, and lifecycle integrity of digital health systems.
- **Information Steward**: An authority responsible for defining the governance policies, classification rules, retention periods, and privacy standards for health information assets.
- **Information Custodian**: An entity or designated role holding operational custody and legal responsibility for safeguarding, preserving, and managing access to health records.
- **Terminology Steward**: An authority responsible for authoring, validating, and governing canonical value sets, clinical ontologies, and semantic mapping tables.

---

### 2.6 Information / Service Participation Roles

Information and Service Participation Roles represent general participant capacities in service provision and information exchange.

- **Service Provider**: An organisation, facility, or practitioner acting in the capacity of delivering a defined healthcare or diagnostic service.
- **Information Supplier**: A participant (person, organisation, or system) that routinely produces, transmits, or submits healthcare information, diagnostic results, or clinical documents into the integration environment.
- **Information Client**: A participant (person, organisation, or system) that queries, retrieves, or consumes healthcare information for clinical review or operational support.

---

## 3. Role Distinction Guardrails and Architectural Invariants

Harmonia enforces strict guardrails to prevent role conflation and maintain architectural clarity:

### 3.1 Distinct Authority and Membership Concepts
> **Care Team membership, caring responsibility, advocacy, representation, information-access authority, consent authority, and clinical decision-making authority are distinct concepts and must not be inferred from one another.**

- A **Carer** does not automatically hold **Substitute Decision-Maker** authority.
- A **Support Person** does not automatically hold **Information Access** authority.
- A member of a **Care Team** does not automatically have unrestricted authority to access all historical health information outside the clinical encounter scope.
- **Consent Authority** derives from the patient or legitimate legal representative; **Governance Authority** derives from statutory or organisational mandate. Neither implies the other.

### 3.2 Referral Direction is Contextual
- Do **not** introduce `Referring Provider` or `Receiving Provider` as fundamental architectural Roles.
- Referral direction is contextual: an organisation or practitioner acts in the stable role of `Service Provider` or `Practitioner`, taking on the contextual qualifier of `Referrer` (source) or `Referee` (target) within a specific `Service Referral` interaction.

### 3.3 Practitioner vs. Service Provider
- `Practitioner` is **not** synonymous with `Service Provider`.
- A `Practitioner` is an individual registered professional. A `Service Provider` is an organisation, facility, or practitioner acting in the business capacity of delivering an agreed healthcare service offering. A Practitioner may fulfil the `Service Provider` role in private practice.

### 3.4 Job Titles are Not Architectural Roles
- Generic professional job titles (e.g., *Doctor*, *Nurse*, *Radiographer*, *Pharmacist*) must **not** be modelled as fundamental architectural Business Roles. They represent professional credentials and qualifications, while `Clinician`, `Prescriber`, `Performer`, and `Care Coordinator` represent architectural operational capacities.
