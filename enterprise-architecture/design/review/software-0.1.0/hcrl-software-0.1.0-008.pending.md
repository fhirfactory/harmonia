<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1-implementation-authority-and-candidate-scope) | Iteration 008 implementation authority and scope | PENDING SOLUTION ARCHITECT REVIEW | The assigned Task authorizes implementation and testing only; this HCRL does not accept the candidate or establish a release. |
| [DR-2](#dr-2-topic-and-topicmask-implementation-conformance) | Accepted Topic/TopicMask software candidate | PENDING SOLUTION ARCHITECT REVIEW | The Implementation Report maps behavior and tests; design qualifications and integration limits remain explicit. |
| [DR-3](#dr-3-backlog-compatibility-and-historical-migration) | Backlog/API compatibility, serialization, and migration | ACCEPTED WITH QUALIFICATIONS | Acceptance covers [Backlog Item 0003, revision 000.002][corrective-backlog] as the corrective work specification. Implementation acceptance and historical compatibility/migration remain open. |
| [DR-4](#dr-4-iu-identity-and-selector-boundaries) | IU representation, legacy selectors, and neutral security selection | PENDING SOLUTION ARCHITECT REVIEW | No IU qualifier vocabulary or historical selector equivalence is inferred. `TopicSubscription` semantics remain unchanged. |
| [DR-5](#dr-5-resource-limits-regex-and-logging) | Candidate limits, RE2/J, logging, performance, and reliability | PENDING SOLUTION ARCHITECT REVIEW | Candidate numeric limits and local diagnostics are implemented; release allocation and shared/remote logging evidence remain open. |
| [DR-6](#dr-6-integrated-verification-and-unexecuted-gates) | Candidate verification and unexecuted integration gates | PENDING SOLUTION ARCHITECT REVIEW | Full reactor passes; ten live-service/broker tests were skipped and require explicit treatment before acceptance. |
| [DR-7](#dr-7-inherited-review-matters-and-v001-preservation) | HCRL.004 carry-forward and Iteration 006 V001 boundary | PENDING SOLUTION ARCHITECT REVIEW | Prior DR-1–DR-9, DR-11, DR-12 and V001 remain pending in their original records and are not accepted here. |
| [DR-8](#dr-8-evidence-times-and-repository-state) | Provenance, unresolved timestamps, and repository state | PENDING SOLUTION ARCHITECT REVIEW | Task/Report event timestamps remain unresolved; the task-specific pre-edit status capture was not separately recorded. |

# HCRL software-0.1.0 / 008 — Calliope Topic/TopicMask Implementation

**Current candidate disposition · Superseded by the clean-slate reset.**
The Solution Architect's [reset assignment][reset-assignment] closes this Naming
implementation candidate without implementation acceptance. Retain the qualified
Backlog disposition in DR-3, the accepted offline design recorded in 004/007, and
all historical findings. The [reset HCRL][foundation-reset] identifies the
recoverable source and the boundaries of this supersession.

The original review record follows.

**Iteration status · PENDING SOLUTION ARCHITECT REVIEW.** This HCRL records implementation activity under the Solution Architect's [Iteration 008 Task][task]. It is a candidate review request only; it supplies no implementation acceptance, first-software-baseline decision, release allocation, commit, tag, push, deployment, or Architecture change.

- **Target software candidate** · `0.1.0`; no accepted software predecessor or first-release decision is inferred.
- **Architecture baseline** · `1.1.26`; root [Architecture release authority][architecture-release] remains `1.1.26` and was not changed.
- **Accepted design** · [Final.001.001][design], accepted with qualifications under [HCRL.004 / DR-10][original-review] with acceptance/finalisation provenance in [HCRL.007][finalisation].
- **Originating implementation backlog** · [BL-CALLIOPE-OO-01, revision 1][backlog].
- **Accepted corrective work specification** · [Backlog Item 0003, revision 000.002][corrective-backlog] under [DR-3](#dr-3-backlog-compatibility-and-historical-migration).
- **Candidate Git state** · Branch `feature/BL-CALLIOPE-OO-01`, HEAD `8af315134a1f2e03e75512f66513ef2f7eeb80b8`; worktree remains uncommitted. No Git operation changed branch or index.
- **Prior worktree boundary** · HCRL.007's Report records the same branch/HEAD and 42 pre-existing modified/untracked files. The session's source edits were confined to `calliope/`; no Hestia/V001 path was edited. This Task-specific activity did not separately capture a `git status` before source edits; see DR-8.
- **Evidence** · [Implementation Task][task] and [Implementation Report][report]. Task-Issued-At and Report-Produced-At remain unresolved; filenames preserve the unresolved prefixes.

<a id="dr-1-implementation-authority-and-candidate-scope"></a>
## DR-1 — Implementation Authority and Candidate Scope

The Solution Architect's Task authorizes implementation and testing of the accepted Topic/TopicMask design in Calliope, subject to the condition that Iteration 008 is available. The [Task][task] records no pre-existing Iteration 008 HCRL/Task/Report collision. It explicitly withholds software acceptance, release, and commit/push authority.

The bounded source delta is the Calliope Topic/TopicMask model, support, direct consumer adaptations, and tests described in the [Implementation Report][report]. Hestia/Mnemosyne V001 and other pre-existing worktree changes remain outside the Calliope source delta and were not edited by this session. The integrated candidate is nevertheless submitted for solution-wide review as required by the Software Development Praxis; no separate acceptance of that pre-existing work is inferred.

The [accepted design][design] and its qualifications govern implementation behavior. The separate HCRL.004 DR-1–DR-9 candidate-review dispositions are not replaced by this Task and remain open as specified in DR-7 below.

**Disposition requested** · Review that the candidate stayed within the authorized Calliope implementation boundary, that Iteration 008 is correctly established, and that no release/commit authority has been inferred. Record implementation acceptance or requested corrections only after independent review.

<a id="dr-2-topic-and-topicmask-implementation-conformance"></a>
## DR-2 — Topic and TopicMask Implementation Conformance

The candidate implements the ordered immutable TopicEntry model; qualifier/value identity and ordering; versioned and non-versioned canonical forms; strict structural and character parsing; hierarchical navigation; sequential/indexed reusable TopicBuilder; ordinary/universal TopicMask kinds; positional exact/contained matching; independent qualifier/value/version RE2/J expressions; canonical mask reconstruction; literal helpers; mask builder snapshots; and logical JSON/Java serialization proxies. Exact behavior, support classes, consumers, and criterion-level evidence are in the [Implementation Report][report].

No Architecture Continuum file, accepted ADR, or canonical design was edited. The implementation establishes no Themis authority: a successful match is neutral selection only. `InformationSecurityContext` retains its independent security-context and action responsibilities; the Task's security-sensitive matching is literal and fails closed on invalid selectors.

The inherited design requires actual module allocation, compatibility and solution-wide conformance evidence. The test results are positive evidence for the behaviors exercised, not proof of historical migration, every external selector, or release acceptance.

**Disposition requested** · Review the exact integrated candidate against Final.001.001 §§1–10 and the Implementation Guide. Decide whether the implementation meets the accepted Java and behavioral contracts, subject to each outstanding qualification in DR-3–DR-6.

<a id="dr-3-backlog-compatibility-and-historical-migration"></a>
## DR-3 — Backlog Compatibility and Historical Migration

At implementation submission, [revision 1][backlog] required existing public and serialization contracts to remain compatible (AC-2) and duplicate Topic positions to be rejected (AC-3). Final.001.001 §8 establishes the new API without Topic/TopicMask UUID identity, and §2 defines indexed replacement. The original Task directed implementation of the accepted design and preserved revision 1 as the originating work specification. The corresponding criterion and compatibility findings remain in the [independent review][backlog-independent-review].

The candidate removes old UUID-derived Topic APIs and the old map-based `getCanonicalName()` contract; `TopicMask` no longer has the old UUID/two-basis form. Some metadata UUID-derived helper names were replaced with canonical-name projections. The deprecated five-position Topic constructor and selected positional compatibility projections remain where meaningful. Old JSON/Java-serialized forms, persisted UUID references, registry uniqueness/aliases, historical selector corpora, and external clients were not replayed or migrated.

The current Git status reports `TopicMaskEntryPattern.java` and `TopicMaskType.java` as deleted. Their origin cannot be attributed to this activity because the task-specific pre-edit status was not separately captured; no references remain in the candidate. Their deletion is not presented as evidence of historical serialization or API migration.

**Original disposition request** · Adjudicate the affected Backlog AC-2/AC-3 status for this candidate; specify any required compatibility adapter, migration evidence, serialization fixture, retained API, or authorized qualification before software acceptance. Preserve the Backlog itself unless separately authorized.

**Solution Architect disposition · Accepted with qualifications.** Mark Hunter
accepted the substantive content of the proposal reviewed as
`BL-CALLIOPE-OO-01`, revision `2`, and directed the AC-6 clarification. His
subsequent identity correction establishes [Backlog Item 0003, revision 000.002][corrective-backlog]
as the accepted work specification for the subsequent bounded corrective
implementation assignment. [BL-CALLIOPE-OO-01, revision 1][backlog] is the
historical predecessor and remains the source for the original Implementation
Task and Report. The qualified acceptance resolves B-01 for the corrective
work specification. [Original disposition][backlog-acceptance-task] ·
[Disposition application Report][backlog-acceptance-report] ·
[Operative identity correction][backlog-identity-task] · [Correction verification][backlog-identity-report]

**Qualifications**

- AC-6 records Topic/TopicMask component-level conformance separately from
  integrated Harmonia software-release acceptance.
- Outstanding platform-level integration, logging and reliability evidence
  remains subject to separate Solution Architect dispositions under DR-5 and DR-6.
- Applicable historical compatibility and migration obligations remain open,
  including affected identifiers, registry rules, representation boundaries and
  selector scope. Architecture Continuum reconciliation remains separately
  governed under the qualified design acceptance.
- The Iteration 008 implementation candidate and the software release remain
  pending separate acceptance.

<a id="dr-4-iu-identity-and-selector-boundaries"></a>
## DR-4 — IU Identity and Selector Boundaries

`TopicFactory.forInformationUnit(UUID)` continues to produce the unqualified raw name `harmonia.mneme.iu.<hex>` and does not replace or own the InformationUnit's independent UUID. Generated `N<position>` qualifiers are structural builder defaults only; they do not establish an IU qualifier vocabulary. The fully qualified IU Topic representation and canonical boundary mapping remain the separate decision identified in HCRL.004 / DR-10.

Legacy Exact/Inclusive name-only and qualifier/value selectors are adapted positionally. Blank, universal-looking, malformed, or ambiguous legacy input fails closed; literal-name helpers quote values rather than compiling them as arbitrary expressions. `InformationSecurityContext` reverse-derives an action display name only when the exact literal helper shape is recognized. `OperationalMetadataMask` fails closed when its legacy subject string cannot be parsed. `TopicSubscription`'s existing case-insensitive behavior was not migrated or proven equivalent. Historical selector corpora, persisted mask shapes, and every security-sensitive caller were unavailable.

**Disposition requested** · Confirm the IU qualifier and external/reference mapping, historical selector equivalence, security-sensitive scope, and treatment of the deliberately unchanged `TopicSubscription` behavior. No match is an authorization decision.

<a id="dr-5-resource-limits-regex-and-logging"></a>
## DR-5 — Resource Limits, RE2/J, and Logging

The candidate directly declares RE2/J `1.8` in Calliope, validates expression lengths before compilation, bounds decoded Topic fields and path depths, bounds canonical text lengths, uses RE2/J `Pattern.quote` for literal helpers, and reuses compiled immutable patterns with invocation-local matchers. Candidate limits are depth `32`, qualifier `64`, value `256`, version `64`, expression `1024`, canonical Topic text `24,704`, and mask text `196,864` code points. Boundary tests exercise these proposed bounds. No separate accepted numeric release policy or measured worst-case performance limit was found; these values and evaluation cost remain qualified candidate choices.

Safe non-PHI local TRACE/ERROR diagnostics use the existing SLF4J API and do not log values, expressions, or exception payloads. Calliope's `PhiLogger` is a PHI-only facade and is not used for these non-PHI errors. This task did not establish the separate Log-Satellite/remote delivery path, bounded latency, saturation, outage, recovery, or delivery-uncertainty behavior, nor a cross-module normal/failure performance envelope.

**Disposition requested** · Confirm the candidate-specific resource limits, regex safeguards and release-specific placement; determine the required shared logging/reliability integration and outstanding performance evidence before acceptance. No new framework or runtime service is proposed here.

<a id="dr-6-integrated-verification-and-unexecuted-gates"></a>
## DR-6 — Integrated Verification and Unexecuted Gates

The [Implementation Report][report] records exact modules, environments, commands, results, initial correction attempts, and limitations. The final full reactor passed all 50 modules: 336 test classes, 1,765 tests, 0 failures, 0 errors, and 10 skipped. Nine skips are in Hestia `DistributedAuthoritativeDockerPathTest` groups: M24 (2), mTLS (3), public-FHIR separation (1), and Path A (3). One Petasos Artemis-module integration test was skipped. They require live Docker/network/broker conditions not present in the run. Calliope, architecture, Agora, Erga, Pylai and Hestia modules compiled and ran as recorded; no code coverage report or application deployment was produced.

**Disposition requested** · Decide whether the ten unexecuted gates and absent coverage/deployment evidence require additional candidate work or may be accepted with explicit qualification. Do not treat a skipped gate as PASS.

<a id="dr-7-inherited-review-matters-and-v001-preservation"></a>
## DR-7 — Inherited Review Matters and V001 Preservation

HCRL.004 DR-1–DR-9, DR-11 and DR-12 retain their recorded `PENDING REVIEW`/`PENDING SOLUTION ARCHITECT REVIEW` statuses and scope; this iteration does not copy, close, or infer dispositions for them. HCRL.004 / DR-10 remains accepted with the seven qualifications carried in DR-3–DR-6 and the architectural correspondence obligation. HCRL.007 remains pending with its own unresolved provenance time. [Iteration 006][v001] remains the separate pending Mneme–Mnemosyne V001 implementation candidate.

No V001/Hestia file was edited, staged, or included in the Calliope source changes by this session. No prior HCRL, Task, or Report was rewritten. The Task-specific initial status limitation is stated in DR-8; it is not evidence that V001 is accepted or that its worktree content was independently re-diffed at this Task's start.

**Disposition requested** · Preserve all inherited pending matters and V001 standing. Confirm whether any inherited whole-solution DR must be revisited against this integrated candidate; otherwise keep it in its original HCRL.

<a id="dr-8-evidence-times-and-repository-state"></a>
## DR-8 — Evidence Times and Repository State

The [Task][task] preserves the operative assignment. Task-Issued-At is `UNRESOLVED`; the attributed instruction-entry timestamp was not exposed at the required precision. This Report's Report-Produced-At is also `UNRESOLVED`; no current time, filesystem metadata, unrelated capture time, or commit timestamp was substituted. Both filenames retain explicit `UNRESOLVED` prefixes for Solution Architect treatment before closure.

The immediately preceding HCRL.007 Report records branch `feature/BL-CALLIOPE-OO-01`, HEAD `8af315134a1f2e03e75512f66513ef2f7eeb80b8`, and 42 pre-existing modified/untracked files. This agent did not separately capture `git status` before source edits. Current branch and HEAD match that reference; all edit operations in this session were path-scoped to `calliope/` and this Iteration 008 evidence boundary, and no Git operation changed the index or branch. This scope history is evidence that Hestia/V001 paths were not touched, but the missing Task-time status snapshot remains disclosed.

No commit, tag, push, software release allocation, Architecture release bump, or repository cleanup occurred. The root Architecture-release authority remains `1.1.26`; no accepted software predecessor was established or inferred.

**Disposition requested** · Resolve both event timestamps and filename prefixes before closure, review the baseline-verification limitation, and confirm the candidate/Git-state correspondence. Do not manufacture a closure timestamp or accepted software baseline.

## Corrective implementation submission — renewed independent review requested

The Solution Architect's [corrective Task][corrective-task] directed correction of [independent-review][backlog-independent-review] findings A-01–A-06 and C-05 under [Backlog Item 0003, revision v000.002][corrective-backlog]. The [corrective Implementation Report][corrective-report] records exact source changes, finding dispositions, C-05 coverage, AC-1–AC-7 mapping, historical compatibility findings, verification and the exact candidate. The original [Implementation Report][report] and the independent review are retained unchanged. DR-1–DR-8 above are not rewritten and are not resolved by this submission.

- **Procedural nonconformance** · Substantive corrective edits began before the required Implementation Guide was read, and further edits followed before the Solution Architect's read condition was satisfied. Both were reported. The Guide was then read in full, the existing changes were reviewed against it and corrected, and resumption was authorised by the Solution Architect. The earlier 27-test / 10-failure run is pre-fix reproduction evidence only.
- **Fresh post-correction verification** · Calliope 292/0/0/0; architecture tests 90/0; full reactor (`mvn -o test -fae`) 1,786 run, 0 failures, 0 errors, 10 skipped (`DistributedAuthoritativeDockerPathTest` 9, `ArtemisHarmoniaSecurityIntegrationTest` 1). Skipped tests are not claimed as passed.
- **Candidate** · Branch `feature/BL-CALLIOPE-OO-01`, HEAD `8af315134a1f2e03e75512f66513ef2f7eeb80b8`, uncommitted worktree; corrective files and SHA-256 prefixes are listed in Report §7. Source changes are confined to `calliope/`.
- **Matters for adjudication** · Predecessor Topic UUID non-retention and native Java serialization compatibility; fail-closed narrowings for historical plain-text names, operational subjects and legacy action names; skipped infrastructure tests; outstanding platform logging/reliability evidence; and UNRESOLVED corrective Task-Issued-At and pre-edit repository-state capture.

**Disposition requested** · Renewed independent implementation review of the corrected candidate, followed by Solution Architect adjudication. This submission supplies no acceptance, baseline, release, commit, tag or push authority.

## Review route and retained history

The Task requests independent Codex implementation review followed by Solution Architect adjudication. This HCRL remains pending until those actions and all required decisions are recorded. The next step does not authorize an Architecture amendment, software release, commit, tag, push, deployment, or changes to Iteration 006.

[task]: ../../evidence/software-0.1.0/008/before/ai-agent-tasks/UNRESOLVED-topic-topicmask-implementation-task.md
[report]: ../../evidence/software-0.1.0/008/after/ai-agent-reports/UNRESOLVED-calliope-topic-topicmask-implementation-report.md
[design]: ../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[original-review]: hcrl-software-0.1.0-004.pending.md#dr-10
[finalisation]: hcrl-software-0.1.0-007.pending.md
[v001]: hcrl-software-0.1.0-006.pending.md
[backlog]: ../../../../docs/backlog/calliope-oo-01-topic-models.md
[corrective-backlog]: ../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.002.md
[backlog-independent-review]: ../../evidence/software-0.1.0/008/after/ai-agent-reports/20261124T005629Z-calliope-topic-topicmask-independent-review.md
[backlog-acceptance-task]: ../../evidence/software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-backlog-revision-2-acceptance.md
[backlog-acceptance-report]: ../../evidence/software-0.1.0/008/after/ai-agent-reports/20261124T011356Z-calliope-backlog-revision-2-acceptance.md
[backlog-identity-task]: ../../evidence/software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-backlog-identity-correction.md
[backlog-identity-report]: ../../evidence/software-0.1.0/008/after/ai-agent-reports/20261124T011844Z-calliope-backlog-identity-correction.md
[architecture-release]: ../../../../architecture-release.json
[corrective-task]: ../../evidence/software-0.1.0/008/before/ai-agent-tasks/unresolved-calliope-topic-topicmask-corrective-task.md
[corrective-report]: ../../evidence/software-0.1.0/008/after/ai-agent-reports/20261124T031123Z-calliope-topic-topicmask-corrective-implementation.md

[foundation-reset]: hcrl-software-0.1.0-009.pending.md
[reset-assignment]: ../../evidence/software-0.1.0/009/before/ai-agent-tasks/unresolved-software-foundation-reset.md#issued-instruction
