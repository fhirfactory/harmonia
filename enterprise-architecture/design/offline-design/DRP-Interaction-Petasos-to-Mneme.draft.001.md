# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Petasos-to-Mneme-001\
**Title:** Interaction::Petasos-to-Mneme\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish the architectural Interaction between Petasos durable work
handoff and Mneme governed Pragma/information management while
preserving the distinction between work acceptance and authoritative
information persistence.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Boundary

Petasos owns durable transfer/handoff between independently recoverable
processing activities. Mneme owns Pragma and governed active information
management.

### 3.2 Work acceptance

A durable Petasos transition may establish durable **work acceptance**
where state necessary to complete/reconstruct processing is recoverable.
It does not establish authoritative committed application state;
Mnemosyne retains that responsibility.

### 3.3 Pragma relationship

Petasos may carry sufficient information/reference/correlation to
identify or reconstruct the relevant Pragma. It does not thereby own
Pragma lifecycle, nor must its transport message be identical to Pragma.

### 3.4 Reconstruction and lineage

On receipt, Mneme can establish the governed Pragma and subsequent
Pragmaton required for execution using authoritative/recoverable
information. Work, causation, correlation, security and provenance
context remain sufficient to preserve lineage across handoff.

### 3.5 Failure

Duplicate delivery, delayed delivery, redelivery and transport
uncertainty are normal. The Interaction does not assume Ergon
idempotence and does not resume inside an Ergon.

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

1.  What minimum recoverable state crosses a Petasos boundary?
2.  Is Pragma reconstructed by value, identity/reference or either?
3.  What security-context continuity is mandatory?
4.  Where is duplicate/redelivery adjudication owned relative to Ponos
    and Mneme?

## 9. Continuum Projection

### 9.1 Proposed Projection

  --------------------------------------------------------------------------------------------------------------------------------------
  Architectural Assertion                          Proposed Continuum Construct    Treatment          Projection Notes    Status
  ------------------------------------------------ ------------------------------- ------------------ ------------------- --------------
  Petasos and Mneme collaborate across durable     Interaction::Petasos-to-Mneme   Create             Canonical           Proposed
  handoff/information-management seam                                                                 Interaction         
                                                                                                      candidate           

  Petasos owns durable work acceptance/handoff     Petasos                         Reuse/Extend       Reuse existing      Proposed
                                                                                                      responsibility      

  Mneme owns Pragma management on receiving side   Mneme + Pragma                  Relate             Reuse Pragma DRP    Proposed
                                                                                                      result              

  Transport representation need not equal Pragma   Interaction constraint          Extend             Non-prescription;   Proposed
                                                                                                      no duplicate        
                                                                                                      transport model     

  Handoff preserves                                Interaction + existing          Relate/Constrain   Traverse vicinity   Lookup
  work/causation/correlation/security/provenance   identity/provenance/security                                           
  lineage                                          constructs                                                             

  Petasos durable work acceptance does not equal   Petasos + Mnemosyne +           Constrain/Relate   Reuse existing      Proposed
  authoritative IU persistence                     AX-05/ADR-018 semantics                            distinction         

  Duplicate/redelivery/uncertainty are normal      Interaction +                   Relate/Constrain   Ownership details   Review
  conditions                                       uncertainty/recovery constructs                    unresolved          
  --------------------------------------------------------------------------------------------------------------------------------------

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

Principal lineage: ADR-018 durable work-acceptance distinction; Pragma
responsibility design; historical transport implementation only as
discovery evidence.
