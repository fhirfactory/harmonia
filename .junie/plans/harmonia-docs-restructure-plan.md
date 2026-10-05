---
sessionId: session-261004-084708-1skt
---

# Requirements & Authority Model

### 1. Executive Summary

This revised documentation restructuring plan supersedes the preliminary proposal by formalizing an enterprise-grade documentation architecture for the Harmonia Health Integration Environment (HIE). Harmonia operates within mission-critical regional healthcare ecosystems where architectural ambiguity, obsolete schema definitions, or fragmented operational runbooks introduce clinical and regulatory risk. The overarching utilitarian objective of this plan is to **maximize system comprehensibility, operational reliability, and engineering velocity while minimizing cognitive overhead, architectural divergence, and error rates**.

Significant revisions and enhancements incorporated into this model include:
1. **Separation of Motivation and Strategy**: Replaces the previous combined `01-motivation-strategy` with two distinct architectural domains: `01-motivation` (capturing stakeholders, drivers, goals, and architectural principles) and `02-strategy` (capturing strategic capabilities, resources, and value stream courses of action).
2. **Primacy of Information Architecture**: In accordance with Harmonia's core mission as an information exchange platform, Information Architecture (`04-information-architecture`) deliberately precedes Application Architecture (`05-application-architecture`), ensuring canonical schemas, information models, and persistence contracts are understood prior to component composition.
3. **Decoupling Application Architecture from Code Subsystems**: Eliminates arbitrary Maven module mirrors (`subsystems/` vs `concepts/`). Components, services, interfaces, middleware enablers, and execution models are classified purely by architectural role.
4. **Dedicated Middleware & Enabler Architecture**: Establishes `05-application-architecture/middleware/` to document the architectural capabilities, constraints, and integration contracts of reusable platform technologies (Artemis, Infinispan, PostgreSQL, WildFly, HAPI FHIR, Matrix) independently from their infrastructure deployment.
5. **Explicit Execution Models Domain**: Formulates `05-application-architecture/execution-models/` to house task processing semantics, Digital Twin execution, and Ergon/Praxis/Pragma workflow models that represent execution dynamics rather than deployable components.
6. **Isolation of Solution Architectures**: Establishes `11-solutions/provider-directory/` to cleanly separate worked clinical solutions from core platform infrastructure.
7. **Comprehensive Verification & Simulation Scope**: Broadens the simulation domain to `10-verification-simulation/`, treating Paradeigma as an exemplar mechanism within a broader architecture assurance framework.
8. **Formalized Documentation Authority & Reconciliation Model**: Explicitly establishes Markdown as the single canonical source of truth, with LaTeX and ODT serving as published representations. Critically, accounts for the historical reality that existing LaTeX sources contain substantial authoritative material missing from Markdown, mandating a rigorous bidirectional reconciliation phase prior to final restructuring.
9. **Preservation of the Strategic DOCX Brief**: Halts premature conversion of `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`, retaining it for formal architectural review.

---

### 2. Documentation Authority & Publication Model

Harmonia enforces a strict documentation authority model: **one authoritative documentation set with multiple synchronized publication representations**.

```text
                            CANONICAL SOURCE OF TRUTH
                                       |
                                       v
                                docs/markdown/
                                       |
                     +-----------------+-----------------+
                     |                                   |
                     v                                   v
                docs/latex/                          docs/odt/
                     |                                   |
                     v                                   v
             Single-Volume PDF                   Compiled ODT Book
          (Harmonia Specification)            (Harmonia Specification)
```

#### 2.1 Canonical Markdown Source (`docs/markdown/`)
`docs/markdown/` contains the **canonical and authoritative architectural and reference content** of the Harmonia repository. All architectural axioms, requirements, domain models, decisions, contracts, and deployment runbooks must ultimately reside in Markdown. It serves as the primary authoring format for developers, architects, and autonomous agents.

#### 2.2 LaTeX Publication Pipeline (`docs/latex/`)
`docs/latex/` is a formal publication representation of the canonical Markdown content. Its singular purpose is to compile a publication-grade, navigable, single-volume reference book (PDF) utilizing custom styling packages (`harmonia-doc.sty`, `harmonia-archimate.sty`), embedded vector diagrams (TikZ), and formal cross-referencing. LaTeX contains document composition, typographic layout, and indexing machinery, but must never become an uncoordinated, independent source of architectural truth. There must ultimately exist a strict 1:1 content relationship between Markdown chapters and LaTeX modules.

#### 2.3 OpenDocument Text Pipeline (`docs/odt/`)
`docs/odt/` produces a single coherent, navigable OpenDocument Text (`.odt`) representation of the same canonical Markdown content, generated via automated scripts (`docs/odt/scripts/generate_odt.py`). ODT contains formatting templates and OASIS OpenDocument XML serializers, maintaining identical 1:1 content fidelity with Markdown.

#### 2.4 The Migration Reconciliation Exception
Historically, the repository's documentation evolved organically, resulting in significant architectural elaboration occurring directly within the LaTeX suite. As established during comprehensive repository inspection, LaTeX chapters (e.g. `appendix-requirements.tex`, `appendix-findings.tex`, `appendix-backlog.tex`, `appendix-mllp-services.tex`, `appendix-provider-registry.tex`, `01-motivation-strategy.tex`) contain extensive specifications, requirements catalogues, and engineering blueprints that are absent from or vastly more complete than existing Markdown documents.

Therefore, the restructuring workflow enforces a strict **Reconciliation Phase**:
```text
Existing Markdown (150 files) ---+
                                 |
                                 +--> Architectural Reconciliation --> New Canonical Markdown
                                 |                                     (docs/markdown/)
Existing LaTeX (58 chapters) ----+                                             |
                                                                               +--> LaTeX PDF
                                                                               |
                                                                               +--> ODT Book
```
Restructuring must not simply move existing Markdown stubs into new directories; it must synthesize and reconcile content from LaTeX to produce complete, canonical Markdown specifications.

#### 2.5 Repository Authority Hierarchy
All documentation and implementation within Harmonia is interpreted in accordance with the strict authority hierarchy defined in `AGENTS.md`:
1. `docs/markdown/01-motivation/principles/architectural-axioms.md` (AX-01 to AX-15)
2. `docs/markdown/13-governance-decisions/guardrails/agent-guardrails.md` (`AGENTS.md`)
3. Applicable accepted Architecture Decision Records (`docs/markdown/13-governance-decisions/architecture-decisions/`)
4. Applicable requirements catalogues and formal design contracts
5. `docs/markdown/12-implementation-migration/roadmaps/harmonia-convergence-runtime-integration-plan.md` (authoritative implementation sequence)
6. Task-specific Junie plans and execution reports

---

### 3. Learning-Tool Requirement & Pedagogical Progression

Harmonia documentation is intentionally engineered as an architectural **learning tool**. An engineer, clinical informatician, or autonomous agent unfamiliar with Harmonia must be able to understand the reasoning and structure of the platform through a progressive conceptual journey:

```text
  [WHY?]                         01-MOTIVATION
                                 Stakeholders, drivers, goals, axioms, requirements
                                       |
                                       v
  [WHAT CAPABILITIES?]           02-STRATEGY
                                 Resources, courses of action, capability maps
                                       |
                                       v
  [WHAT ARCHITECTURE?]           CORE ARCHITECTURAL TIERS
                                 03-Business -> 04-Information -> 05-Application ->
                                 06-Integration -> 07-Technology -> 08-Security ->
                                 09-Resilience & Operability -> 10-Verification
                                       |
                                       v
  [HOW ARE THEY COMPOSED?]       11-SOLUTIONS
                                 Worked implementations (Provider Directory, Longitudinal Record)
                                       |
                                       v
  [HOW DO WE BUILD/TRANSITION?]  12-IMPLEMENTATION & MIGRATION
                                 Master convergence plan, backlogs, deployment guides
                                       |
                                       v
  [HOW DO WE GOVERN/PROTECT?]    13-GOVERNANCE & DECISIONS
                                 ADRs, agent guardrails, conformance findings, gap audits
```

Each major numbered domain in `docs/markdown/` will feature an authoritative `README.md` structured around six standard pedagogical questions:
1. *What is this architectural domain?*
2. *Why does Harmonia need it?*
3. *Which TOGAF / ArchiMate concepts are relevant?*
4. *How does this domain relate to preceding and following domains?*
5. *What Harmonia documentation belongs here?*
6. *What specifically does NOT belong here?*

---

### 4. Scope Boundaries

#### In Scope (Analysis & Restructuring Plan)
- Comprehensive inventory and cross-format reconciliation assessment of all 150 Markdown files, 58 LaTeX chapters, 25 TikZ diagrams, ODT scripts, and DOCX briefs.
- Design of the 14-branch TOGAF/ArchiMate canonical Markdown taxonomy under `docs/markdown/`.
- Detailed 1-to-1 migration matrix mapping every Markdown file to its target path with primary classification, secondary concerns, relevant LaTeX sources, reconciliation status, recommended actions, and rationale.
- Precise architectural classification of all named Harmonia platform elements (Calliope, Mneme, Mnemosyne, Ponos, Pylai, Themis, Petasos, Iris, Agora, Paradeigma, Ergon, Erga, Praxis, Pragma, Hestia, Energeia, Kleio).
- Definition of generic middleware architecture vs deployment topology.
- Definition of solution documentation boundaries vs platform architecture.
- Dependency tracing and remediation planning for repository references (`AGENTS.md`, `README.md`, `generate_odt.py`, internal links).
- Formulation of an incremental, risk-minimized migration and reconciliation sequence.

#### Out of Scope (Strict Planning Invariants)
- **Zero Repository File Mutations**: No moving, renaming, deleting, creating, or editing of documentation files during this planning session.
- **No Premature Document Merging**: Content consolidation, splitting, and LaTeX text extraction are reserved for subsequent execution phases.
- **No DOCX Conversion**: The DOCX strategy document is flagged for human review and will not be converted or relocated during planning.
- **No Pipeline Changes**: Zero alterations to Maven POMs, Makefiles, or Python scripts.

---

### 5. Stakeholder Perspectives & User Stories

- **As an Enterprise & Solutions Architect**, I want documentation structured strictly according to TOGAF and ArchiMate domains so that I can evaluate capability alignment, verify boundary invariants, and conduct governance audits without parsing implementation-specific directory layouts.
- **As a Platform & Integration Developer**, I want canonical information models, persistence contracts, and execution models documented independently of infrastructure middleware, so that I can implement components and Camel routes with unambiguous design contracts.
- **As an Autonomous Agent (Junie / Co-executor)**, I want deterministic, stable paths for architectural axioms, ADRs, guardrails, and master convergence roadmaps, ensuring fail-closed operation and zero hallucinatory path traversal.
- **As an Operations & Infrastructure Engineer**, I want deployment topologies, MicroK8s manifests, and network registers centralized under Technology Architecture, eliminating conflicting listener tables and cluster runbooks.
- **As a Clinical Security & Compliance Officer**, I want all Themis default-deny policies, RBAC/ABAC models, non-PHI audit boundaries, and PHI masking specifications consolidated under Security Architecture to streamline certification reviews.

---

### 6. Functional & Non-Functional Requirements

#### Functional Requirements
1. **Tri-Format Segregation**: Root `./docs` must cleanly separate into `docs/markdown/`, `docs/latex/`, and `docs/odt/`.
2. **Canonical Taxonomy Conformance**: Markdown documentation must adhere to the 14-tier architectural progression (01-motivation through 13-governance-decisions plus 99-reference).
3. **Exhaustive Mapping**: All 150 identified Markdown documents must have a deterministic mapping in the migration matrix.
4. **Bidirectional Reconciliation**: The migration process must cross-reference and enrich Markdown documents with authoritative content from corresponding LaTeX chapters.
5. **Tooling & Reference Alignment**: Explicit remediation plans must be established for `AGENTS.md`, root `README.md`, and `generate_odt.py`.

#### Non-Functional Requirements & Utility Principles
- **Cognitive Optimization**: Maximum directory depth beneath `docs/markdown/` shall not exceed 3 levels. All directory and file names must use lowercase kebab-case.
- **Zero Information Loss**: No document shall be silently omitted or discarded. Incomplete files or obsolete snippets must be flagged and preserved through structured consolidation.
- **Fail-Closed Integrity**: Normative governance documents must maintain uninterrupted authority across all transitional states.
- **Link Stability**: Target relative link structures must ensure robust cross-document navigation across the canonical Markdown tree.

# Current Documentation Inventory

### Executive Summary of Existing `./docs`

The `./docs` directory currently contains:
- **150 Markdown (`.md`) documents** across 17 subdirectories and the root folder.
- **1 Formal LaTeX suite** in `docs/latex/` containing 58 chapter files (`.tex`), 25 TikZ diagram sources (`.tex`), 4 PNG images, 2 style packages (`.sty`), a `Makefile`, and `main.tex`.
- **1 OpenDocument Text archive** in `docs/libreoffice/` (`harmonia-architecture-specification.odt`) supported by an automated generation script (`scripts/generate_odt.py`), templates, and diagram assets.
- **1 Microsoft Word Strategy Document** (`docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`) capturing the strategic vision and LDS alignment.

---

### Current Subdirectories and Apparent Purpose

| Subdirectory | File Count | Apparent Purpose & Content | Observed Structural Problems |
| :--- | :---: | :--- | :--- |
| `docs/` (Root) | 16 | Top-level entry points, core axioms, ADRs, database schema, event catalogues, and resilience specs. | Mixes normative governance (`architectural-axioms.md`) with technical guides and specific event catalogues. Contains duplicate `AGENTS.md`. |
| `docs/architecture/` | 10 | General architecture overviews, runtime topologies, execution models, and consoles. | Severe overlap with root docs (`failure-recovery.md`, `persistence-lifecycle.md`, `port-protocol-register.md`). |
| `docs/backlog/` | 1 | Backlog items (`Harmonia - Task 9 - Backlog.md`). | Spaces in filename; isolated single-file folder. |
| `docs/concepts/` | 22 | Conceptual explanations of platform components and Greek naming metaphors. | Duplicates almost 1-to-1 the components in `docs/modules/`. Has duplicate pairs (e.g., `calliope.md` & `calliope-canonical.md`). |
| `docs/configuration/`| 4 | Network ports, environment matrices, and configuration properties. | Overlaps with `docs/reference/` and `docs/architecture/port-protocol-register.md`. |
| `docs/deployment/` | 13 | MicroK8s, Ansible, container workloads, startup/shutdown runbooks. | Overlaps with root `docs/deployment-microk8s.md`. |
| `docs/design/` | 1 | Detailed technical concurrency contract for governed writes. | Standalone design document, conceptually belongs to Information Architecture. |
| `docs/getting-started/`| 5 | Introductory guides, build instructions, terminology, and quickstarts. | Pragmatic developer onboarding, currently disconnected from TOGAF categorization. |
| `docs/implementation/`| 1 | Master convergence and runtime integration plan. | Critical authority document referenced by `AGENTS.md`. |
| `docs/integration/` | 8 | Protocol adapters (HL7, FHIR, MLLP), correlation, and error handling. | Well-scoped integration domain, but mixes protocol details with generic overviews. |
| `docs/latex/` | 1 (.md) | Authoritative publication-quality book source in LaTeX/TikZ. | Format-specific container; contains supporting `README.md`. |
| `docs/libreoffice/` | 1 (.md) | OpenDocument (.odt) specification generator and templates. | Misnamed folder (tool name rather than standard format `odt/`). |
| `docs/middleware/` | 8 | Specifications of underlying infrastructure (Artemis, Infinispan, PostgreSQL, WildFly, Matrix). | Technology Architecture tier; solid content, minor casing inconsistency (`activeMQ-artemis.md`). |
| `docs/modules/` | 9 | Subsystem specifications for each of the 9 core Maven modules. | Parallel to `docs/concepts/`; creates confusion over where module details live. |
| `docs/operations/` | 3 | Health probes, verification runbooks, and PHI-sanitized logging. | Operational runbooks; overlaps with `docs/security/logging.md`. |
| `docs/paradeigma/` | 16 | Exemplar clinical simulation engine, isolation invariants, and scenarios. | Complete, highly cohesive subsystem documentation, but lives at top-level instead of testing/simulation. |
| `docs/provider-registry/`| 11 | Domain-specific deep dive into Provider Registry service architecture and validation. | Specific business solution documentation placed at top-level. |
| `docs/reference/` | 2 | Registers for network ports and configuration properties. | Triple-duplication of port registers and configuration tables. |
| `docs/security/` | 18 | Themis authorization engine, security policies, identities, and threat models. | Comprehensive security domain documentation; high quality but needs logical grouping. |

---

### Non-Markdown Documentation Assets

1. **LaTeX Documentation Suite (`docs/latex/`)**:
   - `docs/latex/main.tex`: Master LaTeX document orchestrating 7 Parts and 67 Chapters.
   - `docs/latex/chapters/*.tex` (58 files): Detailed technical chapters spanning conceptual taxonomy, application layers, infrastructure, and appendices.
   - `docs/latex/diagrams/*.tex` (25 files): Native TikZ vector diagrams utilizing ArchiMate 3.2 color palettes.
   - `docs/latex/images/*.png` (4 images): High-resolution architectural and entity relationship diagrams.
   - `docs/latex/styles/*.sty` (`harmonia-archimate.sty`, `harmonia-doc.sty`): Custom formatting macros and ArchiMate color definitions.
   - `docs/latex/Makefile`: Automated PDF build harness (`pdflatex` / `latexmk`).
2. **OpenDocument Text Suite (`docs/libreoffice/`)**:
   - `docs/libreoffice/harmonia-architecture-specification.odt`: Complete 160+ page compiled ODT architecture specification.
   - `docs/libreoffice/scripts/generate_odt.py`: 879-line Python compiler that parses LaTeX sources into canonical OASIS OpenDocument Format 1.3 XML.
   - `docs/libreoffice/templates/` (`manifest.xml`, `styles.xml`): ODF styles and layout definitions.
   - `docs/libreoffice/assets/diagrams/*.png` (15 PNGs): Rendered diagram assets embedded into the ODT document.
   - `docs/libreoffice/Makefile`: Build harness for invoking `generate_odt.py`.
3. **Microsoft Word Strategy Brief (`docs/`)**:
   - `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx`: Formative strategic positioning document defining the Local Directory Solution (LDS) rationale, national directory interfaces, and foundational platform scope.

---

### External References and Dependencies

- **`AGENTS.md` (Root)**:
  - Line 34: References `docs/architectural-axioms.md`.
  - Line 418: References `docs/architectural-axioms.md`.
  - Line 422: References `docs/implementation/harmonia-convergence-runtime-integration-plan.md`.
  - Line 447: References `docs/implementation/harmonia-convergence-runtime-integration-plan.md`.
- **`README.md` (Root)**:
  - Lines 427–440: Explicit markdown links to:
    - `docs/architecture/overview.md`
    - `docs/architecture/runtime-architecture.md`
    - `docs/architecture/execution-model.md`
    - `docs/security/architecture.md`
    - `docs/security/logging.md`
    - `docs/provider-registry/security.md`
    - `docs/architecture.md`
    - `docs/persistence-architecture.md`
    - `docs/message-lifecycle.md`
    - `docs/failure-recovery.md`
    - `docs/deployment-microk8s.md`
- **`docs/libreoffice/scripts/generate_odt.py`**:
  - Line 21: Hardcoded path `os.path.join(REPO_ROOT, "docs", "latex")`.
  - Line 25: Output destination `os.path.join(LIBREOFFICE_DIR, "harmonia-architecture-specification.odt")`.
- **Internal Cross-Document Links**:
  - Found across `docs/architecture.md`, `docs/design/governed-write-concurrency-contract.md`, `docs/getting-started/introduction.md`, `docs/integration/overview.md`, `docs/middleware/overview.md`, `docs/paradeigma/overview.md`, and `docs/provider-registry/security.md`.

---

### Complete Inventory of All 150 Markdown Files

| # | Current Path | Document Name | Apparent Purpose | Primary Architectural Concern | Notes & Observed References |
| :-: | :--- | :--- | :--- | :--- | :--- |
| 1 | `docs/` | `AGENTS.md` | Agent rules and invariants | Principles / Guardrails | Duplicate of root `/AGENTS.md`. |
| 2 | `docs/` | `README.md` | Documentation index | Reference / Supporting | Outdated index pointing to flat paths. |
| 3 | `docs/` | `architectural-axioms.md` | Supreme architectural axioms (AX-01 to AX-15) | Principles / Guardrails | Authoritative source of design truth. |
| 4 | `docs/` | `architecture-decisions.md` | Architecture Decision Records (ADR-001 to ADR-026) | Architecture Decisions | Authoritative ADR register. |
| 5 | `docs/` | `architecture.md` | High-level platform architecture summary | Application Architecture | Contains links to persistence, security, recovery. |
| 6 | `docs/` | `database-schema.md` | Relational table catalog and DDL definitions | Information / Data | Authoritative DDL for `hie_fhir_resources`. |
| 7 | `docs/` | `deployment-microk8s.md` | MicroK8s deployment overview | Technology Architecture | Overlaps with `docs/deployment/microk8s.md`. |
| 8 | `docs/` | `failure-recovery.md` | Failure matrix, restart recovery, idempotency | Resilience / Operations | Overlaps with `docs/architecture/failure-recovery.md`. |
| 9 | `docs/` | `failure-scenarios.md` | Concrete failure scenario walkthroughs | Resilience / Operations | Scenario-based operational guide. |
| 10 | `docs/` | `high-availability.md` | Multi-node clustering and broker HA topology | Resilience / Operations | HA design across Artemis and Infinispan. |
| 11 | `docs/` | `message-lifecycle.md` | End-to-end clinical message processing flows | Integration Architecture | Detailed Mermaid flowcharts for ADT/ORM/ORU. |
| 12 | `docs/` | `patient-identity-events.md` | Patient identity business event catalog | Business Architecture | Business events, Agora policies, candidate Praxis. |
| 13 | `docs/` | `persistence-architecture.md`| 4-tier storage hierarchy and entity catalog | Information / Data | Overlaps with `docs/architecture/persistence-lifecycle.md`. |
| 14 | `docs/` | `persistence-recovery-gaps.md`| Persistence & recovery gap analysis (REC-001..004)| Architecture Governance | Gap audit and risk assessments. |
| 15 | `docs/` | `provider-management-events.md`| Provider management business event catalog | Business Architecture | Business events driving Praxis and Agora flows. |
| 16 | `docs/` | `recovery-guarantees.md` | RPO/RTO and formal delivery guarantees | Resilience / Operations | SLA and recovery objectives. |
| 17 | `docs/architecture/` | `convergence-report.md` | Historical convergence assessment | Architecture Governance | Baseline audit findings. |
| 18 | `docs/architecture/` | `execution-model.md` | Ponos, Ergon, Praxis task processing model | Application Architecture | Core workflow engine execution dynamics. |
| 19 | `docs/architecture/` | `failure-recovery.md` | Failure recovery and resiliency model | Resilience / Operations | Duplicate/variant of `docs/failure-recovery.md`. |
| 20 | `docs/architecture/` | `iris-design-system.md` | UI design system, typography, components | Application (Presentation) | Frontend design tokens and Vue standards. |
| 21 | `docs/architecture/` | `iris-operations-console.md`| Operations console architecture and telemetry | Application (Presentation) | Operations UI architecture. |
| 22 | `docs/architecture/` | `overview.md` | 5-tier architecture overview | Application Architecture | System-level architectural blueprint. |
| 23 | `docs/architecture/` | `persistence-lifecycle.md`| Persistence architecture and storage lifecycle | Information / Data | Contains outdated DDL schema snippet. |
| 24 | `docs/architecture/` | `port-protocol-register.md`| Network port and protocol register | Technology Architecture | Duplicate of configuration and reference tables. |
| 25 | `docs/architecture/` | `runtime-architecture.md` | Physical process topology and container layout | Technology Architecture | Concrete runtime and network flows. |
| 26 | `docs/architecture/` | `system-inventory.md` | Subsystem inventory and technology stack | Technology Architecture | Software component breakdown. |
| 27 | `docs/backlog/` | `Harmonia - Task 9 - Backlog.md` | Task 9 backlog items | Implementation / Migration | Spaces in filename; single-item backlog. |
| 28 | `docs/concepts/` | `agora.md` | Agora collaboration concept | Application Architecture | Greek concept overview for Agora. |
| 29 | `docs/concepts/` | `calliope-canonical.md` | Calliope canonical schema concept | Information / Data | Duplicate concept of `calliope.md`. |
| 30 | `docs/concepts/` | `calliope.md` | Calliope subsystem concept | Application Architecture | Canonical modeling concept. |
| 31 | `docs/concepts/` | `energeia-workflow.md` | Energeia workflow concept | Application Architecture | Duplicate concept of `energeia.md`. |
| 32 | `docs/concepts/` | `energeia.md` | Energeia concept | Application Architecture | Core workflow concept. |
| 33 | `docs/concepts/` | `ergon.md` | Ergon discrete work activity concept | Application Architecture | Activity unit concept. |
| 34 | `docs/concepts/` | `harmonia.md` | Root Harmonia platform concept | Motivation & Strategy | Overarching platform vision. |
| 35 | `docs/concepts/` | `hestia-persistence.md` | Hestia persistence concept | Information / Data | Duplicate concept of `hestia.md`. |
| 36 | `docs/concepts/` | `hestia.md` | Hestia data services concept | Application Architecture | Persistence/caching subsystem concept. |
| 37 | `docs/concepts/` | `iris.md` | Iris presentation concept | Application Architecture | Frontend presentation concept. |
| 38 | `docs/concepts/` | `mneme.md` | Mneme active cache concept | Information / Data | Active cache coordination concept. |
| 39 | `docs/concepts/` | `mnemosyne.md` | Mnemosyne durable persistence concept | Information / Data | Authoritative durable storage concept. |
| 40 | `docs/concepts/` | `paradeigma.md` | Paradeigma exemplar simulation concept | Simulation / Testing | Exemplar simulation concept. |
| 41 | `docs/concepts/` | `petasos-messaging.md` | Petasos messaging concept | Integration Architecture | Duplicate concept of `petasos.md`. |
| 42 | `docs/concepts/` | `petasos.md` | Petasos resilient messaging concept | Integration Architecture | Messaging subsystem concept. |
| 43 | `docs/concepts/` | `ponos.md` | Ponos WorkEngine execution concept | Application Architecture | Asynchronous task execution concept. |
| 44 | `docs/concepts/` | `pragma.md` | Pragma task instance concept | Application Architecture | Task execution wrapper concept. |
| 45 | `docs/concepts/` | `praxis.md` | Praxis task sequence concept | Application Architecture | Task sequence orchestration concept. |
| 46 | `docs/concepts/` | `pylai-gateways.md` | Pylai gateways concept | Integration Architecture | Duplicate concept of `pylai.md`. |
| 47 | `docs/concepts/` | `pylai.md` | Pylai interoperability concept | Integration Architecture | Gateway membrane concept. |
| 48 | `docs/concepts/` | `themis-security.md` | Themis security engine concept | Security Architecture | Duplicate concept of `themis.md`. |
| 49 | `docs/concepts/` | `themis.md` | Themis authorization concept | Security Architecture | Policy governance concept. |
| 50 | `docs/configuration/` | `complete-reference.md` | Consolidated configuration guide | Technology Architecture | Dual-dimension configuration reference. |
| 51 | `docs/configuration/` | `configuration-register.md`| Configuration property register | Technology Architecture | Duplicate of reference register. |
| 52 | `docs/configuration/` | `environment-matrix.md` | Environment variables and profiles | Technology Architecture | Dev, Docker, and K8s environment matrix. |
| 53 | `docs/configuration/` | `ports-and-protocols.md` | 18 network listeners reference | Technology Architecture | Duplicate of port-protocol registers. |
| 54 | `docs/deployment/` | `ansible-orchestration.md`| Ansible playbooks for host provisioning | Technology Architecture | Host automation runbook. |
| 55 | `docs/deployment/` | `component-inventory.md` | Deployed container workload inventory | Technology Architecture | Pod and container catalog. |
| 56 | `docs/deployment/` | `kubernetes-workloads.md`| K8s StatefulSets, Deployments, Services | Technology Architecture | K8s manifest documentation. |
| 57 | `docs/deployment/` | `microk8s-reference-guide.md`| Detailed MicroK8s administrator guide | Technology Architecture | Comprehensive K8s admin guide. |
| 58 | `docs/deployment/` | `microk8s.md` | MicroK8s installation & setup | Technology Architecture | Quickstart setup guide for MicroK8s. |
| 59 | `docs/deployment/` | `normal-deployment.md` | Production deployment runbook | Technology Architecture | Step-by-step cluster rollout. |
| 60 | `docs/deployment/` | `prerequisites.md` | Host hardware & software prerequisites | Technology Architecture | System requirements and dependencies. |
| 61 | `docs/deployment/` | `shutdown.md` | Graceful cluster shutdown runbook | Technology Architecture | Drain and shutdown procedures. |
| 62 | `docs/deployment/` | `startup.md` | Cluster bootstrap and startup ordering | Technology Architecture | Dependency-ordered startup sequence. |
| 63 | `docs/deployment/` | `ubuntu.md` | Ubuntu host OS preparation guide | Technology Architecture | Kernel tuning, storage, and networking. |
| 64 | `docs/deployment/` | `undeploy.md` | Workload teardown and cleanup runbook | Technology Architecture | Namespace teardown and PVC handling. |
| 65 | `docs/deployment/` | `upgrade.md` | Rolling upgrade and version migration | Technology Architecture | Upgrade strategies and rollback. |
| 66 | `docs/deployment/` | `verification.md` | Post-deployment smoke tests | Technology Architecture | Cluster validation checks. |
| 67 | `docs/design/` | `governed-write-concurrency-contract.md`| Governed write concurrency contract | Information / Data | Authoritative specification for ADR-018/019. |
| 68 | `docs/getting-started/`| `architecture-at-a-glance.md`| High-level visual walkthrough | Reference / Supporting | Fast visual orientation. |
| 69 | `docs/getting-started/`| `build.md` | Maven and NPM build instructions | Implementation / Migration | Build instructions and tooling setup. |
| 70 | `docs/getting-started/`| `first-deployment.md` | Local single-node deployment guide | Implementation / Migration | First-time deployment tutorial. |
| 71 | `docs/getting-started/`| `introduction.md` | Healthcare context and interoperability | Motivation & Strategy | Healthcare background and problem statement. |
| 72 | `docs/getting-started/`| `terminology.md` | Greek naming taxonomy and glossary | Motivation & Strategy | Etymology and concept definitions. |
| 73 | `docs/implementation/`| `harmonia-convergence-runtime-integration-plan.md`| Master convergence plan | Implementation / Migration | Authoritative convergence roadmap (M1-M8). |
| 74 | `docs/integration/` | `correlation.md` | Correlation, causation, and tracing | Integration Architecture | Tracing standards across exchanges. |
| 75 | `docs/integration/` | `error-handling.md` | Integration exception handling and NACKs | Integration Architecture | Boundary error translation. |
| 76 | `docs/integration/` | `fhir.md` | FHIR R5 REST API specifications | Integration Architecture | Supported FHIR endpoints and interactions. |
| 77 | `docs/integration/` | `hl7-v2.md` | HL7 v2.4/v2.5 MLLP message formats | Integration Architecture | ADT, ORM, ORU segments and mappings. |
| 78 | `docs/integration/` | `messaging.md` | Petasos messaging patterns and queues | Integration Architecture | Queue topologies and consumers. |
| 79 | `docs/integration/` | `overview.md` | Gateway and interoperability overview | Integration Architecture | Gateway architecture overview. |
| 80 | `docs/integration/` | `pylai.md` | Pylai protocol gateway design | Integration Architecture | Pylai boundary contracts. |
| 81 | `docs/integration/` | `rest.md` | Internal microservice REST APIs | Integration Architecture | Inter-service REST specifications. |
| 82 | `docs/latex/` | `README.md` | LaTeX build guide and style documentation | Reference / Supporting | Instructions for compiling LaTeX/TikZ. |
| 83 | `docs/libreoffice/` | `README.md` | LibreOffice ODT generator documentation | Reference / Supporting | Instructions for compiling ODT via Python. |
| 84 | `docs/middleware/` | `activeMQ-artemis.md` | ActiveMQ Artemis messaging engine | Technology Architecture | Mixed-case filename; broker configuration. |
| 85 | `docs/middleware/` | `hapi-fhir.md` | HAPI FHIR JPA persistence engine | Technology Architecture | HAPI FHIR server architecture. |
| 86 | `docs/middleware/` | `infinispan.md` | Infinispan clustered caching grid | Technology Architecture | Hot Rod and JGroups architecture. |
| 87 | `docs/middleware/` | `kubernetes.md` | Kubernetes container orchestration | Technology Architecture | K8s platform specifications. |
| 88 | `docs/middleware/` | `matrix-synapse.md` | Matrix Synapse collaboration server | Technology Architecture | Synapse homeserver setup. |
| 89 | `docs/middleware/` | `overview.md` | Middleware stack overview | Technology Architecture | Overview of the supporting infrastructure. |
| 90 | `docs/middleware/` | `postgresql.md` | PostgreSQL relational database engine | Technology Architecture | RDBMS configuration and tuning. |
| 91 | `docs/middleware/` | `wildfly.md` | WildFly Jakarta EE 10 application server | Technology Architecture | Application server runtime for Ponos/BEFE. |
| 92 | `docs/modules/` | `agora.md` | Agora collaboration module guide | Application Architecture | Implementation details for `agora/`. |
| 93 | `docs/modules/` | `calliope.md` | Calliope canonical schema module guide | Application Architecture | Implementation details for `calliope/`. |
| 94 | `docs/modules/` | `energeia.md` | Energeia workflow module guide | Application Architecture | Implementation details for `energeia/`. |
| 95 | `docs/modules/` | `hestia.md` | Hestia persistence & cache module guide | Application Architecture | Implementation details for `hestia/`. |
| 96 | `docs/modules/` | `iris.md` | Iris presentation module guide | Application Architecture | Implementation details for `iris/`. |
| 97 | `docs/modules/` | `paradeigma.md` | Paradeigma simulation module guide | Simulation / Testing | Implementation details for `paradeigma/`. |
| 98 | `docs/modules/` | `petasos.md` | Petasos messaging module guide | Application Architecture | Implementation details for `petasos/`. |
| 99 | `docs/modules/` | `pylai.md` | Pylai gateways module guide | Application Architecture | Implementation details for `pylai/`. |
| 100 | `docs/modules/` | `themis.md` | Themis security module guide | Security Architecture | Implementation details for `themis/`. |
| 101 | `docs/operations/` | `health-readiness.md` | Liveness and readiness probe runbook | Technology Architecture | Pod probe configurations and monitoring. |
| 102 | `docs/operations/` | `phi-sanitized-logging.md`| PHI-sanitized logging operational runbook| Security Architecture | Zero-PHI log masking procedures. |
| 103 | `docs/operations/` | `verification-runbook.md`| Platform health verification procedures | Resilience / Operations | Operational runbook for verifying cluster. |
| 104 | `docs/paradeigma/` | `architecture.md` | Paradeigma simulation architecture | Simulation / Testing | Architecture of the simulation testbed. |
| 105 | `docs/paradeigma/` | `concepts.md` | Exemplar simulation concepts | Simulation / Testing | Conceptual foundations of simulation. |
| 106 | `docs/paradeigma/` | `configuration.md` | Simulator configuration properties | Simulation / Testing | Scenario properties and parameters. |
| 107 | `docs/paradeigma/` | `deployment-guide.md` | Simulator deployment runbook | Simulation / Testing | Running simulators in Docker/K8s. |
| 108 | `docs/paradeigma/` | `deployment.md` | Simulator deployment specifications | Simulation / Testing | Overlaps with `deployment-guide.md`. |
| 109 | `docs/paradeigma/` | `examples.md` | Clinical simulation walkthroughs | Simulation / Testing | Sample clinical exchange traces. |
| 110 | `docs/paradeigma/` | `failure-injection.md`| Fault injection testing framework | Simulation / Testing | Testing MLLP NACKs and broker drops. |
| 111 | `docs/paradeigma/` | `isolation-invariants.md`| Production isolation invariants | Simulation / Testing | Architectural invariant: zero prod leakage. |
| 112 | `docs/paradeigma/` | `logging-validation.md`| Simulator log validation | Simulation / Testing | Verifying simulator telemetry. |
| 113 | `docs/paradeigma/` | `normal-vs-paradeigma.md`| Normal vs simulator mode comparison | Simulation / Testing | Delineation between prod and simulator. |
| 114 | `docs/paradeigma/` | `overview.md` | Paradeigma subsystem overview | Simulation / Testing | High-level simulation framework tour. |
| 115 | `docs/paradeigma/` | `production-isolation.md`| Production isolation enforcement | Simulation / Testing | Overlaps with `isolation-invariants.md`. |
| 116 | `docs/paradeigma/` | `scenarios.md` | Synthetic clinical scenario catalogue | Simulation / Testing | ADT, ORM, and ORU scenario definitions. |
| 117 | `docs/paradeigma/` | `security-testing.md` | Simulator security and auth validation | Simulation / Testing | Gated simulation testing. |
| 118 | `docs/paradeigma/` | `simulation-framework.md`| Scenario engine architecture | Simulation / Testing | Detailed generator framework. |
| 119 | `docs/paradeigma/` | `synthetic-data.md` | Synthetic patient & clinical data models| Simulation / Testing | Deterministic patient generator rules. |
| 120 | `docs/provider-registry/`| `architecture.md` | Provider Registry architecture | Application (Solution) | Local Directory Solution architecture. |
| 121 | `docs/provider-registry/`| `audit-provenance.md` | Provider audit and Kleio provenance | Security Architecture | Provider change audit records. |
| 122 | `docs/provider-registry/`| `change-processing.md`| Provider change state machine | Application (Solution) | Provider change approval progression. |
| 123 | `docs/provider-registry/`| `failure-recovery.md` | Provider Registry recovery procedures | Resilience / Operations | Specific recovery runbooks for registry. |
| 124 | `docs/provider-registry/`| `fhir-api.md` | Provider Registry FHIR REST endpoints | Integration Architecture | Practitioner, Role, Location REST APIs. |
| 125 | `docs/provider-registry/`| `persistence.md` | Provider Registry relational persistence | Information / Data | Database tables and schemas for registry. |
| 126 | `docs/provider-registry/`| `resource-model.md` | Provider Registry FHIR resource model | Information / Data | Practitioner, Organization entity relations. |
| 127 | `docs/provider-registry/`| `search.md` | Authoritative provider search engine | Application (Solution) | Database-backed provider search logic. |
| 128 | `docs/provider-registry/`| `security.md` | Provider Registry security governance | Security Architecture | Security labels and access control. |
| 129 | `docs/provider-registry/`| `testing.md` | Provider Registry test suite | Simulation / Testing | Unit and integration test catalog. |
| 130 | `docs/provider-registry/`| `validation.md` | Structural and business rule validation | Application (Solution) | Business validation state machines. |
| 131 | `docs/reference/` | `configuration-register.md`| Reference configuration properties | Technology Architecture | Duplicate of configuration register. |
| 132 | `docs/reference/` | `port-protocol-register.md`| Reference network port register | Technology Architecture | Duplicate of port-protocol register. |
| 133 | `docs/security/` | `architecture.md` | Security architecture and Themis engine | Security Architecture | Defence-in-depth and boundary checkpoints. |
| 134 | `docs/security/` | `audit.md` | Security audit event logging | Security Architecture | Non-PHI security audit stream. |
| 135 | `docs/security/` | `ergon-security.md` | Ergon activity security context | Security Architecture | Activity execution security propagation. |
| 136 | `docs/security/` | `failure-behaviour.md`| Security failure modes & fail-closed | Security Architecture | Fail-closed enforcement on security failure. |
| 137 | `docs/security/` | `logging.md` | PHI-aware logging architecture | Security Architecture | Overlaps with `phi-sanitized-logging.md`. |
| 138 | `docs/security/` | `policy-model.md` | Themis policy evaluation & precedence | Security Architecture | Deterministic default-deny policy rules. |
| 139 | `docs/security/` | `ponos-security.md` | Ponos WorkEngine authorization | Security Architecture | Worker execution security context. |
| 140 | `docs/security/` | `pragma-security.md` | Pragma security metadata and tokens | Security Architecture | Security context carried within Pragma. |
| 141 | `docs/security/` | `principals.md` | Security principals & identity model | Security Architecture | User, system, and service principals. |
| 142 | `docs/security/` | `provider-registry-security.md`| Provider Registry security enforcement| Security Architecture | Overlaps with `provider-registry/security.md`. |
| 143 | `docs/security/` | `pylai-security.md` | Gateway boundary security enforcement | Security Architecture | Ingress and egress security interceptors. |
| 144 | `docs/security/` | `roles-authorities.md` | Mnemonic roles to granular authorities | Security Architecture | Mapping `HarmoniaRole` to `HarmoniaAuthority`. |
| 145 | `docs/security/` | `security-gaps.md` | Security gap analysis and audit | Architecture Governance | Audit of security invariants and exceptions. |
| 146 | `docs/security/` | `security-labels.md` | FHIR security labels & confidentiality | Security Architecture | Mapping labels to FHIR `meta.security`. |
| 147 | `docs/security/` | `service-identities.md`| Internal system service identities | Security Architecture | Service account naming and certificates. |
| 148 | `docs/security/` | `testing.md` | Security test suite & verification | Security Architecture | Automated security and ArchUnit tests. |
| 149 | `docs/security/` | `themis.md` | Themis subsystem architecture | Security Architecture | Core Themis service implementation. |
| 150 | `docs/security/` | `threat-model.md` | Threat model & STRIDE assessment | Security Architecture | Threat analysis and mitigations. |

# Proposed Target Architecture

### Proposed Target Directory Hierarchy

```text
docs/
├── markdown/
│   ├── 01-motivation-strategy/
│   │   ├── README.md
│   │   ├── harmonia-strategy-local-directory.md
│   │   ├── healthcare-context.md
│   │   ├── platform-concept.md
│   │   └── terminology-etymology.md
│   ├── 02-principles-governance/
│   │   ├── README.md
│   │   ├── architectural-axioms.md
│   │   ├── architecture-decisions.md
│   │   ├── agent-guardrails.md
│   │   ├── convergence-report.md
│   │   └── security-gaps.md
│   ├── 03-business-architecture/
│   │   ├── README.md
│   │   ├── patient-identity-events.md
│   │   ├── provider-management-events.md
│   │   └── business-process-models.md
│   ├── 04-application-architecture/
│   │   ├── README.md
│   │   ├── system-overview.md
│   │   ├── system-inventory.md
│   │   ├── execution-model.md
│   │   ├── subsystems/
│   │   │   ├── calliope.md
│   │   │   ├── themis.md
│   │   │   ├── hestia.md
│   │   │   ├── petasos.md
│   │   │   ├── energeia.md
│   │   │   ├── pylai.md
│   │   │   ├── iris.md
│   │   │   └── agora.md
│   │   ├── concepts/
│   │   │   ├── ponos.md
│   │   │   ├── ergon.md
│   │   │   ├── praxis.md
│   │   │   ├── pragma.md
│   │   │   ├── mneme.md
│   │   │   └── mnemosyne.md
│   │   ├── presentation/
│   │   │   ├── iris-design-system.md
│   │   │   └── iris-operations-console.md
│   │   └── solutions/
│   │       └── provider-registry/
│   │           ├── architecture.md
│   │           ├── change-processing.md
│   │           ├── search.md
│   │           └── validation.md
│   ├── 05-information-architecture/
│   │   ├── README.md
│   │   ├── persistence-architecture.md
│   │   ├── database-schema.md
│   │   ├── governed-write-concurrency-contract.md
│   │   ├── canonical-models.md
│   │   ├── provider-registry-models.md
│   │   └── provider-registry-persistence.md
│   ├── 06-integration-architecture/
│   │   ├── README.md
│   │   ├── gateway-overview.md
│   │   ├── petasos-messaging.md
│   │   ├── message-lifecycle.md
│   │   ├── protocols/
│   │   │   ├── hl7-v2.md
│   │   │   ├── fhir-rest.md
│   │   │   ├── mllp-gateways.md
│   │   │   └── provider-registry-api.md
│   │   ├── correlation-and-tracing.md
│   │   └── error-handling.md
│   ├── 07-technology-architecture/
│   │   ├── README.md
│   │   ├── runtime-architecture.md
│   │   ├── middleware/
│   │   │   ├── activemq-artemis.md
│   │   │   ├── infinispan.md
│   │   │   ├── postgresql.md
│   │   │   ├── hapi-fhir.md
│   │   │   ├── wildfly.md
│   │   │   ├── matrix-synapse.md
│   │   │   ├── kubernetes.md
│   │   │   └── overview.md
│   │   ├── network/
│   │   │   ├── port-protocol-register.md
│   │   │   └── ingress-routing.md
│   │   ├── configuration/
│   │   │   ├── configuration-register.md
│   │   │   ├── environment-matrix.md
│   │   │   └── complete-reference.md
│   │   └── deployment/
│   │       ├── microk8s-reference-guide.md
│   │       ├── kubernetes-workloads.md
│   │       ├── ansible-orchestration.md
│   │       ├── component-inventory.md
│   │       ├── prerequisites.md
│   │       ├── normal-deployment.md
│   │       ├── startup-shutdown.md
│   │       ├── upgrade-undeploy.md
│   │       └── verification.md
│   ├── 08-security-architecture/
│   │   ├── README.md
│   │   ├── security-overview.md
│   │   ├── themis-engine.md
│   │   ├── policy-model.md
│   ��   ├── principals-and-identities.md
│   │   ├── roles-and-authorities.md
│   │   ├── security-labels.md
│   │   ├── threat-model.md
│   │   ├── non-phi-audit.md
│   │   ├── phi-sanitized-logging.md
│   │   ├── boundary-enforcement/
│   │   │   ├── pylai-security.md
│   │   │   ├── ponos-security.md
│   │   │   ├── pragma-security.md
│   │   │   ├── ergon-security.md
│   │   │   └── provider-registry-security.md
│   │   └── failure-behaviour.md
│   ├── 09-resilience-operations/
│   │   ├── README.md
│   │   ├── high-availability.md
│   │   ├── failure-recovery.md
│   │   ├── failure-scenarios.md
│   │   ├── recovery-guarantees.md
│   │   ├── persistence-recovery-gaps.md
│   │   ├── health-readiness.md
│   │   └── verification-runbook.md
│   ├── 10-simulation-testing/
│   │   ├── README.md
│   │   ├── paradeigma-overview.md
│   │   ├── simulation-architecture.md
│   │   ├── isolation-invariants.md
│   │   ├── simulation-framework.md
│   │   ├── scenario-catalogue.md
│   │   ├── synthetic-data.md
│   │   ├── failure-injection.md
│   │   ├── simulation-deployment.md
│   │   ├── simulation-security.md
│   │   └── provider-registry-testing.md
│   └── 11-implementation-migration/
│       ├── README.md
│       ├── convergence-master-plan.md
│       ├── task-9-backlog.md
│       ├── build-instructions.md
│       └── first-deployment.md
├── latex/
│   ├── chapters/
│   ├── diagrams/
│   ├── images/
│   ├── styles/
│   ├── Makefile
│   └── main.tex
└── odt/
    ├── assets/
    ├── scripts/
    ├── templates/
    ├── Makefile
    ├── harmonia-architecture-specification.odt
    └── Harmonia_Strategy_Local_Directory_and_Broader_Role.docx
```

---

### TOGAF & ArchiMate Mapping Rationale

The proposed Markdown hierarchy directly mirrors the TOGAF Architecture Development Method (ADM) phases and ArchiMate 3.2 layers while remaining pragmatic and engineer-friendly:

```
+-----------------------------------------------------------------------------------------+
|                              TOGAF / ARCHIMATE DOMAIN MAP                               |
+--------------------------+------------------------------+-------------------------------+
| Directory Branch         | TOGAF ADM Equivalent         | ArchiMate 3.2 Concepts        |
+--------------------------+------------------------------+-------------------------------+
| 01-motivation-strategy/  | Preliminary / Architecture   | Stakeholder, Driver, Goal,    |
|                          | Vision (Phase A)             | Principle, Requirement        |
+--------------------------+------------------------------+-------------------------------+
| 02-principles-governance/| Preliminary Phase &          | Principle, Constraint,        |
|                          | Architecture Governance (G)  | Meaning, Business Rule        |
+--------------------------+------------------------------+-------------------------------+
| 03-business-architecture/| Phase B: Business            | Business Actor, Business Role,|
|                          | Architecture                 | Business Process, Event       |
+--------------------------+------------------------------+-------------------------------+
| 04-application-          | Phase C: Application         | Application Component,        |
|    architecture/         | Architecture                 | Application Function, Service |
+--------------------------+------------------------------+-------------------------------+
| 05-information-          | Phase C: Data Architecture   | Data Object, Representation,  |
|    architecture/         |                              | Passive Structure Element     |
+--------------------------+------------------------------+-------------------------------+
| 06-integration-          | Phase C: Interoperability &  | Application Interface,        |
|    architecture/         | Communication                | Application Interaction       |
+--------------------------+------------------------------+-------------------------------+
| 07-technology-           | Phase D: Technology          | Node, Device, System Software,|
|    architecture/         | Architecture                 | Path, Network, Artifact       |
+--------------------------+------------------------------+-------------------------------+
| 08-security-architecture/| Cross-Cutting Security       | Security Principle, Constraint|
|                          | Architecture                 | Assessment, Threat, Risk      |
+--------------------------+------------------------------+-------------------------------+
| 09-resilience-operations/| Cross-Cutting Resilience &   | Work Package, Plateau,        |
|                          | Service Management           | Service Level, Gap            |
+--------------------------+------------------------------+-------------------------------+
| 10-simulation-testing/   | Architecture Realization &   | Test Bed, Exemplar Node,      |
|                          | Verification (Exemplar)      | Simulation Artifact           |
+--------------------------+------------------------------+-------------------------------+
| 11-implementation-       | Phase E: Opportunities &     | Deliverable, Work Package,    |
|    migration/            | Phase F: Migration Planning  | Implementation Plateau        |
+--------------------------+------------------------------+-------------------------------+
```

#### Detailed Domain Alignment:

1. **`01-motivation-strategy/` (Motivation Aspect)**:
   - Captures **why** Harmonia exists: strategic drivers, healthcare interoperability problems, Australian LDS positioning, and core architectural goals.
   - Houses the converted strategy DOCX, healthcare introduction, and Greek etymology/glossary.
2. **`02-principles-governance/` (Architecture Governance & Principles)**:
   - Houses supreme design rules: `architectural-axioms.md` (AX-01 to AX-15), `architecture-decisions.md` (ADRs), and `agent-guardrails.md` (`AGENTS.md`).
   - Elevates architecture principles to first-class status, ensuring they govern all technical designs.
3. **`03-business-architecture/` (Business Layer)**:
   - Contains business-level event catalogues (`patient-identity-events.md`, `provider-management-events.md`) and clinical workflows.
   - Establishes that events reflect clinical and organizational significance rather than mere database mutations.
4. **`04-application-architecture/` (Application Layer)**:
   - Contains software subsystems (`calliope`, `themis`, `hestia`, `petasos`, `energeia`, `pylai`, `iris`, `agora`), conceptual building blocks (`ponos`, `ergon`, `praxis`, `pragma`), presentation architectures, and solution-specific compositions (Provider Registry).
   - Separates reusable platform components from application-specific business solutions.
5. **`05-information-architecture/` (Data Architecture / Information Layer)**:
   - Houses data models, relational database schemas (`database-schema.md`), multi-tier storage specifications (`persistence-architecture.md`), and formal concurrency contracts (`governed-write-concurrency-contract.md`).
   - Preserves AX-05 by cleanly differentiating active-state cache models (Mneme) from durable truth schemas (Mnemosyne).
6. **`06-integration-architecture/` (Application Interaction)**:
   - Houses protocol specifications (HL7 v2, FHIR R5 REST, Netty MLLP), Petasos messaging topologies, correlation tracking, and end-to-end message lifecycles (`message-lifecycle.md`).
7. **`07-technology-architecture/` (Technology Layer)**:
   - Houses infrastructure middleware specifications (ActiveMQ Artemis, Infinispan, PostgreSQL, WildFly, Matrix), the canonical 18-listener network register, configuration registers, and all MicroK8s/Ansible deployment runbooks.
8. **`08-security-architecture/` (Cross-Cutting Concern)**:
   - Dedicated domain for Themis default-deny governance, policy precedence, RBAC/ABAC roles, security labels, non-PHI auditing, and zero-PHI logging.
9. **`09-resilience-operations/` (Cross-Cutting Concern)**:
   - Dedicated domain for high availability clustering, failure recovery matrices, RPO/RTO SLAs, health runbooks, and telemetry.
10. **`10-simulation-testing/` (Architecture Verification)**:
    - Dedicated domain for Paradeigma clinical simulator, synthetic data generators, failure injection, and compile-time/runtime isolation invariants.
11. **`11-implementation-migration/` (Implementation & Migration)**:
    - Houses master implementation roadmaps (`harmonia-convergence-runtime-integration-plan.md`), task backlogs, build instructions, and developer setup guides.

# Markdown Migration Matrix

### Proposed File Migration Map (All 150 Markdown Files)

The following matrix maps every existing Markdown file to its proposed target path under `docs/markdown/`.

| # | Current Path | Proposed Path | Architectural Classification | Reason for Placement | Rename? | Notes |
| :-: | :--- | :--- | :--- | :--- | :-: | :--- |
| 1 | `docs/AGENTS.md` | `docs/markdown/02-principles-governance/agent-guardrails.md` | Principles / Guardrails | Normative agent rules belong in governance. | Yes | Root `/AGENTS.md` remains authoritative symlink/mirror. |
| 2 | `docs/README.md` | `docs/markdown/README.md` | Reference / Supporting | Root index for the reorganized Markdown documentation. | No | Updated with new TOGAF domain table of contents. |
| 3 | `docs/architectural-axioms.md` | `docs/markdown/02-principles-governance/architectural-axioms.md` | Principles / Guardrails | Supreme architectural authority belongs in governance. | No | Update root references and AGENTS.md links. |
| 4 | `docs/architecture-decisions.md` | `docs/markdown/02-principles-governance/architecture-decisions.md` | Architecture Decisions | Architecture Decision Records belong in governance. | No | Maintain historical ADR-001..026 numbering. |
| 5 | `docs/architecture.md` | `docs/markdown/04-application-architecture/system-overview.md` | Application Architecture | High-level platform architecture summary. | Yes | Differentiate from specific subsystem docs. |
| 6 | `docs/database-schema.md` | `docs/markdown/05-information-architecture/database-schema.md` | Information / Data | Relational database schema and DDL definitions. | No | Authoritative relational DDL. |
| 7 | `docs/deployment-microk8s.md` | `docs/markdown/07-technology-architecture/deployment/microk8s-reference-guide.md` | Technology Architecture | Relocate to technology deployment branch. | Yes | Consolidate with existing reference guide. |
| 8 | `docs/failure-recovery.md` | `docs/markdown/09-resilience-operations/failure-recovery.md` | Resilience / Operations | Failure matrix and recovery procedures belong in resilience. | No | Primary authoritative failure-recovery guide. |
| 9 | `docs/failure-scenarios.md` | `docs/markdown/09-resilience-operations/failure-scenarios.md` | Resilience / Operations | Concrete operational failure walkthroughs. | No | Operational scenarios and runbook. |
| 10 | `docs/high-availability.md` | `docs/markdown/09-resilience-operations/high-availability.md` | Resilience / Operations | Clustered HA topology and replication. | No | Multi-node clustering models. |
| 11 | `docs/message-lifecycle.md` | `docs/markdown/06-integration-architecture/message-lifecycle.md` | Integration Architecture | End-to-end clinical message flows and transaction boundaries.| No | Detailed clinical message sequences. |
| 12 | `docs/patient-identity-events.md` | `docs/markdown/03-business-architecture/patient-identity-events.md` | Business Architecture | Patient identity business event catalog. | No | Business domain events driving Praxis. |
| 13 | `docs/persistence-architecture.md`| `docs/markdown/05-information-architecture/persistence-architecture.md` | Information / Data | Multi-tier persistence model and entity catalog. | No | Primary 4-tier storage architecture. |
| 14 | `docs/persistence-recovery-gaps.md`| `docs/markdown/09-resilience-operations/persistence-recovery-gaps.md` | Architecture Governance | Persistence & recovery gap analysis (REC-001..004). | No | Retain as operational gap register. |
| 15 | `docs/provider-management-events.md`| `docs/markdown/03-business-architecture/provider-management-events.md`| Business Architecture | Provider management business event catalog. | No | Business domain events for LDS/Practitioners. |
| 16 | `docs/recovery-guarantees.md` | `docs/markdown/09-resilience-operations/recovery-guarantees.md` | Resilience / Operations | RPO/RTO and formal delivery guarantees. | No | SLA and delivery guarantees. |
| 17 | `docs/architecture/convergence-report.md`| `docs/markdown/02-principles-governance/convergence-report.md` | Architecture Governance | Architecture convergence baseline report. | No | Historical governance baseline. |
| 18 | `docs/architecture/execution-model.md` | `docs/markdown/04-application-architecture/execution-model.md` | Application Architecture | Ponos, Erga, Praxis execution dynamics. | No | Core workflow engine execution. |
| 19 | `docs/architecture/failure-recovery.md`| `docs/markdown/09-resilience-operations/failure-recovery-architecture.md`| Resilience / Operations | Variant failure recovery guide. | Yes | Consolidate into `failure-recovery.md` in cleanup. |
| 20 | `docs/architecture/iris-design-system.md`| `docs/markdown/04-application-architecture/presentation/iris-design-system.md` | Application Architecture | Presentation tier design system and Vue components. | No | Frontend UI standards. |
| 21 | `docs/architecture/iris-operations-console.md`| `docs/markdown/04-application-architecture/presentation/iris-operations-console.md`| Application Architecture | Operations console frontend architecture. | No | Operations UI architecture. |
| 22 | `docs/architecture/overview.md` | `docs/markdown/04-application-architecture/platform-overview.md` | Application Architecture | 5-tier architecture overview. | Yes | Disambiguate from root overview. |
| 23 | `docs/architecture/persistence-lifecycle.md`| `docs/markdown/05-information-architecture/persistence-lifecycle.md` | Information / Data | Storage lifecycle and write-behind synchronization. | No | Outdated DDL flagged for cleanup. |
| 24 | `docs/architecture/port-protocol-register.md`| `docs/markdown/07-technology-architecture/network/port-protocol-register.md` | Technology Architecture | 18 network listeners register. | No | Consolidate with duplicate registers. |
| 25 | `docs/architecture/runtime-architecture.md` | `docs/markdown/07-technology-architecture/runtime-architecture.md` | Technology Architecture | Physical container and process topology. | No | Concrete runtime layouts. |
| 26 | `docs/architecture/system-inventory.md` | `docs/markdown/04-application-architecture/system-inventory.md` | Application Architecture | Subsystem component inventory. | No | Module breakdown and stack inventory. |
| 27 | `docs/backlog/Harmonia - Task 9 - Backlog.md` | `docs/markdown/11-implementation-migration/task-9-backlog.md` | Implementation / Migration | Specific task backlog. | Yes | Normalize filename (remove spaces). |
| 28 | `docs/concepts/agora.md` | `docs/markdown/04-application-architecture/concepts/agora.md` | Application Architecture | Greek naming and conceptual role for Agora. | No | Component concept. |
| 29 | `docs/concepts/calliope-canonical.md` | `docs/markdown/05-information-architecture/canonical-models.md` | Information / Data | Canonical schema and converter concepts. | Yes | Differentiate from module documentation. |
| 30 | `docs/concepts/calliope.md` | `docs/markdown/04-application-architecture/concepts/calliope.md` | Application Architecture | Conceptual role of Calliope. | No | Component concept. |
| 31 | `docs/concepts/energeia-workflow.md` | `docs/markdown/04-application-architecture/concepts/energeia-workflow.md`| Application Architecture | Workflow engine conceptual model. | No | Workflow concepts. |
| 32 | `docs/concepts/energeia.md` | `docs/markdown/04-application-architecture/concepts/energeia.md` | Application Architecture | Energeia conceptual foundations. | No | Component concept. |
| 33 | `docs/concepts/ergon.md` | `docs/markdown/04-application-architecture/concepts/ergon.md` | Application Architecture | Ergon activity unit concept. | No | Activity unit concept. |
| 34 | `docs/concepts/harmonia.md` | `docs/markdown/01-motivation-strategy/platform-concept.md` | Motivation & Strategy | Overarching platform concept. | Yes | Strategic platform concept. |
| 35 | `docs/concepts/hestia-persistence.md` | `docs/markdown/05-information-architecture/hestia-persistence-concept.md`| Information / Data | Conceptual storage models in Hestia. | Yes | Storage concept. |
| 36 | `docs/concepts/hestia.md` | `docs/markdown/04-application-architecture/concepts/hestia.md` | Application Architecture | Hestia data services concept. | No | Component concept. |
| 37 | `docs/concepts/iris.md` | `docs/markdown/04-application-architecture/concepts/iris.md` | Application Architecture | Presentation services concept. | No | Component concept. |
| 38 | `docs/concepts/mneme.md` | `docs/markdown/04-application-architecture/concepts/mneme.md` | Information / Data | Active cache coordination concept. | No | Active state concept. |
| 39 | `docs/concepts/mnemosyne.md` | `docs/markdown/04-application-architecture/concepts/mnemosyne.md` | Information / Data | Authoritative durable persistence concept. | No | Authoritative state concept. |
| 40 | `docs/concepts/paradeigma.md` | `docs/markdown/10-simulation-testing/paradeigma-concept.md` | Simulation / Testing | Exemplar simulation concept. | Yes | Relocate to simulation branch. |
| 41 | `docs/concepts/petasos-messaging.md` | `docs/markdown/06-integration-architecture/petasos-messaging-concept.md` | Integration Architecture | Messaging patterns and envelopes concept. | Yes | Relocate to integration branch. |
| 42 | `docs/concepts/petasos.md` | `docs/markdown/04-application-architecture/concepts/petasos.md` | Application Architecture | Petasos subsystem concept. | No | Component concept. |
| 43 | `docs/concepts/ponos.md` | `docs/markdown/04-application-architecture/concepts/ponos.md` | Application Architecture | Ponos execution engine concept. | No | Execution engine concept. |
| 44 | `docs/concepts/pragma.md` | `docs/markdown/04-application-architecture/concepts/pragma.md` | Application Architecture | Pragma task instance wrapper concept. | No | Task wrapper concept. |
| 45 | `docs/concepts/praxis.md` | `docs/markdown/04-application-architecture/concepts/praxis.md` | Application Architecture | Praxis task sequence concept. | No | Task sequence concept. |
| 46 | `docs/concepts/pylai-gateways.md` | `docs/markdown/06-integration-architecture/pylai-gateways-concept.md` | Integration Architecture | Protocol gateway concept. | Yes | Relocate to integration branch. |
| 47 | `docs/concepts/pylai.md` | `docs/markdown/04-application-architecture/concepts/pylai.md` | Application Architecture | Gateway membrane concept. | No | Component concept. |
| 48 | `docs/concepts/themis-security.md` | `docs/markdown/08-security-architecture/themis-security-concept.md` | Security Architecture | Security authorization concept. | Yes | Relocate to security branch. |
| 49 | `docs/concepts/themis.md` | `docs/markdown/04-application-architecture/concepts/themis.md` | Application Architecture | Themis policy engine concept. | No | Component concept. |
| 50 | `docs/configuration/complete-reference.md`| `docs/markdown/07-technology-architecture/configuration/complete-reference.md`| Technology Architecture | Complete dual-dimension configuration reference. | No | Configuration reference. |
| 51 | `docs/configuration/configuration-register.md`| `docs/markdown/07-technology-architecture/configuration/configuration-register.md`| Technology Architecture | Configuration properties register. | No | Property register. |
| 52 | `docs/configuration/environment-matrix.md`| `docs/markdown/07-technology-architecture/configuration/environment-matrix.md`| Technology Architecture | Environment matrix and Spring profiles. | No | Deployment profiles. |
| 53 | `docs/configuration/ports-and-protocols.md`| `docs/markdown/07-technology-architecture/network/ports-and-protocols.md`| Technology Architecture | Network listeners reference. | No | Consolidate with port register in cleanup. |
| 54 | `docs/deployment/ansible-orchestration.md`| `docs/markdown/07-technology-architecture/deployment/ansible-orchestration.md`| Technology Architecture | Ansible host provisioning automation. | No | Automation playbooks. |
| 55 | `docs/deployment/component-inventory.md` | `docs/markdown/07-technology-architecture/deployment/component-inventory.md` | Technology Architecture | Deployed container workload inventory. | No | Container catalog. |
| 56 | `docs/deployment/kubernetes-workloads.md`| `docs/markdown/07-technology-architecture/deployment/kubernetes-workloads.md`| Technology Architecture | Kubernetes StatefulSets, Deployments, and Services. | No | Workload descriptors. |
| 57 | `docs/deployment/microk8s-reference-guide.md`| `docs/markdown/07-technology-architecture/deployment/microk8s-reference-guide.md`| Technology Architecture | Detailed MicroK8s administrator guide. | No | MicroK8s guide. |
| 58 | `docs/deployment/microk8s.md` | `docs/markdown/07-technology-architecture/deployment/microk8s-setup.md` | Technology Architecture | MicroK8s installation & setup walkthrough. | Yes | Differentiate from reference guide. |
| 59 | `docs/deployment/normal-deployment.md` | `docs/markdown/07-technology-architecture/deployment/normal-deployment.md` | Technology Architecture | Normal cluster deployment runbook. | No | Production deployment. |
| 60 | `docs/deployment/prerequisites.md` | `docs/markdown/07-technology-architecture/deployment/prerequisites.md` | Technology Architecture | Host and software prerequisites. | No | Hardware/software setup. |
| 61 | `docs/deployment/shutdown.md` | `docs/markdown/07-technology-architecture/deployment/shutdown.md` | Technology Architecture | Graceful shutdown runbook. | No | Operational shutdown. |
| 62 | `docs/deployment/startup.md` | `docs/markdown/07-technology-architecture/deployment/startup.md` | Technology Architecture | Dependency-ordered startup sequence. | No | Cluster bootstrap. |
| 63 | `docs/deployment/ubuntu.md` | `docs/markdown/07-technology-architecture/deployment/ubuntu.md` | Technology Architecture | Ubuntu host OS configuration and tuning. | No | Host OS preparation. |
| 64 | `docs/deployment/undeploy.md` | `docs/markdown/07-technology-architecture/deployment/undeploy.md` | Technology Architecture | Teardown and cleanup runbook. | No | Cluster de-provisioning. |
| 65 | `docs/deployment/upgrade.md` | `docs/markdown/07-technology-architecture/deployment/upgrade.md` | Technology Architecture | Rolling upgrade and version migration runbook. | No | Migration runbook. |
| 66 | `docs/deployment/verification.md` | `docs/markdown/07-technology-architecture/deployment/verification.md` | Technology Architecture | Cluster health verification checklist. | No | Smoke test checklist. |
| 67 | `docs/design/governed-write-concurrency-contract.md`| `docs/markdown/05-information-architecture/governed-write-concurrency-contract.md`| Information / Data | Governed write concurrency contract (ADR-018/019). | No | Authoritative design contract. |
| 68 | `docs/getting-started/architecture-at-a-glance.md`| `docs/markdown/01-motivation-strategy/architecture-at-a-glance.md`| Motivation & Strategy | High-level visual orientation. | No | System-level overview. |
| 69 | `docs/getting-started/build.md` | `docs/markdown/11-implementation-migration/build.md` | Implementation / Migration | Maven and npm build instructions. | No | Developer build guide. |
| 70 | `docs/getting-started/first-deployment.md`| `docs/markdown/11-implementation-migration/first-deployment.md` | Implementation / Migration | Local developer quickstart deployment. | No | Developer tutorial. |
| 71 | `docs/getting-started/introduction.md`| `docs/markdown/01-motivation-strategy/healthcare-context.md` | Motivation & Strategy | Healthcare integration context and challenge. | Yes | Clarify healthcare context focus. |
| 72 | `docs/getting-started/terminology.md`| `docs/markdown/01-motivation-strategy/terminology-etymology.md` | Motivation & Strategy | Greek naming taxonomy and glossary. | Yes | Clarify etymology content. |
| 73 | `docs/implementation/harmonia-convergence-runtime-integration-plan.md`| `docs/markdown/11-implementation-migration/harmonia-convergence-runtime-integration-plan.md`| Implementation / Migration | Master convergence implementation plan. | No | Authoritative convergence plan. |
| 74 | `docs/integration/correlation.md` | `docs/markdown/06-integration-architecture/correlation.md` | Integration Architecture | Message correlation, causation, and tracing. | No | Tracing specifications. |
| 75 | `docs/integration/error-handling.md` | `docs/markdown/06-integration-architecture/error-handling.md` | Integration Architecture | Integration error handling and NACK mappings. | No | Gateway error translation. |
| 76 | `docs/integration/fhir.md` | `docs/markdown/06-integration-architecture/protocols/fhir-rest.md` | Integration Architecture | FHIR R5 REST API interactions. | Yes | Explicit protocol subfolder. |
| 77 | `docs/integration/hl7-v2.md` | `docs/markdown/06-integration-architecture/protocols/hl7-v2.md` | Integration Architecture | HL7 v2.4/v2.5 trigger event formats. | Yes | Explicit protocol subfolder. |
| 78 | `docs/integration/messaging.md` | `docs/markdown/06-integration-architecture/messaging.md` | Integration Architecture | Petasos messaging patterns and queues. | No | Queue topologies. |
| 79 | `docs/integration/overview.md` | `docs/markdown/06-integration-architecture/overview.md` | Integration Architecture | Gateway and integration tier overview. | No | Integration tier summary. |
| 80 | `docs/integration/pylai.md` | `docs/markdown/06-integration-architecture/pylai-gateway-architecture.md`| Integration Architecture | Pylai boundary and membrane specifications. | Yes | Disambiguate from module guide. |
| 81 | `docs/integration/rest.md` | `docs/markdown/06-integration-architecture/protocols/internal-rest.md` | Integration Architecture | Internal microservice REST APIs. | Yes | Differentiate from external FHIR REST. |
| 82 | `docs/latex/README.md` | `docs/latex/README.md` | Reference / Supporting | LaTeX build guide remains in `latex/` tree. | No | Excluded from `markdown/` hierarchy. |
| 83 | `docs/libreoffice/README.md` | `docs/odt/README.md` | Reference / Supporting | ODT generator guide relocated to `odt/` tree. | No | Updated for `docs/odt/` path. |
| 84 | `docs/middleware/activeMQ-artemis.md`| `docs/markdown/07-technology-architecture/middleware/activemq-artemis.md`| Technology Architecture | ActiveMQ Artemis messaging engine specs. | Yes | Normalize filename casing to lowercase. |
| 85 | `docs/middleware/hapi-fhir.md` | `docs/markdown/07-technology-architecture/middleware/hapi-fhir.md` | Technology Architecture | HAPI FHIR JPA server specifications. | No | Middleware specification. |
| 86 | `docs/middleware/infinispan.md` | `docs/markdown/07-technology-architecture/middleware/infinispan.md` | Technology Architecture | Infinispan clustered caching engine. | No | Middleware specification. |
| 87 | `docs/middleware/kubernetes.md` | `docs/markdown/07-technology-architecture/middleware/kubernetes.md` | Technology Architecture | Kubernetes container orchestration engine. | No | Middleware specification. |
| 88 | `docs/middleware/matrix-synapse.md` | `docs/markdown/07-technology-architecture/middleware/matrix-synapse.md` | Technology Architecture | Matrix Synapse collaboration server. | No | Middleware specification. |
| 89 | `docs/middleware/overview.md` | `docs/markdown/07-technology-architecture/middleware/overview.md` | Technology Architecture | Middleware stack overview. | No | Middleware tier summary. |
| 90 | `docs/middleware/postgresql.md` | `docs/markdown/07-technology-architecture/middleware/postgresql.md` | Technology Architecture | PostgreSQL relational database specifications. | No | Middleware specification. |
| 91 | `docs/middleware/wildfly.md` | `docs/markdown/07-technology-architecture/middleware/wildfly.md` | Technology Architecture | WildFly Jakarta EE 10 application server. | No | Middleware specification. |
| 92 | `docs/modules/agora.md` | `docs/markdown/04-application-architecture/subsystems/agora.md` | Application Architecture | Agora collaboration subsystem implementation. | Yes | Place in `subsystems/` branch. |
| 93 | `docs/modules/calliope.md` | `docs/markdown/04-application-architecture/subsystems/calliope.md` | Application Architecture | Calliope canonical modeling subsystem. | Yes | Place in `subsystems/` branch. |
| 94 | `docs/modules/energeia.md` | `docs/markdown/04-application-architecture/subsystems/energeia.md` | Application Architecture | Energeia workflow subsystem implementation. | Yes | Place in `subsystems/` branch. |
| 95 | `docs/modules/hestia.md` | `docs/markdown/04-application-architecture/subsystems/hestia.md` | Application Architecture | Hestia persistence & cache subsystem. | Yes | Place in `subsystems/` branch. |
| 96 | `docs/modules/iris.md` | `docs/markdown/04-application-architecture/subsystems/iris.md` | Application Architecture | Iris presentation subsystem implementation. | Yes | Place in `subsystems/` branch. |
| 97 | `docs/modules/paradeigma.md` | `docs/markdown/10-simulation-testing/paradeigma-module.md` | Simulation / Testing | Paradeigma simulation subsystem implementation. | Yes | Relocate to simulation branch. |
| 98 | `docs/modules/petasos.md` | `docs/markdown/04-application-architecture/subsystems/petasos.md` | Application Architecture | Petasos messaging subsystem implementation. | Yes | Place in `subsystems/` branch. |
| 99 | `docs/modules/pylai.md` | `docs/markdown/04-application-architecture/subsystems/pylai.md` | Application Architecture | Pylai gateway subsystem implementation. | Yes | Place in `subsystems/` branch. |
| 100 | `docs/modules/themis.md` | `docs/markdown/08-security-architecture/themis-subsystem.md` | Security Architecture | Themis security subsystem implementation. | Yes | Relocate to security branch. |
| 101 | `docs/operations/health-readiness.md`| `docs/markdown/07-technology-architecture/deployment/health-readiness.md`| Technology Architecture | Liveness/readiness container probe specs. | Yes | Group with deployment runbooks. |
| 102 | `docs/operations/phi-sanitized-logging.md`| `docs/markdown/08-security-architecture/phi-sanitized-logging.md` | Security Architecture | Zero-PHI log masking operational runbook. | Yes | Relocate to security branch. |
| 103 | `docs/operations/verification-runbook.md`| `docs/markdown/09-resilience-operations/verification-runbook.md` | Resilience / Operations | Platform operational verification procedures. | Yes | Group with operational runbooks. |
| 104 | `docs/paradeigma/architecture.md` | `docs/markdown/10-simulation-testing/architecture.md` | Simulation / Testing | Paradeigma synthetic simulation architecture. | No | Simulation architecture. |
| 105 | `docs/paradeigma/concepts.md` | `docs/markdown/10-simulation-testing/concepts.md` | Simulation / Testing | Conceptual foundations of clinical simulation. | No | Simulation concepts. |
| 106 | `docs/paradeigma/configuration.md`| `docs/markdown/10-simulation-testing/configuration.md`| Simulation / Testing | Simulator scenario configuration properties. | No | Simulator configuration. |
| 107 | `docs/paradeigma/deployment-guide.md`| `docs/markdown/10-simulation-testing/deployment-guide.md`| Simulation / Testing | Standalone deployment guide for simulators. | No | Simulator deployment. |
| 108 | `docs/paradeigma/deployment.md` | `docs/markdown/10-simulation-testing/deployment-topology.md`| Simulation / Testing | Simulator deployment topology and containers. | Yes | Disambiguate from deployment-guide. |
| 109 | `docs/paradeigma/examples.md` | `docs/markdown/10-simulation-testing/examples.md` | Simulation / Testing | Sample clinical exchange traces and payloads. | No | Sample payloads. |
| 110 | `docs/paradeigma/failure-injection.md`| `docs/markdown/10-simulation-testing/failure-injection.md`| Simulation / Testing | Fault injection testing framework. | No | Chaos/fault injection. |
| 111 | `docs/paradeigma/isolation-invariants.md`| `docs/markdown/10-simulation-testing/isolation-invariants.md`| Simulation / Testing | Invariant 1: Paradeigma isolation rules. | No | Production isolation rules. |
| 112 | `docs/paradeigma/logging-validation.md`| `docs/markdown/10-simulation-testing/logging-validation.md`| Simulation / Testing | Validating simulator log sanitization. | No | Simulator log checks. |
| 113 | `docs/paradeigma/normal-vs-paradeigma.md`| `docs/markdown/10-simulation-testing/normal-vs-paradeigma.md`| Simulation / Testing | Comparison of production vs simulated flows. | No | Comparative analysis. |
| 114 | `docs/paradeigma/overview.md` | `docs/markdown/10-simulation-testing/overview.md` | Simulation / Testing | Paradeigma simulation subsystem tour. | No | Simulation tour. |
| 115 | `docs/paradeigma/production-isolation.md`| `docs/markdown/10-simulation-testing/production-isolation.md`| Simulation / Testing | Production isolation architecture and proofs. | No | Consolidate with isolation-invariants. |
| 116 | `docs/paradeigma/scenarios.md` | `docs/markdown/10-simulation-testing/scenarios.md` | Simulation / Testing | Synthetic clinical scenario catalogue. | No | ADT, ORM, ORU scenarios. |
| 117 | `docs/paradeigma/security-testing.md`| `docs/markdown/10-simulation-testing/security-testing.md`| Simulation / Testing | Simulated security policy testing. | No | Security test fixtures. |
| 118 | `docs/paradeigma/simulation-framework.md`| `docs/markdown/10-simulation-testing/simulation-framework.md`| Simulation / Testing | Scenario generation engine framework. | No | Framework architecture. |
| 119 | `docs/paradeigma/synthetic-data.md` | `docs/markdown/10-simulation-testing/synthetic-data.md` | Simulation / Testing | Deterministic patient generator algorithms. | No | Data generator rules. |
| 120 | `docs/provider-registry/architecture.md`| `docs/markdown/04-application-architecture/solutions/provider-registry/architecture.md`| Application (Solution) | Local Directory Solution architecture. | No | Solution architecture. |
| 121 | `docs/provider-registry/audit-provenance.md`| `docs/markdown/08-security-architecture/provider-audit-provenance.md`| Security Architecture | Provider change audit and Kleio provenance. | Yes | Relocate to security branch. |
| 122 | `docs/provider-registry/change-processing.md`| `docs/markdown/04-application-architecture/solutions/provider-registry/change-processing.md`| Application (Solution) | Provider change request state machine. | No | Solution workflow. |
| 123 | `docs/provider-registry/failure-recovery.md`| `docs/markdown/09-resilience-operations/provider-registry-recovery.md`| Resilience / Operations | Recovery procedures for Provider Registry. | Yes | Relocate to resilience branch. |
| 124 | `docs/provider-registry/fhir-api.md` | `docs/markdown/06-integration-architecture/protocols/provider-registry-fhir-api.md`| Integration Architecture | Provider Registry FHIR REST endpoints. | Yes | Group with protocol specifications. |
| 125 | `docs/provider-registry/persistence.md`| `docs/markdown/05-information-architecture/provider-registry-persistence.md`| Information / Data | Database tables and JPA mappings for registry. | Yes | Relocate to information branch. |
| 126 | `docs/provider-registry/resource-model.md`| `docs/markdown/05-information-architecture/provider-registry-resource-model.md`| Information / Data | Provider Registry FHIR resource relations. | Yes | Relocate to information branch. |
| 127 | `docs/provider-registry/search.md` | `docs/markdown/04-application-architecture/solutions/provider-registry/search.md`| Application (Solution) | Authoritative database-backed provider search. | No | Solution search feature. |
| 128 | `docs/provider-registry/security.md`| `docs/markdown/08-security-architecture/boundary-enforcement/provider-registry-security.md`| Security Architecture | Domain-specific security labels and RBAC. | Yes | Relocate to security branch. |
| 129 | `docs/provider-registry/testing.md` | `docs/markdown/10-simulation-testing/provider-registry-testing.md`| Simulation / Testing | Provider Registry test suite specifications. | Yes | Relocate to testing branch. |
| 130 | `docs/provider-registry/validation.md`| `docs/markdown/04-application-architecture/solutions/provider-registry/validation.md`| Application (Solution) | Structural and business rule validation. | No | Solution validation rules. |
| 131 | `docs/reference/configuration-register.md`| `docs/markdown/07-technology-architecture/configuration/configuration-register.md`| Technology Architecture | Reference configuration register. | No | Duplicate to be consolidated. |
| 132 | `docs/reference/port-protocol-register.md`| `docs/markdown/07-technology-architecture/network/port-protocol-register.md`| Technology Architecture | Canonical network listeners register. | No | Primary authoritative port register. |
| 133 | `docs/security/architecture.md` | `docs/markdown/08-security-architecture/overview.md` | Security Architecture | Security architecture overview and Themis framework.| Yes | Security overview. |
| 134 | `docs/security/audit.md` | `docs/markdown/08-security-architecture/audit.md` | Security Architecture | Non-PHI security audit logging. | No | Audit stream specification. |
| 135 | `docs/security/ergon-security.md` | `docs/markdown/08-security-architecture/boundary-enforcement/ergon-security.md`| Security Architecture | Ergon activity security context evaluation. | Yes | Boundary enforcement branch. |
| 136 | `docs/security/failure-behaviour.md`| `docs/markdown/08-security-architecture/failure-behaviour.md`| Security Architecture | Fail-closed security failure semantics. | No | Failure semantics. |
| 137 | `docs/security/logging.md` | `docs/markdown/08-security-architecture/phi-sanitized-logging.md`| Security Architecture | PHI-aware logging policy and PhiLogger API. | Yes | Overlaps with operational logging. |
| 138 | `docs/security/policy-model.md` | `docs/markdown/08-security-architecture/policy-model.md` | Security Architecture | Themis policy evaluation & precedence rules. | No | Deterministic policy rules. |
| 139 | `docs/security/ponos-security.md` | `docs/markdown/08-security-architecture/boundary-enforcement/ponos-security.md`| Security Architecture | Ponos workflow execution authorization. | Yes | Boundary enforcement branch. |
| 140 | `docs/security/pragma-security.md` | `docs/markdown/08-security-architecture/boundary-enforcement/pragma-security.md`| Security Architecture | Pragma security metadata and tokens. | Yes | Boundary enforcement branch. |
| 141 | `docs/security/principals.md` | `docs/markdown/08-security-architecture/principals.md` | Security Architecture | Security principals and identity classification. | No | Identity taxonomy. |
| 142 | `docs/security/provider-registry-security.md`| `docs/markdown/08-security-architecture/boundary-enforcement/provider-registry-security.md`| Security Architecture | Provider Registry security enforcement. | Yes | Duplicate of `provider-registry/security.md`. |
| 143 | `docs/security/pylai-security.md` | `docs/markdown/08-security-architecture/boundary-enforcement/pylai-security.md`| Security Architecture | Gateway ingress/egress security interceptors. | Yes | Boundary enforcement branch. |
| 144 | `docs/security/roles-authorities.md`| `docs/markdown/08-security-architecture/roles-authorities.md` | Security Architecture | Mnemonic roles to granular authorities mapping. | No | Role-to-authority matrix. |
| 145 | `docs/security/security-gaps.md` | `docs/markdown/02-principles-governance/security-gaps.md` | Architecture Governance | Security gap audit and exception register. | Yes | Relocate to governance branch. |
| 146 | `docs/security/security-labels.md` | `docs/markdown/08-security-architecture/security-labels.md` | Security Architecture | FHIR security labels and metadata serialization.| No | Security labels mapping. |
| 147 | `docs/security/service-identities.md`| `docs/markdown/08-security-architecture/service-identities.md`| Security Architecture | Internal platform service identities. | No | Service accounts catalog. |
| 148 | `docs/security/testing.md` | `docs/markdown/08-security-architecture/security-testing.md` | Security Architecture | Automated security and ArchUnit tests. | Yes | Security test specifications. |
| 149 | `docs/security/themis.md` | `docs/markdown/08-security-architecture/themis-engine.md` | Security Architecture | Themis core service implementation contracts. | Yes | Disambiguate from subsystem doc. |
| 150 | `docs/security/threat-model.md` | `docs/markdown/08-security-architecture/threat-model.md` | Security Architecture | STRIDE threat model and risk mitigations. | No | Formal threat model. |

---

### Non-Markdown Migration Mapping

| Asset Path | Proposed Path | Target Format | Action / Handling |
| :--- | :--- | :--- | :--- |
| `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx` | `docs/markdown/01-motivation-strategy/harmonia-strategy-local-directory.md` | Markdown (`.md`) | Convert content to Markdown under Motivation/Strategy; retain original DOCX in `docs/odt/` or an archive store for historical provenance. |
| `docs/libreoffice/` (All assets) | `docs/odt/` | OpenDocument (`.odt`) | Rename directory `docs/libreoffice/` to `docs/odt/`; update `generate_odt.py` script constants (`LIBREOFFICE_DIR` -> `ODT_DIR`). |
| `docs/latex/` (All assets) | `docs/latex/` | LaTeX (`.tex`) | Retain top-level location; maintain clean format separation alongside `docs/markdown/` and `docs/odt/`. |

# Issues & Risk Assessment

### Structural Problems Identified

During our systematic inventory and content inspection of `./docs`, the following architectural and documentation defects were identified:

#### 1. Direct Duplication & Content Overlap
- **Network Port Registers**:
  - Found in **three distinct locations**: `docs/configuration/ports-and-protocols.md`, `docs/architecture/port-protocol-register.md`, and `docs/reference/port-protocol-register.md`.
  - All three documents enumerate the same 18 platform network listeners with nearly identical Markdown tables.
  - *Recommendation*: Establish `docs/markdown/07-technology-architecture/network/port-protocol-register.md` as the single authoritative register; consolidate other references.
- **Configuration Registers**:
  - Found in `docs/configuration/configuration-register.md` and `docs/reference/configuration-register.md`.
  - *Recommendation*: Retain a single canonical register in `07-technology-architecture/configuration/`.
- **Failure Recovery Specifications**:
  - Found in `docs/failure-recovery.md` and `docs/architecture/failure-recovery.md`. Both contain the subsystem failure matrix, 4-tier idempotency taxonomy, MLLP ACK codes (AA/AE/AR), and DLQ configuration.
  - *Recommendation*: Consolidate into `docs/markdown/09-resilience-operations/failure-recovery.md`.
- **Agent Guardrails**:
  - Found at repository root `/AGENTS.md` and duplicated at `docs/AGENTS.md`.
  - *Recommendation*: Maintain root `/AGENTS.md` as the primary agent instruction file, symlinked or mirrored to `docs/markdown/02-principles-governance/agent-guardrails.md`.

#### 2. Relational Schema Divergence & Obsolete Snippets
- In `docs/architecture/persistence-lifecycle.md` (lines 78–87), the DDL for `hie_fhir_resources` is defined as:
  ```sql
  CREATE TABLE hie_fhir_resources (
      res_id VARCHAR(64) NOT NULL,
      res_type VARCHAR(64) NOT NULL,
      res_version BIGINT NOT NULL,
      res_text_r5 TEXT NOT NULL,
      res_updated TIMESTAMP WITH TIME ZONE NOT NULL,
      res_deleted BOOLEAN NOT NULL DEFAULT FALSE,
      security_labels VARCHAR(255),
      PRIMARY KEY (res_type, res_id, res_version)
  );
  ```
- In contrast, the authoritative DDL in `docs/database-schema.md` (lines 84–93) and `docs/persistence-architecture.md` (lines 99–105) reflects the active Spring Boot JPA entity (`FhirResourceEntity`):
  ```sql
  CREATE TABLE hie_fhir_resources (
      id BIGSERIAL PRIMARY KEY,
      resource_type VARCHAR(64) NOT NULL,
      fhir_id VARCHAR(128) NOT NULL,
      version_id BIGINT NOT NULL DEFAULT 1,
      resource_json TEXT NOT NULL,
      is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
      last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
      CONSTRAINT uk_resource_type_fhir_id UNIQUE (resource_type, fhir_id)
  );
  ```
- *Severity*: **High**. The schema in `docs/architecture/persistence-lifecycle.md` is obsolete and contradicts active code (`FhirResourceEntity.java`). Developers or agents relying on `persistence-lifecycle.md` will generate broken queries or test assertions.
- *Recommendation*: Update `persistence-lifecycle.md` to reference `database-schema.md` as the single authoritative source of truth for physical relational DDL.

#### 3. Dual Classification Confusion (`concepts/` vs `modules/`)
- Currently, `docs/concepts/` (22 files) and `docs/modules/` (9 files) parallel each other without clear distinction:
  - `docs/concepts/petasos.md` vs `docs/modules/petasos.md`
  - `docs/concepts/calliope.md` vs `docs/modules/calliope.md`
  - `docs/concepts/themis.md` vs `docs/modules/themis.md`
  - `docs/concepts/energeia.md` vs `docs/modules/energeia.md`
  - In addition, `docs/concepts/` contains internal duplicate pairs: `calliope.md` & `calliope-canonical.md`, `energeia.md` & `energeia-workflow.md`, `petasos.md` & `petasos-messaging.md`, `pylai.md` & `pylai-gateways.md`, `themis.md` & `themis-security.md`.
- *Recommendation*: In `04-application-architecture/`, partition into:
  - `subsystems/`: Complete engineering documentation for each of the 9 core modules.
  - `concepts/`: Domain concepts (`ponos`, `ergon`, `praxis`, `pragma`, `mneme`, `mnemosyne`).
  - Merge the duplicate concept pairs into their respective subsystem guides during post-migration cleanup.

#### 4. Filename Inconsistencies & Spacing Anomalies
- `docs/backlog/Harmonia - Task 9 - Backlog.md` contains spaces and capital letters, causing CLI and link-generation friction.
- `docs/middleware/activeMQ-artemis.md` contains mixed-case (`activeMQ`), breaking the lowercase kebab-case convention used by all other files in that directory.
- *Recommendation*: Recommend renaming to `task-9-backlog.md` and `activemq-artemis.md` during restructuring.

#### 5. Format Isolation Anomaly
- `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx` sits loosely in the root of `docs/`.
- `docs/libreoffice/` uses the application brand name rather than standard format name `odt/`.
- *Recommendation*: Relocate DOCX and ODT into `docs/odt/`, convert the DOCX text into Markdown under `docs/markdown/01-motivation-strategy/`, and rename `docs/libreoffice/` to `docs/odt/`.

---

### Migration Dependencies & Tooling Impacts

| Dependency / Tool | Affected File / Reference | Risk Description | Remediation Required |
| :--- | :--- | :--- | :--- |
| **`AGENTS.md` (Root)** | Lines 34, 418, 422, 447 | Hardcoded paths to `docs/architectural-axioms.md` and `docs/implementation/harmonia-convergence-runtime-integration-plan.md`. | Update paths to `docs/markdown/02-principles-governance/architectural-axioms.md` and `docs/markdown/11-implementation-migration/harmonia-convergence-runtime-integration-plan.md`. |
| **`README.md` (Root)** | Lines 427–440 | 11 direct markdown hyperlinks to legacy documentation paths. | Update all hyperlinks to point to their corresponding targets in `docs/markdown/`. |
| **Python Generator Script** | `docs/libreoffice/scripts/generate_odt.py` | Lines 20–25 hardcode directory names `docs/latex` and `docs/libreoffice`. | Rename `LIBREOFFICE_DIR` variable to `ODT_DIR` and update paths to reflect `docs/odt/`. |
| **Internal Markdown Links** | 14 files across `docs/` | Relative links (e.g. `[Persistence Architecture](persistence-architecture.md)`) will break when files move into nested subdirectories. | Execute an automated link rewriting pass adjusting relative paths based on the migration matrix. |
| **Maven & ArchUnit Tests** | Java test suites | Verified: Zero ArchUnit or unit tests reference `docs/` paths. | No Java code impact. Pure documentation refactor. |

---

### Risk Assessment & Mitigation Matrix

| Risk ID | Identified Risk | Impact | Probability | Mitigation Strategy |
| :---: | :--- | :---: | :---: | :--- |
| **RSK-01** | **Broken Hyperlinks in Root README & AGENTS.md** | High | High | Execute an automated link verification script prior to completing the migration stage; update `AGENTS.md` and `README.md` in the same commit. |
| **RSK-02** | **Broken Internal Cross-References** | Medium | High | Construct an automated sed/regex rewriting script that replaces relative links based on source-target path differentials. |
| **RSK-03** | **Disruption of ODT Specification Generation** | Medium | Medium | Validate that `python3 docs/odt/scripts/generate_odt.py` compiles cleanly after the directory move before finalizing the migration. |
| **RSK-04** | **Merge Conflicts with Concurrent Branches** | Medium | Medium | Announce documentation migration window; perform the physical move as an atomic Git commit with zero text edits (using `git mv`), followed by a separate commit for link fixes. |
| **RSK-05** | **Agent Hallucination of Legacy Paths** | Medium | Medium | Provide temporary symbolic links or forwarding stubs for core documents (`architectural-axioms.md`, `harmonia-convergence-runtime-integration-plan.md`) if necessary during transition. |

# Delivery Steps

###   Step 1: Establish Target Directory Structure and Format Separation
Target directories `docs/markdown/`, `docs/latex/`, and `docs/odt/` exist with supporting non-Markdown formats relocated cleanly.
- Create the target directory skeleton under `docs/markdown/` covering all 11 architectural domains (01-motivation through 11-implementation).
- Standardize the ODT documentation directory by moving `docs/libreoffice/` to `docs/odt/`.
- Convert `docs/Harmonia_Strategy_Local_Directory_and_Broader_Role.docx` into Markdown in `docs/markdown/01-motivation-strategy/` and archive the binary DOCX into `docs/odt/` or a dedicated binary archive.
- Update `docs/odt/scripts/generate_odt.py` paths (`LIBREOFFICE_DIR` -> `ODT_DIR`) and verify ODT build scripts remain functional.

###   Step 2: Migrate Strategy, Governance, and Business Architecture Documents
Foundational architecture governance, axioms, decisions, strategy, and business event documentation are migrated to their target TOGAF domains.
- Move root strategy and context documents into `docs/markdown/01-motivation-strategy/` (`strategy-local-directory.md`, `healthcare-context.md`, `terminology.md`).
- Move authoritative governance files into `docs/markdown/02-principles-governance/` (`architectural-axioms.md`, `architecture-decisions.md`, `agent-guardrails.md`).
- Move domain event catalogues into `docs/markdown/03-business-architecture/` (`patient-identity-events.md`, `provider-management-events.md`).
- Update the root `AGENTS.md` and repository README references pointing to `docs/architectural-axioms.md` and `docs/architecture-decisions.md`.

###   Step 3: Migrate Application, Information, Integration, and Technology Documents
Technical core documentation spanning Application, Information, Integration, and Technology architectures is migrated into dedicated domain hierarchies.
- Relocate subsystem, conceptual, and presentation specs into `docs/markdown/04-application-architecture/` (`subsystems/`, `concepts/`, `presentation/`, `solutions/provider-registry/`).
- Relocate relational schemas, persistence models, and concurrency contracts into `docs/markdown/05-information-architecture/` (`persistence-architecture.md`, `database-schema.md`, `governed-write-concurrency-contract.md`).
- Relocate protocol, messaging, and transaction specifications into `docs/markdown/06-integration-architecture/` (`message-lifecycle.md`, `messaging.md`, `protocols/`).
- Relocate middleware, network registers, configuration reference, and deployment guides into `docs/markdown/07-technology-architecture/` (`middleware/`, `network/`, `configuration/`, `deployment/`).

###   Step 4: Migrate Security, Resilience, Simulation, and Implementation Documents
Cross-cutting security, resilience, operational runbooks, synthetic simulation, and master implementation plans are relocated to their target homes.
- Relocate Themis security guides, policy models, and boundary specifications into `docs/markdown/08-security-architecture/`.
- Relocate high availability, failure recovery matrices, RPO/RTO guarantees, and health runbooks into `docs/markdown/09-resilience-operations/`.
- Relocate Paradeigma isolation invariants, simulation frameworks, and test scenarios into `docs/markdown/10-simulation-testing/`.
- Relocate the master convergence plan, backlog, and build guides into `docs/markdown/11-implementation-migration/`.
- Update `AGENTS.md` references pointing to `docs/implementation/harmonia-convergence-runtime-integration-plan.md`.

###   Step 5: Update Hyperlinks, Tooling Paths, and Final Verification
All internal cross-document hyperlinks, repository README links, and tooling paths are fully validated and resolved with zero broken links.
- Execute an automated link-validation script across all Markdown files under `docs/markdown/` to update relative references (`../`, relative paths).
- Update the documentation catalog in root `README.md` to point to the new `docs/markdown/` locations.
- Verify LaTeX Makefile and `generate_odt.py` execution to confirm build pipeline integrity.
- Deliver the final documentation migration verification report documenting completed transitions and resolved anomalies.