# Domain 04 — Information Architecture

## Completion Status and Semantic Baseline

**Baseline date: 2026-10-10. Domain Completion Gate: SATISFIED.**

**Domain04 — Information Architecture is semantically complete for the agreed
Harmonia R1.x/R2.x scope.** The Domain04 architecture provides sufficient
authoritative information meaning for downstream architectural derivation
without requiring downstream domains to invent Information Architecture
semantics. Known retained uncertainties and explicit deferrals remain governed
and do not prevent this baseline. Future changes to Domain04 remain subject to
normal architecture governance.

Completion establishes a semantic baseline rather than immutability. It does
not require exhaustive modelling of every concept or relationship, final
metadata schemas or cardinalities, determination of implementation mechanisms,
physical retirement of all legacy documentation, or answers to all future
architectural questions.

The completion evidence comprises:

- [Domain04 semantic reconciliation](../../../.junie/reports/2026-10-09-domain04-information-architecture-reconciliation-step2.md)
  and the established [managed-state and history assessment](../../../.junie/reports/2026-10-10-domain04-residual-semantic-gap-assessment.md#managed-state-and-history).
- [Task information reconciliation](../../../.junie/reports/2026-10-10-domain04-task-information-reconciliation.md),
  [TaskOutcome refinement](../../../.junie/reports/2026-10-10-domain04-taskoutcome-refinement.md)
  and the canonical [observable-information/domain-meaning boundary](guardrails/observable-information-and-domain-meaning.md).
- The [residual semantic-gap assessment](../../../.junie/reports/2026-10-10-domain04-residual-semantic-gap-assessment.md),
  followed by the [search-semantics reconciliation and completion report](../../../.junie/reports/2026-10-10-domain04-search-semantics-reconciliation.md),
  including its canonical documentation assessment, Domain Completion Gate
  reassessment and repository validation.

The former sole Category A finding **R01 — search-returned information
management — is resolved**, as recorded in the canonical
[search result semantics](patterns/search-results.md#5-resolution-and-retained-boundaries)
and the [finding closure evidence](../../../.junie/reports/2026-10-10-domain04-search-semantics-reconciliation.md#4-previous-finding-and-retained-deferrals).
No Category A Domain04 semantic blocker remains. The
[gate reassessment](../../../.junie/reports/2026-10-10-domain04-search-semantics-reconciliation.md#6-domain-completion-gate-reassessment)
records PASS for agreed-scope semantic sufficiency, truthful upstream
traceability and required knowledge available canonically, and PASS WITH
EXPLICIT DEFERRAL for contradictions/retained uncertainty and legacy knowledge
treatment.

The existing [Task / Work reservations](information-families/task-work.md#5-explicitly-unresolved-architecture),
[metamodel downstream/deferred questions](metamodel/information-architecture-metamodel.md#11-downstream-derivation-and-retained-questions)
and [search-related retained boundaries](patterns/search-results.md#5-resolution-and-retained-boundaries)
remain valid. Their established scope and standing are preserved by the
[completion evidence's retained-deferral record](../../../.junie/reports/2026-10-10-domain04-search-semantics-reconciliation.md#4-previous-finding-and-retained-deferrals);
this declaration does not adjudicate them or convert candidates into approved
architecture.

**Domain05 — Application Architecture is the next programme stage**, as
recorded in the [Architecture Completion Plan](../architecture-completion-plan.md#4-current-programme-position).
Domain05 may determine how Harmonia realises this Information Architecture. It
SHALL NOT silently redefine, replace or infer alternative information meaning
for application convenience. A genuine contradiction or missing upstream
semantic requirement SHALL be raised explicitly through architecture governance
under [AX-17](../governance/architectural-axioms.md#ax-17). Recording this
transition does not begin Domain05 architecture work.

## Overview & Pedagogical Guide

Domain 04 — Information Architecture defines the canonical information models, semantic structures, and conceptual relationships required by the Harmonia Health Integration Environment (HIE). It bridges the business semantics established in **[Domain 03 (Business Architecture)](../03-business-architecture/README.md)** with downstream technical architectures (Application Architecture Domain 05, Integration Architecture Domain 06, and physical storage implementations).

The primary purpose of Domain 04 is to answer the fundamental architectural question:
> **What information does Harmonia need to understand, govern, and manage in order to discharge the responsibilities established by the Business Architecture, and what are the semantic relationships between those information concepts?**

```text
       WHY?
        │  [Domain 01: Motivation]
        ▼  Axioms, Strategic Drivers, Enduring Principles
WHAT CAPABILITIES ARE NEEDED?
        │  [Domain 02: Strategy]
        ▼  Value Streams, established Capabilities & Features (unresolved ancestry preserved)
HOW DOES THE BUSINESS OPERATE?
        │  [Domain 03: Business Architecture]
        ▼  Actors, Roles, Collaborations, Interactions, Functions, Services, Processes, Information Ownership
WHAT INFORMATION IS GOVERNED & MANAGED?
        │  [Domain 04: Information Architecture] (This Domain)
        ▼  Information Concepts, Semantic Relationships, Governance, Assemblies, Lifecycle Models
HOW IS SOFTWARE BEHAVIOUR ALLOCATED?
        │  [Domain 05: Application Architecture]
        ▼  Subsystems, Components, Services, Application State Demarcations
HOW ARE SYSTEMS INTEGRATED?
        │  [Domain 06: Integration Architecture]
        ▼  Integration Fabrics, Protocol Adapters, Message Routing, Wire Profiles
```

---

## 1. Scope & Architectural Purpose

Domain 04 establishes a technology-neutral, implementation-independent, and vendor-agnostic Information Architecture. It formalises the conceptual information responsibilities identified in Domain 03 without prematurely binding to software class structures, database schemas, messaging wire formats, or external standards (such as HL7® or FHIR®).

### Core Semantic Principle
> **Information architecture is derived from business meaning and responsibility. Representation does not define meaning.**

Preserving this principle prevents technical frameworks, wire protocols, or database schemas from distorting health integration semantics. Domain 04 maintains the fundamental distinction:

```text
Business Information Concept
    ≠ Application Data Object
    ≠ FHIR Resource
    ≠ Persistence Entity
    ≠ Java Class
    ≠ Database Table
    ≠ API Payload
```

### Upstream Authority & Immutability Context
- **Historical baseline context**: Domains 01–03 were described as CLOSED/FROZEN for an earlier baseline. Governed subsequent changes require separate authorisation; that historical description does not prohibit them. **Current Domain 03 context** is the [R1.x/R2.x Business Architecture baseline under completion](../03-business-architecture/README.md). This metadata clarification changes no Domain 01/02 status or Domain 04 architecture.
- Domain 04 derives strictly from the business meaning, capabilities, functions, services, processes, and conceptual information responsibilities established in Domain 03 (specifically `docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md`).
- Domain 04 defines semantic models, NOT implementation structures.

### What Belongs in Domain 04
- The canonical Information Architecture Metamodel and core semantic concept definitions, including Harmonia-managed information and the Information Unit boundary.
- Reusable information modelling patterns (e.g. Information Relationships, Qualified Containment, Collections, and Definition-to-Accountability progressions).
- Information governance principles: assertions, granular authority, custody, responsibility, and provenance.
- Concept-specific information lifecycle governance.
- Governed information assemblies and purpose-specific information views.
- The 16 core Information Architecture modelling guardrails and the Information Unit guardrails.
- Traceability frameworks demonstrating explicit derivation from Domain 03 Business Architecture.

### What Is Explicitly Excluded from Domain 04
- **Application Components & Subsystems**: Software component boundaries, Spring beans, or service boundaries (governed under Domain 05).
- **Physical Persistence Schemas**: PostgreSQL DDL, JPA/Hibernate entities, table indexing, or caching topologies.
- **Wire Formats & Interoperability Profiles**: FHIR R4/R5 resource profiles, JSON Schemas, HL7 v2 segment specifications, or REST endpoint definitions (governed under Domain 06 and implementation modules).
- **Exhaustive Clinical Value Sets & Code Systems**: Fine-grained terminology value sets or physical code enumerations (governed under Clinical Knowledge Services and implementation catalogues).
- **Modifications to Upstream Baselines**: Any changes to Domains 01, 02, or 03.

---

## 2. Information Architecture Metamodel Overview

Harmonia adopts an explicit, non-hierarchical semantic metamodel rooted in the
**Information Concept**. The complementary
[Harmonia-managed information and Information Unit model](metamodel/information-architecture-metamodel.md#5-harmonia-managed-information)
defines explicit management responsibility and the governed boundary associating
a Governed Representation with its Management Context. Information Concept
describes meaning; Information Unit defines the management boundary. Neither
establishes a downstream schema or representation mapping.

```mermaid
graph TD
    IC["Information Concept<br/>(Fundamental Semantic Unit)"]
    IC -->|"classified into"| SC["Semantic Categories<br/>(Entity, Activity, Event, Assertion, Relationship, Definition, Collection, Assembly)"]
    IC -->|"possesses"| ATTR["Semantic Characteristics<br/>(Identity, Classification, Relationships, Information Responsibility,<br/>Information Authority, Provenance, Qualification, Lifecycle, Temporal Context)"]
    IC -->|"participates in"| REL["Information Relationship<br/>(Source, Target, Type, Qualification, Properties)"]
    IC -->|"governed by"| GOV["Governance & Lifecycle<br/>(Authority at Assertion/Relationship Level, Concept-Specific Lifecycle)"]
    IC -->|"composed / projected into"| COMP["Assemblies & Views<br/>(Governed Assemblies, Purpose-Specific Views)"]
```

### Key Conceptual Highlights
1. **Non-Hierarchical Semantic Categories**: Categories (*Entity, Activity, Event, Assertion, Relationship, Definition, Collection, Assembly*) are conceptual classifications, not an object-oriented class inheritance tree.
2. **Reified Relationships**: Relationships are first-class semantic constructs with fundamental elements (`Source`, `Target`, `Type`) and available characteristics (`Roles`, `Qualification`, `Effective Period`, `Authority / Evidence`, `Primacy`, `Validity / Status`, `Provenance`) used where meaningful.
3. **Role Separation**: `Relationship Role` (e.g. *Parent, Child, Attending*) is the semantic role within a specific relationship and SHALL NOT imply or create a Domain 03 `Business Role`.
4. **Distinct Containment and Membership**: `Object.contains(Object)` represents hierarchical containment. Collection Membership uses the common relationship pattern without implying containment, composition, hierarchy, or ownership (`Containment ≠ Membership`). Person-oriented concepts do not use recursive containment merely to represent relationships.
5. **Definition-to-Accountability Progression**: The 5-stage pattern (`Definition → Contextualisation / Binding → Fulfilment → Outcome → Accountability`) represents independent semantic concepts with distinct identities, authorities, and lifecycles (encompassing clinical, operational, and administrative outcomes)—not lifecycle states of a single record.
6. **Granular Assertion-Level Authority**: Information Authority can attach directly to individual assertions and relationships, enabling multi-author composite health records.
7. **Authority Preservation in Assemblies**: Assemblies and Views project and coordinate information without acquiring originating authority over constituent data.
8. **Bounded Task Semantics**: `ActionableTaskArchetype` defines reusable work; `ActionableTask` identifies particular work; `FulfillmentTask` represents an undertaking (`0..*` per work instance) with execution state/output. `TaskOutcome` is the governed resolution of the ActionableTask under its explicit outcome policy, referencing participating undertakings and containing Task Completion Metadata and resulting Task.Output(s). ExecutionConcurrency, OutcomeConcurrency.Mode and Outcome.OutputRules.Mode remain orthogonal; recursive TaskOutcome containment is excluded. Remaining detailed questions and ReportedTask remain unresolved in the [bounded Task model](information-families/task-work.md).
9. **Observable Information ≠ Intrinsic Domain Comprehension**: Harmonia governs its own information/execution semantics without intrinsically inferring the meaning of all encapsulated domain content. Developer-defined Ergo logic may intentionally interpret content or invoke optional runtime AI within its governed execution boundary.
10. **Information Unit Boundary**: Identity, Metadata, Content and Context are conceptual partitions. Unit Structure, Content Structure and Content Format remain distinct; supported Unit structures and semantics are explicitly bound within each architectural release. Persistence, security, provenance and transport must recognise both the Unit and its represented information. Detailed structures remain downstream/deferred; see the [canonical Unit model](metamodel/information-architecture-metamodel.md#6-information-unit).
11. **Two Search Cases**: Harmonia-managed search produces a managed metadata **Search Result Set** referencing the Information Unit Versions comprising the result, without duplicating their content or creating new Versions of them. External search is intentionally invoked within a governed Ergo activity; returned information is acquired and persisted within that activity's context, without a separate generic management-acceptance transition. Audit / Assurance information remains distinct; see [search result semantics](patterns/search-results.md).

---

## 3. Pedagogical Reading Path & Navigation

Domain 04 is organised into modular topic sections:

```text
docs/markdown/04-information-architecture/
├── README.md                                    # This domain overview, governing question, and roadmap
├── metamodel/
│   └── information-architecture-metamodel.md   # Metamodel, concept model, core distinctions
├── patterns/
│   ├── information-relationships.md             # Reusable relationship pattern, role separation
│   ├── containment-and-collections.md           # Qualified containment, recursion, collections vs containment
│   ├── definition-to-accountability.md          # 5-stage pattern, Healthcare Service & Task/Work reference models
│   └── search-results.md                       # Managed Version-reference result sets and external Ergo search
├── governance/
│   ├── authority-custody-provenance.md          # Assertion, responsibility, authority, custody, provenance
│   └── information-lifecycle.md                 # Concept-specific lifecycles, transition governance
├── assemblies-views/
│   └── assemblies-and-views.md                  # Assembly vs View, candidate context definitions
├── guardrails/
│   ├── modelling-guardrails.md                  # The 16 canonical Information Architecture guardrails
│   └── observable-information-and-domain-meaning.md # Domain comprehension and optional runtime-AI boundary
├── traceability/
│   └── domain03-traceability.md                 # Derivation framework and representative examples
└── information-families/                        # Detailed Information Families
    ├── README.md                                # Overview, metamodel derivation, authority rules
    ├── person-healthcare-subject.md             # Person, Identity, Correlation, Subject Context
    ├── practitioner.md                          # Practitioner, Registration, Roles, Privileges
    ├── organisation.md                          # Organisation, Identifiers, Hierarchies, Contacts
    ├── healthcare-location.md                   # Locations, Care-Places, Spatial Containment
    ├── healthcare-service.md                    # 5-Stage Service Model & Service Provision Map Evaluation
    ├── device.md                                # Device Definition, Instance, Endpoints
    └── task-work.md                             # Bounded archetype, work instance, undertaking, outcome semantics
```

### Navigating the Documentation Suite

1. **[Metamodel & Concept Model](metamodel/information-architecture-metamodel.md)**: Establishes `Information Concept`, semantic categories/characteristics, [Harmonia-managed information](metamodel/information-architecture-metamodel.md#5-harmonia-managed-information), [Information Unit and its conceptual partition](metamodel/information-architecture-metamodel.md#6-information-unit), the [release-bound model](metamodel/information-architecture-metamodel.md#7-release-bound-information-unit-model), [platform/framework obligations](metamodel/information-architecture-metamodel.md#9-platform--framework-obligations) and [downstream/deferred decisions](metamodel/information-architecture-metamodel.md#11-downstream-derivation-and-retained-questions).
2. **Reusable Information Patterns**:
   - **[Information Relationships](patterns/information-relationships.md)**: The canonical relationship structure, fundamental elements, available characteristics, and strict separation between Relationship Roles and Business Roles.
   - **[Containment and Collections](patterns/containment-and-collections.md)**: Qualified forward recursive containment (`Object.contains(Object)`), entity recursion boundaries, and distinct collection membership semantics.
   - **[Definition to Accountability](patterns/definition-to-accountability.md)**: The 5-stage semantic progression, illustrated via *Healthcare Service* (`OfferedHealthcareService` to `AssuredHealthcareService` with `Order` as an associated direction mechanism) and the bounded *Task / Work* model (archetype, work instance, undertaking and governed ActionableTask resolution), with ReportedTask / Accountability unresolved.
   - **[Search Result Semantics](patterns/search-results.md)**: The managed metadata Search Result Set and its Version-reference membership, governed external search acquisition within Ergo, and distinct snapshot/export and Audit / Assurance meanings.
3. **Information Governance & Lifecycles**:
   - **[Authority, Custody & Provenance](governance/authority-custody-provenance.md)**: Definitions of `Assertion`, granular authority at assertion/relationship level, custody, and provenance models.
   - **[Information Lifecycle](governance/information-lifecycle.md)**: Concept-specific lifecycle principles and transition provenance.
4. **[Assemblies & Views](assemblies-views/assemblies-and-views.md)**: Governance of semantic compositions (`Information Assembly`) and projections (`Information View`) with illustrative candidate context definitions (Healthcare Subject Context, Longitudinal Clinical Record, Encounter Context, etc.).
   - **[Observable Information and Domain Meaning](guardrails/observable-information-and-domain-meaning.md)**: Semantic-agnosticism, the conceptual governed Ergo boundary and optional runtime AI, distinct from AI-assisted development.
5. **[Modelling Guardrails](guardrails/modelling-guardrails.md)**: The 16 core guardrails and navigation to the additional Information Unit guardrails.
6. **[Domain 03 Traceability](traceability/domain03-traceability.md)**: The 4-tier derivation chain (`Capability/Feature → Function/Process → Information Responsibility → Domain 04 Concept`) and representative reference mappings.
7. **[Detailed Information Families](information-families/README.md)**: Concrete information family models for foundational entities, identities, locations, services, and devices:
   - **[Person and Healthcare Subject](information-families/person-healthcare-subject.md)**
   - **[Practitioner](information-families/practitioner.md)**
   - **[Organisation](information-families/organisation.md)**
   - **[Healthcare Location / Care Place](information-families/healthcare-location.md)**
   - **[Healthcare Service](information-families/healthcare-service.md)**
   - **[Device](information-families/device.md)**
   - **[Task / Work — Bounded Information Model](information-families/task-work.md)**: The four adjudicated Task concepts, orthogonal execution/participation/output controls, governed ActionableTask resolution and retained Accountability uncertainty. That bounded reconciliation did not itself declare completion of Domain04 or the wider Task / Work family.
