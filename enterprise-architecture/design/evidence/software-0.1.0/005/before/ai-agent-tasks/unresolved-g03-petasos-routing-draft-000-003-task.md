Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Petasos Routing and Reconciliation · Revision 003

The operative Solution Architect instruction follows. The starting worktree was on `feature/BL-CALLIOPE-OO-01` at Snapshot-004 (`6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`). [Draft.000.002][predecessor] SHA-256 `46db01f8321aa710e7618b71b0c694421a6f36b526eea270fc5a8bf6d647d71e` identifies the exact source revision. [G03 / Iteration 005 / DR-12][hcrl]

## Operative instruction

# Harmonia — Petasos Routing and Reconciliation: Revision 003

**Architecture baseline:** 1.1.26  
**Software:** 0.1.0  
**Review:** G03 / Iteration 005 / DR-12

## Objective

Revise:

`enterprise-architecture/design/offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md`

Create:

`enterprise-architecture/design/offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md`

Incorporate the Solution Architect's agreed decisions concerning:

1. Pre-routing reconciliation.
2. Restart detection using operational timestamps.
3. Mneme-managed routing concurrency.
4. The ephemeral `PetasosRoutingSharedStatus` model.
5. The resulting PF-08 architectural disposition.

**This is a bounded architectural design revision, not an implementation assignment.**

Preserve the existing Subscription-Router, Praxis-Router, Ergon Egress and provenance-recovery decisions.

Do not reopen their established responsibilities.

---

## 1. Pre-routing reconciliation

The Solution Architect has established the following conceptual routing sequence:

```java
RoutingList routingList = buildRoutingList();

routingList = checkToSeeIfAlreadyRoutedAndRemoveCompleted(
    routingList
);

performRoutingOnModifiedList(routingList);
```

Both `Petasos.Subscription-Router` and `Petasos.Praxis-Router` follow this strategy.

### Required semantics

`buildRoutingList()` must retrieve an existing durable routing decision where one has already been established.

Where no applicable decision exists, the router establishes the decision and its complete obligation set under the existing Draft.000.002 contract.

Reconciliation identifies obligations whose routing has already been successfully completed.

Confirmed completed obligations are excluded from the **working routing list**, without deleting their authoritative history.

Incomplete obligations remain eligible for processing, subject to applicable concurrency, authorisation and recovery conditions.

Indeterminate obligations must be reconciled against sufficient authoritative evidence or held pending resolution.

**An indeterminate queue-injection outcome must not automatically cause reinjection.**

The operation is logically:

```java
RoutingList routingList = buildOrRecoverRoutingList();

RoutingList outstanding =
    reconcileAndRemoveConfirmedRouted(routingList);

performRouting(outstanding);
```

These are illustrative method names, not canonical APIs.

## 2. Conditional reconciliation and restart detection

The Solution Architect has established two operational properties:

```java
moduleStartupInstant() : Instant

lastSuccessfulRoutingActivity() : Instant
```

### 2.1 Module startup

`moduleStartupInstant()` identifies the startup instant of the applicable Petasos module instance.

It is operational state, not authoritative evidence of routing completion.

### 2.2 Last successful routing activity

`lastSuccessfulRoutingActivity()` identifies the last successfully completed routing activity for the applicable routing engine.

It must not advance merely because routing was attempted.

An unsuccessful, partially completed or indeterminate activity must not be misrepresented as successful.

### 2.3 Comparison

Conceptually:

```java
if (lastSuccessfulRoutingActivity()
        .isBefore(moduleStartupInstant())) {

    reconcileRoutingList();
}
```

This permits Petasos to detect that its operational continuity may have been interrupted.

The comparison is an **optimisation and recovery trigger**, not a substitute for the authoritative routing records.

A restart is not the only possible cause of invalid operational state.

Reconciliation must also occur when required by:

- An indeterminate routing operation.
- Loss of applicable routing ownership.
- Loss of relevant operational continuity.
- A detected conflict or uncertainty concerning a routing obligation.

Do not require an expensive historical reconciliation for every normal routing activity when operational continuity and the applicable obligation state are already established.

Do not assume that timestamps alone establish whether an individual obligation was durably injected into Artemis.

### 2.4 Initialisation and scope

Specify the required semantics for an engine with no previous successful routing activity.

Do not invent a synthetic historical success timestamp.

Clarify the scope of `lastSuccessfulRoutingActivity` so that successful processing of one routing set cannot incorrectly suppress necessary reconciliation of another.

Preserve the proposed names and `Instant` types.

The exact storage and API implementation remain solution-design matters.

---

## 3. PetasosRoutingSharedStatus

### 3.1 Architectural decision

Introduce the proposed logical operational-state concept:

**`PetasosRoutingSharedStatus`**

This replaces the earlier informal description of a routing semaphore.

The term *semaphore* is not architecturally appropriate because it prescribes a particular concurrency mechanism.

`PetasosRoutingSharedStatus` represents shared operational coordination information used by Petasos routing activities.

### 3.2 Responsibility allocation

**Petasos:**
- Owns the meaning and use of the shared routing state.
- Determines routing obligations and their completion semantics.
- Requests coordinated access through Mneme.
- Performs routing and reconciliation.

**Mneme:**
- Manages the shared operational state.
- Provides the required concurrency and consistency controls.
- Coordinates ownership across applicable Petasos instances.
- Does not interpret subscription rules, Praxis progression or queue-routing decisions.

**Mnemosyne:**
- Continues to provide authoritative persistence for the durable routing decisions and work under its existing contracts.
- Does not become responsible for Petasos routing semantics.

Preserve the existing Mneme-managed operational/concurrency architecture.

Do not introduce another distributed-locking or coordination framework.

### 3.3 Ephemeral state — explicit decision

The Solution Architect has selected:

**Option A — ephemeral shared operational state.**

`PetasosRoutingSharedStatus` is not an additional authoritative durable routing record.

It must be reconstructable after failure or restart using the established authoritative information and routing records.

The authoritative records remain:

- `SubscriptionIdEvent`
- `PraxisRoutingEvent` (provisional name)
- Applicable existing Pragma-family information and provenance.

Avoid duplicating durable routing-decision authority in the shared operational state.

Loss of `PetasosRoutingSharedStatus` must not result in loss of routing obligations or previously established outcomes.

### 3.4 Concurrency scope

The proposed concurrency-control unit is the applicable **RoutingSetList**.

Petasos must prevent conflicting routing activities from concurrently reconciling or fulfilling the same routing set.

Independent routing sets may proceed concurrently.

The shared status must be effective across the relevant Petasos instances, not merely within a single JVM.

Preserve the distinction between:

- Routing-set ownership and coordination.
- Individual routing-obligation completion.
- Authoritative routing-decision persistence.
- Artemis queue injection and acceptance.

The precise ownership, acquisition, release, renewal and failure-recovery mechanism remains for solution design.

### 3.5 Failed-owner protection

An expired or failed routing owner must not continue processing an obligation after another instance has legitimately assumed ownership.

The design must identify the required ownership-validity or fencing invariant.

Do not prescribe a particular implementation or introduce a new framework.

Do not claim that ephemeral state alone provides durable fencing guarantees.

Any proposed mechanism must be compatible with existing Mneme concurrency-management capabilities.

---

## 4. Conceptual combined operation

Capture a combined routing sequence similar to:

```java
RoutingSetList routingSet =
    buildOrRecoverRoutingSet();

PetasosRoutingSharedStatus sharedStatus =
    mneme.acquireRoutingSharedStatus(routingSet.id());

try {

    if (requiresReconciliation(
            sharedStatus,
            moduleStartupInstant(),
            lastSuccessfulRoutingActivity())) {

        routingSet =
            reconcileAndRemoveConfirmedRouted(routingSet);
    }

    performRouting(routingSet);

    if (routingActivitySuccessfullyCompleted()) {
        recordLastSuccessfulRoutingActivity();
    }

} finally {
    mneme.releaseRoutingSharedStatus(sharedStatus);
}
```

**Illustrative pseudocode only.**

It must not be interpreted as prescribing:

- Canonical Java interfaces.
- A particular lock, semaphore or lease implementation.
- A new distributed coordination platform.
- An atomic transaction spanning Mneme and Artemis.
- Timestamp-based proof of queue acceptance.

The architectural requirement is that applicable shared-state coordination protects reconciliation and routing from conflicting concurrent activity.

The exact ordering of routing-set construction, ownership acquisition and authoritative refresh must be reconciled to avoid stale-state decisions.

---

## 5. Preserve the existing router designs

### 5.1 Subscription-Router

Preserve:

- Petasos-owned subscription matching.
- The durable SubscriberSet and `SubscriptionIdEvent`.
- Mneme construction of subscriber-specific Pragmatosis instances.
- Petasos queue injection.
- Per-obligation completion evidence.
- Recovery using the original subscription decision.

The new shared-status mechanism coordinates access to the routing set.

It does not alter the existing Pragmatosis identity or creation semantics.

### 5.2 Praxis-Router

Preserve:

- Ponos-owned Praxis evaluation API.
- Petasos invocation of that API.
- Selection of existing originating Pragmas produced by Ergon Egress.
- Optional Praxis-defined output filters.
- The durable Praxis routing decision.
- Petasos routing of existing Pragmas.
- No additional originating Pragma construction.

The shared-status mechanism coordinates the resulting routing obligations without changing Praxis interpretation ownership.

### 5.3 Ergon Egress and provenance

Preserve the established sequence:

```java
List<Pragma> createdList =
    Ergon.Egress
        .PostBlockingOfDeadTasks()
        .PostPEP
        .CreatePragmaFromOutputList();

Ergon.Egress.HandoverToPetasos(createdList);
```

Retain the authoritative-provenance recovery strategy for reconstructing `createdList` after an uncertain handover.

Preserve original Pragma identities.

Do not introduce another durable handover record solely to preserve `createdList`.

---

## 6. PF-08 disposition

Update the design-level PF-08 treatment to distinguish the agreed architectural strategy from remaining detailed contracts.

### Architectural strategy — resolved

The following design decisions are agreed:

1. Durable routing decisions and complete obligation sets.
2. Reconciliation before routing when required.
3. Exclusion of confirmed completed obligations from the working list.
4. Explicit treatment of incomplete and indeterminate obligations.
5. Conditional restart detection.
6. Mneme-managed shared routing coordination.
7. Ephemeral `PetasosRoutingSharedStatus`.
8. RoutingSetList-level concurrency protection.
9. Reuse of existing work identities during recovery.
10. Provenance reconstruction of originating Pragma lists.
11. Separation of Subscription-Router and Praxis-Router responsibilities.

### Remaining detailed contracts

Retain explicit review obligations for:

- Exact routing decision, routing-set and obligation identities.
- Authoritative proof of Artemis queue acceptance after an indeterminate result.
- Safe reinjection and duplicate suppression.
- Concurrency ownership validity and fencing.
- Cross-instance recovery after owner failure.
- Precise shared-status lifecycle and consistency guarantees.
- Startup and last-success timestamp scope and initialisation.
- Detailed handoff acknowledgement and retry equivalence.
- Execution eligibility following redelivery.
- Implementation and failure-injection verification.

**Do not mark PF-08 as fully resolved.**

Record:

**PF-08 — Architectural strategy resolved; detailed transport, concurrency and recovery contracts outstanding.**

Preserve any earlier unresolved obligations that are not explicitly superseded by the decisions above.

---

## 7. Architectural reconciliation

Reconcile Draft.000.003 against the existing:

- Petasos routing and queue-management responsibilities.
- Mneme operational-state and concurrency responsibilities.
- Mneme/Mnemosyne authoritative persistence contracts.
- Pragma/Pragmatosis identities and lifecycle.
- PetasosParcel and handoff contracts.
- Ergon Egress.
- Ponos/Praxis progression.
- Existing provenance and lineage arrangements.
- Themis enforcement.
- Kleio audit obligations.
- PF-07/PF-08 adjudications.

Identify genuine inconsistencies and required canonical correspondence.

Do not silently revise accepted Continuum elements.

The pending `Mnemosyne.DurableInformation` / `Mnemosyne.DurableData` proposal remains a separate architectural candidate.

---

## 8. Required outputs

### 8.1 Revised DRP

Create:

`DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md`

Preserve Draft.000.002 unchanged.

### 8.2 Reconciliation evidence

Create a bounded reconciliation report under:

`enterprise-architecture/design/evidence/software-0.1.0/005/after/`

Document:

- Exact changes from Draft.000.002.
- Correspondence with existing Mneme concurrency and operational-state capabilities.
- Ephemeral shared-state semantics.
- Startup and successful-routing activity semantics.
- Concurrency and recovery invariants.
- Remaining PF-07/PF-08 contracts.
- Any unresolved conflicts with accepted architecture.

### 8.3 Cumulative HCRL

Update:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

Under DR-12:

- Reference Draft.000.003.
- Record the agreed `PetasosRoutingSharedStatus` concept.
- Record the decision for ephemeral shared operational state.
- Record conditional reconciliation and restart detection.
- Record Mneme-managed RoutingSetList concurrency.
- Record PF-08's qualified architectural disposition.
- Retain all previous dispositions and evidence.

Do not claim final DR-12 acceptance.

---

## 9. Constraints

- No application-code changes.
- No accepted Continuum changes.
- No architecture publication.
- No new routing or workflow framework.
- No new distributed coordination framework.
- No new HA/DR framework.
- No speculative canonical API definitions.
- No commit, tag or push.
- Preserve all earlier draft revisions and evidence.
- Keep the HCRL pending Solution Architect review.

## 10. Completion report

Report:

1. Revised DRP path.
2. Reconciliation evidence path.
3. Updated HCRL reference.
4. Exact PF-08 disposition recorded.
5. Any discrepancies with existing Mneme concurrency architecture.
6. Remaining architectural decisions requiring Solution Architect adjudication.

**The objective is to capture the agreed Petasos routing coordination strategy and close the architectural design portion of PF-08, without prematurely claiming that all transport, concurrency and recovery contracts are resolved.**

[predecessor]: ../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.002.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12
