Report-Produced-At: 2026-11-25T00:12:54.309190Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Final.003 incorporation dispositions and verification — Report

**Outcome** · [HCRL 1.1.28/001][hcrl] records DR-8 **ACCEPTED**, DR-9
**ACCEPTED** and DR-10 **ACCEPTED WITH QUALIFICATION** under the [issued
Solution Architect disposition][task]. DR-1–DR-7 retain their text and
historical authority. [Final.003][design] governs where earlier software
implications conflict, including RE2/J and runtime Topic UUID assumptions.
The accepted ADD and Praxis amendment are prepared for controlled application.

**Predecessor** · The [release authority][release] and [completed
register][current] identify 1.1.26. The branch remains
`feature/BL-0003-Calliope-Topic-and-TopicMask` at commit
`f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`. [1.1.27/002][archunit]
remains separately pending. There is no accepted 1.1.27 result in the release
authority or register. Live application, allocation, and completion of
1.1.28 remain pending. The replay below uses an isolated copy of 1.1.26.

## Exact inputs and application status

| Decision | Reviewed SHA-256 and result |
| --- | --- |
| DR-5 | [Two-path locator correction][locator], `f29779bd8b8b0000442d5413293b157c84de4446239b5031980364f534a49418`. Applied once in the isolated replay. Live application remains pending. The retained input matches SHA-256 `1690ca87200db65d2254acfb5af68b7b960fb044934682b6ed89eb77ce9c5e40` and keeps its original source identity and bindings. |
| DR-8 | [Replacement ADD][add], `e92f13ff2a2945350f376a5854e9b225534ebe40b78e3ce21ccbd6d186034cc7`. Replayed exactly after DR-5. Seven definitions, two association descriptions and [eight scoped retirements][retirements] reproduce the accepted review candidate. Live application awaits the accepted 1.1.27 predecessor. |
| DR-9 | [Praxis amendment][praxis], `7909b8d2c294b1d8e50b82f31a67682d651cb3ccff373077228b3d7bf4b203ca`. Replayed exactly under its distinct authority in the same proposed 1.1.28 change set. The live Software Development Praxis retains its current incorporated text. |

The isolated replay loaded Final.003 at SHA-256
`3902908dbffec9b35c15f3d31bf05b9b3a3e885dabecc71780c51fe4d9886043`
and its attributable acceptance record. `git apply --check --whitespace=error`
passed on each patch at its corresponding preimage. The combined result
matched the [reviewed candidate][reviewed] byte for byte across the
Continuum and governance sources. There were zero patch deviations and
the [reviewed eight-file candidate inventory][reviewed] still applies.

**DR-5 placement** · The live canonical validator still fails at the
missing `accepted-input/DRP-Topic-and-TopicMask.Final.001.000.md` retained
locator. [1.1.27/002 DR-2][archunit-gate] records the dependency. On the
current verification path, 1.1.27 is the earliest required carrier. Record
the correction in that transition's own review, then apply and verify it there.
If that review explicitly dispositions the earlier dependent verification
and leaves DR-5 to 1.1.28, apply it once before DR-8. The separate 1.1.27
review and its evidence retain their entry state.

## Candidate checks and release qualifications

| Check | Outcome and consequence |
| --- | --- |
| Live 1.1.26 baseline | **FAIL, pre-existing.** Canonical validation reproduced the missing retained-input locator. |
| DR-5 only | **PASS.** Canonical validation and retained-input checks. The corrected Continuum matched the previous locator-only replay and preserved 8,459 source bindings. |
| Combined candidate | **PASS.** The maintained canonical validator reported 6,563 elements with canonical and representation identities and provenance coverage, 9,427 relationships, 462 qualified source predicates and 8,466 bindings. Navigation passed. The exact match to the reviewed result preserves all endpoints and directions, 1,304 authored Views and unrelated sources. |
| Open Exchange | **PASS.** The maintained `project --check` regenerated the Exchange in memory and matched the reviewed output byte for byte. Offline schema, references and containment passed with zero allocated identities. SHA-256 `3426ae0c3eb82464180408bf3343494cddcc53ae64002bcb7303a87d66687e1e`. These are candidate checks against 1.1.26. |
| Named ArchUnit suite | **UNRESOLVED.** The required source path is absent. The [earlier invocation][archunit-result] executed zero tests. 1.1.27/002 owns proposed retirement and gate treatment. The 1.1.28 review must establish the accepted treatment and applicable structural-conformance evidence before closure. |
| Actual Archi transport and human review | **UNRESOLVED.** Actual import/export was **NOT_PERFORMED**. The 1.1.28 release review must determine and exercise the applicable transport and structural gates on the resulting model under the [Review Praxis][review]. The Solution Architect must review and confirm the exact incorporated result. |
| Historical provenance | **UNRESOLVED.** The iteration evidence check retains four missing historical Task issuance times and prefixes. DR-7 qualifies only its original reconciliation Task. The DR-6 revision, disposition completion and governance readiness Tasks require explicit treatment within 1.1.28/001 before closure. The current disposition has an attributable observed Task time. |

## Remaining sequence and workspace inventory

1. Complete the separate 1.1.27/002 review, its DR-5 treatment and verification.
   Establish its exact accepted result and match the 1.1.28 patches to that
   predecessor. Carry DR-5 forward only if it remains outstanding.
2. Apply DR-8 and DR-9 to the accepted predecessor. Record any necessary
   patch reconciliation and obtain disposition for substantive changes.
   Rerun the canonical, identity, reference, provenance, Exchange,
   navigation, View, structural and applicable transport checks on the actual
   result. Obtain Solution Architect confirmation and complete the
   governed release steps under the [release policy][policy].
3. Backlog Item 0003 may be revised separately against Final.003 before
   Architecture release. Standalone execution remains subject to the five
   DR-6 conditions and a separate assignment. Dependent software acceptance
   requires applicable architecture incorporation and conformance.

The workspace inventory is one modified [pending HCRL][hcrl] and three new
evidence files in iteration 001, the [Task][task], the [exact HCRL
preimage][preimage] (decompressed SHA-256
`2f78f103e00ef1823da44b2e055b580868048a9f43c855bd373f3f04e6d48a9b`),
and this Report. Entry comparison covered 12,932 tracked and nonignored
untracked paths. It confirmed preservation of the live Continuum, release
authority, prior evidence and reviewed patches. Calliope production Java,
JUnit tests, XML fixtures, Maven POMs, Backlog Item 0003 and Software HCRL 010
retain their entry bytes, including existing unaccepted candidate changes.
The branch, HEAD, tree and index retain their entry state.

[hcrl]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md
[task]: ../../before/ai-agent-tasks/20261125T000910Z-final-003-incorporation-dispositions.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.003.md
[current]: ../../../../../register/1.1.26/hcrl-1.1.26-001.complete.md
[release]: ../../../../../../../architecture-release.json
[archunit]: ../../../../../review/1.1.27/hcrl-1.1.27-002.pending.md
[archunit-gate]: ../../../../../review/1.1.27/hcrl-1.1.27-002.pending.md#dr-2
[archunit-result]: 20261124T054105Z-topic-topicmask-continuum-reconciliation.md
[locator]: ../01-retained-topic-input-locator.patch
[add]: ../final-003-incorporation.proposed.patch
[praxis]: ../software-development-praxis-pre-1-0-0.proposed.patch
[retirements]: ../final-003-retired-source-record.md
[reviewed]: 20261124T235126Z-final-003-incorporation-review.md
[preimage]: ../../before/review-disposition/hcrl.before-final-003-dispositions.md.gz
[review]: ../../../../../../governance/architecture-review-praxis.md
[policy]: ../../../../../../governance/architecture-release-policy.md
