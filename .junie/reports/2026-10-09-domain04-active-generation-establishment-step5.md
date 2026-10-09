# Domain04 — Active Generation Establishment and Failure Boundary — Step 5

**Date:** 2026-10-09. **Status:** COMPLETE within the authorised documentation/integration boundary. **Domain04 semantic sufficiency: NO.**

## Task Goal

Document the human-approved semantics for successful Active Generation
establishment, unsuccessful manifestation, its operational failure classification
and the effect of a failed subsequent attempt on an established Active Generation.
This completes the current Version / Active Generation semantic discussion.

This is Architecture Completion Programme stage 3, Domain04 reconciliation
Step 5. It is a bounded documentation/integration task, not convergence/runtime
implementation or broader managed-state reconciliation.

## Authority and Context Loaded

The task began from fresh repository-held context. Loaded:

- [Root AGENTS.md](../../AGENTS.md) and [docs/AGENTS.md](../../docs/AGENTS.md).
  No narrower scoped AGENTS.md applies to the edited paths.
- [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md),
  including current position, authority loading, task control and completion gates.
- [Canonical Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md),
  including the complete current AX-05 wording.
- Current [Domain04 orientation](../../docs/markdown/04-information-architecture/README.md)
  and [Information Architecture Metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md),
  especially Information Unit Context and existing §§6.5.1–6.5.5.
- Current [Information Lifecycle](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md),
  [Authority / Custody / Provenance](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md),
  [Assemblies and Views](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md)
  and the retained generic work/result ambiguity in the
  [Definition-to-Accountability pattern](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md#3-reference-example-2-task--work-progression).
- [Step 3 report](2026-10-09-domain04-information-unit-step3.md) and
  [Step 4 report](2026-10-09-domain04-version-active-generation-step4.md),
  as evidence/navigation only. Their recommendations do not authorise further work.
- The explicit human-approved Step 5 decisions, as authority for this increment.
  No implementation behaviour, earlier AI interpretation or report supplies them.

| Material authority | Consistency of this integration |
| :--- | :--- |
| **AX-04** | Harmonia establishes semantic outcomes without deriving transactions, cache operations or concurrency mechanisms from engine behaviour. |
| **AX-05** | Successful active manifestation remains distinct from authoritative durable Version progression; failed manifestation establishes no Generation or durable truth. Existing convergence/trust obligations are preserved. |
| **AX-06 / AX-10** | Operational manifestation failure does not by itself change information authority, information state or authoritative progression. |
| **AX-08 / AX-09** | A need to observe or record operational failure does not automatically turn it into information content, provenance or durable audit evidence. No recording mechanism is defined. |
| **AX-14** | The established Generation, unsuccessful attempt, operational outcome and source information retain separate meanings. |
| **AX-15 / AX-17** | Existing uncertainty obligations remain applicable. Unknown effect and absent architectural decisions are not adjudicated through this binary establishment definition. |

### Canonical Documentation Assessment

All approved establishment/failure knowledge is now in canonical Domain04.
Its interpretation requires no historical report or external implementation
document. Step 3's navigation to external state/history, Information Object /
Data Object and Pragma/Praxis dependencies remains a separate reconciliation
concern. Those sources were not loaded as authority or migrated here. No overall
corpus consolidation, terminology equivalence or implementation mapping is declared.

## Canonical Documentation Location

The existing [Information Unit Context §6.5](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#65-context)
owns this increment. New **§§6.5.6–6.5.9** integrate establishment, semantic
indivisibility, operational failure, information/operational separation, existing
Generation preservation and **AG-01–AG-07**. No new canonical document is created.

The existing §6.5.4 retained-question paragraph now points to this boundary and
retains manifestation initiation/realisation and Generation lifecycle questions,
without continuing to present this approved establishment decision as absent.
The Step 4 definitions, examples and VG-01–VG-08 are preserved. Only the
Completion Plan's current Domain04 status paragraph is additionally updated.

## Approved Establishment Semantics

> **Active Generation is a successful-state concept. An unsuccessful attempt to manifest an Information Unit does not create an invalid Active Generation; it fails to establish an Active Generation.**

The manifestation must be successfully established in full according to the
applicable governed architecture. Otherwise that attempt establishes no Active
Generation. The qualification "by this attempt" distinguishes failure to establish
another Generation from absence of all previously established Generations.

AG-01 and AG-03 preserve this boundary. An unsuccessful attempt does not create
an invalid, partial, incomplete, degraded or half-established kind of Generation.

Step 4 remains intact: Version identifies governed information progression;
Active Generation identifies a particular valid manifestation established for
active management or use. Multiple Generations MAY derive from the same Version;
Generation change does not imply Version change. Generation identifies the
manifestation rather than its user/process, is not time, and does not create
durable authoritative progression.

## Semantic Indivisibility

> **Active Generation establishment is semantically indivisible. Partial, degraded or incomplete manifestation SHALL NOT constitute an Active Generation.**

Runtime work may pass through partial, intermediate, degraded or failed
conditions. Those belong to manifestation activity/mechanics and acquire no
Active Generation standing. AG-02 supplies the semantic rule, without defining
implementation states or changing the Information Unit's potentially composite
structure.

AG-07 makes the limit explicit: semantic indivisibility is not transactional
atomicity. It requires no database/distributed transaction, atomic cache
operation, distributed lock, mutex, optimistic locking, MVCC, compare-and-swap,
synchronised, single-thread or single-node execution. No universal structural,
semantic, temporal, authority, security, provenance, relationship or cache
validation checklist is introduced.

## Manifestation Failure as Operational Failure

> **Failure to establish an Active Generation is an operational failure within Harmonia. It describes the unsuccessful manifestation activity and SHALL NOT, by itself, alter the Version or state of the Information Unit from which manifestation was attempted.**

AG-04 classifies the unsuccessful attempt. Its occurrence alone establishes
neither an invalid/changed source Version nor a new Version, lifecycle change,
defective or no-longer-authoritative information, or changed historical truth.
Any such consequence would require separate established architecture.

## Information State / Operational Activity Separation

> **Operational knowledge that Harmonia failed to manifest information is not, by itself, a change to that information.**

AG-05 preserves this distinction. The attempt/outcome belong to Harmonia's
operational activity; the Information Unit and Version remain governed by their
own information semantics. A downstream need to record, audit, monitor or
respond to failure does not make that failure Information Unit state.
No `InformationUnit.state = MANIFESTATION_FAILED` concept, equivalent attribute
or lifecycle transition is introduced.

## Existing Active Generation Preservation

> **Failure to establish a subsequent Active Generation SHALL NOT, by itself, invalidate an already established Active Generation.**

The canonical V7/G31 example shows successful establishment followed by a failed
later manifestation attempt. The later failure does not retroactively alter
G31's successful establishment. AG-06 establishes only that the other attempt's
failure is not, by itself, an invalidating event.

This establishes no indefinite lifetime, expiry, lease, freshness, fallback,
selection, replacement, invalidation trigger, precedence or current-generation
semantics. Subsequent loss of validity/activity and relationships between
Generations remain outside this task.

## AX-05 Consistency

**No material contradiction with current AX-05 was found. AX-05 is unchanged.**

Its current wording states:

> Mneme active-state generation SHALL represent successfully established active state. A valid active-state token SHALL identify an observed generation of that state.

> Failed or degraded convergence following authoritative state progression SHALL NOT establish or advance a valid active-state generation. Where the active representation cannot be successfully converged, its coordination state SHALL be treated as untrusted until reconciled with authoritative state.

The new semantics preserve successful establishment and the separation of
authoritative durable Version from valid active manifestation. Failure establishes
no new Generation and does not itself modify authoritative Version progression;
active manifestation does not manufacture durable truth. No active-state token
or concurrency design is derived.

The existing failed/degraded-convergence and untrusted-coordination requirements
remain applicable, including the Context paragraph already preserving them.
The narrow subsequent-attempt rule neither waives those requirements nor settles
coordination trust, reconciliation or Generation lifecycle. It does not make
untrusted coordination an invalid Active Generation produced by a failed attempt.

## Mechanics Explicitly Not Derived

No decision is made about:

- Manifestation initiation, performing component, data loading, Context assembly,
  completeness checking, coherence testing, success commit or failure detection.
- Operational failure representation, logging, audit, alerting, retry or recovery.
- Generation caching, distribution, replication, expiry, replacement, invalidation
  or concurrent interaction; transactional, persistence or concurrency machinery.
- Freshness, leases, precedence, selection, current-generation meaning, lifetime
  or subsequent loss of validity/activity.

How Harmonia implements, manages and recovers manifestation remains downstream
architecture and engineering. No attribute/schema validation framework or
manifestation implementation is introduced. The current conceptual branch closes
at the documented establishment/failure boundary.

## Other Domain04 Questions Preserved as Unresolved

The [canonical retained questions](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#11-downstream-derivation-and-retained-questions)
and generic Task/work ambiguity are preserved:

- Generic work/result meaning and its relationship to Work Order, To Do and
  synthetic Task.
- Search-result management/atomicity, including management acceptance for
  individual resources, result sets, neither or both.
- Episode responsibility, Observation/Finding semantics, assurance/evidence,
  contextual capacity and Digital Twin information structures.
- Residual candidate-family approval, Information Object / Data Object
  terminology, Pragma/Praxis mapping and PetasosParcel mapping.
- Broader managed-state/history reconciliation and relationships to snapshots,
  information lifecycle, entity state, Praxis state or uncertain effect.

No whole finding is closed because this Version / Active Generation branch is
complete, and no unresolved question is silently answered.

## Validation

### Domain04 local-link validation

`python3 /tmp/domain04-step5-links.py`: **PASS, exit 0** — all **25 Domain04
Markdown files**, **585 local link occurrences**, **93 closed fences**, zero
missing-file/anchor errors and zero structural errors. Local targets outside
Domain04 are included. The inspected validator uses markdown-it-py CommonMark
with tables, inline/image/reference links, HTML links/explicit anchors and
GFM-style heading identifiers. This is local target/structure validation, not a
full renderer build or proof of semantic conformance.

### Whitespace and report/plan checks

`git diff --check`: **PASS, exit 0**, no whitespace diagnostics. Final checks also
cover this new untracked report's whitespace/final newline, required sections
and all 14 required report sections. `python3 /tmp/domain04-step5-report-plan-links.py`:
**PASS, exit 0** — report and Completion Plan, **40 local links**, **five closed
fences**, zero link/structural errors. The Domain04 check is refreshed after
report completion; the passing architecture suite is not rerun without cause.

### Bounded architecture suite

Executed once using native `exec_command`, a 10-second launch yield, a
600-second command bound, 15-second termination grace and a two-minute
no-progress investigation threshold. Completion waiting used `write_stdin`
with a 30-second yield. No stall, timeout, termination or rerun occurred.

```bash
timeout --signal=TERM --kill-after=15s 600s mvn test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS / BUILD SUCCESS, exit 0: 90 tests across 11 architecture suites;
0 failures, 0 errors, 0 skipped.** This matches the expected baseline.
Maven duration: **16.802 seconds**; completion: `2026-10-09T22:20:04+11:00`.
Surefire XML totals were checked against this run's log and each suite's presence
in it. All eight AGENTS.md-named suites ran, plus GovernedWriteComposition,
GovernedWriteContract and MnemosyneAuthoritativePersistence architecture suites.

Evidence: `/tmp/domain04-step5-architecture-tests.log`,
`/tmp/domain04-step5-test-summary.json` and generated architecture Surefire XML
reports. This verifies the selected architecture reactor, not full-repository
behaviour, runtime/deployment readiness or the documentation's semantic meaning.

**Environment/dependency warnings, separately from failures:** four Maven
warnings concern unwritable Jakarta Mail resolver tracking and Central/OSS
snapshot metadata lock files under the read-only `.m2/repository`. Cached
dependencies suffice. ArchUnit detected **Java 21.0.12.1**; no Java/ArchUnit
compatibility warning was emitted. No environment, dependency, production or
test repair was performed.

### Changed-file inventory

This task changes **three files**, measured against the incoming worktree:

| Task file | Step 5 change |
| :--- | :--- |
| [Information Architecture Metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#656-establishment-and-semantic-indivisibility) | New §§6.5.6–6.5.9 and bounded §6.5.4 retained-question integration only. |
| [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md#4-current-programme-position) | Minimum Domain04 status-paragraph update: Step 5 complete, current conceptual branch closed, sufficiency remains NO. |
| [This Step 5 report](2026-10-09-domain04-active-generation-establishment-step5.md) | New completion/validation evidence. |

### Scope / immutability confirmation

The incoming worktree already contains the Step 1–4 Domain04/Plan changes and
four untracked reports. `/tmp/harmonia-domain04-step5-baseline` captures the
incoming diff, status, SHA-256 manifest of **2,293 tracked/input files** and
documentation copies before this task's edits. Preservation comparison confirms
that only the two existing files above change: **2,291 other baseline files
remain byte-identical**, none are missing and this report is the sole new input
file. The metamodel outside this integration and the Plan outside its Domain04
status paragraph are separately checked for preservation. Evidence:
`/tmp/domain04-step5-preservation.json` and `/tmp/domain04-step5-task.diff`.

**Implementation unchanged; tests unchanged; Domain01–03 semantics unchanged;
canonical axioms unchanged; Domain05+ unchanged.** No schema, manifestation
implementation, cache/concurrency mechanism or Generation lifecycle mechanism is
introduced. No broader Domain04 question is resolved. Earlier task changes and
reports are preserved; Maven writes generated output only. The Plan's sequence,
completion gates and Domain03 completion boundary remain unchanged. This
documentation task has no deployment prerequisite and authorises no later step.

## Semantic Sufficiency

> Is Domain04 now semantically sufficient to support Domain05 completion without Domain05 inventing missing Information Architecture?

**NO.** The Version / Active Generation branch now has sufficient approved
establishment/failure semantics and is closed. The existing generic Task/work
meaning, search-management acceptance, contextual assurance/capacity and residual
family questions still require Information Architecture adjudication. Domain05
completion would require inventing some of those information meanings.

Unanswered manifestation mechanics, Generation management/recovery and deferred
schemas are downstream concerns; they are not a reason to deepen this closed
branch or force a different sufficiency assessment. No overall Domain04
completion or Domain05 authorisation follows from Step 5.

## Recommended Next Human Adjudication

**Separately adjudicate one information question: what does the existing
`FulfillmentTask` denote as information, and how is that meaning distinguished
from the underlying work it describes?** The current canonical Task/work pattern
explicitly retains that represented-information versus underlying-work ambiguity.
This is a bounded part of the generic work/result question, not a task lifecycle,
execution model, full taxonomy or application mapping. Existing Business
clinical-work authority and explicitly assigned Harmonia responsibilities remain
controlling; other work/result meanings remain separately unresolved.

This recommendation is non-authoritative and has not begun. **Step 5 stops after
canonical integration, validation, this report and the minimum Completion Plan
status update. No deeper Active Generation mechanics or broader managed-state
reconciliation is commenced.**
