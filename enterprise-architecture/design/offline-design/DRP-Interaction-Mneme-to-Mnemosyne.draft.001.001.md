# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Mneme-to-Mnemosyne-002\
**Title:** Interaction::Mneme-to-Mnemosyne\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Proposal

Establish the governed Interaction through which Mneme uses Mnemosyne's
authoritative persistence and authoritative information-acquisition
capabilities. This Harmonia-owned boundary is intentionally more
prescriptive than the PersistenceService boundary.

## 2. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 3. Architectural Definition

### 3.1 Candidate capability families

Initial capability families are **Read**, **Write** and **Search**.
Write may distinguish CREATE, UPDATE and DELETE/lifecycle operations
where semantics materially differ.

At this boundary, operations use governed information semantics centred
on Information Units and applicable identity, state, version, authority
and management context.

### 3.2 Read

Read distinguishes authoritative information found, legitimate absence,
denial/non-disclosure, failure and unresolved outcome where applicable.
Absence is not persistence failure.

### 3.3 Write

Write requests authoritative durable state establishment/progression.
Mnemosyne determines authoritative outcome. Mneme active-state success
cannot substitute for durable acceptance.

### 3.4 Search

Search is discovery, not simply a fifth CRUD verb. Mneme owns the
Harmonia-facing semantic search requirement and governed Search Result
Set. Mnemosyne owns authoritative persistence-search execution and may
use additive PersistenceService search capability.

Permission to invoke search and permission to learn each
candidate/result are distinct. Result metadata cannot bypass information
controls.

### 3.5 Security / provenance / audit

Applicable authenticated identity and authorisation context participate
without automatically becoming persisted Information Unit content.
Themis owns authorisation semantics; Kleio owns Audit Information
Management. Operations preserve sufficient provenance/audit correlation
for meaningful access, disclosure and state transition.

### 3.6 Invariants

Mneme does not depend on provider/database internals. Mnemosyne does not
expose native persistence objects as the Harmonia contract.
Authoritative state/version remains Mnemosyne's responsibility. Search
does not assume Mneme cache is the complete authoritative search space.

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

1.  Are Read/Write/Search the right first nested Interaction boundaries?
2.  Which write lifecycle distinctions are architecturally material?
3.  What result-set semantics belong here versus broader Information
    Unit/Search architecture?
4.  Which audit events are mandatory at this seam?

## 9. Continuum Projection

### 9.1 Proposed Projection

  ---------------------------------------------------------------------------------------------------------------------------------------
  Architectural Assertion                      Proposed Continuum Construct      Treatment          Projection Notes       Status
  -------------------------------------------- --------------------------------- ------------------ ---------------------- --------------
  Mneme and Mnemosyne have a governed          Interaction::Mneme-to-Mnemosyne   Create/Extend      Existing               Lookup
  Harmonia-owned interaction                                                                        draft/scaffolding may  
                                                                                                    already exist          

  Read is a materially distinct governed       Nested Interaction::Read          Create/Relate      Exact                  Review
  contract                                                                                          interaction/service    
                                                                                                    modelling requires     
                                                                                                    review                 

  Write is a materially distinct governed      Nested Interaction::Write         Create/Relate      CREATE/UPDATE/DELETE   Review
  contract                                                                                          only nested if         
                                                                                                    semantics justify      

  Search is discovery distinct from CRUD       Nested Interaction::Search +      Create/Relate      Reuse Search           Lookup
                                               existing Search constructs                           Management/Search      
                                                                                                    Result Set where       
                                                                                                    present                

  Boundary operates in Information Unit        Interaction + InformationUnit     Relate             Reuse existing         Proposed
  semantics                                                                                         InformationUnit        

  Themis authorisation applies to invocation   Interaction + Themis/authz        Relate/Constrain   Reuse current security Lookup
  and disclosure                               constructs                                           architecture           

  Kleio owns audit while interaction           Interaction + Kleio/Audit         Relate             Do not duplicate Audit Lookup
  contributes evidence                         constructs                                           Information Management 

  Outcomes distinguish                         Interaction + outcome/uncertainty Constrain          Reuse AX-15/outcome    Lookup
  success/absence/denial/failure/uncertainty   constructs                                           vicinity               
  ---------------------------------------------------------------------------------------------------------------------------------------

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

Principal lineage: previous Mneme-to-Mnemosyne DRP; Information Unit,
Authentication/Authorisation and Audit design; current
GPF/PersistenceService design.
