Report-Produced-At: 2026-11-20T04:03:36.660144Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Report — A04 InformationUnit identity and ConceptFDN clarification

The [operative clarification][task] commissioned a draft Design Note correction and Iteration 005 evidence. The source was `feature/BL-CALLIOPE-OO-01` at `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d` (`Software-Version-0.1.0-Snapshot-004`) and Architecture 1.1.26. [HCRL.004 DR-1][hcrl] links the reviewed software and historical BL-CALLIOPE-OO-01 revision 1.

The revised §7.2 records the independently assigned IU UUID as durable identity and states that an IU requires no InstanceFDN. The shared ConceptFDN `solution=harmonia.domain=internal.concept=InformationUnit` identifies the concept. `InformationRepresentation` retains responsibility for describing the payload and its representation. The note identifies the historical spelling as an IU Topic designation. The corresponding §11 bullet refers to the semantic distinction after removal of the illustrative `informationProfile` field. G02 records the clarification and links to the original §7.2 at the exact snapshot commit. [Revised Design Note][note] · [G02 evidence][g02]

The clarification is consistent with the accepted durable IU UUID. IU-06 and Final.002 still require the IU Topic designation, and the accepted IU Class and Final.002 give that Topic its own UUID. F-01.78 preserves the designation as the redesign's unqualified projection. A shared concept path cannot substitute for a per-IU designation. Removing or changing the accepted IU Topic's role, or substituting the ConceptFDN for the designation, would require Solution Architect adjudication under AX-14 and AX-17. The qualified per-IU Topic, boundary mappings, and historical Topic UUID and alias obligations remain open. G03 and G04 still need evidenced selector and reference mappings. [IU-06][iu-original] · [IU Class][iu] · [Final.002 §2.1][iu-final] · [F-01.78][f004] · [G02 evidence][g02]

## Verification

- **PASS** · Checked the six decisions against the revised paragraphs, the original §7.2 against the snapshot, and 84 Markdown paths and fragments across the Design Note, G02 evidence and Task. Verified the tracked diff is limited to the Design Note and the index is unchanged. Within the pre-existing untracked set, only the G02 reconciliation was updated. HCRL.005 is absent. The new Task and this Report document the draft change.
- **PASS** · The offline ArchUnit run on Maven 3.9.12 and OpenJDK 21.0.13.1 reported 90 tests with zero failures, errors or skips across the 36-module dependency reactor in 30.450 seconds.
- **UNRESOLVED** · The original instruction-entry timestamp was unavailable. The Task retains the issued wording and unresolved prefix for Solution Architect treatment before closure. Whole-iteration checking requires a corresponding HCRL.005 and is reserved for that stage.

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  timeout --signal=TERM --kill-after=10s 120s mvn -B -o -ntp -Dstyle.color=never test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

[task]: ../../before/ai-agent-tasks/unresolved-calliope-a04-informationunit-conceptfdn-clarification-task.md
[note]: ../../../../../../../docs/design/DesignNote-ConceptFDN-and-InstanceFDN.md#72-informationunit
[g02]: ../Calliope-G02-Architectural-Reconciliation-005.md#solution-architect-clarification--informationunit-identity-and-conceptfdn
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-1-whole-of-calliope-scope-and-candidate
[iu-original]: ../../../../1.1.22/001/before/accepted-input/DRP-Information-Unit-Framework.Final.001.md#3-iu-identity-atomicity-and-management-boundary
[iu]: ../../../../../../continuum/application/data-object/class/74ec609de1ae498fbf9d13e4ef8c6cbe.md#architectural-content
[iu-final]: ../../../../../offline-design/DRP-Information-Unit-Framework.Final.002.md#21-identity
[f004]: ../../../004/before/F-01-Topic-TopicMask-Adjudications-Continuation-004.md#f-0178--r3-information-unit-topic-designation--accepted
