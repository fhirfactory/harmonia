<!--
  Copyright (c) 2026 Mark Hunter
  SPDX-License-Identifier: GPL-3.0-or-later
-->

# Domain04 — Formal Semantic Completion Baseline

**Date:** 2026-10-10. **Programme:** Harmonia R1.x/R2.x Architecture Completion,
stage 3 completion and transition to stage 4.
**Standing:** bounded execution evidence; the authoritative completion status
is recorded in RADS, not established by this report.

**Domain04 — Information Architecture is formally baselined as semantically complete for the agreed Harmonia R1.x/R2.x scope.**

**The Architecture Completion Programme may proceed to Domain05 — Application Architecture.**

## 1. Authorised Completion and Architectural Consistency

This task records the explicitly authorised completion decision following the
existing reconciliation, adjudication and Domain Completion Gate reassessment.
It performs no further residual-gap assessment or architectural adjudication.

The [repository instructions](../../AGENTS.md),
[documentation instructions](../../docs/AGENTS.md) and
[Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md)
govern the action. [AX-14](../../docs/markdown/governance/architectural-axioms.md#ax-14)
is preserved by leaving existing information distinctions unchanged.
[AX-17](../../docs/markdown/governance/architectural-axioms.md#ax-17)
is preserved by retaining explicit uncertainty, separating historical task
limits from current completion status, and requiring governed upstream review
of any genuine downstream-discovered contradiction or missing semantic
requirement. No axiom or Information Architecture concept is introduced.

The recorded completion means that sufficient authoritative information meaning
exists for downstream architecture to be derived without inventing Information
Architecture semantics. It establishes a baseline for the agreed scope, not
immutability, exhaustive modelling, final schemas/cardinalities, completed
implementation mechanisms, physical legacy retirement or answers to all future
questions. Future Domain04 evolution remains subject to normal architecture
governance. No final whole-programme R1.x/R2.x baseline is declared.

## 2. Files Changed and Status Recorded

| File | Bounded change |
|---|---|
| [Domain04 index](../../docs/markdown/04-information-architecture/README.md#completion-status-and-semantic-baseline) | Adds the dated semantic completion declaration, satisfied gate, evidence references, R01 closure, retained-deferral references and downstream derivation boundary. Changes the Task navigation sentence to past tense so its earlier bounded reconciliation limit is not presented as the current domain status. |
| [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md#4-current-programme-position) | Records Domain04 completion on 2026-10-10, gate satisfaction, preserved deferrals and Domain05 as NEXT. Marks the Information-to-Application derivation boundary. Moves the prior Step 5 paragraph unchanged into an explicitly historical subsection; retains Domain03's accepted outcome and the rest of the programme's governance and future stages. |
| [This report](2026-10-10-domain04-semantic-completion-baseline.md) | Records the authorised action, its evidence, preservation checks and actual validation results. |

The authoritative declaration begins:

> **Domain04 — Information Architecture is semantically complete for the agreed
> Harmonia R1.x/R2.x scope.** The Domain04 architecture provides sufficient
> authoritative information meaning for downstream architectural derivation
> without requiring downstream domains to invent Information Architecture
> semantics. Known retained uncertainties and explicit deferrals remain governed
> and do not prevent this baseline. Future changes to Domain04 remain subject to
> normal architecture governance.

## 3. Completion Evidence and Gate

The RADS index references the existing evidence succinctly:

- [Substantive reconciliation / responsibility and trace corrections](2026-10-09-domain04-information-architecture-reconciliation-step2.md).
- [Managed-state/history assessment of the already closed decisions](2026-10-10-domain04-residual-semantic-gap-assessment.md#managed-state-and-history).
- [Task reconciliation and observable-information/domain-meaning boundary](2026-10-10-domain04-task-information-reconciliation.md),
  [TaskOutcome refinement](2026-10-10-domain04-taskoutcome-refinement.md)
  and the [canonical observable-information guardrail](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md).
- [Residual semantic-gap assessment](2026-10-10-domain04-residual-semantic-gap-assessment.md)
  and subsequent [search-semantics reconciliation / completion-gate report](2026-10-10-domain04-search-semantics-reconciliation.md),
  including canonical documentation, gate and repository-validation evidence.

The existing [gate reassessment](2026-10-10-domain04-search-semantics-reconciliation.md#6-domain-completion-gate-reassessment)
records **PASS** for agreed-scope semantic sufficiency, truthful upstream
traceability and required knowledge available canonically; it records **PASS
WITH EXPLICIT DEFERRAL** for contradictions/retained uncertainty and legacy
knowledge treatment. This authorised action records **Domain Completion Gate:
SATISFIED** on that basis, without repeating the assessment.

Reports remain supporting execution evidence. Canonical information meaning
continues to reside in Domain04 RADS; the user's authorised declaration and
canonical completion record establish the baseline status.

## 4. Explicit Deferrals and Resolved Search Finding

Explicit deferrals retain their existing scope and standing. The index points
to [Task / Work §5](../../docs/markdown/04-information-architecture/information-families/task-work.md#5-explicitly-unresolved-architecture),
[metamodel §11](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#11-downstream-derivation-and-retained-questions),
[search retained boundaries](../../docs/markdown/04-information-architecture/patterns/search-results.md#5-resolution-and-retained-boundaries)
and the [preceding completion evidence's retained-deferral record](2026-10-10-domain04-search-semantics-reconciliation.md#4-previous-finding-and-retained-deferrals).
No new deferral catalogue or candidate approval is created.

ReportedTask remains pending Assurance/Accountability adjudication. Retained
TaskOutcome identity/cardinality/lifecycle questions, completion metadata and
Task.Output detail, assignment/execution and Derived-output mechanisms,
snapshot/export mechanisms, detailed Audit/Assurance, and downstream allocation,
persistence, cache, API, Integration and Technology concerns remain deferred.
Completion does not prematurely resolve any of them.

**R01 — the previous sole Category A search-returned-information finding — is
recorded as resolved. No Category A Domain04 semantic blocker remains, and this
status-only change introduces no new Category A finding.** The canonical
[two-case search decision](../../docs/markdown/04-information-architecture/patterns/search-results.md)
and existing [closure evidence](2026-10-10-domain04-search-semantics-reconciliation.md#4-previous-finding-and-retained-deferrals)
are referenced without elaboration or amendment. The earlier residual report
is preserved unchanged as dated pre-adjudication evidence.

## 5. Programme Transition and Scope Preservation

The plan now shows Domain03 semantically complete (2026-10-09), Domain04
semantically complete (2026-10-10), and **Domain05 — Application Architecture:
NEXT**. Domain05 may determine how Harmonia realises the Information Architecture;
it SHALL NOT silently redefine, replace or infer alternative information meaning
for application convenience. Genuine upstream contradictions or missing semantic
requirements must be explicitly raised through normal architecture governance.

**No Domain05 architecture, reconciliation or derivation was undertaken.** Its
next task remains separately bounded. No runtime/convergence implementation
milestone was commenced. Domain01–03 semantics, axioms, accepted information
decisions and implementation code remain unchanged.

The task began with earlier search-reconciliation changes and two reports
already present. A pre-task SHA-256 snapshot covers 2,301 existing repository
files, including these additions. Only the two intended existing RADS documents
change relative to that snapshot; the other 2,299 files remain byte-identical.
This report is the sole task addition. The prior Step 5 paragraph remains
verbatim in the plan's historical subsection; plan sections 5–11 remain
byte-identical. Apart from the new completion section and the Task navigation
sentence's temporal clarification, the Domain04 index remains unchanged.

## 6. Legacy-Doco Treatment and Canonical Documentation Assessment

No new broad legacy search, review or reconciliation was performed. The
accepted [canonical documentation assessment and legacy dispositions](2026-10-10-domain04-search-semantics-reconciliation.md#5-canonical-documentation-assessment-and-legacy-treatment)
and [preceding residual assessment](2026-10-10-domain04-residual-semantic-gap-assessment.md#7-canonical-documentation-assessment-and-legacy-dependencies)
remain the bounded evidence. They concluded that **no architectural knowledge
required to understand Domain04 for the agreed scope remains solely outside
RADS**. This action records that established conclusion, not a new whole-corpus
assessment.

Relevant material previously identified outside RADS includes:

| Prior source material | Knowledge and canonical owning context | Treatment in this task / remaining work |
|---|---|---|
| [Architecture decisions](../../docs/architecture-decisions.md), including accepted replay distinctions | Operation-specific recovery/replay contracts; governance and later Application/Integration concerns alongside Domain04's already canonical generic work/history boundaries. | Unchanged; no incorporation. Separately authorised downstream/corpus ADR reconciliation remains deferred as previously recorded. |
| [Execution model](../../docs/architecture/execution-model.md), [Pragma](../../docs/concepts/pragma.md), [Praxis](../../docs/concepts/praxis.md), legacy [Information](../../docs/latex/chapters/03-information-architecture.tex) and [Data Architecture](../../docs/latex/chapters/04-data-architecture.tex) | Execution, envelope, representation and persistence implementation guidance; principally Application/Integration/Technology ownership. | Unchanged; no incorporation. Existing downstream execution/representation reconciliation and prior supersession treatment remain in force. |
| [Memory/recovery navigation](../../docs/memory-recovery.md), [Provider Registry search guidance](../../docs/provider-registry/search.md), earlier plans/reports | Historical navigation, external-response examples, implementation support and decision-time evidence; no sole source of required current Domain04 meaning. | Retained as historical/supporting material. Any later provider/API or documentation reconciliation requires its own authorised task. |

**Legacy changes: none.** No content is migrated, rewritten or deleted, and no
new supersession wrapper is introduced. Completion does not certify physical
retirement of legacy documents or final programme-wide canonical consolidation.

## 7. Validation Results

**PASS — prescribed architecture suite:** 90 tests across 11 classes, zero
failures, errors or skipped tests. Maven BUILD SUCCESS, exit 0, 25.713 seconds;
Surefire XML totals and suite names were independently inspected. All eight
AGENTS.md architecture suites ran, together with GovernedWriteComposition,
GovernedWriteContract and MnemosyneAuthoritativePersistence.

```bash
timeout --signal=TERM --kill-after=10s 300s mvn -o test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false -B -ntp
```

Offline execution uses the cached dependencies that passed in the preceding
task and avoids its documented read-only Maven-cache resolution problem. The
known Java 25 ArchUnit import limitation and Maven/Guava deprecated
`sun.misc.Unsafe` warnings remain environmental limitations; reported assertions
completed successfully. These tests verify executable architectural guardrails,
not semantic completeness or approval. No timeout, stall or forced termination
occurred; no wider runtime test was required for this status-only action.

**PASS — documentation checks:** CommonMark/table parsing, one H1 per file,
closed code fences, local file/heading/HTML-anchor targets and incoming links
to the changed RADS documents: **30 Markdown files, 734 local link occurrences,
50 incoming links and 105 fenced blocks; zero missing-target/anchor or structural
errors**. The scope includes all 28 Domain04 Markdown documents, the Completion
Plan and this report. No packaged repository
Markdown/link validator was located; the preceding task's Markdown-it-py
checker was adapted for this action's two-document scope and preservation
snapshot. Each check is bounded by 60 seconds with a five-second termination
grace. External content is not fetched and Mermaid fences are not renderer-tested.

**PASS — changed-document whitespace and preservation:** whitespace and
SHA-256 checks establish the scope described in §5; both `git diff --check`
and `git diff --cached --check` pass. Three already-recorded
trailing-whitespace lines remain in unchanged
`information-families/README.md` (67, 78) and `healthcare-service.md` (111);
they were not cleaned up or represented as a clean whole-corpus whitespace
result. Authoritative links remain valid, existing deferrals remain intact,
and no information semantics, upstream traceability, legacy content or
implementation code is altered beyond recording the authorised baseline.

Temporary supporting evidence: `/tmp/harmonia-domain04-completion-baseline.json`,
`/tmp/harmonia-domain04-completion-validate.py`,
`/tmp/harmonia-domain04-completion-validation.json` and
`/tmp/harmonia-domain04-completion-architecture-tests.log`. These execution
artefacts are not architectural authority and may not survive cleanup.
