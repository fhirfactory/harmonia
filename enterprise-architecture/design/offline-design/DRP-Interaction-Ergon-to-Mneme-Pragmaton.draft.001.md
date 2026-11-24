# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Ergon-to-Mneme-Pragmaton-001\
**Title:** Interaction::Ergon-to-Mneme --- Pragmaton\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish the governed Interaction through which an Ergon receives, uses
and returns its Mneme-managed Pragmaton, replacing ambiguous noun-level
`Context` while retaining contextualisation semantics.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Establishment

Before business logic operates, anticipated governed information is
contextualised by Mneme into a Pragmaton appropriate to the activity.
The Ergo framework exposes the already contextualised envelope to
business logic; it does not become the persistence/resource-acquisition
owner.

### 3.2 Use

Ergon business logic reads/acts upon working values without normally
needing persistence mechanics.

### 3.3 Return

At egress, resulting working state returns to Mneme management. This is
not equivalent to 'write these database records'. Mneme determines
subsequent information-management consequences, including authoritative
persistence where required.

Pragma Output remains distinguishable from changed Pragmaton working
values.

### 3.4 Execution semantics

Ergon is bounded and is not resumed internally after failure. Pragmaton
interaction must support, not undermine, that recovery model.

### 3.5 Security/audit

Contextualised information satisfies applicable disclosure controls, and
material access/outcomes participate in the applicable audit model.

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

1.  Retain historical `Load`/`Save` service names?
2.  What outcome semantics apply when contextualisation cannot safely
    complete?
3.  Does Ergon receive the whole Pragmaton or a controlled view?
4.  What egress information is required for Mneme subsequent handling?

## 9. Continuum Projection

### 9.1 Proposed Projection

  -------------------------------------------------------------------------------------------------------------
  Architectural Assertion        Proposed Continuum Construct  Treatment          Projection     Status
                                                                                  Notes          
  ------------------------------ ----------------------------- ------------------ -------------- --------------
  Ergon and Mneme interact for   Interaction::Ergon-to-Mneme   Create             Canonical      Proposed
  Pragmaton                      (Pragmaton)                                      Interaction    
  contextualisation/use/return                                                    candidate      

  Mneme contextualises before    Interaction +                 Relate             Ergo loader    Review
  business logic                 Contextualisation behaviour +                    exposes; does  
                                 Pragmaton                                        not own        
                                                                                  acquisition    

  Ergon acts upon Pragmaton      Ergon + Pragmaton             Relate/Constrain   Reuse Ergon    Proposed
  without persistence mechanics                                                   construct      

  Egress returns working state   Interaction + Mneme +         Relate             Not equivalent Review
  to Mneme management            Pragmaton                                        to persistence 
                                                                                  write          

  Pragma Output remains distinct Interaction + Pragma Output + Constrain          Depends on     Review
  from Pragmaton changes         Pragmaton                                        Pragma         
                                                                                  projection     

  Failure does not resume inside Interaction + Ergon           Relate/Constrain   Reuse          Proposed
  Ergon                          constraint                                       established    
                                                                                  execution rule 
  -------------------------------------------------------------------------------------------------------------

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

Principal lineage: historical Mneme Context Load/Save design; current
Pragmaton naming/design.
