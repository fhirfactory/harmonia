# Design Review Proposal (DRP)

**DRP:** DRP-Mneme-Audit-001\
**Title:** Mneme Audit --- Concept and Requirements\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish Mneme's contribution to authoritative audit evidence for
Pragma management, Pragmaton contextualisation, governed information
access and architecturally significant active-state transitions while
preserving Kleio's ownership of Audit Information Management.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Ownership and meaning

Kleio owns authoritative Audit Information Management. Mneme contributes
architecturally meaningful events/evidence; it does not create a second
audit authority.

Candidate event families include material Pragmaton
contextualisation/disclosure, governed protected access, adhoc
access/search, Pragma Input/Output crossing governed boundaries,
material security denial/outcome, and exceptional harmonisation
initiation. Exact taxonomy remains for review.

### 3.2 Correlation

Audit contribution retains sufficient identity,
Pragma/Pragmaton/activity correlation, information identity/version
where relevant, requester/executor context, outcome and time to make
evidence meaningful.

### 3.3 Separation

Audit Evidence is distinct from operational logs, traces/spans, Pragma
progression metadata, Information Unit provenance and assurance
evidence. The same real-world occurrence may contribute to multiple
concerns without collapsing their meanings.

### 3.4 Failure

Where audit is mandatory for an operation, inability to establish
required durable audit evidence follows Harmonia's fail-closed audit
rules rather than silently degrading to local logging.

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

1.  Which Mneme events are mandatory audit events?
2.  Is Pragmaton construction itself audited or only resulting
    disclosure/access?
3.  Which denied/failed searches require durable evidence?
4.  What audit obligations attach to Resource Harmonisation?

## 9. Continuum Projection

### 9.1 Proposed Projection

  ------------------------------------------------------------------------------------------------------------------
  Architectural Assertion        Proposed Continuum Construct    Treatment          Projection Notes  Status
  ------------------------------ ------------------------------- ------------------ ----------------- --------------
  Kleio remains authoritative    Kleio + Audit Information       Reuse              No duplicate      Proposed
  Audit Information Management   Management                                         audit authority   
  owner                                                                                               

  Mneme contributes meaningful   Mneme → Kleio/Audit             Relate/Extend      Search existing   Lookup
  audit events/evidence          interaction/service vicinity                       audit             
                                                                                    contribution      
                                                                                    constructs        

  Pragmaton                      Pragmaton/Contextualisation +   Relate             Mandatory event   Review
  contextualisation/disclosure   Audit constructs                                   set remains       
  may be auditable                                                                  review matter     

  Adhoc access/search may be     Adhoc Interaction + Audit       Relate             Event taxonomy    Review
  auditable                      constructs                                         unresolved        

  Pragma Input/Output boundary   Pragma Input/Output + Audit     Relate             Only              Review
  events may be auditable        constructs                                         architecturally   
                                                                                    significant       
                                                                                    events            

  Audit is distinct from         Audit + Logging + Provenance +  Constrain/Relate   Reuse existing    Lookup
  logging/provenance/assurance   Dokimasia constructs                               separations       
  ------------------------------------------------------------------------------------------------------------------

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

Principal lineage: current Audit Framework; Kleio ownership; historical
Mneme Context/Pragma/checkpoint concepts.
