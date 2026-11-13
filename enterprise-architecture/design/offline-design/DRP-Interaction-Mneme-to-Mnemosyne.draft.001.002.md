# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Mneme-to-Mnemosyne-002\
**Title:** Interaction::Mneme-to-Mnemosyne\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Feature Realisation

This DRP realises the Feature boundary between active governed Information Unit management and authoritative persistence/acquisition.

### 1.1 New or Updated Feature Details

Primary candidate Feature context includes `DataManagement.InformationUnits.DataAccessibility`, `DataManagement.InformationUnits.DataSteadfastness.Recovery` and the Authoritative Persistence Feature established/refined for Mnemosyne. The Interaction exists because Mneme's active information-management Features require authoritative services without acquiring persistence ownership.

### 1.2 Associated Requirements

- The interaction SHALL preserve Information Unit governed identity/state/version semantics.
- Read, write and search semantics SHALL preserve authorisation, disclosure, provenance and audit obligations.
- Active-state success SHALL NOT be treated as authoritative durable acceptance.
- Authoritative outcomes SHALL distinguish legitimate absence, denial, failure and uncertainty where applicable.
- Search SHALL remain semantically distinct from simple CRUD access where its contract differs materially.

### 1.3 Associated Strategy and Motivation

Associate with AX-05, Themis authorisation, Kleio audit, Information Unit management, Data Accessibility and Authoritative Persistence architecture. This is a Harmonia-owned boundary and may therefore be architecturally more prescriptive than the Mnemosyne-to-PersistenceService boundary.

## 2. Proposal

Establish the governed Interaction through which Mneme uses Mnemosyne's
authoritative persistence and authoritative information-acquisition
capabilities. This Harmonia-owned boundary is intentionally more
prescriptive than the PersistenceService boundary.

## 3. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 4. Architectural Definition

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

## 5. Explicitly Not Prescribed

Unless explicitly stated otherwise, this proposal does not prescribe
Java classes/interfaces, DTOs, physical schemas, products, frameworks,
protocols, deployment topology, algorithms or other internal
implementation structures. Those remain solution-design space subject to
the accepted architectural semantics and constraints.

## 6. Evidence and Rationale

The proposal synthesises current governed Harmonia architecture, bounded
historical design evidence and the current Solution Architect /
architecture-peer design discussion. Historical implementation and
Legacy-RAD material are discovery evidence rather than independent
current authority.

## 7. Relationship to Current Architecture

If accepted, this proposal should be reconciled into the Architecture
Continuum using existing constructs wherever adequate. Any inability to
represent material semantics should be surfaced as a modelling gap
rather than hidden in implementation or free text.

## 8. Conformance Expectations

A conformant solution preserves the responsibilities, semantic
distinctions and invariants in this proposal while retaining
implementation freedom. Implementation technology alone neither
establishes nor disproves architectural conformance.

## 9. Open Questions / Matters for Review

1.  Are Read/Write/Search the right first nested Interaction boundaries?
2.  Which write lifecycle distinctions are architecturally material?
3.  What result-set semantics belong here versus broader Information
    Unit/Search architecture?
4.  Which audit events are mandatory at this seam?


## 10. Continuum Update

The accepted DRP is to be integrated as a connected architectural delta. The entries below are architecture decisions/proposals for review, not instructions for the Technical Lead to invent missing architecture.

### 10.1 Motivation Updates

- Associate the Feature realisation with the Requirements, Axioms, Principles, Constraints, Goals/Outcomes and other Motivation elements identified in Section 1.
- Add/refine any requirement made explicit by this DRP only after architectural review; do not delegate requirement discovery to implementation.
- Preserve existing Motivation elements unchanged where they already express the required semantics.

### 10.2 Strategy Updates

- Establish/refine the Enterprise Capability and Feature context identified in Section 1.
- Map the Enterprise Capability/Features to the applicable Business and Business-Enabling capability context.
- Associate applicable Courses of Action and other Strategy elements.
- If no defensible Feature and Enterprise Capability placement can be established, stop and reconsider the architectural justification for the DRP.

### 10.3 Business Layer — Information Architecture Updates

- Reuse Information Unit and related governed-information concepts.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates

- No new Business behaviour solely to mirror the application interaction.

### 10.5 Application Layer — Information Architecture Updates

- Define shared application information semantics for read/write/search/outcomes only where required by the governed boundary.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation

- Establish/refine `Interaction::Mneme-to-Mnemosyne` and materially distinct nested interactions/services where their contracts differ architecturally.

### 10.7 Application Layer — Structural / Component Updates

- Reuse Mneme and Mnemosyne and connect the interaction to both; no client/gateway/adapter classes are architecture.

### 10.8 Cross-Layer and Cross-Feature Relationships

- Connect the application realisation to the Feature(s) it realises/supports rather than leaving application constructs isolated.
- Preserve explicit relationships to applicable Requirements, Axioms, Courses of Action and information concepts.
- Establish Access, Realisation, Serving, Assignment, Association, Influence or other native/specialised relationships according to their actual semantics; do not use generic Association where a stronger governed relationship is known.
- Connect related Features where the design depends on another Feature's services or information rather than duplicating that capability locally.

### 10.9 Explicitly Unaffected / No-Change Architecture

- Implementation classes, interfaces, DTOs, algorithms, products, deployment topology and internal module decomposition remain Technical Lead solution-design space unless explicitly elevated by this DRP.
- Existing architecture referenced only as context is not modified unless this section explicitly says so.

## 11. Review Decision

**Decision:** Pending\
**Decision authority:** Solution Architect / Architecture Review\
**Decision notes:** (To be completed during review.)

## 12. Post-Decision Actions

(To be completed after review.)

## Appendix A --- Design Provenance

This appendix is informative, not normative.

Principal lineage: previous Mneme-to-Mnemosyne DRP; Information Unit,
Authentication/Authorisation and Audit design; current
GPF/PersistenceService design.
