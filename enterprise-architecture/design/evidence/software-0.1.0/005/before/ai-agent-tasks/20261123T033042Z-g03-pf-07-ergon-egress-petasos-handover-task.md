Task-Issued-At: 2026-11-23T03:30:42.870Z

Event source: Codex interaction rollout-2026-11-22T22-04-06-01a202ed-72a2-75e3-a092-1706fc2be364.jsonl, record 744, timestamp of the user-role response_item containing the operative instruction entering the session.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Detail — G03 / PF-07 Ergon Egress to Petasos Handover

- **Direction** — The operative assignment below records the Solution Architect's PF-07 adjudications and authorises a bounded revision of the routing candidate, a cumulative DR-12 update and Iteration 005 evidence.
- **Sources** — [Routing Draft.000.003](../../../../../offline-design/DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md) and [accepted Processing Framework Final.001](../../../../../offline-design/DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md#42-stage-and-checkpoint-correspondence). [HCRL.004 DR-12](../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-12) retains the review history. Architecture 1.1.26 and Software candidate 0.1.0 supply the basis.
- **Checked-out state** — `feature/BL-CALLIOPE-OO-01` at `3588f5f8df1fbe65d64e8635a3a6a378bf19360c`, matching its local tracking ref. The index was clean. Earlier DR-12 changes, Processing Framework drafts, Final.001 and evidence were present in the worktree.

## Operative issued instruction

````
# Codex Assignment — G03 / PF-07: Ergon Egress to Petasos Handover

## 1. Objective

Incorporate the Solution Architect's PF-07 adjudications concerning the handover of newly created originating Pragmas from Ergon Egress to Petasos.

The architectural responsibilities and functional behaviour are now agreed.

This assignment is a **bounded design-documentation and reconciliation activity**, not an implementation task or a request to reopen the processing architecture.

## 2. Authority and context

- Accepted Architecture baseline: **1.1.26**
- Software candidate: **0.1.0**
- Review: **G03 / Iteration 005 / DR-12**
- Accepted Processing Framework: `DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md`
- Cumulative HCRL: `hcrl-software-0.1.0-004.pending.md`

The accepted Processing Framework defines the relevant handovers:

**H10 — Originating Pragma Creation**

`ergon.egress.pragma-create`

**H11 — Petasos Route-In**

`petasos.route-in`

The functional sequence and checkpoint allocation are accepted and must remain unchanged.

## 3. PF-07 adjudication — H10

### 3.1 Information contract

The Solution Architect has established the following conceptual information structure:

```java
public class ErgonCreatedPragmaList {

    public class ErgonCreatedPragmaToken {
        String pragmaId;
        Status status;
    }

    List<ErgonCreatedPragmaToken> createdList;
    Integer expectedListSize;
}
```

Treat this as a **conceptual information contract**.

Do not infer specific Java implementation, package placement, serialization or framework dependencies.

The exact names above are the Solution Architect's proposed terminology. Preserve them unless a conflict with accepted canonical terminology is identified and explicitly reported.

### 3.2 Contract semantics

`expectedListSize` identifies the number of originating Pragmas expected from the applicable Ergon Egress processing.

`createdList` contains the individual Pragma tokens produced through Mneme's PragmaFactory.

Each token identifies an originating Pragma and records its creation/persistence status.

The two completeness considerations are distinct:

1. The number of entries in `createdList` corresponds to `expectedListSize`.
2. Each required originating Pragma has been successfully and authoritatively established.

List-size equality alone is not sufficient to establish successful completion.

The creation results must remain attributable to the applicable Ergon processing instance.

### 3.3 Ownership

- **Ergon Egress** owns determination of the expected originating Pragma set and its completeness.
- **Mneme** owns governed Pragma construction and persistence coordination through PragmaFactory.
- **Mnemosyne** establishes the required authoritative durability.

H10 establishes originating Pragma creation and persistence outcomes.

H10 does **not** establish Petasos routing acceptance or downstream execution.

Preserve the distinction between the Egress Pragma and the newly created originating Pragmas.

## 4. PF-07 adjudication — H11

### 4.1 Complete-list acceptance

The Solution Architect has decided:

**Petasos accepts the complete `ErgonCreatedPragmaList`, not a subset.**

The handover is all-or-nothing at the list-acceptance boundary.

Do not introduce partial-list acceptance as normal successful behaviour.

Petasos assumes responsibility for routing the complete accepted set.

Acceptance must be durably established before H11 can claim successful handover.

### 4.2 Acknowledgement

Retain the previously discussed conceptual acknowledgement:

```java
PragmaListHandoffStatus status =
    Petasos.RoutingEngine.Route(createdList);
```

With the conceptual successful acknowledgement:

```text
received = GOOD
commited = TRUE
```

These are conceptual contract terms, not approved Java interfaces, enum values or field spellings.

Preserve the distinction between:

- Receipt of a handover request.
- Durable acceptance of complete-list routing responsibility.
- Subscriber matching and routing decisions.
- Queue acceptance and delivery.
- Downstream execution.

A successful H11 confirms **durable acceptance of routing responsibility for the complete list**.

It does not assert that every Pragma was routed to a subscriber or subsequently executed.

The final acknowledgement representation remains detailed design work.

### 4.3 Zero-subscriber outcomes

The Solution Architect has explicitly established that:

**Petasos may accept an originating Pragma but subsequently determine that it has no subscribers.**

This is a valid routing disposition.

For example:

| Originating Pragma | Subscriber matches | Disposition |
|---|---:|---|
| Pragma A | 2 | Route to two subscribers |
| Pragma B | 0 | Valid no-subscriber disposition |
| Pragma C | 1 | Route to one subscriber |

A zero-subscriber result does not invalidate the earlier complete-list acceptance.

Do not require subscriber matches for every accepted Pragma.

Preserve Petasos ownership of matching and routing and Mneme's role in construction of any required subscriber-specific Pragmatosis.

## 5. Failure and uncertainty boundaries

Preserve the architecture's existing distinct outcomes for:

- Failed or indeterminate originating Pragma creation.
- Incomplete originating Pragma sets.
- Failed or indeterminate complete-list acceptance.
- Successful acceptance followed by routing or delivery failures.
- Valid zero-subscriber dispositions.

Do not conflate an unknown acknowledgement with a proven rejection.

Do not assume that retrying a request permits duplicate work creation.

The detailed durable acknowledgement, reconciliation, idempotence and recovery mechanisms remain subject to their existing owning contracts.

**Do not invent a new transaction coordinator, persistence store, information container or processing component.**

## 6. Documentation changes

### A. Petasos Routing and Reconciliation DRP

Update the existing candidate:

`DRP-Petasos-Routing-and-Reconciliation.draft.000.003.md`

Create its next numbered draft, preserving the predecessor unchanged.

Incorporate the PF-07 decisions into the existing Ergon Egress handover and routing-acceptance material.

Preserve all prior PF-08 dispositions.

### B. Processing Framework

**Do not modify:**

`DRP-Harmonia-Processing-Framework-and-Checkpoints.Final.001.md`

It is already accepted.

Where necessary, reference its H10/H11 architecture from the revised routing DRP.

### C. Related DRPs

Identify any directly affected candidate DRPs, especially the Pragma Model and Petasos–Mneme interaction.

Do not automatically revise them.

Record required companion changes as bounded follow-up obligations unless an exact consistency correction is necessary and independently authorised.

### D. HCRL

Update:

`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

Under **DR-12**, record:

- The `ErgonCreatedPragmaList` conceptual contract.
- The `expectedListSize` completeness requirement.
- Per-Pragma creation/persistence status.
- H10 creation-completeness responsibility.
- H11 all-or-nothing complete-list acceptance.
- Durable routing-responsibility acknowledgement.
- Valid zero-subscriber routing disposition.
- Outstanding detailed acknowledgement and recovery contracts.

Update the PF-07 disposition to reflect that its **architectural responsibilities and normal-path behaviour are resolved**, while detailed contracts and conformance evidence remain outstanding.

Do not mark the collective G03 review accepted or closed.

### E. Evidence

Create the appropriate Task and Report evidence under:

`enterprise-architecture/design/evidence/software-0.1.0/005/`

Use the existing conventions.

Preserve previous evidence and review history.

## 7. Verification

Verify that:

1. The accepted Processing Framework Final.001 remains unchanged.
2. The existing routing draft remains preserved.
3. The revised routing draft accurately describes H10 and H11.
4. Complete-list acceptance is all-or-nothing.
5. Zero-subscriber routing is explicitly valid.
6. Creation, acceptance, routing, delivery and execution outcomes remain distinct.
7. No new architecture component, status domain or processing stage has been introduced.
8. PF-08 decisions and qualifications remain unchanged.
9. HCRL.004 carries forward all previous review decisions and outstanding obligations.
10. Documentation references resolve.

Report unrelated pre-existing validation failures without attempting corrective changes.

## 8. Constraints

Do not:

- Modify software.
- Modify the Architecture Continuum.
- Modify an accepted DRP.
- Create new components or processing stages.
- Expand into HA/DR implementation design.
- Reopen settled publication, WorkSpace or processing-framework decisions.
- Close the collective G03 review.
- Commit, tag or push.

## 9. Deliverables

Return:

1. `DRP-Petasos-Routing-and-Reconciliation.draft.000.004.md`
2. Updated HCRL.004 / DR-12.
3. Concise reconciliation and execution report.

**Objective:** Record the Solution Architect's PF-07 architectural decisions and produce a bounded revised routing candidate, preserving detailed acknowledgement and recovery obligations for subsequent design.
````
