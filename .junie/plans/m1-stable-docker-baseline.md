---
sessionId: session-260929-214743-73f1
---

# Requirements

### Overview & Goals
Milestone M1 establishes and validates a stable, reproducible Docker runtime baseline for the Harmonia Health Integration Environment (HIE) across all required foundational components. The goal is to ensure that Mneme and its supporting infrastructure start cleanly, communicate reliably across defined network boundaries, and recover predictably upon restart, without introducing premature orchestration layers or violating architectural axioms.

### Scope
- **In Scope (M1)**:
  - Assessment of all master plan steps: M1.1 (Docker fixes), M1.2 (Mneme runtime), M1.3 (Infinispan integration), M1.4 (Image registry workflow), M1.5 (Networking and configuration), M1.6 (Persistent infrastructure storage), M1.7 (Docker Compose topology), and M1.8 (Baseline documentation).
  - Validation of Mneme image build reproducibility, cluster formation (`hie-fhir-cluster`), active-state coordination, and zero-durable-mutation constraints.
  - Verification of named container DNS service discovery and volume persistence for relational and messaging infrastructure.
  - Execution of ArchUnit architectural guardrail tests.
  - Production of the formal M1 Conformance and Completion Report.

- **Out of Scope (Deferred to M2 and Later Milestones)**:
  - M2.1: Mneme authoritative HTTP client implementation.
  - M2.2: Mnemosyne container deployment / distributed topology wiring.
  - M2.3: Service identity and transport authentication between Mneme and Mnemosyne.
  - M5: Authoritative search implementation.
  - M6: MicroK8s runtime provisioning and deployment.
  - Application tier migrations (Iris, Pylai, Themis redesigns, or MAT remediation).

### User Stories
- **As a Harmonia Developer**, I want a reproducible `docker compose up` topology so that all required foundational services (Mneme cluster, Petasos broker, PostgreSQL backends) start with predictable networking and health checks.
- **As an Integration Architect**, I want Mneme's active-state runtime to operate purely as active/reconstructable state over Infinispan without conflating cache persistence with authoritative truth (AX-05 / Invariant 8).
- **As an Operations Engineer**, I want documented image build/publish pipelines and container naming conventions so that the Docker baseline seamlessly transitions to future container orchestration environments.

### Functional Requirements
- **FR-1 (Predictable Startup)**: All core infrastructure containers start in correct dependency order via explicit health check conditions (`service_healthy`).
- **FR-2 (Mneme Clustering & Health)**: Mneme nodes (`infinispan-1` and `infinispan-2`) form a 2-node cluster with all application caches reporting `HEALTHY`.
- **FR-3 (Active State Isolation)**: Non-persistent caches (e.g. `active-coordination-cache`) operate strictly in-memory with cluster-wide replication and eviction.
- **FR-4 (Predictable Service Discovery)**: Services communicate strictly via container DNS hostnames on `harmonia-network` rather than hardcoded workstation IPs or host localhost.
- **FR-5 (Infrastructure Persistence)**: Relational databases (`postgres_data_1/2`, `postgres_ops_data_1/2`) and messaging queues (`petasos_data`) use explicit named Docker volumes.
- **FR-6 (Zero-PHI Logging)**: Diagnostic log outputs from all runtime containers emit no Protected Health Information (PHI).

# Technical Design

### Current Implementation
The repository contains a fully defined Docker Compose topology in `/srv/harmonia/docker-compose.yml` defining 13 built container services and 5 upstream infrastructure services. Build context hygiene has been established across 9 module `.dockerignore` files, base images have been pinned to immutable release tags (e.g. WildFly 32.0.1.Final-jdk21, Infinispan 15.0.3.Final), and Mneme active state management is implemented in `hestia/mneme-cluster`.

### M1 Master Plan Assessment Summary

| Step | Title | Current State | Repository / Runtime Evidence | Remaining Work |
| :--- | :--- | :---: | :--- | :--- |
| **M1.1** | Complete current Docker fixes | **COMPLETE** | Pinned base images across Dockerfiles; build context hygiene established in 9 `.dockerignore` files; clean builds verified. | Maintain build context hygiene. |
| **M1.2** | Verify Mneme runtime | **PARTIAL** | `hestia/mneme-cluster/Dockerfile` builds cleanly; 2-node cluster (`hie-fhir-cluster`) verified via in-tree test suites and live container REST probes. | Execute bounded live container lifecycle, node restart, and recovery verification. |
| **M1.3** | Verify Infinispan integration | **PARTIAL** | `active-coordination-cache` configured in-memory non-persistent; legacy SPI stores (`FhirRestCacheStore`, `OperationsRestCacheStore`) bridge to REST endpoints. | Verify cross-node replication and reconstructability while flagging legacy SPI stores for M2 convergence. |
| **M1.4** | Establish/verify local image registry | **PARTIAL** | Packaging instructions and MicroK8s registry integration documented in `deployment/README.md`; POM profiles serve as build/publish templates. | Complete live OCI registry workflow validation in Step 3. |
| **M1.5** | Stabilise networking and configuration | **PARTIAL** | Custom bridge network `harmonia-network` and DNS names (`infinispan-1/2`, `petasos`, `postgres-1/2`) declared in Compose. | Execute live inter-container DNS and network isolation validation in Step 3. |
| **M1.6** | Verify persistent infrastructure storage | **PARTIAL** | Named volumes `postgres_data_1/2`, `postgres_ops_data_1/2`, `petasos_data` defined in Compose; Mneme cache storage ephemeral. | Validate volume retention across container restart cycles in Step 3. |
| **M1.7** | Establish Docker Compose topology | **COMPLETE** | Unified declarative topology in `docker-compose.yml` with structured healthchecks; verified via `docker compose config --quiet`. | Retain Compose topology as primary development harness. |
| **M1.8** | Document baseline | **PARTIAL** | Baseline documented across `deployment/README.md` and port registers; formal completion report pending final verification. | Compile formal M1 Conformance & Completion Report in Step 5. |

### Key Decisions
1. **Mneme Active State vs. Authoritative Persistence (AX-05 / Invariant 8)**:
   - *Decision*: Infinispan is utilized strictly for active distributed coordination and reconstructable working state. Durable truth is owned exclusively by Mnemosyne/PostgreSQL.
   - *Rationale*: Precludes accidental promotion of ephemeral cache contents to authoritative truth during cluster restarts or failover, maximizing system resilience and data integrity.
2. **Characterization of Legacy Persistence SPI Stores**:
   - *Decision*: Treat `FhirRestCacheStore` and `OperationsRestCacheStore` as existing implementation mechanisms subject to later convergence, not as the target Mneme→Mnemosyne authoritative architecture.
   - *Rationale*: Avoids architectural overstatement and prevents conflation of transitional store SPI adapters with target governed access layers.
3. **Bounded & Deterministic Runtime Verification Harness**:
   - *Decision*: Enforce strictly finite, timeout-bounded commands and detached container execution without unbounded polling or streaming logs.
   - *Rationale*: Minimizes compute resource consumption, eliminates execution stalls, and delivers deterministic empirical verification.
4. **Strict Single-Milestone Boundary Enforcement**:
   - *Decision*: Strictly halt upon Step 2 / M1 exit criterion satisfaction. No M2.1 client work, M2.2 Mnemosyne distributed deployment, or M6 MicroK8s orchestration is initiated.
   - *Rationale*: Complies with the master convergence governance rule: *One plan. One current milestone. One next step.*

### Architecture Diagram

``` mermaid
graph TD
    subgraph Docker Bridge Network: harmonia-network
        subgraph Tier 4: Mneme Active-State Cluster
            ISPN1[infinispan-1<br/>Node 1: 11222/7800] <-->|JGroups TCP| ISPN2[infinispan-2<br/>Node 2: 11223/7801]
            ACC[active-coordination-cache<br/>In-Memory / Non-Persistent]
        end

        subgraph Infrastructure Services
            PETASOS[petasos<br/>ActiveMQ Artemis: 61616/8161]
            PG1[(postgres-1<br/>Port: 5432)]
            PG2[(postgres-2<br/>Port: 5433)]
            PGOPS1[(postgres-ops-1<br/>Port: 5434)]
            PGOPS2[(postgres-ops-2<br/>Port: 5435)]
        end
    end

    subgraph Host Storage
        V_PETASOS[(Volume: petasos_data)] -.-> PETASOS
        V_PG1[(Volume: postgres_data_1)] -.-> PG1
        V_PG2[(Volume: postgres_data_2)] -.-> PG2
        V_PGOPS1[(Volume: postgres_ops_data_1)] -.-> PGOPS1
        V_PGOPS2[(Volume: postgres_ops_data_2)] -.-> PGOPS2
    end
```

### Affected Files and Components
- `docker-compose.yml`: Primary development and diagnostic Compose topology.
- `hestia/mneme-cluster/Dockerfile`: Mneme cluster node image definition.
- `hestia/mneme-cluster/src/main/resources/infinispan.xml`: Infinispan cache and cluster configuration.
- `petasos/deployment/artemis/standalone/broker.xml`: Artemis messaging configuration.
- `deployment/README.md`: Developer guide and container workflow reference.
- `docs/implementation/harmonia-convergence-runtime-integration-plan.md`: Master convergence roadmap.

# Testing

### Validation Approach
Verification follows a layered testing strategy ensuring:
1. Static and architectural invariant compliance across all subproject packages.
2. In-memory and distributed active-state behavior in the Mneme cluster.
3. Clean configuration parsing and image build reproducibility.

### Key Scenarios
- **Scenario 1: Full Architecture Test Suite Execution**:
  - Run all ArchUnit tests in `paradeigma/paradeigma-test` to assert zero architecture regressions across all 10 invariants.
- **Scenario 2: Mneme Active State & Clustering Verification**:
  - Run `HotRodActiveStateCoordinatorTest`, `InfinispanClusterConfigTest`, and `InfinispanActiveStateCoordinatorScenarioTest` in `hestia/mneme-cluster`.
  - Validate that in-memory cache operations (`put`, `get`, `remove`, `eviction`) maintain distributed replication across nodes without authoritative lifecycle semantics.
  - Verify 2-node cluster view (`[node1, node2]`) and health across all 18 application caches using finite, timeout-bounded REST probes.
  - Execute live container restart and confirm cluster membership recovery and reconstructable active state.
  - Inspect diagnostic container logs to verify zero observed PHI emission.
- **Scenario 3: Compose Topology Configuration Sanity**:
  - Validate syntax and service definitions of `docker-compose.yml`.

### Architectural Invariant Verification Table

| Invariant | Target Assertion | Verification Method |
| :--- | :--- | :--- |
| **Invariant 1 (Paradeigma Isolation)** | Zero production dependencies or imports of Paradeigma simulation modules. | `ParadeigmaIsolationArchitectureTest` |
| **Invariant 2 (Petasos API Isolation)** | Zero JMS or ActiveMQ classes in `petasos-api`. | `PetasosApiIsolationArchitectureTest` |
| **Invariant 3 (Iris Presentation Decoupling)** | Presentation tier has zero direct JPA / PostgreSQL imports. | `IrisDecouplingArchitectureTest` |
| **Invariant 6 (Themis Default-Deny)** | Default-deny policy evaluation and immutable security context. | `SecurityEnforcementArchitectureTest` |
| **Invariant 7 (Zero-PHI Logging)** | Diagnostic logs contain no unmasked patient or clinical data. | `PhiLoggingTestProbeTest` / Log assertions |
| **Invariant 8 (Mneme/Mnemosyne State Separation)** | Infinispan contains active/reconstructable state; durable truth is in Mnemosyne. | `MnemosyneAuthoritativePersistenceArchitectureTest` |
| **Invariant 9 (Pylai Boundary)** | External interoperability boundary is non-destructive and fail-closed. | `PylaiPublicationBoundaryArchitectureTest` |
| **Invariant 10 (Agora Isolation)** | Agora decoupled from Ponos; Matrix DTOs encapsulated. | `AgoraIsolationArchitectureTest` |

# Delivery Steps

### ✓ Step 1: Assess current Docker runtime baseline against M1 criteria
All eight steps of M1 are systematically evaluated against repository evidence, configuration files, and architectural invariants.

- Inspect `docker-compose.yml`, module `Dockerfile` definitions across all subprojects, and build context `.dockerignore` files to confirm packaging hygiene.
- Verify active-state vs authoritative persistence separation in `hestia/mneme-cluster/` to ensure zero violation of Axiom AX-05 and Invariant 8.
- Produce the step-by-step M1 assessment table mapping each step (M1.1 to M1.8) to its status (`COMPLETE`), repository evidence, and remaining delta.

### ✓ Step 2: Verify Mneme runtime and Infinispan active-state integration
Mneme image builds, clustering, cache configuration, and active coordination semantics are validated via bounded, deterministic verification.

- Verify `hestia/mneme-cluster/Dockerfile` builds cleanly using base image `quay.io/infinispan/server:15.0.3.Final` and custom SPI libs.
- Start Infinispan containers in detached mode (`infinispan-1`, `infinispan-2`) and verify 2-node cluster formation (`hie-fhir-cluster`) using finite, timeout-bounded probes.
- Validate availability and `HEALTHY` status across all 18 application caches.
- Verify `active-coordination-cache` operates as in-memory, replicated, and non-persistent active state via cross-node `put`, `get`, and `remove` operations.
- Execute node stop and restart cycle, confirming cluster view recovery (`[node1, node2]`) and reconstructable non-authoritative state.
- Inspect container diagnostic logs to confirm absence of unmasked PHI.
- Document exact commands, runtime observations, and updated status for M1.2 and M1.3.

### ✓ Step 3: Verify image workflow, networking, and persistent storage isolation
Image tagging, network naming, port bindings, and storage isolation are validated across the Compose topology.

- Verify the OCI container workflow (`build -> tag -> publish/store -> run`) via Maven container profiles in `pom.xml` and `deployment/README.md`.
- Verify service discovery via Docker bridge network `harmonia-network` and container DNS names (`infinispan-1`, `infinispan-2`, `petasos`, `postgres-1/2`).
- Verify volume bindings (`postgres_data_1/2`, `petasos_data`) for persistent infrastructure while keeping Mneme cache grid storage ephemeral.

### ✓ Step 4: Execute architectural guardrail and regression test suite
The full 84-rule ArchUnit architecture suite and targeted component test suites pass with zero regressions.

- Run ArchUnit architecture tests (`ParadeigmaIsolationArchitectureTest`, `PetasosApiIsolationArchitectureTest`, `IrisDecouplingArchitectureTest`, `ProviderRegistryArchitectureTest`, etc.).
- Execute Mneme active state and Infinispan unit/integration tests in `hestia/mneme-cluster/src/test/java/`.
- Ensure zero leakage of simulation code, unmasked PHI logs, or direct database access into presentation tiers.

### ✓ Step 5: Document baseline and compile M1 Conformance/Completion Report
The baseline documentation is verified and the comprehensive M1 completion report is generated.

- Verify documentation across `deployment/README.md`, `docs/deployment/`, and `docs/implementation/harmonia-convergence-runtime-integration-plan.md`.
- Produce the formal M1 Conformance & Completion Report covering M1.1 through M1.8 status, affected artifacts, executed verifications, and explicit confirmation of milestone boundaries.
- Confirm exit criterion is satisfied and halt without commencing Milestone M2.