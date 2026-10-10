<!--
  Copyright (c) 2026 Mark Hunter
  SPDX-License-Identifier: GPL-3.0-or-later
-->

# Domain04 — Search Semantics Reconciliation and Completion Gate Reassessment

**Date:** 2026-10-10. **Programme position:** Architecture Completion Programme
stage 3, Domain04 Information Architecture Reconciliation.
**Standing:** execution and assessment evidence; not architectural authority or
a formal Domain04 completion declaration.

The authorised two-case adjudication is reconciled into RADS. **The previous
sole Category A finding, R01, is resolved. No residual Category A finding was
identified as a direct consequence of this reconciliation.** All five Domain
Completion Gate criteria pass or pass with legitimate explicit deferral.
**Domain04 appears semantically sufficient for the agreed R1.x/R2.x scope.**
No RADS domain-status or formal completion declaration is made; Domain05 is
not commenced.

## 1. Sources Reviewed and Architectural Consistency

Repository-held authority and the explicit task adjudication supplied the
decision. Earlier reports assisted navigation and classification rather than
establishing architecture.

| Sources reviewed | Use |
|---|---|
| [Root AGENTS.md](../../AGENTS.md), [documentation instructions](../../docs/AGENTS.md), [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md), especially §§5–9 | Authority hierarchy, bounded scope, RADS/legacy preservation, semantic reconciliation and all five Domain Completion Gate criteria. No narrower instruction applies. This is architecture completion documentation, not convergence/runtime implementation. |
| [Canonical Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md), materially AX-01–09, AX-12–18 | Information purpose, representation independence, machinery versus semantics, active/durable state, authority, governed security context, meaningful evidence, ingress/egress, preserved distinctions and operational/architectural uncertainty. |
| [Domain04 index](../../docs/markdown/04-information-architecture/README.md) and [metamodel](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md), §§1–11 | Existing categories, explicit management responsibility, Unit boundaries, Version versus Active Generation, downstream reservations and the obsolete generic search question. |
| [Containment and Collections](../../docs/markdown/04-information-architecture/patterns/containment-and-collections.md), [Assemblies and Views](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md) | Collection membership without content containment/ownership; independently governed constituents; distinct dynamic assembly and snapshot meanings. |
| [Observable Information and Domain Meaning](../../docs/markdown/04-information-architecture/guardrails/observable-information-and-domain-meaning.md) | Established semantic opacity and governed Context Loading → Ergo Logic Process → Context Unloading boundary; optional runtime AI remains unchanged. |
| [Information Lifecycle](../../docs/markdown/04-information-architecture/governance/information-lifecycle.md), [Authority, Custody and Provenance](../../docs/markdown/04-information-architecture/governance/authority-custody-provenance.md) | Concept-specific progression, immutable history, authority distinct from custody/consumption and management; evidence and operational context are not automatically persistent content. |
| [Domain03 traceability](../../docs/markdown/04-information-architecture/traceability/domain03-traceability.md), [Task/Work](../../docs/markdown/04-information-architecture/information-families/task-work.md), especially retained questions | Established owner/meaning derivation; preserve TaskOutcome detail and ReportedTask reservations without reopening approved Task semantics. |
| [G1 current disposition](../../docs/markdown/04-information-architecture/reviews/package2-g1-review.md), [G2 standing/current uncertainty](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md), [Dokimasia current disposition](../../docs/markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md) | Closed versus candidate/reserved decisions, dated history and established independent-assurance boundaries; no package approval or new assurance allocation inferred. |
| [Strategy EC-04/05](../../docs/markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-05-search--discovery), [FEAT-ISE-05/08/09](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md#detailed-capability--feature-catalogue-intrinsic--shared-enablement) | Information management, bounded discovery, retrieval mediation and access-controlled search. |
| [Business Intrinsic Enablement](../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md), particularly §§2.1–2.3, 2.9; [Business ownership matrix](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix) | Historical preservation, federated retrieval, search/access filtering, Query Execution Context & Projection State, and generic activity coordination without source/domain ownership transfer. |
| [Immediately preceding residual assessment](2026-10-10-domain04-residual-semantic-gap-assessment.md), R01–R43, Category A brief, canonical assessment and gate | Required assessment input; R01 was the sole A finding. B/C/D distinctions and legacy-only replay debt are checked against current RADS, not promoted from the report into authority. The preceding report remains unchanged historical evidence. |

The approach preserves AX-05 by referencing Versions without substituting
Active Generations or cached state. AX-06/18 preserve external originating and
domain authority despite governed acquisition. AX-07–09 retain security context
and evidence obligations without turning all search metadata or machinery into
audit content. AX-13 preserves external publication semantics. AX-14/17 retain
the two search cases and genuine downstream uncertainty without invented
relationships. No axiom conflict was identified; no axiom or Domain01–03
meaning was changed. No implementation inspection or modification was needed
to derive these semantics.

## 2. RADS Changes

| File | Bounded change |
|---|---|
| [patterns/search-results.md](../../docs/markdown/04-information-architecture/patterns/search-results.md) — new | Authoritative two-case search semantics, Search Result Set collection meaning, governed Unit boundary, Version-reference membership, metadata categories, separate snapshot/export acts, external Ergo acquisition, opacity, Audit/Assurance distinction and retained downstream scope. |
| [metamodel/information-architecture-metamodel.md](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#111-adjudicated-search-management-boundary) | Replaces §11's generic unresolved acceptance question with the adjudicated cases; corrects §6.5.4's search cross-reference without altering closed Version/Generation semantics. That paragraph now points to the already established Task model rather than retaining its obsolete blanket absence statement; no Task decision changes. |
| [assemblies-views/assemblies-and-views.md](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md) | Replaces the undecided resource/set/neither/both alternatives with current semantics; distinguishes ordinary search from self-contained snapshot/export packaging without changing Assembly/View definitions or candidates. |
| [Domain04 README](../../docs/markdown/04-information-architecture/README.md) | Removes obsolete unresolved search status and adds the two-case highlight and navigation. No domain completion status is introduced. |

The Architecture Completion Plan remains unchanged. Its §4 semantic-sufficiency
NO statement records the earlier Step 5 programme position, as recognised by
the preceding assessment; this report supplies the subsequent bounded gate
reassessment without formally maintaining programme/domain completion status.

## 3. Final Information Meaning

### Harmonia-managed search

The canonical concept is **Search Result Set**, a managed metadata collection
specialising existing Collection semantics. “Search Result Metadata-Set” is a
description of that concept, not a second concept or implementation type.
Its own Information Unit boundary governs search/result metadata and membership.
Referenced Units retain their existing boundaries, authority and history.

```text
Managed Information Search
        |
        v
Search Result Set (managed metadata collection)
        |
        +-- Search / Result Metadata
        |
        +-- Membership [0..*]
                |
                +-- Information Unit Version Reference
```

Membership identifies the applicable **Information Unit Version** comprising
the result. Unit U/V7 membership still identifies V7 after U progresses to V8
or changes active manifestation. It does not resolve to whichever current
Version or Active Generation exists when examined later. These are conceptual
references, with no identifier/encoding or retrieval mechanism prescribed.
Zero membership represents an empty result; no universal atomic multi-member
observation is inferred.

**Search alone does not create copies of returned Units, new Versions of
those Units, a BLOB of duplicated member content or a second authority.** The
new result artefact represents search/result information. It does not contain
the members' content. Export, snapshot, document generation, archival or
transmission packaging remains a separate semantic act/capability.

Search context, criteria, execution/result time, result identity and membership
are possible semantic metadata categories. **No complete schema, mandatory
field bundle, serialization, database, cache structure or search API was
invented.** Retention, manifestation, reference realization and search mechanics
remain downstream; no release structure catalogue was supplied.

### External search

External search is intentionally invoked within a governed **Ergo activity**.
Returned information is acquired and persisted as activity information within
that governed context and applicable Information Unit boundary. External origin
does not require another generic `Observed → Accepted Into Management`
transition. This is explicitly governed intentional acquisition, not arbitrary
observation or storage implicitly creating management responsibility. No
universal Unit per returned item, result or activity is imposed.

Returned domain content may remain opaque. Explicit Ergo behaviour may inspect,
apply, associate, transform, route, persist or otherwise act upon it without
intrinsic Platform inference of domain meaning. Source/domain authority is
preserved; no execution engine, AI responsibility or application allocation is
introduced. Persisted activity information is the governed semantic outcome,
not an assumption that any individual invocation/commit succeeded; AX-15 applies.

### Audit / Assurance overlap

The Search Result Set answers which managed Versions comprised the result.
Audit / Assurance may establish requester/actor, authority, access/control,
disclosure, evidence or assurance conclusions according to policy. Correlated
references or overlapping metadata are legitimate and are not eliminated.
The result set is not collapsed into evidence, and all search activity is not
made audit evidence. Detailed Audit / Assurance remains outside this task.
External response projection retains standards-facing AX-02/13 semantics;
internally managed result metadata does not prescribe a public envelope.

## 4. Previous Finding and Retained Deferrals

**R01: A → resolved / sufficiently established.** Its generic question mixed
searches over already-managed information with intentional external acquisition.
The metamodel and Assembly/View guidance no longer retain the obsolete generic
resource/set/neither/both alternatives. The current decision is recorded in
RADS; the old assessment is preserved as decision-time evidence.

**No narrower Category A gap was identified as a direct consequence.** Absence
of a complete result metadata schema, reference encoding, cache/persistence
layout or universal external-result Unit cardinality does not supply a new
generic semantic blocker. Domain04 now establishes the information meaning
needed for their later derivation.

The preceding assessment's remaining B/C/D standings are preserved:

- **ReportedTask (R25): D, deliberately deferred/non-blocking.** Necessity and
  Assurance/Accountability relationships await separate adjudication.
- **TaskOutcome detail (R16–18, R23 and R43): D**, with assignment, output,
  completion metadata and enforcement mechanics retaining their C standing.
  No identity/lifecycle/cardinality, terminal qualification or universal
  authoring/domain-outcome mandate is decided here.
- **Search mechanics (R03): C.** APIs, indexing, pagination, reference
  realization, cache/storage and component allocation remain Domain05 or
  later concerns; they must respect the now-established meaning.
- **Snapshot/export and historical realization (R10): C / separate capability
  concerns.** Search does not itself create the self-contained artefact.
- **Audit/Assurance detail (R27), candidate G2 composition/approval and
  contextual authority/cardinality questions:** retain explicit D standing.
  No closed Task, Version, Active Generation or historical-information decision
  is reopened. No additional family or candidate adoption is required merely
  because more modelling is possible.
- **Accepted replay contract (R34/L01): D**, downstream/corpus canonicalisation
  debt. ADR-015's replay/reprocessing/redelivery and new-attempt/original
  distinctions remain separately governed; generic Task/history semantics do
  not become an exhaustive recovery taxonomy in this task.

## 5. Canonical Documentation Assessment and Legacy Treatment

Relevant material was found outside `/docs/markdown`. Targeted repository
searches covered legacy Markdown/LaTeX search/result discussions, expanding
through the sources below. Legacy source location, implementation details and
historical discussion did not establish current information architecture.

| Legacy material inspected | Knowledge and canonical owning context | Treatment / future reconciliation |
|---|---|---|
| [docs/memory-recovery.md](../../docs/memory-recovery.md), rows 58–62, particularly 59 | Recollection/navigation to previous result/member/atomic-set exploration and search/cache questions. Domain04 search meaning versus later mechanisms. | Retained unchanged as historical navigation, not current unresolved architecture. The actual acceptance decision is now self-contained in RADS. A historical recollection of an open discussion is not a section of adopted architectural meaning to migrate or supersede. |
| [docs/architecture-decisions.md](../../docs/architecture-decisions.md), ADR-010; preceding residual §7/R34/L01 reviewed for remaining ADR debt | Search must remain correct from durable state after cache loss; later recovery contracts retain accepted replay distinctions. Governance and Application/Integration responsibilities, with generic Domain04 state/history boundaries. | ADR-010 is compatible with AX-05 and the new result semantics; it does not decide member/set management. No ADR was replaced or mechanically migrated. Existing downstream/corpus ADR reconciliation, particularly ADR-015, remains explicitly deferred and is not demonstrated necessary solely to understand Domain04's generic model. |
| [docs/provider-registry/search.md](../../docs/provider-registry/search.md), §§1–3 | Historical implementation search path/parameters and FHIR searchset response example. Application/Integration/solution support. | The external response example does not define a managed internal Search Result Set or imply internal content duplication. Retain as supporting/history; assess search path and API contracts during separately authorised downstream/provider-document reconciliation. Its direct-storage path cannot override AX-05 or repository access guardrails. No unrelated mechanism correction was made. |
| [Legacy Information Architecture LaTeX](../../docs/latex/chapters/03-information-architecture.tex), [Data Architecture LaTeX](../../docs/latex/chapters/04-data-architecture.tex), searched for search/result/representation claims | Resource, envelope, persistence/index and checkpoint representation material. Primarily Application/Integration/Technology and implementation guidance. | No competing result-management meaning was identified. Existing downstream representation/checkpoint reconciliation remains outside this bounded decision; no implementation detail was imported into Domain04. |
| [Execution model](../../docs/architecture/execution-model.md), [Pragma](../../docs/concepts/pragma.md), [Praxis](../../docs/concepts/praxis.md), inspected for search/result claims | Execution/envelope/blueprint concerns; general Ergo opacity/runtime-AI boundary is already canonical. Later Application/Execution allocation. | No additional search acceptance meaning was found. Retain existing supporting/history and supersession treatment; no new mapping or section migration. |
| [Runtime integration plan](../../docs/implementation/harmonia-convergence-runtime-integration-plan.md), bounded-search occurrence | Future authoritative search implementation sequencing/verification. Execution sequencing and downstream mechanics. | Compatible supporting execution information; does not govern this architecture-completion documentation task or create result semantics. No runtime milestone commenced. |
| Prior Domain04 reconciliation reports and [residual assessment](2026-10-10-domain04-residual-semantic-gap-assessment.md) | Dated open-question and gate evidence, navigation to authoritative architecture. | Retained unchanged historical execution artefacts. Current semantic replacement is in RADS; report history is not rewritten to make earlier assessments appear current. |

**Legacy sections newly superseded: none.** No inspected legacy section's
adopted architectural meaning is replaced by this adjudication. Compatible
supporting/response detail and historical navigation are deliberately retained;
wrapping them would incorrectly suggest they had supplied the architecture
being replaced. If a future authorised reconciliation replaces specific legacy
architectural meaning, the standing section-level wrapper and unchanged-content
preservation rule remain mandatory. No entire legacy document was superseded.

**No architectural knowledge required to understand Domain04 within the agreed
scope was identified solely outside RADS.** The two search cases, membership,
Unit boundary and opacity now have canonical statements independent of old
discussions or reports. Explicit later-domain ADR/execution/representation debt
is not certification of complete programme-wide corpus consolidation; required
downstream contracts retain their defined authority until reconciled. Recording
a deferral would not waive gate criterion 5 for genuinely required Domain04
knowledge.

## 6. Domain Completion Gate Reassessment

Assessment against [Completion Plan §9](../../docs/markdown/architecture-completion-plan.md#domain-completion-gate):

| Criterion | Result | Evidence and scope limit |
|---|---|---|
| 1. Architecture required for the agreed R1.x/R2.x Domain04 scope sufficiently established | **PASS** | The sole A question R01 is resolved canonically. Managed search governs metadata and Version-reference membership; external acquisition is governed by the intentionally invoking Ergo activity. Existing Unit, Task, Version/Generation, history, source authority and opacity meanings are preserved. No additional generic information meaning was demonstrated necessary. |
| 2. Known contradictions reconciled or explicitly retained unresolved | **PASS WITH EXPLICIT DEFERRAL** | Obsolete generic search alternatives/status are replaced. Version membership does not imply snapshots or current-Generation resolution; external governed acquisition does not imply originating/domain authority. Existing G1/G2, Task and Assurance reservations remain distinguishable from closed decisions and candidate approval. The stale Task absence cross-reference was corrected to the established model without reopening it. |
| 3. Relevant traceability to authoritative upstream architecture truthful | **PASS** | EC-05 and FEAT-ISE-05/08/09 establish search/retrieval/filtering purpose; Business Exchange/Access and its ownership matrix establish responsibility without ownership of source records. The new document explicitly attributes the result-management decision to this adjudication, rather than falsely claiming that upstream search text specified Version membership. No new owner, Feature edge, hierarchy or application mapping was invented. |
| 4. Known relevant knowledge outside RADS incorporated, reconciled/supporting, or explicitly unresolved/deferred | **PASS WITH EXPLICIT DEFERRAL** | §5 records deliberate supporting/historical dispositions for inspected search material and preserves the preceding assessment's explicit later-domain ADR/replay, execution and representation reconciliation debt. Required search meaning is canonical; compatible external response/implementation guidance is not mechanically migrated. No additional legacy-only Domain04 meaning was identified. |
| 5. No knowledge required to understand Domain04 depends solely on non-canonical documentation | **PASS** | Current RADS supplies the agreed-scope information meanings, including this previously absent boundary. Neither historical discussions nor execution reports are required to understand it. Replay and remaining execution/API/technology contracts are later-domain/programme debt, not missing generic Domain04 meaning. This is a bounded assessment, not a final whole-programme corpus audit. |

**Domain04 appears semantically sufficient for the agreed R1.x/R2.x scope.**
Legitimate deferrals do not block this semantic assessment where sufficient
information meaning exists for downstream derivation. Formal Domain04 closure,
programme status maintenance, Domain05 authorisation and the final R1.x/R2.x
baseline remain separate actions; none was performed.

## 7. Validation and Preservation

**PASS — mandatory architecture suite:** 90 tests across 11 classes, 0
failures, 0 errors, 0 skipped; Maven BUILD SUCCESS, exit 0, 26.261 seconds.
Surefire XML was inspected. All eight AGENTS.md suites ran, plus
GovernedWriteComposition, GovernedWriteContract and
MnemosyneAuthoritativePersistence architecture tests. Cached dependencies
allowed offline execution, avoiding the preceding assessment's known
read-only Maven-cache dependency-resolution problem.

```bash
timeout --signal=TERM --kill-after=10s 300s mvn -o test \
  -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false -B -ntp
```

ArchUnit emitted unsupported Java 25 class-file major version 69 import
warnings; Maven/Guava emitted deprecated `sun.misc.Unsafe` warnings. Reported
assertions completed successfully with that environmental import limitation.
The suite verifies executable architectural guardrails, not approval of the
new information semantics or semantic completeness of documentation. No
timeout/stall or forced termination occurred. No broader runtime test was
warranted by documentation-only changes.

**PASS — documentation structure and links:** CommonMark/table parsing,
one H1 per file, closed/nonempty fences, local file/heading/HTML-anchor targets
across all 28 current Domain04 documents, the Completion Plan and this report
(30 Markdown files); 718 local link occurrences, 55 incoming links from other
tracked Markdown to changed RADS targets, 106 fenced blocks, zero structural
or missing-target/anchor errors. Conceptual text diagrams were reviewed;
unchanged Mermaid fences were parsed as fenced content, not renderer-tested.
No packaged repository Markdown/link validator was located; the check follows
the preceding reconciliation's Markdown-it-py and link/preservation method.
The checker uses a 60-second timeout with a five-second termination grace.

**Initial broad whitespace scan — FAIL for pre-existing lines, not these
changes:** three trailing-whitespace lines were found in untouched
`information-families/README.md` (67, 78) and `healthcare-service.md` (111).
Their files match the pre-task SHA-256 baseline. Whitespace checking was scoped
to changed/new documentation; **PASS** for that scope and `git diff --check`.
No unrelated cleanup was performed, and the broad diagnostic is not presented
as a clean whole-corpus whitespace result.

**PASS — scope/preservation:** 2,299 pre-existing files were snapshotted,
including the already-untracked residual assessment. Only the three intended
existing RADS files changed; the other 2,296 files match their hashes. The
Search Result Set document and this report are the only task additions.
Domain01–03, axioms, implementation, tests, legacy documents and earlier reports
remain byte-identical; closed-decision semantics are preserved. Final search confirms no obsolete
generic unresolved search-management statement remains in current Domain04
RADS; the metamodel's historical-question reference explicitly records resolution.

Temporary execution evidence: `/tmp/harmonia-domain04-search-baseline.json`,
`/tmp/harmonia-domain04-search-validate.py`,
`/tmp/harmonia-domain04-search-validation.json` and
`/tmp/harmonia-domain04-search-architecture-tests.log`. These are supporting
execution artefacts, not architectural authority, and may not survive cleanup.
