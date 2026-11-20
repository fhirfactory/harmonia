<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# F-01 decision delta — iteration 004

**Basis** · [Draft.001.002][previous] → [Draft.001.003][draft] against [accepted F-01.75–F-01.81][decisions]

| Decision | Incorporation and change from Draft.001.002 |
| --- | --- |
| F-01.75 | Confirms strict rejection of empty Topic version text while preserving builder/API absence. Confirms raw and escaped projections and navigation boundaries. In the proposed Java arrangement the universal kind exposes an empty entry view and ordinary-only expression/containment calls reject it. Logical interchange is explicitly tagged. [§2][navigation] · [§3][names] · [§4][mask] · [§5][interop] · [§8][api] |
| F-01.76 · R1 | Confirms qualified non-versioned runtime identity, order as structure, possible unqualified collisions, independent architectural and IU identities and no legacy UUID in the core type. Adds explicit reference and registry compatibility obligations, including generated/explicit qualifier collisions. [§1][identity] |
| F-01.77 · R2 | Requires evidence of equivalent basis and scope before Exact/Inclusive conversion, affirmative unrestricted intent before `all()`, explicit name-only translation, legacy UUID/persisted reference handling, compatible RE2/J expressions and fail-closed migration. [§4][mask] · [§5][interop] |
| F-01.78 · R3 | Establishes the IU designation as `getName()` and leaves the distinct qualified representation dependent on governed qualifiers. Preserves the IU UUID, independent version domains and existing boundary designations or explicit migration. [§7][consumers] |
| F-01.79 · R4 | Preserves architectural IDs and governed naming/identity, including continuity through rename or containment changes. Requires explicit correspondence and governed treatment of historical references; excludes architecture identifiers from runtime Topic. [§1][identity] |
| F-01.80 · R5 | Allocates direct RE2/J dependency to the implementing Maven module. Uses the existing application logger, keeps models independent of Themis and leaves the pre-existing `themis-api` concern to separate governance. Any RE2/J abstraction requires demonstrated need. [§6][diagnostics] |
| F-01.81 · R6 | Preserves synchronous, fail-closed Boolean matching. Routine TRACE has no synchronous remote-delivery requirement. Framework owns buffering, backpressure, receipt acknowledgement and recovery. Bounds and measures caller blocking, protects ERROR delivery and requires saturation, outage, recovery and console independence evidence. [§6][diagnostics] · [§10][acceptance] |

## Explicit successions retained

- The [iteration 003 delta][delta003] maps F-01.1–F-01.74 and their earlier corrections, including the F-01.55–F-01.56 qualifier/value correction to F-01.24 and F-01.48, F-01.52's present-version constraint, F-01.45's removal of the proposed wildcard tuple and `@all` encoding, and F-01.71's validated replacement at an occupied assembly index. Draft.001.003 carries those terms forward. The [iteration 002 delta][delta002] preserves the original comparison.
- [Final.001.000][final] retains the older UUID, explicit sequence-persistence, registry-name, mask basis, unrestricted default and no-version contracts as accepted historical design. [RAD 1.1.25 DR-3][rad] records their incorporated architectural standing. The F-01 successor draft uses the later accepted design decisions. Any change to the accepted Continuum requires its own governed application.
- F-01.75 settles API behaviour. The choices of Java exception classes, Java field comparison and diagnostic exit-index representation remain proposals in the draft. The exact representational binding for universal interchange and the IU qualifier vocabulary remain open. [§8][api] · [§9][open]

[previous]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.002.md
[draft]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md
[final]: ../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.000.md
[decisions]: ../before/F-01-Topic-TopicMask-Adjudications-Continuation-004.md
[delta002]: ../../002/after/F-01-Decision-Delta.md
[delta003]: ../../003/after/F-01-Decision-Delta.md
[rad]: ../../../../register/1.1.25/hcrl-1.1.25-001.complete.md#dr-3
[identity]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#1-topic-identity-and-structure
[navigation]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#2-topic-construction-and-navigation
[names]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#3-topic-names-and-strict-canonical-parsing
[mask]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#4-topicmask-kinds-and-matching
[interop]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#5-canonical-mask-expressions-and-logical-interchange
[diagnostics]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#6-resource-limits-concurrency-and-diagnostics
[consumers]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#7-consumer-boundaries
[api]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#8-proposed-java-surface
[open]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#9-open-decisions-and-evidence
[acceptance]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#10-mandatory-behavioural-acceptance
