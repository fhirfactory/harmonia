---
sessionId: session-260925-072749-t7cl
---

# Requirements

### Goal & Outcome
Enforce the authoritative state boundary established in ADR-018 by eliminating false durable-success semantics in Mneme (`FhirRestCacheStore` and `OperationsRestCacheStore`). A failed Mnemosyne write (explicit rejection, HTTP error, or transport crash) must fail the persistence stage and propagate synchronously to originating cache operations rather than being swallowed as a successful completion.

### Scope
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

### Done When
- `FhirRestCacheStore.write(...)` and `OperationsRestCacheStore.write(...)` complete exceptionally when REST calls return `false` or throw exceptions.
- Neither adapter swallows errors via terminal `.exceptionally(err -> return null)` on write operations.
- `infinispan.xml` and `InfinispanClusterConfigTest` have async write-behind disabled for all 17 authoritative persistence caches.
- Focused store tests and configuration tests pass, proving failure propagation and synchronous configuration.
- ArchUnit architecture tests pass (`*ArchitectureTest`).

# Technical Design

### Decisions
- **Decision: Propagate failed stages using standard `PersistenceException` (or failed `CompletionStage`) / not swallowing via `.exceptionally(...)`**
  - *Rationale*: Infinispan `NonBlockingStore.write(...)` requires a failed `CompletionStage` to signal persistence failure to the cache engine. Using standard `org.infinispan.persistence.spi.PersistenceException` or failing the future preserves standard Infinispan error propagation without introducing unnecessary custom exception hierarchies.
- **Decision: Remove async write-behind from `infinispan.xml` for all 17 REST-backed replicated caches / not retaining async write-behind**
  - *Rationale*: Infinispan's `AsyncNonBlockingStore` (write-behind) buffers writes in an in-memory queue and acknowledges the caller immediately. If write-behind is enabled, cache mutators cannot observe downstream store failures regardless of adapter logic. Switching to synchronous cache-store persistence aligns with ADR-018's current-architecture correctness requirements.

### Approach & Touches
- **Adapter remediation**:
  - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/FhirRestCacheStore.java`: In `write(...)`, map `Boolean` result from `restClient.saveResourceJson(...)` via `thenCompose(...)` or `thenApply(...)` such that `false` returns `CompletableFuture.failedStage(new PersistenceException(...))`; remove `.exceptionally(...)` that converts errors to `null`.
  - `hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/store/OperationsRestCacheStore.java`: Apply identical failure propagation in `write(...)`.
- **Configuration & Cluster remediation**:
  - `hestia/mneme-cluster/src/main/resources/infinispan.xml`: Remove `<write-behind modification-queue-size="1024" />` from all 14 FHIR replicated caches (`person-cache`, `relatedperson-cache`, `practitioner-cache`, `practitionerrole-cache`, `organization-cache`, `location-cache`, `healthcareservice-cache`, `group-cache`, `provenance-cache`, `auditevent-cache`, `consent-cache`, `task-cache`, `communication-cache`, `documentreference-cache`) and all 3 Operations caches (`tasksequence-cache`, `messagequeue-cache`, `modulestatus-cache`).
  - `hestia/mneme-cluster/src/test/java/net/fhirfactory/harmonia/infinispan/InfinispanClusterConfigTest.java`: Remove `.async().enable().modificationQueueSize(1024)` from both builder configurations; add assertions verifying `config.async().enabled()` is `false`.

### Nuances / Risks / Corners
- `write(...)` must continue handling `null` entry/key/value safely (returning `CompletableFuture.completedFuture(null)`).
- `delete(...)`, `containsKey(...)`, and `publish*` methods should not be regressed.
- WireMock and Mockito in tests must verify that `CompletionException` / `ExecutionException` contains the underlying failure cause when `.get()` or `.join()` is called on failed stages.

### Suspected Areas (Defect Root Causes)
1. `FhirRestCacheStore.java:110-121`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
2. `OperationsRestCacheStore.java:114-126`: `thenAccept` logs `false` without failing, and `.exceptionally(...)` returns `null`.
3. `infinispan.xml:70,85,100,115,130,145,160,175,189,203,217,231,245,259,273,287,301`: `<write-behind .../>` decouples store execution from cache caller.

# Testing

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

# Assumptions & Open Questions

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

# Delivery Steps

### ✓ Step 1: Remediate Adapter Write Failure Propagation and Store Unit Tests
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

### ✓ Step 2: Configure Synchronous Store Persistence and Validate Full Suite & Architecture
Goal: Remove asynchronous write-behind configuration from Infinispan cache definitions and verify cluster configuration and architectural invariants.
Scope: `hestia/mneme-cluster` (`infinispan.xml`, `InfinispanClusterConfigTest.java`), full test suite, and architecture verification.
Acceptance Criteria:
- [ ] `<write-behind modification-queue-size="1024" />` removed from all 14 FHIR and 3 Operations replicated caches in `infinispan.xml`.
- [ ] `InfinispanClusterConfigTest` builds store configurations without `.async().enable()` and asserts `async().enabled() == false`.
- [ ] `InfinispanClusterConfigTest` passes and verifies cache initialization for all representative caches.
- [ ] Architecture tests in `paradeigma-test` pass without regression.
- [ ] Final summary report includes all 10 required reporting items from the parent task specification.
Verification: `mvn test -pl hestia/mneme-cluster,paradeigma/paradeigma-test -am -Dtest="InfinispanClusterConfigTest,*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false` → green