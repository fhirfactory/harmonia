# Domain 02 — Strategy Analysis & Handover Report

## Requested / Identified

### Task Goal
Discover, extract, compare, and reconcile existing repository evidence relevant to **Domain 02 — Strategy** across Markdown, LaTeX, and strategic DOCX sources, before any canonical Strategy documentation is authored. The objective is to provide a rigorous, evidence-based foundation enabling human architectural review and decision-making, strictly avoiding premature strategy design, ungrounded decisions, or unauthorized canonical authoring.

### Task Activity
Execute an exhaustive multi-source analysis across the Harmonia repository covering:
- LaTeX source specifications (`docs/latex/chapters/01-motivation-strategy.tex`, `01-motivation-strategy-old.tex`, `01-foundations.tex`, `02-conceptual-taxonomy.tex`, `02-business-layer.tex`, `styles/harmonia-archimate.sty`, and `diagrams/fig-motivation-map.tex`).
- Markdown architectural baselines (`docs/concepts/` [all 22 files], `docs/architecture/` [10 files], and `docs/markdown/01-motivation/` [canonical 13-file model]).
- Strategic architectural position brief (`docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`).
- Extraction and classification of candidate capabilities (decoupled from named software components and categorized by evidence derivation type).
- Investigation of strategic resources as an open architectural question under ArchiMate 3.2 criteria.
- Evaluation of candidate courses of action against a 7-question test without canonical identifier assignment.
- Discovery of natural capability clustering hypotheses.
- Motivation-to-Strategy traceability using verbatim canonical Domain 01 nomenclature.
- Delineation of conceptual Strategy-to-Downstream Architecture handoff boundaries.
- Reclassification of implementation-specific material to downstream architecture domains (Domains 03–13).
- Identification of historical, superseded, and contradictory material with retention/retirement recommendations.
- Compilation of unresolved architectural contradictions and open questions requiring human review.
- Recommendation of a coherent target documentation set under `docs/markdown/02-strategy/`.

### Target Outcome
An evidence-based analysis report that tells the enterprise architect:
> *"Here is what the repository appears to say, here are the candidate Strategy concepts that can reasonably be extracted from it, here is where the sources disagree, and here are the decisions that still need to be made."*

It explicitly avoids stating:
> *"Here is the Strategy architecture I have decided Harmonia should use."*

---

## Delivered / Completed

### Sources Reviewed
1. **Master LaTeX Specification**:
   - `docs/latex/chapters/01-motivation-strategy.tex` & `01-motivation-strategy-old.tex`: Historical merged Motivation and Strategy chapters, Table 1.1 (`tab:strategic_capabilities`), strategic value stream equations.
   - `docs/latex/chapters/01-foundations.tex`: Platform motivation, HIE context, and foundational principles.
   - `docs/latex/chapters/02-conceptual-taxonomy.tex`: Conceptual subsystem domains, boundaries, and operational layers.
   - `docs/latex/chapters/02-business-layer.tex`: Business actors, clinical business services, and detailed 5-stage clinical processes.
   - `docs/latex/styles/harmonia-archimate.sty`: ArchiMate 3.2 TikZ styling definitions, colors, and node shapes (`archi-capability`, `archi-valuestream`).
   - `docs/latex/diagrams/fig-motivation-map.tex`: Historical motivation ArchiMate diagram mixing principles, requirements, and component-named capabilities.
2. **Strategic Position Brief**:
   - `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`: Definitive architectural position brief (24 September 2026) establishing the relationship between the enterprise Local Directory capability (aligned to Australian LDS direction) and the broader multi-purpose platform vision (`HARM-STR-001`, `HARM-LDS-001..012`).
3. **Canonical Motivation Baseline**:
   - `docs/markdown/01-motivation/`: All 13 canonical files, including 6 enterprise stakeholders, 5 external authorities, 7 drivers, 5 assessments, 6 strategic goals, 3 platform outcomes, 4 foundational requirements (`REQ-FND-001..004`), 3 external constraints (`CST-EXT-001..003`), and 15 active Architectural Axioms (`AX-01..11`, `AX-13..16`).
4. **Subsystem Concepts & Architectural Overviews**:
   - `docs/concepts/`: 22 concept documents covering `harmonia`, `hestia`, `mneme`, `mnemosyne`, `petasos`, `energeia`, `ponos`, `ergon`, `praxis`, `pragma`, `pylai`, `calliope`, `themis`, `iris`, `agora`, and `paradeigma`.
   - `docs/architecture/`: System inventory, runtime architecture, execution model, persistence lifecycle, failure recovery, and port-protocol register.

### Key Findings
- **Conflation of Capabilities with Components**: Historical LaTeX specifications (Table 1.1 and Figure 1.1) routinely label software modules (`Pylai`, `Petasos`, `Hestia Mneme`, `Energeia`) as "capabilities". In accordance with ArchiMate 3.2, these must be decoupled into functional capabilities representing *what* the platform can do, deferring subsystem structures to downstream Application Architecture (Domain 05).
- **Strategic Resources Are Redundant at Domain 02**: An explicit Strategy-level Resource model does not add meaningful architectural clarity to Harmonia. Candidate physical/software artifacts (caches, message brokers, databases, FHIR JSON schemas) belong to Technology and Application Architecture. Enterprise information assets are more appropriately governed under Information Architecture (Domain 04). The analysis formally recommends that Harmonia's strategy layer operate without an explicit resource model.
- **Principles vs. Courses of Action Conflation**: Existing drafts frequently treat Architectural Axioms (such as `AX-02` "Standards at the boundary" or `AX-05` "State separation") as courses of action. The analysis restores the strict distinction: Axioms *constrain and guide*, whereas Courses of Action represent *chosen strategic approaches* to realize goals.
- **Scope Demarcation of Local Directory (LDS)**: Early repository material blurred whether Harmonia was primarily a Local Directory Solution or a generalized HIE. The 24 September 2026 Strategic Position Brief clarifies that the Local Directory capability is a proving case and a Solution Pack (Domain 11) utilizing shared core platform services (`HARM-STR-001`).
- **Persistence Evolution**: Historical LaTeX drafts emphasize write-behind caching (`Mneme -> Mnemosyne`) as the sole persistence mechanism. Subsequent convergence work and `AX-05` establish that Mneme is ephemeral/reconstructable active state and Mnemosyne is durable truth. Write-behind as an exclusive persistence pattern is classified as `Potentially Superseded — Requires Human Review`.

### Candidate Strategy Model
The discovered candidate Strategy concepts are structured into:
- **11 Candidate Strategic Capabilities** (profiles provided with derivation category, confidence, source traceability, and open questions).
- **6 Candidate Strategic Approaches / Courses of Action** (evaluated against the 7-question criteria).
- **5 Natural Capability Clustering Hypotheses** (observational groupings, not a pre-frozen map).
- **Principal Motivation-to-Strategy Traceability Threads** (connecting canonical drivers, assessments, goals, and axioms to strategic approaches and capabilities).
- **Strategic Clinical Value Stream Flow** (tracing clinical information lifecycle across platform boundaries).

### Reclassification Findings
- Implementation details (ActiveMQ Artemis broker clustering, Netty MLLP framing bytes `0x0B`/`0x1C 0x0D`, PostgreSQL JPA DDL, Infinispan Hot Rod topologies, Vue 3 SPA components) currently located in Strategy/Foundations material are catalogued for reclassification to Domains 04, 05, 06, 07, 08, 10, and 11.

### Validation
- **Evidentiary Completeness**: Analyzed all 7 LaTeX source files, the strategic position brief DOCX, all 22 concept documents, 10 architecture overviews, and all 13 canonical Domain 01 files.
- **Canonical Motivation Integrity**: All referenced Motivation concepts use verbatim canonical names from `docs/markdown/01-motivation/`; zero invented shorthand concepts were introduced.
- **Preservation of Uncertainty**: Open questions, unverified supersessions, and conflicting claims are explicitly documented for human architectural review.
- **Repository Invariance**: Verified via `git status` that no canonical documentation files under `docs/markdown/01-motivation/` or new directories under `docs/markdown/02-strategy/` were created or modified.

### Deviations from Approved Task
None.

### Unresolved Issues
1. **Canonical Capability Model Freezing**: Confirmation of whether the 11 candidate capabilities adequately cover Harmonia's strategic scope, or whether Presentation and Simulation should be represented as platform capabilities or downstream concerns.
2. **Resource Model Inclusion vs. Omission**: Formal human decision on whether to omit ArchiMate 3.2 Strategy Resources entirely or maintain a minimal set of information assets.
3. **Persistence Mechanism Supersession**: Formal architectural determination on whether write-behind cache persistence is fully retired, retained as an optional optimization, or superseded by governed dual-write/asynchronous write paths.
4. **LDS Solution Pack vs. Platform Boundary**: Formal agreement on treating the Local Directory Solution as a Domain 11 Solution Pack rather than defining the platform core.

---

# Comprehensive Analysis Report: Domain 02 — Strategy

---

### Section A: Executive Summary

#### 1. The Meaning of Strategy for Harmonia
In the enterprise architecture of the Harmonia Health Integration Environment (HIE), **Domain 02 — Strategy** answers:
> *"What capabilities, resources, and courses of action are required to respond to Harmonia's motivating drivers, assessments, goals, and architectural axioms?"*

Strategy sits between **Domain 01 (Motivation)**—which establishes *why* Harmonia exists and *what* outcomes it must achieve—and the downstream **Architecture Domains (03–10)**—which define *how* those capabilities are realized structurally, behaviorally, and technologically.

```text
+---------------------------------------------------------------------------------------+
| DOMAIN 01 — MOTIVATION (Canonical Baseline)                                           |
| Why does Harmonia exist and what must it achieve?                                     |
| Drivers | Assessments | Goals | Axioms (Principles) | Outcomes | Constraints          |
+---------------------------------------------------------------------------------------+
                                        │
                                        ▼ (guides & shapes)
+---------------------------------------------------------------------------------------+
| DOMAIN 02 — STRATEGY (Analysis Baseline — Decisions Deferred)                         |
| What capabilities, resources, and strategic approaches respond to Motivation?         |
| Candidate Capabilities | Candidate Approaches | Natural Groupings | Value Stream      |
+---------------------------------------------------------------------------------------+
                                        │
                                        ▼ (realised by)
+---------------------------------------------------------------------------------------+
| DOWNSTREAM ARCHITECTURE DOMAINS (03 – 11)                                             |
| How are those capabilities realised structurally and behaviourally?                   |
| 03 Business | 04 Information | 05 Application | 06 Integration | 07 Technology        |
| 08 Security | 09 Resilience & Operability | 10 Verification | 11 Solution Packs       |
+---------------------------------------------------------------------------------------+
```

#### 2. Major Findings of the Analysis
1. **Component-Centric Strategy Legacy**: Early architectural specifications (notably LaTeX Chapter 1 and Figure 1.1) coupled strategic capabilities directly to named software components (`Pylai`, `Petasos`, `Hestia Mneme`, `Energeia Ponos`, `Iris`). Strategy in Harmonia must describe *what the platform can do*, completely independent of whether a subsystem is implemented in Apache Camel, Netty, ActiveMQ Artemis, Infinispan, or Spring Boot.
2. **Strategy Resources Are Superfluous**: Evaluated against ArchiMate 3.2 criteria, Harmonia's architecture does not benefit from an explicit Strategy-level Resource model. Data stores, cache grids, message brokers, and database tables are Technology/Application artifacts, while clinical information entities and semantic schemas belong to Information Architecture (Domain 04). Modeling them as Strategy Resources introduces unnecessary conceptual overhead.
3. **Distinct Roles for Axioms vs. Courses of Action**: Architectural Axioms (`AX-01` through `AX-16`) are enduring principles that constrain and govern architecture. Courses of Action represent selected strategic architectural approaches (such as deploying boundary membranes, establishing two-tier state separation, or enforcing intrinsic default-deny security) chosen to achieve specific Goals and Outcomes.
4. **Decoupling Platform Core from the Local Directory Solution (LDS)**: The strategic position paper (`Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`, 24 September 2026) firmly establishes that while an enterprise Local Directory capability is a motivating use case, Harmonia is a multi-purpose health integration and information platform (`HARM-STR-001`). LDS is properly positioned as a Solution Pack (Domain 11) built upon shared platform capabilities.
5. **Historical Inconsistencies & Supersession Ambiguities**: Historical drafts asserted write-behind caching (`Mneme -> Mnemosyne`) as the sole persistence mechanism. Current convergence milestones and `AX-05` establish state separation (Mneme = active/reconstructable; Mnemosyne = authoritative durable truth). This item is categorized as `Potentially Superseded — Requires Human Review`.

---

### Section B: Source Material Reviewed

The analysis encompassed the complete repository corpus across LaTeX, DOCX, and Markdown formats:

| Source Identifier | Document Path / Location | Architectural Scope & Description | Relevance to Strategy Layer |
| :--- | :--- | :--- | :--- |
| **LATEX-01** | `docs/latex/chapters/01-motivation-strategy.tex` | Master specification chapter combining Motivation & Strategy. Defines Table 1.1 (`tab:strategic_capabilities`) and value stream equations. | Primary historical source for capabilities, value streams, and subsystem realization mappings. |
| **LATEX-01-OLD**| `docs/latex/chapters/01-motivation-strategy-old.tex` | Preceding draft of merged Chapter 1. | Baseline for historical evolution and supersession tracking. |
| **LATEX-FND** | `docs/latex/chapters/01-foundations.tex` | Foundations chapter defining HIE context, stakeholders, drivers, principles, and strategic value streams. | Primary historical source for 5-stage clinical process value stream and foundational principles. |
| **LATEX-TAX** | `docs/latex/chapters/02-conceptual-taxonomy.tex` | Taxonomy chapter defining subsystem boundaries (`Iris`, `Pylai`, `Petasos`, `Energeia`, `Hestia`, `Calliope`, `Themis`, `Agora`, `Paradeigma`). | Baseline for extracting strategic intent from named subsystem definitions. |
| **LATEX-BIZ** | `docs/latex/chapters/02-business-layer.tex` | Business layer specification defining business actors, roles, clinical services, and detailed 5-stage clinical workflows. | Evidence for clinical value stream flow, business services, and business objects. |
| **LATEX-STY** | `docs/latex/styles/harmonia-archimate.sty` | ArchiMate 3.2 TikZ styling package. Defines node geometry, colors, and line semantics. | Defines visual modeling baseline for `archi-capability` and `archi-valuestream`. Lacks resource/course-of-action styles. |
| **LATEX-MAP** | `docs/latex/diagrams/fig-motivation-map.tex` | ArchiMate diagram mixing principles, requirements, and component-named capabilities. | Visual evidence of legacy conflation between capabilities and named components. |
| **DOCX-STRAT** | `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx` | Strategic Architecture Position Paper (24 Sept 2026). Authored by Mark Hunter. | Authoritative position on enterprise Local Directory capability, multi-purpose scope (`HARM-STR-001`), and requirements (`HARM-LDS-001..012`). |
| **MD-MOTIV** | `docs/markdown/01-motivation/` (13 files) | Canonical, frozen Domain 01 baseline: stakeholders, drivers, assessments, goals, outcomes, axioms, requirements, constraints. | Authoritative Motivation anchor; mandatory nomenclature reference. |
| **MD-CONCEPTS**| `docs/concepts/*.md` (22 files) | Subsystem concept specifications detailing etymology, definitions, ownership boundaries, and invariants. | Source for subsystem strategic intent and anti-responsibilities. |
| **MD-ARCH** | `docs/architecture/*.md` (10 files) | Technical overviews: execution models, runtime topologies, persistence lifecycles, and failure recovery. | Downstream realization concerns and physical constraint mapping. |
| **GOV-AGENTS** | `AGENTS.md` | Architectural Guardrails & Rules for AI Agents. Governed by Architectural Axioms. | Core architectural authority, module hierarchy, and derived invariants (Invariants 1–10). |

---

### Section C: Candidate Capabilities (Detailed Profiles)

The following candidate strategic capabilities represent **analysis hypotheses** extracted from repository evidence. They are explicitly decoupled from component names and categorized by derivation type:

```text
Evidence Derivation Categories:
- Explicitly Defined in Source: Directly stated as a capability or core service in existing text.
- Inferred from Multiple Sources: Synthesized from cross-cutting architectural requirements and axioms.
- Derived from Component Responsibilities: Extracted by abstracting what a named subsystem accomplishes.
- Historical: Present in older drafts but altered by subsequent architectural convergence.
- Conflicting: Supported by some sources but contradicted by others.
- Weakly Supported: Mentioned casually without architectural definition or formal backing.
```

#### Capability 1: Standards-Based Protocol Ingress & Egress
- **Candidate Capability**: Standards-Based Protocol Ingress & Egress
- **Strategic Purpose**: Enable native, bidirectional boundary interaction with external healthcare systems across heterogeneous messaging standards and protocols, terminating external wire framing upon ingress and projecting internal canonical state into standards-compliant wire formats upon egress without leaking internal operational semantics.
- **Motivation Elements Served**:
  - *Driver*: `Clinical Interoperability Across Heterogeneous Standards`
  - *Assessment*: `Silent Data Loss via False Acceptance`
  - *Goal*: `Durable Acceptance & Preservation of Clinical Events` (Goal 1)
  - *Axioms*: `AX-02 (Standards at Boundary)`, `AX-03 (Native Standards Representations)`, `AX-13 (Publication Terminates Management)`
  - *Foundational Requirement*: `REQ-FND-001 (Durable Ingress Acceptance Boundary)`
  - *Outcome*: `Outcome 2: Continuous and Resilient Regional Health Information Exchange`
- **Supporting Source Material**:
  - `docs/latex/chapters/01-motivation-strategy.tex`, Section 1.5, Table 1.1 ("Clinical Gateway Interfaces -> Pylai").
  - `docs/concepts/pylai.md` & `pylai-gateways.md` (Boundary protocol interface, MLLP framing, REST gateways).
  - `AGENTS.md`, Section 2 (Pylai responsibilities) and Section 3 (Invariant 4 & 9).
- **Potential Downstream Realisation**: Domain 06 (Integration Architecture) — protocol drivers, boundary validation behavior, non-destructive projection contracts, and socket/connection lifecycle management.
- **Evidence Derivation Category**: `Derived from Component Responsibilities` (abstracted from `Pylai`).
- **Classification Confidence**: `High`
- **Conflicts / Questions**: Does this capability encompass batch/bulk data exchange (e.g. FHIR Bulk Data export for LDS), or should bulk data transfer be recognized as a distinct specialized capability?

#### Capability 2: Enterprise Healthcare Directory Management
- **Candidate Capability**: Enterprise Healthcare Directory Management
- **Strategic Purpose**: Maintain an authoritative, locally governed, and referentially validated directory of healthcare practitioners, organizations, facilities, operational roles, healthcare services, and electronic service endpoints, supporting federated synchronization with national directories while preserving local operational enrichment.
- **Motivation Elements Served**:
  - *Driver*: `National Healthcare Directory / Endpoint Governance`
  - *Goal*: `Reliable Subject Identity & Referential Integrity` (Goal 4)
  - *Axioms*: `AX-01 (Health-Information Centricity)`, `AX-06 (Explicit Information Authority)`, `AX-14 (Semantic Distinctions Are Preserved)`
  - *Strategic Requirements*: `HARM-STR-001`, `HARM-LDS-001` through `HARM-LDS-012`
  - *Outcome*: `Outcome 1: Reduced Clinical Risk from Unavailable, Fragmented or Incorrectly Associated Information`
- **Supporting Source Material**:
  - `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`, Sections 1–8.
  - `docs/latex/chapters/01-motivation-strategy.tex`, Table 1.1 ("FHIR Provider Registry").
  - `docs/latex/chapters/02-business-layer.tex`, Section 2.3 ("FHIR R5 Provider Directory Management Service").
- **Potential Downstream Realisation**: Domain 04 (Information Architecture — directory models) and Domain 11 (Solution Pack: Provider Directory — synchronization adapters, administrative review).
- **Evidence Derivation Category**: `Explicitly Defined in Source` (strongly articulated in the 24 Sept 2026 Strategic Brief).
- **Classification Confidence**: `High`
- **Conflicts / Questions**: Should this capability be owned wholly within Domain 02 Strategy as a core platform capability, or should it be treated as a Domain 11 Solution Pack capability running on top of generic platform information management? (See Strategic Position Brief Section 9).

#### Capability 3: Vendor-Neutral Longitudinal Health Record Preservation
- **Candidate Capability**: Vendor-Neutral Longitudinal Health Record Preservation
- **Strategic Purpose**: Aggregate, persist, and govern an enduring, vendor-independent longitudinal clinical record across the patient lifespan, preserving source provenance, semantic nuances, and source authority independently of commercial EMR lifecycles.
- **Motivation Elements Served**:
  - *Driver*: `Durable Clinical Information Independence`
  - *Goal*: `Vendor-Independent Longitudinal Clinical Information Coherence` (Goal 5)
  - *Goal*: `Durable Acceptance & Preservation of Clinical Events` (Goal 1)
  - *Axioms*: `AX-01 (Health-Information Centricity)`, `AX-04 (Non-Destructive State Progression)`, `AX-06 (Explicit Information Authority)`
  - *Foundational Requirement*: `REQ-FND-001`
  - *Outcome*: `Outcome 1: Reduced Clinical Risk` & `Outcome 2: Continuous Regional Exchange`
- **Supporting Source Material**:
  - `docs/latex/chapters/01-motivation-strategy.tex`, Table 1.1 ("Clinical FHIR Information Service").
  - `docs/concepts/mnemosyne.md` & `hestia-persistence.md` (authoritative durable state, non-destructive progression).
  - `docs/markdown/01-motivation/goals-outcomes/strategic-goals.md`, Goal 5.
- **Potential Downstream Realisation**: Domain 04 (Information Architecture — longitudinal clinical schemas, versioning semantics) and Domain 07 (Technology Architecture — relational persistence backends).
- **Evidence Derivation Category**: `Inferred from Multiple Sources` (synthesized from Goal 5, `AX-01`, `AX-04`, and `Mnemosyne` concepts).
- **Classification Confidence**: `High`
- **Conflicts / Questions**: What is the relationship between the longitudinal clinical record and patient demographic master data? Does Harmonia maintain subject identity links without becoming a full EMPI? (Domain 01 explicitly establishes Harmonia is NOT an MPI).

#### Capability 4: Resilient Asynchronous Event Decoupling & Distribution
- **Candidate Capability**: Resilient Asynchronous Event Decoupling & Distribution
- **Strategic Purpose**: Decouple ingress event acceptance from downstream processing and distribution, providing guaranteed message persistence, transport durability, load leveling, sliding-window deduplication, and failure isolation across platform tiers.
- **Motivation Elements Served**:
  - *Driver*: `Continuous Clinical Service Availability`
  - *Assessment*: `Silent Data Loss via False Acceptance`
  - *Goal*: `Durable Acceptance & Preservation of Clinical Events` (Goal 1)
  - *Goal*: `Timely Clinical Availability & Non-Destructive Progression` (Goal 2)
  - *Axioms*: `AX-10 (Failure as Normal Condition)`, `AX-15 (Uncertainty Is Preserved Until Resolved)`
  - *Outcome*: `Outcome 2: Continuous and Resilient Regional Health Information Exchange`
- **Supporting Source Material**:
  - `docs/latex/chapters/01-motivation-strategy.tex`, Table 1.1 ("High-Availability Messaging -> Petasos").
  - `docs/concepts/petasos.md` & `petasos-messaging.md` (pure Java transport abstraction, Artemis isolation).
  - `AGENTS.md`, Section 3 (Invariant 2: Petasos API Abstraction).
- **Potential Downstream Realisation**: Domain 07 (Technology Architecture — messaging broker topology, clustering) and Domain 06 (Integration Architecture — queue/topic routing patterns).
- **Evidence Derivation Category**: `Derived from Component Responsibilities` (abstracted from `Petasos`).
- **Classification Confidence**: `High`
- **Conflicts / Questions**: Does messaging constitute a business capability or an underlying technology service? In HIE platforms, guaranteed event decoupling is widely recognized as a core platform capability necessary to satisfy 24/7 clinical availability drivers.

#### Capability 5: Coordinated Clinical Activity Progression
- **Candidate Capability**: Coordinated Clinical Activity Progression
- **Strategic Purpose**: Coordinate distributed, multi-stage integration tasks, message transformations, and multi-destination fan-out deliveries in observable, auditable lockstep with the lifecycle state of real-world clinical entities.
- **Motivation Elements Served**:
  - *Driver*: `Coordinated Operational Activity and Entity State`
  - *Assessment*: `Unmonitored Destination Failure and Operational Divergence`
  - *Goal*: `Coordinated Progression of Operational Activities & Associated Entity State` (Goal 6)
  - *Axioms*: `AX-15 (Uncertainty Is Preserved Until Resolved)`, `AX-16 (Operational Activity & Entity State Progress Together)`
  - *Foundational Requirement*: `REQ-FND-002 (Observable Activity Progression)` & `REQ-FND-004 (Indeterminate State & Controlled Recovery)`
  - *Outcome*: `Outcome 2: Continuous and Resilient Regional Health Information Exchange`
- **Supporting Source Material**:
  - `docs/latex/chapters/01-motivation-strategy.tex`, Table 1.1 ("Modular Task Execution -> Energeia").
  - `docs/concepts/energeia.md`, `ponos.md`, `ergon.md`, `praxis.md`, `pragma.md`.
  - `AGENTS.md`, Section 3 (Invariant 5: Destination Fan-Out State Tracking).
- **Potential Downstream Realisation**: Domain 05 (Application Architecture — activity unit execution models, state machine tracking, fan-out sub-task coordination).
- **Evidence Derivation Category**: `Inferred from Multiple Sources` (derived from `AX-16`, Goal 6, and `Energeia` concept documents).
- **Classification Confidence**: `High`
- **Conflicts / Questions**: How does this platform capability differ from generic business process management (BPMN)? The canonical Motivation model explicitly states Harmonia is *not* a generic workflow engine, but coordinates distributed activities synchronized with clinical entity states.

#### Capability 6: Responsive Managed Information Access
- **Candidate Capability**: Responsive Managed Information Access
- **Strategic Purpose**: Deliver sub-millisecond, highly concurrent read and query access to active operational state, task execution sequences, and cached clinical lookups, completely isolated from central relational persistence locking and contention.
- **Motivation Elements Served**:
  - *Assessment*: `Centralised Synchronous Persistence Can Constrain Concurrency`
  - *Goal*: `Responsive Access to Managed Information` (Goal 3)
  - *Axioms*: `AX-05 (Active Coordination ≠ Authoritative State)`, `AX-11 (Responsive & Resilient Access)`
  - *Outcome*: `Outcome 2: Continuous and Resilient Regional Health Information Exchange`
- **Supporting Source Material**:
  - `docs/latex/chapters/01-motivation-strategy.tex`, Table 1.1 ("In-Memory Cache Grid -> Hestia Mneme").
  - `docs/latex/chapters/01-foundations.tex`, Section 1.3 (Goal 3: Sub-Millisecond Cache Response).
  - `docs/concepts/mneme.md` (active, reconstructable, non-authoritative distributed cache).
  - `AGENTS.md`, Section 3 (Invariant 8: Mneme / Mnemosyne State Separation).
- **Potential Downstream Realisation**: Domain 07 (Technology Architecture — distributed caching topologies) and Domain 05 (Application Architecture — active state query contracts).
- **Evidence Derivation Category**: `Derived from Component Responsibilities` (abstracted from `Mneme`).
- **Classification Confidence**: `High`
- **Conflicts / Questions**: Historical documents paired this capability with "write-behind cache persistence." Since write-behind is now questioned under `AX-05`, is this capability purely read-oriented and active-coordination-oriented? (Yes, `AX-05` asserts Mneme state is active and reconstructable, never authoritative).

#### Capability 7: Point-of-Care Clinical Presentation
- **Candidate Capability**: Point-of-Care Clinical Presentation
- **Strategic Purpose**: Deliver intuitive, human-facing, read-oriented presentation views of longitudinal patient clinical records, encounters, allergies, medications, and diagnostic reports to care teams at the point of care.
- **Motivation Elements Served**:
  - *Goal*: `Vendor-Independent Longitudinal Clinical Information Coherence` (Goal 5)
  - *Outcome*: `Outcome 1: Reduced Clinical Risk from Unavailable, Fragmented or Incorrectly Associated Information`
  - *Axioms*: `AX-01 (Health-Information Centricity)`, `AX-06 (Explicit Information Authority)`
- **Supporting Source Material**:
  - `docs/latex/chapters/01-motivation-strategy.tex`, Table 1.1 ("Longitudinal Clinical Record Presentation -> Iris-Clinical").
  - `docs/concepts/iris.md` (presentation services, decoupled clinical and administrative web applications).
  - `AGENTS.md`, Section 3 (Invariant 3: Iris Presentation Decoupling).
- **Potential Downstream Realisation**: Domain 05 (Application Architecture: Presentation Tier — Vue 3 SPAs, Backend-For-Frontend gateway, user session management).
- **Evidence Derivation Category**: `Derived from Component Responsibilities` (abstracted from `iris-clinical`).
- **Classification Confidence**: `High`
- **Conflicts / Questions**: Is clinical presentation a core HIE platform capability, or is it an application solution that consumes HIE APIs? The repository consistently includes `Iris` in the core architecture stack, but strict decoupling prevents it from asserting persistence authority.

#### Capability 8: Intrinsic Access Governance & Policy Evaluation
- **Candidate Capability**: Intrinsic Access Governance & Policy Evaluation
- **Strategic Purpose**: Enforce platform-wide, default-deny security context evaluation, multi-gate authorization, and credential validation across all ingress requests and internal state-changing operations.
- **Motivation Elements Served**:
  - *Driver*: `Statutory Health Information Privacy & Protection`
  - *Assessment*: `Implicit Perimeter Trust and Diagnostic Data Leakage`
  - *Axioms*: `AX-07 (Security Is Intrinsic to Managed Operations)`
  - *Foundational Requirement*: `REQ-FND-003 (Intrinsic Security, Privacy & Provenance)`
  - *Outcome*: `Outcome 3: Demonstrable Protection and Accountable Handling of Health Information`
- **Supporting Source Material**:
  - `docs/concepts/themis.md` & `themis-security.md` (default-deny ABAC/RBAC policy engine, 4-gate interceptors).
  - `AGENTS.md`, Section 3 (Invariant 6: Default-Deny Security Governance).
  - `docs/markdown/01-motivation/principles/architectural-axioms.md`, `AX-07`.
- **Potential Downstream Realisation**: Domain 08 (Security Architecture — policy evaluation engines, security context contracts, authorization interceptors).
- **Evidence Derivation Category**: `Derived from Component Responsibilities` (abstracted from `Themis`).
- **Classification Confidence**: `High`
- **Conflicts / Questions**: Does security governance act as a discrete functional capability, or is it an architectural guardrail that constrains all other capabilities? In ArchiMate, security management is appropriately represented as a cross-cutting capability serving regulatory and privacy drivers.

#### Capability 9: Tamper-Evident Operational Audit & Provenance Evidence
- **Candidate Capability**: Tamper-Evident Operational Audit & Provenance Evidence
- **Strategic Purpose**: Capture immutable, chronological, and tamper-evident operational and clinical audit evidence establishing source attribution, actor accountability, and processing history across all transactions without leaking Protected Health Information (PHI).
- **Motivation Elements Served**:
  - *Driver*: `Statutory Health Information Privacy & Protection`
  - *Assessment*: `Implicit Perimeter Trust and Diagnostic Data Leakage`
  - *Axioms*: `AX-08 (Evidence Records Meaning)`, `AX-09 (Default Ephemerality of Operational State)`
  - *Foundational Requirement*: `REQ-FND-003`
  - *Outcome*: `Outcome 3: Demonstrable Protection and Accountable Handling of Health Information`
- **Supporting Source Material**:
  - `docs/concepts/themis.md` (themis-audit), `docs/concepts/kleio.md` (audit & provenance ownership).
  - `AGENTS.md`, Section 3 (Invariant 7: Zero-PHI Diagnostic Logging).
  - `docs/markdown/01-motivation/principles/architectural-axioms.md`, `AX-08`.
- **Potential Downstream Realisation**: Domain 08 (Security Architecture) and Domain 04 (Information Architecture — AuditEvent and Provenance structures, tamper-evident log append mechanisms).
- **Evidence Derivation Category**: `Inferred from Multiple Sources` (derived from `AX-08`, `AX-09`, `themis-audit`, and `kleio`).
- **Classification Confidence**: `High`
- **Conflicts / Questions**: Is audit and provenance evidence capture separable from Access Governance, or should they be combined into a unified "Platform Trust & Governance" capability?

#### Capability 10: Care Team Communication & Collaboration Gateway
- **Candidate Capability**: Care Team Communication & Collaboration Gateway
- **Strategic Purpose**: Bridge clinical operational events, integration alerts, and task notifications into structured, secure clinical collaboration channels and room spaces (e.g. Matrix Synapse), automating care team space provisioning and cross-facility communication.
- **Motivation Elements Served**:
  - *Driver*: `Coordinated Operational Activity and Entity State`
  - *Axioms*: `AX-01 (Health-Information Centricity)`, `AX-07 (Security Is Intrinsic)`
  - *Outcome*: `Outcome 2: Continuous Regional Exchange`
- **Supporting Source Material**:
  - `docs/concepts/agora.md` (collaboration projection, Matrix Synapse integration).
  - `docs/latex/chapters/02-conceptual-taxonomy.tex`, Section 2.7 (Collaboration Services).
  - `AGENTS.md`, Section 3 (Invariant 10: Agora Collaboration & Matrix Isolation).
- **Potential Downstream Realisation**: Domain 05 (Application Architecture — collaboration service adapters, Matrix DTO encapsulation).
- **Evidence Derivation Category**: `Derived from Component Responsibilities` / `Weakly Supported` in core motivation.
- **Classification Confidence**: `Medium` / `Low`
- **Conflicts / Questions**: Is Agora a core strategic HIE capability, or is it an optional peripheral solution adapter? `AGENTS.md` Invariant 10 notes: *"Agora makes Matrix a Harmonia collaboration capability; it does NOT make Matrix the Harmonia architecture."* Requires human architectural review to confirm strategic status.

#### Capability 11: Synthetic Clinical Simulation & Environment Assurance
- **Candidate Capability**: Synthetic Clinical Simulation & Environment Assurance
- **Strategic Purpose**: Emulate external healthcare systems (PAS, EMR, LIS, RIS) and execute deterministic failure and load injection scenarios to verify integration resilience and protocol conformance in complete isolation from production systems.
- **Motivation Elements Served**:
  - *Driver*: `Continuous Clinical Service Availability`
  - *Assessment*: `Silent Data Loss via False Acceptance`
  - *Axioms*: `AX-15 (Uncertainty Is Preserved Until Resolved)`
  - *Guardrails*: `AGENTS.md` Invariant 1 (Paradeigma Production Isolation).
- **Supporting Source Material**:
  - `docs/concepts/paradeigma.md` (synthetic clinical simulation, actor emulators).
  - `docs/latex/chapters/02-conceptual-taxonomy.tex`, Section 2.8.
  - `AGENTS.md`, Section 3 (Invariant 1).
- **Potential Downstream Realisation**: Domain 10 (Verification & Simulation Architecture — synthetic profile generators, isolated test harnesses).
- **Evidence Derivation Category**: `Derived from Component Responsibilities` (abstracted from `Paradeigma`).
- **Classification Confidence**: `Medium`
- **Conflicts / Questions**: In ArchiMate, simulation testbeds are typically classified under Implementation & Migration or Verification Architecture rather than Strategy. Does environment assurance warrant strategic capability status, or should it be deferred strictly to Domain 10?

---

### Section D: Candidate Resources (Critical Assessment)

#### 1. ArchiMate 3.2 Conceptual Criteria for Strategic Resources
In ArchiMate 3.2, a **Resource** is defined as an *asset that is owned or controlled by an individual or organization*. Resources are structured elements assigned to capabilities to realize value.

To qualify as a genuine Strategy-level Resource, an element must satisfy three tests:
1. **Asset Value**: Does the enterprise recognize and manage this asset as having strategic, distinct business value?
2. **Technological Independence**: Is the asset defined independently of specific runtime software, middleware products, database tables, or physical hardware?
3. **Strategic Utility**: Does modeling the resource at the Strategy layer provide necessary architectural insight that is not already captured by Capabilities, Information Models, or Technology Components?

#### 2. Critical Evaluation of Candidate Assets

| Candidate Asset | Source Evidence | Architectural Evaluation Against ArchiMate 3.2 Criteria | Strategic Resource Verdict |
| :--- | :--- | :--- | :--- |
| **Infinispan In-Memory Cache Grid** | `01-foundations.tex`, `mneme.md` | Infinispan is an open-source software product and distributed memory middleware. It is an Application/Technology component, not an enterprise resource. | **Rejected** (Technology Architecture artifact) |
| **ActiveMQ Artemis Clustered Broker** | `01-motivation-strategy.tex`, `petasos.md` | Artemis is a messaging broker implementation. Modeling it as a Strategy Resource violates ArchiMate abstraction layers. | **Rejected** (Technology Architecture artifact) |
| **PostgreSQL Relational Storage / HAPI FHIR JPA** | `02-conceptual-taxonomy.tex`, `mnemosyne.md` | Relational database instances and JPA server processes are Technology and Application artifacts. | **Rejected** (Technology/Application artifact) |
| **FHIR R5 Schemas & Profiles** | `01-foundations.tex`, `calliope.md` | FHIR schemas are structural syntax definitions and Information Architecture data models, not enterprise strategic resources. | **Rejected** (Information Architecture artifact) |
| **Authoritative Longitudinal Health Record Asset** | `01-motivation-strategy.tex`, `AX-01`, Goal 5 | The accumulated, vendor-neutral longitudinal clinical history of regional patients is a valuable enterprise informational asset. | **Candidate Asset** (Informational Asset) |
| **Enterprise Healthcare Directory Graph** | `Harmonia_Strategy_Local_Directory...docx` | The referentially verified, federated directory of regional healthcare practitioners, roles, and endpoints is a high-value operational asset. | **Candidate Asset** (Informational Asset) |
| **Tamper-Evident Provenance & Audit Logbook** | `AX-08`, `kleio.md`, `themis-audit` | The chronological legal ledger of health information access and exchange has strategic legal compliance value. | **Candidate Asset** (Informational Asset) |

#### 3. Strategic Resource Architecture Finding
While the *Authoritative Longitudinal Health Record*, the *Enterprise Healthcare Directory Graph*, and the *Tamper-Evident Audit Logbook* represent valuable information assets, **Harmonia does not require an explicit ArchiMate Strategy Resource model in Domain 02**.

*Architectural Justification*:
- In healthcare integration frameworks, information assets are governed by capabilities and formally modeled in **Domain 04 (Information Architecture)** as business and information objects.
- Elevating them to ArchiMate 3.2 `Resource` elements in Domain 02 creates redundant duplication with Domain 04 schemas and risks conflating information concepts with physical database implementations.
- **Formal Recommendation for Human Architectural Review**:
  > *"Harmonia does not currently require an explicit Strategy Resource model. The `resources/` directory in `docs/markdown/02-strategy/` should either be omitted or maintained as a lean placeholder documenting that enterprise information assets are formally owned by Domain 04."*

---

### Section E: Candidate Courses of Action (Strategic Approaches)

The repository reveals several strategic architectural approaches that Harmonia has adopted or is exploring. These are evaluated below using the **7-question criteria**:
1. *What source evidence supports it?*
2. *Is it genuinely a selected strategic approach?*
3. *Is it actually an Architectural Principle?*
4. *Is it a Requirement or Guardrail?*
5. *Is it instead downstream architecture/design?*
6. *Is it solution-specific?*
7. *Is it historical?*

*(Canonical `CoA-xx` identifiers are intentionally not assigned pending human review).*

#### Candidate Approach 1: Boundary Membrane Sovereignty
- **Description**: Terminate external messaging protocols at dedicated boundary gateways, converting external wire formats into internal canonical representations upon ingress and projecting internally managed representations into standards upon egress, ensuring that external protocols never dictate internal runtime semantics.
- **7-Question Evaluation**:
  1. *Source Evidence*: `docs/latex/chapters/01-motivation-strategy.tex`, `pylai.md`, `AX-02`, `AX-13`, `AGENTS.md` Invariant 9.
  2. *Genuinely Selected Approach*: Yes. Harmonia deliberately chooses a membrane architecture over end-to-end pass-through or generic point-to-point bridging.
  3. *Principle vs. Approach*: `AX-02` ("Standards at the boundary; Harmonia within") and `AX-13` ("Publication Terminates Management") are governing Principles. Deploying an isolating membrane adapter layer that enforces this boundary is the strategic Course of Action.
  4. *Requirement or Guardrail*: Implements `REQ-FND-001` and Invariant 9.
  5. *Downstream Design*: No; it governs the entire platform's integration posture.
  6. *Solution-Specific*: No; applies across HL7 v2, FHIR REST, DICOM, and LDS bulk exports.
  7. *Historical*: Active and foundational.
- **Classification Verdict**: **Valid Candidate Course of Action**.

#### Candidate Approach 2: Canonical Information Abstraction
- **Description**: Represent all in-flight operational activities and clinical task states using an immutable canonical domain model (`Pragma` envelopes and canonical domain models in `Calliope`), decoupling core business logic from protocol-specific variations.
- **7-Question Evaluation**:
  1. *Source Evidence*: `01-foundations.tex`, `calliope-canonical.md`, `pragma.md`, `AX-01`, `AX-14`.
  2. *Genuinely Selected Approach*: Yes. Harmonia explicitly rejects maintaining $N \times M$ direct protocol transformations.
  3. *Principle vs. Approach*: `AX-01` mandates health-information centricity; the canonical model pattern is the chosen strategic mechanism to achieve it.
  4. *Requirement or Guardrail*: Relates to `AX-14` (preserving semantic distinctions).
  5. *Downstream Design*: Defines the strategy, while specific DTOs belong to Domain 04 and Domain 05.
  6. *Solution-Specific*: Platform-wide.
  7. *Historical*: Active and implemented.
- **Classification Verdict**: **Valid Candidate Course of Action**.

#### Candidate Approach 3: Two-Tier State Separation (Active Coordination vs. Durable Truth)
- **Description**: Architecturally separate high-concurrency, ephemeral active operational coordination state from immutable, non-destructive durable persistence, allowing independent scaling, fault containment, and elimination of transactional database bottlenecks.
- **7-Question Evaluation**:
  1. *Source Evidence*: `01-foundations.tex`, `hestia.md`, `mneme.md`, `mnemosyne.md`, `AX-05`, `AX-11`, Assessment 5, Goal 3.
  2. *Genuinely Selected Approach*: Yes. Harmonia chose a dual-tier state architecture over a single monolithic transactional database.
  3. *Principle vs. Approach*: `AX-05` ("Active Coordination ≠ Authoritative State") is the governing Principle. Structuring the platform into distinct runtime active-state and durable-state subsystems is the Course of Action.
  4. *Requirement or Guardrail*: Implements `AX-05` and Invariant 8.
  5. *Downstream Design*: Technology choices (Infinispan vs PostgreSQL) belong to Technology Architecture; state separation is strategic.
  6. *Solution-Specific*: Platform-wide.
  7. *Historical*: The *approach* of state separation is active; the historical *write-behind* persistence implementation mechanism is questioned.
- **Classification Verdict**: **Valid Candidate Course of Action**.

#### Candidate Approach 4: Intrinsic Default-Deny Security Fabric
- **Description**: Embed caller security context evaluation, multi-gate authorization, and non-PHI audit capture into every internal platform operation and message hop, rather than relying on perimeter network defenses or voluntary caller compliance.
- **7-Question Evaluation**:
  1. *Source Evidence*: `themis-security.md`, `themis.md`, `AX-07`, `REQ-FND-003`, `AGENTS.md` Invariant 6, Assessment 4.
  2. *Genuinely Selected Approach*: Yes. Harmonia rejects perimeter-only trust in favor of embedded zero-trust security evaluation.
  3. *Principle vs. Approach*: `AX-07` ("Security Is Intrinsic") is the governing Principle. Developing an embedded authorization engine with mandatory interceptor gates is the strategic Course of Action.
  4. *Requirement or Guardrail*: Enforces `REQ-FND-003` and Invariant 6.
  5. *Downstream Design*: Interceptor filter chains and token structures belong to Domain 08.
  6. *Solution-Specific*: Platform-wide.
  7. *Historical*: Active and central to recent convergence work.
- **Classification Verdict**: **Valid Candidate Course of Action**.

#### Candidate Approach 5: Observable Entity-Activity Lifecycle Progression
- **Description**: Bind distributed multi-hop integration tasks and message fan-outs to explicit, observable state machines that progress in synchronized lockstep with the real-world status of the clinical entity, eliminating silent destination divergence.
- **7-Question Evaluation**:
  1. *Source Evidence*: `energeia-workflow.md`, `ponos.md`, `pragma.md`, `AX-16`, Assessment 2, Goal 6.
  2. *Genuinely Selected Approach*: Yes. Moves beyond fire-and-forget message passing to tracked activity progression.
  3. *Principle vs. Approach*: `AX-16` is the governing Principle; implementing discrete Erga activities with granular destination checkpointing is the Course of Action.
  4. *Requirement or Guardrail*: Implements `REQ-FND-002` and Invariant 5.
  5. *Downstream Design*: Camel routes and Ergon classes belong to Domain 05.
  6. *Solution-Specific*: Platform-wide.
  7. *Historical*: Active and implemented.
- **Classification Verdict**: **Valid Candidate Course of Action**.

#### Candidate Approach 6: Enterprise Directory Federation & Local Governance
- **Description**: Implement a relationship-centric local healthcare directory that aggregates and synchronizes authoritative national references while maintaining locally authoritative relationships and operational enrichments.
- **7-Question Evaluation**:
  1. *Source Evidence*: `Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`, `01-motivation-strategy.tex`, Driver 6.
  2. *Genuinely Selected Approach*: Yes. Specifically chosen to align with the Australian LDS direction while remaining independent of single vendor transports.
  3. *Principle vs. Approach*: Strategic position brief defines this as a strategic architectural approach (`HARM-STR-001`, `HARM-LDS-001..012`).
  4. *Requirement or Guardrail*: Directly drives LDS requirements catalogue.
  5. *Downstream Design*: Solution-pack and information models belong to Domains 04 and 11.
  6. *Solution-Specific*: Partially. It is a strategic approach for the Provider Directory problem space, which acts as the initial proving case for the platform.
  7. *Historical*: Highly current (dated 24 September 2026).
- **Classification Verdict**: **Valid Candidate Course of Action** (with the note that it operates as a specialized proving-case approach under the broader platform strategy).

---

### Section F: Candidate Capability Map (Natural Groupings)

The repository evidence does not support an arbitrary, heavily nested capability hierarchy. Instead, observational evidence reveals **five natural capability groupings** based on architectural affinity:

```text
+---------------------------------------------------------------------------------------------------+
| 1. Interoperability & Gateway Capabilities                                                        |
|    - Standards-Based Protocol Ingress & Egress                                                    |
|    - Resilient Asynchronous Event Decoupling & Distribution                                      |
+---------------------------------------------------------------------------------------------------+
| 2. Information Management & Governance Capabilities                                              |
|    - Enterprise Healthcare Directory Management                                                   |
|    - Vendor-Neutral Longitudinal Health Record Preservation                                       |
|    - Responsive Managed Information Access                                                        |
+---------------------------------------------------------------------------------------------------+
| 3. Operational Activity & Clinical Coordination Capabilities                                      |
|    - Coordinated Clinical Activity Progression                                                    |
|    - Care Team Communication & Collaboration Gateway                                              |
+---------------------------------------------------------------------------------------------------+
| 4. Security, Trust & Assurance Capabilities                                                       |
|    - Intrinsic Access Governance & Policy Evaluation                                              |
|    - Tamper-Evident Operational Audit & Provenance Evidence                                      |
|    - Synthetic Clinical Simulation & Environment Assurance                                         |
+---------------------------------------------------------------------------------------------------+
| 5. Presentation & Human Interaction Capabilities                                                  |
|    - Point-of-Care Clinical Presentation                                                          |
|    - (Candidate: Directory Administration & Operational Telemetry)                                |
+---------------------------------------------------------------------------------------------------+
```

*Hypothesis Demarcation*: This clustering is presented strictly as an **observational hypothesis**. The canonical capability map will be formally agreed during human architectural review.

---

### Section G: Motivation → Strategy Relationships (Traceability)

Traceability between canonical Domain 01 (Motivation) and candidate Domain 02 (Strategy) elements is established along primary architectural paths, avoiding an artificial fully connected graph:

#### 1. Drivers & Assessments → Strategic Approaches (Courses of Action)
- `Driver: Clinical Interoperability Across Heterogeneous Standards`
  $\longrightarrow$ *Approach: Boundary Membrane Sovereignty* & *Approach: Canonical Information Abstraction*
- `Assessment: Silent Data Loss via False Acceptance`
  $\longrightarrow$ *Approach: Boundary Membrane Sovereignty* (coupled with durable ingress boundary)
- `Assessment: Centralised Synchronous Persistence Can Constrain Concurrency` & `Driver: Continuous Clinical Service Availability`
  $\longrightarrow$ *Approach: Two-Tier State Separation (Active Coordination vs. Durable Truth)*
- `Assessment: Implicit Perimeter Trust and Diagnostic Data Leakage` & `Driver: Statutory Health Information Privacy & Protection`
  $\longrightarrow$ *Approach: Intrinsic Default-Deny Security Fabric*
- `Assessment: Unmonitored Destination Failure and Operational Divergence` & `Driver: Coordinated Operational Activity and Entity State`
  $\longrightarrow$ *Approach: Observable Entity-Activity Lifecycle Progression*
- `Driver: National Healthcare Directory / Endpoint Governance`
  $\longrightarrow$ *Approach: Enterprise Directory Federation & Local Governance*

#### 2. Strategic Platform Goals & Axioms → Candidate Capabilities
- `Goal 1: Durable Acceptance & Preservation of Clinical Events`
  - Governed by: `AX-02`, `AX-03`, `AX-10`, `REQ-FND-001`
  - Realized by: *Standards-Based Protocol Ingress & Egress* and *Vendor-Neutral Longitudinal Health Record Preservation*
- `Goal 2: Timely Clinical Availability & Non-Destructive Progression`
  - Governed by: `AX-10`, `AX-15`
  - Realized by: *Resilient Asynchronous Event Decoupling & Distribution*
- `Goal 3: Responsive Access to Managed Information`
  - Governed by: `AX-05`, `AX-11`
  - Realized by: *Responsive Managed Information Access*
- `Goal 4: Reliable Subject Identity & Referential Integrity`
  - Governed by: `AX-01`, `AX-06`, `AX-14`, `REQ-FND-003`
  - Realized by: *Enterprise Healthcare Directory Management* and *Vendor-Neutral Longitudinal Health Record Preservation*
- `Goal 5: Vendor-Independent Longitudinal Clinical Information Coherence`
  - Governed by: `AX-01`, `AX-04`, `AX-06`
  - Realized by: *Vendor-Neutral Longitudinal Health Record Preservation* and *Point-of-Care Clinical Presentation*
- `Goal 6: Coordinated Progression of Operational Activities & Associated Entity State`
  - Governed by: `AX-15`, `AX-16`, `REQ-FND-002`, `REQ-FND-004`
  - Realized by: *Coordinated Clinical Activity Progression*

#### 3. Candidate Capabilities → Clinical Business Outcomes
- **Outcome 1: Reduced Clinical Risk from Unavailable, Fragmented or Incorrectly Associated Information**
  - Contributed by: *Vendor-Neutral Longitudinal Health Record Preservation*, *Enterprise Healthcare Directory Management*, *Point-of-Care Clinical Presentation*.
- **Outcome 2: Continuous and Resilient Regional Health Information Exchange**
  - Contributed by: *Standards-Based Protocol Ingress & Egress*, *Resilient Asynchronous Event Decoupling & Distribution*, *Responsive Managed Information Access*, *Coordinated Clinical Activity Progression*.
- **Outcome 3: Demonstrable Protection and Accountable Handling of Health Information**
  - Contributed platform-wide by: *Intrinsic Access Governance & Policy Evaluation*, *Tamper-Evident Operational Audit & Provenance Evidence*.

#### 4. Observed Clinical Value Stream Flow
As documented across LaTeX specifications (`01-foundations.tex`, `01-motivation-strategy.tex`, `02-business-layer.tex`), Harmonia's capabilities execute within an overarching clinical value stream:

$$\text{Ingest Trigger} \longrightarrow \text{Transport \& Deduplicate} \longrightarrow \text{Orchestrate Erga} \longrightarrow \text{Validate \& Reconcile} \longrightarrow \text{Update Clinical State} \longrightarrow \text{Dispatch Egress / Expose APIs}$$

- **Ingest Trigger**: Terminate external protocol (MLLP/REST), validate framing, screen security (`Pylai`, `Themis`).
- **Transport & Deduplicate**: Enqueue to durable messaging, enforce sliding-window deduplication (`Petasos`).
- **Orchestrate Erga**: Dequeue task, instantiate canonical `Pragma`, route to modular activity pipeline (`Energeia Ponos/Praxis`).
- **Validate & Reconcile**: Evaluate business rules, referential integrity, and authority context (`Erga`, `Calliope`).
- **Update Clinical State**: Commit non-destructive versioned update to durable truth (`Mnemosyne`) and refresh active cache (`Mneme`).
- **Dispatch Egress / Expose APIs**: Dispatch outbound notifications to remote systems or serve point-of-care queries (`Pylai`, `Iris`).

---

### Section H: Strategy → Architecture Handoff (Downstream Boundaries)

Strategy defines *what* must be achieved and *which* strategic approaches are chosen; downstream architecture domains define *how* those intentions are structurally and behaviorally realized. Handoffs must remain at the **architectural-concern level**:

| Strategy Capability Hypothesis | Target Downstream Domain | Downstream Architectural Concerns to Be Defined |
| :--- | :--- | :--- |
| **Standards-Based Protocol Ingress & Egress** | Domain 06: Integration Architecture | Protocol driver models, boundary validation contracts, non-destructive projection rules, connection pooling, ACK/NACK generation lifecycles. |
| **Enterprise Healthcare Directory Management** | Domain 04: Information Architecture & Domain 11: Solution Packs | Practitioner/Organization/Location semantic graphs, national HIPS/bulk export synchronization adapters, referential integrity state machines. |
| **Vendor-Neutral Longitudinal Health Record Preservation** | Domain 04: Information Architecture & Domain 07: Technology | Canonical clinical schemas, non-destructive versioning semantics, source authority attribution models, relational persistence schema mappings. |
| **Resilient Asynchronous Event Decoupling & Distribution** | Domain 07: Technology Architecture & Domain 06: Integration | Physical message broker topology, clustering failover protocols, queue/topic addressing conventions, transport security. |
| **Coordinated Clinical Activity Progression** | Domain 05: Application Architecture | Activity unit interfaces (Erga), workflow sequence chaining (Praxis), Pragma checkpointing contracts, multi-destination fan-out state machines. |
| **Responsive Managed Information Access** | Domain 07: Technology Architecture & Domain 05: Application | Distributed cache clustering topologies, active-state data partitioning, cache coherence and invalidation patterns, client query interfaces. |
| **Point-of-Care Clinical Presentation** | Domain 05: Application Architecture (Presentation Tier) | Presentation client boundaries, Backend-For-Frontend gateway contracts, user session governance, read-oriented timeline composition. |
| **Intrinsic Access Governance & Policy Evaluation** | Domain 08: Security Architecture | ABAC/RBAC policy models, security context propagation contracts, authorization gate interceptor lifecycles, cryptographic token validation. |
| **Tamper-Evident Operational Audit & Provenance Evidence** | Domain 08: Security Architecture & Domain 04: Information | AuditEvent and Provenance FHIR schemas, non-PHI sanitization filters, immutable log append mechanisms, cryptographic hashing. |
| **Care Team Communication & Collaboration Gateway** | Domain 05: Application Architecture | Collaboration channel mapping, room lifecycle management, notification dispatch models, external Matrix DTO isolation. |
| **Synthetic Clinical Simulation & Environment Assurance** | Domain 10: Verification & Simulation | Synthetic profile generators, actor emulators (PAS, EMR, LIS), failure injection hooks, production isolation verification. |

---

### Section I: Reclassification Candidates (Routing Table)

The analysis identified substantial implementation-specific, physical, and component-level details currently positioned in historical Strategy/Foundations material that must be routed to downstream domains:

| Misclassified Material in Current Sources | Current Location | Proposed Domain Destination | Architectural Rationale for Reclassification |
| :--- | :--- | :--- | :--- |
| **ActiveMQ Artemis Clustered Broker & Queue Topologies** | `01-motivation-strategy.tex`, Table 1.1; `fig-motivation-map.tex` | **Domain 07: Technology Architecture** | Artemis is a specific middleware implementation. Physical clustering, failover times (<2s), and JMS connection pools belong to Technology Architecture. |
| **Infinispan Cache Topologies & Hot Rod Port 11222** | `01-motivation-strategy.tex`, Table 1.1; `01-foundations.tex` | **Domain 07: Technology Architecture** | In-memory data grid clustering, memory eviction algorithms, and Hot Rod wire protocols are technology infrastructure concerns. |
| **Netty TCP Socket Framing & MLLP Delimiters (`<VT>`, `<FS><CR>`)** | `02-conceptual-taxonomy.tex`, Section 2.2; `02-business-layer.tex` | **Domain 06: Integration Architecture** | Low-level wire framing, TCP socket listeners, port 2575 bindings, and Netty channel pipelines belong to Integration Architecture. |
| **PostgreSQL 16 Relational DDL & SQL Schemas (`hie_fhir_resources`)** | `02-conceptual-taxonomy.tex`, Section 2.5; `02-business-layer.tex` | **Domain 04: Information Architecture** | Relational table definitions, SQL column mappings, foreign keys, and database indexes are Information/Data Architecture concerns. |
| **WildFly 31 Jakarta EE Deployment Descriptors & WAR Packaging** | `02-conceptual-taxonomy.tex`, Section 2.1 & 2.4 | **Domain 05: Application Architecture** | Servlet specifications, WAR packaging, JAX-RS endpoint descriptors, and application server runtimes belong to Application Architecture. |
| **Vue 3 Single Page Application Components & Pinia Stores** | `02-conceptual-taxonomy.tex`, Section 2.1 | **Domain 05: Application Architecture** (Presentation) | Frontend UI component trees, TypeScript interfaces, and state stores belong to the Application Presentation tier. |
| **Matrix Synapse Admin REST API & Application Service Port 8092** | `02-conceptual-taxonomy.tex`, Section 2.7 | **Domain 05: Application Architecture** (Collaboration) | Specific Matrix protocol DTOs, Synapse Admin HTTP endpoints, and AS registration tokens belong to downstream Collaboration Application design. |
| **Specific HL7 Segment Rules (`MSH`, `MSA-1 = AA/AE`)** | `01-motivation-strategy.tex`, `02-business-layer.tex` | **Domain 06: Integration Architecture** | Protocol-specific acknowledgment code parsing and segment mapping belong to Integration Architecture adapter specifications. |

---

### Section J: Historical / Superseded Material (Disposition Table)

Material identified across the repository that is historical, superseded, or contradictory is evaluated below with formal disposition recommendations:

| Historical / Contradictory Material | Source Location | Discovered Inconsistency / Conflict | Recommended Disposition | Human Review Required? |
| :--- | :--- | :--- | :--- | :--- |
| **Component Names Labeled as Capabilities** | `01-motivation-strategy.tex`, Table 1.1; `fig-motivation-map.tex` | Equating software components (`Pylai`, `Petasos`, `Mneme`, `Energeia`) with capabilities violates ArchiMate 3.2. | **Rewrite & Decouple**: Decouple capabilities from component names in Domain 02; map components as downstream realisations in Domain 05. | No (Standard ArchiMate compliance) |
| **Write-Behind Caching as Exclusive Persistence Model** | `01-foundations.tex`, Section 1.4; `01-motivation-strategy.tex` | Historical drafts assert workflow pipelines write exclusively to cache, which asynchronously writes to disk. `AX-05` and recent convergence establish that Mneme is ephemeral/reconstructable, and Mnemosyne alone holds authoritative truth. | **Potentially Superseded — Requires Human Review**: Document that write-behind as the sole persistence mechanism is questioned; human architect must decide whether it is fully superseded or retained as an optional optimization. | **YES (Critical Architectural Decision)** |
| **LDS as Whole-of-Harmonia Scope** | Early project drafts & preliminary scopes | Conflating the Local Directory Solution with the entire platform scope conflicts with the multi-purpose vision. | **Reclassify & Bound**: Superseded by Strategic Position Brief (24 Sept 2026); classify LDS as a Domain 11 Solution Pack running on core platform capabilities (`HARM-STR-001`). | No (Established by approved position brief) |
| **ArchiMate Motivation Map (Old Diagram)** | `docs/latex/diagrams/fig-motivation-map.tex` | Mixes principles, requirements, and component-named capabilities in a legacy layout that contradicts canonical Domain 01. | **Retire from Strategy**: Retire from canonical Domain 02; replace with clean ArchiMate 3.2 Strategy views when authoring occurs. | No (Historical LaTeX artifact) |
| **Direct DB Calls from Presentation Tier** | Historical concept notes | Early notes blurred whether Iris could directly query PostgreSQL for speed. `AGENTS.md` Invariant 3 strictly forbids Iris from depending on JPA/PostgreSQL. | **Superseded**: Formally superseded by `AGENTS.md` Invariant 3 and `IrisDecouplingArchitectureTest`. | No (Enforced by automated ArchUnit tests) |

---

### Section K: Contradictions and Open Questions (For Architectural Review)

The following contradictions and unresolved questions cannot be resolved by assumption and are submitted for formal human architectural review:

#### 1. Question: Canonical Capability Freezing
- *Context*: The analysis identified 11 candidate capabilities.
- *Open Question*: Should **Point-of-Care Clinical Presentation** (Iris) and **Synthetic Clinical Simulation & Environment Assurance** (Paradeigma) be included as first-class Strategic Capabilities in Domain 02, or should they be classified respectively as an Application Presentation Concern (Domain 05) and a Verification Concern (Domain 10)?
- *Architectural Trade-off*: Including them reflects the complete operational platform scope; excluding them maintains a strictly lean integration-and-data core.

#### 2. Question: Formal Status of the Strategy Resource Model
- *Context*: ArchiMate 3.2 allows explicit Resource modeling, but software/database artifacts belong to downstream domains, while information assets belong to Domain 04.
- *Open Question*: Does the human architect approve omitting the ArchiMate Resource model from Domain 02 (recommended), or is there a specific enterprise requirement to model high-level informational assets (`Longitudinal Clinical Record Asset`, `Enterprise Directory Graph Asset`) at the Strategy layer?

#### 3. Question: Persistence Model Authority & Supersession
- *Context*: Historical specifications emphasize write-behind caching (`Mneme -> Mnemosyne`). Convergence work and `AX-05` emphasize that Mneme is ephemeral and Mnemosyne is authoritative durable truth.
- *Open Question*: Has write-behind cache persistence been formally superseded by synchronous/governed persistence to Mnemosyne, or does write-behind remain an approved asynchronous write-path option for specific high-volume, non-critical event streams?

#### 4. Question: Enterprise Healthcare Directory Architecture Scope
- *Context*: The 24 September 2026 Strategic Brief positions the Local Directory Solution as a proving case and a Solution Pack (`HARM-STR-001`).
- *Open Question*: Should Domain 02 define an abstract "Enterprise Healthcare Directory Management" capability that is subsequently realized by Domain 11 (Provider Directory Solution Pack), or does Domain 02 only define generic "Information Management & Preservation", leaving all directory-specific concepts to Domain 11?

#### 5. Question: Collaboration Gateway (Agora) Strategic Standing
- *Context*: Agora integrates Matrix Synapse for clinical communication. `AGENTS.md` Invariant 10 notes that Agora makes Matrix a collaboration capability, but does not make Matrix the Harmonia architecture.
- *Open Question*: Is Care Team Communication a core platform capability of Harmonia, or is Agora an optional downstream integration module?

---

### Section L: Recommended Domain 02 Documentation Set

Once human architectural review resolves the open questions above, canonical documentation should be authored under `docs/markdown/02-strategy/` conforming to the approved working taxonomy.

*Constraint Check*: No files or directories were created under `docs/markdown/02-strategy/` during this analysis.

#### Recommended Target File-by-File Plan

```text
docs/markdown/02-strategy/
├── README.md
│   └── Overview of Domain 02, ArchiMate Strategy layer boundaries, and navigation index.
├── capabilities/
│   ├── README.md (Summary catalogue and capability index)
│   ├── protocol-ingress-egress.md
│   ├── enterprise-directory-management.md
│   ├── longitudinal-record-preservation.md
│   ├── asynchronous-event-distribution.md
│   ├── coordinated-activity-progression.md
│   ├── responsive-information-access.md
│   ├── clinical-record-presentation.md
│   ├── intrinsic-access-governance.md
│   ├── operational-audit-provenance.md
│   ├── care-team-collaboration-gateway.md
│   └── synthetic-clinical-simulation.md
├── resources/
│   └── README.md
│       └── Architecture position documenting the lean resource model: why Harmonia
│           omits physical/component resources from Strategy and delegates informational
│           assets to Domain 04 (Information Architecture).
├── courses-of-action/
│   ├── README.md (Catalogue of strategic architectural approaches)
│   ├── boundary-membrane-sovereignty.md
│   ├── canonical-information-abstraction.md
│   ├── two-tier-state-separation.md
│   ├── intrinsic-default-deny-security.md
│   ├── observable-lifecycle-progression.md
│   └── enterprise-directory-federation.md
├── capability-maps/
│   ├── README.md (Natural capability groupings, cluster definitions, and hierarchy)
│   └── platform-capability-map.md (Visual and structured ArchiMate capability map)
└── strategic-views/
    ├── README.md (Overview of strategic viewpoint models)
    ├── motivation-strategy-traceability.md (Detailed Driver/Goal -> Strategy trace)
    ├── clinical-value-stream-view.md (Strategic value stream flow and stage definitions)
    └── downstream-handoff-boundaries.md (Concern-level handoffs to Domains 03–11)
```

---
*Report Compiled & Delivered: 2026-10-04*
*Author: Autonomous Architecture Agent (Junie)*
*Repository: Harmonia Health Integration Environment (`net.fhirfactory.harmonia`)*
