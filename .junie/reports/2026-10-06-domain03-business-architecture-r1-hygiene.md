# Domain 03 Business Architecture R1 — Hygiene Report

## 1. Task Goal

The goal of this task was to perform a strictly bounded architectural hygiene pass over the accepted R1 Business Architecture documentation under:

`docs/markdown/03-business-architecture`

The R1 Business Architecture is **ACCEPTED** and was **not** redesigned, extended, restructured, reinterpreted, or re-derived. The hygiene pass focused exclusively on:
1. Correcting invented or candidate Feature labels back to canonical Strategy Feature names from `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`.
2. Eliminating downstream Application, Integration, Technology, and implementation-specific terminology, abstracting technical mechanisms back to pure business semantics.
3. Correcting overly restrictive wording concerning Business Functions and data ownership to ensure Function ownership does not imply ownership of all operated-upon information.
4. Correcting the definition of Feature to consistently treat Feature as a finer-grained specialisation of Capability beneath L3 that inherits Capability semantics.
5. Preserving all otherwise accepted R1 architecture and documentation with zero net change to functions, services, processes, actors, roles, collaborations, interactions, and information responsibility assignments.

---

## 2. Files Reviewed

All 15 documents within `docs/markdown/03-business-architecture/` were systematically reviewed:
1. `docs/markdown/03-business-architecture/README.md`
2. `docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md`
3. `docs/markdown/03-business-architecture/actors-roles/actors.md`
4. `docs/markdown/03-business-architecture/actors-roles/roles.md`
5. `docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md`
6. `docs/markdown/03-business-architecture/collaborations-interactions/interactions.md`
7. `docs/markdown/03-business-architecture/behaviours/index.md`
8. `docs/markdown/03-business-architecture/behaviours/01-entity-management.md`
9. `docs/markdown/03-business-architecture/behaviours/02-service-administration.md`
10. `docs/markdown/03-business-architecture/behaviours/03-service-delivery.md`
11. `docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md`
12. `docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md`
13. `docs/markdown/03-business-architecture/processes/business-processes.md`
14. `docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md`
15. `docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md`

Additionally, `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md` was used as the authoritative hierarchy source.

---

## 3. Files Modified

The following 8 files were modified during the hygiene pass:
1. `docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md`
2. `docs/markdown/03-business-architecture/behaviours/01-entity-management.md`
3. `docs/markdown/03-business-architecture/behaviours/02-service-administration.md`
4. `docs/markdown/03-business-architecture/behaviours/03-service-delivery.md`
5. `docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md`
6. `docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md`
7. `docs/markdown/03-business-architecture/processes/business-processes.md`
8. `docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md`
9. `docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md`

*(Note: `README.md`, `actors.md`, `roles.md`, `collaborations.md`, `interactions.md`, and `behaviours/index.md` were reviewed and verified to already conform strictly to business semantics and architectural axioms).*

---

## 4. Canonical Feature Corrections

Invented or candidate feature names across the behaviour specifications were reconciled with canonical Strategy features (`FEAT-*`) in `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`, while preserving all underlying accepted Business Functions and exposed Business Services:

| File | Previous Candidate Feature Heading | Canonical Strategy Feature Restored | Strategy ID | Underlying Function & Service Preserved |
| :--- | :--- | :--- | :--- | :--- |
| `01-entity-management.md` | `Subject Context Establishment` | `Subject Context Binding` | `FEAT-EM-05` | Yes |
| `01-entity-management.md` | `Demographic Profile Governance` | `Demographic Context Governance` | `FEAT-EM-06` | Yes |
| `01-entity-management.md` | `Relationship Association` | `Next-of-Kin & Guardian Association` | `FEAT-EM-07` | Yes |
| `01-entity-management.md` | `Legal Authority Governance` | `Relationship Validity Tracking` | `FEAT-EM-08` | Yes |
| `01-entity-management.md` | `Confidentiality Restrictions` | `Confidentiality Flag Enforcement` | `FEAT-EM-10` | Yes |
| `01-entity-management.md` | `Practitioner Verification` | `National Practitioner Verification` | `FEAT-EM-11` | Yes |
| `01-entity-management.md` | `Practitioner Role Maintenance` | `Role & Specialty Modeling` | `FEAT-EM-13` | Yes |
| `01-entity-management.md` | `Clinical Privilege Verification` | `Clinical Privileging Record` | `FEAT-EM-16` | Yes |
| `01-entity-management.md` | `Organisation Verification` | `National Organisation Verification` | `FEAT-EM-17` | Yes |
| `01-entity-management.md` | `Organisation Profile Governance` | `Organisation Profile Management` | `FEAT-EM-18` | Yes |
| `01-entity-management.md` | `Organisational Hierarchy Maintenance` | `Hierarchical Structure Navigation` | `FEAT-EM-19` | Yes |
| `01-entity-management.md` | `Organisation Contact Governance` | `Directory Contact Management` | `FEAT-EM-20` | Yes |
| `01-entity-management.md` | `Location Master Maintenance` | `Location Geospatial & Address Resolution` | `FEAT-EM-21` | Yes |
| `01-entity-management.md` | `Physical Location Hierarchy` | `Physical Hierarchy Modeling` | `FEAT-EM-22` | Yes |
| `01-entity-management.md` | `Care-Place Definition` | `Atomic Care-Place Modeling` | `FEAT-EM-23` | Yes |
| `01-entity-management.md` | `Health Service Definition` | `Service Catalogue Publishing` | `FEAT-EM-24` | Yes |
| `01-entity-management.md` | `Service / Location / Provider Mapping` | `Service-Location-Provider Binding` | `FEAT-EM-25` | Yes |
| `01-entity-management.md` | `Service Availability Governance` | `Operating Schedule Specification` | `FEAT-EM-26` | Yes |
| `01-entity-management.md` | `Service Eligibility Rules` | `Eligibility & Catchment Specification` | `FEAT-EM-27` | Yes |
| `01-entity-management.md` | `Device Definition Governance` | `Device Type Specification` | `FEAT-EM-28` | Yes |
| `01-entity-management.md` | `Device Instance Identity` | `Device Serial Tracking` | `FEAT-EM-29` | Yes |
| `01-entity-management.md` | `Device Association Maintenance` | `Device-Patient/Location Assignment` | `FEAT-EM-30` | Yes |
| `01-entity-management.md` | `Device Endpoint Configuration` | `Device Endpoint Registration` | `FEAT-EM-31` | Yes |
| `02-service-administration.md` | `Demographic Change Intake` | `Demographic Update Ingestion` | `FEAT-SA-01` | Yes |
| `02-service-administration.md` | `Governed Demographic Distribution` | `Demographic Change Distribution` | `FEAT-SA-02` | Yes |
| `02-service-administration.md` | `Referral Intake` | `Referral Document Ingestion` | `FEAT-SA-03` | Yes |
| `02-service-administration.md` | `Referral Context Assembly` | `Referral Supporting Information` | `FEAT-SA-04` | Yes |
| `02-service-administration.md` | `Referral Progression & Triage` | `Referral Status & Outcome Tracking` | `FEAT-SA-05` | Yes |
| `02-service-administration.md` | `Appointment Event Intake` | `Appointment Notification Ingestion` | `FEAT-SA-06` | Yes |
| `02-service-administration.md` | `Appointment Status Synchronisation` | `Appointment Status Synchronization` | `FEAT-SA-07` | Yes |
| `02-service-administration.md` | `Encounter Context Establishment` | `Encounter Context Creation` | `FEAT-SA-08` | Yes |
| `02-service-administration.md` | `Encounter Care-Place Association` | `Encounter Bed/Location Association` | `FEAT-SA-10` | Yes |
| `02-service-administration.md` | `Order Requisition Intake` | `Order Request Ingestion` | `FEAT-SA-11` | Yes |
| `02-service-administration.md` | `Order Routing & Destination Resolution` | `Order Destination Resolution & Routing` | `FEAT-SA-12` | Yes |
| `02-service-administration.md` | `Closed-Loop Order Progression` | `Order Closed-Loop Progression Tracking` | `FEAT-SA-13` | Yes |
| `02-service-administration.md` | `Order Modification & Cancellation` | `Order Cancellation & Modification Coordination` | `FEAT-SA-14` | Yes |
| `02-service-administration.md` | `Diagnostic Report Binding` | `Diagnostic Report Ingestion & Binding` | `FEAT-SA-16` | Yes |
| `02-service-administration.md` | `Medication Administration Event Ingestion` | `Medication Administration Record (MAR) Ingestion` | `FEAT-SA-20` | Yes |
| `02-service-administration.md` | `Procedure Request Intake` | `Procedure Booking Ingestion` | `FEAT-SA-21` | Yes |
| `02-service-administration.md` | `Clinical Document Intake` | `Clinical Document Ingestion` | `FEAT-SA-23` | Yes |
| `02-service-administration.md` | `Document Lifecycle Governance` | `Document Versioning & Supersession` | `FEAT-SA-24` | Yes |
| `02-service-administration.md` | `Care Plan Ingestion` | `Care Plan Ingestion & Distribution` | `FEAT-SA-26` | Yes |
| `02-service-administration.md` | `Business Acknowledgement Tracking` | `Delivery Acknowledgement Tracking` | `FEAT-SA-28` | Yes |
| `02-service-administration.md` | `Communication Transaction Audit` | `Communication Audit Logging` | `FEAT-SA-29` | Yes |
| `03-service-delivery.md` | `Primary Care Encounter Context Correlation` | `General Practice Shared Record Ingestion` | `FEAT-SD-01` | Yes |
| `03-service-delivery.md` | `Chronic Disease Shared Management` | `Primary Care Notification Dispatch` | `FEAT-SD-02` | Yes |
| `03-service-delivery.md` | `Acute Admission Clinical Context Assembly` | `Acute Care Clinical Context Provision` | `FEAT-SD-04` | Yes |
| `03-service-delivery.md` | `Critical Result Notification Routing` | `Acute Clinical State Event Capture` | `FEAT-SD-03` | Yes |
| `03-service-delivery.md` | `ED Triage Context Integration` | `Emergency Department Arrival & Triage Ingestion` | `FEAT-SD-05` | Yes |
| `03-service-delivery.md` | `Emergency Care Fast-Path Record Access` | `Emergency Care Cross-Facility Correlation` | `FEAT-SD-06` | Yes |
| `03-service-delivery.md` | `Inpatient Multidisciplinary Ward Round Support` | `Inpatient Rounding Context Provision` | `FEAT-SD-07` | Yes |
| `03-service-delivery.md` | `Clinical Discharge Dossier Assembly` | `Inpatient Clinical Progression Tracking` | `FEAT-SD-08` | Yes |
| `03-service-delivery.md` | `Diagnostic Worklist Order Ingestion` | `Laboratory & Imaging Result Distribution` | `FEAT-SD-09` | Yes |
| `03-service-delivery.md` | `Diagnostic Observation Publication` | `Diagnostic History Consolidation` | `FEAT-SD-10` | Yes |
| `03-service-delivery.md` | `Consolidated Medication History Projection` | `Medication History Reconciliation Ingestion` | `FEAT-SD-11` | Yes |
| `03-service-delivery.md` | `Adverse Drug Reaction & Allergy Verification` | `Medication Administration Tracking` | `FEAT-SD-12` | Yes |
| `03-service-delivery.md` | `Immunisation History Syndication` | `Immunisation Event Submission` | `FEAT-SD-13` | Yes |
| `03-service-delivery.md` | `Population Screening Reminder Context` | `Screening Recall Notification Distribution` | `FEAT-SD-14` | Yes |
| `03-service-delivery.md` | `Community Clinical Visit Synchronization` | `Community Encounter Ingestion` | `FEAT-SD-15` | Yes |
| `03-service-delivery.md` | `Home-Based Care Support Network Binding` | `Community Care Plan Synchronization` | `FEAT-SD-16` | Yes |
| `03-service-delivery.md` | `Offline / Disconnected Clinical Capture Synchronization` | `Outreach Encounter Outcome Ingestion` | `FEAT-SD-18` | Yes |
| `03-service-delivery.md` | `Virtual Consultation Context Correlation` | `Virtual Care Session Context Binding` | `FEAT-SD-20` | Yes |
| `03-service-delivery.md` | `Remote Physiological Telemetry Ingestion` | `Remote Telemetry Ingestion` | `FEAT-SD-19` | Yes |
| `04-health-service-operations.md` | `Clinic Queue Progression` | `Operational Queue Progression` | `FEAT-HSO-02` | Yes |
| `04-health-service-operations.md` | `Ward Shift Coordination` | `Ward Patient Flow Coordination` | `FEAT-HSO-03` | Yes |
| `04-health-service-operations.md` | `Theatre Case Progression` | `Operating Theatre Case Progression` | `FEAT-HSO-05` | Yes |
| `04-health-service-operations.md` | `ED Operational Flow Telemetry` | `ED Length of Stay Tracking` | `FEAT-HSO-08` | Yes |
| `04-health-service-operations.md` | `Ambulatory Session Management` | `Outpatient Arrival Registration` | `FEAT-HSO-09` | Yes |
| `04-health-service-operations.md` | `Care-Place Availability Tracking` | `Bed Availability Tracking` | `FEAT-HSO-11` | Yes |
| `04-health-service-operations.md` | `Bed Turnover Progression` | `Bed State Progression` | `FEAT-HSO-12` | Yes |
| `04-health-service-operations.md` | `Specialised Equipment Tracking` | `Mobile Clinical Equipment Tracking` | `FEAT-HSO-14` | Yes |
| `04-health-service-operations.md` | `Regional Capacity Telemetry` | `Operational Capacity Metric Aggregation` | `FEAT-HSO-15` | Yes |
| `04-health-service-operations.md` | `Mobile Staff Presence & Zone Tracking` | `Mobile Worker Task Dispatch` | `FEAT-HSO-16` | Yes |
| `04-health-service-operations.md` | `On-Call Tier Resolution` | `Active On-Call Provider Resolution` | `FEAT-HSO-18` | Yes |
| `04-health-service-operations.md` | `Operational Work Order Dispatch` | `Work Item Instantiation` | `FEAT-HSO-19` | Yes |
| `04-health-service-operations.md` | `Operational Work Progression` | `Work Progress Oversight & Escalation` | `FEAT-HSO-22` | Yes |
| `04-health-service-operations.md` | `Operational Credential Checkpoint` | `Operational Credential Check` | `FEAT-HSO-23` | Yes |
| `04-health-service-operations.md` | `Patient Transfer Coordination` | `Transport Dispatch & Progress Tracking` | `FEAT-HSO-25` | Yes |
| `04-health-service-operations.md` | `Specimen & Asset Courier Tracking` | `Pathology Specimen Transport Tracking` | `FEAT-HSO-26` | Yes |
| `04-health-service-operations.md` | `Discharge Coordination Progression` | `Discharge Readiness & Coordination Oversight` | `FEAT-HSO-28` | Yes |
| `05-intrinsic-enablement.md` | `Longitudinal Record Assembly` | `Longitudinal Health Record (LHR) Assembly` | `FEAT-ISE-01` | Yes |
| `05-intrinsic-enablement.md` | `Active Clinical Context Management` | `Active Clinical Record Access` | `FEAT-ISE-02` | Yes |
| `05-intrinsic-enablement.md` | `Durable Record Preservation` | `Durable Clinical Record Preservation` | `FEAT-ISE-03` | Yes |
| `05-intrinsic-enablement.md` | `Information Submission Ingress` | `Direct Submission Ingestion & Delivery` | `FEAT-ISE-04` | Yes |
| `05-intrinsic-enablement.md` | `Mediated Information Retrieval` | `Clinical Record Retrieval Mediation` | `FEAT-ISE-05` | Yes |
| `05-intrinsic-enablement.md` | `Addressed Information Distribution` | `Addressed Clinical Distribution` | `FEAT-ISE-06` | Yes |
| `05-intrinsic-enablement.md` | `Information Change Syndication` | `Event-Driven Syndication` | `FEAT-ISE-07` | Yes |
| `05-intrinsic-enablement.md` | `Clinical Record Search` | `Federated Clinical Query` | `FEAT-ISE-08` | Yes |
| `05-intrinsic-enablement.md` | `Record Content Retrieval` / `Access-Qualified Result Filtering` | `Access-Controlled Information Filtering` | `FEAT-ISE-09` | Yes |
| `05-intrinsic-enablement.md` | `Protocol Transport Mediation` | `Standards-Based Boundary Exchange` | `FEAT-ISE-10` | Yes |
| `05-intrinsic-enablement.md` | `Access Authority Evaluation` | `Health Information Access & Consent Control` | `FEAT-ISE-11` | Yes |
| `05-intrinsic-enablement.md` | `Consent Constraint Enforcement` / `Security Context Propagation` | `Security Context Propagation` | `FEAT-ISE-12` | Yes |
| `05-intrinsic-enablement.md` | `Security-Significant Audit Recording` | `Non-PHI Compliance Auditing` | `FEAT-ISE-13` | Yes |
| `05-intrinsic-enablement.md` | `Cross-Ontology Concept Mapping` | `Bidirectional Terminology Mapping` | `FEAT-ISE-15` | Yes |
| `05-intrinsic-enablement.md` | `Care-Team Collaboration Coordination` | `Care-Team Clinical Collaboration Coordination` | `FEAT-ISE-16` | Yes |
| `05-intrinsic-enablement.md` | `Collaboration LHR Context Projection` | `In-Conversation LHR Query Resolution` | `FEAT-ISE-17` | Yes |
| `05-intrinsic-enablement.md` | `Collaboration Metadata Governance` | `Zero-PHI Collaboration Metadata Governance` | `FEAT-ISE-18` | Yes |
| `05-intrinsic-enablement.md` | `Operational Team Coordination` | `Operational Team Chat Enablement` | `FEAT-ISE-19` | Yes |
| `05-intrinsic-enablement.md` | `Work Order Coordination` | `Work Order Progression` | `FEAT-ISE-20` | Yes |
| `05-intrinsic-enablement.md` | `To Do Coordination` | `To Do Decision & Approval Coordination` | `FEAT-ISE-21` | Yes |
| `05-intrinsic-enablement.md` | `Synthetic Task Execution Coordination` | `Synthetic Task Orchestration` | `FEAT-ISE-22` | Yes |
| `05-intrinsic-enablement.md` | `Activity Timeout & Escalation Supervision` | `Workflow Timeout & Escalation Oversight` | `FEAT-ISE-23` | Yes |
| `05-intrinsic-enablement.md` | `Operational Schedule Projection` | `Schedule Projection Integration` | `FEAT-ISE-24` | Yes |
| `05-intrinsic-enablement.md` | `Temporal Activity Correlation` | `Temporal Event Correlation` | `FEAT-ISE-25` | Yes |
| `05-intrinsic-enablement.md` | `Contextual Health & Operational Information Presentation` | `Longitudinal Clinical & Operational Presentation` | `FEAT-ISE-26` | Yes |
| `05-intrinsic-enablement.md` | `Semantic Information Governance` | `Information Standards & Semantic Governance` | `FEAT-ISE-27` | Yes |

---

## 5. Business / Implementation Boundary Corrections

The following implementation-specific phrasing was abstracted back to pure business semantics:

| File | Implementation-Specific Wording Removed | Business-Semantic Replacement |
| :--- | :--- | :--- |
| `02-service-administration.md` | `Appointment Event Cache` | `Appointment Event History` |
| `02-service-administration.md` | `Ingests CDA/FHIR/PDF clinical documents from authoring systems.` | `Receives clinical documents from information suppliers.` |
| `02-service-administration.md` | `Technical ACK: Confirms wire/transport receipt (e.g., HTTP 200, MLLP MSA-AA).` | `Technical ACK: Confirms technical/transport receipt by participating systems.` |
| `05-intrinsic-enablement.md` | `in low-latency active state` | `for active care coordination` |
| `05-intrinsic-enablement.md` | `Delivers parsed clinical payloads to requesting applications` | `Delivers structured clinical information to requesting applications` |
| `05-intrinsic-enablement.md` | `Projection Cache` | `Projection State` |
| `05-intrinsic-enablement.md` | `Encapsulates standards-based protocols (e.g., HL7 v2 MLLP, FHIR REST, DICOMweb) to mediate technical transmission across network perimeters.` | `Encapsulates standards-based health information communication to mediate technical transmission between participating parties.` |
| `05-intrinsic-enablement.md` | `Handles protocol serialization, session handshakes, and transport-level error recovery.` | `Handles standards-based communication mediation, transaction coordination, and technical error handling.` |
| `05-intrinsic-enablement.md` | `messaging rooms` / `Collaboration Clinical Card Resolution` / `Collaboration Room Governance Service` | `collaboration spaces` / `Collaboration Clinical Summary Resolution` / `Collaboration Space Governance Service` |
| `05-intrinsic-enablement.md` | `Operational Room Index` | `Operational Channel Index` |
| `05-intrinsic-enablement.md` | `(internally a synthetic task) executed by platform automation, daemons, or services.` | `executed by automated platform services.` |
| `05-intrinsic-enablement.md` | `orchestrates automated system workflows, retries, and compensation logic.` | `orchestrates automated system workflows and operational progression.` |
| `05-intrinsic-enablement.md` | `Delivers presentation-ready view models to clinical and administrative frontends.` | `Delivers contextual health and operational information for presentation.` |
| `05-intrinsic-enablement.md` | `Presentation Layout Schema` | `Presentation Layout Specification` |
| `processes/business-processes.md` | `Draft / Enqueued` $\to$ `Executing` $\to$ `Awaiting-Dependency` $\to$ `Completed` $\to$ `Retrying / Failed` | `Draft / Scheduled` $\to$ `Executing` $\to$ `Awaiting-Dependency` $\to$ `Completed` $\to$ `Progression Stalled / Failed` |
| `processes/business-processes.md` | `Automated task placed on execution queue... exponential backoff retry` | `Automated task instantiated and scheduled for processing... scheduled recovery attempt` |
| `information-responsibility.md` | `CDA/PDF document repository` | `structured and narrative document repository` |
| `information-responsibility.md` | `clinical payload content of messages` | `clinical content of messages` |
| `information-responsibility.md` | `transported message payloads` | `transported message content` |
| `information-responsibility.md` | `UI view models, responsive presentation schemas` | `responsive presentation structures, presentation layouts` |
| `information-responsibility.md` | `Caches, search projections, exchange envelopes, and UI view models` | `Active states, search projections, exchange wrappers, and presentation views` |
| `cross-capability-dependencies.md` | `against governed CDA/FHIR profiles.` | `against governed interchange profiles.` |
| `cross-capability-dependencies.md` | `Collaboration Clinical Card Resolution` | `Collaboration Clinical Summary Resolution` |

---

## 6. Function / Information Ownership Corrections

Wording in `metamodel/business-architecture-metamodel.md` was corrected to decouple Function ownership from data ownership and explicitly accommodate cross-capability service consumption:

- **Previous Restrictive Wording**:
  > *"A Business Function describes what the capability does internally to discharge its architectural mandate. It operates directly upon the information and resources owned by that capability."*
- **Corrected Wording Applied**:
  > *"A Business Function represents behaviour delivered within the responsibility boundary of its owning Capability or Feature. A Business Function may operate upon:*
  > *- information owned by its Capability / Feature;*
  > *- information obtained through Services exposed by another Capability / Feature;*
  > *- information used under an explicit cross-capability dependency.*
  > *Therefore, **ownership of a Function does not imply ownership of every information item upon which that Function operates**."*
- **Reaffirmed Information Ownership Principle**:
  > *"Access, use, transformation, transport, search, presentation, caching, persistence, or coordination of information does not transfer architectural ownership."*

---

## 7. Feature Definition Correction

The definition of Feature in `metamodel/business-architecture-metamodel.md` was canonicalised to define Feature as a finer-grained specialisation of Capability beneath L3, removing atomic system behaviour descriptions:

- **Previous Wording**:
  > *"Feature: The smallest useful statement of required system-enabled behaviour. A Feature is a finer-grained specialisation/subclass of Capability for architectural modelling purposes. It inherits full Capability semantics."*
- **Corrected Wording Applied**:
  > *"Feature: A **Feature** is a finer-grained specialisation of Capability beneath the L3 Capability hierarchy and inherits Capability semantics."*

---

## 8. Validation Results

A comprehensive validation scan was executed across all 15 files in Domain 03:

- **Invented Canonical Feature Labels Remaining**: `0`
- **Internal Harmonia Application Component Leakage (`Mneme`, `Mnemosyne`, `Ponos`, `Pylai`, `Calliope`, `Iris`, `Themis`, `Agora`, `Petasos`, `Paradeigma`, `Kleio`, `Ergon`, `Erga`, `Praxis`)**: `0`
- **Implementation Terminology Matches Reviewed**: `12` matches reviewed across the suite; all confirmed to represent either explicit anti-pattern/non-conflation guardrails (e.g. *"not a FHIR resource"*) or legitimate healthcare operational queue concepts (e.g. *clinic waiting line, portering dispatch queue*).
- **Unexplained Implementation Leakage Remaining**: `0`
- **Architectural Change Counts (Zero Net Delta)**:
  - Functions added: `0`
  - Functions removed: `0`
  - Services added: `0`
  - Services removed: `0`
  - Processes added: `0`
  - Processes removed: `0`
  - Actors changed: `0`
  - Roles changed: `0`
  - Collaborations changed: `0`
  - Interactions changed: `0`
  - Information responsibility assignments changed: `0`

---

## 9. Items Requiring Future Architectural Review

No unresolvable architectural defects or ambiguities were identified during this hygiene pass. All corrections remained strictly within documentation hygiene boundaries and preserved the accepted R1 architecture.

---

## 10. Target Outcome Assessment

| # | Target Outcome | Status | Explanation |
| :--- | :--- | :--- | :--- |
| 1 | Every Capability and Feature name in Domain 03 corresponds to the canonical Strategy hierarchy. | **PASS** | Reconciled 100% of Feature headings across all behaviour files against `02-strategy/capabilities/business-enabling-capabilities.md`. |
| 2 | Candidate Business Functions have NOT accidentally been promoted or renamed as Features. | **PASS** | Restored canonical Strategy Capability/Feature headings while retaining accepted Functions and exposed Services beneath them. |
| 3 | Domain 03 contains no unnecessary Application, Integration, Technology, or implementation-specific terminology. | **PASS** | Removed protocol references (`HL7 v2 MLLP`, `FHIR REST`, `DICOMweb`, `HTTP 200`, `MLLP MSA-AA`), format specifics (`CDA`, `PDF`), and technical jargon (`payload`, `view model`, `retry`, `compensation logic`, `cache`). |
| 4 | Business Functions are correctly described as behaviour owned by a Capability/Feature without implying ownership of all operated-upon information. | **PASS** | Updated the metamodel to explicitly allow Functions to operate on owned information, service-obtained information, or dependency information. |
| 5 | Feature is consistently defined in Domain 03 as a finer-grained specialisation of Capability beneath L3. | **PASS** | Canonicalised the Feature definition in the metamodel and eliminated system-enabled behaviour phrasing. |
| 6 | Accepted Functions, Services, Processes, Actors, Roles, Collaborations, Interactions, information responsibilities, and dependencies remain architecturally unchanged. | **PASS** | Zero net delta confirmed across all architectural invariants. |
| 7 | No new architecture is introduced. | **PASS** | The hygiene pass made only targeted wording and header corrections without inventing or expanding architecture. |
| 8 | A concise hygiene report is authored at `.junie/reports/YYYY-MM-DD-domain03-business-architecture-r1-hygiene.md`. | **PASS** | Authored this comprehensive report conforming to all 10 required sections. |
