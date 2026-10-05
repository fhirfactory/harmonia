---
sessionId: session-261004-140056-jpa1
---

# Requirements

### Overview & Goals

Domain 01 — Motivation is complete and established as canonical under `docs/markdown/01-motivation/`. The purpose of this task is to discover and reconcile existing repository evidence relevant to **Domain 02 — Strategy** before any canonical Strategy documentation is authored.

Domain 02 must explain how Harmonia intends to respond to that Motivation at the strategic architecture level, without prematurely defining Application Architecture, implementation mechanisms, named components, middleware, deployment technologies, or solution-specific designs.

This task is an **analysis, reconciliation, and classification activity only**. It inspects, extracts, compares, classifies, and reports existing Strategy-relevant material across Markdown, LaTeX, and strategic DOCX sources. It identifies candidate capabilities, resources, courses of action, capability groupings, and strategic relationships strictly where supported by repository evidence.

The target outcome is an evidence-based analysis report that enables human architectural review and decision-making:
> *"Here is what the repository appears to say, here are the candidate Strategy concepts that can reasonably be extracted from it, here is where the sources disagree, and here are the decisions that still need to be made."*

It must **not** decide the canonical Strategy model or prescribe:
> *"Here is the Strategy architecture I have decided Harmonia should use."*

---

### Revised Execution Principle

The analysis strictly adheres to an evidence-first, decision-deferred progression:

```text
Repository Evidence
        ↓
     Extract
        ↓
    Classify
        ↓
Compare / Reconcile
        ↓
Candidate Strategy Concepts
        ↓
Contradictions / Questions
        ↓
HUMAN ARCHITECTURAL REVIEW
        ↓
Canonical Strategy Model (future authoring task)
```

---

### Scope

#### In Scope
- Comprehensive analysis of existing Strategy material across the repository:
  - LaTeX sources: `docs/latex/chapters/01-motivation-strategy.tex`, `01-motivation-strategy-old.tex`, `01-foundations.tex`, `02-conceptual-taxonomy.tex`, `02-business-layer.tex`, `styles/harmonia-archimate.sty`, and `diagrams/fig-motivation-map.tex`.
  - Markdown sources: `docs/concepts/` (all 16 subsystem concept documents), `docs/architecture/` (overview, runtime-architecture, execution-model), and `docs/markdown/01-motivation/` (authoritative baseline).
  - Strategic Brief: `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`.
- Discovery and extraction of:
  - Candidate Strategic Capabilities (decoupled from named components).
  - Candidate Strategic Resources (evaluated against ArchiMate 3.2 criteria).
  - Candidate Courses of Action (strategic architectural approaches).
  - Candidate Capability Maps (natural groupings supported by evidence).
  - Strategic Views and Clinical Value Stream Flows.
- Principal Motivation → Strategy relationships tracing back to canonical Domain 01.
- Identification of Strategy → Downstream Architecture handoff boundaries.
- Reclassification recommendations for material currently positioned as Strategy that belongs in downstream domains (Domains 03–13).
- Identification of historical, superseded, or contradictory material with retention/retirement recommendations.
- Identification of unresolved contradictions and open questions requiring human architectural decisions.
- Recommendation of a coherent target Markdown document set under the approved taxonomy `docs/markdown/02-strategy/`.
- Compilation of the final analysis report and authoring of the completion report at `.junie/reports/2026-10-04-domain-02-strategy-analysis.md`.

#### Out of Scope
- Authoring or modifying canonical Domain 02 documentation files under `docs/markdown/02-strategy/`.
- Modifying canonical Domain 01 documentation files under `docs/markdown/01-motivation/`.
- Modifying production Java/TypeScript source code, POM files, or build configurations.
- Redesigning downstream architecture domains (Business, Information, Application, Integration, Technology, Security).
- Resolving unresolved architectural ambiguity or making authoritative architectural choices by assumption.

---

### User Stories

- **As an Enterprise & Solutions Architect**, I want a rigorous analysis of Harmonia's strategic capabilities, resources, and courses of action before canonical authoring begins, so that I can validate that the platform strategy faithfully addresses enterprise motivation without collapsing into component implementation.
- **As a System Modeler**, I want named components (e.g. Pylai, Mneme, Ponos, Artemis) stripped of their false "capability" labels and mapped to underlying platform capabilities and downstream realizations, so that our ArchiMate models remain conceptually sound and standard-compliant.
- **As a Platform Governance Steward**, I want clear reclassification and supersession guidance for legacy LaTeX and DOCX material, so that historical implementation details are cleanly routed to downstream domains rather than polluting the Strategy layer.
- **As a Technical Author or Agent**, I want a recommended structure and boundary definition for `docs/markdown/02-strategy/`, so that authoring can proceed rapidly and consistently once the conceptual model is frozen.

---

### Functional Requirements

1. **Approved Taxonomy Compliance**:
   - Evaluate all material against the approved working taxonomy:
     ```text
     docs/markdown/02-strategy/
     ├── README.md
     ├── capabilities/
     ├── resources/
     ├── courses-of-action/
     ├── capability-maps/
     └── strategic-views/
     ```
   - Do not create directories or files under `docs/markdown/02-strategy/` during this task.

2. **Strict Domain Boundary Demarcation**:
   - Maintain the ArchiMate / TOGAF conceptual boundary:
     - **Motivation**: Why does Harmonia exist and what must it achieve?
     - **Strategy**: What capabilities, resources, and courses of action are required to respond to that Motivation?
     - **Architecture (Downstream)**: How are those capabilities realised structurally and behaviourally?
   - Prevent collapsing Strategy into Application Architecture, Technology Architecture, or component design.

3. **Component-Decoupled Capability Discovery & Evidence Classification**:
   - Identify candidate capabilities describing *what Harmonia must be able to do*, not the component that performs it.
   - Decouple capabilities from named Harmonia components (Pylai, Mneme, Mnemosyne, Ponos, Calliope, Iris, Artemis, Infinispan, HAPI FHIR).
   - Treat all candidate capabilities strictly as **analysis hypotheses**, not pre-frozen or canonical concepts.
   - For every candidate capability, author a detailed profile:
     - Candidate Capability
     - Purpose
     - Motivation Drivers / Goals / Outcomes Served (using exact canonical names from Domain 01)
     - Supporting Source Material (specific files, sections, tables, equations)
     - Potential Downstream Realisation (conceptual architectural concerns, not prescriptive classes)
     - Evidence Derivation Category:
       - `Explicitly Defined in Source`
       - `Inferred from Multiple Sources`
       - `Derived from Component Responsibilities`
       - `Historical`
       - `Conflicting`
       - `Weakly Supported`
     - Classification Confidence (`High` / `Medium` / `Low`)
     - Conflicts / Questions (requiring human review)
   - Do not optimize the candidate capability model for artificial completeness or symmetry.

4. **Strategic Resources as an Open Question**:
   - Investigate whether modeling explicit ArchiMate 3.2 Resources adds meaningful architectural value for Harmonia.
   - Do not assume that data stores, schemas, audit logs, or directory graphs are automatically Strategy Resources.
   - Distinguish strategic resources from application components, infrastructure, databases, FHIR resources, Java classes, implementation libraries, and deployment artifacts.
   - Note explicitly that a valid and acceptable outcome is: *"Harmonia does not currently require an explicit Strategy Resource model."*

5. **Courses of Action Discovery (Not Design)**:
   - Identify candidate Courses of Action representing strategic approaches Harmonia has chosen or appears to have chosen based on repository evidence.
   - Do not design a canonical `CoA-xx` catalog during this analysis.
   - For each potential Course of Action, systematically evaluate:
     1. What source evidence supports it?
     2. Is it genuinely a selected strategic approach?
     3. Is it actually an Architectural Principle? (Preserve the strict distinction: Principles constrain/guide; Courses of Action describe chosen approaches to achieve goals).
     4. Is it a Requirement or Guardrail?
     5. Is it instead downstream architecture/design?
     6. Is it solution-specific?
     7. Is it historical?

6. **Capability Clustering (Hypotheses, Not a Designed Map)**:
   - Identify natural clustering and groupings supported by repository evidence.
   - Treat all capability groupings strictly as observational hypotheses.
   - Do not force candidate capabilities into an artificial finished hierarchy or pre-frozen capability map during this task; the canonical map will be agreed during human architectural review.

7. **Motivation → Strategy Traceability (Exact Canonical Alignment)**:
   - Trace candidate Strategy elements strictly against authoritative Domain 01 material (`docs/markdown/01-motivation/`).
   - Use exact canonical names and semantics for Drivers, Assessments, Goals, Outcomes, Axioms, Foundational Requirements, and External Constraints.
   - Never invent shorthand or unofficial Motivation elements (e.g. no "Goal 2: Zero In-Flight Loss", no "Driver: Care Team Coordination", no "Driver: Operational Reliability").
   - Where historical documentation references Motivation concepts that no longer exist canonically, flag them as historical/superseded references and map them only where justified.
   - Discover and report candidate relationships without constructing a premature canonical graph.

8. **Conceptual Strategy → Downstream Architecture Handoff**:
   - Identify likely downstream ownership at the architectural-concern level (e.g. Integration Architecture defines protocols, boundary behaviour, transformations, and interaction patterns).
   - Strictly avoid prescribing implementation/design details (such as Netty/Camel pipelines, MLLP framing handlers, REST resource controllers, or PostgreSQL DDL) which belong to downstream domain reconciliation.

9. **Careful Reclassification and Historical Supersession Tracking**:
   - Explicitly identify material currently presented as Strategy that more properly belongs in downstream domains (Domains 03–13).
   - Identify historical, superseded, or contradictory material and provide clear recommendations (retain, rewrite, reclassify, supersede, retire).
   - Do not state that a historical approach has been superseded unless that status is proven by canonical architecture or an explicit architecture decision.
   - Where uncertain (such as write-behind cache persistence vs dual-write/governed models), explicitly report: `Potentially Superseded — Requires Human Review`.

10. **Analysis Report & Completion Report**:
    - Produce a comprehensive analysis report containing Sections A through L as requested in the issue description.
    - Author the formal completion report at `.junie/reports/2026-10-04-domain-02-strategy-analysis.md` conforming to the specified format.

---

### Non-Functional Requirements & Governance Guardrails

- **Zero-Modification Constraint**: No production source code, POM files, or canonical documentation (`docs/markdown/01-motivation/` or new `docs/markdown/02-strategy/`) shall be created or modified.
- **Architectural Neutrality & Grounding**: All candidate capabilities, resources, and courses of action must be grounded in concrete repository evidence (LaTeX, Markdown, DOCX) and aligned with the authoritative Architectural Axioms (AX-01..AX-16) and `AGENTS.md`.
- **Preservation of Uncertainty (AX-15)**: Where architectural intent is ambiguous, conflicting, or unsupported by evidence, document the gap as an open question for human review rather than inventing a solution.

# Technical Design

### Current Implementation & Baseline Evidence

The Harmonia repository contains substantial material addressing Strategy concerns, but this material is currently fragmented across multiple formats, historical drafts, and mixed abstraction layers:

1. **Master LaTeX Specification (`docs/latex/`)**:
   - `chapters/01-motivation-strategy.tex` & `chapters/01-foundations.tex`: Historically combined Motivation and Strategy into a single chapter. Table 1.1 (`tab:strategic_capabilities`) defines "Harmonia Strategic Capabilities and Subsystem Realization", but conflates capabilities with owning subsystems (e.g. "Clinical Gateway Interfaces -> Pylai", "In-Memory Cache Grid -> Hestia Mneme", "High-Availability Messaging -> Petasos", "Modular Task Execution -> Energeia").
   - `diagrams/fig-motivation-map.tex`: ArchiMate diagram mixing principles, requirements, and subsystem capabilities (e.g. `cap_ingest: Gateway Ingestion (Pylai)`, `cap_msg: Clustered Messaging (Petasos)`, `cap_workflow: Modular Workflows (Ponos)`).
   - `styles/harmonia-archimate.sty`: Defines ArchiMate 3.2 styling for `archi-capability` and `archi-valuestream` (`#FDF4E3` fill, `#B58428` draw), but lacks styles for `archi-resource` and `archi-courseofaction`.
   - `chapters/02-conceptual-taxonomy.tex` & `chapters/02-business-layer.tex`: Define clinical business services and subsystem operational boundaries, containing strategic value stream flows and lifecycle processes.

2. **Strategic Position Brief (`docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`)**:
   - Authored 24 September 2026 as an architectural position paper.
   - Articulates the strategic relationship between Harmonia's enterprise Local Directory capability (aligned to the Australian Local Directory Solution / LDS direction) and the broader multi-purpose platform vision (patient identity, longitudinal clinical information, vendor-neutral FHIR/HL7 interoperability, cross-domain workflow).
   - Defines strategic requirements (`HARM-STR-001`, `HARM-LDS-001..012`) and architecture guardrails (preventing Harmonia from being reduced to a single-purpose LDS product or coupling core domain models to specific national transports).

3. **Subsystem Concepts & Architectural Overviews (`docs/concepts/`, `docs/architecture/`)**:
   - Detailed concept documents for 16 architectural elements (`harmonia`, `hestia`, `mneme`, `mnemosyne`, `petasos`, `energeia`, `ponos`, `ergon`, `praxis`, `pragma`, `pylai`, `calliope`, `themis`, `iris`, `agora`, `paradeigma`).
   - Define subsystem responsibilities, anti-responsibilities, and runtime lifecycles that contain embedded strategic intent waiting to be decoupled from component names.

4. **Canonical Motivation Baseline (`docs/markdown/01-motivation/`)**:
   - Authoritative, frozen Motivation model comprising:
     - **6 Enterprise Stakeholders**: Regional Health Network Operator, Healthcare Delivery Organizations, Clinicians & Care Teams, Patients & Care Recipients, Platform Operations & Integration Engineers, Healthcare Directory Stewards & Registrars.
     - **5 External Authorities**: Australian Digital Health Agency (ADHA), HI Service Operator, Australian Health Practitioner Regulation Agency (AHPRA), Office of the Australian Information Commissioner (OAIC), Standards Development Organisations (SDOs).
     - **7 Enterprise & Clinical Drivers**:
       1. `Clinical Interoperability Across Heterogeneous Standards`
       2. `Durable Clinical Information Independence`
       3. `Continuous Clinical Service Availability`
       4. `Patient Safety & Identity Integrity`
       5. `Statutory Health Information Privacy & Protection`
       6. `National Healthcare Directory / Endpoint Governance`
       7. `Coordinated Operational Activity and Entity State`
     - **5 Architecture Assessments**:
       1. `Silent Data Loss via False Acceptance`
       2. `Information Fragmentation and Semantic Drift`
       3. `Centralised Synchronous Persistence Can Constrain Concurrency`
       4. `Implicit Perimeter Trust and Diagnostic Data Leakage`
       5. `Unmonitored Destination Failure and Operational Divergence`
     - **6 Strategic Platform Goals**:
       1. `Durable Acceptance & Preservation of Clinical Events`
       2. `Timely Clinical Availability & Non-Destructive Progression`
       3. `Responsive Access to Managed Information`
       4. `Reliable Subject Identity & Referential Integrity`
       5. `Vendor-Independent Longitudinal Clinical Information Coherence`
       6. `Coordinated Progression of Operational Activities & Associated Entity State`
     - **3 Clinical Business Outcomes**:
       - `Outcome 1: Reduced Clinical Risk from Fragmented Information`
       - `Outcome 2: Continuous & Resilient Regional Health Information Exchange`
       - `Outcome 3: Protected & Accountable Handling of Health Information`
     - **4 Foundational Requirements**: `REQ-FND-001` through `REQ-FND-004`.
     - **3 External Constraint Categories**: `CST-EXT-001` through `CST-EXT-003`.
     - **15 Active Architectural Axioms**: `AX-01` through `AX-11` and `AX-13` through `AX-16` (`AX-12` reclassified to Domain 05).

---

### Preliminary Classification Hypotheses

*Notice: No architectural decisions are authorized by this task. The following items are working classification hypotheses only, established to guide evidence discovery and reconciliation. Every hypothesis is provisional and subject to human architectural review.*

1. **Hypothesis: Decoupling Capabilities from Named Subsystems**:
   - *Hypothesis Statement*: To achieve ArchiMate 3.2 Strategy layer compliance, platform capabilities must represent *what Harmonia must be able to do to achieve value*, completely independent of the software components, modules, or middleware currently performing those tasks.
   - *Status*: Provisional analysis hypothesis. Requires human review to confirm whether candidate capabilities accurately reflect strategic scope without collapsing into component design.

2. **Hypothesis: Strategic Resources May Be Unnecessary or Lean**:
   - *Hypothesis Statement*: In ArchiMate, a Resource represents an asset owned or controlled by an organization. Many data stores, schemas, and logs in Harmonia are application or technology artifacts rather than enterprise strategic resources. An explicit Strategy Resource model may add unnecessary overhead and might not be required.
   - *Status*: Open investigation question. The analysis will evaluate whether explicit resource modeling adds value or whether the outcome is: *"Harmonia does not currently require an explicit Strategy Resource model."*

3. **Hypothesis: Principles vs Courses of Action Distinction**:
   - *Hypothesis Statement*: Architectural Axioms (Principles) constrain and guide architecture, whereas Courses of Action represent selected strategic approaches to achieve Goals and Outcomes. Candidate approaches must be treated as discovered possibilities, not pre-approved or designed strategies.
   - *Status*: Provisional analysis hypothesis. Each candidate approach must be tested against the 7 evaluation criteria before human review.

4. **Hypothesis: Downstream Handoffs Remain Conceptual**:
   - *Hypothesis Statement*: Strategy handoffs should identify downstream domain ownership and architectural concerns (e.g. protocol boundaries, data structures, execution lifecycles, security policies) rather than prescribing concrete software libraries, pipelines, or classes.
   - *Status*: Provisional analysis hypothesis. Concrete implementation mappings belong to downstream domain authoring.

---

### Preliminary Evidence & Candidate Hypotheses (Subject to Human Review)

#### 1. Candidate Strategic Capabilities (Analysis Hypotheses)

The following candidate capabilities represent working hypotheses extracted from repository evidence. They are **not** pre-frozen, canonical, or optimized for symmetry:

| Candidate Capability Hypothesis | Strategic Purpose | Motivation Alignment (Canonical) | Potential Downstream Realisation (Conceptual Concern) | Evidence Derivation Category |
| :--- | :--- | :--- | :--- | :--- |
| **Standards-Based Protocol Ingress & Egress** | Accept external health messaging protocols natively at the boundary and emit compliant representations without leaking internal operational semantics. | Driver: Clinical Interoperability Across Heterogeneous Standards<br>Goal 1: Durable Acceptance & Preservation of Clinical Events<br>AX-02, AX-03, AX-13 | Integration Architecture (protocol termination, boundary verification, non-destructive projection) | Derived from Component Responsibilities (`pylai`, Table 1.1 in `01-motivation-strategy.tex`) |
| **Enterprise Healthcare Directory Management** | Maintain an authoritative, locally governed directory of healthcare practitioners, roles, organizations, locations, and service endpoints aligned with national specifications. | Driver: National Healthcare Directory / Endpoint Governance<br>Goal 4: Reliable Subject Identity & Referential Integrity<br>AX-01, AX-06, AX-14 | Information Architecture (directory models) & Solution Pack: Provider Directory | Explicitly Defined in Source (`Harmonia_Strategy_Local_Directory...docx`, `01-foundations.tex`) |
| **Vendor-Neutral Longitudinal Health Record Preservation** | Maintain a durable, vendor-independent, longitudinal clinical record across the patient lifespan, preserving provenance and source authority. | Driver: Durable Clinical Information Independence<br>Goal 5: Vendor-Independent Longitudinal Clinical Information Coherence<br>Goal 1: Durable Acceptance & Preservation of Clinical Events<br>AX-01, AX-04, AX-06 | Information Architecture (longitudinal clinical schemas, non-destructive versioning) | Inferred from Multiple Sources (`01-motivation-strategy.tex`, `mnemosyne.md`, `AX-01`) |
| **Asynchronous Event Decoupling & Distribution** | Decouple incoming event ingestion from downstream processing, guaranteeing message transport, failure isolation, and load leveling across platform tiers. | Driver: Continuous Clinical Service Availability<br>Goal 2: Timely Clinical Availability & Non-Destructive Progression<br>AX-10, AX-15 | Technology Architecture (messaging broker topology) & Integration Architecture | Derived from Component Responsibilities (`petasos`, Table 1.1 `tab:strategic_capabilities`) |
| **Coordinated Clinical Activity Progression** | Coordinate multi-stage operational activities and workflows in observable lockstep with the lifecycle state of real-world clinical entities. | Driver: Coordinated Operational Activity and Entity State<br>Goal 6: Coordinated Progression of Operational Activities & Associated Entity State<br>AX-16, AX-15 | Application Architecture (activity unit execution models, state machine tracking) | Inferred from Multiple Sources (`energeia.md`, `ponos.md`, `pragma.md`, `AX-16`) |
| **Responsive Managed Information Access** | Provide responsive, low-latency concurrent read access to active in-flight operational state and cached clinical information without central persistence bottlenecks. | Assessment: Centralised Synchronous Persistence Can Constrain Concurrency<br>Goal 3: Responsive Access to Managed Information<br>AX-05, AX-11 | Technology Architecture (distributed caching) & Application Architecture | Derived from Component Responsibilities (`mneme`, `01-foundations.tex`, `AX-05`) |
| **Point-of-Care Clinical Presentation** | Deliver unified, read-oriented presentation views of longitudinal patient data, encounters, and diagnostics to care teams. | Outcome 1: Reduced Clinical Risk from Fragmented Information<br>Goal 5: Vendor-Independent Longitudinal Clinical Information Coherence<br>AX-01 | Application Architecture: Presentation Tier | Derived from Component Responsibilities (`iris-clinical`, `01-motivation-strategy.tex`) |
| **Intrinsic Access Governance & Policy Evaluation** | Enforce default-deny security context evaluation, multi-gate authorization, and credential validation across all governed operations. | Driver: Statutory Health Information Privacy & Protection<br>Assessment: Implicit Perimeter Trust and Diagnostic Data Leakage<br>AX-07, Outcome 3 | Security Architecture (access control engines, security context contracts) | Derived from Component Responsibilities (`themis.md`, `AX-07`, `REQ-FND-003`) |
| **Tamper-Evident Operational Audit & Provenance Evidence** | Capture chronological, tamper-evident operational and clinical evidence establishing source attribution and actor accountability without PHI leakage. | Driver: Statutory Health Information Privacy & Protection<br>Assessment: Implicit Perimeter Trust and Diagnostic Data Leakage<br>AX-08, AX-09, Outcome 3 | Security Architecture & Information Architecture (audit schemas, log append mechanisms) | Inferred from Multiple Sources (`kleio.md`, `themis-audit`, `AX-08`, `AX-09`) |
| **Care Team Communication & Collaboration Gateway** | Connect clinical operational triggers to structured communication channels and care team spaces. | Driver: Coordinated Operational Activity and Entity State<br>AX-01, AX-07 | Application Architecture (collaboration service adapters) | Weakly Supported / Implementation-Derived (`agora.md`, `02-conceptual-taxonomy.tex`) |
| **Synthetic Clinical Simulation & Environment Assurance** | Emulate external healthcare systems and execute deterministic failure scenarios to verify integration resilience in total isolation from production. | Driver: Continuous Clinical Service Availability<br>AX-15, Paradeigma Guardrails | Verification & Simulation Architecture (isolated test harness) | Derived from Component Responsibilities (`paradeigma.md`, Invariant 1 in `AGENTS.md`) |

#### 2. Potential Strategic Approaches / Courses of Action (Analysis Hypotheses)

The following potential strategic approaches will be evaluated during the analysis against the 7-question criteria (source evidence, strategic approach vs principle vs requirement vs downstream design vs solution-specific vs historical). Canonical `CoA-xx` IDs are intentionally **not** assigned:

- **Potential Approach: Boundary Membrane Sovereignty**
  - *Description*: Terminate external messaging protocols at strict boundary gateways, converting external formats into internal representations upon ingress and projecting internal representations into standards upon egress.
  - *Evaluation Questions*: Is this a Course of Action or a direct application of Axioms `AX-02` and `AX-13`? Does it belong to Integration Architecture?
- **Potential Approach: Canonical Domain Representation**
  - *Description*: Express all internal operational and clinical data through an immutable canonical domain model rather than executing point-to-point translations.
  - *Evaluation Questions*: Is this a strategic platform Course of Action or an Information Architecture standard?
- **Potential Approach: Two-Tier State Separation (Active Coordination vs Durable Truth)**
  - *Description*: Maintain distinct architectural tiers for distributed active coordination state (ephemeral, reconstructable) and durable authoritative persistence (committed, non-destructive).
  - *Evaluation Questions*: Supported by `AX-05` and Goal 3, but is it a Course of Action or a governing Principle?
- **Potential Approach: Intrinsic Default-Deny Security Fabric**
  - *Description*: Embed security context evaluation and non-PHI audit capture into all platform operations rather than relying on perimeter network defenses.
  - *Evaluation Questions*: Strongly supported by `AX-07` and `REQ-FND-003`; evaluate whether it functions as a strategic Course of Action or an architectural guardrail.
- **Potential Approach: Observable Entity-Activity Lifecycle Progression**
  - *Description*: Bind integration activities and message fan-outs to explicit state machines that progress synchronously with real-world clinical entity states.
  - *Evaluation Questions*: Directly derives from `AX-16` and Driver 7; assess whether it is an enterprise Course of Action or an Application Architecture execution pattern.
- **Potential Approach: Enterprise Directory Federation & Local Governance**
  - *Description*: Implement a locally governed healthcare directory that aggregates national references while maintaining local operational relationships.
  - *Evaluation Questions*: Strongly evidenced in `Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`; evaluate whether this is a platform-wide Course of Action or a Solution Pack strategy.

#### 3. Preliminary Capability Clustering Hypotheses

The source material suggests natural affinities that may form the basis of capability groupings, but these remain hypotheses subject to human architectural review:
- *Hypothesis Grouping A: Boundary Interoperability & Event Decoupling* (Ingress/egress gateway functions, asynchronous distribution).
- *Hypothesis Grouping B: Information Management & Longitudinal Preservation* (Enterprise directory, longitudinal record persistence, active-state access).
- *Hypothesis Grouping C: Operational Activity & Clinical Coordination* (Workflow progression, care-team collaboration).
- *Hypothesis Grouping D: Security, Audit & Assurance* (Access governance, audit/provenance capture, synthetic simulation).
- *Hypothesis Grouping E: Point-of-Care Presentation* (Clinical views, administrative dashboards).

#### 4. Motivation → Strategy Traceability Methodology

Rather than pre-freezing a fixed traceability graph or diagram, the analysis will trace and report evidence connecting canonical Domain 01 elements to candidate Strategy concepts:
- **Drivers & Assessments → Strategic Approaches**: Examining which repository-supported approaches respond to specific external pressures and identified operational hazards.
- **Goals & Axioms → Capabilities**: Mapping which capabilities are necessary to achieve specific canonical Strategic Goals under the constraints of governing Architectural Axioms.
- **Capabilities → Outcomes**: Demonstrating how candidate capabilities contribute to the three canonical Platform Outcomes.

---

### Strategy → Downstream Architecture Conceptual Handoff

The handoff between Strategy and downstream domains is defined at the **architectural-concern level**, identifying what downstream architectures must define without prescribing implementation details:

| Strategy Capability Hypothesis | Target Downstream Domain | Downstream Architectural Concerns to Be Defined |
| :--- | :--- | :--- |
| **Standards-Based Protocol Ingress & Egress** | Domain 06: Integration Architecture | Protocols supported, boundary validation behaviour, translation and non-destructive projection contracts, interaction patterns. |
| **Enterprise Healthcare Directory Management** | Domain 04: Information Architecture & Domain 11: Solution Packs | Practitioner/Organization/Location semantic models, external directory synchronization patterns, referential integrity rules. |
| **Vendor-Neutral Longitudinal Health Record Preservation** | Domain 04: Information Architecture | Canonical health record schemas, non-destructive progression rules, versioning semantics, source authority tracking. |
| **Asynchronous Event Decoupling & Distribution** | Domain 07: Technology Architecture & Domain 06: Integration | Physical message distribution topology, queuing and pub/sub interaction patterns, delivery guarantees. |
| **Coordinated Clinical Activity Progression** | Domain 05: Application Architecture | Activity unit definitions, workflow progression state machines, fan-out tracking mechanisms, error recovery lifecycles. |
| **Responsive Managed Information Access** | Domain 07: Technology Architecture & Domain 05: Application | Active-state caching architecture, cache coherence and invalidation patterns, application-facing query interfaces. |
| **Point-of-Care Clinical Presentation** | Domain 05: Application Architecture | Presentation service boundaries, user session management, clinical view composition, responsive interface patterns. |
| **Intrinsic Access Governance & Policy Evaluation** | Domain 08: Security Architecture | Policy evaluation architecture (ABAC/RBAC), security context contracts, authorization gate interception patterns. |
| **Tamper-Evident Operational Audit & Provenance Evidence** | Domain 08: Security Architecture & Domain 04: Information | Audit record structures, non-PHI sanitization policies, tamper-evident log append mechanisms. |
| **Care Team Communication & Collaboration Gateway** | Domain 05: Application Architecture | Collaboration channel mapping, room lifecycle contracts, notification dispatch models. |
| **Synthetic Clinical Simulation & Environment Assurance** | Domain 10: Verification & Simulation | Synthetic profile generation, actor emulation boundaries, failure injection frameworks, production isolation enforcement. |

---

### Reclassification and Historical Material Analysis

1. **Material for Reclassification to Downstream Domains**:
   - *Message broker configurations, clustering topologies, and protocol drivers* → Reclassify to Domain 07 (Technology Architecture).
   - *Cache engine client configurations, memory partition topologies, and store adapters* → Reclassify to Domain 07 (Technology Architecture).
   - *TCP socket framing delimiters, MLLP framing details, and network port bindings* → Reclassify to Domain 06 (Integration Architecture).
   - *Relational database DDL, table structures, and foreign key definitions* → Reclassify to Domain 04 (Information Architecture).
   - *Application server packaging, runtime descriptors, and servlet definitions* → Reclassify to Domain 05 (Application Architecture).
   - *Frontend SPA component hierarchies, state stores, and CSS themes* → Reclassify to Domain 05 (Application Architecture: Presentation).

2. **Historical Material and Supersession Analysis**:
   - *Component Names Used as Capabilities*: Historical LaTeX tables (e.g. Table 1.1 in `01-motivation-strategy.tex`) and diagrams equate component names (`Pylai`, `Petasos`, `Mneme`, `Energeia`) with capabilities. These will be classified as historical and decoupled in Domain 02.
   - *Write-Behind Caching as Sole Persistence Mechanism*: Historical drafts emphasize write-behind caching (`01-foundations.tex`, `01-motivation-strategy.tex`). Current architecture emphasizes state separation (`AX-05`) and durable truth (`Mnemosyne`). Rather than asserting an unapproved replacement model, this item is classified as: `Potentially Superseded — Requires Human Review`.
   - *Local Directory Scope vs Platform Scope*: Early documentation blurred the boundary between Harmonia as a multi-purpose HIE platform and Harmonia as an LDS implementation. The analysis reconciles this using the 24 September 2026 position brief, recommending LDS be classified as a solution pack (Domain 11) running on core platform capabilities.

---

### Analysis Report & Completion Report Structure

The primary deliverable will be compiled into the analysis report containing:
- **Section A: Executive Summary**
- **Section B: Source Material Reviewed**
- **Section C: Candidate Capabilities (Detailed Profiles)**
- **Section D: Candidate Resources (Critical Assessment)**
- **Section E: Candidate Courses of Action (Strategic Approaches)**
- **Section F: Candidate Capability Map (Natural Groupings)**
- **Section G: Motivation → Strategy Relationships (Traceability)**
- **Section H: Strategy → Architecture Handoff (Downstream Boundaries)**
- **Section I: Reclassification Candidates (Routing Table)**
- **Section J: Historical / Superseded Material (Disposition Table)**
- **Section K: Contradictions and Open Questions (For Architectural Review)**
- **Section L: Recommended Domain 02 Documentation Set (File-by-File Markdown Plan)**

The final execution completion report will be saved to:
`.junie/reports/2026-10-04-domain-02-strategy-analysis.md` conforming strictly to the required heading structure.

# Testing

### Validation Approach

Because this task is strictly an architectural discovery, reconciliation, and classification analysis (with no production code or canonical Domain 02 authoring permitted), validation focuses on:
1. **Evidentiary Completeness**: Confirming that all relevant repository material across LaTeX, Markdown, and DOCX sources was inspected, cross-checked, and accounted for.
2. **Strict Demarcation of Concept Origins**: Verifying that candidate capabilities and strategic approaches are explicitly categorized by derivation type (`Explicitly Defined in Source`, `Inferred from Multiple Sources`, `Derived from Component Responsibilities`, `Historical`, `Conflicting`, `Weakly Supported`) rather than asserted as canonical architecture.
3. **Canonical Motivation Alignment**: Validating that all referenced Motivation elements use the exact approved names and semantics from canonical Domain 01 (`docs/markdown/01-motivation/`), with zero invented shorthand or unapproved concepts.
4. **Open Question Evaluation**: Ensuring that Strategic Resources are investigated critically as an open architectural question (including the valid outcome of not requiring an explicit resource model), and Courses of Action are evaluated against the 7-question criteria rather than designed.
5. **Conceptual Downstream Handoffs**: Confirming that Strategy handoffs remain at the architectural-concern level without prematurely prescribing implementation details, classes, or frameworks.
6. **Supersession Verification**: Ensuring that historical items (such as write-behind caching) are not marked superseded without canonical proof, using `Potentially Superseded — Requires Human Review` where unconfirmed.
7. **Strict Repository Invariance**: Ensuring that no canonical documentation files under `docs/markdown/01-motivation/` or new `docs/markdown/02-strategy/` were modified or prematurely authored.

---

### Key Validation Scenarios

1. **Evidentiary Corpus Coverage Verification**:
   - Verify that `docs/latex/chapters/01-motivation-strategy.tex`, `docs/latex/chapters/01-foundations.tex`, `docs/latex/chapters/02-conceptual-taxonomy.tex`, `docs/latex/chapters/02-business-layer.tex`, `docs/latex/styles/harmonia-archimate.sty`, `docs/latex/diagrams/fig-motivation-map.tex`, `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`, and all 16 `docs/concepts/` documents are represented in the analysis.
   - Confirm that the Local Directory position brief requirements (`HARM-STR-001`, `HARM-LDS-001..012`) are reconciled against the platform's multi-purpose vision.

2. **ArchiMate 3.2 Strategy Layer Conformity & Concept Origin**:
   - Check that candidate Capabilities represent *what* Harmonia must do rather than *which component* executes it.
   - Check that every candidate capability profile records its derivation category, classification confidence, and open conflicts/questions.
   - Verify that candidate Courses of Action are tested against the 7 evaluation questions rather than assigned canonical `CoA-xx` identifiers.
   - Verify that candidate capability groupings are presented as observational clustering hypotheses rather than a finished capability map.

3. **Motivation Alignment Audit**:
   - Check that all referenced Drivers, Assessments, Goals, Outcomes, Axioms, and Requirements match canonical Domain 01 verbatim.
   - Confirm zero occurrences of invented shorthand (such as "Goal 2: Zero In-Flight Loss", "Driver: Care Team Coordination", or "Driver: Operational Reliability").

4. **Reclassification & Routing Precision**:
   - Verify that identified implementation details (e.g. queue configurations, cache topologies, TCP framing delimiters, DDL scripts, Vue components) are mapped to target downstream domains at the architectural-concern level.

5. **Report Schema Completeness**:
   - Confirm that the analysis report satisfies all 12 requested sections (A through L).
   - Confirm that the completion report at `.junie/reports/2026-10-04-domain-02-strategy-analysis.md` conforms strictly to the required handover headings.

---

### Non-Functional & Boundary Checks

- Execute `git status` to ensure zero unstaged or accidental changes to existing canonical files under `docs/markdown/01-motivation/` or repository source code.
- Verify that no directory or file was created under `docs/markdown/02-strategy/`.

# Delivery Steps

### ✓ Step 1: Multi-Source Repository Discovery and Corpus Inventory
A comprehensive inventory of all Strategy-relevant text, models, diagrams, and historical specifications across the repository is established.

- Inspect LaTeX sources: analyze `docs/latex/chapters/01-motivation-strategy.tex`, `01-motivation-strategy-old.tex`, `01-foundations.tex`, `02-conceptual-taxonomy.tex`, `02-business-layer.tex`, `styles/harmonia-archimate.sty`, and `diagrams/fig-motivation-map.tex`.
- Inspect Markdown sources: analyze `docs/concepts/` (all 16 subsystem concept documents), `docs/architecture/` (overview, runtime-architecture, execution-model), and `docs/markdown/01-motivation/` (authoritative baseline).
- Inspect Strategic Brief: analyze `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx` for enterprise local directory position, broader multi-purpose vision, strategic requirements (`HARM-STR-001`, `HARM-LDS-001..012`), and guardrails.
- Extract all statements, definitions, and model elements describing capabilities, resources, courses of action, value streams, and component operational roles into an organized evidentiary corpus.

### ✓ Step 2: Capability & Resource Evidence Analysis and Classification
Candidate strategic capabilities and strategic resources are extracted, decoupled from named components, and classified with evidence derivation categories without pre-freezing a canonical model.

- Decouple capabilities from named Harmonia components and middleware (e.g. translate Pylai, Mneme, Mnemosyne, Ponos, Calliope, Iris, Artemis, Infinispan, HAPI FHIR into pure functional capability hypotheses).
- Author detailed candidate capability profiles for each identified candidate, capturing: Candidate Capability Hypothesis, Purpose, Motivation Elements Served (using exact canonical Domain 01 names), Supporting Source Material, Conceptual Downstream Realisation Concerns, Evidence Derivation Category (`Explicitly Defined in Source`, `Inferred from Multiple Sources`, `Derived from Component Responsibilities`, `Historical`, `Conflicting`, `Weakly Supported`), Classification Confidence, and Conflicts / Questions.
- Assess candidate strategic Resources as an open architectural question: evaluate whether explicit ArchiMate 3.2 Resource modeling adds meaningful architectural value or whether Harmonia's strategy layer is best served without an explicit resource model.
- Document the strategic intent of named subsystems while strictly deferring their detailed component boundaries and class designs to downstream Application Architecture.

### ✓ Step 3: Strategic Approaches Analysis, Natural Clustering, and Motivation Traceability
Candidate courses of action, natural capability groupings, and Motivation-to-Strategy traceability threads are analyzed from evidence without premature design or canonical assignment.

- Analyze potential strategic approaches (Courses of Action) using the 7-question evaluation test (source evidence, strategic approach vs principle vs requirement vs downstream design vs solution-specific vs historical), without assigning canonical `CoA-xx` numbers.
- Identify natural capability groupings supported by repository evidence, presenting them as observational clustering hypotheses rather than a finished capability map.
- Establish meaningful Motivation → Strategy traceability threads connecting canonical Domain 01 Drivers, Assessments, Goals, Outcomes, and governing Architectural Axioms (`AX-01`..`AX-16`) to candidate approaches and capabilities, identifying supporting evidence without pre-freezing a canonical model.
- Map the observed clinical value stream flow from ingestion trigger through transport, orchestration, state update, and publication as documented in existing LaTeX and Markdown sources.

### ✓ Step 4: Downstream Handoff Mapping, Reclassification & Supersession Analysis, and Report Delivery
Conceptual downstream handoffs, reclassification candidates, historical/superseded elements, and open architectural questions are compiled into the final analysis report.

- Map candidate capabilities and strategic approaches to target downstream domains at the architectural-concern level (defining what downstream domains must decide without prescribing implementation classes or libraries).
- Identify material currently presented as Strategy that properly belongs in downstream domains (Domain 03 Business, Domain 04 Information, Domain 05 Application, Domain 06 Integration, Domain 07 Technology, Domain 08 Security, Domain 09 Resilience & Operability, Domain 10 Simulation).
- Analyze historical, superseded, or contradictory material (such as component-named capabilities in LaTeX tables and premature LDS scope definitions), flagging unverified supersessions (such as write-behind caching) as `Potentially Superseded — Requires Human Review`.
- Document unresolved contradictions and open questions requiring human architectural review before Domain 02 can be frozen.
- Recommend the target Markdown document set under `docs/markdown/02-strategy/` (`README.md`, `capabilities/`, `resources/`, `courses-of-action/`, `capability-maps/`, `strategic-views/`).
- Compile and deliver the complete Analysis Report conforming to sections A through L, and author the formal execution completion report at `.junie/reports/2026-10-04-domain-02-strategy-analysis.md`.