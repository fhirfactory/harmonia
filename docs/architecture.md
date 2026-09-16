# Petasos Architecture

Petasos is the reliable, high-availability asynchronous messaging and event distribution framework for the **Harmonia** Health Integration Environment (HIE).

Its primary mandate is to decouple Harmonia microservices, gateways (Pylai), and workflow processors (Ponos/Erga) through resilient, durable message transport powered by **Apache ActiveMQ Artemis**, while completely encapsulating and hiding Artemis-specific connection APIs and JMS boilerplate behind clean, healthcare-ready domain abstractions.

---

### Layered Architecture & Component Model

The Petasos subsystem sits between upstream Harmonia modules and the underlying clustered messaging infrastructure.

```mermaid
graph TD
    HM[Harmonia Module<br/>e.g. Pylai Gateway / Ponos WorkEngine] --> PAPI[Petasos API<br/>Petasos, PetasosProducer, PetasosConsumer, PetasosMessage]
    PAPI --> PCOR[Petasos Core<br/>Envelope Serializers, Dedup, Metrics Collector]
    PCOR --> PART[Petasos Artemis Adapter<br/>ArtemisConnectionManager, MessageConverter, SessionPool]
    PART --> CLUS[ActiveMQ Artemis HA Cluster]

    subgraph "Artemis Clustered Topology"
        direction LR
        subgraph "HA Pair A"
            PA[Primary A<br/>Live Active - Port 61616] <-->|Journal Replication| BA[Backup A<br/>Passive Replica - Port 61617]
        end

        subgraph "HA Pair B"
            PB[Primary B<br/>Live Active - Port 61618] <-->|Journal Replication| BB[Backup B<br/>Passive Replica - Port 61619]
        end

        PA <=====>|Server-side ON_DEMAND Clustering<br/>Redistribution Delay = 0| PB
    end
```

---

### Key Architectural Concepts

#### 1. Clustering vs. High Availability (HA) Replication

Petasos cleanly distinguishes between **Server-Side Clustering** and **HA Replication**:

* **Clustering (`petasos-cluster`)**:
  * Unites multiple **concurrently active** Artemis brokers (`Primary A` and `Primary B`) into a unified messaging mesh.
  * Dynamically load-balances messages across active nodes based on consumer demand (`ON_DEMAND`).
  * If consumers disconnect from one node, queued messages are automatically redistributed across cluster connections to available consumers on other active nodes (`redistribution-delay = 0`).
  * Enables horizontal scalability as new active/backup pairs are introduced.

* **HA Replication (`group-a`, `group-b`)**:
  * Protects an individual broker against node, VM, or process failure.
  * An active Primary node continuously replicates its persistent journal stream (NIO file journal) over the network to a paired passive Backup replica.
  * When a Primary fails, its paired Backup detects the failure and immediately activates as the new Live broker without data loss.

#### 2. Isolated Storage Ownership

* Multiple active Artemis brokers **never share journal directories or storage filesystems**. Shared-storage architectures create single points of failure and storage lock contention.
* Each broker instance (Primary A, Backup A, Primary B, Backup B) maintains its own independent storage tree:
  * `./data/journal` (Artemis high-performance NIO file journal)
  * `./data/bindings` (Queue and routing configurations)
  * `./data/paging` (Paging files when memory thresholds are exceeded)
  * `./data/large-messages` (Large streaming payloads)
  * Unique Broker Identity and Node ID.

#### 3. Broker Durability vs. Harmonia Application Persistence (Mnemosyne)

* **Artemis Journal Durability**:
  * Exclusively responsible for transient in-flight message transport durability until acknowledged by consuming applications.
  * It is **not** a clinical database or application persistence store.
* **Mnemosyne / Mneme Persistence**:
  * Harmonia’s permanent clinical records (FHIR R5 resources, Provenance, AuditEvents, Task histories) are managed and persisted by **Mnemosyne** JPA database servers (PostgreSQL) and **Mneme** distributed caches (Infinispan).

---

### Standard Message Envelope

Petasos provides an immutable, strongly-typed envelope (`PetasosMessage`) designed for healthcare interoperability:

| Field | Type | Description |
| :--- | :--- | :--- |
| `messageId` | `String` | Universally unique message identifier (UUID). |
| `correlationId` | `String` | Tracks the conversational thread or end-to-end integration transaction. |
| `causationId` | `String` | Tracks the immediate predecessor message or event triggering this action. |
| `messageType` | `String` | Logical event type (e.g., `PatientAdmitEvent`, `ObservationDispatch`). |
| `source` | `String` | Logical identifier of the originating gateway or module. |
| `destination` | `PetasosDestination` | Target queue or topic routing address. |
| `timestamp` | `Instant` | Message creation timestamp. |
| `contentType` | `String` | MIME type (e.g., `application/json`, `application/fhir+json`). |
| `schemaIdentifier` | `String` | Model schema URI (e.g., `http://hl7.org/fhir/StructureDefinition/Patient`). |
| `schemaVersion` | `String` | Schema version (e.g., `5.0.0`, `R5`). |
| `payload` | `byte[]` | Opaque binary or text payload. Completely uninspected by Petasos transport. |
| `metadata` | `Map<String, Object>` | Custom transport headers, tracing tokens, and tenancy metadata. |
| `durable` | `boolean` | Indicates whether persistent file journal storage is required before acknowledgment. |
| `duplicateDetectionId`| `String` | Stable identifier used by Artemis and Petasos for idempotent deduplication. |

---

### Delivery Guarantees & Idempotency

* **At-Least-Once Delivery**: Petasos guarantees that acknowledged durable messages are never lost across broker restarts, network interruptions, or failovers. In exchange, consuming applications must be idempotent.
* **Deduplication Mechanisms**:
  1. **Broker-Side Deduplication**: Artemis evaluates the `_AMQ_DUPL_ID` header (mapped from `PetasosMessage.duplicateDetectionId`) against its sliding ID cache (`id-cache-size = 20000`). Duplicate transmissions within the cache window are dropped automatically.
  2. **Client-Side Deduplication**: `DuplicateDetector` maintains an in-memory sliding window cache on consumers to suppress duplicates locally.
  3. **Application Idempotency**: Healthcare workflows use `messageId` and FHIR identifiers in Mneme/Mnemosyne to achieve business-level idempotency.

---

### Observability & Privacy Protection

* **Strict Clinical Privacy**: Petasos explicitly avoids logging payload contents at any log level (`INFO`, `WARN`, `ERROR`, `DEBUG`) to prevent sensitive Protected Health Information (PHI) exposure in server logs.
* **Standard Metrics (`PetasosMetrics`)**:
  * Messages sent and received totals.
  * Message processing and transmission failures.
  * Broker failover and reconnection counts.
  * Message redelivery counts.
  * Dead Letter Queue (DLQ) arrival counts.
  * Active producer and consumer counts.

---

### Persistence, Lifecycle & Recovery Documentation Index

For comprehensive deep dives into Harmonia's persistence model, database schemas, message lifecycles, and failure recovery specifications, refer to:

1. **[Persistence Architecture](persistence-architecture.md)**: 4-tier storage model (Artemis journal, Ponos cache grid, Mnemosyne PostgreSQL database, Paradeigma exemplar state), persistent entity catalogue, and ownership boundaries.
2. **[Database Schema Specification](database-schema.md)**: Detailed relational table definitions (`hie_fhir_resources`, `hie_operations_resources`), JPA mappings, unique constraints, and PostgreSQL physical DDL.
3. **[Message Lifecycle & Transaction Boundaries](message-lifecycle.md)**: End-to-end clinical message flows (PAS ADT fan-out, EMR ORM routing, LMS/RIS ORU ingestion), Mermaid sequence diagrams, and dual-write failure window analyses.
4. **[Failure-Recovery Architecture](failure-recovery.md)**: Comprehensive failure matrix, restart recovery procedures, lease/ownership recovery, 4-tier idempotency model, ACK semantics (AA/AE/AR), retry policies, and DLQ handling.
5. **[Recovery Guarantees & Objectives](recovery-guarantees.md)**: Formal delivery guarantees (at-least-once, effectively-once) and Recovery Point Objectives (RPO) / Recovery Time Objectives (RTO) across all platform boundaries.
6. **[Persistence & Recovery Gap Analysis](persistence-recovery-gaps.md)**: Catalog of identified implementation deviations (REC-001 through REC-004) with risk ratings and remediation roadmaps.
