------- Legacy Content - Superseded ------- Start ------
Superseded by:
[architecture-authoring-praxis.md](../../../enterprise-architecture/governance/architecture-authoring-praxis.md)
Current EA governance is maintained at the successor; original content below is unchanged historical material.
---------------------------------------------------------

> **RAD-1.1.5 representation:** Use the [element incorporation Praxis](../../../enterprise-architecture/governance/continuum-incorporation-praxis.md) for current canonical Markdown placement and resource allocation. The semantic authoring and review rules below continue to govern; historical document carriers do not receive new architecture after successful ingestion.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture Authoring and Round-Trip Praxis — Increment 2C

## Purpose and authority

This Praxis establishes how Solution Architects and authorised architecture
contributors work repeatedly with Harmonia architecture. The practical entry
point is [Working on the Harmonia architecture](../../archimate/README.md).
This RADS contract is the normative lifecycle authority behind that entry point.
It establishes no ingestion or reconciliation engine.

[Architecture Change Provenance](../../../enterprise-architecture/governance/architecture-change-provenance.md) is the single
contract for durable transition evidence across all representations and agents.
Its 2026-10-20 adoption decision supersedes this Praxis's former unqualified
submission/run preservation wording. Input pins, working snapshots and detailed
processing records support active recovery, validation and unresolved matters;
they are not permanent architectural evidence by participating in this lifecycle.
Retain them while needed, including for migration dependencies. Final closure
and any separately authorised disposal follow that contract.

[Architecture Governance](../../../enterprise-architecture/governance/architecture-governance.md)
now defines common Architecture Review and HCRL as the single final semantic
Decision Review Log for the release transition. This Praxis and the specialised
Change Review retain their specialised authoring/submission/reconciliation and
validation purposes, feeding relevant material into HCRL. Governance knowledge
requires no ArchiMate element or continuum counterpart merely through durability.
The target structure does not authorise moving this Praxis or its machinery;
existing paths and specialised semantics remain pending bounded migration.

The Solution Architect's Increment 2C task authorises this lifecycle. Existing
[repository authority](../../../AGENTS.md#1-architectural-authority), accepted
decisions and the [Architectural Axioms](architectural-axioms.md) continue to govern:

| Axiom | Application to this Praxis |
| --- | --- |
| [AX-04](architectural-axioms.md#ax-04) | The governed architecture determines meaning; Archi and processing tools provide machinery. |
| [AX-14](architectural-axioms.md#ax-14) | Working state, submission, architectural acceptance, execution and publication remain distinct, as do semantics and presentation. |
| [AX-15](architectural-axioms.md#ax-15) | Interrupted processing preserves evidence and uncertain effects; a failure does not prove that no mutation occurred. |
| [AX-17](architectural-axioms.md#ax-17) | Proposed or unknown architecture stays distinguishable from accepted architecture; missing meaning returns for adjudication. |

This applies those axioms without changing subsystem responsibilities or runtime
boundaries. Detailed governance remains in its existing contracts:

| Increment | Question answered | Authority |
| --- | --- | --- |
| 2C | How do I work with Harmonia architecture? | This lifecycle and its practical entry point. |
| 2A | How are submitted architectural changes reviewed and authorised? | [Executable Ingestion Change Review](archimate-ingestion-change-review.md), including dispositions, instructions, dependencies and processing evidence. |
| 2B | How are authorised architectural changes interpreted, represented, organised and published? | [Ingestion and Publication Semantics](archimate-ingestion-publication-semantics.md). |

## Reusable engineering mechanism — 2026-10-20

The [ArchiMate Round-Trip Engineering Praxis](archimate-round-trip-engineering-praxis.md)
now establishes the repository-discoverable engineering stages, two-trigger
vocabulary, review/application checkpoints and framework responsibilities for
this lifecycle. Its current containment and submitted-View preservation rules
qualify older general wording below. This is governed agent execution through
shared tools; historical bounded reconcilers are not a universal automatic engine.

## Working model and representations

**The Harmonia repository is the governed architecture system of record.**
Markdown RADS are its governed textual architecture source. Archi is the
interactive architecture workbench. ArchiMate Open Exchange XML is the interchange
mechanism between the repository and that workbench. LaTeX RAD is a publication
representation, with the normal direction `Markdown RAD -> LaTeX RAD -> publication
output`; it supplies no alternative architecture authority or reverse-authoring
path. Governed projection metadata retains its existing subordinate role.

Architects may consume and review architecture through these representations
and other governed projections. None becomes a competing architecture repository.
An incoming Archi model proposes changes to repository-held architecture; merely
existing in Archi confers no governed standing.

## Terms that must remain distinct

| Term | Meaning |
| --- | --- |
| **Architecture Release** | The governed repository release identifier controlled by the [release policy](architecture-release-policy.md) and root [release authority](../../../architecture-release.json). This Praxis changes no increment rule. |
| **Publication** | Production of coherent governed architecture representations after the applicable reconciliation, validation and publication gates. |
| **Published Baseline** | A specific governed publication from which architectural work may safely proceed, identified by its publication/release and exact artefact provenance. |
| **Authoring Iteration** | Mutable architect working state derived from exactly one identifiable Published Baseline, ending at deliberate Submission. It may contain progressive edits and multiple Archi saves; it is not an Architecture Release. |
| **Submission** | The explicit handover of one completed Authoring Iteration to governed ingestion. Submission closes that Authoring Iteration. |
| **Change Review** | The Increment 2A governance transaction in which detected architectural proposals are decomposed into independently adjudicable Change Requests and adjudicated. |

```text
save != submission
submission != acceptance
acceptance != reconciliation
reconciliation != publication
authoring iteration != architecture release
```

## Begin from a published baseline

For editing in Archi, the architect SHALL obtain and import the current applicable
published Harmonia Open Exchange model and confirm the specific baseline/release
before authoring. Use the [published package index](../../archimate/exchange/publish/README.md)
and its publication/inspection evidence. A root release increment alone is not
proof that a corresponding exchange package or new Published Baseline exists.
Do not choose an arbitrary old Archi working file when a newer applicable governed
publication is available. If the applicable publication cannot be established,
raise that uncertainty before starting governed work.

The originating baseline/release and exact published artefact SHALL remain
identifiable throughout the iteration and submission. Retain its source locator
and provenance so the prescribed review can pin the exact baseline hash. Keep
ordinary working copies/backups as needed; they are working state and carry no
independent authority. Exactly one baseline underlies each iteration.

## Author architecture progressively

> Architect authors architecture; Harmonia machinery authors governance machinery.

During an Authoring Iteration, the architect MAY experiment, edit progressively
and save repeatedly. Intermediate saves and autosaves are working state. They
neither submit the iteration nor create a release or governed architecture.

The architect normally authors appropriate native ArchiMate element types,
meaningful native Names, applicable authorised Harmonia `[xxx]` qualifiers,
intended semantic relationships, useful architectural Views and review layouts,
and other architect-owned content already permitted by governance. Use
[2B recognition and naming rules](archimate-ingestion-publication-semantics.md#recognition-classification-and-published-names)
for qualifier/type compatibility; brackets alone grant no classification.

Routine authoring SHOULD NOT require manual manufacture or maintenance of
Canonical IDs, derived `displayName`, `friendlyName` or `canonicalName`, derivable
machine-governed classification metadata, generated relationship navigation homes,
deterministic publication Views or repository processing artefacts. Authorised
machinery supplies these under [2B metadata rules](archimate-ingestion-publication-semantics.md#author-supplied-information-and-machine-governed-metadata)
and existing [identity/naming governance](canonical-identity-and-naming.md), after
the necessary architectural acceptance and resolution of semantic inputs.
Architects MAY inspect machine-governed metadata. This division of labour does
not authorise deletion or alteration of established governed identities.

### Express semantic intent explicitly

Ease of authoring does not remove responsibility for architectural meaning.
Where intended architecture depends on a semantic relationship, the architect
SHOULD explicitly author that relationship as appropriate: Composition,
Realization, Serving, Flow or another valid ArchiMate relationship, with intended
endpoints and direction. Native validity and Harmonia meaning remain subject to
the existing [relationship profile](rad-archimate-model-mapping.md#5-relationship-profile-and-direction)
and adjudication. This Praxis creates no new interpretation of those types.

Folder placement, View nesting and diagram geometry do not substitute for
semantic relationships. Ingestion SHALL NOT be relied upon to infer architecture
never expressed. Where the appropriate meaning is genuinely unknown, preserve
and explain the uncertainty for review rather than invent an edge to make
processing succeed.

### Use Views for reasoning and review

Archi Views are legitimate authoring artefacts. The architect MAY create Views,
add or remove element appearances, expose existing semantic relationships,
arrange presentation and adjust geometry for reasoning, review and readability.

A visual connection referencing an existing semantic relationship is another
appearance of that relationship; it creates no new semantic edge. An intended
new relationship must exist in the semantic model. Its drawn appearance alone
supplies no semantics. Accepted authored Views and generated deterministic Views
retain their distinct [2B preservation/publication obligations](archimate-ingestion-publication-semantics.md#views-as-projections-and-preservation-of-authored-views).
Architects need not manually reproduce deterministic publication Views.

## Submit once and close the iteration

Submission occurs when the architect deliberately exports the completed iteration
and places or provides that artefact to the governed ingestion process according
to the [incoming-exchange procedure](../../archimate/exchange/ingest/README.md)
and [2A run/evidence convention](../../archimate/processing/ingestion/README.md#run-layout).
The deliberate handover, rather than an incidental file operation, is the
lifecycle event. An Archi save, autosave, local backup export, intermediate
working copy or Git commit unrelated to that mechanism is not Submission.

Retain sufficient provenance to establish the originating governed baseline and
release, exact submitted artefact and submission/run identity as applicable.
Pin incoming and baseline hashes under 2A for active processing and recovery.
Use its existing working run layout, numbering and template, subject to
Architecture Change Provenance for durable evidence; this Praxis defines no new
allocation or storage mechanism. The engine remains future work; an explicitly
authorised human/agent procedure must still honour those governance boundaries.

**Submission closes that Authoring Iteration.** Once submitted, it SHALL NOT
remain an open source for another Harmonia submission. Additional changes SHALL
NOT be submitted as another descendant of that closed iteration. Preserve the
submitted model while required for recovery, validation, unresolved decisions or
migration dependencies, and retain local copies for inspection as needed under
Architecture Change Provenance. These copies are no longer starting points for
new governed submissions; Submission does not impose permanent export retention.

The 2A provision for a distinct incoming revision and a predecessor-linked new
run preserves evidence and processing identity; it does not authorise reopening
an iteration or making another descendant submission. Review/instruction revisions
for the same immutable input, or governed processing recovery, are not fresh
Authoring Iterations. A new architecture proposal must follow the next-baseline
rule below.

## Review, reconcile, validate and publish

**Submission means “this is the architecture change proposed for governance.”**
It does not mean that the proposal is now governed Harmonia architecture.

After submission, 2A governs detection, decomposition into independently
adjudicable CRs, Change Review generation, Solution Architect adjudication and
decision-linked authorised reconciliation instructions. Only the Solution
Architect issues Dispositions: Accept, Reject, On-Hold and Manual Intervention
are [calls to action to the processing agent](archimate-ingestion-change-review.md#solution-architect-dispositions).
[Outcome and Outcome Reason](archimate-ingestion-change-review.md#outcome-and-outcome-reason)
independently record the CR's resulting state and why. Issued Dispositions remain
auditable after later resolution. A Resolved Outcome does not accept a proposal
or supply reconciliation authority.

Only authorised architectural changes may enter governed architecture.
Reconciliation applies accepted CRs under their 2A executable instructions,
2B semantics, existing identity/naming/mapping/navigation governance and higher
architectural authority. The executing agent SHALL NOT re-architect the proposal.
Unresolved meaning, missing authority or instruction ambiguity returns for
adjudication; it is not filled in to complete execution.

After authorised reconciliation, validate the resulting governed architecture,
retain working recovery/validation records and record material conclusions in
the applicable HCRL DR under Architecture Change Provenance, satisfy the existing
[release/publication gates](architecture-release-policy.md#review-and-publication-gate)
and [mapping verification gates](rad-archimate-model-mapping.md#8-deterministic-publication-and-verification),
then produce the next coherent publication through its applicable representations.
Only completion of that lifecycle establishes the next Published Baseline.
Ingestion success, Change Review acceptance, a release increment or reconciliation
alone does not establish it. Validation limitations and unresolved effects must
remain explicit.

## Continue from the next publication

```text
Governed Architecture -> Published Baseline N
    -> Authoring Iteration N (multiple edits, saves and Views)
    -> Submission (Iteration N closes)
    -> Detection / Change Review
    -> Solution Architect Adjudication
    -> Authorised Reconciliation
    -> Validation and applicable publication gates
    -> Published Baseline N+1
    -> Authoring Iteration N+1
```

After a submitted iteration closes, further governed architectural authoring
SHALL begin from the next applicable Published Baseline, incorporating the actual
adjudicated and reconciled outcome. The submitted model SHALL NOT be continued
and submitted again. Baseline N/N+1 denotes successive publications in this
example, not a change to release numbering rules.

During the interval before the next publication, the safe default is to refrain
from commencing another governed iteration from the submitted/closed model.
Architects MAY inspect the submission and evidence, participate in Change Review,
answer adjudication questions and discuss or design prospective changes outside
the governed authoring stream. Work intended for subsequent governed submission
begins from the next publication; prospective sketches do not create an exception.

The current Praxis uses a **serial governed publication lineage**. It deliberately
avoids multiple simultaneously submitted descendants of the same baseline or
iteration. It establishes no architectural branches, competing submissions,
parallel reconciliation streams, merge semantics, automatic three-way merge or
conflict-resolution machinery. A future explicit governance mechanism would be
needed to introduce those capabilities. This is a governance simplification,
not a claim that branching and merging are technically impossible.

## Human and processing responsibilities

| Participant | Responsibility |
| --- | --- |
| Architect / authorised contributor | Architectural intent, appropriate elements and explicit relationships, useful authored Views, identification of uncertainty, baseline provenance and deliberate one-time submission. Contributes to review within their authority. |
| Solution Architect | Architectural adjudication, CR dispositions and decisions requiring human architectural authority, including unresolved meaning. |
| Harmonia processing / authorised agents | Detection, evidence capture, deterministic rule application, authorised identity/naming generation, navigation projection, authorised reconciliation, validation, deterministic publication and audit evidence. |

Agents SHALL NOT fill architectural gaps merely to complete processing. Their
authority is bounded by existing governance and the recorded decision-linked
instructions. Contributing or operating a tool does not confer adjudication
authority.

## Recovery and interrupted work

| Situation | Required handling |
| --- | --- |
| Working iteration lost before Submission | Recover from ordinary working copies/backups where available, retaining the original published-baseline provenance. No Submission occurred; the governed baseline remains unchanged. |
| Processing fails after Submission | The iteration remains closed. Preserve the submitted artefact and evidence; follow [2A processing/recovery rules](archimate-ingestion-change-review.md#processing-record-and-audit-evidence), including instruction history and before/after evidence. Do not reopen the model or create an untracked replacement submission. |
| Reconciliation cannot complete or its effects are uncertain | Preserve the actual state and uncertainty under AX-15. Return necessary matters through Change Review for adjudication or Manual Intervention as governed by 2A; an agent cannot assign a disposition. Do not presume rollback, non-occurrence, success or a new baseline. |

The recovery records above remain available while needed. After an authorised
transition, its durable evidence and closure conditions follow Architecture
Change Provenance; permanent recovery histories are not an additional evidence
requirement. Existing dependencies must be resolved through separately authorised
work before any affected workspace is eligible for disposal.

This is practical lifecycle guidance, not a new retry, recovery or reconciliation
system.

## Worked example — illustrative, not architecture authority

This fictional example concerns no current blind-test proposal and approves no
element, relationship, name, identity or classification instance.

1. A Solution Architect imports **Published Baseline N**, records its release and
   exact package provenance, and starts one Authoring Iteration.
2. While exploring a fictional “Workshop Booking” change, they author appropriate
   Business Process and Business Service proposals, explicitly express an intended
   valid Realization between them, and build a useful review View. They save
   several times. These are working proposals with no accepted architectural standing.
3. Once ready, they export and deliberately submit **once**. The iteration closes;
   its export and provenance remain evidence.
4. Harmonia detection produces a Change Review. The Solution Architect adjudicates
   its independent CRs under 2A, using Accept, Reject, On-Hold or Manual Intervention
   as appropriate. This example selects no disposition or architectural outcome.
5. Authorised changes are reconciled under 2A/2B, the resulting architecture is
   validated, and applicable gates complete before **Published Baseline N+1** is
   produced. Rejected and unresolved material remains auditable and does not enter
   the publication as accepted architecture.
6. The architect imports that new publication to start the next iteration, including
   the actual governed outcome. They do not resume authoring from the old submission.

## Increment boundary

This increment formalises Praxis only. It processes or adjudicates no blind-test
proposal, allocates no incoming identities and changes no semantic model. It
implements no ingestion/reconciliation pipeline, Containment View layout engine,
branching or merge system. The existing 2B Name/displayName findings and deferred
metamodel decisions remain unresolved and unmigrated. Stop after documentation,
required patch handling, bounded validation and the implementation report.

------- Legacy Content - Superseded ------- Finish ----
