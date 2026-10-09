# Domain04 — Version and Active Generation Semantics — Step 4

**Date:** 2026-10-09. **Status:** COMPLETE within the authorised documentation/integration boundary. **Domain04 semantic sufficiency: NO.**

## Task Goal

Document the human-approved distinction between Information Unit Version and
Active Generation, explain why both concepts exist, and integrate their semantic
relationship into the Information Unit Context established by Step 3. This is
Architecture Completion Programme stage 3, Domain04 reconciliation Step 4;
it is not convergence/runtime implementation or completion of the managed-state
model.

## Authority and Context Loaded

The task began from fresh repository-held context. Loaded:

- [Root AGENTS.md](../../AGENTS.md) and [docs/AGENTS.md](../../docs/AGENTS.md).
  No narrower scoped AGENTS.md applies to the edited paths.
- [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md),
  including current position, context loading, task control and completion gates.
- [Canonical Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md),
  including the current complete AX-05 wording.
- Current [Domain04 orientation](../../docs/markdown/04-information-architecture/README.md)
  and [Information Architecture Metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md),
  especially Information Unit §§5–11 and Context §6.5.
- [Information Lifecycle](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md)
  and the relevant Unit/search boundary in
  [Assemblies and Views](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md#1-information-assembly-vs-information-view),
  Unit governance integration in
  [Authority / Custody / Provenance](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md),
  and the retained generic Task/work ambiguity in the
  [Definition-to-Accountability pattern](../../docs/markdown/04-information-architecture/patterns/definition-to-accountability.md#3-reference-example-2-task--work-progression).
  Targeted Domain04 terminology searches checked Version/Generation usage and
  existing unresolved boundaries; no unrelated family definition was derived.
- [Step 3 report](2026-10-09-domain04-information-unit-step3.md), as evidence and
  navigation only, and the explicit human-approved Step 4 decisions as authority
  for this bounded integration.

| Material authority | Consistency of the integration |
| :--- | :--- |
| **AX-04** | Information semantics precede engine realisation; no existing implementation behaviour defines the distinction. |
| **AX-05** | Valid active manifestation and authoritative durable progression remain distinct; existing failed/degraded-convergence and untrusted-state requirements remain applicable. |
| **AX-08 / AX-09** | Manifestation or active use does not automatically constitute durable evidence or history; no Generation retention rule is inferred. |
| **AX-14** | Version, Active Generation, process identity and Temporal Context retain their distinct meanings. |
| **AX-15 / AX-17** | Uncertain effect and unestablished validity, lifecycle, convergence, search and work semantics remain explicit rather than inferred. |

**No material contradiction between the approved wording and AX-05 was found.**
No axiom, upstream responsibility or downstream implementation is changed.

### Canonical Documentation Assessment

The approved Version/Active Generation knowledge now resides in canonical
Domain04 and requires no external implementation document or historical report
to establish its meaning. Step 3's navigation to external terminology,
state/history and Pragma/Praxis dependencies remains a separate reconciliation
concern. Those sources were not loaded as authority or migrated here. The task
does not establish their equivalence with Information Unit or declare overall
corpus consolidation complete.

## Canonical Documentation Location

The existing [metamodel Context §6.5](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#65-context)
contains the integrated architecture. Its two concern-table entries now use the
approved meanings; new **§§6.5.1–6.5.5** explain the relationship, observability,
Temporal Context, durable boundary, unresolved realisation and VG-01–VG-08.
No new canonical document or Domain04 restructuring is required. Existing
navigation already reaches this Context section. Only the Completion Plan's
current Domain04 status paragraph is additionally updated.

## Version Definition

> **Version identifies a governed progression of an Information Unit's representation.**

Version concerns governed information progression. The illustrative V7 and V8
labels define no identifier syntax, sequence mechanism or numbering scheme.

## Active Generation Definition

> **Active Generation identifies a particular valid manifestation of an Information Unit established for active management or use.**

Active Generation concerns active manifestation. It is complementary to Version,
not another description or form of Version. The distinction does not complete
what makes a manifestation valid.

## Observability Rationale

Independent processes/functions can act upon manifestations of the same Version
established at different observable points. The canonical example places Process
A against G21, Process B against G22 and Process C against G23, while all three
manifestations derive from V7. They can therefore share governed information
progression while acting against different active manifestations; Version alone
cannot express that distinction.

The process arrows illustrate use, not creation, assignment or ownership. The
example does not require a Generation per process: multiple processes/functions
may legitimately use the same Active Generation. Observability here establishes
neither wall-clock differentiation nor global/local visibility rules.

## Semantic Relationship

- Multiple Active Generations **MAY derive from one Version**. A Version may
  have no current Active Generation; no maximum is established.
- A change of Active Generation **does not by itself imply Version change**,
  governed information progression or a new authoritative durable Version.
- Active Generation **identifies the manifestation rather than the process**
  or function using it. No process-to-Generation cardinality or affinity is defined.
- Active Generation **is not time**: it is neither a timestamp nor a measure of
  time. Temporal Context may explain establishment, applicability or observation;
  one Generation may remain valid across an interval, and different Generations
  need not be differentiated by wall-clock time.

The canonical hierarchy and process example use illustrative labels only.
VG-01–VG-08 record exactly the eight approved semantic guardrails; no further
guardrail identifier is introduced.

## AX-05 / Durable Authority Boundary

> **An Active Generation SHALL NOT be treated as an authoritative durable Version merely because Harmonia has manifested or acted upon it.**

AX-05 assigns authoritative durable state/version progression to Mnemosyne and
active representation/use/coordination to Mneme. Their concurrency domains remain
distinct. Active use does not manufacture durable authoritative progression.

AX-05 already requires successfully established active state. Failed or degraded
convergence following authoritative state progression SHALL NOT establish or
advance a valid active-state generation; coordination remains untrusted until
reconciled where active convergence cannot be successfully established. The
existing canonical paragraph preserves that boundary. Step 4 adds no sufficiency
criteria, convergence algorithm or token mechanism.

It does not follow that every Generation must be persisted, becomes or produces
a Version, is durable, constitutes historical truth, or constitutes evidence.
Existing evidence/lifecycle obligations retain their own scope. AX-05 is unchanged.

## Information Unit Integration

Version and Active Generation remain conceptual concerns within Information Unit
Context. Context membership does not require simple scalar fields. No canonical
VersionId/GenerationId attribute, datatype, identifier scheme, Information Unit
schema or downstream mapping is introduced; existing dotted notation remains
explicitly illustrative.

The semantics may later inform Mneme active information/cache-instance management,
concurrent manifestations, Mnemosyne authoritative durable representation,
convergence, conflict handling and reconstruction. Domain04 establishes meaning
before downstream architecture determines realisation. No cache key/generation,
lock, MVCC, database-column, session, sharding, replication, consistency or
thread/process coordination decision is introduced.

## Deliberately Unresolved Managed-State Questions

The canonical integration preserves these questions without answering them:

- Generation creation triggers, validity beyond applicable AX-05 requirements,
  lifetime, retirement and cessation of active status.
- Ownership, global/local visibility, and process, cache, node or cluster affinity.
- Concurrent-Generation reconciliation, active convergence and uncertain effect.
- Persistence, retention, historical-truth/evidence association and relationships
  to snapshots, lifecycle state, Praxis progression and entity state.

Only the approved existence possibilities and distinctions are established.
The broader managed-state/history model has not begun.

## Search / Work Semantics Preserved as Unresolved

Search remains separately unresolved: whether retrieval creates a Generation,
whether each returned Information Unit or a result set has a Generation, whether
results become Harmonia-managed, or whether search creates Versions. The existing
search-management question and its individual-resource/result-set/neither/both
alternatives remain intact.

IA13 remains deferred. No definition of ActionableTask, FulfillmentTask,
TaskOutcome, ReportedTask, Work Order, To Do or synthetic Task is introduced or
revised. Assurance, evidence and capacity questions are also preserved; this
distinction closes none of those broader findings.

## Validation

### Domain04 local links and whitespace

`python3 /tmp/domain04-step4-links.py`: **PASS, exit 0** — all **25 Domain04
Markdown files**, **584 local link occurrences**, **91 closed fences**, zero
missing-file/anchor errors and zero structural errors. Local targets outside
Domain04 are included. The inspected validator uses markdown-it-py CommonMark
with tables, inline/image/reference links, HTML links/explicit anchors and
GFM-style heading identifiers. This establishes local targets and structure,
not a full renderer build or semantic conformance proof.

`git diff --check`: **PASS, exit 0**, no whitespace diagnostics. Final checks
also cover the untracked report's whitespace/final newline, required report
sections, and report/Completion Plan local links.

### Bounded architecture suite

Executed once using native `exec_command` with a 10-second launch yield, a
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
0 failures, 0 errors, 0 skipped.** The expected baseline is matched. Maven
duration: **17.327 seconds**; completion: `2026-10-09T22:05:17+11:00`. Surefire
XML totals were checked against this run's log and each suite's presence in it.
All eight AGENTS.md-named suites ran, together with GovernedWriteComposition,
GovernedWriteContract and MnemosyneAuthoritativePersistence architecture suites.

Evidence: `/tmp/domain04-step4-architecture-tests.log`,
`/tmp/domain04-step4-test-summary.json` and the generated architecture Surefire
XML reports. This is the selected architecture reactor, not full-repository
testing or runtime/deployment verification.

**Environment/dependency warnings, separately from failures:** four Maven
warnings concerned unwritable Jakarta Mail resolver tracking and Central/OSS
snapshot metadata lock files under the read-only `.m2/repository`. Cached
dependencies sufficed. ArchUnit detected **Java 21.0.12.1**; no Java/ArchUnit
compatibility warning was emitted. No environment, dependency, production or
test repair was performed.

### Changed-file inventory

This task changes **three files**, measured against the incoming worktree:

| Task file | Change |
| :--- | :--- |
| [Information Architecture Metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#65-context) | Two Context concern meanings and new §§6.5.1–6.5.5 only. |
| [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md#4-current-programme-position) | Minimum Domain04 status-paragraph update: Step 4 complete, semantics linked, sufficiency remains NO. |
| [This Step 4 report](2026-10-09-domain04-version-active-generation-step4.md) | New completion/validation evidence. |

### Scope / immutability confirmation

The incoming worktree contained the prior Domain04/Plan modifications and
untracked Step 1–3 reports. `/tmp/domain04-step4-baseline.json` captures **2,292
tracked/input files** before editing; incoming edit-target copies are under
`/tmp/domain04-step4-before`. SHA-256 comparison confirms that only the two
existing files above change, with **2,290 other baseline files byte-identical**,
no missing files and this report as the sole new input file. The metamodel
outside the two concern entries/new subsections and the Plan outside its
Domain04 status paragraph are separately checked for preservation. Evidence:
`/tmp/domain04-step4-preservation.json` and `/tmp/domain04-step4-task.diff`.

**Implementation and tests are unchanged; Domain01–03 semantics, canonical
axioms and Domain05+ are unchanged.** Maven produces generated output only.
No Information Unit schema, Mneme/Mnemosyne implementation decision, candidate
family approval or search/work/assurance/capacity resolution is introduced.
Existing prior changes and historical reports remain preserved. No later
reconciliation step or convergence/runtime activity was commenced.

## Semantic Sufficiency

> Is Domain04 now semantically sufficient to support Domain05 completion without Domain05 inventing missing Information Architecture?

**NO.** The approved distinction is now explicit and explains why two valid
active manifestations can represent the same Version while being different
Active Generations. It does not supply the remaining managed-state/history,
search-result management, generic work/result, assurance/capacity or residual
family semantics. The canonical metamodel's retained questions and the generic
Task/work ambiguity continue to require separate adjudication. Domain05
completion would still require inventing missing information meaning.

Deferred schemas and implementation mechanisms are appropriate downstream
derivations, not themselves a reason for NO. No whole-domain sufficiency
improvement or completion is inferred merely because Step 4 is documented.

## Recommended Next Human Adjudication

**Separately adjudicate one question: what minimum information-level conditions
make a manifestation a valid Active Generation, beyond AX-05's existing
convergence guardrail?** This is a non-authoritative recommendation for a new
bounded task, not a validity rule or authorisation to proceed. Creation triggers,
lifetime, visibility, ownership, reconciliation, persistence, search and work
semantics should remain separately bounded rather than folded into that answer.

Step 4 stops after canonical integration, validation, this report and the
minimum Completion Plan update. The recommended question has not begun.
