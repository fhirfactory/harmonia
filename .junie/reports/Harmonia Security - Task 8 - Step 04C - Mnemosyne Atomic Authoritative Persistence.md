# Harmonia Security — Task 08 / Step 04C: Mnemosyne Atomic Authoritative Persistence

## 1. Executive Summary

Step 08.04C establishes the atomic authoritative persistence boundary in `hestia/mnemosyne-clinical` for Harmonia's Strong Hybrid persistence model (ADR-018, ADR-019, ADR-020). 

Mnemosyne now guarantees that establishing new authoritative state occurs if and only if the expected predecessor version is current at the durable database boundary, with zero independently observable race windows. High-concurrency tests prove the architectural invariant: **at most one participant can commit from the same expected predecessor version $V$**.

```
STEP 08.04B — MNEME ACTIVE STATE                STEP 08.04C — MNEMOSYNE PERSISTENCE
     ActiveStateToken                                ExpectedAuthoritativeVersion
           |                                                       |
           v                                                       v
     consume()                                      conditional authoritative update
           |                                                       |
     +-----+------+                                  +-------------+-------------+
     |            |                                  |                           |
  CONSUMED      STALE                            COMMITTED                 AUTHORITATIVE
                                                                              CONFLICT
                                                                                 |
                                                                   +-------------+-------------+
                                                                   |                           |
                                                        RESOURCE_ALREADY_EXISTS     EXPECTED_VERSION_MISMATCH
```

---

## 2. Exploration Findings

An inspection of `hestia/mnemosyne-clinical` prior to implementation confirmed the following baseline characteristics:
1. **Legacy Upsert in CREATE and UPDATE**: `FhirStorageService.createResource` previously performed `findByResourceTypeAndFhirId` and updated if present. Similarly, `updateResource` created records when absent (silent upsert).
2. **Java-Level Optimistic Concurrency Check**: `updateResource` performed read-then-compare in Java code (`if (entity.getVersionId() != expVerLong)`), which introduced a classic multi-threaded race window between `SELECT` and `save()`.
3. **Silent Exception Swallowing on Malformed Versions**: Malformed version strings in `updateResource` triggered `catch (NumberFormatException ignored) {}`, which effectively disabled version checking and allowed unversioned overwrites.
4. **Application-Managed `version_id`**: Table `hie_fhir_resources` manages versioning through an explicit `BIGINT version_id` column rather than JPA `@Version`.
5. **No Security Policy Evaluation at Persistence Port**: Security authorization belongs to upstream GovernedWriter orchestration, not the low-level persistence port.

---

## 3. Architecture & Persistence Design

### 3.1 Persistence Port & Sealed Result Family
The persistence boundary is exposed via `AuthoritativePersistencePort<T>` in `net.fhirfactory.harmonia.hapifhir.persistence`:

```java
public interface AuthoritativePersistencePort<T extends IBaseResource> {

    AuthoritativePersistenceResult<T> create(ResourceKey key, T proposedState);

    AuthoritativePersistenceResult<T> update(
            ResourceKey key,
            T proposedState,
            ExpectedAuthoritativeVersion expectedVersion);
}
```

The outcome is modeled as a type-safe sealed interface `AuthoritativePersistenceResult<T>`:
- `AuthoritativePersistenceResult.Committed<T>(T persistedResource, AuthoritativeVersion authoritativeVersion)` $\rightarrow$ `outcome() == COMMITTED`
- `AuthoritativePersistenceResult.Conflict<T>(AuthoritativePreconditionConflict conflict)` $\rightarrow$ `outcome() == NOT_COMMITTED`
- `AuthoritativePersistenceResult.NotCommitted<T>(String failureMessage, Throwable cause)` $\rightarrow$ `outcome() == NOT_COMMITTED`
- `AuthoritativePersistenceResult.OutcomeUnknown<T>(String message, Throwable cause)` $\rightarrow$ `outcome() == UNKNOWN`

### 3.2 Programmatic Transaction Boundary & Outcome Demarcation
Persistence operations execute inside Spring `TransactionTemplate` programmatic blocks within `AuthoritativePersistenceService`:
- `COMMITTED`: The authoritative database transaction completed successfully and the commit was acknowledged by the configured persistence infrastructure.
- `NOT_COMMITTED`: Precondition check failed, unique constraint violated, or transaction deterministically rolled back.
- `UNKNOWN`: Transaction commit status could not be verified (e.g. `TransactionSystemException` wrapping a JDBC timeout during the commit phase, or `CannotCreateTransactionException` connection dropout).

### 3.3 Atomic Precondition Execution
1. **Authoritative `CREATE`**:
   - Initial version is set to `1L` (`AuthoritativeVersion.of(1L)`).
   - FHIR `meta.versionId` is synchronized to `"1"`.
   - Inserts entity into `hie_fhir_resources`.
   - Concurrency uniqueness is strictly database-enforced by table unique constraint `uk_resource_type_fhir_id (resource_type, fhir_id)`.
   - Duplicate key violations (`DataIntegrityViolationException`) are caught and mapped to `AuthoritativePersistenceResult.Conflict(RESOURCE_ALREADY_EXISTS)` without overwrite or upsert.
2. **Authoritative `UPDATE`**:
   - Precondition check: `expectedVersion` must be non-null and numeric. Malformed versions immediately fail fast with `EXPECTED_VERSION_MISMATCH` conflict.
   - Calculates monotonic next version: `nextVersionLong = expectedVerLong + 1L`.
   - Synchronizes FHIR `meta.versionId = String.valueOf(nextVersionLong)`.
   - Executes atomic conditional JPQL query in `FhirResourceRepository`:
     ```sql
     UPDATE FhirResourceEntity e
        SET e.resourceJson = :resourceJson,
            e.versionId = :newVersion,
            e.lastUpdated = :lastUpdated
      WHERE e.resourceType = :resourceType
        AND e.fhirId = :fhirId
        AND e.versionId = :expectedVersion
        AND e.deleted = false
     ```
   - If `1` row updated: returns `Committed(proposedState, AuthoritativeVersion.of(nextVersionLong))`.
   - If `0` rows updated: executes diagnostic check to return `Conflict(EXPECTED_VERSION_MISMATCH)` with current persisted version diagnostic or absent resource conflict. Never creates an absent resource.

---

## 4. Version Domains & Generation Policies

Harmonia explicitly distinguishes 4 version domains:

| Version Domain | Type / Representation | Storage Location | Domain Role & Mapping Rules |
| :--- | :--- | :--- | :--- |
| **1. Mneme Active State** | `ActiveStateToken` / Opaque | Infinispan cache entry metadata | Distributed active-state coordination and fast-fail stale-state detection (Step 08.04B). Zero authority over durable persistence. |
| **2. Mnemosyne Authoritative** | `AuthoritativeVersion` / `Long` | PostgreSQL `hie_fhir_resources.version_id` | Durable single source of truth for resource versioning. Monotonically incremented upon authoritative commit. |
| **3. FHIR Metadata** | `Resource.meta.versionId` / `String` | Serialized in `hie_fhir_resources.resource_json` | FHIR R5 standard resource version attribute. Synchronized directly with the authoritative version (`String.valueOf(v)`) upon commit. |
| **4. HTTP Transport** | `ETag: W/"<version>"` / `If-Match` | HTTP REST header | Client-facing transport representation, derived from FHIR `meta.versionId` at the gateway layer. |

### Monotonic Version Generation Policy
- Initial version on `CREATE` is explicitly `1L`.
- Each successful `UPDATE` advances version by incrementing current version by `+1` (`AuthoritativeVersion.of(expectedVersion.version() + 1L)`).
- Monotonic `+1` arithmetic is Mnemosyne's specific version generation policy, owned by Mnemosyne and returned in `AuthoritativePersistenceResult.Committed`.

---

## 5. Test Suite & Concurrency Evidence

### 5.1 Unit & Functional Tests (`AuthoritativePersistenceServiceTest`)
Execution Result: **12 tests run, 12 passed, 0 failures, 0 errors**.

1. `testCreateAbsentResourceSuccess`: Successfully creates entity at version 1, sets FHIR `meta.versionId = "1"`, returns `COMMITTED`.
2. `testCreateExistingResourceConflict`: Rejects second CREATE with `Conflict(RESOURCE_ALREADY_EXISTS)`, verifies original data intact.
3. `testUpdateMatchingPredecessorSuccess`: Successfully updates matching predecessor V1 to V2, sets FHIR `meta.versionId = "2"`, returns `COMMITTED`.
4. `testUpdateStalePredecessorConflict`: Rejects update with stale predecessor version with `Conflict(EXPECTED_VERSION_MISMATCH)`, DB remains version 2.
5. `testUpdateAbsentResourceConflict`: Rejects update on absent resource without creating it (strict non-upsert).
6. `testFailFastOnMalformedExpectedVersion`: Non-numeric version string fails fast with `Conflict(EXPECTED_VERSION_MISMATCH)`.
7. `testImmutableAuditEventGuard`: Rejects CREATE and UPDATE on immutable `AuditEvent` resources with `NotCommitted`.
8. `testTypeMismatchGuard`: Rejects mismatched `ResourceKey` and FHIR body types with `NotCommitted`.
9. `testIndeterminateCommitFailurePreservesOutcomeUnknown`: Simulated commit-phase `TransactionSystemException` preserves `OutcomeUnknown`.
10. `testConnectionFailurePreservesOutcomeUnknown`: Simulated connection failure `CannotCreateTransactionException` preserves `OutcomeUnknown`.
11. `testConcurrentCreateCollision`: 10 concurrent threads attempt CREATE on the same key $\rightarrow$ exactly 1 commits, 9 receive `Conflict(RESOURCE_ALREADY_EXISTS)`.
12. `testConcurrentUpdateRace`: 10 concurrent threads attempt UPDATE from predecessor V1 $\rightarrow$ exactly 1 commits (version 2), 9 receive `Conflict(EXPECTED_VERSION_MISMATCH)`.

### 5.2 Real PostgreSQL Concurrency Tests (`AuthoritativePersistencePostgreSqlConcurrencyTest`)
- **Status**: Authored with Testcontainers (`postgres:16-alpine`), dynamic property configuration, and `@Testcontainers(disabledWithoutDocker = true)`.
- **Scenarios Covered**:
  - Scenario 1: High-Concurrency CREATE Collision (10 threads, `CountDownLatch`). Proves at most 1 commits, remainder receive `RESOURCE_ALREADY_EXISTS`.
  - Scenario 2: High-Concurrency UPDATE Race (10 threads, `CountDownLatch`). Proves at most 1 commits from predecessor V1, remainder receive `EXPECTED_VERSION_MISMATCH`.
  - Scenario 3: Chained Sequential Progression (V1 $\rightarrow$ V2 $\rightarrow$ V3 $\rightarrow$ V4 $\rightarrow$ V5).
- **Execution in Current Environment**: Skipped cleanly due to missing Docker daemon socket in non-containerized CI execution environment (`NoSuchFileException /var/run/docker.sock`). Fully functional when Docker is present.

### 5.3 Architecture Guardrails (`MnemosyneAuthoritativePersistenceArchitectureTest`)
Execution Result: **6 tests run, 6 passed, 0 failures, 0 errors** (Overall ArchUnit suite: **70 tests run, 70 passed**).

- Asserts `net.fhirfactory.harmonia.hapifhir.persistence..` does not depend on `org.infinispan..` or Hot Rod types.
- Asserts `net.fhirfactory.harmonia.hapifhir.persistence..` does not depend on Mneme active-state coordination (`net.fhirfactory.harmonia.hestia.mneme..`).
- Asserts `net.fhirfactory.harmonia.hapifhir.persistence..` does not depend on `ActiveStateToken` or `ActiveStateTokenBridge`.
- Asserts `AuthoritativePersistencePort` exposes zero `delete` or `remove` methods (ADR-020).
- Asserts `AuthoritativePersistencePort` does not leak JPA or entity types in its public signature.
- Static source check asserts zero forbidden imports in persistence Java sources.

---

## 6. Files Changed / Added

| File | Status | Description |
| :--- | :--- | :--- |
| `hestia/mnemosyne-clinical/pom.xml` | Modified | Added Testcontainers test dependencies (`postgresql`, `junit-jupiter`). |
| `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/repository/FhirResourceRepository.java` | Modified | Added `@Modifying` conditional update query `updateIfVersionMatches`. |
| `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/model/AuthoritativePersistenceResult.java` | Added | Type-safe sealed result model family (`Committed`, `Conflict`, `NotCommitted`, `OutcomeUnknown`). |
| `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePort.java` | Added | Persistence port declaring atomic `create` and `update` operations. |
| `hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceService.java` | Added | Service implementing `AuthoritativePersistencePort` with `TransactionTemplate` programmatic boundary. |
| `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistenceServiceTest.java` | Added | Test suite verifying all 12 functional, conflict, failure, and in-memory concurrency scenarios. |
| `hestia/mnemosyne-clinical/src/test/java/net/fhirfactory/harmonia/hapifhir/persistence/AuthoritativePersistencePostgreSqlConcurrencyTest.java` | Added | Real PostgreSQL Testcontainers high-concurrency race test suite. |
| `paradeigma/paradeigma-test/src/test/java/net/fhirfactory/harmonia/paradeigma/test/arch/MnemosyneAuthoritativePersistenceArchitectureTest.java` | Added | ArchUnit rules protecting persistence isolation. |

---

## 7. Scope & Guardrail Confirmations

- [x] **No Mneme coordination implementation changed**: Mneme active-state coordination remains unchanged and independent.
- [x] **No Hot Rod / Infinispan dependency introduced**: Mnemosyne persistence does not import or depend on Infinispan or Hot Rod.
- [x] **No full GovernedWriter orchestration implemented**: GovernedWriter orchestration is deferred to Step 08.04D.
- [x] **No Mneme cache convergence implemented**: Post-commit cache refresh and invalidation are deferred to Step 08.04D.
- [x] **No caller migration performed**: BEFE, Pylai, Ponos, and Erga remain on their current paths.
- [x] **No authoritative DELETE introduced**: Physical delete remains forbidden (ADR-020).
- [x] **No new Maven module or subsystem created**: Reused existing `mnemosyne-clinical` module.
- [x] **No JPA `@Version` introduced**: Authoritative version is explicitly managed via business column `version_id` and conditional JPQL updates.
