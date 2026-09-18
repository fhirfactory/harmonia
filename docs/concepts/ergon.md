# Concept: Ergon `[IMPLEMENTED]`

An Ergon (plural: *Erga*) is Harmonia's atomic, single-responsibility activity execution unit within the Energeia subsystem, responsible for executing discrete clinical transformations, validations, or routing decisions.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Ἔργον* (Ergon, plural: *ἔργα* - Erga)
- **Etymology**: Ancient Greek neuter noun meaning work, deed, action, physical creation, or the completed product of craftsmanship.
- **Classical Context**: In classical literature (such as Hesiod's *Works and Days* — *Ἔργα καὶ Ἡμέραι*), an *ergon* is a discrete, purposeful task: plowing a furrow, forging a blade, or harvesting an olive grove. It is an individual unit of productive effort that contributes to the broader welfare of the community.
- **Architectural Rationale**: Complex clinical workflows must not be monolithic blobs of spaghetti code. By breaking workflows down into modular, reusable, single-responsibility *erga*, Harmonia ensures that each activity can be independently tested, profiled, secured, and reasoned about without side effects.

---

## 2. Architectural Definition `[IMPLEMENTED]`

An Ergon represents a pure functional activity step within a workflow pipeline. It extends the abstract contract `ErgonBase`:

```
+---------------------------------------------------------------------------------------+
|                                    ERGON STRUCTURE                                    |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|                               +-----------------------+                               |
|                               |   Pragma Input State  |                               |
|                               +-----------------------+                               |
|                                           |                                           |
|                                           v                                           |
|                   +-----------------------------------------------+                   |
|                   |           ErgonBase Execution Cycle           |                   |
|                   |                                               |                   |
|                   |  1. Validate Input Pragma & Security Context  |                   |
|                   |  2. Execute Single Clinical Transformation    |                   |
|                   |  3. Record Sub-Status / Fan-Out Checkpoint    |                   |
|                   |  4. Yield Updated Pragma State                |                   |
|                   +-----------------------------------------------+                   |
|                                           |                                           |
|                                           v                                           |
|                               +-----------------------+                               |
|                               |  Pragma Output State  |                               |
|                               +-----------------------+                               |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

### Core Characteristics of an Ergon:
1. **Idempotence**: Re-executing an Ergon with the same `Pragma` input produces identical outputs without duplicate clinical side effects.
2. **Deterministic Checkpointing**: Prior to exiting, the Ergon records an updated `PragmaCheckpoint` detailing the step outcome, execution duration, and destination statuses.
3. **Pure Composition**: Erga do not call other Erga directly; their sequence is orchestrated strictly by **Praxis** blueprints.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What an Ergon Owns
- Discrete transformation logic between data models (e.g., HL7 v2 ADT to FHIR R5 `Patient`/`Encounter`).
- Field-level validation rules and mandatory attribute checks.
- Generating granular destination delivery sub-statuses during fan-out dispatch (REC-002).
- Updating `Pragma` payload references and appending activity-specific audit metrics.

### What an Ergon Explicitly Does NOT Own (Anti-Responsibilities)
- Multi-step workflow sequencing or branch selection (owned by Praxis).
- Consumer thread pools, timeouts, and queue listening (owned by Ponos).
- Direct external socket connections or MLLP wire framing (owned by Pylai).
- Direct raw SQL database pool management (owned by Mnemosyne).

---

## 4. Notable Erga in the Codebase `[IMPLEMENTED]`

| Ergon Class | Target Subsystem | Primary Responsibility | Status |
| :--- | :--- | :--- | :--- |
| **`AdtDistributionErgon`** | `energeia-erga` | Evaluates admission/transfer triggers, resolves target hospital egress endpoints, creates fan-out checkpoints, and routes payloads to outbound queues. | `[IMPLEMENTED]` |
| **`Adt2FhirErgon`** | `energeia-erga` | Parses HL7 v2 ADT segments (MSH, PID, PV1) and maps them into canonical FHIR R5 `Patient` and `Encounter` resources. | `[IMPLEMENTED]` |
| **`Orm2FhirErgon`** | `energeia-erga` | Transforms pharmacy and laboratory order messages (ORM) into FHIR R5 `ServiceRequest` resources. | `[IMPLEMENTED]` |
| **`Oru2FhirErgon`** | `energeia-erga` | Transforms laboratory observation results (ORU^R01) into FHIR R5 `DiagnosticReport` and `Observation` structures. | `[IMPLEMENTED]` |

---

## 5. Architectural Invariant: Granular Fan-Out Tracking (REC-002) `[IMPLEMENTED]`

When an Ergon performs fan-out distribution (such as `AdtDistributionErgon` distributing an admission to three downstream destinations: HIS, LIS, and PACS):
- It must **not** aggregate the outcome into a single boolean status.
- It must emit an explicit `PragmaCheckpoint` containing destination-specific sub-statuses (`destinationId`, `status=QUEUED`, `timestamp`).
- As outbound gateways (`pylai-mllp-out`) receive delivery confirmations, they update these individual sub-statuses in `Task.output` extensions.
