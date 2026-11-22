Task-Issued-At: UNRESOLVED

Event source: Codex interaction. The attributable original instruction-entry timestamp was unavailable. The event and filename prefix require Solution Architect treatment before closure.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Calliope iteration 005 G06 Draft.000.002 refinement

The Solution Architect issued the following clarification after reviewing [Draft.000.001][draft]. It refines the [original G06 adjudications][prior-task] within the existing `software-0.1.0/005` evidence boundary. The originating branch was `feature/BL-CALLIOPE-OO-01` at `6b058c1b332ac6244e6ff2d6aecad9d6c9e2a48d`. HCRL.004 traces the separate software assignment to the historical BL-CALLIOPE-OO-01 revision 1. [HCRL.004][hcrl] · [Backlog Item locator][backlog]

## Operative instruction

~~~markdown
# Harmonia — Calliope Review Iteration 005
## G06 — Lifecycle, Revision and Operational State Refinement

**Repository:** `fhirfactory/harmonia`  
**Branch:** `feature/BL-CALLIOPE-OO-01`  
**Architecture baseline:** 1.1.26  
**Software baseline:** `Software-Version-0.1.0-Snapshot-004`

### Objective

Revise:

`enterprise-architecture/design/offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md`

Create:

`enterprise-architecture/design/offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.002.md`

Preserve the previously agreed A13–A16 decisions.

This is a targeted architectural clarification following Solution Architect review, **not a new investigation or implementation assignment**.

### 1. A13 — Information lifecycle and physical disposal

The Solution Architect confirms that Harmonia manages the logical lifecycle of governed information.

Harmonia shall **not implement or expose an explicit PURGE function**.

Physical data disposal is performed by an authorised database administrator through an external, offline administrative process, following applicable offline archival activities and subject to retention legislation and policy.

The distinction is:

- **DELETE:** Harmonia-managed logical state change.
- **PURGE:** External, offline physical disposal, not a Harmonia operation.

Preserve the previously agreed visibility and operation restrictions for Active, Deleted, Archived and Retired information.

Do not introduce:

- A Harmonia PURGE API.
- A purge scheduler.
- An automated physical-deletion workflow.
- A separate lifecycle-management engine.

### 2. LegalHold boundary

The Solution Architect's SEP designation does not require a new Harmonia legal-hold administration capability.

Preserve existing accepted obligations to recognise and respect applicable legal holds.

Distinguish external legal-hold administration and legal authority from Harmonia's obligation to honour restrictions that apply to governed information.

Do not invent a new legal-hold service, interface or protocol.

Any unresolved mechanism for receiving or validating externally established hold restrictions remains detailed design.

### 3. A14 — DELETE and RETIRE

The Solution Architect confirms:

1. DELETE and RETIRE are distinct governed operations.
2. DELETE changes the applicable content state to `Deleted`.
3. Deleted information is unavailable to general Harmonia services and excluded from ordinary searches and query reports.
4. RETIRE retains its separately governed purpose and semantics.
5. Neither operation authorises physical destruction.
6. DELETE is to be accommodated within existing governed service contracts; no additional service is required.
7. The exact contract allocation and affected IU/content or revision scope remain detailed design and governed incorporation obligations.

Clarify the existing Business IU prohibition against physical destruction as applying to Harmonia-managed operations.

Do not interpret it as prohibiting externally authorised offline disposal by a database administrator.

Preserve applicable retention, archival and legal-hold requirements.

### 4. A15 — Data Version and Managed Revision

Preserve Draft.000.001 without substantive change.

In particular:

- Data Version is data-centric.
- PersistenceService determines its principal value.
- PersistenceService may select or combine native version/revision attributes.
- Managed Revision belongs to Mneme/Mnemosyne.
- Managed Revision supports concurrency, governed management and availability.
- The two concepts remain independent.

Retain the existing authoritative and operational revision distinctions and their detailed design obligations.

### 5. A16 — Synchronous transactions and operational state

Preserve Draft.000.001 without substantive change.

In particular:

- All Mneme/Mnemosyne transactions are synchronous.
- Outcomes reflect their operation-specific success criteria.
- Success requires multi-site durability where mandated.
- Internal operational status and recovery belong to the responsible component.
- No generic `Recovered` transaction state is required.
- No universal operational-state framework is required.

### 6. Architectural reconciliation

Update the reconciliation section to reflect that the Solution Architect has resolved the A13/A14 questions concerning:

- External physical disposal.
- Absence of a Harmonia PURGE operation.
- LegalHold's SEP boundary.
- Distinct DELETE and RETIRE semantics.
- Scope of the Business IU physical-destruction prohibition.

Retain genuine Continuum amendment obligations.

Do not present these settled questions as requiring another Solution Architect decision.

Identify only remaining detailed design, incorporation and verification obligations.

### 7. Evidence and governance

Update the Iteration 005 issue-group evidence to record the A13–A16 adjudications.

Preserve G01/G02 acceptance and G03–G05/G07 standing.

Use the accepted G02 filename:

`DRP-Calliope-Identity-and-Classification-Framework.Final.001.000.md`

Do not modify:

- Implementation code.
- Accepted Architecture Continuum.
- Accepted DRPs.
- Topic/TopicMask Draft.001.003.
- HCRL.004.

Do not create HCRL.005, commit or push.

### Deliverables

1. `DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.002.md`
2. Updated Iteration 005 G06 evidence.
3. Concise report confirming:
   - Decisions incorporated.
   - Preservation of A15/A16.
   - Remaining detailed design and Continuum obligations.
   - Any genuine architectural contradiction that could not be reconciled.

**Stop for Solution Architect review. Do not promote the DRP to Final.001.000 without explicit acceptance.**
~~~

[draft]: ../../../../../offline-design/DRP-Calliope-Lifecycle-Revision-and-Operational-State.draft.000.001.md
[prior-task]: unresolved-calliope-g06-lifecycle-revision-and-operational-state-task.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md
[backlog]: ../../../../../../../docs/backlog/calliope-oo-01-topic-models.md
