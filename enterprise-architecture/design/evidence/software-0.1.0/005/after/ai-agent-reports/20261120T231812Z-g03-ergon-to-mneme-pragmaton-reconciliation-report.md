Report-Produced-At: 2026-11-20T23:18:12.599438Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — G03 Ergon-to-Mneme Pragmaton interaction reconciliation · Iteration 005

The [G03 interaction Task][task] requested a candidate revision based on [Ergon–Mneme Draft.002][previous], the Pragma-family candidates and accepted cache, access, persistence and authorisation contracts. The starting worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with Architecture 1.1.26 and existing changes.

## Result and remaining decisions

- Created [Ergon–Mneme Interaction Draft.000.003][candidate] with corrected heading and subsection numbering. It documents ContextLoader and Mneme collaboration for a particular realisation, WorkSpace access to the Pragmaton, the three governed access cases, adapter responsibilities, and continued observance of accepted Workflow Cache, authoritative revision and materialisation contracts. WorkSpace replaces the former open whole-envelope-or-view question at design level.
- The draft preserves Themis control of initial and subsequent access, distinguishable denial, unavailability and indeterminate outcomes, and the separate return of working changes, new Pragma Outputs and execution outcomes. It retains the existing execution recovery boundary and separately attributes save, authoritative persistence and Petasos delivery outcomes.
- The [reconciliation evidence][evidence] identifies the remaining Load/Save, Ergo/Ergon and adapter correspondence, acquisition and policy-change outcomes, required version/checkpoint evidence, and the formal association and lifecycle and save/unload contracts. The [downstream correction][downstream] calls for Petasos–Mneme to resolve the pre-established realisation and its lineage on receipt, with working context rebuilt through governed execution or recovery as required.
- Extended [DR-12][review] for the exact candidate and its review dependencies. Earlier DR-12 obligations and DR-1–DR-11 are preserved. The source Draft.002 and companion DRPs retain their existing bytes. The candidate and HCRL remain pending.

## Verification and limits

- **PASS** · Checked section numbering and required semantic coverage, 110 local link targets and fragments before Report emission, exact preservation of DR-1–DR-11 and prior DR-12 body, and preservation of existing worktree files outside the authorised DR-12 edit. Candidate SHA-256 `ea9f2008655f833ca5732d5a71c6ca73baa57d3e4d5beff642a848029bad36ee`.
- **PASS** · The bounded offline ArchUnit reactor with Java 21 completed in Maven-reported **28.710 s**, exit 0. All **90 tests** passed with zero failures, errors or skips. This verifies the existing software invariants. Candidate runtime conformance remains future evidence.
- **FAIL** · `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 005` exited 1 with `No corresponding HCRL for software-0.1.0-005`. The cumulative pending HCRL.004 refers to the originating Iteration 005 evidence. The whole-iteration layout check requires the reserved matching HCRL.005.
- **UNRESOLVED** · The system-recorded instruction-entry time was unavailable. The exact Task wording, unresolved timestamp field and filename prefix are retained for Solution Architect treatment before closure.
- **PASS** · Git HEAD, index and refs match the entry state. The delta is confined to the new draft, bounded reconciliation, formal Task/Report and DR-12 extension. Architecture 1.1.26 remains the accepted baseline.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-g03-ergon-to-mneme-pragmaton-reconciliation-task.md
[previous]: ../../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md
[candidate]: ../../../../../offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.000.003.md
[evidence]: ../G03-Ergon-to-Mneme-Pragmaton-Reconciliation-005.md
[downstream]: ../G03-Ergon-to-Mneme-Pragmaton-Reconciliation-005.md#downstream-corrections-for-review
[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
