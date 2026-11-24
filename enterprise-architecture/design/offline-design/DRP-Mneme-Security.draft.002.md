# Design Review Proposal (DRP)

**DRP:** DRP-Mneme-Security-001\
**Title:** Mneme Security --- Concept and Requirements\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Feature Realisation

This DRP realises the security and information-control obligations that constrain Mneme's Information Unit accessibility and contextualisation Features.

### 1.1 New or Updated Feature Details

Primary Feature context is `DataManagement.InformationUnits.DataAccessibility` plus the existing Enterprise Capability/Features for authentication, authorisation and information control. This DRP does not create a parallel Mneme security capability; it defines how Mneme participates in realising existing security Features while managing governed information.

### 1.2 Associated Requirements

- Information disclosure/use through Mneme SHALL be authorised.
- Contextualisation and adhoc access SHALL preserve applicable information labels/compartments and authority constraints.
- Authority to execute an Ergon SHALL NOT imply unrestricted authority to access information.
- Requester, executor and effective authority SHALL remain distinguishable where required.
- Security context used for enforcement SHALL not automatically become durable Information Unit content.
- Failure to establish required authorisation SHALL fail closed.

### 1.3 Associated Strategy and Motivation

Associate with Themis-owned authentication/authorisation architecture, Information Label/compartmentalisation, applicable security axioms/requirements and the Data Accessibility Feature. Existing security architecture is reused rather than recreated inside Mneme.

## 2. Proposal

Establish Mneme security and information-control obligations across
Pragma management, Pragmaton contextualisation, active information
access and adhoc acquisition.

## 3. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 4. Architectural Definition

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

1.  Which decisions occur before contextualisation versus at value
    access?
2.  What security information remains associated with Pragma/Pragmaton?
3.  How are authority changes during long-running activity handled?
4.  Which denial semantics are visible to Ergon logic?


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

- Reuse Identity, Authorisation, Information Label/compartment and Information Unit concepts.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates

- Relate Feature-scoped information-management behaviour to existing security/authorisation Business/Enabling services rather than creating Mneme-specific security duplicates.

### 10.5 Application Layer — Information Architecture Updates

- Add application security-context information only where it is a stable cross-boundary contract; do not persist operational enforcement context by default.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation

- Constrain Mneme contextualisation/access interactions with Themis-owned authorisation and information-control services.

### 10.7 Application Layer — Structural / Component Updates

- Reuse Mneme and Themis; do not create internal security modules as architecture.

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

Principal lineage: current Authentication & Authorisation Framework
design; historical dual-principal/security-context evidence; current
Pragmaton design.
