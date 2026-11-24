Configured authentication: Managed by agent

Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Inspect the current codebase for Task 07 Step 03 candidates that may implement process-local fallbacks for Mneme/Infinispan distributed state.

Map the exact files, approximate line numbers, and current behavior for these candidate classes/services:
- FhirCacheService
- DefaultTaskService
- DefaultProvenanceService
- TaskCacheService
- PragmaCacheService
- PraxisService
- MessageQueueService
- TaskSequenceCacheService
- ModuleStatusService

For each candidate, determine whether any ConcurrentHashMap or equivalent process-local collection is acting as a fallback substitute for Mneme/RemoteCache semantics, versus being genuinely process-local state.

Also inspect any lazy Hot Rod / RemoteCache producer path relevant to these services if it helps explain how fallback activates.

Return a concise inventory with:
- class/module/path
- approximate line numbers for relevant fields/branches
- what the local map/state does
- whether it appears to be a forbidden Mneme fallback or a legitimate process-local structure
- smallest likely modification/test areas for Step 03

**Constraints**  
Read-only exploration only. Do not modify files. Be concise but include exact paths and approximate line numbers.



Exploration is Ready

<UPDATE>
<PREVIOUS_STEP>
Read-only inspection is complete for all named services, their local collections, remote-cache branches, existing focused tests, and the three Hot Rod producer paths. No files were modified and no tests were run.
</PREVIOUS_STEP>
</UPDATE>

**Inventory**

**Forbidden Mneme fallbacks**

| Class/module/path | Relevant lines | Current local state and behavior | Assessment | Smallest likely Step 03 area |
|---|---:|---|---|---|
| `FhirCacheService` — `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java` | map 53; remote lookup 105–113; get 116–128; put 138–152; delete 190–202; search 216–234 | `localFallbackCaches` is `Map<String, Map<String,String>>`. Reads use RemoteCache first and then local state; writes use local state when the remote cache is absent or `put` throws; deletes and searches also fall back locally. | **Forbidden Mneme fallback.** It holds FHIR resource JSON for the named Infinispan resource caches and transparently continues without Mneme. | Remove `localFallbackCaches` and all local read/write/delete/search branches. Make missing cache or RemoteCache operation failure explicit. Extend `iris/iris-befe/.../FhirCacheServiceTest.java` with mocked available/unavailable RemoteCache cases. |
| `DefaultTaskService` — `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultTaskService.java` | map 55; cache resolver 65–74; create/update 98–110, 153–165; read 123–140; delete 176–189; list/count/clear 192–247 | `taskStore` is a `ConcurrentHashMap<String,Task>`. Every create/update writes it before attempting the remote cache; remote reads are copied into it; all misses and unavailable/failed remote operations return the map contents. | **Forbidden Mneme fallback.** This is Task/Pragma working state, not process-local bookkeeping. | Remove `taskStore`; require the `task-cache` RemoteCache for CRUD/list/count/clear. Update/add tests in the MLLP base service-test area using mocked `RemoteCacheManager`/`RemoteCache`. |
| `DefaultProvenanceService` — `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultProvenanceService.java` | map 48; cache resolver 58–67; create/update 89–101, 142–154; read 114–131; delete/list/count/clear 165–235 | `provenanceStore` is a `ConcurrentHashMap<String,Provenance>`. It is populated before remote writes, populated from remote reads, and returned when the remote cache is absent or fails. | **Forbidden Mneme fallback.** Provenance is cached operational/domain state, not local-only metadata. | Remove `provenanceStore` and local branches; make `provenance-cache` availability/operation failures visible. Add focused failure/recovery tests alongside the MLLP service tests. |
| `TaskCacheService` — `energeia/erga/src/main/java/net/fhirfactory/harmonia/praxis/cache/TaskCacheService.java` | maps 60–61; remote resolvers 83–103; task read 105–122; task write 217–232; provenance write 253–268; provenance read 284–301; delete/clear/count 303–429 | `localFallbackCache` and `localFallbackProvenanceCache` store JSON strings. `putTaskJson`/`putProvenanceJson` explicitly write local state if RemoteCache is null or throws; reads return local values; delete/clear operate on local state; `count()` returns only local task-map size. | **Forbidden Mneme fallback**, for both `task-cache` and `provenance-cache`. The class comment explicitly describes “local fallback storage.” | Remove both maps and fallback branches. Make `put*`, `get*`, delete, clear, and count remote-only. Existing `energeia/ponos/.../TaskCacheServiceTest.java` currently constructs the service without a RemoteCacheManager and therefore tests the fallback; it must be changed to test configured RemoteCache success and explicit unavailability. |
| `PragmaCacheService` — `energeia/praxis/src/main/java/net/fhirfactory/harmonia/praxis/cache/PragmaCacheService.java` | map 59; init 71–87; save 96–125; get 134–161; delete 180–200; clear 217–224 | `localFallbackCache` stores copied `Pragma` objects. Startup explicitly logs “using local fallback store”; save writes local state before remote; get falls back to it and then `TaskCacheService`; delete/clear remove it. The remote field is cached once during `init()`. | **Forbidden Mneme fallback.** This is active Pragma workflow state. Delegating to `TaskCacheService` does not make the local map legitimate. | Remove the map, local branches, and fallback-specific init messages. Decide explicitly whether the existing `TaskCacheService` delegation remains as a separate cache path; do not silently treat it as a local substitute. Add remote-success/unavailable/no-local-write tests. |
| `PraxisService` — `energeia/praxis/src/main/java/net/fhirfactory/harmonia/praxis/service/PraxisService.java` | map 53; resolver 90–99; save 137–164; get 175–195; list 198–245; delete 269–289; clear 296–306 | `localFallbackCache` stores serialized `PraxisDefinition` objects. Save always writes it, then tries remote; remote failure/unavailability is logged as successful local persistence. Reads/list merge local entries; delete/clear remove local entries too. | **Forbidden Mneme fallback.** `tasksequence-cache` is the distributed persisted workflow-definition cache. | Remove the map and all merge/fallback branches. Make save/get/list/delete/clear depend on the configured RemoteCache. Existing `energeia/praxis/.../PraxisServiceTest.java` is currently a no-manager local-fallback CRUD test and is the primary test to refactor. |
| `MessageQueueService` — `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/service/MessageQueueService.java` | map 52; resolver 99–108; save 134–157; get 160–184; delete 186–203; list/seed 206–308 | `localFallbackCache` stores serialized `PetasosQueueDefinition` objects. Save writes local state, remote reads repopulate it, and unavailable/failed remote access still returns local definitions. `getAll()` merges local state and seeds defaults through `save()`, which can seed only the local map when Mneme is unavailable. | **Forbidden Mneme fallback** for queue-definition state. This is not Petasos API/Artemis message fallback; it is Mneme-backed configuration/state. | Remove local map and fallback branches, then handle default queue seeding only through the configured RemoteCache. Refactor `energeia/ponos/.../MessageQueueServiceTest.java`, whose setup intentionally passes `null` RemoteCacheManager and currently proves local fallback behavior. |
| `TaskSequenceCacheService` — `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/TaskSequenceCacheService.java` | map 50; default initialization 58–66; resolver 104–112; list 131–177; get 191–208; save 229–266; delete 269–288 | `localFallbackCache` stores serialized `PraxisDefinition` sequence JSON. Startup failure invokes `seedDefaultSequences()` and logs “Seeding local fallback”; list/get read local values; save writes local state whether remote is available or not. | **Forbidden Mneme fallback.** It is a BEFE-side duplicate of the `tasksequence-cache` workflow-definition cache. | Remove the map and fallback-seeding path. Make sequence initialization fail/skip explicitly when Mneme is unavailable rather than silently creating local sequence state. Add focused success/failure/recovery tests for the BEFE service. |

**Module status: retain for now as operational local state**

The following three concrete implementations were inspected because they contain the same pattern:

- `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/service/ModuleStatusService.java`: map 43; update 63–79; get 112–132; aggregate 134–156.
- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/ModuleStatusService.java`: map 43; update 63–86; get 118–138; aggregate 140–162.
- `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultModuleStatusService.java`: map 42; update 62–80; get 115–136; aggregate 138–161.

Each has `localFallbackCache`, remote-first reads, and local fallback when `modulestatus-cache` is unavailable. However, the stored value is a module’s transient readiness/operational status, not Pragma, Task, FHIR resource, workflow-definition, or queue-definition working state. Consistent with the task instruction to inspect but not remove `ModuleStatusService` merely for consistency, this appears to be a **legitimate process-local operational snapshot** for this step, with an important caveat: `waitForModuleReady()` can observe stale local readiness while Mneme is unavailable. Existing focused tests are:

- `energeia/ponos/src/test/java/net/fhirfactory/harmonia/praxis/service/ModuleStatusServiceTest.java`
- `pylai/pylai-mllp-base/src/test/java/net/fhirfactory/harmonia/mllpgateway/service/ModuleStatusServiceTest.java`

No change is recommended here without a separate decision that cluster readiness must fail closed rather than use transient local telemetry.

**Lazy Hot Rod / RemoteCache producer path**

Three relevant producers have the same behavior:

- `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/config/HotRodClientProducer.java:39–76`
- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/HotRodClientProducer.java:39–76`
- `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/HotRodClientProducer.java:39–76`

They first construct `new RemoteCacheManager(..., true)`; if eager startup fails, they construct `new RemoteCacheManager(..., false)` at lines 68–74. Candidate services then test `remoteCacheManager != null && remoteCacheManager.isStarted()` and return `null` from their cache resolver. That `null` path activates the local maps described above. The producer’s lazy startup can remain for this step: once service fallback branches are removed, required operations will fail visibly instead of switching to JVM-local Mneme state. No aggressive startup coupling appears necessary.

**Deliberately non-candidate local collections observed**

- `pylai/pylai-mllp-base/.../config/MllpDestinationRegistry.java:42` — `destinations` is an in-process registry of configured outbound endpoint objects; it is not a RemoteCache substitute.
- `energeia/ponos/.../conduit/PragmaWorkflowDispatcher.java:81` — `registeredWorkflows` is a local implementation registry used to resolve already-registered workflow implementations; it is not serialized Mneme state.
- Temporary `LinkedHashMap` aggregation variables in list/search methods are per-operation computation state, not persistent local fallback stores.

**Summary for the implementing step**

- Exact fallback fields identified: `localFallbackCaches`, `taskStore`, `provenanceStore`, `localFallbackCache` in `TaskCacheService`, `PragmaCacheService`, `PraxisService`, `MessageQueueService`, `TaskSequenceCacheService`, and the three module-status implementations; plus `localFallbackProvenanceCache` in `TaskCacheService`.
- Exact fallback branch families identified: remote resolver returns `null`; remote read/write/list/delete catches log and then use local state; startup/default-seeding branches that explicitly select local storage.
- Expected post-change failure semantics: missing/unstarted manager, missing named cache, or RemoteCache operation exceptions must propagate using existing runtime/Infinispan exceptions rather than return apparent success or local data. No new exception framework is needed.
- No direct Mnemosyne read/write path was introduced or inspected as a replacement.
- No Task 08 final Clinical write path, Task 09 cache-aside reads, or Task 10 Clinical search redesign was implemented.
- Petasos, Mnemosyne, Kleio, and Themis architectures were not redesigned.
- Tests were not run because this was explicitly read-only exploration; no files were modified.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Feedback**  
Use the attached Step 02 report plus the current exploration findings for Step 03.

Important planning focus:
- This is Task 07 Step 03 only: remove production process-local fallbacks that substitute for Mneme/RemoteCache semantics.
- Start from the confirmed candidate inventory and line ranges already identified for FhirCacheService, DefaultTaskService, DefaultProvenanceService, TaskCacheService, PragmaCacheService, PraxisService, MessageQueueService, TaskSequenceCacheService, and ModuleStatusService.
- Preserve scope discipline: inspect each candidate, remove fallback only where the local map substitutes for Mneme, retain legitimate process-local state with justification.
- Keep Task 08/09/10 behavior out of scope; do not redesign Petasos, Mnemosyne, Kleio, Themis, or the final Clinical write path.
- Prefer a practical phased plan that groups coherent changes, includes focused tests per affected service, and covers the final reporting requirements from the issue.
- Account for the lazy Hot Rod producer path already observed: services currently activate local fallback when the producer leaves an unstarted manager or missing cache; the plan should prefer explicit operation failure over startup redesign unless startup coupling is truly required.
- Include verification commands per step and acceptance criteria that prove Mneme-available success, Mneme-unavailable explicit failure, no local fallback reads/writes, practical recovery, and retained legitimate local-state behavior where applicable.
- Consider test runtime optimization guidance: targeted module tests first, then broader verification, short timeouts, and reusable knowledge for later steps.



**Requirements**

**Goal & Outcome**  
Enforce the working-state cache boundary established in ADR-019 by eliminating process-local in-memory fallbacks (`ConcurrentHashMap` stores) that substitute for Mneme/Infinispan distributed working-state semantics. When Mneme is unavailable, operations must fail visibly and explicitly rather than silently degrading into local JVM memory.

**Scope**
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

**Done When**
- All 8 identified fallback `ConcurrentHashMap` fields and their associated read, write, delete, search, and seeding fallback branches are deleted.
- Missing RemoteCacheManager, unstarted Hot Rod client, or missing named cache instances cause explicit runtime failures rather than fallback persistence.
- `ModuleStatusService` in all three modules is verified and documented as legitimate process-local operational state.
- Focused unit and service tests in `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass with mocked and configured cache fixtures.
- Full architecture suite (`*ArchitectureTest`) in `paradeigma-test` passes without regression.

**Technical Design**

**Decisions**
- **Decision: Throw explicit runtime/cache exceptions on missing or failed RemoteCache / not catching and writing to local maps**
    - *Rationale*: ADR-019 dictates that loss of Mneme distributed working state must be visible as loss of capability. Swallowing cache failures or returning JVM-local data creates split-brain state across cluster nodes. Standard exceptions (such as `IllegalStateException` for uninitialized/unavailable cache or propagating `CacheException`) communicate failure cleanly without custom framework bloat.
- **Decision: Retain lazy Hot Rod client producer initialization / not enforcing eager startup crash**
    - *Rationale*: `HotRodClientProducer` creates `new RemoteCacheManager(..., false)` if the cluster is unreachable during bootstrap. Retaining lazy startup allows processes to start in constrained environments while ensuring individual operations fail explicitly when invoking cache calls without an active connection.
- **Decision: Retain `localFallbackCache` in `ModuleStatusService` / not converting to fail-closed distributed lock**
    - *Rationale*: `ModuleStatusService` tracks transient local telemetry and readiness snapshots. It is not workflow, task, or clinical working state. Retaining its local snapshot satisfies the requirement to avoid removing legitimate process-local state.

**Approach & Touches**
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

**Nuances / Risks / Corners**
- **Test Fixture Mocks**: Existing service tests (`PraxisServiceTest`, `TaskCacheServiceTest`, `MessageQueueServiceTest`, `FhirCacheServiceTest`, `TaskServiceTest`, `ProvenanceServiceTest`) constructed services with no arguments (`null` `RemoteCacheManager`) and asserted on the local fallback behavior. Every test must be updated to provide a mocked `RemoteCacheManager` returning a mocked `RemoteCache` for standard positive cases, plus explicit tests for uninitialized/disconnected manager scenarios.
- **Null Safety vs Explicit Failure**: Valid domain misses (e.g. key not found in cache) must return `null` / `Optional.empty()` normally, whereas infrastructure failure (RemoteCache unavailable or Hot Rod communication exception) must throw an explicit exception.
- **Default Seeding Paths**: Methods like `MessageQueueService.seedDefaultQueues()` and `TaskSequenceCacheService.ensureDefaultSequences()` must interact with the remote cache or fail gracefully without populating a hidden in-memory store.

**Testing**

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

**Assumptions & Open Questions**

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

**Delivery Steps**

**Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE**  
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

**Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base**  
Goal: Remove process-local fallback stores from `DefaultTaskService` and `DefaultProvenanceService`, requiring active RemoteCache for Task and Provenance state.  
Scope: `pylai/pylai-mllp-base` (`DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskServiceTest.java`, `ProvenanceServiceTest.java`).  
Acceptance Criteria:
- [ ] `taskStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultTaskService.java`.
- [ ] `provenanceStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultProvenanceService.java`.
- [ ] `DefaultTaskService` and `DefaultProvenanceService` throw explicit runtime exceptions on create, update, delete, search, count, or clear when RemoteCache is unavailable.
- [ ] `DefaultModuleStatusService.java` in `pylai-mllp-base` verified and documented as retained process-local state.
- [ ] `TaskServiceTest` and `ProvenanceServiceTest` refactored to use mocked RemoteCacheManager/RemoteCache, verifying remote success, explicit failure when unavailable, and absence of local fallback state.
  Verification: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia**  
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

**Step 4: Full Test Suite, Architecture Invariant Verification, and Final Report**  
Goal: Execute full test verification across all affected modules, validate ArchUnit architectural guardrails, and compile the final Step 03 report.  
Scope: Full repository test verification (`iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, `energeia-ponos`, `paradeigma-test`).  
Acceptance Criteria:
- [ ] All unit and service test suites across `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass without error.
- [ ] ArchUnit suite in `paradeigma/paradeigma-test` passes without regression (`*ArchitectureTest`).
- [ ] Final report compiled covering all 12 items specified in the parent task (candidate inspection, fallback fields removed, fallback branches removed, post-change failure semantics, retained local maps, lazy Hot Rod behavior assessment, tests added/modified, and guardrail confirmations).
- [ ] Confirmed zero direct Mnemosyne read/write paths introduced, zero Task 08/09/10 leakage, and zero architecture redesign of Petasos, Mnemosyne, Kleio, or Themis.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

REVIEW THE PLAN

**Task description**  
Review the current delivery plan.

**Feedback**  
Focus on whether the plan is the smallest coherent path for Task 07 Step 03.

Review especially:
- whether the step grouping is practical and sequential given module boundaries;
- whether the plan preserves the issue's narrow scope and avoids drifting into Task 08/09/10 or architectural redesign;
- whether tests and verification are sufficient to prove removal of local Mneme fallbacks, explicit failure on Mneme unavailability, no stale local fallback reads/writes, and retained legitimate process-local state for ModuleStatusService;
- whether any important candidate or report requirement from the issue is missing.

**Constraints**  
Keep review scoped to Task 07 Step 03 only; do not drift into broader redesign.



Plan Approved

**Verification**
- **File & field grounding confirmed**: `MessageQueueService` (`energeia/ponos`) has `localFallbackCache` at line 52 and `QUEUE_CACHE_NAME = "messagequeue-cache"` at line 43 — matches the plan's `messagequeue-cache` reference and fallback branches (save/get/delete/list all write local state before/around remote). Verified accurate.
- `TaskCacheService` confirmed in `energeia/erga` with `TASK_CACHE_NAME="task-cache"`, `PROVENANCE_CACHE_NAME="provenance-cache"`, and both `localFallbackCache` + `localFallbackProvenanceCache` fields plus the `else { localFallbackCache.put(...) }` fallback branches. Matches plan.
- `PragmaCacheService` (`energeia/praxis`) confirmed: `PRAGMA_CACHE_NAME="task-cache"`, `localFallbackCache`, plus init-time "using local fallback store" logging — matches plan's removal targets.
- **Test file locations verified**: `FhirCacheServiceTest`, `ProvenanceServiceTest`, `TaskServiceTest`, `ModuleStatusServiceTest` (pylai), `TaskCacheServiceTest` (correctly in `energeia/ponos` though source is in erga), `MessageQueueServiceTest`, `PraxisServiceTest`, `ModuleStatusServiceTest` (ponos) all exist. `TaskSequenceCacheServiceTest` and `PragmaCacheServiceTest` do not exist — plan correctly labels them as new/added.
- **Test constructor claim verified**: `TaskCacheServiceTest` uses no-arg `new TaskCacheService()` (null manager) — confirms plan's premise that existing tests currently exercise the fallback and must be refactored.
- **Scope discipline**: Plan explicitly excludes Task 08/09/10, degraded read modes, BEFE→Mnemosyne paths, and new cache abstractions; retains lazy Hot Rod producer and `ModuleStatusService` local state with documented rationale — all consistent with the issue's narrow scope.
- **Verification commands**: Each step uses real `mvn test -pl ... -am -Dtest="..." -Dsurefire.failIfNoSpecifiedTests=false` targeting the (post-step) tests, plus the guardrail `*ArchitectureTest` suite in Step 4 — matches AGENTS.md tooling.
- **Test coverage**: All 5 issue-required scenarios (Mneme-available success, unavailable explicit failure, no local read/write, recovery, retained legitimate local state) are present in the Testing section and step acceptance criteria. The 12-item report requirement is captured in Step 4.

**Issues**  
None blocking.

**Notes**
- **PragmaCacheService recovery corner (non-blocking)**: `PragmaCacheService` resolves `this.pragmaCache` exactly once during `init()` (line 75). If Mneme is down at startup, the cached reference stays null even after recovery. The plan's generic "Service Recovery on Cache Reconnection" scenario may not cleanly apply to this service without resolving the cache per-operation (as the other services do via `getRemoteCache()`). The executor should resolve the cache lazily/per-call rather than once at init, or the recovery test for PragmaCacheService will not be meaningful. Worth flagging to the executor.
- **PragmaCacheService→TaskCacheService delegation (non-blocking)**: Step 01 research flagged an explicit decision about whether the `taskCacheService` delegation path (both use `task-cache`) remains after fallback removal. The plan removes `localFallbackCache` and routes through RemoteCache but does not explicitly state the fate of the `taskCacheService.get(...)` delegation. Minor; executor can decide, but the plan could have named it.
- **iris-befe ModuleStatusService** has no existing test; since it is retained unchanged, Step 1 documents it without a test run — acceptable.

**Discoveries for future steps**
- `TaskCacheService` source lives in `energeia/erga` but its only test (`TaskCacheServiceTest`) is in `energeia/ponos`; Step 3's `-pl energeia/praxis,energeia/ponos -am` builds erga transitively via `-am`, so erga source edits are compiled/verified even though erga is not in `-pl`.
- Cache name constants: `task-cache`, `provenance-cache`, `messagequeue-cache`; `PragmaCacheService` deliberately reuses `task-cache`.



**Requirements**

**Goal & Outcome**  
Enforce the working-state cache boundary established in ADR-019 by eliminating process-local in-memory fallbacks (`ConcurrentHashMap` stores) that substitute for Mneme/Infinispan distributed working-state semantics. When Mneme is unavailable, operations must fail visibly and explicitly rather than silently degrading into local JVM memory.

**Scope**
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

**Done When**
- All 8 identified fallback `ConcurrentHashMap` fields and their associated read, write, delete, search, and seeding fallback branches are deleted.
- Missing RemoteCacheManager, unstarted Hot Rod client, or missing named cache instances cause explicit runtime failures rather than fallback persistence.
- `ModuleStatusService` in all three modules is verified and documented as legitimate process-local operational state.
- Focused unit and service tests in `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass with mocked and configured cache fixtures.
- Full architecture suite (`*ArchitectureTest`) in `paradeigma-test` passes without regression.

**Technical Design**

**Decisions**
- **Decision: Throw explicit runtime/cache exceptions on missing or failed RemoteCache / not catching and writing to local maps**
    - *Rationale*: ADR-019 dictates that loss of Mneme distributed working state must be visible as loss of capability. Swallowing cache failures or returning JVM-local data creates split-brain state across cluster nodes. Standard exceptions (such as `IllegalStateException` for uninitialized/unavailable cache or propagating `CacheException`) communicate failure cleanly without custom framework bloat.
- **Decision: Retain lazy Hot Rod client producer initialization / not enforcing eager startup crash**
    - *Rationale*: `HotRodClientProducer` creates `new RemoteCacheManager(..., false)` if the cluster is unreachable during bootstrap. Retaining lazy startup allows processes to start in constrained environments while ensuring individual operations fail explicitly when invoking cache calls without an active connection.
- **Decision: Retain `localFallbackCache` in `ModuleStatusService` / not converting to fail-closed distributed lock**
    - *Rationale*: `ModuleStatusService` tracks transient local telemetry and readiness snapshots. It is not workflow, task, or clinical working state. Retaining its local snapshot satisfies the requirement to avoid removing legitimate process-local state.

**Approach & Touches**
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

**Nuances / Risks / Corners**
- **Test Fixture Mocks**: Existing service tests (`PraxisServiceTest`, `TaskCacheServiceTest`, `MessageQueueServiceTest`, `FhirCacheServiceTest`, `TaskServiceTest`, `ProvenanceServiceTest`) constructed services with no arguments (`null` `RemoteCacheManager`) and asserted on the local fallback behavior. Every test must be updated to provide a mocked `RemoteCacheManager` returning a mocked `RemoteCache` for standard positive cases, plus explicit tests for uninitialized/disconnected manager scenarios.
- **Null Safety vs Explicit Failure**: Valid domain misses (e.g. key not found in cache) must return `null` / `Optional.empty()` normally, whereas infrastructure failure (RemoteCache unavailable or Hot Rod communication exception) must throw an explicit exception.
- **Default Seeding Paths**: Methods like `MessageQueueService.seedDefaultQueues()` and `TaskSequenceCacheService.ensureDefaultSequences()` must interact with the remote cache or fail gracefully without populating a hidden in-memory store.

**Testing**

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

**Assumptions & Open Questions**

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

**Delivery Steps**

*** Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE**  
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

**Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base**  
Goal: Remove process-local fallback stores from `DefaultTaskService` and `DefaultProvenanceService`, requiring active RemoteCache for Task and Provenance state.  
Scope: `pylai/pylai-mllp-base` (`DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskServiceTest.java`, `ProvenanceServiceTest.java`).  
Acceptance Criteria:
- [ ] `taskStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultTaskService.java`.
- [ ] `provenanceStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultProvenanceService.java`.
- [ ] `DefaultTaskService` and `DefaultProvenanceService` throw explicit runtime exceptions on create, update, delete, search, count, or clear when RemoteCache is unavailable.
- [ ] `DefaultModuleStatusService.java` in `pylai-mllp-base` verified and documented as retained process-local state.
- [ ] `TaskServiceTest` and `ProvenanceServiceTest` refactored to use mocked RemoteCacheManager/RemoteCache, verifying remote success, explicit failure when unavailable, and absence of local fallback state.
  Verification: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia**  
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

**Step 4: Full Test Suite, Architecture Invariant Verification, and Final Report**  
Goal: Execute full test verification across all affected modules, validate ArchUnit architectural guardrails, and compile the final Step 03 report.  
Scope: Full repository test verification (`iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, `energeia-ponos`, `paradeigma-test`).  
Acceptance Criteria:
- [ ] All unit and service test suites across `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass without error.
- [ ] ArchUnit suite in `paradeigma/paradeigma-test` passes without regression (`*ArchitectureTest`).
- [ ] Final report compiled covering all 12 items specified in the parent task (candidate inspection, fallback fields removed, fallback branches removed, post-change failure semantics, retained local maps, lazy Hot Rod behavior assessment, tests added/modified, and guardrail confirmations).
- [ ] Confirmed zero direct Mnemosyne read/write paths introduced, zero Task 08/09/10 leakage, and zero architecture redesign of Petasos, Mnemosyne, Kleio, or Themis.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Implement the iris-befe portion of Task 07 Step 03.

Confirmed current state from exploration:
- `FhirCacheService` uses `localFallbackCaches` as a forbidden Mneme substitute. Relevant areas: field around line 53; remote lookup around 105–113; local fallback branches in get 116–128, put 138–152, delete 190–202, search 216–234.
- `TaskSequenceCacheService` uses `localFallbackCache` as a forbidden Mneme substitute. Relevant areas: field around line 50; default initialization around 58–66; resolver around 104–112; local fallback/list/get/save/delete behavior around 131–177, 191–208, 229–266, 269–288.
- `iris-befe` `ModuleStatusService` appears to be legitimate transient operational local state and should be left unchanged; just keep that understanding in mind.
- Hot Rod producers can remain lazy for this step; service operations must fail explicitly rather than fall back to JVM-local state.

Required outcomes:
- Remove forbidden process-local fallback fields and branches from both services.
- When the required RemoteCache / Hot Rod capability is unavailable, fail explicitly using the smallest appropriate existing runtime/Infinispan exception style already natural to the module. Do not add a new framework/abstraction.
- Preserve normal not-found semantics for valid cache misses, but infrastructure unavailability must not appear as success.
- For `TaskSequenceCacheService`, eliminate fallback seeding/local initialization behavior; avoid silently creating local default sequence state.
- Add or refactor focused tests to prove:
    1. Mneme available -> configured RemoteCache is used normally.
    2. Mneme unavailable -> operation fails explicitly.
    3. Mneme unavailable -> no process-local fallback read/write path remains.
    4. Recovery where practical -> once cache becomes available again, service can use it normally without stale local state.
- Keep the change narrow to iris-befe; do not drift into Task 08/09/10, direct Mnemosyne paths, or broader redesign.

Execution optimization:
- Start with the narrowest relevant iris-befe test command(s) for `FhirCacheServiceTest` and `TaskSequenceCacheServiceTest`.
- Use short timeouts first (60–120s) and only widen if genuinely necessary.
- Report exact commands run and outcomes in Discoveries.



Code is Ready

**Summary of Completed Work (Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE)**

**1. Fallback Removal in `FhirCacheService.java`**
- Removed forbidden `localFallbackCaches` process-local map (`Map<String, Map<String, String>>`).
- Eliminated all local fallback read, write, delete, and search branches.
- Implemented `requireRemoteCache(String cacheName)` helper throwing `IllegalStateException("Mneme cache [" + cacheName + "] is unavailable")` when `RemoteCacheManager` is null, unstarted, or unable to resolve the named cache.
- Ensured normal not-found semantics (returning `null`) for valid cache misses on active caches.

**2. Fallback Removal in `TaskSequenceCacheService.java`**
- Removed forbidden `localFallbackCache` map (`Map<String, String>`).
- Eliminated fallback seeding in `getAllSequenceJsons()` and fallback persistence in `saveSequence()` and `deleteSequence()`.
- Implemented `requireRemoteCache()` helper enforcing explicit `IllegalStateException` on write, search, count, or retrieve operations when `tasksequence-cache` is unavailable.
- Configured `ensureDefaultSequences()` to interact with `RemoteCache` when available and skip seeding gracefully during uninitialized bootstrap without creating local map fallback state.

**3. Verification of `ModuleStatusService.java` in `iris-befe`**
- Inspected and verified `ModuleStatusService` as legitimate process-local operational status and readiness telemetry snapshot. Retained unchanged.

**4. Test Suite Refactoring & Additions**
- **`FhirCacheServiceTest.java`**: Refactored to test against mocked `RemoteCacheManager`/`RemoteCache`. Added tests for normal operations (save, retrieve, delete, search, bundle), valid cache miss (`null`), explicit `IllegalStateException` on null/unstarted manager or null named cache, remote exception propagation (`HotRodClientException`), and clean reconnection recovery without stale local state (12 tests passing).
- **`TaskSequenceCacheServiceTest.java`**: Added comprehensive test suite verifying remote CRUD, listing, counting, default sequence seeding when empty, skip when populated/unavailable, explicit `IllegalStateException` on unstarted/missing cache, transport exception propagation, and recovery upon reconnect (13 tests passing).
- **`FhirRestResourceIntegrationTest.java`**, **`OperationsServerManagerTest.java`**, **`OperationsServerManagerIntegrationTest.java`**: Updated mock fixtures to supply mocked `RemoteCacheManager` backing `FhirCacheService` and `TaskSequenceCacheService`.

---

**Discoveries for future steps**
- **Execution commands & speed**: Use `mvn test -pl <module> -am -Dtest="<TestClass>" -Dsurefire.failIfNoSpecifiedTests=false` to execute focused tests quickly (~1.5–3.5s) while satisfying Maven reactor inter-module dependencies.
- **Infinispan Mocking**: `RemoteCache.values()` and `RemoteCache.keySet()` return `CloseableIteratorCollection` and `CloseableIteratorSet`; mock iterators using a small `toCloseableIterator(Iterator<T>)` helper to avoid `ClassCastException` with standard `java.util.Iterator`.
- **Exception handling**: Avoid wrapping `saveSequence` within generic JSON serialization try-catch blocks so `IllegalStateException` propagates directly rather than being masked as `RuntimeException("Serialization error: ...")`.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Review only the iris-befe scope for Task 07 Step 03.

Focus on:
- whether `FhirCacheService` no longer keeps or uses any process-local Mneme fallback state;
- whether `TaskSequenceCacheService` no longer keeps or uses any process-local Mneme fallback state or local default-seeding substitute;
- whether missing/unstarted RemoteCache / Hot Rod capability now fails explicitly rather than appearing successful;
- whether valid cache-miss semantics remain intact;
- whether the added/refactored tests truthfully prove remote success, explicit failure on unavailability, no local fallback path, and practical recovery;
- whether `ModuleStatusService` was correctly left untouched;
- any unintended regressions or scope drift beyond iris-befe.

Also verify the claimed test execution/results if observable.



Implementation Approved

**Verification**
- **`FhirCacheService.java`**: `localFallbackCaches` map fully removed. `getResourceJson`, `putResourceJson`, `deleteResource`, and `searchResources` now route exclusively through a `requireRemoteCache(cacheName)` helper that throws `IllegalStateException("Mneme cache [...] is unavailable")` when the manager is null, unstarted, or the named cache resolves to null. Valid cache misses still return `null` (via `remoteCache.get`). Transport exceptions (`HotRodClientException`) propagate naturally.
- **`TaskSequenceCacheService.java`**: `localFallbackCache` map removed. `ensureDefaultSequences()` now checks the real remote cache's `isEmpty()` and gracefully skips seeding when the cache is unavailable (no local seeding). `saveSequence`, `getSequenceJson`, `getAllSequenceJsons`, `deleteSequence`, and `count` all go through `requireRemoteCache()` and fail explicitly when unavailable. Cache misses correctly return `Optional.empty()`.
- **`ModuleStatusService.java` (iris-befe)**: Confirmed untouched — retains its `localFallbackCache` as legitimate process-local telemetry, per the plan's explicit decision.
- **Tests**: `FhirCacheServiceTest` (12) and `TaskSequenceCacheServiceTest` (13) genuinely verify remote CRUD success against a mocked `RemoteCacheManager`/`RemoteCache`, explicit `IllegalStateException` on null/unstarted manager and null named cache, `HotRodClientException` propagation, valid cache-miss semantics, default-seeding when empty/skip when populated/skip when unavailable, and recovery-on-reconnect proving no stale local state. Mocks are backed by real `ConcurrentHashMap` stores so behavior (not just interactions) is asserted.
- **Verification command** `mvn test -pl iris/iris-befe -am -Dtest="FhirCacheServiceTest,TaskSequenceCacheServiceTest" ...` → BUILD SUCCESS, 25 tests, 0 failures (~1.8s).
- **Full module regression**: `mvn test -pl iris/iris-befe` → 173 tests, 0 failures, including the executor-modified `FhirRestResourceIntegrationTest`, `OperationsServerManagerTest`, and `OperationsServerManagerIntegrationTest` fixtures. No regressions.
- **Scope**: Changes confined to iris-befe; no Mnemosyne/Task 08/09/10 drift. (`AuditEventResource`/`ThemisClinicalAuthorizationFilter` diffs in the working tree belong to unrelated pre-existing Task 06 work, not this step's services.)

**Issues**  
None blocking.

**Notes**
- Unused `import java.util.concurrent.ConcurrentHashMap;` now remains in both `FhirCacheService.java` (line 38) and `TaskSequenceCacheService.java` (line 35) after fallback removal. Purely cosmetic — compiles and passes cleanly.
- `FhirCacheService.searchResources` now calls `.stream()` directly on the `CloseableIteratorCollection` returned by `remoteCache.values()` without explicitly closing it. This is a minor theoretical resource-leak concern against real Infinispan; not exercised by mocks and not a regression from prior behavior. Non-blocking.
- Out-of-scope observation: `OperationsAggregatorService.java` (iris-befe) holds a `localPragmaStore` ConcurrentHashMap, but it is not among the plan's 8 identified fallback fields and is outside this step's scope — flagged only for awareness.
- The `-am` full build fails on `kleio`'s `DurableAuditServicePostgreSqlConcurrencyTest` due to no Docker/Testcontainers environment — an environmental issue unrelated to this step.

**Discoveries for future steps**
- To run iris-befe module tests in isolation, first `mvn install -pl iris/iris-befe -am -DskipTests -q` (installs dependency jars), then `mvn test -pl iris/iris-befe` (~a few seconds, avoids the Docker-dependent kleio test). Running `-am test` directly triggers `kleio`'s Testcontainers test which fails without Docker.
- Infinispan `RemoteCache.values()`/`keySet()` return `CloseableIteratorCollection`/`CloseableIteratorSet`; mocking requires a `toCloseableIterator` helper (already established in these tests) to avoid `ClassCastException`.



**Requirements**

**Goal & Outcome**  
Enforce the working-state cache boundary established in ADR-019 by eliminating process-local in-memory fallbacks (`ConcurrentHashMap` stores) that substitute for Mneme/Infinispan distributed working-state semantics. When Mneme is unavailable, operations must fail visibly and explicitly rather than silently degrading into local JVM memory.

**Scope**
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

**Done When**
- All 8 identified fallback `ConcurrentHashMap` fields and their associated read, write, delete, search, and seeding fallback branches are deleted.
- Missing RemoteCacheManager, unstarted Hot Rod client, or missing named cache instances cause explicit runtime failures rather than fallback persistence.
- `ModuleStatusService` in all three modules is verified and documented as legitimate process-local operational state.
- Focused unit and service tests in `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass with mocked and configured cache fixtures.
- Full architecture suite (`*ArchitectureTest`) in `paradeigma-test` passes without regression.

**Technical Design**

**Decisions**
- **Decision: Throw explicit runtime/cache exceptions on missing or failed RemoteCache / not catching and writing to local maps**
    - *Rationale*: ADR-019 dictates that loss of Mneme distributed working state must be visible as loss of capability. Swallowing cache failures or returning JVM-local data creates split-brain state across cluster nodes. Standard exceptions (such as `IllegalStateException` for uninitialized/unavailable cache or propagating `CacheException`) communicate failure cleanly without custom framework bloat.
- **Decision: Retain lazy Hot Rod client producer initialization / not enforcing eager startup crash**
    - *Rationale*: `HotRodClientProducer` creates `new RemoteCacheManager(..., false)` if the cluster is unreachable during bootstrap. Retaining lazy startup allows processes to start in constrained environments while ensuring individual operations fail explicitly when invoking cache calls without an active connection.
- **Decision: Retain `localFallbackCache` in `ModuleStatusService` / not converting to fail-closed distributed lock**
    - *Rationale*: `ModuleStatusService` tracks transient local telemetry and readiness snapshots. It is not workflow, task, or clinical working state. Retaining its local snapshot satisfies the requirement to avoid removing legitimate process-local state.

**Approach & Touches**
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

**Nuances / Risks / Corners**
- **Test Fixture Mocks**: Existing service tests (`PraxisServiceTest`, `TaskCacheServiceTest`, `MessageQueueServiceTest`, `FhirCacheServiceTest`, `TaskServiceTest`, `ProvenanceServiceTest`) constructed services with no arguments (`null` `RemoteCacheManager`) and asserted on the local fallback behavior. Every test must be updated to provide a mocked `RemoteCacheManager` returning a mocked `RemoteCache` for standard positive cases, plus explicit tests for uninitialized/disconnected manager scenarios.
- **Null Safety vs Explicit Failure**: Valid domain misses (e.g. key not found in cache) must return `null` / `Optional.empty()` normally, whereas infrastructure failure (RemoteCache unavailable or Hot Rod communication exception) must throw an explicit exception.
- **Default Seeding Paths**: Methods like `MessageQueueService.seedDefaultQueues()` and `TaskSequenceCacheService.ensureDefaultSequences()` must interact with the remote cache or fail gracefully without populating a hidden in-memory store.

**Testing**

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

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE**  
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

**Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base**  
Goal: Remove process-local fallback stores from `DefaultTaskService` and `DefaultProvenanceService`, requiring active RemoteCache for Task and Provenance state.  
Scope: `pylai/pylai-mllp-base` (`DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskServiceTest.java`, `ProvenanceServiceTest.java`).  
Acceptance Criteria:
- [ ] `taskStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultTaskService.java`.
- [ ] `provenanceStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultProvenanceService.java`.
- [ ] `DefaultTaskService` and `DefaultProvenanceService` throw explicit runtime exceptions on create, update, delete, search, count, or clear when RemoteCache is unavailable.
- [ ] `DefaultModuleStatusService.java` in `pylai-mllp-base` verified and documented as retained process-local state.
- [ ] `TaskServiceTest` and `ProvenanceServiceTest` refactored to use mocked RemoteCacheManager/RemoteCache, verifying remote success, explicit failure when unavailable, and absence of local fallback state.
  Verification: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia**  
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

**Step 4: Full Test Suite, Architecture Invariant Verification, and Final Report**  
Goal: Execute full test verification across all affected modules, validate ArchUnit architectural guardrails, and compile the final Step 03 report.  
Scope: Full repository test verification (`iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, `energeia-ponos`, `paradeigma-test`).  
Acceptance Criteria:
- [ ] All unit and service test suites across `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass without error.
- [ ] ArchUnit suite in `paradeigma/paradeigma-test` passes without regression (`*ArchitectureTest`).
- [ ] Final report compiled covering all 12 items specified in the parent task (candidate inspection, fallback fields removed, fallback branches removed, post-change failure semantics, retained local maps, lazy Hot Rod behavior assessment, tests added/modified, and guardrail confirmations).
- [ ] Confirmed zero direct Mnemosyne read/write paths introduced, zero Task 08/09/10 leakage, and zero architecture redesign of Petasos, Mnemosyne, Kleio, or Themis.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Requirements**

**Goal & Outcome**  
Enforce the working-state cache boundary established in ADR-019 by eliminating process-local in-memory fallbacks (`ConcurrentHashMap` stores) that substitute for Mneme/Infinispan distributed working-state semantics. When Mneme is unavailable, operations must fail visibly and explicitly rather than silently degrading into local JVM memory.

**Scope**
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

**Done When**
- All 8 identified fallback `ConcurrentHashMap` fields and their associated read, write, delete, search, and seeding fallback branches are deleted.
- Missing RemoteCacheManager, unstarted Hot Rod client, or missing named cache instances cause explicit runtime failures rather than fallback persistence.
- `ModuleStatusService` in all three modules is verified and documented as legitimate process-local operational state.
- Focused unit and service tests in `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass with mocked and configured cache fixtures.
- Full architecture suite (`*ArchitectureTest`) in `paradeigma-test` passes without regression.

**Technical Design**

**Decisions**
- **Decision: Throw explicit runtime/cache exceptions on missing or failed RemoteCache / not catching and writing to local maps**
    - *Rationale*: ADR-019 dictates that loss of Mneme distributed working state must be visible as loss of capability. Swallowing cache failures or returning JVM-local data creates split-brain state across cluster nodes. Standard exceptions (such as `IllegalStateException` for uninitialized/unavailable cache or propagating `CacheException`) communicate failure cleanly without custom framework bloat.
- **Decision: Retain lazy Hot Rod client producer initialization / not enforcing eager startup crash**
    - *Rationale*: `HotRodClientProducer` creates `new RemoteCacheManager(..., false)` if the cluster is unreachable during bootstrap. Retaining lazy startup allows processes to start in constrained environments while ensuring individual operations fail explicitly when invoking cache calls without an active connection.
- **Decision: Retain `localFallbackCache` in `ModuleStatusService` / not converting to fail-closed distributed lock**
    - *Rationale*: `ModuleStatusService` tracks transient local telemetry and readiness snapshots. It is not workflow, task, or clinical working state. Retaining its local snapshot satisfies the requirement to avoid removing legitimate process-local state.

**Approach & Touches**
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

**Nuances / Risks / Corners**
- **Test Fixture Mocks**: Existing service tests (`PraxisServiceTest`, `TaskCacheServiceTest`, `MessageQueueServiceTest`, `FhirCacheServiceTest`, `TaskServiceTest`, `ProvenanceServiceTest`) constructed services with no arguments (`null` `RemoteCacheManager`) and asserted on the local fallback behavior. Every test must be updated to provide a mocked `RemoteCacheManager` returning a mocked `RemoteCache` for standard positive cases, plus explicit tests for uninitialized/disconnected manager scenarios.
- **Null Safety vs Explicit Failure**: Valid domain misses (e.g. key not found in cache) must return `null` / `Optional.empty()` normally, whereas infrastructure failure (RemoteCache unavailable or Hot Rod communication exception) must throw an explicit exception.
- **Default Seeding Paths**: Methods like `MessageQueueService.seedDefaultQueues()` and `TaskSequenceCacheService.ensureDefaultSequences()` must interact with the remote cache or fail gracefully without populating a hidden in-memory store.

**Testing**

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

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE**  
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

*** Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base**  
Goal: Remove process-local fallback stores from `DefaultTaskService` and `DefaultProvenanceService`, requiring active RemoteCache for Task and Provenance state.  
Scope: `pylai/pylai-mllp-base` (`DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskServiceTest.java`, `ProvenanceServiceTest.java`).  
Acceptance Criteria:
- [ ] `taskStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultTaskService.java`.
- [ ] `provenanceStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultProvenanceService.java`.
- [ ] `DefaultTaskService` and `DefaultProvenanceService` throw explicit runtime exceptions on create, update, delete, search, count, or clear when RemoteCache is unavailable.
- [ ] `DefaultModuleStatusService.java` in `pylai-mllp-base` verified and documented as retained process-local state.
- [ ] `TaskServiceTest` and `ProvenanceServiceTest` refactored to use mocked RemoteCacheManager/RemoteCache, verifying remote success, explicit failure when unavailable, and absence of local fallback state.
  Verification: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia**  
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

**Step 4: Full Test Suite, Architecture Invariant Verification, and Final Report**  
Goal: Execute full test verification across all affected modules, validate ArchUnit architectural guardrails, and compile the final Step 03 report.  
Scope: Full repository test verification (`iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, `energeia-ponos`, `paradeigma-test`).  
Acceptance Criteria:
- [ ] All unit and service test suites across `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass without error.
- [ ] ArchUnit suite in `paradeigma/paradeigma-test` passes without regression (`*ArchitectureTest`).
- [ ] Final report compiled covering all 12 items specified in the parent task (candidate inspection, fallback fields removed, fallback branches removed, post-change failure semantics, retained local maps, lazy Hot Rod behavior assessment, tests added/modified, and guardrail confirmations).
- [ ] Confirmed zero direct Mnemosyne read/write paths introduced, zero Task 08/09/10 leakage, and zero architecture redesign of Petasos, Mnemosyne, Kleio, or Themis.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



IMPLEMENTATION

**Task description**  
Fully complete step 2.

**Feedback**  
Implement the pylai-mllp-base portion of Task 07 Step 03.

Confirmed current state from exploration:
- `DefaultTaskService` uses `taskStore` as a forbidden Mneme substitute. Relevant areas: field around line 55; cache resolver 65–74; create/update around 98–110 and 153–165; read around 123–140; delete around 176–189; list/count/clear around 192–247.
- `DefaultProvenanceService` uses `provenanceStore` as a forbidden Mneme substitute. Relevant areas: field around line 48; cache resolver 58–67; create/update around 89–101 and 142–154; read around 114–131; delete/list/count/clear around 165–235.
- `DefaultModuleStatusService` appears to be legitimate transient operational local state and should be left unchanged.
- Hot Rod producers can remain lazy for this step; service operations must fail explicitly rather than fall back to JVM-local state.

Required outcomes:
- Remove forbidden process-local fallback fields and branches from both services.
- When the required RemoteCache / Hot Rod capability is unavailable, fail explicitly using the smallest appropriate existing runtime/Infinispan exception style already natural to the module. Do not add a new framework/abstraction.
- Preserve normal not-found semantics for valid cache misses, but infrastructure unavailability must not appear as success.
- Add or refactor focused tests to prove:
    1. Mneme available -> configured RemoteCache is used normally.
    2. Mneme unavailable -> operation fails explicitly.
    3. Mneme unavailable -> no process-local fallback read/write path remains.
    4. Recovery where practical -> once cache becomes available again, service can use it normally without stale local state.
- Keep the change narrow to pylai-mllp-base; do not drift into Task 08/09/10, direct Mnemosyne paths, or broader redesign.

Known from previous steps:
- Step 1 in iris-befe is complete and green.
- For focused module tests in this repo, `-am` may pull in unrelated Docker-dependent modules; prefer the narrowest viable commands first and report what worked.

Execution optimization:
- Start with the narrowest relevant pylai-mllp-base test command(s) for `TaskServiceTest`, `ProvenanceServiceTest`, and `ModuleStatusServiceTest`.
- Use short timeouts first (60–120s) and only widen if genuinely necessary.
- Report exact commands run and outcomes in Discoveries.



Code is Ready

**Summary of Completed Work (Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base)**

**1. Fallback Removal in `DefaultTaskService.java`**
- Removed forbidden `taskStore` process-local map (`Map<String, Task>`).
- Eliminated all local fallback branches in `create()`, `getById()`, `update()`, `delete()`, `getAll()`, `search()`, `count()`, and `clear()`.
- Implemented `requireRemoteCache()` helper that explicitly throws `IllegalStateException("Mneme cache [task-cache] is unavailable")` when `RemoteCacheManager` is null, unstarted (`!isStarted()`), or fails to resolve the named cache.
- Preserved normal FHIR not-found semantics returning `Optional.empty()` on valid cache misses.

**2. Fallback Removal in `DefaultProvenanceService.java`**
- Removed forbidden `provenanceStore` process-local map (`Map<String, Provenance>`).
- Eliminated all local fallback branches in `create()`, `getById()`, `update()`, `delete()`, `getAll()`, `search()`, `count()`, and `clear()`.
- Implemented `requireRemoteCache()` helper that explicitly throws `IllegalStateException("Mneme cache [provenance-cache] is unavailable")` when `RemoteCacheManager` is null, unstarted, or fails to resolve the named cache.
- Preserved normal FHIR not-found semantics returning `Optional.empty()` on valid cache misses.

**3. Verification of `DefaultModuleStatusService.java` in `pylai-mllp-base`**
- Inspected and documented `localFallbackCache` in `DefaultModuleStatusService` as legitimate retained process-local state for node status telemetry and readiness snapshots per ADR-019.

**4. Test Suite Refactoring & Additions**
- **`TaskServiceTest.java`**: Refactored to use mocked `RemoteCacheManager` and `RemoteCache`. Added 9 comprehensive unit tests covering:
    1. Full CRUD and count operations against remote cache.
    2. Search filtering by ID, status, priority, patient, identifier, and text against remote cache.
    3. Valid cache miss returns `Optional.empty()`.
    4. Explicit `IllegalStateException` when `RemoteCacheManager` is null across all operations.
    5. Explicit `IllegalStateException` when `RemoteCacheManager` is unstarted.
    6. Explicit `IllegalStateException` when named cache is null.
    7. Remote cache transport exception (`HotRodClientException`) propagation.
    8. Zero local fallback state mutation when cache fails.
    9. Clean service recovery upon reconnect without stale local data.
- **`ProvenanceServiceTest.java`**: Refactored to use mocked `RemoteCacheManager` and `RemoteCache`. Added 9 comprehensive unit tests matching the above scenarios for `Provenance`.
- **`ModuleStatusServiceTest.java`**: Verified 5/5 tests passing with retained local status snapshot behavior.
- **Regression suites**: Full test suites in `pylai-mllp-base` (48 tests), `pylai-mllp-in` (46 tests), `pylai-mllp-out` (24 tests), and `paradeigma-test` architecture suite (57 tests) pass with zero errors.

---

**Discoveries for future steps**
- **Execution commands & speed**: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` executes in ~3.5s.
- **Infinispan Mocking**: `RemoteCache.size()` inherits from `java.util.Map.size()` and returns `int`, so Mockito stubbing must return `int` (e.g. `remoteStore.size()`) rather than `(long)` to avoid boxed `Long` to `Integer` `ClassCastException`.
- **CloseableIterator Mocking**: When stubbing `RemoteCache.values()` and `RemoteCache.keySet()`, mock `CloseableIteratorCollection` and `CloseableIteratorSet` with a custom `toCloseableIterator(Iterator<T>)` adapter to avoid `ClassCastException` with standard `java.util.Iterator`.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 2.

**Feedback**  
Review only the pylai-mllp-base scope for Task 07 Step 03.

Focus on:
- whether `DefaultTaskService` no longer keeps or uses any process-local Mneme fallback state;
- whether `DefaultProvenanceService` no longer keeps or uses any process-local Mneme fallback state;
- whether missing/unstarted RemoteCache / Hot Rod capability now fails explicitly rather than appearing successful;
- whether valid not-found semantics remain intact;
- whether the added/refactored tests truthfully prove remote success, explicit failure on unavailability, no local fallback path, and practical recovery;
- whether `DefaultModuleStatusService` was correctly left untouched;
- any unintended regressions or scope drift beyond pylai-mllp-base.

Also verify the claimed test execution/results if observable.



Implementation Approved

**Verification**
- **`DefaultTaskService.java`**: Confirmed `taskStore` `ConcurrentHashMap` field and all fallback read/write/list/count/clear branches are fully removed. All CRUD/search/count/clear operations now route exclusively through `task-cache` via `requireRemoteCache()`, which throws `IllegalStateException("Mneme cache [task-cache] is unavailable")` when the manager is null, unstarted, or the named cache resolves to null. Valid cache misses still return `Optional.empty()` (getById) / empty list (getAll). No local fallback field remains.
- **`DefaultProvenanceService.java`**: Confirmed `provenanceStore` map and all fallback branches removed. Symmetric behavior to TaskService with `provenance-cache` and correct explicit-failure semantics; valid misses preserved.
- **Failure semantics**: Infrastructure unavailability (null/unstarted manager, null named cache) throws `IllegalStateException`; remote transport failures (`HotRodClientException`) propagate unswallowed — no false success. This aligns with ADR-019 and the ingress dual-write safety invariant (REC-001).
- **`DefaultModuleStatusService.java`**: Verified via `git diff` the only change is Javadoc documentation (+6 lines) documenting `localFallbackCache` as legitimate retained process-local telemetry per ADR-019. The map and behavior are untouched — exactly matching the acceptance criterion.
- **Tests**: `TaskServiceTest` (9) and `ProvenanceServiceTest` (9) refactored to inject mocked `RemoteCacheManager`/`RemoteCache` backed by a `remoteStore` map. They genuinely prove: remote CRUD/search success (asserting on `remoteStore`), valid miss → empty, explicit `IllegalStateException` across all ops on null/unstarted/null-cache, `HotRodClientException` propagation, zero-local-fallback-state after failure+reconnect, and clean recovery. Tests are not over-mocked — they exercise real service logic against a simulated remote store.
- **Execution**: `mvn test -pl pylai/pylai-mllp-base -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest"` → 23 tests, 0 failures. Full module `mvn test -pl pylai/pylai-mllp-base` → 48 tests, BUILD SUCCESS, no regressions.
- **Scope**: Changes confined to pylai-mllp-base; no Task 08/09/10 drift, no direct Mnemosyne paths, no new abstraction framework.

**Issues**  
None blocking.

**Notes**
- Minor non-blocking: both `DefaultTaskService.java` (line 34) and `DefaultProvenanceService.java` (line 33) retain now-unused `import java.util.concurrent.ConcurrentHashMap;` after the fallback map removal. Cosmetic only; does not affect compilation or behavior.

**Discoveries for future steps**
- Targeted test command runs in ~2s once compiled; full pylai-mllp-base module ~compiles+48 tests without Docker dependencies. `-am` not needed for the base module tests since deps are already built.
- Infinispan mocking pattern established (reusable for Step 3): mock `RemoteCache.size()` returning `int` (not boxed `Long`), and mock `CloseableIteratorCollection`/`CloseableIteratorSet` with a custom `toCloseableIterator` adapter for `values()`/`keySet()`.



**Requirements**

**Goal & Outcome**  
Enforce the working-state cache boundary established in ADR-019 by eliminating process-local in-memory fallbacks (`ConcurrentHashMap` stores) that substitute for Mneme/Infinispan distributed working-state semantics. When Mneme is unavailable, operations must fail visibly and explicitly rather than silently degrading into local JVM memory.

**Scope**
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

**Done When**
- All 8 identified fallback `ConcurrentHashMap` fields and their associated read, write, delete, search, and seeding fallback branches are deleted.
- Missing RemoteCacheManager, unstarted Hot Rod client, or missing named cache instances cause explicit runtime failures rather than fallback persistence.
- `ModuleStatusService` in all three modules is verified and documented as legitimate process-local operational state.
- Focused unit and service tests in `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass with mocked and configured cache fixtures.
- Full architecture suite (`*ArchitectureTest`) in `paradeigma-test` passes without regression.

**Technical Design**

**Decisions**
- **Decision: Throw explicit runtime/cache exceptions on missing or failed RemoteCache / not catching and writing to local maps**
    - *Rationale*: ADR-019 dictates that loss of Mneme distributed working state must be visible as loss of capability. Swallowing cache failures or returning JVM-local data creates split-brain state across cluster nodes. Standard exceptions (such as `IllegalStateException` for uninitialized/unavailable cache or propagating `CacheException`) communicate failure cleanly without custom framework bloat.
- **Decision: Retain lazy Hot Rod client producer initialization / not enforcing eager startup crash**
    - *Rationale*: `HotRodClientProducer` creates `new RemoteCacheManager(..., false)` if the cluster is unreachable during bootstrap. Retaining lazy startup allows processes to start in constrained environments while ensuring individual operations fail explicitly when invoking cache calls without an active connection.
- **Decision: Retain `localFallbackCache` in `ModuleStatusService` / not converting to fail-closed distributed lock**
    - *Rationale*: `ModuleStatusService` tracks transient local telemetry and readiness snapshots. It is not workflow, task, or clinical working state. Retaining its local snapshot satisfies the requirement to avoid removing legitimate process-local state.

**Approach & Touches**
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

**Nuances / Risks / Corners**
- **Test Fixture Mocks**: Existing service tests (`PraxisServiceTest`, `TaskCacheServiceTest`, `MessageQueueServiceTest`, `FhirCacheServiceTest`, `TaskServiceTest`, `ProvenanceServiceTest`) constructed services with no arguments (`null` `RemoteCacheManager`) and asserted on the local fallback behavior. Every test must be updated to provide a mocked `RemoteCacheManager` returning a mocked `RemoteCache` for standard positive cases, plus explicit tests for uninitialized/disconnected manager scenarios.
- **Null Safety vs Explicit Failure**: Valid domain misses (e.g. key not found in cache) must return `null` / `Optional.empty()` normally, whereas infrastructure failure (RemoteCache unavailable or Hot Rod communication exception) must throw an explicit exception.
- **Default Seeding Paths**: Methods like `MessageQueueService.seedDefaultQueues()` and `TaskSequenceCacheService.ensureDefaultSequences()` must interact with the remote cache or fail gracefully without populating a hidden in-memory store.

**Testing**

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

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE**  
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

**✓ Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base**  
Goal: Remove process-local fallback stores from `DefaultTaskService` and `DefaultProvenanceService`, requiring active RemoteCache for Task and Provenance state.  
Scope: `pylai/pylai-mllp-base` (`DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskServiceTest.java`, `ProvenanceServiceTest.java`).  
Acceptance Criteria:
- [ ] `taskStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultTaskService.java`.
- [ ] `provenanceStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultProvenanceService.java`.
- [ ] `DefaultTaskService` and `DefaultProvenanceService` throw explicit runtime exceptions on create, update, delete, search, count, or clear when RemoteCache is unavailable.
- [ ] `DefaultModuleStatusService.java` in `pylai-mllp-base` verified and documented as retained process-local state.
- [ ] `TaskServiceTest` and `ProvenanceServiceTest` refactored to use mocked RemoteCacheManager/RemoteCache, verifying remote success, explicit failure when unavailable, and absence of local fallback state.
  Verification: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia**  
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

**Step 4: Full Test Suite, Architecture Invariant Verification, and Final Report**  
Goal: Execute full test verification across all affected modules, validate ArchUnit architectural guardrails, and compile the final Step 03 report.  
Scope: Full repository test verification (`iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, `energeia-ponos`, `paradeigma-test`).  
Acceptance Criteria:
- [ ] All unit and service test suites across `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass without error.
- [ ] ArchUnit suite in `paradeigma/paradeigma-test` passes without regression (`*ArchitectureTest`).
- [ ] Final report compiled covering all 12 items specified in the parent task (candidate inspection, fallback fields removed, fallback branches removed, post-change failure semantics, retained local maps, lazy Hot Rod behavior assessment, tests added/modified, and guardrail confirmations).
- [ ] Confirmed zero direct Mnemosyne read/write paths introduced, zero Task 08/09/10 leakage, and zero architecture redesign of Petasos, Mnemosyne, Kleio, or Themis.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Requirements**

**Goal & Outcome**  
Enforce the working-state cache boundary established in ADR-019 by eliminating process-local in-memory fallbacks (`ConcurrentHashMap` stores) that substitute for Mneme/Infinispan distributed working-state semantics. When Mneme is unavailable, operations must fail visibly and explicitly rather than silently degrading into local JVM memory.

**Scope**
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

**Done When**
- All 8 identified fallback `ConcurrentHashMap` fields and their associated read, write, delete, search, and seeding fallback branches are deleted.
- Missing RemoteCacheManager, unstarted Hot Rod client, or missing named cache instances cause explicit runtime failures rather than fallback persistence.
- `ModuleStatusService` in all three modules is verified and documented as legitimate process-local operational state.
- Focused unit and service tests in `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass with mocked and configured cache fixtures.
- Full architecture suite (`*ArchitectureTest`) in `paradeigma-test` passes without regression.

**Technical Design**

**Decisions**
- **Decision: Throw explicit runtime/cache exceptions on missing or failed RemoteCache / not catching and writing to local maps**
    - *Rationale*: ADR-019 dictates that loss of Mneme distributed working state must be visible as loss of capability. Swallowing cache failures or returning JVM-local data creates split-brain state across cluster nodes. Standard exceptions (such as `IllegalStateException` for uninitialized/unavailable cache or propagating `CacheException`) communicate failure cleanly without custom framework bloat.
- **Decision: Retain lazy Hot Rod client producer initialization / not enforcing eager startup crash**
    - *Rationale*: `HotRodClientProducer` creates `new RemoteCacheManager(..., false)` if the cluster is unreachable during bootstrap. Retaining lazy startup allows processes to start in constrained environments while ensuring individual operations fail explicitly when invoking cache calls without an active connection.
- **Decision: Retain `localFallbackCache` in `ModuleStatusService` / not converting to fail-closed distributed lock**
    - *Rationale*: `ModuleStatusService` tracks transient local telemetry and readiness snapshots. It is not workflow, task, or clinical working state. Retaining its local snapshot satisfies the requirement to avoid removing legitimate process-local state.

**Approach & Touches**
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

**Nuances / Risks / Corners**
- **Test Fixture Mocks**: Existing service tests (`PraxisServiceTest`, `TaskCacheServiceTest`, `MessageQueueServiceTest`, `FhirCacheServiceTest`, `TaskServiceTest`, `ProvenanceServiceTest`) constructed services with no arguments (`null` `RemoteCacheManager`) and asserted on the local fallback behavior. Every test must be updated to provide a mocked `RemoteCacheManager` returning a mocked `RemoteCache` for standard positive cases, plus explicit tests for uninitialized/disconnected manager scenarios.
- **Null Safety vs Explicit Failure**: Valid domain misses (e.g. key not found in cache) must return `null` / `Optional.empty()` normally, whereas infrastructure failure (RemoteCache unavailable or Hot Rod communication exception) must throw an explicit exception.
- **Default Seeding Paths**: Methods like `MessageQueueService.seedDefaultQueues()` and `TaskSequenceCacheService.ensureDefaultSequences()` must interact with the remote cache or fail gracefully without populating a hidden in-memory store.

**Testing**

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

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE**  
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

**✓ Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base**  
Goal: Remove process-local fallback stores from `DefaultTaskService` and `DefaultProvenanceService`, requiring active RemoteCache for Task and Provenance state.  
Scope: `pylai/pylai-mllp-base` (`DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskServiceTest.java`, `ProvenanceServiceTest.java`).  
Acceptance Criteria:
- [ ] `taskStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultTaskService.java`.
- [ ] `provenanceStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultProvenanceService.java`.
- [ ] `DefaultTaskService` and `DefaultProvenanceService` throw explicit runtime exceptions on create, update, delete, search, count, or clear when RemoteCache is unavailable.
- [ ] `DefaultModuleStatusService.java` in `pylai-mllp-base` verified and documented as retained process-local state.
- [ ] `TaskServiceTest` and `ProvenanceServiceTest` refactored to use mocked RemoteCacheManager/RemoteCache, verifying remote success, explicit failure when unavailable, and absence of local fallback state.
  Verification: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

*** Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia**  
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

**Step 4: Full Test Suite, Architecture Invariant Verification, and Final Report**  
Goal: Execute full test verification across all affected modules, validate ArchUnit architectural guardrails, and compile the final Step 03 report.  
Scope: Full repository test verification (`iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, `energeia-ponos`, `paradeigma-test`).  
Acceptance Criteria:
- [ ] All unit and service test suites across `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass without error.
- [ ] ArchUnit suite in `paradeigma/paradeigma-test` passes without regression (`*ArchitectureTest`).
- [ ] Final report compiled covering all 12 items specified in the parent task (candidate inspection, fallback fields removed, fallback branches removed, post-change failure semantics, retained local maps, lazy Hot Rod behavior assessment, tests added/modified, and guardrail confirmations).
- [ ] Confirmed zero direct Mnemosyne read/write paths introduced, zero Task 08/09/10 leakage, and zero architecture redesign of Petasos, Mnemosyne, Kleio, or Themis.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



IMPLEMENTATION

**Task description**  
Fully complete step 3.

**Feedback**  
Implement the Energeia portion of Task 07 Step 03.

Confirmed current state from exploration:
- `TaskCacheService` (`energeia/erga`) uses forbidden Mneme substitute maps: `localFallbackCache` and `localFallbackProvenanceCache`. Relevant areas: fields around 60–61; remote resolvers 83–103; task read 105–122; task write 217–232; provenance write 253–268; provenance read 284–301; delete/clear/count 303–429.
- `PragmaCacheService` (`energeia/praxis`) uses forbidden `localFallbackCache`. Relevant areas: field around 59; init 71–87; save 96–125; get 134–161; delete 180–200; clear 217–224. Important review note: it currently resolves/caches the remote cache at init, so recovery after startup outage may require resolving the cache lazily/per operation instead.
- `PraxisService` (`energeia/praxis`) uses forbidden `localFallbackCache`. Relevant areas: field around 53; resolver 90–99; save 137–164; get 175–195; list 198–245; delete 269–289; clear 296–306.
- `MessageQueueService` (`energeia/ponos`) uses forbidden `localFallbackCache`. Relevant areas: field around 52; resolver 99–108; save 134–157; get 160–184; delete 186–203; list/seed 206–308. Default queue seeding currently can populate only local fallback state when Mneme is unavailable.
- `energeia/ponos` `ModuleStatusService` appears to be legitimate transient operational local state and should be left unchanged.
- Hot Rod producers can remain lazy for this step; service operations must fail explicitly rather than fall back to JVM-local state.

Required outcomes:
- Remove the forbidden process-local fallback fields and branches from all four services.
- When the required RemoteCache / Hot Rod capability is unavailable, fail explicitly using the smallest appropriate existing runtime/Infinispan exception style already natural to the module. Do not add a new framework/abstraction.
- Preserve normal not-found semantics for valid cache misses, but infrastructure unavailability must not appear as success.
- For `PragmaCacheService`, make recovery practical if possible by resolving the cache lazily/per-call instead of caching a null remote reference from startup.
- For default seeding paths (`MessageQueueService`, and any similar path touched), do not silently create local default state; either use the configured RemoteCache or fail/skip explicitly as appropriate.
- Add or refactor focused tests to prove:
    1. Mneme available -> configured RemoteCache is used normally.
    2. Mneme unavailable -> operation fails explicitly.
    3. Mneme unavailable -> no process-local fallback read/write path remains.
    4. Recovery where practical -> once cache becomes available again, service can use it normally without stale local state.
- Keep the change narrow to Energeia; do not drift into Task 08/09/10, direct Mnemosyne paths, or broader redesign.

Known from previous steps:
- Step 1 in iris-befe and step 2 in pylai-mllp-base are complete and green.
- Reusable mocking pattern established: mock `RemoteCache.size()` as `int`, and use closeable-iterator helpers for `values()`/`keySet()` to avoid `ClassCastException`.
- `TaskCacheServiceTest` currently lives under `energeia/ponos` even though the source service is in `energeia/erga`.

Execution optimization:
- Start with the narrowest relevant test command(s) for `TaskCacheServiceTest`, `PraxisServiceTest`, `MessageQueueServiceTest`, `PragmaCacheServiceTest`, and `ModuleStatusServiceTest`.
- Use short timeouts first (60–120s) and only widen if genuinely necessary.
- Report exact commands run and outcomes in Discoveries.



Code is Ready

**Summary of Completed Work (Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia)**

**1. Fallback Removal in `TaskCacheService.java` (`energeia/erga`)**
- Removed forbidden `localFallbackCache` (`Map<String, String>`) and `localFallbackProvenanceCache` (`Map<String, String>`) fields.
- Eliminated all local fallback execution branches across `getTaskJson()`, `putTaskJson()`, `saveProvenance()`, `putProvenanceJson()`, `getProvenanceJson()`, `deleteProvenance()`, `deleteTask()`, `clear()`, and `count()`.
- Added `requireTaskCache()` and `requireProvenanceCache()` helpers that throw explicit `IllegalStateException("Mneme cache [task-cache] is unavailable")` or `IllegalStateException("Mneme cache [provenance-cache] is unavailable")` whenever `RemoteCacheManager` is uninitialized, stopped, or fails to resolve the named cache.

**2. Fallback Removal in `PragmaCacheService.java` (`energeia/praxis`)**
- Removed forbidden `localFallbackCache` (`Map<String, Pragma>`) field.
- Removed startup static caching of the `RemoteCache` instance (`pragmaCache` field), enabling lazy per-operation resolution and clean service recovery upon reconnect.
- Updated `savePragma()`, `getPragma()`, `deletePragma()`, and `clear()` to operate strictly against the `task-cache` `RemoteCache`, throwing explicit `IllegalStateException` when the cache is unavailable.

**3. Fallback Removal in `PraxisService.java` (`energeia/praxis`)**
- Removed forbidden `localFallbackCache` (`Map<String, String>`) field and all local fallback read/write/merge branches.
- Updated `save()`, `create()`, `update()`, `getById()`, `getAll()`, `delete()`, `count()`, and `clear()` to interact solely with the `tasksequence-cache` `RemoteCache` via `requireRemoteCache()`.

**4. Fallback Removal in `MessageQueueService.java` (`energeia/ponos`)**
- Removed forbidden `localFallbackCache` (`Map<String, String>`) field.
- Refactored `init()` so queue cache inspection occurs only when `RemoteCacheManager` is present and active.
- Updated `save()`, `getById()`, `delete()`, `getAll()`, `count()`, and `clear()` to operate strictly via `requireRemoteCache()`, throwing explicit `IllegalStateException` when `messagequeue-cache` is unavailable.
- Ensured `seedDefaultQueues()` persists definitions directly to the remote cache without populating local fallback state.

**5. Verification of `ModuleStatusService.java` (`energeia/ponos`)**
- Verified and documented `localFallbackCache` in `ModuleStatusService` as retained process-local state for transient node readiness snapshots per ADR-019.

**6. Test Suites Refactored & Added**
- **`PraxisServiceTest.java`**: Refactored to test `PraxisDefinition` CRUD with mocked `RemoteCacheManager` / `RemoteCache`, explicit `IllegalStateException` on missing/unstarted cache, transport failure propagation, zero local fallback mutation, and reconnect recovery.
- **`PragmaCacheServiceTest.java`**: Created new test suite covering canonical `Pragma` CRUD, checkpoints, explicit `IllegalStateException` when cache is absent, zero fallback mutations, reconnect recovery, and `TaskCacheService` delegation.
- **`TaskCacheServiceTest.java`**: Refactored to test `Task`, `Provenance`, and `Pragma` caching against remote caches, asserting explicit `IllegalStateException` on missing cache, transport exceptions, zero fallback mutations, and reconnect recovery.
- **`MessageQueueServiceTest.java`**: Refactored to test queue definition seeding, retrieval, deletion, count, and clear against remote cache, asserting explicit failure on unstarted/null cache manager, transport exception propagation, zero fallback mutations, and clean reconnect recovery.

---

**Discoveries for future steps**
- **Execution commands & speed**:
    - `mvn test -pl energeia/praxis,energeia/ponos -am -Dtest="TaskCacheServiceTest,PraxisServiceTest,MessageQueueServiceTest,PragmaCacheServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` executes in ~2.8s.
    - `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` executes all 57 ArchUnit tests across the repository in ~9.1s.
- **Execution optimization**: Always supply `-Dsurefire.failIfNoSpecifiedTests=false` when running targeted `-Dtest=...` commands with `-am` across reactor modules to prevent surefire from failing on modules that do not contain matching test names.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 3.

**Feedback**  
Review only the Energeia scope for Task 07 Step 03.

Focus on:
- whether `TaskCacheService` no longer keeps or uses any process-local Mneme fallback state for task/provenance working state;
- whether `PragmaCacheService` no longer keeps or uses any process-local Mneme fallback state, and whether lazy/per-call cache resolution now makes recovery practical after startup outage;
- whether `PraxisService` no longer keeps or uses any process-local Mneme fallback state;
- whether `MessageQueueService` no longer keeps or uses any process-local Mneme fallback state or local default-seeding substitute;
- whether missing/unstarted RemoteCache / Hot Rod capability now fails explicitly rather than appearing successful;
- whether valid not-found semantics remain intact;
- whether the added/refactored tests truthfully prove remote success, explicit failure on unavailability, no local fallback path, and practical recovery;
- whether `ModuleStatusService` was correctly left untouched;
- any unintended regressions or scope drift beyond the Energeia services listed in the plan.

Also verify the claimed test execution/results if observable.



Implementation Approved

**Verification**
- **TaskCacheService** (`energeia/erga`): `localFallbackCache` and `localFallbackProvenanceCache` fully removed. `getTaskJson`, `saveTask`, `putTaskJson`, `putProvenanceJson`, `getProvenanceJson`, `deleteTask`, `deleteProvenance`, `count`, `clear` all route through `requireTaskCache()`/`requireProvenanceCache()` which throw `IllegalStateException("Mneme cache [...] is unavailable")`. Valid not-found returns `null`/`Optional.empty()`.
- **PragmaCacheService** (`energeia/praxis`): `localFallbackCache` removed. Cache is now resolved lazily per-call via `getRemoteCache()` (no cached static reference), making post-startup recovery practical — verified by `testRecoveryUponReconnect`. `savePragma`/`getPragma`/`deletePragma`/`clear` fail explicitly when unavailable; serialization errors rethrown as RuntimeException.
- **PraxisService** (`energeia/praxis`): `localFallbackCache` removed; all CRUD/list/count/clear route solely through `tasksequence-cache` via `requireRemoteCache()`.
- **MessageQueueService** (`energeia/ponos`): `localFallbackCache` removed; `seedDefaultQueues()` persists via `save()` → remote cache only (no hidden local seeding); `init()` only triggers seeding when the manager is started.
- **ModuleStatusService** (`energeia/ponos`): confirmed NOT modified in this step (git diff scope limited to the 4 services + 3 tests) — correctly retained as legitimate process-local telemetry.
- **Tests**: All target suites pass — TaskCacheServiceTest (12), MessageQueueServiceTest (9), PragmaCacheServiceTest (10), PraxisServiceTest (9), ModuleStatusServiceTest (5), plus full ponos/praxis suites green. Tests are behavior-driven with real remote-store simulation (ConcurrentHashMap-backed mocks) and genuinely assert: remote success, explicit failure on null/unstarted manager, HotRodClientException transport propagation, zero-fallback state after failure+reconnect, and recovery.
- No scope drift: no direct Mnemosyne paths, no Task 08/09/10 leakage, no Petasos/Themis/Kleio redesign.

**Issues**
1. **`TaskCacheService.savePragma` swallows the explicit unavailability exception.** In `energeia/erga/.../TaskCacheService.java` lines 182-188, the body wraps both `objectMapper.writeValueAsString(pragma)` and `putTaskJson(...)` in a broad `catch (Exception e)` that only logs `error` and then returns `pragma`. Because `putTaskJson` → `requireTaskCache()` throws `IllegalStateException` when Mneme is unavailable, this exception is caught and swallowed, so `savePragma` returns success even though nothing was persisted — a silent degradation of task working state, which is exactly what ADR-019 forbids (though no local JVM store is created). This is inconsistent with the sibling `PragmaCacheService.savePragma` (lines 143-149), which correctly rethrows RuntimeExceptions. `savePragma` is not in the plan's enumerated remote-only method list and no test covers the cache-unavailable case for it, so it does not block the enumerated acceptance criteria, but it is a genuine fail-explicit gap.

**Suggested alternative**  
Narrow the `catch` in `TaskCacheService.savePragma` to serialization only (e.g., catch `JsonProcessingException`/serialization exceptions), or rethrow `RuntimeException`/`IllegalStateException` as `PragmaCacheService.savePragma` already does, and add a cache-unavailable test asserting `IllegalStateException`. Switching cost: cheap (a few lines + one test).

**Notes**
- Cosmetic (non-blocking): unused `import java.util.concurrent.ConcurrentHashMap` remains in `TaskCacheService` (line 39), `PraxisService` (line 33), `MessageQueueService` (line 33), and `PragmaCacheService` (line 36). Stale javadoc still references "local fallback storage" (`TaskCacheService` line 42) and "fallback memory cache" (`PraxisService` line 40).
- `TaskCacheServiceTest` physically lives under `energeia/ponos` though the service source is in `energeia/erga`; harmless (ponos depends on erga) and consistent with prior arrangement.

**Discoveries for future steps**
- Verification command `mvn test -pl energeia/praxis,energeia/ponos -am -Dtest="TaskCacheServiceTest,PraxisServiceTest,MessageQueueServiceTest,PragmaCacheServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` completes in ~2–3 min with `-am` reactor build; individual test classes each run <2s.
- Reusable mock pattern confirmed working: back RemoteCache mocks with a `ConcurrentHashMap` remote-store and use `CloseableIterator`/`CloseableIteratorCollection`/`CloseableIteratorSet` helpers for `values()`/`keySet()` to avoid ClassCastException.
- Repo has large amounts of unrelated uncommitted changes (Task 6 audit/Kleio/Themis work, `pylai` DefaultModuleStatusService); the energeia Step 3 diff is cleanly isolated to 7 files.



IMPLEMENTATION

**Task description**  
Within step 3, do only the part in ## Task scope.

**Task scope**  
Fix the remaining fail-explicit gap in `TaskCacheService.savePragma(...)` and add/adjust focused test coverage for that exact path.

**Feedback**  
The reviewer is right about one concrete defect and the rest of step 3 should be kept as-is.

Problem to fix:
- In `energeia/erga/.../TaskCacheService.savePragma(...)`, the current broad `catch (Exception e)` swallows `IllegalStateException` from `putTaskJson(...)` / `requireTaskCache()` when Mneme is unavailable.
- That makes `savePragma(...)` return success even though nothing was persisted, which violates the required explicit-failure semantics.

What to change:
- Narrow the catch so cache-unavailability / runtime infrastructure failures are NOT swallowed.
- It is fine to catch only serialization-specific exceptions, or to rethrow runtime exceptions after logging; use the smallest coherent change consistent with the rest of the class/module.
- Preserve the intended behavior for genuine serialization problems, but do not mask Mneme unavailability.
- Add or update a focused test that proves `savePragma(...)` fails explicitly when the task cache is unavailable, and that success still works when the cache is available.

Keep everything else from step 3 unchanged unless compilation requires a tiny follow-on edit.

Known from previous steps:
- Reviewer specifically flagged `TaskCacheService.savePragma` lines around 182–188 as the gap.
- `PragmaCacheService.savePragma` already rethrows runtime failures and can be used as a consistency reference.
- Reuse the established remote-cache mock patterns and keep test execution narrow and fast.

Execution optimization:
- Run the narrowest relevant test command for `TaskCacheServiceTest` first.
- Use short timeouts and report exact commands/outcomes in Discoveries.



Code is Ready

I have remediated the fail-explicit gap in `TaskCacheService.savePragma` to ensure that cache unavailability does not lead to silent degradation, satisfying the ADR-019 requirements for visible failure semantics.

**Changes Implemented**

1.  **`TaskCacheService.java`**:
    *   Added `import com.fasterxml.jackson.core.JsonProcessingException;`.
    *   Updated `savePragma` to narrow the `try-catch` block, catching only `JsonProcessingException` for serialization failures and allowing infrastructure runtime exceptions (like `IllegalStateException` when the cache is unavailable) to propagate, ensuring explicit failure semantics.
2.  **`TaskCacheServiceTest.java`**:
    *   Added `testSavePragmaFailureWhenCacheUnavailable` to verify that `savePragma` now throws `IllegalStateException` when the cache is unavailable.
    *   Added `testSavePragmaSuccess` to confirm normal `savePragma` operation when the cache is available.

**Verification**
*   Ran `mvn test -pl energeia/ponos -am -Dtest="TaskCacheServiceTest" -Dsurefire.failIfNoSpecifiedTests=false`.
*   All 14 tests in `TaskCacheServiceTest` passed, including the two newly added tests, confirming the fix works as expected.

**Discoveries for future steps**
*   The established mock pattern using `Mockito` and `ConcurrentHashMap` remains efficient and reliable for validating failure/success conditions in these services.
*   The fix for `TaskCacheService.savePragma` is consistent with the established practice in `PragmaCacheService` and other services to propagate runtime exceptions on infrastructure failure.



IMPLEMENTATION REVIEW

**Task description**  
Within step 3, do only the part in ## Task scope.

**Task scope**  
Fix the remaining fail-explicit gap in `TaskCacheService.savePragma(...)` and add/adjust focused test coverage for that exact path.

**Feedback**  
Review only this retry scope.

Focus on:
- whether `TaskCacheService.savePragma(...)` no longer swallows `IllegalStateException` / runtime infrastructure failures from Mneme unavailability;
- whether serialization-only handling remains reasonable;
- whether the new or adjusted `TaskCacheServiceTest` cases truthfully prove explicit failure on unavailable cache and success on available cache;
- whether the claimed targeted test execution/results are observable;
- whether the rest of step 3 remained unchanged.



Implementation Approved

**Verification**
- **Fix confirmed** (`energeia/erga/.../TaskCacheService.java`, `savePragma` lines 174–193): the previously broad `catch (Exception e)` is now narrowed to `catch (JsonProcessingException e)`, which rethrows as `RuntimeException("Serialization failure", e)`. The `IllegalStateException` from `putTaskJson(...)` → `requireTaskCache()` now propagates cleanly on Mneme unavailability instead of being swallowed and returning fake success. This closes the exact fail-explicit gap flagged in the prior review and is consistent with the sibling `PragmaCacheService.savePragma`.
- **Serialization handling** remains reasonable: genuine serialization problems still produce a `RuntimeException`, while infrastructure unavailability surfaces as `IllegalStateException`.
- **Tests added** (`energeia/ponos/.../TaskCacheServiceTest.java`):
    - `testSavePragmaFailureWhenCacheUnavailable` (lines 217–227): sets `mockCacheManager.isStarted()` → false and asserts `savePragma` throws `IllegalStateException` with message `Mneme cache [task-cache] is unavailable`. This genuinely exercises the previously-swallowed path.
    - `testSavePragmaSuccess` (lines 229–240): asserts success with cache available and verifies `mockTaskCache.put(eq("pragma-2"), anyString())` is actually invoked — confirming real remote write, not a mock no-op.
- **Test execution observed**: `mvn test -pl energeia/ponos -am -Dtest="TaskCacheServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` completed with exit code 0 (BUILD SUCCESS) in ~30s reactor build.
- **Rest of step 3 unchanged**: remote-only routing via `requireTaskCache()`/`requireProvenanceCache()` and other services' fallbacks remain as previously approved; no scope drift beyond the two-file retry scope.

**Issues**  
None.

**Notes**
- Minor non-blocking carryovers from earlier passes (not in this retry scope): stale javadoc references and any unused imports noted in the prior review; these do not affect correctness.



**Requirements**

**Goal & Outcome**  
Enforce the working-state cache boundary established in ADR-019 by eliminating process-local in-memory fallbacks (`ConcurrentHashMap` stores) that substitute for Mneme/Infinispan distributed working-state semantics. When Mneme is unavailable, operations must fail visibly and explicitly rather than silently degrading into local JVM memory.

**Scope**
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

**Done When**
- All 8 identified fallback `ConcurrentHashMap` fields and their associated read, write, delete, search, and seeding fallback branches are deleted.
- Missing RemoteCacheManager, unstarted Hot Rod client, or missing named cache instances cause explicit runtime failures rather than fallback persistence.
- `ModuleStatusService` in all three modules is verified and documented as legitimate process-local operational state.
- Focused unit and service tests in `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass with mocked and configured cache fixtures.
- Full architecture suite (`*ArchitectureTest`) in `paradeigma-test` passes without regression.

**Technical Design**

**Decisions**
- **Decision: Throw explicit runtime/cache exceptions on missing or failed RemoteCache / not catching and writing to local maps**
    - *Rationale*: ADR-019 dictates that loss of Mneme distributed working state must be visible as loss of capability. Swallowing cache failures or returning JVM-local data creates split-brain state across cluster nodes. Standard exceptions (such as `IllegalStateException` for uninitialized/unavailable cache or propagating `CacheException`) communicate failure cleanly without custom framework bloat.
- **Decision: Retain lazy Hot Rod client producer initialization / not enforcing eager startup crash**
    - *Rationale*: `HotRodClientProducer` creates `new RemoteCacheManager(..., false)` if the cluster is unreachable during bootstrap. Retaining lazy startup allows processes to start in constrained environments while ensuring individual operations fail explicitly when invoking cache calls without an active connection.
- **Decision: Retain `localFallbackCache` in `ModuleStatusService` / not converting to fail-closed distributed lock**
    - *Rationale*: `ModuleStatusService` tracks transient local telemetry and readiness snapshots. It is not workflow, task, or clinical working state. Retaining its local snapshot satisfies the requirement to avoid removing legitimate process-local state.

**Approach & Touches**
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

**Nuances / Risks / Corners**
- **Test Fixture Mocks**: Existing service tests (`PraxisServiceTest`, `TaskCacheServiceTest`, `MessageQueueServiceTest`, `FhirCacheServiceTest`, `TaskServiceTest`, `ProvenanceServiceTest`) constructed services with no arguments (`null` `RemoteCacheManager`) and asserted on the local fallback behavior. Every test must be updated to provide a mocked `RemoteCacheManager` returning a mocked `RemoteCache` for standard positive cases, plus explicit tests for uninitialized/disconnected manager scenarios.
- **Null Safety vs Explicit Failure**: Valid domain misses (e.g. key not found in cache) must return `null` / `Optional.empty()` normally, whereas infrastructure failure (RemoteCache unavailable or Hot Rod communication exception) must throw an explicit exception.
- **Default Seeding Paths**: Methods like `MessageQueueService.seedDefaultQueues()` and `TaskSequenceCacheService.ensureDefaultSequences()` must interact with the remote cache or fail gracefully without populating a hidden in-memory store.

**Testing**

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

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE**  
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

**✓ Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base**  
Goal: Remove process-local fallback stores from `DefaultTaskService` and `DefaultProvenanceService`, requiring active RemoteCache for Task and Provenance state.  
Scope: `pylai/pylai-mllp-base` (`DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskServiceTest.java`, `ProvenanceServiceTest.java`).  
Acceptance Criteria:
- [ ] `taskStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultTaskService.java`.
- [ ] `provenanceStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultProvenanceService.java`.
- [ ] `DefaultTaskService` and `DefaultProvenanceService` throw explicit runtime exceptions on create, update, delete, search, count, or clear when RemoteCache is unavailable.
- [ ] `DefaultModuleStatusService.java` in `pylai-mllp-base` verified and documented as retained process-local state.
- [ ] `TaskServiceTest` and `ProvenanceServiceTest` refactored to use mocked RemoteCacheManager/RemoteCache, verifying remote success, explicit failure when unavailable, and absence of local fallback state.
  Verification: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**✓ Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia**  
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

**Step 4: Full Test Suite, Architecture Invariant Verification, and Final Report**  
Goal: Execute full test verification across all affected modules, validate ArchUnit architectural guardrails, and compile the final Step 03 report.  
Scope: Full repository test verification (`iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, `energeia-ponos`, `paradeigma-test`).  
Acceptance Criteria:
- [ ] All unit and service test suites across `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass without error.
- [ ] ArchUnit suite in `paradeigma/paradeigma-test` passes without regression (`*ArchitectureTest`).
- [ ] Final report compiled covering all 12 items specified in the parent task (candidate inspection, fallback fields removed, fallback branches removed, post-change failure semantics, retained local maps, lazy Hot Rod behavior assessment, tests added/modified, and guardrail confirmations).
- [ ] Confirmed zero direct Mnemosyne read/write paths introduced, zero Task 08/09/10 leakage, and zero architecture redesign of Petasos, Mnemosyne, Kleio, or Themis.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Requirements**

**Goal & Outcome**  
Enforce the working-state cache boundary established in ADR-019 by eliminating process-local in-memory fallbacks (`ConcurrentHashMap` stores) that substitute for Mneme/Infinispan distributed working-state semantics. When Mneme is unavailable, operations must fail visibly and explicitly rather than silently degrading into local JVM memory.

**Scope**
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

**Done When**
- All 8 identified fallback `ConcurrentHashMap` fields and their associated read, write, delete, search, and seeding fallback branches are deleted.
- Missing RemoteCacheManager, unstarted Hot Rod client, or missing named cache instances cause explicit runtime failures rather than fallback persistence.
- `ModuleStatusService` in all three modules is verified and documented as legitimate process-local operational state.
- Focused unit and service tests in `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass with mocked and configured cache fixtures.
- Full architecture suite (`*ArchitectureTest`) in `paradeigma-test` passes without regression.

**Technical Design**

**Decisions**
- **Decision: Throw explicit runtime/cache exceptions on missing or failed RemoteCache / not catching and writing to local maps**
    - *Rationale*: ADR-019 dictates that loss of Mneme distributed working state must be visible as loss of capability. Swallowing cache failures or returning JVM-local data creates split-brain state across cluster nodes. Standard exceptions (such as `IllegalStateException` for uninitialized/unavailable cache or propagating `CacheException`) communicate failure cleanly without custom framework bloat.
- **Decision: Retain lazy Hot Rod client producer initialization / not enforcing eager startup crash**
    - *Rationale*: `HotRodClientProducer` creates `new RemoteCacheManager(..., false)` if the cluster is unreachable during bootstrap. Retaining lazy startup allows processes to start in constrained environments while ensuring individual operations fail explicitly when invoking cache calls without an active connection.
- **Decision: Retain `localFallbackCache` in `ModuleStatusService` / not converting to fail-closed distributed lock**
    - *Rationale*: `ModuleStatusService` tracks transient local telemetry and readiness snapshots. It is not workflow, task, or clinical working state. Retaining its local snapshot satisfies the requirement to avoid removing legitimate process-local state.

**Approach & Touches**
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

**Nuances / Risks / Corners**
- **Test Fixture Mocks**: Existing service tests (`PraxisServiceTest`, `TaskCacheServiceTest`, `MessageQueueServiceTest`, `FhirCacheServiceTest`, `TaskServiceTest`, `ProvenanceServiceTest`) constructed services with no arguments (`null` `RemoteCacheManager`) and asserted on the local fallback behavior. Every test must be updated to provide a mocked `RemoteCacheManager` returning a mocked `RemoteCache` for standard positive cases, plus explicit tests for uninitialized/disconnected manager scenarios.
- **Null Safety vs Explicit Failure**: Valid domain misses (e.g. key not found in cache) must return `null` / `Optional.empty()` normally, whereas infrastructure failure (RemoteCache unavailable or Hot Rod communication exception) must throw an explicit exception.
- **Default Seeding Paths**: Methods like `MessageQueueService.seedDefaultQueues()` and `TaskSequenceCacheService.ensureDefaultSequences()` must interact with the remote cache or fail gracefully without populating a hidden in-memory store.

**Testing**

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

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE**  
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

**✓ Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base**  
Goal: Remove process-local fallback stores from `DefaultTaskService` and `DefaultProvenanceService`, requiring active RemoteCache for Task and Provenance state.  
Scope: `pylai/pylai-mllp-base` (`DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskServiceTest.java`, `ProvenanceServiceTest.java`).  
Acceptance Criteria:
- [ ] `taskStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultTaskService.java`.
- [ ] `provenanceStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultProvenanceService.java`.
- [ ] `DefaultTaskService` and `DefaultProvenanceService` throw explicit runtime exceptions on create, update, delete, search, count, or clear when RemoteCache is unavailable.
- [ ] `DefaultModuleStatusService.java` in `pylai-mllp-base` verified and documented as retained process-local state.
- [ ] `TaskServiceTest` and `ProvenanceServiceTest` refactored to use mocked RemoteCacheManager/RemoteCache, verifying remote success, explicit failure when unavailable, and absence of local fallback state.
  Verification: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**✓ Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia**  
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

*** Step 4: Full Test Suite, Architecture Invariant Verification, and Final Report**  
Goal: Execute full test verification across all affected modules, validate ArchUnit architectural guardrails, and compile the final Step 03 report.  
Scope: Full repository test verification (`iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, `energeia-ponos`, `paradeigma-test`).  
Acceptance Criteria:
- [ ] All unit and service test suites across `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass without error.
- [ ] ArchUnit suite in `paradeigma/paradeigma-test` passes without regression (`*ArchitectureTest`).
- [ ] Final report compiled covering all 12 items specified in the parent task (candidate inspection, fallback fields removed, fallback branches removed, post-change failure semantics, retained local maps, lazy Hot Rod behavior assessment, tests added/modified, and guardrail confirmations).
- [ ] Confirmed zero direct Mnemosyne read/write paths introduced, zero Task 08/09/10 leakage, and zero architecture redesign of Petasos, Mnemosyne, Kleio, or Themis.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



IMPLEMENTATION

**Task description**  
Fully complete step 4.

**Feedback**  
This is the final verification/reporting step for Task 07 Step 03.

Use the completed work from steps 1–3 and produce the final evidence needed for acceptance.

Required outcomes:
- Run the most relevant verification for all affected modules and the architecture guardrails.
- Confirm the final state needed for the issue report, including:
    1. Every candidate local fallback inspected.
    2. For each candidate: class/module, purpose of local state, whether it substituted for Mneme, action taken.
    3. Exact ConcurrentHashMap/equivalent fallback fields removed.
    4. Exact fallback branches removed.
    5. Failure behavior when Mneme is unavailable after the change.
    6. Any genuinely process-local maps deliberately retained and why.
    7. Lazy Hot Rod/startup behavior discovered and whether it required change.
    8. Tests added or changed.
    9. Test results.
    10. Confirmation that no direct Mnemosyne read/write path was introduced.
    11. Confirmation that Task 08, Task 09 and Task 10 behavior was not implemented.
    12. Confirmation that Petasos, Mnemosyne, Kleio and Themis architecture was not redesigned.
- Keep the scope strictly to Step 03 and do not start any next task block.

Known from previous steps:
- Step 1 (iris-befe) removed local fallbacks from `FhirCacheService` and `TaskSequenceCacheService`; `ModuleStatusService` retained.
- Step 2 (pylai-mllp-base) removed local fallbacks from `DefaultTaskService` and `DefaultProvenanceService`; `DefaultModuleStatusService` retained.
- Step 3 (Energeia) removed local fallbacks from `TaskCacheService`, `PragmaCacheService`, `PraxisService`, and `MessageQueueService`; `ModuleStatusService` retained; retry fixed `TaskCacheService.savePragma(...)` so Mneme unavailability now propagates.
- Exploration classified the three ModuleStatusService implementations as legitimate transient operational local state for this step.
- Hot Rod producers in iris-befe, pylai-mllp-base, and energeia/ponos can remain lazy because service operations now fail explicitly when Mneme is unavailable.

Execution optimization:
- Prefer targeted module test commands first, then the architecture suite.
- Use short timeouts first and only widen if genuinely needed.
- Report exact commands run and outcomes in Discoveries.
- If there are any environment-specific test limitations, report them clearly.



Code is Ready

**Final Report: Task 07 Step 03 — Working-State Cache Boundary Enforcement (Process-Local Mneme Fallback Removal)**

**Executive Summary**  
In compliance with **ADR-019**, all forbidden process-local in-memory fallback stores (`ConcurrentHashMap` maps) substituting for Mneme/Infinispan distributed working-state semantics across presentation, gateway, workflow, activity, and engine tiers have been permanently eliminated. When Mneme is unavailable, unstarted, or disconnected, operations fail visibly and explicitly with standard runtime/Infinispan exceptions (`IllegalStateException`) rather than silently degrading into local JVM memory. Legitimate node-local operational telemetry (`ModuleStatusService`) has been verified and retained. Full unit, service, integration, and ArchUnit architectural guardrail suites pass with zero errors or regressions.

---

**Comprehensive Item-by-Item Findings & Verification**

**1. Candidate Local Fallbacks Inspected**  
The following 9 candidate classes/services were comprehensively inspected across the codebase:
1. `FhirCacheService` (`iris/iris-befe`)
2. `TaskSequenceCacheService` (`iris/iris-befe`)
3. `DefaultTaskService` (`pylai/pylai-mllp-base`)
4. `DefaultProvenanceService` (`pylai/pylai-mllp-base`)
5. `TaskCacheService` (`energeia/erga`)
6. `PragmaCacheService` (`energeia/praxis`)
7. `PraxisService` (`energeia/praxis`)
8. `MessageQueueService` (`energeia/ponos`)
9. `ModuleStatusService` (`iris/iris-befe`, `pylai/pylai-mllp-base`, `energeia/ponos`)

---

**2. Candidate Evaluation & Actions Taken**

| Candidate Class | Module & Path | Purpose of Local State | Assessment | Action Taken |
| :--- | :--- | :--- | :--- | :--- |
| `FhirCacheService` | `iris/iris-befe` | Stored FHIR resource JSON for named resource caches. | **Forbidden Mneme fallback** | Removed `localFallbackCaches` map and all local read/write/delete/search fallback branches. Made operations remote-only with explicit `IllegalStateException` on missing cache. |
| `TaskSequenceCacheService` | `iris/iris-befe` | Stored serialized `PraxisDefinition` workflow sequences locally and seeded local defaults. | **Forbidden Mneme fallback** | Removed `localFallbackCache` map and fallback seeding branch. Enforced remote-only CRUD against `tasksequence-cache`. |
| `DefaultTaskService` | `pylai/pylai-mllp-base` | In-memory `Task` store populated before remote writes and used on remote failure. | **Forbidden Mneme fallback** | Removed `taskStore` map. Routed all Task CRUD, listing, counting, and clearing exclusively to `task-cache` with explicit `IllegalStateException`. |
| `DefaultProvenanceService` | `pylai/pylai-mllp-base` | In-memory `Provenance` store populated before remote writes and returned on remote cache absence/failure. | **Forbidden Mneme fallback** | Removed `provenanceStore` map. Routed all Provenance operations exclusively to `provenance-cache` with explicit `IllegalStateException`. |
| `TaskCacheService` | `energeia/erga` | In-memory JSON stores for tasks and provenances; count returned local map size. | **Forbidden Mneme fallback** | Removed `localFallbackCache` and `localFallbackProvenanceCache`. Made task, provenance, and pragma operations remote-only with explicit failure. Fixed `savePragma` catch block to propagate runtime infrastructure exceptions. |
| `PragmaCacheService` | `energeia/praxis` | Copied `Pragma` objects to local map before remote writes and served on remote failure. | **Forbidden Mneme fallback** | Removed `localFallbackCache` and static initialization cache reference. Routed all Pragma operations lazily and directly through `task-cache` RemoteCache. |
| `PraxisService` | `energeia/praxis` | Stored serialized `PraxisDefinition` objects and merged local entries on reads. | **Forbidden Mneme fallback** | Removed `localFallbackCache` map and read/write merge branches. Enforced remote-only operations against `tasksequence-cache`. |
| `MessageQueueService` | `energeia/ponos` | Stored serialized `PetasosQueueDefinition` objects and seeded defaults locally on remote unavailability. | **Forbidden Mneme fallback** | Removed `localFallbackCache` map and fallback seeding. Routed queue definitions and default seeding exclusively to `messagequeue-cache`. |
| `ModuleStatusService` | `iris-befe`, `pylai-mllp-base`, `energeia-ponos` | Local node operational readiness telemetry and transient health snapshots. | **Legitimate process-local state** | **Retained** as ephemeral node readiness state per ADR-019. |

---

**3. Exact Fallback Fields Removed**  
The following 9 forbidden fallback fields were deleted:
1. `FhirCacheService.java`: `private final Map<String, Map<String, String>> localFallbackCaches = new ConcurrentHashMap<>();`
2. `TaskSequenceCacheService.java`: `private final Map<String, String> localFallbackCache = new ConcurrentHashMap<>();`
3. `DefaultTaskService.java`: `private final ConcurrentHashMap<String, Task> taskStore = new ConcurrentHashMap<>();`
4. `DefaultProvenanceService.java`: `private final ConcurrentHashMap<String, Provenance> provenanceStore = new ConcurrentHashMap<>();`
5. `TaskCacheService.java`: `private final Map<String, String> localFallbackCache = new ConcurrentHashMap<>();`
6. `TaskCacheService.java`: `private final Map<String, String> localFallbackProvenanceCache = new ConcurrentHashMap<>();`
7. `PragmaCacheService.java`: `private final Map<String, Pragma> localFallbackCache = new ConcurrentHashMap<>();`
8. `PraxisService.java`: `private final Map<String, String> localFallbackCache = new ConcurrentHashMap<>();`
9. `MessageQueueService.java`: `private final Map<String, String> localFallbackCache = new ConcurrentHashMap<>();`

---

**4. Exact Fallback Branches Removed**
- **`FhirCacheService`**:
    - Removed fallback to `localFallbackCaches.get(resourceType)` on `getRemoteCache()` null or catch block in `getResourceJson`.
    - Removed catch-and-write to `localFallbackCaches` in `putResourceJson`.
    - Removed fallback deletion in `deleteResource`.
    - Removed local entry search in `searchResources`.
- **`TaskSequenceCacheService`**:
    - Removed fallback local seeding in `ensureDefaultSequences()`.
    - Removed local map read fallback in `getSequenceJson()` and `getAllSequenceJsons()`.
    - Removed local map mutation in `saveSequence()` and `deleteSequence()`.
- **`DefaultTaskService`**:
    - Removed local pre-population in `createTask()` and `updateTask()`.
    - Removed local map read fallback on cache miss/failure in `getTask()`, `getTaskByPayloadReference()`, and `listTasks()`.
    - Removed local map deletion/clearing in `deleteTask()` and `clear()`.
- **`DefaultProvenanceService`**:
    - Removed local pre-population in `createProvenance()` and `updateProvenance()`.
    - Removed local map read fallback in `getProvenance()`, `getProvenanceByTargetReference()`, and `listProvenances()`.
    - Removed local map deletion/clearing in `deleteProvenance()` and `clear()`.
- **`TaskCacheService`**:
    - Removed local map read/write fallbacks in `getTaskJson()`, `putTaskJson()`, `saveProvenance()`, `putProvenanceJson()`, `getProvenanceJson()`, `deleteTask()`, and `deleteProvenance()`.
    - Removed fallback-based sizing in `count()`.
    - Narrowed `savePragma()` exception handling so `IllegalStateException` on missing cache is never swallowed.
- **`PragmaCacheService`**:
    - Removed local fallback branch in `savePragma()`, `getPragma()`, `deletePragma()`, and `clear()`.
    - Removed init-time "using local fallback store" branch and static `pragmaCache` assignment.
- **`PraxisService`**:
    - Removed local map write fallback and "Saved locally" log in `save()`, `create()`, and `update()`.
    - Removed local map merging in `getById()` and `getAll()`.
    - Removed local map removal in `delete()` and `clear()`.
- **`MessageQueueService`**:
    - Removed local queue definition fallback storage in `save()`, `getById()`, `delete()`, `getAll()`, `count()`, and `clear()`.
    - Removed fallback branch in `seedDefaultQueues()`.

---

**5. Post-Change Failure Semantics When Mneme Is Unavailable**
- **Missing / Stopped RemoteCacheManager or Missing Named Cache**:
    - Calls to `requireRemoteCache()`, `requireTaskCache()`, `requireProvenanceCache()`, `requireSequenceCache()`, and `requireQueueCache()` immediately throw `IllegalStateException("Mneme cache [...] is unavailable")`.
    - Operations never return mock success or cached local data.
- **Remote Cache Outage / Transport Failure**:
    - Underlying Infinispan `CacheException` / `HotRodClientException` propagate cleanly up the call stack to trigger visible error responses or upstream retry mechanisms (e.g. MLLP NACK).
- **Domain Misses vs Infrastructure Failures**:
    - Normal cache misses when the cache is connected continue to return `null` or `Optional.empty()`.
    - Infrastructure unavailability throws an explicit exception.

---

**6. Genuinely Process-Local Maps Retained & Justification**
- **`ModuleStatusService`** in `iris-befe`, `pylai-mllp-base`, and `energeia-ponos`:
    - Retained `localFallbackCache` (`ConcurrentHashMap<String, ModuleStatusSnapshot>`).
    - *Justification*: Module readiness and telemetry snapshots reflect ephemeral in-process node lifecycle state, not clinical, task, pragma, or workflow distributed working state. Retaining local telemetry avoids node bootstrap deadlocks while still synchronizing to `modulestatus-cache` when available.

---

**7. Lazy Hot Rod / Startup Behavior Assessment**
- Inspected `HotRodClientProducer` in `iris-befe`, `pylai-mllp-base`, and `energeia-ponos`.
- The producer attempts eager start, falling back to `new RemoteCacheManager(configuration, false)` if the cluster is unreachable during bootstrap.
- *Assessment*: Lazy startup is retained because service operations now fail explicitly on invocation when unstarted, allowing deployment in test/constrained environments without masked state corruption.
- In `PragmaCacheService`, static remote cache caching was replaced with dynamic per-invocation resolution (`requireRemoteCache()`) to ensure clean reconnect recovery.

---

**8. Tests Added and Modified**
- **`iris-befe`**:
    - `FhirCacheServiceTest.java`: Refactored to test remote CRUD with mocked `RemoteCacheManager` / `RemoteCache`, explicit `IllegalStateException` on unavailable cache, zero local fallback writes, and reconnect recovery.
    - `TaskSequenceCacheServiceTest.java`: Created 13 focused tests covering sequence CRUD, default sequence verification, explicit failure when cache is unavailable, zero fallback state, and reconnect recovery.
- **`pylai-mllp-base`**:
    - `TaskServiceTest.java`: Updated to test `DefaultTaskService` CRUD with mocked `RemoteCache`, verifying remote delegation, `IllegalStateException` on missing cache, zero fallback state, and reconnect recovery.
    - `ProvenanceServiceTest.java`: Updated to test `DefaultProvenanceService` CRUD with mocked `RemoteCache`, verifying remote delegation, `IllegalStateException` on missing cache, zero fallback state, and reconnect recovery.
    - `ModuleStatusServiceTest.java`: Verified retained local status snapshot behavior.
- **`energeia-praxis`**:
    - `PraxisServiceTest.java`: Refactored to test `PraxisService` against `tasksequence-cache`, verifying CRUD, `IllegalStateException` on unstarted/null cache manager, transport exception propagation, zero fallback mutations, and reconnect recovery.
    - `PragmaCacheServiceTest.java`: Created 10 focused tests covering `Pragma` CRUD, checkpoints, explicit failure when cache is unavailable, zero local fallback persistence, reconnect recovery, and `TaskCacheService` delegation.
- **`energeia-ponos` / `energeia-erga`**:
    - `TaskCacheServiceTest.java`: Refactored to test `TaskCacheService` against remote caches (`task-cache`, `provenance-cache`), verifying task, provenance, and pragma caching, explicit `IllegalStateException` on unavailable cache, transport exceptions, zero fallback mutations, and reconnect recovery.
    - `MessageQueueServiceTest.java`: Refactored to test queue seeding, retrieval, deletion, and counts against remote cache, asserting explicit failure on unstarted/null cache manager, zero fallback state, and reconnect recovery.
    - `ModuleStatusServiceTest.java`: Verified retained local status snapshot behavior.

---

**9. Test Results**

| Module / Target | Command Run | Test Suite(s) | Results |
| :--- | :--- | :--- | :--- |
| `iris-befe` | `mvn test -pl iris/iris-befe -am -Dtest="FhirCacheServiceTest,TaskSequenceCacheServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` | `FhirCacheServiceTest` (12), `TaskSequenceCacheServiceTest` (13) | **25/25 Passed** (0 Failures, 0 Errors) |
| `pylai-mllp-base` | `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` | `TaskServiceTest` (9), `ProvenanceServiceTest` (9), `ModuleStatusServiceTest` (5) | **23/23 Passed** (0 Failures, 0 Errors) |
| `energeia-praxis`, `energeia-ponos` | `mvn test -pl energeia/praxis,energeia/ponos -am -Dtest="TaskCacheServiceTest,PraxisServiceTest,MessageQueueServiceTest,PragmaCacheServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` | `TaskCacheServiceTest` (14), `PragmaCacheServiceTest` (10), `PraxisServiceTest` (9), `MessageQueueServiceTest` (9), `ModuleStatusServiceTest` (5) | **47/47 Passed** (0 Failures, 0 Errors) |
| `paradeigma-test` (ArchUnit) | `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` | `IrisDecouplingArchitectureTest` (5), `PetasosApiIsolationArchitectureTest` (3), `PackageLayeringArchitectureTest` (4), `ParadeigmaIsolationArchitectureTest` (4), `ProviderRegistryArchitectureTest` (2), `AgoraIsolationArchitectureTest` (7), `SecurityEnforcementArchitectureTest` (32) | **57/57 Passed** (0 Failures, 0 Errors) |

---

**10. Direct Mnemosyne Read/Write Confirmation**
- **Confirmed**: Zero direct Mnemosyne read/write paths, JPA entity access, or database calls were introduced into `iris-befe`, `pylai-mllp-base`, or `energeia`.
- All working-state operations strictly use Infinispan Hot Rod `RemoteCache` interfaces.

**11. Task 08 / Task 09 / Task 10 Scope Boundary Confirmation**
- **Confirmed**:
    - No Task 08 Clinical write path redesign was implemented.
    - No Task 09 cache-aside read architecture or degraded Mnemosyne read modes were implemented.
    - No Task 10 Clinical search redesign was implemented.

**12. Architectural Invariant & Subsystem Integrity Confirmation**
- **Confirmed**: Zero architectural redesign was performed on Petasos messaging, Mnemosyne JPA repositories, Kleio audit persistence, or Themis security policies.
- All ArchUnit architectural rules passed without violation.

---

**Discoveries for future steps**
- **Execution commands & speed**:
    - Targeted service tests across all affected modules run in under ~4 seconds total:
        - `mvn test -pl iris/iris-befe -am -Dtest="FhirCacheServiceTest,TaskSequenceCacheServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` (~3.5s)
        - `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` (~3.4s)
        - `mvn test -pl energeia/praxis,energeia/ponos -am -Dtest="TaskCacheServiceTest,PraxisServiceTest,MessageQueueServiceTest,PragmaCacheServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` (~3.8s)
    - Full ArchUnit suite: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` (~9.2s).
- **Execution optimization**: Always include `-Dsurefire.failIfNoSpecifiedTests=false` when running multi-module reactor tests with `-Dtest=...` to avoid reactor build failures on intermediate modules lacking matching test names.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 4.

**Feedback**  
Review only the final verification/reporting step for Task 07 Step 03.

Focus on:
- whether the reported candidate inventory, actions taken, and retained-local-state rationale match the implemented work;
- whether the claimed failure semantics and scope-boundary confirmations are accurate;
- whether the reported test changes and test results are observable and sufficient;
- whether the final report stays within Step 03 scope and does not claim unverified work;
- whether any important issue-report item is missing or misstated.



Implementation Approved

**Verification**
- **All module test suites re-run and pass with counts matching the report:**
    - `iris-befe`: `FhirCacheServiceTest` (12), `TaskSequenceCacheServiceTest` (13) → BUILD SUCCESS.
    - `pylai-mllp-base`: `TaskServiceTest` (9), `ProvenanceServiceTest` (9), `ModuleStatusServiceTest` (5) = 23 → BUILD SUCCESS.
    - `energeia/praxis`,`energeia/ponos`: `PragmaCacheServiceTest` (10), `PraxisServiceTest` (9) [praxis 19]; `TaskCacheServiceTest` (14), `MessageQueueServiceTest` (9), `ModuleStatusServiceTest` (5) [ponos 28] → BUILD SUCCESS.
    - `paradeigma-test`: 7 `*ArchitectureTest` suites = 57 tests, 0 failures → BUILD SUCCESS (matches report's 57/57).
- **Code state matches report:** grep confirms the only remaining `localFallbackCache` maps are in the three `ModuleStatusService`/`DefaultModuleStatusService` classes (documented as retained legitimate local telemetry) plus test-only mock stores (`remoteTaskStore`/`remoteProvStore`). All 8 forbidden fallback fields (`localFallbackCaches`, `taskStore`, `provenanceStore`, `localFallbackCache`×4, `localFallbackProvenanceCache`) are gone.
- **Failure semantics verified in source and tests:** services throw `IllegalStateException("Mneme cache [...] is unavailable")` when cache manager is null/unstarted (TaskCacheService, MessageQueueService, PragmaCacheService, PraxisService), and propagate `HotRodClientException` on transport failure. Tests genuinely assert these (`assertThatThrownBy` for null manager, not-started manager, transport propagation, and zero-fallback-state mutation) — not trivial/over-mocked.
- **Scope-boundary claims confirmed:** `IrisDecouplingArchitectureTest` and `PackageLayeringArchitectureTest` pass, corroborating no JPA/Mnemosyne read/write path introduced and no cross-tier redesign; no new custom exception/abstraction classes were added.

**Issues**  
None blocking.

**Notes**
- The executor added a duplicate plan file `.junie/plans/remove-process-local-mneme-fallbacks.md` (a copy of the requirements plan). Harmless artifact, not part of code/tests; non-blocking.
- The "final report" was delivered as the executor's response text rather than a committed `.junie/reports/*Task 7 - Step 3*.md` file (Step 1 and Step 2 reports for this task family do exist as files). The acceptance criterion "Final report compiled" is satisfied by the response content, which is accurate and covers all 12 items; if a persisted report file is expected by convention, that is a minor follow-up.

**Discoveries for future steps**
- Targeted runs are fast: iris-befe ~ a few s, pylai ~3s, energeia (praxis+ponos) ~5s, full arch suite ~10s. Always pass `-Dsurefire.failIfNoSpecifiedTests=false` with `-pl ... -am -Dtest=...` to avoid reactor failures on intermediate modules lacking matching tests.
- To extract results without noise, pipe mvn output through `grep -E "Tests run.*in net|BUILD"` (the `-q` flag suppresses per-class counts, so avoid it when you need counts).



**Requirements**

**Goal & Outcome**  
Enforce the working-state cache boundary established in ADR-019 by eliminating process-local in-memory fallbacks (`ConcurrentHashMap` stores) that substitute for Mneme/Infinispan distributed working-state semantics. When Mneme is unavailable, operations must fail visibly and explicitly rather than silently degrading into local JVM memory.

**Scope**
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

**Done When**
- All 8 identified fallback `ConcurrentHashMap` fields and their associated read, write, delete, search, and seeding fallback branches are deleted.
- Missing RemoteCacheManager, unstarted Hot Rod client, or missing named cache instances cause explicit runtime failures rather than fallback persistence.
- `ModuleStatusService` in all three modules is verified and documented as legitimate process-local operational state.
- Focused unit and service tests in `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass with mocked and configured cache fixtures.
- Full architecture suite (`*ArchitectureTest`) in `paradeigma-test` passes without regression.

**Technical Design**

**Decisions**
- **Decision: Throw explicit runtime/cache exceptions on missing or failed RemoteCache / not catching and writing to local maps**
    - *Rationale*: ADR-019 dictates that loss of Mneme distributed working state must be visible as loss of capability. Swallowing cache failures or returning JVM-local data creates split-brain state across cluster nodes. Standard exceptions (such as `IllegalStateException` for uninitialized/unavailable cache or propagating `CacheException`) communicate failure cleanly without custom framework bloat.
- **Decision: Retain lazy Hot Rod client producer initialization / not enforcing eager startup crash**
    - *Rationale*: `HotRodClientProducer` creates `new RemoteCacheManager(..., false)` if the cluster is unreachable during bootstrap. Retaining lazy startup allows processes to start in constrained environments while ensuring individual operations fail explicitly when invoking cache calls without an active connection.
- **Decision: Retain `localFallbackCache` in `ModuleStatusService` / not converting to fail-closed distributed lock**
    - *Rationale*: `ModuleStatusService` tracks transient local telemetry and readiness snapshots. It is not workflow, task, or clinical working state. Retaining its local snapshot satisfies the requirement to avoid removing legitimate process-local state.

**Approach & Touches**
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

**Nuances / Risks / Corners**
- **Test Fixture Mocks**: Existing service tests (`PraxisServiceTest`, `TaskCacheServiceTest`, `MessageQueueServiceTest`, `FhirCacheServiceTest`, `TaskServiceTest`, `ProvenanceServiceTest`) constructed services with no arguments (`null` `RemoteCacheManager`) and asserted on the local fallback behavior. Every test must be updated to provide a mocked `RemoteCacheManager` returning a mocked `RemoteCache` for standard positive cases, plus explicit tests for uninitialized/disconnected manager scenarios.
- **Null Safety vs Explicit Failure**: Valid domain misses (e.g. key not found in cache) must return `null` / `Optional.empty()` normally, whereas infrastructure failure (RemoteCache unavailable or Hot Rod communication exception) must throw an explicit exception.
- **Default Seeding Paths**: Methods like `MessageQueueService.seedDefaultQueues()` and `TaskSequenceCacheService.ensureDefaultSequences()` must interact with the remote cache or fail gracefully without populating a hidden in-memory store.

**Testing**

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

**Assumptions & Open Questions**

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

**Delivery Steps**

**✓ Step 1: Remediate Presentation Tier Fallbacks in Iris BEFE**  
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

**✓ Step 2: Remediate Gateway & Ingress Fallbacks in Pylai MLLP Base**  
Goal: Remove process-local fallback stores from `DefaultTaskService` and `DefaultProvenanceService`, requiring active RemoteCache for Task and Provenance state.  
Scope: `pylai/pylai-mllp-base` (`DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskServiceTest.java`, `ProvenanceServiceTest.java`).  
Acceptance Criteria:
- [ ] `taskStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultTaskService.java`.
- [ ] `provenanceStore` `ConcurrentHashMap` and fallback read/write branches removed from `DefaultProvenanceService.java`.
- [ ] `DefaultTaskService` and `DefaultProvenanceService` throw explicit runtime exceptions on create, update, delete, search, count, or clear when RemoteCache is unavailable.
- [ ] `DefaultModuleStatusService.java` in `pylai-mllp-base` verified and documented as retained process-local state.
- [ ] `TaskServiceTest` and `ProvenanceServiceTest` refactored to use mocked RemoteCacheManager/RemoteCache, verifying remote success, explicit failure when unavailable, and absence of local fallback state.
  Verification: `mvn test -pl pylai/pylai-mllp-base -am -Dtest="TaskServiceTest,ProvenanceServiceTest,ModuleStatusServiceTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

**✓ Step 3: Remediate Workflow, Activity & Engine Fallbacks in Energeia**  
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

**✓ Step 4: Full Test Suite, Architecture Invariant Verification, and Final Report**  
Goal: Execute full test verification across all affected modules, validate ArchUnit architectural guardrails, and compile the final Step 03 report.  
Scope: Full repository test verification (`iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, `energeia-ponos`, `paradeigma-test`).  
Acceptance Criteria:
- [ ] All unit and service test suites across `iris-befe`, `pylai-mllp-base`, `energeia-erga`, `energeia-praxis`, and `energeia-ponos` pass without error.
- [ ] ArchUnit suite in `paradeigma/paradeigma-test` passes without regression (`*ArchitectureTest`).
- [ ] Final report compiled covering all 12 items specified in the parent task (candidate inspection, fallback fields removed, fallback branches removed, post-change failure semantics, retained local maps, lazy Hot Rod behavior assessment, tests added/modified, and guardrail confirmations).
- [ ] Confirmed zero direct Mnemosyne read/write paths introduced, zero Task 08/09/10 leakage, and zero architecture redesign of Petasos, Mnemosyne, Kleio, or Themis.
  Verification: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Summary**
- Completed Task 07 Step 03 by removing production JVM-local Mneme fallback behavior from the targeted Iris, Pylai, and Energeia services so distributed working-state loss now fails visibly instead of degrading silently.
- Retained only legitimate process-local operational telemetry in the three `ModuleStatusService` implementations; no direct Mnemosyne paths or Task 08/09/10 redesign work was introduced.

**Changes**
- Removed the fallback fields/branches from `FhirCacheService`, `TaskSequenceCacheService`, `DefaultTaskService`, `DefaultProvenanceService`, `TaskCacheService`, `PragmaCacheService`, `PraxisService`, and `MessageQueueService`, replacing them with explicit cache-availability failures (`IllegalStateException`) and remote-only behavior.
- Adjusted `PragmaCacheService` to resolve caches lazily for practical recovery after startup outages, and fixed `TaskCacheService.savePragma(...)` so Mneme unavailability is no longer swallowed as false success.
- Added/refactored focused tests across `iris-befe`, `pylai-mllp-base`, `energeia/praxis`, and `energeia/ponos` to prove remote success, explicit failure on unavailability, no local fallback reads/writes, and recovery where practical.

**Verification**
- Passed targeted module suites for `iris-befe`, `pylai-mllp-base`, and `energeia/praxis` + `energeia/ponos`, plus the full `*ArchitectureTest` ArchUnit suite in `paradeigma/paradeigma-test`.
- Confirmed only the three `ModuleStatusService` local maps remain, documented as legitimate transient local telemetry rather than Mneme substitutes.

