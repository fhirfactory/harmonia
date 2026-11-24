<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Manifest, resulting source and build identity | ACCEPTED | Accept the empty development foundation at `0.1.0-SNAPSHOT`. Software Release 0.1.0 acceptance is outside scope. |
| [DR-2](#dr-2) | Verification and the ArchUnit transition | ACCEPTED WITH QUALIFICATION | The missing retained-input locator remains open. The ArchUnit amendment remains separately governed. |
| [DR-3](#dr-3) | Historical implementation candidate supersession | ACCEPTED | Preserve prior findings, provenance, decisions and accepted design authority. |
| [DR-4](#dr-4) | Historical deployment knowledge and convergence evidence | ACCEPTED | Historical evidence only. Deployment requires separate authority. |
| [DR-5](#dr-5) | Bounded Minutiae | ACCEPTED WITH QUALIFICATION | `Minutiae-Accepted: Yes`. Preserve the unresolved original Task time and filename prefix. |

# HCRL software-0.1.0 / 009 — Software Foundation Reset

**Status** · COMPLETE — ACCEPTED SOFTWARE 0.1.0 DEVELOPMENT FOUNDATION.  
**Backlog** · [0004, revision 001][backlog].  
**Origin** · Accepted Architecture 1.1.26, branch `feature/BL-CALLIOPE-OO-01`,
checkpoint `718fc278be5b89b6138d4341feddefce1269eaa6`, clean before changes.
GitHub confirmed the checkpoint as the branch tip. The accepted first-baseline
decision did not establish a software predecessor.  
**Accepted candidate** · The reset reviewed against the originating checkpoint and
recorded in the [historical review copy][reviewed].
Maven build identity `0.1.0-SNAPSHOT`. The [Task][task] records the issued
authority and the [Report][report] records the result. Software release
acceptance, deployment and further implementation require their own authority.

**Review disposition** · Mark Hunter, Solution Architect, issued the DR-1–DR-5
dispositions in the review response for this HCRL, subject to confirmation that
the existing evidence supports the stated results. The checks below confirmed
that correspondence. In the final closure instruction, Mark Hunter confirmed
all five recorded dispositions and expressly authorised registration, commit
and push to the existing feature branch. The instruction establishes the
Software 0.1.0 development foundation. Software Release 0.1.0, a release tag,
Architecture 1.1.27 acceptance or publication, and Naming implementation are
outside the closure scope.

<a id="dr-1"></a>
## DR-1 — Manifest, resulting source and build identity

The exact [approved manifest][manifest] matches the 1,326 observed tracked
deletions. All 12,837 retained tracked paths remain, with 50 authorised POM edits
and four historical review addenda. The reactor retains all project and parent
links, coordinates other than version, and packaging types. All 50 project
versions and 49 parent references are `0.1.0-SNAPSHOT`. The root retains Java 21,
UTF-8 and generic compiler, testing and archiving configuration. The deleted
software's dependencies, packaging and deployment executions were removed.
Architecture ownership and relationships retain their accepted standing.

The [retained-path patch][edits] and manifest reconstruct the tracked component
of the candidate from the originating checkpoint. The [Task][task], the
[Report][report] and the other linked evidence identify the additions. This
records the module and source scope for Backlog AC-1 and AC-2.

**Solution Architect disposition · ACCEPTED.** Accept the organised removal of
the exploratory implementation and establishment of the empty Maven foundation.
The accepted scope covers the 1,326 tracked deletions, preservation of protected
historical and architectural material, the 50-project hierarchy and adoption of
`0.1.0-SNAPSHOT`. Software Release 0.1.0 remains unaccepted.

<a id="dr-2"></a>
## DR-2 — Verification and the ArchUnit transition

Maven 3.9.12 on OpenJDK 21.0.13.1 passed bounded offline `clean verify` across
50 projects and `-pl calliope -am verify` across two projects. Surefire ran zero
tests. All 37 application-module archives contained only Maven metadata. Exact
deletions, protection of retained paths and absence of Java or frontend
implementation and placeholders passed [structural verification][checks].

The assignment and adjudication retire this candidate's exploratory ArchUnit
suite. The reset verification covers build and structural conformance. Retired
application regressions and integrations, replacement architecture tests and
runtime capabilities have no executed coverage here. The general verification
requirement is addressed by [Architecture 1.1.27/002][amendment]; its normative
text is a pending proposal. Canonical validation separately returned **FAIL** on
the already missing 1.1.25 Topic/TopicMask retained-input locator. The [Report][report]
records the exact path, environment retry and checkpoint correspondence.

**Solution Architect disposition · ACCEPTED WITH QUALIFICATION.** The reported
JDK 21 build, packaging and structural checks are accepted as evidence for the
empty development foundation. Application functionality and runtime capability
are outside this acceptance. The pre-existing missing Topic/TopicMask
retained-input locator does not block acceptance of this reset. Resolve it or
record its formal disposition before dependent acceptance or the relevant
Architecture transition. Retirement of the obsolete ArchUnit suite remains
subject to the separately governed [Architecture amendment][amendment].

<a id="dr-3"></a>
## DR-3 — Historical implementation candidate supersession

The [issued reset assignment][task] supersedes the exact Naming [008][naming]
and V001 [006][v001] software candidates. Their implementation review ends
without acceptance. The earlier exploratory Calliope candidate in [001][early]
and the implementation portions of [004][design] are likewise superseded. The
linked addenda preserve the original DRs, sources and evidence. Prior compatibility
and test gaps remain historical findings of those candidates.

The qualified acceptance in 004 DR-10, [007 design finalisation][finalisation],
other design and provenance matters, and accepted architectural requirements
retain their standing. Continuum correspondence and governed persisted data
require treatment under their own authority when applicable. The checkpoint
preserves the source without carrying the old Java compatibility layer into
the foundation.

**Solution Architect disposition · ACCEPTED.** Accept the supersession of Naming,
V001 and the associated exploratory implementation candidates. Preserve their
historical findings, provenance and review decisions. The accepted Naming
design and applicable architectural requirements remain authoritative. The clean
software foundation carries no historical implementation compatibility obligations.

<a id="dr-4"></a>
## DR-4 — Historical deployment knowledge and convergence evidence

The [deployment observations][deployment] preserve the pre-reset source paths,
configuration discrepancies and recovery knowledge. Earlier durability,
concurrency and distributed-path evidence remains attributable to its recorded
source and environment. The empty reactor supplies no current operational proof.
Future runtime verification belongs to its own authorised increment. The reset
performed repository and local build work only.

**Solution Architect disposition · ACCEPTED.** Accept the historical deployment
observations and convergence evidence as records of the previous exploratory
implementation. Current operational functionality is outside the scope of this
evidence. Deployment activity requires separate authorisation.

<a id="dr-5"></a>
## DR-5 — Bounded Minutiae

The Task emitter recorded an unresolved instruction-entry event and filename
prefix. The Report emission time is recorded by the governed tool. Original
review and evidence locations are preserved.

**Solution Architect disposition · ACCEPTED WITH QUALIFICATION.**

```text
Minutiae-Accepted: Yes
Comment: The available Task and Report provenance is accepted for this reset. The original Task instruction-entry event and filename prefix remain unresolved and must not be reconstructed through invented timestamps or identifiers. Preserve the actual evidence and its limitations.
```

## Final governance closure

Rechecked the current candidate against the approved manifest, retained-path
patch and structural verification. The deletion set and its checksum, all
12,837 retained paths, the 54 recorded edits, all 50 projects and 49 parent
version references, and the contents and checksums of all 37 archives match.
The existing build logs confirm the reported full and Calliope reactor results.
The canonical validation log confirms the failure qualified in DR-2. The
historical records, proposed ArchUnit patch and Task and Report evidence remain
retrievable. This was an evidence correspondence check of the existing results.

Pre-commit verification matched the reviewed candidate's exact deletion set,
all 54 retained-path edits and all 37 archive checksums. This register entry
and the preserved review copy accompany the accepted implementation in the
authorised closure commit on `feature/BL-CALLIOPE-OO-01`. The implementation
is identified separately by the checkpoint, approved deletion manifest and
retained-path patch. The resulting commit, remote branch correspondence and
working tree status are verified after the Git transition.

The retained-input locator remains open under DR-2 and the original Task
instruction-entry event and filename prefix remain unresolved under DR-5.
Architecture 1.1.26 remains the governing baseline. The separately governed
Architecture 1.1.27/002 proposal retains its pending status.

[backlog]: ../../../../docs/backlog/BacklogItem-0004-Software-Foundation-Reset.001.md
[task]: ../../evidence/software-0.1.0/009/before/ai-agent-tasks/unresolved-software-foundation-reset.md
[report]: ../../evidence/software-0.1.0/009/after/ai-agent-reports/20261124T045808Z-software-foundation-reset.md
[manifest]: ../../evidence/software-0.1.0/009/before/approved-delete-paths.txt
[checks]: ../../evidence/software-0.1.0/009/after/structural-verification.json
[edits]: ../../evidence/software-0.1.0/009/after/retained-path-edits.patch.gz
[deployment]: ../../evidence/software-0.1.0/009/after/historical-deployment-observations.md
[amendment]: ../../review/1.1.27/hcrl-1.1.27-002.pending.md
[naming]: ../../review/software-0.1.0/hcrl-software-0.1.0-008.pending.md
[v001]: ../../review/software-0.1.0/hcrl-software-0.1.0-006.pending.md
[early]: ../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md
[design]: ../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[finalisation]: ../../review/software-0.1.0/hcrl-software-0.1.0-007.pending.md
[reviewed]: ../../review/software-0.1.0/hcrl-software-0.1.0-009.pending.md
