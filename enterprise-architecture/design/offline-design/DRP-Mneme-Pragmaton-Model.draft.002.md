# Design Review Proposal (DRP)

**DRP:** DRP-Mneme-Pragmaton-Model-001\
**Title:** Mneme Pragmaton Model --- Concept and Requirements\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-24

## 1. Feature Realisation

This DRP realises the Information Unit management Features concerned with making governed information available as bounded active working information for Harmonia execution.

### 1.1 New or Updated Feature Details

Primary candidate Feature context:

- `DataManagement.InformationUnits.DataAccessibility` — provide authorised access to governed information required for activity.
- `DataManagement.InformationUnits.DataSteadfastness.Availability` — maintain availability of actively managed information appropriate to distributed execution.
- A Feature for **execution contextualisation** is required beneath the appropriate `DataManagement.InformationUnits` Enterprise Capability if the existing Continuum does not already provide one.

The architectural proposition is that Mneme realises this Feature context by contextualising governed information into a bounded **Pragmaton** for Ergon execution.

### 1.2 Associated Requirements

- Harmonia SHALL establish a bounded working information environment for execution without redefining authoritative Information Unit semantics.
- Active working state SHALL remain distinguishable from authoritative durable state.
- Governed identity, authority and version correlation SHALL survive contextualisation.
- Anticipated working information SHALL not prevent explicit governed acquisition of additional information.
- Loss of active working state SHALL NOT imply loss of acknowledged authoritative durable information.
- Changed working values and genuinely new Pragma Output SHALL remain semantically distinguishable.

### 1.3 Associated Strategy and Motivation

Associate the Feature with applicable Data Management/Information Unit capability context, AX-05 active-versus-authoritative state separation, applicable resilience/security/audit axioms and requirements, and the architectural direction that Mneme manages active governed information while Mnemosyne manages authoritative persistence. Existing motivation/strategy constructs are reused where adequate; gaps exposed by this DRP are architecture work for review.

## 2. Proposal

Tentatively establish **Pragmaton** as the Mneme-managed **Data
Execution Envelope** within/associated with a Pragma: the bounded
working information environment contextualised for an Ergon. Use
`contextualise/contextualisation` as the activity rather than generic
`Context` as the canonical noun.

## 3. Design Context

These drafts capture the current Harmonia design position sufficiently
to support review across CLI sessions and temporal gaps. They
deliberately define enduring architectural identity, responsibility,
semantics and constraints rather than attempting an exhaustive
requirements catalogue. Unresolved matters remain explicit for Solution
Architect adjudication.

## 4. Architectural Definition

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

1.  Accept/reject/rename `Pragmaton`.
2.  Containment versus association with Pragma?
3.  Minimum Pragmaton identity/version semantics?
4.  Exact save/unload semantics for changed working values?
5.  Boundary between Mneme contextualisation and Ergo framework
    exposure?


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

- Establish/refine the governed information concept for Pragmaton if accepted, and relate it to Information Unit and Pragma semantics.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates

- No Business behaviour is required solely to mirror Mneme internals; add only if contextualisation represents business/enabling behaviour at this layer.

### 10.5 Application Layer — Information Architecture Updates

- Add/refine application information concepts needed to express Pragmaton semantics, without prescribing implementation DTO/class decomposition.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation

- Establish/refine Contextualisation and related Mneme application behaviour as the Feature realisation; relate adhoc access separately.

### 10.7 Application Layer — Structural / Component Updates

- Reuse Application Component::Mneme and assign/associate the accepted behaviour/services to it; do not prescribe internal Mneme modules/classes.

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

Principal lineage: historical `Context`/`Loaded Context`, Context
Load/Save and Adhoc access design; current decision to tentatively
rename the noun `Pragmaton` and retain contextualisation as the process
term.
