# Design Review Proposal (DRP)

**DRP:** DRP-Mnemosyne-Data-Persistence-Sharding-001\
**Title:** Mnemosyne Data Persistence Sharding --- Concept and
Requirements\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish sharding as a governed Mnemosyne persistence-management
concern while separating semantic partitionability from physical
partitioning/routing mechanisms.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Concept

A **shard** is a managed partition of authoritative persistence used to
distribute information or persistence responsibility while preserving
governed meaning and obligations.

Mnemosyne owns governed shard management/routing. It does **not**
independently decide that an information domain is semantically safe to
shard; that permission comes from applicable information/governance
architecture.

### 3.2 Semantic invariance

Sharding must not change Information Unit identity/meaning, information
authority, provenance, compartments/classification, authoritative
version semantics, audit obligations or authorised consumer-visible
meaning.

### 3.3 PersistenceService relationship

Shard and PersistenceService are not synonyms. A shard may be realised
by one or more services; a service may support multiple shards. Native
provider partitioning may be used where it satisfies Harmonia semantics.

### 3.4 Domains and resilience

Clinical Information, Configuration & Governance, and Operations &
Assurance are semantic persistence-responsibility domains, not
instructions to create exactly three databases or shards. Configuration
& Governance must not acquire divergent governance authority merely
through partitioning. Operations & Assurance may be horizontally
partitioned where governed meaning is preserved.

Shard placement/routing inherits Mnemosyne availability/recovery
obligations.

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

1.  What construct authorises semantic partitionability?
2.  Is shard membership itself governed durable metadata?
3.  Which re-sharding invariants must be architectural?
4.  Are any domains explicitly prohibited from divergent partitioning?

## 9. Continuum Projection

### 9.1 Proposed Projection

  --------------------------------------------------------------------------------------------------------------------------------------------
  Architectural Assertion                                      Proposed Continuum     Treatment          Projection Notes       Status
                                                               Construct                                                        
  ------------------------------------------------------------ ---------------------- ------------------ ---------------------- --------------
  Mnemosyne owns governed shard management                     Mnemosyne + Shard      Extend/Reuse       Historical construct   Lookup
                                                               Management                                likely exists;         
                                                               behaviour/capability                      traverse first         
                                                               vicinity                                                         

  Shard is managed authoritative-persistence partition         Candidate Topic::Shard Reuse/Create       Information concept    Review
                                                               or existing Shard                         only if no adequate    
                                                               concept                                   construct exists       

  Semantic authority determines whether information may be     Shard Management +     Constrain/Relate   Mnemosyne does not     Review
  sharded                                                      information governance                    invent semantic safety 
                                                               constructs                                                       

  Sharding preserves                                           Shard/Sharding +       Constrain          Relationships to       Lookup
  identity/authority/provenance/classification/version/audit   existing governed                         existing concepts      
                                                               information constructs                    rather than duplicates 

  Shard and PersistenceService are distinct                    Shard +                Relate/Constrain   Cardinality/topology   Review
                                                               PersistenceService                        deliberately open      

  Persistence responsibility domains do not prescribe topology Existing               Extend/Constrain   Reuse if present       Lookup
                                                               Clinical/C&G/O&A                                                 
                                                               persistence-domain                                               
                                                               constructs                                                       
  --------------------------------------------------------------------------------------------------------------------------------------------

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

Principal lineage: historical Mnemosyne shard-management design; current
PersistenceService and HA/DR design.
