<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Baseline — Enterprise Capabilities

[Baseline authority and reading guide](README.md). Currentness does not imply approval. Definitions are reproduced without semantic rewriting. IDs below retain their distinct roles.

## Catalogue

- [Coordinated Activity & Interaction](#hcm-ec-coordinated-activity-interaction) — Proposed; current; placement Proposed
- [Activity & Execution](#ec-10) — Canonical; current; placement Proposed
- [Entity-Relative Operational Progression](#hcm-ec-entity-relative-operational-progression) — Proposed; current; placement Proposed
- [Governed Activity Execution Control](#hcm-ec-governed-activity-execution-control) — Proposed; current; placement Proposed
- [Operational Fulfilment Definition](#hcm-ec-operational-fulfilment-definition) — Proposed; current; placement Proposed
- [Event & Subscription](#ec-09) — Canonical; current; placement Proposed
- [Interaction & Experience](#ec-11) — Canonical; current; placement Proposed
- [Interoperability & Exchange](#ec-08) — Canonical; current; placement Proposed
- [Governed Information Operations](#hcm-ec-governed-information-operations) — Proposed; current; placement Proposed
- [Context Management](#ec-02) — Canonical; current; placement Proposed
- [Information Management](#ec-04) — Canonical; current; placement Proposed
- [Active Information Availability](#hcm-ec-active-information-availability) — Proposed; current; placement Proposed
- [Authoritative Durable State Establishment](#hcm-ec-authoritative-durable-state-establishment) — Proposed; current; placement Proposed
- [Binary Content Association & Resolution](#hcm-ec-binary-content-association-resolution) — Proposed; current; placement Proposed
- [Managed Entity & Relationship](#ec-01) — Canonical; current; placement Proposed
- [Managed State & Lifecycle](#ec-03) — Canonical; current; placement Proposed
- [Search & Discovery](#ec-05) — Canonical; current; placement Proposed
- [Semantic Governance & Conformance](#ec-13) — Canonical; current; placement Proposed
- [Trust, Accountability & Assurance](#hcm-ec-trust-accountability-assurance) — Proposed; current; placement Proposed
- [Information Authority Qualification](#hcm-ec-information-authority-qualification) — Proposed; current; placement Proposed
- [Operational Assurance](#ec-12) — Canonical; current; placement Proposed
- [Policy & Control](#ec-06) — Canonical; current; placement Proposed
- [Operation Policy Resolution](#hcm-ec-operation-policy-resolution) — Proposed; current; placement Proposed
- [Operation-Bound Policy Enforcement](#hcm-ec-operation-bound-policy-enforcement) — Proposed; current; placement Proposed
- [Provenance & Traceability](#ec-07) — Canonical; current; placement Proposed
- [Governed Activity Audit Evidence](#hcm-ec-governed-activity-audit-evidence) — Proposed; current; placement Proposed
- [Information Attribution & Lineage](#hcm-ec-information-attribution-lineage) — Proposed; current; placement Proposed
- [Service Guardian](#ec-14) — Canonical; current; placement Proposed

<a id="hcm-ec-coordinated-activity-interaction"></a>

## Coordinated Activity & Interaction

Enable governed interaction, event participation, boundary exchange and accountable activity progression.

**Overview**

Requests, events, work and human interaction are related ways to cause or observe change, with distinct contracts and progression responsibilities.

**Intent**

Make requests, event participation, work and human interaction progress according to their different contracts.

**Boundary**

Exchange is not execution, notification is not outcome, and interaction is not clinical authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-coordinated-activity-interaction |
| Snapshot publication reference | HCM-EC-coordinated-activity-interaction |
| ArchiMate semantic ID | id-6d2882efaa7756e886bb64a9ca045885 |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-COORDINATED-ACTIVITY-INTERACTION |

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Activity & Execution](enterprise-capabilities.md#ec-10) — Canonical; placement Proposed
- [Event & Subscription](enterprise-capabilities.md#ec-09) — Canonical; placement Proposed
- [Interaction & Experience](enterprise-capabilities.md#ec-11) — Canonical; placement Proposed
- [Interoperability & Exchange](enterprise-capabilities.md#ec-08) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-coordinated-activity-interaction`) and the element's version-qualified Properties.

<a id="ec-10"></a>

## Activity & Execution

- **Architectural Scope**: Define, instantiate, match, assign, dispatch, supervise, and progress coordinated operational and clinical activity units.
- **Functional Responsibilities**:
  - Instantiating operational work items (Work Orders, To Dos, synthetic platform Tasks) in response to triggers.
  - Matching and assigning work units to eligible human or automated performers and roles based on policy rules.
  - Dispatching work units to their assigned performers with acknowledgement according to the applicable interaction contract.
  - Supervising execution progress, tracking timeouts, managing escalations, and handling activity interruptions.
- **Decoupling Note**: Free of worker threads, thread pools, execution daemons, or specific workflow runtime engines.

Activity & Execution provides reusable instantiation, matching, dispatch and supervision for governed activity units. Human Work Orders, human decision To Dos and automated Synthetic Tasks retain different performer and authority semantics while sharing a need for accountable progression. This reuse supports operational coordination without making a runtime engine the definition of the work.

**Boundary**

Coordination does not confer clinical work allocation or judgement. The capability specifies neither worker threads nor a particular execution engine.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-10 |
| Snapshot publication reference | EC-10 |
| ArchiMate semantic ID | id-49d4f69ebeeb5ea782a52172fb3e8a7b |
| Architecture Identity | EC-10 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Activity & Execution |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-COORDINATED-ACTIVITY-INTERACTION.CT2-EC-10 |
| Structural aliases | ["EC-10"] |

Parent: [Coordinated Activity & Interaction](enterprise-capabilities.md#hcm-ec-coordinated-activity-interaction). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Entity-Relative Operational Progression](enterprise-capabilities.md#hcm-ec-entity-relative-operational-progression) — Proposed; placement Proposed
- [Governed Activity Execution Control](enterprise-capabilities.md#hcm-ec-governed-activity-execution-control) — Proposed; placement Proposed
- [Operational Fulfilment Definition](enterprise-capabilities.md#hcm-ec-operational-fulfilment-definition) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-10`) and the element's version-qualified Properties.

<a id="hcm-ec-entity-relative-operational-progression"></a>

## Entity-Relative Operational Progression

Coordinate serial, entity-relative operational inputs and their interested behaviours.

**Overview**

Patient, practitioner and bed concerns recur across different business contexts; each can need a coherent progression frame without one central entity service.

**Intent**

Maintain a coherent operational reference frame as one entity reacts to successive inputs.

**Boundary**

Interest fan-out is distinct from intra-entity concurrency; entity progression does not acquire originating facts.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-entity-relative-operational-progression |
| Snapshot publication reference | HCM-EC-entity-relative-operational-progression |
| ArchiMate semantic ID | id-873bd8a6758955b996c204275d26ed4f |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-COORDINATED-ACTIVITY-INTERACTION.CT2-EC-10.CT3-EC-ENTITY-RELATIVE-OPERATIONAL-PROGRESSION |

Parent: [Activity & Execution](enterprise-capabilities.md#ec-10). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-entity-relative-operational-progression`) and the element's version-qualified Properties.

<a id="hcm-ec-governed-activity-execution-control"></a>

## Governed Activity Execution Control

Instantiate, dispatch, supervise and progress authorised operational undertakings with explicit outcome standing.

**Overview**

Reusable execution control respects work-unit archetype, participation and uncertain effects rather than equating dispatch with completion.

**Intent**

Progress accountable operational work without equating dispatch, execution and successful outcome.

**Boundary**

Does not own underlying clinical-work meaning, clinical decisions or originator business authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-governed-activity-execution-control |
| Snapshot publication reference | HCM-EC-governed-activity-execution-control |
| ArchiMate semantic ID | id-dfb644fa99745553bf0ff980faf30c6d |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-COORDINATED-ACTIVITY-INTERACTION.CT2-EC-10.CT3-EC-GOVERNED-ACTIVITY-EXECUTION-CONTROL |

Parent: [Activity & Execution](enterprise-capabilities.md#ec-10). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-governed-activity-execution-control`) and the element's version-qualified Properties.

<a id="hcm-ec-operational-fulfilment-definition"></a>

## Operational Fulfilment Definition

Define the context requirements, expected outcomes and housekeeping of an operational undertaking.

**Overview**

The semantic execution envelope applies to one-step and multi-step activity and stays distinct from dispatch and supervision.

**Intent**

Keep the semantic promise of an undertaking separate from the machinery that executes it.

**Boundary**

A fulfilment definition does not execute, orchestrate or choreograph its activity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-operational-fulfilment-definition |
| Snapshot publication reference | HCM-EC-operational-fulfilment-definition |
| ArchiMate semantic ID | id-d066b2ac060b5d0fa4429dde7b2c73c2 |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-COORDINATED-ACTIVITY-INTERACTION.CT2-EC-10.CT3-EC-OPERATIONAL-FULFILMENT-DEFINITION |

Parent: [Activity & Execution](enterprise-capabilities.md#ec-10). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-operational-fulfilment-definition`) and the element's version-qualified Properties.

<a id="ec-09"></a>

## Event & Subscription

- **Architectural Scope**: Publish, ingest, correlate, filter, and syndicate events and subscription-driven changes across the enterprise landscape.
- **Functional Responsibilities**:
  - Ingesting operational and clinical event triggers from external systems and internal state changes.
  - Managing topic- and criteria-based event subscriptions for internal and external consumers.
  - Evaluating dynamic subscription filters to match event payloads with interested subscriber criteria.
  - Delivering asynchronous event notifications reliably to registered subscribers.
- **Decoupling Note**: Defined strictly in terms of event publishing, criteria matching, and notification delivery; entirely independent of broker topics, partitions, or queues.

Event & Subscription connects significant changes with participants whose registered interests match them. It makes event ingestion, correlation, interest criteria and notification reusable across clinical and operational contexts. The architectural meaning is the relationship between a change and an interested consumer, rather than the broker constructs used to carry it.

**Boundary**

Receiving a notification does not determine the consumer's business response; topics and queues do not define the capability.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-09 |
| Snapshot publication reference | EC-09 |
| ArchiMate semantic ID | id-770ebb3e3bdc5f2bbe77b2bfd55bd74d |
| Architecture Identity | EC-09 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Event & Subscription |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-COORDINATED-ACTIVITY-INTERACTION.CT2-EC-09 |
| Structural aliases | ["EC-09"] |

Parent: [Coordinated Activity & Interaction](enterprise-capabilities.md#hcm-ec-coordinated-activity-interaction). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-09`) and the element's version-qualified Properties.

<a id="ec-11"></a>

## Interaction & Experience

- **Architectural Scope**: Provide contextual interaction between human actors, Harmonia, and enterprise presentation services, including visualization, alerting, and collaboration integration.
- **Functional Responsibilities**:
  - Mediating presentation interactions via dedicated backend-for-frontend gateways decoupled from internal databases.
  - Rendering clinical timelines, provider directory management interfaces, and operational console dashboards.
  - Providing contextual notifications and alerts to human users across web and mobile channels.
  - Projecting longitudinal record context into secure multidisciplinary collaboration spaces.
- **Boundary Note**: Does not absorb the healthcare semantics of Clinical Collaboration or medical documentation. Consumes established identity and security context; does not own authentication, consent, or clinical truth.

Interaction & Experience makes contextual information and governed actions available to human participants through presentation and collaboration integration. Clinical timelines, directory administration and operational dashboards have different purposes, but share a need to retain established identity, security and information context. The recurring responsibility is usable interaction within those boundaries.

**Boundary**

Presentation does not own consent, authentication or clinical truth, and collaboration projection does not absorb clinical documentation semantics.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-11 |
| Snapshot publication reference | EC-11 |
| ArchiMate semantic ID | id-82cd1f6a3e2352639e22c542f5cb3a9d |
| Architecture Identity | EC-11 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Interaction & Experience |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-COORDINATED-ACTIVITY-INTERACTION.CT2-EC-11 |
| Structural aliases | ["EC-11"] |

Parent: [Coordinated Activity & Interaction](enterprise-capabilities.md#hcm-ec-coordinated-activity-interaction). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-11`) and the element's version-qualified Properties.

<a id="ec-08"></a>

## Interoperability & Exchange

- **Architectural Scope**: Resolve destinations, transform schemas, route, transport, deliver, acknowledge, and coordinate standards-based information exchange across enterprise boundaries.
- **Functional Responsibilities**:
  - Providing multi-protocol boundary adaptation for healthcare standards (HL7 v2, FHIR REST, DICOM, secure messaging).
  - Executing bidirectional structural and semantic transformations between legacy formats and canonical representations.
  - Ensuring reliable addressed delivery and fan-out distribution to external clinical endpoints.
  - Processing and distinguishing technical delivery acknowledgements from business acknowledgements.
- **Boundary Rule**: Egress terminates Harmonia management of the emitted representation (Axiom AX-13).

Interoperability & Exchange moves information across participating boundaries through destination resolution, transformation, delivery and acknowledgements. It preserves the difference between technical transmission and the recipient's business response so that an exchange can be understood under its actual contract. Standards govern the external representation while internal management semantics remain private.

**Boundary**

Egress ends Harmonia management of the emitted representation. Retained delivery evidence does not extend control over an external recipient or transfer payload authority.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-08 |
| Snapshot publication reference | EC-08 |
| ArchiMate semantic ID | id-dbd66e69788450649e184966aa724797 |
| Architecture Identity | EC-08 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Interoperability & Exchange |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-COORDINATED-ACTIVITY-INTERACTION.CT2-EC-08 |
| Structural aliases | ["EC-08"] |

Parent: [Coordinated Activity & Interaction](enterprise-capabilities.md#hcm-ec-coordinated-activity-interaction). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-08`) and the element's version-qualified Properties.

<a id="hcm-ec-governed-information-operations"></a>

## Governed Information Operations

Maintain identifiable, meaningful, context-qualified and retrievable information through valid state progression.

**Overview**

Entity integrity, context, lifecycle, information maintenance, discovery and semantic conformance are constituent reusable abilities; none is an implementation subsystem.

**Intent**

Keep information identity, meaning, context, state and retrieval compatible across diverse healthcare uses.

**Boundary**

Information management and semantic governance do not grant originating domain authority or erase active/durable state separation.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-governed-information-operations |
| Snapshot publication reference | HCM-EC-governed-information-operations |
| ArchiMate semantic ID | id-a0eebbba1ce35b2ea9bf499d3ea6b4c2 |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-GOVERNED-INFORMATION-OPERATIONS |

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Context Management](enterprise-capabilities.md#ec-02) — Canonical; placement Proposed
- [Information Management](enterprise-capabilities.md#ec-04) — Canonical; placement Proposed
- [Managed Entity & Relationship](enterprise-capabilities.md#ec-01) — Canonical; placement Proposed
- [Managed State & Lifecycle](enterprise-capabilities.md#ec-03) — Canonical; placement Proposed
- [Search & Discovery](enterprise-capabilities.md#ec-05) — Canonical; placement Proposed
- [Semantic Governance & Conformance](enterprise-capabilities.md#ec-13) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-governed-information-operations`) and the element's version-qualified Properties.

<a id="ec-02"></a>

## Context Management

- **Architectural Scope**: Establish, validate, propagate, and preserve the execution, clinical, security, and administrative context within which information and activities have meaning.
- **Functional Responsibilities**:
  - Binding the subject of care, encounter, ordering provider, and healthcare service context to in-flight messages and transactions.
  - Propagating tamper-evident, attributable security context across asynchronous, multi-hop operational boundaries.
  - Ensuring context preservation during cross-protocol transformations across disparate healthcare exchange formats and protocols.
  - Detecting and rejecting requests where mandatory operational or clinical context is missing or inconsistent.
- **Collaborative Nature**: Bounded reusable context responsibility may be realised through participation across gateways, activity execution and information-access boundaries. Participation does not transfer semantic ownership.

Context Management preserves the circumstances in which information and activity have meaning as they cross processing boundaries. Subject, encounter, provider, service and security context must remain attributable and consistent rather than be reconstructed from convenient local assumptions. Its reuse is especially significant in multi-hop activity where a syntactically valid message can otherwise lose the context required for legitimate interpretation.

**Boundary**

Context participation does not transfer semantic ownership. Generic context management and assurance-specific context semantics contribute together without allocating a new central Context Service.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-02 |
| Snapshot publication reference | EC-02 |
| ArchiMate semantic ID | id-06e038737c5355edbc0591e08c594ab0 |
| Architecture Identity | EC-02 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Context Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-02 |
| Structural aliases | ["EC-02"] |

Parent: [Governed Information Operations](enterprise-capabilities.md#hcm-ec-governed-information-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-02`) and the element's version-qualified Properties.

<a id="ec-04"></a>

## Information Management

- **Architectural Scope**: Validate, represent, persist, retrieve, version, and manage governed clinical, administrative, and operational information assets.
- **Functional Responsibilities**:
  - Validating inbound information structures against canonical information constraints.
  - Providing authoritative durable persistence for historical health records and administrative registries.
  - Managing active distributed working memory representations for low-latency retrieval.
  - Governing document versioning, supersession, amendments, and retractions without data loss.
- **Architectural Principle**: Enforces the foundational separation between *active distributed state access* and *authoritative durable state preservation* (Axiom AX-05).

Information Management provides the recurring ability to represent and maintain governed information through validation, preservation, retrieval and versioned change. It accommodates both responsive active use and authoritative durable preservation while keeping those responsibilities separate. This distinction allows availability and working state to support care without turning a cache into durable truth.

**Boundary**

Engine capabilities provide machinery under Harmonia semantics. Persistence success does not establish information authority or make the platform the originator of the represented facts.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-04 |
| Snapshot publication reference | EC-04 |
| ArchiMate semantic ID | id-9e79bc474aed516c8c6752ab81b63d6b |
| Architecture Identity | EC-04 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Information Management |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-04 |
| Structural aliases | ["EC-04"] |

Parent: [Governed Information Operations](enterprise-capabilities.md#hcm-ec-governed-information-operations). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Active Information Availability](enterprise-capabilities.md#hcm-ec-active-information-availability) — Proposed; placement Proposed
- [Authoritative Durable State Establishment](enterprise-capabilities.md#hcm-ec-authoritative-durable-state-establishment) — Proposed; placement Proposed
- [Binary Content Association & Resolution](enterprise-capabilities.md#hcm-ec-binary-content-association-resolution) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-04`) and the element's version-qualified Properties.

<a id="hcm-ec-active-information-availability"></a>

## Active Information Availability

Maintain responsive, reconstructable active information and coordinate its safe use.

**Overview**

Active representation, observation and convergence support availability while preserving an explicit trust basis for use.

**Intent**

Provide responsive managed use without promoting active representation into durable truth.

**Boundary**

An active generation is not an authoritative version; failure to converge does not establish valid active state.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-active-information-availability |
| Snapshot publication reference | HCM-EC-active-information-availability |
| ArchiMate semantic ID | id-50306e15812e5ba3aa26482f2c9c84ac |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-04.CT3-EC-ACTIVE-INFORMATION-AVAILABILITY |

Parent: [Information Management](enterprise-capabilities.md#ec-04). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-active-information-availability`) and the element's version-qualified Properties.

<a id="hcm-ec-authoritative-durable-state-establishment"></a>

## Authoritative Durable State Establishment

Atomically establish durable managed state and its authoritative version progression.

**Overview**

Durable acceptance gives managed information an explicit committed state from which applicable active representations can be reconstructed.

**Intent**

Give managed change an explicit committed state and authoritative progression basis.

**Boundary**

Commit is distinct from semantic source authority and from successful active convergence; applications do not gain a raw persistence path.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-authoritative-durable-state-establishment |
| Snapshot publication reference | HCM-EC-authoritative-durable-state-establishment |
| ArchiMate semantic ID | id-591ee54ab6145bf58e837667f64a4265 |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-04.CT3-EC-AUTHORITATIVE-DURABLE-STATE-ESTABLISHMENT |

Parent: [Information Management](enterprise-capabilities.md#ec-04). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-authoritative-durable-state-establishment`) and the element's version-qualified Properties.

<a id="hcm-ec-binary-content-association-resolution"></a>

## Binary Content Association & Resolution

Maintain content associations and custody knowledge sufficient for governed payload resolution, retrieval and assembly.

**Overview**

Document attachments, diagnostic media and operational artefacts share a need to locate usable content while distinguishing logical meaning from physical possession.

**Intent**

Make logical information and its usable payloads consistently associable while retaining custody boundaries.

**Boundary**

No universal binary custody, storage topology or persistence authority is implied.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-binary-content-association-resolution |
| Snapshot publication reference | HCM-EC-binary-content-association-resolution |
| ArchiMate semantic ID | id-08ef90abe8f35705add6ae823077cc76 |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-04.CT3-EC-BINARY-CONTENT-ASSOCIATION-RESOLUTION |

Parent: [Information Management](enterprise-capabilities.md#ec-04). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-binary-content-association-resolution`) and the element's version-qualified Properties.

<a id="ec-01"></a>

## Managed Entity & Relationship

- **Architectural Scope**: Identify, classify, relate, and maintain the structural integrity of managed healthcare and administrative entities throughout the enterprise landscape.
- **Functional Responsibilities**:
  - Uniquely identifying core domain entities (Persons, Practitioners, Roles, Organisations, Locations, Services, Devices).
  - Resolving and maintaining multi-authority identifier systems and aliases without destructive overwriting.
  - Representing and traversing multi-dimensional relationships (affiliations, organizational hierarchies, physical location trees, care team memberships).
  - Maintaining referential integrity across interconnected entity graphs.
- **Anti-Responsibilities**: Does not own physical database relational tables or foreign key constraints (Domain 04/07).

This reusable capability keeps managed healthcare entities identifiable and their relationships structurally intelligible. It supplies common identity, alias and relationship behaviour recurring in persons, practitioners, organisations, locations and devices. Reuse concerns those architectural responsibilities, allowing the healthcare-specific meaning of each entity to remain with its governing context.

**Boundary**

Relationship integrity is not a physical database design. Correlating entities does not establish originating clinical identity authority or a new standalone Correlation capability.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-01 |
| Snapshot publication reference | EC-01 |
| ArchiMate semantic ID | id-d53727d908365757a293b0896b2c769b |
| Architecture Identity | EC-01 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Managed Entity & Relationship |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-01 |
| Structural aliases | ["EC-01"] |

Parent: [Governed Information Operations](enterprise-capabilities.md#hcm-ec-governed-information-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-01`) and the element's version-qualified Properties.

<a id="ec-03"></a>

## Managed State & Lifecycle

- **Architectural Scope**: Govern the current and historical state, effective temporal periods, state transition validations, and lifecycle progression of managed integration artifacts and entities.
- **Functional Responsibilities**:
  - Defining and enforcing valid state transition state machines (e.g., `REQUESTED` $\to$ `VALIDATED` $\to$ `IN_PROGRESS` $\to$ `COMPLETED`).
  - Maintaining temporal validity intervals (effective start and end dates) for relationships, credentials, and states.
  - Supporting non-destructive state progression, preserving historical state versions for longitudinal review.
  - Rejecting illegal or out-of-order state transitions while accommodating idempotent replay.
- **Anti-Responsibilities**: Does not define thread-level mutexes or database transaction isolation levels.

Managed State & Lifecycle provides reusable responsibility for valid progression, temporal applicability and non-destructive change. A directory relationship, clinical document and operational work item have different business states, yet all need an intelligible distinction between current standing, historical state and invalid transitions. The capability captures that recurrence without replacing each subject's lifecycle meaning.

**Boundary**

Business lifecycle integrity does not prescribe locks, database isolation or worker implementation, and illustrative state names do not define every subject's transitions.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-03 |
| Snapshot publication reference | EC-03 |
| ArchiMate semantic ID | id-5891936941f65d20afad86b0697879c7 |
| Architecture Identity | EC-03 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Managed State & Lifecycle |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-03 |
| Structural aliases | ["EC-03"] |

Parent: [Governed Information Operations](enterprise-capabilities.md#hcm-ec-governed-information-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-03`) and the element's version-qualified Properties.

<a id="ec-05"></a>

## Search & Discovery

- **Architectural Scope**: Index, search, resolve, filter, and discover information assets, entities, directory endpoints, and healthcare services across the enterprise.
- **Functional Responsibilities**:
  - Providing multi-criteria indexed search across complex entity graphs (e.g., find active cardiologists at Location X accepting new referrals).
  - Resolving service and practitioner communication endpoints for addressed delivery.
  - Supporting bounded, paginated clinical queries across longitudinal patient records.
  - Applying dynamic security and consent filters to search results prior to egress.
- **Anti-Responsibilities**: Does not dictate specific search engine indexes, inverted index implementations, or SQL query syntaxes.

Search & Discovery makes information, entities and service endpoints findable through governed criteria and bounded queries. It addresses the recurring need to locate relevant material or a destination before it can be used. Security-qualified results preserve the distinction between knowing that something exists and being entitled to obtain or disclose it.

**Boundary**

Discovery does not decide policy or prescribe a search engine, index format or query language.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-05 |
| Snapshot publication reference | EC-05 |
| ArchiMate semantic ID | id-e11f02764fe55ff29c0782db21198040 |
| Architecture Identity | EC-05 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Search & Discovery |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-05 |
| Structural aliases | ["EC-05"] |

Parent: [Governed Information Operations](enterprise-capabilities.md#hcm-ec-governed-information-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-05`) and the element's version-qualified Properties.

<a id="ec-13"></a>

## Semantic Governance & Conformance

- **Architectural Scope**: Govern canonical models, schemas, terminologies, value sets, mapping tables, constraint rules, and semantic conformance validation across the enterprise.
- **Functional Responsibilities**:
  - Defining and publishing canonical healthcare data models and profile structures.
  - Governing clinical terminology mappings, value sets, and concept lookups (SNOMED CT, LOINC).
  - Validating inbound and outbound message representations against published semantic rules.
  - Governing semantic versioning, deprecation, and backward compatibility across schema revisions.
- **Component Relationship**: Informs the Calliope strategic logical component, but Calliope is the responsibility centre, not the capability definition.

This capability makes canonical information models, terminology bindings, mappings and conformance rules governable across enterprise use. It addresses both what representations mean and whether they respect the published rules, keeping version and compatibility concerns explicit. A conformant representation can therefore be distinguished from one that merely uses a supported transport or parser.

**Boundary**

Conformance does not establish clinical truth, information authority or disclosure permission. Calliope is an established responsibility centre, not the definition of the capability.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-13 |
| Snapshot publication reference | EC-13 |
| ArchiMate semantic ID | id-7d6f635ffb6f5c6181bde1d07b859662 |
| Architecture Identity | EC-13 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Semantic Governance & Conformance |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-13 |
| Structural aliases | ["EC-13"] |

Parent: [Governed Information Operations](enterprise-capabilities.md#hcm-ec-governed-information-operations). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-13`) and the element's version-qualified Properties.

<a id="hcm-ec-trust-accountability-assurance"></a>

## Trust, Accountability & Assurance

Make governed activity permissible, attributable, operationally dependable and independently assessable.

**Overview**

Policy, evidence, resilience and independent assurance contribute to confidence through different responsibilities. Keeping them as peers prevents technical success or recorded evidence from becoming a business assurance conclusion.

**Intent**

Keep permission, attribution, operational integrity and independent confidence distinguishable and usable together.

**Boundary**

Cross-cutting participation does not distribute semantic ownership, mandate centralisation or authorise the assured subject to determine its own conclusion.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-trust-accountability-assurance |
| Snapshot publication reference | HCM-EC-trust-accountability-assurance |
| ArchiMate semantic ID | id-9c7d1cf0966a5d1bb0c17707051a793e |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT1 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE |

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Information Authority Qualification](enterprise-capabilities.md#hcm-ec-information-authority-qualification) — Proposed; placement Proposed
- [Operational Assurance](enterprise-capabilities.md#ec-12) — Canonical; placement Proposed
- [Policy & Control](enterprise-capabilities.md#ec-06) — Canonical; placement Proposed
- [Provenance & Traceability](enterprise-capabilities.md#ec-07) — Canonical; placement Proposed
- [Service Guardian](enterprise-capabilities.md#ec-14) — Canonical; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-trust-accountability-assurance`) and the element's version-qualified Properties.

<a id="hcm-ec-information-authority-qualification"></a>

## Information Authority Qualification

Represent and qualify the source-authority and credibility standing of information independently of transport, persistence and concurrency.

**Overview**

Identity correlation, clinical-history synthesis, reference-content use and assurance evidence all need to distinguish the standing of an assertion from technical acceptance. This recurring ability is insufficiently explicit in the existing fourteen EC definitions and is proposed as a new peer responsibility.

**Intent**

Keep source credibility explicit even when the technical handling of competing assertions is equally valid.

**Boundary**

This is not independent service assurance, a universal conflict arbiter or clinical truth determination. Existing EC-13 governs authority nomenclature; this capability applies and preserves the qualified standing.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-information-authority-qualification |
| Snapshot publication reference | HCM-EC-information-authority-qualification |
| ArchiMate semantic ID | id-e4ba93a12970559e916ecaabb0eb0bb3 |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-INFORMATION-AUTHORITY-QUALIFICATION |

Parent: [Trust, Accountability & Assurance](enterprise-capabilities.md#hcm-ec-trust-accountability-assurance). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-information-authority-qualification`) and the element's version-qualified Properties.

<a id="ec-12"></a>

## Operational Assurance

- **Architectural Scope**: Ensure concurrency integrity, system resilience, duplicate detection, exception handling, automated recovery, and operational telemetry across platform operations.
- **Functional Responsibilities**:
  - Enforcing sliding-window duplicate message detection and idempotent suppression.
  - Guaranteeing ingress dual-write safety (durable acceptance before emitting positive application ACK).
  - Managing structured failure recovery, exception escalation, and dead-letter isolation.
  - Emitting platform telemetry, operational health metrics, and queue processing metrics.
- **Collaborative Nature**: Realized collaboratively across boundary gateways, messaging infrastructure, execution workers, and storage layers.

#### EC-12 / EC-14 Boundary

EC-12 retains the established scope and responsibilities above. It may contribute directly to generic operational/processing assurance, limited to that contribution in the [approved assurance matrix](../../02-strategy/capability-maps/health-service-assurance-derivation.md#collaborative-enterprise-capability-contribution-matrix). It does not own the general semantics of assurance disposition, assurance-specific criteria applicability, evidentiary association, assessment against assurance criteria, adjudication, or independent assurance findings and conclusions. Those distinguishing responsibilities are introduced through EC-14 Service Guardian. Operational health and business-level processing assurance remain distinct; EC-12 is not expanded to absorb EC-14.

Operational Assurance preserves processing integrity under concurrency, duplicate delivery and failure. It brings together resilience, durable ingress acceptance, recovery and operational telemetry so that technical degradation does not silently weaken the managed operation's semantics. Its contribution is collaborative across operational boundaries while remaining a bounded architectural responsibility.

**Boundary**

Operational health is distinct from independent assessment of a governed subject. Assurance-specific criteria applicability, adjudication and conclusions belong to the established EC-14 contribution.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-12 |
| Snapshot publication reference | EC-12 |
| ArchiMate semantic ID | id-9bbd142f818e50d39a3d0429f74b72b4 |
| Architecture Identity | EC-12 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Operational Assurance |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-12 |
| Structural aliases | ["EC-12"] |

Parent: [Trust, Accountability & Assurance](enterprise-capabilities.md#hcm-ec-trust-accountability-assurance). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-12`) and the element's version-qualified Properties.

<a id="ec-06"></a>

## Policy & Control

- **Architectural Scope**: Evaluate and enforce security, consent, privacy, and regulatory policies including authority, access control, and information handling rules.
- **Functional Responsibilities**:
  - Executing impartial, default-deny policy evaluations for all integration transactions.
  - Enforcing Role-Based and Attribute-Based Access Control (RBAC/ABAC) policies.
  - Evaluating patient consent directives and privacy restrictions at boundary and query checkpoints.
  - Enforcing statutory information disclosure boundaries (e.g., mental health, VIP records).
- **Collaborative Nature**: Evaluated across boundary gateways, internal bus dispatch, storage interfaces, and presentation layers.

Policy & Control supplies the reusable evaluation and enforcement of authority, consent, privacy and handling constraints. Its purpose is consistent governed action across ingress, information access, execution and presentation boundaries. Default-deny evaluation preserves the need for established authority rather than treating a caller's intent or a technically valid request as permission.

**Boundary**

Participation at multiple enforcement points does not distribute policy ownership or turn operational security context into persistent clinical content.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-06 |
| Snapshot publication reference | EC-06 |
| ArchiMate semantic ID | id-a6f6d30d8fce5ac78f60f7aefa71b28e |
| Architecture Identity | EC-06 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Policy & Control |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-06 |
| Structural aliases | ["EC-06"] |

Parent: [Trust, Accountability & Assurance](enterprise-capabilities.md#hcm-ec-trust-accountability-assurance). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Operation Policy Resolution](enterprise-capabilities.md#hcm-ec-operation-policy-resolution) — Proposed; placement Proposed
- [Operation-Bound Policy Enforcement](enterprise-capabilities.md#hcm-ec-operation-bound-policy-enforcement) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-06`) and the element's version-qualified Properties.

<a id="hcm-ec-operation-policy-resolution"></a>

## Operation Policy Resolution

Resolve effective, temporally applicable permission criteria for an established identity and governed operation.

**Overview**

Permission depends on controlled criteria, role assignments, action and information scope; revocation is distinct from natural expiry.

**Intent**

Determine the effective permitted action under the applicable identity, information and temporal criteria.

**Boundary**

Does not authenticate identity or grant permission from caller-controlled tags or mere role names.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-operation-policy-resolution |
| Snapshot publication reference | HCM-EC-operation-policy-resolution |
| ArchiMate semantic ID | id-0331dca5104b504794a216037ed9bd40 |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-06.CT3-EC-OPERATION-POLICY-RESOLUTION |

Parent: [Policy & Control](enterprise-capabilities.md#ec-06). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-operation-policy-resolution`) and the element's version-qualified Properties.

<a id="hcm-ec-operation-bound-policy-enforcement"></a>

## Operation-Bound Policy Enforcement

Enforce resolved policy constraints at the boundary where a governed action is attempted.

**Overview**

Distributed participants apply the applicable effective authorisation and fail closed when authority is absent or untrustworthy.

**Intent**

Ensure an operation cannot proceed merely because its caller omitted or bypassed a voluntary policy check.

**Boundary**

Enforcement participation does not distribute policy ownership or make security context persistent content.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-operation-bound-policy-enforcement |
| Snapshot publication reference | HCM-EC-operation-bound-policy-enforcement |
| ArchiMate semantic ID | id-9c71dcd24d0154db8ee01ab204012f48 |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-06.CT3-EC-OPERATION-BOUND-POLICY-ENFORCEMENT |

Parent: [Policy & Control](enterprise-capabilities.md#ec-06). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-operation-bound-policy-enforcement`) and the element's version-qualified Properties.

<a id="ec-07"></a>

## Provenance & Traceability

- **Architectural Scope**: Preserve required origin attribution, meaningful transformation history, significant execution checkpoints, tamper-evident audit evidence and end-to-end traceability. Harmonia SHALL preserve provenance, audit and operational evidence where required by governing obligations, architectural significance, accountability, reconstruction, security or assurance needs. Routine technical activity SHALL NOT automatically require durable evidentiary recording.
- **Functional Responsibilities**:
  - Recording attributable, verifiable origin metadata for information sources, authors, and timestamps.
  - Tracking information-significant transformation history and required mapping checkpoints as information crosses system seams.
  - Capturing non-PHI operational and security audit trails for regulatory compliance (HIPAA, GDPR).
  - Supporting end-to-end transaction tracing across asynchronous processing pipelines.
- **Collaborative Nature**: Participating elements preserve required provenance and audit context according to policy. Telemetry, operational facts, provenance, audit and assurance evidence remain distinct. Being evidence is contextual, not an intrinsic property of every recorded fact; preservation does not establish an assurance conclusion.

Provenance & Traceability preserves the meaningful attribution and history needed to explain information and significant activity. It connects source, transformation and required checkpoints without making every internal mechanism permanent evidence. Its recurring contribution supports accountability, reconstruction and assurance use while retaining the context that makes a recorded fact evidentially relevant.

**Boundary**

Telemetry, provenance, audit and assurance evidence remain distinct. Recording evidence neither establishes an assurance conclusion nor automatically requires retention of routine technical activity.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-07 |
| Snapshot publication reference | EC-07 |
| ArchiMate semantic ID | id-d71457435a085e749ffb1e1a3b551228 |
| Architecture Identity | EC-07 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Provenance & Traceability |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-07 |
| Structural aliases | ["EC-07"] |

Parent: [Trust, Accountability & Assurance](enterprise-capabilities.md#hcm-ec-trust-accountability-assurance). This assertion retains Proposed placement standing.

Immediate decomposition (independent assertion standing is in [relationships](relationships.md)):

- [Governed Activity Audit Evidence](enterprise-capabilities.md#hcm-ec-governed-activity-audit-evidence) — Proposed; placement Proposed
- [Information Attribution & Lineage](enterprise-capabilities.md#hcm-ec-information-attribution-lineage) — Proposed; placement Proposed

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-07`) and the element's version-qualified Properties.

<a id="hcm-ec-governed-activity-audit-evidence"></a>

## Governed Activity Audit Evidence

Capture required significant activity and decision evidence under its applicable governed capture authority.

**Overview**

Denied actions and successful mutations may both require trustworthy evidence; the audited identity and capture authority remain distinct.

**Intent**

Preserve required significant action evidence under its proper capture authority.

**Boundary**

Mandatory capture authority is specific to the established audit responsibility, not a platform-wide permission shortcut.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-governed-activity-audit-evidence |
| Snapshot publication reference | HCM-EC-governed-activity-audit-evidence |
| ArchiMate semantic ID | id-606f9b0a3e6a51b5a9f6be7518853d78 |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-07.CT3-EC-GOVERNED-ACTIVITY-AUDIT-EVIDENCE |

Parent: [Provenance & Traceability](enterprise-capabilities.md#ec-07). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-governed-activity-audit-evidence`) and the element's version-qualified Properties.

<a id="hcm-ec-information-attribution-lineage"></a>

## Information Attribution & Lineage

Preserve source attribution and significant transformation relationships for managed information.

**Overview**

Lineage explains how an assertion reached its present form without turning every infrastructure event into provenance.

**Intent**

Explain the information-significant origin and transformation of an assertion.

**Boundary**

Does not infer clinical credibility from a successful transport or establish independent assurance findings.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | proposal:ec-information-attribution-lineage |
| Snapshot publication reference | HCM-EC-information-attribution-lineage |
| ArchiMate semantic ID | id-08b52c79a6275638bdaa2e33d701aaea |
| Architecture Identity | Unresolved — no allocation |
| Construct | enterprise_capability |
| Tier/classification | CT3 |
| Architectural standing | Proposed |
| Name standing | Proposed |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-07.CT3-EC-INFORMATION-ATTRIBUTION-LINEAGE |

Parent: [Provenance & Traceability](enterprise-capabilities.md#ec-07). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `proposal:ec-information-attribution-lineage`) and the element's version-qualified Properties.

<a id="ec-14"></a>

## Service Guardian

- **Architectural Scope**: Service Guardian provides the reusable capability to independently establish whether governed service behaviour, state or outcomes remain within defined assurance expectations by associating applicable criteria and trustworthy evidence with a governed subject, assessing that evidence, adjudicating the assessment, and establishing reportable assurance findings and conclusions.
- **Functional Responsibilities**:
  - Supplying assurance-specific semantics for assurance disposition and assurance criteria applicability.
  - Associating a governed subject/context with applicable criteria and sufficient trustworthy evidence for a particular assurance purpose.
  - Assessing evidence against applicable criteria and adjudicating assessed detail to establish reportable findings, exceptions and conclusions.
  - Preserving assurance independence, temporal/version-specific evidence and criteria applicability, and explicit inability to establish a conclusion where evidence is insufficient.
- **Collaborative Nature**: Supplies the assurance-specific semantics required to bind existing reusable capabilities into governed assurance. It contributes directly to Assurance Design, Assurance Criteria Management and Governed Assurance; direct contribution is not sole realisation. The [approved matrix](../../02-strategy/capability-maps/health-service-assurance-derivation.md#collaborative-enterprise-capability-contribution-matrix) preserves the contributions of EC-01 through EC-13.
- **Anti-Responsibilities**: Does not own the underlying governed subject, authoritative governance requirements, or source information used as evidence. Does not perform, manage, remediate, assign, delegate or operationally escalate the subject activity. Does not replace System Operations or EC-12 Operational Assurance, and does not provide Clinical Services Delivery Assurance.
- **Allocation Boundary**: No strategic logical component, application component, runtime service or implementation construct is allocated EC-14. The catalogue defines a capability, not a software component or deployment boundary.

#### Conceptual Behavioural Sequence

```text
Define Good Behaviour
    ↓
Gather Evidence
    ↓
Assess Evidence against Good Behaviour
    ↓
Adjudicate Assessed Detail
    ↓
Report
```

This sequence explains the capability; it is not an implementation workflow specification or a derived Business Process. “Define Good Behaviour” identifies assurance expectations and criteria; it does not transfer ownership of governing requirements to Service Guardian. Source information ownership remains with its responsible capability; assurance owns its contextual evidentiary association.

The [Business Enabling assurance principles](../../02-strategy/capabilities/business-enabling-capabilities.md#health-service-assurance-approved-business-enabling-capabilities) govern disposition, generic processing assurance, evidence/criteria temporality, assessment/adjudication, independence, failure/recovery and explicit evidence insufficiency. These semantics are not universally reducible to PASS / FAIL / UNKNOWN and do not imply an assurance information model or persistence mechanism.

Service Guardian supplies the reusable assurance semantics that bind a governed subject to applicable criteria and trustworthy evidence, support assessment and adjudication, and establish reportable findings. Its contribution distinguishes explicit evidence insufficiency from a conclusion about the subject. Existing Enterprise Capabilities contribute collaboratively; EC-14 does not duplicate all their information, policy or execution responsibilities.

Independence concerns control of evaluation and conclusion. A subject may supply evidence but cannot solely determine, suppress or retrospectively change its own assurance outcome.

**Boundary**

The capability does not manage or remediate the subject, replace operational assurance, or assure clinical adequacy. No application component, runtime service or deployment boundary is allocated, and the explanatory sequence is not a derived Business Process.

### Governed record

| Field | Value |
| --- | --- |
| Source capture key | enterprise:ec-14 |
| Snapshot publication reference | EC-14 |
| ArchiMate semantic ID | id-4dbf578f52bd5b3b9301b9807ce60f30 |
| Architecture Identity | EC-14 |
| Construct | enterprise_capability |
| Tier/classification | CT2 |
| Architectural standing | Canonical |
| Name standing | Canonical |
| Canonical name | Service Guardian |
| Record currentness | current |
| Placement standing | Proposed |
| Structural ID standing | Proposed |
| Structural Canonical ID | CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-14 |
| Structural aliases | ["EC-14"] |

Parent: [Trust, Accountability & Assurance](enterprise-capabilities.md#hcm-ec-trust-accountability-assurance). This assertion retains Proposed placement standing.

No further governed Capability decomposition recorded in this baseline. The anchor View is retained.

Exact source locators, provenance, name/identity history and deferred assertions are retained in [architecture.json](architecture.json) (record `enterprise:ec-14`) and the element's version-qualified Properties.
