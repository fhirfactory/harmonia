# Design Review Proposal (DRP)

**DRP:** DRP-Mneme-Pragmaton-Availability-Resilience-Recovery-001\
**Title:** Mneme Pragmaton Availability, Resilience and Recovery ---
Goals and Constraints\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Feature Realisation

This DRP primarily realises `DataManagement.InformationUnits.DataSteadfastness.Availability` for active working information and contributes to `DataManagement.InformationUnits.DataSteadfastness.Recovery` through reconstructability rather than authoritative durability.

### 1.1 New or Updated Feature Details

The Feature framing distinguishes Mneme/Pragmaton active-state steadfastness from Mnemosyne authoritative durable recovery. Pragmaton availability supports distributed execution; Pragmaton recovery means safe reconstruction/re-establishment of working state, not promotion of the active envelope into an authoritative store.

### 1.2 Associated Requirements

- Pragmaton state SHALL be reconstructable from authoritative information and/or durable processing transitions as applicable.
- Loss of a node/cache/active envelope SHALL NOT lose acknowledged authoritative information.
- Recovery SHALL NOT resume inside an Ergon.
- Partition/failure/uncertainty SHALL be represented safely.
- Concurrency/recovery SHALL preserve governed identity/state correlation without prescribing a particular algorithm.

### 1.3 Associated Strategy and Motivation

Associate with Data Steadfastness Availability/Recovery, AX-05, Petasos durable handoff and the architectural separation between active state and authoritative persistence.

## 2. Proposal

Establish availability, resilience and recovery obligations for
Mneme-managed Pragmaton state while preserving the invariant that active
working state is non-authoritative and reconstructable.

## 3. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 4. Architectural Definition

### 3.1 Availability and reconstructability

Mneme should make Pragmaton state sufficiently available for Harmonia's
distributed execution model, preferentially using native
distributed/cache capabilities where they satisfy Harmonia semantics.

Pragmaton state representing acknowledged durable information must be
reconstructable from Mnemosyne authoritative state and/or an explicitly
durable Petasos transition as applicable. Volatile Pragmaton state
cannot be the sole recoverable representation of acknowledged
authoritative information or accepted work.

### 3.2 Failure during execution

Loss, partition or uncertainty affecting Pragmaton validity must be
explicit. Mneme must not silently provide stale/incomplete working
information as known-valid state.

Recovery respects the Ergon rule: never resume inside an Ergon.
Retry/recovery occurs through the appropriate execution boundary.

### 3.3 Distribution/concurrency

Distribution, node loss and concurrency are normal. Mneme preserves
enough identity/state/version correlation to recognise unsafe delivery
or reconciliation without architecture prescribing locking, MVCC or
conflict algorithms.

### 3.4 Authority

Mnemosyne remains the authoritative recovery baseline for committed
Information Units. Replicated active state does not become authoritative
by being highly available.

## 5. Explicitly Not Prescribed

Unless explicitly stated otherwise, this proposal does not prescribe
Java classes/interfaces, DTOs, physical schemas, products, frameworks,
protocols, deployment topology, algorithms or other internal
implementation structures. Those remain solution-design space subject to
the accepted architectural semantics and constraints.

## 6. Evidence and Rationale

The proposal synthesises current governed Harmonia architecture, bounded
historical design evidence and the current Solution Architect /
architecture-peer design discussion. Historical implementation and
Legacy-RAD material are discovery evidence rather than independent
current authority.

## 7. Relationship to Current Architecture

If accepted, this proposal should be reconciled into the Architecture
Continuum using existing constructs wherever adequate. Any inability to
represent material semantics should be surfaced as a modelling gap
rather than hidden in implementation or free text.

## 8. Conformance Expectations

A conformant solution preserves the responsibilities, semantic
distinctions and invariants in this proposal while retaining
implementation freedom. Implementation technology alone neither
establishes nor disproves architectural conformance.

## 9. Open Questions / Matters for Review

1.  What validity information accompanies reconstructed Pragmaton?
2.  Which failures require abandoning/restarting an Ergon?
3.  What availability objectives belong to architecture versus
    deployment configuration?
4.  How is degraded/partitioned state exposed to Ponos/Ergo?


## 10. Continuum Update

The accepted DRP is to be integrated as a connected architectural delta. The entries below are architecture decisions/proposals for review, not instructions for the Technical Lead to invent missing architecture.

### 10.1 Motivation Updates

- Associate the Feature realisation with the Requirements, Axioms, Principles, Constraints, Goals/Outcomes and other Motivation elements identified in Section 1.
- Add/refine any requirement made explicit by this DRP only after architectural review; do not delegate requirement discovery to implementation.
- Preserve existing Motivation elements unchanged where they already express the required semantics.

### 10.2 Strategy Updates

- Establish/refine the Enterprise Capability and Feature context identified in Section 1.
- Map the Enterprise Capability/Features to the applicable Business and Business-Enabling capability context.
- Associate applicable Courses of Action and other Strategy elements.
- If no defensible Feature and Enterprise Capability placement can be established, stop and reconsider the architectural justification for the DRP.

### 10.3 Business Layer — Information Architecture Updates

- Reuse Pragmaton/Information Unit semantics; no new Business information object merely for cache/recovery state.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates

- Refine continuity/recovery behaviour only where needed by the Feature model.

### 10.5 Application Layer — Information Architecture Updates

- Add application recovery/correlation state concepts only if stable architectural contracts require them.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation

- Constrain/refine Mneme contextualisation/active-state behaviours as realisations of Availability/Recovery.

### 10.7 Application Layer — Structural / Component Updates

- Reuse Mneme; do not prescribe cache/grid/replication products or topology.

### 10.8 Cross-Layer and Cross-Feature Relationships

- Connect the application realisation to the Feature(s) it realises/supports rather than leaving application constructs isolated.
- Preserve explicit relationships to applicable Requirements, Axioms, Courses of Action and information concepts.
- Establish Access, Realisation, Serving, Assignment, Association, Influence or other native/specialised relationships according to their actual semantics; do not use generic Association where a stronger governed relationship is known.
- Connect related Features where the design depends on another Feature's services or information rather than duplicating that capability locally.

### 10.9 Explicitly Unaffected / No-Change Architecture

- Implementation classes, interfaces, DTOs, algorithms, products, deployment topology and internal module decomposition remain Technical Lead solution-design space unless explicitly elevated by this DRP.
- Existing architecture referenced only as context is not modified unless this section explicitly says so.

## 11. Review Decision

**Decision:** Pending\
**Decision authority:** Solution Architect / Architecture Review\
**Decision notes:** (To be completed during review.)

## 12. Post-Decision Actions

(To be completed after review.)

## Appendix A --- Design Provenance

This appendix is informative, not normative.

Principal lineage: ADR-018; AX-05; historical distributed Mneme Context
design.
