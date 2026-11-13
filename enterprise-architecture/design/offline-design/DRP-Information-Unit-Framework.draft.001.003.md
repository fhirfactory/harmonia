# Design Review Proposal (DRP)

**DRP Identity:** DRP-Information-Unit-Framework-001\
**Current revision:** draft.003\
**Filename:** DRP-Information-Unit-Framework.draft.003.md\
**Title:** Core Information Unit Framework\
**Status:** Draft\
**Prepared for:** Architecture Review\  
**Prepared by:** ChatGPT, architecture peer\  
**Solution Architect:** Mark Hunter\
**Date:** 2026-10-27\

## 1. Feature Realisation

This DRP establishes the foundational Information Unit architecture required for Harmonia to manage information as governed, independently identifiable units across information management, persistence, integration, workflow, security, discovery, publication/subscription, routing, audit and other Harmonia capabilities.

The **Information Unit** is the fundamental managed data element within Harmonia.

It is not owned by Mneme, Mnemosyne, Themis, an integration service, a persistence technology, or any other subsystem. Those capabilities operate upon or make use of Information Units according to their respective architectural responsibilities.

### 1.1 Candidate Capability and Feature Context

Architecture development associated with this DRP has identified a candidate Enterprise Capability:

> **Information Content and State Management**

Candidate definition:

> Provides Harmonia with the ability to persist, identify, verify, discover, protect, temporally manage, and establish provenance and accountability for information content and state.

The following candidate Features have been identified beneath this capability:

- **Durable Information Persistence**
- **Information State Verification**
- **Information Content Identification and Protection**
- **Information Discovery and Distribution**
- **Temporal Information Management**
- **Information Provenance and Accountability**
- **Information Lifecycle Management**

These Capability and Feature elements are **not yet asserted by this DRP to exist in the governed ArchiMate model or Architecture Continuum**.

They are architecture discovered during development of this DRP and are to be incorporated into the ArchiMate model, reviewed through the normal architecture round-trip process, and subsequently referenced canonically by this DRP.

This DRP must therefore distinguish between:

1. established Information Unit architecture;
2. architecture proposed directly by this DRP; and
3. Capability, Feature and Requirement architecture identified during this work but **pending inclusion in the governed ArchiMate model and Continuum**.

The Information Unit architecture is expected to support Features beyond the candidate set above. In particular, integration-engine, workflow, task, audit and security Features may depend upon the same foundational Information Unit model without thereby becoming part of Information Content and State Management.

### 1.2 Candidate Requirement Context

Two candidate RequirementGroups have been identified during architecture development:

- **GovernedInformationState**
- **InformationPersistence**

These RequirementGroups are **pending inclusion in the governed ArchiMate model and Continuum**.

Candidate requirements arising from this DRP include:

- Harmonia shall manage information through a technology-neutral, governed Information Unit.
- Harmonia shall maintain Information Unit identity separately from identities belonging to represented information or represented entities.
- Harmonia shall maintain exactly one Information Representation for each Information Unit.
- Harmonia shall preserve the semantic distinction between Information Unit Metadata, Container Metadata, Content Metadata and Content.
- Harmonia shall enable Information Unit characteristics required by filtering, discovery, publication/subscription, routing and information protection to be evaluated without requiring consumers to depend upon the internal structure of the Information Unit.
- Harmonia shall maintain information structure independently from representation format.
- Harmonia shall maintain governed version progression independently from active manifestation or generation.
- Harmonia shall preserve applicable provenance, authority, lifecycle, temporal and Information Unit relationship semantics.
- Harmonia shall permit Content-aware and Content-neutral services to operate upon Information Units without redefining the Information Unit architecture.
- Harmonia shall enable independently governed representations to be expressed as independently governed Information Units and related through governed Information Unit relationships and provenance.

These are candidate architecture requirements and SHALL NOT be represented as already-governed Continuum Requirements until incorporated and reviewed through the architecture process.

### 1.3 Associated Strategy and Motivation

The Information Unit architecture is expected to associate with existing Harmonia architecture concerning:

- governed information management;
- authoritative and active information state;
- information responsibility, authority, custody and consumption;
- provenance;
- authentication and authorisation;
- information classification and compartmentalisation;
- audit;
- resilience;
- distributed operation;
- workflow and execution;
- integration and information exchange.

Existing Axioms, Principles, Requirements, Courses of Action and other Motivation elements SHALL be reused where their semantics apply.

Missing architecture exposed by this DRP is to be authored through architecture governance rather than delegated implicitly to implementation.

---

## 2. Proposal

Formalise the Harmonia **Information Unit** as the foundational and indivisible unit of governed information management.

The Information Unit belongs within the foundational Calliope model namespace:

`model.core.information`

The Information Unit SHALL be reusable by otherwise independent Harmonia domains and subsystems.

Conceptually:

```text
InformationUnit
├── Identity
├── Metadata
└── InformationRepresentation [1..1]
    ├── ContainerMetadata
    ├── ContentMetadata
    └── Content
```

This replaces the earlier conceptual partition:

```text
InformationUnit
├── Identity
├── Metadata
├── Content
└── Context
```

The previous model correctly identified several necessary concerns but did not adequately distinguish:

- Harmonia-aware Information Unit management metadata;
- metadata concerning the container or envelope of an Information Representation;
- metadata describing represented Content; and
- the represented Content itself.

The revised model makes those distinctions explicit.

---

## 3. Architectural Definition

### 3.1 Information Unit

An **Information Unit** is the indivisible unit of information governance within Harmonia.

It establishes the boundary within which Harmonia gives information independent governed standing.

An Information Unit has:

- an Information Unit Identity;
- Harmonia-aware Information Unit Metadata; and
- exactly one Information Representation.

The Information Unit is not synonymous with:

- its Content;
- its Information Representation;
- a FHIR Resource;
- a message;
- a Java object;
- a persistence entity;
- a database record;
- a transport payload;
- a cache entry;
- an HTTP request;
- a message-broker message.

Those constructs may participate in the implementation, transport, persistence or interpretation of an Information Unit without defining what an Information Unit is.

### 3.2 Indivisibility

The Information Unit is internally structured but **indivisible as a unit of governance**.

Its internal elements may be modelled and manipulated according to their respective semantics, but they do not independently acquire Information Unit standing merely by being structurally identifiable.

The unit of governance and the unit of representation are deliberately aligned:

```text
InformationUnit [1]
        │
        └── InformationRepresentation [1..1]
```

Where information requires independent governed identity, lifecycle, provenance, authority, temporal standing or relationships, it SHALL be represented as another Information Unit rather than as an independently governed representation embedded within the original Information Unit.

This prevents the Information Unit from becoming an aggregate of independently governed representations.

---

## 4. Information Unit Identity

**InformationUnit.Identity** identifies the Information Unit itself.

It SHALL remain distinguishable from:

1. the identity of represented information;
2. the identity of an entity described by that information;
3. persistence-provider identifiers;
4. transport or message identifiers;
5. correlation identifiers used for operational purposes.

No identifier syntax, datatype or generation mechanism is prescribed by this DRP.

Information Unit Identity establishes the identity of the **governed Harmonia information element**.

---

## 5. Information Unit Metadata

**InformationUnit.Metadata** is Harmonia-aware contextual information required for Harmonia to recognise, govern and operate upon the Information Unit.

It answers the general question:

> **What does Harmonia need to know about this Information Unit in order to govern and process it?**

Information Unit Metadata is therefore not Content metadata and is not merely technical metadata.

It may include semantics concerning, where applicable:

- Information Unit type or structure;
- lifecycle and state;
- governed version;
- active generation;
- temporal context;
- provenance linkage;
- authority;
- relationships with other Information Units;
- correlation;
- governance/security context;
- processing context;
- source or destination where expressed in Harmonia terms.

The exact logical attributes remain subject to model development.

Information Unit Metadata should normally contain sufficient information for Harmonia information-management services, including Mnemosyne, to operate upon an Information Unit without examining its Content.

This is an architectural separation of concern rather than a prohibition on accessing other Information Unit structures where a particular capability legitimately requires them.

---

## 6. Information Representation

Each Information Unit has exactly one:

> **InformationRepresentation [1..1]**

The Information Representation is the single represented information construct governed by that Information Unit.

It comprises:

```text
InformationRepresentation
├── ContainerMetadata
├── ContentMetadata
└── Content
```

The Information Representation is not merely a transport payload.

It represents the combination of:

- information concerning the container or envelope associated with the representation;
- information describing the Content; and
- the Content itself.

A materially distinct representation requiring independent governed standing is represented as another Information Unit and related through governed Information Unit relationships and provenance.

The architecture SHALL NOT introduce multiple independently governed Information Representations within a single Information Unit merely to model transformation history, alternative formats, derivations or previous representations.

Those concerns belong to Information Unit relationships, provenance, temporal management, lifecycle and audit as applicable.

---

## 7. Container Metadata

**InformationUnit.InformationRepresentation.ContainerMetadata** describes characteristics of the container, envelope or carriage associated with the Information Representation.

Its conceptual inspiration includes the useful information-management pattern represented by FHIR `DocumentManifest`, while deliberately generalising beyond FHIR resources and FHIR datatypes.

Container Metadata may describe concerns such as:

- container/envelope type;
- source;
- destination;
- author/originator;
- recipient;
- creation or assembly context;
- transport or interaction context;
- relationship to associated information;
- descriptive characteristics of the representation envelope.

For integration-engine behaviour this distinction is particularly important.

For example, information conveyed through:

- an HTTP `PUT`;
- a broker Topic;
- an event;
- a document exchange;
- another integration mechanism;

may carry container semantics that remain significant to Harmonia independently of the semantics of the Content itself.

Container Metadata is therefore not merely disposable transport plumbing.

Neither is it automatically Information Unit Metadata.

The distinction is:

> **Information Unit Metadata describes the Information Unit in Harmonia terms.**

> **Container Metadata describes the container or envelope associated with its Information Representation.**

---

## 8. Content Metadata

**InformationUnit.InformationRepresentation.ContentMetadata** describes the represented Content without requiring the Information Unit management machinery to interpret the Content itself.

Its conceptual inspiration includes the useful information-management pattern represented by FHIR `Attachment`, generalised beyond FHIR-specific types.

Content Metadata may identify characteristics such as:

- content type;
- semantic structure;
- schema/profile/model identity;
- format or encoding;
- language;
- size;
- digest/hash;
- location/reference where Content is externally represented;
- other characteristics required to identify, verify or appropriately handle the Content.

The architecture maintains:

```text
Unit Structure ≠ Content Structure ≠ Content Format
```

Information Unit structure identifies the governed Information Unit structure/type.

Content Structure identifies the semantic or structural definition applicable to the represented Content.

Content Format identifies its concrete encoding or representation.

These distinctions SHALL NOT be collapsed merely because a particular implementation uses the same identifier or artefact for more than one purpose.

---

## 9. Content

**InformationUnit.InformationRepresentation.Content** is the represented information itself.

Content may be:

- structured;
- composite;
- binary;
- textual;
- externally defined;
- referenced;
- inline;
- domain-specific;
- opaque to some Harmonia services;
- semantically understood by other Harmonia services.

The Information Unit architecture SHALL NOT require every Harmonia subsystem to understand the internal semantics of Content.

Conversely, it SHALL NOT require Content to be opaque.

A specialised service may legitimately depend upon detailed Content semantics.

For example, a FHIR-oriented PersistenceService implemented using a HAPI JPA Server may need to understand FHIR Content deeply in order to provide native persistence, search and other capabilities.

Another PersistenceService may treat the same Content as opaque information.

Therefore:

> **Content interpretation is not an invariant responsibility of Information Unit management, but Content-aware services may interpret and operate upon Content where appropriate to their architectural responsibility.**

---

## 10. Encapsulation and Governed Behaviour

The internal structure of the Information Unit SHALL NOT become an uncontrolled external contract through which every Harmonia subsystem traverses Information Unit internals.

In particular, consumers should not normally become coupled to paths such as:

```text
InformationUnit.InformationRepresentation.ContentMetadata....
```

merely to perform filtering, publication/subscription, routing or security decisions.

The Information Unit should expose governed behaviour through stable interfaces appropriate to the Feature being realised.

Candidate implementation patterns include exposed Masks and Functions, for example conceptually:

```text
InformationUnit.TopicMask
InformationUnit.SecurityMask
InformationUnit.<OtherMask>
```

with behaviours such as:

```text
get(...)
test(...)
```

or other Feature-appropriate operations.

These examples are **implementation/design direction rather than prescribed Java interfaces**.

The architectural principle is:

> **Consumers reason about an Information Unit through governed capabilities exposed by the Information Unit rather than by depending upon its internal metadata representation.**

A Mask may derive its result from:

```text
InformationUnit.Metadata
InformationUnit.InformationRepresentation.ContainerMetadata
InformationUnit.InformationRepresentation.ContentMetadata
```

without exposing that derivation to the caller.

A Function may similarly provide governed interpretation of Information Unit characteristics without disclosing the underlying metadata.

This preserves encapsulation while enabling cross-cutting Harmonia Features to reason efficiently about Information Units.

---

## 11. Filtering, Discovery, Publication/Subscription and Security

Filtering, discovery, publication/subscription, routing and information protection may require characteristics originating from any of:

```text
InformationUnit.Metadata
InformationUnit.InformationRepresentation.ContainerMetadata
InformationUnit.InformationRepresentation.ContentMetadata
```

These metadata domains SHALL remain semantically distinct.

They SHALL NOT be flattened merely to simplify querying or filtering.

Instead, Harmonia should expose the governed characteristics required by applicable Features through Information Unit behaviour.

For example:

- publication/subscription may require Topic-oriented characteristics;
- information protection may require Security-oriented characteristics;
- routing may require characteristics derived from Information Unit and Container context;
- discovery may require characteristics derived from Information Unit and Content Metadata.

The precise Masks, Functions and interfaces SHALL be derived from the Features requiring them rather than invented as a universal Information Unit API in advance.

This permits the Information Unit implementation to evolve internally while maintaining stable Feature-facing contracts.

---

## 12. Information Unit Relationships

An Information Unit may relate to other Information Units through governed relationship attributes or constructs.

Candidate semantics include relationships such as:

```text
relatesTo
partOf
basedOn
```

The canonical relationship vocabulary remains subject to Information Architecture development.

These relationships provide a mechanism for expressing associations between independently governed Information Units without embedding multiple independently governed representations within one Information Unit.

For example, transformed, derived, repackaged or otherwise separately governed information may be represented as another Information Unit and associated with its predecessor or source through applicable relationships and provenance.

Information Unit relationships SHALL NOT substitute for provenance where derivation/provenance semantics are required.

Likewise provenance SHALL NOT be expected to replace all semantic relationships between Information Units.

---

## 13. Provenance, Audit and Accountability

Information Unit management participates in, but does not own, Harmonia's provenance, audit and accountability frameworks.

The architecture distinguishes:

```text
Provenance
≠ Audit Evidence
≠ Information Unit Relationship
≠ Security Context
```

Provenance establishes relevant origin, derivation and lineage semantics.

Audit records relevant activity concerning Information Units.

Accountability may identify the actor, system, process or cause responsible for a change.

Information Unit relationships express semantic relationships between independently governed Information Units.

These concerns may overlap operationally but SHALL NOT be collapsed architecturally.

The candidate Feature **Information Provenance and Accountability**, pending ArchiMate/Continuum inclusion, is expected to establish the applicable behaviour more fully.

---

## 14. Version, Generation and Temporal State

The architecture retains the distinction between governed Version and Active Generation.

Conceptually:

```text
Version
    = governed progression of represented information

Active Generation
    = valid active manifestation established for management/use
    ≠ necessarily a new Version
```

Temporal information concerning an Information Unit may additionally distinguish:

- when information was observed;
- when information was applicable;
- when Harmonia became aware of it;
- when its governed state changed;
- when a representation was established.

The candidate Feature **Temporal Information Management**, pending ArchiMate/Continuum inclusion, is expected to govern these semantics more fully.

---

## 15. Information Lifecycle

Information lifecycle is distinct from persistence lifecycle.

Information may be:

- established;
- current/active;
- superseded;
- withdrawn;
- retained;
- archived;
- disposed;
- subject to other governed lifecycle states.

The candidate Feature **Information Lifecycle Management**, pending ArchiMate/Continuum inclusion, is expected to establish the relevant lifecycle behaviour and requirements.

Persistence technology may support lifecycle behaviour but does not define the semantic lifecycle of the Information Unit.

---

## 16. Relationship to Mnemosyne

Mnemosyne acts upon the Information Unit as the governed information element presented to Harmonia's authoritative persistence capability.

For the majority of Mnemosyne behaviour, the expected operational surface is:

```text
InformationUnit
├── Identity
├── Metadata
└── InformationRepresentation
    ├── ContainerMetadata
    └── ContentMetadata   [where required]
```

Mnemosyne should ordinarily be able to make information-governance and persistence-orchestration decisions from Information Unit Identity and Information Unit Metadata.

It may use Container Metadata or Content Metadata where a particular persistence operation requires additional representation characteristics.

Mnemosyne does not thereby become responsible for interpreting Content semantics.

A **PersistenceService**, however, may be Content-aware.

Accordingly:

```text
Mnemosyne
├── GovernedPersistenceFramework
└── PersistenceService [1..*]
```

does not imply that every PersistenceService treats Content as opaque.

The Information Unit abstraction preserves the governance boundary while allowing PersistenceServices to exploit native content-oriented capabilities where appropriate.

Detailed Mnemosyne behaviour belongs to the Mnemosyne DRP and SHALL reference the canonical Information Unit architecture established by this DRP.

---

## 17. Relationship to Integration Behaviour

The Information Representation model deliberately preserves Container Metadata because Harmonia integration capabilities may need the container or interaction envelope to remain significant alongside the Content.

The same Content conveyed through different containers or interaction mechanisms may carry materially different integration semantics.

An integration-engine capability may therefore inspect, create, transform or otherwise act upon Container Metadata as part of its Feature responsibilities.

This does not make integration semantics part of Mnemosyne or of Content itself.

The Information Unit provides the governed information construct through which those capabilities can operate.

---

## 18. Logical Model

The foundational logical model belongs under:

```text
model
└── core
    └── information
```

with the primary logical construct:

```text
model.core.information.InformationUnit
```

Conceptually:

```text
InformationUnit
├── Identity
├── Metadata
└── InformationRepresentation [1..1]
    ├── ContainerMetadata
    ├── ContentMetadata
    └── Content
```

This DRP does not require each conceptual construct to map one-to-one to a Java class, record, database structure or serialised element.

However, the logical model SHALL preserve the semantic distinctions sufficiently for implementations and Feature contracts to rely upon them.

Candidate logical constructs include:

```text
InformationUnit
InformationUnitId
InformationUnitMetadata
InformationRepresentation
ContainerMetadata
ContentMetadata
Content
InformationUnitVersion
ActiveGeneration
```

Exact Class and Topic modelling remains subject to Architecture Review and reconciliation with the existing registry.

---

## 19. FHIR Design Lineage

The Information Unit is not a FHIR construct and SHALL NOT depend upon FHIR.

However, several FHIR information patterns provide useful conceptual precedent for the model.

The Content Metadata + Content pattern is informed by the general pattern represented by FHIR `Attachment`: metadata describing externally structured content together with the content or reference to that content.

The Container Metadata pattern is informed by the general information-management concerns represented by FHIR `DocumentManifest`: metadata concerning the contextual container, assembly or delivery of represented information.

Information Unit Metadata is informed in part by the Harmonia-relevant contextual patterns present in FHIR `MessageHeader` and `Bundle`, generalised beyond FHIR messaging and stripped of FHIR-specific types and constraints.

These are **design inspirations only**.

Harmonia SHALL generalise the useful semantics and SHALL NOT reproduce FHIR resource structures merely because they provided conceptual precedent.

---

## 20. Explicitly Not Prescribed

This proposal does not prescribe:

- Java class/interface/record form;
- JSON/XML representation;
- FHIR representation;
- persistence schema;
- database technology;
- cache representation;
- HTTP or messaging implementation;
- UUID or other identifier format;
- physical Mask representation;
- bit width or encoding of Masks;
- universal `Mask` interface;
- exact `get()` or `test()` signatures;
- schema/profile technology;
- whether Content is inline or referenced;
- whether every metadata characteristic is durable;
- Mneme implementation behaviour;
- Mnemosyne PersistenceService implementation;
- integration-engine implementation.

The architecture defines semantic boundaries and responsibilities.

Implementation remains free to realise them appropriately provided those boundaries remain observable and enforceable.

---

## 21. Conformance Expectations

A conformant Information Unit implementation:

- treats Information Unit as the indivisible unit of Harmonia information governance;
- maintains exactly one Information Representation per Information Unit;
- preserves Information Unit Identity independently from represented-information and represented-entity identity;
- distinguishes Information Unit Metadata from representation metadata;
- distinguishes Container Metadata from Content Metadata;
- distinguishes Content Metadata from Content;
- preserves Unit Structure independently from Content Structure;
- preserves Content Structure independently from Content Format;
- preserves Version independently from Active Generation;
- permits Content-aware and Content-neutral consumers;
- permits container-aware integration behaviour;
- exposes Feature-required Information Unit characteristics without requiring consumers to depend upon internal metadata structure;
- permits independently governed representations to be represented as independently governed Information Units;
- supports governed relationships and provenance between Information Units;
- does not make Mneme, Mnemosyne, Themis, FHIR, persistence technology or Java implementation part of the foundational Information Unit definition.

---

## 22. Open Questions / Matters for Review

1. Accept `InformationRepresentation` as the canonical replacement for `Payload`.
2. Confirm `InformationUnit.InformationRepresentation [1..1]` as an Information Unit invariant.
3. Confirm Information Unit as the **indivisible unit of Harmonia information governance**.
4. Determine the canonical Topic/Class treatment of:
    - InformationUnitMetadata;
    - InformationRepresentation;
    - ContainerMetadata;
    - ContentMetadata;
    - Content.
5. Reconcile existing `InformationUnitContext` semantics into the revised model, principally Information Unit Metadata and referenced external governance frameworks.
6. Determine the canonical Information Unit relationship vocabulary, including candidate `relatesTo`, `partOf` and `basedOn` semantics.
7. Determine which Feature-facing Masks and Functions become architecturally significant as the associated Features are developed.
8. Reconcile Version, Active Generation, lifecycle and temporal concepts against existing Continuum Topics.
9. Incorporate and govern the candidate **Information Content and State Management** Capability and associated Features through the ArchiMate round-trip before replacing the provisional references in this DRP.
10. Incorporate and govern the candidate `GovernedInformationState` and `InformationPersistence` RequirementGroups and their accepted Requirements through the ArchiMate round-trip.

---

## 23. Continuum Update

The accepted DRP is to be integrated as a connected architectural delta.

### 23.1 Motivation

Associate Information Unit architecture with applicable existing Motivation elements.

Candidate Requirements and RequirementGroups identified by this DRP SHALL first be incorporated into the ArchiMate model and governed through architecture review.

Once accepted, this DRP SHALL reference those canonical elements rather than duplicate their definitions.

### 23.2 Strategy

Establish, subject to architecture review:

```text
Enterprise Capability:
    Information Content and State Management
```

and its accepted Features.

The Information Unit itself is foundational information architecture and SHALL NOT be artificially owned by this Enterprise Capability merely because that capability makes extensive use of it.

Other Harmonia capabilities may legitimately depend upon Information Unit semantics.

### 23.3 Business Layer — Information Architecture

Refine the existing Information Unit architecture to express:

```text
InformationUnit
├── Identity
├── Metadata
└── InformationRepresentation [1..1]
    ├── ContainerMetadata
    ├── ContentMetadata
    └── Content
```

Replace or reconcile the previous `Identity / Metadata / Content / Context` partition.

Preserve existing governed semantics that remain applicable rather than recreating them under new names.

### 23.4 Business/Application Behaviour

Feature-specific Functions and Services SHALL be introduced through their respective Feature architecture.

The Information Unit DRP SHALL NOT invent Functions or Services merely to provide behaviour-shaped counterparts to its information constructs.

### 23.5 Application Information Architecture

Establish logical Information Unit constructs where they provide stable contracts shared by multiple Harmonia components.

Preserve encapsulation: application components should depend upon Feature-facing Information Unit behaviour rather than internal metadata structure wherever practicable.

### 23.6 Cross-Layer and Cross-Feature Relationships

Connect Information Unit-consuming behaviours to the Features they realise.

Use the strongest applicable governed ArchiMate relationship rather than generic Association where stronger semantics are known.

Feature dependencies SHALL be represented rather than duplicating Information Unit capabilities within each subsystem.

### 23.7 Explicitly Unaffected

This DRP does not itself prescribe:

- implementation classes;
- interface signatures;
- algorithms;
- persistence products;
- transport products;
- deployment topology;
- internal component/module decomposition.

Such decisions remain solution-design space unless explicitly elevated through architecture review.

---

## 24. Review Decision

**Decision:** Pending  
**Decision authority:** Solution Architect / Architecture Review  
**Decision notes:** To be completed during review.

---

## 25. Post-Decision Actions

If accepted:

1. update the ArchiMate Information Unit model to reflect the accepted Information Unit / Information Representation structure;
2. incorporate the candidate Information Content and State Management Capability, accepted Features and RequirementGroups/Requirements through the normal architecture round-trip;
3. reconcile existing Information Unit Topics against the revised semantics;
4. establish accepted logical Classes/Topics under `model.core.information`;
5. update the Mnemosyne DRP to consume the canonical Information Unit architecture;
6. update subsequent integration, Themis, discovery/pub-sub and other DRPs to reference the canonical Information Unit contracts as applicable;
7. derive Mask/Function interfaces incrementally from the Features that require them rather than predefining a universal Information Unit interface catalogue;
8. use the resulting canonical Continuum architecture as the implementation constraint.

## Appendix A — Design Provenance

This appendix is informative rather than normative.

The revised Information Unit architecture was developed from:

- existing governed Information Unit architecture;
- existing Unit Structure / Content Structure / Format distinctions;
- current Information Unit logical-model work;
- current Mnemosyne authoritative-persistence design;
- existing provenance, audit, security and governed-information concepts;
- Solution Architect / architecture-peer analysis of Information Unit encapsulation and representation semantics;
- generalised design patterns informed by FHIR `Attachment`, `DocumentManifest`, `MessageHeader` and `Bundle`;
- current architecture development of Information Content and State Management Features and Requirements.

FHIR constructs are design evidence and conceptual precedent only. They do not constitute Harmonia architectural authority.