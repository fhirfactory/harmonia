Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: Yes

**Requirements**

**Overview & Goals**  
This assessment delivers a bounded, read-only architectural and implementation evaluation to establish the intended remediation approach for **MAT-01**, **MAT-02**, and **MAT-05** from the *Harmonia Architectural Axiom Conformance Assessment*.

The primary objectives are:
1. **Restore State Separation (AX-05)**: Establish a mechanically enforced application-facing **Mneme** managed-information boundary for active use, while preserving **Mnemosyne** as the sole establisher of authoritative durable state.
2. **Eliminate Raw Active Cache Mutation (MAT-01)**: Prevent application and presentation tiers from mutating Infinispan cache memory directly, performing uncoordinated writes, or inventing local version progressions.
3. **Establish Authoritative Search Boundary (MAT-02, ADR-010)**: Prohibit cluster-wide `remoteCache.values()` memory scans, define a type-neutral authoritative search contract, and isolate concrete search implementation behind the unresolved **MAT-06** boundary.
4. **Decouple Presentation from Persistence (MAT-05, Invariant 3)**: Remove direct PostgreSQL/JDBC/JPA coupling and `@DataSourceDefinition` bindings from `iris-befe` for both clinical and audit data without conflating Kleio audit evidence with Mnemosyne clinical persistence.
5. **Preserve Governed Information Lifecycle (ADR-020)**: Model record retirement as governed authoritative `UPDATE` operations using lifecycle semantics appropriate to each information type, rejecting unsupported HTTP `DELETE` operations at presentation boundaries.
6. **Strictly Isolate MAT-06**: Keep all immediate remediation designs completely agnostic to whether Mnemosyne Clinical uses custom relational JPA persistence or HAPI FHIR JPA Server.

---

**Scope**  
**In Scope**
- Comprehensive architectural assessment of the current runtime topology and execution containers (`iris-befe`, `infinispan-cluster`, `mnemosyne-clinical`, `pylai-fhir-registry`).
- Tracing and diagnosing all 8 concrete read, create, update, delete, search, and audit access paths across the repository.
- Root-cause reframing identifying the absence of a mechanically enforced Mneme managed-information boundary.
- Technical evaluation of previous Task 08/09/10 artefacts (retain, rework, supersede, or block).
- Target logical access models for point `READ`, `CREATE`, `UPDATE`, lifecycle transitions, `SEARCH`, and `Kleio` audit queries.
- Clear separation between application/domain contracts (e.g. `WriteResult`, `GovernedRead`) and presentation HTTP status mappings (`201`, `200`, `404`, `405`, `409`, `OperationOutcome`).
- Refined 5-goal implementation sequence with clear decoupling of Goal 3A (search contract/port) from Goal 3B (concrete search implementation blocked by MAT-06) and separate resolution of Goal 4 (Iris -> Kleio audit boundary).
- Mechanical enforcement strategy using ArchUnit and fail-closed platform APIs.

**Out of Scope**
- Implementing code modifications or making git commits (strictly a planning and architecture assessment).
- Resolving **MAT-06** (custom relational persistence vs. HAPI FHIR JPA Server).
- Implementing Goal 3B concrete authoritative search prior to MAT-06 resolution.
- Creating new distributed network protocols or microservices.
- Conflating Kleio audit evidence with Mneme/Mnemosyne clinical information.

---

**Architectural Authority & Axiom Governance**  
All analyses adhere strictly to the Harmonia Architectural Authority Hierarchy:
1. `docs/architectural-axioms.md` (Highest authority)
    - **AX-01**: Health-Information Centricity.
    - **AX-02**: Standards at the boundary; Harmonia within the boundary.
    - **AX-04**: Use the machinery; own the semantics.
    - **AX-05**: Active state and authoritative durable state are distinct (*"Mneme manages active use; Mnemosyne establishes durable truth"*).
    - **AX-06**: Explicit information authority.
    - **AX-07**: Security is intrinsic to managed operations (Themis default-deny).
    - **AX-10**: Resilience through visible failure (no silent in-memory fallback).
    - **AX-11**: Responsive and highly available managed information access.
    - **AX-12**: Hide plumbing, not information.
    - **AX-13**: Harmonia management has an explicit boundary (fail-closed egress projection).
2. `../../AGENTS-old2.md` (Repository Invariants 1 through 10, particularly Invariant 3 [Iris Presentation Decoupling] and Invariant 8 [Mneme/Mnemosyne State Separation]).
3. Accepted Architecture Decisions (`ADR-003`, `ADR-006`, `ADR-010`, `ADR-013`, `ADR-018`, `ADR-019`, `ADR-020`).

**Technical Design**

**1. Current Runtime Topology**

The Harmonia platform executes across distinct runtime processes and communication protocols:

| Runtime Process / Container | Framework / Host | Network / Ports | Core Subsystems Hosted | Outbound Communication Mechanisms |
| :--- | :--- | :--- | :--- | :--- |
| **`iris-befe`** (`harmonia-befe`) | WildFly Jakarta EE 10 | HTTP `8080` (Clinical), `8090` (Ops), `9990` (Admin) | Iris BEFE REST resources, CORS filters, Themis security filters | • Hot Rod TCP (`11222`) to `infinispan-1`<br/>• Direct JDBC (`5432`) to PostgreSQL `fhir_node_1` (MAT-05 violation) |
| **`mneme-cluster`** (`infinispan-1`, `infinispan-2`) | Infinispan 15 Standalone | Hot Rod TCP `11222`, JGroups `7800` | Clustered active caches, `active-coordination-cache`, `FhirRestCacheStore` | • HTTP Client (`8080`) to `mnemosyne-clinical` via `HapiFhirRestClient` (read-through / write-behind SPI) |
| **`mnemosyne-clinical`** (`hapi-fhir-1`, `hapi-fhir-2`) | Spring Boot 3 + HAPI FHIR | HTTP `8080` (internal) / `8081` (external host) | HAPI FHIR `JpaRestfulServer`, `FhirStorageService`, `AuthoritativePersistenceService`, `DefaultGovernedWriter` | • JDBC (`5432`) to PostgreSQL `fhir_node_1` |
| **`mnemosyne-operations`** (`operations-1`, `operations-2`) | Spring Boot 3 | HTTP `8080` (internal) / `8085` (external host) | Operations REST APIs, Task Sequence state persistence | • JDBC (`5432`) to PostgreSQL `ops_node_1` |
| **`petasos`** (`harmonia-petasos`) | ActiveMQ Artemis 2.33 | OpenWire TCP `61616` | Petasos resilient message queues | Artemis clustering protocols |
| **`energeia-ponos`** (`task-processor`) | WildFly + Apache Camel | HTTP `8083` | Ponos WorkEngine, Praxis workflow orchestration, Erga activities | • OpenWire TCP (`61616`) to Petasos<br/>• Hot Rod TCP (`11222`) to Infinispan |
| **`pylai-fhir-registry`** | Spring Boot 3 | HTTP `8082` | Inbound FHIR REST gateway, `ChangeRequestSubmissionService` | • OpenWire TCP (`61616`) to Petasos<br/>• In-process / HTTP to `FhirStorageService` |

---

**2. Current Access Paths & Bypasses**

```
1. Iris/BEFE -> READ Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.read(id)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.getResourceJson -> remoteCache.get(id)
     -> Authoritative-State: [BYPASSED if present in cache] / CacheStore HTTP GET to Mnemosyne on miss
     -> Durable Store: PostgreSQL fhir_node_1 (via CacheStore read-through fallback only)
   [STATUS]: DEFECTIVE (Treats cache as primary record; no active-state token or provenance boundary)

2. Iris/BEFE -> CREATE Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.create(payload)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.putResourceJson -> remoteCache.put(id, payload)
     -> Authoritative-State: [ABSENT / BYPASSED] (GovernedWriter / AuthoritativePersistencePort ignored)
     -> Durable Store: [BYPASSED] (Volatile cache only; uncoordinated write-behind may race)
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-01)

3. Iris/BEFE -> UPDATE Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.update(id, payload)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.saveResource -> sets local versionId=ver+1 -> remoteCache.put(id)
     -> Authoritative-State: [ABSENT / BYPASSED] (ActiveStateCoordinator / CAS persistence ignored)
     -> Durable Store: [BYPASSED]
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-01, local version progression)

4. Iris/BEFE -> Lifecycle / Deletion
   Caller: Iris SPA
     -> Application API: PractitionerResource.delete(id)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.deleteResource -> remoteCache.remove(id)
     -> Authoritative-State: [ABSENT / BYPASSED]
     -> Durable Store: [BYPASSED / Physical delete if cache store syncs]
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-01, ADR-020 violation: physical delete exposed)

5. Iris/BEFE -> SEARCH Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.search(id, name, identifier)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.searchResources -> remoteCache.values() full scan into heap
     -> Authoritative-State: [ABSENT / BYPASSED] (Mnemosyne / HAPI search indexing bypassed)
     -> Durable Store: [BYPASSED]
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-02, ADR-010 violation)

6. Pylai -> READ Practitioner
   Caller: External FHIR Client
     -> Application API: FhirRestGatewayController.readResource(type, id)
     -> Module: pylai-fhir-registry
     -> Runtime Boundary: In-process Spring bean / HTTP
     -> Active-State: [BYPASSED] (Does not check Mneme active cache)
     -> Authoritative-State: FhirStorageService.getResource -> FhirResourceRepository.findByResourceTypeAndFhirId
     -> Durable Store: PostgreSQL hie_fhir_resources
     -> Egress: PylaiFhirPublicationProjector.projectForPublication
   [STATUS]: AUTHORITATIVE (Bypasses Mneme cache-aside acceleration)

7. Pylai -> SEARCH Practitioner
   Caller: External FHIR Client
     -> Application API: FhirRestGatewayController.searchResources(type, allParams)
     -> Module: pylai-fhir-registry
     -> Runtime Boundary: In-process Spring bean / HTTP
     -> Active-State: [BYPASSED]
     -> Authoritative-State: FhirStorageService.searchResources -> repository.findByResourceType + in-memory filtering
     -> Durable Store: PostgreSQL hie_fhir_resources
     -> Egress: PylaiFhirPublicationProjector.projectForPublication
   [STATUS]: AUTHORITATIVE (Subject to persistence query capabilities)

8. Iris/BEFE -> Direct Kleio Audit Persistence (MAT-05)
   Caller: Iris Console / Admin SPA
     -> Application API: AuditEventResource.search() / read(id)
     -> Module: iris-befe
     -> Runtime Boundary: Direct JDBC (:5432) via AuditDataSourceProducer
     -> Direct Coupling: @DataSourceDefinition binds KleioAuditDS directly to postgres-1
     -> Execution: DurableAuditService / JdbcAppendOnlyAuditEventRepository executes SQL queries in BEFE
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-05, Invariant 3 violation)
```

---

**3. MAT-01 / MAT-02 / MAT-05 Root-Cause Assessment**

**Reframed Root Cause**  
The architectural root cause is:
> **"There is no mechanically enforced application-facing Mneme managed-information boundary, causing callers to receive raw infrastructure capabilities and bypass Harmonia information-management semantics."**

The defect is not merely the absence of a class named `MnemeClient`. Rather, because `iris-befe` was provided with raw infrastructure primitives (`RemoteCacheManager` Hot Rod client and container `@DataSourceDefinition` JDBC pools) rather than a governed application boundary:
1. Application endpoints defaulted to treating Infinispan `RemoteCache` as a key-value database for point CRUD, inventing uncoordinated writes and local version progressions (`MAT-01`).
2. Search was implemented by naively pulling all cached strings via `remoteCache.values()` and filtering them in JVM memory (`MAT-02`).
3. Presentation queries for audit events were wired directly to PostgreSQL JDBC repositories because no service-level query port existed (`MAT-05`).

**Analysis of Existing Mneme APIs vs. Application Facade Boundary**  
Inspection of existing public interfaces reveals:
- `calliope/model/governedwrite/GovernedWriter.java`: Exposes pure write contracts (`create` and `update`) with `ResourceKey`, `GovernedRead`, `WriteResult`, and `ThemisSecurityContext`.
- `calliope/model/governedwrite/ActiveStateCoordinator.java` & `ActiveStateConvergencePort.java`: Internal active-state coordination and post-commit cache convergence SPIs.
- `hestia/mnemosyne-clinical`: Contains `DefaultGovernedWriter` orchestrating writes across Themis, active coordination, authoritative persistence, and convergence.

**Why an Evolved Application-Facing Boundary Is Required**:  
While `GovernedWriter` provides a sound contract for write operations, application callers require a cohesive, unified managed-information access boundary that provides:
1. **Point READ**: Cache-aside resolution with authoritative fallback on cache miss and active read-repair (`get(ResourceKey)` returning `GovernedRead<T>`).
2. **Governed WRITE**: `create` and `update` delegating through the governed write pipeline.
3. **Governed SEARCH**: Explicit search delegation port prohibiting in-memory cache scans.
   Whether this boundary is realized as an expanded `MnemeAccessPort` / `MnemeClient` or an evolution of existing contracts, it must encapsulate raw `RemoteCache` access and mechanically enforce Harmonia governance.

**Managed-Type Classification Inspection**  
Inspection of Calliope and Mneme shows:
- `calliope/model/registry/ProviderRegistryConstants.java` defines `SUPPORTED_RESOURCE_TYPES` (`Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, `Group`) and validation rules.
- Calliope canonical schema definitions represent the semantic source of truth for Harmonia domain types.
- **Architectural Decision**: Semantic classification (`information type -> managed/governed semantics`) must have **one authoritative owner** (Calliope). Mneme consumes this semantic classification to manage physical cache and runtime configurations. A separate, parallel `ManagedTypeRegistry` must NOT be created before semantic ownership is formally decided.

**Kleio Audit Query Boundary Inspection**
- `kleio-core` defines `AuditService`, `AuditQuery`, and `HarmoniaAuditEvent`.
- `kleio-persistence` provides `DurableAuditService` and `JdbcAppendOnlyAuditEventRepository`.
- `kleio-fhir` provides `HarmoniaAuditEventMapper`.
- **Architectural Boundary**: Kleio audit evidence represents immutable security/audit history and is **not** Mnemosyne-managed clinical domain information. Remediating MAT-05 in `iris-befe` must **not** route audit queries through Mneme by default. Instead, the application-facing Kleio query boundary (`Iris -> ? -> Kleio`) is treated as a separate architectural problem to be resolved without direct JDBC bindings.

---

**4. Existing Machinery That Can Be Reused**

The repository already contains high-quality, production-ready building blocks:
1. **Governed Write Pipeline (`hestia/mnemosyne-clinical` & `calliope`)**:
    - `DefaultGovernedWriter`: Full orchestration of Themis security -> `ActiveStateCoordinator` -> `AuthoritativePersistencePort` -> `ActiveStateConvergencePort`.
    - `AuthoritativePersistenceService`: Atomic CREATE uniqueness and conditional UPDATE with expected predecessor version checking (`TransactionTemplate`).
    - Sealed result types: `WriteResult`, `AuthoritativePersistenceResult`, `ConvergenceStatus`.
2. **Active State Coordination & Convergence (`hestia/mneme-cluster`)**:
    - `HotRodActiveStateCoordinator`: Lightweight token observation and atomic consumption over `active-coordination-cache` with fail-fast visible failure.
    - `HotRodMnemeConvergence`: Bounded CAS loop post-commit cache convergence with newer-version protection.
3. **Publication & Security Boundary (`pylai-fhir-registry` & `calliope`)**:
    - `PylaiFhirPublicationProjector`: Non-destructive fail-closed egress sanitization stripping internal operational metadata.
    - `FhirSecurityTagManager` & `ThemisSecurityContext`: Explicit distinction between persistent clinical confidentiality labels and transient operational security context.
4. **Kleio Audit Infrastructure (`kleio`)**:
    - `AuditService`, `AuditQuery`, and `HarmoniaAuditEventMapper`: Standardized interfaces for querying and mapping immutable audit events.
5. **Resilient Transport (`petasos` & `energeia-ponos`)**:
    - ActiveMQ Artemis queues and Camel `Pragma` task sequence pipelines for asynchronous governed change workflows.

---

**5. Previous Task 08/09/10 Assessment**

| Task / Artefact | Repository Location | Assessment | Detailed Rationale & Action |
| :--- | :--- | :--- | :--- |
| **Task 08: Governed Write Contracts** | `calliope/model/governedwrite/*` | **RETAIN** | Perfectly aligned with AX-05, AX-07, and AX-10. Retain pure contracts (`GovernedWriter`, `ResourceKey`, `ActiveStateToken`, `ExpectedAuthoritativeVersion`, `WriteResult`). |
| **Task 08: Active-State Coordinator** | `hestia/mneme-cluster/coordination/*` | **RETAIN** | Production Hot Rod coordinator over `active-coordination-cache`. Retain as internal engine component. |
| **Task 08: Active-State Convergence** | `hestia/mneme-cluster/convergence/*` | **RETAIN** | Production Hot Rod CAS convergence port with version protection. Retain as internal engine component. |
| **Task 08: Authoritative Persistence** | `hestia/mnemosyne-clinical/persistence/*` | **RETAIN (LOGICAL)** | Atomic persistence logic and result models are valid. Retain interface contracts; persistence implementation remains subject to MAT-06 abstraction. |
| **Task 08: BEFE Write Migration (08.05A)** | `.junie/plans/migrate-befe-clinical-writes.md` | **REWORK** | Target concept of unified Mneme access boundary is correct; re-align with revised Goal 2. |
| **Task 09: Cache-Aside Point Reads** | `docs/backlog/Harmonia - Task 9 - Backlog.md` | **REWORK / INTEGRATE** | Cache-aside read on miss + read-repair is essential for AX-05/AX-11. Integrate into Goal 3A while strictly isolating internal version provenance (`BL-09-01`). |
| **Task 10: Governed Lifecycle (ADR-020)** | `docs/architecture-decisions.md` (ADR-020) | **RETAIN** | Prohibits physical deletion. Model retirement as authoritative `UPDATE`; reject HTTP `DELETE` at presentation boundary with `405 Method Not Allowed`. |
| **Legacy Infinispan Cache Store SPI** | `hestia/mneme-persistence/*` | **SUPERSEDE** | Uncoordinated background HTTP store (`FhirRestCacheStore`) causes race conditions and hides persistence failure. Supersede with explicit synchronous cache-aside and governed write convergence. |

---

**6. Target Logical Access Model**

```mermaid
graph LR
    subgraph Client Tier
        APP[Application / Iris BEFE]
    end

    subgraph Mneme Boundary
        MC[Mneme Access Boundary / Facade]
        COORD[ActiveStateCoordinator]
        CONV[ActiveStateConvergencePort]
    end

    subgraph Governance & Security
        THM[Themis Policy Engine]
    end

    subgraph Mnemosyne Authoritative Tier
        GW[GovernedWriter]
        AUTH_PORT[AuthoritativePersistencePort]
        SEARCH_PORT[AuthoritativeSearchPort]
        STORE[(Authoritative Store / Postgres)]
    end

    subgraph Active Cache
        CACHE[(Infinispan Cluster)]
    end

    %% Read Flow
    APP -- 1. get(key) --> MC
    MC -- 2. Cache Lookup --> CACHE
    CACHE -. miss .-> MC
    MC -. 3. Authoritative Read .-> AUTH_PORT
    AUTH_PORT --> STORE
    AUTH_PORT -. 4. Return Truth .-> MC
    MC -. 5. Converge Cache .-> CACHE
    MC --> APP

    %% Write Flow
    APP -- 1. create / update(key, val) --> MC
    MC -- 2. Execute Governed Write --> GW
    GW -- 3. Authorize --> THM
    GW -- 4. Consume Token --> COORD
    COORD <--> CACHE
    GW -- 5. Commit Atomic CAS --> AUTH_PORT
    AUTH_PORT --> STORE
    GW -- 6. Post-Commit Converge --> CONV
    CONV --> CACHE
    GW --> MC
    MC --> APP

    %% Search Flow
    APP -- 1. search(query) --> MC
    MC -- 2. Delegate Search --> SEARCH_PORT
    SEARCH_PORT --> STORE
    SEARCH_PORT --> MC
    MC --> APP
```

**Point READ Protocol (Cache-Aside + Read-Repair)**
1. Application invokes `mneme.get(ResourceKey key, Class<T> clazz)`.
2. `Mneme` checks active cache in Infinispan.
3. **Cache Hit**: Validates entry freshness and active token; returns cached representation in `GovernedRead<T>`.
4. **Cache Miss / Invalidation**: Fall-through to `AuthoritativePersistencePort.read(key)`.
5. If resource exists in Mnemosyne:
    - Returns authoritative state.
    - Synchronously or asynchronously converges active cache via `ActiveStateConvergencePort` (non-destructive read-repair).
    - Strips internal management provenance (`BL-09-01`) before returning domain representation to caller.
6. If resource is absent: returns empty `Optional`.

**Governed CREATE Protocol**
1. Application invokes `mneme.create(ResourceKey key, T resource, ThemisSecurityContext securityContext)`.
2. Evaluates Themis authorization (`ThemisAction.CREATE`).
3. Invokes `AuthoritativePersistencePort.create(key, resource)`.
    - Mnemosyne atomically verifies unique existence and commits record with authoritative version 1.
4. On successful commit: invokes `ActiveStateConvergencePort.converge()` to seed active cache with version 1.
5. Returns domain `WriteResult<T>`.
6. Presentation layer (`iris-befe` / `pylai`) maps `WriteResult.committed` to HTTP `201 Created` with corresponding `Location` and `ETag`.

**Governed UPDATE Protocol**
1. Application performs a point read, obtaining resource and its `GovernedRead<T>` observation context (holding `ActiveStateToken` and `ExpectedAuthoritativeVersion`).
2. Application modifies resource and invokes `mneme.update(GovernedRead<T> context, T proposed, ThemisSecurityContext securityContext)`.
3. Evaluates Themis authorization (`ThemisAction.UPDATE`).
4. Coordinates active state: `ActiveStateCoordinator.consume(key, observedToken)`. If token is stale, rejects with `WriteResult.activeStateConflict`.
5. Executes authoritative persistence: `AuthoritativePersistencePort.update(key, proposed, expectedVersion)`.
    - Mnemosyne verifies predecessor version matches in durable store.
    - Atomically increments authoritative version and commits new state.
6. On successful commit: converges active cache via `ActiveStateConvergencePort` (CAS loop with newer-version protection).
7. Returns domain `WriteResult<T>`.
8. Presentation layer maps `WriteResult.committed` to HTTP `200 OK` with updated `ETag`.

**Lifecycle Transition (ADR-020 Lifecycle Governance)**
1. Governed resources are never physically deleted via application CRUD.
2. Lifecycle transitions (e.g. `status = inactive`, `entered-in-error`, `de-registered`) are modeled as governed authoritative `UPDATE` operations using the domain lifecycle semantics appropriate to that specific information type.
3. Application/presentation endpoints reject HTTP `DELETE` operations where physical deletion is unsupported, returning HTTP `405 Method Not Allowed` with structured `OperationOutcome` explaining ADR-020 lifecycle governance.

**Authoritative SEARCH Protocol Boundary**
1. Application invokes `mneme.search(Class<T> type, SearchQuery query)`.
2. `Mneme` delegates directly to `AuthoritativeSearchPort` in Mnemosyne.
3. Mnemosyne executes the query against native database search/index machinery.
4. Results are returned to `Mneme` as domain result sets and delivered to the caller.
5. Cluster-wide `remoteCache.values()` scans are strictly prohibited as search semantics.

**Kleio Audit Query Boundary (`Iris -> ? -> Kleio`)**
1. Presentation queries for audit events (e.g. `AuditEventResource` in `iris-befe`) require decoupled read access to `AuditService`.
2. Kleio audit records are append-only evidence and are kept distinct from Mnemosyne clinical persistence and Mneme active cache.
3. The cross-runtime query boundary shall be established via a dedicated service port or read-only service endpoint rather than embedding direct `@DataSourceDefinition` JDBC pools in the presentation container.

---

**7. Mechanical Enforcement Strategy**

To satisfy Axiom **AX-12** (*"Hide Plumbing, Not Information"*) and Section 5 (*"Make the safe thing the easy thing, and make the unsafe thing difficult or impossible"*):

1. **API Boundary Encapsulation**:
    - `iris-befe` and application modules shall ONLY depend on `calliope` (contracts) and Mneme application client contracts.
    - Raw `infi

**Requirements**

**Overview & Goals**  
This assessment delivers a bounded, read-only architectural and implementation evaluation to establish the intended remediation approach for **MAT-01**, **MAT-02**, and **MAT-05** from the *Harmonia Architectural Axiom Conformance Assessment*.

The primary objectives are:
1. **Restore State Separation (AX-05)**: Establish a mechanically enforced application-facing **Mneme** managed-information boundary for active use, while preserving **Mnemosyne** as the sole establisher of authoritative durable state.
2. **Eliminate Raw Active Cache Mutation (MAT-01)**: Prevent application and presentation tiers from mutating Infinispan cache memory directly, performing uncoordinated writes, or inventing local version progressions.
3. **Establish Authoritative Search Boundary (MAT-02, ADR-010)**: Prohibit cluster-wide `remoteCache.values()` memory scans, define a type-neutral authoritative search contract, and isolate concrete search implementation behind the unresolved **MAT-06** boundary.
4. **Decouple Presentation from Persistence (MAT-05, Invariant 3)**: Remove direct PostgreSQL/JDBC/JPA coupling and `@DataSourceDefinition` bindings from `iris-befe` for both clinical and audit data without conflating Kleio audit evidence with Mnemosyne clinical persistence.
5. **Preserve Governed Information Lifecycle (ADR-020)**: Model record retirement as governed authoritative `UPDATE` operations using lifecycle semantics appropriate to each information type, rejecting unsupported HTTP `DELETE` operations at presentation boundaries.
6. **Strictly Isolate MAT-06**: Keep all immediate remediation designs completely agnostic to whether Mnemosyne Clinical uses custom relational JPA persistence or HAPI FHIR JPA Server.

---

**Scope**  
**In Scope**
- Comprehensive architectural assessment of the current runtime topology and execution containers (`iris-befe`, `infinispan-cluster`, `mnemosyne-clinical`, `pylai-fhir-registry`).
- Tracing and diagnosing all 8 concrete read, create, update, delete, search, and audit access paths across the repository.
- Root-cause reframing identifying the absence of a mechanically enforced Mneme managed-information boundary.
- Technical evaluation of previous Task 08/09/10 artefacts (retain, rework, supersede, or block).
- Target logical access models for point `READ`, `CREATE`, `UPDATE`, lifecycle transitions, `SEARCH`, and `Kleio` audit queries.
- Clear separation between application/domain contracts (e.g. `WriteResult`, `GovernedRead`) and presentation HTTP status mappings (`201`, `200`, `404`, `405`, `409`, `OperationOutcome`).
- Refined 5-goal implementation sequence with clear decoupling of Goal 3A (search contract/port) from Goal 3B (concrete search implementation blocked by MAT-06) and separate resolution of Goal 4 (Iris -> Kleio audit boundary).
- Mechanical enforcement strategy using ArchUnit and fail-closed platform APIs.

**Out of Scope**
- Implementing code modifications or making git commits (strictly a planning and architecture assessment).
- Resolving **MAT-06** (custom relational persistence vs. HAPI FHIR JPA Server).
- Implementing Goal 3B concrete authoritative search prior to MAT-06 resolution.
- Creating new distributed network protocols or microservices.
- Conflating Kleio audit evidence with Mneme/Mnemosyne clinical information.

---

**Architectural Authority & Axiom Governance**  
All analyses adhere strictly to the Harmonia Architectural Authority Hierarchy:
1. `docs/architectural-axioms.md` (Highest authority)
    - **AX-01**: Health-Information Centricity.
    - **AX-02**: Standards at the boundary; Harmonia within the boundary.
    - **AX-04**: Use the machinery; own the semantics.
    - **AX-05**: Active state and authoritative durable state are distinct (*"Mneme manages active use; Mnemosyne establishes durable truth"*).
    - **AX-06**: Explicit information authority.
    - **AX-07**: Security is intrinsic to managed operations (Themis default-deny).
    - **AX-10**: Resilience through visible failure (no silent in-memory fallback).
    - **AX-11**: Responsive and highly available managed information access.
    - **AX-12**: Hide plumbing, not information.
    - **AX-13**: Harmonia management has an explicit boundary (fail-closed egress projection).
2. `../../AGENTS-old2.md` (Repository Invariants 1 through 10, particularly Invariant 3 [Iris Presentation Decoupling] and Invariant 8 [Mneme/Mnemosyne State Separation]).
3. Accepted Architecture Decisions (`ADR-003`, `ADR-006`, `ADR-010`, `ADR-013`, `ADR-018`, `ADR-019`, `ADR-020`).

**Technical Design**

**1. Current Runtime Topology**

The Harmonia platform executes across distinct runtime processes and communication protocols:

| Runtime Process / Container | Framework / Host | Network / Ports | Core Subsystems Hosted | Outbound Communication Mechanisms |
| :--- | :--- | :--- | :--- | :--- |
| **`iris-befe`** (`harmonia-befe`) | WildFly Jakarta EE 10 | HTTP `8080` (Clinical), `8090` (Ops), `9990` (Admin) | Iris BEFE REST resources, CORS filters, Themis security filters | • Hot Rod TCP (`11222`) to `infinispan-1`<br/>• Direct JDBC (`5432`) to PostgreSQL `fhir_node_1` (MAT-05 violation) |
| **`mneme-cluster`** (`infinispan-1`, `infinispan-2`) | Infinispan 15 Standalone | Hot Rod TCP `11222`, JGroups `7800` | Clustered active caches, `active-coordination-cache`, `FhirRestCacheStore` | • HTTP Client (`8080`) to `mnemosyne-clinical` via `HapiFhirRestClient` (read-through / write-behind SPI) |
| **`mnemosyne-clinical`** (`hapi-fhir-1`, `hapi-fhir-2`) | Spring Boot 3 + HAPI FHIR | HTTP `8080` (internal) / `8081` (external host) | HAPI FHIR `JpaRestfulServer`, `FhirStorageService`, `AuthoritativePersistenceService`, `DefaultGovernedWriter` | • JDBC (`5432`) to PostgreSQL `fhir_node_1` |
| **`mnemosyne-operations`** (`operations-1`, `operations-2`) | Spring Boot 3 | HTTP `8080` (internal) / `8085` (external host) | Operations REST APIs, Task Sequence state persistence | • JDBC (`5432`) to PostgreSQL `ops_node_1` |
| **`petasos`** (`harmonia-petasos`) | ActiveMQ Artemis 2.33 | OpenWire TCP `61616` | Petasos resilient message queues | Artemis clustering protocols |
| **`energeia-ponos`** (`task-processor`) | WildFly + Apache Camel | HTTP `8083` | Ponos WorkEngine, Praxis workflow orchestration, Erga activities | • OpenWire TCP (`61616`) to Petasos<br/>• Hot Rod TCP (`11222`) to Infinispan |
| **`pylai-fhir-registry`** | Spring Boot 3 | HTTP `8082` | Inbound FHIR REST gateway, `ChangeRequestSubmissionService` | • OpenWire TCP (`61616`) to Petasos<br/>• In-process / HTTP to `FhirStorageService` |

---

**2. Current Access Paths & Bypasses**

```
1. Iris/BEFE -> READ Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.read(id)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.getResourceJson -> remoteCache.get(id)
     -> Authoritative-State: [BYPASSED if present in cache] / CacheStore HTTP GET to Mnemosyne on miss
     -> Durable Store: PostgreSQL fhir_node_1 (via CacheStore read-through fallback only)
   [STATUS]: DEFECTIVE (Treats cache as primary record; no active-state token or provenance boundary)

2. Iris/BEFE -> CREATE Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.create(payload)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.putResourceJson -> remoteCache.put(id, payload)
     -> Authoritative-State: [ABSENT / BYPASSED] (GovernedWriter / AuthoritativePersistencePort ignored)
     -> Durable Store: [BYPASSED] (Volatile cache only; uncoordinated write-behind may race)
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-01)

3. Iris/BEFE -> UPDATE Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.update(id, payload)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.saveResource -> sets local versionId=ver+1 -> remoteCache.put(id)
     -> Authoritative-State: [ABSENT / BYPASSED] (ActiveStateCoordinator / CAS persistence ignored)
     -> Durable Store: [BYPASSED]
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-01, local version progression)

4. Iris/BEFE -> Lifecycle / Deletion
   Caller: Iris SPA
     -> Application API: PractitionerResource.delete(id)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.deleteResource -> remoteCache.remove(id)
     -> Authoritative-State: [ABSENT / BYPASSED]
     -> Durable Store: [BYPASSED / Physical delete if cache store syncs]
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-01, ADR-020 violation: physical delete exposed)

5. Iris/BEFE -> SEARCH Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.search(id, name, identifier)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.searchResources -> remoteCache.values() full scan into heap
     -> Authoritative-State: [ABSENT / BYPASSED] (Mnemosyne / HAPI search indexing bypassed)
     -> Durable Store: [BYPASSED]
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-02, ADR-010 violation)

6. Pylai -> READ Practitioner
   Caller: External FHIR Client
     -> Application API: FhirRestGatewayController.readResource(type, id)
     -> Module: pylai-fhir-registry
     -> Runtime Boundary: In-process Spring bean / HTTP
     -> Active-State: [BYPASSED] (Does not check Mneme active cache)
     -> Authoritative-State: FhirStorageService.getResource -> FhirResourceRepository.findByResourceTypeAndFhirId
     -> Durable Store: PostgreSQL hie_fhir_resources
     -> Egress: PylaiFhirPublicationProjector.projectForPublication
   [STATUS]: AUTHORITATIVE (Bypasses Mneme cache-aside acceleration)

7. Pylai -> SEARCH Practitioner
   Caller: External FHIR Client
     -> Application API: FhirRestGatewayController.searchResources(type, allParams)
     -> Module: pylai-fhir-registry
     -> Runtime Boundary: In-process Spring bean / HTTP
     -> Active-State: [BYPASSED]
     -> Authoritative-State: FhirStorageService.searchResources -> repository.findByResourceType + in-memory filtering
     -> Durable Store: PostgreSQL hie_fhir_resources
     -> Egress: PylaiFhirPublicationProjector.projectForPublication
   [STATUS]: AUTHORITATIVE (Subject to persistence query capabilities)

8. Iris/BEFE -> Direct Kleio Audit Persistence (MAT-05)
   Caller: Iris Console / Admin SPA
     -> Application API: AuditEventResource.search() / read(id)
     -> Module: iris-befe
     -> Runtime Boundary: Direct JDBC (:5432) via AuditDataSourceProducer
     -> Direct Coupling: @DataSourceDefinition binds KleioAuditDS directly to postgres-1
     -> Execution: DurableAuditService / JdbcAppendOnlyAuditEventRepository executes SQL queries in BEFE
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-05, Invariant 3 violation)
```

---

**3. MAT-01 / MAT-02 / MAT-05 Root-Cause Assessment**

**Reframed Root Cause**  
The architectural root cause is:
> **"There is no mechanically enforced application-facing Mneme managed-information boundary, causing callers to receive raw infrastructure capabilities and bypass Harmonia information-management semantics."**

The defect is not merely the absence of a class named `MnemeClient`. Rather, because `iris-befe` was provided with raw infrastructure primitives (`RemoteCacheManager` Hot Rod client and container `@DataSourceDefinition` JDBC pools) rather than a governed application boundary:
1. Application endpoints defaulted to treating Infinispan `RemoteCache` as a key-value database for point CRUD, inventing uncoordinated writes and local version progressions (`MAT-01`).
2. Search was implemented by naively pulling all cached strings via `remoteCache.values()` and filtering them in JVM memory (`MAT-02`).
3. Presentation queries for audit events were wired directly to PostgreSQL JDBC repositories because no service-level query port existed (`MAT-05`).

**Analysis of Existing Mneme APIs vs. Application Facade Boundary**  
Inspection of existing public interfaces reveals:
- `calliope/model/governedwrite/GovernedWriter.java`: Exposes pure write contracts (`create` and `update`) with `ResourceKey`, `GovernedRead`, `WriteResult`, and `ThemisSecurityContext`.
- `calliope/model/governedwrite/ActiveStateCoordinator.java` & `ActiveStateConvergencePort.java`: Internal active-state coordination and post-commit cache convergence SPIs.
- `hestia/mnemosyne-clinical`: Contains `DefaultGovernedWriter` orchestrating writes across Themis, active coordination, authoritative persistence, and convergence.

**Why an Evolved Application-Facing Boundary Is Required**:  
While `GovernedWriter` provides a sound contract for write operations, application callers require a cohesive, unified managed-information access boundary that provides:
1. **Point READ**: Cache-aside resolution with authoritative fallback on cache miss and active read-repair (`get(ResourceKey)` returning `GovernedRead<T>`).
2. **Governed WRITE**: `create` and `update` delegating through the governed write pipeline.
3. **Governed SEARCH**: Explicit search delegation port prohibiting in-memory cache scans.
   Whether this boundary is realized as an expanded `MnemeAccessPort` / `MnemeClient` or an evolution of existing contracts, it must encapsulate raw `RemoteCache` access and mechanically enforce Harmonia governance.

**Managed-Type Classification Inspection**  
Inspection of Calliope and Mneme shows:
- `calliope/model/registry/ProviderRegistryConstants.java` defines `SUPPORTED_RESOURCE_TYPES` (`Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, `Group`) and validation rules.
- Calliope canonical schema definitions represent the semantic source of truth for Harmonia domain types.
- **Architectural Decision**: Semantic classification (`information type -> managed/governed semantics`) must have **one authoritative owner** (Calliope). Mneme consumes this semantic classification to manage physical cache and runtime configurations. A separate, parallel `ManagedTypeRegistry` must NOT be created before semantic ownership is formally decided.

**Kleio Audit Query Boundary Inspection**
- `kleio-core` defines `AuditService`, `AuditQuery`, and `HarmoniaAuditEvent`.
- `kleio-persistence` provides `DurableAuditService` and `JdbcAppendOnlyAuditEventRepository`.
- `kleio-fhir` provides `HarmoniaAuditEventMapper`.
- **Architectural Boundary**: Kleio audit evidence represents immutable security/audit history and is **not** Mnemosyne-managed clinical domain information. Remediating MAT-05 in `iris-befe` must **not** route audit queries through Mneme by default. Instead, the application-facing Kleio query boundary (`Iris -> ? -> Kleio`) is treated as a separate architectural problem to be resolved without direct JDBC bindings.

---

**4. Existing Machinery That Can Be Reused**

The repository already contains high-quality, production-ready building blocks:
1. **Governed Write Pipeline (`hestia/mnemosyne-clinical` & `calliope`)**:
    - `DefaultGovernedWriter`: Full orchestration of Themis security -> `ActiveStateCoordinator` -> `AuthoritativePersistencePort` -> `ActiveStateConvergencePort`.
    - `AuthoritativePersistenceService`: Atomic CREATE uniqueness and conditional UPDATE with expected predecessor version checking (`TransactionTemplate`).
    - Sealed result types: `WriteResult`, `AuthoritativePersistenceResult`, `ConvergenceStatus`.
2. **Active State Coordination & Convergence (`hestia/mneme-cluster`)**:
    - `HotRodActiveStateCoordinator`: Lightweight token observation and atomic consumption over `active-coordination-cache` with fail-fast visible failure.
    - `HotRodMnemeConvergence`: Bounded CAS loop post-commit cache convergence with newer-version protection.
3. **Publication & Security Boundary (`pylai-fhir-registry` & `calliope`)**:
    - `PylaiFhirPublicationProjector`: Non-destructive fail-closed egress sanitization stripping internal operational metadata.
    - `FhirSecurityTagManager` & `ThemisSecurityContext`: Explicit distinction between persistent clinical confidentiality labels and transient operational security context.
4. **Kleio Audit Infrastructure (`kleio`)**:
    - `AuditService`, `AuditQuery`, and `HarmoniaAuditEventMapper`: Standardized interfaces for querying and mapping immutable audit events.
5. **Resilient Transport (`petasos` & `energeia-ponos`)**:
    - ActiveMQ Artemis queues and Camel `Pragma` task sequence pipelines for asynchronous governed change workflows.

---

**5. Previous Task 08/09/10 Assessment**

| Task / Artefact | Repository Location | Assessment | Detailed Rationale & Action |
| :--- | :--- | :--- | :--- |
| **Task 08: Governed Write Contracts** | `calliope/model/governedwrite/*` | **RETAIN** | Perfectly aligned with AX-05, AX-07, and AX-10. Retain pure contracts (`GovernedWriter`, `ResourceKey`, `ActiveStateToken`, `ExpectedAuthoritativeVersion`, `WriteResult`). |
| **Task 08: Active-State Coordinator** | `hestia/mneme-cluster/coordination/*` | **RETAIN** | Production Hot Rod coordinator over `active-coordination-cache`. Retain as internal engine component. |
| **Task 08: Active-State Convergence** | `hestia/mneme-cluster/convergence/*` | **RETAIN** | Production Hot Rod CAS convergence port with version protection. Retain as internal engine component. |
| **Task 08: Authoritative Persistence** | `hestia/mnemosyne-clinical/persistence/*` | **RETAIN (LOGICAL)** | Atomic persistence logic and result models are valid. Retain interface contracts; persistence implementation remains subject to MAT-06 abstraction. |
| **Task 08: BEFE Write Migration (08.05A)** | `.junie/plans/migrate-befe-clinical-writes.md` | **REWORK** | Target concept of unified Mneme access boundary is correct; re-align with revised Goal 2. |
| **Task 09: Cache-Aside Point Reads** | `docs/backlog/Harmonia - Task 9 - Backlog.md` | **REWORK / INTEGRATE** | Cache-aside read on miss + read-repair is essential for AX-05/AX-11. Integrate into Goal 3A while strictly isolating internal version provenance (`BL-09-01`). |
| **Task 10: Governed Lifecycle (ADR-020)** | `docs/architecture-decisions.md` (ADR-020) | **RETAIN** | Prohibits physical deletion. Model retirement as authoritative `UPDATE`; reject HTTP `DELETE` at presentation boundary with `405 Method Not Allowed`. |
| **Legacy Infinispan Cache Store SPI** | `hestia/mneme-persistence/*` | **SUPERSEDE** | Uncoordinated background HTTP store (`FhirRestCacheStore`) causes race conditions and hides persistence failure. Supersede with explicit synchronous cache-aside and governed write convergence. |

---

**6. Target Logical Access Model**

```mermaid
graph LR
    subgraph Client Tier
        APP[Application / Iris BEFE]
    end

    subgraph Mneme Boundary
        MC[Mneme Access Boundary / Facade]
        COORD[ActiveStateCoordinator]
        CONV[ActiveStateConvergencePort]
    end

    subgraph Governance & Security
        THM[Themis Policy Engine]
    end

    subgraph Mnemosyne Authoritative Tier
        GW[GovernedWriter]
        AUTH_PORT[AuthoritativePersistencePort]
        SEARCH_PORT[AuthoritativeSearchPort]
        STORE[(Authoritative Store / Postgres)]
    end

    subgraph Active Cache
        CACHE[(Infinispan Cluster)]
    end

    %% Read Flow
    APP -- 1. get(key) --> MC
    MC -- 2. Cache Lookup --> CACHE
    CACHE -. miss .-> MC
    MC -. 3. Authoritative Read .-> AUTH_PORT
    AUTH_PORT --> STORE
    AUTH_PORT -. 4. Return Truth .-> MC
    MC -. 5. Converge Cache .-> CACHE
    MC --> APP

    %% Write Flow
    APP -- 1. create / update(key, val) --> MC
    MC -- 2. Execute Governed Write --> GW
    GW -- 3. Authorize --> THM
    GW -- 4. Consume Token --> COORD
    COORD <--> CACHE
    GW -- 5. Commit Atomic CAS --> AUTH_PORT
    AUTH_PORT --> STORE
    GW -- 6. Post-Commit Converge --> CONV
    CONV --> CACHE
    GW --> MC
    MC --> APP

    %% Search Flow
    APP -- 1. search(query) --> MC
    MC -- 2. Delegate Search --> SEARCH_PORT
    SEARCH_PORT --> STORE
    SEARCH_PORT --> MC
    MC --> APP
```

**Point READ Protocol (Cache-Aside + Read-Repair)**
1. Application invokes `mneme.get(ResourceKey key, Class<T> clazz)`.
2. `Mneme` checks active cache in Infinispan.
3. **Cache Hit**: Validates entry freshness and active token; returns cached representation in `GovernedRead<T>`.
4. **Cache Miss / Invalidation**: Fall-through to `AuthoritativePersistencePort.read(key)`.
5. If resource exists in Mnemosyne:
    - Returns authoritative state.
    - Synchronously or asynchronously converges active cache via `ActiveStateConvergencePort` (non-destructive read-repair).
    - Strips internal management provenance (`BL-09-01`) before returning domain representation to caller.
6. If resource is absent: returns empty `Optional`.

**Governed CREATE Protocol**
1. Application invokes `mneme.create(ResourceKey key, T resource, ThemisSecurityContext securityContext)`.
2. Evaluates Themis authorization (`ThemisAction.CREATE`).
3. Invokes `AuthoritativePersistencePort.create(key, resource)`.
    - Mnemosyne atomically verifies unique existence and commits record with authoritative version 1.
4. On successful commit: invokes `ActiveStateConvergencePort.converge()` to seed active cache with version 1.
5. Returns domain `WriteResult<T>`.
6. Presentation layer (`iris-befe` / `pylai`) maps `WriteResult.committed` to HTTP `201 Created` with corresponding `Location` and `ETag`.

**Governed UPDATE Protocol**
1. Application performs a point read, obtaining resource and its `GovernedRead<T>` observation context (holding `ActiveStateToken` and `ExpectedAuthoritativeVersion`).
2. Application modifies resource and invokes `mneme.update(GovernedRead<T> context, T proposed, ThemisSecurityContext securityContext)`.
3. Evaluates Themis authorization (`ThemisAction.UPDATE`).
4. Coordinates active state: `ActiveStateCoordinator.consume(key, observedToken)`. If token is stale, rejects with `WriteResult.activeStateConflict`.
5. Executes authoritative persistence: `AuthoritativePersistencePort.update(key, proposed, expectedVersion)`.
    - Mnemosyne verifies predecessor version matches in durable store.
    - Atomically increments authoritative version and commits new state.
6. On successful commit: converges active cache via `ActiveStateConvergencePort` (CAS loop with newer-version protection).
7. Returns domain `WriteResult<T>`.
8. Presentation layer maps `WriteResult.committed` to HTTP `200 OK` with updated `ETag`.

**Lifecycle Transition (ADR-020 Lifecycle Governance)**
1. Governed resources are never physically deleted via application CRUD.
2. Lifecycle transitions (e.g. `status = inactive`, `entered-in-error`, `de-registered`) are modeled as governed authoritative `UPDATE` operations using the domain lifecycle semantics appropriate to that specific information type.
3. Application/presentation endpoints reject HTTP `DELETE` operations where physical deletion is unsupported, returning HTTP `405 Method Not Allowed` with structured `OperationOutcome` explaining ADR-020 lifecycle governance.

**Authoritative SEARCH Protocol Boundary**
1. Application invokes `mneme.search(Class<T> type, SearchQuery query)`.
2. `Mneme` delegates directly to `AuthoritativeSearchPort` in Mnemosyne.
3. Mnemosyne executes the query against native database search/index machinery.
4. Results are returned to `Mneme` as domain result sets and delivered to the caller.
5. Cluster-wide `remoteCache.values()` scans are strictly prohibited as search semantics.

**Kleio Audit Query Boundary (`Iris -> ? -> Kleio`)**
1. Presentation queries for audit events (e.g. `AuditEventResource` in `iris-befe`) require decoupled read access to `AuditService`.
2. Kleio audit records are append-only evidence and are kept distinct from Mnemosyne clinical persistence and Mneme active cache.
3. The cross-runtime query boundary shall be established via a dedicated service port or read-only service endpoint rather than embedding direct `@DataSourceDefinition` JDBC pools in the presentation container.

---

**7. Mechanical Enforcement Strategy**

To satisfy Axiom **AX-12** (*"Hide Plumbing, Not Information"*) and Section 5 (*"Make the safe thing the easy thing, and make the unsafe thing difficult or impossible"*):

1. **API Boundary Encapsulation**:
    - `iris-befe` and application modules shall ONLY depend on `calliope` (contracts) and Mneme application client contracts.
    - Raw `infinispan-client-hotrod` and `RemoteCache` access shall NOT be exported to presentation or application web packages.
2. **ArchUnit Automated Architecture Rules (`paradeigma-test`)**:
    - `IrisDecouplingArchitectureTest`: Asserts zero imports of `jakarta.persistence.*`, `org.hibernate.*`, `org.postgresql.*`, `ca.uhn.fhir.jpa.*`, and zero `@DataSourceDefinition` annotations in `iris-befe`.
    - `MnemeStateSeparationArchitectureTest`: Asserts that application and presentation tiers never invoke `RemoteCache.put()`, `RemoteCache.remove()`, or `RemoteCache.values()`.
    - `GovernedWriteContractArchitectureTest`: Asserts that `AuthoritativePersistencePort` and `GovernedWriter` contain zero physical `delete()` methods.
    - `AuditPersistenceDecouplingArchitectureTest`: Asserts that presentation modules do not instantiate direct database connections for audit logging.
3. **Fail-Closed Type Safety**:
    - Unmanaged or uncoordinated resource writes fail closed with an explicit typed failure, preventing silent pass-through to uncoordinated cache.

---

**8. MAT-06 Dependency Isolation**

**MAT-06 Boundary**: Whether Mnemosyne Clinical uses custom relational FHIR JPA persistence or HAPI FHIR JPA Server.

**Independent Remediation (Proceeds in Goals 1, 2, 3A, 4, 5)**
- Evolving/establishing the Mneme application-facing managed-information contracts (`GovernedWriter`, `GovernedRead`, `ResourceKey`, `AuthoritativeSearchPort`).
- Migration of `iris-befe` `POST / CREATE` and `PUT / UPDATE` to the Mneme boundary.
- Elimination of raw `RemoteCache.put()` and local version progression.
- Enforcement of ADR-020 lifecycle transitions and HTTP `405 Method Not Allowed` for `DELETE`.
- Implementation of cache-aside point READ with read-repair in Mneme.
- Definition of `AuthoritativeSearchPort` interface and prohibition of `remoteCache.values()` as search semantics.
- Decoupling of `iris-befe` from direct PostgreSQL/Kleio JDBC datasources (MAT-05).
- ArchUnit automated architectural enforcement tests.

**Blocked on MAT-06 (Goal 3B - Must Stop at Interface Boundary)**
- Concrete implementation of `AuthoritativeSearchPort` (custom SQL/JPA queries vs. HAPI `IFhirResourceDao.search()`).
- Decommissioning of legacy search fallback before native authoritative search is online.
- Mnemosyne internal database schema and HAPI JPA server configuration.

**Implementation Sequence**

**1. Revised Bounded Implementation Sequence**

The remediation is decomposed into five compact, sequentially executable implementation goals with an explicit pause boundary for MAT-06:

```
[Goal 1: Establish/Evolve Mneme Application-Facing Managed-Information Contract]
                                      │
                                      ▼
[Goal 2: Migrate Governed Writes & Enforce ADR-020 Lifecycle in Presentation Tier]
                                      │
                                      ▼
[Goal 3A: Implement Cache-Aside Point READ & Establish Authoritative Search Port]
                                      │
                                      ▼
                    ═════════════════════════════════════
                    🛑 STOP FOR MAT-06 ARCHITECTURE DECISION
                    ═════════════════════════════════════
                                      │
                                      ▼
[Goal 3B: Implement Concrete Authoritative Search & Decommission Cache Scans]
                                      │
                                      ▼
[Goal 4: Resolve Iris -> Kleio Query Coupling (MAT-05 Presentation DB Decoupling)]
                                      │
                                      ▼
[Goal 5: Add & Strengthen Mechanical ArchUnit Architectural Invariant Tests]
```

**Goal 1: Establish/Evolve Mneme Application-Facing Managed-Information Contract**
- **Findings Addressed**: Foundation for MAT-01, MAT-02.
- **Architectural Invariant Established**: Single application-facing managed-information access boundary (`AX-05`); semantic type authority in Calliope (`AX-06`).
- **Modules Affected**: `calliope`, `hestia/mneme-cluster`.
- **Prerequisites**: Existing Task 08 contracts in `calliope/model/governedwrite`.
- **Scope & Deliverables**:
    - Inspect existing public Mneme APIs and determine whether existing contracts (`GovernedWriter`, `GovernedRead`, `ResourceKey`) can be evolved or encapsulated into a unified application-facing access boundary.
    - Inspect Calliope canonical model and registry mechanisms (`ProviderRegistryConstants.SUPPORTED_RESOURCE_TYPES`) to reuse existing semantic type classifications rather than inventing a duplicate `ManagedTypeRegistry`.
    - Formalize point read contract (`GovernedRead<T>`), write contract, and search contract interface boundaries.
    - Keep domain contracts strictly decoupled from presentation HTTP semantics (`200`, `201`, `404`, `405`, `409`, `OperationOutcome`).
- **Stop Condition**: Pure unit tests in `calliope` and `mneme-cluster` verify contracts and type validation without modifying the presentation tier.

**Goal 2: Migrate Governed Writes and Enforce ADR-020 Lifecycle in Presentation Tier**
- **Findings Addressed**: **MAT-01** (Iris BEFE Raw Cache Mutation & Local Version Progression).
- **Architectural Invariant Established**: Active vs. Durable State Separation (`AX-05`); Lifecycle Governance / No Physical Deletion (`ADR-020`).
- **Modules Affected**: `iris/iris-befe`, `calliope`, `hestia/mnemosyne-clinical`.
- **Prerequisites**: Goal 1.
- **Scope & Deliverables**:
    - Migrate `PractitionerResource` and other BEFE clinical endpoints to use the Mneme governed write boundary for `POST` (create) and `PUT` (update).
    - Remove `remoteCache.put()` and local `versionId` increments from `FhirCacheService`.
    - Remove `remoteCache.remove()` and update BEFE `@DELETE` methods to return `405 Method Not Allowed` with structured `OperationOutcome` explaining ADR-020.
    - Retain existing Task 08 coordination and convergence machinery (`HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`, `DefaultGovernedWriter`).
- **Stop Condition**: BEFE `POST` and `PUT` execute authoritatively through Mnemosyne and converge Infinispan; `DELETE` returns HTTP 405; raw `remoteCache.put` is removed from BEFE write paths.

**Goal 3A: Implement Cache-Aside Point READ and Establish Authoritative Search Port Boundary**
- **Findings Addressed**: **MAT-02** (Foundation), **BL-09-01** (Cache-Aside Read & Provenance Isolation).
- **Architectural Invariant Established**: Authoritative Search Boundary (`ADR-010`); Non-Destructive Active Cache Acceleration (`AX-05`, `AX-11`).
- **Modules Affected**: `hestia/mneme-cluster`, `calliope`, `iris/iris-befe`.
- **Prerequisites**: Goal 1 & Goal 2.
- **Scope & Deliverables**:
    - Implement cache-aside point READ logic in Mneme: check active cache -> on miss, fall-through to `AuthoritativePersistencePort.read(key)` -> execute active read-repair via `ActiveStateConvergencePort`.
    - Isolate internal management provenance metadata (`BL-09-01`) before returning domain representations to callers.
    - Define `AuthoritativeSearchPort` interface in `calliope` representing type-neutral authoritative search queries.
    - Explicitly prohibit `remoteCache.values()` as authoritative search semantics in architectural rules.
    - **Explicit Stop Boundary**: STOP before Goal 3B pending the resolution of MAT-06.
- **Stop Condition**: Point `READ` transparently populates and repairs cold cache; `AuthoritativeSearchPort` contract is defined; execution pauses for MAT-06 decision.

**Goal 3B: Implement Concrete Authoritative Search and Decommission Cache Scans (Post MAT-06)**
- **Findings Addressed**: **MAT-02** (Authoritative Search Completion).
- **Architectural Invariant Established**: Authoritative Native Index Search (`AX-04`, `ADR-010`).
- **Modules Affected**: `hestia/mnemosyne-clinical`, `hestia/mneme-cluster`, `iris/iris-befe`.
- **Prerequisites**: Goal 3A AND formal resolution of **MAT-06** (custom relational JPA vs. HAPI FHIR JPA Server).
- **Scope & Deliverables**:
    - Implement `AuthoritativeSearchPort` using the selected native Mnemosyne search engine.
    - Wire Mneme search delegation to Mnemosyne's native search implementation.
    - Completely decommission and delete `remoteCache.values()` scanning logic from `FhirCacheService`.
- **Stop Condition**: Clinical searches execute against native database indexes; `remoteCache.values()` is completely deleted from the codebase.

**Goal 4: Resolve Iris -> Kleio Presentation Query Coupling (MAT-05 Boundary)**
- **Findings Addressed**: **MAT-05** (Presentation Database Coupling for Audit Events).
- **Architectural Invariant Established**: Presentation Decoupling (`Invariant 3`, `AX-12`).
- **Modules Affected**: `iris/iris-befe`, `kleio/kleio-persistence`, `kleio/kleio-core`.
- **Prerequisites**: Goals 1–3A.
- **Scope & Deliverables**:
    - Inspect existing `AuditService` query capabilities and runtime boundaries.
    - Resolve the cross-runtime audit query boundary between `iris-befe` and Kleio without routing through Mneme clinical caches and without inventing ad-hoc network services.
    - Remove `AuditDataSourceProducer.java`, `@DataSourceDefinition`, and direct PostgreSQL/JDBC dependencies from `iris-befe`.
- **Stop Condition**: `AuditEventResource` queries audit events via a decoupled service boundary with zero `@DataSourceDefinition` or direct JDBC drivers in `iris-befe`.

**Goal 5: Add and Strengthen Mechanical ArchUnit Architectural Invariant Tests**
- **Findings Addressed**: **MAT-10** (Mechanical Enforcement of Axioms).
- **Architectural Invariant Established**: Mechanical Enforcement (`AGENTS.md Section 4`, `Section 5`).
- **Modules Affected**: `paradeigma/paradeigma-test`.
- **Prerequisites**: Goals 1–4.
- **Scope & Deliverables**:
    - Add/strengthen ArchUnit tests in `paradeigma-test`:
        1. Asserts zero `RemoteCache.put()`, `RemoteCache.remove()`, or `RemoteCache.values()` calls in presentation/application tiers.
        2. Asserts zero `jakarta.persistence.*`, `org.hibernate.*`, `org.postgresql.*`, or `@DataSourceDefinition` in `iris-befe`.
        3. Asserts zero direct Kleio persistence implementation access from Iris presentation classes.
        4. Asserts zero physical `delete()` methods in governed persistence contracts (`AuthoritativePersistencePort`, `GovernedWriter`).
        5. Asserts all application access to managed information traverses the established Mneme boundary.
- **Stop Condition**: Full reactor architecture test suite (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) passes 100%.

---

**2. Unresolved Architectural Questions**

The following genuine architectural questions remain outside the scope of current authority and require formal decisions:

1. **Audit Query Boundary across JVM Containers (MAT-05 / Kleio)**:
    - *Question*: How should `iris-befe` query durable Kleio audit events when running in a separate WildFly container from PostgreSQL, given that Kleio audit evidence is distinct from Mneme-managed clinical information?
    - *Options*: (a) Expose a dedicated read-only Kleio REST query endpoint; (b) Provide an isolated, strictly read-only audit datasource configuration decoupled from clinical persistence; (c) Project summarized non-PHI audit events to an administrative query port.
2. **Managed-Type Classification Ownership (Calliope vs. Mneme)**:
    - *Question*: Is the canonical definition of which resources/information types are governed owned exclusively by Calliope domain models, or does Mneme maintain an independent classification layer?
    - *Recommendation*: Establish Calliope as the sole semantic source of truth (`ProviderRegistryConstants` / domain descriptors), with Mneme consuming that definition for runtime cache configuration.
3. **MAT-06 Mnemosyne Clinical Persistence Engine Choice**:
    - *Question*: Should Mnemosyne Clinical permanently adopt HAPI FHIR JPA Server as its storage and search engine, or continue evolving Harmonia's custom relational PostgreSQL schema?
    - *Status*: Strictly preserved as unresolved per assessment constraints.

**Testing**

**Validation Approach**  
Verification relies on static architectural analysis, unit contract testing, integration scenario testing, and automated ArchUnit architecture rules.

---

**Key Scenarios & Target Outcomes**

| Scenario ID | Test Target | Precondition | Execution Flow | Expected Verification Outcome |
| :--- | :--- | :--- | :--- | :--- |
| **SC-01** | **Governed CREATE via Mneme Boundary** | Empty active cache, clean DB | `mneme.create(key, Practitioner, secContext)` | • Themis permits operation.<br/>• Mnemosyne commits row to PostgreSQL with version 1.<br/>• Mneme active cache is seeded with version 1.<br/>• Presentation maps to HTTP 201 Created with ETag `W/"1"`. |
| **SC-02** | **Governed UPDATE with Valid Token** | Resource exists at version 1 | Read version 1 -> modify -> `mneme.update(readContext, Practitioner, secContext)` | • Active token consumed.<br/>• Mnemosyne commits version 2 via atomic CAS.<br/>• Mneme active cache converges to version 2.<br/>• Presentation maps to HTTP 200 OK with ETag `W/"2"`. |
| **SC-03** | **Governed UPDATE Stale Version Conflict** | Resource modified concurrently to version 2 | Submit UPDATE with expected version 1 | • Mnemosyne rejects update (0 rows matched).<br/>• Returns `WriteResult.authoritativeConflict`.<br/>• Presentation maps to HTTP 409 Conflict; cache remains uncorrupted. |
| **SC-04** | **Authoritative-Backed Search Contract (Goal 3A)** | Test harness for Mneme search | Invoke `mneme.search(type, query)` | • Search delegates to `AuthoritativeSearchPort`.<br/>• Zero `remoteCache.values()` scans executed.<br/>• MAT-06 boundary respected. |
| **SC-05** | **Cache-Aside Read on Cold Start (Goal 3A)** | Resource in DB; not in cache | `mneme.get(ResourceKey, Practitioner.class)` | • Active cache misses.<br/>• Reads authoritatively from Mnemosyne.<br/>• Converges active cache (read-repair).<br/>• Provenance metadata stripped from payload. |
| **SC-06** | **ADR-020 DELETE Rejection (Goal 2)** | Governed resource exists | HTTP `DELETE /api/fhir/Practitioner/123` | • Endpoint returns HTTP `405 Method Not Allowed`.<br/>• Returns OperationOutcome directing caller to governed lifecycle update.<br/>• Zero physical deletions in cache or database. |
| **SC-07** | **Presentation Tier DB Decoupling (Goal 4 & 5)** | `iris-befe` deployment inspection | Inspect loaded classes and datasources | • Zero `@DataSourceDefinition` in `iris-befe`.<br/>• Zero `org.postgresql` JDBC connections from BEFE.<br/>• ArchUnit test passes. |

---

**Automated Architecture Conformance Tests (ArchUnit)**

The following tests in `paradeigma/paradeigma-test` shall mechanically enforce the boundaries:

1. **`IrisDecouplingArchitectureTest`**:
    - Asserts `iris-befe` contains no classes importing `jakarta.persistence.*`, `org.hibernate.*`, or `org.postgresql.*`.
    - Asserts zero `@DataSourceDefinition` annotations exist within `iris-befe`.
2. **`MnemeStateSeparationArchitectureTest`**:
    - Asserts no presentation resource or controller calls `RemoteCache.values()`, `RemoteCache.put()`, or `RemoteCache.remove()`.
    - Asserts all application access traverses the established Mneme boundary.
3. **`GovernedWriteContractArchitectureTest`**:
    - Asserts `AuthoritativePersistencePort` and `GovernedWriter` expose no physical delete methods.
4. **`KleioPersistenceIsolationArchitectureTest`**:
    - Asserts presentation modules do not import direct Kleio JDBC repository implementations.
5. **`ParadeigmaIsolationArchitectureTest`**:
    - Asserts zero production dependencies on simulation models or test harnesses.

**Delivery Steps**

**Step 1: Establish/Evolve Mneme Application-Facing Contracts and Inspect Classification Mechanisms**  
Establish and formalize the type-neutral application-facing managed-information contracts across Calliope, Mneme, and Mnemosyne without modifying presentation code.

- Inspect existing public Mneme/Calliope contracts (`GovernedWriter`, `GovernedRead`, `ResourceKey`) and evolve or encapsulate them into a unified application-facing access boundary.
- Inspect Calliope canonical model definitions (`ProviderRegistryConstants.SUPPORTED_RESOURCE_TYPES`) to reuse existing semantic type classifications rather than creating a duplicate registry.
- Define pure Calliope contracts for point READ (`GovernedRead`), governed WRITE (`create`, `update`), and SEARCH port boundaries.
- Keep domain contracts strictly decoupled from HTTP presentation semantics (`200`, `201`, `404`, `405`, `409`, `OperationOutcome`).

**Step 2: Migrate Governed Writes and Enforce Lifecycle Semantics in Presentation Tier**  
Migrate Iris BEFE write operations to the established Mneme boundary and enforce ADR-020 lifecycle transitions.

- Migrate `PractitionerResource` and other BEFE clinical endpoints to use the Mneme governed write boundary for `POST` (create) and `PUT` (update).
- Remove `remoteCache.put()` and local `versionId` increments from `FhirCacheService`.
- Remove `remoteCache.remove()` and update BEFE `@DELETE` methods to return `405 Method Not Allowed` with structured `OperationOutcome` explaining ADR-020.
- Retain existing Task 08 coordination and convergence machinery (`HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`, `DefaultGovernedWriter`).

**Step 3: Implement Cache-Aside Point READ and Define Authoritative Search Port Boundary**  
Implement cache-aside point reads with read-repair and define the authoritative search contract while pausing for MAT-06.

- Implement cache-aside point READ logic in Mneme: check active cache -> on miss, fall-through to `AuthoritativePersistencePort.read(key)` -> execute active read-repair via `ActiveStateConvergencePort`.
- Isolate internal management provenance metadata (`BL-09-01`) before returning domain representations to callers.
- Define `AuthoritativeSearchPort` interface in `calliope` and prohibit `remoteCache.values()` as authoritative search semantics.
- Enforce the explicit stop boundary pausing concrete search implementation pending the MAT-06 decision.

**Step 4: Resolve Iris-to-Kleio Query Coupling and Presentation Persistence Decoupling**  
Decouple Iris BEFE from direct PostgreSQL/JDBC audit database connections as a separate MAT-05 boundary.

- Inspect existing `AuditService` query capabilities and runtime boundaries in `kleio-core` and `kleio-persistence`.
- Resolve the cross-runtime audit query boundary between `iris-befe` and Kleio without routing audit evidence through Mneme clinical caches or inventing ad-hoc network services.
- Remove `AuditDataSourceProducer.java`, `@DataSourceDefinition`, and direct PostgreSQL/JDBC dependencies from `iris-befe`.

**Step 5: Implement Authoritative Search Implementation (Post MAT-06) and Comprehensive ArchUnit Enforcement**  
Implement concrete authoritative search after MAT-06 resolution and add/strengthen mechanical ArchUnit architecture tests.

- Implement `AuthoritativeSearchPort` using the selected native Mnemosyne search engine (post MAT-06) and safely delete legacy `remoteCache.values()` scanning logic.
- Add and strengthen ArchUnit tests in `paradeigma-test` asserting: zero `RemoteCache` direct access in presentation resources, zero `DataSourceDefinition` or JDBC imports in Iris, zero direct Kleio persistence implementation access from Iris, zero physical delete methods in governed persistence contracts, and mandatory usage of the Mneme boundary.



**Requirements**

**Overview & Goals**
This assessment delivers a bounded, read-only architectural and implementation evaluation to establish the intended remediation approach for **MAT-01**, **MAT-02**, and **MAT-05** from the *Harmonia Architectural Axiom Conformance Assessment*.

The primary objectives are:
1. **Restore State Separation (AX-05)**: Establish a mechanically enforced application-facing **Mneme** managed-information boundary for active use, while preserving **Mnemosyne** as the sole establisher of authoritative durable state.
2. **Eliminate Raw Active Cache Mutation (MAT-01)**: Prevent application and presentation tiers from mutating Infinispan cache memory directly, performing uncoordinated writes, or inventing local version progressions.
3. **Establish Authoritative Search Boundary (MAT-02, ADR-010)**: Prohibit cluster-wide `remoteCache.values()` memory scans, define a type-neutral authoritative search contract, and isolate concrete search implementation behind the unresolved **MAT-06** boundary.
4. **Decouple Presentation from Persistence (MAT-05, Invariant 3)**: Remove direct PostgreSQL/JDBC/JPA coupling and `@DataSourceDefinition` bindings from `iris-befe` for both clinical and audit data without conflating Kleio audit evidence with Mnemosyne clinical persistence.
5. **Preserve Governed Information Lifecycle (ADR-020)**: Model record retirement as governed authoritative `UPDATE` operations using lifecycle semantics appropriate to each information type, rejecting unsupported HTTP `DELETE` operations at presentation boundaries.
6. **Strictly Isolate MAT-06**: Keep all immediate remediation designs completely agnostic to whether Mnemosyne Clinical uses custom relational JPA persistence or HAPI FHIR JPA Server.

---

**Scope**
**In Scope**
- Comprehensive architectural assessment of the current runtime topology and execution containers (`iris-befe`, `infinispan-cluster`, `mnemosyne-clinical`, `pylai-fhir-registry`).
- Tracing and diagnosing all 8 concrete read, create, update, delete, search, and audit access paths across the repository.
- Root-cause reframing identifying the absence of a mechanically enforced Mneme managed-information boundary.
- Technical evaluation of previous Task 08/09/10 artefacts (retain, rework, supersede, or block).
- Target logical access models for point `READ`, `CREATE`, `UPDATE`, lifecycle transitions, `SEARCH`, and `Kleio` audit queries.
- Clear separation between application/domain contracts (e.g. `WriteResult`, `GovernedRead`) and presentation HTTP status mappings (`201`, `200`, `404`, `405`, `409`, `OperationOutcome`).
- Refined 5-goal implementation sequence with clear decoupling of Goal 3A (search contract/port) from Goal 3B (concrete search implementation blocked by MAT-06) and separate resolution of Goal 4 (Iris -> Kleio audit boundary).
- Mechanical enforcement strategy using ArchUnit and fail-closed platform APIs.

**Out of Scope**
- Implementing code modifications or making git commits (strictly a planning and architecture assessment).
- Resolving **MAT-06** (custom relational persistence vs. HAPI FHIR JPA Server).
- Implementing Goal 3B concrete authoritative search prior to MAT-06 resolution.
- Creating new distributed network protocols or microservices.
- Conflating Kleio audit evidence with Mneme/Mnemosyne clinical information.

---

**Architectural Authority & Axiom Governance**
All analyses adhere strictly to the Harmonia Architectural Authority Hierarchy:
1. `docs/architectural-axioms.md` (Highest authority)
    - **AX-01**: Health-Information Centricity.
    - **AX-02**: Standards at the boundary; Harmonia within the boundary.
    - **AX-04**: Use the machinery; own the semantics.
    - **AX-05**: Active state and authoritative durable state are distinct (*"Mneme manages active use; Mnemosyne establishes durable truth"*).
    - **AX-06**: Explicit information authority.
    - **AX-07**: Security is intrinsic to managed operations (Themis default-deny).
    - **AX-10**: Resilience through visible failure (no silent in-memory fallback).
    - **AX-11**: Responsive and highly available managed information access.
    - **AX-12**: Hide plumbing, not information.
    - **AX-13**: Harmonia management has an explicit boundary (fail-closed egress projection).
2. `../../AGENTS-old2.md` (Repository Invariants 1 through 10, particularly Invariant 3 [Iris Presentation Decoupling] and Invariant 8 [Mneme/Mnemosyne State Separation]).
3. Accepted Architecture Decisions (`ADR-003`, `ADR-006`, `ADR-010`, `ADR-013`, `ADR-018`, `ADR-019`, `ADR-020`).

**Technical Design**

**1. Current Runtime Topology**

The Harmonia platform executes across distinct runtime processes and communication protocols:

| Runtime Process / Container | Framework / Host | Network / Ports | Core Subsystems Hosted | Outbound Communication Mechanisms |
| :--- | :--- | :--- | :--- | :--- |
| **`iris-befe`** (`harmonia-befe`) | WildFly Jakarta EE 10 | HTTP `8080` (Clinical), `8090` (Ops), `9990` (Admin) | Iris BEFE REST resources, CORS filters, Themis security filters | • Hot Rod TCP (`11222`) to `infinispan-1`<br/>• Direct JDBC (`5432`) to PostgreSQL `fhir_node_1` (MAT-05 violation) |
| **`mneme-cluster`** (`infinispan-1`, `infinispan-2`) | Infinispan 15 Standalone | Hot Rod TCP `11222`, JGroups `7800` | Clustered active caches, `active-coordination-cache`, `FhirRestCacheStore` | • HTTP Client (`8080`) to `mnemosyne-clinical` via `HapiFhirRestClient` (read-through / write-behind SPI) |
| **`mnemosyne-clinical`** (`hapi-fhir-1`, `hapi-fhir-2`) | Spring Boot 3 + HAPI FHIR | HTTP `8080` (internal) / `8081` (external host) | HAPI FHIR `JpaRestfulServer`, `FhirStorageService`, `AuthoritativePersistenceService`, `DefaultGovernedWriter` | • JDBC (`5432`) to PostgreSQL `fhir_node_1` |
| **`mnemosyne-operations`** (`operations-1`, `operations-2`) | Spring Boot 3 | HTTP `8080` (internal) / `8085` (external host) | Operations REST APIs, Task Sequence state persistence | • JDBC (`5432`) to PostgreSQL `ops_node_1` |
| **`petasos`** (`harmonia-petasos`) | ActiveMQ Artemis 2.33 | OpenWire TCP `61616` | Petasos resilient message queues | Artemis clustering protocols |
| **`energeia-ponos`** (`task-processor`) | WildFly + Apache Camel | HTTP `8083` | Ponos WorkEngine, Praxis workflow orchestration, Erga activities | • OpenWire TCP (`61616`) to Petasos<br/>• Hot Rod TCP (`11222`) to Infinispan |
| **`pylai-fhir-registry`** | Spring Boot 3 | HTTP `8082` | Inbound FHIR REST gateway, `ChangeRequestSubmissionService` | • OpenWire TCP (`61616`) to Petasos<br/>• In-process / HTTP to `FhirStorageService` |

---

**2. Current Access Paths & Bypasses**

```
1. Iris/BEFE -> READ Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.read(id)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.getResourceJson -> remoteCache.get(id)
     -> Authoritative-State: [BYPASSED if present in cache] / CacheStore HTTP GET to Mnemosyne on miss
     -> Durable Store: PostgreSQL fhir_node_1 (via CacheStore read-through fallback only)
   [STATUS]: DEFECTIVE (Treats cache as primary record; no active-state token or provenance boundary)

2. Iris/BEFE -> CREATE Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.create(payload)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.putResourceJson -> remoteCache.put(id, payload)
     -> Authoritative-State: [ABSENT / BYPASSED] (GovernedWriter / AuthoritativePersistencePort ignored)
     -> Durable Store: [BYPASSED] (Volatile cache only; uncoordinated write-behind may race)
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-01)

3. Iris/BEFE -> UPDATE Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.update(id, payload)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.saveResource -> sets local versionId=ver+1 -> remoteCache.put(id)
     -> Authoritative-State: [ABSENT / BYPASSED] (ActiveStateCoordinator / CAS persistence ignored)
     -> Durable Store: [BYPASSED]
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-01, local version progression)

4. Iris/BEFE -> Lifecycle / Deletion
   Caller: Iris SPA
     -> Application API: PractitionerResource.delete(id)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.deleteResource -> remoteCache.remove(id)
     -> Authoritative-State: [ABSENT / BYPASSED]
     -> Durable Store: [BYPASSED / Physical delete if cache store syncs]
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-01, ADR-020 violation: physical delete exposed)

5. Iris/BEFE -> SEARCH Practitioner
   Caller: Iris SPA
     -> Application API: PractitionerResource.search(id, name, identifier)
     -> Module: iris-befe
     -> Runtime Boundary: Hot Rod TCP (:11222)
     -> Active-State: FhirCacheService.searchResources -> remoteCache.values() full scan into heap
     -> Authoritative-State: [ABSENT / BYPASSED] (Mnemosyne / HAPI search indexing bypassed)
     -> Durable Store: [BYPASSED]
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-02, ADR-010 violation)

6. Pylai -> READ Practitioner
   Caller: External FHIR Client
     -> Application API: FhirRestGatewayController.readResource(type, id)
     -> Module: pylai-fhir-registry
     -> Runtime Boundary: In-process Spring bean / HTTP
     -> Active-State: [BYPASSED] (Does not check Mneme active cache)
     -> Authoritative-State: FhirStorageService.getResource -> FhirResourceRepository.findByResourceTypeAndFhirId
     -> Durable Store: PostgreSQL hie_fhir_resources
     -> Egress: PylaiFhirPublicationProjector.projectForPublication
   [STATUS]: AUTHORITATIVE (Bypasses Mneme cache-aside acceleration)

7. Pylai -> SEARCH Practitioner
   Caller: External FHIR Client
     -> Application API: FhirRestGatewayController.searchResources(type, allParams)
     -> Module: pylai-fhir-registry
     -> Runtime Boundary: In-process Spring bean / HTTP
     -> Active-State: [BYPASSED]
     -> Authoritative-State: FhirStorageService.searchResources -> repository.findByResourceType + in-memory filtering
     -> Durable Store: PostgreSQL hie_fhir_resources
     -> Egress: PylaiFhirPublicationProjector.projectForPublication
   [STATUS]: AUTHORITATIVE (Subject to persistence query capabilities)

8. Iris/BEFE -> Direct Kleio Audit Persistence (MAT-05)
   Caller: Iris Console / Admin SPA
     -> Application API: AuditEventResource.search() / read(id)
     -> Module: iris-befe
     -> Runtime Boundary: Direct JDBC (:5432) via AuditDataSourceProducer
     -> Direct Coupling: @DataSourceDefinition binds KleioAuditDS directly to postgres-1
     -> Execution: DurableAuditService / JdbcAppendOnlyAuditEventRepository executes SQL queries in BEFE
   [STATUS]: CRITICAL AXIOM CONFLICT (MAT-05, Invariant 3 violation)
```

---

**3. MAT-01 / MAT-02 / MAT-05 Root-Cause Assessment**

**Reframed Root Cause**
The architectural root cause is:
> **"There is no mechanically enforced application-facing Mneme managed-information boundary, causing callers to receive raw infrastructure capabilities and bypass Harmonia information-management semantics."**

The defect is not merely the absence of a class named `MnemeClient`. Rather, because `iris-befe` was provided with raw infrastructure primitives (`RemoteCacheManager` Hot Rod client and container `@DataSourceDefinition` JDBC pools) rather than a governed application boundary:
1. Application endpoints defaulted to treating Infinispan `RemoteCache` as a key-value database for point CRUD, inventing uncoordinated writes and local version progressions (`MAT-01`).
2. Search was implemented by naively pulling all cached strings via `remoteCache.values()` and filtering them in JVM memory (`MAT-02`).
3. Presentation queries for audit events were wired directly to PostgreSQL JDBC repositories because no service-level query port existed (`MAT-05`).

**Analysis of Existing Mneme APIs vs. Application Facade Boundary**
Inspection of existing public interfaces reveals:
- `calliope/model/governedwrite/GovernedWriter.java`: Exposes pure write contracts (`create` and `update`) with `ResourceKey`, `GovernedRead`, `WriteResult`, and `ThemisSecurityContext`.
- `calliope/model/governedwrite/ActiveStateCoordinator.java` & `ActiveStateConvergencePort.java`: Internal active-state coordination and post-commit cache convergence SPIs.
- `hestia/mnemosyne-clinical`: Contains `DefaultGovernedWriter` orchestrating writes across Themis, active coordination, authoritative persistence, and convergence.

**Why an Evolved Application-Facing Boundary Is Required**:  
While `GovernedWriter` provides a sound contract for write operations, application callers require a cohesive, unified managed-information access boundary that provides:
1. **Point READ**: Cache-aside resolution with authoritative fallback on cache miss and active read-repair (`get(ResourceKey)` returning `GovernedRead<T>`).
2. **Governed WRITE**: `create` and `update` delegating through the governed write pipeline.
3. **Governed SEARCH**: Explicit search delegation port prohibiting in-memory cache scans.
   Whether this boundary is realized as an expanded `MnemeAccessPort` / `MnemeClient` or an evolution of existing contracts, it must encapsulate raw `RemoteCache` access and mechanically enforce Harmonia governance.

**Managed-Type Classification Inspection**
Inspection of Calliope and Mneme shows:
- `calliope/model/registry/ProviderRegistryConstants.java` defines `SUPPORTED_RESOURCE_TYPES` (`Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, `Group`) and validation rules.
- Calliope canonical schema definitions represent the semantic source of truth for Harmonia domain types.
- **Architectural Decision**: Semantic classification (`information type -> managed/governed semantics`) must have **one authoritative owner** (Calliope). Mneme consumes this semantic classification to manage physical cache and runtime configurations. A separate, parallel `ManagedTypeRegistry` must NOT be created before semantic ownership is formally decided.

**Kleio Audit Query Boundary Inspection**
- `kleio-core` defines `AuditService`, `AuditQuery`, and `HarmoniaAuditEvent`.
- `kleio-persistence` provides `DurableAuditService` and `JdbcAppendOnlyAuditEventRepository`.
- `kleio-fhir` provides `HarmoniaAuditEventMapper`.
- **Architectural Boundary**: Kleio audit evidence represents immutable security/audit history and is **not** Mnemosyne-managed clinical domain information. Remediating MAT-05 in `iris-befe` must **not** route audit queries through Mneme by default. Instead, the application-facing Kleio query boundary (`Iris -> ? -> Kleio`) is treated as a separate architectural problem to be resolved without direct JDBC bindings.

---

**4. Existing Machinery That Can Be Reused**

The repository already contains high-quality, production-ready building blocks:
1. **Governed Write Pipeline (`hestia/mnemosyne-clinical` & `calliope`)**:
    - `DefaultGovernedWriter`: Full orchestration of Themis security -> `ActiveStateCoordinator` -> `AuthoritativePersistencePort` -> `ActiveStateConvergencePort`.
    - `AuthoritativePersistenceService`: Atomic CREATE uniqueness and conditional UPDATE with expected predecessor version checking (`TransactionTemplate`).
    - Sealed result types: `WriteResult`, `AuthoritativePersistenceResult`, `ConvergenceStatus`.
2. **Active State Coordination & Convergence (`hestia/mneme-cluster`)**:
    - `HotRodActiveStateCoordinator`: Lightweight token observation and atomic consumption over `active-coordination-cache` with fail-fast visible failure.
    - `HotRodMnemeConvergence`: Bounded CAS loop post-commit cache convergence with newer-version protection.
3. **Publication & Security Boundary (`pylai-fhir-registry` & `calliope`)**:
    - `PylaiFhirPublicationProjector`: Non-destructive fail-closed egress sanitization stripping internal operational metadata.
    - `FhirSecurityTagManager` & `ThemisSecurityContext`: Explicit distinction between persistent clinical confidentiality labels and transient operational security context.
4. **Kleio Audit Infrastructure (`kleio`)**:
    - `AuditService`, `AuditQuery`, and `HarmoniaAuditEventMapper`: Standardized interfaces for querying and mapping immutable audit events.
5. **Resilient Transport (`petasos` & `energeia-ponos`)**:
    - ActiveMQ Artemis queues and Camel `Pragma` task sequence pipelines for asynchronous governed change workflows.

---

**5. Previous Task 08/09/10 Assessment**

| Task / Artefact | Repository Location | Assessment | Detailed Rationale & Action |
| :--- | :--- | :--- | :--- |
| **Task 08: Governed Write Contracts** | `calliope/model/governedwrite/*` | **RETAIN** | Perfectly aligned with AX-05, AX-07, and AX-10. Retain pure contracts (`GovernedWriter`, `ResourceKey`, `ActiveStateToken`, `ExpectedAuthoritativeVersion`, `WriteResult`). |
| **Task 08: Active-State Coordinator** | `hestia/mneme-cluster/coordination/*` | **RETAIN** | Production Hot Rod coordinator over `active-coordination-cache`. Retain as internal engine component. |
| **Task 08: Active-State Convergence** | `hestia/mneme-cluster/convergence/*` | **RETAIN** | Production Hot Rod CAS convergence port with version protection. Retain as internal engine component. |
| **Task 08: Authoritative Persistence** | `hestia/mnemosyne-clinical/persistence/*` | **RETAIN (LOGICAL)** | Atomic persistence logic and result models are valid. Retain interface contracts; persistence implementation remains subject to MAT-06 abstraction. |
| **Task 08: BEFE Write Migration (08.05A)** | `.junie/plans/migrate-befe-clinical-writes.md` | **REWORK** | Target concept of unified Mneme access boundary is correct; re-align with revised Goal 2. |
| **Task 09: Cache-Aside Point Reads** | `docs/backlog/Harmonia - Task 9 - Backlog.md` | **REWORK / INTEGRATE** | Cache-aside read on miss + read-repair is essential for AX-05/AX-11. Integrate into Goal 3A while strictly isolating internal version provenance (`BL-09-01`). |
| **Task 10: Governed Lifecycle (ADR-020)** | `docs/architecture-decisions.md` (ADR-020) | **RETAIN** | Prohibits physical deletion. Model retirement as authoritative `UPDATE`; reject HTTP `DELETE` at presentation boundary with `405 Method Not Allowed`. |
| **Legacy Infinispan Cache Store SPI** | `hestia/mneme-persistence/*` | **SUPERSEDE** | Uncoordinated background HTTP store (`FhirRestCacheStore`) causes race conditions and hides persistence failure. Supersede with explicit synchronous cache-aside and governed write convergence. |

---

**6. Target Logical Access Model**

```mermaid
graph LR
    subgraph Client Tier
        APP[Application / Iris BEFE]
    end

    subgraph Mneme Boundary
        MC[Mneme Access Boundary / Facade]
        COORD[ActiveStateCoordinator]
        CONV[ActiveStateConvergencePort]
    end

    subgraph Governance & Security
        THM[Themis Policy Engine]
    end

    subgraph Mnemosyne Authoritative Tier
        GW[GovernedWriter]
        AUTH_PORT[AuthoritativePersistencePort]
        SEARCH_PORT[AuthoritativeSearchPort]
        STORE[(Authoritative Store / Postgres)]
    end

    subgraph Active Cache
        CACHE[(Infinispan Cluster)]
    end

    %% Read Flow
    APP -- 1. get(key) --> MC
    MC -- 2. Cache Lookup --> CACHE
    CACHE -. miss .-> MC
    MC -. 3. Authoritative Read .-> AUTH_PORT
    AUTH_PORT --> STORE
    AUTH_PORT -. 4. Return Truth .-> MC
    MC -. 5. Converge Cache .-> CACHE
    MC --> APP

    %% Write Flow
    APP -- 1. create / update(key, val) --> MC
    MC -- 2. Execute Governed Write --> GW
    GW -- 3. Authorize --> THM
    GW -- 4. Consume Token --> COORD
    COORD <--> CACHE
    GW -- 5. Commit Atomic CAS --> AUTH_PORT
    AUTH_PORT --> STORE
    GW -- 6. Post-Commit Converge --> CONV
    CONV --> CACHE
    GW --> MC
    MC --> APP

    %% Search Flow
    APP -- 1. search(query) --> MC
    MC -- 2. Delegate Search --> SEARCH_PORT
    SEARCH_PORT --> STORE
    SEARCH_PORT --> MC
    MC --> APP
```

**Point READ Protocol (Cache-Aside + Read-Repair)**
1. Application invokes `mneme.get(ResourceKey key, Class<T> clazz)`.
2. `Mneme` checks active cache in Infinispan.
3. **Cache Hit**: Validates entry freshness and active token; returns cached representation in `GovernedRead<T>`.
4. **Cache Miss / Invalidation**: Fall-through to `AuthoritativePersistencePort.read(key)`.
5. If resource exists in Mnemosyne:
    - Returns authoritative state.
    - Synchronously or asynchronously converges active cache via `ActiveStateConvergencePort` (non-destructive read-repair).
    - Strips internal management provenance (`BL-09-01`) before returning domain representation to caller.
6. If resource is absent: returns empty `Optional`.

**Governed CREATE Protocol**
1. Application invokes `mneme.create(ResourceKey key, T resource, ThemisSecurityContext securityContext)`.
2. Evaluates Themis authorization (`ThemisAction.CREATE`).
3. Invokes `AuthoritativePersistencePort.create(key, resource)`.
    - Mnemosyne atomically verifies unique existence and commits record with authoritative version 1.
4. On successful commit: invokes `ActiveStateConvergencePort.converge()` to seed active cache with version 1.
5. Returns domain `WriteResult<T>`.
6. Presentation layer (`iris-befe` / `pylai`) maps `WriteResult.committed` to HTTP `201 Created` with corresponding `Location` and `ETag`.

**Governed UPDATE Protocol**
1. Application performs a point read, obtaining resource and its `GovernedRead<T>` observation context (holding `ActiveStateToken` and `ExpectedAuthoritativeVersion`).
2. Application modifies resource and invokes `mneme.update(GovernedRead<T> context, T proposed, ThemisSecurityContext securityContext)`.
3. Evaluates Themis authorization (`ThemisAction.UPDATE`).
4. Coordinates active state: `ActiveStateCoordinator.consume(key, observedToken)`. If token is stale, rejects with `WriteResult.activeStateConflict`.
5. Executes authoritative persistence: `AuthoritativePersistencePort.update(key, proposed, expectedVersion)`.
    - Mnemosyne verifies predecessor version matches in durable store.
    - Atomically increments authoritative version and commits new state.
6. On successful commit: converges active cache via `ActiveStateConvergencePort` (CAS loop with newer-version protection).
7. Returns domain `WriteResult<T>`.
8. Presentation layer maps `WriteResult.committed` to HTTP `200 OK` with updated `ETag`.

**Lifecycle Transition (ADR-020 Lifecycle Governance)**
1. Governed resources are never physically deleted via application CRUD.
2. Lifecycle transitions (e.g. `status = inactive`, `entered-in-error`, `de-registered`) are modeled as governed authoritative `UPDATE` operations using the domain lifecycle semantics appropriate to that specific information type.
3. Application/presentation endpoints reject HTTP `DELETE` operations where physical deletion is unsupported, returning HTTP `405 Method Not Allowed` with structured `OperationOutcome` explaining ADR-020 lifecycle governance.

**Authoritative SEARCH Protocol Boundary**
1. Application invokes `mneme.search(Class<T> type, SearchQuery query)`.
2. `Mneme` delegates directly to `AuthoritativeSearchPort` in Mnemosyne.
3. Mnemosyne executes the query against native database search/index machinery.
4. Results are returned to `Mneme` as domain result sets and delivered to the caller.
5. Cluster-wide `remoteCache.values()` scans are strictly prohibited as search semantics.

**Kleio Audit Query Boundary (`Iris -> ? -> Kleio`)**
1. Presentation queries for audit events (e.g. `AuditEventResource` in `iris-befe`) require decoupled read access to `AuditService`.
2. Kleio audit records are append-only evidence and are kept distinct from Mnemosyne clinical persistence and Mneme active cache.
3. The cross-runtime query boundary shall be established via a dedicated service port or read-only service endpoint rather than embedding direct `@DataSourceDefinition` JDBC pools in the presentation container.

---

**7. Mechanical Enforcement Strategy**

To satisfy Axiom **AX-12** (*"Hide Plumbing, Not Information"*) and Section 5 (*"Make the safe thing the easy thing, and make the unsafe thing difficult or impossible"*):

1. **API Boundary Encapsulation**:
    - `iris-befe` and application modules shall ONLY depend on `calliope` (contracts) and Mneme application client contracts.
    - Raw `infinispan-client-hotrod` and `RemoteCache` access shall NOT be exported to presentation or application web packages.
2. **ArchUnit Automated Architecture Rules (`paradeigma-test`)**:
    - `IrisDecouplingArchitectureTest`: Asserts zero imports of `jakarta.persistence.*`, `org.hibernate.*`, `org.postgresql.*`, `ca.uhn.fhir.jpa.*`, and zero `@DataSourceDefinition` annotations in `iris-befe`.
    - `MnemeStateSeparationArchitectureTest`: Asserts that application and presentation tiers never invoke `RemoteCache.put()`, `RemoteCache.remove()`, or `RemoteCache.values()`.
    - `GovernedWriteContractArchitectureTest`: Asserts that `AuthoritativePersistencePort` and `GovernedWriter` contain zero physical `delete()` methods.
    - `AuditPersistenceDecouplingArchitectureTest`: Asserts that presentation modules do not instantiate direct database connections for audit logging.
3. **Fail-Closed Type Safety**:
    - Unmanaged or uncoordinated resource writes fail closed with an explicit typed failure, preventing silent pass-through to uncoordinated cache.

---

**8. MAT-06 Dependency Isolation**

**MAT-06 Boundary**: Whether Mnemosyne Clinical uses custom relational FHIR JPA persistence or HAPI FHIR JPA Server.

**Independent Remediation (Proceeds in Goals 1, 2, 3A, 4, 5)**
- Evolving/establishing the Mneme application-facing managed-information contracts (`GovernedWriter`, `GovernedRead`, `ResourceKey`, `AuthoritativeSearchPort`).
- Migration of `iris-befe` `POST / CREATE` and `PUT / UPDATE` to the Mneme boundary.
- Elimination of raw `RemoteCache.put()` and local version progression.
- Enforcement of ADR-020 lifecycle transitions and HTTP `405 Method Not Allowed` for `DELETE`.
- Implementation of cache-aside point READ with read-repair in Mneme.
- Definition of `AuthoritativeSearchPort` interface and prohibition of `remoteCache.values()` as search semantics.
- Decoupling of `iris-befe` from direct PostgreSQL/Kleio JDBC datasources (MAT-05).
- ArchUnit automated architectural enforcement tests.

**Blocked on MAT-06 (Goal 3B - Must Stop at Interface Boundary)**
- Concrete implementation of `AuthoritativeSearchPort` (custom SQL/JPA queries vs. HAPI `IFhirResourceDao.search()`).
- Decommissioning of legacy search fallback before native authoritative search is online.
- Mnemosyne internal database schema and HAPI JPA server configuration.

**Implementation Sequence**

**1. Revised Bounded Implementation Sequence**

The remediation is decomposed into five compact, sequentially executable implementation goals with an explicit pause boundary for MAT-06:

```
[Goal 1: Establish/Evolve Mneme Application-Facing Managed-Information Contract]
                                      │
                                      ▼
[Goal 2: Migrate Governed Writes & Enforce ADR-020 Lifecycle in Presentation Tier]
                                      │
                                      ▼
[Goal 3A: Implement Cache-Aside Point READ & Establish Authoritative Search Port]
                                      │
                                      ▼
                    ═════════════════════════════════════
                    🛑 STOP FOR MAT-06 ARCHITECTURE DECISION
                    ═════════════════════════════════════
                                      │
                                      ▼
[Goal 3B: Implement Concrete Authoritative Search & Decommission Cache Scans]
                                      │
                                      ▼
[Goal 4: Resolve Iris -> Kleio Query Coupling (MAT-05 Presentation DB Decoupling)]
                                      │
                                      ▼
[Goal 5: Add & Strengthen Mechanical ArchUnit Architectural Invariant Tests]
```

**Goal 1: Establish/Evolve Mneme Application-Facing Managed-Information Contract**
- **Findings Addressed**: Foundation for MAT-01, MAT-02.
- **Architectural Invariant Established**: Single application-facing managed-information access boundary (`AX-05`); semantic type authority in Calliope (`AX-06`).
- **Modules Affected**: `calliope`, `hestia/mneme-cluster`.
- **Prerequisites**: Existing Task 08 contracts in `calliope/model/governedwrite`.
- **Scope & Deliverables**:
    - Inspect existing public Mneme APIs and determine whether existing contracts (`GovernedWriter`, `GovernedRead`, `ResourceKey`) can be evolved or encapsulated into a unified application-facing access boundary.
    - Inspect Calliope canonical model and registry mechanisms (`ProviderRegistryConstants.SUPPORTED_RESOURCE_TYPES`) to reuse existing semantic type classifications rather than inventing a duplicate `ManagedTypeRegistry`.
    - Formalize point read contract (`GovernedRead<T>`), write contract, and search contract interface boundaries.
    - Keep domain contracts strictly decoupled from presentation HTTP semantics (`200`, `201`, `404`, `405`, `409`, `OperationOutcome`).
- **Stop Condition**: Pure unit tests in `calliope` and `mneme-cluster` verify contracts and type validation without modifying the presentation tier.

**Goal 2: Migrate Governed Writes and Enforce ADR-020 Lifecycle in Presentation Tier**
- **Findings Addressed**: **MAT-01** (Iris BEFE Raw Cache Mutation & Local Version Progression).
- **Architectural Invariant Established**: Active vs. Durable State Separation (`AX-05`); Lifecycle Governance / No Physical Deletion (`ADR-020`).
- **Modules Affected**: `iris/iris-befe`, `calliope`, `hestia/mnemosyne-clinical`.
- **Prerequisites**: Goal 1.
- **Scope & Deliverables**:
    - Migrate `PractitionerResource` and other BEFE clinical endpoints to use the Mneme governed write boundary for `POST` (create) and `PUT` (update).
    - Remove `remoteCache.put()` and local `versionId` increments from `FhirCacheService`.
    - Remove `remoteCache.remove()` and update BEFE `@DELETE` methods to return `405 Method Not Allowed` with structured `OperationOutcome` explaining ADR-020.
    - Retain existing Task 08 coordination and convergence machinery (`HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`, `DefaultGovernedWriter`).
- **Stop Condition**: BEFE `POST` and `PUT` execute authoritatively through Mnemosyne and converge Infinispan; `DELETE` returns HTTP 405; raw `remoteCache.put` is removed from BEFE write paths.

**Goal 3A: Implement Cache-Aside Point READ and Establish Authoritative Search Port Boundary**
- **Findings Addressed**: **MAT-02** (Foundation), **BL-09-01** (Cache-Aside Read & Provenance Isolation).
- **Architectural Invariant Established**: Authoritative Search Boundary (`ADR-010`); Non-Destructive Active Cache Acceleration (`AX-05`, `AX-11`).
- **Modules Affected**: `hestia/mneme-cluster`, `calliope`, `iris/iris-befe`.
- **Prerequisites**: Goal 1 & Goal 2.
- **Scope & Deliverables**:
    - Implement cache-aside point READ logic in Mneme: check active cache -> on miss, fall-through to `AuthoritativePersistencePort.read(key)` -> execute active read-repair via `ActiveStateConvergencePort`.
    - Isolate internal management provenance metadata (`BL-09-01`) before returning domain representations to callers.
    - Define `AuthoritativeSearchPort` interface in `calliope` representing type-neutral authoritative search queries.
    - Explicitly prohibit `remoteCache.values()` as authoritative search semantics in architectural rules.
    - **Explicit Stop Boundary**: STOP before Goal 3B pending the resolution of MAT-06.
- **Stop Condition**: Point `READ` transparently populates and repairs cold cache; `AuthoritativeSearchPort` contract is defined; execution pauses for MAT-06 decision.

**Goal 3B: Implement Concrete Authoritative Search and Decommission Cache Scans (Post MAT-06)**
- **Findings Addressed**: **MAT-02** (Authoritative Search Completion).
- **Architectural Invariant Established**: Authoritative Native Index Search (`AX-04`, `ADR-010`).
- **Modules Affected**: `hestia/mnemosyne-clinical`, `hestia/mneme-cluster`, `iris/iris-befe`.
- **Prerequisites**: Goal 3A AND formal resolution of **MAT-06** (custom relational JPA vs. HAPI FHIR JPA Server).
- **Scope & Deliverables**:
    - Implement `AuthoritativeSearchPort` using the selected native Mnemosyne search engine.
    - Wire Mneme search delegation to Mnemosyne's native search implementation.
    - Completely decommission and delete `remoteCache.values()` scanning logic from `FhirCacheService`.
- **Stop Condition**: Clinical searches execute against native database indexes; `remoteCache.values()` is completely deleted from the codebase.

**Goal 4: Resolve Iris -> Kleio Presentation Query Coupling (MAT-05 Boundary)**
- **Findings Addressed**: **MAT-05** (Presentation Database Coupling for Audit Events).
- **Architectural Invariant Established**: Presentation Decoupling (`Invariant 3`, `AX-12`).
- **Modules Affected**: `iris/iris-befe`, `kleio/kleio-persistence`, `kleio/kleio-core`.
- **Prerequisites**: Goals 1–3A.
- **Scope & Deliverables**:
    - Inspect existing `AuditService` query capabilities and runtime boundaries.
    - Resolve the cross-runtime audit query boundary between `iris-befe` and Kleio without routing through Mneme clinical caches and without inventing ad-hoc network services.
    - Remove `AuditDataSourceProducer.java`, `@DataSourceDefinition`, and direct PostgreSQL/JDBC dependencies from `iris-befe`.
- **Stop Condition**: `AuditEventResource` queries audit events via a decoupled service boundary with zero `@DataSourceDefinition` or direct JDBC drivers in `iris-befe`.

**Goal 5: Add and Strengthen Mechanical ArchUnit Architectural Invariant Tests**
- **Findings Addressed**: **MAT-10** (Mechanical Enforcement of Axioms).
- **Architectural Invariant Established**: Mechanical Enforcement (`AGENTS.md Section 4`, `Section 5`).
- **Modules Affected**: `paradeigma/paradeigma-test`.
- **Prerequisites**: Goals 1–4.
- **Scope & Deliverables**:
    - Add/strengthen ArchUnit tests in `paradeigma-test`:
        1. Asserts zero `RemoteCache.put()`, `RemoteCache.remove()`, or `RemoteCache.values()` calls in presentation/application tiers.
        2. Asserts zero `jakarta.persistence.*`, `org.hibernate.*`, `org.postgresql.*`, or `@DataSourceDefinition` in `iris-befe`.
        3. Asserts zero direct Kleio persistence implementation access from Iris presentation classes.
        4. Asserts zero physical `delete()` methods in governed persistence contracts (`AuthoritativePersistencePort`, `GovernedWriter`).
        5. Asserts all application access to managed information traverses the established Mneme boundary.
- **Stop Condition**: Full reactor architecture test suite (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) passes 100%.

---

**2. Unresolved Architectural Questions**

The following genuine architectural questions remain outside the scope of current authority and require formal decisions:

1. **Audit Query Boundary across JVM Containers (MAT-05 / Kleio)**:
    - *Question*: How should `iris-befe` query durable Kleio audit events when running in a separate WildFly container from PostgreSQL, given that Kleio audit evidence is distinct from Mneme-managed clinical information?
    - *Options*: (a) Expose a dedicated read-only Kleio REST query endpoint; (b) Provide an isolated, strictly read-only audit datasource configuration decoupled from clinical persistence; (c) Project summarized non-PHI audit events to an administrative query port.
2. **Managed-Type Classification Ownership (Calliope vs. Mneme)**:
    - *Question*: Is the canonical definition of which resources/information types are governed owned exclusively by Calliope domain models, or does Mneme maintain an independent classification layer?
    - *Recommendation*: Establish Calliope as the sole semantic source of truth (`ProviderRegistryConstants` / domain descriptors), with Mneme consuming that definition for runtime cache configuration.
3. **MAT-06 Mnemosyne Clinical Persistence Engine Choice**:
    - *Question*: Should Mnemosyne Clinical permanently adopt HAPI FHIR JPA Server as its storage and search engine, or continue evolving Harmonia's custom relational PostgreSQL schema?
    - *Status*: Strictly preserved as unresolved per assessment constraints.

**Testing**

**Validation Approach**
Verification relies on static architectural analysis, unit contract testing, integration scenario testing, and automated ArchUnit architecture rules.

---

**Key Scenarios & Target Outcomes**

| Scenario ID | Test Target | Precondition | Execution Flow | Expected Verification Outcome |
| :--- | :--- | :--- | :--- | :--- |
| **SC-01** | **Governed CREATE via Mneme Boundary** | Empty active cache, clean DB | `mneme.create(key, Practitioner, secContext)` | • Themis permits operation.<br/>• Mnemosyne commits row to PostgreSQL with version 1.<br/>• Mneme active cache is seeded with version 1.<br/>• Presentation maps to HTTP 201 Created with ETag `W/"1"`. |
| **SC-02** | **Governed UPDATE with Valid Token** | Resource exists at version 1 | Read version 1 -> modify -> `mneme.update(readContext, Practitioner, secContext)` | • Active token consumed.<br/>• Mnemosyne commits version 2 via atomic CAS.<br/>• Mneme active cache converges to version 2.<br/>• Presentation maps to HTTP 200 OK with ETag `W/"2"`. |
| **SC-03** | **Governed UPDATE Stale Version Conflict** | Resource modified concurrently to version 2 | Submit UPDATE with expected version 1 | • Mnemosyne rejects update (0 rows matched).<br/>• Returns `WriteResult.authoritativeConflict`.<br/>• Presentation maps to HTTP 409 Conflict; cache remains uncorrupted. |
| **SC-04** | **Authoritative-Backed Search Contract (Goal 3A)** | Test harness for Mneme search | Invoke `mneme.search(type, query)` | • Search delegates to `AuthoritativeSearchPort`.<br/>• Zero `remoteCache.values()` scans executed.<br/>• MAT-06 boundary respected. |
| **SC-05** | **Cache-Aside Read on Cold Start (Goal 3A)** | Resource in DB; not in cache | `mneme.get(ResourceKey, Practitioner.class)` | • Active cache misses.<br/>• Reads authoritatively from Mnemosyne.<br/>• Converges active cache (read-repair).<br/>• Provenance metadata stripped from payload. |
| **SC-06** | **ADR-020 DELETE Rejection (Goal 2)** | Governed resource exists | HTTP `DELETE /api/fhir/Practitioner/123` | • Endpoint returns HTTP `405 Method Not Allowed`.<br/>• Returns OperationOutcome directing caller to governed lifecycle update.<br/>• Zero physical deletions in cache or database. |
| **SC-07** | **Presentation Tier DB Decoupling (Goal 4 & 5)** | `iris-befe` deployment inspection | Inspect loaded classes and datasources | • Zero `@DataSourceDefinition` in `iris-befe`.<br/>• Zero `org.postgresql` JDBC connections from BEFE.<br/>• ArchUnit test passes. |

---

**Automated Architecture Conformance Tests (ArchUnit)**

The following tests in `paradeigma/paradeigma-test` shall mechanically enforce the boundaries:

1. **`IrisDecouplingArchitectureTest`**:
    - Asserts `iris-befe` contains no classes importing `jakarta.persistence.*`, `org.hibernate.*`, or `org.postgresql.*`.
    - Asserts zero `@DataSourceDefinition` annotations exist within `iris-befe`.
2. **`MnemeStateSeparationArchitectureTest`**:
    - Asserts no presentation resource or controller calls `RemoteCache.values()`, `RemoteCache.put()`, or `RemoteCache.remove()`.
    - Asserts all application access traverses the established Mneme boundary.
3. **`GovernedWriteContractArchitectureTest`**:
    - Asserts `AuthoritativePersistencePort` and `GovernedWriter` expose no physical delete methods.
4. **`KleioPersistenceIsolationArchitectureTest`**:
    - Asserts presentation modules do not import direct Kleio JDBC repository implementations.
5. **`ParadeigmaIsolationArchitectureTest`**:
    - Asserts zero production dependencies on simulation models or test harnesses.

**Delivery Steps**

**Step 1: Establish/Evolve Mneme Application-Facing Contracts and Inspect Classification Mechanisms**
Establish and formalize the type-neutral application-facing managed-information contracts across Calliope, Mneme, and Mnemosyne without modifying presentation code.

- Inspect existing public Mneme/Calliope contracts (`GovernedWriter`, `GovernedRead`, `ResourceKey`) and evolve or encapsulate them into a unified application-facing access boundary.
- Inspect Calliope canonical model definitions (`ProviderRegistryConstants.SUPPORTED_RESOURCE_TYPES`) to reuse existing semantic type classifications rather than creating a duplicate registry.
- Define pure Calliope contracts for point READ (`GovernedRead`), governed WRITE (`create`, `update`), and SEARCH port boundaries.
- Keep domain contracts strictly decoupled from HTTP presentation semantics (`200`, `201`, `404`, `405`, `409`, `OperationOutcome`).

**Step 2: Migrate Governed Writes and Enforce Lifecycle Semantics in Presentation Tier**
Migrate Iris BEFE write operations to the established Mneme boundary and enforce ADR-020 lifecycle transitions.

- Migrate `PractitionerResource` and other BEFE clinical endpoints to use the Mneme governed write boundary for `POST` (create) and `PUT` (update).
- Remove `remoteCache.put()` and local `versionId` increments from `FhirCacheService`.
- Remove `remoteCache.remove()` and update BEFE `@DELETE` methods to return `405 Method Not Allowed` with structured `OperationOutcome` explaining ADR-020.
- Retain existing Task 08 coordination and convergence machinery (`HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`, `DefaultGovernedWriter`).

**Step 3: Implement Cache-Aside Point READ and Define Authoritative Search Port Boundary**
Implement cache-aside point reads with read-repair and define the authoritative search contract while pausing for MAT-06.

- Implement cache-aside point READ logic in Mneme: check active cache -> on miss, fall-through to `AuthoritativePersistencePort.read(key)` -> execute active read-repair via `ActiveStateConvergencePort`.
- Isolate internal management provenance metadata (`BL-09-01`) before returning domain representations to callers.
- Define `AuthoritativeSearchPort` interface in `calliope` and prohibit `remoteCache.values()` as authoritative search semantics.
- Enforce the explicit stop boundary pausing concrete search implementation pending the MAT-06 decision.

**Step 4: Resolve Iris-to-Kleio Query Coupling and Presentation Persistence Decoupling**
Decouple Iris BEFE from direct PostgreSQL/JDBC audit database connections as a separate MAT-05 boundary.

- Inspect existing `AuditService` query capabilities and runtime boundaries in `kleio-core` and `kleio-persistence`.
- Resolve the cross-runtime audit query boundary between `iris-befe` and Kleio without routing audit evidence through Mneme clinical caches or inventing ad-hoc network services.
- Remove `AuditDataSourceProducer.java`, `@DataSourceDefinition`, and direct PostgreSQL/JDBC dependencies from `iris-befe`.

**Step 5: Implement Authoritative Search Implementation (Post MAT-06) and Comprehensive ArchUnit Enforcement**
Implement concrete authoritative search after MAT-06 resolution and add/strengthen mechanical ArchUnit architecture tests.

- Implement `AuthoritativeSearchPort` using the selected native Mnemosyne search engine (post MAT-06) and safely delete legacy `remoteCache.values()` scanning logic.
- Add and strengthen ArchUnit tests in `paradeigma-test` asserting: zero `RemoteCache` direct access in presentation resources, zero `DataSourceDefinition` or JDBC imports in Iris, zero direct Kleio persistence implementation access from Iris, zero physical delete methods in governed persistence contracts, and mandatory usage of the Mneme boundary.

ANALYSIS

**Task description**  
Validate and synthesize a revised read-only remediation sequence for the accepted Mneme/Mnemosyne assessment. Focus specifically on: (1) reframing root cause as absence of a mechanically enforced application-facing Mneme managed-information boundary rather than a specific class name; (2) whether existing public Mneme/Calliope interfaces and classification mechanisms appear reusable or whether a new facade/registry would require explicit justification; (3) splitting Goal 3 into 3A contract/boundary work versus 3B concrete search implementation blocked by MAT-06; (4) keeping Kleio audit query decoupling separate from Mneme/Mnemosyne clinical information flow; and (5) producing a concise revised bounded implementation sequence for review only, with no code changes.

**Constraints**  
Read-only synthesis only. Do not propose implementation details that resolve MAT-06. Do not assume a new ManagedTypeRegistry unless repository evidence clearly establishes it. Do not conflate domain/application contracts with HTTP semantics.



**Architectural Synthesis & Revised Remediation Sequence (MAT-01, MAT-02, MAT-05)**

---

**1. Reframed Root Cause Analysis**

The architectural defect across `MAT-01`, `MAT-02`, and `MAT-05` is not specifically the absence of a class named `MnemeClient`.

The fundamental root cause is:
> **"There is no mechanically enforced application-facing Mneme managed-information boundary, causing callers to receive raw infrastructure capabilities and bypass Harmonia information-management semantics."**

Because `iris-befe` was provisioned with raw infrastructure primitives (`RemoteCacheManager` Hot Rod client and container-level `@DataSourceDefinition` JDBC pools) rather than an enforced, governed application boundary:
1. Presentation endpoints defaulted to treating Infinispan `RemoteCache` as a key-value database for point CRUD, inventing uncoordinated writes and local version progressions (`MAT-01`).
2. Search was implemented by naively pulling all cached entries via `remoteCache.values()` into JVM heap memory and performing un-indexed linear scans (`MAT-02`).
3. Presentation queries for audit events were wired directly to PostgreSQL JDBC repositories because no decoupled service-level query port was established (`MAT-05`).

---

**2. Analysis of Existing Contracts & Classification Mechanisms**

**Existing Public Contracts vs. Facade Evolution**
An inspection of existing public interfaces reveals robust contracts already defined in `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/`:
- `GovernedWriter`: Caller-facing contract exposing pure `create` and `update` operations requiring `ResourceKey`, `GovernedRead`, `WriteResult`, and `ThemisSecurityContext`.
- `ActiveStateCoordinator` & `ActiveStateConvergencePort`: Internal coordination and post-commit cache convergence SPIs.
- `AuthoritativePersistenceService`: Atomic CREATE uniqueness and conditional UPDATE with expected predecessor version checking.

**Architectural Rationale**:  
Rather than inventing an ungrounded or competing abstraction layer, Goal 1 will inspect whether `GovernedWriter` and `GovernedRead` can be directly evolved or encapsulated into a cohesive application-facing access boundary that provides:
1. **Point `READ`**: Cache-aside resolution with authoritative fallback on cache miss and active read-repair (`GovernedRead<T>`).
2. **Governed `WRITE`**: Unified `create` and `update` operations delegating through the governed write pipeline.
3. **Governed `SEARCH`**: Search port delegation prohibiting in-memory cache scans.

**Managed-Type Classification Ownership**
- `calliope/src/main/java/net/fhirfactory/harmonia/model/registry/ProviderRegistryConstants.java` defines the canonical list of supported types (`SUPPORTED_RESOURCE_TYPES` for `Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, `Group`) and validation rules.
- **Architectural Invariant**: Semantic classification (`information type -> managed/governed semantics`) must have **one authoritative owner** (`calliope`). Mneme consumes this domain classification to configure physical caches and runtime behaviour. A parallel, competing `ManagedTypeRegistry` will **not** be implemented unless an unresolved ownership conflict formally necessitates an architectural decision.

---

**3. Authoritative Search Separation & MAT-06 Boundary**

The remediation of search is strictly divided into two distinct phases to maintain the **MAT-06** boundary (custom relational JPA vs. HAPI FHIR JPA Server):

```
Goal 3A: Authoritative Search Contract & Cache-Aside Read
   │ (Defines AuthoritativeSearchPort, point READ cache-aside, prohibits remoteCache.values())
   ▼
═════════════════════════════════════════════════════════════════════════════
🛑 STOP BOUNDARY: Formal Architecture Decision for MAT-06
═════════════════════════════════════════════════════════════════════════════
   │
   ▼
Goal 3B: Concrete Authoritative Search Implementation
   │ (Implements search engine behind port using chosen native machinery; removes cache scan)
```

- **Goal 3A (Contract & Boundary)**: Establishes the application-facing search port, type-neutral query models, unit tests proving search is not a cache enumeration, and implements cache-aside point `READ` with read-repair (`BL-09-01`).
- **Goal 3B (Concrete Search Engine)**: Implements `AuthoritativeSearchPort` against Mnemosyne's native search engine and deletes `remoteCache.values()` scanning logic only after an authoritative replacement is operational.

---

**4. Kleio Audit Decoupling Boundary (`MAT-05`)**

- **Domain Separation**: Kleio audit evidence represents immutable security/audit history and is **not** Mnemosyne-managed clinical domain information.
- **Access Boundary**: Remediating `MAT-05` in `iris-befe` must **not** route audit queries through Mneme clinical caches by default.
- **Isolation Strategy**: The application-facing Kleio query boundary (`Iris -> ? -> Kleio`) is treated as a separate architectural problem. Existing `AuditService` and `HarmoniaAuditEventMapper` capabilities will be accessed via a decoupled service boundary, removing `@DataSourceDefinition` and direct PostgreSQL JDBC driver dependencies from `iris-befe`.

---

**5. Domain Contracts vs. HTTP Presentation Semantics**

- **Domain Layer (`calliope`, `mneme`)**: Contracts return domain-level sealed structures (e.g. `WriteResult.Committed`, `WriteResult.ActiveStateConflict`, `GovernedRead<T>`). Domain contracts do not reference HTTP status codes or REST headers.
- **Presentation Layer (`iris-befe`, `pylai`)**: The web/REST layer is solely responsible for mapping domain outcomes to protocol responses:
    - `WriteResult.Committed` -> HTTP `201 Created` (with `Location` and `ETag`) or HTTP `200 OK`.
    - `WriteResult.ActiveStateConflict` / Predecessor Mismatch -> HTTP `409 Conflict`.
    - Domain validation failures -> HTTP `400 Bad Request` or HTTP `422 Unprocessable Entity` with `OperationOutcome`.
- **Version Domains**: `FHIR meta.versionId`, HTTP `ETag`, Mneme active-state tokens, and Mnemosyne authoritative versions are maintained as distinct version domains.

---

**6. Governed Information Lifecycle Semantics (`ADR-020`)**

- Governed resources have no physical `DELETE` semantics.
- Lifecycle transitions (e.g. `status = inactive`, `entered-in-error`, `de-registered`) are modeled as governed authoritative `UPDATE` operations using the domain lifecycle semantics appropriate to that specific information type.
- Presentation endpoints reject HTTP `DELETE` where physical deletion is unsupported by returning HTTP `405 Method Not Allowed` with a structured `OperationOutcome` explaining ADR-020 lifecycle governance.

---

**7. Revised Bounded Implementation Sequence**

```
[Goal 1: Establish/Evolve Mneme Application-Facing Managed-Information Contract]
                                      │
                                      ▼
[Goal 2: Migrate Governed Writes & Enforce ADR-020 Lifecycle in Presentation Tier]
                                      │
                                      ▼
[Goal 3A: Implement Cache-Aside Point READ & Establish Authoritative Search Port]
                                      │
                                      ▼
                    ═════════════════════════════════════
                    🛑 STOP FOR MAT-06 ARCHITECTURE DECISION
                    ═════════════════════════════════════
                                      │
                                      ▼
[Goal 3B: Implement Concrete Authoritative Search & Decommission Cache Scans]
                                      │
                                      ▼
[Goal 4: Resolve Iris -> Kleio Query Coupling (MAT-05 Presentation DB Decoupling)]
                                      │
                                      ▼
[Goal 5: Add & Strengthen Mechanical ArchUnit Architectural Invariant Tests]
```

**Goal 1: Establish/Evolve Mneme Application-Facing Managed-Information Contract**
- **Findings Addressed**: Foundation for `MAT-01`, `MAT-02`.
- **Architectural Invariant Established**: Single application-facing managed-information access boundary (`AX-05`); semantic type authority in Calliope (`AX-06`).
- **Modules Affected**: `calliope`, `hestia/mneme-cluster`.
- **Prerequisites**: Existing Task 08 contracts in `calliope/model/governedwrite`.
- **Scope & Deliverables**:
    - Inspect existing public Mneme/Calliope contracts (`GovernedWriter`, `GovernedRead`, `ResourceKey`) and evolve or encapsulate them into a unified application-facing access boundary.
    - Inspect Calliope canonical model definitions (`ProviderRegistryConstants.SUPPORTED_RESOURCE_TYPES`) to reuse existing semantic type classifications without creating a duplicate registry.
    - Define pure Calliope contracts for point `READ` (`GovernedRead`), governed `WRITE` (`create`, `update`), and `SEARCH` port boundaries.
    - Keep domain contracts strictly decoupled from HTTP presentation semantics (`200`, `201`, `404`, `405`, `409`, `OperationOutcome`).
- **Explicit Stop Condition**: Pure unit tests in `calliope` and `mneme-cluster` verify contracts and type validation without modifying the presentation tier.

**Goal 2: Migrate Governed Writes and Enforce ADR-020 Lifecycle in Presentation Tier**
- **Findings Addressed**: `MAT-01` (Iris BEFE Raw Cache Mutation & Local Version Progression).
- **Architectural Invariant Established**: Active vs. Durable State Separation (`AX-05`); Lifecycle Governance / No Physical Deletion (`ADR-020`).
- **Modules Affected**: `iris/iris-befe`, `calliope`, `hestia/mnemosyne-clinical`.
- **Prerequisites**: Goal 1.
- **Scope & Deliverables**:
    - Migrate `PractitionerResource` and other BEFE clinical endpoints to use the Mneme governed write boundary for `POST` (create) and `PUT` (update).
    - Remove `remoteCache.put()` and local `versionId` increments from `FhirCacheService`.
    - Remove `remoteCache.remove()` and update BEFE `@DELETE` methods to return `405 Method Not Allowed` with structured `OperationOutcome` explaining ADR-020.
    - Retain existing Task 08 coordination and convergence machinery (`HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`, `DefaultGovernedWriter`).
- **Explicit Stop Condition**: BEFE `POST` and `PUT` execute authoritatively through Mnemosyne and converge Infinispan; `DELETE` returns HTTP 405; raw `remoteCache.put` is removed from BEFE write paths.

**Goal 3A: Implement Cache-Aside Point READ and Establish Authoritative Search Port Boundary**
- **Findings Addressed**: `MAT-02` (Foundation), `BL-09-01` (Cache-Aside Read & Provenance Isolation).
- **Architectural Invariant Established**: Authoritative Search Boundary (`ADR-010`); Non-Destructive Active Cache Acceleration (`AX-05`, `AX-11`).
- **Modules Affected**: `hestia/mneme-cluster`, `calliope`, `iris/iris-befe`.
- **Prerequisites**: Goal 1 & Goal 2.
- **Scope & Deliverables**:
    - Implement cache-aside point `READ` logic in Mneme: check active cache -> on miss, fall-through to `AuthoritativePersistencePort.read(key)` -> execute active read-repair via `ActiveStateConvergencePort`.
    - Isolate internal management provenance metadata (`BL-09-01`) before returning domain representations to callers.
    - Define `AuthoritativeSearchPort` interface in `calliope` representing type-neutral authoritative search queries.
    - Explicitly prohibit `remoteCache.values()` as authoritative search semantics in architectural rules.
    - **Explicit Stop Boundary**: STOP before Goal 3B pending the resolution of MAT-06.
- **Explicit Stop Condition**: Point `READ` transparently populates and repairs cold cache; `AuthoritativeSearchPort` contract is defined; execution pauses for MAT-06 decision.

**Goal 3B: Implement Concrete Authoritative Search and Decommission Cache Scans (Post MAT-06)**
- **Findings Addressed**: `MAT-02` (Authoritative Search Completion).
- **Architectural Invariant Established**: Authoritative Native Index Search (`AX-04`, `ADR-010`).
- **Modules Affected**: `hestia/mnemosyne-clinical`, `hestia/mneme-cluster`, `iris/iris-befe`.
- **Prerequisites**: Goal 3A AND formal resolution of **MAT-06** (custom relational JPA vs. HAPI FHIR JPA Server).
- **Scope & Deliverables**:
    - Implement `AuthoritativeSearchPort` using the selected native Mnemosyne search engine.
    - Wire Mneme search delegation to Mnemosyne's native search implementation.
    - Completely decommission and delete `remoteCache.values()` scanning logic from `FhirCacheService`.
- **Explicit Stop Condition**: Clinical searches execute against native database indexes; `remoteCache.values()` is completely deleted from the codebase.

**Goal 4: Resolve Iris -> Kleio Presentation Query Coupling (MAT-05 Boundary)**
- **Findings Addressed**: `MAT-05` (Presentation Database Coupling for Audit Events).
- **Architectural Invariant Established**: Presentation Decoupling (`Invariant 3`, `AX-12`).
- **Modules Affected**: `iris/iris-befe`, `kleio/kleio-persistence`, `kleio/kleio-core`.
- **Prerequisites**: Goals 1–3A.
- **Scope & Deliverables**:
    - Inspect existing `AuditService` query capabilities and runtime boundaries.
    - Resolve the cross-runtime audit query boundary between `iris-befe` and Kleio without routing through Mneme clinical caches and without inventing ad-hoc network services.
    - Remove `AuditDataSourceProducer.java`, `@DataSourceDefinition`, and direct PostgreSQL/JDBC dependencies from `iris-befe`.
- **Explicit Stop Condition**: `AuditEventResource` queries audit events via a decoupled service boundary with zero `@DataSourceDefinition` or direct JDBC drivers in `iris-befe`.

**Goal 5: Add and Strengthen Mechanical ArchUnit Architectural Invariant Tests**
- **Findings Addressed**: `MAT-10` (Mechanical Enforcement of Axioms).
- **Architectural Invariant Established**: Mechanical Enforcement (`AGENTS.md Section 4`, `Section 5`).
- **Modules Affected**: `paradeigma/paradeigma-test`.
- **Prerequisites**: Goals 1–4.
- **Scope & Deliverables**:
    - Add/strengthen ArchUnit tests in `paradeigma-test`:
        1. Asserts zero `RemoteCache.put()`, `RemoteCache.remove()`, or `RemoteCache.values()` calls in presentation/application tiers.
        2. Asserts zero `jakarta.persistence.*`, `org.hibernate.*`, `org.postgresql.*`, or `@DataSourceDefinition` in `iris-befe`.
        3. Asserts zero direct Kleio persistence implementation access from Iris presentation classes.
        4. Asserts zero physical `delete()` methods in governed persistence contracts (`AuthoritativePersistencePort`, `GovernedWriter`).
        5. Asserts all application access to managed information traverses the established Mneme boundary.
- **Explicit Stop Condition**: Full reactor architecture test suite (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) passes 100%.

---

**8. Preserved Findings & Topology Baseline**

1. **Runtime Topology**:
    - `iris-befe` (WildFly EE 10 container).
    - `mneme-cluster` (Infinispan 15 cluster over Hot Rod TCP `11222`).
    - `mnemosyne-clinical` (Spring Boot 3 + HAPI FHIR / PostgreSQL `5432`).
    - `petasos` (ActiveMQ Artemis `61616`).
    - `energeia-ponos` (WildFly Camel runtime `8083`).
    - `pylai-fhir-registry` (Spring Boot 3 inbound gateway `8082`).
2. **Access Paths & Bypasses**: All 8 concrete paths (Path 1 to Path 8) from the initial assessment remain accepted and preserved.
3. **Task 08/09/10 Artefacts**: Retain `GovernedWriter`, `HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`, and `DefaultGovernedWriter`. Supersede legacy `FhirRestCacheStore`.
4. **MAT-06 Isolation**: Preserved strictly unresolved until Goal 3B. No custom persistence or HAPI JPA replacement is selected or designed in this plan.

---

**Discoveries for future steps**
- `calliope/model/governedwrite/GovernedWriter.java` and `calliope/model/registry/ProviderRegistryConstants.java` already provide production contracts and type constants, avoiding the need for redundant abstractions in Goal 1.
- `HotRodActiveStateCoordinator` and `HotRodMnemeConvergence` in `hestia/mneme-cluster` provide complete CAS and coordination logic ready for immediate integration in Goal 2.

Analysis is Ready

**Architectural Synthesis & Revised Remediation Sequence (MAT-01, MAT-02, MAT-05)**

---

**1. Reframed Root Cause Analysis**

The architectural defect across `MAT-01`, `MAT-02`, and `MAT-05` is not specifically the absence of a class named `MnemeClient`.

The fundamental root cause is:
> **"There is no mechanically enforced application-facing Mneme managed-information boundary, causing callers to receive raw infrastructure capabilities and bypass Harmonia information-management semantics."**

Because `iris-befe` was provisioned with raw infrastructure primitives (`RemoteCacheManager` Hot Rod client and container-level `@DataSourceDefinition` JDBC pools) rather than an enforced, governed application boundary:
1. Presentation endpoints defaulted to treating Infinispan `RemoteCache` as a key-value database for point CRUD, inventing uncoordinated writes and local version progressions (`MAT-01`).
2. Search was implemented by naively pulling all cached entries via `remoteCache.values()` into JVM heap memory and performing un-indexed linear scans (`MAT-02`).
3. Presentation queries for audit events were wired directly to PostgreSQL JDBC repositories because no decoupled service-level query port was established (`MAT-05`).

---

**2. Analysis of Existing Contracts & Classification Mechanisms**

**Existing Public Contracts vs. Facade Evolution**  
An inspection of existing public interfaces reveals robust contracts already defined in `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/`:
- `GovernedWriter`: Caller-facing contract exposing pure `create` and `update` operations requiring `ResourceKey`, `GovernedRead`, `WriteResult`, and `ThemisSecurityContext`.
- `ActiveStateCoordinator` & `ActiveStateConvergencePort`: Internal coordination and post-commit cache convergence SPIs.
- `AuthoritativePersistenceService`: Atomic CREATE uniqueness and conditional UPDATE with expected predecessor version checking.

**Architectural Rationale**:  
Rather than inventing an ungrounded or competing abstraction layer, Goal 1 will inspect whether `GovernedWriter` and `GovernedRead` can be directly evolved or encapsulated into a cohesive application-facing access boundary that provides:
1. **Point `READ`**: Cache-aside resolution with authoritative fallback on cache miss and active read-repair (`GovernedRead<T>`).
2. **Governed `WRITE`**: Unified `create` and `update` operations delegating through the governed write pipeline.
3. **Governed `SEARCH`**: Search port delegation prohibiting in-memory cache scans.

**Managed-Type Classification Ownership**
- `calliope/src/main/java/net/fhirfactory/harmonia/model/registry/ProviderRegistryConstants.java` defines the canonical list of supported types (`SUPPORTED_RESOURCE_TYPES` for `Practitioner`, `PractitionerRole`, `Organization`, `Location`, `HealthcareService`, `Endpoint`, `Group`) and validation rules.
- **Architectural Invariant**: Semantic classification (`information type -> managed/governed semantics`) must have **one authoritative owner** (`calliope`). Mneme consumes this domain classification to configure physical caches and runtime behaviour. A parallel, competing `ManagedTypeRegistry` will **not** be implemented unless an unresolved ownership conflict formally necessitates an architectural decision.

---

**3. Authoritative Search Separation & MAT-06 Boundary**

The remediation of search is strictly divided into two distinct phases to maintain the **MAT-06** boundary (custom relational JPA vs. HAPI FHIR JPA Server):

```
Goal 3A: Authoritative Search Contract & Cache-Aside Read
   │ (Defines AuthoritativeSearchPort, point READ cache-aside, prohibits remoteCache.values())
   ▼
═════════════════════════════════════════════════════════════════════════════
🛑 STOP BOUNDARY: Formal Architecture Decision for MAT-06
═════════════════════════════════════════════════════════════════════════════
   │
   ▼
Goal 3B: Concrete Authoritative Search Implementation
   │ (Implements search engine behind port using chosen native machinery; removes cache scan)
```

- **Goal 3A (Contract & Boundary)**: Establishes the application-facing search port, type-neutral query models, unit tests proving search is not a cache enumeration, and implements cache-aside point `READ` with read-repair (`BL-09-01`).
- **Goal 3B (Concrete Search Engine)**: Implements `AuthoritativeSearchPort` against Mnemosyne's native search engine and deletes `remoteCache.values()` scanning logic only after an authoritative replacement is operational.

---

**4. Kleio Audit Decoupling Boundary (`MAT-05`)**

- **Domain Separation**: Kleio audit evidence represents immutable security/audit history and is **not** Mnemosyne-managed clinical domain information.
- **Access Boundary**: Remediating `MAT-05` in `iris-befe` must **not** route audit queries through Mneme clinical caches by default.
- **Isolation Strategy**: The application-facing Kleio query boundary (`Iris -> ? -> Kleio`) is treated as a separate architectural problem. Existing `AuditService` and `HarmoniaAuditEventMapper` capabilities will be accessed via a decoupled service boundary, removing `@DataSourceDefinition` and direct PostgreSQL JDBC driver dependencies from `iris-befe`.

---

**5. Domain Contracts vs. HTTP Presentation Semantics**

- **Domain Layer (`calliope`, `mneme`)**: Contracts return domain-level sealed structures (e.g. `WriteResult.Committed`, `WriteResult.ActiveStateConflict`, `GovernedRead<T>`). Domain contracts do not reference HTTP status codes or REST headers.
- **Presentation Layer (`iris-befe`, `pylai`)**: The web/REST layer is solely responsible for mapping domain outcomes to protocol responses:
    - `WriteResult.Committed` -> HTTP `201 Created` (with `Location` and `ETag`) or HTTP `200 OK`.
    - `WriteResult.ActiveStateConflict` / Predecessor Mismatch -> HTTP `409 Conflict`.
    - Domain validation failures -> HTTP `400 Bad Request` or HTTP `422 Unprocessable Entity` with `OperationOutcome`.
- **Version Domains**: `FHIR meta.versionId`, HTTP `ETag`, Mneme active-state tokens, and Mnemosyne authoritative versions are maintained as distinct version domains.

---

**6. Governed Information Lifecycle Semantics (`ADR-020`)**

- Governed resources have no physical `DELETE` semantics.
- Lifecycle transitions (e.g. `status = inactive`, `entered-in-error`, `de-registered`) are modeled as governed authoritative `UPDATE` operations using the domain lifecycle semantics appropriate to that specific information type.
- Presentation endpoints reject HTTP `DELETE` where physical deletion is unsupported by returning HTTP `405 Method Not Allowed` with a structured `OperationOutcome` explaining ADR-020 lifecycle governance.

---

**7. Revised Bounded Implementation Sequence**

```
[Goal 1: Establish/Evolve Mneme Application-Facing Managed-Information Contract]
                                      │
                                      ▼
[Goal 2: Migrate Governed Writes & Enforce ADR-020 Lifecycle in Presentation Tier]
                                      │
                                      ▼
[Goal 3A: Implement Cache-Aside Point READ & Establish Authoritative Search Port]
                                      │
                                      ▼
                    ═════════════════════════════════════
                    🛑 STOP FOR MAT-06 ARCHITECTURE DECISION
                    ═════════════════════════════════════
                                      │
                                      ▼
[Goal 3B: Implement Concrete Authoritative Search & Decommission Cache Scans]
                                      │
                                      ▼
[Goal 4: Resolve Iris -> Kleio Query Coupling (MAT-05 Presentation DB Decoupling)]
                                      │
                                      ▼
[Goal 5: Add & Strengthen Mechanical ArchUnit Architectural Invariant Tests]
```

**Goal 1: Establish/Evolve Mneme Application-Facing Managed-Information Contract**
- **Findings Addressed**: Foundation for `MAT-01`, `MAT-02`.
- **Architectural Invariant Established**: Single application-facing managed-information access boundary (`AX-05`); semantic type authority in Calliope (`AX-06`).
- **Modules Affected**: `calliope`, `hestia/mneme-cluster`.
- **Prerequisites**: Existing Task 08 contracts in `calliope/model/governedwrite`.
- **Scope & Deliverables**:
    - Inspect existing public Mneme/Calliope contracts (`GovernedWriter`, `GovernedRead`, `ResourceKey`) and evolve or encapsulate them into a unified application-facing access boundary.
    - Inspect Calliope canonical model definitions (`ProviderRegistryConstants.SUPPORTED_RESOURCE_TYPES`) to reuse existing semantic type classifications without creating a duplicate registry.
    - Define pure Calliope contracts for point `READ` (`GovernedRead`), governed `WRITE` (`create`, `update`), and `SEARCH` port boundaries.
    - Keep domain contracts strictly decoupled from HTTP presentation semantics (`200`, `201`, `404`, `405`, `409`, `OperationOutcome`).
- **Explicit Stop Condition**: Pure unit tests in `calliope` and `mneme-cluster` verify contracts and type validation without modifying the presentation tier.

**Goal 2: Migrate Governed Writes and Enforce ADR-020 Lifecycle in Presentation Tier**
- **Findings Addressed**: `MAT-01` (Iris BEFE Raw Cache Mutation & Local Version Progression).
- **Architectural Invariant Established**: Active vs. Durable State Separation (`AX-05`); Lifecycle Governance / No Physical Deletion (`ADR-020`).
- **Modules Affected**: `iris/iris-befe`, `calliope`, `hestia/mnemosyne-clinical`.
- **Prerequisites**: Goal 1.
- **Scope & Deliverables**:
    - Migrate `PractitionerResource` and other BEFE clinical endpoints to use the Mneme governed write boundary for `POST` (create) and `PUT` (update).
    - Remove `remoteCache.put()` and local `versionId` increments from `FhirCacheService`.
    - Remove `remoteCache.remove()` and update BEFE `@DELETE` methods to return `405 Method Not Allowed` with structured `OperationOutcome` explaining ADR-020.
    - Retain existing Task 08 coordination and convergence machinery (`HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`, `DefaultGovernedWriter`).
- **Explicit Stop Condition**: BEFE `POST` and `PUT` execute authoritatively through Mnemosyne and converge Infinispan; `DELETE` returns HTTP 405; raw `remoteCache.put` is removed from BEFE write paths.

**Goal 3A: Implement Cache-Aside Point READ and Establish Authoritative Search Port Boundary**
- **Findings Addressed**: `MAT-02` (Foundation), `BL-09-01` (Cache-Aside Read & Provenance Isolation).
- **Architectural Invariant Established**: Authoritative Search Boundary (`ADR-010`); Non-Destructive Active Cache Acceleration (`AX-05`, `AX-11`).
- **Modules Affected**: `hestia/mneme-cluster`, `calliope`, `iris/iris-befe`.
- **Prerequisites**: Goal 1 & Goal 2.
- **Scope & Deliverables**:
    - Implement cache-aside point `READ` logic in Mneme: check active cache -> on miss, fall-through to `AuthoritativePersistencePort.read(key)` -> execute active read-repair via `ActiveStateConvergencePort`.
    - Isolate internal management provenance metadata (`BL-09-01`) before returning domain representations to callers.
    - Define `AuthoritativeSearchPort` interface in `calliope` representing type-neutral authoritative search queries.
    - Explicitly prohibit `remoteCache.values()` as authoritative search semantics in architectural rules.
    - **Explicit Stop Boundary**: STOP before Goal 3B pending the resolution of MAT-06.
- **Explicit Stop Condition**: Point `READ` transparently populates and repairs cold cache; `AuthoritativeSearchPort` contract is defined; execution pauses for MAT-06 decision.

**Goal 3B: Implement Concrete Authoritative Search and Decommission Cache Scans (Post MAT-06)**
- **Findings Addressed**: `MAT-02` (Authoritative Search Completion).
- **Architectural Invariant Established**: Authoritative Native Index Search (`AX-04`, `ADR-010`).
- **Modules Affected**: `hestia/mnemosyne-clinical`, `hestia/mneme-cluster`, `iris/iris-befe`.
- **Prerequisites**: Goal 3A AND formal resolution of **MAT-06** (custom relational JPA vs. HAPI FHIR JPA Server).
- **Scope & Deliverables**:
    - Implement `AuthoritativeSearchPort` using the selected native Mnemosyne search engine.
    - Wire Mneme search delegation to Mnemosyne's native search implementation.
    - Completely decommission and delete `remoteCache.values()` scanning logic from `FhirCacheService`.
- **Explicit Stop Condition**: Clinical searches execute against native database indexes; `remoteCache.values()` is completely deleted from the codebase.

**Goal 4: Resolve Iris -> Kleio Presentation Query Coupling (MAT-05 Boundary)**
- **Findings Addressed**: `MAT-05` (Presentation Database Coupling for Audit Events).
- **Architectural Invariant Established**: Presentation Decoupling (`Invariant 3`, `AX-12`).
- **Modules Affected**: `iris/iris-befe`, `kleio/kleio-persistence`, `kleio/kleio-core`.
- **Prerequisites**: Goals 1–3A.
- **Scope & Deliverables**:
    - Inspect existing `AuditService` query capabilities and runtime boundaries.
    - Resolve the cross-runtime audit query boundary between `iris-befe` and Kleio without routing through Mneme clinical caches and without inventing ad-hoc network services.
    - Remove `AuditDataSourceProducer.java`, `@DataSourceDefinition`, and direct PostgreSQL/JDBC dependencies from `iris-befe`.
- **Explicit Stop Condition**: `AuditEventResource` queries audit events via a decoupled service boundary with zero `@DataSourceDefinition` or direct JDBC drivers in `iris-befe`.

**Goal 5: Add and Strengthen Mechanical ArchUnit Architectural Invariant Tests**
- **Findings Addressed**: `MAT-10` (Mechanical Enforcement of Axioms).
- **Architectural Invariant Established**: Mechanical Enforcement (`AGENTS.md Section 4`, `Section 5`).
- **Modules Affected**: `paradeigma/paradeigma-test`.
- **Prerequisites**: Goals 1–4.
- **Scope & Deliverables**:
    - Add/strengthen ArchUnit tests in `paradeigma-test`:
        1. Asserts zero `RemoteCache.put()`, `RemoteCache.remove()`, or `RemoteCache.values()` calls in presentation/application tiers.
        2. Asserts zero `jakarta.persistence.*`, `org.hibernate.*`, `org.postgresql.*`, or `@DataSourceDefinition` in `iris-befe`.
        3. Asserts zero direct Kleio persistence implementation access from Iris presentation classes.
        4. Asserts zero physical `delete()` methods in governed persistence contracts (`AuthoritativePersistencePort`, `GovernedWriter`).
        5. Asserts all application access to managed information traverses the established Mneme boundary.
- **Explicit Stop Condition**: Full reactor architecture test suite (`mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`) passes 100%.

---

**8. Preserved Findings & Topology Baseline**

1. **Runtime Topology**:
    - `iris-befe` (WildFly EE 10 container).
    - `mneme-cluster` (Infinispan 15 cluster over Hot Rod TCP `11222`).
    - `mnemosyne-clinical` (Spring Boot 3 + HAPI FHIR / PostgreSQL `5432`).
    - `petasos` (ActiveMQ Artemis `61616`).
    - `energeia-ponos` (WildFly Camel runtime `8083`).
    - `pylai-fhir-registry` (Spring Boot 3 inbound gateway `8082`).
2. **Access Paths & Bypasses**: All 8 concrete paths (Path 1 to Path 8) from the initial assessment remain accepted and preserved.
3. **Task 08/09/10 Artefacts**: Retain `GovernedWriter`, `HotRodActiveStateCoordinator`, `HotRodMnemeConvergence`, and `DefaultGovernedWriter`. Supersede legacy `FhirRestCacheStore`.
4. **MAT-06 Isolation**: Preserved strictly unresolved until Goal 3B. No custom persistence or HAPI JPA replacement is selected or designed in this plan.

---

**Discoveries for future steps**
- `calliope/model/governedwrite/GovernedWriter.java` and `calliope/model/registry/ProviderRegistryConstants.java` already provide production contracts and type constants, avoiding the need for redundant abstractions in Goal 1.
- `HotRodActiveStateCoordinator` and `HotRodMnemeConvergence` in `hestia/mneme-cluster` provide complete CAS and coordination logic ready for immediate integration in Goal 2.



**Summary**
- Revised the remediation sequence as a read-only architectural assessment: the core defect is the absence of a mechanically enforced application-facing Mneme managed-information boundary, not specifically the absence of a `MnemeClient` class.
- Preserved the accepted runtime topology, access-path analysis, Task 08 coordination/convergence findings, cache-store supersession, and MAT-06 isolation.

**Changes**
- Reframed Goal 1 to inspect and evolve existing public contracts (`GovernedWriter`, `GovernedRead`, `ResourceKey`, existing Calliope classification evidence) before introducing any new facade or registry; no new `ManagedTypeRegistry` is assumed.
- Split search remediation into Goal 3A (cache-aside point READ + authoritative search contract/boundary only) and Goal 3B (concrete authoritative search implementation after MAT-06), with an explicit stop boundary between them.
- Kept Kleio audit decoupling separate from Mneme/Mnemosyne clinical flows in Goal 4, and kept HTTP/FHIR status mapping in presentation boundaries rather than inside Mneme domain contracts.

**Verification**
- Validated the revision against repository evidence already inspected in `calliope`, `hestia`, `kleio`, and the prior assessment artefacts; no code changes were made.

**Notes**
- Revised bounded sequence for review: Goal 1 contract/classification boundary, Goal 2 governed writes + lifecycle semantics, Goal 3A read + search contract, STOP for MAT-06, Goal 3B authoritative search, Goal 4 Iris->Kleio decoupling, Goal 5 ArchUnit enforcement.

