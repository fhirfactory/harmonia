<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Common Topic definition and runtime identity (R1) | ACCEPTED | The ordered qualified non-versioned path establishes runtime identity. Preserve independent identities and governed reference obligations. |
| [DR-2](#dr-2) | Common TopicMask definition and matching (R2) | ACCEPTED | Accept structural equality, positional and version-aware matching, exact and inclusive containment, and explicit universality. |
| [DR-3](#dr-3) | TopicIdentityStable Feature (E04.01.04.F01) | ACCEPTED | Preserve architectural element identity continuity independently of runtime path identity and retain governed reference-continuity requirements. |
| [DR-4](#dr-4) | IU and Store adapter correspondence and retained qualifications | ACCEPTED WITH QUALIFICATION | Accept the bounded correspondence. Defer IU qualifier vocabulary, specialised mappings and downstream integration to their respective backlog items. |
| [DR-5](#dr-5) | Retained Topic/TopicMask input locator | ACCEPTED | Authorise the exact two-path correction and record it in the Architecture transition that incorporates it. |
| [DR-6](#dr-6) | Architecture transition and standalone Naming prerequisites | ACCEPTED WITH QUALIFICATION | Accept the reconciliation as supporting work for Software 0.1.0. Standalone Naming may start before the Architecture releases when the recorded conditions are met. |
| [DR-7](#dr-7) | Bounded Minutiae | ACCEPTED WITH QUALIFICATION | `Minutiae-Accepted: Yes`. Preserve the unresolved original Task issuance timestamp and filename prefix. |
| [DR-8](#dr-8) | Final.003 replacement ADD and retirement of superseded text | ACCEPTED | Accept the exact seven-element ADD, two association descriptions and eight scoped retirements. Final.003 governs where earlier implementation implications conflict. |
| [DR-9](#dr-9) | Pre-1.0.0 Software Development Praxis amendment | ACCEPTED | Incorporate the exact amendment under its own decision in the same Architecture 1.1.28 change set as DR-8. |
| [DR-10](#dr-10) | Final.003 transition sequence and verification | ACCEPTED WITH QUALIFICATION | Sequence 1.1.26 → 1.1.27 → 1.1.28. Apply DR-5 once at the first required gate. Application and closure depend on the actual accepted predecessor and release verification. |

# HCRL 1.1.28 / 001 — Topic and TopicMask Continuum reconciliation

**Status** · DR-8 and DR-9 accepted. DR-10 accepted with qualification.
Final.003 incorporation and Architecture 1.1.28 closure remain pending the
accepted 1.1.27 predecessor and applicable release verification.
Architecture 1.1.26 remains the current incorporated release.

**Disposition correspondence** · The [Solution Architect’s supplied dispositions][dispositions]
govern DR-1–DR-5 and DR-7. The [revised instruction][dr6-task] governs DR-6.
The [disposition completion report][disposition-report] verifies the recorded result.
[Final.003][final-003] was accepted under the [design disposition][final-003-acceptance].
The [incorporation preparation Task][final-003-task] records approval of the
direction and pre-1.0.0 principle. The [operative Solution Architect
dispositions][final-003-dispositions] govern DR-8–DR-10.

**Origin** · Accepted Architecture 1.1.26, Git `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`,
tree `1846199b36bbfb07c87746c1367c059698b7120d`, branch
`feature/BL-0003-Calliope-Topic-and-TopicMask`. Software development target
`0.1.0-SNAPSHOT`. The [foundation reset][reset] superseded exploratory software
candidates and preserved the qualified accepted design.

**Proposed target** · 1.1.28, conditional on [1.1.27/002][archunit] closing first
as a separate Architecture transition. DR-6 sets the conditions under which
standalone Naming can begin ahead of those releases. The current accepted
Architecture release remains 1.1.26.

**Original review basis · DR-1–DR-7** · The [issued Task][task], qualified accepted
[Final.001.001][design], [Software HCRL 004 DR-10][design-acceptance],
[007 finalisation provenance][finalisation] and [1.1.25 DR-3][predecessor-topic].
The [locator patch][locator] and [original Naming ADD][candidate] retain their
reviewed correspondence in that order against the origin. The original
DR-1–DR-7 wording and original verification describe that review.

**Accepted review basis · DR-8–DR-10** · The [replacement ADD][final-003-add]
follows the [locator repair][locator] in the reviewed isolation sequence. The
[Praxis amendment][coherence-praxis] has its own authority within the same
Architecture 1.1.28 change set. The [candidate review Report][final-003-report]
and [disposition Report][final-003-disposition-report] record the reviewed
correspondence and current replay. The [pre-disposition HCRL][disposition-preimage]
preserves the exact reviewed pending record. The [earlier HCRL preimage][hcrl-preimage]
and prior proposals retain their respective historical scope.

<a id="dr-1"></a>
## DR-1 — Common Topic definition and runtime identity (R1)

The 1.1.25 incorporated runtime UUID and mandatory sequence-number representation
conflict with the qualified accepted ordered path. Adopt the non-versioned ordered
qualifier/value path for identity, equality, hashing and ordering. Optional entry
versions carry independent meaning. The amendments cover immutable entries and
construction, literal ancestry, derived naming forms, strict escaping and parsing,
and ordered logical reconstruction. Explicit positions support construction and
carry no independent identity or required field in interchange. The runtime model
permits distinct qualified paths with the same unqualified name. Existing governed
registry-scope uniqueness rules, aliases and reference obligations remain in force
at their respective boundaries. The Topic DataObject and its representation retain
their existing identifiers. [Naming ADD patch][candidate] · [Final.001.001 §§1–3][design]

**Solution Architect disposition · ACCEPTED.** Accept the proposed common Topic
definition and its correspondence to the qualified Naming design. The ordered,
qualified, non-versioned path establishes runtime Topic identity. Preserve
the independent identities and obligations of architectural elements, InformationUnits
and governed references. Incorporation addresses R1's common-model contradiction.
Applicable reference or registry migration and changes to the uniqueness scope
require separate treatment. Registry service design and Topic instance allocation
remain outside this correspondence.

<a id="dr-2"></a>
## DR-2 — Common TopicMask definition and matching (R2)

Adopt the ordinary mask's structural equality over ordered qualifier/value and
optional version expressions and its containment flag. Retain exact-depth default,
trailing inclusive containment, full-field positional RE2/J evaluation and the
distinction between absent, present-empty and other version criteria. Explicit
universal masks have their own logical kind. The amendment covers builder
validation, escaped ordinary expressions, logical interchange, bounded evaluation
and fail-closed semantics. The TopicMask-to-Topic association describes the same
neutral selection responsibility. Update its description while retaining its native
type, identifier, direction, endpoints and original evidence. [Naming ADD patch][candidate] · [Final.001.001 §§4–7][design]

**Solution Architect disposition · ACCEPTED.** Accept the proposed TopicMask
definition and matching semantics. Preserve structural equality, positional matching,
optional version constraints, exact and inclusive containment, and explicit universal
matching. Exclude obsolete runtime UUID identity and historical exploratory
compatibility semantics. Incorporation addresses R2's common-model contradiction.
Existing governed selectors, where present, retain the requirement for exact or
inclusive criterion equivalence, established unrestricted intent, and evidence
against wider selection when migrated. Themis and the consuming Features retain
their decisions.

<a id="dr-3"></a>
## DR-3 — TopicIdentityStable Feature (E04.01.04.F01)

The accepted Feature says Topic identities survive rename and reclassification.
Its unqualified wording conflicts with Final.001.001 when applied to a changed
runtime Topic path. The [previous DR-3][prior-identity] records deferral of that
tension for a different assignment. The amendment preserves the Feature identity,
and scopes rename continuity to a governed architectural element that remains the
same. A changed qualified runtime path establishes a different runtime identity.
Existing governed historical references retain their explicit continuity treatment.
[TopicIdentityStable preimage][identity-feature] · [Naming ADD patch][candidate]

**Solution Architect disposition · ACCEPTED.** Accept the proposed clarification
of TopicIdentityStable. Architectural element identity continuity is independent
of runtime Topic identity. Changing a runtime Topic's qualified path establishes
a different runtime Topic identity. Preserve applicable governed reference-continuity
requirements.

<a id="dr-4"></a>
## DR-4 — IU and Store adapter correspondence and retained qualifications

The logical and conceptual IU definitions separately assert a Topic UUID. Update
both descriptions and the existing IU-to-Topic association to distinguish the
persistent IU UUID from its Topic's established unqualified designation and the
Topic's own qualified runtime identity. The exact qualified IU vocabulary and
boundary mappings remain deferred under R3. The Store adapter description refers
to dot-separated text evaluation. Point it to shared TopicMask evaluation over
structured Topics. Its capability registration, full-coverage and instance
selection obligations retain their existing scope. All other graph assertions and
ownership arrangements remain as recorded. [Naming ADD patch][candidate]

**Solution Architect disposition · ACCEPTED WITH QUALIFICATION.** Accept the
narrow IU and Store adapter correspondence changes. Preserve IU identity independence
and shared TopicMask selection semantics. Defer precise IU qualifier vocabularies,
specialised reference mappings and downstream integration decisions to their
appropriate backlog items. Functional service implementation and redesign remain
outside this reconciliation.

Preserve the other DR-10 qualifications. R4's identity-domain distinction is
represented here. The Foundation Reset removed the exploratory implementation's
compatibility obligations. Applicable governed persisted data, registry and alias
contracts remain subject to preservation or adjudicated migration. R5 keeps module
allocation, pinned RE2/J, release limits and the separate `themis-api` concern in
their governed scope. R6 keeps logging integration, bounded latency, reliability
and uncertain delivery open. Naming implementation, its behavioural evidence and
the other model increments require their own assignments. [Software DR-10 qualifications][design-acceptance] · [Software HCRL 009 DR-3][reset]

<a id="dr-5"></a>
## DR-5 — Retained Topic/TopicMask input locator

The live validator fails at the already missing `retainedAt` path ending in
`accepted-input/DRP-Topic-and-TopicMask.Final.001.000.md`. Both common elements
pin SHA-256 `1690ca87200db65d2254acfb5af68b7b960fb044934682b6ed89eb77ce9c5e40`.
The retained file with basename `DRP-Topic-and-TopicMask.Final.001.md` matches
that digest and the historical input inventory. The current source with suffix
`.001.000.md` is byte-identical and has its own separate source locator. The
correction changes the two `retainedAt` values only. It preserves the source
locators, selectors, digest, authority, retained bytes and historical records.
Canonical validation passes with the locator-only patch in isolation.
[Retained input][retained] · [Locator patch][locator] · [Prior reset qualification][reset-verification]

**Solution Architect disposition · ACCEPTED.** Authorise the precise two-path
locator correction. Preserve the verified digest, source evidence, historical
provenance and original architectural authority. Record the correction in the
Architecture transition that incorporates it. Preserve 1.1.27/002's existing
review and evidence.

**Final.003 candidate replay** · The accepted patch reproduces the locator-only
pass on the exact 1.1.26 source and supplies the replacement ADD's preimage.
The repair is still absent from the live Continuum. DR-10 retains the decision
on its application placement. [Current verification][final-003-report]

<a id="dr-6"></a>
## DR-6 — Architecture transition and standalone Naming prerequisites

**Solution Architect disposition · ACCEPTED WITH QUALIFICATION.** The Topic and
TopicMask Continuum reconciliation is accepted as supporting architectural work
for Software 0.1.0. Architecture 1.1.26 remains the accepted baseline. Preserve
the separately governed 1.1.27/002 amendment and the proposed 1.1.28/001
reconciliation. Incorporate architectural changes through the established
Architecture Change Review and release process. Architecture release identities
and Software 0.1.0 retain their independent standing. [Revised disposition][dr6-task]

**Standalone Naming implementation** may begin before completion and publication
of Architecture 1.1.27 and 1.1.28 when all five conditions are met.

1. The accepted Naming design establishes an unambiguous implementation contract.
2. The Solution Architect has accepted Backlog Item 0003.
3. Applicable implementation prerequisites and outstanding decisions have explicit dispositions.
4. Architectural discrepancies remain recorded and governed.
5. Software acceptance claims architectural conformance only to the extent the evidence establishes it.

The architectural reconciliation must be incorporated and verified before any
dependent acceptance requiring it. A bounded implementation assignment follows
the [Software Development Praxis][software-praxis].

The complete isolated candidate passes canonical validation for 6,563 elements
and 9,427 relationships. It preserves every canonical and Exchange identifier,
graph endpoint, navigation entry and all 1,304 authored Views. The candidate
Exchange passes offline XSD and reference checks and regenerates byte-identically.
Published model, reader and narrative material remains at 1.1.25 under the accepted
governance-only 1.1.26 transition. After authorised incorporation, regenerate the
root Manifest, Exchange, reader and narrative through the existing tools at the
authorised version. [Verification][verification] · [Architecture 1.1.26 publication scope][predecessor] · [Release policy][policy]

**Original candidate's remaining Architecture review and verification** · Before closing a dependent
transition, establish which authorised scope carries DR-5 or record the specific
verification treatment. Recheck the exact accepted predecessor and rerun applicable
checks before application. The current ArchUnit command executes zero tests on the
reset checkpoint. Its missing coverage remains open for explicit disposition or
treatment through the separately governed 1.1.27/002 amendment. The canonical
validation result establishes only its recorded scope. DR-10 records the corresponding
open decisions for the Final.003 candidate. [Disposition update report][dr6-report]

<a id="dr-7"></a>
## DR-7 — Bounded Minutiae

**Solution Architect disposition · ACCEPTED WITH QUALIFICATION.** The bounded
group comprises the [Task Detail][task], the [reconciliation Report][report],
and the supporting patch and verification evidence. The original Task issuance
timestamp and filename prefix remain **UNRESOLVED** under the recorded qualification.

```text
Minutiae-Accepted: Yes
Comment: The available Task, Report and verification provenance is accepted for this bounded reconciliation. The original Task issuance timestamp and filename prefix remain unresolved. Do not invent or reconstruct historical timestamps or identifiers.
```

<a id="dr-8"></a>
## DR-8 — Final.003 replacement ADD and retirement of superseded text

Use [Final.003][final-003] as the accepted R1/R2 design. Refine the existing Topic,
TopicMask, Calliope-Models, logical InformationUnit, conceptual Information Unit,
TopicIdentityStable Feature and Mnemosyne Adapter (Store) definitions in place.
Keep the common Topic's ordered qualified, version-independent runtime identity,
opaque optional versions, immutable construction and strict reconstruction. Adopt
explicit mask kinds, positional wildcard and version criteria, bounded numeric
comparisons and endpoint validation, strict interchange and safe failure outcomes.
Carry the supported Java surface, package containment and invocation-local
diagnostic boundary into the existing Calliope responsibility. The current design
requires pre-1.0.0 consumer coherence and supplies no superseded selector service.

The two association descriptions refine the existing TopicMask-to-Topic and
logical IU-to-Topic correspondence. The candidate preserves all element and
relationship identities, endpoints, directions, interfaces, dependency edges and
Views. The IU UUID remains an independent identity. The governed registry,
authority, IU vocabulary and specialised mappings retain their independently
established scope. Coherent refactoring applies to affected consumers at their
own interfaces. [Replacement ADD][final-003-add] · [Final.003 §7][final-003-boundaries]

The exact candidate replaces both earlier ADDs in the authorised application
sequence. It removes the temporary `namingReconciliation` marker and represents
design-source authority using existing scoped bindings and provenance. Retire the
eight identified passages from the affected current definitions. The live
committed preimages remain retrievable from Git. The current authoritative
replacement retains the governed InformationUnit's indivisibility and its
distinction from transactional atomicity, the active IU §§6.5.1–6.5.9
guardrails, and its metadata and representation responsibilities. Existing
registry and reference obligations and the Store adapter interfaces and
capability obligations retain their own authority.
[Retired-source correspondence][retirements] · [Earlier Final.003 proposal][earlier-final-003-add]

**Solution Architect disposition · ACCEPTED.** Accept the exact [replacement
ADD][final-003-add], SHA-256
`e92f13ff2a2945350f376a5854e9b225534ebe40b78e3ce21ccbd6d186034cc7`,
and its eight scoped retirements. The governing design source is
[Final.003][final-003], SHA-256
`3902908dbffec9b35c15f3d31bf05b9b3a3e885dabecc71780c51fe4d9886043`.
Final.003 supersedes conflicting implementation implications of DR-1–DR-4,
including RE2/J requirements and runtime Topic UUID assumptions. The original
dispositions remain as decision history. Preserve the established identities,
directions, endpoints, interfaces, dependencies, navigation, Views and
independently governed responsibilities. [Operative disposition][final-003-dispositions]

**Application and verification** · The exact isolated replay reproduced the
reviewed ADD and passed canonical and Exchange checks. Live application is
pending the accepted 1.1.27 predecessor. The actual incorporated result requires
the DR-10 release checks and confirmation. [Replay evidence][final-003-disposition-report]

<a id="dr-9"></a>
## DR-9 — Pre-1.0.0 Software Development Praxis amendment

The accepted pre-1.0.0 principle requires one coherent implementation of the
current accepted architecture. Dependency analysis establishes all affected
components and consumers. Update their contracts, interfaces, adapters,
representations, configuration, integrations, tests, fixtures, documentation and
build dependencies as applicable. Completion requires applicable regression and
integration verification of the reconciled system. Superseded pre-1.0.0 APIs and
implementations carry no backward-compatibility obligation.

The exact [Praxis amendment][coherence-praxis] adds this principle to §2,
requires scoped dependency analysis and verification in the Backlog Item under
§3, and records conformance and actual results with the existing Implementation
Report and software HCRL under §6. This is a normative governance amendment to
the current [Software Development Praxis][software-praxis]. The exact patch
applied unchanged in the isolated candidate. The live Praxis retains its present
authority and bytes. [Replay evidence][final-003-disposition-report]

**Solution Architect disposition · ACCEPTED.** Incorporate the exact [Praxis
amendment][coherence-praxis], SHA-256
`7909b8d2c294b1d8e50b82f31a67682d651cb3ccff373077228b3d7bf4b203ca`,
under DR-9 in the same Architecture 1.1.28 change set as DR-8. The amendment
uses the existing backlog, assignment, Implementation Report and software HCRL
mechanisms. Affected consumers must operate against the current accepted
contract, with behavioural regression and integration evidence. Compilation
alone does not establish coherence. DR-9 retains distinct authority and
verification within the shared transition. [Operative disposition][final-003-dispositions]

<a id="dr-10"></a>
## DR-10 — Final.003 transition sequence and verification

**Solution Architect disposition · ACCEPTED WITH QUALIFICATION.** Progress
Architecture 1.1.26 → 1.1.27 → 1.1.28. Complete the separate
[1.1.27/002 review][archunit] and its gates before using the actual accepted
1.1.27 result as the immediate predecessor. Incorporate DR-8 and DR-9 in
1.1.28 and carry DR-5 only if the predecessor lacks the correction. Reconcile
the exact patches against that predecessor with the minimum changes needed
to preserve the accepted scope. Record any differences and obtain renewed
disposition for substantive changes. Each accepted change set receives one
increment under the [release policy][policy]. Completion requires verification
and Solution Architect confirmation of the exact incorporated result.
[Operative disposition][final-003-dispositions] · [Review Praxis][review-praxis]

**Predecessor and DR-5 placement** · The current release authority and register
establish Architecture 1.1.26. The separate 1.1.27/002 review is pending
and supplies no accepted predecessor result. Its DR-2 identifies the
canonical locator dependency. The live validator reproduced the missing
retained-input **FAIL**, and the isolation change to the two `retainedAt`
values cleared it. The current verification path therefore places the
[exact DR-5 repair][locator] in 1.1.27, subject to recording it in that
transition's own review. Its SHA-256 is
`f29779bd8b8b0000442d5413293b157c84de4446239b5031980364f534a49418`.
If that review explicitly dispositions the earlier dependent verification
and leaves the repair to 1.1.28, apply it there before the ADD. Apply once.
Preserve the source identity, retained bytes, original digest and distinct
bindings. DR-5 has been exercised in isolation and remains absent from the
live Continuum. [Disposition Report][final-003-disposition-report]

**Preparation result** · A replay from the exact 1.1.26 Git tree applied
DR-5, then DR-8 and DR-9, with zero deviations from the reviewed patches.
The locator-only and combined canonical checks passed. The combined replay
reproduced the reviewed Markdown and deterministic Exchange bytes and
passed offline schema, reference and containment checks. The reviewed
result preserves 6,563 element identities, 9,427 relationship identities,
directions and endpoints, and 1,304 authored Views. The isolated replay
is the prepared candidate against 1.1.26. Live DR-8 and DR-9 application,
verification against accepted 1.1.27 and release completion remain pending.
[Replay evidence][final-003-disposition-report]

**Actual result checks** · Verify the exact source and accepted 1.1.27
predecessor, canonical Markdown and scoped change, element and relationship
identities, directions and endpoints, source bindings and retained inputs,
provenance, and correspondence to Final.003 and the DR-9 amendment.
Generate and verify Exchange XML, including schemas, references,
determinism, authored Views and navigation. Complete applicable architecture
tests, structural-conformance and actual transport checks. Present the
complete resulting change for human review and confirmation.
[Review Praxis][review-praxis]

| Outstanding gate | Owning review and treatment |
| --- | --- |
| Architecture 1.1.27 and DR-5 | 1.1.27/002 must establish its exact scope, verification and closure, including the DR-5 dependency. 1.1.28 application awaits an accepted predecessor. |
| Named ArchUnit source suite | The suite is absent and the earlier invocation ran zero tests. 1.1.27/002 owns the proposed retirement. Verify the accepted gate treatment and applicable conformance checks in 1.1.28 before closure. |
| Actual Archi transport and complete-result review | Import/export has not been performed. The 1.1.28 release review must establish the applicable transport and structural gates and obtain their evidence where required. The Solution Architect must confirm the exact incorporated result. |
| Four historical Task issuance times and prefixes | Keep the times unresolved. DR-7's qualification applies to its original reconciliation Task only. Retain the other three unresolved times for explicit treatment in this HCRL before closure. |

**Software follow-on** · Revise [Backlog Item 0003][backlog-0003] separately
against Final.003, including affected dependency refactoring and conformance
criteria. Preserve the exact [Software 010 candidate and review][software-010].
Carry strict XML reconstruction and encoding, early depth, accurate failure
metadata, public-surface and package containment, test accounting, documentation
and applicable integrated verification into successor review. The older
RE2/J-specific findings retain their evidence role for the original candidate.
The next backlog revision can be prepared ahead of Architecture release. The
next standalone assignment requires an accepted backlog revision and the
other four DR-6 conditions. Dependent acceptance requires incorporated and
verified architecture. [Independent software review][independent-010]

[task]: ../../evidence/1.1.28/001/before/ai-agent-tasks/unresolved-topic-topicmask-continuum-reconciliation.md
[dispositions]: ../../evidence/1.1.28/001/before/ai-agent-tasks/unresolved-complete-disposition-record.md
[disposition-report]: ../../evidence/1.1.28/001/after/ai-agent-reports/20261124T062555Z-complete-disposition-record.md
[dr6-task]: ../../evidence/1.1.28/001/before/ai-agent-tasks/unresolved-revised-dr-6-disposition.md
[dr6-report]: ../../evidence/1.1.28/001/after/ai-agent-reports/20261124T055110Z-revised-dr-6-disposition.md
[report]: ../../evidence/1.1.28/001/after/ai-agent-reports/20261124T054105Z-topic-topicmask-continuum-reconciliation.md
[verification]: ../../evidence/1.1.28/001/after/verification.json
[locator]: ../../evidence/1.1.28/001/after/01-retained-topic-input-locator.patch
[candidate]: ../../evidence/1.1.28/001/after/02-topic-topicmask-continuum.patch
[design]: ../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[design-acceptance]: ../software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[finalisation]: ../software-0.1.0/hcrl-software-0.1.0-007.pending.md#dr-1
[reset]: ../../register/software-0.1.0/hcrl-software-0.1.0-009.complete.md#dr-3
[reset-verification]: ../../register/software-0.1.0/hcrl-software-0.1.0-009.complete.md#dr-2
[predecessor]: ../../register/1.1.26/hcrl-1.1.26-001.complete.md
[predecessor-topic]: ../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3
[prior-identity]: ../1.1.27/hcrl-1.1.27-001.pending.md#dr-3
[archunit]: ../1.1.27/hcrl-1.1.27-002.pending.md
[retained]: ../../evidence/1.1.25/001/before/accepted-input/DRP-Topic-and-TopicMask.Final.001.md
[identity-feature]: ../../../continuum/strategy/capability/feature/9210349f026b4ff1aca1092c309b4139.md
[policy]: ../../../governance/architecture-release-policy.md
[software-praxis]: ../../../governance/software-development-praxis.md
[final-003]: ../../offline-design/DRP-Topic-and-TopicMask.Final.003.md
[final-003-boundaries]: ../../offline-design/DRP-Topic-and-TopicMask.Final.003.md#7-consumer-boundaries
[final-003-acceptance]: ../../evidence/1.1.28/001/before/ai-agent-tasks/20261124T222614Z-topic-drp-final-003-acceptance.md
[final-003-task]: ../../evidence/1.1.28/001/before/ai-agent-tasks/20261124T234229Z-final-003-incorporation-review.md
[final-003-report]: ../../evidence/1.1.28/001/after/ai-agent-reports/20261124T235126Z-final-003-incorporation-review.md
[final-003-dispositions]: ../../evidence/1.1.28/001/before/ai-agent-tasks/20261125T000910Z-final-003-incorporation-dispositions.md
[final-003-disposition-report]: ../../evidence/1.1.28/001/after/ai-agent-reports/20261125T001254Z-final-003-incorporation-dispositions.md
[disposition-preimage]: ../../evidence/1.1.28/001/before/review-disposition/hcrl.before-final-003-dispositions.md.gz
[final-003-add]: ../../evidence/1.1.28/001/after/final-003-incorporation.proposed.patch
[earlier-final-003-add]: ../../evidence/1.1.28/001/after/final-003-continuum-add.proposed.patch
[retirements]: ../../evidence/1.1.28/001/after/final-003-retired-source-record.md
[coherence-praxis]: ../../evidence/1.1.28/001/after/software-development-praxis-pre-1-0-0.proposed.patch
[hcrl-preimage]: ../../evidence/1.1.28/001/before/review-disposition/hcrl.before-final-003-incorporation.md.gz
[review-praxis]: ../../../governance/architecture-review-praxis.md
[backlog-0003]: ../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md
[software-010]: ../software-0.1.0/hcrl-software-0.1.0-010.pending.md
[independent-010]: ../../evidence/software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md
