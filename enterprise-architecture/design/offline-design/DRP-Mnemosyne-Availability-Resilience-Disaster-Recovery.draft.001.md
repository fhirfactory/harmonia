# Design Review Proposal (DRP)

**DRP:** DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery-001\
**Title:** Mnemosyne and PersistenceService Availability, Resilience and
Disaster Recovery --- Goals and Constraints\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish explicit availability, resilience and disaster-recovery
obligations for Mnemosyne and its PersistenceServices without
prescribing the technology or topology by which they are realised.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Fault / Recovery

Authoritatively persisted Information Units must remain durably
preservable and recoverable across applicable failure and disaster
conditions. Recovery preserves governed identity, authoritative
state/version, provenance and information-control semantics and must not
silently manufacture a new authoritative history.

### 3.2 Performance / Availability

Mnemosyne must provide authoritative persistence and
information-acquisition capabilities with availability appropriate to
Harmonia's distributed operational model. Failure, degradation and
uncertainty are explicit; a weaker local fallback must not masquerade as
authoritative persistence.

HA and DR are distinct: HA concerns continued authoritative service
through failure; DR concerns restoration after catastrophic loss.

### 3.3 PersistenceService realisation

PersistenceServices may use native replication, clustering, journalling,
snapshots, backup/restore, geographic redundancy, failover or other
mechanisms. Architecture defines the obligation; the solution
demonstrates how it is satisfied.

Different PersistenceServices may have different resilience
capabilities, but their use must be appropriate to the recovery
obligations of assigned information.

### 3.4 Separation

Mneme may provide highly available access to reconstructable active
state but is not the disaster-recovery authority for committed
Information Units. Petasos may provide durable work
acceptance/recoverable handoff but does not replace Mnemosyne as the
committed-state authority.

### 3.5 Invariants

-   No acknowledged authoritative state depends solely on volatile/cache
    state for recovery.
-   HA mechanisms cannot weaken authoritative acceptance.
-   Recovery cannot change governed identity/version meaning.
-   Site/network/storage failure is a normal architectural condition.
-   Infrastructure may realise the requirement; it cannot make the
    requirement disappear.

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

1.  Which information classes need differentiated availability/recovery
    objectives?
2.  Do numerical RTO/RPO targets belong to architecture, deployment
    policy or configuration?
3.  What verification is required before recovered authoritative service
    resumes?
4.  Which recovery evidence is audit evidence versus operational
    evidence?

## 9. Continuum Projection

### 9.1 Proposed Projection

  ----------------------------------------------------------------------------------------------------------
  Architectural        Proposed Continuum     Treatment          Projection Notes             Status
  Assertion            Construct                                                              
  -------------------- ---------------------- ------------------ ---------------------------- --------------
  Mnemosyne owns       Mnemosyne              Extend             Responsibility/constraint;   Proposed
  durable preservation                                           reuse existing recovery      
  and recovery                                                   semantics                    
  obligations                                                                                 

  HA and DR are        Availability +         Reuse/Extend       Prefer existing              Lookup
  distinct             Recovery/Resilience                       principles/requirements if   
  architectural        motivation vicinity                       present                      
  concerns                                                                                    

  PersistenceService   PersistenceService +   Constrain/Relate   Outcome obligation at        Review
  may realise HA/DR    Mnemosyne                                 Mnemosyne; mechanism below   
  using native                                                   boundary                     
  capabilities                                                                                

  Mneme active         Mneme + Mnemosyne +    Relate/Constrain   Reuse active/durable         Proposed
  availability is not  AX-05                                     separation                   
  authoritative DR                                                                            

  Petasos work         Petasos + Mnemosyne    Relate/Constrain   Reuse ADR-018 semantics      Proposed
  recovery does not                                                                           
  replace                                                                                     
  committed-state                                                                             
  recovery                                                                                    

  Degraded/uncertain   Existing               Reuse/Relate       Do not create duplicate      Lookup
  recovery must be     uncertainty/AX-15                         uncertainty concept          
  explicit             vicinity                                                               
  ----------------------------------------------------------------------------------------------------------

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

Principal lineage: ADR-018; AX-05 state-responsibility reconciliation;
FCAPS review of Mnemosyne.
