# Architectural Axioms (AX-01 to AX-17)

## Overview

The Architectural Axioms are the highest-level design authority within the Harmonia Health Integration Environment (HIE). They describe the fundamental, enduring architectural truths upon which Harmonia is engineered. All architectural decisions, subsystem designs, implementation patterns, and code modifications must conform to these principles.

In accordance with platform governance, each axiom is documented using a standardized **three-tier structure**:
1. **Principle**: The normative, enduring, and technology-independent statement of truth.
2. **Architectural Implications**: What the principle mandates or restricts across downstream architecture domains.
3. **Current Harmonia Realisation**: Concrete technological and framework choices currently deployed within Harmonia (clearly delineated as non-normative realisations).

Historical identifiers are strictly preserved. Note that **AX-12** has been formally reclassified to Domain 05 Application Architecture (see [Historical Reclassifications](reclassified-principles.md)).

For [AX-17 — Architectural Authority and Explicit Uncertainty](../../../architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty), consult the authoritative axiom register. Its normative text is maintained there.

---

## AX-01: Harmonia Is Health-Information Centric

### 1. Principle
> **Harmonia is a distributed health-information management and interoperability framework. Its primary purpose is the ingestion, validation, transformation, governance, processing, persistence, distribution, and exposure of health-related information. Harmonia shall support applicable health-information standards and models while retaining the ability to manage information according to Harmonia's own internal operational requirements.**

### 2. Architectural Implications
- Health-information semantics take precedence over generic infrastructure convenience.
- Standards such as HL7 FHIR and HL7 v2 are first-class architectural concerns, not secondary payloads attached to a generic enterprise message bus.
- Generic infrastructure capabilities (brokers, databases, caches) should be utilized where advantageous, but shall never dictate Harmonia's clinical information semantics.
- *Deliberate Boundaries*: Harmonia is not merely a generic FHIR server, nor is it restricted exclusively to FHIR information. Not every internal Harmonia object must correspond directly to an external interoperability standard.

### 3. Current Harmonia Realisation
- Canonical health data structures and clinical models are defined in `calliope`, free from dependencies on higher-level orchestration or messaging middleware.
- Information authority, provenance, and privacy classifications are attached directly to managed health records.

---

## AX-02: Standards at the Boundary; Harmonia Within the Boundary

### 1. Principle
> **Harmonia shall ingest and expose health information using the standards, information models, and interoperability protocols applicable to an external interface contract. Within Harmonia, information may be augmented, represented, indexed, cached, distributed, governed, versioned, correlated, and persisted using Harmonia-specific semantics and metadata necessary to satisfy Harmonia's operational requirements. Harmonia's internal operational semantics are private to Harmonia. External systems shall not be required to understand, preserve, reproduce, or participate in them.**

### 2. Architectural Implications
- **Boundary Membrane Pattern**: System boundaries require agreement on interoperability contracts; internal architectures do not.
- Harmonia respects external wire contracts at its ingress and egress membranes, but preserves internal sovereignty over its operational data model.
- External systems must never be forced to understand or echo Harmonia-private distributed coordination metadata.
- Harmonia-private operational metadata (distributed lock tokens, cache versions, queue routing pragmas) shall not leak into external payloads.

### 3. Current Harmonia Realisation
- The `pylai` subsystem acts as an interoperability membrane (supporting MLLP, FHIR REST, DICOM) that transforms external wire formats into internal representations upon ingress, and projects internally managed representations into compliant standards-based models upon egress.

---

## AX-03: Native Standards Representations

### 1. Principle
> **Standards-defined information objects shall retain their standards-defined representation within Harmonia wherever practicable. Harmonia shall not introduce parallel or derivative representations solely to accommodate internal management concerns where the standard representation provides an appropriate extensibility mechanism.**

### 2. Architectural Implications
- Eliminates the anti-pattern of maintaining duplicate internal object models that mirror external standards, which introduces severe mapping overhead and semantic drift.
- Where a healthcare standard provides a native extensibility mechanism (such as FHIR extensions), Harmonia-specific durable management metadata is stored using those native facilities rather than separate companion objects.
- Non-standards-defined operational state (e.g., active task queues) utilizes representation-appropriate internal metadata structures.

### 3. Current Harmonia Realisation
- FHIR resources are maintained directly as native FHIR models using HAPI FHIR structures in `calliope`.
- Jurisdictional extensions (such as Australian AU-Base / AU-Core profiles) are treated as native standards elements, distinct from private Harmonia operational extensions.

---

## AX-04: Harmonia Owns the Semantics; Engines Provide the Machinery

### 1. Principle
> **Harmonia owns the architectural semantics governing information authority, security, lifecycle, concurrency, provenance, auditability, resilience, and operational integrity. Harmonia shall preferentially use capabilities supplied by underlying technology engines where those capabilities satisfy Harmonia's architectural invariants rather than reproduce equivalent functionality.**

### 2. Architectural Implications
- **Maxim**: *"Use the machinery; own the semantics."*
- Commodity infrastructure (relational databases, message brokers, caching engines) should be leveraged for physical execution, but their native operational behaviors do not define platform rules.
- An engine's technical features (such as database auto-incrementing IDs or JMS priority queues) must not be mistaken for business-level architectural semantics (such as information authority or clinical urgency).

### 3. Current Harmonia Realisation
- HAPI FHIR provides low-level FHIR parsing, indexing, and storage machinery.
- Infinispan provides distributed in-memory caching and atomic primitives.
- ActiveMQ Artemis provides durable messaging queues.
- PostgreSQL provides ACID relational persistence.
- Harmonia defines and governs all lifecycle, authority, and security semantics above these engines.

---

## AX-05: Active State and Authoritative Durable State Are Distinct

### 1. Principle
> **Active operational state and authoritative durable state are distinct architectural responsibilities. Active operational state encompasses application-facing information access, distributed in-memory coordination, active observation, and transient workflow coordination. Authoritative durable state encompasses the permanent, durable record of truth, authoritative version progression, and persistent governance metadata. Active operational state manages active use; authoritative durable state establishes durable truth.**

### 2. Architectural Implications
- Conflating active state with authoritative persistence leads to severe architectural failures: treating volatile in-memory caches as authoritative storage, or exposing transactional database schemas directly to application consumers.
- Active operational state may coordinate, observe, or validate state transitions, but only authoritative persistence can establish a new committed version.
- Upon authoritative commit, active operational state converges toward authoritative truth.
- Active operational state must be reconstructable from authoritative durable state.
- Loss of active operational nodes shall not promote uncommitted state into durable truth; loss of durable persistence shall not cause active cache state to be treated as durable.

### 3. Current Harmonia Realisation
- Segregated within the `hestia` subsystem:
  - **Mneme** owns application-facing access and active distributed state coordination (realized via Infinispan).
  - **Mnemosyne** alone establishes authoritative durable state and version progression (realized via PostgreSQL and JPA).
  - Presentation services (`iris`) consume Mneme client contracts and are strictly decoupled from direct database persistence.

---

## AX-06: Information Authority Is Explicit

### 1. Principle
> **Harmonia shall represent the authority and credibility of managed information independently of its technical transport, cache state, persistence state, or concurrency state. Harmonia shall support governed information-authority concepts, including Authoritative, Informational, and Anecdotal.**

### 2. Architectural Implications
- Technical correctness and information credibility are orthogonal:
  - Concurrency determines *whether* information has changed.
  - Semantic analysis determines *what* changed.
  - Information authority determines the *standing and trustworthiness* of competing assertions.
- A successfully persisted record is not necessarily clinically authoritative (it may be an unverified patient-entered note).
- Anecdotal or preliminary information is preserved with its authority tag intact rather than discarded or falsely upgraded to authoritative status.

### 3. Current Harmonia Realisation
- Authority classifications (`Authoritative`, `Informational`, `Anecdotal`) are formally modeled in `calliope` and evaluated by `themis` policy rules when resolving clinical data conflicts.

---

## AX-07: Security Is Intrinsic to Managed Operations

### 1. Principle
> **Every governed operation executes within an established security context and is subject to platform-enforced security policy. Security is intrinsic to Harmonia-managed operations rather than dependent upon voluntary caller behaviour.**

### 2. Architectural Implications
- Security is part of the operational semantics of the platform, not an optional perimeter filter or utility library invoked voluntarily by application code.
- All operations execute under a verifiable security context; unauthenticated or unauthorized actions default to `DENY`.
- Security context is transient operational context and does not automatically become persisted clinical content.
- Operational diagnostics must be PHI-safe: Protected Health Information must never be emitted into non-clinical logging streams.

### 3. Current Harmonia Realisation
- `themis` owns centralized security policy evaluation, providing default-deny access decisions (`ThemisDecision.DENY`).
- Canonical `ThemisSecurityContext` is propagated across thread and message boundaries.
- Downstream guardrails forbid unmasked PHI in diagnostic logs (`SEC-010`), and audit records are routed to `kleio`.

---

## AX-08: Evidence Records Meaning, Not Machinery

### 1. Principle
> **Provenance and audit evidence shall describe information-significant, security-significant, and business-significant events, assertions, and decisions. Internal implementation mechanics shall not ordinarily become provenance or audit evidence merely because they occurred.**

### 2. Architectural Implications
- **Maxim**: *"Preserve the meaningful fact, not every mechanism that produced it."*
- The durable audit trail exists to answer clinical, legal, and regulatory questions (who accessed or modified this record, when, and under what authority), not to serve as an unbounded distributed debug log.
- Infrastructure events (cache misses, network retries, thread pool adjustments) belong in transient operational logs or telemetry, not in durable audit evidence.

### 3. Current Harmonia Realisation
- `kleio` manages durable audit evidence, recording structured FHIR `AuditEvent` and `Provenance` resources.
- Internal messaging and synchronization mechanics are segregated into diagnostic logging streams.

---

## AX-09: Transient Operational State Is Ephemeral by Default

### 1. Principle
> **Harmonia may maintain transient operational state associated with managed information where required for caching, persistence, concurrency, security, routing, resilience, diagnostics, and processing integrity. Such state shall be retained only for as long as required by its operational purpose and shall normally disappear when that purpose has been satisfied. Transient operational state shall not automatically become provenance or audit evidence.**

### 2. Architectural Implications
- Prevents database bloat and memory leaks in high-throughput integration pipelines.
- Operational coordination artifacts (such as routing tokens, temporary locks, and intermediate task pragmas) are purged once the corresponding operational activity completes.
- Controlled diagnostic mechanisms must allow engineers to observe transient operational state when explicitly enabled.

### 3. Current Harmonia Realisation
- In-memory lock entries in Infinispan configure explicit time-to-live (TTL) and eviction policies.
- Task execution envelopes in `energeia` are finalized and decommissioned upon delivery confirmation.

---

## AX-10: Distribution, Load and Failure Are Normal Operating Conditions

### 1. Principle
> **Harmonia shall be designed on the assumption of distributed deployment, sustained processing load, and failure or degradation of individual runtime components. Loss or degradation of implementation machinery shall not silently alter the authoritative, security, governance, or information-authority semantics of managed information.**

### 2. Architectural Implications
- Hardware, operating systems, networks, and remote endpoints will fail. The platform must be fail-safe.
- Work that crosses an authoritative durable boundary must be recoverable following a crash.
- Increased workload must be expressed as bounded, durable backpressure rather than unbounded heap growth or thread exhaustion.
- Failures must be explicitly surfaced; components must never fall back to weaker local behavior that compromises consistency.

### 3. Current Harmonia Realisation
- `petasos` provides durable message queues and guaranteed delivery semantics.
- Gateway ingress dual-write verification (`REC-001`) ensures messages are persisted before positive transport acknowledgement.
- ArchUnit tests enforce isolation and architectural invariants continuously.

---

## AX-11: Managed Information Access Is Highly Available and Responsive

### 1. Principle
> **Harmonia shall provide highly available, responsive, and load-tolerant access to managed information and active coordination state. The architecture shall favour bounded resource consumption, distributed workload processing, reconstructable active state, and graceful expression of processing pressure. Availability shall not be achieved by weakening authoritative-state, concurrency, security, governance, or information-authority guarantees.**

### 2. Architectural Implications
- **Technology-Independent**: Does not mandate specific caching products or reactive libraries.
- High-frequency read queries from clinical users and operational components must remain responsive and decoupled from the latency of heavy transactional write persistence.
- Different categories of managed state may employ different distribution and consistency topologies appropriate to their clinical urgency.

### 3. Current Harmonia Realisation
- Implemented via `hestia-mneme` using distributed Infinispan caches configured for near-cache reads, isolating presentation layers (`iris`) from direct relational database lock contention.

---

## AX-13: Harmonia Management Has an Explicit Boundary

### 1. Principle
> **Ingress establishes Harmonia management. Internal processing preserves Harmonia management. Egress terminates Harmonia management of the emitted representation. Harmonia may retain evidence describing the information from which an external representation was derived, the transformation applied, the transmission performed, and the externally observable outcome. Harmonia shall not attribute its internal authority, governance, concurrency, security, availability, or operational guarantees to an emitted representation after that representation crosses an external egress boundary.**

### 2. Architectural Implications
- **Maxim**: *"Provenance may describe or cross the boundary; operational control does not."*
- Harmonia governs information while within its control; it cannot control an emitted copy once received by an external third-party system.
- Outbound acknowledgements or external version identifiers communicate boundary transaction outcomes, but do not extend Harmonia's internal transactions into external environments.
- Any subsequent resubmission of an emitted record represents a brand new ingress interaction.

### 3. Current Harmonia Realisation
- Enforced at `pylai` egress gateways (`pylai-mllp-out`, FHIR REST clients). Outbound dispatch terminates Harmonia's operational lifecycle for that transaction, while `kleio` records the transmission provenance.

---

## AX-14: Semantic Distinctions Are Preserved

### 1. Principle
> **Harmonia shall preserve meaningful distinctions between states, outcomes, and concepts throughout its internal processing and across subsystem boundaries. A distinction that is significant to information meaning, authority, security, concurrency, persistence, processing outcome, or operational correctness shall not be collapsed merely because an underlying technology, transport, API, or implementation abstraction does not represent that distinction directly. Where an external interface contract intentionally presents a simpler or different semantic model, Harmonia may project its internal semantics into that contract explicitly at the applicable boundary.**

### 2. Architectural Implications
- **Maxim**: *"Preserve semantics internally; project deliberately at boundaries."*
- Prevents loss of vital information due to premature simplification:
  - Access denial (`DENY`) must not be collapsed into resource absence (`NOT_FOUND`).
  - Persistence failure must not be reported as data absence.
  - Active coordination state must not be conflated with durable persistence versioning.
- External contracts may demand simpler error codes, but internal models must preserve full semantic fidelity.

### 3. Current Harmonia Realisation
- Distinct token structures in `calliope`: `ActiveStateToken` and `AuthoritativeVersion` represent separate concurrency domains and are never substituted for one another.

---

## AX-15: Uncertainty Is Preserved Until Resolved

### 1. Principle
> **Where Harmonia cannot establish the state, outcome, or effect of an operation with the certainty required by its governing contract, that uncertainty shall be represented explicitly. Harmonia shall not infer, manufacture, or assume a more certain outcome merely to simplify processing, recovery, or application behaviour. Uncertainty may be resolved through authoritative observation, reconciliation, or other governed evidence capable of establishing the required state.**

### 2. Architectural Implications
- **Maxim**: *"Do not turn 'unknown' into 'yes' or 'no'."*
- Distributed failures (network dropouts, unacknowledged socket closures) leave operations in an indeterminate state.
- The platform must explicitly represent the outcome as `INDETERMINATE` rather than making dangerous assumptions (e.g., assuming failure and re-executing non-idempotent operations, or assuming success and dropping monitoring).
- Recovery must proceed through explicit reconciliation against authoritative state.

### 3. Current Harmonia Realisation
- Explicit `INDETERMINATE` state representations in `energeia` task execution; reconciliation workflows query downstream destinations before triggering retries ([`REQ-FND-004`](../requirements-constraints/foundational-requirements.md)).

---

## AX-16: Operational Activity and Entity State Progress Together

### 1. Principle
> **Operational activity associated with a real-world entity progresses through explicit and observable state transitions coordinated with the governed state of that entity. Operational activity is not treated merely as a sequence of disconnected message transfers.**

### 2. Architectural Implications
- Health integration involves multi-stage workflows (e.g., ADT distribution, lab specimen processing).
- Operational tasks must be tracked in lockstep with the real-world state of the clinical entity.
- Prevents silent destination divergence during fan-out processing: every destination hop is an observable sub-state of the parent operational activity.

### 3. Current Harmonia Realisation
- Implemented in `energeia-erga` (e.g., `AdtDistributionErgon` tracking destination fan-out checkpoints).
- Multi-destination statuses (`FANOUT_DISPATCH_INITIATED`, `QUEUED`, `DELIVERED`) are recorded in Pragma envelopes and FHIR `Task.output` extensions.
