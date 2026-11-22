Report-Produced-At: 2026-11-20T23:09:12.978433Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — G03 Mneme Pragma Model reconciliation · Iteration 005

The [G03 Task][task] requested reconciliation from [Pragma Model Draft.002][previous] against [publication/subscription Draft.000.002][publication], [Pragmaton Draft.000.003][pragmaton] and bounded Continuum/Design Note sources. Work started on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d)), with Architecture 1.1.26 and existing worktree changes.

## Result and open decisions

- Created [Mneme Pragma Model Draft.000.003][candidate], retaining the ownership, evidence, progression and output distinctions of Draft.002 and correcting numbering under §4. The draft incorporates the work definition, archetypal Pragma and subscriber-specific Pragmatosis roles, exact IU input/output cardinalities, immutable summaries and the Pragmaton/WorkSpace relationship.
- Recorded the instructed `DoNotBefore` requirement as the minimum earliest-permitted execution constraint under existing scheduling responsibility. No named field contract was located in the bounded source search. Canonical placement, boundary decision ownership and recovery evidence require Solution Architect review. Initiating publication, realisation and physical handoff remain separate concerns.
- Retained canonical work/outcome correspondence, historical input cardinality, minimum lifecycle and lineage, result attribution and specialised naming for adjudication. The [reconciliation evidence][evidence] records proposed follow-on corrections for Petasos–Mneme, Ergon–Mneme, Pragmaton and publication/subscription. Those proposals leave the existing companion drafts intact.
- Extended [DR-12][review] with the G03 candidate and requested dispositions, preserving DR-1 through DR-11 and prior DR-12 obligations. G03 A06/A07 and the related selector and migration questions remain open. The Pragma Model draft and HCRL await Solution Architect review.

## Verification and limits

- **PASS** · Checked the numbered DRP sections and semantic coverage, 119 local link targets and fragments before Report emission, the unchanged DR-1–DR-11 and prior DR-12 body, and the bounded worktree delta. The candidate SHA-256 is `f392ca86c4c679c0248475a2dc0b5c43ca433cceb32a01166fe76cd69ba48971`.
- **PASS** · The offline ArchUnit reactor with Java 21 completed in Maven-reported **28.017 s**, exit 0. All **90 tests** passed with zero failures, errors or skips. These results apply to the current software and establish no runtime conformance to this candidate design.
- **FAIL** · `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 005` exited 1 with `No corresponding HCRL for software-0.1.0-005`. HCRL.004 contains the cumulative review. The layout checker needs the reserved corresponding HCRL.005 to validate the whole iteration.
- **UNRESOLVED** · The system-recorded instruction-entry time was unavailable. The exact Task wording, unresolved timestamp field and filename prefix are retained for Solution Architect treatment before closure.
- **PASS** · Git HEAD, index and refs match the entry state. The delta is confined to the new draft, G03 evidence, formal Task/Report and DR-12 extension. Earlier draft revisions, other DRPs, application code and accepted architecture were preserved.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-g03-mneme-pragma-model-reconciliation-task.md
[previous]: ../../../../../offline-design/DRP-Mneme-Pragma-Model.draft.002.md
[candidate]: ../../../../../offline-design/DRP-Mneme-Pragma-Model.draft.000.003.md
[publication]: ../../../../../offline-design/DRP-Pragma-Publication-and-Subscription.draft.000.002.md
[pragmaton]: ../../../../../offline-design/DRP-Mneme-Pragmaton-Model.draft.000.003.md
[evidence]: ../G03-Mneme-Pragma-Model-Reconciliation-005.md
[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
