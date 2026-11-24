# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Mnemosyne-to-PersistenceService-001\
**Title:** Interaction::Mnemosyne-to-PersistenceService\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish an intentionally abstract Interaction governing collaboration
between Mnemosyne and a PersistenceService while allowing each service
to realise it according to its underlying persistence solution.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Boundary

The Interaction carries sufficient semantics for Mnemosyne to
request/use persistence capability, correlate provider-native activity
with governed identity/state where necessary, determine authoritative
outcome, apply governance/security/provenance/audit/logging obligations,
use additive capabilities, and recognise
failure/degradation/uncertainty.

### 3.2 Representation freedom

The Interaction is **not required to use Information Unit as its
implementation representation or contract**. Mnemosyne may transform,
decompose or map an Information Unit before interacting with a
PersistenceService. Search may return native
hits/identifiers/projections that Mnemosyne subsequently resolves into
governed results.

### 3.3 Capability exposure

PersistenceServices may expose useful native capabilities such as
search, traversal, indexing, transaction support, version checks or bulk
operations. Mnemosyne may govern/use them without forcing unrelated
services to implement artificial equivalents.

### 3.4 Intentional asymmetry

This lower Interaction protects Harmonia obligations while preserving
implementation freedom. It is intentionally less prescriptive than
`Interaction::Mneme-to-Mnemosyne`, which crosses a Harmonia-owned
semantic boundary.

### 3.5 Invariants

A PersistenceService cannot redefine authoritative persistence. Native
identity/version cannot replace governed identity/version. Provider
success is not automatically Harmonia success. Failure/uncertainty
cannot be silently normalised into success. Applicable information
controls remain effective.

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

1.  Which obligations belong explicitly on this Interaction versus
    Mnemosyne/GPF?
2.  When does an additive provider capability warrant a nested
    Interaction?
3.  What minimum correlation semantics are necessary without prescribing
    a common envelope?

## 9. Continuum Projection

### 9.1 Proposed Projection

  ------------------------------------------------------------------------------------------------------------------------------------------------
  Architectural Assertion          Proposed Continuum Construct                   Treatment          Projection Notes               Status
  -------------------------------- ---------------------------------------------- ------------------ ------------------------------ --------------
  Mnemosyne and PersistenceService Interaction::Mnemosyne-to-PersistenceService   Create             Interaction is architectural,  Proposed
  collaborate across an abstract                                                                     not technical API              
  boundary                                                                                                                          

  Interaction preserves            Interaction + GPF + identity/state/outcome     Relate/Constrain   Reuse established concepts     Lookup
  authoritative                    constructs                                                                                       
  outcome/correlation/governance                                                                                                    
  obligations                                                                                                                       

  Interaction need not be          Interaction constraint                         Extend             Explicit                       Proposed
  InformationUnit-shaped                                                                             non-prescription/constraint;   
                                                                                                     no new information construct   

  Provider capabilities may be     Interaction + PersistenceService capability    Relate             Nested Interaction only where  Review
  exposed additively               relationships                                                     materially distinct            

  Provider native identity/version Interaction + identity/version constructs      Constrain          Reuse InformationUnit          Lookup
  may correlate but not redefine                                                                     identity/version architecture  
  governed identity                                                                                                                 
  ------------------------------------------------------------------------------------------------------------------------------------------------

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

Principal lineage: current Mnemosyne conceptual design; historical
persistence-service diagrams and oversight design.
