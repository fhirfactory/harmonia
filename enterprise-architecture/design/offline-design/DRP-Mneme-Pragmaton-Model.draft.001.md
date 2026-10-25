# Design Review Proposal (DRP)

**DRP:** DRP-Mneme-Pragmaton-Model-001\
**Title:** Mneme Pragmaton Model --- Concept and Requirements\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Tentatively establish **Pragmaton** as the Mneme-managed **Data
Execution Envelope** within/associated with a Pragma: the bounded
working information environment contextualised for an Ergon. Use
`contextualise/contextualisation` as the activity rather than generic
`Context` as the canonical noun.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Identity

**Pragmaton** is the tentative canonical name for the managed Data
Execution Envelope established by Mneme for bounded activity.

### 3.2 Contextualisation

**Contextualisation** is the Mneme-managed process of selecting,
acquiring, mapping, correlating and making governed information
available within a Pragmaton. An Information Unit may be contextualised
without losing governed identity, authority or authoritative version
semantics.

### 3.3 Working state

Pragmaton is active working state. It may contain values derived from
authoritative Information Units and may evolve during bounded Ergon
execution. Pragmaton state/version and constituent Information Unit
authoritative state/version are distinct.

It does not imply a globally transactionally consistent distributed
snapshot unless a specific contract requires one.

### 3.4 Expected and adhoc information

Pragmaton represents anticipated/bounded working information for
efficient execution, not a closed universe. Runtime needs beyond it use
explicit governed Adhoc Information Access.

### 3.5 Output distinction

Changed Pragmaton working values and genuinely new Content expressed as
Pragma Output remain semantically distinguishable.

### 3.6 Invariants

Pragmaton is managed by Mneme and is non-authoritative active state
unless separately committed through authoritative mechanisms. Loss of
Pragmaton cannot imply loss of acknowledged authoritative durable
information. Constituent identity/version remains traceable.

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

1.  Accept/reject/rename `Pragmaton`.
2.  Containment versus association with Pragma?
3.  Minimum Pragmaton identity/version semantics?
4.  Exact save/unload semantics for changed working values?
5.  Boundary between Mneme contextualisation and Ergo framework
    exposure?

## 9. Continuum Projection

### 9.1 Proposed Projection

  ----------------------------------------------------------------------------------------------------------------
  Architectural         Proposed Continuum Construct  Treatment          Projection Notes           Status
  Assertion                                                                                         
  --------------------- ----------------------------- ------------------ -------------------------- --------------
  Pragmaton is the      Topic::Pragmaton              Create             New candidate canonical    Review
  managed Data                                                           Topic                      
  Execution Envelope                                                                                

  Mneme manages         Mneme → Pragmaton             Relate             Exact relationship type    Review
  Pragmaton                                                              requires review            

  Pragmaton is          Pragma ↔ Pragmaton            Relate             Composition vs other       Review
  within/associated                                                      relationship unresolved    
  with Pragma                                                                                       

  Contextualisation     Contextualisation behaviour   Reuse/Create       Search for existing        Lookup
  establishes/manages   vicinity                                         Context                    
  working information                                                    Load/managed-information   
                                                                         behaviour first            

  Information Units may InformationUnit ↔ Pragmaton / Relate             Do not redefine            Review
  be contextualised     Contextualisation                                InformationUnit            
  into Pragmaton                                                                                    

  Pragmaton             Pragmaton + InformationUnit + Constrain/Relate   Reuse active/durable state Lookup
  state/version differs AX-05                                            architecture               
  from IU authoritative                                                                             
  state/version                                                                                     

  Adhoc needs use       Pragmaton +                   Relate             Detailed interaction       Proposed
  governed Mneme access Interaction::Ergon-to-Mneme                      separately owned           
                        Adhoc Information Access                                                    

  Changed working       Pragmaton + Pragma Output     Constrain/Relate   Reuse/create Pragma Output Review
  values differ from                                                     based on Pragma DRP        
  Pragma Output                                                                                     
  ----------------------------------------------------------------------------------------------------------------

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

Principal lineage: historical `Context`/`Loaded Context`, Context
Load/Save and Adhoc access design; current decision to tentatively
rename the noun `Pragmaton` and retain contextualisation as the process
term.
