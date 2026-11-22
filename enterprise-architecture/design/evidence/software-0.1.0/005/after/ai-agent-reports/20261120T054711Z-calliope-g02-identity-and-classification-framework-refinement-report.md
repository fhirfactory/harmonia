Report-Produced-At: 2026-11-20T05:47:11.621151Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — Calliope iteration 005 G02 framework refinement

Recorded the [Solution Architect's clarification][task] in [working Draft.000.002][draft]. The originating branch was `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`), with Architecture Continuum 1.1.26. HCRL.004 identifies the separately reviewed implementation and historical [BL-CALLIOPE-OO-01 revision 1][backlog]. [Predecessor Draft.000.001][prior-draft] · [HCRL.004][hcrl]

The revision confirms Topic as a reusable datatype with structural value identity and no independently assigned ID or UUID. The IU retains its immutable UUID and ConceptFDN, and can produce a derived Topic value where required. TopicMask and the 81 F-01 adjudications retain their settled semantics. The A05 framework text is preserved. Updated the [G02 reconciliation][g02] and [issue inventory][groups] with the clarification and its review status.

## Outstanding obligations

- Reconcile the 1.1.26 Topic, TopicMask and IU Class definitions through ADD/HCRL. Record Topic value semantics, the IU profile and derived value, and the previously adjudicated structural mask semantics while preserving governed element identities and relationships. [Draft reconciliation][treatment]
- Establish the independently governed historical reference corpus and demonstrate correspondence for in-scope UUIDs, aliases and legacy bindings outside the redesigned datatypes. Exact IU qualifiers, compressed encoding and boundary mappings remain detailed design. G03 still requires no-widening proof for selector migration. [G02 reconciliation][g02] · [Issue inventory][groups]
- Allocate value-set ownership and specify membership, extensions and conversions under the established authorities. [A05 evidence][a05]

## Verification

- **PASS** · Checked 118 unique local link targets and fragments across the draft, two updated evidence files and Task. Confirmed that Draft.000.001, Topic/TopicMask Draft.001.003, G01 and G03–G07, existing implementation, accepted Continuum, HCRL.004, release, HEAD, refs and index were preserved. Draft.000.002 SHA-256 is `946e869ba1aa98338d6bd0ba31f0a590a6a6037eb2757e4dc06e0de9a6241355`.
- **PASS** · The bounded ArchUnit run on OpenJDK 21.0.13.1 and Maven 3.9.12 completed in 28.937 seconds. All 90 tests passed with zero failures, errors or skips against the existing implementation.
- **UNRESOLVED** · The refinement instruction-entry timestamp was unavailable. The formal Task preserves the issued wording and unresolved filename prefix for Solution Architect treatment before closure. The whole-iteration evidence check requires the reserved HCRL.005. [Task][task]
- **UNRESOLVED** · The pre-existing Topic and TopicMask retained-input locators still point to the absent suffixed filename. The separately retained input and offline source match the recorded hash. [Evidence limits][limits]

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

**Checkpoint** · Draft.000.002 remains a working draft for Solution Architect review. The draft and the Iteration 005 evidence updates are in the working tree.

[task]: ../../before/ai-agent-tasks/unresolved-calliope-g02-identity-and-classification-framework-refinement-task.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[backlog]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[prior-draft]: ../../../../../offline-design/DRP-Calliope-Identity-and-Classification-Framework.draft.000.001.md
[draft]: ../../../../../offline-design/DRP-Calliope-Identity-and-Classification-Framework.draft.000.002.md
[treatment]: ../../../../../offline-design/DRP-Calliope-Identity-and-Classification-Framework.draft.000.002.md#4-reconciliation-with-architecture-continuum-1126
[g02]: ../Calliope-G02-Architectural-Reconciliation-005.md
[groups]: ../Calliope-Architectural-Issue-Groups-005.md
[a05]: ../Calliope-G02-Architectural-Reconciliation-005.md#a05--classification-vocabulary-ownership
[limits]: ../Calliope-G02-Architectural-Reconciliation-005.md#evidence-limits
