# Business Information Responsibility & Ownership Model

## 1. Architectural Principles of Information Responsibility

In the Harmonia Business Architecture, **Business Information Responsibility** establishes the authoritative conceptual ownership, stewardship, and lifecycle governance of business information assets across capability boundaries.

### 1.1 The Fundamental Ownership Invariant
> **Business information ownership derives strictly from Capability responsibility and the Functions/Processes through which that responsibility is discharged.**

### 1.2 Non-Transfer of Ownership Guardrail
Consuming, searching, transporting, caching, indexing, coordinating, presenting, or physically persisting information by another capability **never transfers architectural ownership**:
- **Health Information Exchange** transports clinical documents, but ownership of the document content remains with *Clinical Record Administration*.
- **Health Information Access** executes semantic searches across diagnostic reports, but originating authority for findings remains attributable to the performing diagnostic provider. *Diagnostic Administration* governs correlation, ingestion, binding, notification, status and addenda/correction information; it does not acquire diagnostic analysis, clinical interpretation or originating findings authority.
- **Health Information Control** evaluates access policies, but does not own the patient demographic or clinical data being protected.
- **Workflow & Activity Coordination** coordinates task timers and state transitions, but does not own the clinical or operational meaning of the task.
- **Presentation Services** renders clinical summaries on clinician dashboards, but owns only the presentation view configuration, not the clinical facts rendered.

### 1.3 Purity of Business Information Semantics
Business Information concepts retain authentic clinical and operational definitions. They must **not** be equated to or conflated with downstream implementation constructs:
- An **Identity Alias** is conceptually a name/alias by which a person is known, not a FHIR `HumanName` datatype or a database varchar column.
- A **Care-Place Definition** is conceptually the physical specification of a bed/bay, not a database foreign key.
- An **Encounter Context** is conceptually a healthcare episode, not a FHIR `Encounter` resource or JSON wire payload.

---

## 2. Canonical Business Information Ownership Matrix

The table below defines the authoritative R1 Business Information Responsibility demarcations across the capability suite:

| Owning Capability | Conceptual Information Asset | Architectural Scope & Governed Information Semantics | Non-Ownership Demarcations & Guardrails |
| :--- | :--- | :--- | :--- |
| **Person Identity** *(Client Admin)* | **Person Identity & Identifier Correlation Graph** | Authoritative person master records, identifier namespace bindings, demographic traits, identity aliases, probabilistic/deterministic linkage correlations, and merge/unlink audit logs. | Does not own transactional encounter registrations or clinical observations. |
| **Healthcare Subject Context** | **Healthcare Subject Profile** | Subject demographic context, preferred communication methods, language/interpreter needs, cultural requirements, and verified vital status (death notifications). | Does not own person identifier correlation. |
| **Client Relationships** | **Client Support Network & Legal Mandates** | Carer relationships, next-of-kin contacts, nominated representatives, and verified legal mandates (powers of attorney, guardianship orders). | Does not own clinical consent directives or clinical decisions. |
| **Client Privacy** | **Client Consent & Privacy Directives** | Patient consent preferences, opt-in/opt-out records, sensitive health category exclusions, and disclosure restriction directives. | Does not own access control policy evaluation rules. |
| **Provider Administration** | **Practitioner Registry & Role Bindings** | Practitioner master profiles, national registration identifiers (HPI-I), professional qualifications, verified specialties, practitioner role assignments, electronic messaging endpoints, and approved scopes of clinical practice. | Does not own healthcare service catalogue definitions or facility operational rosters. |
| **Organisation Administration** | **Healthcare Organisation Registry** | Enterprise legal identities, national facility identifiers (HPI-O), accreditation status, organisational hierarchy trees (directorates/departments), and official facility contacts. | Does not own physical location geospatial data. |
| **Location Administration** | **Location & Care-Place Definitions** | Physical sites, campuses, buildings, floors, wards, rooms, bays, and structural care-place specifications (bed bay infrastructure, gas outlets, physical telemetry wiring). | Does not own operational occupancy, cleaning state, or bed turnover workflows. |
| **Health Service Administration** | **Service Catalogue & Service/Location/Provider Map** | Healthcare service definitions, clinical specialty taxonomy, service availability schedules, eligibility rules, and the multi-dimensional mapping matrix: $\text{Service} \leftrightarrow \text{Provider} \leftrightarrow \text{Location} \leftrightarrow \text{Practitioner}$. | Owns the *mapping associations*; does not own the mapped Provider, Location, or Practitioner entities. |
| **Clinical Device Administration** | **Device Registry & Association Ledger** | Medical device types, manufacturers, Unique Device Identifiers (UDI), physical serial numbers, active device-to-patient/bed bindings, and network communication endpoints. | Does not own physiological observation streams. |
| **Client-Centred Administration** | **Administrative Intake Ledger** | Departmental registration intake queues, administrative demographic modification batches, and demographic distribution subscriber lists. | Does not own authoritative person master identity. |
| **Referral Administration** | **Referral Master & Triage Ledger** | Incoming referral packages, clinical indications, priority triage categories, specialist acceptance decisions, and referral disposition histories. | Does not own closed-loop diagnostic or therapeutic orders. |
| **Scheduling Administration** | **Appointment Synchronisation State** | Ingested appointment booking milestones, schedule change events, and consolidated patient appointment timeline caches. | Does not own host PAS booking engines or master clinic slots. |
| **Episode & Encounter Administration** | **Encounter Master & Movement Ledger** | Encounter identifiers, clinical class, admitting diagnoses, attending clinicians, encounter lifecycle states, and historical care-place movement logs. | Does not own longitudinal health record aggregation. |
| **Order Administration** | **Clinical Order & Closed-Loop Matrix** | Clinical diagnostic, medication and procedural order information, routing directives, closed-loop state milestones, and order-result correlation links. | Does not own diagnostic interpretations or referral assumption of care. |
| **Diagnostic Administration** | **Diagnostic Correlation & Linkage Registry** | Diagnostic order-to-report correlation bindings, accession number linkages, report delivery status ledgers, and addenda/correction notices. | Does not own clinical specimen analysis or imaging interpretation. |
| **Medication Administration** | **Medication Event Timeline Matrix** | Aggregated medication-order information, community/hospital dispensing updates and administration-event information. Nurse e-MAR records are non-exhaustive examples; scope is not restricted to bedside, nursing or inpatient contexts. | Does not prescribe, dispense, or administer medications. |
| **Procedure Administration** | **Procedure Request & Documentation Index** | Surgical/procedural booking requisitions, operation reports, anaesthetic records, and procedural note indices. | Does not own surgical theatre suite logistics. |
| **Clinical Record Administration** | **Clinical Document Registry & Lifecycle State** | Clinical document master metadata, structured and narrative document repository, document version histories, addenda links, and legal document states (*Draft* to *Superseded*). | Does not own real-time collaborative discussion transcripts. |
| **Care Plan Administration** | **Shared Care Plan & Clinical Goal Registry** | Multidisciplinary care plan definitions, agreed clinical goals, barrier logs, action items, and care team participant enrolments. | Does not own individual clinical encounter notes. |
| **Clinical Communication Administration** | **Clinical Message Dispatch & Audit Register** | Secure message dispatch logs, end-to-end business delivery acknowledgements (business ACKs), and non-repudiation communication audit trails. | Does not own the clinical content of messages. |
| **Patient Clinical Record** | **Governed Longitudinal Health Record (LHR)** | Canonical longitudinal health record synthesis, unified chronological clinical timeline, active problem lists, verified allergy lists, and immutable historical record archives. | Does not own original legal documents; owns the canonical integrated clinical synthesis. |
| **Bed & Care-Place Management** | **Operational Bed State & Turnover Ledger** | Real-time bed occupancy status (*Available*, *Occupied*, *Cleaning*, *Locked*), environmental locks, turnover milestone logs, and cleaning verification timestamps. | Does not own static physical care-place definitions. |
| **Work Allocation & Dispatch** | **Operational Work Dispatch & Milestone Ledger** | Facility support work requests, portering/cleaning queues, mobile worker assignments, and job progression milestone timestamps. | Does not own clinical task review decisions or clinical orders. |
| **Patient Transport** | **Patient Transport Request & Transit Log** | Transport booking manifests, escort requirements, transit milestone logs, and clinical handover receipts. | Does not own vehicle fleet asset maintenance. |
| **Clinical Logistics Coordination** | **Specimen Manifest & Chain-of-Custody Register** | Pathology bio-specimen courier manifests, temperature compliance logs, courier custody signatures, and laboratory delivery receipts. | Does not own pathology diagnostic results. |
| **Discharge Management** | **Discharge Readiness & EDD Register** | Multidisciplinary discharge barrier checklists, Estimated Date of Discharge (EDD) records, and departure readiness confirmations. | Does not own clinical discharge summary authoring. |
| **Health Information Exchange** | **Exchange Transaction Ledger & Route Matrix** | Ingress message transaction receipts, federated query logs, point-to-point routing tables, and publish-subscribe syndication topic matrices. | Does not own transported message content. |
| **Health Information Access** | **Query Execution Context & Projection State** | Semantic query execution contexts, search indices, and access-qualified temporary result projection state. | Does not own access control policies or queried source records. |
| **Health Information Control** | **Security Context & Policy Ruleset** | Authorisation policy definitions (RBAC/ABAC rules), security context tokens, consent restriction matrices, and tamper-evident security audit trails. | Does not own patient demographic or clinical data. |
| **Clinical Knowledge Services** | **Canonical Ontologies & Mapping Tables** | Governed clinical terminology structures (SNOMED-CT, LOINC, AMT), value sets, and cross-ontology semantic translation maps. | Does not own patient-specific clinical coded observations. |
| **Clinical Collaboration** | **Collaboration Space Metadata & Discourse Index** | Care-team collaboration channel metadata, membership registries, discussion thread indices, and clinical summary contextual projection links. | Collaborative discourse does not constitute authoritative clinical records. |
| **General Collaboration** | **Operational Collaboration Space Ledger** | Non-clinical operational messaging spaces, committee rooms, and logistics discussion logs. | Does not own patient clinical data. |
| **Workflow & Activity Coordination** | **Generic Activity Instance Registry & Timers** | Generic task state progression graphs, timer triggers, deadline escalation rules, and worklist queues across Work Orders, To Dos, and Synthetic Tasks. | Owns generic coordination semantics; does not own business meaning or clinical outcome. |
| **Calendar Management** | **Projected Unified Temporal Timeline** | Composite chronological schedule projections, shift boundaries, and temporal activity correlation indices. | Does not own authoritative host rosters or booking slots. |
| **Presentation Services** | **Presentation Specification & Layout Preferences** | Responsive presentation structures, presentation layouts, and clinician preference configurations. | Does not own presented clinical or operational data. |
| **Information Design Governance** | **Canonical Semantic Models & Conformance Schemas** | Enterprise canonical semantic model definitions, data element dictionaries, terminology binding rules, exchange profiles, and semantic conformance constraint sets. | Does not own runtime instances of data. |

---

## 3. Information Lifecycle & Stewardship Governance

1. **Stewardship by Owning Capability**: The capability that creates or governs a business information asset is solely accountable for its semantic validity, lifecycle progression, and retention policies.
2. **Access via Defined Services**: Consuming capabilities must interact with information assets exclusively through the exposed Business Services of the owning capability.
3. **No Secondary Truth**: Active states, search projections, exchange wrappers, and presentation views are non-authoritative derived artifacts; they must never be treated as secondary sources of durable truth.

---

## 4. Assurance-Related Business Information Responsibilities

The [approved assurance definitions](../../02-strategy/capabilities/business-enabling-capabilities.md#health-service-assurance-approved-business-enabling-capabilities) directly support the following responsibility demarcations. They supplement the existing matrix without inventing named information assets, assigning an Information Object identity or commencing Information Architecture. The [capability-scoped assurance view](../behaviours/health-service-assurance.md#3-direct-capability-scoped-responsibility-consequences) documents the behaviour supporting each responsibility; discrete Function/Process decomposition remains unresolved.

| Established responsibility context | Directly supported assurance-related information responsibility | Preserved boundary / unresolved authority |
| :--- | :--- | :--- |
| **Assurance Design** | Defines how satisfaction is assured: assurance disposition, applicable criteria, required evidence and evaluation expectations. | Does not own or redefine the governing requirement. Responsibility for approving a disposition or establishing its governing authority is not derived. No asset name or detailed information lifecycle is assigned. |
| **Assurance Criteria Management** | Establishes and manages reusable, governed, temporally identifiable assurance criteria, including applicable evidence and completeness expectations where defined. | Criteria do not silently alter, weaken, strengthen or replace their source requirement; not all criteria must derive from formal Governance. Criterion approval authority, detailed lifecycle and retention responsibilities remain unresolved. |
| **Governed Assurance** | Owns the contextual association that particular information constitutes evidence for a particular assurance purpose; establishes its assessments, findings, exceptions and conclusions while preserving evidence insufficiency. | Does not acquire source-information ownership, originating authority or control of the subject. Additional asset decomposition, custody, conclusion approval/revision authority, lifecycle and retention rules remain unresolved. |

The [Guardian Role](../actors-roles/roles.md#guardian-governed-assurance) establishes evaluation and conclusion responsibility; a Role assignment does not transfer capability ownership of source information or confer clinical authority. Legal mandates in Client Relationships and clinical review, clinical qualification, clinical document and clinical-information authority remain distinct from governed assurance.

Evidence inclusion is contextual, potentially temporal and version-specific. Its association must distinguish the information state relevant to assurance from the current source state. Evaluation uses temporally appropriate evidence and criteria. These obligations do not select a storage, replication or preservation mechanism or make all operational observations permanent evidence.

Provenance, audit information, evidence, assessment, findings/conclusions, Business Outcomes and the operational response remain distinct. Insufficient assurance evidence establishes neither satisfaction nor non-satisfaction; it is distinct from the assurance activity's own execution uncertainty or failure. A governed assurance conclusion does not establish clinical adequacy, clinical correctness or professional judgement. No assurance information taxonomy, representation, persistence structure or downstream information model is derived.
