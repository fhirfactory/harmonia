# Design Review Proposal (DRP)

**DRP:** DRP-Mneme-Pragmaton-Model-001\
**Title:** Mneme Pragmaton Model --- Concept and Requirements\
**Status:** Draft\
**Prepared for:** Architecture Review\
**Prepared by:** ChatGPT, architecture peer\
**Solution Architect:** Mark Hunter\
**Date:** 2026-11-21
**Revision:** Proposed `draft.000.003`, derived from `draft.002` (preserves the source draft; for Solution Architect review)

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
- Ergon SHALL access its contextualised working information through the managed `Ergon.WorkSpace` boundary, rather than needing direct persistence or cache mechanics.
- Additional information acquisition from Mneme SHALL remain mediated through the `Ergon-InformationAdapter` and applicable Themis PEP/PDP controls; possession of a reference alone confers no access right.
- Pragmaton SHALL remain logically distinct from the Mneme Workflow Cache, other caches, Pragma/Pragmatosis, and authoritative Information Units.

### 1.3 Associated Strategy and Motivation

Associate the Feature with applicable Data Management/Information Unit capability context, AX-05 active-versus-authoritative state separation, applicable resilience/security/audit axioms and requirements, and the architectural direction that Mneme manages active governed information while Mnemosyne manages authoritative persistence. Existing motivation/strategy constructs are reused where adequate; gaps exposed by this DRP are architecture work for review.

## 2. Proposal

Tentatively establish **Pragmaton** as the Mneme-managed **Data
Execution Envelope** within/associated with a Pragma: the bounded
working information environment contextualised for an Ergon. Use
`contextualise/contextualisation` as the activity rather than generic
`Context` as the canonical noun. The Ergon-facing view of this managed
working information environment is `Ergon.WorkSpace`; it is not proposed
as a separate independently governed information asset. This revision
adds the associative-space design intent and clarifies interaction with
the Mneme Workflow Cache and governed on-demand information acquisition.

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

### 3.7 Pragmaton and Ergon.WorkSpace

`Pragmaton` names the Mneme-managed active execution-information environment.
`Ergon.WorkSpace` names the Ergon-facing managed access surface for that
environment. They are complementary perspectives of **one** managed
working-information concept, not competing information-management models.

`Ergon.ContextLoader` establishes the initial processing context appropriate
to the incoming Pragma or its specialisation, while `Ergon.ProcessingEngine`
uses the resulting `Ergon.WorkSpace` to perform its bounded activity. These
roles are tightly coupled to the Mneme Workflow Cache. Context establishment
is a collaboration: Mneme governs contextualisation and information supply;
the Ergon-side ContextLoader establishes/exposes the processing-specific
initial working context. This clarification must be reconciled with older
wording that implies either party alone performs every step.

The WorkSpace is associated with a **particular realised undertaking** of
work (for example a Pragmatosis or Noesis), rather than necessarily being a
durable property inside the published archetypal Pragma. Exact API, lifecycle,
interface ownership and allocation/reuse remain solution-design questions.

### 3.8 Associative managed information-space semantics (JavaSpaces inspiration)

The intended experience is analogous to the *associative information-space*
idea historically illustrated by JavaSpaces: processing can access available
working information using relevant identity, characteristics and relationships,
without directly managing where or how those values were acquired. A Pragmaton
is **not** constrained to a plain list of resources or a simple key/value map.

The analogy expresses **design intent**, not a technology requirement. It
neither mandates JavaSpaces/Jini nor imports distributed tuple-space
coordination, destructive `take`, leasing, transactions, network sharing or
arbitrary mutation semantics. Nor does this DRP prescribe a graph database,
universal query language, mandatory graph materialisation, or precise
Java lookup/matching APIs. Identity-based lookup, associative discovery and
relationship-aware resolution are candidate useful behaviours, subject to
further bounded interface design and performance/security constraints.

Pragmaton is scoped to bounded work execution and must not become a
second general-purpose information-management, persistence or authorisation
service.

### 3.9 Mneme Workflow Cache relationship

The **Mneme Workflow Cache** provides the workflow-local, value-oriented
information caching capability supporting Pragmaton. The `Ergon.WorkSpace`
provides the processing-facing abstraction; the cache is its underlying
information-management support, not the WorkSpace contract itself.

This is distinct from the Mneme Standard Cache and from authoritative
persistence through Mnemosyne. Active working information may be localised
near the Ergon; this DRP does not require a particular cache product,
physical arrangement, replication algorithm or Java class structure.

No loss or eviction of active working information may invalidate already
acknowledged authoritative durability.

### 3.10 Initial contextualisation and specialised entry points

The initial information space is established according to the semantics
of the work presented for processing. The general Ergon path may begin
with a Pragma's single input InformationUnit, using its governed summary
and dereferencing the anticipated information set; this does **not** limit
the total information the processing component may subsequently need.

Different Ergon specialisations can use different contextualisation and
dereferencing mechanisms without requiring a different Pragmaton model:

- DigitalTwin-Behaviour processes `Stimulus` (a `Pragma` subclass), using
  Stimulus/Twin-specific entry-point and dereferencing semantics; it need
  not start at an Encounter or treat the Digital Twin as an InformationUnit.
- Pylai ingress and egress are specialised Ergon processing arrangements;
  their external-communication boundary and initial-context semantics may
  differ while the managed working-information model remains common.
- Dokimasia assurance processing may establish assurance-specific context
  through the same governed information-management capability.

This list illustrates distinct entry-point semantics; it does not settle
all respective class structures or interaction contracts.

### 3.11 On-demand acquisition and authorisation

Pragmaton is a managed, potentially expanding working information space:
initial contextualisation is an efficient starting point, not an exhaustive
or permanently fixed set. A request for unavailable referenced or otherwise
needed information may trigger **on-demand/lazy resolution** via the
`Ergon-InformationAdapter` and Mneme's governed access services.

`Ergon.WorkSpace` may initiate or surface such a request but **does not own
independent retrieval authority**. The adapter owns retrieval mediation;
applicable PEP/Themis decisions determine whether information is permitted
into the workspace. The same constraint applies to ContextLoader's initial
dereferencing and any later acquisition. An existing reference, cache entry
or previous authorised use must not independently grant broader access.

The exact contracts for match criteria, lookup outcomes, non-disclosure,
errors, caching of authorised information, permission changes and
resolution consistency remain subject to interaction design. Do not assume
all relevant information must be eagerly fetched or universally available.

### 3.12 Pragma, Pragmatosis, InformationUnit and output separation

A published `Pragma` represents archetypal work; a subscriber-specific
realisation (general name `Pragmatosis`, with `Noesis` for a realised
`Stimulus`) represents an identifiable undertaking. The Pragmaton is the
working information space for execution of that undertaking, **not**
the work definition, an undertaking record or a substitute for an input
InformationUnit.

The Pragma/realisation may carry **content-free InformationUnit summary
snapshots** needed for work selection, traceability and contextualisation.
These are immutable, **non-authoritative** snapshots and must never become
independent authoritative InformationUnit versions. Actual content is
managed separately through the governed Mneme/Mnemosyne information path.

During execution, modification of contextualised working values is not
by itself creation of new information. Genuinely new InformationUnits are
recorded through the undertaking's Pragma output semantics, then subject
to the separately governed PublishingMask and downstream subscription
model. This DRP does not redefine publication, subscription, routing,
filtering or Petasos handoff; cross-reference the dedicated publication/
subscription DRP for those contracts.

### 3.13 Operational, provenance and assurance boundaries

Context establishment, governed additional retrieval and the distinction
between working state and authoritative values must remain sufficiently
traceable for appropriate audit, provenance, OA&M and Dokimasia assurance.
A generated/derived `Dokimasia.Praxis` represents assurance expectations
for a defined Praxis; Pragmaton does not own or reconstruct that assurance
model. Normal filtering of produced output is not inherently an Ergon
processing failure.

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

1.  Accept/reject/rename `Pragmaton` as the Mneme-managed working-information concept (while retaining `Ergon.WorkSpace` as its Ergon-facing surface).
2.  Formal association and lifecycle of a Pragmaton with a realised undertaking; whether any association with the published archetypal Pragma must be durable.
3.  Minimum Pragmaton identity/version semantics and consistency across dynamically acquired resource values.
4.  Exact save/unload semantics for changed working values and separately generated InformationUnits.
5.  Exact responsibility and interaction boundary between Mneme contextualisation, `Ergon.ContextLoader`, `Ergon.WorkSpace` and `Ergon.ProcessingEngine`.
6.  Associative lookup/relationship semantics, identity/indexing, lookup outcomes and whether matching is purely local or may initiate governed acquisition.
7.  On-demand retrieval interface and Themis/PEP enforcement, including denied or unavailable information, authorisation changes and permitted workspace reuse.
8.  Workflow Cache locality, eviction and Pragmaton lifecycle expectations; no specific implementation technology is predetermined.
9.  Reconcile references to historical `Context` and `Pragmaton` across interaction DRPs without silently changing accepted authority.


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

Revision `draft.000.003` additionally records the 2026-11-21 Solution
Architect / architecture-peer discussion of JavaSpaces-like managed
information-space thinking, `Ergon.WorkSpace`, the Mneme Workflow Cache,
`Ergon-InformationAdapter`, Themis-governed lazy dereferencing, and
specialised Ergon entry points. This draft does **not** by itself amend
an accepted Architecture Continuum or Final DRP.

Principal lineage: historical `Context`/`Loaded Context`, Context
Load/Save and Adhoc access design; current decision to tentatively
rename the noun `Pragmaton` and retain contextualisation as the process
term.
