# Concept: Hestia `[IMPLEMENTED]`

Hestia is Harmonia's foundational data and persistence subsystem, governing both high-speed ephemeral in-memory caching (**Mneme**) and durable, lifelong relational persistence (**Mnemosyne**).

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Ἑστία* (Hestia)
- **Etymology**: Derived from the Indo-European root *\*wes-* (to dwell, to abide, or to live). In ancient Greek culture, the *hestia* was the central hearth of the home, temple, or city.
- **Mythological Context**: Hestia is the virgin goddess of the hearth, home, architecture, domestic life, and state order. She received the first and last offerings at every sacrificial banquet. Her flame in the prytaneion (civic hall) was never allowed to die, symbolizing continuity, life, and foundational security.
- **Architectural Rationale**: Just as the hearth is the secure, enduring foundation around which the household revolves, Hestia is the foundation upon which all Harmonia state rests. Clinical workflows require both rapid, warm, immediate access to in-flight data and an unquenchable, durable sanctuary for historical patient records.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Hestia defines a **Dual-State Persistence Architecture** that separates in-flight coordination from historical record-keeping:

```
+---------------------------------------------------------------------------------------+
|                                    HESTIA SUBSYSTEM                                   |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +---------------------------------------+   +------------------------------------+  |
|   |          MNEME (Ephemeral)            |   |       MNEMOSYNE (Durable)          |  |
|   |                                       |   |                                    |  |
|   | * Clustered Infinispan 15.0.3 DataGrid|   | * Mnemosyne Clinical               |  |
|   | * Sub-millisecond L1/L2 Cache         |   |   - HAPI FHIR R5 JPA Engine        |  |
|   | * Hot Rod Binary Protocol (:11222)    |   |   - PostgreSQL 16 (fhir_node_*)    |  |
|   | * In-flight Tasks & TaskSequences     |   |                                    |  |
|   | * Write-Behind Store SPI              |   | * Mnemosyne Operations             |  |
|   |                                       |   |   - Spring Data JPA Repository     |  |
|   |                                       |   |   - PostgreSQL 16 (ops_node_*)     |  |
|   +---------------------------------------+   +------------------------------------+  |
|                        |                                         ^                    |
|                        +======== Async Write-Behind =============+                    |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

1. **Mneme (Ephemeral In-Memory Grid)**: Provides sub-millisecond read/write access for active integration workflows, task checkpoints, and sliding-window deduplication caches.
2. **Mnemosyne (Durable Relational Persistence)**: Provides ACID-compliant, lifelong storage for clinical FHIR R5 resources and operational audit trails.
3. **Write-Behind Asynchrony**: In-memory updates in Mneme can be asynchronously offloaded to Mnemosyne via the custom Infinispan `NonBlockingStore` SPI, shielding low-latency ingress pipelines from relational database write spikes.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Hestia Owns
- The clustered Infinispan 15.0.3 data grid topology and Hot Rod server configuration (`mneme-cluster`).
- Custom non-blocking cache store SPI implementations bridging Mneme and Mnemosyne (`mneme-persistence`).
- HAPI FHIR R5 JPA persistence server implementation, REST endpoints (`/fhir/r5/*`), and database schemas (`mnemosyne-clinical`).
- Operational metadata persistence server (`/api/operations/*`), TaskSequence status schemas, and audit stores (`mnemosyne-operations`).
- Relational schema tables: `hie_fhir_resources`, `hie_operations_resources`, `hie_task_sequences`.
- Operations CLI for database inspection and cache grid administration (`hie-operations-cli`).

### What Hestia Explicitly Does NOT Own (Anti-Responsibilities)
- Direct network socket listening or MLLP framing (owned by Pylai).
- ActiveMQ Artemis connection factory pooling or JMS session management (owned by Petasos).
- Task consumer worker loops or workflow activity execution (owned by Energeia).
- Web browser rendering or client-side UI routing (owned by Iris).

---

## 4. Key Components & Interfaces `[IMPLEMENTED]`

| Component | Module Name | Implementation Technology | Primary Interfaces / Endpoints | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Mneme Data Grid** | `mneme-cluster` | Infinispan 15.0.3.Final | Hot Rod binary RPC (`:11222`), JGroups (`:7800`) | `[IMPLEMENTED]` |
| **Cache Store SPI** | `mneme-persistence` | Infinispan NonBlockingStore SPI | `FhirRestCacheStore`, `OperationsCacheStore` | `[IMPLEMENTED]` |
| **Clinical Persistence** | `mnemosyne-clinical` | Spring Boot 3.2.5, HAPI FHIR JPA | HTTP REST (`/fhir/r5/*`), `IFhirResourceDao` | `[IMPLEMENTED]` |
| **Operations Persistence**| `mnemosyne-operations`| Spring Boot 3.2.5, Spring Data JPA | HTTP REST (`/api/operations/*`), `TaskSequenceRepository`| `[IMPLEMENTED]` |
| **Operations CLI** | `hie-operations-cli` | Java 21 CLI (Picocli) | Administrative CLI commands | `[IMPLEMENTED]` |

---

## 5. Architectural Invariants `[IMPLEMENTED]`

1. **Storage Separation**: Clinical data (`fhir_node_*`) and operational metadata (`ops_node_*`) must reside in isolated PostgreSQL database instances or schemas to prevent operational queries from competing with patient-care workloads.
2. **Hot Rod Isolation**: Higher-level tiers (such as Iris BEFE) query Mneme via Hot Rod client connections and must never maintain direct JDBC pools to Mnemosyne.
