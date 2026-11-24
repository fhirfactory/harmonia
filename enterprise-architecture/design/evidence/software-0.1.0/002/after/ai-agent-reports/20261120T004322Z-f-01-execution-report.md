Report-Produced-At: 2026-11-20T00:43:22.113205Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# F-01 execution report — Topic and TopicMask design consolidation

- **Outcome** · Documentation candidate prepared for Solution Architect review
- **Evidence boundary** · `software-0.1.0/002`, as assigned in the [operative Task][task]
- **Branch and source state** · `feature/BL-CALLIOPE-OO-01`, HEAD `fd52408e3d8be433bb43eedd2a448354646d7518`, Architecture `1.1.26`
- **Backlog correspondence** · [BL-CALLIOPE-OO-01 revision 1][backlog] (`3c93558550da6a140a8668e120638efb8e9a368d`). [Item 00001 revision 001][wider-backlog] (`88c21095fcabd2909fa4d42e661358b08147e0fe`) supplies context for the wider Calliope candidate already recorded in the pending [software iteration 001 review][hcrl001]. This assignment covers F-01 design preparation.

## Result

The [F-01 adjudications][f01] provided the accepted discussion decisions. Reviewed and completed two worktree documents already present at entry, [Draft.002][draft] and the [decision delta][delta]. Draft.002 covers F-01.1–F-01.44, gives proposed Java signatures and specifies mandatory future behavioural evidence. The delta compares Draft.002 with the historical [Final.001][final]. The [architecture reconciliation][architecture] records the accepted Continuum baseline, material conflicts, consumer dependencies and outstanding decisions. The [implementation gap analysis][gaps] identifies observed Java and interchange contracts, departures from the new design, affected consumers and missing verification.

The matching grammar, explicit universal encoding, display and accessor edges are proposals for review. Release limits, RE2/J capability and complexity evidence, application dependency allocation, and logging under backpressure remain open. Current Topic and TopicMask UUID, sequencing and selector-default semantics were confirmed through the completed [RAD 1.1.25 DR-3][dr3]. The disposition sought covers historical identity and alias treatment, IU naming and qualified mapping, compatibility of held masks, dependency allocation and diagnostic delivery. The targeted source review establishes the inspected contracts and representative callers. Deployed configuration, retained external payloads and adapter compatibility remain unverified. [Draft qualifications][open] · [Reconciliation findings][architecture]

## Verification

| Check | Outcome |
| --- | --- |
| Compare F-01 identifiers and ranges with the adjudication record and review cross-document correspondence. Check local Markdown links and anchors and the draft's worked regex-escaping example. | **PASS.** All 44 decisions mapped. Local link targets and anchors in the draft and supporting documents resolve. The proposed worked example encodes the stated raw expressions. This check supplied no RE2/J parser or runtime proof. |
| Compare source hashes with the initial observed bytes and review Git state. | **PASS.** The supplied adjudication record, Final.001 and existing formal Task matched their entry hashes. Git inspection found no changes to Java, tests, POMs, the Continuum, HCRLs or release metadata. Existing worktree edits outside the deliverables were present at entry. |
| Maven ArchUnit reactor, scope specified below. | **PASS.** Java 21.0.13.1, Maven 3.9.12, offline. 90 tests, zero failures, errors or skips, 36 reactor projects, 31.237 s. The initial Java 25.0.5.1 run also completed 90 tests, with ArchUnit class-file major version 69 warnings and fallback imports for JDK classes. The Java 21 rerun against the repository compiler release completed without those warnings. |
| Focused existing Calliope tests, scope specified below. | **PASS.** Java 25.0.5.1, Maven 3.9.12, offline. 61 tests, zero failures, errors or skips, 2.784 s. These tests characterise the existing candidate. |
| Iteration evidence checker, `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 002`. | **FAIL (checker prerequisite).** `No corresponding HCRL for software-0.1.0-002` (exit 1). The explicit evidence allocation is 002 and the existing software HCRL is 001. The Task excludes HCRL creation or advancement. The historical Task issuance time and its filename prefix also remain **UNRESOLVED** and require Solution Architect treatment before closure. |
| F-01 behavioural implementation, integrated solution regression, RE2/J runtime proof and logging failure evidence. | **UNRESOLVED for any future implementation candidate.** Existing baseline checks were executed. The new requirements in [Draft §10][acceptance] remain a future implementation acceptance gate. |

Maven invocations, with command-level bounds of 240 seconds for each ArchUnit run and 120 seconds for the focused run:

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn -o -B -ntp test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
mvn -o -B -ntp -pl calliope -am test \
  '-Dtest=TopicTest,TopicMaskTest,TopicBuilderTest,TopicFactoryTest,TopicSubscriptionTest,InformationUnitSerializationTest,InformationUnitArchitecturalConformanceTest,InformationSecurityContextTest,OperationalMetadataTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
```

## Review boundary and resulting state

Draft.002 and the iteration-002 Markdown evidence are working-tree documents pending Solution Architect review. No accepted architectural or software transition occurred. No source, test, accepted Continuum, HCRL or release state was changed, and no ADD, publication, commit or push took place. Review of the exact draft and explicit disposition of the related architecture findings are the next human checkpoint. The existing pending candidate under software iteration 001 retains its recorded standing. [Architecture reconciliation][architecture] · [Pending software review][hcrl001]

[task]: ../../before/ai-agent-tasks/unresolved-f-01-design-consolidation-task.md
[f01]: ../../before/F-01-Topic-TopicMask-Adjudications.md
[draft]: ../../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.001.md
[open]: ../../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.001.md#9-open-decisions-and-evidence
[acceptance]: ../../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.001.md#10-mandatory-behavioural-acceptance
[final]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.000.md
[delta]: ../F-01-Decision-Delta.md
[architecture]: ../F-01-Architecture-Reconciliation.md
[gaps]: ../F-01-Implementation-Gap-Analysis.md
[dr3]: ../../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3
[hcrl001]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md
[backlog]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[wider-backlog]: ../../../../../../../docs/backlog/BacklogItem-0001-Calliope-Model-Restructure.001.md
