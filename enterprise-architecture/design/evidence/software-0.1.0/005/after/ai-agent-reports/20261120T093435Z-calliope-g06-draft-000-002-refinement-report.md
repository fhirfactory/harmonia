Report-Produced-At: 2026-11-20T09:34:35.415820Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — Calliope iteration 005 G06 Draft.000.002 refinement

[Draft.000.002][draft] applies the [Solution Architect's clarification][task] to [Draft.000.001][prior-draft] on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), against Architecture Continuum 1.1.26. The [Iteration 005 evidence][groups] records the revised A13/A14 dispositions and carries forward A15/A16 and the other issue groups.

## Decisions incorporated into the draft

- **A13** · Harmonia manages logical lifecycle state and recognises and respects externally established legal holds. Legal-hold authority and administration are SEP. An authorised database administrator performs physical PURGE externally and offline after applicable offline archival, subject to retention legislation, policy and holds. Harmonia exposes no PURGE capability. [A13][a13]
- **A14** · Logical DELETE sets Deleted within the existing service boundary. RETIRE retains its separately governed purpose. Neither operation confers physical destruction authority. The Business IU prohibition applies to Harmonia-managed operations. The clarification resolves the scope conflict raised in Draft.000.001. The accepted 1.1.26 wording still requires governed amendment. [A14][a14]
- **A15/A16** · Both draft sections are byte-identical to Draft.000.001, preserving Data Version and Managed Revision, applicable durability, synchronous Outcomes, and component-owned status and recovery. The Active, Deleted, Archived and Retired rules are also byte-identical. [Draft.000.002][draft]

## Remaining obligations

Specify the DELETE binding and affected IU/content or revision scope within the existing contracts, the receipt and validation of external hold restrictions, and native/overlay behaviour. Carry the clarified disposal and hold boundaries and logical DELETE into the applicable IU, persistence and service contract definitions with traceability to the existing hold and retention Features. Later verification covers lifecycle visibility and retention, revision comparisons and active generations, authoritative durability and uncertainty. The bounded reconciliation identified no remaining architectural contradiction. [Remaining obligations][remaining] · [HCRL.004][hcrl]

## Verification and provenance

- **PASS** · Checked 118 document-local links and fragments before report emission. Verified preserved A15/A16 and lifecycle rules, G01/G02 acceptance, G03–G05/G07 standing, Draft.000.001, and all finding rows outside A13/A14. Protected paths, branch refs and index matched the entry state. The worktree edits for this refinement are limited to Draft.000.002 and the Iteration 005 evidence and provenance. The draft is pending Solution Architect review.
- **PASS** · The bounded offline ArchUnit reactor completed in **30.419 seconds**, with **90 tests passed**, zero failures, errors or skips. This checks the existing Snapshot-004 implementation. G06 runtime and conformance checks remain future obligations.
- **UNRESOLVED** · The refinement instruction's original entry timestamp was unavailable. Its event field and filename prefix record that limitation for treatment before closure. The whole-iteration evidence checker requires the reserved HCRL.005. [Refinement Task][task]

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-calliope-g06-draft-000-002-refinement-task.md
[prior-draft]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md
[draft]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.002.md
[groups]: ../Calliope-Architectural-Issue-Groups-005.md#g06--lifecycle-authoritative-revisions-and-operational-metadata
[a13]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.002.md#1-a13--governed-information-lifecycle-and-data-state
[a14]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.002.md#2-a14--logical-delete-and-distinct-retire
[remaining]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.002.md#5-remaining-design-incorporation-and-verification
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
