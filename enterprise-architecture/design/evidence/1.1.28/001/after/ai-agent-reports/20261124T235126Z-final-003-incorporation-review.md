Report-Produced-At: 2026-11-24T23:51:26.566728Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Final.003 Architecture incorporation candidate — Report

**Standing** · Ready for Solution Architect review in [Architecture 1.1.28/001][hcrl].
The [Task][task] approves the incorporation direction and the pre-1.0.0
implementation-coherence principle. [Final.003][design] is the accepted design.
The exact replacement ADD, the scope and wording of the Praxis amendment, and
the release transition remain for disposition in DR-8–DR-10.

**Origin** · Architecture 1.1.26, previous release 1.1.25, Software
`0.1.0-SNAPSHOT`, branch `feature/BL-0003-Calliope-Topic-and-TopicMask`,
HEAD `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`. The exact 1.1.26 Markdown baseline
and the already accepted Final.003 and its attributable acceptance record were
the inputs to the isolated replay. The live baseline and root release authority
remain at 1.1.26.

## Exact candidate and inventory

Replay against the pinned 1.1.26 source using `git apply`, in the order below.
Provide the accepted Final.003 source and its [acceptance Task][acceptance] at
their repository paths. The [initial Final.003 proposal][original-proposal]
and both earlier ADDs retain their historical review scope.

| Order and standing | Exact input and SHA-256 |
| --- | --- |
| 1. Accepted DR-5 correction | [01-retained-topic-input-locator.patch][locator] · `f29779bd8b8b0000442d5413293b157c84de4446239b5031980364f534a49418` |
| 2. Proposed replacement ADD | [final-003-incorporation.proposed.patch][add] · `e92f13ff2a2945350f376a5854e9b225534ebe40b78e3ce21ccbd6d186034cc7` |
| 3. Proposed normative governance amendment | [software-development-praxis-pre-1-0-0.proposed.patch][praxis-patch] · `7909b8d2c294b1d8e50b82f31a67682d651cb3ccff373077228b3d7bf4b203ca` |

[Final.003][design] retains SHA-256
`3902908dbffec9b35c15f3d31bf05b9b3a3e885dabecc71780c51fe4d9886043`.
DR-5 changes exactly the two `retainedAt` locators in Topic and TopicMask.
The [retained 1.1.25 input][retained-input] matches SHA-256
`1690ca87200db65d2254acfb5af68b7b960fb044934682b6ed89eb77ce9c5e40`.
The original source paths, bindings, selectors, decision authority and digests
were preserved. The replacement ADD adds seven distinct, section-scoped
Final.003 source bindings with the accepted design digest and attributable
authority. It removes `namingReconciliation` and uses existing standing and
provenance fields. The HCRL records the candidate's pending standing.

| Candidate file, relative to `enterprise-architecture/continuum/` | Scope |
| --- | --- |
| `application/data-object/9e2ffa9b4a1d4422a33c2a4bbcfc86ef.md` | Topic. DR-5 locator and ordered qualified runtime identity, opaque versions, construction, navigation and strict naming. |
| `application/data-object/bb489157a6bf48be926c5a601bb0a6c3.md` | TopicMask. DR-5 locator and the bounded wildcard, version, containment and interchange contracts. Directed TopicMask-to-Topic association `id-1adf053e54b74ba8a15db8eec3760f06`. |
| `application/application-component/307b3440a507403f8bc214d290bffb6a.md` | Calliope-Models. Supported Java and XML surface, containment, typed validation and invocation-local diagnostics. |
| `application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md` | Logical InformationUnit. Independent IU UUID and Topic designation. Directed logical IU-to-Topic association `id-14ff6e983e1e45ac81f7f181b5339c8c`. |
| `business/business-object/10284034c8534b639284386b064f2c54.md` | Conceptual Information Unit. Confirmed allocation, independent identity, Topic designation and retained guardrails. |
| `strategy/capability/feature/9210349f026b4ff1aca1092c309b4139.md` | TopicIdentityStable. Continuity of the same governed architectural element and separate runtime path identity. |
| `application/application-function/9c680d56a6b943da8ff22f3264e19bc7.md` | Mnemosyne Adapter (Store). Shared selection over structured Topics with the five existing interfaces and coverage and eligibility obligations. |

The eighth candidate file is
`enterprise-architecture/governance/software-development-praxis.md`.
Its exact patch applied unchanged. The amendment adds the accepted principle
in §2, includes dependency analysis and affected-consumer refactoring and
verification in §3's backlog scope, and uses §6's existing Implementation
Report and HCRL processes for exact candidate verification. The rule ends
at the Software 1.0.0 boundary. Later compatibility follows the applicable
release and interface contracts. The live [Praxis][praxis] remains the accepted
text. DR-9 asks for separately recorded authority for the normative amendment
within the same Architecture transition, or its own transition if required.

The [retired-source record][retirements] accounts for eight passages across
four of the affected definitions. It pins their committed originals and
identifies their current replacements. The cleanup retains the active
IU §§6.5.1–6.5.9 guardrails, governed registry obligations, the separate
IU vocabulary and mapping decisions, and the confirmed [1.1.25 IU and Store
authority][governed-iu]. It retires obsolete runtime Topic UUID, selector
and duplicated IU descriptions, including the inconsistent G3 row. The
InformationUnit UUID and the distinction between governed indivisibility and
transactional atomicity remain. The candidate introduces no legacy dialect,
compatibility API or migration mechanism.

AX-04 and AX-09–AX-12 are reflected in bounded string-based selection,
fail-closed evaluation and encapsulated Naming machinery. AX-06–AX-08
preserve neutral selection, Themis authority and Kleio audit responsibility.
AX-14 preserves the distinct identity, version, kind and presence semantics.
AX-15 and AX-17 keep unverified transition facts and unapproved scope open.
The source reconciliations retain current authority through existing elements
and edges. [Architectural axioms][axioms] · [Final.003 correspondence][design]

## Validation evidence

Run the maintained `architecture_continuum.py --root <isolated-root> validate`
and `project --output <candidate.xml>` operations, then rerun `project` with
`--check` against that output. Enable the repository's offline XSD dependencies
for projection. The runs used a temporary isolated root and a scratch output
file. The fingerprint of the deterministic output is
`3426ae0c3eb82464180408bf3343494cddcc53ae64002bcb7303a87d66687e1e`.
Publication remains a separate governed operation.

| Check | Observed outcome |
| --- | --- |
| Live 1.1.26 canonical validation | **FAIL, pre-existing.** The missing retained-input path still ends in `DRP-Topic-and-TopicMask.Final.001.000.md`. The reviewed correction remains outside the live baseline. |
| DR-5 in isolation | **PASS.** Correct original digest and accepted source identity. Exactly two locator changes. Baseline canonical validation passed with 8,459 source bindings and no duplicate binding. |
| Exact combined replay | **PASS.** Independent replay reproduced candidate bytes. Exactly seven Continuum files and one governance file differ from the locator-corrected precursor. All unrelated canonical files and the release authority matched. |
| Canonical and retained-source integrity | **PASS.** 6,563 elements, 9,427 relationships, 462 qualified source predicates and 8,466 source bindings. Canonical and representation identity and provenance coverage each reached 6,563. Eleven added canonical Markdown links resolved, including fragments. No candidate status mechanism or superseded UUID or regex requirement remains in the affected definitions. |
| Exchange projection | **PASS.** Offline XSD, references, containment and deterministic `--check`. Zero allocated publication identities. All element IDs and types, relationship IDs, endpoints, native types and direction attributes match the locator-corrected predecessor. Only the two named associations have amended canonical descriptions and related semantic wording. |
| Views and navigation | **PASS.** The canonical navigation file matched byte for byte. All 1,304 authored Views and their projected XML subtree matched. |
| Canonical negative controls | **PASS.** The maintained validator rejected a missing endpoint on the TopicMask association and rejected a duplicate Final.003 source binding in separate contaminated candidates. The rejected mutations were confined to scratch copies. |
| Named ArchUnit suite | **UNRESOLVED, pre-existing.** The mandated source suite is absent on the reset checkpoint. The [previous check][original-verification] executed zero tests. No suite result is attributable to this candidate. Obtain explicit treatment under [1.1.27/002][archunit] or the applicable dependent review. |
| Actual Archi transport and human verification | **UNRESOLVED.** Actual import/export transport was **NOT_PERFORMED**. Human review of the exact incorporation and, where applicable, the resulting publication or live workbench remains outstanding. Automated identity and View checks provide evidence for the applicable review gate determination. |
| HCRL provenance layout and timing | **UNRESOLVED, pre-existing.** The maintained checker reported zero layout errors and four historic unresolved Task issuance timestamps and filename prefixes. These concern the earlier reconciliation, DR-6 revision, disposition completion and governance readiness. The current Task and Report use observed event times. DR-7 covers only its original bounded group. |
| Software and accepted-state integrity | **PASS.** The exact reviewed software patch reproduces all 33 existing candidate paths. The entry workspace comparison covers 12,927 tracked or nonignored untracked paths and confirms preservation of prior evidence, accepted DRPs, backlog, Software HCRL 010, production code, tests, fixtures, POMs, live governance and the release authority. |

## Workspace changes and review sequence

This activity modifies one pending HCRL and adds five evidence files in its
existing iteration. The full workspace change inventory is the [HCRL][hcrl],
the [Task][task], this Report, the [replacement ADD][add], the
[retired-source record][retirements] and the [HCRL preimage][preimage].
The decompressed preimage SHA-256 is
`40cc36347d87dca99fc328bda2a46851a571693a3a71df6c18e0acefa0daa354`.
The existing locator and Praxis patches retain their exact bytes. Git
branch, HEAD, tree, index and pre-existing changes retain their entry state.

1. Keep [1.1.27/002][archunit] on its separate review path. Confirm the actual
   accepted predecessor for the Final.003 transition and place the already
   accepted DR-5 repair where it first becomes necessary for a dependent gate.
   If 1.1.27 carries DR-5, verify that exact result there. If the Final.003
   transition carries it, obtain explicit treatment of earlier dependent gates.
2. Disposition the exact ADD and retirement in DR-8, the amendment and its
   incorporation scope in DR-9, and remaining verification and release
   prerequisites in DR-10. Apply the authorised changes to the actual
   predecessor and repeat applicable checks. Confirm the exact incorporated
   result before Architecture completion under the [Review Praxis][review]
   and [release policy][policy].
3. Use Final.003 for the next [Backlog Item 0003][backlog] revision and
   affected-consumer analysis. Preserve the [Software 010 review][software]
   and its independent evidence for the existing candidate. The accepted
   backlog revision and separate assignment remain necessary under DR-6
   before standalone implementation. Dependent software acceptance requires
   incorporated and verified architecture and its own exact candidate and
   release verification.

[task]: ../../before/ai-agent-tasks/20261124T234229Z-final-003-incorporation-review.md
[acceptance]: ../../before/ai-agent-tasks/20261124T222614Z-topic-drp-final-003-acceptance.md
[hcrl]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.003.md
[locator]: ../01-retained-topic-input-locator.patch
[add]: ../final-003-incorporation.proposed.patch
[praxis-patch]: ../software-development-praxis-pre-1-0-0.proposed.patch
[original-proposal]: ../final-003-incorporation-proposal.md
[original-verification]: 20261124T054105Z-topic-topicmask-continuum-reconciliation.md
[retirements]: ../final-003-retired-source-record.md
[preimage]: ../../before/review-disposition/hcrl.before-final-003-incorporation.md.gz
[retained-input]: ../../../../1.1.25/001/before/accepted-input/DRP-Topic-and-TopicMask.Final.001.md
[governed-iu]: ../../../../../register/1.1.25/hcrl-1.1.25-001.complete.md
[archunit]: ../../../../../review/1.1.27/hcrl-1.1.27-002.pending.md
[axioms]: ../../../../../../governance/architectural-axioms.md
[praxis]: ../../../../../../governance/software-development-praxis.md
[review]: ../../../../../../governance/architecture-review-praxis.md
[policy]: ../../../../../../governance/architecture-release-policy.md
[backlog]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md
[software]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
