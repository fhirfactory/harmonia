<!--
  Copyright (c) 2026 Mark Hunter
  SPDX-License-Identifier: GPL-3.0-or-later
-->

# Governed Search Result Semantics

## 1. Authority and Two Search Cases

The authorised 2026-10-10 search adjudication distinguishes:

1. **Harmonia-Managed Information Search** over existing managed Information
   Units / Information Unit Versions.
2. **External Information Search** intentionally performed as part of an
   Ergo activity.

These cases have different management semantics. A generic transition from
observed information to accepted management SHALL NOT replace this distinction.
The [Harmonia-managed information definition and Information Unit boundary](../metamodel/information-architecture-metamodel.md#5-harmonia-managed-information)
continue to govern. Management responsibility remains distinct from originating
information authority and authority over the underlying domain activity.

The established upstream responsibilities are [Search & Discovery (EC-05)](../../02-strategy/capabilities/enterprise-capabilities.md#ec-05-search--discovery),
[retrieval mediation and governed query/search (FEAT-ISE-05/08/09)](../../02-strategy/capabilities/business-enabling-capabilities.md#detailed-capability--feature-catalogue-intrinsic--shared-enablement),
and Business [Health Information Exchange](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#22-health-information-exchange-hie)
and [Health Information Access](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#23-health-information-access).
The [Business ownership matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix)
establishes Query Execution Context & Projection State without ownership of
queried source records. Those sources establish search purpose and responsibility;
the two-case management boundary below is the explicit Domain04 adjudication,
not a claim that the upstream sources already specified result membership.

[AX-04](../../governance/architectural-axioms.md#ax-04) preserves semantic
authority independently of machinery; [AX-05](../../governance/architectural-axioms.md#ax-05)
preserves active/durable state distinctions; [AX-06](../../governance/architectural-axioms.md#ax-06)
and [AX-18](../../governance/architectural-axioms.md#ax-18) preserve source and
domain authority. [AX-07–09](../../governance/architectural-axioms.md#ax-07)
govern security context and meaningful evidence; [AX-13](../../governance/architectural-axioms.md#ax-13)
preserves the external management boundary; [AX-14](../../governance/architectural-axioms.md#ax-14)
and [AX-17](../../governance/architectural-axioms.md#ax-17) preserve the
distinctions and explicitly unestablished details below. No new axiom is required.

## 2. Harmonia-Managed Information Search

> **A search over Harmonia-managed information produces a governed
> metadata-based Search Result Set referencing the Information Unit Versions
> comprising that result. Search does not, by itself, duplicate or create new
> versions of the returned managed information.**

**Search Result Set** names this managed metadata collection. It specialises
the established [Collection meaning](../metamodel/information-architecture-metamodel.md#21-semantic-classifications)
and [membership distinction](containment-and-collections.md#3-collections-vs-containment-distinct-semantics)
through search-specific Version-reference membership. It introduces no new
semantic category or Information Concept-to-Information Unit equivalence.
“Search Result Metadata-Set” describes the same concept rather than an
additional concept or representation.

Management attaches to the result set's own governed search/result metadata
and membership within its Information Unit boundary. The referenced
Information Units / Versions retain their existing management boundaries,
authority and history. Creating the result artefact SHALL NOT create copies
of the returned Units, new Versions of those Units, a BLOB of duplicated
managed information, or another authority for that information.

```text
Harmonia-Managed Information
        |
        | search
        v
Search Result Set (managed metadata collection)
        |
        +-- Search / Result Metadata
        |
        +-- Result Membership [0..*]
                |
                +-- Information Unit Version Reference
```

### 2.1 Information Unit Version Membership

Membership records the applicable **Information Unit Version**, expressing:

> **These were the managed Information Unit Versions comprising the result
> of this search.**

It SHALL NOT resolve later to whichever Active Generation or current Version
happens to exist when the result set is examined. For example, membership
identifying Unit U's Version V7 continues to identify V7 after U progresses to
V8 or has a different active manifestation. These labels establish no
identifier scheme. The [closed Version / Active Generation semantics](../metamodel/information-architecture-metamodel.md#651-version-and-active-generation)
remain unchanged.

The set contains membership references, not copies of member content. The
conceptual `0..*` membership permits an empty result. It establishes neither
a reference encoding nor downstream retrieval, retention or manifestation
mechanics, and implies no universal atomic observation of all members.

### 2.2 Search / Result Metadata

The set SHALL contain sufficient metadata to represent the search and its
result as a governed information artefact. Semantic categories may include
search context, search criteria, execution/result time, result-set identity,
result membership and other metadata necessary to establish the governed
meaning of the result.

These categories are not a complete metadata schema, mandatory field list,
datatype or serialization. They prescribe no database, API or cache structure.
Search context does not automatically persist operational security context as
resource content; AX-07 remains applicable.

### 2.3 Search and Separate Self-Contained Artefacts

Version-specific membership preserves the meaning of a search result; it does
not make ordinary search a generic snapshot mechanism. The Search Result Set
is not a self-contained duplicated parcel of the referenced content.

Export, snapshot, document generation, archival package, transmission package
or another self-contained information artefact requires a separate semantic
act/capability. Such acts are not intrinsic to search and are not designed here.
The existing [Assembly/View and snapshot distinctions](../assemblies-views/assemblies-and-views.md#3-assembly-lifecycle-vs-constituent-lifecycle)
remain applicable without an automatic Search Result Set-to-Assembly mapping.

## 3. External Information Search within an Ergo Activity

> **An external search performed by Harmonia is intentionally undertaken
> within an Ergo activity. Information returned by that search is acquired
> and persisted within the governed context of that activity and does not
> require a separate generic management-acceptance transition.**

For the architecture currently in scope, Harmonia does not randomly or
autonomously search external sources. The Ergo activity intentionally invokes
the search and provides the governed context in which the returned information
is obtained and persisted as activity information. Management responsibility
attaches within that activity's governed information context and applicable
Information Unit boundary; it is not a new acceptance decision merely because
the source is external. This establishes no universal Unit per returned item,
per external result or per activity.

```text
Ergo Activity
      |
      +-- intentionally invokes External Search
      |        |
      |        v
      |   Returned Information
      |        |
      +--------+
      |
      v
Governed Ergo Activity / Context
      |
      v
Persisted Activity Information
```

This is an intentional governed acquisition, not a rule that arbitrary
observation, storage or consumption establishes management. The generic
`Observed -> Accepted Into Management` transition is unnecessary for this
case. Originating authority remains source-qualified; acquisition confers no
authority over the underlying domain activity. The diagram expresses
information meaning, not proof that every invocation succeeds or persistence
has completed; AX-15 continues to preserve uncertain operational outcomes.

### 3.1 External Content and Semantic Opacity

Returned domain content may remain semantically opaque to Harmonia. Ergo
logic may inspect, apply, associate, transform, route, persist or otherwise act
upon it according to explicitly established behaviour. This does not imply
that Harmonia independently infers the content's domain meaning.

The established [observable-information and governed Ergo logic boundary](../guardrails/observable-information-and-domain-meaning.md#3-governed-ergo-logic-boundary)
continues to apply. No new execution allocation, intrinsic inference or
runtime-AI responsibility is established.

## 4. Search Result Information and Audit / Assurance

The Search Result Set establishes which managed Information Unit Versions
comprised a search result. Audit / Assurance information may establish other
meaning: requester/actor, applicable authority, access/control decisions,
disclosure, operational evidence or assurance conclusions. These meanings
SHALL remain distinct.

```text
External Consumer
       |
       v
     Pylai
       |
       +----> Audit / Assurance information (according to policy)
       |
       v
Harmonia-Managed Information Search
       |
       v
Search Result Set
```

Overlapping metadata or references are legitimate; correlation does not make
these artefacts equivalent and does not require eliminating the overlap.
The result set SHALL NOT be collapsed into Audit / Assurance evidence. Its
possible contextual evidentiary use does not redefine its information meaning
or make every search operation audit evidence. Detailed Audit / Assurance
semantics remain outside this adjudication.

An externally published search response remains governed by its applicable
interoperability contract. AX-13 terminates Harmonia management of the emitted
representation, while internally retained managed information remains governed.
This conceptual result set prescribes no external envelope or response format.

## 5. Resolution and Retained Boundaries

This two-case adjudication resolves the former generic search-management
question retained in the metamodel and Assembly/View guidance. It establishes
the managed metadata result boundary and governed external acquisition without
a generic management-acceptance transition.

Search APIs, indexing, pagination, result encoding, reference realization,
cache/persistence structures, component allocation and manifestation mechanics
remain downstream concerns. Result metadata detail, retention and separate
snapshot/export capabilities are not designed here. Existing explicit
TaskOutcome detail, ReportedTask, candidate-family and Audit / Assurance
deferrals retain their standing under AX-17. This bounded decision does not
declare Domain04 complete or authorise Domain05 derivation.
