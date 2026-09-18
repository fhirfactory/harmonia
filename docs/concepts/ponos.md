# Concept: Ponos `[IMPLEMENTED]`

Ponos is the high-concurrency background task execution worker daemon of the Energeia subsystem, responsible for consuming tasks from Petasos queues, evaluating Themis authorization, managing execution thread pools, and driving activity lifecycles.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Πόνος* (Ponos)
- **Etymology**: Ancient Greek masculine noun meaning hard work, strenuous toil, labor, or physical strain.
- **Mythological Context**: In Hesiod's *Theogony*, Ponos is personified as the spirit of heavy physical toil and ceaseless labor, an offspring of Eris (strife). Unlike leisurely intellectual contemplation, Ponos represents the unglamorous, unrelenting muscular effort required to plow fields, haul stones, and build cities.
- **Architectural Rationale**: In an enterprise integration engine, someone must do the heavy lifting: consuming endless streams of events from message brokers, spawning threads, handling timeouts, committing checkpoints, and surviving broker crashes. Ponos is Harmonia's tireless worker, executing the arduous operational labor of message processing 24/7/365 without complaint.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Ponos operates as an autonomous worker service (packaged as a Spring Boot microservice or WildFly deployment) attached to the Petasos messaging backbone:

```
+---------------------------------------------------------------------------------------+
|                                    PONOS WORKENGINE                                   |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                       QUEUE CONSUMER LOOP & DISPATCHER                         |  |
|   |   - Listens on `petasos.queue.task.inbound` via PetasosConsumer                |  |
|   |   - Configurable consumer thread concurrency (default: 10 threads)             |  |
|   +--------------------------------------------------------------------------------+  |
|                                         | Receives Task
|                                         v
|   +--------------------------------------------------------------------------------+  |
|   |                       THEMIS SECURITY EVALUATION GATE                          |  |
|   |   - Evaluates caller principal & authorities via ThemisPolicy                  |  |
|   |   - Requires `PRV_PROCESS` / `PRV_TRANSFORM` authorities                       |  |
|   +--------------------------------------------------------------------------------+  |
|                                         | Authorized
|                                         v
|   +--------------------------------------------------------------------------------+  |
|   |                       EXECUTION POOL & TIMEOUT MONITOR                         |  |
|   |   - Spawns worker thread for target Praxis workflow blueprint                  |  |
|   |   - Enforces execution timeout (default: 300 seconds)                          |  |
|   |   - Sequentially invokes Erga activity units                                   |  |
|   |   - Commits intermediate Pragma checkpoints to Mneme cache                     |  |
|   +--------------------------------------------------------------------------------+  |
|                                         | On Failure / DLQ
|                                         v
|   +--------------------------------------------------------------------------------+  |
|   |                       RETRY & DEAD-LETTER ESCALATION                           |  |
|   |   - Increments redelivery attempt counter                                      |  |
|   |   - Routes poison pills to `petasos.queue.task.dlq` after max retries (5)       |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Ponos Owns
- The queue consumer worker loop on `petasos.queue.task.inbound`.
- Worker thread pool sizing, thread lifecycle management, and concurrency limits (`PONOS_CONCURRENCY`).
- Execution timeouts and thread interruption on hung activity processors (`PONOS_TASK_TIMEOUT_SEC`).
- Security gate invocation delegating to Themis prior to task execution.
- Checkpoint commit coordination with the Mneme distributed cache grid.
- Administrative inspection, pause/resume, and queue flush operations via `ponos-cli`.

### What Ponos Explicitly Does NOT Own (Anti-Responsibilities)
- Workflow sequence blueprint definitions (owned by Praxis).
- Specific clinical payload translation or business logic (owned by Erga).
- Low-level JMS connection pooling or broker journal persistence (owned by Petasos).
- External network listeners or socket termination (owned by Pylai).
- Direct collaboration or Matrix room management (Agora coordinates via Petasos, not directly with Ponos).

---

## 4. Key Classes & Configuration `[IMPLEMENTED]`

| Class / Component | Module Name | Role | Status |
| :--- | :--- | :--- | :--- |
| `PonosApplication` | `energeia-ponos` | Spring Boot main application entry point | `[IMPLEMENTED]` |
| `TaskEventConsumerService` | `energeia-ponos` | Message consumer listening on inbound task queue | `[IMPLEMENTED]` |
| `PonosWorkEngine` | `energeia-ponos` | Coordinates thread pools, timeouts, and execution | `[IMPLEMENTED]` |
| `PonosCommand` | `energeia-ponos-cli` | Administrative CLI tool for queue and worker control | `[IMPLEMENTED]` |

### Configuration Parameters `[CONFIGURED]`
- `PONOS_CONCURRENCY`: `10` (Default number of parallel worker threads)
- `PONOS_TASK_TIMEOUT_SEC`: `300` (Task execution timeout in seconds)
- `PETASOS_INBOUND_QUEUE`: `petasos.queue.task.inbound`
- `PETASOS_DLQ_QUEUE`: `petasos.queue.task.dlq`
