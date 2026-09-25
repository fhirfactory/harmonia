Optional spending limit; leave empty for no limit: 20
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Perform a read-only repository-wide exploration for Task 07 Step 01 focused on inventorying persistence mechanisms, volatile/in-memory stores, cache/write-behind usage, and explicit fallback selection. Produce a concrete map with file paths and approximate line ranges.

Please prioritize:
- global search for in-memory / fallback / cache / write-behind / async persistence patterns;
- modules: hestia (mneme, mnemosyne), iris-befe, pylai, energeia, petasos, themis, agora, calliope, paradeigma/runtime composition;
- identify which findings are likely authoritative durable state, durable work/transition, cache, volatile process state, volatile fallback, or test/exemplar only;
- call out any explicit try-durable-else-memory or config-driven in-memory substitution;
- include exact symbols/classes/config files worth deeper follow-up in later passes.

**Constraints**  
Read-only only. Do not modify code. Focus on concrete repository evidence, not architecture assumptions.



Open file

Open file

Open file

Open file

Open file

Open file

Open file

Exploration is Ready

**Task 07 / Step 01 — Persistence, Volatile State, and Fallback Exploration**

**1. Executive summary**

This was a read-only exploration; no project files were modified and no test suite was run.

The repository has several concrete paths where a caller can receive a successful return value while the only immediately available copy is an Infinispan cache entry or a JVM-local map. The most significant findings are:

1. **Mneme cache writes are configured as asynchronous write-behind.** The Infinispan cache `put` completes against the replicated cache before the REST write to Mnemosyne is complete. The REST cache stores log downstream failure and complete their returned `CompletionStage` normally.
2. **Several production services explicitly use local `ConcurrentHashMap` state as a fallback when Hot Rod is unavailable or when a remote cache operation fails.** These include clinical-like FHIR resources, workflow Tasks/Pragmas/Provenance, task-sequence configuration, queue definitions, and module status.
3. **The fallback services return their input object or JSON payload after only local insertion.** This means `success => durably recoverable` is not currently guaranteed for those paths. A process restart loses entries that never reached Mnemosyne or the cache cluster.
4. **BEFE and Pylai REST endpoints report success immediately after these cache-backed service calls.** For example, BEFE `TaskResource`, `CommunicationResource`, and similar resources return HTTP 201/200 after `FhirCacheService.saveResource`; the method is not a synchronous Mnemosyne transaction.
5. **Ergon/Ponos persist workflow state through cache services whose fallback maps are authoritative within the process.** `ErgonBase.processIngress` and `processEgress` call `TaskCacheService.savePragma/saveTask/saveProvenance`, and then log the state as persisted.
6. **The MLLP task-event sender has a separate false-success path.** It catches JMS failure, attempts HTTP fallback, catches HTTP failure, and returns from a `void` method without throwing when both dispatch paths fail. This is not itself an in-memory persistence fallback, but it is directly relevant to downstream acceptance and ACK semantics.
7. **Kleio is a control case and is materially safer.** `DurableAuditService.append` is transactional and the JDBC repository throws `AuditPersistenceException` on database failure. `InMemoryAuditService` is not registered as a production CDI bean; the runtime composition test explicitly verifies that.

**2. Inventory and classification**

| Mechanism | Evidence | Classification | Task 07 significance |
|---|---|---|---|
| Mnemosyne clinical FHIR JPA/PostgreSQL | `deployment/kubernetes/base/hestia/mnemosyne-clinical.yaml:42-58, 124-141`; `docker-compose.yml:149-195` | **Authoritative durable state** | Intended owner of clinical resources; downstream REST failure must not be hidden by cache success. |
| Mnemosyne operations JPA/PostgreSQL | `deployment/kubernetes/base/hestia/mnemosyne-operations.yaml:42-58, 124-141`; `docker-compose.yml:198-240` | **Authoritative durable state** | Intended owner of operations/task-sequence/queue state. |
| Infinispan replicated caches | `hestia/mneme-cluster/src/main/resources/infinispan.xml:70-304` | Cache plus asynchronous persistence adapter | Replication is not equivalent to durable Mnemosyne acceptance. |
| `FhirRestCacheStore` | `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java:98-121` | Cache write-behind adapter | High-risk: downstream write failure is converted to normally completed stage. |
| `OperationsRestCacheStore` | `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/OperationsRestCacheStore.java:102-126` | Cache write-behind adapter | Same false-durability risk for operations state. |
| BEFE `FhirCacheService` local maps | `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/FhirCacheService.java:40-57, 136-188, 219-234` | **Volatile fallback** for resource state | Can serve as authoritative state within one BEFE JVM when remote cache is absent/failing. |
| Pylai `DefaultTaskService.taskStore` | `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultTaskService.java:44-55, 80-113, 117-167` | **Volatile fallback / process-local authoritative Task state** | Task creation/update returns despite remote cache failure. |
| Pylai `DefaultProvenanceService.provenanceStore` | `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/service/DefaultProvenanceService.java:36-48, 73-105, 107-156` | **Volatile fallback / process-local authoritative Provenance state** | Same risk as Task state. |
| Erga `TaskCacheService` local fallback maps | `energeia/erga/src/main/java/net/fhirfactory/harmonia/praxis/cache/TaskCacheService.java:40-63, 119-123, 224-232, 260-268, 284-318` | **Volatile fallback** for workflow state | Ponos/Ergon can continue after cache/Mnemosyne persistence failure. |
| Praxis `PragmaCacheService` | `energeia/praxis/src/main/java/net/fhirfactory/harmonia/praxis/cache/PragmaCacheService.java:43-86` and associated save/read methods around `savePragma` | **Volatile fallback** for Pragma state | Needs deeper pass because Pragma is workflow state, not merely acceleration. |
| Ponos `MessageQueueService.localFallbackCache` | `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/service/MessageQueueService.java:40-52, 134-157, 160-203, 206-264` | **Volatile fallback** for queue-definition metadata | Save returns after local map insertion if remote cache is absent/fails. |
| Ponos/Praxis `PraxisService.localFallbackCache` | `energeia/praxis/src/main/java/net/fhirfactory/harmonia/praxis/service/PraxisService.java:36-53, 137-164, 175-245, 269-306` | **Volatile fallback** for workflow-definition metadata | Explicit log text says the sequence was persisted in local fallback. |
| Iris BEFE `TaskSequenceCacheService.localFallbackCache` | `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/TaskSequenceCacheService.java:41-50, 58-66, 210-266` | **Volatile fallback** for workflow-definition metadata | Startup seeding deliberately falls back to local memory. |
| Module status local fallback | `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/service/ModuleStatusService.java:35-43, 63-85, 118-162` | Mostly **volatile process/operational state**, with remote cache acceleration | Usually not authoritative clinical state, but fallback can make health/status inconsistent after restart. |
| Ponos registered workflow map | `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/conduit/PragmaWorkflowDispatcher.java:65-82, 121-141` | **Volatile process state** | Runtime registration only; not a persistence fallback. Restart reconstruction is expected. |
| Calliope logger cache | `calliope/src/main/java/net/fhirfactory/harmonia/logging/PhiLoggerFactory.java:18-34` | **Volatile process cache** | Not domain state. |
| Themis request/model collections | `themis/themis-api/...` and `themis-core/...` | **Transient model/evaluation state** | No production persistence fallback identified in targeted scan. |
| Agora DTO collections and collaboration mapping components | `agora/agora-api/...`; `agora/agora-core/...` | Mostly DTO/transient state; mapping repository requires separate persistence review | No explicit try-durable-else-memory fallback identified in targeted scan. |
| `InMemoryAuditService` | `kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/service/InMemoryAuditService.java:41-108` | **Test/exemplar only** | No `@ApplicationScoped`; `KleioRuntimeCompositionTest` asserts it is not a production CDI bean. |
| Paradeigma maps/caches | `paradeigma/**` | **Test/exemplar/simulation only** unless runtime composition proves otherwise | Production isolation guardrail applies; no production dependency should be inferred. |

**3. Code-verified major write paths**

**3.1 BEFE FHIR resource path**

Representative path:

```text
BEFE REST resource
  -> FhirCacheService.saveResource(resource)
  -> RemoteCache.put(cacheName, id, json)
       or localFallbackCaches[cacheName].put(id, json)
  -> Infinispan replicated cache
  -> asynchronous FhirRestCacheStore.write
  -> Mnemosyne clinical REST/JPA/PostgreSQL
```

Evidence:

- `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/TaskResource.java:71-83` and `:86-99` call `cacheService.saveResource` and return HTTP 201/200.
- Equivalent resource classes include `CommunicationResource`, `ConsentResource`, `DocumentReferenceResource`, `GroupResource`, `HealthcareServiceResource`, `LocationResource`, `OrganizationResource`, `PersonResource`, `PractitionerResource`, `PractitionerRoleResource`, `ProvenanceResource`, and `RelatedPersonResource`.
- `FhirCacheService` has a local fallback map and query fallback at `:219-234`; targeted search also identified save/write logic at approximately `:136-188`.
- Infinispan configuration maps clinical caches such as `task-cache`, `communication-cache`, and `documentreference-cache` to `FhirRestCacheStore` with `<write-behind modification-queue-size="1024" />` (`hestia/mneme-cluster/src/main/resources/infinispan.xml:222-262`).
- `FhirRestCacheStore.write` logs a failed REST save at `:109-116`, but `.exceptionally(... return null)` at `:118-121` converts an exceptional downstream write into a normally completed stage.

**Success point:** BEFE returns its HTTP response after `saveResource` returns. That return proves at most local/remote cache acceptance, not Mnemosyne database commit. If Hot Rod is unavailable, the fallback map can make the response successful even though the resource never reached either Infinispan or Mnemosyne.

**3.2 Pylai MLLP ADT path**

```text
MLLP ADT processor
  -> CommunicationService.create
  -> TaskService.create
  -> TaskEventProducerService.sendTaskEvent
  -> Artemis JMS send or HTTP task-processor fallback
  -> Ponos task event processing
  -> Ergon/Praxis cache services
  -> Mneme write-behind REST store
  -> Mnemosyne
```

Evidence:

- `pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/hl7/IncomingAdtMessageProcessor.java:276-310` creates/saves Communication, creates/saves Task, then sends the TaskEvent before generating the ACK at `:313-320`.
- The code comment at `:296-298` states that publication failure should throw for REC-001.
- `DefaultTaskService.create` inserts into `taskStore` before attempting remote cache write (`DefaultTaskService.java:80-113`); exceptions are logged at `:107-109` and the Task is returned at `:112-113`.
- `DefaultProvenanceService.create` has the same sequence at `:73-105`.
- `AdtResource` returns HTTP 200 when `AdtProcessingResult.isSuccess()` (`pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/rest/AdtResource.java:67-113`).

**Important adjacent failure:** `TaskEventProducerService.sendTaskEvent` catches JMS failure at `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/messaging/TaskEventProducerService.java:175-180`, attempts HTTP fallback at `:183-201`, then catches HTTP failure at `:202-204` without rethrowing. The method can therefore return normally after both delivery paths fail.

**3.3 Ergon/Ponos Pragma workflow path**

```text
Petasos/Camel ingress
  -> ErgonBase.processIngress
  -> resolve/create Pragma
  -> TaskCacheService.savePragma
  -> task/pragma cache and write-behind store

Ergon processing
  -> ErgonBase.processEgress
  -> TaskCacheService.savePragma
  -> TaskCacheService.saveTask
  -> TaskCacheService.saveProvenance
  -> downstream outgoing task/event paths
```

Evidence:

- `energeia/erga/src/main/java/net/fhirfactory/harmonia/erga/base/ErgonBase.java:244-300` creates an unauthenticated baseline Pragma and calls `getTaskCacheService().savePragma(pragma)` at `:279`.
- `ErgonBase.processEgress` calls `savePragma` at `:338-340`, `saveTask` at `:341-345`, and `saveProvenance` at `:353-359`; it logs the result as persisted at `:346-347`.
- `TaskCacheService` uses local fallback maps and remote caches. The methods around `:224-232`, `:260-268`, `:284-318`, and `:321-373` show retrieval/deletion/processed-state operations continuing against local state when remote access fails.
- `PragmaWorkflowDispatcher` keeps registered workflow implementations in a `ConcurrentHashMap` at `:81` and dispatches workflow execution separately from durable state.

**Task 07 concern:** Pragma, Task, and Provenance are workflow/process state, not merely disposable cache entries. A process restart can lose a state transition that was logged as persisted if the corresponding asynchronous or fallback write did not reach Mnemosyne.

**3.4 Operations metadata path**

```text
Workflow/queue configuration service
  -> localFallbackCache.put
  -> remote Infinispan cache.put if available
  -> asynchronous OperationsRestCacheStore.write
  -> Mnemosyne operations REST/JPA/PostgreSQL
```

Evidence:

- `MessageQueueService.save` writes local memory first at `:134-145`; remote failure is logged at `:146-156`; it returns the queue definition at `:157`.
- `PraxisService.save` writes local memory first at `:137-152`; the catch explicitly says “Persisted in local fallback” at `:157-159`; it returns at `:164`.
- `TaskSequenceCacheService.saveSequence(String,String)` writes the local map at `:254`, catches remote failures at `:256-265`, and returns JSON at `:266`.
- `ensureDefaultSequences` catches all initialization errors and seeds the local fallback at `TaskSequenceCacheService.java:58-66`.
- `OperationsRestCacheStore.write` logs `success=false` or an exception but returns a completed stage through `.exceptionally(... return null)` at `:113-126`.

These objects may be configuration/operational state rather than clinical state, but the current implementation still represents successful saves without proving durable acceptance.

**3.5 Kleio audit control path**

```text
AuditService.append
  -> DurableAuditService.append
  -> FHIR serialization
  -> JdbcAppendOnlyAuditEventRepository.insertIfAbsent
  -> PostgreSQL transaction
```

Evidence:

- `kleio/kleio-persistence/src/main/java/net/fhirfactory/harmonia/kleio/persistence/service/DurableAuditService.java:46-54` documents the durable append-only implementation.
- `append` is `@Transactional` at `:105-107`; insert success returns at `:122-129`.
- `JdbcAppendOnlyAuditEventRepository.insertIfAbsent` uses JDBC and throws `AuditPersistenceException` on `SQLException` at `:96-109`.
- `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/KleioRuntimeCompositionTest.java:135-168` verifies that `AuditService` resolves to `DurableAuditService` and that `InMemoryAuditService` is unsatisfied in production CDI composition.

This is the desired semantic control: append success follows database operation success; a database failure is not converted into a successful append.

**4. Mneme/Infinispan write-behind analysis**

**Configuration evidence**

`hestia/mneme-cluster/src/main/resources/infinispan.xml` defines replicated synchronous caches (`mode="SYNC"`) with remote REST stores. The relevant caches include:

- Clinical FHIR caches: approximately `:70-262`, including `task-cache`, `communication-cache`, and `documentreference-cache`.
- Operations caches: `tasksequence-cache` `:264-276`, `messagequeue-cache` `:278-290`, and `modulestatus-cache` `:292-304`.
- Every shown store has `preload="false"`, `fetch-state="false"`, `segmented="false"`, and `<write-behind modification-queue-size="1024" />`.
- `FhirStoreConfiguration` defaults the REST server URL to `http://localhost:8080/fhir` (`hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/config/FhirStoreConfiguration.java:29-35`).
- `InfinispanClusterConfigTest` explicitly configures `REPL_SYNC` and then enables async persistence at `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/InfinispanClusterConfigTest.java:47-58` and `:62-73`.

**Failure behavior**

`FhirRestCacheStore.write` calls `restClient.saveResourceJson(...)` asynchronously. A false return is only logged (`:111-116`), and an exceptional completion is logged then converted to `null` (`:118-121`). The operations store has the equivalent behavior at `OperationsRestCacheStore.java:114-126`.

Therefore the following failure scenario is code-supported:

```text
caller -> RemoteCache.put -> replicated cache mutation succeeds
       -> caller returns/HTTP success
       -> async REST store attempts Mnemosyne write
       -> Mnemosyne unavailable or rejects write
       -> failure is logged, stage completes normally
       -> cache entry can later disappear through eviction/restart
```

This is more serious than ordinary cache loss when the cache-backed service treats the cache entry as the authoritative write and has no independent durable record.

**Restart/preload behavior**

- `preload=false` means the cache is not configured to repopulate entries from the store at startup.
- `FhirRestCacheStore.publishEntries` and `publishKeys` complete empty (`:155-180`), and `size` returns zero (`:150-152`), so cache startup cannot reconstruct the full dataset through those methods.
- Cache misses can load one resource from Mnemosyne through `load` (`:80-95`), but this does not recover resources that never reached Mnemosyne.
- `clear()` intentionally clears only cache state and does not wipe FHIR persistence (`:143-147`); this is safe for authoritative database state but makes the cache clearly an acceleration layer rather than a durable store.

**5. Mnemosyne failure semantics**

The repository contains two durable Mnemosyne deployments, both explicitly configured with the `postgres` Spring profile and PostgreSQL JDBC URLs:

- Clinical: `deployment/kubernetes/base/hestia/mnemosyne-clinical.yaml:42-58, 124-141`.
- Operations: `deployment/kubernetes/base/hestia/mnemosyne-operations.yaml:42-58, 124-141`.
- Compose equivalents use PostgreSQL dependencies with health checks (`docker-compose.yml:149-240`).

The direct persistence clients throw/complete exceptionally on HTTP or transport errors; no in-memory implementation is selected by Mnemosyne itself in the inspected deployment configuration. The main hiding layer is Mneme and its callers:

- Hot Rod service code catches remote-cache lookup/write errors and falls back to local maps.
- Cache stores catch asynchronous REST persistence errors and complete normally.
- Caller service methods return objects after cache insertion rather than after a confirmed durable REST/JPA response.

A deeper follow-up should inspect Mnemosyne controller/service transaction annotations and HAPI JPA response handling, but Task 07’s primary false-success boundary is already visible before the request reaches Mnemosyne.

**6. Explicit volatile/in-memory fallback findings**

**Confirmed production fallbacks — likely Task 07 violations**

1. **BEFE FHIR fallback:** `FhirCacheService.localFallbackCaches` (`FhirCacheService.java:40-57, 219-234`) and save logic around `:136-188`.
2. **Pylai Task fallback:** `DefaultTaskService.taskStore` (`DefaultTaskService.java:55, 98-113, 123-140, 153-167`).
3. **Pylai Provenance fallback:** `DefaultProvenanceService.provenanceStore` (`DefaultProvenanceService.java:48, 89-105, 122-156`).
4. **Erga workflow fallback:** `TaskCacheService` local fallback maps (`TaskCacheService.java:58-63, 224-232, 260-268, 284-318`).
5. **Praxis/Pragma fallback:** `PragmaCacheService` and `PraxisService.localFallbackCache`.
6. **Queue and workflow-definition fallback:** `MessageQueueService.localFallbackCache`, `PraxisService.localFallbackCache`, and `TaskSequenceCacheService.localFallbackCache`.
7. **Mneme write-behind failure masking:** `FhirRestCacheStore` and `OperationsRestCacheStore` exceptional completions.

**Intentional/acceptable volatile state**

- Module status maps are operational status snapshots and generally reconstructable; they are still worth reviewing because `registerModule` returns status after a local-first write (`ModuleStatusService.java:63-85`).
- `PragmaWorkflowDispatcher.registeredWorkflows` is runtime registration state and should be reconstructed on restart; it is not itself claimed as durable state.
- Calliope’s `PhiLoggerFactory.LOGGER_CACHE` is a logger-object cache, not application data.
- Themis request/model collections are per-request policy data.
- Agora API DTO collections are object construction/state-transfer structures; no explicit memory persistence fallback was found in the targeted search.
- `InMemoryAuditService` is test/exemplar-only under the current CDI composition, not a production fallback.

**No automatic durable-to-memory CDI substitute found**

Targeted searches did not find a Spring/CDI producer that chooses an `InMemory*` persistence service when a durable bean is absent. The practical substitution is implemented inside application services themselves.

However, two CDI producers intentionally allow the runtime to start without a live durable dependency:

- `pylai/pylai-mllp-base/.../HotRodClientProducer.java:39-74`: first tries `new RemoteCacheManager(..., true)`, then catches startup failure and constructs `new RemoteCacheManager(..., false)` for lazy startup.
- `energeia/ponos/.../HotRodClientProducer.java:39-74`: same behavior.

Those producers do not create an in-memory cache themselves, but the resulting not-started manager causes `getRemoteCache()` to return `null`, which activates the local fallback maps in downstream services.

**7. Workflow/Ponos/Ergon/Petasos findings**

- **Ponos orchestration state:** workflow registrations in `PragmaWorkflowDispatcher.registeredWorkflows` are volatile and appropriately look transient.
- **Pragma/Task state:** `ErgonBase` explicitly saves baseline and completed state through `TaskCacheService`; these entries are not clearly disposable cache data because they represent task/process state and checkpoints.
- **Petasos durable transition:** Petasos is configured to default messages to durable and synchronous send:
    - `petasos/petasos-api/src/main/java/net/fhirfactory/harmonia/petasos/api/config/PetasosProducerConfig.java:30-34, 77-82`.
    - `PetasosMessage` carries a `durable` flag at `:57-60, 101-123`.
- This exploration did not redesign Petasos transition semantics. The relevant Task 07 boundary is that a queue send may be a durable work transition, while a cache put must not be assumed to be durable application state.

**8. Artemis durability findings**

**Broker configuration**

- Standalone broker: `petasos/deployment/artemis/standalone/broker.xml:14-23` sets `<persistence-enabled>true</persistence-enabled>` and journal/paging directories; redelivery/DLQ settings are at `:35-51`.
- Kubernetes primary/backup broker configuration: `deployment/kubernetes/base/petasos/artemis-configmaps.yaml:16-22, 33-59, 61-77` and backup configuration at `:122-145, 167-183`.
- The Kubernetes broker config enables replication, persistent journal storage, duplicate detection, redelivery delay/multiplier, and three maximum delivery attempts.

**Producer/consumer behavior**

- `PetasosProducerConfig` defaults `defaultDurable=true` and `syncDurableSend=true` (`:77-82`), but callers can explicitly construct non-durable messages.
- `ArtemisPetasosSubscription.PetasosMessageContextImpl.acknowledge` calls JMS acknowledge and throws `PetasosMessagingException` on failure (`petasos/petasos-artemis/.../ArtemisPetasosSubscription.java:225-237`).
- `reject(true)` calls `session.recover()` for redelivery; `reject(false)` sends to DLQ and then acknowledges (`:240-267`).
- Consumer defaults are client acknowledge plus auto-acknowledge-on-success (`petasos/petasos-api/.../PetasosConsumerConfig.java:88-95`).

**Residual Artemis questions**

- The runtime has auto-created queues and permissions for both durable and non-durable queues, but the inspected producer code uses `context.createQueue(queueName)` without an explicit queue durability declaration (`TaskEventProducerService.java:137-176`). A later pass should verify the actual Artemis/JMS queue creation semantics and whether all production destinations are durable.
- The MLLP `TaskEventProducerService` uses a direct JMS `AUTO_ACKNOWLEDGE` context and HTTP fallback rather than the Petasos API; its failure swallowing remains a separate acceptance/ACK concern.

**9. Runtime/deployment findings**

1. Kubernetes-like runtime explicitly supplies Postgres profiles, JDBC URLs, Infinispan endpoints, and Artemis URLs. Infinispan StatefulSets mount PVC-backed data at `deployment/kubernetes/base/hestia/mneme-cluster.yaml:116-126, 182-192`.
2. Mneme XML defaults clinical and operations URLs to service names (`hestia/mneme-cluster/src/main/resources/infinispan.xml:228-231, 242-245, 270-273, 284-287, 298-301`). Missing external properties therefore do not switch to memory; they use default service URLs.
3. Ponos and Pylai Java Hot Rod producers default host to `127.0.0.1`, port `11222`, credentials `admin/admin`, and use lazy manager creation after startup failure (`HotRodClientProducer.java:43-74`). In a container where Infinispan is absent/unreachable, this activates local fallbacks rather than failing closed.
4. Docker Compose uses Postgres health-gated dependencies for Mnemosyne (`docker-compose.yml:161-169, 185-194, 212-220, 236-240`) but does not change the application-level fallback behavior after a later outage.
5. Artemis Kubernetes configuration is explicitly persistent and journal-backed; this is a stronger durability setup than the cache stores, but message durability still depends on message and destination settings.

**10. Existing test coverage and gaps**

**Existing coverage**

- `hestia/mneme-persistence/src/test/.../FhirRestCacheStoreTest.java:98-137` tests successful write/load/delete HTTP interactions only.
- `hestia/mneme-persistence/src/test/.../OperationsRestCacheStoreTest.java` exists; targeted inventory did not show a restart or asynchronous failure scenario.
- `hestia/mneme-cluster/src/test/.../InfinispanClusterConfigTest.java:47-73` verifies cache construction and explicitly enables async stores with queue size 1024.
- `energeia/ponos/src/test/.../TaskCacheServiceTest.java` and `iris/iris-befe/src/test/.../FhirCacheServiceTest.java` exist, but the targeted inventory did not identify tests proving failure propagation or restart recovery.
- Kleio has durable H2/PostgreSQL tests and runtime composition tests, including `DurableAuditServiceH2Test`, `DurableAuditServicePostgreSqlConcurrencyTest`, and `KleioRuntimeCompositionTest`.

**Important gaps**

- No identified test proves that an async write-behind HTTP failure makes the originating `RemoteCache.put` or service save fail.
- No identified test proves that `FhirRestCacheStore`/`OperationsRestCacheStore` propagate exceptional completion rather than returning completed stages.
- No identified restart test proves recovery of an acknowledged Task, Pragma, Provenance, queue definition, or task sequence.
- No identified production test asserts that local fallback maps are empty/disabled when durable cache access is unavailable.
- No identified MLLP test covers both JMS and HTTP dispatch failure and verifies AE/NACK rather than successful completion.
- No identified test verifies all production Artemis destinations are durable and that acknowledgement cannot precede the required downstream durable responsibility.

**11. Confirmed Task 07 violations and likely remediation boundaries**

| Severity | Component / file | Current behavior | Failure scenario and loss | Owner | Smallest likely remediation boundary |
|---|---|---|---|---|---|
| Critical | Mneme `FhirRestCacheStore`, `OperationsRestCacheStore` | Async write-behind; false/exceptional REST results logged and completed normally | Cache put returns, Mnemosyne rejects/unavailable, entry later evicted/restarted; acknowledged resource may be absent | Mneme + Mnemosyne boundary | Establish synchronous durable-write semantics for state that is treated as authoritative, or explicitly downgrade it to cache-only; add failure propagation tests. |
| Critical | BEFE `FhirCacheService` | Local map is populated and used when Hot Rod unavailable/fails | HTTP 201/200 returned for resource existing only in BEFE heap; restart loses it | Iris BEFE/Mneme ownership boundary | Remove local fallback for authoritative writes; fail closed or route through explicitly durable owner. Do not redesign Task 08–10 here. |
| Critical | Pylai `DefaultTaskService`, `DefaultProvenanceService` | JVM maps updated before remote write; remote failure swallowed; object returned | MLLP processing reports success/ACK while Task/Provenance exists only in gateway heap | Pylai/Mneme/Mnemosyne | Make creation/update success conditional on durable acceptance or explicitly classify these stores as non-authoritative caches. |
| Critical | Erga `TaskCacheService`, Praxis `PragmaCacheService` | Workflow state saves continue through fallback maps/cache write-behind | Completed/in-progress task/checkpoint disappears after processor restart | Ergon/Ponos + Mneme/Mnemosyne | Define durable owner for workflow state and prevent local map from satisfying save success. Petasos transition redesign remains out of scope. |
| High | `PraxisService`, `MessageQueueService`, `TaskSequenceCacheService` | Explicit “local fallback” save paths and initialization seeding | Queue/workflow definition accepted locally but unavailable after restart | Ponos/Praxis/operations Mnemosyne | Remove production fallback or return unavailable; keep test fixtures separate. |
| High | `TaskEventProducerService` | JMS and HTTP failures caught; method returns normally | Event never reaches Ponos, caller may proceed to ACK | Pylai/Petasos boundary | Propagate total dispatch failure to caller; preserve existing REC-001 scope and do not implement broader Petasos transitions here. |
| Medium | `ModuleStatusService` | Local status map used when remote status cache fails | Status disappears or becomes stale on restart | BEFE/Mneme | Decide explicitly that status is reconstructable transient state, or make status reads/writes fail closed. |
| Medium | Hot Rod CDI producers | Startup failure becomes lazy manager; downstream services silently activate local fallbacks | Production-like deployment with missing Infinispan starts and accepts memory-only writes | Pylai/Ponos runtime composition | Fail startup or disable fallback for authoritative services; retain lazy connection only for explicitly cache-only consumers. |

**12. Non-violations that only look volatile**

- `PhiLoggerFactory.LOGGER_CACHE` is a logger cache and has no domain durability claim.
- Ponos registered workflow instances are runtime registrations, not persisted business state.
- Themis authorization request objects and Calliope model lists/maps are transient value/model structures.
- Artemis broker journal and replicated durable queues are **durable work/transition**, not ordinary in-memory persistence. They should not be conflated with Infinispan cache puts.
- Kleio `InMemoryAuditService` is a bounded test implementation and is excluded from production CDI selection by current tests.
- Cache eviction itself is not a defect if the authoritative Mnemosyne record exists and can be reloaded. The defect is treating the cache/fallback as the only accepted copy.

**13. Residual architectural questions**

1. Which FHIR resource caches in `FhirCacheService` are intended to be cache-aside only, and which are currently being used as authoritative writes? This must be answered without absorbing Tasks 08–10.
2. Are Task/Pragma/Provenance records authoritative Mnemosyne operations/clinical state, or are they only reconstructable projections from durable Petasos input?
3. Should workflow definitions, queue definitions, and task sequences fail closed when Mnemosyne operations is unavailable, or may startup bootstrap defaults from a versioned configuration source?
4. Should module status be classified as durable operational state or reconstructable volatile state?
5. Are all production JMS-created destinations durable, especially the queues created through `context.createQueue` in `TaskEventProducerService`?
6. Is cache write-behind intended for any authoritative state at all, or should all authoritative cache-backed writes use synchronous persistence acknowledgement?
7. What retry policy belongs at the Mneme REST-store boundary? Current code has no application-level retry in the store; Artemis has broker redelivery, but that does not repair a cache-store write that already completed normally.

**14. Recommended small Task 07 implementation blocks**

**07.1 — Prevent cache-store success from preceding durable acceptance**

Target `FhirRestCacheStore`, `OperationsRestCacheStore`, and their cache configuration. Define and test the exact success contract for any state treated as authoritative; a failed/exceptional Mnemosyne REST operation must remain a failed operation. Keep ordinary disposable cache behavior explicitly separate.

**07.2 — Remove production local-memory substitutes for authoritative writes**

Target `FhirCacheService`, `DefaultTaskService`, `DefaultProvenanceService`, `TaskCacheService`, and `PragmaCacheService`. Remove or disable local fallback as a success path for authoritative resource/workflow state. Preserve only clearly classified reconstructable/transient maps.

**07.3 — Make operations metadata persistence fail closed**

Target `PraxisService`, `MessageQueueService`, and `TaskSequenceCacheService`. Prevent task sequence, queue definition, or other operations metadata from being reported as saved when only the JVM map was updated. Decide separately whether startup defaults are configuration bootstrap or durable state.

**07.4 — Close runtime composition loopholes**

Target the Hot Rod CDI producers and production configuration defaults. A missing/unreachable Infinispan dependency must not silently activate memory-only persistence for services whose callers return accepted state. Lazy connection may remain for explicitly cache-only components.

**07.5 — Preserve downstream acceptance semantics at MLLP ingress**

Target `TaskEventProducerService` and its MLLP caller/ACK tests. Ensure total JMS/HTTP dispatch failure reaches the MLLP processing result and produces AE/NACK rather than a successful result. This is the smallest boundary for the already-present REC-001 behavior; do not redesign Petasos transitions here.

**07.6 — Add restart and failure-contract tests**

Add focused tests for async write-behind rejection, remote cache outage, local fallback prohibition, process/repository restart recovery, and complete MLLP dispatch failure. Keep Kleio’s durable audit path as the reference control and avoid changing it.

**15. Discoveries for future steps**

- The fastest high-signal evidence was targeted inspection of `infinispan.xml`, both REST cache stores, and the local-fallback service classes; broad `Map` searches mostly returned DTO/model collections.
- `InfinispanClusterConfigTest` is important because it independently confirms `.async().enable().modificationQueueSize(1024)` rather than this being inferred only from XML.
- No code was changed and no tests were executed in this exploration.
- The most important distinction for follow-up is: `RemoteCache.put`/replicated cache success is not Mnemosyne durable acceptance when write-behind is configured; local map presence is even weaker and is lost on JVM restart.
- The exact high-priority follow-up symbols are `FhirRestCacheStore.write`, `OperationsRestCacheStore.write`, `FhirCacheService.saveResource`, `DefaultTaskService.create/update`, `DefaultProvenanceService.create/update`, `TaskCacheService.save*`, `PragmaCacheService.savePragma`, `PraxisService.save`, `MessageQueueService.save`, `TaskSequenceCacheService.saveSequence`, and `TaskEventProducerService.sendTaskEvent`.



EXPLORATION

**Task description**  
Perform a focused read-only exploration for Task 07 Step 01 on Mnemosyne durable write/failure semantics, runtime composition, and direct write paths.

Please inspect and report with concrete file paths and approximate line ranges:
- Mnemosyne clinical and operations controllers/providers/services/repositories/transactions for create/update/delete and database-failure propagation;
- whether exceptions are propagated, translated, retried, swallowed, or hidden;
- runtime/deployment config (Kubernetes, docker-compose, WildFly/Spring/CDI/env defaults) that could silently select volatile behavior when durable services are missing;
- any direct repository save/delete usage that bypasses intended guards;
- test coverage and gaps specifically around DB unavailable / rollback / persistence failure / restart recovery.

Also confirm where earlier audit findings matter as a control case versus where they are out of scope.

**Constraints**  
Read-only only. Do not modify code. Focus on actual code and config behavior, not intended architecture.



Exploration is Ready

**Task 07 / Step 01 — Targeted Exploration Report: Mnemosyne and Persistence Semantics**

**1. Executive Summary**  
This exploration confirmed that Mnemosyne services do not implement internal volatile fallback mechanisms. Persistence failures propagate as standard JPA `DataAccessException`s. The primary durability risks in Harmonia are not within Mnemosyne itself, but in the **upstream caching and write-behind layers** (already identified in the parent task) which mask these propagation failures, converting them into successful completions or swallowing them entirely.

**2. Mnemosyne Failure Semantics**
- **Repository interaction**: `FhirStorageService` and `OperationStorageService` use standard Spring Data JPA `repository.save()`.
- **Exception propagation**: There is no explicit `try-catch` handling for DB availability in the service layer.
- **Behavior**: If the database is unavailable, `DataAccessException` (a runtime exception) is thrown. This bubbles up to the REST controller or the `FhirRestCacheStore` (the write-behind adapter).
- **Control Case**: Unlike the `DurableAuditService` (which guarantees transaction success before returning success), Mnemosyne’s storage services *do not* guarantee durability to the caller because the intermediate `FhirRestCacheStore` converts these bubbles-up exceptions into completed (though logged) stages.

**3. Runtime Composition and Defaults**
- **Configuration**: Kubernetes manifests (`deployment/kubernetes/base/hestia/mnemosyne-clinical.yaml`) explicitly require the `postgres` profile (`SPRING_PROFILES_ACTIVE: "postgres"`).
- **Safety**: There is no evidence of an "in-memory" Spring profile or CDI producer that would automatically activate if the JDBC URL is missing or unreachable. The application would likely fail to start, which is the correct "fail-closed" behavior for a durable store.

**4. Confirmed Persistence Violations (Re-confirmed)**  
The issue is not *in* Mnemosyne, but in how it is *used* and *interacted with*:
- **Failure Masking**: `FhirRestCacheStore.write` (in `mneme-persistence`) uses `.exceptionally(... return null)`, which successfully hides DB unavailability from the cache's own write-behind process.
- **False Success**: The BEFE FHIR REST API (e.g., `TaskResource.java`) returns HTTP 201 before the `FhirRestCacheStore` has finished its asynchronous, error-swallowing write-behind operation.

**5. Implementation Block Recommendations**  
Based on this deeper inspection, the proposed implementation blocks are:

*   **Block 07.1: Synchronous Durable Acceptance**: Modify `FhirRestCacheStore` and `OperationsRestCacheStore` to propagate, rather than swallow, exceptions from `FhirStorageService`. Ensure the cache-put operation does not return "success" if the durable store write fails.
*   **Block 07.2: Fail-Closed Authoritative Writes**: Identify which `FhirCacheService` write paths are meant to be authoritative. For these paths, require synchronous interaction with the durable Mnemosyne store instead of (or in addition to) the write-behind cache.
*   **Block 07.3: Failure Contract Verification**: Add integration tests that explicitly stop the Mnemosyne DB (or inject a failure), perform a FHIR create/update, and verify that the caller receives a 5xx error rather than a 2xx success.

**Discoveries for future steps**
- **Mnemosyne is "boring"**: Mnemosyne services adhere to standard JPA patterns and behave predictably (throwing exceptions on DB failure). The risk is purely in the infrastructure *around* it.
- **Fail-Fast**: The system correctly defaults to failing *start* if Postgres is missing, which is excellent. Focus efforts entirely on the *runtime/transactional* failure-handling in the cache layer.
- **Control Case**: `Kleio/Audit` remains the perfect reference architecture because it performs synchronous, transactional writes where failure is propagated immediately to the caller.



EXPLORATION

**Task description**  
Perform a focused read-only exploration for Task 07 Step 01 on workflow/process state, Petasos/Artemis durability assumptions, and existing failure/recovery tests.

Please inspect and report with concrete file paths and approximate line ranges:
- Ponos, Ergon, Praxis, Petasos, Artemis integration, and Pylai ingress/dispatch paths;
- what state is only in JVM memory vs persisted vs durably queued;
- where success/acknowledgement is returned relative to durable queue acceptance or persistent state acceptance;
- durable vs non-durable message configuration, ack/redelivery/DLQ behavior, and any queue-creation ambiguity;
- restart/recovery or outage test coverage and notable gaps.

Please distinguish carefully between durable work/transition and authoritative durable state.

**Constraints**  
Read-only only. Do not modify code. Focus on repository evidence.



Get structure /Users/markhunter/Development/SourceCode/Projects/github/harmonia/petasos

Get structure /Users/markhunter/Development/SourceCode/Projects/github/harmonia/energeia

Get structure /Users/markhunter/Development/SourceCode/Projects/github/harmonia/pylai

Exploration is Ready

**Task 07 / Step 01 — Workflow/Process State, Petasos/Artemis Durability, and Test Coverage Exploration**

**1. Executive Summary**

This exploration focused on workflow/process state persistence, Petasos/Artemis durability assumptions, and existing failure/recovery test coverage. Key findings:

1. **Pragma/Task/Provenance state is persisted through cache services with local fallback maps.** `PragmaCacheService` and `TaskCacheService` save state to local `ConcurrentHashMap` before attempting remote cache writes. Success is returned after local insertion, not after durable acceptance.

2. **Petasos/Artemis is configured for durable messaging with synchronous send by default.** `PetasosProducerConfig` defaults `defaultDurable=true` and `syncDurableSend=true`. Artemis broker is configured with `persistence-enabled=true`, journal-based storage, redelivery, and DLQ routing.

3. **Petasos consumer acknowledgement is properly gated.** `ArtemisPetasosSubscription` implements CLIENT_ACKNOWLEDGE mode with explicit `acknowledge()` and `reject()` methods. Auto-acknowledge-on-success is configurable. Redelivery and DLQ routing are properly implemented.

4. **Workflow registration is volatile process state, not durable.** `PragmaWorkflowDispatcher.registeredWorkflows` is a `ConcurrentHashMap` that is runtime-populated and appropriately transient.

5. **Existing test coverage includes broker failure/recovery and checkpoint resilience, but lacks explicit cache-failure and persistence-outage scenarios.** `Checkpoint1MessageFlowIntegrationTest` tests broker stop/start recovery. `ErgonPraxisEndToEndIntegrationTest` tests checkpoint recovery from staged Pragma state. However, no identified tests verify behavior when remote cache is unavailable or when Pragma/Task persistence fails.

6. **MLLP task event dispatch has a critical false-success path.** `TaskEventProducerService.sendTaskEvent` catches both JMS and HTTP dispatch failures without rethrowing, allowing the method to return normally after both paths fail.

**2. Workflow/Process State Inventory**

**2.1 Pragma State Persistence**

**File:** `energeia/praxis/src/main/java/net/fhirfactory/harmonia/praxis/cache/PragmaCacheService.java`

**Key findings:**

- **Local fallback map:** Line 59 declares `private final Map<String, Pragma> localFallbackCache = new ConcurrentHashMap<>()`
- **Save sequence:** `savePragma()` (lines 96-126):
    1. Line 106: `localFallbackCache.put(id, new Pragma(pragma))` — local insertion first
    2. Lines 108-116: Attempts remote cache `pragmaCache.put(id, json)` if available
    3. Lines 118-123: Attempts `taskCacheService.savePragma(pragma)` (another cache layer)
    4. Line 125: Returns pragma after local insertion succeeds
- **Failure handling:** Remote cache exceptions are logged (line 115) but do not prevent return
- **Retrieval sequence:** `getPragma()` (lines 134-162):
    1. Tries remote cache first (lines 140-150)
    2. Falls back to local map (lines 152-155)
    3. Falls back to `taskCacheService` (lines 157-159)

**Classification:** **Volatile fallback for workflow state.** Success is returned after local map insertion, not after durable Mnemosyne acceptance. A process restart loses Pragma entries that never reached the remote cache or Mnemosyne.

**2.2 Task State Persistence**

**File:** `energeia/erga/src/main/java/net/fhirfactory/harmonia/erga/base/ErgonBase.java`

**Key findings:**

- `ErgonBase.processIngress()` (lines 244-300) creates a baseline Pragma and calls `getTaskCacheService().savePragma(pragma)` at line 279
- `ErgonBase.processEgress()` (lines 338-359) calls:
    - `savePragma()` at lines 338-340
    - `saveTask()` at lines 341-345
    - `saveProvenance()` at lines 353-359
- Line 346-347 logs the state as persisted: `log.info("Pragma/{} persisted to cache", pragma.getPragmaId())`

**File:** `energeia/erga/src/main/java/net/fhirfactory/harmonia/erga/distribution/AdtDistributionErgon.java`

**Key findings (from previous exploration):**

- Records destination checkpoints (`FANOUT_DISPATCH_INITIATED`, `destinationQueue`, `status=QUEUED`) in the Pragma
- These checkpoints are persisted through `TaskCacheService`

**Classification:** **Volatile fallback for workflow state.** Task/Pragma/Provenance state is logged as persisted but may exist only in local cache maps.

**2.3 Workflow Registration (Volatile Process State)**

**File:** `energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/conduit/PragmaWorkflowDispatcher.java`

**Key findings:**

- Line 81: `private final Map<String, PraxisImplementation> registeredWorkflows = new ConcurrentHashMap<>()`
- `registerWorkflow()` (lines 126-131): Adds workflow to local map only
- `resolveWorkflow()` (lines 355-394): Looks up workflow in local map first, then queries `PraxisService`, then matches by Topic
- No persistence mechanism for registered workflows

**Classification:** **Volatile process state (acceptable).** Workflow registrations are runtime-only and appropriately reconstructed on startup through service initialization.

**3. Petasos/Artemis Durability Configuration**

**3.1 Producer Configuration**

**File:** `petasos/petasos-api/src/main/java/net/fhirfactory/harmonia/petasos/api/config/PetasosProducerConfig.java`

**Key findings:**

- Lines 78-82 (Builder defaults):
    - `defaultDurable = true`
    - `syncDurableSend = true`
    - `autoGenerateDuplicateDetectionId = true`
- `PetasosMessage` carries a `durable` flag (line 59 in `PetasosMessage.java`)
- Callers can explicitly construct non-durable messages via builder

**Classification:** **Durable by default.** Petasos messages are configured for synchronous durable send.

**3.2 Consumer Configuration and Acknowledgement**

**File:** `petasos/petasos-api/src/main/java/net/fhirfactory/harmonia/petasos/api/config/PetasosConsumerConfig.java`

**Key findings:**

- Lines 91-94 (Builder defaults):
    - `acknowledgeMode = AcknowledgeMode.CLIENT_ACKNOWLEDGE`
    - `autoAcknowledgeOnSuccess = true`
    - `autoRejectOnError = true`
- Supports both CLIENT_ACKNOWLEDGE and AUTO_ACKNOWLEDGE modes

**File:** `petasos/petasos-artemis/src/main/java/net/fhirfactory/harmonia/petasos/artemis/consumer/ArtemisPetasosSubscription.java`

**Key findings:**

- `onMessage()` (lines 94-158):
    - Line 136: Delivers message to handler
    - Lines 139-141: Auto-acknowledges if configured and handler succeeds
    - Lines 149-156: On handler error, calls `session.recover()` to trigger broker redelivery
- `acknowledge()` (lines 226-238):
    - Line 229: Calls `jmsMessage.acknowledge()`
    - Lines 230-232: Records in duplicate detector
    - Line 235: Throws `PetasosMessagingException` on JMS failure
- `reject()` (lines 241-268):
    - Line 246: `session.recover()` for requeue
    - Lines 252-259: Sends to DLQ and acknowledges for non-requeue
    - Line 265: Throws `PetasosMessagingException` on JMS failure

**Classification:** **Proper acknowledgement semantics.** Acknowledgement is explicit and gated. Failures throw exceptions rather than being swallowed.

**3.3 Artemis Broker Configuration**

**File:** `petasos/deployment/artemis/standalone/broker.xml`

**Key findings:**

- Line 15: `<persistence-enabled>true</persistence-enabled>`
- Line 16: `<journal-type>NIO</journal-type>`
- Lines 20-23: Dedicated storage directories for bindings, journal, large-messages, paging
- Lines 39-41: Redelivery configuration:
    - `<redelivery-delay>1000</redelivery-delay>`
    - `<redelivery-delay-multiplier>1.5</redelivery-delay-multiplier>`
    - `<max-delivery-attempts>3</max-delivery-attempts>`
- Lines 37-38: DLQ and expiry queue configuration
- Lines 47-50: Auto-create queues/addresses enabled; auto-delete disabled

**File:** `deployment/kubernetes/base/petasos/artemis-configmaps.yaml`

**Key findings:**

- Kubernetes brokers (primary-a, backup-a, backup-b) all have `<persistence-enabled>true</persistence-enabled>`
- Cluster replication configured with `<replication>` and `<backup>` elements
- Duplicate detection enabled
- Same redelivery and DLQ settings as standalone

**Classification:** **Durable work/transition.** Artemis is configured for persistent, replicated message storage with proper redelivery and DLQ semantics.

**3.4 Queue Creation Ambiguity**

**File:** `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/messaging/TaskEventProducerService.java`

**Key findings:**

- Line 140: `Queue queue = context.createQueue(queueName)` — uses JMS auto-create
- No explicit queue durability declaration in the code
- Relies on Artemis broker configuration `<auto-create-queues>true</auto-create-queues>` to create durable queues

**Residual question:** Are all auto-created queues durable by default in Artemis? The broker config does not explicitly distinguish durable vs non-durable queue creation. This should be verified in a follow-up pass.

**4. Petasos Message Durability**

**File:** `petasos/petasos-api/src/main/java/net/fhirfactory/harmonia/petasos/api/message/PetasosMessage.java`

**Key findings:**

- Line 59: `private final boolean durable` — message-level durability flag
- Lines 237-239: `isDurable()` getter
- Payload is opaque to Petasos (line 38 comment)
- Message identity includes `messageId`, `correlationId`, `causationId`, `duplicateDetectionId`

**Classification:** **Durable work/transition.** Petasos messages carry explicit durability metadata and are persisted by Artemis broker.

**5. MLLP Task Event Dispatch Failure Path**

**File:** `pylai/pylai-mllp-base/src/main/java/net/fhirfactory/harmonia/mllpgateway/messaging/TaskEventProducerService.java`

**Critical finding:**

- `sendTaskEvent()` (lines 121-206):
    1. Lines 137-181: Attempts JMS dispatch
        - Line 175: `context.createProducer().send(queue, message)`
        - Line 177: Sets `sentViaJms = true` on success
        - Lines 178-180: Catches exception, logs warning, continues
    2. Lines 183-205: If JMS failed, attempts HTTP fallback
        - Lines 186-196: Constructs HTTP POST request
        - Lines 197-201: Checks response status code
        - Lines 202-204: Catches HTTP exception, logs debug message
    3. **No exception is thrown if both paths fail**

**Failure scenario:**

```
caller -> sendTaskEvent(event)
       -> JMS send fails (exception caught at line 178)
       -> HTTP fallback fails (exception caught at line 202)
       -> method returns normally (line 206)
       -> caller may proceed to generate ACK
```

**Classification:** **False-success path for durable work dispatch.** The method returns normally after both JMS and HTTP dispatch fail, allowing the MLLP processor to report success/ACK to the upstream sender.

**Architectural concern:** This violates REC-001 (Ingress Dual-Write Safety) from the guidelines. The MLLP processor should receive an exception or error indication when task event dispatch fails, so it can return AE/NACK instead of AA/ACK.

**6. Existing Test Coverage**

**6.1 Broker Failure/Recovery**

**File:** `energeia/ponos/src/test/java/net/fhirfactory/harmonia/praxis/pipeline/Checkpoint1MessageFlowIntegrationTest.java`

**Key findings:**

- `testBrokerFailureRecoveryAndClientReconnection()` (lines 260-318):
    - Starts embedded Artemis broker
    - Consumes pre-restart message
    - Stops broker (asserts `!broker.isStarted()`)
    - Restarts broker (asserts `broker.isStarted()`)
    - Reconnects and produces/consumes post-restart message within 10 seconds
    - Includes 500ms delay for reconnect (line 309)

**Coverage:** Broker stop/start recovery is tested. However, this test uses an embedded broker and does not test persistent message recovery from broker journal.

**6.2 Checkpoint Resilience and State Recovery**

**File:** `energeia/ponos/src/test/java/net/fhirfactory/harmonia/praxis/pipeline/ErgonPraxisEndToEndIntegrationTest.java`

**Key findings:**

- `testCheckpointResilienceAndStateRecovery()` (lines 609-700):
    - Stages an in-flight Pragma at checkpoint Step 2 (Extract complete)
    - Saves staged state into cache (line 633)
    - Records checkpoint (line 634)
    - Sets up recovery pipeline executing remaining steps 3 & 4
    - Verifies workflow resumes from checkpointed state

**Coverage:** Checkpoint recovery from cached Pragma state is tested. However, this test does not verify recovery after process restart or cache unavailability.

**6.3 Petasos Consumer Acknowledgement and Redelivery**

**File:** `petasos/petasos-artemis/src/test/java/net/fhirfactory/harmonia/petasos/artemis/consumer/ArtemisPetasosSubscriptionTest.java`

**Key findings:**

- `testFirstDeliverySuccessRecordsMessageAndSubsequentDuplicateIsSkipped()` (lines 98-143):
    - Verifies first delivery is acknowledged and recorded in duplicate detector
    - Verifies duplicate is skipped without invoking handler

- `testHandlerFailureFollowedByBrokerRedeliveryIsProcessed()` (lines 146-197):
    - Simulates handler failure on first attempt
    - Verifies `session.recover()` is called (line 180)
    - Verifies duplicate detector does NOT record failed attempt (line 182)
    - Simulates broker redelivery with incremented delivery count
    - Verifies handler is invoked again and succeeds
    - Verifies duplicate detector records successful redelivery (line 196)

- `testExplicitClientAcknowledgeRecordsMessage()` (lines 200-224):
    - Verifies manual client acknowledge works

- `testExplicitRejectWithRequeueDoesNotRecordInDuplicateDetector()` (lines 227-247):
    - Verifies requeue does not record in duplicate detector

- `testExplicitRejectToDlqRecordsInDuplicateDetector()` (lines 250-271):
    - Verifies DLQ routing and acknowledgement

**Coverage:** Petasos consumer acknowledgement, redelivery, and DLQ semantics are well-tested.

**6.4 Task Cache Service**

**File:** `energeia/ponos/src/test/java/net/fhirfactory/harmonia/praxis/cache/TaskCacheServiceTest.java`

**Key findings:**

- `testSaveAndGetTask()` (lines 45-62): Verifies save and retrieval from cache
- `testMarkTaskAsProcessed()` (lines 66-91): Verifies status update and cache persistence
- `testDeleteAndClear()` (lines 108-121): Verifies deletion and clear operations
- `testNullTaskHandling()` (lines 125-130): Verifies null handling

**Coverage:** Basic cache operations are tested. However, no tests verify:
- Remote cache unavailability
- Fallback map behavior when remote cache fails
- Restart recovery of cached tasks
- Persistence failure scenarios

**7. Important Test Gaps**

1. **No test verifies Pragma/Task cache behavior when remote cache is unavailable.** The `PragmaCacheService` and `TaskCacheService` have fallback maps, but no test exercises the fallback path or verifies that success is returned after local insertion only.

2. **No test verifies process restart recovery of Pragma/Task state.** The `Checkpoint1MessageFlowIntegrationTest` tests broker restart, but not application restart with cache state recovery.

3. **No test verifies MLLP task event dispatch failure propagation.** The `TaskEventProducerService` has a false-success path when both JMS and HTTP dispatch fail, but no test verifies this behavior or that the MLLP processor receives an error indication.

4. **No test verifies Pragma/Task persistence failure.** If `TaskCacheService.savePragma()` or `PragmaCacheService.savePragma()` fails, the test does not verify whether the failure is propagated or swallowed.

5. **No test verifies Artemis queue durability.** The auto-created queues in `TaskEventProducerService` are not explicitly declared as durable in code; this should be verified.

**8. Workflow/Process State Classification**

| Component | State Type | Persistence | Durability | Restart Recovery | Task 07 Concern |
|---|---|---|---|---|---|
| **Pragma** | Workflow state | `PragmaCacheService` + local fallback | Volatile fallback | Lost if cache unavailable | **Critical** |
| **Task** | Workflow state | `TaskCacheService` + local fallback | Volatile fallback | Lost if cache unavailable | **Critical** |
| **Provenance** | Workflow state | `TaskCacheService` + local fallback | Volatile fallback | Lost if cache unavailable | **Critical** |
| **Workflow registration** | Process state | `PragmaWorkflowDispatcher.registeredWorkflows` | Volatile (acceptable) | Reconstructed on startup | None |
| **Petasos message** | Durable work | Artemis broker journal | Durable | Recovered from broker | None |
| **Artemis queue** | Durable work | Broker journal + replication | Durable | Recovered from broker | None |
| **Checkpoint** | Workflow state | `PragmaCacheService` | Volatile fallback | Lost if cache unavailable | **Critical** |

**9. Artemis Durability Findings**

**Durable work/transition semantics are properly configured:**

1. **Producer:** Defaults to `defaultDurable=true` and `syncDurableSend=true`
2. **Consumer:** Uses CLIENT_ACKNOWLEDGE mode with explicit acknowledgement gating
3. **Broker:** Persistent journal storage, replication, redelivery, and DLQ routing
4. **Message identity:** Includes `duplicateDetectionId` for deduplication
5. **Redelivery:** Configured with delay multiplier and max attempts (3)

**However, a residual question remains:** Are all auto-created queues in Artemis durable by default? The code in `TaskEventProducerService` uses `context.createQueue(queueName)` without explicit durability declaration. This should be verified in a follow-up pass.

**10. Kleio Audit Control Case (Verification)**

**File:** `kleio/kleio-persistence/src/main/java/net/fhirfactory/harmonia/kleio/persistence/service/DurableAuditService.java`

**Key findings (from previous exploration):**

- `append()` is `@Transactional` (line 105-107)
- Uses JDBC `JdbcAppendOnlyAuditEventRepository.insertIfAbsent()` (line 122-129)
- Throws `AuditPersistenceException` on `SQLException` (line 96-109)
- `InMemoryAuditService` is not registered as a production CDI bean

**Contrast with Pragma/Task persistence:**

| Aspect | Kleio Audit | Pragma/Task |
|---|---|---|
| **Success condition** | Database transaction commit | Local map insertion |
| **Failure handling** | Exception thrown | Exception logged, success returned |
| **Restart recovery** | Database query | Local map (lost on restart) |
| **Fallback** | None (fail-closed) | Local `ConcurrentHashMap` |

**11. Confirmed Task 07 Violations**

| Component | Current Behavior | Failure Scenario | Success Returned Before | Loss on Restart | Severity |
|---|---|---|---|---|---|
| **PragmaCacheService** | Local map + remote cache write-behind | Remote cache unavailable | After local insertion | Pragma entries | **Critical** |
| **TaskCacheService** | Local map + remote cache write-behind | Remote cache unavailable | After local insertion | Task entries | **Critical** |
| **TaskEventProducerService** | JMS + HTTP fallback | Both dispatch paths fail | After method returns | Task event lost | **Critical** |
| **ErgonBase.processEgress** | Logs state as persisted after cache save | Cache save fails | After cache save | Workflow state | **Critical** |
| **AdtDistributionErgon** | Records fan-out checkpoints in Pragma | Pragma cache fails | After local insertion | Checkpoint state | **Critical** |

**12. Non-Violations (Legitimate Volatile State)**

- **Workflow registration:** `PragmaWorkflowDispatcher.registeredWorkflows` is appropriately volatile and reconstructed on startup
- **Petasos messages:** Properly durable via Artemis broker
- **Artemis queues:** Properly durable via broker journal and replication
- **Kleio audit:** Properly durable via transactional database persistence

**13. Residual Architectural Questions**

1. **Are all auto-created Artemis queues durable by default?** The code in `TaskEventProducerService` uses `context.createQueue(queueName)` without explicit durability declaration. Verify Artemis behavior.

2. **Should Pragma/Task/Provenance be classified as authoritative durable state or reconstructable projections?** This affects whether they should fail-closed or have fallback behavior.

3. **What is the intended recovery path for Pragma/Task state after process restart?** Should it be reconstructed from Petasos input, or should it be recovered from Mnemosyne?

4. **Should task event dispatch failure propagate to the MLLP processor?** Currently, `TaskEventProducerService.sendTaskEvent()` returns normally after both JMS and HTTP dispatch fail, allowing the MLLP processor to report success/ACK.

**14. Recommended Task 07 Implementation Blocks**

**07.1 — Prevent Pragma/Task cache-fallback from masking durable acceptance**

**Target:** `PragmaCacheService`, `TaskCacheService`, `ErgonBase.processEgress`

**Invariant:** Pragma/Task/Provenance save success must not be returned until durable acceptance is confirmed.

**Scope:** Remove or disable local fallback as a success path for authoritative workflow state. Preserve only explicitly classified reconstructable/transient maps.

**07.2 — Propagate task event dispatch failure to MLLP processor**

**Target:** `TaskEventProducerService.sendTaskEvent()`, MLLP ingress caller

**Invariant:** Total JMS/HTTP dispatch failure must reach the MLLP processing result and produce AE/NACK rather than successful completion.

**Scope:** Throw exception or return error status when both dispatch paths fail. Preserve existing REC-001 behavior; do not redesign Petasos transitions.

**07.3 — Add failure/recovery test coverage**

**Target:** Test suite for Pragma/Task cache, MLLP dispatch, Ergon state persistence

**Invariant:** Verify behavior when remote cache is unavailable, when persistence fails, and when process restarts.

**Scope:** Add focused tests for:
- Remote cache unavailability
- Pragma/Task persistence failure
- Process restart recovery
- Complete MLLP dispatch failure
- Checkpoint recovery from durable state

**15. Discoveries for Future Steps**

- **Petasos/Artemis durability is properly configured.** The producer defaults to durable/sync send, the consumer uses CLIENT_ACKNOWLEDGE with explicit gating, and the broker is configured for persistent journal storage with replication. No changes needed to Petasos/Artemis durability semantics.

- **Workflow registration is appropriately volatile.** `PragmaWorkflowDispatcher.registeredWorkflows` is runtime-only and reconstructed on startup. This is not a Task 07 concern.

- **The critical Task 07 boundary is between cache success and durable acceptance.** `PragmaCacheService` and `TaskCacheService` return success after local map insertion, not after remote cache or Mnemosyne acceptance. This is the primary violation.

- **MLLP task event dispatch has a separate false-success path.** `TaskEventProducerService.sendTaskEvent()` returns normally after both JMS and HTTP dispatch fail. This is a secondary violation affecting REC-001 (Ingress Dual-Write Safety).

- **Kleio audit remains the reference control.** `DurableAuditService` performs synchronous transactional writes where failure is propagated immediately to the caller. This is the desired semantic.

- **Existing test coverage includes broker recovery and checkpoint resilience, but lacks cache-failure and persistence-outage scenarios.** The test suite should be extended to verify behavior when remote cache is unavailable and when process restarts.

- **No code was changed and no tests were executed in this exploration.**



ANALYSIS

**Task description**  
Synthesize the exploration findings into a Task 07 Step 01 architectural report outline. Focus on:
- separating confirmed violations from likely-but-needs-deeper-confirmation areas;
- sharpening the difference between volatile cache loss and incorrectly acknowledged durable state loss;
- producing a coherent set of small implementation blocks;
- identifying any important caveats where the evidence is suggestive but not yet definitive.

Please keep the synthesis grounded in the already explored files and evidence, and structure it to match the user's requested output sections as closely as practical.



**1. Executive Summary**

This synthesis evaluates the persistence architecture across the Harmonia codebase to eliminate failure modes that risk silent data loss, false operational success, and corrupted clinical or workflow state. The core objective is ensuring that any state reported as accepted is durably recoverable (`accepted => durably recoverable`), rather than merely held in volatile JVM process memory.

The exploration reveals three distinct systemic risks:
1. **Asynchronous Write-Behind Exception Masking**: In `hestia/mneme-persistence`, `FhirRestCacheStore` and `OperationsRestCacheStore` configure write-behind caching to downstream Mnemosyne REST persistence. When Mnemosyne rejects or fails a write, the cache store catches the exception, logs it, and completes the asynchronous stage normally (`.exceptionally(... return null)`). Callers are acknowledged as successful before downstream durable writes even occur or when they silently fail.
2. **Volatile Local Memory Fallback in Production Services**: In `iris/iris-befe`, `pylai/pylai-mllp-base`, `energeia/erga`, and `energeia/praxis`, services maintain local `ConcurrentHashMap` instances. When remote Infinispan caches fail or are unreachable, writes fall back to local process memory while still returning success. A process restart permanently destroys this unpersisted state.
3. **Ingress Dual-Write Failure Masking**: In `pylai/pylai-mllp-base`, `TaskEventProducerService` catches both JMS and HTTP fallback transmission errors without rethrowing, permitting the MLLP ingress gateway to return an `AA` (Application Accept) ACK to upstream clinical systems even when the event was completely lost.

In contrast, the Kleio audit subsystem (`DurableAuditService` / `JdbcAppendOnlyAuditEventRepository`) serves as an exemplary control case: it enforces synchronous, transactional persistence and fails closed when the database is unavailable.

---

**2. Inventory of Persistence Mechanisms**

| Subsystem / Layer | Component / File Reference | Backing Store / Mechanism | Architectural Scope |
| :--- | :--- | :--- | :--- |
| **Mnemosyne Clinical** | `deployment/kubernetes/base/hestia/mnemosyne-clinical.yaml:42-58`<br>`docker-compose.yml:149-195` | PostgreSQL / HAPI FHIR JPA | Authoritative clinical resource repository (`fhir_node_*`). |
| **Mnemosyne Operations** | `deployment/kubernetes/base/hestia/mnemosyne-operations.yaml:42-58`<br>`docker-compose.yml:198-240` | PostgreSQL / JPA | Authoritative operations, task sequence, and queue metadata repository (`ops_node_*`). |
| **Mneme Cluster** | `hestia/mneme-cluster/src/main/resources/infinispan.xml:70-304` | Infinispan replicated synchronous cache (`REPL_SYNC`) | Cluster-wide cache acceleration layer with asynchronous write-behind adapters. |
| **Mneme Persistence Adapters** | `hestia/mneme-persistence/.../FhirRestCacheStore.java:98-121`<br>`hestia/mneme-persistence/.../OperationsRestCacheStore.java:102-126` | Remote REST client to Mnemosyne | Asynchronous cache store interface translating Infinispan mutations to Mnemosyne HTTP writes. |
| **BEFE Resource Service** | `iris/iris-befe/.../FhirCacheService.java:40-57, 136-188, 219-234` | Infinispan RemoteCache + `ConcurrentHashMap` fallback | FHIR resource gateway and presentation cache service. |
| **BEFE Task Sequence Service** | `iris/iris-befe/.../TaskSequenceCacheService.java:41-50, 58-66, 210-266` | Infinispan RemoteCache + `ConcurrentHashMap` fallback | Workflow sequence definition store. |
| **Pylai Task & Provenance Services** | `pylai/pylai-mllp-base/.../DefaultTaskService.java:44-55, 80-167`<br>`pylai/pylai-mllp-base/.../DefaultProvenanceService.java:36-48, 73-156` | Infinispan RemoteCache + `ConcurrentHashMap` (`taskStore`, `provenanceStore`) | MLLP gateway workflow Task and Provenance tracking stores. |
| **Erga / Praxis Workflow Cache** | `energeia/erga/.../TaskCacheService.java:40-63, 224-318`<br>`energeia/praxis/.../PragmaCacheService.java:43-86, 96-126` | Infinispan RemoteCache + `ConcurrentHashMap` fallback | In-flight Pragma and Ergon processing state cache. |
| **Ponos Configuration Services** | `energeia/ponos/.../MessageQueueService.java:40-52, 134-203`<br>`energeia/praxis/.../PraxisService.java:36-53, 137-245` | Infinispan RemoteCache + `ConcurrentHashMap` fallback | Queue definitions and workflow execution metadata. |
| **Petasos Artemis Broker** | `petasos/deployment/artemis/standalone/broker.xml:14-51`<br>`deployment/kubernetes/base/petasos/artemis-configmaps.yaml:16-77` | ActiveMQ Artemis persistent journal (NIO/AIO) | Durable asynchronous messaging and inter-activity transition broker. |
| **Kleio Audit Repository** | `kleio/kleio-persistence/.../DurableAuditService.java:46-54, 105-129`<br>`kleio/kleio-persistence/.../JdbcAppendOnlyAuditEventRepository.java:96-109` | PostgreSQL via JDBC append-only transactions | Authoritative security and operational audit trail. |

---

**3. Classification of Persistence Mechanisms**

To ensure stability and eliminate catastrophic data loss scenarios, persistence mechanisms are classified by their durability contract:

1. **Authoritative Durable State**:
    - `Mnemosyne Clinical` (PostgreSQL): Primary authoritative source of FHIR clinical data.
    - `Mnemosyne Operations` (PostgreSQL): Primary authoritative source of workflow sequences and queue topologies.
    - `Kleio DurableAuditService` (PostgreSQL): Immutable audit trail. Must fail closed synchronously.
2. **Durable Work / Transition**:
    - `Petasos / ActiveMQ Artemis`: Broker journal-backed queues. Governs recoverable work transfers across activity boundaries.
3. **Cache (Disposable Acceleration)**:
    - `Mneme Infinispan cluster`: Replicated in-memory cache intended to accelerate point reads. Cache eviction or loss must not result in unrecoverable state loss.
4. **Volatile Process State (Legitimate / Transient)**:
    - `PragmaWorkflowDispatcher.registeredWorkflows`: In-memory CDI registration map of executable pipeline beans, rebuilt on startup.
    - `PhiLoggerFactory.LOGGER_CACHE`: Local instance cache for sanitizing loggers.
5. **Volatile Fallback (Critical Violations)**:
    - `FhirCacheService.localFallbackCaches`
    - `DefaultTaskService.taskStore` & `DefaultProvenanceService.provenanceStore`
    - `TaskCacheService.localFallbackCache` & `PragmaCacheService.localFallbackCache`
    - `MessageQueueService.localFallbackCache`, `PraxisService.localFallbackCache`, and `TaskSequenceCacheService.localFallbackCache`
    - *These fallbacks intercept writes when durable infrastructure fails, returning success while holding data only in local heap.*
6. **Test / Exemplar Only**:
    - `InMemoryAuditService`: Non-production test stub excluded from production CDI scanning.

---

**4. Code-Verified Major Write Paths**

**4.1 BEFE FHIR Resource REST Ingress**
```text
HTTP Client (POST/PUT /fhir/r5/Task)
  -> TaskResource.create/update (iris-befe)
  -> FhirCacheService.saveResource(resource)
  -> [Branch A]: RemoteCache.put(cacheName, id, json)
       -> Infinispan REPL_SYNC Cache
       -> Asynchronous Write-Behind Queue (capacity 1024)
       -> FhirRestCacheStore.write(MarshalledEntry)
       -> Mnemosyne REST API -> HAPI FHIR JPA -> PostgreSQL (Async)
  -> [Branch B - On Remote Failure]: localFallbackCaches[cacheName].put(id, json)
  -> Return HTTP 201 Created / 200 OK (Synchronous to caller)
```
- **Evidence**: `TaskResource.java:71-99`, `FhirCacheService.java:136-188`, `infinispan.xml:222-262`, `FhirRestCacheStore.java:98-121`.
- **Failure Point**: HTTP 201/200 is returned immediately after Branch A (cache write) or Branch B (local map write). If Mnemosyne fails during write-behind, or if Branch B occurred, the caller assumes durable persistence while the resource exists only in transient memory.

**4.2 Pylai MLLP Ingress and Event Dispatch**
```text
MLLP Client (HL7 v2 ADT)
  -> IncomingAdtMessageProcessor.processMessage (pylai-mllp-in)
  -> DefaultTaskService.create(task) -> taskStore.put() + remoteCache.put()
  -> DefaultProvenanceService.create(provenance) -> provenanceStore.put() + remoteCache.put()
  -> TaskEventProducerService.sendTaskEvent(taskEvent)
       -> JMS send to Artemis Queue
       -> [On JMS Failure]: HTTP fallback POST to Ponos
       -> [On HTTP Failure]: Catch and log (Swallow exception)
  -> Return MLLP MSA|AA (Application Accept) ACK
```
- **Evidence**: `IncomingAdtMessageProcessor.java:276-320`, `DefaultTaskService.java:80-113`, `TaskEventProducerService.java:137-206`.
- **Failure Point**: `sendTaskEvent` catches both JMS and HTTP transport failures without rethrowing (`TaskEventProducerService.java:178-204`). The processor continues to line 313 and transmits an `AA` ACK upstream even if both event publication attempts failed completely.

**4.3 Ergon Processing and Egress**
```text
Petasos Message Consumer
  -> ErgonBase.processIngress -> TaskCacheService.savePragma(pragma)
  -> ErgonBase.processEgress
       -> TaskCacheService.savePragma(pragma)
       -> TaskCacheService.saveTask(task)
       -> TaskCacheService.saveProvenance(provenance)
  -> Petasos Message Producer (Egress event)
```
- **Evidence**: `ErgonBase.java:244-300, 338-359`, `TaskCacheService.java:96-126`.
- **Failure Point**: Workflow transitions are recorded via `TaskCacheService` which falls back to local heap on cache failure. State logged as "persisted" (`ErgonBase.java:346-347`) is vulnerable to immediate process loss.

**4.4 Kleio Audit Control Case (Synchronous & Durable)**
```text
Audit Event Producer
  -> DurableAuditService.append(auditEvent) [@Transactional]
  -> JdbcAppendOnlyAuditEventRepository.insertIfAbsent(record)
  -> PostgreSQL INSERT via JDBC
  -> Transaction Commit -> Return Success
```
- **Evidence**: `DurableAuditService.java:105-129`, `JdbcAppendOnlyAuditEventRepository.java:96-109`.
- **Control Property**: Any database failure throws `AuditPersistenceException`, bubbling up directly to the caller. No intermediate write-behind or local memory substitution exists.

---

**5. Mneme / Infinispan Write-Behind Analysis**

**5.1 Configuration Analysis**
`hestia/mneme-cluster/src/main/resources/infinispan.xml` defines caches (`task-cache`, `communication-cache`, `tasksequence-cache`, `messagequeue-cache`, etc.) with the following attributes:
- `mode="SYNC"` (synchronous cluster replication across Infinispan nodes).
- `preload="false"`, `fetch-state="false"`, `segmented="false"`.
- `<write-behind modification-queue-size="1024" />`.

**5.2 Exception Swallowing in REST Stores**
In `FhirRestCacheStore.java`:
```java
// Line 110-121
return restClient.saveResourceJson(resourceType, id, json)
    .thenAccept(success -> {
        if (!success) {
            log.warn("Failed to save resource {}/{} to FHIR persistence", resourceType, id);
        }
    })
    .exceptionally(ex -> {
        log.error("Error writing resource {}/{} to FHIR persistence: {}", resourceType, id, ex.getMessage());
        return null; // Swallows exception and completes stage normally
    });
```
`OperationsRestCacheStore.java:114-126` contains identical logic.

**5.3 Sharpening the Boundary: Cache Loss vs. Acknowledged State Loss**
- **Benign Cache Loss**: If an authoritative database holds the primary record, an Infinispan node crash or cache eviction merely causes a cache miss. The subsequent read reloads the entity from Mnemosyne via `load()`.
- **Unacceptable Authoritative State Loss**: Under the current write-behind design, a caller receives HTTP 201/200 once the item is in Infinispan memory. If Mnemosyne is offline or the REST call fails, the modification queue is dropped or completes exceptionally without alerting the caller. When the cache entry is evicted or the cluster restarts, the data is permanently lost.

---

**6. Mnemosyne Failure Semantics**

1. **JPA Layer Behavior**: `FhirStorageService` and `OperationStorageService` in `hestia/mnemosyne-*` use Spring Data JPA repositories. Persistence errors throw standard `DataAccessException` / `TransactionSystemException`.
2. **Transaction Integrity**: Mnemosyne does not implement internal volatile fallback buffers. If PostgreSQL is unreachable, Mnemosyne REST endpoints fail with HTTP 500.
3. **Deployment Safety**: Manifests (`deployment/kubernetes/base/hestia/mnemosyne-*.yaml`) mandate `SPRING_PROFILES_ACTIVE: "postgres"`. Missing JDBC endpoints prevent startup rather than dropping into an embedded H2/in-memory mode.
4. **Root Vulnerability**: Mnemosyne correctly fails closed; the failure is masked exclusively by the upstream caching adapters (`FhirRestCacheStore`, `OperationsRestCacheStore`, and `FhirCacheService`).

---

**7. Explicit Volatile / In-Memory Fallback Findings**

| Module | File & Line Range | Code Pattern | Classification |
| :--- | :--- | :--- | :--- |
| `iris-befe` | `FhirCacheService.java`<br>lines 40-57, 219-234 | `localFallbackCaches.computeIfAbsent(...)`<br>Used on Hot Rod connection failure. | **Production Fallback (Violation)** |
| `iris-befe` | `TaskSequenceCacheService.java`<br>lines 58-66, 254-266 | `localFallbackCache.put(id, sequence)`<br>Catches remote cache error and returns raw JSON. | **Production Fallback (Violation)** |
| `pylai-mllp-base` | `DefaultTaskService.java`<br>lines 55, 98-113 | `taskStore.put(task.getId(), task)`<br>Updates local map first; logs remote cache error and returns task. | **Production Fallback (Violation)** |
| `pylai-mllp-base` | `DefaultProvenanceService.java`<br>lines 48, 89-105 | `provenanceStore.put(prov.getId(), prov)`<br>Updates local map first; logs remote cache error and returns provenance. | **Production Fallback (Violation)** |
| `energeia-erga` | `TaskCacheService.java`<br>lines 58-63, 224-268 | `localFallbackCache.put(id, task)`<br>Provides local fallback for tasks and pragmas. | **Production Fallback (Violation)** |
| `energeia-praxis` | `PragmaCacheService.java`<br>lines 59, 106-125 | `localFallbackCache.put(id, new Pragma(pragma))`<br>Local insertion executed before remote cache attempt. | **Production Fallback (Violation)** |
| `energeia-ponos` | `MessageQueueService.java`<br>lines 48, 134-157 | `localFallbackCache.put(name, queue)`<br>Logs remote cache failure and returns queue definition. | **Production Fallback (Violation)** |
| `energeia-praxis` | `PraxisService.java`<br>lines 49, 137-164 | `localFallbackCache.put(name, seq)`<br>Logs "Persisted in local fallback" and returns sequence. | **Production Fallback (Violation)** |
| `iris-befe` | `ModuleStatusService.java`<br>lines 35-43, 63-85 | `localFallbackCache.put(id, status)`<br>Maintains transient module heartbeat/status. | **Volatile Operational State (Low Risk / Reconstructable)** |
| `kleio-core` | `InMemoryAuditService.java`<br>lines 41-108 | In-memory `List<AuditEvent>` backing. | **Test / Exemplar Only** (Unsatisfied in production CDI) |

---

**8. Workflow, Ponos, Ergon, and Petasos State Findings**

1. **Pragma and Task Lifecycle**:
    - In `ErgonBase.java:279, 338-359`, workflow steps checkpoint state through `TaskCacheService` and `PragmaCacheService`.
    - Because these cache services rely on local maps when Infinispan fails, an in-flight workflow checkpoint can be completely lost during a container restart, preventing recovery of in-flight work.
2. **Workflow Dispatcher State**:
    - `PragmaWorkflowDispatcher.registeredWorkflows` (`PragmaWorkflowDispatcher.java:81`) is an in-memory registry of CDI handler beans. This is legitimate volatile process state because it is reconstructed deterministically during application bootstrap.
3. **Petasos Work Handoff**:
    - Petasos message envelopes are designed to carry causality, correlation, and tracking identifiers (`PetasosMessage.java:59`). While messaging transitions provide durable work handoffs, they do not replace durable application state in Mnemosyne.

---

**9. Petasos and Artemis Durability Findings**

1. **Broker Configuration**:
    - `petasos/deployment/artemis/standalone/broker.xml:15-41` and `deployment/kubernetes/base/petasos/artemis-configmaps.yaml:16-77` establish `<persistence-enabled>true</persistence-enabled>`, NIO journal storage, replication across primary/backup brokers, and dead-letter queue (DLQ) addresses.
2. **Producer & Consumer Contracts**:
    - `PetasosProducerConfig.java:78-82` sets `defaultDurable=true` and `syncDurableSend=true`.
    - `PetasosConsumerConfig.java:91-94` and `ArtemisPetasosSubscription.java:94-158, 226-268` enforce `CLIENT_ACKNOWLEDGE` mode. Failures trigger `session.recover()` or explicit DLQ routing.
3. **Queue Creation Ambiguity (Caveat)**:
    - In `TaskEventProducerService.java:140`, queues are created dynamically via `context.createQueue(queueName)`. While the Artemis broker config sets `<auto-create-queues>true</auto-create-queues>`, explicit durability parameters are omitted in the JMS invocation. Production destinations should be confirmed as durable queue bindings.

---

**10. Runtime and Deployment Composition Findings**

1. **Lazy Hot Rod Client Initialization**:
    - In `pylai/pylai-mllp-base/.../HotRodClientProducer.java:39-74` and `energeia/ponos/.../HotRodClientProducer.java:39-74`, the CDI producer attempts eager connection to Infinispan (`127.0.0.1:11222`). If the cluster is unreachable, it catches the exception and instantiates `new RemoteCacheManager(..., false)` (lazy startup).
    - *Impact*: In an environment where Infinispan is misconfigured or offline, the container starts without error, returns `null` from `getRemoteCache()`, and silently routes all production traffic into local heap `ConcurrentHashMap` instances.
2. **Environment URL Defaults**:
    - `FhirStoreConfiguration.java:29-35` defaults `fhir.store.rest.url` to `http://localhost:8080/fhir`. Missing environment variables in container deployments fall back to localhost rather than halting startup.

---

**11. Existing Test Coverage and Critical Testing Gaps**

**11.1 Current Test Strengths**
- **Broker Crash / Recovery**: `Checkpoint1MessageFlowIntegrationTest.java:260-318` validates Artemis stop/restart recovery and client reconnection.
- **Consumer Acknowledgement**: `ArtemisPetasosSubscriptionTest.java:98-271` validates client acknowledge, duplicate skipping, and redelivery on error.
- **Audit Persistence Verification**: `KleioRuntimeCompositionTest.java` and `DurableAuditServicePostgreSqlConcurrencyTest.java` verify that audit persistence is strictly transactional and that in-memory stubs are excluded.

**11.2 Critical Testing Gaps**
- **Write-Behind Drop Scenarios**: No test exists to verify system behavior when `FhirRestCacheStore` encounters an HTTP 500 or timeout from Mnemosyne.
- **Fail-Closed Verification on Cache Outage**: No integration tests verify that `FhirCacheService`, `DefaultTaskService`, or `PragmaCacheService` fail closed (returning 5xx/errors) when Infinispan and local fallbacks are disabled.
- **Restart Recovery of Acknowledged Entities**: No tests verify that a FHIR Task or MLLP ADT message acknowledged to an external client is recoverable from disk after an immediate hard `kill -9` of the JVM.
- **Ingress Dual-Write Failure Propagation**: No test verifies that MLLP returns an `AE` NACK when `TaskEventProducerService` fails to publish to Artemis.

---

**12. Confirmed Task 07 Violations**

| Violation ID | Component & File | Exact Behavior | Failure & Loss Impact | Remediation Boundary |
| :--- | :--- | :--- | :--- | :--- |
| **VIO-01** | `FhirRestCacheStore.java:118-121`<br>`OperationsRestCacheStore.java:123-126` | Catches async REST persistence exceptions and returns `null` completed stage. | Mnemosyne write failure is hidden; acknowledged state disappears upon cache eviction. | Make cache persistence synchronous or propagate write-behind failure; fail operations that cannot be made durable. |
| **VIO-02** | `FhirCacheService.java:40-57, 136-188` | Stores resources in `localFallbackCaches` when RemoteCache is absent or throws error. | Client receives HTTP 201/200; state exists only in JVM heap and is lost on restart. | Remove local memory fallback for authoritative writes; fail closed with HTTP 503. |
| **VIO-03** | `DefaultTaskService.java:80-113`<br>`DefaultProvenanceService.java:73-105` | Updates `taskStore` / `provenanceStore` first; swallows remote cache exceptions. | MLLP pipeline assumes Task is stored; gateway restart destroys task state. | Remove local map fallback; require durable Mnemosyne acceptance. |
| **VIO-04** | `TaskEventProducerService.java:175-204` | Catches JMS exception, tries HTTP fallback, catches HTTP exception, returns normally. | Ingress gateway sends `AA` ACK even though message was never published or persisted. | Propagate exception on total dispatch failure to trigger MLLP `AE` NACK (REC-001). |
| **VIO-05** | `TaskCacheService.java:58-63, 224-318`<br>`PragmaCacheService.java:59, 106-125` | Writes Pragma/Task to local map before/instead of durable store. | Ergon activity logs state as persisted; restart aborts workflow recovery. | Remove local fallback; ensure workflow checkpoint persistence fails closed. |
| **VIO-06** | `MessageQueueService.java:134-157`<br>`PraxisService.java:137-164`<br>`TaskSequenceCacheService.java:254-266` | Saves queue/workflow configuration to `localFallbackCache` when cache fails. | Topology/sequence updates appear successful but revert on container restart. | Enforce fail-closed persistence against Mnemosyne Operations. |
| **VIO-07** | `HotRodClientProducer.java:39-74` (Pylai & Ponos) | Falls back to lazy unstarted `RemoteCacheManager` on initial connection failure. | System silently activates volatile local fallback maps in production containers. | Fail container startup if required persistence infrastructure is unreachable. |

---

**13. Legitimate Volatile State and Non-Violations**

To prevent overengineering, the following components are recognized as valid volatile stores that do **not** violate Task 07:
1. **`PragmaWorkflowDispatcher.registeredWorkflows`**: CDI registration map storing references to instantiated pipeline beans. Purely in-memory, deterministic, and reconstructed at startup.
2. **`PhiLoggerFactory.LOGGER_CACHE`**: Internal utility cache for parameterized logger instances.
3. **Themis Decision & Context Objects**: Immutable evaluation models (`ThemisDecision`, `PragmaSecurityContext`) instantiated per request.
4. **`ModuleStatusService.localFallbackCache`**: High-frequency node heartbeats. While remote caching is preferred, node status is ephemeral and continuously refreshed by active nodes.
5. **Read Caches with Authoritative Backing**: An Infinispan cache functioning strictly as a read-through/cache-aside accelerator is valid, provided it does not intercept and falsely acknowledge writes.

---

**14. Residual Questions and Suggestive Caveats Requiring Confirmation**

1. **JMS Queue Durability Declaration**: In `TaskEventProducerService.java:140`, `context.createQueue(queueName)` is used without explicit queue attribute flags. While broker XML enables auto-creation, a test is needed to verify whether auto-created queues inherit persistent disk backing by default under Artemis JMS 2.0.
2. **Authoritative vs. Reconstructable Status of Pragma Envelopes**: Is an in-flight `Pragma` strictly required to be persisted to Mnemosyne at every step, or can it be safely reconstructed from the originating Petasos queue message during consumer redelivery? (This dictates whether `TaskCacheService` must write to Mnemosyne or rely on Artemis transaction boundaries).
3. **Seeding of Operations Defaults**: `TaskSequenceCacheService.ensureDefaultSequences` seeds defaults into local memory if the cache is empty. It must be determined whether bootstrap defaults should be written directly to Mnemosyne Operations upon initial deployment.

---

**15. Recommended Small Task 07 Implementation Blocks**

To maintain architectural focus without prematurely absorbing Tasks 08–10 (Clinical write/read/search redesign), Task 07 should be partitioned into four cohesive, independently testable implementation blocks:

```text
+---------------------------------------------------------------------------------------+
| Block 07.1: Eliminate Write-Behind Exception Masking in Mneme                         |
|   - Disable async write-behind for authoritative caches in infinispan.xml             |
|   - Propagate exceptions in FhirRestCacheStore & OperationsRestCacheStore             |
|   - Verify that Mnemosyne REST failure immediately fails the cache operation          |
+---------------------------------------------------------------------------------------+
                                           |
                                           v
+---------------------------------------------------------------------------------------+
| Block 07.2: Remove Production Local Memory Fallbacks                                  |
|   - Remove localFallbackCaches from FhirCacheService, DefaultTaskService,             |
|     DefaultProvenanceService, TaskCacheService, and PragmaCacheService                |
|   - Enforce fail-closed (HTTP 503 / PersistenceException) on cache/store outage       |
|   - Remove memory fallback in PraxisService, MessageQueueService, TaskSequenceService |
+---------------------------------------------------------------------------------------+
                                           |
                                           v
+---------------------------------------------------------------------------------------+
| Block 07.3: Ingress Dual-Write Safety and Dispatch Failure Propagation (REC-001)      |
|   - Update TaskEventProducerService.sendTaskEvent to throw on total failure           |
|   - Ensure IncomingAdtMessageProcessor returns AE NACK on dispatch exception          |
|   - Remove lazy HotRodClientProducer fallback; fail startup on cache connection error |
+---------------------------------------------------------------------------------------+
                                           |
                                           v
+---------------------------------------------------------------------------------------+
| Block 07.4: Failure and Durability Contract Verification Test Suite                   |
|   - Add integration tests for Mnemosyne outage and write-behind failure rejection     |
|   - Add tests verifying process restart recovery of acknowledged state                |
|   - Add tests verifying MLLP AE NACK generation on Petasos/HTTP dispatch outage       |
+---------------------------------------------------------------------------------------+
```

**Detailed Scope of Blocks:**

- **Block 07.1: Eliminate Write-Behind Exception Masking in Mneme**
    - **Invariant**: Cache store writes to Mnemosyne must not report success if the durable write fails.
    - **Target**: `hestia/mneme-persistence` (`FhirRestCacheStore`, `OperationsRestCacheStore`), `hestia/mneme-cluster` (`infinispan.xml`).
    - **Action**: Remove async write-behind (`<write-behind>`) for authoritative clinical/operations caches; remove exception-swallowing `.exceptionally(... return null)` handlers.

- **Block 07.2: Remove Production Local Memory Fallbacks**
    - **Invariant**: State must never be accepted into volatile heap as a substitute for unavailable durable persistence.
    - **Target**: `iris/iris-befe` (`FhirCacheService`, `TaskSequenceCacheService`), `pylai/pylai-mllp-base` (`DefaultTaskService`, `DefaultProvenanceService`), `energeia/erga` (`TaskCacheService`), `energeia/praxis` (`PragmaCacheService`, `PraxisService`), `energeia/ponos` (`MessageQueueService`).
    - **Action**: Eliminate `ConcurrentHashMap` fallback maps for authoritative entities; throw actionable service exceptions when remote infrastructure is unreachable.

- **Block 07.3: Ingress Dual-Write Safety and Dispatch Failure Propagation (REC-001)**
    - **Invariant**: Downstream publication or persistence failure must prevent positive acknowledgement (`AA` ACK) to upstream senders.
    - **Target**: `pylai/pylai-mllp-base` (`TaskEventProducerService`, `HotRodClientProducer`), `pylai/pylai-mllp-in` (`IncomingAdtMessageProcessor`), `energeia/ponos` (`HotRodClientProducer`).
    - **Action**: Propagate exceptions across `sendTaskEvent`; enforce fail-fast container bootstrap in `HotRodClientProducer`.

- **Block 07.4: Failure and Durability Contract Verification Test Suite**
    - **Invariant**: Every failure-handling and durability boundary must be asserted by automated tests.
    - **Target**: `hestia/mneme-persistence/src/test/...`, `pylai/pylai-mllp-in/src/test/...`, `energeia/ponos/src/test/...`.
    - **Action**: Implement targeted tests for database outage, cache disconnection, hard process crash recovery, and MLLP `AE` NACK generation.

---

**Discoveries for Future Steps**

- **High-Signal Inspection Targets**: The root cause of false durability in Harmonia is concentrated in `infinispan.xml`, the two REST cache stores (`FhirRestCacheStore`, `OperationsRestCacheStore`), and the `localFallbackCache` maps in the service layer.
- **Mnemosyne is Inherently Robust**: Mnemosyne's JPA layer correctly fails closed and throws exceptions during database outages; it requires no internal fallback remediation.
- **Kleio is the Proven Template**: `DurableAuditService` demonstrates the exact pattern needed: synchronous, transactional persistence that propagates database errors immediately to the caller.
- **Artemis Messaging Integrity**: Petasos/Artemis messaging is already configured for durable synchronous delivery and client acknowledgements; work should focus on gateway exception propagation rather than re-architecting Artemis.

Analysis is Ready

**1. Executive Summary**

This synthesis evaluates the persistence architecture across the Harmonia codebase to eliminate failure modes that risk silent data loss, false operational success, and corrupted clinical or workflow state. The core objective is ensuring that any state reported as accepted is durably recoverable (`accepted => durably recoverable`), rather than merely held in volatile JVM process memory.

The exploration reveals three distinct systemic risks:
1. **Asynchronous Write-Behind Exception Masking**: In `hestia/mneme-persistence`, `FhirRestCacheStore` and `OperationsRestCacheStore` configure write-behind caching to downstream Mnemosyne REST persistence. When Mnemosyne rejects or fails a write, the cache store catches the exception, logs it, and completes the asynchronous stage normally (`.exceptionally(... return null)`). Callers are acknowledged as successful before downstream durable writes even occur or when they silently fail.
2. **Volatile Local Memory Fallback in Production Services**: In `iris/iris-befe`, `pylai/pylai-mllp-base`, `energeia/erga`, and `energeia/praxis`, services maintain local `ConcurrentHashMap` instances. When remote Infinispan caches fail or are unreachable, writes fall back to local process memory while still returning success. A process restart permanently destroys this unpersisted state.
3. **Ingress Dual-Write Failure Masking**: In `pylai/pylai-mllp-base`, `TaskEventProducerService` catches both JMS and HTTP fallback transmission errors without rethrowing, permitting the MLLP ingress gateway to return an `AA` (Application Accept) ACK to upstream clinical systems even when the event was completely lost.

In contrast, the Kleio audit subsystem (`DurableAuditService` / `JdbcAppendOnlyAuditEventRepository`) serves as an exemplary control case: it enforces synchronous, transactional persistence and fails closed when the database is unavailable.

---

**2. Inventory of Persistence Mechanisms**

| Subsystem / Layer | Component / File Reference | Backing Store / Mechanism | Architectural Scope |
| :--- | :--- | :--- | :--- |
| **Mnemosyne Clinical** | `deployment/kubernetes/base/hestia/mnemosyne-clinical.yaml:42-58`<br>`docker-compose.yml:149-195` | PostgreSQL / HAPI FHIR JPA | Authoritative clinical resource repository (`fhir_node_*`). |
| **Mnemosyne Operations** | `deployment/kubernetes/base/hestia/mnemosyne-operations.yaml:42-58`<br>`docker-compose.yml:198-240` | PostgreSQL / JPA | Authoritative operations, task sequence, and queue metadata repository (`ops_node_*`). |
| **Mneme Cluster** | `hestia/mneme-cluster/src/main/resources/infinispan.xml:70-304` | Infinispan replicated synchronous cache (`REPL_SYNC`) | Cluster-wide cache acceleration layer with asynchronous write-behind adapters. |
| **Mneme Persistence Adapters** | `hestia/mneme-persistence/.../FhirRestCacheStore.java:98-121`<br>`hestia/mneme-persistence/.../OperationsRestCacheStore.java:102-126` | Remote REST client to Mnemosyne | Asynchronous cache store interface translating Infinispan mutations to Mnemosyne HTTP writes. |
| **BEFE Resource Service** | `iris/iris-befe/.../FhirCacheService.java:40-57, 136-188, 219-234` | Infinispan RemoteCache + `ConcurrentHashMap` fallback | FHIR resource gateway and presentation cache service. |
| **BEFE Task Sequence Service** | `iris/iris-befe/.../TaskSequenceCacheService.java:41-50, 58-66, 210-266` | Infinispan RemoteCache + `ConcurrentHashMap` fallback | Workflow sequence definition store. |
| **Pylai Task & Provenance Services** | `pylai/pylai-mllp-base/.../DefaultTaskService.java:44-55, 80-167`<br>`pylai/pylai-mllp-base/.../DefaultProvenanceService.java:36-48, 73-156` | Infinispan RemoteCache + `ConcurrentHashMap` (`taskStore`, `provenanceStore`) | MLLP gateway workflow Task and Provenance tracking stores. |
| **Erga / Praxis Workflow Cache** | `energeia/erga/.../TaskCacheService.java:40-63, 224-318`<br>`energeia/praxis/.../PragmaCacheService.java:43-86, 96-126` | Infinispan RemoteCache + `ConcurrentHashMap` fallback | In-flight Pragma and Ergon processing state cache. |
| **Ponos Configuration Services** | `energeia/ponos/.../MessageQueueService.java:40-52, 134-203`<br>`energeia/praxis/.../PraxisService.java:36-53, 137-245` | Infinispan RemoteCache + `ConcurrentHashMap` fallback | Queue definitions and workflow execution metadata. |
| **Petasos Artemis Broker** | `petasos/deployment/artemis/standalone/broker.xml:14-51`<br>`deployment/kubernetes/base/petasos/artemis-configmaps.yaml:16-77` | ActiveMQ Artemis persistent journal (NIO/AIO) | Durable asynchronous messaging and inter-activity transition broker. |
| **Kleio Audit Repository** | `kleio/kleio-persistence/.../DurableAuditService.java:46-54, 105-129`<br>`kleio/kleio-persistence/.../JdbcAppendOnlyAuditEventRepository.java:96-109` | PostgreSQL via JDBC append-only transactions | Authoritative security and operational audit trail. |

---

**3. Classification of Persistence Mechanisms**

To ensure stability and eliminate catastrophic data loss scenarios, persistence mechanisms are classified by their durability contract:

1. **Authoritative Durable State**:
    - `Mnemosyne Clinical` (PostgreSQL): Primary authoritative source of FHIR clinical data.
    - `Mnemosyne Operations` (PostgreSQL): Primary authoritative source of workflow sequences and queue topologies.
    - `Kleio DurableAuditService` (PostgreSQL): Immutable audit trail. Must fail closed synchronously.
2. **Durable Work / Transition**:
    - `Petasos / ActiveMQ Artemis`: Broker journal-backed queues. Governs recoverable work transfers across activity boundaries.
3. **Cache (Disposable Acceleration)**:
    - `Mneme Infinispan cluster`: Replicated in-memory cache intended to accelerate point reads. Cache eviction or loss must not result in unrecoverable state loss.
4. **Volatile Process State (Legitimate / Transient)**:
    - `PragmaWorkflowDispatcher.registeredWorkflows`: In-memory CDI registration map of executable pipeline beans, rebuilt on startup.
    - `PhiLoggerFactory.LOGGER_CACHE`: Local instance cache for sanitizing loggers.
5. **Volatile Fallback (Critical Violations)**:
    - `FhirCacheService.localFallbackCaches`
    - `DefaultTaskService.taskStore` & `DefaultProvenanceService.provenanceStore`
    - `TaskCacheService.localFallbackCache` & `PragmaCacheService.localFallbackCache`
    - `MessageQueueService.localFallbackCache`, `PraxisService.localFallbackCache`, and `TaskSequenceCacheService.localFallbackCache`
    - *These fallbacks intercept writes when durable infrastructure fails, returning success while holding data only in local heap.*
6. **Test / Exemplar Only**:
    - `InMemoryAuditService`: Non-production test stub excluded from production CDI scanning.

---

**4. Code-Verified Major Write Paths**

**4.1 BEFE FHIR Resource REST Ingress**
```text
HTTP Client (POST/PUT /fhir/r5/Task)
  -> TaskResource.create/update (iris-befe)
  -> FhirCacheService.saveResource(resource)
  -> [Branch A]: RemoteCache.put(cacheName, id, json)
       -> Infinispan REPL_SYNC Cache
       -> Asynchronous Write-Behind Queue (capacity 1024)
       -> FhirRestCacheStore.write(MarshalledEntry)
       -> Mnemosyne REST API -> HAPI FHIR JPA -> PostgreSQL (Async)
  -> [Branch B - On Remote Failure]: localFallbackCaches[cacheName].put(id, json)
  -> Return HTTP 201 Created / 200 OK (Synchronous to caller)
```
- **Evidence**: `TaskResource.java:71-99`, `FhirCacheService.java:136-188`, `infinispan.xml:222-262`, `FhirRestCacheStore.java:98-121`.
- **Failure Point**: HTTP 201/200 is returned immediately after Branch A (cache write) or Branch B (local map write). If Mnemosyne fails during write-behind, or if Branch B occurred, the caller assumes durable persistence while the resource exists only in transient memory.

**4.2 Pylai MLLP Ingress and Event Dispatch**
```text
MLLP Client (HL7 v2 ADT)
  -> IncomingAdtMessageProcessor.processMessage (pylai-mllp-in)
  -> DefaultTaskService.create(task) -> taskStore.put() + remoteCache.put()
  -> DefaultProvenanceService.create(provenance) -> provenanceStore.put() + remoteCache.put()
  -> TaskEventProducerService.sendTaskEvent(taskEvent)
       -> JMS send to Artemis Queue
       -> [On JMS Failure]: HTTP fallback POST to Ponos
       -> [On HTTP Failure]: Catch and log (Swallow exception)
  -> Return MLLP MSA|AA (Application Accept) ACK
```
- **Evidence**: `IncomingAdtMessageProcessor.java:276-320`, `DefaultTaskService.java:80-113`, `TaskEventProducerService.java:137-206`.
- **Failure Point**: `sendTaskEvent` catches both JMS and HTTP transport failures without rethrowing (`TaskEventProducerService.java:178-204`). The processor continues to line 313 and transmits an `AA` ACK upstream even if both event publication attempts failed completely.

**4.3 Ergon Processing and Egress**
```text
Petasos Message Consumer
  -> ErgonBase.processIngress -> TaskCacheService.savePragma(pragma)
  -> ErgonBase.processEgress
       -> TaskCacheService.savePragma(pragma)
       -> TaskCacheService.saveTask(task)
       -> TaskCacheService.saveProvenance(provenance)
  -> Petasos Message Producer (Egress event)
```
- **Evidence**: `ErgonBase.java:244-300, 338-359`, `TaskCacheService.java:96-126`.
- **Failure Point**: Workflow transitions are recorded via `TaskCacheService` which falls back to local heap on cache failure. State logged as "persisted" (`ErgonBase.java:346-347`) is vulnerable to immediate process loss.

**4.4 Kleio Audit Control Case (Synchronous & Durable)**
```text
Audit Event Producer
  -> DurableAuditService.append(auditEvent) [@Transactional]
  -> JdbcAppendOnlyAuditEventRepository.insertIfAbsent(record)
  -> PostgreSQL INSERT via JDBC
  -> Transaction Commit -> Return Success
```
- **Evidence**: `DurableAuditService.java:105-129`, `JdbcAppendOnlyAuditEventRepository.java:96-109`.
- **Control Property**: Any database failure throws `AuditPersistenceException`, bubbling up directly to the caller. No intermediate write-behind or local memory substitution exists.

---

**5. Mneme / Infinispan Write-Behind Analysis**

**5.1 Configuration Analysis**  
`hestia/mneme-cluster/src/main/resources/infinispan.xml` defines caches (`task-cache`, `communication-cache`, `tasksequence-cache`, `messagequeue-cache`, etc.) with the following attributes:
- `mode="SYNC"` (synchronous cluster replication across Infinispan nodes).
- `preload="false"`, `fetch-state="false"`, `segmented="false"`.
- `<write-behind modification-queue-size="1024" />`.

**5.2 Exception Swallowing in REST Stores**  
In `FhirRestCacheStore.java`:
```java
// Line 110-121
return restClient.saveResourceJson(resourceType, id, json)
    .thenAccept(success -> {
        if (!success) {
            log.warn("Failed to save resource {}/{} to FHIR persistence", resourceType, id);
        }
    })
    .exceptionally(ex -> {
        log.error("Error writing resource {}/{} to FHIR persistence: {}", resourceType, id, ex.getMessage());
        return null; // Swallows exception and completes stage normally
    });
```
`OperationsRestCacheStore.java:114-126` contains identical logic.

**5.3 Sharpening the Boundary: Cache Loss vs. Acknowledged State Loss**
- **Benign Cache Loss**: If an authoritative database holds the primary record, an Infinispan node crash or cache eviction merely causes a cache miss. The subsequent read reloads the entity from Mnemosyne via `load()`.
- **Unacceptable Authoritative State Loss**: Under the current write-behind design, a caller receives HTTP 201/200 once the item is in Infinispan memory. If Mnemosyne is offline or the REST call fails, the modification queue is dropped or completes exceptionally without alerting the caller. When the cache entry is evicted or the cluster restarts, the data is permanently lost.

---

**6. Mnemosyne Failure Semantics**

1. **JPA Layer Behavior**: `FhirStorageService` and `OperationStorageService` in `hestia/mnemosyne-*` use Spring Data JPA repositories. Persistence errors throw standard `DataAccessException` / `TransactionSystemException`.
2. **Transaction Integrity**: Mnemosyne does not implement internal volatile fallback buffers. If PostgreSQL is unreachable, Mnemosyne REST endpoints fail with HTTP 500.
3. **Deployment Safety**: Manifests (`deployment/kubernetes/base/hestia/mnemosyne-*.yaml`) mandate `SPRING_PROFILES_ACTIVE: "postgres"`. Missing JDBC endpoints prevent startup rather than dropping into an embedded H2/in-memory mode.
4. **Root Vulnerability**: Mnemosyne correctly fails closed; the failure is masked exclusively by the upstream caching adapters (`FhirRestCacheStore`, `OperationsRestCacheStore`, and `FhirCacheService`).

---

**7. Explicit Volatile / In-Memory Fallback Findings**

| Module | File & Line Range | Code Pattern | Classification |
| :--- | :--- | :--- | :--- |
| `iris-befe` | `FhirCacheService.java`<br>lines 40-57, 219-234 | `localFallbackCaches.computeIfAbsent(...)`<br>Used on Hot Rod connection failure. | **Production Fallback (Violation)** |
| `iris-befe` | `TaskSequenceCacheService.java`<br>lines 58-66, 254-266 | `localFallbackCache.put(id, sequence)`<br>Catches remote cache error and returns raw JSON. | **Production Fallback (Violation)** |
| `pylai-mllp-base` | `DefaultTaskService.java`<br>lines 55, 98-113 | `taskStore.put(task.getId(), task)`<br>Updates local map first; logs remote cache error and returns task. | **Production Fallback (Violation)** |
| `pylai-mllp-base` | `DefaultProvenanceService.java`<br>lines 48, 89-105 | `provenanceStore.put(prov.getId(), prov)`<br>Updates local map first; logs remote cache error and returns provenance. | **Production Fallback (Violation)** |
| `energeia-erga` | `TaskCacheService.java`<br>lines 58-63, 224-268 | `localFallbackCache.put(id, task)`<br>Provides local fallback for tasks and pragmas. | **Production Fallback (Violation)** |
| `energeia-praxis` | `PragmaCacheService.java`<br>lines 59, 106-125 | `localFallbackCache.put(id, new Pragma(pragma))`<br>Local insertion executed before remote cache attempt. | **Production Fallback (Violation)** |
| `energeia-ponos` | `MessageQueueService.java`<br>lines 48, 134-157 | `localFallbackCache.put(name, queue)`<br>Logs remote cache failure and returns queue definition. | **Production Fallback (Violation)** |
| `energeia-praxis` | `PraxisService.java`<br>lines 49, 137-164 | `localFallbackCache.put(name, seq)`<br>Logs "Persisted in local fallback" and returns sequence. | **Production Fallback (Violation)** |
| `iris-befe` | `ModuleStatusService.java`<br>lines 35-43, 63-85 | `localFallbackCache.put(id, status)`<br>Maintains transient module heartbeat/status. | **Volatile Operational State (Low Risk / Reconstructable)** |
| `kleio-core` | `InMemoryAuditService.java`<br>lines 41-108 | In-memory `List<AuditEvent>` backing. | **Test / Exemplar Only** (Unsatisfied in production CDI) |

---

**8. Workflow, Ponos, Ergon, and Petasos State Findings**

1. **Pragma and Task Lifecycle**:
    - In `ErgonBase.java:279, 338-359`, workflow steps checkpoint state through `TaskCacheService` and `PragmaCacheService`.
    - Because these cache services rely on local maps when Infinispan fails, an in-flight workflow checkpoint can be completely lost during a container restart, preventing recovery of in-flight work.
2. **Workflow Dispatcher State**:
    - `PragmaWorkflowDispatcher.registeredWorkflows` (`PragmaWorkflowDispatcher.java:81`) is an in-memory registry of CDI handler beans. This is legitimate volatile process state because it is reconstructed deterministically during application bootstrap.
3. **Petasos Work Handoff**:
    - Petasos message envelopes are designed to carry causality, correlation, and tracking identifiers (`PetasosMessage.java:59`). While messaging transitions provide durable work handoffs, they do not replace durable application state in Mnemosyne.

---

**9. Petasos and Artemis Durability Findings**

1. **Broker Configuration**:
    - `petasos/deployment/artemis/standalone/broker.xml:15-41` and `deployment/kubernetes/base/petasos/artemis-configmaps.yaml:16-77` establish `<persistence-enabled>true</persistence-enabled>`, NIO journal storage, replication across primary/backup brokers, and dead-letter queue (DLQ) addresses.
2. **Producer & Consumer Contracts**:
    - `PetasosProducerConfig.java:78-82` sets `defaultDurable=true` and `syncDurableSend=true`.
    - `PetasosConsumerConfig.java:91-94` and `ArtemisPetasosSubscription.java:94-158, 226-268` enforce `CLIENT_ACKNOWLEDGE` mode. Failures trigger `session.recover()` or explicit DLQ routing.
3. **Queue Creation Ambiguity (Caveat)**:
    - In `TaskEventProducerService.java:140`, queues are created dynamically via `context.createQueue(queueName)`. While the Artemis broker config sets `<auto-create-queues>true</auto-create-queues>`, explicit durability parameters are omitted in the JMS invocation. Production destinations should be confirmed as durable queue bindings.

---

**10. Runtime and Deployment Composition Findings**

1. **Lazy Hot Rod Client Initialization**:
    - In `pylai/pylai-mllp-base/.../HotRodClientProducer.java:39-74` and `energeia/ponos/.../HotRodClientProducer.java:39-74`, the CDI producer attempts eager connection to Infinispan (`127.0.0.1:11222`). If the cluster is unreachable, it catches the exception and instantiates `new RemoteCacheManager(..., false)` (lazy startup).
    - *Impact*: In an environment where Infinispan is misconfigured or offline, the container starts without error, returns `null` from `getRemoteCache()`, and silently routes all production traffic into local heap `ConcurrentHashMap` instances.
2. **Environment URL Defaults**:
    - `FhirStoreConfiguration.java:29-35` defaults `fhir.store.rest.url` to `http://localhost:8080/fhir`. Missing environment variables in container deployments fall back to localhost rather than halting startup.

---

**11. Existing Test Coverage and Critical Testing Gaps**

**11.1 Current Test Strengths**
- **Broker Crash / Recovery**: `Checkpoint1MessageFlowIntegrationTest.java:260-318` validates Artemis stop/restart recovery and client reconnection.
- **Consumer Acknowledgement**: `ArtemisPetasosSubscriptionTest.java:98-271` validates client acknowledge, duplicate skipping, and redelivery on error.
- **Audit Persistence Verification**: `KleioRuntimeCompositionTest.java` and `DurableAuditServicePostgreSqlConcurrencyTest.java` verify that audit persistence is strictly transactional and that in-memory stubs are excluded.

**11.2 Critical Testing Gaps**
- **Write-Behind Drop Scenarios**: No test exists to verify system behavior when `FhirRestCacheStore` encounters an HTTP 500 or timeout from Mnemosyne.
- **Fail-Closed Verification on Cache Outage**: No integration tests verify that `FhirCacheService`, `DefaultTaskService`, or `PragmaCacheService` fail closed (returning 5xx/errors) when Infinispan and local fallbacks are disabled.
- **Restart Recovery of Acknowledged Entities**: No tests verify that a FHIR Task or MLLP ADT message acknowledged to an external client is recoverable from disk after an immediate hard `kill -9` of the JVM.
- **Ingress Dual-Write Failure Propagation**: No test verifies that MLLP returns an `AE` NACK when `TaskEventProducerService` fails to publish to Artemis.

---

**12. Confirmed Task 07 Violations**

| Violation ID | Component & File | Exact Behavior | Failure & Loss Impact | Remediation Boundary |
| :--- | :--- | :--- | :--- | :--- |
| **VIO-01** | `FhirRestCacheStore.java:118-121`<br>`OperationsRestCacheStore.java:123-126` | Catches async REST persistence exceptions and returns `null` completed stage. | Mnemosyne write failure is hidden; acknowledged state disappears upon cache eviction. | Make cache persistence synchronous or propagate write-behind failure; fail operations that cannot be made durable. |
| **VIO-02** | `FhirCacheService.java:40-57, 136-188` | Stores resources in `localFallbackCaches` when RemoteCache is absent or throws error. | Client receives HTTP 201/200; state exists only in JVM heap and is lost on restart. | Remove local memory fallback for authoritative writes; fail closed with HTTP 503. |
| **VIO-03** | `DefaultTaskService.java:80-113`<br>`DefaultProvenanceService.java:73-105` | Updates `taskStore` / `provenanceStore` first; swallows remote cache exceptions. | MLLP pipeline assumes Task is stored; gateway restart destroys task state. | Remove local map fallback; require durable Mnemosyne acceptance. |
| **VIO-04** | `TaskEventProducerService.java:175-204` | Catches JMS exception, tries HTTP fallback, catches HTTP exception, returns normally. | Ingress gateway sends `AA` ACK even though message was never published or persisted. | Propagate exception on total dispatch failure to trigger MLLP `AE` NACK (REC-001). |
| **VIO-05** | `TaskCacheService.java:58-63, 224-318`<br>`PragmaCacheService.java:59, 106-125` | Writes Pragma/Task to local map before/instead of durable store. | Ergon activity logs state as persisted; restart aborts workflow recovery. | Remove local fallback; ensure workflow checkpoint persistence fails closed. |
| **VIO-06** | `MessageQueueService.java:134-157`<br>`PraxisService.java:137-164`<br>`TaskSequenceCacheService.java:254-266` | Saves queue/workflow configuration to `localFallbackCache` when cache fails. | Topology/sequence updates appear successful but revert on container restart. | Enforce fail-closed persistence against Mnemosyne Operations. |
| **VIO-07** | `HotRodClientProducer.java:39-74` (Pylai & Ponos) | Falls back to lazy unstarted `RemoteCacheManager` on initial connection failure. | System silently activates volatile local fallback maps in production containers. | Fail container startup if required persistence infrastructure is unreachable. |

---

**13. Legitimate Volatile State and Non-Violations**

To prevent overengineering, the following components are recognized as valid volatile stores that do **not** violate Task 07:
1. **`PragmaWorkflowDispatcher.registeredWorkflows`**: CDI registration map storing references to instantiated pipeline beans. Purely in-memory, deterministic, and reconstructed at startup.
2. **`PhiLoggerFactory.LOGGER_CACHE`**: Internal utility cache for parameterized logger instances.
3. **Themis Decision & Context Objects**: Immutable evaluation models (`ThemisDecision`, `PragmaSecurityContext`) instantiated per request.
4. **`ModuleStatusService.localFallbackCache`**: High-frequency node heartbeats. While remote caching is preferred, node status is ephemeral and continuously refreshed by active nodes.
5. **Read Caches with Authoritative Backing**: An Infinispan cache functioning strictly as a read-through/cache-aside accelerator is valid, provided it does not intercept and falsely acknowledge writes.

---

**14. Residual Questions and Suggestive Caveats Requiring Confirmation**

1. **JMS Queue Durability Declaration**: In `TaskEventProducerService.java:140`, `context.createQueue(queueName)` is used without explicit queue attribute flags. While broker XML enables auto-creation, a test is needed to verify whether auto-created queues inherit persistent disk backing by default under Artemis JMS 2.0.
2. **Authoritative vs. Reconstructable Status of Pragma Envelopes**: Is an in-flight `Pragma` strictly required to be persisted to Mnemosyne at every step, or can it be safely reconstructed from the originating Petasos queue message during consumer redelivery? (This dictates whether `TaskCacheService` must write to Mnemosyne or rely on Artemis transaction boundaries).
3. **Seeding of Operations Defaults**: `TaskSequenceCacheService.ensureDefaultSequences` seeds defaults into local memory if the cache is empty. It must be determined whether bootstrap defaults should be written directly to Mnemosyne Operations upon initial deployment.

---

**15. Recommended Small Task 07 Implementation Blocks**

To maintain architectural focus without prematurely absorbing Tasks 08–10 (Clinical write/read/search redesign), Task 07 should be partitioned into four cohesive, independently testable implementation blocks:

```text
+---------------------------------------------------------------------------------------+
| Block 07.1: Eliminate Write-Behind Exception Masking in Mneme                         |
|   - Disable async write-behind for authoritative caches in infinispan.xml             |
|   - Propagate exceptions in FhirRestCacheStore & OperationsRestCacheStore             |
|   - Verify that Mnemosyne REST failure immediately fails the cache operation          |
+---------------------------------------------------------------------------------------+
                                           |
                                           v
+---------------------------------------------------------------------------------------+
| Block 07.2: Remove Production Local Memory Fallbacks                                  |
|   - Remove localFallbackCaches from FhirCacheService, DefaultTaskService,             |
|     DefaultProvenanceService, TaskCacheService, and PragmaCacheService                |
|   - Enforce fail-closed (HTTP 503 / PersistenceException) on cache/store outage       |
|   - Remove memory fallback in PraxisService, MessageQueueService, TaskSequenceService |
+---------------------------------------------------------------------------------------+
                                           |
                                           v
+---------------------------------------------------------------------------------------+
| Block 07.3: Ingress Dual-Write Safety and Dispatch Failure Propagation (REC-001)      |
|   - Update TaskEventProducerService.sendTaskEvent to throw on total failure           |
|   - Ensure IncomingAdtMessageProcessor returns AE NACK on dispatch exception          |
|   - Remove lazy HotRodClientProducer fallback; fail startup on cache connection error |
+---------------------------------------------------------------------------------------+
                                           |
                                           v
+---------------------------------------------------------------------------------------+
| Block 07.4: Failure and Durability Contract Verification Test Suite                   |
|   - Add integration tests for Mnemosyne outage and write-behind failure rejection     |
|   - Add tests verifying process restart recovery of acknowledged state                |
|   - Add tests verifying MLLP AE NACK generation on Petasos/HTTP dispatch outage       |
+---------------------------------------------------------------------------------------+
```

**Detailed Scope of Blocks:**

- **Block 07.1: Eliminate Write-Behind Exception Masking in Mneme**
    - **Invariant**: Cache store writes to Mnemosyne must not report success if the durable write fails.
    - **Target**: `hestia/mneme-persistence` (`FhirRestCacheStore`, `OperationsRestCacheStore`), `hestia/mneme-cluster` (`infinispan.xml`).
    - **Action**: Remove async write-behind (`<write-behind>`) for authoritative clinical/operations caches; remove exception-swallowing `.exceptionally(... return null)` handlers.

- **Block 07.2: Remove Production Local Memory Fallbacks**
    - **Invariant**: State must never be accepted into volatile heap as a substitute for unavailable durable persistence.
    - **Target**: `iris/iris-befe` (`FhirCacheService`, `TaskSequenceCacheService`), `pylai/pylai-mllp-base` (`DefaultTaskService`, `DefaultProvenanceService`), `energeia/erga` (`TaskCacheService`), `energeia/praxis` (`PragmaCacheService`, `PraxisService`), `energeia/ponos` (`MessageQueueService`).
    - **Action**: Eliminate `ConcurrentHashMap` fallback maps for authoritative entities; throw actionable service exceptions when remote infrastructure is unreachable.

- **Block 07.3: Ingress Dual-Write Safety and Dispatch Failure Propagation (REC-001)**
    - **Invariant**: Downstream publication or persistence failure must prevent positive acknowledgement (`AA` ACK) to upstream senders.
    - **Target**: `pylai/pylai-mllp-base` (`TaskEventProducerService`, `HotRodClientProducer`), `pylai/pylai-mllp-in` (`IncomingAdtMessageProcessor`), `energeia/ponos` (`HotRodClientProducer`).
    - **Action**: Propagate exceptions across `sendTaskEvent`; enforce fail-fast container bootstrap in `HotRodClientProducer`.

- **Block 07.4: Failure and Durability Contract Verification Test Suite**
    - **Invariant**: Every failure-handling and durability boundary must be asserted by automated tests.
    - **Target**: `hestia/mneme-persistence/src/test/...`, `pylai/pylai-mllp-in/src/test/...`, `energeia/ponos/src/test/...`.
    - **Action**: Implement targeted tests for database outage, cache disconnection, hard process crash recovery, and MLLP `AE` NACK generation.

---

**Discoveries for Future Steps**

- **High-Signal Inspection Targets**: The root cause of false durability in Harmonia is concentrated in `infinispan.xml`, the two REST cache stores (`FhirRestCacheStore`, `OperationsRestCacheStore`), and the `localFallbackCache` maps in the service layer.
- **Mnemosyne is Inherently Robust**: Mnemosyne's JPA layer correctly fails closed and throws exceptions during database outages; it requires no internal fallback remediation.
- **Kleio is the Proven Template**: `DurableAuditService` demonstrates the exact pattern needed: synchronous, transactional persistence that propagates database errors immediately to the caller.
- **Artemis Messaging Integrity**: Petasos/Artemis messaging is already configured for durable synchronous delivery and client acknowledgements; work should focus on gateway exception propagation rather than re-architecting Artemis.



**Summary**
- Completed a read-only architectural exploration for Task 07 Step 01 and identified confirmed false-durability risks where Harmonia can acknowledge success before durable acceptance: Mneme Infinispan write-behind, production local-memory fallbacks, and MLLP task-event dispatch failure masking.
- Distinguished true defects from legitimate volatility: Mnemosyne PostgreSQL and Kleio audit are authoritative durable paths; Petasos/Artemis is durable work/transition; several `ConcurrentHashMap` registries/status caches are transient only when explicitly reconstructable.

**Changes**
- Mapped major write paths with concrete code references: BEFE REST resources -> `FhirCacheService` -> Infinispan -> `FhirRestCacheStore` -> Mnemosyne; Pylai MLLP ADT ingress -> `DefaultTaskService` / `DefaultProvenanceService` / `TaskEventProducerService`; Ergon/Praxis workflow checkpoints -> `TaskCacheService` / `PragmaCacheService`; Kleio control path -> `DurableAuditService` -> JDBC append-only repository.
- Confirmed primary Task 07 violations: `FhirRestCacheStore` and `OperationsRestCacheStore` swallow async persistence failure; `FhirCacheService`, `DefaultTaskService`, `DefaultProvenanceService`, `TaskCacheService`, `PragmaCacheService`, `PraxisService`, `MessageQueueService`, and `TaskSequenceCacheService` can return success after only local-map/cache acceptance; `TaskEventProducerService` can return normally after both JMS and HTTP dispatch fail.
- Confirmed runtime composition risks and non-risks: Hot Rod producers in Pylai/Ponos allow lazy startup that activates volatile local fallbacks when Infinispan is unreachable; Mnemosyne itself does not silently switch to in-memory mode and appears to fail closed on DB outage; attached Task 06 audit findings remain a valid control case, not a Task 07 defect.
- Recommended coherent implementation blocks: (07.1) remove async write-behind false-success semantics, (07.2) remove production local-memory substitutes for authoritative/workflow writes, (07.3) enforce ingress dispatch failure propagation for REC-001, (07.4) fail startup rather than silently enabling volatile fallback where durability is required, (07.5) add outage/restart/failure-contract tests.

**Verification**
- Read-only repository exploration only; no code modified and no broad suites run. Findings are grounded in inspected repository files including `hestia/mneme-cluster/infinispan.xml`, `hestia/mneme-persistence/*CacheStore.java`, `iris-befe/FhirCacheService.java`, `pylai-mllp-base/DefaultTaskService.java`, `DefaultProvenanceService.java`, `TaskEventProducerService.java`, `energeia/*CacheService.java`, `petasos/*`, and deployment manifests.

**Notes**
- Important caveats for follow-up: verify whether Artemis auto-created queues are durable by default; decide which workflow/process objects are authoritative durable state versus reconstructable transient state; add explicit tests for Mnemosyne outage, cache-store failure, restart recovery, and AE/NACK behavior on total dispatch failure.

