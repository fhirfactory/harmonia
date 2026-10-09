# Canonical Business Enabling Capability Model & Features

## Overview & Architecture

The Business Enabling Capability Tier defines **what systems and information infrastructure must enable or provide in support of the healthcare enterprise**.

Where Business Capabilities define the high-level healthcare landscape (what the enterprise does), Business Enabling Capabilities specify the functional abilities systems must deliver to make healthcare delivery, administration, and operations possible.

### Architectural Principles Governing this Model
1. **Authentic Healthcare Operating Contexts**: Capabilities are grouped into five authentic healthcare contextual views. They reflect genuine healthcare operating environments (e.g., Ward Operations, Order Administration, Patient Clinical Record), **not** low-level integration engine plumbing or middleware mechanics.
2. **Independence from Commercial Product Boundaries**: Capabilities are defined independently of commercial software product boundaries (such as PAS, EMR, LIS, RIS, HRMS, or FMIS). A commercial product may implement multiple capabilities, drawing "dotted lines" around parts of the model; the capability model remains orthogonal to vendor packaging.
3. **Atomic Feature Decomposition**: Within established Harmonia-Relevant and Harmonia-Core Capability contexts, system behaviour is decomposed into atomic **Features**—the smallest useful, testable, technology-neutral statements of system-enabled behaviour. Reference and Adjacent capabilities are intentionally not decomposed into features.
4. **Technology Neutrality**: No capability or feature is defined in terms of specific software products (HAPI FHIR, Infinispan, PostgreSQL, ActiveMQ Artemis, Camel, Netty, Vue, Spring) or runtime plumbing (threads, queues, ports, DDL).
5. **Architectural Scope Sufficiency**: The Business Enabling Feature catalogue provides the established R1.x/R2.x Feature baseline for the capabilities and contextual views presently decomposed. Completeness SHALL NOT be interpreted as requiring artificial Feature decomposition of capabilities for which no Feature-level decomposition has been architecturally established. Roadmap items (such as authoritative master-patient EMPI reconciliation) are explicitly demarcated as uncommitted Harmonia 3.x candidates.

Under [approved G1 K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing), the five views, catalogue numbering, indentation and decomposition presentation do not establish Architectural Element identity, CT1 / CT2 / CT3 assignment, ancestry or root status. The 137 established Features retain their FT type, names and identifiers. Affected complete Capability ancestry and structural Canonical IDs remain unresolved. Derivation may proceed from established ownership and responsibility without inventing intermediate elements.

The approved [Health Service Assurance derivation](../capability-maps/health-service-assurance-derivation.md) additionally establishes **Assurance Design**, **Assurance Criteria Management** and **Governed Assurance** as Business Enabling Capabilities supporting BC-18. Their definitions and assurance principles are recorded [below](#health-service-assurance-approved-business-enabling-capabilities). Their contextual-view placement, relevance classification, Capability Tier, complete ancestry and structural Canonical IDs remain unresolved; they are not assigned through document position or lexical similarity. No additional Feature catalogue is derived.

---

## The Five Contextual Views

The Business Enabling capabilities are structured across five contextual views:

```text
┌────────────────────────────────────────────────────────────────────────┐
│ 1. ENTITY MANAGEMENT                                                   │
│    Governance and administration of healthcare entities throughout      │
│    their lifecycle: Clients, Providers, Organisations, Locations,       │
│    Services, Products, and Devices.                                    │
├────────────────────────────────────────────────────────────────────────┤
│ 2. SERVICE ADMINISTRATION                                              │
│    Administrative, logistical, and financial progression of care:      │
│    Client Intake, Referrals, Encounters, Orders, Diagnostics,          │
│    Care Plans, Clinical Messaging, and Billing Extraction.             │
├────────────────────────────────────────────────────────────────────────┤
│ 3. SERVICE DELIVERY                                                    │
│    Systems enablement across clinical delivery settings: Primary Care, │
│    Acute Inpatient, Emergency, Surgical, Diagnostic, Medication,       │
│    Allied Health, Community, and Remote/Virtual Care.                  │
├────────────────────────────────────────────────────────────────────────┤
│ 4. HEALTH SERVICE OPERATIONS                                           │
│    Facility logistics, bed and care-place management, capacity,        │
│    clinical resource coordination, work allocation and dispatch,       │
│    patient transport, and discharge progression.                       │
├────────────────────────────────────────────────────────────────────────┤
│ 5. INTRINSIC / SHARED ENABLEMENT                                       │
│    Reusable system capabilities: Longitudinal Clinical Record,         │
│    Health Information Exchange, Information Access, Policy & Security  │
│    Control, Collaboration, Workflow Coordination, and Presentation.    │
└────────────────────────────────────────────────────────────────────────┘
```

---

## Architectural Scope Statement (Harmonia 1.x/2.x vs. 3.x Roadmap)

> The Business Enabling Feature catalogue provides the established R1.x/R2.x Feature baseline for the capabilities and contextual views presently decomposed. Completeness SHALL NOT be interpreted as requiring artificial Feature decomposition of capabilities for which no Feature-level decomposition has been architecturally established.

- **Established Coverage**: The model records established capability responsibilities and the 137-Feature baseline. BC-17 enablement and the assurance capabilities' unestablished contextual-view, tier, ancestry and Feature relationships remain explicit gaps; structural symmetry is not a completion requirement.
- **Sufficient Does Not Mean Frozen Forever**: Additional capabilities and features may be introduced where future Harmonia scope requires them.
- **Roadmap Demarcation — Patient Identity**:
  - **Harmonia 1.x/2.x Scope**: Includes cross-system identifier resolution, identifier correlation, identity alias association, identity provenance, identity federation across regional nodes, and governed administrative correction.
  - **Harmonia 3.x Roadmap Candidate**: Authoritative master-patient identity reconciliation, probabilistic matching algorithms, and Enterprise Master Person Index (EMPI) merge/unmerge engines remain an uncommitted Harmonia 3.x candidate. Harmonia 1.x/2.x relies on authoritative upstream PAS/MPI feeds rather than acting as the master clinical EMPI authority.

---

# View 1: Entity Management

Entity Management encompasses systems enablement for identifying, governing, and managing core healthcare entities throughout their operational lifecycles.

```text
1. Client Administration
   ├── 1.1 Person Identity [Harmonia-Core]
   ├── 1.2 Healthcare Subject [Harmonia-Core]
   ├── 1.3 Client Relationship [Harmonia-Relevant]
   └── 1.4 Client Privacy [Harmonia-Core]
2. Provider Administration
   ├── 2.1 Practitioner [Harmonia-Core]
   ├── 2.2 Practitioner Role [Harmonia-Core]
   ├── 2.3 Provider Relationship [Harmonia-Relevant]
   └── 2.4 Provider Engagement [Harmonia-Relevant]
3. Organisation Administration
   ├── 3.1 Healthcare Organisation [Harmonia-Core]
   ├── 3.2 Organisational Structure [Harmonia-Core]
   └── 3.3 Organisation Contact [Harmonia-Relevant]
4. Location Administration
   ├── 4.1 Healthcare Location [Harmonia-Core]
   ├── 4.2 Location Structure [Harmonia-Core]
   └── 4.3 Care-Place Definition [Harmonia-Core]
5. Health Service Administration
   ├── 5.1 Healthcare Service Definition [Harmonia-Core]
   ├── 5.2 Service Provision Relationship [Harmonia-Core]
   ├── 5.3 Service Availability Definition [Harmonia-Core]
   └── 5.4 Service Eligibility Definition [Harmonia-Relevant]
6. Health Product Administration
   ├── 6.1 Health Product Definition [Adjacent]
   ├── 6.2 Product Formulary & Catalogue [Adjacent]
   └── 6.3 Product Batch & Serial Tracking [Reference]
7. Clinical Device Administration
   ├── 7.1 Device Definition [Harmonia-Relevant]
   ├── 7.2 Device Instance [Harmonia-Relevant]
   ├── 7.3 Device Association [Harmonia-Relevant]
   └── 7.4 Device Connectivity [Harmonia-Relevant]
```

### Detailed Capability & Feature Catalogue: Entity Management

#### 1. Client Administration
Governs the representation, identification, and demographic context of individuals receiving care.

- **1.1 Person Identity** (`Harmonia-Core`)
  - *Definition*: Resolving, maintaining, and correlating identities and identifiers assigned to an individual across different authorities.
  - `FEAT-EM-01`: **Identifier Resolution**: Ingest and resolve external identifiers against registered issuing authorities and namespace systems.
  - `FEAT-EM-02`: **Cross-Authority Identifier Correlation**: Correlate disparate local identifiers (e.g., hospital MRN, clinic client ID, national IHI) belonging to the same individual.
  - `FEAT-EM-03`: **Identity Alias Association**: Maintain explicit associations between active identifiers and historical or demographic aliases without destructive overwriting.
  - `FEAT-EM-04`: **Governed Identity Correction**: Apply administrative identity updates, linked-record corrections, and split/merge notifications propagated from authoritative sources.

- **1.2 Healthcare Subject** (`Harmonia-Core`)
  - *Definition*: Managing the core healthcare subject context bound to clinical records and operational activities.
  - `FEAT-EM-05`: **Subject Context Binding**: Bind authenticated person identity to clinical encounters, orders, and observations as the enduring subject of care with verifiable association.
  - `FEAT-EM-06`: **Demographic Context Governance**: Manage essential demographic attributes (date of birth, sex, vital status) with full provenance and timestamping.

- **1.3 Client Relationship** (`Harmonia-Relevant`)
  - *Definition*: Representing relationships between the client and carers, guardians, or contacts.
  - `FEAT-EM-07`: **Next-of-Kin & Guardian Association**: Associate nominated emergency contacts, legal guardians, and care agents with the client record.
  - `FEAT-EM-08`: **Relationship Validity Tracking**: Maintain effective date intervals and authority scopes for nominated client relationships.

- **1.4 Client Privacy** (`Harmonia-Core`)
  - *Definition*: Enforcing client-level privacy preferences, consent directives, and confidentiality markers.
  - `FEAT-EM-09`: **Consent Directive Evaluation**: Evaluate client consent directives against requested information disclosure and exchange actions.
  - `FEAT-EM-10`: **Confidentiality Flag Enforcement**: Enforce restricted access flags (e.g., VIP, sensitive diagnosis, restricted record) across query and presentation interfaces.

#### 2. Provider Administration
Governs healthcare practitioners, their professional qualifications, operational roles, and organisational engagements.

- **2.1 Practitioner** (`Harmonia-Core`)
  - *Definition*: Identifying and verifying individual healthcare professionals.
  - `FEAT-EM-11`: **National Practitioner Verification**: Ingest, validate, and verify national practitioner identifiers (e.g., HPI-I) and professional registration status.
  - `FEAT-EM-12`: **Practitioner Profile Governance**: Maintain core professional demographic details, qualifications, and active practice status.

- **2.2 Practitioner Role** (`Harmonia-Core`)
  - *Definition*: Modeling the formal role, specialty, and operational capacity of a practitioner at a given organisation or location.
  - `FEAT-EM-13`: **Role & Specialty Modeling**: Define and associate clinical specialties, practice roles, and service capacities for a practitioner.
  - `FEAT-EM-14`: **Electronic Endpoint Binding**: Associate validated communication endpoints (secure messaging addresses, direct routing queues) with specific practitioner roles.

- **2.3 Provider Relationship** (`Harmonia-Relevant`)
  - *Definition*: Managing organizational affiliations and multidisciplinary team relationships.
  - `FEAT-EM-15`: **Practitioner-Organisation Affiliation**: Maintain temporal affiliations between practitioners and healthcare organizations.

- **2.4 Provider Engagement** (`Harmonia-Relevant`)
  - *Definition*: Capturing terms of service and clinical privilege agreements.
  - `FEAT-EM-16`: **Clinical Privileging Record**: Track active clinical admitting and practice privileges within healthcare facilities.

#### 3. Organisation Administration
Governs healthcare delivery organizations, facilities, and network structures.

- **3.1 Healthcare Organisation** (`Harmonia-Core`)
  - *Definition*: Authoritative identification and profiling of legal and operational healthcare entities.
  - `FEAT-EM-17`: **National Organisation Verification**: Validate national organization identifiers (e.g., HPI-O) and legal entity classifications.
  - `FEAT-EM-18`: **Organisation Profile Management**: Govern directory profiles, operational status, and published service descriptions.

- **3.2 Organisational Structure** (`Harmonia-Core`)
  - *Definition*: Managing parent-child hierarchies, departments, and corporate relationships.
  - `FEAT-EM-19`: **Hierarchical Structure Navigation**: Model, traverse, and resolve nested departmental, divisional, and regional healthcare networks.

- **3.3 Organisation Contact** (`Harmonia-Relevant`)
  - *Definition*: Maintaining verified administrative and clinical communication channels.
  - `FEAT-EM-20`: **Directory Contact Management**: Manage verified phone, email, and administrative contact channels for healthcare facilities.

#### 4. Location Administration
Governs the physical spaces, facilities, and care environments where healthcare is delivered.

- **4.1 Healthcare Location** (`Harmonia-Core`)
  - *Definition*: Identifying and profiling geographic locations, physical campuses, and health facilities.
  - `FEAT-EM-21`: **Location Geospatial & Address Resolution**: Validate physical addresses, geospatial coordinates, and location operational status.

- **4.2 Location Structure** (`Harmonia-Core`)
  - *Definition*: Modeling the physical architecture of healthcare campuses.
  - `FEAT-EM-22`: **Physical Hierarchy Modeling**: Represent campus, building, wing, floor, and ward architectural relationships.

- **4.3 Care-Place Definition** (`Harmonia-Core`)
  - *Definition*: Modeling the atomic care delivery places (beds, bays, treatment chairs, surgical tables).
  - `FEAT-EM-23`: **Atomic Care-Place Modeling**: Define specific beds, care-bays, and examination rooms with their physical capabilities (e.g., telemetry-capable, negative-pressure).

#### 5. Health Service Administration
Governs the healthcare services offered by providers and organizations.

- **5.1 Healthcare Service Definition** (`Harmonia-Core`)
  - *Definition*: Specifying clinical and administrative health services offered to the public or clinical referrers.
  - `FEAT-EM-24`: **Service Catalogue Publishing**: Publish structured healthcare service definitions, clinical specialties, and intake criteria to regional directories.

- **5.2 Service Provision Relationship** (`Harmonia-Core`)
  - *Definition*: Binding health services to specific organizations, locations, and practitioner roles.
  - `FEAT-EM-25`: **Service-Location-Provider Binding**: Correlate which organization provides a named health service at which physical location with which practitioner roles.

- **5.3 Service Availability Definition** (`Harmonia-Core`)
  - *Definition*: Modeling regular operating hours, exceptional closures, and emergency availability.
  - `FEAT-EM-26`: **Operating Schedule Specification**: Define temporal availability, clinic operating hours, and on-call service windows.

- **5.4 Service Eligibility Definition** (`Harmonia-Relevant`)
  - *Definition*: Capturing referral criteria, catchment boundaries, and clinical eligibility.
  - `FEAT-EM-27`: **Eligibility & Catchment Specification**: Define clinical referral thresholds and geographic catchment boundaries.

#### 6. Health Product Administration
Governs medications, blood products, implants, and consumable health goods.
- **6.1 Health Product Definition** (`Adjacent`): Classification of pharmaceutical and consumable products.
- **6.2 Product Formulary & Catalogue** (`Adjacent`): Management of approved facility medication formularies.
- **6.3 Product Batch & Serial Tracking** (`Reference`): Supply chain batch and lot management.
*(No features decomposed for Reference/Adjacent capabilities).*

#### 7. Clinical Device Administration
Governs medical equipment, patient monitors, diagnostic instruments, and telehealth devices.

- **7.1 Device Definition** (`Harmonia-Relevant`)
  - *Definition*: Specifying medical equipment types, models, and regulatory classifications.
  - `FEAT-EM-28`: **Device Type Specification**: Catalog medical device models, regulatory approvals, and capabilities.

- **7.2 Device Instance** (`Harmonia-Relevant`)
  - *Definition*: Tracking physical device hardware instances and serial numbers.
  - `FEAT-EM-29`: **Device Serial Tracking**: Ingest and maintain unique device identifier (UDI) records and serial numbers.

- **7.3 Device Association** (`Harmonia-Relevant`)
  - *Definition*: Associating clinical devices with patients, beds, or rooms.
  - `FEAT-EM-30`: **Device-Patient/Location Assignment**: Track dynamic association between a telemetry monitor, patient, and physical bed.

- **7.4 Device Connectivity** (`Harmonia-Relevant`)
  - *Definition*: Registering communication protocols and network endpoints for medical devices.
  - `FEAT-EM-31`: **Device Endpoint Registration**: Register communication interfaces and security credentials for diagnostic telemetry feeds.

---

# View 2: Service Administration

Service Administration encompasses systems enablement for the operational, administrative, logistical, and financial progression of care.

```text
1. Client-Centred Administration [Harmonia-Relevant]
2. Referral Administration [Harmonia-Relevant]
3. Scheduling Administration [Harmonia-Relevant]
4. Episode & Encounter Administration [Harmonia-Core]
5. Order Administration [Harmonia-Core]
6. Diagnostic Administration [Harmonia-Core]
7. Medication Administration [Harmonia-Relevant]
8. Procedure Administration [Harmonia-Relevant]
9. Clinical Record Administration [Harmonia-Core]
10. Care Plan Administration [Harmonia-Relevant]
11. Clinical Communication Administration [Harmonia-Core]
12. Funding & Coverage Administration [Adjacent]
13. Clinical Charging & Claims Administration [Adjacent]
```

### Critical Domain Distinctions: Service Administration

#### Distinction: Referral vs. Order
- **Referral Administration**: A **Referral** formally requests another care participant or service provider to assess, manage or assume some degree of clinical care responsibility. Referral intent, disposition, responsibility actually assumed and Transfer of Care are distinct. Referral may lead to shared or assumed responsibility or transfer of ongoing care, but does not itself establish acceptance, delivery, assumption or Transfer of Care, or universally require an Appointment, Encounter or Order.
- **Order Administration**: An **Order** requests a defined, bounded **fulfilment activity** (e.g., execute a diagnostic test, dispense a medication, perform a procedure) and strictly supports a **closed-loop progression** returning an outcome to the requester.

#### Distinction: Technical ACK vs. Business ACK
- **Technical Delivery Acknowledgement**: A technical acknowledgement establishes only the acceptance state defined by the applicable interaction contract. It SHALL NOT be interpreted as durable acceptance, committed business state, successful processing or successful business disposition unless that meaning is explicitly established by the governing contract. HTTP 200 does not universally establish successful business disposition, and HTTP 202 does not universally establish durable queue acceptance; either may have stronger meaning under an explicit contract.
- **Business Acknowledgement**: A clinical/operational message generated by the receiving application or clinical staff confirming that the order or referral has been reviewed, accepted for execution, scheduled, or rejected.
$$\text{\bf Technical Delivery Acknowledgement} \neq \text{\bf Business Acknowledgement}$$

Acknowledgement of a handoff is not, by itself, evidence of eventual downstream business outcome. [REQ-FND-001](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-001-durable-ingress-acceptance-boundary) continues to require the applicable durable acceptance boundary before Harmonia positively acknowledges ingress acceptance.

Business acknowledgement is contextual and may communicate acceptance, rejection or another explicit disposition; it does not establish a universal reviewed → accepted → scheduled progression or transfer originating authority.

#### Order Closed-Loop Progression Semantics
All order-related capabilities must support the end-to-end closed loop:
$$\text{Destination} \longrightarrow \text{Route} \longrightarrow \text{Deliver} \longrightarrow \text{Acknowledge} \longrightarrow \text{Progress} \longrightarrow \text{Complete} \longrightarrow \text{Outcome}$$

---

### Detailed Capability & Feature Catalogue: Service Administration

#### 1. Client-Centred Administration (`Harmonia-Relevant`)
- **1.1 Client Intake & Demographic Update**
  - `FEAT-SA-01`: **Demographic Update Ingestion**: Ingest and validate patient admission and registration updates from external PAS systems.
  - `FEAT-SA-02`: **Demographic Change Distribution**: Fan out verified client demographic updates to downstream clinical subsystems.

#### 2. Referral Administration (`Harmonia-Relevant`)
- **2.1 Referral Intake & Progression**
  - `FEAT-SA-03`: **Referral Document Ingestion**: Ingest structured clinical referral messages across integration boundaries.
  - `FEAT-SA-04`: **Referral Supporting Information**: Collate and associate supporting clinical documentation, diagnostic history, and patient context with referral requests.
  - `FEAT-SA-05`: **Referral Status & Outcome Tracking**: Track referral operational progression and communicate referral outcomes (acceptance, decline, or redirection) to referring providers.

#### 3. Scheduling Administration (`Harmonia-Relevant`)
- **3.1 Appointment & Booking Administration**
  - `FEAT-SA-06`: **Appointment Notification Ingestion**: Ingest scheduled appointment events from departmental scheduling systems.
  - `FEAT-SA-07`: **Appointment Status Synchronization**: Propagate booking cancellations, reschedules, and attendances across care coordination systems.

#### 4. Episode & Encounter Administration (`Harmonia-Core`)
- **4.1 Encounter Lifecycle Management**
  - `FEAT-SA-08`: **Encounter Context Creation**: Create and maintain authoritative encounter tracking contexts for hospital admissions, outpatient visits, and community contacts.
  - `FEAT-SA-09`: **Encounter State Progression**: Track encounter status transitions (`PLANNED` $\to$ `ARRIVED` $\to$ `IN_PROGRESS` $\to$ `ON_LEAVE` $\to$ `DISCHARGED`).
  - `FEAT-SA-10`: **Encounter Bed/Location Association**: Dynamically maintain the association between an encounter, care team, and physical bed or ward location.

#### 5. Order Administration (`Harmonia-Core`)
- **5.1 Closed-Loop Order Progression**
  - `FEAT-SA-11`: **Order Request Ingestion**: Ingest clinical diagnostic, medication, and procedure order requests with validated ordering clinician context.
  - `FEAT-SA-12`: **Order Destination Resolution & Routing**: Resolve fulfillment endpoints and reliably route orders to performing service systems.
  - `FEAT-SA-13`: **Order Closed-Loop Progression Tracking**: Track order progression across destination determination, routing, delivery, receipt acknowledgement (distinguishing technical delivery acknowledgement from business acknowledgement), execution progress, completion, and outcome association.
  - `FEAT-SA-14`: **Order Cancellation & Modification Coordination**: Coordinate order cancellation and amendment requests, ensuring synchronization with performing systems.

#### 6. Diagnostic Administration (`Harmonia-Core`)
- **6.1 Diagnostic Workflow Management**
  - `FEAT-SA-15`: **Diagnostic Request Correlation**: Correlate diagnostic test orders with collected clinical specimens and patient encounters.
  - `FEAT-SA-16`: **Diagnostic Report Ingestion & Binding**: Ingest finalized laboratory and radiology reports, verifying content integrity and binding them to the parent order.
  - `FEAT-SA-17`: **Diagnostic Report Distribution**: Distribute finalized laboratory, pathology, and imaging reports to ordering clinicians and designated care teams.

This Feature retains its finalised-report responsibility. Broader Domain03 Diagnostic Administration behaviour may also handle preliminary, corrected and addendum/correction information without redefining this narrower Feature.

#### 7. Medication Administration (`Harmonia-Relevant`)
- **7.1 Medication Order & Dispense Management**
  - `FEAT-SA-18`: **Medication Order Ingestion**: Ingest inpatient and discharge prescription orders.
  - `FEAT-SA-19`: **Dispense Event Tracking**: Ingest medication dispense verification records from pharmacy management systems.
  - `FEAT-SA-20`: **Medication Administration Record (MAR) Ingestion**: Ingest point-of-care medication administration events.

#### 8. Procedure Administration (`Harmonia-Relevant`)
- **8.1 Procedure & Surgical Administration**
  - `FEAT-SA-21`: **Procedure Booking Ingestion**: Ingest scheduled surgical case bookings and procedural requests.
  - `FEAT-SA-22`: **Procedural Documentation Ingestion**: Ingest operative reports, anesthesia records, and post-procedure summaries.

#### 9. Clinical Record Administration (`Harmonia-Core`)
- **9.1 Clinical Documentation Lifecycle**
  - `FEAT-SA-23`: **Clinical Document Ingestion**: Ingest structured and unstructured clinical documents (discharge summaries, consult notes, letters).
  - `FEAT-SA-24`: **Document Versioning & Supersession**: Manage document classification, author attribution, amendment attribution, versioning, and supersession or withdrawal without destructive data loss.
  - `FEAT-SA-25`: **Document Metadata Indexing**: Index clinical document metadata (author, specialty, encounter, date, confidentiality) for rapid discovery.

#### 10. Care Plan Administration (`Harmonia-Relevant`)
- **10.1 Care Plan Coordination**
  - `FEAT-SA-26`: **Care Plan Ingestion & Distribution**: Ingest multidisciplinary care plans and syndicate updates to authorized care team members.

#### 11. Clinical Communication Administration (`Harmonia-Core`)
- **11.1 Secure Clinical Communication**
  - `FEAT-SA-27`: **Clinical Communication Distribution**: Distribute secure clinical communications between verified healthcare providers and designated care team endpoints.
  - `FEAT-SA-28`: **Delivery Acknowledgement Tracking**: Correlate recipient delivery and read receipts with outbound clinical communications, distinguishing transport delivery from recipient acknowledgement. Receipt/read evidence does not by itself establish business acceptance, clinical review, incorporation, approval, finality or originating authority.
  - `FEAT-SA-29`: **Communication Audit Logging**: Maintain an enduring, non-PHI, tamper-evident log of all clinical communication transactions.

#### 12. Funding & Coverage Administration (`Adjacent`)
- Ingestion of patient insurance coverage and eligibility flags. *(No features decomposed)*.

#### 13. Clinical Charging & Claims Administration (`Adjacent`)
- Export of clinical activity summaries and charging records to billing systems. *(No features decomposed)*.

---

# View 3: Service Delivery

Service Delivery encompasses systems enablement across genuine healthcare delivery settings and clinical care delivery contexts.

```text
1. Clinical Assessment [Adjacent]
2. Primary Care [Harmonia-Relevant]
3. Acute Care [Harmonia-Relevant]
4. Emergency Care [Harmonia-Relevant]
5. Inpatient Care [Harmonia-Relevant]
6. Ambulatory Care [Adjacent]
7. Surgical & Procedural Care [Adjacent]
8. Diagnostic Services [Harmonia-Relevant]
9. Therapeutic Services [Adjacent]
10. Medication Therapy [Harmonia-Relevant]
11. Allied Health [Adjacent]
12. Preventive Care [Harmonia-Relevant]
13. Rehabilitation [Adjacent]
14. Community Care [Harmonia-Relevant]
15. Outreach Care [Harmonia-Relevant]
16. Remote & Virtual Care [Harmonia-Relevant]
```

### Architectural Principles: Service Delivery
1. **Heterogeneous Axes Preserved**: The 16 Service Delivery capabilities represent heterogeneous clinical settings (acute, emergency, primary), modalities (virtual, outreach), and specialties (diagnostic, therapeutic). They are deliberately preserved as authentic healthcare contexts rather than normalized into artificial abstractions.
2. **Zero Harmonia-Core in Service Delivery**: No Service Delivery capability is classified as `Harmonia-Core`. Harmonia provides integration, transport, state management, and notification; **Harmonia does not practice medicine or deliver clinical therapy**.
3. **Canonical Enabling Pattern for Relevant Features**:
$$\text{Provide Context} \longrightarrow \text{Receive Clinical State/Outcome} \longrightarrow \text{Correlate} \longrightarrow \text{Preserve Provenance} \longrightarrow \text{Make Available} \longrightarrow \text{Enable Next Activity}$$

---

### Detailed Capability & Feature Catalogue: Service Delivery

#### 2. Primary Care (`Harmonia-Relevant`)
- **2.1 Primary Care Coordination**
  - `FEAT-SD-01`: **General Practice Shared Record Ingestion**: Ingest shared health summaries, encounter notes, and immunization records from primary care practice management systems.
  - `FEAT-SD-02`: **Primary Care Notification Dispatch**: Dispatch emergency department arrival, acute admission, and discharge notifications to the patient's registered general practitioner.

#### 3. Acute Care (`Harmonia-Relevant`)
- **3.1 Acute Care Monitoring & Event Coordination**
  - `FEAT-SD-03`: **Acute Clinical State Event Capture**: Ingest clinical deterioration notifications and acute clinical status updates.
  - `FEAT-SD-04`: **Acute Care Clinical Context Provision**: Provide consolidated longitudinal history and active medication lists to acute care clinicians at point of triage.

#### 4. Emergency Care (`Harmonia-Relevant`)
- **4.1 Emergency Care Progression**
  - `FEAT-SD-05`: **Emergency Department Arrival & Triage Ingestion**: Ingest emergency presentation, triage category, and chief complaint records.
  - `FEAT-SD-06`: **Emergency Care Cross-Facility Correlation**: Correlate incoming emergency presentations with recent regional admissions, prior discharge summaries, and known allergy profiles.

#### 5. Inpatient Care (`Harmonia-Relevant`)
- **5.1 Multidisciplinary Inpatient Care Coordination**
  - `FEAT-SD-07`: **Inpatient Rounding Context Provision**: Aggregate diagnostic results, current medication administration records, and nursing notes for multidisciplinary inpatient rounds.
  - `FEAT-SD-08`: **Inpatient Clinical Progression Tracking**: Capture inpatient clinical milestone completions (e.g., medical stability achieved, allied health clearance).

#### 8. Diagnostic Services (`Harmonia-Relevant`)
- **8.1 Diagnostic Service Integration**
  - `FEAT-SD-09`: **Laboratory & Imaging Result Distribution**: Reliably distribute structured diagnostic reports (pathology, radiology) to ordering clinicians and designated secondary recipients.
  - `FEAT-SD-10`: **Diagnostic History Consolidation**: Consolidate historical diagnostic trends (e.g., cumulative laboratory results) across multiple regional providers into an integrated view.

#### 10. Medication Therapy (`Harmonia-Relevant`)
- **10.1 Medication Therapy Provision and Monitoring**
  - `FEAT-SD-11`: **Medication History Reconciliation Ingestion**: Ingest reconciled medication histories compiled during clinical transitions of care.
  - `FEAT-SD-12`: **Medication Administration Tracking**: Distribute medication therapy orders, dispensing updates, and administration records across clinical care boundaries.

#### 12. Preventive Care (`Harmonia-Relevant`)
- **12.1 Screening and Immunisation Enablement**
  - `FEAT-SD-13`: **Immunisation Event Submission**: Ingest point-of-care immunisation administration records and syndicate to national immunization registers.
  - `FEAT-SD-14`: **Screening Recall Notification Distribution**: Distribute clinical screening reminders and recall notices to designated healthcare providers.

#### 14. Community Care (`Harmonia-Relevant`)
- **14.1 Community Care Coordination**
  - `FEAT-SD-15`: **Community Encounter Ingestion**: Ingest home visit notes, community nursing observations, and social support assessments.
  - `FEAT-SD-16`: **Community Care Plan Synchronization**: Synchronize community care plan updates between hospital outreach teams and community health organizations.

#### 15. Outreach Care (`Harmonia-Relevant`)
- **15.1 Outreach Care Continuity**
  - `FEAT-SD-17`: **Outreach Visit Context Provision**: Package and deliver portable clinical summaries and offline-capable care summaries for remote outreach clinics.
  - `FEAT-SD-18`: **Outreach Encounter Outcome Ingestion**: Ingest and reconcile clinical documentation gathered during rural and remote outreach visits.

#### 16. Remote & Virtual Care (`Harmonia-Relevant`)
- **16.1 Remote Patient Monitoring**
  - `FEAT-SD-19`: **Remote Telemetry Ingestion**: Ingest home patient monitoring data, biometric telemetry, and patient-reported outcome measures (PROMs).
  - `FEAT-SD-20`: **Virtual Care Session Context Binding**: Associate synchronous virtual/telehealth consultations with the longitudinal health record and active care episode.

*(Capabilities 1 Clinical Assessment, 6 Ambulatory Care, 7 Surgical & Procedural Care, 9 Therapeutic Services, 11 Allied Health, and 13 Rehabilitation remain Adjacent or Reference and are not decomposed into atomic features).*

---

# View 4: Health Service Operations

Health Service Operations encompasses systems enablement for the operational management, logistics, capacity, and resource coordination of healthcare facilities and services.

```text
1. Clinic & Practice Operations [Harmonia-Relevant]
2. Ward Operations [Harmonia-Relevant]
3. Theatre Operations [Harmonia-Relevant]
4. Emergency Department Operations [Harmonia-Relevant]
5. Outpatient Operations [Harmonia-Relevant]
6. Bed & Care-Place Management [Harmonia-Core]
7. Clinical Resource Management [Harmonia-Relevant]
8. Service Capacity Management [Harmonia-Relevant]
9. Clinical Workforce Management [Adjacent]
10. Clinical Rostering [Adjacent]
11. Mobile Staff Management [Harmonia-Relevant]
12. On-Call Management [Harmonia-Relevant]
13. Work Allocation & Dispatch [Harmonia-Core]
14. Clinical Training & Education [Reference]
15. Clinical Competency Management [Reference]
16. Clinical Qualification Management [Harmonia-Relevant]
17. Clinical Supervision [Reference]
18. Patient Transport [Harmonia-Core]
19. Clinical Logistics Coordination [Harmonia-Relevant]
20. Discharge Management [Harmonia-Core]
```

### Architectural Principles: Health Service Operations
1. **Facility Logistics, Not IT Machinery**: Health Service Operations reflects real-world health facility logistics and coordination (wards, beds, staff allocation, patient transfer); it must **never** be redefined or reinterpreted as protocol mediation, message queuing, deduplication, or integration software machinery.
2. **Healthcare Service Context Rule**:
> **Where operational activity occurs in the context of a Healthcare Service, that service context must be explicitly associated with the activity and state rather than inferred solely from practitioner, organisation, or location.**
3. **General Operational Progression Pattern**:
$$\text{Event} \longrightarrow \text{Entity Context} \longrightarrow \text{Required Activity} \longrightarrow \text{Assignment} \longrightarrow \text{Dispatch} \longrightarrow \text{ACK} \longrightarrow \text{Progress} \longrightarrow \text{Outcome} \longrightarrow \text{Entity State Change} \longrightarrow \text{Next Activity}$$

---

### Detailed Capability & Feature Catalogue: Health Service Operations

#### 1. Clinic & Practice Operations (`Harmonia-Relevant`)
- **1.1 Session & Queue Coordination**
  - `FEAT-HSO-01`: **Clinic Session Tracking**: Ingest and track clinic operating session start, delay, and completion states.
  - `FEAT-HSO-02`: **Operational Queue Progression**: Track client queue progression through reception, waiting area, consultation, and billing.

#### 2. Ward Operations (`Harmonia-Relevant`)
- **2.1 Ward Logistics Coordination**
  - `FEAT-HSO-03`: **Ward Patient Flow Coordination**: Coordinate inpatient ward occupancy, care-place allocation, and patient bed status across care facilities.
  - `FEAT-HSO-04`: **Clinical Handover Context Assembly**: Collate current ward patient lists, active medical concerns, and pending tasks for nursing shift handovers.

#### 3. Theatre Operations (`Harmonia-Relevant`)
- **3.1 Perioperative Logistics**
  - `FEAT-HSO-05`: **Operating Theatre Case Progression**: Track surgical case state transitions (`CALLED` $\to$ `IN_THEATRE` $\to$ `ANAESTHETISED` $\to$ `INCISION` $\to$ `CLOSED` $\to$ `RECOVERY`).
  - `FEAT-HSO-06`: **Perioperative Resource Notification**: Dispatch alerts to portering, sterilization, and recovery teams based on surgical case progression milestones.

#### 4. Emergency Department Operations (`Harmonia-Relevant`)
- **4.1 ED Operational Coordination**
  - `FEAT-HSO-07`: **ED Bed & Bay Tracking**: Track occupancy and status of emergency cubicles, resuscitation bays, and short-stay beds.
  - `FEAT-HSO-08`: **ED Length of Stay Tracking**: Monitor operational wait times and length-of-stay milestones, generating operational escalation notifications.

#### 5. Outpatient Operations (`Harmonia-Relevant`)
- **5.1 Outpatient Flow Enablement**
  - `FEAT-HSO-09`: **Outpatient Arrival Registration**: Ingest self-service check-in and clinic arrival events, notifying treating clinicians.
  - `FEAT-HSO-10`: **Post-Clinic Order Coordination**: Capture follow-up booking and diagnostic order requirements generated at clinic completion.

#### 6. Bed & Care-Place Management (`Harmonia-Core`)
- **6.1 Bed State & Turnover Management**
  - `FEAT-HSO-11`: **Bed Availability Tracking**: Maintain operational status of all registered beds and care-places (`VACANT`, `OCCUPIED`, `BLOCKED`, `RESERVED`, `DIRTY`, `CLEANING`, `CLEAN`).
  - `FEAT-HSO-12`: **Bed State Progression**: Progress bed states in response to clinical and environmental events (e.g., patient discharge automatically transitions bed to `DIRTY`).
  - `FEAT-HSO-13`: **Bed Readiness Notification**: Notify bed allocation managers and clinical units when a cleaned bed achieves `READY` status.

#### 7. Clinical Resource Management (`Harmonia-Relevant`)
- **7.1 Specialized Equipment Coordination**
  - `FEAT-HSO-14`: **Mobile Clinical Equipment Tracking**: Track physical location and allocation status of shared clinical equipment (infusion pumps, ventilators, mobile telemetry).

#### 8. Service Capacity Management (`Harmonia-Relevant`)
- **8.1 Facility Demand & Capacity Telemetry**
  - `FEAT-HSO-15`: **Operational Capacity Metric Aggregation**: Aggregate operational capacity telemetry (occupancy rates, wait times, diversion status) across facilities.

#### 11. Mobile Staff Management (`Harmonia-Relevant`)
- **11.1 Mobile Worker Dispatch & Safety**
  - `FEAT-HSO-16`: **Mobile Worker Task Dispatch**: Dispatch community care tasks and urgent visits to roving clinicians' mobile devices.
    - **Architectural scope qualification (2026-10-09 human adjudication)**: Dispatch here concerns integration with external/business mobile work-management mechanisms: exchanging externally established assignment, dispatch and operational progression/status information with applicable worker/context information. Harmonia does not thereby determine or manage clinical work, clinical handover, clinical allocation or EMR worklists. External “work/task” vocabulary does not redefine Harmonia synthetic Task. The Feature name/identifier are retained; the proposed name `Mobile Work Task Management Integration` requires separate human confirmation. See the [Business integration boundary](../../03-business-architecture/metamodel/business-architecture-metamodel.md#37-clinical-work-integration-boundary).
  - `FEAT-HSO-17`: **Mobile Visit Status Tracking**: Ingest arrival, visit progression, and safety check-in milestones from roving staff.

#### 12. On-Call Management (`Harmonia-Relevant`)
- **12.1 On-Call Directory Resolution**
  - `FEAT-HSO-18`: **Active On-Call Provider Resolution**: Resolve currently active on-call specialists, registrars, and operational managers for a given specialty and hospital at any point in time.

#### 13. Work Allocation & Dispatch (`Harmonia-Core`)
- **13.1 Operational Work Assignment & Supervision**
  - `FEAT-HSO-19`: **Work Item Instantiation**: Instantiate operational units of work from clinical triggers (e.g., bed cleaning required, specimen transport required).
  - `FEAT-HSO-20`: **Worker Matching & Allocation**: Match work items against available staff roles, credentials, and locations based on allocation policies.
  - `FEAT-HSO-21`: **Work Dispatch Delivery**: Deliver work orders to target workers, devices, or team queues with technical and operational acknowledgement capture.
  - `FEAT-HSO-22`: **Work Progress Oversight & Escalation**: Monitor work progression against time-to-completion thresholds, escalating stalled or unacknowledged tasks.

<a id="16-clinical-credential-management-harmonia-relevant"></a>

#### 16. Clinical Qualification Management (`Harmonia-Relevant`)
- **16.1 Credential Verification Checkpoint**
  - `FEAT-HSO-23`: **Operational Credential Check**: Validate that a practitioner possesses active credentials and required competency certs prior to dispatching specialized work.

#### 18. Patient Transport (`Harmonia-Core`)
- **18.1 Patient Transfer Coordination**
  - `FEAT-HSO-24`: **Transport Request Ingestion**: Ingest internal (portering) and external (ambulance, inter-hospital) patient transfer requests with clinical mobility requirements.
  - `FEAT-HSO-25`: **Transport Dispatch & Progress Tracking**: Dispatch transport orders, tracking departure, transit, and destination arrival checkpoints.

#### 19. Clinical Logistics Coordination (`Harmonia-Relevant`)
- **19.1 Specimen & Supply Courier Tracking**
  - `FEAT-HSO-26`: **Pathology Specimen Transport Tracking**: Track pickup, custody chain, transport conditions, and laboratory delivery of clinical specimens.

#### 20. Discharge Management (`Harmonia-Core`)
- **20.1 Discharge Progression & Coordination**
  - `FEAT-HSO-27`: **Discharge Milestone Tracking**: Track clinical criteria-led discharge milestones (medications dispensed, transportation arranged, home care confirmed).
  - `FEAT-HSO-28`: **Discharge Readiness & Coordination Oversight**: Track and aggregate multidisciplinary discharge milestones, home care confirmations, and transit readiness across care teams.
  - `FEAT-HSO-29`: **Discharge Notification Dispatch**: Dispatch departure notifications, updated bed states, and finalized discharge summaries to external care providers upon physical patient exit.

---

# View 5: Intrinsic / Shared Enablement

Intrinsic / Shared Enablement encompasses the horizontal, platform-wide system capabilities required to support all healthcare operating contexts.

```text
1. Patient Clinical Record [Harmonia-Core]
2. Health Information Exchange [Harmonia-Core]
3. Health Information Access [Harmonia-Core]
4. Health Information Communication [Harmonia-Core]
5. Health Information Control [Harmonia-Core]
6. Clinical Knowledge Services [Harmonia-Relevant]
7. Clinical Collaboration [Harmonia-Core]
8. General Collaboration [Harmonia-Relevant]
9. Workflow & Activity Coordination [Harmonia-Core]
10. Calendar Management [Harmonia-Core]
11. Presentation Services [Harmonia-Core]
12. Information Design Governance [Harmonia-Core]
```

### Critical Domain Distinctions: Intrinsic / Shared Enablement

#### Health Information Exchange: Four Interaction Models
1. **Submission**: *"I send this to you."* (Point-to-point push of an authored artifact).
2. **Retrieval**: *"I ask for this."* (Synchronous or asynchronous pull of specific records).
3. **Distribution**: *"I deliberately send this to identified recipients."* (Addressed fan-out delivery to designated systems or clinical actors).
4. **Syndication**: *"I publish information/change and subscribers receive matching interests."* (Publish-subscribe topic/criteria distribution).

#### Workflow & Activity Coordination: Three Work Units
1. **Work Order**: A unit of work where a **human is doing something** physical or operational (e.g., clean bed 4B, transfer patient to X-ray, draw blood sample).
2. **To Do**: A unit of work where a **human is reviewing, updating, or deciding**, including clinical approvals, sign-offs, and triage reviews.
3. **Task**: A unit of **non-human executable work** (internally a synthetic task) executed by platform automation, daemons, or services.
*Execution Semantics*: Workflow Management provides the coordination and state engine through which these three activity types progress.

#### Clinical Collaboration Context
Clinical Collaboration is **Harmonia-Core** because Harmonia provides **Longitudinal Health Record (LHR)-aware collaboration spaces**. Clinicians can converse within secure multidisciplinary team rooms and query patient context directly (e.g., *"What were Fred's last three HbA1c results?"*).
*Important Boundary*: A clinical statement made within a collaboration chat does **not** automatically become an authoritative clinical Observation or medical record entry. It remains collaborative discourse unless formally authored into a clinical document.

#### Presentation Services Decoupling
Presentation Services consumes established identity, security, and clinical context. Presentation does **not** own:
- Authentication or credential storage.
- Identity lifecycle or EMPI reconciliation.
- Consent decisions or access-policy evaluation.
- Authoritative clinical state preservation.

---

### Detailed Capability & Feature Catalogue: Intrinsic / Shared Enablement

#### 1. Patient Clinical Record (`Harmonia-Core`)
- **1.1 Longitudinal Clinical Record Governance**
  - `FEAT-ISE-01`: **Longitudinal Health Record (LHR) Assembly**: Assemble disparate encounters, diagnostic reports, medications, and clinical documents into a unified, vendor-neutral longitudinal timeline.
  - `FEAT-ISE-02`: **Active Clinical Record Access**: Provide low-latency, active distributed access to consolidated patient clinical summaries.
  - `FEAT-ISE-03`: **Durable Clinical Record Preservation**: Durably maintain governed, vendor-neutral longitudinal clinical representation and preservation with verifiable integrity controls.

#### 2. Health Information Exchange (`Harmonia-Core`)
- **2.1 Multi-Model Information Exchange**
  - `FEAT-ISE-04`: **Direct Submission Ingestion & Delivery**: Support push submissions of clinical documents and messages across boundaries.
  - `FEAT-ISE-05`: **Clinical Record Retrieval Mediation**: Mediate query-based retrieval requests across external and internal repositories.
  - `FEAT-ISE-06`: **Addressed Clinical Distribution**: Fan out clinical events and diagnostic reports to designated external and internal recipients with per-destination delivery tracking.
  - `FEAT-ISE-07`: **Event-Driven Syndication**: Publish clinical and directory change events to subscribed systems matching interest filters.

#### 3. Health Information Access (`Harmonia-Core`)
- **3.1 Governed Query & Search**
  - `FEAT-ISE-08`: **Federated Clinical Query**: Execute bounded, indexed queries across distributed clinical resources and directory graphs.
  - `FEAT-ISE-09`: **Access-Controlled Information Filtering**: Filter search results dynamically according to requesting user authorizations and patient consent directives.

#### 4. Health Information Communication (`Harmonia-Core`)
- **4.1 Boundary Protocol Mediation**
  - `FEAT-ISE-10`: **Standards-Based Boundary Exchange**: Provide standards-conformant boundary exchange for healthcare messaging and resource representations.

#### 5. Health Information Control (`Harmonia-Core`)
- **5.1 Policy & Security Governance**
  - `FEAT-ISE-11`: **Health Information Access & Consent Control**: Evaluate patient consent preferences, practitioner authority boundaries, and access control policies for managed clinical records.
  - `FEAT-ISE-12`: **Security Context Propagation**: Propagate tamper-evident, attributable security context across asynchronous processing boundaries.
  - `FEAT-ISE-13`: **Non-PHI Compliance Auditing**: Generate tamper-evident, verifiable audit records of security-significant events without emitting unmasked PHI.

#### 6. Clinical Knowledge Services (`Harmonia-Relevant`)
- **6.1 Terminology & Reference Mapping**
  - `FEAT-ISE-14`: **Clinical Concept Resolution**: Resolve local clinical codes against standard terminologies (SNOMED CT, LOINC, ICD-10).
  - `FEAT-ISE-15`: **Bidirectional Terminology Mapping**: Translate between legacy local code systems and canonical national healthcare profiles.

#### 7. Clinical Collaboration (`Harmonia-Core`)
- **7.1 LHR-Aware Multidisciplinary Collaboration**
  - `FEAT-ISE-16`: **Care-Team Clinical Collaboration Coordination**: Coordinate secure multidisciplinary clinical communication and consultation exchange bound to patient encounters or clinical topics.
  - `FEAT-ISE-17`: **In-Conversation LHR Query Resolution**: Resolve clinical data queries within an authorized collaboration context, presenting verified clinical summaries directly to care team members.
  - `FEAT-ISE-18`: **Zero-PHI Collaboration Metadata Governance**: Ensure collaboration channel titles, topics, and aliases use opaque identifiers free of patient names, MRNs, or sensitive details.

#### 8. General Collaboration (`Harmonia-Relevant`)
- **8.1 Operational Messaging**
  - `FEAT-ISE-19`: **Operational Team Chat Enablement**: Provide secure instant messaging channels for departmental, administrative, and facility support staff.

#### 9. Workflow & Activity Coordination (`Harmonia-Core`)
- **9.1 Multi-Work-Unit Orchestration**
  - `FEAT-ISE-20`: **Work Order Progression**: Manage execution states and dispatch of human operational work orders.
  - `FEAT-ISE-21`: **To Do Decision & Approval Coordination**: Coordinate human clinical review items, sign-offs, and approval workflows.
  - `FEAT-ISE-22`: **Synthetic Task Orchestration**: Execute non-human, automated platform activity units and sequence orchestration.
  - `FEAT-ISE-23`: **Workflow Timeout & Escalation Oversight**: Supervise operational deadlines, executing automated timeout notifications and supervisory escalations.

#### 10. Calendar Management (`Harmonia-Core`)
- **10.1 Operational & Clinical Calendar Sync**
  - `FEAT-ISE-24`: **Schedule Projection Integration**: Project clinician rosters, on-call schedules, and facility operating calendars into operational views.
  - `FEAT-ISE-25`: **Temporal Event Correlation**: Correlate incoming clinical events with scheduled appointments and operational calendar milestones.

#### 11. Presentation Services (`Harmonia-Core`)
- **11.1 Decoupled Human Interaction & Dashboards**
  - `FEAT-ISE-26`: **Longitudinal Clinical & Operational Presentation**: Deliver contextual clinical timelines, directory administration views, and operational management dashboards.

#### 12. Information Design Governance (`Harmonia-Core`)
- **12.1 Canonical Semantics & Schema Governance**
  - `FEAT-ISE-27`: **Information Standards & Semantic Governance**: Govern canonical clinical data definitions, terminology bindings, and exchange profiles across regional systems.
  - `FEAT-ISE-28`: **Semantic Conformance Verification**: Verify inbound and outbound representations against published semantic rules and conformance constraints.

---

## Health Service Assurance: Approved Business Enabling Capabilities

[REQ-FND-005 — Independent Assurance of Governed Activity](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity) motivates [BC-18 — Health Service Assurance](business-capabilities.md#18-health-service-assurance). BC-18 requires the three Business Enabling Capabilities defined here. They are collaboratively realised by the approved contributions of existing Enterprise Capabilities and [EC-14 — Service Guardian](enterprise-capabilities.md#ec-14-service-guardian), as recorded in the [contribution matrix](../capability-maps/health-service-assurance-derivation.md#collaborative-enterprise-capability-contribution-matrix).

These are Strategy capabilities and semantic boundaries. Their presentation does not create a sixth contextual view, allocate them to an existing view, establish CT1 / CT2 / CT3 ancestry, or derive Business Functions, Services, Processes, Features or assurance Information Concepts.

### Assurance Design

- **Definition**: The capability to determine and define how satisfaction of a governed requirement, constraint or expected behaviour will be assured, including its assurance disposition, applicable criteria, required evidence and evaluation expectations.
- **Fundamental Question**: **How will satisfaction of this requirement, constraint or expected behaviour be assured?**
- **Boundary**: Assurance Design is a peer concern to Behaviour Design and Failure/Recovery Design when implementing governed behaviour. This relationship establishes design concerns, not additional capability entries, application design or implementation specifications.

### Assurance Criteria Management

- **Definition**: The capability to establish and manage reusable, governed and temporally identifiable assurance criteria through which subjects may be evaluated.
- **Scope**: Criteria may express governance rule boundaries, quality constraints, performance expectations, compliance criteria, temporal criteria, evidence requirements or completeness expectations. These are examples, not an exhaustive taxonomy.
- **Boundary**: Criteria management does not own or replace the authoritative governance requirement from which a criterion may derive. Not every assurance criterion is required to derive from formal Governance. Criteria may have their own lifecycle, version and temporal applicability.

> **Assurance Criteria may operationalise a governing requirement for evaluation, but SHALL NOT silently alter, weaken, strengthen or replace the authoritative requirement from which they derive.**

### Governed Assurance

- **Definition**: The capability to independently evaluate a governed subject against applicable assurance criteria through the assembly and evaluation of sufficient trustworthy evidence, establishing explicit findings, exceptions and assurance conclusions.
- **Fundamental Question**: **What can we independently establish about this subject against these criteria from the available trustworthy evidence?**
- **Assurance Semantics**: Establishing the subject/context of assurance, applicable assurance criteria, evidentiary association, assessment, adjudication, findings, exceptions and conclusions.
- **Boundary**: Governed Assurance does not manage, remediate, assign, delegate, escalate operationally or complete the subject activity. It may manage its own assurance activity; findings may inform operational response without transferring responsibility for that response.

### Assurance Disposition

Every implemented behaviour has an assurance aspect. Every governed obligation, functional requirement, non-functional requirement or other defined implementation requirement SHALL have an explicit assurance disposition identifying how its satisfaction is assured.

The approved disposition concepts are:

- **Generic Assurance sufficient**
- **Existing Specific Assurance applies**
- **Existing Assurance must be extended**
- **New Specific Assurance required**
- **Not Assurable**
- **Not Worth Assuring**

These are architectural semantics, not an information model or software enumeration. **Not Assurable** does not mean the requirement is not required. **Not Worth Assuring** does not mean the requirement is optional or unimportant. Both are explicit governed assurance decisions, not absence of assurance configuration. An explicit disposition does not require specific assurance of every individual activity, information item or outcome; REQ-FND-005 applies where assurance is required.

### Generic Processing Assurance and System Operations

Harmonia-managed workflow behaviour SHALL be subject to applicable generic processing assurance. This concerns business-level processing behaviour above and beyond System Operations. Potential concerns include progression, completion, timeliness, expected processing pathway, failure behaviour, recovery behaviour, outcome, and aggregate or pattern behaviour. This is not a final exhaustive criteria catalogue.

System Operations asks whether the technical machinery is operating. Generic processing assurance asks whether governed business behaviour is operating within expected boundaries.

> **Operational Health ≠ Processing Assurance.**

Failure and recovery are part of the expected behavioural space of the activity being executed. The activity/Praxis remains responsible for handling its own expected failure and recovery behaviour. Assurance independently establishes whether normal, failure and recovery behaviour remain within applicable expectations. This responsibility boundary does not allocate assurance execution to Praxis or another solution construct.

[EC-12 Operational Assurance](enterprise-capabilities.md#ec-12-operational-assurance) retains its established resilience, concurrency, exception/recovery and telemetry responsibilities and may contribute directly to generic operational/processing assurance. [EC-14 Service Guardian](enterprise-capabilities.md#ec-14-service-guardian) supplies the distinguishing assurance-specific semantics; neither replaces System Operations or the other Enterprise Capability.

### Assurance Evidence Assembly and Information Ownership

Assurance Evidence Assembly is the evidence-assembly concern within Governed Assurance; this statement does not establish a fourth Business Enabling Capability or a downstream Function, Service or Information Concept.

Underlying information used as assurance evidence remains owned and managed by the capability responsible for that information. Assurance owns the contextual association that particular information constitutes evidence for a particular assurance purpose. Being evidence is contextual, not an intrinsic universal property of source information.

Evidence may be associated through reference, linkage, aggregation, representation, mirroring or derivation. These possibilities do not prescribe technical storage or replication. An association may identify a particular version, state or temporal context of the underlying information.

> **Evidence inclusion may be temporally and version-specific. Association of information as assurance evidence SHALL be capable of distinguishing the particular information state relevant to the assurance activity from the information's current state.**

### Temporally Appropriate Evidence and Criteria

Applicable assurance criteria may themselves be versioned and temporally applicable. A conclusion may depend both on the information state relevant to the evidence and on the criterion state applicable to the subject at the relevant time. Assurance evaluates temporally appropriate evidence against temporally appropriate criteria. No persistence, active-state, replication or retention mechanism is derived here.

### Assessment, Adjudication and Explicit Insufficiency

| Concern | Fundamental question | Semantic boundary |
| :--- | :--- | :--- |
| **Assessment** | What does this evidence demonstrate with respect to the applicable criterion? | May supply quantitative, qualitative, compound, temporal or confidence-bearing information. |
| **Adjudication** | What assurance finding or conclusion follows from that assessment? | Establishes the finding or conclusion from assessed detail; it is distinct from assessment and operational response. |

Assurance SHALL NOT be reduced universally to PASS / FAIL / UNKNOWN. Insufficient evidence is not equivalent to either satisfaction or non-satisfaction. Under REQ-FND-005, where available evidence cannot establish the required assurance conclusion with sufficient confidence, that inability and evidence insufficiency SHALL remain explicit.

An assurance activity may execute correctly while lacking sufficient evidence to establish a conclusion about its subject. This is distinct from an indeterminate execution outcome or execution failure. [REQ-FND-004](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-004-explicit-indeterminate-outcome) and [AX-15](../../governance/architectural-axioms.md#ax-15) continue to govern uncertainty about operational state, effect or outcome, including the assurance activity's own execution where applicable.

### Assurance Independence

Independent Assurance is performed through an independently governed activity whose progression and assurance conclusion are not controlled by the activity or mechanism responsible for performing or managing the subject being assured.

Independence does not require a different organisation, person, application, Kubernetes cluster or database, an external auditor, or physical separation. The subject activity may supply evidence but SHALL NOT solely determine, suppress, manufacture or retrospectively alter its own assurance conclusion.

> **Evidence dependency does not compromise assurance independence; control dependency does.**

Assurance may manage its own assurance activity without assuming management of the subject activity. No technical or organisational separation is prescribed by this boundary.

### Clinical Services Delivery Assurance Boundary

Health Service Assurance SHALL NOT be represented as Clinical Services Delivery Assurance. Harmonia does not thereby assume responsibility for assuring clinical judgement, professional practice, clinical adequacy of care, or delivery of clinical services by healthcare practitioners or healthcare organisations.

Information concerning clinical service delivery MAY be the subject of governed assurance where an explicit applicable requirement establishes that concern. This does not transfer responsibility for Clinical Services Delivery Assurance to Harmonia. [Capability 06 — Clinical Quality, Safety & Improvement](business-capabilities.md#06-clinical-quality-safety--improvement) retains its established clinical quality/safety responsibilities.

> **Harmonia may assure facts and behaviour concerning clinical activity where explicitly required; it does not thereby assure the clinical adequacy of care.**

### Strategy Derivation Boundary

The [approved derivation view](../capability-maps/health-service-assurance-derivation.md#unresolved-relationships-and-downstream-boundary) stops at collaborative Enterprise Capability realisation. No strategic logical or application component is allocated these capabilities or EC-14. No Dokimasia, Ponos, Praxis, Pragma, Digital Twin, Mneme, Mnemosyne, Calliope, Iris, Pylai or other implementation relationship is established. Business Architecture, Information Architecture and solution derivation remain subsequent activities.

---

## Downstream Progression

The Business Enabling Capability catalogue and its atomic Features establish what systems must enable for the healthcare enterprise. These features serve as the empirical basis for deriving the reusable, technology-neutral platform capabilities:
- [Enterprise Capabilities (EC-01 .. EC-14)](enterprise-capabilities.md): The established EC-01 through EC-13 and approved EC-14 Service Guardian; the Health Service Assurance contribution model is established without adding Feature or component allocations.
- [Health Service Assurance Derivation](../capability-maps/health-service-assurance-derivation.md): REQ-FND-005 → BC-18 → the three approved Business Enabling Capabilities → collaborative Enterprise Capability realisation.
- [Capability Tier Progression Model](../capability-maps/capability-tier-model.md): Detailed vertical derivation rules and representative composition examples.
- [ICT Foundation Lenses](ict-foundation-lenses.md): Technical enablement considerations guiding technology realization.
