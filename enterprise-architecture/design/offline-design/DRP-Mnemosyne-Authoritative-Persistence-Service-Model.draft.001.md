# Design Review Proposal (DRP)

**DRP:** DRP-Mnemosyne-Authoritative-Persistence-Service-Model-001\
**Title:** Mnemosyne Authoritative Persistence Service Model --- Concept
and Governed Persistence\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish Mnemosyne as Harmonia's Information System for governed
authoritative persistence of Information Units, conceptually comprising
a **GovernedPersistenceFramework** and one or more
**PersistenceServices**. Architecture defines what must remain true;
PersistenceService solution design determines how it is made true.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Identity and decomposition

``` text
Mnemosyne
├── GovernedPersistenceFramework
└── PersistenceService [1..*]
```

The **Information Unit** is the governed thing Mnemosyne logically acts
upon. This does not require an InformationUnit-shaped physical
persistence representation or lower technical contract.

The **GovernedPersistenceFramework (GPF)** establishes the common
governance and behavioural envelope for authoritative persistence. A
PersistenceService cannot independently redefine authoritative
persistence.

A **PersistenceService** provides concrete persistence capability within
that envelope. Representation, mapping, persistence model, technology
and mechanisms remain solution-design choices.

### 3.2 First-class aspects

Governance, provenance, audit participation, operational logging and
security/information control are first-class aspects of governed
persistence. They need not be physically embedded as fields of persisted
information. Themis retains authorisation semantics; Kleio retains
authoritative Audit Information Management.

### 3.3 Authoritative state

Technical storage, caching, indexing, replication or acknowledgement
does not by itself establish authoritative acceptance. Provider-native
identifiers, revisions, timestamps and transaction IDs may support
implementation and traceability but do not replace governed
identity/state/version.

Where the information model defines governed state/version progression,
the GPF preserves and enforces that progression as part of authoritative
persistence.

### 3.4 Capability contribution

Persistence is fundamental. A PersistenceService may expose additional
native capabilities, such as rich search. Such capabilities contribute
additively to Mnemosyne and need not be manufactured for unrelated
PersistenceServices.

### 3.5 Outcome semantics

Mnemosyne must distinguish authoritative success, legitimate absence
where applicable, failure and unresolved/uncertain outcome. A provider
response is evidence used to determine the Harmonia outcome; it is not
automatically that outcome.

### 3.6 Invariants

-   Persistence responsibility does not confer semantic ownership of
    persisted information.
-   Active/cache success is not authoritative durable acceptance.
-   Governed identity remains distinct from provider-native identity.
-   Persistence must not weaken applicable information controls.
-   PersistenceService internals remain encapsulated.
-   The Mnemosyne-to-PersistenceService Interaction need not be
    InformationUnit-shaped.

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

1.  Accept `GovernedPersistenceFramework` as the canonical name?
2.  Which historical Mnemosyne capabilities are intrinsic versus
    historical solution requirements?
3.  How should additive PersistenceService capabilities be represented
    without implying universal parity?
4.  What minimum authoritative outcome vocabulary belongs canonically to
    Mnemosyne?

## 9. Continuum Projection

### 9.1 Proposed Projection

  ----------------------------------------------------------------------------------------------------------------------------------------------------------
  Architectural Assertion                        Proposed Continuum Construct                 Treatment          Projection Notes             Status
  ---------------------------------------------- -------------------------------------------- ------------------ ---------------------------- --------------
  Mnemosyne is the governed authoritative        Application Component::Mnemosyne + existing  Extend             Mnemosyne                    Proposed
  persistence system for Information Units       BusinessObject::InformationUnit                                 definition/responsibility;   
                                                                                                                 relate to existing           
                                                                                                                 InformationUnit without      
                                                                                                                 redefining it                

  Mnemosyne comprises GPF +                      Mnemosyne + candidate                        Create/Relate      Likely                       Review
  PersistenceService\[1..\*\]                    GovernedPersistenceFramework + candidate                        component/service/function   
                                                 PersistenceService                                              constructs; exact metamodel  
                                                                                                                 types require vicinity       
                                                                                                                 review                       

  GPF establishes common persistence             GovernedPersistenceFramework                 Create             Definition and               Review
  governance/behaviour envelope                                                                                  responsibility               

  PersistenceService supplies concrete           PersistenceService                           Create             Abstract architectural       Review
  persistence capability                                                                                         construct, not product       
                                                                                                                 instance                     

  Governance/provenance/audit/logging/security   Existing                                     Relate/Constrain   Reuse existing owners; do    Lookup
  are first-class persistence aspects            Governance/Provenance/Kleio/Logging/Themis                      not create duplicate         
                                                 vicinity                                                        concepts                     

  Authoritative success differs from provider    Mnemosyne + authoritative-state/outcome      Extend             Attach to Mnemosyne/GPF and  Lookup
  acknowledgement                                constraints                                                     existing AX-05/AX-15         
                                                                                                                 vicinity                     

  Native identity/version does not replace       Mnemosyne/GPF + InformationUnit              Constrain          Reuse existing               Lookup
  governed identity/version                      identity/version architecture                                   identity/version concepts    

  PersistenceService may contribute additive     PersistenceService + Search                  Relate             Do not manufacture universal Review
  capabilities such as Search                    Management/capability vicinity                                  capability                   
  ----------------------------------------------------------------------------------------------------------------------------------------------------------

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

Principal lineage: current Information Unit architecture; AX-05
active/durable separation; ADR-018; historical Mnemosyne
persistence-management design; current GPF/PersistenceService design
discussion.
