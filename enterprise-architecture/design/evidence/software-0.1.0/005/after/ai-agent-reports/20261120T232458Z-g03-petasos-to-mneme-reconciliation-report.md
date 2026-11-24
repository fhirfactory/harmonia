Report-Produced-At: 2026-11-20T23:24:58.385886Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — G03 Petasos-to-Mneme interaction reconciliation · Iteration 005

The [G03 Task][task] requested revision of [Petasos–Mneme Draft.002][previous] against the Pragma-family candidates and the accepted or qualified handoff, information, security and recovery contracts. The starting worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with Architecture 1.1.26 and existing changes.

## Result and remaining decisions

- Created [Petasos–Mneme Draft.000.003][candidate] with the agreed eight-step handoff and corrected subsection numbering. Mneme establishes one authoritative, recoverable realisation per matching subscription. A by-value PetasosParcel references it. Receipt resolves that existing identity. ContextLoader and Mneme establish or reconstruct working information when governed execution requires it.
- Added minimum acknowledgement and evidence semantics and separate treatment for undertaking persistence, durable Petasos acceptance, delivery, execution, Mneme save and authoritative IU persistence. The draft covers same-subscriber matches, partial fan-out, uncertain outcomes, repeated handoff, duplicate delivery, consumer failure, Ergon failure and `DoNotBefore`. It preserves execution identity and separates duplicate recognition from Ergon idempotence.
- The [reconciliation evidence][evidence] records the qualified PetasosParcel difference in reference target and responsibility for establishing the undertaking. It preserves the general ADR-018 work-acceptance boundary and identifies follow-on Continuum and DRP correspondence. Canonical referent, acknowledgement ownership, minimum recoverable evidence, retry equivalence, time evidence and trusted metadata remain for Solution Architect adjudication.
- Extended [DR-12][review] for the candidate and discrepancy with all prior review obligations preserved. Prior revisions and other DRPs retain their original bytes. The candidate and HCRL remain pending.

## Verification and limits

- **PASS** · Verified required sequence and semantic markers, section numbering, 126 local link targets and fragments before Report emission, byte identity of DR-1–DR-11 and the earlier DR-12 body, and preservation of existing worktree paths outside the authorised DR-12 edit. Candidate SHA-256 `e579d7600f42bd9563d556bbb30f22c21277a9c90208dfb9a83d216ea30670da`.
- **PASS** · The bounded offline ArchUnit reactor with Java 21 completed in Maven-reported **27.537 s**, exit 0. All **90 tests** passed with zero failures, errors or skips. The results verify the existing software. Candidate runtime conformance remains future evidence.
- **FAIL** · `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 005` exited 1 with `No corresponding HCRL for software-0.1.0-005`. Cumulative pending HCRL.004 refers to the originating Iteration 005 evidence. Whole-iteration validation requires the reserved HCRL.005.
- **UNRESOLVED** · The system-recorded instruction-entry time was unavailable. The exact instruction, unresolved timestamp and filename prefix are retained for Solution Architect treatment before closure. [G03 Task][task]
- **PASS** · Git HEAD, index and refs match entry state. The worktree delta is confined to the new draft, reconciliation evidence, Task/Report and DR-12 extension. Architecture 1.1.26 remains the accepted baseline.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-g03-petasos-to-mneme-reconciliation-task.md
[previous]: ../../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md
[candidate]: ../../../../../offline-design/DRP-Interaction-Petasos-to-Mneme.draft.000.003.md
[evidence]: ../G03-Petasos-to-Mneme-Reconciliation-005.md
[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
