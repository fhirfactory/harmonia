<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1-carry-forward-existing-software-candidate) | Existing Calliope candidate and scope | PENDING REVIEW | Carry forward HCRL 004 DR-1; no disposition or candidate acceptance is inferred. |
| [DR-2](#dr-2-carry-forward-package-hierarchy) | Package hierarchy and cohesion | PENDING REVIEW | Carry forward HCRL 004 DR-2. |
| [DR-3](#dr-3-carry-forward-information-model) | Information model and construction | PENDING REVIEW | Carry forward HCRL 004 DR-3, including IU representation and legacy compatibility questions. |
| [DR-4](#dr-4-carry-forward-consumer-conformance) | Consumer migration and conformance | PENDING REVIEW | Carry forward HCRL 004 DR-4 and its exact-candidate limits. |
| [DR-5](#dr-5-carry-forward-documentation-and-source-size) | Documentation and source size | PENDING REVIEW | Carry forward HCRL 004 DR-5; its Calliope inventory is not evidence for the V001 changes. |
| [DR-6](#dr-6-integrated-software-candidate-and-baseline) | Integrated candidate, verification and baseline | PENDING REVIEW | Carry forward HCRL 004 DR-6; no accepted software predecessor or first-release decision is inferred. |
| [DR-7](#dr-7-carry-forward-fhir-task-security-context) | FHIR Task security-context authority | PENDING REVIEW | Carry forward HCRL 004 DR-7; V001 does not resolve that separate transport trust question. |
| [DR-8](#dr-8-carry-forward-delete-operation-semantics) | Persistence DELETE semantics | PENDING REVIEW | Carry forward HCRL 004 DR-8; this HCRL does not infer a meaning for the existing enum. |
| [DR-9](#dr-9-carry-forward-legacy-iu-json) | Legacy IU JSON representation | PENDING REVIEW | Carry forward HCRL 004 DR-9; no migration waiver is inferred. |
| [DR-10](#dr-10-carry-forward-topic-topicmask-design) | Topic/TopicMask Draft.001.003 | PENDING SOLUTION ARCHITECT REVIEW | Carry forward HCRL 004 DR-10 and its implementation obligations. |
| [DR-11](#dr-11-provenance-and-iteration-005-evidence-gap) | Provenance and iteration-005 evidence gap | PENDING REVIEW | Preserve HCRL 004 DR-11, unresolved event times and the missing HCRL.005/layout-check result; this authorization does not retrospectively close them. |
| [DR-12](#dr-12-carry-forward-pragma-and-g03-reviews) | Pragma publication/subscription and G03 reviews | PENDING SOLUTION ARCHITECT REVIEW | Carry forward HCRL 004 DR-12 and all its linked qualifications and unresolved obligations. |
| [DR-13](#dr-13-v001-assignment-and-candidate-scope) | V001 assignment authority, backlog and exact candidate | PENDING REVIEW | Review this implementation against Backlog Item `00002`, revision `1`, and the exact V001 dispositions; no implementation acceptance is claimed. |
| [DR-14](#dr-14-governed-non-fhir-informationunit-persistence) | Governed IU write/read and integrity | PENDING REVIEW | Review Mneme→Mnemosyne→GPF→Non-FHIR PostgreSQL behaviour, identity/version integrity and lifecycle. |
| [DR-15](#dr-15-operational-status-ownership-and-persistence) | Operational Status ownership and persistence | PENDING REVIEW | Review the owner-defined Status round trip and the preserved distinction from InformationUnit. |
| [DR-16](#dr-16-themis-authorization-and-outcome-durability) | Themis authorization and outcome/durability semantics | PENDING REVIEW | Review default-deny enforcement and truthful successful, failed, indeterminate and qualified local durability results. |
| [DR-17](#dr-17-fhir-persistence-boundary) | FHIR PersistenceService boundary | PENDING REVIEW | Preserve the existing implementation; FHIR changes remain outside primary V001 scope. |
| [DR-18](#dr-18-implementation-conformance-and-verification) | Implementation conformance and verification | PENDING REVIEW | Review exact commands, component/solution-wide checks, PostgreSQL evidence, limitations and implementation quality. |

# HCRL software-0.1.0 / 006 — Mneme–Mnemosyne Governed Persistence V001

**Current candidate disposition · Superseded by the clean-slate reset.**
The Solution Architect's [reset assignment][reset-assignment] closes the V001
implementation candidate without implementation acceptance. Preserve the original
DRs, implementation evidence and source correspondence below. Inherited matters
retain their recorded standing at their originating review. The [reset HCRL][foundation-reset]
records the checkpoint from which the removed source can be recovered.

The original review record follows.

**Iteration status · PENDING SOLUTION ARCHITECT REVIEW.**

The Solution Architect's task-specific [iteration disposition](../../evidence/software-0.1.0/006/before/ai-agent-tasks/UNRESOLVED-mneme-mnemosyne-governed-persistence-v001-task.md) expressly establishes iteration `006` for the V001 implementation and authorizes creation of this pending HCRL. HCRL `004` is preserved unchanged; this iteration does not accept the implementation, allocate a software release, or confer commit/tag/push authority.

HCRL `004` remains pending for its Calliope and design-review candidate. All DR-1–DR-12 matters and qualifications are carried forward below by identity and linked to their originating record. The existing iteration-005 evidence remains at its original paths. The HCRL.004 DR-11 finding that the iteration-005 evidence-layout check lacks its corresponding HCRL remains unresolved; the specific authorization of iteration `006` neither creates a retrospective HCRL.005 nor closes that finding. Preserve this discontinuity for Solution Architect review.

- **Target Software Release:** `0.1.0` (candidate only; acceptance remains pending)
- **Accepted Architecture Baseline:** `1.1.26`; no Architecture Continuum change is proposed
- **Entry Branch / HEAD:** `feature/BL-CALLIOPE-OO-01` / `8af315134a1f2e03e75512f66513ef2f7eeb80b8`
- **Entry working tree:** clean before this assignment's backlog item was created
- **Candidate source:** integrated, uncommitted worktree based on the entry HEAD; exact changed-file correspondence and test outcomes are recorded in the Implementation Report
- **Backlog Item:** [00002, revision 1 — Mneme–Mnemosyne Governed Persistence V001](../../../../docs/backlog/BacklogItem-0002-Mneme-Mnemosyne-Governed-Persistence-V001.001.md)
- **Task Details:** [V001 implementation Task](../../evidence/software-0.1.0/006/before/ai-agent-tasks/UNRESOLVED-mneme-mnemosyne-governed-persistence-v001-task.md)
- **Implementation Report:** [V001 implementation report](../../evidence/software-0.1.0/006/after/ai-agent-reports/UNRESOLVED-mneme-mnemosyne-governed-persistence-v001-implementation-report.md)
- **Accepted software predecessor:** not established in HCRL 004; DR-6 carries the first-baseline decision forward without inferring it

---

<a id="dr-1-carry-forward-existing-software-candidate"></a>
## DR-1 — Carry-forward: existing software candidate and scope

HCRL 004 DR-1 remains **PENDING REVIEW** for candidate `2fc15febe2a91c811fd48e83f3e7544dc37f8124` and its Calliope assignment-specific scope. That task authorization, its unresolved event time and the existing candidate are preserved at their recorded scope. This V001 assignment neither changes nor closes that review. See [HCRL 004 / DR-1](hcrl-software-0.1.0-004.pending.md#dr-1-whole-of-calliope-scope-and-candidate) and the [carry-forward reconciliation](../../evidence/software-0.1.0/004/after/HCRL-Carry-Forward-Reconciliation.md).

<a id="dr-2-carry-forward-package-hierarchy"></a>
## DR-2 — Carry-forward: package hierarchy and cohesion

HCRL 004 DR-2 remains **PENDING REVIEW** for the prior candidate's package/responsibility mapping. V001's new package choices are separately reviewable under DR-18 and do not resolve the prior Calliope review. See [HCRL 004 / DR-2](hcrl-software-0.1.0-004.pending.md#dr-2-design-note-hierarchy-and-cohesive-package-organization).

<a id="dr-3-carry-forward-information-model"></a>
## DR-3 — Carry-forward: information model and construction

HCRL 004 DR-3 remains **PENDING REVIEW**, including accepted InformationUnit semantics, model and serialization choices, version meaning, security classification, lifecycle and the untested legacy representation question. V001 evidence must establish its actual preservation results without claiming a waiver for the existing candidate. See [HCRL 004 / DR-3](hcrl-software-0.1.0-004.pending.md#dr-3-information-model-value-sets-and-construction).

<a id="dr-4-carry-forward-consumer-conformance"></a>
## DR-4 — Carry-forward: consumer migration and conformance

HCRL 004 DR-4 remains **PENDING REVIEW** for the exact prior migration candidate and its recorded test scope. V001 integration results are new evidence for the integrated source candidate only. See [HCRL 004 / DR-4](hcrl-software-0.1.0-004.pending.md#dr-4-consumer-migration-and-architectural-conformance).

<a id="dr-5-carry-forward-documentation-and-source-size"></a>
## DR-5 — Carry-forward: documentation and source size

HCRL 004 DR-5 remains **PENDING REVIEW**. The previously reported 104-file Calliope count and 473-line maximum apply only to that candidate and cannot establish V001's source-size or educational-documentation conformance. See [HCRL 004 / DR-5](hcrl-software-0.1.0-004.pending.md#dr-5-500-line-source-file-limit-and-documentation).

<a id="dr-6-integrated-software-candidate-and-baseline"></a>
## DR-6 — Integrated software candidate and baseline

HCRL 004 DR-6 remains **PENDING REVIEW**. Its 251 Calliope tests, 90 architecture tests and 50-module reactor results retain the source scope and limitations recorded there; they are not results for this V001 candidate. The accepted predecessor or explicit first-release treatment remains for Solution Architect determination. This HCRL requests review of the integrated V001 candidate only after its exact source, applicable component and solution-wide checks, and qualifications are recorded.

See [HCRL 004 / DR-6](hcrl-software-0.1.0-004.pending.md#dr-6-software-010-candidate-verification-and-review-request).

<a id="dr-7-carry-forward-fhir-task-security-context"></a>
## DR-7 — Carry-forward: FHIR Task security-context authority

HCRL 004 DR-7 remains **PENDING REVIEW**. V001 does not change `PragmaFhirSecurityConverter` or establish the trust boundary of caller-supplied FHIR Task extensions. See [HCRL 004 / DR-7](hcrl-software-0.1.0-004.pending.md#dr-7-pragma-fhir-security-context-authority).

<a id="dr-8-carry-forward-delete-operation-semantics"></a>
## DR-8 — Carry-forward: Persistence DELETE operation semantics

HCRL 004 DR-8 remains **PENDING REVIEW**. V001's prohibition on physical deletion as ordinary managed-information lifecycle behaviour does not supply the missing interpretation of the existing `PersistenceOperationType.DELETE`. See [HCRL 004 / DR-8](hcrl-software-0.1.0-004.pending.md#dr-8-persistence-delete-operation-semantics).

<a id="dr-9-carry-forward-legacy-iu-json"></a>
## DR-9 — Carry-forward: legacy InformationUnit JSON

HCRL 004 DR-9 remains **PENDING REVIEW**. V001 round-trip tests must identify the exact current candidate representation; they do not prove compatibility or migration of historical `InformationUnitMetadata` snapshots. See [HCRL 004 / DR-9](hcrl-software-0.1.0-004.pending.md#dr-9-legacy-information-unit-json-representation).

<a id="dr-10-carry-forward-topic-topicmask-design"></a>
## DR-10 — Carry-forward: Topic/TopicMask Draft.001.003

HCRL 004 DR-10 remains **PENDING SOLUTION ARCHITECT REVIEW**, including its listed compatibility, mapping and implementation obligations. The V001 persistence work does not adjudicate or close the design review. See [HCRL 004 / DR-10](hcrl-software-0.1.0-004.pending.md#dr-10).

<a id="dr-11-provenance-and-iteration-005-evidence-gap"></a>
## DR-11 — Provenance and iteration-005 evidence gap

HCRL 004 DR-11 remains **PENDING REVIEW**. Prior Task event times and unresolved prefixes remain unresolved. Iteration-005 evidence remains in its originating locations; HCRL 004 records that the corresponding HCRL.005 is absent and that the layout check requires it. The Solution Architect's explicit iteration-006 authorization supplies the current V001 boundary but does not retroactively create, review, or close iteration 005. The relationship between the existing iteration-005 evidence, the reserved HCRL.005, and this authorized iteration 006 remains expressly visible for Solution Architect review.

See [HCRL 004 / DR-11](hcrl-software-0.1.0-004.pending.md#dr-11-bounded-minutiae) and [carry-forward reconciliation](../../evidence/software-0.1.0/004/after/HCRL-Carry-Forward-Reconciliation.md).

<a id="dr-12-carry-forward-pragma-and-g03-reviews"></a>
## DR-12 — Carry-forward: Pragma publication/subscription and G03 reviews

HCRL 004 DR-12 remains **PENDING SOLUTION ARCHITECT REVIEW**. Preserve its complete candidate lineage, accepted-with-qualifications offline design records, and outstanding PF-07/PF-08, G03, identity, handoff, status, audit, trust, checkpoint and conformance obligations. This V001 task does not implement, incorporate or adjudicate those separate designs. See [HCRL 004 / DR-12](hcrl-software-0.1.0-004.pending.md#dr-12) and its linked evidence.

<a id="dr-13-v001-assignment-and-candidate-scope"></a>
## DR-13 — V001 assignment authority, backlog and exact candidate

The Solution Architect's [V001 dispositions](../../evidence/software-0.1.0/006/before/ai-agent-tasks/UNRESOLVED-mneme-mnemosyne-governed-persistence-v001-task.md) authorize Backlog Item `00002`, revision `1`, and implementation within the specified scope. The assignment's exact wording and both dispositions are retained in the Task. The candidate is the uncommitted worktree based on entry HEAD `8af315134a1f2e03e75512f66513ef2f7eeb80b8`; exact changed-file correspondence is listed in the [Implementation Report](../../evidence/software-0.1.0/006/after/ai-agent-reports/UNRESOLVED-mneme-mnemosyne-governed-persistence-v001-implementation-report.md). No Architecture change was proposed or discovered. The candidate remains unaccepted.

**Disposition requested** · Review the exact integrated candidate against the authorized V001 assignment and Backlog Item, with no software acceptance or release inferred from this HCRL's preparation.

<a id="dr-14-governed-non-fhir-informationunit-persistence"></a>
## DR-14 — Governed Non-FHIR InformationUnit persistence

**Actual candidate result** · The V001 stack composes Mneme `GovernedWriter`/`GovernedReader`, Themis PEPs, Mnemosyne controllers, the GPF and the Non-FHIR PostgreSQL service. The Testcontainers PostgreSQL 16 acceptance test passed four cases: governed IU create/read integrity, identical create replay, expected-version update and stale conflict, fresh-service-stack reconnection, unauthorized denial, and database connection failure without false success. The persisted UUID/canonical topic, metadata, representation, references and versions 1→2 were verified; historical versions remain append-only. See the [Implementation Report](../../evidence/software-0.1.0/006/after/ai-agent-reports/UNRESOLVED-mneme-mnemosyne-governed-persistence-v001-implementation-report.md).

**Disposition requested** · Review those actual results and the remaining development-only/production qualifications. No acceptance or lifecycle interpretation beyond this tested contract is claimed.

**Disposition requested** · Review actual end-to-end tests, implementation conformance, failure semantics and any unsupported conditions recorded in the Implementation Report.

<a id="dr-15-operational-status-ownership-and-persistence"></a>
## DR-15 — Operational Status ownership and persistence

**Actual candidate result** · The PostgreSQL integration test recorded and read owner-defined Status, updated its version from 1 to 2, and verified no IU identity row was created. Its opaque payload is not parsed or converted; owner/subject/type remains the Status key. The test passed against the V001 PostgreSQL 16 container.

**Disposition requested** · Review the actual Status representation, authorization, persistence/read results and negative cases without inferring unresolved Status-domain architecture.

<a id="dr-16-themis-authorization-and-outcome-durability"></a>
## DR-16 — Themis authorization and outcome/durability

**Actual candidate result** · Both Mneme and Mnemosyne reject IU writes when no Themis policy matches; Themis evaluator failure/null/deny is fail-closed in the PEP. The explicit development profile accepts only `LOCAL_TRANSACTION_COMMITTED`; the production cross-site profile maps local-only commit to `OutcomeUnknown` while retaining observed local assurance. PostgreSQL connection failure before commit returned `NotCommitted`; SQLSTATE 08006 during a synthetic commit-phase classifier test returned `OutcomeUnknown`. A real connection loss during COMMIT and production cross-site durability were not exercised. No production IU/Status Themis policies were introduced; authorized policies are test-scoped only.

**Disposition requested** · Review policy enforcement and tests, exact outcome mappings, PostgreSQL development configuration, and production-conformance qualifications. No failed or unexercised case is treated as a pass.

<a id="dr-17-fhir-persistence-boundary"></a>
## DR-17 — FHIR PersistenceService boundary

The FHIR PersistenceService remains outside the primary V001 scope. A small `FhirPersistenceServiceBinding` routes the existing `AuthoritativePersistencePort` through the GPF and qualifies only the existing local transaction; the Clinical persistence implementation was not rewritten. In the existing Testcontainers PostgreSQL 16 concurrency fixture, the bounded GPF create/update/read test passed along with the class's other tests (4 total). This does not claim FHIR production cross-site durability.

**Disposition requested** · Confirm the existing boundary was not weakened and review any optional, bounded integration evidence or explicit deferral.

<a id="dr-18-implementation-conformance-and-verification"></a>
## DR-18 — Implementation conformance and verification

The [Implementation Report](../../evidence/software-0.1.0/006/after/ai-agent-reports/UNRESOLVED-mneme-mnemosyne-governed-persistence-v001-implementation-report.md) records the exact source changes, reuse, Testcontainers PostgreSQL 16 results, Themis behavior, limitations and commands. Validation passed: the Hestia reactor completed all 11 modules, the architecture suite reported 90 tests across 37 modules, and full `mvn test` succeeded across 50 modules in 7:14. Maven's final Surefire result lines (`93` for the Hestia invocation and `158` for the full invocation) are module-level, not reactor-wide aggregates. `git diff --check` passed; every new production Java file is below 500 lines (maximum 320). No test or conformance failure remains known; a transient assertion-matcher mistake during development was corrected before the final passes. The software candidate represents the integrated Harmonia solution even though code changes are bounded to V001 modules.

**Disposition requested** · Review exact source correspondence and the reported PASS, FAIL, TIMEOUT, STALLED and UNRESOLVED outcomes. Any required verification not completed remains visible for explicit Solution Architect treatment; the agent does not accept its own implementation.

## Minutiae

- **Task-Issued-At and filename prefix:** **UNRESOLVED** because the attributable system timestamp of the operative assignment entering the interaction was not exposed. The timestamp is retained as unresolved in the Task filename and body; it must not be reconstructed from current time or file metadata.
- **Solution Architect disposition event timestamps:** not supplied in the interaction; retained as unresolved in the Task record rather than guessed.
- **Iteration 005:** existing evidence and the missing HCRL.005/layout-check prerequisite remain at their original standing under DR-11. The explicit iteration-006 authorization establishes this task's boundary only.
- **Report-Produced-At and filename prefix:** **UNRESOLVED** because the attributable system timestamp of report emission is not exposed; preserved as unresolved rather than inferred.
- **Accepted software predecessor:** not established by HCRL 004; no predecessor is inferred from branch, Maven version or passing tests.

---

**Architecture status** · No Architecture release increment, Continuum change, offline-design acceptance, publication or architecture decision is requested by this software-only implementation. Existing accepted authority and qualifications remain in force.

**Software status** · Pending implementation, exact-candidate verification and Solution Architect review. No accepted software predecessor, baseline or `software/v0.1.0` tag is asserted.
[foundation-reset]: hcrl-software-0.1.0-009.pending.md
[reset-assignment]: ../../evidence/software-0.1.0/009/before/ai-agent-tasks/unresolved-software-foundation-reset.md#issued-instruction
