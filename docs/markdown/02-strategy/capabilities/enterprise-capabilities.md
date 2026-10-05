# Canonical Enterprise Capability Model (EC-01 .. EC-13)

## Overview & Purpose

The Enterprise Capability Tier represents **reusable ICT architectural functionality** that recurs across multiple distinct healthcare business enabling capabilities and features.

Enterprise Capabilities answer the fundamental architectural question:
> **"How is this function delivered across our systems?"**

Where Business Enabling capabilities are defined within authentic clinical, operational, and administrative healthcare contexts (e.g., Ward Census, Medication Administration, Closed-Loop Orders), Enterprise Capabilities represent the **horizontal, domain-neutral capabilities** through which those healthcare features are delivered.

```text
┌────────────────────────────────────────────────────────────────────────┐
│ BUSINESS ENABLING FEATURES (Hundreds of domain-specific behaviours)    │
│ Example: Patient Bed Turnover, Pathology Order, Practitioner Lookup    │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ derived across features by asking:
                                    │ "How is this function delivered?"
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ ENTERPRISE CAPABILITY TIER (EC-01 .. EC-13)                            │
│ 13 Reusable, Technology-Neutral Architectural Capabilities             │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ informs clustering of
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│ STRATEGIC LOGICAL COMPONENTS (Pass B Responsibility Model)             │
│ Mneme, Mnemosyne, Ponos, Pylai, Calliope, Iris, Digital Twin           │
└────────────────────────────────────────────────────────────────────────┘
```

---

## Derivation Methodology & Architectural Principles

The 13 Enterprise Capabilities were derived by systematically analyzing the system-enabled features across all five Business Enabling views. The derivation adheres to five strict architectural principles:

### 1. Vertical Recurrence Test
A functional requirement qualifies as an Enterprise Capability only if it recurs vertically across multiple, diverse healthcare domains.
- If a function appears only within laboratory orders, it remains domain-specific Business Enabling behaviour.
- If a function recurs across clinical document versioning, directory update proposals, and bed state turnover, it reflects an underlying Enterprise Capability (**EC-03 Managed State & Lifecycle**).

### 2. Behavioural Properties vs. Top-Level Capabilities
Not every recurring function or algorithmic mechanism warrants promotion to a standalone top-level Enterprise Capability:
- **Correlation** is an intrinsic behavioural property appearing across multiple capabilities (e.g., entity correlation in EC-01, event correlation in EC-09, context correlation in EC-02), **not** a standalone `CorrelationService`.
- **Acknowledgement** is a reusable interaction pattern within exchange semantics (EC-08) and operational activity tracking (EC-10), **not** an independent capability.
- **Assignment and Dispatch** are core execution functions within **EC-10 Activity & Execution**.
- **Indexing and Caching** are technical enabling mechanisms within **EC-04 Information Management** and **EC-05 Search & Discovery**, not top-level architectural capabilities.
- **Retry and Circuit Breaking** are resilience properties within **EC-12 Operational Assurance**.

### 3. Reusable Capability $\neq$ Centralised Service
> **An Enterprise Capability describes reusable architectural functionality; it does NOT imply a monolithic, centralized runtime service.**

Some Enterprise Capabilities are inherently cross-cutting and collaborative:
- **EC-02 Context Management**: Context must be established at ingress (Pylai), maintained during active access (Mneme), propagated across execution units (Ponos), and preserved in audit (Kleio). It cannot be isolated into a single "Context Service".
- **EC-06 Policy & Control**: Policy evaluation must guard ingress boundaries, storage persistence interfaces, and presentation gateways alike.
- **EC-07 Provenance & Traceability**: Attribution and audit evidence must be captured across every boundary hop and state transformation.
- **EC-12 Operational Assurance**: Resilience, duplicate suppression, and concurrency governance must be enforced collaboratively across messaging, storage, and execution layers.

Artificially allocating these cross-cutting capabilities to a single component merely for diagrammatic neatness violates distributed resilience and sound architectural decomposition.

### 4. Zero Technology Leakage
Enterprise Capabilities are technology-neutral:
- They are free of concurrency primitives (threads, worker thread pools, execution daemons).
- They are free of message broker constructs (topics, queues, partitions, JMS headers).
- They are free of storage technologies (PostgreSQL, Infinispan, JPA, DDL, table locks).
- They are free of specific software packages (HAPI FHIR, Apache Camel, Netty, Spring Boot, Vue 3).

### 5. Capabilities vs. Software Components
An Enterprise Capability describes *what* functionality is provided. Subsystems (such as Pylai, Ponos, Mneme, Mnemosyne, Calliope, and Iris) represent *strategic logical component responsibility centres* that will be elaborated in Pass B. Software components are consumers, aggregators, or providers of capabilities; **a software component name is never a capability definition**.

---

## The 13 Canonical Enterprise Capabilities

```text
┌────────────────────────────────────────────────────────────────────────┐
│  EC-01  Managed Entity & Relationship                                  │
│  EC-02  Context Management                                             │
│  EC-03  Managed State & Lifecycle                                      │
│  EC-04  Information Management                                         │
│  EC-05  Search & Discovery                                             │
│  EC-06  Policy & Control                                               │
│  EC-07  Provenance & Traceability                                      │
│  EC-08  Interoperability & Exchange                                    │
│  EC-09  Event & Subscription                                           │
│  EC-10  Activity & Execution                                           │
│  EC-11  Interaction & Experience                                       │
│  EC-12  Operational Assurance                                          │
│  EC-13  Semantic Governance & Conformance                              │
└────────────────────────────────────────────────────────────────────────┘
```

---

### EC-01: Managed Entity & Relationship
- **Architectural Scope**: Identify, classify, relate, and maintain the structural integrity of managed healthcare and administrative entities throughout the enterprise landscape.
- **Functional Responsibilities**:
  - Uniquely identifying core domain entities (Persons, Practitioners, Roles, Organisations, Locations, Services, Devices).
  - Resolving and maintaining multi-authority identifier systems and aliases without destructive overwriting.
  - Representing and traversing multi-dimensional relationships (affiliations, organizational hierarchies, physical location trees, care team memberships).
  - Maintaining referential integrity across interconnected entity graphs.
- **Anti-Responsibilities**: Does not own physical database relational tables or foreign key constraints (Domain 04/07).

### EC-02: Context Management
- **Architectural Scope**: Establish, validate, propagate, and preserve the execution, clinical, security, and administrative context within which information and activities have meaning.
- **Functional Responsibilities**:
  - Binding the subject of care, encounter, ordering provider, and healthcare service context to in-flight messages and transactions.
  - Propagating tamper-evident, attributable security context across asynchronous, multi-hop operational boundaries.
  - Ensuring context preservation during cross-protocol transformations across disparate healthcare exchange formats and protocols.
  - Detecting and rejecting requests where mandatory operational or clinical context is missing or inconsistent.
- **Collaborative Nature**: Cross-cutting capability realized collaboratively across boundary gateways, task envelopes, execution workers, and storage facades.

### EC-03: Managed State & Lifecycle
- **Architectural Scope**: Govern the current and historical state, effective temporal periods, state transition validations, and lifecycle progression of managed integration artifacts and entities.
- **Functional Responsibilities**:
  - Defining and enforcing valid state transition state machines (e.g., `REQUESTED` $\to$ `VALIDATED` $\to$ `IN_PROGRESS` $\to$ `COMPLETED`).
  - Maintaining temporal validity intervals (effective start and end dates) for relationships, credentials, and states.
  - Supporting non-destructive state progression, preserving historical state versions for longitudinal review.
  - Rejecting illegal or out-of-order state transitions while accommodating idempotent replay.
- **Anti-Responsibilities**: Does not define thread-level mutexes or database transaction isolation levels.

### EC-04: Information Management
- **Architectural Scope**: Validate, represent, persist, retrieve, version, and manage governed clinical, administrative, and operational information assets.
- **Functional Responsibilities**:
  - Validating inbound information structures against canonical information constraints.
  - Providing authoritative durable persistence for historical health records and administrative registries.
  - Managing active distributed working memory representations for low-latency retrieval.
  - Governing document versioning, supersession, amendments, and retractions without data loss.
- **Architectural Principle**: Enforces the foundational separation between *active distributed state access* and *authoritative durable state preservation* (Axiom AX-05).

### EC-05: Search & Discovery
- **Architectural Scope**: Index, search, resolve, filter, and discover information assets, entities, directory endpoints, and healthcare services across the enterprise.
- **Functional Responsibilities**:
  - Providing multi-criteria indexed search across complex entity graphs (e.g., find active cardiologists at Location X accepting new referrals).
  - Resolving service and practitioner communication endpoints for addressed delivery.
  - Supporting bounded, paginated clinical queries across longitudinal patient records.
  - Applying dynamic security and consent filters to search results prior to egress.
- **Anti-Responsibilities**: Does not dictate specific search engine indexes, inverted index implementations, or SQL query syntaxes.

### EC-06: Policy & Control
- **Architectural Scope**: Evaluate and enforce security, consent, privacy, and regulatory policies including authority, access control, and information handling rules.
- **Functional Responsibilities**:
  - Executing impartial, default-deny policy evaluations for all integration transactions.
  - Enforcing Role-Based and Attribute-Based Access Control (RBAC/ABAC) policies.
  - Evaluating patient consent directives and privacy restrictions at boundary and query checkpoints.
  - Enforcing statutory information disclosure boundaries (e.g., mental health, VIP records).
- **Collaborative Nature**: Evaluated across boundary gateways, internal bus dispatch, storage interfaces, and presentation layers.

### EC-07: Provenance & Traceability
- **Architectural Scope**: Preserve origin attribution, transformation history, execution checkpoints, tamper-evident audit evidence, and end-to-end traceability across the platform.
- **Functional Responsibilities**:
  - Recording attributable, verifiable origin metadata for information sources, authors, and timestamps.
  - Tracking transformation history and mapping checkpoints as information crosses system seams.
  - Capturing non-PHI operational and security audit trails for regulatory compliance (HIPAA, GDPR).
  - Supporting end-to-end transaction tracing across asynchronous processing pipelines.
- **Collaborative Nature**: Cross-cutting obligation embedded into message envelopes, gateway receipts, and storage mutations.

### EC-08: Interoperability & Exchange
- **Architectural Scope**: Resolve destinations, transform schemas, route, transport, deliver, acknowledge, and coordinate standards-based information exchange across enterprise boundaries.
- **Functional Responsibilities**:
  - Providing multi-protocol boundary adaptation for healthcare standards (HL7 v2, FHIR REST, DICOM, secure messaging).
  - Executing bidirectional structural and semantic transformations between legacy formats and canonical representations.
  - Ensuring reliable addressed delivery and fan-out distribution to external clinical endpoints.
  - Processing and distinguishing technical delivery acknowledgements from business acknowledgements.
- **Boundary Rule**: Egress terminates Harmonia management of the emitted representation (Axiom AX-13).

### EC-09: Event & Subscription
- **Architectural Scope**: Publish, ingest, correlate, filter, and syndicate events and subscription-driven changes across the enterprise landscape.
- **Functional Responsibilities**:
  - Ingesting operational and clinical event triggers from external systems and internal state changes.
  - Managing topic- and criteria-based event subscriptions for internal and external consumers.
  - Evaluating dynamic subscription filters to match event payloads with interested subscriber criteria.
  - Delivering asynchronous event notifications reliably to registered subscribers.
- **Decoupling Note**: Defined strictly in terms of event publishing, criteria matching, and notification delivery; entirely independent of broker topics, partitions, or queues.

### EC-10: Activity & Execution
- **Architectural Scope**: Define, instantiate, match, assign, dispatch, supervise, and progress coordinated operational and clinical activity units.
- **Functional Responsibilities**:
  - Instantiating operational work items (Work Orders, To Dos, synthetic platform Tasks) in response to triggers.
  - Matching and assigning work units to eligible workers, roles, or execution daemons based on policy rules.
  - Dispatching work orders to worker endpoints, devices, or execution queues with delivery confirmation.
  - Supervising execution progress, tracking timeouts, managing escalations, and handling activity interruptions.
- **Decoupling Note**: Free of worker threads, thread pools, execution daemons, or specific workflow runtime engines.

### EC-11: Interaction & Experience
- **Architectural Scope**: Provide contextual interaction between human actors, Harmonia, and enterprise presentation services, including visualization, alerting, and collaboration integration.
- **Functional Responsibilities**:
  - Mediating presentation interactions via dedicated backend-for-frontend gateways decoupled from internal databases.
  - Rendering clinical timelines, provider directory management interfaces, and operational console dashboards.
  - Providing contextual notifications and alerts to human users across web and mobile channels.
  - Projecting longitudinal record context into secure multidisciplinary collaboration spaces.
- **Boundary Note**: Does not absorb the healthcare semantics of Clinical Collaboration or medical documentation. Consumes established identity and security context; does not own authentication, consent, or clinical truth.

### EC-12: Operational Assurance
- **Architectural Scope**: Ensure concurrency integrity, system resilience, duplicate detection, exception handling, automated recovery, and operational telemetry across platform operations.
- **Functional Responsibilities**:
  - Enforcing sliding-window duplicate message detection and idempotent suppression.
  - Guaranteeing ingress dual-write safety (durable acceptance before emitting positive application ACK).
  - Managing structured failure recovery, exception escalation, and dead-letter isolation.
  - Emitting platform telemetry, operational health metrics, and queue processing metrics.
- **Collaborative Nature**: Realized collaboratively across boundary gateways, messaging infrastructure, execution workers, and storage layers.

### EC-13: Semantic Governance & Conformance
- **Architectural Scope**: Govern canonical models, schemas, terminologies, value sets, mapping tables, constraint rules, and semantic conformance validation across the enterprise.
- **Functional Responsibilities**:
  - Defining and publishing canonical healthcare data models and profile structures.
  - Governing clinical terminology mappings, value sets, and concept lookups (SNOMED CT, LOINC).
  - Validating inbound and outbound message representations against published semantic rules.
  - Governing semantic versioning, deprecation, and backward compatibility across schema revisions.
- **Component Relationship**: Informs the Calliope strategic logical component, but Calliope is the responsibility centre, not the capability definition.

---

## Summary Matrix: Enterprise Capabilities

| ID | Enterprise Capability Name | Architectural Focus & Essence | Key Recurring Functions |
| :--- | :--- | :--- | :--- |
| **EC-01** | Managed Entity & Relationship | Entity identification, graph relationships, referential integrity | Identity resolution, relationship navigation, alias linking |
| **EC-02** | Context Management | Establishing, validating, and propagating operational context | Context binding, multi-hop propagation, security tokens |
| **EC-03** | Managed State & Lifecycle | State machines, temporal validity, lifecycle progression | State validation, effective periods, non-destructive updates |
| **EC-04** | Information Management | Validation, durable persistence, active distributed state | Schema validation, durable preservation and recovery, active access |
| **EC-05** | Search & Discovery | Indexing, multi-criteria search, endpoint resolution | Indexed query, criteria filtering, endpoint discovery |
| **EC-06** | Policy & Control | Impartial default-deny security, access control, consent | RBAC/ABAC evaluation, consent enforcement, VIP gating |
| **EC-07** | Provenance & Traceability | Origin attribution, audit evidence, transformation history | Tamper-evident audit logging, source attribution, chain of custody |
| **EC-08** | Interoperability & Exchange | Multi-protocol boundary exchange, transport, delivery | Protocol mediation, transformation, routing, delivery ACK |
| **EC-09** | Event & Subscription | Event publishing, criteria filtering, notification dispatch | Subscription management, criteria matching, syndication |
| **EC-10** | Activity & Execution | Work unit instantiation, assignment, dispatch, supervision | Matching, dispatching, progress tracking, escalation |
| **EC-11** | Interaction & Experience | Decoupled presentation, user interaction, dashboards | BEFE mediation, clinical view rendering, alert display |
| **EC-12** | Operational Assurance | Resilience, concurrency integrity, duplicate suppression | Dual-write safety, deduplication, recovery, telemetry |
| **EC-13** | Semantic Governance & Conformance| Canonical schemas, terminologies, conformance rules | Schema publishing, concept mapping, constraint checking |

---

## Multi-Capability Composition Dynamics

Enterprise Capabilities do not operate in isolation; they compose harmoniously to deliver complex healthcare features. Below are three representative multi-capability compositions demonstrating this derivation logic:

### Composition 1: Person Identifier Resolution
- **Healthcare Need**: Resolving multi-authority identifiers (MRN, national IHI, clinic client IDs) for an incoming patient transaction.
- **Composed Enterprise Capabilities**:
  - `EC-01 Managed Entity & Relationship`: Resolves entity models and alias relationships.
  - `EC-05 Search & Discovery`: Searches existing identifier registers and resolves matches.
  - `EC-03 Managed State & Lifecycle`: Validates identifier active/superseded status.
  - `EC-07 Provenance & Traceability`: Records origin authority, timestamp, and audit trail.
  - `EC-06 Policy & Control`: Evaluates requesting system access permissions.
  - `EC-12 Operational Assurance`: Guarantees deterministic matching under concurrent load.

### Composition 2: Closed-Loop Order Progression
- **Healthcare Need**: Ingesting a diagnostic order, routing to a laboratory, and tracking execution through to report outcome.
- **Composed Enterprise Capabilities**:
  - `EC-02 Context Management`: Binds clinical encounter, patient, and ordering provider context.
  - `EC-05 Search & Discovery`: Resolves target laboratory endpoint and service capability.
  - `EC-01 Managed Entity & Relationship`: Links order entity to patient, provider, and service.
  - `EC-08 Interoperability & Exchange`: Manages boundary protocol delivery and captures technical ACK.
  - `EC-03 Managed State & Lifecycle`: Enforces order state progression (`PLACED` $\to$ `ACCEPTED` $\to$ `IN_PROGRESS` $\to$ `COMPLETED`).
  - `EC-10 Activity & Execution`: Instantiates activity units, monitors timeouts, and supervises fulfillment.
  - `EC-07 Provenance & Traceability`: Captures end-to-end chain of custody and delivery timestamps.
  - `EC-12 Operational Assurance`: Enforces dual-write safety and duplicate suppression.

### Composition 3: Work Allocation & Dispatch
- **Healthcare Need**: Coordinating operational facility tasks (e.g., patient transport, ward bed cleaning).
- **Composed Enterprise Capabilities**:
  - `EC-02 Context Management`: Binds patient, physical room/bed, and service context.
  - `EC-03 Managed State & Lifecycle`: Progresses work order state (`CREATED` $\to$ `DISPATCHED` $\to$ `IN_PROGRESS` $\to$ `DONE`).
  - `EC-05 Search & Discovery`: Discovers available and credentialed staff matching task criteria.
  - `EC-01 Managed Entity & Relationship`: Links assigned worker, patient, location, and equipment.
  - `EC-10 Activity & Execution`: Executes allocation logic, worker dispatch, and escalation oversight.
  - `EC-08 Interoperability & Exchange`: Delivers work orders to mobile devices or external systems.
  - `EC-12 Operational Assurance`: Prevents duplicate assignment and ensures resilient retry.
  - `EC-07 Provenance & Traceability`: Captures worker acceptance and task completion evidence.

---

## Downstream Progression

Enterprise Capabilities represent what reusable functions the platform delivers. In downstream architectural passes:
- **Authoring Pass B (Strategy Components & Resources)**: Evaluates how these capabilities cluster into the strategic logical component responsibility model (Mneme, Mnemosyne, Ponos, Pylai, Calliope, Iris) and candidate strategic resources.
- **Domains 05–07 (Application, Integration, Technology)**: Implements these capabilities through concrete software packages, integration protocols, and runtime technologies.
- [ICT Foundation Lenses](ict-foundation-lenses.md): Cross-cutting technical enablement considerations guiding technology realization.
- [Capability Tier Progression Model](../capability-maps/capability-tier-model.md): Detailed vertical derivation rules.
