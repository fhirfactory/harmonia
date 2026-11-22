Report-Produced-At: 2026-11-20T09:15:09.680252Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — Calliope iteration 005 G06 lifecycle, revision and operational state

The G06 working draft records the Solution Architect's A13–A16 adjudications against Architecture Continuum 1.1.26 and `Software-Version-0.1.0-Snapshot-004` at `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d` on `feature/BL-CALLIOPE-OO-01`. Iteration 005 records these dispositions for draft review. G01/G02 acceptance and the remaining G03–G05/G07 findings keep their recorded standing. [Task][task] · [Draft.000.001][draft] · [Issue groups][groups]

## Decisions and reconciliation

- **A13** · Persistently managed information has governed state. Native state and a durable Mnemosyne overlay must give equivalent lifecycle behaviour. External legal-hold administration needs a binding that honours the accepted hold-recognition and hold-respect obligations. [A13][a13]
- **A14** · DELETE sets Deleted. RETIRE remains a distinct governed operation, and physical disposal belongs to an external offline process. The accepted `GovernedWriter` names RETIRE and the Summary contract is limited to CREATE/RETIRE. The application binding and scope of the new logical DELETE require disposition. The accepted Business IU categorically protects asserted or durably recorded information from physical destruction. Any overlap with external offline disposal requires explicit disposition. [A14][a14]
- **A15** · PersistenceService determines optional Data Version independently of Managed Revision. The accepted IU `version` and version-based interaction terms denote authoritative Managed Revision, so their wording and mappings need reconciliation while preserving current authority, history and concurrency. [A15][a15]
- **A16** · Synchronous transactions and factual Outcomes align with the accepted five-contract interaction and its durability requirements. Components own processing status and recovery. Required façade projections keep their operation-specific meaning. [A16][a16]

The outstanding design and verification cover native and overlay state parity, hold enforcement and state-aware visibility, the DELETE path and revision scope, provider Data Version mappings, expected-revision comparisons and active generations, and durability evidence, uncertain completion, recovery and authorised operational views. The draft identifies the Continuum descriptions needing later governed amendment and the matters for Solution Architect disposition. HCRL.004 DR-3, DR-5 and DR-8 retain their pending software-review standing. [Draft review matters][review] · [HCRL.004][hcrl]

## Verification and Git state

- **PASS** · Checked 114 document-local links and fragments across the draft, issue groups and Task. Confirmed six preserved issue-group sections, 14 unchanged finding rows and the four adjudicated G06 entries. Checked draft and issue formatting and `git diff --check`.
- **PASS** · The bounded offline ArchUnit reactor completed in **29.549 seconds** on OpenJDK 21.0.13.1 and Maven 3.9.12. **90 passed**, with zero failures, errors or skips. This run exercises the existing software snapshot. G06 implementation and runtime conformance remain future verification obligations.
- **UNRESOLVED** · The Task's original instruction-entry timestamp was unavailable. Its event and filename prefix require Solution Architect treatment before closure. The whole-iteration checker exited 1 at its corresponding-HCRL prerequisite because HCRL.005 is reserved for a later instruction. A scoped check verified the new evidence placement and the Task's explicit unresolved event field. [Task][task]
- **Git scope** · The worktree changes for this task comprise the new working draft, the G06 issue-group update and the formal Task/Report. Entry-state hashes, Git diff and ref/index checks preserve existing accepted DRPs, the accepted Continuum, the release file, Topic/TopicMask Draft.001.003, HCRL.004, implementation and unrelated worktree changes. HEAD remains at Snapshot-004. The draft is pending Solution Architect review.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-calliope-g06-lifecycle-revision-and-operational-state-task.md
[draft]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md
[groups]: ../Calliope-Architectural-Issue-Groups-005.md#g06--lifecycle-authoritative-revisions-and-operational-metadata
[a13]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md#1-a13--governed-information-lifecycle-and-data-state
[a14]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md#2-a14--logical-delete-and-distinct-retire
[a15]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md#3-a15--data-version-and-managed-revision
[a16]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md#4-a16--synchronous-transactions-outcomes-and-operational-ownership
[review]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md#5-matters-for-solution-architect-review
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
