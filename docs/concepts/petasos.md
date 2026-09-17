# Concept: Petasos `[IMPLEMENTED]`

Petasos is Harmonia's high-availability messaging and event transport framework, providing guaranteed, fault-tolerant message delivery while isolating upper application tiers from underlying broker implementations.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Πέτασος* (Petasos)
- **Etymology**: Ancient Greek noun referring to the broad-brimmed sun hat typically made of felt or straw worn by travelers, couriers, and merchants.
- **Mythological Context**: In Greek mythology, the petasos was the iconic winged hat worn by Hermes, the herald of the Olympian gods and patron of travelers, heralds, and messengers. With his winged sandals and petasos, Hermes moved effortlessly between the divine realm, the mortal earth, and the underworld, ensuring that messages were carried safely, swiftly, and without interference across boundaries.
- **Architectural Rationale**: Just as Hermes' petasos guaranteed safe passage across treacherous realms, Petasos guarantees that clinical events are transported reliably across decoupled subsystems without data loss, corruption, or broker lock-in.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Petasos separates abstract message passing from physical broker connectivity across four modules:

```
+---------------------------------------------------------------------------------------+
|                                   PETASOS SUBSYSTEM                                   |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                                  petasos-api                                   |  |
|   |                                                                                |  |
|   | * Pure Transport Interfaces: Petasos, PetasosProducer, PetasosConsumer        |  |
|   | * Opaque Message Envelope: PetasosMessage (payload as byte[] or String)       |  |
|   | * Mandatory Invariant: ZERO JMS or Apache ActiveMQ imports/dependencies       |  |
|   +--------------------------------------------------------------------------------+  |
|                                         ^                                             |
|                                         | Implements                                  |
|   +-------------------------------------+------------------------------------------+  |
|   |                                                                                |  |
|   |   +---------------------------------------+   +-----------------------------+  |  |
|   |   |             petasos-core              |   |       petasos-artemis       |  |  |
|   |   |                                       |   |                             |  |  |
|   |   | * Envelope serialization & metadata   |   | * Artemis 2.33.0 Adapter    |  |  |
|   |   | * Sliding Window Deduplication        |   | * JMS 3.1 Connection Pools  |  |  |
|   |   |   (DuplicateDetector)                 |   | * Transparent HA Failover   |  |  |
|   |   | * Transport metrics & telemetry       |   |   ((tcp://...)?ha=true)     |  |  |
|   |   +---------------------------------------+   +-----------------------------+  |  |
|   |                                                                                |  |
|   +--------------------------------------------------------------------------------+  |
|                                         | Connects                                    |
|                                         v                                             |
|   +--------------------------------------------------------------------------------+  |
|   |                Apache ActiveMQ Artemis 2.33.0 Clustered Brokers                |  |
|   |                   (Symmetric HA Cluster with File Journals)                    |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

1. **Pure API Abstraction (`petasos-api`)**: Defines pure transport contracts without any tie to JMS or Artemis, allowing applications to produce and consume messages without leaking middleware types.
2. **Core Framing & Deduplication (`petasos-core`)**: Provides envelope lifecycle management, header propagation, and LRU-based sliding-window message deduplication (`DuplicateDetector`) to protect against duplicate processing during network hiccups.
3. **Artemis Broker Adapter (`petasos-artemis`)**: Production adapter providing connection pooling, automatic reconnect failover, and queue/topic management on Apache ActiveMQ Artemis 2.33.0.
4. **Resilience Testing Harness (`petasos-test`)**: Embedded test cluster simulating split-brain, network partition, and broker failover scenarios.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Petasos Owns
- Pure transport interfaces (`Petasos`, `PetasosProducer`, `PetasosConsumer`, `PetasosDestination`).
- Transport envelope models (`PetasosMessage`) and wire serialization.
- Transport-level message deduplication via sliding window memory buffers (`DuplicateDetector`).
- ActiveMQ Artemis cluster configuration, connection pools, and failover parameters.
- Dead-Letter Queue (DLQ) routing policies and redelivery limit enforcement.
- Broker metrics collection (`PetasosMetrics`).

### What Petasos Explicitly Does NOT Own (Anti-Responsibilities)
- Clinical payload parsing or validation (payloads are treated as opaque binary `byte[]` or strings).
- Relational clinical persistence (owned by Mnemosyne).
- Task sequence state transitions or workflow rules (owned by Energeia).
- External network protocol listeners like MLLP (owned by Pylai).

---

## 4. Architectural Invariants `[IMPLEMENTED]`

### Invariant 2: Petasos API Isolation `[IMPLEMENTED]`
- `petasos-api` must contain **strictly zero** imports or dependencies on `org.apache.activemq..`, `jakarta.jms..`, or `javax.jms..`.
- Higher-layer modules (such as Calliope, Themis, Energeia, and Pylai) interact solely with `petasos-api` interfaces, remaining completely decoupled from broker vendor libraries.
- Continuously verified by `PetasosApiIsolationArchitectureTest`.

---

## 5. Key Classes & Configuration `[IMPLEMENTED]`

| Component | Module Name | Technology | Key Classes | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Messaging Contracts** | `petasos-api` | Pure Java 21 | `Petasos`, `PetasosProducer`, `PetasosConsumer`, `PetasosMessage` | `[IMPLEMENTED]` |
| **Deduplication & Envelope**| `petasos-core`| Java 21, Slf4j | `DuplicateDetector`, `PetasosMessageBuilder`, `PetasosMetrics` | `[IMPLEMENTED]` |
| **Artemis HA Adapter** | `petasos-artemis` | Artemis 2.33.0, JMS 3.1 | `PetasosArtemisConnectionFactory`, `PetasosArtemisSessionPool` | `[IMPLEMENTED]` |
| **Embedded Test Harness** | `petasos-test` | JUnit 5, Artemis Embedded | `EmbeddedArtemisClusterTestHarness`, `BrokerFailoverTest` | `[IMPLEMENTED]` |

### Core Configuration Keys `[CONFIGURED]`
- `PETASOS_BROKER_URL`: `(tcp://artemis-1:61616,tcp://artemis-2:61616)?ha=true&reconnectAttempts=-1`
- `PETASOS_SESSION_POOL_SIZE`: `20`
- `PETASOS_DEDUP_CACHE_SIZE`: `20000`
- `PETASOS_MAX_REDELIVERY`: `5`
