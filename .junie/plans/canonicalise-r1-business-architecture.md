---
sessionId: session-261006-160254-zclk
---

# Requirements

### Overview & Goals

The objective of this task is to perform a **strictly bounded architectural hygiene pass** over the accepted R1 Business Architecture documentation under:

`docs/markdown/03-business-architecture`

The existing Domain 03 architecture is **ACCEPTED**. This task does **NOT** redesign, extend, restructure, reinterpret, or re-derive the Business Architecture. The sole purpose is to correct documentation hygiene issues identified during human review of the completed R1 Domain 03 documentation.

The required corrections are strictly limited to:
1. Removing or correcting invented / renamed Feature names that are not canonical Strategy Features, restoring the canonical Strategy Capability / Feature context while preserving underlying Functions and Services.
2. Removing Application, Integration, Technology, and implementation-specific terminology that has leaked into Business Architecture, abstracting technical mechanisms back to pure business semantics.
3. Correcting overly restrictive wording concerning Business Functions and information ownership so that Function ownership does not imply ownership of all operated-upon information.
4. Correcting the Domain 03 definition of Feature so that Feature is consistently treated as a finer-grained specialisation of Capability beneath L3 inheriting Capability semantics.
5. Preserving all otherwise accepted R1 architecture and documentation (Actors, Roles, Collaborations, Interactions, Functions, Services, Processes, Information Responsibilities, Dependencies).
6. Authoring a concise hygiene report at `.junie/reports/YYYY-MM-DD-domain03-business-architecture-r1-hygiene.md`.

---

### Scope

#### In Scope
- Reviewing and applying targeted hygiene corrections across all 15 documents in `docs/markdown/03-business-architecture/`:
  - `README.md`: Align Feature definition, remove technology leakage, ensure pure business framing.
  - `metamodel/business-architecture-metamodel.md`: Correct Feature definition (finer-grained Capability specialisation), correct Function semantics and information ownership rules.
  - `actors-roles/actors.md`: Audit for implementation terminology leakage and preserve actor-role distinctions.
  - `actors-roles/roles.md`: Audit and verify role definitions against implementation terminology.
  - `collaborations-interactions/collaborations.md`: Audit collaborative structures and context qualifiers.
  - `collaborations-interactions/interactions.md`: Audit interaction definitions and boundary rules, removing technical transport phrasing (e.g., in notification and exchange definitions).
  - `behaviours/index.md`: Ensure framing reflects canonical Strategy Capability / Feature hierarchy.
  - `behaviours/01-entity-management.md`: Verify all Feature headings match canonical Strategy Features; abstract technical endpoint/credential phrasing.
  - `behaviours/02-service-administration.md`: Correct invented feature labels (e.g., `Demographic Change Intake`, `Referral Context Assembly`, `Clinical Document Intake`), remove format/protocol specifics (`CDA`, `PDF`, `HTTP 200`, `MLLP MSA-AA`, `payload`, `cache`).
  - `behaviours/03-service-delivery.md`: Audit clinical service enablement contexts for technical terminology leakage while preserving the care-enablement boundary.
  - `behaviours/04-health-service-operations.md`: Verify Feature headings (e.g., `Clinic Queue Progression` vs canonical `Clinic & Practice Operations` / `Clinic Operational Queue Management`), remove technical queue/telemetry implementation phrasing.
  - `behaviours/05-intrinsic-enablement.md`: Correct invented feature labels (`Protocol Transport Mediation`, `Clinical Record Search`, `Operational Team Coordination`), remove protocol names (`HL7 v2 MLLP`, `FHIR REST`, `DICOMweb`, `network perimeter`, `view model`, `frontend`, `payload`), and refine synthetic task descriptions.
  - `processes/business-processes.md`: Audit the 16 principal processes to eliminate implementation leakage (`queue`, `cache`, `retry`, `payload`) while preserving exact process lifecycles and state transitions.
  - `information-responsibility/information-responsibility.md`: Correct information responsibility tables and text to remove technical artifact names (`cache`, `queue`, `payload`) and reinforce capability-derived ownership.
  - `dependencies/cross-capability-dependencies.md`: Audit dependency descriptions and Mermaid diagrams to ensure pure business service consumption semantics.
- Authoring the completion hygiene report at `.junie/reports/YYYY-MM-DD-domain03-business-architecture-r1-hygiene.md`.

#### Out of Scope & Explicit Non-Goals
- Modifying Domain 01 (Motivation), Domain 02 (Strategy), Domain 04 (Information Architecture), or any other architecture domain.
- Adding, removing, or renaming canonical Capabilities.
- Adding or removing Features, Functions, Services, Processes, Actors, Roles, Collaborations, Interactions, or Information Responsibilities.
- Modifying source code, POM files, configurations, or unit/architecture tests.
- Broad prose rewriting, gratuitous restructuring, or stylistic churn.
- Inventing new architecture to resolve ambiguous edge cases (unresolvable issues must be recorded under *Items Requiring Future Architectural Review*).

---

### User Stories

- **As an Enterprise Architect & Reviewer**, I want Domain 03 to reference only canonical Strategy Capabilities and Features so that traceability across architecture tiers remains 100% rigorous and uncorrupted by candidate or transient names.
- **As a Domain Modeler**, I want Business Functions and Services described with authentic business semantics rather than downstream implementation jargon (e.g. wire protocols, data formats, UI view models) so that Domain 03 remains technology-neutral and vendor-independent.
- **As a Solution Architect**, I want Function semantics to explicitly allow operating upon information from exposed services and dependencies without implying that the Function's owning Capability must own all operated-upon data.
- **As a System Architect**, I want Feature defined consistently as a finer-grained Capability specialisation inheriting Capability semantics rather than merely an atomic system behaviour statement.

---

### Functional Requirements

1. **Canonical Capability & Feature Alignment**:
   - Every Capability and Feature name in Domain 03 must correspond exactly to the canonical Strategy hierarchy defined in `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`.
   - Candidate business function or descriptive names (such as `Demographic Change Intake`, `Referral Context Assembly`, `Protocol Transport Mediation`, `Clinical Record Search`, `Operational Team Coordination`) must NOT be represented as canonical Features.
   - When an invented feature heading is identified, restore the canonical Strategy Capability / Feature context while preserving the underlying accepted Business Function and exposed Service.
2. **Feature Definition Consistency**:
   - Define Feature in Domain 03 exclusively as: *A Feature is a finer-grained specialisation of Capability beneath the L3 Capability hierarchy and inherits Capability semantics.*
   - Remove or rewrite any definitions describing Feature primarily as "the smallest useful statement of required system-enabled behaviour" or treating Feature merely as atomic system behaviour.
3. **Function Semantics & Information Ownership Nuance**:
   - Replace restrictive wording claiming a Function operates solely on information owned by that Capability with: *A Business Function represents behaviour delivered within the responsibility boundary of its owning Capability or Feature. A Business Function may operate upon information owned by its Capability/Feature, information obtained through Services exposed by another Capability/Feature, or information used under an explicit cross-capability dependency.*
   - Explicitly affirm that *ownership of a Function does not imply ownership of every information item upon which that Function operates*.
   - Reaffirm that *access, use, transformation, transport, search, presentation, caching, persistence, or coordination of information does not transfer architectural ownership*.
4. **Elimination of Implementation Terminology Leakage**:
   - Contextually review and replace implementation-specific terminology across all Domain 03 documents:
     - Data formats / encodings: `CDA`, `PDF`, `JSON`, `payload`, `schema` $\to$ abstract to *clinical document*, *structured information*, *business representation*.
     - Communication protocols: `HL7 v2 MLLP`, `FHIR REST`, `DICOMweb`, `network perimeter`, `HTTP 200`, `MLLP MSA-AA` $\to$ abstract to *standards-based health information communication*, *technical receipt*, *participating parties*.
     - UI / Client artifacts: `frontend`, `view model`, `clinical card`, `messaging room` $\to$ abstract to *contextual presentation*, *communication space*.
     - Technical execution mechanics: `retry`, `compensation logic`, `queue`, `cache` $\to$ abstract to *operational progression*, *delivery tracking*, *active state*.
   - Retain legitimate business/governance standards references where the standard itself has statutory, clinical, or governance significance (e.g. nationally governed identifiers, recognized terminology standards).
5. **Architectural Invariant Preservation (Zero Delta)**:
   - Ensure the hygiene pass results in exact zero net change to the accepted architecture:
     - Functions added/removed: 0 / 0
     - Services added/removed: 0 / 0
     - Processes added/removed: 0 / 0
     - Actors changed: 0
     - Roles changed: 0
     - Collaborations changed: 0
     - Interactions changed: 0
     - Information responsibility assignments changed: 0
6. **Hygiene Completion Report**:
   - Produce `.junie/reports/YYYY-MM-DD-domain03-business-architecture-r1-hygiene.md` conforming to the specified 10-section structure with PASS/PARTIAL/FAIL target outcome assessments.

---

### Non-Functional Requirements

- **Abstraction Purity**: Zero references to internal Harmonia application components (`Mneme`, `Mnemosyne`, `Ponos`, `Pylai`, `Calliope`, `Iris`, `Petasos`, `Themis`, `Agora`) or downstream technology constructs (Java classes, DDL tables, REST endpoints, Kubernetes resources).
- **Link & Anchor Integrity**: Maintain 100% resolution of internal Markdown relative hyperlinks across all files in `docs/markdown/03-business-architecture/` and back to `docs/markdown/02-strategy/`.
- **Minimal Invasive Modification**: Follow surgical, precise editing to maintain readability and avoid gratuitous prose churn.

# Technical Design

### Current State & Findings

The completed R1 Business Architecture baseline in `docs/markdown/03-business-architecture/` has been accepted, but human review highlighted specific documentation hygiene issues:

1. **Invented / Candidate Feature Names**:
   - `behaviours/02-service-administration.md`: Used `Feature: Demographic Change Intake` instead of canonical `Client Demographics Management` (`FEAT-SA-01`), `Feature: Referral Context Assembly` instead of canonical `Referral Ingestion & Registration` / `Referral Context Assembly` hierarchy, and `Feature: Clinical Document Intake` instead of canonical `Clinical Document Ingestion` (`FEAT-SA-19`).
   - `behaviours/05-intrinsic-enablement.md`: Used `Feature: Clinical Record Search` instead of canonical `Search & Query Services` (`FEAT-ISE-07`), `Feature: Protocol Transport Mediation` instead of canonical `Standards Communication Gateway` (`FEAT-ISE-09`), and `Feature: Operational Team Coordination` instead of canonical `Operational Collaboration Spaces` (`FEAT-ISE-18`).
2. **Implementation Terminology Leakage**:
   - Specific protocol references (`HL7 v2 MLLP`, `FHIR REST`, `DICOMweb`, `HTTP 200`, `MLLP MSA-AA`, `network perimeter`) exist in `behaviours/02-service-administration.md` and `behaviours/05-intrinsic-enablement.md`.
   - Technical data representation terms (`CDA`, `PDF`, `payload`, `schema`, `view model`, `frontend`) exist in `behaviours/02-service-administration.md`, `behaviours/05-intrinsic-enablement.md`, and `information-responsibility/information-responsibility.md`.
   - Technical execution terms (`retry`, `compensation logic`, `queue`, `cache`) appear in process and responsibility descriptions.
3. **Overly Restrictive Function Semantics**:
   - `metamodel/business-architecture-metamodel.md` line 56 states that a Function *"operates directly upon the information and resources owned by that capability"*, which conflicts with cross-capability service consumption where Functions operate upon information obtained through external services or dependencies.
4. **Feature Definition Inconsistency**:
   - `metamodel/business-architecture-metamodel.md` line 27 and `README.md` describe Feature as *"the smallest useful statement of required system-enabled behaviour"*, rather than consistently defining Feature as a finer-grained specialisation of Capability beneath L3.

---

### Key Architectural Decisions for Hygiene Corrections

1. **Decision 1: Canonical Feature Restoration**:
   - *Approach*: Reconcile every Feature header against `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`. Where an invented feature label was used, restore the canonical Strategy Feature name and ID while retaining the accepted Business Function and exposed Service underneath.
   - *Rationale*: Eliminates architectural drift and ensures 100% traceability between Strategy and Business Architecture.
2. **Decision 2: Business Abstraction of Implementation Jargon**:
   - *Approach*: Contextually abstract implementation mechanisms to business semantics:
     - Replace `HL7 v2 MLLP, FHIR REST and DICOMweb` $\to$ *standards-based health information communication*.
     - Replace `CDA/FHIR/PDF clinical documents` $\to$ *structured and narrative clinical documents*.
     - Replace `view models for frontends` $\to$ *contextual health and operational information for presentation*.
     - Replace `HTTP 200, MLLP MSA-AA` $\to$ *transport/technical receipt*.
     - Replace `queue/cache` $\to$ *active progression ledger / active state*.
   - *Rationale*: Preserves technology neutrality while clearly documenting business behaviour.
3. **Decision 3: Decouple Function Ownership from Data Ownership**:
   - *Approach*: Update the Function rule in the metamodel to explicitly state that a Function delivered within its capability boundary may operate upon owned information, service-obtained information, or dependency information.
   - *Rationale*: Prevents incorrect inferences that a Capability must own all information processed by its functions.
4. **Decision 4: Feature as Capability Specialisation**:
   - *Approach*: Canonicalise the Feature definition in Domain 03: *A Feature is a finer-grained specialisation of Capability beneath the L3 Capability hierarchy and inherits Capability semantics.*
   - *Rationale*: Re-establishes structural coherence in the capability hierarchy.
5. **Decision 5: Minimal Invasive Editing Guardrail**:
   - *Approach*: Make targeted search-and-replace edits to affected lines without restructuring document layouts, reformatting tables unnecessarily, or re-deriving architecture.
   - *Rationale*: Prevents unintentional regressions in accepted content.

---

### Planned Document Modifications

| Document | Planned Hygiene Corrections |
| :--- | :--- |
| `README.md` | Update Feature definition; verify absence of implementation terms. |
| `metamodel/business-architecture-metamodel.md` | Canonicalise Feature definition (line 27); update Function semantics and data operation nuance (line 56); review data responsibility text. |
| `actors-roles/actors.md` | Audit for implementation terminology leakage and preserve external system vs internal component distinction. |
| `actors-roles/roles.md` | Audit role definitions against technical terms. |
| `collaborations-interactions/collaborations.md` | Audit collaboration definitions and context qualifiers. |
| `collaborations-interactions/interactions.md` | Audit interaction categories and boundary rules, abstracting technical transport phrasing. |
| `behaviours/index.md` | Align overview text with Strategy capability hierarchy. |
| `behaviours/01-entity-management.md` | Verify Feature names match Strategy `FEAT-EM-*`; abstract technical endpoint phrasing. |
| `behaviours/02-service-administration.md` | Correct invented Feature names (Demographic Change Intake, Referral Context Assembly, Clinical Document Intake) to canonical `FEAT-SA-*`; abstract `CDA/PDF`, `HTTP 200 / MLLP MSA-AA`, `payload`, `cache`. |
| `behaviours/03-service-delivery.md` | Audit service delivery contexts for technical terminology while preserving the care-enablement boundary. |
| `behaviours/04-health-service-operations.md` | Verify Feature names against canonical `FEAT-HSO-*`; abstract technical queue/telemetry implementation phrasing. |
| `behaviours/05-intrinsic-enablement.md` | Correct invented Feature names (Protocol Transport Mediation, Clinical Record Search, Operational Team Coordination) to canonical `FEAT-ISE-*`; abstract `HL7 v2 MLLP`, `FHIR REST`, `DICOMweb`, `view model`, `frontend`, `payload`. |
| `processes/business-processes.md` | Audit 16 processes to eliminate technical execution jargon (`queue`, `cache`, `retry`, `payload`) while preserving exact states. |
| `information-responsibility/information-responsibility.md` | Abstract technical artifact names (`cache`, `queue`, `payload`) from information responsibility entries. |
| `dependencies/cross-capability-dependencies.md` | Audit dependency matrices and diagrams for pure business service consumption. |

---

### Traceability & Invariant Guardrails

- **Zero Delta Invariant**: No additions or deletions to Functions, Services, Processes, Actors, Roles, Collaborations, Interactions, or Information Responsibilities.
- **Pure Business Boundary**: Zero mentions of internal application components (`Mneme`, `Mnemosyne`, `Ponos`, `Pylai`, `Calliope`, `Iris`, `Petasos`, `Themis`, `Agora`).
- **Canonical Strategy Hierarchy**: 100% match with `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`.

# Testing

### Validation Approach & Automated Checks

Validation will be performed via targeted pattern searches, cross-reference verifications, and architectural invariant checks across all files in `docs/markdown/03-business-architecture/`:

1. **Check A: Canonical Capability & Feature Hierarchy Validation**:
   - Perform regex check against all `Feature:` headings in `behaviours/` and `metamodel/` to ensure 100% match with canonical Strategy features in `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`.
   - Verify that 0 invented feature names (e.g., `Demographic Change Intake`, `Referral Context Assembly`, `Protocol Transport Mediation`, `Clinical Record Search`, `Operational Team Coordination`) remain as canonical features.
2. **Check B: Feature Definition Consistency Validation**:
   - Verify that all definitions of Feature in `metamodel/` and `README.md` describe Feature as a finer-grained Capability specialisation.
   - Verify that "smallest useful statement of required system-enabled behaviour" is removed from Domain 03.
3. **Check C: Function Semantics & Ownership Nuance Validation**:
   - Verify that `metamodel/business-architecture-metamodel.md` explicitly decouples Function ownership from exclusive information ownership.
4. **Check D: Implementation Terminology Scan**:
   - Execute regex scans for:
     `\b(HL7|MLLP|FHIR|DICOM|CDA|PDF|REST|JSON|Java|schema|payload|frontend|view model|queue|topic|Kubernetes|cache|retry|compensation)\b`
   - For every remaining match, confirm that it represents a legitimate business/governance concept (e.g., statutory standard, governed identifier) rather than implementation leakage.
5. **Check E: Application Component Isolation Scan**:
   - Execute regex scans for:
     `\b(Mneme|Mnemosyne|Ponos|Pylai|Calliope|Iris|Petasos|Themis|Agora|Paradeigma|Kleio|Ergon|Erga|Praxis)\b`
   - Ensure zero occurrences in Domain 03.
6. **Check F: Architectural Invariant Verification (Zero Delta Count)**:
   - Verify that the total count of Business Functions, Business Services, Business Processes, Business Actors, Business Roles, Business Collaborations, and Business Interactions remains identical to the accepted R1 baseline.
7. **Check G: Markdown Link Integrity**:
   - Verify all relative Markdown links across Domain 03 and to Domain 02 resolve without broken references.

---

### Hygiene Completion Report Verification

- Verify that `.junie/reports/YYYY-MM-DD-domain03-business-architecture-r1-hygiene.md` is authored with all 10 required sections:
  1. Task Goal
  2. Files Reviewed
  3. Files Modified
  4. Canonical Feature Corrections
  5. Business / Implementation Boundary Corrections
  6. Function / Information Ownership Corrections
  7. Feature Definition Correction
  8. Validation Results (including zero-delta confirmation)
  9. Items Requiring Future Architectural Review
  10. Target Outcome Assessment (PASS/PARTIAL/FAIL for all 8 target outcomes).

# Delivery Steps

### ✓ Step 1: Canonicalise Metamodel, README, Actors, Roles, Collaborations, and Interactions
Review and correct `README.md`, `metamodel/business-architecture-metamodel.md`, `actors-roles/actors.md`, `actors-roles/roles.md`, `collaborations-interactions/collaborations.md`, and `collaborations-interactions/interactions.md`.

- Update Feature definition across `README.md` and `metamodel/business-architecture-metamodel.md` to consistently define Feature as a finer-grained specialisation of Capability beneath L3, removing "smallest useful statement of required system-enabled behaviour".
- Update Function semantics in `metamodel/business-architecture-metamodel.md` to decouple Function ownership from data ownership and explicitly accommodate cross-capability service consumption and dependencies.
- Audit `actors-roles/actors.md`, `actors-roles/roles.md`, `collaborations-interactions/collaborations.md`, and `collaborations-interactions/interactions.md` to remove technical implementation leakage (such as transport protocols or network terms) while preserving the exact accepted taxonomy and boundary rules.

### ✓ Step 2: Canonicalise Capability-Scoped Behaviours across Five Contextual Views
Review and correct `behaviours/index.md`, `behaviours/01-entity-management.md`, `behaviours/02-service-administration.md`, `behaviours/03-service-delivery.md`, `behaviours/04-health-service-operations.md`, and `behaviours/05-intrinsic-enablement.md`.

- Reconcile all Feature headings against canonical Strategy features in `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`.
- Correct invented feature labels in `02-service-administration.md` (e.g., `Demographic Change Intake` -> `Client Demographics Management` [FEAT-SA-01], `Clinical Document Intake` -> `Clinical Document Ingestion` [FEAT-SA-19]).
- Correct invented feature labels in `05-intrinsic-enablement.md` (e.g., `Clinical Record Search` -> `Search & Query Services` [FEAT-ISE-07], `Protocol Transport Mediation` -> `Standards Communication Gateway` [FEAT-ISE-09], `Operational Team Coordination` -> `Operational Collaboration Spaces` [FEAT-ISE-18]).
- Check `04-health-service-operations.md` and `01-entity-management.md` feature headings against Strategy.
- Remove implementation/protocol terms (`HL7 v2 MLLP`, `FHIR REST`, `DICOMweb`, `HTTP 200`, `MLLP MSA-AA`, `CDA`, `PDF`, `view model`, `frontend`, `payload`, `network perimeter`, etc.) and abstract back to pure business semantics.

### ✓ Step 3: Canonicalise Business Processes, Information Responsibilities, and Cross-Capability Dependencies
Review and correct `processes/business-processes.md`, `information-responsibility/information-responsibility.md`, and `dependencies/cross-capability-dependencies.md`.

- Audit the 16 principal processes in `processes/business-processes.md` to eliminate implementation jargon (`queue`, `cache`, `retry`, `compensation`, `payload`) while preserving exact states and lifecycles.
- Audit `information-responsibility/information-responsibility.md` to remove technical artifact names and reinforce capability-derived ownership.
- Audit `dependencies/cross-capability-dependencies.md` to ensure pure business service consumption semantics.

### ✓ Step 4: Perform Architectural Validation and Author the Hygiene Report
Execute the comprehensive validation suite and compile the completion hygiene report.

- Run checks for canonical feature alignment, implementation terminology scans, component isolation, zero delta invariant counts, and link integrity.
- Author `.junie/reports/2026-10-06-domain03-business-architecture-r1-hygiene.md` with all 10 required sections and explicit target outcome assessments.