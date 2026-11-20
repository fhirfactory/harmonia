<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# F-01 execution report — iteration 003

- **Outcome** · Documentation package prepared for Solution Architect review
- **Boundary** · `software-0.1.0/003`, [operative Task][task]
- **Originating state** · `feature/BL-CALLIOPE-OO-01`, HEAD `fd52408e3d8be433bb43eedd2a448354646d7518`, Architecture 1.1.26
- **Backlog correspondence** · [BL-CALLIOPE-OO-01 revision 1][backlog], Git blob `3c93558550da6a140a8668e120638efb8e9a368d`

## Result

[Draft.001.002][draft] is a self-contained successor draft to the historical [Final.001.000][final]. It incorporates the original F-01.1–F-01.44 requirements subject to the continuation, maps all F-01.45–F-01.73 adjudications and follows the F-01.74 consolidation disposition. In particular, null and empty qualifiers generate `N<position>` at assembly, values are mandatory and never generated, ordinary mask expressions round-trip through two escaping layers, and universal masks carry a separate logical kind. The [decision delta][delta] identifies the superseded interpretation and other refinements. The [gap analysis][gaps] records the existing code and consumer exceptions, and [Draft §10][acceptance] specifies future JUnit evidence.

The review compared both adjudication records, the two predecessor DRPs and the iteration-002 delta, reconciliation and implementation gaps. It consulted the repository governance and relevant canonical Principles, the exact RAD 1.1.25 Topic/TopicMask dispositions and bounded excerpts on IU designation, naming, dependencies and logging. Java inspection covered current Topic/TopicMask constructors and factories, builders, representative tests, IU and routing callers, and Maven declarations. The existing branch's deployment and stored-input population remain unverified.

## Exceptions for review

| Matter | Outcome |
| --- | --- |
| **Architectural reconciliation** | R1–R4 remain open for runtime and governed identity, legacy UUIDs/references and registry rules, mask migration, IU Topic designation and architectural naming. R5–R6 retain Calliope dependency placement and application logging under backpressure. [Architecture reconciliation][reconciliation] |
| **Draft-level detail** | The draft proposes rejecting textual empty versions in strict Topic input, raw and escaped name projections as specified, Java field ordering with `String.compareTo`, navigation edge behaviour, ordinary-only mask expression and containment accessors and descriptive universal display, and an absent exit index before any field evaluation. These details require review. [Draft §9][open] |
| **Runtime and migration evidence** | Release-defined numerical limits, pinned RE2/J safeguards, adapter encoding and compatibility, and log saturation and recovery still require evidence. The new F-01 acceptance cases remain future implementation requirements. [Draft §10][acceptance] · [Implementation gaps][gaps] |
| **Formal Task time** | **UNRESOLVED.** The interaction exposed no attributable instruction-entry timestamp. The Task retains the exact operative wording, with an unresolved timestamp and filename prefix for Solution Architect treatment before closure. [Task][task] |
| **Iteration-level governance checker** | **FAIL (prerequisite).** `python3 enterprise-architecture/tooling/architecture_review_evidence.py --version software-0.1.0 --iteration 003` returned exit 1 with `No corresponding HCRL for software-0.1.0-003`. The existing pending software HCRL is iteration 001. This assignment establishes the 003 documentation and evidence boundary without initiating an HCRL. [Existing pending HCRL][hcrl] |

## Verification and resulting state

| Check | Outcome |
| --- | --- |
| Adjudication coverage and Markdown consistency | **PASS.** Mapped all 44 original entries, all 29 continuation adjudications and F-01.74. Checked local links and anchors, worked Topic and mask text examples, and ordinary mask outer-layer decoding for absent, empty and wildcard expressions. This check covers the document grammar and examples. Actual RE2/J compilation and runtime matching remain future evidence. |
| Historical preservation and scope | **PASS.** All iteration-002 files, the two predecessor DRPs and the supplied iteration-003 continuation matched entry SHA-256 hashes. The pre-existing tracked worktree diff and Git index diff matched their entry snapshots. Repository-wide whitespace diagnostics identified only pre-existing tracked edits. |
| Existing ArchUnit reactor | **PASS.** 90 tests, zero failures, errors or skips, Java 21.0.13.1 and Maven 3.9.12 offline, Maven total 29.804 seconds. These checks characterize the inspected branch. |
| F-01 implementation acceptance | **Future.** The design specifies the required evidence. No implementation candidate was submitted. |

The baseline verification command had a 240-second bound.

~~~bash
timeout 240s env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn -o -B -ntp test \
  -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' \
  -Dsurefire.failIfNoSpecifiedTests=false
~~~

The four named outputs are retained under iteration 003 `after/`, with the formal Task and Report in the governed provenance directories. The draft and iteration evidence are untracked working-tree additions. Java, tests, Maven configuration, accepted architecture, HCRLs and release metadata were outside the change set. No ADD, publication, commit or push occurred. The next checkpoint is Solution Architect review of the exact draft and disposition of its open matters. [Formal Report][formal]

[task]: ../before/ai-agent-tasks/unresolved-f-01-design-consolidation-task.md
[backlog]: ../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[final]: ../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.000.md
[draft]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md
[open]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#9-open-decisions-and-evidence
[acceptance]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md#10-mandatory-behavioural-acceptance
[delta]: F-01-Decision-Delta.md
[gaps]: F-01-Implementation-Gap-Analysis.md
[reconciliation]: F-01-Architecture-Reconciliation.md
[hcrl]: ../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md
[formal]: ai-agent-reports/20261120T015908Z-f-01-execution-report.md
