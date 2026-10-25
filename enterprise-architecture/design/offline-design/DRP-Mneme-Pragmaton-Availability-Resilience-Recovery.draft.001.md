# Design Review Proposal (DRP)

**DRP:** DRP-Mneme-Pragmaton-Availability-Resilience-Recovery-001\
**Title:** Mneme Pragmaton Availability, Resilience and Recovery ---
Goals and Constraints\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish availability, resilience and recovery obligations for
Mneme-managed Pragmaton state while preserving the invariant that active
working state is non-authoritative and reconstructable.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

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

## 4. Explicitly Not Prescribed

Unless explicitly stated otherwise, this proposal does not prescribe
Java classes/interfaces, DTOs, physical schemas, products, frameworks,
protocols, deployment topology, algorithms or other internal
implementation structures. Those remain solution-design space subject to
the accepted architectural semantics and constraints.

## 5. Evidence and Rationale

The proposal synthesises current governed Harmonia architecture, bounded
historical design evidence and the current Solution Architect /
architecture-peer design discussion. Historical implementation and
Legacy-RAD material are discovery evidence rather than independent
current authority.

## 6. Relationship to Current Architecture

If accepted, this proposal should be reconciled into the Architecture
Continuum using existing constructs wherever adequate. Any inability to
represent material semantics should be surfaced as a modelling gap
rather than hidden in implementation or free text.

## 7. Conformance Expectations

A conformant solution preserves the responsibilities, semantic
distinctions and invariants in this proposal while retaining
implementation freedom. Implementation technology alone neither
establishes nor disproves architectural conformance.

## 8. Open Questions / Matters for Review

1.  What validity information accompanies reconstructed Pragmaton?
2.  Which failures require abandoning/restarting an Ergon?
3.  What availability objectives belong to architecture versus
    deployment configuration?
4.  How is degraded/partitioned state exposed to Ponos/Ergo?

## 9. Continuum Projection

### 9.1 Proposed Projection

  -------------------------------------------------------------------------------------------------------------------
  Architectural Assertion         Proposed Continuum        Treatment          Projection Notes        Status
                                  Construct                                                            
  ------------------------------- ------------------------- ------------------ ----------------------- --------------
  Pragmaton active state must be  Pragmaton + Mneme         Extend             Likely                  Review
  sufficiently available          availability/resilience                      motivation/constraint   
                                  constraints                                  projection              

  Pragmaton is reconstructable,   Pragmaton + Mnemosyne +   Relate/Constrain   Reuse active/durable    Proposed
  not authoritative               AX-05                                        architecture            

  Petasos durable transition may  Pragmaton/Pragma +        Relate             Do not make Petasos     Review
  support work reconstruction     Petasos                                      authoritative           
                                                                               information store       

  Failure/partition/uncertainty   Pragmaton + existing      Relate/Constrain   Reuse AX-15 vicinity    Lookup
  must be explicit                uncertainty constructs                                               

  Recovery never resumes inside   Pragmaton + Ergon         Relate/Constrain   Reuse established Ergon Proposed
  Ergon                           execution constraints                        rule                    

  Concurrency safety requires     Pragmaton + state/version Constrain          No lock/MVCC construct  Lookup
  identity/state/version          constructs                                   unless independently    
  correlation but no prescribed                                                architectural           
  algorithm                                                                                            
  -------------------------------------------------------------------------------------------------------------------

### 9.2 Projection Rules

The Continuum Projection identifies the expected architectural
consequences of accepting this DRP. It is **not** a mechanical mapping
of DRP sections to ArchiMate elements.

Each assertion SHALL be reconciled against the existing Continuum before
creating a construct:

**traverse → identify → reuse where semantically adequate → extend where
appropriate → create only where necessary.**

A single assertion may affect multiple Continuum constructs. Multiple
explanatory statements may produce one architectural change. Rationale,
evidence, implementation non-prescriptions and unresolved matters do not
automatically create Continuum constructs.

Where projection requires an additional architectural decision, the
projection SHALL stop at that point and return the matter for Solution
Architect adjudication rather than inventing the missing architecture.

### 9.3 Unresolved Projection Matters

Rows marked **Review** are proposed mapping choices requiring Solution
Architect confirmation. Rows marked **Lookup** require Continuum
vicinity traversal before deciding whether to reuse, extend or create a
construct.

## 10. Review Decision

**Decision:** Pending\
**Decision authority:** Solution Architect / Architecture Review\
**Decision notes:** (To be completed during review.)

## 11. Post-Decision Actions

(To be completed after review.)

## Appendix A --- Design Provenance

This appendix is informative, not normative.

Principal lineage: ADR-018; AX-05; historical distributed Mneme Context
design.
