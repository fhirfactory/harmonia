Continuing **Plan** Mode



Now I'm examining the main Java files to find where the getCache method is called, aiming to understand how caching is currently handled in the code.

**Requirements**

**Overview & Goals**  
Step 08.04D designs the production composition of the governed-write capabilities established in Steps 08.04A (Caller-facing Contracts), 08.04B (Mneme Active-State Coordination), and 08.04C (Mnemosyne Atomic Authoritative Persistence).

The core objective is to compose:
1. **Themis Security Authorization** (`ThemisAuthorizer`, `ThemisSecurityContext`)
2. **Mneme Active-State Coordination** (`ActiveStateCoordinator.consume`)
3. **Mnemosyne Authoritative Persistence** (`AuthoritativePersistencePort.create` / `update`)
4. **Guarded Mneme Post-Commit Convergence** (`ActiveStateConvergencePort.converge`)

into a single unified, deterministic `DefaultGovernedWriter` implementation supporting `CREATE` and `UPDATE` operations while upholding the fundamental architectural baselines (ADR-018, ADR-019, ADR-020).

**Scope**
- **In Scope**:
    - Architectural placement of `DefaultGovernedWriter` composition in existing runtime module `hestia/mnemosyne-clinical` (preserving `calliope` as a pure contract/model library and preserving `AuthoritativePersistencePort` in `mnemosyne-clinical`).
    - Explicit execution sequence for governed `UPDATE` and governed `CREATE`.
    - Formal architectural decision on `CREATE` active-state coordination vs direct authoritative creation with post-commit convergence.
    - Themis authorization enforcement point (strictly evaluated *before* active token consumption and persistence).
    - Complete failure and outcome mapping matrix covering all combinations of Themis, Mneme, and Mnemosyne results.
    - Post-commit Mneme convergence CAS-loop algorithm with explicit authoritative-version provenance metadata and strict newer-version protection (preventing older commits from overwriting or invalidating newer cached versions).
    - Precise semantics for `ConvergenceStatus.CONVERGED` ("no further cache action required") and `ConvergenceStatus.DEGRADED`.
    - Truthful preservation of `UNKNOWN` commit outcomes without automatic retries or token rollbacks.
    - Comprehensive unit and scenario test plans covering all concurrency, authorization, coordination, persistence, and convergence states.
    - ArchUnit architectural rules enforcing module isolation, zero cyclic dependencies, and zero physical DELETE semantics.
    - Catalogue of existing bypasses and explicit boundary definition with Task 09 (Cache-aside point reads).

- **Out of Scope**:
    - Full caller migration across BEFE, Pylai, and Energeia (deferred to subsequent integration steps).
    - Implementation of physical DELETE operations (strictly prohibited by ADR-020).
    - Implementation of Task 09 cache-aside point reads and read repairs.
    - Implementation of automated reconciliation machinery for `UNKNOWN` outcomes (deferred to future reconciliation task).
    - Creation of redundant Maven modules (`hestia-governance`, `clinical-write-orchestrator-service`).

**User Stories**
- **As a Clinical Gateway or BEFE Resource Provider**, I want to execute governed updates through a unified `GovernedWriter` that validates Themis authorization, consumes the active state token in Mneme, and conditionally commits to Mnemosyne, so that lost updates and unauthorized modifications are prevented.
- **As a System Administrator**, I want authoritative writes that succeed in Mnemosyne to remain committed even if transient cache convergence fails (returning `Committed` with `DEGRADED` convergence), so that durable persistence is never rolled back due to ephemeral cache grid issues.
- **As a Security Auditor**, I want all write operations to evaluate Themis policies before consuming coordination tokens or touching durable databases, so that unauthorized requests are rejected cleanly at the gate.

**Functional Requirements**
1. **Governed UPDATE Execution**:
    - Accepts `GovernedRead<T> current`, proposed state `T proposed`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately without touching Mneme or Mnemosyne.
    - Consumes observed `ActiveStateToken` via `ActiveStateCoordinator.consume`:
        - If `STALE`: aborts immediately and returns `WriteResult.ActiveConflict`.
        - If `UNAVAILABLE`: aborts immediately and returns `WriteResult.NotCommitted` (fail-fast, zero local fallback).
    - Executes authoritative persistence via `AuthoritativePersistencePort.update`:
        - If `Conflict(EXPECTED_VERSION_MISMATCH)`: returns `WriteResult.AuthoritativeConflict` (token remains consumed).
        - If `NotCommitted`: returns `WriteResult.NotCommitted` (token remains consumed).
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown` (token remains consumed, no retry).
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
2. **Governed CREATE Execution**:
    - Accepts `ResourceKey key`, initial state `T resource`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately.
    - Executes authoritative persistence via `AuthoritativePersistencePort.create` (Mnemosyne atomically enforces absence precondition via unique constraints):
        - If `Conflict(RESOURCE_ALREADY_EXISTS)`: returns `WriteResult.AuthoritativeConflict`.
        - If `NotCommitted`: returns `WriteResult.NotCommitted`.
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown`.
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
3. **No Physical DELETE**:
    - `GovernedWriter` exposes only `create` and `update`. All lifecycle transitions (e.g. deprecation, deactivation, suspension) are authoritative `update` operations with FHIR status codes.

**Non-Functional Requirements**
- **Independence & Isolation**: Mnemosyne persistence must not depend on Mneme cache. Mneme cache loss must not affect durable database correctness.
- **Zero Local Fallback**: Distributed cache outages must produce visible failures (`UNAVAILABLE` / `DEGRADED`), never silent fallbacks to JVM-local synchronization maps.
- **Distinct Version Domains**: The four version domains (`ActiveStateToken`, `AuthoritativeVersion`, `FHIR meta.versionId`, `HTTP ETag`) must remain strictly isolated. No cross-domain version assumptions or synthetic versions.
- **Deterministic Outcome Mapping**: Every combination of underlying outcomes must map unambiguously into the sealed `WriteResult<T>` hierarchy.

**Technical Design**

**Current Implementation & Baseline Artifacts**
- **Caller-Facing Contracts (`calliope`)**:
    - `GovernedWriter`: Interface defining `<T> WriteResult<T> create(...)` and `<T> WriteResult<T> update(...)`.
    - `GovernedRead<T>`: Record packaging `ResourceKey`, payload `T`, `ActiveStateToken`, and `AuthoritativeVersion`.
    - `WriteResult<T>`: Sealed interface with records `Committed<T>`, `ActiveConflict<T>`, `AuthoritativeConflict<T>`, `OutcomeUnknown<T>`, and `NotCommitted<T>`.
    - `ActiveStateCoordinator`: Interface defining `observe(ResourceKey)` and `consume(ResourceKey, ActiveStateToken)`.
    - `ActiveStateConvergencePort`: Domain port interface defining `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
    - `ConvergenceStatus`: Sealed hierarchy / enum (`CONVERGED`, `DEGRADED`).
- **Mneme Active-State Coordination (`hestia/mneme-cluster`)**:
    - `HotRodActiveStateCoordinator`: Implements `ActiveStateCoordinator` using Hot Rod `replaceWithVersion` against `active-coordination-cache`.
- **Mnemosyne Authoritative Persistence (`hestia/mnemosyne-clinical`)**:
    - `AuthoritativePersistencePort<T>`: Port interface defining `create` and `update` (remains in `mnemosyne-clinical`).
    - `AuthoritativePersistenceService`: Implements `AuthoritativePersistencePort<IBaseResource>` using `TransactionTemplate` and conditional SQL updates.
    - `AuthoritativePersistenceResult<T>`: Sealed interface with `Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown` (remains in `mnemosyne-clinical`).
- **Themis Policy Evaluation (`themis/themis-api` & `themis/themis-core`)**:
    - `ThemisAuthorizer`: Evaluates `ThemisAuthorizationRequest` and returns `ThemisAuthorizationDecision`.
    - `ThemisSecurityContext`: Immutable context carrying originating/executing principals, authorities, correlation, and causation IDs.

---

**Key Architectural Decisions**

**Decision 1: Module Placement of GovernedWriter Composition**
- **Chosen Approach**: Place the production `DefaultGovernedWriter` implementation in `hestia/mnemosyne-clinical` (under `net.fhirfactory.harmonia.hapifhir.governed`), preserving `AuthoritativePersistencePort` and `AuthoritativePersistenceResult` in `mnemosyne-clinical`. `calliope` remains strictly a library for canonical models and domain contracts (`GovernedWriter`, `GovernedRead`, `WriteResult`, `ActiveStateCoordinator`, `ActiveStateConvergencePort`, `AuthoritativeVersion`, `ConvergenceStatus`). Hot Rod convergence logic (`HotRodMnemeConvergence`) is placed in `hestia/mneme-cluster`.
- **Rationale**:
    - `calliope` owns canonical schemas and semantic governance. It must never become an application runtime service orchestrator.
    - `hestia/mnemosyne-clinical` is the smallest existing runtime module that already depends on `calliope`, `themis-api`, and `themis-core`, and houses the clinical persistence implementations.
    - `DefaultGovernedWriter` interacts solely with domain interfaces (`ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, `ActiveStateConvergencePort`), without depending on Infinispan/Hot Rod concrete classes.
    - This avoids creating redundant Maven modules (e.g. `hestia-governance`), avoids dependency cycles, avoids leaking JPA/Infinispan into Calliope, and keeps contracts pure.

**Decision 2: Themis Authorization Precedence**
- **Chosen Approach**: Evaluate Themis authorization as the very first step in both `create` and `update`, strictly *before* consuming the `ActiveStateToken` in Mneme and *before* invoking Mnemosyne.
- **Rationale**: An unauthorized caller must not mutate active coordination state, burn ActiveStateTokens, or trigger database queries.

**Decision 3: CREATE Precondition Model & Active-State Coordination Evaluation**
- **Evaluation of Alternatives**:
    - **Option A (Chosen): Direct Authoritative CREATE with Post-Commit Convergence**
        - `Themis -> Mnemosyne CREATE -> Mneme Post-Commit Convergence`
    - **Option B (Rejected): Pre-Persistence Mneme Cold-State Coordination**
        - `Themis -> Mneme cold-state coordination -> Mnemosyne CREATE -> Mneme convergence`
- **Architectural Rationale & Invariant**:
    - An `ActiveStateToken` represents an observed point-in-time active state of an *existing* resource. For a newly created resource, no prior active state exists to observe.
    - Mnemosyne exclusively defines the durable authoritative state boundary (ADR-018) and owns the authoritative *absence* precondition (enforced via database unique constraints on `[resource_type, fhir_id]`).
    - Option B would require reserving a speculative token or placeholder marker in Mneme for an absent resource prior to database insertion. If Mnemosyne persistence subsequently fails, or if Mneme partitions/restarts, this creates phantom reservations, requires token rollback machinery (violating the fundamental invariant that consumed tokens are never rolled back), and creates distributed dual-master race conditions between cache and database.
    - **Invariant**: *Mnemosyne exclusively owns the authoritative absence precondition. CREATE operations deliberately do not perform pre-persistence active-state coordination in Mneme, and instead seed Mneme active state via post-commit convergence upon successful authoritative creation ($V=1$).*
    - No synthetic authoritative version, lease, or JVM lock is invented.

**Decision 4: Guarded Mneme Post-Commit Convergence with Distinct Version Domains**
- **Version Domain Separation**:
    1. `ActiveStateToken`: Opaque Hot Rod entry version CAS token for active-state coordination.
    2. `AuthoritativeVersion`: Mnemosyne durable database sequence version ($1, 2, 3\dots$).
    3. FHIR `Resource.meta.versionId`: FHIR specification payload field (string).
    4. HTTP ETag / `If-Match`: HTTP transport concurrency header.
    - *Invariant*: Convergence must never assume `FHIR meta.versionId == AuthoritativeVersion`.
- **Authoritative Provenance in Mneme**:
    - Post-commit convergence compares the incoming `AuthoritativeVersion` against explicit metadata describing the authoritative version represented by the cached Mneme value.
    - **Cache Contract Analysis & Chosen Representation**:
        - *Investigation of Existing Caches*: All existing clinical caches (`person-cache`, `task-cache`, `organization-cache`, etc.) store raw FHIR JSON `String`. Existing readers (`FhirCacheService`, `TaskCacheService`, `DefaultTaskService`) parse cached values directly using `FhirContext.newJsonParser().parseResource(...)`.
        - *Incompatibility of Outer JSON Envelope*: An outer wrapper (`{ authoritativeVersion: ..., resource: ... }`) is invalid FHIR JSON and would cause `DataFormatException` in all existing readers, violating the constraint not to migrate all cache consumers in 08.04D.
        - *Chosen Smallest Compatible Representation (`MnemeCachedResource`)*: Authoritative version provenance is embedded directly inside the standard FHIR resource under `Resource.meta.extension` using canonical URI `http://harmonia.fhirfactory.net/structure/authoritative-version` (with `Integer64Type` or numeric representation).
        - *Backwards Compatibility & Atomicity*: The cached value remains 100% valid raw FHIR JSON. HAPI FHIR parsers across all existing consumers parse it seamlessly without schema errors. The resource payload and its authoritative provenance are read and written atomically in a single Infinispan cache entry, eliminating dual-cache consistency hazards.
        - *Legacy Cache Entries*: Any pre-existing cache entry lacking the authoritative-version extension is treated as $V_{cached} = 0$, allowing governed writes to safely converge over legacy entries.
    - Infinispan `MetadataValue.getVersion()` remains strictly an opaque CAS token for `replaceWithVersion`.
- **Convergence Semantics**:
    - `ConvergenceStatus.CONVERGED` explicitly means **"no further cache action is required"** (the cache already reflects this authoritative version or a newer authoritative progression), NOT "the supplied committed representation was written to cache".
    - If the cached representation is newer or equal ($V_{cached} \ge V_{committed}$), convergence immediately succeeds with `CONVERGED` without overwriting the newer state.
    - If the cached representation is older ($V_{cached} < V_{committed}$), conditional replacement is attempted using the opaque Hot Rod version token (`replaceWithVersion`).
    - If the cache entry is absent (cold cache), `putIfAbsent` is executed.
    - If CAS fails or retries are exhausted under contention/outage, `ConvergenceStatus.DEGRADED` is returned without unconditionally evicting the cache entry.

---

**Composed Architecture & Sequence Flows**

```mermaid
graph TD
  Caller[Caller: BEFE / Pylai / Erga] -->|GovernedRead + Proposed State| GW[DefaultGovernedWriter in mnemosyne-clinical]
  GW -->|1. Authorize Action| Themis[ThemisAuthorizer]
  Themis -->|Allow / Deny| GW
  GW -->|2. Consume Token (UPDATE only)| MnemeCoord[Mneme: ActiveStateCoordinator]
  MnemeCoord -->|CONSUMED / STALE / UNAVAILABLE| GW
  GW -->|3. Atomic Persistence| Mnemosyne[Mnemosyne: AuthoritativePersistencePort]
  Mnemosyne -->|COMMITTED / CONFLICT / UNKNOWN| GW
  GW -->|4. Guarded CAS Convergence| MnemeConv[Mneme: ActiveStateConvergencePort]
  MnemeConv -->|CONVERGED / DEGRADED| GW
  GW -->|WriteResult<T>| Caller
```

**UPDATE Execution Flow**
1. **Validate Input**: Check non-null `GovernedRead`, `proposed`, `securityContext`, and matching resource types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.UPDATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Active-State Progression (Mneme)**:
    - Call `coordinator.consume(current.key(), current.activeToken())`.
    - If `STALE`: return `WriteResult.activeStateConflict(ActiveStateConflict.of(key, "Active state token is stale"))`.
    - If `UNAVAILABLE`: return `WriteResult.notCommitted(key, "Active state coordination unavailable")`.
4. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.update(current.key(), proposed, current.expectedAuthoritativeVersion())`.
    - If `Conflict(EXPECTED_VERSION_MISMATCH)`: return `WriteResult.authoritativeConflict(conflict.conflict())`. (Token remains consumed).
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`. (Token remains consumed).
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`. (Token remains consumed, no retry).
5. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, newVersion)`: call `convergencePort.converge(key, resource, newVersion)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, newVersion)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, newVersion, "Mneme cache convergence degraded")`.

**CREATE Execution Flow**
1. **Validate Input**: Check non-null `ResourceKey`, `resource`, `securityContext`, and matching types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.CREATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.create(key, resource)`.
    - If `Conflict(RESOURCE_ALREADY_EXISTS)`: return `WriteResult.authoritativeConflict(conflict.conflict())`.
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`.
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`.
4. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, v1)`: call `convergencePort.converge(key, resource, v1)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, v1)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, v1, "Mneme cache convergence degraded")`.

---

**Failure and Outcome Mapping Matrix**

| Scenario / Condition | Themis Decision | Mneme Coordination | Mnemosyne Persistence | Mneme Convergence | WriteResult Variant | Token Consumed? | Database State |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **A. Themis Denies** | `DENY` | Not Called | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **B. Mneme Stale** | `ALLOW` | `STALE` | Not Called | Not Called | `WriteResult.ActiveConflict` | No | Unchanged |
| **C. Mneme Unavailable** | `ALLOW` | `UNAVAILABLE` | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **D. Authoritative Conflict** | `ALLOW` | `CONSUMED` | `Conflict(VERSION_MISMATCH)` | Not Called | `WriteResult.AuthoritativeConflict` | Yes | Unchanged |
| **E. Persistence Not Committed** | `ALLOW` | `CONSUMED` | `NotCommitted` | Not Called | `WriteResult.NotCommitted` | Yes | Unchanged |
| **F. Persistence Unknown** | `ALLOW` | `CONSUMED` | `OutcomeUnknown` | Not Called | `WriteResult.OutcomeUnknown` | Yes | Ambiguous (No Retry) |
| **G. Committed + Converged** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `CONVERGED` | `WriteResult.Committed(CONVERGED)` | Yes | Committed ($V$) |
| **H. Committed + Degraded** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `DEGRADED` | `WriteResult.Committed(DEGRADED)` | Yes | Committed ($V$) |
| **I. CREATE Duplicate** | `ALLOW` | N/A | `Conflict(ALREADY_EXISTS)` | Not Called | `WriteResult.AuthoritativeConflict` | N/A | Unchanged |

---

**Post-Commit Mneme Convergence Algorithm**

```java
public class HotRodMnemeConvergence implements ActiveStateConvergencePort {
    public static final String AUTHORITATIVE_VERSION_EXT_URL = "http://harmonia.fhirfactory.net/structure/authoritative-version";

    private final RemoteCacheManager cacheManager;
    private final FhirContext fhirContext;

    @Override
    public <T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion) {
        String cacheName = resolveCacheName(key.resourceType());
        RemoteCache<String, String> cache = cacheManager.getCache(cacheName);
        if (cache == null) return ConvergenceStatus.DEGRADED;

        // Attach explicit authoritative-version provenance into FHIR meta.extension
        IBaseResource fhirResource = (IBaseResource) committedResource;
        MnemeCachedResource.attachAuthoritativeVersion(fhirResource, committedVersion);
        String payloadJson = fhirContext.newJsonParser().encodeResourceToString(fhirResource);
        long targetVer = committedVersion.value();

        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                MetadataValue<String> currentMeta = cache.getWithMetadata(key.id());
                if (currentMeta != null) {
                    long cachedVer = MnemeCachedResource.extractAuthoritativeVersion(currentMeta.getValue());
                    // Newer-Version Invariant: never overwrite newer cache state.
                    // CONVERGED means no further cache action is required.
                    if (cachedVer >= targetVer) {
                        return ConvergenceStatus.CONVERGED;
                    }
                    // Hot Rod opaque CAS token replacement
                    boolean casSuccess = cache.replaceWithVersion(key.id(), payloadJson, currentMeta.getVersion());
                    if (casSuccess) return ConvergenceStatus.CONVERGED;
                } else {
                    String existing = cache.putIfAbsent(key.id(), payloadJson);
                    if (existing == null) return ConvergenceStatus.CONVERGED;
                }
            } catch (Exception e) {
                return ConvergenceStatus.DEGRADED;
            }
        }
        // CAS exhausted: DO NOT evict entry (preserves newer versions)
        return ConvergenceStatus.DEGRADED;
    }
}
```

---

**Dependency Graph**

```
calliope (contracts: GovernedWriter, GovernedRead, WriteResult, ActiveStateCoordinator,
          ActiveStateConvergencePort, AuthoritativeVersion, ConvergenceStatus)
  └── depends on: themis-api (ThemisAuthorizer, ThemisSecurityContext)
  └── depends on: hapi-fhir-structures-r5

hestia/mneme-cluster (implementations: HotRodActiveStateCoordinator, HotRodMnemeConvergence)
  └── depends on: calliope, infinispan-client-hotrod

hestia/mnemosyne-clinical (persistence & composition: AuthoritativePersistencePort, AuthoritativePersistenceService, DefaultGovernedWriter)
  └── depends on: calliope, themis-api, themis-core, spring-boot-starter-data-jpa

iris/iris-befe (presentation gateway)
  └── depends on: calliope, themis-api, themis-core, hestia/mneme-cluster
```

**Graph Properties**:
- Purely unidirectional.
- Zero cyclic dependencies.
- No JPA, Hibernate, or Infinispan types leaked into `calliope` or `GovernedWriter` API.
- `DefaultGovernedWriter` lives in `mnemosyne-clinical` as a runtime orchestrator.

---

**Concrete File Structure Changes**

**Files to Add:**
1. `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ActiveStateConvergencePort.java` (Domain convergence port interface)
2. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/MnemeCachedResource.java` (Helper to attach/extract `AuthoritativeVersion` provenance via standard FHIR `Resource.meta.extension`)
3. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergence.java` (Hot Rod CAS convergence implementation)
4. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriter.java` (Production orchestrator in runtime module)
5. `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriterTest.java` (Comprehensive composition unit tests)
6. `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergenceTest.java` (Convergence CAS & newer-version tests)
7. `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteCompositionArchitectureTest.java` (ArchUnit rules)

**Files Preserved Without Relocation:**
1. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java` (Remains in `mnemosyne-clinical`)
2. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/model/AuthoritativePersistenceResult.java` (Remains in `mnemosyne-clinical`)
3. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java` (Remains in `mnemosyne-clinical`)

**Testing**

**Validation Approach**  
Verification of Step 08.04D composition will be performed using a multi-tier testing strategy:
1. **Composition Unit Tests (`DefaultGovernedWriterTest`)**:
    - Uses Mockito-based test doubles for `ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, and `ActiveStateConvergencePort`.
    - Validates all 15 permutations of the Failure and Outcome Mapping Matrix.
2. **Convergence CAS Unit & Scenario Tests (`HotRodMnemeConvergenceTest`)**:
    - Validates the newer-version protection invariant ($V_{cached} \ge V_{committed}$ is preserved).
    - Validates cold-cache population via `putIfAbsent`.
    - Validates CAS loss handling and non-eviction on retry exhaustion.
3. **ArchUnit Architecture Tests (`GovernedWriteCompositionArchitectureTest`)**:
    - Enforces package layering, zero dependency cycles, and zero JPA/Infinispan leaks into `calliope`.

---

**Key Scenarios**

**Scenario 1: Authorised UPDATE Happy Path**
- **Setup**: `GovernedRead` with V1 and token `T1`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne commits V2 $\to$ Convergence succeeds with V2.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.CONVERGED`, version `AuthoritativeVersion.of(2L)`.

**Scenario 2: Themis Denied**
- **Setup**: `GovernedRead` with V1. Themis mock configured to return `DENY`.
- **Flow**: Themis denies $\to$ write immediately terminates.
- **Outcome**: `WriteResult.NotCommitted`. Zero invocations on `ActiveStateCoordinator` and `AuthoritativePersistencePort`.

**Scenario 3: Stale ActiveStateToken**
- **Setup**: `GovernedRead` with stale token `T_stale`.
- **Flow**: Themis allows $\to$ Mneme returns `STALE` $\to$ write immediately terminates.
- **Outcome**: `WriteResult.ActiveConflict`. Zero invocations on `AuthoritativePersistencePort`.

**Scenario 4: Mneme Unavailable**
- **Setup**: `GovernedRead` with V1. `ActiveStateCoordinator` returns `UNAVAILABLE`.
- **Flow**: Themis allows $\to$ Mneme returns `UNAVAILABLE` $\to$ write terminates with fail-fast visible error.
- **Outcome**: `WriteResult.NotCommitted` (fail-fast, no JVM-local fallback).

**Scenario 5: Authoritative Precondition Conflict (Expected Version Mismatch)**
- **Setup**: `GovernedRead` with V1. Mnemosyne returns `Conflict(EXPECTED_VERSION_MISMATCH, current=V3)`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne returns Conflict.
- **Outcome**: `WriteResult.AuthoritativeConflict(EXPECTED_VERSION_MISMATCH)`. Token `T1` remains consumed.

**Scenario 6: Authoritative Persistence Unknown**
- **Setup**: Mnemosyne returns `OutcomeUnknown("Commit ACK timeout")`.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne returns `OutcomeUnknown`.
- **Outcome**: `WriteResult.OutcomeUnknown`. Token remains consumed. No automatic retry attempted.

**Scenario 7: Committed with Degraded Convergence**
- **Setup**: Mnemosyne commits V2, but Mneme convergence throws timeout exception.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne commits V2 $\to$ Convergence returns `DEGRADED`.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.DEGRADED`. Durable commit remains intact.

**Scenario 8: Delayed Older Convergence Preserves Newer Cache State**
- **Setup**: Cache currently contains V43. Delayed convergence thread attempts to converge committed V42.
- **Flow**: `HotRodMnemeConvergence.converge` reads cached V43 $\to$ detects $43 \ge 42 \to$ terminates immediately with `CONVERGED`.
- **Outcome**: Cache remains unchanged at V43. V42 does not overwrite or evict V43.

**Scenario 9: Authorised CREATE Happy Path & Duplicate Detection**
- **Setup**: Proposed new resource `Patient/100`.
- **Flow A**: Themis allows $\to$ Mnemosyne creates V1 $\to$ Convergence populates cache with V1 $\to$ returns `WriteResult.Committed(V1)`.
- **Flow B**: Concurrent CREATE with same key $\to$ Mnemosyne unique constraint triggers $\to$ returns `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)`.

---

**Architecture Guardrails to Add**
- `GovernedWriteCompositionArchitectureTest`:
    1. `GovernedWriter` resides in `calliope` and `DefaultGovernedWriter` resides in `hestia/mnemosyne-clinical`.
    2. `calliope` must have zero dependencies on `hestia`, `iris`, `pylai`, `energeia`, `agora`, or `paradeigma`.
    3. `DefaultGovernedWriter` must not import or depend on `org.infinispan..`, `jakarta.persistence..`, `org.hibernate..`, or `ca.uhn.fhir.jpa..`.
    4. `AuthoritativePersistencePort` and `ActiveStateCoordinator` must not expose DELETE, REMOVE, or PURGE methods (ADR-020).
    5. `HotRodMnemeConvergence` must not depend on `AuthoritativePersistencePort` or JPA entities.

**Bypasses & Deferred Work**

**Catalogue of Remaining Bypasses (Post-08.04D)**

While Step 08.04D establishes the complete production governed-write composition, existing runtime components still contain legacy write-path bypasses that will be migrated in subsequent steps:

| Subsystem / Component | File / Location | Legacy Behavior | Target Migration Step |
| :--- | :--- | :--- | :--- |
| **Iris BEFE** | `FhirCacheService.java` | Unconditional `remoteCache.put(id, json)` and `remoteCache.remove(id)` | Task 08.05 / 08.06 (BEFE Governed Writer Migration) |
| **Mneme Persistence** | `FhirRestCacheStore.java` | Asynchronous write-behind store bypassing governed coordination | Task 08.07 (Write-Behind Decommissioning) |
| **Mnemosyne Clinical** | HAPI Resource Providers | Direct `FhirStorageService.createResource/updateResource` | Task 08.05 / Task 09 |
| **Energeia Ponos** | `ErgonBase.java` / `PraxisService.java` | Direct cache updates for task synchronization | Task 08.06 (Workflow Task Governed Migration) |
| **Pylai MLLP** | `pylai-mllp-in` / `pylai-fhir-provider-registry` | Direct cache and storage mutations | Task 08.06 (Gateway Governed Migration) |

---

**Explicitly Deferred Work**
1. **Task 09 (Cache-Aside Point Reads)**: Governed point reads (`GovernedRead<T>`), cache-aside read-through, and read repairs will be designed and implemented in Task 09.
2. **UNKNOWN Outcome Reconciliation Framework**: Automated asynchronous reconciliation of `WriteResult.OutcomeUnknown` is deferred to a dedicated post-Task 08 reconciliation capability.
3. **Legacy Caller Migration**: Modifying BEFE REST endpoints, Pylai gateways, and Ponos routes to call `GovernedWriter` will occur in follow-up integration steps.

**Delivery Steps**

**Step 1: Define Governed-Write Convergence Contract in Calliope**  
Define pure convergence port contract in Calliope while preserving persistence ports in Mnemosyne Clinical.

- Define `ActiveStateConvergencePort` in `calliope` (`net.fhirfactory.harmonia.model.governedwrite`) with method `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
- Verify `AuthoritativePersistencePort<T>` and `AuthoritativePersistenceResult<T>` remain in `hestia/mnemosyne-clinical` without leaking into Calliope.
- Add unit tests in `calliope` verifying contract invariants, immutability, and null safety.

**Step 2: Implement Guarded Mneme Hot Rod Convergence in hestia-mneme-cluster**  
Mneme cluster provides production Hot Rod CAS-loop convergence with explicit authoritative version provenance metadata, ensuring older commits cannot overwrite or invalidate newer cache representations.

- Implement `MnemeCachedResource` helper in `hestia/mneme-cluster` to attach and extract explicit authoritative version provenance via standard FHIR `Resource.meta.extension` (`http://harmonia.fhirfactory.net/structure/authoritative-version`), maintaining 100% backwards compatibility with raw FHIR JSON cache contracts and zero dual-cache consistency hazards.
- Implement `HotRodMnemeConvergence` in `hestia/mneme-cluster` implementing `ActiveStateConvergencePort`.
- Implement CAS loop with `MetadataValue<String> currentMeta = cache.getWithMetadata(id)` and `cache.replaceWithVersion(id, payloadJson, currentMeta.getVersion())`.
- Enforce newer-version protection invariant: if cached authoritative version is greater than or equal to committed version, immediately return `ConvergenceStatus.CONVERGED` (signifying no further cache action required).
- Handle cold cache / empty cache via `putIfAbsent(id, payloadJson)` and treat unversioned legacy cache entries as version 0.
- Enforce failure handling: upon CAS exhaustion or cluster timeout, return `ConvergenceStatus.DEGRADED` without unconditionally evicting the cache entry.
- Implement comprehensive unit and scenario tests for `HotRodMnemeConvergence`.

**Step 3: Implement DefaultGovernedWriter Orchestrator in mnemosyne-clinical**  
GovernedWriter orchestrator composes Themis authorization, Mneme coordination, Mnemosyne persistence, and Mneme convergence into a unified CREATE and UPDATE workflow within the mnemosyne-clinical runtime module.

- Implement `DefaultGovernedWriter` in `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`) implementing `GovernedWriter`.
- Implement `update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext)`:
    - Validate parameters (fail-closed on null).
    - Evaluate Themis authorization via `ThemisAuthorizer.authorize(...)` with `ThemisAction.UPDATE`; return `WriteResult.NotCommitted` on deny.
    - Consume active state token via `ActiveStateCoordinator.consume(key, activeToken)`; return `WriteResult.ActiveConflict` on `STALE` or `WriteResult.NotCommitted` on `UNAVAILABLE`.
    - Persist update via `AuthoritativePersistencePort.update(key, proposed, expectedVersion)`.
    - Map persistence conflict (`EXPECTED_VERSION_MISMATCH`) to `WriteResult.AuthoritativeConflict` with consumed token preserved.
    - Map persistence unknown to `WriteResult.OutcomeUnknown` without automatic retry or token rollback.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` with `CONVERGED` or `DEGRADED`.
- Implement `create(ResourceKey key, T resource, ThemisSecurityContext securityContext)`:
    - Validate parameters.
    - Evaluate Themis authorization with `ThemisAction.CREATE`; return `WriteResult.NotCommitted` on deny.
    - Persist new resource via `AuthoritativePersistencePort.create(key, resource)` (atomic database uniqueness check); return `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)` on conflict.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
- Implement comprehensive composition unit tests covering all 15 outcome permutations and concurrency scenarios.

**Step 4: Add ArchUnit Architecture Guardrails and Integration Verification**  
Paradeigma test suite enforces architectural layering, isolation, and absence of physical deletion across the composed governed-write stack.

- Update `GovernedWriteContractArchitectureTest` in `paradeigma/paradeigma-test` to verify that `GovernedWriter` contracts remain pure in `calliope`.
- Update `MnemosyneAuthoritativePersistenceArchitectureTest` to assert that `DefaultGovernedWriter` in `mnemosyne-clinical` remains free of direct Infinispan Hot Rod classes.
- Add ArchUnit rules verifying that `GovernedWriter`, `AuthoritativePersistencePort`, and `ActiveStateCoordinator` do not expose DELETE or REMOVE methods.
- Verify full test suite and architecture rules with `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`.



**Requirements**

**Overview & Goals**
Step 08.04D designs the production composition of the governed-write capabilities established in Steps 08.04A (Caller-facing Contracts), 08.04B (Mneme Active-State Coordination), and 08.04C (Mnemosyne Atomic Authoritative Persistence).

The core objective is to compose:
1. **Themis Security Authorization** (`ThemisAuthorizer`, `ThemisSecurityContext`)
2. **Mneme Active-State Coordination** (`ActiveStateCoordinator.consume`)
3. **Mnemosyne Authoritative Persistence** (`AuthoritativePersistencePort.create` / `update`)
4. **Guarded Mneme Post-Commit Convergence** (`ActiveStateConvergencePort.converge`)

into a single unified, deterministic `DefaultGovernedWriter` implementation supporting `CREATE` and `UPDATE` operations while upholding the fundamental architectural baselines (ADR-018, ADR-019, ADR-020).

**Scope**
- **In Scope**:
    - Architectural placement of `DefaultGovernedWriter` composition in existing runtime module `hestia/mnemosyne-clinical` (preserving `calliope` as a pure contract/model library and preserving `AuthoritativePersistencePort` in `mnemosyne-clinical`).
    - Explicit execution sequence for governed `UPDATE` and governed `CREATE`.
    - Formal architectural decision on `CREATE` active-state coordination vs direct authoritative creation with post-commit convergence.
    - Themis authorization enforcement point (strictly evaluated *before* active token consumption and persistence).
    - Complete failure and outcome mapping matrix covering all combinations of Themis, Mneme, and Mnemosyne results.
    - Post-commit Mneme convergence CAS-loop algorithm with explicit authoritative-version provenance metadata and strict newer-version protection (preventing older commits from overwriting or invalidating newer cached versions).
    - Precise semantics for `ConvergenceStatus.CONVERGED` ("no further cache action required") and `ConvergenceStatus.DEGRADED`.
    - Truthful preservation of `UNKNOWN` commit outcomes without automatic retries or token rollbacks.
    - Comprehensive unit and scenario test plans covering all concurrency, authorization, coordination, persistence, and convergence states.
    - ArchUnit architectural rules enforcing module isolation, zero cyclic dependencies, and zero physical DELETE semantics.
    - Catalogue of existing bypasses and explicit boundary definition with Task 09 (Cache-aside point reads).

- **Out of Scope**:
    - Full caller migration across BEFE, Pylai, and Energeia (deferred to subsequent integration steps).
    - Implementation of physical DELETE operations (strictly prohibited by ADR-020).
    - Implementation of Task 09 cache-aside point reads and read repairs.
    - Implementation of automated reconciliation machinery for `UNKNOWN` outcomes (deferred to future reconciliation task).
    - Creation of redundant Maven modules (`hestia-governance`, `clinical-write-orchestrator-service`).

**User Stories**
- **As a Clinical Gateway or BEFE Resource Provider**, I want to execute governed updates through a unified `GovernedWriter` that validates Themis authorization, consumes the active state token in Mneme, and conditionally commits to Mnemosyne, so that lost updates and unauthorized modifications are prevented.
- **As a System Administrator**, I want authoritative writes that succeed in Mnemosyne to remain committed even if transient cache convergence fails (returning `Committed` with `DEGRADED` convergence), so that durable persistence is never rolled back due to ephemeral cache grid issues.
- **As a Security Auditor**, I want all write operations to evaluate Themis policies before consuming coordination tokens or touching durable databases, so that unauthorized requests are rejected cleanly at the gate.

**Functional Requirements**
1. **Governed UPDATE Execution**:
    - Accepts `GovernedRead<T> current`, proposed state `T proposed`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately without touching Mneme or Mnemosyne.
    - Consumes observed `ActiveStateToken` via `ActiveStateCoordinator.consume`:
        - If `STALE`: aborts immediately and returns `WriteResult.ActiveConflict`.
        - If `UNAVAILABLE`: aborts immediately and returns `WriteResult.NotCommitted` (fail-fast, zero local fallback).
    - Executes authoritative persistence via `AuthoritativePersistencePort.update`:
        - If `Conflict(EXPECTED_VERSION_MISMATCH)`: returns `WriteResult.AuthoritativeConflict` (token remains consumed).
        - If `NotCommitted`: returns `WriteResult.NotCommitted` (token remains consumed).
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown` (token remains consumed, no retry).
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
2. **Governed CREATE Execution**:
    - Accepts `ResourceKey key`, initial state `T resource`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately.
    - Executes authoritative persistence via `AuthoritativePersistencePort.create` (Mnemosyne atomically enforces absence precondition via unique constraints):
        - If `Conflict(RESOURCE_ALREADY_EXISTS)`: returns `WriteResult.AuthoritativeConflict`.
        - If `NotCommitted`: returns `WriteResult.NotCommitted`.
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown`.
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
3. **No Physical DELETE**:
    - `GovernedWriter` exposes only `create` and `update`. All lifecycle transitions (e.g. deprecation, deactivation, suspension) are authoritative `update` operations with FHIR status codes.

**Non-Functional Requirements**
- **Independence & Isolation**: Mnemosyne persistence must not depend on Mneme cache. Mneme cache loss must not affect durable database correctness.
- **Zero Local Fallback**: Distributed cache outages must produce visible failures (`UNAVAILABLE` / `DEGRADED`), never silent fallbacks to JVM-local synchronization maps.
- **Distinct Version Domains**: The four version domains (`ActiveStateToken`, `AuthoritativeVersion`, `FHIR meta.versionId`, `HTTP ETag`) must remain strictly isolated. No cross-domain version assumptions or synthetic versions.
- **Deterministic Outcome Mapping**: Every combination of underlying outcomes must map unambiguously into the sealed `WriteResult<T>` hierarchy.

**Technical Design**

**Current Implementation & Baseline Artifacts**
- **Caller-Facing Contracts (`calliope`)**:
    - `GovernedWriter`: Interface defining `<T> WriteResult<T> create(...)` and `<T> WriteResult<T> update(...)`.
    - `GovernedRead<T>`: Record packaging `ResourceKey`, payload `T`, `ActiveStateToken`, and `AuthoritativeVersion`.
    - `WriteResult<T>`: Sealed interface with records `Committed<T>`, `ActiveConflict<T>`, `AuthoritativeConflict<T>`, `OutcomeUnknown<T>`, and `NotCommitted<T>`.
    - `ActiveStateCoordinator`: Interface defining `observe(ResourceKey)` and `consume(ResourceKey, ActiveStateToken)`.
    - `ActiveStateConvergencePort`: Domain port interface defining `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
    - `ConvergenceStatus`: Sealed hierarchy / enum (`CONVERGED`, `DEGRADED`).
- **Mneme Active-State Coordination (`hestia/mneme-cluster`)**:
    - `HotRodActiveStateCoordinator`: Implements `ActiveStateCoordinator` using Hot Rod `replaceWithVersion` against `active-coordination-cache`.
- **Mnemosyne Authoritative Persistence (`hestia/mnemosyne-clinical`)**:
    - `AuthoritativePersistencePort<T>`: Port interface defining `create` and `update` (remains in `mnemosyne-clinical`).
    - `AuthoritativePersistenceService`: Implements `AuthoritativePersistencePort<IBaseResource>` using `TransactionTemplate` and conditional SQL updates.
    - `AuthoritativePersistenceResult<T>`: Sealed interface with `Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown` (remains in `mnemosyne-clinical`).
- **Themis Policy Evaluation (`themis/themis-api` & `themis/themis-core`)**:
    - `ThemisAuthorizer`: Evaluates `ThemisAuthorizationRequest` and returns `ThemisAuthorizationDecision`.
    - `ThemisSecurityContext`: Immutable context carrying originating/executing principals, authorities, correlation, and causation IDs.

---

**Key Architectural Decisions**

**Decision 1: Module Placement of GovernedWriter Composition**
- **Chosen Approach**: Place the production `DefaultGovernedWriter` implementation in `hestia/mnemosyne-clinical` (under `net.fhirfactory.harmonia.hapifhir.governed`), preserving `AuthoritativePersistencePort` and `AuthoritativePersistenceResult` in `mnemosyne-clinical`. `calliope` remains strictly a library for canonical models and domain contracts (`GovernedWriter`, `GovernedRead`, `WriteResult`, `ActiveStateCoordinator`, `ActiveStateConvergencePort`, `AuthoritativeVersion`, `ConvergenceStatus`). Hot Rod convergence logic (`HotRodMnemeConvergence`) is placed in `hestia/mneme-cluster`.
- **Rationale**:
    - `calliope` owns canonical schemas and semantic governance. It must never become an application runtime service orchestrator.
    - `hestia/mnemosyne-clinical` is the smallest existing runtime module that already depends on `calliope`, `themis-api`, and `themis-core`, and houses the clinical persistence implementations.
    - `DefaultGovernedWriter` interacts solely with domain interfaces (`ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, `ActiveStateConvergencePort`), without depending on Infinispan/Hot Rod concrete classes.
    - This avoids creating redundant Maven modules (e.g. `hestia-governance`), avoids dependency cycles, avoids leaking JPA/Infinispan into Calliope, and keeps contracts pure.

**Decision 2: Themis Authorization Precedence**
- **Chosen Approach**: Evaluate Themis authorization as the very first step in both `create` and `update`, strictly *before* consuming the `ActiveStateToken` in Mneme and *before* invoking Mnemosyne.
- **Rationale**: An unauthorized caller must not mutate active coordination state, burn ActiveStateTokens, or trigger database queries.

**Decision 3: CREATE Precondition Model & Active-State Coordination Evaluation**
- **Evaluation of Alternatives**:
    - **Option A (Chosen): Direct Authoritative CREATE with Post-Commit Convergence**
        - `Themis -> Mnemosyne CREATE -> Mneme Post-Commit Convergence`
    - **Option B (Rejected): Pre-Persistence Mneme Cold-State Coordination**
        - `Themis -> Mneme cold-state coordination -> Mnemosyne CREATE -> Mneme convergence`
- **Architectural Rationale & Invariant**:
    - An `ActiveStateToken` represents an observed point-in-time active state of an *existing* resource. For a newly created resource, no prior active state exists to observe.
    - Mnemosyne exclusively defines the durable authoritative state boundary (ADR-018) and owns the authoritative *absence* precondition (enforced via database unique constraints on `[resource_type, fhir_id]`).
    - Option B would require reserving a speculative token or placeholder marker in Mneme for an absent resource prior to database insertion. If Mnemosyne persistence subsequently fails, or if Mneme partitions/restarts, this creates phantom reservations, requires token rollback machinery (violating the fundamental invariant that consumed tokens are never rolled back), and creates distributed dual-master race conditions between cache and database.
    - **Invariant**: *Mnemosyne exclusively owns the authoritative absence precondition. CREATE operations deliberately do not perform pre-persistence active-state coordination in Mneme, and instead seed Mneme active state via post-commit convergence upon successful authoritative creation ($V=1$).*
    - No synthetic authoritative version, lease, or JVM lock is invented.

**Decision 4: Guarded Mneme Post-Commit Convergence with Distinct Version Domains**
- **Version Domain Separation**:
    1. `ActiveStateToken`: Opaque Hot Rod entry version CAS token for active-state coordination.
    2. `AuthoritativeVersion`: Mnemosyne durable database sequence version ($1, 2, 3\dots$).
    3. FHIR `Resource.meta.versionId`: FHIR specification payload field (string).
    4. HTTP ETag / `If-Match`: HTTP transport concurrency header.
    - *Invariant*: Convergence must never assume `FHIR meta.versionId == AuthoritativeVersion`.
- **Authoritative Provenance in Mneme**:
    - Post-commit convergence compares the incoming `AuthoritativeVersion` against explicit metadata describing the authoritative version represented by the cached Mneme value.
    - **Cache Contract Analysis & Chosen Representation**:
        - *Investigation of Existing Caches*: All existing clinical caches (`person-cache`, `task-cache`, `organization-cache`, etc.) store raw FHIR JSON `String`. Existing readers (`FhirCacheService`, `TaskCacheService`, `DefaultTaskService`) parse cached values directly using `FhirContext.newJsonParser().parseResource(...)`.
        - *Incompatibility of Outer JSON Envelope*: An outer wrapper (`{ authoritativeVersion: ..., resource: ... }`) is invalid FHIR JSON and would cause `DataFormatException` in all existing readers, violating the constraint not to migrate all cache consumers in 08.04D.
        - *Chosen Smallest Compatible Representation (`MnemeCachedResource`)*: Authoritative version provenance is embedded directly inside the standard FHIR resource under `Resource.meta.extension` using canonical URI `http://harmonia.fhirfactory.net/structure/authoritative-version` (with `Integer64Type` or numeric representation).
        - *Backwards Compatibility & Atomicity*: The cached value remains 100% valid raw FHIR JSON. HAPI FHIR parsers across all existing consumers parse it seamlessly without schema errors. The resource payload and its authoritative provenance are read and written atomically in a single Infinispan cache entry, eliminating dual-cache consistency hazards.
        - *Legacy Cache Entries*: Any pre-existing cache entry lacking the authoritative-version extension is treated as $V_{cached} = 0$, allowing governed writes to safely converge over legacy entries.
    - Infinispan `MetadataValue.getVersion()` remains strictly an opaque CAS token for `replaceWithVersion`.
- **Convergence Semantics**:
    - `ConvergenceStatus.CONVERGED` explicitly means **"no further cache action is required"** (the cache already reflects this authoritative version or a newer authoritative progression), NOT "the supplied committed representation was written to cache".
    - If the cached representation is newer or equal ($V_{cached} \ge V_{committed}$), convergence immediately succeeds with `CONVERGED` without overwriting the newer state.
    - If the cached representation is older ($V_{cached} < V_{committed}$), conditional replacement is attempted using the opaque Hot Rod version token (`replaceWithVersion`).
    - If the cache entry is absent (cold cache), `putIfAbsent` is executed.
    - If CAS fails or retries are exhausted under contention/outage, `ConvergenceStatus.DEGRADED` is returned without unconditionally evicting the cache entry.

---

**Composed Architecture & Sequence Flows**

```mermaid
graph TD
  Caller[Caller: BEFE / Pylai / Erga] -->|GovernedRead + Proposed State| GW[DefaultGovernedWriter in mnemosyne-clinical]
  GW -->|1. Authorize Action| Themis[ThemisAuthorizer]
  Themis -->|Allow / Deny| GW
  GW -->|2. Consume Token (UPDATE only)| MnemeCoord[Mneme: ActiveStateCoordinator]
  MnemeCoord -->|CONSUMED / STALE / UNAVAILABLE| GW
  GW -->|3. Atomic Persistence| Mnemosyne[Mnemosyne: AuthoritativePersistencePort]
  Mnemosyne -->|COMMITTED / CONFLICT / UNKNOWN| GW
  GW -->|4. Guarded CAS Convergence| MnemeConv[Mneme: ActiveStateConvergencePort]
  MnemeConv -->|CONVERGED / DEGRADED| GW
  GW -->|WriteResult<T>| Caller
```

**UPDATE Execution Flow**
1. **Validate Input**: Check non-null `GovernedRead`, `proposed`, `securityContext`, and matching resource types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.UPDATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Active-State Progression (Mneme)**:
    - Call `coordinator.consume(current.key(), current.activeToken())`.
    - If `STALE`: return `WriteResult.activeStateConflict(ActiveStateConflict.of(key, "Active state token is stale"))`.
    - If `UNAVAILABLE`: return `WriteResult.notCommitted(key, "Active state coordination unavailable")`.
4. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.update(current.key(), proposed, current.expectedAuthoritativeVersion())`.
    - If `Conflict(EXPECTED_VERSION_MISMATCH)`: return `WriteResult.authoritativeConflict(conflict.conflict())`. (Token remains consumed).
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`. (Token remains consumed).
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`. (Token remains consumed, no retry).
5. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, newVersion)`: call `convergencePort.converge(key, resource, newVersion)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, newVersion)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, newVersion, "Mneme cache convergence degraded")`.

**CREATE Execution Flow**
1. **Validate Input**: Check non-null `ResourceKey`, `resource`, `securityContext`, and matching types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.CREATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.create(key, resource)`.
    - If `Conflict(RESOURCE_ALREADY_EXISTS)`: return `WriteResult.authoritativeConflict(conflict.conflict())`.
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`.
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`.
4. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, v1)`: call `convergencePort.converge(key, resource, v1)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, v1)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, v1, "Mneme cache convergence degraded")`.

---

**Failure and Outcome Mapping Matrix**

| Scenario / Condition | Themis Decision | Mneme Coordination | Mnemosyne Persistence | Mneme Convergence | WriteResult Variant | Token Consumed? | Database State |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **A. Themis Denies** | `DENY` | Not Called | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **B. Mneme Stale** | `ALLOW` | `STALE` | Not Called | Not Called | `WriteResult.ActiveConflict` | No | Unchanged |
| **C. Mneme Unavailable** | `ALLOW` | `UNAVAILABLE` | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **D. Authoritative Conflict** | `ALLOW` | `CONSUMED` | `Conflict(VERSION_MISMATCH)` | Not Called | `WriteResult.AuthoritativeConflict` | Yes | Unchanged |
| **E. Persistence Not Committed** | `ALLOW` | `CONSUMED` | `NotCommitted` | Not Called | `WriteResult.NotCommitted` | Yes | Unchanged |
| **F. Persistence Unknown** | `ALLOW` | `CONSUMED` | `OutcomeUnknown` | Not Called | `WriteResult.OutcomeUnknown` | Yes | Ambiguous (No Retry) |
| **G. Committed + Converged** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `CONVERGED` | `WriteResult.Committed(CONVERGED)` | Yes | Committed ($V$) |
| **H. Committed + Degraded** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `DEGRADED` | `WriteResult.Committed(DEGRADED)` | Yes | Committed ($V$) |
| **I. CREATE Duplicate** | `ALLOW` | N/A | `Conflict(ALREADY_EXISTS)` | Not Called | `WriteResult.AuthoritativeConflict` | N/A | Unchanged |

---

**Post-Commit Mneme Convergence Algorithm**

```java
public class HotRodMnemeConvergence implements ActiveStateConvergencePort {
    public static final String AUTHORITATIVE_VERSION_EXT_URL = "http://harmonia.fhirfactory.net/structure/authoritative-version";

    private final RemoteCacheManager cacheManager;
    private final FhirContext fhirContext;

    @Override
    public <T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion) {
        String cacheName = resolveCacheName(key.resourceType());
        RemoteCache<String, String> cache = cacheManager.getCache(cacheName);
        if (cache == null) return ConvergenceStatus.DEGRADED;

        // Attach explicit authoritative-version provenance into FHIR meta.extension
        IBaseResource fhirResource = (IBaseResource) committedResource;
        MnemeCachedResource.attachAuthoritativeVersion(fhirResource, committedVersion);
        String payloadJson = fhirContext.newJsonParser().encodeResourceToString(fhirResource);
        long targetVer = committedVersion.value();

        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                MetadataValue<String> currentMeta = cache.getWithMetadata(key.id());
                if (currentMeta != null) {
                    long cachedVer = MnemeCachedResource.extractAuthoritativeVersion(currentMeta.getValue());
                    // Newer-Version Invariant: never overwrite newer cache state.
                    // CONVERGED means no further cache action is required.
                    if (cachedVer >= targetVer) {
                        return ConvergenceStatus.CONVERGED;
                    }
                    // Hot Rod opaque CAS token replacement
                    boolean casSuccess = cache.replaceWithVersion(key.id(), payloadJson, currentMeta.getVersion());
                    if (casSuccess) return ConvergenceStatus.CONVERGED;
                } else {
                    String existing = cache.putIfAbsent(key.id(), payloadJson);
                    if (existing == null) return ConvergenceStatus.CONVERGED;
                }
            } catch (Exception e) {
                return ConvergenceStatus.DEGRADED;
            }
        }
        // CAS exhausted: DO NOT evict entry (preserves newer versions)
        return ConvergenceStatus.DEGRADED;
    }
}
```

---

**Dependency Graph**

```
calliope (contracts: GovernedWriter, GovernedRead, WriteResult, ActiveStateCoordinator,
          ActiveStateConvergencePort, AuthoritativeVersion, ConvergenceStatus)
  └── depends on: themis-api (ThemisAuthorizer, ThemisSecurityContext)
  └── depends on: hapi-fhir-structures-r5

hestia/mneme-cluster (implementations: HotRodActiveStateCoordinator, HotRodMnemeConvergence)
  └── depends on: calliope, infinispan-client-hotrod

hestia/mnemosyne-clinical (persistence & composition: AuthoritativePersistencePort, AuthoritativePersistenceService, DefaultGovernedWriter)
  └── depends on: calliope, themis-api, themis-core, spring-boot-starter-data-jpa

iris/iris-befe (presentation gateway)
  └── depends on: calliope, themis-api, themis-core, hestia/mneme-cluster
```

**Graph Properties**:
- Purely unidirectional.
- Zero cyclic dependencies.
- No JPA, Hibernate, or Infinispan types leaked into `calliope` or `GovernedWriter` API.
- `DefaultGovernedWriter` lives in `mnemosyne-clinical` as a runtime orchestrator.

---

**Concrete File Structure Changes**

**Files to Add:**
1. `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ActiveStateConvergencePort.java` (Domain convergence port interface)
2. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/MnemeCachedResource.java` (Helper to attach/extract `AuthoritativeVersion` provenance via standard FHIR `Resource.meta.extension`)
3. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergence.java` (Hot Rod CAS convergence implementation)
4. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriter.java` (Production orchestrator in runtime module)
5. `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriterTest.java` (Comprehensive composition unit tests)
6. `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergenceTest.java` (Convergence CAS & newer-version tests)
7. `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteCompositionArchitectureTest.java` (ArchUnit rules)

**Files Preserved Without Relocation:**
1. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java` (Remains in `mnemosyne-clinical`)
2. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/model/AuthoritativePersistenceResult.java` (Remains in `mnemosyne-clinical`)
3. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java` (Remains in `mnemosyne-clinical`)

**Testing**

**Validation Approach**
Verification of Step 08.04D composition will be performed using a multi-tier testing strategy:
1. **Composition Unit Tests (`DefaultGovernedWriterTest`)**:
    - Uses Mockito-based test doubles for `ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, and `ActiveStateConvergencePort`.
    - Validates all 15 permutations of the Failure and Outcome Mapping Matrix.
2. **Convergence CAS Unit & Scenario Tests (`HotRodMnemeConvergenceTest`)**:
    - Validates the newer-version protection invariant ($V_{cached} \ge V_{committed}$ is preserved).
    - Validates cold-cache population via `putIfAbsent`.
    - Validates CAS loss handling and non-eviction on retry exhaustion.
3. **ArchUnit Architecture Tests (`GovernedWriteCompositionArchitectureTest`)**:
    - Enforces package layering, zero dependency cycles, and zero JPA/Infinispan leaks into `calliope`.

---

**Key Scenarios**

**Scenario 1: Authorised UPDATE Happy Path**
- **Setup**: `GovernedRead` with V1 and token `T1`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne commits V2 $\to$ Convergence succeeds with V2.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.CONVERGED`, version `AuthoritativeVersion.of(2L)`.

**Scenario 2: Themis Denied**
- **Setup**: `GovernedRead` with V1. Themis mock configured to return `DENY`.
- **Flow**: Themis denies $\to$ write immediately terminates.
- **Outcome**: `WriteResult.NotCommitted`. Zero invocations on `ActiveStateCoordinator` and `AuthoritativePersistencePort`.

**Scenario 3: Stale ActiveStateToken**
- **Setup**: `GovernedRead` with stale token `T_stale`.
- **Flow**: Themis allows $\to$ Mneme returns `STALE` $\to$ write immediately terminates.
- **Outcome**: `WriteResult.ActiveConflict`. Zero invocations on `AuthoritativePersistencePort`.

**Scenario 4: Mneme Unavailable**
- **Setup**: `GovernedRead` with V1. `ActiveStateCoordinator` returns `UNAVAILABLE`.
- **Flow**: Themis allows $\to$ Mneme returns `UNAVAILABLE` $\to$ write terminates with fail-fast visible error.
- **Outcome**: `WriteResult.NotCommitted` (fail-fast, no JVM-local fallback).

**Scenario 5: Authoritative Precondition Conflict (Expected Version Mismatch)**
- **Setup**: `GovernedRead` with V1. Mnemosyne returns `Conflict(EXPECTED_VERSION_MISMATCH, current=V3)`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne returns Conflict.
- **Outcome**: `WriteResult.AuthoritativeConflict(EXPECTED_VERSION_MISMATCH)`. Token `T1` remains consumed.

**Scenario 6: Authoritative Persistence Unknown**
- **Setup**: Mnemosyne returns `OutcomeUnknown("Commit ACK timeout")`.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne returns `OutcomeUnknown`.
- **Outcome**: `WriteResult.OutcomeUnknown`. Token remains consumed. No automatic retry attempted.

**Scenario 7: Committed with Degraded Convergence**
- **Setup**: Mnemosyne commits V2, but Mneme convergence throws timeout exception.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne commits V2 $\to$ Convergence returns `DEGRADED`.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.DEGRADED`. Durable commit remains intact.

**Scenario 8: Delayed Older Convergence Preserves Newer Cache State**
- **Setup**: Cache currently contains V43. Delayed convergence thread attempts to converge committed V42.
- **Flow**: `HotRodMnemeConvergence.converge` reads cached V43 $\to$ detects $43 \ge 42 \to$ terminates immediately with `CONVERGED`.
- **Outcome**: Cache remains unchanged at V43. V42 does not overwrite or evict V43.

**Scenario 9: Authorised CREATE Happy Path & Duplicate Detection**
- **Setup**: Proposed new resource `Patient/100`.
- **Flow A**: Themis allows $\to$ Mnemosyne creates V1 $\to$ Convergence populates cache with V1 $\to$ returns `WriteResult.Committed(V1)`.
- **Flow B**: Concurrent CREATE with same key $\to$ Mnemosyne unique constraint triggers $\to$ returns `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)`.

---

**Architecture Guardrails to Add**
- `GovernedWriteCompositionArchitectureTest`:
    1. `GovernedWriter` resides in `calliope` and `DefaultGovernedWriter` resides in `hestia/mnemosyne-clinical`.
    2. `calliope` must have zero dependencies on `hestia`, `iris`, `pylai`, `energeia`, `agora`, or `paradeigma`.
    3. `DefaultGovernedWriter` must not import or depend on `org.infinispan..`, `jakarta.persistence..`, `org.hibernate..`, or `ca.uhn.fhir.jpa..`.
    4. `AuthoritativePersistencePort` and `ActiveStateCoordinator` must not expose DELETE, REMOVE, or PURGE methods (ADR-020).
    5. `HotRodMnemeConvergence` must not depend on `AuthoritativePersistencePort` or JPA entities.

**Bypasses & Deferred Work**

**Catalogue of Remaining Bypasses (Post-08.04D)**

While Step 08.04D establishes the complete production governed-write composition, existing runtime components still contain legacy write-path bypasses that will be migrated in subsequent steps:

| Subsystem / Component | File / Location | Legacy Behavior | Target Migration Step |
| :--- | :--- | :--- | :--- |
| **Iris BEFE** | `FhirCacheService.java` | Unconditional `remoteCache.put(id, json)` and `remoteCache.remove(id)` | Task 08.05 / 08.06 (BEFE Governed Writer Migration) |
| **Mneme Persistence** | `FhirRestCacheStore.java` | Asynchronous write-behind store bypassing governed coordination | Task 08.07 (Write-Behind Decommissioning) |
| **Mnemosyne Clinical** | HAPI Resource Providers | Direct `FhirStorageService.createResource/updateResource` | Task 08.05 / Task 09 |
| **Energeia Ponos** | `ErgonBase.java` / `PraxisService.java` | Direct cache updates for task synchronization | Task 08.06 (Workflow Task Governed Migration) |
| **Pylai MLLP** | `pylai-mllp-in` / `pylai-fhir-provider-registry` | Direct cache and storage mutations | Task 08.06 (Gateway Governed Migration) |

---

**Explicitly Deferred Work**
1. **Task 09 (Cache-Aside Point Reads)**: Governed point reads (`GovernedRead<T>`), cache-aside read-through, and read repairs will be designed and implemented in Task 09.
2. **UNKNOWN Outcome Reconciliation Framework**: Automated asynchronous reconciliation of `WriteResult.OutcomeUnknown` is deferred to a dedicated post-Task 08 reconciliation capability.
3. **Legacy Caller Migration**: Modifying BEFE REST endpoints, Pylai gateways, and Ponos routes to call `GovernedWriter` will occur in follow-up integration steps.

**Delivery Steps**

**Step 1: Define Governed-Write Convergence Contract in Calliope**
Define pure convergence port contract in Calliope while preserving persistence ports in Mnemosyne Clinical.

- Define `ActiveStateConvergencePort` in `calliope` (`net.fhirfactory.harmonia.model.governedwrite`) with method `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
- Verify `AuthoritativePersistencePort<T>` and `AuthoritativePersistenceResult<T>` remain in `hestia/mnemosyne-clinical` without leaking into Calliope.
- Add unit tests in `calliope` verifying contract invariants, immutability, and null safety.

**Step 2: Implement Guarded Mneme Hot Rod Convergence in hestia-mneme-cluster**
Mneme cluster provides production Hot Rod CAS-loop convergence with explicit authoritative version provenance metadata, ensuring older commits cannot overwrite or invalidate newer cache representations.

- Implement `MnemeCachedResource` helper in `hestia/mneme-cluster` to attach and extract explicit authoritative version provenance via standard FHIR `Resource.meta.extension` (`http://harmonia.fhirfactory.net/structure/authoritative-version`), maintaining 100% backwards compatibility with raw FHIR JSON cache contracts and zero dual-cache consistency hazards.
- Implement `HotRodMnemeConvergence` in `hestia/mneme-cluster` implementing `ActiveStateConvergencePort`.
- Implement CAS loop with `MetadataValue<String> currentMeta = cache.getWithMetadata(id)` and `cache.replaceWithVersion(id, payloadJson, currentMeta.getVersion())`.
- Enforce newer-version protection invariant: if cached authoritative version is greater than or equal to committed version, immediately return `ConvergenceStatus.CONVERGED` (signifying no further cache action required).
- Handle cold cache / empty cache via `putIfAbsent(id, payloadJson)` and treat unversioned legacy cache entries as version 0.
- Enforce failure handling: upon CAS exhaustion or cluster timeout, return `ConvergenceStatus.DEGRADED` without unconditionally evicting the cache entry.
- Implement comprehensive unit and scenario tests for `HotRodMnemeConvergence`.

**Step 3: Implement DefaultGovernedWriter Orchestrator in mnemosyne-clinical**
GovernedWriter orchestrator composes Themis authorization, Mneme coordination, Mnemosyne persistence, and Mneme convergence into a unified CREATE and UPDATE workflow within the mnemosyne-clinical runtime module.

- Implement `DefaultGovernedWriter` in `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`) implementing `GovernedWriter`.
- Implement `update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext)`:
    - Validate parameters (fail-closed on null).
    - Evaluate Themis authorization via `ThemisAuthorizer.authorize(...)` with `ThemisAction.UPDATE`; return `WriteResult.NotCommitted` on deny.
    - Consume active state token via `ActiveStateCoordinator.consume(key, activeToken)`; return `WriteResult.ActiveConflict` on `STALE` or `WriteResult.NotCommitted` on `UNAVAILABLE`.
    - Persist update via `AuthoritativePersistencePort.update(key, proposed, expectedVersion)`.
    - Map persistence conflict (`EXPECTED_VERSION_MISMATCH`) to `WriteResult.AuthoritativeConflict` with consumed token preserved.
    - Map persistence unknown to `WriteResult.OutcomeUnknown` without automatic retry or token rollback.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` with `CONVERGED` or `DEGRADED`.
- Implement `create(ResourceKey key, T resource, ThemisSecurityContext securityContext)`:
    - Validate parameters.
    - Evaluate Themis authorization with `ThemisAction.CREATE`; return `WriteResult.NotCommitted` on deny.
    - Persist new resource via `AuthoritativePersistencePort.create(key, resource)` (atomic database uniqueness check); return `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)` on conflict.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
- Implement comprehensive composition unit tests covering all 15 outcome permutations and concurrency scenarios.

**Step 4: Add ArchUnit Architecture Guardrails and Integration Verification**
Paradeigma test suite enforces architectural layering, isolation, and absence of physical deletion across the composed governed-write stack.

- Update `GovernedWriteContractArchitectureTest` in `paradeigma/paradeigma-test` to verify that `GovernedWriter` contracts remain pure in `calliope`.
- Update `MnemosyneAuthoritativePersistenceArchitectureTest` to assert that `DefaultGovernedWriter` in `mnemosyne-clinical` remains free of direct Infinispan Hot Rod classes.
- Add ArchUnit rules verifying that `GovernedWriter`, `AuthoritativePersistencePort`, and `ActiveStateCoordinator` do not expose DELETE or REMOVE methods.
- Verify full test suite and architecture rules with `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`.

Now I'm opening the plan document governed-write-composition-08-04d to understand the initial guidelines and structure for composing the code changes needed.

**Requirements**

**Overview & Goals**  
Step 08.04D designs the production composition of the governed-write capabilities established in Steps 08.04A (Caller-facing Contracts), 08.04B (Mneme Active-State Coordination), and 08.04C (Mnemosyne Atomic Authoritative Persistence).

The core objective is to compose:
1. **Themis Security Authorization** (`ThemisAuthorizer`, `ThemisSecurityContext`)
2. **Mneme Active-State Coordination** (`ActiveStateCoordinator.consume`)
3. **Mnemosyne Authoritative Persistence** (`AuthoritativePersistencePort.create` / `update`)
4. **Guarded Mneme Post-Commit Convergence** (`ActiveStateConvergencePort.converge`)

into a single unified, deterministic `DefaultGovernedWriter` implementation supporting `CREATE` and `UPDATE` operations while upholding the fundamental architectural baselines (ADR-018, ADR-019, ADR-020).

**Scope**
- **In Scope**:
    - Architectural placement of `DefaultGovernedWriter` composition in existing runtime module `hestia/mnemosyne-clinical` (preserving `calliope` as a pure contract/model library and preserving `AuthoritativePersistencePort` in `mnemosyne-clinical`).
    - Explicit execution sequence for governed `UPDATE` and governed `CREATE`.
    - Formal architectural decision on `CREATE` active-state coordination vs direct authoritative creation with post-commit convergence.
    - Themis authorization enforcement point (strictly evaluated *before* active token consumption and persistence).
    - Complete failure and outcome mapping matrix covering all combinations of Themis, Mneme, and Mnemosyne results.
    - Post-commit Mneme convergence CAS-loop algorithm with explicit authoritative-version provenance metadata and strict newer-version protection (preventing older commits from overwriting or invalidating newer cached versions).
    - Precise semantics for `ConvergenceStatus.CONVERGED` ("no further cache action required") and `ConvergenceStatus.DEGRADED`.
    - Truthful preservation of `UNKNOWN` commit outcomes without automatic retries or token rollbacks.
    - Comprehensive unit and scenario test plans covering all concurrency, authorization, coordination, persistence, and convergence states.
    - ArchUnit architectural rules enforcing module isolation, zero cyclic dependencies, and zero physical DELETE semantics.
    - Catalogue of existing bypasses and explicit boundary definition with Task 09 (Cache-aside point reads).

- **Out of Scope**:
    - Full caller migration across BEFE, Pylai, and Energeia (deferred to subsequent integration steps).
    - Implementation of physical DELETE operations (strictly prohibited by ADR-020).
    - Implementation of Task 09 cache-aside point reads and read repairs.
    - Implementation of automated reconciliation machinery for `UNKNOWN` outcomes (deferred to future reconciliation task).
    - Creation of redundant Maven modules (`hestia-governance`, `clinical-write-orchestrator-service`).

**User Stories**
- **As a Clinical Gateway or BEFE Resource Provider**, I want to execute governed updates through a unified `GovernedWriter` that validates Themis authorization, consumes the active state token in Mneme, and conditionally commits to Mnemosyne, so that lost updates and unauthorized modifications are prevented.
- **As a System Administrator**, I want authoritative writes that succeed in Mnemosyne to remain committed even if transient cache convergence fails (returning `Committed` with `DEGRADED` convergence), so that durable persistence is never rolled back due to ephemeral cache grid issues.
- **As a Security Auditor**, I want all write operations to evaluate Themis policies before consuming coordination tokens or touching durable databases, so that unauthorized requests are rejected cleanly at the gate.

**Functional Requirements**
1. **Governed UPDATE Execution**:
    - Accepts `GovernedRead<T> current`, proposed state `T proposed`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately without touching Mneme or Mnemosyne.
    - Consumes observed `ActiveStateToken` via `ActiveStateCoordinator.consume`:
        - If `STALE`: aborts immediately and returns `WriteResult.ActiveConflict`.
        - If `UNAVAILABLE`: aborts immediately and returns `WriteResult.NotCommitted` (fail-fast, zero local fallback).
    - Executes authoritative persistence via `AuthoritativePersistencePort.update`:
        - If `Conflict(EXPECTED_VERSION_MISMATCH)`: returns `WriteResult.AuthoritativeConflict` (token remains consumed).
        - If `NotCommitted`: returns `WriteResult.NotCommitted` (token remains consumed).
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown` (token remains consumed, no retry).
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
2. **Governed CREATE Execution**:
    - Accepts `ResourceKey key`, initial state `T resource`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately.
    - Executes authoritative persistence via `AuthoritativePersistencePort.create` (Mnemosyne atomically enforces absence precondition via unique constraints):
        - If `Conflict(RESOURCE_ALREADY_EXISTS)`: returns `WriteResult.AuthoritativeConflict`.
        - If `NotCommitted`: returns `WriteResult.NotCommitted`.
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown`.
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
3. **No Physical DELETE**:
    - `GovernedWriter` exposes only `create` and `update`. All lifecycle transitions (e.g. deprecation, deactivation, suspension) are authoritative `update` operations with FHIR status codes.

**Non-Functional Requirements**
- **Independence & Isolation**: Mnemosyne persistence must not depend on Mneme cache. Mneme cache loss must not affect durable database correctness.
- **Zero Local Fallback**: Distributed cache outages must produce visible failures (`UNAVAILABLE` / `DEGRADED`), never silent fallbacks to JVM-local synchronization maps.
- **Distinct Version Domains**: The four version domains (`ActiveStateToken`, `AuthoritativeVersion`, `FHIR meta.versionId`, `HTTP ETag`) must remain strictly isolated. No cross-domain version assumptions or synthetic versions.
- **Deterministic Outcome Mapping**: Every combination of underlying outcomes must map unambiguously into the sealed `WriteResult<T>` hierarchy.

**Technical Design**

**Current Implementation & Baseline Artifacts**
- **Caller-Facing Contracts (`calliope`)**:
    - `GovernedWriter`: Interface defining `<T> WriteResult<T> create(...)` and `<T> WriteResult<T> update(...)`.
    - `GovernedRead<T>`: Record packaging `ResourceKey`, payload `T`, `ActiveStateToken`, and `AuthoritativeVersion`.
    - `WriteResult<T>`: Sealed interface with records `Committed<T>`, `ActiveConflict<T>`, `AuthoritativeConflict<T>`, `OutcomeUnknown<T>`, and `NotCommitted<T>`.
    - `ActiveStateCoordinator`: Interface defining `observe(ResourceKey)` and `consume(ResourceKey, ActiveStateToken)`.
    - `ActiveStateConvergencePort`: Domain port interface defining `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
    - `ConvergenceStatus`: Sealed hierarchy / enum (`CONVERGED`, `DEGRADED`).
- **Mneme Active-State Coordination (`hestia/mneme-cluster`)**:
    - `HotRodActiveStateCoordinator`: Implements `ActiveStateCoordinator` using Hot Rod `replaceWithVersion` against `active-coordination-cache`.
- **Mnemosyne Authoritative Persistence (`hestia/mnemosyne-clinical`)**:
    - `AuthoritativePersistencePort<T>`: Port interface defining `create` and `update` (remains in `mnemosyne-clinical`).
    - `AuthoritativePersistenceService`: Implements `AuthoritativePersistencePort<IBaseResource>` using `TransactionTemplate` and conditional SQL updates.
    - `AuthoritativePersistenceResult<T>`: Sealed interface with `Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown` (remains in `mnemosyne-clinical`).
- **Themis Policy Evaluation (`themis/themis-api` & `themis/themis-core`)**:
    - `ThemisAuthorizer`: Evaluates `ThemisAuthorizationRequest` and returns `ThemisAuthorizationDecision`.
    - `ThemisSecurityContext`: Immutable context carrying originating/executing principals, authorities, correlation, and causation IDs.

---

**Key Architectural Decisions**

**Decision 1: Module Placement of GovernedWriter Composition**
- **Chosen Approach**: Place the production `DefaultGovernedWriter` implementation in `hestia/mnemosyne-clinical` (under `net.fhirfactory.harmonia.hapifhir.governed`), preserving `AuthoritativePersistencePort` and `AuthoritativePersistenceResult` in `mnemosyne-clinical`. `calliope` remains strictly a library for canonical models and domain contracts (`GovernedWriter`, `GovernedRead`, `WriteResult`, `ActiveStateCoordinator`, `ActiveStateConvergencePort`, `AuthoritativeVersion`, `ConvergenceStatus`). Hot Rod convergence logic (`HotRodMnemeConvergence`) is placed in `hestia/mneme-cluster`.
- **Rationale**:
    - `calliope` owns canonical schemas and semantic governance. It must never become an application runtime service orchestrator.
    - `hestia/mnemosyne-clinical` is the smallest existing runtime module that already depends on `calliope`, `themis-api`, and `themis-core`, and houses the clinical persistence implementations.
    - `DefaultGovernedWriter` interacts solely with domain interfaces (`ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, `ActiveStateConvergencePort`), without depending on Infinispan/Hot Rod concrete classes.
    - This avoids creating redundant Maven modules (e.g. `hestia-governance`), avoids dependency cycles, avoids leaking JPA/Infinispan into Calliope, and keeps contracts pure.

**Decision 2: Themis Authorization Precedence**
- **Chosen Approach**: Evaluate Themis authorization as the very first step in both `create` and `update`, strictly *before* consuming the `ActiveStateToken` in Mneme and *before* invoking Mnemosyne.
- **Rationale**: An unauthorized caller must not mutate active coordination state, burn ActiveStateTokens, or trigger database queries.

**Decision 3: CREATE Precondition Model & Active-State Coordination Evaluation**
- **Evaluation of Alternatives**:
    - **Option A (Chosen): Direct Authoritative CREATE with Post-Commit Convergence**
        - `Themis -> Mnemosyne CREATE -> Mneme Post-Commit Convergence`
    - **Option B (Rejected): Pre-Persistence Mneme Cold-State Coordination**
        - `Themis -> Mneme cold-state coordination -> Mnemosyne CREATE -> Mneme convergence`
- **Architectural Rationale & Invariant**:
    - An `ActiveStateToken` represents an observed point-in-time active state of an *existing* resource. For a newly created resource, no prior active state exists to observe.
    - Mnemosyne exclusively defines the durable authoritative state boundary (ADR-018) and owns the authoritative *absence* precondition (enforced via database unique constraints on `[resource_type, fhir_id]`).
    - Option B would require reserving a speculative token or placeholder marker in Mneme for an absent resource prior to database insertion. If Mnemosyne persistence subsequently fails, or if Mneme partitions/restarts, this creates phantom reservations, requires token rollback machinery (violating the fundamental invariant that consumed tokens are never rolled back), and creates distributed dual-master race conditions between cache and database.
    - **Invariant**: *Mnemosyne exclusively owns the authoritative absence precondition. CREATE operations deliberately do not perform pre-persistence active-state coordination in Mneme, and instead seed Mneme active state via post-commit convergence upon successful authoritative creation ($V=1$).*
    - No synthetic authoritative version, lease, or JVM lock is invented.

**Decision 4: Guarded Mneme Post-Commit Convergence with Distinct Version Domains**
- **Version Domain Separation**:
    1. `ActiveStateToken`: Opaque Hot Rod entry version CAS token for active-state coordination.
    2. `AuthoritativeVersion`: Mnemosyne durable database sequence version ($1, 2, 3\dots$).
    3. FHIR `Resource.meta.versionId`: FHIR specification payload field (string).
    4. HTTP ETag / `If-Match`: HTTP transport concurrency header.
    - *Invariant*: Convergence must never assume `FHIR meta.versionId == AuthoritativeVersion`.
- **Authoritative Provenance in Mneme**:
    - Post-commit convergence compares the incoming `AuthoritativeVersion` against explicit metadata describing the authoritative version represented by the cached Mneme value.
    - **Cache Contract Analysis & Chosen Representation**:
        - *Investigation of Existing Caches*: All existing clinical caches (`person-cache`, `task-cache`, `organization-cache`, etc.) store raw FHIR JSON `String`. Existing readers (`FhirCacheService`, `TaskCacheService`, `DefaultTaskService`) parse cached values directly using `FhirContext.newJsonParser().parseResource(...)`.
        - *Incompatibility of Outer JSON Envelope*: An outer wrapper (`{ authoritativeVersion: ..., resource: ... }`) is invalid FHIR JSON and would cause `DataFormatException` in all existing readers, violating the constraint not to migrate all cache consumers in 08.04D.
        - *Chosen Smallest Compatible Representation (`MnemeCachedResource`)*: Authoritative version provenance is embedded directly inside the standard FHIR resource under `Resource.meta.extension` using canonical URI `http://harmonia.fhirfactory.net/structure/authoritative-version` (with `Integer64Type` or numeric representation).
        - *Backwards Compatibility & Atomicity*: The cached value remains 100% valid raw FHIR JSON. HAPI FHIR parsers across all existing consumers parse it seamlessly without schema errors. The resource payload and its authoritative provenance are read and written atomically in a single Infinispan cache entry, eliminating dual-cache consistency hazards.
        - *Legacy Cache Entries*: Any pre-existing cache entry lacking the authoritative-version extension is treated as $V_{cached} = 0$, allowing governed writes to safely converge over legacy entries.
    - Infinispan `MetadataValue.getVersion()` remains strictly an opaque CAS token for `replaceWithVersion`.
- **Convergence Semantics**:
    - `ConvergenceStatus.CONVERGED` explicitly means **"no further cache action is required"** (the cache already reflects this authoritative version or a newer authoritative progression), NOT "the supplied committed representation was written to cache".
    - If the cached representation is newer or equal ($V_{cached} \ge V_{committed}$), convergence immediately succeeds with `CONVERGED` without overwriting the newer state.
    - If the cached representation is older ($V_{cached} < V_{committed}$), conditional replacement is attempted using the opaque Hot Rod version token (`replaceWithVersion`).
    - If the cache entry is absent (cold cache), `putIfAbsent` is executed.
    - If CAS fails or retries are exhausted under contention/outage, `ConvergenceStatus.DEGRADED` is returned without unconditionally evicting the cache entry.

---

**Composed Architecture & Sequence Flows**

```mermaid
graph TD
  Caller[Caller: BEFE / Pylai / Erga] -->|GovernedRead + Proposed State| GW[DefaultGovernedWriter in mnemosyne-clinical]
  GW -->|1. Authorize Action| Themis[ThemisAuthorizer]
  Themis -->|Allow / Deny| GW
  GW -->|2. Consume Token (UPDATE only)| MnemeCoord[Mneme: ActiveStateCoordinator]
  MnemeCoord -->|CONSUMED / STALE / UNAVAILABLE| GW
  GW -->|3. Atomic Persistence| Mnemosyne[Mnemosyne: AuthoritativePersistencePort]
  Mnemosyne -->|COMMITTED / CONFLICT / UNKNOWN| GW
  GW -->|4. Guarded CAS Convergence| MnemeConv[Mneme: ActiveStateConvergencePort]
  MnemeConv -->|CONVERGED / DEGRADED| GW
  GW -->|WriteResult<T>| Caller
```

**UPDATE Execution Flow**
1. **Validate Input**: Check non-null `GovernedRead`, `proposed`, `securityContext`, and matching resource types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.UPDATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Active-State Progression (Mneme)**:
    - Call `coordinator.consume(current.key(), current.activeToken())`.
    - If `STALE`: return `WriteResult.activeStateConflict(ActiveStateConflict.of(key, "Active state token is stale"))`.
    - If `UNAVAILABLE`: return `WriteResult.notCommitted(key, "Active state coordination unavailable")`.
4. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.update(current.key(), proposed, current.expectedAuthoritativeVersion())`.
    - If `Conflict(EXPECTED_VERSION_MISMATCH)`: return `WriteResult.authoritativeConflict(conflict.conflict())`. (Token remains consumed).
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`. (Token remains consumed).
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`. (Token remains consumed, no retry).
5. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, newVersion)`: call `convergencePort.converge(key, resource, newVersion)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, newVersion)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, newVersion, "Mneme cache convergence degraded")`.

**CREATE Execution Flow**
1. **Validate Input**: Check non-null `ResourceKey`, `resource`, `securityContext`, and matching types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.CREATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.create(key, resource)`.
    - If `Conflict(RESOURCE_ALREADY_EXISTS)`: return `WriteResult.authoritativeConflict(conflict.conflict())`.
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`.
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`.
4. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, v1)`: call `convergencePort.converge(key, resource, v1)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, v1)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, v1, "Mneme cache convergence degraded")`.

---

**Failure and Outcome Mapping Matrix**

| Scenario / Condition | Themis Decision | Mneme Coordination | Mnemosyne Persistence | Mneme Convergence | WriteResult Variant | Token Consumed? | Database State |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **A. Themis Denies** | `DENY` | Not Called | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **B. Mneme Stale** | `ALLOW` | `STALE` | Not Called | Not Called | `WriteResult.ActiveConflict` | No | Unchanged |
| **C. Mneme Unavailable** | `ALLOW` | `UNAVAILABLE` | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **D. Authoritative Conflict** | `ALLOW` | `CONSUMED` | `Conflict(VERSION_MISMATCH)` | Not Called | `WriteResult.AuthoritativeConflict` | Yes | Unchanged |
| **E. Persistence Not Committed** | `ALLOW` | `CONSUMED` | `NotCommitted` | Not Called | `WriteResult.NotCommitted` | Yes | Unchanged |
| **F. Persistence Unknown** | `ALLOW` | `CONSUMED` | `OutcomeUnknown` | Not Called | `WriteResult.OutcomeUnknown` | Yes | Ambiguous (No Retry) |
| **G. Committed + Converged** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `CONVERGED` | `WriteResult.Committed(CONVERGED)` | Yes | Committed ($V$) |
| **H. Committed + Degraded** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `DEGRADED` | `WriteResult.Committed(DEGRADED)` | Yes | Committed ($V$) |
| **I. CREATE Duplicate** | `ALLOW` | N/A | `Conflict(ALREADY_EXISTS)` | Not Called | `WriteResult.AuthoritativeConflict` | N/A | Unchanged |

---

**Post-Commit Mneme Convergence Algorithm**

```java
public class HotRodMnemeConvergence implements ActiveStateConvergencePort {
    public static final String AUTHORITATIVE_VERSION_EXT_URL = "http://harmonia.fhirfactory.net/structure/authoritative-version";

    private final RemoteCacheManager cacheManager;
    private final FhirContext fhirContext;

    @Override
    public <T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion) {
        String cacheName = resolveCacheName(key.resourceType());
        RemoteCache<String, String> cache = cacheManager.getCache(cacheName);
        if (cache == null) return ConvergenceStatus.DEGRADED;

        // Attach explicit authoritative-version provenance into FHIR meta.extension
        IBaseResource fhirResource = (IBaseResource) committedResource;
        MnemeCachedResource.attachAuthoritativeVersion(fhirResource, committedVersion);
        String payloadJson = fhirContext.newJsonParser().encodeResourceToString(fhirResource);
        long targetVer = committedVersion.value();

        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                MetadataValue<String> currentMeta = cache.getWithMetadata(key.id());
                if (currentMeta != null) {
                    long cachedVer = MnemeCachedResource.extractAuthoritativeVersion(currentMeta.getValue());
                    // Newer-Version Invariant: never overwrite newer cache state.
                    // CONVERGED means no further cache action is required.
                    if (cachedVer >= targetVer) {
                        return ConvergenceStatus.CONVERGED;
                    }
                    // Hot Rod opaque CAS token replacement
                    boolean casSuccess = cache.replaceWithVersion(key.id(), payloadJson, currentMeta.getVersion());
                    if (casSuccess) return ConvergenceStatus.CONVERGED;
                } else {
                    String existing = cache.putIfAbsent(key.id(), payloadJson);
                    if (existing == null) return ConvergenceStatus.CONVERGED;
                }
            } catch (Exception e) {
                return ConvergenceStatus.DEGRADED;
            }
        }
        // CAS exhausted: DO NOT evict entry (preserves newer versions)
        return ConvergenceStatus.DEGRADED;
    }
}
```

---

**Dependency Graph**

```
calliope (contracts: GovernedWriter, GovernedRead, WriteResult, ActiveStateCoordinator,
          ActiveStateConvergencePort, AuthoritativeVersion, ConvergenceStatus)
  └── depends on: themis-api (ThemisAuthorizer, ThemisSecurityContext)
  └── depends on: hapi-fhir-structures-r5

hestia/mneme-cluster (implementations: HotRodActiveStateCoordinator, HotRodMnemeConvergence)
  └── depends on: calliope, infinispan-client-hotrod

hestia/mnemosyne-clinical (persistence & composition: AuthoritativePersistencePort, AuthoritativePersistenceService, DefaultGovernedWriter)
  └── depends on: calliope, themis-api, themis-core, spring-boot-starter-data-jpa

iris/iris-befe (presentation gateway)
  └── depends on: calliope, themis-api, themis-core, hestia/mneme-cluster
```

**Graph Properties**:
- Purely unidirectional.
- Zero cyclic dependencies.
- No JPA, Hibernate, or Infinispan types leaked into `calliope` or `GovernedWriter` API.
- `DefaultGovernedWriter` lives in `mnemosyne-clinical` as a runtime orchestrator.

---

**Concrete File Structure Changes**

**Files to Add:**
1. `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ActiveStateConvergencePort.java` (Domain convergence port interface)
2. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/MnemeCachedResource.java` (Helper to attach/extract `AuthoritativeVersion` provenance via standard FHIR `Resource.meta.extension`)
3. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergence.java` (Hot Rod CAS convergence implementation)
4. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriter.java` (Production orchestrator in runtime module)
5. `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriterTest.java` (Comprehensive composition unit tests)
6. `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergenceTest.java` (Convergence CAS & newer-version tests)
7. `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteCompositionArchitectureTest.java` (ArchUnit rules)

**Files Preserved Without Relocation:**
1. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java` (Remains in `mnemosyne-clinical`)
2. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/model/AuthoritativePersistenceResult.java` (Remains in `mnemosyne-clinical`)
3. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java` (Remains in `mnemosyne-clinical`)

**Testing**

**Validation Approach**  
Verification of Step 08.04D composition will be performed using a multi-tier testing strategy:
1. **Composition Unit Tests (`DefaultGovernedWriterTest`)**:
    - Uses Mockito-based test doubles for `ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, and `ActiveStateConvergencePort`.
    - Validates all 15 permutations of the Failure and Outcome Mapping Matrix.
2. **Convergence CAS Unit & Scenario Tests (`HotRodMnemeConvergenceTest`)**:
    - Validates the newer-version protection invariant ($V_{cached} \ge V_{committed}$ is preserved).
    - Validates cold-cache population via `putIfAbsent`.
    - Validates CAS loss handling and non-eviction on retry exhaustion.
3. **ArchUnit Architecture Tests (`GovernedWriteCompositionArchitectureTest`)**:
    - Enforces package layering, zero dependency cycles, and zero JPA/Infinispan leaks into `calliope`.

---

**Key Scenarios**

**Scenario 1: Authorised UPDATE Happy Path**
- **Setup**: `GovernedRead` with V1 and token `T1`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne commits V2 $\to$ Convergence succeeds with V2.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.CONVERGED`, version `AuthoritativeVersion.of(2L)`.

**Scenario 2: Themis Denied**
- **Setup**: `GovernedRead` with V1. Themis mock configured to return `DENY`.
- **Flow**: Themis denies $\to$ write immediately terminates.
- **Outcome**: `WriteResult.NotCommitted`. Zero invocations on `ActiveStateCoordinator` and `AuthoritativePersistencePort`.

**Scenario 3: Stale ActiveStateToken**
- **Setup**: `GovernedRead` with stale token `T_stale`.
- **Flow**: Themis allows $\to$ Mneme returns `STALE` $\to$ write immediately terminates.
- **Outcome**: `WriteResult.ActiveConflict`. Zero invocations on `AuthoritativePersistencePort`.

**Scenario 4: Mneme Unavailable**
- **Setup**: `GovernedRead` with V1. `ActiveStateCoordinator` returns `UNAVAILABLE`.
- **Flow**: Themis allows $\to$ Mneme returns `UNAVAILABLE` $\to$ write terminates with fail-fast visible error.
- **Outcome**: `WriteResult.NotCommitted` (fail-fast, no JVM-local fallback).

**Scenario 5: Authoritative Precondition Conflict (Expected Version Mismatch)**
- **Setup**: `GovernedRead` with V1. Mnemosyne returns `Conflict(EXPECTED_VERSION_MISMATCH, current=V3)`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne returns Conflict.
- **Outcome**: `WriteResult.AuthoritativeConflict(EXPECTED_VERSION_MISMATCH)`. Token `T1` remains consumed.

**Scenario 6: Authoritative Persistence Unknown**
- **Setup**: Mnemosyne returns `OutcomeUnknown("Commit ACK timeout")`.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne returns `OutcomeUnknown`.
- **Outcome**: `WriteResult.OutcomeUnknown`. Token remains consumed. No automatic retry attempted.

**Scenario 7: Committed with Degraded Convergence**
- **Setup**: Mnemosyne commits V2, but Mneme convergence throws timeout exception.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne commits V2 $\to$ Convergence returns `DEGRADED`.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.DEGRADED`. Durable commit remains intact.

**Scenario 8: Delayed Older Convergence Preserves Newer Cache State**
- **Setup**: Cache currently contains V43. Delayed convergence thread attempts to converge committed V42.
- **Flow**: `HotRodMnemeConvergence.converge` reads cached V43 $\to$ detects $43 \ge 42 \to$ terminates immediately with `CONVERGED`.
- **Outcome**: Cache remains unchanged at V43. V42 does not overwrite or evict V43.

**Scenario 9: Authorised CREATE Happy Path & Duplicate Detection**
- **Setup**: Proposed new resource `Patient/100`.
- **Flow A**: Themis allows $\to$ Mnemosyne creates V1 $\to$ Convergence populates cache with V1 $\to$ returns `WriteResult.Committed(V1)`.
- **Flow B**: Concurrent CREATE with same key $\to$ Mnemosyne unique constraint triggers $\to$ returns `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)`.

---

**Architecture Guardrails to Add**
- `GovernedWriteCompositionArchitectureTest`:
    1. `GovernedWriter` resides in `calliope` and `DefaultGovernedWriter` resides in `hestia/mnemosyne-clinical`.
    2. `calliope` must have zero dependencies on `hestia`, `iris`, `pylai`, `energeia`, `agora`, or `paradeigma`.
    3. `DefaultGovernedWriter` must not import or depend on `org.infinispan..`, `jakarta.persistence..`, `org.hibernate..`, or `ca.uhn.fhir.jpa..`.
    4. `AuthoritativePersistencePort` and `ActiveStateCoordinator` must not expose DELETE, REMOVE, or PURGE methods (ADR-020).
    5. `HotRodMnemeConvergence` must not depend on `AuthoritativePersistencePort` or JPA entities.

**Bypasses & Deferred Work**

**Catalogue of Remaining Bypasses (Post-08.04D)**

While Step 08.04D establishes the complete production governed-write composition, existing runtime components still contain legacy write-path bypasses that will be migrated in subsequent steps:

| Subsystem / Component | File / Location | Legacy Behavior | Target Migration Step |
| :--- | :--- | :--- | :--- |
| **Iris BEFE** | `FhirCacheService.java` | Unconditional `remoteCache.put(id, json)` and `remoteCache.remove(id)` | Task 08.05 / 08.06 (BEFE Governed Writer Migration) |
| **Mneme Persistence** | `FhirRestCacheStore.java` | Asynchronous write-behind store bypassing governed coordination | Task 08.07 (Write-Behind Decommissioning) |
| **Mnemosyne Clinical** | HAPI Resource Providers | Direct `FhirStorageService.createResource/updateResource` | Task 08.05 / Task 09 |
| **Energeia Ponos** | `ErgonBase.java` / `PraxisService.java` | Direct cache updates for task synchronization | Task 08.06 (Workflow Task Governed Migration) |
| **Pylai MLLP** | `pylai-mllp-in` / `pylai-fhir-provider-registry` | Direct cache and storage mutations | Task 08.06 (Gateway Governed Migration) |

---

**Explicitly Deferred Work**
1. **Task 09 (Cache-Aside Point Reads)**: Governed point reads (`GovernedRead<T>`), cache-aside read-through, and read repairs will be designed and implemented in Task 09.
2. **UNKNOWN Outcome Reconciliation Framework**: Automated asynchronous reconciliation of `WriteResult.OutcomeUnknown` is deferred to a dedicated post-Task 08 reconciliation capability.
3. **Legacy Caller Migration**: Modifying BEFE REST endpoints, Pylai gateways, and Ponos routes to call `GovernedWriter` will occur in follow-up integration steps.

**Delivery Steps**

*** Step 1: Define Governed-Write Convergence Contract in Calliope**  
Define pure convergence port contract in Calliope while preserving persistence ports in Mnemosyne Clinical.

- Define `ActiveStateConvergencePort` in `calliope` (`net.fhirfactory.harmonia.model.governedwrite`) with method `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
- Verify `AuthoritativePersistencePort<T>` and `AuthoritativePersistenceResult<T>` remain in `hestia/mnemosyne-clinical` without leaking into Calliope.
- Add unit tests in `calliope` verifying contract invariants, immutability, and null safety.

**Step 2: Implement Guarded Mneme Hot Rod Convergence in hestia-mneme-cluster**  
Mneme cluster provides production Hot Rod CAS-loop convergence with explicit authoritative version provenance metadata, ensuring older commits cannot overwrite or invalidate newer cache representations.

- Implement `MnemeCachedResource` helper in `hestia/mneme-cluster` to attach and extract explicit authoritative version provenance via standard FHIR `Resource.meta.extension` (`http://harmonia.fhirfactory.net/structure/authoritative-version`), maintaining 100% backwards compatibility with raw FHIR JSON cache contracts and zero dual-cache consistency hazards.
- Implement `HotRodMnemeConvergence` in `hestia/mneme-cluster` implementing `ActiveStateConvergencePort`.
- Implement CAS loop with `MetadataValue<String> currentMeta = cache.getWithMetadata(id)` and `cache.replaceWithVersion(id, payloadJson, currentMeta.getVersion())`.
- Enforce newer-version protection invariant: if cached authoritative version is greater than or equal to committed version, immediately return `ConvergenceStatus.CONVERGED` (signifying no further cache action required).
- Handle cold cache / empty cache via `putIfAbsent(id, payloadJson)` and treat unversioned legacy cache entries as version 0.
- Enforce failure handling: upon CAS exhaustion or cluster timeout, return `ConvergenceStatus.DEGRADED` without unconditionally evicting the cache entry.
- Implement comprehensive unit and scenario tests for `HotRodMnemeConvergence`.

**Step 3: Implement DefaultGovernedWriter Orchestrator in mnemosyne-clinical**  
GovernedWriter orchestrator composes Themis authorization, Mneme coordination, Mnemosyne persistence, and Mneme convergence into a unified CREATE and UPDATE workflow within the mnemosyne-clinical runtime module.

- Implement `DefaultGovernedWriter` in `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`) implementing `GovernedWriter`.
- Implement `update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext)`:
    - Validate parameters (fail-closed on null).
    - Evaluate Themis authorization via `ThemisAuthorizer.authorize(...)` with `ThemisAction.UPDATE`; return `WriteResult.NotCommitted` on deny.
    - Consume active state token via `ActiveStateCoordinator.consume(key, activeToken)`; return `WriteResult.ActiveConflict` on `STALE` or `WriteResult.NotCommitted` on `UNAVAILABLE`.
    - Persist update via `AuthoritativePersistencePort.update(key, proposed, expectedVersion)`.
    - Map persistence conflict (`EXPECTED_VERSION_MISMATCH`) to `WriteResult.AuthoritativeConflict` with consumed token preserved.
    - Map persistence unknown to `WriteResult.OutcomeUnknown` without automatic retry or token rollback.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` with `CONVERGED` or `DEGRADED`.
- Implement `create(ResourceKey key, T resource, ThemisSecurityContext securityContext)`:
    - Validate parameters.
    - Evaluate Themis authorization with `ThemisAction.CREATE`; return `WriteResult.NotCommitted` on deny.
    - Persist new resource via `AuthoritativePersistencePort.create(key, resource)` (atomic database uniqueness check); return `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)` on conflict.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
- Implement comprehensive composition unit tests covering all 15 outcome permutations and concurrency scenarios.

**Step 4: Add ArchUnit Architecture Guardrails and Integration Verification**  
Paradeigma test suite enforces architectural layering, isolation, and absence of physical deletion across the composed governed-write stack.

- Update `GovernedWriteContractArchitectureTest` in `paradeigma/paradeigma-test` to verify that `GovernedWriter` contracts remain pure in `calliope`.
- Update `MnemosyneAuthoritativePersistenceArchitectureTest` to assert that `DefaultGovernedWriter` in `mnemosyne-clinical` remains free of direct Infinispan Hot Rod classes.
- Add ArchUnit rules verifying that `GovernedWriter`, `AuthoritativePersistencePort`, and `ActiveStateCoordinator` do not expose DELETE or REMOVE methods.
- Verify full test suite and architecture rules with `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`.



I am creating the ActiveStateConvergencePort interface in Calliope and adding unit tests for contract invariants and null safety.

**Requirements**

**Overview & Goals**  
Step 08.04D designs the production composition of the governed-write capabilities established in Steps 08.04A (Caller-facing Contracts), 08.04B (Mneme Active-State Coordination), and 08.04C (Mnemosyne Atomic Authoritative Persistence).

The core objective is to compose:
1. **Themis Security Authorization** (`ThemisAuthorizer`, `ThemisSecurityContext`)
2. **Mneme Active-State Coordination** (`ActiveStateCoordinator.consume`)
3. **Mnemosyne Authoritative Persistence** (`AuthoritativePersistencePort.create` / `update`)
4. **Guarded Mneme Post-Commit Convergence** (`ActiveStateConvergencePort.converge`)

into a single unified, deterministic `DefaultGovernedWriter` implementation supporting `CREATE` and `UPDATE` operations while upholding the fundamental architectural baselines (ADR-018, ADR-019, ADR-020).

**Scope**
- **In Scope**:
    - Architectural placement of `DefaultGovernedWriter` composition in existing runtime module `hestia/mnemosyne-clinical` (preserving `calliope` as a pure contract/model library and preserving `AuthoritativePersistencePort` in `mnemosyne-clinical`).
    - Explicit execution sequence for governed `UPDATE` and governed `CREATE`.
    - Formal architectural decision on `CREATE` active-state coordination vs direct authoritative creation with post-commit convergence.
    - Themis authorization enforcement point (strictly evaluated *before* active token consumption and persistence).
    - Complete failure and outcome mapping matrix covering all combinations of Themis, Mneme, and Mnemosyne results.
    - Post-commit Mneme convergence CAS-loop algorithm with explicit authoritative-version provenance metadata and strict newer-version protection (preventing older commits from overwriting or invalidating newer cached versions).
    - Precise semantics for `ConvergenceStatus.CONVERGED` ("no further cache action required") and `ConvergenceStatus.DEGRADED`.
    - Truthful preservation of `UNKNOWN` commit outcomes without automatic retries or token rollbacks.
    - Comprehensive unit and scenario test plans covering all concurrency, authorization, coordination, persistence, and convergence states.
    - ArchUnit architectural rules enforcing module isolation, zero cyclic dependencies, and zero physical DELETE semantics.
    - Catalogue of existing bypasses and explicit boundary definition with Task 09 (Cache-aside point reads).

- **Out of Scope**:
    - Full caller migration across BEFE, Pylai, and Energeia (deferred to subsequent integration steps).
    - Implementation of physical DELETE operations (strictly prohibited by ADR-020).
    - Implementation of Task 09 cache-aside point reads and read repairs.
    - Implementation of automated reconciliation machinery for `UNKNOWN` outcomes (deferred to future reconciliation task).
    - Creation of redundant Maven modules (`hestia-governance`, `clinical-write-orchestrator-service`).

**User Stories**
- **As a Clinical Gateway or BEFE Resource Provider**, I want to execute governed updates through a unified `GovernedWriter` that validates Themis authorization, consumes the active state token in Mneme, and conditionally commits to Mnemosyne, so that lost updates and unauthorized modifications are prevented.
- **As a System Administrator**, I want authoritative writes that succeed in Mnemosyne to remain committed even if transient cache convergence fails (returning `Committed` with `DEGRADED` convergence), so that durable persistence is never rolled back due to ephemeral cache grid issues.
- **As a Security Auditor**, I want all write operations to evaluate Themis policies before consuming coordination tokens or touching durable databases, so that unauthorized requests are rejected cleanly at the gate.

**Functional Requirements**
1. **Governed UPDATE Execution**:
    - Accepts `GovernedRead<T> current`, proposed state `T proposed`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately without touching Mneme or Mnemosyne.
    - Consumes observed `ActiveStateToken` via `ActiveStateCoordinator.consume`:
        - If `STALE`: aborts immediately and returns `WriteResult.ActiveConflict`.
        - If `UNAVAILABLE`: aborts immediately and returns `WriteResult.NotCommitted` (fail-fast, zero local fallback).
    - Executes authoritative persistence via `AuthoritativePersistencePort.update`:
        - If `Conflict(EXPECTED_VERSION_MISMATCH)`: returns `WriteResult.AuthoritativeConflict` (token remains consumed).
        - If `NotCommitted`: returns `WriteResult.NotCommitted` (token remains consumed).
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown` (token remains consumed, no retry).
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
2. **Governed CREATE Execution**:
    - Accepts `ResourceKey key`, initial state `T resource`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately.
    - Executes authoritative persistence via `AuthoritativePersistencePort.create` (Mnemosyne atomically enforces absence precondition via unique constraints):
        - If `Conflict(RESOURCE_ALREADY_EXISTS)`: returns `WriteResult.AuthoritativeConflict`.
        - If `NotCommitted`: returns `WriteResult.NotCommitted`.
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown`.
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
3. **No Physical DELETE**:
    - `GovernedWriter` exposes only `create` and `update`. All lifecycle transitions (e.g. deprecation, deactivation, suspension) are authoritative `update` operations with FHIR status codes.

**Non-Functional Requirements**
- **Independence & Isolation**: Mnemosyne persistence must not depend on Mneme cache. Mneme cache loss must not affect durable database correctness.
- **Zero Local Fallback**: Distributed cache outages must produce visible failures (`UNAVAILABLE` / `DEGRADED`), never silent fallbacks to JVM-local synchronization maps.
- **Distinct Version Domains**: The four version domains (`ActiveStateToken`, `AuthoritativeVersion`, `FHIR meta.versionId`, `HTTP ETag`) must remain strictly isolated. No cross-domain version assumptions or synthetic versions.
- **Deterministic Outcome Mapping**: Every combination of underlying outcomes must map unambiguously into the sealed `WriteResult<T>` hierarchy.

**Technical Design**

**Current Implementation & Baseline Artifacts**
- **Caller-Facing Contracts (`calliope`)**:
    - `GovernedWriter`: Interface defining `<T> WriteResult<T> create(...)` and `<T> WriteResult<T> update(...)`.
    - `GovernedRead<T>`: Record packaging `ResourceKey`, payload `T`, `ActiveStateToken`, and `AuthoritativeVersion`.
    - `WriteResult<T>`: Sealed interface with records `Committed<T>`, `ActiveConflict<T>`, `AuthoritativeConflict<T>`, `OutcomeUnknown<T>`, and `NotCommitted<T>`.
    - `ActiveStateCoordinator`: Interface defining `observe(ResourceKey)` and `consume(ResourceKey, ActiveStateToken)`.
    - `ActiveStateConvergencePort`: Domain port interface defining `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
    - `ConvergenceStatus`: Sealed hierarchy / enum (`CONVERGED`, `DEGRADED`).
- **Mneme Active-State Coordination (`hestia/mneme-cluster`)**:
    - `HotRodActiveStateCoordinator`: Implements `ActiveStateCoordinator` using Hot Rod `replaceWithVersion` against `active-coordination-cache`.
- **Mnemosyne Authoritative Persistence (`hestia/mnemosyne-clinical`)**:
    - `AuthoritativePersistencePort<T>`: Port interface defining `create` and `update` (remains in `mnemosyne-clinical`).
    - `AuthoritativePersistenceService`: Implements `AuthoritativePersistencePort<IBaseResource>` using `TransactionTemplate` and conditional SQL updates.
    - `AuthoritativePersistenceResult<T>`: Sealed interface with `Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown` (remains in `mnemosyne-clinical`).
- **Themis Policy Evaluation (`themis/themis-api` & `themis/themis-core`)**:
    - `ThemisAuthorizer`: Evaluates `ThemisAuthorizationRequest` and returns `ThemisAuthorizationDecision`.
    - `ThemisSecurityContext`: Immutable context carrying originating/executing principals, authorities, correlation, and causation IDs.

---

**Key Architectural Decisions**

**Decision 1: Module Placement of GovernedWriter Composition**
- **Chosen Approach**: Place the production `DefaultGovernedWriter` implementation in `hestia/mnemosyne-clinical` (under `net.fhirfactory.harmonia.hapifhir.governed`), preserving `AuthoritativePersistencePort` and `AuthoritativePersistenceResult` in `mnemosyne-clinical`. `calliope` remains strictly a library for canonical models and domain contracts (`GovernedWriter`, `GovernedRead`, `WriteResult`, `ActiveStateCoordinator`, `ActiveStateConvergencePort`, `AuthoritativeVersion`, `ConvergenceStatus`). Hot Rod convergence logic (`HotRodMnemeConvergence`) is placed in `hestia/mneme-cluster`.
- **Rationale**:
    - `calliope` owns canonical schemas and semantic governance. It must never become an application runtime service orchestrator.
    - `hestia/mnemosyne-clinical` is the smallest existing runtime module that already depends on `calliope`, `themis-api`, and `themis-core`, and houses the clinical persistence implementations.
    - `DefaultGovernedWriter` interacts solely with domain interfaces (`ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, `ActiveStateConvergencePort`), without depending on Infinispan/Hot Rod concrete classes.
    - This avoids creating redundant Maven modules (e.g. `hestia-governance`), avoids dependency cycles, avoids leaking JPA/Infinispan into Calliope, and keeps contracts pure.

**Decision 2: Themis Authorization Precedence**
- **Chosen Approach**: Evaluate Themis authorization as the very first step in both `create` and `update`, strictly *before* consuming the `ActiveStateToken` in Mneme and *before* invoking Mnemosyne.
- **Rationale**: An unauthorized caller must not mutate active coordination state, burn ActiveStateTokens, or trigger database queries.

**Decision 3: CREATE Precondition Model & Active-State Coordination Evaluation**
- **Evaluation of Alternatives**:
    - **Option A (Chosen): Direct Authoritative CREATE with Post-Commit Convergence**
        - `Themis -> Mnemosyne CREATE -> Mneme Post-Commit Convergence`
    - **Option B (Rejected): Pre-Persistence Mneme Cold-State Coordination**
        - `Themis -> Mneme cold-state coordination -> Mnemosyne CREATE -> Mneme convergence`
- **Architectural Rationale & Invariant**:
    - An `ActiveStateToken` represents an observed point-in-time active state of an *existing* resource. For a newly created resource, no prior active state exists to observe.
    - Mnemosyne exclusively defines the durable authoritative state boundary (ADR-018) and owns the authoritative *absence* precondition (enforced via database unique constraints on `[resource_type, fhir_id]`).
    - Option B would require reserving a speculative token or placeholder marker in Mneme for an absent resource prior to database insertion. If Mnemosyne persistence subsequently fails, or if Mneme partitions/restarts, this creates phantom reservations, requires token rollback machinery (violating the fundamental invariant that consumed tokens are never rolled back), and creates distributed dual-master race conditions between cache and database.
    - **Invariant**: *Mnemosyne exclusively owns the authoritative absence precondition. CREATE operations deliberately do not perform pre-persistence active-state coordination in Mneme, and instead seed Mneme active state via post-commit convergence upon successful authoritative creation ($V=1$).*
    - No synthetic authoritative version, lease, or JVM lock is invented.

**Decision 4: Guarded Mneme Post-Commit Convergence with Distinct Version Domains**
- **Version Domain Separation**:
    1. `ActiveStateToken`: Opaque Hot Rod entry version CAS token for active-state coordination.
    2. `AuthoritativeVersion`: Mnemosyne durable database sequence version ($1, 2, 3\dots$).
    3. FHIR `Resource.meta.versionId`: FHIR specification payload field (string).
    4. HTTP ETag / `If-Match`: HTTP transport concurrency header.
    - *Invariant*: Convergence must never assume `FHIR meta.versionId == AuthoritativeVersion`.
- **Authoritative Provenance in Mneme**:
    - Post-commit convergence compares the incoming `AuthoritativeVersion` against explicit metadata describing the authoritative version represented by the cached Mneme value.
    - **Cache Contract Analysis & Chosen Representation**:
        - *Investigation of Existing Caches*: All existing clinical caches (`person-cache`, `task-cache`, `organization-cache`, etc.) store raw FHIR JSON `String`. Existing readers (`FhirCacheService`, `TaskCacheService`, `DefaultTaskService`) parse cached values directly using `FhirContext.newJsonParser().parseResource(...)`.
        - *Incompatibility of Outer JSON Envelope*: An outer wrapper (`{ authoritativeVersion: ..., resource: ... }`) is invalid FHIR JSON and would cause `DataFormatException` in all existing readers, violating the constraint not to migrate all cache consumers in 08.04D.
        - *Chosen Smallest Compatible Representation (`MnemeCachedResource`)*: Authoritative version provenance is embedded directly inside the standard FHIR resource under `Resource.meta.extension` using canonical URI `http://harmonia.fhirfactory.net/structure/authoritative-version` (with `Integer64Type` or numeric representation).
        - *Backwards Compatibility & Atomicity*: The cached value remains 100% valid raw FHIR JSON. HAPI FHIR parsers across all existing consumers parse it seamlessly without schema errors. The resource payload and its authoritative provenance are read and written atomically in a single Infinispan cache entry, eliminating dual-cache consistency hazards.
        - *Legacy Cache Entries*: Any pre-existing cache entry lacking the authoritative-version extension is treated as $V_{cached} = 0$, allowing governed writes to safely converge over legacy entries.
    - Infinispan `MetadataValue.getVersion()` remains strictly an opaque CAS token for `replaceWithVersion`.
- **Convergence Semantics**:
    - `ConvergenceStatus.CONVERGED` explicitly means **"no further cache action is required"** (the cache already reflects this authoritative version or a newer authoritative progression), NOT "the supplied committed representation was written to cache".
    - If the cached representation is newer or equal ($V_{cached} \ge V_{committed}$), convergence immediately succeeds with `CONVERGED` without overwriting the newer state.
    - If the cached representation is older ($V_{cached} < V_{committed}$), conditional replacement is attempted using the opaque Hot Rod version token (`replaceWithVersion`).
    - If the cache entry is absent (cold cache), `putIfAbsent` is executed.
    - If CAS fails or retries are exhausted under contention/outage, `ConvergenceStatus.DEGRADED` is returned without unconditionally evicting the cache entry.

---

**Composed Architecture & Sequence Flows**

```mermaid
graph TD
  Caller[Caller: BEFE / Pylai / Erga] -->|GovernedRead + Proposed State| GW[DefaultGovernedWriter in mnemosyne-clinical]
  GW -->|1. Authorize Action| Themis[ThemisAuthorizer]
  Themis -->|Allow / Deny| GW
  GW -->|2. Consume Token (UPDATE only)| MnemeCoord[Mneme: ActiveStateCoordinator]
  MnemeCoord -->|CONSUMED / STALE / UNAVAILABLE| GW
  GW -->|3. Atomic Persistence| Mnemosyne[Mnemosyne: AuthoritativePersistencePort]
  Mnemosyne -->|COMMITTED / CONFLICT / UNKNOWN| GW
  GW -->|4. Guarded CAS Convergence| MnemeConv[Mneme: ActiveStateConvergencePort]
  MnemeConv -->|CONVERGED / DEGRADED| GW
  GW -->|WriteResult<T>| Caller
```

**UPDATE Execution Flow**
1. **Validate Input**: Check non-null `GovernedRead`, `proposed`, `securityContext`, and matching resource types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.UPDATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Active-State Progression (Mneme)**:
    - Call `coordinator.consume(current.key(), current.activeToken())`.
    - If `STALE`: return `WriteResult.activeStateConflict(ActiveStateConflict.of(key, "Active state token is stale"))`.
    - If `UNAVAILABLE`: return `WriteResult.notCommitted(key, "Active state coordination unavailable")`.
4. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.update(current.key(), proposed, current.expectedAuthoritativeVersion())`.
    - If `Conflict(EXPECTED_VERSION_MISMATCH)`: return `WriteResult.authoritativeConflict(conflict.conflict())`. (Token remains consumed).
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`. (Token remains consumed).
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`. (Token remains consumed, no retry).
5. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, newVersion)`: call `convergencePort.converge(key, resource, newVersion)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, newVersion)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, newVersion, "Mneme cache convergence degraded")`.

**CREATE Execution Flow**
1. **Validate Input**: Check non-null `ResourceKey`, `resource`, `securityContext`, and matching types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.CREATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.create(key, resource)`.
    - If `Conflict(RESOURCE_ALREADY_EXISTS)`: return `WriteResult.authoritativeConflict(conflict.conflict())`.
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`.
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`.
4. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, v1)`: call `convergencePort.converge(key, resource, v1)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, v1)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, v1, "Mneme cache convergence degraded")`.

---

**Failure and Outcome Mapping Matrix**

| Scenario / Condition | Themis Decision | Mneme Coordination | Mnemosyne Persistence | Mneme Convergence | WriteResult Variant | Token Consumed? | Database State |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **A. Themis Denies** | `DENY` | Not Called | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **B. Mneme Stale** | `ALLOW` | `STALE` | Not Called | Not Called | `WriteResult.ActiveConflict` | No | Unchanged |
| **C. Mneme Unavailable** | `ALLOW` | `UNAVAILABLE` | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **D. Authoritative Conflict** | `ALLOW` | `CONSUMED` | `Conflict(VERSION_MISMATCH)` | Not Called | `WriteResult.AuthoritativeConflict` | Yes | Unchanged |
| **E. Persistence Not Committed** | `ALLOW` | `CONSUMED` | `NotCommitted` | Not Called | `WriteResult.NotCommitted` | Yes | Unchanged |
| **F. Persistence Unknown** | `ALLOW` | `CONSUMED` | `OutcomeUnknown` | Not Called | `WriteResult.OutcomeUnknown` | Yes | Ambiguous (No Retry) |
| **G. Committed + Converged** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `CONVERGED` | `WriteResult.Committed(CONVERGED)` | Yes | Committed ($V$) |
| **H. Committed + Degraded** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `DEGRADED` | `WriteResult.Committed(DEGRADED)` | Yes | Committed ($V$) |
| **I. CREATE Duplicate** | `ALLOW` | N/A | `Conflict(ALREADY_EXISTS)` | Not Called | `WriteResult.AuthoritativeConflict` | N/A | Unchanged |

---

**Post-Commit Mneme Convergence Algorithm**

```java
public class HotRodMnemeConvergence implements ActiveStateConvergencePort {
    public static final String AUTHORITATIVE_VERSION_EXT_URL = "http://harmonia.fhirfactory.net/structure/authoritative-version";

    private final RemoteCacheManager cacheManager;
    private final FhirContext fhirContext;

    @Override
    public <T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion) {
        String cacheName = resolveCacheName(key.resourceType());
        RemoteCache<String, String> cache = cacheManager.getCache(cacheName);
        if (cache == null) return ConvergenceStatus.DEGRADED;

        // Attach explicit authoritative-version provenance into FHIR meta.extension
        IBaseResource fhirResource = (IBaseResource) committedResource;
        MnemeCachedResource.attachAuthoritativeVersion(fhirResource, committedVersion);
        String payloadJson = fhirContext.newJsonParser().encodeResourceToString(fhirResource);
        long targetVer = committedVersion.value();

        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                MetadataValue<String> currentMeta = cache.getWithMetadata(key.id());
                if (currentMeta != null) {
                    long cachedVer = MnemeCachedResource.extractAuthoritativeVersion(currentMeta.getValue());
                    // Newer-Version Invariant: never overwrite newer cache state.
                    // CONVERGED means no further cache action is required.
                    if (cachedVer >= targetVer) {
                        return ConvergenceStatus.CONVERGED;
                    }
                    // Hot Rod opaque CAS token replacement
                    boolean casSuccess = cache.replaceWithVersion(key.id(), payloadJson, currentMeta.getVersion());
                    if (casSuccess) return ConvergenceStatus.CONVERGED;
                } else {
                    String existing = cache.putIfAbsent(key.id(), payloadJson);
                    if (existing == null) return ConvergenceStatus.CONVERGED;
                }
            } catch (Exception e) {
                return ConvergenceStatus.DEGRADED;
            }
        }
        // CAS exhausted: DO NOT evict entry (preserves newer versions)
        return ConvergenceStatus.DEGRADED;
    }
}
```

---

**Dependency Graph**

```
calliope (contracts: GovernedWriter, GovernedRead, WriteResult, ActiveStateCoordinator,
          ActiveStateConvergencePort, AuthoritativeVersion, ConvergenceStatus)
  └── depends on: themis-api (ThemisAuthorizer, ThemisSecurityContext)
  └── depends on: hapi-fhir-structures-r5

hestia/mneme-cluster (implementations: HotRodActiveStateCoordinator, HotRodMnemeConvergence)
  └── depends on: calliope, infinispan-client-hotrod

hestia/mnemosyne-clinical (persistence & composition: AuthoritativePersistencePort, AuthoritativePersistenceService, DefaultGovernedWriter)
  └── depends on: calliope, themis-api, themis-core, spring-boot-starter-data-jpa

iris/iris-befe (presentation gateway)
  └── depends on: calliope, themis-api, themis-core, hestia/mneme-cluster
```

**Graph Properties**:
- Purely unidirectional.
- Zero cyclic dependencies.
- No JPA, Hibernate, or Infinispan types leaked into `calliope` or `GovernedWriter` API.
- `DefaultGovernedWriter` lives in `mnemosyne-clinical` as a runtime orchestrator.

---

**Concrete File Structure Changes**

**Files to Add:**
1. `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ActiveStateConvergencePort.java` (Domain convergence port interface)
2. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/MnemeCachedResource.java` (Helper to attach/extract `AuthoritativeVersion` provenance via standard FHIR `Resource.meta.extension`)
3. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergence.java` (Hot Rod CAS convergence implementation)
4. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriter.java` (Production orchestrator in runtime module)
5. `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriterTest.java` (Comprehensive composition unit tests)
6. `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergenceTest.java` (Convergence CAS & newer-version tests)
7. `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteCompositionArchitectureTest.java` (ArchUnit rules)

**Files Preserved Without Relocation:**
1. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java` (Remains in `mnemosyne-clinical`)
2. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/model/AuthoritativePersistenceResult.java` (Remains in `mnemosyne-clinical`)
3. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java` (Remains in `mnemosyne-clinical`)

**Testing**

**Validation Approach**  
Verification of Step 08.04D composition will be performed using a multi-tier testing strategy:
1. **Composition Unit Tests (`DefaultGovernedWriterTest`)**:
    - Uses Mockito-based test doubles for `ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, and `ActiveStateConvergencePort`.
    - Validates all 15 permutations of the Failure and Outcome Mapping Matrix.
2. **Convergence CAS Unit & Scenario Tests (`HotRodMnemeConvergenceTest`)**:
    - Validates the newer-version protection invariant ($V_{cached} \ge V_{committed}$ is preserved).
    - Validates cold-cache population via `putIfAbsent`.
    - Validates CAS loss handling and non-eviction on retry exhaustion.
3. **ArchUnit Architecture Tests (`GovernedWriteCompositionArchitectureTest`)**:
    - Enforces package layering, zero dependency cycles, and zero JPA/Infinispan leaks into `calliope`.

---

**Key Scenarios**

**Scenario 1: Authorised UPDATE Happy Path**
- **Setup**: `GovernedRead` with V1 and token `T1`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne commits V2 $\to$ Convergence succeeds with V2.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.CONVERGED`, version `AuthoritativeVersion.of(2L)`.

**Scenario 2: Themis Denied**
- **Setup**: `GovernedRead` with V1. Themis mock configured to return `DENY`.
- **Flow**: Themis denies $\to$ write immediately terminates.
- **Outcome**: `WriteResult.NotCommitted`. Zero invocations on `ActiveStateCoordinator` and `AuthoritativePersistencePort`.

**Scenario 3: Stale ActiveStateToken**
- **Setup**: `GovernedRead` with stale token `T_stale`.
- **Flow**: Themis allows $\to$ Mneme returns `STALE` $\to$ write immediately terminates.
- **Outcome**: `WriteResult.ActiveConflict`. Zero invocations on `AuthoritativePersistencePort`.

**Scenario 4: Mneme Unavailable**
- **Setup**: `GovernedRead` with V1. `ActiveStateCoordinator` returns `UNAVAILABLE`.
- **Flow**: Themis allows $\to$ Mneme returns `UNAVAILABLE` $\to$ write terminates with fail-fast visible error.
- **Outcome**: `WriteResult.NotCommitted` (fail-fast, no JVM-local fallback).

**Scenario 5: Authoritative Precondition Conflict (Expected Version Mismatch)**
- **Setup**: `GovernedRead` with V1. Mnemosyne returns `Conflict(EXPECTED_VERSION_MISMATCH, current=V3)`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne returns Conflict.
- **Outcome**: `WriteResult.AuthoritativeConflict(EXPECTED_VERSION_MISMATCH)`. Token `T1` remains consumed.

**Scenario 6: Authoritative Persistence Unknown**
- **Setup**: Mnemosyne returns `OutcomeUnknown("Commit ACK timeout")`.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne returns `OutcomeUnknown`.
- **Outcome**: `WriteResult.OutcomeUnknown`. Token remains consumed. No automatic retry attempted.

**Scenario 7: Committed with Degraded Convergence**
- **Setup**: Mnemosyne commits V2, but Mneme convergence throws timeout exception.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne commits V2 $\to$ Convergence returns `DEGRADED`.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.DEGRADED`. Durable commit remains intact.

**Scenario 8: Delayed Older Convergence Preserves Newer Cache State**
- **Setup**: Cache currently contains V43. Delayed convergence thread attempts to converge committed V42.
- **Flow**: `HotRodMnemeConvergence.converge` reads cached V43 $\to$ detects $43 \ge 42 \to$ terminates immediately with `CONVERGED`.
- **Outcome**: Cache remains unchanged at V43. V42 does not overwrite or evict V43.

**Scenario 9: Authorised CREATE Happy Path & Duplicate Detection**
- **Setup**: Proposed new resource `Patient/100`.
- **Flow A**: Themis allows $\to$ Mnemosyne creates V1 $\to$ Convergence populates cache with V1 $\to$ returns `WriteResult.Committed(V1)`.
- **Flow B**: Concurrent CREATE with same key $\to$ Mnemosyne unique constraint triggers $\to$ returns `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)`.

---

**Architecture Guardrails to Add**
- `GovernedWriteCompositionArchitectureTest`:
    1. `GovernedWriter` resides in `calliope` and `DefaultGovernedWriter` resides in `hestia/mnemosyne-clinical`.
    2. `calliope` must have zero dependencies on `hestia`, `iris`, `pylai`, `energeia`, `agora`, or `paradeigma`.
    3. `DefaultGovernedWriter` must not import or depend on `org.infinispan..`, `jakarta.persistence..`, `org.hibernate..`, or `ca.uhn.fhir.jpa..`.
    4. `AuthoritativePersistencePort` and `ActiveStateCoordinator` must not expose DELETE, REMOVE, or PURGE methods (ADR-020).
    5. `HotRodMnemeConvergence` must not depend on `AuthoritativePersistencePort` or JPA entities.

**Bypasses & Deferred Work**

**Catalogue of Remaining Bypasses (Post-08.04D)**

While Step 08.04D establishes the complete production governed-write composition, existing runtime components still contain legacy write-path bypasses that will be migrated in subsequent steps:

| Subsystem / Component | File / Location | Legacy Behavior | Target Migration Step |
| :--- | :--- | :--- | :--- |
| **Iris BEFE** | `FhirCacheService.java` | Unconditional `remoteCache.put(id, json)` and `remoteCache.remove(id)` | Task 08.05 / 08.06 (BEFE Governed Writer Migration) |
| **Mneme Persistence** | `FhirRestCacheStore.java` | Asynchronous write-behind store bypassing governed coordination | Task 08.07 (Write-Behind Decommissioning) |
| **Mnemosyne Clinical** | HAPI Resource Providers | Direct `FhirStorageService.createResource/updateResource` | Task 08.05 / Task 09 |
| **Energeia Ponos** | `ErgonBase.java` / `PraxisService.java` | Direct cache updates for task synchronization | Task 08.06 (Workflow Task Governed Migration) |
| **Pylai MLLP** | `pylai-mllp-in` / `pylai-fhir-provider-registry` | Direct cache and storage mutations | Task 08.06 (Gateway Governed Migration) |

---

**Explicitly Deferred Work**
1. **Task 09 (Cache-Aside Point Reads)**: Governed point reads (`GovernedRead<T>`), cache-aside read-through, and read repairs will be designed and implemented in Task 09.
2. **UNKNOWN Outcome Reconciliation Framework**: Automated asynchronous reconciliation of `WriteResult.OutcomeUnknown` is deferred to a dedicated post-Task 08 reconciliation capability.
3. **Legacy Caller Migration**: Modifying BEFE REST endpoints, Pylai gateways, and Ponos routes to call `GovernedWriter` will occur in follow-up integration steps.

**Delivery Steps**

**✓ Step 1: Define Governed-Write Convergence Contract in Calliope**  
Define pure convergence port contract in Calliope while preserving persistence ports in Mnemosyne Clinical.

- Define `ActiveStateConvergencePort` in `calliope` (`net.fhirfactory.harmonia.model.governedwrite`) with method `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
- Verify `AuthoritativePersistencePort<T>` and `AuthoritativePersistenceResult<T>` remain in `hestia/mnemosyne-clinical` without leaking into Calliope.
- Add unit tests in `calliope` verifying contract invariants, immutability, and null safety.

*** Step 2: Implement Guarded Mneme Hot Rod Convergence in hestia-mneme-cluster**  
Mneme cluster provides production Hot Rod CAS-loop convergence with explicit authoritative version provenance metadata, ensuring older commits cannot overwrite or invalidate newer cache representations.

- Implement `MnemeCachedResource` helper in `hestia/mneme-cluster` to attach and extract explicit authoritative version provenance via standard FHIR `Resource.meta.extension` (`http://harmonia.fhirfactory.net/structure/authoritative-version`), maintaining 100% backwards compatibility with raw FHIR JSON cache contracts and zero dual-cache consistency hazards.
- Implement `HotRodMnemeConvergence` in `hestia/mneme-cluster` implementing `ActiveStateConvergencePort`.
- Implement CAS loop with `MetadataValue<String> currentMeta = cache.getWithMetadata(id)` and `cache.replaceWithVersion(id, payloadJson, currentMeta.getVersion())`.
- Enforce newer-version protection invariant: if cached authoritative version is greater than or equal to committed version, immediately return `ConvergenceStatus.CONVERGED` (signifying no further cache action required).
- Handle cold cache / empty cache via `putIfAbsent(id, payloadJson)` and treat unversioned legacy cache entries as version 0.
- Enforce failure handling: upon CAS exhaustion or cluster timeout, return `ConvergenceStatus.DEGRADED` without unconditionally evicting the cache entry.
- Implement comprehensive unit and scenario tests for `HotRodMnemeConvergence`.

**Step 3: Implement DefaultGovernedWriter Orchestrator in mnemosyne-clinical**  
GovernedWriter orchestrator composes Themis authorization, Mneme coordination, Mnemosyne persistence, and Mneme convergence into a unified CREATE and UPDATE workflow within the mnemosyne-clinical runtime module.

- Implement `DefaultGovernedWriter` in `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`) implementing `GovernedWriter`.
- Implement `update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext)`:
    - Validate parameters (fail-closed on null).
    - Evaluate Themis authorization via `ThemisAuthorizer.authorize(...)` with `ThemisAction.UPDATE`; return `WriteResult.NotCommitted` on deny.
    - Consume active state token via `ActiveStateCoordinator.consume(key, activeToken)`; return `WriteResult.ActiveConflict` on `STALE` or `WriteResult.NotCommitted` on `UNAVAILABLE`.
    - Persist update via `AuthoritativePersistencePort.update(key, proposed, expectedVersion)`.
    - Map persistence conflict (`EXPECTED_VERSION_MISMATCH`) to `WriteResult.AuthoritativeConflict` with consumed token preserved.
    - Map persistence unknown to `WriteResult.OutcomeUnknown` without automatic retry or token rollback.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` with `CONVERGED` or `DEGRADED`.
- Implement `create(ResourceKey key, T resource, ThemisSecurityContext securityContext)`:
    - Validate parameters.
    - Evaluate Themis authorization with `ThemisAction.CREATE`; return `WriteResult.NotCommitted` on deny.
    - Persist new resource via `AuthoritativePersistencePort.create(key, resource)` (atomic database uniqueness check); return `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)` on conflict.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
- Implement comprehensive composition unit tests covering all 15 outcome permutations and concurrency scenarios.

**Step 4: Add ArchUnit Architecture Guardrails and Integration Verification**  
Paradeigma test suite enforces architectural layering, isolation, and absence of physical deletion across the composed governed-write stack.

- Update `GovernedWriteContractArchitectureTest` in `paradeigma/paradeigma-test` to verify that `GovernedWriter` contracts remain pure in `calliope`.
- Update `MnemosyneAuthoritativePersistenceArchitectureTest` to assert that `DefaultGovernedWriter` in `mnemosyne-clinical` remains free of direct Infinispan Hot Rod classes.
- Add ArchUnit rules verifying that `GovernedWriter`, `AuthoritativePersistencePort`, and `ActiveStateCoordinator` do not expose DELETE or REMOVE methods.
- Verify full test suite and architecture rules with `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`.



I am implementing the MnemeCachedResource helper and the HotRodMnemeConvergence CAS-loop convergence service in hestia-mneme-cluster.

**Requirements**

**Overview & Goals**  
Step 08.04D designs the production composition of the governed-write capabilities established in Steps 08.04A (Caller-facing Contracts), 08.04B (Mneme Active-State Coordination), and 08.04C (Mnemosyne Atomic Authoritative Persistence).

The core objective is to compose:
1. **Themis Security Authorization** (`ThemisAuthorizer`, `ThemisSecurityContext`)
2. **Mneme Active-State Coordination** (`ActiveStateCoordinator.consume`)
3. **Mnemosyne Authoritative Persistence** (`AuthoritativePersistencePort.create` / `update`)
4. **Guarded Mneme Post-Commit Convergence** (`ActiveStateConvergencePort.converge`)

into a single unified, deterministic `DefaultGovernedWriter` implementation supporting `CREATE` and `UPDATE` operations while upholding the fundamental architectural baselines (ADR-018, ADR-019, ADR-020).

**Scope**
- **In Scope**:
    - Architectural placement of `DefaultGovernedWriter` composition in existing runtime module `hestia/mnemosyne-clinical` (preserving `calliope` as a pure contract/model library and preserving `AuthoritativePersistencePort` in `mnemosyne-clinical`).
    - Explicit execution sequence for governed `UPDATE` and governed `CREATE`.
    - Formal architectural decision on `CREATE` active-state coordination vs direct authoritative creation with post-commit convergence.
    - Themis authorization enforcement point (strictly evaluated *before* active token consumption and persistence).
    - Complete failure and outcome mapping matrix covering all combinations of Themis, Mneme, and Mnemosyne results.
    - Post-commit Mneme convergence CAS-loop algorithm with explicit authoritative-version provenance metadata and strict newer-version protection (preventing older commits from overwriting or invalidating newer cached versions).
    - Precise semantics for `ConvergenceStatus.CONVERGED` ("no further cache action required") and `ConvergenceStatus.DEGRADED`.
    - Truthful preservation of `UNKNOWN` commit outcomes without automatic retries or token rollbacks.
    - Comprehensive unit and scenario test plans covering all concurrency, authorization, coordination, persistence, and convergence states.
    - ArchUnit architectural rules enforcing module isolation, zero cyclic dependencies, and zero physical DELETE semantics.
    - Catalogue of existing bypasses and explicit boundary definition with Task 09 (Cache-aside point reads).

- **Out of Scope**:
    - Full caller migration across BEFE, Pylai, and Energeia (deferred to subsequent integration steps).
    - Implementation of physical DELETE operations (strictly prohibited by ADR-020).
    - Implementation of Task 09 cache-aside point reads and read repairs.
    - Implementation of automated reconciliation machinery for `UNKNOWN` outcomes (deferred to future reconciliation task).
    - Creation of redundant Maven modules (`hestia-governance`, `clinical-write-orchestrator-service`).

**User Stories**
- **As a Clinical Gateway or BEFE Resource Provider**, I want to execute governed updates through a unified `GovernedWriter` that validates Themis authorization, consumes the active state token in Mneme, and conditionally commits to Mnemosyne, so that lost updates and unauthorized modifications are prevented.
- **As a System Administrator**, I want authoritative writes that succeed in Mnemosyne to remain committed even if transient cache convergence fails (returning `Committed` with `DEGRADED` convergence), so that durable persistence is never rolled back due to ephemeral cache grid issues.
- **As a Security Auditor**, I want all write operations to evaluate Themis policies before consuming coordination tokens or touching durable databases, so that unauthorized requests are rejected cleanly at the gate.

**Functional Requirements**
1. **Governed UPDATE Execution**:
    - Accepts `GovernedRead<T> current`, proposed state `T proposed`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately without touching Mneme or Mnemosyne.
    - Consumes observed `ActiveStateToken` via `ActiveStateCoordinator.consume`:
        - If `STALE`: aborts immediately and returns `WriteResult.ActiveConflict`.
        - If `UNAVAILABLE`: aborts immediately and returns `WriteResult.NotCommitted` (fail-fast, zero local fallback).
    - Executes authoritative persistence via `AuthoritativePersistencePort.update`:
        - If `Conflict(EXPECTED_VERSION_MISMATCH)`: returns `WriteResult.AuthoritativeConflict` (token remains consumed).
        - If `NotCommitted`: returns `WriteResult.NotCommitted` (token remains consumed).
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown` (token remains consumed, no retry).
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
2. **Governed CREATE Execution**:
    - Accepts `ResourceKey key`, initial state `T resource`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately.
    - Executes authoritative persistence via `AuthoritativePersistencePort.create` (Mnemosyne atomically enforces absence precondition via unique constraints):
        - If `Conflict(RESOURCE_ALREADY_EXISTS)`: returns `WriteResult.AuthoritativeConflict`.
        - If `NotCommitted`: returns `WriteResult.NotCommitted`.
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown`.
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
3. **No Physical DELETE**:
    - `GovernedWriter` exposes only `create` and `update`. All lifecycle transitions (e.g. deprecation, deactivation, suspension) are authoritative `update` operations with FHIR status codes.

**Non-Functional Requirements**
- **Independence & Isolation**: Mnemosyne persistence must not depend on Mneme cache. Mneme cache loss must not affect durable database correctness.
- **Zero Local Fallback**: Distributed cache outages must produce visible failures (`UNAVAILABLE` / `DEGRADED`), never silent fallbacks to JVM-local synchronization maps.
- **Distinct Version Domains**: The four version domains (`ActiveStateToken`, `AuthoritativeVersion`, `FHIR meta.versionId`, `HTTP ETag`) must remain strictly isolated. No cross-domain version assumptions or synthetic versions.
- **Deterministic Outcome Mapping**: Every combination of underlying outcomes must map unambiguously into the sealed `WriteResult<T>` hierarchy.

**Technical Design**

**Current Implementation & Baseline Artifacts**
- **Caller-Facing Contracts (`calliope`)**:
    - `GovernedWriter`: Interface defining `<T> WriteResult<T> create(...)` and `<T> WriteResult<T> update(...)`.
    - `GovernedRead<T>`: Record packaging `ResourceKey`, payload `T`, `ActiveStateToken`, and `AuthoritativeVersion`.
    - `WriteResult<T>`: Sealed interface with records `Committed<T>`, `ActiveConflict<T>`, `AuthoritativeConflict<T>`, `OutcomeUnknown<T>`, and `NotCommitted<T>`.
    - `ActiveStateCoordinator`: Interface defining `observe(ResourceKey)` and `consume(ResourceKey, ActiveStateToken)`.
    - `ActiveStateConvergencePort`: Domain port interface defining `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
    - `ConvergenceStatus`: Sealed hierarchy / enum (`CONVERGED`, `DEGRADED`).
- **Mneme Active-State Coordination (`hestia/mneme-cluster`)**:
    - `HotRodActiveStateCoordinator`: Implements `ActiveStateCoordinator` using Hot Rod `replaceWithVersion` against `active-coordination-cache`.
- **Mnemosyne Authoritative Persistence (`hestia/mnemosyne-clinical`)**:
    - `AuthoritativePersistencePort<T>`: Port interface defining `create` and `update` (remains in `mnemosyne-clinical`).
    - `AuthoritativePersistenceService`: Implements `AuthoritativePersistencePort<IBaseResource>` using `TransactionTemplate` and conditional SQL updates.
    - `AuthoritativePersistenceResult<T>`: Sealed interface with `Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown` (remains in `mnemosyne-clinical`).
- **Themis Policy Evaluation (`themis/themis-api` & `themis/themis-core`)**:
    - `ThemisAuthorizer`: Evaluates `ThemisAuthorizationRequest` and returns `ThemisAuthorizationDecision`.
    - `ThemisSecurityContext`: Immutable context carrying originating/executing principals, authorities, correlation, and causation IDs.

---

**Key Architectural Decisions**

**Decision 1: Module Placement of GovernedWriter Composition**
- **Chosen Approach**: Place the production `DefaultGovernedWriter` implementation in `hestia/mnemosyne-clinical` (under `net.fhirfactory.harmonia.hapifhir.governed`), preserving `AuthoritativePersistencePort` and `AuthoritativePersistenceResult` in `mnemosyne-clinical`. `calliope` remains strictly a library for canonical models and domain contracts (`GovernedWriter`, `GovernedRead`, `WriteResult`, `ActiveStateCoordinator`, `ActiveStateConvergencePort`, `AuthoritativeVersion`, `ConvergenceStatus`). Hot Rod convergence logic (`HotRodMnemeConvergence`) is placed in `hestia/mneme-cluster`.
- **Rationale**:
    - `calliope` owns canonical schemas and semantic governance. It must never become an application runtime service orchestrator.
    - `hestia/mnemosyne-clinical` is the smallest existing runtime module that already depends on `calliope`, `themis-api`, and `themis-core`, and houses the clinical persistence implementations.
    - `DefaultGovernedWriter` interacts solely with domain interfaces (`ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, `ActiveStateConvergencePort`), without depending on Infinispan/Hot Rod concrete classes.
    - This avoids creating redundant Maven modules (e.g. `hestia-governance`), avoids dependency cycles, avoids leaking JPA/Infinispan into Calliope, and keeps contracts pure.

**Decision 2: Themis Authorization Precedence**
- **Chosen Approach**: Evaluate Themis authorization as the very first step in both `create` and `update`, strictly *before* consuming the `ActiveStateToken` in Mneme and *before* invoking Mnemosyne.
- **Rationale**: An unauthorized caller must not mutate active coordination state, burn ActiveStateTokens, or trigger database queries.

**Decision 3: CREATE Precondition Model & Active-State Coordination Evaluation**
- **Evaluation of Alternatives**:
    - **Option A (Chosen): Direct Authoritative CREATE with Post-Commit Convergence**
        - `Themis -> Mnemosyne CREATE -> Mneme Post-Commit Convergence`
    - **Option B (Rejected): Pre-Persistence Mneme Cold-State Coordination**
        - `Themis -> Mneme cold-state coordination -> Mnemosyne CREATE -> Mneme convergence`
- **Architectural Rationale & Invariant**:
    - An `ActiveStateToken` represents an observed point-in-time active state of an *existing* resource. For a newly created resource, no prior active state exists to observe.
    - Mnemosyne exclusively defines the durable authoritative state boundary (ADR-018) and owns the authoritative *absence* precondition (enforced via database unique constraints on `[resource_type, fhir_id]`).
    - Option B would require reserving a speculative token or placeholder marker in Mneme for an absent resource prior to database insertion. If Mnemosyne persistence subsequently fails, or if Mneme partitions/restarts, this creates phantom reservations, requires token rollback machinery (violating the fundamental invariant that consumed tokens are never rolled back), and creates distributed dual-master race conditions between cache and database.
    - **Invariant**: *Mnemosyne exclusively owns the authoritative absence precondition. CREATE operations deliberately do not perform pre-persistence active-state coordination in Mneme, and instead seed Mneme active state via post-commit convergence upon successful authoritative creation ($V=1$).*
    - No synthetic authoritative version, lease, or JVM lock is invented.

**Decision 4: Guarded Mneme Post-Commit Convergence with Distinct Version Domains**
- **Version Domain Separation**:
    1. `ActiveStateToken`: Opaque Hot Rod entry version CAS token for active-state coordination.
    2. `AuthoritativeVersion`: Mnemosyne durable database sequence version ($1, 2, 3\dots$).
    3. FHIR `Resource.meta.versionId`: FHIR specification payload field (string).
    4. HTTP ETag / `If-Match`: HTTP transport concurrency header.
    - *Invariant*: Convergence must never assume `FHIR meta.versionId == AuthoritativeVersion`.
- **Authoritative Provenance in Mneme**:
    - Post-commit convergence compares the incoming `AuthoritativeVersion` against explicit metadata describing the authoritative version represented by the cached Mneme value.
    - **Cache Contract Analysis & Chosen Representation**:
        - *Investigation of Existing Caches*: All existing clinical caches (`person-cache`, `task-cache`, `organization-cache`, etc.) store raw FHIR JSON `String`. Existing readers (`FhirCacheService`, `TaskCacheService`, `DefaultTaskService`) parse cached values directly using `FhirContext.newJsonParser().parseResource(...)`.
        - *Incompatibility of Outer JSON Envelope*: An outer wrapper (`{ authoritativeVersion: ..., resource: ... }`) is invalid FHIR JSON and would cause `DataFormatException` in all existing readers, violating the constraint not to migrate all cache consumers in 08.04D.
        - *Chosen Smallest Compatible Representation (`MnemeCachedResource`)*: Authoritative version provenance is embedded directly inside the standard FHIR resource under `Resource.meta.extension` using canonical URI `http://harmonia.fhirfactory.net/structure/authoritative-version` (with `Integer64Type` or numeric representation).
        - *Backwards Compatibility & Atomicity*: The cached value remains 100% valid raw FHIR JSON. HAPI FHIR parsers across all existing consumers parse it seamlessly without schema errors. The resource payload and its authoritative provenance are read and written atomically in a single Infinispan cache entry, eliminating dual-cache consistency hazards.
        - *Legacy Cache Entries*: Any pre-existing cache entry lacking the authoritative-version extension is treated as $V_{cached} = 0$, allowing governed writes to safely converge over legacy entries.
    - Infinispan `MetadataValue.getVersion()` remains strictly an opaque CAS token for `replaceWithVersion`.
- **Convergence Semantics**:
    - `ConvergenceStatus.CONVERGED` explicitly means **"no further cache action is required"** (the cache already reflects this authoritative version or a newer authoritative progression), NOT "the supplied committed representation was written to cache".
    - If the cached representation is newer or equal ($V_{cached} \ge V_{committed}$), convergence immediately succeeds with `CONVERGED` without overwriting the newer state.
    - If the cached representation is older ($V_{cached} < V_{committed}$), conditional replacement is attempted using the opaque Hot Rod version token (`replaceWithVersion`).
    - If the cache entry is absent (cold cache), `putIfAbsent` is executed.
    - If CAS fails or retries are exhausted under contention/outage, `ConvergenceStatus.DEGRADED` is returned without unconditionally evicting the cache entry.

---

**Composed Architecture & Sequence Flows**

```mermaid
graph TD
  Caller[Caller: BEFE / Pylai / Erga] -->|GovernedRead + Proposed State| GW[DefaultGovernedWriter in mnemosyne-clinical]
  GW -->|1. Authorize Action| Themis[ThemisAuthorizer]
  Themis -->|Allow / Deny| GW
  GW -->|2. Consume Token (UPDATE only)| MnemeCoord[Mneme: ActiveStateCoordinator]
  MnemeCoord -->|CONSUMED / STALE / UNAVAILABLE| GW
  GW -->|3. Atomic Persistence| Mnemosyne[Mnemosyne: AuthoritativePersistencePort]
  Mnemosyne -->|COMMITTED / CONFLICT / UNKNOWN| GW
  GW -->|4. Guarded CAS Convergence| MnemeConv[Mneme: ActiveStateConvergencePort]
  MnemeConv -->|CONVERGED / DEGRADED| GW
  GW -->|WriteResult<T>| Caller
```

**UPDATE Execution Flow**
1. **Validate Input**: Check non-null `GovernedRead`, `proposed`, `securityContext`, and matching resource types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.UPDATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Active-State Progression (Mneme)**:
    - Call `coordinator.consume(current.key(), current.activeToken())`.
    - If `STALE`: return `WriteResult.activeStateConflict(ActiveStateConflict.of(key, "Active state token is stale"))`.
    - If `UNAVAILABLE`: return `WriteResult.notCommitted(key, "Active state coordination unavailable")`.
4. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.update(current.key(), proposed, current.expectedAuthoritativeVersion())`.
    - If `Conflict(EXPECTED_VERSION_MISMATCH)`: return `WriteResult.authoritativeConflict(conflict.conflict())`. (Token remains consumed).
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`. (Token remains consumed).
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`. (Token remains consumed, no retry).
5. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, newVersion)`: call `convergencePort.converge(key, resource, newVersion)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, newVersion)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, newVersion, "Mneme cache convergence degraded")`.

**CREATE Execution Flow**
1. **Validate Input**: Check non-null `ResourceKey`, `resource`, `securityContext`, and matching types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.CREATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.create(key, resource)`.
    - If `Conflict(RESOURCE_ALREADY_EXISTS)`: return `WriteResult.authoritativeConflict(conflict.conflict())`.
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`.
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`.
4. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, v1)`: call `convergencePort.converge(key, resource, v1)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, v1)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, v1, "Mneme cache convergence degraded")`.

---

**Failure and Outcome Mapping Matrix**

| Scenario / Condition | Themis Decision | Mneme Coordination | Mnemosyne Persistence | Mneme Convergence | WriteResult Variant | Token Consumed? | Database State |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **A. Themis Denies** | `DENY` | Not Called | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **B. Mneme Stale** | `ALLOW` | `STALE` | Not Called | Not Called | `WriteResult.ActiveConflict` | No | Unchanged |
| **C. Mneme Unavailable** | `ALLOW` | `UNAVAILABLE` | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **D. Authoritative Conflict** | `ALLOW` | `CONSUMED` | `Conflict(VERSION_MISMATCH)` | Not Called | `WriteResult.AuthoritativeConflict` | Yes | Unchanged |
| **E. Persistence Not Committed** | `ALLOW` | `CONSUMED` | `NotCommitted` | Not Called | `WriteResult.NotCommitted` | Yes | Unchanged |
| **F. Persistence Unknown** | `ALLOW` | `CONSUMED` | `OutcomeUnknown` | Not Called | `WriteResult.OutcomeUnknown` | Yes | Ambiguous (No Retry) |
| **G. Committed + Converged** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `CONVERGED` | `WriteResult.Committed(CONVERGED)` | Yes | Committed ($V$) |
| **H. Committed + Degraded** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `DEGRADED` | `WriteResult.Committed(DEGRADED)` | Yes | Committed ($V$) |
| **I. CREATE Duplicate** | `ALLOW` | N/A | `Conflict(ALREADY_EXISTS)` | Not Called | `WriteResult.AuthoritativeConflict` | N/A | Unchanged |

---

**Post-Commit Mneme Convergence Algorithm**

```java
public class HotRodMnemeConvergence implements ActiveStateConvergencePort {
    public static final String AUTHORITATIVE_VERSION_EXT_URL = "http://harmonia.fhirfactory.net/structure/authoritative-version";

    private final RemoteCacheManager cacheManager;
    private final FhirContext fhirContext;

    @Override
    public <T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion) {
        String cacheName = resolveCacheName(key.resourceType());
        RemoteCache<String, String> cache = cacheManager.getCache(cacheName);
        if (cache == null) return ConvergenceStatus.DEGRADED;

        // Attach explicit authoritative-version provenance into FHIR meta.extension
        IBaseResource fhirResource = (IBaseResource) committedResource;
        MnemeCachedResource.attachAuthoritativeVersion(fhirResource, committedVersion);
        String payloadJson = fhirContext.newJsonParser().encodeResourceToString(fhirResource);
        long targetVer = committedVersion.value();

        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                MetadataValue<String> currentMeta = cache.getWithMetadata(key.id());
                if (currentMeta != null) {
                    long cachedVer = MnemeCachedResource.extractAuthoritativeVersion(currentMeta.getValue());
                    // Newer-Version Invariant: never overwrite newer cache state.
                    // CONVERGED means no further cache action is required.
                    if (cachedVer >= targetVer) {
                        return ConvergenceStatus.CONVERGED;
                    }
                    // Hot Rod opaque CAS token replacement
                    boolean casSuccess = cache.replaceWithVersion(key.id(), payloadJson, currentMeta.getVersion());
                    if (casSuccess) return ConvergenceStatus.CONVERGED;
                } else {
                    String existing = cache.putIfAbsent(key.id(), payloadJson);
                    if (existing == null) return ConvergenceStatus.CONVERGED;
                }
            } catch (Exception e) {
                return ConvergenceStatus.DEGRADED;
            }
        }
        // CAS exhausted: DO NOT evict entry (preserves newer versions)
        return ConvergenceStatus.DEGRADED;
    }
}
```

---

**Dependency Graph**

```
calliope (contracts: GovernedWriter, GovernedRead, WriteResult, ActiveStateCoordinator,
          ActiveStateConvergencePort, AuthoritativeVersion, ConvergenceStatus)
  └── depends on: themis-api (ThemisAuthorizer, ThemisSecurityContext)
  └── depends on: hapi-fhir-structures-r5

hestia/mneme-cluster (implementations: HotRodActiveStateCoordinator, HotRodMnemeConvergence)
  └── depends on: calliope, infinispan-client-hotrod

hestia/mnemosyne-clinical (persistence & composition: AuthoritativePersistencePort, AuthoritativePersistenceService, DefaultGovernedWriter)
  └── depends on: calliope, themis-api, themis-core, spring-boot-starter-data-jpa

iris/iris-befe (presentation gateway)
  └── depends on: calliope, themis-api, themis-core, hestia/mneme-cluster
```

**Graph Properties**:
- Purely unidirectional.
- Zero cyclic dependencies.
- No JPA, Hibernate, or Infinispan types leaked into `calliope` or `GovernedWriter` API.
- `DefaultGovernedWriter` lives in `mnemosyne-clinical` as a runtime orchestrator.

---

**Concrete File Structure Changes**

**Files to Add:**
1. `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ActiveStateConvergencePort.java` (Domain convergence port interface)
2. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/MnemeCachedResource.java` (Helper to attach/extract `AuthoritativeVersion` provenance via standard FHIR `Resource.meta.extension`)
3. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergence.java` (Hot Rod CAS convergence implementation)
4. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriter.java` (Production orchestrator in runtime module)
5. `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriterTest.java` (Comprehensive composition unit tests)
6. `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergenceTest.java` (Convergence CAS & newer-version tests)
7. `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteCompositionArchitectureTest.java` (ArchUnit rules)

**Files Preserved Without Relocation:**
1. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java` (Remains in `mnemosyne-clinical`)
2. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/model/AuthoritativePersistenceResult.java` (Remains in `mnemosyne-clinical`)
3. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java` (Remains in `mnemosyne-clinical`)

**Testing**

**Validation Approach**  
Verification of Step 08.04D composition will be performed using a multi-tier testing strategy:
1. **Composition Unit Tests (`DefaultGovernedWriterTest`)**:
    - Uses Mockito-based test doubles for `ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, and `ActiveStateConvergencePort`.
    - Validates all 15 permutations of the Failure and Outcome Mapping Matrix.
2. **Convergence CAS Unit & Scenario Tests (`HotRodMnemeConvergenceTest`)**:
    - Validates the newer-version protection invariant ($V_{cached} \ge V_{committed}$ is preserved).
    - Validates cold-cache population via `putIfAbsent`.
    - Validates CAS loss handling and non-eviction on retry exhaustion.
3. **ArchUnit Architecture Tests (`GovernedWriteCompositionArchitectureTest`)**:
    - Enforces package layering, zero dependency cycles, and zero JPA/Infinispan leaks into `calliope`.

---

**Key Scenarios**

**Scenario 1: Authorised UPDATE Happy Path**
- **Setup**: `GovernedRead` with V1 and token `T1`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne commits V2 $\to$ Convergence succeeds with V2.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.CONVERGED`, version `AuthoritativeVersion.of(2L)`.

**Scenario 2: Themis Denied**
- **Setup**: `GovernedRead` with V1. Themis mock configured to return `DENY`.
- **Flow**: Themis denies $\to$ write immediately terminates.
- **Outcome**: `WriteResult.NotCommitted`. Zero invocations on `ActiveStateCoordinator` and `AuthoritativePersistencePort`.

**Scenario 3: Stale ActiveStateToken**
- **Setup**: `GovernedRead` with stale token `T_stale`.
- **Flow**: Themis allows $\to$ Mneme returns `STALE` $\to$ write immediately terminates.
- **Outcome**: `WriteResult.ActiveConflict`. Zero invocations on `AuthoritativePersistencePort`.

**Scenario 4: Mneme Unavailable**
- **Setup**: `GovernedRead` with V1. `ActiveStateCoordinator` returns `UNAVAILABLE`.
- **Flow**: Themis allows $\to$ Mneme returns `UNAVAILABLE` $\to$ write terminates with fail-fast visible error.
- **Outcome**: `WriteResult.NotCommitted` (fail-fast, no JVM-local fallback).

**Scenario 5: Authoritative Precondition Conflict (Expected Version Mismatch)**
- **Setup**: `GovernedRead` with V1. Mnemosyne returns `Conflict(EXPECTED_VERSION_MISMATCH, current=V3)`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne returns Conflict.
- **Outcome**: `WriteResult.AuthoritativeConflict(EXPECTED_VERSION_MISMATCH)`. Token `T1` remains consumed.

**Scenario 6: Authoritative Persistence Unknown**
- **Setup**: Mnemosyne returns `OutcomeUnknown("Commit ACK timeout")`.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne returns `OutcomeUnknown`.
- **Outcome**: `WriteResult.OutcomeUnknown`. Token remains consumed. No automatic retry attempted.

**Scenario 7: Committed with Degraded Convergence**
- **Setup**: Mnemosyne commits V2, but Mneme convergence throws timeout exception.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne commits V2 $\to$ Convergence returns `DEGRADED`.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.DEGRADED`. Durable commit remains intact.

**Scenario 8: Delayed Older Convergence Preserves Newer Cache State**
- **Setup**: Cache currently contains V43. Delayed convergence thread attempts to converge committed V42.
- **Flow**: `HotRodMnemeConvergence.converge` reads cached V43 $\to$ detects $43 \ge 42 \to$ terminates immediately with `CONVERGED`.
- **Outcome**: Cache remains unchanged at V43. V42 does not overwrite or evict V43.

**Scenario 9: Authorised CREATE Happy Path & Duplicate Detection**
- **Setup**: Proposed new resource `Patient/100`.
- **Flow A**: Themis allows $\to$ Mnemosyne creates V1 $\to$ Convergence populates cache with V1 $\to$ returns `WriteResult.Committed(V1)`.
- **Flow B**: Concurrent CREATE with same key $\to$ Mnemosyne unique constraint triggers $\to$ returns `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)`.

---

**Architecture Guardrails to Add**
- `GovernedWriteCompositionArchitectureTest`:
    1. `GovernedWriter` resides in `calliope` and `DefaultGovernedWriter` resides in `hestia/mnemosyne-clinical`.
    2. `calliope` must have zero dependencies on `hestia`, `iris`, `pylai`, `energeia`, `agora`, or `paradeigma`.
    3. `DefaultGovernedWriter` must not import or depend on `org.infinispan..`, `jakarta.persistence..`, `org.hibernate..`, or `ca.uhn.fhir.jpa..`.
    4. `AuthoritativePersistencePort` and `ActiveStateCoordinator` must not expose DELETE, REMOVE, or PURGE methods (ADR-020).
    5. `HotRodMnemeConvergence` must not depend on `AuthoritativePersistencePort` or JPA entities.

**Bypasses & Deferred Work**

**Catalogue of Remaining Bypasses (Post-08.04D)**

While Step 08.04D establishes the complete production governed-write composition, existing runtime components still contain legacy write-path bypasses that will be migrated in subsequent steps:

| Subsystem / Component | File / Location | Legacy Behavior | Target Migration Step |
| :--- | :--- | :--- | :--- |
| **Iris BEFE** | `FhirCacheService.java` | Unconditional `remoteCache.put(id, json)` and `remoteCache.remove(id)` | Task 08.05 / 08.06 (BEFE Governed Writer Migration) |
| **Mneme Persistence** | `FhirRestCacheStore.java` | Asynchronous write-behind store bypassing governed coordination | Task 08.07 (Write-Behind Decommissioning) |
| **Mnemosyne Clinical** | HAPI Resource Providers | Direct `FhirStorageService.createResource/updateResource` | Task 08.05 / Task 09 |
| **Energeia Ponos** | `ErgonBase.java` / `PraxisService.java` | Direct cache updates for task synchronization | Task 08.06 (Workflow Task Governed Migration) |
| **Pylai MLLP** | `pylai-mllp-in` / `pylai-fhir-provider-registry` | Direct cache and storage mutations | Task 08.06 (Gateway Governed Migration) |

---

**Explicitly Deferred Work**
1. **Task 09 (Cache-Aside Point Reads)**: Governed point reads (`GovernedRead<T>`), cache-aside read-through, and read repairs will be designed and implemented in Task 09.
2. **UNKNOWN Outcome Reconciliation Framework**: Automated asynchronous reconciliation of `WriteResult.OutcomeUnknown` is deferred to a dedicated post-Task 08 reconciliation capability.
3. **Legacy Caller Migration**: Modifying BEFE REST endpoints, Pylai gateways, and Ponos routes to call `GovernedWriter` will occur in follow-up integration steps.

**Delivery Steps**

**✓ Step 1: Define Governed-Write Convergence Contract in Calliope**  
Define pure convergence port contract in Calliope while preserving persistence ports in Mnemosyne Clinical.

- Define `ActiveStateConvergencePort` in `calliope` (`net.fhirfactory.harmonia.model.governedwrite`) with method `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
- Verify `AuthoritativePersistencePort<T>` and `AuthoritativePersistenceResult<T>` remain in `hestia/mnemosyne-clinical` without leaking into Calliope.
- Add unit tests in `calliope` verifying contract invariants, immutability, and null safety.

**✓ Step 2: Implement Guarded Mneme Hot Rod Convergence in hestia-mneme-cluster**  
Mneme cluster provides production Hot Rod CAS-loop convergence with explicit authoritative version provenance metadata, ensuring older commits cannot overwrite or invalidate newer cache representations.

- Implement `MnemeCachedResource` helper in `hestia/mneme-cluster` to attach and extract explicit authoritative version provenance via standard FHIR `Resource.meta.extension` (`http://harmonia.fhirfactory.net/structure/authoritative-version`), maintaining 100% backwards compatibility with raw FHIR JSON cache contracts and zero dual-cache consistency hazards.
- Implement `HotRodMnemeConvergence` in `hestia/mneme-cluster` implementing `ActiveStateConvergencePort`.
- Implement CAS loop with `MetadataValue<String> currentMeta = cache.getWithMetadata(id)` and `cache.replaceWithVersion(id, payloadJson, currentMeta.getVersion())`.
- Enforce newer-version protection invariant: if cached authoritative version is greater than or equal to committed version, immediately return `ConvergenceStatus.CONVERGED` (signifying no further cache action required).
- Handle cold cache / empty cache via `putIfAbsent(id, payloadJson)` and treat unversioned legacy cache entries as version 0.
- Enforce failure handling: upon CAS exhaustion or cluster timeout, return `ConvergenceStatus.DEGRADED` without unconditionally evicting the cache entry.
- Implement comprehensive unit and scenario tests for `HotRodMnemeConvergence`.

*** Step 3: Implement DefaultGovernedWriter Orchestrator in mnemosyne-clinical**  
GovernedWriter orchestrator composes Themis authorization, Mneme coordination, Mnemosyne persistence, and Mneme convergence into a unified CREATE and UPDATE workflow within the mnemosyne-clinical runtime module.

- Implement `DefaultGovernedWriter` in `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`) implementing `GovernedWriter`.
- Implement `update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext)`:
    - Validate parameters (fail-closed on null).
    - Evaluate Themis authorization via `ThemisAuthorizer.authorize(...)` with `ThemisAction.UPDATE`; return `WriteResult.NotCommitted` on deny.
    - Consume active state token via `ActiveStateCoordinator.consume(key, activeToken)`; return `WriteResult.ActiveConflict` on `STALE` or `WriteResult.NotCommitted` on `UNAVAILABLE`.
    - Persist update via `AuthoritativePersistencePort.update(key, proposed, expectedVersion)`.
    - Map persistence conflict (`EXPECTED_VERSION_MISMATCH`) to `WriteResult.AuthoritativeConflict` with consumed token preserved.
    - Map persistence unknown to `WriteResult.OutcomeUnknown` without automatic retry or token rollback.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` with `CONVERGED` or `DEGRADED`.
- Implement `create(ResourceKey key, T resource, ThemisSecurityContext securityContext)`:
    - Validate parameters.
    - Evaluate Themis authorization with `ThemisAction.CREATE`; return `WriteResult.NotCommitted` on deny.
    - Persist new resource via `AuthoritativePersistencePort.create(key, resource)` (atomic database uniqueness check); return `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)` on conflict.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
- Implement comprehensive composition unit tests covering all 15 outcome permutations and concurrency scenarios.

**Step 4: Add ArchUnit Architecture Guardrails and Integration Verification**  
Paradeigma test suite enforces architectural layering, isolation, and absence of physical deletion across the composed governed-write stack.

- Update `GovernedWriteContractArchitectureTest` in `paradeigma/paradeigma-test` to verify that `GovernedWriter` contracts remain pure in `calliope`.
- Update `MnemosyneAuthoritativePersistenceArchitectureTest` to assert that `DefaultGovernedWriter` in `mnemosyne-clinical` remains free of direct Infinispan Hot Rod classes.
- Add ArchUnit rules verifying that `GovernedWriter`, `AuthoritativePersistencePort`, and `ActiveStateCoordinator` do not expose DELETE or REMOVE methods.
- Verify full test suite and architecture rules with `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`.



I am creating the DefaultGovernedWriter orchestrator in mnemosyne-clinical to compose Themis authorization, Mneme coordination, Mnemosyne persistence, and post-commit convergence.

**Requirements**

**Overview & Goals**  
Step 08.04D designs the production composition of the governed-write capabilities established in Steps 08.04A (Caller-facing Contracts), 08.04B (Mneme Active-State Coordination), and 08.04C (Mnemosyne Atomic Authoritative Persistence).

The core objective is to compose:
1. **Themis Security Authorization** (`ThemisAuthorizer`, `ThemisSecurityContext`)
2. **Mneme Active-State Coordination** (`ActiveStateCoordinator.consume`)
3. **Mnemosyne Authoritative Persistence** (`AuthoritativePersistencePort.create` / `update`)
4. **Guarded Mneme Post-Commit Convergence** (`ActiveStateConvergencePort.converge`)

into a single unified, deterministic `DefaultGovernedWriter` implementation supporting `CREATE` and `UPDATE` operations while upholding the fundamental architectural baselines (ADR-018, ADR-019, ADR-020).

**Scope**
- **In Scope**:
    - Architectural placement of `DefaultGovernedWriter` composition in existing runtime module `hestia/mnemosyne-clinical` (preserving `calliope` as a pure contract/model library and preserving `AuthoritativePersistencePort` in `mnemosyne-clinical`).
    - Explicit execution sequence for governed `UPDATE` and governed `CREATE`.
    - Formal architectural decision on `CREATE` active-state coordination vs direct authoritative creation with post-commit convergence.
    - Themis authorization enforcement point (strictly evaluated *before* active token consumption and persistence).
    - Complete failure and outcome mapping matrix covering all combinations of Themis, Mneme, and Mnemosyne results.
    - Post-commit Mneme convergence CAS-loop algorithm with explicit authoritative-version provenance metadata and strict newer-version protection (preventing older commits from overwriting or invalidating newer cached versions).
    - Precise semantics for `ConvergenceStatus.CONVERGED` ("no further cache action required") and `ConvergenceStatus.DEGRADED`.
    - Truthful preservation of `UNKNOWN` commit outcomes without automatic retries or token rollbacks.
    - Comprehensive unit and scenario test plans covering all concurrency, authorization, coordination, persistence, and convergence states.
    - ArchUnit architectural rules enforcing module isolation, zero cyclic dependencies, and zero physical DELETE semantics.
    - Catalogue of existing bypasses and explicit boundary definition with Task 09 (Cache-aside point reads).

- **Out of Scope**:
    - Full caller migration across BEFE, Pylai, and Energeia (deferred to subsequent integration steps).
    - Implementation of physical DELETE operations (strictly prohibited by ADR-020).
    - Implementation of Task 09 cache-aside point reads and read repairs.
    - Implementation of automated reconciliation machinery for `UNKNOWN` outcomes (deferred to future reconciliation task).
    - Creation of redundant Maven modules (`hestia-governance`, `clinical-write-orchestrator-service`).

**User Stories**
- **As a Clinical Gateway or BEFE Resource Provider**, I want to execute governed updates through a unified `GovernedWriter` that validates Themis authorization, consumes the active state token in Mneme, and conditionally commits to Mnemosyne, so that lost updates and unauthorized modifications are prevented.
- **As a System Administrator**, I want authoritative writes that succeed in Mnemosyne to remain committed even if transient cache convergence fails (returning `Committed` with `DEGRADED` convergence), so that durable persistence is never rolled back due to ephemeral cache grid issues.
- **As a Security Auditor**, I want all write operations to evaluate Themis policies before consuming coordination tokens or touching durable databases, so that unauthorized requests are rejected cleanly at the gate.

**Functional Requirements**
1. **Governed UPDATE Execution**:
    - Accepts `GovernedRead<T> current`, proposed state `T proposed`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately without touching Mneme or Mnemosyne.
    - Consumes observed `ActiveStateToken` via `ActiveStateCoordinator.consume`:
        - If `STALE`: aborts immediately and returns `WriteResult.ActiveConflict`.
        - If `UNAVAILABLE`: aborts immediately and returns `WriteResult.NotCommitted` (fail-fast, zero local fallback).
    - Executes authoritative persistence via `AuthoritativePersistencePort.update`:
        - If `Conflict(EXPECTED_VERSION_MISMATCH)`: returns `WriteResult.AuthoritativeConflict` (token remains consumed).
        - If `NotCommitted`: returns `WriteResult.NotCommitted` (token remains consumed).
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown` (token remains consumed, no retry).
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
2. **Governed CREATE Execution**:
    - Accepts `ResourceKey key`, initial state `T resource`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately.
    - Executes authoritative persistence via `AuthoritativePersistencePort.create` (Mnemosyne atomically enforces absence precondition via unique constraints):
        - If `Conflict(RESOURCE_ALREADY_EXISTS)`: returns `WriteResult.AuthoritativeConflict`.
        - If `NotCommitted`: returns `WriteResult.NotCommitted`.
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown`.
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
3. **No Physical DELETE**:
    - `GovernedWriter` exposes only `create` and `update`. All lifecycle transitions (e.g. deprecation, deactivation, suspension) are authoritative `update` operations with FHIR status codes.

**Non-Functional Requirements**
- **Independence & Isolation**: Mnemosyne persistence must not depend on Mneme cache. Mneme cache loss must not affect durable database correctness.
- **Zero Local Fallback**: Distributed cache outages must produce visible failures (`UNAVAILABLE` / `DEGRADED`), never silent fallbacks to JVM-local synchronization maps.
- **Distinct Version Domains**: The four version domains (`ActiveStateToken`, `AuthoritativeVersion`, `FHIR meta.versionId`, `HTTP ETag`) must remain strictly isolated. No cross-domain version assumptions or synthetic versions.
- **Deterministic Outcome Mapping**: Every combination of underlying outcomes must map unambiguously into the sealed `WriteResult<T>` hierarchy.

**Technical Design**

**Current Implementation & Baseline Artifacts**
- **Caller-Facing Contracts (`calliope`)**:
    - `GovernedWriter`: Interface defining `<T> WriteResult<T> create(...)` and `<T> WriteResult<T> update(...)`.
    - `GovernedRead<T>`: Record packaging `ResourceKey`, payload `T`, `ActiveStateToken`, and `AuthoritativeVersion`.
    - `WriteResult<T>`: Sealed interface with records `Committed<T>`, `ActiveConflict<T>`, `AuthoritativeConflict<T>`, `OutcomeUnknown<T>`, and `NotCommitted<T>`.
    - `ActiveStateCoordinator`: Interface defining `observe(ResourceKey)` and `consume(ResourceKey, ActiveStateToken)`.
    - `ActiveStateConvergencePort`: Domain port interface defining `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
    - `ConvergenceStatus`: Sealed hierarchy / enum (`CONVERGED`, `DEGRADED`).
- **Mneme Active-State Coordination (`hestia/mneme-cluster`)**:
    - `HotRodActiveStateCoordinator`: Implements `ActiveStateCoordinator` using Hot Rod `replaceWithVersion` against `active-coordination-cache`.
- **Mnemosyne Authoritative Persistence (`hestia/mnemosyne-clinical`)**:
    - `AuthoritativePersistencePort<T>`: Port interface defining `create` and `update` (remains in `mnemosyne-clinical`).
    - `AuthoritativePersistenceService`: Implements `AuthoritativePersistencePort<IBaseResource>` using `TransactionTemplate` and conditional SQL updates.
    - `AuthoritativePersistenceResult<T>`: Sealed interface with `Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown` (remains in `mnemosyne-clinical`).
- **Themis Policy Evaluation (`themis/themis-api` & `themis/themis-core`)**:
    - `ThemisAuthorizer`: Evaluates `ThemisAuthorizationRequest` and returns `ThemisAuthorizationDecision`.
    - `ThemisSecurityContext`: Immutable context carrying originating/executing principals, authorities, correlation, and causation IDs.

---

**Key Architectural Decisions**

**Decision 1: Module Placement of GovernedWriter Composition**
- **Chosen Approach**: Place the production `DefaultGovernedWriter` implementation in `hestia/mnemosyne-clinical` (under `net.fhirfactory.harmonia.hapifhir.governed`), preserving `AuthoritativePersistencePort` and `AuthoritativePersistenceResult` in `mnemosyne-clinical`. `calliope` remains strictly a library for canonical models and domain contracts (`GovernedWriter`, `GovernedRead`, `WriteResult`, `ActiveStateCoordinator`, `ActiveStateConvergencePort`, `AuthoritativeVersion`, `ConvergenceStatus`). Hot Rod convergence logic (`HotRodMnemeConvergence`) is placed in `hestia/mneme-cluster`.
- **Rationale**:
    - `calliope` owns canonical schemas and semantic governance. It must never become an application runtime service orchestrator.
    - `hestia/mnemosyne-clinical` is the smallest existing runtime module that already depends on `calliope`, `themis-api`, and `themis-core`, and houses the clinical persistence implementations.
    - `DefaultGovernedWriter` interacts solely with domain interfaces (`ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, `ActiveStateConvergencePort`), without depending on Infinispan/Hot Rod concrete classes.
    - This avoids creating redundant Maven modules (e.g. `hestia-governance`), avoids dependency cycles, avoids leaking JPA/Infinispan into Calliope, and keeps contracts pure.

**Decision 2: Themis Authorization Precedence**
- **Chosen Approach**: Evaluate Themis authorization as the very first step in both `create` and `update`, strictly *before* consuming the `ActiveStateToken` in Mneme and *before* invoking Mnemosyne.
- **Rationale**: An unauthorized caller must not mutate active coordination state, burn ActiveStateTokens, or trigger database queries.

**Decision 3: CREATE Precondition Model & Active-State Coordination Evaluation**
- **Evaluation of Alternatives**:
    - **Option A (Chosen): Direct Authoritative CREATE with Post-Commit Convergence**
        - `Themis -> Mnemosyne CREATE -> Mneme Post-Commit Convergence`
    - **Option B (Rejected): Pre-Persistence Mneme Cold-State Coordination**
        - `Themis -> Mneme cold-state coordination -> Mnemosyne CREATE -> Mneme convergence`
- **Architectural Rationale & Invariant**:
    - An `ActiveStateToken` represents an observed point-in-time active state of an *existing* resource. For a newly created resource, no prior active state exists to observe.
    - Mnemosyne exclusively defines the durable authoritative state boundary (ADR-018) and owns the authoritative *absence* precondition (enforced via database unique constraints on `[resource_type, fhir_id]`).
    - Option B would require reserving a speculative token or placeholder marker in Mneme for an absent resource prior to database insertion. If Mnemosyne persistence subsequently fails, or if Mneme partitions/restarts, this creates phantom reservations, requires token rollback machinery (violating the fundamental invariant that consumed tokens are never rolled back), and creates distributed dual-master race conditions between cache and database.
    - **Invariant**: *Mnemosyne exclusively owns the authoritative absence precondition. CREATE operations deliberately do not perform pre-persistence active-state coordination in Mneme, and instead seed Mneme active state via post-commit convergence upon successful authoritative creation ($V=1$).*
    - No synthetic authoritative version, lease, or JVM lock is invented.

**Decision 4: Guarded Mneme Post-Commit Convergence with Distinct Version Domains**
- **Version Domain Separation**:
    1. `ActiveStateToken`: Opaque Hot Rod entry version CAS token for active-state coordination.
    2. `AuthoritativeVersion`: Mnemosyne durable database sequence version ($1, 2, 3\dots$).
    3. FHIR `Resource.meta.versionId`: FHIR specification payload field (string).
    4. HTTP ETag / `If-Match`: HTTP transport concurrency header.
    - *Invariant*: Convergence must never assume `FHIR meta.versionId == AuthoritativeVersion`.
- **Authoritative Provenance in Mneme**:
    - Post-commit convergence compares the incoming `AuthoritativeVersion` against explicit metadata describing the authoritative version represented by the cached Mneme value.
    - **Cache Contract Analysis & Chosen Representation**:
        - *Investigation of Existing Caches*: All existing clinical caches (`person-cache`, `task-cache`, `organization-cache`, etc.) store raw FHIR JSON `String`. Existing readers (`FhirCacheService`, `TaskCacheService`, `DefaultTaskService`) parse cached values directly using `FhirContext.newJsonParser().parseResource(...)`.
        - *Incompatibility of Outer JSON Envelope*: An outer wrapper (`{ authoritativeVersion: ..., resource: ... }`) is invalid FHIR JSON and would cause `DataFormatException` in all existing readers, violating the constraint not to migrate all cache consumers in 08.04D.
        - *Chosen Smallest Compatible Representation (`MnemeCachedResource`)*: Authoritative version provenance is embedded directly inside the standard FHIR resource under `Resource.meta.extension` using canonical URI `http://harmonia.fhirfactory.net/structure/authoritative-version` (with `Integer64Type` or numeric representation).
        - *Backwards Compatibility & Atomicity*: The cached value remains 100% valid raw FHIR JSON. HAPI FHIR parsers across all existing consumers parse it seamlessly without schema errors. The resource payload and its authoritative provenance are read and written atomically in a single Infinispan cache entry, eliminating dual-cache consistency hazards.
        - *Legacy Cache Entries*: Any pre-existing cache entry lacking the authoritative-version extension is treated as $V_{cached} = 0$, allowing governed writes to safely converge over legacy entries.
    - Infinispan `MetadataValue.getVersion()` remains strictly an opaque CAS token for `replaceWithVersion`.
- **Convergence Semantics**:
    - `ConvergenceStatus.CONVERGED` explicitly means **"no further cache action is required"** (the cache already reflects this authoritative version or a newer authoritative progression), NOT "the supplied committed representation was written to cache".
    - If the cached representation is newer or equal ($V_{cached} \ge V_{committed}$), convergence immediately succeeds with `CONVERGED` without overwriting the newer state.
    - If the cached representation is older ($V_{cached} < V_{committed}$), conditional replacement is attempted using the opaque Hot Rod version token (`replaceWithVersion`).
    - If the cache entry is absent (cold cache), `putIfAbsent` is executed.
    - If CAS fails or retries are exhausted under contention/outage, `ConvergenceStatus.DEGRADED` is returned without unconditionally evicting the cache entry.

---

**Composed Architecture & Sequence Flows**

```mermaid
graph TD
  Caller[Caller: BEFE / Pylai / Erga] -->|GovernedRead + Proposed State| GW[DefaultGovernedWriter in mnemosyne-clinical]
  GW -->|1. Authorize Action| Themis[ThemisAuthorizer]
  Themis -->|Allow / Deny| GW
  GW -->|2. Consume Token (UPDATE only)| MnemeCoord[Mneme: ActiveStateCoordinator]
  MnemeCoord -->|CONSUMED / STALE / UNAVAILABLE| GW
  GW -->|3. Atomic Persistence| Mnemosyne[Mnemosyne: AuthoritativePersistencePort]
  Mnemosyne -->|COMMITTED / CONFLICT / UNKNOWN| GW
  GW -->|4. Guarded CAS Convergence| MnemeConv[Mneme: ActiveStateConvergencePort]
  MnemeConv -->|CONVERGED / DEGRADED| GW
  GW -->|WriteResult<T>| Caller
```

**UPDATE Execution Flow**
1. **Validate Input**: Check non-null `GovernedRead`, `proposed`, `securityContext`, and matching resource types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.UPDATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Active-State Progression (Mneme)**:
    - Call `coordinator.consume(current.key(), current.activeToken())`.
    - If `STALE`: return `WriteResult.activeStateConflict(ActiveStateConflict.of(key, "Active state token is stale"))`.
    - If `UNAVAILABLE`: return `WriteResult.notCommitted(key, "Active state coordination unavailable")`.
4. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.update(current.key(), proposed, current.expectedAuthoritativeVersion())`.
    - If `Conflict(EXPECTED_VERSION_MISMATCH)`: return `WriteResult.authoritativeConflict(conflict.conflict())`. (Token remains consumed).
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`. (Token remains consumed).
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`. (Token remains consumed, no retry).
5. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, newVersion)`: call `convergencePort.converge(key, resource, newVersion)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, newVersion)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, newVersion, "Mneme cache convergence degraded")`.

**CREATE Execution Flow**
1. **Validate Input**: Check non-null `ResourceKey`, `resource`, `securityContext`, and matching types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.CREATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.create(key, resource)`.
    - If `Conflict(RESOURCE_ALREADY_EXISTS)`: return `WriteResult.authoritativeConflict(conflict.conflict())`.
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`.
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`.
4. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, v1)`: call `convergencePort.converge(key, resource, v1)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, v1)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, v1, "Mneme cache convergence degraded")`.

---

**Failure and Outcome Mapping Matrix**

| Scenario / Condition | Themis Decision | Mneme Coordination | Mnemosyne Persistence | Mneme Convergence | WriteResult Variant | Token Consumed? | Database State |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **A. Themis Denies** | `DENY` | Not Called | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **B. Mneme Stale** | `ALLOW` | `STALE` | Not Called | Not Called | `WriteResult.ActiveConflict` | No | Unchanged |
| **C. Mneme Unavailable** | `ALLOW` | `UNAVAILABLE` | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **D. Authoritative Conflict** | `ALLOW` | `CONSUMED` | `Conflict(VERSION_MISMATCH)` | Not Called | `WriteResult.AuthoritativeConflict` | Yes | Unchanged |
| **E. Persistence Not Committed** | `ALLOW` | `CONSUMED` | `NotCommitted` | Not Called | `WriteResult.NotCommitted` | Yes | Unchanged |
| **F. Persistence Unknown** | `ALLOW` | `CONSUMED` | `OutcomeUnknown` | Not Called | `WriteResult.OutcomeUnknown` | Yes | Ambiguous (No Retry) |
| **G. Committed + Converged** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `CONVERGED` | `WriteResult.Committed(CONVERGED)` | Yes | Committed ($V$) |
| **H. Committed + Degraded** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `DEGRADED` | `WriteResult.Committed(DEGRADED)` | Yes | Committed ($V$) |
| **I. CREATE Duplicate** | `ALLOW` | N/A | `Conflict(ALREADY_EXISTS)` | Not Called | `WriteResult.AuthoritativeConflict` | N/A | Unchanged |

---

**Post-Commit Mneme Convergence Algorithm**

```java
public class HotRodMnemeConvergence implements ActiveStateConvergencePort {
    public static final String AUTHORITATIVE_VERSION_EXT_URL = "http://harmonia.fhirfactory.net/structure/authoritative-version";

    private final RemoteCacheManager cacheManager;
    private final FhirContext fhirContext;

    @Override
    public <T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion) {
        String cacheName = resolveCacheName(key.resourceType());
        RemoteCache<String, String> cache = cacheManager.getCache(cacheName);
        if (cache == null) return ConvergenceStatus.DEGRADED;

        // Attach explicit authoritative-version provenance into FHIR meta.extension
        IBaseResource fhirResource = (IBaseResource) committedResource;
        MnemeCachedResource.attachAuthoritativeVersion(fhirResource, committedVersion);
        String payloadJson = fhirContext.newJsonParser().encodeResourceToString(fhirResource);
        long targetVer = committedVersion.value();

        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                MetadataValue<String> currentMeta = cache.getWithMetadata(key.id());
                if (currentMeta != null) {
                    long cachedVer = MnemeCachedResource.extractAuthoritativeVersion(currentMeta.getValue());
                    // Newer-Version Invariant: never overwrite newer cache state.
                    // CONVERGED means no further cache action is required.
                    if (cachedVer >= targetVer) {
                        return ConvergenceStatus.CONVERGED;
                    }
                    // Hot Rod opaque CAS token replacement
                    boolean casSuccess = cache.replaceWithVersion(key.id(), payloadJson, currentMeta.getVersion());
                    if (casSuccess) return ConvergenceStatus.CONVERGED;
                } else {
                    String existing = cache.putIfAbsent(key.id(), payloadJson);
                    if (existing == null) return ConvergenceStatus.CONVERGED;
                }
            } catch (Exception e) {
                return ConvergenceStatus.DEGRADED;
            }
        }
        // CAS exhausted: DO NOT evict entry (preserves newer versions)
        return ConvergenceStatus.DEGRADED;
    }
}
```

---

**Dependency Graph**

```
calliope (contracts: GovernedWriter, GovernedRead, WriteResult, ActiveStateCoordinator,
          ActiveStateConvergencePort, AuthoritativeVersion, ConvergenceStatus)
  └── depends on: themis-api (ThemisAuthorizer, ThemisSecurityContext)
  └── depends on: hapi-fhir-structures-r5

hestia/mneme-cluster (implementations: HotRodActiveStateCoordinator, HotRodMnemeConvergence)
  └── depends on: calliope, infinispan-client-hotrod

hestia/mnemosyne-clinical (persistence & composition: AuthoritativePersistencePort, AuthoritativePersistenceService, DefaultGovernedWriter)
  └── depends on: calliope, themis-api, themis-core, spring-boot-starter-data-jpa

iris/iris-befe (presentation gateway)
  └── depends on: calliope, themis-api, themis-core, hestia/mneme-cluster
```

**Graph Properties**:
- Purely unidirectional.
- Zero cyclic dependencies.
- No JPA, Hibernate, or Infinispan types leaked into `calliope` or `GovernedWriter` API.
- `DefaultGovernedWriter` lives in `mnemosyne-clinical` as a runtime orchestrator.

---

**Concrete File Structure Changes**

**Files to Add:**
1. `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ActiveStateConvergencePort.java` (Domain convergence port interface)
2. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/MnemeCachedResource.java` (Helper to attach/extract `AuthoritativeVersion` provenance via standard FHIR `Resource.meta.extension`)
3. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergence.java` (Hot Rod CAS convergence implementation)
4. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriter.java` (Production orchestrator in runtime module)
5. `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriterTest.java` (Comprehensive composition unit tests)
6. `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergenceTest.java` (Convergence CAS & newer-version tests)
7. `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteCompositionArchitectureTest.java` (ArchUnit rules)

**Files Preserved Without Relocation:**
1. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java` (Remains in `mnemosyne-clinical`)
2. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/model/AuthoritativePersistenceResult.java` (Remains in `mnemosyne-clinical`)
3. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java` (Remains in `mnemosyne-clinical`)

**Testing**

**Validation Approach**  
Verification of Step 08.04D composition will be performed using a multi-tier testing strategy:
1. **Composition Unit Tests (`DefaultGovernedWriterTest`)**:
    - Uses Mockito-based test doubles for `ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, and `ActiveStateConvergencePort`.
    - Validates all 15 permutations of the Failure and Outcome Mapping Matrix.
2. **Convergence CAS Unit & Scenario Tests (`HotRodMnemeConvergenceTest`)**:
    - Validates the newer-version protection invariant ($V_{cached} \ge V_{committed}$ is preserved).
    - Validates cold-cache population via `putIfAbsent`.
    - Validates CAS loss handling and non-eviction on retry exhaustion.
3. **ArchUnit Architecture Tests (`GovernedWriteCompositionArchitectureTest`)**:
    - Enforces package layering, zero dependency cycles, and zero JPA/Infinispan leaks into `calliope`.

---

**Key Scenarios**

**Scenario 1: Authorised UPDATE Happy Path**
- **Setup**: `GovernedRead` with V1 and token `T1`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne commits V2 $\to$ Convergence succeeds with V2.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.CONVERGED`, version `AuthoritativeVersion.of(2L)`.

**Scenario 2: Themis Denied**
- **Setup**: `GovernedRead` with V1. Themis mock configured to return `DENY`.
- **Flow**: Themis denies $\to$ write immediately terminates.
- **Outcome**: `WriteResult.NotCommitted`. Zero invocations on `ActiveStateCoordinator` and `AuthoritativePersistencePort`.

**Scenario 3: Stale ActiveStateToken**
- **Setup**: `GovernedRead` with stale token `T_stale`.
- **Flow**: Themis allows $\to$ Mneme returns `STALE` $\to$ write immediately terminates.
- **Outcome**: `WriteResult.ActiveConflict`. Zero invocations on `AuthoritativePersistencePort`.

**Scenario 4: Mneme Unavailable**
- **Setup**: `GovernedRead` with V1. `ActiveStateCoordinator` returns `UNAVAILABLE`.
- **Flow**: Themis allows $\to$ Mneme returns `UNAVAILABLE` $\to$ write terminates with fail-fast visible error.
- **Outcome**: `WriteResult.NotCommitted` (fail-fast, no JVM-local fallback).

**Scenario 5: Authoritative Precondition Conflict (Expected Version Mismatch)**
- **Setup**: `GovernedRead` with V1. Mnemosyne returns `Conflict(EXPECTED_VERSION_MISMATCH, current=V3)`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne returns Conflict.
- **Outcome**: `WriteResult.AuthoritativeConflict(EXPECTED_VERSION_MISMATCH)`. Token `T1` remains consumed.

**Scenario 6: Authoritative Persistence Unknown**
- **Setup**: Mnemosyne returns `OutcomeUnknown("Commit ACK timeout")`.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne returns `OutcomeUnknown`.
- **Outcome**: `WriteResult.OutcomeUnknown`. Token remains consumed. No automatic retry attempted.

**Scenario 7: Committed with Degraded Convergence**
- **Setup**: Mnemosyne commits V2, but Mneme convergence throws timeout exception.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne commits V2 $\to$ Convergence returns `DEGRADED`.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.DEGRADED`. Durable commit remains intact.

**Scenario 8: Delayed Older Convergence Preserves Newer Cache State**
- **Setup**: Cache currently contains V43. Delayed convergence thread attempts to converge committed V42.
- **Flow**: `HotRodMnemeConvergence.converge` reads cached V43 $\to$ detects $43 \ge 42 \to$ terminates immediately with `CONVERGED`.
- **Outcome**: Cache remains unchanged at V43. V42 does not overwrite or evict V43.

**Scenario 9: Authorised CREATE Happy Path & Duplicate Detection**
- **Setup**: Proposed new resource `Patient/100`.
- **Flow A**: Themis allows $\to$ Mnemosyne creates V1 $\to$ Convergence populates cache with V1 $\to$ returns `WriteResult.Committed(V1)`.
- **Flow B**: Concurrent CREATE with same key $\to$ Mnemosyne unique constraint triggers $\to$ returns `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)`.

---

**Architecture Guardrails to Add**
- `GovernedWriteCompositionArchitectureTest`:
    1. `GovernedWriter` resides in `calliope` and `DefaultGovernedWriter` resides in `hestia/mnemosyne-clinical`.
    2. `calliope` must have zero dependencies on `hestia`, `iris`, `pylai`, `energeia`, `agora`, or `paradeigma`.
    3. `DefaultGovernedWriter` must not import or depend on `org.infinispan..`, `jakarta.persistence..`, `org.hibernate..`, or `ca.uhn.fhir.jpa..`.
    4. `AuthoritativePersistencePort` and `ActiveStateCoordinator` must not expose DELETE, REMOVE, or PURGE methods (ADR-020).
    5. `HotRodMnemeConvergence` must not depend on `AuthoritativePersistencePort` or JPA entities.

**Bypasses & Deferred Work**

**Catalogue of Remaining Bypasses (Post-08.04D)**

While Step 08.04D establishes the complete production governed-write composition, existing runtime components still contain legacy write-path bypasses that will be migrated in subsequent steps:

| Subsystem / Component | File / Location | Legacy Behavior | Target Migration Step |
| :--- | :--- | :--- | :--- |
| **Iris BEFE** | `FhirCacheService.java` | Unconditional `remoteCache.put(id, json)` and `remoteCache.remove(id)` | Task 08.05 / 08.06 (BEFE Governed Writer Migration) |
| **Mneme Persistence** | `FhirRestCacheStore.java` | Asynchronous write-behind store bypassing governed coordination | Task 08.07 (Write-Behind Decommissioning) |
| **Mnemosyne Clinical** | HAPI Resource Providers | Direct `FhirStorageService.createResource/updateResource` | Task 08.05 / Task 09 |
| **Energeia Ponos** | `ErgonBase.java` / `PraxisService.java` | Direct cache updates for task synchronization | Task 08.06 (Workflow Task Governed Migration) |
| **Pylai MLLP** | `pylai-mllp-in` / `pylai-fhir-provider-registry` | Direct cache and storage mutations | Task 08.06 (Gateway Governed Migration) |

---

**Explicitly Deferred Work**
1. **Task 09 (Cache-Aside Point Reads)**: Governed point reads (`GovernedRead<T>`), cache-aside read-through, and read repairs will be designed and implemented in Task 09.
2. **UNKNOWN Outcome Reconciliation Framework**: Automated asynchronous reconciliation of `WriteResult.OutcomeUnknown` is deferred to a dedicated post-Task 08 reconciliation capability.
3. **Legacy Caller Migration**: Modifying BEFE REST endpoints, Pylai gateways, and Ponos routes to call `GovernedWriter` will occur in follow-up integration steps.

**Delivery Steps**

**✓ Step 1: Define Governed-Write Convergence Contract in Calliope**  
Define pure convergence port contract in Calliope while preserving persistence ports in Mnemosyne Clinical.

- Define `ActiveStateConvergencePort` in `calliope` (`net.fhirfactory.harmonia.model.governedwrite`) with method `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
- Verify `AuthoritativePersistencePort<T>` and `AuthoritativePersistenceResult<T>` remain in `hestia/mnemosyne-clinical` without leaking into Calliope.
- Add unit tests in `calliope` verifying contract invariants, immutability, and null safety.

**✓ Step 2: Implement Guarded Mneme Hot Rod Convergence in hestia-mneme-cluster**  
Mneme cluster provides production Hot Rod CAS-loop convergence with explicit authoritative version provenance metadata, ensuring older commits cannot overwrite or invalidate newer cache representations.

- Implement `MnemeCachedResource` helper in `hestia/mneme-cluster` to attach and extract explicit authoritative version provenance via standard FHIR `Resource.meta.extension` (`http://harmonia.fhirfactory.net/structure/authoritative-version`), maintaining 100% backwards compatibility with raw FHIR JSON cache contracts and zero dual-cache consistency hazards.
- Implement `HotRodMnemeConvergence` in `hestia/mneme-cluster` implementing `ActiveStateConvergencePort`.
- Implement CAS loop with `MetadataValue<String> currentMeta = cache.getWithMetadata(id)` and `cache.replaceWithVersion(id, payloadJson, currentMeta.getVersion())`.
- Enforce newer-version protection invariant: if cached authoritative version is greater than or equal to committed version, immediately return `ConvergenceStatus.CONVERGED` (signifying no further cache action required).
- Handle cold cache / empty cache via `putIfAbsent(id, payloadJson)` and treat unversioned legacy cache entries as version 0.
- Enforce failure handling: upon CAS exhaustion or cluster timeout, return `ConvergenceStatus.DEGRADED` without unconditionally evicting the cache entry.
- Implement comprehensive unit and scenario tests for `HotRodMnemeConvergence`.

**✓ Step 3: Implement DefaultGovernedWriter Orchestrator in mnemosyne-clinical**  
GovernedWriter orchestrator composes Themis authorization, Mneme coordination, Mnemosyne persistence, and Mneme convergence into a unified CREATE and UPDATE workflow within the mnemosyne-clinical runtime module.

- Implement `DefaultGovernedWriter` in `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`) implementing `GovernedWriter`.
- Implement `update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext)`:
    - Validate parameters (fail-closed on null).
    - Evaluate Themis authorization via `ThemisAuthorizer.authorize(...)` with `ThemisAction.UPDATE`; return `WriteResult.NotCommitted` on deny.
    - Consume active state token via `ActiveStateCoordinator.consume(key, activeToken)`; return `WriteResult.ActiveConflict` on `STALE` or `WriteResult.NotCommitted` on `UNAVAILABLE`.
    - Persist update via `AuthoritativePersistencePort.update(key, proposed, expectedVersion)`.
    - Map persistence conflict (`EXPECTED_VERSION_MISMATCH`) to `WriteResult.AuthoritativeConflict` with consumed token preserved.
    - Map persistence unknown to `WriteResult.OutcomeUnknown` without automatic retry or token rollback.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` with `CONVERGED` or `DEGRADED`.
- Implement `create(ResourceKey key, T resource, ThemisSecurityContext securityContext)`:
    - Validate parameters.
    - Evaluate Themis authorization with `ThemisAction.CREATE`; return `WriteResult.NotCommitted` on deny.
    - Persist new resource via `AuthoritativePersistencePort.create(key, resource)` (atomic database uniqueness check); return `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)` on conflict.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
- Implement comprehensive composition unit tests covering all 15 outcome permutations and concurrency scenarios.

*** Step 4: Add ArchUnit Architecture Guardrails and Integration Verification**  
Paradeigma test suite enforces architectural layering, isolation, and absence of physical deletion across the composed governed-write stack.

- Update `GovernedWriteContractArchitectureTest` in `paradeigma/paradeigma-test` to verify that `GovernedWriter` contracts remain pure in `calliope`.
- Update `MnemosyneAuthoritativePersistenceArchitectureTest` to assert that `DefaultGovernedWriter` in `mnemosyne-clinical` remains free of direct Infinispan Hot Rod classes.
- Add ArchUnit rules verifying that `GovernedWriter`, `AuthoritativePersistencePort`, and `ActiveStateCoordinator` do not expose DELETE or REMOVE methods.
- Verify full test suite and architecture rules with `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`.



I am creating the GovernedWriteCompositionArchitectureTest ArchUnit rules and verifying the entire architectural test suite across the repository.

I have verified that all governed-write architecture rules pass. I am now running the test suites across Calliope, mneme-cluster, and mnemosyne-clinical to confirm complete verification.

**Requirements**

**Overview & Goals**  
Step 08.04D designs the production composition of the governed-write capabilities established in Steps 08.04A (Caller-facing Contracts), 08.04B (Mneme Active-State Coordination), and 08.04C (Mnemosyne Atomic Authoritative Persistence).

The core objective is to compose:
1. **Themis Security Authorization** (`ThemisAuthorizer`, `ThemisSecurityContext`)
2. **Mneme Active-State Coordination** (`ActiveStateCoordinator.consume`)
3. **Mnemosyne Authoritative Persistence** (`AuthoritativePersistencePort.create` / `update`)
4. **Guarded Mneme Post-Commit Convergence** (`ActiveStateConvergencePort.converge`)

into a single unified, deterministic `DefaultGovernedWriter` implementation supporting `CREATE` and `UPDATE` operations while upholding the fundamental architectural baselines (ADR-018, ADR-019, ADR-020).

**Scope**
- **In Scope**:
    - Architectural placement of `DefaultGovernedWriter` composition in existing runtime module `hestia/mnemosyne-clinical` (preserving `calliope` as a pure contract/model library and preserving `AuthoritativePersistencePort` in `mnemosyne-clinical`).
    - Explicit execution sequence for governed `UPDATE` and governed `CREATE`.
    - Formal architectural decision on `CREATE` active-state coordination vs direct authoritative creation with post-commit convergence.
    - Themis authorization enforcement point (strictly evaluated *before* active token consumption and persistence).
    - Complete failure and outcome mapping matrix covering all combinations of Themis, Mneme, and Mnemosyne results.
    - Post-commit Mneme convergence CAS-loop algorithm with explicit authoritative-version provenance metadata and strict newer-version protection (preventing older commits from overwriting or invalidating newer cached versions).
    - Precise semantics for `ConvergenceStatus.CONVERGED` ("no further cache action required") and `ConvergenceStatus.DEGRADED`.
    - Truthful preservation of `UNKNOWN` commit outcomes without automatic retries or token rollbacks.
    - Comprehensive unit and scenario test plans covering all concurrency, authorization, coordination, persistence, and convergence states.
    - ArchUnit architectural rules enforcing module isolation, zero cyclic dependencies, and zero physical DELETE semantics.
    - Catalogue of existing bypasses and explicit boundary definition with Task 09 (Cache-aside point reads).

- **Out of Scope**:
    - Full caller migration across BEFE, Pylai, and Energeia (deferred to subsequent integration steps).
    - Implementation of physical DELETE operations (strictly prohibited by ADR-020).
    - Implementation of Task 09 cache-aside point reads and read repairs.
    - Implementation of automated reconciliation machinery for `UNKNOWN` outcomes (deferred to future reconciliation task).
    - Creation of redundant Maven modules (`hestia-governance`, `clinical-write-orchestrator-service`).

**User Stories**
- **As a Clinical Gateway or BEFE Resource Provider**, I want to execute governed updates through a unified `GovernedWriter` that validates Themis authorization, consumes the active state token in Mneme, and conditionally commits to Mnemosyne, so that lost updates and unauthorized modifications are prevented.
- **As a System Administrator**, I want authoritative writes that succeed in Mnemosyne to remain committed even if transient cache convergence fails (returning `Committed` with `DEGRADED` convergence), so that durable persistence is never rolled back due to ephemeral cache grid issues.
- **As a Security Auditor**, I want all write operations to evaluate Themis policies before consuming coordination tokens or touching durable databases, so that unauthorized requests are rejected cleanly at the gate.

**Functional Requirements**
1. **Governed UPDATE Execution**:
    - Accepts `GovernedRead<T> current`, proposed state `T proposed`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately without touching Mneme or Mnemosyne.
    - Consumes observed `ActiveStateToken` via `ActiveStateCoordinator.consume`:
        - If `STALE`: aborts immediately and returns `WriteResult.ActiveConflict`.
        - If `UNAVAILABLE`: aborts immediately and returns `WriteResult.NotCommitted` (fail-fast, zero local fallback).
    - Executes authoritative persistence via `AuthoritativePersistencePort.update`:
        - If `Conflict(EXPECTED_VERSION_MISMATCH)`: returns `WriteResult.AuthoritativeConflict` (token remains consumed).
        - If `NotCommitted`: returns `WriteResult.NotCommitted` (token remains consumed).
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown` (token remains consumed, no retry).
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
2. **Governed CREATE Execution**:
    - Accepts `ResourceKey key`, initial state `T resource`, and `ThemisSecurityContext securityContext`.
    - Evaluates Themis authorization: if denied, aborts immediately.
    - Executes authoritative persistence via `AuthoritativePersistencePort.create` (Mnemosyne atomically enforces absence precondition via unique constraints):
        - If `Conflict(RESOURCE_ALREADY_EXISTS)`: returns `WriteResult.AuthoritativeConflict`.
        - If `NotCommitted`: returns `WriteResult.NotCommitted`.
        - If `OutcomeUnknown`: returns `WriteResult.OutcomeUnknown`.
        - If `Committed`: triggers post-commit Mneme convergence and returns `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
3. **No Physical DELETE**:
    - `GovernedWriter` exposes only `create` and `update`. All lifecycle transitions (e.g. deprecation, deactivation, suspension) are authoritative `update` operations with FHIR status codes.

**Non-Functional Requirements**
- **Independence & Isolation**: Mnemosyne persistence must not depend on Mneme cache. Mneme cache loss must not affect durable database correctness.
- **Zero Local Fallback**: Distributed cache outages must produce visible failures (`UNAVAILABLE` / `DEGRADED`), never silent fallbacks to JVM-local synchronization maps.
- **Distinct Version Domains**: The four version domains (`ActiveStateToken`, `AuthoritativeVersion`, `FHIR meta.versionId`, `HTTP ETag`) must remain strictly isolated. No cross-domain version assumptions or synthetic versions.
- **Deterministic Outcome Mapping**: Every combination of underlying outcomes must map unambiguously into the sealed `WriteResult<T>` hierarchy.

**Technical Design**

**Current Implementation & Baseline Artifacts**
- **Caller-Facing Contracts (`calliope`)**:
    - `GovernedWriter`: Interface defining `<T> WriteResult<T> create(...)` and `<T> WriteResult<T> update(...)`.
    - `GovernedRead<T>`: Record packaging `ResourceKey`, payload `T`, `ActiveStateToken`, and `AuthoritativeVersion`.
    - `WriteResult<T>`: Sealed interface with records `Committed<T>`, `ActiveConflict<T>`, `AuthoritativeConflict<T>`, `OutcomeUnknown<T>`, and `NotCommitted<T>`.
    - `ActiveStateCoordinator`: Interface defining `observe(ResourceKey)` and `consume(ResourceKey, ActiveStateToken)`.
    - `ActiveStateConvergencePort`: Domain port interface defining `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
    - `ConvergenceStatus`: Sealed hierarchy / enum (`CONVERGED`, `DEGRADED`).
- **Mneme Active-State Coordination (`hestia/mneme-cluster`)**:
    - `HotRodActiveStateCoordinator`: Implements `ActiveStateCoordinator` using Hot Rod `replaceWithVersion` against `active-coordination-cache`.
- **Mnemosyne Authoritative Persistence (`hestia/mnemosyne-clinical`)**:
    - `AuthoritativePersistencePort<T>`: Port interface defining `create` and `update` (remains in `mnemosyne-clinical`).
    - `AuthoritativePersistenceService`: Implements `AuthoritativePersistencePort<IBaseResource>` using `TransactionTemplate` and conditional SQL updates.
    - `AuthoritativePersistenceResult<T>`: Sealed interface with `Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown` (remains in `mnemosyne-clinical`).
- **Themis Policy Evaluation (`themis/themis-api` & `themis/themis-core`)**:
    - `ThemisAuthorizer`: Evaluates `ThemisAuthorizationRequest` and returns `ThemisAuthorizationDecision`.
    - `ThemisSecurityContext`: Immutable context carrying originating/executing principals, authorities, correlation, and causation IDs.

---

**Key Architectural Decisions**

**Decision 1: Module Placement of GovernedWriter Composition**
- **Chosen Approach**: Place the production `DefaultGovernedWriter` implementation in `hestia/mnemosyne-clinical` (under `net.fhirfactory.harmonia.hapifhir.governed`), preserving `AuthoritativePersistencePort` and `AuthoritativePersistenceResult` in `mnemosyne-clinical`. `calliope` remains strictly a library for canonical models and domain contracts (`GovernedWriter`, `GovernedRead`, `WriteResult`, `ActiveStateCoordinator`, `ActiveStateConvergencePort`, `AuthoritativeVersion`, `ConvergenceStatus`). Hot Rod convergence logic (`HotRodMnemeConvergence`) is placed in `hestia/mneme-cluster`.
- **Rationale**:
    - `calliope` owns canonical schemas and semantic governance. It must never become an application runtime service orchestrator.
    - `hestia/mnemosyne-clinical` is the smallest existing runtime module that already depends on `calliope`, `themis-api`, and `themis-core`, and houses the clinical persistence implementations.
    - `DefaultGovernedWriter` interacts solely with domain interfaces (`ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, `ActiveStateConvergencePort`), without depending on Infinispan/Hot Rod concrete classes.
    - This avoids creating redundant Maven modules (e.g. `hestia-governance`), avoids dependency cycles, avoids leaking JPA/Infinispan into Calliope, and keeps contracts pure.

**Decision 2: Themis Authorization Precedence**
- **Chosen Approach**: Evaluate Themis authorization as the very first step in both `create` and `update`, strictly *before* consuming the `ActiveStateToken` in Mneme and *before* invoking Mnemosyne.
- **Rationale**: An unauthorized caller must not mutate active coordination state, burn ActiveStateTokens, or trigger database queries.

**Decision 3: CREATE Precondition Model & Active-State Coordination Evaluation**
- **Evaluation of Alternatives**:
    - **Option A (Chosen): Direct Authoritative CREATE with Post-Commit Convergence**
        - `Themis -> Mnemosyne CREATE -> Mneme Post-Commit Convergence`
    - **Option B (Rejected): Pre-Persistence Mneme Cold-State Coordination**
        - `Themis -> Mneme cold-state coordination -> Mnemosyne CREATE -> Mneme convergence`
- **Architectural Rationale & Invariant**:
    - An `ActiveStateToken` represents an observed point-in-time active state of an *existing* resource. For a newly created resource, no prior active state exists to observe.
    - Mnemosyne exclusively defines the durable authoritative state boundary (ADR-018) and owns the authoritative *absence* precondition (enforced via database unique constraints on `[resource_type, fhir_id]`).
    - Option B would require reserving a speculative token or placeholder marker in Mneme for an absent resource prior to database insertion. If Mnemosyne persistence subsequently fails, or if Mneme partitions/restarts, this creates phantom reservations, requires token rollback machinery (violating the fundamental invariant that consumed tokens are never rolled back), and creates distributed dual-master race conditions between cache and database.
    - **Invariant**: *Mnemosyne exclusively owns the authoritative absence precondition. CREATE operations deliberately do not perform pre-persistence active-state coordination in Mneme, and instead seed Mneme active state via post-commit convergence upon successful authoritative creation ($V=1$).*
    - No synthetic authoritative version, lease, or JVM lock is invented.

**Decision 4: Guarded Mneme Post-Commit Convergence with Distinct Version Domains**
- **Version Domain Separation**:
    1. `ActiveStateToken`: Opaque Hot Rod entry version CAS token for active-state coordination.
    2. `AuthoritativeVersion`: Mnemosyne durable database sequence version ($1, 2, 3\dots$).
    3. FHIR `Resource.meta.versionId`: FHIR specification payload field (string).
    4. HTTP ETag / `If-Match`: HTTP transport concurrency header.
    - *Invariant*: Convergence must never assume `FHIR meta.versionId == AuthoritativeVersion`.
- **Authoritative Provenance in Mneme**:
    - Post-commit convergence compares the incoming `AuthoritativeVersion` against explicit metadata describing the authoritative version represented by the cached Mneme value.
    - **Cache Contract Analysis & Chosen Representation**:
        - *Investigation of Existing Caches*: All existing clinical caches (`person-cache`, `task-cache`, `organization-cache`, etc.) store raw FHIR JSON `String`. Existing readers (`FhirCacheService`, `TaskCacheService`, `DefaultTaskService`) parse cached values directly using `FhirContext.newJsonParser().parseResource(...)`.
        - *Incompatibility of Outer JSON Envelope*: An outer wrapper (`{ authoritativeVersion: ..., resource: ... }`) is invalid FHIR JSON and would cause `DataFormatException` in all existing readers, violating the constraint not to migrate all cache consumers in 08.04D.
        - *Chosen Smallest Compatible Representation (`MnemeCachedResource`)*: Authoritative version provenance is embedded directly inside the standard FHIR resource under `Resource.meta.extension` using canonical URI `http://harmonia.fhirfactory.net/structure/authoritative-version` (with `Integer64Type` or numeric representation).
        - *Backwards Compatibility & Atomicity*: The cached value remains 100% valid raw FHIR JSON. HAPI FHIR parsers across all existing consumers parse it seamlessly without schema errors. The resource payload and its authoritative provenance are read and written atomically in a single Infinispan cache entry, eliminating dual-cache consistency hazards.
        - *Legacy Cache Entries*: Any pre-existing cache entry lacking the authoritative-version extension is treated as $V_{cached} = 0$, allowing governed writes to safely converge over legacy entries.
    - Infinispan `MetadataValue.getVersion()` remains strictly an opaque CAS token for `replaceWithVersion`.
- **Convergence Semantics**:
    - `ConvergenceStatus.CONVERGED` explicitly means **"no further cache action is required"** (the cache already reflects this authoritative version or a newer authoritative progression), NOT "the supplied committed representation was written to cache".
    - If the cached representation is newer or equal ($V_{cached} \ge V_{committed}$), convergence immediately succeeds with `CONVERGED` without overwriting the newer state.
    - If the cached representation is older ($V_{cached} < V_{committed}$), conditional replacement is attempted using the opaque Hot Rod version token (`replaceWithVersion`).
    - If the cache entry is absent (cold cache), `putIfAbsent` is executed.
    - If CAS fails or retries are exhausted under contention/outage, `ConvergenceStatus.DEGRADED` is returned without unconditionally evicting the cache entry.

---

**Composed Architecture & Sequence Flows**

```mermaid
graph TD
  Caller[Caller: BEFE / Pylai / Erga] -->|GovernedRead + Proposed State| GW[DefaultGovernedWriter in mnemosyne-clinical]
  GW -->|1. Authorize Action| Themis[ThemisAuthorizer]
  Themis -->|Allow / Deny| GW
  GW -->|2. Consume Token (UPDATE only)| MnemeCoord[Mneme: ActiveStateCoordinator]
  MnemeCoord -->|CONSUMED / STALE / UNAVAILABLE| GW
  GW -->|3. Atomic Persistence| Mnemosyne[Mnemosyne: AuthoritativePersistencePort]
  Mnemosyne -->|COMMITTED / CONFLICT / UNKNOWN| GW
  GW -->|4. Guarded CAS Convergence| MnemeConv[Mneme: ActiveStateConvergencePort]
  MnemeConv -->|CONVERGED / DEGRADED| GW
  GW -->|WriteResult<T>| Caller
```

**UPDATE Execution Flow**
1. **Validate Input**: Check non-null `GovernedRead`, `proposed`, `securityContext`, and matching resource types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.UPDATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Active-State Progression (Mneme)**:
    - Call `coordinator.consume(current.key(), current.activeToken())`.
    - If `STALE`: return `WriteResult.activeStateConflict(ActiveStateConflict.of(key, "Active state token is stale"))`.
    - If `UNAVAILABLE`: return `WriteResult.notCommitted(key, "Active state coordination unavailable")`.
4. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.update(current.key(), proposed, current.expectedAuthoritativeVersion())`.
    - If `Conflict(EXPECTED_VERSION_MISMATCH)`: return `WriteResult.authoritativeConflict(conflict.conflict())`. (Token remains consumed).
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`. (Token remains consumed).
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`. (Token remains consumed, no retry).
5. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, newVersion)`: call `convergencePort.converge(key, resource, newVersion)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, newVersion)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, newVersion, "Mneme cache convergence degraded")`.

**CREATE Execution Flow**
1. **Validate Input**: Check non-null `ResourceKey`, `resource`, `securityContext`, and matching types.
2. **Authorize (Themis)**:
    - Build `ThemisAuthorizationRequest` with action `ThemisAction.CREATE` and target `ThemisResource.of(key.resourceType(), key.id())`.
    - Call `themisAuthorizer.authorize(request)`. If denied, return `WriteResult.notCommitted(key, "Themis authorization denied: " + decision.message())`.
3. **Authoritative Persistence (Mnemosyne)**:
    - Call `persistencePort.create(key, resource)`.
    - If `Conflict(RESOURCE_ALREADY_EXISTS)`: return `WriteResult.authoritativeConflict(conflict.conflict())`.
    - If `NotCommitted`: return `WriteResult.notCommitted(key, notCommitted.failureMessage())`.
    - If `OutcomeUnknown`: return `WriteResult.outcomeUnknown(key, unknown.message())`.
4. **Guarded Post-Commit Convergence**:
    - If `Committed(resource, v1)`: call `convergencePort.converge(key, resource, v1)`.
    - If `CONVERGED`: return `WriteResult.committed(key, resource, v1)`.
    - If `DEGRADED`: return `WriteResult.committedDegraded(key, resource, v1, "Mneme cache convergence degraded")`.

---

**Failure and Outcome Mapping Matrix**

| Scenario / Condition | Themis Decision | Mneme Coordination | Mnemosyne Persistence | Mneme Convergence | WriteResult Variant | Token Consumed? | Database State |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **A. Themis Denies** | `DENY` | Not Called | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **B. Mneme Stale** | `ALLOW` | `STALE` | Not Called | Not Called | `WriteResult.ActiveConflict` | No | Unchanged |
| **C. Mneme Unavailable** | `ALLOW` | `UNAVAILABLE` | Not Called | Not Called | `WriteResult.NotCommitted` | No | Unchanged |
| **D. Authoritative Conflict** | `ALLOW` | `CONSUMED` | `Conflict(VERSION_MISMATCH)` | Not Called | `WriteResult.AuthoritativeConflict` | Yes | Unchanged |
| **E. Persistence Not Committed** | `ALLOW` | `CONSUMED` | `NotCommitted` | Not Called | `WriteResult.NotCommitted` | Yes | Unchanged |
| **F. Persistence Unknown** | `ALLOW` | `CONSUMED` | `OutcomeUnknown` | Not Called | `WriteResult.OutcomeUnknown` | Yes | Ambiguous (No Retry) |
| **G. Committed + Converged** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `CONVERGED` | `WriteResult.Committed(CONVERGED)` | Yes | Committed ($V$) |
| **H. Committed + Degraded** | `ALLOW` | `CONSUMED` (or N/A) | `Committed(V)` | `DEGRADED` | `WriteResult.Committed(DEGRADED)` | Yes | Committed ($V$) |
| **I. CREATE Duplicate** | `ALLOW` | N/A | `Conflict(ALREADY_EXISTS)` | Not Called | `WriteResult.AuthoritativeConflict` | N/A | Unchanged |

---

**Post-Commit Mneme Convergence Algorithm**

```java
public class HotRodMnemeConvergence implements ActiveStateConvergencePort {
    public static final String AUTHORITATIVE_VERSION_EXT_URL = "http://harmonia.fhirfactory.net/structure/authoritative-version";

    private final RemoteCacheManager cacheManager;
    private final FhirContext fhirContext;

    @Override
    public <T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion) {
        String cacheName = resolveCacheName(key.resourceType());
        RemoteCache<String, String> cache = cacheManager.getCache(cacheName);
        if (cache == null) return ConvergenceStatus.DEGRADED;

        // Attach explicit authoritative-version provenance into FHIR meta.extension
        IBaseResource fhirResource = (IBaseResource) committedResource;
        MnemeCachedResource.attachAuthoritativeVersion(fhirResource, committedVersion);
        String payloadJson = fhirContext.newJsonParser().encodeResourceToString(fhirResource);
        long targetVer = committedVersion.value();

        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                MetadataValue<String> currentMeta = cache.getWithMetadata(key.id());
                if (currentMeta != null) {
                    long cachedVer = MnemeCachedResource.extractAuthoritativeVersion(currentMeta.getValue());
                    // Newer-Version Invariant: never overwrite newer cache state.
                    // CONVERGED means no further cache action is required.
                    if (cachedVer >= targetVer) {
                        return ConvergenceStatus.CONVERGED;
                    }
                    // Hot Rod opaque CAS token replacement
                    boolean casSuccess = cache.replaceWithVersion(key.id(), payloadJson, currentMeta.getVersion());
                    if (casSuccess) return ConvergenceStatus.CONVERGED;
                } else {
                    String existing = cache.putIfAbsent(key.id(), payloadJson);
                    if (existing == null) return ConvergenceStatus.CONVERGED;
                }
            } catch (Exception e) {
                return ConvergenceStatus.DEGRADED;
            }
        }
        // CAS exhausted: DO NOT evict entry (preserves newer versions)
        return ConvergenceStatus.DEGRADED;
    }
}
```

---

**Dependency Graph**

```
calliope (contracts: GovernedWriter, GovernedRead, WriteResult, ActiveStateCoordinator,
          ActiveStateConvergencePort, AuthoritativeVersion, ConvergenceStatus)
  └── depends on: themis-api (ThemisAuthorizer, ThemisSecurityContext)
  └── depends on: hapi-fhir-structures-r5

hestia/mneme-cluster (implementations: HotRodActiveStateCoordinator, HotRodMnemeConvergence)
  └── depends on: calliope, infinispan-client-hotrod

hestia/mnemosyne-clinical (persistence & composition: AuthoritativePersistencePort, AuthoritativePersistenceService, DefaultGovernedWriter)
  └── depends on: calliope, themis-api, themis-core, spring-boot-starter-data-jpa

iris/iris-befe (presentation gateway)
  └── depends on: calliope, themis-api, themis-core, hestia/mneme-cluster
```

**Graph Properties**:
- Purely unidirectional.
- Zero cyclic dependencies.
- No JPA, Hibernate, or Infinispan types leaked into `calliope` or `GovernedWriter` API.
- `DefaultGovernedWriter` lives in `mnemosyne-clinical` as a runtime orchestrator.

---

**Concrete File Structure Changes**

**Files to Add:**
1. `calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite/ActiveStateConvergencePort.java` (Domain convergence port interface)
2. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/MnemeCachedResource.java` (Helper to attach/extract `AuthoritativeVersion` provenance via standard FHIR `Resource.meta.extension`)
3. `hestia/mneme-cluster/src/main/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergence.java` (Hot Rod CAS convergence implementation)
4. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriter.java` (Production orchestrator in runtime module)
5. `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriterTest.java` (Comprehensive composition unit tests)
6. `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/hestia/mneme/convergence/HotRodMnemeConvergenceTest.java` (Convergence CAS & newer-version tests)
7. `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/GovernedWriteCompositionArchitectureTest.java` (ArchUnit rules)

**Files Preserved Without Relocation:**
1. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java` (Remains in `mnemosyne-clinical`)
2. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/model/AuthoritativePersistenceResult.java` (Remains in `mnemosyne-clinical`)
3. `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java` (Remains in `mnemosyne-clinical`)

**Testing**

**Validation Approach**  
Verification of Step 08.04D composition will be performed using a multi-tier testing strategy:
1. **Composition Unit Tests (`DefaultGovernedWriterTest`)**:
    - Uses Mockito-based test doubles for `ThemisAuthorizer`, `ActiveStateCoordinator`, `AuthoritativePersistencePort`, and `ActiveStateConvergencePort`.
    - Validates all 15 permutations of the Failure and Outcome Mapping Matrix.
2. **Convergence CAS Unit & Scenario Tests (`HotRodMnemeConvergenceTest`)**:
    - Validates the newer-version protection invariant ($V_{cached} \ge V_{committed}$ is preserved).
    - Validates cold-cache population via `putIfAbsent`.
    - Validates CAS loss handling and non-eviction on retry exhaustion.
3. **ArchUnit Architecture Tests (`GovernedWriteCompositionArchitectureTest`)**:
    - Enforces package layering, zero dependency cycles, and zero JPA/Infinispan leaks into `calliope`.

---

**Key Scenarios**

**Scenario 1: Authorised UPDATE Happy Path**
- **Setup**: `GovernedRead` with V1 and token `T1`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne commits V2 $\to$ Convergence succeeds with V2.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.CONVERGED`, version `AuthoritativeVersion.of(2L)`.

**Scenario 2: Themis Denied**
- **Setup**: `GovernedRead` with V1. Themis mock configured to return `DENY`.
- **Flow**: Themis denies $\to$ write immediately terminates.
- **Outcome**: `WriteResult.NotCommitted`. Zero invocations on `ActiveStateCoordinator` and `AuthoritativePersistencePort`.

**Scenario 3: Stale ActiveStateToken**
- **Setup**: `GovernedRead` with stale token `T_stale`.
- **Flow**: Themis allows $\to$ Mneme returns `STALE` $\to$ write immediately terminates.
- **Outcome**: `WriteResult.ActiveConflict`. Zero invocations on `AuthoritativePersistencePort`.

**Scenario 4: Mneme Unavailable**
- **Setup**: `GovernedRead` with V1. `ActiveStateCoordinator` returns `UNAVAILABLE`.
- **Flow**: Themis allows $\to$ Mneme returns `UNAVAILABLE` $\to$ write terminates with fail-fast visible error.
- **Outcome**: `WriteResult.NotCommitted` (fail-fast, no JVM-local fallback).

**Scenario 5: Authoritative Precondition Conflict (Expected Version Mismatch)**
- **Setup**: `GovernedRead` with V1. Mnemosyne returns `Conflict(EXPECTED_VERSION_MISMATCH, current=V3)`.
- **Flow**: Themis allows $\to$ Mneme consumes `T1` $\to$ Mnemosyne returns Conflict.
- **Outcome**: `WriteResult.AuthoritativeConflict(EXPECTED_VERSION_MISMATCH)`. Token `T1` remains consumed.

**Scenario 6: Authoritative Persistence Unknown**
- **Setup**: Mnemosyne returns `OutcomeUnknown("Commit ACK timeout")`.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne returns `OutcomeUnknown`.
- **Outcome**: `WriteResult.OutcomeUnknown`. Token remains consumed. No automatic retry attempted.

**Scenario 7: Committed with Degraded Convergence**
- **Setup**: Mnemosyne commits V2, but Mneme convergence throws timeout exception.
- **Flow**: Themis allows $\to$ Mneme consumes token $\to$ Mnemosyne commits V2 $\to$ Convergence returns `DEGRADED`.
- **Outcome**: `WriteResult.Committed` with `ConvergenceStatus.DEGRADED`. Durable commit remains intact.

**Scenario 8: Delayed Older Convergence Preserves Newer Cache State**
- **Setup**: Cache currently contains V43. Delayed convergence thread attempts to converge committed V42.
- **Flow**: `HotRodMnemeConvergence.converge` reads cached V43 $\to$ detects $43 \ge 42 \to$ terminates immediately with `CONVERGED`.
- **Outcome**: Cache remains unchanged at V43. V42 does not overwrite or evict V43.

**Scenario 9: Authorised CREATE Happy Path & Duplicate Detection**
- **Setup**: Proposed new resource `Patient/100`.
- **Flow A**: Themis allows $\to$ Mnemosyne creates V1 $\to$ Convergence populates cache with V1 $\to$ returns `WriteResult.Committed(V1)`.
- **Flow B**: Concurrent CREATE with same key $\to$ Mnemosyne unique constraint triggers $\to$ returns `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)`.

---

**Architecture Guardrails to Add**
- `GovernedWriteCompositionArchitectureTest`:
    1. `GovernedWriter` resides in `calliope` and `DefaultGovernedWriter` resides in `hestia/mnemosyne-clinical`.
    2. `calliope` must have zero dependencies on `hestia`, `iris`, `pylai`, `energeia`, `agora`, or `paradeigma`.
    3. `DefaultGovernedWriter` must not import or depend on `org.infinispan..`, `jakarta.persistence..`, `org.hibernate..`, or `ca.uhn.fhir.jpa..`.
    4. `AuthoritativePersistencePort` and `ActiveStateCoordinator` must not expose DELETE, REMOVE, or PURGE methods (ADR-020).
    5. `HotRodMnemeConvergence` must not depend on `AuthoritativePersistencePort` or JPA entities.

**Bypasses & Deferred Work**

**Catalogue of Remaining Bypasses (Post-08.04D)**

While Step 08.04D establishes the complete production governed-write composition, existing runtime components still contain legacy write-path bypasses that will be migrated in subsequent steps:

| Subsystem / Component | File / Location | Legacy Behavior | Target Migration Step |
| :--- | :--- | :--- | :--- |
| **Iris BEFE** | `FhirCacheService.java` | Unconditional `remoteCache.put(id, json)` and `remoteCache.remove(id)` | Task 08.05 / 08.06 (BEFE Governed Writer Migration) |
| **Mneme Persistence** | `FhirRestCacheStore.java` | Asynchronous write-behind store bypassing governed coordination | Task 08.07 (Write-Behind Decommissioning) |
| **Mnemosyne Clinical** | HAPI Resource Providers | Direct `FhirStorageService.createResource/updateResource` | Task 08.05 / Task 09 |
| **Energeia Ponos** | `ErgonBase.java` / `PraxisService.java` | Direct cache updates for task synchronization | Task 08.06 (Workflow Task Governed Migration) |
| **Pylai MLLP** | `pylai-mllp-in` / `pylai-fhir-provider-registry` | Direct cache and storage mutations | Task 08.06 (Gateway Governed Migration) |

---

**Explicitly Deferred Work**
1. **Task 09 (Cache-Aside Point Reads)**: Governed point reads (`GovernedRead<T>`), cache-aside read-through, and read repairs will be designed and implemented in Task 09.
2. **UNKNOWN Outcome Reconciliation Framework**: Automated asynchronous reconciliation of `WriteResult.OutcomeUnknown` is deferred to a dedicated post-Task 08 reconciliation capability.
3. **Legacy Caller Migration**: Modifying BEFE REST endpoints, Pylai gateways, and Ponos routes to call `GovernedWriter` will occur in follow-up integration steps.

**Delivery Steps**

**✓ Step 1: Define Governed-Write Convergence Contract in Calliope**  
Define pure convergence port contract in Calliope while preserving persistence ports in Mnemosyne Clinical.

- Define `ActiveStateConvergencePort` in `calliope` (`net.fhirfactory.harmonia.model.governedwrite`) with method `<T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion)`.
- Verify `AuthoritativePersistencePort<T>` and `AuthoritativePersistenceResult<T>` remain in `hestia/mnemosyne-clinical` without leaking into Calliope.
- Add unit tests in `calliope` verifying contract invariants, immutability, and null safety.

**✓ Step 2: Implement Guarded Mneme Hot Rod Convergence in hestia-mneme-cluster**  
Mneme cluster provides production Hot Rod CAS-loop convergence with explicit authoritative version provenance metadata, ensuring older commits cannot overwrite or invalidate newer cache representations.

- Implement `MnemeCachedResource` helper in `hestia/mneme-cluster` to attach and extract explicit authoritative version provenance via standard FHIR `Resource.meta.extension` (`http://harmonia.fhirfactory.net/structure/authoritative-version`), maintaining 100% backwards compatibility with raw FHIR JSON cache contracts and zero dual-cache consistency hazards.
- Implement `HotRodMnemeConvergence` in `hestia/mneme-cluster` implementing `ActiveStateConvergencePort`.
- Implement CAS loop with `MetadataValue<String> currentMeta = cache.getWithMetadata(id)` and `cache.replaceWithVersion(id, payloadJson, currentMeta.getVersion())`.
- Enforce newer-version protection invariant: if cached authoritative version is greater than or equal to committed version, immediately return `ConvergenceStatus.CONVERGED` (signifying no further cache action required).
- Handle cold cache / empty cache via `putIfAbsent(id, payloadJson)` and treat unversioned legacy cache entries as version 0.
- Enforce failure handling: upon CAS exhaustion or cluster timeout, return `ConvergenceStatus.DEGRADED` without unconditionally evicting the cache entry.
- Implement comprehensive unit and scenario tests for `HotRodMnemeConvergence`.

**✓ Step 3: Implement DefaultGovernedWriter Orchestrator in mnemosyne-clinical**  
GovernedWriter orchestrator composes Themis authorization, Mneme coordination, Mnemosyne persistence, and Mneme convergence into a unified CREATE and UPDATE workflow within the mnemosyne-clinical runtime module.

- Implement `DefaultGovernedWriter` in `hestia/mnemosyne-clinical` (`net.fhirfactory.harmonia.hapifhir.governed`) implementing `GovernedWriter`.
- Implement `update(GovernedRead<T> current, T proposed, ThemisSecurityContext securityContext)`:
    - Validate parameters (fail-closed on null).
    - Evaluate Themis authorization via `ThemisAuthorizer.authorize(...)` with `ThemisAction.UPDATE`; return `WriteResult.NotCommitted` on deny.
    - Consume active state token via `ActiveStateCoordinator.consume(key, activeToken)`; return `WriteResult.ActiveConflict` on `STALE` or `WriteResult.NotCommitted` on `UNAVAILABLE`.
    - Persist update via `AuthoritativePersistencePort.update(key, proposed, expectedVersion)`.
    - Map persistence conflict (`EXPECTED_VERSION_MISMATCH`) to `WriteResult.AuthoritativeConflict` with consumed token preserved.
    - Map persistence unknown to `WriteResult.OutcomeUnknown` without automatic retry or token rollback.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` with `CONVERGED` or `DEGRADED`.
- Implement `create(ResourceKey key, T resource, ThemisSecurityContext securityContext)`:
    - Validate parameters.
    - Evaluate Themis authorization with `ThemisAction.CREATE`; return `WriteResult.NotCommitted` on deny.
    - Persist new resource via `AuthoritativePersistencePort.create(key, resource)` (atomic database uniqueness check); return `WriteResult.AuthoritativeConflict(RESOURCE_ALREADY_EXISTS)` on conflict.
    - Execute post-commit convergence via `ActiveStateConvergencePort.converge(key, resource, version)` and return `WriteResult.Committed` (`CONVERGED` or `DEGRADED`).
- Implement comprehensive composition unit tests covering all 15 outcome permutations and concurrency scenarios.

**✓ Step 4: Add ArchUnit Architecture Guardrails and Integration Verification**  
Paradeigma test suite enforces architectural layering, isolation, and absence of physical deletion across the composed governed-write stack.

- Update `GovernedWriteContractArchitectureTest` in `paradeigma/paradeigma-test` to verify that `GovernedWriter` contracts remain pure in `calliope`.
- Update `MnemosyneAuthoritativePersistenceArchitectureTest` to assert that `DefaultGovernedWriter` in `mnemosyne-clinical` remains free of direct Infinispan Hot Rod classes.
- Add ArchUnit rules verifying that `GovernedWriter`, `AuthoritativePersistencePort`, and `ActiveStateCoordinator` do not expose DELETE or REMOVE methods.
- Verify full test suite and architecture rules with `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`.



**Summary**
- Implemented the production governed-write composition (`DefaultGovernedWriter`) uniting Themis authorization, Mneme active-state coordination, Mnemosyne authoritative persistence, and guarded post-commit cache convergence.
- Guaranteed complete isolation between durable database persistence and cache convergence with strict newer-version protection.

**Changes**
- Defined `ActiveStateConvergencePort` in `calliope` as a pure domain port interface.
- Implemented `MnemeCachedResource` and `HotRodMnemeConvergence` in `hestia/mneme-cluster` using Hot Rod CAS loop convergence and FHIR `Resource.meta.extension` authoritative version provenance.
- Implemented `DefaultGovernedWriter` in `hestia/mnemosyne-clinical` enforcing Themis authorization precedence before active token consumption and persistence, with full mapping into the sealed `WriteResult<T>` hierarchy.
- Added `GovernedWriteCompositionArchitectureTest` in `paradeigma/paradeigma-test` asserting module isolation, pure contracts, and zero physical DELETE semantics.

**Verification**
- Executed comprehensive unit and scenario test suites in `calliope`, `hestia/mneme-cluster`, `hestia/mnemosyne-clinical`, and `paradeigma/paradeigma-test` (all 76 architecture tests passed with 0 failures).
- Validated all 15 permutations of the failure and outcome matrix, including Themis denial, active state conflict, authoritative version mismatch, outcome unknown, and newer-version preservation.

