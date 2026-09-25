---
sessionId: session-260925-081138-jlol
---

# Requirements

### Goal & Outcome
Enforce the working-state cache boundary established in ADR-019 by eliminating process-local in-memory fallbacks (`ConcurrentHashMap` stores) that substitute for Mneme/Infinispan distributed working-state semantics. When Mneme is unavailable, operations must fail visibly and explicitly rather than silently degrading into local JVM memory.

### Scope
- **In Scope**:
  - Remove forbidden local fallback maps and fallback execution branches from:
    - `iris/iris-befe`: `FhirCacheService.java`, `TaskSequenceCacheService.java`.
    - `pylai/pylai-mllp-base`: `DefaultTaskService.java`, `DefaultProvenanceService.java`.
    - `energeia/erga`: `TaskCacheService.java`.
    - `energeia/praxis`: `PragmaCacheService.java`, `PraxisService.java`.
    - `energeia/ponos`: `MessageQueueService.java`.
  - Review and retain legitimate process-local operational status maps in `ModuleStatusService` (`energeia-ponos`, `iris-befe`, and `pylai-mllp-base`).
  - Ensure failure semantics propagate standard runtime/Infinispan exceptions (e.g. `IllegalStateException` or `CacheException`) when the RemoteCache or Hot Rod client is unavailable.
  - Retain lazy Hot Rod client startup behavior in `HotRodClientProducer` while ensuring service operations fail explicitly when invoked without an active cache connection.
  - Expand focused service tests to verify: RemoteCache available success, RemoteCache unavailable explicit failure, zero fallback read/write operations, and clean recovery upon reconnect.
- **Out of Scope**:
  - Task 08 final Clinical write path redesign or direct BEFE-to-Mnemosyne read/write paths.
  - Task 09 cache-aside read architecture or degraded read modes via Mnemosyne.
  - Task 10 Clinical search redesign.
  - Redesigning Petasos messaging, Mnemosyne JPA repositories, Kleio audit persistence, or Themis security policies.
  - Introducing new generic cache abstractions or availability frameworks.

### Done When
- All 8 identified fallback `ConcurrentHashMap` fields and their associated read, write, delete, search, and seeding fallback branches are deleted.
- Missing RemoteCacheManager, unstarted Hot Rod client, or missing named cache instances cause explicit runtime failures rather than fallback persistence.
- `ModuleStatusService` in all three modules is verified and documented as legitimate process-local operational state.
- Focused unit and service tests in `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass with mocked and configured cache fixtures.
- Full architecture suite (`*ArchitectureTest`) in `paradeigma-test` passes without regression.

# Technical Design

### Decisions
- **Decision: Throw explicit runtime/cache exceptions on missing or failed RemoteCache / not catching and writing to local maps**
  - *Rationale*: ADR-019 dictates that loss of Mneme distributed working state must be visible as loss of capability. Swallowing cache failures or returning JVM-local data creates split-brain state across cluster nodes. Standard exceptions (such as `IllegalStateException` for uninitialized/unavailable cache or propagating `CacheException`) communicate failure cleanly without custom framework bloat.
- **Decision: Retain lazy Hot Rod client producer initialization / not enforcing eager startup crash**
  - *Rationale*: `HotRodClientProducer` creates `new RemoteCacheManager(..., false)` if the cluster is unreachable during bootstrap. Retaining lazy startup allows processes to start in constrained environments while ensuring individual operations fail explicitly when invoking cache calls without an active connection.
- **Decision: Retain `localFallbackCache` in `ModuleStatusService` / not converting to fail-closed distributed lock**
  - *Rationale*: `ModuleStatusService` tracks transient local telemetry and readiness snapshots. It is not workflow, task, or clinical working state. Retaining its local snapshot satisfies the requirement to avoid removing legitimate process-local state.

### Approach & Touches
- **Presentation Tier (`iris-befe`)**:
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java`: Remove `localFallbackCaches`. In `getResourceJson`, `putResourceJson`, `deleteResource`, and `searchResources`, require active `RemoteCache` and fail explicitly when `getRemoteCache()` returns `null` or when remote operations fail.
  - `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/TaskSequenceCacheService.java`: Remove `localFallbackCache`. Eliminate fallback branches in `ensureDefaultSequences()`, `saveSequence()`, `getSequenceJson()`, `deleteSequence()`, and `getAllSequenceJsons()`.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/service/FhirCacheServiceTest.java`: Refactor tests to inject a mocked `RemoteCacheManager` / `RemoteCache`, verifying remote operations, explicit failure when unavailable, and no local storage.
  - `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/service/TaskSequenceCacheServiceTest.java`: Add focused tests verifying remote success, explicit failure on missing cache, and recovery.
- **Gateway Tier (`pylai-mllp-base`)**:
  - `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultTaskService.java`: Remove `taskStore`. Route CRUD, list, count, and clear exclusively through `task-cache` RemoteCache. Fail explicitly when the cache is unavailable.
  - `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultProvenanceService.java`: Remove `provenanceStore`. Route CRUD, list, count, and clear exclusively through `provenance-cache` RemoteCache. Fail explicitly when unavailable.
  - `pylai/pylai-mllp-base/src/test/java/net/fhirfactory/harmonia/mllpgateway/service/TaskServiceTest.java` & `ProvenanceServiceTest.java`: Update test setup to use mocked `RemoteCacheManager` / `RemoteCache` instances. Add failure and recovery test cases.
- **Workflow & Activity Tier (`energeia-erga`, `energeia-praxis`, `energeia-ponos`)**:
  - `energeia/erga/src/main/java/net/fhirfactory/harmonia/praxis/cache/TaskCacheService.java`: Remove `localFallbackCache` and `localFallbackProvenanceCache`. Make `getTaskJson`, `saveTask`, `putTaskJson`, `putProvenanceJson`, `getProvenanceJson`, `deleteTask`, `deleteProvenance`, `count`, and `clear` remote-only.
  - `energeia/praxis/src/main/java/net/fhirfactory/harmonia/praxis/cache/PragmaCacheService.java`: Remove `localFallbackCache` and local fallback init logging. Ensure `savePragma`, `getPragma`, `deletePragma`, and `clear` operate on `RemoteCache`.
  - `energeia/praxis/src/main/java/net/fhirfactory/harmonia/praxis/service/PraxisService.java`: Remove `localFallbackCache`. Route save, get, search, delete, count, and clear solely through `tasksequence-cache` RemoteCache.
  - `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/service/MessageQueueService.java`: Remove `localFallbackCache`. Route queue persistence, retrieval, and default queue seeding solely through `messagequeue-cache` RemoteCache.
  - `energeia/ponos/src/test/java/net/fhirfactory/harmonia/praxis/cache/TaskCacheServiceTest.java`, `PraxisServiceTest.java`, `MessageQueueServiceTest.java`: Refactor test setups to supply mocked/configured cache managers and assert on explicit failure when cache is absent. Add test class for `PragmaCacheService`.

### Nuances / Risks / Corners
- **Test Fixture Mocks**: Existing service tests (`PraxisServiceTest`, `TaskCacheServiceTest`, `MessageQueueServiceTest`, `FhirCacheServiceTest`, `TaskServiceTest`, `ProvenanceServiceTest`) constructed services with no arguments (`null` `RemoteCacheManager`) and asserted on the local fallback behavior. Every test must be updated to provide a mocked `RemoteCacheManager` returning a mocked `RemoteCache` for standard positive cases, plus explicit tests for uninitialized/disconnected manager scenarios.
- **Null Safety vs Explicit Failure**: Valid domain misses (e.g. key not found in cache) must return `null` / `Optional.empty()` normally, whereas infrastructure failure (RemoteCache unavailable or Hot Rod communication exception) must throw an explicit exception.
- **Default Seeding Paths**: Methods like `MessageQueueService.seedDefaultQueues()` and `TaskSequenceCacheService.ensureDefaultSequences()` must interact with the remote cache or fail gracefully without populating a hidden in-memory store.

# Testing

- **Scenario: Normal RemoteCache CRUD Success** — When `RemoteCacheManager` is started and the named cache is present, CRUD operations read and write to the remote cache normally across all services.
- **Scenario: Explicit Failure on Unstarted or Null RemoteCacheManager** — When `RemoteCacheManager` is null, unstarted, or fails to resolve the named cache, write operations throw `IllegalStateException` (or `CacheException`) and do not return mock success.
- **Scenario: Zero Local Fallback State Mutation** — Verify that upon remote cache failure, no internal `ConcurrentHashMap` exists to store or serve stale working state.
- **Scenario: Service Recovery on Cache Reconnection** — Verify that when a mocked `RemoteCacheManager` transitions from unavailable to available, subsequent service calls succeed without stale local cache interference.
- **Scenario: Retained ModuleStatusService Local Telemetry** — Verify that `ModuleStatusService` continues to maintain local operational readiness snapshots while delegating to remote cache when available.
- **Regression Targets**:
  - `iris/iris-befe`: `FhirCacheServiceTest`, `TaskSequenceCacheServiceTest`.
  - `pylai/pylai-mllp-base`: `TaskServiceTest`, `ProvenanceServiceTest`, `ModuleStatusServiceTest`.
  - `energeia/praxis`: `PraxisServiceTest`, `PragmaCacheServiceTest`.
  - `energeia/ponos`: `TaskCacheServiceTest`, `MessageQueueServiceTest`, `ModuleStatusServiceTest`.
  - `paradeigma/paradeigma-test`: `*ArchitectureTest` (all 7 architecture suites).

# Assumptions & Open Questions

- **Significant Assumption: Runtime Exception Vocabulary for Missing RemoteCache**:
  - *Chosen Option*: Throw `IllegalStateException` (e.g., "Mneme cache [task-cache] is unavailable") when `getRemoteCache()` returns `null`, and propagate underlying `CacheException` or `HotRodClientException` on remote transport failure.
  - *Rationale*: Standard Java/Infinispan runtime exception semantics avoid introducing unnecessary custom exception classes or cache abstractions while clearly signaling the absence of distributed working state.
  - *Alternative*: Creating custom `MnemeUnavailableException`.
  - *Impact*: Low complexity, zero unnecessary API surface.
- **Significant Assumption: Retaining ModuleStatusService Process-Local Map**:
  - *Chosen Option*: Retain `localFallbackCache` in `ModuleStatusService` across `ponos`, `befe`, and `mllp-base`.
  - *Rationale*: Module readiness is ephemeral node telemetry, not business/clinical working state.
  - *Alternative*: Forcing module readiness to fail closed without local fallback.
  - *Impact*: Preserves node bootstrap health-check behavior.

# Delivery Steps

### ✓ Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE
Goal: Remove process-local fallback caches from `FhirCacheService` and `TaskSequenceCacheService`, ensuring explicit failure when RemoteCache is unavailable.
Scope: `iris/iris-befe` (`FhirCacheService.java`, `TaskSequenceCacheService.java`, `FhirCacheServiceTest.java`, `TaskSequenceCacheServiceTest.java`).
Acceptance Criteria:
- [ ] `localFallbackCaches` map and all local fallback branches removed from `FhirCacheService.java`.
- [ ] `FhirCacheService` throws explicit runtime exception when RemoteCache is unavailable for write/search operations.
- [ ] `localFallbackCache` map and fallback seeding removed from `TaskSequenceCacheService.java`.
- [ ] `TaskSequenceCacheService` fails explicitly when saving or retrieving sequences without an active RemoteCache.
- [ ] `ModuleStatusService.java` in `iris-befe` verified and documented as retained process-local state.
- [ ] `FhirCacheServiceTest` and new/updated `TaskSequenceCacheServiceTest` verify: remote success, explicit failure when remote cache is unavailable, zero local fallback persistence, and recovery upon cache reconnection.
Verification: `mvn test -pl iris/iris-befe -am -Dtest="FhirCacheServiceTest,TaskSequenceCacheServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

### ✓ Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base
Goal: Remove process-local fallback stores from `DefaultTaskService` and `DefaultProvenanceService`, requiring active RemoteCache for Task and Provenance state.
Scope: `pylai/pylai-mllp-base` (`DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskServiceTest.java`, `ProvenanceServiceTest.java`).
Acceptance Criteria:
- [ ] `taskStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultTaskService.java`.
- [ ] `provenanceStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultProvenanceService.java`.
- [ ] `DefaultTaskService` and `DefaultProvenanceService` throw explicit runtime exceptions on create, update, delete, search, count, or clear when RemoteCache is unavailable.
- [ ] `DefaultModuleStatusService.java` in `pylai-mllp-base` verified and documented as retained process-local state.
- [ ] `TaskServiceTest` and `ProvenanceServiceTest` refactored to use mocked RemoteCacheManager/RemoteCache, verifying remote success, explicit failure when unavailable, and absence of local fallback state.
Verification: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

### ✓ Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia
Goal: Remove process-local fallback stores from `TaskCacheService`, `PragmaCacheService`, `PraxisService`, and `MessageQueueService`.
Scope: `energeia/erga` (`TaskCacheService.java`), `energeia/praxis` (`PragmaCacheService.java`, `PraxisService.java`, `PraxisServiceTest.java`, `PragmaCacheServiceTest.java`), `energeia/ponos` (`MessageQueueService.java`, `TaskCacheServiceTest.java`, `MessageQueueServiceTest.java`).
Acceptance Criteria:
- [ ] `localFallbackCache` and `localFallbackProvenanceCache` removed from `TaskCacheService.java`; task and provenance caching made remote-only.
- [ ] `localFallbackCache` removed from `PragmaCacheService.java`; Pragma persistence and checkpoint queries made remote-only.
- [ ] `localFallbackCache` removed from `PraxisService.java`; workflow definition persistence and queries made remote-only.
- [ ] `localFallbackCache` removed from `MessageQueueService.java`; queue definition persistence and default seeding routed through RemoteCache.
- [ ] `ModuleStatusService.java` in `energeia-ponos` verified and documented as retained process-local state.
- [ ] `TaskCacheServiceTest`, `PraxisServiceTest`, `MessageQueueServiceTest`, and new `PragmaCacheServiceTest` pass, verifying remote operations, explicit failure when remote cache is unavailable, zero local fallback persistence, and recovery upon reconnection.
Verification: `mvn test -pl energeia/praxis,energeia/ponos -am -Dtest="TaskCacheServiceTest,PraxisServiceTest,MessageQueueServiceTest,PragmaCacheServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

### ✓ Step 4: Full Test Suite, Architecture Invariant Verification, and Final Report
Goal: Execute full test verification across all affected modules, validate ArchUnit architectural guardrails, and compile the final Step 03 report.
Scope: Full repository test verification (`iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, `energeia-ponos`, `paradeigma-test`).
Acceptance Criteria:
- [ ] All unit and service test suites across `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass without error.
- [ ] ArchUnit suite in `paradeigma/paradeigma-test` passes without regression (`*ArchitectureTest`).
- [ ] Final report compiled covering all 12 items specified in the parent task (candidate inspection, fallback fields removed, fallback branches removed, post-change failure semantics, retained local maps, lazy Hot Rod behavior assessment, tests added/modified, and guardrail confirmations).
- [ ] Confirmed zero direct Mnemosyne read/write paths introduced, zero Task 08/09/10 leakage, and zero architecture redesign of Petasos, Mnemosyne, Kleio, or Themis.
Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green