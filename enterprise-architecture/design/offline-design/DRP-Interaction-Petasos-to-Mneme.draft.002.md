# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Petasos-to-Mneme-001\
**Title:** Interaction::Petasos-to-Mneme\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Feature Realisation

This DRP realises the boundary between durable work handoff and Mneme-owned Pragma/information management.

### 1.1 New or Updated Feature Details

The Interaction supports Enterprise Features concerned with durable work transfer/recovery and Pragma management while preserving the separation from Information Unit authoritative persistence. It should map to both the applicable work-management capability context and the Data Management/Information Unit context.

### 1.2 Associated Requirements

- Petasos durable work acceptance SHALL remain distinguishable from authoritative Information Unit persistence.
- Sufficient identity/correlation/provenance/security context SHALL survive handoff to reconstruct/manage the relevant Pragma.
- Transport representation SHALL NOT be required to equal the canonical Pragma representation.
- Duplicate/redelivery SHALL be safely handled without assuming Ergon idempotence.
- Recovery SHALL not resume inside an Ergon.
- Mneme SHALL retain Pragma information-management ownership after handoff.

### 1.3 Associated Strategy and Motivation

Associate with durable work/recovery Features, Pragma Management, Data Steadfastness Recovery, AX-05/ADR-018 semantics and applicable security/provenance requirements.

## 2. Proposal

Establish the architectural Interaction between Petasos durable work
handoff and Mneme governed Pragma/information management while
preserving the distinction between work acceptance and authoritative
information persistence.

## 3. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 4. Architectural Definition

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

1.  What minimum recoverable state crosses a Petasos boundary?
2.  Is Pragma reconstructed by value, identity/reference or either?
3.  What security-context continuity is mandatory?
4.  Where is duplicate/redelivery adjudication owned relative to Ponos
    and Mneme?


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

- Reuse Pragma, Information Unit and work/correlation information concepts.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates

- Relate durable work handoff and Pragma-management behaviours to their Business/Enabling capability context.

### 10.5 Application Layer — Information Architecture Updates

- Define stable handoff/correlation semantics only where needed across the application boundary.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation

- Establish/refine Petasos-to-Mneme Interaction and relate it to durable work and Pragma-management Features.

### 10.7 Application Layer — Structural / Component Updates

- Reuse Petasos and Mneme; transport adapters/serialisers/handlers remain implementation design.

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

Principal lineage: ADR-018 durable work-acceptance distinction; Pragma
responsibility design; historical transport implementation only as
discovery evidence.
