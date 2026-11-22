Task-Issued-At: UNRESOLVED

Event source: Codex interaction. The attributable original instruction-entry timestamp was unavailable. The event and filename prefix require Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Calliope iteration 005 G06 lifecycle, revision and operational state

The Solution Architect supplied A13–A16 in the Codex interaction and authorised the following draft and reconciliation work in the existing `software-0.1.0/005` evidence boundary. The originating branch was `feature/BL-CALLIOPE-OO-01` at `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`. HCRL.004 traces the separate software assignment to the historical BL-CALLIOPE-OO-01 revision 1. [HCRL.004][hcrl] · [Backlog Item locator][backlog] · [Iteration 005 issue groups][groups]

## Operative instruction

~~~markdown
# Harmonia — Calliope Review Iteration 005
## G06 — Lifecycle, Version, Revision and Operational State

### Working context

**Repository:** `fhirfactory/harmonia`  
**Branch:** `feature/BL-CALLIOPE-OO-01`  
**Architecture baseline:** 1.1.26  
**Software baseline:** `Software-Version-0.1.0-Snapshot-004`

**Cumulative review:**  
`enterprise-architecture/design/review/software-0.1.0/hcrl-software-0.1.0-004.pending.md`

**Accepted G02 DRP:**  
`enterprise-architecture/design/offline-design/DRP-Calliope-Identity-and-Classification-Framework.Final.001.000.md`

### Objective

Document the Solution Architect's G06 adjudications covering findings **A13–A16**.

Create:

`enterprise-architecture/design/offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md`

Reconcile these decisions against existing architectural authority.

This assignment concerns architectural documentation and reconciliation, **not implementation or Continuum modification**.

---

## 1. Review authority

Use the following as the principal sources:

- `DRP-Information-Unit-Framework.Final.002.md`
- `DRP-Calliope-Identity-and-Classification-Framework.Final.001.000.md`
- Relevant accepted Mneme/Mnemosyne interaction and persistence DRPs.
- Architecture Continuum 1.1.26.
- `Calliope-Architectural-Issue-Groups-005.md`
- Existing G02 reconciliation evidence.
- The cumulative HCRL.004 review obligations.

Preserve the accepted G01 and G02 decisions.

Use targeted architectural evidence and bounded excerpts. Avoid unnecessary analysis of disposable implementation code.

## 2. A13 — Information lifecycle and data state

### Architectural decision

All persistently managed information has a state.

That state may be:

1. Natively supported by the underlying PersistenceService; or
2. Maintained through a Mnemosyne-managed overlay where the underlying PersistenceService does not provide the required capability.

The externally observable lifecycle semantics must remain consistent regardless of the mechanism.

### Agreed lifecycle states

| State | Architectural semantics |
|---|---|
| **Deleted** | Information is unavailable to general Harmonia services. |
| **Archived** | Information is available through GET and through searches that explicitly request archived information. UPDATE and DELETE are prohibited. |
| **Retired** | Information is available exclusively for logging, audit-event and provenance purposes. |
| **LegalHold** | An SEP responsibility, outside Harmonia's native lifecycle-management capability. |

Preserve any already accepted semantics for Active information, subject to reconciliation.

Do not assume that LegalHold is a mutually exclusive state alongside Deleted, Archived and Retired.

### Requirements

- Distinguish governed information state from operational processing state.
- Preserve information retention requirements.
- Ensure state enforcement is independent of the underlying persistence engine's native capabilities.
- Any Mnemosyne-managed state overlay must be sufficiently durable and coherent to preserve the governed lifecycle semantics.
- Do not introduce a general-purpose lifecycle workflow engine.
- Do not invent additional state transitions without explicit authority.

Identify any genuine conflict with existing IU lifecycle contracts.

## 3. A14 — DELETE semantics

### Architectural decision

`DELETE` is a **logical content-state operation**, not a physical destruction operation.

Its effect is to change the applicable content state to:

`Deleted`

Consequences:

- Deleted information is unavailable to general Harmonia services.
- Deleted information is excluded from ordinary searches and query reports.
- The underlying information remains subject to applicable retention requirements.
- Physical clean-up and destruction are outside Harmonia.
- Applicable data retention legislation and policy govern offline clean-up activities.

Physical data disposal is an external, offline activity.

### Requirements

Reconcile the existing DELETE operation with the previously accepted RETIRE semantics.

Preserve the distinction between Deleted, Archived and Retired.

Do not interpret DELETE as authorisation to physically remove retained information.

Do not introduce a Harmonia-managed retention or physical clean-up engine.

Record any contract changes needed for the accepted IU lifecycle or persistence interfaces.

## 4. A15 — Data Version versus Managed Revision

### Architectural decision

**Version** and **Revision** are distinct concepts.

#### Version

Version is a **data-centric identifier**.

The underlying information may supply:

- A `version` attribute.
- A `revision` attribute.
- Both.
- Neither.

The applicable PersistenceService determines the principal data-centric Version.

Where multiple attributes exist, the PersistenceService may:

- Select one attribute.
- Combine multiple attributes.
- Construct an appropriate composite value.

This is the responsibility of the PersistenceService, subject to its documented contract.

The Version is not necessarily numeric, ordered or sequential.

The underlying information's supplied attributes are preserved.

#### Revision

Revision is a **Mneme/Mnemosyne-managed identifier** supporting:

- Concurrency control.
- Governed information management.
- Availability.
- Coordination between active and authoritative representations.

Revision is independent of the data-centric Version.

### Requirements

- Do not derive Managed Revision from Data Version.
- Do not use Data Version as an implicit concurrency token.
- Do not require data to supply a Version.
- Do not impose numeric interpretation on arbitrary Version values.
- Preserve the distinction between authoritative and operational revision responsibilities where already governed.
- Preserve TopicEntry version semantics as an independent concern.
- Do not introduce a universal version-conversion framework.

Reconcile the terminology and ownership with existing Mneme/Mnemosyne contracts.

Identify any remaining detailed design required for authoritative revisions, active generations and expected-revision comparisons.

## 5. A16 — Operational status, transactions and recovery

### Architectural decision

**All transactions across the Mneme/Mnemosyne boundary are synchronous.**

The transaction response communicates the result against the applicable success criteria.

The established Outcome semantics remain applicable:

- Success.
- Failure.
- Indeterminate.

Synchronous execution does not imply successful execution.

### Operational status

Operational processing status is internal to the responsible Mneme or Mnemosyne component.

The shared InformationUnit model does not require a general-purpose transaction-progress or recovery-status structure.

Preserve existing component observability and operational management responsibilities.

### Recovery and durability

Recovery is an internal component responsibility.

The principal architectural concern at the persistence boundary is whether the transaction satisfies its required durability criteria.

Where the contract requires multi-site durability, Success must mean that the required multi-site durability has been achieved.

Failure or Indeterminate must accurately communicate the corresponding outcome when durability cannot be established.

Do not introduce a generic `Recovered` transaction state.

### Requirements

- Distinguish transaction outcome from persistent information lifecycle state.
- Preserve synchronous request/response semantics.
- Preserve the existing governed write-durability requirements.
- Do not infer transaction success solely from an internal processing-stage value.
- Preserve factual uncertainty in reported outcomes.
- Keep internal operational observations within their appropriate component boundaries.
- Do not invent a universal transaction-state, observation or recovery framework.

## 6. Architectural reconciliation

For each A13–A16 decision, identify:

1. Applicable accepted architecture.
2. The new Solution Architect adjudication.
3. Any genuine semantic conflict.
4. Required Continuum amendments.
5. Detailed design obligations.
6. Implementation and verification obligations.

Pay particular attention to:

- IU lifecycle and retention.
- Mnemosyne state overlays.
- Mneme/Mnemosyne synchronous contracts.
- PersistenceService state and Version responsibilities.
- Managed Revision ownership.
- Multi-site authoritative durability.
- Outcome semantics.
- Existing OperationalMetadata representations.

Do not treat historical Java enums, flags, factories or implementation behaviours as architectural authority.

Where a new adjudication supersedes an earlier accepted requirement, **record the conflict explicitly for governed incorporation**. Do not silently rewrite the accepted baseline.

## 7. Update Iteration 005 evidence

Update:

`Calliope-Architectural-Issue-Groups-005.md`

Record:

| Finding | Disposition |
|---|---|
| A13 | Lifecycle and information-state semantics adjudicated; reconciliation obligations retained. |
| A14 | DELETE logical-state semantics adjudicated; physical clean-up explicitly outside Harmonia. |
| A15 | Data Version and Managed Revision distinguished; PersistenceService Version construction accepted. |
| A16 | Synchronous transactions, contract-defined outcomes and component-owned operational status accepted. |

Preserve G01/G02 acceptance and all remaining G03–G05/G07 findings.

Do not modify the cumulative HCRL yet.

## 8. Governance constraints

- Do not modify implementation code.
- Do not modify the accepted Architecture Continuum.
- Do not modify existing accepted DRPs.
- Do not modify Topic/TopicMask Draft.001.003.
- Do not modify HCRL.004.
- Do not create HCRL.005.
- Do not commit or push.
- Do not introduce additional architectural decisions.

Follow the agreed DRP lifecycle:

`draft.000.001 → draft.000.002 → Final.001.000`

The initial G06 deliverable must remain a working draft pending Solution Architect review.

## 9. Deliverables

1. `DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md`
2. Updated Iteration 005 issue-group evidence.
3. Concise report identifying:
   - Decisions recorded.
   - Conflicts with accepted architectural authority.
   - Outstanding detailed design and verification obligations.
   - Any issue genuinely requiring further Solution Architect adjudication.

**Stop for Solution Architect review. Do not promote the DRP or initiate another issue group.**
~~~

[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[backlog]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
[groups]: ../../after/Calliope-Architectural-Issue-Groups-005.md#g06--lifecycle-authoritative-revisions-and-operational-metadata
