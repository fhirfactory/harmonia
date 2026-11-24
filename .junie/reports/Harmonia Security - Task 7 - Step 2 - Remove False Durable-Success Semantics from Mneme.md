Optional spending limit; leave empty for no limit: 50
Required for Goal Mode: Auto
Pause for plan review before starting the goal: No

EXPLORATION

**Task description**  
Inspect the current Task 07 Step 02 change surface for removing false durable-success semantics from Mneme. Map the exact files, approximate line numbers, and current behavior for:
- `FhirRestCacheStore.write(...)`
- `OperationsRestCacheStore.write(...)`
- the REST client save methods they call and how false/exceptional results are handled
- relevant Infinispan cache-store configuration, especially any `<write-behind ...>` entries for authoritative-write caches
- existing focused tests around these cache stores / config

Also identify the smallest coherent implementation/test/configuration areas likely required for this step, and note any obvious exclusions that must remain untouched per the issue.

**Constraints**  
Read-only exploration only. Do not modify files. Be concise but include exact paths and approximate line numbers.



Exploration is Ready

Read-only inspection completed; no files were modified and no tests were run.

**1. Current behavior confirmed**

**FHIR adapter**  
Path: `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`

- `write(...)`: lines 99-122.
- Null entry/key/value returns `CompletableFuture.completedFuture(null)` at lines 100-102.
- Normal REST completion is handled with `thenAccept(...)` at lines 110-117. A `false` result is only logged at lines 112-114; it does not fail the stage.
- Exceptional REST completion is handled by `.exceptionally(...)` at lines 118-121, which logs and returns `null`. This converts the exceptional completion into a normally completed `CompletionStage<Void>`.
- The store exposes `setRestClient(...)` at lines 183-189, so focused tests can inject a mocked client.

**Operations adapter**  
Path: `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/OperationsRestCacheStore.java`

- `write(...)`: lines 103-127.
- Null entry/key/value returns a completed stage at lines 104-106.
- `thenAccept(...)` at lines 114-121 only logs whether the REST client returned true or false; false remains successful from the adapter's perspective.
- `.exceptionally(...)` at lines 122-126 logs and returns `null`, swallowing transport/REST exceptions.
- `setRestClient(...)` is available at lines 265-271 for mocked failure tests.

**2. REST client behavior**

**FHIR client**  
Path: `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/HapiFhirRestClient.java`

- `saveResourceJson(...)`: lines 74-95.
- HTTP 2xx produces `true`.
- Any non-2xx response produces `false` after logging at lines 85-93.
- `HttpClient.sendAsync(...)` failures are not caught in this method, so transport/unavailable-server errors remain exceptional until the adapter's current `.exceptionally(...)` handler swallows them.

**Operations client**  
Path: `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/OperationsRestClient.java`

- `saveResourceJson(...)`: lines 106-127.
- HTTP 2xx produces `true`; non-2xx produces `false` and logs safe operational metadata at lines 117-125.
- `sendAsync(...)` exceptions are not caught here and therefore remain exceptional until swallowed by `OperationsRestCacheStore.write(...)`.

The REST clients already distinguish successful and failed HTTP responses by returning `Boolean`; the narrowest correction is therefore in the two cache-store adapters, not necessarily in the clients.

**3. Infinispan configuration**

Path: `hestia/mneme-cluster/src/main/resources/infinispan.xml`

- The configuration comment at lines 59-60 explicitly describes the caches as “Write-Behind Persistence”.
- All 17 replicated caches have a REST-backed store and `<write-behind modification-queue-size="1024" />`:
    - 14 FHIR stores use `FhirRestCacheStore`: `person-cache`, `relatedperson-cache`, `practitioner-cache`, `practitionerrole-cache`, `organization-cache`, `location-cache`, `healthcareservice-cache`, `group-cache`, `provenance-cache`, `auditevent-cache`, `consent-cache`, `task-cache`, `communication-cache`, and `documentreference-cache`.
    - Their write-behind entries occur at lines 70, 85, 100, 115, 130, 145, 160, 175, 189, 203, 217, 231, 245, and 259 respectively.
    - 3 operations stores use `OperationsRestCacheStore`: `tasksequence-cache`, `messagequeue-cache`, and `modulestatus-cache`.
    - Their write-behind entries occur at lines 273, 287, and 301 respectively.
- No separately configured cache-only store is evident in this XML; every defined cache has one of the two REST persistence adapters.
- Because write-behind queues the store mutation separately from the cache operation, the originating cache write cannot reliably observe a later Mnemosyne failure. Removing exception swallowing alone is therefore insufficient for the requested originating-operation failure semantics. The likely minimum configuration change is removal of asynchronous write-behind from the affected REST-backed authoritative-write stores, leaving the store itself configured for synchronous persistence.

Related configuration implementation:

- `FhirStoreConfiguration.java`: lines 41-43 retain `AsyncStoreConfiguration` in the store configuration.
- `OperationsStoreConfiguration.java`: lines 41-43 do the same.
- `FhirStoreConfigurationBuilder.java`: lines 52-55 passes `async.create()` into the configuration.
- `OperationsStoreConfigurationBuilder.java`: lines 51-54 passes `async.create()` into the configuration.

These classes may not require structural changes if synchronous operation is achieved by omitting the async configuration, but they are part of the configuration surface to verify.

**4. Existing focused tests**

**FHIR store tests**  
Path: `hestia/mneme-persistence/src/test/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStoreTest.java`

- WireMock setup: lines 49-60; store initialization/configuration: lines 63-89.
- Existing successful write test: lines 98-112. It verifies a PUT and waits for normal completion, but does not assert the returned stage semantics for false or exceptional REST results.
- Existing load/delete/coordinate tests: lines 114-162.
- The test currently constructs `FhirStoreConfiguration` with a mocked `AsyncStoreConfiguration` at lines 82-86, but this is only initialization; it does not verify synchronous versus asynchronous persistence.

**Operations store tests**  
Path: `hestia/mneme-persistence/src/test/java/net/fhirfactory/harmonia/persistence/store/OperationsRestCacheStoreTest.java`

- WireMock setup: lines 46-58; store initialization/configuration: lines 60-85.
- Existing successful write test: lines 95-109.
- Existing load/delete/contains/publish/coordinate tests: lines 111-202.
- The test also constructs `OperationsStoreConfiguration` with a mocked `AsyncStoreConfiguration` at lines 79-83 without asserting async behavior.

**Existing client failure coverage**  
Path: `hestia/mneme-persistence/src/test/java/net/fhirfactory/harmonia/persistence/client/OperationsRestClientLoggingTest.java`

- Lines 90-136 verify that a 500 PUT causes `OperationsRestClient.saveResourceJson(...)` to return `false` and logs safe metadata.
- This does not exercise `OperationsRestCacheStore.write(...)`, so it does not protect the adapter failure contract.
- No equivalent focused FHIR save-client failure test was found.

**Cluster configuration test**  
Path: `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/InfinispanClusterConfigTest.java`

- FHIR test configuration explicitly enables async persistence at lines 49-58.
- Operations test configuration explicitly enables async persistence at lines 62-73.
- It defines only representative caches at lines 78-83: three FHIR caches and three operations caches.
- The only test, lines 94-120, verifies cache instantiation; it does not assert that the store is synchronous or that async write-behind is disabled.
- This test builds configurations programmatically and does not directly parse/validate the production `infinispan.xml`, so XML changes and test-builder changes can currently drift independently.

**5. Smallest coherent implementation/test/configuration areas**

1. Change only the `write(...)` methods in:
    - `FhirRestCacheStore.java`, lines 99-122.
    - `OperationsRestCacheStore.java`, lines 103-127.
      The false result should be mapped to a failed stage, and no terminal `exceptionally(... return null)` handler should remain on this write path. Existing REST exceptions should pass through unchanged or wrapped only with an existing/local exception vocabulary if required.

2. Extend the two existing store tests with three focused cases each:
    - true result completes successfully;
    - false result completes exceptionally;
    - exceptional client completion remains exceptional.
      Mockito injection through the existing `setRestClient(...)` methods is the smallest route; WireMock can also verify false HTTP responses.

3. Since write-behind prevents the originating cache operation from observing the persistence result, update the affected REST-backed cache definitions in `infinispan.xml` by removing their async write-behind elements. The current evidence shows all 14 FHIR and all 3 operations stores are REST-backed; there is no clearly cache-only store in this configuration to preserve. Keep cache mode, encoding, store class, properties, and other attributes unchanged.

4. Update `InfinispanClusterConfigTest.java` to build synchronous store configurations instead of calling `.async().enable()`, and add assertions that the affected store configurations are not asynchronous. The representative test should remain otherwise unchanged, while ideally covering the production XML separately if the project already has a suitable parser/test mechanism.

**6. Explicit exclusions**

The following should remain untouched in this step, consistent with the issue:

- BEFE/local fallback behavior, including `iris/iris-befe/.../FhirCacheService.java`.
- `DefaultTaskService`, `DefaultProvenanceService`, `TaskCacheService`, `PragmaCacheService`, `PraxisService`, `MessageQueueService`, and `TaskSequenceCacheService` local fallback paths.
- `ModuleStatusService`, `TaskEventProducerService`, and `IncomingAdtMessageProcessor`.
- Petasos APIs, Artemis producer/consumer semantics, Mnemosyne JPA/repository architecture, Kleio, clinical search, cache-aside reads, and the final Clinical write-path redesign.

Other `exceptionally(... return null)` handlers in non-write operations, such as operational publish/list flows, were not part of this narrow adapter-write correction and should not be broadened without a separate requirement.

**7. Test status and implications**

- No tests were run because this was explicitly read-only exploration.
- The key Task 08 implication is that synchronous cache-store persistence is only a correctness measure for the current architecture; it does not establish the final authoritative Clinical write path. Task 08 may replace the cache-mediated write route entirely.
- No local-memory fallback behavior was changed.
- Petasos, Mnemosyne, and Kleio architecture was not redesigned.

**Discoveries for future steps**

- Production XML has 17 write-behind entries, while `InfinispanClusterConfigTest` independently creates six representative async configurations; both surfaces must be kept consistent.
- The REST clients already return `false` for non-2xx PUT responses and leave transport errors exceptional, so adapter-level failure propagation is the smallest code change.
- Existing store tests expose REST-client setters, making false/exception tests possible without changing production constructors or introducing a new abstraction.



PLAN THE TASK

**Task description**  
Plan the whole task.

**Feedback**  
Use the current exploration findings already established for Task 07 Step 02. Focus on the smallest coherent remediation that preserves the issue's narrow scope:
- confirm the current false-success behavior in `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)`;
- propagate false REST results and exceptional completions as persistence failures;
- determine and include the minimum Infinispan config/test changes needed if async write-behind blocks originating-operation failure propagation;
- add focused store/config tests first, then relevant module and architecture verification;
- explicitly keep Task 08 redesign, cache-aside reads, search redesign, Petasos changes, and local fallback behavior out of scope.

Please produce a practical stepwise plan with acceptance criteria and verification for each step, and ensure the final report requirements from the issue are covered.



**Requirements**

**Goal & Outcome**  
Enforce the authoritative state boundary established in ADR-018 by eliminating false durable-success semantics in Mneme (`FhirRestCacheStore` and `OperationsRestCacheStore`). A failed Mnemosyne write (explicit rejection, HTTP error, or transport crash) must fail the persistence stage and propagate synchronously to originating cache operations rather than being swallowed as a successful completion.

**Scope**
- **In Scope**:
    - Remediate `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` to return failed completion stages on false REST results or exceptional completions.
    - Remove asynchronous `<write-behind modification-queue-size="1024" />` configurations from authoritative-write caches in `infinispan.xml` (all 14 FHIR caches and 3 Operations caches).
    - Update `InfinispanClusterConfigTest` to configure synchronous stores and verify non-async store attributes.
    - Expand `FhirRestCacheStoreTest` and `OperationsRestCacheStoreTest` to verify success, false-result failure, and exceptional-result failure contracts.
    - Run focused tests, module test suites, and ArchUnit architecture tests.
- **Out of Scope**:
    - Redesigning the Clinical write path, direct BEFE -> Mnemosyne writes, or `FhirCacheService` (Task 08).
    - Cache-aside read behaviors (Task 09) and Clinical search redesign (Task 10).
    - Petasos messaging transitions or Artemis producer/consumer semantics.
    - Modifying local in-memory fallback paths (`FhirCacheService`, `DefaultTaskService`, `DefaultProvenanceService`, `TaskCacheService`, `PragmaCacheService`, `PraxisService`, `MessageQueueService`, `TaskSequenceCacheService`, `ModuleStatusService`, `TaskEventProducerService`, `IncomingAdtMessageProcessor`).
    - Mnemosyne JPA/repository architecture or Kleio durability redesign.

**Done When**
- `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` complete exceptionally when REST calls return `false` or throw exceptions.
- Neither adapter swallows errors via terminal `.exceptionally(err -> return null)` on write operations.
- `infinispan.xml` and `InfinispanClusterConfigTest` have async write-behind disabled for all 17 authoritative persistence caches.
- Focused store tests and configuration tests pass, proving failure propagation and synchronous configuration.
- ArchUnit architecture tests pass (`*ArchitectureTest`).

**Technical Design**

**Decisions**
- **Decision: Propagate failed stages using standard `PersistenceException` (or failed `CompletionStage`) / not swallowing via `.exceptionally(...)`**
    - *Rationale*: Infinispan `NonBlockingStore.write(...)` requires a failed `CompletionStage` to signal persistence failure to the cache engine. Using standard `org.infinispan.persistence.spi.PersistenceException` or failing the future preserves standard Infinispan error propagation without introducing unnecessary custom exception hierarchies.
- **Decision: Remove async write-behind from `infinispan.xml` for all 17 REST-backed replicated caches / not retaining async write-behind**
    - *Rationale*: Infinispan's `AsyncNonBlockingStore` (write-behind) buffers writes in an in-memory queue and acknowledges the caller immediately. If write-behind is enabled, cache mutators cannot observe downstream store failures regardless of adapter logic. Switching to synchronous cache-store persistence aligns with ADR-018's current-architecture correctness requirements.

**Approach & Touches**
- **Adapter remediation**:
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`: In `write(...)`, map `Boolean` result from `restClient.saveResourceJson(...)` via `thenCompose(...)` or `thenApply(...)` such that `false` returns `CompletableFuture.failedStage(new PersistenceException(...))`; remove `.exceptionally(...)` that converts errors to `null`.
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/OperationsRestCacheStore.java`: Apply identical failure propagation in `write(...)`.
- **Configuration & Cluster remediation**:
    - `hestia/mneme-cluster/src/main/resources/infinispan.xml`: Remove `<write-behind modification-queue-size="1024" />` from all 14 FHIR replicated caches (`person-cache`, `relatedperson-cache`, `practitioner-cache`, `practitionerrole-cache`, `organization-cache`, `location-cache`, `healthcareservice-cache`, `group-cache`, `provenance-cache`, `auditevent-cache`, `consent-cache`, `task-cache`, `communication-cache`, `documentreference-cache`) and all 3 Operations caches (`tasksequence-cache`, `messagequeue-cache`, `modulestatus-cache`).
    - `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/InfinispanClusterConfigTest.java`: Remove `.async().enable().modificationQueueSize(1024)` from both builder configurations; add assertions verifying `config.async().enabled()` is `false`.

**Nuances / Risks / Corners**
- `write(...)` must continue handling `null` entry/key/value safely (returning `CompletableFuture.completedFuture(null)`).
- `delete(...)`, `containsKey(...)`, and `publish*` methods should not be regressed.
- WireMock and Mockito in tests must verify that `CompletionException` / `ExecutionException` contains the underlying failure cause when `.get()` or `.join()` is called on failed stages.

**Suspected Areas (Defect Root Causes)**
1. `FhirRestCacheStore.java:110-121`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
2. `OperationsRestCacheStore.java:114-126`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
3. `infinispan.xml:70,85,100,115,130,145,160,175,189,203,217,231,245,259,273,287,301`: `<write-behind .../>` decouples store execution from cache caller.

**Testing**

- **Scenario: FHIR Store Write Success** — REST client returns `true` (or WireMock 200) -> stage completes normally.
- **Scenario: FHIR Store Write Rejected / False** — REST client returns `false` (or WireMock 500) -> stage completes exceptionally with persistence failure.
- **Scenario: FHIR Store REST Exception** — REST client completes exceptionally (transport error / ConnectException) -> stage completes exceptionally with cause preserved.
- **Scenario: Operations Store Write Success** — REST client returns `true` (or WireMock 200) -> stage completes normally.
- **Scenario: Operations Store Write Rejected / False** — REST client returns `false` (or WireMock 500) -> stage completes exceptionally with persistence failure.
- **Scenario: Operations Store REST Exception** — REST client completes exceptionally (transport error) -> stage completes exceptionally with cause preserved.
- **Scenario: Cluster Configuration Synchronous Persistence** — `InfinispanClusterConfigTest` asserts `async().enabled() == false` on all defined store configurations.
- **Regression targets**:
    - `hestia/mneme-persistence` suite (`FhirRestCacheStoreTest`, `OperationsRestCacheStoreTest`, `OperationsRestClientLoggingTest`).
    - `hestia/mneme-cluster` suite (`InfinispanClusterConfigTest`).
    - ArchUnit architecture test suite (`*ArchitectureTest`).

**Assumptions & Open Questions**

- **Significant Assumption: PersistenceException for failure signaling**:
    - *Chosen Option*: Throw or wrap in `org.infinispan.persistence.spi.PersistenceException` (or propagate underlying `CompletionException`).
    - *Rationale*: Standard Infinispan SPI contract for failing asynchronous persistence operations; avoids inventing custom exception hierarchies.
    - *Alternative*: Creating a custom `MnemePersistenceException`.
    - *Impact*: Low; aligns with standard Infinispan error handling.
- **Significant Assumption: Removal of Write-Behind across all 17 caches in `infinispan.xml`**:
    - *Chosen Option*: Remove `<write-behind>` from all 14 FHIR and 3 Operations caches.
    - *Rationale*: All 17 caches currently back authoritative application state into Mnemosyne relational databases and have no cache-only status.
    - *Alternative*: Keeping write-behind on some caches.
    - *Impact*: Synchronous persistence guarantees caller visibility of persistence failures.

**Delivery Steps**

**Step 1: Remediate Adapter Write Failure Propagation and Store Unit Tests**  
Goal: Ensure `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` propagate failed REST results and exceptions as failed stages, verified by focused unit tests.  
Scope: `hestia/mneme-persistence` (`FhirRestCacheStore.java`, `OperationsRestCacheStore.java`, `FhirRestCacheStoreTest.java`, `OperationsRestCacheStoreTest.java`).  
Acceptance Criteria:
- [ ] `FhirRestCacheStore.write(...)` returns a failed `CompletionStage` when `restClient.saveResourceJson(...)` returns `false` or completes exceptionally.
- [ ] `OperationsRestCacheStore.write(...)` returns a failed `CompletionStage` when `restClient.saveResourceJson(...)` returns `false` or completes exceptionally.
- [ ] Terminal `.exceptionally(error -> return null)` is removed from both `write(...)` implementations.
- [ ] `FhirRestCacheStoreTest` has tests verifying: successful write (200 / true), failed write (false / 500), and exceptional write completion.
- [ ] `OperationsRestCacheStoreTest` has tests verifying: successful write (200 / true), failed write (false / 500), and exceptional write completion.
- [ ] All existing store tests (load, delete, coordinate resolution, publish) continue to pass.
  Verification: `mvn test -pl hestia/mneme-persistence -am` → green

**Step 2: Configure Synchronous Store Persistence and Validate Full Suite & Architecture**  
Goal: Remove asynchronous write-behind configuration from Infinispan cache definitions and verify cluster configuration and architectural invariants.  
Scope: `hestia/mneme-cluster` (`infinispan.xml`, `InfinispanClusterConfigTest.java`), full test suite, and architecture verification.  
Acceptance Criteria:
- [ ] `<write-behind modification-queue-size="1024" />` removed from all 14 FHIR and 3 Operations replicated caches in `infinispan.xml`.
- [ ] `InfinispanClusterConfigTest` builds store configurations without `.async().enable()` and asserts `async().enabled() == false`.
- [ ] `InfinispanClusterConfigTest` passes and verifies cache initialization for all representative caches.
- [ ] Architecture tests in `paradeigma-test` pass without regression.
- [ ] Final summary report includes all 10 required reporting items from the parent task specification.
  Verification: `mvn test -pl hestia/mneme-cluster,paradeigma/paradeigma-test -am -Dtest="InfinispanClusterConfigTest,*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



REVIEW THE PLAN

**Task description**  
Review the current delivery plan.

**Feedback**  
Focus on whether the plan is the smallest coherent path for Task 07 Step 02. Review especially:
- whether adapter failure propagation and write-behind configuration are split appropriately;
- whether removing `<write-behind>` from all 17 REST-backed caches is justified versus too broad;
- whether the plan includes enough focused tests to prove false/exceptional persistence remains failure;
- whether anything essential is missing for the issue's required completion report.

**Constraints**  
Keep review scoped to Task 07 Step 02 only; do not drift into Task 08 or broader redesign.



Plan Approved

**Verification**
- **Adapter behavior confirmed**: `FhirRestCacheStore.write(...)` (lines 99-122) and `OperationsRestCacheStore.write(...)` (lines 102-127) both use `.thenAccept(...)` that only logs a `false` result plus a terminal `.exceptionally(err -> return null)` that swallows exceptions — exactly the false-success defect the plan targets. Both stores expose `setRestClient(...)` enabling Mockito injection as the plan assumes.
- **Write-behind config confirmed**: `infinispan.xml` contains exactly 17 `<store class=...>` entries (14 `FhirRestCacheStore` + 3 `OperationsRestCacheStore`), each with a `<write-behind modification-queue-size="1024"/>` element (17 elements + 1 comment). All 17 are REST-backed authoritative-write stores; no cache-only store exists to be wrongly modified, so removing write-behind from all 17 is justified, not too broad.
- **Cluster test confirmed**: `InfinispanClusterConfigTest` builds both FHIR and Operations store configs with `.async().enable().modificationQueueSize(1024)` (lines 56-58, 71-73) and only asserts cache instantiation — matching the plan's update target.
- **Test infrastructure confirmed**: `FhirRestCacheStoreTest` uses WireMock + Mockito and already has a success-path `testWrite`; the plan's three-case expansion (success/false/exception) fits the existing harness.
- **Scope discipline confirmed**: Out-of-scope items (FhirCacheService, local fallbacks, Petasos/Artemis, Mnemosyne JPA, cache-aside reads, Task 08/09/10) are explicitly excluded; the two-step split (adapter+tests, then config+suite/arch) is coherent.
- **Reporting coverage confirmed**: Step 2 acceptance criteria explicitly require the final summary to include all 10 parent-task reporting items.

**Issues**  
None blocking.

**Notes**
- The plan's central architectural judgment (Option B: async write-behind fundamentally prevents the originating cache operation from observing store failure, so synchronous persistence is required) is sound and matches Infinispan's `AsyncNonBlockingStore` semantics. Removing write-behind while leaving `mode="SYNC"` and all other store attributes intact is the minimum correct change.
- The choice of `org.infinispan.persistence.spi.PersistenceException` (available on the infinispan-core classpath) over a custom exception aligns with the issue's "no new persistence framework" guardrail.
- Per-step `Verification:` commands use real module/targeted Maven invocations and are appropriate; testing depth is proportionate to this narrow remediation.

**Discoveries for future steps**
- Production `infinispan.xml` (17 write-behind entries) and `InfinispanClusterConfigTest` (6 representative programmatic configs) are independent surfaces that can drift — both must be kept consistent when changing persistence semantics.
- Existing store unit tests pass a mocked `AsyncStoreConfiguration` into the store `Configuration` constructor purely as wiring; this is unaffected by removing XML write-behind and needs no change for the failure-contract tests.



**Requirements**

**Goal & Outcome**  
Enforce the authoritative state boundary established in ADR-018 by eliminating false durable-success semantics in Mneme (`FhirRestCacheStore` and `OperationsRestCacheStore`). A failed Mnemosyne write (explicit rejection, HTTP error, or transport crash) must fail the persistence stage and propagate synchronously to originating cache operations rather than being swallowed as a successful completion.

**Scope**
- **In Scope**:
    - Remediate `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` to return failed completion stages on false REST results or exceptional completions.
    - Remove asynchronous `<write-behind modification-queue-size="1024" />` configurations from authoritative-write caches in `infinispan.xml` (all 14 FHIR caches and 3 Operations caches).
    - Update `InfinispanClusterConfigTest` to configure synchronous stores and verify non-async store attributes.
    - Expand `FhirRestCacheStoreTest` and `OperationsRestCacheStoreTest` to verify success, false-result failure, and exceptional-result failure contracts.
    - Run focused tests, module test suites, and ArchUnit architecture tests.
- **Out of Scope**:
    - Redesigning the Clinical write path, direct BEFE -> Mnemosyne writes, or `FhirCacheService` (Task 08).
    - Cache-aside read behaviors (Task 09) and Clinical search redesign (Task 10).
    - Petasos messaging transitions or Artemis producer/consumer semantics.
    - Modifying local in-memory fallback paths (`FhirCacheService`, `DefaultTaskService`, `DefaultProvenanceService`, `TaskCacheService`, `PragmaCacheService`, `PraxisService`, `MessageQueueService`, `TaskSequenceCacheService`, `ModuleStatusService`, `TaskEventProducerService`, `IncomingAdtMessageProcessor`).
    - Mnemosyne JPA/repository architecture or Kleio durability redesign.

**Done When**
- `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` complete exceptionally when REST calls return `false` or throw exceptions.
- Neither adapter swallows errors via terminal `.exceptionally(err -> return null)` on write operations.
- `infinispan.xml` and `InfinispanClusterConfigTest` have async write-behind disabled for all 17 authoritative persistence caches.
- Focused store tests and configuration tests pass, proving failure propagation and synchronous configuration.
- ArchUnit architecture tests pass (`*ArchitectureTest`).

**Technical Design**

**Decisions**
- **Decision: Propagate failed stages using standard `PersistenceException` (or failed `CompletionStage`) / not swallowing via `.exceptionally(...)`**
    - *Rationale*: Infinispan `NonBlockingStore.write(...)` requires a failed `CompletionStage` to signal persistence failure to the cache engine. Using standard `org.infinispan.persistence.spi.PersistenceException` or failing the future preserves standard Infinispan error propagation without introducing unnecessary custom exception hierarchies.
- **Decision: Remove async write-behind from `infinispan.xml` for all 17 REST-backed replicated caches / not retaining async write-behind**
    - *Rationale*: Infinispan's `AsyncNonBlockingStore` (write-behind) buffers writes in an in-memory queue and acknowledges the caller immediately. If write-behind is enabled, cache mutators cannot observe downstream store failures regardless of adapter logic. Switching to synchronous cache-store persistence aligns with ADR-018's current-architecture correctness requirements.

**Approach & Touches**
- **Adapter remediation**:
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`: In `write(...)`, map `Boolean` result from `restClient.saveResourceJson(...)` via `thenCompose(...)` or `thenApply(...)` such that `false` returns `CompletableFuture.failedStage(new PersistenceException(...))`; remove `.exceptionally(...)` that converts errors to `null`.
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/OperationsRestCacheStore.java`: Apply identical failure propagation in `write(...)`.
- **Configuration & Cluster remediation**:
    - `hestia/mneme-cluster/src/main/resources/infinispan.xml`: Remove `<write-behind modification-queue-size="1024" />` from all 14 FHIR replicated caches (`person-cache`, `relatedperson-cache`, `practitioner-cache`, `practitionerrole-cache`, `organization-cache`, `location-cache`, `healthcareservice-cache`, `group-cache`, `provenance-cache`, `auditevent-cache`, `consent-cache`, `task-cache`, `communication-cache`, `documentreference-cache`) and all 3 Operations caches (`tasksequence-cache`, `messagequeue-cache`, `modulestatus-cache`).
    - `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/InfinispanClusterConfigTest.java`: Remove `.async().enable().modificationQueueSize(1024)` from both builder configurations; add assertions verifying `config.async().enabled()` is `false`.

**Nuances / Risks / Corners**
- `write(...)` must continue handling `null` entry/key/value safely (returning `CompletableFuture.completedFuture(null)`).
- `delete(...)`, `containsKey(...)`, and `publish*` methods should not be regressed.
- WireMock and Mockito in tests must verify that `CompletionException` / `ExecutionException` contains the underlying failure cause when `.get()` or `.join()` is called on failed stages.

**Suspected Areas (Defect Root Causes)**
1. `FhirRestCacheStore.java:110-121`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
2. `OperationsRestCacheStore.java:114-126`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
3. `infinispan.xml:70,85,100,115,130,145,160,175,189,203,217,231,245,259,273,287,301`: `<write-behind .../>` decouples store execution from cache caller.

**Testing**

- **Scenario: FHIR Store Write Success** — REST client returns `true` (or WireMock 200) -> stage completes normally.
- **Scenario: FHIR Store Write Rejected / False** — REST client returns `false` (or WireMock 500) -> stage completes exceptionally with persistence failure.
- **Scenario: FHIR Store REST Exception** — REST client completes exceptionally (transport error / ConnectException) -> stage completes exceptionally with cause preserved.
- **Scenario: Operations Store Write Success** — REST client returns `true` (or WireMock 200) -> stage completes normally.
- **Scenario: Operations Store Write Rejected / False** — REST client returns `false` (or WireMock 500) -> stage completes exceptionally with persistence failure.
- **Scenario: Operations Store REST Exception** — REST client completes exceptionally (transport error) -> stage completes exceptionally with cause preserved.
- **Scenario: Cluster Configuration Synchronous Persistence** — `InfinispanClusterConfigTest` asserts `async().enabled() == false` on all defined store configurations.
- **Regression targets**:
    - `hestia/mneme-persistence` suite (`FhirRestCacheStoreTest`, `OperationsRestCacheStoreTest`, `OperationsRestClientLoggingTest`).
    - `hestia/mneme-cluster` suite (`InfinispanClusterConfigTest`).
    - ArchUnit architecture test suite (`*ArchitectureTest`).

**Assumptions & Open Questions**

- **Significant Assumption: PersistenceException for failure signaling**:
    - *Chosen Option*: Throw or wrap in `org.infinispan.persistence.spi.PersistenceException` (or propagate underlying `CompletionException`).
    - *Rationale*: Standard Infinispan SPI contract for failing asynchronous persistence operations; avoids inventing custom exception hierarchies.
    - *Alternative*: Creating a custom `MnemePersistenceException`.
    - *Impact*: Low; aligns with standard Infinispan error handling.
- **Significant Assumption: Removal of Write-Behind across all 17 caches in `infinispan.xml`**:
    - *Chosen Option*: Remove `<write-behind>` from all 14 FHIR and 3 Operations caches.
    - *Rationale*: All 17 caches currently back authoritative application state into Mnemosyne relational databases and have no cache-only status.
    - *Alternative*: Keeping write-behind on some caches.
    - *Impact*: Synchronous persistence guarantees caller visibility of persistence failures.

**Delivery Steps**

*** Step 1: Remediate Adapter Write Failure Propagation and Store Unit Tests**  
Goal: Ensure `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` propagate failed REST results and exceptions as failed stages, verified by focused unit tests.  
Scope: `hestia/mneme-persistence` (`FhirRestCacheStore.java`, `OperationsRestCacheStore.java`, `FhirRestCacheStoreTest.java`, `OperationsRestCacheStoreTest.java`).  
Acceptance Criteria:
- [ ] `FhirRestCacheStore.write(...)` returns a failed `CompletionStage` when `restClient.saveResourceJson(...)` returns `false` or completes exceptionally.
- [ ] `OperationsRestCacheStore.write(...)` returns a failed `CompletionStage` when `restClient.saveResourceJson(...)` returns `false` or completes exceptionally.
- [ ] Terminal `.exceptionally(error -> return null)` is removed from both `write(...)` implementations.
- [ ] `FhirRestCacheStoreTest` has tests verifying: successful write (200 / true), failed write (false / 500), and exceptional write completion.
- [ ] `OperationsRestCacheStoreTest` has tests verifying: successful write (200 / true), failed write (false / 500), and exceptional write completion.
- [ ] All existing store tests (load, delete, coordinate resolution, publish) continue to pass.
  Verification: `mvn test -pl hestia/mneme-persistence -am` → green

**Step 2: Configure Synchronous Store Persistence and Validate Full Suite & Architecture**  
Goal: Remove asynchronous write-behind configuration from Infinispan cache definitions and verify cluster configuration and architectural invariants.  
Scope: `hestia/mneme-cluster` (`infinispan.xml`, `InfinispanClusterConfigTest.java`), full test suite, and architecture verification.  
Acceptance Criteria:
- [ ] `<write-behind modification-queue-size="1024" />` removed from all 14 FHIR and 3 Operations replicated caches in `infinispan.xml`.
- [ ] `InfinispanClusterConfigTest` builds store configurations without `.async().enable()` and asserts `async().enabled() == false`.
- [ ] `InfinispanClusterConfigTest` passes and verifies cache initialization for all representative caches.
- [ ] Architecture tests in `paradeigma-test` pass without regression.
- [ ] Final summary report includes all 10 required reporting items from the parent task specification.
  Verification: `mvn test -pl hestia/mneme-cluster,paradeigma/paradeigma-test -am -Dtest="InfinispanClusterConfigTest,*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Implement the narrow Task 07 Step 02 remediation for the two Mneme REST-backed cache stores.

Required outcomes:
- In `FhirRestCacheStore.write(...)`, a `false` result from the REST client must become a persistence failure, not a warning-only success.
- In `OperationsRestCacheStore.write(...)`, a `false` result from the REST client must become a persistence failure, not a warning-only success.
- Exceptional REST client completions must remain exceptional; do not terminate the write path with `.exceptionally(... -> null)` or any equivalent swallowing behavior.
- Prefer the smallest coherent change using existing Infinispan/Harmonia exception vocabulary; do not build a new persistence framework.
- Expand the existing focused store tests so each store explicitly proves: success completes normally, false result fails, exceptional completion fails.
- Preserve the current null-entry/key/value no-op behavior unless compilation/tests require a minimal adjustment.
- Keep delete/load/publish/other behaviors outside the write path unchanged.

Known from previous steps:
- `FhirRestCacheStore.write(...)` is around lines 99–122 and currently logs false results in `thenAccept(...)`, then swallows exceptions via `.exceptionally(... return null)`.
- `OperationsRestCacheStore.write(...)` is around lines 103–127 with the same defect.
- `HapiFhirRestClient.saveResourceJson(...)` returns `true` on 2xx, `false` on non-2xx, and leaves transport failures exceptional.
- `OperationsRestClient.saveResourceJson(...)` behaves the same way.
- Existing store tests already have harnesses and both stores expose `setRestClient(...)` for Mockito injection.

Optimize execution speed:
- Edit only the store classes and their focused tests in this step.
- Start with narrow test commands for the two store test classes before any broader suite.
- Use short timeouts first (60–120s per command) and only widen if genuinely needed.
- Report exact commands run and outcomes in Discoveries.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Solution Summary: Remediate Adapter Write Failure Propagation and Store Unit Tests (Step 1)**

**1. Implementation Details**
- **`FhirRestCacheStore.java`**:
    - Remediated `write(segment, entry)` to map the Boolean REST client result using `.thenCompose(...)`.
    - When `restClient.saveResourceJson(...)` returns `false`, `write(...)` returns `CompletableFuture.failedFuture(new PersistenceException("Failed to persist <resourceType>/<id> to FHIR server"))`.
    - Removed terminal `.exceptionally(error -> return null)` handler so exceptional completions (transport/server failures) propagate naturally as failed stages.
    - Preserved null-entry/key/value safe no-op handling (`CompletableFuture.completedFuture(null)`).
- **`OperationsRestCacheStore.java`**:
    - Remediated `write(segment, entry)` to map the Boolean REST client result using `.thenCompose(...)`.
    - When `restClient.saveResourceJson(...)` returns `false`, `write(...)` returns `CompletableFuture.failedFuture(new PersistenceException("Failed to persist <objectType>/<id> to Operations JPA server"))`.
    - Removed terminal `.exceptionally(throwable -> return null)` handler so exceptional completions propagate naturally as failed stages.
    - Preserved null-entry/key/value safe no-op handling.

**2. Test Coverage & Expansion**
- **`FhirRestCacheStoreTest.java`**:
    - `testWrite`: Verifies successful write (HTTP 200 / true) completes normally.
    - `testWriteFailureOnServerError`: Verifies failed write (HTTP 500 / false) throws `ExecutionException` with cause `PersistenceException` and descriptive error message.
    - `testWriteFailureOnClientException`: Injects mock client returning a failed future to verify that transport/connection exceptions propagate without swallowing.
    - `testWriteNullHandling`: Verifies null entry, null key, and null value return completed future with null.
- **`OperationsRestCacheStoreTest.java`**:
    - `testWrite`: Verifies successful write (HTTP 200 / true) completes normally.
    - `testWriteFailureOnServerError`: Verifies failed write (HTTP 500 / false) throws `ExecutionException` with cause `PersistenceException`.
    - `testWriteFailureOnClientException`: Injects mock client returning a failed future to verify exception propagation.
    - `testWriteNullHandling`: Verifies null entry, null key, and null value return completed future with null.

**3. Verification**  
Executed `mvn test -pl hestia/mneme-persistence -am` (took ~3.5s, 24 tests passed across `mneme-persistence`, 0 failures, 0 errors, 0 skipped).

**Discoveries for future steps**
- **Execution commands & speed**: `mvn test -pl hestia/mneme-persistence -am` executes in ~3.5s; the `-am` flag ensures prerequisite modules (`calliope`, `themis-api`) are present on the test classpath for `PhiLoggerFactory`.
- **Infinispan SPI error handling**: `org.infinispan.persistence.spi.PersistenceException` works cleanly with `NonBlockingStore.write` via `CompletableFuture.failedFuture(...)`.
- **Injection for tests**: Both `FhirRestCacheStore` and `OperationsRestCacheStore` have `setRestClient(...)` methods allowing direct mock injection to test exceptional client completions alongside WireMock HTTP status stubbing.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Review only the step-1 scope: adapter write failure propagation in `FhirRestCacheStore` and `OperationsRestCacheStore`, plus their focused tests.

Focus on:
- whether a `false` REST client result now fails the write stage instead of being logged-and-swallowed;
- whether exceptional REST completions remain exceptional, with no terminal swallowing handler on the write path;
- whether null entry/key/value no-op behavior was preserved;
- whether test coverage truthfully proves success, false-result failure, and exceptional-result failure for both stores;
- whether any unintended regression was introduced outside the write path.

Also verify the claimed Maven test execution/results if observable.



Implementation Approved

**Verification**
- **`FhirRestCacheStore.write(...)`** (lines 111–121): Correctly maps `Boolean` result via `.thenCompose(...)`. `false` now returns `CompletableFuture.failedFuture(new PersistenceException(...))`; success returns completed future. The terminal `.exceptionally(err -> null)` swallowing handler is removed, so exceptional REST completions propagate naturally. Null entry/key/value no-op preserved (lines 101–103).
- **`OperationsRestCacheStore.write(...)`** (lines 115–125): Identical, correct remediation with `PersistenceException` on `false` and no swallowing handler. Null handling preserved (lines 105–107).
- **No regressions outside write path**: `load`, `delete`, `containsKey`, `clear`, `size`, `publishEntries`, `publishKeys` unchanged. The existing `.exceptionally(...)` in `OperationsRestCacheStore.publishEntries` (line 198) is correctly left untouched (out of scope — non-write path).
- **Test coverage (FHIR)**: `testWrite` (200/true success), `testWriteFailureOnServerError` (500 → `ExecutionException` cause `PersistenceException` with message "Failed to persist Person/101"), `testWriteFailureOnClientException` (injected failed future → cause preserved), `testWriteNullHandling` (null entry/key/value). Existing load/delete/coordinate tests retained.
- **Test coverage (Operations)**: Same four write scenarios plus retained load/delete/contains/publish/coordinate tests.
- **Tests are meaningful, not over-mocked**: false-result path exercised via real WireMock 500 HTTP responses through the real REST client; exceptional path via Mockito-injected failed future. Both assert on the actual thrown cause type and message.
- **Test execution**: `mvn test -pl hestia/mneme-persistence -am` completed with exit code 0 (green). Logs confirm the 500-path ("Write-behind failed to persist Person/101" / "Failed to persist tasksequence/seq-001") and 200-path ("Write-behind successfully persisted Person/101") execute as designed.

**Issues**  
None.

**Notes**
- Step 2 scope (infinispan.xml write-behind removal, InfinispanClusterConfigTest) is correctly untouched here — those are future-step items and not part of this pass.
- Executor's claimed ~3.5s runtime is plausible; observed run completed quickly.

**Discoveries for future steps**
- `mvn test -pl hestia/mneme-persistence -am` runs green in a few seconds; `-am` needed so prerequisite modules (calliope, themis-api) are on the classpath for `PhiLoggerFactory`.
- HTTP non-2xx (WireMock 500) surfaces as `false` from both REST clients (not an exception), so the `false` → `PersistenceException` branch is what covers server-error scenarios; genuine transport failures need mock-injected failed futures.



**Requirements**

**Goal & Outcome**  
Enforce the authoritative state boundary established in ADR-018 by eliminating false durable-success semantics in Mneme (`FhirRestCacheStore` and `OperationsRestCacheStore`). A failed Mnemosyne write (explicit rejection, HTTP error, or transport crash) must fail the persistence stage and propagate synchronously to originating cache operations rather than being swallowed as a successful completion.

**Scope**
- **In Scope**:
    - Remediate `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` to return failed completion stages on false REST results or exceptional completions.
    - Remove asynchronous `<write-behind modification-queue-size="1024" />` configurations from authoritative-write caches in `infinispan.xml` (all 14 FHIR caches and 3 Operations caches).
    - Update `InfinispanClusterConfigTest` to configure synchronous stores and verify non-async store attributes.
    - Expand `FhirRestCacheStoreTest` and `OperationsRestCacheStoreTest` to verify success, false-result failure, and exceptional-result failure contracts.
    - Run focused tests, module test suites, and ArchUnit architecture tests.
- **Out of Scope**:
    - Redesigning the Clinical write path, direct BEFE -> Mnemosyne writes, or `FhirCacheService` (Task 08).
    - Cache-aside read behaviors (Task 09) and Clinical search redesign (Task 10).
    - Petasos messaging transitions or Artemis producer/consumer semantics.
    - Modifying local in-memory fallback paths (`FhirCacheService`, `DefaultTaskService`, `DefaultProvenanceService`, `TaskCacheService`, `PragmaCacheService`, `PraxisService`, `MessageQueueService`, `TaskSequenceCacheService`, `ModuleStatusService`, `TaskEventProducerService`, `IncomingAdtMessageProcessor`).
    - Mnemosyne JPA/repository architecture or Kleio durability redesign.

**Done When**
- `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` complete exceptionally when REST calls return `false` or throw exceptions.
- Neither adapter swallows errors via terminal `.exceptionally(err -> return null)` on write operations.
- `infinispan.xml` and `InfinispanClusterConfigTest` have async write-behind disabled for all 17 authoritative persistence caches.
- Focused store tests and configuration tests pass, proving failure propagation and synchronous configuration.
- ArchUnit architecture tests pass (`*ArchitectureTest`).

**Technical Design**

**Decisions**
- **Decision: Propagate failed stages using standard `PersistenceException` (or failed `CompletionStage`) / not swallowing via `.exceptionally(...)`**
    - *Rationale*: Infinispan `NonBlockingStore.write(...)` requires a failed `CompletionStage` to signal persistence failure to the cache engine. Using standard `org.infinispan.persistence.spi.PersistenceException` or failing the future preserves standard Infinispan error propagation without introducing unnecessary custom exception hierarchies.
- **Decision: Remove async write-behind from `infinispan.xml` for all 17 REST-backed replicated caches / not retaining async write-behind**
    - *Rationale*: Infinispan's `AsyncNonBlockingStore` (write-behind) buffers writes in an in-memory queue and acknowledges the caller immediately. If write-behind is enabled, cache mutators cannot observe downstream store failures regardless of adapter logic. Switching to synchronous cache-store persistence aligns with ADR-018's current-architecture correctness requirements.

**Approach & Touches**
- **Adapter remediation**:
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`: In `write(...)`, map `Boolean` result from `restClient.saveResourceJson(...)` via `thenCompose(...)` or `thenApply(...)` such that `false` returns `CompletableFuture.failedStage(new PersistenceException(...))`; remove `.exceptionally(...)` that converts errors to `null`.
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/OperationsRestCacheStore.java`: Apply identical failure propagation in `write(...)`.
- **Configuration & Cluster remediation**:
    - `hestia/mneme-cluster/src/main/resources/infinispan.xml`: Remove `<write-behind modification-queue-size="1024" />` from all 14 FHIR replicated caches (`person-cache`, `relatedperson-cache`, `practitioner-cache`, `practitionerrole-cache`, `organization-cache`, `location-cache`, `healthcareservice-cache`, `group-cache`, `provenance-cache`, `auditevent-cache`, `consent-cache`, `task-cache`, `communication-cache`, `documentreference-cache`) and all 3 Operations caches (`tasksequence-cache`, `messagequeue-cache`, `modulestatus-cache`).
    - `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/InfinispanClusterConfigTest.java`: Remove `.async().enable().modificationQueueSize(1024)` from both builder configurations; add assertions verifying `config.async().enabled()` is `false`.

**Nuances / Risks / Corners**
- `write(...)` must continue handling `null` entry/key/value safely (returning `CompletableFuture.completedFuture(null)`).
- `delete(...)`, `containsKey(...)`, and `publish*` methods should not be regressed.
- WireMock and Mockito in tests must verify that `CompletionException` / `ExecutionException` contains the underlying failure cause when `.get()` or `.join()` is called on failed stages.

**Suspected Areas (Defect Root Causes)**
1. `FhirRestCacheStore.java:110-121`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
2. `OperationsRestCacheStore.java:114-126`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
3. `infinispan.xml:70,85,100,115,130,145,160,175,189,203,217,231,245,259,273,287,301`: `<write-behind .../>` decouples store execution from cache caller.

**Testing**

- **Scenario: FHIR Store Write Success** — REST client returns `true` (or WireMock 200) -> stage completes normally.
- **Scenario: FHIR Store Write Rejected / False** — REST client returns `false` (or WireMock 500) -> stage completes exceptionally with persistence failure.
- **Scenario: FHIR Store REST Exception** — REST client completes exceptionally (transport error / ConnectException) -> stage completes exceptionally with cause preserved.
- **Scenario: Operations Store Write Success** — REST client returns `true` (or WireMock 200) -> stage completes normally.
- **Scenario: Operations Store Write Rejected / False** — REST client returns `false` (or WireMock 500) -> stage completes exceptionally with persistence failure.
- **Scenario: Operations Store REST Exception** — REST client completes exceptionally (transport error) -> stage completes exceptionally with cause preserved.
- **Scenario: Cluster Configuration Synchronous Persistence** — `InfinispanClusterConfigTest` asserts `async().enabled() == false` on all defined store configurations.
- **Regression targets**:
    - `hestia/mneme-persistence` suite (`FhirRestCacheStoreTest`, `OperationsRestCacheStoreTest`, `OperationsRestClientLoggingTest`).
    - `hestia/mneme-cluster` suite (`InfinispanClusterConfigTest`).
    - ArchUnit architecture test suite (`*ArchitectureTest`).

**Assumptions & Open Questions**

- **Significant Assumption: PersistenceException for failure signaling**:
    - *Chosen Option*: Throw or wrap in `org.infinispan.persistence.spi.PersistenceException` (or propagate underlying `CompletionException`).
    - *Rationale*: Standard Infinispan SPI contract for failing asynchronous persistence operations; avoids inventing custom exception hierarchies.
    - *Alternative*: Creating a custom `MnemePersistenceException`.
    - *Impact*: Low; aligns with standard Infinispan error handling.
- **Significant Assumption: Removal of Write-Behind across all 17 caches in `infinispan.xml`**:
    - *Chosen Option*: Remove `<write-behind>` from all 14 FHIR and 3 Operations caches.
    - *Rationale*: All 17 caches currently back authoritative application state into Mnemosyne relational databases and have no cache-only status.
    - *Alternative*: Keeping write-behind on some caches.
    - *Impact*: Synchronous persistence guarantees caller visibility of persistence failures.

**Delivery Steps**

**✓ Step 1: Remediate Adapter Write Failure Propagation and Store Unit Tests**  
Goal: Ensure `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` propagate failed REST results and exceptions as failed stages, verified by focused unit tests.  
Scope: `hestia/mneme-persistence` (`FhirRestCacheStore.java`, `OperationsRestCacheStore.java`, `FhirRestCacheStoreTest.java`, `OperationsRestCacheStoreTest.java`).  
Acceptance Criteria:
- [ ] `FhirRestCacheStore.write(...)` returns a failed `CompletionStage` when `restClient.saveResourceJson(...)` returns `false` or completes exceptionally.
- [ ] `OperationsRestCacheStore.write(...)` returns a failed `CompletionStage` when `restClient.saveResourceJson(...)` returns `false` or completes exceptionally.
- [ ] Terminal `.exceptionally(error -> return null)` is removed from both `write(...)` implementations.
- [ ] `FhirRestCacheStoreTest` has tests verifying: successful write (200 / true), failed write (false / 500), and exceptional write completion.
- [ ] `OperationsRestCacheStoreTest` has tests verifying: successful write (200 / true), failed write (false / 500), and exceptional write completion.
- [ ] All existing store tests (load, delete, coordinate resolution, publish) continue to pass.
  Verification: `mvn test -pl hestia/mneme-persistence -am` → green

**Step 2: Configure Synchronous Store Persistence and Validate Full Suite & Architecture**  
Goal: Remove asynchronous write-behind configuration from Infinispan cache definitions and verify cluster configuration and architectural invariants.  
Scope: `hestia/mneme-cluster` (`infinispan.xml`, `InfinispanClusterConfigTest.java`), full test suite, and architecture verification.  
Acceptance Criteria:
- [ ] `<write-behind modification-queue-size="1024" />` removed from all 14 FHIR and 3 Operations replicated caches in `infinispan.xml`.
- [ ] `InfinispanClusterConfigTest` builds store configurations without `.async().enable()` and asserts `async().enabled() == false`.
- [ ] `InfinispanClusterConfigTest` passes and verifies cache initialization for all representative caches.
- [ ] Architecture tests in `paradeigma-test` pass without regression.
- [ ] Final summary report includes all 10 required reporting items from the parent task specification.
  Verification: `mvn test -pl hestia/mneme-cluster,paradeigma/paradeigma-test -am -Dtest="InfinispanClusterConfigTest,*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Requirements**

**Goal & Outcome**  
Enforce the authoritative state boundary established in ADR-018 by eliminating false durable-success semantics in Mneme (`FhirRestCacheStore` and `OperationsRestCacheStore`). A failed Mnemosyne write (explicit rejection, HTTP error, or transport crash) must fail the persistence stage and propagate synchronously to originating cache operations rather than being swallowed as a successful completion.

**Scope**
- **In Scope**:
    - Remediate `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` to return failed completion stages on false REST results or exceptional completions.
    - Remove asynchronous `<write-behind modification-queue-size="1024" />` configurations from authoritative-write caches in `infinispan.xml` (all 14 FHIR caches and 3 Operations caches).
    - Update `InfinispanClusterConfigTest` to configure synchronous stores and verify non-async store attributes.
    - Expand `FhirRestCacheStoreTest` and `OperationsRestCacheStoreTest` to verify success, false-result failure, and exceptional-result failure contracts.
    - Run focused tests, module test suites, and ArchUnit architecture tests.
- **Out of Scope**:
    - Redesigning the Clinical write path, direct BEFE -> Mnemosyne writes, or `FhirCacheService` (Task 08).
    - Cache-aside read behaviors (Task 09) and Clinical search redesign (Task 10).
    - Petasos messaging transitions or Artemis producer/consumer semantics.
    - Modifying local in-memory fallback paths (`FhirCacheService`, `DefaultTaskService`, `DefaultProvenanceService`, `TaskCacheService`, `PragmaCacheService`, `PraxisService`, `MessageQueueService`, `TaskSequenceCacheService`, `ModuleStatusService`, `TaskEventProducerService`, `IncomingAdtMessageProcessor`).
    - Mnemosyne JPA/repository architecture or Kleio durability redesign.

**Done When**
- `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` complete exceptionally when REST calls return `false` or throw exceptions.
- Neither adapter swallows errors via terminal `.exceptionally(err -> return null)` on write operations.
- `infinispan.xml` and `InfinispanClusterConfigTest` have async write-behind disabled for all 17 authoritative persistence caches.
- Focused store tests and configuration tests pass, proving failure propagation and synchronous configuration.
- ArchUnit architecture tests pass (`*ArchitectureTest`).

**Technical Design**

**Decisions**
- **Decision: Propagate failed stages using standard `PersistenceException` (or failed `CompletionStage`) / not swallowing via `.exceptionally(...)`**
    - *Rationale*: Infinispan `NonBlockingStore.write(...)` requires a failed `CompletionStage` to signal persistence failure to the cache engine. Using standard `org.infinispan.persistence.spi.PersistenceException` or failing the future preserves standard Infinispan error propagation without introducing unnecessary custom exception hierarchies.
- **Decision: Remove async write-behind from `infinispan.xml` for all 17 REST-backed replicated caches / not retaining async write-behind**
    - *Rationale*: Infinispan's `AsyncNonBlockingStore` (write-behind) buffers writes in an in-memory queue and acknowledges the caller immediately. If write-behind is enabled, cache mutators cannot observe downstream store failures regardless of adapter logic. Switching to synchronous cache-store persistence aligns with ADR-018's current-architecture correctness requirements.

**Approach & Touches**
- **Adapter remediation**:
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`: In `write(...)`, map `Boolean` result from `restClient.saveResourceJson(...)` via `thenCompose(...)` or `thenApply(...)` such that `false` returns `CompletableFuture.failedStage(new PersistenceException(...))`; remove `.exceptionally(...)` that converts errors to `null`.
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/OperationsRestCacheStore.java`: Apply identical failure propagation in `write(...)`.
- **Configuration & Cluster remediation**:
    - `hestia/mneme-cluster/src/main/resources/infinispan.xml`: Remove `<write-behind modification-queue-size="1024" />` from all 14 FHIR replicated caches (`person-cache`, `relatedperson-cache`, `practitioner-cache`, `practitionerrole-cache`, `organization-cache`, `location-cache`, `healthcareservice-cache`, `group-cache`, `provenance-cache`, `auditevent-cache`, `consent-cache`, `task-cache`, `communication-cache`, `documentreference-cache`) and all 3 Operations caches (`tasksequence-cache`, `messagequeue-cache`, `modulestatus-cache`).
    - `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/InfinispanClusterConfigTest.java`: Remove `.async().enable().modificationQueueSize(1024)` from both builder configurations; add assertions verifying `config.async().enabled()` is `false`.

**Nuances / Risks / Corners**
- `write(...)` must continue handling `null` entry/key/value safely (returning `CompletableFuture.completedFuture(null)`).
- `delete(...)`, `containsKey(...)`, and `publish*` methods should not be regressed.
- WireMock and Mockito in tests must verify that `CompletionException` / `ExecutionException` contains the underlying failure cause when `.get()` or `.join()` is called on failed stages.

**Suspected Areas (Defect Root Causes)**
1. `FhirRestCacheStore.java:110-121`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
2. `OperationsRestCacheStore.java:114-126`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
3. `infinispan.xml:70,85,100,115,130,145,160,175,189,203,217,231,245,259,273,287,301`: `<write-behind .../>` decouples store execution from cache caller.

**Testing**

- **Scenario: FHIR Store Write Success** — REST client returns `true` (or WireMock 200) -> stage completes normally.
- **Scenario: FHIR Store Write Rejected / False** — REST client returns `false` (or WireMock 500) -> stage completes exceptionally with persistence failure.
- **Scenario: FHIR Store REST Exception** — REST client completes exceptionally (transport error / ConnectException) -> stage completes exceptionally with cause preserved.
- **Scenario: Operations Store Write Success** — REST client returns `true` (or WireMock 200) -> stage completes normally.
- **Scenario: Operations Store Write Rejected / False** — REST client returns `false` (or WireMock 500) -> stage completes exceptionally with persistence failure.
- **Scenario: Operations Store REST Exception** — REST client completes exceptionally (transport error) -> stage completes exceptionally with cause preserved.
- **Scenario: Cluster Configuration Synchronous Persistence** — `InfinispanClusterConfigTest` asserts `async().enabled() == false` on all defined store configurations.
- **Regression targets**:
    - `hestia/mneme-persistence` suite (`FhirRestCacheStoreTest`, `OperationsRestCacheStoreTest`, `OperationsRestClientLoggingTest`).
    - `hestia/mneme-cluster` suite (`InfinispanClusterConfigTest`).
    - ArchUnit architecture test suite (`*ArchitectureTest`).

**Assumptions & Open Questions**

- **Significant Assumption: PersistenceException for failure signaling**:
    - *Chosen Option*: Throw or wrap in `org.infinispan.persistence.spi.PersistenceException` (or propagate underlying `CompletionException`).
    - *Rationale*: Standard Infinispan SPI contract for failing asynchronous persistence operations; avoids inventing custom exception hierarchies.
    - *Alternative*: Creating a custom `MnemePersistenceException`.
    - *Impact*: Low; aligns with standard Infinispan error handling.
- **Significant Assumption: Removal of Write-Behind across all 17 caches in `infinispan.xml`**:
    - *Chosen Option*: Remove `<write-behind>` from all 14 FHIR and 3 Operations caches.
    - *Rationale*: All 17 caches currently back authoritative application state into Mnemosyne relational databases and have no cache-only status.
    - *Alternative*: Keeping write-behind on some caches.
    - *Impact*: Synchronous persistence guarantees caller visibility of persistence failures.

**Delivery Steps**

**✓ Step 1: Remediate Adapter Write Failure Propagation and Store Unit Tests**  
Goal: Ensure `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` propagate failed REST results and exceptions as failed stages, verified by focused unit tests.  
Scope: `hestia/mneme-persistence` (`FhirRestCacheStore.java`, `OperationsRestCacheStore.java`, `FhirRestCacheStoreTest.java`, `OperationsRestCacheStoreTest.java`).  
Acceptance Criteria:
- [ ] `FhirRestCacheStore.write(...)` returns a failed `CompletionStage` when `restClient.saveResourceJson(...)` returns `false` or completes exceptionally.
- [ ] `OperationsRestCacheStore.write(...)` returns a failed `CompletionStage` when `restClient.saveResourceJson(...)` returns `false` or completes exceptionally.
- [ ] Terminal `.exceptionally(error -> return null)` is removed from both `write(...)` implementations.
- [ ] `FhirRestCacheStoreTest` has tests verifying: successful write (200 / true), failed write (false / 500), and exceptional write completion.
- [ ] `OperationsRestCacheStoreTest` has tests verifying: successful write (200 / true), failed write (false / 500), and exceptional write completion.
- [ ] All existing store tests (load, delete, coordinate resolution, publish) continue to pass.
  Verification: `mvn test -pl hestia/mneme-persistence -am` → green

*** Step 2: Configure Synchronous Store Persistence and Validate Full Suite & Architecture**  
Goal: Remove asynchronous write-behind configuration from Infinispan cache definitions and verify cluster configuration and architectural invariants.  
Scope: `hestia/mneme-cluster` (`infinispan.xml`, `InfinispanClusterConfigTest.java`), full test suite, and architecture verification.  
Acceptance Criteria:
- [ ] `<write-behind modification-queue-size="1024" />` removed from all 14 FHIR and 3 Operations replicated caches in `infinispan.xml`.
- [ ] `InfinispanClusterConfigTest` builds store configurations without `.async().enable()` and asserts `async().enabled() == false`.
- [ ] `InfinispanClusterConfigTest` passes and verifies cache initialization for all representative caches.
- [ ] Architecture tests in `paradeigma-test` pass without regression.
- [ ] Final summary report includes all 10 required reporting items from the parent task specification.
  Verification: `mvn test -pl hestia/mneme-cluster,paradeigma/paradeigma-test -am -Dtest="InfinispanClusterConfigTest,*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green

IMPLEMENTATION

**Task description**  
Fully complete step 2.

**Feedback**  
Finish the remaining Task 07 Step 02 work: minimum configuration correction for failure propagation plus verification.

Required outcomes:
- Determine and implement the minimum Infinispan config change needed so originating cache operations can observe persistence failures.
- Based on prior exploration and plan review, async write-behind is the blocker; remove `<write-behind modification-queue-size="1024" />` from the affected authoritative-write REST-backed caches in `hestia/mneme-cluster/src/main/resources/infinispan.xml`.
- Keep all other cache/store attributes intact; do not redesign the write path.
- Update `InfinispanClusterConfigTest` so representative configs are synchronous rather than `.async().enable()`, and add assertions proving the affected store configs are not asynchronous while keeping the rest of the test intent intact.
- Run the relevant cluster/configuration tests and the architecture guardrails after the focused changes.

Known from previous steps:
- Step 1 is complete and reviewed: both store adapters now fail on false/exceptional persistence results and their tests are green.
- Exploration found 17 REST-backed authoritative-write caches in production XML (14 FHIR + 3 Operations), each currently configured with `<write-behind modification-queue-size="1024" />`.
- Review confirmed no cache-only store exists in this XML, so removing write-behind from all 17 is justified and still narrow for this issue.
- `InfinispanClusterConfigTest` currently builds representative configs with `.async().enable().modificationQueueSize(1024)` and only asserts cache instantiation.

Optimize execution speed:
- Edit only the cluster XML and `InfinispanClusterConfigTest` in this step.
- Start with the narrowest relevant test command(s) for `InfinispanClusterConfigTest`, then run the architecture suite.
- Use short command timeouts first and widen only if required.
- Report exact commands run and outcomes in Discoveries.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Solution Summary: Configure Synchronous Store Persistence and Validate Full Suite & Architecture (Step 2)**

**1. Implementation Details**
- **`hestia/mneme-cluster/src/main/resources/infinispan.xml`**:
    - Removed `<write-behind modification-queue-size="1024" />` from all 14 FHIR replicated caches (`person-cache`, `relatedperson-cache`, `practitioner-cache`, `practitionerrole-cache`, `organization-cache`, `location-cache`, `healthcareservice-cache`, `group-cache`, `provenance-cache`, `auditevent-cache`, `consent-cache`, `task-cache`, `communication-cache`, `documentreference-cache`).
    - Removed `<write-behind modification-queue-size="1024" />` from all 3 Operations replicated caches (`tasksequence-cache`, `messagequeue-cache`, `modulestatus-cache`).
    - Updated configuration comment to reflect synchronous persistence.
    - Retained all existing cache replication modes, encodings, store classes, and properties intact.

- **`hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/InfinispanClusterConfigTest.java`**:
    - Updated test builders (`FhirStoreConfigurationBuilder` and `OperationsStoreConfigurationBuilder`) to configure synchronous persistence by omitting `.async().enable()`.
    - Added assertions in `testCacheInitialization()` verifying that `async().enabled()` is `false` for all representative caches (`person-cache`, `practitioner-cache`, `organization-cache`, `tasksequence-cache`, `messagequeue-cache`, `modulestatus-cache`).
    - Added `testProductionXmlConfiguration()` using DOM parsing to verify `infinispan.xml` contains zero `<write-behind>` tags and maintains 17 replicated caches.

**2. Verification & Test Execution**
- **`InfinispanClusterConfigTest`**:
    - Command: `mvn test -pl hestia/mneme-cluster -am -Dtest="InfinispanClusterConfigTest" -Dsurefire.failIfNoSpecifiedTests=false`
    - Result: BUILD SUCCESS (2 tests run, 0 failures, 0 errors, 0 skipped).
- **`paradeigma-test` Architecture Suite**:
    - Command: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`
    - Result: BUILD SUCCESS (57 architecture tests run across 7 suites, 0 failures, 0 errors, 0 skipped).
- **Combined `mneme-persistence` & `mneme-cluster` Suite**:
    - Command: `mvn test -pl hestia/mneme-persistence,hestia/mneme-cluster -am`
    - Result: BUILD SUCCESS (26 tests run, 0 failures, 0 errors, 0 skipped).

**Discoveries for future steps**
- **Execution commands & speed**:
    - Running single targeted tests in multi-module builds requires `-Dsurefire.failIfNoSpecifiedTests=false` when using `-am` to prevent upstream modules lacking the specified test from failing the build.
    - `InfinispanClusterConfigTest` takes ~5.1s to boot embedded cluster transport and cache manager.
    - ArchUnit tests in `paradeigma/paradeigma-test` execute in ~10s.
- **Infinispan configuration nuances**:
    - `infinispan.xml` contains a `<server>` element parsed specifically by the server runtime; embedded `ParserRegistry` in standalone core throws `CacheConfigurationException` on `<server>`, making standard XML DOM/SAX parsing optimal for unit test validation of server configuration files.
- **Synchronous store defaults**:
    - Infinispan store builders default to `async().enabled() == false` when `.async().enable()` is not invoked, enabling synchronous write propagation to the underlying cache store.



IMPLEMENTATION REVIEW

**Task description**  
Fully review step 2.

**Feedback**  
Review only the step-2 scope: the minimum Infinispan configuration change for originating-operation failure visibility, the `InfinispanClusterConfigTest` updates, and the claimed verification runs.

Focus on:
- whether removing `<write-behind ...>` from the affected REST-backed caches was done cleanly and without unrelated config churn;
- whether `InfinispanClusterConfigTest` now truthfully proves synchronous persistence semantics and checks the production XML proportionately;
- whether the claimed focused cluster and architecture test executions/results are observable;
- any residual correctness concerns specific to Task 07 Step 02, not Task 08 redesign.



Implementation Approved

**Verification**
- `infinispan.xml`: all 17 `<write-behind modification-queue-size="1024" />` entries removed (grep confirms 0 remaining in the module); all 17 `<replicated-cache>` definitions intact with mode/encoding/store class/properties unchanged. Only additional change is the comment "Write-Behind Persistence" → "Synchronous Persistence" — clean, no unrelated churn.
- `InfinispanClusterConfigTest`: builders now omit `.async().enable()`; `testCacheInitialization()` asserts `stores().get(0).async().enabled()` is `false` on all 6 representative caches (3 FHIR + 3 Operations) — this truthfully proves synchronous store semantics. Added `testProductionXmlConfiguration()` uses DOM parsing to assert 0 `<write-behind>` nodes and exactly 17 `<replicated-cache>` — proportionate coverage of the production XML.
- Ran `mvn test -pl hestia/mneme-cluster,paradeigma/paradeigma-test -am -Dtest="InfinispanClusterConfigTest,*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → BUILD SUCCESS: InfinispanClusterConfigTest 2/2 green, 57 architecture tests green across 7 suites (Iris, PetasosApi, PackageLayering, ParadeigmaIsolation, ProviderRegistry, AgoraIsolation, SecurityEnforcement). Executor's claimed runs are observable/reproducible.
- Step 1 non-regression: `mvn test -pl hestia/mneme-persistence -am` → BUILD SUCCESS (24 tests, FhirRestCacheStoreTest 7, OperationsRestCacheStoreTest 9, OperationsRestClientLoggingTest 8).

**Issues**  
None blocking.

**Notes**
- Running `mvn test -pl hestia/mneme-persistence` WITHOUT `-am` fails with `NoClassDefFoundError: net/fhirfactory/harmonia/logging/PhiLoggerFactory` (7 errors in FhirRestCacheStoreTest). This is a build-ordering artifact — the upstream logging module is not in the local repo — NOT a regression from Step 2. With `-am` it passes cleanly. Always use `-am` for these modules.
- Minor (non-blocking, out of scope): `hestia/mneme-cluster/pom.xml` `<description>` still references "write-behind persistence"; cosmetic only.
- `getElementsByTagName("write-behind")` with a namespace-aware factory works here because write-behind is unprefixed and the assertion expects 0 nodes; harmless.

**Discoveries for future steps**
- `InfinispanClusterConfigTest` boots embedded cluster transport (~5.2s); combined cluster+architecture run finishes well under 10 min.
- `infinispan.xml` contains a `<server>` element that embedded Infinispan `ParserRegistry` cannot parse; DOM/SAX parsing is the correct approach for XML validation in unit tests (executor used this).
- For single-test runs in these modules, `-am` is mandatory (upstream `harmonia-logging`/calliope must be built), plus `-Dsurefire.failIfNoSpecifiedTests=false`.



**Requirements**

**Goal & Outcome**  
Enforce the authoritative state boundary established in ADR-018 by eliminating false durable-success semantics in Mneme (`FhirRestCacheStore` and `OperationsRestCacheStore`). A failed Mnemosyne write (explicit rejection, HTTP error, or transport crash) must fail the persistence stage and propagate synchronously to originating cache operations rather than being swallowed as a successful completion.

**Scope**
- **In Scope**:
    - Remediate `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` to return failed completion stages on false REST results or exceptional completions.
    - Remove asynchronous `<write-behind modification-queue-size="1024" />` configurations from authoritative-write caches in `infinispan.xml` (all 14 FHIR caches and 3 Operations caches).
    - Update `InfinispanClusterConfigTest` to configure synchronous stores and verify non-async store attributes.
    - Expand `FhirRestCacheStoreTest` and `OperationsRestCacheStoreTest` to verify success, false-result failure, and exceptional-result failure contracts.
    - Run focused tests, module test suites, and ArchUnit architecture tests.
- **Out of Scope**:
    - Redesigning the Clinical write path, direct BEFE -> Mnemosyne writes, or `FhirCacheService` (Task 08).
    - Cache-aside read behaviors (Task 09) and Clinical search redesign (Task 10).
    - Petasos messaging transitions or Artemis producer/consumer semantics.
    - Modifying local in-memory fallback paths (`FhirCacheService`, `DefaultTaskService`, `DefaultProvenanceService`, `TaskCacheService`, `PragmaCacheService`, `PraxisService`, `MessageQueueService`, `TaskSequenceCacheService`, `ModuleStatusService`, `TaskEventProducerService`, `IncomingAdtMessageProcessor`).
    - Mnemosyne JPA/repository architecture or Kleio durability redesign.

**Done When**
- `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` complete exceptionally when REST calls return `false` or throw exceptions.
- Neither adapter swallows errors via terminal `.exceptionally(err -> return null)` on write operations.
- `infinispan.xml` and `InfinispanClusterConfigTest` have async write-behind disabled for all 17 authoritative persistence caches.
- Focused store tests and configuration tests pass, proving failure propagation and synchronous configuration.
- ArchUnit architecture tests pass (`*ArchitectureTest`).

**Technical Design**

**Decisions**
- **Decision: Propagate failed stages using standard `PersistenceException` (or failed `CompletionStage`) / not swallowing via `.exceptionally(...)`**
    - *Rationale*: Infinispan `NonBlockingStore.write(...)` requires a failed `CompletionStage` to signal persistence failure to the cache engine. Using standard `org.infinispan.persistence.spi.PersistenceException` or failing the future preserves standard Infinispan error propagation without introducing unnecessary custom exception hierarchies.
- **Decision: Remove async write-behind from `infinispan.xml` for all 17 REST-backed replicated caches / not retaining async write-behind**
    - *Rationale*: Infinispan's `AsyncNonBlockingStore` (write-behind) buffers writes in an in-memory queue and acknowledges the caller immediately. If write-behind is enabled, cache mutators cannot observe downstream store failures regardless of adapter logic. Switching to synchronous cache-store persistence aligns with ADR-018's current-architecture correctness requirements.

**Approach & Touches**
- **Adapter remediation**:
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`: In `write(...)`, map `Boolean` result from `restClient.saveResourceJson(...)` via `thenCompose(...)` or `thenApply(...)` such that `false` returns `CompletableFuture.failedStage(new PersistenceException(...))`; remove `.exceptionally(...)` that converts errors to `null`.
    - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/OperationsRestCacheStore.java`: Apply identical failure propagation in `write(...)`.
- **Configuration & Cluster remediation**:
    - `hestia/mneme-cluster/src/main/resources/infinispan.xml`: Remove `<write-behind modification-queue-size="1024" />` from all 14 FHIR replicated caches (`person-cache`, `relatedperson-cache`, `practitioner-cache`, `practitionerrole-cache`, `organization-cache`, `location-cache`, `healthcareservice-cache`, `group-cache`, `provenance-cache`, `auditevent-cache`, `consent-cache`, `task-cache`, `communication-cache`, `documentreference-cache`) and all 3 Operations caches (`tasksequence-cache`, `messagequeue-cache`, `modulestatus-cache`).
    - `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/InfinispanClusterConfigTest.java`: Remove `.async().enable().modificationQueueSize(1024)` from both builder configurations; add assertions verifying `config.async().enabled()` is `false`.

**Nuances / Risks / Corners**
- `write(...)` must continue handling `null` entry/key/value safely (returning `CompletableFuture.completedFuture(null)`).
- `delete(...)`, `containsKey(...)`, and `publish*` methods should not be regressed.
- WireMock and Mockito in tests must verify that `CompletionException` / `ExecutionException` contains the underlying failure cause when `.get()` or `.join()` is called on failed stages.

**Suspected Areas (Defect Root Causes)**
1. `FhirRestCacheStore.java:110-121`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
2. `OperationsRestCacheStore.java:114-126`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
3. `infinispan.xml:70,85,100,115,130,145,160,175,189,203,217,231,245,259,273,287,301`: `<write-behind .../>` decouples store execution from cache caller.

**Testing**

- **Scenario: FHIR Store Write Success** — REST client returns `true` (or WireMock 200) -> stage completes normally.
- **Scenario: FHIR Store Write Rejected / False** — REST client returns `false` (or WireMock 500) -> stage completes exceptionally with persistence failure.
- **Scenario: FHIR Store REST Exception** — REST client completes exceptionally (transport error / ConnectException) -> stage completes exceptionally with cause preserved.
- **Scenario: Operations Store Write Success** — REST client returns `true` (or WireMock 200) -> stage completes normally.
- **Scenario: Operations Store Write Rejected / False** — REST client returns `false` (or WireMock 500) -> stage completes exceptionally with persistence failure.
- **Scenario: Operations Store REST Exception** — REST client completes exceptionally (transport error) -> stage completes exceptionally with cause preserved.
- **Scenario: Cluster Configuration Synchronous Persistence** — `InfinispanClusterConfigTest` asserts `async().enabled() == false` on all defined store configurations.
- **Regression targets**:
    - `hestia/mneme-persistence` suite (`FhirRestCacheStoreTest`, `OperationsRestCacheStoreTest`, `OperationsRestClientLoggingTest`).
    - `hestia/mneme-cluster` suite (`InfinispanClusterConfigTest`).
    - ArchUnit architecture test suite (`*ArchitectureTest`).

**Assumptions & Open Questions**

- **Significant Assumption: PersistenceException for failure signaling**:
    - *Chosen Option*: Throw or wrap in `org.infinispan.persistence.spi.PersistenceException` (or propagate underlying `CompletionException`).
    - *Rationale*: Standard Infinispan SPI contract for failing asynchronous persistence operations; avoids inventing custom exception hierarchies.
    - *Alternative*: Creating a custom `MnemePersistenceException`.
    - *Impact*: Low; aligns with standard Infinispan error handling.
- **Significant Assumption: Removal of Write-Behind across all 17 caches in `infinispan.xml`**:
    - *Chosen Option*: Remove `<write-behind>` from all 14 FHIR and 3 Operations caches.
    - *Rationale*: All 17 caches currently back authoritative application state into Mnemosyne relational databases and have no cache-only status.
    - *Alternative*: Keeping write-behind on some caches.
    - *Impact*: Synchronous persistence guarantees caller visibility of persistence failures.

**Delivery Steps**

**✓ Step 1: Remediate Adapter Write Failure Propagation and Store Unit Tests**  
Goal: Ensure `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` propagate failed REST results and exceptions as failed stages, verified by focused unit tests.  
Scope: `hestia/mneme-persistence` (`FhirRestCacheStore.java`, `OperationsRestCacheStore.java`, `FhirRestCacheStoreTest.java`, `OperationsRestCacheStoreTest.java`).  
Acceptance Criteria:
- [ ] `FhirRestCacheStore.write(...)` returns a failed `CompletionStage` when `restClient.saveResourceJson(...)` returns `false` or completes exceptionally.
- [ ] `OperationsRestCacheStore.write(...)` returns a failed `CompletionStage` when `restClient.saveResourceJson(...)` returns `false` or completes exceptionally.
- [ ] Terminal `.exceptionally(error -> return null)` is removed from both `write(...)` implementations.
- [ ] `FhirRestCacheStoreTest` has tests verifying: successful write (200 / true), failed write (false / 500), and exceptional write completion.
- [ ] `OperationsRestCacheStoreTest` has tests verifying: successful write (200 / true), failed write (false / 500), and exceptional write completion.
- [ ] All existing store tests (load, delete, coordinate resolution, publish) continue to pass.
  Verification: `mvn test -pl hestia/mneme-persistence -am` → green

**✓ Step 2: Configure Synchronous Store Persistence and Validate Full Suite & Architecture**  
Goal: Remove asynchronous write-behind configuration from Infinispan cache definitions and verify cluster configuration and architectural invariants.  
Scope: `hestia/mneme-cluster` (`infinispan.xml`, `InfinispanClusterConfigTest.java`), full test suite, and architecture verification.  
Acceptance Criteria:
- [ ] `<write-behind modification-queue-size="1024" />` removed from all 14 FHIR and 3 Operations replicated caches in `infinispan.xml`.
- [ ] `InfinispanClusterConfigTest` builds store configurations without `.async().enable()` and asserts `async().enabled() == false`.
- [ ] `InfinispanClusterConfigTest` passes and verifies cache initialization for all representative caches.
- [ ] Architecture tests in `paradeigma-test` pass without regression.
- [ ] Final summary report includes all 10 required reporting items from the parent task specification.
  Verification: `mvn test -pl hestia/mneme-cluster,paradeigma/paradeigma-test -am -Dtest="InfinispanClusterConfigTest,*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green



**Summary**
- Completed Task 07 Step 02: Mneme cache-store writes no longer convert failed Mnemosyne persistence into successful completion, and authoritative REST-backed caches no longer use async write-behind.
- Confirmed before modification that both `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` logged `false` results and swallowed exceptional completions; async write-behind prevented originating cache operations from observing downstream failures.

**Changes**
- Updated `FhirRestCacheStore` and `OperationsRestCacheStore` so REST `false` results now fail with `PersistenceException`, exceptional REST completions remain exceptional, and null entry/key/value no-op behavior stays unchanged; no Petasos, Mnemosyne JPA/repository, Kleio, or local-memory fallback paths were redesigned or modified.
- Removed `<write-behind modification-queue-size="1024" />` from all 17 authoritative REST-backed caches in `hestia/mneme-cluster/src/main/resources/infinispan.xml` (14 FHIR + 3 Operations) as the minimum correctness change; updated `InfinispanClusterConfigTest` to assert synchronous store configuration and validate production XML.
- Expanded `FhirRestCacheStoreTest` and `OperationsRestCacheStoreTest` to prove success, explicit `false` rejection, and exceptional completion behavior; no remaining false-success behavior was changed outside these write adapters, and broader write-path redesign implications are deferred to Task 08.

**Verification**
- Passed: `mvn test -pl hestia/mneme-persistence -am`, `mvn test -pl hestia/mneme-cluster -am -Dtest="InfinispanClusterConfigTest" -Dsurefire.failIfNoSpecifiedTests=false`, and `mvn test -pl hestia/mneme-cluster,paradeigma/paradeigma-test -am -Dtest="InfinispanClusterConfigTest,*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`.
- Findings for the required report: current behavior was confirmed pre-change; async write-behind did prevent propagation; exact changes were limited to the two cache-store `write(...)` methods, the 17 XML cache entries, `InfinispanClusterConfigTest`, and focused store tests; local fallback behavior stayed untouched; Petasos/Mnemosyne/Kleio architecture was not redesigned.

**Notes**
- Remaining deliberately deferred items: final authoritative Clinical write-path redesign, direct BEFE→Mnemosyne writes, cache-aside reads, Clinical search changes, and any other false-success/local-fallback behavior outside the two Mneme write adapters addressed here.

