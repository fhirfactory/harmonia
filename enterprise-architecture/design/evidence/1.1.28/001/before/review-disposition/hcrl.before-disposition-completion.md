<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Common Topic definition and runtime identity (R1) | Pending | Adopt the qualified accepted structure, identity, names and interchange. |
| [DR-2](#dr-2) | Common TopicMask definition and matching (R2) | Pending | Adopt structural identity and positional criteria, and distinguish explicit universality. |
| [DR-3](#dr-3) | TopicIdentityStable Feature (E04.01.04.F01) | Pending | Confirm the proposed scope of identity continuity through architectural rename or reclassification. |
| [DR-4](#dr-4) | IU and Store adapter correspondence and retained qualifications | Pending | Correct direct contradictions in the consumer descriptions and retain the governed boundaries. |
| [DR-5](#dr-5) | Retained Topic/TopicMask input locator | Pending | Authorise the two-path correction to the digest-matched retained input. |
| [DR-6](#dr-6) | Architecture transition and standalone Naming prerequisites | ACCEPTED WITH QUALIFICATION | Accept the reconciliation as supporting work for Software 0.1.0. Standalone Naming may start before the Architecture releases when the recorded conditions are met. |
| [DR-7](#dr-7) | Bounded Minutiae | Pending | Review supporting provenance and the unresolved Task issuance event. |

# HCRL 1.1.28 / 001 — Topic and TopicMask Continuum reconciliation

**Status** · Pending Architecture Change Review and release. DR-6 records the
Solution Architect’s qualified acceptance of the supporting work.

**Disposition correspondence** · The [revised instruction][dr6-task] refers to
earlier dispositions for DR-1–DR-5 and DR-7. The review copy at entry lists those
entries as pending. The attributable wording or reviewed source for those
dispositions remains to be established.

**Origin** · Accepted Architecture 1.1.26, Git `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`,
tree `1846199b36bbfb07c87746c1367c059698b7120d`, branch
`feature/BL-0003-Calliope-Topic-and-TopicMask`. Software development target
`0.1.0-SNAPSHOT`. The [foundation reset][reset] superseded exploratory software
candidates and preserved the qualified accepted design.

**Proposed target** · 1.1.28, conditional on [1.1.27/002][archunit] closing first
as a separate Architecture transition. DR-6 sets the conditions under which
standalone Naming can begin ahead of those releases. The current accepted
Architecture release remains 1.1.26.

**Review basis** · The [issued Task][task], qualified accepted
[Final.001.001][design], [Software HCRL 004 DR-10][design-acceptance],
[007 finalisation provenance][finalisation] and [1.1.25 DR-3][predecessor-topic].
Review the canonical amendments in [the locator patch][locator] and
[the Naming ADD patch][candidate], in that order against the origin. The patches
are the exact proposed result; the live Continuum represents the accepted baseline.

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

**Decision requested** · Accept the bounded common Topic correspondence. This
addresses R1's current-model contradiction upon incorporation. Applicable governed
reference or registry migration and any different uniqueness scope require separate
treatment. Neither a new registry service nor a Topic instance is proposed.

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

**Decision requested** · Accept the bounded common TopicMask correspondence. This
addresses R2's current-model contradiction upon incorporation. Existing governed
selectors, where present, retain the requirement for exact or inclusive criterion
equivalence, established unrestricted intent, and evidence against wider selection
when migrated. Themis and the consuming Features retain their decisions.

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

**Decision requested** · Accept this scope for E04.01.04.F01, or identify another
specific identity domain covered by the Feature and give its bounded treatment.
The accepted Naming design remains the basis for the candidate. The Feature's
material correspondence remains pending adjudication.

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

**Decision requested** · Accept these narrow downstream correspondences and
preserve the other DR-10 qualifications. R4's identity-domain distinction is
represented here, with concrete mappings for specialised references still required
where applicable. The Foundation Reset removed the exploratory implementation's
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

**Decision requested** · Authorise the exact locator correction. It can be
reviewed independently in time for 1.1.27/002's dependent validation. Record
its application in the authorised transition that carries it and reference
that disposition here. Preserve 1.1.27/002's existing review and evidence.

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

**Remaining Architecture review and verification** · Before closing a dependent
transition, establish which authorised scope carries DR-5 or record the specific
verification treatment. Recheck the exact accepted predecessor and rerun applicable
checks before application. The current ArchUnit command executes zero tests on the
reset checkpoint. Its missing coverage remains open for explicit disposition or
treatment through the separately governed 1.1.27/002 amendment. The canonical
validation result establishes only its recorded scope. [Disposition update report][dr6-report]

<a id="dr-7"></a>
## DR-7 — Bounded Minutiae

Supporting evidence comprises the [Task Detail][task], the [reconciliation
Report][report] and purpose-selected patch and verification evidence. The original Task
instruction-entry timestamp and filename prefix are **UNRESOLVED**. Require
Solution Architect treatment before closure. The Report emission has its own
observed timestamp. No historical time was reconstructed.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

[task]: ../../evidence/1.1.28/001/before/ai-agent-tasks/unresolved-topic-topicmask-continuum-reconciliation.md
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
