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
│   ├── Terminology Steward
│   └── Guardian
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

Governance and Authority Roles represent statutory regulators, policy bodies, enterprise data stewards and independent governed-assurance responsibility. Membership of this family does not make governance definition, stewardship and Guardianship the same responsibility or grant the Guardian authority to define governing requirements.

- **Regulator**: A statutory authority responsible for professional registration, healthcare standards enforcement, and accreditation (e.g., AHPRA).
- **Policy Authority**: An entity responsible for defining enterprise or jurisdictional clinical, security, and operational policies.
- **System Steward**: An organisational authority responsible for the governance, availability, and lifecycle integrity of digital health systems.
- **Information Steward**: An authority responsible for defining the governance policies, classification rules, retention periods, and privacy standards for health information assets.
- **Information Custodian**: An entity or designated role holding operational custody and legal responsibility for safeguarding, preserving, and managing access to health records.
- **Terminology Steward**: An authority responsible for authoring, validating, and governing canonical value sets, clinical ontologies, and semantic mapping tables.

<a id="guardian-governed-assurance"></a>

#### Guardian — Governed Assurance

- **Canonical Name**: `Guardian`. **Element Type**: Business Role. Canonical ID remains unresolved; the metamodel does not establish Role identifier tokens or structural context. This definition is distinct from the existing care-support Guardian under Representative; the name/reference collision is explicitly unresolved in [§3.6](#36-canonical-role-name-collision--unresolved).
- **Definition**: A Business Role responsible for independently evaluating governed activity, information, state or outcomes against applicable assurance criteria and establishing assurance findings and conclusions from sufficient trustworthy evidence.
- **Derivation**: [REQ-FND-005](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity), [BC-18 Health Service Assurance](../../02-strategy/capabilities/business-capabilities.md#18-health-service-assurance) and [Governed Assurance](../../02-strategy/capabilities/business-enabling-capabilities.md#governed-assurance) establish the independent evaluation/conclusion responsibility documented in the [bounded Domain03 assurance derivation](../behaviours/health-service-assurance.md). Guardian is not EC-14 Service Guardian: EC-14 is an Enterprise Capability, not a Business Role or software component.
- **Management Boundary**: A Guardian may manage its own assurance activity but does not thereby manage, perform, remediate, assign, delegate or operationally escalate the subject being assured. Findings may identify a need for operational response; responsibility for that response remains with its applicable operational capability and authority.
- **Clinical Boundary**: Guardian confers no clinical authority and does not establish clinical adequacy, clinical correctness or professional clinical judgement. Clinical review and clinical assurance remain with their applicable clinical processes and authorities. Information concerning clinical activity may be subject to Guardianship where an explicit applicable governed requirement establishes that concern; this does not constitute Clinical Services Delivery Assurance.
- **Conclusion Boundary**: Evidence assembly and assessment do not themselves establish a conclusion. Adjudication establishes the finding or conclusion from assessed evidence against applicable criteria. Where evidence is insufficient to conclude with the required confidence, insufficiency remains explicit and implies neither satisfaction nor non-satisfaction. This is distinct from execution failure or uncertainty about whether assurance activity occurred.
- **Allocation Boundary**: The Role establishes evaluation and conclusion responsibility within Governed Assurance. It does not automatically assign Assurance Design, criterion approval, clinical review or operational response to Guardian. Eligible Actors, mandates, additional Role relationships, Interactions and Collaborations remain unresolved. It has no application, runtime or implementation allocation.

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
- `Referrer` remains the canonical Business Role representing the capacity to formally refer, distinct from `Requester` and `Service Provider`. An Actor fulfils a Role and participates in a particular Referral; the Actor, Role, participation, local Relationship Role and context qualifier are distinct.
- Practitioner fulfilment of Referrer is supported. Eligibility of other Actor kinds is not exhaustively established; an Organisation being a referral source does not by itself establish fulfilment of the Referrer Business Role.
- `Organisation#ACT Health-as-ServiceProvider#ReferralSource` remains a qualified reference: `ReferralSource` is a contextual qualifier, not a new Business Role. The target qualifier/role is not established by the former `Referee` wording and remains unresolved.
- Package1 `Referring Clinician` remains a local Relationship Role; it does not redefine the Referrer Business Role.

### 3.3 Practitioner vs. Service Provider
- `Practitioner` is **not** synonymous with `Service Provider`.
- A `Practitioner` is an individual registered professional. A `Service Provider` is an organisation, facility, or practitioner acting in the business capacity of delivering an agreed healthcare service offering. A Practitioner may fulfil the `Service Provider` role in private practice.

### 3.4 Job Titles are Not Architectural Roles
- Generic professional job titles (e.g., *Doctor*, *Nurse*, *Radiographer*, *Pharmacist*) must **not** be modelled as fundamental architectural Business Roles. They represent professional credentials and qualifications, while `Clinician`, `Prescriber`, `Performer`, and `Care Coordinator` represent architectural operational capacities.

### 3.5 Governance, Management and Guardianship Independence

**Governance defines → Management performs → Guardianship assures** is a semantic responsibility distinction, not a required organisational structure, execution sequence or technical topology. A real-world Actor may potentially fulfil different Roles in different contexts where governance permits. REQ-FND-005 still requires independently governed assurance progression and conclusion: the subject's performer or manager must not solely determine, suppress, manufacture or retrospectively alter its assurance outcome. Supplying evidence does not confer control of assurance.

Existing Clinician, Practitioner, Care Coordinator, Policy Authority, steward and operational Role responsibilities remain distinct. Clinical review or clinical assurance does not become Guardian responsibility through lexical similarity, and no generic clinical-assurance authority Role is created. A Role does not itself confer information-access authority.

### 3.6 Canonical Role Name Collision — Unresolved

The incoming catalogue already uses **Guardian** under **Care Support → Representative** for legally appointed custody and personal welfare decision-making. That definition remains authoritative and unchanged. The newly requested **Guardian** under **Governance / Authority** expresses independent governed assurance, not legal representation. Neither role implies the other's mandate or authority.

An explicit human architectural decision is required to disambiguate their canonical names and references without silently renaming the established Role. Until that decision, references in this derivation identify the governed-assurance definition by its dedicated document anchor and responsibility context. An unqualified `-as-Guardian` reference is ambiguous; a context qualifier does not establish a new canonical Role identity. No Role alias, identifier namespace, grammar extension or migration is invented to resolve the collision. The two meanings remain explicit under AX-14 and the identification decision remains unresolved under AX-17.
