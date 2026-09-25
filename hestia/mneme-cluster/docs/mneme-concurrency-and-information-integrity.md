```{=html}
<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->
```
# Mneme Concurrency and Information Integrity

## 1. Purpose

This note captures the architectural intent for concurrency, semantic
conflict detection and information integrity within Mneme.

It is deliberately not a final design for Task 08. The Mneme
distributed-behaviour laboratory has established the technical
concurrency characteristics available from Infinispan; the relationship
between those mechanisms, Mnemosyne authoritative persistence, FHIR
versioning, information authority and conflict-resolution policy remains
an explicit design concern.

The objective is to make safe behaviour the default platform behaviour
rather than something every developer must remember to implement
correctly.

## 2. Problem

Silent lost updates and developer-dependent concurrency conventions are
unacceptable.

A distributed system can make a resource consistently visible across
participants while still allowing a stale whole-resource update to
overwrite a legitimate intervening change. Synchronous replication
therefore addresses distributed visibility, but does not by itself
prevent lost updates.

This is particularly important for Harmonia because resources may:

-   be read and updated by different runtime participants;
-   contain multiple independently meaningful attributes;
-   originate from, or be influenced by, multiple information sources;
-   carry different provenance, authority and assurance characteristics;
    and
-   ultimately represent clinical or operational information where
    silent loss may have downstream consequences.

Correctness must not depend on a developer having read a particular
`HOWTO.md`, copied the right example, or remembered to use a particular
Infinispan primitive.

## 3. Architectural principles

### 3.1 Enforce rather than rely on developer knowledge

> **Correctness requirements SHALL be enforced by platform boundaries
> where practicable and SHALL NOT depend solely upon developer
> knowledge, coding convention, documentation, or voluntary use of the
> correct implementation pattern.**

Documentation explains *why*. The platform API should enforce *what*.
Tests should demonstrate *that*.

### 3.2 Mneme responsibility

Mneme owns Harmonia's distributed resource access and coordination
capability for active application state.

For concurrency this means that Mneme should provide a governed
mechanism for detecting stale active-state modifications without
becoming the authoritative durability boundary.

> **Mneme coordinates active distributed state; Mnemosyne commits
> authoritative durable state.**

### 3.3 Keep the common path cheap

Routine reads and uncontested writes should remain inexpensive.

The normal path should require no semantic diff, three-way comparison,
conflict history or information-authority evaluation:

``` text
READ
  |
  v
Mneme read
  |
  +-- value
  +-- opaque concurrency token
          |
          v
   application work
          |
          v
conditional update
          |
       SUCCESS
```

Semantic analysis belongs on the exceptional conflict path.

### 3.4 ADR-020: Governed information lifecycle and deletion guardrail

In alignment with **ADR-020 (Governed Information Uses Lifecycle State Rather
Than Physical Deletion)**, Harmonia establishes that governed persisted
information is never physically deleted through normal operations:

> **Harmonia SHALL NOT expose physical deletion as a normal operation for
> governed persisted information. Logical deletion SHALL be represented as a
> domain-appropriate lifecycle transition and processed as a concurrency-controlled
> authoritative update. Mneme cache eviction SHALL NOT be interpreted as
> deletion of authoritative state. Archival, retention-based purge and physical
> disposal are outside the current Harmonia framework scope.**

This guardrail maintains four strict architectural distinctions:

- **Resource Lifecycle Transition**: Authoritative domain state progression
  (e.g., transitioning to `inactive`, `entered-in-error`, `cancelled`, or
  `completed`) committed to Mnemosyne strictly as a concurrency-controlled
  authoritative `UPDATE` with provenance capture and Kleio audit logging.
- **Mneme Eviction**: Invalidation, expiration, or removal of non-authoritative
  active cache entries from Mneme/Infinispan according to capacity or TTL
  policies. Eviction affects only the active working set and does not alter
  durable state in Mnemosyne.
- **Archival**: Long-term historical data tiering and migration, designated as
  outside the current Harmonia framework scope.
- **Purge / Physical Disposal**: Permanent statutory data destruction,
  designated as outside the current Harmonia framework scope.

**Infinispan `removeWithVersion()` characterization**:
Infinispan Hot Rod's `removeWithVersion()` is a version-aware conditional
removal capability used for distributed cache coordination and active-set
management within the non-authoritative cache. It is a technical cluster
coordination capability, not a Harmonia governed resource deletion semantic.

### 3.5 Do not turn Mneme into a version repository

Mneme must not retain historical generations of every active resource
merely to support occasional conflict analysis.

Where a three-way comparison is required, the design should preserve the
distinction between:

-   `BASE` --- the representation against which a modification was made;
-   `CURRENT` --- the currently visible representation; and
-   `PROPOSED` --- the representation the caller intends to establish.

How `BASE` is retained is deliberately unresolved. The cost should,
where practical, be associated with an active modification rather than
every cached resource.

## 4. What the Mneme laboratory has established

The Block 3B laboratory demonstrates the following Infinispan behaviour
for Mneme's current distributed topology.

### 4.1 Unconditional whole-resource updates can lose information

Two participants can read the same resource, independently modify
different attributes, and subsequently perform unconditional `put()`
operations.

The later whole-resource write can overwrite the earlier participant's
independent modification because the later participant is writing a
stale representation.

This is a lost update even though the two intended business changes do
not themselves overlap.

### 4.2 Version-aware operations can detect stale active state

Infinispan Hot Rod provides an opaque entry-version token through
`getWithMetadata()` and supports conditional operations such as
`replaceWithVersion()` and `removeWithVersion()`.

The laboratory demonstrates that:

-   a conditional update using the current entry version succeeds;
-   a subsequent update using the stale version is rejected;
-   stale tokens are rejected irrespective of whether they are presented
    by the same or another Hot Rod client;
-   multiple participants presenting the same original token do not all
    succeed; and
-   a stale conditional removal is rejected after the entry has been
    modified.

These findings establish a useful native mechanism for detecting stale
Mneme active state. They do **not** establish the final Task 08
authoritative write protocol.

### 4.3 Conflict detection is not conflict resolution

A rejected conditional update tells Harmonia that the representation
used by the caller is no longer current.

It does not tell Harmonia:

-   which attributes changed;
-   which changes were made externally;
-   which changes the caller intended;
-   whether those changes overlap;
-   whether apparently independent changes violate a cross-field
    invariant;
-   which information source has authority for the affected information;
    or
-   whether the update should be retried, merged, rejected or stewarded.

Infinispan can detect the stale state. Harmonia must determine what that
stale state means.

## 5. Layers of conflict

Concurrency, semantic conflict and information authority are related but
must not be collapsed into one mechanism.

### 5.1 Version conflict

A version conflict answers:

> **Has the active representation changed since the caller read it?**

This is a technical concurrency concern and is naturally associated with
Mneme.

### 5.2 Semantic conflict

A semantic conflict answers:

> **Did the information the caller intended to change also change?**

This requires understanding `BASE`, `CURRENT` and `PROPOSED`, or an
equivalent explicit change set.

For example:

``` text
BASE:
    telephone = 555-0100
    address   = 100 Main St

CURRENT:
    telephone = 555-9999
    address   = 100 Main St

PROPOSED:
    telephone = 555-0100
    address   = 200 Elm St
```

There is a version conflict, but the intended changes do not
structurally overlap:

``` text
external change:
    telephone

proposed change:
    address

direct overlap:
    NONE
```

A different example may produce both a version conflict and a semantic
conflict.

### 5.3 Information-authority conflict

An information-authority conflict answers a different question:

> **What is the standing of competing assertions about the same
> information?**

Harmonia's information classifications such as `AUTHORITATIVE`,
`INFORMATIONAL` and `ANECDOTAL`, together with provenance and source
information, may inform resolution policy.

They must not be reduced to a simplistic precedence rule such as:

``` text
if AUTHORITATIVE then overwrite
```

Authority is contextual. A source may be authoritative for one class of
information and merely informational for another.

An authority conflict can also exist without a technical concurrency
conflict. A newly received informational assertion may contradict an
existing authoritative fact even when no simultaneous update has
occurred.

## 6. Separation of responsibilities

The emerging responsibility model is:

``` text
Pylai
    establishes ingress/source/provenance context
              |
              v
Calliope
    defines canonical semantics, paths,
    profiles and information-governance meaning
              |
              v
Mneme
    coordinates active distributed state
    and detects stale representations
              |
              v
Semantic Change Analysis
    determines what changed and whether
    intended and external changes overlap
              |
              v
Information Governance / Policy
    considers provenance, authority,
    assurance and applicable rules
              |
              v
Ponos / Ergon
    executes retry, resolution or
    stewarding workflow where required

Mnemosyne
    remains authoritative durable application state

Kleio
    records architecturally significant
    decisions and actions as immutable evidence
```

The exact placement of semantic change analysis is not yet decided. It
should not cause Mneme itself to become the owner of FHIR or
canonical-model semantics.

## 7. FHIR considerations

FHIR resources make naive structural diffing unsafe as a general
semantic-conflict mechanism.

Array positions in JSON are not necessarily stable semantic identities.
Repeating elements such as identifiers, telecoms, addresses, codings and
extensions may require model-aware comparison.

Conflict analysis should therefore operate at a meaningful
canonical/FHIR semantic level rather than blindly treating JSON paths
such as `/telecom/2/value` as durable identities.

FHIR `Resource.meta.versionId`, Infinispan entry versions, Mnemosyne
durable versions and HTTP/FHIR ETag or `If-Match` values are separate
concepts unless an explicit architecture deliberately relates them.

They must not be assumed to be interchangeable.

## 8. Performance and loading

The concurrency design should preserve a strongly asymmetric cost model.

``` text
LEVEL 0 — READ
    normal distributed Mneme access

LEVEL 1 — UNCONTESTED WRITE
    conditional active-state update

LEVEL 2 — VERSION CONFLICT
    obtain current representation

LEVEL 3 — SEMANTIC ANALYSIS
    BASE / CURRENT / PROPOSED or ChangeSet analysis

LEVEL 4 — RESOLUTION
    retry, policy, user decision or stewarding workflow
```

The increasingly expensive behaviour should occur increasingly rarely.

In particular:

-   routine reads must not perform semantic conflict analysis;
-   uncontested writes must not perform three-way diffing;
-   every cached resource must not carry historical copies merely to
    support conflict reporting;
-   semantic analysis should be resource/object scoped rather than
    recursively treating an arbitrary referenced graph as one
    concurrency unit; and
-   distributed payloads should remain small enough that
    conflict-support metadata does not materially inflate Hot Rod
    traffic, replication cost or Mneme memory usage.

Where the caller already knows its intended change set, Harmonia should
avoid rediscovering that intent by unnecessarily diffing entire objects.

## 9. Developer-facing API direction

Application code should use a governed Mneme facade rather than directly
remembering the correct Infinispan concurrency primitive.

Conceptually:

``` java
VersionedResource<Patient> patient = mneme.read(patientKey);

Patient proposed = updatePatient(patient.value());

UpdateResult<Patient> result = mneme.update(patient, proposed);
```

The public abstraction should express Harmonia semantics, not expose
Infinispan implementation details.

A future conflict result may progressively evolve from a simple version
conflict toward richer information such as:

``` text
ConcurrentModificationConflict

resource key
expected active-state token
current active-state token

BASE
CURRENT
PROPOSED

external changes
proposed changes
overlapping changes

source / provenance
authority / assurance
```

This is architectural direction, not a commitment to retain every field
or representation shown above.

Unrestricted direct access to mutable governed `RemoteCache` instances
should eventually be reviewed. A platform invariant that can be bypassed
accidentally is not a strong invariant.

## 10. Resolution policy

The concurrency mechanism should detect and describe conflicts. It
should not automatically infer that every non-overlapping change is safe
to merge.

Two structurally distinct changes may still violate a semantic or
business invariant.

Possible resolution outcomes may eventually include:

``` text
ACCEPT
REJECT
REREAD_AND_RETRY
REAPPLY_NON_OVERLAPPING_CHANGE
REQUEST_USER_RESOLUTION
QUEUE_FOR_STEWARDING
RECORD_COMPETING_ASSERTION
```

Which outcomes are permitted, and under what authority, belong to policy
and workflow rather than to Infinispan.

Automatic merge should be introduced only where the model and policy can
demonstrate that it is safe.

## 11. Progressive implementation

The intended progression is deliberately incremental:

1.  **Version conflict detection** --- prevent silent stale writes and
    deletes.
2.  **Rich conflict context** --- retain or recover enough context to
    explain a conflict.
3.  **Semantic change analysis** --- distinguish resource change from
    overlapping intended change.
4.  **Authority and assurance-aware classification** --- incorporate
    source, provenance and information-governance semantics.
5.  **Resolution workflows and policy** --- support user, workflow or
    stewarding decisions.
6.  **Selective automatic reconciliation** --- only where safety and
    semantic independence are demonstrable.

Each stage should remain useful without requiring the later stages to
exist.

## 12. Open Task 08 questions

Task 08 must deliberately address the relationship between Mneme
active-state coordination and Mnemosyne authoritative persistence. In
particular:

1.  At what point is an update considered authoritative?
2.  Which version is checked at the Mnemosyne durable-write boundary?
3.  How, if at all, is the Infinispan entry version related to the
    Mnemosyne persisted version?
4.  What happens when Mneme coordination succeeds but the Mnemosyne
    write is rejected or fails?
5.  What happens when Mnemosyne commits successfully but the subsequent
    Mneme update or refresh fails?
6.  Which token is exposed through a FHIR/HTTP `ETag` or `If-Match`
    contract?
7.  Where is retry performed, and which component owns reapplication or
    semantic conflict analysis?
8.  How are authoritative-state conflicts distinguished from
    active-state conflicts?
9.  How is `BASE` made available for semantic conflict analysis without
    turning Mneme into a historical version store?
10. How are provenance, information authority and assurance carried
    across the write without making them part of Infinispan's technical
    concurrency mechanism?
11. Which operations may be automatically retried and which require
    explicit workflow or human resolution?
12. How are significant conflict and resolution decisions represented in
    Kleio audit evidence?

These questions are intentionally unresolved by the Mneme laboratory.

## 13. Guardrails

The following guardrails apply to subsequent design and implementation:

-   Mneme SHALL NOT become the authoritative durable state repository.
-   Successful Mneme coordination SHALL NOT by itself imply successful
    authoritative persistence.
-   Mneme SHALL NOT silently degrade to process-local application state.
-   Concurrency/version checks SHOULD be enforced by the governed
    platform API rather than voluntary caller convention.
-   FHIR version, Infinispan entry version and Mnemosyne durable version
    SHALL remain distinct unless an explicit architecture defines their
    relationship.
-   Information authority and assurance SHALL inform policy; they SHALL
    NOT be implemented as a simplistic overwrite priority.
-   Semantic conflict analysis SHOULD execute on the exceptional
    conflict path rather than routine reads.
-   Mneme SHOULD NOT retain historical copies of every active resource
    solely to support conflict analysis.
-   Automatic merge SHALL NOT be inferred solely from the absence of
    directly overlapping structural paths.
-   Task 08 SHALL define the authoritative write boundary before
    production concurrency behaviour is changed.

## 14. Key concept

> **Concurrency tells Harmonia whether information changed. Semantic
> analysis tells Harmonia what changed. Information authority and
> assurance tell Harmonia about the standing of competing assertions.
> Policy determines what Harmonia should do about them.**

A related implementation principle follows:

> **Optimise the common path for speed. Spend intelligence when a
> conflict actually occurs.**

And the platform principle remains:

> **Make the safe thing the easy thing, and make the unsafe thing
> difficult or impossible.**
