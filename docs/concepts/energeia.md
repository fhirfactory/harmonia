# Concept: Energeia `[IMPLEMENTED]`

Energeia is Harmonia's task processing, activity execution, and workflow orchestration subsystem, responsible for transforming inbound clinical events into completed clinical workflows.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Ἐνέργεια* (Energeia)
- **Etymology**: Coined by Aristotle from *ἐν-* (in) and *ἔργον* (work, deed). It translates literally as "being-at-work" or "actuality."
- **Philosophical Context**: In Aristotelian philosophy (notably the *Metaphysics* and *Nicomachean Ethics*), Aristotle created *energeia* to contrast with *dynamis* (potentiality). While *dynamis* represents the latent capacity to do something, *energeia* is the actual activity, movement, and realization of that capacity in the real world.
- **Architectural Rationale**: A clinical event arriving at an interface engine is merely potential (*dynamis*). It has the potential to update a patient record, notify a laboratory, or dispatch an imaging order. Energeia is the active engine that actualizes this potential through deterministic execution, transforming inert message bytes into clinical realities.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Energeia provides a structured 4-tier task execution hierarchy:

```
+---------------------------------------------------------------------------------------+
|                                  ENERGEIA SUBSYSTEM                                   |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                        PRAXIS (Workflow Blueprints)                            |  |
|   |   - TaskSequence blueprints defining multi-stage execution DAGs                |  |
|   |   - TaskSequenceLoader & TaskSequenceDefaultSeeder                             |  |
|   +--------------------------------------------------------------------------------+  |
|                                         | Instantiates                                |
|                                         v                                             |
|   +--------------------------------------------------------------------------------+  |
|   |                        PRAGMA (Task Execution State)                           |  |
|   |   - Canonical task state envelope (Pragma, PragmaCheckpoint)                   |  |
|   |   - Carries Security Context, Destination Sub-Statuses, Payload References     |  |
|   +--------------------------------------------------------------------------------+  |
|                                         | Executed by                                 |
|                                         v                                             |
|   +--------------------------------------------------------------------------------+  |
|   |                          PONOS (WorkEngine Workers)                            |  |
|   |   - Concurrency-controlled worker daemons consuming Petasos queues             |  |
|   |   - Manages execution timeouts, retry backoff, and DLQ escalation              |  |
|   +--------------------------------------------------------------------------------+  |
|                                         | Invokes                                     |
|                                         v                                             |
|   +--------------------------------------------------------------------------------+  |
|   |                           ERGA (Activity Execution)                            |  |
|   |   - Atomic, single-responsibility activity units (ErgonBase)                   |  |
|   |   - AdtDistributionErgon, Adt2FhirErgon, HL7 and FHIR transformers             |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

1. **Praxis (Workflow Blueprints)**: Declarative definitions of clinical integration workflows. Blueprints specify the ordered sequence of activities required to complete a business process.
2. **Pragma (Execution Envelopes)**: The runtime state machine capturing task execution progress, security assertions, and destination fan-out checkpoints.
3. **Ponos (WorkEngine Workers)**: High-concurrency worker threads that consume tasks from inbound queues, evaluate Themis authorization, and orchestrate execution.
4. **Erga (Activity Units)**: The discrete operational building blocks that perform concrete business logic (transforming payloads, querying patient records, routing to egress).

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Energeia Owns
- Task sequence blueprint definition and dynamic seeder (`praxis`).
- Runtime task execution engine and worker daemon management (`ponos`).
- Discrete activity execution library (`erga`).
- Pragma task state envelope structures and checkpoint transition logic.
- Operational worker inspection CLI (`ponos-cli`).
- Retry semantics, execution timeout enforcement, and checkpoint commits to Mneme/Mnemosyne.

### What Energeia Explicitly Does NOT Own (Anti-Responsibilities)
- Direct network socket termination or MLLP framing (owned by Pylai).
- JMS message broker infrastructure or clustering (owned by Petasos).
- Long-term relational database schemas (owned by Mnemosyne).
- Security policy definitions or RBAC/ABAC authority verification (delegated to Themis).

---

## 4. Key Submodules & Components `[IMPLEMENTED]`

| Component | Module Name | Technology | Key Classes | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Erga Activities** | `erga` | Java 21, Camel | `ErgonBase`, `AdtDistributionErgon`, `Adt2FhirErgon` | `[IMPLEMENTED]` |
| **Praxis Blueprints** | `praxis` | Java 21, Jackson | `TaskSequenceLoader`, `TaskSequenceDefaultSeeder`, `PraxisModel`| `[IMPLEMENTED]` |
| **Ponos WorkEngine** | `ponos` | Spring Boot 3.2.5 / WildFly | `PonosWorkEngine`, `PonosWorkerService`, `TaskEventConsumer` | `[IMPLEMENTED]` |
| **Ponos Admin CLI** | `ponos-cli` | Java 21 CLI (Picocli) | `PonosCommand`, `QueueInspectionTool` | `[IMPLEMENTED]` |

---

## 5. Architectural Guarantees `[IMPLEMENTED]`

1. **Fan-Out State Tracking (REC-002)**: Multi-destination activities (such as `AdtDistributionErgon`) create distinct sub-checkpoints for each destination endpoint, preventing partial delivery failures from silently succeeding.
2. **Deterministic Checkpointing**: Every task state transition emits an immutable `PragmaCheckpoint` to Mneme, enabling zero-loss recovery if a worker daemon restarts mid-execution.
