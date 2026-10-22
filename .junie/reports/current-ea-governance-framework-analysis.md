# Current-State Analysis: Harmonia Enterprise Architecture Governance Framework

## Report Provenance and Authority Notice

- **Task-Issued-At**: UNRESOLVED (Preserved under AX-17; no attributable system issuance timestamp was provided for this autonomous analysis task. Unavailable original times are preserved as unresolved rather than inferred from filesystem metadata or clock.)
- **Report-Produced-At**: UNRESOLVED (Preserved under AX-17; unattributable session timestamp.)
- **HCRL Association**: UNRESOLVED (AX-17: No HCRL association was authorised or established for this analysis-only task. The applicability of release increments under `AGENTS.md:184-193` (§1.3) and evidence directory creation under `AGENTS.md:194-210` (§1.4) to analysis-only reporting is preserved as an unresolved matter for Solution Architect review. No Task/Report evidence was written under `enterprise-architecture/design/evidence/` and no release increment was applied to `architecture-release.json`.)
- **Architectural Authority Notice**: This report resides under `.junie/reports/` and constitutes working and investigative analysis. Per `AGENTS.md:656-666` (§6) and `AGENTS.md:56-81` (§1.1), files under `.junie/reports/` and `.junie/plans/` do not possess architectural authority and cannot modify or override repository governance.
- **Inspection Methodology**: All analysis in this report was conducted via read-only static inspection of governance contracts, schemas, Python scripts, Makefiles, and existing evidence JSON artifacts under `enterprise-architecture/design/evidence/1.1.5/002/after/`. No whole-system builds, XeLaTeX compilations, Maven builds, release increments, repository mutations, or actual ArchiMate transport executions were performed. Bounded command execution timeouts (60–120s) were observed for all read-only diagnostic inspections.

---

## 1. Executive Summary

### 1.1 Purpose and Assessment Context

This report establishes an evidence-based, static-inspection current-state analysis of the Harmonia Enterprise Architecture (EA) governance framework. Harmonia is a mission-critical Health Integration Environment (HIE) engineered for resilient, distributed, and deterministic clinical interoperability. Over recent release cycles (culminating in RAD-1.1.5 HCRL-002), Harmonia has undergone an architectural transformation: transitioning from legacy narrative documentation structures (`docs/markdown/`) to an individually represented, machine-actionable Enterprise Architecture Continuum (`enterprise-architecture/continuum/`).

The scope of this investigation comprises a read-only static inspection across:
1. **Normative Governance Contracts**: Authoritative rules, human and agent authority boundaries, release versioning policies, and temporal provenance standards (`enterprise-architecture/governance/`, `AGENTS.md`).
2. **Continuum Representation and Ingestion Machinery**: Canonical Markdown schemas, front-matter metadata, identity allocation mechanics, ArchiMate Open Exchange round-trip tooling, and migration adapters (`scripts/`, `docs/archimate/framework/tools/`).
3. **Assurance, Validation, and Invariant Suites**: Metamodel relationship checkers, containment validators, offline XML schema verification, standing reproduction scripts, and automated ArchUnit software architecture rules (`paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/`).
4. **Publication Engines and Evidence Artifacts**: Open Exchange XML projection (`enterprise-architecture/continuum/documentation/published/1.1.5/Harmonia-Architecture.xml`), interactive zero-dependency HTML browser (`enterprise-architecture/continuum/documentation/published/1.1.5/continuum.html`), LaTeX/PDF identity catalogues (`enterprise-architecture/continuum/documentation/published/1.1.5/continuum-catalogue.pdf`), and empirical metrics recorded under `enterprise-architecture/design/evidence/1.1.5/002/after/`.

In strict adherence to repository guardrails (`AGENTS.md:56-81` §1.1, `AGENTS.md:656-666` §6), this analysis was executed entirely via read-only inspection. No whole-system builds, XeLaTeX compilations, Maven builds, release increments, or repository mutations were performed.

### 1.2 Governance Architecture and Operating Model

Harmonia's governance operating model enforces an explicit hierarchy of design authority:
- **Highest Architectural Authority**: The 18 Architectural Axioms (`enterprise-architecture/governance/architectural-axioms.md:1-26`) establish the foundational design rules for the entire platform. All designs, contracts, implementations, and agent activities are strictly subordinate to these axioms. Existing code does not establish intent over axioms (AX-01).
- **Normative Operational Contracts**: Platform governance, decision lifecycles, provenance obligations, and element incorporation praxes are codified in core contracts: `enterprise-architecture/governance/architecture-governance.md:35,48,344-345`, `enterprise-architecture/governance/architecture-change-provenance.md:140-163`, `enterprise-architecture/governance/architecture-review-praxis.md:170-173`, `enterprise-architecture/governance/continuum-incorporation-praxis.md:32,94-100`, and `enterprise-architecture/governance/continuum-representation.md:133`.
- **Human Authority Boundaries (AX-14 / AX-17)**: The governance framework strictly demarcates human Solution Architect (SA) and Architecture Review Board (ARB) authority from automated AI agent execution. AI agents operate strictly within bounded, authorized directives. Agents cannot confirm architectural closure, manufacture unprompted decisions, or resolve uncertainties through inference. When ambiguity arises, agents are mandated to preserve the uncertainty under AX-17.
- **Decision Review Lifecycle (HCRL)**: Governed change sets progress through a formal Harmonia Change Review Log (HCRL) state machine: candidate directives begin as pending files (with `.pending.md` suffix) in `enterprise-architecture/design/review/`, undergo authorized pre-application disposition by the Solution Architect, application, and verification, and achieve formal closure as completed records (with `.complete.md` suffix) in `enterprise-architecture/design/register/` upon attributable SA confirmation.
- **Atomic Release Versioning**: Every accepted repository modification applies a patch increment to the root authority file `architecture-release.json:1-4` prepared during change application under `docs/markdown/governance/architecture-release-policy.md:54-58`.

### 1.3 Core Tooling, Ingestion, and Publication Pipeline

The EA Continuum currently models **1,602 individually represented elements**, **1,651 native relationships**, **462 qualified source assertions**, and **1,209 diagram Views** across four ArchiMate layers (`enterprise-architecture/continuum/business/`, `enterprise-architecture/continuum/application/`, `enterprise-architecture/continuum/motivation/`, and `enterprise-architecture/continuum/strategy/`):
- **Canonical Representation**: Each architectural element is represented as an individual Markdown file with strict YAML/JSON front-matter conforming to `enterprise-architecture/governance/continuum/element.schema.json` (15 mandatory metadata properties).
- **Canonical Identity Allocation & Derived Exchange IDs**: Canonical identity is allocated as UUIDv4 exclusively during explicitly authorised creation or ingestion operations (`enterprise-architecture/governance/continuum-incorporation-praxis.md:32`, `enterprise-architecture/governance/continuum-representation.md:133`, `scripts/architecture_continuum.py:442`). Derived Open Exchange identifiers are computed deterministically via UUIDv5 exclusively upon new element allocation (`scripts/architecture_continuum.py:440, 445`). Downstream Open Exchange projection (`scripts/architecture_continuum.py:330`) emits stored exchange identifiers directly from front-matter (`m['exchange']['identifier']`); 1,378 of the 1,602 migrated elements retain historical representation identifiers without dynamic recomputation. Staged workbench ingestion and validation tools strictly preserve existing identities.
- **Multi-Channel Publication Projections**:
  1. *Review Manifest*: `scripts/continuum_manifest.py` scans element front-matters to project `enterprise-architecture/continuum/MANIFEST.md` (302,408 bytes).
  2. *Open Exchange XML*: `scripts/architecture_continuum.py:320-418` projects `enterprise-architecture/continuum/documentation/published/1.1.5/Harmonia-Architecture.xml` (25,163,803 bytes / 25.16 MB) driven by `enterprise-architecture/design/archimate/projection-template.xml` (10,007,373 bytes / 10.0 MB), retaining 1,209 visual diagram Views.
  3. *Interactive HTML Architecture Browser*: `scripts/publish_continuum.py:20-80` generates `enterprise-architecture/continuum/documentation/published/1.1.5/continuum.html` (10,043,810 bytes), embedding the complete continuum corpus as a client-side JSON dataset with instant search, facet filtering, and cross-element navigation (`scripts/publish_continuum.py:29-34`).
  4. *Standing Identity Catalogue (LaTeX/PDF)*: `scripts/publish_continuum.py:44-78` generates `enterprise-architecture/continuum/documentation/published/1.1.5/continuum-catalogue.tex` (780,708 bytes), supporting optional compilation into a 261-page PDF catalogue (`enterprise-architecture/continuum/documentation/published/1.1.5/continuum-catalogue.pdf`, 649,557 bytes) for human review.

### 1.4 Assurance Framework and Invariant Verification

Assurance within Harmonia is structured as a five-tier multi-layered framework:
1. **Tier 1 (Canonical Front-Matter & Structure)**: Enforces schema validity against `enterprise-architecture/governance/continuum/element.schema.json`, matching display names, and mandatory architectural content sections (`scripts/architecture_continuum.py:60-140`).
2. **Tier 2 (Metamodel Semantics & Containment)**: Validates endpoint compatibility against `enterprise-architecture/governance/continuum/type-catalogue.json` and `enterprise-architecture/governance/continuum/native-relationships.json`, enforcing containment hierarchy rules and rejecting circular parentage (`docs/archimate/framework/tools/containment.py`).
3. **Tier 3 (Open Exchange XML Schema Compliance)**: Validates projected XML offline against ArchiMate 3.0 Open Exchange standard XSD schemas (`docs/archimate/framework/schemas/archimate3_Diagram.xsd`, `docs/archimate/framework/schemas/archimate3_View.xsd`, `docs/archimate/framework/schemas/archimate3_Model.xsd`).
4. **Tier 4 (Standing Quality Checkers)**: Validates clean-room build reproducibility (`scripts/verify_continuum_reproducibility.py`), checks 1,499 internal Markdown relative links (`scripts/verify_continuum_links.py`), and verifies source succession preservation across 855 retired sections (`scripts/verify_continuum_sources.py`).
5. **Tier 5 (Automated Software Architecture Suite / ArchUnit)**: 11 test classes comprising 90 tests located in `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/` continuously enforce key architectural invariants:
   - *Invariant 1 (Paradeigma Isolation)*: Zero production POM dependencies, Java imports, or simulation flags across all modules.
   - *Invariant 2 (Petasos API Abstraction)*: Zero JMS/Artemis API leakage into `petasos-api`.
   - *Invariant 3 (Iris Presentation Decoupling)*: Zero JPA/Hibernate/PostgreSQL dependencies in Iris SPAs and BEFE gateway.
   - *Invariant 3 (Provider Registry Isolation)*: Four distinct server-side directories (`pylai/pylai-fhir-registry`, `energeia/erga`, `themis/themis-core`, `hestia/mnemosyne-clinical`) decoupled from UI components.
   - *Invariant 6 (Default-Deny Security)*: Pure Themis security context propagation and default-deny policy evaluation.
   - *Invariant 8 (Mneme/Mnemosyne Persistence Separation)*: Pure domain port contracts, zero Hot Rod / Infinispan dependencies in Mnemosyne, and elimination of physical DELETE/REMOVE methods.
   - *Invariant 9 & 10 (Pylai Interoperability Boundary & Agora Matrix Isolation)*: Non-destructive external projection and complete encapsulation of Matrix DTOs.

### 1.5 Key Coupling Hotspots, Technical Debt, and Governance Ambiguities

Static inspection identified several structural coupling hotspots, legacy artifacts, and governance ambiguities requiring architectural attention:
- **Tooling Coupling (`F-13-01`, `F-13-02`, `F-13-03`)**: Active Continuum scripts in `scripts/` (`scripts/architecture_continuum.py`, `scripts/continuum_workbench.py`, `scripts/architecture_identity.py`) dynamically inject `sys.path` to import legacy modules from `docs/archimate/framework/tools/` (`docs/archimate/framework/tools/containment.py`, `docs/archimate/framework/tools/workspace_paths.py`, `docs/archimate/framework/tools/information_naming.py`). Furthermore, active workbench review tooling directly imports legacy round-trip modules (`docs/archimate/framework/tools/change_review.py`).
- **Coexistence of Active and Superseded Governance Documentation (`F-14-04`, `F-14-05`)**: Active governance contracts (`docs/markdown/governance/architecture-release-policy.md` and `docs/markdown/governance/archimate-round-trip-engineering-praxis.md`) reside under legacy documentation directories, co-located alongside superseded historical copies wrapped in section-level supersession markers.
- **Pending Holistic Acceptance of Continuum Model (`F-14-02`)**: While the Continuum operates prospectively as the canonical source for individually represented elements under `AGENTS.md:84-125` (§1.2), formal holistic acceptance remains pending in `enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md:6,9`.
- **Axioms Authority Referent Divergence (`F-14-01`)**: The 18 Architectural Axioms exist simultaneously as narrative Markdown (`enterprise-architecture/governance/architectural-axioms.md:1-26`), rule clauses in `AGENTS.md:28-38`, and 18 Continuum Principle elements (`enterprise-architecture/continuum/motivation/principle/`).
- **Evidence Log Path Uncertainty & Historical Test Failures (`F-14-09`, `F-14-10`)**: Historical evidence summary JSON (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json`) references non-existent `"validation/"` log paths, and records a 10-error failure across 251 tests in the historical framework test suite at task entry that was preserved and not rerun in subsequent rounds.

### 1.6 Computational Scope and Cost Profiles

Analysis of empirical evidence recorded in `enterprise-architecture/design/evidence/1.1.5/002/after/` reveals distinct cost profiles across different operational scopes:
- **Empirical Execution Benchmarks**:
  - Continuum Unit Test Suite (20 tests in `scripts/tests/test_continuum.py`): **97.215 seconds** (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:3-10`). (Full `make all` wall-clock time was unrecorded and remains UNRESOLVED).
  - ArchUnit Architecture Test Suite (90 tests): **18.096 seconds** Maven reactor total time (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation/architecture-tests.txt:815`).
  - XeLaTeX PDF Catalogue Compilation: 261 pages compiled via XeLaTeX (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-pdf-validation.json:1-8`).
- **Documented Per-Change Obligations vs Automatic Triggers**: Governance contracts (`enterprise-architecture/governance/continuum-incorporation-praxis.md:94-100`, `AGENTS.md:559-561`) establish documented obligations for contributors to execute validation, clean-room reproducibility, and ArchUnit test runs before accepting changes. No automatic code hook or filesystem trigger executes these cascades on raw file writes.
- **Migration-Specific Suites vs Routine Changes**: Costly historical validation suites (such as the 420-second historical framework suite or whole-model Archi interchange verification) belong to migration milestones and are not required for routine incremental element changes.

### 1.7 Structure of this Report

This report is organized into 16 structured sections:
- **Sections 2–6**: Current operating model, recognized entry points, major governance components, invocation flows, and architecture mutation paths.
- **Sections 7–10**: Validation suites, evidence/provenance contracts, publication engines, and human/agent decision boundaries.
- **Sections 11–14**: Computational cost profiles, migration machinery, duplication/coupling analysis, and observed governance ambiguities.
- **Section 15**: Comprehensive Current-State Governance Map (component taxonomy, ASCII flow diagrams, invocation matrix, and boundary mappings).
- **Section 16**: Catalog of neutral findings and strategic trade-offs for Solution Architect review.

---

## 2. Current Governance Operating Model

### 2.1 Normative Authority Scope and Hierarchy

The Harmonia governance framework defines rules governing architectural authority, decision records, design contracts, and implementation sequencing.

#### Documented Hierarchy for Junie Plans
`AGENTS.md:668-688` (§6.1) defines the explicit authority hierarchy against which Junie plans must be interpreted [Documented]:

> 1. `enterprise-architecture/governance/architectural-axioms.md`
> 2. this `AGENTS.md`
> 3. applicable accepted Architecture Decision Records
> 4. applicable requirements and design contracts
> 5. `docs/implementation/harmonia-convergence-runtime-integration-plan.md` for convergence/runtime implementation sequencing
> 6. the task-specific Junie plan

The convergence/runtime implementation plan (`docs/implementation/harmonia-convergence-runtime-integration-plan.md:1-85`) is authoritative for the order and scope of implementation activities, but does not override higher-level authorities (`AGENTS.md:680-683`).

#### Structural Authority Relationships and Unordered Boundaries
While §6.1 scopes the hierarchy to the interpretation of Junie plans, broader repository authority relationships exhibit specific structural mappings and boundaries [Inferred]:
- **ADRs and the HCRL Register**: In repository practice, Architecture Decision Records are operationalised through completed HCRL records located in `enterprise-architecture/design/register/` (e.g., `enterprise-architecture/design/register/1.1.4/hcrl-1.1.4-004.complete.md`) [Inferred].
- **Requirements, Design Contracts, and Governance Contracts**: Core normative governance contracts reside under `enterprise-architecture/governance/` (`enterprise-architecture/governance/architecture-governance.md`, `enterprise-architecture/governance/architecture-change-provenance.md`, `enterprise-architecture/governance/architecture-review-praxis.md`, `enterprise-architecture/governance/continuum-representation.md`, `enterprise-architecture/governance/continuum-incorporation-praxis.md`). Per `AGENTS.md:106-109`, `enterprise-architecture/governance/architecture-governance.md` and `enterprise-architecture/governance/architecture-change-provenance.md` are the adopted common authoritative contracts [Documented].
- **Unresolved Inter-Contract Precedence**: No governing source specifies a total ordering between completed HCRL register entries and normative governance contracts. Governance contracts were themselves adopted through completed HCRL transitions (`enterprise-architecture/governance/architecture-governance.md:10-36`), and completed HCRLs derive their legitimacy from the governance contracts. This relationship remains mutually reinforcing rather than linearly ranked [Uncertain].
- **Historical and Execution Agent Rules**:
  - `AGENTS-old.md:1-80`: Represents historical predecessor rules pointing to `docs/architectural-axioms.md` [Historical].
  - `openspec/AGENTS.md:1-12`: Contains OpenSpec-specific operational instructions for Junie execution within spec-driven workflows [Documented].

### 2.2 Continuum Representation, Axiom Status, and Pending Standing

A core aspect of the current governance model is the representation and migration of architectural axioms and elements into the Enterprise Architecture Continuum [Observed]:

1. **Axioms Authority Referents and Native Principles**:
   - `enterprise-architecture/governance/architectural-axioms.md:5` explicitly states: *"The following individually represented Principles are the highest architectural authority. RAD-1.1.5 changes their canonical representation, not their meaning, identity or authority. This page is navigation, not a second definition register."* [Documented].
   - However, repository sources contain diverging authority referents regarding the axioms [Observed]:
     - `AGENTS.md:28-38` (§1) states that all architecture and implementation is governed by the Harmonia Architectural Axioms defined in `enterprise-architecture/governance/architectural-axioms.md` (identifying the Markdown file at line 34).
     - `enterprise-architecture/governance/architecture-governance.md:51-52` links `docs/markdown/governance/architectural-axioms.md` as "the highest architectural authority".
     - `enterprise-architecture/governance/architecture-governance.md:277-278` states "The individual Architectural Axioms retain highest authority" (linking `enterprise-architecture/governance/architectural-axioms.md`).
     - Front matter in individual Principle elements (e.g., `enterprise-architecture/continuum/motivation/principle/e9bd61acaaea4577bc4086a5639e52c3.md:44-50`) identifies `docs/markdown/governance/architectural-axioms.md` with `role: "current governed definition"`.
   - The 18 Architectural Axioms (AX-01 through AX-18) are instantiated as canonical ArchiMate Principle elements in `enterprise-architecture/continuum/motivation/principle/` (e.g., `enterprise-architecture/continuum/motivation/principle/e9bd61acaaea4577bc4086a5639e52c3.md` for AX-01) [Observed].
   - Front-matter for these principle files explicitly records "standing.semantic": "Canonical" and "identity.allocation": "Preserved existing resource-owned UUID32; no allocation in migration" [Observed].
   - The precise relationship and formal standing between the Markdown index, the historical definition document, and the native Continuum Principle representations remains an unresolved authority question catalogued for Solution Architect review [Uncertain].

2. **Pending Standing of the Continuum Migration**:
   - The element-oriented Continuum model was introduced in RAD-1.1.5 via `enterprise-architecture/design/evidence/1.1.5/002/before/ai-agent-tasks/20261021T092725Z-establish-and-apply-element-continuum.md` [Observed].
   - However, the governing record `enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md` remains in a pending state: DR-2 records "holistic acceptance pending" (`enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md:6`), DR-5 records "ARB review in progress" (`enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md:9`), and `AGENTS.md:98-99` confirms "holistic acceptance remains pending" [Documented].
   - Consequently, while the Continuum operates as the active working repository for architectural elements, its designation as the sole canonical authority remains formally pending final Solution Architect/ARB confirmation [Uncertain].

3. **Legacy Documentation Governance and Section Supersession**:
   - `AGENTS.md:83-125` (§1.2) defines legacy documentation ("legacy-doco") as documentation outside governed sources (`enterprise-architecture/continuum/`, `enterprise-architecture/governance/`, and active `docs/markdown/` contracts) [Documented].
   - Information discovered in legacy documentation must be reviewed and adjudicated before incorporation ("legacy-doco -> architectural review/adjudication -> RADS") (`AGENTS.md:127-144`).
   - When legacy material is superseded, `AGENTS.md:146-182` mandates surrounding the original content with explicit supersession wrappers ("------- Legacy Content - Superseded ------- Start ------") rather than rewriting or deleting it [Documented].

### 2.3 HCRL Review and Decision Lifecycle

The HCRL is the single authoritative semantic change and review record for release transitions (`enterprise-architecture/governance/architecture-governance.md:134-178`).

#### The Six Praxis Stages
`enterprise-architecture/governance/architecture-review-praxis.md` defines the end-to-end review lifecycle across six explicit stages [Documented]:
1. **Stage 1: Establish the task and originating state** (`enterprise-architecture/governance/architecture-review-praxis.md:104-129`): Identify operative instructions, immediate governed release, baseline state, and applicable authority; retain formal Task Details in `enterprise-architecture/design/evidence/<RAD-version>/<HCRL-iteration>/before/ai-agent-tasks/`.
2. **Stage 2: Prepare the candidate HCRL** (`enterprise-architecture/governance/architecture-review-praxis.md:130-146`): Create `enterprise-architecture/design/review/<version>/hcrl-<version>-<iteration>.pending.md` using `enterprise-architecture/governance/templates/hcrl-template.pending.md`; retain review preparation Tasks in "before/" and Reports/outputs in "after/"; pending iterations consume no releases (`enterprise-architecture/governance/architecture-review-praxis.md:144-145`).
3. **Stage 3: Obtain and preserve adjudication** (`enterprise-architecture/governance/architecture-review-praxis.md:147-160`): Solution Architect (Mark Hunter) and ARB review candidate DRs; record attributable dispositions and comments.
4. **Stage 4: Apply the authorised scope** (`enterprise-architecture/governance/architecture-review-praxis.md:161-174`): Apply only unambiguous authorised directives and deterministic derivations; recheck baseline; prepare the one accepted-change patch increment under release policy (`enterprise-architecture/governance/architecture-review-praxis.md:170-173`); retain formal application Tasks in `enterprise-architecture/design/evidence/<RAD-version>/<HCRL-iteration>/before/ai-agent-tasks/`.
5. **Stage 5: Report and verify** (`enterprise-architecture/governance/architecture-review-praxis.md:180-222`): Emit formal Report in `enterprise-architecture/design/evidence/<RAD-version>/<HCRL-iteration>/after/ai-agent-reports/`; execute bounded validation; record factual outcomes and limits in applicable DRs; run evidence layout validation.
6. **Stage 6: Confirm and complete the transition** (`enterprise-architecture/governance/architecture-review-praxis.md:223-238`): Present results for Solution Architect confirmation; upon confirmation, complete the HCRL at `enterprise-architecture/design/register/<version>/hcrl-<version>-<iteration>.complete.md`.

#### Decision Review Log and Adjudication Semantics
- **Decision Review Structure**: Each HCRL contains one Decision Review table with columns "Decision Review | Subject | Solution Architect Disposition | Comment" (`enterprise-architecture/governance/architecture-governance.md:150-154`). Top-level matters are numbered DR-1 through DR-X, with optional single-level subordinate matters (e.g., DR-4.01) (`enterprise-architecture/governance/architecture-governance.md:156-161`).
- **Human Dispositions**: Record the attributable Solution Architect disposition and comment against the reviewed matter/content, preserving material revisions and their reasons. Pending fields, submitter classification, elapsed time, successful tooling and processing outcomes supply no acceptance. Do not replace a recorded human disposition with an agent-generated lifecycle status (`enterprise-architecture/governance/architecture-governance.md:170-175`).
- **Bounded Minutiae Control**: Minor or supporting matters are disclosed as a bounded Minutiae group evaluated by a single governed control: "Minutiae-Accepted: Yes | No" (`enterprise-architecture/governance/architecture-governance.md:180-197`). Silence does not constitute acceptance, and "ACCEPTED_WITHOUT_COMMENT" is prohibited as a per-item disposition (`enterprise-architecture/governance/architecture-governance.md:191-197`).
- **Retention and Disposal of Pending Records**: Pending iterations preserve the review/adjudication lifecycle and are retained while needed for active review and traceability (`enterprise-architecture/governance/architecture-governance.md:325-333`, `enterprise-architecture/governance/architecture-review-praxis.md:232-233`). Disposal of superseded pending material may be determined pragmatically after completion without requiring indefinite retention (`enterprise-architecture/governance/architecture-governance.md:329-333`).
- **Multi-Iteration and Rejection History**: HCRL transitions frequently involve multiple iterations or whole rejections. For example, `enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-001.pending.md` was rejected as a whole under DR-1 of `enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md:28-33`, and iteration 1.1.4-004 completed after iterations 001–003 established demonstration refactoring (`enterprise-architecture/design/register/1.1.4/hcrl-1.1.4-004.complete.md:1-100`).

### 2.4 Release Versioning and Root Release State

- **Release Authority**: `architecture-release.json` is the single source of authority for Harmonia Architecture/RAD release versions (`docs/markdown/governance/architecture-release-policy.md:7-17`, `AGENTS.md:184-193`).
- **Patch Increment Policy and Helper**: Every accepted repository change set must apply a patch increment (e.g., 1.1.4 -> 1.1.5) via `scripts/architecture_release.py` (`docs/markdown/governance/architecture-release-policy.md:228-241`). Minor or major increments require explicit governance decisions (`AGENTS.md:189-190`).
- **Decoupling from Software Builds**: Architecture release versions operate independently of Maven build versions (`pom.xml:26`, fixed at "1.0.0-SNAPSHOT") and Git branch names (`docs/markdown/governance/architecture-release-policy.md:27-29`).
- **Application-Time Increment Mechanics vs Acceptance Status**:
  - `architecture-release.json` currently records version 1.1.5 and previous_version 1.1.4.
  - `enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md` is currently pending (DR-2, DR-5), noting at line 22: "resulting root release 1.1.5 / previous 1.1.4".
  - This state is fully consistent with Praxis Stage 4 (`enterprise-architecture/governance/architecture-review-praxis.md:170-173`) and release policy precedent (`docs/markdown/governance/architecture-release-policy.md:54-58`), which mandate that governance application prepare the one accepted-change patch increment during change set application to identify the release used for resulting-state verification [Documented].
  - Crucially, the governing contracts explicitly establish that "an increment alone does not establish acceptance or publication" (`enterprise-architecture/governance/architecture-review-praxis.md:172`) and "the increment records this applied change set and supplies no acceptance" (`docs/markdown/governance/architecture-release-policy.md:56`). Holistic acceptance of the 1.1.5 change set remains pending Solution Architect confirmation (`[Documented]`, `[Observed]`).

---

## 3. Entry Points / Recognised Operations

The Harmonia governance framework defines entry points spanning command-line interfaces (CLIs), Makefile orchestration targets, and formal prompt triggers governed by Praxis contracts.

### 3.1 CLI Command Interfaces

Governance operations are exposed via CLI scripts located primarily in `scripts/` and supported by legacy tools in `docs/archimate/framework/tools/` [Observed]:

| CLI Script / Command | Recognized Arguments & Options | Primary Function & Operational Boundary |
| :--- | :--- | :--- |
| `python3 scripts/architecture_continuum.py validate` | `--root <path>`, `--report <path>` | Evaluates the complete canonical Continuum against JSON schemas, native ArchiMate XSD, relationship endpoint matrices, naming rules, and migration boundaries (`scripts/architecture_continuum.py:125-300`). Read-only verification; allocates zero identities. |
| `python3 scripts/architecture_continuum.py project` | `--output <xml>`, `--report <path>`, `"--check"` | Synthesizes an ArchiMate 3.0 Open Exchange XML model from canonical Markdown elements using `enterprise-architecture/design/archimate/projection-template.xml` (`scripts/architecture_continuum.py:320-418`). With "--check", asserts byte-for-byte freshness without writing. |
| `python3 scripts/architecture_continuum.py template` | `--type <ArchiMateType>`, `--specialization <Name>` | Emits a compliant Markdown template with JSON front-matter conforming to the governed type catalogue profile (`scripts/architecture_continuum.py:455-466`). |
| `python3 scripts/architecture_continuum.py create` | `--spec <spec.json>` | **Explicit Canonical Identity Allocation Entry Point** (`enterprise-architecture/governance/continuum-incorporation-praxis.md:32` instructs allocation through `create`; `enterprise-architecture/governance/continuum-representation.md:133` also allows an explicitly authorised creation/ingestion operation to allocate; whether other operations may allocate is an open ambiguity [Documented]): Allocates a single UUIDv4 "canonicalId" (line 442), derives a deterministic UUIDv5 "exchange.identifier" (line 445), preflights prospective continuum validity, creates the parent directory (line 447), and creates the element file in exclusive creation mode `'x'` (line 450) (`scripts/architecture_continuum.py:425-453`). |
| `python3 scripts/continuum_workbench.py review` | `--baseline <xml>`, `--submitted <xml>`, `--output <json>` | Hash-bound round-trip review: Compares current projection baseline with architect-submitted export, reporting native element, property, relationship, and View differences without mutating canonical sources (`scripts/continuum_workbench.py:54-84`). |
| `python3 scripts/continuum_workbench.py apply` | `--decision <decision.json>` | Hash-bound round-trip apply: Enforces strict pre-apply gates (checks authority file existence per `enterprise-architecture/design/archimate/README.md:40-42`, asserts whole projection baselineSha256 matches current projection at line 96, requires empty unresolved additions/deletions at line 94, requires explicit disposition for every change at lines 103-104, restricts relationship and View actions to reject/defer only at line 107, and verifies element preimages at line 114); preflights updates in a temporary staging tree (`scripts/continuum_workbench.py:121-138`); and overwrites replaced element files in place via "write_text" (line 141) with appended review provenance (no filesystem transaction or concurrent-writer guarantee claimed, lines 139-140). |
| `python3 scripts/continuum_manifest.py` | `--root <path>`, `"--check"` | Generates or verifies `enterprise-architecture/continuum/MANIFEST.md` navigation and review projection across all populated native types and specializations (`scripts/continuum_manifest.py:40-116`). |
| `python3 scripts/publish_continuum.py` | `--output <dir>`, `"--check"` | Generates full-content HTML review browser ("continuum.html"), LaTeX identity catalogue ("continuum-catalogue.tex"), versioned index (`enterprise-architecture/continuum/documentation/published/1.1.5/README.md`), and publication verification JSON ("publication.json") (`scripts/publish_continuum.py:44-89`). |
| `python3 scripts/architecture_release.py` ("show", "check", "bump") | "show" \| "check" \| "bump --expect <ver>" | Manages root `architecture-release.json` authority; "bump" prepares an atomic patch increment for an accepted change set (`scripts/architecture_release.py:41-75`). |
| `python3 scripts/architecture_review_evidence.py` | `--version <ver>`, `--iteration <iter>` | Verifies HCRL iteration evidence layout, header timestamp presence ("Task-Issued-At" / "Report-Produced-At"), and filename prefix correspondences (`scripts/architecture_review_evidence.py:23-100`). |

### 3.2 Makefile Orchestration Targets

`docs/archimate/framework/Makefile` defines automation targets enforcing time-bounded execution via the coreutils "timeout" utility [Documented], [Observed]:

```text
docs/archimate/framework/Makefile
├── Dynamic Release Expansion: RAD_VERSION := $(shell python3 scripts/architecture_release.py show) (line 14)
├── Active Continuum Targets (Default pipeline)
│   ├── all: continuum continuum-test continuum-check (line 16)
│   ├── continuum: manifest (90s) -> project XML (90s) -> publish HTML/LaTeX (90s) (lines 19-22)
│   ├── continuum-check: manifest --check (90s) -> project --check (90s) -> publish --check (90s) (lines 24-27)
│   └── continuum-test: unittest discover test_continuum*.py (120s) (lines 29-30)
├── Historical Baseline Procedures (Replay against release fixtures)
│   └── historical-baseline-all: baseline -> test -> baseline-check -> baseline-report -> baseline-independent (line 34)
├── Historical LaTeX Recovery Procedures
│   └── recovery-all: recovery -> test -> recovery-check -> recovery-report (line 48)
├── Historical 1.0.4 Publication Pipeline
│   └── legacy-all: generate -> inventory -> adoption -> bindings -> test -> check -> report (lines 60-81)
└── Historical Shared Test Suite
    └── test: unittest discover tests (420s timeout, lines 83-84)
```

- **Dynamic Release Version Expansion (`docs/archimate/framework/Makefile:14`)**:
  - `RAD_VERSION := $(shell $(PYTHON) $(REPOSITORY)/scripts/architecture_release.py show)` runs dynamically on every Makefile invocation to determine the publication output path (`enterprise-architecture/continuum/documentation/published/$(RAD_VERSION)`).
- **Active Continuum Pipeline (`docs/archimate/framework/Makefile:14-30`)**:
  - `make -C docs/archimate/framework all`: Executes the primary validation, generation, and test suite.
  - `make -C docs/archimate/framework continuum`: Runs `scripts/continuum_manifest.py`, `scripts/architecture_continuum.py project`, and `scripts/publish_continuum.py` with 90-second timeout limits per step (`docs/archimate/framework/Makefile:19-22`).
  - `make -C docs/archimate/framework continuum-check`: Verifies staleness of the manifest, Open Exchange XML, and publication browser using `"--check"` flags (`docs/archimate/framework/Makefile:24-27`).
  - `make -C docs/archimate/framework continuum-test`: Runs unit tests under `scripts/tests/test_continuum.py` with a 120-second timeout (`docs/archimate/framework/Makefile:29-30`).
- **Historical Procedures & Long-Running Tests (`docs/archimate/framework/Makefile:32-87`)**:
  - Targets such as "historical-baseline-all", "recovery-all", and "legacy-all" are explicitly retained for historical replay against release fixtures and do not form part of the current canonical Continuum pipeline (`docs/archimate/framework/Makefile:32-34, 59-60`).
  - The historical shared "test" target (`docs/archimate/framework/Makefile:83-84`) executes legacy unit tests under `docs/archimate/framework/tests` with a 420-second timeout limit.

### 3.3 Governed Prompt Triggers (Round-Trip Praxis)

`docs/markdown/governance/archimate-round-trip-engineering-praxis.md:59-71` defines standard prompt trigger phrases that activate governed agent workflows without requiring restatement of mechanics [Documented]:

1. **Review Trigger**:
   > `"Model Updated. Please review and generate change-review.md."`
   - Activates Discovery and Validate/Observe stages. Discovers unique baseline and architect export XML files via `docs/archimate/framework/tools/roundtrip_praxis.py discover`, observes differences via `docs/archimate/framework/tools/diagnose_roundtrip.py`, and stops at the **Change Review checkpoint** ("change-review.md").
2. **Apply Trigger**:
   > `"change-review.md reviewed and updated. Apply directives."`
   - Activates Read Adjudication and Apply/Derive stages. Reads the reviewed document as human adjudication, checks pinned input SHA256 digests via `docs/archimate/framework/tools/roundtrip_praxis.py inspect-review`, applies authorised directives, and stops at any missing or contradictory directive.

#### Adapter Distinctions and Current Release Limits
- **Praxis Banner & Bounded Scope**: The banner in `docs/markdown/governance/archimate-round-trip-engineering-praxis.md:1` explicitly establishes that for canonical resources in RAD-1.1.5, the [Continuum workbench procedure](enterprise-architecture/design/archimate/README.md) implements the exact-baseline, review, and application gates, whereas historical source-adapter instructions apply only to their originating editions [Documented].
- **Discovery Locator Gap in RAD-1.1.5**: Legacy `roundtrip_praxis.py discover` automatically scans `docs/archimate/exchange/publish/<version>` (`docs/archimate/framework/tools/roundtrip_praxis.py:26-41`). Because the canonical RAD-1.1.5 model is published under `enterprise-architecture/continuum/documentation/published/1.1.5/` and no "1.1.5" directory exists under `docs/archimate/exchange/publish/`, automated discovery without explicit paths cannot locate the current release model [Observed].
- **Format Mismatch**: The historical round-trip workflow generated and consumed Markdown review documents ("change-review.md"), whereas the Continuum workbench produces structured JSON review evidence (`scripts/continuum_workbench.py:81-84`) and consumes JSON decision files referencing HCRL DR table entries (`scripts/continuum_workbench.py:87-108`) [Observed].

### 3.4 Operational Scopes and Bounded Unit-of-Work

Execution across these entry points enforces specific unit-of-work boundaries [Observed]:
- **Granular Scopes**:
  - Element Creation: Bounded to validating prospective insertion of a single element (`scripts/architecture_continuum.py:425-453`).
  - Workbench Apply: Bounded to applying specific reviewed element replacements matching explicit HCRL dispositions (`scripts/continuum_workbench.py:87-146`).
  - Release Bump: Bounded to atomic update of `architecture-release.json` (`scripts/architecture_release.py:41-60`).
- **System-Wide Scopes**:
  - Projection, manifest generation, and publication browser synthesis execute as whole-continuum passes over all canonical element files in `enterprise-architecture/continuum/`.
- **Preflight Gating and Boundary Limits**:
  - Preflight checks validate prospective states (e.g. `create()` verifies prospective continuum validity at `scripts/architecture_continuum.py:441, 449`, though it creates parent directories at line 447; `apply()` preflights in a temporary staging tree at `scripts/continuum_workbench.py:121-138`, but performs ordinary in-place file writes at line 141 without filesystem transaction guarantees). Validation tools fail closed when encountering schema violations, illegal relationship endpoints, or unjustified unresolved properties.

---

## 4. Major Governance Components

The Harmonia governance architecture comprises eight primary functional components spanning canonical element management, projection, publication, round-trip adaptation, release tracking, and assurance.

### 4.1 Canonical Continuum Element Engine (`scripts/architecture_continuum.py`)

The Continuum element engine maintains the canonical file-based repository of architectural elements under `enterprise-architecture/continuum/` [Documented], [Observed]:

1. **Canonical Markdown Element Format**:
   - Each element is represented as an individual Markdown file whose filename is `<canonicalId>.md` (`scripts/architecture_continuum.py:105`).
   - File structure begins with strict JSON front-matter delimited by "---" lines (`scripts/architecture_continuum.py:79-90`).
   - The body contains a top-level heading matching "displayName" (`# <displayName>`) followed by mandatory `## Architectural content` (`scripts/architecture_continuum.py:148-151`).
2. **Governance Configuration Schemas**:
   - `enterprise-architecture/governance/continuum/element.schema.json`: JSON Schema Draft 2020-12 defining metadata contracts, requiring exactly 15 fields: "schemaVersion", "canonicalId", "archimateType", "specialization", "displayName", "friendlyName", "canonicalName", "standing", "identity", "naming", "provenance", "sourceBindings", "exchange", "relationships", and "unresolvedProperties" (`enterprise-architecture/governance/continuum/element.schema.json:6-22`, `scripts/architecture_continuum.py:133-145`).
   - `enterprise-architecture/governance/continuum/type-catalogue.json`: Authoritative catalogue of 60 native ArchiMate types, layer/aspect directories, display prefixes, and specializations (`scripts/architecture_continuum.py:93-106, 109-122`).
   - `enterprise-architecture/governance/continuum/native-relationships.json`: Permitted native relationship endpoint matrices mapped across ArchiMate concepts (`scripts/architecture_continuum.py:139-140, 225-233`).
   - `enterprise-architecture/governance/continuum/migration-boundary.json`: Pinned historical representation identifiers, type bindings, and withdrawn allocation blacklists preventing identity reuse (`scripts/architecture_continuum.py:278-288`).
3. **Identity Allocation & Exchange Identifier Mechanics**:
   - **Canonical Population Allocation Breakdown**: Static inspection (regex over file contents) of the 1,602 canonical element files in `enterprise-architecture/continuum/` reveals three distinct allocation histories [Observed]:
     - **Preserved UUID32 (1,394 elements)**: Preserved existing resource-owned UUID32 identities without reallocation (`identity.allocation: "Preserved existing resource-owned UUID32; no allocation in migration"`).
     - **Migration-Task UUIDv4 (165 elements)**: Ingested during the RAD-1.1.5 migration Task via direct calls to `uuid.uuid4().hex` in `scripts/ingest_rad_definitions.py:50` (`identity.allocation: "UUIDv4 allocated once under the explicit RAD-1.1.5 migration Task"`).
     - **Explicit Create UUIDv4 (43 elements)**: Allocated via calls to `c.create()` in `scripts/apply_arb_designnote_assertions.py:68` (`identity.allocation: "UUIDv4 allocated once by explicit create"`).
   - **Prospective Identity Allocation**: Governed by `enterprise-architecture/governance/continuum-incorporation-praxis.md` and `enterprise-architecture/governance/continuum-representation.md`, `create()` (`scripts/architecture_continuum.py:425-453`) is the primary prospective canonical identity allocation path, allocating UUIDv4 at line 442 (`m['canonicalId'] = uuid.uuid4().hex`).
   - **Exchange Identifier Derivation vs Emission**: `create()` derives the Open Exchange identifier deterministically via UUIDv5 from "canonicalId" (`scripts/architecture_continuum.py:420-422, 445`). Crucially, `project()` does not derive identifiers; it emits the stored `m['exchange']['identifier']` at line 330 (`node = ET.SubElement(es, tag('element'), identifier=m['exchange']['identifier'])`). Migrated elements retain their historical exchange IDs (e.g., "id-e6d709af40515f9a9d6d3e65a42cd004" for AX-01 in `enterprise-architecture/continuum/motivation/principle/e9bd61acaaea4577bc4086a5639e52c3.md:58-60`) [Observed].
   - **Relationship Identifier Derivations in Migration**: The migration scripts derived deterministic relationship identifiers using UUIDv5 over "SEMANTIC_NAMESPACE" with structured domain prefixes, including `arb-qualified:<sourcePointer>` (`scripts/apply_arb_continuum_reconciliation.py:82`) and `arb-matrix:<source>:<selector>` (`scripts/apply_arb_remaining_sources.py:62`) [Observed].

### 4.2 ArchiMate Open Exchange Projection Engine (`scripts/architecture_continuum.py:320-418`)

Synthesizes the complete ArchiMate 3.0 Open Exchange XML model at `enterprise-architecture/continuum/documentation/published/1.1.5/Harmonia-Architecture.xml` [Documented], [Observed]:
- Driven by `enterprise-architecture/design/archimate/projection-template.xml`, which owns existing model-level properties, property definitions, navigation structures, and **1,209 Views** (including their geometry), while containing zero semantic elements or relationships (`enterprise-architecture/design/archimate/README.md:6-10`, `scripts/architecture_continuum.py:323-327`).
- Emits elements with stored exchange identifiers, native ArchiMate types (`xsi:type`), display names, and Markdown body content as `<documentation>` (`scripts/architecture_continuum.py:330-336`).
- Projects outgoing relationships and properties, updates model-level release properties from `architecture-release.json`, and adds navigation items under `<organizations>` (`scripts/architecture_continuum.py:350-379`).
- Executes multi-layer validation: offline schema validation against `docs/archimate/framework/schemas/archimate3_Diagram.xsd`, dangling reference checks, containment cycle resolution via `docs/archimate/framework/tools/containment.py`, and information naming checks via `docs/archimate/framework/tools/information_naming.py` (`scripts/architecture_continuum.py:388-414`).

### 4.3 Continuum Review Manifest Generator (`scripts/continuum_manifest.py`)

Generates `enterprise-architecture/continuum/MANIFEST.md` as a generated review and navigation projection [Documented], [Observed]:
- Iterates over all canonical elements, grouping them by layer/aspect and native ArchiMate type per `enterprise-architecture/governance/continuum/type-catalogue.json` (`scripts/continuum_manifest.py:44-50`).
- Generates Markdown tables detailing element display names, native types, specializations, semantic standing, currentness, canonical names, canonical IDs, relative file paths, and unresolved properties (`scripts/continuum_manifest.py:80-105`).
- Validates the continuum prior to rendering and supports `"--check"` for automated CI/gate verification (`scripts/continuum_manifest.py:41, 108-116`).

### 4.4 Multi-Format Continuum Publication Engine (`scripts/publish_continuum.py`)

Produces publication and review artifacts under `enterprise-architecture/continuum/documentation/published/1.1.5/` [Documented], [Observed]:
- **Interactive HTML Browser ("continuum.html")**: Single-page application embedding full element Markdown bodies rendered to HTML, client-side filtering by type and currentness, and in-browser search (`scripts/publish_continuum.py:20-36, 75`).
- **LaTeX Identity Catalogue ("continuum-catalogue.tex")**: Standalone LaTeX document cataloguing all element identities, standings, friendly names, canonical names, and source paths for formal Solution Architect/ARB review (`scripts/publish_continuum.py:50-74`).
- **Review Index (`enterprise-architecture/continuum/documentation/published/1.1.5/README.md`)**: Table-of-contents Markdown index linking canonical sources, HTML browser, Open Exchange XML, and LaTeX catalogue (`scripts/publish_continuum.py:47-49, 56-61`).
- **Verification Manifest ("publication.json")**: SHA256 checksums and verification metadata across all generated publication artifacts (`scripts/publish_continuum.py:77-78`).

### 4.5 Hash-Bound Continuum Workbench Adapter (`scripts/continuum_workbench.py`)

Bridges the file-based Continuum with the Archi visual modeling environment [Documented], [Observed]:
- **Review Engine (`scripts/continuum_workbench.py:54-84`)**: Compares projected baseline XML with incoming architect XML export. Reports differences across native elements, property values, relationships, and Views without mutating canonical sources. Emits structured review JSON recording baseline and submission digests.
- **Apply Engine (`scripts/continuum_workbench.py:87-146`)**: Consumes an attributable decision file referencing an HCRL DR. Enforces strict pre-apply assurance gates:
  1. *Authority presence check*: Verifies the referenced authority source file exists (`scripts/continuum_workbench.py:88-90`), validating the presence of recorded authority rather than its authenticity (`enterprise-architecture/design/archimate/README.md:40-42`).
  2. *Review and baseline integrity*: Asserts that "reviewSha256" matches the review evidence (line 92) and that the whole current projection hash matches "baselineSha256" byte for byte (line 96). Any intermediate continuum, template, or release version change blocks apply.
  3. *Unresolved identity gate*: Requires "unresolved" additions/deletions to be empty (line 94).
  4. *Complete disposition coverage*: Requires an explicit disposition ("apply", "reject", "defer") with an attributable reason for every reviewed element, relationship, and View change (lines 103-106).
  5. *Restriction to element amendments*: Prohibits automatic apply of relationship or View changes at line 107 (allowing "reject" or "defer" only; relationship/View changes require reviewed amendment to canonical element front-matter or `enterprise-architecture/design/archimate/projection-template.xml`).
  6. *Preimage verification*: Asserts that the SHA256 digest of the canonical element file on disk matches the review preimage digest (line 114).
  7. *Staged preflight*: Copies prospective replacements and schema dependencies into a temporary directory ("tempfile.TemporaryDirectory", lines 121-138) and executes full continuum validation before writing to disk.
  8. *In-place replacement*: Overwrites replaced element files in place via "write_text" (line 141) with appended review provenance. Per `scripts/continuum_workbench.py:139-140` and `enterprise-architecture/design/archimate/README.md:49-50`, multi-file writes are preflighted but claim no filesystem transaction or concurrent-writer guarantee.

### 4.6 Release Version Authority (`scripts/architecture_release.py`)

Maintains root `architecture-release.json` authority [Documented], [Observed]:
- Validates that "version" and "previous_version" adhere to semantic version formatting ("X.Y.Z") and that previous versions precede current versions (`scripts/architecture_release.py:16-38`).
- Implements atomic patch increment ("bump_patch") using temporary file writing and atomic filesystem replacement ("NamedTemporaryFile" + "os.replace", lines 41-60).

### 4.7 Evidence Layout Validator (`scripts/architecture_review_evidence.py`)

Enforces evidence layout and temporal provenance standards [Documented], [Observed]:
- Scans `enterprise-architecture/design/evidence/` for "before/ai-agent-tasks/" and "after/ai-agent-reports/" (`scripts/architecture_review_evidence.py:23-42`).
- Validates presence of "Task-Issued-At" and "Report-Produced-At" fields and asserts correspondence between filename timestamp prefixes and recorded ISO 8601 timestamps (`scripts/architecture_review_evidence.py:44-80`).

---

## 5. Invocation and Dependency Flows

The Harmonia governance framework executes through structured invocation workflows connecting CLI tools, build targets, and governance praxes.

### 5.1 End-to-End Governance Execution Flows

```text
1. Continuum Validation & Publication Flow:
   [make continuum]
         │
         ├──> [continuum_manifest.py] ───────────> Validates elements -> Writes MANIFEST.md
         │
         ├──> [architecture_continuum.py project] ─> Validates -> Loads template -> Resolves containment/naming -> Emits Harmonia-Architecture.xml
         │
         └──> [publish_continuum.py] ─────────────> Renders HTML browser, LaTeX catalogue, README.md, publication.json

2. ArchiMate Round-Trip Engineering Review Flow:
   [Architect Export (Archi)] + [Current Projection Baseline]
         │
         └──> [continuum_workbench.py review] ───> Validates inputs -> Compares elements/views -> Emits review evidence JSON
                                                         │
                                                         └──> [Solution Architect Adjudication (HCRL DR Table)]

3. ArchiMate Round-Trip Engineering Apply Flow:
   [Reviewed Decision JSON (HCRL DR)] + [Canonical Elements]
         │
         └──> [continuum_workbench.py apply] ────> Verifies whole baseline SHA256 & preimages -> Preflights in tempdir -> In-place write -> Appends Provenance
                                                         │
                                                         └──> [architecture_release.py bump] (Stage 4 Patch Increment)

4. HCRL Transition & Evidence Lifecycle Flow:
   Stage 1 (Task in before/) ──> Stage 2 (Candidate HCRL in review/) ──> Stage 3 (SA Adjudication)
                                                                               │
   Stage 6 (SA Confirmation -> register/) <── Stage 5 (Report in after/ + verify) <── Stage 4 (Apply directives + Release Bump)
```

1. **Continuum Validation & Publication Flow**:
   - `make -C docs/archimate/framework continuum` executes three sequential steps:
     1. `scripts/continuum_manifest.py`: Validates continuum and regenerates `enterprise-architecture/continuum/MANIFEST.md` (`docs/archimate/framework/Makefile:20`).
     2. `scripts/architecture_continuum.py project`: Validates elements, applies `enterprise-architecture/design/archimate/projection-template.xml`, runs containment and information naming checks, and emits `enterprise-architecture/continuum/documentation/published/1.1.5/Harmonia-Architecture.xml` (`docs/archimate/framework/Makefile:21`).
     3. `scripts/publish_continuum.py`: Generates "continuum.html", "continuum-catalogue.tex", `enterprise-architecture/continuum/documentation/published/1.1.5/README.md`, and "publication.json" (`docs/archimate/framework/Makefile:22`).
2. **ArchiMate Round-Trip Engineering Review Flow**:
   - Architect creates model changes in Archi and exports Open Exchange XML.
   - Operator/agent invokes `python3 scripts/continuum_workbench.py review --baseline <proj.xml> --submitted <arch.xml> --output <review.json>`.
   - Tool verifies exact baseline match, extracts element/view differences, and outputs structured change records (`scripts/continuum_workbench.py:54-84`).
   - Operator creates or updates candidate HCRL DR table in `enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md`.
3. **ArchiMate Round-Trip Engineering Apply Flow**:
   - Operator/agent prepares decision file referencing attributable HCRL DR.
   - Operator/agent invokes `python3 scripts/continuum_workbench.py apply --decision <decision.json>`.
   - Tool verifies authority file presence, review SHA256, current whole projection baseline SHA256, and element preimages; stages prospective continuum in a temporary directory; validates schemas and relationships; overwrites element files in place; and appends review provenance (`scripts/continuum_workbench.py:87-146`).
   - Change set application prepares patch increment via `python3 scripts/architecture_release.py bump --expect <version>` per Praxis Stage 4 (`enterprise-architecture/governance/architecture-review-praxis.md:170-173`).
4. **HCRL Transition & Evidence Lifecycle Flow**:
   - Follows Praxis Stages 1–6 (`enterprise-architecture/governance/architecture-review-praxis.md:104-238`), managing task details in "before/ai-agent-tasks/", candidate HCRLs in "enterprise-architecture/design/review/", execution reports in "after/ai-agent-reports/", evidence layout validation via `scripts/architecture_review_evidence.py`, and final register promotion to "enterprise-architecture/design/register/".

### 5.2 Dynamic Import and Cross-Tree Dependencies

The Continuum scripts in `scripts/` exhibit direct dynamic import dependencies on legacy framework tools in `docs/archimate/framework/tools/` [Observed]:

```text
scripts/architecture_continuum.py
├── sys.path.insert(0, 'docs/archimate/framework/tools')
│   ├── from governed_metadata import component, qualified_names (lines 166-172)
│   ├── from generate_views import load_schema (lines 388-390)
│   ├── import containment (line 401)
│   ├── import information_naming (line 402)
│   └── from diagnose_roundtrip import Exchange (line 403)
└── External Libraries: jsonschema (Draft202012Validator), lxml (etree)

scripts/continuum_workbench.py
├── import architecture_continuum (line 15)
└── sys.path.insert(0, 'docs/archimate/framework/tools')
    └── from change_review import signature (lines 47-48)

scripts/architecture_identity.py
├── from architecture_release import load_release, version_parts (line 17)
├── import rads_business_architecture as business (line 18)
└── sys.path.insert(0, 'docs/archimate/framework/tools')
    ├── from workspace_paths import historical_path (lines 29-30)
    ├── import assess_rad (line 31)
    ├── import generate_views as views (line 32)
    └── import publish_documentation as documentation (line 33)
```

- **Runtime Path Injection**: `scripts/architecture_continuum.py` inserts `docs/archimate/framework/tools` into "sys.path" at lines 166, 171, and 388 to reuse naming, containment, and schema resolution logic from legacy tools.
- **Workbench Coupling**: `scripts/continuum_workbench.py` imports `scripts/architecture_continuum.py` directly (line 15) and injects `docs/archimate/framework/tools` at line 47 to access the signature function of change_review.
- **Identity Script Coupling**: `scripts/architecture_identity.py` injects `docs/archimate/framework/tools` at line 29 to import "workspace_paths", "assess_rad", "generate_views", and "publish_documentation".

---

## 6. Architecture Mutation Paths

The Harmonia governance framework defines nine distinct architecture mutation paths, each governed by specific preflight checks, identity allocation constraints, and failure recovery boundaries [Observed].

### 6.1 Mutation Path Classification and Boundaries

| Mutation Path | Entry Point / Mechanism | Target Artifacts | Identity Allocation Boundary | Preflight Checks & Gating | Rollback / Failure Recovery |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Canonical Element Creation** | `python3 scripts/architecture_continuum.py create --spec <spec.json>` (`scripts/architecture_continuum.py:425-453`) | `enterprise-architecture/continuum/` | **Explicit allocation path instructed by the incorporation praxis** (`enterprise-architecture/governance/continuum-incorporation-praxis.md:32`; `enterprise-architecture/governance/continuum-representation.md:133` also allows authorised creation/ingestion, so exclusivity is not established [Documented]): Allocates UUIDv4 "canonicalId" (line 442) and derives UUIDv5 "exchange.identifier" (line 445). | Requires attributable authority & provenance; asserts source bindings uniqueness; preflights prospective continuum validation with preview element (`lines 429-441`). | Exclusive file opening mode `'x'` (line 450); aborts before write if validation fails. |
| **Direct Canonical Element Editing** | Manual authoring / Agent edit under authorised task | `enterprise-architecture/continuum/` | **Zero allocation**: Modifies content while preserving existing "canonicalId" and "exchange.identifier". | No gate on the write. Optional after-the-fact validation may be run via `python3 scripts/architecture_continuum.py validate` (schema, heading match, `## Architectural content`, relationship endpoints). | Git working tree reversion (`git checkout / restore`). |
| **Workbench Submission Apply** | `python3 scripts/continuum_workbench.py apply --decision <decision.json>` (`scripts/continuum_workbench.py:87-146`) | `enterprise-architecture/continuum/` | **Zero allocation**: Updates element properties and documentation from reviewed Archi changes. Prohibits retyping or exchange ID rebinding (`lines 115-116`). | Asserts authority file presence; checks review SHA256, whole projection baseline SHA256, and canonical source preimage SHA256; requires empty unresolved list and complete disposition coverage; executes staged preflight validation in "tempfile.TemporaryDirectory" (`lines 88-138`). | In-place file overwrites via "write_text" (line 141) with appended review provenance; no filesystem transaction guarantee claimed (lines 139-140); retained review and preimage digests support recovery. |
| **Scripted Semantic Reconciliation** | Migration scripts (e.g. `scripts/apply_arb_continuum_reconciliation.py`) | Canonical Markdown element files (`enterprise-architecture/continuum/`) | **Non-zero element allocation**: Generally records "identity.correspondence" and rebinds relationships, but `scripts/apply_arb_designnote_assertions.py:68` allocated 43 identities. | Consumes explicit reviewed reconciliation plan (`enterprise-architecture/design/evidence/1.1.5/002/before/arb-reconciliation-plan.json`); validates native relationship endpoint applicability (`lines 31-35`); many validate *after* writing. | Deterministic idempotent replay ("Correspondences are stable on retry", line 144); records invocation evidence in `enterprise-architecture/design/evidence/1.1.5/002/after/`. |
| **Release Patch Increment** | `python3 scripts/architecture_release.py bump --expect <version>` (`scripts/architecture_release.py:41-60`) | Root `architecture-release.json` | **Version transition**: Updates "version" and "previous_version". | Asserts current release equals "--expect" (line 45); verifies semver formatting. | Atomic file replacement via "NamedTemporaryFile" + "os.replace" (lines 52-56) prevents partial JSON reads. |
| **Amending Projection Template** | Manual authoring / reviewed template update | `enterprise-architecture/design/archimate/projection-template.xml` | **Zero element allocation**: Updates model-level properties, property definitions, organization folders, or any of the 1,209 Views and their geometry. | No gate on the edit itself; checked only if `python3 scripts/architecture_continuum.py project` is subsequently run (offline XSD validation, containment resolution). | Git working tree reversion. |
| **Amending Governance Schemas & Configs** | Manual authoring / reviewed configuration update | `enterprise-architecture/governance/continuum/` (`enterprise-architecture/governance/continuum/element.schema.json`, `enterprise-architecture/governance/continuum/type-catalogue.json`, `enterprise-architecture/governance/continuum/native-relationships.json`, `enterprise-architecture/governance/continuum/migration-boundary.json`) | **Zero element allocation**: Amends metadata schemas, native type mappings, relationship endpoint matrices, or historical migration boundaries. | No gate on the edit itself; these files are consulted when continuum validation is subsequently run, so errors surface only then (optional after-the-fact validation). | Git working tree reversion. |
| **Generated Manifest Generation** | `python3 scripts/continuum_manifest.py` / `make -C docs/archimate/framework continuum` | `enterprise-architecture/continuum/MANIFEST.md` | **Zero element allocation**: Regenerates the navigation and review table projection across all 1,602 canonical elements. | Validates complete continuum prior to rendering (`scripts/continuum_manifest.py:41`). | Deterministic regeneration via `scripts/continuum_manifest.py`. |
| **Scripted Finalizer Rewrites** | `scripts/finalize_continuum_succession.py` | `enterprise-architecture/governance/architectural-axioms.md`, `docs/markdown/governance/baseline/*.md`, every non-baseline source mapped in `enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json`, and the evidence files `enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json` and `enterprise-architecture/design/evidence/1.1.5/002/after/corpus-accounting.json` | **Zero element allocation**: Rewrites the Axioms navigation index, prepends succession banners to mapped non-baseline sources (skipped if already present; line 18), wraps baseline carriers (line 24), and rewrites both evidence JSON files (line 31) (`scripts/finalize_continuum_succession.py:12-37`). | No gate: writes occur before any validation and without checking original bytes; depends on `git show`. | Idempotent script execution; Git working tree reversion. |

### 6.2 Preflight Validation and Mutation Constraints

Only four paths check before writing: `create` (preview validation), workbench apply (staged gates), the release bump ("--expect" check) and the manifest (validation before render). Direct editing, template edits, configuration edits, scripted reconciliation (which validates *after* writing, e.g. `scripts/apply_arb_continuum_reconciliation.py:136-147`) and the finalizer have no pre-write gate; validation for them is optional or after-the-fact [Observed].
1. **Prospective Allocation Rule**: `create()` (`scripts/architecture_continuum.py:425-453`) is the allocation path instructed by `enterprise-architecture/governance/continuum-incorporation-praxis.md:32`; `enterprise-architecture/governance/continuum-representation.md:133` additionally allows explicitly authorised creation/ingestion, so exclusivity is an open ambiguity [Documented]. The docstring at `scripts/architecture_continuum.py:426` covers only validation and publication; that workbench apply and the release bump allocate nothing is observed from their own code, not from that docstring.
2. **Preimage Integrity Check**: Workbench apply verifies that the SHA256 digest of the canonical element file on disk matches the preimage digest captured during review (`scripts/continuum_workbench.py:114`).
3. **Staged Temporary Validation**: Workbench apply stages prospective replacements and schema dependencies in a temporary directory ("tempfile.TemporaryDirectory") and runs complete continuum validation before writing to disk (`scripts/continuum_workbench.py:121-138`).
4. **Endpoint Validity Enforcement**: Relationship assertions added or modified during reconciliation or editing must validate against the permitted endpoint matrix in `enterprise-architecture/governance/continuum/native-relationships.json` (`scripts/architecture_continuum.py:225-233`).
5. **Write Semantics & Concurrency Limits**: Workbench apply overwrites replaced element files in place via "write_text" (`scripts/continuum_workbench.py:141`). Preflight validation ensures validity prior to writes, but no filesystem transaction or concurrent-writer guarantee is claimed (`scripts/continuum_workbench.py:139-140`, `enterprise-architecture/design/archimate/README.md:49-50`). Retained review digests and source preimages support recovery.

---

## 7. Validation and Assurance

The Harmonia Enterprise Architecture governance framework implements a multi-layered, fail-closed validation and assurance architecture designed to verify element schema conformance, metamodel relationship semantics, XML projection fidelity, clean-room reproducibility, link resolution, and software architectural invariants [Observed].

### 7.1 Multi-Tiered Assurance Architecture

```text
┌─────────────────────────────────────────────────────────────────────────┐
│ Tier 1: Canonical Front-Matter & Markdown Structure                     │
│   ├── JSON schema validation (element.schema.json, 15 required fields)  │
│   ├── Heading match: # <displayName> == metadata.displayName            │
│   └── Mandatory ## Architectural content section                        │
├─────────────────────────────────────────────────────────────────────────┤
│ Tier 2: Metamodel Semantics & Relationship Integrity                    │
│   ├── Type catalogue profiles (type-catalogue.json) & Model.xsd drift   │
│   ├── Permitted endpoint matrix (native-relationships.json)             │
│   ├── Migration boundary rules (migration-boundary.json)                │
│   ├── Containment hierarchy and cycle rejection (project-time)          │
│   └── Information naming standards (project-time)                       │
├─────────────────────────────────────────────────────────────────────────┤
│ Tier 3: Deterministic Open Exchange XML Projection                      │
│   ├── Offline XML Schema validation (archimate3_Diagram.xsd)            │
│   ├── Byte-for-byte projection check (--check against baseline XML)    │
│   └── Retained line-end whitespace and character reference encoding     │
├─────────────────────────────────────────────────────────────────────────┤
│ Tier 4: Verification Checkers & Assurance Tooling                        │
│   ├── Standing: clean-room reproduction (verify_continuum_reproducibility)│
│   ├── Standing: rendered local Markdown link check (verify_continuum_links)│
│   ├── Migration-specific: source preservation (verify_continuum_sources)│
│   ├── Migration-specific: transport verifier (verify_continuum_transport)│
│   ├── Migration-specific: ARB reconciliation verifier                   │
│   ├── Historical: governance evidence verifier (verify_governance_evidence)│
│   └── Test runners & step scripts (test_runner.py, run-step*.sh)        │
├─────────────────────────────────────────────────────────────────────────┤
│ Tier 5: Java Software Architecture Invariants (ArchUnit)                │
│   ├── 11 architecture test classes (paradeigma-test/src/test/java/...)  │
│   ├── Module isolation: Paradeigma leaf simulation, Agora/Matrix        │
│   ├── API pure abstractions: Petasos zero JMS/Artemis leakage           │
│   ├── Presentation decoupling: Iris zero JPA/Hibernate/PostgreSQL       │
│   ├── Interoperability boundary: Pylai non-destructive projection       │
│   └── Package layering & Themis default-deny security evaluation        │
└─────────────────────────────────────────────────────────────────────────┘
```

### 7.2 Schema, Type Catalogue, and Metamodel Validation

The continuum validation engine separates core metadata validation (validate()) from projection-time tree evaluation (project()) in `scripts/architecture_continuum.py` [Observed]:

1. **Front-Matter JSON Schema Validation (validate())**:
   - Each element file is decoded from JSON front matter between --- delimiters (`scripts/architecture_continuum.py:79-86`); corpus traversal and expected layer/type-catalogue location checks occur in load() (`scripts/architecture_continuum.py:108-118`).
   - The front-matter payload is validated against `enterprise-architecture/governance/continuum/element.schema.json:6-22`, whose 15 required properties are schemaVersion, canonicalId, archimateType, specialization, displayName, friendlyName, canonicalName, standing, identity, naming, provenance, sourceBindings, exchange, relationships, and unresolvedProperties (`scripts/architecture_continuum.py:133-145`). qualifiedAssertions is optional and documentation is not a required top-level field.
   - The Markdown body must begin with a level-one heading matching the metadata display name, followed by a populated mandatory Architectural content section (`scripts/architecture_continuum.py:146-151`).
2. **Type Catalogue and Metamodel Profile Conformance (validate())**:
   - The element-path resolver checks the native type, specialization profile, native-type compatibility, and derived type/layer directory against `enterprise-architecture/governance/continuum/type-catalogue.json`; the loader rejects a file whose location differs (`scripts/architecture_continuum.py:93-105`, `scripts/architecture_continuum.py:108-118`).
   - Core validation separately compares catalogue native types and relationship types with the native ArchiMate Model XSD to detect metamodel drift (`scripts/architecture_continuum.py:133-140`).
3. **Relationship Endpoint Matrix Verification (validate())**:
   - Outbound relationships declared under the relationships field are validated against `enterprise-architecture/governance/continuum/native-relationships.json` (`scripts/architecture_continuum.py:139-140`, `scripts/architecture_continuum.py:225-233`).
   - The source native type and target native type must form a permitted combination for the specified relationship type (e.g. Composition, Aggregation, Assignment, Realization, Serving, Access, Association). Capability cannot assign behaviour, even though Assignment is an XSD type (`scripts/tests/test_continuum.py:105-111`).
4. **Migration Boundary Enforcement (validate())**:
   - `enterprise-architecture/governance/continuum/migration-boundary.json` is evaluated in `scripts/architecture_continuum.py:278-288` to prevent reuse of withdrawn unaccepted allocations, unreviewed representation rebindings, or unreviewed native retyping (`scripts/tests/test_continuum.py:62-70`).
5. **Projection-Time Containment and Information Naming Validation (project())**:
   - During projection, the containment resolver evaluates the full hierarchy and rejects invalid/circular containment findings (`scripts/architecture_continuum.py:401-406`, `docs/archimate/framework/tools/containment.py:1-120`, `scripts/tests/test_continuum.py:83-89`).
   - The information naming check verifies persisted naming values against proposed values for information elements (`scripts/architecture_continuum.py:401-414`, `docs/archimate/framework/tools/information_naming.py:1-60`, `scripts/tests/test_continuum.py:77-82`).
6. **Projection-Time XML Schema Validation (project())**:
   - During projection, generated Open Exchange XML is validated offline against `docs/archimate/framework/schemas/archimate3_Diagram.xsd` (which imports `docs/archimate/framework/schemas/archimate3_View.xsd` and `docs/archimate/framework/schemas/archimate3_Model.xsd`) (`scripts/architecture_continuum.py:389-391`).
   - XML identifier uniqueness and reference integrity are asserted in `scripts/architecture_continuum.py:392-398`.

### 7.3 Standing Verification Checkers vs Migration and Historical Tooling

The repository maintains standing Python verification scripts alongside migration-specific and historical assurance tooling (`enterprise-architecture/governance/continuum-representation.md:229-231`) [Documented], [Observed]:

1. **Standing Verification Checkers (Active / Routine Assurance)**:
   - **Isolated Clean-Room Reproducibility (`scripts/verify_continuum_reproducibility.py`)**: Re-creates an isolated staging directory (lines 18-44) containing maintained inputs: canonical Markdown elements, configuration schemas, Python sources, and root release file. It excludes historical capture/processing material (line 43), runs manifest, projection and publication generators with 90-second subprocess timeouts, then compares five published files plus the Continuum manifest byte-for-byte against the committed baseline (lines 36-42). The retained outcome in `enterprise-architecture/design/evidence/1.1.5/002/after/arb-reproducibility.json:1-17` is PASS for 1,602 canonical sources with zero identities allocated during the verifier run.
   - **Rendered Markdown Link Verification (`scripts/verify_continuum_links.py`)**: Parses element Markdown into HTML fragments and checks repository-local relative targets and fragments (`scripts/verify_continuum_links.py:13-29`). In the ARB evidence state, `enterprise-architecture/design/evidence/1.1.5/002/after/arb-link-validation.json:1-7` records PASS for 1,499 links, with no missing targets or unresolved anchors; `enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:46-51` repeats that outcome. The separate `enterprise-architecture/design/evidence/1.1.5/002/after/link-validation.json:1-7` records 1,018 links from an earlier state, not the ARB 1,602-element corpus. Both checks are local-only; external targets are not dereferenced.
2. **Migration-Specific and Transition Verifiers (Historical / Bounded Transition Scope)**:
   - **Source Succession & Range Preservation Verification (`scripts/verify_continuum_sources.py`)**: A migration-specific verifier that reads pinned source-succession records and uses git show to validate entry-source hashes and preservation of retired text ranges (`scripts/verify_continuum_sources.py:10-21`). Its output is `enterprise-architecture/design/evidence/1.1.5/002/after/source-preservation-validation.json:1-8`, which records PASS for 855 ranges across 57 files. `enterprise-architecture/design/evidence/1.1.5/002/after/corpus-accounting.json:17-18` is the related corpus inventory, not the verifier output.
   - **Archi Transport Verification (`scripts/verify_continuum_transport.py`)**: A parameterised verifier compares the canonical population and selected element fields/properties, checks relationship and View record sets for missing/additional/changed records, and requires zero workbench review changes (`scripts/verify_continuum_transport.py:13-33`). It does not hard-code the population counts; the ARB evidence reports those metrics separately. Notably, while the tool compares exported and re-imported XML/model data, this transport comparison cannot independently authenticate that an actual external Archi application execution occurred. Notably, while the tool compares exported and re-imported XML/model data, this transport comparison cannot independently authenticate that an actual external Archi application execution occurred. Notably, while the tool compares exported and re-imported XML/model data, this transport comparison cannot independently authenticate that an actual external Archi application execution occurred. Notably, while the tool compares exported and re-imported XML/model data, this transport comparison cannot independently authenticate that an actual external Archi application execution occurred.
   - **ARB Continuum Reconciliation Verification (`scripts/verify_arb_continuum_reconciliation.py`)**: Migration-specific verifier validating reconciliation rules applied during the RAD-1.1.5 transition.
3. **Historical Governance Evidence Verifier**:
   - `docs/archimate/framework/tools/verify_governance_evidence.py:1-149`: Historical one-off verifier created for RAD-1.0.14 namespace correction and governance tooling relocation under `docs/archimate/governance-tooling/`, ensuring historical links and file SHA256 checksums remained intact. Distinguished from active provenance tooling (`scripts/architecture_review_evidence.py`).
4. **Test Runners, Baseline Output Checkers, and Step Scripts**:
   - `scripts/test_runner.py:1-303` provides a general test harness with progress/stall handling, bounded subprocesses, and Surefire XML result collection; that harness is not itself an EA governance validation rule.
   - `docs/archimate/framework/tools/verify_baseline_outputs.py:1-38` is a legacy framework baseline-output reproduction checker; it invokes the historical synchroniser and report generator in temporary output and compares generated files.
   - `docs/archimate/framework/tools/verify_workspace.py:1-13` identifies itself as a bounded 1.0.7-to-1.0.8 migration proof and pins the 1.0.7 baseline SHA; it is historical/migration-specific, not a generic workspace-hygiene checker.
   - `scripts/run-step2-concurrency-test.sh` and `scripts/run-step3-verification.sh` are convergence-programme Maven runners (Mnemosyne PostgreSQL concurrency and ArchUnit respectively), not EA-continuum governance assurance gates.

### 7.4 Automated Software Architecture Suite (ArchUnit)

Software architectural invariants are continuously enforced via an ArchUnit test suite located in `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/` [Documented], [Tested]:

| ArchUnit Test Class | Enforced Architectural Invariant / Axiom | Verification Rule & Constraint |
| :--- | :--- | :--- |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/ParadeigmaIsolationArchitectureTest.java` | **Invariant 1: Paradeigma Production Isolation** (`AGENTS.md:405-416`) | Asserts zero production POM dependencies, zero production Java class imports, and zero simulation flags for net.fhirfactory.harmonia.paradeigma.* across all modules. |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PetasosApiIsolationArchitectureTest.java` | **Invariant 2: Petasos API Abstraction** (`AGENTS.md:418-429`) | Asserts zero org.apache.activemq.., jakarta.jms.., or javax.jms.. imports or API exposure in petasos-api. Payload treated as opaque streams. |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/IrisDecouplingArchitectureTest.java` | **Invariant 3: Iris Presentation Decoupling** (`AGENTS.md:431-446`) | Asserts zero JPA/Hibernate (jakarta.persistence.., org.hibernate..) or PostgreSQL driver dependencies in Iris presentation modules. |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/ProviderRegistryArchitectureTest.java` | **Invariant 3: Provider Registry Isolation** (`AGENTS.md:439-446`) | Asserts iris-administration SPA separation from server-side Provider Registry governance across four server-side directories (`pylai/pylai-fhir-registry`, `energeia/erga`, `themis/themis-core`, `hestia/mnemosyne-clinical`). |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PackageLayeringArchitectureTest.java` | **Selected Package-Layering Constraints** (`AGENTS.md:271-397`; `AGENTS.md:576-577`) | The implementation contains four rules: Calliope/model packages, Themis API, Petasos API, and Ponos/Praxis isolation from Artemis server packages (`paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PackageLayeringArchitectureTest.java:45-127`). This is not comprehensive layering enforcement across all nine subprojects. |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/PylaiPublicationBoundaryArchitectureTest.java` | **Invariant 9: External Interoperability Boundary (AX-05, AX-13)** (`AGENTS.md:519-535`) | Asserts Pylai external publication membrane encapsulation, non-destructive projection, and isolation from JPA/persistence and Ponos workflow internals. |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/AgoraIsolationArchitectureTest.java` | **Invariant 10: Agora / Matrix Collaboration Isolation** (`AGENTS.md:537-555`) | Asserts Ponos decoupling (zero Ponos dependencies; Petasos queue coordination), Matrix DTO encapsulation in agora-matrix, and Paradeigma isolation. |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java` | **Security, audit, logging, and perimeter assertions** (`AGENTS.md:473-501`; mapping is narrower than the full test class) | The 32-test class covers Themis contracts/security-context handling, Kleio audit boundaries, PHI-safe diagnostic behavior, CORS restrictions, and BEFE authorization perimeter checks; it is not limited to Invariant 6 and does not establish a general zero-caller-override rule (`paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/SecurityEnforcementArchitectureTest.java:71-1054`). |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/MnemosyneAuthoritativePersistenceArchitectureTest.java` | **Selected Mneme/Mnemosyne separation checks** (`AGENTS.md:503-517`; test-to-invariant mapping [Inferred]) | Checks that Mnemosyne persistence avoids Infinispan/Hot Rod, Mneme coordination, and ActiveStateToken dependencies; that AuthoritativePersistencePort exposes no DELETE/REMOVE methods; and that its contract does not leak JPA/entity types. It does not by itself prove sole authoritative-state establishment or all application bypass constraints (`paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/MnemosyneAuthoritativePersistenceArchitectureTest.java:39-43`, `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/MnemosyneAuthoritativePersistenceArchitectureTest.java:58-121`). |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteCompositionArchitectureTest.java` | **Governed-write composition checks** [Inferred; no GovernedWrite-specific mapping is stated in AGENTS.md] | Asserts selected class placement, dependency exclusions for governed access and HotRod convergence, and prohibited DELETE/REMOVE/PURGE method names; the test contains no Ergon activity-unit checks (`paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteCompositionArchitectureTest.java:39-45`, `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteCompositionArchitectureTest.java:58-161`). |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteContractArchitectureTest.java` | **Governed-write contract purity checks** [Inferred; no GovernedWrite-specific mapping is stated in AGENTS.md] | Checks selected domain contracts against Infinispan/Hot Rod, JPA/Hibernate, database-driver, HTTP/Web-framework, and physical deletion dependencies; not a general execution-invariant or activity-unit test (`paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteContractArchitectureTest.java:39-44`, `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteContractArchitectureTest.java:61-106`). |

- **Execution Profile**:
  - In the ARB validation run (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation/architecture-tests.txt:745-771`): **11 test classes, 90 tests, 0 failures, 0 errors, 0 skipped**. The Maven log’s reactor **Total time** is **18.096 seconds** at line 815, while the Paradeigma test-module line reports **16.114 seconds** at line 811; 18.096 seconds is not the test-execution duration alone.
  - The earlier intermediate-state record (`enterprise-architecture/design/evidence/1.1.5/002/after/manifest-validation.json:60-67`) reports a Maven **Total time** of **18.874 seconds**, from a different corpus/evidence state.

### 7.5 Evidence Layout and Provenance Validation

- `scripts/architecture_review_evidence.py`: Validates the task/report layout for an explicitly selected HCRL version and iteration (`scripts/architecture_review_evidence.py:12-21`), checks recorded Task-Issued-At and Report-Produced-At values and their sortable filename-prefix correspondence (`scripts/architecture_review_evidence.py:23-42`), and does not establish semantic coverage, authenticity, or acceptance (`scripts/architecture_review_evidence.py:3-7`).
- Distinct from historical evidence tools (`docs/archimate/framework/tools/verify_governance_evidence.py`), `scripts/architecture_review_evidence.py` serves as an explicitly invoked validator for task and report directory layouts and timestamp prefix consistency.

---

## 8. Evidence and Provenance

### 8.1 Normative Provenance Framework

The normative provenance framework is established by `enterprise-architecture/governance/architecture-change-provenance.md:1-322` and enforced through `AGENTS.md:194-230` (§1.4) and `enterprise-architecture/governance/architecture-review-praxis.md:1-275` [Documented]. It provides a representation-neutral contract for capturing task inputs, execution reports, and supporting verification artifacts across all AI agent and human architectural activities.

### 8.2 Documented Evidence Layout and Roles

`enterprise-architecture/governance/architecture-change-provenance.md:140-163` defines the iteration-scoped evidence structure [Documented]:

```text
enterprise-architecture/design/evidence/<RAD-version>/<HCRL-iteration>/
├── before/
│   └── ai-agent-tasks/
└── after/
    └── ai-agent-reports/
```

- **Input and Output Roles**: "before/" and "after/" represent activity-input and activity-output roles (`enterprise-architecture/governance/architecture-change-provenance.md:158-162`). They do not assert that a contained artifact existed in the predecessor or successor Git states, nor does directory placement confer architectural acceptance [Documented].
- **Supporting Artifacts**: Supporting validation logs, transport dumps, and corpus accounting files generated during an iteration reside under "after/" (e.g., `enterprise-architecture/design/evidence/1.1.5/002/after/arb-pdf-validation.json`, `enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json`) [Observed].

### 8.3 Temporal Provenance Contract

`enterprise-architecture/governance/architecture-change-provenance.md:179-218` and `AGENTS.md:212-224` define strict rules for temporal provenance [Documented]:

1. **Observable Event Definitions**:
   - "Task-Issued-At": The attributable system timestamp recording the operative instruction entering the agent interaction (`enterprise-architecture/governance/architecture-change-provenance.md:186-188`).
   - "Report-Produced-At": The attributable system timestamp recording emission of the completed Report (`enterprise-architecture/governance/architecture-change-provenance.md:188-190`).
   - Neither timestamp asserts earlier human composition time or internal model generation time (`enterprise-architecture/governance/architecture-change-provenance.md:190-192`).
2. **Timestamp Formatting and Precision**: Must use unambiguous ISO 8601 formatting with UTC 'Z' (or explicit offset), e.g., "2026-10-21T09:27:25.083Z" (`enterprise-architecture/governance/architecture-change-provenance.md:181-184`).
3. **Sortable Filename Prefixes**: Filenames must begin with a filesystem-safe sortable UTC timestamp derived from the event, followed by a descriptive name: "YYYYMMDDTHHMMSSZ-<descriptive-name>.md" (`enterprise-architecture/governance/architecture-change-provenance.md:196-198`).
4. **Preservation of Unavailable Times**: If an original event time cannot be established from source evidence, it must be recorded explicitly as UNRESOLVED for Solution Architect treatment before closure (`enterprise-architecture/governance/architecture-change-provenance.md:210-217`, `AGENTS.md:221-224`). Agents must never guess missing times from filesystem metadata, git commit times, or current system clocks.

### 8.4 Verification Machinery and Limits

Automated verification of iteration evidence is implemented in `scripts/architecture_review_evidence.py` [Documented], [Observed]:
- **Scope and Functionality**: Validates iteration directory structure, checks presence of "Task-Issued-At" / "Report-Produced-At" fields in Markdown headers, and verifies that filename prefixes correspond to recorded timestamps (`scripts/architecture_review_evidence.py:23-42`).
- **Declared Limitations**: As stated in `scripts/architecture_review_evidence.py:3-7` and line 86, the script performs "Layout and recorded event/prefix correspondence only; no semantic coverage, authenticity or acceptance claim" [Documented].
- **Exit Semantics**: Returns structured JSON with explicit outcome values (`scripts/architecture_review_evidence.py:83, 100`):
  - PASS (exit code 0): Layout and timestamp/prefix correspondences verified.
  - FAIL (exit code 1): Structural layout errors, malformed timestamps, or missing mandatory fields detected.
  - UNRESOLVED (exit code 2): Recorded timestamps contain UNRESOLVED, non-Markdown evidence is detected, or task/report correspondence requires human adjudication.

### 8.5 Durable Evidence vs Transient Working Material

The governance framework enforces a strict epistemic boundary between durable evidence and transient working material (`AGENTS.md:225-229`, `enterprise-architecture/governance/architecture-change-provenance.md:80-92`) [Documented]:
- **Transient Working Material**: Files under `.junie/plans/`, `.junie/reports/`, and local command logs are working artifacts. They are discardable when their immediate analytical purpose ends and carry zero architectural authority.
- **Durable Evidence**: Material retained under `enterprise-architecture/design/evidence/` because it materially supports, explains, reproduces, or verifies a governed HCRL transition (`enterprise-architecture/governance/architecture-change-provenance.md:86-92`).

---

## 9. Publication / Projection / Documentation Activities

The Harmonia governance framework maintains multi-format publication and projection engines that derive human-readable and machine-readable artifacts from canonical Markdown sources without allocating identities during publication [Observed].

### 9.1 Review Manifest Generation (`scripts/continuum_manifest.py`)

The Continuum manifest generator creates a single-point navigation index for the entire canonical corpus [Observed]:
- **Input Corpus**: render() validates and loads the full canonical corpus, then groups loaded elements for output (`scripts/continuum_manifest.py:40-53`); the ARB evidence inventory records 1,602 elements (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-reproducibility.json:1-4`).
- **Validation Precondition**: Validates the continuum corpus prior to generation via validate() (`scripts/continuum_manifest.py:41`).
- **Output Artifact**: Emits `enterprise-architecture/continuum/MANIFEST.md` structured by ArchiMate layer and native type.
- **Content Structure**: Renders tabular rows containing element display name, relative source link, canonical identity, standing/currentness and source qualifications (`scripts/continuum_manifest.py:40-60`).
- **Governance Role**: Explicitly declared as a generated navigation and review projection; carries no independent architectural authority (`enterprise-architecture/design/evidence/1.1.5/002/after/manifest-validation.json:69`).

### 9.2 ArchiMate 3.0 Open Exchange XML Model Projection (project subcommand in `scripts/architecture_continuum.py`)

The Open Exchange projection engine synthesizes a compliant ArchiMate 3.0 XML exchange model from canonical elements (`scripts/architecture_continuum.py:320-418`) [Observed]:
- **Template Synthesis**: Ingests `enterprise-architecture/design/archimate/projection-template.xml`, which defines model-level metadata, property definitions, folder hierarchies, and 1,209 pre-configured Views.
- **Identifier Preservation**: Projects canonical elements using their stored exchange identifier rather than allocating new UUIDs during projection (`scripts/architecture_continuum.py:320-330`).
- **Formatting and Whitespace Encoding**: Preserves source whitespace in element bodies and encodes retained line-end whitespace as XML numeric character references (`scripts/architecture_continuum.py:384-385`; `enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:70-95`).
- **Output Artifact**: Emits `enterprise-architecture/continuum/documentation/published/1.1.5/Harmonia-Architecture.xml` (exact size: **25,163,803 bytes** / 25.16 MB).
- **Validation**: Enforces structural validity against offline Open Exchange schemas rooted at `docs/archimate/framework/schemas/archimate3_Diagram.xsd`, which imports `docs/archimate/framework/schemas/archimate3_View.xsd` and `docs/archimate/framework/schemas/archimate3_Model.xsd`, via `scripts/architecture_continuum.py:389-391`. With the check option, asserts byte-for-byte freshness against the on-disk target without rewriting (`scripts/architecture_continuum.py:485-486`).

### 9.3 Interactive HTML Architecture Browser (`scripts/publish_continuum.py`)

`scripts/publish_continuum.py:20-80` produces a standalone, self-contained single-page architecture browser [Observed]:
- **Target Artifact**: `enterprise-architecture/continuum/documentation/published/1.1.5/continuum.html` (exact size: **10,043,810 bytes**).
- **Embedded Dataset**: Converts canonical element bodies to HTML fragments with the Markdown library and serializes the dataset into an embedded JSON block (`scripts/publish_continuum.py:44-75`).
- **Client-Side Capabilities**: Pure vanilla JavaScript provides real-time search across display names, canonical IDs, friendly names, and full-text Markdown bodies; facet filtering by native ArchiMate type/specialization and currentness state; and interactive cross-element navigation via custom data-element links (`scripts/publish_continuum.py:29-34`).
- **Zero-Dependency Architecture**: Operates entirely client-side without external web fonts, CDN dependencies, or server backends.

### 9.4 LaTeX Source Generation & Optional XeLaTeX PDF Catalogue

`scripts/publish_continuum.py:44-78` generates a complete LaTeX identity and standing catalogue [Observed]:
- **LaTeX Source Generation**: Emits `enterprise-architecture/continuum/documentation/published/1.1.5/continuum-catalogue.tex` (780,708 bytes), organizing the identity and standing catalogue and using fontspec/DejaVu Sans (`scripts/publish_continuum.py:50-78`).
- **XeLaTeX PDF Compilation**: An **optional, manual step** (`enterprise-architecture/governance/continuum-incorporation-praxis.md:101-103`): run XeLaTeX twice on the generated source in a temporary output directory. It is **not** a target in `docs/archimate/framework/Makefile:16-30` or `docs/archimate/framework/Makefile:83-87`, and is **not** compiled by `scripts/publish_continuum.py:82-90`. The recorded environment values, SOURCE_DATE_EPOCH=1792574845 and FORCE_SOURCE_DATE=1, come from `enterprise-architecture/design/evidence/1.1.5/002/after/arb-pdf-validation.json:3-7`, not the praxis.
- **Output Artifact**: `enterprise-architecture/continuum/documentation/published/1.1.5/continuum-catalogue.pdf` (649,557 bytes, 261 pages; SHA256 2c3c2e9459d2d596ea4ef63d431240c818dbd2d8346c6cf40f77d5979a027f10) as recorded in `enterprise-architecture/design/evidence/1.1.5/002/after/arb-pdf-validation.json:2-7`.
- **Declared Scope**: Explicitly scoped as an identity and standing catalogue for Solution Architect and ARB review; not a secondary narrative authority (`scripts/publish_continuum.py:50-52`).

### 9.5 Release Documentation and Publication Checksum Manifest

`scripts/publish_continuum.py:76-89` generates three documentation artifacts and a JSON record [Observed]:
- **Publisher Outputs**: The script writes only the HTML browser, README, LaTeX source, and publication.json; it does not generate the XML projection or compile the PDF (`scripts/publish_continuum.py:76-89`). The README is 303,415 bytes and publication.json is 843 bytes.
- **Publication Checksum Manifest**: `enterprise-architecture/continuum/documentation/published/1.1.5/publication.json` records SHA256 entries for exactly the HTML browser, README, and LaTeX source (`scripts/publish_continuum.py:76-78`; `enterprise-architecture/continuum/documentation/published/1.1.5/publication.json:13-17`). It does not hash the Open Exchange XML or PDF and has no allocatedIdentities field; allocatedIdentities is instead recorded as 0 by `enterprise-architecture/design/evidence/1.1.5/002/after/arb-reproducibility.json:12-17`.

### 9.6 Archi Interchange & Round-Trip Transport Evidence

Evidence of the operator-attributed Archi interchange is recorded in `enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:1-118` and summarized in `enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:60-75` [Observed]:
- **Operator and Environment**: The evidence names “Codex; installed Archi 5.10 commandline application with authorised local display access” (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:2-6`, `enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:115-117`). It records an initial gtk_init_check display failure followed by authorised local-display execution (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:117`).
- **Transport Metrics**: Evidence reports 1,602 semantic elements, 1,651 relationships, 1,209 Views, 4,005 visual objects, and 1,804 visual connections for both published and incoming models (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:11-58`); published XML is 25,163,803 bytes and incoming XML is 26,687,030 bytes (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:32-56`). These counts are evidence metrics, not hard-coded assertions in the parameterised verifier.
- **Verifier Basis**: The verifier requires equal canonical populations, equality of selected element fields and owned properties, no missing/additional/changed relationship or View records, and zero canonical workbench-review changes (`scripts/verify_continuum_transport.py:13-33`). The evidence records canonicalWorkbenchReview counts 0/0/0 (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:105-109`), providing the bounded basis for its PASS.
- **Raw Comparator Results**: The comparator status remains DIFFERENCES. Its raw classifications include 312 semantically equivalent normalisations, 4,917 information-loss findings, 13,151 semantic differences, and 2,781 unexplained differences; the evidence retains those categories rather than collapsing them into equivalence (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:86-103`).
- **Returned Artifact and Limits**: The ARB return is `enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/archi-transport/returned-model.xml.gz`, with its execution log at `enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/archi-transport/transport.txt` (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:6`, `enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:115-117`). This differs from the earlier `enterprise-architecture/design/evidence/1.1.5/002/after/archi-transport/returned-model.xml.gz` artifact. The evidence expressly does not claim whole-model byte fidelity; it notes Archi-added English language attributes and organization-navigation restructuring, and does not assert visual inspection or architectural acceptance (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:110-113`).

---

## 10. Human and Agent Decision Points

### 10.1 Human and Organizational Authority Roles

`enterprise-architecture/governance/architecture-governance.md:65-80` defines the division of authority across human, forum, and agent roles [Documented]:

| Role | Entity / Mechanism | Authority Boundary & Governance Mandate |
| :--- | :--- | :--- |
| **Solution Architect** | Mark Hunter | **Final Architectural Authority**: Sole authority for confirming, accepting, or rejecting architectural changes, approving HCRL register completions, and issuing binding design directions (`enterprise-architecture/governance/architecture-governance.md:67-71`). |
| **Architecture Review Board (ARB)** | ARB Forum | **Collaborative Review & Adjudication Forum**: Collaborative review body providing peer review, evaluating candidate proposals, and adjudicating semantic classifications; participation does not imply an independent Board approval mechanism (`enterprise-architecture/governance/architecture-governance.md:67-71`). |
| **Peer Reviewer** | ChatGPT | **Collaborative Peer / Reviewer**: May challenge, recommend, concur or dissent, but cannot independently approve (`enterprise-architecture/governance/architecture-governance.md:73-74`). |
| **Co-Executor / Agent** | Codex / Junie | **Execution & Reporting (Zero Authority)**: Executes bounded implementation and analysis tasks; operates strictly within assigned scopes; possesses zero authority to approve architecture, confirm closure, or manufacture decisions (`enterprise-architecture/governance/architecture-governance.md:74-76`, `AGENTS.md:56-81, 656-666`). |
| **Deterministic Tooling** | Python validation scripts, ArchUnit suites | **Deterministic Verification**: Evaluates syntax, schema conformance, relationship integrity, and structural isolation rules; cannot adjudicate semantic intent (`scripts/architecture_continuum.py:125-300`). |

### 10.2 Agent Guardrails and Operating Boundaries

`AGENTS.md` establishes strict operational constraints for AI agents [Documented]:

1. **Epistemic Discipline and Uncertainty Preservation (AX-17)**:
   - When repository authority is ambiguous, conflicting, or absent, agents must report and preserve the uncertainty under AX-17 rather than inventing solutions or guessing intent (`AGENTS.md:76-78`).
   - Where an existing implementation conflicts with an Architectural Axiom, the conflict must be reported rather than silently resolved in favor of the code (`AGENTS.md:43-46`). Existing implementation is not evidence of intent (`AGENTS.md:48-49`).

2. **Context Independence and Fresh Context Default (§1.1)**:
   - Substantive tasks must start assuming fresh AI context (`AGENTS.md:58-62`).
   - Agents must rely on repository-held authority and explicit task instructions rather than remembered or previous-session interpretations (`AGENTS.md:64-70`).

3. **Reconciliation Preceding Incorporation (§1.2)**:
   - Architectural content discovered in legacy documentation cannot be copied directly into RADS/Continuum sources; it must be reviewed and adjudicated first: "legacy-doco -> architectural review/adjudication -> RADS" (`AGENTS.md:127-136`).

4. **Identity Allocation Boundary**:
   - **Canonical Population Allocation Breakdown**: Static inspection of the 1,602 canonical element files in `enterprise-architecture/continuum/` reveals three distinct allocation histories [Observed]:
     - 1,394 elements: Preserved existing resource-owned UUID32 identities without reallocation (`identity.allocation: "Preserved existing resource-owned UUID32; no allocation in migration"`).
     - 165 elements: Ingested during the RAD-1.1.5 migration Task via direct calls to `uuid.uuid4().hex` in `scripts/ingest_rad_definitions.py:50` (`identity.allocation: "UUIDv4 allocated once under the explicit RAD-1.1.5 migration Task"`).
     - 43 elements: Allocated via calls to `c.create()` in `scripts/apply_arb_designnote_assertions.py:68` (`identity.allocation: "UUIDv4 allocated once by explicit create"`).
   - **Prospective Allocation Path**: `scripts/architecture_continuum.py:425-453` defines `create()`, which allocates a UUIDv4 at line 442 ("canonicalId") and requires attributable authority and provenance at line 429 [Observed]. `enterprise-architecture/governance/continuum-incorporation-praxis.md:32` instructs allocation through `create`, and `enterprise-architecture/governance/continuum-representation.md:133` allows explicitly authorised creation/ingestion, so whether `create()` is exclusive remains an open ambiguity [Documented].
   - **Exchange Identifiers**: `create()` derives the exchange identifier deterministically via UUIDv5 when creating a new element (`scripts/architecture_continuum.py:420-422, 445`). `project()` emits the identifier already stored in `m['exchange']['identifier']`; it does not derive a new identifier during projection (`scripts/architecture_continuum.py:320-330`). Migrated elements retain their existing exchange identifiers: AX-01, for example, stores "id-e6d709af40515f9a9d6d3e65a42cd004" (`enterprise-architecture/continuum/motivation/principle/e9bd61acaaea4577bc4086a5639e52c3.md:58-60`) [Observed].

5. **Bounded Execution Rules (§5.1)**:
   - Commands, tests, and builds must use bounded waiting: ~2 minutes for unit/arch tests, ~5 minutes for integration builds, and a hard maximum of 10 minutes without observable progress (`AGENTS.md:623-654`).
   - Result semantics must never be collapsed: PASS, FAIL, TIMEOUT, STALLED, and UNRESOLVED remain distinct diagnostic outcomes (`AGENTS.md:648-654`).

6. **Scope Discipline and Master Plan Sequencing (§6.2–§6.4)**:
   - Implementation work governed by `docs/implementation/harmonia-convergence-runtime-integration-plan.md` operates under the strict rule: "One plan. One current milestone. One next step." (`AGENTS.md:731-733`).
   - Speculative implementation across milestones is strictly prohibited (`AGENTS.md:735-758`).

---

## 11. Scope and Cost Characteristics

Static inspection of evidence records and governance scripts enables quantitative analysis of computational costs, execution durations, corpus scales, and unit-of-work scope expansion cascades without requiring live whole-system execution [Observed].

### 11.1 Empirical Cost and Duration Metrics across Three Evidence States

Existing evidence artifacts in `enterprise-architecture/design/evidence/1.1.5/002/after/` record precise timing benchmarks, test suite sizes, and failure states across three distinct historical and reconciliation progression states [Observed]:

1. **State 1: Post-initial-migration validation (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json`)**:
   - This record is validation after the initial migration was applied. Only the `"historicalFrameworkAtEntry"` object (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:16-22`) refers to task entry.
   - **Continuum pipeline tests**: Recorded as PASS for 11 tests with `"testDurationSeconds"` **53.154** (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:2-8`). The recorded command is `make -C docs/archimate/framework all`; make wall-clock time is unrecorded and remains UNRESOLVED.
   - **ArchUnit Architecture Suite**: Recorded as PASS for 90 tests in **18.318 seconds** under `mvn test -pl paradeigma/paradeigma-test -am -Dtest=*ArchitectureTest ...` (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:9-15`).
   - **Historical Framework Suite at Entry**: Recorded as **FAIL** with 251 tests run and **10 errors** (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:16-22`). The recorded explanation is "Current baseline authority drift; reconcile explicitly: AGENTS.md. Present at task entry, before this migration. Historical pins preserved."
   - **Shared Semantics Suite**: Recorded as **FAIL** with 45 passed tests and **2 setup errors** (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:23-29`), citing two historical publication fixture classes hitting the same `AGENTS.md` authority pin.
   - **Final Continuum Tests**: Recorded as PASS for 11 tests in **55.097 seconds** (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:39-44`).
   - **PDF Compilation**: 255 pages compiled via XeLaTeX (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:30-37`).
   - **Evidence Path Note**: Referenced log paths (such as `"validation/continuum-checks.log"` and `"validation/architecture-tests.log"`) cite an unpersisted `"validation/"` directory rather than existing filesystem paths (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:7,13,21,28,43`). See `F-14-09`.

2. **State 2: Intermediate 1,559-Element Corpus Projection (`enterprise-architecture/design/evidence/1.1.5/002/after/manifest-validation.json`)**:
   - **Corpus Scale**: 1,559 canonical elements and manifest rows (`enterprise-architecture/design/evidence/1.1.5/002/after/manifest-validation.json:5-7`).
   - **Continuum Unit Tests**: Recorded as PASS for 16 tests in **75.256 seconds** under `python3 -m unittest discover -s scripts/tests -p 'test_continuum*.py' -v` (`enterprise-architecture/design/evidence/1.1.5/002/after/manifest-validation.json:43-48`).
   - **ArchUnit Architecture Suite**: Recorded as PASS for 90 tests in **18.874 seconds** (`enterprise-architecture/design/evidence/1.1.5/002/after/manifest-validation.json:60-67`).
   - **Historical Suite Handling**: Explicitly noted that earlier historical-suite authority-pin failures remained recorded in original migration evidence and were not rerun or rewritten (`enterprise-architecture/design/evidence/1.1.5/002/after/manifest-validation.json:70`).

3. **State 3: Final 1,602-Element Reconciliation Corpus (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json` & `enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation/`)**:
   - **Corpus Scale**: 1,602 canonical elements, 1,651 relationships, and 1,209 Views (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:64`).
   - **Continuum Pipeline Unit Tests**: `enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation/continuum-tests-and-generation.txt:8-33` records that running 20 unit tests via `python3 -m unittest discover -s ../../../scripts/tests -p 'test_continuum*.py' -v` took **97.215 seconds** (cited at `enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:3-10`). The total wall-clock time for the full `make -C docs/archimate/framework all` execution was unrecorded and remains UNRESOLVED.
   - **ArchUnit Architecture Suite**: Executed 90 tests in **18.096 seconds** Maven reactor total time (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation/architecture-tests.txt:815`, cited at `enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:17-30`).
   - **PDF Compilation**: 261 pages compiled via XeLaTeX (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-pdf-validation.json:1-8`).
   - **Historical Suite Handling**: Historical suites were not rerun (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:72-76`).

4. **Makefile Process Timeout Caps (`docs/archimate/framework/Makefile`)**:
   - Enforced hard process timeouts via `timeout --signal=TERM --kill-after=5s <N>s`:
     - **90s Hard Cap**: Manifest generation/check, projection generation/check, HTML/LaTeX publication/check (`docs/archimate/framework/Makefile:19-27`), baseline sync/check (`docs/archimate/framework/Makefile:36-40`), and recovery/check (`docs/archimate/framework/Makefile:50-54`).
     - **120s Hard Cap**: Continuum unit tests (`docs/archimate/framework/Makefile:29-30`) and baseline-independent checks (`docs/archimate/framework/Makefile:45-46`).
     - **420s Hard Cap**: Historical framework test suite (`docs/archimate/framework/Makefile:83-84`).
     - **30s Hard Cap**: Legacy assessment, identity inventory, adoption, bindings, check, and report targets (`docs/archimate/framework/Makefile:42-44, 56-58, 62-82, 86-87`).
   - These timeout bounds implement the non-blocking execution guardrails mandated by `AGENTS.md:623-646` (§5.1).

### 11.2 Corpus Scale and Population Metrics

Static inspection of the repository reveals the concrete scale of the Harmonia Enterprise Architecture corpus [Observed]:
- **Canonical Elements**: **1,602** individually represented Markdown element files under `enterprise-architecture/continuum/` across 4 populated ArchiMate layer directories (`enterprise-architecture/continuum/business/`, `enterprise-architecture/continuum/application/`, `enterprise-architecture/continuum/motivation/`, `enterprise-architecture/continuum/strategy/`).
- **Native ArchiMate Relationships**: **1,651** outbound relationship assertions embedded in canonical front-matter (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:64`).
- **Qualified Source Predicates**: **462** source predicates tracked under "qualifiedSourcePredicates" (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation/continuum-tests-and-generation.txt:5`).
- **ArchiMate Diagram Views**: **1,209** pre-configured Views defined in `enterprise-architecture/design/archimate/projection-template.xml` and projected into `enterprise-architecture/continuum/documentation/published/1.1.5/Harmonia-Architecture.xml`.
- **Rendered Local Hyperlinks Checked**: **1,499** relative links validated across element bodies with zero broken links or unresolved anchors (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-link-validation.json:1-7`, `enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:48-52`), distinguished from 1,018 links in earlier evidence (`enterprise-architecture/design/evidence/1.1.5/002/after/link-validation.json:1-7`).
- **Legacy Documentation Corpus Processed**: **179** legacy Markdown files processed during RAD-1.1.5 migration, with **855** retired sections wrapped in supersession markers across **57** source files (`enterprise-architecture/design/evidence/1.1.5/002/after/corpus-accounting.json:3, 17-18`, `enterprise-architecture/design/evidence/1.1.5/002/after/source-preservation-validation.json:1-8`).
- **Published and Returned Interchange XML Scales**: Published Open Exchange XML is **25,163,803 bytes** and incoming returned XML from Archi is **26,687,030 bytes** (`enterprise-architecture/design/evidence/1.1.5/002/after/arb-transport/validation.json:32-56`). Published interactive HTML browser is **10,043,810 bytes** (`enterprise-architecture/continuum/documentation/published/1.1.5/continuum.html`).

### 11.3 Scope Expansion and Cascade Dynamics

The architecture governance framework exhibits significant unit-of-work scope expansion where localized element changes trigger whole-solution re-evaluation, while distinguishing code-enforced checks from explicitly invoked validation [Observed]:

1. **Local Mutation to Global Projection Invalidation**:
   - Modifying a single canonical element file (e.g. updating a rationale paragraph in `enterprise-architecture/continuum/motivation/principle/e9bd61acaaea4577bc4086a5639e52c3.md`) alters the whole-model projection baseline hash ("baselineSha256").
   - Because projection and publication tooling operates on the entire corpus rather than incremental diffs, this single edit invalidates existing projections.
2. **Code-Enforced Scans during Generation / Build**:
   - Regenerating `enterprise-architecture/continuum/MANIFEST.md` triggers `scripts/continuum_manifest.py:40-62` to scan and parse front-matter for all 1,602 elements.
   - Re-synthesizing `enterprise-architecture/continuum/documentation/published/1.1.5/Harmonia-Architecture.xml` via `scripts/architecture_continuum.py:320-418` parses all 1,602 elements, evaluates 1,651 relationships and 1,209 Views, executes containment tree and information naming checks, and validates against offline XML schemas.
   - Re-publishing via `scripts/publish_continuum.py:20-89` scans all 1,602 elements, transforms Markdown bodies to HTML, and writes the 10,043,810-byte HTML browser, README navigation tables, LaTeX catalogue, and three-entry `enterprise-architecture/continuum/documentation/published/1.1.5/publication.json`.
3. **Documented Per-Change Obligations (Not Code-Automatic)** [Documented]:
   - After authorised element changes, `enterprise-architecture/governance/continuum-incorporation-praxis.md:94-100` instructs running `make -C docs/archimate/framework all` for current publication, the root review manifest, mutation tests and staleness checks, and running `scripts/verify_continuum_reproducibility.py` to prove maintained-input independence.
   - `AGENTS.md:559-561` requires ArchUnit verification for every repository modification against `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/`.
   - These are documented obligations. No automatic post-edit chain invokes them; they remain distinct from the code-enforced scans in item 2 and from the optional steps in item 4.
4. **Optional / Operator-Driven Steps**:
   - **Corpus Link Validation**: `scripts/verify_continuum_links.py:1-33` is an explicitly invoked script that parses all 1,602 element Markdown bodies and validates 1,499 relative links and URL fragments.
   - **PDF Compilation**: Compiling `enterprise-architecture/continuum/documentation/published/1.1.5/continuum-catalogue.pdf` is an optional, manual XeLaTeX command (`enterprise-architecture/governance/continuum-incorporation-praxis.md:101-103`) not executed by the Makefile or publishing scripts.
   - **Archi Interchange Transport**: Operator-driven execution using the Archi 5.10 CLI with local display access.
5. **Absence of Incremental Delta Caching**:
   - The tooling lacks delta compilation, partial view re-projection, or dependency-directed caching. Every publication run performs an $O(N)$ whole-corpus synthesis.
6. **Routine Incremental Updates vs Migration Scope**:
   - **Crucial Distinction**: While major historical migration milestones (such as RAD-1.1.5 HCRL-002) executed full-suite transport proofs, XeLaTeX recompilation, and legacy test runs, routine incremental changes under governed praxis do not require re-executing costly historical test suites (such as the 420-second historical test suite in `docs/archimate/framework/Makefile:83-84`). No automatic chain exists from a local element edit to whole-solution validation and publication.

---

## 12. Migration-Specific or Historical Machinery

The repository contains specialized tooling developed for historical RAD transitions and the RAD-1.1.5 Continuum migration. These tools serve historical recovery, standing verification, and migration transformation purposes [Observed].

### 12.1 RAD-1.1.5 Continuum Migration and Verification Scripts (`scripts/`)

The migration of Harmonia architecture into the canonical file-based Continuum was executed via a suite of bounded migration adapters in `scripts/` [Observed]:

1. **Initial Ingestion Adapters**:
   - `scripts/ingest_rad_continuum.py`: Extracted baseline JSON resource captures (`docs/markdown/governance/baseline/`) and the 1.1.0 Open Exchange XML model into individual canonical Markdown element files under `enterprise-architecture/continuum/` (`scripts/ingest_rad_continuum.py:46-162`). Synthesized `enterprise-architecture/design/archimate/projection-template.xml` and `enterprise-architecture/governance/continuum/migration-boundary.json`.
   - `scripts/ingest_rad_assertions.py`, `scripts/ingest_rad_contributions.py`, `scripts/ingest_rad_definitions.py`: Ingested fine-grained source definitions and contributions from legacy documentation into element bodies.
2. **ARB Semantic Reconciliation Adapters**:
   - `scripts/apply_arb_continuum_reconciliation.py`: Applied HCRL 1.1.5-002 ARB semantic reconciliation directives (`enterprise-architecture/design/evidence/1.1.5/002/before/arb-reconciliation-plan.json`), linking capability lineages ("identity.correspondence") and materializing native relationship mappings (`scripts/apply_arb_continuum_reconciliation.py:38-149`). Correspondences are stable on retry (line 144).
   - `scripts/apply_arb_designnote_assertions.py`: Ingested normative assertions from Domain 05 DesignNotes into canonical elements via `c.create()` (line 68).
   - `scripts/apply_arb_remaining_sources.py`: Processed remaining source assertions and native relationships.
3. **Source Succession and Retirement Scripts**:
   - `scripts/continuum_source_succession.py`: Rebased Markdown links within canonical elements and applied supersession markers ("<!-- continuum-supersession:start -->") to legacy documentation (`scripts/continuum_source_succession.py:19-20, 114-151`).
   - `scripts/complete_rad_continuum_sources.py`: Ingested exact source range locators from `docs/markdown/governance/baseline/architecture.json` into canonical elements (`scripts/complete_rad_continuum_sources.py:17-83`).
   - `scripts/finalize_continuum_succession.py`: Finalized succession records in `enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json`, updated `enterprise-architecture/design/evidence/1.1.5/002/after/corpus-accounting.json`, generated the Axioms navigation index at `enterprise-architecture/governance/architectural-axioms.md`, prepended succession banners to every mapped non-baseline source (line 18; skipped if already present) and wrapped baseline carriers under `docs/markdown/governance/baseline/` (line 24). These writes occur before any validation and without checking the original bytes; the script also depends on `git show` (`scripts/finalize_continuum_succession.py:6-38`).
4. **Standing Verification and Migration Checks**:
   - `scripts/verify_continuum_reproducibility.py` and `scripts/verify_continuum_links.py`: Perform standing verification checks across the continuum corpus to assert clean-room projection reproducibility and link integrity (`enterprise-architecture/governance/continuum-representation.md:229-231`) [Documented].
   - `scripts/verify_arb_continuum_reconciliation.py`, `scripts/verify_continuum_sources.py`, `scripts/verify_continuum_transport.py`: Verified migration invariants, source boundaries, and transport fidelity during the RAD-1.1.5 transition.
5. **Operating Mandate and Tool Standing**:
   - `enterprise-architecture/governance/continuum-representation.md:236-243` explicitly establishes the standing of source ingestion scripts [Documented]:
     > *"The one-time source ingestion scripts are bounded migration adapters. Their allocated identity results are persisted. Routine regeneration never reruns UUID allocation or prose extraction... Do not use these as future classifiers."*
   - Ingestion scripts represent bounded historical migration machinery (`[Historical]`), while verification scripts serve as standing assurance checks (`[Documented]`).

### 12.2 ArchiMate Framework Tools (`docs/archimate/framework/tools/`)

Tools under `docs/archimate/framework/tools/` comprise two distinct functional categories [Observed]:

1. **Active Continuum Pipeline Dependencies**:
   - The prospective Continuum engine actively imports core logic from `docs/archimate/framework/tools/` at runtime:
     - `scripts/architecture_continuum.py` imports `docs/archimate/framework/tools/governed_metadata.py`, `docs/archimate/framework/tools/generate_views.py`, `docs/archimate/framework/tools/containment.py`, `docs/archimate/framework/tools/information_naming.py`, and `docs/archimate/framework/tools/diagnose_roundtrip.py` (lines 166-172, 388-390, 401-403).
     - `scripts/continuum_workbench.py` imports `docs/archimate/framework/tools/change_review.py` (lines 47-48) and copies the entire tools directory into its temporary staging tree during apply (line 124).
     - Through these modules, tools such as `docs/archimate/framework/tools/workspace_paths.py`, `docs/archimate/framework/tools/home_relationships.py`, `docs/archimate/framework/tools/relationship_state.py`, and `docs/archimate/framework/tools/element_correlation.py` are imported at module level by the active pipeline. Other historical modules are co-located in `docs/archimate/framework/tools/` and are only lazily imported by functions the live pipeline does not call.
2. **Historical Baseline and Recovery Replay Tools**:
   - A distinct subset of framework tools is retained strictly for replaying historical RAD editions (1.0.4 through 1.1.0) against legacy release fixtures (`docs/archimate/framework/Makefile:32-34, 59-60`) [Historical]:
     - `docs/archimate/framework/tools/reconcile_108.py`: Replays accepted 1.0.8 changes against legacy XML models (`docs/archimate/framework/tools/reconcile_108.py:1-120`).
     - `docs/archimate/framework/tools/recover_v02.py`: Recovers pinned LaTeX RAD v0.2 snapshot models (`docs/archimate/framework/tools/recover_v02.py:1-120`).
     - `docs/archimate/framework/tools/synchronise_baseline.py`: Replays baseline synchronization for historical capability models (`docs/archimate/framework/tools/synchronise_baseline.py:1-120`).
     - `docs/archimate/framework/tools/publish_navigation.py`, `docs/archimate/framework/tools/publish_information_release.py`: Historical 1.0.4 XML and documentation publishers (`docs/archimate/framework/Makefile:60-77`).
     - `docs/archimate/framework/tools/governance_history.py`: Validates transition hash chains across historical release manifests (`docs/archimate/framework/tools/governance_history.py:1-38`).
     - `docs/archimate/framework/tools/home_context.py`, `docs/archimate/framework/tools/information_population.py`, `docs/archimate/framework/tools/information_naming_run.py`, `docs/archimate/framework/tools/information_workspace_run.py`: Specialized legacy workspace processing scripts.

---

## 13. Duplication, Coupling and Overlap

Static inspection of the governance framework identifies specific areas of technical coupling, functional duplication, and architectural overlap across scripts, schemas, and documentation [Observed].

| Finding ID | Status Classification | Finding Title | Technical Description & Repository Evidence |
| :--- | :--- | :--- | :--- |
| `F-13-01` | `[Observed]` | Dynamic "sys.path" Runtime Injection Coupling Root Scripts to Framework Tools | `scripts/architecture_continuum.py` (lines 166, 171, 388, 401–403) and `scripts/continuum_workbench.py` (line 47) dynamically insert `docs/archimate/framework/tools` into "sys.path" to import "governed_metadata", "generate_views", "containment", "information_naming", "diagnose_roundtrip", and "change_review". This creates tight runtime coupling where the prospective Continuum engine depends on unmodularized legacy framework scripts located in a documentation subfolder. |
| `F-13-02` | `[Observed]` | Namespace Duplication and Legacy Identity Script Reachability | `scripts/architecture_identity.py` (827 lines) allocates no identities; writes inventory/validation outputs (`docs/markdown/governance/architecture-identity/inventory.json`, `docs/markdown/governance/architecture-identity/inventory.md`, `docs/markdown/governance/architecture-identity/bindings-validation.json`, `docs/markdown/governance/architecture-identity/adoption-validation.json`); reachable via the "legacy-all", "recovery-all", and "test" paths and a standalone unit test. Both `scripts/architecture_identity.py` and `scripts/architecture_continuum.py` independently define `SEMANTIC_NAMESPACE = uuid.uuid5(uuid.NAMESPACE_URL, 'urn:harmonia:architecture:semantic:v1')` and identical `semantic_id()` helper functions (`scripts/architecture_identity.py:22, 40-43` vs `scripts/architecture_continuum.py:37, 420-422`). |
| `F-13-03` | `[Observed]` | Functional Coupling between Continuum Workbench and Legacy Round-Trip Tools | `scripts/continuum_workbench.py` implements hash-bound review and application for canonical Markdown elements, while `docs/archimate/framework/tools/roundtrip_praxis.py` and `docs/archimate/framework/tools/change_review.py` implement round-trip discovery, observation, and signature comparison for XML exchange models. `scripts/continuum_workbench.py` (42-51) imports and calls the signature function of change_review to implement signature extraction, demonstrating runtime coupling rather than functional duplication. |
| `F-13-04` | `[Observed]` | Coexistence of Active Continuum and Historical Pipelines in Framework Makefile | `docs/archimate/framework/Makefile` mixes active Continuum build targets ("continuum", "continuum-test", "continuum-check" invoking `scripts/architecture_continuum.py`, `scripts/continuum_manifest.py`, `scripts/publish_continuum.py`) with legacy publication and recovery targets ("historical-baseline-all", "recovery-all", "legacy-all" invoking tools in `docs/archimate/framework/tools/`). The default target "all" executes the Continuum pipeline, but legacy targets remain exposed. |
| `F-13-05` | `[Observed]` | Fragmented Schema and Metamodel Configuration Storage | Governance schemas and configuration profiles are dispersed across multiple repository locations without a unified directory structure: `enterprise-architecture/governance/continuum/element.schema.json`, `enterprise-architecture/governance/continuum/type-catalogue.json`, `enterprise-architecture/governance/continuum/native-relationships.json`, `docs/archimate/framework/schemas/archimate3_Diagram.xsd`, and `docs/markdown/governance/architecture-identity/registry.schema.json`. |
| `F-13-06` | `[Observed]` | Dual Representation of Architectural Axioms | The 18 Architectural Axioms exist simultaneously as individual canonical ArchiMate "Principle" elements in `enterprise-architecture/continuum/motivation/principle/`, as a Markdown navigation index in `enterprise-architecture/governance/architectural-axioms.md`, and as superseded legacy definitions with wrapper markers in `docs/markdown/governance/architectural-axioms.md`. |

---

## 14. Observed Ambiguities / Inconsistencies

Static inspection of normative governance contracts, schemas, scripts, and documentation across the repository reveals specific technical ambiguities, inconsistencies, and unretired legacy references [Observed].

| Finding ID | Status Classification | Finding Title | Technical Description & Repository Evidence |
| :--- | :--- | :--- | :--- |
| `F-14-01` | `[Observed]` | Axioms Authority Referent Divergence Across Governance Sources | Repository sources diverge on the canonical authority referent for Architectural Axioms: `enterprise-architecture/governance/architectural-axioms.md:5` states that the individually represented Principle elements in `enterprise-architecture/continuum/motivation/principle/` are highest authority; `AGENTS.md:28-38` points to `enterprise-architecture/governance/architectural-axioms.md`; `enterprise-architecture/governance/architecture-governance.md:51-52` links `docs/markdown/governance/architectural-axioms.md`; and individual Principle front-matters (e.g. `enterprise-architecture/continuum/motivation/principle/e9bd61acaaea4577bc4086a5639e52c3.md:44-50`) link `docs/markdown/governance/architectural-axioms.md` with role `"current governed definition"`. |
| `F-14-02` | `[Documented]` | Holistic Acceptance of Continuum Model Formally Pending | While the Continuum operates as the active working repository for architectural elements, its formal holistic acceptance remains pending: DR-2 and DR-5 of `enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md:6,9` record "holistic acceptance pending" and "ARB review in progress", and `AGENTS.md:98-99` confirms "holistic acceptance remains pending". |
| `F-14-03` | `[Observed]` | Praxis Description Drift vs Continuum Workbench Implementation | `docs/markdown/governance/archimate-round-trip-engineering-praxis.md:1` includes an active adapter banner stating that the continuum workbench implements the exact-baseline, review and application gates for individual canonical Markdown resources, and that historical source-adapter instructions remain applicable only to their originating editions. The Discover-through-Accept stage table at lines 76–82 still describes whole-model discovery, Change Request generation, and a real complete-model Archi round trip (`docs/markdown/governance/archimate-round-trip-engineering-praxis.md:81`) without naming a publication directory. The historical published-package locator `docs/archimate/exchange/publish/` is inferred via `docs/archimate/framework/tools/workspace_paths.py:13`; that directory currently contains edition trees `docs/archimate/exchange/publish/1.0.4`, `docs/archimate/exchange/publish/1.0.6`, `docs/archimate/exchange/publish/1.0.7`, `docs/archimate/exchange/publish/1.0.8`, `docs/archimate/exchange/publish/1.0.9`, `docs/archimate/exchange/publish/1.0.10`, and `docs/archimate/exchange/publish/1.1.0`, and does not contain a 1.1.5 tree. Active round-trip operations use `scripts/continuum_workbench.py` on canonical Markdown files and structured decision JSON without multi-file CR documents. Whether the complete-model Archi round trip at praxis line 81 applies to incremental element changes remains `[Inferred]`/`[Uncertain]`. |
| `F-14-04` | `[Documented]` | Active Governance Contracts Located Under Legacy Documentation Path | Active governed contracts `docs/markdown/governance/architecture-release-policy.md` and `docs/markdown/governance/archimate-round-trip-engineering-praxis.md` reside under `docs/markdown/governance/` without supersession wrappers. Sibling `docs/markdown/governance/architectural-axioms.md` uses section-level supersession wrappers around individual axioms (partial section succession banner plus per-axiom wrappers), not a whole-document superseded copy. Sibling `docs/markdown/governance/architecture-change-provenance.md` has a current-authority header followed by a whole-document legacy wrapper at `docs/markdown/governance/architecture-change-provenance.md:17-347`. This mixed status within a single directory creates potential confusion regarding whether files under `docs/markdown/governance/` are active or legacy. |
| `F-14-05` | `[Historical]` | Historical 3-File Archival Instructions in Superseded Provenance Document | `docs/markdown/governance/architecture-change-provenance.md:57-64` contains historical text describing a 3-file archive package ("before.tar.gz", "after.tar.gz", "change-review.md"). This text is enclosed in an explicit supersession wrapper (`docs/markdown/governance/architecture-change-provenance.md:17-347`) stating it is superseded by `enterprise-architecture/governance/architecture-change-provenance.md`. It represents a historical navigation hazard rather than an active conflicting mandate. |
| `F-14-06` | `[Documented]` | Open Ambiguity on Canonical Identity Allocation Exclusivity | `enterprise-architecture/governance/continuum-incorporation-praxis.md:32` instructs allocation via `scripts/architecture_continuum.py create`, whereas `enterprise-architecture/governance/continuum-representation.md:133` allows an "explicitly authorised creation or ingestion operation" to allocate identities. Whether `create()` is the exclusive allocation path or whether other migration/workbench tools may allocate identities under explicit authority remains an open ambiguity. |
| `F-14-07` | `[Observed]` | Co-location of Active and Historical Tooling in `docs/archimate/framework/tools/` | Active runtime dependencies (`docs/archimate/framework/tools/containment.py`, `docs/archimate/framework/tools/generate_views.py`, `docs/archimate/framework/tools/information_naming.py`, `docs/archimate/framework/tools/governed_metadata.py`, `docs/archimate/framework/tools/diagnose_roundtrip.py`) are co-located in `docs/archimate/framework/tools/` alongside historical replay and migration tools (`docs/archimate/framework/tools/reconcile_108.py`, `docs/archimate/framework/tools/recover_v02.py`, `docs/archimate/framework/tools/synchronise_baseline.py`, `docs/archimate/framework/tools/verify_workspace.py`). This lack of physical separation obscures active versus historical tool boundaries. |
| `F-14-08` | `[Observed]` | Hardcoded Historical Release Versions vs Dynamic Lookup in Tooling | `docs/archimate/framework/Makefile:14` dynamically resolves RAD_VERSION via `python3 scripts/architecture_release.py show`, whereas legacy framework verification tools (such as `docs/archimate/framework/tools/verify_workspace.py:11,25` and `docs/archimate/framework/tools/verify_governance_evidence.py:16`) hardcode historical version strings ("1.0.7", "1.0.8", "1.0.14"). |
| `F-14-09` | `[Uncertain]` | Reference to Non-Existent Log Directory in Historical Evidence Summary JSON | `enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json` references log files under `"validation/"` (`"validation/continuum-checks.log"` at line 7, `"validation/architecture-tests.log"` at line 13, `"validation/historical-suite-at-entry.log"` at line 21, `"validation/shared-semantics-tests.log"` at line 28, `"validation/continuum-tests-final.log"` at line 43). No directory named `"validation/"` exists under `enterprise-architecture/design/evidence/1.1.5/002/after/`, and no log files exist under `enterprise-architecture/design/evidence/1.1.5/002/`. The later directory `enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation/` holds logs of a different run (20 tests, not 11) and is not an equivalent of the missing files. Whether the referenced logs were ever persisted is UNRESOLVED. |
| `F-14-10` | `[Tested]` | Unresolved Failure of Historical Framework Test Suite at Task Entry | `enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:16-22` records the historical framework test suite as FAIL with 10 errors across 251 tests, with the recorded explanation "Current baseline authority drift; reconcile explicitly: AGENTS.md. Present at task entry, before this migration. Historical pins preserved." That recorded explanation is not independently re-established here. A companion `"sharedSemantics"` FAIL is recorded at `enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:23-29` (45 tests passed, 2 setup errors). Subsequent evidence artifacts (`enterprise-architecture/design/evidence/1.1.5/002/after/manifest-validation.json:70`, `enterprise-architecture/design/evidence/1.1.5/002/after/arb-validation-summary.json:72-76`) record that these suites were not rerun. The current suite outcome is therefore UNRESOLVED. The 10 failing tests cannot be identified because the referenced log is absent (see `F-14-09`). |

## 15. Current-State Governance Map

### 15.1 Architectural Components & Authority Taxonomy

The following taxonomy maps all active, transition, and historical components comprising the Harmonia Enterprise Architecture governance framework [Observed], [Documented]:

| Subsystem / Tool | Primary Repository Path | Operational Role & Execution Scope | Operational Status | Authority Level | Primary Invokers / Callers |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Architectural Axioms** | `enterprise-architecture/governance/architectural-axioms.md:1-26` | Highest design authority; defines 18 foundational platform axioms (AX-01 to AX-18). | Active | Supreme Design Authority | Solution Architect, ARB, Agents (Enforced by ArchUnit) |
| **Architecture Governance Contract** | `enterprise-architecture/governance/architecture-governance.md:35,48,344-345` | Core governance principles, review praxes, and human/agent authority boundaries. | Active | Governed Contract | Solution Architect, ARB, Agents |
| **Architecture Change Provenance** | `enterprise-architecture/governance/architecture-change-provenance.md:140-163` | Representation-neutral provenance rules, task/report layouts, and temporal standards. | Active | Governed Contract | Solution Architect, Agents, Evidence Tooling |
| **Architecture Review Praxis** | `enterprise-architecture/governance/architecture-review-praxis.md:170-173` | Structured review procedures, decision logging, and evidence capture rules. | Active | Governed Contract | Solution Architect, Agents |
| **Continuum Incorporation Praxis** | `enterprise-architecture/governance/continuum-incorporation-praxis.md:32,94-100` | Standardized procedures for element creation, migration, validation, and projection. | Active | Governed Contract | Solution Architect, ARB, Agents |
| **Continuum Representation** | `enterprise-architecture/governance/continuum-representation.md:133` | Formal specification for canonical Markdown structure, front-matter schemas, and identities. | Active | Governed Contract | Solution Architect, ARB, Agents |
| **Release Policy** | `docs/markdown/governance/architecture-release-policy.md:54-58` | Governs `architecture-release.json` patch increments and release lifecycle. | Active | Governed Contract | Solution Architect, Release Scripts |
| **ArchiMate Round-Trip Praxis** | `docs/markdown/governance/archimate-round-trip-engineering-praxis.md` | Governs Archi tool visual interchange, diff review, and directive application. | Active (Adapter Banner) | Governed Contract | Solution Architect, Agents |
| **Release Authority Record** | `architecture-release.json:1-4` | Root release version authority; single source of truth for platform release version. | Active | Release Authority | Publication & Release Scripts |
| **Decision Review Logs (Pending)** | `enterprise-architecture/design/review/` | Active HCRL decision review logs awaiting completion (`.pending.md` files). | Active | Decision Record | Solution Architect, Agents |
| **Decision Register (Complete)** | `enterprise-architecture/design/register/` | Finalized, authoritative HCRL records of completed release transitions (`.complete.md` files). | Active (Historical) | Decision Record | Solution Architect, ARB |
| **Canonical Continuum Elements** | `enterprise-architecture/continuum/` (`enterprise-architecture/continuum/business/`, `enterprise-architecture/continuum/application/`, `enterprise-architecture/continuum/motivation/`, `enterprise-architecture/continuum/strategy/`) | 1,602 individually represented canonical architectural elements in Markdown format. | Active | Canonical Architecture | Workbench, Projection, Publication |
| **Continuum Schemas & Config** | `enterprise-architecture/governance/continuum/` (`enterprise-architecture/governance/continuum/element.schema.json`, `enterprise-architecture/governance/continuum/type-catalogue.json`, etc.) | JSON schemas and metamodel configurations governing element validation and placement. | Active | Normative Schema | Workbench, Validation Scripts |
| **Continuum Workbench CLI** | `scripts/continuum_workbench.py` | CLI tool providing review and apply commands for Archi visual round-trip engineering. | Active | Operational Tooling | Solution Architect, Agents (Manual) |
| **Continuum Engine** | `scripts/architecture_continuum.py` | Core Continuum engine; handles element CRUD, validation CLI, XML projection, and identity allocation on create. | Active | Operational Tooling | Workbench, Makefile, Manual |
| **Historical Identity Inventory & Validator** | `scripts/architecture_identity.py:29-32` | Identity inventory and contract assessment tool; provides inventory, adoption, bindings, and check commands. | Active (Inspection) | Operational Tooling | Scripts Tests (`scripts/tests/test_architecture_identity.py`), Legacy Makefile Targets |
| **Manifest Generator** | `scripts/continuum_manifest.py` | Scans canonical corpus and generates `enterprise-architecture/continuum/MANIFEST.md`. | Active | Projection Tooling | Makefile (`make continuum`), Manual |
| **Publication Engine** | `scripts/publish_continuum.py` | Generates HTML browser (`enterprise-architecture/continuum/documentation/published/1.1.5/continuum.html`), LaTeX source (`enterprise-architecture/continuum/documentation/published/1.1.5/continuum-catalogue.tex`), and README. | Active | Publication Tooling | Makefile (`make continuum`), Manual |
| **Architecture Release Manager** | `scripts/architecture_release.py:1-82` | Validates and applies patch increments to root `architecture-release.json`. | Active | Release Tooling | Solution Architect, Makefile (`docs/archimate/framework/Makefile:14`), Manual Release Tasks |
| **Provenance Evidence Verifier** | `scripts/architecture_review_evidence.py` | Explicitly invoked validator for task/report directory layout and ISO 8601 UTC timestamp prefixes. | Active | Assurance Tooling | Agents, Quality Assurance (Manual) |
| **Clean-Room Reproducibility Verifier** | `scripts/verify_continuum_reproducibility.py` | Standing clean-room checker verifying byte-level identity allocation and projection stability. | Active | Assurance Tooling | Quality Assurance (Documented Manual) |
| **Link Integrity Verifier** | `scripts/verify_continuum_links.py` | Standing validator checking 1,499 relative Markdown targets and fragment anchors. | Active | Assurance Tooling | Quality Assurance (Documented Manual) |
| **Source Succession Verifier** | `scripts/verify_continuum_sources.py` | Verifies byte-level preservation of 855 retired sections across 57 source files. | Transition / Migration | Assurance Tooling | Migration Verification (Manual) |
| **Archi Transport Verifier** | `scripts/verify_continuum_transport.py` | Parameterized verifier comparing exported and re-imported Open Exchange XML models. | Transition / Migration | Assurance Tooling | Solution Architect, Transport Proofs |
| **Automated Architecture Suite** | `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/` | 11 ArchUnit test classes (90 tests) enforcing code-level software architectural invariants. | Active | Continuous Assurance | Maven Build (`mvn test`), Manual |
| **Framework Build & Automation** | `docs/archimate/framework/Makefile` | Makefile orchestrating manifest, projection, HTML/LaTeX publication, and unit test targets. | Active (Coupled) | Build Automation | Solution Architect, Agents (Manual) |
| **Legacy Framework Tools** | `docs/archimate/framework/tools/` (`docs/archimate/framework/tools/containment.py`, `docs/archimate/framework/tools/workspace_paths.py`, `docs/archimate/framework/tools/change_review.py`, etc.) | Legacy Python modules providing containment, path resolution, and round-trip support. | Active (Coupled) | Supporting Library | Continuum Engine, Identity, Workbench |
| **Historical Migration Adapters** | `scripts/` (`scripts/complete_rad_continuum_sources.py`, `scripts/finalize_continuum_succession.py`, etc.) | One-off migration scripts used during RAD-1.1.4 and RAD-1.1.5 migration milestones. | Historical / Retained | Historical Artifact | Historical Reference |

### 15.2 End-to-End Governance and Invocation Flows

The operational flows below illustrate the interaction between human authorities, automated agents, deterministic tooling, and persistence boundaries.

#### Flow 1: Normative Governance Decision Lifecycle (Stages 1–6)

```text
+---------------------------------------------------------------------------------------------------+
|                   NORMATIVE GOVERNANCE DECISION LIFECYCLE (PRAXIS STAGES 1-6)                     |
+---------------------------------------------------------------------------------------------------+

   +---------------------------------------------------------------------------------------------+
   | [Stage 1: Establish the Task and Originating State]                                         |
   | - Identify operative instruction, actual immediate release, source state, and authority     |
   | - Retain formal Task Detail at before/ai-agent-tasks/ with ISO 8601 UTC timestamp          |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | [Stage 2: Prepare the Candidate HCRL]                                                       |
   | - Draft candidate Decision Review Log at:                                                   |
   |     enterprise-architecture/design/review/<version>/hcrl-<version>-<iter>.pending.md        |
   | - Retain review-preparation Task in before/ and Report / durable supporting evidence in after/ |
   | - Pending iterations consume no release increment and supply no acceptance                  |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | [Stage 3: Obtain and Preserve Adjudication]                                                 |
   | - Solution Architect (SA) holds final decision and acceptance authority; ARB reviews        |
   | - SA records attributable dispositions (apply/reject/defer), conditions, and reasons in DR  |
   | - Agents cannot apply directives without attributable pre-application SA authorization      |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | [Stage 4: Apply the Authorised Scope & Prepare Release Increment]                           |
   | - Apply unambiguous authorised directives to canonical Continuum elements or codebase       |
   | - Retain application Task Details in before/ai-agent-tasks/                                 |
   | - Prepare patch increment in architecture-release.json (release policy lines 54-58)         |
   | - Discoveries affecting architectural meaning return to HCRL DR for adjudication             |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | [Stage 5: Report and Verify the Incorporated Result]                                        |
   | - Retain formal Task Report in after/ai-agent-reports/ with ISO 8601 Report-Produced-At     |
   | - Execute verification obligations (scripts/architecture_continuum.py validate, ArchUnit)   |
   | - Record actual application activity, verification outcomes, and limits in HCRL DR          |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | [Stage 6: Confirm and Complete the Transition]                                              |
   | - Present incorporated result for Solution Architect confirmation                           |
   | - Upon attributable SA confirmation, move HCRL from review/ to register/ (.complete.md)     |
   | - Agents report facts and cannot self-confirm architectural closure                         |
   | - Completion consumes no additional release increment; evidence accumulated throughout      |
   +---------------------------------------------------------------------------------------------+
```

#### Flow 2: Continuum Mutation, Projection, and Validation Flows

```text
+---------------------------------------------------------------------------------------------------+
|                      CONTINUUM MUTATION, PROJECTION, AND VALIDATION FLOWS                         |
+---------------------------------------------------------------------------------------------------+

   +---------------------------------------------------------------------------------------------+
   | Architecture Mutation Entry Points                                                          |
   | - Direct Canonical Markdown Edit (Manual Authoring / Agent)                                 |
   | - Scripted Element Creation (scripts/architecture_continuum.py create)                      |
   | - Staged Round-Trip Mutation (scripts/continuum_workbench.py apply)                         |
   +----------------------------------------------+----------------------------------------------+
                                                  | (Direct edits are ungated on file write)
                                                  v
   +---------------------------------------------------------------------------------------------+
   | Explicit Element Validation (Documented / Manual CLI Invocation)                            |
   | - python3 scripts/architecture_continuum.py validate                                        |
   | - Validates YAML front-matter against element.schema.json                                   |
   | - Validates type catalogue endpoints & mandatory Markdown sections                          |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | Review Manifest Generation                                                                  |
   | - python3 scripts/continuum_manifest.py (or make continuum)                                 |
   | - Scans all 1,602 elements; writes enterprise-architecture/continuum/MANIFEST.md            |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | Open Exchange XML Projection                                                                |
   | - python3 scripts/architecture_continuum.py project (or make continuum)                     |
   | - Resolves containment hierarchies via docs/archimate/framework/tools/containment.py        |
   | - Emits stored exchange identifiers directly from front-matter (m['exchange']['identifier'])|
   | - Injects 1,209 diagram views from projection-template.xml (10,007,373 bytes)               |
   | - Validates against ArchiMate 3.0 Open Exchange XSDs; writes XML (25,163,803 bytes)         |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | Multi-Channel Publication Pipeline                                                          |
   | - python3 scripts/publish_continuum.py (or make continuum)                                  |
   | - Emits continuum.html (10,043,810 bytes self-contained browser with search :29-34)         |
   | - Emits continuum-catalogue.tex (780,708 bytes LaTeX identity catalogue)                    |
   | - Emits publication.json (SHA-256 digests)                                                  |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | Documented Per-Change Verification Obligations (Executed Manually by Contributor/Agent)     |
   | - Reproducibility: python3 scripts/verify_continuum_reproducibility.py                       |
   | - Links:           python3 scripts/verify_continuum_links.py                                |
   | - Unit Tests:      python3 -m unittest scripts/tests/test_continuum.py (or make continuum-test) |
   | - ArchUnit:        mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"    |
   +---------------------------------------------------------------------------------------------+
```

#### Flow 3: Archi / Open Exchange Round-Trip Lifecycle

```text
+---------------------------------------------------------------------------------------------------+
|                           ARCHI / OPEN EXCHANGE ROUND-TRIP LIFECYCLE                              |
+---------------------------------------------------------------------------------------------------+

   +---------------------------------------------------------------------------------------------+
   | Projected Open Exchange XML (Harmonia-Architecture.xml)                                     |
   | - Downstream projection emitted from canonical Markdown elements                            |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | Visual Diagram Editing in Archi Tool (Manual / GUI)                                         |
   | - Architect edits diagrams, visual coordinates, or elements in Archi                        |
   | - Architect exports modified Open Exchange XML from Archi                                   |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | Current Continuum Workbench Review Phase                                                    |
   | - python3 scripts/continuum_workbench.py review \                                           |
   |     --baseline Harmonia-Architecture.xml \                                                 |
   |     --submitted Archi-Export.xml \                                                         |
   |     --output review-evidence.json                                                           |
   | - Requires baseline XML to match current canonical projection byte-for-byte (:59-60)        |
   | - Compares models; computes baselineSha256 and submissionSha256 in review JSON              |
   | - Added/deleted identities placed in unresolved list (blocks apply)                         |
   | - Writes structured review JSON evidence file (reviewSha256 is NOT in review JSON)          |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | Solution Architect Review & Decision Recording                                              |
   | - SA evaluates diffs; creates decision JSON referencing attributable authority HCRL path   |
   | - Specifies 'apply', 'reject', or 'defer' action + reason per item                          |
   | - Decision JSON holds reviewSha256 (hash of the review JSON file)                           |
   +----------------------------------------------+----------------------------------------------+
                                                  |
                                                  v
   +---------------------------------------------------------------------------------------------+
   | Current Continuum Workbench Apply Phase                                                     |
   | - python3 scripts/continuum_workbench.py apply \                                           |
   |     --decision review-decision.json                                                         |
   | - Verifies authority HCRL file exists on disk (os.path.isfile)                              |
   | - Verifies reviewSha256 matches hash of review JSON file (:92)                              |
   | - Verifies zero unresolved identity additions/deletions (:94)                                |
   | - Rechecks current canonical projection against baselineSha256 (:96)                        |
   | - Verifies explicit disposition for every reviewed change (:104-108)                         |
   | - Stages modifications in temporary copy and runs preflight validate() (:138)               |
   | - In-place writes applied to canonical Markdown files (not an atomic multi-file transaction)|
   +---------------------------------------------------------------------------------------------+

 (Note: The legacy change-review.md Markdown diff workflow under docs/archimate/framework/tools/change_review.py
  is governed by docs/markdown/governance/archimate-round-trip-engineering-praxis.md as a distinct legacy mechanism
  retained under an adapter banner, rather than declared superseded.)
```

### 15.3 Component Invocation & Dependency Matrix

The table below outlines the structural invocation pathways and runtime dependency mechanisms across the governance framework [Observed]:

| Invoking Component | Invoked Target / Dependency | Invocation Method | Invocation Nature | Dependency Classification | Gating Impact / Failure Behavior |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `scripts/continuum_workbench.py` | `scripts/architecture_continuum.py` | Direct Python import | Code-Invoked | Intra-Package Python Module | Hard Fail: Execution aborts immediately on import error. |
| `scripts/continuum_workbench.py` | `docs/archimate/framework/tools/change_review.py:47-48` | Dynamic `sys.path.insert(0, ...)` + import | Code-Invoked | Cross-Directory Tooling Coupling | Hard Fail: Aborts if review signature import fails. |
| `scripts/architecture_identity.py:29-32` | `docs/archimate/framework/tools/workspace_paths.py` | Dynamic `sys.path.insert(0, ...)` + import | Code-Invoked | Cross-Directory Tooling Coupling | Hard Fail: Aborts if workspace paths module missing. |
| `scripts/architecture_identity.py:29-32` | `docs/archimate/framework/tools/assess_rad.py` | Dynamic `sys.path.insert(0, ...)` + import | Code-Invoked | Cross-Directory Tooling Coupling | Hard Fail: Identity assessment fails. |
| `scripts/architecture_identity.py:29-32` | `docs/archimate/framework/tools/generate_views.py` | Dynamic `sys.path.insert(0, ...)` + import | Code-Invoked | Cross-Directory Tooling Coupling | Hard Fail: View generation helpers abort. |
| `scripts/architecture_continuum.py:166-172` | `docs/archimate/framework/tools/governed_metadata.py` | Dynamic `sys.path.insert(0, ...)` + import | Code-Invoked | Cross-Directory Tooling Coupling | Hard Fail: Metadata extraction aborts. |
| `scripts/architecture_continuum.py:388-403` | `docs/archimate/framework/tools/generate_views.py` | Dynamic `sys.path.insert(0, ...)` + import | Code-Invoked | Cross-Directory Tooling Coupling | Hard Fail: XML view projection aborts. |
| `scripts/architecture_continuum.py:388-403` | `docs/archimate/framework/tools/containment.py` | Dynamic `sys.path.insert(0, ...)` + import | Code-Invoked | Cross-Directory Tooling Coupling | Hard Fail: Containment hierarchy check aborts. |
| `scripts/architecture_continuum.py:388-403` | `docs/archimate/framework/tools/information_naming.py` | Dynamic `sys.path.insert(0, ...)` + import | Code-Invoked | Cross-Directory Tooling Coupling | Hard Fail: Naming validation aborts. |
| `scripts/architecture_continuum.py:388-403` | `docs/archimate/framework/tools/diagnose_roundtrip.py` | Dynamic `sys.path.insert(0, ...)` + import | Code-Invoked | Cross-Directory Tooling Coupling | Hard Fail: Round-trip diagnostics fail. |
| `scripts/architecture_continuum.py` | `docs/archimate/framework/schemas/archimate3_Diagram.xsd` | Python XMLSchema read | Code-Invoked | File System XML Schema | Hard Fail: Open Exchange projection aborted. |
| `scripts/publish_continuum.py` | `scripts/architecture_continuum.py` | Direct Python import | Code-Invoked | Intra-Package Python Module | Hard Fail: Publication pipeline aborts. |
| `scripts/publish_continuum.py` | `architecture-release.json` | File System JSON read | Code-Invoked | Authority File Dependency | Hard Fail: Release version resolution fails. |
| `docs/archimate/framework/Makefile:14` | `scripts/architecture_release.py:1-82` | Shell Command (`python3 ... show`) | Makefile-Invoked | Version Query Helper | Build configuration variable `RAD_VERSION` resolution. |
| `docs/archimate/framework/Makefile:16-30` | `scripts/continuum_manifest.py` | Shell Command (python3) | Makefile-Invoked | Build Target Orchestration | Gating Fail: make continuum aborts on error. |
| `docs/archimate/framework/Makefile:16-30` | `scripts/architecture_continuum.py project` | Shell Command (python3) | Makefile-Invoked | Build Target Orchestration | Gating Fail: make continuum aborts on error. |
| `docs/archimate/framework/Makefile:16-30` | `scripts/publish_continuum.py` | Shell Command (python3) | Makefile-Invoked | Build Target Orchestration | Gating Fail: make continuum aborts on error. |
| `docs/archimate/framework/Makefile:16-30` | `scripts/tests/test_continuum.py` | Shell Command (python3) | Makefile-Invoked | Test Target Orchestration | Gating Fail: make continuum-test aborts on error. |
| Contributor / Agent | `scripts/architecture_continuum.py validate` | CLI Command (python3) | Documented Manual | Manual Verification Gate | Fail-closed validation reported to author. |
| Contributor / Agent | `scripts/verify_continuum_reproducibility.py` | CLI Command (python3) | Documented Manual | Manual Verification Gate | Clean-room reproducibility verified. |
| Contributor / Agent | `scripts/verify_continuum_links.py` | CLI Command (python3) | Documented Manual | Manual Verification Gate | Relative Markdown links verified. |
| Contributor / Agent | `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/` | Maven CLI (`mvn test -pl ...`) | Documented Manual | Software Architecture Gate | Build fails on architecture rule violation. |
| Contributor / Agent | `scripts/architecture_review_evidence.py` | CLI Command (python3) | Documented Manual | Evidence Layout Gate | Task/report directory layout verified. |

### 15.4 Governance System Boundaries and Encapsulation

The Harmonia Enterprise Architecture governance framework enforces six primary governance boundaries [Documented], [Observed]:
1. **Canonical Continuum Boundary (`enterprise-architecture/continuum/`) vs Published Projections**:
   - *Encapsulation*: The Continuum directory is the sole canonical persistence store for individual architectural elements. All external formats (Open Exchange XML `enterprise-architecture/continuum/documentation/published/1.1.5/Harmonia-Architecture.xml`, HTML browser `enterprise-architecture/continuum/documentation/published/1.1.5/continuum.html`, LaTeX/PDF catalogue `enterprise-architecture/continuum/documentation/published/1.1.5/continuum-catalogue.pdf`, and review manifest `enterprise-architecture/continuum/MANIFEST.md`) are non-authoritative downstream projections.
   - *Permeability*: Strictly controlled; modifications to canonical elements occur through direct Markdown authoring, staged `scripts/continuum_workbench.py apply` executions, or validated ingestion tooling. Projections are never edited directly.
2. **Review vs Register Lifecycle Boundary (`enterprise-architecture/design/review/` vs `enterprise-architecture/design/register/`)**:
   - *Encapsulation*: Active, in-flight architecture decision logs reside as candidate files (with `.pending.md` suffix) in `enterprise-architecture/design/review/`. Completed, historically sealed decision logs reside as immutable records (with `.complete.md` suffix) in `enterprise-architecture/design/register/`.
   - *Permeability*: Unidirectional progression governed exclusively by human Solution Architect confirmation (AX-14). Agents cannot self-confirm architectural closure (`AGENTS.md:246`), but upon attributable SA confirmation, may perform the authorized file move from `enterprise-architecture/design/review/` to `enterprise-architecture/design/register/`.
3. **Provenance Iteration Boundary (`enterprise-architecture/design/evidence/<RAD-version>/<HCRL-iteration>/`)**:
   - *Encapsulation*: Each governed release iteration encapsulates immutable task inputs (under before/ai-agent-tasks/ directories) and execution reports (under after/ai-agent-reports/ directories).
   - *Permeability*: Retained throughout the iteration lifecycle. Subsequent amendments to reports are preserved as additional provenance without replacing original timestamps (`AGENTS.md:219-220`), and historical records are not rewritten (`AGENTS.md:238-239`).
4. **Working Artefact vs Authority Boundary (`.junie/plans/` and `.junie/reports/`)**:
   - *Encapsulation*: Files under `.junie/plans/` and `.junie/reports/` are working and historical execution artefacts. They carry zero architectural authority per `AGENTS.md:56-81` (§1.1) and `AGENTS.md:656-666` (§6).
   - *Permeability*: Strictly non-authoritative; architectural meaning discovered in working reports must undergo formal review and adjudication before incorporation into authoritative governance artifacts.
5. **Governance Metamodel & Schema Boundary (`enterprise-architecture/governance/continuum/`)**:
   - *Encapsulation*: JSON schemas (`enterprise-architecture/governance/continuum/element.schema.json`) and metamodel catalogs (`enterprise-architecture/governance/continuum/type-catalogue.json`, `enterprise-architecture/governance/continuum/native-relationships.json`) define valid element types, metadata properties, and allowed relationship endpoints.
   - *Permeability*: Modified only through formal architecture governance review. Tooling reads schemas as read-only validation rules.
6. **Release Authority Boundary (`architecture-release.json`)**:
   - *Encapsulation*: The root `architecture-release.json` file is the single current-release authority for the entire platform (`AGENTS.md:184-192` §1.3).
   - *Permeability*: Tooling reads the file; publication does not increment it. Patch increments are prepared exclusively during change application for accepted repository change sets (`docs/markdown/governance/architecture-release-policy.md:54-58`).

---

## 16. Findings Requiring Solution Architect Review

This section synthesizes key structural, operational, and governance findings requiring formal Solution Architect (SA) and Architecture Review Board (ARB) review. In accordance with platform guardrails (`AGENTS.md:56-81` §1.1, `enterprise-architecture/governance/architecture-governance.md:35,48`), this catalog is strictly analytical and non-prescriptive: it identifies evidenced architectural tensions, governing constraints, risks, and trade-offs as open questions for Solution Architect adjudication without prescribing future operating models or preempting decisions on the Solution Architect's behalf [Observed], [Documented].

### 16.1 Summary Catalog of Review Findings

 Finding ID | Epistemic Status | Title & Core Subject Area | Relevant Citations & Precursors |
 :--- | :--- | :--- | :--- |
 `F-16-01` | `[Observed]` | Decoupling and Consolidation of Continuum Tooling from Legacy Framework Modules | `F-13-01`, `F-13-02`, `F-13-03`; `scripts/architecture_continuum.py:27-31`, `docs/archimate/framework/tools/workspace_paths.py` |
 `F-16-02` | `[Documented]` | Formal Lifecycle Status and Retirement Policy for RAD-1.1.5 Migration Scripts | `F-12-01`, `F-13-05`; `scripts/complete_rad_continuum_sources.py`, `scripts/finalize_continuum_succession.py` |
 `F-16-03` | `[Documented]` | Governance Documentation Path Consolidation and Legacy Supersession Finalization | `F-14-04`, `F-14-05`; `docs/markdown/governance/architecture-release-policy.md`, `enterprise-architecture/governance/` |
 `F-16-04` | `[Documented]` | Formal Progression and Closure of Holistic Acceptance for Continuum Model in HCRL 1.1.5-002 | `F-14-02`; `enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md:6,9`, `AGENTS.md:98-99` |
 `F-16-05` | `[Observed]` | Harmonization of Architectural Axioms Representation across Governance Contracts and Continuum Elements | `F-13-06`, `F-14-01`; `enterprise-architecture/governance/architectural-axioms.md:1-26`, `AGENTS.md:28-38`, `enterprise-architecture/continuum/motivation/principle/` |
 `F-16-06` | `[Documented]` | Differentiation of Per-Change Incremental Verification vs Whole-Corpus Release Verification | `F-11-01`, `F-11-02`, `F-11-03`; `enterprise-architecture/governance/continuum-incorporation-praxis.md:94-100`, `AGENTS.md:559-561`, `docs/archimate/framework/Makefile` |
 `F-16-07` | `[Uncertain]` | Provenance Contract Application Scope for Non-Mutating, Analysis-Only Tasks | AX-17; `AGENTS.md:184-210` (§1.3, §1.4), `enterprise-architecture/governance/architecture-change-provenance.md:140-163` |
 `F-16-08` | `[Tested]` | Remediation Strategy for Entry-State Historical Framework Suite Failures and Missing Evidence Log Paths | `F-14-09`, `F-14-10`; `enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:16-22` |
 `F-16-09` | `[Documented]` | Canonical Identity Allocation Exclusivity Contract Clarification | `F-14-06`; `enterprise-architecture/governance/continuum-incorporation-praxis.md:32`, `enterprise-architecture/governance/continuum-representation.md:133` |
 `F-16-10` | `[Observed]` | ArchiMate Visual Diagram Persistence and Invariant Governance Strategy | `enterprise-architecture/design/archimate/projection-template.xml`, `docs/archimate/framework/tools/generate_views.py`, `docs/archimate/framework/tools/view_presentation.py` |

---

### 16.2 Detailed Review Findings and Open Questions for Solution Architect Review

#### F-16-01 [Observed]: Decoupling and Consolidation of Continuum Tooling from Legacy Framework Modules
- **Evidenced Tension**: Active Continuum tooling located in `scripts/` (`scripts/architecture_continuum.py`, `scripts/continuum_workbench.py`, `scripts/architecture_identity.py`) dynamically injects `sys.path` to import supporting modules (`docs/archimate/framework/tools/containment.py`, `docs/archimate/framework/tools/workspace_paths.py`, `docs/archimate/framework/tools/information_naming.py`, `docs/archimate/framework/tools/change_review.py`) from `docs/archimate/framework/tools/` (`F-13-01`, `F-13-03`).
- **Governing Constraints & Risks**: Prospective element authoring, validation, and projection pipelines depend on historical directory structures. Modifying or cleaning legacy framework files risks breaking active Continuum tooling if path dependencies are unmapped.
- **Open Question for Solution Architect**: How should the cross-directory dependency between `scripts/` and `docs/archimate/framework/tools/` be resolved to ensure clean tool modularity without disrupting active Continuum authoring and projection operations?

#### F-16-02 [Documented]: Formal Lifecycle Status and Retirement Policy for RAD-1.1.5 Migration Scripts
- **Evidenced Tension**: The `scripts/` directory contains specialized migration scripts (`scripts/complete_rad_continuum_sources.py`, `scripts/finalize_continuum_succession.py`) that executed bounded historical transitions for RAD-1.1.4 and RAD-1.1.5 (`F-12-01`).
- **Governing Constraints & Risks**: Co-locating one-off migration scripts alongside active prospective tools (`scripts/continuum_workbench.py`, `scripts/architecture_continuum.py`) creates potential ambiguity for contributors regarding which scripts represent supported operational entry points.
- **Open Question for Solution Architect**: What formal lifecycle designation or boundary should apply to completed migration scripts in `scripts/` to distinguish historical migration tooling from ongoing Continuum maintenance operations?

#### F-16-03 [Documented]: Governance Documentation Path Consolidation and Legacy Supersession Finalization
- **Evidenced Tension**: Active governance contracts (`docs/markdown/governance/architecture-release-policy.md` and `docs/markdown/governance/archimate-round-trip-engineering-praxis.md`) remain located under legacy documentation paths, co-located with superseded historical documents wrapped in section-level supersession markers (`F-14-04`, `F-14-05`).
- **Governing Constraints & Risks**: Retaining active contracts within a legacy documentation folder creates navigation confusion and risks misinterpretation of active versus superseded authority.
- **Open Question for Solution Architect**: Should active governance contracts currently residing under `docs/markdown/governance/` be consolidated into `enterprise-architecture/governance/`, and what supersession tracking should accompany such a transition under `AGENTS.md:144-182` (§1.2)?

#### F-16-04 [Documented]: Formal Progression and Closure of Holistic Acceptance for Continuum Model in HCRL 1.1.5-002
- **Evidenced Tension**: The EA Continuum operates prospectively as the canonical source for individually represented architectural elements (`AGENTS.md:84-125` §1.2), yet formal holistic acceptance of the model remains recorded as pending in `enterprise-architecture/design/review/1.1.5/hcrl-1.1.5-002.pending.md:6,9` (`F-14-02`).
- **Governing Constraints & Risks**: Operating with a pending HCRL leaves formal governance closure open, retaining candidate standing across 1,602 elements and delaying register transition (`AGENTS.md:215-224`).
- **Open Question for Solution Architect**: What formal review criteria and closure procedure should the Solution Architect establish to progress and complete holistic acceptance of the Continuum model recorded in HCRL 1.1.5-002?

#### F-16-05 [Observed]: Harmonization of Architectural Axioms Representation across Governance Contracts and Continuum Elements
- **Evidenced Tension**: The 18 Architectural Axioms exist simultaneously as narrative Markdown (`enterprise-architecture/governance/architectural-axioms.md:1-26`), rule clauses in `AGENTS.md:28-38`, and 18 Continuum Principle elements under `enterprise-architecture/continuum/motivation/principle/` (`F-13-06`, `F-14-01`).
- **Governing Constraints & Risks**: Maintaining dual representations across Markdown contracts and Continuum element files creates potential maintenance drift and ambiguity regarding which artifact constitutes the primary referent for axiom updates.
- **Open Question for Solution Architect**: Which artifact constitutes the primary authoritative source for platform Architectural Axioms, and how should synchronization between narrative governance contracts and Continuum Principle elements be maintained?

#### F-16-06 [Documented]: Differentiation of Per-Change Incremental Verification vs Whole-Corpus Release Verification
- **Evidenced Tension**: Governance documentation (`enterprise-architecture/governance/continuum-incorporation-praxis.md:94-100`, `AGENTS.md:559-561`) establishes comprehensive verification obligations (including XML projection, manifest updates, reproducibility checks, and ArchUnit test runs) for accepted repository changes (`F-11-01`, `F-11-02`, `F-11-03`).
- **Governing Constraints & Risks**: Executing whole-corpus validation and full test suites for minor, single-element edits introduces significant developer iteration overhead, while omitting checks risks latent integrity defects.
- **Open Question for Solution Architect**: How should verification obligations be structured between routine per-change authoring activities and formal release transitions to balance development efficiency with architectural assurance?

#### F-16-07 [Uncertain]: Provenance Contract Application Scope for Non-Mutating, Analysis-Only Tasks
- **Evidenced Tension**: Governance rules (`AGENTS.md:184-230` §1.3, §1.4; `enterprise-architecture/governance/architecture-change-provenance.md:140-163`) mandate patch increments in `architecture-release.json` and iteration evidence generation for accepted repository changes. Their applicability to non-mutating, analysis-only reporting tasks under `.junie/reports/` is unresolved under AX-17.
- **Governing Constraints & Risks**: Enforcing release increments and formal HCRL evidence trees for read-only analyses risks release version churn and manufactured HCRL records without SA authorization; conversely, omitting evidence records limits traceability.
- **Open Question for Solution Architect**: Does the Solution Architect intend for non-mutating, analysis-only agent activities to participate in formal HCRL evidence capture and release patch increments, or should such tasks operate under an explicit exemption?

#### F-16-08 [Tested]: Remediation Strategy for Entry-State Historical Framework Suite Failures and Missing Evidence Log Paths
- **Evidenced Tension**: Historical evidence summary JSON (`enterprise-architecture/design/evidence/1.1.5/002/after/validation-summary.json:16-22`) records a 10-error failure across 251 tests in the legacy framework test suite at task entry and cites non-existent `"validation/"` log paths (`F-14-09`, `F-14-10`).
- **Governing Constraints & Risks**: Historical evidence records cannot be rewritten retroactively under `AGENTS.md:210-213` (§1.4), yet unaddressed test failures and broken log paths in evidence trees create uncertainty regarding framework regression status.
- **Open Question for Solution Architect**: What governance approach should be applied to address recorded historical framework test failures and missing log paths without violating provenance immutability rules for past release iterations?

#### F-16-09 [Documented]: Canonical Identity Allocation Exclusivity Contract Clarification
- **Evidenced Tension**: `enterprise-architecture/governance/continuum-incorporation-praxis.md:32` instructs allocation of canonical UUIDv4 via `scripts/architecture_continuum.py create`, whereas `enterprise-architecture/governance/continuum-representation.md:133` specifies that "Only an explicitly authorised creation/ingestion operation allocates UUIDv4 once" (`F-14-06`).
- **Governing Constraints & Risks**: Ambiguity regarding whether manual UUID allocation is permitted or whether all allocations must pass through tooling creates potential inconsistency in front-matter authoring workflows and preflight checks.
- **Open Question for Solution Architect**: Should canonical UUIDv4 allocation be strictly restricted to deterministic tooling entry points, or do explicitly authorized manual authoring procedures also constitute valid allocation paths?

#### F-16-10 [Observed]: ArchiMate Visual Diagram Persistence and Invariant Governance Strategy
- **Evidenced Tension**: Visual diagram layouts (1,209 Views) are currently persisted inside `enterprise-architecture/design/archimate/projection-template.xml` (10,007,373 bytes / 10.0 MB) and projected into Open Exchange XML (25,163,803 bytes / 25.16 MB) via `scripts/architecture_continuum.py project`. Legacy programmatic view generators exist under `docs/archimate/framework/tools/generate_views.py` and `docs/archimate/framework/tools/view_presentation.py`.
- **Governing Constraints & Risks**: Maintaining diagram layouts inside a large XML template couples visual representation maintenance to Archi XML round-trip diffing, while programmatic view generation lacks full visual layout flexibility.
- **Open Question for Solution Architect**: What long-term governance and persistence strategy should govern ArchiMate visual diagram views and layout invariants as the Continuum model continues to evolve?

---