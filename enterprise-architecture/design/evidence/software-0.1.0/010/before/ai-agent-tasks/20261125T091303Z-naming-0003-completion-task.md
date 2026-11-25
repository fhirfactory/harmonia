<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Junie Completion Task — Backlog Item 0003

Task-Issued-At: 2026-11-25T09:13:03.379Z

**Task record amended at** · 2026-11-25T09:50:03Z (relative-link correction;
the original Task-Issued-At is unchanged).

**Issuing source** · Operative user issue description in the current Junie
interaction; the task-event timestamp is supplied by the session task record.
The issue does not identify the requester's role as Solution Architect. Its
instruction names the Solution Architect as the recipient for the final
verification report. No earlier composition time is asserted.

**Assignee** · Junie.

**Repository / branch** · `fhirfactory/harmonia` ·
`feature/BL-0003-Calliope-Topic-and-TopicMask`.

**Software target** · `0.1.0-SNAPSHOT` · review target `software-0.1.0-010`.

**Starting source** · HEAD `d79b62add0fb3f85cccafc29972330dd033b7145`, tree
`8b8dd180720ea36d36248d16bda7be2a7c73b4e7`. The worktree/index status at task
entry was not captured before the first edits and remains **UNRESOLVED**; do not
infer a clean starting state. The pending Software HCRL 010 identifies its
previous reviewed candidate separately as commit
`f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree
`1846199b36bbfb07c87746c1367c059698b7120d`.

**Implementation authority** · Accepted Backlog Item 0003 revision `000.004`
and `DRP-Topic-and-TopicMask.Final.003.md`. Proposed revision `000.005` is a
documentation reconciliation candidate only; it is not accepted and does not
replace the accepted assignment specification. [Accepted Backlog Item][accepted]
· [Proposed revision][proposed] · [Final.003][design]

## Assignment

Complete the bounded Backlog Item 0003 corrections and evidence requested in the
operative issue:

1. In `CanonicalPathSyntax.java`, keep rejecting empty completed Topic fields but
   allow the Topic parser to classify empty qualifiers and values as their actual
   `MALFORMED_TEXT` field failures (`=LAB`, `Role=`, and `Role:1=`). Preserve
   structural syntax failures as `CANONICAL_TEXT` and preserve empty mask
   pattern behavior.
2. Remove the stray `what` in the Javadoc of `NamingSerialization.java`.
3. Add focused regression coverage for public and internal diagnostics, known
   entry indices and unchanged empty-mask behavior.
4. Reconcile the XML 1.0-only contract and the task-context implementation
   decisions with Final.003 and the Backlog Item. Keep the existing accepted
   Final.003 sections unchanged; distinguish proposed reconciliation from
   accepted architecture and record uncertainty where the repository review
   does not contain an attributable disposition.
5. Run Calliope tests, full Maven `verify`, Calliope Javadoc, the specified
   architecture-test selector, `git diff --check`, and a replay check of the
   retained candidate patch. Report exact scope and outcomes.
6. Preserve this Task and a corresponding Implementation Report under
   `software-0.1.0/010`.

## Governance, authority and stop conditions

The pending [Software HCRL 010][review] has DR-1 through DR-5 marked `Not
recorded`, including DR-3 on the supported API/package boundary and DR-4 on
acceptance prerequisites. The task context refers to implementation decisions
at 07:02Z and 07:26Z, but the pending HCRL does not contain attributable
Solution Architect dispositions for this exact candidate. Preserve this
uncertainty under AX-17; do not infer acceptance or use this Task as an HCRL
disposition.

The requested source correction is limited to the accepted Topic rejection and
diagnostic contract and makes no module, dependency, persistence, security or
deployment-boundary change. Relevant canonical authority is AX-04 (Harmonia
owns semantics), AX-12 (hide plumbing, not information), AX-14 (preserve
semantic distinctions) and AX-17 (preserve authority and explicit uncertainty).
The documentation reconciliation remains proposed pending Solution Architect
review. [Architectural Axioms][axioms] · [Architecture Review Praxis][review-praxis]
· [Software Development Praxis][software-praxis] · [Architecture Change Provenance][provenance]

Do not modify, advance or complete HCRL 010 while its required dispositions and
gates remain unresolved; do not create HCRL 011 without the applicable
Solution Architect disposition. Do not allocate a software or Architecture
release, commit, push, merge, tag, publish, deploy, or accept the candidate.
Submit the exact candidate, results and unresolved HCRL/documentation decisions
to the Solution Architect for review.

## Verification directions

Use JDK 21 and preserve bounded command execution. Required commands:

```text
mvn -B -ntp -pl calliope -am test
mvn -B -ntp verify
mvn -B -ntp -pl calliope -am javadoc:javadoc
mvn -B -ntp -pl calliope -am javadoc:jar
mvn -B -ntp -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false test
git diff --check
```

The architecture selector is expected to find no tests in the current
`paradeigma-test` module and must be reported as **UNEXECUTED**, not PASS. A
separate focused pre-fix run may be used only to establish the regression; report
that result as a reproduction, not as final verification. Keep the retained
candidate patch scoped to Calliope source/tests and report any documentation
candidate files separately.

## Operative issued wording

> “On `feature/BL-0003-Calliope-Topic-and-TopicMask`, complete Backlog Item
> 0003.
>
> **Corrections**
>
> 1. `CanonicalPathSyntax.java`: correct diagnostic classification for empty
>    qualifiers/values (`=LAB`, `Role=`, `Role:1=`). Retain rejection behaviour.
> 2. `NamingSerialization.java`: remove the stray `what` from the Javadoc.
>
> **Completion**
>
> - Add focused regression tests where needed.
> - Run Calliope tests and full Maven `verify`.
> - Reconcile the XML 1.0-only contract and previously accepted implementation
>   decisions with the governing design and backlog documentation.
> - Complete HCRL 010 and the required Task/Implementation Report evidence.
> - Follow the established backlog acceptance and release-closure process.
>
> Do not introduce unrelated changes or reopen accepted design decisions.
>
> Stop before committing, pushing, or declaring acceptance. Report the final
> verification results, documentation changes, and any remaining issues to the
> Solution Architect for approval.”

[accepted]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.004.md
[proposed]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.005.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.003.md
[review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
[axioms]: ../../../../../../governance/architectural-axioms.md
[review-praxis]: ../../../../../../governance/architecture-review-praxis.md
[software-praxis]: ../../../../../../governance/software-development-praxis.md
[provenance]: ../../../../../../governance/architecture-change-provenance.md