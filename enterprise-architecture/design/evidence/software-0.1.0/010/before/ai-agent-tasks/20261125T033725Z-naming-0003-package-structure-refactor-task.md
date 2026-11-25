<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Junie Task — Calliope Naming Package-Structure Review

Task-Issued-At: 2026-11-25T03:37:25.465Z

**Issuing authority** · The operative user instruction in the Junie interaction.
This task authorises the bounded software package-structure refactor and review;
it does not supply new architectural decisions, software acceptance, or HCRL
disposition. The timestamp is the attributable task-issuance time recorded for
this interaction; no earlier composition time is asserted.

**Assignee** · Junie.

**Repository / branch** · `fhirfactory/harmonia` ·
`feature/BL-0003-Calliope-Topic-and-TopicMask`.

**Software target** · `0.1.0-SNAPSHOT`, review target `software-0.1.0-010`.

**Observed base** · HEAD `46a08484e5c824b870fecda59b943ba37a241e2b`. The exact
pre-edit worktree snapshot for this bounded refactor was not captured; the
resulting candidate and complete repository state are identified in its Report.

## Assignment

Review and refactor the Calliope Naming implementation's Java package structure
to better reflect:

- `docs/design/DesignNote-Object-and-Class-Granularity.md`;
- `docs/design/DesignNote-Calliope-Initial-Model-Architecture.md`; and
- `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`.

Preserve the accepted supported behavior and public contract in Backlog Item
0003 revision `000.004` and
`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.003.md`.
Do not introduce compatibility code merely to retain implementation structure.
Exercise judgement about principal naming models, dependent entry values,
builders, public value sets and implementation utilities. Use cohesive,
function-oriented subpackages only where the accepted visibility and
encapsulation contracts permit them. Identify and explain any conflict between
package-private restrictions and further decomposition.

Do not change accepted design documents, Backlog Items or HCRLs. Do not commit,
push, merge, tag, publish, or declare the software candidate accepted. Present
the resulting implementation and verification for independent review.

## Required verification and report

Use JDK 21. Run the Calliope reactor tests and full Maven verification:

```text
mvn -B -ntp -pl calliope -am test
mvn -B -ntp verify
```

Run the applicable documentation check. Report the resulting package structure,
the design decisions and their rationale, any unresolved API/encapsulation
conflict, consumer impact, exact commands and outcomes, and the candidate
identity. Do not claim checks that were not executed.

## Governing context

The two `docs/design` notes are draft implementation guidance and do not
supersede the Architectural Axioms, accepted Final.003, or accepted Backlog Item
0003 v000.004. Final.003 §8 defines the supported Java API and requires the
codec and package-private parsing, matching, diagnostic and limit machinery to
remain inside the Naming package. The package refactor must not silently widen
that API boundary.

## Operative task wording

> “Review the current Calliope Naming implementation on
> `feature/BL-0003-Calliope-Topic-and-TopicMask`.”
>
> “Refactor the implementation to better reflect these documents. In
> particular, consider the distinction between principal domain objects,
> dependent data types, value sets, builders and implementation utilities. Use
> cohesive, function-oriented subpackages where appropriate, drawing on the
> established Calliope package architecture.”
>
> “Preserve the behaviour and supported API specified by accepted Backlog Item
> 0003 v000.004 and `DRP-Topic-and-TopicMask.Final.003.md`. Do not introduce
> compatibility code merely to preserve the existing implementation structure.”
>
> “Where the accepted design's package-private restrictions conflict with
> sensible encapsulation, identify the conflict and explain your proposed
> resolution.”
>
> “Run the applicable tests and Maven verification. Report the resulting
> package structure, significant design decisions, any unresolved conflicts and
> verification results.”
>
> “Do not change accepted design documents, backlog items or HCRLs. Do not
> commit or push. Present the resulting implementation for review.”

[granularity]: ../../../../../../../docs/design/DesignNote-Object-and-Class-Granularity.md
[calliope-architecture]: ../../../../../../../docs/design/DesignNote-Calliope-Initial-Model-Architecture.md
[implementation-guide]: ../../../../../../../docs/implementation/Harmonia-Implementation-Guide-Philosophy.md
[backlog]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.004.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.003.md