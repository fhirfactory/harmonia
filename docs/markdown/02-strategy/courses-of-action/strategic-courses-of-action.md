# Strategic Courses of Action Catalogue

## Overview

This catalogue establishes the six authoritative **Strategic Courses of Action** for Harmonia. Each Course of Action defines a technology-neutral architectural approach that configures Harmonia's resources and capabilities to satisfy the foundational drivers, goals, and axioms established in **Domain 01 (Motivation)**.

All Courses of Action comply with the five-point quality test: they possess explicit motivational traceability, materially shape Enterprise Capabilities (`EC-01` through `EC-13`), remain invariant under technology substitution, provide broad platform guidance, and offer actionable direction for downstream domains (Domains 03–13).

---

## 1. Catalogue of Strategic Courses of Action

### COA-01: Boundary Membrane Sovereignty

#### Strategic Approach
> **Preserve strict separation between external standards-compliant exchange representations and internal Harmonia-governed operational semantics.**

Harmonia acts as an integration environment bridging heterogeneous external healthcare systems and internal integration services. This course of action establishes that:
- External exchange contracts (such as FHIR REST interactions, MLLP message streams, or directory protocols) govern only the interaction semantics at the enterprise boundary.
- Internal Harmonia processing, context propagation, execution coordination, and state management must never adopt an external exchange representation as their private operational domain model.
- Egress across the boundary terminates Harmonia's governance of the emitted representation.

#### Motivational Grounding & Traceability
- **Architectural Axioms**:
  - `AX-02` (Boundary Membrane): External representations are boundary projections; internal semantics are sovereign.
  - `AX-03` (Native Standards): Standards govern interoperability at the boundary; standards are not distorted to fit private operational models.
  - `AX-13` (Explicit Management Boundary): Egress terminates platform management; internal operational metadata does not leak across the boundary.
  - `AX-14` (Preserve Distinctions): Distinctions between external exchange syntax and internal operational meaning are preserved.
- **Strategic Drivers & Goals**: Interoperability Mandates, Vendor Independence, Unified Ingress/Egress Governance.

#### Capability Realisation & Configuration
- **Primary Capabilities**: Shapes `EC-08` (Interoperability & Exchange) and `EC-13` (Semantic Governance & Conformance).
- **Component Boundary Impact**: Informs the boundary responsibility of **Pylai**, establishing it as the standards-facing membrane rather than an internal processing engine or generic transport layer.

#### Downstream Direction (Domains 03–13)
- Application Architecture (Domain 05) and Integration Architecture (Domain 06) must implement fail-closed boundary projections, ensuring private coordination metadata (e.g., distributed transaction IDs, internal routing tags) is never emitted externally.
- Information Architecture (Domain 04) must maintain distinct canonical information models rather than treating external FHIR structures as the sole internal representation.

---

### COA-02: Distinct Management and Durable Preservation of Information and State

#### Strategic Approach
> **Maintain a clear separation between the governed management of Harmonia information and state and its durable preservation and recovery, allowing runtime management and persistence concerns to evolve independently without compromising information integrity.**

This course of action establishes that:
- Runtime management of information, active relationships, context, and state is an operational governance concern requiring high availability, concurrent access, and responsive query navigation.
- Authoritative durable state establishment, preservation and recovery atomically establish authoritative state and authoritative version progression, preserve the durable management metadata required to interpret that state, and ensure historical retention, immutability, and state reconstruction.
- The distinction is strictly one of **architectural responsibility**, not a crude division between "ephemeral data" and "durable data". Information managed at runtime frequently represents durable healthcare concepts, but runtime management and durable preservation fulfill distinct architectural purposes.

#### Motivational Grounding & Traceability
- **Architectural Axioms**:
  - `AX-05` (Active vs Durable State): Application-facing active state access/coordination and authoritative durable state/version establishment remain separate architectural concerns.
  - `AX-09` (Ephemeral Operational State): Transient execution states are reconstructable and decoupled from durable health records.
  - `AX-11` (High Availability & Responsiveness): Information access must not be blocked by backend persistence latencies or recovery locks.
  - `AX-14` (Preserve Distinctions): Preserve the boundary between runtime information governance and storage mechanics.
- **Strategic Drivers & Goals**: 24/7 Clinical Operational Continuity, High-Throughput Access, Authoritative Preservation.

#### Capability Realisation & Configuration
- **Primary Capabilities**: Shapes `EC-03` (State & Lifecycle Governance) and `EC-04` (Information Management & Access).
- **Component Boundary Impact**: Explicitly establishes the seam between **Mneme** (application-facing access and active information/state management) and **Mnemosyne** (authoritative durable state/version establishment, preservation and recovery). Neither component subsumes the other; Mnemosyne does not own operational activity progression, workflow execution, active distributed state, Digital Twin coordination, or application-facing query interfaces. Mneme may reject or coordinate a proposed state progression before persistence; only Mnemosyne establishes the new authoritative durable state. Following authoritative commit, Mneme converges its active representation toward that state. Mneme active-state generation and Mnemosyne authoritative version remain distinct concurrency domains (`AX-05`).

#### Downstream Direction (Domains 03–13)
- Application Architecture (Domain 05) must forbid direct presentation/application access to database/JPA layers (enforcing Invariant 8).
- Technology Architecture (Domain 07) must allow distributed in-memory data fabrics (for runtime state) and durable relational/document stores (for preservation) to be configured and scaled independently.

---

### COA-03: Meaning-Centric Provenance and Traceability

#### Strategic Approach
> **Preserve provenance, authority, attribution and traceability for information-significant and business-significant actions and state changes, while avoiding unnecessary elevation of transient operational mechanics into enduring business evidence.**

Healthcare integration requires undeniable accountability without overwhelming storage or logging systems with low-level operational noise. This course of action establishes that:
- Every action that modifies clinical information, asserts authority, alters lifecycle state, or executes an access decision must capture semantic provenance: who, what, when, why, and under whose authority.
- Transient operational mechanics (e.g., thread switches, socket retries, queue polling, network pinging) represent operational telemetry, not business or clinical evidence.
- Strategy sets the architectural requirement for semantic attribution and non-repudiation without dictating specific implementation mechanisms (such as cryptographic signatures, write-once ledgers, or specific storage engines) at this layer.

#### Motivational Grounding & Traceability
- **Architectural Axioms**:
  - `AX-06` (Explicit Authority): Every state change and action must trace to an explicit, authenticated authority.
  - `AX-07` (Intrinsic Security): Security, authorization, and provenance are embedded within operations, not bolted on.
  - `AX-08` (Meaning over Machinery): Record semantic and business meaning, not transient technical machinery.
  - `AX-14` (Preserve Distinctions): Distinguish enduring clinical/business evidence from transient technical logs.
- **Strategic Drivers & Goals**: Regulatory Accountability, Clinical Governance, Legal Auditability, Evidentiary Integrity.

#### Capability Realisation & Configuration
- **Primary Capabilities**: Shapes `EC-06` (Policy & Control) and `EC-07` (Provenance & Traceability).
- **Component Boundary Impact**: Governs how all logical components emit evidence, ensuring that audit trails capture governed business assertions rather than unmasked PHI or low-level machine noise (supporting Invariants 6 and 7).

#### Downstream Direction (Domains 03–13)
- Security Architecture (Domain 08) and Information Architecture (Domain 04) define the concrete audit and provenance schemas (Kleio evidence trails) ensuring non-PHI audit capture and cryptographic verification downstream.

---

### COA-04: Governed Asynchronous Activity Progression

#### Strategic Approach
> **Progress multi-stage operational activities through explicit, observable unit-of-work transitions coordinated with entity state, separating execution from boundary exchange.**

Complex healthcare integrations (e.g., closed-loop diagnostic ordering, multi-destination clinical document fan-out) span multiple distributed systems and cannot rely on brittle synchronous distributed transactions. This course of action establishes that:
- Work progresses through discrete, observable units of work whose lifecycle state transitions are explicitly tracked and auditable.
- Operational progression progresses in lockstep with governed entity state (`AX-16`), preserving explicit uncertainty (`AX-15`) when external responses are pending or failed.
- Execution progression is strictly decoupled from boundary transport and exchange mechanics: activity execution determines *that* an external interaction is needed, but does not own transport protocols or wire-level retries.

#### Motivational Grounding & Traceability
- **Architectural Axioms**:
  - `AX-10` (Distribution & Normal Failure): Systems fail normally; activity progression must be resilient, idempotent, and restartable.
  - `AX-15` (Preserve Uncertainty): Never assume success or failure; pending, timed-out, or ambiguous outcomes must be explicitly modeled.
  - `AX-16` (Activity & State Progress Together): Operational activity and managed information state advance in coordinated lockstep.
- **Strategic Drivers & Goals**: Resilient Regional Integration, Asynchronous Clinical Decoupling, Bounded Workflow Recovery.

#### Capability Realisation & Configuration
- **Primary Capabilities**: Shapes `EC-03` (State & Lifecycle Governance), `EC-09` (Event & Subscription Management), and `EC-10` (Activity & Execution Coordination).
- **Component Boundary Impact**: Grounds the responsibility of **Ponos** as the execution engine for governed units of work, while strictly enforcing Guardrail G4 (Ponos progresses execution, but does not acquire transport adapters or wire protocols).

#### Downstream Direction (Domains 03–13)
- Application Architecture (Domain 05) and Integration Architecture (Domain 06) implement durable task envelopes (Pragma), resilient messaging (Petasos), and explicit fan-out tracking extensions (enforcing Invariants 4 and 5).

---

### COA-05: Entity-Centred Operational Coordination

#### Strategic Approach
> **Coordinate governed information, state and operational activity around real-world healthcare entities where those entities are operationally significant in their own right and require active management, without requiring every managed entity to maintain a permanently active execution construct.**

Healthcare operations naturally center around authentic real-world entities: patients, practitioners, care teams, wards, and beds. This course of action establishes that:
- Harmonia organizes the coordination of state and ongoing activities around the operational identity of the real-world entity itself.
- **The Digital Twin is the architectural construct** through which this entity-centred coordination strategy is realised. A Twin coordinates information, state, and activity across the Mneme/Ponos seam for a specific real-world entity.
- **Demand-Driven Lifecycle**: A managed entity does not require a permanently running execution thread or process. Twins are activated when entity-centred operational activity or active monitoring is required, and become quiescent when work concludes.
- **Representation $\neq$ Twin**: Simply possessing a database record or FHIR resource does not justify or constitute a Digital Twin. Coordination is justified only when the entity is operationally active and evolving in state.

#### Motivational Grounding & Traceability
- **Architectural Axioms**:
  - `AX-01` (Health-Information Centric): Architecture aligns with genuine healthcare entities and clinical relationships.
  - `AX-11` (High Availability & Responsiveness): High concurrency is achieved by isolating entity coordination without central serialisation bottlenecks.
  - `AX-16` (Activity & State Progress Together): Entity state and active operational workflows are coordinated dynamically.
- **Strategic Drivers & Goals**: Holistic Patient-Centric Care, High Concurrency without Serialization, Real-Time Healthcare Coordination.

#### Capability Realisation & Configuration
- **Primary Capabilities**: Shapes `EC-01` (Managed Entity & Relationship), `EC-02` (Context Management), and `EC-10` (Activity & Execution Coordination).
- **Component Boundary Impact**: Validates the **Digital Twin** as an active management construct bridging the **Mneme ↔ Ponos** boundary, while asserting that the Twin is an architectural construct rather than an independent deployable platform component.

#### Downstream Direction (Domains 03–13)
- Application Architecture (Domain 05) must model Twin archetypes using composite context (combining multiple resources such as Practitioner, PractitionerRole, and Location) rather than equating twins to single FHIR resources or permanent background threads.

---

### COA-06: Collaborative Cross-Cutting Capability Realisation

#### Strategic Approach
> **Realize cross-cutting platform capabilities (context, policy, provenance, assurance) collaboratively across participating components rather than through centralized, bottlenecked runtime services.**

Harmonia requires pervasive platform capabilities—such as security policy evaluation, contextual propagation, operational assurance, and provenance tracking—across all transactions. This course of action establishes that:
- **Reusable Capability $\neq$ Centralised Service (Guardrail G1)**: The existence of a reusable enterprise capability does not imply that a single monolithic service must execute it centrally.
- Cross-cutting capabilities define common contracts, schemas, and semantic rules, which are evaluated locally and collaboratively across all participating components.
- Centralized choke points and synchronous runtime bottlenecks are strictly avoided, ensuring horizontal scalability and fault isolation.

#### Motivational Grounding & Traceability
- **Architectural Axioms**:
  - `AX-04` (Own Semantics / Use Machinery): Maintain sovereign capability rules while delegating execution across components.
  - `AX-07` (Intrinsic Security): Security and policy evaluation must be pervasive and intrinsic to all operations, not an external proxy.
  - `AX-11` (High Availability & Responsiveness): Eliminate single points of congestion and failure across the integration fabric.
- **Strategic Drivers & Goals**: High Scalability, Fault Tolerance, Zero Bottleneck Architecture, Decentralised Resilience.

#### Capability Realisation & Configuration
- **Primary Capabilities**: Shapes `EC-02` (Context Management), `EC-06` (Policy & Control), `EC-07` (Provenance & Traceability), and `EC-12` (Operational Assurance).
- **Component Boundary Impact**: Prevents components like **Themis** (Policy & Control) from becoming synchronous network choke points for every micro-operation; policy contracts are distributed and evaluated collaboratively.

#### Downstream Direction (Domains 03–13)
- Application Architecture (Domain 05) and Technology Architecture (Domain 07) must package cross-cutting capabilities as clean API contracts (`themis-api`, `petasos-api`) that can be embedded locally within subsystem runtimes.

---

## 2. Traceability & Alignment Matrix

The following matrix synthesises the strategic alignment between Domain 01 Motivational foundations, the six Strategic Courses of Action, and Domain 02 Enterprise Capabilities:

| Course of Action | Core Motivational Axioms | Key Strategic Drivers & Goals | Supported Enterprise Capabilities | Key Downstream Architectural Constraint |
| :--- | :--- | :--- | :--- | :--- |
| **COA-01: Boundary Membrane Sovereignty** | `AX-02`, `AX-03`, `AX-13`, `AX-14` | Interoperability Mandates, Vendor Independence | `EC-08`, `EC-13` | External standards (FHIR) govern boundary interaction, never internal domain/execution models. |
| **COA-02: Distinct Management & Preservation of State** | `AX-05`, `AX-09`, `AX-11`, `AX-14` | 24/7 Availability, Enduring Information Preservation | `EC-03`, `EC-04` | Mneme manages active access/state; Mnemosyne establishes authoritative durable state/versions and preserves/recovers them. No direct DB access by apps. |
| **COA-03: Meaning-Centric Provenance & Traceability** | `AX-06`, `AX-07`, `AX-08`, `AX-14` | Clinical Governance, Legal Auditability | `EC-06`, `EC-07` | Business and security assertions captured as non-PHI evidence; transient mechanics treated as telemetry. |
| **COA-04: Governed Asynchronous Activity Progression** | `AX-10`, `AX-15`, `AX-16` | Asynchronous Integration, Resilient Recovery | `EC-03`, `EC-09`, `EC-10` | Units of work track discrete state; execution progression is strictly decoupled from transport/connectivity. |
| **COA-05: Entity-Centred Operational Coordination** | `AX-01`, `AX-11`, `AX-16` | Patient-Centric Care, High Horizontal Concurrency | `EC-01`, `EC-02`, `EC-10` | Digital Twins coordinate entity state/activity across Mneme/Ponos on demand; twin $\neq$ single FHIR resource. |
| **COA-06: Collaborative Cross-Cutting Capability Realisation** | `AX-04`, `AX-07`, `AX-11` | Scalability, Zero Central Bottlenecks | `EC-02`, `EC-06`, `EC-07`, `EC-12` | Reusable capabilities implemented collaboratively via local contracts, preventing monolithic choke points. |

---

## 3. Summary

Together, these six Courses of Action provide a comprehensive, technology-neutral strategic framework. They guide how Harmonia realizes its enterprise capabilities, respects its foundational architectural axioms, and bounds the responsibilities of its strategic logical components without premature commitment to specific software libraries or runtime technologies.
