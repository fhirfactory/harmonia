<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Junie Task — Calliope Naming Object Model and Function-Package Redesign

**Task-Issued-At** · 2026-11-25T04:35:58.919Z

**Timestamp source** · `startedAtMs` 1795581358919 of request
`prompt-261125-153558-9ua2` in the Junie session task log
(`session-261125-124313-6ibo/tasks.json`). This is the attributable system
time at which the operative instruction entered the agent interaction. No
earlier composition time is asserted. Earlier Tasks in this iteration used the
session index `lastPromptAt` value, which can differ by a few milliseconds.
That value was overwritten by later prompts and is no longer available for
this instruction.

**Issuing authority** · The Solution Architect's operative instruction and the
subsequent attributable dispositions recorded below. This Task authorises a
bounded software refactor. It supplies no software acceptance, HCRL
disposition or Architecture release change.

**Assignee** · Junie.

**Repository / branch** · `fhirfactory/harmonia` ·
`feature/BL-0003-Calliope-Topic-and-TopicMask`.

**Software target** · `0.1.0-SNAPSHOT`, review target `software-0.1.0-010`.

**Starting source** · HEAD `46a08484e5c824b870fecda59b943ba37a241e2b`.
The Calliope working state was the previous package-refactor candidate, with
tree `d186b400cf2ee01c0fc91aa10421ff036105613d` (reconstructed by staging
`calliope` over HEAD in a temporary index before any edit).

## Operative instruction (2026-11-25T04:35:58.919Z)

> Revisit the Calliope Naming implementation on
> `feature/BL-0003-Calliope-Topic-and-TopicMask`.
>
> Read and apply: `DesignNote-Calliope-Initial-Model-Architecture.md`,
> `DesignNote-Object-and-Class-Granularity.md`,
> `Harmonia-Implementation-Guide-Philosophy.md`.
>
> The previous iteration largely retained the existing class organisation
> because of Java package-visibility constraints. This time, reconsider the
> object model and package structure from first principles. We want cohesive,
> domain-oriented packages, with principal domain objects clearly distinguished
> from supporting data types, value sets, builders, parsers, validators and
> utilities.
>
> Exercise engineering judgement about class responsibilities, construction
> boundaries and visibility. Existing implementation choices are not
> constraints simply because they already exist. Use accepted Backlog Item 0003
> v000.004 and Final.003 for the required Naming behaviour. Where their
> structural prescriptions conflict with sound OO design, identify the conflict
> and propose a resolution rather than perpetuating it.
>
> Refactor the implementation accordingly, including tests and documentation.
> Run applicable Maven verification. Report your design rationale, significant
> trade-offs and any unresolved decisions. Do not change accepted
> architectural documents or backlog items, and do not commit or push. Present
> the result as an unaccepted candidate for our review.

## Fail-closed escalation and Solution Architect dispositions

The first proposal made Final.003 §8 package-private machinery public inside
`naming.internal.*` packages and introduced a JPMS descriptor. Under
Architecture Governance (*Governance enforcement and fail-closed authority*),
the agent stopped before implementing it and requested disposition on three
questions. The questionnaire responses carry no attributable system
timestamp in the session records, so **their time is UNRESOLVED**. Their
content was:

- **Internal visibility.** “Are JPMS and the internal hierarchy are warranted
  before authorising those specific mechanisms?”
- **JPMS module.** “no. do not introduce module-info.java or JPMS. process with
  the agreed function-orientated package restructuring using standard Java
  packages and appropriate visibility.”
- **XML implementation split.** No response.

**Disposition — 2026-11-25T07:02:20.439Z**
(`prompt-261125-180220-1l0p`, task-log `startedAtMs`):

> You are authorised to to keep the separation for NameXmlCodec, but please
> remove the "internal" from the package naming and maintain function only
> packaging.

**Disposition — 2026-11-25T07:26:44.640Z**
(`prompt-261125-182644-zzoa`, task-log `startedAtMs`):

> one important detail.... the Topic, TopicMask etc classes need to support
> Serialisation.

**Interpreted bounded authority.** Together these dispositions authorise:

- function-only standard Java packages, with no `internal` naming and no JPMS;
- appropriate visibility, including public implementation types where Java
  requires them across function packages;
- keeping the `NamingXmlCodec` facade separate from the XML mechanics; and
- making the Naming models serializable.

They do not amend Final.003, Backlog Item 0003 or any HCRL. The departures and
the serialization extension still require Architecture Review and HCRL
treatment.

## Required verification and report

Use JDK 21. Run `mvn -B -ntp -pl calliope -am test` and `mvn -B -ntp verify`,
plus the applicable documentation, dependency and architecture checks. Report
the resulting structure, rationale, trade-offs, conflicts with Final.003 §8,
unresolved decisions, exact results and the candidate identity. Do not claim
checks that were not executed.

[granularity]: ../../../../../../../docs/design/DesignNote-Object-and-Class-Granularity.md
[calliope-architecture]: ../../../../../../../docs/design/DesignNote-Calliope-Initial-Model-Architecture.md
[implementation-guide]: ../../../../../../../docs/implementation/Harmonia-Implementation-Guide-Philosophy.md
[backlog]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.004.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.003.md
