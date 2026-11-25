Report-Produced-At: 2026-11-24T20:56:19.117222Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Topic and TopicMask DRP reconciliation and Draft.002.001

**Standing** · Review preparation complete. The design draft, filename
reconciliation and governance amendment await Solution Architect disposition.
[Operative Task][task] · [Filename and reference inventory][inventory] ·
[Design draft][draft] · [Final filename patch][rename] ·
[Governance amendment patch][amendment]

**Origin** · Architecture `1.1.26`, software `0.1.0-SNAPSHOT`, branch
`feature/BL-0003-Calliope-Topic-and-TopicMask`, HEAD
`f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`. [Root release authority][release] ·
[Acceptance provenance][accepted-design] · [Design finalisation][finalisation]

## Architecture Review preparation

[Architecture Governance][governance] is the proposed normative location
for the new offline DRP naming clause, adjacent to its repository structure
and migration rules. The amendment specifies the draft's preceding Final
baseline and draft iteration, the single resulting Final number, the
acceptance transition and historical reconciliation. Its text is submitted
as an exact patch under the [Review Praxis][review-praxis] and
[Change Provenance][provenance]. Adoption of a normative rule requires an
accepted Architecture transition and patch increment under the [release
policy][release-policy]. The mechanical filename correction preserves
standing, accepted semantics and historical provenance.

The two corrected Final paths replay in isolation. The mapping retains both
historical filenames as byte-exact compatibility copies, since accepted
Backlog Items, prior HCRLs and Tasks, archived inputs and Continuum source
bindings cite them. The first primary copy is byte-exact. The second changes
only the version label in the cover and its first Final lineage citation.
The approved source draft and its original acceptance metadata remain
retrievable. A later application can adopt the corrected paths for new
citations while keeping those byte-exact compatibility locators.

The formal Task and Report are associated with [Architecture 1.1.28/001][architecture],
the existing exact R1/R2 reconciliation under review. That review's DR-2
disposition covers RE2/J in the exact candidate, and DR-6 governs dependent
acceptance. This preparation supplies new material for Solution Architect
decision about the pending architecture path. The 1.1.28 HCRL and its exact
reviewed inputs are preserved.

The pending review needs an explicit disposition on the following matters.

- **Filename reconciliation and governance** · Confirm the two-copy mapping,
  compatibility-locator treatment and proposed amendment. Establish the
  applicable reviewed change set for application. Authorise any eventual
  historic locator retirement separately against its actual dependents.
- **R1/R2 design** · Adjudicate the wildcard language, version domain and
  comparisons, old and new selector format correspondence, supported surface
  and diagnostics choices in [draft §9][decisions]. Determine whether the new
  design receives a revised exact 1.1.28 candidate or a later Architecture
  transition, accounting for 1.1.27/002, DR-5, DR-6 and the release predecessor.
  Acknowledge the authoritative 1.1.25 incorporated model and the qualified
  offline acceptance. Preserve the canonical Topic and TopicMask IDs, and
  review each affected assertion and association before dependent acceptance.
- **Software dependency** · Treat Software 010 against the exact accepted
  authority applicable to its retained candidate. If the design changes,
  authorise the revised Backlog Item and software candidate separately and
  require fresh conformance and review evidence.

Preparation stops here under Architecture Governance's fail-closed rule and
AX-17. The originating iteration is an evidence locator. It supplies no new
transition assignment or adjudication. AX-08 supports preserving meaningful
historical correspondence, and AX-14 keeps filename, standing and release
identity distinct. The [draft's architectural correspondence][axioms] records
the matching, operational and authority constraints.

### Mechanical filename validation

The [existing Continuum validator][continuum-validator] checks canonical
elements, source bindings and retained evidence paths. Its [rendered link
checker][link-validator] checks links from canonical elements and skips
historical provenance. Neither enumerates offline DRP candidates.
[Architecture review evidence tooling][evidence-validator] already takes an
explicit Architecture/software target and HCRL iteration. Extend that
read-only check, following approval, with an explicitly selected list of
submitted offline Markdown DRPs and a separate historical inventory mode.
Apply this full-name syntax rule to each submitted basename.

~~~text
DRP-.+\.(?:Draft\.[0-9]{3}\.(?!000)[0-9]{3}|Final\.(?!000)[0-9]{3})\.md
~~~

A submitted candidate with bad case, a missing component, an extra Final
component or a zero iteration/Final should fail. Review baseline and
successor continuity, same-package stem, exact disposition and accepted
standing against the HCRL, since a filename alone cannot prove them. Report
old paths under the bounded reconciliation mapping or as historical
exceptions. Keep this check scoped to submitted offline Markdown DRPs so
the current corpus, evidence, template and index do not become adoption
blockers. The same syntax rule was applied here to a temporary replay of
the two Finals and the new draft and to the scoped historical inventory.

## Independent review finding treatment

The findings below retain their original applicability to the current
candidate. The proposed treatment depends on an accepted successor design.
[Independent review findings][findings] · [Junie Implementation Report][implementation-report]

| Finding | Required treatment under a future accepted simplified design |
| --- | --- |
| 1 · RE2/J lexical guards and valid braced escape | Replace the engine-specific checks only for a newly authorised candidate. The class, quote, escape, repetition, group depth, expansion and native program-limit findings still apply to the retained regex candidate. Replace their prospective criteria with wildcard grammar, literal and allocation boundary tests. |
| 2 · XML empty completed qualifier becomes `N0` | Correct strict reconstruction of completed fields. Qualifier generation belongs to assembly. Test independent malformed records and retention of actual decoded versions. |
| 3 · XML encoding and whitespace | Reject conflicting declarations and non-XML whitespace in element-only content, including universal records. Retain UTF-8/Base64, deterministic round trips and secure parser checks. |
| 4 · Late canonical depth bound | Bound segment materialisation while scanning in both parsers. Verify early rejection of a seventeenth entry and safe depth metadata. This defect is independent of the regex envelope. |
| 5 · Loss of failure context | Preserve actual read, navigation, or builder operation, field and known valid entry index. Verify handling of absent index, malformed fields and failed builder operations, with payload-free diagnostics. |
| 6 · Public API and package containment | Decide the six model types plus necessary validation metadata and four XML operations as the supported scope. Review visible constructors, cross-package internal constants and exception factory, match-only records and enums, and codec placement. Require an exact public member inventory. Any production diagnostic handler needs explicit disposition. |
| 7 · Verification and evidence | Establish the test count and coverage from actual reports and tests. The independent clean and ordinary 50-module runs executed 36 distinct tests, all in Calliope across seven classes. The 72 claim has no supporting execution, and the claimed native program-size unit test is absent. Distinguish the successful independent probe of that boundary from the candidate test suite. Record missing cross-module regression, integration and applicable conformance evidence for disposition. |
| 8 · Educational documentation | Correct rendering guidance, absence and access errors, builder growth and mode explanations, validation metadata and package responsibilities. Apply the [Implementation Guide][guide] to actual supported behaviour, including recovery and 95/5 failure paths. |

Keep the positive independent evidence for Topic semantics, logical kind,
containment, immutable snapshots, typed diagnostics and concurrency at its
recorded scope. In a future submission, require strict independently authored
XML fixtures, exact and exceeded limits, structural tests that check actual
dependency coordinates/scope and exported members, and timed mixed-outcome
concurrency and late mismatch tests. Retain the operational logging and
interim structural verification qualifications as recorded. An approved
version contract, supported API scope, governed migration and required
integration treatment still need explicit decisions.

## Backlog Item 0003 next revision

Use the adjudicated design as authority and identify the exact new item
revision and scope. Keep [accepted revision 000.003][backlog] and its reviewed
input as historical records. Required specification changes are as follows.

| Criteria or section | Required revision after adjudication |
| --- | --- |
| Context, associated requirements, handoff, expected outcomes | Cite the exact accepted successor and its applicable Continuum/HCRL decision, selector compatibility scope, and approved Java/XML and diagnostics surface. Carry forward the preserved model and consumer boundaries. |
| AC-1, AC-2 and AC-5 | Preserve Topic identity, names, navigation and builders. Describe mask structural equality over original text and optional constraint presence. Cover position-aware validation and builder preservation across rejection paths. |
| AC-3 and AC-4 | Specify the adjudicated version and range contract and optional constraint states. Replace regex behaviour, flags and quoting with the six text shapes, literal and empty cases, both escape layers, fail-closed outcomes, null, ordered checks and containment. |
| AC-6 and AC-7 | Specify the accepted canonical version and XML contract, including dialect discrimination if applicable. Verify ordered raw data, absence versus present-empty and exact deterministic reconstruction using independent fixtures. Correct empty-qualifier, encoding and whitespace handling. |
| AC-8 and AC-9 | Replace engine specific safeguards with grammar, decoded and encoded limits, early depth and allocation bounds, maximum input work and safe boundary diagnostics. Retain XML hardening and test evaluation/handler failure. Verify approved version bounds independently. |
| AC-10 and AC-11 and dependency scope | Use immutable wildcard descriptions and bounded mixed-outcome concurrency. Retire the direct RE2/J allowance and its probes only for the revised authorised implementation. Retain authorised test scope, Calliope independence, the line limit, and deliberate codec, validation and local diagnostic package/visibility checks. |
| AC-12 and AC-13 | Require exact-candidate evidence with actual test counts, reviewed analogue coverage, solution-wide applicability and dispositions, plus educational documentation and accepted diagnostics deferrals. Keep operational logger reliability, backpressure and delivery evidence in its governed scope. |

## Software HCRL 010 dispositions to obtain

The [pending HCRL][software-review] retains the independent changes-requested
recommendation. The Solution Architect dispositions need to identify the
actual candidate and applicable architecture for each matter.

| HCRL matter | Required disposition or application evidence |
| --- | --- |
| DR-1 | Decide the disposition of the existing RE2/J candidate against its reviewed limits. If approving a successor, record the exact superseding design and revised backlog authority. Carry early canonical depth enforcement and bounds testing forward in either case. |
| DR-2 | Require strict completed data, XML envelope and kind handling, and truthful bounded failure metadata. Retain secure parser, logical absence/presence, typed failure and operational logging requirements. |
| DR-3 | Decide the supported public types and methods, factories, codec placement and cross-package contract. Set diagnostic visibility, optional handler treatment and approval boundary before public API freeze. |
| DR-4 | Retain the evidence correction for the unsupported test count and misstated coverage. Decide missing integrated and architectural verification treatment, including 1.1.28 DR-5/DR-6 and the separate 1.1.27/002 amendment. Require the exact resulting candidate, repeat applicable checks, independent review and first software acceptance disposition. |
| DR-5 | Preserve both original Junie records, the exact patch and independent evidence. Resolve the checker format mismatch by an attributable governed correction or explicit treatment. The unavailable original Junie Report event and filename prefix remain unresolved. |

Any correction to the Junie test accounting or event-field representation
should retain the original report and establish separate attributable
correspondence. No original event is inferred from file metadata or tool
output. [Software Development Praxis][software-praxis]

## Verification and resulting state

| Check | Outcome |
| --- | --- |
| Baseline inventory, patch preflight and isolated replay | **PASS** · The two patches pass `git apply --check --whitespace=error` and replay against the original source. The two old paths retain the original blobs. Proposed Final.001 matches the retained accepted input. Final.002 preserves §§1–10 and changes two cover lines only. Draft §§1–3 and 7 match the qualified baseline clauses. |
| Version syntax and relative links | **PASS** for the three submitted names and the new deliverable links in the scoped overlay. Reject wrong-case, missing-component, extra Final and zero version examples. Six pre-existing link failures in two verbatim HCRL snapshots are reported in the inventory, with their originating HCRL locator. The 81 other syntax exceptions are inventoried for separate disposition. |
| Exact software candidate and entry state | **PASS** · Replayed the retained patch over the HEAD Calliope subtree and matched all 33 paths byte-for-byte. Patch SHA-256 `70ad92b2503e89b15ff150e89562723b23ca80c1f7cc94648863e7f5a553f97a`. Existing worktree files, including reports and backlog review input, match entry fingerprints. Branch, HEAD, tree and index match entry. |
| Canonical validation | **FAIL** · Bounded `architecture_continuum.py validate` on the live 1.1.26 baseline returned the existing missing retained-input locator recorded in [1.1.28 DR-5][locator]. Its exact correction remains in that review. |
| Architecture 1.1.28/001 evidence | **UNRESOLVED** · The conventional event field and UTC prefix are supported for this Task. The iteration checker reports four earlier Tasks with unavailable original times. Apply the recorded qualifications and closure treatment. |
| Software 010 evidence | **FAIL** · The conventional event-field checker rejects the two existing Junie record formats. The Codex review records are recognised and the original Junie Report event remains unavailable. |
| Architecture test and software test execution | No ArchUnit test sources exist at the specified suite path in this checkout. The scoped qualification and missing coverage remain visible. Software test evidence above comes from the retained independent review of the exact candidate. This assignment ran document, provenance and integrity checks. |

The live deliverable is the proposed `Draft.002.001` in offline-design and its
review material within the originating evidence boundary. Architecture remains
at 1.1.26 and git remains at the entry HEAD with a clean index. The acceptance
and application gates for the two patches and design remain pending.

[task]: ../../before/ai-agent-tasks/20261124T204957Z-topic-drp-reconciliation-and-draft.md
[inventory]: ../topic-drp-filename-inventory.md
[draft]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Draft.002.001.md
[decisions]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Draft.002.001.md#9-decisions-for-solution-architect-adjudication
[axioms]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Draft.002.001.md#architectural-correspondence
[rename]: ../topic-drp-final-filename-reconciliation.patch
[amendment]: ../drp-filename-governance-amendment.patch
[release]: ../../../../../../../architecture-release.json
[accepted-design]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[finalisation]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-007.pending.md#dr-1
[architecture]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
[locator]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md#dr-5
[software-review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
[governance]: ../../../../../../governance/architecture-governance.md#repository-structure-and-migration-boundary
[review-praxis]: ../../../../../../governance/architecture-review-praxis.md
[provenance]: ../../../../../../governance/architecture-change-provenance.md
[release-policy]: ../../../../../../governance/architecture-release-policy.md
[software-praxis]: ../../../../../../governance/software-development-praxis.md
[continuum-validator]: ../../../../../../tooling/architecture_continuum.py
[link-validator]: ../../../../../../tooling/verify_continuum_links.py
[evidence-validator]: ../../../../../../tooling/architecture_review_evidence.py
[findings]: ../../../../software-0.1.0/010/after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md#findings-and-required-treatment
[implementation-report]: ../../../../software-0.1.0/010/after/ai-agent-reports/unresolved-naming-0003-implementation-report.md
[backlog]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md
[guide]: ../../../../../../../docs/implementation/Harmonia-Implementation-Guide-Philosophy.md
