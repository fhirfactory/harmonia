# Design Review Proposal (DRP)

**DRP:** DRP-Mneme-Security-001\
**Title:** Mneme Security --- Concept and Requirements\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish Mneme security and information-control obligations across
Pragma management, Pragmaton contextualisation, active information
access and adhoc acquisition.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Security context

Mneme operates within established authenticated/security context and
must not manufacture authority because context is absent. Missing or
indeterminate protected authority fails closed.

### 3.2 Contextualisation as disclosure

Placing information into a Pragmaton is itself a disclosure/use
decision. It must satisfy applicable requester/executor,
activity/purpose and information-control conditions before business
logic receives the value.

### 3.3 Adhoc access

Permission to execute an Ergon does not grant unrestricted Mneme access.
Adhoc access/search is independently governed. Discovery metadata such
as identifiers, counts, facets and ordering cannot leak protected
existence.

### 3.4 Authority separation

Where originating/requesting and executing process authority are
distinct, Mneme preserves that distinction. Process/service authority
cannot silently amplify what the originating authority may cause or
learn.

### 3.5 Information controls

Compartmentalisation/classification remains effective while information
is cached, contextualised, transformed or returned. Operational security
context may be associated with Pragma/Pragmaton but does not
automatically become persisted Information Unit content.

Themis owns authorisation semantics; Mneme consumes/enforces them as
architecturally assigned.

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

1.  Which decisions occur before contextualisation versus at value
    access?
2.  What security information remains associated with Pragma/Pragmaton?
3.  How are authority changes during long-running activity handled?
4.  Which denial semantics are visible to Ergon logic?

## 9. Continuum Projection

### 9.1 Proposed Projection

  -----------------------------------------------------------------------------------------------------------------------------
  Architectural Assertion      Proposed Continuum Construct             Treatment          Projection Notes      Status
  ---------------------------- ---------------------------------------- ------------------ --------------------- --------------
  Mneme consumes/enforces      Mneme + Themis + authorisation           Relate/Extend      Reuse existing Themis Lookup
  Themis-owned authorisation   constructs                                                  architecture          
  semantics                                                                                                      

  Contextualisation is an      Contextualisation +                      Constrain/Relate   Apply to Pragmaton    Review
  information disclosure/use   authorisation/information-control                           construction          
  decision                     constructs                                                                        

  Adhoc access is              Interaction::Ergon-to-Mneme Adhoc +      Relate/Constrain   Detailed interaction  Proposed
  independently authorised     Themis                                                      separate              

  Requester and executor       Existing                                 Reuse/Relate       Search security       Lookup
  authority remain             Identity/Authorisation/Profile/context                      vicinity before       
  distinguishable              constructs                                                  modelling             

  Compartment/classification   Mneme/Pragmaton +                        Relate/Constrain   Reuse                 Lookup
  controls survive active use  InformationLabel/compartment constructs                     information-control   
                                                                                           architecture          

  Operational security context Security context + InformationUnit       Constrain          Likely existing AX-07 Lookup
  is not automatically IU                                                                  semantics             
  content                                                                                                        
  -----------------------------------------------------------------------------------------------------------------------------

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

Principal lineage: current Authentication & Authorisation Framework
design; historical dual-principal/security-context evidence; current
Pragmaton design.
