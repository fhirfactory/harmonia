<!-- continuum-source-succession -->
> **RAD-1.1.5 source succession:** Partial section succession; unwrapped scope/governance and unresolved material retain their recorded roles. See the [canonical continuum](../../../../enterprise-architecture/continuum/documentation/published/1.1.5/README.md) and [section correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json). Original source wording and standing below are preserved.

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
│   ├── Service Assurance Modeller
│   └── Service Guardian
└── Information / Service Participation Roles
    ├── Service Provider
    ├── Information Supplier
    └── Information Client
```

---

### 2.1 Care Participant Roles

Care Participant Roles represent individuals or subjects receiving healthcare, health monitoring, or preventive services.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Client](../../../../enterprise-architecture/continuum/business/business-role/3b90ebcc858e4b0685c7950ceeb8f3fd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Client**: A person or party who enters into a service relationship with a healthcare organisation or practitioner.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  <!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Patient](../../../../enterprise-architecture/continuum/business/business-role/0d4ad18a7472447ebcd30619b37dce39.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Patient**: A Client actively receiving clinical assessment, diagnosis, medical treatment, nursing care, or direct clinical management.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Subject of Care](../../../../enterprise-architecture/continuum/business/business-role/ada564bbff80416aa661a1d2cde705f4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Subject of Care**: The identifiable individual whose health, clinical observations, biological samples, or clinical documents form the subject matter of an encounter or health record (often synonymous with Patient, but distinct when specimens, genomic data, or population screening cohorts are processed).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


---

### 2.2 Care Delivery Roles

Care Delivery Roles represent healthcare professionals, clinical teams, and service agents directly involved in clinical decision-making, order placement, or service execution.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Practitioner](../../../../enterprise-architecture/continuum/business/business-role/0b5544e3d9d741b39607a5e61d79049f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Practitioner**: An individual registered, certified, or qualified to provide healthcare services.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  <!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinician](../../../../enterprise-architecture/continuum/business/business-role/94fbd35dadc543dd8c5efaca1951e005.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Clinician**: A Practitioner actively engaged in direct clinical assessment, diagnosis, treatment, or medical decision-making.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Care Coordinator](../../../../enterprise-architecture/continuum/business/business-role/84cf0b0868b94a1ea5ce5791fcfa66f9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Care Coordinator**: A practitioner or case manager responsible for planning, synchronising, and monitoring cross-specialty or cross-setting care delivery for a patient.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Referrer](../../../../enterprise-architecture/continuum/business/business-role/197b811fc8594cc3a68a372b74008ba0.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Referrer**: A practitioner or service agent who formally requests another service provider to assess, manage, or assume partial/total clinical responsibility for a patient.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Requester](../../../../enterprise-architecture/continuum/business/business-role/dd262c6bed9a44829e7a4f6796c13b3b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Requester**: A practitioner or clinical agent who formally places an order for a diagnostic investigation, medication supply, or procedural intervention.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Prescriber](../../../../enterprise-architecture/continuum/business/business-role/4a6e12d4f48d426a98136ea50ad91ef9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Prescriber**: A practitioner legally authorised and clinically responsible for authorising the dispensing and administration of scheduled medications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Performer](../../../../enterprise-architecture/continuum/business/business-role/c87e99ff36224b9da5a05bbd3002da34.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Performer**: A clinician, proceduralist, technician, or care agent who executes a diagnostic test, surgical procedure, clinical observation, or therapeutic intervention.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


---

### 2.3 Care Support Roles

Care Support Roles represent informal carers, personal supporters, patient advocates, and legally constituted representatives who support the patient or exercise surrogate decision-making authority.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Carer](../../../../enterprise-architecture/continuum/business/business-role/3c2784f8f3404e978c618311148e3422.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Carer**: An individual who provides unpaid, ongoing personal care, support, or assistance to a patient.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Support Person](../../../../enterprise-architecture/continuum/business/business-role/c2334e0e5c3b40789c672471ea82c465.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Support Person**: An individual nominated by the patient to accompany them, provide moral/emotional support, or assist in communication during care episodes.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Advocate](../../../../enterprise-architecture/continuum/business/business-role/8845af697832409b90c51a65d7b06669.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Advocate**: An individual or agency acting on behalf of the patient to promote their rights, preferences, and interests within the healthcare system.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Representative](../../../../enterprise-architecture/continuum/business/business-role/ca4ae4c178eb488d98118db7773b2127.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Representative**: An individual or legal entity empowered to act or make decisions on behalf of the patient.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  <!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Guardian](../../../../enterprise-architecture/continuum/business/business-role/81942689c7b74960a23ce0d6da94859c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Guardian**: A legally appointed guardian exercising general legal custody and personal welfare decision-making.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  <!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Substitute Decision-Maker](../../../../enterprise-architecture/continuum/business/business-role/c30a4e1e36b44fd3a953395740d946ef.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Substitute Decision-Maker**: An individual legally authorised to make specific medical treatment decisions when the patient lacks decision-making capacity.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  <!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Delegate / Proxy](../../../../enterprise-architecture/continuum/business/business-role/da5d23f9d93a49f8ad11232c12a23e3f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Delegate / Proxy**: An individual nominated by the patient or authorised by law to access health information, receive communications, or perform specific administrative acts.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


---

### 2.4 Operational Roles

Operational Roles represent personnel and logistics agents responsible for managing healthcare facilities, inpatient wards, theatre suites, bed capacity, work allocation, and physical patient movement.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Coordinator](../../../../enterprise-architecture/continuum/business/business-role/0dbbce90dc3e48b190890c832f52cb6f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Service Coordinator**: An operational administrator who manages service schedules, clinic queues, and service capacity.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Ward Coordinator](../../../../enterprise-architecture/continuum/business/business-role/9b4ea5f137c9474db1c9dc5ac57a1207.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Ward Coordinator**: A senior nurse or ward administrator responsible for shift-level operational oversight, staffing balance, and bed usage within a clinical unit.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Patient Flow Coordinator](../../../../enterprise-architecture/continuum/business/business-role/e8f5b7caaee84a67afda4844b1580cee.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Patient Flow Coordinator**: A facility-wide coordinator managing admissions, emergency department egress, transfers, and bed allocations across hospital directorates.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Bed Manager](../../../../enterprise-architecture/continuum/business/business-role/4f44e0f4c2ae4774a652c44874449487.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Bed Manager**: An operational agent who tracks physical bed availability, assigns care-places, and coordinates bed turnover logistics.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Work Coordinator](../../../../enterprise-architecture/continuum/business/business-role/5c52eb31e8ba45a7a4eea4fa8d66d0c5.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Work Coordinator**: An operational manager or dispatch supervisor who creates, assigns, reallocates, and tracks operational work tasks (e.g., portering, cleaning, specimen transport).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Wardsperson](../../../../enterprise-architecture/continuum/business/business-role/ebc0fbc6ad7645439a32e1f1a7944209.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Wardsperson**: An operational staff member (orderly/porter) responsible for the physical transport of patients, equipment, specimens, and clinical supplies.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


---

### 2.5 Governance / Authority Roles

Governance and Authority Roles represent statutory regulators, policy bodies, enterprise data stewards, Service Assurance modelling and independent governed-assurance responsibility. Membership of this family does not make governance authority, assurance modelling, stewardship and Guardianship the same responsibility or grant either assurance Role authority to approve governing requirements, Assurance Definitions or Assurance Criteria.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Regulator](../../../../enterprise-architecture/continuum/business/business-role/1160219638ef42b292a5612e5ac7c539.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Regulator**: A statutory authority responsible for professional registration, healthcare standards enforcement, and accreditation (e.g., AHPRA).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Policy Authority](../../../../enterprise-architecture/continuum/business/business-role/e0ff9ce2cf4448568d1caf7a44023f48.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Policy Authority**: An entity responsible for defining enterprise or jurisdictional clinical, security, and operational policies.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [System Steward](../../../../enterprise-architecture/continuum/business/business-role/27d2797d2f9e4a9f9131300467d212bc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **System Steward**: An organisational authority responsible for the governance, availability, and lifecycle integrity of digital health systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Steward](../../../../enterprise-architecture/continuum/business/business-role/4110a988149e4049844d4b683ff5d8d4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Steward**: An authority responsible for defining the governance policies, classification rules, retention periods, and privacy standards for health information assets.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Custodian](../../../../enterprise-architecture/continuum/business/business-role/603a0fada51746ef9d26dd6fe20b2e78.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Custodian**: An entity or designated role holding operational custody and legal responsibility for safeguarding, preserving, and managing access to health records.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Terminology Steward](../../../../enterprise-architecture/continuum/business/business-role/c3cdd7a91a584942abac167af15f642c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Terminology Steward**: An authority responsible for authoring, validating, and governing canonical value sets, clinical ontologies, and semantic mapping tables.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


<a id="service-assurance-modeller"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Assurance Modeller](../../../../enterprise-architecture/continuum/business/business-role/962735ea17a840e5a772f813d3ad7d45.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

#### Service Assurance Modeller

- **Canonical Name**: `Service Assurance Modeller`. **Element Type**: Business Role. Canonical ID remains unresolved; the metamodel does not establish Role identifier tokens or structural context.
- **Fundamental Responsibility**: Determine and model how satisfaction of a governed requirement, constraint or expected behaviour is to be assured.
- **Responsibility Contexts**: [Assurance Design](../../02-strategy/capabilities/business-enabling-capabilities.md#assurance-design) and [Assurance Criteria Management](../../02-strategy/capabilities/business-enabling-capabilities.md#assurance-criteria-management).
- **Performed Business Functions**: [Design Service Assurance](../behaviours/health-service-assurance.md#design-service-assurance), owned by Assurance Design; [Manage Assurance Criteria](../behaviours/health-service-assurance.md#manage-assurance-criteria), owned by Assurance Criteria Management. Role performance does not replace capability ownership.
- **Principal Process Performance**: [Assurance Process Design](../processes/business-processes.md#assurance-process-design), owned by Assurance Design, with both established Functions participating without transferring Assurance Criteria Management ownership. Assurance Definition remains a conceptual output/boundary; its use by Service Guardian does not establish a Modeller-to-Guardian Interaction.
- **Authority Boundary**: Modelling does not inherently confer authority to approve the governing requirement, Assurance Definition or Assurance Criteria. Governance Authority is exercised through existing mandated authority Roles as described in [§3.7](#37-governance-authority-and-assurance-modelling).
- **Clinical and Operational Boundary**: The Role confers no clinical authority, clinical correctness or adequacy determination, professional clinical judgement, peer-review or clinical-governance authority, or Clinical Services Delivery Assurance. It does not manage, perform, remediate, assign, delegate or operationally escalate the subject activity merely because it models assurance concerning that activity. Explicitly required assurance concerning clinical information or behaviour does not transfer clinical responsibility.
- **Allocation Boundary**: The Role models how satisfaction is assured; it does not perform the resulting independent assurance merely by fulfilling this Role. Detailed Actor eligibility, mandates, approval workflows, further Interactions and Collaborations remain unresolved. No application, runtime or implementation allocation is established.

<a id="guardian-governed-assurance"></a>

------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


<a id="service-guardian"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Guardian](../../../../enterprise-architecture/continuum/business/business-role/3ed945eeff23404d8dae43382ea746d1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

#### Service Guardian

- **Canonical Name**: `Service Guardian`. **Element Type**: Business Role. Canonical ID remains unresolved. The previous assurance-role name collision is [resolved in §3.6](#36-assurance-role-naming--resolved); the care-support Guardian under Representative is unchanged.
- **Definition**: A Business Role responsible for independently evaluating governed activity, information, state or outcomes against applicable assurance criteria and establishing assurance findings and conclusions from sufficient trustworthy evidence.
- **Derivation**: [REQ-FND-005](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity), [BC-18 Health Service Assurance](../../02-strategy/capabilities/business-capabilities.md#18-health-service-assurance) and [Governed Assurance](../../02-strategy/capabilities/business-enabling-capabilities.md#governed-assurance) establish the independent evaluation/conclusion responsibility documented in the [bounded Domain03 assurance derivation](../behaviours/health-service-assurance.md). The shared name is intentional: **EC-14 Service Guardian is an Enterprise Capability; Service Guardian here is a Business Role.** Neither is a software component.
- **Performed Business Functions**: [Establish Assurance Context](../behaviours/health-service-assurance.md#establish-assurance-context), [Assess Assurance Evidence](../behaviours/health-service-assurance.md#assess-assurance-evidence) and [Adjudicate Assurance Assessment](../behaviours/health-service-assurance.md#adjudicate-assurance-assessment), all owned by Governed Assurance. Context establishment includes evidence assembly; adjudication includes establishment of findings/conclusions. No separate evidence-assembly, conclusion-establishment or reporting Function is added.
- **Approved Process / Interaction Participation**: Principal performing Role for [Assurance Process Execution](../processes/business-processes.md#assurance-process-execution); communicates established outcomes through [Assurance Process Reporting / Communication](../processes/business-processes.md#assurance-process-reporting-communication). Participates in [Service Assurance Request, Status and Outcome Communication](../collaborations-interactions/interactions.md#5-approved-service-assurance-interactions) with the approved requesting/status/recipient Roles. This adds no Function or Collaboration and does not make status an assurance conclusion.
- **Management Boundary**: Service Guardian may manage progression of its own assurance activity but does not thereby manage, perform, remediate, assign, delegate or operationally escalate the subject being assured. Findings may cause another responsible party to initiate operational response; responsibility for that response remains with its applicable operational capability and authority.
- **Clinical Boundary**: Service Guardian confers no clinical authority and does not establish clinical adequacy, clinical correctness or professional clinical judgement, replace clinical peer review or clinical governance, or assume Clinical Services Delivery Assurance. Clinical review and clinical assurance remain with their applicable clinical processes and authorities. Information and behaviour concerning clinical activity may be subject to Guardianship where an explicit applicable governed requirement establishes that concern; this does not transfer clinical responsibility.
- **Conclusion Boundary**: Evidence assessment remains distinct from adjudication. Adjudication determines the finding or conclusion from assessed evidence and applicable criteria. Where evidence is insufficient to conclude with the required confidence, insufficiency remains explicit and implies neither satisfaction nor non-satisfaction. This is distinct from execution failure or uncertainty about whether assurance activity occurred.
- **Allocation Boundary**: The Role performs independent assurance within Governed Assurance. It does not inherently approve governing requirements, Assurance Definitions or Assurance Criteria, or acquire modelling, clinical review or operational-response responsibility. Detailed Actor eligibility/mandates beyond the approved Role relationships, further Interactions and Collaborations remain unresolved. It has no application, runtime or implementation allocation.

---



------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

### 2.6 Information / Service Participation Roles

Information and Service Participation Roles represent general participant capacities in service provision and information exchange.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Provider](../../../../enterprise-architecture/continuum/business/business-role/7e85b6b2c65a4e038e11711a4c7f7c60.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Service Provider**: An organisation, facility, or practitioner acting in the capacity of delivering a defined healthcare or diagnostic service.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Supplier](../../../../enterprise-architecture/continuum/business/business-role/545713793b154ed189f4229256933053.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Supplier**: A participant (person, organisation, or system) that routinely produces, transmits, or submits healthcare information, diagnostic results, or clinical documents into the integration environment.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Client](../../../../enterprise-architecture/continuum/business/business-role/3ac6ca6e8c9a42b8a3b48e9631073235.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Client**: A participant (person, organisation, or system) that queries, retrieves, or consumes healthcare information for clinical review or operational support.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


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

Existing Clinician, Practitioner, Care Coordinator, Policy Authority, steward and operational Role responsibilities remain distinct. Neither Service Assurance Modeller nor Service Guardian acquires clinical review or clinical assurance responsibility through lexical similarity, and no generic clinical-assurance authority Role is created. A Role does not itself confer information-access authority.

<a id="36-canonical-role-name-collision--unresolved"></a>

### 3.6 Assurance Role Naming — Resolved

The approved human review decision on 2026-10-08 names the governed-assurance Business Role **Service Guardian**. **Guardian** under **Care Support → Representative** retains its established legal custody and personal-welfare definition unchanged. The naming collision is **resolved**; neither Role implies the other's mandate or authority. EC-14 retains the intentional shared name Service Guardian as a distinct Enterprise Capability.

Qualified references use the existing grammar and identify **Service Guardian** in the Role position for governed assurance; **Guardian** identifies the legal/personal-welfare Role. No actor-specific assurance reference or compact Role token is allocated here because detailed Actor eligibility and such token allocation are unestablished. The earlier assurance wording Guardian is not allocated as an alternative canonical name or Role alias. The old document anchors are retained solely to resolve historical navigation, not as Role identifiers or aliases. Canonical IDs remain unresolved without a namespace or grammar change.

### 3.7 Governance Authority and Assurance Modelling

**Governance Authority establishes what is authoritative. Service Assurance Modeller models how satisfaction will be assured. Service Guardian independently establishes what the applicable evidence demonstrates.**

Governance Authority describes the authority exercised through existing mandated Roles, not a new Business Role. **Policy Authority** already represents enterprise or jurisdictional policy authority; **Regulator** represents statutory authority. Existing steward Roles retain their established subject-specific governance mandates. This reference reuses those responsibilities without expanding their mandates or assigning an approval workflow for Assurance Definitions or Assurance Criteria.

Modelling or managing a criterion does not transfer authority for the requirement from which it derives. Approval authority for a governing requirement, Assurance Definition or Assurance Criteria must be established by the applicable governance mandate; neither assurance Role possesses it merely through modelling or assurance. Exact approval allocation and workflows remain unresolved. See the [responsibility model](../behaviours/health-service-assurance.md#2-governance-management-guardianship-and-clinical-authority).

### 3.8 Approved Assurance Consumer and Participant Relationships

**Care Coordinator, Service Coordinator, Regulator, Policy Authority and System Steward** may request Service Assurance and receive applicable established outcomes within their respective authority and information requirements. **Only System Steward** consumes Request Service Assurance Status. The [Service/consumer concern matrix](../behaviours/health-service-assurance.md#54-approved-consumer-and-recipient-concerns) and [three approved Interactions](../collaborations-interactions/interactions.md#5-approved-service-assurance-interactions) establish these relationships without changing existing Role definitions or exhaustively defining their mandates. Requester, status consumer and outcome recipient need not coincide; outcome communication is not mandatory broadcast.

Requesting does not confer control of assessment/adjudication or authority to alter Assurance Definition/Criteria. Care Coordinator retains clinical interpretation, judgement and management responsibility; Service Coordinator retains operational management and applicable improvement responsibility; Regulator and Policy Authority retain governance/compliance responsibility; System Steward's status observation is stewardship / management monitoring of assurance progression, not assurance. Service Guardian assumes none of those recipients' subsequent actions.

No Financial Governance Role is established: [financial governance is deliberately outside Harmonia scope](../behaviours/health-service-assurance.md#financial-governance-exclusion), with exact authorised external recipients still unresolved. [Governed Assurance does not recursively assure its own execution](../behaviours/health-service-assurance.md#assurance-non-recursion-boundary); established activity execution integrity is not another Service Guardian responsibility or conclusion. No new Actor eligibility, Collaboration membership or implementation allocation is inferred.
