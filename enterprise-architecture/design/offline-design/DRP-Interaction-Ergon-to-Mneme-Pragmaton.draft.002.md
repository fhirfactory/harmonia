# Design Review Proposal (DRP)

**DRP:** DRP-Interaction-Ergon-to-Mneme-Pragmaton-001\
**Title:** Interaction::Ergon-to-Mneme --- Pragmaton\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Feature Realisation

This DRP realises the execution-facing boundary of the Information Unit contextualisation/Pragmaton Feature.

### 1.1 New or Updated Feature Details

The Interaction supports the Feature by which Mneme establishes governed working information for Ergon execution and receives resulting working state without making the Ergon responsible for persistence mechanics.

### 1.2 Associated Requirements

- Mneme SHALL contextualise governed information before it is exposed as Pragmaton working state.
- Ergon business logic SHALL operate on the governed working envelope without needing persistence knowledge.
- Egress SHALL return changed working state to Mneme management.
- Changed Pragmaton values and Pragma Output SHALL remain distinguishable.
- Failure/recovery SHALL respect the rule that execution does not resume inside an Ergon.
- Security/provenance/audit context SHALL survive the interaction as required.

### 1.3 Associated Strategy and Motivation

Associate with the Pragmaton/Contextualisation Feature, Data Accessibility, Ergon execution architecture, AX-05 and applicable security/audit requirements.

## 2. Proposal

Establish the governed Interaction through which an Ergon receives, uses
and returns its Mneme-managed Pragmaton, replacing ambiguous noun-level
`Context` while retaining contextualisation semantics.

## 3. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 4. Architectural Definition

### 3.1 Establishment

Before business logic operates, anticipated governed information is
contextualised by Mneme into a Pragmaton appropriate to the activity.
The Ergo framework exposes the already contextualised envelope to
business logic; it does not become the persistence/resource-acquisition
owner.

### 3.2 Use

Ergon business logic reads/acts upon working values without normally
needing persistence mechanics.

### 3.3 Return

At egress, resulting working state returns to Mneme management. This is
not equivalent to 'write these database records'. Mneme determines
subsequent information-management consequences, including authoritative
persistence where required.

Pragma Output remains distinguishable from changed Pragmaton working
values.

### 3.4 Execution semantics

Ergon is bounded and is not resumed internally after failure. Pragmaton
interaction must support, not undermine, that recovery model.

### 3.5 Security/audit

Contextualised information satisfies applicable disclosure controls, and
material access/outcomes participate in the applicable audit model.

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

1.  Retain historical `Load`/`Save` service names?
2.  What outcome semantics apply when contextualisation cannot safely
    complete?
3.  Does Ergon receive the whole Pragmaton or a controlled view?
4.  What egress information is required for Mneme subsequent handling?


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

- Reuse Pragmaton, Pragma and Information Unit concepts.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates

- No Business behaviour merely to mirror loader/unloader mechanics.

### 10.5 Application Layer — Information Architecture Updates

- Define only shared application information semantics needed by the interaction.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation

- Establish/refine the Ergon-to-Mneme Pragmaton Interaction and relate it to Contextualisation and Feature realisation.

### 10.7 Application Layer — Structural / Component Updates

- Reuse Ergon/Ergo and Mneme; loader/unloader/helper classes remain implementation design.

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

Principal lineage: historical Mneme Context Load/Save design; current
Pragmaton naming/design.
