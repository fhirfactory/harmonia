---
sessionId: session-260925-111445-noa2
---

# Requirements

- **Goal / outcome**:
  Establish an explicit architectural platform invariant across Harmonia documentation that Harmonia does not provide physical DELETE semantics for governed persisted information. A logical deletion is a domain-appropriate lifecycle transition and therefore an authoritative UPDATE. Long-term archival, retention-based purge, and physical disposal are formally designated as outside the scope of the current Harmonia framework.
- **Scope**:
  - **In scope**:
    - Add ADR-020 ("Governed Information Uses Lifecycle State Rather Than Physical Deletion", Status: Accepted, Date: 25 September 2026) to the authoritative Markdown register (`docs/architecture-decisions.md`) and authoritative LaTeX appendix (`docs/latex/chapters/appendix-decisions.tex`).
    - Qualify the ADR-018 Task 08 roadmap sequence in both Markdown and LaTeX to clarify that Task 08 designs CREATE and UPDATE write semantics without a physical authoritative DELETE path.
    - Update `hestia/mneme-cluster/docs/mneme-concurrency-and-information-integrity.md` with a concise section referencing ADR-020, distinguishing resource lifecycle transitions (authoritative UPDATEs) from reconstructable Mneme cache evictions, noting out-of-scope archival/purge, and clarifying that Infinispan's `removeWithVersion()` is a laboratory-observed capability, not a Harmonia governed resource deletion semantic.
    - Perform a targeted consistency review and update existing architecture/persistence documentation (`docs/database-schema.md`, `docs/persistence-architecture.md`, `docs/architecture/persistence-lifecycle.md`, `docs/integration/fhir.md`, `docs/provider-registry/persistence.md`, `docs/concepts/hestia-persistence.md`, `docs/concepts/pylai.md`, `docs/getting-started/architecture-at-a-glance.md`, `docs/latex/chapters/04-data-architecture.tex`, `docs/latex/chapters/03-application-layer.tex`, `docs/provider-registry/fhir-api.md`) to align with ADR-020.
    - Classify all documentation DELETE occurrences into categories (A: Governed resource deletion requiring correction, B: Mneme cache eviction, C: Test/laboratory capability, D: Infrastructure/administrative deletion, E: Historical/design discussion).
    - Formally identify and report `PersistenceOperationEnvelope` / `PersistenceOperationType` DELETE operations as a retained architectural consistency item requiring a subsequent decision.
  - **Out of scope**:
    - Modifying production source code, Java classes, APIs, or database schemas.
    - Modifying existing unit/integration tests or ArchUnit architecture rules.
    - Removing, redesigning, or reinterpreting `PersistenceOperationEnvelope.java` or `PersistenceOperationType.java`.
    - Implementing soft-delete flags, generic "deleted" properties, or archive tables.
    - Implementing Task 08, changing Mneme cache runtime behavior, or removing `removeWithVersion()` laboratory characterization tests.
- **Done when**:
  - ADR-020 is published in both `docs/architecture-decisions.md` and `docs/latex/chapters/appendix-decisions.tex` with semantic equivalence.
  - ADR-018 Task 08 sequence is qualified with CREATE/UPDATE/lifecycle-transition semantics in both ADR registers.
  - `mneme-concurrency-and-information-integrity.md` is updated concisely referencing ADR-020 without duplicating the full ADR text.
  - All Class A governed-resource DELETE claims across architecture docs are corrected to domain-appropriate lifecycle state transitions.
  - `git diff` confirms zero changes to production Java code (`calliope`, `themis`, `hestia`, `petasos`, `energeia`, `pylai`, `iris`, `agora`, `kleio`, `paradeigma`).
  - Architecture test suite (`*ArchitectureTest`) executes and passes cleanly.

# Technical Design

- **Decisions**:
  - *ADR-020 Register Placement*: Add ADR-020 to `docs/architecture-decisions.md` and `docs/latex/chapters/appendix-decisions.tex` following the exact conventions and structure of ADR-018 and ADR-019 (Context, Decision, Distinctions, Task 08 Consequence, Concurrency Consequence, FHIR Lifecycle Semantics, Audit and Provenance, Consequences, Principles).
  - *Four Core Distinctions*:
    1. **Resource Lifecycle Transition**: Authoritative state change in Mnemosyne committed as a conditional UPDATE subject to concurrency, provenance, and Kleio audit.
    2. **Mneme Eviction**: Removal of a reconstructable active-state entry from the distributed cache; does NOT change or delete authoritative Mnemosyne state.
    3. **Archival**: Long-term movement/management of legacy records; formally out of scope for the current Harmonia framework.
    4. **Purge / Disposal**: Physical destruction of records under retention policy; formally out of scope for the current Harmonia framework.
  - *Task 08 Framing*: Explicitly define that CREATE establishes authoritative state, UPDATE changes authoritative state, LIFECYCLE TRANSITION is an UPDATE, and physical DELETE is not an authoritative Harmonia operation. Task 08 designs CREATE and UPDATE write protocols only.
  - *Infinispan vs Resource Semantics*: `removeWithVersion()` is an observed technical capability of Infinispan Hot Rod in laboratory testing, NOT a Harmonia governed resource deletion operation. Low-level cache eviction is distinct from resource destruction.
  - *PersistenceOperationEnvelope Preservation*: Report `PersistenceOperationEnvelope` and `PersistenceOperationType` DELETE enum values and factory methods (`ofDelete`) as an architectural consistency finding requiring subsequent platform decision; do NOT edit the Java classes.
- **Approach & touches**:
  - `docs/architecture-decisions.md`: Insert ADR-020 after ADR-019; qualify Task 08 in ADR-018 implementation sequence (lines ~393–404).
  - `docs/latex/chapters/appendix-decisions.tex`: Insert LaTeX `\subsection{ADR-020...}` after ADR-019; qualify Task 08 sequence (~lines 234–246).
  - `hestia/mneme-cluster/docs/mneme-concurrency-and-information-integrity.md`: Add a concise section referencing ADR-020 and summarizing the 4 distinctions, lifecycle UPDATE semantics, and `removeWithVersion()` distinction.
  - `docs/database-schema.md`: Correct soft-delete flag and administrative purge descriptions to reflect domain lifecycle transitions and out-of-scope purge.
  - `docs/persistence-architecture.md`: Reconcile "soft-deletion (`is_deleted`)" and "regulatory purge" sections with ADR-020 lifecycle principles.
  - `docs/architecture/persistence-lifecycle.md`: Refine schema examples and distinguish governed resource lifecycle from Artemis journal message compaction.
  - `docs/integration/fhir.md` & `docs/provider-registry/persistence.md`: Clarify `@Delete` annotations, referential integrity safeguards, and `410 Gone` semantics as domain lifecycle states.
  - `docs/concepts/hestia-persistence.md`, `docs/concepts/pylai.md`, `docs/getting-started/architecture-at-a-glance.md`: Replace generic "CRUD" references with CREATE, UPDATE, search, and lifecycle transition semantics.
  - `docs/latex/chapters/04-data-architecture.tex` & `docs/latex/chapters/03-application-layer.tex`: Align LaTeX data architecture and application descriptions with ADR-020 lifecycle semantics.
  - Reference analog: Structure and tone of ADR-018 and ADR-019 in `docs/architecture-decisions.md` and `docs/latex/chapters/appendix-decisions.tex`.
- **Nuances / risks / corners**:
  - *Do not mechanically replace the word DELETE*: Infrastructure deletions (e.g. Artemis broker journal compaction upon consumer ACK / DLQ move, Kubernetes pod/PVC undeployment in `18-undeployment-retention.tex`) and Kleio audit immutability rules prohibiting deletion must remain intact.
  - *Historical LaTeX files*: `docs/latex/chapters/appendix-decisions-old.tex` and `appendix-decisions-old2.tex` are non-compiled historical artifacts; do not treat them as current ADR targets.
  - *Semantic Equivalence*: Ensure LaTeX and Markdown ADR-020 texts convey identical normative principles, constraints, and consequences despite markup formatting differences.
- **Contracts**:
  - Normative Guardrail:
    > "Harmonia SHALL NOT expose physical deletion as a normal operation for governed persisted information. Logical deletion SHALL be represented as a domain-appropriate lifecycle transition and processed as a concurrency-controlled authoritative update. Mneme cache eviction SHALL NOT be interpreted as deletion of authoritative state. Archival, retention-based purge and physical disposal are outside the current Harmonia framework scope."

# Testing

- Must-hold verification scenarios:
  - Zero code modifications: `git diff --stat -- ':(exclude)docs' ':(exclude)hestia/mneme-cluster/docs'` produces empty output.
  - Semantic equivalence between `docs/architecture-decisions.md` (Markdown) and `docs/latex/chapters/appendix-decisions.tex` (LaTeX) for ADR-020 title, status (Accepted), date (25 September 2026), four distinctions, Task 08 write consequences, concurrency flow, and normative guardrails.
  - Secondary doc inspection: `hestia/mneme-cluster/docs/mneme-concurrency-and-information-integrity.md` contains concise reference to ADR-020 without replicating the full decision text.
  - Consistency classification completed for all documentation DELETE occurrences (Categories A through E).
  - `PersistenceOperationEnvelope` DELETE factory methods and enum values documented in the final report as a preserved consistency item.
  - Regression safety: Architecture test suite passes cleanly: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.

# Assumptions & Open Questions

- **Significant assumptions**:
  - *PersistenceOperationEnvelope Retention*: Retain existing `PersistenceOperationType.DELETE` and `PersistenceOperationEnvelope.ofDelete()` in production code without modification, treating it as an open architectural consistency finding for future resolution. *Impact*: Prevents scope creep and preserves binary backwards compatibility for existing callers and tests.
  - *Historical LaTeX ADR Copies*: `appendix-decisions-old.tex` and `appendix-decisions-old2.tex` are excluded from `main.tex` and will not be edited, preserving historical archive fidelity while editing only compiled `appendix-decisions.tex`.
- **Source conflicts resolved**:
  - Earlier documentation in `docs/database-schema.md` and `docs/persistence-architecture.md` described generic `is_deleted` flags and regulatory purge. Resolved in favor of ADR-020: domain-appropriate lifecycle state within the resource (e.g. status, active, entered-in-error) and out-of-scope purge/disposal.

# Delivery Steps

### ✓ Step 1: Publish ADR-020 in Authoritative Markdown and LaTeX Registers
Goal: Add ADR-020 to both Markdown and LaTeX architecture decision registers and qualify the ADR-018 Task 08 sequence.
Scope: `docs/architecture-decisions.md`, `docs/latex/chapters/appendix-decisions.tex`
Acceptance Criteria:
- [ ] `docs/architecture-decisions.md` contains ADR-020 ("Governed Information Uses Lifecycle State Rather Than Physical Deletion", Status: Accepted, Date: 25 September 2026) matching ADR-018/ADR-019 style.
- [ ] `docs/architecture-decisions.md` ADR-018 implementation sequence (~lines 393–404) explicitly documents that Task 08 establishes authoritative CREATE and UPDATE write semantics without a physical DELETE path.
- [ ] `docs/latex/chapters/appendix-decisions.tex` contains semantically equivalent LaTeX `\subsection{ADR-020...}` with appropriate labels, ArchiMate citations, and quotes matching ADR-018/ADR-019.
- [ ] `docs/latex/chapters/appendix-decisions.tex` ADR-018 implementation sequence (~lines 234–246) is updated to reflect CREATE/UPDATE/lifecycle semantics.
- [ ] Both registers clearly state the four distinctions (Lifecycle Transition, Mneme Eviction, Archival, Purge/Disposal) and the normative platform guardrail.
Verification: `grep -E "ADR-020|Governed Information Uses Lifecycle State" docs/architecture-decisions.md docs/latex/chapters/appendix-decisions.tex` → matches found in both files

### ✓ Step 2: Update Mneme Concurrency and Information Integrity Documentation
Goal: Add a concise section referencing ADR-020 and distinguishing cache eviction and Infinispan capabilities from Harmonia resource semantics.
Scope: `hestia/mneme-cluster/docs/mneme-concurrency-and-information-integrity.md`
Acceptance Criteria:
- [ ] A concise section is added to `mneme-concurrency-and-information-integrity.md` referencing ADR-020 without duplicating the entire ADR body.
- [ ] The document explicitly establishes: no physical delete; lifecycle transition == authoritative UPDATE; Mneme eviction != resource deletion; archival and retention purge are outside current scope.
- [ ] The document clarifies that Infinispan's `removeWithVersion()` is an available distributed cache capability (tested in laboratory), but is not a Harmonia governed-resource deletion semantic.
- [ ] Concurrency sequence diagram or flow notes that lifecycle transitions execute via conditional UPDATE subject to stale-update detection.
Verification: `grep -E "ADR-020|removeWithVersion|eviction" hestia/mneme-cluster/docs/mneme-concurrency-and-information-integrity.md` → matches found with correct semantic context

### ✓ Step 3: Targeted Consistency Review and Documentation Harmonization
Goal: Harmonize existing architecture, persistence, and gateway documentation to remove generic physical DELETE claims for governed resources while preserving legitimate cache, lab, and infrastructure references.
Scope: `docs/database-schema.md`, `docs/persistence-architecture.md`, `docs/architecture/persistence-lifecycle.md`, `docs/integration/fhir.md`, `docs/provider-registry/persistence.md`, `docs/concepts/hestia-persistence.md`, `docs/concepts/pylai.md`, `docs/getting-started/architecture-at-a-glance.md`, `docs/provider-registry/fhir-api.md`, `docs/latex/chapters/04-data-architecture.tex`, `docs/latex/chapters/03-application-layer.tex`
Acceptance Criteria:
- [ ] `docs/database-schema.md` and `docs/persistence-architecture.md` are updated to replace generic soft-delete flag / purge claims with domain-appropriate lifecycle transition semantics and out-of-scope purge notes.
- [ ] `docs/architecture/persistence-lifecycle.md` clarifies resource lifecycle states while retaining Artemis message journal deletion on ACK.
- [ ] `docs/integration/fhir.md` and `docs/provider-registry/persistence.md` qualify `@Delete` and `410 Gone` as domain lifecycle representations and referential integrity guards.
- [ ] `docs/concepts/hestia-persistence.md`, `docs/concepts/pylai.md`, and `docs/getting-started/architecture-at-a-glance.md` qualify "full CRUD" references to CREATE, UPDATE, read/search, and lifecycle transitions.
- [ ] `docs/latex/chapters/04-data-architecture.tex` (and `03-application-layer.tex`) aligns FHIR resource catalog and relational schema descriptions with ADR-020.
- [ ] Legitimate occurrences of DELETE (Categories B, C, D, E) such as cache eviction, laboratory characterization, Artemis journal compaction, and immutable Kleio audit protections are preserved.
Verification: `git diff --stat docs/` → shows updated documentation files with harmonized lifecycle terminology

### ✓ Step 4: Verification of Zero Code Changes and Final Deliverable Reporting
Goal: Confirm that no production code or schemas were altered, run regression architecture tests, and compile the final deliverable summary report.
Scope: Entire repository
Acceptance Criteria:
- [ ] `git status --porcelain` confirms changes are strictly confined to markdown (`.md`) and LaTeX (`.tex`) documentation files.
- [ ] Zero Java source files (`*.java`), POM files (`pom.xml`), SQL migration scripts, or configuration files are modified.
- [ ] ArchUnit test suite runs and passes cleanly across all modules.
- [ ] Final deliverable report is prepared answering the 8 parent task points:
  1. Files inspected.
  2. Files modified.
  3. ADR-020 summary.
  4. Relevant existing documentation statements corrected.
  5. Markdown/LaTeX consistency confirmation.
  6. Remaining architectural inconsistencies discovered (including `PersistenceOperationEnvelope` report).
  7. Specific occurrences of DELETE deliberately retained and rationale.
  8. Confirmation that no production code was modified.
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green (exit code 0)