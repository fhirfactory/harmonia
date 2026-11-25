<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Junie Implementation Task — Backlog Item 0003

Task-Issued-At: 2026-11-25T01:43:36.190Z

**Issuing authority** · Mark Hunter, Solution Architect, through the operative
implementation assignment in the Junie interaction. The event timestamp is the
attributable system instruction-entry time; it does not assert an earlier
composition time. Source event: `UserPromptEvent prompt-261125-124336-2ej7`,
session `session-261125-124313-6ibo`.

**Assignee** · Junie.

**Repository / branch** · `fhirfactory/harmonia` ·
`feature/BL-0003-Calliope-Topic-and-TopicMask`.

**Software target** · `0.1.0-SNAPSHOT`, review target `software-0.1.0-010`.

**Starting source** · HEAD `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`, matching the accepted software
foundation. The index is clean. The pre-existing worktree contains the
unaccepted Calliope RE2/J candidate and `calliope/pom.xml` change, plus unrelated
documentation, backlog, Architecture review and evidence changes. Its Calliope
inventory was 23 production Java files, seven JUnit classes and two XML fixtures;
the compile-scope RE2/J dependency was in the modified Calliope POM. Preserve all
pre-existing changes not required by this bounded assignment, including the
unrelated `.junie/plans/naming-0003-implementation-task.proposed.md`,
`.junie/plans/naming-0003-junie-handoff.md` and the modified convergence plan.
The exact predecessor candidate patch is
`enterprise-architecture/design/evidence/software-0.1.0/010/after/naming-0003-candidate.patch`,
SHA-256 `70ad92b2503e89b15ff150e89562723b23ca80c1f7cc94648863e7f5a553f97a`;
it is unaccepted material, not design authority. The implementation Report records
the starting status and exact candidate treatment.

**Implementation authority** · Accepted Backlog Item 0003 revision `000.004`.
Its complete AC-1–AC-13 specification and acceptance evidence are authoritative.
The accepted detailed design is `DRP-Topic-and-TopicMask.Final.003.md`.
[Accepted Backlog Item][backlog] · [Acceptance record][acceptance] ·
[Final.003][design]

## Assignment

Implement the complete Calliope Naming contract from the accepted backlog and
Final.003. The production boundary is
`calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/`, with
corresponding Calliope tests, fixtures, Maven changes and supported-contract
documentation. Implement all accepted Topic and TopicBuilder behaviour, the six
TopicMask wildcard forms and version constraints, strict canonical and XML
interchange, the §8 public API, safe typed validation, local optional diagnostics,
resource limits and concurrent immutable reuse. Remove obsolete RE2/J code,
dependency and tests; do not retain compatibility APIs.

Refactor actual affected direct or indirect consumers only where the accepted
Naming contract is sufficient authority. Do not implement unrelated InformationUnit,
Pragma, Petasos, subscription/publication, registry/alias, persistence, Themis,
Kleio or operational-logging functionality. If an evidenced consumer requires an
independently governed decision that the accepted design does not supply, leave
that boundary unchanged and report the exact dependency.

The previous RE2/J implementation is an unaccepted candidate and reusable
engineering material, not design authority. Preserve the accepted foundation
and unrelated worktree changes; pre-1.0.0 coherence takes precedence over
backward compatibility.

## Governance and verification boundaries

Architecture HCRL 1.1.28/001 DR-6 authorises standalone Naming implementation
before Architecture 1.1.27/002 and 1.1.28 completion when its five conditions
are met: Final.003 supplies the unambiguous contract; Backlog Item 0003 v000.004
is accepted; applicable prerequisites and outstanding matters have explicit
dispositions; discrepancies remain recorded and governed; and software conformance
claims remain bounded by evidence. [DR-6][dr6] Final.003 and Backlog Item 0003
v000.004 provide the accepted contract and specification; the recorded DR-6
dispositions preserve implementation prerequisites, architectural discrepancies
and the limits on conformance claims.
Architecture 1.1.26 remains the incorporated baseline. Do not modify HCRL
1.1.28/001, Architecture release state, Software HCRL 010 or prior findings.
Dependent software acceptance remains gated on applicable Architecture
incorporation and verification, exact-candidate independent review, and Solution
Architect disposition. The named ArchUnit suite is absent and must be reported
as unexecuted; do not imply it passed. Preserve the separate logging and
downstream-boundary qualifications.

DR-6's five conditions are recorded as follows: Final.003 establishes the
unambiguous contract; Backlog Item 0003 v000.004 is explicitly accepted; the
preparation report records dispositions for applicable prerequisites and
outstanding decisions; architectural discrepancies and deferred responsibilities
remain governed under DR-4/DR-6/DR-10; and all conformance statements must be
limited to evidence. The retained Software HCRL 010 has no Solution Architect
disposition for the earlier RE2/J candidate. Its findings remain attached to
that exact candidate; the successor is independently verified and submitted,
not used to rewrite or close the old review.

Use JDK 21. Run the required commands with bounded waiting:

```text
mvn -B -ntp -pl calliope -am test
mvn -B -ntp verify
```

Also perform applicable structural, dependency and affected-consumer checks.
Map evidence to AC-1–AC-13, report test cases actually executed from reports,
and distinguish separate Maven runs without duplicate-count inflation. Document
unavailable tests or checks and their acceptance implications. Prepare the
Implementation Report under the existing Software Development Praxis, including
the exact candidate state, source and consumer/dependency analysis, verification
results, qualifications and limitations. Submit for independent Codex review;
do not self-accept the candidate.

No commit, push, merge, tag, publication, deployment, backlog revision,
Architecture change or HCRL closure is authorised by this assignment.

## Operative issued wording

> “The Solution Architect authorises implementation of the accepted:
> `docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.004.md`.”
>
> “Implement the accepted Naming model within Calliope, including all applicable
> acceptance criteria AC-1 through AC-13.”
>
> “The accepted backlog item is the implementation specification. Final.003 is
> the authoritative detailed design. Do not reopen architectural decisions or
> substitute an alternative design.”
>
> “The previous RE2/J implementation is unaccepted.”
>
> “Preserve all unrelated worktree changes. The previous RE2/J implementation
> candidate may be refactored, replaced or deleted as necessary. Do not revert
> the entire worktree to the foundation checkpoint.”
>
> “Before Software 1.0.0, implementation coherence takes precedence over
> backward compatibility.”
>
> “Identify and refactor actual affected direct and indirect consumers, if any,
> to the accepted contract. Do not introduce a compatibility layer or legacy
> Naming API. If a consumer requires an architectural decision not supplied by
> the accepted design, report the precise boundary and leave that independently
> governed functionality unchanged.”
>
> “Use JDK 21. Run `mvn -B -ntp -pl calliope -am test` then
> `mvn -B -ntp verify`. Also run applicable structural, dependency and
> integration checks.”
>
> “Do not claim unavailable checks passed.”
>
> “Do not commit, push, merge, tag or publish.”
>
> “Complete the authorised implementation, testing, documentation and
> Implementation Report in the current assignment.”
>
> “Stop after presenting the resulting candidate and verification evidence for
> independent review. The next activity is Codex review and Solution Architect
> adjudication—not automatic software acceptance.”

[backlog]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.004.md
[acceptance]: ../../../../../../../docs/backlog/evidence/0003/000.004-acceptance-report.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.003.md
[dr6]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md#dr-6
[software-review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
[prior-review]: ../../after/ai-agent-reports/20261124T091229Z-naming-0003-independent-review.md
[preparation]: ../../../../../../../docs/backlog/evidence/0003/000.004-preparation-report.md