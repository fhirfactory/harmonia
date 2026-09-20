---
sessionId: session-260920-122016-1bx6
---

# Requirements

### Overview & Goals
The goal of this investigation is to provide an authoritative, evidence-based architectural assessment of the current runtime deployment of **Petasos** (Messaging & Event Transport) and its physical relationship to **Energeia / Ponos** (Task Processing WorkEngine).

In Harmonia's conceptual model:
- **Petasos** is the enterprise messaging backbone, abstracting and standardizing transport over Apache ActiveMQ Artemis.
- **Ponos** is the asynchronous task processing engine within Energeia, executing tasks, managing thread pools, evaluating Themis authorization, and orchestrating Erga activities.

This investigation resolves the exact runtime coupling between Petasos and Ponos, examines why the system was constructed this way, evaluates the operational and architectural impacts of separating them, and provides concrete current and target runtime blueprints without altering the existing verified baseline.

---

### Scope
- **In Scope**:
  - Codebase and packaging trace of Petasos, Ponos, Pylai, Agora, and Iris components.
  - Determination of Artemis broker ownership, lifecycle, JVM process, storage/journal configuration, and security settings.
  - Comparative analysis between the conceptual domain model and physical deployment topology.
  - Impact analysis across clustering, persistence, scalability, failure domains, networking, and test suites.
  - Target runtime architecture diagrams and migration prerequisites.
- **Out of Scope**:
  - Modifying code, POM files, Docker Compose configurations, Kubernetes manifests, or test suites.
  - Renaming services or altering broker URLs in the running baseline environment.

---

### Key Findings Summary
1. **Actual Runtime Pattern**: **Option A — Artemis is Embedded inside Ponos / task-processor**.
   - An `EmbeddedActiveMQ` instance is programmatically instantiated and owned by the WildFly 31 JVM process running `task-sequence-processor.war` inside the `task-processor` container.
2. **Broker Storage & Security**:
   - Persistence is explicitly disabled (`setPersistenceEnabled(false)`). Journal directories are generated with ephemeral timestamps (`target/artemis-data/journal-...`).
   - Broker security is disabled (`setSecurityEnabled(false)`).
3. **Network Exposure**:
   - Ponos opens a Netty TCP acceptor on `0.0.0.0:61616` from within its WildFly container. External services (`mllp-gateway`, `mllp-outbound-his`, `mllp-outbound-lis`) connect over the Docker bridge network to `tcp://task-processor:61616`. Ponos connects internally via `vm://0`.
4. **Architectural Divergence**:
   - While the codebase cleanly separates messaging interfaces (`petasos-api`) from execution engines (`energeia/ponos`), the physical deployment merges the broker middleware into the worker runtime, creating an inverted lifecycle dependency where the worker owns the messaging infrastructure.

# Technical Design

### 1. Current Implementation Trace

#### 1.1 Source Modules & Packages
- **`petasos/petasos-api`**: Pure Java messaging contracts (`Petasos`, `PetasosProducer`, `PetasosConsumer`, `PetasosMessage`, `PetasosDestination`) in package `net.fhirfactory.harmonia.petasos.api.*`. Strictly free of JMS or vendor classes.
- **`petasos/petasos-core`**: Serialization, sliding deduplication window (`DuplicateDetector`), and metrics collection (`PetasosMetricsCollector`) in package `net.fhirfactory.harmonia.petasos.core.*`.
- **`petasos/petasos-artemis`**: ActiveMQ Artemis client adapter (`ArtemisPetasos`, `ArtemisConnectionManager`, `ArtemisPetasosProducer`, `ArtemisPetasosConsumer`) in package `net.fhirfactory.harmonia.petasos.artemis.*`.
- **`petasos/petasos-test`**: Integration test harness (`EmbeddedArtemisCluster`) and HA failover tests.
- **`energeia/ponos`**: WorkEngine hosting the embedded broker manager (`ArtemisBrokerManager.java`), queue configuration (`QueueConfig.java`), and JMS connection factory producer (`JmsConnectionFactoryProducer.java`) in packages `net.fhirfactory.harmonia.praxis.*`.

#### 1.2 Broker Instantiation & Process Ownership
- **Instantiating Class**: `net.fhirfactory.harmonia.praxis.messaging.ArtemisBrokerManager` in `energeia/ponos`.
- **CDI Lifecycle**: `@ApplicationScoped` bean initialized on startup via `@Observes @Initialized(ApplicationScoped.class)` or `@PostConstruct start()`, and torn down via `@PreDestroy stop()`.
- **Packaging Artifact**: `energeia/ponos/target/task-sequence-processor.war` (Maven artifact `net.fhirfactory.harmonia:ponos:war`).
- **Owning Process**: The WildFly 31 Jakarta EE application server JVM process executing inside the container.
- **Container / Service**: Docker Compose service `task-processor` (container name `hie-task-processor`), built from `energeia/ponos/Dockerfile` (base image `quay.io/wildfly/wildfly:latest-jdk21`).
- **Port Bindings**: Port `61616` is exposed on `task-processor` and mapped to host port `61616:61616`.

#### 1.3 Broker Configuration Details
```java
// energeia/ponos/.../ArtemisBrokerManager.java
Configuration config = new ConfigurationImpl();
config.setPersistenceEnabled(false);
config.setSecurityEnabled(false);
config.setJournalDirectory("target/artemis-data/journal-" + System.nanoTime());
config.setBindingsDirectory("target/artemis-data/bindings-" + System.nanoTime());
config.setLargeMessagesDirectory("target/artemis-data/largemsg-" + System.nanoTime());
config.setPagingDirectory("target/artemis-data/paging-" + System.nanoTime());

config.addAcceptorConfiguration("in-vm", "vm://0");
config.addAcceptorConfiguration(new TransportConfiguration(
    NettyAcceptorFactory.class.getName(), Map.of("host", "0.0.0.0", "port", 61616)));
```
- **Persistence / Journal**: Disabled (`setPersistenceEnabled(false)`). Messages exist solely in volatile JVM memory.
- **Security**: Disabled (`setSecurityEnabled(false)`). Unauthenticated TCP connections accepted.
- **Management Console**: No Artemis web console (port 8161) is enabled or exposed in Ponos. WildFly management (port 9990) and Ponos REST API (port 8080) are exposed instead.
- **Queue Initialization**:
  - Dynamically read from `MessageQueueService.getAll()` (Infinispan cache / JPA store).
  - Baseline fallbacks from `QueueConfig`: `task.processing.queue`, `task.event.queue`, `task.event.queue.mllp-gateway-default`.

#### 1.4 Connected Services
| Service Name | Role | Connection Protocol & Endpoint | Config Class / Variable |
| :--- | :--- | :--- | :--- |
| **`task-processor`** (Ponos) | Consumer (`task.event.queue`), Producer (`outbound.*`) | In-VM `vm://0` (fallback `tcp://localhost:61616`) | `JmsConnectionFactoryProducer.java` |
| **`mllp-gateway`** (`pylai-mllp-in`) | Producer (`task.event.queue`) | TCP `tcp://task-processor:61616` | `TaskProcessorConfig.java` (`TASK_BROKER_HOST`) |
| **`mllp-outbound-his`** (`pylai-mllp-out`) | Consumer (`petasos.queue.mllp.outbound.his_north`) | TCP `tcp://task-processor:61616` | `MllpOutboundConfig.java` (`TASK_PROCESSOR_BROKER_URL`) |
| **`mllp-outbound-lis`** (`pylai-mllp-out`) | Consumer (`petasos.queue.mllp.outbound.lis_main`) | TCP `tcp://task-processor:61616` | `MllpOutboundConfig.java` (`TASK_PROCESSOR_BROKER_URL`) |
| **`agora`** (`agora-service`) | Producer/Consumer (`petasos.queue.agora.*`) | TCP `(tcp://artemis-primary-a:61616,...)` (K8s) | `AgoraTopics.java` (`PETASOS_BROKER_URL`) |

---

### 2. Why It Is Implemented This Way
- **Historical Origin**: Commit `5d87a41a` (*"Expanded Version before Rename"*) added `ArtemisBrokerManager` as an embedded broker directly inside the Ponos WAR to achieve a fully self-contained execution unit during initial development.
- **Development Convenience**: Embedding Artemis inside WildFly reduced the number of separate container workloads required to run integration tests and local single-node workflows.
- **Standalone Artifacts Already Exist**: Standalone 4-node Artemis cluster configurations and Docker Compose definitions were subsequently authored in `petasos/deployment/` (with dedicated `broker.xml` definitions for `primary-a`, `backup-a`, `primary-b`, `backup-b`) and Kubernetes manifests in `deployment/kubernetes/base/petasos/`.
- **Coupling Status**: The arrangement in the root `docker-compose.yml` is an **unmerged historical convenience artifact** rather than a deliberate architectural decision.

---

### 3. Architectural Alignment & Violation Assessment
- **Conceptual Separation**:
  - `Petasos` = Messaging & Transport middleware
  - `Energeia/Ponos` = Task processing and workflow execution
  - Intended Dependency: `Ponos -> Petasos API -> ActiveMQ Artemis`
- **Current Physical Violation**:
  - `Ponos owns ActiveMQ Artemis`: Ponos acts as the infrastructure host, lifecycle manager, and network listener for the broker.
  - `Inverted Lifecycle & Failure Coupling`: If Ponos encounters an OutOfMemoryError, thread lock, or restarts due to a deployment update, the entire messaging infrastructure crashes, interrupting Pylai MLLP ingress and dropping non-persisted messages.
  - `Horizontal Scalability Anti-Pattern`: Ponos cannot scale to multiple worker instances (`replicas > 1`) in Docker Compose because every instance would attempt to bind TCP port 61616 and run an independent, non-clustered broker.

---

### 4. Separate Petasos Runtime Assessment
Moving ActiveMQ Artemis to a dedicated `petasos` container yields significant architectural and operational improvements:

1. **Architectural Separation & Broker Ownership**:
   - Establishes a true client-server topology. Ponos becomes a pure Petasos client via `petasos-api` and `petasos-artemis`.
   - `ArtemisBrokerManager` is disabled in Ponos (`TASK_BROKER_ENABLED=false`).
2. **Container Lifecycle & Startup Ordering**:
   - `petasos` starts as an infrastructure Tier 3 service alongside `infinispan`.
   - `pylai-mllp-in`, `pylai-mllp-out`, `task-processor`, and `agora` depend on `petasos` health (`condition: service_healthy`).
3. **Persistence & Message Durability**:
   - Dedicated Docker volume (e.g. `petasos_artemis_data:/var/lib/artemis-instance/data`).
   - Enables durable journaling (`setPersistenceEnabled(true)`), ensuring in-flight clinical events survive container restarts.
4. **Failure Isolation & Independent Scaling**:
   - Crash of Ponos WildFly worker does not terminate the broker or drop messages. Pylai continues ingesting clinical messages into Artemis queues.
   - Ponos can scale horizontally across $N$ worker containers consuming from `task.event.queue` using standard JMS competing consumers.
5. **Observability & Health Telemetry**:
   - Artemis Web Console and Management API enabled on port `8161`.
   - Iris Monitor / BEFE (`PetasosHealthProvider`) connects directly to Artemis management API / JMX / Jolokia endpoints for real queue depth, enqueue/dequeue rate, and consumer metrics instead of stub data.
6. **Local Compose vs Kubernetes Parity**:
   - Harmonizes root `docker-compose.yml` with the Kubernetes deployment (`deployment/kubernetes/base/petasos/artemis-*.yaml`), which already specifies independent Artemis broker pods.

---

### 5. Architectural Diagrams

```
========================================================================================
CURRENT RUNTIME ARCHITECTURE (EMBEDDED IN PONOS)
========================================================================================

    [ External Systems ]
             │
             │ MLLP :2575
             ▼
    ┌────────────────────────────────────────────────────────┐
    │  hie-mllp-gateway (pylai-mllp-in)                      │
    └────────────────────────┬───────────────────────────────┘
                             │
                             │ TCP :61616 (TASK_BROKER_HOST=task-processor)
                             ▼
    ┌────────────────────────────────────────────────────────┐
    │  hie-task-processor (energeia/ponos container)         │
    │                                                        │
    │  ┌──────────────────────────────────────────────────┐  │
    │  │ WildFly 31 Application Server (JVM Process)      │  │
    │  │                                                  │  │
    │  │  ┌────────────────────────────────────────────┐  │  │
    │  │  │ ArtemisBrokerManager (EmbeddedActiveMQ)    │  │  │
    │  │  │ - Acceptors: vm://0, tcp://0.0.0.0:61616   │  │  │
    │  │  │ - Storage: In-Memory / Ephemeral Journal   │  │  │
    │  │  │ - Security: Disabled                       │  │  │
    │  │  └──────────────────────┬─────────────────────┘  │  │
    │  │                         │ vm://0                 │  │
    │  │  ┌────────��─────────────▼─────────────────────┐  │  │
    │  │  │ Ponos WorkEngine (Task Sequence Processor) │  │  │
    │  │  │ - PetasosQueueToExchangeConduit            │  │  │
    │  │  │ - Praxis / Erga Pipelines                  │  │  │
    │  │  └──────────────────────┬─────────────────────┘  │  │
    │  └─────────────────────────┼────────────────────────┘  │
    └────────────────────────────┼───────────────────────────┘
                                 │
                                 │ TCP :61616 (petasos.queue.mllp.outbound.*)
                                 ▼
    ┌────────────────────────────────────────────────────────┐
    │  hie-mllp-outbound-his / lis (pylai-mllp-out)          │
    └────────────────────────────────────────────────────────┘
```

```
========================================================================================
TARGET CANDIDATE RUNTIME ARCHITECTURE (DEDICATED PETASOS CONTAINER)
========================================================================================

    [ External Systems ]
             │
             │ MLLP :2575
             ▼
    ┌────────────────────────────────────────────────────────┐
    │  hie-mllp-gateway (pylai-mllp-in)                      │
    └────────────────────────┬───────────────────────────────┘
                             │
                             │ TCP :61616 (Publish task.event.queue)
                             ▼
    ┌────────────────────────────────────────────────────────┐
    │  hie-petasos (Dedicated Artemis Broker Container)      │
    │  Image: apache/activemq-artemis:2.33.0                 │
    │  - Ports: 61616 (Core/JMS), 8161 (Web Console/Metrics) │
    │  - Volume: petasos_data:/var/lib/artemis-instance/data │
    │  - Persistence: Durable Journal Enabled                │
    │  - Clustering: Ready for HA Primary/Backup Topology    │
    └───────────────┬────────────────────────┬───────────────┘
                    │                        │
       TCP :61616   │           TCP :61616   │
       (Consume     │           (Consume     │
        task.event) │            outbound)   │
                    ▼                        ▼
    ┌───────────────────────────────┐ ┌──────────────────────────────┐
    │  hie-task-processor (Ponos)   │ │  hie-mllp-outbound-his / lis │
    │  WildFly 31 (Pure Worker)     │ │  pylai-mllp-out (Egress)     │
    │  - TASK_BROKER_ENABLED=false  │ └──────────────────────────────┘
    │  - Scales to N worker replicas│
    └───────────────────────────────┘
```

---

### 6. Open Questions & ADRs Required Prior to Implementation
1. **ADR-PETASOS-001: Separation of Petasos Messaging Runtime**: Formally deprecate embedded Artemis execution inside Ponos and mandate dedicated Petasos container runtime across all deployment topologies.
2. **ADR-PETASOS-002: Broker Topology Selection for Local Development**: Decide whether local Docker Compose runs a single-node standalone Artemis container or the full 4-node HA cluster defined in `petasos/deployment/docker-compose.yml`.
3. **ADR-PETASOS-003: Message Durability & Journal Retention Policy**: Define journal flush guarantees and volume retention policies for healthcare compliance.
4. **ADR-PETASOS-004: Artemis Authentication & Secrets Management**: Transition from `setSecurityEnabled(false)` to role-based broker authentication with credentials injected via environment secrets.

# Testing

### Validation Approach
Verification of the architecture investigation findings involves zero-risk static and configuration analysis, confirming that the baseline environment remains untouched while all findings are fully substantiated by the repository.

---

### Key Scenarios & Verification Evidence
1. **Scenario 1: Embedded Broker Verification**:
   - Verified that `ArtemisBrokerManager.java` contains `new EmbeddedActiveMQ()`, `config.setPersistenceEnabled(false)`, and registers Netty TCP port `61616`.
   - Verified that `energeia/ponos/pom.xml` packages `artemis-jakarta-server:2.33.0`.
   - Verified that `energeia/ponos/Dockerfile` exposes port 61616 on the WildFly image.
2. **Scenario 2: Gateway Connection Verification**:
   - Verified that `docker-compose.yml` passes `TASK_BROKER_HOST: task-processor` and `TASK_PROCESSOR_BROKER_URL: tcp://task-processor:61616` to `mllp-gateway` and `mllp-outbound-*`.
3. **Scenario 3: Standalone Reference Verification**:
   - Verified that `petasos/deployment/docker-compose.yml` and `deployment/kubernetes/base/petasos/` contain reference standalone Artemis configurations.
4. **Scenario 4: Non-Interference with Baseline**:
   - Confirmed that no POM files, source files, Dockerfiles, or Compose configurations were modified during this investigation.

# Delivery Steps

### ✓ Step 1: Audit Current Petasos Runtime and Packaging Boundaries
Conduct a comprehensive codebase and runtime trace to identify broker instantiation, thread ownership, lifecycle management, and network bindings across all subprojects.

- Audit `energeia/ponos` (`ArtemisBrokerManager.java`, `QueueConfig.java`, `JmsConnectionFactoryProducer.java`) to verify the exact instantiation path of `EmbeddedActiveMQ` inside the WildFly JVM process.
- Audit all broker clients and consumers across `pylai/pylai-mllp-in`, `pylai/pylai-mllp-out`, `agora/agora-service`, and `iris/iris-befe` to document their network connection strings (`tcp://task-processor:61616`) and environment variables.
- Inspect packaging configurations across `energeia/ponos/pom.xml`, `energeia/ponos/Dockerfile`, and root `docker-compose.yml` to confirm container ownership of port 61616.

### ✓ Step 2: Evaluate Separation Impact and Operational Architecture
Assess the technical and operational implications of separating ActiveMQ Artemis from the Ponos WildFly container into a dedicated Petasos runtime container.

- Evaluate container lifecycle, Docker Compose and Kubernetes networking, healthcheck probes, and startup ordering dependencies (`depends_on`).
- Evaluate message durability, journal filesystem volume mounts (`petasos_data`), security/authentication boundaries, and Iris Monitor telemetry discovery.
- Analyze the impact on horizontal scalability (scaling Ponos worker replicas without broker collisions), high availability (Artemis clustering), and existing integration test harnesses (`petasos-test`, `EmbeddedArtemisCluster`).

### ✓ Step 3: Define Target Architecture Model and Migration Roadmap
Formulate target architecture models, transition matrices, and Architectural Decision Records (ADRs) to guide future runtime separation.

- Produce formal Architectural Decision Record (ADR) drafts documenting the migration from embedded broker to standalone Petasos service.
- Define target configuration schemas, environment variable conventions (`PETASOS_BROKER_URL`), and Docker Compose/Kubernetes service definitions.
- Establish validation criteria and rollback safeguards to preserve the verified baseline stability during any eventual implementation.