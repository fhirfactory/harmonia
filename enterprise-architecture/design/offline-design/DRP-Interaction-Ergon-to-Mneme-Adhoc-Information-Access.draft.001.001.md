# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access-001\
**Title:** Interaction::Ergon-to-Mneme --- Adhoc Information Access\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish a distinct governed Interaction for Ergon business logic that
discovers during execution a legitimate need for information not already
present in its Pragmaton.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Scope

This Interaction applies to explicit developer-authored runtime
information requests beyond the existing Pragmaton. It is materially
different from platform-controlled contextualisation.

### 3.2 Capability

It may support direct governed information acquisition and
search/discovery, never raw cache/database access.

### 3.3 Security

Ergon execution authority is not universal information authority. Each
request is governed under applicable requester/executor,
activity/purpose and information-control context.

### 3.4 Result handling

Returned information may be contextualised into Pragmaton or supplied as
a bounded result according to the accepted Pragmaton model; this remains
for review.

### 3.5 Cost/design feedback

Adhoc access is legitimate but may be more expensive than anticipated
contextualisation. Repeated predictable adhoc access may suggest
revisiting expected information requirements; it is not itself an
architectural error.

### 3.6 Provenance/audit

Requests and protected disclosures remain correlatable to Pragma,
Pragmaton, Ergon and security context and participate in mandatory audit
rules.

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

1.  Split direct access and search into nested Interactions?
2.  Does acquired information join Pragmaton automatically, explicitly
    or never?
3.  What reproducibility expectations apply if authoritative state
    changes mid-Ergon?
4.  Which outcomes are exposed to developer logic versus framework
    handling?

## 9. Continuum Projection

### 9.1 Proposed Projection

  ------------------------------------------------------------------------------------------------------------
  Architectural Assertion  Proposed Continuum Construct  Treatment          Projection Notes    Status
  ------------------------ ----------------------------- ------------------ ------------------- --------------
  Developer-authored Ergon Interaction::Ergon-to-Mneme   Create             Distinct from       Proposed
  logic may request        (Adhoc Information Access)                       platform            
  information beyond                                                        contextualisation   
  Pragmaton                                                                                     

  Adhoc acquisition may    Nested capability/Interaction Create/Relate      Split only if       Review
  include direct read and  candidates                                       materially distinct 
  search                                                                                        

  Adhoc access uses Mneme  Interaction + Mneme           Relate             No raw              Lookup
  governed information     information-access behaviours                    cache/database      
  services                                                                  access              

  Execution authority is   Interaction + Themis/authz    Constrain/Relate   Reuse security      Lookup
  not universal            constructs                                       architecture        
  information authority                                                                         

  Returned information may Interaction + Pragmaton       Relate             Decision explicitly Review
  join Pragmaton or remain                                                  unresolved          
  bounded result                                                                                

  Access remains           Interaction + Pragma +        Relate             Preserve            Review
  correlated to            Pragmaton + Ergon                                provenance/audit    
  Pragma/Pragmaton/Ergon                                                    lineage             
  ------------------------------------------------------------------------------------------------------------

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

Principal lineage: historical Adhoc Resource Access and Adhoc Search
design; current distinction between platform contextualisation and
developer access.
