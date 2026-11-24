# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access-001\
**Title:** Interaction::Ergon-to-Mneme --- Adhoc Information Access\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Feature Realisation

This DRP realises the dynamic/adhoc aspect of `DataManagement.InformationUnits.DataAccessibility` when Ergon business logic requires governed information beyond the anticipated Pragmaton.

### 1.1 New or Updated Feature Details

The Feature is distinct from normal Pragmaton contextualisation because the information need arises during developer-authored business logic. The Interaction may support direct acquisition and search while remaining inside Mneme's governed information-management boundary.

### 1.2 Associated Requirements

- Adhoc information access SHALL be explicit and governed.
- Authority to execute an Ergon SHALL NOT imply authority to access arbitrary information.
- Acquisition/search SHALL preserve applicable identity, security, provenance and audit semantics.
- Business logic SHALL NOT bypass Mneme to access cache/persistence mechanisms directly.
- Returned information SHALL remain correlated to the requesting activity and its Pragma/Pragmaton context.

### 1.3 Associated Strategy and Motivation

Associate with Data Accessibility, Themis authorisation, Kleio audit, Pragmaton contextualisation and applicable search/information-management architecture.

## 2. Proposal

Establish a distinct governed Interaction for Ergon business logic that
discovers during execution a legitimate need for information not already
present in its Pragmaton.

## 3. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 4. Architectural Definition

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

1.  Split direct access and search into nested Interactions?
2.  Does acquired information join Pragmaton automatically, explicitly
    or never?
3.  What reproducibility expectations apply if authoritative state
    changes mid-Ergon?
4.  Which outcomes are exposed to developer logic versus framework
    handling?


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

- Reuse Information Unit, Pragmaton and search-result information concepts.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates

- No Business behaviour solely to mirror the application call; relate to information-access Business/Enabling capability where appropriate.

### 10.5 Application Layer — Information Architecture Updates

- Define stable request/result semantics only where architecturally required.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation

- Establish/refine the Adhoc Information Access Interaction; split acquisition/search only if contracts/constraints materially differ.

### 10.7 Application Layer — Structural / Component Updates

- Reuse Ergon/Ergo and Mneme; repository/client/helper implementation remains Technical Lead design.

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

Principal lineage: historical Adhoc Resource Access and Adhoc Search
design; current distinction between platform contextualisation and
developer access.
