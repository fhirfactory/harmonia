# Petasos Subproject Reference: High-Availability Messaging `[IMPLEMENTED]`

Petasos is Harmonia's resilient messaging abstraction and transport framework, isolating Apache ActiveMQ Artemis 2.33.0 broker clients behind pure transport interfaces.

---

## 1. Subproject Architecture & Leaf Modules `[IMPLEMENTED]`

```
petasos/
├── petasos-api/       # Pure Java messaging contracts (Petasos, Producer, Consumer, Message)
├── petasos-core/      # Deduplication, serialization, metrics & property resolution
├── petasos-artemis/   # ActiveMQ Artemis adapter, connection pool & JMS session manager
└── petasos-test/      # Embedded Artemis cluster test harness & failover suites
```

### Subproject Maven Coordinates `[CONFIGURED]`
- **Parent GroupId**: `net.fhirfactory.harmonia`
- **ArtifactId**: `petasos`
- **Version**: `1.0.0-SNAPSHOT`
- **Packaging**: `pom`

---

## 2. Leaf Module Deep-Dives `[IMPLEMENTED]`

### 2.1 `petasos-api` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:petasos-api:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.petasos.api`
  - `net.fhirfactory.harmonia.petasos.api.config`
  - `net.fhirfactory.harmonia.petasos.api.consumer`
  - `net.fhirfactory.harmonia.petasos.api.destination`
  - `net.fhirfactory.harmonia.petasos.api.exception`
  - `net.fhirfactory.harmonia.petasos.api.health`
  - `net.fhirfactory.harmonia.petasos.api.message`
  - `net.fhirfactory.harmonia.petasos.api.metrics`
  - `net.fhirfactory.harmonia.petasos.api.producer`
  - `net.fhirfactory.harmonia.petasos.api.topology`
- **Key Classes & Interfaces**:
  - `Petasos`: Root messaging client factory interface.
  - `PetasosProducer`: High-throughput message publisher supporting synchronous and asynchronous dispatch.
  - `PetasosConsumer` / `PetasosSubscription`: Queue consumer and subscription handle.
  - `PetasosMessage` / `PetasosMessageBuilder`: Opaque transport envelope holding payload (`byte[]` or `String`), correlation headers, and destination metadata.
  - `PetasosDestination`: Represents a target physical queue or topic.
  - `PetasosHealth` / `ConnectionState`: Health probe contracts reporting broker connection status (`CONNECTED`, `CONNECTING`, `DISCONNECTED`).
  - `PetasosBrokerTopology`: Exposes discovered cluster nodes and load-balancing topologies.
- **Mandatory Invariant (Invariant 2)**: Strictly zero imports or dependencies on `org.apache.activemq..`, `jakarta.jms..`, or `javax.jms..`.

### 2.2 `petasos-core` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:petasos-core:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.petasos.core.config`
  - `net.fhirfactory.harmonia.petasos.core.dedup`
  - `net.fhirfactory.harmonia.petasos.core.metrics`
  - `net.fhirfactory.harmonia.petasos.core.serialization`
- **Key Classes & Interfaces**:
  - `DuplicateDetector`: In-memory LRU sliding-window filter that tracks message IDs and content hashes to prevent duplicate processing.
  - `PetasosMessageSerializer`: Handles high-performance binary and JSON serialization of message envelopes.
  - `PetasosMetricsCollector`: Aggregates publish latencies, queue delivery rates, and error counters.
  - `PetasosPropertyResolver`: Resolves broker configuration properties from environment variables and system properties.
- **Runtime Dependencies**: `petasos-api`, `calliope`, Jackson Databind, Slf4j.

### 2.3 `petasos-artemis` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:petasos-artemis:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.petasos.artemis`
  - `net.fhirfactory.harmonia.petasos.artemis.connection`
  - `net.fhirfactory.harmonia.petasos.artemis.consumer`
  - `net.fhirfactory.harmonia.petasos.artemis.converter`
  - `net.fhirfactory.harmonia.petasos.artemis.producer`
- **Key Classes & Interfaces**:
  - `ArtemisPetasos`: Implementation of `Petasos` factory backed by ActiveMQ Artemis.
  - `ArtemisConnectionManager`: Manages Artemis JMS `ConnectionFactory` pools, client reconnection handlers, and failover listeners.
  - `ArtemisPetasosProducer`: JMS message producer mapping `PetasosMessage` into Artemis `BytesMessage` or `TextMessage`.
  - `ArtemisPetasosConsumer`: JMS message listener pool consuming from target Artemis queues.
  - `ArtemisMessageConverter`: Bi-directional converter between `PetasosMessage` and Jakarta JMS `Message`.
- **Runtime Dependencies**: `petasos-core`, Apache ActiveMQ Artemis JMS Client (`2.33.0`), Jakarta JMS API 3.1.

### 2.4 `petasos-test` `[IMPLEMENTED]`
- **Maven Coordinates**: `net.fhirfactory.harmonia:petasos-test:1.0.0-SNAPSHOT` (jar)
- **Primary Packages**:
  - `net.fhirfactory.harmonia.petasos.test.harness`
- **Key Classes**:
  - `EmbeddedArtemisCluster`: Programmatic test harness spawning embedded Artemis broker pairs with simulated network delays and failovers.
- **Runtime Dependencies**: `petasos-artemis`, Artemis Server 2.33.0, JUnit Jupiter 5.10.2.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Petasos Owns
- Transport abstraction interfaces decoupled from messaging middleware.
- Message envelope lifecycle (`PetasosMessage`), payload containment, and header metadata.
- Transport-level sliding window deduplication (`DuplicateDetector`).
- ActiveMQ Artemis cluster configuration, connection failover management, and session pooling.
- Queue-level metrics and telemetry collection (`PetasosMetrics`).

### What Petasos Explicitly Does NOT Own (Anti-Responsibilities)
- Parsing or validating HL7 or FHIR clinical payloads (payloads are treated as opaque `byte[]`).
- Long-term clinical database persistence (owned by Mnemosyne).
- Workflow activity execution or TaskSequence logic (owned by Energeia).
- External network protocol listeners like MLLP (owned by Pylai).

---

## 4. Configuration Parameters `[CONFIGURED]`

| Property / Env Var | Target Module | Default Value | Purpose |
| :--- | :--- | :--- | :--- |
| `PETASOS_BROKER_URL` | `petasos-artemis` | `(tcp://artemis-1:61616,tcp://artemis-2:61616)?ha=true` | Primary failover URL |
| `PETASOS_SESSION_POOL_SIZE` | `petasos-artemis` | `20` | Max pooled JMS sessions |
| `PETASOS_DEDUP_CACHE_SIZE` | `petasos-core` | `20000` | Sliding window dedup size |
| `PETASOS_MAX_REDELIVERY` | `petasos-artemis` | `5` | Redelivery attempts before DLQ |

---

## 5. Verification & Testing `[IMPLEMENTED]`

- **Execute All Petasos Tests**:
  ```bash
  mvn test -pl petasos/petasos-api,petasos/petasos-core,petasos/petasos-artemis,petasos/petasos-test -am
  ```
- **Run Petasos API Isolation Architecture Test**:
  ```bash
  mvn test -pl paradeigma/paradeigma-test -am -Dtest="PetasosApiIsolationArchitectureTest"
  ```
- **Key Test Classes**: `PetasosApiIsolationArchitectureTest`, `ArtemisFailoverTest`, `SlidingWindowDedupTest`.
