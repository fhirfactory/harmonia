Report-Produced-At: 2026-11-20T22:55:48.433072Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — Pragmaton and publication/subscription DRP reconciliation · Iteration 005

The [follow-up Task][task] supplied the Solution Architect's design clarification and the [Pragmaton Draft.000.003 candidate][pragmaton] for comparison with the [original publication/subscription draft][original-draft] and [Ergon–Mneme interaction Draft.002][interaction]. The originating branch was `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with Architecture 1.1.26 and existing worktree changes. The existing software candidate and backlog correspondence remain recorded in [HCRL.004][hcrl].

## Changes and standing

- Created [publication/subscription Draft.000.002][draft], preserving PS-01–PS-16 and the original draft. It treats Pragmaton as the Mneme-managed execution-information environment and `Ergon.WorkSpace` as the Ergon-facing access abstraction. It references the candidate for the JavaSpaces analogy and preserves Themis-governed lazy access, immutable content-free IU snapshots, Workflow Cache/Standard Cache boundaries and authoritative persistence.
- Updated the [Iteration 005 reconciliation][reconciliation] in place and retained its [byte-identical original][original-evidence]. The updated evidence distinguishes agreement in the draft designs, historical correspondence, software-level choices and unresolved architectural contracts.
- Proposed the [Ergon–Mneme interaction amendments][amendments] in the updated evidence. They refine shared initial contextualisation, ProcessingEngine use of WorkSpace, the adapter's retrieval mediation, the existing Workflow Cache allocation, Themis controls, and governed return of working changes apart from new Pragma Output. The source interaction and the Solution Architect's Pragmaton candidate were preserved without edits.
- Extended [HCRL.004 / DR-12][review] to review the two candidates and interaction amendments. DR-1 through DR-11 are preserved byte for byte. DR-12 retains its obligations and pending disposition.

## Remaining decisions

The conceptual WorkSpace relationship is agreed at design level. The Solution Architect's remaining review concerns exact candidate acceptance and Continuum correspondence, formal Pragmaton association and lifecycle, acquisition and authority-change outcomes, and governed save/unload outcomes. The updated evidence records where historical Context Load/Save and ad hoc access wording require alignment and retains the accepted embedded LOCAL Workflow Cache allocation and MC-01–MC-07 qualifications. Implementation choices such as API shape and indexing remain solution design. The original publication, task/outcome, handoff/selection, SummaryCache, specialisation and Assurance matters carry forward under DR-12. [Outstanding decisions][decisions] · [DR-12][review]

## Verification and limits

- **PASS** · Verified all 16 PS decisions in order and checked 194 local link targets and fragments before report emission. Compared DR-1–DR-11, prior DR-12 obligations, prior evidence bytes, and source file digests against the entry state. Publication/subscription Draft.000.002 SHA-256 `7c11996566444cd2f4a721395d35bb29387c94f70c91e4e8875d7912f2f60b58`. Pragmaton Draft.000.003 SHA-256 `b7b5af312317cc76904e707468f300035bb6c0eb68d2c82c76ce88f46f789d19`.
- **PASS** · Bounded offline ArchUnit reactor with Java 21 completed in Maven-reported **28.365 s**, exit 0. All **90 tests** passed with zero failures, errors or skips. This verifies the existing software invariants. Runtime conformance to the candidate design remains future evidence.
- **FAIL** · `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 005` exited 1 with `No corresponding HCRL for software-0.1.0-005`. The cumulative pending HCRL.004 references the originating Iteration 005 evidence. The whole-iteration layout check requires the reserved matching HCRL.005.
- **UNRESOLVED** · The system-recorded instruction-entry time was unavailable. The Task preserves the exact operative instruction with the unresolved timestamp and filename prefix for Solution Architect treatment before closure. [Task][task]
- **PASS** · Git HEAD, index and refs match the entry state. The worktree delta consists of the new draft and Task/Report, updated evidence and its preserved earlier version, and the DR-12 extension. Architecture 1.1.26 remains the accepted baseline.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-pragmaton-publication-subscription-reconciliation-task.md
[pragmaton]: ../../../../../offline-design/DRP-Mneme-Pragmaton-Model.draft.000.003.md
[original-draft]: ../../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.001.md
[draft]: ../../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.002.md
[interaction]: ../../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md
[original-evidence]: ../Pragma-Publication-and-Subscription-Reconciliation-005.draft-000.001.md
[reconciliation]: ../Pragma-Publication-and-Subscription-Reconciliation-005.md
[amendments]: ../Pragma-Publication-and-Subscription-Reconciliation-005.md#proposed-ergonmneme-interaction-amendments
[decisions]: ../Pragma-Publication-and-Subscription-Reconciliation-005.md#outstanding-architectural-decisions
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
